package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_TabCfg {




private static final int colSequenceNo = 2;
private static final int colSchemaPattern = colSequenceNo + 1;
private static final int colNamePattern = colSchemaPattern + 1;
private static final int colSchemaPatternExcluded = colNamePattern + 1;
private static final int colNamePatternExcluded = colSchemaPatternExcluded + 1;
private static final int colPctFree = colNamePatternExcluded + 1;
private static final int colIsVolatile = colPctFree + 1;
private static final int colUseRowCompression = colIsVolatile + 1;
private static final int colUseIndexCompression = colUseRowCompression + 1;

private static final int firstRow = 3;

private static final String sheetName = "TabCfg";

private static final int processingStep = 2;

public static M78_TabCfg_Utilities.TabCfgParamDescriptors g_TabCfgParams;


private static void readSheet() {
M78_TabCfg_Utilities.initTabCfgParamDescriptors(M78_TabCfg.g_TabCfgParams);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue() + "" != "") {
M78_TabCfg_Utilities.allocTabCfgParamDescriptorIndex(M78_TabCfg.g_TabCfgParams);
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].sequenceNumber = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue(), null);
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].schemaPattern = M00_Excel.getCell(thisSheet, thisRow, colSchemaPattern).getStringCellValue().trim();
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].NamePattern = M00_Excel.getCell(thisSheet, thisRow, colNamePattern).getStringCellValue().trim();
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].schemaPatternExcluded = M00_Excel.getCell(thisSheet, thisRow, colSchemaPatternExcluded).getStringCellValue().trim();
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].NamePatternExcluded = M00_Excel.getCell(thisSheet, thisRow, colNamePatternExcluded).getStringCellValue().trim();
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].pctFree = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colPctFree).getStringCellValue(), null);
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].isVolatile = M04_Utilities.getTvBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsVolatile).getStringCellValue(), null);
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].useRowCompression = M04_Utilities.getTvBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseRowCompression).getStringCellValue(), null);
M78_TabCfg.g_TabCfgParams.descriptors[M78_TabCfg.g_TabCfgParams.numDescriptors].useIndexCompression = M04_Utilities.getTvBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseIndexCompression).getStringCellValue(), null);

thisRow = thisRow + 1;
}
}


public static void getTabCfgParams() {
if ((M78_TabCfg.g_TabCfgParams.numDescriptors == 0)) {
readSheet();
}
}


public static void resetTabCfgParams() {
M78_TabCfg.g_TabCfgParams.numDescriptors = 0;
}


public static void genTabCfgCsv(Integer ddlType) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnTableCfg, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);

int i;
for (int i = 1; i <= M78_TabCfg.g_TabCfgParams.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, String.valueOf(M78_TabCfg.g_TabCfgParams.descriptors[i].sequenceNumber) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_TabCfg.g_TabCfgParams.descriptors[i].schemaPattern.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_TabCfg.g_TabCfgParams.descriptors[i].NamePattern.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].schemaPatternExcluded.compareTo("") == 0 ? "" : "\"" + M78_TabCfg.g_TabCfgParams.descriptors[i].schemaPatternExcluded.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].NamePatternExcluded == "" ? "" : "\"" + M78_TabCfg.g_TabCfgParams.descriptors[i].NamePatternExcluded.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].pctFree < 0 ? "" : String.valueOf(M78_TabCfg.g_TabCfgParams.descriptors[i].pctFree)) + ",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].isVolatile == M01_Common.TvBoolean.tvTrue ? M01_LDM.gc_dbTrue : (M78_TabCfg.g_TabCfgParams.descriptors[i].isVolatile == M01_Common.TvBoolean.tvFalse ? M01_LDM.gc_dbFalse : "")) + ",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].useRowCompression == M01_Common.TvBoolean.tvTrue ? M01_LDM.gc_dbTrue : (M78_TabCfg.g_TabCfgParams.descriptors[i].useRowCompression == M01_Common.TvBoolean.tvFalse ? M01_LDM.gc_dbFalse : "")) + ",");
M00_FileWriter.printToFile(fileNo, (M78_TabCfg.g_TabCfgParams.descriptors[i].useIndexCompression == M01_Common.TvBoolean.tvTrue ? M01_LDM.gc_dbTrue : (M78_TabCfg.g_TabCfgParams.descriptors[i].useIndexCompression == M01_Common.TvBoolean.tvFalse ? M01_LDM.gc_dbFalse : "")) + ",");
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


public static void dropTabCfgsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnTableCfg, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}

}