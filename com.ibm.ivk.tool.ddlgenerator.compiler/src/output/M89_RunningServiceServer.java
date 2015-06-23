package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M89_RunningServiceServer {


// ### IF IVK ###


private static final int processingStep = 5;


public static void genRssSupDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genRssSupForDb(M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genRssSupForDb(M01_Common.DdlTypeId.edtPdm);

for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm & !M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive) {
genRssSupByPool(M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}


private static void genRssSupForDb(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
// we currently do not support this
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStep, ddlType, null, null, null, M01_Common.phaseAliases, M01_Common.ldmIterationPostProc);

String unqualTabNamePaiMessageLog;
unqualTabNamePaiMessageLog = M04_Utilities.getUnqualObjName(M01_Globals_IVK.g_qualTabNamePaiMessageLog);
String unqualTabNameRssStatus;
unqualTabNameRssStatus = M04_Utilities.getUnqualObjName(M01_Globals_IVK.g_qualTabNameRssStatus);
String unqualTabNameRssHistory;
unqualTabNameRssHistory = M04_Utilities.getUnqualObjName(unqualTabNameRssHistory);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
String columnDefault;

String qualProcNameRssGetStatus;

// ####################################################################################################################
// #    Procedure retrieving the Running-Service-Server-Status
// ####################################################################################################################
qualProcNameRssGetStatus = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexPaiLog, M01_ACM_IVK.spnRssGetStatus, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameRssGetStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "(optional) CD User Id of the mdsUser calling this procedure (if NULL, use CURRENT USER)");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of status records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_msgStmntText", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_message", "VARCHAR(32600)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_grantCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_timestampStr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_timestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_userIdStr", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_correlationIdStr", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_callerCorrelationIdStr", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dataPoolStr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOidStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_subSystemStr", "VARCHAR(3)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_serverIdStr", "VARCHAR(64)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_threadIdStr", "VARCHAR(64)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_actionStr", "VARCHAR(30)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_useCaseIdStr", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_commonActivityStr", "VARCHAR(30)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_customTextStr", "VARCHAR(128)", "NULL", null, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "v_lastSystemStartup", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_formatError", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_ignoreRecord", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_ignoreErrors", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genVarDecl(fileNo, "v_logTableName1", "VARCHAR(100)", "'" + unqualTabNamePaiMessageLog + "_1'", null, null);
M11_LRT.genVarDecl(fileNo, "v_logTableName2", "VARCHAR(100)", "'" + unqualTabNamePaiMessageLog + "_2'", null, null);
M11_LRT.genVarDecl(fileNo, "v_logSchemaName", "VARCHAR(50)", "'" + M04_Utilities.getSchemaName(M01_Globals_IVK.g_qualTabNamePaiMessageLog) + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_logAliasName", "VARCHAR(50)", "'" + unqualTabNamePaiMessageLog + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_qualLogAliasName", "VARCHAR(100)", "'" + M01_Globals_IVK.g_qualTabNamePaiMessageLog + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_prevLogTableName", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_qualPrevLogTableName", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nextLogTableName", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_qualNextLogTableName", "VARCHAR(100)", "NULL", null, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "v_cleanupStmntCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cleanupRowCount", "INTEGER", "NULL", null, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_mySqlCode", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "valueTooLong", "22001", null);
M11_LRT.genCondDecl(fileNo, "dateTimeFormatError", "22007", null);
M11_LRT.genCondDecl(fileNo, "smallIntFormatError", "22018", null);
M11_LRT.genCondDecl(fileNo, "numericOverflowError", "22003", null);
M11_LRT.genCondDecl(fileNo, "objectNotExists", "42704", null);
M11_LRT.genCondDecl(fileNo, "objectAlreadyExists", "42710", null);
M11_LRT.genCondDecl(fileNo, "cursorNotOpen", "24501", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_msgStmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE msgCursor CURSOR WITH HOLD FOR v_msgStmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR dateTimeFormatError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_formatError = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR smallIntFormatError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_formatError = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR numericOverflowError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_formatError = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR valueTooLong");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_formatError = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR objectNotExists");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR objectAlreadyExists");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR cursorNotOpen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = SQLCODE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT (v_ignoreErrors = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameRssGetStatus, ddlType, -3, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.ignoreConstraint = true;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRssHistory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genTransformedAttrDeclByDomain(tabColumns.descriptors[i].acmAttributeName, "-", M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformation, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals_IVK.g_classIndexPaiMessageLog, null, i < tabColumns.numDescriptors, ddlType, null, null, null, null, 0, null, null, null, null, null, null));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON ROLLBACK PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelogConsolidated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LIKE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON ROLLBACK PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameRssGetStatus, ddlType, null, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine next log file", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(BASE_TABNAME, TABNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_prevLogTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSCAT.TABLES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABSCHEMA = v_logSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABNAME = v_logAliasName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TYPE IN ('A', 'T')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_qualPrevLogTableName = v_logSchemaName || '.' || v_prevLogTableName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nextLogTableName     = (CASE WHEN COALESCE(v_prevLogTableName, '') = v_logTableName2 THEN v_logTableName1 ELSE v_logTableName2 END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_qualNextLogTableName = v_logSchemaName || '.' || v_nextLogTableName;");

M11_LRT.genProcSectionHeader(fileNo, "truncat table identified by v_qualNextLogTableName", 0, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntText =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'TRUNCATE TABLE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_qualNextLogTableName || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'DROP STORAGE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'IGNORE DELETE TRIGGERS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CONTINUE IDENTITY ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'IMMEDIATE'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M11_LRT.genProcSectionHeader(fileNo, "commit to allow truncate to next log table", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntText;");

M11_LRT.genProcSectionHeader(fileNo, "drop Alias / Table with name v_qualLogAliasName", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_ignoreErrors = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (SELECT 1 FROM SYSCAT.TABLES WHERE TYPE = 'T' AND TABNAME = v_logAliasName AND TABSCHEMA = v_logSchemaName) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntText = 'DROP TABLE ' || v_qualLogAliasName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntText = 'DROP ALIAS ' || v_qualLogAliasName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntText;");

M11_LRT.genProcSectionHeader(fileNo, "lock current message log table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_qualPrevLogTableName IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntText = 'LOCK TABLE ' || v_qualPrevLogTableName || ' IN EXCLUSIVE MODE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "create Alias with name v_qualLogAliasName", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntText = 'CREATE ALIAS ' || v_qualLogAliasName || ' FOR ' || v_qualNextLogTableName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntText;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_ignoreErrors = " + M01_LDM.gc_dbFalse + ";");

M11_LRT.genProcSectionHeader(fileNo, "commit to allow access to new log table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "COMMIT;");

String qualProcedureNameCleanup;
qualProcedureNameCleanup = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnCleanData, ddlType, null, null, null, null, null, null);
M11_LRT.genProcSectionHeader(fileNo, "perform housekeeping on History-table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameCleanup + "(2, 'DBAdmin', 'PaiLog', v_cleanupStmntCount, v_cleanupRowCount);");

M11_LRT.genProcSectionHeader(fileNo, "process PAI log messages", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS(SELECT 1 FROM SYSCAT.TABLES WHERE TABSCHEMA = v_logSchemaName AND TABNAME = v_prevLogTableName) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_msgStmntText = 'SELECT CAST(MESSAGE AS VARCHAR(2000)) FROM ' || v_qualPrevLogTableName || ' WHERE LEVEL = ''INFO'' AND LENGTH(MESSAGE) <= 2000';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_msgStmnt FROM v_msgStmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN msgCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH msgCursor INTO v_message;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE (v_mySqlCode = 0) DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_timestampStr           = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_timestamp              = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_userIdStr              = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_correlationIdStr       = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_callerCorrelationIdStr = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_dataPoolStr            = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_orgOid                 = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_psOid                  = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_accessModeId           = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lrtOidStr              = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lrtOid                 = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_subSystemStr           = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_serverIdStr            = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_threadIdStr            = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_actionStr              = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_useCaseIdStr           = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_commonActivityStr      = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_customTextStr          = NULL;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_formatError            = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_ignoreRecord           = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_ignoreErrors           = 1; -- in case of error during parsing: just ignore record");

M11_LRT.genProcSectionHeader(fileNo, "parse message text", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "parse:");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR msgElemLoop AS msgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CAST(RTRIM(LEFT(SUBSTR(elem, 2, LENGTH(elem)-2), 100)) AS VARCHAR(100)) AS elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABLE(" + M01_Globals.g_qualFuncNameStrElems + "(v_message, CAST(',' AS CHAR(1)), '\"', '\"')) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "elem IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_mySqlCode = -501 THEN -- CURSOR NOT OPEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEAVE parse;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "IF (posIndex =  0) THEN SET v_timestampStr      = elem;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  1) THEN SET v_userIdStr         = RTRIM(LEFT(elem, 16));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  2) THEN SET v_correlationIdStr  = RTRIM(LEFT(elem, 128));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  3) THEN SET v_dataPoolStr       = elem;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  4) THEN SET v_lrtOidStr         = RTRIM(LEFT(elem, 25));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  5) THEN SET v_subSystemStr      = UCASE(RTRIM(elem));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  6) THEN SET v_serverIdStr       = RTRIM(LEFT(elem, 64));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  7) THEN SET v_threadIdStr       = RTRIM(LEFT(elem, 64));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  8) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex =  9) THEN SET v_actionStr         = RTRIM(LEFT(elem, 30));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex = 10) THEN SET v_useCaseIdStr      = RTRIM(LEFT(elem, 128));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex = 11) THEN SET v_commonActivityStr = RTRIM(LEFT(elem, 30));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSEIF (posIndex = 12) THEN SET v_customTextStr     = RTRIM(LEFT(elem, 128));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_lrtOidStr, '') <> '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_lrtOid = " + M01_Globals.g_dbtOid + "(v_lrtOidStr);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF LENGTH(v_subSystemStr) > 3 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_ignoreRecord = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_actionStr, '') = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_ignoreRecord = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_useCaseIdStr, '') = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_ignoreRecord = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
// FIXME: set v_userId and v_correlationId to NULL if ''
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_timestampStr, '') <> '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_timestamp = TIMESTAMP(REPLACE(REPLACE(v_timestampStr, ':', '.'), '-24.', '-00.'));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_dataPoolStr, '') <> '' THEN");

M11_LRT.genProcSectionHeader(fileNo, "parse v_dataPoolStr", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ABS(orgOid),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ABS(psOid),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ABS(accessModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");

String qualFuncNameParseDataPools;
qualFuncNameParseDataPools = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnParseDataPools, ddlType, null, null, null, null, null, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABLE(" + qualFuncNameParseDataPools + "(v_dataPoolStr)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_ignoreErrors = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_formatError = " + M01_LDM.gc_dbFalse + " AND v_ignoreRecord = " + M01_LDM.gc_dbFalse + " THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_actionStr = 'beginInterface' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_callerCorrelationIdStr = RTRIM(CAST(LEFT(v_customTextStr, 128) AS VARCHAR(128)));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "userId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "correlationId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "callerId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessMode_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "subSystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "serverId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "threadId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "action,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "service,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "commonActivity,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "customText");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_userIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_correlationIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_callerCorrelationIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_subSystemStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_serverIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_threadIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_actionStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_useCaseIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_commonActivityStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_customTextStr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_mySqlCode = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH msgCursor INTO v_message;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE msgCursor WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "copy records to History-Table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRssHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
boolean isFirstCond;
isFirstCond = true;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRssHistory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].valueList.compareTo("") == 0)) {
if (!(isFirstCond)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
isFirstCond = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + " IN (" + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].valueList + ")");
} else if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue.compareTo("") == 0) |  !(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength.compareTo("") == 0) | !(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue.compareTo("") == 0)) {
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue.compareTo("") == 0)) {
if (!(isFirstCond)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
isFirstCond = false;
if (tabColumns.descriptors[i].isNullable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + ", " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue + ") >= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + " >= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue);
}
}
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue.compareTo("") == 0)) {
if (!(isFirstCond)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
isFirstCond = false;
if (tabColumns.descriptors[i].isNullable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + ", " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue + ") >= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + " <= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue);
}
}
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength.compareTo("") == 0)) {
if (!(isFirstCond)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
isFirstCond = false;
if (tabColumns.descriptors[i].isNullable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + " IS NULL OR LENGTH(" + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + ") >= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LENGTH(" + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + ") >= " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength);
}
}
} else if (!(tabColumns.descriptors[i].isNullable)) {
if (!(isFirstCond)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
isFirstCond = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabColumns.descriptors[i].acmAttributeName.toUpperCase() + " IS NOT NULL");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "merge with old Status info", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "userId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "correlationId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "callerId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessMode_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "subSystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "serverId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "threadId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "service,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "action");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "STARTTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CORRELATIONID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALLERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORGOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PSOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SUBSYSTEM,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SERVERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THREADID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SERVICE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'begin'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRssStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "ignore records not relevant for Status", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(ACTION NOT IN ('start', 'stop') AND ACTION NOT LIKE 'begin%' AND ACTION NOT LIKE 'end%')");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRssStatus, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
// FIXME: HACK
if (tabColumns.descriptors[i].acmAttributeName.toUpperCase() == "STARTTIME") {
goto NextI;
}

String attrName;
attrName = tabColumns.descriptors[i].acmAttributeName;
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].valueList.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + attrName.toUpperCase() + " NOT IN (" + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].valueList + ")");
} else if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue.compareTo("") == 0) |  !(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength.compareTo("") == 0) | !(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue.compareTo("") == 0)) {
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + attrName.toUpperCase() + " < " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minValue);
}
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + attrName.toUpperCase() + " > " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].maxValue);
}
if (!(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LENGTH(" + attrName.toUpperCase() + ") < " + M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].minLength);
}
} else if (!(tabColumns.descriptors[i].isNullable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + attrName.toUpperCase() + " IS NULL");
}
NextI:
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SERVICE = ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "for Status ignore all records prior to last server startup", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelog L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "subSystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "serverId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "MAX(timestamp) AS timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SESSION.ivkMessagelog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "action IN ('start', 'stop')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "subSystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "serverId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") V_ServerReStart");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ServerReStart.timestamp > L.timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ServerReStart.subSystem = L.subSystem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ServerReStart.serverId = L.serverId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "normalize ACTION for Status", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE SESSION.ivkMessagelog SET action = 'begin' WHERE action LIKE 'begin_%';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE SESSION.ivkMessagelog SET action = 'end'   WHERE action LIKE 'end_%';");

M11_LRT.genProcSectionHeader(fileNo, "for Status ignore all 'correlated record pairs' of 'begin-end' related to the same subsystem", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN ATOMIC");
M11_LRT.genProcSectionHeader(fileNo, "declare variables", 2, true);
M11_LRT.genVarDecl(fileNo, "v_stmntMsgSeqTextPrev", "VARCHAR(2000)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_correlationIdPrev", "VARCHAR(128)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_serverIdPrev", "VARCHAR(64)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_subSystemPrev", "VARCHAR(3)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_servicePrev", "VARCHAR(128)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_actionPrev", "VARCHAR(30)", "NULL", 2, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "v_timestampPrev", "TIMESTAMP", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_userIdPrev", M01_Globals.g_dbtUserId, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_callerIdPrev", "VARCHAR(128)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_orgOidPrev", M01_Globals.g_dbtOid, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_psOidPrev", M01_Globals.g_dbtOid, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_accessmodeIdPrev", M01_Globals.g_dbtEnumId, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOidPrev", M01_Globals.g_dbtOid, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_threadIdPrev", "VARCHAR(64)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_commonActivityPrev", "VARCHAR(30)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_customTextPrev", "VARCHAR(128)", "NULL", 2, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "v_stmntMsgSeqText", "VARCHAR(2000)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_correlationId", "VARCHAR(128)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_serverId", "VARCHAR(64)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_subSystem", "VARCHAR(3)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_service", "VARCHAR(128)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_action", "VARCHAR(30)", "NULL", 2, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", 2, null);
M11_LRT.genVarDecl(fileNo, "v_stmntMsgSeqPrev", "STATEMENT", null, 2, null);
M11_LRT.genVarDecl(fileNo, "v_stmntMsgSeq", "STATEMENT", null, 2, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE msgSeqCursorPrev          CURSOR WITH HOLD FOR v_stmntMsgSeqPrev;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE msgSeqCursor              CURSOR WITH HOLD FOR v_stmntMsgSeq;");

M11_LRT.genProcSectionHeader(fileNo, "initialize cursors", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntMsgSeqTextPrev = 'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'timestamp, userid, correlationid, callerid, orgoid, psoid, accessmode_id, lrtoid, subsystem, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'serverid, threadid, service, action, commonactivity, customtext ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM SESSION.ivkMessagelog ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'ORDER BY subsystem, correlationid, serverid, service, timestamp';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntMsgSeqText = 'select correlationid, subsystem, serverid, service, action ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM SESSION.ivkMessagelog ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'ORDER BY subsystem, correlationid, serverid, service, timestamp';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmntMsgSeqPrev FROM v_stmntMsgSeqTextPrev;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmntMsgSeq     FROM v_stmntMsgSeqText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN msgSeqCursorPrev;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN msgSeqCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "2 * FETCH for 'LookAhead-Cursor'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH msgSeqCursor INTO v_correlationId, v_subSystem, v_serverId, v_service, v_action;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH msgSeqCursor INTO v_correlationId, v_subSystem, v_serverId, v_service, v_action;");

M11_LRT.genProcSectionHeader(fileNo, "1 * FETCH for 'Current Position Cursor'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "msgSeqCursorPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_actionPrev, v_commonActivityPrev, v_customTextPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE (v_mySqlCode = 0) DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_correlationId = v_correlationIdPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_serverId = v_serverIdPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_service = v_servicePrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_action = 'end'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_actionPrev = 'begin'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") THEN");

M11_LRT.genProcSectionHeader(fileNo, "found a pair of matching records - ignore both", 4, true);
M11_LRT.genProcSectionHeader(fileNo, "mark 'current' cursor 'invalid' (in case we do not read a value again", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_subSystemPrev = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "give both Cursors one extra move forward because we got two records to ignore", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msgSeqCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_correlationId, v_subSystem, v_serverId, v_service, v_action");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msgSeqCursorPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_actionPrev, v_commonActivityPrev, v_customTextPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.ivkMessagelogConsolidated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "userid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "correlationid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "callerid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessmode_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "lrtoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "subsystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "serverid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "threadid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "service,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "action,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "commonactivity,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "customtext");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_timestampPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_userIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_correlationIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_callerIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_orgOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_psOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_accessmodeIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_subSystemPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_serverIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_threadIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_servicePrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_actionPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_commonActivityPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_customTextPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_mySqlCode = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "mark previous cursor 'invalid' (in case we do not read a value again", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_subSystemPrev = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "move both Cursors forward to ignore record", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "msgSeqCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_correlationId, v_subSystem, v_serverId, v_service, v_action");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "msgSeqCursorPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_timestampPrev, v_userIdPrev, v_correlationIdPrev, v_callerIdPrev, v_orgOidPrev, v_psOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_accessmodeIdPrev, v_lrtOidPrev, v_subSystemPrev, v_serverIdPrev, v_threadIdPrev, v_servicePrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_actionPrev, v_commonActivityPrev, v_customTextPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_mySqlCode = COALESCE(v_mySqlCode, SQLCODE);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_subSystemPrev IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "this record is not yet processed", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.ivkMessagelogConsolidated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "userid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "correlationid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "callerid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "orgoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessmode_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "lrtoid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "subsystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "serverid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "threadid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "service,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "action,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "commonactivity,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "customtext");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_timestampPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_userIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_correlationIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_callerIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_orgOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_psOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_accessmodeIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtOidPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_subSystemPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_serverIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_threadIdPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_servicePrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_actionPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_commonActivityPrev,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_customTextPrev");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "close cursor", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE msgSeqCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE msgSeqCursorPrev;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "delete old Status info", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRssStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine current Status info", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRssStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "STARTTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CORRELATIONID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALLERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORGOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PSOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SUBSYSTEM,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SERVERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THREADID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SERVICE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "userId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "correlationId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "callerId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessMode_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "subSystem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "serverId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "threadId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "service");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ivkMessagelogConsolidated LStart");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LStart.action = 'begin'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.ivkMessagelogConsolidated LStop");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LStop.timestamp >= LStart.timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LStart.correlationId = LStop.correlationId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LStart.serverId = LStop.serverId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LStart.service = LStop.service");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LStop.action = 'end'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count number of records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameRssGetStatus, ddlType, null, "'cdUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "commit new status", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "COMMIT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genRssSupByPool(Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

int thisPoolIndex; 
if (thisPoolIndexW == null) {
thisPoolIndex = -1;
} else {
thisPoolIndex = thisPoolIndexW;
}

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPostProc);

String qualProcNameRssGetStatusGlobal;
String qualProcNameRssGetStatusLocal;

// ####################################################################################################################
// #    Procedure retrieving the Running-Service-Server-Status
// ####################################################################################################################
qualProcNameRssGetStatusGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexPaiLog, M01_ACM_IVK.spnRssGetStatus, ddlType, null, null, null, null, null, null);
qualProcNameRssGetStatusLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnRssGetStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameRssGetStatusLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- procedure expects the UserId of the current user being held in register 'CURRENT USER'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of status records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameRssGetStatusLocal, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current user", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(RTRIM(LEFT(CURRENT USER, 16)) AS " + M01_Globals.g_dbtUserId + ");");

M11_LRT.genProcSectionHeader(fileNo, "call 'global' procedure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameRssGetStatusGlobal + "(v_cdUserId, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameRssGetStatusLocal, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Procedure retrieving the Running-Service-Server-Status
// ####################################################################################################################
qualProcNameRssGetStatusLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnRssGetStatus, ddlType, thisOrgIndex, thisPoolIndex, null, "MBS", null, null);

M22_Class_Utilities.printSectionHeader("Procedure retrieving the Running-Service-Server-Status", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameRssGetStatusLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, false, "CD User Id of the mdsUser calling this procedure");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameRssGetStatusLocal, ddlType, null, "'cdUserId_in", null, null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current user", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = COALESCE(v_cdUserId, CAST(RTRIM(LEFT(CURRENT USER, 16)) AS " + M01_Globals.g_dbtUserId + "));");

M11_LRT.genProcSectionHeader(fileNo, "call 'global' procedure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameRssGetStatusGlobal + "(v_cdUserId, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameRssGetStatusLocal, ddlType, null, "'cdUserId_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### ENDIF IVK ###


}