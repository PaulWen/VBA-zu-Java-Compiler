package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M94_DBAdmin_Partitioning {


// ### IF IVK ###


public class PartitionType {
public static final int ptNone = 0;
public static final int ptPsOid = 1;
public static final int ptPsOidCid = 2;
public static final int ptDivOid = 4;
}

private static final int processingStepAdmin = 4;


public static void genDbAdminPartitioningDdl(Integer ddlType) {
if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStepAdmin, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

M94_DBAdmin_Partitioning.genDbAdminPartitioningByPsDdlByDdlType(fileNo, ddlType);
M94_DBAdmin_Partitioning.genDbAdminPartitioningByDivDdlByDdlType(fileNo, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genDbAdminPartitioningByPsDdlByDdlType(int fileNo, Integer ddlTypeW) {
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
return;
}

// ####################################################################################################################
// #    SP for configuring table partitioning (by PS_OID)
// ####################################################################################################################

boolean andOrFlag;
andOrFlag = false;

String qualProcedureNameTablePartCfg;
qualProcedureNameTablePartCfg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnSetTablePartCfgPs, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for configuring table partitioning (by " + M01_Globals_IVK.g_anPsOid + ")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameTablePartCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "toggle_in", M01_Globals.g_dbtBoolean, true, "if set to '1' switch on table partitioning, otherwise switch off");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colDeclTxt", "VARCHAR(20000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_partitionClauseTxt", "VARCHAR(100)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_foundPartitionCrit", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "altObjError", "38553", null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR altObjError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

String tempTabNameStatementTabCfg;
tempTabNameStatementTabCfg = M94_DBAdmin.tempTabNameStatement + "TabPartCfg";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 20000, true, true, true, null, "TabPartCfg", null, null, true, null, "msg", "VARCHAR(2048)", "refId", "INTEGER");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameTablePartCfg, ddlType, null, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) AND (toggle_in = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameTablePartCfg, ddlType, -2, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine partitiong-clause of statement", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF toggle_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR psLoop AS psCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS." + M01_Globals.g_anOid + " AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS." + M01_Globals_IVK.g_anIsUnderConstruction + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_partitionClauseTxt = v_partitionClauseTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'PARTITION P' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(c_psOid), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") || ' STARTING ' || RTRIM(CHAR(c_psOid)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(c_psOid)) || ' INCLUSIVE'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_foundPartitionCrit = " + M01_LDM.gc_dbTrue + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_partitionClauseTxt = ' PARTITION BY (" + M01_Globals_IVK.g_anPsOid + ") (' || v_partitionClauseTxt || ')';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.COMPRESSION AS c_tabCompression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TBSPACE AS c_tbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.INDEX_TBSPACE AS c_indTbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.LONG_TBSPACE AS c_longTbSpace");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME = '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "assemble CREATE TABLE statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CREATE TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colDeclTxt = '';");

M11_LRT.genProcSectionHeader(fileNo, "loop over columns to assemble column-declarations", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS colCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.COLNAME   AS c_colName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TYPENAME  AS c_colType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.LENGTH    AS c_length,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.SCALE     AS c_scale,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.DEFAULT   AS c_default,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.NULLS     AS c_nulls,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.COMPRESS  AS c_compress,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.CONSTNAME AS c_constName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TYPE      AS c_constType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TEXT      AS c_constText");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS CO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLCHECKS CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.TABSCHEMA = CO.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.TABNAME = CO.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.COLNAME = CO.COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.CHECKS CH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TABSCHEMA = CC.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TABNAME = CC.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.CONSTNAME = CC.CONSTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TYPE IN ('A', 'C')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TABSCHEMA = c_tabSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TABNAME = c_tabName");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colDeclTxt = v_colDeclTxt || (CASE v_colDeclTxt WHEN '' THEN '' ELSE ', ' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_colName || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_colType ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN c_colType IN ('CHARACTER', 'VARCHAR', 'LONG VARCHAR', 'CLOB', 'BLOB', 'REAL')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN '(' || RTRIM(CHAR(c_length)) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN c_colType IN ('DECIMAL')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN '(' || RTRIM(CHAR(c_length)) || ',' || RTRIM(CHAR(c_scale)) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_nulls = 'N' THEN ' NOT NULL' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(' DEFAULT ' || c_default, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_compress = 'S' THEN ' COMPRESS SYSTEM DEFAULT' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_constType = 'C' THEN COALESCE(' CONSTRAINT ' || c_constName || ' CHECK (' || c_constText || ')', '') ELSE '' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "finalize CREATE TABLE statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || ' (' || v_colDeclTxt || ')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(' IN ' || c_tbSpace, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(' INDEX IN ' || c_indTbSpace, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_tabCompression IN ('V', 'B') THEN ' VALUE COMPRESSION' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' COMPRESS YES ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN v_foundPartitionCrit = 1 THEN v_partitionClauseTxt ELSE '' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', '' || REPLACE(v_stmntTxt, '''', '''''') || '', -1, ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjMsg", "VARCHAR(2048)", "''", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjId", "INTEGER", "-1", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjStmntTxt", "VARCHAR(100)", "NULL", 4, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 4, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 4, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_altObjId = -1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_altObjStmntTxt = 'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ?, ?, ?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_altObjStmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjMsg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF mode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "refId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN NULL ELSE v_altObjId END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN v_diagnostics ELSE v_altObjMsg END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ''' || REPLACE(v_stmntTxt, '''', '''''') || ''', -1, ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF mode_in = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameTablePartCfg, ddlType, null, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for adding a table partition for PS-tagged tables
// ####################################################################################################################

String qualProcedureNameAddTablePartitionByPs;
qualProcedureNameAddTablePartitionByPs = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnAddTablePartitionByPs, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for PS-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByPs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "if set to '1' commit after each statement");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntChkTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_chkVal", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_doAddPartition", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_errMsg", "VARCHAR(30)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_partitionClauseTxt", "VARCHAR(200)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_tablespaceClauseTxt", "VARCHAR(200)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_returnResult", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);
M11_LRT.genVarDecl(fileNo, "SQLCODE", "INTEGER", "0", null, null);
if (M03_Config.supportRangePartitioningByClassId) {
M11_LRT.genVarDecl(fileNo, "v_stmntPartClauseTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psSupportsPartByCId", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_tabSupportsPartByCId", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 1, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 1, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR WITH HOLD FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

String tempTabNameStatementAddTabPart;
tempTabNameStatementAddTabPart = M94_DBAdmin.tempTabNameStatement + "AddTabPartByPs";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, null, 200, true, true, true, null, "AddTabPartByPs", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "hook: if mode_in = '-1' or '-2', suppress return of results, but fill temporary table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in < 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_returnResult = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET mode_in = mode_in + 2;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM " + tempTabNameStatementAddTabPart + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, -2, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (M03_Config.supportRangePartitioningByClassId) {
M11_LRT.genProcSectionHeader(fileNo, "determine whether PS supports partitioning by CLASSID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS(SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameClassIdPartitionBoundaries + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in AND (LBOUND IS NOT NULL OR UBOUND IS NOT NULL)) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psSupportsPartByCId = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (!(M03_Config.supportRangePartitioningByClassId)) {
M11_LRT.genProcSectionHeader(fileNo, "determine partitiong-clause of statement", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_partitionClauseTxt = ' ADD PARTITION P' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(psOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") ||" + "' STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE';");
}

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME AS c_tabName");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " = T.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " = T.TABNAME");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L_MQT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityName + " = L_MQT." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityType + " = L_MQT." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntitySection + " = L_MQT." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = L_MQT." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = L_MQT." + M01_Globals.g_anLdmIsGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L_MQT." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P_MQT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P_MQT." + M01_Globals.g_anPdmLdmFkSchemaName + " = L_MQT." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P_MQT." + M01_Globals.g_anPdmLdmFkTableName + " = L_MQT." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = P_MQT." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = P_MQT." + M01_Globals.g_anPoolTypeId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " AS aet");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aet." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aet." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aet." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

if (!(M03_Config.noPartitioningInDataPools.compareTo("") == 0)) {
int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M04_Utilities.includedInList(M03_Config.noPartitioningInDataPools, M72_DataPool.g_pools.descriptors[i].id)) {
if (andOrFlag) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
}
M11_LRT.genProcSectionHeader(fileNo, "no data partitioning in datapool " + String.valueOf(M72_DataPool.g_pools.descriptors[i].id), 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(P." + M01_Globals.g_anPoolTypeId + ",-1) <> " + String.valueOf(M72_DataPool.g_pools.descriptors[i].id));
andOrFlag = true;
}
}
}

if (!(M03_Config.partitionLrtPrivateWhenMqt)) {
if (andOrFlag) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M11_LRT.genProcSectionHeader(fileNo, "if MQT-table exists, partitioning is not supported on private tables", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals.g_anLdmIsLrt + ", " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals_IVK.g_anLdmIsMqt + ", " + M01_LDM.gc_dbTrue + ") = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_MQT." + M01_Globals.g_anPoolTypeId + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
andOrFlag = true;
}

if (!(M03_Config.partitionLrtPublicWhenMqt)) {
if (andOrFlag) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M11_LRT.genProcSectionHeader(fileNo, "if MQT-table exists, partitioning is not supported on public tables", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals.g_anLdmIsLrt + ", " + M01_LDM.gc_dbTrue + ") = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals_IVK.g_anLdmIsMqt + ", " + M01_LDM.gc_dbTrue + ") = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_MQT." + M01_Globals.g_anPoolTypeId + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
andOrFlag = true;
}

if (!(M03_Config.partitionLrtPrivateWhenNoMqt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M11_LRT.genProcSectionHeader(fileNo, "if MQT-table does not exist, partitioning is not supported on private tables", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals.g_anLdmIsLrt + ", " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_MQT." + M01_Globals.g_anPoolTypeId + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
andOrFlag = true;
}

if (!(M03_Config.partitionLrtPublicWhenNoMqt)) {
if (andOrFlag) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M11_LRT.genProcSectionHeader(fileNo, "if MQT-table does not exist, partitioning is not supported on public tables", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(L." + M01_Globals.g_anLdmIsLrt + ", " + M01_LDM.gc_dbTrue + ") = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_MQT." + M01_Globals.g_anPoolTypeId + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
andOrFlag = true;
}

if (andOrFlag) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "aet.isRangePartAll = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME = '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TYPE = 'T'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_doAddPartition = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_errMsg = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "check whether table supports partitioning by PS", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''" + M01_Globals_IVK.g_anPsOid + "'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_doAddPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_errMsg = 'not enabled for partitioning';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "check whether psOid_in is already covered by some partition", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WITH ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'V_P (TABSCHEMA, TABNAME, LOWVALUE, LOWINCLUSIVE, HIGHVALUE, HIGHINCLUSIVE) AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABSCHEMA,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN POSSTR(LOWVALUE, '','') > 0 THEN LEFT(LOWVALUE, COALESCE(POSSTR(LOWVALUE, '','')-1, LENGTH(LOWVALUE))) ELSE LOWVALUE END),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'LOWINCLUSIVE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN POSSTR(HIGHVALUE, '','') > 0 THEN LEFT(HIGHVALUE, COALESCE(POSSTR(HIGHVALUE, '','')-1, LENGTH(HIGHVALUE))) ELSE HIGHVALUE END),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'HIGHINCLUSIVE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SYSCAT.DATAPARTITIONS' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT 1 FROM V_P P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''" + M01_Globals_IVK.g_anPsOid + "'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND ((P.LOWINCLUSIVE  = ''Y'' AND P.LOWVALUE  <= RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))) OR (P.LOWINCLUSIVE  <> ''Y'' AND P.LOWVALUE  < RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))))' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND ((P.HIGHINCLUSIVE = ''Y'' AND P.HIGHVALUE >= RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))) OR (P.HIGHINCLUSIVE <> ''Y'' AND P.HIGHVALUE > RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))))' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' FETCH FIRST 1 ROW ONLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbTrue + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_doAddPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_errMsg = 'already covered by partition';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine Tablespace-names", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tablespaceClauseTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' IN ' || tbs_d.tbspace || ' INDEX IN ' || tbs_i.tbspace || ' LONG IN ' || tbs_l.tbspace");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.DATAPARTITIONS AS dp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "syscat.tablespaces AS tbs_d");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dp.tbspaceid = tbs_d.tbspaceid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "syscat.tablespaces AS tbs_i");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dp.index_tbspaceid = tbs_i.tbspaceid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "syscat.tablespaces AS tbs_l");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dp.long_tbspaceid = tbs_l.tbspaceid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dp.TABSCHEMA = c_tabSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dp.TABNAME = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

if (M03_Config.supportRangePartitioningByClassId) {
M11_LRT.genProcSectionHeader(fileNo, "determine whether table supports partitioning by CLASSID", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''" + M01_Globals.g_anCid + "'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tabSupportsPartByCId = COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ");");

M11_LRT.genProcSectionHeader(fileNo, "determine partitiong-clause of statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_tabSupportsPartByCId = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_psSupportsPartByCId = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntPartClauseTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''P'' || COALESCE(LBOUND, ''" + M22_Class_Utilities.getClassId(0, 0) + "'') || ' || '''' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(psOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'STARTING (' || RTRIM(CHAR(psOid_in)) || ','' || COALESCE('''''''' || LBOUND || '''''''', ''MINVALUE'') || '') INCLUSIVE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'ENDING (' || RTRIM(CHAR(psOid_in)) || ','' || COALESCE('''''''' || UBOUND || '''''''', ''MAXVALUE'') || '') INCLUSIVE'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M01_Globals_IVK.g_qualTabNameClassIdPartitionBoundaries + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'ORDER BY ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'COALESCE(LBOUND, ''" + M22_Class_Utilities.getClassId(0, 0) + "'')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntPartClauseTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'''P' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(psOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'STARTING (' || RTRIM(CHAR(psOid_in)) || ', MINVALUE) INCLUSIVE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'ENDING (' || RTRIM(CHAR(psOid_in)) || ', MAXVALUE) INCLUSIVE'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SYSIBM.SYSDUMMY1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntPartClauseTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''P' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(psOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SYSIBM.SYSDUMMY1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntPartClauseTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''P' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(psOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SYSIBM.SYSDUMMY1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "loop over partitions and assemble ALTER TABLE statement", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntPartClauseTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_partitionClauseTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE (SQLCODE = 0) DO");

M11_LRT.genProcSectionHeader(fileNo, "assemble ALTER TABLE statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' ADD PARTITION ' || v_partitionClauseTxt || v_tablespaceClauseTxt;");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE v_doAddPartition WHEN 0 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_errMsg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in >= 1 AND v_doAddPartition = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "COMMIT if requested", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF autoCommit_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "count statement", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + 1;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH c INTO v_partitionClauseTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 AND v_returnResult = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for PS-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByPs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameAddTablePartitionByPs + "(mode_in, psOid_in, tabSchema_in, tabName_in, 1, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for PS-tagged tables - for ALL PS in table ProductStructure", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByPs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "AddTabPartByPs", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over ProductStructures", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR psLoop AS psCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS." + M01_Globals.g_anOid + " AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "add table partitions for this specific ProductStructure", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcedureNameAddTablePartitionByPs + "((CASE WHEN mode_in IN (0,1) THEN (mode_in-2) ELSE mode_in END), c_psOid, tabSchema_in, tabName_in, 1, v_rowCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for PS-tagged tables - for ALL PS in table ProductStructure and all tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByPs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameAddTablePartitionByPs + "(mode_in, NULL, NULL, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for deleting a table partition for PS-tagged tables
// ####################################################################################################################

String qualProcedureNameDeleteTablePartitionByPs;
qualProcedureNameDeleteTablePartitionByPs = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnDeleteTablePartitionByPs, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for deleting a table partition for PS-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameDeleteTablePartitionByPs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_chkVal", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dbPartitionName", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_doDelPartition", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_errMsg", "VARCHAR(30)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 1, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 1, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR WITH HOLD FOR v_stmnt;");

String tempTabNameStatementDelTabPartitionByDiv;
tempTabNameStatementDelTabPartitionByDiv = M94_DBAdmin.tempTabNameStatement + "DelTabPartByDiv";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "DelTabPartByDiv", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning by PS", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, -2, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA     AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME       AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.COMPRESSION   AS c_tabCompression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TBSPACE       AS c_tbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.INDEX_TBSPACE AS c_indTbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.LONG_TBSPACE  AS c_longTbSpace");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME = '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TYPE = 'T'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_doDelPartition = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_errMsg = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "check whether table supports partitioning by PS", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''" + M01_Globals_IVK.g_anPsOid + "'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_doDelPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_errMsg = 'not enabled for partitioning';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "check whether psOid_in defines some partition", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_dbPartitionName = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WITH ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'V_DP ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DATAPARTITIONNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'LOWINCLUSIVE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'LOWVALUE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'HIGHINCLUSIVE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'HIGHVALUE' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DATAPARTITIONNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'LOWINCLUSIVE, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(LOWVALUE) = 1 THEN " + M01_Globals.g_dbtOid + "(LOWVALUE) ELSE ' || RTRIM(CHAR(psOid_in)) || '-1 END), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'HIGHINCLUSIVE, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(HIGHVALUE) = 1 THEN " + M01_Globals.g_dbtOid + "(HIGHVALUE) ELSE ' || RTRIM(CHAR(psOid_in)) || '+1 END) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SYSCAT.DATAPARTITIONS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'COALESCE(LOWVALUE, '''') <> '''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'OR ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'COALESCE(HIGHVALUE, '''') <> '''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABSCHEMA = ''' || c_tabSchema || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABNAME = ''' || c_tabName || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DATAPARTITIONNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'V_DP ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'((LOWINCLUSIVE  = ''Y'' AND LOWVALUE  <= ' || RTRIM(CHAR(psOid_in)) || ') OR (LOWINCLUSIVE  <> ''Y'' AND LOWVALUE  < ' || RTRIM(CHAR(psOid_in)) || ')) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'((HIGHINCLUSIVE = ''Y'' AND HIGHVALUE >= ' || RTRIM(CHAR(psOid_in)) || ') OR (HIGHINCLUSIVE <> ''Y'' AND HIGHVALUE > ' || RTRIM(CHAR(psOid_in)) || ')) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FETCH FIRST 1 ROW ONLY'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH c INTO v_dbPartitionName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_dbPartitionName IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_dbPartitionName = '???';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_doDelPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_errMsg = 'not covered by partition';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "assemble ALTER TABLE statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' DETACH PARTITION ' || v_dbPartitionName || ' INTO ' || RTRIM(c_tabSchema) || '.' || c_tabName || '_DET' || RTRIM(CHAR(psOid_in));");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStatementDelTabPartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE v_doDelPartition WHEN 0 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_errMsg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 AND v_doDelPartition = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementDelTabPartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, null, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


public static void genDbAdminPartitioningByDivDdlByDdlType(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

// ####################################################################################################################
// #    SP for configuring table partitioning (by DIV_OID)
// ####################################################################################################################

String qualProcedureNameTablePartCfg;
qualProcedureNameTablePartCfg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnSetTablePartCfgDiv, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for configuring table partitioning (by DIV_OID)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameTablePartCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "toggle_in", M01_Globals.g_dbtBoolean, true, "if set to '1' switch on table partitioning, otherwise switch off");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colDeclTxt", "VARCHAR(20000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_partitionClauseTxt", "VARCHAR(100)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_foundPartitionCrit", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "altObjError", "38553", null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR altObjError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

String tempTabNameStatementTabCfg;
tempTabNameStatementTabCfg = M94_DBAdmin.tempTabNameStatement + "TabPartCfg";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 20000, true, true, true, null, "TabPartCfg", null, null, true, null, "msg", "VARCHAR(2048)", "refId", "INTEGER");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameTablePartCfg, ddlType, null, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) AND (toggle_in = 1) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameTablePartCfg, ddlType, -2, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA     AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME       AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME       AS c_colName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.COMPRESSION   AS c_tabCompression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TBSPACE       AS c_tbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.INDEX_TBSPACE AS c_indTbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.LONG_TBSPACE  AS c_longTbSpace");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME LIKE '%DIV_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine partitiong-clause of statement for this table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_foundPartitionCrit = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_partitionClauseTxt = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF toggle_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR divLoop AS divCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "DIV." + M01_Globals.g_anOid + " AS c_divOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameDivision + " DIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "DIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_partitionClauseTxt = v_partitionClauseTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'PARTITION D' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(c_divOid), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") || ' STARTING ' || RTRIM(CHAR(c_divOid)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(c_divOid)) || ' INCLUSIVE'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_foundPartitionCrit = " + M01_LDM.gc_dbTrue + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_partitionClauseTxt = ' PARTITION BY RANGE (' || c_colName || ') (' || v_partitionClauseTxt || ')';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "assemble CREATE TABLE statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CREATE TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colDeclTxt = '';");

M11_LRT.genProcSectionHeader(fileNo, "loop over columns to assemble column-declarations", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS colCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.COLNAME   AS c_colName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TYPENAME  AS c_colType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.LENGTH    AS c_length,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.SCALE     AS c_scale,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.DEFAULT   AS c_default,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.NULLS     AS c_nulls,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.COMPRESS  AS c_compress,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.CONSTNAME AS c_constName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TYPE      AS c_constType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TEXT      AS c_constText");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS CO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLCHECKS CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.TABSCHEMA = CO.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.TABNAME = CO.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.COLNAME = CO.COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.CHECKS CH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TABSCHEMA = CC.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TABNAME = CC.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.CONSTNAME = CC.CONSTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CH.TYPE IN ('A', 'C')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TABSCHEMA = c_tabSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CO.TABNAME = c_tabName");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colDeclTxt = v_colDeclTxt || (CASE v_colDeclTxt WHEN '' THEN '' ELSE ', ' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_colName || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_colType ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN c_colType IN ('CHARACTER', 'VARCHAR', 'LONG VARCHAR', 'CLOB', 'BLOB', 'REAL')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN '(' || RTRIM(CHAR(c_length)) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN c_colType IN ('DECIMAL')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN '(' || RTRIM(CHAR(c_length)) || ',' || RTRIM(CHAR(c_scale)) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_nulls = 'N' THEN ' NOT NULL' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(' DEFAULT ' || c_default, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_compress = 'S' THEN ' COMPRESS SYSTEM DEFAULT' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_constType = 'C' THEN COALESCE(' CONSTRAINT ' || c_constName || ' CHECK (' || c_constText || ')', '') ELSE '' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "finalize CREATE TABLE statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || ' (' || v_colDeclTxt || ')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(' IN ' || c_tbSpace, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(' INDEX IN ' || c_indTbSpace, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN c_tabCompression IN ('V', 'B') THEN ' VALUE COMPRESSION' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' COMPRESS YES ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN v_foundPartitionCrit = 1 THEN v_partitionClauseTxt ELSE '' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', '' || REPLACE(v_stmntTxt, '''', '''''') || '', -1, ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjMsg", "VARCHAR(2048)", "''", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjId", "INTEGER", "-1", 4, null);
M11_LRT.genVarDecl(fileNo, "v_altObjStmntTxt", "VARCHAR(100)", "NULL", 4, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 4, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 4, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_altObjId = -1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_altObjStmntTxt = 'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ?, ?, ?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_altObjStmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjMsg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_altObjId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF mode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "refId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN NULL ELSE v_altObjId END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_altObjId = -1 THEN v_diagnostics ELSE v_altObjMsg END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ''' || REPLACE(v_stmntTxt, '''', '''''') || ''', -1, ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF mode_in = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementTabCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameTablePartCfg, ddlType, null, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for adding a table partition for DIV-tagged tables
// ####################################################################################################################

String qualProcedureNameAddTablePartitionByDiv;
qualProcedureNameAddTablePartitionByDiv = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnAddTablePartitionByDiv, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for DIV-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "divOid_in", M01_Globals.g_dbtOid, true, "OID of the Division to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "if set to '1' commit after each statement");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_chkVal", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_doAddPartition", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_errMsg", "VARCHAR(30)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_partitionClauseTxt", "VARCHAR(200)", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_returnResult", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 1, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 1, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR WITH HOLD FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

String tempTabNameStatementAddTabPart;
tempTabNameStatementAddTabPart = M94_DBAdmin.tempTabNameStatement + "AddTabPartByDiv";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, null, 200, true, true, true, null, "AddTabPartByDiv", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "hook: if mode_in = '-1' or '-2', suppress return of results, but fill temporary table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in < 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_returnResult = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET mode_in = mode_in + 2;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine partitiong-clause of statement", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_partitionClauseTxt = ' ADD PARTITION D' || RIGHT(DIGITS(" + M01_Globals.g_dbtOid + "(0)) || DIGITS(divOid_in), " + String.valueOf(M01_Globals.gc_maxDb2PartitionNameSuffixLen) + ") ||" + "' STARTING ' || RTRIM(CHAR(divOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(divOid_in)) || ' INCLUSIVE';");

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA     AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME       AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.COMPRESSION   AS c_tabCompression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TBSPACE       AS c_tbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.INDEX_TBSPACE AS c_indTbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.LONG_TBSPACE  AS c_longTbSpace");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME LIKE '%DIV_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TYPE = 'T'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME NOT LIKE 'PRODUCTSTRUCTURE%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME NOT LIKE 'DIVISION%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SYSCAT.COLUMNS CP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.COLNAME = '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_doAddPartition = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_errMsg = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "check whether table supports partitioning by DIV_OID", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT 1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_doAddPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_errMsg = 'not enabled for partitioning';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "check whether divOid_in is already covered by some partition", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT 1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND ((P.LOWINCLUSIVE  = ''Y'' AND P.LOWVALUE  <= RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))) OR (P.LOWINCLUSIVE  <> ''Y'' AND P.LOWVALUE  < RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))))' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND ((P.HIGHINCLUSIVE = ''Y'' AND P.HIGHVALUE >= RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))) OR (P.HIGHINCLUSIVE <> ''Y'' AND P.HIGHVALUE > RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))))' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbTrue + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_doAddPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_errMsg = 'already covered by partition';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "assemble ALTER TABLE statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || v_partitionClauseTxt;");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE v_doAddPartition WHEN 0 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_errMsg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 AND v_doAddPartition = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "COMMIT if requested", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF autoCommit_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "count statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 AND v_returnResult = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for DIV-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "divOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameAddTablePartitionByDiv + "(mode_in, divOid_in, tabSchema_in, tabName_in, 1, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for DIV-tagged tables - for ALL Divisions", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "AddTabPartByDiv", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over Divisions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR divLoop AS divCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DIV." + M01_Globals.g_anOid + " AS c_divOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDivision + " DIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "add table partitions for this specific Division", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcedureNameAddTablePartitionByDiv + "((CASE WHEN mode_in IN (0,1) THEN (mode_in-2) ELSE mode_in END), c_divOid, tabSchema_in, tabName_in, 1, v_rowCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementAddTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for adding a table partition for DIV-tagged tables - for ALL Divisions", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameAddTablePartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcedureNameAddTablePartitionByDiv + "(mode_in, NULL, NULL, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, null, "mode_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for deleting a table partition for DIV-tagged tables
// ####################################################################################################################

String qualProcedureNameDeleteTablePartitionByDiv;
qualProcedureNameDeleteTablePartitionByDiv = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnDeleteTablePartitionByDiv, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for deleting a table partition for DIV-tagged tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameDeleteTablePartitionByDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "divOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to add partitions for");
M11_LRT.genProcParm(fileNo, "IN", "tabSchema_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema name pattern of the table(s) to configure");
M11_LRT.genProcParm(fileNo, "IN", "tabName_in", M01_Globals.g_dbtDbTableName, true, "(optional) name pattern of the table to configure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of configuration statements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_chkVal", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dbPartitionName", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_doDelPartition", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_errMsg", "VARCHAR(30)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 1, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 1, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR WITH HOLD FOR v_stmnt;");

String tempTabNameStatementDelTabPart;
tempTabNameStatementDelTabPart = M94_DBAdmin.tempTabNameStatement + "DelTabPartByDiv";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "DelTabPartByDiv", null, null, true, null, "msg", "VARCHAR(30)", null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether DB supports table partitioning", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_db2Release < 9.05) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, -2, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over matching tables to configure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA     AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME       AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.COMPRESSION   AS c_tabCompression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TBSPACE       AS c_tbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.INDEX_TBSPACE AS c_indTbSpace,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.LONG_TBSPACE  AS c_longTbSpace");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME LIKE '%DIV_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TYPE = 'T'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '" + M01_Globals.g_allSchemaNamePattern + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME NOT LIKE 'PRODUCTSTRUCTURE%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME NOT LIKE 'DIVISION%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SYSCAT.COLUMNS CP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.TABSCHEMA = C.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CP.COLNAME = '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_doDelPartition = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_errMsg = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "check whether table supports partitioning by DIV_OID", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_chkVal = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT 1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "' FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH c INTO v_chkVal;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE c WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(v_chkVal, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_doDelPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_errMsg = 'not enabled for partitioning';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "check whether divOid_in defines some partition", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_dbPartitionName = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntChkTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WITH ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'V_DP ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DATAPARTITIONNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'LOWINCLUSIVE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'LOWVALUE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'HIGHINCLUSIVE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'HIGHVALUE' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'P.DATAPARTITIONNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'P.LOWINCLUSIVE, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(P.LOWVALUE) = 1 THEN " + M01_Globals.g_dbtOid + "(P.LOWVALUE) ELSE ' || RTRIM(CHAR(divOid_in)) || '-1 END), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'P.HIGHINCLUSIVE, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(CASE WHEN " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(P.HIGHVALUE) = 1 THEN " + M01_Globals.g_dbtOid + "(P.HIGHVALUE) ELSE ' || RTRIM(CHAR(divOid_in)) || '+1 END) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SYSCAT.DATAPARTITIONS P ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SYSCAT.DATAPARTITIONEXPRESSION E ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'COALESCE(LOWVALUE, '''') <> '''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'OR ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'COALESCE(HIGHVALUE, '''') <> '''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABSCHEMA = ''' || c_tabSchema || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'TABNAME = ''' || c_tabName || ''' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DATAPARTITIONNAME ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'V_DP ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'((LOWINCLUSIVE  = ''Y'' AND LOWVALUE  <= ' || RTRIM(CHAR(divOid_in)) || ') OR (LOWINCLUSIVE  <> ''Y'' AND LOWVALUE  < ' || RTRIM(CHAR(divOid_in)) || ')) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'((HIGHINCLUSIVE = ''Y'' AND HIGHVALUE >= ' || RTRIM(CHAR(divOid_in)) || ') OR (HIGHINCLUSIVE <> ''Y'' AND HIGHVALUE > ' || RTRIM(CHAR(divOid_in)) || ')) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FETCH FIRST 1 ROW ONLY'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntChkTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH c INTO v_dbPartitionName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_dbPartitionName IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_dbPartitionName = '???';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_doDelPartition = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_errMsg = 'not covered by partition';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "assemble ALTER TABLE statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' DETACH PARTITION ' || v_dbPartitionName || ' INTO ' || RTRIM(c_tabSchema) || '.' || c_tabName || '_DET' || RTRIM(CHAR(divOid_in));");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStatementDelTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "msg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE v_doDelPartition WHEN 0 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_errMsg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute configuration", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 AND v_doDelPartition = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS f,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementDelTabPart);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, null, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
// ### ENDIF IVK ###


}