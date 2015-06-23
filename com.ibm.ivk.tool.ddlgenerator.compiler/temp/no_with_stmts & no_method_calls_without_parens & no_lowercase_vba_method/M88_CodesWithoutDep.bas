 Attribute VB_Name = "M88_CodesWithoutDep"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const tempCodeOidTabName = "SESSION.CodeOid"
 Private Const tempAspectOidTabName = "SESSION.AspectOid"
 Private Const tempCodeOidTabNameReferred = "SESSION.CodeOidsReferred"
 
 Private Const processingStep = 5
 
 
 
 Private Sub genDdlForTempCodeAspOids( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for ASPECT-OIDs", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempAspectOidTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Private Sub genDdlForTempCodeOids( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary tables for CODE-OIDs", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempCodeOidTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempCodeOidTabNameReferred
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "codeNumber "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Sub genCodesWithoutDepDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtLdm Then
     genCodesWithoutDepDdlByPool(edtLdm)
   ElseIf ddlType = edtPdm Then
     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And Not g_pools.descriptors(thisPoolIndex).commonItemsLocal And g_pools.descriptors(thisPoolIndex).supportAcm And Not g_pools.descriptors(thisPoolIndex).isArchive Then
             genCodesWithoutDepDdlByPool(edtPdm, thisOrgIndex, thisPoolIndex)
           End If
       Next thisPoolIndex
     Next thisOrgIndex
   End If
 End Sub
 
 
 Private Sub genCodesWithoutDepVAspectViewDdl( _
   fileNo As Integer, _
   referToAllAspectsInPs As Boolean, _
   ByRef qualTabNameAspectOid As String, _
   ByRef qualTabNameGenericAspect As String, _
   Optional ByRef psOidVarName As String = "v_psOid", _
   Optional addComma As Boolean = False, _
   Optional indent As Integer = 1 _
 )
     If Not referToAllAspectsInPs Then
       Print #fileNo, addTab(indent + 1); "V_AspectOid"
       Print #fileNo, addTab(indent + 0); "("
       Print #fileNo, addTab(indent + 1); "asp_oid"
       Print #fileNo, addTab(indent + 0); ")"
       Print #fileNo, addTab(indent + 0); "AS"
       Print #fileNo, addTab(indent + 0); "("
       Print #fileNo, addTab(indent + 1); "SELECT"
       Print #fileNo, addTab(indent + 2); g_anOid
       Print #fileNo, addTab(indent + 1); "FROM"
       Print #fileNo, addTab(indent + 2); qualTabNameAspectOid
       Print #fileNo, addTab(indent + 0); "),"
     End If
     Print #fileNo, addTab(indent + 1); "V_Aspect"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "A.*"
     Print #fileNo, addTab(indent + 1); "FROM"
     Print #fileNo, addTab(indent + 2); qualTabNameGenericAspect; " A"

     If Not referToAllAspectsInPs Then
       Print #fileNo, addTab(indent + 1); "INNER JOIN"
       Print #fileNo, addTab(indent + 2); "V_AspectOid O"
       Print #fileNo, addTab(indent + 1); "ON"
       Print #fileNo, addTab(indent + 2); "A."; g_anOid; " = O.asp_oid"
     End If

     Print #fileNo, addTab(indent + 1); "WHERE"
     Print #fileNo, addTab(indent + 2); "A."; conPsOid; " = "; psOidVarName

     Print #fileNo, addTab(indent + 0); ")"; IIf(addComma, ",", "")
 End Sub
 
 
 Private Sub genCodesWithoutDepDdlByPool( _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If
 
   If Not generateSupportForUc304 Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameExpression As String
   qualTabNameExpression = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameTerm As String
   qualTabNameTerm = genQualTabNameByClassIndex(g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex)

   ' ####################################################################################################################
   ' #    Determine Codes without dependencies
   ' ####################################################################################################################

   Dim i As Integer
   Dim referToAllAspectsInPs As Boolean
   Dim useRegDynamicForOidList As Boolean
   Dim useOidListParameter As Boolean

   Const implementCodesWithoutDepViaRegDynamic = False
   Const implementCodesWithoutDepViaOidList = True

   If implementCodesWithoutDepViaOidList Then
     Dim qualProcedureNameCodesWithoutDepAddOids As String

     qualProcedureNameCodesWithoutDepAddOids = _
       genQualProcName(g_sectionIndexAliasLrt, spnGetCodesWithoutDepAddOids, ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for adding Aspect-OIDS as filter for Codes without dependencies", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameCodesWithoutDepAddOids
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", True, "string holding the OIDs of Aspects")
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of OIDs added")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genSpLogDecl(fileNo, -1, True)
 
     genProcSectionHeader(fileNo, "declare conditions", , Not supportSpLogging Or Not generateSpLogMessages)
     genCondDecl(fileNo, "alreadyExist", "42710")
     genCondDecl(fileNo, "illegalCharacter", "22018")
 
     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR illegalCharacter"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore (extra blanks, ',' etc.)"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempCodeAspOids(fileNo, , False)

     genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDepAddOids, ddlType, , "aspOidList_in", "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "initialize output parameter", 1)
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"

     genProcSectionHeader(fileNo, "retrieve OIDs of referred Aspects")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); tempAspectOidTabName
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); g_dbtOid; "(E.elem)"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "E.elem IS NOT NULL AND E.elem <> ''"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "determine number of OIDs to retrieved")
     Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

     genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDepAddOids, ddlType, , "aspOidList_in", "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If

   Dim qualProcedureNameCodesWithoutDep As String
   For i = IIf(implementCodesWithoutDepViaRegDynamic, 1, 2) To IIf(implementCodesWithoutDepViaOidList, 3, 2)
     useRegDynamicForOidList = (i = 1)
     referToAllAspectsInPs = (i = 2)
     useOidListParameter = (i = 3)

     ' we provide multiple APIs for this, two based on an explicit list of ASPECT-OIDs and one which refers to all ASPECTs of the current ProductStructure
     qualProcedureNameCodesWithoutDep = _
       genQualProcName(g_sectionIndexAliasLrt, spnGetCodesWithoutDep, ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for determining Codes without dependencies", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameCodesWithoutDep
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure we are working with")
     genProcParm(fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division we are working with")
     If useRegDynamicForOidList Then
       genProcParm(fileNo, "IN", "regSubKey_in", "VARCHAR(64)", True, "'subKey' identifying the records in table 'REGISTRYDYNAMIC' holding the OIDs of Aspects")
     ElseIf useOidListParameter Then
       genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", True, "string holding the OIDs of Aspects")
     End If
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of CODEs found without dependencies")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genSigMsgVarDecl(fileNo)
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare conditions")
     genCondDecl(fileNo, "alreadyExist", "42710")

     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempCodeOids(fileNo, , True)

     If useRegDynamicForOidList Or useOidListParameter Then
       genDdlForTempCodeAspOids(fileNo, , Not useOidListParameter)
     End If

     genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDep, ddlType, , "psOid_in", "divisionOid_in", IIf(useRegDynamicForOidList, "'regSubKey_in", IIf(useOidListParameter, "aspOidList_in", "")), "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "initialize output parameter", 1)
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"

     If useOidListParameter Then
       genProcSectionHeader(fileNo, "determine OIDs of referred Aspects")
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); tempAspectOidTabName
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "oid"
       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); g_dbtOid; "(E.elem)"
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "E.elem IS NOT NULL AND E.elem <> ''"
       Print #fileNo, addTab(1); ";"
 
       genProcSectionHeader(fileNo, "determine number of OIDs to retrieved")
       Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"
     End If

     If useRegDynamicForOidList Then
       genProcSectionHeader(fileNo, "determine OIDs of referred Aspects")
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); tempAspectOidTabName
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "oid"
       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"

       If useOidListParameter Then
         Print #fileNo, addTab(2); g_dbtOid; "(E.elem)"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "E.elem IS NOT NULL AND E.elem <> ''"
       ElseIf useRegDynamicForOidList Then
         Print #fileNo, addTab(2); "CAST(LEFT("; g_anValue; ",19) AS "; g_dbtOid; ")"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); g_qualTabNameRegistryDynamic
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anSection; " = '"; gc_regDynamicSectionCodeWithoutDependencies; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); g_anKey; " = '"; gc_regDynamicKeyCodeWithoutDependencies; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); g_anSubKey; " = regSubKey_in"
       End If

       Print #fileNo, addTab(1); ";"
     End If

     genProcSectionHeader(fileNo, "determine codes referred to by " & g_anSr0Context)
     Print #fileNo, addTab(1); "FOR aspectLoop AS aspectCursor CURSOR FOR"
     Print #fileNo, addTab(2); "WITH"
     genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", , 2)
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); g_anSr0Context
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Aspect"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anSr0Context; " IS NOT NULL"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempCodeOidTabNameReferred
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "codeNumber"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "E.ELEM"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(" & g_anSr0Context & ", CAST('+' AS CHAR(1)))) AS E"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "(E.ELEM IS NOT NULL AND E.ELEM <> '')"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.codenumber = E.ELEM)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END FOR;"
 
     genProcSectionHeader(fileNo, "determine codes referred to by SR1CONTEXT")
     Print #fileNo, addTab(1); "FOR aspectLoop AS aspectCursor CURSOR FOR"
     Print #fileNo, addTab(2); "WITH"
     genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", , 2)
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "SR1CONTEXT"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Aspect"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "SR1CONTEXT IS NOT NULL"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempCodeOidTabNameReferred
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "codeNumber"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "E.ELEM"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(SR1CONTEXT, CAST('+' AS CHAR(1)))) AS E"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "(E.ELEM IS NOT NULL AND E.ELEM <> '')"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.codeNumber = E.ELEM)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END FOR;"
 
     genProcSectionHeader(fileNo, "determine codes referred to by NSR1CONTEXT")
     Print #fileNo, addTab(1); "FOR aspectLoop AS aspectCursor CURSOR FOR"
     Print #fileNo, addTab(2); "WITH"
     genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", , 2)
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "NSR1CONTEXT"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Aspect"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "NSR1CONTEXT IS NOT NULL"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempCodeOidTabNameReferred
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "codeNumber"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "E.ELEM"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(NSR1CONTEXT, CAST('+' AS CHAR(1)))) AS E"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "(E.ELEM IS NOT NULL AND E.ELEM <> '')"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.codeNumber = E.ELEM)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END FOR;"
 
     genProcSectionHeader(fileNo, "determine OIDs for referred codes identified so far")
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); tempCodeOidTabNameReferred; " R"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "R."; g_anOid; " = (SELECT OID FROM "; qualTabNameGenericCode; " C WHERE R.CodeNumber = C."; g_anCodeNumber; " AND C.CDIDIV_OID = divisionOid_in AND C."; g_anIsDeleted; " = 0)"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "determine codes referred to by BCDBCD_OID, BPCBPC_OID, BCCBCD_OID")
     Print #fileNo, addTab(1); "FOR aspectLoop AS aspectCursor CURSOR FOR"
     Print #fileNo, addTab(2); "WITH"
     genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", , 2)
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "BCDBCD_OID,"
     Print #fileNo, addTab(3); "BPCBPC_OID,"
     Print #fileNo, addTab(3); "BCCBCD_OID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Aspect"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "IF BCDBCD_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.oid = BCDBCD_OID) THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; tempCodeOidTabNameReferred; " ( oid ) VALUES (BCDBCD_OID);"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(2); "IF BPCBPC_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.oid = BPCBPC_OID) THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; tempCodeOidTabNameReferred; " ( oid ) VALUES (BPCBPC_OID);"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(2); "IF BCCBCD_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM "; tempCodeOidTabNameReferred; " C WHERE C.oid = BCCBCD_OID) THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; tempCodeOidTabNameReferred; " ( oid ) VALUES (BCCBCD_OID);"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "END FOR;"

     genProcSectionHeader(fileNo, "determine codes referred by TERMs")
     Dim tabColumns As EntityColumnDescriptors
     tabColumns = nullEntityColumnDescriptors
     Dim transformation As AttributeListTransformation
     initAttributeTransformation(transformation, 0, , True)

     genTransformedAttrListForEntityWithColReuse(g_classIndexGenericAspect, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomNone)
     Dim isFirstLoop As Boolean

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); tempCodeOidTabNameReferred
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V_Exp"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("

     isFirstLoop = True
     Dim j As Integer
     For j = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(j).acmAttributeIndex > 0 Then
           If g_attributes.descriptors(tabColumns.descriptors(j).acmAttributeIndex).isExpression And (tabColumns.descriptors(j).columnCategory And (eacNational Or eacNationalBool)) = 0 Then
             If Not isFirstLoop Then
               Print #fileNo, addTab(3); "UNION ALL"
             End If
             Print #fileNo, addTab(2); "SELECT DISTINCT A."; genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(j).acmAttributeIndex).shortName & "EXP"); " FROM "; qualTabNameGenericAspect; " A WHERE A."; g_anPsOid; " = psOid_in"
             If g_attributes.descriptors(tabColumns.descriptors(j).acmAttributeIndex).isNationalizable Then
               Print #fileNo, addTab(3); "UNION ALL"
               Print #fileNo, addTab(2); "SELECT DISTINCT A."; genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(j).acmAttributeIndex).shortName & "EXP", , , , True); " FROM "; qualTabNameGenericAspect; " A WHERE A."; g_anPsOid; " = psOid_in"
             End If
             isFirstLoop = False
           End If
       End If
     Next j

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "v_ExpDistinct"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Exp"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "oid IS NOT NULL"
     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "V_Code"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "T.CCRCDE_OID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameTerm; " T"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); "v_ExpDistinct E"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T."; g_anAhOid; " = E.oid"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T.CCRCDE_OID IS NOT NULL"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "C.oid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V_Code C"
     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
     Print #fileNo, addTab(2); tempCodeOidTabNameReferred; " R"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "C.oid = R.oid"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "R.oid IS NULL"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "determine result set of CODE OIDs 'not referred'")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); tempCodeOidTabName
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "C."; g_anOid
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameGenericCode; " C"
     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
     Print #fileNo, addTab(2); tempCodeOidTabNameReferred; " R"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "C."; g_anOid; " = R.oid"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "C.CDIDIV_OID = divisionOid_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "R.oid IS NULL"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "determine number of CODE-OIDs in result set", 1)
     Print #fileNo, addTab(1); "SET rowCount_out = (SELECT COUNT(*) FROM "; tempCodeOidTabName; ");"

     genProcSectionHeader(fileNo, "return Code-OIDs to application", 1)
     Print #fileNo, addTab(1); "BEGIN"
     genProcSectionHeader(fileNo, "declare cursor", 2, True)
     Print #fileNo, addTab(2); "DECLARE codeCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "oid"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); tempCodeOidTabName
     Print #fileNo, addTab(3); "FOR READ ONLY"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 2)
     Print #fileNo, addTab(2); "OPEN codeCursor;"
     Print #fileNo, addTab(1); "END;"

     genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDep, ddlType, , "psOid_in", "divisionOid_in", IIf(useRegDynamicForOidList, "'regSubKey_in", IIf(useOidListParameter, "aspOidList_in", "")), "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

    ' ##############################################################

     printSectionHeader("SP for Determining Codes without dependencies", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameCodesWithoutDep
     Print #fileNo, addTab(0); "("
     If useRegDynamicForOidList Then
       genProcParm(fileNo, "IN", "regSubKey_in", "VARCHAR(64)", True, "'subKey' identifying the records in table 'REGISTRYDYNAMIC' holding the OIDs of Aspects")
     ElseIf useOidListParameter Then
       genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", True, "string holding the OIDs of Aspects")
     End If
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of CODEs found without dependencies")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader(fileNo, "declare variables", , True)
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
     genSpLogDecl(fileNo)
 
     genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDep, ddlType, , IIf(useRegDynamicForOidList, "'regSubKey_in", IIf(useOidListParameter, "aspOidList_in", "")), "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "determine ProductStructure")
     Print #fileNo, addTab(1); "SET v_psOid = "; g_activePsOidDdl; ";"

     genProcSectionHeader(fileNo, "make sure that ProductStructure exists and Division can be determined")
     Print #fileNo, addTab(1); "SET v_divisionOid ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "PDIDIV_OID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); g_qualTabNameProductStructure
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anOid; " = v_psOid"
     Print #fileNo, addTab(1); ");"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF (v_divisionOid IS NULL) THEN"
     genSpLogProcEscape(fileNo, qualProcedureNameCodesWithoutDep, ddlType, 2, IIf(useRegDynamicForOidList, "'regSubKey_in", IIf(useOidListParameter, "aspOidList_in", "")), "rowCount_out")
     genSignalDdlWithParms("psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_psOid))")
     Print #fileNo, addTab(1); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(1); "CALL "; qualProcedureNameCodesWithoutDep; "(v_psOid, v_divisionOid, "; _
                               IIf(useRegDynamicForOidList, "regSubKey_in, ", ""); _
                               IIf(useOidListParameter, "aspOidList_in, ", ""); _
                               "rowCount_out);"

     genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDep, ddlType, , IIf(useRegDynamicForOidList, "'regSubKey_in", IIf(useOidListParameter, "aspOidList_in", "")), "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 
 End Sub
 ' ### ENDIF IVK ###
 
