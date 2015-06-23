package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_DbCfg {




private static final int colEntryFilter = 1;
private static final int colParameter = 2;
private static final int colValue = colParameter + 1;
private static final int colIsDbmParam = colValue + 1;
private static final int colIsDbProfileParam = colIsDbmParam + 1;
private static final int colSequenceNo = colIsDbProfileParam + 1;
private static final int colServerPlatform = colSequenceNo + 1;
private static final int colMinDbRelease = colServerPlatform + 1;

private static final int firstRow = 3;

private static final String sheetName = "DbCfg";

public static M78_DbCfg_Utilities.DbCfgParamDescriptors g_dbCfgParams;


private static void readSheet() {
M78_DbCfg_Utilities.initDbCfgParamDescriptors(M78_DbCfg.g_dbCfgParams);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colParameter).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M78_DbCfg_Utilities.allocDbCfgParamDescriptorIndex(M78_DbCfg.g_dbCfgParams);
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].parameter = M00_Excel.getCell(thisSheet, thisRow, colParameter).getStringCellValue().trim();
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].value = M00_Excel.getCell(thisSheet, thisRow, colValue).getStringCellValue().trim();
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].isDbmCfgParam = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsDbmParam).getStringCellValue(), null);
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].isDbProfileParam = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsDbProfileParam).getStringCellValue(), null);
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].sequenceNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNo).getStringCellValue(), -1);
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].serverPlatform = M00_Excel.getCell(thisSheet, thisRow, colServerPlatform).getStringCellValue().trim();
M78_DbCfg.g_dbCfgParams.descriptors[M78_DbCfg.g_dbCfgParams.numDescriptors].minDbRelease = M00_Excel.getCell(thisSheet, thisRow, colMinDbRelease).getStringCellValue().trim();

NextRow:
thisRow = thisRow + 1;
}
}


public static void getDbCfgParams() {
if ((M78_DbCfg.g_dbCfgParams.numDescriptors == 0)) {
readSheet();
}
}


public static void resetDbCfgParams() {
M78_DbCfg.g_dbCfgParams.numDescriptors = 0;
}


}