package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M74_Container {




private static final int colTableSpaceName = 2;
private static final int colContainerName = colTableSpaceName + 1;
private static final int colType = colContainerName + 1;
private static final int colIsCommonToOrgs = colType + 1;
private static final int colSpecificToOrg = colIsCommonToOrgs + 1;
private static final int colIsCommonToPools = colSpecificToOrg + 1;
private static final int colSpecificToPool = colIsCommonToPools + 1;
private static final int colIsPdmSpecific = colSpecificToPool + 1;
private static final int colSize = colIsPdmSpecific + 1;

private static final int processingStep = 2;

private static final int firstRow = 3;

private static final String sheetName = "Cont";

public static M74_Container_Utilities.ContainerDescriptors g_containers;


private static void readSheet() {
M74_Container_Utilities.initContainerDescriptors(M74_Container.g_containers);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colContainerName).getStringCellValue() + "" != "") {
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].tableSpaceName = M00_Excel.getCell(thisSheet, thisRow, colTableSpaceName).getStringCellValue().trim();
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].containerName = M00_Excel.getCell(thisSheet, thisRow, colContainerName).getStringCellValue().trim();
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].type = M74_Container_Utilities.getContainerType(M00_Excel.getCell(thisSheet, thisRow, colType).getStringCellValue());
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].isCommonToPools = M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].specificToPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue(), null);
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
M74_Container.g_containers.descriptors[M74_Container_Utilities.allocContainerDescriptorIndex(M74_Container.g_containers)].size = M04_Utilities.getLong(M00_Excel.getCell(thisSheet, thisRow, colSize).getStringCellValue(), null);

thisRow = thisRow + 1;
}
}


public static void getContainers() {
if ((M74_Container.g_containers.numDescriptors == 0)) {
readSheet();
}
}


public static void resetContainers() {
M74_Container.g_containers.numDescriptors = 0;
}


public static void evalContainers() {
int i;
for (int i = 1; i <= M74_Container.g_containers.numDescriptors; i++) {
M74_Container.g_containers.descriptors[i].containerName = M79_KwMap.kwTranslate(M74_Container.g_containers.descriptors[i].containerName);
M74_Container.g_containers.descriptors[i].containerIndex = i;
}
}


}