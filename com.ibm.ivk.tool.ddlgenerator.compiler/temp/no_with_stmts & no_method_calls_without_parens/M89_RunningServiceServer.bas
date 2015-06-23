 Attribute VB_Name = "M89_RunningServiceServer"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 5
 
 
 Sub genRssSupDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtLdm Then
     genRssSupForDb(edtLdm)
   ElseIf ddlType = edtPdm Then
     genRssSupForDb(edtPdm)

     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).supportAcm And Not g_pools.descriptors(thisPoolIndex).isArchive Then
             genRssSupByPool(edtPdm, thisOrgIndex, thisPoolIndex)
           End If
       Next thisPoolIndex
     Next thisOrgIndex
   End If
 End Sub
 
 
 Private Sub genRssSupForDb( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtLdm Then
     ' we currently do not support this
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStep, ddlType, , , , phaseAliases, ldmIterationPostProc)

   Dim unqualTabNamePaiMessageLog As String
   unqualTabNamePaiMessageLog = getUnqualObjName(g_qualTabNamePaiMessageLog)
   Dim unqualTabNameRssStatus As String
   unqualTabNameRssStatus = getUnqualObjName(g_qualTabNameRssStatus)
   Dim unqualTabNameRssHistory As String
   unqualTabNameRssHistory = getUnqualObjName(unqualTabNameRssHistory)

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   Dim columnDefault As String

   Dim qualProcNameRssGetStatus As String

   ' ####################################################################################################################
   ' #    Procedure retrieving the Running-Service-Server-Status
   ' ####################################################################################################################
   qualProcNameRssGetStatus = genQualProcName(g_sectionIndexPaiLog, spnRssGetStatus, ddlType)
 
   printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRssGetStatus
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "(optional) CD User Id of the mdsUser calling this procedure (if NULL, use CURRENT USER)")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of status records retrieved")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_msgStmntText", "VARCHAR(2000)", "NULL")
   genVarDecl(fileNo, "v_message", "VARCHAR(32600)", "NULL")
   genVarDecl(fileNo, "v_grantCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_timestampStr", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_timestamp", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_userIdStr", g_dbtUserId, "NULL")
   genVarDecl(fileNo, "v_correlationIdStr", "VARCHAR(128)", "NULL")
   genVarDecl(fileNo, "v_callerCorrelationIdStr", "VARCHAR(128)", "NULL")
   genVarDecl(fileNo, "v_dataPoolStr", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_accessModeId", g_dbtEnumId, "NULL")
   genVarDecl(fileNo, "v_lrtOidStr", "VARCHAR(25)", "NULL")
   genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_subSystemStr", "VARCHAR(3)", "NULL")
   genVarDecl(fileNo, "v_serverIdStr", "VARCHAR(64)", "NULL")
   genVarDecl(fileNo, "v_threadIdStr", "VARCHAR(64)", "NULL")
   genVarDecl(fileNo, "v_actionStr", "VARCHAR(30)", "NULL")
   genVarDecl(fileNo, "v_useCaseIdStr", "VARCHAR(128)", "NULL")
   genVarDecl(fileNo, "v_commonActivityStr", "VARCHAR(30)", "NULL")
   genVarDecl(fileNo, "v_customTextStr", "VARCHAR(128)", "NULL")
   Print #fileNo,
   genVarDecl(fileNo, "v_lastSystemStartup", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_formatError", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_ignoreRecord", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_ignoreErrors", g_dbtBoolean, gc_dbFalse)
   Print #fileNo,

   genVarDecl(fileNo, "v_logTableName1", "VARCHAR(100)", "'" & unqualTabNamePaiMessageLog & "_1'")
   genVarDecl(fileNo, "v_logTableName2", "VARCHAR(100)", "'" & unqualTabNamePaiMessageLog & "_2'")
   genVarDecl(fileNo, "v_logSchemaName", "VARCHAR(50)", "'" & getSchemaName(g_qualTabNamePaiMessageLog) & "'")
   genVarDecl(fileNo, "v_logAliasName", "VARCHAR(50)", "'" & unqualTabNamePaiMessageLog & "'")
   genVarDecl(fileNo, "v_qualLogAliasName", "VARCHAR(100)", "'" & g_qualTabNamePaiMessageLog & "'")
   genVarDecl(fileNo, "v_prevLogTableName", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_qualPrevLogTableName", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_nextLogTableName", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_qualNextLogTableName", "VARCHAR(100)", "NULL")
   Print #fileNo,
   genVarDecl(fileNo, "v_cleanupStmntCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_cleanupRowCount", "INTEGER", "NULL")
   Print #fileNo,
   genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_mySqlCode", "INTEGER", "NULL")
   genSpLogDecl(fileNo)
 
   genProcSectionHeader(fileNo, "declare conditions")
   genCondDecl(fileNo, "valueTooLong", "22001")
   genCondDecl(fileNo, "dateTimeFormatError", "22007")
   genCondDecl(fileNo, "smallIntFormatError", "22018")
   genCondDecl(fileNo, "numericOverflowError", "22003")
   genCondDecl(fileNo, "objectNotExists", "42704")
   genCondDecl(fileNo, "objectAlreadyExists", "42710")
   genCondDecl(fileNo, "cursorNotOpen", "24501")
 
   genProcSectionHeader(fileNo, "declare statements")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
   genVarDecl(fileNo, "v_msgStmnt", "STATEMENT")

   genProcSectionHeader(fileNo, "declare cursor")
   Print #fileNo, addTab(1); "DECLARE msgCursor CURSOR WITH HOLD FOR v_msgStmnt;"
 
   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR dateTimeFormatError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_formatError = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR smallIntFormatError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_formatError = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR numericOverflowError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_formatError = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR valueTooLong"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_formatError = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objectNotExists"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objectAlreadyExists"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR cursorNotOpen"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_mySqlCode = SQLCODE;"
   Print #fileNo, addTab(2); "IF NOT (v_ignoreErrors = 1) THEN"
   genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out")
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(1); "("

   initAttributeTransformation(transformation, 0)
   transformation.ignoreConstraint = True
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrListForEntityWithColReuse(g_classIndexRssHistory, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone)

   Dim i As Integer
   For i = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); genTransformedAttrDeclByDomain(tabColumns.descriptors(i).acmAttributeName, "-", eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformation, _
                                 eactClass, g_classIndexPaiMessageLog, , i < tabColumns.numDescriptors, ddlType, , , , , 0)
   Next i

   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "ON ROLLBACK PRESERVE ROWS"
   Print #fileNo, addTab(1); "WITH REPLACE;"

   Print #fileNo,
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelogConsolidated"
   Print #fileNo, addTab(1); "LIKE"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "ON ROLLBACK PRESERVE ROWS"
   Print #fileNo, addTab(1); "WITH REPLACE;"

   genSpLogProcEnter(fileNo, qualProcNameRssGetStatus, ddlType, , "'cdUserId_in", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNo, "determine next log file")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "COALESCE(BASE_TABNAME, TABNAME)"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_prevLogTableName"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TABLES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TABSCHEMA = v_logSchemaName"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TABNAME = v_logAliasName"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TYPE IN ('A', 'T')"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_qualPrevLogTableName = v_logSchemaName || '.' || v_prevLogTableName;"
   Print #fileNo, addTab(1); "SET v_nextLogTableName     = (CASE WHEN COALESCE(v_prevLogTableName, '') = v_logTableName2 THEN v_logTableName1 ELSE v_logTableName2 END);"
   Print #fileNo, addTab(1); "SET v_qualNextLogTableName = v_logSchemaName || '.' || v_nextLogTableName;"
 
   genProcSectionHeader(fileNo, "truncat table identified by v_qualNextLogTableName", 0, True)
   Print #fileNo, addTab(1); "SET v_stmntText ="
   Print #fileNo, addTab(2); "'TRUNCATE TABLE ' ||"
   Print #fileNo, addTab(3); "v_qualNextLogTableName || ' ' ||"
   Print #fileNo, addTab(3); "'DROP STORAGE ' ||"
   Print #fileNo, addTab(2); "'IGNORE DELETE TRIGGERS ' ||"
   Print #fileNo, addTab(2); "'CONTINUE IDENTITY ' ||"
   Print #fileNo, addTab(2); "'IMMEDIATE'"
   Print #fileNo, addTab(1); ";"
   genProcSectionHeader(fileNo, "commit to allow truncate to next log table", 1, True)
   Print #fileNo, addTab(1); "COMMIT;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntText;"

   genProcSectionHeader(fileNo, "drop Alias / Table with name v_qualLogAliasName", 1, True)
   Print #fileNo, addTab(1); "SET v_ignoreErrors = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "IF EXISTS (SELECT 1 FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = v_logAliasName AND TABSCHEMA = v_logSchemaName) THEN"
   Print #fileNo, addTab(2); "SET v_stmntText = 'DROP TABLE ' || v_qualLogAliasName;"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntText;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "SET v_stmntText = 'DROP ALIAS ' || v_qualLogAliasName;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntText;"
 
   genProcSectionHeader(fileNo, "lock current message log table")
   Print #fileNo, addTab(1); "IF v_qualPrevLogTableName IS NOT NULL THEN"
   Print #fileNo, addTab(2); "SET v_stmntText = 'LOCK TABLE ' || v_qualPrevLogTableName || ' IN EXCLUSIVE MODE';"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntText;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "create Alias with name v_qualLogAliasName")
   Print #fileNo, addTab(1); "SET v_stmntText = 'CREATE ALIAS ' || v_qualLogAliasName || ' FOR ' || v_qualNextLogTableName;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntText;"

   Print #fileNo, addTab(1); "SET v_ignoreErrors = "; gc_dbFalse; ";"

   genProcSectionHeader(fileNo, "commit to allow access to new log table")
   Print #fileNo, addTab(1); "COMMIT;"

   Dim qualProcedureNameCleanup As String
   qualProcedureNameCleanup = genQualProcName(g_sectionIndexDbAdmin, spnCleanData, ddlType)
   genProcSectionHeader(fileNo, "perform housekeeping on History-table")
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanup; "(2, 'DBAdmin', 'PaiLog', v_cleanupStmntCount, v_cleanupRowCount);"
 
   genProcSectionHeader(fileNo, "process PAI log messages")

   Print #fileNo, addTab(1); "IF EXISTS(SELECT 1 FROM SYSCAT.TABLES WHERE TABSCHEMA = v_logSchemaName AND TABNAME = v_prevLogTableName) THEN"

   Print #fileNo, addTab(2); "SET v_msgStmntText = 'SELECT CAST(MESSAGE AS VARCHAR(2000)) FROM ' || v_qualPrevLogTableName || ' WHERE LEVEL = ''INFO'' AND LENGTH(MESSAGE) <= 2000';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_msgStmnt FROM v_msgStmntText;"
   Print #fileNo, addTab(2); "OPEN msgCursor;"
   Print #fileNo, addTab(2); "SET v_mySqlCode = NULL;"
   Print #fileNo, addTab(2); "FETCH msgCursor INTO v_message;"
   Print #fileNo, addTab(2); "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);"
   Print #fileNo,
   Print #fileNo, addTab(2); "WHILE (v_mySqlCode = 0) DO"
 
   Print #fileNo, addTab(3); "SET v_timestampStr           = NULL;"
   Print #fileNo, addTab(3); "SET v_timestamp              = NULL;"
   Print #fileNo, addTab(3); "SET v_userIdStr              = NULL;"
   Print #fileNo, addTab(3); "SET v_correlationIdStr       = NULL;"
   Print #fileNo, addTab(3); "SET v_callerCorrelationIdStr = NULL;"
   Print #fileNo, addTab(3); "SET v_dataPoolStr            = NULL;"
   Print #fileNo, addTab(3); "SET v_orgOid                 = NULL;"
   Print #fileNo, addTab(3); "SET v_psOid                  = NULL;"
   Print #fileNo, addTab(3); "SET v_accessModeId           = NULL;"
   Print #fileNo, addTab(3); "SET v_lrtOidStr              = NULL;"
   Print #fileNo, addTab(3); "SET v_lrtOid                 = NULL;"
   Print #fileNo, addTab(3); "SET v_subSystemStr           = NULL;"
   Print #fileNo, addTab(3); "SET v_serverIdStr            = NULL;"
   Print #fileNo, addTab(3); "SET v_threadIdStr            = NULL;"
   Print #fileNo, addTab(3); "SET v_actionStr              = NULL;"
   Print #fileNo, addTab(3); "SET v_useCaseIdStr           = NULL;"
   Print #fileNo, addTab(3); "SET v_commonActivityStr      = NULL;"
   Print #fileNo, addTab(3); "SET v_customTextStr          = NULL;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_formatError            = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_ignoreRecord           = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_ignoreErrors           = 1; -- in case of error during parsing: just ignore record"

   genProcSectionHeader(fileNo, "parse message text", 3)
   Print #fileNo, addTab(3); "parse:"
   Print #fileNo, addTab(3); "FOR msgElemLoop AS msgCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CAST(RTRIM(LEFT(SUBSTR(elem, 2, LENGTH(elem)-2), 100)) AS VARCHAR(100)) AS elem,"
   Print #fileNo, addTab(5); "posIndex"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "TABLE("; g_qualFuncNameStrElems; "(v_message, CAST(',' AS CHAR(1)), '""', '""')) AS X"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "elem IS NOT NULL"
   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "IF v_mySqlCode = -501 THEN -- CURSOR NOT OPEN"
   Print #fileNo, addTab(5); "LEAVE parse;"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(6); "IF (posIndex =  0) THEN SET v_timestampStr      = elem;"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  1) THEN SET v_userIdStr         = RTRIM(LEFT(elem, 16));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  2) THEN SET v_correlationIdStr  = RTRIM(LEFT(elem, 128));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  3) THEN SET v_dataPoolStr       = elem;"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  4) THEN SET v_lrtOidStr         = RTRIM(LEFT(elem, 25));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  5) THEN SET v_subSystemStr      = UCASE(RTRIM(elem));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  6) THEN SET v_serverIdStr       = RTRIM(LEFT(elem, 64));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  7) THEN SET v_threadIdStr       = RTRIM(LEFT(elem, 64));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  8) THEN"
   Print #fileNo, addTab(4); "ELSEIF (posIndex =  9) THEN SET v_actionStr         = RTRIM(LEFT(elem, 30));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex = 10) THEN SET v_useCaseIdStr      = RTRIM(LEFT(elem, 128));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex = 11) THEN SET v_commonActivityStr = RTRIM(LEFT(elem, 30));"
   Print #fileNo, addTab(4); "ELSEIF (posIndex = 12) THEN SET v_customTextStr     = RTRIM(LEFT(elem, 128));"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF COALESCE(v_lrtOidStr, '') <> '' THEN"
   Print #fileNo, addTab(4); "SET v_lrtOid = "; g_dbtOid; "(v_lrtOidStr);"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "IF LENGTH(v_subSystemStr) > 3 THEN"
   Print #fileNo, addTab(4); "SET v_ignoreRecord = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "IF COALESCE(v_actionStr, '') = '' THEN"
   Print #fileNo, addTab(4); "SET v_ignoreRecord = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "IF COALESCE(v_useCaseIdStr, '') = '' THEN"
   Print #fileNo, addTab(4); "SET v_ignoreRecord = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "END IF;"
   ' FIXME: set v_userId and v_correlationId to NULL if ''
   Print #fileNo,
   Print #fileNo, addTab(3); "IF COALESCE(v_timestampStr, '') <> '' THEN"
   Print #fileNo, addTab(4); "SET v_timestamp = TIMESTAMP(REPLACE(REPLACE(v_timestampStr, ':', '.'), '-24.', '-00.'));"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF COALESCE(v_dataPoolStr, '') <> '' THEN"

   genProcSectionHeader(fileNo, "parse v_dataPoolStr", 4, True)
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "ABS(orgOid),"
   Print #fileNo, addTab(5); "ABS(psOid),"
   Print #fileNo, addTab(5); "ABS(accessModeId)"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_orgOid,"
   Print #fileNo, addTab(5); "v_psOid,"
   Print #fileNo, addTab(5); "v_accessModeId"
   Print #fileNo, addTab(4); "FROM"

   Dim qualFuncNameParseDataPools As String
   qualFuncNameParseDataPools = genQualFuncName(g_sectionIndexMeta, udfnParseDataPools, ddlType, , , , , , True)

   Print #fileNo, addTab(5); "TABLE("; qualFuncNameParseDataPools; "(v_dataPoolStr)) AS X"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY -- there should be only one row"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_ignoreErrors = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "IF v_formatError = "; gc_dbFalse; " AND v_ignoreRecord = "; gc_dbFalse; " THEN"

   Print #fileNo, addTab(4); "IF v_actionStr = 'beginInterface' THEN"
   Print #fileNo, addTab(5); "SET v_callerCorrelationIdStr = RTRIM(CAST(LEFT(v_customTextStr, 128) AS VARCHAR(128)));"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "timestamp,"
   Print #fileNo, addTab(5); "userId,"
   Print #fileNo, addTab(5); "correlationId,"
   Print #fileNo, addTab(5); "callerId,"
   Print #fileNo, addTab(5); "orgOid,"
   Print #fileNo, addTab(5); "psOid,"
   Print #fileNo, addTab(5); "accessMode_ID,"
   Print #fileNo, addTab(5); "lrtOid,"
   Print #fileNo, addTab(5); "subSystem,"
   Print #fileNo, addTab(5); "serverId,"
   Print #fileNo, addTab(5); "threadId,"
   Print #fileNo, addTab(5); "action,"
   Print #fileNo, addTab(5); "service,"
   Print #fileNo, addTab(5); "commonActivity,"
   Print #fileNo, addTab(5); "customText"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "v_timestamp,"
   Print #fileNo, addTab(5); "v_userIdStr,"
   Print #fileNo, addTab(5); "v_correlationIdStr,"
   Print #fileNo, addTab(5); "v_callerCorrelationIdStr,"
   Print #fileNo, addTab(5); "v_orgOid,"
   Print #fileNo, addTab(5); "v_psOid,"
   Print #fileNo, addTab(5); "v_accessModeId,"
   Print #fileNo, addTab(5); "v_lrtOid,"
   Print #fileNo, addTab(5); "v_subSystemStr,"
   Print #fileNo, addTab(5); "v_serverIdStr,"
   Print #fileNo, addTab(5); "v_threadIdStr,"
   Print #fileNo, addTab(5); "v_actionStr,"
   Print #fileNo, addTab(5); "v_useCaseIdStr,"
   Print #fileNo, addTab(5); "v_commonActivityStr,"
   Print #fileNo, addTab(5); "v_customTextStr"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_mySqlCode = NULL;"
   Print #fileNo, addTab(3); "FETCH msgCursor INTO v_message;"
   Print #fileNo, addTab(3); "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);"
   Print #fileNo, addTab(2); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(2); "CLOSE msgCursor WITH RELEASE;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "copy records to History-Table")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); g_qualTabNameRssHistory
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "*"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog"

   Print #fileNo, addTab(1); "WHERE"
   Dim isFirstCond As Boolean
   isFirstCond = True
 
   initAttributeTransformation(transformation, 0)
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrListForEntityWithColReuse(g_classIndexRssHistory, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone)

   For i = 1 To tabColumns.numDescriptors
         If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).valueList <> "" Then
           If Not isFirstCond Then
             Print #fileNo, addTab(3); "AND"
           End If
           isFirstCond = False
           Print #fileNo, addTab(2); UCase(tabColumns.descriptors(i).acmAttributeName); " IN ("; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).valueList; ")"
         ElseIf g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue <> "" Or g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength <> "" Or g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue <> "" Then
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue <> "" Then
             If Not isFirstCond Then
               Print #fileNo, addTab(3); "AND"
             End If
             isFirstCond = False
             If tabColumns.descriptors(i).isNullable Then
               Print #fileNo, addTab(2); "COALESCE("; UCase(tabColumns.descriptors(i).acmAttributeName); ", "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue; ") >= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue
             Else
               Print #fileNo, addTab(2); UCase(tabColumns.descriptors(i).acmAttributeName); " >= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue
             End If
           End If
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue <> "" Then
             If Not isFirstCond Then
               Print #fileNo, addTab(3); "AND"
             End If
             isFirstCond = False
             If tabColumns.descriptors(i).isNullable Then
               Print #fileNo, addTab(2); "COALESCE("; UCase(tabColumns.descriptors(i).acmAttributeName); ", "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue; ") >= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue
             Else
               Print #fileNo, addTab(2); UCase(tabColumns.descriptors(i).acmAttributeName); " <= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue
             End If
           End If
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength <> "" Then
             If Not isFirstCond Then
               Print #fileNo, addTab(3); "AND"
             End If
             isFirstCond = False
             If tabColumns.descriptors(i).isNullable Then
               Print #fileNo, addTab(2); "("; UCase(tabColumns.descriptors(i).acmAttributeName); " IS NULL OR LENGTH("; UCase(tabColumns.descriptors(i).acmAttributeName); ") >= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength; ")"
             Else
               Print #fileNo, addTab(2); "LENGTH("; UCase(tabColumns.descriptors(i).acmAttributeName); ") >= "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength
             End If
           End If
         ElseIf Not tabColumns.descriptors(i).isNullable Then
           If Not isFirstCond Then
             Print #fileNo, addTab(3); "AND"
           End If
           isFirstCond = False
           Print #fileNo, addTab(2); UCase(tabColumns.descriptors(i).acmAttributeName); " IS NOT NULL"
         End If
   Next i
 
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "merge with old Status info")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "timestamp,"
   Print #fileNo, addTab(2); "userId,"
   Print #fileNo, addTab(2); "correlationId,"
   Print #fileNo, addTab(2); "callerId,"
   Print #fileNo, addTab(2); "orgOid,"
   Print #fileNo, addTab(2); "psOid,"
   Print #fileNo, addTab(2); "accessMode_ID,"
   Print #fileNo, addTab(2); "lrtOid,"
   Print #fileNo, addTab(2); "subSystem,"
   Print #fileNo, addTab(2); "serverId,"
   Print #fileNo, addTab(2); "threadId,"
   Print #fileNo, addTab(2); "service,"
   Print #fileNo, addTab(2); "action"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "STARTTIME,"
   Print #fileNo, addTab(2); "USERID,"
   Print #fileNo, addTab(2); "CORRELATIONID,"
   Print #fileNo, addTab(2); "CALLERID,"
   Print #fileNo, addTab(2); "ORGOID,"
   Print #fileNo, addTab(2); "PSOID,"
   Print #fileNo, addTab(2); g_anAccessModeId; ","
   Print #fileNo, addTab(2); g_anLrtOid; ","
   Print #fileNo, addTab(2); "SUBSYSTEM,"
   Print #fileNo, addTab(2); "SERVERID,"
   Print #fileNo, addTab(2); "THREADID,"
   Print #fileNo, addTab(2); "SERVICE,"
   Print #fileNo, addTab(2); "'begin'"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameRssStatus
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "ignore records not relevant for Status")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "(ACTION NOT IN ('start', 'stop') AND ACTION NOT LIKE 'begin%' AND ACTION NOT LIKE 'end%')"

   initAttributeTransformation(transformation, 0)
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrListForEntityWithColReuse(g_classIndexRssStatus, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone)

   For i = 1 To tabColumns.numDescriptors
       ' FIXME: HACK
       If UCase(tabColumns.descriptors(i).acmAttributeName) = "STARTTIME" Then
         GoTo NextI
       End If

       Dim attrName As String
       attrName = tabColumns.descriptors(i).acmAttributeName
         If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).valueList <> "" Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); UCase(attrName); " NOT IN ("; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).valueList; ")"
         ElseIf g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue <> "" Or g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength <> "" Or g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue <> "" Then
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue <> "" Then
             Print #fileNo, addTab(3); "OR"
             Print #fileNo, addTab(2); UCase(attrName); " < "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minValue
           End If
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue <> "" Then
             Print #fileNo, addTab(3); "OR"
             Print #fileNo, addTab(2); UCase(attrName); " > "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).maxValue
           End If
           If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength <> "" Then
             Print #fileNo, addTab(3); "OR"
             Print #fileNo, addTab(2); "LENGTH("; UCase(attrName); ") < "; g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).minLength
           End If
         ElseIf Not tabColumns.descriptors(i).isNullable Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); UCase(attrName); " IS NULL"
         End If
 NextI:
   Next i

   Print #fileNo, addTab(3); "OR"
   Print #fileNo, addTab(2); "SERVICE = ''"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "for Status ignore all records prior to last server startup")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelog L"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"

   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "subSystem,"
   Print #fileNo, addTab(6); "serverId,"
   Print #fileNo, addTab(6); "MAX(timestamp) AS timestamp"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SESSION.ivkMessagelog"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "action IN ('start', 'stop')"
   Print #fileNo, addTab(5); "GROUP BY"
   Print #fileNo, addTab(6); "subSystem,"
   Print #fileNo, addTab(6); "serverId"
   Print #fileNo, addTab(4); ") V_ServerReStart"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "V_ServerReStart.timestamp > L.timestamp"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V_ServerReStart.subSystem = L.subSystem"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V_ServerReStart.serverId = L.serverId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "normalize ACTION for Status")
   Print #fileNo, addTab(1); "UPDATE SESSION.ivkMessagelog SET action = 'begin' WHERE action LIKE 'begin_%';"
   Print #fileNo, addTab(1); "UPDATE SESSION.ivkMessagelog SET action = 'end'   WHERE action LIKE 'end_%';"

   genProcSectionHeader(fileNo, "for Status ignore all 'correlated record pairs' of 'begin-end' related to the same subsystem")
   Print #fileNo, addTab(1); "BEGIN ATOMIC"
   genProcSectionHeader(fileNo, "declare variables", 2, True)
   genVarDecl(fileNo, "v_stmntMsgSeqTextPrev", "VARCHAR(2000)", "NULL", 2)
   genVarDecl(fileNo, "v_correlationIdPrev", "VARCHAR(128)", "NULL", 2)
   genVarDecl(fileNo, "v_serverIdPrev", "VARCHAR(64)", "NULL", 2)
   genVarDecl(fileNo, "v_subSystemPrev", "VARCHAR(3)", "NULL", 2)
   genVarDecl(fileNo, "v_servicePrev", "VARCHAR(128)", "NULL", 2)
   genVarDecl(fileNo, "v_actionPrev", "VARCHAR(30)", "NULL", 2)
   Print #fileNo,
   genVarDecl(fileNo, "v_timestampPrev", "TIMESTAMP", "NULL", 2)
   genVarDecl(fileNo, "v_userIdPrev", g_dbtUserId, "NULL", 2)
   genVarDecl(fileNo, "v_callerIdPrev", "VARCHAR(128)", "NULL", 2)
   genVarDecl(fileNo, "v_orgOidPrev", g_dbtOid, "NULL", 2)
   genVarDecl(fileNo, "v_psOidPrev", g_dbtOid, "NULL", 2)
   genVarDecl(fileNo, "v_accessmodeIdPrev", g_dbtEnumId, "NULL", 2)
   genVarDecl(fileNo, "v_lrtOidPrev", g_dbtOid, "NULL", 2)
   genVarDecl(fileNo, "v_threadIdPrev", "VARCHAR(64)", "NULL", 2)
   genVarDecl(fileNo, "v_commonActivityPrev", "VARCHAR(30)", "NULL", 2)
   genVarDecl(fileNo, "v_customTextPrev", "VARCHAR(128)", "NULL", 2)
   Print #fileNo,
   genVarDecl(fileNo, "v_stmntMsgSeqText", "VARCHAR(2000)", "NULL", 2)
   genVarDecl(fileNo, "v_correlationId", "VARCHAR(128)", "NULL", 2)
   genVarDecl(fileNo, "v_serverId", "VARCHAR(64)", "NULL", 2)
   genVarDecl(fileNo, "v_subSystem", "VARCHAR(3)", "NULL", 2)
   genVarDecl(fileNo, "v_service", "VARCHAR(128)", "NULL", 2)
   genVarDecl(fileNo, "v_action", "VARCHAR(30)", "NULL", 2)
 
   genProcSectionHeader(fileNo, "declare statements", 2)
   genVarDecl(fileNo, "v_stmntMsgSeqPrev", "STATEMENT", , 2)
   genVarDecl(fileNo, "v_stmntMsgSeq", "STATEMENT", , 2)
 
   genProcSectionHeader(fileNo, "declare cursor", 2)
   Print #fileNo, addTab(2); "DECLARE msgSeqCursorPrev          CURSOR WITH HOLD FOR v_stmntMsgSeqPrev;"
   Print #fileNo, addTab(2); "DECLARE msgSeqCursor              CURSOR WITH HOLD FOR v_stmntMsgSeq;"
 
   genProcSectionHeader(fileNo, "initialize cursors", 2)
   Print #fileNo, addTab(2); "SET v_stmntMsgSeqTextPrev = 'SELECT ' ||"
   Print #fileNo, addTab(3); "'timestamp, userid, correlationid, callerid, orgoid, psoid, accessmode_id, lrtoid, subsystem, ' ||"
   Print #fileNo, addTab(3); "'serverid, threadid, service, action, commonactivity, customtext ' ||"
   Print #fileNo, addTab(3); "'FROM SESSION.ivkMessagelog ' ||"
   Print #fileNo, addTab(3); "'ORDER BY subsystem, correlationid, serverid, service, timestamp';"
   Print #fileNo, addTab(2); "SET v_stmntMsgSeqText = 'select correlationid, subsystem, serverid, service, action ' ||"
   Print #fileNo, addTab(3); "'FROM SESSION.ivkMessagelog ' ||"
   Print #fileNo, addTab(3); "'ORDER BY subsystem, correlationid, serverid, service, timestamp';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmntMsgSeqPrev FROM v_stmntMsgSeqTextPrev;"
   Print #fileNo, addTab(2); "PREPARE v_stmntMsgSeq     FROM v_stmntMsgSeqText;"
   Print #fileNo, addTab(2); "OPEN msgSeqCursorPrev;"
   Print #fileNo, addTab(2); "OPEN msgSeqCursor;"
   Print #fileNo, addTab(2); "SET v_mySqlCode = NULL;"
 
   genProcSectionHeader(fileNo, "2 * FETCH for 'LookAhead-Cursor'", 2)
   Print #fileNo, addTab(2); "FETCH msgSeqCursor INTO v_correlationId, v_subSystem, v_serverId, v_service, v_action;"
   Print #fileNo, addTab(2); "FETCH msgSeqCursor INTO v_correlationId, v_subSystem, v_serverId, v_service, v_action;"
 
   genProcSectionHeader(fileNo, "1 * FETCH for 'Current Position Cursor'", 2)
   Print #fileNo, addTab(2); "FETCH"
   Print #fileNo, addTab(3); "msgSeqCursorPrev"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,"
   Print #fileNo, addTab(3); "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,"
   Print #fileNo, addTab(3); "v_actionPrev, v_commonActivityPrev, v_customTextPrev"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);"

   Print #fileNo,
   Print #fileNo, addTab(2); "WHILE (v_mySqlCode = 0) DO"
 
   Print #fileNo, addTab(3); "IF ("
   Print #fileNo, addTab(6); "v_correlationId = v_correlationIdPrev"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "v_serverId = v_serverIdPrev"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "v_service = v_servicePrev"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "v_action = 'end'"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "v_actionPrev = 'begin'"
   Print #fileNo, addTab(5); ") THEN"
 
   genProcSectionHeader(fileNo, "found a pair of matching records - ignore both", 4, True)
   genProcSectionHeader(fileNo, "mark 'current' cursor 'invalid' (in case we do not read a value again", 4, True)
   Print #fileNo, addTab(4); "SET v_subSystemPrev = NULL;"
 
   genProcSectionHeader(fileNo, "give both Cursors one extra move forward because we got two records to ignore", 4)
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "msgSeqCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_correlationId, v_subSystem, v_serverId, v_service, v_action"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "msgSeqCursorPrev"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,"
   Print #fileNo, addTab(5); "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,"
   Print #fileNo, addTab(5); "v_actionPrev, v_commonActivityPrev, v_customTextPrev"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); "SESSION.ivkMessagelogConsolidated"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "timestamp,"
   Print #fileNo, addTab(5); "userid,"
   Print #fileNo, addTab(5); "correlationid,"
   Print #fileNo, addTab(5); "callerid,"
   Print #fileNo, addTab(5); "orgoid,"
   Print #fileNo, addTab(5); "psoid,"
   Print #fileNo, addTab(5); "accessmode_id,"
   Print #fileNo, addTab(5); "lrtoid,"
   Print #fileNo, addTab(5); "subsystem,"
   Print #fileNo, addTab(5); "serverid,"
   Print #fileNo, addTab(5); "threadid,"
   Print #fileNo, addTab(5); "service,"
   Print #fileNo, addTab(5); "action,"
   Print #fileNo, addTab(5); "commonactivity,"
   Print #fileNo, addTab(5); "customtext"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(4); "v_timestampPrev,"
   Print #fileNo, addTab(4); "v_userIdPrev,"
   Print #fileNo, addTab(4); "v_correlationIdPrev,"
   Print #fileNo, addTab(4); "v_callerIdPrev,"
   Print #fileNo, addTab(4); "v_orgOidPrev,"
   Print #fileNo, addTab(4); "v_psOidPrev,"
   Print #fileNo, addTab(4); "v_accessmodeIdPrev,"
   Print #fileNo, addTab(4); "v_lrtOidPrev,"
   Print #fileNo, addTab(4); "v_subSystemPrev,"
   Print #fileNo, addTab(4); "v_serverIdPrev,"
   Print #fileNo, addTab(4); "v_threadIdPrev,"
   Print #fileNo, addTab(4); "v_servicePrev,"
   Print #fileNo, addTab(4); "v_actionPrev,"
   Print #fileNo, addTab(4); "v_commonActivityPrev,"
   Print #fileNo, addTab(4); "v_customTextPrev"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "SET v_mySqlCode = NULL;"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader(fileNo, "mark previous cursor 'invalid' (in case we do not read a value again", 3)
   Print #fileNo, addTab(3); "SET v_subSystemPrev = NULL;"
 
   genProcSectionHeader(fileNo, "move both Cursors forward to ignore record", 3)
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "msgSeqCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_correlationId, v_subSystem, v_serverId, v_service, v_action"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "msgSeqCursorPrev"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,"
   Print #fileNo, addTab(4); "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,"
   Print #fileNo, addTab(4); "v_actionPrev, v_commonActivityPrev, v_customTextPrev"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);"

   Print #fileNo, addTab(2); "END WHILE;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_subSystemPrev IS NOT NULL THEN"
   genProcSectionHeader(fileNo, "this record is not yet processed", 3, True)
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.ivkMessagelogConsolidated"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "timestamp,"
   Print #fileNo, addTab(4); "userid,"
   Print #fileNo, addTab(4); "correlationid,"
   Print #fileNo, addTab(4); "callerid,"
   Print #fileNo, addTab(4); "orgoid,"
   Print #fileNo, addTab(4); "psoid,"
   Print #fileNo, addTab(4); "accessmode_id,"
   Print #fileNo, addTab(4); "lrtoid,"
   Print #fileNo, addTab(4); "subsystem,"
   Print #fileNo, addTab(4); "serverid,"
   Print #fileNo, addTab(4); "threadid,"
   Print #fileNo, addTab(4); "service,"
   Print #fileNo, addTab(4); "action,"
   Print #fileNo, addTab(4); "commonactivity,"
   Print #fileNo, addTab(4); "customtext"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_timestampPrev,"
   Print #fileNo, addTab(4); "v_userIdPrev,"
   Print #fileNo, addTab(4); "v_correlationIdPrev,"
   Print #fileNo, addTab(4); "v_callerIdPrev,"
   Print #fileNo, addTab(4); "v_orgOidPrev,"
   Print #fileNo, addTab(4); "v_psOidPrev,"
   Print #fileNo, addTab(4); "v_accessmodeIdPrev,"
   Print #fileNo, addTab(4); "v_lrtOidPrev,"
   Print #fileNo, addTab(4); "v_subSystemPrev,"
   Print #fileNo, addTab(4); "v_serverIdPrev,"
   Print #fileNo, addTab(4); "v_threadIdPrev,"
   Print #fileNo, addTab(4); "v_servicePrev,"
   Print #fileNo, addTab(4); "v_actionPrev,"
   Print #fileNo, addTab(4); "v_commonActivityPrev,"
   Print #fileNo, addTab(4); "v_customTextPrev"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader(fileNo, "close cursor", 2)
   Print #fileNo, addTab(2); "CLOSE msgSeqCursor;"
   Print #fileNo, addTab(2); "CLOSE msgSeqCursorPrev;"
 
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader(fileNo, "delete old Status info")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); g_qualTabNameRssStatus
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "determine current Status info")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); g_qualTabNameRssStatus
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "STARTTIME,"
   Print #fileNo, addTab(2); "USERID,"
   Print #fileNo, addTab(2); "CORRELATIONID,"
   Print #fileNo, addTab(2); "CALLERID,"
   Print #fileNo, addTab(2); "ORGOID,"
   Print #fileNo, addTab(2); "PSOID,"
   Print #fileNo, addTab(2); g_anAccessModeId; ","
   Print #fileNo, addTab(2); g_anLrtOid; ","
   Print #fileNo, addTab(2); "SUBSYSTEM,"
   Print #fileNo, addTab(2); "SERVERID,"
   Print #fileNo, addTab(2); "THREADID,"
   Print #fileNo, addTab(2); "SERVICE"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "timestamp,"
   Print #fileNo, addTab(2); "userId,"
   Print #fileNo, addTab(2); "correlationId,"
   Print #fileNo, addTab(2); "callerId,"
   Print #fileNo, addTab(2); "orgOid,"
   Print #fileNo, addTab(2); "psOid,"
   Print #fileNo, addTab(2); "accessMode_ID,"
   Print #fileNo, addTab(2); "lrtOid,"
   Print #fileNo, addTab(2); "subSystem,"
   Print #fileNo, addTab(2); "serverId,"
   Print #fileNo, addTab(2); "threadId,"
   Print #fileNo, addTab(2); "service"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ivkMessagelogConsolidated LStart"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "LStart.action = 'begin'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.ivkMessagelogConsolidated LStop"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "LStop.timestamp >= LStart.timestamp"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LStart.correlationId = LStop.correlationId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LStart.serverId = LStop.serverId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LStart.service = LStop.service"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LStop.action = 'end'"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "count number of records")
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

   genSpLogProcExit(fileNo, qualProcNameRssGetStatus, ddlType, , "'cdUserId_in", "rowCount_out")

   genProcSectionHeader(fileNo, "commit new status")
   Print #fileNo, addTab(1); "COMMIT;"

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
 
 
 Private Sub genRssSupByPool( _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If

   On Error GoTo ErrorExit
 
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPostProc)
 
   Dim qualProcNameRssGetStatusGlobal As String
   Dim qualProcNameRssGetStatusLocal As String

   ' ####################################################################################################################
   ' #    Procedure retrieving the Running-Service-Server-Status
   ' ####################################################################################################################
   qualProcNameRssGetStatusGlobal = genQualProcName(g_sectionIndexPaiLog, spnRssGetStatus, ddlType)
   qualProcNameRssGetStatusLocal = genQualProcName(g_sectionIndexAliasLrt, spnRssGetStatus, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRssGetStatusLocal
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "-- procedure expects the UserId of the current user being held in register 'CURRENT USER'"
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of status records retrieved")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameRssGetStatusLocal, ddlType, , "rowCount_out")

   genProcSectionHeader(fileNo, "determine current user")
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(RTRIM(LEFT(CURRENT USER, 16)) AS "; g_dbtUserId; ");"

   genProcSectionHeader(fileNo, "call 'global' procedure")
   Print #fileNo, addTab(1); "CALL "; qualProcNameRssGetStatusGlobal; "(v_cdUserId, rowCount_out);"

   genSpLogProcExit(fileNo, qualProcNameRssGetStatusLocal, ddlType, , "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Procedure retrieving the Running-Service-Server-Status
   ' ####################################################################################################################
   qualProcNameRssGetStatusLocal = genQualProcName(g_sectionIndexAliasLrt, spnRssGetStatus, ddlType, thisOrgIndex, thisPoolIndex, , "MBS")
 
   printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRssGetStatusLocal
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, False, "CD User Id of the mdsUser calling this procedure")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameRssGetStatusLocal, ddlType, , "'cdUserId_in")

   genProcSectionHeader(fileNo, "determine current user")
   Print #fileNo, addTab(1); "SET v_cdUserId = COALESCE(v_cdUserId, CAST(RTRIM(LEFT(CURRENT USER, 16)) AS "; g_dbtUserId; "));"

   genProcSectionHeader(fileNo, "call 'global' procedure")
   Print #fileNo, addTab(1); "CALL "; qualProcNameRssGetStatusGlobal; "(v_cdUserId, v_rowCount);"

   genSpLogProcExit(fileNo, qualProcNameRssGetStatusLocal, ddlType, , "'cdUserId_in")

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
 ' ### ENDIF IVK ###
 
