package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_CleanJobs {




private static final int colEntryFilter = 1;
private static final int colJobCategory = 2;
private static final int colJobName = colJobCategory + 1;
private static final int colLevel = colJobName + 1;
private static final int colSequenceNo = colLevel + 1;
private static final int colTableSchema = colSequenceNo + 1;
private static final int colTableName = colTableSchema + 1;
private static final int colTableRef = colTableName + 1;
private static final int colCondition = colTableRef + 1;
private static final int colCommitCount = colCondition + 1;

private static final int firstRow = 3;

private static final String sheetName = "CleanJobs";

private static final int processingStep = 2;

public static M79_CleanJobs_Utilities.CleanJobDescriptors g_cleanjobs;


private static void readSheet() {
M79_CleanJobs_Utilities.initCleanJobDescriptors(M79_CleanJobs.g_cleanjobs);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colJobCategory).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].jobCategory = M00_Excel.getCell(thisSheet, thisRow, colJobCategory).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].jobName = M00_Excel.getCell(thisSheet, thisRow, colJobName).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].level = M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].sequenceNo = M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].tableSchema = M00_Excel.getCell(thisSheet, thisRow, colTableSchema).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].tableName = M00_Excel.getCell(thisSheet, thisRow, colTableName).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].tableRef = M00_Excel.getCell(thisSheet, thisRow, colTableRef).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].condition = M00_Excel.getCell(thisSheet, thisRow, colCondition).getStringCellValue().trim();
M79_CleanJobs.g_cleanjobs.descriptors[M79_CleanJobs_Utilities.allocCleanJobDescriptorIndex(M79_CleanJobs.g_cleanjobs)].commitCount = M04_Utilities.getLong(M00_Excel.getCell(thisSheet, thisRow, colCommitCount).getStringCellValue(), null);

NextRow:
thisRow = thisRow + 1;
}
}


public static void getCleanJobs() {
if ((M79_CleanJobs.g_cleanjobs.numDescriptors == 0)) {
readSheet();
}
}


public static void resetCleanJobs() {
M79_CleanJobs.g_cleanjobs.numDescriptors = 0;
}


public static void genCleanJobsCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnCleanJobs, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M79_CleanJobs.g_cleanjobs.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].jobCategory + "\",");
M00_FileWriter.printToFile(fileNo, (!(M79_CleanJobs.g_cleanjobs.descriptors[i].jobName.compareTo("") == 0) ? "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].jobName + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_CleanJobs.g_cleanjobs.descriptors[i].level != "" ? M79_CleanJobs.g_cleanjobs.descriptors[i].level : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_CleanJobs.g_cleanjobs.descriptors[i].sequenceNo != "" ? M79_CleanJobs.g_cleanjobs.descriptors[i].sequenceNo : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_CleanJobs.g_cleanjobs.descriptors[i].tableSchema.compareTo("") == 0) ? "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].tableSchema + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].tableName + "\",");
M00_FileWriter.printToFile(fileNo, (!(M79_CleanJobs.g_cleanjobs.descriptors[i].tableRef.compareTo("") == 0) ? "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].tableRef + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_CleanJobs.g_cleanjobs.descriptors[i].condition != "" ? "\"" + M79_CleanJobs.g_cleanjobs.descriptors[i].condition + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_CleanJobs.g_cleanjobs.descriptors[i].commitCount > 0 ? String.valueOf(M79_CleanJobs.g_cleanjobs.descriptors[i].commitCount) : "") + ",");
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropCleanJobsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnCleanJobs, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}



}