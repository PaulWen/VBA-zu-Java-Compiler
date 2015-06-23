package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M94_IndexMetrics {




private static final String pc_tempTabNameIndexMetrics = "SESSION.IndexMetrics";

private static final int processingStepAdmin = 4;


public static void genDbIndexMetricsDdl(Integer ddlType) {
if (!(M03_Config.supportIndexMetrics)) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMonitor, processingStepAdmin, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

genDbIndexMetricsDdlUtilities(fileNo, ddlType);
genDbIndexMetricsDdlGetMetrics(fileNo, ddlType);
genDbIndexMetricsDdlAnalysis(fileNo, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDbIndexMetricsDdlUtilities(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

if (M03_Config.generateDdlCreateSeq) {
// ####################################################################################################################
// #    Sequence for Index Metrics
// ####################################################################################################################

String qualSeqNameIndexMetricsId;
qualSeqNameIndexMetricsId = M04_Utilities.genQualSeqName(M01_Globals.g_sectionIndexDbMonitor, M01_LDM.gc_seqNameIndexMetricsId, ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "create sequence for index metrics IDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE SEQUENCE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualSeqNameIndexMetricsId + " AS " + M01_Globals.g_dbtSequence);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "START WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCREMENT BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CYCLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CACHE 500");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDbIndexMetricsDdlGetMetrics(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (M03_Config.generateFwkTest) {
return;
}

//On Error GoTo ErrorExit 

String qualSeqNameIndexMetricsId;
qualSeqNameIndexMetricsId = M04_Utilities.genQualSeqName(M01_Globals.g_sectionIndexDbMonitor, M01_LDM.gc_seqNameIndexMetricsId, ddlType, null, null, null, null, null, null);

String qualProcNameGetIndexMetrics;

// ####################################################################################################################
// #    SP for retrieving Index Metrics data
// ####################################################################################################################

qualProcNameGetIndexMetrics = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetIndexMetrics, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for retrieving Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "INOUT", "metricsId_inout", "BIGINT", true, "(optional) ID of the index metrics");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'1' retrieve index metrics and list result, '2' retrieve metrics only");
M11_LRT.genProcParm(fileNo, "IN", "onlyUsedIndexes_in", "INTEGER", true, "iff '1' retrieve metrics for all indexes with usage-count > 0, otherwise for all indexes");

M11_LRT.genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern) of the table(s) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the table(s) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "IN", "indSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern)of the index(es) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "IN", "indNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the index(es) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records collected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_now", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetrics, ddlType, null, "metricsId_inout", "mode_in", "onlyUsedIndexes_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out", null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initalize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports MON_GET_INDEX", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.07) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameGetIndexMetrics, ddlType, 2, "metricsId_inout", "mode_in", "onlyUsedIndexes_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out", null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "INDEXMETRICS MONITORING", "9.7", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_now = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "collect metrics data", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "create metrics ID if none is provided", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF metricsId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET metricsId_inout = NEXTVAL FOR " + qualSeqNameIndexMetricsId + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "for each table do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN tabSchemaNamePattern_in IS NULL THEN NULL ELSE RTRIM(T.TABSCHEMA) END) AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN tabNamePattern_in       IS NULL THEN NULL ELSE T.TABNAME   END) AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(tabSchemaNamePattern_in, '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.TABNAME LIKE COALESCE(tabNamePattern_in, '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.TYPE = 'T'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "insert index usage records into INDEXMETRICS table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntText =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_qualTabNameIndexMetrics + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'MID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'METRICS_TIMESTAMP,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TBSPACEID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TABLEID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'INDEXID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'PARTITIONID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NUMSCANS,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NUMSCANSINDEXONLY,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'INDSCHEMA,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'INDNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TABSCHEMA,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(metricsId_inout)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || v_now || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'B.TBSPACEID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'B.TABLEID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.IID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.DATA_PARTITION_ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.INDEX_SCANS,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.INDEX_ONLY_SCANS,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.INDSCHEMA,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.INDNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABSCHEMA,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TABLE(SYSPROC.MON_GET_INDEX(' || COALESCE('''' || c_schemaName || '''', 'NULL') || ', ' || COALESCE('''' || c_tabName || '''', 'NULL') || ', -2)) AS T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SYSCAT.INDEXES AS S ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABSCHEMA = S.TABSCHEMA ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABNAME = S.TABNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.IID = S.IID ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SYSCAT.TABLES AS B ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABSCHEMA = B.TABSCHEMA ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T.TABNAME = B.TABNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.INDSCHEMA LIKE ''' || COALESCE(indSchemaNamePattern_in, '" + M01_Globals.g_allSchemaNamePattern + "') || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.INDNAME LIKE ''' || COALESCE(indNamePattern_in, '%') || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN onlyUsedIndexes_in = 1 THEN 'AND T.INDEX_SCANS > 0' ELSE '' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "use 'last' metric ID if none is provided", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF metricsId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(MID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "metricsId_inout");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MID = metricsId_inout");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetrics, ddlType, 1, "metricsId_inout", "mode_in", "onlyUsedIndexes_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "rowCount_out", null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'1' retrieve index metrics and list result, '2' retrieve metrics only");
M11_LRT.genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern) of the table(s) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the table(s) to collect index metrics for");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records collected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_metricsId", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetrics, ddlType, null, "mode_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetrics + "(v_metricsId, mode_in, 1, tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetrics, ddlType, null, "mode_in", "'tabSchemaNamePattern_in", "'tabNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'1' retrieve index metrics and list result, '2' retrieve metrics only");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records collected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_metricsId", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetrics, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetrics + "(v_metricsId, mode_in, 1, NULL, NULL, NULL, NULL, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetrics, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDbIndexMetricsDdlAnalysis(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
// we do not support this for LDM
return;
}

//On Error GoTo ErrorExit 

String qualProcNameGetIndexMetricsAnalysis;

// ####################################################################################################################
// #    SP for analyzing Index Metrics data
// ####################################################################################################################

qualProcNameGetIndexMetricsAnalysis = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetIndexMetricsAnalysis, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for analyzing Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetricsAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern) of the table(s) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the table(s) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "indSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern)of the index(es) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "indNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the index(es) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", false, "(optional) specifies time granularity to analyze (default: hour 'HH')");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_colList", "VARCHAR(2048)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsFormat", "VARCHAR(64)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dbRestarted", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastMetricsTimestamp", "VARCHAR(64)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastNumScans", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastNumScansIndexOnly", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_deltaNumScans", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_deltaNumScansIndexOnly", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_totalNumScans", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_totalNumScansIndexOnly", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastMetNumScans", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastMetNumScansIndexOnly", "BIGINT", "NULL", null, null);

M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine granularity", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET granularity_in = COALESCE(UPPER(granularity_in), 'HH');");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF POSSTR(granularity_in, 'MI') > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tsFormat = 'YYYY-MM-DD HH24:MI';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF POSSTR(granularity_in, 'HH') > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tsFormat = 'YYYY-MM-DD HH24';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF POSSTR(granularity_in, 'DD') > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tsFormat = 'YYYY-MM-DD';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF POSSTR(granularity_in, 'MM') > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tsFormat = 'YYYY-MM';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tsFormat = 'YYYY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "declare temporary table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "tableSchema       " + M01_Globals.g_dbtDbSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "tableName         VARCHAR(100),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "indexSchema       " + M01_Globals.g_dbtDbSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "indexName         VARCHAR(100),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "partitionId       INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "timeGranule       VARCHAR(20),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "scans             INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "scansIndexOnly    INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sumScans          INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sumScansIndexOnly INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dbRestarted       " + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON ROLLBACK PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports MON_GET_INDEX", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.07) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, 2, "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "INDEXMETRICS MONITORING", "9.7", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over indexes", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR idxLoop AS idxCur CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(TABSCHEMA, 30) AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(TABNAME,  100) AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(INDSCHEMA, 30) AS c_indSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(INDNAME,  100) AS c_indName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARTITIONID         AS c_partitionId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(TABSCHEMA) LIKE COALESCE(tabSchemaNamePattern_in, '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(TABNAME) LIKE COALESCE(tabNamePattern_in, '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(INDSCHEMA) LIKE COALESCE(indSchemaNamePattern_in, '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(INDNAME) LIKE COALESCE(indNamePattern_in, '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INDSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INDNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARTITIONID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lastMetricsTimestamp     = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lastNumScans             = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lastNumScansIndexOnly    = 0;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lastMetNumScans          = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lastMetNumScansIndexOnly = 0;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_totalNumScans            = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_totalNumScansIndexOnly   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_deltaNumScans            = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_deltaNumScansIndexOnly   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_dbRestarted              = " + M01_LDM.gc_dbFalse + ";");

M11_LRT.genProcSectionHeader(fileNo, "accumulate metrics-information for this index", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR metricsLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_IndexMetrics");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "metricsTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "numScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "numScansIndexOnly");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VARCHAR_FORMAT(METRICS_TIMESTAMP, v_tsFormat),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INDEXID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PARTITIONID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NUMSCANS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NUMSCANSINDEXONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABSCHEMA = c_tabSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABNAME = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INDSCHEMA = c_indSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INDNAME = c_indName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(PARTITIONID,-1) = COALESCE(c_partitionId,-1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mid               AS c_mid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "metricsTimestamp  AS c_metricsTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "numScans          AS c_numScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "numScansIndexOnly AS c_numScansIndexOnly");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_IndexMetrics");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "metricsTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "if this metricsTimestampGranule differs from previous one, store accumulated values", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_lastMetricsTimestamp, c_metricsTimestamp) <> c_metricsTimestamp THEN");
M11_LRT.genProcSectionHeader(fileNo, "insert into temporary table", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "tableSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "indexSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "indexName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "timeGranule,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "scans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "scansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sumScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sumScansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "dbRestarted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_indSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_indName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_lastMetricsTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE v_deltaNumScans WHEN 0 THEN NULL ELSE v_deltaNumScans END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE v_deltaNumScansIndexOnly WHEN 0 THEN NULL ELSE v_deltaNumScansIndexOnly END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_totalNumScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_totalNumScansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_dbRestarted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_lastMetNumScans          = v_totalNumScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_lastMetNumScansIndexOnly = v_totalNumScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF (c_numScans < v_lastNumScans) OR (c_numScansIndexOnly < v_lastNumScansIndexOnly) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_deltaNumScans            = c_numScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_deltaNumScansIndexOnly   = c_numScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_totalNumScans            = c_numScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_totalNumScansIndexOnly   = c_numScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_lastMetNumScans          = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_lastMetNumScansIndexOnly = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_dbRestarted              = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_deltaNumScans            = c_numScans          - v_lastMetNumScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_deltaNumScansIndexOnly   = c_numScansIndexOnly - v_lastMetNumScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_totalNumScans            = c_numScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_totalNumScansIndexOnly   = c_numScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_dbRestarted              = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastMetricsTimestamp  = c_metricsTimestamp;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastNumScans          = c_numScans;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastNumScansIndexOnly = c_numScansIndexOnly;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "insert last record for this index into temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lastMetricsTimestamp IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "timeGranule,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "scans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "scansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sumScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sumScansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dbRestarted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_indSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_indName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lastMetricsTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE v_deltaNumScans WHEN 0 THEN NULL ELSE v_deltaNumScans END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE v_deltaNumScansIndexOnly WHEN 0 THEN NULL ELSE v_deltaNumScansIndexOnly END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_totalNumScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_totalNumScansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_dbRestarted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE iuCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT UNIQUE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "timeGranule,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "partitionId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "scans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "scansIndexOnly,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sumScans,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sumScansIndexOnly");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameIndexMetrics);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "timeGranule,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "indexName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "partitionId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN iuCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'indSchemaNamePattern_in", "'indNamePattern_in", "'granularity_in", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetricsAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern) of the table(s) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", true, "(optional) name (-pattern) of the table(s) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", false, "(optional) specifies time granularity to analyze (default: hour 'HH')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'granularity_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetricsAnalysis + "(tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, granularity_in);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", "'granularity_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetricsAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "tabSchemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema (-pattern) of the table(s) to analyze");
M11_LRT.genProcParm(fileNo, "IN", "tabNamePattern_in", "VARCHAR(100)", false, "(optional) name (-pattern) of the table(s) to analyze");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetricsAnalysis + "(tabSchemaNamePattern_in, tabNamePattern_in, NULL, NULL, NULL);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'tabSchemaNamePattern_in", "'tabNamePattern_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetricsAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "granularity_in", "VARCHAR(4)", false, "(optional) specifies time granularity to analyze (default: hour 'HH')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'granularity_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetricsAnalysis + "(NULL, NULL, NULL, NULL, granularity_in);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, "'granularity_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Index Metrics data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetIndexMetricsAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, null, null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGetIndexMetricsAnalysis + "(NULL, NULL, NULL, NULL, NULL);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGetIndexMetricsAnalysis, ddlType, null, null, null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}