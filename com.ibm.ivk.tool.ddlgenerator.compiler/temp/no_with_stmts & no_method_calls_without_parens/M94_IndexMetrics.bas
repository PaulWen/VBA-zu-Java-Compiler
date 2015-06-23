 Attribute VB_Name = "M94_IndexMetrics"
 Option Explicit
 
 Private Const pc_tempTabNameIndexMetrics = "SESSION.IndexMetrics"
 
 Private Const processingStepAdmin = 4
 
 
 Sub genDbIndexMetricsDdl( _
   ddlType As DdlTypeId _
 )
   If Not supportIndexMetrics Then
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMonitor, processingStepAdmin, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   genDbIndexMetricsDdlUtilities(fileNo, ddlType)
   genDbIndexMetricsDdlGetMetrics(fileNo, ddlType)
   genDbIndexMetricsDdlAnalysis(fileNo, ddlType)
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbIndexMetricsDdlUtilities( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   If generateDdlCreateSeq Then
     ' ####################################################################################################################
     ' #    Sequence for Index Metrics
     ' ####################################################################################################################

     Dim qualSeqNameIndexMetricsId As String
     qualSeqNameIndexMetricsId = genQualSeqName(g_sectionIndexDbMonitor, gc_seqNameIndexMetricsId, ddlType)

     genProcSectionHeader(fileNo, "create sequence for index metrics IDs")
     Print #fileNo, addTab(0); "CREATE SEQUENCE"
     Print #fileNo, addTab(1); qualSeqNameIndexMetricsId; " AS "; g_dbtSequence
     Print #fileNo, addTab(0); "START WITH"
     Print #fileNo, addTab(1); "1"
     Print #fileNo, addTab(0); "INCREMENT BY"
     Print #fileNo, addTab(1); "1"
     Print #fileNo, addTab(0); "NO CYCLE"
     Print #fileNo, addTab(0); "CACHE 500"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbIndexMetricsDdlGetMetrics( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim qualSeqNameIndexMetricsId As String
   qualSeqNameIndexMetricsId = genQualSeqName(g_sectionIndexDbMonitor, gc_seqNameIndexMetricsId, ddlType)
 
   Dim qualProcNameGetIndexMetrics As String
 
   ' ####################################################################################################################
   ' #    SP for retrieving Index Metrics data
   ' ####################################################################################################################

   qualProcNameGetIndexMetrics = genQualProcName(g_sectionIndexDbMonitor, spnGetIndexMetrics, ddlType)

   printSectionHeader("SP for retrieving Index Metrics data", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetrics
   Print #fileNo, addTab(0); "("
 
   genProcParm(fileNo, "INOUT", "metricsId_inout", "BIGINT", True, "(optional) ID of the index metrics")
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'1' retrieve index metrics and list result, '2' retrieve metrics only")
   genProcParm(fileNo, "IN", "onlyUsedIndexes_in", "INTEGER", True, "iff '1' retrieve metrics for all indexes with usage-count > 0, otherwise for all indexes")
 
   genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern) of the table(s) to collect index metrics for")
   genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the table(s) to collect index metrics for")
   genProcParm(fileNo, "IN", "indSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern)of the index(es) to collect index metrics for")
   genProcParm(fileNo, "IN", "indNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the index(es) to collect index metrics for")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records collected")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_stmntText", "VARCHAR(1000)", "NULL")
   genVarDecl(fileNo, "v_now", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_db2Release", g_dbtDbRelease, "NULL")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(_
     fileNo, qualProcNameGetIndexMetrics, ddlType, , "metricsId_inout", "mode_in", "onlyUsedIndexes_in", _
     "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out")
 
   genProcSectionHeader(fileNo, "initalize variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader(fileNo, "determine whether DB supports MON_GET_INDEX")
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.07) THEN"
   genSpLogProcEscape(_
     fileNo, qualProcNameGetIndexMetrics, ddlType, 2, "metricsId_inout", "mode_in", "onlyUsedIndexes_in", _
     "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out")
   genSignalDdlWithParms("featureNotSupported", fileNo, 2, "INDEXMETRICS MONITORING", "9.7")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_now = CURRENT TIMESTAMP;"
 
   genProcSectionHeader(fileNo, "collect metrics data")
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   genProcSectionHeader(fileNo, "create metrics ID if none is provided", 2, True)
   Print #fileNo, addTab(2); "IF metricsId_inout IS NULL THEN"
   Print #fileNo, addTab(3); "SET metricsId_inout = NEXTVAL FOR "; qualSeqNameIndexMetricsId; ";"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader(fileNo, "for each table do")
   Print #fileNo, addTab(2); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT DISTINCT"
   Print #fileNo, addTab(4); "(CASE WHEN tabSchemaNamePattern_in IS NULL THEN NULL ELSE RTRIM(T.TABSCHEMA) END) AS c_schemaName,"
   Print #fileNo, addTab(4); "(CASE WHEN tabNamePattern_in       IS NULL THEN NULL ELSE T.TABNAME   END) AS c_tabName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES T"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "RTRIM(T.TABSCHEMA) LIKE COALESCE(tabSchemaNamePattern_in, '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABNAME LIKE COALESCE(tabNamePattern_in, '%')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TYPE = 'T'"
   Print #fileNo, addTab(2); "DO"
   genProcSectionHeader(fileNo, "insert index usage records into INDEXMETRICS table", 3, True)
   Print #fileNo, addTab(3); "SET v_stmntText ="
   Print #fileNo, addTab(4); "'INSERT INTO ' ||"
   Print #fileNo, addTab(5); "'"; g_qualTabNameIndexMetrics; "' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'MID,' ||"
   Print #fileNo, addTab(5); "'METRICS_TIMESTAMP,' ||"
   Print #fileNo, addTab(5); "'TBSPACEID,' ||"
   Print #fileNo, addTab(5); "'TABLEID,' ||"
   Print #fileNo, addTab(5); "'INDEXID,' ||"
   Print #fileNo, addTab(5); "'PARTITIONID,' ||"
   Print #fileNo, addTab(5); "'NUMSCANS,' ||"
   Print #fileNo, addTab(5); "'NUMSCANSINDEXONLY,' ||"
   Print #fileNo, addTab(5); "'INDSCHEMA,' ||"
   Print #fileNo, addTab(5); "'INDNAME,' ||"
   Print #fileNo, addTab(5); "'TABSCHEMA,' ||"
   Print #fileNo, addTab(5); "'TABNAME' ||"
   Print #fileNo, addTab(4); "')' ||"
   Print #fileNo, addTab(4); "'SELECT ' ||"
   Print #fileNo, addTab(5); "RTRIM(CHAR(metricsId_inout)) || ',' ||"
   Print #fileNo, addTab(5); "'''' || v_now || ''',' ||"
   Print #fileNo, addTab(5); "'B.TBSPACEID,' ||"
   Print #fileNo, addTab(5); "'B.TABLEID,' ||"
   Print #fileNo, addTab(5); "'T.IID,' ||"
   Print #fileNo, addTab(5); "'T.DATA_PARTITION_ID,' ||"
   Print #fileNo, addTab(5); "'T.INDEX_SCANS,' ||"
   Print #fileNo, addTab(5); "'T.INDEX_ONLY_SCANS,' ||"
   Print #fileNo, addTab(5); "'S.INDSCHEMA,' ||"
   Print #fileNo, addTab(5); "'S.INDNAME,' ||"
   Print #fileNo, addTab(5); "'T.TABSCHEMA,' ||"
   Print #fileNo, addTab(5); "'T.TABNAME ' ||"
   Print #fileNo, addTab(4); "'FROM ' ||"
   Print #fileNo, addTab(5); "'TABLE(SYSPROC.MON_GET_INDEX(' || COALESCE('''' || c_schemaName || '''', 'NULL') || ', ' || COALESCE('''' || c_tabName || '''', 'NULL') || ', -2)) AS T ' ||"
   Print #fileNo, addTab(4); "'INNER JOIN ' ||"
   Print #fileNo, addTab(5); "'SYSCAT.INDEXES AS S ' ||"
   Print #fileNo, addTab(4); "'ON ' ||"
   Print #fileNo, addTab(5); "'T.TABSCHEMA = S.TABSCHEMA ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'T.TABNAME = S.TABNAME ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'T.IID = S.IID ' ||"
   Print #fileNo, addTab(4); "'INNER JOIN ' ||"
   Print #fileNo, addTab(5); "'SYSCAT.TABLES AS B ' ||"
   Print #fileNo, addTab(4); "'ON ' ||"
   Print #fileNo, addTab(5); "'T.TABSCHEMA = B.TABSCHEMA ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'T.TABNAME = B.TABNAME ' ||"
   Print #fileNo, addTab(4); "'WHERE ' ||"
   Print #fileNo, addTab(5); "'S.INDSCHEMA LIKE ''' || COALESCE(indSchemaNamePattern_in, '"; g_allSchemaNamePattern; "') || ''' ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'S.INDNAME LIKE ''' || COALESCE(indNamePattern_in, '%') || ''' ' ||"
   Print #fileNo, addTab(5); "(CASE WHEN onlyUsedIndexes_in = 1 THEN 'AND T.INDEX_SCANS > 0' ELSE '' END)"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntText;"
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "ELSE"
   genProcSectionHeader(fileNo, "use 'last' metric ID if none is provided", 2, True)
   Print #fileNo, addTab(2); "IF metricsId_inout IS NULL THEN"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "MAX(MID)"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "metricsId_inout"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameIndexMetrics
   Print #fileNo, addTab(3); "WITH UR;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "*"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameIndexMetrics
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "MID = metricsId_inout"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(_
     fileNo, qualProcNameGetIndexMetrics, ddlType, 1, "metricsId_inout", "mode_in", "onlyUsedIndexes_in", _
     "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP for retrieving Index Metrics data", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetrics
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'1' retrieve index metrics and list result, '2' retrieve metrics only")
   genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern) of the table(s) to collect index metrics for")
   genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the table(s) to collect index metrics for")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records collected")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_metricsId", "BIGINT", "NULL")
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetrics, ddlType, , "mode_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "rowCount_out")

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetrics; "(v_metricsId, mode_in, 1, tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit(fileNo, qualProcNameGetIndexMetrics, ddlType, , "mode_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP for retrieving Index Metrics data", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetrics
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "INTEGER", True, "'1' retrieve index metrics and list result, '2' retrieve metrics only")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records collected")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_metricsId", "BIGINT", "NULL")
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetrics, ddlType, , "mode_in", "rowCount_out")

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetrics; "(v_metricsId, mode_in, 1, NULL, NULL, NULL, NULL, rowCount_out);"

   genSpLogProcExit(fileNo, qualProcNameGetIndexMetrics, ddlType, , "mode_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbIndexMetricsDdlAnalysis( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim qualProcNameGetIndexMetricsAnalysis As String

   ' ####################################################################################################################
   ' #    SP for analyzing Index Metrics data
   ' ####################################################################################################################

   qualProcNameGetIndexMetricsAnalysis = genQualProcName(g_sectionIndexDbMonitor, spnGetIndexMetricsAnalysis, ddlType)

   printSectionHeader("SP for analyzing Index Metrics data", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetricsAnalysis
   Print #fileNo, addTab(0); "("
 
   genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern) of the table(s) to analyze")
   genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the table(s) to analyze")
   genProcParm(fileNo, "IN", "indSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern)of the index(es) to analyze")
   genProcParm(fileNo, "IN", "indNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the index(es) to analyze")
   genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", False, "(optional) specifies time granularity to analyze (default: hour 'HH')")
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_colList", "VARCHAR(2048)", "''")
   genVarDecl(fileNo, "v_db2Release", g_dbtDbRelease, "NULL")
   genVarDecl(fileNo, "v_tsFormat", "VARCHAR(64)", "NULL")
   genVarDecl(fileNo, "v_dbRestarted", g_dbtBoolean, "NULL")
   genVarDecl(fileNo, "v_lastMetricsTimestamp", "VARCHAR(64)", "NULL")
   genVarDecl(fileNo, "v_lastNumScans", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_lastNumScansIndexOnly", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_deltaNumScans", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_deltaNumScansIndexOnly", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_totalNumScans", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_totalNumScansIndexOnly", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_lastMetNumScans", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_lastMetNumScansIndexOnly", "BIGINT", "NULL")

   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in", _
                             "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in")
 
   genProcSectionHeader(fileNo, "determine granularity")
   Print #fileNo, addTab(1); "SET granularity_in = COALESCE(UPPER(granularity_in), 'HH');"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF POSSTR(granularity_in, 'MI') > 0 THEN"
   Print #fileNo, addTab(2); "SET v_tsFormat = 'YYYY-MM-DD HH24:MI';"
   Print #fileNo, addTab(1); "ELSEIF POSSTR(granularity_in, 'HH') > 0 THEN"
   Print #fileNo, addTab(2); "SET v_tsFormat = 'YYYY-MM-DD HH24';"
   Print #fileNo, addTab(1); "ELSEIF POSSTR(granularity_in, 'DD') > 0 THEN"
   Print #fileNo, addTab(2); "SET v_tsFormat = 'YYYY-MM-DD';"
   Print #fileNo, addTab(1); "ELSEIF POSSTR(granularity_in, 'MM') > 0 THEN"
   Print #fileNo, addTab(2); "SET v_tsFormat = 'YYYY-MM';"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_tsFormat = 'YYYY';"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "declare temporary table")
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); pc_tempTabNameIndexMetrics
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "tableSchema       "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(2); "tableName         VARCHAR(100),"
   Print #fileNo, addTab(2); "indexSchema       "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(2); "indexName         VARCHAR(100),"
   Print #fileNo, addTab(2); "partitionId       INTEGER,"
   Print #fileNo, addTab(2); "timeGranule       VARCHAR(20),"
   Print #fileNo, addTab(2); "scans             INTEGER,"
   Print #fileNo, addTab(2); "scansIndexOnly    INTEGER,"
   Print #fileNo, addTab(2); "sumScans          INTEGER,"
   Print #fileNo, addTab(2); "sumScansIndexOnly INTEGER,"
   Print #fileNo, addTab(2); "dbRestarted       "; g_dbtBoolean
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "ON ROLLBACK PRESERVE ROWS"
   Print #fileNo, addTab(1); "WITH REPLACE;"
 
   genProcSectionHeader(fileNo, "determine whether DB supports MON_GET_INDEX")
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.07) THEN"
   genSpLogProcEscape(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, 2, "'tabSchemaNamePattern_in", "'tabNamePattern_in", _
                             "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in")
   genSignalDdlWithParms("featureNotSupported", fileNo, 2, "INDEXMETRICS MONITORING", "9.7")
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "loop over indexes")
   Print #fileNo, addTab(1); "FOR idxLoop AS idxCur CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "LEFT(TABSCHEMA, 30) AS c_tabSchema,"
   Print #fileNo, addTab(3); "LEFT(TABNAME,  100) AS c_tabName,"
   Print #fileNo, addTab(3); "LEFT(INDSCHEMA, 30) AS c_indSchema,"
   Print #fileNo, addTab(3); "LEFT(INDNAME,  100) AS c_indName,"
   Print #fileNo, addTab(3); "PARTITIONID         AS c_partitionId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameIndexMetrics
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(TABSCHEMA) LIKE COALESCE(tabSchemaNamePattern_in, '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(TABNAME) LIKE COALESCE(tabNamePattern_in, '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(INDSCHEMA) LIKE COALESCE(indSchemaNamePattern_in, '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(INDNAME) LIKE COALESCE(indNamePattern_in, '%')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "TABNAME,"
   Print #fileNo, addTab(3); "TABSCHEMA,"
   Print #fileNo, addTab(3); "INDSCHEMA,"
   Print #fileNo, addTab(3); "INDNAME,"
   Print #fileNo, addTab(3); "PARTITIONID"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_lastMetricsTimestamp     = NULL;"
   Print #fileNo, addTab(2); "SET v_lastNumScans             = 0;"
   Print #fileNo, addTab(2); "SET v_lastNumScansIndexOnly    = 0;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_lastMetNumScans          = 0;"
   Print #fileNo, addTab(2); "SET v_lastMetNumScansIndexOnly = 0;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_totalNumScans            = 0;"
   Print #fileNo, addTab(2); "SET v_totalNumScansIndexOnly   = 0;"
   Print #fileNo, addTab(2); "SET v_deltaNumScans            = 0;"
   Print #fileNo, addTab(2); "SET v_deltaNumScansIndexOnly   = 0;"
   Print #fileNo, addTab(2); "SET v_dbRestarted              = "; gc_dbFalse; ";"
 
   genProcSectionHeader(fileNo, "accumulate metrics-information for this index", 2)
   Print #fileNo, addTab(2); "FOR metricsLoop AS"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V_IndexMetrics"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "mid,"
   Print #fileNo, addTab(4); "metricsTimestamp,"
   Print #fileNo, addTab(4); "indexId,"
   Print #fileNo, addTab(4); "partitionId,"
   Print #fileNo, addTab(4); "numScans,"
   Print #fileNo, addTab(4); "numScansIndexOnly"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "MID,"
   Print #fileNo, addTab(5); "VARCHAR_FORMAT(METRICS_TIMESTAMP, v_tsFormat),"
   Print #fileNo, addTab(5); "INDEXID,"
   Print #fileNo, addTab(5); "PARTITIONID,"
   Print #fileNo, addTab(5); "NUMSCANS,"
   Print #fileNo, addTab(5); "NUMSCANSINDEXONLY"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameIndexMetrics
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "TABSCHEMA = c_tabSchema"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "TABNAME = c_tabName"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "INDSCHEMA = c_indSchema"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "INDNAME = c_indName"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(PARTITIONID,-1) = COALESCE(c_partitionId,-1)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "mid               AS c_mid,"
   Print #fileNo, addTab(4); "metricsTimestamp  AS c_metricsTimestamp,"
   Print #fileNo, addTab(4); "numScans          AS c_numScans,"
   Print #fileNo, addTab(4); "numScansIndexOnly AS c_numScansIndexOnly"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_IndexMetrics"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "metricsTimestamp"
   Print #fileNo, addTab(2); "DO"
 
   genProcSectionHeader(fileNo, "if this metricsTimestampGranule differs from previous one, store accumulated values", 3, True)
   Print #fileNo, addTab(3); "IF COALESCE(v_lastMetricsTimestamp, c_metricsTimestamp) <> c_metricsTimestamp THEN"
   genProcSectionHeader(fileNo, "insert into temporary table", 4, True)
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); pc_tempTabNameIndexMetrics
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "tableSchema,"
   Print #fileNo, addTab(5); "tableName,"
   Print #fileNo, addTab(5); "indexSchema,"
   Print #fileNo, addTab(5); "indexName,"
   Print #fileNo, addTab(5); "partitionId,"
   Print #fileNo, addTab(5); "timeGranule,"
   Print #fileNo, addTab(5); "scans,"
   Print #fileNo, addTab(5); "scansIndexOnly,"
   Print #fileNo, addTab(5); "sumScans,"
   Print #fileNo, addTab(5); "sumScansIndexOnly,"
   Print #fileNo, addTab(5); "dbRestarted"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "c_tabSchema,"
   Print #fileNo, addTab(5); "c_tabName,"
   Print #fileNo, addTab(5); "c_indSchema,"
   Print #fileNo, addTab(5); "c_indName,"
   Print #fileNo, addTab(5); "c_partitionId,"
   Print #fileNo, addTab(5); "v_lastMetricsTimestamp,"
   Print #fileNo, addTab(5); "(CASE v_deltaNumScans WHEN 0 THEN NULL ELSE v_deltaNumScans END),"
   Print #fileNo, addTab(5); "(CASE v_deltaNumScansIndexOnly WHEN 0 THEN NULL ELSE v_deltaNumScansIndexOnly END),"
   Print #fileNo, addTab(5); "v_totalNumScans,"
   Print #fileNo, addTab(5); "v_totalNumScansIndexOnly,"
   Print #fileNo, addTab(5); "v_dbRestarted"
   Print #fileNo, addTab(4); ");"
   Print #fileNo,
   Print #fileNo, addTab(4); "SET v_lastMetNumScans          = v_totalNumScans;"
   Print #fileNo, addTab(4); "SET v_lastMetNumScansIndexOnly = v_totalNumScansIndexOnly;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "IF (c_numScans < v_lastNumScans) OR (c_numScansIndexOnly < v_lastNumScansIndexOnly) THEN"
   Print #fileNo, addTab(4); "SET v_deltaNumScans            = c_numScans;"
   Print #fileNo, addTab(4); "SET v_deltaNumScansIndexOnly   = c_numScansIndexOnly;"
   Print #fileNo, addTab(4); "SET v_totalNumScans            = c_numScans;"
   Print #fileNo, addTab(4); "SET v_totalNumScansIndexOnly   = c_numScansIndexOnly;"
   Print #fileNo, addTab(4); "SET v_lastMetNumScans          = 0;"
   Print #fileNo, addTab(4); "SET v_lastMetNumScansIndexOnly = 0;"
   Print #fileNo, addTab(4); "SET v_dbRestarted              = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_deltaNumScans            = c_numScans          - v_lastMetNumScans;"
   Print #fileNo, addTab(4); "SET v_deltaNumScansIndexOnly   = c_numScansIndexOnly - v_lastMetNumScansIndexOnly;"
   Print #fileNo, addTab(4); "SET v_totalNumScans            = c_numScans;"
   Print #fileNo, addTab(4); "SET v_totalNumScansIndexOnly   = c_numScansIndexOnly;"
   Print #fileNo, addTab(4); "SET v_dbRestarted              = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastMetricsTimestamp  = c_metricsTimestamp;"
   Print #fileNo, addTab(3); "SET v_lastNumScans          = c_numScans;"
   Print #fileNo, addTab(3); "SET v_lastNumScansIndexOnly = c_numScansIndexOnly;"
   Print #fileNo, addTab(2); "END FOR;"
 
   genProcSectionHeader(fileNo, "insert last record for this index into temporary table", 2)
   Print #fileNo, addTab(2); "IF v_lastMetricsTimestamp IS NOT NULL THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); pc_tempTabNameIndexMetrics
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "tableSchema,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "indexSchema,"
   Print #fileNo, addTab(4); "indexName,"
   Print #fileNo, addTab(4); "partitionId,"
   Print #fileNo, addTab(4); "timeGranule,"
   Print #fileNo, addTab(4); "scans,"
   Print #fileNo, addTab(4); "scansIndexOnly,"
   Print #fileNo, addTab(4); "sumScans,"
   Print #fileNo, addTab(4); "sumScansIndexOnly,"
   Print #fileNo, addTab(4); "dbRestarted"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_tabSchema,"
   Print #fileNo, addTab(4); "c_tabName,"
   Print #fileNo, addTab(4); "c_indSchema,"
   Print #fileNo, addTab(4); "c_indName,"
   Print #fileNo, addTab(4); "c_partitionId,"
   Print #fileNo, addTab(4); "v_lastMetricsTimestamp,"
   Print #fileNo, addTab(4); "(CASE v_deltaNumScans WHEN 0 THEN NULL ELSE v_deltaNumScans END),"
   Print #fileNo, addTab(4); "(CASE v_deltaNumScansIndexOnly WHEN 0 THEN NULL ELSE v_deltaNumScansIndexOnly END),"
   Print #fileNo, addTab(4); "v_totalNumScans,"
   Print #fileNo, addTab(4); "v_totalNumScansIndexOnly,"
   Print #fileNo, addTab(4); "v_dbRestarted"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader(fileNo, "return result to application", 1)
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader(fileNo, "declare cursor", 2, True)
   Print #fileNo, addTab(2); "DECLARE iuCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT UNIQUE"
   Print #fileNo, addTab(4); "timeGranule,"
   Print #fileNo, addTab(4); "tableSchema,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "indexSchema,"
   Print #fileNo, addTab(4); "indexName,"
   Print #fileNo, addTab(4); "partitionId,"
   Print #fileNo, addTab(4); "scans,"
   Print #fileNo, addTab(4); "scansIndexOnly,"
   Print #fileNo, addTab(4); "sumScans,"
   Print #fileNo, addTab(4); "sumScansIndexOnly"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameIndexMetrics
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "timeGranule,"
   Print #fileNo, addTab(4); "tableSchema,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "indexSchema,"
   Print #fileNo, addTab(4); "indexName,"
   Print #fileNo, addTab(4); "partitionId"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader(fileNo, "leave cursor open for application", 2)
   Print #fileNo, addTab(2); "OPEN iuCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in", _
                             "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader("SP for analyzing Index Metrics data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetricsAnalysis
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern) of the table(s) to analyze")
   genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", True, "(optional) name (-pattern) of the table(s) to analyze")
   genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", False, "(optional) specifies time granularity to analyze (default: hour 'HH')")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)

   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'granularity_in")

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetricsAnalysis; "(tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, granularity_in);"
 
   genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'granularity_in")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP for analyzing Index Metrics data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetricsAnalysis
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", g_dbtDbSchemaName, True, "(optional) schema (-pattern) of the table(s) to analyze")
   genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", False, "(optional) name (-pattern) of the table(s) to analyze")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in")

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetricsAnalysis; "(tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, NULL);"
 
   genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'tabSchemaNamePattern_in", "'tabNamePattern_in")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP for analyzing Index Metrics data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetricsAnalysis
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", False, "(optional) specifies time granularity to analyze (default: hour 'HH')")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'granularity_in")

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetricsAnalysis; "(NULL, NULL, NULL, NULL, granularity_in);"
 
   genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, , "'granularity_in")
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader("SP for analyzing Index Metrics data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameGetIndexMetricsAnalysis
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType)

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameGetIndexMetricsAnalysis; "(NULL, NULL, NULL, NULL, NULL);"
 
   genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType)
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
