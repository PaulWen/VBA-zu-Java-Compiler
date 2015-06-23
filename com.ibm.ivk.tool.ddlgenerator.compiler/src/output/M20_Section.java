package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M20_Section {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colShortName = colSection + 1;
private static final int colSeqNo = colShortName + 1;
private static final int colSpecificToOrgs = colSeqNo + 1;
private static final int colSpecificToPool = colSpecificToOrgs + 1;
private static final int colJavaPackage = colSpecificToPool + 1;
private static final int colJavaParentPackage = colJavaPackage + 1;

private static final int firstRow = 3;

private static final String sheetName = "Sect";

private static final int ldmSchemaCsvProcessingStep = 1;
private static final int acmSchemaCsvProcessingStep = 1;
private static final int pdmCsvProcessingStep = 2;

public static M20_Section_Utilities.SectionDescriptors g_sections;


private static void readSheet() {
M20_Section_Utilities.initSectionDescriptors(M20_Section.g_sections);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

M20_Section.g_sections.maxSeqNo = -1;

while (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSeqNo).getStringCellValue(), null);
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].specificToOrgs = M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrgs).getStringCellValue().trim();
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].specificToPools = M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue().trim();

if (M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName.toUpperCase() == "FWKTEST" & ! M03_Config.generateFwkTest) {
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].specificToOrgs = String.valueOf(100);//this will never be a valid MPC
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].specificToPools = String.valueOf(100);
}
M20_Section.g_sections.maxSeqNo = (M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo > M20_Section.g_sections.maxSeqNo ? M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo : M20_Section.g_sections.maxSeqNo);

NextRow:
thisRow = thisRow + 1;
}

// add some technical sections
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM.snAlias;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM.ssnAlias;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM_IVK.snAliasDelObj;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM_IVK.ssnAliasDelObj;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM.snAliasLrt;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM.ssnAliasLrt;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM_IVK.snAliasPsDpFiltered;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM_IVK.ssnAliasPsDpFiltered;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM_IVK.snAliasPsDpFilteredExtended;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM_IVK.ssnAliasPsDpFilteredExtended;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM.snAliasPrivateOnly;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM.ssnAliasPrivateOnly;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].sectionName = M01_ACM.snHelp;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].shortName = M01_ACM.ssnHelp;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo = M20_Section.g_sections.maxSeqNo + 1;
M20_Section.g_sections.maxSeqNo = M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].seqNo;
M20_Section.g_sections.descriptors[M20_Section_Utilities.allocSectionDescriptorIndex(M20_Section.g_sections)].isTechnical = true;
}


public static M20_Section_Utilities.SectionDescriptors getSections() {
M20_Section_Utilities.SectionDescriptors returnValue;
if ((M20_Section.g_sections.numDescriptors == 0)) {
readSheet();
}
return returnValue;
}

public static void resetSections() {
M20_Section.g_sections.numDescriptors = 0;
}


public static Integer getSectionIndexByName(String sectName,  Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;
M20_Section.getSections();

for (i = 1; i <= 1; i += (1)) {
if (M20_Section.g_sections.descriptors[i].sectionName.toUpperCase() == sectName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
M04_Utilities.logMsg("unable to identify section '" + sectName + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtLdm, null, null);
}
return returnValue;
}


public static String getSectionShortNameByName(String sectName) {
String returnValue;
int sectIndex;
returnValue = sectName;
sectIndex = M20_Section.getSectionIndexByName(sectName, null);
if ((sectIndex > 0)) {
returnValue = M20_Section.g_sections.descriptors[sectIndex].shortName;
}

return returnValue;
}


public static String getSectionSeqNoByName(String sectName) {
String returnValue;
int sectIndex;
returnValue = 0;
sectIndex = M20_Section.getSectionIndexByName(sectName, null);
if ((sectIndex > 0)) {
returnValue = M20_Section.g_sections.descriptors[sectIndex].seqNo;
}

return returnValue;
}


public static String getSectionSeqNoByIndex(int sectionIndex) {
String returnValue;
returnValue = 0;
if ((sectionIndex > 0)) {
returnValue = M20_Section.g_sections.descriptors[sectionIndex].seqNo;
}

return returnValue;
}


public static void genSectionAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmSection, acmSchemaCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M20_Section.g_sections.numDescriptors; i++) {
if (M00_Helper.inStr(1, M20_Section.g_sections.descriptors[i].specificToOrgs, "999") <= 0 & ! M20_Section.g_sections.descriptors[i].isTechnical != 0) {
M00_FileWriter.printToFile(fileNo, "\"" + M20_Section.g_sections.descriptors[i].sectionName.toUpperCase() + "\",\"" + M20_Section.g_sections.descriptors[i].shortName.toUpperCase() + "\"," + M04_Utilities.getCsvTrailer(0));
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genSectionLdmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnLdmSchema, ldmSchemaCsvProcessingStep, "LDM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M20_Section.g_sections.numDescriptors; i++) {
if (M00_Helper.inStr(1, M20_Section.g_sections.descriptors[i].specificToOrgs, "999") <= 0 & ! M20_Section.g_sections.descriptors[i].isTechnical != 0) {
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[i].sectionName, M20_Section.g_sections.descriptors[i].shortName, null, null, null) + "\"," + M04_Utilities.getCsvTrailer(0));
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genSectionPdmMetaCsv() {
String fileName;
String fileNamePri;
int fileNo;
int fileNoPri;
int fileNoTemplate;
int fileNoPriTemplate;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmSchema, pdmCsvProcessingStep, "PDM", M01_Common.DdlTypeId.edtPdm, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
fileNamePri = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmPrimarySchema, pdmCsvProcessingStep, "PDM", M01_Common.DdlTypeId.edtPdm, null, null, null, null, null);
fileNoPri = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoPri, fileNamePri, true);
//On Error GoTo ErrorExit 

int thisSection;
int thisPoolIndex;
int thisPoolId;
int thisOrgIndex;
int thisOrgId;
String schemaNameLdm;
String schemaNamePdm;
String schemaNameAliasPdm;
String schemaNameNativePdm;
String schemaNamePrivateOnlyPdm;
String schemaNamePublicOnlyPdm;
int thisFileNo;
String orgIdStr;
// ### IF IVK ###
String schemaNamePsDpFilteredPdm;
String schemaNamePsDpFilteredPdmExtended;
String schemaNameDeletedObjectPdm;
// ### ENDIF IVK ###

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) & ! M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
schemaNameAliasPdm = M04_Utilities.genSchemaName(M01_ACM.snAliasLrt, M01_ACM.ssnAliasLrt, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
// ### IF IVK ###
schemaNamePsDpFilteredPdm = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasPsDpFiltered, M01_ACM_IVK.ssnAliasPsDpFiltered, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNamePsDpFilteredPdmExtended = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasPsDpFilteredExtended, M01_ACM_IVK.ssnAliasPsDpFilteredExtended, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNameDeletedObjectPdm = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasDelObj, M01_ACM_IVK.ssnAliasDelObj, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
// ### ENDIF IVK ###
schemaNamePrivateOnlyPdm = M04_Utilities.genSchemaName(M01_ACM.snAliasPrivateOnly, M01_ACM.ssnAliasPrivateOnly, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
} else {
// ### IF IVK ###
if (M03_Config.supportAliasDelForNonLrtPools) {
schemaNameAliasPdm = M04_Utilities.genSchemaName(M01_ACM.snAlias, M01_ACM.ssnAlias, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNameDeletedObjectPdm = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasDelObj, M01_ACM_IVK.ssnAliasDelObj, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
} else {
schemaNameAliasPdm = M04_Utilities.genSchemaName(M01_ACM.snAliasLrt, M01_ACM.ssnAliasLrt, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNameDeletedObjectPdm = "";
}
schemaNamePsDpFilteredPdm = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasPsDpFiltered, M01_ACM_IVK.ssnAliasPsDpFiltered, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNamePsDpFilteredPdmExtended = M04_Utilities.genSchemaName(M01_ACM_IVK.snAliasPsDpFilteredExtended, M01_ACM_IVK.ssnAliasPsDpFilteredExtended, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
// ### ELSE IVK ###
//           schemaNameAliasPdm = genSchemaName(snAliasLrt, ssnAliasLrt, edtPdm, thisOrgIndex, thisPoolIndex)
// ### ENDIF IVK ###
schemaNamePrivateOnlyPdm = "";
}

schemaNameNativePdm = M04_Utilities.genSchemaName(M01_ACM.snAlias, M01_ACM.ssnAlias, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
schemaNamePublicOnlyPdm = M04_Utilities.genSchemaName(M01_ACM.snAliasPublicOnly, M01_ACM.ssnAliasPublicOnly, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);

thisFileNo = fileNoPri;
orgIdStr = String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id);

M00_FileWriter.printToFile(thisFileNo, "\"" + schemaNameAliasPdm + "\",");
// ### IF IVK ###
M00_FileWriter.printToFile(thisFileNo, "\"" + schemaNamePsDpFilteredPdm + "\",");
M00_FileWriter.printToFile(thisFileNo, "\"" + schemaNamePsDpFilteredPdmExtended + "\",");
M00_FileWriter.printToFile(thisFileNo, (schemaNameDeletedObjectPdm.compareTo("") == 0 ? "" : "\"" + schemaNameDeletedObjectPdm + "\"") + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(thisFileNo, (schemaNameNativePdm.compareTo("") == 0 ? "" : "\"" + schemaNameNativePdm + "\"") + ",");
M00_FileWriter.printToFile(thisFileNo, (schemaNamePrivateOnlyPdm.compareTo("") == 0 ? "" : "\"" + schemaNamePrivateOnlyPdm + "\"") + ",");
M00_FileWriter.printToFile(thisFileNo, (schemaNamePublicOnlyPdm.compareTo("") == 0 ? "" : "\"" + schemaNamePublicOnlyPdm + "\"") + ",");
M00_FileWriter.printToFile(thisFileNo, orgIdStr + ",");
M00_FileWriter.printToFile(thisFileNo, String.valueOf(thisPoolId) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(thisFileNo, "0,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(thisFileNo, M04_Utilities.getCsvTrailer(0));

if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
M00_FileWriter.closeFile(thisFileNo);
}
}
}
}
}

for (int thisSection = 1; thisSection <= M20_Section.g_sections.numDescriptors; thisSection++) {
// HACK - we have some dummy MPC 999
if (M00_Helper.inStr(1, M20_Section.g_sections.descriptors[thisSection].specificToOrgs, "999") <= 0 & ! M20_Section.g_sections.descriptors[thisSection].isTechnical != 0) {
schemaNameLdm = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[thisSection].sectionName, M20_Section.g_sections.descriptors[thisSection].shortName, M01_Common.DdlTypeId.edtLdm, null, null);
schemaNamePdm = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[thisSection].sectionName, M20_Section.g_sections.descriptors[thisSection].shortName, M01_Common.DdlTypeId.edtPdm, null, null);
M00_FileWriter.printToFile(fileNo, "\"" + schemaNamePdm + "\"," + "," + "," + "\"" + schemaNameLdm + "\"," + M04_Utilities.getCsvTrailer(0));
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (!(M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate)) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;

if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmSchema, pdmCsvProcessingStep, "PDM", M01_Common.DdlTypeId.edtPdm, null, null, null, null, thisOrgIndex);
fileNoTemplate = M00_FileWriter.freeFileNumber();
M04_Utilities.assertDir(fileName);
M00_FileWriter.openFileForOutput(fileNoTemplate, fileName, true);
thisFileNo = fileNoTemplate;
orgIdStr = M04_Utilities.genTemplateParamWrapper(String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id), null);
} else {
thisFileNo = fileNo;
orgIdStr = String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id);
}

if ((M20_Section.g_sections.descriptors[thisSection].specificToOrgs.compareTo("") == 0 |  M04_Utilities.includedInList(M20_Section.g_sections.descriptors[thisSection].specificToOrgs, thisOrgId))) {
if (M20_Section.g_sections.descriptors[thisSection].specificToPools.compareTo("") == 0 |  M04_Utilities.includedInList(M20_Section.g_sections.descriptors[thisSection].specificToPools, 0)) {
schemaNamePdm = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[thisSection].sectionName, M20_Section.g_sections.descriptors[thisSection].shortName, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null);
M00_FileWriter.printToFile(thisFileNo, "\"" + schemaNamePdm + "\"," + orgIdStr + "," + "," + "\"" + schemaNameLdm + "\"," + M04_Utilities.getCsvTrailer(0));
}

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;

if ((M20_Section.g_sections.descriptors[thisSection].specificToPools.compareTo("") == 0 |  M04_Utilities.includedInList(M20_Section.g_sections.descriptors[thisSection].specificToPools, thisPoolId)) &  M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) & M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm) {
schemaNamePdm = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[thisSection].sectionName, M20_Section.g_sections.descriptors[thisSection].shortName, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(thisFileNo, "\"" + schemaNamePdm + "\"," + orgIdStr + "," + String.valueOf(thisPoolId) + "," + "\"" + schemaNameLdm + "\"," + M04_Utilities.getCsvTrailer(0));
}
}
}

if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
M00_FileWriter.closeFile(thisFileNo);
}
}
}
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
M00_FileWriter.closeFile(fileNoPri);
M00_FileWriter.closeFile(fileNoTemplate);
M00_FileWriter.closeFile(fileNoPriTemplate);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropSectionsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnLdmSchema, M01_Globals.g_targetDir, ldmSchemaCsvProcessingStep, onlyIfEmpty, "LDM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmSchema, M01_Globals.g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmPrimarySchema, M01_Globals.g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM");
}


public static void evalSections() {
int thisSectionIndex;
for (thisSectionIndex = 1; thisSectionIndex <= 1; thisSectionIndex += (1)) {
M20_Section.g_sections.descriptors[thisSectionIndex].sectionIndex = thisSectionIndex;
//Compiler: array mit vier dimensionen
//ReDim .fileNoDdl(-1 To g_orgs.numDescriptors, -1 To g_pools.numDescriptors, 1 To gc_maxProcessingStep, UBound(g_fileNameIncrements))
}
}


}