 Attribute VB_Name = "M27_Meta"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 3
 
 Global Const tempTabNameGenWorkSpaceResult = "SESSION.GenWorkSpaceResult"
 
 Sub genAcmMetaSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim i As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtLdm Then
     genAcmMetaSupportDdlForCode(edtLdm)
     genAcmMetaSupportDdlForCodeByPool(, , edtLdm)
     genAcmMetaSupportDdlForMetaByPool(, , edtLdm)
   ElseIf ddlType = edtPdm Then
     genAcmMetaSupportDdlForCode(edtPdm)
     genAcmMetaSupportDdlForGenWorkspace(edtPdm)
     genAcmMetaSupportDdlForCtsConfig(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
         If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             If g_pools.descriptors(thisPoolIndex).isActive And g_pools.descriptors(thisPoolIndex).supportAcm And Not g_pools.descriptors(thisPoolIndex).isArchive Then
               genAcmMetaSupportDdlForCodeByPool(thisOrgIndex, thisPoolIndex, edtPdm)
               genAcmMetaSupportDdlForMetaByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             End If
             If g_pools.descriptors(thisPoolIndex).isActive And g_pools.descriptors(thisPoolIndex).supportAcm Then
               genAcmMetaSupportDdlForGenWorkspaceByPool(thisOrgIndex, thisPoolIndex, edtPdm)
             End If
         End If
       Next thisOrgIndex
     Next thisPoolIndex

     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       genAcmMetaSupportDdlForGenWorkspaceByOrg(thisOrgIndex, edtPdm)
     Next thisOrgIndex
   End If
 End Sub
 
 Private Sub genAcmMetaSupportDdlForCodeByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseCoreSupport, ldmIterationPoolSpecific)
 
   ' ####################################################################################################################
   ' #    currently no objects to create
   ' ####################################################################################################################

 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Private Sub genAcmMetaSupportDdlForMetaByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseCoreSupport, ldmIterationPoolSpecific)
 
   Dim qualProcNameGetGroupElementsGlobal As String
   Dim qualProcNameGetGroupElementsLocal As String
   qualProcNameGetGroupElementsGlobal = genQualProcName(g_sectionIndexMeta, spnGetGroupElements, ddlType)
   qualProcNameGetGroupElementsLocal = genQualProcName(g_sectionIndexAliasLrt, spnGetGroupElements, ddlType, thisOrgIndex, thisPoolIndex)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetGroupElementsLocal
   Print #fileNo, addTab(0); "( "
   Print #fileNo, addTab(1); "IN languageId_in           INTEGER,"
   Print #fileNo, addTab(1); "IN fallbackLanguageId_in   INTEGER,"
   Print #fileNo, addTab(1); "IN classId_in              VARCHAR(5),"
   Print #fileNo, addTab(1); "IN groupElementOid_in      BIGINT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   Print #fileNo, addTab(1); "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;"
   Print #fileNo, addTab(1); "DECLARE v_dstmntTxt       VARCHAR(500)     DEFAULT NULL;"
   Print #fileNo, addTab(1); "DECLARE v_restmntTxt      VARCHAR(500)     DEFAULT NULL;"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "-- declare statement"
   Print #fileNo, addTab(1); "DECLARE v_stmnt                   STATEMENT;"
   Print #fileNo, addTab(1); "DECLARE v_restmnt                 STATEMENT;"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "-- declare cursor"
   Print #fileNo, addTab(1); "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;"
   Print #fileNo, addTab(1); ""
   Print #fileNo, addTab(1); "-- temporary table for GroupElements from VL6CMET.GETGROUPELEMENTS"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.GroupElements"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid         BIGINT,"
   Print #fileNo, addTab(2); "classid     VARCHAR(5),"
   Print #fileNo, addTab(2); "divOid      BIGINT,"
   Print #fileNo, addTab(2); "psOid       BIGINT,"
   Print #fileNo, addTab(2); "orgOid      BIGINT,"
   Print #fileNo, addTab(2); "accModeId   INTEGER,"
   Print #fileNo, addTab(2); "entity      VARCHAR(250)"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "WITH REPLACE;"
   Print #fileNo, addTab(1); ""
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.GroupElementsDistinct"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classid     VARCHAR(5),"
   Print #fileNo, addTab(2); "divOid      BIGINT,"
   Print #fileNo, addTab(2); "psOid       BIGINT,"
   Print #fileNo, addTab(2); "orgOid      BIGINT,"
   Print #fileNo, addTab(2); "accModeId   INTEGER,"
   Print #fileNo, addTab(2); "entity      VARCHAR(250)"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "WITH REPLACE;"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "-- call 'global procedure'"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameGetGroupElementsGlobal; "(?,?,?,?)';"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "languageId_in,"
   Print #fileNo, addTab(2); "fallbackLanguageId_in,"
   Print #fileNo, addTab(2); "classId_in,"
   Print #fileNo, addTab(2); "groupElementOid_in"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); ""
   Print #fileNo, addTab(1); "SET v_dstmntTxt = 'INSERT INTO SESSION.GroupElementsDistinct (classid, divOid, psOid, orgOid, accModeId, entity) SELECT DISTINCT classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElements';"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_dstmntTxt;"
   Print #fileNo, addTab(1); ""
   Print #fileNo, addTab(1); "SET v_restmntTxt = 'SELECT classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElementsDistinct';"
   Print #fileNo, addTab(1); "PREPARE v_restmnt FROM v_restmntTxt;"
   Print #fileNo, addTab(1); "OPEN c_return;"
   Print #fileNo, addTab(0); ""
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
 
 Private Sub genAcmMetaSupportDdlForCode( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexCode, processingStep, ddlType, , , , phaseCoreSupport)

   ' ####################################################################################################################
   ' #    Function for decomposing Sr0Context into CodeNumbers
   ' ####################################################################################################################

   Dim qualFuncNameParseSr0Context As String
   qualFuncNameParseSr0Context = genQualFuncName(g_sectionIndexMeta, udfnParseSr0Context, ddlType, , , , , , True)

   printSectionHeader("Function for decomposing Sr0Context into CodeNumbers", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameParseSr0Context
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "sr0Context_in", "VARCHAR(50)", False, "string-encode list of CodeNumbers delimited by '+'")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "codeNumber01 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber02 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber03 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber04 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber05 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber06 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber07 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber08 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber09 "; g_dbtCodeNumber; ","
   Print #fileNo, addTab(2); "codeNumber10 "; g_dbtCodeNumber
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "CONTAINS SQL"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_sr0Context", "VARCHAR(50)", "NULL")
   genVarDecl(fileNo, "v_pos", "INTEGER", "1")
   genVarDecl(fileNo, "v_start", "INTEGER", "1")
   genVarDecl(fileNo, "v_codeIndex", "INTEGER", "1")
   genVarDecl(fileNo, "v_codeNumber", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber01", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber02", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber03", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber04", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber05", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber06", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber07", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber08", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber09", g_dbtCodeNumber, "NULL")
   genVarDecl(fileNo, "v_codeNumber10", g_dbtCodeNumber, "NULL")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_sr0Context = REPLACE(REPLACE(RTRIM(LTRIM(sr0Context_in)), '-', '+'), ' ', '');"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (v_codeIndex <= 10) AND (v_pos > 0) DO"
   Print #fileNo, addTab(2); "SET v_pos = LOCATE('+', v_sr0Context, v_start);"
   Print #fileNo, addTab(2); "IF v_pos = 0 THEN"
   Print #fileNo, addTab(3); "SET v_codeNumber = SUBSTR(v_sr0Context, v_start);"
   Print #fileNo, addTab(2); "ELSEIF v_pos > 0 THEN"
   Print #fileNo, addTab(3); "SET v_codeNumber = SUBSTR(v_sr0Context, v_start, v_pos - v_start);"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF     v_codeIndex =  1 THEN SET v_codeNumber01 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  2 THEN SET v_codeNumber02 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  3 THEN SET v_codeNumber03 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  4 THEN SET v_codeNumber04 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  5 THEN SET v_codeNumber05 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  6 THEN SET v_codeNumber06 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  7 THEN SET v_codeNumber07 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  8 THEN SET v_codeNumber08 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex =  9 THEN SET v_codeNumber09 = v_codeNumber;"
   Print #fileNo, addTab(2); "ELSEIF v_codeIndex = 10 THEN SET v_codeNumber10 = v_codeNumber;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_start = v_pos + 1;"
   Print #fileNo, addTab(2); "SET v_codeIndex = v_codeIndex + 1;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "v_codeNumber01,"
   Print #fileNo, addTab(3); "v_codeNumber02,"
   Print #fileNo, addTab(3); "v_codeNumber03,"
   Print #fileNo, addTab(3); "v_codeNumber04,"
   Print #fileNo, addTab(3); "v_codeNumber05,"
   Print #fileNo, addTab(3); "v_codeNumber06,"
   Print #fileNo, addTab(3); "v_codeNumber07,"
   Print #fileNo, addTab(3); "v_codeNumber08,"
   Print #fileNo, addTab(3); "v_codeNumber09,"
   Print #fileNo, addTab(3); "v_codeNumber10"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
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
 
 
 
 
 
 Private Sub genAcmMetaSupportDdlForGenWorkspace( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexMeta, processingStep, ddlType, , , , phaseUseCases)
 
   ' ####################################################################################################################
   ' #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
   ' ####################################################################################################################

   Dim qualProcedureNameGlobal As String
   Dim qualProcedureNameLocal As String
 
   qualProcedureNameGlobal = genQualProcName(g_sectionIndexMeta, spnGenWorkspace, ddlType)
 
   Dim qualTabNameTempOrgOids As String
   qualTabNameTempOrgOids = "SESSION.OrgOids"
   Dim qualTabNameTempPsOids As String
   qualTabNameTempPsOids = "SESSION.PsOids"
   Dim qualTabNameTempAccessModeIds As String
   qualTabNameTempAccessModeIds = "SESSION.AccessModeIds"
 
   Dim useListParams As Boolean
   Dim withSqlError As Boolean
   Dim i As Integer
   For i = 1 To 3
     useListParams = (i = 1)
     withSqlError = (i = 2)

     qualProcedureNameLocal = _
       genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType, , , , IIf(useListParams, "S", IIf(withSqlError, "_WITHERROR", "")), False)

     printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameLocal
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
     If useListParams Then
       genProcParm(fileNo, "IN", "orgIdList_in", "VARCHAR(200)", True, "(optional) ','-delimited list of IDs of Organizations")
       genProcParm(fileNo, "IN", "accessModeIdList_in", "VARCHAR(50)", True, "(optional) ','-delimited list of AccessModes")
       genProcParm(fileNo, "IN", "psOidList_in", "VARCHAR(400)", True, "(optional) ','-delimited list of OIDs of ProductStructures")
     Else
       genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)")
       genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) identifies the 'rule scope' of the work space")
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")
     End If
     genProcParm(fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'")
     genProcParm(fileNo, "IN", "useRel2ProdLock_in", g_dbtBoolean, True, "lock data pools first if (and only if) set to '1'")
     genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", False, "number of calls to GEN_WORKSPACE submitted")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_error", "VARCHAR(256)", "NULL")
     genVarDecl(fileNo, "v_errorInfo", "VARCHAR(1024)", "NULL")
     genVarDecl(fileNo, "v_warning", "VARCHAR(512)", "NULL")
     genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL")
     genSigMsgVarDecl(fileNo)
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")

     genProcSectionHeader(fileNo, "temporary table for procedure results")

     Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(2); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "seqNo        INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),"
     Print #fileNo, addTab(2); "orgId        "; g_dbtEnumId; ","
     Print #fileNo, addTab(2); "accessModeId "; g_dbtEnumId; ","
     Print #fileNo, addTab(2); "psOid        "; g_dbtOid; ","
     Print #fileNo, addTab(2); "stateMent    VARCHAR(100),"
     Print #fileNo, addTab(2); "error        VARCHAR(256),"
     Print #fileNo, addTab(2); "info         VARCHAR(1024),"
     Print #fileNo, addTab(2); "warning      VARCHAR(512)"
     Print #fileNo, addTab(1); ")"

     genDdlForTempTableDeclTrailer(fileNo, 1, True, True, True)

     genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs")
     Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempOrgOids; "( id "; g_dbtEnumId; ", oid "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
     Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempPsOids; "( oid "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
     Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempAccessModeIds; "( id "; g_dbtEnumId; " ) NOT LOGGED WITH REPLACE;"

     If useListParams Then
       genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     Else
       genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     End If

     genProcSectionHeader(fileNo, "initialize output parameter")
     Print #fileNo, addTab(1); "SET callCount_out = 0;"

     If useListParams Then
       genProcSectionHeader(fileNo, "determine referred ORG-OIDs")
       Print #fileNo, addTab(1); "IF orgIdList_in IS NULL THEN"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid ) SELECT O.ID, O.ORGOID FROM "; g_qualTabNamePdmOrganization; " O;"
       Print #fileNo, addTab(1); "ELSE"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid )"
       Print #fileNo, addTab(3); "SELECT O.ID, O.ORGOID FROM TABLE ( "; g_qualFuncNameStrElems; "(orgIdList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "INNER JOIN "; g_qualTabNamePdmOrganization; " O ON O.ID = "; g_dbtEnumId; "(X.elem);"
       Print #fileNo, addTab(1); "END IF;"

       genProcSectionHeader(fileNo, "determine referred PS-OIDs")
       Print #fileNo, addTab(1); "IF psOidList_in IS NULL THEN"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempPsOids; "( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P;"
       Print #fileNo, addTab(1); "ELSE"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempPsOids; "( oid )"
       Print #fileNo, addTab(3); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "INNER JOIN "; g_qualTabNameProductStructure; " P ON P."; g_anOid; " = "; g_dbtOid; "(X.elem);"
       Print #fileNo, addTab(1); "END IF;"

       genProcSectionHeader(fileNo, "determine referred AccessMode-IDs")
       Print #fileNo, addTab(1); "IF accessModeIdList_in IS NULL THEN"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S;"
       Print #fileNo, addTab(1); "ELSE"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id )"
       Print #fileNo, addTab(3); "SELECT "; g_dbtEnumId; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "INNER JOIN "; g_qualTabNameDataPoolAccessMode; " S ON S.ID = "; g_dbtEnumId; "(X.elem);"
       Print #fileNo, addTab(1); "END IF;"
     Else
       genProcSectionHeader(fileNo, "initialize referred IDs / OIDs")
       Print #fileNo, addTab(1); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid ) SELECT O.ID, O.ORGOID FROM "; g_qualTabNamePdmOrganization; " O WHERE COALESCE(orgId_in, O.ID) = O.ID;"
       Print #fileNo, addTab(1); "IF accessModeId_in IS NULL THEN"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID AND S.ID < 4;"
       Print #fileNo, addTab(1); "ELSE"
       Print #fileNo, addTab(2); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID;"
       Print #fileNo, addTab(1); "END IF;"
       Print #fileNo, addTab(1); "INSERT INTO "; qualTabNameTempPsOids; "( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P WHERE COALESCE(psOid_in, P."; g_anOid; ") = P."; g_anOid; ";"
     End If
 
     genProcSectionHeader(fileNo, "ignore AccessMode """ & CStr(g_migDataPoolId) & """")
     Print #fileNo, addTab(1); "DELETE FROM "; qualTabNameTempAccessModeIds; " WHERE id = "; CStr(g_migDataPoolId); ";"
 
     Dim qualProcNameSetLock As String
     qualProcNameSetLock = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "GENWS")
     Dim qualProcNameResetLock As String
     qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "GENWS")

     genProcSectionHeader(fileNo, "if required lock all matching data pools")
     Print #fileNo, addTab(1); "IF useRel2ProdLock_in = 1 THEN"

     Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
     genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", 3, True)
     Print #fileNo, addTab(3); "SAVEPOINT rel2ProdLock UNIQUE ON ROLLBACK RETAIN CURSORS;"
     Print #fileNo, addTab(2); "END IF;"

     genProcSectionHeader(fileNo, "loop over all matching data pools", 2)
     Print #fileNo, addTab(2); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "OR.id           AS c_orgId,"
     Print #fileNo, addTab(4); "OR.oid          AS c_orgOid,"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; "  AS c_accessModeId,"
     Print #fileNo, addTab(4); "PS.oid          AS c_psOid"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); g_qualTabNamePdmPrimarySchema; " SC"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempOrgOids; " OR"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "OR.id = SC."; g_anOrganizationId
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempPsOids; " PS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "1 = 1"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempAccessModeIds; " AM"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; " = AM.ID"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "OR.id,"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; ","
     Print #fileNo, addTab(4); "PS.oid"
     Print #fileNo, addTab(2); "DO"

     genProcSectionHeader(fileNo, "lock data pool", 3, True)
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; qualProcNameSetLock; "(''' ||"
     Print #fileNo, addTab(13); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
     Print #fileNo, addTab(13); "'''<admin>'',' ||"
     Print #fileNo, addTab(13); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
     Print #fileNo, addTab(13); "'''<cmd>'',' ||"
     Print #fileNo, addTab(13); "'?)';"

     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(4); "EXECUTE"
     Print #fileNo, addTab(5); "v_stmnt"
     Print #fileNo, addTab(4); "INTO"
     Print #fileNo, addTab(5); "v_numDataPools"
     Print #fileNo, addTab(4); ";"

     genProcSectionHeader(fileNo, "if lock on data pool could not be aquired rollback and exit", 4)
     Print #fileNo, addTab(4); "IF v_numDataPools = 0 THEN"
     Print #fileNo, addTab(5); "ROLLBACK TO SAVEPOINT rel2ProdLock;"

     Print #fileNo, addTab(5); "RELEASE SAVEPOINT rel2ProdLock;"

     If useListParams Then
       genSpLogProcEscape(fileNo, qualProcedureNameLocal, ddlType, 5, "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     Else
       genSpLogProcEscape(fileNo, qualProcedureNameLocal, ddlType, 5, "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     End If

     genSignalDdlWithParms("setRel2ProdLockFail", fileNo, 5, "GENWS", , , , , , , , , "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))")
     Print #fileNo, addTab(4); "END IF;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader(fileNo, "store statement in temporary table", 4, True)
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "statement"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "c_orgId,"
     Print #fileNo, addTab(5); "c_accessModeId,"
     Print #fileNo, addTab(5); "c_psOid,"
     Print #fileNo, addTab(5); "v_stmntTxt"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     Print #fileNo,
     Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(3); "IF autoCommit_in = 1 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     If useListParams Then
       genProcSectionHeader(fileNo, "re-determine referred ORG-OIDs", 4)
       Print #fileNo, addTab(4); "IF orgIdList_in IS NULL THEN"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid ) SELECT O.ID, O.ORGOID FROM "; g_qualTabNamePdmOrganization; " O;"
       Print #fileNo, addTab(4); "ELSE"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid )"
       Print #fileNo, addTab(6); "SELECT O.ID, O.ORGOID FROM TABLE ( "; g_qualFuncNameStrElems; "(orgIdList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(6); "INNER JOIN "; g_qualTabNamePdmOrganization; " O ON O.ID = "; g_dbtEnumId; "(X.elem);"
       Print #fileNo, addTab(4); "END IF;"

       genProcSectionHeader(fileNo, "re-determine referred PS-OIDs", 4)
       Print #fileNo, addTab(4); "IF psOidList_in IS NULL THEN"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempPsOids; "( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P;"
       Print #fileNo, addTab(4); "ELSE"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempPsOids; "( oid )"
       Print #fileNo, addTab(6); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(6); "INNER JOIN "; g_qualTabNameProductStructure; " P ON P."; g_anOid; " = "; g_dbtOid; "(X.elem);"
       Print #fileNo, addTab(4); "END IF;"

       genProcSectionHeader(fileNo, "re-determine referred AccessMode-IDs", 4)
       Print #fileNo, addTab(4); "IF accessModeIdList_in IS NULL THEN"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S;"
       Print #fileNo, addTab(4); "ELSE"
       Print #fileNo, addTab(5); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id )"
       Print #fileNo, addTab(6); "SELECT "; g_dbtEnumId; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(6); "INNER JOIN "; g_qualTabNameDataPoolAccessMode; " S ON S.ID = "; g_dbtEnumId; "(X.elem);"
       Print #fileNo, addTab(4); "END IF;"
     Else
       genProcSectionHeader(fileNo, "re-initialize referred IDs / OIDs", 4)
       Print #fileNo, addTab(4); "INSERT INTO "; qualTabNameTempOrgOids; "( id, oid ) SELECT O.ID, O.ORGOID FROM "; g_qualTabNamePdmOrganization; " O WHERE COALESCE(orgId_in, O.ID) = O.ID;"
       Print #fileNo, addTab(4); "INSERT INTO "; qualTabNameTempAccessModeIds; "( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID;"
       Print #fileNo, addTab(4); "INSERT INTO "; qualTabNameTempPsOids; "( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P WHERE COALESCE(psOid_in, P."; g_anOid; ") = P."; g_anOid; ";"
     End If
     genProcSectionHeader(fileNo, "ignore AccessMode """ & CStr(g_migDataPoolId) & """", 4)
     Print #fileNo, addTab(4); "DELETE FROM "; qualTabNameTempAccessModeIds; " WHERE id = "; CStr(g_migDataPoolId); ";"
     Print #fileNo, addTab(3); "ELSE"
     Print #fileNo, addTab(4); "RELEASE SAVEPOINT rel2ProdLock;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "loop over all matching data pools")
     Print #fileNo, addTab(1); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "SC."; g_anOrganizationId; " AS c_orgId,"
     Print #fileNo, addTab(3); "OR.oid AS c_orgOid,"
     Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; " AS c_accessModeId,"
     Print #fileNo, addTab(3); "SC.NATIVESCHEMANAME1 AS c_schemaName1,"
     Print #fileNo, addTab(3); "SC.NATIVESCHEMANAME2 AS c_schemaName2,"
     Print #fileNo, addTab(3); "PS.oid AS c_psOid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "SC1."; g_anOrganizationId; ","
     Print #fileNo, addTab(5); "SC1."; g_anPoolTypeId; ","
     Print #fileNo, addTab(5); "SC1."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME1,"
     Print #fileNo, addTab(5); "SC1."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME2"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); g_qualTabNamePdmPrimarySchema; " SC1"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "SC1."; g_anOrganizationId; ","
     Print #fileNo, addTab(5); CStr(g_sim1DataPoolId); " AS "; g_anPoolTypeId; ","
     Print #fileNo, addTab(5); "SC1."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME1,"
     Print #fileNo, addTab(5); "SC2."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME2"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); g_qualTabNamePdmPrimarySchema; " SC1"
     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); g_qualTabNamePdmPrimarySchema; " SC2"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "SC1."; g_anOrganizationId; " = SC2."; g_anOrganizationId
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "SC1."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "SC2."; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType)
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "SC1."; g_anOrganizationId; ","
     Print #fileNo, addTab(5); CStr(g_sim2DataPoolId); " AS "; g_anPoolTypeId; ","
     Print #fileNo, addTab(5); "SC1."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME1,"
     Print #fileNo, addTab(5); "SC2."; g_anPdmNativeSchemaName; " AS NATIVESCHEMANAME2"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); g_qualTabNamePdmPrimarySchema; " SC1"
     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); g_qualTabNamePdmPrimarySchema; " SC2"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "SC1."; g_anOrganizationId; " = SC2."; g_anOrganizationId
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "SC1."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "SC2."; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType)
     Print #fileNo, addTab(3); ") SC"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); qualTabNameTempOrgOids; " OR"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "OR.id = SC."; g_anOrganizationId
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); qualTabNameTempPsOids; " PS"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "1 = 1"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); qualTabNameTempAccessModeIds; " AM"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; " = AM.ID"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "OR.id,"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; ","
     Print #fileNo, addTab(4); "PS.oid"
     Print #fileNo, addTab(1); "DO"

     genProcSectionHeader(fileNo, "initialize procedure parameters", 2, True)
     Print #fileNo, addTab(2); "SET v_error     = NULL;"
     Print #fileNo, addTab(2); "SET v_errorInfo = NULL;"
     Print #fileNo, addTab(2); "SET v_warning   = NULL;"

     genProcSectionHeader(fileNo, "call 'global procedure'", 2)
     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcedureNameGlobal; "(' ||"
     Print #fileNo, addTab(12); "'''' || c_schemaName1 || ''',' ||"
     Print #fileNo, addTab(12); "'''' || c_schemaName2 || ''',' ||"
     Print #fileNo, addTab(12); "RTRIM(CHAR(c_orgOid)) || ',' ||"
     Print #fileNo, addTab(12); "RTRIM(CHAR(c_psOid)) || ',' ||"
     Print #fileNo, addTab(12); "RTRIM(CHAR(c_accessModeId)) || ',' ||"
     Print #fileNo, addTab(12); "'?,?,?)';"

     Print #fileNo,
     Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE"
     Print #fileNo, addTab(4); "v_stmnt"
     Print #fileNo, addTab(3); "INTO"
     Print #fileNo, addTab(4); "v_error,"
     Print #fileNo, addTab(4); "v_errorInfo,"
     Print #fileNo, addTab(4); "v_warning"
     Print #fileNo, addTab(3); ";"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF autoCommit_in = 1 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"

     genProcSectionHeader(fileNo, "keep track of error messages", 3)
     Print #fileNo, addTab(3); "INSERT INTO"
     Print #fileNo, addTab(4); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "orgId,"
     Print #fileNo, addTab(4); "accessModeId,"
     Print #fileNo, addTab(4); "psOid,"
     Print #fileNo, addTab(4); "statement,"
     Print #fileNo, addTab(4); "error,"
     Print #fileNo, addTab(4); "info,"
     Print #fileNo, addTab(4); "warning"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(3); "VALUES"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "c_orgId,"
     Print #fileNo, addTab(4); "c_accessModeId,"
     Print #fileNo, addTab(4); "c_psOid,"
     Print #fileNo, addTab(4); "v_stmntTxt,"
     Print #fileNo, addTab(4); "v_error,"
     Print #fileNo, addTab(4); "v_errorInfo,"
     Print #fileNo, addTab(4); "v_warning"
     Print #fileNo, addTab(3); ");"

     Print #fileNo, addTab(2); "ELSE"
     genProcSectionHeader(fileNo, "store statement in temporary table", 3, True)
     Print #fileNo, addTab(3); "INSERT INTO"
     Print #fileNo, addTab(4); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "orgId,"
     Print #fileNo, addTab(4); "accessModeId,"
     Print #fileNo, addTab(4); "psOid,"
     Print #fileNo, addTab(4); "statement"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(3); "VALUES"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "c_orgId,"
     Print #fileNo, addTab(4); "c_accessModeId,"
     Print #fileNo, addTab(4); "c_psOid,"
     Print #fileNo, addTab(4); "v_stmntTxt"
     Print #fileNo, addTab(3); ");"
     Print #fileNo, addTab(2); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(2); "SET callCount_out = callCount_out + 1;"

     Print #fileNo, addTab(1); "END FOR;"

     genProcSectionHeader(fileNo, "if required unlock all matching data pools")
     Print #fileNo, addTab(1); "IF useRel2ProdLock_in = 1 THEN"

     genProcSectionHeader(fileNo, "loop over all matching data pools", 2, True)
     Print #fileNo, addTab(2); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "OR.id           AS c_orgId,"
     Print #fileNo, addTab(4); "OR.oid          AS c_orgOid,"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; "  AS c_accessModeId,"
     Print #fileNo, addTab(4); "PS.oid          AS c_psOid"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); g_qualTabNamePdmPrimarySchema; " SC"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempOrgOids; " OR"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "OR.id = SC."; g_anOrganizationId
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempPsOids; " PS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "1 = 1"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); qualTabNameTempAccessModeIds; " AM"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; " = AM.ID"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "OR.id,"
     Print #fileNo, addTab(4); "SC."; g_anPoolTypeId; ","
     Print #fileNo, addTab(4); "PS.oid"
     Print #fileNo, addTab(2); "DO"

     genProcSectionHeader(fileNo, "unlock data pool", 3, True)
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "(''' ||"
     Print #fileNo, addTab(13); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
     Print #fileNo, addTab(13); "'''<admin>'',' ||"
     Print #fileNo, addTab(13); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
     Print #fileNo, addTab(13); "'''<cmd>'',' ||"
     Print #fileNo, addTab(13); "'?)';"

     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(4); "EXECUTE"
     Print #fileNo, addTab(5); "v_stmnt"
     Print #fileNo, addTab(4); "INTO"
     Print #fileNo, addTab(5); "v_numDataPools"
     Print #fileNo, addTab(4); ";"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader(fileNo, "store statement in temporary table", 4, True)
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "statement"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "c_orgId,"
     Print #fileNo, addTab(5); "c_accessModeId,"
     Print #fileNo, addTab(5); "c_psOid,"
     Print #fileNo, addTab(5); "v_stmntTxt"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"
     Print #fileNo, addTab(2); "END FOR;"

     Print #fileNo,
     Print #fileNo, addTab(2); "IF autoCommit_in = 1 THEN"
     Print #fileNo, addTab(3); "COMMIT;"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "return results to application")
     Print #fileNo, addTab(1); "IF mode_in = 1 THEN"
     Print #fileNo, addTab(2); "BEGIN"
     Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "statement,"
     Print #fileNo, addTab(5); "error,"
     Print #fileNo, addTab(5); "info,"
     Print #fileNo, addTab(5); "warning"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(4); "ORDER BY"
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "seqNo"
     Print #fileNo, addTab(3); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 3)
     Print #fileNo, addTab(3); "OPEN resCursor;"
     Print #fileNo, addTab(2); "END;"

     Print #fileNo, addTab(1); "ELSEIF mode_in = 0 THEN"

     Print #fileNo, addTab(2); "BEGIN"
     Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "statement"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); tempTabNameGenWorkSpaceResult
     Print #fileNo, addTab(4); "ORDER BY"
     Print #fileNo, addTab(5); "orgId,"
     Print #fileNo, addTab(5); "accessModeId,"
     Print #fileNo, addTab(5); "psOid,"
     Print #fileNo, addTab(5); "seqNo"
     Print #fileNo, addTab(3); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 3)
     Print #fileNo, addTab(3); "OPEN resCursor;"
     Print #fileNo, addTab(2); "END;"
     Print #fileNo, addTab(1); "END IF;"

     If withSqlError Then
       Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
       Print #fileNo, addTab(2); "IF EXISTS ( SELECT 1 FROM "; tempTabNameGenWorkSpaceResult; " WHERE error IS NOT NULL ) THEN"
       genSignalDdlWithParms("GenWsWithError", fileNo, 3, "GENWS", , , , , , , , , "COALESCE( ( SELECT COALESCE( MIN( info ), MIN( error ) ) FROM SESSION.GenWorkSpaceResult WHERE error IS NOT NULL ), 'GenWs Error' )")
       Print #fileNo, addTab(2); "END IF;"
       Print #fileNo, addTab(1); "END IF;"
     End If

     If useListParams Then
       genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     Else
       genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out")
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i

   ' ####################################################################################################################

   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) identifies the 'rule scope' of the work space")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")
   genProcParm(fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'")
   genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", False, "number of calls to GEN_WORKSPACE submitted")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out")

   genProcSectionHeader(fileNo, "call 'global procedure'", 1, True)
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameLocal; "(mode_in, orgId_in, accessModeId_in, psOid_in, autoCommit_in, 0, callCount_out);"

   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
   ' ####################################################################################################################

   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) identifies the 'rule scope' of the work space")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")
   genProcParm(fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'")
   genProcParm(fileNo, "IN", "useRel2ProdLock_in", g_dbtBoolean, True, "lock data pools first if (and only if) set to '1'")
   genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", True, "number of calls to GEN_WORKSPACE submitted")
   genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
   genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
   genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare conditions")
   genCondDecl(fileNo, "notFound", "02000")

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE resCursor CURSOR WITH HOLD FOR v_stmnt;"
 
   genProcSectionHeader(fileNo, "declare continue handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET callCount_out   = 0;"
   Print #fileNo, addTab(1); "SET gwspError_out   = NULL;"
   Print #fileNo, addTab(1); "SET gwspInfo_out    = NULL;"
   Print #fileNo, addTab(1); "SET gwspWarning_out = NULL;"

   genProcSectionHeader(fileNo, "call 'global procedure'", 1)
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameLocal; "(?,?,?,?,?,?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "callCount_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "mode_in,"
   Print #fileNo, addTab(2); "orgId_in,"
   Print #fileNo, addTab(2); "accessModeId_in,"
   Print #fileNo, addTab(2); "psOid_in,"
   Print #fileNo, addTab(2); "autoCommit_in,"
   Print #fileNo, addTab(2); "useRel2ProdLock_in"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "check for errors", 1)
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'SELECT error, info, warning FROM "; tempTabNameGenWorkSpaceResult; _
                             " WHERE error IS NOT NULL ORDER BY orgId, psOid, accessModeId FETCH FIRST 1 ROWS ONLY';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(1); "OPEN resCursor;"
   Print #fileNo,
   Print #fileNo, addTab(1); "FETCH"
   Print #fileNo, addTab(2); "resCursor"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "gwspError_out,"
   Print #fileNo, addTab(2); "gwspInfo_out,"
   Print #fileNo, addTab(2); "gwspWarning_out"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE resCursor;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF gwspWarning_out IS NULL THEN"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT warning FROM "; tempTabNameGenWorkSpaceResult; _
                             " WHERE warning IS NOT NULL ORDER BY orgId, psOid, accessModeId FETCH FIRST 1 ROWS ONLY';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN resCursor;"
   Print #fileNo,
   Print #fileNo, addTab(2); "FETCH"
   Print #fileNo, addTab(3); "resCursor"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "gwspWarning_out"
   Print #fileNo, addTab(2); ";"

   Print #fileNo,
   Print #fileNo, addTab(2); "CLOSE resCursor;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) identifies the 'rule scope' of the work space")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")
   genProcParm(fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'")
   genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", True, "number of calls to GEN_WORKSPACE submitted")
   genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
   genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
   genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", _
     "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

   genProcSectionHeader(fileNo, "call 'global procedure'", 1, True)
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameLocal; "(mode_in, orgId_in, accessModeId_in, psOid_in, autoCommit_in, 0, callCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);"
 
   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", _
     "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
 
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
 
 
 Private Sub genAcmMetaSupportDdlForCtsConfig( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportCtsConfigByTemplate Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexMeta, processingStep, ddlType, , , , phaseDbSupport2)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)
 
   Dim qualProcedureNameSetCtsConfig As String
 
   qualProcedureNameSetCtsConfig = genQualProcName(g_sectionIndexMeta, spnSetCtsConfig, ddlType)

   ' ####################################################################################################################
   ' #    Stored Procedure initializing table CTSCONFIG
   ' ####################################################################################################################
 
   printSectionHeader("Stored Procedure initializing table CTSCONFIG", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCtsConfig
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "cts_in", "INTEGER", True, "(optional) CTS-ID to configure")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of ProductStructureto configure")
   genProcParm(fileNo, "IN", "orgOid_in", g_dbtOid, True, "(optional) OID of Organization to configure")
   genProcParm(fileNo, "IN", "ruleScopeId_in", g_dbtEnumId, True, "(optional) ID of RuleScope (Access Mode) to configure")
   genProcParm(fileNo, "IN", "serviceTypeId_in", g_dbtEnumId, True, "(optional) id of ServiceType to configure")
   genProcParm(fileNo, "IN", "overWrite_in", g_dbtBoolean, True, "existing records will be overwritten if and only if set to '1'")
   genProcParm(fileNo, "OUT", "rowCountDel_out", "INTEGER", True, "number of rows deleted in CTSCONFIG")
   genProcParm(fileNo, "OUT", "rowCountIns_out", "INTEGER", False, "number of rows inserted in CTSCONFIG")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genSpLogDecl(fileNo)
 
   Dim qualTabNameTempStatement As String
   qualTabNameTempStatement = tempTabNameStatement & "CtsConfig"
 
   genDdlForTempStatement(fileNo, 1, True, 400, True, , , , "CtsConfig", True, , , , "flag", "INTEGER")
 
   Dim qualTabNameTempOrgOids As String
   qualTabNameTempOrgOids = "SESSION.OrgOids"
   Dim qualTabNameTempPsOids As String
   qualTabNameTempPsOids = "SESSION.PsOids"
   Dim qualTabNameTempRuleScopeIds As String
   qualTabNameTempRuleScopeIds = "SESSION.RuleScopeIds"
   Dim qualTabNameTempSeverviceTypes As String
   qualTabNameTempSeverviceTypes = "SESSION.SeverviceTypes"
 
   genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs")
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempOrgOids; "( oid "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempPsOids; "( oid "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempRuleScopeIds; "( id "; g_dbtEnumId; " ) NOT LOGGED WITH REPLACE;"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE "; qualTabNameTempSeverviceTypes; "( type "; g_dbtEnumId; " ) NOT LOGGED WITH REPLACE;"
 
   genSpLogProcEnter(fileNo, qualProcedureNameSetCtsConfig, ddlType, , "mode_in", "cts_in", "psOid_in", "orgOid_in", _
                             "ruleScopeId_in", "serviceTypeId_in", "overWrite_in", "rowCountDel_out", "rowCountIns_out")

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET rowCountDel_out = 0;"
   Print #fileNo, addTab(1); "SET rowCountIns_out = 0;"
 
   genProcSectionHeader(fileNo, "loop over config templates and derive config records")
   Print #fileNo, addTab(1); "FOR templateLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CTS           AS c_cts,"
   Print #fileNo, addTab(3); "ORGOIDS       AS c_orgOIdsTmpl,"
   Print #fileNo, addTab(3); "PSOIDS        AS c_psOidsTpl,"
   Print #fileNo, addTab(3); "RULESCOPES    AS c_ruleScopesTpl,"
   Print #fileNo, addTab(3); "SERVICETYPES  AS c_serviceTypesTpl,"
   Print #fileNo, addTab(3); "STICKY        AS c_sticky,"
   Print #fileNo, addTab(3); "SIZEFACTOR    AS c_sizeFactor"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameCtsConfigTemplate
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "COALESCE(cts_in, CTS) = CTS"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader(fileNo, "empty temporary tables", 2, True)
   Print #fileNo, addTab(2); "DELETE FROM SESSION.OrgOids;"
   Print #fileNo, addTab(2); "DELETE FROM SESSION.PsOids;"
   Print #fileNo, addTab(2); "DELETE FROM SESSION.RuleScopeIds;"
   Print #fileNo, addTab(2); "DELETE FROM SESSION.SeverviceTypes;"
 
   genProcSectionHeader(fileNo, "determine ORG-OIDs related to this entry", 2)
   Print #fileNo, addTab(2); "IF c_orgOIdsTmpl IS NULL THEN"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.OrgOids ( oid ) SELECT O."; g_anOid; " FROM "; g_qualTabNameOrganization; " O WHERE COALESCE(orgOid_in, O."; g_anOid; ") = O."; g_anOid; ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.OrgOids ( oid )"
   Print #fileNo, addTab(4); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(c_orgOIdsTmpl, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameOrganization; " O ON O."; g_anOid; " = "; g_dbtOid; "(X.elem) WHERE COALESCE(orgOid_in, O."; g_anOid; ") = O."; g_anOid; ";"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "determine PS-OIDs related to this entry", 2)
   Print #fileNo, addTab(2); "IF c_psOidsTpl IS NULL THEN"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.PsOids ( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P WHERE COALESCE(psOid_in, P."; g_anOid; ") = P."; g_anOid; ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.PsOids ( oid )"
   Print #fileNo, addTab(4); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(c_psOidsTpl, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameProductStructure; " P ON P."; g_anOid; " = "; g_dbtOid; "(X.elem) WHERE COALESCE(psOid_in, P."; g_anOid; ") = P."; g_anOid; ";"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "determine rulescope-IDs related to this entry", 2)
   Print #fileNo, addTab(2); "IF c_ruleScopesTpl IS NULL THEN"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.RuleScopeIds ( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S WHERE COALESCE(ruleScopeId_in, S.ID) = S.ID;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.RuleScopeIds ( id )"
   Print #fileNo, addTab(4); "SELECT "; g_dbtEnumId; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(c_ruleScopesTpl, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameDataPoolAccessMode; " S ON S.ID = "; g_dbtEnumId; "(X.elem) WHERE COALESCE(ruleScopeId_in, S.ID) = S.ID;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "determine service-types related to this entry", 2)
   Print #fileNo, addTab(2); "IF c_serviceTypesTpl IS NULL THEN"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.SeverviceTypes ( type ) SELECT 1 FROM SYSIBM.SYSDUMMY1 WHERE COALESCE(serviceTypeId_in, 1) = 1;"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.SeverviceTypes ( type ) SELECT 2 FROM SYSIBM.SYSDUMMY1 WHERE COALESCE(serviceTypeId_in, 2) = 2;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.SeverviceTypes ( type )"
   Print #fileNo, addTab(4); "SELECT "; g_dbtEnumId; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(c_serviceTypesTpl, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(4); "WHERE "; g_dbtEnumId; "(X.elem) IN (1,2);"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "determine DELETE- and INSERT-statements", 2)
   Print #fileNo, addTab(2); "IF mode_in < 2 THEN"

   Print #fileNo, addTab(3); "IF overWrite_in = 1 THEN"
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); "SESSION.StatementsCtsConfig"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "flag,"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "0,"
   Print #fileNo, addTab(5); "'DELETE FROM ' ||"
   Print #fileNo, addTab(6); "'"; g_qualTabNameCtsConfig; " ' ||"
   Print #fileNo, addTab(5); "'WHERE ' ||"
   Print #fileNo, addTab(6); "'"; g_anRuleScope; " = ' || RTRIM(CHAR(R.id))  || ' AND ' ||"
   Print #fileNo, addTab(6); "'SERVICETYPE = ' || RTRIM(CHAR(S.type))|| ' AND ' ||"
   Print #fileNo, addTab(6); "'CORORG_OID = ' || RTRIM(CHAR(O."; g_anOid; ")) || ' AND ' ||"
   Print #fileNo, addTab(6); "'"; g_anPsOid; " = ' || RTRIM(CHAR(P."; g_anOid; "))"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SESSION.OrgOids O,"
   Print #fileNo, addTab(5); "SESSION.PsOids P,"
   Print #fileNo, addTab(5); "SESSION.RuleScopeIds R,"
   Print #fileNo, addTab(5); "SESSION.SeverviceTypes S,"
   Print #fileNo, addTab(5); g_qualTabNameCtsConfig; " C"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "C."; g_anRuleScope; " = R.id"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "C.SERVICETYPE = S.type"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "C.CORORG_OID = O.oid"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "C."; g_anPsOid; " = P.oid"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader(fileNo, "count the number of rows to delete", 4)
   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCountDel_out = rowCountDel_out + v_rowCount;"

   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.StatementsCtsConfig"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "flag,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1,"
   Print #fileNo, addTab(4); "'INSERT INTO ' ||"
   Print #fileNo, addTab(5); "'"; g_qualTabNameCtsConfig; " ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'OID, ' ||"
   Print #fileNo, addTab(5); "'CTS, ' ||"
   Print #fileNo, addTab(5); "'"; g_anRuleScope; ", ' ||"
   Print #fileNo, addTab(5); "'SERVICETYPE, ' ||"
   Print #fileNo, addTab(5); "'STICKY, ' ||"
   Print #fileNo, addTab(5); "'SIZEFACTOR, ' ||"
   Print #fileNo, addTab(5); "'CORORG_OID, ' ||"
   Print #fileNo, addTab(5); "'"; g_anPsOid; ", ' ||"
   Print #fileNo, addTab(5); "'"; g_anCreateUser; ", ' ||"
   Print #fileNo, addTab(5); "'"; g_anCreateTimestamp; ", ' ||"
   Print #fileNo, addTab(5); "'"; g_anUpdateUser; ", ' ||"
   Print #fileNo, addTab(5); "'"; g_anLastUpdateTimestamp; ", ' ||"
   Print #fileNo, addTab(5); "'"; g_anVersionId; " ' ||"
   Print #fileNo, addTab(4); "')' ||"
   Print #fileNo, addTab(4); "' VALUES ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'NEXTVAL FOR "; qualSeqNameOid; ", ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(c_cts)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(R.id)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(S.type)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(c_sticky)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(c_sizeFactor)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(O.oid)) ||', ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(P.oid)) ||', ' ||"
   Print #fileNo, addTab(5); "'CURRENT USER, ' ||"
   Print #fileNo, addTab(5); "'CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(5); "'CURRENT USER, ' ||"
   Print #fileNo, addTab(5); "'CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(5); "'1' ||"
   Print #fileNo, addTab(4); "')'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.OrgOids O"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.PsOids P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.RuleScopeIds R"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.SeverviceTypes S"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameCtsConfig; " C"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "C."; g_anRuleScope; " = R.id"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.SERVICETYPE = S.type"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.CORORG_OID = O.oid"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anPsOid; " = P.oid"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "overWrite_in = "; gc_dbTrue
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "C."; g_anOid; " IS NULL"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader(fileNo, "count the number of rows inserted", 3)
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCountIns_out = rowCountIns_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in > 0 THEN"
   genProcSectionHeader(fileNo, "execute DELETE-statement to remove eventually existing records", 3, True)
   Print #fileNo, addTab(3); "IF overWrite_in = 1 THEN"
   Print #fileNo, addTab(4); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameCtsConfig; " C"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "EXISTS ("
   Print #fileNo, addTab(6); "SELECT"
   Print #fileNo, addTab(7); "1"
   Print #fileNo, addTab(6); "FROM"
   Print #fileNo, addTab(7); "SESSION.OrgOids O,"
   Print #fileNo, addTab(7); "SESSION.PsOids P,"
   Print #fileNo, addTab(7); "SESSION.RuleScopeIds R,"
   Print #fileNo, addTab(7); "SESSION.SeverviceTypes S"
   Print #fileNo, addTab(6); "WHERE"
   Print #fileNo, addTab(7); "C."; g_anRuleScope; " = R.id"
   Print #fileNo, addTab(8); "AND"
   Print #fileNo, addTab(7); "C.SERVICETYPE = S.type"
   Print #fileNo, addTab(8); "AND"
   Print #fileNo, addTab(7); "C.CORORG_OID = O.oid"
   Print #fileNo, addTab(8); "AND"
   Print #fileNo, addTab(7); "C."; g_anPsOid; " = P.oid"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader(fileNo, "count the number of rows deleted", 4)
   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCountDel_out = rowCountDel_out + v_rowCount;"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader(fileNo, "execute INSERT-statement", 3)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); g_qualTabNameCtsConfig
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); g_anOid; ","
   Print #fileNo, addTab(4); "CTS,"
   Print #fileNo, addTab(4); g_anRuleScope; ","
   Print #fileNo, addTab(4); "SERVICETYPE,"
   Print #fileNo, addTab(4); "STICKY,"
   Print #fileNo, addTab(4); "SIZEFACTOR,"
   Print #fileNo, addTab(4); "CORORG_OID,"
   Print #fileNo, addTab(4); g_anPsOid; ","
   Print #fileNo, addTab(4); g_anCreateUser; ","
   Print #fileNo, addTab(4); g_anCreateTimestamp; ","
   Print #fileNo, addTab(4); g_anUpdateUser; ","
   Print #fileNo, addTab(4); g_anLastUpdateTimestamp; ","
   Print #fileNo, addTab(4); g_anVersionId
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "NEXTVAL FOR "; qualSeqNameOid; ","
   Print #fileNo, addTab(4); "c_cts,"
   Print #fileNo, addTab(4); "R.id,"
   Print #fileNo, addTab(4); "S.type,"
   Print #fileNo, addTab(4); "c_sticky,"
   Print #fileNo, addTab(4); "c_sizeFactor,"
   Print #fileNo, addTab(4); "O.oid,"
   Print #fileNo, addTab(4); "P.oid,"
   Print #fileNo, addTab(4); "CURRENT USER,"
   Print #fileNo, addTab(4); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(4); "CURRENT USER,"
   Print #fileNo, addTab(4); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.OrgOids O"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.PsOids P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.RuleScopeIds R"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SESSION.SeverviceTypes S"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "(1=1)"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameCtsConfig; " C"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "C."; g_anRuleScope; " = R.id"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.SERVICETYPE = S.type"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.CORORG_OID = O.oid"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anPsOid; " = P.oid"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C."; g_anOid; " IS NULL"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "count the number of rows inserted", 3)
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCountIns_out = rowCountIns_out + v_rowCount;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameTempStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "flag ASC,"
   Print #fileNo, addTab(5); "statement ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit(fileNo, qualProcedureNameSetCtsConfig, ddlType, , "mode_in", "cts_in", "psOid_in", "orgOid_in", _
                             "ruleScopeId_in", "serviceTypeId_in", "overWrite_in", "rowCountDel_out", "rowCountIns_out")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
 
   printSectionHeader("Stored Procedure initializing table CTSCONFIG", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCtsConfig
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "OUT", "rowCountDel_out", "INTEGER", True, "number of rows deleted in CTSCONFIG")
   genProcParm(fileNo, "OUT", "rowCountIns_out", "INTEGER", False, "number of rows inserted in CTSCONFIG")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcedureNameSetCtsConfig, ddlType, , "mode_in", "rowCountDel_out", "rowCountIns_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetCtsConfig; "(mode_in, NULL, NULL, NULL, NULL, NULL, 1, rowCountDel_out, rowCountIns_out);"
 
   genSpLogProcExit(fileNo, qualProcedureNameSetCtsConfig, ddlType, , "mode_in", "rowCountDel_out", "rowCountIns_out")
 
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
 
 
 Private Sub genAcmMetaSupportDdlForGenWorkspaceByOrg( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, , , phaseDbSupport)
 
   Dim qualProcedureNameGlobal As String
   Dim qualProcedureNameLocal As String
 
   ' ####################################################################################################################
   ' #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
   ' ####################################################################################################################

   qualProcedureNameGlobal = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType)
   qualProcedureNameLocal = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType, thisOrgIndex)
 
   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, False, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_callCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_orgId", g_dbtEnumId, "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "psOid_in")

   genProcSectionHeader(fileNo, "call 'global procedure'")
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameGlobal; "(?,"; genOrgId(thisOrgIndex, ddlType, True); ",?,?,0,?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_callCount"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "mode_in,"
   Print #fileNo, addTab(2); "v_orgId,"
   Print #fileNo, addTab(2); "psOid_in"
   Print #fileNo, addTab(1); ";"

   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "psOid_in")

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
 
 
 Private Sub genAcmMetaSupportDdlForGenWorkspaceByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)
 
   Dim qualProcedureNameGlobal As String
   Dim qualProcedureNameLocal As String
 
   ' ####################################################################################################################
   ' #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
   ' ####################################################################################################################

   qualProcedureNameGlobal = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType)
   qualProcedureNameLocal = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, False, "(optional) OID of the Product Structure to call GEN_WORKSPACE for")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_callCount", "INTEGER", "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "psOid_in")

   genProcSectionHeader(fileNo, "call 'global procedure'")
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameGlobal; "(?,"; genOrgId(thisOrgIndex, ddlType, True); ","; genPoolId(thisPoolIndex, ddlType); ",?,0,?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_callCount"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "mode_in,"
   Print #fileNo, addTab(2); "psOid_in"
   Print #fileNo, addTab(1); ";"

   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "mode_in", "psOid_in")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If thisPoolIndex > 0 Then
       If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
         GoTo NormalExit
       End If
   End If

   ' ####################################################################################################################
   ' #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
   ' ####################################################################################################################

   qualProcedureNameGlobal = genQualProcName(g_sectionIndexMeta, spnGenWorkspace, ddlType)
   qualProcedureNameLocal = genQualProcName(g_sectionIndexAliasLrt, spnGenWorkspace, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameLocal
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "workingSchema_in", "VARCHAR(12)", True, "primary schema of the work data pool")
   genProcParm(fileNo, "IN", "productiveSchema_in", "VARCHAR(12)", True, "primary schema of the productive data pool")
   genProcParm(fileNo, "IN", "orgOid_in", g_dbtOid, True, "OID of the Organization")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure")
   genProcParm(fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "identifies the 'rule scope' of the work space")
   genProcParm(fileNo, "OUT", "errorAt_out", "VARCHAR(256)", True, "in case of error: provides information about the error context")
   genProcParm(fileNo, "OUT", "errorInfo_out", "VARCHAR(1024)", True, "in case of error: JAVA stack trace")
   genProcParm(fileNo, "OUT", "warnings_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the procedure's result")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, , "'workingSchema_in", "'productiveSchema_in", "orgOid_in", "psOid_in", _
                             "accessModeId_in", "'errorAt_out", "'errorInfo_out", "'warnings_out")

   genProcSectionHeader(fileNo, "call 'global procedure'")
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameGlobal; "(?,?,?,?,?,?,?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "errorAt_out,"
   Print #fileNo, addTab(2); "errorInfo_out,"
   Print #fileNo, addTab(2); "warnings_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "workingSchema_in,"
   Print #fileNo, addTab(2); "productiveSchema_in,"
   Print #fileNo, addTab(2); "orgOid_in,"
   Print #fileNo, addTab(2); "psOid_in,"
   Print #fileNo, addTab(2); "accessModeId_in"
   Print #fileNo, addTab(1); ";"

   genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, , "'workingSchema_in", "'productiveSchema_in", "orgOid_in", "psOid_in", _
                             "accessModeId_in", "'errorAt_out", "'errorInfo_out", "'warnings_out")

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
 
 
 Sub genCallGenWorkspaceDdl( _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef orgOidVar As String, _
   ByRef psOidVar As String, _
   ByRef accessMode As Integer, _
   ByRef errorVar As String, _
   ByRef infoVar As String, _
   ByRef warningsVar As String, _
   Optional indent As Integer = 1, _
   Optional ddlType As DdlTypeId = edtPdm, _
   Optional skipNl As Boolean = False _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim targetSchemaName As String
   targetSchemaName = genSchemaName(snAlias, ssnAlias, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualProcedureNameGlobal As String
   qualProcedureNameGlobal = genQualProcName(g_sectionIndexMeta, spnGenWorkspace, ddlType)
 
   genProcSectionHeader(fileNo, "generate new solver-workspace (organization " & genOrgId(thisOrgIndex, ddlType, True) & ", accessMode " & genPoolId(thisPoolIndex, ddlType) & ")", indent, skipNl)

   Print #fileNo, addTab(indent + 0); "SET v_stmntTxt = 'CALL "; qualProcedureNameGlobal; "(''"; targetSchemaName; "'', ''"; targetSchemaName; "'', ?, ?, "; CStr(accessMode); ", ?, ?, ?)';"
 
   Print #fileNo, addTab(indent + 0); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo, addTab(indent + 0); "EXECUTE"
   Print #fileNo, addTab(indent + 1); "v_stmnt"
   Print #fileNo, addTab(indent + 0); "INTO"
   Print #fileNo, addTab(indent + 1); errorVar; ","
   Print #fileNo, addTab(indent + 1); infoVar; ","
   Print #fileNo, addTab(indent + 1); warningsVar
   Print #fileNo, addTab(indent + 0); "USING"
   Print #fileNo, addTab(indent + 1); orgOidVar; ","
   Print #fileNo, addTab(indent + 1); psOidVar
   Print #fileNo, addTab(indent + 0); ";"
 End Sub
 ' ### ENDIF IVK ###
 
 
