 Attribute VB_Name = "M85_DataFix"
 ' ### IF IVK ###
 Option Explicit
 
 Global Const tempExpOidTabName = "SESSION.ExpOid"
 Global Const tempFtoExpOidTabName = "SESSION.FtoExpOid"
 Global Const tempCodeOidTabName = "SESSION.CodeOid"
 Global Const tempDataPoolTabName = "SESSION.DataPool"
 Global Const tempAffectedObjectsTabName = "SESSION.AffectedObjects"
 
 Private Const generateExpCopySupport = True
 
 Private Const processingStep = 1
 Private Sub genDeleteCBMVDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
 
   If Not supportSectionDataFix Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Delete of CBMV is only supported at 'pool-level'
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Delete of CBMV only supported in data pools supporting LRT
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   On Error GoTo ErrorExit

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewNameGenericAspectMqt As String
   qualViewNameGenericAspectMqt = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True)

   Dim qualViewNameGenericAspectNlTextMqt As String
   qualViewNameGenericAspectNlTextMqt = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, True)

   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualLrtCommitProcName As String
   qualLrtCommitProcName = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)

   ' ####################################################################################################################
   ' #    SP for Deleting 'CBMVs '
   ' ####################################################################################################################

   Dim qualProcNameDeleteTechProperty As String
   qualProcNameDeleteTechProperty = genQualProcName(g_sectionIndexDataFix, spnDeleteCBMV, ddlType, thisOrgIndex, thisPoolIndex)
   printSectionHeader("SP for 'Deleting CBMV for a code'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDeleteTechProperty
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "code_in", "VARCHAR(15)", True, "Code")
   genProcParm(fileNo, "IN", "codeOid_in", g_dbtOid, True, "Code-OID")
   genProcParm(fileNo, "IN", "cdUserId_in", "VARCHAR(15)", True, "User id")
   genProcParm(fileNo, "IN", "ps_oid_in", g_dbtOid, True, "PS-OID")
   genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records (sum over all involved tables)")
   Print #fileNo, addTab(0); ")"

   Print #fileNo,
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "SPECIFIC DELETECBMV"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare constants")
   Print #fileNo, addTab(1); "DECLARE c_trNumber           INTEGER          CONSTANT     3;                    -- logical transaction number"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_lrtOid", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_msg", "VARCHAR(70)", "NULL")
   genVarDecl(fileNo, "v_genChangelog", "INTEGER", "1")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_gwspError", "INTEGER", "0")
   genVarDecl(fileNo, "v_gwspInfo", "INTEGER", "0")
   genVarDecl(fileNo, "v_gwspWarning", "INTEGER", "0")

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "stmnt", "STATEMENT")
 
   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader(fileNo, "make sure that DB2-registers are empty")
   Print #fileNo, addTab(1); "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );"
 
   genProcSectionHeader(fileNo, "open LRT")
   Print #fileNo, addTab(2); "CALL "; qualLrtBeginProcName; "(cdUserId_in, c_trNumber, ps_oid_in, 0, v_lrtOid);"
   Print #fileNo,
   Print #fileNo, addTab(2); "CALL SYSPROC.WLM_SET_CLIENT_INFO( cdUserId_in, v_lrtOid, ps_oid_in, NULL, NULL );"
 
   genProcSectionHeader(fileNo, "delete delete CBMV from GenericAspects")
   Print #fileNo, addTab(1); "DELETE FROM "
   Print #fileNo, addTab(2); qualViewNameGenericAspectMqt; " GA "
   Print #fileNo, addTab(1); "WHERE "
   Print #fileNo, addTab(2); "GA.BCDBCD_OID = codeOid_in "
   Print #fileNo, addTab(1); "AND "
   Print #fileNo, addTab(2); "GA.CLASSID = '09006' "
   Print #fileNo, addTab(1); "AND "
   Print #fileNo, addTab(2); "GA.PS_OID = ps_oid_in "
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "delete GenericAspect_Nl-Text")
   Print #fileNo, addTab(1); "DELETE FROM "
   Print #fileNo, addTab(2); qualViewNameGenericAspectNlTextMqt; " NL "
   Print #fileNo, addTab(1); "WHERE "
   Print #fileNo, addTab(2); "EXISTS (SELECT 1 FROM "; qualViewNameGenericAspectMqt; " GA WHERE GA.INLRT = v_lrtOid AND NL.AHOID = GA.AHOID AND GA.LRTSTATE = 3) AND "
   Print #fileNo, addTab(2); "NL.AHCLASSID = '09006' AND "
   Print #fileNo, addTab(2); "NL.PS_OID = ps_oid_in "
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "set LRT comment")
   Print #fileNo, addTab(1); "INSERT INTO "
   Print #fileNo, addTab(2); qualTabNameLrt; "_NL_TEXT "
   Print #fileNo, addTab(2); "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) "
   Print #fileNo, addTab(1); "VALUES ("
   Print #fileNo, addTab(2); "NEXTVAL FOR "; genQualOidSeqNameForOrg(thisOrgIndex, ddlType); ", v_lrtOid, 1,  'MDS Service Skript: Löschen der Code-BM-Gültigkeiten für Code ''' || RTRIM( code_in ) || '''. PsOid: '  || RTRIM( ps_oid_in ), ps_oid_in)"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "commit LRT")
   Print #fileNo, addTab(1); "CALL "; qualLrtCommitProcName; "(v_lrtOid, 0, v_genChangelog, v_rowCount, v_gwspError, v_gwspInfo, v_gwspWarning );"
   Print #fileNo, addTab(1); "SET recordCount_out = v_rowCount;"
   Print #fileNo,

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
   Print #fileNo,
 
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 
 End Sub
 
 Sub genDdlForExpEntityLockAndUpdate( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   thisOrgIndex As Integer, _
   thisPoolIndex As Integer, _
   ByRef classIndex As Integer, _
   objOidVariable As String, _
   isPrimaryOrg As Boolean, _
   qualProcNameAssignCodeCat As String _
 )
   Dim tabColumns As EntityColumnDescriptors
   Dim transformation As AttributeListTransformation

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabName As String
   qualTabName = genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True)
   Dim unqualTabName As String
   unqualTabName = getUnqualObjName(qualTabName)
 
   Dim qualViewName As String
   Dim qualViewTaxParameter As String
   If classIndex <> g_classIndexTaxParameter Then
     qualViewName = genQualViewNameByEntityIndex(classIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)
   Else
     qualViewName = genQualViewNameByEntityIndex(classIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, False)
     qualViewTaxParameter = genQualViewNameByEntityIndex(classIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)
   End If

   Dim qualViewNameGenericAspect As String
   qualViewNameGenericAspect = genQualViewNameByEntityIndex(g_classIndexGenericAspect, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 0)

   genTransformedAttrListForEntityWithColReuse(classIndex, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, , , edomNone)

   genProcSectionHeader(fileNo, "update re-mapped Expression-references in " & unqualTabName & "s", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualViewName; " EN"
   Print #fileNo, addTab(2); "SET"
   Dim includeOr As Boolean
   Dim numExpressions As Integer
   Dim i As Integer
   numExpressions = 0
   If classIndex <> g_classIndexTaxParameter Then
     For i = 1 To tabColumns.numDescriptors
         If (tabColumns.descriptors(i).columnCategory And eacFkOidExpression) <> 0 And (tabColumns.descriptors(i).columnCategory And eacNationalBool) = 0 Then
           numExpressions = numExpressions + 1
           Print #fileNo, addTab(3); "EN."; tabColumns.descriptors(i).columnName; " = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = " & tabColumns.descriptors(i).columnName & ")," & tabColumns.descriptors(i).columnName & "),"
         End If
     Next i
   Else
     Print #fileNo, addTab(3); "EN.FOREXP_OID = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = FOREXP_OID),FOREXP_OID),"
   End If
   Print #fileNo, addTab(3); "EN."; g_anStatus; " = "; CStr(statusWorkInProgress); ","
   Print #fileNo, addTab(3); "EN."; g_anUpdateUser; " = v_cdUserId,"
   Print #fileNo, addTab(3); "EN."; g_anLastUpdateTimestamp; " = v_currentTs,"
   Print #fileNo, addTab(3); "EN."; g_anVersionId; " = "; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "EN."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("

   If classIndex <> g_classIndexTaxParameter Then
     includeOr = False
     numExpressions = 0
     For i = 1 To tabColumns.numDescriptors
         If (tabColumns.descriptors(i).columnCategory And eacFkOidExpression) <> 0 And (tabColumns.descriptors(i).columnCategory And eacNationalBool) = 0 Then
           numExpressions = numExpressions + 1
           If includeOr Then
             Print #fileNo, addTab(5); "OR"
           End If
           includeOr = True
           Print #fileNo, addTab(4); "EN."; tabColumns.descriptors(i).columnName; " IN ("
           Print #fileNo, addTab(5); "SELECT"
           Print #fileNo, addTab(6); "oid"
           Print #fileNo, addTab(5); "FROM"
           Print #fileNo, addTab(6); tempExpOidTabName
           Print #fileNo, addTab(4); ")"
         End If
     Next i
   Else
     Print #fileNo, addTab(4); "EN.FOREXP_OID IN ("
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "oid"
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); tempExpOidTabName
     Print #fileNo, addTab(4); ")"
   End If
   Print #fileNo, addTab(3); ")"
   If Not isPrimaryOrg And classIndex <> g_classIndexTaxParameter Then
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "EXISTS (SELECT 1 FROM VL6CASP021.GENERICASPECT GA WHERE GA.OID = EN.AHOID AND GA.ISNATIONAL = 1)"
   End If
   Print #fileNo, addTab(2); ";"

   If classIndex = g_classIndexTaxParameter Then
     genProcSectionHeader(fileNo, "lock GenericAspects which belongs to locked " & unqualTabName, 2)
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualViewTaxParameter
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); g_anStatus; " = "; CStr(statusWorkInProgress); ","
     Print #fileNo, addTab(3); g_anUpdateUser; " = v_cdUserId,"
     Print #fileNo, addTab(3); g_anLastUpdateTimestamp; " = v_currentTs,"
     Print #fileNo, addTab(3); g_anVersionId; " = "; g_anVersionId; " + 1"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); g_anAhOid
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualViewName
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ";"
   Else
     genProcSectionHeader(fileNo, "lock GenericAspects which belongs to locked " & unqualTabName, 2)
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualViewNameGenericAspect
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); g_anStatus; " = "; CStr(statusWorkInProgress); ","
     Print #fileNo, addTab(3); g_anUpdateUser; " = v_cdUserId,"
     Print #fileNo, addTab(3); g_anLastUpdateTimestamp; " = v_currentTs,"
     Print #fileNo, addTab(3); g_anVersionId; " = "; g_anVersionId; " + 1"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); g_anAhOid
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualViewName
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(4); ")"
     If Not isPrimaryOrg Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); g_anIsNational; " = "; gc_dbTrue
     End If
     Print #fileNo, addTab(3); ";"
   End If

 End Sub
 

 Sub genDdlForTempFtoExpOid( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for FTO-Expression-OIDs")

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempFtoExpOidTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 Sub genDdlForTempExpOid( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for Expression-OIDs")

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempExpOidTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 Sub genDdlForTempAffectedObjects( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for AffectedObjects")
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempAffectedObjectsTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid                  "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "classid              VARCHAR(5),"
   Print #fileNo, addTab(indent + 1); "inLrt                "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "owner                VARCHAR(16),"
   Print #fileNo, addTab(indent + 1); "tr                   "; g_dbtInteger
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 Sub genDdlForTempCodeOid( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False, _
   Optional includeHasBeenSetProductive As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for Code-OIDs")

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempCodeOidTabName
   Print #fileNo, addTab(indent + 0); "("
   If includeHasBeenSetProductive Then
     Print #fileNo, addTab(indent + 1); "oid                  "; g_dbtOid; ","
     Print #fileNo, addTab(indent + 1); "hasBeenSetProductive "; g_dbtBoolean; ","
     Print #fileNo, addTab(indent + 1); "codeNumber           "; g_dbtCodeNumber
   Else
     Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid; ","
     Print #fileNo, addTab(indent + 1); "codeNumber "; g_dbtCodeNumber
   End If
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Sub genDdlForTempDataPool( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for Data Pools")

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempDataPoolTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "orgId        "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "orgOid       "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "psOid        "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "accessModeId "; g_dbtEnumId
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Sub genDataFixSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtPdm Then
     genDataFixSupportUtils(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).supportUpdates Then
             genAssignCodeCatSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genDeleteNSR1SupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genModifyCodeTypeSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genCheckAffectedObjectsByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genDeleteProdCodeSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genDeleteTechAspectSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genDeleteTechPropertySupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genDeleteCBMVDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             genActivateNationalCodeTextSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
           End If
       Next thisOrgIndex
     Next thisPoolIndex
   End If
 End Sub
 Private Sub genActivateNationalCodeTextSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If
 
   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' only supported in data pools supporting LRT
     Exit Sub
   End If

   If thisOrgIndex = g_primaryOrgId Then
     ' only supported in non-primary data pools
     Exit Sub
   End If

   On Error GoTo ErrorExit
 
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim targetSchemaName As String
   targetSchemaName = genSchemaName(snAlias, ssnAlias, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
   Dim unqualTabNameGenericCode As String
   unqualTabNameGenericCode = getUnqualObjName(qualTabNameGenericCode)
   Dim qualViewNameGenericCode As String
   qualViewNameGenericCode = genQualViewNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, True, True)

   Dim qualTabNameGenericCodeNlText As String
   qualTabNameGenericCodeNlText = genQualNlTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)

   Dim unqualTabNameGenericCodeNlText As String
   unqualTabNameGenericCodeNlText = getUnqualObjName(qualTabNameGenericCodeNlText)
   Dim qualViewNameGenericCodeNlText As String
   qualViewNameGenericCodeNlText = genQualViewNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, True)
 
   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)
 

   ' ####################################################################################################################
   ' #    SP to activate national code texts
   ' ####################################################################################################################

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcNameActivateNationalCodeTexts As String
   qualProcNameActivateNationalCodeTexts = genQualProcName(g_sectionIndexDataFix, spnActivateNationalCodeTexts, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP to activate national code texts", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameActivateNationalCodeTexts
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", g_dbtOid, True, " -- '0' - only list affected records, '1' execute changes")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of 'current' Product Structure  - used for LRT and to determine division")
   genProcParm(fileNo, "IN", "languageId_in", g_dbtOid, True, "1 German, 2 English, ...., see Language_Enum")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being updated (sum over all tables)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_recordCount", "INTEGER", "0 ")
   genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_trNumber", "INTEGER", "1")

   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
   genVarDecl(fileNo, "v_previewStmnt", "STATEMENT")

   Print #fileNo, addTab(1); "DECLARE clientcur CURSOR WITH RETURN FOR v_previewStmnt;"
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"


   genProcSectionHeader(fileNo, "determine division OID")
   Print #fileNo, addTab(1); "SET v_divisionOid ="
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PDIDIV_OID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anOid; " = psOid_in"
   Print #fileNo, addTab(1); ");"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF (v_divisionOid IS NULL) THEN"
     genSignalDdlWithParms("psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(psOid_in))")
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(1); "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   Print #fileNo, addTab(1); "IF (mode_in = 0) THEN"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'GC.CODENUMBER, NL.LABEL, NL.LABEL_NATIONAL ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'FROM ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || '"; qualTabNameGenericCodeNlText; " NL, "; qualTabNameGenericCode; " GC ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'WHERE ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'NL.LANGUAGE_ID = ? ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'GC."; g_anIsDeleted; " = "; gc_dbFalse; " ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'NL."; g_anIsDeleted; " = "; gc_dbFalse; " ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'NL.LABEL_ISNATACTIVE = "; gc_dbFalse; " ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'NL.LABEL_NATIONAL IS NOT NULL AND LENGTH(NL.LABEL_NATIONAL) > 0 ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'NL.GCO_OID = GC."; g_anOid; " ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'AND ';"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || 'GC.CDIDIV_OID = ? ';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_previewStmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN clientcur USING languageId_in, v_divisionOid;"
   Print #fileNo, addTab(1); "ELSE"

   genProcSectionHeader(fileNo, "determine number of affected GenericCodeNlText")
   Print #fileNo, addTab(2); "SET v_recordCount = ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COUNT(*)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCodeNlText; " NL"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "NL.LANGUAGE_ID = languageId_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NL."; g_anIsDeleted; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NL.LABEL_ISNATACTIVE = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NL.LABEL_NATIONAL IS NOT NULL AND LENGTH(NL.LABEL_NATIONAL) > 0 "
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "EXISTS (SELECT 1 FROM "; qualTabNameGenericCode; " GC WHERE GC."; g_anOid; " = NL.GCO_OID AND GC."; g_anIsDeleted; " = "; gc_dbFalse; " AND GC.CDIDIV_OID = v_divisionOid)"
   Print #fileNo, addTab(2); ");"
 
   genProcSectionHeader(fileNo, "if no records are affected, there is nothing to do")
   Print #fileNo, addTab(2); "IF v_recordCount > 0 THEN"
 
   genProcSectionHeader(fileNo, "open LRT")
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; qualLrtBeginProcName; " (?, ?, ?, 0, ? )';"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_lrtOid"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "cdUserId_in,"
   Print #fileNo, addTab(4); "v_trNumber,"
   Print #fileNo, addTab(4); "psOid_in"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(3); "CALL SYSPROC.WLM_SET_CLIENT_INFO(cdUserId_in, v_lrtOid, psOid_in, NULL, NULL);"
 
   genProcSectionHeader(fileNo, "set LRT comment")
   Print #fileNo, addTab(3); "INSERT INTO "
   Print #fileNo, addTab(4); qualTabNameLrt; "_NL_TEXT "
   Print #fileNo, addTab(4); "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) "
   Print #fileNo, addTab(3); "VALUES ("
   Print #fileNo, addTab(4); "NEXTVAL FOR "; genQualOidSeqNameForOrg(thisOrgIndex, ddlType); ", v_lrtOid, 1,  'Nationale Codebezeichnung für die Sprache '  || RTRIM( languageId_in ) || ' wurde aktiviert', psOid_in)"
   Print #fileNo, addTab(3); ";"

    genProcSectionHeader(fileNo, "update GenericCode_NlText")
    Print #fileNo, addTab(3); "SET v_stmntTxt =                ' UPDATE "; qualViewNameGenericCodeNlText; " NL';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' SET ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   LABEL_ISNATACTIVE = 1,';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   VERSIONID = VERSIONID + 1';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' WHERE ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   NL.LANGUAGE_ID = ?';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   NL.LABEL_ISNATACTIVE = 0';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND  ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   NL.LABEL_NATIONAL IS NOT NULL';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   EXISTS (SELECT 1 FROM "; qualTabNameGenericCode; " GC WHERE GC.OID = NL.GCO_OID AND GC.ISDELETED = 0 AND GC.CDIDIV_OID = ?)';"
 
    Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
    Print #fileNo, addTab(3); "EXECUTE"
    Print #fileNo, addTab(3); "  v_stmnt"
    Print #fileNo, addTab(3); "USING"
    Print #fileNo, addTab(3); "  languageId_in, v_divisionOid"
    Print #fileNo, addTab(3); ";"

    Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
    Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"

    genProcSectionHeader(fileNo, "update GenericCode")
    Print #fileNo, addTab(3); "SET v_stmntTxt =                ' UPDATE "; qualViewNameGenericCode; " GC';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' SET ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   UPDATEUSER = ?,';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   LASTUPDATETIMESTAMP = CURRENT TIMESTAMP,';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   VERSIONID = VERSIONID + 1';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' WHERE ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   GC.CDIDIV_OID = ?';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '  AND ';"
    Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '   EXISTS (SELECT 1 FROM "; qualTabNameGenericCodeNlText; "  NL WHERE NL.AHOID = GC.OID AND NL.INLRT = ?)';"
 
    Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
    Print #fileNo, addTab(3); "EXECUTE"
    Print #fileNo, addTab(3); "  v_stmnt"
    Print #fileNo, addTab(3); "USING"
    Print #fileNo, addTab(3); "  cdUserId_in, v_divisionOid, v_lrtOid"
    Print #fileNo, addTab(3); ";"

    Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
    Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
 
    Print #fileNo, addTab(2); "END IF;"
    Print #fileNo, addTab(1); "END IF;"
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
 
 
 Private Sub genDataFixSupportUtils( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit
 
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)

   ' ####################################################################################################################
   ' #    SP for 'transactional securely' executing Data Fix scripts
   ' ####################################################################################################################
 
   Dim qualProcedureNameSetApplVersion As String
   qualProcedureNameSetApplVersion = genQualProcName(g_sectionIndexDbAdmin, spnSetApplVersion, ddlType)
 
   Dim qualProcedureNameDropObjects As String
   qualProcedureNameDropObjects = genQualProcName(g_sectionIndexDbAdmin, spnDropObjects, ddlType)

   Dim qualProcedureNameDfxExecute As String
   qualProcedureNameDfxExecute = genQualProcName(g_sectionIndexDataFix, spnDfxExecute, ddlType)

   Dim schemaNameDataFix As String
   schemaNameDataFix = genSchemaName(snDataFix, ssnDataFix, ddlType)
   printSectionHeader("SP for 'transactional securely' executing Data Fix scripts", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDfxExecute
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dfxProcName_in", "VARCHAR(100)", True, "unqualified name of the DFX-routine to execute")
   genProcParm(fileNo, "IN", "version_in", "VARCHAR(20)", True, "version-info")
   genProcParm(fileNo, "IN", "revision_in", "VARCHAR(20)", True, "revision-info")
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "(optional) OID of LRT if fix is implemented via LRT")
   genProcParm(fileNo, "IN", "onlyOnce_in", g_dbtBoolean, True, "(optional) if set to '1' register the fix as 'once-only-fix'")
   genProcParm(fileNo, "IN", "description_in", "VARCHAR(100)", True, "description text to store")
   genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records affected by the data fix")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genProcSectionHeader(fileNo, "declare variables", , Not supportSpLogging Or Not generateSpLogMessages)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_error", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_objCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_objFailCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_catchError", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_rc", "INTEGER", "0")
   genVarDecl(fileNo, "v_msg", "VARCHAR(300)", "NULL")
   genSpLogDecl(fileNo)
 
   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_msg = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(2); "SET v_msg = LEFT(TRANSLATE( v_msg, ' ', x'FF' ), "; CStr(gc_dbMaxSignalMessageLength); ");"
   Print #fileNo, addTab(2); "SET v_msg = LEFT(v_msg, "; CStr(gc_dbMaxSignalMessageLength); ");"
   Print #fileNo, addTab(2); "IF v_catchError = "; gc_dbFalse; " THEN"
 
   genSpLogProcEscape(fileNo, qualProcedureNameDfxExecute, ddlType, 3, "'dfxProcName_in", "'version_in", "'revision_in", _
     "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out")
 
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_error = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter(fileNo, qualProcedureNameDfxExecute, ddlType, , "'dfxProcName_in", "'version_in", "'revision_in", _
     "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameters and variables")
   Print #fileNo, addTab(1); "SET v_catchError = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; schemaNameDataFix; ".' || dfxProcName_in || '(?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "recordCount_out"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rc = DB2_RETURN_STATUS;"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_catchError = "; gc_dbFalse; ";"
   Print #fileNo, addTab(1); "IF v_error = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(2); "CALL "; qualProcedureNameSetApplVersion; "(version_in, revision_in, lrtOid_in, onlyOnce_in, description_in);"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameDropObjects; "(2, 'PROCEDURE', '"; schemaNameDataFix; "%', dfxProcName_in, NULL, NULL, v_objCount, v_objFailCount);"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_error = 1 THEN"
 
   genSpLogProcEscape(fileNo, qualProcedureNameDfxExecute, ddlType, 2, "'dfxProcName_in", "'version_in", "'revision_in", _
     "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out")
 
   Print #fileNo, addTab(2); "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
 
   genSpLogProcExit(fileNo, qualProcedureNameDfxExecute, ddlType, 1, "'dfxProcName_in", "'version_in", "'revision_in", _
     "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out")
 
   Print #fileNo, addTab(1); "RETURN v_rc;"
 
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
 
 
 Private Sub genDeleteNSR1SupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If
 
   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' only supported in data pools supporting LRT
     Exit Sub
   End If

   If thisOrgIndex = g_primaryOrgId Then
     ' only supported in non-primary data pools
     Exit Sub
   End If

   On Error GoTo ErrorExit
 
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim targetSchemaName As String
   targetSchemaName = genSchemaName(snAlias, ssnAlias, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   Dim unqualTabNameGenericAspect As String
   unqualTabNameGenericAspect = getUnqualObjName(qualTabNameGenericAspect)
   Dim qualViewNameGenericAspect As String
   qualViewNameGenericAspect = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True)

   Dim qualTabNameGenericAspectNlText As String
   qualTabNameGenericAspectNlText = genQualNlTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   Dim unqualTabNameGenericAspectNlText As String
   unqualTabNameGenericAspectNlText = getUnqualObjName(qualTabNameGenericAspectNlText)
   Dim qualViewNameGenericAspectNlText As String
   qualViewNameGenericAspectNlText = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, True)
 
   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualFuncNameSr0 As String
   qualFuncNameSr0 = genQualFuncName(g_sectionIndexAspect, "Sr0Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualFuncNameSr1 As String
   qualFuncNameSr1 = genQualFuncName(g_sectionIndexAspect, "Sr1Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualFuncNameNsr1 As String
   qualFuncNameNsr1 = genQualFuncName(g_sectionIndexAspect, "Nsr1Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex)

   ' ####################################################################################################################
   ' #    SP to Delete a NSR1
   ' ####################################################################################################################

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcNameDeleteNSR1 As String
   qualProcNameDeleteNSR1 = genQualProcName(g_sectionIndexDataFix, spnDeleteNSR1, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP to Delete a NSR1", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDeleteNSR1
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the Aspect to delete")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "(optional) CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", True, "(optional) logical transaction number - only used if 'lrtOid_inout IS NULL'")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of 'current' Product Structure  - only used if 'lrtOid_inout IS NULL'")
   genProcParm(fileNo, "INOUT", "lrtOid_inout", g_dbtLrtId, True, "(optional) OID of the LRT used for any data manipulation (may already exist)")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being deleted (sum over all tables)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
   genVarDecl(fileNo, "v_classIdNsr1", "CHARACTER(5)", "'" & g_classes.descriptors(g_classIndexNSr1Validity).classIdStr & "'")
   genVarDecl(fileNo, "v_classIdTypePrice", "CHARACTER(5)", "'" & g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr & "'")
   genVarDecl(fileNo, "v_parentClassId", "CHARACTER(5)", "'" & g_classes.descriptors(g_classIndexGenericAspect).classIdStr & "'")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_generationCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_typePriceCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_recordCount", "INTEGER", "0 ")
   genVarDecl(fileNo, "v_nlTextCount", "INTEGER", "0 ")
   genVarDecl(fileNo, "v_countAffectedEntity", "INTEGER", "0 ")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "determine PS-OID if LRT is given, if not begin a new LRT")
   Print #fileNo, addTab(1); "IF lrtOid_inout IS NOT NULL THEN"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "L."; g_anPsOid; ","
   Print #fileNo, addTab(3); "U."; g_anUserId
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_psOid,"
   Print #fileNo, addTab(3); "v_cdUserId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameLrt; " L"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameUser; " U"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L.UTROWN_OID = U."; g_anOid
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "L."; g_anOid; " = lrtOid_inout"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anEndTime; " IS NULL"
   Print #fileNo, addTab(2); "WITH UR;"
   genProcSectionHeader(fileNo, "make sure we found PS", 2)
   Print #fileNo, addTab(2); "IF v_psOid IS NULL THEN"
   genSignalDdlWithParms("lrtNotExist", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(lrtOid_inout))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_psoid = psOid_in;"
   Print #fileNo, addTab(2); "SET v_cdUserId = cdUserId_in;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL " & qualLrtBeginProcName & "(?,?,?,0,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "lrtOid_inout"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "v_cdUserId,"
   Print #fileNo, addTab(3); "trNumber_in,"
   Print #fileNo, addTab(3); "v_psOid"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "determine number of affected NSR1")
   Print #fileNo, addTab(1); "SET v_recordCount = ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COUNT(*)"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspect
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CLASSID = v_classIdNsr1"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); g_anIsDeleted; " = "; gc_dbFalse
   Print #fileNo, addTab(1); ");"
 
   genProcSectionHeader(fileNo, "if no GenericAspects are affected, there is nothing to do")
   Print #fileNo, addTab(1); "If v_recordCount = 0 Then"
   genSignalDdlWithParms("objNotFound", fileNo, 2, "NSR1", unqualTabNameGenericAspect, , , , , , , , "RTRIM(CHAR(oid_in))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "check if other NSR1 Generations exists")
   Print #fileNo, addTab(1); "SET v_generationCount = ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COUNT(*)"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspect; " NSR1_1"
   Print #fileNo, addTab(2); "JOIN"
   Print #fileNo, addTab(3); qualTabNameGenericAspect; " NSR1_2"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "NSR1_1.NSR1CONTEXT = NSR1_2.NSR1CONTEXT"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NSR1_1.E1VEX1_OID = NSR1_2.E1VEX1_OID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "NSR1_1."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NSR1_1."; g_anCid; " = v_classIdNsr1"
   Print #fileNo, addTab(1); ");"

   genProcSectionHeader(fileNo, "if no other NSR1 Generations exists, check if TypePrices for this NSR1 exist")
   Print #fileNo, addTab(1); "If v_generationCount = 1 Then"
   Print #fileNo, addTab(2); "SET v_typePriceCount = ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COUNT(DISTINCT TYPEPRICE."; g_anOid; ")"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericAspect; " NSR1"
   Print #fileNo, addTab(3); "JOIN"
   Print #fileNo, addTab(4); qualTabNameGenericAspect; " SR1"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "NSR1.E1VEX1_OID = SR1."; g_anOid
   Print #fileNo, addTab(3); "JOIN"
   Print #fileNo, addTab(4); qualTabNameGenericAspect; " SR0"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "SR1.E0VEX0_OID = SR0."; g_anOid
   Print #fileNo, addTab(3); ","; qualTabNameGenericAspect; " TYPEPRICE"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "NSR1."; g_anOid; " = oid_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NSR1."; g_anCid; " = v_classIdNsr1"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); qualFuncNameNsr1; "(TYPEPRICE."; g_anOid; ") = "; qualFuncNameNsr1; "(NSR1."; g_anOid; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); qualFuncNameSr1; "(TYPEPRICE."; g_anOid; ") = "; qualFuncNameSr1; "(SR1."; g_anOid; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); qualFuncNameSr0; "(TYPEPRICE."; g_anOid; ") = "; qualFuncNameSr0; "(SR0."; g_anOid; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TYPEPRICE."; g_anCid; " = v_classIdTypePrice"
   Print #fileNo, addTab(2); ");"
 
   genProcSectionHeader(fileNo, "if TypePrices for this NSR1 exist, throw exception", 2)
   Print #fileNo, addTab(2); "If v_typePriceCount > 0 Then"
   genSignalDdlWithParms("deleteNotAllowedForReason", fileNo, 3, "NSR1", "TypePrice(s) exist", , , , , , , , "RTRIM(CHAR(oid_in))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "set environment variables")
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL SYSPROC.WLM_SET_CLIENT_INFO(''' || RTRIM(v_cdUserId) || ''', ''' || RTRIM(CHAR(lrtOid_inout)) || ''', ''' || RTRIM(CHAR(v_psOid)) || ''', NULL, NULL)';"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader(fileNo, "delete GenericAspect via LRT view (marks it with LRTSTATE deleted)")
   Print #fileNo, addTab(1); "DELETE"
   Print #fileNo, addTab(2); qualViewNameGenericAspect
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = oid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "delete GENERICASPECT_NL_TEXTs via LRT view (implicitely brings them into private tables and marks it with LRTSTATE deleted)")
   Print #fileNo, addTab(1); "DELETE"
   Print #fileNo, addTab(2); qualViewNameGenericAspectNlText
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "GAS_OID = oid_in"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); g_anPsOid; " = v_psOid;"
 
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_nlTextCount = ROW_COUNT;"
 
   Print #fileNo, addTab(1); "SET rowCount_out = v_recordCount + v_nlTextCount;"
 
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
 
 
 Private Sub genAssignCodeCatSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If
 
   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' only supported in data pools supporting LRT
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim isPrimaryOrg As Boolean
   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewNameAcmEntityFkCol As String
   qualViewNameAcmEntityFkCol = genQualViewName(g_sectionIndexDbMeta, vnAcmEntityFkCol, vnsAcmEntityFkCol, ddlType)

   Dim qualTabNameTerm As String
   qualTabNameTerm = genQualTabNameByClassIndex(g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameTermLrt As String
   qualTabNameTermLrt = genQualTabNameByClassIndex(g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, , True)
 
   Dim qualTabNameExpressionLrt As String
   qualTabNameExpressionLrt = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualViewNameExpressionLrt As String
   qualViewNameExpressionLrt = genQualViewNameByEntityIndex(g_classIndexExpression, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameGenericAspectLrt As String
   qualTabNameGenericAspectLrt = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, , True)
 
   Dim qualViewNameGenericAspect As String
   qualViewNameGenericAspect = genQualViewNameByEntityIndex(g_classIndexGenericAspect, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualViewNameEndSlot As String
   qualViewNameEndSlot = genQualViewNameByEntityIndex(g_classIndexEndSlot, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewNameGenericCode As String
   qualViewNameGenericCode = genQualViewNameByEntityIndex(g_classIndexGenericCode, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualTabNameCategory As String
   qualTabNameCategory = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameCategoryGen As String
   qualTabNameCategoryGen = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, True)
   Dim qualTabNameCategoryGenNlText As String
   qualTabNameCategoryGenNlText = genQualNlTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualViewNameCodeCategory As String
   qualViewNameCodeCategory = genQualViewNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, True, True)

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors

   ' ####################################################################################################################
   ' #    SP for Re-Assignment of Codes to a Category
   ' ####################################################################################################################

   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcNameAssignCodeCat As String
   qualProcNameAssignCodeCat = genQualProcName(g_sectionIndexAliasLrt, spnAssignCodeCat, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for '(Re)Assignment of Codes to a Category'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameAssignCodeCat
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "codeOid_in", g_dbtOid, True, "OID of the Code to assign to a category")
   genProcParm(fileNo, "IN", "categoryNewOid_in", g_dbtOid, True, "OID of the Category to assign the Codes to")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied (sum over all tables)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   If Not isPrimaryOrg Then
     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "alreadyExist", "42710")
   End If
 
   genProcSectionHeader(fileNo, "declare variables", , isPrimaryOrg)
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   If Not isPrimaryOrg Then
     genVarDecl(fileNo, "v_mpcExpCount", "INTEGER", "0")
   End If
   genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_lrtForeignOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_unknownCodeOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_lockedCodeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumberInUse", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_endSlotOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_endSlotCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_categoryOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_aspectOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_actHeadOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_actElemOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_conHeadOid", g_dbtOid, "NULL")
 
   genVarDecl(fileNo, "v_expLockedOid", g_dbtOid, "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE codeCursor CURSOR FOR v_stmnt;"

   If Not isPrimaryOrg Then
     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
     Print #fileNo, addTab(1); "END;"
   End If

   genDdlForTempOidMap(fileNo, , True)
   If Not isPrimaryOrg Then
     genDdlForTempFtoExpOid(fileNo, , True)
   End If
   genDdlForTempExpOid(fileNo, , True)
   genDdlForTempCodeOid(fileNo, , isPrimaryOrg)

   genSpLogProcEnter(fileNo, qualProcNameAssignCodeCat, ddlType, , "codeOid_in", "categoryNewOid_in", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET v_currentTs  = CURRENT TIMESTAMP;"

   genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, True, 1)
 
   genProcSectionHeader(fileNo, "verify that we have an active transaction")
   Print #fileNo, addTab(1); "IF "; gc_db2RegVarLrtOid; " = '' THEN"
   genSignalDdl("noLrt", fileNo, 2)
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "SET v_lrtOid  = BIGINT('0' || CURRENT CLIENT_WRKSTNNAME);"

   If Not isPrimaryOrg Then
     genProcSectionHeader(fileNo, "cleanup Code-OIDs if CodeNumberList or CodeOidList is given")
     Print #fileNo, addTab(1); "IF (codeOid_in IS NOT NULL) THEN"
     Print #fileNo, addTab(2); "DELETE FROM "; tempCodeOidTabName; ";"
     Print #fileNo, addTab(1); "END IF;"
   End If

   genProcSectionHeader(fileNo, "determine PS-OID")
   Dim indent As Integer
   indent = 0
   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "L."; g_anPsOid; ","
   Print #fileNo, addTab(indent + 2); "U."; g_anUserId
   Print #fileNo, addTab(indent + 1); "INTO"
   Print #fileNo, addTab(indent + 2); "v_psOid,"
   Print #fileNo, addTab(indent + 2); "v_cdUserId"
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); qualTabNameLrt; " L"
   Print #fileNo, addTab(indent + 1); "INNER JOIN"
   Print #fileNo, addTab(indent + 2); g_qualTabNameUser; " U"
   Print #fileNo, addTab(indent + 1); "ON"
   Print #fileNo, addTab(indent + 2); "L.UTROWN_OID = U."; g_anOid
   Print #fileNo, addTab(indent + 1); "WHERE"
   Print #fileNo, addTab(indent + 2); "L."; g_anOid; " = v_lrtOid"
   Print #fileNo, addTab(indent + 3); "AND"
   Print #fileNo, addTab(indent + 2); "L."; g_anEndTime; " IS NULL"
   Print #fileNo, addTab(indent + 1); "WITH UR;"

   genProcSectionHeader(fileNo, "make sure we found PS", 1)
   Print #fileNo, addTab(indent + 1); "IF v_psOid IS NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, -3, "codeOid_in", "categoryNewOid_in", "rowCount_out")
   genSignalDdlWithParms("lrtNotExist", fileNo, indent + 2, , , , , , , , , , "RTRIM(CHAR(v_lrtOid))")
   Print #fileNo, addTab(indent + 1); "END IF;"
 
   genProcSectionHeader(fileNo, "determine Division-OID")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "PDIDIV_OID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_divisionOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = v_psoid"
   Print #fileNo, addTab(1); "WITH UR;"

   genProcSectionHeader(fileNo, "insert Code-OID to temp table")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); g_anOid
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "VALUES "
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "codeOid_in"
   Print #fileNo, addTab(1); ");"
   Print #fileNo,
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "codeNumber = ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); g_anCodeNumber
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCode
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anOid; " = codeOid_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1)
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "oid"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_unknownCodeOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "codeNumber IS NULL"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_unknownCodeOid IS NOT NULL THEN"
   genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_unknownCodeOid))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "verify that no LRT of the given PS refers to any of the Codes", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " AS c_orgId,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityId; " = A."; g_anAcmEntityId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " <> AFK.REFENTITYID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.ID = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " ASC"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, True)
   Print #fileNo, addTab(2); "SET v_colConditions = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "AFK.FKCOL AS c_fkCol"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " = c_tabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "FKCOL ASC"
   Print #fileNo, addTab(2); "DO"
 
   Print #fileNo, addTab(3); "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' AND ' END) || '(C.' || RTRIM(c_fkCol) || ' = T.oid)';"

   Print #fileNo, addTab(2); "END FOR;"
 
   genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2)
   Print #fileNo, addTab(2); "SET v_codeNumberInUse = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT C."; g_anInLrt; ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C INNER JOIN "; _
                               tempCodeOidTabName; " T ON ' || v_colConditions || ' WHERE C."; g_anInLrt; " <> ' || RTRIM(CHAR(v_lrtOid)) || ' AND "; g_anPsOid; " = ' || v_psOid;"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN codeCursor;"
   Print #fileNo, addTab(2); "FETCH codeCursor INTO v_lrtForeignOid, v_codeNumberInUse;"
   Print #fileNo, addTab(2); "CLOSE codeCursor WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_codeNumberInUse IS NOT NULL THEN"
   genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, , , , , , , , , , "v_codeNumberInUse", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtForeignOid))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "make sure a Category-OID is given")
   Print #fileNo, addTab(1); "SET v_categoryOid = categoryNewOid_in;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_categoryOid IS NULL THEN"
   genSignalDdlWithParms("catNotExist", fileNo, 2)
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "verify that Category uniquely defines an EndSlot")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "eslOid,"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY eslOid)"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_endSlotOid,"
   Print #fileNo, addTab(2); "v_endSlotCount"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "E."; g_anOid; " AS eslOid"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SELECT E."; g_anOid; " FROM "; qualViewNameEndSlot; " E WHERE E."; g_anPsOid; " = v_psOid AND E.ESCESC_OID = v_categoryOid AND E."; g_anIsDeleted; " = 0"
   Print #fileNo, addTab(4); ") E"
   Print #fileNo, addTab(2); ") V_Esl"
   Print #fileNo, addTab(1); "ORDER BY"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY eslOid) DESC"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_endSlotOid IS NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, -2, "codeOid_in", "categoryNewOid_in", "rowCount_out")
   genSignalDdlWithParms("eslNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_categoryOid))")
 
   Print #fileNo, addTab(1); "END IF;"

   If Not isPrimaryOrg Then
     genProcSectionHeader(fileNo, "retrieve Expression-OIDs from FTO")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); tempFtoExpOidTabName
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "OID"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameExpressionLrt
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); ";"
   End If

   genProcSectionHeader(fileNo, "retrieve Expression-OIDs")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempExpOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "T."; g_anAhOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName; " C"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameTerm; " T"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "C.oid = T.CCRCDE_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T."; g_anPsOid; " = v_psOid"
   If Not isPrimaryOrg Then
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "NOT EXISTS (SELECT 1 FROM "; tempFtoExpOidTabName; " E WHERE T."; g_anAhOid; " = E.oid)"
   End If
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "verify that none of the Expressions is involved in some LRT in this ProductStructure (PS included for performance)")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "EXP."; g_anOid
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_expLockedOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameExpressionLrt; " EXP"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); tempExpOidTabName; " EXPOID"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "EXPOID.oid = EXP."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "EXP."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "EXP."; g_anInLrt; " <> v_lrtOid"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,

   Print #fileNo, addTab(1); "IF v_expLockedOid IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, , "codeOid_in", "categoryNewOid_in", "rowCount_out")
 
   genSignalDdlWithParms("expLocked", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_expLockedOid))")
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "IF v_endSlotCount = 1 THEN"
   genProcSectionHeader(fileNo, "Category has unique EndSlot", 2)
   genProcSectionHeader(fileNo, "loop over all Expression-forming tables and copy data into LRT-tables", 2)
   Print #fileNo, addTab(2); "FOR tabCursor AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "PT."; g_anPdmFkSchemaName; " AS v_tabSchema,"
   Print #fileNo, addTab(4); "PT."; g_anPdmTableName; " AS v_tabName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AE"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " LT"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "LT."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LT."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " PT"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PT."; g_anPdmLdmFkSchemaName; " = LT."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PT."; g_anPdmLdmFkTableName; " = LT."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "PT."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PT."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LT."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("

   Dim firstEntity As Boolean
   firstEntity = True

   Dim c As Integer
   For c = 1 To g_classes.numDescriptors
       If (g_classes.descriptors(c).superClassIndex <= 0) And g_classes.descriptors(c).isSubjectToExpCopy Then
         If Not firstEntity Then
           Print #fileNo, addTab(6); "OR"
         End If
         Print #fileNo, addTab(5); "("
         Print #fileNo, addTab(6); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "AE."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(c).sectionName); "'"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "AE."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(c).className); "'"
         Print #fileNo, addTab(5); ")"
         firstEntity = False
       End If
   Next c
   Dim r As Integer
   For r = 1 To g_relationships.numDescriptors
       If g_relationships.descriptors(r).implementsInOwnTable And g_relationships.descriptors(r).isSubjectToExpCopy Then
         If Not firstEntity Then
           Print #fileNo, addTab(6); "OR"
         End If
         Print #fileNo, addTab(5); "("
         Print #fileNo, addTab(6); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "AE."; g_anAcmEntitySection; " = '"; UCase(g_relationships.descriptors(r).sectionName); "'"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "AE."; g_anAcmEntityName; " = '"; UCase(g_relationships.descriptors(r).relName); "'"
         Print #fileNo, addTab(5); ")"
          firstEntity = False
      End If
   Next r

   Print #fileNo, addTab(4); ")"

   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "LT."; g_anLdmIsGen; " ASC,"
   Print #fileNo, addTab(4); "LT."; g_anLdmIsNl; " ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL ' || v_tabSchema || '.EXPCP2LRT_' || v_tabName || '(?,?,?,?)' ;"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "v_lrtOid,"
   Print #fileNo, addTab(4); "v_cdUserId,"
   Print #fileNo, addTab(4); "v_currentTs"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);"

   Print #fileNo, addTab(2); "END FOR;"

   If Not isPrimaryOrg Then
     genProcSectionHeader(fileNo, "retrieve Expression-OIDs from FTO")
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempExpOidTabName
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "OID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameExpressionLrt
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ISINVALID = "; CStr(gc_dbTrue)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(2); ";"
   End If

   genProcSectionHeader(fileNo, "update EndSlot-references in Terms involving re-assigned Codes", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualTabNameTermLrt
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "ESLESL_OID = v_endSlotOid"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); g_anInLrt; " = v_lrtOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CCRCDE_OID IN ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "oid"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempCodeOidTabName
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader(fileNo, "update EndSlot-references in GenericAspects having re-assigned Codes as 'BaseCode'", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualViewNameGenericAspect
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "BESESL_OID = v_endSlotOid,"
   Print #fileNo, addTab(3); g_anStatus; " = "; CStr(statusWorkInProgress); ","
   Print #fileNo, addTab(3); g_anUpdateUser; " = v_cdUserId,"
   Print #fileNo, addTab(3); g_anLastUpdateTimestamp; " = v_currentTs,"
   Print #fileNo, addTab(3); g_anVersionId; " = "; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "BCDBCD_OID IN ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anOid
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempCodeOidTabName
   Print #fileNo, addTab(3); ")"
   If Not isPrimaryOrg Then
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anIsNational; " = "; gc_dbTrue
   End If
   Print #fileNo, addTab(2); ";"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 0)

   genTransformedAttrListForEntityWithColReuse(g_classIndexGenericAspect, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, , , edomNone)
 
   genProcSectionHeader(fileNo, "update re-mapped Expression-references in GenericAspects", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualViewNameGenericAspect
   Print #fileNo, addTab(2); "SET"
   Dim includeOr As Boolean
   Dim numExpressions As Integer
   Dim i As Integer
   numExpressions = 0
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacFkOidExpression) <> 0 And (tabColumns.descriptors(i).columnCategory And eacNationalBool) = 0 Then
         numExpressions = numExpressions + 1
         Print #fileNo, addTab(3); tabColumns.descriptors(i).columnName; " = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = " & tabColumns.descriptors(i).columnName & ")," & tabColumns.descriptors(i).columnName & "),"
       End If
   Next i
   Print #fileNo, addTab(3); g_anStatus; " = "; CStr(statusWorkInProgress); ","
   Print #fileNo, addTab(3); g_anUpdateUser; " = v_cdUserId,"
   Print #fileNo, addTab(3); g_anLastUpdateTimestamp; " = v_currentTs,"
   Print #fileNo, addTab(3); g_anVersionId; " = "; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
 
   includeOr = False
   numExpressions = 0
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacFkOidExpression) <> 0 And (tabColumns.descriptors(i).columnCategory And eacNationalBool) = 0 Then
         numExpressions = numExpressions + 1
         If includeOr Then
           Print #fileNo, addTab(5); "OR"
         End If
         includeOr = True
         Print #fileNo, addTab(4); tabColumns.descriptors(i).columnName; " IN ("
         Print #fileNo, addTab(5); "SELECT"
         Print #fileNo, addTab(6); "oid"
         Print #fileNo, addTab(5); "FROM"
         Print #fileNo, addTab(6); tempExpOidTabName
         Print #fileNo, addTab(4); ")"
       End If
   Next i
   Print #fileNo, addTab(3); ")"
   If Not isPrimaryOrg Then
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anIsNational; " = "; gc_dbTrue
   End If
   Print #fileNo, addTab(2); ";"
 
   genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, g_classIndexActionHeading, "v_actHeadOid", isPrimaryOrg, qualProcNameAssignCodeCat)
   genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, g_classIndexActionElement, "v_actElemOid", isPrimaryOrg, qualProcNameAssignCodeCat)
   genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, g_classIndexConditionHeading, "v_condHeadOid", isPrimaryOrg, qualProcNameAssignCodeCat)
   genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, g_classIndexTaxParameter, "v_taxOid", isPrimaryOrg, qualProcNameAssignCodeCat)
 
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     genProcSectionHeader(fileNo, "register all relevant entities as being affected by the LRT", 2)

     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(2); "("

     genAttrListForEntity(g_classIndexLrtAffectedEntity, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, False, edomListNonLrt)
 
     Print #fileNo, addTab(2); ")"

     Print #fileNo, addTab(2); "WITH"
     Print #fileNo, addTab(3); "V_ExtraEntities"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "entityId,"
     Print #fileNo, addTab(3); "entityType,"
     Print #fileNo, addTab(3); "opId"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "AS"
     Print #fileNo, addTab(2); "("

     Print #fileNo, addTab(3); "VALUES ('"; g_classes.descriptors(g_classIndexExpression).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "', "; CStr(lrtStatusUpdated); ")"
     If isPrimaryOrg Then
       Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "VALUES ('"; g_classes.descriptors(g_classIndexGenericCode).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "', "; CStr(lrtStatusLocked); ")"
       Print #fileNo, addTab(3); "UNION ALL"
     'End If
     'Print #fileNo, addTab(3); "VALUES ('"; g_classes.descriptors(g_classIndexGenericAspect).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "', "; CStr(lrtStatusUpdated); ")"
     'If isPrimaryOrg Then
       'Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "VALUES ('"; g_relationships.descriptors(g_relIndexCodeCategory).relIdStr; "', '"; gc_acmEntityTypeKeyRel; "', "; CStr(lrtStatusUpdated); ")"
     End If

     Dim j As Integer
     For j = 1 To g_classes.numDescriptors
         If (g_classes.descriptors(j).superClassIndex <= 0) And g_classes.descriptors(j).isSubjectToExpCopy Then
           Print #fileNo, addTab(3); "UNION ALL"
           Print #fileNo, addTab(3); "VALUES ('"; g_classes.descriptors(j).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "', "; CStr(lrtStatusCreated); ")"
         End If
     Next j

     For j = 1 To g_relationships.numDescriptors
         If g_relationships.descriptors(j).implementsInOwnTable And g_relationships.descriptors(j).isSubjectToExpCopy Then
           Print #fileNo, addTab(3); "UNION ALL"
           Print #fileNo, addTab(3); "VALUES ('"; g_relationships.descriptors(j).relIdStr; "', '"; gc_acmEntityTypeKeyRel; "', "; CStr(lrtStatusCreated); ")"
         End If
     Next j

     Print #fileNo, addTab(2); ")"

     Print #fileNo, addTab(2); "SELECT DISTINCT"

     initAttributeTransformation(transformation, 2, , , , "PSE.")

     setAttributeMapping(transformation, 1, conLrtOid, "v_lrtOid")
     setAttributeMapping(transformation, 2, conAcmOrParEntityId, "PSE." & g_anAcmEntityId)

     genTransformedAttrListForEntity(g_classIndexLrtAffectedEntity, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , , , edomListNonLrt)

     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "A."; g_anAcmEntityId; ","
     Print #fileNo, addTab(5); "A."; g_anAcmEntityType; ","
     Print #fileNo, addTab(5); "L."; g_anLdmIsGen; ","
     Print #fileNo, addTab(5); "L."; g_anLdmIsNl; ","
     Print #fileNo, addTab(5); "V.opId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " A"

     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); "V_ExtraEntities V"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "A."; g_anAcmEntityId; " = V.entityId"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "A."; g_anAcmEntityType; " = V.entityType"

     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
     Print #fileNo, addTab(3); ") PSE"

     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "NOT EXISTS ("

     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "1"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualTabNameLrtAffectedEntity; " AE"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "AE."; g_anLrtOid; " = v_lrtOid"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "AE."; g_anAcmOrParEntityId; " = PSE."; g_anAcmEntityId

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 2)

     setAttributeMapping(transformation, 1, conLrtOid, "")
     setAttributeMapping(transformation, 2, conAcmOrParEntityId, "")

     genTransformedAttrListForEntityWithColReuse(g_classIndexLrtAffectedEntity, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 5, , , edomNone)

     Dim col As Integer
     For col = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "AE."; tabColumns.descriptors(col).columnName; " = PSE."; tabColumns.descriptors(col).columnName
     Next col

     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); ";"
   End If

   Print #fileNo,

   Print #fileNo, addTab(1); "ELSE"
   genProcSectionHeader(fileNo, "If Category has multiple EndSlots then only make Expressions invalid - User needs to respond to 2nd Lvl Text", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualViewNameExpressionLrt
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "ISINVALID = "; CStr(gc_dbTrue)
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "OID IN (SELECT oid FROM "; tempExpOidTabName; ");"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(fileNo, qualProcNameAssignCodeCat, ddlType, , "codeOid_in", "categoryNewOid_in", "rowCount_out")

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
 
 
 Private Sub genCheckAffectedObjectsByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Check Affected Objects for AssignCodeCategory is only supported at 'pool-level'
     Exit Sub
   End If
 
   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Check Affected Objects for AssignCodeCategory is only supported in data pools supporting LRT
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameChangeLogFactoryProd As String
   qualTabNameChangeLogFactoryProd = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)

   Dim qualTabNameCategoryFactoryProd As String
   qualTabNameCategoryFactoryProd = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)

   Dim qualTabNameGeneralSettings As String
   qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameUser As String
   qualTabNameUser = genQualTabNameByClassIndex(g_classIndexUser, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameTerm As String
   qualTabNameTerm = genQualTabNameByClassIndex(g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualViewNameAcmEntityFkCol As String
   qualViewNameAcmEntityFkCol = genQualViewName(g_sectionIndexDbMeta, vnAcmEntityFkCol, vnsAcmEntityFkCol, ddlType)

   ' ####################################################################################################################
   ' #    SP for Checking Affected Objects for AssignCodeCategory
   ' ####################################################################################################################

   Dim qualProcNameCheckAffectedObjects As String
   Dim qualProcNameCheckAffectedObjectsIntern As String
   qualProcNameCheckAffectedObjects = genQualProcName(g_sectionIndexAliasLrt, spnCheckAffectedObjects, ddlType, thisOrgIndex, thisPoolIndex)
   qualProcNameCheckAffectedObjectsIntern = qualProcNameCheckAffectedObjects
   printSectionHeader("SP for Checking if Affected Objects for AssignCodeCategory are locked/exist in other LRTs", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameCheckAffectedObjectsIntern
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", True, "(optional) list of Code-Numbers to assign to a category")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of 'current' Product Structure  - only used if 'lrtOid_in IS NULL'")
   genProcParm(fileNo, "IN", "lrtOid_in", "BIGINT", False, "(optional) OID of the LRT used for any data manipulation (may already exist)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 1"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(3000)", "NULL")
   genVarDecl(fileNo, "v_restmntTxt", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_unknownCodeNumber", "VARCHAR(15)", "NULL")

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
   genVarDecl(fileNo, "v_restmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;"

   genDdlForTempExpOid(fileNo, , True)
   genDdlForTempCodeOid(fileNo, , True)
   genDdlForTempAffectedObjects(fileNo, , True)

   genProcSectionHeader(fileNo, "determine PS-OID if LRT is given")
   Print #fileNo, addTab(1); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anPsOid
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameLrt
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anOid; " = lrtOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); g_anEndTime; " IS NULL"
   Print #fileNo, addTab(2); "WITH UR;"

   genProcSectionHeader(fileNo, "make sure we found PS", 2)
   Print #fileNo, addTab(2); "IF v_psOid IS NULL THEN"
   genSignalDdlWithParms("lrtNotExist", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(lrtOid_in))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_psOid = psOid_in;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "determine Division-OID")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "PDIDIV_OID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_divisionOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = v_psOid"
   Print #fileNo, addTab(1); "WITH UR;"

   genProcSectionHeader(fileNo, "retrieve Code-numbers from list - if given")
   Print #fileNo, addTab(1); "IF codeNumberList_in IS NOT NULL THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempCodeOidTabName
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "codeNumber"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "RTRIM(LTRIM(E.elem))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "E.elem IS NOT NULL AND E.elem <> ''"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader(fileNo, "map OIDs to Code-numbers (using REPEATABLE READ)", 2)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); tempCodeOidTabName; " T"
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "T.oid = ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anOid
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "C.CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "C."; g_anCodeNumber; " = T.codeNumber"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "WITH RR;"
 
   genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 2)
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "codeNumber"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_unknownCodeNumber"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempCodeOidTabName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "oid IS NULL"
   Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_unknownCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameCheckAffectedObjectsIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("codeNumberNotKnown", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(v_unknownCodeNumber))")
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "ELSE"
 
   If (thisOrgIndex = g_primaryOrgIndex) Then
     genSpLogProcEscape(fileNo, qualProcNameCheckAffectedObjectsIntern, ddlType, 2, "'codeNumberList_in", "psOid_in", "lrtOid_in")
     genSignalDdlWithParms("illegParam", fileNo, 2, "codeNumberList_in", , , , , , , , , "RTRIM(CHAR(codeNumberList_in))")
   Else
     Print #fileNo,
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempCodeOidTabName
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid,"
     Print #fileNo, addTab(3); "codeNumber"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "ahObjectId,"
     Print #fileNo, addTab(3); "baseCodeNumber"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameChangeLogFactoryProd; " CL"
     Print #fileNo, addTab(2); "JOIN"
     Print #fileNo, addTab(3); qualTabNameGeneralSettings; " GS"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "CL.PS_OID = GS.PS_OID"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "CL.OPTIMESTAMP > GS.LASTCENTRALDATATRANSFERCOMMIT"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CL.PS_OID = v_psoid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CL.DBTABLENAME = 'CODECATEGORY'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CL.DBCOLUMNNAME = 'CAT_OID'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CL.OLDVALUEBIGINT <> (SELECT OID FROM "; qualTabNameCategoryFactoryProd; " WHERE ISDEFAULT = 1 AND PS_OID = v_psoid)"
     Print #fileNo, addTab(2); ";"
   End If

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "verify that no LRT of current PS refers to any of the Codes", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " AS c_orgId,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityId; " = A."; g_anAcmEntityId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " <> AFK.REFENTITYID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.ID = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " ASC"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2)
   Print #fileNo, addTab(2); "SET v_colConditions = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "AFK.FKCOL AS c_fkCol"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " = c_tabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "FKCOL ASC"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' OR ' END) || 'C.' || RTRIM(c_fkCol) || ' IN (SELECT T.oid FROM "; tempCodeOidTabName; " T)';"
   Print #fileNo, addTab(2); "END FOR;"
   genProcSectionHeader(fileNo, "determine locked affected objects", 2)
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO "; tempAffectedObjectsTabName; " (oid, classid, inLrt) SELECT DISTINCT C.AHOID, C.AHCLASSID, C.INLRT FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE C.INLRT IS NOT NULL AND (' || v_colConditions || ')';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "v_colConditions"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "retrieve Expression-OIDs", 1)
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempExpOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "T."; g_anAhOid
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName; " C"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameTerm; " T"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "C.oid = T.CCRCDE_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(1); ";"
 
 
   genProcSectionHeader(fileNo, "check GenericAspects referring to mapped Expressions", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " AS c_orgId,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityId; " = A."; g_anAcmEntityId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexExpression); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " <> AFK.REFENTITYID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(A.ENTITYID <> '"; getClassIdStrByIndex(g_classIndexTaxParameter); "' OR (L.ISGEN = 1 AND A.ENTITYID = '"; getClassIdStrByIndex(g_classIndexTaxParameter); "'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.ID = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " ASC"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2)
   Print #fileNo, addTab(2); "IF c_tabName = '"; Mid(qualTabNameGenericAspect, InStr(1, qualTabNameGenericAspect, ".") + 1); "' THEN"
   Print #fileNo, addTab(3); "SET v_colConditions = 'C.VALEXP_OID_NATIONAL IN (SELECT OID FROM "; tempExpOidTabName; ")';"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_colConditions = '';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "AFK.FKCOL AS c_fkCol"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexExpression); "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " = c_tabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "FKCOL ASC"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' OR ' END) || '(C.' || RTRIM(c_fkCol) || ' IN (SELECT oid FROM "; tempExpOidTabName; "))';"
   Print #fileNo, addTab(2); "END FOR;"
   genProcSectionHeader(fileNo, "determine locked affected objects", 2)
   Print #fileNo, addTab(2); "IF c_tabSchemaName = '"; genSchemaName(snDecision, ssnDecision, ddlType, thisOrgIndex, thisPoolIndex); "' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; tempAffectedObjectsTabName; " (oid, classid, inLrt) SELECT DISTINCT GA.AHOID, GA.AHCLASSID, GA.INLRT FROM VL6CASP011.GENERICASPECT GA WHERE GA.AHOID IN (SELECT DISTINCT C.AHOID FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE PS_OID = ' || v_psOid || ' AND (' || v_colConditions || ')) AND INLRT IS NOT NULL';"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; tempAffectedObjectsTabName; " (oid, classid, inLrt) SELECT DISTINCT C.AHOID, C.AHCLASSID, C.INLRT FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE PS_OID = ' || v_psOid || ' AND INLRT IS NOT NULL AND (' || v_colConditions || ')';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); tempAffectedObjectsTabName; " S"
   Print #fileNo, addTab(1); "SET "
   Print #fileNo, addTab(2); "tr = ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "L.TRNUMBER "
   Print #fileNo, addTab(3); "FROM "
   Print #fileNo, addTab(4); qualTabNameLrt; " L"
   Print #fileNo, addTab(3); "WHERE "
   Print #fileNo, addTab(4); "S.inLrt = L.OID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "tr IS NULL"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); tempAffectedObjectsTabName; " S"
   Print #fileNo, addTab(1); "SET "
   Print #fileNo, addTab(2); "owner = ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "U.CDUSERID "
   Print #fileNo, addTab(3); "FROM ";
   Print #fileNo, addTab(4); qualTabNameLrt; " L "
   Print #fileNo, addTab(3); "JOIN ";
   Print #fileNo, addTab(4); qualTabNameUser; " U "
   Print #fileNo, addTab(3); "ON "
   Print #fileNo, addTab(4); "U.OID = L.UTROWN_OID "
   Print #fileNo, addTab(3); "WHERE "
   Print #fileNo, addTab(4); "S.inLrt = L.OID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "owner IS NULL"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
 
   Print #fileNo, addTab(1); "SET v_restmntTxt = 'SELECT DISTINCT oid, classid, owner, tr FROM "; tempAffectedObjectsTabName; "';"
   Print #fileNo, addTab(1); "PREPARE v_restmnt FROM v_restmntTxt;"
   Print #fileNo, addTab(1); "OPEN c_return;"

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
 
 Private Sub genModifyCodeTypeSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Modification of CodeType is only supported at 'pool-level'
     Exit Sub
   End If
 
   If (thisOrgIndex <> g_primaryOrgIndex) Then
     ' Modification of CodeType is only supported in factory
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Modification of CodeType only supported in data pools supporting LRT
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   Dim qualTabNameLrtNlText As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)
   qualTabNameLrtNlText = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameGenericCodeLrt As String
   qualTabNameGenericCodeLrt = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualProcNameSetLock As String
   qualProcNameSetLock = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "OTHERS")
   Dim qualProcNameResetLock As String
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "OTHERS")
 
   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors

   ' ####################################################################################################################
   ' #    SP for Modifying the Type of Codes
   ' ####################################################################################################################

   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcNameModifyCodeType As String
   Dim qualProcNameModifyCodeTypeIntern As String
   qualProcNameModifyCodeType = genQualProcName(g_sectionIndexAliasLrt, spnModifyCodeType, ddlType, thisOrgIndex, thisPoolIndex)
   qualProcNameModifyCodeTypeIntern = qualProcNameModifyCodeType
   printSectionHeader("SP for 'Modifying the Type of Codes'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameModifyCodeTypeIntern
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", True, "list of Code-Numbers to modify")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of 'current' Product Structure")
   genProcParm(fileNo, "OUT", "lrtOid_out", g_dbtOid, True, "OID of the LRT used for any data manipulation (implicitly opened)")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being affected (sum over all tables)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_codeNumberIllegalType", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_lockedCodeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_unknownCodeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_dataPoolDescrStringWdp", "VARCHAR(4000)", "NULL")
   genVarDecl(fileNo, "v_dataPoolDescrStringPdp", "VARCHAR(4000)", "NULL")
   genVarDecl(fileNo, "v_thisAccessMode", g_dbtEnumId, "0")
   genVarDecl(fileNo, "v_numPs", "INTEGER", "0")
   genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_gwspError", "VARCHAR(256)", "NULL")
   genVarDecl(fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL")
   genVarDecl(fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempCodeOid(fileNo, , True, True, True)

   genSpLogProcEnter(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, , "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET lrtOid_out   = NULL;"
   Print #fileNo, addTab(1); "SET v_currentTs  = CURRENT TIMESTAMP;"

   genProcSectionHeader(fileNo, "determine Division-OID")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "PDIDIV_OID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_divisionOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = psOid_in"
   Print #fileNo, addTab(1); "WITH UR;"

   genProcSectionHeader(fileNo, "make sure that we found Division")
   Print #fileNo, addTab(1); "IF (v_divisionOid IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                              "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(psOid_in))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "retrieve Code-numbers from list")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "codeNumber"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "RTRIM(LTRIM(E.elem))"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "E.elem IS NOT NULL AND E.elem <> ''"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "if no code number is given there is nothing to do")
   Print #fileNo, addTab(1); "IF (SELECT COUNT(*) FROM "; tempCodeOidTabName; ") = 0 THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                              "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("procParamEmpty", fileNo, 2, "codeNumberList_in")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "map OIDs to Code-numbers (using REPEATABLE READ)", 1)
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); tempCodeOidTabName; " T"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "T.oid = ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); g_anOid
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C.CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anCodeNumber; " = T.codeNumber"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "WITH RR;"
 
   genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1)
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "codeNumber"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_unknownCodeNumber"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "oid IS NULL"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_unknownCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_unknownCodeNumber))")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "verify that all Codes are ""HilfsCode""", 1)
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "S.codeNumber"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_codeNumberIllegalType"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName; " S"
   Print #fileNo, addTab(1); "JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " GC"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "GC."; g_anOid; " = S."; g_anOid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "GC.CTYTYP_OID <> 128"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_codeNumberIllegalType IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("codeNotHilfsCode", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_codeNumberIllegalType))")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "determine OID of Organization", 1)
   Print #fileNo, addTab(1); "SELECT ORGOID INTO v_orgOid FROM "; g_qualTabNamePdmOrganization; " WHERE ID = "; genOrgId(thisOrgIndex, ddlType, True); " WITH UR;"
 
   genProcSectionHeader(fileNo, "determine data pool descriptor string for all ProductStructures in division", 1)
   Print #fileNo, addTab(1); "SET v_dataPoolDescrStringWdp = '';"
   Print #fileNo, addTab(1); "SET v_dataPoolDescrStringPdp = '';"
   Print #fileNo, addTab(1); "SET v_numPs = 0;"
   Print #fileNo, addTab(1); "FOR psLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_numPs = v_numPs + 1;"
   Print #fileNo, addTab(2); "SET v_dataPoolDescrStringWdp ="
   Print #fileNo, addTab(3); "v_dataPoolDescrStringWdp ||"
   Print #fileNo, addTab(3); "(CASE v_dataPoolDescrStringWdp WHEN '' THEN '' ELSE '|' END) ||"
   Print #fileNo, addTab(3); "RTRIM(CHAR(v_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR("; CStr(g_workDataPoolId); "));"
   Print #fileNo, addTab(2); "SET v_dataPoolDescrStringPdp ="
   Print #fileNo, addTab(3); "v_dataPoolDescrStringPdp ||"
   Print #fileNo, addTab(3); "(CASE v_dataPoolDescrStringPdp WHEN '' THEN '' ELSE '|' END) ||"
   Print #fileNo, addTab(3); "RTRIM(CHAR(v_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR("; CStr(g_productiveDataPoolId); "));"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "loop over work and productive data pools and lock", 1)
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameSetLock; "(?,' || '''<admin>'', ? ,' || '''update code type'', ?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,

   Print #fileNo, addTab(1); "SET v_thisAccessMode = "; CStr(g_workDataPoolId); ";"
   Print #fileNo, addTab(1); "WHILE v_thisAccessMode IS NOT NULL DO"
   Print #fileNo, addTab(2); "IF v_thisAccessMode = "; CStr(g_workDataPoolId); " THEN"
   Print #fileNo, addTab(3); "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringWdp, cdUserId_in;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringPdp, cdUserId_in;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader(fileNo, "insist on data pools being locked", 2)
   Print #fileNo, addTab(2); "IF v_numDataPools <> v_numPs THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("setRel2ProdLocksFail", fileNo, 3, "OTHER", , , , , , , , , "RTRIM(CHAR(v_orgOid))", "RTRIM(CHAR(v_thisAccessMode))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET v_thisAccessMode = (CASE v_thisAccessMode WHEN "; CStr(g_workDataPoolId); " THEN "; CStr(g_productiveDataPoolId); " ELSE NULL END);"
   Print #fileNo, addTab(1); "END WHILE;"

   genProcSectionHeader(fileNo, "begin a new LRT", 1)
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualLrtBeginProcName; "(?,?,?,0,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "lrtOid_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "cdUserId_in,"
   Print #fileNo, addTab(2); "trNumber_in,"
   Print #fileNo, addTab(2); "psOid_in"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "lock Codes")
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "C."; g_anInLrt; " = lrtOid_out"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "C.CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C."; g_anInLrt; " IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C."; g_anOid; " IN (SELECT oid FROM "; tempCodeOidTabName; ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "verify that all Codes are locked by this LRT")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "C."; g_anCodeNumber
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_lockedCodeNumber"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "C.CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C."; g_anInLrt; " <> lrtOid_out"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C."; g_anOid; " IN (SELECT oid FROM "; tempCodeOidTabName; ")"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF (v_lockedCodeNumber IS NOT NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("lrtLockAlreadyLockedDetail", fileNo, 2, qualTabNameGenericCode, , , , , , , , , "v_lockedCodeNumber")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "copy public Codes to private / update CodeType")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameGenericCodeLrt
   Print #fileNo, addTab(1); "("

   genAttrListForEntity(g_classIndexGenericCode, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, , edomListLrt)

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   initAttributeTransformation(transformation, 7, , , , "PUB.")
   setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusUpdated))
   setAttributeMapping(transformation, 2, "CTYTYP_OID", "100")
   setAttributeMapping(transformation, 3, conStatusId, CStr(statusWorkInProgress))
   setAttributeMapping(transformation, 4, conLrtComment, "CAST(NULL AS VARCHAR(1))")
   setAttributeMapping(transformation, 5, conLastUpdateTimestamp, "v_currentTs")
   setAttributeMapping(transformation, 6, conUpdateUser, "cdUserId_in")
   setAttributeMapping(transformation, 7, conVersionId, "PUB." & g_anVersionId & " + 1")

   genTransformedAttrListForEntity(g_classIndexGenericCode, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt)

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " PUB"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "PUB.CDIDIV_OID = v_divisionOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PUB."; UCase(g_anInLrt); " = lrtOid_out"
   Print #fileNo, addTab(1); ";"
 
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     genProcSectionHeader(fileNo, "register all relevant entities as being affected by the LRT")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(1); "("

     genAttrListForEntity(g_classIndexLrtAffectedEntity, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt)
 
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation(transformation, 6, , , , "PUB.")
     setAttributeMapping(transformation, 1, conLrtOid, "lrtOid_out")
     setAttributeMapping(transformation, 2, conAcmOrParEntityId, "'" & CStr(g_classes.descriptors(g_classIndexGenericCode).classIdStr) & "'")
     setAttributeMapping(transformation, 3, conAcmEntityType, "'" & gc_acmEntityTypeKeyClass & "'")
     setAttributeMapping(transformation, 4, conLdmIsGen, gc_dbFalse)
     setAttributeMapping(transformation, 5, conLdmIsNl, gc_dbFalse)
     setAttributeMapping(transformation, 6, conLrtOpId, CStr(lrtStatusUpdated))

     genTransformedAttrListForEntity(g_classIndexLrtAffectedEntity, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt)

     Print #fileNo, addTab(1); ");"
   Else
     ' ???
   End If

   genProcSectionHeader(fileNo, "create LRT-comment")

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameLrtNlText
   Print #fileNo, addTab(1); "("
 
   genNlsAttrDeclsForEntity(g_classIndexLrt, eactClass, fileNo, qualTabNameLrtNlText, , ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListNonLrt)

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "VALUES"
   Print #fileNo, addTab(1); "("

   initAttributeTransformation(transformation, 6)

   setAttributeMapping(transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid)
   setAttributeMapping(transformation, 2, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexLrt).shortName), "lrtOid_out")
   setAttributeMapping(transformation, 3, conLanguageId, "1")
   setAttributeMapping(transformation, 4, conTransactionComment, "'Map CODETYPE: Hilfscode -> Ausstattungscode'")
   setAttributeMapping(transformation, 5, conVersionId, "1")
   setAttributeMapping(transformation, 6, conPsOid, "psOid_in")

   genNlsTransformedAttrListForEntity(g_classIndexLrt, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , _
     edomListNonLrt)
 
   Print #fileNo, addTab(1); ");"
 
   Print #fileNo,

   Dim qualProcNameLrtCommit As String
   qualProcNameLrtCommit = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)

   genProcSectionHeader(fileNo, "commit LRT")
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameLrtCommit; "(?,0,?,?,?,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"

   Print #fileNo, addTab(2); "rowCount_out,"
   Print #fileNo, addTab(2); "v_gwspError,"
   Print #fileNo, addTab(2); "v_gwspInfo,"
   Print #fileNo, addTab(2); "v_gwspWarning"

   Print #fileNo, addTab(1); "USING"

   Print #fileNo, addTab(2); "lrtOid_out"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "loop over work and productive data pools and unlock", 1)
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "(?,' || '''<admin>'', ? ,' || '''update code type'', ?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,

   Print #fileNo, addTab(1); "SET v_thisAccessMode = "; CStr(g_workDataPoolId); ";"
   Print #fileNo, addTab(1); "WHILE v_thisAccessMode IS NOT NULL DO"
   Print #fileNo, addTab(2); "IF v_thisAccessMode = "; CStr(g_workDataPoolId); " THEN"
   Print #fileNo, addTab(3); "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringWdp, cdUserId_in;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringPdp, cdUserId_in;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader(fileNo, "insist on data pools being unlocked", 2)
   Print #fileNo, addTab(2); "IF v_numDataPools <> v_numPs THEN"
   genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")
   genSignalDdlWithParms("resetRel2ProdLocksFail", fileNo, 3, "OTHER", , , , , , , , , "RTRIM(CHAR(v_orgOid))", "RTRIM(CHAR(v_thisAccessMode))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET v_thisAccessMode = (CASE v_thisAccessMode WHEN "; CStr(g_workDataPoolId); " THEN "; CStr(g_productiveDataPoolId); " ELSE NULL END);"
   Print #fileNo, addTab(1); "END WHILE;"

   genSpLogProcExit(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 1, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", _
                             "psOid_in", "lrtOid_out", "rowCount_out")

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
 
 
 Private Sub genDeleteProdCodeSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportSectionDataFix Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Modification of CodeType is only supported at 'pool-level'
     Exit Sub
   End If
 
   If (thisOrgIndex <> g_primaryOrgIndex) Then
     ' Modification of CodeType is only supported in factory
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Modification of CodeType only supported in data pools supporting LRT
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   Dim qualTabNameLrtNlText As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)
   qualTabNameLrtNlText = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameGenericCodeLrt As String
   qualTabNameGenericCodeLrt = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualViewNameAcmEntityFkCol As String
   qualViewNameAcmEntityFkCol = genQualViewName(g_sectionIndexDbMeta, vnAcmEntityFkCol, vnsAcmEntityFkCol, ddlType)

   Dim qualProcNameSetLock As String
   qualProcNameSetLock = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "EXCLUSIVEWRITE")
   Dim qualProcNameResetLock As String
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "EXCLUSIVEWRITE")
 
   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors

   Dim qualProcNameGenWs As String
   qualProcNameGenWs = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType)

   ' ####################################################################################################################
   ' #    SP for Deleteting a Set of Productive Codes
   ' ####################################################################################################################

   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcNameDeleteProdCode As String
   qualProcNameDeleteProdCode = genQualProcName(g_sectionIndexDataFix, spnDeleteProductiveCode, ddlType, thisOrgIndex, thisPoolIndex)
   printSectionHeader("SP for 'Deleteting a Set of Productive Codes'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDeleteProdCode
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", True, "list of Code-Numbers to delete")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "divOid_in", g_dbtOid, True, "OID of Division")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of Codes being delete")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "fkViolationOnDelete", "23504")

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_processedCodeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_thisCodeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_tabNameChangeLog", "VARCHAR(80)", "NULL")
   genVarDecl(fileNo, "v_tabNameGenericCode", "VARCHAR(80)", "NULL")
   genVarDecl(fileNo, "v_callCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE codeCursor CURSOR FOR v_stmnt;"

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR fkViolationOnDelete"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader(fileNo, "declare variables", 2, True)
 
   genVarDecl(fileNo, "v_tabSchema", g_dbtDbSchemaName, "NULL", 2)
   genVarDecl(fileNo, "v_tabName", g_dbtDbTableName, "NULL", 2)
   genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL", 2)
 
   genProcSectionHeader(fileNo, "retrieve diagnostics string", 2)
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"

   genProcSectionHeader(fileNo, "if we are not currently processing a Code we do not process exception message", 2)
   Print #fileNo, addTab(2); "IF v_processedCodeNumber IS NULL THEN"
   Print #fileNo, addTab(3); "ROLLBACK;"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "parse diagnostics string", 2)
   Print #fileNo, addTab(2); "FOR elemLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "ELEM AS c_elem,"
   Print #fileNo, addTab(4); "POSINDEX AS c_pos"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(v_diagnostics, CAST('.' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "ELEM IS NOT NULL"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF     c_pos = 0 THEN SET v_tabSchema = COALESCE(CAST(c_elem AS "; g_dbtDbSchemaName; "),'??');"
   Print #fileNo, addTab(3); "ELSEIF c_pos = 1 THEN SET v_tabName   = COALESCE(CAST(c_elem AS VARCHAR(50)),'??');"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader(fileNo, "signal MDS-message", 2)
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, -2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("fkViolationOnDelete", fileNo, 2, "Code", , , , , , , , , "v_processedCodeNumber", "v_tabSchema || '.' || v_tabName")
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempCodeOid(fileNo, , True, True, True, True)
   genDdlForTempDataPool(fileNo, , True, True, True)

   genSpLogProcEnter(fileNo, qualProcNameDeleteProdCode, ddlType, , "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNo, "make sure that Division is valid")
   Print #fileNo, addTab(1); "IF (SELECT 1 FROM "; g_qualTabNameDivision; " WHERE "; g_anOid; " = divOid_in) IS NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("divNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(divOid_in))")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "make sure that CD-User is valid")
   Print #fileNo, addTab(1); "IF (SELECT 1 FROM "; g_qualTabNameUser; " WHERE "; g_anUserId; " = cdUserId_in) IS NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("userUnknown", fileNo, 2, , , , , , , , , , "cdUserId_in")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "retrieve Code-numbers from list")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "codeNumber"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "RTRIM(LTRIM(E.elem))"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "E.elem IS NOT NULL AND E.elem <> ''"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "if no code number is given there is nothing to do")
   Print #fileNo, addTab(1); "IF (SELECT COUNT(*) FROM "; tempCodeOidTabName; ") = 0 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("procParamEmpty", fileNo, 2, "codeNumberList_in")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "map OIDs to Code-numbers", 1)
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); tempCodeOidTabName; " T"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "T.oid,"
   Print #fileNo, addTab(3); "T.hasBeenSetProductive"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); g_anOid; ","
   Print #fileNo, addTab(4); g_anHasBeenSetProductive
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C.CDIDIV_OID = divOid_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anCodeNumber; " = T.codeNumber"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1)
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "codeNumber"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_thisCodeNumber"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "oid IS NULL"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_thisCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, , , , , , , , , , "v_thisCodeNumber")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "verify that all Code-numbers are productive", 1)
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "codeNumber"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_thisCodeNumber"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); tempCodeOidTabName
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "COALESCE(hasBeenSetProductive, "; gc_dbFalse; ") = "; gc_dbFalse
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_thisCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("codeNumberNotProductive", fileNo, 2, , , , , , , , , , "v_thisCodeNumber")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "determine work data pools holding at least one of the referred Codes", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " AS c_poolTypeId,"
   Print #fileNo, addTab(3); "O.ID AS c_orgId,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader(fileNo, "check if this data pool holds at least one of the referred Codes", 2, True)
   Print #fileNo, addTab(2); "SET v_thisCodeNumber = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT T.codeNumber FROM ' || c_tabSchemaName || '.' || c_tabName || ' C INNER JOIN "; _
                             tempCodeOidTabName; " T ON C."; g_anOid; " = T.oid FETCH FIRST 1 ROW ONLY';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN codeCursor;"
   Print #fileNo, addTab(2); "FETCH codeCursor INTO v_thisCodeNumber;"
   Print #fileNo, addTab(2); "CLOSE codeCursor WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_thisCodeNumber IS NOT NULL THEN"
   genProcSectionHeader(fileNo, "keep track of associated data pools (assume that (pre)productive pool holds Code if work data pool does)", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempDataPoolTabName
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "orgId,"
   Print #fileNo, addTab(4); "orgOid,"
   Print #fileNo, addTab(4); "psOid,"
   Print #fileNo, addTab(4); "accessModeId"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "c_orgId,"
   Print #fileNo, addTab(4); "c_orgOid,"
   Print #fileNo, addTab(4); "PS."; g_anOid; ","
   Print #fileNo, addTab(4); "S."; g_anPoolTypeId
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameProductStructure; " PS,"
   Print #fileNo, addTab(4); g_qualTabNamePdmPrimarySchema; " S"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "S."; g_anOrganizationId; " = c_orgId"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "S."; g_anPoolTypeId; " IN ("; CStr(g_workDataPoolId); ","; CStr(g_productiveDataPoolId); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "PS.PDIDIV_OID = divOid_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "verify that none of the Codes itself is involved in some LRT", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.ID IN (SELECT orgId FROM "; tempDataPoolTabName; ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2, True)
   Print #fileNo, addTab(2); "SET v_thisCodeNumber = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT C."; g_anInLrt; ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || c_tabName || ' C INNER JOIN "; _
                             tempCodeOidTabName; " T ON C."; g_anOid; " = T.oid';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN codeCursor;"
   Print #fileNo, addTab(2); "FETCH codeCursor INTO v_lrtOid, v_thisCodeNumber;"
   Print #fileNo, addTab(2); "CLOSE codeCursor WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_thisCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, , , , , , , , , , "v_thisCodeNumber", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtOid))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "verify that no LRT refers to any of the Codes", 1)
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " AS c_orgId,"
   Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityId; " = A."; g_anAcmEntityId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "O.ID = P."; g_anOrganizationId
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " <> AFK.REFENTITYID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.ID IN (SELECT orgId FROM "; tempDataPoolTabName; ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " ASC,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " ASC"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, True)
   Print #fileNo, addTab(2); "SET v_colConditions = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "AFK.FKCOL AS c_fkCol"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameAcmEntityFkCol; " AFK"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "AFK.REFENTITYTYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AFK.REFENTITYID = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = c_orgId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " = c_tabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "FKCOL ASC"
   Print #fileNo, addTab(2); "DO"
 
   Print #fileNo, addTab(3); "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' AND ' END) || '(C.' || RTRIM(c_fkCol) || ' = T.oid)';"

   Print #fileNo, addTab(2); "END FOR;"
 
   genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2)
   Print #fileNo, addTab(2); "SET v_thisCodeNumber = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT C."; g_anInLrt; ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || c_tabName || ' C INNER JOIN "; _
                             tempCodeOidTabName; " T ON ' || v_colConditions;"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN codeCursor;"
   Print #fileNo, addTab(2); "FETCH codeCursor INTO v_lrtOid, v_thisCodeNumber;"
   Print #fileNo, addTab(2); "CLOSE codeCursor WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_thisCodeNumber IS NOT NULL THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, , , , , , , , , , "v_thisCodeNumber", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtOid))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "exclusively lock each involved data pool", 1)
   Print #fileNo, addTab(1); "FOR poolLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "orgOid AS c_orgOid,"
   Print #fileNo, addTab(3); "psOid AS c_psOid,"
   Print #fileNo, addTab(3); "accessModeId AS c_accessModeId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempDataPoolTabName
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "orgId ASC,"
   Print #fileNo, addTab(3); "psOid ASC,"
   Print #fileNo, addTab(3); "accessModeId ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "lock this data pool", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameSetLock; "(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',''<admin>'', ? ,''delete productive Codes'', ?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "EXECUTE v_stmnt INTO v_numDataPools USING cdUserId_in;"

   genProcSectionHeader(fileNo, "insist on data pool being locked", 2)
   Print #fileNo, addTab(2); "IF v_numDataPools <> 1 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("setRel2ProdLockFail", fileNo, 3, "EXCLUSIVEWRITE", , , , , , , , , "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "loop over organizations and accessmodes to delete Codes", 1)
   Print #fileNo, addTab(1); "FOR poolLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "orgId AS c_orgId,"
   Print #fileNo, addTab(3); "accessModeId AS c_accessModeId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempDataPoolTabName
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "orgId ASC,"
   Print #fileNo, addTab(3); "accessModeId ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "determine ChangeLog- and GenericCode-table for this data pool", 2, True)
   Print #fileNo, addTab(2); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "RTRIM(P."; g_anPdmFkSchemaName; ") || '.' || P."; g_anPdmTableName; " AS c_qualTabName,"
   Print #fileNo, addTab(4); "(CASE A."; g_anAcmEntityId; " WHEN '"; getClassIdStrByIndex(g_classIndexChangeLog); "' THEN 1 "; _
                             "WHEN '"; getClassIdStrByIndex(g_classIndexGenericCode); "' THEN 2 "; _
                             "ELSE 4 END) AS c_seqNo"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("; _
                             "'"; getClassIdStrByIndex(g_classIndexChangeLog); "', "; _
                             "'"; getClassIdStrByIndex(g_classIndexGenericCode); "')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = c_orgId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " = c_accessModeId"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "IF c_seqNo = 1 THEN"
   Print #fileNo, addTab(4); "SET v_tabNameChangeLog = c_qualTabName;"
   Print #fileNo, addTab(3); "ELSEIF c_seqNo = 2 THEN"
   Print #fileNo, addTab(4); "SET v_tabNameGenericCode = c_qualTabName;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader(fileNo, "create changelog entries for Codes to delete", 2)
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "v_tabNameChangeLog ||"
   Print #fileNo, addTab(3); "'(' ||"
   Print #fileNo, addTab(4); "'OID,' ||"
   Print #fileNo, addTab(4); "'"; g_anAcmEntityId; ",' ||"
   Print #fileNo, addTab(4); "'"; g_anAcmEntityType; ",' ||"
   Print #fileNo, addTab(4); "'"; g_anAhCid; ",' ||"
   Print #fileNo, addTab(4); "'AHOBJECTID,' ||"
   Print #fileNo, addTab(4); "'GEN,' ||"
   Print #fileNo, addTab(4); "'NL,' ||"
   Print #fileNo, addTab(4); "'DBTABLENAME,' ||"
   Print #fileNo, addTab(4); "'OBJECTID,' ||"
   Print #fileNo, addTab(4); "'"; g_anValidFrom; ",' ||"
   Print #fileNo, addTab(4); "'"; g_anValidTo; ",' ||"
   Print #fileNo, addTab(4); "'BASECODENUMBER,' ||"
   Print #fileNo, addTab(4); "'BASECODETYPE,' ||"
   Print #fileNo, addTab(4); "'CODEKIND_ID,' ||"
   Print #fileNo, addTab(4); "'DIVISIONOID,' ||"
   Print #fileNo, addTab(4); "'OPERATION_ID,' ||"
   Print #fileNo, addTab(4); "'OPTIMESTAMP,' ||"
   Print #fileNo, addTab(4); "'"; g_anUserId; "' ||"
   Print #fileNo, addTab(3); "') ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"
   Print #fileNo, addTab(4); "'NEXTVAL FOR "; g_schemaNameCtoMeta; "' || RIGHT(DIGITS(c_orgId), 2) || '."; getUnqualObjName(qualSeqNameOid); ",' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anCid; ",' ||"
   Print #fileNo, addTab(4); "'''"; gc_acmEntityTypeKeyClass; "'',' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anAhCid; ",' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anAhOid; ",' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'''"; getUnqualObjName(qualTabNameGenericCode); "'',' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anOid; ",' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anValidFrom; ",' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anValidTo; ",' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anCodeNumber; ",' ||"
   Print #fileNo, addTab(4); "'(SELECT T.CODETYPENUMBER FROM "; g_qualTabNameCodeType; " T WHERE T."; g_anOid; " = CDE.CTYTYP_OID),' ||"
   Print #fileNo, addTab(4); "'(CASE CDE."; g_anIsNational; " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),' ||"
   Print #fileNo, addTab(4); "'CDE.CDIDIV_OID,' ||"
   Print #fileNo, addTab(4); "'"; CStr(lrtStatusDeleted); ",' ||"
   Print #fileNo, addTab(4); "'CURRENT TIMESTAMP,' ||"
   Print #fileNo, addTab(4); "'''' || cdUserId_in || ''' ' ||"

   Print #fileNo, addTab(3); "'FROM ' ||"

   Print #fileNo, addTab(4); "v_tabNameGenericCode || ' CDE ' ||"
   Print #fileNo, addTab(3); "'INNER JOIN ' ||"
   Print #fileNo, addTab(4); "'"; tempCodeOidTabName; " O ' ||"
   Print #fileNo, addTab(3); "'ON ' ||"
   Print #fileNo, addTab(4); "'CDE."; g_anOid; " = O.oid ' "
 
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader(fileNo, "loop over tables to delete Codes", 2)
   Print #fileNo, addTab(2); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_tabSchemaName,"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE WHEN A."; g_anAcmEntityId; " = '"; getClassIdStrByIndex(g_classIndexNotice); "' AND A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(6); "THEN 'CNOBCO_OID'"
   Print #fileNo, addTab(6); "ELSE '"; g_anAhOid; "'"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") AS c_colName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "A."; g_anAhCid; " = '"; getClassIdStrByIndex(g_classIndexGenericCode); "'"
   Print #fileNo, addTab(6); "OR"
   genProcSectionHeader(fileNo, "special treatment of 'Notes': they do not prohibit delete of Codes", 5, True)
   Print #fileNo, addTab(5); "A."; g_anAcmEntityId; " = '"; getClassIdStrByIndex(g_classIndexNotice); "' AND A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = c_orgId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " = c_accessModeId"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "L."; g_anLdmFkSequenceNo; " DESC"
   Print #fileNo, addTab(2); "DO"

   genProcSectionHeader(fileNo, "delete Codes individually in order to be able to name Codes in error messages", 3, True)
   Print #fileNo, addTab(3); "FOR codeLoop AS"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "oid AS c_codeOid,"
   Print #fileNo, addTab(5); "codeNumber AS c_codeNumber"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempCodeOidTabName
   Print #fileNo, addTab(3); "DO"
   genProcSectionHeader(fileNo, "delete Code", 4, True)
   Print #fileNo, addTab(4); "SET v_processedCodeNumber = c_codeNumber;"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'DELETE FROM ' || c_tabSchemaName || '.' || c_tabName || ' WHERE ' || c_colName || ' = ' || RTRIM(CHAR(c_codeOid));"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader(fileNo, "count the number of affected rows", 4)
   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(3); "END FOR;"

   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "'deactivate' continue handler for FK-violation", 1)
   Print #fileNo, addTab(1); "SET v_processedCodeNumber = NULL;"
 
   genProcSectionHeader(fileNo, "call GENWORKSPACE in each involved data pool", 1)
   Print #fileNo, addTab(1); "FOR poolLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "orgId AS c_orgId,"
   Print #fileNo, addTab(3); "orgOid AS c_orgOid,"
   Print #fileNo, addTab(3); "psOid AS c_psOid,"
   Print #fileNo, addTab(3); "accessModeId AS c_accessModeId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempDataPoolTabName
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "orgId ASC,"
   Print #fileNo, addTab(3); "psOid ASC,"
   Print #fileNo, addTab(3); "accessModeId ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "call GENWORKSPACE for this data pool", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameGenWs; "(2, ?, ?, ?, 0, 0, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_callCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "c_orgId,"
   Print #fileNo, addTab(3); "c_psOid,"
   Print #fileNo, addTab(3); "c_accessModeId"
   Print #fileNo, addTab(2); ";"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "unlock each involved data pool", 1)
   Print #fileNo, addTab(1); "FOR poolLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "orgOid AS c_orgOid,"
   Print #fileNo, addTab(3); "psOid AS c_psOid,"
   Print #fileNo, addTab(3); "accessModeId AS c_accessModeId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempDataPoolTabName
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "orgId ASC,"
   Print #fileNo, addTab(3); "psOid ASC,"
   Print #fileNo, addTab(3); "accessModeId ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "unlock this data pool", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',''<admin>'', ? ,''delete productive Codes'', ?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "EXECUTE v_stmnt INTO v_numDataPools USING cdUserId_in;"

   genProcSectionHeader(fileNo, "insist on data pool being unlocked", 2)
   Print #fileNo, addTab(2); "IF v_numDataPools <> 1 THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")
   genSignalDdlWithParms("resetRel2ProdLockFail", fileNo, 3, "EXCLUSIVEWRITE", , , , , , , , , "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))")
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out")

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
 
 
 Private Sub genDeleteTechAspectSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportSectionDataFix Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Delete of Technical Aspect is only supported at 'pool-level'
     Exit Sub
   End If
 
   If (thisOrgIndex <> g_primaryOrgIndex) Then
     ' Delete of Technical Aspect is only supported in factory
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Delete of Technical Aspect only supported in data pools supporting LRT
     Exit Sub
   End If

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericAspectPub As String
   qualTabNameGenericAspectPub = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   Dim unqualTabNameGenericAspectPub As String
   unqualTabNameGenericAspectPub = getUnqualObjName(qualTabNameGenericAspectPub)
   Dim qualTabNameGenericAspectPriv As String
   qualTabNameGenericAspectPriv = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualTabNameProperty As String
   qualTabNameProperty = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   ' ####################################################################################################################
   ' #    SP for Deleteting 'Technical Aspects'
   ' ####################################################################################################################

   Dim qualProcNameDeleteTechAspect As String
   qualProcNameDeleteTechAspect = genQualProcName(g_sectionIndexDataFix, spnDeleteTechAspect, ddlType, thisOrgIndex, thisPoolIndex)
   printSectionHeader("SP for 'Deleteting a 'technical' Aspect'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDeleteTechAspect
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the Aspect to delete")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure the Aspect corresponds to")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "tr_in", "BIGINT", False, "logical number of the user's transaction to use for record deletion")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_oid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_lrtEntityIdCount", "INTEGER", "0")
   genSpLogDecl(fileNo)
 
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter(fileNo, qualProcNameDeleteTechAspect, ddlType, 1, "oid_in", "psOid_in", "'cdUserId_in", "tr_in")
 
   genProcSectionHeader(fileNo, "verify that oid_in refers to TechData in the given ProductStructure")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); g_anOid
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_oid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectPub
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = oid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PRPAPR_OID IN ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "P."; g_anOid
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameProperty; " P"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePropertyTemplate; " PT"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PT."; g_anOid; " = P.PTMHTP_OID"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PT.ID IN (1, 2, 4, 5, 9, 43, 157, 186, 187, 188, 189, 190, 191, 192)"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "check whether record was found")
   Print #fileNo, addTab(1); "IF (v_oid IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteTechAspect, ddlType, -2, "oid_in", "psOid_in", "'cdUserId_in", "tr_in")
   genSignalDdlWithParms("objNotFound", fileNo, 2, "Tech.Aspect", unqualTabNameGenericAspectPub, , , , , , , , "RTRIM(CHAR(oid_in))")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "determine user transaction")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "LRT."; g_anOid
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_lrtOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameUser; " USR"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameLrt; " LRT"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "LRT.UTROWN_OID = USR."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "LRT."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LRT.TRNUMBER = tr_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LRT."; g_anEndTime; " IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "USR."; g_anUserId; " = cdUserId_in"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "verify that user transaction exists")
   Print #fileNo, addTab(1); "IF (v_lrtOid IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameDeleteTechAspect, ddlType, -2, "oid_in", "psOid_in", "'cdUserId_in", "tr_in")
   genSignalDdlWithParms("logLrtNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(tr_in))", "cdUserId_in", "RTRIM(CHAR(psOid_in))")
   Print #fileNo, addTab(1); "END IF;"
 
     Dim qualProcNameLrtLockGenericAspect
     qualProcNameLrtLockGenericAspect = genQualProcNameByEntityIndex(g_classes.descriptors(g_classIndexGenericAspect).classIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, , , , , "LRTLOCK")

     genProcSectionHeader(fileNo, "LRT-lock record")
     Print #fileNo, addTab(1); "CALL "; qualProcNameLrtLockGenericAspect; "(v_lrtOid, psOid_in, v_oid, v_rowCount);"
 
     genProcSectionHeader(fileNo, "convert LRT-lock to LRT-delete")
     Print #fileNo, addTab(1); "UPDATE "; qualTabNameGenericAspectPriv; " SET LRTSTATE = "; CStr(lrtStatusDeleted); " WHERE "; g_anOid; " = v_oid AND "; g_anInLrt; " = v_lrtOid;"

     genDdlForUpdateAffectedEntities(fileNo, "ACM-Class", eactClass, gc_acmEntityTypeKeyClass, False, False, qualTabNameLrtAffectedEntity, _
          g_classes.descriptors(g_classIndexGenericAspect).classIdStr, g_classes.descriptors(g_classIndexGenericAspect).classIdStr, "v_lrtOid", 1, CStr(lrtStatusDeleted), False)

   genSpLogProcExit(fileNo, qualProcNameDeleteTechAspect, ddlType, 1, "oid_in", "psOid_in", "'cdUserId_in", "tr_in")
 
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
 
 
 Private Sub genOidMapSql( _
   ddlType As DdlTypeId, _
   ByRef colName As String, _
   ByRef qualTabName As String, _
   ByRef qualSeqNameOid As String, _
   ByRef lrtOidFilterStr As String, _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional ByRef psOidFilterStr As String = "", _
   Optional joinExpOid As Boolean = False _
 )
   genProcSectionHeader(fileNo, gc_sqlDelimLine2, indent + 1)
   genProcSectionHeader(fileNo, "determine new OIDs to be mapped related to column '" & colName & "'", indent + 1, True)

   Print #fileNo, addTab(indent + 1); "DELETE FROM "; tempOidNewTabName; ";"
 
   Print #fileNo,
   Print #fileNo, addTab(indent + 1); "OPEN mapCursor;"
   Print #fileNo, addTab(indent + 1); "SET v_oid   = 0;"
   Print #fileNo, addTab(indent + 1); "SET v_atEnd = "; gc_dbFalse; ";"
   Print #fileNo, addTab(indent + 1); "FETCH mapCursor INTO v_oid, v_map2Oid;"
   Print #fileNo, addTab(indent + 1); "FOR recordLoop AS csr CURSOR FOR"
   If joinExpOid Then
     Print #fileNo, addTab(indent + 2); "SELECT"
     Print #fileNo, addTab(indent + 3); "L."; colName; " AS v_record_"; colName
     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); qualTabName; " L"
     Print #fileNo, addTab(indent + 2); "INNER JOIN"
     Print #fileNo, addTab(indent + 3); tempExpOidTabName; " E"
     Print #fileNo, addTab(indent + 2); "ON"
     Print #fileNo, addTab(indent + 3); "L."; g_anAhOid; " = E.oid"
     Print #fileNo, addTab(indent + 2); "WHERE"
     If lrtOidFilterStr <> "" Then
       Print #fileNo, addTab(indent + 3); "L."; g_anInLrt; " = "; lrtOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
     End If
     If psOidFilterStr <> "" Then
       Print #fileNo, addTab(indent + 3); "L."; g_anPsOid; " = "; psOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
     End If
     Print #fileNo, addTab(indent + 3); "L."; colName; " IS NOT NULL"
     Print #fileNo, addTab(indent + 2); "ORDER BY"
     Print #fileNo, addTab(indent + 3); "L."; colName
   Else
     Print #fileNo, addTab(indent + 2); "SELECT"
     Print #fileNo, addTab(indent + 3); colName; " AS v_record_"; colName
     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); qualTabName
     Print #fileNo, addTab(indent + 2); "WHERE"
     If lrtOidFilterStr <> "" Then
       Print #fileNo, addTab(indent + 3); g_anInLrt; " = "; lrtOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
     End If
     If psOidFilterStr <> "" Then
       Print #fileNo, addTab(indent + 3); g_anPsOid; " = "; psOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
     End If
     Print #fileNo, addTab(indent + 3); colName; " IS NOT NULL"
     Print #fileNo, addTab(indent + 2); "ORDER BY"
     Print #fileNo, addTab(indent + 3); colName
   End If
   Print #fileNo, addTab(indent + 1); "DO"

   Print #fileNo, addTab(indent + 2); "WHILE (v_atEnd = 0) AND (v_record_"; colName; " >= v_oid) DO"
   Print #fileNo, addTab(indent + 3); "FETCH mapCursor INTO v_oid, v_map2Oid;"
   Print #fileNo, addTab(indent + 2); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(indent + 2); "IF (v_atEnd = 1) OR (v_record_"; colName; " < v_oid) THEN"
   Print #fileNo, addTab(indent + 3); "INSERT INTO "; tempOidNewTabName; "(oid) VALUES(v_record_"; colName; ");"
   Print #fileNo, addTab(indent + 2); "END IF;"

   Print #fileNo, addTab(indent + 1); "END FOR;"
   Print #fileNo, addTab(indent + 1); "CLOSE mapCursor WITH RELEASE;"

   genProcSectionHeader(fileNo, "add new OIDs to set of OIDs to be mapped", indent + 1)
   Print #fileNo, addTab(indent + 1); "INSERT INTO"
   Print #fileNo, addTab(indent + 2); tempOidMapTabName
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "oid,"
   Print #fileNo, addTab(indent + 2); "map2Oid"
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 1); "WITH"
   Print #fileNo, addTab(indent + 2); "V"
   Print #fileNo, addTab(indent + 1); "AS"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT DISTINCT"
   Print #fileNo, addTab(indent + 3); "oid"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); tempOidNewTabName
   Print #fileNo, addTab(indent + 1); "),"

   Print #fileNo, addTab(indent + 2); "v_newOid"
   Print #fileNo, addTab(indent + 1); "AS"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); "oid"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); "V"
   Print #fileNo, addTab(indent + 2); "WHERE"
   Print #fileNo, addTab(indent + 3); "NOT EXISTS"
   Print #fileNo, addTab(indent + 4); "( SELECT 1 FROM "; tempOidMapTabName; " M WHERE M.oid = V.oid )"

   Print #fileNo, addTab(indent + 1); ")"

   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "oid,"
   Print #fileNo, addTab(indent + 2); "NEXTVAL FOR "; qualSeqNameOid
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); "v_newOid"
   Print #fileNo, addTab(indent + 1); ";"

   If lrtOidFilterStr <> "" Then
     genProcSectionHeader(fileNo, "map OIDs in column '" & colName & "'", indent + 1)
     Print #fileNo, addTab(indent + 1); "OPEN mapCursor;"
     Print #fileNo, addTab(indent + 1); "SET v_oid = 0;"
     Print #fileNo, addTab(indent + 1); "FOR recordLoop AS csr CURSOR FOR"
     If joinExpOid Then
       Print #fileNo, addTab(indent + 2); "SELECT"
       Print #fileNo, addTab(indent + 3); "L."; colName; " AS v_record_"; colName
       Print #fileNo, addTab(indent + 2); "FROM"
       Print #fileNo, addTab(indent + 3); qualTabName; " L"
       Print #fileNo, addTab(indent + 2); "WHERE"
       Print #fileNo, addTab(indent + 3); "L."; g_anInLrt; " = "; lrtOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "L."; colName; " IS NOT NULL"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "EXISTS(SELECT 1 FROM "; tempExpOidTabName; " E WHERE L."; g_anAhOid; " = E.oid)"
       Print #fileNo, addTab(indent + 2); "ORDER BY"
       Print #fileNo, addTab(indent + 3); "L."; colName
    Else
       Print #fileNo, addTab(indent + 2); "SELECT"
       Print #fileNo, addTab(indent + 3); colName; " AS v_record_"; colName
       Print #fileNo, addTab(indent + 2); "FROM"
       Print #fileNo, addTab(indent + 3); qualTabName
       Print #fileNo, addTab(indent + 2); "WHERE"
       Print #fileNo, addTab(indent + 3); g_anInLrt; " = "; lrtOidFilterStr
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); colName; " IS NOT NULL"
       Print #fileNo, addTab(indent + 2); "ORDER BY"
       Print #fileNo, addTab(indent + 3); colName
     End If
     Print #fileNo, addTab(indent + 2); "FOR UPDATE OF"
     Print #fileNo, addTab(indent + 3); colName
     Print #fileNo, addTab(indent + 1); "DO"

     Print #fileNo, addTab(indent + 2); "WHILE v_oid < v_record_"; colName; " DO"
     Print #fileNo, addTab(indent + 3); "FETCH mapCursor INTO v_oid, v_map2Oid;"
     Print #fileNo, addTab(indent + 2); "END WHILE;"
     Print #fileNo,
     Print #fileNo, addTab(indent + 2); "UPDATE "; qualTabName; " SET "; colName; " = v_map2Oid WHERE CURRENT OF csr;"
     Print #fileNo, addTab(indent + 1); "END FOR;"
 
     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "CLOSE mapCursor WITH RELEASE;"
   End If
 End Sub
 
 
 Private Sub genExpCopySupportDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional includeExtendedEntitySet As Boolean = False _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' PS-Copy is only supported at 'pool-level'
     Exit Sub
   End If
 
   Dim sectionName As String
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim classIndex As Integer
   Dim useSurrogateKey As Boolean
   Dim dbAcmEntityType As String

   Dim joinExpOid As Boolean
   Dim hasAhoidCol As Boolean
   Dim ahoidCol As String

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityTypeDescr = "ACM-Class"
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       classIndex = g_classes.descriptors(acmEntityIndex).classIndex
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
       dbAcmEntityType = gc_acmEntityTypeKeyClass
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityTypeDescr = "ACM-Relationship"
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyRel
       classIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
       useSurrogateKey = useSurrogateKeysForNMRelationships And (g_relationships.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or g_relationships.descriptors(acmEntityIndex).logLastChange)
   Else
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation
   Dim transformationDebug As AttributeListTransformation

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNamePub As String
   qualTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen)
   Dim qualTabNamePriv As String
   qualTabNamePriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True)

   Dim qualViewName As String
   qualViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualExpViewName As String
   qualExpViewName = genQualViewNameByEntityIndex(g_classIndexExpression, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcName As String

   ' ####################################################################################################################
   ' #    SP for copying Expression-records related to a given Product Structure to LRT-table(s) / includes mapping of OIDS
   ' ####################################################################################################################

   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , "EXPCP2LRT")

   printSectionHeader("SP for copying Expression-records of table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """) into private tables / includes OID-mapping", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "ID of the LRT corresponding to this transaction")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
   genProcParm(fileNo, "IN", "currentTs_in", "TIMESTAMP", True, "timestamp of this transaction")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "alreadyExist", "42710")
   genCondDecl(fileNo, "notFound", "02000")

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_oid", g_dbtOid, "0")
   genVarDecl(fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_map2Oid", g_dbtOid, "0")
   genVarDecl(fileNo, "v_stmntText", "VARCHAR(200)", "'SELECT oid, map2Oid FROM " & tempOidMapTabName & " ORDER BY oid FOR READ ONLY'")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
 
   If Not forGen And Not lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     genVarDecl(fileNo, "acRecordCount", "INTEGER", "0")
   End If
   genSpLogDecl(fileNo)
 
   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE mapCursor CURSOR FOR v_stmnt;"
 
   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_atEnd = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempOidMap(fileNo, , , True)
   If (thisOrgIndex > g_primaryOrgIndex) And acmEntityIndex = g_classIndexExpression Then
     genDdlForTempFtoExpOid(fileNo, , True)
   End If
   genDdlForTempExpOid(fileNo)

   joinExpOid = True
   If (thisOrgIndex > g_primaryOrgIndex) Then
     If acmEntityIndex = g_classIndexExpression Then
       Print #fileNo,
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); tempOidMapTabName
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "oid,"
       Print #fileNo, addTab(2); "map2Oid"
       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "I."; g_anOid; ","
       Print #fileNo, addTab(2); "MIN(E."; g_anOid; ")"
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNamePriv; " I"
       Print #fileNo, addTab(1); "JOIN"
       Print #fileNo, addTab(2); qualTabNamePriv; " E"
       Print #fileNo, addTab(1); "ON"
       Print #fileNo, addTab(2); "I."; g_anOid; " <> E."; g_anOid
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I.TERMSTRING = E.TERMSTRING"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I."; g_anInLrt; " = E."; g_anInLrt
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "E.ISINVALID = "; CStr(gc_dbFalse)
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "I."; g_anInLrt; " = lrtOid_in"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I.ISINVALID = "; CStr(gc_dbTrue)
       Print #fileNo, addTab(1); "GROUP BY"
       Print #fileNo, addTab(2); "I."; g_anOid
       Print #fileNo, addTab(1); ";"

       Print #fileNo,
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); tempOidMapTabName
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "oid,"
       Print #fileNo, addTab(2); "map2Oid"
       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "I.EXTTRM_OID,"
       Print #fileNo, addTab(2); "MIN(E.EXTTRM_OID)"
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNamePriv; " I"
       Print #fileNo, addTab(1); "JOIN"
       Print #fileNo, addTab(2); qualTabNamePriv; " E"
       Print #fileNo, addTab(1); "ON"
       Print #fileNo, addTab(2); "I.EXTTRM_OID <> E.EXTTRM_OID"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I.TERMSTRING = E.TERMSTRING"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I."; g_anInLrt; " = E."; g_anInLrt
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "E.ISINVALID = "; CStr(gc_dbFalse)
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "I."; g_anInLrt; " = lrtOid_in"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "I.ISINVALID = "; CStr(gc_dbTrue)
       Print #fileNo, addTab(1); "GROUP BY"
       Print #fileNo, addTab(2); "I.EXTTRM_OID"
       Print #fileNo, addTab(1); ";"
     End If
   End If

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "#currentTs_in", "rowCount_out")

   genProcSectionHeader(fileNo, "copy the 'public records' relate to the given Expressions into 'private table'")
   Print #fileNo, addTab(1); "MERGE INTO"
   Print #fileNo, addTab(2); qualTabNamePriv; " T"
   Print #fileNo, addTab(1); "USING ("
   Print #fileNo, addTab(2); "SELECT"
   initAttributeTransformation(transformation, 8, , True, True, "EN.", , , , , , , , , eacAnyOid)
   setAttributeMapping(transformation, 1, conInLrt, "")
   setAttributeMapping(transformation, 2, conHasBeenSetProductive, "")
   setAttributeMapping(transformation, 3, conStatusId, "")
   setAttributeMapping(transformation, 4, conLrtState, "")
   setAttributeMapping(transformation, 5, conCreateUser, "")
   setAttributeMapping(transformation, 6, conCreateTimestamp, "")
   setAttributeMapping(transformation, 7, conUpdateUser, "")
   setAttributeMapping(transformation, 8, conLastUpdateTimestamp, "")
   genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, True, True, forGen, edomListLrt)
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewName; " EN"
   If acmEntityIndex <> g_classIndexExpression Then
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); qualExpViewName; " EX"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "EN."; g_anAhOid; " = EX."; g_anOid
   End If
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); tempExpOidTabName; " E"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "EN."; g_anAhOid; " = E.oid"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(EN."; g_anInLrt; " = lrtOid_in"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "EN."; g_anInLrt; " IS NULL)"
   'rs41
   If acmEntityIndex = g_classIndexExpression Then
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "ISINVALID = "; CStr(gc_dbFalse)
   End If
   Print #fileNo, addTab(2); ") S"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "T."; g_anOid; " = S."; g_anOid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T."; g_anPsOid; " = S."; g_anPsOid; ""
   Print #fileNo, addTab(1); "WHEN NOT MATCHED THEN"

   Print #fileNo, addTab(2); "INSERT ("

   genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt)

   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES ("

   initAttributeTransformation(transformation, 10, , True, True, "S.", , , , , , , , , eacAnyOid)

   setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusCreated))
   setAttributeMapping(transformation, 2, conInLrt, "lrtOid_in")
   setAttributeMapping(transformation, 3, conHasBeenSetProductive, gc_dbFalse)
   setAttributeMapping(transformation, 4, conStatusId, CStr(statusWorkInProgress))
   setAttributeMapping(transformation, 5, conLrtComment, "CAST(NULL AS VARCHAR(1))")
   setAttributeMapping(transformation, 6, conCreateTimestamp, "currentTs_in")
   setAttributeMapping(transformation, 7, conLastUpdateTimestamp, "currentTs_in")
   setAttributeMapping(transformation, 8, conCreateUser, "cdUserId_in")
   setAttributeMapping(transformation, 9, conUpdateUser, "cdUserId_in")
   setAttributeMapping(transformation, 10, conVersionId, "1")

   genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt)

   Print #fileNo, addTab(2); ")"
   If acmEntityIndex = g_classIndexExpression Then
     Print #fileNo, addTab(1); "WHEN MATCHED AND T.ISINVALID = "; CStr(gc_dbFalse); " THEN"
   Else
     Print #fileNo, addTab(1); "WHEN MATCHED THEN"
   End If
   Print #fileNo, addTab(2); "UPDATE SET (UPDATEUSER, LASTUPDATETIMESTAMP, VERSIONID) = (cdUserId_in,  currentTs_in, T.VERSIONID + 1)"
   Print #fileNo, addTab(1); "ELSE IGNORE"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected rows")
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader(fileNo, "prepare cursor for OID-mapping")
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntText;"

   If forGen And useSurrogateKey Then
     genOidMapSql(ddlType, genAttrName(entityShortName & "_" & g_surrogateKeyNameShort, ddlType), _
                  qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, , joinExpOid)
   End If

   hasAhoidCol = False
     Dim i As Integer
     For i = 1 To transformation.oidDescriptors.numDescriptors
         If (transformation.oidDescriptors.descriptors(i).colCat And (eacFkOidExpElement Or eacOid)) <> 0 Then
           genOidMapSql(ddlType, transformation.oidDescriptors.descriptors(i).colName, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, , joinExpOid)
         End If
         If (transformation.oidDescriptors.descriptors(i).colCat And eacAhOid) <> 0 Then
            hasAhoidCol = True
            ahoidCol = transformation.oidDescriptors.descriptors(i).colName
         End If
     Next i

   If hasAhoidCol Then
     genOidMapSql(ddlType, ahoidCol, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, , joinExpOid)
   End If



     If transformation.nlAttrRefs.numDescriptors > 0 Then
       logMsg("NL-attributes for Expression-tables currently not supported for copy", ellError, ddlType, thisOrgIndex, thisPoolIndex)
     End If

   If Not forGen And Not lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     ' we need to do this only once for the 'non-Gen-class'
     Print #fileNo,
     Print #fileNo, addTab(1); "SET acRecordCount ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "COUNT(*)"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anLrtOid; " = lrtOid_in"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anAcmOrParEntityId; " = '"; entityIdStr
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anAcmEntityType; " = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "OPID = "; CStr(lrtStatusCreated)
     Print #fileNo, addTab(1); ");"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF (acRecordCount = 0) THEN"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); g_anLrtOid; ","
     Print #fileNo, addTab(3); g_anAcmOrParEntityId; ","
     Print #fileNo, addTab(3); g_anAcmEntityType; ","
     Print #fileNo, addTab(3); g_anLrtOpId
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(2); "lrtOid_in,"
     Print #fileNo, addTab(2); "'"; entityIdStr; "',"
     Print #fileNo, addTab(2); "'"; dbAcmEntityType; "',"
     Print #fileNo, addTab(2); CStr(lrtStatusCreated)
     Print #fileNo, addTab(2); ");"
     Print #fileNo, addTab(1); "END IF;"
   End If

   If acmEntityIndex = g_classIndexExpression Then
     genProcSectionHeader(fileNo, "copy the 'public records' relate to the given Expressions into 'private table' to set invalid")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNamePriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt)

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation(transformation, 8, , True, True, "S.", , , , , , , , , eacAnyOid)

     setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusUpdated))
     setAttributeMapping(transformation, 2, conInLrt, "lrtOid_in")
     setAttributeMapping(transformation, 3, conIsInvalid, CStr(gc_dbTrue))
     setAttributeMapping(transformation, 4, conStatusId, CStr(statusWorkInProgress))
     setAttributeMapping(transformation, 5, conLrtComment, "CAST(NULL AS VARCHAR(1))")
     setAttributeMapping(transformation, 6, conLastUpdateTimestamp, "currentTs_in")
     setAttributeMapping(transformation, 7, conUpdateUser, "cdUserId_in")
     setAttributeMapping(transformation, 8, conVersionId, "S.VERSIONID + 1")

     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt)

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNamePub; " S"
     Print #fileNo, addTab(1); "INNER JOIN"
     Print #fileNo, addTab(2); tempExpOidTabName; " E"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "S."; g_anAhOid; " = E.oid"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader(fileNo, "count the number of affected rows")
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
     Print #fileNo,
     Print #fileNo, addTab(1); "UPDATE "
     Print #fileNo, addTab(2); qualTabNamePub; " E"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "E."; g_anInLrt; " = lrtOid_in"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS (SELECT 1 FROM "; qualTabNamePriv; " L WHERE E."; g_anOid; " = L."; g_anOid; ")"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E."; g_anInLrt; " IS NULL"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader(fileNo, "count the number of affected rows")
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
     Print #fileNo,

   End If

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "#currentTs_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 End Sub
 
 
 Sub genExpCopySupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   If generateExpCopySupport And g_classes.descriptors(classIndex).isSubjectToExpCopy Then
     genExpCopySupportDdlForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen)
   End If
 End Sub
 
 
 Sub genExpCopySupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     If generateExpCopySupport And g_relationships.descriptors(thisRelIndex).isSubjectToExpCopy Then
       genExpCopySupportDdlForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen)
     End If
 End Sub
 
 Private Sub genDeleteTechPropertySupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportSectionDataFix Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' Delete of Technical Property is only supported at 'pool-level'
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
     ' Delete of Technical Property only supported in data pools supporting LRT
     Exit Sub
   End If


   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewNameGenericAspectMqt As String
   qualViewNameGenericAspectMqt = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True)

   Dim qualViewNameGenericAspectNlTextMqt As String
   qualViewNameGenericAspectNlTextMqt = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, True)
 
   Dim qualViewNamePropertyMqt As String
   qualViewNamePropertyMqt = genQualViewNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, False, True, True)
 
   Dim qualViewNamePropertyGenMqt As String
   qualViewNamePropertyGenMqt = genQualViewNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, True, True)
 
   Dim qualTabNamePropertyGenNlText As String
   qualTabNamePropertyGenNlText = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, False, False, True)
 
   Dim qualViewNamePropertyGenNlTextMqt As String
   qualViewNamePropertyGenNlTextMqt = genQualViewNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, True)
 
   Dim qualViewNameCpGroupHasPropertyLrt As String
   qualViewNameCpGroupHasPropertyLrt = genQualViewNameByRelIndex(g_relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualViewNameSpGroupHasPropertyLrt As String
   qualViewNameSpGroupHasPropertyLrt = genQualViewNameByRelIndex(g_relIndexSpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualViewNameAggregationSlotHasNumericPropertyLrt As String
   qualViewNameAggregationSlotHasNumericPropertyLrt = genQualViewNameByRelIndex(g_relIndexAggregationSlotHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualViewNameCategoryHasNumericPropertyLrt As String
   qualViewNameCategoryHasNumericPropertyLrt = genQualViewNameByRelIndex(g_relIndexCategoryHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, True)
 
   Dim qualViewNamePropertyValidForOrganizationLrt As String
   qualViewNamePropertyValidForOrganizationLrt = genQualViewNameByRelIndex(g_relIndexPropertyValidForOrganization, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabExpression As String
   qualTabExpression = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabTerm As String
   qualTabTerm = genQualTabNameByClassIndex(g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualLrtCommitProcName As String
   qualLrtCommitProcName = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   ' ####################################################################################################################
   ' #    SP for Deleteting 'Technical properties (generic aspects that point to technical properties'
   ' ####################################################################################################################

   Dim qualProcNameDeleteTechProperty As String
   qualProcNameDeleteTechProperty = genQualProcName(g_sectionIndexDataFix, spnDeleteTechProperty, ddlType, thisOrgIndex, thisPoolIndex)
   printSectionHeader("SP for 'Deleteting a 'technical' property'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDeleteTechProperty
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "Property-OID")
   If (thisOrgIndex = g_primaryOrgIndex) Then
     genProcParm(fileNo, "IN", "id_in", "INTEGER", True, "PropertyTemplate-ID")
   End If
   genProcParm(fileNo, "IN", "ps_oid_in", g_dbtOid, True, "PS-OID")
   genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records (sum over all involved tables)")
   Print #fileNo, addTab(0); ")"

   Print #fileNo,
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "SPECIFIC DELTECHPROPERTY"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare constants")
   Print #fileNo, addTab(1); "DECLARE c_cdUserId           VARCHAR(15)      CONSTANT     'IVKMDS_tec_10';           -- CD User Id of the mdsUser"
   Print #fileNo, addTab(1); "DECLARE c_trNumber           INTEGER          CONSTANT     2;                    -- logical transaction number"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_lrtOid", "BIGINT", "NULL")
   If (thisOrgIndex = g_primaryOrgIndex) Then
     genVarDecl(fileNo, "v_prtOid", "BIGINT", "NULL")
   End If
   genVarDecl(fileNo, "v_prtName", "VARCHAR(255)", "NULL")
   genVarDecl(fileNo, "v_msg", "VARCHAR(70)", "NULL")
   genVarDecl(fileNo, "v_genChangelog", "INTEGER", "1")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_gwspError", "INTEGER", "0")
   genVarDecl(fileNo, "v_gwspInfo", "INTEGER", "0")
   genVarDecl(fileNo, "v_gwspWarning", "INTEGER", "0")

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
   Print #fileNo,
   genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
   Print #fileNo,
 
   genProcSectionHeader(fileNo, "make sure that DB2-registers are empty")
   Print #fileNo, addTab(1); "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );"
   Print #fileNo,


   If (thisOrgIndex = g_primaryOrgIndex) Then
     Print #fileNo, addTab(1); "IF id_in IS NULL AND oid_in IS NULL THEN"
     Print #fileNo, addTab(2); "SET v_msg = RTRIM(LEFT('[MDS]: PropertyOID or TemplateID is necessary.',70));"
     Print #fileNo, addTab(2); "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(1); "IF id_in IS NOT NULL AND oid_in IS NOT NULL THEN"
     Print #fileNo, addTab(2); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = ' || id_in || ' AND PT.OID = P.PTMHTP_OID WHERE P.AHOID = ' || oid_in || ' AND P.PS_OID = ' || ps_oid_in || ') > 0', 'OID and ID belongs to different Properties.');"
     Print #fileNo, addTab(2); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ') > 0', 'OID doesn''t exists in PS.');"
     Print #fileNo, addTab(2); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY_GEN WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');"
     Print #fileNo, addTab(2); "SET v_prtOid = oid_in;"
     Print #fileNo, addTab(1); "ELSE"
     Print #fileNo, addTab(2); "IF id_in IS NULL THEN"
     Print #fileNo, addTab(3); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ') > 0', 'OID doesn''t exists in PS.');"
     Print #fileNo, addTab(3); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY_GEN WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');"
     Print #fileNo, addTab(3); "SET v_prtOid = oid_in;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = ' || id_in || ' AND PT.OID = P.PTMHTP_OID WHERE P.PS_OID = ' || ps_oid_in || ') > 0', 'Property doesn''t exists in PS.');"
     Print #fileNo, addTab(3); "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST.PROPERTYTEMPLATE WHERE ID = ' || id_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');"
     Print #fileNo, addTab(3); "SET v_prtOid = (SELECT P.OID FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = id_in AND PT.OID = P.PTMHTP_OID WHERE P.PS_OID = ps_oid_in);"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo,
   End If


   If (thisOrgIndex = g_primaryOrgIndex) Then
     Print #fileNo, addTab(1); "SET v_prtName = (SELECT LABEL FROM "; qualTabNamePropertyGenNlText; " WHERE AHOID = v_prtOid AND PS_OID = ps_oid_in AND LANGUAGE_ID = 1 FETCH FIRST ROW ONLY);"
   Else
     Print #fileNo, addTab(1); "SET v_prtName = (SELECT LABEL FROM "; qualTabNamePropertyGenNlText; " WHERE AHOID = oid_in AND PS_OID = ps_oid_in AND LANGUAGE_ID = 1 FETCH FIRST ROW ONLY);"
   End If


   If (thisOrgIndex = g_primaryOrgIndex) Then
     Print #fileNo,
     Print #fileNo, addTab(1); "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );"
     genProcSectionHeader(fileNo, "delete in MPCs")
     Print #fileNo, addTab(1); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "O.ID AS c_orgId,"
     Print #fileNo, addTab(3); "O.ORGOID AS c_orgOid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "VL6CDBM.PDMORGANIZATION_ENUM O"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "O.ID > 1"
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); "O.ID"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "SET v_stmntText = 'CALL VL6CDFX' || CAST(RIGHT('00' || RTRIM(CAST(c_orgId AS CHAR(2))),2) AS CHAR(2)) || '1.DELTECHPROPERTY(' || v_prtOid || ', ' || ps_oid_in || ', ?)';"
     Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntText;"
     Print #fileNo, addTab(1); "END FOR;"
   End If



   Print #fileNo,
   genProcSectionHeader(fileNo, "open LRT")
   Print #fileNo, addTab(2); "CALL "; qualLrtBeginProcName; "(c_cdUserId, c_trNumber, ps_oid_in, 0, v_lrtOid);"
   Print #fileNo,
   Print #fileNo, addTab(2); "CALL SYSPROC.WLM_SET_CLIENT_INFO( c_cdUserId, v_lrtOid, ps_oid_in, NULL, NULL );"
 
   genProcSectionHeader(fileNo, "delete GenericAspects")
   Print #fileNo, addTab(1); "DELETE FROM ";
   Print #fileNo,
   Print #fileNo, addTab(2); qualViewNameGenericAspectMqt; " GA "
   Print #fileNo, addTab(1); "WHERE ";
   Print #fileNo,
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "GA.PRPAPR_OID = v_prtOid ";
   Else
      Print #fileNo, addTab(2); "GA.PRPAPR_OID = oid_in ";
   End If
   Print #fileNo,
   Print #fileNo, addTab(1); "AND ";
   Print #fileNo,
   Print #fileNo, addTab(2); "GA.PS_OID = ps_oid_in ";
   Print #fileNo,
   Print #fileNo, addTab(1); "AND ";
   Print #fileNo,
   Print #fileNo, addTab(2); "GA.ISDELETED = 0 ";
   Print #fileNo,
   Print #fileNo, addTab(1); ";";

   Print #fileNo,
   genProcSectionHeader(fileNo, "delete GenericAspect_Nl-Text")
   Print #fileNo, addTab(1); "DELETE FROM ";
   Print #fileNo,
   Print #fileNo, addTab(2); qualViewNameGenericAspectNlTextMqt; " NL "
   Print #fileNo, addTab(1); "WHERE ";
   Print #fileNo,
   Print #fileNo, addTab(2); "EXISTS (SELECT 1 FROM "; qualViewNameGenericAspectMqt; " GA WHERE GA.AHOID = NL.AHOID AND GA.INLRT = v_lrtOid) ";
   Print #fileNo,
   Print #fileNo, addTab(1); "AND ";
   Print #fileNo,
   Print #fileNo, addTab(2); "NL.PS_OID = ps_oid_in ";
   Print #fileNo,
   Print #fileNo, addTab(1); "AND ";
   Print #fileNo,
   Print #fileNo, addTab(2); "NL.ISDELETED = 0 ";
   Print #fileNo,
   Print #fileNo, addTab(1); ";";
   Print #fileNo,

   genProcSectionHeader(fileNo, "delete Property", 1, True)
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualViewNamePropertyMqt; " P"
   Print #fileNo, addTab(1); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "P.AHOID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "P.AHOID = oid_in "
   End If
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "P.PS_OID = ps_oid_in"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "P.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualViewNamePropertyGenMqt, " PG"
   Print #fileNo, addTab(1); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "PG.AHOID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "PG.AHOID = oid_in "
   End If
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "PG.PS_OID = ps_oid_in"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "PG.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualViewNamePropertyGenNlTextMqt; " PNL"
   Print #fileNo, addTab(1); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "PNL.AHOID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "PNL.AHOID = oid_in "
   End If
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "PNL.PS_OID = ps_oid_in"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "PNL.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(3); qualViewNameCpGroupHasPropertyLrt; " CPG"
   Print #fileNo, addTab(2); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "CPG.PRP_OID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "CPG.PRP_OID = oid_in "
   End If
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CPG.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(4); "CPG.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualViewNameSpGroupHasPropertyLrt; " SPG"
   Print #fileNo, addTab(2); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "SPG.PRP_OID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "SPG.PRP_OID = oid_in "
   End If
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "SPG.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(4); "SPG.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualViewNameAggregationSlotHasNumericPropertyLrt; " AHP"
   Print #fileNo, addTab(2); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "AHP.NPR_OID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "AHP.NPR_OID = oid_in "
   End If
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AHP.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(4); "AHP.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualViewNameCategoryHasNumericPropertyLrt; " CHP"
   Print #fileNo, addTab(2); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "CHP.NPR_OID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "CHP.NPR_OID = oid_in "
   End If
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CHP.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(4); "CHP.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualViewNamePropertyValidForOrganizationLrt; " PVO"
   Print #fileNo, addTab(2); "WHERE"
   If (thisOrgIndex = g_primaryOrgIndex) Then
      Print #fileNo, addTab(2); "PVO.PRP_OID = v_prtOid "
   Else
      Print #fileNo, addTab(2); "PVO.PRP_OID = oid_in "
   End If
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PVO.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(4); "PVO.ISDELETED = 0"
   Print #fileNo, addTab(1); ";"


   Print #fileNo,
   genProcSectionHeader(fileNo, "set LRT comment")
   Print #fileNo, addTab(1); "INSERT INTO "
   Print #fileNo, addTab(2); qualTabNameLrt; "_NL_TEXT "
   Print #fileNo, addTab(1); "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) "
   Print #fileNo, addTab(1); "VALUES ("
   Print #fileNo, addTab(2); "NEXTVAL FOR "; genQualOidSeqNameForOrg(thisOrgIndex, ddlType); ", v_lrtOid, 1,  'MDS Service Skript: Löschen der technischen Eigenschaft ' || COALESCE(v_prtName, '-') || '. PsOid: '  || RTRIM( ps_oid_in ), ps_oid_in"
   Print #fileNo, addTab(1); ");"

   Print #fileNo,
   genProcSectionHeader(fileNo, "commit LRT")
   Print #fileNo, addTab(1); "CALL "; qualLrtCommitProcName; "(v_lrtOid, 0, v_genChangelog, v_rowCount, v_gwspError, v_gwspInfo, v_gwspWarning );"
   Print #fileNo, addTab(1); "SET recordCount_out = v_rowCount;"
   Print #fileNo,

   genProcSectionHeader(fileNo, "mark invalid Expressions", 1, True)
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabExpression; " EX"
   Print #fileNo, addTab(1); "SET EX.ISINVALID = 1"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(4); "EX.PS_OID = ps_oid_in"
   Print #fileNo, addTab(6); "AND"
   If (thisOrgIndex = g_primaryOrgIndex) Then
     Print #fileNo, addTab(4); "EXISTS(SELECT 1 FROM "; qualTabTerm; " T WHERE T.PCRPRP_OID = v_prtOid AND EX.OID = T.AHOID AND EX.PS_OID = T.PS_OID)"
   Else
     Print #fileNo, addTab(4); "EXISTS(SELECT 1 FROM "; qualTabTerm; " T WHERE T.PCRPRP_OID = oid_in AND EX.OID = T.AHOID AND EX.PS_OID = T.PS_OID)"
   End If
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
   Print #fileNo,


 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ENDIF IVK ###
 
 
 
 
