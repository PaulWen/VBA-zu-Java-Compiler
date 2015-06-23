package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_DbProfile {




private static final int colEntryFilter = 1;
private static final int colProfileName = 2;
private static final int colObjectType = colProfileName + 1;
private static final int colSchemaName = colObjectType + 1;
private static final int colObjectName = colSchemaName + 1;
private static final int colSequenceNo = colObjectName + 1;
private static final int colConfigParameter = colSequenceNo + 1;
private static final int colConfigValue = colConfigParameter + 1;
private static final int colServerPlatform = colConfigValue + 1;
private static final int colMinDbRelease = colServerPlatform + 1;

private static final int firstRow = 3;

private static final String sheetName = "DbProf";

private static final int processingStep = 2;

public static M78_DbProfile_Utilities.DbCfgProfileDescriptors g_dbCfgProfiles;


private static void readSheet() {
M78_DbProfile_Utilities.initDbCfgProfileDescriptors(M78_DbProfile.g_dbCfgProfiles);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colObjectType).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M78_DbProfile_Utilities.allocDbCfgProfileDescriptorIndex(M78_DbProfile.g_dbCfgProfiles);
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].profileName = M00_Excel.getCell(thisSheet, thisRow, colProfileName).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].objectType = M00_Excel.getCell(thisSheet, thisRow, colObjectType).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].schemaName = M00_Excel.getCell(thisSheet, thisRow, colSchemaName).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].objectName = M00_Excel.getCell(thisSheet, thisRow, colObjectName).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].sequenceNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue(), null);
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].configParameter = M00_Excel.getCell(thisSheet, thisRow, colConfigParameter).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].configValue = M00_Excel.getCell(thisSheet, thisRow, colConfigValue).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].serverPlatform = M00_Excel.getCell(thisSheet, thisRow, colServerPlatform).getStringCellValue().trim();
M78_DbProfile.g_dbCfgProfiles.descriptors[M78_DbProfile.g_dbCfgProfiles.numDescriptors].minDbRelease = M00_Excel.getCell(thisSheet, thisRow, colMinDbRelease).getStringCellValue().trim();

NextRow:
thisRow = thisRow + 1;
}
}


public static void getDbCfgProfiles() {
if ((M78_DbProfile.g_dbCfgProfiles.numDescriptors == 0)) {
readSheet();
}
}


public static void resetDbCfgProfiles() {
M78_DbProfile.g_dbCfgProfiles.numDescriptors = 0;
}


public static void genDbCfgProfileCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnDbCfgProfile, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M78_DbProfile.g_dbCfgProfiles.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].profileName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].objectType.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, (!(M78_DbProfile.g_dbCfgProfiles.descriptors[i].schemaName.compareTo("") == 0) ? "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].schemaName.toUpperCase() + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].objectName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, (M78_DbProfile.g_dbCfgProfiles.descriptors[i].sequenceNo > 0 ? M78_DbProfile.g_dbCfgProfiles.descriptors[i].sequenceNo : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].configParameter.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].configValue + "\",");
M00_FileWriter.printToFile(fileNo, (!(M78_DbProfile.g_dbCfgProfiles.descriptors[i].serverPlatform.compareTo("") == 0) ? "\"" + M78_DbProfile.g_dbCfgProfiles.descriptors[i].serverPlatform.toUpperCase() + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M78_DbProfile.g_dbCfgProfiles.descriptors[i].minDbRelease.compareTo("") == 0) ? M00_Helper.replace(M78_DbProfile.g_dbCfgProfiles.descriptors[i].minDbRelease, ",", ".").toUpperCase() : "") + ",");
M00_FileWriter.printToFile(fileNo, "");
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropDbCfgProfilesCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnDbCfgProfile, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}




}