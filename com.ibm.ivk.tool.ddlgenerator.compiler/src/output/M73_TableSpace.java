package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M73_TableSpace {




private static final int colEntryFilter = 1;
private static final int colTableSpaceName = 2;
private static final int colShortName = colTableSpaceName + 1;
private static final int colIsCommonToOrgs = colShortName + 1;
private static final int colSpecificToOrg = colIsCommonToOrgs + 1;
private static final int colIsCommonToPools = colSpecificToOrg + 1;
private static final int colSpecificToPool = colIsCommonToPools + 1;
private static final int colIsPdmSpecific = colSpecificToPool + 1;
private static final int colIsMonitor = colIsPdmSpecific + 1;
private static final int colType = colIsMonitor + 1;
private static final int colManagedBy = colType + 1;
private static final int colPageSize = colManagedBy + 1;
private static final int colAutoResize = colPageSize + 1;
private static final int colIncreasePercent = colAutoResize + 1;
private static final int colIncreaseAbsolute = colIncreasePercent + 1;
private static final int colMaxSize = colIncreaseAbsolute + 1;
private static final int colExtentSize = colMaxSize + 1;
private static final int colPrefetchSize = colExtentSize + 1;
private static final int colBufferPool = colPrefetchSize + 1;
private static final int colOverhead = colBufferPool + 1;
private static final int colTransferRate = colOverhead + 1;
private static final int colUseFileSystemCaching = colTransferRate + 1;
private static final int colSupportDroppedTableReovery = colUseFileSystemCaching + 1;

private static final int processingStep = 2;

private static final int firstRow = 3;

private static final String sheetName = "TS";

public static M73_TableSpace_Utilities.TableSpaceDescriptors g_tableSpaces;

private static void readSheet() {
M73_TableSpace_Utilities.initTableSpaceDescriptors(M73_TableSpace.g_tableSpaces);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colTableSpaceName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].tableSpaceName = M00_Excel.getCell(thisSheet, thisRow, colTableSpaceName).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].isCommonToPools = M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].specificToPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].isMonitor = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsMonitor).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].type = M00_Excel.getCell(thisSheet, thisRow, colType).getStringCellValue();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].category = M73_TableSpace_Utilities.getTabSpaceCategory(M00_Excel.getCell(thisSheet, thisRow, colManagedBy).getStringCellValue());
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].pageSize = M00_Excel.getCell(thisSheet, thisRow, colPageSize).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].autoResize = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colAutoResize).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].increasePercent = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colIncreasePercent).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].increaseAbsolute = M00_Excel.getCell(thisSheet, thisRow, colIncreaseAbsolute).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].maxSize = M00_Excel.getCell(thisSheet, thisRow, colMaxSize).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].extentSize = M00_Excel.getCell(thisSheet, thisRow, colExtentSize).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].prefetchSize = M00_Excel.getCell(thisSheet, thisRow, colPrefetchSize).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].bufferPoolName = M00_Excel.getCell(thisSheet, thisRow, colBufferPool).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].overhead = M00_Excel.getCell(thisSheet, thisRow, colOverhead).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].transferrate = M00_Excel.getCell(thisSheet, thisRow, colTransferRate).getStringCellValue().trim();
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].useFileSystemCaching = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseFileSystemCaching).getStringCellValue(), null);
M73_TableSpace.g_tableSpaces.descriptors[M73_TableSpace_Utilities.allocTableSpaceDescriptorIndex(M73_TableSpace.g_tableSpaces)].supportDroppedTableRecovery = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportDroppedTableReovery).getStringCellValue(), null);

NextRow:
thisRow = thisRow + 1;
}
}


public static void getTableSpaces() {
if ((M73_TableSpace.g_tableSpaces.numDescriptors == 0)) {
readSheet();
}
}


public static void resetTableSpaces() {
M73_TableSpace.g_tableSpaces.numDescriptors = 0;
}


public static Integer getTableSpaceIndexByName(String tableSpaceName) {
Integer returnValue;
int i;

returnValue = -1;
M73_TableSpace.getTableSpaces();

for (i = 1; i <= 1; i += (1)) {
if (M73_TableSpace.g_tableSpaces.descriptors[i].tableSpaceName.toUpperCase() == tableSpaceName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}

public static String getTableSpaceDdlBaseFileName(Integer ddlType) {
String returnValue;
returnValue = M04_Utilities.baseName(M04_Utilities.genDdlFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, ddlType, null, null, null, null, null), null, null, null, null);
return returnValue;
}


private static void genTableSpaceDdl(M73_TableSpace_Utilities.TableSpaceDescriptor tablespace, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

int thisPoolIndex; 
if (thisPoolIndexW == null) {
thisPoolIndex = -1;
} else {
thisPoolIndex = thisPoolIndexW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm &  tablespace.isPdmSpecific) {
return;
}

int fileNo;
String thisOrgDescriptorStr;

thisOrgDescriptorStr = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);

fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);

//On Error GoTo ErrorExit 

M22_Class_Utilities.printChapterHeader("TableSpace \"" + tablespace.tableSpaceName + "\"", fileNo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (!(tablespace.type.compareTo("") == 0) ? tablespace.type.toUpperCase() + " " : "") + "TABLESPACE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tablespace.tableSpaceName.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("PAGESIZE ", null, null) + (!(tablespace.pageSize.compareTo("") == 0) ? tablespace.pageSize : "4096"));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("MANAGED BY ", null, null) + (tablespace.category == M73_TableSpace_Utilities.TabSpaceCategory.tscDms ? "DATABASE" : "SYSTEM"));

int j;
int numContainerRefs;
numContainerRefs = tablespace.containerRefs.numDescriptors;
if (tablespace.category == M73_TableSpace_Utilities.TabSpaceCategory.tscSms) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING (");
for (int j = 1; j <= numContainerRefs; j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.genContainerNameByIndex(tablespace.containerRefs.descriptors[j], thisOrgIndex, thisPoolIndex, null) + "'" + (j == numContainerRefs ? "" : ","));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING (");
for (int j = 1; j <= numContainerRefs; j++) {
int thisContainerIndex;
thisContainerIndex = tablespace.containerRefs.descriptors[j];
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (M74_Container.g_containers.descriptors[tablespace.containerRefs.descriptors[j]].type == M74_Container_Utilities.containerType.cntFile ? "FILE" : "DEVICE") + " " + "'" + M04_Utilities.genContainerNameByIndex(thisContainerIndex, thisOrgIndex, thisPoolIndex, null) + "'" + " " + String.valueOf(M74_Container.g_containers.descriptors[tablespace.containerRefs.descriptors[j]].size) + (j == numContainerRefs ? "" : ","));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

if (tablespace.autoResize) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("AUTORESIZE ", null, null) + "YES");

if (tablespace.increasePercent > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("INCREASESIZE ", null, null) + String.valueOf(tablespace.increasePercent) + " PERCENT");
} else if (!(tablespace.increaseAbsolute.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("INCREASESIZE ", null, null) + tablespace.increaseAbsolute);
}

if (!(tablespace.maxSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("MAXSIZE ", null, null) + tablespace.maxSize);
}
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(tablespace.extentSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("EXTENTSIZE ", null, null) + tablespace.extentSize);
}
if (!(tablespace.prefetchSize.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("PREFETCHSIZE ", null, null) + tablespace.prefetchSize);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("BUFFERPOOL ", null, null) + M04_Utilities.genBufferPoolNameByIndex(tablespace.bufferPoolIndex, thisOrgIndex, thisPoolIndex, null));

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (!(tablespace.useFileSystemCaching) ? "NO " : "") + "FILE SYSTEM CACHING");
if (!(tablespace.overhead.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("OVERHEAD ", null, null) + tablespace.overhead);
}
if (!(tablespace.transferrate.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("TRANSFERRATE ", null, null) + tablespace.transferrate);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.paddRight("DROPPED TABLE RECOVERY ", null, null) + (tablespace.supportDroppedTableRecovery ? "ON" : "OFF"));
}
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, "");

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTableSpacesDdl(Integer ddlType) {
int i;
int thisOrgIndex;
int tabSpaceIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
for (tabSpaceIndex = 1; tabSpaceIndex <= 1; tabSpaceIndex += (1)) {
genTableSpaceDdl(M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex], M01_Common.DdlTypeId.edtLdm, null, null);
}
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (tabSpaceIndex = 1; tabSpaceIndex <= 1; tabSpaceIndex += (1)) {
if (M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].isCommonToOrgs) {
genTableSpaceDdl(M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex], M01_Common.DdlTypeId.edtPdm, null, null);
} else {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].specificToOrgId <= 0 |  M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].specificToOrgId == M71_Org.g_orgs.descriptors[thisOrgIndex].id) {
if (M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].isCommonToPools) {
genTableSpaceDdl(M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex], M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null);
} else {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if ((M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].specificToPool <= 0 |  M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex].specificToPool == M72_DataPool.g_pools.descriptors[thisPoolIndex].id) &  M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genTableSpaceDdl(M73_TableSpace.g_tableSpaces.descriptors[tabSpaceIndex], M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}
}
}
}
}


public static void evalTablespaces() {
int thisTabSpaceIndex;
int thisContainerIndex;
for (thisTabSpaceIndex = 1; thisTabSpaceIndex <= 1; thisTabSpaceIndex += (1)) {
M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.numDescriptors = 0;
for (thisContainerIndex = 1; thisContainerIndex <= 1; thisContainerIndex += (1)) {
if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.toUpperCase() == M74_Container.g_containers.descriptors[thisContainerIndex].tableSpaceName.toUpperCase()) {
M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs.descriptors[(M74_Container_Utilities.allocContainerDescriptorRefIndex(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].containerRefs))] = thisContainerIndex;
}
M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].bufferPoolIndex = M75_BufferPool.getBufferPoolIndexByName(M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].bufferPoolName);
M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceIndex = thisTabSpaceIndex;
}
}
}


}