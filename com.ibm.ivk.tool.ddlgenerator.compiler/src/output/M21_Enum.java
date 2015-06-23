package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M21_Enum {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colEnumName = colSection + 1;
private static final int colI18nId = colEnumName + 1;
private static final int colIsEnumlang = colI18nId + 1;
private static final int colShortName = colIsEnumlang + 1;
private static final int colIdDomainSection = colShortName + 1;
private static final int colIdDomainName = colIdDomainSection + 1;
private static final int colValueMaxLength = colIdDomainName + 1;
private static final int colIsCommonToOrgs = colValueMaxLength + 1;
private static final int colIsCommonToPools = colIsCommonToOrgs + 1;
private static final int colEnumId = colIsCommonToPools + 1;
private static final int colNotAcmRelated = colEnumId + 1;
private static final int colNoAlias = colNotAcmRelated + 1;
// ### IF IVK ###
private static final int colNoXmlExport = colNoAlias + 1;
private static final int colUseXmlExport = colNoXmlExport + 1;
private static final int colIsLrtSpecific = colUseXmlExport + 1;
// ### ELSE IVK ###
//Private Const colIsLrtSpecific = colNoAlias + 1
// ### ENDIF IVK ###
private static final int colIsPdmSpecific = colIsLrtSpecific + 1;
private static final int colTabSpaceData = colIsPdmSpecific + 1;
private static final int colTabSpaceLong = colTabSpaceData + 1;
private static final int colTabSpaceNl = colTabSpaceLong + 1;
private static final int colTabSpaceIndex = colTabSpaceNl + 1;
private static final int colValueId = colTabSpaceIndex + 1;
private static final int colValueLang1 = colValueId + 1;
private static final int colValueLang2 = colValueLang1 + 1;
private static final int colValueLang3 = colValueLang2 + 1;
private static final int colValueLang4 = colValueLang3 + 1;

private static final int colFirstValueLang = colValueLang1;
private static final int colLastValueLang = colValueLang4;
private static final int colFirstAttr = colLastValueLang + 1;

private static final int firstRow = 3;

private static final String sheetName = "Enum";

private static final int processingStep = 1;
private static final int acmCsvProcessingStep = 0;

private static final String suffixLabel = "_LABEL";
private static final String suffixLabelShort = "LBL";
private static final String suffixText = "_TEXT";
private static final String suffixTextShort = "TXT";

public static M21_Enum_Utilities.EnumDescriptors g_enums;


private static void checkRow(Sheet thisSheet, int thisRow, String secName, String enumName, String shortName) {
if (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
secName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue();
}

if (M00_Excel.getCell(thisSheet, thisRow, colEnumName).getStringCellValue() + "" != "") {
enumName = M00_Excel.getCell(thisSheet, thisRow, colEnumName).getStringCellValue();
}

if (M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue() + "" != "") {
shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue();
}

}

private static void readSheet() {
M21_Enum_Utilities.initEnumDescriptors(M21_Enum.g_enums);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
String lastEnumName;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

String currSection;
String currEnumName;
String currShortName;
checkRow(thisSheet, thisRow, currSection, currEnumName, currShortName);
lastEnumName = "";

int i;
while (M00_Excel.getCell(thisSheet, thisRow, colEnumName).getStringCellValue() + M00_Excel.getCell(thisSheet, thisRow, colValueId).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

if (!(currEnumName.compareTo("") == 0) &  !(currEnumName.compareTo(lastEnumName) == 0)) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].sectionName = currSection;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumName = currEnumName;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isEnumLang = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsEnumlang).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].shortName = currShortName;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainSection = M00_Excel.getCell(thisSheet, thisRow, colIdDomainSection).getStringCellValue().trim();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainName = M00_Excel.getCell(thisSheet, thisRow, colIdDomainName).getStringCellValue().trim();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].maxLength = (M00_Excel.getCell(thisSheet, thisRow, colValueMaxLength).getStringCellValue() + "" == "" ? -1 : new Double(M00_Excel.getCell(thisSheet, thisRow, colValueMaxLength).getStringCellValue()).intValue());
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToPools = M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colEnumId).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].notAcmRelated = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotAcmRelated).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].noAlias = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoAlias).getStringCellValue(), null);
// ### IF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].noXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoXmlExport).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].useXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseXmlExport).getStringCellValue(), null);
// ### ENDIF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isLrtSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsLrtSpecific).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceData = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceData).getStringCellValue();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceLong = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceLong).getStringCellValue();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceNl = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceNl).getStringCellValue();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceIndex = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceIndex).getStringCellValue();

M21_Enum_Utilities.initEnumVals(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values);

String thisEnumName;
thisEnumName = currEnumName;
while (M00_Excel.getCell(thisSheet, thisRow, colValueId).getStringCellValue() + "" != "" &  thisEnumName.compareTo(currEnumName) == 0) {
int colLang;
for (colLang = colFirstValueLang; colLang <= 1; colLang += (1)) {
if (M00_Excel.getCell(thisSheet, thisRow, colLang).getStringCellValue() + "" != "") {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].id = new Double(M00_Excel.getCell(thisSheet, thisRow, colValueId).getStringCellValue()).intValue();
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M04_Utilities.pullOid;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].languageId = colLang - colFirstValueLang + 1;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].valueString = M00_Excel.getCell(thisSheet, thisRow, colLang).getStringCellValue().trim();
for (i = 1; i <= 1; i += (1)) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(i)] = M00_Excel.getCell(thisSheet, thisRow, colFirstAttr + i - 1).getStringCellValue();
}
}
}
thisRow = thisRow + 1;
if (M00_Excel.getCell(thisSheet, thisRow, colEnumName).getStringCellValue() + "" != "") {
thisEnumName = M00_Excel.getCell(thisSheet, thisRow, colEnumName).getStringCellValue();
}

}
lastEnumName = currEnumName;
checkRow(thisSheet, thisRow, currSection, currEnumName, currShortName);
}
if (currEnumName.compareTo(lastEnumName) == 0) {
NextRow:
thisRow = thisRow + 1;
checkRow(thisSheet, thisRow, currSection, currEnumName, currShortName);
}
}
}

private static void genEnumDdl(int thisEnumIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
return;
}

int thisOrgId;
int thisPoolId;
if (thisOrgIndex > 0) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
} else {
thisOrgId = -1;
}

if (thisPoolIndex > 0) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;
} else {
thisPoolId = -1;
}


// ### IF IVK ###
int fileNoXmlF;
int fileNoXmlV;
// ### ENDIF IVK ###
boolean poolSupportXmlExport;
boolean poolSupportAcm;

//On Error GoTo ErrorExit 

if (thisPoolIndex > 0) {
poolSupportXmlExport = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportXmlExport;
poolSupportAcm = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm;
}

int ldmIteration;
if (M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "" == "") {
goto NormalExit;
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].isLrtSpecific & ! M01_Globals.g_genLrtSupport) {
goto NormalExit;
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].isPdmSpecific &  ddlType != M01_Common.DdlTypeId.edtPdm) {
goto NormalExit;
}

if (M03_Config.ignoreUnknownSections &  (M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex < 0)) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs &  (!(M20_Section.g_sections.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex].specificToOrgs.compareTo("") == 0) & ! M04_Utilities.includedInList(M20_Section.g_sections.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex].specificToOrgs, thisOrgId)))) {
goto NormalExit;
}
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools &  (!(M20_Section.g_sections.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex].specificToPools.compareTo("") == 0) & ! M04_Utilities.includedInList(M20_Section.g_sections.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex].specificToPools, thisPoolId)))) {
goto NormalExit;
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolId != -1) {
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated & ! poolSupportAcm)) {
goto NormalExit;
}
}

ldmIteration = (M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs ? M01_Common.ldmIterationGlobal : M01_Common.ldmIterationPoolSpecific);

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);

// ### IF IVK ###
if (M03_Config.generateXmlExportSupport) {
fileNoXmlV = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseXmlExport, ldmIteration);

if (M03_Config.generateXsdInCtoSchema &  ddlType == M01_Common.DdlTypeId.edtPdm & thisOrgIndex > 0) {
fileNoXmlF = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, processingStep, ddlType, null, null, null, M01_Common.phaseXmlExport, ldmIteration);
} else {
fileNoXmlF = fileNoXmlV;
}
}

// ### ENDIF IVK ###

String qualTabName;
String qualTabNameLdm;
String qualIndexName;
String qualLangTabName;
String qualLangTabNameLdm;

qualTabName = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);
qualTabNameLdm = (ddlType == M01_Common.DdlTypeId.edtLdm ? qualTabName : M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null));
qualLangTabName = M21_Enum.getQualTabNameLanguageEnum(thisOrgIndex, thisPoolIndex, ddlType);
qualLangTabNameLdm = M21_Enum.getQualTabNameLanguageEnum(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtLdm);

M96_DdlSummary.addTabToDdlSummary(qualTabName, ddlType, M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated);
M78_DbMeta.registerQualTable(qualTabNameLdm, qualTabName, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, thisOrgIndex, thisPoolIndex, ddlType, M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated, null, null, null, null);

String enumNameLbl;
String enumNameLblShort;
String enumNameDbLbl;
String enumNameDbLblShort;
enumNameLbl = M04_Utilities.genNlObjName(M21_Enum.g_enums.descriptors[thisEnumIndex].enumName, null, null, null);
enumNameLblShort = M04_Utilities.genNlObjShortName(M21_Enum.g_enums.descriptors[thisEnumIndex].shortName, null, null, null);
enumNameDbLbl = M04_Utilities.genNlObjName(M21_Enum.g_enums.descriptors[thisEnumIndex].enumNameDb, null, null, null);
enumNameDbLblShort = M04_Utilities.genNlObjShortName(M21_Enum.g_enums.descriptors[thisEnumIndex].shortName, null, null, null);

if (M03_Config.generateDdlCreateTable) {
M22_Class_Utilities.printChapterHeader("Enumeration \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\"", fileNo);
M00_FileWriter.printToFile(fileNo, "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (M21_Enum.g_enums.descriptors[thisEnumIndex].domainIndexId > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumId, M01_ACM.cosnEnumId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, null, null, null, null, null, null, null, null));
}

M22_Class.genAttrDeclsForEnum(thisEnumIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null);

M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressValuesInEnumTabs & M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, null, M01_Common.AttrCategory.eacVid, null, null, null, null));

M00_FileWriter.printToFile(fileNo, ")");
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceData.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData, thisOrgIndex, thisPoolIndex, null));
}
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceLong.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "LONG IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong, thisOrgIndex, thisPoolIndex, null));
}
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndex.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "INDEX IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex, null));
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressValuesInEnumTabs) {
M00_FileWriter.printToFile(fileNo, "VALUE COMPRESSION");
}
M00_FileWriter.printToFile(fileNo, "COMPRESS YES");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
if (M03_Config.generateDdlCreatePK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genPkName(M21_Enum.g_enums.descriptors[thisEnumIndex].enumName, M21_Enum.g_enums.descriptors[thisEnumIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, "PRIMARY KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
}

if (M03_Config.generateCommentOnTables & ! M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M22_Class.genDbObjComment("TABLE", qualTabName, "ACM-Enumeration \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\"", fileNo, thisOrgIndex, thisPoolIndex, null);
}

if (M03_Config.generateCommentOnColumns & ! M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "COMMENT ON " + qualTabName + " (");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumId, M01_ACM.cosnEnumId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null, "[LDM] Enumeration Value"));
// ### ELSE IVK ###
//     Print #fileNo, genAttrDecl(conEnumId, cosnEnumId, eavtDomainEnumId, .enumIndex, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Enumeration Value")
// ### ENDIF IVK ###

M22_Class.genAttrDeclsForEnum(thisEnumIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DdlOutputMode.edomComment);

M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressValuesInEnumTabs & M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, M01_Common.DdlOutputMode.edomComment, M01_Common.AttrCategory.eacVid, null, null, null, "[LDM] Record version tag"));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
String qualTabNameNl;
String qualLdmTabNameNl;
qualTabNameNl = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null);
qualLdmTabNameNl = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, true, null, null);

M96_DdlSummary.addTabToDdlSummary(qualTabNameNl, ddlType, M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated);
M78_DbMeta.registerQualTable(qualLdmTabNameNl, qualTabNameNl, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, thisOrgIndex, thisPoolIndex, ddlType, M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated, null, null, true, null);

if (M03_Config.generateDdlCreateTable) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, "(");
M22_Class_Utilities.printSectionHeader("Surrogate Key", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, null, null, null, null, null, null));
M22_Class_Utilities.printSectionHeader("Reference to ENUM table", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumRefId, M01_ACM.cosnEnumRefId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, null, null, null, null, null, null, null, null));
M22_Class_Utilities.printSectionHeader("Language of this LABEL", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conLanguageId, M01_ACM.cosnLanguageId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M01_Globals_IVK.g_enumIndexLanguage, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, null, null, null, null, null, null, null, null));
M22_Class_Utilities.printSectionHeader("LABEL", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumLabelText, M01_ACM.cosnEnumLabelText, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, null, null, null, null, null, null, null, null));

M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1", false, ddlType, null, null, M01_Common.AttrCategory.eacVid, null, null, null, null));
M00_FileWriter.printToFile(fileNo, ")");

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceData.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexNl, thisOrgIndex, thisPoolIndex, null));
}
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceLong.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "LONG IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong, thisOrgIndex, thisPoolIndex, null));
}
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndex.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "INDEX IN " + M04_Utilities.genTablespaceNameByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex, null));
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressValuesInEnumTabs & M03_Config.dbCompressValuesInNlsTabs) {
M00_FileWriter.printToFile(fileNo, "VALUE COMPRESSION");
}
M00_FileWriter.printToFile(fileNo, "COMPRESS YES");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.generateCommentOnTables & ! M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M22_Class.genDbObjComment("TABLE", qualTabNameNl, "ACM-Enumeration \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\" (NL)", fileNo, thisOrgIndex, thisPoolIndex, null);
}

if (M03_Config.generateCommentOnColumns & ! M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "COMMENT ON " + qualTabNameNl + " (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, M01_Common.DdlOutputMode.edomComment, null, null, null, null, "[LDM] Record (/Object) Identifier"));
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumRefId, M01_ACM.cosnEnumRefId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null, "[LDM] Reference to parent-Enumeration-table"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conLanguageId, M01_ACM.cosnLanguageId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M01_Globals_IVK.g_enumIndexLanguage, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null, "[LDM] Language identifier"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDecl(M01_ACM.conEnumLabelText, M01_ACM.cosnEnumLabelText, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL", null, ddlType, null, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null, "[LDM] NL-value of Enumeration literal"));
// ### ELSE IVK ###
//       Print #fileNo, genAttrDecl(conEnumRefId, cosnEnumRefId, eavtDomainEnumId, .enumIndex, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Reference to parent-Enumeration-table")
//       Print #fileNo, genAttrDecl(conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Language identifier")
//       Print #fileNo, genAttrDecl(conEnumLabelText, cosnEnumLabelText, eavtDomainEnumValue, .enumIndex, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] NL-value of Enumeration literal")
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1", false, ddlType, null, M01_Common.DdlOutputMode.edomComment, M01_Common.AttrCategory.eacVid, null, null, null, "[LDM] Record version tag"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.generateDdlCreatePK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genPkName(enumNameLbl, enumNameLblShort, ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.generateDdlCreateFK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genFkName(enumNameLbl, enumNameLblShort, "RID", ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anEnumRefId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualLdmTabNameNl, qualTabNameLdm, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, null, null, null);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForEnums & M03_Config.generateDdlCreateIndex) {
qualIndexName = M04_Utilities.genQualIndexName(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, enumNameLbl + "PAR", enumNameLblShort + "PAR", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anEnumRefId + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}//indexExcp
}

if (M03_Config.generateDdlCreateFK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genFkName(enumNameLbl, enumNameLblShort, "LID", ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anLanguageId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualLangTabName + " (" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualLdmTabNameNl, qualLangTabNameLdm, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, null, null, null);

if (M03_Config.generateDdlCreateIndex) {
if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForEnums & M03_Config.generateIndexOnFkForNLang) {
qualIndexName = M04_Utilities.genQualIndexName(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, enumNameLbl + "LAN", enumNameLblShort + "LAN", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}//indexExcp
}

qualIndexName = M04_Utilities.genQualObjName(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].shortName + "LBL_UK", M21_Enum.g_enums.descriptors[thisEnumIndex].shortName + "LBL_UK", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anEnumRefId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}//indexExcp
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M21_Enum.g_enums.descriptors[thisEnumIndex].noAlias) {
String qualEnumTabNameLdm;
qualEnumTabNameLdm = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].enumNameDb, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools, !(M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated), qualEnumTabNameLdm, qualTabName, M21_Enum.g_enums.descriptors[thisEnumIndex].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, false, false, false, false, "Enumeration \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\"", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     genAliasDdl .sectionIndex, .enumNameDb, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
//       qualEnumTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
//       "Enumeration """ & .sectionName & "." & .enumName & """"
// ### ENDIF IVK ###

qualEnumTabNameLdm = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, enumNameDbLbl, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools, !(M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated), qualEnumTabNameLdm, qualTabNameNl, M21_Enum.g_enums.descriptors[thisEnumIndex].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, false, false, false, false, "Enumeration-Label \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + enumNameDbLbl + "\"", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     genAliasDdl .sectionIndex, enumNameDbLbl, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
//       qualEnumTabNameLdm, qualTabNameNl, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
//       "Enumeration-Label """ & .sectionName & "." & enumNameDbLbl & """"
// ### ENDIF IVK ###
}

M21_Enum.genEnumCsv(thisEnumIndex, ddlType, thisOrgIndex, thisPoolIndex);

// enums may be a copy taken from g_enumss! make sure we update the original source!
M21_Enum.g_enums.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex].isLdmCsvExported = true;
M21_Enum.g_enums.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex].isCtoAliasCreated = true;

M21_Enum.g_enums.descriptors[thisEnumIndex].isLdmCsvExported = true;// safe is safe ;-)
M21_Enum.g_enums.descriptors[thisEnumIndex].isCtoAliasCreated = true;// safe is safe ;-)

// ### IF IVK ###
GenXmlExport:
if (M03_Config.generateXmlExportSupport &  (ddlType == M01_Common.DdlTypeId.edtLdm |  thisPoolIndex < 1 | poolSupportXmlExport)) {
M14_XMLExport.genXmlExportDdlForEnum(thisEnumIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType);
}

// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
// ### IF IVK ###
M00_FileWriter.closeFile(fileNoXmlV);
M00_FileWriter.closeFile(fileNoXmlF);
// ### ENDIF IVK ###

return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genEnumsDdl(Integer ddlType) {
int thisEnumIndex;
int thisOrgIndex;
int thisPoolIndex;

M21_Enum.resetEnumsCsvExported();

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
for (thisEnumIndex = 1; thisEnumIndex <= 1; thisEnumIndex += (1)) {
genEnumDdl(thisEnumIndex, M01_Common.DdlTypeId.edtLdm, null, null);
}

M21_Enum.resetEnumsCsvExported();
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisEnumIndex = 1; thisEnumIndex <= 1; thisEnumIndex += (1)) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs) {
genEnumDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtPdm, null, null);

// if there is some data pool which locally implements this enumeration, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genEnumDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}

} else {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools) {
genEnumDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null);

// if there is some data pool which locally implements this enumeration, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genEnumDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}

} else {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genEnumDdl(M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}
}

M21_Enum.resetEnumsCsvExported();
}
}


public static void genEnumAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);

int i;
for (int i = 1; i <= M21_Enum.g_enums.numDescriptors; i++) {
if ((!(M21_Enum.g_enums.descriptors[i].isPdmSpecific |  ddlType == M01_Common.DdlTypeId.edtPdm))) {
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].enumName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].shortName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M01_Globals.gc_acmEntityTypeKeyEnum + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].enumIdStr + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M21_Enum.g_enums.descriptors[i].i18nId + "\",");
M00_FileWriter.printToFile(fileNo, (M21_Enum.g_enums.descriptors[i].isCommonToOrgs ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M21_Enum.g_enums.descriptors[i].isCommonToPools ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M21_Enum.g_enums.descriptors[i].supportXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M21_Enum.g_enums.descriptors[i].useXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, ",0,0,0,0,0,,,0,0,0,0,0,0,0,0,0,0,0,,,,,");
// ### ELSE IVK ###
//       Print #fileNo, ",0,0,0,0,0,0,0,0,";
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, ",,,,,0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(12));
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


public static void dropEnumsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

int i;
int j;
int orgIndex;
int poolIndex;

// FIXME: why do we use '3' here?
final int maxSteps = 3;
String enumName;
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
for (i = 1; i <= 1; i += (1)) {
for (int j = 0; j <= maxSteps; j++) {
enumName = M21_Enum.g_enums.descriptors[i].enumName;
int k;
for (int k = 1; k <= 2; k++) {
M04_Utilities.killCsvFileWhereEver(M21_Enum.g_enums.descriptors[i].sectionIndex, enumName, M01_Globals.g_targetDir, j, onlyIfEmpty, null);
M04_Utilities.killCsvFileWhereEver(M21_Enum.g_enums.descriptors[i].sectionIndex, enumName + "_" + M01_LDM.tabPrefixNl + suffixText, M01_Globals.g_targetDir, j, onlyIfEmpty, null);

M04_Utilities.killCsvFileWhereEver(M21_Enum.g_enums.descriptors[i].sectionIndex, enumName, M01_Globals.g_targetDir, j, onlyIfEmpty, "PDM");
M04_Utilities.killCsvFileWhereEver(M21_Enum.g_enums.descriptors[i].sectionIndex, enumName + "_" + M01_LDM.tabPrefixNl + suffixText, M01_Globals.g_targetDir, j, onlyIfEmpty, "PDM");
enumName = M21_Enum.g_enums.descriptors[i].enumNameDb;
}
}
}
}


public static void genEnumCsv(int thisEnumIndex, Integer ddlType,  int thisOrgIndex,  int thisPoolIndex) {
//On Error GoTo ErrorExit 

String fileName;
String fileNameLabel;
if (M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName.compareTo("") == 0 |  M21_Enum.g_enums.descriptors[thisEnumIndex].enumName.compareTo("") == 0) {
goto NormalExit;
}

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].enumNameDb, (M21_Enum.g_enums.descriptors[thisEnumIndex].isEnumLang ? 0 : 1), (M21_Enum.g_enums.descriptors[thisEnumIndex].refersToPdm ? "PDM" : ""), ddlType, thisOrgIndex, thisPoolIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools, null);

M04_Utilities.assertDir(fileName);

if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
fileNameLabel = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex, M04_Utilities.genNlObjName(M21_Enum.g_enums.descriptors[thisEnumIndex].enumNameDb, null, null, null), 2, (M21_Enum.g_enums.descriptors[thisEnumIndex].refersToPdm ? "PDM" : ""), ddlType, thisOrgIndex, thisPoolIndex, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToOrgs, M21_Enum.g_enums.descriptors[thisEnumIndex].isCommonToPools, null);
}

int fileNoEnumCsv;
int fileNoEnumCsvOrg;
int thisFileNoCsv;
fileNoEnumCsv = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoEnumCsv, fileName, false);
int fileNoEnumLabelCsv;
int fileNoEnumLabelCsvOrg;
int thisFileNoLabelCsv;
if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
fileNoEnumLabelCsv = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoEnumLabelCsv, fileNameLabel, false);
}

String oidStr;
String valIdStr;
String extraCommas;
String thisComma;
int maxAttrs;
maxAttrs = (M21_Enum_Utilities.maxAttrsPerEnum < M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.numDescriptors ? M21_Enum_Utilities.maxAttrsPerEnum : M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.numDescriptors);
int i;
int j;
int k;
for (i = M00_Helper.lBound(M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals); i <= 1; i += (1)) {
for (int k = 0; k <= (thisOrgIndex <= 0 &  M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].isOrgSpecific & ddlType == M01_Common.DdlTypeId.edtPdm ? M71_Org.g_orgs.numDescriptors : 0); k++) {
if (k == 0 &  M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].isOrgSpecific) {
goto NextK;
}

if (k > 0 &  thisOrgIndex <= 0 & M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].isOrgSpecific & ddlType == M01_Common.DdlTypeId.edtPdm) {
goto NextK;
} else {
oidStr = String.valueOf(M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].oid);
valIdStr = String.valueOf(M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].id);

thisFileNoCsv = fileNoEnumCsv;
thisFileNoLabelCsv = fileNoEnumLabelCsv;
}

String thisValue;
extraCommas = "";
thisComma = (i == M00_Helper.lBound(M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals) &  M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType == M01_Common.typeId.etNone ? "" : ",");

for (int j = 1; j <= maxAttrs; j++) {
thisValue = M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].attrStrings[j];
if (thisValue.compareTo("") == 0) {
thisValue = M24_Attribute.g_attributes.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[j].refIndex].defaultValue;
if (thisValue.substring(0, 1) == "'") {
thisValue = thisValue.substring(thisValue.length() - 1 - thisValue.length() - 1);
}
if (thisValue.substring(thisValue.length() - 1 - 1) == "'") {
thisValue = thisValue.substring(0, thisValue.length() - 1);
}
}

if (!(thisValue.compareTo("") == 0)) {
switch (M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[j].refIndex].domainIndex].dataType) {
case M01_Common.typeId.etChar: {case M01_Common.typeId.etVarchar: {extraCommas = extraCommas + thisComma + "\"" + thisValue + "\"";
break;
}case M01_Common.typeId.etDate: {case M01_Common.typeId.etTime: {case M01_Common.typeId.etTimestamp: {if (M00_Helper.inStr(1, thisValue, "'") > 0 != 0) {
thisValue = thisValue.substring(thisValue.length() - 1 - thisValue.length() - M00_Helper.inStr(1, thisValue, "'"));
}
if (M00_Helper.inStr(1, thisValue, "'") > 0 != 0) {
thisValue = "'" + thisValue.substring(0, M00_Helper.inStr(1, thisValue, "'"));
}
if (thisValue.toUpperCase() == "CURRENT TIMESTAMP") {
thisValue = new SimpleDateFormat("yyyy-MM-DD-00.00.00.000000").format(new Date());
}
extraCommas = extraCommas + thisComma + thisValue;
break;
}case M01_Common.typeId.etDecimal: {case M01_Common.typeId.etDouble: {case M01_Common.typeId.etFloat: {extraCommas = extraCommas + thisComma + M00_Helper.replace(thisValue, ",", ".");
break;
}default: {extraCommas = extraCommas + thisComma + thisValue;
}}
} else {
extraCommas = extraCommas + thisComma;
}
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
M00_FileWriter.printToFile(thisFileNoLabelCsv, oidStr + "," + valIdStr + "," + M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].languageId + ",\"" + M00_Helper.replace(M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].valueString, "\"", "\"\"") + "\",1");
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].languageId == M01_Globals_IVK.gc_langIdGerman) {
M00_FileWriter.printToFile(thisFileNoCsv, (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType == M01_Common.typeId.etNone ? "" : valIdStr) + extraCommas + ",1");
}

if (M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[i].isOrgSpecific) {
M00_FileWriter.closeFile(fileNoEnumCsvOrg);
if (M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType != M01_Common.typeId.etNone) {
M00_FileWriter.closeFile(fileNoEnumLabelCsvOrg);
}
}
NextK:
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoEnumCsv);
M00_FileWriter.closeFile(fileNoEnumCsvOrg);
M00_FileWriter.closeFile(fileNoEnumLabelCsv);
M00_FileWriter.closeFile(fileNoEnumLabelCsvOrg);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void resetEnumsCsvExported() {
int i;
for (i = 1; i <= 1; i += (1)) {
M21_Enum.g_enums.descriptors[i].isLdmCsvExported = false;
// ### IF IVK ###
M21_Enum.g_enums.descriptors[i].isXsdExported = false;
// ### ENDIF IVK ###
M21_Enum.g_enums.descriptors[i].isCtoAliasCreated = false;
}
}


public static void getEnums() {
if ((M21_Enum.g_enums.numDescriptors == 0)) {
readSheet();

// make sure we've read the MPCs and Attributes
M71_Org.getOrgs();
M24_Attribute.getAttributes();

int enumLangIndex;

enumLangIndex = M21_Enum_Utilities.getEnumLangIndex();

int i;
int j;
// create enum PdmOrganization
M24_Attribute.addAttribute(M01_ACM_IVK.exnPdmOrganization, M01_ACM_IVK.enPdmOrganization, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M01_ACM.conOrgOid, M01_ACM.cosnOrgOid, M01_ACM.dxnOid, M01_ACM.dnOid, null, null, null, null, null, null, null, null, null);
// ### IF IVK ###
M24_Attribute.addAttribute(M01_ACM_IVK.exnPdmOrganization, M01_ACM_IVK.enPdmOrganization, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M01_ACM.conPdmSequenceSchemaName, M01_ACM.cosnPdmSequenceSchemaName, M01_ACM.snDbMeta, M01_ACM.dnDbSchemaName, null, null, null, null, null, null, null, null, null);
// ### ENDIF IVK ###

M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].sectionName = M01_ACM_IVK.exnPdmOrganization;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumName = M01_ACM_IVK.enPdmOrganization;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isEnumLang = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].shortName = M01_ACM_IVK.esnPdmOrganization;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumId = 999;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainSection = M01_ACM.snMeta;
// FIXME: get rid of hard-coding
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainName = M01_ACM.dnEnumId;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].maxLength = 20;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToOrgs = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToPools = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].notAcmRelated = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].noAlias = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isLrtSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isPdmSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].refersToPdm = true;

if (enumLangIndex > 0) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceData = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceData;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceIndex = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceIndex;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceLong = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceLong;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceNl = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceNl;
}

M21_Enum_Utilities.initEnumVals(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values);
String orgOidStr;
for (int i = 1; i <= M71_Org.g_orgs.numDescriptors; i++) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].id = M71_Org.g_orgs.descriptors[i].id;
if (M71_Org.g_orgs.descriptors[i].isTemplate) {
orgOidStr = "" + M04_Utilities.genTemplateParamWrapper(M71_Org_Utilities.pullOrgOidByIndex(i), true);

M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].isOrgSpecific = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M71_Org_Utilities.pullOrgOidByIndex(i);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(1)] = "" + orgOidStr;
} else {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].isOrgSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M04_Utilities.pullOid;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(1)] = "" + M71_Org.g_orgs.descriptors[i].oid;
}
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].languageId = M01_Globals_IVK.gc_langIdEnglish;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].valueString = M71_Org.g_orgs.descriptors[i].name;
// ### IF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(2)] = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, M01_Common.DdlTypeId.edtPdm, i, null);

for (j = 3; j <= 1; j += (1)) {
// ### ELSE IVK ###
//         For j = 2 To maxAttrsPerEnum Step 1
// ### ENDIF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(j)] = "";
}

// We need to have german values - otherwise the enum values do not show up in the CSV file
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].id = M71_Org.g_orgs.descriptors[i].id;
if (M71_Org.g_orgs.descriptors[i].isTemplate) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].isOrgSpecific = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M71_Org_Utilities.pullOrgOidByIndex(i);
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(1)] = orgOidStr;
} else {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].isOrgSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M04_Utilities.pullOid;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(1)] = "" + M71_Org.g_orgs.descriptors[i].oid;
}
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].languageId = M01_Globals_IVK.gc_langIdGerman;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].valueString = M71_Org.g_orgs.descriptors[i].name;
// ### IF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(2)] = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, M01_Common.DdlTypeId.edtPdm, i, null);
for (j = 3; j <= 1; j += (1)) {
// ### ELSE IVK ###
//         For j = 2 To maxAttrsPerEnum Step 1
// ### ENDIF IVK ###
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].attrStrings[(j)] = "";
}
}

// make sure we've read the DataPools
M72_DataPool.getDataPools();

// create enum PdmDataPoolType
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].sectionName = M01_ACM_IVK.exnPdmDataPoolType;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumName = M01_ACM_IVK.enPdmDataPoolType;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isEnumLang = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].shortName = M01_ACM_IVK.esnPdmDataPoolType;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].enumId = 998;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainSection = M01_ACM.snMeta;
// FIXME: get rid of hard-coding
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].idDomainName = M01_ACM.dnEnumId;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].maxLength = 30;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToOrgs = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isCommonToPools = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].notAcmRelated = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].noAlias = true;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isLrtSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].isPdmSpecific = false;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].refersToPdm = true;

if (enumLangIndex > 0) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceData = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceData;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceIndex = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceIndex;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceLong = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceLong;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].tabSpaceNl = M21_Enum.g_enums.descriptors[enumLangIndex].tabSpaceNl;
}

M21_Enum_Utilities.initEnumVals(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values);

for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].supportAcm) {
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].id = M72_DataPool.g_pools.descriptors[i].id;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M04_Utilities.pullOid;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].languageId = M01_Globals_IVK.gc_langIdEnglish;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].valueString = M72_DataPool.g_pools.descriptors[i].name;
// We need to have german values - otherwise the enum values do not show up in the CSV file
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].id = M72_DataPool.g_pools.descriptors[i].id;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].oid = M04_Utilities.pullOid;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].languageId = M01_Globals_IVK.gc_langIdGerman;
M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values.vals[M21_Enum_Utilities.allocEnumValIndex(M21_Enum.g_enums.descriptors[M21_Enum_Utilities.allocEnumDescriptorIndex(M21_Enum.g_enums)].values)].valueString = M72_DataPool.g_pools.descriptors[i].name;
}
}
}
}


public static void resetEnums() {
M21_Enum.g_enums.numDescriptors = 0;
}


public static String getQualTabNameLanguageEnum( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexLanguage, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);
return returnValue;
}


public static Integer getEnumIndexByName(String sectionName, String enumName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;
M21_Enum.getEnums();

for (i = 1; i <= 1; i += (1)) {
if (M21_Enum.g_enums.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase() &  M21_Enum.g_enums.descriptors[i].enumName.toUpperCase() == enumName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
M04_Utilities.logMsg("unable to identify enumeration '" + sectionName + "." + enumName + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtLdm, null, null);
}
return returnValue;
}


public static Integer getEnumIndexByI18nId(String i18nId) {
Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M21_Enum.g_enums.descriptors[i].i18nId.toUpperCase() == i18nId.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Boolean isEnum(String sectionName, String enumName, Integer enumIndexW) {
int enumIndex; 
if (enumIndexW == null) {
enumIndex = -1;
} else {
enumIndex = enumIndexW;
}

Boolean returnValue;
returnValue = false;

enumIndex = M21_Enum.getEnumIndexByName(sectionName, enumName, true);
if ((enumIndex > 0)) {
returnValue = true;
}
return returnValue;
}


public static void evalEnums() {
int thisEnumIndex;
int thisAttrIndex;

for (thisEnumIndex = 1; thisEnumIndex <= 1; thisEnumIndex += (1)) {
M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex = M20_Section.getSectionIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName, null);
M21_Enum.g_enums.descriptors[thisEnumIndex].sectionShortName = "";
if (M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex > 0) {
M21_Enum.g_enums.descriptors[thisEnumIndex].sectionShortName = M20_Section.g_sections.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex].shortName;
}
}

for (thisEnumIndex = 1; thisEnumIndex <= 1; thisEnumIndex += (1)) {
M21_Enum.g_enums.descriptors[thisEnumIndex].enumIdStr = M21_Enum_Utilities.getEnumIdByIndex(thisEnumIndex);

M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex = thisEnumIndex;
M21_Enum.g_enums.descriptors[thisEnumIndex].enumNameDb = M04_Utilities.genEnumObjName(M21_Enum.g_enums.descriptors[thisEnumIndex].enumName, null);
M21_Enum.g_enums.descriptors[thisEnumIndex].domainIndexId = M25_Domain.getDomainIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].idDomainSection, M21_Enum.g_enums.descriptors[thisEnumIndex].idDomainName, null);
M21_Enum.g_enums.descriptors[thisEnumIndex].idDataType = M25_Domain.g_domains.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].domainIndexId].dataType;
M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.numDescriptors = 0;
M21_Enum.g_enums.descriptors[thisEnumIndex].refersToPdm = M21_Enum.g_enums.descriptors[thisEnumIndex].refersToPdm |  M21_Enum.g_enums.descriptors[thisEnumIndex].isPdmSpecific;

for (thisAttrIndex = 1; thisAttrIndex <= 1; thisAttrIndex += (1)) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName.toUpperCase() == M24_Attribute.g_attributes.descriptors[thisAttrIndex].sectionName.toUpperCase() &  M21_Enum.g_enums.descriptors[thisEnumIndex].enumName.toUpperCase() == M24_Attribute.g_attributes.descriptors[thisAttrIndex].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[thisAttrIndex].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {

M24_Attribute.g_attributes.descriptors[thisAttrIndex].acmEntityIndex = thisEnumIndex;
M24_Attribute.g_attributes.descriptors[thisAttrIndex].isPdmSpecific = M24_Attribute.g_attributes.descriptors[thisAttrIndex].isPdmSpecific |  M21_Enum.g_enums.descriptors[thisEnumIndex].isPdmSpecific;
if (!(M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated)) {
M24_Attribute.g_attributes.descriptors[thisAttrIndex].isNotAcmRelated = false;
}

if (M21_Enum.isEnum(M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainSection, M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainName, null)) {
M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtEnum;
// ### IF IVK ###
} else if (M26_Type.isType(M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainSection, M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainName, null)) {
M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtType;
// ### ENDIF IVK ###
} else {
M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute;
}
M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].attrRefs)].refIndex = thisAttrIndex;
}
}
for (int thisAttrIndex = 1; thisAttrIndex <= M21_Enum.g_enums.descriptors[thisEnumIndex].values.numVals; thisAttrIndex++) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[thisAttrIndex].valueString.length() > M21_Enum.g_enums.descriptors[thisEnumIndex].maxLength) {
M04_Utilities.logMsg("Enumeration\"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + " has maximum literal length " + M21_Enum.g_enums.descriptors[thisEnumIndex].maxLength + " but literal \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[thisAttrIndex].valueString + "\" has length " + M21_Enum.g_enums.descriptors[thisEnumIndex].values.vals[thisAttrIndex].valueString.length(), M01_Common.LogLevel.ellError, null, null, null);
}
}

// ### IF IVK ###
// determine whether class supports XML-export
M21_Enum.g_enums.descriptors[thisEnumIndex].supportXmlExport = !(M21_Enum.g_enums.descriptors[thisEnumIndex].noXmlExport);

// ### ENDIF IVK ###
// determine TableSpaces
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData = (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceData.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceData) : -1);
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex = (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndex.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndex) : -1);
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong = (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceLong.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceLong) : -1);
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexNl = (!(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceNl.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceNl) : -1);

if (M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData > 0) {
if (M73_TableSpace.g_tableSpaces.descriptors[M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData].category == M73_TableSpace_Utilities.TabSpaceCategory.tscSms) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex > 0 &  M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex != M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData) {
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexIndex = M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData;
M04_Utilities.logMsg("index table space \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndex + "\" for enum \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
if (M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong > 0 &  M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong != M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData) {
M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexLong = M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceIndexData;
M04_Utilities.logMsg("long table space \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].tabSpaceLong + "\" for enum \"" + M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName + "." + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
}
}

int domainIndexThisEnum;
domainIndexThisEnum = M25_Domain_Utilities.allocDomainDescriptorIndex(M25_Domain.g_domains);
M25_Domain.g_domains.descriptors[domainIndexThisEnum].sectionName = M21_Enum.g_enums.descriptors[thisEnumIndex].sectionName;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].domainName = "EnumVal" + M21_Enum.g_enums.descriptors[thisEnumIndex].enumName;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].dataType = M01_Common.typeId.etVarchar;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].minLength = "";
M25_Domain.g_domains.descriptors[domainIndexThisEnum].maxLength = M21_Enum.g_enums.descriptors[thisEnumIndex].maxLength;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].scale = 0;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].minValue = "";
M25_Domain.g_domains.descriptors[domainIndexThisEnum].maxValue = "";
M25_Domain.g_domains.descriptors[domainIndexThisEnum].valueList = "";
M25_Domain.g_domains.descriptors[domainIndexThisEnum].constraint = "";
M25_Domain.g_domains.descriptors[domainIndexThisEnum].notLogged = false;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].notCompact = false;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].supportUnicode = false;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].unicodeExpansionFactor = 1;
M25_Domain.g_domains.descriptors[domainIndexThisEnum].isGenerated = true;

M25_Domain.g_domains.descriptors[domainIndexThisEnum].domainIndex = domainIndexThisEnum;

M21_Enum.g_enums.descriptors[thisEnumIndex].domainIndexValue = domainIndexThisEnum;
}
}


}