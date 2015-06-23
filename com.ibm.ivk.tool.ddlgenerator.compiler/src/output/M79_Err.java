package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_Err {




private static final int colEntryFilter = 1;
private static final int colId = 2;
private static final int colIsActive = colId + 1;
private static final int colIsTechnical = colIsActive + 1;
private static final int colSqlState = colIsTechnical + 1;
private static final int colBusErrorMessageNo = colSqlState + 1;
private static final int colMessagePattern = colBusErrorMessageNo + 1;
private static final int colLength = colMessagePattern + 1;
private static final int colMessageExplanation = colLength + 1;
private static final int colBusErrorMessageText = colMessageExplanation + 1;
private static final int colComment = colBusErrorMessageText + 1;
private static final int colContext = colComment + 1;

private static final int processingStep = 2;
private static final int acmCsvProcessingStep = 1;
private static final int firstRow = 3;
private static final String sheetName = "Err";

public static M79_Err_Utilities.ErrDescriptors g_errs;


private static void readSheet() {
M79_Err_Utilities.initErrDescriptors(M79_Err.g_errs);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colId).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

if (M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsActive).getStringCellValue(), null)) {
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].id = M00_Excel.getCell(thisSheet, thisRow, colId).getStringCellValue().trim();
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].isTechnical = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsTechnical).getStringCellValue(), null);
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].sqlStateOffset = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSqlState).getStringCellValue(), null);
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].busErrorMessageNo = M00_Excel.getCell(thisSheet, thisRow, colBusErrorMessageNo).getStringCellValue().trim();
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].messagePattern = M00_Excel.getCell(thisSheet, thisRow, colMessagePattern).getStringCellValue().trim();

M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].messageExplanation = M00_Excel.getCell(thisSheet, thisRow, colMessageExplanation).getStringCellValue().trim();
M79_Err.g_errs.descriptors[M79_Err_Utilities.allocErrDescriptorIndex(M79_Err.g_errs)].conEnumLabelText = M00_Excel.getCell(thisSheet, thisRow, colContext).getStringCellValue().trim();
}

NextRow:
thisRow = thisRow + 1;
}
}


public static void getErrs() {
if ((M79_Err.g_errs.numDescriptors == 0)) {
readSheet();
}
}


public static void resetErrs() {
M79_Err.g_errs.numDescriptors = 0;
}


private static String transformErrMsg(int msgIndex, Variant arg1, Variant arg2, Variant arg3, Variant arg4, Variant arg5, Variant arg6, Variant arg7, Variant arg8, Variant arg9, String parm1W, String parm2W, String parm3W, String parm4W) {
String parm1; 
if (parm1W == null) {
parm1 = "";
} else {
parm1 = parm1W;
}

String parm2; 
if (parm2W == null) {
parm2 = "";
} else {
parm2 = parm2W;
}

String parm3; 
if (parm3W == null) {
parm3 = "";
} else {
parm3 = parm3W;
}

String parm4; 
if (parm4W == null) {
parm4 = "";
} else {
parm4 = parm4W;
}

String returnValue;
String result;
result = M00_Helper.replace(M79_Err.g_errs.descriptors[msgIndex].messagePattern, "%1", arg1 + "");
if (!(M79_Err.g_errs.descriptors[msgIndex].busErrorMessageNo.compareTo("") == 0)) {
result = M00_Helper.replace(result, "%b", M79_Err.g_errs.descriptors[msgIndex].busErrorMessageNo + "");
}
result = M00_Helper.replace(result, "%2", arg2 + "");
result = M00_Helper.replace(result, "%3", arg3 + "");
result = M00_Helper.replace(result, "%4", arg4 + "");
result = M00_Helper.replace(result, "%5", arg5 + "");
result = M00_Helper.replace(result, "%6", arg6 + "");
result = M00_Helper.replace(result, "%7", arg7 + "");
result = M00_Helper.replace(result, "%8", arg8 + "");
result = M00_Helper.replace(result, "%9", arg9 + "");

result = M00_Helper.replace(result, "'", "''");

result = M00_Helper.replace(result, "$1", "' || " + parm1 + " || '");
result = M00_Helper.replace(result, "$2", "' || " + parm2 + " || '");
result = M00_Helper.replace(result, "$3", "' || " + parm3 + " || '");
result = M00_Helper.replace(result, "$4", "' || " + parm4 + " || '");

returnValue = "'" + M03_Config.g_cfgSqlMsgPrefix + result + "'";
return returnValue;
}


public static Long getSqlStateByOffset(int offset) {
Long returnValue;
returnValue = M03_Config.g_cfgSqlStateStart + offset;
return returnValue;
}

public static void genSignalDdl(String id, int fileNo, Integer indentW, Variant arg1W, Variant arg2W, Variant arg3W, Variant arg4W, Variant arg5W, Variant arg6W, Variant arg7W, Variant arg8W, Variant arg9W) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Variant arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

Variant arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

Variant arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

Variant arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

Variant arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

Variant arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

Variant arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

Variant arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

Variant arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

int i;
for (int i = 1; i <= M79_Err.g_errs.numDescriptors; i++) {
if (M79_Err.g_errs.descriptors[i].id.toUpperCase() == id.toUpperCase()) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SIGNAL SQLSTATE '" + String.valueOf(M79_Err.getSqlStateByOffset(M79_Err.g_errs.descriptors[i].sqlStateOffset)) + "' SET MESSAGE_TEXT = " + transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, null, null, null, null) + ";");
return;
}
}

M04_Utilities.logMsg("unknown SIGNAL id '" + id + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}


public static void genSigMsgVarDecl(int fileNo, Integer indentW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

M11_LRT.genVarDecl(fileNo, "v_msg", "VARCHAR(" + M01_LDM.gc_dbMaxSignalMessageLength + ")", "NULL", indent, null);
}

public static void genSignalDdlWithParms(String id, int fileNo, Integer indentW, Variant arg1W, Variant arg2W, Variant arg3W, Variant arg4W, Variant arg5W, Variant arg6W, Variant arg7W, Variant arg8W, Variant arg9W, String parm1W, String parm2W, String parm3W, String parm4W) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Variant arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

Variant arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

Variant arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

Variant arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

Variant arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

Variant arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

Variant arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

Variant arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

Variant arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String parm1; 
if (parm1W == null) {
parm1 = "";
} else {
parm1 = parm1W;
}

String parm2; 
if (parm2W == null) {
parm2 = "";
} else {
parm2 = parm2W;
}

String parm3; 
if (parm3W == null) {
parm3 = "";
} else {
parm3 = parm3W;
}

String parm4; 
if (parm4W == null) {
parm4 = "";
} else {
parm4 = parm4W;
}

int i;
for (int i = 1; i <= M79_Err.g_errs.numDescriptors; i++) {
if (M79_Err.g_errs.descriptors[i].id.toUpperCase() == id.toUpperCase()) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SET v_msg = RTRIM(LEFT(" + transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, parm1, parm2, parm3, parm4) + "," + String.valueOf(M01_LDM.gc_dbMaxSignalMessageLength) + "));");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SIGNAL SQLSTATE '" + String.valueOf(M79_Err.getSqlStateByOffset(M79_Err.g_errs.descriptors[i].sqlStateOffset)) + "' " + "SET MESSAGE_TEXT = v_msg;");
return;
}
}

M04_Utilities.logMsg("unknown SIGNAL id '" + id + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}


public static void genSignalDdlWithParmsForCompoundSql(String id, int fileNo, Integer indentW, Variant arg1W, Variant arg2W, Variant arg3W, Variant arg4W, Variant arg5W, Variant arg6W, Variant arg7W, Variant arg8W, Variant arg9W, String parm1W, String parm2W, String parm3W, String parm4W) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Variant arg1; 
if (arg1W == null) {
arg1 = "";
} else {
arg1 = arg1W;
}

Variant arg2; 
if (arg2W == null) {
arg2 = "";
} else {
arg2 = arg2W;
}

Variant arg3; 
if (arg3W == null) {
arg3 = "";
} else {
arg3 = arg3W;
}

Variant arg4; 
if (arg4W == null) {
arg4 = "";
} else {
arg4 = arg4W;
}

Variant arg5; 
if (arg5W == null) {
arg5 = "";
} else {
arg5 = arg5W;
}

Variant arg6; 
if (arg6W == null) {
arg6 = "";
} else {
arg6 = arg6W;
}

Variant arg7; 
if (arg7W == null) {
arg7 = "";
} else {
arg7 = arg7W;
}

Variant arg8; 
if (arg8W == null) {
arg8 = "";
} else {
arg8 = arg8W;
}

Variant arg9; 
if (arg9W == null) {
arg9 = "";
} else {
arg9 = arg9W;
}

String parm1; 
if (parm1W == null) {
parm1 = "";
} else {
parm1 = parm1W;
}

String parm2; 
if (parm2W == null) {
parm2 = "";
} else {
parm2 = parm2W;
}

String parm3; 
if (parm3W == null) {
parm3 = "";
} else {
parm3 = parm3W;
}

String parm4; 
if (parm4W == null) {
parm4 = "";
} else {
parm4 = parm4W;
}

int i;
for (int i = 1; i <= M79_Err.g_errs.numDescriptors; i++) {
if (M79_Err.g_errs.descriptors[i].id.toUpperCase() == id.toUpperCase()) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SET v_msg = LEFT(" + transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, parm1, parm2, parm3, parm4) + "," + String.valueOf(M01_LDM.gc_dbMaxSignalMessageLength) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SIGNAL SQLSTATE '" + String.valueOf(M79_Err.getSqlStateByOffset(M79_Err.g_errs.descriptors[i].sqlStateOffset)) + "' " + "SET MESSAGE_TEXT = v_msg;");
return;
}
}

M04_Utilities.logMsg("unknown SIGNAL id '" + id + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}


public static void dropErrorCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnErrMsg, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


public static void genErrorCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnErrMsg, acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
String newline;
newline = "" + vbLf;
for (int i = 1; i <= M79_Err.g_errs.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, String.valueOf(M79_Err.g_errs.descriptors[i].sqlStateOffset + M03_Config.g_cfgSqlStateStart) + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Err.g_errs.descriptors[i].busErrorMessageNo.compareTo("") == 0) ? M79_Err.g_errs.descriptors[i].busErrorMessageNo : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M00_Helper.replace(M79_Err.g_errs.descriptors[i].messageExplanation, "\"", "\"\"") + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M00_Helper.replace(M00_Helper.replace(M79_Err.g_errs.descriptors[i].conEnumLabelText, newline, "\\n", null, null, vbBinaryCompare), "\"", "\"\"") + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}

M00_FileWriter.closeFile(fileNo);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}