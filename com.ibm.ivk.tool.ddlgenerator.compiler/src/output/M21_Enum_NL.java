package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M21_Enum_NL {




private static final int colEntryFilter = 1;

private static final int colI18nId = 4;
private static final int colFirstLang = colI18nId + 1;

private static int[] langIds;

private static final int firstRow = 4;

private static final String sheetName = "Enum-NL";

public static int numLangsForEnumsNl;
private static boolean isIntialized;

private static final int acmCsvProcessingStep = 0;

public static M21_Enum_Utilities_NL.EnumNlDescriptors g_enumsNl;


private static void readSheet() {
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));

int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

if (!(M01_Common.isInitialized)) {
M21_Enum_NL.numLangsForEnumsNl = 0;

while (M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + M21_Enum_NL.numLangsForEnumsNl).getStringCellValue() + "" != "") {
M21_Enum_NL.numLangsForEnumsNl = M21_Enum_NL.numLangsForEnumsNl + 1;
}
langIds =  new int[M21_Enum_NL.numLangsForEnumsNl];
int i;
for (int i = 1; i <= M21_Enum_NL.numLangsForEnumsNl; i++) {
langIds[(i)] = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + i - 1).getStringCellValue(), null);

if (langIds[i] < 0) {
M04_Utilities.logMsg("invalid language ID '" + M00_Excel.getCell(thisSheet, thisRow - 1, i).getStringCellValue() + "' found in sheet '" + thisSheet + "' (column" + colFirstLang + i - 1 + "", M01_Common.LogLevel.ellError, null, null, null);
}
}
}

if (M21_Enum_NL.numLangsForEnumsNl > 0) {
while (M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M21_Enum_NL.g_enumsNl.descriptors[M21_Enum_Utilities_NL.allocEnumNlDescriptorIndex(M21_Enum_NL.g_enumsNl)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
for (int i = 1; i <= M21_Enum_NL.numLangsForEnumsNl; i++) {
M21_Enum_NL.g_enumsNl.descriptors[M21_Enum_Utilities_NL.allocEnumNlDescriptorIndex(M21_Enum_NL.g_enumsNl)].nl[(i)] = M00_Excel.getCell(thisSheet, thisRow, colFirstLang + i - 1).getStringCellValue().trim();
}
NextRow:
thisRow = thisRow + 1;
}
}
}


public static void getEnumsNl() {
if (M21_Enum_NL.g_enumsNl.numDescriptors == 0) {
readSheet();
}
}


public static void resetEnumsNl() {
M21_Enum_NL.g_enumsNl.numDescriptors = 0;
M01_Common.isInitialized = false;
}


public static void evalEnumsNl() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
M21_Enum_NL.g_enumsNl.descriptors[i].enumIndex = M21_Enum.getEnumIndexByI18nId(M21_Enum_NL.g_enumsNl.descriptors[i].i18nId);
}
}


public static void dropEnumsNlCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


public static void genEnumNlAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
int j;
for (int i = 1; i <= M21_Enum_NL.g_enumsNl.numDescriptors; i++) {
for (int j = 1; j <= M21_Enum_NL.numLangsForEnumsNl; j++) {
if (M21_Enum_NL.g_enumsNl.descriptors[i].nl[j] != "" &  M21_Enum_NL.g_enumsNl.descriptors[i].enumIndex > 0) {
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[M21_Enum_NL.g_enumsNl.descriptors[i].enumIndex].sectionName.toUpperCase() + "\"," + "\"" + M21_Enum.g_enums.descriptors[M21_Enum_NL.g_enumsNl.descriptors[i].enumIndex].enumName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyEnum + "\"," + String.valueOf(j) + "," + "\"" + M21_Enum_NL.g_enumsNl.descriptors[i].nl[j] + "\"," + M04_Utilities.getCsvTrailer(0));
}
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}