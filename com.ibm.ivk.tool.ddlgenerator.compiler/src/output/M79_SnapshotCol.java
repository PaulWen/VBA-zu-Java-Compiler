package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotCol {




private static final int colEntryFilter = 1;
private static final int colTabName = 2;
private static final int colColName = colTabName + 1;
private static final int colColAlias = colColName + 1;
private static final int colDisplayFunction = colColAlias + 1;
private static final int colColumnExpression = colDisplayFunction + 1;
private static final int colSequenceNo = colColumnExpression + 1;
private static final int colCategory = colSequenceNo + 1;
private static final int colLevel = colCategory + 1;

private static final int firstRow = 3;
private static final String sheetName = "SnCol";
private static final int processingStep = 2;

public static M79_SnapshotCol_Utilities.SnapshotColDescriptors g_snapshotCols;


private static void readSheet() {
M79_SnapshotCol_Utilities.initSnapshotColDescriptors(M79_SnapshotCol.g_snapshotCols);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colTabName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].tabName = M00_Excel.getCell(thisSheet, thisRow, colTabName).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].colName = M00_Excel.getCell(thisSheet, thisRow, colColName).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].colAlias = M00_Excel.getCell(thisSheet, thisRow, colColAlias).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].displayFunction = M00_Excel.getCell(thisSheet, thisRow, colDisplayFunction).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].columnExpression = M00_Excel.getCell(thisSheet, thisRow, colColumnExpression).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].sequenceNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue(), null);
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].category = M00_Excel.getCell(thisSheet, thisRow, colCategory).getStringCellValue().trim();
M79_SnapshotCol.g_snapshotCols.descriptors[M79_SnapshotCol_Utilities.allocSnapshotColDescriptorIndex(M79_SnapshotCol.g_snapshotCols)].level = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue(), null);

NextRow:
thisRow = thisRow + 1;
}
}


public static void getSnapshotCols() {
if ((M79_SnapshotCol.g_snapshotCols.numDescriptors == 0)) {
readSheet();
}
}


public static void resetSnapshotCols() {
M79_SnapshotCol.g_snapshotCols.numDescriptors = 0;
}


public static void genSnapshotColsCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotCol, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M79_SnapshotCol.g_snapshotCols.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].tabName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].colName + "\",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].colAlias.trim() == "" ? "" : "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].colAlias + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].displayFunction.trim() == "" ? "" : "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].displayFunction + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].columnExpression.trim() == "" ? "" : "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].columnExpression + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].sequenceNo >= 0 ? String.valueOf(M79_SnapshotCol.g_snapshotCols.descriptors[i].sequenceNo) : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].category.trim() == "" ? "" : "\"" + M79_SnapshotCol.g_snapshotCols.descriptors[i].category + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotCol.g_snapshotCols.descriptors[i].level >= 0 ? String.valueOf(M79_SnapshotCol.g_snapshotCols.descriptors[i].level) : ""));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropSnapshotColsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotCol, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}


}