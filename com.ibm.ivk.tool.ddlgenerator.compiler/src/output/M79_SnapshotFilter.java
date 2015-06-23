package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotFilter {




private static final int colEntryFilter = 1;
private static final int colTabName = 2;
private static final int colLevel = colTabName + 1;
private static final int colCollectFilter = colLevel + 1;
private static final int colSelectFilter = colCollectFilter + 1;

private static final int firstRow = 3;
private static final String sheetName = "SnFl";
private static final int processingStep = 2;

public static M79_SnapshotFilter_Utilities.SnapshotFilterDescriptors g_snapshotFilter;


private static void readSheet() {
M79_SnapshotFilter_Utilities.initSnapshotFilterDescriptors(M79_SnapshotFilter.g_snapshotFilter);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colTabName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].tabName = M00_Excel.getCell(thisSheet, thisRow, colTabName).getStringCellValue().trim();
M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].level = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue(), null);
M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].collectFilter = M00_Excel.getCell(thisSheet, thisRow, colCollectFilter).getStringCellValue().trim();
M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].selectFilter = M00_Excel.getCell(thisSheet, thisRow, colSelectFilter).getStringCellValue().trim();

if (M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].selectFilter.compareTo("=") == 0) {
M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].selectFilter = M79_SnapshotFilter.g_snapshotFilter.descriptors[M79_SnapshotFilter_Utilities.allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter.g_snapshotFilter)].collectFilter;
}
NextRow:
thisRow = thisRow + 1;
}
}


public static void getSnapshotFilter() {
if ((M79_SnapshotFilter.g_snapshotFilter.numDescriptors == 0)) {
readSheet();
}
}


public static void resetSnapshotFilter() {
M79_SnapshotFilter.g_snapshotFilter.numDescriptors = 0;
}


public static void genSnapshotFilterCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotFilter, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M79_SnapshotFilter.g_snapshotFilter.numDescriptors; i++) {
if (!(M79_SnapshotFilter.g_snapshotFilter.descriptors[i].selectFilter.compareTo("") == 0) |  !(M79_SnapshotFilter.g_snapshotFilter.descriptors[i].collectFilter.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotFilter.g_snapshotFilter.descriptors[i].tabName + "\",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotFilter.g_snapshotFilter.descriptors[i].level >= 0 ? String.valueOf(M79_SnapshotFilter.g_snapshotFilter.descriptors[i].level) : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotFilter.g_snapshotFilter.descriptors[i].collectFilter.compareTo("") == 0 ? "" : "\"" + M79_SnapshotFilter.g_snapshotFilter.descriptors[i].collectFilter + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotFilter.g_snapshotFilter.descriptors[i].selectFilter.compareTo("") == 0 ? "" : "\"" + M79_SnapshotFilter.g_snapshotFilter.descriptors[i].selectFilter + "\""));
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


public static void dropSnapshotFilterCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotFilter, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}


}