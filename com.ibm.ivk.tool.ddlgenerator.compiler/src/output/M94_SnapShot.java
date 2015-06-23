package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M94_SnapShot {




private static final int processingStepAdmin = 4;

private static final String pc_tempTabNameSnRecords = "SESSION.Records";


public static void genDbSnapshotDdl(Integer ddlType) {
int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMonitor, processingStepAdmin, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

genDbSnapshotDdlUtilities(fileNo, ddlType);
genDbSnapshotDdlGetSnapshot(fileNo, ddlType);
genDbSnapshotDdlAdmin(fileNo, ddlType);
genDbSnapshotDdlAnalysis(fileNo, ddlType);
genDbEventMonitoringDdl(fileNo, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDbSnapshotDdlUtilities(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

boolean largeTables;
largeTables = false;
if (M03_Config.snapshotApiVersion.substring(0, 1) == "9") {
largeTables = true;
}

if (M03_Config.generateDdlCreateSeq) {
// ####################################################################################################################
// #    Sequence for SNAPSHOT IDs
//  ####################################################################################################################

String qualSeqNameSnapShotId;
qualSeqNameSnapShotId = M04_Utilities.genQualSeqName(M01_Globals.g_sectionIndexDbMonitor, M01_LDM.gc_seqNameSnapshotId, ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "create sequence for snapshot IDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE SEQUENCE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualSeqNameSnapShotId + " AS " + M01_Globals.g_dbtSequence);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "START WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCREMENT BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CYCLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CACHE 500");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    UDF for determining columns to be retrieved in snapshot table
// ####################################################################################################################

String qualFuncNameSnCols;
qualFuncNameSnCols = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnSnapshotCols, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for determining columns to be retrieved in snapshot table", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameSnCols);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tabName_in", M01_Globals.g_dbtDbTableName, true, "name of the snapshot table to retrieve columns for");
M11_LRT.genProcParm(fileNo, "", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "", "tabVariable_in", "VARCHAR(16)", false, "(optional) reference variable to use to qualify column");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + (largeTables ? "4096" : "2048") + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_colList", "VARCHAR(" + (largeTables ? "4096" : "2048") + ")", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_colPrefix", "VARCHAR(17)", "''", null, null);

M11_LRT.genProcSectionHeader(fileNo, "loop over columns related to the given table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.COLEXPRESSION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN RTRIM(C.COLALIAS       ) = '' THEN CAST(NULL AS VARCHAR(1)) ELSE C.COLALIAS END       ) AS COLALIAS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN RTRIM(C.DISPLAYFUNCNAME) = '' THEN CAST(NULL AS VARCHAR(1)) ELSE C.DISPLAYFUNCNAME END) AS DISPLAYFUNCNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotCol + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(UPPER(C.TABLENAME) = UPPER(tabName_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(category_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(C.CATEGORY, category_in) = category_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(level_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(C.LEVEL, level_in) >= level_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colList =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_colList ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN v_colList = '' THEN '' ELSE ',' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN DISPLAYFUNCNAME IS NULL THEN '' ELSE '" + M04_Utilities.getSchemaName(qualFuncNameSnCols) + ".' || DISPLAYFUNCNAME || '(' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(COLEXPRESSION, COLNAME) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN DISPLAYFUNCNAME IS NULL THEN '' ELSE ')' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE WHEN COLALIAS IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN (CASE WHEN DISPLAYFUNCNAME IS NULL AND COLEXPRESSION IS NULL THEN '' ELSE ' AS \"' || COLNAME || '\"' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE ' AS \"' || COLALIAS || '\"'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_colList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric application status to text (short)
// ####################################################################################################################

String qualFuncNameApplStatus2StrS;
qualFuncNameApplStatus2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting application status to text (short)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameApplStatus2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "applStatusNum_in", "BIGINT", false, "application status");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE applStatusNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'PIN' -- performing initialization");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'CPE' -- connect pending");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'CCO' -- connect completed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'UEX' -- UOW executing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'UWA' -- UOW waiting");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'LWT' -- lock wait");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'CAC' -- commit active");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'RAC' -- rollback active");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'RPL' -- recompiling plan");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'CSS' -- compiling SQL statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'RIN' -- request interrupted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'DPE' -- disconnect pending");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'PTR' -- Prepared transaction");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'HCO' -- heuristically committed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'HRB' -- heuristically rolled back");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'TEN' -- Transaction ended");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'CRD' -- Creating Database");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'RSD' -- Restarting Database");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'RDB' -- Restoring Database");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'PBK' -- Performing Backup");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'PFL' -- Performing fast load");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'PFU' -- Performing fast unload");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'WDT' -- Wait to disable tablespace");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'QTS' -- Quiescing tablespace");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'WRN' -- Waiting for remote node");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'PRR' -- Pending results from remote request");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 26 THEN 'ADC' -- App has been decoupled from coord");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 27 THEN 'RSP' -- Rollback to savepoint");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE         '???'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric application status to text (long)
// ####################################################################################################################

String qualFuncNameApplStatus2Str;
qualFuncNameApplStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting application status to text (long)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameApplStatus2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "applStatusNum_in", "BIGINT", false, "application status");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(35)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE applStatusNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'performing initialization'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'connect pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'connect completed'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'UOW executing'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'UOW waiting'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'lock wait'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'commit active'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'rollback active'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'recompiling plan'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'compiling SQL statement'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'request interrupted'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'disconnect pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Prepared transaction'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'heuristically committed'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'heuristically rolled back'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'Transaction ended'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'Creating Database'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'Restarting a Database'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Restoring a Database'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'Performing a Backup'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'Performing a fast load'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'Performing a fast unload'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'Wait to disable tablespace'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'Quiescing a tablespace'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'Waiting for remote node'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'Pending results from remote request'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 26 THEN 'App has been decoupled from coord'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 27 THEN 'Rollback to savepoint'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(applStatusNum_in AS CHAR(35)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric platform ID to text (short)
// ####################################################################################################################

String qualFuncNamePlatform2Str;
qualFuncNamePlatform2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnPlatform2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric platform ID to text (short)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePlatform2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "platformId_in", "BIGINT", false, "platform ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(5)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE platformId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'UNK'   -- Unknown platform");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'OS2'   -- OS/2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'DOS'   -- DOS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'WIN'   -- Windows");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'AIX'   -- AIX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'NT'    -- NT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'HP'    -- HP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'SUN'   -- Sun");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'MVS'   -- MVS (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN '400'   -- AS400 (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'VM'    -- VM (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'VSE'   -- VSE (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'UDRD'  -- Unknown DRDA Client");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'SNI'   -- Siemens Nixdorf");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'MacC'  -- Macintosh Client");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'W95'   -- Windows 95");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'SCO'   -- SCO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'SIGR'  -- Silicon Graphic");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'LINUX' -- Linux");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'DYNIX' -- DYNIX/ptx");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'AIX64' -- AIX 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'SUN64' -- Sun 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'HP64'  -- HP 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'NT64'  -- NT 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'L390'  -- Linux for S/390");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'L900'  -- Linux for z900");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 26 THEN 'LIA64' -- Linux for IA64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 27 THEN 'LPPC'  -- Linux for PPC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 28 THEN 'LPP64' -- Linux for PPC64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 29 THEN 'OS390' -- OS/390 Tools (CC, DW)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 30 THEN 'L8664' -- Linux for x86-64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 31 THEN 'HPI32' -- HP-UX Itanium 32bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 32 THEN 'HPI64' -- HP-UX Itanium 64bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 33 THEN 'S8632' -- Sun x86 32bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 34 THEN 'S8664' -- Sun x86-64 64bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(platformId_in AS CHAR(5)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric platform ID to text (long)
// ####################################################################################################################

qualFuncNamePlatform2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnPlatform2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric platform ID to text (long)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePlatform2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "platformId_in", "BIGINT", false, "platform ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(21)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE platformId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'Unknown platform'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'OS/2'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'DOS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'Windows'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'AIX'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'NT'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'HP'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'Sun'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'MVS (via DRDA)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'AS400 (via DRDA)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'VM (via DRDA)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'VSE (via DRDA)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Unknown DRDA Client'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'Siemens Nixdorf'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'Macintosh Client'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'Windows 95'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'SCO'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'Silicon Graphic'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Linux'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'DYNIX/ptx'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'AIX 64 bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'Sun 64 bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'HP 64 bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'NT 64 bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'Linux for S/390'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'Linux for z900'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 26 THEN 'Linux for IA64'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 27 THEN 'Linux for PPC'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 28 THEN 'Linux for PPC64'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 29 THEN 'OS/390 Tools (CC, DW)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 30 THEN 'Linux for x86-64'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 31 THEN 'HP-UX Itanium 32bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 32 THEN 'HP-UX Itanium 64bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 33 THEN 'Sun x86 32bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 34 THEN 'Sun x86-64 64bit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(platformId_in AS CHAR(21)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric protocol ID to text
// ####################################################################################################################

String qualFuncNameProtocol2Str;
qualFuncNameProtocol2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnProtocol2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric protocol ID to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameProtocol2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "protocolId_in", "BIGINT", false, "platform ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(17)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE protocolId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'APPC'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'NETBIOS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'APPN'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'TCPIP'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'APPC using CPIC'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'IPX/SPX'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'Local IPC'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'Named Pipe'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'TCPIP using SOCKS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'TCPIP using SSL'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(protocolId_in AS CHAR(17)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric database status to text
// ####################################################################################################################

String qualFuncNameDbStatus2Str;
qualFuncNameDbStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnDbStatus2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric database status to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameDbStatus2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "dbStatus_in", "BIGINT", false, "DB status ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(15)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE dbStatus_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'active'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Quiesce pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'quiesced'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'rolling forward'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(dbStatus_in AS CHAR(15)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric database manager status to text
// ####################################################################################################################

String qualFuncNameDbmStatus2Str;
qualFuncNameDbmStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnDbmStatus2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric database manager status to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameDbmStatus2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "dbmStatus_in", "BIGINT", false, "DB manager status ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(15)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE dbmStatus_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'active'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Quiesce pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'quiesced'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(dbmStatus_in AS CHAR(15)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric statement type to text (short)
// ####################################################################################################################

String qualFuncNameStmntType2StrS;
qualFuncNameStmntType2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric statement type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStmntType2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "statementTypeNum_in", "BIGINT", false, "numeric statement type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(7)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE statementTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Static'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'Dynamic'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'non-SQL'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'unknown'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(statementTypeNum_in AS CHAR(7)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric statement type to text (long)
// ####################################################################################################################

String qualFuncNameStmntType2Str;
qualFuncNameStmntType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric statement type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStmntType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "statementTypeNum_in", "BIGINT", false, "numeric statement type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(17)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE statementTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Static statement'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'Dynamic statement'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'other than SQL'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'unknown'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(statementTypeNum_in AS CHAR(17)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric statement operation to text (short)
// ####################################################################################################################

String qualFuncNameStmntOperation2StrS;
qualFuncNameStmntOperation2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric statement operation to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStmntOperation2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "statementOpNum_in", "BIGINT", false, "numeric statement operation");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(8)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE statementOpNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'SQL Pre'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'SQL Exe'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'SQL Imm'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'SQL Ope'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'SQL Ftc'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'SQL Clo'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'SQL Des'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'SQL Com'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'SQL Rbk'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'SQL Fre'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'Pre com'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Call SP'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'SELECT'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'Prep op'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'Prep ex'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Compile'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'SET'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'Runstats'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'Reorg'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'Rebind'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'Redist'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'GetTabAu'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'GetAdmAu'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(statementOpNum_in AS CHAR(7)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric statement operation to text (long)
// ####################################################################################################################

String qualFuncNameStmntOperation2Str;
qualFuncNameStmntOperation2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric statement operation to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStmntOperation2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "statementOpNum_in", "BIGINT", false, "numeric statement operation");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(35)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE statementOpNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'SQL Prepare'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'SQL Execute'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'SQL Execute Immediate'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'SQL Open'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'SQL Fetch'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'SQL Close'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'SQL Describe'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'SQL Static Commit'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'SQL Static Rollback'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'SQL Free Locator'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'Prepare to commit (2-phase commit)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Call a stored procedure'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'SELECT statement'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'Prep. and open (DB2 Connect only)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'Prep. and execute (DB2 Connect)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Compile (DB2 Connect only)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'SET statement'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 20 THEN 'Runstats'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 21 THEN 'Reorg'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 22 THEN 'Rebind package'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 23 THEN 'Redistribute'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 24 THEN 'Get Table Authorization'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 25 THEN 'Get Administrative Authorization'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(statementOpNum_in AS CHAR(35)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric lock mode to text (short)
// ####################################################################################################################

String qualFuncNameLockMode2StrS;
qualFuncNameLockMode2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockMode2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock mode to text (short)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockMode2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockModeNum_in", "BIGINT", false, "numeric lock mode");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockModeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN ''    -- No Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'IS'  -- Intention Share Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'IX'  -- Intention Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'S'   -- Share Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'SIX' -- Share with Intention Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'X'   -- Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'IN'  -- Intent None");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'Z'   -- Super Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'U'   -- Update Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'NS'  -- Next Key Share Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'NX'  -- Next Key Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'W'   -- Weak Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'NW'  -- Next Key Weak Exclusive Lock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE         '???'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric lock mode to text (long)
// ####################################################################################################################

String qualFuncNameLockMode2Str;
qualFuncNameLockMode2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockMode2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock mode to text (long)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockMode2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockModeNum_in", "BIGINT", false, "numeric lock mode");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(36)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockModeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'No Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Intention Share Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'Intention Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'Share Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'Share with Intention Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'Intent None'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'Super Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'Update Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'Next Key Share Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'Next Key Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'Weak Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Next Key Weak Exclusive Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(lockModeNum_in AS CHAR(36)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric lock object type to text
// ####################################################################################################################

String qualFuncNameLockObjType2Str;
qualFuncNameLockObjType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockObjType2Str, ddlType, null, null, null, null, null, null);
String qualFuncNameLockObjType2StrS;
qualFuncNameLockObjType2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockObjType2StrS, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock object type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockObjType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockObjTypeNum_in", "BIGINT", false, "numeric lock object type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(35)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockObjTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Table Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'Table Row Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'Internal Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'Tablespace Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'End of Table'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'Key Value Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'Internal Lock on the Sysboot Table'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'Internal Plan Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'Internal Variation Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'Internal Sequence Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'Bufferpool Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'Internal LONG/LOB Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'Internal Catalog Cache Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'Internal Online Backup Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'Internal Object Table Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'Internal Table Alter Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'Internal DMS Sequence Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Inplace Reorg Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'Block Lock'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(lockObjTypeNum_in AS CHAR(35)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock object type to text (short)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockObjType2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockObjTypeNum_in", "BIGINT", false, "numeric lock object type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(13)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockObjTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'Table'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'Row'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  3 THEN 'Internal'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  4 THEN 'TableSpace'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'End of Table'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'Key Value'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  7 THEN 'IntSysboot'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  8 THEN 'Plan'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  9 THEN 'Variation'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 10 THEN 'Sequence'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 11 THEN 'Bufferpool'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 12 THEN 'LONG/LOB'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 13 THEN 'Catalog Cache'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 14 THEN 'Online Backup'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 15 THEN 'Object Table'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 16 THEN 'Table Alter'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 17 THEN 'DMS Sequence'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 18 THEN 'Reorg'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 19 THEN 'Block'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(lockObjTypeNum_in AS CHAR(13)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric lock status to text
// ####################################################################################################################

String qualFuncNameLockStatus2Str;
String qualFuncNameLockStatus2StrS;
qualFuncNameLockStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockStatus2Str, ddlType, null, null, null, null, null, null);
qualFuncNameLockStatus2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockStatus2Str + "_S", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock status to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockStatus2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockStatusNum_in", "BIGINT", false, "numeric lock status");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(10)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockStatusNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'granted'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'converting'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(lockStatusNum_in AS CHAR(10)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for converting numeric lock status to text (short)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLockStatus2StrS);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "lockStatusNum_in", "BIGINT", false, "numeric lock status");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE lockStatusNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'GRA'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'CON'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(lockStatusNum_in AS CHAR(3)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric tablespace container type to text
// ####################################################################################################################

String qualFuncNameContType2Str;
qualFuncNameContType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnContType2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric tablespace container type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameContType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "containerTypeNum_in", "BIGINT", false, "container type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(17)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE containerTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'SMS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'DMS device (page)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  2 THEN 'DMS file (page)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  5 THEN 'DMS device (ext)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  6 THEN 'DMS file (ext)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(containerTypeNum_in AS CHAR(17)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric boolean to text
// ####################################################################################################################

String qualFuncNameBoolean2Str;
qualFuncNameBoolean2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnBoolean2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric boolean to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameBoolean2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "booleanNum_in", "BIGINT", false, "numeric boolean value (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE booleanNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  0 THEN 'no'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1 THEN 'yes'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(booleanNum_in AS CHAR(3)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric tablespace state to text
// ####################################################################################################################

String qualFuncNameTsState2Str;
qualFuncNameTsState2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnTsState2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric tablespace state to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameTsState2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tablespaceStateNum_in", "BIGINT", false, "numeric tablespace state");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(41)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE tableSpaceStateNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN           0  THEN 'Normal'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN           1  THEN 'Quiesced (SHARE)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN           2  THEN 'Quiesced (UPDATE)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN           4  THEN 'Quiesced (EXCLUSIVE)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN           8  THEN 'Load pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN          16  THEN 'Delete pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN          32  THEN 'Backup pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN          64  THEN 'Roll forward in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN         128  THEN 'Roll forward pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN         256  THEN 'Restore pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN         256  THEN 'Recovery pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN         512  THEN 'Disable pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN        1024  THEN 'Reorg in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN        2048  THEN 'Backup in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN        4096  THEN 'Storage must be defined'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN        8192  THEN 'Restore in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN       16384  THEN 'Offline and not accessible'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN       32768  THEN 'Drop pending'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN    33554432  THEN 'Storage may be defined'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN    67108864  THEN 'Storage Definition ''final'''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN   134217728  THEN 'Storage Def. changed prior to rollforward'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN   268435456  THEN 'DMS rebalancer active'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN   536870912  THEN 'TBS deletion in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN  1073741824  THEN 'TBS creation in progress'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(tableSpaceStateNum_in AS CHAR(41)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric tablespace content type to text
// ####################################################################################################################

String qualFuncNameTsContType2Str;
qualFuncNameTsContType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnTsContType2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric tablespace content type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameTsContType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tsContentTypeNum_in", "BIGINT", false, "numeric tablespace content type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(10)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE tsContentTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 0 THEN 'any'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 1 THEN 'long'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 2 THEN 'temp (sys)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 3 THEN 'temp (usr)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(tsContentTypeNum_in AS CHAR(10)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric tablespace type to text
// ####################################################################################################################

String qualFuncNameTsType2Str;
qualFuncNameTsType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnTsType2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric tablespace type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameTsType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tableSpaceTypeNum_in", "BIGINT", false, "numeric tablespace type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE tableSpaceTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 0 THEN 'DMS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 1 THEN 'SMS'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(tableSpaceTypeNum_in AS CHAR(3)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF for converting numeric table type to text
// ####################################################################################################################

String qualFuncNameTabType2Str;
qualFuncNameTabType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnTabType2Str, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for converting numeric table type to text", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameTabType2Str);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "tableTypeNum_in", "BIGINT", false, "numeric table type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(10)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE tableTypeNum_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 1 THEN 'user'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 2 THEN 'dropped'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 3 THEN 'temporary'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 4 THEN 'system'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN 5 THEN 'reorg'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE RTRIM(CAST(tableTypeNum_in AS CHAR(10)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genDdlForTempTablesSnapshotAnalysis(int fileNo, Integer ddlType, int maxRecordLength, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean withReplace; 
if (withReplaceW == null) {
withReplace = true;
} else {
withReplace = withReplaceW;
}

boolean onCommitPreserve; 
if (onCommitPreserveW == null) {
onCommitPreserve = false;
} else {
onCommitPreserve = onCommitPreserveW;
}

boolean onRollbackPreserve; 
if (onRollbackPreserveW == null) {
onRollbackPreserve = false;
} else {
onRollbackPreserve = onRollbackPreserveW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for analysis records retrieved", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "seqNo  INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "record VARCHAR(" + String.valueOf(maxRecordLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


private static void genDbSnapshotDdlGetSnapshot(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

// ### IF IVK ###
if (M03_Config.snapshotApiVersion.substring(0, 1) == "8") {
genDbSnapshotDdlGetSnapshotV8(fileNo, ddlType);
} else if (M03_Config.snapshotApiVersion.compareTo("9.7") == 0) {
genDbSnapshotDdlGetSnapshotV9_7(fileNo, ddlType);
}
// ### ELSE IVK ###
// genDbSnapshotDdlGetSnapshotV9_7 fileNo, ddlType
// ### ENDIF IVK ###
}


private static void genDbSnapshotDdlGetSnapshotV9_7(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

String qualFuncNameSnCols;
qualFuncNameSnCols = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnSnapshotCols, ddlType, null, null, null, null, null, null);
String qualSeqNameSnapShotId;
qualSeqNameSnapShotId = M04_Utilities.genQualSeqName(M01_Globals.g_sectionIndexDbMonitor, M01_LDM.gc_seqNameSnapshotId, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on database manager
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Dbm, M01_ACM.vnSnapshotV9Dbm, M01_ACM.vsnSnapshotV9Dbm, M01_ACM.clxnSnapshotV9Dbm, M01_ACM.clnSnapshotV9Dbm, "database manager", "SNAP_GET_DBM_V95", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on database manager memory pool
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9DbmMemoryPool, M01_ACM.vnSnapshotV9DbmMemoryPool, M01_ACM.vsnSnapshotV9DbmMemoryPool, M01_ACM.clxnSnapshotV9DbmMemoryPool, M01_ACM.clnSnapshotV9DbmMemoryPool, "database manager memory pool", "SNAP_GET_DBM_MEMORY_POOL ", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on database
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Db, M01_ACM.vnSnapshotV9Db, M01_ACM.vsnSnapshotV9Db, M01_ACM.clxnSnapshotV9Db, M01_ACM.clnSnapshotV9Db, "database", "SNAP_GET_DB_V97", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on database memory pool
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9DbMemoryPool, M01_ACM.vnSnapshotV9DbMemoryPool, M01_ACM.vsnSnapshotV9DbMemoryPool, M01_ACM.clxnSnapshotV9DbMemoryPool, M01_ACM.clnSnapshotV9DbMemoryPool, "database memory pool", "SNAP_GET_DB_MEMORY_POOL", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespaces
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9TbSp, M01_ACM.vnSnapshotV9TbSp, M01_ACM.vsnSnapshotV9TbSp, M01_ACM.clxnSnapshotV9TbSp, M01_ACM.clnSnapshotV9TbSp, "table spaces", "SNAP_GET_TBSP_V91", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespace partitions
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9TbSpPart, M01_ACM.vnSnapshotV9TbSpPart, M01_ACM.vsnSnapshotV9TbSpPart, M01_ACM.clxnSnapshotV9TbSpPart, M01_ACM.clnSnapshotV9TbSpPart, "table space partitions", "SNAP_GET_TBSP_PART_V97", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespace quiescer
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9TbSpQuiescer, M01_ACM.vnSnapshotV9TbSpQuiescer, M01_ACM.vsnSnapshotV9TbSpQuiescer, M01_ACM.clxnSnapshotV9TbSpQuiescer, M01_ACM.clnSnapshotV9TbSpQuiescer, "table space quiescer", "SNAP_GET_TBSP_QUIESCER", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespace range
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9TbSpRange, M01_ACM.vnSnapshotV9TbSpRange, M01_ACM.vsnSnapshotV9TbSpRange, M01_ACM.clxnSnapshotV9TbSpRange, M01_ACM.clnSnapshotV9TbSpRange, "table space range", "SNAP_GET_TBSP_RANGE", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on container
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Container, M01_ACM.vnSnapshotV9Container, M01_ACM.vsnSnapshotV9Container, M01_ACM.clxnSnapshotV9Container, M01_ACM.clnSnapshotV9Container, "container", "SNAP_GET_CONTAINER_V91", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on bufferpools
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Bp, M01_ACM.vnSnapshotV9Bp, M01_ACM.vsnSnapshotV9Bp, M01_ACM.clxnSnapshotV9Bp, M01_ACM.clnSnapshotV9Bp, "buffer pools", "SNAP_GET_BP_V95", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on bufferpool partitions
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9BpPart, M01_ACM.vnSnapshotV9BpPart, M01_ACM.vsnSnapshotV9BpPart, M01_ACM.clxnSnapshotV9BpPart, M01_ACM.clnSnapshotV9BpPart, "buffer pool partitions", "SNAP_GET_BP_PART", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tables
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Tab, M01_ACM.vnSnapshotV9Tab, M01_ACM.vsnSnapshotV9Tab, M01_ACM.clxnSnapshotV9Tab, M01_ACM.clnSnapshotV9Tab, "tables", "SNAP_GET_TAB_V91", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on table reorg
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9TabReorg, M01_ACM.vnSnapshotV9TabReorg, M01_ACM.vsnSnapshotV9TabReorg, M01_ACM.clxnSnapshotV9TabReorg, M01_ACM.clnSnapshotV9TabReorg, "table reorg", "SNAP_GET_TAB_REORG", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on agents
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Agent, M01_ACM.vnSnapshotV9Agent, M01_ACM.vsnSnapshotV9Agent, M01_ACM.clxnSnapshotV9Agent, M01_ACM.clnSnapshotV9Agent, "agents", "SNAP_GET_AGENT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on agent memory pools
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9AgentMemoryPool, M01_ACM.vnSnapshotV9AgentMemoryPool, M01_ACM.vsnSnapshotV9AgentMemoryPool, M01_ACM.clxnSnapshotV9AgentMemoryPool, M01_ACM.clnSnapshotV9AgentMemoryPool, "agent memory pools", "SNAP_GET_AGENT_MEMORY_POOL", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on applications
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Appl, M01_ACM.vnSnapshotV9Appl, M01_ACM.vsnSnapshotV9Appl, M01_ACM.clxnSnapshotV9Appl, M01_ACM.clnSnapshotV9Appl, "applications", "SNAP_GET_APPL_V95", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on application infos
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9ApplInfo, M01_ACM.vnSnapshotV9ApplInfo, M01_ACM.vsnSnapshotV9ApplInfo, M01_ACM.clxnSnapshotV9ApplInfo, M01_ACM.clnSnapshotV9ApplInfo, "application infos", "SNAP_GET_APPL_INFO_V95", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on locks
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Lock, M01_ACM.vnSnapshotV9Lock, M01_ACM.vsnSnapshotV9Lock, M01_ACM.clxnSnapshotV9Lock, M01_ACM.clnSnapshotV9Lock, "locks", "SNAP_GET_LOCK", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on lock waits
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9LockWait, M01_ACM.vnSnapshotV9LockWait, M01_ACM.vsnSnapshotV9LockWait, M01_ACM.clxnSnapshotV9LockWait, M01_ACM.clnSnapshotV9LockWait, "lock waits", "SNAP_GET_LOCKWAIT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on statements
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Stmt, M01_ACM.vnSnapshotV9Statement, M01_ACM.vsnSnapshotV9Statement, M01_ACM.clxnSnapshotV9Statement, M01_ACM.clnSnapshotV9Statement, "statements", "SNAP_GET_STMT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on dynamic SQL statements
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9DynSql, M01_ACM.vnSnapshotV9DynSql, M01_ACM.vsnSnapshotV9DynSql, M01_ACM.clxnSnapshotV9DynSql, M01_ACM.clnSnapshotV9DynSql, "dynamic SQL statements", "SNAP_GET_DYN_SQL_V91", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on detail log
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9DetailLog, M01_ACM.vnSnapshotV9DetailLog, M01_ACM.vsnSnapshotV9DetailLog, M01_ACM.clxnSnapshotV9DetailLog, M01_ACM.clnSnapshotV9DetailLog, "detail log", "SNAP_GET_DETAILLOG_V91", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on fcm (fast communication manager)
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Fcm, M01_ACM.vnSnapshotV9Fcm, M01_ACM.vsnSnapshotV9Fcm, M01_ACM.clxnSnapshotV9Fcm, M01_ACM.clnSnapshotV9Fcm, "fcm (fast communication manager)", "SNAP_GET_FCM", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on fcm part (fast communication manager)
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9FcmPart, M01_ACM.vnSnapshotV9FcmPart, M01_ACM.vsnSnapshotV9FcmPart, M01_ACM.clxnSnapshotV9FcmPart, M01_ACM.clnSnapshotV9FcmPart, "fcm part (fast communication manager)", "SNAP_GET_FCM_PART", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on hadr
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Hadr, M01_ACM.vnSnapshotV9Hadr, M01_ACM.vsnSnapshotV9Hadr, M01_ACM.clxnSnapshotV9Hadr, M01_ACM.clnSnapshotV9Hadr, "HADR", "SNAP_GET_HADR", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on storage path
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9StoragePaths, M01_ACM.vnSnapshotV9StoragePaths, M01_ACM.vsnSnapshotV9StoragePaths, M01_ACM.clxnSnapshotV9StoragePaths, M01_ACM.clnSnapshotV9StoragePaths, "storage paths", "SNAP_GET_STORAGE_PATHS_V97", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on subsection
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Subsection, M01_ACM.vnSnapshotV9Subsection, M01_ACM.vsnSnapshotV9Subsection, M01_ACM.clxnSnapshotV9Subsection, M01_ACM.clnSnapshotV9Subsection, "subsection", "SNAP_GET_SUBSECTION", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on switches
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Switches, M01_ACM.vnSnapshotV9Switches, M01_ACM.vsnSnapshotV9Switches, M01_ACM.clxnSnapshotV9Switches, M01_ACM.clnSnapshotV9Switches, "switches", "SNAP_GET_SWITCHES", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on util
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9Util, M01_ACM.vnSnapshotV9Util, M01_ACM.vsnSnapshotV9Util, M01_ACM.clxnSnapshotV9Util, M01_ACM.clnSnapshotV9Util, "util", "SNAP_GET_UTIL", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on util progress
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotV9UtilProgress, M01_ACM.vnSnapshotV9UtilProgress, M01_ACM.vsnSnapshotV9UtilProgress, M01_ACM.clxnSnapshotV9UtilProgress, M01_ACM.clnSnapshotV9UtilProgress, "util progress", "SNAP_GET_UTIL_PROGRESS", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

M24_Attribute_Utilities.AttributeListTransformation transformation;

// ####################################################################################################################
// #    SP for retrieving collective snapshot information
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshot, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optionally) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "useLogging_in", M01_Globals.g_dbtBoolean, true, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 30");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "create snapshot ID if none is provided", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >=1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET snapshotId_inout = NEXTVAL FOR " + qualSeqNameSnapShotId + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSnapshotHandle);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SNAPSHOT_TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET snapshotId_inout = (SELECT MAX(ID) FROM " + M01_Globals.g_qualTabNameSnapshotHandle + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RETURN;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = UR;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot procedures", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS procCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.PROCNAME       AS c_procName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ISAPPLSPECIFIC AS c_isApplSpecific");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(agentId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.ISAPPLSPECIFIC = 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(category_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(T.CATEGORY, category_in) = category_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(level_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(T.LEVEL, level_in) >= level_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "call snapshot procedure", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (c_isApplSpecific = 1) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || c_procName || ' (?,?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "useLogging_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "agentId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "category_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || c_procName || ' (?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "useLogging_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "category_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "count rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "reset ISOLATION LEVEL", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = RESET;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optionally) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 30");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, 0, agentId_in, category_in, level_in, tabCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information (short parameter list)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "useLogging_in", M01_Globals.g_dbtBoolean, false, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 30");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_agentId", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_category", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_level", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, useLogging_in, v_agentId, v_category, v_level, v_tabCount, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information (short parameter list)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", false, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 30");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_agentId", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_category", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_level", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, 0, v_agentId, v_category, v_level, v_tabCount, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
private static void genDbSnapshotDdlGetSnapshotV8(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

String qualFuncNameSnCols;
qualFuncNameSnCols = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnSnapshotCols, ddlType, null, null, null, null, null, null);
String qualSeqNameSnapShotId;
qualSeqNameSnapShotId = M04_Utilities.genQualSeqName(M01_Globals.g_sectionIndexDbMonitor, M01_LDM.gc_seqNameSnapshotId, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on database manager
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotDbm, M01_ACM_IVK.vnSnapshotV8Dbm, M01_ACM_IVK.vsnSnapshotV8Dbm, M01_ACM_IVK.clxnSnapshotV8Dbm, M01_ACM_IVK.clnSnapshotV8Dbm, "database manager", "SNAPSHOT_DBM", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, false);

// ####################################################################################################################
// #    SP for retrieving snapshot on database
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotDb, M01_ACM_IVK.vnSnapshotV8Db, M01_ACM_IVK.vsnSnapshotV8Db, M01_ACM_IVK.clxnSnapshotV8Db, M01_ACM_IVK.clnSnapshotV8Db, "database", "SNAPSHOT_DATABASE", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespace configuration
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotTbsCfg, M01_ACM_IVK.vnSnapshotV8TbsCfg, M01_ACM_IVK.vsnSnapshotV8TbsCfg, M01_ACM_IVK.clxnSnapshotV8TbsCfg, M01_ACM_IVK.clnSnapshotV8TbsCfg, "tablespace configuration", "SNAPSHOT_TBS_CFG", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tablespaces
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotTbs, M01_ACM_IVK.vnSnapshotV8Tbs, M01_ACM_IVK.vsnSnapshotV8Tbs, M01_ACM_IVK.clxnSnapshotV8Tbs, M01_ACM_IVK.clnSnapshotV8Tbs, "table spaces", "SNAPSHOT_TBS", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on container
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotContainer, M01_ACM_IVK.vnSnapshotV8Container, M01_ACM_IVK.vsnSnapshotV8Container, M01_ACM_IVK.clxnSnapshotV8Container, M01_ACM_IVK.clnSnapshotV8Container, "container", "SNAPSHOT_CONTAINER", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on bufferpools
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotBufferpool, M01_ACM_IVK.vnSnapshotV8Bufferpool, M01_ACM_IVK.vsnSnapshotV8Bufferpool, M01_ACM_IVK.clxnSnapshotV8BufferPool, M01_ACM_IVK.clnSnapshotV8BufferPool, "buffer pools", "SNAPSHOT_BP", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on tables
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotTable, M01_ACM_IVK.vnSnapshotV8Table, M01_ACM_IVK.vsnSnapshotV8Table, M01_ACM_IVK.clxnSnapshotV8Table, M01_ACM_IVK.clnSnapshotV8Table, "tables", "SNAPSHOT_TABLE", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on agents
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotAgent, M01_ACM_IVK.vnSnapshotV8Agent, M01_ACM_IVK.vsnSnapshotV8Agent, M01_ACM_IVK.clxnSnapshotV8Agent, M01_ACM_IVK.clnSnapshotV8Agent, "agents", "SNAPSHOT_AGENT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on locks
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotLock, M01_ACM_IVK.vnSnapshotV8Lock, M01_ACM_IVK.vsnSnapshotV8Lock, M01_ACM_IVK.clxnSnapshotV8Lock, M01_ACM_IVK.clnSnapshotV8Lock, "locks", "SNAPSHOT_LOCK", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on lock waits
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotLockWait, M01_ACM_IVK.vnSnapshotV8LockWait, M01_ACM_IVK.vsnSnapshotV8LockWait, M01_ACM_IVK.clxnSnapshotV8LockWait, M01_ACM_IVK.clnSnapshotV8LockWait, "lock waits", "SNAPSHOT_LOCKWAIT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on applications
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotAppl, M01_ACM_IVK.vnSnapshotV8SnapshotAppl, M01_ACM_IVK.vsnSnapshotV8SnapshotAppl, M01_ACM_IVK.clxnSnapshotV8Appl, M01_ACM_IVK.clnSnapshotV8Appl, "applications", "SNAPSHOT_APPL", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on application infos
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotApplInfo, M01_ACM_IVK.vnSnapshotV8ApplInfo, M01_ACM_IVK.vsnSnapshotV8ApplInfo, M01_ACM_IVK.clxnSnapshotV8ApplInfo, M01_ACM_IVK.clnSnapshotV8ApplInfo, "application infos", "SNAPSHOT_APPL_INFO", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on statements
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotStatement, M01_ACM_IVK.vnSnapshotV8Statement, M01_ACM_IVK.vsnSnapshotV8Statement, M01_ACM_IVK.clxnSnapshotV8Statement, M01_ACM_IVK.clnSnapshotV8Statement, "statements", "SNAPSHOT_STATEMENT", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, true, null);

// ####################################################################################################################
// #    SP for retrieving snapshot on dynamic SQL statements
// ####################################################################################################################

genGetSnapshotForXyzDdlV(fileNo, ddlType, M01_ACM.spnGetSnapshotSql, M01_ACM_IVK.vnSnapshotV8Sql, M01_ACM_IVK.vsnSnapshotV8Sql, M01_ACM_IVK.clxnSnapshotV8Sql, M01_ACM_IVK.clnSnapshotV8Sql, "dynamic SQL statements", "SNAPSHOT_DYN_SQL", qualFuncNameSnCols, qualSeqNameSnapShotId, M01_Globals.g_qualTabNameSnapshotType, M01_Globals.g_qualTabNameSnapshotFilter, M01_Globals.g_qualTabNameSnapshotHandle, false, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;

// ####################################################################################################################
// #    SP for retrieving collective snapshot information
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optionally) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "useLogging_in", M01_Globals.g_dbtBoolean, true, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "create snapshot ID if none is provided", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >=1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET snapshotId_inout = NEXTVAL FOR " + qualSeqNameSnapShotId + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSnapshotHandle);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SNAPSHOT_TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET snapshotId_inout = (SELECT MAX(ID) FROM " + M01_Globals.g_qualTabNameSnapshotHandle + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RETURN;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = UR;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot procedures", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS procCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.PROCNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ISAPPLSPECIFIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(agentId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.ISAPPLSPECIFIC = 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(category_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(T.CATEGORY, category_in) = category_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(level_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(T.LEVEL, level_in) >= level_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "call snapshot procedure", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF ISAPPLSPECIFIC = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || PROCNAME || ' (?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "agentId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "category_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || PROCNAME || ' (?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "useLogging_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "category_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "count rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "reset ISOLATION LEVEL", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = RESET;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optionally) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, 0, agentId_in, category_in, level_in, tabCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information (short parameter list)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "useLogging_in", M01_Globals.g_dbtBoolean, false, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_agentId", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_category", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_level", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, useLogging_in, v_agentId, v_category, v_level, v_tabCount, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving collective snapshot information (short parameter list)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshot);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", false, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_agentId", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_category", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_level", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshot + "(snapshotId_inout, mode_in, 0, v_agentId, v_category, v_level, v_tabCount, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshot, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### ENDIF IVK ###
private static void genRecreateSnapshotTabDdl(int fileNo, int seqNo, Integer acmEntityType, int acmEntityIndex, String tempTabNameCrTabStmnt, Integer ddlTypeW, String viewNameW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String viewName; 
if (viewNameW == null) {
viewName = null;
} else {
viewName = viewNameW;
}

String qualProcNameRevalidate;
qualProcNameRevalidate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnRevalidate, ddlType, null, null, null, "VIEWS", null, null);

String qualProcNameSetGrants;
qualProcNameSetGrants = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnGrant, ddlType, null, null, null, "Fltr", M04_Utilities.ObjNameDelimMode.eondmNone, null);

String qualTabName;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabName = M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, null, null, null, null, null, null, null, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
qualTabName = M04_Utilities.genQualTabNameByRelIndex(acmEntityIndex, ddlType, null, null, null, null, null, null, null, null);
} else {
return;
}

M11_LRT.genProcSectionHeader(fileNo, "DROP-Statement for table \"" + qualTabName + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'DROP TABLE " + qualTabName + "';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tempTabNameCrTabStmnt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + String.valueOf(seqNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "CREATE-Statement for table \"" + qualTabName + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CREATE TABLE ' || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + qualTabName + " ' || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'(' || CHR(10) ||");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
String columnDefault;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.trimRight = false;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

String attributeShortName;
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
columnDefault = "";
attributeShortName = "???";
if (tabColumns.descriptors[i].acmAttributeIndex > 0) {
columnDefault = M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].defaultValue;
attributeShortName = M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].shortName;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M00_Helper.replace(M04_Utilities.genTransformedAttrDeclByDomain(tabColumns.descriptors[i].acmAttributeName, attributeShortName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformation, acmEntityType, acmEntityIndex, (tabColumns.descriptors[i].isNullable ? "" : "NOT NULL") + (columnDefault.compareTo("") == 0 ? "" : " DEFAULT " + columnDefault), false, ddlType, null, null, null, null, 0, null, null, null, null, null, null), "'", "''") + (i < tabColumns.numDescriptors ? "," : "") + "' || CHR(10) ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "') ' || CHR(10) ||");
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceData.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexData, null, null, null) + " ' || CHR(10) ||");
}
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceLong.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'LONG IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexLong, null, null, null) + " ' || CHR(10) ||");
}
if (M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndex != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'INDEX IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexIndex, null, null, null) + " ' || CHR(10) ||");
}
if (M22_Class.g_classes.descriptors[acmEntityIndex].useValueCompression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'VALUE COMPRESSION' ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'COMPRESS YES' ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "'';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tempTabNameCrTabStmnt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "300000 + " + String.valueOf(seqNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "700000 + " + String.valueOf(seqNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'CALL " + qualProcNameSetGrants + "(2, ''" + M04_Utilities.getSchemaName(qualTabName) + "%'', ''" + M04_Utilities.getUnqualObjName(qualTabName) + "'', ?)'");
if (!(viewName.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "400000 + " + String.valueOf(seqNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'CALL " + qualProcNameRevalidate + "(''" + M01_Globals.g_schemaNameCtoDbMonitor + "%'', ''" + viewName.toUpperCase() + "'', 2, ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "600000 + " + String.valueOf(seqNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'CALL " + qualProcNameSetGrants + "(2, ''" + M01_Globals.g_schemaNameCtoDbMonitor + "%'', ''" + viewName.toUpperCase() + "'', ?)'");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNameSetGrants + "(2, '" + M04_Utilities.getSchemaName(qualTabName) + "%', '" + M04_Utilities.getUnqualObjName(qualTabName) + "', v_grantCount);");

if (!(viewName.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNameRevalidate + "('" + M01_Globals.g_schemaNameCtoDbMonitor + "%', '" + viewName.toUpperCase() + "', 2, v_viewCount);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNameSetGrants + "(2, '" + M01_Globals.g_schemaNameCtoDbMonitor + "%', '" + viewName.toUpperCase() + "', v_grantCount);");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}


private static void genDbSnapshotDdlAdmin(int fileNo, Integer ddlTypeW) {
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

// ####################################################################################################################
// #    SP for creating snapshot-views
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGenViewSnapshot, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for creating snapshot-views", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", false, "(optional) level to use for column filtering");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "level_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET level_in = COALESCE(level_in, 0);");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot views", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS procCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.VIEWNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ISAPPLSPECIFIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || REPLACE(VIEWNAME, 'V_', 'GENVIEW_') || ' (?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "level_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for cleaning up snapshot data
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnSnapshotClear, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for cleaning up snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optionally) only snapshot data 'before' this snapshot is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "before_in", "TIMESTAMP", true, "(optionally) only snapshot data before this timestamp is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only");
M11_LRT.genProcParm(fileNo, "IN", "commitCount_in", "INTEGER", true, "number of rows to delete before commit (0 = no commit, -1 disable logging + final commit)");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_in", "#before_in", "mode_in", "commitCount_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET commitCount_in = COALESCE(commitCount_in, 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out   = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot tables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABLENAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "cleanup snapshot table", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'DELETE FROM " + M04_Utilities.getSchemaName(M01_Globals.g_qualTabNameSnapshotType) + ".' || TABLENAME;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF snapshotId_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' WHERE (SID <= ' || RTRIM(CHAR(snapshotId_in)) || ')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF before_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || (CASE WHEN snapshotId_in IS NULL THEN ' WHERE' ELSE ' AND' END) || ' (SNAPSHOT_TIMESTAMP <= ''' || RTRIM(CHAR(before_in)) || ''')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M94_DBAdmin.tempTabNameStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tabCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF commitCount_in > 0 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = REPLACE(v_stmntTxt, 'DELETE FROM', 'DELETE FROM (SELECT * FROM') || ' FETCH FIRST ' || RTRIM(CHAR(commitCount_in)) || ' ROWS ONLY)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_rowCount = commitCount_in;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHILE v_rowCount = commitCount_in DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "commit if logging is disabled (to minimize risk of unaccessible table)", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF commitCount_in < 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M94_DBAdmin.tempTabNameStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_in", "#before_in", "mode_in", "commitCount_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for cleaning up snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optionally) only snapshot data 'before' this snapshot is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "before_in", "TIMESTAMP", true, "(optionally) only snapshot data before this timestamp is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_in", "#before_in", "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(snapshotId_in, before_in, mode_in, -1, tabCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_in", "#before_in", "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for cleaning up snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "before_in", "TIMESTAMP", true, "(optionally) only snapshot data before this timestamp is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "before_in", "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(NULL, before_in, mode_in, -1, tabCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "before_in", "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for cleaning up snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of snapshot tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(NULL, NULL, mode_in, -1, tabCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "mode_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for re-creating Snapshot-Tables
// ####################################################################################################################

String qualProcNameReCreateSnapshots;
qualProcNameReCreateSnapshots = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnReCreateSnapshotTables, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for re-creating Snapshot-Tables", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameReCreateSnapshots);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "tableCount_out", "INTEGER", false, "number of tables re-created");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "doesNotExist", "42704", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_returnResult", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);
M11_LRT.genVarDecl(fileNo, "v_tableCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_viewCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_grantCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR doesNotExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_returnResult = " + M01_LDM.gc_dbFalse + "; -- just fill the table");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameReCreateSnapshots, ddlType, null, "mode_in", "tableCount_out", null, null, null, null, null, null, null, null, null, null);

String tempTabNameCrTabStmnt;
tempTabNameCrTabStmnt = M94_DBAdmin.tempTabNameStatement + "CrTab";
M94_DBAdmin.genDdlForTempStatement(fileNo, 1, false, 30000, null, true, null, null, "CrTab", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "SET output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tableCount_out = " + String.valueOf(M79_SnapshotType.g_snapshotTypes.numDescriptors) + ";");

String qualProcNameSetGrants;
qualProcNameSetGrants = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnGrant, ddlType, null, null, null, "Fltr", M04_Utilities.ObjNameDelimMode.eondmNone, null);

String tabSpaceNameSnapshot;
tabSpaceNameSnapshot = "";
int classIndexSnapshot;
String qualTabName;
int j;
for (int j = 1; j <= M79_SnapshotType.g_snapshotTypes.numDescriptors; j++) {
classIndexSnapshot = M79_SnapshotType.g_snapshotTypes.descriptors[j].classIndex;

if (tabSpaceNameSnapshot.compareTo("") == 0) {
tabSpaceNameSnapshot = M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceData;
}

genRecreateSnapshotTabDdl(fileNo, j, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndexSnapshot, tempTabNameCrTabStmnt, ddlType, M79_SnapshotType.g_snapshotTypes.descriptors[j].viewName);
}

int i;
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (!(M22_Class.g_classes.descriptors[i].tabSpaceData.compareTo(tabSpaceNameSnapshot) == 0) |  M22_Class.g_classes.descriptors[i].sectionName.compareTo(M01_ACM.snDbMonitor) == 0) {
goto NextI;
}

genRecreateSnapshotTabDdl(fileNo, j, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[i].classIndex, tempTabNameCrTabStmnt, ddlType, null);
j = j + 1;
NextI:
}

for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
if (!(M23_Relationship.g_relationships.descriptors[i].tabSpaceData.compareTo(tabSpaceNameSnapshot) == 0) |  M23_Relationship.g_relationships.descriptors[i].sectionName.compareTo(M01_ACM.snDbMonitor) == 0) {
goto NextII;
}

genRecreateSnapshotTabDdl(fileNo, j, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[i].relIndex, tempTabNameCrTabStmnt, ddlType, null);
j = j + 1;
NextII:
}

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 AND v_returnResult = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameCrTabStmnt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameReCreateSnapshots, ddlType, null, "mode_in", "tableCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for pruning Snapshot Monitor Tablespace
// ####################################################################################################################

String qualProcNamePruneSnapshots;
qualProcNamePruneSnapshots = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnSnapshotPrune, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for pruning Snapshot-Monitor-Tablespace", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePruneSnapshots);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "containerSize_in", "INTEGER", true, "size of tablespace container to allocate initially");
M11_LRT.genProcParm(fileNo, "OUT", "tableCount_out", "INTEGER", false, "number of tables re-created");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "doesNotExist", "42704", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsPageSize", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsType", "CHAR(1)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsBufferpoolName", "VARCHAR(128)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsExtentSize", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsPrefetchSize", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsOverhead", "DOUBLE", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tsTransferRate", "DOUBLE", "NULL", null, null);

M11_LRT.genVarDecl(fileNo, "v_containerFound", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_grantCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxtDropTs", "VARCHAR(300)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR doesNotExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

tempTabNameCrTabStmnt = M94_DBAdmin.tempTabNameStatement + "CrTab";
M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 30000, null, true, null, null, "CrTab", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "set output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tableCount_out = " + String.valueOf(M79_SnapshotType.g_snapshotTypes.numDescriptors) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET containerSize_in = COALESCE(containerSize_in, 100000);");

int thisTabSpaceIndex;
String tsNameList;
tsNameList = "";
for (int thisTabSpaceIndex = 1; thisTabSpaceIndex <= M73_TableSpace.g_tableSpaces.numDescriptors; thisTabSpaceIndex++) {
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].isMonitor) {
tsNameList = tsNameList + (tsNameList.compareTo("") == 0 ? "" : ", ") + "'" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "'";
}
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePruneSnapshots, ddlType, null, "mode_in", "containerSize_in", "tableCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "Drop all tables in tablespaces \"" + tsNameList + "\"", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY T.TABSCHEMA ASC, T.TABNAME ASC) TABNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(T.TBSPACE) IN (" + tsNameList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(T.INDEX_TBSPACE) IN (" + tsNameList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(T.LONG_TBSPACE) IN (" + tsNameList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'DROP TABLE ' || RTRIM(TABSCHEMA) || '.' || TABNAME;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

int tsNo;
tsNo = 1;
for (int thisTabSpaceIndex = 1; thisTabSpaceIndex <= M73_TableSpace.g_tableSpaces.numDescriptors; thisTabSpaceIndex++) {
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].isMonitor) {
M11_LRT.genProcSectionHeader(fileNo, "determine statements for drop and create of tablespace \"" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxtDropTs = 'DROP TABLESPACE " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.PAGESIZE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.TBSPACETYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "B.BPNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.EXTENTSIZE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.PREFETCHSIZE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.OVERHEAD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.TRANSFERRATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsPageSize,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsBufferpoolName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsExtentSize,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsPrefetchSize,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsOverhead,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tsTransferRate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSCAT.TABLESPACES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSCAT.BUFFERPOOLS B");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.BUFFERPOOLID = B.BUFFERPOOLID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPPER(T.TBSPACE) = '" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tsPageSize       = COALESCE(v_tsPageSize       , " + (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].pageSize.compareTo("") == 0) ? M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].pageSize : "4096") + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tsType           = COALESCE(v_tsType           , '" + (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].category == M73_TableSpace_Utilities.TabSpaceCategory.tscDms ? "D" : "S") + "');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tsBufferpoolName = COALESCE(v_tsBufferpoolName , '" + M04_Utilities.genBufferPoolNameByIndex(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].bufferPoolIndex, null, null, null) + "');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tsExtentSize     = COALESCE(v_tsExtentSize     , " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].extentSize + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tsPrefetchSize   = COALESCE(v_tsPrefetchSize   , " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].prefetchSize + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CREATE " + (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].type.compareTo("") == 0) ? M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].type.toUpperCase() + " " : "") + "TABLESPACE ' || CHR(10) ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + " ' || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'PAGESIZE ' || CHAR(v_tsPageSize) || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'MANAGED BY ' || (CASE v_tsType WHEN 'D' THEN 'DATABASE ' ELSE 'SYSTEM ' END) || CHR(10) ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'USING ( ' || CHR(10);");

M11_LRT.genProcSectionHeader(fileNo, "determine tablespace container", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_containerFound = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR containerLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.CONTAINER_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.CONTAINER_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY C.CONTAINER_NAME ASC) CNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE(SYSPROC.SNAPSHOT_CONTAINER(CURRENT SERVER,-1)) C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.TABLESPACE_NAME = '" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY C.CONTAINER_NAME DESC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_containerFound = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF CONTAINER_TYPE = 0 THEN -- SMS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '  ''' || CONTAINER_NAME || '''' || CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE -- DMS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF CONTAINER_TYPE IN (2,6) THEN -- DMS / File");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = v_stmntTxt || '  FILE ''' || CONTAINER_NAME || ''' ' || CHAR(containerSize_in) || (CASE CNO WHEN 1 THEN ' ' ELSE ', ' END) || CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE -- DMS / Disk");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = v_stmntTxt || '  DEVICE ''' || CONTAINER_NAME || ''' ' || CHAR(containerSize_in) || (CASE CNO WHEN 1 THEN ' ' ELSE ', ' END) || CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_containerFound = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || ') ' || CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_tsType = 'D' THEN");
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].autoResize) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AUTORESIZE YES ' || CHR(10) ||");
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increasePercent > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INCREASESIZE " + String.valueOf(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increasePercent) + " PERCENT ' || CHR(10) ||");
} else if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increaseAbsolute.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INCREASESIZE " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increaseAbsolute + " ' || CHR(10) ||");
}
if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].maxSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'MAXSIZE " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].maxSize + " ' || CHR(10) ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt ||");

int numContainerRefs;
numContainerRefs = M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.numDescriptors;
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].category == M73_TableSpace_Utilities.TabSpaceCategory.tscSms) {
for (int j = 1; j <= numContainerRefs; j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M04_Utilities.genContainerNameByIndex(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.descriptors[j], null, null, null) + "'" + (j == numContainerRefs ? "" : ",") + " ' || CHR(10 ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "') ' || CHR(10) ||");
} else {
for (int j = 1; j <= numContainerRefs; j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'  " + (M74_Container.g_containers.descriptors[M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.descriptors[j]].type == M74_Container_Utilities.containerType.cntFile ? "FILE" : "DEVICE") + " " + "''" + M04_Utilities.genContainerNameByIndex(M74_Container.g_containers.descriptors[M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.descriptors[j]].containerIndex, null, null, null) + "''" + " " + String.valueOf(M74_Container.g_containers.descriptors[M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.descriptors[j]].size) + (j == numContainerRefs ? "" : ",") + " ' || chr(10) ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "') ' || CHR(10) ||");

if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].autoResize) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'AUTORESIZE YES ' || CHR(10) ||");

if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increasePercent > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INCREASESIZE " + String.valueOf(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increasePercent) + " PERCENT ' || CHR(10) ||");
} else if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increaseAbsolute.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INCREASESIZE " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].increaseAbsolute + " ' || CHR(10) ||");
}

if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].maxSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'MAXSIZE " + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].maxSize + " ' || CHR(10) ||");
}
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = v_stmntTxt ||");

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].extentSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'EXTENTSIZE ' || CHAR(v_tsExtentSize) || CHR(10) ||");
}
if (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].prefetchSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'PREFETCHSIZE ' || CHAR(v_tsPrefetchSize) || CHR(10) ||");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'BUFFERPOOL " + M04_Utilities.genBufferPoolNameByIndex(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].bufferPoolIndex, null, null, null) + " ' || CHR(10) ||");

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + (!(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].useFileSystemCaching) ? "NO " : "") + "FILE SYSTEM CACHING ' || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN v_tsOverhead IS NULL THEN '' ELSE 'OVERHEAD ' || CHAR(v_tsOverhead) || ' ' END) || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN v_tsTransferRate IS NULL THEN '' ELSE 'TRANSFERRATE ' || CHAR(v_tsTransferRate) || ' ' END) || CHR(10) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'DROPPED TABLE RECOVERY " + (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].supportDroppedTableRecovery ? "ON" : "OFF") + " ' || CHR(10) ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "store statements in temporary table", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempTabNameCrTabStmnt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(100000 + tsNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmntTxtDropTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(200000 + tsNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(500000 + tsNo) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CALL " + qualProcNameSetGrants + "(2, NULL, ''" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "'', ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "commit changes", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNo, "drop tablespace", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxtDropTs;");

M11_LRT.genProcSectionHeader(fileNo, "commit changes", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNo, "create tablespace", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "set GRANTs on tablespace", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNameSetGrants + "(2, NULL, '" + M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() + "', v_grantCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

tsNo = tsNo + 1;
}
}

M11_LRT.genProcSectionHeader(fileNo, "recreate Snapshot tables", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameReCreateSnapshots + "(mode_in, tableCount_out);");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameCrTabStmnt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePruneSnapshots, ddlType, null, "mode_in", "containerSize_in", "tableCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

qualProcNamePruneSnapshots = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnSnapshotPrune, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for pruning Snapshot-Monitor-Tablespace", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePruneSnapshots);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "tableCount_out", "INTEGER", false, "number of tables re-created");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePruneSnapshots, ddlType, null, "mode_in", "tableCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNamePruneSnapshots + "(mode_in, 100000, tableCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePruneSnapshots, ddlType, null, "mode_in", "tableCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDbSnapshotDdlAnalysis(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

// ### IF IVK ###
if (M03_Config.snapshotApiVersion.substring(0, 1) == "8") {
genDbSnapshotDdlAnalysisV8(fileNo, ddlType);
} else if (M03_Config.snapshotApiVersion.compareTo("9.7") == 0) {
genDbSnapshotDdlAnalysisV9_7(fileNo, ddlType);
}
// ### ELSE IVK ###
// genDbSnapshotDdlAnalysisV9_7 fileNo, ddlType
// ### ENDIF IVK ###
}


private static void genDbSnapshotDdlAnalysisV9_7(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
// we do not support this for LDM
return;
}

//On Error GoTo ErrorExit 

// ####################################################################################################################
// #    SP for analyzing LOCK-WAIT snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing LOCK-WAIT snapshot data", fileNo, null, null);

String qualFuncNameLockMode2Str;
qualFuncNameLockMode2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockMode2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameLockObjType2Str;
qualFuncNameLockObjType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockObjType2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameStmntType2Str;
qualFuncNameStmntType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameStmntType2StrS;
qualFuncNameStmntType2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str + "_S", ddlType, null, null, null, null, null, null);

String qualFuncNameApplStatus2Str;
qualFuncNameApplStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameApplStatus2StrS;
qualFuncNameApplStatus2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str + "_S", ddlType, null, null, null, null, null, null);

String qualFuncNameStmntOperation2Str;
qualFuncNameStmntOperation2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameStmntOperation2StrS;
qualFuncNameStmntOperation2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str + "_S", ddlType, null, null, null, null, null, null);

final int maxRecordLength = 8000;

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "ignored for this procedure");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_delimLine", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_emptyLine", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstLine", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "VARCHAR(" + String.valueOf(2 * maxRecordLength) + ")", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_agentLoopCount", "SMALLINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, maxRecordLength, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_delimLine = REPEAT('-', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_emptyLine = REPEAT(' ', 100);");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snWtLoop AS snWtCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID                  L_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SNAPSHOT_TIMESTAMP   L_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID             L_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID_HOLDING_LK  L_AGENT_ID_HOLDING_LK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_WAIT_START_TIME L_LOCK_WAIT_START_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_MODE            L_LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_OBJECT_TYPE     LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_MODE_REQUESTED  L_LOCK_MODE_REQUESTED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TBSP_NAME            L_TBSP_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TABSCHEMA            L_TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TABNAME              L_TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.APPL_STATUS         AW_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.APPL_ID             AW_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.PRIMARY_AUTH_ID     AW_PRIMARY_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.SESSION_AUTH_ID     AW_SESSION_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.CLIENT_NNAME        AW_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_USERID AW_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_WKSTN  AW_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_APP    AW_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_ACC_STR       AW_TPMON_ACC_STR,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.APPL_STATUS         AH_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.APPL_ID             AH_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.PRIMARY_AUTH_ID     AH_PRIMARY_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.SESSION_AUTH_ID     AH_SESSION_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.CLIENT_NNAME        AH_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_USERID AH_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_WKSTN  AH_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_APP    AH_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_ACC_STR       AH_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotLockWait + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " AW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID = AW.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID = AW.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID = AH.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID_HOLDING_LK = AH.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(snapshotId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(L.SID = snapshotId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(L_SID)), '') || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Timestamp               : ' || COALESCE(RTRIM(CHAR(L_SNAPSHOT_TIMESTAMP)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Wait Start Time    : ' || COALESCE(RTRIM(CHAR(L_LOCK_WAIT_START_TIME)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Object Type        : ' || COALESCE(" + qualFuncNameLockObjType2Str + "(LOCK_OBJECT_TYPE), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Mode               : ' || COALESCE(" + qualFuncNameLockMode2Str + "(L_LOCK_MODE), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Mode Requested     : ' || COALESCE(" + qualFuncNameLockMode2Str + "(L_LOCK_MODE_REQUESTED), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Space             : ' || COALESCE(L_TBSP_NAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Schema            : ' || COALESCE(L_TABSCHEMA, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Name              : ' || COALESCE(L_TABNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_agentLoopCount = 1;");

M11_LRT.genProcSectionHeader(fileNo, "loop over agents: 1 = agent waiting for lock, 2 = agent holding lock", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REPEAT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_agentLoopCount = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   'Agent waiting for Lock  : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AW_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(AW_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID (P)  : ' || COALESCE(AW_PRIMARY_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID (S)  : ' || COALESCE(AW_SESSION_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_NNAME          : ' || COALESCE(AW_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_USERID         : ' || COALESCE(AW_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AW_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_APPLNAME       : ' || COALESCE(AW_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_ACCTNG         : ' || COALESCE(AW_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   'Agent holding Lock      : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID_HOLDING_LK)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AH_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(AH_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID (P)  : ' || COALESCE(AH_PRIMARY_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID (S)  : ' || COALESCE(AH_SESSION_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_NNAME          : ' || COALESCE(AH_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_USERID         : ' || COALESCE(AH_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AH_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_APPLNAME       : ' || COALESCE(AH_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_ACCTNG         : ' || COALESCE(AH_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all statements related to this agent", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_firstLine = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR snStLoop AS snStCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.ROWS_READ,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.ROWS_WRITTEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_OPERATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_START,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_STOP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS ELAPSED_TIME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID = L_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.AGENT_ID = (CASE v_agentLoopCount WHEN 1 THEN L_AGENT_ID ELSE L_AGENT_ID_HOLDING_LK END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_START");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_firstLine = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Statement Start ', 27) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Statement Stop ' , 28) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Type '           ,  9) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Op '             ,  9) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Rows Read '      , 13) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Rows Written '   , 13) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Time Elapsed '   , 18) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   ' STATEMENT' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 27) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 28) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine,  9) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine,  9) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 13) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 13) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 18) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 81) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_firstLine = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CHAR(STMT_START),''),26) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CHAR(STMT_STOP),''),26) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(" + qualFuncNameStmntType2StrS + "(STMT_TYPE),''), 7) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(" + qualFuncNameStmntOperation2StrS + "(STMT_OPERATION),''), 7) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_READ)),''),11) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_WRITTEN)),''),11) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CAST(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                     CAST(SECOND(ELAPSED_TIME) + 60 * (MINUTE(ELAPSED_TIME) + 60 * (HOUR(ELAPSED_TIME) + 24 * DAY(ELAPSED_TIME))) + CAST(MICROSECOND(ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   AS CHAR(16)),''),16) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   COALESCE(REPLACE(LEFT(STMT_TEXT,80), CHR(10), ' '), '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_agentLoopCount = v_agentLoopCount + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNTIL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_agentLoopCount = 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END REPEAT;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(CLOB(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_thisRecord || v_nl)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ", " + String.valueOf(maxRecordLength));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M22_Class_Utilities.printSectionHeader("SP for analyzing LOCK-WAIT snapshot data", fileNo, null, null);

// ####################################################################################################################

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait + "(snapshotId_in, 0, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing Application snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Appplication snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "if '0' retrieve records involving inconsistencies, if '1' retrieve all records");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_delimLine", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtIdStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousLrtIdStr", "VARCHAR(25)", "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_orgIdLrtStr", "VARCHAR(2)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgIdLockStr", "VARCHAR(2)", "NULL", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_qualTabNameLock", "VARCHAR(100)", "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_orgIdStmntStr", "VARCHAR(2)", "NULL", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_creatorStmnt", "VARCHAR(20)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "VARCHAR(80)", "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOidStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousPsOidStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOidLrtStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousCdUserId", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserIdLrt", M01_Globals.g_dbtUserId, "NULL", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousRecord", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecordInfo", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousRecordInfo", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousApplStatus", "SMALLINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousAgentId", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstSid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousSid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstTimeStamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_outputPreviousRecord", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(300)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, 2048, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_delimLine = REPEAT('-', 100);");

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snAppLoop AS snAppCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SID                 A_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SNAPSHOT_TIMESTAMP  A_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_STATUS         A_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AGENT_ID            A_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_ID             A_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.PRIMARY_AUTH_ID     A_PRIMARY_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SESSION_AUTH_ID     A_SESSION_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.CLIENT_NNAME        A_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_USERID A_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_WKSTN  A_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_APP    A_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_ACC_STR       A_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(snapshotId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A.SID = snapshotId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AGENT_ID ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SID      ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecordInfo = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_firstSid       = COALESCE(v_firstSid,       A_SID);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_firstTimeStamp = COALESCE(v_firstTimeStamp, A_SNAPSHOT_TIMESTAMP);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lrtIdStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_WKSTN ),  25));");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psOidStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_APP   ),  25));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cdUserId = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_USERID), 100));");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_orgIdLrtStr     = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_orgIdLockStr    = NULL;");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_qualTabNameLock = NULL;");
M00_FileWriter.printToFile(fileNo, "");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF LENGTH(v_lrtIdStr) > LENGTH('" + M01_LDM.gc_sequenceMinValue + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_orgIdLrtStr = LEFT(v_lrtIdStr, LENGTH(v_lrtIdStr) - LENGTH('" + M01_LDM.gc_sequenceMinValue + "'));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF LENGTH(v_orgIdLrtStr) = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_orgIdLrtStr = '0' || v_orgIdLrtStr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(v_orgIdLrtStr) = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt  = 'SELECT RTRIM(CHAR(L." + M01_Globals_IVK.g_anPsOid + ")), U." + M01_Globals.g_anUserId + " FROM " + M04_Utilities.genSchemaName(M01_ACM.snLrt, M01_ACM.ssnLrt, ddlType, null, null) + "' || RTRIM(CHAR(v_orgIdLrtStr)) || '" + String.valueOf(M01_Globals.g_workDataPoolId) + ".LRT L LEFT OUTER JOIN " + M01_Globals.g_qualTabNameUser + " U ON U." + M01_Globals.g_anOid + " = L.UTROWN_OID WHERE L." + M01_Globals.g_anOid + " = ' || v_lrtIdStr || ' WITH UR';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_psOidLrtStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_cdUserIdLrt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(V.tabSchema) || '.' || RTRIM(V.tabName)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgIdLockStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_qualTabNameLock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.TABSCHEMA AS tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.TABNAME AS tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(RIGHT(L.TABSCHEMA, 3), 2) AS orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN LEFT(RIGHT(L.TABSCHEMA, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotLock + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.SID = A_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.AGENT_ID = A_AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(LEFT(RIGHT(L.TABSCHEMA, 3), 2)) = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(L.TABSCHEMA, 3) = '" + M03_Config.productKey + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sortCrit ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.creator,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.stmt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgIdStmntStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_creatorStmnt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(LEFT(S.CREATOR, 20)) AS creator,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS stmt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(LEFT(RIGHT(S.CREATOR, 3), 2)) AS orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN LEFT(RIGHT(S.CREATOR, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID = A_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.AGENT_ID = A_AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(LEFT(RIGHT(S.CREATOR, 3), 2)) = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(S.CREATOR, 3) = '" + M03_Config.productKey + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sortCrit ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdLockStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    ORG-ID according to LRT-OID-Register <-> ''locked Table'' : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdLockStr || COALESCE(' (' || v_qualTabNameLock || ')', '') || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdStmntStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    ORG-ID according to LRT-OID-Register <-> ''static SQL''   : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdStmntStr || COALESCE(' (' || v_creatorStmnt || ' / ' || v_stmnt || ')', '') || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_psOidStr IS NOT NULL AND v_psOidLrtStr IS NOT NULL AND v_psOidStr <> v_psOidLrtStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    PS-OID according to PS-OID-Register  <-> LRT            : ' || v_psOidStr || ' <-> ' || v_psOidLrtStr || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_cdUserId IS NOT NULL AND v_cdUserIdLrt IS NOT NULL AND v_cdUserId <> v_cdUserIdLrt THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    CD-UserId according to UID-Register  <-> LRT            : ''' || v_cdUserId || ''' <-> ''' || v_cdUserIdLrt || '''' || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_outputPreviousRecord = (CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (v_previousRecordInfo IS NOT NULL AND v_previousRecordInfo <> v_thisRecordInfo)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (v_previousApplStatus IS NOT NULL AND v_previousApplStatus <> A_APPL_STATUS)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousAgentId, -1) <> A_AGENT_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousLrtIdStr, '') <> COALESCE(v_lrtIdStr, ''))");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousPsOidStr, '') <> COALESCE(v_psOidStr, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousCdUserId, '') <> COALESCE(v_cdUserId, ''))");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               (mode_in = 1 OR v_previousRecordInfo <> '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                             THEN 1 ELSE 0 END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecord = v_thisRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord =  LEFT(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(v_firstSid)), '') || (CASE WHEN v_firstSid <> A_SID THEN ' - ' || COALESCE(RTRIM(CHAR(A_SID)), '') ELSE '' END) || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Timestamp               : ' || COALESCE(RTRIM(CHAR(v_firstTimeStamp)), '') || (CASE WHEN v_firstTimeStamp <> A_SNAPSHOT_TIMESTAMP THEN ' - ' || COALESCE(RTRIM(CHAR(A_SNAPSHOT_TIMESTAMP)), '') ELSE '' END) || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Agent                   : ' || COALESCE(RTRIM(CHAR(A_AGENT_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Application Id          : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Application Status      : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(A_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Authorization ID (P)    : ' || COALESCE(A_PRIMARY_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Authorization ID (S)    : ' || COALESCE(A_SESSION_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_NNAME            : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_USERID           : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_WRKSTNNAME       : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_APPLNAME         : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_ACCTNG           : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "(CASE WHEN v_thisRecordInfo = '' THEN '' ELSE '  Inconsistencies       : ' || v_nl || v_nl || v_thisRecordInfo || v_nl END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + ", 1024");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + ");");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_outputPreviousRecord = 1 THEN");

M11_LRT.genProcSectionHeader(fileNo, "add statement related infos", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR snStmtLoop AS snStmtCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS S_STMT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.SID >= v_firstSid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.SID <= COALESCE(v_previousSid, v_firstSid))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.STMT_TEXT IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.AGENT_ID = A_AGENT_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FETCH FIRST 10 ROWS ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_previousRecord = v_previousRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(17) + "'      > ' || S_STMT || v_nl;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_previousRecord || v_nl || v_delimLine || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_previousSid   = A_SID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_firstSid      = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecord     = v_thisRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecordInfo = v_thisRecordInfo;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousApplStatus = A_APPL_STATUS;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousAgentId    = A_AGENT_ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousLrtIdStr   = v_lrtIdStr;");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousPsOidStr   = v_psOidStr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousCdUserId   = v_cdUserId;");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_outputPreviousRecord = (CASE WHEN (v_firstSid IS NOT NULL) AND (mode_in = 1 OR v_previousRecordInfo <> '') THEN 1 ELSE 0 END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_outputPreviousRecord = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_previousRecord");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out  = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing STATEMENT snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

final int maxRecordLengthStmnt = 32000;

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotIdStart_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "snapshotIdEnd_in", "BIGINT", true, "(optional) identifies the last snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", true, "(optional) identifies the fime of the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "endTime_in", "TIMESTAMP", true, "(optional) identifies the fime of the last snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "determines the level of details provided (0=low, 1=medium, 2=high)");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_headLine1", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_sidDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_agentDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmtDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "CLOB(100M)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecordLck", "CLOB(100M)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmtNo", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordLength", "INTEGER", String.valueOf(maxRecordLengthStmnt), null, null);
M11_LRT.genVarDecl(fileNo, "v_lastSid", "BIGINT", "-1", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastAgentId", "BIGINT", "-1", null, null);
M11_LRT.genVarDecl(fileNo, "v_numLocks", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, maxRecordLengthStmnt, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_sidDelimLine = REPEAT('#', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_agentDelimLine = REPEAT('=', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmtDelimLine = REPEAT('-', 100);");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snStmntLoop AS snAppCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID                  S_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SNAPSHOT_TIMESTAMP   S_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID             S_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_TEXT            S_STMT_TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.ROWS_READ            S_ROWS_READ,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.ROWS_WRITTEN         S_ROWS_WRITTEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_TYPE            S_STMT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_OPERATION       S_STMT_OPERATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.QUERY_COST_ESTIMATE  S_QUERY_COST_ESTIMATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.QUERY_CARD_ESTIMATE  S_QUERY_CARD_ESTIMATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_SORTS           S_STMT_SORTS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.TOTAL_SORT_TIME      S_TOTAL_SORT_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SORT_OVERFLOWS       S_SORT_OVERFLOWS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_DELETED     S_INT_ROWS_DELETED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_UPDATED     S_INT_ROWS_UPDATED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_INSERTED    S_INT_ROWS_INSERTED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_START           S_STMT_START,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_STOP            S_STMT_STOP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS S_ELAPSED_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_STATUS          A_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_ID              A_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.PRIMARY_AUTH_ID      A_PRIMARY_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SESSION_AUTH_ID      A_SESSION_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.CLIENT_NNAME         A_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_USERID  A_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_WKSTN   A_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_APP     A_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_ACC_STR        A_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID = A.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID = A.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(snapshotIdStart_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SID >= snapshotIdStart_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(snapshotIdEnd_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SID <= snapshotIdEnd_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(startTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SNAPSHOT_TIMESTAMP >= startTime_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(endTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SNAPSHOT_TIMESTAMP <= endTime_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(agentId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.AGENT_ID = agentId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(mode_in > 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(STMT_TEXT IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID        ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID   ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_START ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "retrieve LOCK-infos for previous Statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (v_lastSid <> S_SID OR v_lastAgentId <> S_AGENT_ID) AND v_lastSid > 0 AND v_lastAgentId > 0 THEN");
genSaveLockInfoDdl(fileNo, 3, M01_Globals.g_qualTabNameSnapshotLock, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_nl;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lastSid <> S_SID THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_sidDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(S_SID                            )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Timestamp             : ' || COALESCE(RTRIM(CHAR(S_SNAPSHOT_TIMESTAMP             )), '') || v_nl || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastSid = S_SID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastAgentId = -1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmtNo = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lastAgentId <> S_AGENT_ID THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_agentDelimLine || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Agent Id                : ' || COALESCE(RTRIM(CHAR(S_AGENT_ID                       )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Application Id        : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(A_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Authorization ID (P)  : ' || COALESCE(A_PRIMARY_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Authorization ID (S)  : ' || COALESCE(A_SESSION_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_NNAME          : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_USERID         : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_WRKSTNNAME     : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_APPLNAME       : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_ACCTNG         : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastAgentId = S_AGENT_ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmtNo = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_thisRecord || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " v_stmtDelimLine || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement               : ' || COALESCE(RTRIM(CHAR(v_stmtNo)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Stop        : ' || COALESCE(CHAR(S_STMT_STOP ),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Time Elapsed          : ' || COALESCE(CAST(CAST(SECOND(S_ELAPSED_TIME) + 60 * (MINUTE(S_ELAPSED_TIME) + 60 * (HOUR(S_ELAPSED_TIME) + 24 * DAY(S_ELAPSED_TIME))) + CAST(MICROSECOND(S_ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6)) AS CHAR(16)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Type                  : ' || COALESCE(" + qualFuncNameStmntType2Str + "(S_STMT_TYPE),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Op                    : ' || COALESCE(" + qualFuncNameStmntOperation2Str + "(S_STMT_OPERATION),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Read             : ' || COALESCE(RTRIM(CHAR(S_ROWS_READ)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Written          : ' || COALESCE(RTRIM(CHAR(S_ROWS_WRITTEN)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Query Cost (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_COST_ESTIMATE)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Query Card (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_CARD_ESTIMATE)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Sorts       : ' || COALESCE(RTRIM(CHAR(S_STMT_SORTS)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Total Sort Time       : ' || COALESCE(RTRIM(CHAR(S_TOTAL_SORT_TIME)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Sort Overflows        : ' || COALESCE(RTRIM(CHAR(S_SORT_OVERFLOWS)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Deleted (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_DELETED)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Updated (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_UPDATED)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Inserted (int.)  : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_INSERTED)),'') || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement Text          : ' || v_nl || v_nl || COALESCE(RTRIM(LEFT(REPLACE(S_STMT_TEXT, CHR(10), ' '), 120)), '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement Text          : ' || v_nl || v_nl || COALESCE(S_STMT_TEXT, '');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmtNo = v_stmtNo + 1;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + pc_tempTabNameSnRecords + "(record) VALUES (LEFT(CLOB(v_thisRecord || v_nl),  v_recordLength));");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "retrieve LOCK-infos for last Statement", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lastSid > 0 AND v_lastAgentId > 0 THEN");
genSaveLockInfoDdl(fileNo, 2, M01_Globals.g_qualTabNameSnapshotLock, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement + "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, 0, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "determines the level of details provided (0=low, 1=medium, 2=high)");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement + "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, mode_in, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysis, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot types supporting analysis", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS procCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "REPLACE(PROCNAME, 'GETSNAPSHOT', 'GETSNAPSHOTANALYSIS') AS PROCNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SUPPORTANALYSIS = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualProcNameGetSnapshotAnalysis) + ".' || PROCNAME || '(?,0,?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_recordCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "snapshotId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + v_recordCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysis, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
private static void genDbSnapshotDdlAnalysisV8(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
// we do not support this for LDM
return;
}

//On Error GoTo ErrorExit 

// ####################################################################################################################
// #    SP for analyzing LOCK-WAIT snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing LOCK-WAIT snapshot data", fileNo, null, null);

String qualFuncNameLockMode2Str;
qualFuncNameLockMode2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockMode2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameLockObjType2Str;
qualFuncNameLockObjType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockObjType2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameStmntType2StrS;
qualFuncNameStmntType2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str + "_S", ddlType, null, null, null, null, null, null);

String qualFuncNameStmntType2Str;
qualFuncNameStmntType2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntType2Str, ddlType, null, null, null, null, null, null);

String qualFuncNameApplStatus2Str;
String qualFuncNameApplStatus2StrS;
qualFuncNameApplStatus2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str, ddlType, null, null, null, null, null, null);
qualFuncNameApplStatus2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnApplStatus2Str + "_S", ddlType, null, null, null, null, null, null);

String qualFuncNameStmntOperation2StrS;
qualFuncNameStmntOperation2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str + "_S", ddlType, null, null, null, null, null, null);

String qualFuncNameStmntOperation2Str;
qualFuncNameStmntOperation2Str = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnStmntOp2Str, ddlType, null, null, null, null, null, null);

final int maxRecordLength = 8000;

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "ignored for this procedure");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_delimLine", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_emptyLine", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstLine", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "VARCHAR(" + String.valueOf(2 * maxRecordLength) + ")", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_agentLoopCount", "SMALLINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, maxRecordLength, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_delimLine = REPEAT('-', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_emptyLine = REPEAT(' ', 100);");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snWtLoop AS snWtCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID                  L_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SNAPSHOT_TIMESTAMP   L_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID             L_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID_HOLDING_LK  L_AGENT_ID_HOLDING_LK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_WAIT_START_TIME L_LOCK_WAIT_START_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_MODE            L_LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_OBJECT_TYPE     LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.LOCK_MODE_REQUESTED  L_LOCK_MODE_REQUESTED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TABLESPACE_NAME      L_TABLESPACE_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TABLE_SCHEMA         L_TABLE_SCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.TABLE_NAME           L_TABLE_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.APPL_STATUS         AW_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.APPL_ID             AW_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.AUTH_ID             AW_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.CLIENT_NNAME        AW_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_USERID AW_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_WKSTN  AW_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_CLIENT_APP    AW_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AW.TPMON_ACC_STR       AW_TPMON_ACC_STR,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.APPL_STATUS         AH_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.APPL_ID             AH_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.AUTH_ID             AH_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.CLIENT_NNAME        AH_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_USERID AH_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_WKSTN  AH_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_CLIENT_APP    AH_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.TPMON_ACC_STR       AH_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotLockWait + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " AW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID = AW.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID = AW.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID = AH.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID_HOLDING_LK = AH.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(snapshotId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(L.SID = snapshotId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(L_SID                            )), '') || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Timestamp               : ' || COALESCE(RTRIM(CHAR(L_SNAPSHOT_TIMESTAMP             )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Wait Start Time    : ' || COALESCE(RTRIM(CHAR(L_LOCK_WAIT_START_TIME           )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Object Type        : ' || COALESCE(" + qualFuncNameLockObjType2Str + "(LOCK_OBJECT_TYPE), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Mode               : ' || COALESCE(" + qualFuncNameLockMode2Str + "(L_LOCK_MODE           ), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Lock Mode Requested     : ' || COALESCE(" + qualFuncNameLockMode2Str + "(L_LOCK_MODE_REQUESTED ), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Space             : ' || COALESCE(L_TABLESPACE_NAME                             , '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Schema            : ' || COALESCE(L_TABLE_SCHEMA                                , '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   'Table Name              : ' || COALESCE(L_TABLE_NAME                                  , '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_agentLoopCount = 1;");

M11_LRT.genProcSectionHeader(fileNo, "loop over agents: 1 = agent waiting for lock, 2 = agent holding lock", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REPEAT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_agentLoopCount = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   'Agent waiting for Lock  : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AW_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(AW_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID      : ' || COALESCE(AW_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_NNAME          : ' || COALESCE(AW_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_USERID         : ' || COALESCE(AW_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AW_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_APPLNAME       : ' || COALESCE(AW_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_ACCTNG         : ' || COALESCE(AW_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   'Agent holding Lock      : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID_HOLDING_LK)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AH_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(AH_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  Authorization ID      : ' || COALESCE(AH_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_NNAME          : ' || COALESCE(AH_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_USERID         : ' || COALESCE(AH_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AH_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_APPLNAME       : ' || COALESCE(AH_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '  CLIENT_ACCTNG         : ' || COALESCE(AH_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all statements related to this agent", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_firstLine = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR snStLoop AS snStCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.ROWS_READ,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.ROWS_WRITTEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_OPERATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_START,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_STOP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS ELAPSED_TIME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID = L_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.AGENT_ID = (CASE v_agentLoopCount WHEN 1 THEN L_AGENT_ID ELSE L_AGENT_ID_HOLDING_LK END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.STMT_START");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_firstLine = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Statement Start ', 27) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Statement Stop ' , 28) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Type '           ,  9) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Op '             ,  9) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Rows Read '      , 13) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Rows Written '   , 13) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   RIGHT(v_emptyLine || 'Time Elapsed '   , 18) || '|' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   ' STATEMENT' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 27) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 28) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine,  9) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine,  9) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 13) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 13) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 18) || '+' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   LEFT(v_delimLine, 81) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_firstLine = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_thisRecord = v_thisRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   '    ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CHAR(STMT_START),''),26) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CHAR(STMT_STOP),''),26) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(" + qualFuncNameStmntType2StrS + "(STMT_TYPE),''), 7) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(" + qualFuncNameStmntOperation2StrS + "(STMT_OPERATION),''), 7) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_READ)),''),11) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_WRITTEN)),''),11) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   CHAR(COALESCE(CAST(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                     CAST(SECOND(ELAPSED_TIME) + 60 * (MINUTE(ELAPSED_TIME) + 60 * (HOUR(ELAPSED_TIME) + 24 * DAY(ELAPSED_TIME))) + CAST(MICROSECOND(ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   AS CHAR(16)),''),16) || ' | ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   COALESCE(REPLACE(LEFT(STMT_TEXT,80), CHR(10), ' '), '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "                   v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_agentLoopCount = v_agentLoopCount + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNTIL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_agentLoopCount = 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END REPEAT;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(CLOB(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_thisRecord || v_nl)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ", " + String.valueOf(maxRecordLength));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M22_Class_Utilities.printSectionHeader("SP for analyzing LOCK-WAIT snapshot data", fileNo, null, null);

// ####################################################################################################################

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait + "(snapshotId_in, 0, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing Application snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing Appplication snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "if '0' retrieve records involving inconsistencies, if '1' retrieve all records");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_delimLine", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtIdStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousLrtIdStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgIdLrtStr", "VARCHAR(2)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgIdLockStr", "VARCHAR(2)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_qualTabNameLock", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgIdStmntStr", "VARCHAR(2)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_creatorStmnt", "VARCHAR(20)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOidStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousPsOidStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOidLrtStr", "VARCHAR(25)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousCdUserId", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserIdLrt", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousRecord", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecordInfo", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousRecordInfo", "VARCHAR(2048)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousApplStatus", "SMALLINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousAgentId", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstSid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_previousSid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstTimeStamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_outputPreviousRecord", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(300)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, 2048, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_delimLine = REPEAT('-', 100);");

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snAppLoop AS snAppCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SID                 A_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SNAPSHOT_TIMESTAMP  A_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_STATUS         A_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AGENT_ID            A_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_ID             A_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AUTH_ID             A_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.CLIENT_NNAME        A_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_USERID A_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_WKSTN  A_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_APP    A_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_ACC_STR       A_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(snapshotId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A.SID = snapshotId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AGENT_ID ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.SID      ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecordInfo = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_firstSid       = COALESCE(v_firstSid,       A_SID);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_firstTimeStamp = COALESCE(v_firstTimeStamp, A_SNAPSHOT_TIMESTAMP);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_lrtIdStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_WKSTN ),  25));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psOidStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_APP   ),  25));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cdUserId = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_USERID), 100));");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_orgIdLrtStr     = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_orgIdLockStr    = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_qualTabNameLock = NULL;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF LENGTH(v_lrtIdStr) > LENGTH('" + M01_LDM.gc_sequenceMinValue + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_orgIdLrtStr = LEFT(v_lrtIdStr, LENGTH(v_lrtIdStr) - LENGTH('" + M01_LDM.gc_sequenceMinValue + "'));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF LENGTH(v_orgIdLrtStr) = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_orgIdLrtStr = '0' || v_orgIdLrtStr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF " + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(v_orgIdLrtStr) = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt  = 'SELECT RTRIM(CHAR(L." + M01_Globals_IVK.g_anPsOid + ")), U." + M01_Globals.g_anUserId + " FROM " + M04_Utilities.genSchemaName(M01_ACM.snLrt, M01_ACM.ssnLrt, ddlType, null, null) + "' || RTRIM(CHAR(v_orgIdLrtStr)) || '" + String.valueOf(M01_Globals.g_workDataPoolId) + ".LRT L LEFT OUTER JOIN " + M01_Globals.g_qualTabNameUser + " U ON U." + M01_Globals.g_anOid + " = L.UTROWN_OID WHERE L." + M01_Globals.g_anOid + " = ' || v_lrtIdStr || ' WITH UR';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN c;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_psOidLrtStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_cdUserIdLrt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOSE c WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(V.tabSchema) || '.' || RTRIM(V.tabName)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgIdLockStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_qualTabNameLock");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.TABLE_SCHEMA AS tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.TABLE_NAME AS tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2) AS orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotLock + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.SID = A_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L.AGENT_ID = A_AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2)) = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(L.TABLE_SCHEMA, 3) = '" + M03_Config.productKey + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sortCrit ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.creator,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.stmt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgIdStmntStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_creatorStmnt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(LEFT(S.CREATOR, 20)) AS creator,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS stmt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(LEFT(RIGHT(S.CREATOR, 3), 2)) AS orgIdStr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN LEFT(RIGHT(S.CREATOR, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.SID = A_SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S.AGENT_ID = A_AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(LEFT(RIGHT(S.CREATOR, 3), 2)) = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT(S.CREATOR, 3) = '" + M03_Config.productKey + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sortCrit ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdLockStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    ORG-ID according to LRT-OID-Register <-> ''locked Table'' : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdLockStr || COALESCE(' (' || v_qualTabNameLock || ')', '') || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdStmntStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    ORG-ID according to LRT-OID-Register <-> ''static SQL''   : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdStmntStr || COALESCE(' (' || v_creatorStmnt || ' / ' || v_stmnt || ')', '') || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_psOidStr IS NOT NULL AND v_psOidLrtStr IS NOT NULL AND v_psOidStr <> v_psOidLrtStr THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    PS-OID according to PS-OID-Register  <-> LRT            : ' || v_psOidStr || ' <-> ' || v_psOidLrtStr || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_cdUserId IS NOT NULL AND v_cdUserIdLrt IS NOT NULL AND v_cdUserId <> v_cdUserIdLrt THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecordInfo = v_thisRecordInfo ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "           '    CD-UserId according to UID-Register  <-> LRT            : ''' || v_cdUserId || ''' <-> ''' || v_cdUserIdLrt || '''' || v_nl;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_outputPreviousRecord = (CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (v_previousRecordInfo IS NOT NULL AND v_previousRecordInfo <> v_thisRecordInfo)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (v_previousApplStatus IS NOT NULL AND v_previousApplStatus <> A_APPL_STATUS)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousAgentId, -1) <> A_AGENT_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousLrtIdStr, '') <> COALESCE(v_lrtIdStr, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousPsOidStr, '') <> COALESCE(v_psOidStr, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                   OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 (COALESCE(v_previousCdUserId, '') <> COALESCE(v_cdUserId, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                                 AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                               (mode_in = 1 OR v_previousRecordInfo <> '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "                             THEN 1 ELSE 0 END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecord = v_thisRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord =  LEFT(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(v_firstSid)), '') || (CASE WHEN v_firstSid <> A_SID THEN ' - ' || COALESCE(RTRIM(CHAR(A_SID)), '') ELSE '' END) || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Timestamp               : ' || COALESCE(RTRIM(CHAR(v_firstTimeStamp)), '') || (CASE WHEN v_firstTimeStamp <> A_SNAPSHOT_TIMESTAMP THEN ' - ' || COALESCE(RTRIM(CHAR(A_SNAPSHOT_TIMESTAMP)), '') ELSE '' END) || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Agent                   : ' || COALESCE(RTRIM(CHAR(A_AGENT_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Application Id          : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Application Status      : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(A_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'Authorization ID        : ' || COALESCE(A_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_NNAME            : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_USERID           : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_WRKSTNNAME       : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_APPLNAME         : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "'CLIENT_ACCTNG           : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + "(CASE WHEN v_thisRecordInfo = '' THEN '' ELSE '  Inconsistencies       : ' || v_nl || v_nl || v_thisRecordInfo || v_nl END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(14) + ", 1024");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + ");");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_outputPreviousRecord = 1 THEN");

M11_LRT.genProcSectionHeader(fileNo, "add statement related infos", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR snStmtLoop AS snStmtCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS S_STMT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.SID >= v_firstSid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.SID <= COALESCE(v_previousSid, v_firstSid))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.STMT_TEXT IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(S.AGENT_ID = A_AGENT_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FETCH FIRST 10 ROWS ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_previousRecord = v_previousRecord ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(17) + "'      > ' || S_STMT || v_nl;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_previousRecord || v_nl || v_delimLine || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_previousSid   = A_SID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_firstSid      = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecord     = v_thisRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousRecordInfo = v_thisRecordInfo;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousApplStatus = A_APPL_STATUS;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousAgentId    = A_AGENT_ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousLrtIdStr   = v_lrtIdStr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousPsOidStr   = v_psOidStr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_previousCdUserId   = v_cdUserId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_outputPreviousRecord = (CASE WHEN (v_firstSid IS NOT NULL) AND (mode_in = 1 OR v_previousRecordInfo <> '') THEN 1 ELSE 0 END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_outputPreviousRecord = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_previousRecord");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out  = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl, ddlType, null, "snapshotId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing STATEMENT snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

final int maxRecordLengthStmnt = 32000;

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotIdStart_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "snapshotIdEnd_in", "BIGINT", true, "(optional) identifies the last snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", true, "(optional) identifies the fime of the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "endTime_in", "TIMESTAMP", true, "(optional) identifies the fime of the last snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "determines the level of details provided (0=low, 1=medium, 2=high)");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "truncated", "01004", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_headLine1", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_sidDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_agentDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmtDelimLine", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecord", "CLOB(100M)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisRecordLck", "CLOB(100M)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmtNo", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_nl", "CHAR(1)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordLength", "INTEGER", String.valueOf(maxRecordLengthStmnt), null, null);
M11_LRT.genVarDecl(fileNo, "v_lastSid", "BIGINT", "-1", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastAgentId", "BIGINT", "-1", null, null);
M11_LRT.genVarDecl(fileNo, "v_numLocks", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR truncated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M94_SnapShot.genDdlForTempTablesSnapshotAnalysis(fileNo, ddlType, maxRecordLengthStmnt, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nl = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_sidDelimLine = REPEAT('#', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_agentDelimLine = REPEAT('=', 100);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmtDelimLine = REPEAT('-', 100);");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching snapshots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR snStmntLoop AS snAppCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID                  S_SID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SNAPSHOT_TIMESTAMP   S_SNAPSHOT_TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID             S_AGENT_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_TEXT            S_STMT_TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.ROWS_READ            S_ROWS_READ,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.ROWS_WRITTEN         S_ROWS_WRITTEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_TYPE            S_STMT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_OPERATION       S_STMT_OPERATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.QUERY_COST_ESTIMATE  S_QUERY_COST_ESTIMATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.QUERY_CARD_ESTIMATE  S_QUERY_CARD_ESTIMATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_SORTS           S_STMT_SORTS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.TOTAL_SORT_TIME      S_TOTAL_SORT_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SORT_OVERFLOWS       S_SORT_OVERFLOWS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_DELETED     S_INT_ROWS_DELETED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_UPDATED     S_INT_ROWS_UPDATED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INT_ROWS_INSERTED    S_INT_ROWS_INSERTED,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_START           S_STMT_START,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_STOP            S_STMT_STOP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS S_ELAPSED_TIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_STATUS          A_APPL_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.APPL_ID              A_APPL_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.AUTH_ID              A_AUTH_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.CLIENT_NNAME         A_CLIENT_NNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_USERID  A_TPMON_CLIENT_USERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_WKSTN   A_TPMON_CLIENT_WKSTN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_CLIENT_APP     A_TPMON_CLIENT_APP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.TPMON_ACC_STR        A_TPMON_ACC_STR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotStatement + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotApplInfo + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID = A.SID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID = A.AGENT_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(snapshotIdStart_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SID >= snapshotIdStart_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(snapshotIdEnd_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SID <= snapshotIdEnd_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(startTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SNAPSHOT_TIMESTAMP >= startTime_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(endTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SNAPSHOT_TIMESTAMP <= endTime_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(agentId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.AGENT_ID = agentId_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(mode_in > 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(STMT_TEXT IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SID        ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.AGENT_ID   ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.STMT_START ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "retrieve LOCK-infos for previous Statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (v_lastSid <> S_SID OR v_lastAgentId <> S_AGENT_ID) AND v_lastSid > 0 AND v_lastAgentId > 0 THEN");
genSaveLockInfoDdl(fileNo, 3, M01_Globals.g_qualTabNameSnapshotLock, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_nl;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lastSid <> S_SID THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_sidDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(S_SID                            )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Timestamp             : ' || COALESCE(RTRIM(CHAR(S_SNAPSHOT_TIMESTAMP             )), '') || v_nl || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastSid = S_SID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastAgentId = -1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmtNo = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lastAgentId <> S_AGENT_ID THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_agentDelimLine || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Agent Id                : ' || COALESCE(RTRIM(CHAR(S_AGENT_ID                       )), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Application Id        : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Application Status    : ' || COALESCE(" + qualFuncNameApplStatus2Str + "(A_APPL_STATUS), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Authorization ID      : ' || COALESCE(A_AUTH_ID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_NNAME          : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_USERID         : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_WRKSTNNAME     : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_APPLNAME       : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  CLIENT_ACCTNG         : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_lastAgentId = S_AGENT_ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmtNo = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisRecord = v_thisRecord || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " v_stmtDelimLine || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement               : ' || COALESCE(RTRIM(CHAR(v_stmtNo)), '') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "   v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Stop        : ' || COALESCE(CHAR(S_STMT_STOP ),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Time Elapsed          : ' || COALESCE(CAST(CAST(SECOND(S_ELAPSED_TIME) + 60 * (MINUTE(S_ELAPSED_TIME) + 60 * (HOUR(S_ELAPSED_TIME) + 24 * DAY(S_ELAPSED_TIME))) + CAST(MICROSECOND(S_ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6)) AS CHAR(16)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Type                  : ' || COALESCE(" + qualFuncNameStmntType2Str + "(S_STMT_TYPE),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Op                    : ' || COALESCE(" + qualFuncNameStmntOperation2Str + "(S_STMT_OPERATION),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Read             : ' || COALESCE(RTRIM(CHAR(S_ROWS_READ)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Written          : ' || COALESCE(RTRIM(CHAR(S_ROWS_WRITTEN)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Query Cost (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_COST_ESTIMATE)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Query Card (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_CARD_ESTIMATE)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Statement Sorts       : ' || COALESCE(RTRIM(CHAR(S_STMT_SORTS)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Total Sort Time       : ' || COALESCE(RTRIM(CHAR(S_TOTAL_SORT_TIME)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Sort Overflows        : ' || COALESCE(RTRIM(CHAR(S_SORT_OVERFLOWS)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Deleted (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_DELETED)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Updated (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_UPDATED)),'') || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " '  Rows Inserted (int.)  : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_INSERTED)),'') || v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement Text          : ' || v_nl || v_nl || COALESCE(RTRIM(LEFT(REPLACE(S_STMT_TEXT, CHR(10), ' '), 120)), '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + " 'Statement Text          : ' || v_nl || v_nl || COALESCE(S_STMT_TEXT, '');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmtNo = v_stmtNo + 1;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + pc_tempTabNameSnRecords + "(record) VALUES (LEFT(CLOB(v_thisRecord || v_nl),  v_recordLength));");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "retrieve LOCK-infos for last Statement", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lastSid > 0 AND v_lastAgentId > 0 THEN");
genSaveLockInfoDdl(fileNo, 2, M01_Globals.g_qualTabNameSnapshotLock, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return records to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameSnRecords);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN recordCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement + "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, 0, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "recordCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing STATEMENT snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the first snapshot to analyze");
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "INTEGER", true, "(otional) identifies the agent to analyze");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "determines the level of details provided (0=low, 1=medium, 2=high)");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement + "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, mode_in, recordCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement, ddlType, null, "snapshotId_in", "agentId_in", "mode_in", "recordCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for analyzing snapshot data
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for analyzing snapshot data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualProcNameGetSnapshotAnalysis);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "snapshotId_in", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot to analyze");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records retrieved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 15");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysis, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all snapshot types supporting analysis", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS procCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "REPLACE(PROCNAME, 'GETSNAPSHOT', 'GETSNAPSHOTANALYSIS') AS PROCNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SUPPORTANALYSIS = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + M04_Utilities.getSchemaName(M01_Globals.g_qualProcNameGetSnapshotAnalysis) + ".' || PROCNAME || '(?,0,?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_recordCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "snapshotId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET recordCount_out = recordCount_out + v_recordCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, M01_Globals.g_qualProcNameGetSnapshotAnalysis, ddlType, null, "snapshotId_in", "recordCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### ENDIF IVK ###
private static void genSaveLockInfoDdl(int fileNo, int indent, String M01_Globals.g_qualTabNameSnapshotLock, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String qualFuncNameLockObjType2StrS;
qualFuncNameLockObjType2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockObjType2StrS, ddlType, null, null, null, null, null, null);
String qualFuncNameLockStatus2StrS;
qualFuncNameLockStatus2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockStatus2Str + "_S", ddlType, null, null, null, null, null, null);
String qualFuncNameLockMode2StrS;
qualFuncNameLockMode2StrS = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnLockMode2Str + "_S", ddlType, null, null, null, null, null, null);

boolean useApiV9;
useApiV9 = false;
if (M03_Config.snapshotApiVersion.compareTo("9.7") == 0) {
useApiV9 = true;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_thisRecordLck = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_numLocks      = 0;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FOR lckLoop AS lckCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_LOCK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_ESCALATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TBSP_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_COUNT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_ESCALATION,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TABNAME" : "TABLE_NAME") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TABSCHEMA" : "TABLE_SCHEMA") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TBSP_NAME" : "TABLESPACE_NAME") + ",");
// ### ELSE IVK ###
// Print #fileNo, addTab(indent + 3); "L.TABNAME,"
// Print #fileNo, addTab(indent + 3); "L.TABSCHEMA,"
// Print #fileNo, addTab(indent + 3); "L.TBSP_NAME,"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_qualTabNameSnapshotLock + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.SID = v_lastSid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.AGENT_ID = v_lastAgentId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "(mode_in > 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "(L.LOCK_MODE NOT IN(1,2,4,6))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L.LOCK_ESCALATION,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TABNAME" : "TABLE_NAME") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TABSCHEMA" : "TABLE_SCHEMA") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + (useApiV9 ? "TBSP_NAME" : "TABLESPACE_NAME"));
// ### ELSE IVK ###
// Print #fileNo, addTab(indent + 3); "L.TABNAME,"
// Print #fileNo, addTab(indent + 3); "L.TABSCHEMA,"
// Print #fileNo, addTab(indent + 3); "L.TBSP_NAME"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_OBJECT_TYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_MODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_STATUS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_ESCALATION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_TBSP_NAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L_LOCK_COUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ROWNUMBER() OVER (ORDER BY L_TABSCHEMA ASC, L_TABNAME ASC) L_ROWNUM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_LOCK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ORDER BY L_ROWNUM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_thisRecordLck = v_thisRecordLck ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "'  ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(L_ROWNUM),''),4) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(" + qualFuncNameLockObjType2StrS + "(L_LOCK_OBJECT_TYPE)),''),13) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(" + qualFuncNameLockStatus2StrS + "(L_LOCK_STATUS)),''),3) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(" + qualFuncNameLockMode2StrS + "(L_LOCK_MODE)),''),3) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(L_LOCK_ESCALATION),''),3) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(CHAR(L_LOCK_COUNT),''),7) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(L_TBSP_NAME,''),15) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "CHAR(COALESCE(RTRIM(L_TABSCHEMA) || '.' || L_TABNAME,''),60) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 12) + "v_nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_numLocks = v_numLocks + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF v_numLocks > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INSERT INTO " + pc_tempTabNameSnRecords + "(record) VALUES(LEFT(CLOB(v_nl || v_stmtDelimLine || v_nl || v_nl ||" + " 'Locks                   : ' || v_nl || v_nl || v_thisRecordLck || v_nl),  v_recordLength));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}


private static void genGetSnapshotForXyzDdlV(int fileNo, Integer ddlType, String spName, String viewName, String viewShortName, String classSectionName, String className, String forWhom, String db2UdfName, String colFltrUdfName, String qualIdSequenceName, String M01_Globals.g_qualTabNameSnapshotType, String M01_Globals.g_qualTabNameSnapshotFilter, String M01_Globals.g_qualTabNameSnapshotHandle, Boolean isApplSpecificW, Boolean useUdfDbParamW) {
boolean isApplSpecific; 
if (isApplSpecificW == null) {
isApplSpecific = true;
} else {
isApplSpecific = isApplSpecificW;
}

boolean useUdfDbParam; 
if (useUdfDbParamW == null) {
useUdfDbParam = true;
} else {
useUdfDbParam = useUdfDbParamW;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;

boolean largeTables;
largeTables = false;
if (M03_Config.snapshotApiVersion.substring(0, 1) == "9") {
largeTables = true;
}

// ####################################################################################################################
// #    SP for retrieving snapshot of specified type
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, spName, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for retrieving snapshot on " + forWhom, fileNo, null, null);

int classIndexSnapshot;
classIndexSnapshot = M22_Class.getClassIndexByName(classSectionName, className, null);
String qualTabNameSnapshot;
qualTabNameSnapshot = M04_Utilities.genQualTabNameByClassIndex(classIndexSnapshot, ddlType, null, null, null, null, null, null, null, null, null);

String unqualTabNameSnapshot;
unqualTabNameSnapshot = M04_Utilities.getUnqualObjName(qualTabNameSnapshot);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M11_LRT.genProcParm(fileNo, "IN", "useLogging_in", M01_Globals.g_dbtBoolean, true, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported");
if (isApplSpecific) {
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
}
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "tableNotAccessible", "55019", null);
M11_LRT.genCondDecl(fileNo, "tableDoesNotExist", "42704", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_snapshotTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_myLevel", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_filter", "VARCHAR(" + (largeTables ? "8000" : "4000") + ")", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(" + (largeTables ? "8192" : "4096") + ")", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxtNoLog", "VARCHAR(512)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxtCrTab", "VARCHAR(" + (largeTables ? "12000" : "10000") + ")", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recreateTable", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_grantCount", "INTEGER", "0", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR tableNotAccessible");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_recreateTable = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR tableDoesNotExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_recreateTable = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", (isApplSpecific ? "agentId_in" : ""), "'category_in", "level_in", "rowCount_out", null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine snapshot timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_snapshotTs = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "determine this procedure's level", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_myLevel = COALESCE((SELECT LEVEL FROM " + M01_Globals.g_qualTabNameSnapshotType + " WHERE PROCNAME = '" + spName.toUpperCase() + "'), 0);");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = UR;");

M11_LRT.genProcSectionHeader(fileNo, "collect snapshot data", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "create snapshot ID if none is provided", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET snapshotId_inout = NEXTVAL FOR " + qualIdSequenceName + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine collect-filter to apply", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR filterLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.COLLECTFILTER AS FILTER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSnapshotFilter + " F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.TABLENAME = '" + unqualTabNameSnapshot + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.LEVEL <= level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.COLLECTFILTER IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.trimRight = false;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(classIndexSnapshot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 3, null, null, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "retrieve data", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INSERT INTO " + qualTabNameSnapshot + "(' ||");
int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + tabColumns.descriptors[k].columnName + (k == tabColumns.numDescriptors ? "" : ",") + "' ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnName.compareTo("SID") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CHAR(snapshotId_inout) ||" + (k == tabColumns.numDescriptors ? "" : " ',' ||"));
} else if (tabColumns.descriptors[k].columnName.compareTo("SNAPSHOT_TIMESTAMP") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'TIMESTAMP(''' || CHAR(v_snapshotTs) ||" + (k == tabColumns.numDescriptors ? "" : " '''),' ||"));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + tabColumns.descriptors[k].columnName + (k == tabColumns.numDescriptors ? " " : ",") + "' ||");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'TABLE(SYSPROC." + db2UdfName.toUpperCase() + "(" + (useUdfDbParam ? "CURRENT SERVER," : "") + "-1)) AS SN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN v_filter = '' THEN '(0=0) ' ELSE '(' || v_filter || ') ' END)");

if (isApplSpecific) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "|| (CASE WHEN agentId_in IS NULL THEN '' ELSE ' AND (AGENT_ID = ' || CHAR(agentId_in) || ')' END)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_recreateTable = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxtCrTab = 'DROP TABLE " + qualTabNameSnapshot + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxtCrTab;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxtCrTab =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CREATE TABLE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + qualTabNameSnapshot + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'(' ||");

int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M04_Utilities.genTransformedAttrDeclByDomain(tabColumns.descriptors[i].acmAttributeName, "???", M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformation, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndexSnapshot, (tabColumns.descriptors[i].isNullable ? "" : "NOT NULL"), false, ddlType, null, null, null, null, 0, null, null, null, null, null, null) + (i < tabColumns.numDescriptors ? "," : "") + "' ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "')' ||");

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceData.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceIndexData, null, null, null) + "' ||");
}
if (!(M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceLong.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' LONG IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceIndexLong, null, null, null) + "' ||");
}
if (M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceIndex != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' INDEX IN " + M04_Utilities.genTablespaceNameByIndex(M22_Class.g_classes.descriptors[classIndexSnapshot].tabSpaceIndexIndex, null, null, null) + "' ||");
}

if (M22_Class.g_classes.descriptors[classIndexSnapshot].useValueCompression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' VALUE COMPRESSION' ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' COMPRESS YES'");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxtCrTab;");

String qualProcedureNameSetGrants;
qualProcedureNameSetGrants = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnGrant + "Fltr", ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "set GRANTs on new table", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL " + qualProcedureNameSetGrants + "(2, '" + M04_Utilities.getSchemaName(qualTabNameSnapshot) + "', '" + M04_Utilities.getUnqualObjName(qualTabNameSnapshot) + "', v_grantCount);");

M11_LRT.genProcSectionHeader(fileNo, "try again to retrieve data", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "use 'last' snapshot ID if none is provided", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF snapshotId_inout IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "snapshotId_inout");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSnapshotHandle);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "register snapshot ID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (snapshotId_inout IS NOT NULL) AND (NOT EXISTS (SELECT 1 FROM " + M01_Globals.g_qualTabNameSnapshotHandle + " WHERE ID = snapshotId_inout)) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotHandle);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SNAPSHOT_TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "snapshotId_inout,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CURRENT TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (mode_in <= 1) and (level_in <= v_myLevel) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 3, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxtRowCount", "VARCHAR(2048)", "NULL", 3, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", 3, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 3, null);
M11_LRT.genVarDecl(fileNo, "v_stmntRowCount", "STATEMENT", null, 3, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resultCursor CURSOR WITH RETURN TO CLIENT FOR v_stmnt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE rowCountCursor CURSOR FOR v_stmntRowCount;");

M11_LRT.genProcSectionHeader(fileNo, "determine select-filter to apply", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_filter = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR filterLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "F.SELECTFILTER AS FILTER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameSnapshotFilter + " F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "F.TABLENAME = '" + unqualTabNameSnapshot + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "F.LEVEL <= level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "F.SELECTFILTER IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine SELECT statement", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF snapshotId_inout IS NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + colFltrUdfName + "('" + className.toUpperCase() + "', category_in, level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + qualTabNameSnapshot + "' || (CASE v_filter WHEN '' THEN '' ELSE ' WHERE ' || v_filter END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + colFltrUdfName + "('" + className.toUpperCase() + "', category_in, level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + qualTabNameSnapshot + "' || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(SID = ' || RTRIM(CHAR(snapshotId_inout)) || ')' || (CASE v_filter WHEN '' THEN '' ELSE ' AND (' || v_filter || ')' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in = 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "count the number of rows", 4, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxtRowCount =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'COUNT(*) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'" + qualTabNameSnapshot + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'(SID = ' || RTRIM(CHAR(snapshotId_inout)) || ')' || (CASE v_filter WHEN '' THEN '' ELSE ' AND (' || v_filter || ')' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmntRowCount FROM v_stmntTxtRowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN rowCountCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "rowCountCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "rowCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOSE rowCountCursor WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resultCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "reset ISOLATION LEVEL", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET CURRENT ISOLATION = RESET;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "useLogging_in", (isApplSpecific ? "agentId_in" : ""), "'category_in", "level_in", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving snapshot on " + forWhom, fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
if (isApplSpecific) {
M11_LRT.genProcParm(fileNo, "IN", "agentId_in", "BIGINT", true, "(optional) id of the agent to filter snapshot data for");
}
M11_LRT.genProcParm(fileNo, "IN", "category_in", "VARCHAR(10)", true, "(optional) category to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

if (isApplSpecific) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "rowCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "'category_in", "level_in", "rowCount_out", null, null, null, null, null, null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "call procedure", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(snapshotId_inout, mode_in, 1, " + (isApplSpecific ? "agentId_in," : "") + "category_in, level_in, rowCount_out);");

if (isApplSpecific) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "rowCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", "'category_in", "level_in", "rowCount_out", null, null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for retrieving snapshot on " + forWhom + " (short parameter list)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "snapshotId_inout", M01_Globals.g_dbtOid, true, "(optional) identifies the snapshot");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", false, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
if (isApplSpecific) {
M11_LRT.genVarDecl(fileNo, "v_agentId", "BIGINT", "NULL", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_category", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_level", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(snapshotId_inout, mode_in, " + (isApplSpecific ? "v_agentId," : "") + "v_category, v_level, v_rowCount);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "snapshotId_inout", "mode_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for creating snapshot-view of specified type
// ####################################################################################################################

String qualFuncNameSnCols;
qualFuncNameSnCols = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.udfnSnapshotCols, ddlType, null, null, null, null, null, null);

String qualViewName;
qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMonitor, viewName, viewShortName, ddlType, null, null, null, null, null, null, null, null, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, viewName, ddlType, null, null, "GenView", null, null, null);

M22_Class_Utilities.printSectionHeader("SP for creating snapshot-view on " + forWhom, fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", false, "(optional) level to use for column filtering");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "viewAlreadyExists", "42710", null);
M11_LRT.genCondDecl(fileNo, "viewDoesNotExist", "42704", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_filter", "VARCHAR(" + (largeTables ? "8000" : "4000") + ")", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(" + (largeTables ? "8192" : "4096") + ")", "NULL", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR viewAlreadyExists");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR viewDoesNotExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLWARNING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "level_in", null, null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine collect-filter to apply", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR filterLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "F.COLLECTFILTER AS FILTER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameSnapshotFilter + " F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "F.TABLENAME = '" + db2UdfName + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "F.LEVEL <= level_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntityWithColReuse(classIndexSnapshot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 3, null, null, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "drop view - if exists", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'DROP VIEW " + qualViewName + "';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "create view", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CREATE VIEW " + qualViewName + " ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'( ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualFuncNameSnCols + "('" + unqualTabNameSnapshot + "', CAST(NULL AS VARCHAR(1)), level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + qualTabNameSnapshot + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN v_filter = '' THEN '(0=0) ' ELSE '(' || v_filter || ') ' END) ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntTxt;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "level_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


public static void genDbAdminDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

if (thisOrgIndex < 0 |  thisPoolIndex < 0) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStepAdmin, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseDbSupport, null);

String qualProcedureNameCleanupGlobal;
qualProcedureNameCleanupGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnCleanData, ddlType, null, null, null, null, null, null);
String qualProcedureNameCleanupLocal;
qualProcedureNameCleanupLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnCleanData, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for executing data cleanup jobs (wrapper with unique name / no overloadding)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameCleanupLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only");
M11_LRT.genProcParm(fileNo, "IN", "jobCategory_in", "VARCHAR(20)", true, "category of the clean-job to execute");
M11_LRT.genProcParm(fileNo, "IN", "jobName_in", "VARCHAR(20)", true, "name of the clean-job to execute");
M11_LRT.genProcParm(fileNo, "IN", "level_in", "INTEGER", true, "(optional) level to use for column filtering");
M11_LRT.genProcParm(fileNo, "IN", "parameter1_in", "VARCHAR(30)", true, "(optional) parameter 1 to use in condition term for job");
M11_LRT.genProcParm(fileNo, "IN", "parameter2_in", "VARCHAR(30)", true, "(optional) parameter 2 to use in condition term for job");
M11_LRT.genProcParm(fileNo, "IN", "parameter3_in", "VARCHAR(30)", true, "(optional) parameter 3 to use in condition term for job");

M11_LRT.genProcParm(fileNo, "OUT", "stmntCount_out", "INTEGER", true, "number of statements for this job");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows deleted in any table");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameCleanupLocal, ddlType, null, "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out", null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameCleanupGlobal + "(mode_in, jobCategory_in, jobName_in, level_in, parameter1_in, parameter2_in, parameter3_in, stmntCount_out, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameCleanupLocal, ddlType, null, "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out", null, null, null);

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


private static void genDbEventMonitoringDdl(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 


// ####################################################################################################################
// #    SP for creating event monitor for locking
// ####################################################################################################################

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VL6CMON.EVENTMONITORCREATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "eventMonitorCount_out", "INTEGER", false, "number of event monitor created");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "doesNotExist", "42704", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_returnResult", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);
M11_LRT.genVarDecl(fileNo, "v_tableCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_viewCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_grantCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR doesNotExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "just ignore", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_returnResult = 0;  -- just fill the table");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "temporary table for statements", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo     INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "statement VARCHAR(30000)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON ROLLBACK PRESERVE ROWS;");

M11_LRT.genProcSectionHeader(fileNo, "SET output parameter", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET eventMonitorCount_out = 1;");

M11_LRT.genProcSectionHeader(fileNo, "DROP-Statement for event monitor \"EVMON_LOCKING \"", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'SET EVENT MONITOR evmon_locking STATE 0';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "100000 + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'DROP EVENT MONITOR EVMON_LOCKING ';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "200000 + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "CREATE-Statement for event monitor \"EVMON_LOCKING \"", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'CREATE EVENT MONITOR EVMON_LOCKING FOR LOCKING WRITE TO UNFORMATTED EVENT TABLE ( TABLE VL6CMON.EVMON_LOCKING IN MONITOR ) AUTOSTART ';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "300000 + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 0, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 AND v_returnResult = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for cleaning up event monitor data
// ####################################################################################################################

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VL6CMON.EVENTMONITORCLEAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "before_in", "TIMESTAMP", true, "(optionally) only event monitor data before this timestamp is cleaned up");
M11_LRT.genProcParm(fileNo, "IN", "commitCount_in", "INTEGER", true, "number of rows to delete before commit (0 = no commit, -1 disable logging + final commit)");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of event monitor tables affected");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for statements", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo     INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "statement VARCHAR(400)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON ROLLBACK PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET commitCount_in = COALESCE(commitCount_in, 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out   = 0;");

M11_LRT.genProcSectionHeader(fileNo, "cleanup event monitor table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'DELETE FROM VL6CMON.evmon_locking';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF before_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' WHERE ( EVENT_TIMESTAMP <= ''' || RTRIM( CHAR( before_in ) ) || ''')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tabCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF commitCount_in > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = REPLACE(v_stmntTxt, 'DELETE FROM', 'DELETE FROM (SELECT * FROM') || ' FETCH FIRST ' || RTRIM(CHAR(commitCount_in)) || ' ROWS ONLY)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_rowCount = commitCount_in;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHILE v_rowCount = commitCount_in DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "commit if logging is disabled (to minimize risk of unaccessible table)", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF commitCount_in < 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");


M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.StatementsEMC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);



NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}