package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M91_DBMeta {




private static final int processingStepMeta = 4;
private static final int processingStepMeta2 = 5;


public static void genDbMetaDdl(Integer ddlType) {
genDbMetaDdl_1(ddlType);
M91_DBMeta.genDbMetaDdl_2(ddlType);
}


private static void genDbMetaDdl_1(Integer ddlType) {
int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, processingStepMeta, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

final String scrSchemaName = "srcSchema";
final String scrTableName = "srcTable";
final String maxPathLengthName = "maxPathLength";
final String sequenceNoName = "seqNo";

String qualFuncNamePdmSchemaName;
qualFuncNamePdmSchemaName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.udfnPdmSchemaName, ddlType, null, null, null, null, null, null);

String qualViewName;
String qualViewNameLdm;
String qualViewNameTabDepChain;

// ####################################################################################################################
// #    Procedure asserting a condition
// ####################################################################################################################

String qualProcNameAssert;
qualProcNameAssert = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.spnAssert, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Procedure asserting a condition", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameAssert);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "condition_in", "VARCHAR(2048)", true, "SQL-expression");
M11_LRT.genProcParm(fileNo, "IN", "message_in", "VARCHAR(50)", false, "exception-message used if condition does not evaluate to 'true'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 1, true);
M11_LRT.genVarDecl(fileNo, "v_condValue", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(2150)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_msg", "VARCHAR(100)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_cond CURSOR FOR v_stmntCond;");

M11_LRT.genProcSectionHeader(fileNo, "determine condition value of 'condition_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntText = 'VALUES(CASE WHEN ' || condition_in || ' THEN 1 ELSE 0 END)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmntCond FROM v_stmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN c_cond;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH c_cond INTO v_condValue;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE c_cond WITH RELEASE;");

M11_LRT.genProcSectionHeader(fileNo, "SIGNAL if condition does not evaluate to 'true'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_condValue = 0) THEN");
M79_Err.genSignalDdlWithParms("assertFailed", fileNo, 2, null, null, null, null, null, null, null, null, null, "message_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### IF IVK ###
// ####################################################################################################################
// #    Function mapping a 'Sparte' to the corresponding PS-OID
// ####################################################################################################################

String qualFuncNameSparte2PsOid;
qualFuncNameSparte2PsOid = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnSparte2PsOid, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function mapping a 'Sparte' to the corresponding PS-OID", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameSparte2PsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "sparte_in", "CHAR(1)", false, "SPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_dbtOid + "(" + M01_Globals_IVK.g_anValue + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSection + " = 'MAPPING_DPLUS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anKey + " = 'DPSPARTE2PS_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSubKey + " = sparte_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function mapping a 'Sparte' to the corresponding DIV-OID
// ####################################################################################################################

String qualFuncNameSparte2DivOid;
qualFuncNameSparte2DivOid = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnSparte2DivOid, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function mapping a 'Sparte' to the corresponding DIV-OID", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameSparte2DivOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "sparte_in", "CHAR(1)", false, "SPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_dbtOid + "(" + M01_Globals_IVK.g_anValue + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSection + " = 'MAPPING_DPLUS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anKey + " = 'DPSPARTE2DIV_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSubKey + " = sparte_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function mapping a PS-OID to the corresponding 'Sparte'
// ####################################################################################################################

String qualFuncNamePsOid2Sparte;
qualFuncNamePsOid2Sparte = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnPsOid2Sparte, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function mapping a PS-OID to the corresponding 'Sparte'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePsOid2Sparte);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, "OID of ProductStructure");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CHAR(1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSubKey);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSection + " = 'MAPPING_DPLUS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anKey + " = 'DPSPARTE2PS_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "" + M01_Globals_IVK.g_anValue + " = CHAR(psOid_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### ENDIF IVK ###
// ####################################################################################################################
// #    Function returning the PDM schema name for a given ACM section, organization and data pool
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function returning the PDM schema name for a given ACM section, organization and data pool", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "acmSection_in", "VARCHAR(20)", true, "name of the ACM-section");
M11_LRT.genProcParm(fileNo, "", "orgId_in", "INTEGER", true, "ID of the organization");
M11_LRT.genProcParm(fileNo, "", "poolId_in", "INTEGER", false, "ID of the data pool");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(10)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "( SELECT CAST(REPLACE('" + M04_Utilities.genSchemaName("<$$$>", "<$$$>", ddlType, null, null) + "', '<$$$>', SECTIONSHORTNAME) AS VARCHAR(50)) FROM " + M01_Globals.g_qualTabNameAcmSection + " WHERE SECTIONNAME = acmSection_in ) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgId_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RIGHT(RTRIM('00' || CAST(orgId_in AS CHAR(2))),2) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "poolId_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(poolId_in AS CHAR(1))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function returning the ','-separated list of column names of a database table prefixed by an optional column prefix
// ####################################################################################################################

String qualFuncNameDbTabColList;
qualFuncNameDbTabColList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.udfnDbTabColList, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning the ','-separated list of column names of a database table prefixed by an optional column prefix", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameDbTabColList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "table schema name");
M11_LRT.genProcParm(fileNo, "", "tabName_in", M01_Globals.g_dbtDbTableName, true, "table name");
M11_LRT.genProcParm(fileNo, "", "prefix_in", "VARCHAR(20)", false, "(optional) column prefix");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(8000)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_colList", "VARCHAR(8000)", "''", null, null);

M11_LRT.genProcSectionHeader(fileNo, "add each column name of the table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABSCHEMA = tabSchema_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABNAME = tabName_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COLNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colList = v_colList || (CASE v_colList WHEN '' THEN '' ELSE ',' END) || COALESCE(prefix_in, '') || COLNAME;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_colList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    View for LDM table dependency chains based on foreign keys
// ####################################################################################################################

qualViewNameTabDepChain = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnLdmTabDepChain, M01_ACM.vnsLdmTabDepChain, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for LDM table dependency chains based on foreign keys", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewNameTabDepChain);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + scrSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + scrTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DSTSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DSTTABLE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PATHLENGTH,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PATH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('' AS VARCHAR(1)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('' AS VARCHAR(1)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('' AS VARCHAR(1))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Dep");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstTable");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC_TABLENAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DST_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DST_TABLENAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameFkDependency);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmIsEnforced + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND NOT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SRC_SCHEMANAME=DST_SCHEMANAME AND SRC_TABLENAME=DST_TABLENAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_DepClosure");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "pathLength,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(SrcSchema || '.' || SrcTable || '->' || DstSchema || '.' || DstTable AS VARCHAR(2000))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_Dep");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D.dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D.dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D.pathlength + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.srcSchema || '.' || T.SrcTable || '->' || D.Path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_Dep         T,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_DepClosure  D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.dstSchema = D.srcSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.dstTable = D.srcTable");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D.pathLength < 50");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_DepClosureAll");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "pathLength,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "pathLength,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_DepClosure");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dstTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "path");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_DepClosureAll");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    View order LDM-tables according to their involvement in foreign key chains
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnLdmTabDepOrder, M01_ACM.vnsLdmTabDepOrder, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View ordering LDM-tables according to their involvement in foreign key chains", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + scrSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + scrTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + maxPathLengthName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + sequenceNoName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_DepClosureMaxLevelFromTop");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + scrSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + scrTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + maxPathLengthName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRCSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRCTABLE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(PATHLENGTH)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameTabDepChain);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRCSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRCTABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_TabsOrderedByDependencies");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "maxLevel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "srcSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "srcTable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "maxPathLength,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY maxPathLength ASC, srcSchema ASC, srcTable ASC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_DepClosureMaxLevelFromTop");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_TabsOrderedByDependencies");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    View relating ACM-Entities to Foreign-Key-names implenting relationships
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAcmEntityFkCol, M01_ACM.vnsAcmEntityFkCol, ddlType, null, null, null, null, null, null, null, null, null, null);
M22_Class_Utilities.printSectionHeader("View relating ACM-Entities to Foreign-Key-names implenting relationships", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REFENTITYSECTION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REFENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REFENTITYID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REFENTITYTYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FKCOL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntitySection + " WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntitySection + "  ELSE EPar." + M01_Globals.g_anAcmEntitySection + "     END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityName + "    WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityName + "     ELSE EPar." + M01_Globals.g_anAcmEntityName + "        END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityId + "      WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityId + "       ELSE EPar." + M01_Globals.g_anAcmEntityId + "          END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityType + "    WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityType + "     ELSE EPar." + M01_Globals.g_anAcmEntityType + "        END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntitySection + "  WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntitySection + " ELSE ELeftPar." + M01_Globals.g_anAcmEntitySection + " END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityName + "     WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityName + "    ELSE ELeftPar." + M01_Globals.g_anAcmEntityName + "    END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityId + "       WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityId + "      ELSE ELeftPar." + M01_Globals.g_anAcmEntityId + "      END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN ELeftPar." + M01_Globals.g_anAcmEntityType + "     WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN ERightPar." + M01_Globals.g_anAcmEntityType + "    ELSE ELeftPar." + M01_Globals.g_anAcmEntityType + "    END),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 OR E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(E." + M01_Globals.g_anAcmAliasShortName + ", E." + M01_Globals.g_anAcmEntityShortName + ") ELSE '' END ) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1 THEN E." + M01_Globals.g_anAcmRlShortName + " WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN E." + M01_Globals.g_anAcmLrShortName + " ELSE ELeft." + M01_Globals.g_anAcmEntityShortName + " END) || '_OID'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " EPar");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EPar." + M01_Globals.g_anAcmEntityName + " = COALESCE(E." + M01_Globals.g_anAcmOrParEntityName + ", E." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EPar." + M01_Globals.g_anAcmEntitySection + " = COALESCE(E." + M01_Globals.g_anAcmOrParEntitySection + ", E." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EPar." + M01_Globals.g_anAcmEntityType + " = E." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " ELeft");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmLeftEntityName + " = ELeft." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmLeftEntitySection + " = ELeft." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmLeftEntityType + " = ELeft." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(E." + M01_Globals.g_anAcmMaxLeftCardinality + " IS NULL AND E." + M01_Globals.g_anAcmMaxRightCardinality + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmRightEntityName + " = ELeft." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmRightEntitySection + " = ELeft." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmRightEntityType + " = ELeft." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " ERight");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmRightEntityName + " = ERight. " + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmRightEntitySection + " = ERight." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmRightEntityType + " = ERight." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " ELeftPar");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELeftPar." + M01_Globals.g_anAcmEntityName + " = COALESCE(ELeft." + M01_Globals.g_anAcmOrParEntityName + ", ELeft." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELeftPar." + M01_Globals.g_anAcmEntitySection + " = COALESCE(ELeft." + M01_Globals.g_anAcmOrParEntitySection + ", ELeft." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELeftPar." + M01_Globals.g_anAcmEntityType + " = ELeft." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " ERightPar");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ERightPar." + M01_Globals.g_anAcmEntityName + " = COALESCE(ERight." + M01_Globals.g_anAcmOrParEntityName + ", ERight." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ERightPar." + M01_Globals.g_anAcmEntitySection + " = COALESCE(ERight." + M01_Globals.g_anAcmOrParEntitySection + ", ERight." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ERightPar." + M01_Globals.g_anAcmEntityType + " = ERight." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmEntityType + " = 'R'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmIsEnforced + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmMaxRightCardinality + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmMaxLeftCardinality + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NOT (E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmMaxLeftCardinality + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmMaxRightCardinality + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    View relating PDM-tables to their LDM-tables and ACM-entities
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnPdmTable, M01_ACM.vnsPdmTable, ddlType, null, null, null, null, null, null, null, null, null, null);
M22_Class_Utilities.printSectionHeader("View relating PDM-tables to their LDM-tables and ACM-entities", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_SECTION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISCTO,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISCTP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISLRT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISRANGEPARTALL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISGEN,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISPSFORMING,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISLOGCHANGE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_ISABSTRACT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_PARSECTION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ENTITY_PARNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_MINLEFTCARDINALITY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_MAXLEFTCARDINALITY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_LEFT_ENTITYSECTION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_LEFT_ENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_LEFT_ENTITYTYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_MINRIGHTCARDINALITY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_MAXRIGHTCARDINALITY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_RIGHT_ENTITYSECTION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_RIGHT_ENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "REL_RIGHT_ENTITYTYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_TABLENAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_FKSEQUENCENO,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_ISLRT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_ISNL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LDM_ISGEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PDM_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anPdmTypedTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PDM_" + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PDM_POOLTYPE_ID");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsCto + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsCtp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsRangePartAll + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anLdmIsGen + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals_IVK.g_anAcmIsPs + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals_IVK.g_anAcmIsPsForming + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsLogChange + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsAbstract + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmOrParEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmOrParEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmMinLeftCardinality + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmMaxLeftCardinality + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmLeftEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmLeftEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmLeftEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmMinRightCardinality + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmMaxRightCardinality + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmRightEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmRightEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmRightEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmFkSequenceNo + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPoolTypeId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " AE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameLdmTable + " LT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmTable + " PT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntitySection + " = COALESCE(AE." + M01_Globals.g_anAcmOrParEntitySection + ", AE." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityName + " = COALESCE(AE." + M01_Globals.g_anAcmOrParEntityName + ", AE." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityType + " = COALESCE(AE." + M01_Globals.g_anAcmOrParEntityType + ", AE." + M01_Globals.g_anAcmEntityType + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkSchemaName + " = LT." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkTableName + " = LT." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function retrieving ','-separated list of LOB columns per ACM-entity
// ####################################################################################################################

String qualFuncNameLobAttrs;
qualFuncNameLobAttrs = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.udfnAcmLobAttrs, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function retrieving ','-separated list of LOB columns per ACM-entity", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLobAttrs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "acmEntitySection_in", "VARCHAR(20)", true, "section name of the ACM-entity");
M11_LRT.genProcParm(fileNo, "", "acmEntityName_in", "VARCHAR(50)", true, "name of the ACM-entity");
M11_LRT.genProcParm(fileNo, "", "acmEntityType_in", M01_Globals.g_dbtEntityType, false, "type of the ACM-entity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(1024)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_attrNameList", "VARCHAR(1024)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "loop over ACM attributes related to th given ACM-entity", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR attrLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmAttributeName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmAttribute + " A,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmDomain + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = acmEntitySection_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = acmEntityName_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = acmEntityType_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmDomainSection + " = D." + M01_Globals.g_anAcmDomainSection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmDomainName + " = D." + M01_Globals.g_anAcmDomainName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "D." + M01_Globals.g_anAcmDbDataType + " LIKE '%LOB%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anLdmSequenceNo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_attrNameList = COALESCE(v_attrNameList, '');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_attrNameList = RTRIM(LEFT(v_attrNameList || (CASE v_attrNameList WHEN '' THEN '' ELSE ',' END) || " + M01_Globals.g_anAcmAttributeName + ", 1024));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_attrNameList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### IF IVK ###
// ####################################################################################################################
// #    View mapping ACM-entity-names to XSD-generating UDFs
// ####################################################################################################################

String poolIdsSupportingXmlExport;
poolIdsSupportingXmlExport = "";
int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].supportXmlExport) {
poolIdsSupportingXmlExport = poolIdsSupportingXmlExport + (poolIdsSupportingXmlExport.compareTo("") == 0 ? "" : ",") + M72_DataPool.g_pools.descriptors[i].id;
}
}

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXsdFuncMap, M01_ACM_IVK.vsnXsdFuncMap, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View mapping ACM-entity-names to XSD-generating UDFs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xsdFuncSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xsdFuncName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "blobAttributes");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityShortName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isAbstract,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityShortName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsAbstract + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntitySection + ", A." + M01_Globals.g_anAcmEntitySection + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntityName + ", A." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntitySection + " = PAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityName + " = PAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityType + " = PAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmSupportXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmUseXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePdmSchemaName + "(S.SECTIONNAME, CAST(NULL AS INTEGER), CAST(NULL AS INTEGER)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CAST('F_' || A.entityShortName || '_" + M01_LDM_IVK.gc_xsdObjNameSuffix.toUpperCase() + "' AS VARCHAR(20)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLobAttrs + "(A.entitySection, A.entityName, A.entityType)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameAcmSection + " S,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection = S.SECTIONNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntitySection = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntityName = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "((P." + M01_Globals.g_anOrganizationId + " = " + String.valueOf(M01_Globals.g_primaryOrgId) + ") OR (P." + M01_Globals.g_anOrganizationId + " IS NULL))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "((P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals.g_workDataPoolIndex, ddlType) + ") OR (P." + M01_Globals.g_anPoolTypeId + " IS NULL))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXsdFuncMap, M01_ACM_IVK.vsnXsdFuncMap, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXsdFuncMap, true, true, false, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "View mapping ACM-entity-names to XSD-generating UDFs", null, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    View mapping ACM-entity-names to XML-generating UDFs
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlFuncMap, M01_ACM_IVK.vsnXmlFuncMap, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View mapping ACM-entity-names to XML-generating UDFs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ldmSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ldmTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "poolId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xmlFuncSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xmlFuncName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "blobAttributes");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityShortName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isAbstract,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntityShortName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityShortName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsAbstract + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmIsPs + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntitySection + ", A." + M01_Globals.g_anAcmEntitySection + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntityName + ", A." + M01_Globals.g_anAcmEntityName + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntityShortName + ", A." + M01_Globals.g_anAcmEntityShortName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntitySection + " = PAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityName + " = PAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityType + " = PAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmSupportXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmUseXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L.schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L.tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "O.ORGOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePdmSchemaName + "(S.SECTIONNAME, P." + M01_Globals.g_anOrganizationId + ", P." + M01_Globals.g_anPoolTypeId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CAST('F_' || A." + M01_Globals.g_anAcmEntityShortName + " || '_" + M01_LDM_IVK.gc_xmlObjNameSuffix.toUpperCase() + "' AS VARCHAR(20)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLobAttrs + "(A." + M01_Globals.g_anAcmEntitySection + ", A." + M01_Globals.g_anAcmEntityName + ", A." + M01_Globals.g_anAcmEntityType + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection = S.SECTIONNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntitySection = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntityName = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "((P." + M01_Globals.g_anPoolTypeId + " IN (" + poolIdsSupportingXmlExport + ")) OR (P." + M01_Globals.g_anPoolTypeId + " IS NULL))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anOrganizationId + " = O.ID");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlFuncMap, M01_ACM_IVK.vsnXmlFuncMap, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlFuncMap, true, true, false, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "View mapping ACM-entity-names to XML-generating UDFs", null, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    View mapping ACM-entity-names to XML-generating Views
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlViewMap, M01_ACM_IVK.vsnXmlViewMap, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View mapping ACM-entity-names to XML-generating Views", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "acmEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ldmSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ldmTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgOId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "poolId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xmlViewSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "xmlViewName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "blobAttributes");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityShortName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isAbstract,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "parEntityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityShortName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsAbstract + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmIsPs + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntitySection + ", A." + M01_Globals.g_anAcmEntitySection + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(PAR." + M01_Globals.g_anAcmEntityName + ", A." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntitySection + " = PAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityName + " = PAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmOrParEntityType + " = PAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmSupportXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmUseXmlExport + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "O.ORGOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePdmSchemaName + "(S.SECTIONNAME, P." + M01_Globals.g_anOrganizationId + ", P." + M01_Globals.g_anPoolTypeId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CAST('V_' || A." + M01_Globals.g_anAcmEntityName + " || '_" + M01_LDM_IVK.gc_xmlObjNameSuffix.toUpperCase() + "' AS VARCHAR(60)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLobAttrs + "(A." + M01_Globals.g_anAcmEntitySection + ", A." + M01_Globals.g_anAcmEntityName + ", A." + M01_Globals.g_anAcmEntityType + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_AcmEntity A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entitySection = S.SECTIONNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntitySection = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.parEntityName = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A.entityType = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "((P." + M01_Globals.g_anPoolTypeId + " IN (" + poolIdsSupportingXmlExport + ")) OR (P." + M01_Globals.g_anPoolTypeId + " IS NULL))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "P." + M01_Globals.g_anOrganizationId + " = O.ID");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlViewMap, M01_ACM_IVK.vsnXmlViewMap, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnXmlViewMap, true, true, false, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "View mapping ACM-entity-names to XML-generating Views", null, null, null, null, null, null, null, null);

// ### ENDIF IVK ###
NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genDbMetaDdl_2(Integer ddlType) {
int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, processingStepMeta2, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

// ####################################################################################################################
// #    Function to get a list of subclasses' ClassIds for a given ClassId
// ####################################################################################################################

String qualFuncNameGetSubClassIds;
qualFuncNameGetSubClassIds = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.udfnGetSubClassIds, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function to get a list of subclasses' ClassIds for a given ClassId", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameGetSubClassIds);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "classId_in", M01_Globals.g_dbtEntityId, false, "CLASSID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(1000)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_subClassIds VARCHAR(1024);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_subClassIds = classId_in;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR eLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH V_Node (entitySection, entityName, entityType, entityid, rootId, depth) AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R." + M01_Globals.g_anAcmEntityId + " = classId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.rootId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.depth + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " C,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Node P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntitySection + " = P.entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntityName + " = P.entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntityType + " = P.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.depth < 100");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId AS c_entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Node");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId <> v_subClassIds");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY entityId ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 166 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_subClassIds = v_subClassIds || ',' || c_entityId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_subClassIds;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function to get a list of subclasses' ClassIds for a given ClassId
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function returning set of subclass-Ids for a list of given ClassIds", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualFuncNameGetSubClassIdsByList);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "classIdList_in", "CHAR(200)", false, "','-separated list of CLASSIDs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId    " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isAbstract " + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "rootId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "depth,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isAbstract");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmIsAbstract);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(classIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityId + " = X.elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityType + "='" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.rootId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.depth + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmIsAbstract);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " C,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NODE P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntitySection + " = P.entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntityName + " = P.entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmSupEntityType + " = P.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.depth < 100");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isAbstract");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
// ### IF IVK ###

// ####################################################################################################################
// #    View representing a pseudo Enum for entities with flag EntityFilterEnum Criteria, including list of subclass ids
// ####################################################################################################################

String qualViewName;
String qualViewNameLdm;
qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnEntityFilterEnum, M01_ACM_IVK.vsnEntityFilterEnum, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("representing a pseudo Enum for entities with flag EntityFilterEnum Criteria, including list of subclass ids", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SUBCLASSIDS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY E." + M01_Globals.g_anAcmEntityId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualFuncNameGetSubClassIds + "(E." + M01_Globals.g_anAcmEntityId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anAcmEntityFilterEnumCriteria + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnEntityFilterEnum, M01_ACM_IVK.vsnEntityFilterEnum, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnEntityFilterEnum, true, true, true, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "View representing a pseudo Enum for entities with flag EntityFilterEnum Criteria", null, null, true, null, null, null, null, null);

// ####################################################################################################################
// #    View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnEntityFilterNlTextEnum, M01_ACM_IVK.vsnEntityFilterNlTextEnum, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anEnumRefId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY E." + M01_Globals.g_anAcmEntityId + ", ET." + M01_Globals.g_anLanguageId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (PARTITION BY ET." + M01_Globals.g_anLanguageId + " ORDER BY E." + M01_Globals.g_anAcmEntityId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ET." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ET." + M01_Globals.g_anAcmEntityLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntityNl + " ET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmEntityName + " = ET." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmEntitySection + " = ET." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anAcmEntityType + " = ET." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anAcmEntityFilterEnumCriteria + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnEntityFilterEnum, M01_ACM_IVK.vsnEntityFilterEnum, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexCommon, M01_ACM_IVK.vnEntityFilterNlTextEnum, true, true, false, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria", null, null, true, null, null, null, null, null);
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}



}