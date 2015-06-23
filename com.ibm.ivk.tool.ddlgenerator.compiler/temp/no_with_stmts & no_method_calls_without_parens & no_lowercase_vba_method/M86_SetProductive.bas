 Attribute VB_Name = "M86_SetProductive"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const pc_tempTabNameDataPool = "SESSION.DataPool"
 
 Private Const pc_tempTabNameOrgOids = "SESSION.OrgOids"
 Private Const pc_tempTabNamePsOids = "SESSION.PsOids"
 Private Const pc_tempTabNameAccessModeIds = "SESSION.AccessModeIds"
 
 'Fixme: Implement this as enumeration
 
 Global Const statusWorkInProgress = 1
 Global Const statusReadyForActivation = 2
 Global Const statusReadyForRelease = 3
 Global Const statusReadyToBeSetProductive = 4
 Global Const statusProductive = 5
 
 Private Const processingStep = 2
 
 Private Const lockModeSharedWrite = "S"
 Private Const lockModeSharedRead = "R"
 Private Const lockModeExclusiveWrite = "E"
 
 Private Const lockLogOpSet = "S"
 Private Const lockLogOpReSet = "R"
 
 
 
 Sub genSetProdSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm Then
     genSetProdSupportForDb()
     genSetProdSupportForDb2()
     genSetProdSupportForDb3()
     genRel2ProdLockWrapperDdlForDb()
     genRel2ProdLockCompatibilityWrapperDdlForDb()

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
         If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
           If g_pools.descriptors(thisPoolIndex).supportLrt Then
             genSetProdSupportDdlByPool(thisOrgIndex, thisPoolIndex, g_orgs.descriptors(thisOrgIndex).setProductiveTargetPoolIndex, edtPdm)
           End If
           genSetProdSupportDdlByPoolForAllPools(thisOrgIndex, thisPoolIndex, edtPdm)
         End If
       Next thisOrgIndex
     Next thisPoolIndex
   End If
 End Sub
 
 
 Sub genDdlForTempTablesSp( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional includeFilterTable As Boolean = False, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   If includeFilterTable Then
     genProcSectionHeader(fileNo, "temporary table for 'Set Productive'-filtered (by LRT) records", indent)
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameSpFilteredEntities
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "priceOid         "; g_dbtOid; "   NOT NULL"
     Print #fileNo, addTab(indent + 0); ")"
     genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
   End If

   genProcSectionHeader(fileNo, "temporary table for 'Set Productive'-affected records", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameSpAffectedEntities
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "orParEntityType  CHAR(1) NOT NULL,"
   Print #fileNo, addTab(indent + 1); "orParEntityId    "; g_dbtEntityId; " NOT NULL,"
   Print #fileNo, addTab(indent + 1); "isNl             "; g_dbtBoolean; " NOT NULL DEFAULT 0,"
   Print #fileNo, addTab(indent + 1); "isGen            "; g_dbtBoolean; " NOT NULL DEFAULT 0,"
   Print #fileNo, addTab(indent + 1); "oid              "; g_dbtOid; " NOT NULL,"
   Print #fileNo, addTab(indent + 1); "opId             "; g_dbtEnumId; ""
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Private Sub genSetProdSupportForDb( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexSetProductive, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualViewName As String
   Dim qualViewNameLdm  As String

   ' ####################################################################################################################
   ' #    create view to determine PDM tables involved in 'setting data productive'
   ' ####################################################################################################################
 
   qualViewName = genQualViewName(g_sectionIndexDbMeta, vnSetProdAffectedPdmTab, vsnSetProdAffectedPdmTab, ddlType)
 
   printSectionHeader("View for all PDM-tables involved in 'setting data productive'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); ""; g_anPdmTableName; ","
   Print #fileNo, addTab(1); "SOURCE_SCHEMANAME,"
   Print #fileNo, addTab(1); g_anAcmEntitySection; ","
   Print #fileNo, addTab(1); g_anAcmEntityName; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAhCid; ","
   Print #fileNo, addTab(1); g_anAcmCondenseData; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); "SEQNO"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "PDMW."; g_anPdmTableName; ","
   Print #fileNo, addTab(2); "PDMW."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntityName; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "A."; g_anAhCid; ","
   Print #fileNo, addTab(2); "A."; g_anAcmCondenseData; ","
   Print #fileNo, addTab(2); "PDMW."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "PDMW."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "L."; g_anLdmFkSequenceNo
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(1); "JOIN"
   Print #fileNo, addTab(2); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmIsCto; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmIsCtp; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anAcmEntitySection; " = A."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anAcmEntityName; " = A."; g_anAcmEntityName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(1); "JOIN"
   Print #fileNo, addTab(2); g_qualTabNamePdmTable; " PDMW"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "L."; g_anLdmSchemaName; " = PDMW."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anLdmTableName; " = PDMW."; g_anPdmLdmFkTableName
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnSetProdAffectedPdmTab, vsnSetProdAffectedPdmTab, edtLdm)
   genAliasDdl(g_sectionIndexDbMeta, vnSetProdAffectedPdmTab, _
                     True, True, True, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                     """Set Productive""-related PDM-TABLES View")
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genSetProdSupportForDb2( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexSetProductive, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualFuncNameGenRel2ProdLockKey As String
   qualFuncNameGenRel2ProdLockKey = genQualFuncName(g_sectionIndexMeta, udfnGenRel2ProdLockKey, ddlType, , , , , , True)
 
   Dim qualFuncNameParseDataPools As String
   qualFuncNameParseDataPools = genQualFuncName(g_sectionIndexMeta, udfnParseDataPools, ddlType, , , , , , True)

   Dim targetHistoryTab As Boolean
   Dim transformation As AttributeListTransformation


 
   ' ####################################################################################################################
   ' #    Release all 'Set Productive'-locks held by a given application (server)
   ' ####################################################################################################################

   Dim qualProcNameReSetLocks As String
   qualProcNameReSetLocks = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLocks, ddlType)

   Dim qualProcNameResetLock As String
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType)

   printSectionHeader("SP to release all 'Set Productive'-locks held by a given application (-server)", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameReSetLocks
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User initiating the lock release")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools unlocked")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter(fileNo, qualProcNameReSetLocks, ddlType, , "requestorId_in", "'cdUserId_in", "numDataPools_out")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET numDataPools_out = 0;"

   genProcSectionHeader(fileNo, "loop over locks related to given 'requestorId_in'")
   Print #fileNo, addTab(1); "FOR lockLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anAccessModeId; ","
   Print #fileNo, addTab(3); "RPOORG_OID,"
   Print #fileNo, addTab(3); g_anPsOid; ","
   Print #fileNo, addTab(3); g_anLockMode; ","
   Print #fileNo, addTab(3); g_anLockContext
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameRel2ProdLock
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(requestorId_in IS NULL)"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(REQUESTORID = requestorId_in)"
 
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "' ||"
   Print #fileNo, addTab(3); "(CASE "; g_anLockMode; " WHEN '"; lockModeSharedWrite; "' THEN '_SHAREDWRITE' WHEN '"; lockModeSharedRead; "' THEN '_SHAREDREAD' ELSE '_EXCLUSIVEWRITE' END) ||"
   Print #fileNo, addTab(3); "'(''' || RTRIM(CHAR(RPOORG_OID)) || ',' || RTRIM(CHAR("; g_anPsOid; ")) || ',' || RTRIM(CHAR("; g_anAccessModeId; ")) || ''',' ||"
   Print #fileNo, addTab(3); "'''' || requestorId_in || ''',''' || COALESCE(cdUserId_in, '<system>') || ''',''' || "; g_anLockContext; " || ''', ?)'"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_numDataPools"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET numDataPools_out = numDataPools_out + v_numDataPools;"
   Print #fileNo, addTab(1); "END FOR;"


   genSpLogProcExit(fileNo, qualProcNameReSetLocks, ddlType, , "requestorId_in", "'cdUserId_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Release all orphan 'Set Productive'-locks
   ' ####################################################################################################################

   Dim qualProcNameReSetLocksOrphan As String
   qualProcNameReSetLocksOrphan = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLocksOrphan, ddlType)

   printSectionHeader("SP to release all orphan 'Set Productive'-locks", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameReSetLocksOrphan
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "lockCtxtOuterPattern_in", "VARCHAR(100)", True, "(optional) (pattern of) 'outer' lockContexts considered as 'orphan' - default 'DBMaster%'")
   genProcParm(fileNo, "IN", "lockCtxtInnerPattern_in", "VARCHAR(100)", True, "(optional) (pattern of) 'outer' lockContexts considered as 'orphan' - default 'UC1022%'")
   genProcParm(fileNo, "IN", "minAgeMinutes_in", "INTEGER", True, "(optional) minimum age of lock to be considered 'orphan' (# minutes) - default 20")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "(optional) identifies the User initiating the lock release - default '<system>'")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools unlocked")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0")
   genVarDecl(fileNo, "v_refTimestamp", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_firstOuterLockTs", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_lastInnerLockTs", "TIMESTAMP", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")

   genSpLogProcEnter(fileNo, qualProcNameReSetLocksOrphan, ddlType, , "'lockCtxtOuterPattern_in", "'lockCtxtInnerPattern_in", "minAgeMinutes_in", "'cdUserId_in", "numDataPools_out")
 
   genProcSectionHeader(fileNo, "verify input parameter")
   Print #fileNo, addTab(1); "SET lockCtxtOuterPattern_in = COALESCE(lockCtxtOuterPattern_in, 'DBMaster%');"
   Print #fileNo, addTab(1); "SET lockCtxtInnerPattern_in = COALESCE(lockCtxtInnerPattern_in, 'UC1022%');"
   Print #fileNo, addTab(1); "SET minAgeMinutes_in        = COALESCE(minAgeMinutes_in,        20);"
   Print #fileNo, addTab(1); "SET cdUserId_in             = COALESCE(cdUserId_in,             '<system>');"

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET numDataPools_out = 0;"

   genProcSectionHeader(fileNo, "determine timestamp of oldest outer lock")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "MIN("; g_anLockTimestamp; ")"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_firstOuterLockTs"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock; " O"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "O."; g_anLockContext; " LIKE lockCtxtOuterPattern_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "O."; g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "O.REQUESTORID = 'anonymous'"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "if there is no outer lock there is nothing to do")
   Print #fileNo, addTab(1); "IF v_firstOuterLockTs IS NULL THEN"
   Print #fileNo, addTab(2); "RETURN 0;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "determine timestamp of youngest inner lock (from history)")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "MAX("; g_anLockTimestamp; ")"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_lastInnerLockTs"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory; " I"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "I."; g_anLockContext; " LIKE lockCtxtInnerPattern_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "I."; g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "if there is some inner lock during 'most recent history' we need to examine 'gap in lock-history'")
   Print #fileNo, addTab(1); "IF v_lastInnerLockTs >= (CURRENT TIMESTAMP - minAgeMinutes_in MINUTE) THEN"
   genProcSectionHeader(fileNo, "determine reference time stamp such that all 'older locks' are known to be 'orphan'", 2, True)
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "MAX("; g_anLockTimestamp; ")"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_refTimestamp"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameRel2ProdLockHistory; " H"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "H."; g_anLockTimestamp; " > v_firstOuterLockTs"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "H."; g_anLockContext; " LIKE lockCtxtInnerPattern_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "H."; g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NOT EXISTS ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameRel2ProdLockHistory; " R"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "R."; g_anLockTimestamp; " > (H."; g_anLockTimestamp; " - minAgeMinutes_in MINUTE)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "R."; g_anLockTimestamp; " < H."; g_anLockTimestamp; ""
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "R."; g_anLockContext; " LIKE lockCtxtInnerPattern_in"
   Print #fileNo, addTab(7); "OR"
   Print #fileNo, addTab(6); "R."; g_anLockContext; " LIKE lockCtxtOuterPattern_in"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "R."; g_anLockMode; " ='"; lockModeSharedRead; "'"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "ELSE"
   genProcSectionHeader(fileNo, "no inner lock found 'in recent history' -> all outer locks with sufficient age are orphan", 2, True)
   Print #fileNo, addTab(2); "SET v_refTimestamp   = CURRENT TIMESTAMP - minAgeMinutes_in MINUTE;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "if no reference time stamp was found there is nothing to do")
   Print #fileNo, addTab(1); "IF v_refTimestamp IS NULL THEN"
   Print #fileNo, addTab(2); "RETURN 0;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "loop over orphan locks")
   Print #fileNo, addTab(1); "FOR lockLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "O."; g_anAccessModeId; ","
   Print #fileNo, addTab(3); "O.RPOORG_OID,"
   Print #fileNo, addTab(3); "O."; g_anPsOid; ","
   Print #fileNo, addTab(3); "O.REQUESTORID,"
   Print #fileNo, addTab(3); "O."; g_anLockContext; ","
   Print #fileNo, addTab(3); "O."; g_anLockMode; ""
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameRel2ProdLock; " O"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "O."; g_anLockContext; " LIKE lockCtxtOuterPattern_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O."; g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O.REQUESTORID = 'anonymous'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "O."; g_anLockTimestamp; " < v_refTimestamp"
 
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "_OTHER' ||"
   Print #fileNo, addTab(3); "'(''' || RTRIM(CHAR(RPOORG_OID)) || ',' || RTRIM(CHAR("; g_anPsOid; ")) || ',' || RTRIM(CHAR("; g_anAccessModeId; ")) || ''',' ||"
   Print #fileNo, addTab(3); "'''' || REQUESTORID || ''',''' || COALESCE(cdUserId_in, '<system>') || ''',' || COALESCE('''' || "; g_anLockContext; " || '''', 'NULL') || ',?)'"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_numDataPools"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET numDataPools_out = numDataPools_out + v_numDataPools;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "delete outdated outer locks")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock; " O"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "O."; g_anLockContext; " LIKE lockCtxtOuterPattern_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "O."; g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "O.REQUESTORID = 'anonymous'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "O."; g_anLockTimestamp; " < v_refTimestamp"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "delete outdated inner locks")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); ""; g_anLockContext; " LIKE lockCtxtInnerPattern_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anLockMode; " = '"; lockModeSharedRead; "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); ""; g_anLockTimestamp; " < v_refTimestamp"
   Print #fileNo, addTab(1); ";"

   genSpLogProcExit(fileNo, qualProcNameReSetLocksOrphan, ddlType, , "'lockCtxtOuterPattern_in", "'lockCtxtInnerPattern_in", "minAgeMinutes_in", "'cdUserId_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP to release all orphan 'Set Productive'-locks", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameReSetLocksOrphan
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User initiating the lock release")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools unlocked")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcNameReSetLocksOrphan, ddlType, , "'cdUserId_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL "; qualProcNameReSetLocksOrphan; "('DBMaster%', 'UC1022%', 20, cdUserId_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcNameReSetLocksOrphan, ddlType, , "'cdUserId_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Function for generating KEY to use for locking a data pool for 'Set Productive'
   ' ####################################################################################################################

   printSectionHeader("Function for generating KEY to use for locking a data pool for 'Set Productive'", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameGenRel2ProdLockKey
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "orgOid_in", g_dbtOid, True, "OID of the data pool's organization")
   genProcParm(fileNo, "", "psOid_in", g_dbtOid, True, "OID of the data pool's Product Structure")
   genProcParm(fileNo, "", "accessMode_in", g_dbtEnumId, False, "access mode of the data pool")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(0); "VARCHAR(50)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"

   Print #fileNo, addTab(1); "RTRIM(CAST(orgOid_in AS CHAR(20))) || ',' || RTRIM(CAST(psOid_in AS CHAR(20))) || ',' || RTRIM(CAST(accessMode_in AS CHAR(2)))"

   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Function for parsing '|'-separated List of data pool descriptors <ORG_OID,PS_OID,ACCESSMODE_ID>
   ' ####################################################################################################################

   printSectionHeader("Function for parsing '|'-separated List of data pool descriptors <ORG_OID," & g_anPsOid & "," & g_anAccessModeId & ">", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameParseDataPools
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "dataPoolDescr_in", "VARCHAR(4000)", False, "'|'-separated List of expressions <ORG_OID," & g_anPsOid & "," & g_anAccessModeId & ">")
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "orgOid       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "psOid        "; g_dbtOid; ","
   Print #fileNo, addTab(2); "accessModeId "; g_dbtEnumId
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V_list"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "row"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "RTRIM(LTRIM(REPLACE(REPLACE(elem, '<', ''), '>', '')))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(dataPoolDescr_in, CAST('|' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "V_list1"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "col1,"
   Print #fileNo, addTab(2); "rowRest"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "LEFT(row, POSSTR(row, ',')-1),"
   Print #fileNo, addTab(3); "RIGHT(row, LENGTH(row)-POSSTR(row, ','))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_list"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "V_list2"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "col1,"
   Print #fileNo, addTab(2); "col2,"
   Print #fileNo, addTab(2); "col3"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "col1,"
   Print #fileNo, addTab(3); "LEFT(rowRest, POSSTR(rowRest, ',')-1),"
   Print #fileNo, addTab(3); "RIGHT(rowRest, LENGTH(rowRest)-POSSTR(rowRest, ','))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_list1"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CAST(CASE col1 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col1 END AS "; g_dbtOid; "),"
   Print #fileNo, addTab(2); "CAST(CASE col2 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col2 END AS "; g_dbtOid; "),"
   Print #fileNo, addTab(2); "CAST(CASE col3 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col3 END AS "; g_dbtEnumId; ")"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_list2"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP determining whether a LOCK is set on 'Set Productive'
   ' ####################################################################################################################

   Dim qualProcNameLockIsSet As String
   Dim mode As String, modeShort As String
   Dim procNameSuffix As String

   Dim j As Integer
   For j = 1 To 3
     If (j = 1) Then
         mode = "SHAREDWRITE"
         modeShort = lockModeSharedWrite
     ElseIf (j = 2) Then
         mode = "SHAREDREAD"
         modeShort = lockModeSharedRead
     Else
         mode = "EXCLUSIVEWRITE"
         modeShort = lockModeExclusiveWrite
     End If

     procNameSuffix = "_IN_" & mode & "_MODE"

     qualProcNameLockIsSet = genQualProcName(g_sectionIndexDbMeta, spnRel2ProdIsSet, ddlType, , , , procNameSuffix, eondmNone)

     printSectionHeader("SP determinig whether a LOCK is set on 'Set Productive' (" & mode & ") for a given data pool", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameLockIsSet
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool to query the LOCK-status for")
     genProcParm(fileNo, "OUT", "isLocked_out", g_dbtBoolean, False, "specifies whether a LOCK is set (0=false, 1=true)")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "delimMissing", "38552")
     genCondDecl(fileNo, "castError", "22018")
 
     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"

     genSpLogProcEnter(fileNo, qualProcNameLockIsSet, ddlType, , "'dataPoolDescr_in", "isLocked_out")

     genProcSectionHeader(fileNo, "parse dataPoolDescr_in")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "orgOid,"
     Print #fileNo, addTab(2); "psOid,"
     Print #fileNo, addTab(2); "accessModeId"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid,"
     Print #fileNo, addTab(2); "v_accessModeId"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescr_in)) AS X"
     Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "verify syntax of input parameter")
     Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN"
     genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "check if data pool is locked")
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "isLocked_out ="
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "CASE WHEN"
     Print #fileNo, addTab(5); "("
     Print #fileNo, addTab(6); "SELECT"
     Print #fileNo, addTab(7); "COUNT(*)"
     Print #fileNo, addTab(6); "FROM"
     Print #fileNo, addTab(7); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(6); "WHERE"
     Print #fileNo, addTab(7); "RPOORG_OID = v_orgOid"
     Print #fileNo, addTab(8); "AND"
     Print #fileNo, addTab(7); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(8); "AND"
     Print #fileNo, addTab(7); g_anAccessModeId; " = v_accessModeId"
     Print #fileNo, addTab(8); "AND"
     Print #fileNo, addTab(7); g_anLockMode; " = '"; modeShort; "'"
     Print #fileNo, addTab(5); ") > 0"
     Print #fileNo, addTab(4); "THEN"
     Print #fileNo, addTab(5); "1"
     Print #fileNo, addTab(4); "ELSE"
     Print #fileNo, addTab(5); "0"
     Print #fileNo, addTab(4); "END"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(1); ";"
 
     genSpLogProcExit(fileNo, qualProcNameLockIsSet, ddlType, , "'dataPoolDescr_in", "isLocked_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   Next j
 
   Dim qualProcNameSetLocks As String
 
   For j = 1 To 2
     mode = IIf(j = 1, "SHAREDWRITE", "SHAREDREAD")
     modeShort = IIf(j = 1, lockModeSharedWrite, lockModeSharedRead)
     procNameSuffix = IIf(j = 1, "_SHAREDWRITE", "_SHAREDREAD")

     ' ####################################################################################################################
     ' #    SP to acquire LOCKs for 'Set Productive'
     ' ####################################################################################################################

     qualProcNameSetLocks = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , procNameSuffix, eondmNone)

     printSectionHeader("SP to acquire LOCK for 'Set Productive'", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameSetLocks
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "data pool to acquire LOCK for")
     genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) acquiring the lock")
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User acquiring the lock")
     genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
     genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of datapools locked (0 or 1)")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "delimMissing", "38552")
     genCondDecl(fileNo, "castError", "22018")

     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
     genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL")
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"

     genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

     genProcSectionHeader(fileNo, "determine current timestamp")
     Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

     genProcSectionHeader(fileNo, "parse dataPoolDescr_in")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "orgOid,"
     Print #fileNo, addTab(2); "psOid,"
     Print #fileNo, addTab(2); "accessModeId"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid,"
     Print #fileNo, addTab(2); "v_accessModeId"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescr_in)) AS X"
     Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader(fileNo, "verify syntax of input parameter")
     Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN"
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", , True)
     Print #fileNo, addTab(1); "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;"
 
     genProcSectionHeader(fileNo, "Step 1: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "CASE WHEN"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anLockMode; " IN ('"; IIf(j = 1, lockModeSharedRead, lockModeSharedWrite); "', '"; lockModeExclusiveWrite; "')"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(3); ") > 0"
       Print #fileNo, addTab(2); "THEN"
       Print #fileNo, addTab(3); "1"
       Print #fileNo, addTab(2); "ELSE"
       Print #fileNo, addTab(3); "0"
       Print #fileNo, addTab(2); "END"
       Print #fileNo, addTab(1); ");"

       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"


    genProcSectionHeader(fileNo, "Step 2: insert new lock")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RPOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; modeShort; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid)";
     Print #fileNo, addTab(1); ";"


     genProcSectionHeader(fileNo, "Step 3: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "CASE WHEN"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anLockMode; " IN ('"; IIf(j = 1, lockModeSharedRead, lockModeSharedWrite); "', '"; lockModeExclusiveWrite; "')"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(3); ") > 0"
       Print #fileNo, addTab(2); "THEN"
       Print #fileNo, addTab(3); "1"
       Print #fileNo, addTab(2); "ELSE"
       Print #fileNo, addTab(3); "0"
       Print #fileNo, addTab(2); "END"
       Print #fileNo, addTab(1); ");"

       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "ROLLBACK TO SAVEPOINT rel2ProdLockSp;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"
 
     Print #fileNo, addTab(1); "RELEASE SAVEPOINT rel2ProdLockSp;"
     Print #fileNo,
 'determine old lock value
     Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "v_lockValue = "
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anLockMode; " = '"; modeShort; "');"
 
 
     genProcSectionHeader(fileNo, "Step 4: add history entry")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockValueOld; ","
     Print #fileNo, addTab(2); g_anLockValueNew; ","
     Print #fileNo, addTab(2); g_anLockOperation; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RHOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; modeShort; "',"
     Print #fileNo, addTab(2); "v_lockValue -1,"
     Print #fileNo, addTab(2); "v_lockValue,"
     Print #fileNo, addTab(2); "'"; lockLogOpSet; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid"
     Print #fileNo, addTab(1); ");"
 
 
     Print #fileNo,
     Print #fileNo, addTab(1); "SET numDataPools_out = 1;"

     genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next j

   ' ####################################################################################################################

   mode = "EXCLUSIVEWRITE"
   qualProcNameSetLocks = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , mode)

   printSectionHeader("SP to acquire LOCK for 'Set Productive' (" & mode & ")", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameSetLocks
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "data pool to acquire LOCK for")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) acquiring the lock")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User acquiring the lock")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools locked (0 or 1)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "delimMissing", "38552")
   genCondDecl(fileNo, "castError", "22018")

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
   genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

   genProcSectionHeader(fileNo, "parse dataPoolDescr_in")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "orgOid,"
   Print #fileNo, addTab(2); "psOid,"
   Print #fileNo, addTab(2); "accessModeId"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_orgOid,"
   Print #fileNo, addTab(2); "v_psOid,"
   Print #fileNo, addTab(2); "v_accessModeId"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescr_in)) AS X"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "verify syntax of input parameter")
   Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", , True)
   Print #fileNo, addTab(1); "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;"

   genProcSectionHeader(fileNo, "Step 1: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "CASE WHEN"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(3); ") > 0"
       Print #fileNo, addTab(2); "THEN"
       Print #fileNo, addTab(3); "1"
       Print #fileNo, addTab(2); "ELSE"
       Print #fileNo, addTab(3); "0"
       Print #fileNo, addTab(2); "END"
       Print #fileNo, addTab(1); ");"

       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"


    genProcSectionHeader(fileNo, "Step 2: insert new lock")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RPOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; lockModeExclusiveWrite; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid)";
     Print #fileNo, addTab(1); ";"


     genProcSectionHeader(fileNo, "Step 3: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "CASE WHEN"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(3); ") > 1"
       Print #fileNo, addTab(2); "THEN"
       Print #fileNo, addTab(3); "1"
       Print #fileNo, addTab(2); "ELSE"
       Print #fileNo, addTab(3); "0"
       Print #fileNo, addTab(2); "END"
       Print #fileNo, addTab(1); ");"

       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "ROLLBACK TO SAVEPOINT rel2ProdLockSp;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"
       Print #fileNo,

       Print #fileNo, addTab(1); "RELEASE SAVEPOINT rel2ProdLockSp;"
 
 
     genProcSectionHeader(fileNo, "Step 4: add history entry")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockValueOld; ","
     Print #fileNo, addTab(2); g_anLockValueNew; ","
     Print #fileNo, addTab(2); g_anLockOperation; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RHOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; lockModeExclusiveWrite; "',"
     Print #fileNo, addTab(2); "0,"
     Print #fileNo, addTab(2); "1,"
     Print #fileNo, addTab(2); "'"; lockLogOpSet; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid"
     Print #fileNo, addTab(1); ");"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET numDataPools_out = 1;"

   genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

 
   For j = 1 To 2
     mode = IIf(j = 1, "SHAREDWRITES", "SHAREDREADS")
     modeShort = IIf(j = 1, lockModeSharedWrite, lockModeSharedRead)
     procNameSuffix = IIf(j = 1, "_SHAREDWRITES", "_SHAREDREADS")

   'mode = "SHAREDREADS"
   qualProcNameSetLocks = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , mode)

   printSectionHeader("SP to acquire LOCKs for 'Set Productive' (" & mode & ")", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameSetLocks
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", True, "datapools to acquire LOCKs for")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) acquiring the lock")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User acquiring the lock")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of datapools locked")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "delimMissing", "38552")
   genCondDecl(fileNo, "castError", "22018")

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0")
   genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
   genSpLogDecl(fileNo)
 
   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescrs_in")
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescrs_in")
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "temporary table for data pool infos")
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); pc_tempTabNameDataPool
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "orgOid       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "psOid        "; g_dbtOid; ","
   Print #fileNo, addTab(2); "accessModeId "; g_dbtEnumId
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNo, 1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET numDataPools_out = 0;"

   genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", , True)
   Print #fileNo, addTab(1); "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;"

   genProcSectionHeader(fileNo, "loop over data pool descriptors")
   Print #fileNo, addTab(1); "FOR dataPoolDescrLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "orgOid        AS orgOidFltr,"
   Print #fileNo, addTab(3); "psOid         AS psOidFltr,"
   Print #fileNo, addTab(3); "accessModeId  AS accessModeIdFltr"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescrs_in)) AS X"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "collect all matching data pools in temporary table", 2, True)
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameDataPool
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "orgOid,"
   Print #fileNo, addTab(3); "psOid,"
   Print #fileNo, addTab(3); "accessModeId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P.DPOORG_OID,"
   Print #fileNo, addTab(3); "P.DPSPST_OID,"
   Print #fileNo, addTab(3); "P."; g_anAccessModeId
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataPool; " P"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); pc_tempTabNameDataPool; " TP"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "(P.DPOORG_OID = TP.orgOid)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(P.DPSPST_OID = TP.psOid)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(P."; g_anAccessModeId; " = TP.accessModeId)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(TP.accessModeId IS NULL)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(orgOidFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P.DPOORG_OID = orgOidFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(psOidFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P.DPSPST_OID = psOidFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(accessModeIdFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P."; g_anAccessModeId; " = accessModeIdFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(1); "END FOR;"


   genProcSectionHeader(fileNo, "Step 1: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "SELECT "
       Print #fileNo, addTab(3); "COUNT(*)"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); g_qualTabNameRel2ProdLock; " R"
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); pc_tempTabNameDataPool; " P"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "P.orgOid = R.RPOORG_OID"
       Print #fileNo, addTab(2); "AND"
       Print #fileNo, addTab(3); "P.psOid = R."; g_anPsOid
       Print #fileNo, addTab(2); "AND"
       Print #fileNo, addTab(3); "P.accessModeId = R."; g_anAccessModeId
       Print #fileNo, addTab(2); "WHERE"

       Print #fileNo, addTab(3); "R."; g_anLockMode; " IN ('"; IIf(j = 1, lockModeSharedRead, lockModeSharedWrite); "', '"; lockModeExclusiveWrite; "')"
       Print #fileNo, addTab(2); "WITH UR"
       Print #fileNo, addTab(1); ");"
       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"


   genProcSectionHeader(fileNo, "Step 2: insert new lock")

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RPOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "P.accessModeId,"
     Print #fileNo, addTab(2); "'"; modeShort; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "P.orgOid,"
     Print #fileNo, addTab(2); "P.psOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); pc_tempTabNameDataPool; " P"
     Print #fileNo, addTab(1);
     Print #fileNo, addTab(1); ";"


   genProcSectionHeader(fileNo, "Step 3: check for concurrent lock")
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "numDataPools_out = "
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "SELECT "
       Print #fileNo, addTab(3); "COUNT(*)"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); g_qualTabNameRel2ProdLock; " R"
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); pc_tempTabNameDataPool; " P"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "P.orgOid = R.RPOORG_OID"
       Print #fileNo, addTab(2); "AND"
       Print #fileNo, addTab(3); "P.psOid = R."; g_anPsOid
       Print #fileNo, addTab(2); "AND"
       Print #fileNo, addTab(3); "P.accessModeId = R."; g_anAccessModeId
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "R."; g_anLockMode; " IN ('"; IIf(j = 1, lockModeSharedRead, lockModeSharedWrite); "', '"; lockModeExclusiveWrite; "')"
       Print #fileNo, addTab(2); "WITH UR"
       Print #fileNo, addTab(1); ");"

       Print #fileNo,
       Print #fileNo, addTab(1); "IF numDataPools_out > 0 THEN"
       Print #fileNo, addTab(2); "SET numDataPools_out = 0;"
       Print #fileNo, addTab(2); "ROLLBACK TO SAVEPOINT rel2ProdLockSp;"
       Print #fileNo, addTab(2); "RETURN 0;"
       Print #fileNo, addTab(1); "END IF;"
       Print #fileNo,
       Print #fileNo, addTab(1); "RELEASE SAVEPOINT rel2ProdLockSp;"


   genProcSectionHeader(fileNo, "Step 4: add history entries")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockValueOld; ","
     Print #fileNo, addTab(2); g_anLockValueNew; ","
     Print #fileNo, addTab(2); g_anLockOperation; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RHOORG_OID, "
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"

    Print #fileNo, addTab(1); "WITH"
    Print #fileNo, addTab(2); "COUNTERS_PER_DATAPOOL (ORG_OID, PS_OID, ACCESSMODE_ID, COUNTER) AS"
    Print #fileNo, addTab(3); "(SELECT"
    Print #fileNo, addTab(4); "DP.DPOORG_OID, DP.DPSPST_OID, DP.ACCESSMODE_ID, COALESCE(R.COUNTER, 0)"
    Print #fileNo, addTab(3); "FROM"
    Print #fileNo, addTab(4); "VL6CMET.DataPool DP"
    Print #fileNo, addTab(3); "LEFT OUTER JOIN"
    Print #fileNo, addTab(4); "(SELECT"
    Print #fileNo, addTab(5); "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID, COUNT(1) AS COUNTER"
    Print #fileNo, addTab(4); "FROM"
    Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock; " RPL"
    Print #fileNo, addTab(4); "WHERE "
    Print #fileNo, addTab(5); " RPL.LOCKMODE = '"; modeShort; "'"
    Print #fileNo, addTab(4); "GROUP BY"
    Print #fileNo, addTab(5); "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID"
    Print #fileNo, addTab(4); ") R"
    Print #fileNo, addTab(3); " ON"
    Print #fileNo, addTab(4); "R.RPOORG_OID = DP.DPOORG_OID"
    Print #fileNo, addTab(3); "AND"
    Print #fileNo, addTab(4); "R.PS_OID = DP.DPSPST_OID"
    Print #fileNo, addTab(3); "AND"
    Print #fileNo, addTab(4); "R.ACCESSMODE_ID = DP.ACCESSMODE_ID"
    Print #fileNo, addTab(1); ")"
    Print #fileNo, addTab(1); "SELECT"
    Print #fileNo, addTab(2); "requestorId_in,"
    Print #fileNo, addTab(2); "cdUserId_in,"
    Print #fileNo, addTab(2); "lockContext_in,"
    Print #fileNo, addTab(2); "CPD.ACCESSMODE_ID,"
    Print #fileNo, addTab(2); "'"; modeShort; "',"
    Print #fileNo, addTab(2); "CPD.COUNTER -1,"
    Print #fileNo, addTab(2); "CPD.COUNTER,"
    Print #fileNo, addTab(2); "'"; lockLogOpSet; "',"
    Print #fileNo, addTab(2); "v_currentTimestamp,"
    Print #fileNo, addTab(2); "CPD.ORG_OID,"
    Print #fileNo, addTab(2); "CPD.PS_OID"
    Print #fileNo, addTab(1); "FROM"
    Print #fileNo, addTab(2); "COUNTERS_PER_DATAPOOL CPD"
    Print #fileNo, addTab(1); "INNER JOIN"
    Print #fileNo, addTab(2); "SESSION.DataPool p"
    Print #fileNo, addTab(1); "ON"
    Print #fileNo, addTab(2); "p.orgOid = CPD.ORG_OID"
    Print #fileNo, addTab(1); "AND"
    Print #fileNo, addTab(2); "p.psOid = CPD.PS_OID"
    Print #fileNo, addTab(1); "AND"
    Print #fileNo, addTab(2); "p.accessModeId = CPD.ACCESSMODE_ID"
    Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected data pools")
   Print #fileNo, addTab(1); "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;"


   genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
  Next j

 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genSetProdSupportForDb3( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexSetProductive, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualFuncNameGenRel2ProdLockKey As String
   qualFuncNameGenRel2ProdLockKey = genQualFuncName(g_sectionIndexMeta, udfnGenRel2ProdLockKey, ddlType, , , , , , True)
 
   Dim qualFuncNameParseDataPools As String
   qualFuncNameParseDataPools = genQualFuncName(g_sectionIndexMeta, udfnParseDataPools, ddlType, , , , , , True)

   Dim qualProcNameSetLocks As String
   Dim procNameSuffix As String
   Dim mode As String
   Dim modeShort As String

   Dim qualProcNameResetLock As String
   Dim j As Integer
   For j = 1 To 2
     mode = IIf(j = 1, "SHAREDWRITE", "SHAREDREAD")
     modeShort = IIf(j = 1, lockModeSharedWrite, lockModeSharedRead)
     procNameSuffix = IIf(j = 1, "_SHAREDWRITE", "_SHAREDREAD")

     ' ####################################################################################################################
     ' #    Release LOCKs for 'Set Productive'
     ' ####################################################################################################################

     qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , procNameSuffix, eondmNone)

     printSectionHeader("SP to release LOCK for 'Set Productive'", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameResetLock
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "data pool for which to release the lock")
     genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) releasing the lock")
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User releasing the lock")
     genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
     genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools unlocked (0 or 1)")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "delimMissing", "38552")
     genCondDecl(fileNo, "castError", "22018")

     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
     genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL")
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
     Print #fileNo, addTab(1); "BEGIN"
     genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END;"

     genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

     genProcSectionHeader(fileNo, "determine current timestamp")
     Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

     genProcSectionHeader(fileNo, "parse dataPoolDescr_in")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "orgOid,"
     Print #fileNo, addTab(2); "psOid,"
     Print #fileNo, addTab(2); "accessModeId"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid,"
     Print #fileNo, addTab(2); "v_accessModeId"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescr_in)) AS X"
     Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader(fileNo, "verify syntax of input parameter")
     Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN"
     genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
     genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
     Print #fileNo, addTab(1); "END IF;"

   'determine old lock value
       Print #fileNo,
       Print #fileNo, addTab(1); "SET "
       Print #fileNo, addTab(2); "v_lockValue = "
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT "
       Print #fileNo, addTab(5); "COUNT(*)"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "RPOORG_OID = v_orgOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anAccessModeId; " = v_accessModeId"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(5); g_anLockMode; " = '"; modeShort; "');"

     genProcSectionHeader(fileNo, "remove log-record for lock")
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "REQUESTORID = requestorId_in"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); "COALESCE("; g_anLockContext; ", '') = COALESCE(lockContext_in, '')"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anAccessModeId; " = v_accessModeId"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); "RPOORG_OID = v_orgOid"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anLockMode; " = '"; modeShort; "'"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader(fileNo, "count the number of affected log records")
     Print #fileNo, addTab(1); "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;"

     Print #fileNo,
     Print #fileNo, addTab(1); "IF numDataPools_out = 0 THEN"
     genProcSectionHeader(fileNo, "if no log record was found, do not write history entry", 2, True)
     Print #fileNo, addTab(2); "RETURN 0;"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "add history-records to keep track of released locks")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockValueOld; ","
     Print #fileNo, addTab(2); g_anLockValueNew; ","
     Print #fileNo, addTab(2); g_anLockOperation; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RHOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; modeShort; "',"
     Print #fileNo, addTab(2); "v_lockValue,"
     Print #fileNo, addTab(2); "v_lockValue - 1,"
     Print #fileNo, addTab(2); "'"; lockLogOpReSet; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid)"
     Print #fileNo, addTab(1); ";"

     Print #fileNo,
     Print #fileNo, addTab(1); "SET numDataPools_out = 1;"
     Print #fileNo, addTab(1); "RETURN 0;"

     genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next j

   ' ####################################################################################################################

   mode = "EXCLUSIVEWRITE"
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , mode)

   printSectionHeader("SP to release LOCK for 'Set Productive' (" & mode & ")", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameResetLock
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "data pool for which to release the lock")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) releasing the lock")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User releasing the lock")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools unlocked (0 or 1)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "delimMissing", "38552")
   genCondDecl(fileNo, "castError", "22018")

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
   genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter(fileNo, qualProcNameResetLock, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
 
   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

   genProcSectionHeader(fileNo, "parse dataPoolDescr_in")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "orgOid,"
   Print #fileNo, addTab(2); "psOid,"
   Print #fileNo, addTab(2); "accessModeId"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_orgOid,"
   Print #fileNo, addTab(2); "v_psOid,"
   Print #fileNo, addTab(2); "v_accessModeId"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescr_in)) AS X"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "verify syntax of input parameter")
   Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescr_in")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "remove log-record for lock")
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "REQUESTORID = requestorId_in"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); "COALESCE("; g_anLockContext; ", '') = COALESCE(lockContext_in, '')"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anAccessModeId; " = v_accessModeId"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); "RPOORG_OID = v_orgOid"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); g_anLockMode; " = '"; lockModeExclusiveWrite; "';"
 
     genProcSectionHeader(fileNo, "count the number of affected records")
     Print #fileNo, addTab(1); "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;"

     Print #fileNo,
     Print #fileNo, addTab(1); "IF numDataPools_out = 0 THEN"
     genProcSectionHeader(fileNo, "if no log record was found, do not write history entry", 2, True)
     Print #fileNo, addTab(2); "RETURN 0;"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "add history record to keep track of released locks")
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "REQUESTORID,"
     Print #fileNo, addTab(2); g_anUserId; ","
     Print #fileNo, addTab(2); g_anLockContext; ","
     Print #fileNo, addTab(2); g_anAccessModeId; ","
     Print #fileNo, addTab(2); g_anLockMode; ","
     Print #fileNo, addTab(2); g_anLockValueOld; ","
     Print #fileNo, addTab(2); g_anLockValueNew; ","
     Print #fileNo, addTab(2); g_anLockOperation; ","
     Print #fileNo, addTab(2); g_anLockTimestamp; ","
     Print #fileNo, addTab(2); "RHOORG_OID,"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES("
     Print #fileNo, addTab(2); "requestorId_in,"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "lockContext_in,"
     Print #fileNo, addTab(2); "v_accessModeId,"
     Print #fileNo, addTab(2); "'"; lockModeExclusiveWrite; "',"
     Print #fileNo, addTab(2); "1,"
     Print #fileNo, addTab(2); "0,"
     Print #fileNo, addTab(2); "'"; lockLogOpReSet; "',"
     Print #fileNo, addTab(2); "v_currentTimestamp,"
     Print #fileNo, addTab(2); "v_orgOid,"
     Print #fileNo, addTab(2); "v_psOid)"
     Print #fileNo, addTab(1); ";"

     Print #fileNo,
    Print #fileNo, addTab(1); "SET numDataPools_out = 1;"
    Print #fileNo, addTab(1); "RETURN 0;"

   genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   'mode = "SHAREDREADS"
   For j = 1 To 2
     mode = IIf(j = 1, "SHAREDWRITES", "SHAREDREADS")
     modeShort = IIf(j = 1, lockModeSharedWrite, lockModeSharedRead)
     procNameSuffix = IIf(j = 1, "_SHAREDWRITES", "_SHAREDREADS")
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , mode)

   printSectionHeader("SP to release LOCKs for 'Set Productive' (" & mode & ")", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameResetLock
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", True, "datapools for which to release the lock")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server) releasing the locks")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User releasing the locks")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of datapools unlocked")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "delimMissing", "38552")
   genCondDecl(fileNo, "castError", "22018")

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)
 
   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR delimMissing"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescrs_in")
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR castError"
   Print #fileNo, addTab(1); "BEGIN"
   genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, , , , , , , , , , "dataPoolDescrs_in")
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "temporary table for data pool infos")
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); pc_tempTabNameDataPool
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "orgOid       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "psOid        "; g_dbtOid; ","
   Print #fileNo, addTab(2); "accessModeId "; g_dbtEnumId
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNo, 1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameResetLock, ddlType, , "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
 
   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET numDataPools_out = 0;"

   genProcSectionHeader(fileNo, "loop over data pool descriptors")
   Print #fileNo, addTab(1); "FOR dataPoolDescrLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "orgOid        AS orgOidFltr,"
   Print #fileNo, addTab(3); "psOid         AS psOidFltr,"
   Print #fileNo, addTab(3); "accessModeId  AS accessModeIdFltr"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE("; qualFuncNameParseDataPools; "(dataPoolDescrs_in)) AS X"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader(fileNo, "collect all matching data pools in temporary table", 2, True)
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameDataPool
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "orgOid,"
   Print #fileNo, addTab(3); "psOid,"
   Print #fileNo, addTab(3); "accessModeId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "P.DPOORG_OID,"
   Print #fileNo, addTab(3); "P.DPSPST_OID,"
   Print #fileNo, addTab(3); "P."; g_anAccessModeId; ""
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDataPool; " P"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); pc_tempTabNameDataPool; " TP"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "(P.DPOORG_OID = TP.orgOid)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(P.DPSPST_OID = TP.psOid)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(P."; g_anAccessModeId; " = TP.accessModeId)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(TP.accessModeId IS NULL)"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(orgOidFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P.DPOORG_OID = orgOidFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(psOidFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P.DPSPST_OID = psOidFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(accessModeIdFltr IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(P."; g_anAccessModeId; " = accessModeIdFltr)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "add history-records to keep track of locks")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLockHistory
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "REQUESTORID,"
   Print #fileNo, addTab(2); g_anUserId; ","
   Print #fileNo, addTab(2); g_anLockContext; ","
   Print #fileNo, addTab(2); g_anAccessModeId; ","
   Print #fileNo, addTab(2); g_anLockMode; ","
   Print #fileNo, addTab(2); g_anLockValueOld; ","
   Print #fileNo, addTab(2); g_anLockValueNew; ","
   Print #fileNo, addTab(2); g_anLockOperation; ","
   Print #fileNo, addTab(2); g_anLockTimestamp; ","
   Print #fileNo, addTab(2); "RHOORG_OID,"
   Print #fileNo, addTab(2); g_anPsOid
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "COUNTERS_PER_DATAPOOL (ORG_OID, PS_OID, ACCESSMODE_ID, COUNTER) AS"
   Print #fileNo, addTab(3); "(SELECT"
   Print #fileNo, addTab(4); "DP.DPOORG_OID, DP.DPSPST_OID, DP.ACCESSMODE_ID, COALESCE(R.COUNTER, 0)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "VL6CMET.DataPool DP"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "(SELECT"
   Print #fileNo, addTab(5); "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID, COUNT(1) AS COUNTER"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameRel2ProdLock; " RPL"
   Print #fileNo, addTab(4); "WHERE "
   Print #fileNo, addTab(5); "RPL.LOCKMODE = '"; modeShort; "'"
   Print #fileNo, addTab(4); "GROUP BY"
   Print #fileNo, addTab(5); "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID"
   Print #fileNo, addTab(4); ") R"
   Print #fileNo, addTab(3); " ON"
   Print #fileNo, addTab(4); "R.RPOORG_OID = DP.DPOORG_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "R.PS_OID = DP.DPSPST_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "R.ACCESSMODE_ID = DP.ACCESSMODE_ID"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "requestorId_in,"
   Print #fileNo, addTab(2); "cdUserId_in,"
   Print #fileNo, addTab(2); "lockContext_in,"
   Print #fileNo, addTab(2); "CPD.ACCESSMODE_ID,"
   Print #fileNo, addTab(2); "'"; modeShort; "',"
   Print #fileNo, addTab(2); "CPD.COUNTER,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "'"; lockLogOpReSet; "',"
   Print #fileNo, addTab(2); "v_currentTimestamp,"
   Print #fileNo, addTab(2); "CPD.ORG_OID,"
   Print #fileNo, addTab(2); "CPD.PS_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "COUNTERS_PER_DATAPOOL CPD"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); "SESSION.DataPool p"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "p.orgOid = CPD.ORG_OID"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "p.psOid = CPD.PS_OID"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "p.accessModeId = CPD.ACCESSMODE_ID"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "remove log-records for locks")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); g_qualTabNameRel2ProdLock; " L"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "L.REQUESTORID = requestorId_in"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "COALESCE(L."; g_anLockContext; ", '') = COALESCE(lockContext_in, '')"
   Print #fileNo, addTab(1); "AND"
   Print #fileNo, addTab(2); "EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameDataPool; " P"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "L."; g_anAccessModeId; " = P.accessModeId"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "L.RPOORG_OID = P.orgOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "L."; g_anPsOid; " = P.psOid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected data pools")
   Print #fileNo, addTab(1); "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;"

   Print #fileNo,
   genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, , "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

  Next j

   ' ####################################################################################################################


 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Private Sub genRel2ProdLockCompatibilityWrapperDdlForDb( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexSetProductive, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)

   Dim qualProcName As String

   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType)

   printSectionHeader("SP SET_REL2PRODLOCK", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.SET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "GENWS")

   printSectionHeader("SP SET_REL2PRODLOCK_GENWS", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.SET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "OTHER")

   printSectionHeader("SP SET_REL2PRODLOCK_OTHER", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.SET_REL2PRODLOCK_SHAREDREAD(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "OTHERS")

   printSectionHeader("SP SET_REL2PRODLOCK_OTHERS", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(50)", True, "specifies the data pools")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) acquiring the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.SET_REL2PRODLOCK_SHAREDREADS(dataPoolDescrs_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType)

   printSectionHeader("SP RESET_REL2PRODLOCK", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.RESET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "GENWS")

   printSectionHeader("SP RESET_REL2PRODLOCK_GENWS", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.RESET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "OTHER")

   printSectionHeader("SP RESET_REL2PRODLOCK_OTHER", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.RESET_REL2PRODLOCK_SHAREDREAD(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "OTHERS")

   printSectionHeader("SP RESET_REL2PRODLOCK_OTHERS", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(50)", True, "specifies the data pools")
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "(optional) identifies the Application (Server) to release the locks for")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.RESET_REL2PRODLOCK_SHAREDREADS(dataPoolDescrs_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexDbMeta, spnRel2ProdIsSet, ddlType)

   printSectionHeader("SP IS_REL2PRODLOCK_SET", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool")
   genProcParm(fileNo, "OUT", "isLocked_out", g_dbtBoolean, False, "specifies whether a LOCK is set (0=false, 1=true)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "isLocked_out")

   Print #fileNo, addTab(1); "CALL VL6CDBM.IS_REL2PRODLOCK_SET_IN_EXCLUSIVEWRITE_MODE(dataPoolDescr_in, isLocked_out);"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "'dataPoolDescr_in", "isLocked_out")

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
 
 Private Sub genRel2ProdLockWrapperDdlForDb( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexSetProductive, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)

   Dim qualProcedureNameRel2ProdLocks As String

   Dim qualTabNameTempStatement As String
   qualTabNameTempStatement = tempTabNameStatement & "Rel2ProdLocks"

   Dim forReset As Boolean
   Dim spName As String
   Dim i As Integer
   For i = 1 To 2
     forReset = (i = 2)
     If forReset Then
       spName = spnResetRel2ProdLock
       qualProcedureNameRel2ProdLocks = _
         genQualProcName(g_sectionIndexDbAdmin, spnResetRel2ProdLocksWrapper, ddlType)
     Else
       spName = spnSetRel2ProdLock
       qualProcedureNameRel2ProdLocks = _
         genQualProcName(g_sectionIndexDbAdmin, spnSetRel2ProdLocksWrapper, ddlType)
     End If

     ' ####################################################################################################################
     ' #    Wrapper-Stored Procedure for requesting / releasing REL2PROD-Locks
     ' ####################################################################################################################

     printSectionHeader("Wrapper-Stored Procedure for " & IIf(forReset, "release of", "requesting") & " REL2PROD-Locks", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameRel2ProdLocks
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only")
     genProcParm(fileNo, "IN", "psOidList_in", "VARCHAR(400)", True, "(optional) ','-delimited list of OIDs of ProductStructures")
     genProcParm(fileNo, "IN", "orgOidList_in", "VARCHAR(400)", True, "(optional) ','-delimited list of OIDs of Organizations")
     genProcParm(fileNo, "IN", "accessModeIdList_in", "VARCHAR(50)", True, "(optional) ','-delimited list of AccessModes")
     genProcParm(fileNo, "IN", "abortOnFailure_in", g_dbtBoolean, True, "if set to '1' abort (and rollback all locks requested so far)")
     genProcParm(fileNo, "IN", "lockMode_in", g_dbtEnumId, True, "'0' = 'SHAREDREAD', '1' = 'EXCLUSIVEWRITE', '2' = 'SHAREDWRITE'")
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the (Business-) User calling this procedure")
     genProcParm(fileNo, "IN", "lockContext_in", "VARCHAR(100)", True, "(optional) refers to the Use Case context")
     genProcParm(fileNo, "OUT", "psOidFail_out", g_dbtOid, True, "identifies the PS of the (last) data pool for which lock-operation failed")
     genProcParm(fileNo, "OUT", "orgOidFail_out", g_dbtOid, True, "identifies the Organization of the (last) data pool for which lock-operation failed")
     genProcParm(fileNo, "OUT", "accessModeIdFail_out", g_dbtEnumId, True, "identifies the AccessMode of the (last) data pool for which lock-operation failed")
     genProcParm(fileNo, "OUT", "locksRequested_out", "INTEGER", True, "number of locks processed")
     genProcParm(fileNo, "OUT", "locksFailed_out", "INTEGER", False, "number of locks failed to process")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", , True)
     Print #fileNo, addTab(1); "SAVEPOINT lockFail ON ROLLBACK RETAIN CURSORS;"

     Print #fileNo,
     Print #fileNo, addTab(1); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", 2, True)
     genSigMsgVarDecl(fileNo, 2)
     genVarDecl(fileNo, "v_procName", "VARCHAR(50)", "NULL", 2)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(150)", "NULL", 2)
     genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0", 2)
     genVarDecl(fileNo, "v_requestorId", "VARCHAR(100)", "'anonymous'", 2)
     genSpLogDecl(fileNo, 2, True)

     genProcSectionHeader(fileNo, "declare statement", 2)
     genVarDecl(fileNo, "v_stmnt", "STATEMENT", , 2)

     genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", 2)
     Print #fileNo, addTab(2); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
     Print #fileNo, addTab(2); "BEGIN"
     Print #fileNo, addTab(3); "ROLLBACK TO SAVEPOINT lockFail;"
     Print #fileNo, addTab(3); "RESIGNAL;"
     Print #fileNo, addTab(2); "END;"

     genDdlForTempStatement(fileNo, 2, True, 150, True, True, True, , "Rel2ProdLocks", True, , , , "status", "CHAR(1)")

     genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs", 2)
     Print #fileNo, addTab(2); "DECLARE GLOBAL TEMPORARY TABLE "; pc_tempTabNameOrgOids; "( oid  "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
     Print #fileNo, addTab(2); "DECLARE GLOBAL TEMPORARY TABLE "; pc_tempTabNamePsOids; "( oid  "; g_dbtOid; " ) NOT LOGGED WITH REPLACE;"
     Print #fileNo, addTab(2); "DECLARE GLOBAL TEMPORARY TABLE "; pc_tempTabNameAccessModeIds; "( id "; g_dbtEnumId; " ) NOT LOGGED WITH REPLACE;"

     genSpLogProcEnter(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 2, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", _
                               "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out")

     genProcSectionHeader(fileNo, "verify input parameter", 2)
     Print #fileNo, addTab(2); "SET cdUserId_in = COALESCE(cdUserId_in, LEFT(CURRENT USER, 16));"

     genProcSectionHeader(fileNo, "initialize output parameter", 2)
     Print #fileNo, addTab(2); "SET psOidFail_out        = NULL;"
     Print #fileNo, addTab(2); "SET orgOidFail_out       = NULL;"
     Print #fileNo, addTab(2); "SET accessModeIdFail_out = NULL;"
     Print #fileNo, addTab(2); "SET locksRequested_out   = 0;"
     Print #fileNo, addTab(2); "SET locksFailed_out      = 0;"

     genProcSectionHeader(fileNo, "determine procedure to call", 2)
     Print #fileNo, addTab(2); "IF lockMode_in = 0 THEN"
     Print #fileNo, addTab(3); "SET v_procName = '"; _
                               genQualProcName(g_sectionIndexDbMeta, spName, ddlType, , , , "SHAREDREAD"); "';"
     Print #fileNo, addTab(2); "ELSEIF lockMode_in = 1 THEN"
     Print #fileNo, addTab(3); "SET v_procName = '"; _
                               genQualProcName(g_sectionIndexDbMeta, spName, ddlType, , , , "EXCLUSIVEWRITE"); "';"
     Print #fileNo, addTab(2); "ELSEIF lockMode_in = 2 THEN"
     Print #fileNo, addTab(3); "SET v_procName = '"; _
                               genQualProcName(g_sectionIndexDbMeta, spName, ddlType, , , , "SHAREDWRITE"); "';"
     Print #fileNo, addTab(2); "ELSE"
     genSpLogProcEscape(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 3, , "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", _
                               "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out")
     genSignalDdlWithParms("illegParam", fileNo, 3, "lockMode_in", , , , , , , , , "RTRIM(CHAR(lockMode_in))")
     Print #fileNo, addTab(2); "END IF;"

     genProcSectionHeader(fileNo, "determine referred ORG-OIDs", 2)
     Print #fileNo, addTab(2); "IF orgOidList_in IS NULL THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNameOrgOids; " ( oid ) SELECT O."; g_anOid; " FROM "; g_qualTabNameOrganization; " O;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNameOrgOids; " ( oid )"
     Print #fileNo, addTab(4); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(orgOidList_in, CAST(',' AS CHAR(1))) ) AS X"
     Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameOrganization; " O ON O."; g_anOid; " = "; g_dbtOid; "(X.elem);"
     Print #fileNo, addTab(2); "END IF;"

     genProcSectionHeader(fileNo, "determine referred PS-OIDs", 2)
     Print #fileNo, addTab(2); "IF psOidList_in IS NULL THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNamePsOids; " ( oid ) SELECT P."; g_anOid; " FROM "; g_qualTabNameProductStructure; " P;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNamePsOids; " ( oid )"
     Print #fileNo, addTab(4); "SELECT "; g_dbtOid; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X"
     Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameProductStructure; " P ON P."; g_anOid; " = "; g_dbtOid; "(X.elem);"
     Print #fileNo, addTab(2); "END IF;"

     genProcSectionHeader(fileNo, "determine referred AccessMode-IDs", 2)
     Print #fileNo, addTab(2); "IF accessModeIdList_in IS NULL THEN"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNameAccessModeIds; " ( id ) SELECT S.ID FROM "; g_qualTabNameDataPoolAccessMode; " S;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "INSERT INTO "; pc_tempTabNameAccessModeIds; " ( id )"
     Print #fileNo, addTab(4); "SELECT "; g_dbtEnumId; "(X.elem) FROM TABLE ( "; g_qualFuncNameStrElems; "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X"
     Print #fileNo, addTab(4); "INNER JOIN "; g_qualTabNameDataPoolAccessMode; " S ON S.ID = "; g_dbtEnumId; "(X.elem);"
     Print #fileNo, addTab(2); "END IF;"

     genProcSectionHeader(fileNo, "loop over referred data pools and lock", 2)
     Print #fileNo, addTab(2); "FOR poolLoop AS poolCursor CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "DP.DPOORG_OID AS c_orgOid,"
     Print #fileNo, addTab(4); "DP.DPSPST_OID AS c_psOid,"
     Print #fileNo, addTab(4); "DP."; g_anAccessModeId; " AS c_accessModeId"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "VL6CMET.DATAPOOL DP"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); pc_tempTabNameOrgOids; " O"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "DP.DPOORG_OID = O.oid"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); pc_tempTabNamePsOids; " P"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "DP.DPSPST_OID = P.oid"
     Print #fileNo, addTab(3); "INNER JOIN"
     Print #fileNo, addTab(4); pc_tempTabNameAccessModeIds; " A"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "DP."; g_anAccessModeId; " = A.id"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "DP.DPOORG_OID,"
     Print #fileNo, addTab(4); "DP.DPSPST_OID,"
     Print #fileNo, addTab(4); "DP."; g_anAccessModeId

     Print #fileNo, addTab(2); "DO"

     genProcSectionHeader(fileNo, "count data pool", 3, True)
     Print #fileNo, addTab(3); "SET locksRequested_out = locksRequested_out + 1;"

     genProcSectionHeader(fileNo, "determine statement to execute", 3)
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL ' || v_procName || '(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || '''' ||"; _
                               "', ''' || v_requestorId || ''', ''' || cdUserId_in || ''', ''' || lockContext_in || ''', ?)';"
     Print #fileNo, addTab(3); "SET v_numDataPools = 0;"

     genProcSectionHeader(fileNo, "execute statement", 3)
     Print #fileNo, addTab(3); "IF mode_in > 0 THEN"
     Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo, addTab(4); "EXECUTE v_stmnt INTO v_numDataPools;"
     Print #fileNo, addTab(3); "END IF;"

     genProcSectionHeader(fileNo, "store statement in temporary table", 3)
     Print #fileNo, addTab(3); "IF mode_in < 2 THEN"
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); qualTabNameTempStatement
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "status,"
     Print #fileNo, addTab(5); "statement"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "(CASE WHEN v_numDataPools = 0 THEN '-' ELSE '+' END),"
     Print #fileNo, addTab(5); "v_stmntTxt"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     genProcSectionHeader(fileNo, "verify that operation succeeded", 3)
     Print #fileNo, addTab(3); "IF mode_in > 0 THEN"
     Print #fileNo, addTab(4); "IF v_numDataPools = 0 THEN"
     genProcSectionHeader(fileNo, "count data pool failed", 5, True)
     Print #fileNo, addTab(5); "SET locksFailed_out = locksFailed_out + 1;"

     genProcSectionHeader(fileNo, "keep track of failed data pool ", 5)
     Print #fileNo, addTab(5); "SET psOidFail_out        = c_psOid;"
     Print #fileNo, addTab(5); "SET orgOidFail_out       = c_orgOid;"
     Print #fileNo, addTab(5); "SET accessModeIdFail_out = c_accessModeId;"

     genProcSectionHeader(fileNo, "exit - if requested to abort on failure", 5)
     Print #fileNo, addTab(5); "IF abortOnFailure_in = 1 THEN"

     genSpLogProcEscape(fileNo, qualProcedureNameRel2ProdLocks, ddlType, -6, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", _
                               "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out")
     Print #fileNo, addTab(6); "ROLLBACK TO SAVEPOINT lockFail;"

     Print #fileNo, addTab(6); "RETURN 1;"
     Print #fileNo, addTab(5); "END IF;"
     Print #fileNo, addTab(4); "END IF;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     genProcSectionHeader(fileNo, "return result to application", 2)
     Print #fileNo, addTab(2); "IF mode_in = 1 THEN"
     Print #fileNo, addTab(3); "BEGIN"
     Print #fileNo, addTab(4); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "status,"
     Print #fileNo, addTab(6); "statement"
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); qualTabNameTempStatement
     Print #fileNo, addTab(5); "ORDER BY"
     Print #fileNo, addTab(6); "seqNo"
     Print #fileNo, addTab(5); "FOR READ ONLY"
     Print #fileNo, addTab(4); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 4)
     Print #fileNo, addTab(4); "OPEN resCursor;"
     Print #fileNo, addTab(3); "END;"

     Print #fileNo, addTab(2); "ELSEIF mode_in = 0 THEN"

     Print #fileNo, addTab(3); "BEGIN"
     Print #fileNo, addTab(4); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "statement"
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); qualTabNameTempStatement
     Print #fileNo, addTab(5); "ORDER BY"
     Print #fileNo, addTab(6); "seqNo"
     Print #fileNo, addTab(5); "FOR READ ONLY"
     Print #fileNo, addTab(4); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 4)
     Print #fileNo, addTab(4); "OPEN resCursor;"
     Print #fileNo, addTab(3); "END;"
     Print #fileNo, addTab(2); "END IF;"

     genSpLogProcExit(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 2, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", _
                              "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out")

     Print #fileNo, addTab(1); "END;"
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
 
 
 Private Sub genSetProdSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional srcPoolIndex As Integer = -1, _
   Optional dstPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not g_genLrtSupport Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, dstPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   Dim qualViewNameAffectedPdmTabGlob As String
   qualViewNameAffectedPdmTabGlob = genQualViewName(g_sectionIndexDbMeta, vnSetProdAffectedPdmTab, vsnSetProdAffectedPdmTab, ddlType)
 
   Dim qualTabNameChangeLog As String
   Dim qualTabNameChangeLogNl As String

   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, dstPoolIndex)
   qualTabNameChangeLogNl = genQualNlTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, dstPoolIndex)

   Dim qualTabNameGenericAspectSrc As String
   qualTabNameGenericAspectSrc = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, srcPoolIndex)

   Dim qualTabNameGenericAspectDst As String
   qualTabNameGenericAspectDst = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, dstPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   ' ####################################################################################################################
   ' #    SP for Determining Entities affected by 'Set Data Productive'
   ' ####################################################################################################################

   Dim qualProcedureNameSpGetAffectedEntities As String
 
   qualProcedureNameSpGetAffectedEntities = _
     genQualProcName(g_sectionIndexProductStructure, spnSPGetAffectedEntities, ddlType, thisOrgIndex, srcPoolIndex)
 
   printSectionHeader("SP for Determining Entities affected by 'Set Data Productive'", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSpGetAffectedEntities
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - if specified only consider prices related to this LRT")

   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables containing records to be set productive")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records found to be 'set productive'")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions")
   genCondDecl(fileNo, "alreadyExist", "42710")

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_tabCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempTablesSp(fileNo, , True)
   Print #fileNo,
   Print #fileNo, addTab(1); "CREATE INDEX"
   Print #fileNo, addTab(2); "SESSION.IDX_SPAFFECTEDENTITIES"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); gc_tempTabNameSpAffectedEntities
   Print #fileNo, addTab(1); "(OID ASC);"

   genSpLogProcEnter(fileNo, qualProcedureNameSpGetAffectedEntities, ddlType, , "psOid_in", "lrtOid_in", "tabCount_out", "rowCount_out")
 
   genProcSectionHeader(fileNo, "initialize output parameters")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader(fileNo, "cleanup temporary table", 1)
   Print #fileNo, addTab(1); "DELETE FROM "; gc_tempTabNameSpAffectedEntities; ";"
   Print #fileNo, addTab(1); "DELETE FROM "; gc_tempTabNameSpFilteredEntities; ";"

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, srcPoolIndex)
   Dim qualTabNameProperty As String
   qualTabNameProperty = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, srcPoolIndex)
   Dim priceAssignmentClassIdList As String
   priceAssignmentClassIdList = g_classes.descriptors(g_classIndexGenericAspect).subclassIdStrListNonAbstractPriceAssignment
   Dim qualTabNameTypeSpec As String
   qualTabNameTypeSpec = genQualTabNameByClassIndex(g_classIndexTypeSpec, ddlType, thisOrgIndex, srcPoolIndex)
   Dim qualTabNameTypeStandardEquipment As String
   qualTabNameTypeStandardEquipment = genQualTabNameByClassIndex(g_classIndexTypeStandardEquipment, ddlType, thisOrgIndex, srcPoolIndex)

   genProcSectionHeader(fileNo, "if LRT-OID is specified determine OIDs of records related to this LRT for filtering", 1)
   Print #fileNo, addTab(1); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); gc_tempTabNameSpFilteredEntities
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "priceOid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_FilteredOid"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "oid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CAST(LEFT("; g_anValue; ",19) AS "; g_dbtOid; ")"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameRegistryDynamic

   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anSection; " = '"; gc_regDynamicSectionAutoSetProd; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anKey; " = '"; gc_regDynamicKeyAutoSetProd; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anSubKey; " = '"; Right("00" & genOrgId(thisOrgIndex, ddlType, True), 2); "-' || RTRIM(CAST(lrtOid_in AS CHAR(40)))"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_qualFuncNameIsNumeric; "("; g_anValue; ") = "; gc_dbTrue
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "R.oid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_FilteredOid R"
   Print #fileNo, addTab(2); "INNER Join"
   Print #fileNo, addTab(3); qualTabNameGenericAspectSrc; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "R.oid = A."; g_anOid
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anStatus; " = "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "loop over tables related to 'set productive'")
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
 
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " AS c_entityType,"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " AS c_classId,"
   Print #fileNo, addTab(3); "A."; g_anAcmIsPriceRelated; " AS c_isPriceRelated,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " AS c_entityId,"
   Print #fileNo, addTab(3); "A."; g_anAcmCondenseData; " AS c_condenseData,"
   Print #fileNo, addTab(3); "(CASE WHEN A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "' AND A."; g_anAcmEntityId; " = A."; g_anAhCid; " THEN 1 ELSE 0 END) AS c_isAggHead,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityShortName; " || '_OID' AS c_fkName,"
   Print #fileNo, addTab(3); "COALESCE(NP2DIV.RELSHORTNAME || NP2DIV.DIRRELSHORTNAME || '_OID', (CASE WHEN A.AHCLASSID = '05006' AND A.ENTITYID <> A.AHCLASSID AND A.ISPS=0 THEN 'DIV_OID' ELSE cast (NULL as varchar(20)) END)) AS c_fkNameDiv,"
   Print #fileNo, addTab(3); "A."; g_anAcmIsPs; " AS c_isPs,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " AS c_isNl,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " AS c_isGen,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName,"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmTableName; " AS c_parTableName,"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmTableName; " AS c_parParTableName"
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

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPAR."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPAR."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPAR."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPAR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPAR."; g_anLdmIsGen; " <= L."; g_anLdmIsGen; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(LPAR."; g_anLdmIsNl; " = 0 AND L."; g_anLdmIsNl; " = 0 AND LPAR."; g_anLdmIsGen; " <> L."; g_anLdmIsGen; ")"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(LPAR."; g_anLdmIsNl; " = 0 AND L."; g_anLdmIsNl; " = 1 AND LPAR."; g_anLdmIsGen; " = L."; g_anLdmIsGen; ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmLdmFkSchemaName; " = LPAR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmLdmFkTableName; " = LPAR."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anOrganizationId; " = P."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anPoolTypeId; " = P."; g_anPoolTypeId
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPARPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPARPAR."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPARPAR."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPARPAR."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsGen; " < LPAR."; g_anLdmIsGen; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPARPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmLdmFkSchemaName; " = LPARPAR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmLdmFkTableName; " = LPARPAR."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anOrganizationId; " = P."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPoolTypeId; " = P."; g_anPoolTypeId

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anAcmEntityName; " AS RELNAME,"
   Print #fileNo, addTab(5); g_anAcmEntityShortName; " AS RELSHORTNAME,"
   Print #fileNo, addTab(5); g_anAcmLeftEntityName; " AS REFENTITYNAME,"
   Print #fileNo, addTab(5); g_anAcmLrShortName; " AS DIRRELSHORTNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_anAcmRightEntityName; " = '"; UCase(clnDivision); "'"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anAcmEntityName; " AS RELNAME,"
   Print #fileNo, addTab(5); g_anAcmEntityShortName; " AS RELSHORTNAME,"
   Print #fileNo, addTab(5); g_anAcmRightEntityName; " AS REFENTITYNAME,"
   Print #fileNo, addTab(5); g_anAcmRlShortName; " AS DIRRELSHORTNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_anAcmLeftEntityName; " = '"; UCase(clnDivision); "'"
   Print #fileNo, addTab(3); ") NP2DIV"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "NP2DIV.REFENTITYNAME = A."; g_anAcmEntityName

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsCto; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsCtp; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(srcPoolIndex, ddlType)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(A."; g_anAcmCondenseData; " = 0 OR (A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "' AND A."; g_anAcmEntityId; " = A."; g_anAhCid; "))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(lrtOId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "((A."; g_anAcmCondenseData; " = 1 OR A."; g_anAcmIsPriceRelated; " = 1) AND L."; g_anLdmIsNl; " = 0)"
   Print #fileNo, addTab(3); ")"

   Print #fileNo, addTab(2); "ORDER BY L.FKSEQUENCENO ASC"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "IF c_condenseData = 1 THEN"
   genProcSectionHeader(fileNo, "just insert a 'dummy-OID'", 3, True)
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'INSERT INTO "; gc_tempTabNameSpAffectedEntities; "(orParEntityType,orParEntityId,isNl,isGen,oid,opId) VALUES (' ||"
   Print #fileNo, addTab(4); "'''' || c_entityType || ''',' ||"
   Print #fileNo, addTab(4); "'''' || c_entityId || ''',' ||"
   Print #fileNo, addTab(4); "CAST(c_isNl AS CHAR(1)) || ',' ||"
   Print #fileNo, addTab(4); "CAST(c_isGen AS CHAR(1)) || ',' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'"; CStr(lrtStatusCreated); ")';"
   Print #fileNo, addTab(2); "ELSE"

   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'INSERT INTO "; gc_tempTabNameSpAffectedEntities; "(orParEntityType,orParEntityId,isNl,isGen,oid,opId) ' ||"
   Print #fileNo, addTab(4); "'SELECT ' ||"
   Print #fileNo, addTab(4); "'''' || c_entityType || ''',' ||"
   Print #fileNo, addTab(4); "'''' || c_entityId || ''',' ||"
   Print #fileNo, addTab(4); "CAST(c_isNl AS CHAR(1)) || ',' ||"
   Print #fileNo, addTab(4); "CAST(c_isGen AS CHAR(1)) || ',' ||"
   Print #fileNo, addTab(4); "'T."; g_anOid; ",' ||"
   Print #fileNo, addTab(4); "'(CASE WHEN T."; g_anHasBeenSetProductive; "="; gc_dbFalse; " AND T."; g_anIsDeleted; "="; gc_dbFalse; " THEN "; CStr(lrtStatusCreated); " "; _
                             "WHEN T."; g_anHasBeenSetProductive; "="; gc_dbTrue; " AND T."; g_anIsDeleted; "="; gc_dbFalse; " THEN "; CStr(lrtStatusUpdated); " "; _
                             "WHEN T."; g_anHasBeenSetProductive; "="; gc_dbTrue; " AND T."; g_anIsDeleted; "="; gc_dbTrue; " THEN "; CStr(lrtStatusDeleted); " "; _
                             "ELSE CAST(NULL AS "; g_dbtEnumId; ") END) ' ||"
   Print #fileNo, addTab(4); "'FROM ' || c_schemaName || '.' || c_tableName || ' T ' ||"
   Print #fileNo, addTab(4); "'WHERE ' ||"

   Print #fileNo, addTab(4); "("

   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(5); "WHEN lrtOid_in IS NULL THEN 'T."; g_anStatus; " = "; CStr(statusReadyToBeSetProductive); "'"
   Print #fileNo, addTab(5); "ELSE 'EXISTS (SELECT 1 FROM "; gc_tempTabNameSpFilteredEntities; " F WHERE F.priceOid = T."; g_anAhOid; ")'"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"

   Print #fileNo, addTab(4); "("

   Print #fileNo, addTab(5); "CASE c_isPs"
   Print #fileNo, addTab(5); "WHEN 0"
   Print #fileNo, addTab(5); "THEN"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "CASE"
   Print #fileNo, addTab(7); "WHEN c_fkNameDiv IS NULL THEN ''"

   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 0) AND (c_isGen = 0)) THEN "; _
                                      "' AND T.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'"
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1) AND (c_isGen = 1)) THEN "; _
                                      "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P.' || c_fkName || ' = PP."; g_anOid; " AND PP.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'"
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1)  OR (c_isGen = 1)) THEN "; _
                                      "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'"

   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(6); ")"

   Print #fileNo, addTab(5); "ELSE  ' AND ' ||"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "CASE"
   Print #fileNo, addTab(7); "WHEN (c_isNl = 0) AND (c_isGen = 0) THEN 'T."; g_anPsOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20)))"
   Print #fileNo, addTab(7); "WHEN (c_isNl = 1) AND (c_isGen = 1) THEN 'EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P.' || c_fkName || ' = PP."; g_anOid; " AND PP."; g_anPsOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'"
   Print #fileNo, addTab(7); "WHEN (c_isNl = 1)  OR (c_isGen = 1) THEN 'EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P."; g_anPsOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'"
   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"

   genProcSectionHeader(fileNo, "filter out calculated prices", 4, True)
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE WHEN (c_isPriceRelated = 1) AND (c_classId = '"; getClassIdByClassIndex(g_classIndexGenericAspect); "') THEN"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "CASE"
   Print #fileNo, addTab(7); "WHEN c_isAggHead=1 AND c_isNl=0 THEN ' AND ((T."; g_anCid; " NOT IN ("; Replace(priceAssignmentClassIdList, "'", "''"); ")) OR (COALESCE((SELECT PRT.ID FROM "; qualTabNameProperty; " PRP INNER JOIN "; g_qualTabNamePropertyTemplate; " PRT ON PRP.PTMHTP_OID = PRT."; g_anOid; " WHERE T.PRPAPR_OID = PRP."; g_anOid; "), -1) NOT IN ("; propertyTemplateIdListCalcPrice; ")))'"
   Print #fileNo, addTab(7); "ELSE ' AND ((T."; g_anAhCid; " NOT IN ("; Replace(priceAssignmentClassIdList, "'", "''"); ")) OR (COALESCE((SELECT PRT.ID FROM "; qualTabNameGenericAspect; " GA INNER JOIN "; qualTabNameProperty; " PRP ON GA.PRPAPR_OID = PRP."; g_anOid; " INNER JOIN "; g_qualTabNamePropertyTemplate; " PRT ON PRP.PTMHTP_OID = PRT."; g_anOid; " WHERE T."; g_anAhOid; " = GA."; g_anOid; "), -1) NOT IN ("; propertyTemplateIdListCalcPrice; ")))'"
   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(5); "ELSE ''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"

   genProcSectionHeader(fileNo, "filter out typespecs with references to non-productive TypePriceAssignments", 4, True)
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE WHEN (c_entityType = 'C' AND c_entityId = '"; getClassIdByClassIndex(g_classIndexTypeSpec); "') THEN"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "' AND (T.TSTTPA_OID IS NULL' ||"
   Print #fileNo, addTab(8); "' OR T.TSTTPA_OID IN (SELECT TPA.OID FROM "; qualTabNameGenericAspect; " TPA WHERE TPA.STATUS_ID = 5)' ||"
   Print #fileNo, addTab(8); "' OR T.TSTTPA_OID IN (SELECT OID FROM "; gc_tempTabNameSpAffectedEntities; "))'"
 '  Print #fileNo, addTab(7); "' AND (T.PTYPTY_OID IS NULL OR T.PTYPTY_OID IN (SELECT TS.OID FROM "; qualTabNameTypeSpec; " TS WHERE TS.STATUS_ID IN (4,5)))'"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(5); "ELSE ''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"

   genProcSectionHeader(fileNo, "filter out typestandardequipments with references to non-productive TypeSpecs", 4, True)
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE WHEN (c_entityType = 'C' AND c_entityId = '"; getClassIdByClassIndex(g_classIndexTypeStandardEquipment); "') THEN"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "' AND (T.TSETYS_OID IN (SELECT TS.OID FROM "; qualTabNameTypeSpec; " TS WHERE TS.STATUS_ID = 5)' ||"
   Print #fileNo, addTab(8); "' OR (T.TSETYS_OID IN (SELECT OID FROM "; gc_tempTabNameSpAffectedEntities; ")))'"
   'Print #fileNo, addTab(7); "' AND (TS.PTYPTY_OID IS NULL OR TS.PTYPTY_OID IN (SELECT PREV.OID FROM "; qualTabNameTypeSpec; " PREV WHERE PREV.STATUS_ID IN (4,5))))'"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(5); "ELSE ''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"

   Print #fileNo, addTab(4); "' WITH UR';"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader(fileNo, "count the number of affected rows and tables", 2)
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_rowCount > 0 THEN"
   Print #fileNo, addTab(3); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit(fileNo, qualProcedureNameSpGetAffectedEntities, ddlType, , "psOid_in", "lrtOid_in", "tabCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for preprocessing Setting Data Productive GENERICASPECT
   ' ####################################################################################################################

   If thisOrgIndex <> g_primaryOrgIndex Then

     Dim qualProcedureNameSetProdPre As String
     qualProcedureNameSetProdPre = genQualProcName(g_sectionIndexAspect, spnSetProductivePreProcess, ddlType, thisOrgIndex, srcPoolIndex, , UCase(clnGenericAspect))

     printSectionHeader("SP for preprocessing Setting Data Productive '" & qualTabNameGenericAspectDst & "'", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameSetProdPre
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
     genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - if specified set only prices productive related to this LRT")
     genProcParm(fileNo, "IN", "opId_in", g_dbtEnumId, False, "identifies the operation (insert, update, delete, gen NL-Text for ChangeLog) to set productive")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF ( opId_in = " & CStr(lrtStatusDeleted) & " ) THEN"
     genProcSectionHeader(fileNo, "CCPCCP_OID reference set NULL, if the central record has been deleted", 2, True)
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualTabNameGenericAspectDst; " AS gas"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(2); "gas.ccpccp_oid = NULL"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "gas.ccpccp_oid IS NOT NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "gas."; g_anIsNational; " = "; gc_dbTrue
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "gas.classid IN ( '09031', '09033')"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "gas.ps_oid = psOid_in"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "EXISTS ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "'1'"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualTabNameGenericAspectSrc; " AS gas_ne"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(4); "gas.ccpccp_oid = gas_ne.oid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "gas_ne."; g_anIsNational; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "gas_ne.classid IN ( '09031', '09033')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "gas_ne.ps_oid = psOid_in"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "gas_ne."; g_anIsDeleted; " = "; gc_dbTrue
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo,
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

   End If

   ' ####################################################################################################################
   ' #   SP for determining if division data is set productive
   ' ####################################################################################################################

   Dim qualProcedureName As String
 
   qualProcedureName = _
     genQualProcName(g_sectionIndexAliasLrt, spnSetProductiveIncludesDivisionData, ddlType, thisOrgIndex, srcPoolIndex)
 
   printSectionHeader("SP for determining if division data is set productive", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
   genProcParm(fileNo, "OUT", "result_out", g_dbtBoolean, False, "0 = false, 1 = true")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
   Print #fileNo, addTab(2); "DECLARE objCursor CURSOR FOR v_stmnt;"
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcedureName, ddlType, , "psOid_in", "result_out")
 
   genProcSectionHeader(fileNo, "initialize output parameters")
   Print #fileNo, addTab(1); "SET result_out = 0;"

   genProcSectionHeader(fileNo, "loop over tables related to 'set productive'")
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
 
   Print #fileNo, addTab(3); "A."; g_anAcmEntityShortName; " || '_OID' AS c_fkName,"
   Print #fileNo, addTab(3); "A."; g_anAhCid; " AS c_ahclassId,"
   Print #fileNo, addTab(3); "NP2DIV.RELSHORTNAME || NP2DIV.DIRRELSHORTNAME || '_OID' AS c_fkNameDiv,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " AS c_isNl,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " AS c_isGen,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName,"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmTableName; " AS c_parTableName,"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmTableName; " AS c_parParTableName"
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

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPAR."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPAR."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPAR."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPAR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPAR."; g_anLdmIsGen; " <= L."; g_anLdmIsGen; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(LPAR."; g_anLdmIsNl; " = 0 AND L."; g_anLdmIsNl; " = 0 AND LPAR."; g_anLdmIsGen; " <> L."; g_anLdmIsGen; ")"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(LPAR."; g_anLdmIsNl; " = 0 AND L."; g_anLdmIsNl; " = 1 AND LPAR."; g_anLdmIsGen; " = L."; g_anLdmIsGen; ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmLdmFkSchemaName; " = LPAR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anPdmLdmFkTableName; " = LPAR."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anOrganizationId; " = P."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPAR."; g_anPoolTypeId; " = P."; g_anPoolTypeId
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPARPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPARPAR."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPARPAR."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPARPAR."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsGen; " < LPAR."; g_anLdmIsGen; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPARPAR."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPARPAR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmLdmFkSchemaName; " = LPARPAR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPdmLdmFkTableName; " = LPARPAR."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anOrganizationId; " = P."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPARPAR."; g_anPoolTypeId; " = P."; g_anPoolTypeId

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anAcmEntityName; " AS RELNAME,"
   Print #fileNo, addTab(5); g_anAcmEntityShortName; " AS RELSHORTNAME,"
   Print #fileNo, addTab(5); g_anAcmLeftEntityName; " AS REFENTITYNAME,"
   Print #fileNo, addTab(5); g_anAcmLrShortName; " AS DIRRELSHORTNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_anAcmRightEntityName; " = '"; UCase(clnDivision); "'"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anAcmEntityName; " AS RELNAME,"
   Print #fileNo, addTab(5); g_anAcmEntityShortName; " AS RELSHORTNAME,"
   Print #fileNo, addTab(5); g_anAcmRightEntityName; " AS REFENTITYNAME,"
   Print #fileNo, addTab(5); g_anAcmRlShortName; " AS DIRRELSHORTNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_anAcmLeftEntityName; " = '"; UCase(clnDivision); "'"
   Print #fileNo, addTab(3); ") NP2DIV"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "NP2DIV.REFENTITYNAME = A."; g_anAcmEntityName

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsCto; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsCtp; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "A.ISPS = "; gc_dbFalse
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "A.ISPSFORMING = "; gc_dbFalse
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(srcPoolIndex, ddlType)
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "(A."; g_anAcmCondenseData; " = 0 OR (A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "' AND A."; g_anAcmEntityId; " = A."; g_anAhCid; "))"

   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'SELECT COUNT(*) ' ||"
   Print #fileNo, addTab(4); "'FROM ' || c_schemaName || '.' || c_tableName || ' T ' ||"
   Print #fileNo, addTab(4); "'WHERE ' ||"
   Print #fileNo, addTab(5); "'T."; g_anStatus; " = "; CStr(statusReadyToBeSetProductive); "'"
   Print #fileNo, addTab(4); " || ("

   Print #fileNo, addTab(7); "CASE"
   'special logic for CodeValidForOrganization and EndNodeHasGnericCode, which have same schema as GenericCode
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NULL AND c_ahclassid = '05006') THEN "; _
                                 "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.GENERICCODE P WHERE T.GCO_OID = P."; g_anOid; " AND P.CDIDIV_OID = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'"
   Print #fileNo, addTab(7); "WHEN c_fkNameDiv IS NULL THEN ''"
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 0) AND (c_isGen = 0)) THEN "; _
                                      "' AND T.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'"
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1) AND (c_isGen = 1)) THEN "; _
                                      "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P.' || c_fkName || ' = PP."; g_anOid; " AND PP.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'"
   Print #fileNo, addTab(7); "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1)  OR (c_isGen = 1)) THEN "; _
                                      "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P."; g_anOid; " AND P.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'"

   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(6); ")"

   Print #fileNo, addTab(4); " || ' WITH UR';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN objCursor;"
   Print #fileNo, addTab(2); "FETCH objCursor INTO v_rowCount;"
   Print #fileNo, addTab(2); "CLOSE objCursor;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_rowCount > 0 THEN"
   Print #fileNo, addTab(3); "SET result_out = 1;"
   Print #fileNo, addTab(3); "RETURN;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit(fileNo, qualProcedureName, ddlType, , "psOid_in", "result_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   '------------------------------------------------------------------------------------------------


   Dim callGenWorkSpace As Boolean
   Dim simulate As Boolean
   Dim procNameSuffix As String
   Dim p As Integer
   For p = 2 To IIf(supportSimulationSps, 3, 2)
     ' we currently do not support SETPRODUCTIVE without GEN_WORKSPACE
     ' to support this start loop with 'p = 1'
     callGenWorkSpace = (p = 2)
     simulate = (p = 3)
     procNameSuffix = IIf(simulate, "sim", "")
     ' ####################################################################################################################
     ' #    SP for Setting Data Productive
     ' ####################################################################################################################

     Dim qualProcedureNameSetProdInt As String
     Dim qualProcedureNameSetProd As String

     qualProcedureNameSetProd = _
       genQualProcName( _
         g_sectionIndexAliasLrt, spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, , procNameSuffix _
       )
     qualProcedureNameSetProdInt = _
       genQualProcName( _
         g_sectionIndexProductStructure, spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, , procNameSuffix _
       )
     printSectionHeader("SP for Setting Data Productive (internal)", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameSetProdInt
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
     genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - if specified set only prices productive related to this LRT")
     If callGenWorkSpace Then
       genProcParm(fileNo, "IN", "isAdHoc_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse & " and isGenWsAs_in = " & gc_dbTrue)
       genProcParm(fileNo, "IN", "isGenWsAs_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse & " and isGenWsAs_in = " & gc_dbTrue)
     End If

     If simulate Then
       genProcParm(fileNo, "OUT", "refId_out", "INTEGER", True, "ID used to identify persisted records related to this procedure call")
     End If

     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected")

     If callGenWorkSpace Then
       genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
       genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
       genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
     End If

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare conditions")
     genCondDecl(fileNo, "alreadyExist", "42710")

     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_tabCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_opType", "INTEGER", CStr(lrtStatusCreated))
     genVarDecl(fileNo, "v_setProductiveTs", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_isUnderConstruction", g_dbtBoolean, gc_dbFalse)
     If callGenWorkSpace Then
       genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
     End If

     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")

     genProcSectionHeader(fileNo, "declare continue handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempTablesSp(fileNo, , , True)
     genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, , , , True)

     If callGenWorkSpace Then
       genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       If simulate Then
         genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "refId_out", "rowCount_out")
       Else
         genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out")
       End If
     End If

     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET rowCount_out      = 0;"
     Print #fileNo, addTab(1); "SET v_setProductiveTs = CURRENT TIMESTAMP;"

     If simulate Then
       Print #fileNo, addTab(1); "SET refId_out         = NULL;"
     End If

     If callGenWorkSpace Then
       Print #fileNo, addTab(1); "SET gwspError_out     = NULL;"
       Print #fileNo, addTab(1); "SET gwspInfo_out      = NULL;"
       Print #fileNo, addTab(1); "SET gwspWarning_out   = NULL;"
     End If

     If simulate Then
       genProcSectionHeader(fileNo, "set savepoint to rollback to")
       Print #fileNo, addTab(1); "SAVEPOINT simulateReset UNIQUE ON ROLLBACK RETAIN CURSORS;"
     End If

     genProcSectionHeader(fileNo, "verify that Product Structure is not 'under construction'")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); g_anIsUnderConstruction
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_isUnderConstruction"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = psOid_in"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF NOT (v_isUnderConstruction = 0) THEN"
     If callGenWorkSpace Then
       genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       If simulate Then
         genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "refId_out", "rowCount_out")
       Else
         genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "rowCount_out")
       End If
     End If
     genSignalDdlWithParms("setProdUndConstr", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(psOid_in))")
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "determine OIDs affected by 'Set Productive'", 1)
     Print #fileNo, addTab(1); "CALL "; qualProcedureNameSpGetAffectedEntities; "(psOid_in, lrtOid_in, v_tabCount, v_rowCount);"
     Print #fileNo, addTab(1); "SET v_rowCount = 0;"

     genProcSectionHeader(fileNo, "handle all 'DELETE', 'INSERT', 'UPDATE' and 'GEN-NL-CHANGELOG'", 1)
     Print #fileNo, addTab(1); "SET v_opType = "; CStr(lrtStatusDeleted); ";"
     Print #fileNo, addTab(1); "WHILE v_opType IS NOT NULL DO"
     Print #fileNo, addTab(2); "FOR tabLoop AS"
     Print #fileNo, addTab(3); "WITH"
     Print #fileNo, addTab(4); "V_SpAffectedEntity"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "entityType,"
     Print #fileNo, addTab(4); "entityId,"
     Print #fileNo, addTab(4); "isNl,"
     Print #fileNo, addTab(4); "isGen"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(3); "AS"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT DISTINCT"
     Print #fileNo, addTab(5); "orParEntityType,"
     Print #fileNo, addTab(5); "orParEntityId,"
     Print #fileNo, addTab(5); "isNl,"
     Print #fileNo, addTab(5); "isGen"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameSpAffectedEntities
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "(v_opType = 0)"
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "(opId = v_opType)"
     Print #fileNo, addTab(3); "),"
     Print #fileNo, addTab(4); "V_SpAffectedTab"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "tableName,"
     Print #fileNo, addTab(4); "schemaName,"
     Print #fileNo, addTab(4); "seqNo"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(3); "AS"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT DISTINCT"
     Print #fileNo, addTab(5); "V."; g_anPdmTableName; ","
     Print #fileNo, addTab(5); "V.SOURCE_SCHEMANAME,"
     Print #fileNo, addTab(5); "V.SEQNO"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualViewNameAffectedPdmTabGlob; " V"
     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); "V_SpAffectedEntity E"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "V."; g_anAcmEntityType; " = E.entityType"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "V."; g_anAcmEntityId; " = E.entityId"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "(E.isNl = 1 OR V."; g_anLdmIsNl; " = E.isNl)"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "V."; g_anLdmIsGen; " = E.isGen"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "V."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "V."; g_anPoolTypeId; " = "; genPoolId(srcPoolIndex, ddlType)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "("
     Print #fileNo, addTab(6); "V."; g_anAcmEntityType; " <> '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(7); "OR"
     Print #fileNo, addTab(6); "V."; g_anAcmCondenseData; " = "; gc_dbFalse
     Print #fileNo, addTab(7); "OR"
     Print #fileNo, addTab(6); "V."; g_anAhCid; " = V."; g_anAcmEntityId
     Print #fileNo, addTab(5); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "V.tableName  AS c_tableName,"
     Print #fileNo, addTab(4); "V.schemaName AS c_schemaName"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "V_SpAffectedTab V"
     Print #fileNo, addTab(3); "ORDER BY"
     genProcSectionHeader(fileNo, "sequence of tables processed must be inverted for 'DELETE'", 4, True)
     Print #fileNo, addTab(4); "(CASE WHEN v_opType = "; CStr(lrtStatusDeleted); " THEN -1 ELSE 1 END) * V.seqNo ASC"
     Print #fileNo, addTab(3); "WITH UR"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     If thisOrgIndex <> g_primaryOrgIndex Then
       Print #fileNo,
       genProcSectionHeader(fileNo, "preprocessing Setting Data Productive GENERICASPECT", 4, True)
       Print #fileNo, addTab(3); "IF c_tableName = '"; UCase(clnGenericAspect); "' AND v_opType = "; CStr(lrtStatusDeleted); " THEN"
       Print #fileNo, addTab(4); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.SETPRODUCTIVEPREPROC_' || c_tableName || '( ?, ?, ? )' ;"
       Print #fileNo, addTab(4); ""
       Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo, addTab(0); ""
       Print #fileNo, addTab(4); "EXECUTE"
       Print #fileNo, addTab(5); "v_stmnt"
       Print #fileNo, addTab(4); "USING"
       Print #fileNo, addTab(5); "psOid_in,"
       Print #fileNo, addTab(5); "lrtOid_in,"
       Print #fileNo, addTab(5); "v_opType"
       Print #fileNo, addTab(4); ";"
       Print #fileNo, addTab(3); "END IF;"
       Print #fileNo,
     End If
 
     Print #fileNo, addTab(3); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.SETPRODUCTIVE_' || c_tableName || '(?,?,?,?,?)' ;"
     Print #fileNo,
     Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE"
     Print #fileNo, addTab(4); "v_stmnt"
     Print #fileNo, addTab(3); "INTO"
     Print #fileNo, addTab(4); "v_rowCount"
     Print #fileNo, addTab(3); "USING"
     Print #fileNo, addTab(4); "psOid_in,"
     Print #fileNo, addTab(4); "lrtOid_in,"
     Print #fileNo, addTab(4); "v_opType,"
     Print #fileNo, addTab(4); "v_setProductiveTs"
     Print #fileNo, addTab(3); ";"
     Print #fileNo,
     Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);"
     Print #fileNo, addTab(2); "END FOR;"

     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_opType = (CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN "; CStr(lrtStatusCreated); " WHEN "; CStr(lrtStatusCreated); " THEN "; CStr(lrtStatusUpdated); " WHEN "; CStr(lrtStatusUpdated); " THEN "; CStr(lrtStatusLocked); " ELSE NULL END);"

     Print #fileNo, addTab(1); "END WHILE;"

     If Not simulate Then
       Print #fileNo,
       Print #fileNo, addTab(1); "IF rowCount_out > 0 THEN"

       genPersistChangeLogDdl(fileNo, g_classIndexChangeLog, qualTabNameChangeLog, gc_tempTabNameChangeLog, _
                              qualTabNameChangeLogNl, gc_tempTabNameChangeLogNl, qualSeqNameOid, ddlType, thisOrgIndex, dstPoolIndex, 2, eclSetProd, , , True, "v_setProductiveTs")
     End If

     If callGenWorkSpace Then
       genProcSectionHeader(fileNo, "determine OID of Organization", 2)

       Print #fileNo, addTab(2); "SET v_orgOid = (SELECT ORGOID FROM "; g_qualTabNamePdmOrganization; " WHERE ID = "; genOrgId(thisOrgIndex, ddlType, True); ");"

       Print #fileNo, addTab(2); "IF isAdHoc_in = "; gc_dbFalse; " AND isGenWsAs_in = "; gc_dbTrue; " THEN"
       genCallGenWorkspaceDdl(fileNo, thisOrgIndex, srcPoolIndex, "v_orgOid", "psOid_in", g_pools.descriptors(srcPoolIndex).id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", 3, ddlType, True)
       Print #fileNo, addTab(2); "END IF;"

       genCallGenWorkspaceDdl(fileNo, thisOrgIndex, dstPoolIndex, "v_orgOid", "psOid_in", g_pools.descriptors(dstPoolIndex).id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", 2, ddlType)
     End If

     If Not simulate Then
       genProcSectionHeader(fileNo, "mark records in work data pool as 'being productive' and delete records marked as 'deleted'", 2)

       Print #fileNo, addTab(2); "SET v_opType = "; CStr(lrtStatusLocked); ";"
       Print #fileNo, addTab(2); "WHILE v_opType <> "; CStr(lrtStatusDeleted); " DO"

       genProcSectionHeader(fileNo, "1st loop: INSERT, 2nd loop: UPDATE, 3rd loop: DELETE", 3, True)
       Print #fileNo, addTab(3); "SET v_opType = (CASE v_opType WHEN 0 THEN "; CStr(lrtStatusCreated); " WHEN "; CStr(lrtStatusCreated); " THEN "; CStr(lrtStatusUpdated); " ELSE "; CStr(lrtStatusDeleted); " END);"

       Print #fileNo,
       Print #fileNo, addTab(3); "FOR tabLoop AS"
       Print #fileNo, addTab(4); "WITH"
       Print #fileNo, addTab(5); "V_SpAffectedEntity"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "entityType,"
       Print #fileNo, addTab(5); "entityId,"
       Print #fileNo, addTab(5); "isNl,"
       Print #fileNo, addTab(5); "isGen"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(4); "AS"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "SELECT DISTINCT"
       Print #fileNo, addTab(6); "orParEntityType,"
       Print #fileNo, addTab(6); "orParEntityId,"
       Print #fileNo, addTab(6); "isNl,"
       Print #fileNo, addTab(6); "isGen"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); gc_tempTabNameSpAffectedEntities
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "opId = v_opType"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(4); "SELECT DISTINCT"
       Print #fileNo, addTab(5); "V."; g_anPdmTableName; " AS c_tableName,"
       Print #fileNo, addTab(5); "V.SOURCE_SCHEMANAME AS c_schemaName,"
       Print #fileNo, addTab(5); "(CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN - V.SEQNO ELSE V.SEQNO END) AS c_seqNo"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); qualViewNameAffectedPdmTabGlob; " V"
       Print #fileNo, addTab(4); "INNER JOIN"
       Print #fileNo, addTab(5); "V_SpAffectedEntity E"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "V."; g_anAcmEntityType; " = E.entityType"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "V."; g_anAcmEntityId; " = E.entityId"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "(E.isNl = 1 OR V."; g_anLdmIsNl; " = E.isNl)"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "V."; g_anLdmIsGen; " = E.isGen"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "V."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "V."; g_anPoolTypeId; " = "; genPoolId(srcPoolIndex, ddlType)
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "("
       Print #fileNo, addTab(6); "(V."; g_anAcmCondenseData; " = 0)"
       Print #fileNo, addTab(7); "OR"
       Print #fileNo, addTab(6); "(V."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "' AND V."; g_anAhCid; " = V."; g_anAcmEntityId; ")"
       Print #fileNo, addTab(5); ")"
       Print #fileNo, addTab(4); "ORDER BY"
       Print #fileNo, addTab(5); "(CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN - V.SEQNO ELSE V.SEQNO END) ASC"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(4); "FOR READ ONLY"

       Print #fileNo, addTab(3); "DO"
       Print #fileNo, addTab(4); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnSetProductivePostProcess); "_' || c_tableName || '(?,?,?)' ;"
       Print #fileNo,
       Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,

       Print #fileNo, addTab(4); "EXECUTE"
       Print #fileNo, addTab(5); "v_stmnt"
       Print #fileNo, addTab(4); "USING"
       Print #fileNo, addTab(5); "psOid_in,"
       Print #fileNo, addTab(5); "lrtOid_in,"
       Print #fileNo, addTab(5); "v_opType"
       Print #fileNo, addTab(4); ";"
       Print #fileNo, addTab(3); "END FOR;"
       Print #fileNo, addTab(2); "END WHILE;"

       Print #fileNo, addTab(1); "END IF;"
     End If

     If callGenWorkSpace Then
       genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       If simulate Then
         genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "refId_out", "rowCount_out")
       Else
         genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out")
       End If
     End If

     If simulate Then
       genProcSectionHeader(fileNo, "rollback to savepoint")
       Print #fileNo, addTab(1); "ROLLBACK TO SAVEPOINT simulateReset;"

       genProcSectionHeader(fileNo, "persist content of temporay tables")

       Dim qualProcNameTracePersist As String
       qualProcNameTracePersist = genQualProcName(g_sectionIndexTrace, spnTracePersist, ddlType, thisOrgIndex, srcPoolIndex)

       Print #fileNo, addTab(1); "CALL "; qualProcNameTracePersist; "(refId_out, v_rowCount, v_tabCount);"

       genProcSectionHeader(fileNo, "release savepoint")
       Print #fileNo, addTab(1); "RELEASE SAVEPOINT simulateReset;"
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     Dim genCallSwitchParam As Boolean
     Dim genCallSwitchParam2 As Boolean
     Dim j As Long
     For j = IIf(simulate, 55555, 1) To IIf(callGenWorkSpace, 3, 1)
       genCallSwitchParam = (j = 2)
       genCallSwitchParam2 = (j = 3)

       ' ####################################################################################################################
       ' #    SP for Setting Data Productive
       ' ####################################################################################################################

       qualProcedureNameSetProd = genQualProcName(g_sectionIndexAliasLrt, spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex)

       printSectionHeader("SP for Setting Data Productive", fileNo)
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcedureNameSetProd
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
       If genCallSwitchParam Or genCallSwitchParam2 Then
         genProcParm(fileNo, "IN", "isAdHoc_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse)
       End If
       If genCallSwitchParam2 Then
         genProcParm(fileNo, "IN", "isGenWsAs_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse & " and isGenWsAs_in = " & gc_dbTrue)
       End If
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected")

       If callGenWorkSpace Then
         genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
         genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
         genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
       End If

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genProcSectionHeader(fileNo, "declare variables", , True)
       genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
       genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
       genSpLogDecl(fileNo)

       genProcSectionHeader(fileNo, "declare statement")
       genVarDecl(fileNo, "v_stmnt", "STATEMENT")

       If callGenWorkSpace Then
         If genCallSwitchParam Then
           genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
       Else
         genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "rowCount_out")
       End If

       genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, srcPoolIndex, tvNull, 1)

       Print #fileNo,
       If callGenWorkSpace Then
         If genCallSwitchParam2 Then
           Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,?,?,?,?,?,?)';"
         Else
           Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,"; IIf(genCallSwitchParam, "?,1,", "1,1,"); "?,?,?,?)';"
         End If
       Else
         Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,?)';"
       End If
       Print #fileNo,
       Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(1); "EXECUTE"
       Print #fileNo, addTab(2); "v_stmnt"
       Print #fileNo, addTab(1); "INTO"
       If callGenWorkSpace Then
         Print #fileNo, addTab(2); "rowCount_out,"
         Print #fileNo, addTab(2); "gwspError_out,"
         Print #fileNo, addTab(2); "gwspInfo_out,"
         Print #fileNo, addTab(2); "gwspWarning_out"
       Else
         Print #fileNo, addTab(2); "rowCount_out"
       End If
       Print #fileNo, addTab(1); "USING"
       Print #fileNo, addTab(2); "psOid_in,"
       If genCallSwitchParam2 Then
         Print #fileNo, addTab(2); "v_lrtOid,"
         Print #fileNo, addTab(2); "isAdHoc_in,"
         Print #fileNo, addTab(2); "isGenWsAs_in"
       ElseIf genCallSwitchParam Then
         Print #fileNo, addTab(2); "v_lrtOid,"
         Print #fileNo, addTab(2); "isAdHoc_in"
       Else
         Print #fileNo, addTab(2); "v_lrtOid"
       End If
       Print #fileNo, addTab(1); ";"

       If callGenWorkSpace Then
         If genCallSwitchParam Then
           genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
       Else
         genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "rowCount_out")
       End If

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim

       ' ####################################################################################################################
       ' #    SP for Setting Prices Productive
       ' ####################################################################################################################

       qualProcedureNameSetProd = genQualProcName(g_sectionIndexAliasLrt, spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, , "Prices")

       printSectionHeader("SP for Setting Prices Productive", fileNo)
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcedureNameSetProd
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - set only prices productive related to this LRT")
       If genCallSwitchParam Or genCallSwitchParam2 Then
         genProcParm(fileNo, "IN", "isAdHoc_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse)
       End If
       If genCallSwitchParam2 Then
         genProcParm(fileNo, "IN", "isGenWsAs_in", g_dbtBoolean, True, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " & gc_dbFalse & " and isGenWsAs_in = " & gc_dbTrue)
       End If
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected")

       If callGenWorkSpace Then
         genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
         genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
         genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
       End If

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genProcSectionHeader(fileNo, "declare variables", , True)
       genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
       genSpLogDecl(fileNo)

       genProcSectionHeader(fileNo, "declare statement")
       genVarDecl(fileNo, "v_stmnt", "STATEMENT")

       If callGenWorkSpace Then
         If genCallSwitchParam Then
           genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
       Else
         genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out")
       End If

       genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, srcPoolIndex, tvNull, 1)

       genProcSectionHeader(fileNo, "call 'general procedure for SETPRODUCTIVE'")
       If callGenWorkSpace Then
         If genCallSwitchParam2 Then
           Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,?,?,?,?,?,?)';"
         Else
           Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,"; IIf(genCallSwitchParam, "?,1,", "1,1,"); "?,?,?,?)';"
         End If
       Else
         Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcedureNameSetProdInt; "(?,?,?)';"
       End If

       Print #fileNo,
       Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(1); "EXECUTE"
       Print #fileNo, addTab(2); "v_stmnt"
       Print #fileNo, addTab(1); "INTO"
       If callGenWorkSpace Then
         Print #fileNo, addTab(2); "rowCount_out,"
         Print #fileNo, addTab(2); "gwspError_out,"
         Print #fileNo, addTab(2); "gwspInfo_out,"
         Print #fileNo, addTab(2); "gwspWarning_out"
       Else
         Print #fileNo, addTab(2); "rowCount_out"
       End If
       Print #fileNo, addTab(1); "USING"
       Print #fileNo, addTab(2); "psOid_in,"
       If genCallSwitchParam2 Then
         Print #fileNo, addTab(2); "lrtOid_in,"
         Print #fileNo, addTab(2); "isAdHoc_in,"
         Print #fileNo, addTab(2); "isGenWsAs_in"
       ElseIf genCallSwitchParam Then
         Print #fileNo, addTab(2); "lrtOid_in,"
         Print #fileNo, addTab(2); "isAdHoc_in"
       Else
         Print #fileNo, addTab(2); "lrtOid_in"
       End If
       Print #fileNo, addTab(1); ";"

       If callGenWorkSpace Then
         If genCallSwitchParam Then
           genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
       Else
         genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, , "psOid_in", "lrtOid_in", "rowCount_out")
       End If

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     Next j
   Next p
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genSetProdSupportDdlByPoolForAllPools( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtLdm Or thisPoolIndex < 1 Then
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportAcm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   Dim sectionName As String
   Dim sectionNameShort As String
   Dim sectionIndex As Integer

   sectionName = snAliasLrt
   sectionNameShort = ssnAliasLrt
   sectionIndex = g_sectionIndexAliasLrt

   Dim i As Integer
   For i = 1 To IIf(supportFilteringByPsDpMapping, 2, 1)
     If i = 2 Then
       sectionIndex = g_sectionIndexAliasPsDpFiltered
       sectionName = snAliasPsDpFiltered
       sectionNameShort = ssnAliasPsDpFiltered
     End If

     ' ####################################################################################################################
     ' #    SP: Determine wether a LOCK is set on 'Set Productive'
     ' ####################################################################################################################

     Dim qualProcNameLockIsSetLocal As String
     Dim qualProcNameLockIsSetGlobal As String

     qualProcNameLockIsSetLocal = genQualProcName(sectionIndex, spnRel2ProdIsSet, ddlType, thisOrgIndex, thisPoolIndex)
     qualProcNameLockIsSetGlobal = genQualProcName(g_sectionIndexDbMeta, spnRel2ProdIsSet, ddlType)

     genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, , ddlType)
     genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_EXCLUSIVEWRITE_MODE", ddlType)
     genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_SHAREDWRITE_MODE", ddlType)
     genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_SHAREDREAD_MODE", ddlType)
 
     ' ####################################################################################################################
     ' #    'local' SPs to acquire LOCKs for 'Set Productive'
     ' ####################################################################################################################

     Dim qualProcNameLocal As String
     Dim qualProcNameGlobal As String
     qualProcNameLocal = genQualProcName(sectionIndex, spnSetRel2ProdLock, ddlType, thisOrgIndex, thisPoolIndex)
     qualProcNameGlobal = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType)

     'TODO(TF): remove these wrapper SPs as soon as application code is changed
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", , ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "GENWS", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "OTHER", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "OTHERS", ddlType)

     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "EXCLUSIVEWRITE", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDWRITE", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDREAD", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDREADS", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDWRITES", ddlType)

     ' ####################################################################################################################
     ' #    'local' SPs to release LOCKs for 'Set Productive'
     ' ####################################################################################################################

     qualProcNameLocal = genQualProcName(sectionIndex, spnResetRel2ProdLock, ddlType, thisOrgIndex, thisPoolIndex)
     qualProcNameGlobal = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType)

     'TODO(TF): remove these wrapper SPs as soon as application code is changed
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", , ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "GENWS", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "OTHER", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "OTHERS", ddlType)

     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "EXCLUSIVEWRITE", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "SHAREDWRITE", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "SHAREDREAD", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "SHAREDREADS", ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "SHAREDWRITES", ddlType)
 
     qualProcNameLocal = genQualProcName(sectionIndex, spnResetRel2ProdLocks, ddlType, thisOrgIndex, thisPoolIndex)
     qualProcNameGlobal = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLocks, ddlType)
     genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 0, thisOrgIndex, thisPoolIndex, "release", , ddlType, False)

     If thisOrgIndex = g_primaryOrgIndex And (thisPoolIndex = g_workDataPoolIndex) And i = 1 Then
       qualProcNameLocal = genQualProcName(g_sectionIndexAlias, spnResetRel2ProdLocks, ddlType)
       genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 0, -1, -1, "release", , ddlType, False)
     End If

   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 Private Sub genIsLockedDdl( _
   fileNo As Integer, _
   ByRef qualProcNameLocal As String, _
   ByRef qualProcNameGlobal As String, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional ByRef procNameSuffix As String = "", _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim suffix As String
   suffix = IIf(procNameSuffix = "", "", "_" & procNameSuffix)


     printSectionHeader("SP determinig whether a LOCK is set on 'Set Productive' for a given data pool" & procNameSuffix, fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameLocal; suffix
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "specifies the data pool to query the LOCK-status for")
     genProcParm(fileNo, "OUT", "isLocked_out", g_dbtBoolean, False, "specifies whether a LOCK is set (0=false, 1=true)")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genSpLogProcEnter(fileNo, qualProcNameLocal & suffix, ddlType, , "'dataPoolDescr_in", "isLocked_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "call 'global' procedure'", , True)
     Print #fileNo, addTab(1); "CALL "; qualProcNameGlobal; suffix; "(dataPoolDescr_in, isLocked_out);"

     genSpLogProcExit(fileNo, qualProcNameLocal & suffix, ddlType, , "'dataPoolDescr_in", "isLocked_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 End Sub
 
 Private Sub genSetLockDdl( _
   fileNo As Integer, _
   ByRef qualProcNameLocal As String, _
   ByRef qualProcNameGlobal As String, _
   numPoolDescrs As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional ByRef descr As String = "acquire", _
   Optional ByRef procNameSuffix As String = "", _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional includeLockContext As Boolean = True _
 )
   Dim suffix As String
   suffix = IIf(procNameSuffix = "", "", "_" & procNameSuffix)

   printSectionHeader("SP to " & descr & " locks for 'Set Productive'" & IIf(procNameSuffix = "", "", " (" & procNameSuffix & ")"), fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameLocal; suffix
   Print #fileNo, addTab(0); "("
   If numPoolDescrs = 1 Then
     genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", True, "data pool for which to " & descr & " the lock")
   ElseIf numPoolDescrs > 0 Then
     genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", True, "data pools for which to " & descr & " the locks")
   End If
   genProcParm(fileNo, "IN", "requestorId_in", g_dbtLockRequestorId, True, "identifies the Application (Server)")
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "identifies the User")
   If includeLockContext Then
     genProcParm(fileNo, "IN", "lockContext_in", g_dbtR2pLockContext, True, "(optional) refers to the Use Case context")
   End If
   genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", False, "number of data pools processed")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl(fileNo, -1, True)
 
   If includeLockContext Then
     genSpLogProcEnter(fileNo, qualProcNameLocal & suffix, ddlType, , IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "'dataPoolDescr_in", "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   Else
     genSpLogProcEnter(fileNo, qualProcNameLocal & suffix, ddlType, , IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "'dataPoolDescr_in", "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "numDataPools_out")
   End If

   genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

   genProcSectionHeader(fileNo, "call 'global' procedure'", , True)
   If includeLockContext Then
     Print #fileNo, addTab(1); "CALL "; qualProcNameGlobal; suffix; "("; IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "dataPoolDescr_in, ", "dataPoolDescrs_in, ")); "requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);"

     genSpLogProcExit(fileNo, qualProcNameLocal & suffix, ddlType, , IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "'dataPoolDescr_in", "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out")
   Else
     Print #fileNo, addTab(1); "CALL "; qualProcNameGlobal; suffix; "("; IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "dataPoolDescr_in, ", "dataPoolDescrs_in, ")); "requestorId_in, cdUserId_in, numDataPools_out);"

     genSpLogProcExit(fileNo, qualProcNameLocal & suffix, ddlType, , IIf(numPoolDescrs = 0, "", IIf(numPoolDescrs = 1, "'dataPoolDescr_in", "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "numDataPools_out")
   End If

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Sub genSetProdSupportSpsForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionIndex As Integer
   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim entityTypeDescr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim entityIdStrList As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim hasNoIdentity As Boolean
   Dim ignoreForChangelog As Boolean
   Dim hasNlAttributes As Boolean
   Dim aggHeadClassIndex As Integer
   Dim aggHeadShortClassName As String
   Dim isGenericAspect As Boolean
   Dim navToDivRelRefIndex As Integer ' follow this relationship when navigating to Division
   Dim navToDivDirection As RelNavigationDirection ' indicates wheter we need to follow left or right hand side to navigate to Division
   Dim navToFirstClassToDivDirection As RelNavigationDirection ' if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
   Dim navRefClassIndex As Integer
   Dim navRefClassShortName As String
   Dim fkAttrToClass As String
   Dim hasGroupIdAttrs As Boolean
   Dim isSubjectToPreisDurchschuss As Boolean
   Dim condenseData As Boolean
   Dim isAggHead As Boolean
   Dim aggChildClassIndexes() As Integer

   On Error GoTo ErrorExit

   isGenericAspect = False
   ReDim aggChildClassIndexes(0 To 0)

   If acmEntityType = eactClass Then
       navToFirstClassToDivDirection = -1
       navToDivRelRefIndex = g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex
       navToDivDirection = g_classes.descriptors(acmEntityIndex).navPathToDiv.navDirection
       navRefClassIndex = -1

       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       isGenericAspect = (UCase(g_classes.descriptors(acmEntityIndex).className) = "GENERICASPECT")
       isSubjectToPreisDurchschuss = g_classes.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss

       hasGroupIdAttrs = Not forNl And Not forGen And g_classes.descriptors(acmEntityIndex).hasGroupIdAttrInNonGenInclSubClasses

       If forNl Then
         entityTypeDescr = "ACM-Class (NL-Text)"
       Else
         entityTypeDescr = "ACM-Class"
       End If
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       entityIdStrList = getSubClassIdStrListByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex)
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
       aggChildClassIndexes = g_classes.descriptors(acmEntityIndex).aggChildClassIndexes
   ElseIf acmEntityType = eactRelationship Then
       navToFirstClassToDivDirection = g_relationships.descriptors(acmEntityIndex).navPathToDiv.navDirectionToClass
       navToDivRelRefIndex = -1
       navToDivDirection = -1
       If navToFirstClassToDivDirection = etLeft Then
         ' we need to follow relationship to left -> figure out what the complete path to Division is
         navRefClassIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
         navRefClassShortName = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName
         fkAttrToClass = genSurrogateKeyName(ddlType, navRefClassShortName)
           navToDivRelRefIndex = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).navPathToDiv.relRefIndex
           navToDivDirection = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).navPathToDiv.navDirection
       ElseIf navToFirstClassToDivDirection = etRight Then
         ' we need to follow relationship to right -> figure out what the complete path to Division is
         navRefClassIndex = g_relationships.descriptors(acmEntityIndex).rightEntityIndex
         navRefClassShortName = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName
         fkAttrToClass = genSurrogateKeyName(ddlType, g_relationships.descriptors(acmEntityIndex).lrShortRelName)
           navToDivRelRefIndex = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).navPathToDiv.relRefIndex
           navToDivDirection = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).navPathToDiv.navDirection
       End If

       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       If forNl Then
         entityTypeDescr = "ACM-Relationship (NL-Text)"
       Else
         entityTypeDescr = "ACM-Relationship"
       End If

       hasGroupIdAttrs = False

       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       entityIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       dbAcmEntityType = "R"
       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       relRefs.numRefs = 0
       isGenForming = False
       hasNoIdentity = False
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       isSubjectToPreisDurchschuss = g_relationships.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
       condenseData = False
       isAggHead = False
   Else
     Exit Sub
   End If

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 0 Or srcPoolIndex < 0) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
   If condenseData And Not isAggHead Then
     ' propagataion of data for aggregate children is done by aggregate head
     Exit Sub
   End If
 
   If aggHeadClassIndex > 0 Then
     aggHeadShortClassName = g_classes.descriptors(aggHeadClassIndex).shortName
   End If
 
   Dim transformation As AttributeListTransformation
   Dim qualTabNameSrc As String
   Dim qualTabNameSrcPar As String
   Dim qualTabNameTgt As String
   Dim qualTabNamenNavRef As String
   Dim qualTabNameAggHead As String
   Dim qualTabNameAggHeadNl As String
   Dim qualTabNameSrcNl As String
   Dim qualTabNameTgtNl As String
   Dim qualTabNameSrcGen As String

   If navRefClassIndex > 0 Then
     qualTabNamenNavRef = genQualTabNameByClassIndex(navRefClassIndex, ddlType, thisOrgIndex, srcPoolIndex, forGen)
   End If

   qualTabNameSrc = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, , , forNl)
   qualTabNameTgt = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , forNl)

   If forNl Or (navRefClassIndex > 0) Then
     qualTabNameSrcPar = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen)
   End If

   If isGenForming And Not hasNoIdentity Then
     qualTabNameSrcGen = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, True)
   Else
     qualTabNameSrcGen = ""
   End If

   If Not ignoreForChangelog And Not forNl And hasNlAttributes Then
     qualTabNameTgtNl = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , True)
     qualTabNameSrcNl = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, , , True)
   End If

   If aggHeadClassIndex > 0 Then
     qualTabNameAggHead = genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, srcPoolIndex)
     qualTabNameAggHeadNl = genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, srcPoolIndex, , , , True)
   End If

   Dim fkAttrToDiv As String
   Dim psFkAttrToDiv As String
   If navToDivRelRefIndex > 0 Then
       If g_classes.descriptors(g_classIndexProductStructure).navPathToDiv.navDirection = etLeft Then
         psFkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(g_classIndexProductStructure).navPathToDiv.relRefIndex).leftFkColName(ddlType)
       Else
         psFkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(g_classIndexProductStructure).navPathToDiv.relRefIndex).rightFkColName(ddlType)
       End If
       If navToDivDirection = etLeft Then
         fkAttrToDiv = g_relationships.descriptors(navToDivRelRefIndex).leftFkColName(ddlType)
       Else
         fkAttrToDiv = g_relationships.descriptors(navToDivRelRefIndex).rightFkColName(ddlType)
       End If
   End If

   Dim attrNameFkEntity As String
   attrNameFkEntity = genSurrogateKeyName(ddlType, acmEntityShortName)

   ' ####################################################################################################################
   ' #    SP for Setting Data Productive for given class / relationship
   ' ####################################################################################################################
 
   Dim qualProcName As String
   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, , , forNl, spnSetProductive)

   printSectionHeader("SP for Setting Data Productive for """ & qualTabNameSrc & """ (" & entityTypeDescr & " """ & _
     g_sections.descriptors(sectionIndex).sectionName & "." & acmEntityName & """" & IIf(forGen, "(GEN)", "") & ")", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to set productive")
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - if NOT NULL set only prices productive related to this LRT")
   genProcParm(fileNo, "IN", "opId_in", g_dbtEnumId, True, "identifies the operation (insert, update, delete, gen NL-Text for ChangeLog) to set productive")
   genProcParm(fileNo, "IN", "setProductiveTs_in", "TIMESTAMP", True, "marks the timestamp of setting data productive")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this call")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   If Not (condenseData) Then
     If acmEntityIndex = g_classIndexExpression Then
       ' we currently only have this for Expressions; we thus explicitly refer to Propagate-Routine for Expressions
       Dim qualProcNamePropagate As String
       qualProcNamePropagate = genQualProcName(g_sectionIndexMeta, spnPropExpr, ddlType)

       Dim qualProcNameInvPropagate As String
       qualProcNameInvPropagate = genQualProcName(g_sectionIndexMeta, spnPropInvExpr, ddlType)

       genProcSectionHeader(fileNo, "declare variables", , True)
       genVarDecl(fileNo, "v_numSuccess", "INTEGER")
       genVarDecl(fileNo, "v_numFail", "INTEGER")
       genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)")
       genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0")
       genSpLogDecl(fileNo)

       genSpLogProcEnter(fileNo, qualProcName, ddlType, , "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out")

       genProcSectionHeader(fileNo, "propagate new Aggregates to productive data pool")
       Print #fileNo, addTab(1); "IF ( opId_in = "; CStr(lrtStatusCreated); " ) THEN"

       Dim qualProcNameGenChangeLog As String
       qualProcNameGenChangeLog = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , forNl, spnSpGenChangelog)

       genProcSectionHeader(fileNo, "generate Change Log for propagated records", 2)
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameGenChangeLog; "(?,?,?,?)';"
       Print #fileNo,

       Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(2); "EXECUTE"
       Print #fileNo, addTab(3); "v_stmnt"
       Print #fileNo, addTab(2); "INTO"
       Print #fileNo, addTab(3); "v_rowCountCLog"
       Print #fileNo, addTab(2); "USING"
       Print #fileNo, addTab(3); "psOid_in,"
       Print #fileNo, addTab(3); "opId_in,"
       Print #fileNo, addTab(3); "setProductiveTs_in"
       Print #fileNo, addTab(2); ";"

       genProcSectionHeader(fileNo, "propagate records", 2, True)
       Print #fileNo, addTab(2); "CALL "; qualProcNamePropagate; "(psOid_in,"; genOrgId(thisOrgIndex, ddlType, True); ","; genPoolId(srcPoolIndex, ddlType); ","; genOrgId(thisOrgIndex, ddlType, True); ","; genPoolId(dstPoolIndex); ",v_numSuccess,v_numFail);"
       Print #fileNo, addTab(2); "SET rowCount_out = v_numSuccess;"

       Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusUpdated); " ) THEN"

       genProcSectionHeader(fileNo, "generate Change Log for propagated records", 2, True)
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameGenChangeLog; "(?,?,?,?)';"
       Print #fileNo,
 
       Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(2); "EXECUTE"
       Print #fileNo, addTab(3); "v_stmnt"
       Print #fileNo, addTab(2); "INTO"
       Print #fileNo, addTab(3); "v_rowCountCLog"
       Print #fileNo, addTab(2); "USING"
       Print #fileNo, addTab(3); "psOid_in,"
       Print #fileNo, addTab(3); "opId_in,"
       Print #fileNo, addTab(3); "setProductiveTs_in"
       Print #fileNo, addTab(2); ";"

       genProcSectionHeader(fileNo, "propagate records", 2, True)
       Print #fileNo, addTab(2); "CALL "; qualProcNameInvPropagate; "(psOid_in,"; genOrgId(thisOrgIndex, ddlType, True); ","; genPoolId(srcPoolIndex, ddlType); ","; genOrgId(thisOrgIndex, ddlType, True); ","; genPoolId(dstPoolIndex); ",setProductiveTs_in,v_numSuccess,v_numFail);"
       Print #fileNo, addTab(2); "SET rowCount_out = v_numSuccess;"

       Print #fileNo, addTab(1); "END IF;"
     Else
       genProcSectionHeader(fileNo, "declare conditions", , True)
       genCondDecl(fileNo, "alreadyExist", "42710")
       genCondDecl(fileNo, "notFound", "02000")

       If (Not ignoreForChangelog And Not forNl) Or (maintainGroupIdColumnsInSetProductive And hasGroupIdAttrs) Then
         genProcSectionHeader(fileNo, "declare variables")
         genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)")
       End If
       If Not ignoreForChangelog And Not forNl Then
         genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0")
       End If
       If maintainGroupIdColumnsInSetProductive And hasGroupIdAttrs Then
         genVarDecl(fileNo, "v_gidColCount", "INTEGER")
         genVarDecl(fileNo, "v_gidValCount", "BIGINT")
       End If
       genSpLogDecl(fileNo)

       genProcSectionHeader(fileNo, "declare continue handler")
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore"
       Print #fileNo, addTab(1); "END;"
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore"
       Print #fileNo, addTab(1); "END;"

       If Not forNl And hasNlAttributes Then
         genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, srcPoolIndex, ddlType, 1)
       End If

       genDdlForTempTablesSp(fileNo)

       genSpLogProcEnter(fileNo, qualProcName, ddlType, , "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out")

       genProcSectionHeader(fileNo, "initialize output parameter")
       Print #fileNo, addTab(1); "SET rowCount_out = 0;"

       If Not ignoreForChangelog And Not forNl Then
         Dim qualCalledProcedureName As String
         qualCalledProcedureName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , forNl, spnSpGenChangelog)

         Print #fileNo,
         If hasNlAttributes Then
           genProcSectionHeader(fileNo, "generate Change Log", 2, True)
           Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualCalledProcedureName; "(?,?,?,?)';"
           Print #fileNo,

           Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
           Print #fileNo,
           Print #fileNo, addTab(1); "EXECUTE"
           Print #fileNo, addTab(2); "v_stmnt"
           Print #fileNo, addTab(1); "INTO"
           Print #fileNo, addTab(2); "v_rowCountCLog"
           Print #fileNo, addTab(1); "USING"
           Print #fileNo, addTab(2); "psOid_in,"
           Print #fileNo, addTab(2); "opId_in,"
           Print #fileNo, addTab(2); "setProductiveTs_in"
           Print #fileNo, addTab(1); ";"
         Else
           Print #fileNo, addTab(1); "IF ( opId_in <> "; CStr(lrtStatusLocked); " ) THEN"
           genProcSectionHeader(fileNo, "generate Change Log", 2, True)
           Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualCalledProcedureName; "(?,?,?,?)';"
           Print #fileNo,

           Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
           Print #fileNo,
           Print #fileNo, addTab(2); "EXECUTE"
           Print #fileNo, addTab(3); "v_stmnt"
           Print #fileNo, addTab(2); "INTO"
           Print #fileNo, addTab(3); "v_rowCountCLog"
           Print #fileNo, addTab(2); "USING"
           Print #fileNo, addTab(3); "psOid_in,"
           Print #fileNo, addTab(3); "opId_in,"
           Print #fileNo, addTab(3); "setProductiveTs_in"
           Print #fileNo, addTab(2); ";"
           Print #fileNo, addTab(1); "END IF;"
         End If
       End If
     genProcSectionHeader(fileNo, "execute requested operation")
     If forNl Or Not hasNlAttributes Then
       Print #fileNo, addTab(1); "IF ( opId_in = "; CStr(lrtStatusCreated); " ) THEN"
     Else
       Print #fileNo, addTab(1); "IF ( opId_in = "; CStr(lrtStatusLocked); " ) THEN"

       genAddNlTextChangeLogDdlForIndividualAttrs(_
         fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, gc_tempTabNameChangeLog, gc_tempTabNameChangeLogNl, _
         qualTabNameSrcNl, genSurrogateKeyName(ddlType, acmEntityShortName), qualTabNameAggHeadNl, _
         genSurrogateKeyName(ddlType, aggHeadShortClassName), attrRefs, relRefs, forGen, "", "psOid_in", _
         thisOrgIndex, srcPoolIndex, False, True, ddlType, 2)

       Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusCreated); " ) THEN"
     End If

     If maintainGroupIdColumnsInSetProductive And hasGroupIdAttrs Then
       genProcSectionHeader(fileNo, "determine group IDs for new records in Work Data Pool", 2, True)

       Dim unqualSourceTabName As String
       unqualSourceTabName = getUnqualObjName(qualTabNameSrc)

       Dim qualProcNameGaSync As String
       qualProcNameGaSync = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, , , forNl, spnGroupIdSync)

       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameGaSync; "(?,"; IIf(disableLoggingDuringSync, "0,", ""); "?,?)';"
       Print #fileNo,

       Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(2); "EXECUTE"
       Print #fileNo, addTab(3); "v_stmnt"
       Print #fileNo, addTab(2); "INTO"
       Print #fileNo, addTab(3); "v_gidColCount,"
       Print #fileNo, addTab(3); "v_gidValCount"
       Print #fileNo, addTab(2); "USING"
       Print #fileNo, addTab(3); "psOid_in"
       Print #fileNo, addTab(2); ";"
     End If

     genProcSectionHeader(fileNo, "INSERT: propagate all records marked as 'created' in the work data pool to tables of the target data pool", 2, _
                                  Not (maintainGroupIdColumnsInSetProductive And hasGroupIdAttrs))
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameTgt
     Print #fileNo, addTab(2); "("

     If forNl Then
       genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, False, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     Else
       genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     End If

     Print #fileNo, addTab(2); ")"

     Print #fileNo, addTab(2); "SELECT"

     initAttributeTransformation(transformation, 7, , , , "WORK.")

     setAttributeMapping(transformation, 1, conHasBeenSetProductive, gc_dbTrue)
     setAttributeMapping(transformation, 2, conStatusId, CStr(statusProductive))
     setAttributeMapping(transformation, 3, conVersionId, "WORK." & g_anVersionId & " + 1")
     setAttributeMapping(transformation, 4, conIsDeleted, gc_dbFalse)
     setAttributeMapping(transformation, 5, conCreateTimestamp, "setProductiveTs_in")
     setAttributeMapping(transformation, 6, conLastUpdateTimestamp, "setProductiveTs_in")
     setAttributeMapping(transformation, 7, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")

     If forNl Then
       genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     Else
       genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, , False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     End If

     Print #fileNo, addTab(2); "FROM"

     Print #fileNo, addTab(3); qualTabNameSrc; " WORK"

     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); gc_tempTabNameSpAffectedEntities; " E"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "E.orParEntityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "E.orParEntityId = '"; entityIdStr; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "E.opId = opId_in"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "E.oid = WORK."; g_anOid

     If (isPsTagged Or navToDivRelRefIndex > 0) And forNl Then
       ' if we are processing an NL-Text table we need to navigate to parent table
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); qualTabNameSrcPar; " PAR"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PAR."; g_anOid; " = WORK."; attrNameFkEntity
     ElseIf Not isPsTagged And navRefClassIndex > 0 Then
       ' if we need to navigate to a class first before following a relation, do so
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); qualTabNamenNavRef; " PAR"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PAR."; g_anOid; " = WORK."; genSurrogateKeyName(ddlType, navRefClassShortName)
     End If

     ' alternative navigation to Division if we cannot navigate to ProductStructure
     If Not isPsTagged And navToDivRelRefIndex > 0 Then
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); g_qualTabNameProductStructure; " PS"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PS."; g_anOid; " = psOid_in"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); IIf(forNl Or navRefClassIndex > 0, "PAR.", "WORK."); fkAttrToDiv; " = PS."; psFkAttrToDiv
     End If

     Print #fileNo, addTab(2); "WHERE"
     If isPsTagged Then
       Print #fileNo, addTab(3); IIf(forNl, "PAR.", "WORK."); g_anPsOid; " = psOid_in"
     Else
       Print #fileNo, addTab(3); "(1=1)"
     End If

     Print #fileNo, addTab(2); ";"

     genProcSectionHeader(fileNo, "count the number of affected rows", 2)
     Print #fileNo, addTab(2); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

     Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusUpdated); " ) THEN"

     Print #fileNo, addTab(2); "-- UPDATE: propagate all records marked as 'changed' in the work data pool to tables of the target data pool"

     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualTabNameTgt; " PROD"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(2); "("

     If forNl Then
       genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, False, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     Else
       genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     End If

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "="
     Print #fileNo, addTab(2); "("

     Print #fileNo, addTab(3); "SELECT"

     initAttributeTransformation(transformation, 7, , , , "WORK.")

     setAttributeMapping(transformation, 1, conStatusId, CStr(statusProductive))
     setAttributeMapping(transformation, 2, conVersionId, "WORK." & conVersionId & " + 1")
     setAttributeMapping(transformation, 3, conCreateUser, "PROD." & g_anCreateUser)
     setAttributeMapping(transformation, 4, conCreateTimestamp, "PROD." & g_anCreateTimestamp)
     setAttributeMapping(transformation, 5, conLastUpdateTimestamp, "setProductiveTs_in")
     setAttributeMapping(transformation, 6, conHasBeenSetProductive, gc_dbTrue)
     setAttributeMapping(transformation, 7, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")

     If forNl Then
       genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, dstPoolIndex, 4, forGen, False, , edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     Else
       genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 4, , False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
     End If

     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameSrc; " WORK"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "WORK."; g_anOid; " = PROD."; g_anOid
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "PROD."; g_anOid; " IN"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "WORK."; g_anOid
     Print #fileNo, addTab(5); "FROM"

     Print #fileNo, addTab(6); qualTabNameSrc; " WORK"

     Print #fileNo, addTab(5); "INNER JOIN"
     Print #fileNo, addTab(6); gc_tempTabNameSpAffectedEntities; " E"
     Print #fileNo, addTab(5); "ON"
     Print #fileNo, addTab(6); "E.orParEntityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.orParEntityId = '"; entityIdStr; "'"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.opId = opId_in"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.oid = WORK."; g_anOid

     If (isPsTagged Or navToDivRelRefIndex > 0) And forNl Then
       ' if we are processing an NL-Text table we need to navigate to parent table
       Print #fileNo, addTab(5); "INNER JOIN"
       Print #fileNo, addTab(6); qualTabNameSrcPar; " PAR"
       Print #fileNo, addTab(5); "ON"
       Print #fileNo, addTab(6); "PAR."; g_anOid; " = WORK."; attrNameFkEntity
     ElseIf Not isPsTagged And navRefClassIndex > 0 Then
       ' if we need to navigate to a class first before following a relation, do so
       Print #fileNo, addTab(5); "INNER JOIN"
       Print #fileNo, addTab(6); qualTabNamenNavRef; " PAR"
       Print #fileNo, addTab(5); "ON"
       Print #fileNo, addTab(6); "PAR."; g_anOid; " = WORK."; genSurrogateKeyName(ddlType, navRefClassShortName)
     End If

     ' alternative navigation to Division if we cannot navigate to ProductStructure
     If Not isPsTagged And navToDivRelRefIndex > 0 Then
       Print #fileNo, addTab(5); "INNER JOIN"
       Print #fileNo, addTab(6); g_qualTabNameProductStructure; " PS"
       Print #fileNo, addTab(5); "ON"
       Print #fileNo, addTab(6); "PS."; g_anOid; " = psOid_in"
       Print #fileNo, addTab(7); "AND"
       Print #fileNo, addTab(6); IIf(forNl Or navRefClassIndex > 0, "PAR.", "WORK."); fkAttrToDiv; " = PS."; psFkAttrToDiv
     End If

     Print #fileNo, addTab(5); "WHERE"
     Print #fileNo, addTab(6); "PROD."; g_anOid; " = WORK."; g_anOid
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "WORK."; g_anStatus; " = "; CStr(statusReadyToBeSetProductive)
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "WORK."; g_anHasBeenSetProductive; " = "; gc_dbTrue
     If isPsTagged Then
       Print #fileNo, addTab(7); "AND"
       Print #fileNo, addTab(6); IIf(forNl, "PAR.", "WORK."); g_anPsOid; " = psOid_in"
     End If

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader(fileNo, "count the number of affected rows", 2)
     Print #fileNo, addTab(2); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

     Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusDeleted); " ) THEN"
     genProcSectionHeader(fileNo, "DELETE: delete all 'deleted' records in the target data pool", 2, True)

     Print #fileNo, addTab(2); "DELETE FROM"
     Print #fileNo, addTab(3); qualTabNameTgt; " PROD"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "PROD."; g_anOid
     Print #fileNo, addTab(4); "IN ("
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "WORK."; g_anOid
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); qualTabNameSrc; " WORK"

     Print #fileNo, addTab(5); "INNER JOIN"
     Print #fileNo, addTab(6); gc_tempTabNameSpAffectedEntities; " E"
     Print #fileNo, addTab(5); "ON"
     Print #fileNo, addTab(6); "E.orParEntityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.orParEntityId = '"; entityIdStr; "'"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.opId = opId_in"
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(7); "AND"
     Print #fileNo, addTab(6); "E.oid = WORK."; g_anOid

     Print #fileNo, addTab(5); "WHERE"
     Print #fileNo, addTab(6); "PROD."; g_anOid; " = WORK."; g_anOid
     If isPsTagged Then
       If forNl Then
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "WORK."; attrNameFkEntity; " IN"
         Print #fileNo, addTab(7); "( SELECT "; g_anOid; " FROM "; qualTabNameSrcPar; " WHERE "; g_anPsOid; " = psOid_in )"
       Else
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "WORK."; g_anPsOid; " = psOid_in"
       End If
     Else
       If navToDivRelRefIndex > 0 Then
         Print #fileNo, addTab(7); "AND"

         If forNl Or navRefClassIndex > 0 Then
           ' need to navigate to parent to find the reference to Division
           Print #fileNo, addTab(6); "("
           Print #fileNo, addTab(7); "SELECT"
           Print #fileNo, addTab(8); "PAR."; fkAttrToDiv
           Print #fileNo, addTab(7); "FROM"
           Print #fileNo, addTab(8); IIf(forNl, qualTabNameSrcPar, qualTabNamenNavRef); " PAR"
           Print #fileNo, addTab(7); "WHERE"
           Print #fileNo, addTab(8); "PAR."; g_anOid; " = PROD."; genSurrogateKeyName(ddlType, IIf(forNl, acmEntityShortName, navRefClassShortName))
           Print #fileNo, addTab(6); ")"
           Print #fileNo, addTab(6); "="
         Else
           Print #fileNo, addTab(6); fkAttrToDiv; " ="
         End If

         Print #fileNo, addTab(6); "("
         Print #fileNo, addTab(7); "SELECT"
         Print #fileNo, addTab(8); psFkAttrToDiv
         Print #fileNo, addTab(7); "FROM"
         Print #fileNo, addTab(8); g_qualTabNameProductStructure
         Print #fileNo, addTab(7); "WHERE"
         Print #fileNo, addTab(8); g_anOid; " = psOid_in"
         Print #fileNo, addTab(6); ")"

       End If
     End If

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader(fileNo, "count the number of affected rows", 2)
     Print #fileNo, addTab(2); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

     Print #fileNo, addTab(1); "END IF;"
     End If
   End If
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for 'post-processing' data in Work Data Pool after 'setProductive' for given class / relationship
   ' ####################################################################################################################
 
   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, , , forNl, spnSetProductivePostProcess)

   printSectionHeader("post-processing' data in Work Data Pool after 'setProductive' for """ & qualTabNameSrc & """ (" & entityTypeDescr & " """ & _
     g_sections.descriptors(sectionIndex).sectionName & "." & acmEntityName & """" & IIf(forGen, "(GEN)", "") & ")", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to process")
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtOid, True, "LRT-OID - if NOT NULL only process prices related to this LRT")
   genProcParm(fileNo, "IN", "opId_in", g_dbtEnumId, False, "identifies the operation (insert, update or delete) to process")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "alreadyExist", "42710")

   genSpLogDecl(fileNo, , True)

   genProcSectionHeader(fileNo, "declare continue handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempTablesSp(fileNo)

   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "psOid_in", "opId_in")


   If acmEntityIndex = g_classIndexExpression Then
     Print #fileNo,
     Print #fileNo, addTab(1); "IF ( opId_in = "; CStr(lrtStatusCreated); " ) THEN"

     genProcSectionHeader(fileNo, "mark all 'new' records of the work data pool as 'being productive'", 2, True)

     Dim i As Integer
     For i = 1 To UBound(aggChildClassIndexes)
         If g_classes.descriptors(aggChildClassIndexes(i)).classIndex = g_classes.descriptors(aggChildClassIndexes(i)).orMappingSuperClassIndex Then
           Print #fileNo,

           Dim j As Integer
           For j = 1 To IIf(g_classes.descriptors(aggChildClassIndexes(i)).isGenForming And Not g_classes.descriptors(aggChildClassIndexes(i)).hasNoIdentity, 2, 1)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); genQualTabNameByClassIndex(g_classes.descriptors(aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, srcPoolIndex, (j = 2)); " S"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "S."; g_anHasBeenSetProductive; " = 1,"
             If g_classes.descriptors(aggChildClassIndexes(i)).classIndex = g_classIndexExpression Then
               Print #fileNo, addTab(3); "S."; g_anStatus; " = 5,"
             End If
             Print #fileNo, addTab(3); "S."; g_anVersionId; " = S."; g_anVersionId; " + 1"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "S."; g_anHasBeenSetProductive; " = "; gc_dbFalse
             If g_classes.descriptors(aggChildClassIndexes(i)).isPsTagged Then
               Print #fileNo, addTab(4); "AND"
               Print #fileNo, addTab(3); "S."; g_anPsOid; " = psOid_in"
             End If

             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); genQualTabNameByClassIndex(g_classes.descriptors(aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, dstPoolIndex, (j = 2)); " T"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "S."; g_anOid; " = T."; g_anOid
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "T."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); ";"
             Print #fileNo,

             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); genQualTabNameByClassIndex(g_classes.descriptors(aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, dstPoolIndex, (j = 2)); " S"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "S."; g_anHasBeenSetProductive; " = 1,"
             If g_classes.descriptors(aggChildClassIndexes(i)).classIndex = g_classIndexExpression Then
               Print #fileNo, addTab(3); "S."; g_anStatus; " = 5,"
             End If
             Print #fileNo, addTab(3); "S."; g_anVersionId; " = S."; g_anVersionId; " + 1"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "S."; g_anHasBeenSetProductive; " = "; gc_dbFalse
             If g_classes.descriptors(aggChildClassIndexes(i)).isPsTagged Then
               Print #fileNo, addTab(4); "AND"
               Print #fileNo, addTab(3); "S."; g_anPsOid; " = psOid_in"
             End If

             Print #fileNo, addTab(2); ";"

           Next j

         End If
     Next i

   Else
     Print #fileNo,
     Print #fileNo, addTab(1); "IF ( opId_in = "; CStr(lrtStatusCreated); " ) THEN"

     genProcSectionHeader(fileNo, "mark all 'new' records of the work data pool as 'being productive'", 2, True)
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualTabNameSrc; " UPD"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); "UPD."; g_anHasBeenSetProductive; " = 1,"
     Print #fileNo, addTab(3); "UPD."; g_anStatus; " = "; CStr(statusProductive); ","
     Print #fileNo, addTab(3); "UPD."; g_anVersionId; " = "; g_anVersionId; " + 1"
     Print #fileNo, addTab(2); "WHERE"

     Print #fileNo, addTab(3); "UPD."; g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "E.oid"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameSpAffectedEntities; " E"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "E.orParEntityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "E.orParEntityId = '"; entityIdStr; "'"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "E.opId = opId_in"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "E.oid = UPD."; g_anOid
     Print #fileNo, addTab(3); ")"

     If isPsTagged Then
       If forNl Then
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "UPD."; attrNameFkEntity; " IN ( SELECT "; g_anOid; " FROM "; qualTabNameSrcPar; " WHERE "; g_anPsOid; " = psOid_in )"
       Else
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); g_anPsOid; " = psOid_in"
       End If
     Else
       If navToDivRelRefIndex > 0 Then
         Print #fileNo, addTab(4); "AND"

         If forNl Or navRefClassIndex > 0 Then
           ' need to navigate to parent to find the reference to Division
           Print #fileNo, addTab(3); "("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "PAR."; fkAttrToDiv
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); IIf(forNl, qualTabNameSrcPar, qualTabNamenNavRef); " PAR"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PAR."; g_anOid; " = UPD."; genSurrogateKeyName(ddlType, IIf(forNl, acmEntityShortName, navRefClassShortName))
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(3); "="
         Else
           Print #fileNo, addTab(3); fkAttrToDiv; " ="
         End If

         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); psFkAttrToDiv
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); g_qualTabNameProductStructure
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); g_anOid; " = psOid_in"
         Print #fileNo, addTab(3); ")"
       End If
     End If

     Print #fileNo, addTab(2); ";"

   End If

   Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusUpdated); " ) THEN"

   genProcSectionHeader(fileNo, "mark all 'changed' records of the work data pool as 'being productive'", 2, True)
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualTabNameSrc; " UPD"
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "UPD."; g_anStatus; " = "; CStr(statusProductive); ","
   Print #fileNo, addTab(3); "UPD."; g_anVersionId; " = "; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"

   Print #fileNo, addTab(3); "UPD."; g_anOid; " IN ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "E.oid"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); gc_tempTabNameSpAffectedEntities; " E"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "E.orParEntityType = '"; dbAcmEntityType; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.orParEntityId = '"; entityIdStr; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.opId = opId_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.oid = UPD."; g_anOid
   Print #fileNo, addTab(3); ")"

   If isPsTagged Then
     If forNl Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "UPD."; attrNameFkEntity; " IN ( SELECT "; g_anOid; " FROM "; qualTabNameSrcPar; " WHERE "; g_anPsOid; " = psOid_in )"
     Else
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "UPD."; g_anPsOid; " = psOid_in"
     End If
   Else
     If navToDivRelRefIndex > 0 Then
       Print #fileNo, addTab(4); "AND"

       If forNl Or navRefClassIndex > 0 Then
         ' need to navigate to parent to find the reference to Division
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "PAR."; fkAttrToDiv
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); IIf(forNl, qualTabNameSrcPar, qualTabNamenNavRef); " PAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PAR."; g_anOid; " = UPD."; genSurrogateKeyName(ddlType, IIf(forNl, acmEntityShortName, navRefClassShortName))
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(3); "="
       Else
         Print #fileNo, addTab(3); fkAttrToDiv; " ="
       End If

       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); psFkAttrToDiv
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameProductStructure
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); g_anOid; " = psOid_in"
       Print #fileNo, addTab(3); ")"
     End If
   End If

   Print #fileNo, addTab(2); ";"
 
   Print #fileNo, addTab(1); "ELSEIF ( opId_in = "; CStr(lrtStatusDeleted); " ) THEN"
 
   genProcSectionHeader(fileNo, "delete all 'deleted' records in the work data pool", 2, True)
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualTabNameSrc; " DEL"
   Print #fileNo, addTab(2); "WHERE"

   Print #fileNo, addTab(3); "DEL."; g_anOid; " IN ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "E.oid"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); gc_tempTabNameSpAffectedEntities; " E"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "E.orParEntityType = '"; dbAcmEntityType; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.orParEntityId = '"; entityIdStr; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.opId = opId_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.oid = DEL."; g_anOid
   Print #fileNo, addTab(3); ")"

   If isPsTagged Then
     If forNl Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "DEL."; attrNameFkEntity; " IN"
       Print #fileNo, addTab(4); "( SELECT "; g_anOid; " FROM "; qualTabNameSrcPar; " WHERE "; g_anPsOid; " = psOid_in )"
     Else
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "DEL."; g_anPsOid; " = psOid_in"
     End If
   Else
     If navToDivRelRefIndex > 0 Then
       Print #fileNo, addTab(4); "AND"

       If forNl Or navRefClassIndex > 0 Then
         ' need to navigate to parent to find the reference to Division
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "PAR."; fkAttrToDiv
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); IIf(forNl, qualTabNameSrcPar, qualTabNamenNavRef); " PAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PAR."; g_anOid; " = DEL."; genSurrogateKeyName(ddlType, IIf(forNl, acmEntityShortName, navRefClassShortName))
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(3); "="
       Else
         Print #fileNo, addTab(3); fkAttrToDiv; " ="
       End If

       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); psFkAttrToDiv
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameProductStructure
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); g_anOid; " = psOid_in"
       Print #fileNo, addTab(3); ")"
     End If
   End If

   Print #fileNo, addTab(2); ";"
 
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(fileNo, qualProcName, ddlType, , "psOid_in", "opId_in")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If Not ignoreForChangelog And Not forNl Then
     ' we handle ChangeLog only in the non-NL-case; NL-tables are handled inside
     genChangeLogSupportForEntity(acmEntityIndex, acmEntityType, relRefs, _
       qualTabNameSrc, qualTabNameSrcNl, qualTabNameTgt, qualTabNameTgtNl, qualTabNameSrcGen, qualTabNameAggHeadNl, qualTabNameAggHead, _
       thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl, eclSetProd)
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
 End Sub
 
 
 Sub genSetProdSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     genSetProdSupportSpsForEntity(g_classes.descriptors(classIndex).classIndex, eactClass, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen)

     If IIf(forGen, g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses) Then
       genSetProdSupportSpsForEntity(g_classes.descriptors(classIndex).classIndex, eactClass, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, True)
     End If
 End Sub
 
 
 Sub genSetProdSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     genSetProdSupportSpsForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen)

     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       genSetProdSupportSpsForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, True)
     End If
 End Sub
 
 ' ### ENDIF IVK ###
 
 
 
 
