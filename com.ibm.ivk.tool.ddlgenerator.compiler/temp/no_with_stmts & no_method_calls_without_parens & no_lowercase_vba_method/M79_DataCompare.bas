 Attribute VB_Name = "M79_DataCompare"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colCheckName = 2
 Private Const colSection = colCheckName + 1
 Private Const colEntityName = colSection + 1
 Private Const colEntityType = colEntityName + 1
 Private Const colDataPoolId = colEntityType + 1
 Private Const colRefDataPoolId = colDataPoolId + 1
 Private Const colAttrName = colRefDataPoolId + 1
 Private Const colCompareMode = colAttrName + 1
 Private Const colSequenceNumber = colCompareMode + 1
 
 Private Const processingStep = 2
 
 Private Const firstRow = 3
 
 Private Const sheetName = "DComp"
 
 Private Const cmpModeLeftNotRight = "<--"
 Private Const cmpModeRightNotLeft = "-->"
 Private Const cmpModeDiffer = "<->"
 Private Const cmpModeDupLeft = "<##"
 Private Const cmpModeDupRight = "##>"
 
 Global g_dComps As DCompDescriptors
 
 
 Private Sub readSheet()
   initDCompDescriptors(g_dComps)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colSection) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).checkName = Trim(thisSheet.Cells(thisRow, colCheckName))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).entityName = Trim(thisSheet.Cells(thisRow, colEntityName))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).cType = getAttrContainerType(Trim(thisSheet.Cells(thisRow, colEntityType)))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).dataPoolId = getInteger(thisSheet.Cells(thisRow, colDataPoolId), -1)
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).refDataPoolId = getInteger(thisSheet.Cells(thisRow, colRefDataPoolId), -1)
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).attrName = Trim(thisSheet.Cells(thisRow, colAttrName))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).compareMode = getDataCompareMode(thisSheet.Cells(thisRow, colCompareMode))
       g_dComps.descriptors(allocDCompDescriptorIndex(g_dComps)).sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNumber))
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getDComps()
   If (g_dComps.numDescriptors = 0) Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetDComps()
  g_dComps.numDescriptors = 0
 End Sub
 
 
 Sub genDCompSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtPdm Then
     genDCompSupportDdlByType(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportLrt Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             genDCompSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
           End If
          Next thisOrgIndex
        End If
      Next thisPoolIndex
   End If
 End Sub
 
 
 Private Sub genDCompSupportDdlByType( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportSstCheck Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataCheck, processingStep, ddlType, , , , phaseDataCompare, ldmIterationGlobal)
 
   ' ####################################################################################################################
   ' #    SP comparing data in tables / views
   ' ####################################################################################################################
 
   Dim numKeyColumns As Integer
   numKeyColumns = 25
   Dim maxKeyValLength As Integer
   maxKeyValLength = 50
   Dim keyColPrefix As String
   keyColPrefix = "keyCol"
   Dim keyValPrefix As String
   keyValPrefix = "keyVal"

   Dim qualProcNameDataCompare As String
   qualProcNameDataCompare = genQualProcName(g_sectionIndexDataCheck, spnDataChkCompare, ddlType)

   Dim tempTabNameStmntCompare As String
   tempTabNameStmntCompare = tempTabNameStatement & "Compare"

   printSectionHeader("SP comparing data in tables / views", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataCompare
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the (set of) compare-checks to execute")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names that apply")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables / views that apply")
   genProcParm(fileNo, "OUT", "compareCount_out", "INTEGER", True, "number of comparisons executed")
   genProcParm(fileNo, "OUT", "diffCount_out", "INTEGER", False, "number of 'differences' identified")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 2"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL")
   genVarDecl(fileNo, "v_firstCol", "VARCHAR(80)", "NULL")
   genVarDecl(fileNo, "v_compareMode", "CHAR(3)", "NULL")
   genVarDecl(fileNo, "v_colNo", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_selColList1", "VARCHAR(2500)", "NULL")
   genVarDecl(fileNo, "v_selColList2", "VARCHAR(2500)", "NULL")
   genVarDecl(fileNo, "v_grpColList1", "VARCHAR(2500)", "NULL")
   genVarDecl(fileNo, "v_grpColList2", "VARCHAR(2500)", "NULL")
   genVarDecl(fileNo, "v_tgtColList", "VARCHAR(2500)", "NULL")
   genVarDecl(fileNo, "v_joinCond", "VARCHAR(8000)", "NULL")
   genVarDecl(fileNo, "v_compareCond", "VARCHAR(8000)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 16000, True, , , , "Compare")
 
   Dim dbTypeColname As String
   dbTypeColname = getDbDataTypeByDomainName(dxnPdmColumnName, dnPdmColumnName)
   Dim qualTabNameTempCompareResults As String
   qualTabNameTempCompareResults = "SESSION.CompareResults"
   genProcSectionHeader(fileNo, "temporary table for comparison result")
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); qualTabNameTempCompareResults
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "schemaName    "; getDbDataTypeByDomainName(dxnDbSchemaName, dnDbSchemaName); ","
   Print #fileNo, addTab(2); "refSchemaName "; getDbDataTypeByDomainName(dxnDbSchemaName, dnDbSchemaName); ","
   Print #fileNo, addTab(2); "objName       "; getDbDataTypeByDomainName(dxnPdmTableName, dnPdmTableName); ","
   Print #fileNo, addTab(2); "mode          CHAR(3),"
   Dim k As Integer
   For k = 1 To numKeyColumns
     Print #fileNo, addTab(2); keyColPrefix; paddRight(CStr(k), 8); dbTypeColname; ","
     Print #fileNo, addTab(2); keyValPrefix; paddRight(CStr(k), 8); "VARCHAR("; CStr(maxKeyValLength); ")"; IIf(k < numKeyColumns, ",", "")
   Next k

   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNo, 1, True, True, True)
 
   genSpLogProcEnter(fileNo, qualProcNameDataCompare, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "compareCount_out", "diffCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET compareCount_out = 0;"
   Print #fileNo, addTab(1); "SET diffCount_out    = 0;"
 
   genProcSectionHeader(fileNo, "loop over tables / views to compare")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100)) AS c_cmpSchemaName,"
   Print #fileNo, addTab(3); "CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100)) AS c_refSchemaName,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") AS c_entitySection,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_entityName,"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " AS c_entityType,"
   Print #fileNo, addTab(3); "C."; g_anAccessModeId; " AS c_dataPoolid,"
   Print #fileNo, addTab(3); "C.REFACCESSMODE_ID AS c_refDataPoolid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "determine list of key-columns", 2, True)
   Print #fileNo, addTab(2); "SET v_colNo       = 0;"
   Print #fileNo, addTab(2); "SET v_firstCol    = '';"
   Print #fileNo, addTab(2); "SET v_selColList1 = '';"
   Print #fileNo, addTab(2); "SET v_selColList2 = '';"
   Print #fileNo, addTab(2); "SET v_grpColList1 = '';"
   Print #fileNo, addTab(2); "SET v_grpColList2 = '';"
   Print #fileNo, addTab(2); "SET v_tgtColList  = '';"
   Print #fileNo, addTab(2); "SET v_joinCond    = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A."; g_anAcmAttributeName; " AS c_attributeName,"
   Print #fileNo, addTab(4); "(CASE WHEN COL.TYPENAME IN ('VARCHAR', 'CHARACTER') THEN 1 ELSE 0 END) AS c_isChar,"
   Print #fileNo, addTab(4); "(CASE WHEN COL.CODEPAGE = 0                         THEN 1 ELSE 0 END) AS c_isBinary"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDataComparisonAttribute; " A"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS COL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "UPPER(COL.COLNAME) = UPPER(A."; g_anAcmAttributeName; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "UPPER(COL.TABNAME) = UPPER(A."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "UPPER(COL.TABSCHEMA) = c_cmpSchemaName"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "UPPER(A."; g_anAcmEntitySection; ") = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UPPER(A."; g_anAcmEntityName; ") = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.COMPAREMODE = 'K'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(COL.TYPENAME, '') NOT IN ('BLOB', 'CLOB')"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "A.SEQUENCENO"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF v_firstCol = '' THEN"
   Print #fileNo, addTab(4); "SET v_firstCol = UPPER(c_attributeName);"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "SET v_colNo = v_colNo + 1;"
   Print #fileNo, addTab(3); "IF c_isChar = 1 THEN"
   Print #fileNo, addTab(4); "IF c_isBinary = 1 THEN"
   Print #fileNo, addTab(5); "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(HEX(T1.' || UPPER(c_attributeName) || '),"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(5); "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(HEX(T2.' || UPPER(c_attributeName) || '),"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(4); "ELSE"
   Print #fileNo, addTab(5); "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(T1.' || UPPER(c_attributeName) || ',"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(5); "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(T2.' || UPPER(c_attributeName) || ',"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(RTRIM(CHAR(T1.' || UPPER(c_attributeName) || ')),"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(4); "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(RTRIM(CHAR(T2.' || UPPER(c_attributeName) || ')),"; CStr(maxKeyValLength); ")';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "SET v_grpColList1 = v_grpColList1 || (CASE v_grpColList1 WHEN '' THEN '' ELSE ',' END) || 'T1.' || UPPER(c_attributeName);"
   Print #fileNo, addTab(3); "SET v_grpColList2 = v_grpColList2 || (CASE v_grpColList2 WHEN '' THEN '' ELSE ',' END) || 'T2.' || UPPER(c_attributeName);"
   Print #fileNo, addTab(3); "SET v_joinCond    = v_joinCond    || (CASE v_joinCond    WHEN '' THEN '' ELSE ' AND ' END) || '(T1.' || UPPER(c_attributeName) || '=T2.'   || UPPER(c_attributeName) || ')';"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_colNo <= "; CStr(numKeyColumns); " THEN"
   Print #fileNo, addTab(4); "SET v_tgtColList = v_tgtColList || ',"; keyColPrefix; "' || RTRIM(CHAR(v_colNo)) || ',"; keyValPrefix; "' || RTRIM(CHAR(v_colNo));"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
   genProcSectionHeader(fileNo, "determine list of compare-columns", 2)
   Print #fileNo, addTab(2); "SET v_compareCond = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A."; g_anAcmAttributeName; " AS c_attributeName,"
   Print #fileNo, addTab(4); "(CASE WHEN COL.TYPENAME IN ('BLOB', 'CLOB') THEN 1 ELSE 0 END) AS c_isLob"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDataComparisonAttribute; " A"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS COL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "COL.COLNAME = UPPER(A."; g_anAcmAttributeName; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "COL.TABNAME = UPPER(A."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "COL.TABSCHEMA = c_cmpSchemaName"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "UPPER(A."; g_anAcmEntitySection; ") = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UPPER(A."; g_anAcmEntityName; ") = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.COMPAREMODE = 'C'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "A.SEQUENCENO"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF c_isLob = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "SET v_compareCond = v_compareCond || (CASE v_compareCond WHEN '' THEN '' ELSE ' OR ' END) || '(T1.' || UPPER(c_attributeName) || '<>T2.'   || UPPER(c_attributeName) || ')';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader(fileNo, "loop over compare-modes and determine compare-statements", 2)
   Print #fileNo, addTab(2); "SET v_compareMode = '"; cmpModeLeftNotRight; "';"
   Print #fileNo, addTab(2); "WHILE"
   Print #fileNo, addTab(3); "v_compareMode IS NOT NULL"
   Print #fileNo, addTab(2); "DO"

   genProcSectionHeader(fileNo, "identify records in tables depending on the resp. compare mode", 3, True)
   Print #fileNo, addTab(3); "IF v_compareMode IN ('"; cmpModeLeftNotRight; "') THEN"
 
   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||"
   Print #fileNo, addTab(5); "' FROM ' ||"
   Print #fileNo, addTab(6); "c_cmpSchemaName || '.' || c_entityName || ' T1' ||"
   Print #fileNo, addTab(5); "' ' || (CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'LEFT OUTER' WHEN '"; cmpModeRightNotLeft; "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||"
   Print #fileNo, addTab(6); "c_refSchemaName || '.' || c_entityName || ' T2' ||"
   Print #fileNo, addTab(5); "' ON ' ||"
   Print #fileNo, addTab(6); "v_joinCond ||"
   Print #fileNo, addTab(5); "' WHERE ' ||"
   Print #fileNo, addTab(6); "(CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'T2.' || v_firstCol || ' IS NULL' ELSE 'T1.' || v_firstCol || ' IS NULL' END);"
 
   Print #fileNo, addTab(3); "ELSEIF v_compareMode IN ('"; cmpModeRightNotLeft; "') THEN"

   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList2 ||"
   Print #fileNo, addTab(5); "' FROM ' ||"
   Print #fileNo, addTab(6); "c_cmpSchemaName || '.' || c_entityName || ' T1' ||"
   Print #fileNo, addTab(5); "' ' || (CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'LEFT OUTER' WHEN '"; cmpModeRightNotLeft; "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||"
   Print #fileNo, addTab(6); "c_refSchemaName || '.' || c_entityName || ' T2' ||"
   Print #fileNo, addTab(5); "' ON ' ||"
   Print #fileNo, addTab(6); "v_joinCond ||"
   Print #fileNo, addTab(5); "' WHERE ' ||"
   Print #fileNo, addTab(6); "(CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'T2.' || v_firstCol || ' IS NULL' ELSE 'T1.' || v_firstCol || ' IS NULL' END);"
 
   Print #fileNo, addTab(3); "ELSEIF v_compareMode IN ('"; cmpModeDiffer; "') THEN"
   Print #fileNo, addTab(4); "IF v_compareCond = '' THEN"
   Print #fileNo, addTab(5); "SET v_stmntTxt = NULL;"
   Print #fileNo, addTab(4); "ELSE"
   Print #fileNo, addTab(5); "SET v_stmntTxt ="
   Print #fileNo, addTab(6); "'SELECT ' ||"
   Print #fileNo, addTab(7); "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||"
   Print #fileNo, addTab(6); "' FROM ' ||"
   Print #fileNo, addTab(7); "c_cmpSchemaName || '.' || c_entityName || ' T1' ||"
   Print #fileNo, addTab(6); "' ' || (CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'LEFT OUTER' WHEN '"; cmpModeRightNotLeft; "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||"
   Print #fileNo, addTab(7); "c_refSchemaName || '.' || c_entityName || ' T2' ||"
   Print #fileNo, addTab(6); "' ON ' ||"
   Print #fileNo, addTab(7); "v_joinCond ||"
   Print #fileNo, addTab(6); "' WHERE ' ||"
   Print #fileNo, addTab(7); "(CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN 'T2.' || v_firstCol || ' IS NULL' WHEN '"; cmpModeRightNotLeft; "' THEN 'T1.' || v_firstCol || ' IS NULL' ELSE v_compareCond END);"
   Print #fileNo, addTab(4); "END IF;"
 
   Print #fileNo, addTab(3); "ELSEIF v_compareMode IN ('"; cmpModeDupLeft; "') THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||"
   Print #fileNo, addTab(5); "' FROM ' ||"
   Print #fileNo, addTab(6); "(CASE WHEN v_compareMode = '"; cmpModeDupLeft; "' THEN c_cmpSchemaName ELSE c_refSchemaName END) || '.' || c_entityName || ' T1' ||"
   Print #fileNo, addTab(5); "' GROUP BY ' ||"
   Print #fileNo, addTab(6); "v_grpColList1 ||"
   Print #fileNo, addTab(5); "' HAVING ' ||"
   Print #fileNo, addTab(6); "'COUNT(*) > 1';"
 
   Print #fileNo, addTab(3); "ELSEIF v_compareMode IN ('"; cmpModeDupRight; "') THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList2 ||"
   Print #fileNo, addTab(5); "' FROM ' ||"
   Print #fileNo, addTab(6); "(CASE WHEN v_compareMode = '"; cmpModeDupLeft; "' THEN c_cmpSchemaName ELSE c_refSchemaName END) || '.' || c_entityName || ' T2' ||"
   Print #fileNo, addTab(5); "' GROUP BY ' ||"
   Print #fileNo, addTab(6); "v_grpColList2 ||"
   Print #fileNo, addTab(5); "' HAVING ' ||"
   Print #fileNo, addTab(6); "'COUNT(*) > 1';"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_stmntTxt IS NOT NULL THEN"
 
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 5, True)
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStmntCompare
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "statement"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(6); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "v_stmntTxt"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "retrieve comparison result", 5, True)
   Print #fileNo, addTab(5); "SET v_stmntTxt ="
   Print #fileNo, addTab(6); "'INSERT INTO ' ||"
   Print #fileNo, addTab(7); "'"; qualTabNameTempCompareResults; "' ||"
   Print #fileNo, addTab(6); "'(' ||"
   Print #fileNo, addTab(7); "'schemaName,refSchemaName,objName,mode' || v_tgtColList ||"
   Print #fileNo, addTab(6); "') ' || v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader(fileNo, "count number of differences found", 5)
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(5); "SET diffCount_out = diffCount_out + v_rowCount;"
   Print #fileNo, addTab(4); "END IF;"
 
   genProcSectionHeader(fileNo, "keep track of number of comparisons executed", 4)
   Print #fileNo, addTab(4); "SET compareCount_out = compareCount_out + 1;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader(fileNo, "select next compare-mode", 3)
   Print #fileNo, addTab(3); "SET v_compareMode = (CASE v_compareMode WHEN '"; cmpModeLeftNotRight; "' THEN '"; cmpModeRightNotLeft; "' "; _
                             "WHEN '"; cmpModeRightNotLeft; "' THEN '"; cmpModeDiffer; "' "; _
                             "WHEN '"; cmpModeDiffer; "' THEN '"; cmpModeDupLeft; "' "; _
                             "WHEN '"; cmpModeDupLeft; "' THEN '"; cmpModeDupRight; "' "; _
                             "ELSE NULL END);"

   Print #fileNo, addTab(2); "END WHILE;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntCompare
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "*"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameTempCompareResults
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "schemaName ASC,"
   Print #fileNo, addTab(5); "refSchemaName ASC,"
   Print #fileNo, addTab(5); "objName ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataCompare, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "compareCount_out", "diffCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader("SP comparing data in tables / views", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataCompare
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "OUT", "compareCount_out", "INTEGER", True, "number of comparisons executed")
   genProcParm(fileNo, "OUT", "diffCount_out", "INTEGER", False, "number of 'differences' identified")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 2"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcNameDataCompare, ddlType, , "mode_in", "compareCount_out", "diffCount_out")
 
   Print #fileNo, addTab(1); "CALL "; qualProcNameDataCompare; "(mode_in, NULL, NULL, NULL, compareCount_out, diffCount_out);"
 
   genSpLogProcExit(fileNo, qualProcNameDataCompare, ddlType, , "mode_in", "compareCount_out", "diffCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP cleaning up data in tables
   ' ####################################################################################################################
 
   Dim qualProcNameDataCleanup As String
   qualProcNameDataCleanup = genQualProcName(g_sectionIndexDataCheck, spnDataChkCleanup, ddlType)

   Dim tempTabNameStmntCleanup As String
   tempTabNameStmntCleanup = tempTabNameStatement & "Cleanup"

   printSectionHeader("SP cleaning up data in tables", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataCleanup
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the set of tables to cleanup")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names to cleanup")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables to cleanup")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "determines the data pool holding the tables to cleanup")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables cleaned up")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(120)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
 
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 120, True, True, True, , "Cleanup")
 
   genSpLogProcEnter(fileNo, qualProcNameDataCleanup, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "accessModeId_in", "tabCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNo, "loop over tables to cleanup")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE"
   Print #fileNo, addTab(4); "WHEN accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(4); "THEN CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "ELSE CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ") AS c_tabSchema,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "accessModeId_in = C.REFACCESSMODE_ID"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "determine DELETE-statement", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'DELETE FROM ' || c_tabSchema || '.' || c_tabName;"
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntCleanup
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute DELETE-statement", 3, True)
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntCleanup
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataCleanup, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "accessModeId_in", "tabCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP copying data to reference tables
   ' ####################################################################################################################
 
   Dim qualProcNameDataCp2RefTab As String
   qualProcNameDataCp2RefTab = genQualProcName(g_sectionIndexDataCheck, spnDataChkCp2RefTab, ddlType)

   Dim tempTabNameStmntCp2RefTab As String
   tempTabNameStmntCp2RefTab = tempTabNameStatement & "Cp2Ref"

   printSectionHeader("SP copying data to reference tables", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataCp2RefTab
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the set of tables to copy")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names to copy")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables to copy")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables copied")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows copied")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 200, True, True, True, , "Cp2Ref")
 
   genSpLogProcEnter(fileNo, qualProcNameDataCp2RefTab, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", "rowCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNo, "loop over tables to copy")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100)) AS c_cmpSchemaName,"
   Print #fileNo, addTab(3); "CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100)) AS c_refSchemaName,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "determine COPY-statement", 2, True)
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'INSERT INTO ' || c_refSchemaName || '.' || c_tabName || ' SELECT * FROM ' || c_cmpSchemaName || '.' || c_tabName;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntCp2RefTab
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute DELETE-statement", 3, True)
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntCp2RefTab
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataCp2RefTab, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", "rowCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP exporting data
   ' ####################################################################################################################
 
   Dim qualProcNameDataExport As String
   qualProcNameDataExport = genQualProcName(g_sectionIndexDataCheck, spnDataChkExport, ddlType)

   Dim tempTabNameStmntExport As String
   tempTabNameStmntExport = tempTabNameStatement & "Export"

   printSectionHeader("SP exporting data", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataExport
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the set of tables to export")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) determines the data pool holding the tables to export")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names to export")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables to export")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables exported")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_db2Release", g_dbtDbRelease, "NULL")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 200, True, True, True, , "Export")
 
   genSpLogProcEnter(fileNo, qualProcNameDataExport, ddlType, , "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
 
   genProcSectionHeader(fileNo, "Verify that this DB-Version supports Export")
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF v_db2Release < 9 and mode_in >= 1 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDataExport, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
   genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_db2Release))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "loop over tables to export")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE"
   Print #fileNo, addTab(4); "WHEN accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(4); "THEN CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "ELSE CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ") AS c_tabSchema,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "accessModeId_in = C.REFACCESSMODE_ID"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "determine EXPORT-statement", 2, True)
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'EXPORT TO ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF LOBFILE ' ||"; _
                             "c_tabSchema || '.' || c_tabName || ' MODIFIED BY LOBSINFILE SELECT * FROM ' || c_tabSchema || '.' || c_tabName;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntExport
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute EXPORT-statement", 3, True)
   Print #fileNo, addTab(4); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntExport
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataExport, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP importing data
   ' ####################################################################################################################
 
   Dim qualProcNameDataImport As String
   qualProcNameDataImport = genQualProcName(g_sectionIndexDataCheck, spnDataChkImport, ddlType)

   Dim tempTabNameStmntImport As String
   tempTabNameStmntImport = tempTabNameStatement & "Import"

   printSectionHeader("SP importing data", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataImport
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the set of tables to import")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) determines the data pool holding the tables to import")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names to import")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables to import")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables imported")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_db2Release", g_dbtDbRelease, "NULL")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 200, True, True, True, , "Import")
 
   genSpLogProcEnter(fileNo, qualProcNameDataImport, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
 
   genProcSectionHeader(fileNo, "Verify that this DB-Version supports IMPORT")
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF v_db2Release < 9 and mode_in >= 1 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDataImport, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
   genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_db2Release))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "loop over tables to import")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE"
   Print #fileNo, addTab(4); "WHEN accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(4); "THEN CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "ELSE CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ") AS c_tabSchema,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "accessModeId_in = C.REFACCESSMODE_ID"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "determine IMPORT-statement", 2, True)
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'IMPORT FROM ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF COMMITCOUNT 10000 INSERT INTO ' ||"; _
                             "c_tabSchema || '.' || c_tabName;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntImport
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute IMPORT-statement", 3, True)
   Print #fileNo, addTab(4); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntImport
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataImport, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP loading data
   ' ####################################################################################################################
 
   Dim qualProcNameDataLoad As String
   qualProcNameDataLoad = genQualProcName(g_sectionIndexDataCheck, spnDataChkLoad, ddlType)

   Dim tempTabNameStmntLoad As String
   tempTabNameStmntLoad = tempTabNameStatement & "Load"

   printSectionHeader("SP loading data", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDataLoad
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", True, "(optional) identifies the set of tables to load")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) determines the data pool holding the tables to import")
   genProcParm(fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) determines the logical schema names to load")
   genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", True, "(optional) determines the tables to load")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables loaded")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_db2Release", g_dbtDbRelease, "NULL")

   ' FIXME: use as parameter ?
   genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1")

   genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)

   genDdlForTempStatement(fileNo, 1, True, 200, True, True, True, , "Load")
 
   genSpLogProcEnter(fileNo, qualProcNameDataLoad, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
 
   genProcSectionHeader(fileNo, "Verify that this DB-Version supports LOAD")
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF v_db2Release < 9 and mode_in >= 1 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDataLoad, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
   genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_db2Release))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "loop over tables to load")
   Print #fileNo, addTab(1); "FOR objLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE"
   Print #fileNo, addTab(4); "WHEN accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(4); "THEN CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C."; g_anAccessModeId; "   ),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "ELSE CAST('"; genSchemaName("", "", ddlType); "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ") AS c_tabSchema,"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntityName; ") AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataComparison; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmSection; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "UPPER(C."; g_anAcmEntitySection; ") = UPPER(S.SECTIONNAME)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(UPPER(C."; g_anAcmEntityName; ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "accessModeId_in = C."; g_anAccessModeId; ""
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "accessModeId_in = C.REFACCESSMODE_ID"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "determine LOAD-statement", 2, True)
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'LOAD FROM ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF INSERT INTO ' ||"; _
                             "c_tabSchema || '.' || c_tabName;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntLoad
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute LOAD-statement", 3, True)
   Print #fileNo, addTab(4); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "determine SET-INTEGRITY-statement", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt   = 'SET INTEGRITY FOR ' || c_tabSchema || '.' || c_tabName || ' IMMEDIATE CHECKED';"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStmntLoad
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "execute EXPORT-statement", 3, True)
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStmntLoad
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcNameDataLoad, ddlType, , "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDCompSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If
 End Sub
 
 Sub evalDComps()
   Dim i As Integer, j As Integer

     Dim enumDescr As EnumDescriptor
     Dim typeDescr As TypeDescriptor

     For i = 1 To g_dComps.numDescriptors Step 1
         ' determine references to attributes
         g_dComps.descriptors(i).attrRef = -1
         If g_dComps.descriptors(i).cType = eactClass Then
           g_dComps.descriptors(i).attrRef = getAttributeIndexByClassNameAndName(g_dComps.descriptors(i).sectionName, g_dComps.descriptors(i).entityName, g_dComps.descriptors(i).attrName, True)
           If g_dComps.descriptors(i).attrRef < 0 Then
             logMsg("unknown attribute """ & g_dComps.descriptors(i).attrName & """ used in specification of DataComparison for """ & g_dComps.descriptors(i).sectionName & "." & g_dComps.descriptors(i).entityName & """", ellError, edtNone)
           End If
         ElseIf g_dComps.descriptors(i).cType = eactRelationship Then
           ' fixme - implement this (if we need this)
         ElseIf g_dComps.descriptors(i).cType = eactEnum Then
           ' fixme - implement this (if we need this)
         End If
     Next i
 End Sub
 
 
 Sub genDCompCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   If Not supportSstCheck Or ddlType <> edtPdm Then
     Exit Sub
   End If

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDataCheck, clnDataComparison, processingStep, "DataCheck", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim lastCheckName As String
   Dim lastSectionName As String
   Dim lastEntityName As String
   Dim lastCType As AcmAttrContainerType
 
   lastCheckName = ""
   lastSectionName = ""
   lastEntityName = ""
   lastCType = eactType
   Dim i As Integer
   For i = 1 To g_dComps.numDescriptors
       If (lastCheckName <> g_dComps.descriptors(i).checkName) Or _
          (lastSectionName <> g_dComps.descriptors(i).sectionName) Or _
          (lastEntityName <> g_dComps.descriptors(i).entityName) Or _
          (lastCType <> g_dComps.descriptors(i).cType) Then
         Print #fileNo, """"; g_dComps.descriptors(i).checkName; """,";
         Print #fileNo, """"; g_dComps.descriptors(i).sectionName; """,";
         Print #fileNo, """"; g_dComps.descriptors(i).entityName; """,";
         Print #fileNo, """"; getAcmEntityTypeKey(g_dComps.descriptors(i).cType); """,";
         Print #fileNo, IIf(g_dComps.descriptors(i).dataPoolId >= 0, CStr(g_dComps.descriptors(i).dataPoolId), ""); ",";
         Print #fileNo, IIf(g_dComps.descriptors(i).refDataPoolId >= 0, CStr(g_dComps.descriptors(i).refDataPoolId), "")
         lastCheckName = g_dComps.descriptors(i).checkName
         lastSectionName = g_dComps.descriptors(i).sectionName
         lastEntityName = g_dComps.descriptors(i).entityName
         lastCType = g_dComps.descriptors(i).cType
       End If
   Next i
 
   Close #fileNo
 
   fileName = genCsvFileName(g_targetDir, g_sectionIndexDataCheck, clnDataComparisonAttribute, processingStep, "DataCheck", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   For i = 1 To g_dComps.numDescriptors
       Print #fileNo, """"; g_dComps.descriptors(i).sectionName; """,";
       Print #fileNo, """"; g_dComps.descriptors(i).entityName; """,";
       Print #fileNo, """"; getAcmEntityTypeKey(g_dComps.descriptors(i).cType); """,";
       Print #fileNo, """"; genAttrName(g_dComps.descriptors(i).attrName, ddlType); """,";
       Print #fileNo, """"; IIf(g_dComps.descriptors(i).compareMode = dcmKey, "K", IIf(g_dComps.descriptors(i).compareMode = dcmCompare, "C", "N")); """,";
       Print #fileNo, IIf(g_dComps.descriptors(i).sequenceNo >= 0, CStr(g_dComps.descriptors(i).sequenceNo), "0")
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub dropDCompCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDataCheck, clnDataComparison, g_targetDir, processingStep, , "DataCheck")
   killCsvFileWhereEver(g_sectionIndexDataCheck, clnDataComparisonAttribute, g_targetDir, processingStep, , "DataCheck")
 End Sub
 ' ### ENDIF IVK ###
