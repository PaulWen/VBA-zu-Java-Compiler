package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M75_BufferPool {




private static final int colBufPoolName = 2;
private static final int colShortName = colBufPoolName + 1;
private static final int colIsCommonToOrgs = colShortName + 1;
private static final int colSpecificToOrg = colIsCommonToOrgs + 1;
private static final int colIsCommonToPools = colSpecificToOrg + 1;
private static final int colSpecificToPool = colIsCommonToPools + 1;
private static final int colIsPdmSpecific = colSpecificToPool + 1;
private static final int colNumBlockPages = colIsPdmSpecific + 1;
private static final int colPageSize = colNumBlockPages + 1;
private static final int colSize = colPageSize + 1;

private static final int processingStep = 1;

private static final int firstRow = 3;

private static final String sheetName = "BP";

public static M75_BufferPool_Utilities.BufferPoolDescriptors g_bufPools;


private static void readSheet() {
M75_BufferPool_Utilities.BufferPoolDescriptor thisSection;

M75_BufferPool_Utilities.initBufferPoolDescriptors(M75_BufferPool.g_bufPools);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colBufPoolName).getStringCellValue() + "" != "") {
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].bufPoolName = M00_Excel.getCell(thisSheet, thisRow, colBufPoolName).getStringCellValue().trim();
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].isCommonToPools = M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].specificToPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].numBlockPages = M04_Utilities.getLong(M00_Excel.getCell(thisSheet, thisRow, colNumBlockPages).getStringCellValue(), null);
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].pageSize = M00_Excel.getCell(thisSheet, thisRow, colPageSize).getStringCellValue();
M75_BufferPool.g_bufPools.descriptors[M75_BufferPool_Utilities.allocBufferPoolDescriptorIndex(M75_BufferPool.g_bufPools)].numPages = M04_Utilities.getLong(M00_Excel.getCell(thisSheet, thisRow, colSize).getStringCellValue(), null);

thisRow = thisRow + 1;
}
}


public static void getBufferPools() {
if ((M75_BufferPool.g_bufPools.numDescriptors == 0)) {
readSheet();
}
}


public static void resetBufferPools() {
M75_BufferPool.g_bufPools.numDescriptors = 0;
}


public static Integer getBufferPoolIndexByName(String bufPoolName) {
Integer returnValue;
int i;

returnValue = -1;
M75_BufferPool.getBufferPools();

for (i = 1; i <= 1; i += (1)) {
if (M75_BufferPool.g_bufPools.descriptors[i].bufPoolName.toUpperCase() == bufPoolName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static String getBufferPoolDdlBaseFileName(Integer ddlType) {
String returnValue;
returnValue = M04_Utilities.baseName(M04_Utilities.genDdlFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, ddlType, null, null, null, null, null), null, null, null, null);
return returnValue;
}


private static void genBufferPoolDdl(int thisBufPoolIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

if (ddlType != M01_Common.DdlTypeId.edtPdm |  thisBufPoolIndex < 0) {
return;
}

if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].isPdmSpecific &  ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

String thisOrgDescriptorStr;
thisOrgDescriptorStr = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);

int fileNoCr;
fileNoCr = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);

//On Error GoTo ErrorExit 

M22_Class_Utilities.printChapterHeader("Bufferpool \"" + M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolName + "\"", fileNoCr);
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(0) + "CREATE BUFFERPOOL");
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(1) + M04_Utilities.genBufferPoolNameByIndex(M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolIndex, thisOrgIndex, thisPoolIndex, null));
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(1) + "SIZE " + String.valueOf(M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].numPages));
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(1) + "PAGESIZE " + String.valueOf(M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].pageSize));
if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].numBlockPages >= 0) {
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(1) + "NUMBLOCKPAGES " + String.valueOf(M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].numBlockPages));
}
M00_FileWriter.printToFile(fileNoCr, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNoCr, "");

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoCr);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genBufferPoolsDdl(Integer ddlType) {
int i;
int thisOrgIndex;
int thisBufPoolIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
for (thisBufPoolIndex = 1; thisBufPoolIndex <= 1; thisBufPoolIndex += (1)) {
genBufferPoolDdl(thisBufPoolIndex, M01_Common.DdlTypeId.edtLdm, null, null);
}
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisBufPoolIndex = 1; thisBufPoolIndex <= 1; thisBufPoolIndex += (1)) {
if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].isCommonToOrgs) {
genBufferPoolDdl(thisBufPoolIndex, M01_Common.DdlTypeId.edtPdm, null, null);
} else {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].specificToOrgId <= 0 |  M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].specificToOrgId == M71_Org.g_orgs.descriptors[thisOrgIndex].id) {
if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].isCommonToPools) {
genBufferPoolDdl(thisBufPoolIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null);
} else {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if ((M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].specificToPool <= 0 |  M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].specificToPool == M72_DataPool.g_pools.descriptors[thisPoolIndex].id) &  M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genBufferPoolDdl(thisBufPoolIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}
}
}
}
}


}