package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M25_Domain {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colDomain = colSection + 1;
private static final int colDataType = colDomain + 1;
private static final int colMinLength = colDataType + 1;
private static final int colMaxLength = colMinLength + 1;
private static final int colScale = colMaxLength + 1;
private static final int colMinValue = colScale + 1;
private static final int colMaxValue = colMinValue + 1;
private static final int colValueList = colMaxValue + 1;
private static final int colCheckConstraint = colValueList + 1;
private static final int colNotLogged = colCheckConstraint + 1;
private static final int colNotCompact = colNotLogged + 1;
private static final int colIsGenerated = colNotCompact + 1;
private static final int colUnicodeExpansionFactor = colIsGenerated + 1;

private static final int firstRow = 3;

private static final String sheetName = "Dom";

private static final int acmCsvProcessingStep = 3;
private static final int acmCsvProcessingStepEnum = 4;

public static M25_Domain_Utilities.DomainDescriptors g_domains;

private static M25_Domain_Utilities.DomainDescriptors readSheet() {
M25_Domain_Utilities.DomainDescriptors returnValue;
M25_Domain_Utilities.initDomainDescriptors(M25_Domain.g_domains);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

String lastSection;
while (M00_Excel.getCell(thisSheet, thisRow, colDomain).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
if ((M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].sectionName + "" == "")) {
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].sectionName = lastSection;
}

M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].domainName = M00_Excel.getCell(thisSheet, thisRow, colDomain).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType = M02_ToolMeta.getDataTypeId(M00_Excel.getCell(thisSheet, thisRow, colDataType).getStringCellValue());
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].minLength = M00_Excel.getCell(thisSheet, thisRow, colMinLength).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].maxLength = M00_Excel.getCell(thisSheet, thisRow, colMaxLength).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].scale = M00_Excel.getCell(thisSheet, thisRow, colScale).getStringCellValue();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].minValue = M00_Excel.getCell(thisSheet, thisRow, colMinValue).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].maxValue = M00_Excel.getCell(thisSheet, thisRow, colMaxValue).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].valueList = M00_Excel.getCell(thisSheet, thisRow, colValueList).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].constraint = M00_Excel.getCell(thisSheet, thisRow, colCheckConstraint).getStringCellValue().trim();
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].notLogged = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotLogged).getStringCellValue(), null);
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].notCompact = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotCompact).getStringCellValue(), null);
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].supportUnicode = (M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etChar |  M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etClob | M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etLongVarchar | M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etVarchar) &  (M00_Excel.getCell(thisSheet, thisRow, colUnicodeExpansionFactor).getStringCellValue().trim() != "");
M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].isGenerated = (M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etBigInt |  M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etInteger | M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].dataType == M01_Common.typeId.etSmallint) &  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsGenerated).getStringCellValue(), null);

M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].unicodeExpansionFactor = (M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].supportUnicode ? M04_Utilities.getSingle(M00_Excel.getCell(thisSheet, thisRow, colUnicodeExpansionFactor).getStringCellValue(), M03_Config.unicodeExpansionFactor) : 1);

lastSection = M25_Domain.g_domains.descriptors[M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains)].sectionName;
NextRow:

thisRow = thisRow + 1;
}
return returnValue;
}


public static void getDomains() {
if (M25_Domain.g_domains.numDescriptors == 0) {
readSheet();
}
}


public static void resetDomains() {
M25_Domain.g_domains.numDescriptors = 0;
}


public static Integer getDomainIndexByName(String sectionName, String domainName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;
M25_Domain.getDomains();

for (i = 1; i <= 1; i += (1)) {
if (M25_Domain.g_domains.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase() &  M25_Domain.g_domains.descriptors[i].domainName.toUpperCase() == domainName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
errMsgBox("unable to identify domain '" + sectionName + "." + domainName + "'", vbCritical);
}
return returnValue;
}


public static String getDbDatatypeByDomainIndex(int domainIndex) {
String returnValue;
returnValue = "";

if ((domainIndex > 0)) {
returnValue = M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[domainIndex].dataType, M25_Domain.g_domains.descriptors[domainIndex].maxLength, M25_Domain.g_domains.descriptors[domainIndex].scale, M25_Domain.g_domains.descriptors[domainIndex].supportUnicode, M25_Domain.g_domains.descriptors[domainIndex].unicodeExpansionFactor);
}
return returnValue;
}


public static String getDbDataTypeByDomainName(String sectionName, String domainName) {
String returnValue;
int domainIndex;
returnValue = "";
domainIndex = M25_Domain.getDomainIndexByName(sectionName, domainName, null);
if ((domainIndex > 0)) {
returnValue = M25_Domain.getDbDatatypeByDomainIndex(domainIndex);
}

return returnValue;
}


public static Integer getDbMaxDataTypeLengthByDomainName(String sectionName, String domainName) {
Integer returnValue;
int domainIndex;
returnValue = -1;
domainIndex = M25_Domain.getDomainIndexByName(sectionName, domainName, null);
if ((domainIndex > 0)) {
returnValue = M25_Domain.g_domains.descriptors[domainIndex].maxLength * (M25_Domain.g_domains.descriptors[domainIndex].supportUnicode ? (M25_Domain.g_domains.descriptors[domainIndex].unicodeExpansionFactor >= 1 ? M25_Domain.g_domains.descriptors[domainIndex].unicodeExpansionFactor : M03_Config.unicodeExpansionFactor) : 1);
}
return returnValue;
}


public static void evalDomains() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
M25_Domain.g_domains.descriptors[i].domainIndex = i;
}
}


public static void dropDomainCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmDomain, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmDomain, M01_Globals.g_targetDir, acmCsvProcessingStepEnum, onlyIfEmpty, "ACM");
}


public static void genDomainAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmDomain, acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M25_Domain.g_domains.numDescriptors; i++) {
if (!(M25_Domain.g_domains.descriptors[i].isGenerated)) {
M00_FileWriter.printToFile(fileNo, "\"" + M25_Domain.g_domains.descriptors[i].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M25_Domain.g_domains.descriptors[i].domainName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "0,");
M00_FileWriter.printToFile(fileNo, "\"" + M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[i].dataType, M25_Domain.g_domains.descriptors[i].maxLength, M25_Domain.g_domains.descriptors[i].scale, M25_Domain.g_domains.descriptors[i].supportUnicode, M25_Domain.g_domains.descriptors[i].unicodeExpansionFactor).toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].minLength.compareTo("") == 0 ? "" : "\"" + M25_Domain.g_domains.descriptors[i].minLength + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].maxLength == "" ? "" : "\"" + M25_Domain.g_domains.descriptors[i].maxLength + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].minValue.compareTo("") == 0 ? "" : M25_Domain.g_domains.descriptors[i].minValue) + ",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].minValue.compareTo("") == 0 ? "" : M25_Domain.g_domains.descriptors[i].maxValue) + ",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].scale < 0 ? "" : String.valueOf(M25_Domain.g_domains.descriptors[i].scale)) + ",");
M00_FileWriter.printToFile(fileNo, (M25_Domain.g_domains.descriptors[i].supportUnicode ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}
}

M00_FileWriter.closeFile(fileNo);
fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmDomain, acmCsvProcessingStepEnum, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);

for (int i = 1; i <= M21_Enum.g_enums.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].enumName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "1,");
M00_FileWriter.printToFile(fileNo, "\"" + M02_ToolMeta.getDataType(M21_Enum.g_enums.descriptors[i].idDataType, null, null, null, null).toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, ",,,,,");
M00_FileWriter.printToFile(fileNo, "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}