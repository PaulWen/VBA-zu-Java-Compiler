package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M08_VBA_Profiling {


// ### IF IVK ###


private static final int colEntryFilter = 1;
private static final int colModuleName = 2;
private static final int colProcName = 3;
private static final int colLevel = 4;

private static final int processingStep = 2;
private static final int firstRow = 3;
private static final String sheetName = "Prof";

public static M08_VBA_Profiling_Utilities.ProfLevelDescriptors g_profLevels;

private static int profFileNo;
private static boolean profPaused;
private static long profCallCount;
private static int profCallLevel;

public static class SystemTime {
public int wYear;
public int wMonth;
public int wDayOfWeek;
public int wDay;
public int wHour;
public int wMinute;
public int wSecond;
public int wMilliseconds;

public SystemTime(int wYear, int wMonth, int wDayOfWeek, int wDay, int wHour, int wMinute, int wSecond, int wMilliseconds) {
this.wYear = wYear;
this.wMonth = wMonth;
this.wDayOfWeek = wDayOfWeek;
this.wDay = wDay;
this.wHour = wHour;
this.wMinute = wMinute;
this.wSecond = wSecond;
this.wMilliseconds = wMilliseconds;
}
}

private static void readSheet() {
M79_Err_Utilities.initErrDescriptors(M79_Err.g_errs);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colModuleName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue()) |  M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue(), 1) <= 0) {
goto NextRow;
}

M08_VBA_Profiling.g_profLevels.descriptors[M08_VBA_Profiling_Utilities.allocProfLevelDescriptorIndex(M08_VBA_Profiling.g_profLevels)].level = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colLevel).getStringCellValue(), 1);
M08_VBA_Profiling.g_profLevels.descriptors[M08_VBA_Profiling_Utilities.allocProfLevelDescriptorIndex(M08_VBA_Profiling.g_profLevels)].moduleName = M04_Utilities.baseName(M00_Excel.getCell(thisSheet, thisRow, colModuleName).getStringCellValue().trim(), ".bas", null, null, null);
M08_VBA_Profiling.g_profLevels.descriptors[M08_VBA_Profiling_Utilities.allocProfLevelDescriptorIndex(M08_VBA_Profiling.g_profLevels)].procName = M00_Excel.getCell(thisSheet, thisRow, colProcName).getStringCellValue().trim();

NextRow:
thisRow = thisRow + 1;
}
}

public static void profLogClose() {
//On Error Resume Next 

M00_FileWriter.closeFile(profFileNo);
profFileNo = 0;
profCallCount = 0;
profCallLevel = 0;
profPaused = true;
}
// ### ENDIF IVK ###



}