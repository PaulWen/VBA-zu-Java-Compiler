 Attribute VB_Name = "M97_DataCheck"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStepDataCheck = 4
 
 Type xrefAttributeMappingForCl
   mapFrom As String
   mapTo As String
   isTv As Boolean
   isNullable As Boolean
   classIdStrList As String
   relIdStrList As String
 End Type
 
 Type xrefAttributeMappingsForCl
   numMappings As Integer
   mappings() As xrefAttributeMappingForCl
 End Type
 
 
 Private Sub initXrefAttributeMappingsForCl( _
   ByRef mapping As xrefAttributeMappingsForCl _
 )
     mapping.numMappings = 0
 End Sub
 
 
 Private Sub addXrefAttributeMappingForCl( _
   ByRef mapping As xrefAttributeMappingsForCl, _
   ByRef mapFrom As String, _
   ByRef mapTo As String, _
   ByVal acmEntityType As AcmAttrContainerType, _
   ByRef acmEntityIdStrList As String, _
   Optional ByVal isNullable As Boolean, _
   Optional ByVal isTv As Boolean = False _
 )
     If mapping.numMappings = 0 Then
       ReDim mapping.mappings(1 To gc_allocBlockSize)
     End If

     Dim i As Integer
     For i = 1 To mapping.numMappings
         If UCase(mapping.mappings(i).mapFrom) = UCase(mapFrom) And UCase(mapping.mappings(i).mapTo) = UCase(mapTo) And mapping.mappings(i).isTv = isTv And mapping.mappings(i).isNullable = isNullable Then
           If acmEntityType = eactClass Then
             mapping.mappings(i).classIdStrList = mapping.mappings(i).classIdStrList & IIf(mapping.mappings(i).classIdStrList = "", "", ",") & acmEntityIdStrList
           ElseIf acmEntityType = eactRelationship Then
             mapping.mappings(i).relIdStrList = mapping.mappings(i).relIdStrList & IIf(mapping.mappings(i).relIdStrList = "", "", ",") & acmEntityIdStrList
           End If
           Exit Sub
         End If
     Next i

     ' mapping not found - add new one
     If mapping.numMappings >= UBound(mapping.mappings) Then
       ReDim Preserve mapping.mappings(1 To mapping.numMappings + gc_allocBlockSize)
     End If
     mapping.numMappings = mapping.numMappings + 1
       mapping.mappings(mapping.numMappings).mapFrom = mapFrom
       mapping.mappings(mapping.numMappings).mapTo = mapTo
       mapping.mappings(mapping.numMappings).isTv = isTv
       mapping.mappings(mapping.numMappings).isNullable = isNullable

       If acmEntityType = eactClass Then
         mapping.mappings(mapping.numMappings).classIdStrList = acmEntityIdStrList
         mapping.mappings(mapping.numMappings).relIdStrList = ""
       ElseIf acmEntityType = eactRelationship Then
         mapping.mappings(mapping.numMappings).relIdStrList = acmEntityIdStrList
         mapping.mappings(mapping.numMappings).classIdStrList = ""
       End If
 End Sub
 
 
 Sub genDataCheckUtilitiesDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtLdm Then
     genDataCheckUtilitiesDdlByDdl(edtLdm)
   ElseIf ddlType = edtPdm Then
     genDataCheckUtilitiesDdlByDdl(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
         If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And _
            sectionValidForPoolAndOrg(g_sectionIndexDataCheck, thisOrgIndex, thisPoolIndex) Then
           genDataCheckUtilitiesDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
         End If
        Next thisOrgIndex
      Next thisPoolIndex
    End If
 End Sub
 
 
 Sub genDataCheckUtilitiesDdlByDdl( _
   ddlType As DdlTypeId _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataCheck, processingStepDataCheck, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   Dim qualProcName As String

   ' ####################################################################################################################
   ' #    Procedure TESTDATA
   ' ####################################################################################################################

   qualProcName = genQualProcName(g_sectionIndexDataCheck, spnTestData, ddlType)
 
   printSectionHeader("Generic SP for Testing Consistency of Data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "message_in", "VARCHAR(1000)", True)
   genProcParm(fileNo, "IN", "tbl_in", "VARCHAR(5000)", True)
   genProcParm(fileNo, "IN", "stmt_in", "VARCHAR(5000)", True)
   genProcParm(fileNo, "IN", "minCount_in", "INTEGER", True)
   genProcParm(fileNo, "IN", "maxCount_in", "INTEGER", True)
   genProcParm(fileNo, "IN", "countRecords_in", g_dbtBoolean, False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_cnt", "INTEGER", "0")

   genProcSectionHeader(fileNo, "declare statements")
   genVarDecl(fileNo, "v_stmntCnt", "STATEMENT")
   genVarDecl(fileNo, "v_stmntRet", "STATEMENT")
 
   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE cntCursor   CURSOR FOR v_stmntCnt;"
   Print #fileNo, addTab(1); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR v_stmntRet;"

   genProcSectionHeader(fileNo, "wrap 'stmt_in' with COUNT-clause if required")
   Print #fileNo, addTab(1); "IF countRecords_in = 1 THEN"
   Print #fileNo, addTab(2); "SET stmt_in = COALESCE(tbl_in, '') || 'SELECT COUNT(*) FROM (' || stmt_in ||') AS Q';"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "determine number of records returned by 'stmt_in'")
   Print #fileNo, addTab(1); "PREPARE v_stmntCnt FROM stmt_in;"

   Print #fileNo, addTab(1); "OPEN cntCursor;"
   Print #fileNo, addTab(1); "FETCH cntCursor INTO v_cnt;"
   Print #fileNo, addTab(1); "CLOSE cntCursor WITH RELEASE;"

   genProcSectionHeader(fileNo, "create return-message")
   Print #fileNo, addTab(1); "SET stmt_in = 'SELECT ''' ||"
   Print #fileNo, addTab(2); "(CASE WHEN v_cnt < minCount_in OR v_cnt > maxCount_in THEN 'ERROR' ELSE 'OK' END) ||"
   Print #fileNo, addTab(2); "' with ' || message_in ||"
   Print #fileNo, addTab(2); "' - MINCOUNT: ' || RTRIM(CHAR(minCount_in)) || ' MAXCOUNT: ' || RTRIM(CHAR(maxCount_in)) || ' actual COUNT: ' || RTRIM(CHAR(v_cnt)) || ''' FROM SYSIBM.SYSDUMMY1';"

   Print #fileNo, addTab(1); "PREPARE v_stmntRet FROM stmt_in;"

   genProcSectionHeader(fileNo, "return message to application")
   Print #fileNo, addTab(1); "OPEN stmntCursor;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Procedure GETVALUE
   ' ####################################################################################################################

   Dim procNameSuffix As String
   Dim procParamDbType As String
   Dim i As Integer
   For i = 1 To 2
     If i = 1 Then
       procNameSuffix = ""
       procParamDbType = "VARCHAR(100)"
     ElseIf i = 2 Then
       procNameSuffix = "_BIGINT"
       procParamDbType = "BIGINT"
     End If

     qualProcName = genQualProcName(g_sectionIndexDataCheck, spnGetValue & procNameSuffix, ddlType)
 
     printSectionHeader("SP evaluating SQL-query and retrieving single result value", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "stmt_in", "VARCHAR(5000)", True)
     genProcParm(fileNo, "OUT", "value_out", procParamDbType, False)
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare statements")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genProcSectionHeader(fileNo, "declare cursor")
     Print #fileNo, addTab(1); "DECLARE c1 CURSOR FOR v_stmnt;"

     genProcSectionHeader(fileNo, "determine result value returned by 'stmt_in'")
     Print #fileNo, addTab(1); "PREPARE v_stmnt FROM stmt_in;"
     Print #fileNo, addTab(1); "OPEN c1;"
     Print #fileNo, addTab(1); "FETCH c1 INTO value_out;"
     Print #fileNo, addTab(1); "CLOSE c1 WITH RELEASE;"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i
 
   ' ####################################################################################################################
   ' #    Function GETSCHEMA
   ' ####################################################################################################################
 
   Dim qualFuncName As String
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnGetSchema, ddlType, , , , , , True)
 
   printSectionHeader("Function supporting data check", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtDbSchemaName
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CAST(RTRIM(LEFT("; g_anValue; ", 30)) AS VARCHAR(30))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameRegistryStatic
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anSection; " = 'DATACHECK' AND "; g_anKey; " = 'SCHEMA' AND "; g_anSubKey; " = 'CURRENT'"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDataCheckUtilitiesDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If Not g_genLrtSupport Then
     Exit Sub
   End If

   Dim thisPoolId As Integer
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataCheck, processingStepDataCheck, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)
 
   Dim qualTabNameAggregationSlotGen As String
   qualTabNameAggregationSlotGen = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualTabNameAggregationSlotGenNl As String
   qualTabNameAggregationSlotGenNl = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)
 
   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameCodeCategory As String
   qualTabNameCodeCategory = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex)

   Dim classIdStrMasterEndSlot As String
   classIdStrMasterEndSlot = getClassIdStrByIndex(g_classIndexMasterEndSlot)

   Dim qualTabNameEndSlot As String
   qualTabNameEndSlot = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameEndSlotGen As String
   qualTabNameEndSlotGen = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualTabNameEndSlotGenNl As String
   qualTabNameEndSlotGenNl = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)
 
   Dim qualTabNameProperty As String
   qualTabNameProperty = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNamePropertyGen As String
   qualTabNamePropertyGen = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualTabNamePropertyGenNl As String
   qualTabNamePropertyGenNl = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)
 
   Dim qualTabNameAggregationSlotHasNumericProperty As String
   qualTabNameAggregationSlotHasNumericProperty = genQualTabNameByRelIndex(g_relIndexAggregationSlotHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameCategoryHasNumericProperty As String
   qualTabNameCategoryHasNumericProperty = genQualTabNameByRelIndex(g_relIndexCategoryHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualFuncName As String

   ' ####################################################################################################################
   ' #    Function AGGRSLOTOID4LABEL
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnAggrSlotOid4Label, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of German label of AggregationSlot", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
 
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "aggregationSlotLabel_in", "VARCHAR(256)", True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "AG.ASL_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameAggregationSlotGenNl; " NL"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameAggregationSlotGen; " AG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "AG."; g_anOid; " = NL.ASL_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NL.LABEL = aggregationSlotLabel_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "AG."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function AGGRSLOTOID4PROP
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnAggrSlotOid4Prop, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of AggregationSlot assigned to given property", fileNo)
   printSectionHeader("assumes that a maximum of only one AggregationSlot is assigned to the property", fileNo, True)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
 
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "propertyOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(0); "SELECT"
   Print #fileNo, addTab(1); "ANP.ASL_OID"
   Print #fileNo, addTab(0); "FROM"
   Print #fileNo, addTab(1); qualTabNameAggregationSlotHasNumericProperty; " ANP"
   Print #fileNo, addTab(0); "WHERE"
   Print #fileNo, addTab(1); "ANP.NPR_OID = propertyOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function CATOID4CODE
   ' ####################################################################################################################
 
   Dim qualFuncNameCatOid4Code As String
   qualFuncNameCatOid4Code = genQualFuncName(g_sectionIndexDataCheck, udfnCatOid4Code, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of the Category for a given Code", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
 
   Print #fileNo, addTab(1); qualFuncNameCatOid4Code
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CC.CAT_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCodeCategory; " CC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CC.GCO_OID = GC."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS.PDIDIV_OID = GC.CDIDIV_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "CC."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = psOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTLABEL4OID
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotLabel4Oid, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of German label of EndSlot", fileNo)
   printSectionHeader("assumption: no history in ENDSLOT_GEN (1:1 with ENDSLOT)", fileNo, True)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "endSlotOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(240)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CASE"
   Print #fileNo, addTab(3); "WHEN LOCATE(' ', NL.LABEL) > 0"
   Print #fileNo, addTab(3); "THEN '''' || NL.LABEL || ''''"
   Print #fileNo, addTab(3); "ELSE NL.LABEL"
   Print #fileNo, addTab(2); "END AS ES_LABEL"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlotGenNl; " NL"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "NL.ESL_OID = EG."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG.ESL_OID = endSlotOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4CODE
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4Code, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of EndSlot corresponding to the given Code", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ES."; g_anOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCodeCategory; " CC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CC.GCO_OID = GC."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlot; " ES"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "ES.ESCESC_OID = CC.CAT_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS.PDIDIV_OID = GC.CDIDIV_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "CC."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anCid; " = '"; classIdStrMasterEndSlot; " '"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4CODE_OL
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4CodeOL, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of EndSlot corresponding to the given Code (ignoring 'Lack')", fileNo)
   printSectionHeader("assumption: no history in ENDSLOT_GEN (1:1 with ENDSLOT)", fileNo, True)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ES."; g_anOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCodeCategory; " CC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CC.GCO_OID = GC."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlot; " ES"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "ES.ESCESC_OID = CC.CAT_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "EG.ESL_OID = ES."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS.PDIDIV_OID = GC.CDIDIV_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "CC."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anCid; " = '"; classIdStrMasterEndSlot; " '"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG."; g_anSlotType; " < 5"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4CODE_ST
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4CodeST, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning OID of EndSlot corresponding to the given Code and slot type", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, True)
   genProcParm(fileNo, "", "slotType_in", g_dbtEnumId, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ES."; g_anOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCodeCategory; " CC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CC.GCO_OID = GC."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlot; " ES"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "ES.ESCESC_OID = CC.CAT_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "EG.ESL_OID = ES."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS.PDIDIV_OID = GC.CDIDIV_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "CC."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anCid; " = '"; classIdStrMasterEndSlot; " '"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG."; g_anSlotType; " = slotType_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4CODE_TB
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4CodeTB, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning list of EndSlot-OIDs corresponding to the given Code", fileNo)
   printSectionHeader("only applicable to paint slots", fileNo, True)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "TABLE ("
   Print #fileNo, addTab(2); "oid "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ES."; g_anOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCodeCategory; " CC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CC.GCO_OID = GC."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlot; " ES"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "ES.ESCESC_OID = CC.CAT_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "EG.ESL_OID = ES."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS.PDIDIV_OID = GC.CDIDIV_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "CC."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ES."; g_anCid; " = '"; classIdStrMasterEndSlot; " '"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG.ASSIGNEDPAINTZONEKEY =''"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4LABEL
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4Label, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning EndSlot-OID for German label", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "endSlotLabel_in", "VARCHAR(256)", True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "EG.ESL_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameEndSlotGenNl; " NL"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "EG."; g_anOid; " = NL.ESL_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NL.LABEL = endSlotLabel_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function ENDSLOTOID4LZCODE
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnEndSlotOid4LzCode, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning PaintZone-EndSlot-OID for given PaintZone-Code", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "EG.ESL_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameEndSlotGen; " EG"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "EG."; g_anSlotType; " = 5"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG.ASSIGNEDPAINTZONEKEY = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EG."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function GCOOID4CODE
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnGcoOid4Code, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning Code-OID for given CodeNumber in Division", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "divOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "GC."; g_anOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "GC."; g_anCodeNumber; " = codeNumber_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC.CDIDIV_OID = divOid_in"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function NPROID4CODE
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnNprOid4Code, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning NumericProperty-OID for given Code and PropertyLabel", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, True)
   genProcParm(fileNo, "", "label_in", "VARCHAR(255)", False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CN.NPR_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameCategoryHasNumericProperty; " CN"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyGen; " PG"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CN.NPR_OID = PG.PRP_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyGenNl; " NL"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PG."; g_anOid; " = NL.PRP_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); qualFuncNameCatOid4Code; "(codeNumber_in, psOid_in) = CN.CAT_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "CN."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "UPPER(NL.LABEL) = UPPER(label_in)"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function NPROID4CODE_ID
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnNprOid4CodeId, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning NumericProperty-OID for given Code and PropertyTemplate-ID", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "codeNumber_in", g_dbtCodeNumber, True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, True)
   genProcParm(fileNo, "", "templateId_in", g_dbtEnumId, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CN.NPR_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameCategoryHasNumericProperty; " CN"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameProperty; " PR"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CN.NPR_OID = PR."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
     Print #fileNo, addTab(2); genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, thisOrgIndex, thisPoolIndex, , , , , , , True); " PT"
   Else
     Print #fileNo, addTab(2); g_qualTabNamePropertyTemplate; " PT"
   End If
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PR.PTMHTP_OID = PT."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "CN."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PT.ID = templateId_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); qualFuncNameCatOid4Code; "(codeNumber_in, psOid_in) = CN.CAT_OID"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function PROPOID4PROPLABEL
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexDataCheck, udfnPropOid4PropLabel, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
 
   printSectionHeader("Function returning Property-OID for given label", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "propertyLabel_in", "VARCHAR(50)", True)
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, False)
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtOid
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "PG.PRP_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNamePropertyGen; " PG"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyGenNl; " NL"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PG."; g_anOid; " = NL.PRP_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NL.LABEL = propertyLabel_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PG."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

     If Not g_pools.descriptors(thisPoolIndex).supportLrt And (g_pools.descriptors(thisPoolIndex).id <> g_orgs.descriptors(thisOrgIndex).setProductiveTargetPoolId) Then
       GoTo NormalExit
     End If

 If genDataCheckCl Then
   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex)
 
   ' ####################################################################################################################
   ' #    Procedure verifying content of change log
   ' ####################################################################################################################
 
   Dim qualProcNameCheckChangeLog As String
   qualProcNameCheckChangeLog = genQualProcName(g_sectionIndexDataCheck, spnCheckChangeLog, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP verifying content of ChangeLog", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameCheckChangeLog
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "OUT", "recordCount_out", "SMALLINT", False, "number of consistency violations found")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_cnt", "INTEGER", "0")

   Dim xrefMapping As xrefAttributeMappingsForCl
   initXrefAttributeMappingsForCl(xrefMapping)

   Dim i As Integer
   For i = 1 To g_attributes.numDescriptors
       If Not strArrayIsNull(g_attributes.descriptors(i).mapsToChangeLogAttributes) Then
         Dim k As Integer
         For k = LBound(g_attributes.descriptors(i).mapsToChangeLogAttributes) To UBound(g_attributes.descriptors(i).mapsToChangeLogAttributes)
           addXrefAttributeMappingForCl(xrefMapping, g_attributes.descriptors(i).attributeName, g_attributes.descriptors(i).mapsToChangeLogAttributes(k), g_attributes.descriptors(i).cType, _
             IIf(g_attributes.descriptors(i).cType = eactClass, g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).subclassIdStrListNonAbstract, g_relationships.descriptors(g_attributes.descriptors(i).acmEntityIndex).relIdStr))
         Next k
       End If
   Next i

   For i = 1 To xrefMapping.numMappings
       Print #fileNo, addTab(1); "-- CL-attribute (xref)"; xrefMapping.mappings(i).mapFrom; " maps to "; xrefMapping.mappings(i).mapTo; "["; xrefMapping.mappings(i).isTv; " / "; xrefMapping.mappings(i).isNullable; " / "; xrefMapping.mappings(i).classIdStrList; " / "; xrefMapping.mappings(i).relIdStrList; "]"

       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "CL."; g_anAcmEntityType; ","
       Print #fileNo, addTab(2); "CL."; g_anAcmEntityId; ","
       Print #fileNo, addTab(2); "CL."; g_anAhCid; ","
       Print #fileNo, addTab(2); "CL.GEN,"
       Print #fileNo, addTab(2); "CL.NL,"
       Print #fileNo, addTab(2); "CL.OPERATION_ID,"
       Print #fileNo, addTab(2); "CL."; g_anPsOid; ","
       Print #fileNo, addTab(2); CStr(g_orgs.descriptors(thisOrgIndex).id); " AS ORGID,"
       Print #fileNo, addTab(2); CStr(thisPoolId); " AS ACCESSMODEID,"
       Print #fileNo, addTab(2); "CL."; UCase(xrefMapping.mappings(i).mapTo); " AS COLUMN,"
       Print #fileNo, addTab(2); "COUNT(*)"
       Print #fileNo, addTab(1); "INTO"
       Print #fileNo, addTab(2); "v_cnt"
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNameChangeLog; " CL"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "("
       If xrefMapping.mappings(i).classIdStrList <> "" Then
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "CL."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "CL."; g_anAcmEntityId; " IN ("; xrefMapping.mappings(i).classIdStrList; ")"
         Print #fileNo, addTab(3); ")"
       End If
       If xrefMapping.mappings(i).relIdStrList <> "" Then
         If xrefMapping.mappings(i).classIdStrList <> "" Then
           Print #fileNo, addTab(4); "OR"
         End If

         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "CL."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "CL."; g_anAcmEntityId; " IN ("; xrefMapping.mappings(i).relIdStrList; ")"
         Print #fileNo, addTab(3); ")"
       End If
       Print #fileNo, addTab(2); ")"

       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "CL."; UCase(xrefMapping.mappings(i).mapTo); " IS NULL"
       Print #fileNo, addTab(1); "GROUP BY"
       Print #fileNo, addTab(2); "CL."; g_anAcmEntityType; ","
       Print #fileNo, addTab(2); "CL."; g_anAcmEntityId; ","
       Print #fileNo, addTab(2); "CL."; g_anAhCid; ","
       Print #fileNo, addTab(2); "CL.GEN,"
       Print #fileNo, addTab(2); "CL.NL,"
       Print #fileNo, addTab(2); "CL.OPERATION_ID,"
       Print #fileNo, addTab(2); "CL."; g_anPsOid; ","
       Print #fileNo, addTab(2); "CL."; UCase(xrefMapping.mappings(i).mapTo)
       Print #fileNo, addTab(1); ";"
 
       If xrefMapping.mappings(i).classIdStrList <> "" Then
         Print #fileNo,
         Print #fileNo, addTab(1); "-- analogously check via aggregate head"
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "COUNT(*)"
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_cnt"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNameChangeLog; " CL"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "CL."; g_anAhCid; " IN ("; xrefMapping.mappings(i).classIdStrList; ")"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "CL."; UCase(xrefMapping.mappings(i).mapTo); " IS NULL"
         Print #fileNo, addTab(1); ";"
       End If
   Next i

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End If
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ENDIF IVK ###
 
