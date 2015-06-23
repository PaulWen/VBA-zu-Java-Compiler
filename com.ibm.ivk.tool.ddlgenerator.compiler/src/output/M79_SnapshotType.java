package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotType {




private static final int colEntryFilter = 1;
private static final int colProcName = 2;
private static final int colTabName = colProcName + 1;
private static final int colViewName = colTabName + 1;
private static final int colSequenceNo = colViewName + 1;
private static final int colSequenceNoCollect = colSequenceNo + 1;
private static final int colCategory = colSequenceNoCollect + 1;
private static final int colLevel = colCategory + 1;
private static final int colIsApplSpecific = colLevel + 1;
private static final int colSupportAnalysis = colIsApplSpecific + 1;

private static final int firstRow = 3;
private static final String sheetName = "SnTp";
private static final int processingStep = 2;

public static M79_SnapshotType_Utilities.SnapshotTypeDescriptors g_snapshotTypes;


private static void readSheet() {
M79_SnapshotType_Utilities.initSnapshotTypeDescriptors(M79_SnapshotType.g_snapshotTypes);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colProcName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].procName = M00_Excel.getCell(thisSheet, thisRow, colProcName).getStringCellValue().trim();
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].className = M00_Excel.getCell(thisSheet, thisRow, colTabName).getStringCellValue().trim();
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].viewName = M00_Excel.getCell(thisSheet, thisRow, colViewName).getStringCellValue().trim();
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].sequenceNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue(), null);
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].sequenceNoCollect = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNoCollect).getStringCellValue(), null);
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].category = M00_Excel.getCell(thisSheet, thisRow, colCategory).getStringCellValue().trim();
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].level = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue(), null);
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].isApplSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsApplSpecific).getStringCellValue(), null);
M79_SnapshotType.g_snapshotTypes.descriptors[M79_SnapshotType_Utilities.allocSnapshotTypeDescriptorIndex(M79_SnapshotType.g_snapshotTypes)].supportAnalysis = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportAnalysis).getStringCellValue(), null);

NextRow:
thisRow = thisRow + 1;
}
}


public static void getSnapshotTypes() {
if ((M79_SnapshotType.g_snapshotTypes.numDescriptors == 0)) {
readSheet();
}
}


public static void resetSnapshotTypes() {
M79_SnapshotType.g_snapshotTypes.numDescriptors = 0;
}


public static void genSnapshotTypesCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotType, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M79_SnapshotType.g_snapshotTypes.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotType.g_snapshotTypes.descriptors[i].procName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotType.g_snapshotTypes.descriptors[i].className + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_SnapshotType.g_snapshotTypes.descriptors[i].viewName + "\",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotType.g_snapshotTypes.descriptors[i].sequenceNo >= 0 ? String.valueOf(M79_SnapshotType.g_snapshotTypes.descriptors[i].sequenceNo) : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotType.g_snapshotTypes.descriptors[i].category.compareTo("") == 0 ? "" : "\"" + M79_SnapshotType.g_snapshotTypes.descriptors[i].category + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotType.g_snapshotTypes.descriptors[i].level > 0 ? String.valueOf(M79_SnapshotType.g_snapshotTypes.descriptors[i].level) : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotType.g_snapshotTypes.descriptors[i].isApplSpecific ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M79_SnapshotType.g_snapshotTypes.descriptors[i].supportAnalysis ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropSnapshotTypesCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.clnSnapshotType, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}


public static void evalSnapshotTypes() {
int i;
for (int i = 1; i <= M79_SnapshotType.g_snapshotTypes.numDescriptors; i++) {
M79_SnapshotType.g_snapshotTypes.descriptors[i].classIndex = M22_Class.getClassIndexByName(M01_ACM.snDbMonitor, M79_SnapshotType.g_snapshotTypes.descriptors[i].className, null);
}
}


}