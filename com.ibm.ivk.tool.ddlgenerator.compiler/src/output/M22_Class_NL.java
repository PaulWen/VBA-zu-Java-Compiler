package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M22_Class_NL {




private static final int colEntryFilter = 1;
private static final int colI18nId = M22_Class.colClassI18nId;
private static final int colFirstLang = colI18nId + 1;

private static int[] langIds;

private static final int firstRow = 4;

private static final String sheetName = "Class";

public static int numLangsForClassesNl;
private static boolean isIntialized;

private static final int acmCsvProcessingStep = 1;

public static M22_Class_Utilities_NL.ClassNlDescriptors g_classesNl;


private static void readSheet() {
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));

int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

if (!(M01_Common.isInitialized)) {
M22_Class_NL.numLangsForClassesNl = 0;

while (M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + M22_Class_NL.numLangsForClassesNl).getStringCellValue() + "" != "") {
M22_Class_NL.numLangsForClassesNl = M22_Class_NL.numLangsForClassesNl + 1;
}
langIds =  new int[M22_Class_NL.numLangsForClassesNl];
int i;
for (int i = 1; i <= M22_Class_NL.numLangsForClassesNl; i++) {
langIds[(i)] = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + i - 1).getStringCellValue(), null);

if (langIds[i] < 0) {
M04_Utilities.logMsg("invalid language ID '" + M00_Excel.getCell(thisSheet, thisRow - 1, i).getStringCellValue() + "' found in sheet '" + thisSheet + "' (column" + colFirstLang + i - 1 + "", M01_Common.LogLevel.ellError, null, null, null);
}
}
}

if (M22_Class_NL.numLangsForClassesNl > 0) {
while (M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M22_Class_NL.g_classesNl.descriptors[M22_Class_Utilities_NL.allocClassNlDescriptorIndex(M22_Class_NL.g_classesNl)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
for (int i = 1; i <= M22_Class_NL.numLangsForClassesNl; i++) {
M22_Class_NL.g_classesNl.descriptors[M22_Class_Utilities_NL.allocClassNlDescriptorIndex(M22_Class_NL.g_classesNl)].nl[(i)] = M00_Excel.getCell(thisSheet, thisRow, colFirstLang + i - 1).getStringCellValue().trim();
}

NextRow:
thisRow = thisRow + 1;
}
}
}


public static void getClassesNl() {
if (M22_Class_NL.g_classesNl.numDescriptors == 0) {
readSheet();
}
}


public static void resetClassesNl() {
M22_Class_NL.g_classesNl.numDescriptors = 0;
M01_Common.isInitialized = false;
}


public static void evalClassesNl() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
M22_Class_NL.g_classesNl.descriptors[i].classIndex = M22_Class.getClassIndexByI18nId(M22_Class_NL.g_classesNl.descriptors[i].i18nId);
if (M22_Class_NL.g_classesNl.descriptors[i].classIndex > 0) {
M22_Class.g_classes.descriptors[M22_Class_NL.g_classesNl.descriptors[i].classIndex].classNlIndex = i;
}
}
}


public static void dropClassesNlCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


public static void genClassNlAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int thisClassNlIndex;
int thisLangId;
for (int thisClassNlIndex = 1; thisClassNlIndex <= M22_Class_NL.g_classesNl.numDescriptors; thisClassNlIndex++) {
for (int thisLangId = 1; thisLangId <= M22_Class_NL.numLangsForClassesNl; thisLangId++) {
if (M22_Class_NL.g_classesNl.descriptors[thisClassNlIndex].nl[thisLangId] != "" &  M22_Class_NL.g_classesNl.descriptors[thisClassNlIndex].classIndex > 0) {
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[M22_Class_NL.g_classesNl.descriptors[thisClassNlIndex].classIndex].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[M22_Class_NL.g_classesNl.descriptors[thisClassNlIndex].classIndex].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(thisLangId) + "," + "\"" + M22_Class_NL.g_classesNl.descriptors[thisClassNlIndex].nl[thisLangId] + "\"," + M04_Utilities.getCsvTrailer(0));
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