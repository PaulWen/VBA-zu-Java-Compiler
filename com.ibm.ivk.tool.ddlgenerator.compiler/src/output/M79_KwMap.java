package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_KwMap {




private static final int colKeywordName = 2;
private static final int colValue = colKeywordName + 1;

private static final int processingStep = 2;
private static final int firstRow = 3;
private static final String sheetName = "KwMap";

public static M79_KwMap_Utilities.KwMapDescriptors g_kwMaps;


private static void readSheet() {
M79_KwMap_Utilities.initKwMapDescriptors(M79_KwMap.g_kwMaps);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colKeywordName).getStringCellValue() + "" != "") {
M79_KwMap.g_kwMaps.descriptors[M79_KwMap_Utilities.allocKwMapDescriptorIndex(M79_KwMap.g_kwMaps)].keyword = M00_Excel.getCell(thisSheet, thisRow, colKeywordName).getStringCellValue().trim();
M79_KwMap.g_kwMaps.descriptors[M79_KwMap_Utilities.allocKwMapDescriptorIndex(M79_KwMap.g_kwMaps)].value = M00_Excel.getCell(thisSheet, thisRow, colValue).getStringCellValue().trim();

thisRow = thisRow + 1;
}
}


public static void getKwMaps() {
if ((M79_KwMap.g_kwMaps.numDescriptors == 0)) {
readSheet();
}
}


public static void resetKwMaps() {
M79_KwMap.g_kwMaps.numDescriptors = 0;
}


public static String kwTranslate( String text) {
String returnValue;
// predefined keywords
text = M00_Helper.replace(text, "<productKey>", M03_Config.productKey.toUpperCase());

int i;
for (int i = 1; i <= M79_KwMap.g_kwMaps.numDescriptors; i++) {
text = M00_Helper.replace(text, M79_KwMap.g_kwMaps.descriptors[i].keyword, M79_KwMap.g_kwMaps.descriptors[i].value);
}

returnValue = text;
return returnValue;
}

}