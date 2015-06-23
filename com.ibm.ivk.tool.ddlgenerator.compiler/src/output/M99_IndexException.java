package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M99_IndexException {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colSectionShortName = colSection + 1;
private static final int colIndexName = colSectionShortName + 1;
private static final int colNoIndexInPool = colIndexName + 1;

private static final int firstRow = 3;

private static final String sheetName = "IdxExcp";

public static M99_IndexException_Utilities.IndexExcpDescriptors g_indexExcp;


private static void readSheet() {
M99_IndexException_Utilities.initIndexExcpDescriptors(M99_IndexException.g_indexExcp);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M99_IndexException_Utilities.allocIndexExcpDescriptorIndex(M99_IndexException.g_indexExcp);
M99_IndexException.g_indexExcp.descriptors[M99_IndexException.g_indexExcp.numDescriptors].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
M99_IndexException.g_indexExcp.descriptors[M99_IndexException.g_indexExcp.numDescriptors].sectionShortName = M00_Excel.getCell(thisSheet, thisRow, colSectionShortName).getStringCellValue().trim();
M99_IndexException.g_indexExcp.descriptors[M99_IndexException.g_indexExcp.numDescriptors].indexName = M00_Excel.getCell(thisSheet, thisRow, colIndexName).getStringCellValue().trim();
M99_IndexException.g_indexExcp.descriptors[M99_IndexException.g_indexExcp.numDescriptors].noIndexInPool = M00_Excel.getCell(thisSheet, thisRow, colNoIndexInPool).getStringCellValue().trim();

NextRow:
thisRow = thisRow + 1;
}
}


public static void getIndexExcp() {
if ((M99_IndexException.g_indexExcp.numDescriptors == 0)) {
readSheet();
}
}


}