package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M07_SpLogging {




private static final int processingStepSpLog = 5;

private static final boolean implementSpLogByWrapper = true;

private static final int maxSpLogArgLength = 40;

private static final String logEventTypeEntry = "B";
private static final String logEventTypeEscape = "X";
private static final String logEventTypeExit = "E";
private static final String logEventTypeLog = "L";

private static final String logEventContextTypeProcedure = "P";
private static final String logEventContextTypeFunction = "F";
private static final String logEventContextTypeTrigger = "T";

private static final int switchPosLogByConfig = 2;
private static final int switchPosLogProcedure = 3;
private static final int switchPosLogFunction = 4;
private static final int switchPosLogTrigger = 5;


public static void genSpDdl() {
if (!(M03_Config.generatePdm | ! M03_Config.supportSpLogging)) {
return;
}

if (M03_Config.spLogMode != M01_Common.DbSpLogMode.esplFile) {
return;
}

Integer ddlType;
ddlType = M01_Common.DdlTypeId.edtPdm;

int spLogHandleLength;
spLogHandleLength = (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? 160 : 13);

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSpLog, processingStepSpLog, ddlType, null, null, "Deploy", M01_Common.phaseCoreSupport, null);

//On Error GoTo ErrorExit 

String qualProcName;
String unqualProcName;
String externalProcName;

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
// ####################################################################################################################
// #    UPDATE_SP_CONFIG
// ####################################################################################################################
unqualProcName = "UPDATE_SP_CONFIG";
externalProcName = "UpdateSPConfig";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "SP_NAME", "VARCHAR(128)", true, "name of the stored procedure");
M11_LRT.genProcParm(fileNo, "IN", "MODE", "CHAR(1)", false, "update mode ('Y' or 'N')");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    GET_SP_CONFIG
// ####################################################################################################################
unqualProcName = "GET_SP_CONFIG";
externalProcName = "GetSPConfig";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MODIFIES SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    GET_SP_LOG
// ####################################################################################################################
unqualProcName = "GET_SP_LOG";
externalProcName = "GetSPLog";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "LOG_FILE_NAME", "VARCHAR(30)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MODIFIES SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    OPEN_LOG
// ####################################################################################################################
unqualProcName = "OPEN_LOG";
externalProcName = "OpenLog";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "SP_NAME", "VARCHAR(128)", true, null);
M11_LRT.genProcParm(fileNo, "OUT", "HANDLE", "CHAR(160) FOR BIT DATA", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    CLOSE_LOG
// ####################################################################################################################
unqualProcName = "CLOSE_LOG";
externalProcName = "CloseLog";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    LOGGER
// ####################################################################################################################
unqualProcName = "LOGGER";
externalProcName = "Logger";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", true, null);
M11_LRT.genProcParm(fileNo, "IN", "MSG", "VARCHAR(4000)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    LOGINFO
// ####################################################################################################################
unqualProcName = "LOGINFO";
externalProcName = "Loginfo";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", true, null);
M11_LRT.genProcParm(fileNo, "IN", "MSG", "VARCHAR(4000)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SNOW
// ####################################################################################################################
unqualProcName = "SNOW";
externalProcName = "Snow";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "msg", "VARCHAR(100)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    TRUNCATE
// ####################################################################################################################
unqualProcName = "TRUNCATE";
externalProcName = "truncate_table";

M22_Class_Utilities.printSectionHeader("Stored Procedure " + unqualProcName, fileNo, null, null);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, unqualProcName, ddlType, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "schemaName", "VARCHAR(130)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "tableName", "VARCHAR(130)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MODIFIES SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALLED ON NULL INPUT");

if (M03_Config.generateSpLogMessages) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARAMETER STYLE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DBINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FENCED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT THREADSAFE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INHERIT SPECIAL REGISTERS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PROGRAM TYPE SUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "EXTERNAL NAME '<spPathPrefix>splogger!" + externalProcName + "'");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genSpTabLogRecordDdl(int fileNo, Integer ddlType, String spLogHdlVar, String spEntryTsVar, String eventType, String contextSchemaVar, String contextNameVar, String contextType, String messageVar) {
if (eventType.compareTo(logEventTypeEntry) == 0) {
M11_LRT.genProcSectionHeader(fileNo, "create new log handle", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET " + spLogHdlVar + " = GENERATE_UNIQUE();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET " + spEntryTsVar + " = CURRENT TIMESTAMP;");
}

M11_LRT.genProcSectionHeader(fileNo, "check whether log message is to be ignored", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (COALESCE(" + M01_LDM.gc_db2RegVarCtrl + ", '') = '') OR (LEFT(RIGHT('0000000' || " + M01_LDM.gc_db2RegVarCtrl + ", " + String.valueOf(switchPosLogByConfig) + "), 1) = '1') THEN");
M11_LRT.genProcSectionHeader(fileNo, "check by config table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSqlLogCfg + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(C." + M01_Globals_IVK.g_anEventType + ", '" + eventType + "') = '" + eventType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(C." + M01_Globals.g_anSpLogContextSchema + ", " + contextSchemaVar + ") = " + contextSchemaVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(C." + M01_Globals.g_anSpLogContextName + ", " + contextNameVar + ") = " + contextNameVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(C." + M01_Globals.g_anSpLogContextType + ", '" + contextType + "') = '" + contextType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN 0;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

if (contextType.compareTo(logEventContextTypeProcedure) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF LEFT(RIGHT('0000000' || " + M01_LDM.gc_db2RegVarCtrl + ", " + String.valueOf(switchPosLogProcedure) + "), 1) <> '1' THEN");
} else if (contextType.compareTo(logEventContextTypeFunction) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF LEFT(RIGHT('0000000' || " + M01_LDM.gc_db2RegVarCtrl + ", " + String.valueOf(switchPosLogFunction) + "), 1) <> '1' THEN");
} else if (contextType.compareTo(logEventContextTypeTrigger) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF LEFT('0000000' || RIGHT(" + M01_LDM.gc_db2RegVarCtrl + ", " + String.valueOf(switchPosLogTrigger) + "), 1) <> '1' THEN");
}
M11_LRT.genProcSectionHeader(fileNo, "check by special register", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "place record in Log-Table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameSqlLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLog, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 2, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 16, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conId, spLogHdlVar, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conSpLogEventTime, "CURRENT TIMESTAMP", null, null, null);
if (eventType.compareTo(logEventTypeEntry) == 0) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conSpLogEventTimeRelative, "0", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conSpLogEventTimeRelative, "TIMESTAMPDIFF(2, CHAR(CURRENT TIMESTAMP - " + spEntryTsVar + ")) + " + "(DECIMAL(TIMESTAMPDIFF(1, CHAR('00000000000000.' || RIGHT(CHAR(CURRENT TIMESTAMP - " + spEntryTsVar + "),6)))) / 1000000)", null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conEventType, "'" + eventType + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conSpLogContextSchema, contextSchemaVar, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conSpLogContextName, contextNameVar, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conSpLogContextType, "'" + contextType + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conMessage, "REPLACE(" + messageVar + ", CHR(10), ' ')", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conSchema, "CURRENT SCHEMA", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conPath, "CURRENT PATH", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conClientApplName, "CURRENT CLIENT_APPLNAME", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conClientWrkstnName, "CURRENT CLIENT_WRKSTNNAME", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conClientAcctng, "CURRENT CLIENT_ACCTNG", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conClientUserId, "CURRENT CLIENT_USERID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conIsolation, "CURRENT ISOLATION", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conUser, "CURRENT USER", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexSqlLog, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, null, null, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
}


public static void genSpLogWrapperDdl(Integer ddlType) {
M07_SpLogging.genSpDdl();

if (ddlType != M01_Common.DdlTypeId.edtPdm | ! M03_Config.supportSpLogging) {
return;
}

int spLogHandleLength;
spLogHandleLength = (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? 160 : 13);

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSpLog, processingStepSpLog, ddlType, null, null, null, M01_Common.phaseCoreSupport, null);

//On Error GoTo ErrorExit 

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

// ####################################################################################################################
// #    Wrapper-SP for placing a 'procedure entry log message'
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_ENTER", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Wrapper-SP for placing a 'procedure entry log message'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "INOUT", "spLogHdl_inout", "CHAR(" + spLogHandleLength + ") FOR BIT DATA", true, "log handle");
M11_LRT.genProcParm(fileNo, "INOUT", "spEntryTimestamp_inout", "TIMESTAMP", true, "procedure's entry timestamp");
M11_LRT.genProcParm(fileNo, "IN", "procSchema_in", M01_Globals.g_dbtDbSchemaName, true, "procedure schema name");
M11_LRT.genProcParm(fileNo, "IN", "procName_in", "VARCHAR(128)", true, "procedure name");
M11_LRT.genProcParm(fileNo, "IN", "argList_in", "VARCHAR(800)", false, "list of argument values");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M11_LRT.genCondDecl(fileNo, "implNotFound", "42724", 1);
M11_LRT.genCondDecl(fileNo, "procTerminated", "38503", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;");

M11_LRT.genProcSectionHeader(fileNo, "call SP-Logging Procedure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "OPEN_LOG", ddlType, null, null, null, null, null, null) + "(procName_in, spLogHdl_inout);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "LOGINFO", ddlType, null, null, null, null, null, null) + "(spLogHdl_inout, '--> entering Procedure ' || procName_in || '(' || argList_in || ')');");
} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M11_LRT.genCondDecl(fileNo, "tabDoesNotExist", "42704", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;");

genSpTabLogRecordDdl(fileNo, ddlType, "spLogHdl_inout", "spEntryTimestamp_inout", logEventTypeEntry, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Wrapper-SP for placing a 'procedure exit log message'
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_EXIT", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Wrapper-SP for placing a 'procedure exit log message'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "spLogHdl_in", "CHAR(" + spLogHandleLength + ") FOR BIT DATA", true, "log handle");
M11_LRT.genProcParm(fileNo, "IN", "spEntryTimestamp_in", "TIMESTAMP", true, "procedure's entry timestamp");
M11_LRT.genProcParm(fileNo, "IN", "procSchema_in", M01_Globals.g_dbtDbSchemaName, true, "procedure schema name");
M11_LRT.genProcParm(fileNo, "IN", "procName_in", "VARCHAR(128)", true, "procedure name");
M11_LRT.genProcParm(fileNo, "IN", "argList_in", "VARCHAR(800)", false, "list of argument values");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M11_LRT.genCondDecl(fileNo, "implNotFound", "42724", 1);
M11_LRT.genCondDecl(fileNo, "procTerminated", "38503", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;");

M11_LRT.genProcSectionHeader(fileNo, "call SP-Logging Procedure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "LOGINFO", ddlType, null, null, null, null, null, null) + "(spLogHdl_in, '<-- leaving Procedure ' || procName_in || '(' || argList_in || ')');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "CLOSE_LOG", ddlType, null, null, null, null, null, null) + "(spLogHdl_in);");
} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M11_LRT.genCondDecl(fileNo, "tabDoesNotExist", "42704", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;");

genSpTabLogRecordDdl(fileNo, ddlType, "spLogHdl_in", "spEntryTimestamp_in", logEventTypeExit, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Wrapper-SP for placing a 'procedure escape log message'
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_ESC", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Wrapper-SP for placing a 'procedure escape log message'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "spLogHdl_in", "CHAR(" + spLogHandleLength + ") FOR BIT DATA", true, "log handle");
M11_LRT.genProcParm(fileNo, "IN", "spEntryTimestamp_in", "TIMESTAMP", true, "procedure's entry timestamp");
M11_LRT.genProcParm(fileNo, "IN", "procSchema_in", M01_Globals.g_dbtDbSchemaName, true, "procedure schema name");
M11_LRT.genProcParm(fileNo, "IN", "procName_in", "VARCHAR(128)", true, "procedure name");
M11_LRT.genProcParm(fileNo, "IN", "argList_in", "VARCHAR(800)", false, "list of argument values");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M11_LRT.genCondDecl(fileNo, "implNotFound", "42724", 1);
M11_LRT.genCondDecl(fileNo, "procTerminated", "38503", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;");

M11_LRT.genProcSectionHeader(fileNo, "call SP-Logging Procedure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "LOGINFO", ddlType, null, null, null, null, null, null) + "(spLogHdl_in, '<-- escaping Procedure ' || procName_in || '(' || argList_in || ')');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "CLOSE_LOG", ddlType, null, null, null, null, null, null) + "(spLogHdl_in);");
} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M11_LRT.genCondDecl(fileNo, "tabDoesNotExist", "42704", 1);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;");

genSpTabLogRecordDdl(fileNo, ddlType, "spLogHdl_in", "spEntryTimestamp_in", logEventTypeEscape, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for activating / deactivating Stored Procedure Logging
// ####################################################################################################################

String qualProcedureNameActivate;
boolean deactivateMode;

int mode;
for (int mode = 1; mode <= 2; mode++) {
deactivateMode = (mode == 2);
if (deactivateMode) {
qualProcedureNameActivate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "DEACTIVATE", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for deactivating Stored Procedure Logging", fileNo, null, null);
} else {
qualProcedureNameActivate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "ACTIVATE", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activating Stored Procedure Logging", fileNo, null, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameActivate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "eventType_in", "CHAR(1)", true, "event types to activate");
M11_LRT.genProcParm(fileNo, "IN", "contextType_in", "CHAR(1)", true, "distinguishes context types 'P' (procedure), 'F' (function) and 'T' (trigger)");
M11_LRT.genProcParm(fileNo, "IN", "contextSchemaPattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema-filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", true, "(optional) filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of activation-statements executed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL", null, null);
if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 400, null, null, null, null, null, null, null, true, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameActivate, ddlType, null, "mode_in", "'eventType_in", "'contextType_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null);

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR procLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROCSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROCNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SYSCAT.PROCEDURES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "contextSchemaPattern_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RTRIM(PROCSCHEMA) LIKE contextSchemaPattern_in ESCAPE '\\'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(PROCSCHEMA) LIKE '" + M01_Globals.g_allSchemaNamePattern + "' ESCAPE '\\'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "contextNamePattern_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RTRIM(PROCNAME) LIKE contextNamePattern_in ESCAPE '\\'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "UPDATE_SP_CONFIG", ddlType, null, null, null, null, null, null) + "(''' || PROCSCHEMA || '.' || PROCNAME || ''',''' || " + (deactivateMode ? "N" : "Y") + " || ''')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M94_DBAdmin.tempTabNameStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SEQNO,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(contextType_in, '" + logEventContextTypeProcedure + "') = '" + logEventContextTypeProcedure + "' THEN");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M94_DBAdmin.tempTabNameStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 4, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conEventType, "eventType_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conSpLogContextSchema, "(CASE WHEN contextSchemaPattern_in IS NULL THEN contextSchemaPattern_in ELSE P.PROCSCHEMA END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conSpLogContextName, "(CASE WHEN contextNamePattern_in IS NULL THEN contextNamePattern_in ELSE P.PROCNAME END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conSpLogContextType, "(CASE WHEN (contextNamePattern_in IS NULL OR P.PROCNAME IS NULL) THEN contextType_in ELSE '" + logEventContextTypeProcedure + "' END)", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, null, null, 5, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SYSCAT.PROCEDURES P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P.PROCSCHEMA LIKE COALESCE(contextSchemaPattern_in, '" + M01_Globals.g_allSchemaNamePattern + "') ESCAPE '\\'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P.PROCNAME LIKE COALESCE(contextNamePattern_in, '%') ESCAPE '\\'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_Flagged");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag,");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 4, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE WHEN EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + M01_Globals.g_qualTabNameSqlLogCfg + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHERE");
if (deactivateMode) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "( C." + M01_Globals_IVK.g_anEventType + " = eventType_in OR eventType_in is NULL )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "( C." + M01_Globals.g_anSpLogContextType + " = contextType_in OR contextType_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "( C." + M01_Globals.g_anSpLogContextSchema + " like contextSchemaPattern_in||'%' OR contextSchemaPattern_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "( C." + M01_Globals.g_anSpLogContextName + " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ") THEN '+' ELSE ' ' END");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "COALESCE(C." + M01_Globals.g_anSpLogContextSchema + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextSchema + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "COALESCE(C." + M01_Globals.g_anSpLogContextName + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextName + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "COALESCE(C." + M01_Globals.g_anSpLogContextType + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextType + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "COALESCE(C." + M01_Globals_IVK.g_anEventType + ", '#') = COALESCE(V." + M01_Globals_IVK.g_anEventType + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ") THEN ' ' ELSE '+' END");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "),");
M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 5, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (ORDER BY " + M01_Globals.g_anSpLogContextSchema + ", " + M01_Globals.g_anSpLogContextName + "),");

if (deactivateMode) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'DELETE FROM " + M01_Globals.g_qualTabNameSqlLogCfg + " C ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " CASE WHEN " + M01_Globals_IVK.g_anEventType + " IS NULL     THEN '1=1' ELSE 'C." + M01_Globals_IVK.g_anEventType + "'||' = '||''''||" + M01_Globals_IVK.g_anEventType + "||''''                  END || ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " CASE WHEN " + M01_Globals.g_anSpLogContextType + " IS NULL   THEN '1=1' ELSE 'C." + M01_Globals.g_anSpLogContextType + "'||' = '||''''||" + M01_Globals.g_anSpLogContextType + "||''''              END || ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " CASE WHEN " + M01_Globals.g_anSpLogContextSchema + " IS NULL THEN '1=1' ELSE 'C." + M01_Globals.g_anSpLogContextSchema + "'||' LIKE '||''''||" + M01_Globals.g_anSpLogContextSchema + "||'%'||''''  END || ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " CASE WHEN " + M01_Globals.g_anSpLogContextName + " IS NULL   THEN '1=1' ELSE 'C." + M01_Globals.g_anSpLogContextName + "'||' LIKE '||''''||" + M01_Globals.g_anSpLogContextName + "||'%'||''''      END,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO " + M01_Globals.g_qualTabNameSqlLogCfg + " (' ||");
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 4, null, null, M01_Common.DdlOutputMode.edomNone, null);
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + tabColumns.descriptors[i].columnName + (i < tabColumns.numDescriptors ? "," : "") + "' ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "') VALUES (' ||");
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (tabColumns.descriptors[i].columnName.compareTo(M01_ACM_IVK.conEventType) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE('''' || " + M01_Globals_IVK.g_anEventType + " || '''', 'NULL')" + (i < tabColumns.numDescriptors ? " || ','" : "") + " ||");
} else if (tabColumns.descriptors[i].columnName.compareTo(M01_ACM.conSpLogContextSchema) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE('''' || " + M01_Globals.g_anSpLogContextSchema + " || '''', 'NULL')" + (i < tabColumns.numDescriptors ? " || ','" : "") + " ||");
} else if (tabColumns.descriptors[i].columnName.compareTo(M01_ACM.conSpLogContextName) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE('''' || " + M01_Globals.g_anSpLogContextName + " || '''', 'NULL')" + (i < tabColumns.numDescriptors ? " || ','" : "") + " ||");
} else if (tabColumns.descriptors[i].columnName.compareTo(M01_ACM.conSpLogContextType) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN " + M01_Globals.g_anSpLogContextName + " IS NULL THEN 'NULL' ELSE '''" + logEventContextTypeProcedure + "''' END)" + (i < tabColumns.numDescriptors ? " || ','" : "") + " ||");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')',");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_flagged");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = (SELECT COUNT(*) FROM " + M94_DBAdmin.tempTabNameStatement + " WHERE flag = '+');");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "update configuration", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(contextType_in, '" + logEventContextTypeProcedure + "') = '" + logEventContextTypeProcedure + "' THEN");

if (deactivateMode) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSqlLogCfg + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( C." + M01_Globals_IVK.g_anEventType + " = eventType_in OR eventType_in is NULL )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( C." + M01_Globals.g_anSpLogContextType + " = contextType_in OR contextType_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( C." + M01_Globals.g_anSpLogContextSchema + " like contextSchemaPattern_in||'%' OR contextSchemaPattern_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( C." + M01_Globals.g_anSpLogContextName + " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( C." + M01_Globals.g_anSpLogContextName + " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameSqlLogCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 4, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 4, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conEventType, "eventType_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conSpLogContextSchema, "(CASE WHEN contextSchemaPattern_in IS NULL THEN contextSchemaPattern_in ELSE P.PROCSCHEMA END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conSpLogContextName, "(CASE WHEN contextNamePattern_in IS NULL THEN contextNamePattern_in ELSE P.PROCNAME END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conSpLogContextType, "(CASE WHEN (contextNamePattern_in IS NULL OR P.PROCNAME IS NULL) THEN contextType_in ELSE '" + logEventContextTypeProcedure + "' END)", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, null, null, 5, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SYSCAT.PROCEDURES P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P.PROCSCHEMA LIKE COALESCE(contextSchemaPattern_in, '" + M01_Globals.g_allSchemaNamePattern + "') ESCAPE '\\'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P.PROCNAME LIKE COALESCE(contextNamePattern_in, '%') ESCAPE '\\'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexSqlLogCfg, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 4, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals.g_qualTabNameSqlLogCfg + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(C." + M01_Globals.g_anSpLogContextSchema + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextSchema + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(C." + M01_Globals.g_anSpLogContextName + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextName + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(C." + M01_Globals.g_anSpLogContextType + ", '#') = COALESCE(V." + M01_Globals.g_anSpLogContextType + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(C." + M01_Globals_IVK.g_anEventType + ", '#') = COALESCE(V." + M01_Globals_IVK.g_anEventType + ", '#')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS F,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M94_DBAdmin.tempTabNameStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqno ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameActivate, ddlType, null, "mode_in", "'eventType_in", "'contextType_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
// ####################################################################################################################
// #    SP for activating Stored Procedure Logging
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for activating Stored Procedure Logging", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameActivate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "contextSchemaPattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema-filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", true, "(optional) filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of activation-statements executed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameActivate, ddlType, null, "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, false);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameActivate + "(mode_in, contextSchemaPattern_in, contextNamePattern_in, 'Y', rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameActivate, ddlType, null, "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, false);
} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for deactivating Stored Procedure Logging
// ####################################################################################################################

String qualProcedureNameDeactivate;
qualProcedureNameDeactivate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "DEACTIVATE", ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for deactivating Stored Procedure Logging", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameDeactivate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "contextSchemaPattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) schema-filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", true, "(optional) filter for contexts to activate");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of activation-statements executed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
if (M03_Config.spLogAutonomousTransaction) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AUTONOMOUS");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameDeactivate, ddlType, null, "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameActivate + "(mode_in, contextSchemaPattern_in, contextNamePattern_in, 'N', rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameDeactivate, ddlType, null, "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", null, null, null, null, null, null, null, null);
} else if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genSpLogDecl(int fileNo, Integer indentW, Boolean genHeaderW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean genHeader; 
if (genHeaderW == null) {
genHeader = false;
} else {
genHeader = genHeaderW;
}

if (!(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages)) {
return;
}

int spLogHandleLength;
spLogHandleLength = (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? 160 : 13);

if (genHeader) {
boolean skipNl;
skipNl = false;
if (indent < 0) {
skipNl = true;
indent = - indent;
}

M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent, skipNl);
}

if (M03_Config.spLogMode == M01_Common.DbSpLogMode.esplTable) {
M11_LRT.genVarDecl(fileNo, "v_spEntryTimestamp", "TIMESTAMP", "NULL", indent, null);
}
M11_LRT.genVarDecl(fileNo, "v_spLogHdl", "CHAR(" + spLogHandleLength + ") FOR BIT DATA", "NULL", indent, null);
}


public static String pEnterArg(String arg, String prefixW, String postfixW) {
String prefix; 
if (prefixW == null) {
prefix = "',' || ";
} else {
prefix = prefixW;
}

String postfix; 
if (postfixW == null) {
postfix = " || ";
} else {
postfix = postfixW;
}

String returnValue;
returnValue = "";

boolean isStringArg;
boolean isDateTimeArg;
if (arg.substring(0, 1) == "'" &  arg.substring(0, 2) != "'.") {
isStringArg = true;
arg = arg.substring(arg.length() - 1 - arg.length() - 1);
} else if (arg.substring(0, 1) == "#") {
isDateTimeArg = true;
arg = arg.substring(arg.length() - 1 - arg.length() - 1);
}

if (arg.compareTo("") == 0) {
returnValue = "";
} else if (arg.compareTo("?") == 0) {
returnValue = prefix + "'?'" + postfix;
} else if (arg.substring(arg.length() - 1 - 4).toUpperCase() == "_OUT") {
returnValue = prefix + "'[?]'" + postfix;
} else if (arg.substring(arg.length() - 1 - 6).toUpperCase() == "_INOUT") {
if (isStringArg) {
returnValue = prefix + "'[' || COALESCE(''''||RTRIM(LEFT(" + arg + ", " + maxSpLogArgLength + "))||'''','-') || ']'" + postfix;
} else if (isDateTimeArg) {
returnValue = prefix + "'[' || COALESCE(''''||RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + ")))||'''','-') || ']'" + postfix;
} else {
returnValue = prefix + "'[' || COALESCE(RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + "))),'-') || ']'" + postfix;
}
} else {
if (isStringArg) {
returnValue = prefix + "COALESCE(''''||RTRIM(LEFT(" + arg + ", " + maxSpLogArgLength + "))||'''','-')" + postfix;
} else if (isDateTimeArg) {
returnValue = prefix + "COALESCE(''''||RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + ")))||'''','-')" + postfix;
} else {
returnValue = prefix + "COALESCE(RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + "))),'-')" + postfix;
}
}
return returnValue;
}


public static String pExitArg(String arg, String prefixW, String postfixW) {
String prefix; 
if (prefixW == null) {
prefix = "',' || ";
} else {
prefix = prefixW;
}

String postfix; 
if (postfixW == null) {
postfix = " || ";
} else {
postfix = postfixW;
}

String returnValue;
returnValue = "";

boolean isStringArg;
boolean isDateTimeArg;
if (arg.substring(0, 1) == "'" &  arg.substring(0, 2) != "'.") {
isStringArg = true;
arg = arg.substring(arg.length() - 1 - arg.length() - 1);
} else if (arg.substring(0, 1) == "#") {
isDateTimeArg = true;
arg = arg.substring(arg.length() - 1 - arg.length() - 1);
}

if (arg.compareTo("") == 0) {
returnValue = "";
} else if (arg.compareTo("?") == 0) {
returnValue = prefix + "'?'" + postfix;
} else if (arg.substring(arg.length() - 1 - 4).toUpperCase() == "_OUT") {
if (isStringArg) {
returnValue = prefix + "'[' || COALESCE(''''||RTRIM(LEFT(" + arg + ", " + maxSpLogArgLength + "))||'''','-') || ']'" + postfix;
} else if (isDateTimeArg) {
returnValue = prefix + "'[' || COALESCE(''''||RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + ")))||'''','-') || ']'" + postfix;
} else {
returnValue = prefix + "'[' || COALESCE(RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + "))),'-') || ']'" + postfix;
}
} else {
if (isStringArg) {
returnValue = prefix + "COALESCE(''''||RTRIM(LEFT(" + arg + ", " + maxSpLogArgLength + "))||'''','-')" + postfix;
} else if (isDateTimeArg) {
returnValue = prefix + "COALESCE(''''||RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + ")))||'''','-')" + postfix;
} else {
returnValue = prefix + "COALESCE(RTRIM(CAST(" + arg + " AS CHAR(" + maxSpLogArgLength + "))),'-')" + postfix;
}
}
return returnValue;
}


public static void genSpLogProcEnter(int fileNo, String qualProcName, Integer ddlTypeW, Integer indentW, String arg1W, String arg2W, String arg3W, String arg4W, String arg5W, String arg6W, String arg7W, String arg8W, String arg9W, String arg10W, String arg11W, String arg12W) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

String arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

String arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

String arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

String arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

String arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

String arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

String arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

String arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String arg10; 
if (arg10W == null) {
arg10 = "";
} else {
arg10 = arg10W;
}

String arg11; 
if (arg11W == null) {
arg11 = "";
} else {
arg11 = arg11W;
}

String arg12; 
if (arg12W == null) {
arg12 = "";
} else {
arg12 = arg12W;
}

if (!(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages)) {
return;
}

String procSchemaName;
String procName;
procSchemaName = M04_Utilities.getSchemaName(qualProcName);
procName = M04_Utilities.getUnqualObjName(qualProcName);

if (implementSpLogByWrapper) {
boolean skipNl;
skipNl = false;
if (indent < 0) {
skipNl = true;
indent = - indent;
}

M11_LRT.genProcSectionHeader(fileNo, "log procedure entry", indent, skipNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_ENTER", ddlType, null, null, null, null, null, null) + "(v_spLogHdl, v_spEntryTimestamp, " + "'" + procSchemaName + "', " + "'" + procName + "', " + M07_SpLogging.pEnterArg(arg1, "", null) + M07_SpLogging.pEnterArg(arg2, null, null) + M07_SpLogging.pEnterArg(arg3, null, null) + M07_SpLogging.pEnterArg(arg4, null, null) + M07_SpLogging.pEnterArg(arg5, null, null) + M07_SpLogging.pEnterArg(arg6, null, null) + M07_SpLogging.pEnterArg(arg7, null, null) + M07_SpLogging.pEnterArg(arg8, null, null) + M07_SpLogging.pEnterArg(arg9, null, null) + M07_SpLogging.pEnterArg(arg10, null, null) + M07_SpLogging.pEnterArg(arg11, null, null) + M07_SpLogging.pEnterArg(arg12, null, null) + "''" + ");");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "BEGIN");
M11_LRT.genCondDecl(fileNo, "implNotFound", "42724", indent + 1);
M11_LRT.genCondDecl(fileNo, "procTerminated", "38503", indent + 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, "OPEN_LOG", ddlType, null, null, null, null, null, null) + "('" + qualProcName + "',v_spLogHdl);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, "LOGINFO", ddlType, null, null, null, null, null, null) + "(v_spLogHdl, '--> entering Procedure " + qualProcName + "(' || " + M07_SpLogging.pEnterArg(arg1, "", null) + M07_SpLogging.pEnterArg(arg2, null, null) + M07_SpLogging.pEnterArg(arg3, null, null) + M07_SpLogging.pEnterArg(arg4, null, null) + M07_SpLogging.pEnterArg(arg5, null, null) + M07_SpLogging.pEnterArg(arg6, null, null) + M07_SpLogging.pEnterArg(arg7, null, null) + M07_SpLogging.pEnterArg(arg8, null, null) + M07_SpLogging.pEnterArg(arg9, null, null) + M07_SpLogging.pEnterArg(arg10, null, null) + M07_SpLogging.pEnterArg(arg11, null, null) + M07_SpLogging.pEnterArg(arg12, null, null) + "')');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END;");
}
}


public static void genSpLogProcExit(int fileNo, String qualProcName, Integer ddlTypeW, Integer indentW, String arg1W, String arg2W, String arg3W, String arg4W, String arg5W, String arg6W, String arg7W, String arg8W, String arg9W, String arg10W, String arg11W, String arg12W) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

String arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

String arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

String arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

String arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

String arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

String arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

String arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

String arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String arg10; 
if (arg10W == null) {
arg10 = "";
} else {
arg10 = arg10W;
}

String arg11; 
if (arg11W == null) {
arg11 = "";
} else {
arg11 = arg11W;
}

String arg12; 
if (arg12W == null) {
arg12 = "";
} else {
arg12 = arg12W;
}

if (!(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages)) {
return;
}

String procSchemaName;
String procName;
procSchemaName = M04_Utilities.getSchemaName(qualProcName);
procName = M04_Utilities.getUnqualObjName(qualProcName);

if (implementSpLogByWrapper) {
boolean skipNl;
skipNl = false;
if (indent < 0) {
skipNl = true;
indent = - indent;
}

M11_LRT.genProcSectionHeader(fileNo, "log procedure exit", indent, skipNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_EXIT", ddlType, null, null, null, null, null, null) + "(v_spLogHdl, v_spEntryTimestamp, " + "'" + procSchemaName + "', " + "'" + procName + "', " + M07_SpLogging.pExitArg(arg1, "", null) + M07_SpLogging.pExitArg(arg2, null, null) + M07_SpLogging.pExitArg(arg3, null, null) + M07_SpLogging.pExitArg(arg4, null, null) + M07_SpLogging.pExitArg(arg5, null, null) + M07_SpLogging.pExitArg(arg6, null, null) + M07_SpLogging.pExitArg(arg7, null, null) + M07_SpLogging.pExitArg(arg8, null, null) + M07_SpLogging.pExitArg(arg9, null, null) + M07_SpLogging.pExitArg(arg10, null, null) + M07_SpLogging.pExitArg(arg11, null, null) + M07_SpLogging.pExitArg(arg12, null, null) + "''" + ");");
} else {
genSpLogProcExitByMode(fileNo, qualProcName, null, ddlType, indent, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
}
}


public static void genSpLogProcEscape(int fileNo, String qualProcName, Integer ddlTypeW, Integer indentW, String arg1W, String arg2W, String arg3W, String arg4W, String arg5W, String arg6W, String arg7W, String arg8W, String arg9W, String arg10W, String arg11W, String arg12W) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

String arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

String arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

String arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

String arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

String arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

String arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

String arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

String arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String arg10; 
if (arg10W == null) {
arg10 = "";
} else {
arg10 = arg10W;
}

String arg11; 
if (arg11W == null) {
arg11 = "";
} else {
arg11 = arg11W;
}

String arg12; 
if (arg12W == null) {
arg12 = "";
} else {
arg12 = arg12W;
}

if (!(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages)) {
return;
}

String procSchemaName;
String procName;
procSchemaName = M04_Utilities.getSchemaName(qualProcName);
procName = M04_Utilities.getUnqualObjName(qualProcName);

if (implementSpLogByWrapper) {
boolean skipNl;
skipNl = false;
if (indent < 0) {
skipNl = true;
indent = - indent;
}

M11_LRT.genProcSectionHeader(fileNo, "log procedure escape", indent, skipNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexSpLog, "SPLOG_ESC", ddlType, null, null, null, null, null, null) + "(v_spLogHdl, v_spEntryTimestamp, " + "'" + procSchemaName + "', " + "'" + procName + "', " + M07_SpLogging.pExitArg(arg1, "", null) + M07_SpLogging.pExitArg(arg2, null, null) + M07_SpLogging.pExitArg(arg3, null, null) + M07_SpLogging.pExitArg(arg4, null, null) + M07_SpLogging.pExitArg(arg5, null, null) + M07_SpLogging.pExitArg(arg6, null, null) + M07_SpLogging.pExitArg(arg7, null, null) + M07_SpLogging.pExitArg(arg8, null, null) + M07_SpLogging.pExitArg(arg9, null, null) + M07_SpLogging.pExitArg(arg10, null, null) + M07_SpLogging.pExitArg(arg11, null, null) + M07_SpLogging.pExitArg(arg12, null, null) + "''" + ");");
} else {
genSpLogProcExitByMode(fileNo, qualProcName, "escaping", ddlType, indent, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
}
}


private static void genSpLogProcExitByMode(int fileNo, String procName, String modeW, Integer ddlTypeW, Integer indentW, String arg1W, String arg2W, String arg3W, String arg4W, String arg5W, String arg6W, String arg7W, String arg8W, String arg9W, String arg10W, String arg11W, String arg12W) {
String mode; 
if (modeW == null) {
mode = "leaving";
} else {
mode = modeW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

String arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

String arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

String arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

String arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

String arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

String arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

String arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

String arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String arg10; 
if (arg10W == null) {
arg10 = "";
} else {
arg10 = arg10W;
}

String arg11; 
if (arg11W == null) {
arg11 = "";
} else {
arg11 = arg11W;
}

String arg12; 
if (arg12W == null) {
arg12 = "";
} else {
arg12 = arg12W;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "BEGIN");
M11_LRT.genCondDecl(fileNo, "implNotFound", "42724", indent + 1);
M11_LRT.genCondDecl(fileNo, "procTerminated", "38503", indent + 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, "LOGINFO", ddlType, null, null, null, null, null, null) + "(v_spLogHdl, '<-- " + mode + " leaving Procedure " + procName + "(' || " + M07_SpLogging.pExitArg(arg1, "", null) + M07_SpLogging.pExitArg(arg2, null, null) + M07_SpLogging.pExitArg(arg3, null, null) + M07_SpLogging.pExitArg(arg4, null, null) + M07_SpLogging.pExitArg(arg5, null, null) + M07_SpLogging.pExitArg(arg6, null, null) + M07_SpLogging.pExitArg(arg7, null, null) + M07_SpLogging.pExitArg(arg8, null, null) + M07_SpLogging.pExitArg(arg9, null, null) + M07_SpLogging.pExitArg(arg10, null, null) + M07_SpLogging.pExitArg(arg11, null, null) + M07_SpLogging.pExitArg(arg12, null, null) + "')');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, "CLOSE_LOG", ddlType, null, null, null, null, null, null) + "(v_spLogHdl);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END;");
}

}