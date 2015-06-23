package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M23_Relationship_NL {




private static final int colI18nId = M23_Relationship.colRelI18nId;
private static final int colFirstLang = colI18nId + 1;

private static int[] langIds;

private static final int firstRow = 4;

private static final String sheetName = "Rel";

public static int numLangsForRelationshipsNl;
private static boolean isIntialized;

private static final int acmCsvProcessingStep = 2;

public static M23_Relationship_Utilities_NL.RelationshipNlDescriptors g_relationshipsNl;


private static void readSheet() {
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));

int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

if (!(M01_Common.isInitialized)) {
M23_Relationship_NL.numLangsForRelationshipsNl = 0;

while (M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + M23_Relationship_NL.numLangsForRelationshipsNl).getStringCellValue() + "" != "") {
M23_Relationship_NL.numLangsForRelationshipsNl = M23_Relationship_NL.numLangsForRelationshipsNl + 1;
}
if (M23_Relationship_NL.numLangsForRelationshipsNl > 0) {
langIds =  new int[M23_Relationship_NL.numLangsForRelationshipsNl];
}

int i;
for (int i = 1; i <= M23_Relationship_NL.numLangsForRelationshipsNl; i++) {
langIds[(i)] = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + i - 1).getStringCellValue(), null);

if (langIds[i] < 0) {
M04_Utilities.logMsg("invalid language ID '" + M00_Excel.getCell(thisSheet, thisRow - 1, i).getStringCellValue() + "' found in sheet '" + thisSheet + "' (column" + colFirstLang + i - 1 + "", M01_Common.LogLevel.ellError, null, null, null);
}
}
}

if (M23_Relationship_NL.numLangsForRelationshipsNl > 0) {
while (M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue() + "" != "") {
M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship_Utilities_NL.allocRelationshipNlDescriptorIndex(M23_Relationship_NL.g_relationshipsNl)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
for (int i = 1; i <= M23_Relationship_NL.numLangsForRelationshipsNl; i++) {
M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship_Utilities_NL.allocRelationshipNlDescriptorIndex(M23_Relationship_NL.g_relationshipsNl)].nl[(i)] = M00_Excel.getCell(thisSheet, thisRow, colFirstLang + i - 1).getStringCellValue().trim();
}
thisRow = thisRow + 1;
}
}
}


public static void getRelationshipsNl() {
if (M23_Relationship_NL.g_relationshipsNl.numDescriptors == 0) {
readSheet();
}
}


public static void resetRelationshipsNl() {
M23_Relationship_NL.g_relationshipsNl.numDescriptors = 0;
M01_Common.isInitialized = false;
}


public static void evalRelationshipsNl() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
M23_Relationship_NL.g_relationshipsNl.descriptors[i].relationshipIndex = M23_Relationship.getRelIndexByI18nId(M23_Relationship_NL.g_relationshipsNl.descriptors[i].i18nId);
if (M23_Relationship_NL.g_relationshipsNl.descriptors[i].relationshipIndex > 0) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_NL.g_relationshipsNl.descriptors[i].relationshipIndex].relNlIndex = i;
}
}
}


public static void dropRelationshipsNlCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


public static void genRelationshipNlAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmEntity, null, null, null), acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int thisRelNlIndex;
int thisLangId;
for (int thisRelNlIndex = 1; thisRelNlIndex <= M23_Relationship_NL.g_relationshipsNl.numDescriptors; thisRelNlIndex++) {
for (int thisLangId = 1; thisLangId <= M23_Relationship_NL.numLangsForRelationshipsNl; thisLangId++) {
if (M23_Relationship_NL.g_relationshipsNl.descriptors[thisRelNlIndex].nl[thisLangId] != "" &  M23_Relationship_NL.g_relationshipsNl.descriptors[thisRelNlIndex].relationshipIndex > 0) {
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_NL.g_relationshipsNl.descriptors[thisRelNlIndex].relationshipIndex].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_NL.g_relationshipsNl.descriptors[thisRelNlIndex].relationshipIndex].relName.toUpperCase() + "\"," + "\"R\"," + String.valueOf(thisLangId) + "," + "\"" + M23_Relationship_NL.g_relationshipsNl.descriptors[thisRelNlIndex].nl[thisLangId] + "\"," + M04_Utilities.getCsvTrailer(0));
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