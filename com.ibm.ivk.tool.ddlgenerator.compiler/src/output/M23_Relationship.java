package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M23_Relationship {




private static final int colEntryFilter = 1;
private static final int colSectionName = 2;
private static final int colRelName = colSectionName + 1;
private static final int colAggHeadSection = colRelName + 1;
private static final int colAggHeadName = colAggHeadSection + 1;
private static final int colNameShort = colAggHeadName + 1;
// ### IF IVK ###
private static final int colLrtClassification = colNameShort + 1;
private static final int colLrtActivationStatusMode = colLrtClassification + 1;
private static final int colIgnoreForChangeLog = colLrtActivationStatusMode + 1;
// ### ELSE IVK ###
//Private Const colIgnoreForChangeLog = colNameShort + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colMapsToACMAttribute = colIgnoreForChangeLog + 1;
private static final int colAcmMappingIsInstantiated = colMapsToACMAttribute + 1;
private static final int colNavPathToDivision = colAcmMappingIsInstantiated + 1;
private static final int colReuseName = colNavPathToDivision + 1;
// ### ELSE IVK ###
//Private Const colReuseName = colIgnoreForChangeLog + 1
// ### ENDIF IVK ###
private static final int colReuseShortName = colReuseName + 1;
// ### IF IVK ###
private static final int colRefersToClAttributes = colReuseShortName + 1;
private static final int colIsCommonToOrgs = colRefersToClAttributes + 1;
// ### ELSE IVK ###
//Private Const colIsCommonToOrgs = colReuseShortName + 1
// ### ENDIF IVK ###
private static final int colSpecificToOrg = colIsCommonToOrgs + 1;
private static final int colFkReferenceOrg = colSpecificToOrg + 1;
private static final int colIsCommonToPools = colFkReferenceOrg + 1;
private static final int colSpecificToPool = colIsCommonToPools + 1;
private static final int colFkReferencePool = colSpecificToPool + 1;
private static final int colNoIndexesInPool = colFkReferencePool + 1;
private static final int colUseValueCompression = colNoIndexesInPool + 1;
private static final int colUseSurrogateKey = colUseValueCompression + 1;
private static final int colUseVersionTag = colUseSurrogateKey + 1;
private static final int colRelId = colUseVersionTag + 1;
// ### IF IVK ###
private static final int colNoRangePartitioning = colRelId + 1;
private static final int colNotAcmRelated = colNoRangePartitioning + 1;
// ### ELSE IVK ###
//Private Const colNotAcmRelated = colRelId + 1
// ### ENDIF IVK ###
private static final int colNoAlias = colNotAcmRelated + 1;
// ### IF IVK ###
private static final int colNoXmlExport = colNoAlias + 1;
private static final int colUseXmlExport = colNoXmlExport + 1;
private static final int colIsLrtSpecific = colUseXmlExport + 1;
// ### ELSE IVK ###
//Private Const colIsLrtSpecific = colNoAlias + 1
// ### ENDIF IVK ###
private static final int colIsPdmSpecific = colIsLrtSpecific + 1;
// ### IF IVK ###
private static final int colIncludeInPdmExportSeqNo = colIsPdmSpecific + 1;
private static final int colIsVolatile = colIncludeInPdmExportSeqNo + 1;
// ### ELSE IVK ###
//Private Const colIsVolatile = colIsPdmSpecific + 1
// ### ENDIF IVK ###
private static final int colIsNotEnforced = colIsVolatile + 1;
private static final int colIsNl = colIsNotEnforced + 1;
private static final int colIncludeInPkIndex = colIsNl + 1;
private static final int colLeftSection = colIncludeInPkIndex + 1;
private static final int colLeftClass = colLeftSection + 1;
private static final int colLeftTargetType = colLeftClass + 1;
private static final int colLRName = colLeftTargetType + 1;
private static final int colLRNameShort = colLRName + 1;
private static final int colLRLdmName = colLRNameShort + 1;
private static final int colMinLeftCardinality = colLRLdmName + 1;
private static final int colMaxLeftCardinality = colMinLeftCardinality + 1;
private static final int colIsIdentifyingLeft = colMaxLeftCardinality + 1;
private static final int colLRFkMaintenanceMode = colIsIdentifyingLeft + 1;
private static final int colUseIndexOnLeftFk = colLRFkMaintenanceMode + 1;
// ### IF IVK ###
private static final int colLeftDependentAttribute = colUseIndexOnLeftFk + 1;
private static final int colRightSection = colLeftDependentAttribute + 1;
// ### ELSE IVK ###
//Private Const colRightSection = colUseIndexOnLeftFk + 1
// ### ENDIF IVK ###
private static final int colRightClass = colRightSection + 1;
private static final int colRightTargetType = colRightClass + 1;
private static final int colRLName = colRightTargetType + 1;
private static final int colRLNameShort = colRLName + 1;
private static final int colRLLdmName = colRLNameShort + 1;
private static final int colMinRightCardinality = colRLLdmName + 1;
private static final int colMaxRightCardinality = colMinRightCardinality + 1;
private static final int colIsIdentifyingRight = colMaxRightCardinality + 1;
private static final int colRLFkMaintenanceMode = colIsIdentifyingRight + 1;
private static final int colUseIndexOnRightFk = colRLFkMaintenanceMode + 1;
// ### IF IVK ###
//Private Const colIsRightRefToTimeVarying = colUseIndexOnRightFk + 1
private static final int colRightDependentAttribute = colUseIndexOnRightFk + 1;
private static final int colIsNationalizable = colRightDependentAttribute + 1;
private static final int colIsPsForming = colIsNationalizable + 1;
private static final int colSupportExtendedPsCopy = colIsPsForming + 1;
private static final int colLogLastChange = colSupportExtendedPsCopy + 1;
// ### ELSE IVK ###
//Private Const colLogLastChange = colUseIndexOnRightFk + 1
// ### ENDIF IVK ###
private static final int colLogLastChangeInView = colLogLastChange + 1;
private static final int colLogLastChangeAutoMaint = colLogLastChangeInView + 1;
private static final int colIsUserTransactional = colLogLastChangeAutoMaint + 1;
private static final int colUseMqtToImplementLrt = colIsUserTransactional + 1;
// ### IF IVK ###
private static final int colNoTransferToProduction = colUseMqtToImplementLrt + 1;
private static final int colNoFto = colNoTransferToProduction + 1;
private static final int colFtoSingleObjProcessing = colNoFto + 1;
private static final int colTabSpaceData = colFtoSingleObjProcessing + 1;
// ### ELSE IVK ###
//Private Const colTabSpaceData = colUseMqtToImplementLrt + 1
// ### ENDIF IVK ###
private static final int colTabSpaceLong = colTabSpaceData + 1;
private static final int colTabSpaceNl = colTabSpaceLong + 1;
private static final int colTabSpaceIndex = colTabSpaceNl + 1;
private static final int colIsTv = colTabSpaceIndex + 1;
private static final int colI18nId = colIsTv + 1;

public static final int colRelI18nId = colI18nId;

private static final int firstRow = 4;

private static final String sheetName = "Rel";

// ### IF IVK ###
private static final int processingStepPsCopy = 1;
private static final int processingStepPsCopy2 = 2;
private static final int processingStepExpCopy = 6;
private static final int processingStepSetProd = 5;
private static final int processingStepFto = 3;
private static final int processingStepAllowedCountries = 4;
// ### ENDIF IVK ###
private static final int processingStep = 3;
private static final int processingStepLrt = 4;
private static final int processingStepAcmCsv = 2;

public static M23_Relationship_Utilities.RelationshipDescriptors g_relationships;
// ### IF IVK ###

private static final int maxAlCountryListLen = 1024;
// ### ENDIF IVK ###


public static void genAttrMapping(M24_Attribute_Utilities.AttributeMappingForCl[] mapping, String str, Boolean isTvW, Integer attrIndexW) {
boolean isTv; 
if (isTvW == null) {
isTv = false;
} else {
isTv = isTvW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

String[] list;
String[] elems;
int prio;
prio = 0;
list = "".split(",");
list = str.split(",");

if (M00_Helper.uBound(list) >= 0) {
mapping =  new M24_Attribute_Utilities.AttributeMappingForCl[M00_Helper.uBound(list)];
}

int i;
for (int i = M00_Helper.lBound(list); i <= M00_Helper.uBound(list); i++) {
list[(i)] = list[i].trim();

if (M00_Helper.inStr(list[i], ":") != 0) {
elems = list[i].split(":");
prio = new Double(elems[0]).intValue();
list[(i)] = elems[1];
}

elems = list[i].split("/");
if (M00_Helper.uBound(elems) == 1) {
mapping[i].prio = prio;
mapping[i].mapFrom = elems[0];
mapping[i].mapTo = elems[1];
mapping[i].isTv = isTv;
mapping[i].attrIndex = attrIndex;
}
}
}


public static void addAttrMapping(M24_Attribute_Utilities.AttributeMappingForCl[] mapping, String mapFrom, String mapTo, Boolean isTvW, Integer attrIndexW) {
boolean isTv; 
if (isTvW == null) {
isTv = false;
} else {
isTv = isTvW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

if (M04_Utilities.arrayIsNull(mapping)) {
mapping =  new M24_Attribute_Utilities.AttributeMappingForCl[0];
} else {
M24_Attribute_Utilities.AttributeMappingForCl[] mappingBackup = mapping;
mapping =  new M24_Attribute_Utilities.AttributeMappingForCl[M00_Helper.uBound(mapping) + 1];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.AttributeMappingForCl value : mappingBackup) {
mapping[indexCounter] = value;
indexCounter++;
}
}

mapping[M00_Helper.uBound(mapping)].mapFrom = mapFrom;
mapping[M00_Helper.uBound(mapping)].mapTo = mapTo;
mapping[M00_Helper.uBound(mapping)].isTv = isTv;
mapping[M00_Helper.uBound(mapping)].attrIndex = attrIndex;
}


private static void readSheet() {
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

String lastSection;
String clMapping;
while (M00_Excel.getCell(thisSheet, thisRow, colRelName).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSectionName).getStringCellValue();
if ((M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName + "" == "")) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName = lastSection;
}

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relName = M00_Excel.getCell(thisSheet, thisRow, colRelName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].aggHeadSection = M00_Excel.getCell(thisSheet, thisRow, colAggHeadSection).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].aggHeadName = M00_Excel.getCell(thisSheet, thisRow, colAggHeadName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].shortName = M00_Excel.getCell(thisSheet, thisRow, colNameShort).getStringCellValue().trim();
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrtClassification = M00_Excel.getCell(thisSheet, thisRow, colLrtClassification).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrtActivationStatusMode = M00_Excel.getCell(thisSheet, thisRow, colLrtActivationStatusMode).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].navPathStrToDivision = M00_Excel.getCell(thisSheet, thisRow, colNavPathToDivision).getStringCellValue().trim();
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].ignoreForChangelog = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIgnoreForChangeLog).getStringCellValue(), null);

// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].virtuallyMapsTo.description = M00_Excel.getCell(thisSheet, thisRow, colMapsToACMAttribute).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isVirtual = (!(M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].virtuallyMapsTo.description.compareTo("") == 0));
if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isVirtual) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].virtuallyMapsTo.isInstantiated = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colAcmMappingIsInstantiated).getStringCellValue(), null);
}

// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].reuseName = M00_Excel.getCell(thisSheet, thisRow, colReuseName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].reuseShortName = M00_Excel.getCell(thisSheet, thisRow, colReuseShortName).getStringCellValue().trim();
// ### IF IVK ###
clMapping = M00_Excel.getCell(thisSheet, thisRow, colRefersToClAttributes).getStringCellValue().trim();
if (!(clMapping.compareTo("") == 0)) {
M23_Relationship.genAttrMapping(M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].refersToClAttribute, clMapping, null, null);
}
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].leftClassSectionName = M00_Excel.getCell(thisSheet, thisRow, colLeftSection).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].leftClassName = M00_Excel.getCell(thisSheet, thisRow, colLeftClass).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].leftTargetType = M23_Relationship_Utilities.getRelRefTargetType(M00_Excel.getCell(thisSheet, thisRow, colLeftTargetType).getStringCellValue());
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrRelName = M00_Excel.getCell(thisSheet, thisRow, colLRName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrShortRelName = M00_Excel.getCell(thisSheet, thisRow, colLRNameShort).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrLdmRelName = M00_Excel.getCell(thisSheet, thisRow, colLRLdmName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useLrLdmRelName = !(M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrLdmRelName.compareTo("") == 0);
if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrLdmRelName.compareTo("-") == 0) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrLdmRelName = "";
}
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].fkReferenceOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colFkReferenceOrg).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isCommonToPools = M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].specificToPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].fkReferencePoolId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colFkReferencePool).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noIndexesInPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colNoIndexesInPool).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useValueCompression = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseValueCompression).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useSurrogateKey = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseSurrogateKey).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useVersiontag = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseVersionTag).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colRelId).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].notAcmRelated = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotAcmRelated).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noAlias = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoAlias).getStringCellValue(), null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noRangePartitioning = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoRangePartitioning).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoXmlExport).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseXmlExport).getStringCellValue(), null);
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isLrtSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsLrtSpecific).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].includeInPdmExportSeqNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colIncludeInPdmExportSeqNo).getStringCellValue(), -1);
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isVolatile = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsVolatile).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isNotEnforced = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNotEnforced).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isNl = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNl).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].includeInPkIndex = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIncludeInPkIndex).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minLeftCardinality = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colMinLeftCardinality).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxLeftCardinality = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colMaxLeftCardinality).getStringCellValue(), null);
if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minLeftCardinality == -1) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minLeftCardinality = (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxLeftCardinality == 1 ? 1 : 0);
}

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingLeft = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsIdentifyingLeft).getStringCellValue(), null);

if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingLeft &  M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxLeftCardinality != 1) {
M04_Utilities.logMsg("unable to implement ACM-related relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relName + " as 'left-identifying' since 'max left cardinality <> 1' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingLeft = false;
} else if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight &  M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minRightCardinality != 1) {
M04_Utilities.logMsg("unable to implement ACM-related relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relName + " as 'left-identifying' since 'min left cardinality <> 1' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingLeft = false;
}

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].lrFkMaintenanceMode = M04_Utilities.getFkMaintenanceMode(M00_Excel.getCell(thisSheet, thisRow, colLRFkMaintenanceMode).getStringCellValue());

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useIndexOnLeftFk = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseIndexOnLeftFk).getStringCellValue(), null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].leftDependentAttribute = M00_Excel.getCell(thisSheet, thisRow, colLeftDependentAttribute).getStringCellValue().trim();
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rightClassSectionName = M00_Excel.getCell(thisSheet, thisRow, colRightSection).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rightClassName = M00_Excel.getCell(thisSheet, thisRow, colRightClass).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rightTargetType = M23_Relationship_Utilities.getRelRefTargetType(M00_Excel.getCell(thisSheet, thisRow, colRightTargetType).getStringCellValue());
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlRelName = M00_Excel.getCell(thisSheet, thisRow, colRLName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlShortRelName = M00_Excel.getCell(thisSheet, thisRow, colRLNameShort).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlLdmRelName = M00_Excel.getCell(thisSheet, thisRow, colRLLdmName).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useRlLdmRelName = !(M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlLdmRelName.compareTo("") == 0);
if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlLdmRelName.compareTo("-") == 0) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlLdmRelName = "";
}
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minRightCardinality = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colMinRightCardinality).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxRightCardinality = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colMaxRightCardinality).getStringCellValue(), null);
if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minRightCardinality == -1) {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minRightCardinality = (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxRightCardinality == 1 ? 1 : 0);
}
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsIdentifyingRight).getStringCellValue(), null);

if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight &  M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].maxRightCardinality != 1) {
M04_Utilities.logMsg("unable to implement ACM-related relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relName + " as 'right-identifying' since 'max right cardinality <> 1' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight = false;
} else if (M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight &  M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].minRightCardinality != 1) {
M04_Utilities.logMsg("unable to implement ACM-related relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].relName + " as 'right-identifying' since 'min right cardinality <> 1' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isIdentifyingRight = false;
}

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rlFkMaintenanceMode = M04_Utilities.getFkMaintenanceMode(M00_Excel.getCell(thisSheet, thisRow, colRLFkMaintenanceMode).getStringCellValue());

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useIndexOnRightFk = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseIndexOnRightFk).getStringCellValue(), null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].rightDependentAttribute = M00_Excel.getCell(thisSheet, thisRow, colRightDependentAttribute).getStringCellValue().trim();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isNationalizable = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNationalizable).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isPsForming = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPsForming).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].supportExtendedPsCopy = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportExtendedPsCopy).getStringCellValue(), null);
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].logLastChange = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChange).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].logLastChangeInView = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChangeInView).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].logLastChangeAutoMaint = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChangeAutoMaint).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isUserTransactional = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsUserTransactional).getStringCellValue(), null);
if (M00_Excel.getCell(thisSheet, thisRow, colIsUserTransactional).getStringCellValue().trim().toUpperCase() == "M") {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isLrtMeta = true;
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isUserTransactional = false;
} else {
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isUserTransactional = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsUserTransactional).getStringCellValue(), null);
}
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].useMqtToImplementLrt = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseMqtToImplementLrt).getStringCellValue(), null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noTransferToProduction = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoTransferToProduction).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].noFto = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoFto).getStringCellValue(), null);
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].ftoSingleObjProcessing = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colFtoSingleObjProcessing).getStringCellValue(), null);
// ### ENDIF IVK ###

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].tabSpaceData = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceData).getStringCellValue();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].tabSpaceLong = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceLong).getStringCellValue();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].tabSpaceNl = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceNl).getStringCellValue();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].tabSpaceIndex = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceIndex).getStringCellValue();
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isTimeVarying = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsTv).getStringCellValue(), null);

// ### IF IVK ###

M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].defaultStatus = M86_SetProductive.statusReadyForActivation;
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isSubjectToArchiving = false;
M23_Relationship.g_relationships.descriptors[M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships)].isMdsExpressionRel = false;
// ### ENDIF IVK ###

NextRow:
thisRow = thisRow + 1;
}
}


public static void resetRelationshipsCsvExported() {
int i;
for (i = 1; i <= 1; i += (1)) {
M23_Relationship.g_relationships.descriptors[i].isLdmCsvExported = false;
M23_Relationship.g_relationships.descriptors[i].isLdmLrtCsvExported = false;
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].isXsdExported = false;
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[i].isCtoAliasCreated = false;
}
}


public static void getRelationships() {
if (M23_Relationship.g_relationships.numDescriptors == 0) {
readSheet();
}
}


public static void resetRelationships() {
M23_Relationship.g_relationships.numDescriptors = 0;
}


public static Integer getMaxRelIdBySection(String sectionName) {
Integer returnValue;
int maxRelId;
maxRelId = 0;

int i;
for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
if (M23_Relationship.g_relationships.descriptors[i].sectionName.compareTo(sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].relId > maxRelId) {
maxRelId = M23_Relationship.g_relationships.descriptors[i].relId;
}
}

returnValue = maxRelId;
return returnValue;
}


public static void setRelationshipReusedRelIndex(int relIndex, int reusedRelIndex) {
if (relIndex > 0) {
while (M23_Relationship.g_relationships.descriptors[reusedRelIndex].reusedRelIndex > 0) {
reusedRelIndex = M23_Relationship.g_relationships.descriptors[reusedRelIndex].reusedRelIndex;
}
M23_Relationship.g_relationships.descriptors[relIndex].reusedRelIndex = reusedRelIndex;

String msg;
msg = "reusing relationship \"" + M23_Relationship.g_relationships.descriptors[reusedRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[reusedRelIndex].relName + "\" [" + M23_Relationship.g_relationships.descriptors[reusedRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[reusedRelIndex].leftClassName + "<->" + M23_Relationship.g_relationships.descriptors[reusedRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[reusedRelIndex].rightClassName + "] (" + reusedRelIndex + ")";
msg = msg + " for \"" + M23_Relationship.g_relationships.descriptors[relIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[relIndex].relName + "\" [" + M23_Relationship.g_relationships.descriptors[relIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[relIndex].leftClassName + "<->" + M23_Relationship.g_relationships.descriptors[relIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[relIndex].rightClassName + "] (" + relIndex + ")";

M04_Utilities.logMsg(msg, M01_Common.LogLevel.ellInfo, null, null, null);

//Debug.Print msg
if (reusedRelIndex > 0) {
// add 'relIndex' to the list of relIndexes 'reusing' the relationship
M23_Relationship_Utilities.addRelIndex(M23_Relationship.g_relationships.descriptors[reusedRelIndex].reusingRelIndexes, relIndex);
}
}
}


public static Integer getRelIndexByName(String sectionName, String relName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

M23_Relationship.getRelationships();

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == relName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
M04_Utilities.logMsg("unable to identify relationship '" + sectionName + "." + relName + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtLdm, null, null);
}
return returnValue;
}


public static String getRelIdStrByIndex(int relIndex) {
String returnValue;
int i;

returnValue = -1;

if (relIndex > 0 &  relIndex < M23_Relationship.g_relationships.numDescriptors) {
returnValue = M23_Relationship.g_relationships.descriptors[relIndex].relIdStr;
}
return returnValue;
}


public static Integer getRelIndexByI18nId(String i18nId) {
Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].i18nId.toUpperCase() == i18nId.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static void genTransformedAttrDeclsForRelationshipWithColReUse(int thisRelIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean suppressMetaAttrs; 
if (suppressMetaAttrsW == null) {
suppressMetaAttrs = false;
} else {
suppressMetaAttrs = suppressMetaAttrsW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

String ukAttrDecls;
String pkAttrList;
String leftFkAttrs;
String rightFkAttrs;

//On Error GoTo ErrorExit 

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForRelationshipWithColReUse_Int(int thisRelIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, String ukAttrDecls, String pkAttrList, String leftFkAttrs, String rightFkAttrs, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW, Boolean useAlternativeDefaultsW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean suppressMetaAttrs; 
if (suppressMetaAttrsW == null) {
suppressMetaAttrs = false;
} else {
suppressMetaAttrs = suppressMetaAttrsW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

int numAttrs;

//On Error GoTo ErrorExit 

numAttrs = M23_Relationship.g_relationships.descriptors[thisRelIndex].attrRefs.numDescriptors;

if (!(suppressMetaAttrs &  M03_Config.useSurrogateKeysForNMRelationships & M23_Relationship.g_relationships.descriptors[thisRelIndex].useSurrogateKey)) {
M22_Class_Utilities.printSectionHeader("Surrogate Key", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacOid, null, indent, null, "[LDM] Relationship identifier", null, null, null, null, null), null, null);
pkAttrList = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, null, null, null, null, null, null);
}

M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, transformation, tabColumns, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, suppressMetaAttrs, false, M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional, null, forLrt, outputMode, indent, null, null, null, useAlternativeDefaults, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange &  (!(forLrt |  M03_Config.g_cfgGenLogChangeForLrtTabs)) & !suppressMetaAttrs) {
M24_Attribute.genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, outputMode, indent, null, useAlternativeDefaults);
}

if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.numIndexes > 0) {
int i;
for (i = 1; i <= 1; i += (1)) {
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i], transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, true, forLrt, outputMode);

M23_Relationship.genTransformedAttrDeclForRelationshipsByRelWithColReuse(M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i], transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, outputMode, indent, null, null);
}
}

if (suppressMetaAttrs) {
return;
}

M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;
leftClass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex];
rightclass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex];
M22_Class_Utilities.ClassDescriptor leftOrClass;
M22_Class_Utilities.ClassDescriptor rightOrClass;
leftOrClass = M22_Class.getOrMappingSuperClass(leftClass.sectionName, leftClass.className);
rightOrClass = M22_Class.getOrMappingSuperClass(rightclass.sectionName, rightclass.className);

ukAttrDecls = "";
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Class \"" + leftClass.sectionName + "." + leftClass.className + "\"", fileNo, outputMode, null);
// ### IF IVK ###
leftFkAttrs = M24_Attribute.genFkTransformedAttrDeclsWithColReuse(leftClass.classIndex, ((M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality == 0) &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1) & M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable ? "" : "NOT NULL"), leftOrClass.isPsForming, transformation, tabColumns, leftClass.className, leftClass.shortName, fileNo, ddlType, null, null, true, ukAttrDecls, outputMode, indent, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1) {
M22_Class_Utilities.printSectionHeader("Foreign Key (nationalized) corresponding to Class \"" + leftClass.sectionName + "." + leftClass.className + "\"", fileNo, null, null);
M24_Attribute.genFkTransformedAttrDeclsWithColReuse(leftOrClass.classIndex, "", leftOrClass.isPsForming, transformation, tabColumns, leftClass.className, leftClass.shortName, fileNo, ddlType, null, true, null, null, outputMode, indent, true);
}
// ### ELSE IVK ###
//   leftFkAttrs = _
//     genFkTransformedAttrDeclsWithColReuse(leftClass.classIndex, "NOT NULL", False, transformation, tabColumns, leftClass.className, _
//       leftClass.shortName, fileNo, ddlType, , , True, ukAttrDecls, outputMode, indent)
// ### ENDIF IVK ###
//    End With

boolean addComma;
addComma = M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag |  (M03_Config.supportNlForRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl) | M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged;

M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Class \"" + rightclass.sectionName + "." + rightclass.className + "\"", fileNo, outputMode, null);
// ### IF IVK ###
rightFkAttrs = M24_Attribute.genFkTransformedAttrDeclsWithColReuse(rightclass.classIndex, ((M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality == 0) &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1) & M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable ? "" : "NOT NULL"), rightOrClass.isPsForming, transformation, tabColumns, rightclass.className, rightclass.shortName, fileNo, ddlType, addComma |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1), null, true, ukAttrDecls, outputMode, indent, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1) {
M22_Class_Utilities.printSectionHeader("Foreign Key (nationalized) corresponding to Class \"" + rightclass.sectionName + "." + rightclass.className + "\"", fileNo, outputMode, null);
M24_Attribute.genFkTransformedAttrDeclsWithColReuse(rightOrClass.classIndex, "", rightOrClass.isPsForming, transformation, tabColumns, rightclass.className, rightclass.shortName, fileNo, ddlType, addComma, true, null, null, outputMode, indent, true);
}
// ### ELSE IVK ###
//   rightFkAttrs = _
//     genFkTransformedAttrDeclsWithColReuse(rightclass.classIndex, "NOT NULL", False, transformation, tabColumns, rightclass.className, _
//       rightClass.shortName, fileNo, ddlType, addComma, , True, ukAttrDecls, outputMode, indent)
// ### ENDIF IVK ###

// ### IF IVK ###
if (M03_Config.supportNlForRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl) {
addComma = M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable | M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged;

M22_Class_Utilities.printSectionHeader("Language Id (Relationship has stereotype <nlText>)", fileNo, outputMode, null);

M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLanguageId, M01_ACM.cosnLanguageId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M01_Globals_IVK.g_enumIndexLanguage, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL", addComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacLangId, null, indent, null, "[LDM] Language identifier", null, null, null, null, null), null, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable) {
addComma = M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged;

M22_Class_Utilities.printSectionHeader("Is the nationalized reference active?", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsNationalActive, M01_ACM_IVK.cosnIsNationalActive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL DEFAULT 0" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), addComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacRegular |  M01_Common.AttrCategory.eacNationalBool, null, indent, null, "[LDM] Is the nationalized reference active?", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged) {
// this relationship also needs to be considered PS-tagged
M22_Class_Utilities.printSectionHeader("Product Structure Tag", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conPsOid, M01_ACM_IVK.cosnPsOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL", M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag, ddlType, null, outputMode, M01_Common.AttrCategory.eacPsOid, null, indent, null, "[LDM] Product Structure Tag", null, null, null, null, null), null, null);
} else {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].aggHeadName.compareTo("GenericCode") == 0) {
M22_Class_Utilities.printSectionHeader("Division-Column", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conDivOid, M01_ACM_IVK.cosnDivOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL DEFAULT 0", M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag, ddlType, null, outputMode, M01_Common.AttrCategory.eacDivOid, null, indent, null, "[LDM] Division Tag", null, null, null, null, null), null, null);
}
}

// ### ENDIF IVK ###
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].useVersiontag) {
M22_Class_Utilities.printSectionHeader("Relationship Version Id", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, outputMode, M01_Common.AttrCategory.eacVid, null, indent, null, "[LDM] Record version tag", "1", null, null, null, null), null, null);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}



public static void genRelIdList() {
if (!(M03_Config.generateEntityIdList)) {
return;
}

String fileName;
fileName = M04_Utilities.genMetaFileName(M01_Globals.g_targetDir, "RelId", null);
M04_Utilities.assertDir(fileName);
int fileNo;
fileNo = M00_FileWriter.freeFileNumber();

//On Error GoTo ErrorExit 
M00_FileWriter.openFileForOutput(fileNo, fileName, false);

int thisRelIndex;
int maxQualRelNameLen;
maxQualRelNameLen = 0;

for (thisRelIndex = 1; thisRelIndex <= 1; thisRelIndex += (1)) {
if (!(M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relId > 0)) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.length() > maxQualRelNameLen) {
maxQualRelNameLen = M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.length();
}
}
}

for (thisRelIndex = 1; thisRelIndex <= 1; thisRelIndex += (1)) {
if (!(M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relId > 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.paddRight(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, maxQualRelNameLen, null) + " : " + M23_Relationship.g_relationships.descriptors[thisRelIndex].relIdStr);
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


// ### IF IVK ###
private static void genAllowedCountriesListFunction(int thisRelIndex, int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

boolean isDisallowedCountries;
int targetClassIndex;
String targetClassName;
String targetSectionName;
String qualTabName;
String qualTabNameLrt;
boolean targetIsGenericAspect;

String qualTabNameCountryIdXRef;
qualTabNameCountryIdXRef = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryIdXRef, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
String qualTabNameOrgManagesCountry;
qualTabNameOrgManagesCountry = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexOrgManagesCountry, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
String qualTabNameCountrySpec;
qualTabNameCountrySpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList != M01_Common.RelNavigationMode.ernmNone |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList != M01_Common.RelNavigationMode.ernmNone) {
targetClassName = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex].className;
targetSectionName = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex].sectionName;
qualTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

targetIsGenericAspect = (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex].classIndex == M01_Globals_IVK.g_classIndexGenericAspect);
}
isDisallowedCountries = (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList != M01_Common.RelNavigationMode.ernmNone);

if (targetClassName.compareTo(M01_ACM_IVK.clnGenericAspect) == 0) {
// we currently only support utility functions for this class
} else {
return;
}

boolean M72_DataPool.poolSupportLrt;
returnValue = false;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

boolean isWorkDataPool;
isWorkDataPool = (thisPoolIndex == M01_Globals.g_workDataPoolIndex);

String qualFuncName;
boolean use3DigitIds;
String funcName;
int maxResLen;

int i;
for (int i = 1; i <= 2; i++) {
use3DigitIds = (i == 2);
if (isDisallowedCountries) {
funcName = (use3DigitIds ? M01_ACM_IVK.udfnDisallowedCountry2Str0 : M01_ACM_IVK.udfnDisallowedCountry2Str);
maxResLen = M01_Globals_IVK.gc_disallowedCountriesMaxLength;
} else {
funcName = (use3DigitIds ? M01_ACM_IVK.udfnAllowedCountry2Str0 : M01_ACM_IVK.udfnAllowedCountry2Str);
maxResLen = M01_Globals_IVK.gc_allowedCountriesMaxLength;
}

boolean lrtAware;
boolean includeDeletedPrivRecords;
boolean includeDeletedPubRecords;
String udfSuffixName;
int k;
for (int k = 1; k <= ((ddlType == M01_Common.DdlTypeId.edtLdm |  M72_DataPool.poolSupportLrt) &  use3DigitIds ? 4 : 1); k++) {
lrtAware = ((k == 2) |  (k == 3));

includeDeletedPrivRecords = false;
includeDeletedPubRecords = false;
udfSuffixName = "";
if (k == 3) {
includeDeletedPrivRecords = true;
udfSuffixName = "_D";
} else if (k == 4) {
includeDeletedPubRecords = true;
udfSuffixName = "_D";
}

int targetSectionIndex;
int l;
for (int l = 1; l <= (use3DigitIds &  targetIsGenericAspect & k == 1 ? 2 : 1); l++) {
targetSectionIndex = (l == 1 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex : M01_Globals.g_sectionIndexAliasLrt);

qualFuncName = M04_Utilities.genQualFuncName(targetSectionIndex, funcName + udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for concatenating CountrySpec-IDs for ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (use3DigitIds ? " (use 3-digit IDs" + (lrtAware ? " / LRT-aware" : "") + (includeDeletedPrivRecords |  includeDeletedPubRecords ? " / for deleted records" : "") + ")" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
if (lrtAware) {
M11_LRT.genProcParm(fileNo, "", "lrtOid_in", M01_Globals.g_dbtOid, true, "OID of the LRT used for reference");
}
M11_LRT.genProcParm(fileNo, "", "maxLength_in", "INTEGER", false, "maximum length of string returned");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxAlCountryListLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_cspIdList", "VARCHAR(" + String.valueOf(new Double(1 * maxAlCountryListLen).intValue()) + ")", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_trailer", "CHAR(3)", "'...'", null, null);
if (lrtAware) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}

if (lrtAware) {
M11_LRT.genProcSectionHeader(fileNo, "allow for NULL being passed for 'lrtOid_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = COALESCE(lrtOid_in, 0);");
}

M11_LRT.genProcSectionHeader(fileNo, "loop over relationship records related to the given OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR cspLoop AS");

if (isWorkDataPool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Pub");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CXR.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (PARTITION BY CXR.CSP_OID ORDER BY PUB." + M01_Globals_IVK.g_anIsDeleted + " DESC)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CXR.CSP_OID");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCountryIdXRef + " CXR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUB.ACLACL_OID = CXR.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PUB." + M01_Globals.g_anOid + " = oid_in)");

if (!(includeDeletedPubRecords)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PUB." + M01_Globals_IVK.g_anIsDeleted + " = 0)");
}

if (lrtAware) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(PUB." + M01_Globals.g_anInLrt + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(PUB." + M01_Globals.g_anInLrt + " <> v_lrtOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Priv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CXR.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (PARTITION BY CXR.CSP_OID ORDER BY PRIV." + M01_Globals.g_anLrtState + " DESC)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CXR.CSP_OID");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCountryIdXRef + " CXR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV.ACLACL_OID = CXR.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals.g_anOid + " = oid_in)");
if (!(includeDeletedPrivRecords)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals.g_anInLrt + " = v_lrtOid)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Csp_Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Pub WHERE seqNo = 1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Pub");
}

if (lrtAware) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Priv WHERE seqNo = 1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Priv");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID AS c_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Csp_Oid A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameCountrySpec + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.csp_oid = C." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID AS c_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabName + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryIdXRef + " CXR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ACLACL_OID = CXR.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountrySpec + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anOid + " = CXR.CSP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CXR.CSP_OID = C." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID");
if (use3DigitIds) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 256 ROWS ONLY");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
if (use3DigitIds) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RIGHT('000' || RTRIM(CAST(c_id AS CHAR(5))), 3);");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(c_id AS CHAR(5)));");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "post-process result string", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_cspIdList = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF LENGTH(v_cspIdList) > maxLength_in THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN LEFT(v_cspIdList, (maxLength_in - LENGTH(v_trailer))) || v_trailer;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN v_cspIdList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_cspIdList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

qualFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, funcName + udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for concatenating CountrySpec-IDs for ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (use3DigitIds ? " (use 3-digit IDs" + (lrtAware ? " / LRT-aware" : "") + ")" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, lrtAware, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
if (lrtAware) {
M11_LRT.genProcParm(fileNo, "", "lrtOid_in", M01_Globals.g_dbtOid, false, "OID of the LRT used for reference");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxAlCountryListLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName + "(oid_in" + (lrtAware ? ", lrtOid_in" : "") + ", " + String.valueOf(maxResLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

// ####################################################################################################################
// #    UDF to decide whether a record is valid for a given Organization
// ####################################################################################################################

if (!(isDisallowedCountries)) {
String qualTabNameCountryGroupElem;
qualTabNameCountryGroupElem = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryGroupElement, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualTabNameDisAllowed;
qualTabNameDisAllowed = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "Dis");

qualFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, "HASALCNTRY", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function deciding whether a \"" + targetSectionName + "." + targetClassName + "\" is valid for a given Organization", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (targetIsGenericAspect) {
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of an '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].className + "'-object");
M11_LRT.genProcParm(fileNo, "", "classId_in", M01_Globals.g_dbtEntityId, true, "CLASSID of the '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].className + "'-object");
} else {
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
}
M11_LRT.genProcParm(fileNo, "", "orgOid_in", M01_Globals.g_dbtOid, false, "OID of '" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexOrganization].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexOrganization].className + "'-object");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (targetIsGenericAspect) {
M11_LRT.genProcSectionHeader(fileNo, "special consideration for SR0Validity", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexSr0Validity].classIdStr + "' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "check ALLOWEDCOUNTRIES-association\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CY." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CY." + M01_Globals.g_anCid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameCountryIdXRef + " CXR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.ACLACL_OID = CXR.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameCountrySpec + " CY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CY." + M01_Globals.g_anOid + " = CXR.CSP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_AllowedCountrySpec AC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CY." + M01_Globals.g_anCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_AllowedCountrySpec_Expanded AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountrySpec + " CY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryGroupElem + " GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid = GE.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID = CY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level < 100");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded_ByOrg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OC.ORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_AllowedCountrySpec_Expanded AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameOrgManagesCountry + " OC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.classId = '02002'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.countrySpecOid = OC.CNT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OC.ORG_OID = orgOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_DisallowedCountrySpec_Expanded");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CXR.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabName + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryIdXRef + " CXR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A.DCLDCL_OID = CXR.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountrySpec + " CY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CY." + M01_Globals.g_anOid + " = CXR.CSP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_DisallowedCountrySpec_Expanded AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountrySpec + " CY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryGroupElem + " GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid = GE.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID = CY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level < 50");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.countrySpecOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded_ByOrg V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId = '02002'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.countrySpecOid NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VD.countrySpecOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_DisallowedCountrySpec_Expanded VD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}


private static void genAllowedCountriesFunction(int thisRelIndex, int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

boolean isDisallowedCountries;
String oidAttrName;
String targetClassName;
String targetSectionName;
boolean targetIsGenericAspect;

String qualTabNameOrgManagesCountry;
qualTabNameOrgManagesCountry = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexOrgManagesCountry, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
String qualTabNameCountrySpec;
qualTabNameCountrySpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries != M01_Common.RelNavigationMode.ernmNone |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries != M01_Common.RelNavigationMode.ernmNone) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft) {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].rightFkColName[ddlType];
} else {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].leftFkColName[ddlType];
}
targetClassName = M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].className;
targetSectionName = M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].sectionName;
targetIsGenericAspect = (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[(M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)].orMappingSuperClassIndex].classIndex == M01_Globals_IVK.g_classIndexGenericAspect);
isDisallowedCountries = true;
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries != M01_Common.RelNavigationMode.ernmNone) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft) {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].rightFkColName[ddlType];
} else {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].leftFkColName[ddlType];
}
isDisallowedCountries = false;
}

boolean M72_DataPool.poolSupportLrt;
returnValue = false;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

boolean isWorkDataPool;
isWorkDataPool = (thisPoolIndex == M01_Globals.g_workDataPoolIndex);

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null);

String qualFuncName;
boolean use3DigitIds;
String funcName;
int maxResLen;

int i;
for (int i = 1; i <= 2; i++) {
use3DigitIds = (i == 2);
if (isDisallowedCountries) {
funcName = (use3DigitIds ? M01_ACM_IVK.udfnDisallowedCountry2Str0 : M01_ACM_IVK.udfnDisallowedCountry2Str);
maxResLen = M01_Globals_IVK.gc_disallowedCountriesMaxLength;
} else {
funcName = (use3DigitIds ? M01_ACM_IVK.udfnAllowedCountry2Str0 : M01_ACM_IVK.udfnAllowedCountry2Str);
maxResLen = M01_Globals_IVK.gc_allowedCountriesMaxLength;
}

boolean lrtAware;
boolean includeDeletedPrivRecords;
boolean includeDeletedPubRecords;
String udfSuffixName;
int k;
for (int k = 1; k <= ((ddlType == M01_Common.DdlTypeId.edtLdm |  M72_DataPool.poolSupportLrt) &  use3DigitIds ? 4 : 1); k++) {
lrtAware = ((k == 2) |  (k == 3));

includeDeletedPrivRecords = false;
includeDeletedPubRecords = false;
udfSuffixName = "";
if (k == 3) {
includeDeletedPrivRecords = true;
udfSuffixName = "_D";
} else if (k == 4) {
includeDeletedPubRecords = true;
udfSuffixName = "_D";
}

int targetSectionIndex;
int l;
for (int l = 1; l <= (use3DigitIds &  targetIsGenericAspect & k == 1 ? 2 : 1); l++) {
targetSectionIndex = (l == 1 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex : M01_Globals.g_sectionIndexAliasLrt);

qualFuncName = M04_Utilities.genQualFuncName(targetSectionIndex, funcName + udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for concatenating CountrySpec-IDs for ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (use3DigitIds ? " (use 3-digit IDs" + (lrtAware ? " / LRT-aware" : "") + (includeDeletedPrivRecords |  includeDeletedPubRecords ? " / for deleted records" : "") + ")" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
if (lrtAware) {
M11_LRT.genProcParm(fileNo, "", "lrtOid_in", M01_Globals.g_dbtOid, true, "OID of the LRT used for reference");
}
M11_LRT.genProcParm(fileNo, "", "maxLength_in", "INTEGER", false, "maximum length of string returned");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxAlCountryListLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_cspIdList", "VARCHAR(" + String.valueOf(new Double(1 * maxAlCountryListLen).intValue()) + ")", "''", null, null);
M11_LRT.genVarDecl(fileNo, "v_trailer", "CHAR(3)", "'...'", null, null);
if (lrtAware) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}

if (lrtAware) {
M11_LRT.genProcSectionHeader(fileNo, "allow for NULL being passed for 'lrtOid_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = COALESCE(lrtOid_in, 0);");
}

M11_LRT.genProcSectionHeader(fileNo, "loop over relationship records related to the given OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR cspLoop AS");

if (isWorkDataPool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Pub");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUB.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (PARTITION BY PUB.CSP_OID ORDER BY PUB." + M01_Globals_IVK.g_anIsDeleted + " DESC)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUB.CSP_OID");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PUB." + oidAttrName + " = oid_in)");

if (!(includeDeletedPubRecords)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PUB." + M01_Globals_IVK.g_anIsDeleted + " = 0)");
}

if (lrtAware) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(PUB." + M01_Globals.g_anInLrt + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(PUB." + M01_Globals.g_anInLrt + " <> v_lrtOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Priv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (PARTITION BY PRIV.CSP_OID ORDER BY PRIV." + M01_Globals.g_anLrtState + " DESC)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV.CSP_OID");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + oidAttrName + " = oid_in)");
if (!(includeDeletedPrivRecords)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals.g_anInLrt + " = v_lrtOid)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Csp_Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csp_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (includeDeletedPubRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Pub WHERE seqNo = 1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Pub");
}

if (lrtAware) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION");
if (includeDeletedPrivRecords) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Priv WHERE seqNo = 1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT csp_oid FROM V_Priv");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID AS c_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Csp_Oid A,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountrySpec + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.csp_oid = C." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID AS c_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabName + " A,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountrySpec + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + oidAttrName + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.CSP_OID = C." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ID");
if (use3DigitIds) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 256 ROWS ONLY");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
if (use3DigitIds) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RIGHT('000' || RTRIM(CAST(c_id AS CHAR(5))), 3);");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(c_id AS CHAR(5)));");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "post-process result string", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_cspIdList = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cspIdList = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF LENGTH(v_cspIdList) > maxLength_in THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN LEFT(v_cspIdList, (maxLength_in - LENGTH(v_trailer))) || v_trailer;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN v_cspIdList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_cspIdList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

qualFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, funcName + udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for concatenating CountrySpec-IDs for ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (use3DigitIds ? " (use 3-digit IDs" + (lrtAware ? " / LRT-aware" : "") + ")" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, lrtAware, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
if (lrtAware) {
M11_LRT.genProcParm(fileNo, "", "lrtOid_in", M01_Globals.g_dbtOid, false, "OID of the LRT used for reference");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxAlCountryListLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName + "(oid_in" + (lrtAware ? ", lrtOid_in" : "") + ", " + String.valueOf(maxResLen) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

// ####################################################################################################################
// #    UDF to decide whether a record is valid for a given Organization
// ####################################################################################################################

if (!(isDisallowedCountries)) {
String qualTabNameCountryGroupElem;
qualTabNameCountryGroupElem = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryGroupElement, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualTabNameDisAllowed;
qualTabNameDisAllowed = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "Dis");

qualFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, "HASALCNTRY", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function deciding whether a \"" + targetSectionName + "." + targetClassName + "\" is valid for a given Organization", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (targetIsGenericAspect) {
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of an '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].className + "'-object");
M11_LRT.genProcParm(fileNo, "", "classId_in", M01_Globals.g_dbtEntityId, true, "CLASSID of the '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].className + "'-object");
} else {
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, true, "OID of '" + targetSectionName + "." + targetClassName + "'-object");
}
M11_LRT.genProcParm(fileNo, "", "orgOid_in", M01_Globals.g_dbtOid, false, "OID of '" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexOrganization].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexOrganization].className + "'-object");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (targetIsGenericAspect) {
M11_LRT.genProcSectionHeader(fileNo, "special consideration for SR0Validity", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexSr0Validity].classIdStr + "' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "check ALLOWEDCOUNTRIES-association\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CY." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CY." + M01_Globals.g_anCid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameCountrySpec + " CY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " AC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.CSP_OID = CY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC." + oidAttrName + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_AllowedCountrySpec AC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CY." + M01_Globals.g_anCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_AllowedCountrySpec_Expanded  AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountrySpec + " CY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryGroupElem + " GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid = GE.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID = CY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level < 100");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded_ByOrg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OC.ORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_AllowedCountrySpec_Expanded AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameOrgManagesCountry + " OC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.classId = '02002'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AC.countrySpecOid = OC.CNT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OC.ORG_OID = orgOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_DisallowedCountrySpec_Expanded");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countrySpecOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameDisAllowed + " AC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC." + oidAttrName + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_DisallowedCountrySpec_Expanded AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountrySpec + " CY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryGroupElem + " GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.countrySpecOid = GE.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GE.CSP_OID = CY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AC.level < 50");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.countrySpecOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AllowedCountrySpec_Expanded_ByOrg V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId = '02002'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.countrySpecOid NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VD.countrySpecOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_DisallowedCountrySpec_Expanded VD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}


private static void genAllowedCountriesView(int thisRelIndex, int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

boolean isDisallowedCountries;
String oidAttrName;

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft) {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].rightFkColName[ddlType];
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmRight |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmRight) {
oidAttrName = M23_Relationship.g_relationships.descriptors[thisRelIndex].leftFkColName[ddlType];
}

isDisallowedCountries = M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries != M01_Common.RelNavigationMode.ernmNone;

String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, (isDisallowedCountries ? "DAC" : "AC"), null, null);

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualCountrySpecTabName;
qualCountrySpecTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String relNameInfix;
relNameInfix = M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.substring(0, 1).toUpperCase() + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.substring(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.length() - 1 - M23_Relationship.g_relationships.descriptors[thisRelIndex].relName.length() - 1);

M22_Class_Utilities.printSectionHeader("View for concatenating CountrySpec-IDs for ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + oidAttrName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "COUNTRYSPECS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_" + relNameInfix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "countrySpecId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AC." + oidAttrName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CS.ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (PARTITION BY AC." + oidAttrName + " ORDER BY CS.ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " AC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualCountrySpecTabName + " CS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AC.CSP_OID = CS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_" + relNameInfix + "Str");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "countrySpecs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(RTRIM(CAST(countrySpecId AS CHAR(5))) AS VARCHAR(500)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_" + relNameInfix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNo = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AC." + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ACS.CountrySpecs || ',' || CAST(RTRIM(CAST(AC.CountrySpecId AS CHAR(5))) AS VARCHAR(500)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ACS.Level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_" + relNameInfix + "Str ACS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_" + relNameInfix + "    AC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ACS." + oidAttrName + " = AC." + oidAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AC.seqNo = ACS.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ACS.level < 5000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_" + relNameInfix + "StrMax");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "countrySpecs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countrySpecs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_" + relNameInfix + "Str ACS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + oidAttrName + " FROM V_" + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "Str ACS2 WHERE ACS." + oidAttrName + " = ACS2." + oidAttrName + " AND ACS2.Level > ACS.Level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + oidAttrName.toLowerCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "countrySpecs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_" + relNameInfix + "StrMax");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


// ### ENDIF IVK ###
private static void genRelationshipDdl(int thisRelIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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


if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
return;
}

String thisOrgDescriptorStr;
// ### IF IVK ###
boolean isDivTagged;
Integer tabPartitionType;
// ### ENDIF IVK ###

int fileNo;
int fileNoFk;
// ### IF IVK ###
int fileNoAc;
int fileNoXmlF;
int fileNoXmlV;
int fileNoPs;
// ### ENDIF IVK ###
int fileNoLc;
int fileNoLrt;
int fileNoLrtView;
int fileNoClView;
int fileNoLrtSup;
// ### IF IVK ###
int fileNoSetProd;
int fileNoSetProdCl;
int fileNoFto;
int fileNoPsCopy;
int fileNoPsCopy2;
int fileNoExpCopy;
int fileNoArc;
// ### ENDIF IVK ###

thisOrgDescriptorStr = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);

int orgSetProductiveTargetPoolIndex;
orgSetProductiveTargetPoolIndex = -1;
if (thisOrgIndex > 0) {
orgSetProductiveTargetPoolIndex = M71_Org.g_orgs.descriptors[thisOrgIndex].setProductiveTargetPoolIndex;
}

boolean poolSuppressUniqueConstraints;
boolean M72_DataPool.poolSupportLrt;
boolean poolCommonItemsLocal;
boolean poolSupportAcm;
boolean poolSuppressRefIntegrity;
boolean poolSupportUpdates;
boolean poolSupportXmlExport;
if (thisPoolIndex > 0) {
poolSuppressUniqueConstraints = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressUniqueConstraints;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
poolCommonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
poolSupportAcm = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm;
poolSuppressRefIntegrity = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressRefIntegrity;
poolSupportUpdates = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportUpdates;
poolSupportXmlExport = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportXmlExport;
} else {
returnValue = true;
poolSupportAcm = true;
poolSupportUpdates = true;
}

int ldmIteration;

if (ddlType != M01_Common.DdlTypeId.edtPdm &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isPdmSpecific) {
goto NormalExit;
}

// ### IF IVK ###
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "" == "" |  ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl) : true) &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1))) &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList == M01_Common.RelNavigationMode.ernmNone) & (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList == M01_Common.RelNavigationMode.ernmNone)) {
goto NormalExit;
}
// ### ELSE IVK ###
//   If (.sectionName & "" = "" Or (IIf(supportNlForRelationships, Not .isNl, True) And (.maxLeftCardinality = 1 Or .maxRightCardinality = 1))) Then
//     GoTo NormalExit
//   End If
// ### ENDIF IVK ###

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship |  M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
goto NormalExit;
}

if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].reusedRelIndex > 0) {
goto NormalExit;
}

if (M03_Config.ignoreUnknownSections &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex < 0)) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M20_Section_Utilities.sectionValidForPoolAndOrg(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, thisOrgIndex, thisPoolIndex))) {
goto NormalExit;
}
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isLrtSpecific & ! M01_Globals.g_genLrtSupport) {
goto NormalExit;
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].specificToOrgId > 0 &  ddlType == M01_Common.DdlTypeId.edtPdm & M23_Relationship.g_relationships.descriptors[thisRelIndex].specificToOrgId != thisOrgId) {
goto NormalExit;
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].specificToPool > 0 &  ddlType == M01_Common.DdlTypeId.edtPdm & M23_Relationship.g_relationships.descriptors[thisRelIndex].specificToPool != thisPoolId) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolId != -1) {
if (!(M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated & ! poolSupportAcm)) {
goto NormalExit;
}
}


ldmIteration = (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs ? M01_Common.ldmIterationGlobal : M01_Common.ldmIterationPoolSpecific);

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries) {
fileNoAc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepAllowedCountries, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseModuleMeta, ldmIteration);
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountriesList |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountriesList) {
fileNoAc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepAllowedCountries, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseModuleMeta, ldmIteration);

genAllowedCountriesListFunction(thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType);

goto NormalExit;
}

// ### ENDIF IVK ###
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseFksRelTabs, ldmIteration);

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId > 0 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId > 0)) {
fileNoFk = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex : thisOrgIndex), (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex : thisPoolIndex), null, M01_Common.phaseFksRelTabs, M01_Common.ldmIterationPoolSpecific);
} else if (ddlType == M01_Common.DdlTypeId.edtLdm &  ldmIteration != M01_Common.ldmIterationPoolSpecific) {
fileNoFk = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, null, null, null, M01_Common.phaseFksRelTabs, M01_Common.ldmIterationPoolSpecific);
} else {
fileNoFk = fileNo;
}

if (M03_Config.generateLrt) {
fileNoLrt = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrt, ldmIteration);

fileNoLrtView = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrtViews, ldmIteration);

fileNoClView = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseChangeLogViews, ldmIteration);

fileNoLrtSup = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrtSupport, ldmIteration);
}

// ### IF IVK ###
if (M03_Config.generatePsTaggingView &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged) {
fileNoPs = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phasePsTagging, ldmIteration);
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange) {
if () |  (M03_Config.generateLogChangeView & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged & M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChangeInView)) {
if (fileNoPs > 0) {
fileNoLc = fileNoPs;
} else {
fileNoLc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLogChange, ldmIteration);
}
}
}
// ### ELSE IVK ###
//   If .logLastChange Then
//     If (.logLastChangeAutoMaint) Or (generateLogChangeView And Not .isUserTransactional And .logLastChangeInView) Then
//       fileNoLc = openDdlFile(g_targetDir, .sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLogChange, ldmIteration)
//     End If
//   End If
// ### ENDIF IVK ###

// ### IF IVK ###
if (M01_Globals.g_genLrtSupport &  M03_Config.generatePsCopySupport & (M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsForming |  M23_Relationship.g_relationships.descriptors[thisRelIndex].supportExtendedPsCopy) & M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional) {
fileNoPsCopy = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepPsCopy, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
fileNoPsCopy2 = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepPsCopy2, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}

if (M01_Globals.g_genLrtSupport &  M03_Config.generateExpCopySupport & M23_Relationship.g_relationships.descriptors[thisRelIndex].isSubjectToExpCopy) {
fileNoExpCopy = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepExpCopy, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.supportArchivePool & M72_DataPool.poolSupportsArchiving(thisPoolId)) {
fileNoArc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseArchive, ldmIteration);
}

if (M03_Config.generateLrt) {
if (orgSetProductiveTargetPoolIndex > 0) {
fileNoSetProd = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);

fileNoSetProdCl = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, null, M01_Common.phaseChangeLogViews, ldmIteration);
}

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].noFto) {
fileNoFto = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStepFto, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}
}

if (M03_Config.generateXmlExportSupport) {
fileNoXmlV = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseXmlExport, ldmIteration);

if (M03_Config.generateXsdInCtoSchema &  ddlType == M01_Common.DdlTypeId.edtPdm & thisOrgId != -1) {
fileNoXmlF = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, processingStep, ddlType, null, null, null, M01_Common.phaseXmlExport, ldmIteration);
} else {
fileNoXmlF = fileNoXmlV;
}
}

// ### ENDIF IVK ###
//On Error GoTo ErrorExit 

boolean genSupportForLrt;
genSupportForLrt = false;
if (M01_Globals.g_genLrtSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional) {
if (thisPoolIndex > 1) {
genSupportForLrt = M72_DataPool.poolSupportLrt;
} else {
genSupportForLrt = (ddlType == M01_Common.DdlTypeId.edtLdm) & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools;
}
}

// (optionally) loop twice over the table structure: first run: 'Main' (public) table; second run: corresponding (private) LRT-tables
int loopCount;
int iteration;
boolean forLrt;
loopCount = (genSupportForLrt ? 2 : 1);

String qualTabName;
String qualTabNameLdm;
String relSectionName;
String relShortName;
String relName;
int numAttrs;
String leftFkAttrs;
String rightFkAttrs;
String ukAttrDecls;
String pkAttrList;

M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;
leftClass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex];
rightclass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex];

M22_Class_Utilities.ClassDescriptor leftOrClass;
M22_Class_Utilities.ClassDescriptor rightOrClass;
leftOrClass = M22_Class.getOrMappingSuperClass(leftClass.sectionName, leftClass.className);
rightOrClass = M22_Class.getOrMappingSuperClass(rightclass.sectionName, rightclass.className);

relSectionName = M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName;
relName = M23_Relationship.g_relationships.descriptors[thisRelIndex].relName;
relShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName;
numAttrs = M23_Relationship.g_relationships.descriptors[thisRelIndex].attrRefs.numDescriptors;

// ### IF IVK ###
isDivTagged = M23_Relationship.g_relationships.descriptors[thisRelIndex].leftIsDivision |  M23_Relationship.g_relationships.descriptors[thisRelIndex].rightIsDivision | rightOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode | leftOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode;

// ### ENDIF IVK ###

for (iteration = 1; iteration <= 1; iteration += (1)) {
forLrt = (iteration == 2);

qualTabName = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, null, null);
qualTabNameLdm = (ddlType == M01_Common.DdlTypeId.edtLdm ? qualTabName : M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, null, null));

M96_DdlSummary.addTabToDdlSummary(qualTabName, ddlType, false);
M78_DbMeta.registerQualTable(qualTabNameLdm, qualTabName, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, ddlType, M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated, false, forLrt, null, null);

String leftQualTabName;
String rightQualTabName;
String leftQualTabNameLdm;
String rightQualTabNameLdm;
boolean leftUseSurrogateKey;
boolean rightUseSurrogateKey;
boolean leftUseFileNoFk;
boolean rightUseFileNoFk;

boolean isLeftRefToGen;
boolean isLeftRefToNl;
isLeftRefToGen = (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttGen) != 0 &  leftOrClass.isGenForming & !leftOrClass.hasNoIdentity;
isLeftRefToNl = (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttNL) != 0 &  ((isLeftRefToGen &  leftOrClass.hasNlAttrsInGenInclSubClasses) |  (!(isLeftRefToGen &  leftOrClass.hasNlAttrsInNonGenInclSubClasses)));

boolean isRightRefToGen;
boolean isRightRefToNl;
isRightRefToGen = (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttGen) != 0 &  rightOrClass.isGenForming & !rightOrClass.hasNoIdentity;
isRightRefToNl = (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttNL) != 0 &  ((isRightRefToGen &  rightOrClass.hasNlAttrsInGenInclSubClasses) |  (!(isRightRefToGen &  rightOrClass.hasNlAttrsInNonGenInclSubClasses)));

leftUseFileNoFk = (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs & ! leftOrClass.isCommonToOrgs) |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools & ! leftOrClass.isCommonToPools);
leftUseSurrogateKey = M22_Class.getUseSurrogateKeyByClassName(leftOrClass.sectionName, leftOrClass.className);
leftQualTabName = M04_Utilities.genQualTabNameByClassIndex(leftOrClass.classIndex, ddlType, (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex : thisOrgIndex), (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex : thisPoolIndex), isLeftRefToGen, null, null, null, null, null, null);
leftQualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(leftOrClass.classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, isLeftRefToGen, null, null, isLeftRefToNl, null, null, null);
rightUseFileNoFk = (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs & ! rightOrClass.isCommonToOrgs) |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools & ! rightOrClass.isCommonToPools);
rightUseSurrogateKey = M22_Class.getUseSurrogateKeyByClassName(rightOrClass.sectionName, rightOrClass.className);
rightQualTabName = M04_Utilities.genQualTabNameByClassIndex(rightOrClass.classIndex, ddlType, (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgIndex : thisOrgIndex), (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex > 0 ? M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolIndex : thisPoolIndex), isRightRefToGen, null, null, null, null, null, null);
rightQualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(rightOrClass.classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, isRightRefToGen, null, null, isRightRefToNl, null, null, null);

if (M03_Config.generateDdlCreateTable) {
M22_Class_Utilities.printChapterHeader((M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated ? "LDM" : "ACM") + "-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (!(forLrt) ? "" : " (LRT)") + " (" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + rightclass.sectionName + "." + rightclass.className + "[" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo);

if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.numIndexes > 0) {
int i;
for (i = 1; i <= 1; i += (1)) {
M22_Class_Utilities.printComment("subsuming relationship:  \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].relName + "\" (" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].rightClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].reusingRelIndexes.indexes[i]].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo, null, null);
}
}

M00_FileWriter.printToFile(fileNo, "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, "(");

pkAttrList = "";

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, null, false, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), poolCommonItemsLocal);

M00_FileWriter.printToFile(fileNo, ")");

// ### IF IVK ###
String fkAttrToDiv;
if (isDivTagged &  M03_Config.supportRangePartitioningByDivOid) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftIsDivision) {
fkAttrToDiv = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName, null, null, null, null);
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightIsDivision) {
fkAttrToDiv = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName, null, null, null, null);
} else {
fkAttrToDiv = M01_ACM_IVK.conDivOid;
}
}

M22_Class.genTabDeclTrailer(fileNo, ddlType, isDivTagged, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, thisOrgIndex, thisPoolIndex, false, forLrt, false, false, fkAttrToDiv, tabPartitionType);
// ### ELSE IVK ###
//       genTabDeclTrailer fileNo, ddlType, eactRelationship, .relIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if ((forLrt &  M03_Config.lrtTablesVolatile) |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isVolatile) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE " + qualTabName + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.generateCommentOnTables & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M22_Class.genDbObjComment("TABLE", qualTabName, "ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"" + (forLrt ? " (LRT)" : ""), fileNo, thisOrgIndex, thisPoolIndex, null);
}

if (M03_Config.generateCommentOnColumns & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "COMMENT ON " + qualTabName + " (");
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, false, false, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomListLrt : M01_Common.DdlOutputMode.edomListNonLrt) |  M01_Common.DdlOutputMode.edomComment, poolCommonItemsLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].noAlias) {
// ### IF IVK ###
M22_Class.genAliasDdl(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs, M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools, !(M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated), qualTabNameLdm, qualTabName, M23_Relationship.g_relationships.descriptors[thisRelIndex].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, forLrt, false, false, false, "ACM-Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", null, M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional, M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged, null, M23_Relationship.g_relationships.descriptors[thisRelIndex].isSubjectToArchiving, M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChangeInView, null, null);
// ### ELSE IVK ###
//       genAliasDdl(.sectionIndex, .relName, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
//                   qualTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, _
//                   "ACM-Relationship """ & .sectionName & "." & .relName & """", , .isUserTransactional, .logLastChangeInView)
// ### ENDIF IVK ###
}

if (!(((ddlType == M01_Common.DdlTypeId.edtPdm) &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].noIndexesInPool >= 0) & (M23_Relationship.g_relationships.descriptors[thisRelIndex].noIndexesInPool == thisPoolId)))) {
// ### IF IVK ###
M76_Index.genIndexesForEntity(qualTabName, thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, false, forLrt, false, false, poolSuppressUniqueConstraints, tabPartitionType);
// ### ELSE IVK ###
//       genIndexesForEntity qualTabName, thisRelIndex.relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, _
//         False, poolSuppressUniqueConstraints
// ### ENDIF IVK ###
}

String fkName;
String qualIndexName;
// ### IF IVK ###
if ((rightOrClass.isPsTagged |  leftOrClass.isPsTagged)) {
if (!(poolSuppressRefIntegrity)) {

fkName = M04_Utilities.genFkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, "PS", ddlType, thisOrgIndex, thisPoolIndex, null, null);

String qualTabNameProductStructureLdm;
qualTabNameProductStructureLdm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProductStructure, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null);

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Product Structure\" Table", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals_IVK.g_anPsOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameProductStructure + " (" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualTabNameProductStructureLdm, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, null, null, null);
}

} else if ((rightOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode |  leftOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode)) {
if (!(poolSuppressRefIntegrity)) {

fkName = M04_Utilities.genFkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, "DIV", ddlType, thisOrgIndex, thisPoolIndex, null, null);

String qualTabNameDivisionLdm;
qualTabNameDivisionLdm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDivision, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null);

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Division\" Table", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals_IVK.g_anDivOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameDivision + " (" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualTabNameDivisionLdm, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, null, null, null);
}

}

// ### ENDIF IVK ###
if (M03_Config.supportNlForRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl) {
// DDL for Foreign Key to 'Language Table'
if (!(poolSuppressRefIntegrity)) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Language Table\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + qualTabName);
M00_FileWriter.printToFile(fileNo, "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + M04_Utilities.genFkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + "LAN", M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + "LAN", "", ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + "(" + M01_Globals.g_anLanguageId + ")");
M00_FileWriter.printToFile(fileNo, "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + M01_Globals.g_qualTabNameLanguage + "(" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexLanguage, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null), M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, null, null, null);
}
}

if (!(forLrt)) {
int fileNoToUse;
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !rightOrClass.isCommonToOrgs & !poolSuppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since this relationship is common to MPCs and (right) class \"" + rightOrClass.sectionName + "." + rightOrClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" is common to MPCs");
}
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & (!((rightOrClass.isCommonToPools |  rightOrClass.isCommonToOrgs))) & !poolSuppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since this relationship is common to Pools and (right) class \"" + rightOrClass.sectionName + "." + rightOrClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" is common to pools");
}
} else {
if (!(poolSuppressRefIntegrity)) {
if (M03_Config.generateDdlCreateFK) {
fileNoToUse = (rightUseFileNoFk ? fileNoFk : fileNo);
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId > 0 & ! rightOrClass.isCommonToOrgs) |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId > 0 & ! rightOrClass.isCommonToPools)) {
M00_FileWriter.printToFile(fileNoToUse, "-- foreign key for " + (rightOrClass.isCommonToOrgs ? "cto-" : (rightOrClass.isCommonToPools ? "ctp-" : "")) + "class \"" + rightOrClass.sectionName + "." + rightOrClass.className + "\" is implemented in" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId > 0 ? " MPC " + M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId : "") + (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId > 0 ? " Pool " + M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId : ""));
}

M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Class \"" + rightclass.sectionName + "." + rightclass.className + "\"", fileNoToUse, null, null);
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ADD CONSTRAINT");

M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + M04_Utilities.genFkName(rightclass.className, rightclass.shortName, relShortName, ddlType, thisOrgIndex, thisPoolIndex, null, null));

M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(rightclass.classIndex, "", thisPoolIndex, ddlType, M04_Utilities.genSurrogateKeyName(ddlType, rightclass.shortName, null, null, null, null), null, (rightclass.subClassIdStrSeparatePartition.numMaps > 0 ? false : true), null, null) + ")");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + rightQualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(rightOrClass.classIndex, thisPoolIndex, ddlType, M01_Globals.g_anOid, rightOrClass.aggHeadClassIdStr, null, null) + ")");
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].rlFkMaintenanceMode == M23_Relationship_Utilities.FkMaintenanceMode.efkmCascade) {
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ON DELETE CASCADE");
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced) {
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "NOT ENFORCED");
}
M00_FileWriter.printToFile(fileNoToUse, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, rightQualTabNameLdm, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, null, null, !(M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced));
}
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !leftOrClass.isCommonToOrgs & !poolSuppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since this relationship is common to MPCs and (left) class \"" + leftOrClass.sectionName + "." + leftOrClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" is common to MPCs");
}
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & (!((leftOrClass.isCommonToPools |  leftOrClass.isCommonToOrgs))) & !poolSuppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since this relationship is common to Pools and (right) class \"" + leftOrClass.sectionName + "." + leftOrClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" is common to pools");
}
} else {
if (!(poolSuppressRefIntegrity)) {
if (M03_Config.generateDdlCreateFK) {
fileNoToUse = (leftUseFileNoFk ? fileNoFk : fileNo);
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId > 0 & ! leftOrClass.isCommonToOrgs) |  (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId > 0 & ! leftOrClass.isCommonToPools)) {
M00_FileWriter.printToFile(fileNoToUse, "-- foreign key for " + (leftOrClass.isCommonToOrgs ? "cto-" : (leftOrClass.isCommonToPools ? "ctp-" : "")) + "class \"" + leftOrClass.sectionName + "." + leftOrClass.className + "\" is implemented in" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId > 0 ? " MPC " + M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId : "") + (M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId > 0 ? " Pool " + M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId : ""));
}

M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Class \"" + leftClass.sectionName + "." + leftClass.className + "\"", fileNoToUse, null, null);
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ADD CONSTRAINT");

M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + M04_Utilities.genFkName(leftClass.className, leftClass.shortName, relShortName, ddlType, thisOrgIndex, thisPoolIndex, null, null));

M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(leftClass.classIndex, "", thisPoolIndex, ddlType, M04_Utilities.genSurrogateKeyName(ddlType, leftClass.shortName, null, null, null, null), null, (leftClass.subClassIdStrSeparatePartition.numMaps > 0 ? false : true), null, null) + ")");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(1) + leftQualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(leftOrClass.classIndex, thisPoolIndex, ddlType, M01_Globals.g_anOid, leftOrClass.aggHeadClassIdStr, null, null) + ")");
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].lrFkMaintenanceMode == M23_Relationship_Utilities.FkMaintenanceMode.efkmCascade) {
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "ON DELETE CASCADE");
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced) {
M00_FileWriter.printToFile(fileNoToUse, M04_Utilities.addTab(0) + "NOT ENFORCED");
}
M00_FileWriter.printToFile(fileNoToUse, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, leftQualTabNameLdm, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, null, null, !(M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced));
}
}
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M24_Attribute.genNlsTabsForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNo, fileNo, ddlType, null, forLrt, leftFkAttrs + ", " + rightFkAttrs, ukAttrDecls, poolCommonItemsLocal);
}
// ### IF IVK ###

if ((!(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftDependentAttribute.compareTo("") == 0) |  !(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightDependentAttribute.compareTo("") == 0)) &  (!(forLrt | ! M72_DataPool.poolSupportLrt | !M23_Relationship.g_relationships.descriptors[thisRelIndex].useMqtToImplementLrt)) & poolSupportUpdates) {
// triggers to maintain derived attributes (for LRT-MQT-supported relationships this is done in MQT-triggers)
genVirtualAttrTriggerForRel(fileNoLrtSup, thisRelIndex, qualTabName, thisOrgIndex, thisPoolIndex, forLrt, M72_DataPool.poolSupportLrt, ddlType);
}
// ### ENDIF IVK ###
}

if (M01_Globals.g_genLrtSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & !poolCommonItemsLocal) {
M11_LRT.genLrtSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoClView, fileNo, fileNoLrtSup, ddlType);
}

if (M03_Config.genFksForLrtOnRelationships) {
if (genSupportForLrt & ! poolSuppressRefIntegrity) {
// ### IF IVK ###
M24_Attribute.genFksForLrtByEntity(qualTabName, qualTabNameLdm, thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, false, forLrt, null, tabPartitionType);
// ### ELSE IVK ###
//       genFksForLrtByEntity qualTabName, qualTabNameLdm, thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt
// ### ENDIF IVK ###
}
}

// ### IF IVK ###
if (genSupportForLrt) {
if (M03_Config.generatePsCopySupport) {
M82_PSCopy.genPsCopySupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType, null);
}

if (M03_Config.generateExpCopySupport) {
M85_DataFix.genExpCopySupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType, null);
}

if (orgSetProductiveTargetPoolIndex > 0) {
M86_SetProductive.genSetProdSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType, null);
}

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].noFto) {
M87_FactoryTakeOver.genFtoSupportDdlForRelationship(thisRelIndex, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType, null);
}
}

if (M03_Config.generatePsTaggingView &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged) {
M13_PSTag.genPsTagSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType, null);
}

// ### ENDIF IVK ###
if (M03_Config.generateLogChangeView & ! M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged & M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange & M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChangeInView) {
M18_LogChange.genLogChangeSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, null);
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange &  M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChangeAutoMaint) {
M18_LogChange.genLogChangeAutoMaintSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, null, forLrt);
}

// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.supportArchivePool & M72_DataPool.poolSupportsArchiving(thisPoolId)) {
M16_Archive.genArchiveSupportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoArc, ddlType);
}

GenXmlExport:
if (M03_Config.generateXmlExportSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].supportXmlExport & (ddlType == M01_Common.DdlTypeId.edtLdm |  thisPoolId == -1 | poolSupportXmlExport)) {
M14_XMLExport.genXmlExportDdlForRelationship(thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType);
}

// ### ENDIF IVK ###
// relationship may be a copy taken from g_relationships! make sure we update the original source!
M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex].isLdmCsvExported = true;
M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex].isCtoAliasCreated = true;

M23_Relationship.g_relationships.descriptors[thisRelIndex].isLdmCsvExported = true;// safe is safe ;-)
M23_Relationship.g_relationships.descriptors[thisRelIndex].isCtoAliasCreated = true;
if (genSupportForLrt) {
M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex].isLdmLrtCsvExported = true;
M23_Relationship.g_relationships.descriptors[thisRelIndex].isLdmLrtCsvExported = true;// safe is safe ;-)
}
// ### IF IVK ###

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isDisallowedCountries |  M23_Relationship.g_relationships.descriptors[thisRelIndex].isAllowedCountries) {
genAllowedCountriesFunction(thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType);
genAllowedCountriesView(thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType);
}
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
M00_FileWriter.closeFile(fileNoFk);
// ### IF IVK ###
M00_FileWriter.closeFile(fileNoAc);
// ### ENDIF IVK ###
M00_FileWriter.closeFile(fileNoLrt);
M00_FileWriter.closeFile(fileNoLrtView);
M00_FileWriter.closeFile(fileNoClView);
M00_FileWriter.closeFile(fileNoLrtSup);
// ### IF IVK ###
M00_FileWriter.closeFile(fileNoSetProd);
M00_FileWriter.closeFile(fileNoSetProdCl);
M00_FileWriter.closeFile(fileNoFto);
M00_FileWriter.closeFile(fileNoPsCopy);
M00_FileWriter.closeFile(fileNoPsCopy2);
M00_FileWriter.closeFile(fileNoExpCopy);
M00_FileWriter.closeFile(fileNoPs);
// ### ENDIF IVK ###
M00_FileWriter.closeFile(fileNoLc);
// ### IF IVK ###
M00_FileWriter.closeFile(fileNoArc);
M00_FileWriter.closeFile(fileNoXmlV);
M00_FileWriter.closeFile(fileNoXmlF);
// ### ENDIF IVK ###
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
private static void genVirtualAttrTriggerForRelAndClass(int fileNo, int thisRelIndex, String qualTabName, int attrIndex, int refClassIndex, int refClassOrParentIndex, String refColumnName,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean M72_DataPool.poolSupportLrtW, Integer ddlTypeW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean M72_DataPool.poolSupportLrt; 
if (M72_DataPool.poolSupportLrtW == null) {
M72_DataPool.poolSupportLrt = true;
} else {
M72_DataPool.poolSupportLrt = M72_DataPool.poolSupportLrtW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String qualTriggerName;
String qualRefTabName;
String qualRefTabNameLrt;
String attrName;
M24_Attribute_Utilities.AttributeListTransformation transformation;
String refClassName;
String refSectionName;

attrName = M04_Utilities.genAttrNameByIndex(attrIndex, ddlType);

refClassName = M22_Class.g_classes.descriptors[refClassIndex].className;
refSectionName = M22_Class.g_classes.descriptors[refClassIndex].sectionName;

qualRefTabName = M04_Utilities.genQualTabNameByClassIndex(refClassOrParentIndex, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, null, null, null, null, null);
qualRefTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(refClassOrParentIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, "VA_INS", M04_Utilities.ObjNameDelimMode.eondmSuffix, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger for maintaining virtual column \"" + attrName + "\" in table \"" + qualRefTabName + "\" (ACM-Class \"" + refSectionName + "." + refClassName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "update virtual column in " + (forLrt ? "private " : (M72_DataPool.poolSupportLrt ? "public " : "")) + "table \"" + qualRefTabName + "\"", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualRefTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt : ""), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + refColumnName);

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, "VA_UPD", M04_Utilities.ObjNameDelimMode.eondmSuffix, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger for maintaining virtual column \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftDependentAttribute + "\" in table \"" + qualRefTabName + "\" (ACM-Class \"" + refSectionName + "." + refClassName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "update virtual column in " + (forLrt ? "private " : (M72_DataPool.poolSupportLrt ? "public " : "")) + "table \"" + qualRefTabName + "\"", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualRefTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt : ""), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + refColumnName);

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (!(forLrt &  M72_DataPool.poolSupportLrt)) {
M11_LRT.genProcSectionHeader(fileNo, "update virtual column in private table \"" + qualRefTabNameLrt + "\"", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualRefTabNameLrt + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", "T." + M01_Globals.g_anInLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + refColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_Globals.gc_newRecordName + "." + refColumnName + " <> " + M01_Globals.gc_oldRecordName + "." + refColumnName + " THEN");

M11_LRT.genProcSectionHeader(fileNo, "update virtual column in " + (forLrt ? "private " : (M72_DataPool.poolSupportLrt ? "public " : "")) + "table \"" + qualRefTabName + "\"", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualRefTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt : ""), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + refColumnName);

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

if (!(forLrt &  M72_DataPool.poolSupportLrt)) {
M11_LRT.genProcSectionHeader(fileNo, "update virtual column in private table \"" + qualRefTabNameLrt + "\"", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualRefTabNameLrt + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", "T." + M01_Globals.g_anInLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + refColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, "VA_DEL", M04_Utilities.ObjNameDelimMode.eondmSuffix, null);

M22_Class_Utilities.printSectionHeader("Delete-Trigger for maintaining virtual column \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftDependentAttribute + "\" in table \"" + qualRefTabName + "\" (ACM-Class \"" + refSectionName + "." + refClassName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER DELETE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "update virtual column in " + (forLrt ? "private " : (M72_DataPool.poolSupportLrt ? "public " : "")) + "table \"" + qualRefTabName + "\"", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualRefTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt : ""), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + attrName + " = " + M04_Utilities.transformAttrName(attrName, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, M24_Attribute.g_attributes.descriptors[attrIndex].valueTypeIndex, transformation, ddlType, null, null, null, true, attrIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anInLrt);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}


private static void genVirtualAttrTriggerForRel(int fileNo, int thisRelIndex, String qualTabName,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean M72_DataPool.poolSupportLrtW, Integer ddlTypeW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean M72_DataPool.poolSupportLrt; 
if (M72_DataPool.poolSupportLrtW == null) {
M72_DataPool.poolSupportLrt = true;
} else {
M72_DataPool.poolSupportLrt = M72_DataPool.poolSupportLrtW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

// relationships are never updated; thus there is no need for an update trigger
if (!(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftDependentAttribute.compareTo("") == 0)) {
genVirtualAttrTriggerForRelAndClass(fileNo, M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, qualTabName, M24_Attribute.getAttributeIndexByName(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftDependentAttribute), M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex].orMappingSuperClassIndex, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex].shortName, null, null, null, null), thisOrgIndex, thisPoolIndex, forLrt, M72_DataPool.poolSupportLrt, ddlType);
}
}


// ### ENDIF IVK ###
public static void genTransformedAttrDeclForRelationshipsByClassWithColReuse(int thisClassIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Boolean forSubClassW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean timeVaryingRelsW, Integer outputModeW, Integer indentW, Boolean addCommaW, Boolean includeReusedRelsW) {
boolean forSubClass; 
if (forSubClassW == null) {
forSubClass = false;
} else {
forSubClass = forSubClassW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

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

boolean timeVaryingRels; 
if (timeVaryingRelsW == null) {
timeVaryingRels = false;
} else {
timeVaryingRels = timeVaryingRelsW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

boolean includeReusedRels; 
if (includeReusedRelsW == null) {
includeReusedRels = false;
} else {
includeReusedRels = includeReusedRelsW;
}

int i;
String attrSpecifics;

//On Error GoTo ErrorExit 

String relShortName;
String relLdmShortName;
String relDirectedShortName;
int effectiveRelIndex;
boolean classHasNoIdentity;
boolean supportedExpRel;
boolean supportedNonGenRel;

// determine number of foreign key columns in this class
int numFkAttrs;
numFkAttrs = 0;

classHasNoIdentity = M22_Class.g_classes.descriptors[thisClassIndex].hasNoIdentity;
for (i = 1; i <= 1; i += (1)) {
//determine supported Expression Gen Relation
if (timeVaryingRels &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isTimeVarying & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isMdsExpressionRel & !classHasNoIdentity) {
supportedExpRel = true;
} else {
supportedExpRel = false;
}
//determine all non-Gen Relation (if generating nonGen, then only process non-TimeVarying attributes, or those for a noIdentity class)
if (!(timeVaryingRels &  (!(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isTimeVarying |  classHasNoIdentity)))) {
supportedNonGenRel = true;
} else {
supportedNonGenRel = false;
}
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNl) : true) &  M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 & (supportedNonGenRel |  supportedExpRel)) {
if (!(M03_Config.reuseRelationships |  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex <= 0 | includeReusedRels)) {
numFkAttrs = numFkAttrs + 1;
}
} else if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNl) : true) &  M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1 & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 & (supportedNonGenRel |  supportedExpRel)) {
if (!(M03_Config.reuseRelationships |  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex <= 0 | includeReusedRels)) {
numFkAttrs = numFkAttrs + 1;
}
}
}

for (i = 1; i <= 1; i += (1)) {
//determine supported Expression Gen Relation
if (timeVaryingRels &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isTimeVarying & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isMdsExpressionRel & !classHasNoIdentity) {
supportedExpRel = true;
} else {
supportedExpRel = false;
}
//determine all non-Gen Relation (if generating nonGen, then only process non-TimeVarying attributes, or those for a noIdentity class)
if (!(timeVaryingRels &  (!(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isTimeVarying |  classHasNoIdentity)))) {
supportedNonGenRel = true;
} else {
supportedNonGenRel = false;
}
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNl) : true) &  M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 & (supportedNonGenRel |  supportedExpRel)) {
attrSpecifics = (forSubClass |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality == 0) ? "" : "NOT NULL");
M22_Class_Utilities.printSectionHeader("\"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relName + "\" (" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassName + "[" + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo, outputMode, null);
M22_Class_Utilities.printComment("Relationship \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassName + "\"", fileNo, outputMode, null);
if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 & !includeReusedRels) {
M22_Class_Utilities.printComment("reusing foreign key for relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassName + "\"", fileNo, outputMode, null);
} else {
effectiveRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex);
relShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].effectiveShortName;
relDirectedShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].lrShortRelName;
relLdmShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].lrLdmRelName;
// FIXME: Parameter forLRTtab needs to be set ??
M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, false, false, true, null, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isUserTransactional, null, null, outputMode, indent, null, null, null, null, null);
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightEntityIndex, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].useLrLdmRelName ? relLdmShortName : relShortName + relDirectedShortName), !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].useLrLdmRelName), M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma |  (numFkAttrs > 1));
// ### ELSE IVK ###
//           genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
//             .relIndex, IIf(.useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), Not .useLrLdmRelName, _
//             attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
// ### ENDIF IVK ###
numFkAttrs = numFkAttrs - 1;
}
}
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNl) : true) &  M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1 & M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 & (supportedNonGenRel |  supportedExpRel)) {
attrSpecifics = (forSubClass |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0) ? "" : "NOT NULL");
M22_Class_Utilities.printSectionHeader("\"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relName + "\" (" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassName + "[" + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo, outputMode, null);
M22_Class_Utilities.printComment("Relationship \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rlRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].rightClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftClassName + "\"", fileNo, outputMode, null);
if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 & !includeReusedRels) {
M22_Class_Utilities.printComment("reusing foreign key for relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassName + "\"", fileNo, outputMode, null);
} else {
effectiveRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].reusedRelIndex : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex);
relShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].effectiveShortName;
relDirectedShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].rlShortRelName;
relLdmShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].rlLdmRelName;

// FIXME: Parameter forLRTtab needs to be set ??
M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, false, true, null, null, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isUserTransactional, null, null, outputMode, indent, null, null, null, null, true);
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].leftEntityIndex, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].useRlLdmRelName ? relLdmShortName : relShortName + relDirectedShortName), !(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].useRlLdmRelName), M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[i].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma |  (numFkAttrs > 1));
// ### ELSE IVK ###
//           genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
//             .relIndex, IIf(.useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), Not .useRlLdmRelName, _
//             attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
// ### ENDIF IVK ###
numFkAttrs = numFkAttrs - 1;
}
}
NextI:
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclForRelationshipsByRelWithColReuse(int thisRelIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer outputModeW, Integer indentW, Boolean addCommaW, Boolean includeReusedRelsW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

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

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

boolean includeReusedRels; 
if (includeReusedRelsW == null) {
includeReusedRels = false;
} else {
includeReusedRels = includeReusedRelsW;
}

int i;
String attrSpecifics;

String relShortName;
String relLdmShortName;
String relDirectedShortName;
int effectiveRelIndex;

//On Error GoTo ErrorExit 

// determine number of foreign key columns in this relationship
int numFkAttrs;
numFkAttrs = 0;
for (i = 1; i <= 1; i += (1)) {
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNl) : true) &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1) {
if (!(M03_Config.reuseRelationships |  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex <= 0 | includeReusedRels)) {
numFkAttrs = numFkAttrs + 1;
}
} else if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNl) : true) &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1 & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1) {
if (!(M03_Config.reuseRelationships |  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex <= 0 | includeReusedRels)) {
numFkAttrs = numFkAttrs + 1;
}
}
}

for (i = 1; i <= 1; i += (1)) {
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNl) : true) &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1) {
attrSpecifics = (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality == 0 ? "" : "NOT NULL");
M22_Class_Utilities.printSectionHeader("\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relName + "\" (" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo, outputMode, null);
M22_Class_Utilities.printComment("Relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassName + "\"", fileNo, outputMode, null);
if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 & !includeReusedRels) {
M22_Class_Utilities.printComment("reusing foreign key for relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassName + "\"", fileNo, outputMode, null);
} else {
effectiveRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex : M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex);
relShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].effectiveShortName;
relDirectedShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].lrShortRelName;
relLdmShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].lrLdmRelName;
// FIXME: Parameter forLRTtab needs to be set ??
M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex, transformation, tabColumns, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, false, false, true, null, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isUserTransactional, null, null, outputMode, indent, null, null, null, null, null);
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightEntityIndex, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].useLrLdmRelName ? relLdmShortName : relShortName + relDirectedShortName), !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].useLrLdmRelName), M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma |  (numFkAttrs > 1));
// ### ELSE IVK ###
//           genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
//             .relIndex, IIf(.useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
//             Not .useLrLdmRelName, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
// ### ENDIF IVK ###
numFkAttrs = numFkAttrs - 1;
}
}
if ((M03_Config.supportNlForRelationships ? !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNl) : true) &  M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1 & M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1) {
attrSpecifics = (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0 ? "" : "NOT NULL");
M22_Class_Utilities.printSectionHeader("\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relName + "\" (" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minLeftCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1 ? "1" : "n") + "] <-> " + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassName + "[" + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality == 0 |  (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality == 1 &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality != 1) ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].minRightCardinality + ".." : "") + (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1 ? "1" : "m") + "])", fileNo, outputMode, null);
M22_Class_Utilities.printComment("Relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rlRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].rightClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftClassName + "\"", fileNo, outputMode, null);
if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 & !includeReusedRels) {
M22_Class_Utilities.printComment("reusing foreign key for relationship \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex].rightClassName + "\"", fileNo, outputMode, null);
} else {
effectiveRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].reusedRelIndex : M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex);
relShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].effectiveShortName;
relDirectedShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].rlShortRelName;
relLdmShortName = M23_Relationship.g_relationships.descriptors[effectiveRelIndex].rlLdmRelName;

// FIXME: Parameter forLRTtab needs to be set ??
M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex, transformation, tabColumns, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, false, true, null, null, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isUserTransactional, null, null, outputMode, indent, null, null, null, null, true);
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].leftEntityIndex, M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].useRlLdmRelName ? relLdmShortName : relShortName + relDirectedShortName), !(M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].useRlLdmRelName), M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].relRefs.refs[i].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma |  (numFkAttrs > 1));
// ### ELSE IVK ###
//           genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
//             .relIndex, IIf(.useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
//             Not .useRlLdmRelName, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
// ### ENDIF IVK ###
numFkAttrs = numFkAttrs - 1;
}
}
NextI:
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}





public static void genRelationshipsDdl(Integer ddlType) {
int thisRelIndex;
int thisOrgIndex;
int thisPoolIndex;

M23_Relationship.resetRelationshipsCsvExported();

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
for (thisRelIndex = 1; thisRelIndex <= 1; thisRelIndex += (1)) {
genRelationshipDdl(thisRelIndex, null, null, M01_Common.DdlTypeId.edtLdm);
}

M23_Relationship.resetRelationshipsCsvExported();
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisRelIndex = 1; thisRelIndex <= 1; thisRelIndex += (1)) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToOrgs) {
genRelationshipDdl(thisRelIndex, null, null, M01_Common.DdlTypeId.edtPdm);

// if there is some data pool which locally implements this relationship, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genRelationshipDdl(thisRelIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}

} else {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isCommonToPools) {
genRelationshipDdl(thisRelIndex, thisOrgIndex, null, M01_Common.DdlTypeId.edtPdm);

// if there is some data pool which locally implements this class, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genRelationshipDdl(thisRelIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}

} else {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genRelationshipDdl(thisRelIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}

M23_Relationship.resetRelationshipsCsvExported();
}
}


public static void dropRelationshipsCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, M01_Globals.g_targetDir, processingStepAcmCsv, onlyIfEmpty, "ACM");
}


public static void genRelationshipAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, processingStepAcmCsv, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
if (!(M23_Relationship.g_relationships.descriptors[i].notAcmRelated)) {
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].shortName.toUpperCase());
M00_FileWriter.printToFile(fileNo, "\",\"R\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].relIdStr + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].i18nId + "\",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isCommonToPools ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].supportXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].useXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (!(M23_Relationship.g_relationships.descriptors[i].aggHeadClassIdStr.compareTo("") == 0) ? "\"" + M23_Relationship.g_relationships.descriptors[i].aggHeadClassIdStr + "\"" : "") + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].noFto ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isUserTransactional ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isLrtMeta ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isUserTransactional &  M23_Relationship.g_relationships.descriptors[i].useMqtToImplementLrt ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (false ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (!(M23_Relationship.g_relationships.descriptors[i].lrtActivationStatusMode.compareTo("") == 0) ? "\"" + M23_Relationship.g_relationships.descriptors[i].lrtActivationStatusMode + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M23_Relationship.g_relationships.descriptors[i].lrtClassification.compareTo("") == 0) ? "\"" + M23_Relationship.g_relationships.descriptors[i].lrtClassification + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isSubjectToArchiving ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "0,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isPsTagged ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isPsForming ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].logLastChange ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, "0,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isSubjectToPreisDurchschuss ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isUserTransactional &  M23_Relationship.g_relationships.descriptors[i].hasOrganizationSpecificReference ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].ignoreForChangelog ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, "0,0,,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, ",,,,,,");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].reuseName.compareTo("") == 0 ? "" : "\"" + M23_Relationship.g_relationships.descriptors[i].reuseName.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].reuseShortName.compareTo("") == 0 ? "" : "\"" + M23_Relationship.g_relationships.descriptors[i].reuseShortName.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].isNotEnforced ? M01_LDM.gc_dbFalse : M01_LDM.gc_dbTrue) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].rlShortRelName.compareTo("") == 0 ? "" : "\"" + M23_Relationship.g_relationships.descriptors[i].rlShortRelName.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, String.valueOf(M23_Relationship.g_relationships.descriptors[i].minLeftCardinality) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality > 0 ? M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality + "" : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].leftClassName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M23_Relationship.g_relationships.descriptors[i].leftEntityType) + "\",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].lrShortRelName.compareTo("") == 0 ? "" : "\"" + M23_Relationship.g_relationships.descriptors[i].lrShortRelName.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, String.valueOf(M23_Relationship.g_relationships.descriptors[i].minRightCardinality) + ",");
M00_FileWriter.printToFile(fileNo, (M23_Relationship.g_relationships.descriptors[i].maxRightCardinality > 0 ? M23_Relationship.g_relationships.descriptors[i].maxRightCardinality + "" : "") + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].rightClassName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M23_Relationship.g_relationships.descriptors[i].rightEntityType) + "\"," + M04_Utilities.getCsvTrailer(0));
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


public static void evalRelationships() {
int i;
int j;

M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;

for (i = 1; i <= 1; i += (1)) {
// determine TableSpaces
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData = (!(M23_Relationship.g_relationships.descriptors[i].tabSpaceData.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M23_Relationship.g_relationships.descriptors[i].tabSpaceData) : -1);
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexIndex = (!(M23_Relationship.g_relationships.descriptors[i].tabSpaceIndex.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M23_Relationship.g_relationships.descriptors[i].tabSpaceIndex) : -1);
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexLong = (!(M23_Relationship.g_relationships.descriptors[i].tabSpaceLong.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M23_Relationship.g_relationships.descriptors[i].tabSpaceLong) : -1);
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexNl = (!(M23_Relationship.g_relationships.descriptors[i].tabSpaceNl.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M23_Relationship.g_relationships.descriptors[i].tabSpaceNl) : -1);
M23_Relationship.g_relationships.descriptors[i].useValueCompression = M23_Relationship.g_relationships.descriptors[i].useValueCompression &  M03_Config.dbCompressValues;

// initialize variables
M23_Relationship.g_relationships.descriptors[i].hasLabel = false;
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].hasIsNationalInclSubClasses = M23_Relationship.g_relationships.descriptors[i].isNationalizable;
// ### ENDIF IVK ###

M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex = -1;
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndexExact = -1;
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIdStr = "";
if (!(M23_Relationship.g_relationships.descriptors[i].notAcmRelated)) {
if (!(M23_Relationship.g_relationships.descriptors[i].aggHeadSection.compareTo("") == 0) &  !(M23_Relationship.g_relationships.descriptors[i].aggHeadName.compareTo("") == 0)) {
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex = M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[i].aggHeadSection, M23_Relationship.g_relationships.descriptors[i].aggHeadName, null);
if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex <= 0) {
M04_Utilities.logMsg("unable to identify aggregate head class '" + M23_Relationship.g_relationships.descriptors[i].aggHeadSection + "." + M23_Relationship.g_relationships.descriptors[i].aggHeadName + "'", M01_Common.LogLevel.ellError, null, null, null);
} else {
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIdStr = M22_Class_Utilities.getClassIdByClassIndex(M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex);
}
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndexExact = M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex;
}
}

// determine references to indexes
M23_Relationship.g_relationships.descriptors[i].indexRefs.numRefs = 0;
for (j = 1; j <= 1; j += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M76_Index.g_indexes.descriptors[j].sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == M76_Index.g_indexes.descriptors[j].className.toUpperCase()) {
M23_Relationship.g_relationships.descriptors[i].indexRefs.refs[(M76_Index_Utilities.allocIndexDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].indexRefs))] = j;
}
}

// determine reference to section
M23_Relationship.g_relationships.descriptors[i].sectionIndex = M20_Section.getSectionIndexByName(M23_Relationship.g_relationships.descriptors[i].sectionName, null);
if (M23_Relationship.g_relationships.descriptors[i].sectionIndex > 0) {
M23_Relationship.g_relationships.descriptors[i].sectionShortName = M20_Section.g_sections.descriptors[M23_Relationship.g_relationships.descriptors[i].sectionIndex].shortName;
}

if (M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData > 0) {
if (M73_TableSpace.g_tableSpaces.descriptors[M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData].category == M73_TableSpace_Utilities.TabSpaceCategory.tscSms) {
if (M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexIndex > 0 &  M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexIndex != M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData) {
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexIndex = M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData;
M04_Utilities.logMsg("index table space \"" + M23_Relationship.g_relationships.descriptors[i].tabSpaceIndex + "\" for relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
if (M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexLong > 0 &  M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexLong != M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData) {
M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexLong = M23_Relationship.g_relationships.descriptors[i].tabSpaceIndexData;
M04_Utilities.logMsg("long table space \"" + M23_Relationship.g_relationships.descriptors[i].tabSpaceLong + "\" for relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
}
}

// confirm that relationship name is unique
for (j = 1; j <= 1; j += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase()) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\" is not unque", M01_Common.LogLevel.ellFatal, null, null, null);
}
}
// ### IF IVK ###

// determine whether class supports XML-export
if (M23_Relationship.g_relationships.descriptors[i].noXmlExport) {
M23_Relationship.g_relationships.descriptors[i].supportXmlExport = false;
} else if (M23_Relationship.g_relationships.descriptors[i].isCommonToPools |  M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs) {
M23_Relationship.g_relationships.descriptors[i].supportXmlExport = true;
} else {
if (M23_Relationship.g_relationships.descriptors[i].specificToPool >= 0) {
if (M72_DataPool.g_pools.descriptors[M23_Relationship.g_relationships.descriptors[i].specificToPool].supportXmlExport) {
M23_Relationship.g_relationships.descriptors[i].supportXmlExport = true;
}
} else {
M23_Relationship.g_relationships.descriptors[i].supportXmlExport = true;
}
}
// ### ENDIF IVK ###
}

for (i = 1; i <= 1; i += (1)) {
// verify consistency of aggregate heads with object relational mapping
if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex != M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex) {
M04_Utilities.logMsg("potential inconsistency: aggregate head of relationship '" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "' is not identical to its 'OR-mapping parent class' " + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].className + "'", M01_Common.LogLevel.ellInfo, null, null, null);
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex;
M23_Relationship.g_relationships.descriptors[i].aggHeadSection = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].sectionName;
M23_Relationship.g_relationships.descriptors[i].aggHeadName = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].className;
M23_Relationship.g_relationships.descriptors[i].aggHeadClassIdStr = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].classIdStr;
}
}
// ### IF IVK ###

// determine whether aggregate head is price assignment
if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndexExact > 0) {
M23_Relationship.g_relationships.descriptors[i].hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndexExact].hasPriceAssignmentSubClass;
} else if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex > 0) {
M23_Relationship.g_relationships.descriptors[i].hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex].hasPriceAssignmentSubClass;
}
// ### ENDIF IVK ###
}

for (i = 1; i <= 1; i += (1)) {
M23_Relationship.g_relationships.descriptors[i].relIndex = i;

if (M23_Relationship.g_relationships.descriptors[i].fkReferenceOrgId > 0) {
M23_Relationship.g_relationships.descriptors[i].fkReferenceOrgIndex = M71_Org.getOrgIndexById(M23_Relationship.g_relationships.descriptors[i].fkReferenceOrgId);
}
if (M23_Relationship.g_relationships.descriptors[i].fkReferencePoolId > 0) {
M23_Relationship.g_relationships.descriptors[i].fkReferencePoolIndex = M71_Org.getOrgIndexById(M23_Relationship.g_relationships.descriptors[i].fkReferencePoolId);
}
// determine relationship ID as string
M23_Relationship.g_relationships.descriptors[i].relIdStr = M23_Relationship_Utilities.getRelIdByIndex(i);

if (M23_Relationship.g_relationships.descriptors[i].isUserTransactional &  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools |  M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs)) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "has stereotype <lrt> but is common to " + (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs ? "organizations (cto)" : "pools (ctp)") + " - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].isUserTransactional = false;
}

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].isPsForming & ! M23_Relationship.g_relationships.descriptors[i].isUserTransactional & !M23_Relationship.g_relationships.descriptors[i].isCommonToPools) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "relationship is 'PS-forming' but does not have stereotype <lrt>", M01_Common.LogLevel.ellInfo, null, null, null);
}

// ### ENDIF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].isUserTransactional &  M23_Relationship.g_relationships.descriptors[i].logLastChange & !M23_Relationship.g_relationships.descriptors[i].logLastChangeInView) {
M04_Utilities.logMsg("inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "relationship has stereotypes <logChange> and <lrt> but does not support 'logChangeInView' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].logLastChangeInView = true;
}

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].isPsTagged &  M23_Relationship.g_relationships.descriptors[i].logLastChange & !M23_Relationship.g_relationships.descriptors[i].logLastChangeInView) {
M04_Utilities.logMsg("inconsistency with class \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "relationship has stereotypes <logChange> and <ps> but does not support 'logChangeInView' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].logLastChangeInView = true;
}

// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[i].leftEntityIndex = M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[i].leftClassSectionName, M23_Relationship.g_relationships.descriptors[i].leftClassName, true);
if (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex > 0) {
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].leftIsDivision = (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex == M01_Globals_IVK.g_classIndexDivision);
// ### ENDIF IVK ###
leftClass = M22_Class.getClassByIndex(M23_Relationship.g_relationships.descriptors[i].leftEntityIndex);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].leftIsSubjectToArchiving = leftClass.isSubjectToArchiving;
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[i].leftEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
M23_Relationship.g_relationships.descriptors[i].leftEntityShortName = leftClass.shortName;
} else {
M23_Relationship.g_relationships.descriptors[i].leftEntityIndex = M23_Relationship.getRelIndexByName(M23_Relationship.g_relationships.descriptors[i].leftClassSectionName, M23_Relationship.g_relationships.descriptors[i].leftClassName, null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].leftIsSubjectToArchiving = M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isSubjectToArchiving;
// ### ENDIF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex > 0) {
M23_Relationship.g_relationships.descriptors[i].leftEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship;
M23_Relationship.g_relationships.descriptors[i].leftEntityShortName = M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].shortName;
}
}

M23_Relationship.g_relationships.descriptors[i].rightEntityIndex = M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[i].rightClassSectionName, M23_Relationship.g_relationships.descriptors[i].rightClassName, true);
if (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex > 0) {
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].rightIsDivision = (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex == M01_Globals_IVK.g_classIndexDivision);
// ### ENDIF IVK ###
rightclass = M22_Class.getClassByIndex(M23_Relationship.g_relationships.descriptors[i].rightEntityIndex);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].rightIsSubjectToArchiving = rightclass.isSubjectToArchiving;
// ### ENDIF IVK ###
M23_Relationship.g_relationships.descriptors[i].rightEntityIndex = M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[i].rightClassSectionName, M23_Relationship.g_relationships.descriptors[i].rightClassName, true);
M23_Relationship.g_relationships.descriptors[i].rightEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
M23_Relationship.g_relationships.descriptors[i].rightEntityShortName = rightclass.shortName;
} else {
M23_Relationship.g_relationships.descriptors[i].rightEntityIndex = M23_Relationship.getRelIndexByName(M23_Relationship.g_relationships.descriptors[i].rightClassSectionName, M23_Relationship.g_relationships.descriptors[i].rightClassName, null);
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].rightIsSubjectToArchiving = M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isSubjectToArchiving;
// ### ENDIF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex > 0) {
M23_Relationship.g_relationships.descriptors[i].rightEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship;
M23_Relationship.g_relationships.descriptors[i].rightEntityShortName = M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].shortName;
}
}

M23_Relationship.g_relationships.descriptors[i].attrRefs.numDescriptors = 0;
// ### IF IVK ###
M23_Relationship.g_relationships.descriptors[i].isSubjectToArchiving = M23_Relationship.g_relationships.descriptors[i].leftIsSubjectToArchiving |  M23_Relationship.g_relationships.descriptors[i].rightIsSubjectToArchiving;
// ### ENDIF IVK ###

if (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex <= 0) {
M04_Utilities.logMsg("Unable to identify 'right' class \"" + M23_Relationship.g_relationships.descriptors[i].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[i].rightClassName + "\" of relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\"", (M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 ? M01_Common.LogLevel.ellWarning : M01_Common.LogLevel.ellError), null, null, null);
goto NextI;
}
if (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex <= 0) {
M04_Utilities.logMsg("Unable to identify 'left' class \"" + M23_Relationship.g_relationships.descriptors[i].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[i].leftClassName + "\" of relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\"", (M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 ? M01_Common.LogLevel.ellWarning : M01_Common.LogLevel.ellError), null, null, null);
goto NextI;
}

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].specificToOrgId >= 0 & ! M23_Relationship.g_relationships.descriptors[i].noFto) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is specific to MPC " + M23_Relationship.g_relationships.descriptors[i].specificToOrgId + " but does not have stereotype <nt2m> (no transfer to MPC) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].noFto = true;
} else if (M23_Relationship.g_relationships.descriptors[i].specificToPool >= 0 & ! M23_Relationship.g_relationships.descriptors[i].noTransferToProduction) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is specific to pool " + M23_Relationship.g_relationships.descriptors[i].specificToPool + " but does not have stereotype <nt2p> (no transfer to production) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].noTransferToProduction = true;
}

if (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs & ! M23_Relationship.g_relationships.descriptors[i].noFto) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is common to organizations (cto) but does not have stereotype <nt2m> (no transfer to MPC) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].noFto = true;
} else if (M23_Relationship.g_relationships.descriptors[i].isCommonToPools & ! M23_Relationship.g_relationships.descriptors[i].noTransferToProduction) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is common to pools (ctp) but does not have stereotype <nt2p> (no transfer to production) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].noTransferToProduction = true;
}

if (leftClass.noFto &  rightclass.noFto & !M23_Relationship.g_relationships.descriptors[i].noFto) {
M04_Utilities.logMsg("inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred classes \"" + leftClass.sectionName + "." + leftClass.className + "\" and " + "\"" + rightclass.sectionName + "." + rightclass.className + "\" have stereotype <nt2m> but relationship has not", M01_Common.LogLevel.ellWarning, null, null, null);
}

if (leftClass.noTransferToProduction &  rightclass.noTransferToProduction & !M23_Relationship.g_relationships.descriptors[i].noTransferToProduction) {
M04_Utilities.logMsg("inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred classes \"" + leftClass.sectionName + "." + leftClass.className + "\" and " + "\"" + rightclass.sectionName + "." + rightclass.className + "\" have stereotype <nt2p> but relationship has not", M01_Common.LogLevel.ellWarning, null, null, null);
}

// check if relationship needs to be considered PS-tagged
M23_Relationship.g_relationships.descriptors[i].isPsTagged = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged;

// ### ENDIF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 &  leftClass.isUserTransactional & !rightclass.isUserTransactional) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred class \"" + leftClass.sectionName + "." + leftClass.className + "\" is user-transactional " + "but referred class \"" + rightclass.sectionName + "." + rightclass.className + "\" is not", M01_Common.LogLevel.ellWarning, null, null, null);
} else if (M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 &  rightclass.isUserTransactional & !leftClass.isUserTransactional) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred class \"" + rightclass.sectionName + "." + rightclass.className + "\" is user-transactional " + "but referred class \"" + leftClass.sectionName + "." + leftClass.className + "\" is not", M01_Common.LogLevel.ellWarning, null, null, null);
} else if (M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 &  leftClass.isUserTransactional & !M23_Relationship.g_relationships.descriptors[i].isUserTransactional) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred class \"" + leftClass.sectionName + "." + leftClass.className + "\" is user-transactional " + "but relationship is not", M01_Common.LogLevel.ellWarning, null, null, null);
} else if (M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 &  rightclass.isUserTransactional & !M23_Relationship.g_relationships.descriptors[i].isUserTransactional) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred class \"" + rightclass.sectionName + "." + rightclass.className + "\" is user-transactional " + "but relationship is not", M01_Common.LogLevel.ellWarning, null, null, null);
}
if (leftClass.isCommonToOrgs == rightclass.isCommonToOrgs &  leftClass.isCommonToOrgs != M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred classes \"" + leftClass.sectionName + "." + leftClass.className + "\" and \"" + rightclass.sectionName + "." + rightclass.className + "\" are " + (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs ? "not " : "") + "common to MPCs " + "but relationship is" + (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs ? "" : " not"), M01_Common.LogLevel.ellWarning, null, null, null);
}
if (leftClass.isCommonToPools == rightclass.isCommonToPools &  leftClass.isCommonToPools != M23_Relationship.g_relationships.descriptors[i].isCommonToPools) {
M04_Utilities.logMsg("potential inconsistency with relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "referred classes \"" + leftClass.sectionName + "." + leftClass.className + "\" and \"" + rightclass.sectionName + "." + rightclass.className + "\" are " + (M23_Relationship.g_relationships.descriptors[i].isCommonToPools ? "not " : "") + "common to Pools " + "but relationship is" + (M23_Relationship.g_relationships.descriptors[i].isCommonToPools ? "" : " not"), M01_Common.LogLevel.ellWarning, null, null, null);
}

if ((!(M03_Config.supportNlForRelationships)) |  (!(M23_Relationship.g_relationships.descriptors[i].isNl))) {
if ((M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1)) {
M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].numRelBasedFkAttrs = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].numRelBasedFkAttrs + 1;
} else if ((M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1)) {
M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].numRelBasedFkAttrs = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].numRelBasedFkAttrs + 1;
}
}

// ### IF IVK ###
// Fixme: get rid of hard coded relatioship names
if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase(), "DISALLOWEDCOUNTRIESLIST") != 0) {
M23_Relationship.g_relationships.descriptors[i].isDisallowedCountriesList = (M00_Helper.inStr(leftClass.className.toUpperCase(), "COUNTRYSPEC") ? M01_Common.RelNavigationMode.ernmLeft : M01_Common.RelNavigationMode.ernmRight);
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase(), "ALLOWEDCOUNTRIESLIST")) {
M23_Relationship.g_relationships.descriptors[i].isAllowedCountriesList = (M00_Helper.inStr(leftClass.className.toUpperCase(), "COUNTRYSPEC") ? M01_Common.RelNavigationMode.ernmLeft : M01_Common.RelNavigationMode.ernmRight);
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase(), "DISALLOWEDCOUNTRIES")) {
M23_Relationship.g_relationships.descriptors[i].isDisallowedCountries = (M00_Helper.inStr(leftClass.className.toUpperCase(), "COUNTRYSPEC") ? M01_Common.RelNavigationMode.ernmLeft : M01_Common.RelNavigationMode.ernmRight);
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase(), "ALLOWEDCOUNTRIES")) {
M23_Relationship.g_relationships.descriptors[i].isAllowedCountries = (M00_Helper.inStr(leftClass.className.toUpperCase(), "COUNTRYSPEC") ? M01_Common.RelNavigationMode.ernmLeft : M01_Common.RelNavigationMode.ernmRight);
}

if (!(M23_Relationship.g_relationships.descriptors[i].navPathStrToDivision.compareTo("") == 0)) {
M23_Relationship_Utilities.genNavPathForRelationship(i, M23_Relationship.g_relationships.descriptors[i].navPathToDiv, M23_Relationship.g_relationships.descriptors[i].navPathStrToDivision);
}

// ### ENDIF IVK ###
M22_Class.addAggChildRelIndex(M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex, M23_Relationship.g_relationships.descriptors[i].relIndex);

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].supportExtendedPsCopy & ! M23_Relationship.g_relationships.descriptors[i].isPsTagged) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is tagged to 'support PSCOPY' but is not PS-tagged - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].supportExtendedPsCopy = false;
}

if (M23_Relationship.g_relationships.descriptors[i].supportExtendedPsCopy &  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools |  M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs)) {
M04_Utilities.logMsg("relationship \"" + M23_Relationship.g_relationships.descriptors[i].sectionName + "." + M23_Relationship.g_relationships.descriptors[i].relName + "\": " + "is tagged to 'support PSCOPY' is but common " + (M23_Relationship.g_relationships.descriptors[i].isCommonToOrgs ? "organizations (cto)" : "pools (ctp)"), M01_Common.LogLevel.ellFixableWarning, null, null, null);
M23_Relationship.g_relationships.descriptors[i].supportExtendedPsCopy = false;
}

// ### ENDIF IVK ###

NextI:
}

for (i = 1; i <= 1; i += (1)) {
M23_Relationship.g_relationships.descriptors[i].relRefs.numRefs = 0;
for (j = 1; j <= 1; j += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].leftClassSectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].leftClassName.toUpperCase()) {

M23_Relationship.g_relationships.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].relRefs)].refIndex = j;
M23_Relationship.g_relationships.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].relRefs)].refType = M01_Common.RelNavigationDirection.etLeft;
} else if (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].rightClassSectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].rightClassName.toUpperCase() & M23_Relationship.g_relationships.descriptors[i].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {

M23_Relationship.g_relationships.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].relRefs)].refIndex = j;
M23_Relationship.g_relationships.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].relRefs)].refType = M01_Common.RelNavigationDirection.etRight;
}
}
}

for (i = 1; i <= 1; i += (1)) {
for (j = 1; j <= 1; j += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[j].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {

M24_Attribute.g_attributes.descriptors[j].acmEntityIndex = i;
M24_Attribute.g_attributes.descriptors[j].isPdmSpecific = M24_Attribute.g_attributes.descriptors[j].isPdmSpecific |  M23_Relationship.g_relationships.descriptors[i].isPdmSpecific;
if (!(M23_Relationship.g_relationships.descriptors[i].notAcmRelated)) {
M24_Attribute.g_attributes.descriptors[j].isNotAcmRelated = false;
}

if (M24_Attribute.g_attributes.descriptors[j].isTimeVarying) {
M04_Utilities.logMsg("stereotype <tv> for attribute \"" + M24_Attribute.g_attributes.descriptors[j].attributeName + "\" at relationship \"" + M24_Attribute.g_attributes.descriptors[j].className + "\" is not supported - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M24_Attribute.g_attributes.descriptors[j].isTimeVarying = false;
}
if (M24_Attribute.g_attributes.descriptors[j].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
M23_Relationship.g_relationships.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtEnum;
} else {
M23_Relationship.g_relationships.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute;
}
M23_Relationship.g_relationships.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].attrRefs)].refIndex = j;
if (M24_Attribute.g_attributes.descriptors[j].isNl) {
M23_Relationship.g_relationships.descriptors[i].nlAttrRefs.descriptors[(M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M23_Relationship.g_relationships.descriptors[i].nlAttrRefs))] = M23_Relationship.g_relationships.descriptors[i].attrRefs.descriptors[M23_Relationship.g_relationships.descriptors[i].attrRefs.numDescriptors];
}
}
}
}

// identify attributes which may be 'reused' (mapped to the same column) based on the OR-mapping rules
int relIndex;
int matchRelIndex;

M23_Relationship_Utilities.RelationshipDescriptor relationship;
M23_Relationship_Utilities.RelationshipDescriptor matchRelationship;

M22_Class_Utilities.ClassDescriptor matchLeftClass;
M22_Class_Utilities.ClassDescriptor matchRightClass;
// loop over all relationships being mapped to a foreign key; try to match against any other relationship being mapped to a foreign key to the same table
for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
relationship = M23_Relationship.g_relationships.descriptors[relIndex];

leftClass = M22_Class.getClassByIndex(relationship.leftEntityIndex);
rightclass = M22_Class.getClassByIndex(relationship.rightEntityIndex);

if (leftClass.notAcmRelated |  rightclass.notAcmRelated) {
goto NextRel;
}

if (relationship.maxLeftCardinality == 1 |  relationship.maxRightCardinality == 1) {
// loop over all relationships potentially mapping to the same foreign key
for (matchRelIndex = 1; matchRelIndex <= 1; matchRelIndex += (1)) {
matchRelationship = M23_Relationship.g_relationships.descriptors[matchRelIndex];

if (matchRelationship.maxLeftCardinality == 1 |  matchRelationship.maxRightCardinality == 1) {
matchLeftClass = M22_Class.getClassByIndex(matchRelationship.leftEntityIndex);
matchRightClass = M22_Class.getClassByIndex(matchRelationship.rightEntityIndex);

if (relationship.maxLeftCardinality == 1) {
if (matchRelationship.maxLeftCardinality == 1 &  leftClass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex & rightclass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex) {
if ((!(relationship.reuseName.compareTo("") == 0)) &  (relationship.reuseName.compareTo(matchRelationship.reuseName) == 0)) {
M23_Relationship.setRelationshipReusedRelIndex(relIndex, matchRelIndex);
goto NextRel;
} else {
if (relationship.reuseName.compareTo("") == 0) {
M04_Utilities.logMsg("potential candidates for reuse of foreign key attribute: relationships \"" + relationship.relName + "\"/\"" + matchRelationship.relName + "\"", M01_Common.LogLevel.ellInfo, null, null, null);
}
}
} else if (matchRelationship.maxRightCardinality == 1 &  leftClass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex & rightclass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex) {
if ((!(relationship.reuseName.compareTo("") == 0)) &  (relationship.reuseName.compareTo(matchRelationship.reuseName) == 0)) {
M23_Relationship.setRelationshipReusedRelIndex(relIndex, matchRelIndex);
goto NextRel;
} else {
if (relationship.reuseName.compareTo("") == 0) {
M04_Utilities.logMsg("potential candidates for reuse of foreign key attribute: relationships \"" + relationship.relName + "\"/\"" + matchRelationship.relName + "\"", M01_Common.LogLevel.ellInfo, null, null, null);
}
}
}
} else if (relationship.maxRightCardinality == 1) {
if (matchRelationship.maxLeftCardinality == 1 &  leftClass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex & rightclass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex) {
if ((!(relationship.reuseName.compareTo("") == 0)) &  (relationship.reuseName.compareTo(matchRelationship.reuseName) == 0)) {
M23_Relationship.setRelationshipReusedRelIndex(relIndex, matchRelIndex);
goto NextRel;
} else {
if (relationship.reuseName.compareTo("") == 0) {
M04_Utilities.logMsg("potential candidates for reuse of foreign key attribute: relationships \"" + relationship.relName + "\"/\"" + matchRelationship.relName + "\"", M01_Common.LogLevel.ellInfo, null, null, null);
}
}
} else if (matchRelationship.maxRightCardinality == 1 &  leftClass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex & rightclass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex) {
if ((!(relationship.reuseName.compareTo("") == 0)) &  (relationship.reuseName.compareTo(matchRelationship.reuseName) == 0)) {
M23_Relationship.setRelationshipReusedRelIndex(relIndex, matchRelIndex);
goto NextRel;
} else {
if (relationship.reuseName.compareTo("") == 0) {
M04_Utilities.logMsg("potential candidates for reuse of foreign key attribute: relationships \"" + relationship.relName + "\"/\"" + matchRelationship.relName + "\"", M01_Common.LogLevel.ellInfo, null, null, null);
}
}
}
}
}
}
} else {
// relationship.maxLeftCardinality = -1 And relationship.maxRightCardinality = -1
// loop over all relationships potentially mapping to the same relationship table
for (matchRelIndex = 1; matchRelIndex <= 1; matchRelIndex += (1)) {
matchRelationship = M23_Relationship.g_relationships.descriptors[matchRelIndex];
if (matchRelationship.maxLeftCardinality == -1 &  matchRelationship.maxRightCardinality == -1) {
matchLeftClass = M22_Class.getClassByIndex(matchRelationship.leftEntityIndex);
matchRightClass = M22_Class.getClassByIndex(matchRelationship.rightEntityIndex);
if ((leftClass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex &  rightclass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex) |  (leftClass.orMappingSuperClassIndex == matchRightClass.orMappingSuperClassIndex &  rightclass.orMappingSuperClassIndex == matchLeftClass.orMappingSuperClassIndex)) {
if ((!(relationship.reuseName.compareTo("") == 0)) &  (relationship.reuseName.compareTo(matchRelationship.reuseName) == 0)) {
M23_Relationship.setRelationshipReusedRelIndex(relIndex, matchRelIndex);
goto NextRel;
} else {
M04_Utilities.logMsg("potential candidates for reuse of relationship table: relationships \"" + relationship.relName + "\"/\"" + matchRelationship.relName + "\"" + " " + leftClass.orMappingSuperClassIndex + "/" + matchLeftClass.orMappingSuperClassIndex + "/" + rightclass.orMappingSuperClassIndex + "/" + matchRightClass.orMappingSuperClassIndex, M01_Common.LogLevel.ellInfo, null, null, null);
}
}
}
}
}
NextRel:
}

for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
// determine effective short name
M23_Relationship.g_relationships.descriptors[relIndex].effectiveShortName = (M03_Config.reuseRelationships &  !(M23_Relationship.g_relationships.descriptors[relIndex].reuseShortName.compareTo("") == 0) ? M23_Relationship.g_relationships.descriptors[relIndex].reuseShortName : M23_Relationship.g_relationships.descriptors[relIndex].shortName);

// determine whether this relationship is implemented in an 'own table'
if ((M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[relIndex].reusedRelIndex > 0)) {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable = false;
} else if (M03_Config.supportNlForRelationships &  M23_Relationship.g_relationships.descriptors[relIndex].isNl) {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable = true;
} else if (M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == -1 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == -1) {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable = true;
} else {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable = false;
}
}

for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
if (!(M23_Relationship.g_relationships.descriptors[relIndex].reuseName.compareTo("") == 0)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == -1 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == 1) {
for (j = relIndex - 1; j <= -1; j += (-1)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex == M23_Relationship.g_relationships.descriptors[j].leftEntityIndex &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[j].rightEntityIndex].orMappingSuperClassIndex & M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == M23_Relationship.g_relationships.descriptors[j].maxLeftCardinality & M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == M23_Relationship.g_relationships.descriptors[j].maxRightCardinality & M23_Relationship.g_relationships.descriptors[relIndex].reuseName.compareTo(M23_Relationship.g_relationships.descriptors[j].reuseName) == 0) {
M23_Relationship.g_relationships.descriptors[relIndex].isReusedInSameEntity = (M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex != M23_Relationship.g_relationships.descriptors[j].rightEntityIndex);
goto NextRelIndex;
}
}
} else if (M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == -1 &  M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == 1) {
for (j = relIndex - 1; j <= -1; j += (-1)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex == M23_Relationship.g_relationships.descriptors[j].rightEntityIndex &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[j].leftEntityIndex].orMappingSuperClassIndex & M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == M23_Relationship.g_relationships.descriptors[j].maxLeftCardinality & M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == M23_Relationship.g_relationships.descriptors[j].maxRightCardinality & M23_Relationship.g_relationships.descriptors[relIndex].reuseName.compareTo(M23_Relationship.g_relationships.descriptors[j].reuseName) == 0) {
M23_Relationship.g_relationships.descriptors[relIndex].isReusedInSameEntity = (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex != M23_Relationship.g_relationships.descriptors[j].leftEntityIndex);
goto NextRelIndex;
}
}
}
}
}
NextRelIndex:
}

for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
// if relationship is not implemented in own table, determine table implementing it
M23_Relationship.g_relationships.descriptors[relIndex].implementsInEntity = M01_Common.RelNavigationMode.ernmNone;
if (!(M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == 1) {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInEntity = M01_Common.RelNavigationMode.ernmLeft;
} else {
M23_Relationship.g_relationships.descriptors[relIndex].implementsInEntity = M01_Common.RelNavigationMode.ernmRight;
}
}
}

// ### IF IVK ###
// determine classes / relationships being subject to 'PreisDurchschuss'
//  For relIndex = 1 To g_relationships.numDescriptors Step 1
//    With g_relationships.descriptors(relIndex)
//      If .leftEntityIndex > 0 Then
//        If g_classes.descriptors(.leftEntityIndex).hasPriceAssignmentSubClass Then
//          If .maxLeftCardinality < 0 And .maxRightCardinality < 0 Then
//            .isSubjectToPreisDurchschuss = True
//          ElseIf .maxRightCardinality = 1 And g_classes.descriptors(.rightEntityIndex).isPsTagged And Not g_classes.descriptors(.rightEntityIndex).isPsForming Then
//            With g_classes.descriptors(.rightEntityIndex)
//              If .aggHeadClassIndexExact <= 0 Then
//                .isSubjectToPreisDurchschuss = True
//              ElseIf g_classes.descriptors(.aggHeadClassIndexExact).isSubjectToPreisDurchschuss Then
//                .isSubjectToPreisDurchschuss = True
//              End If
//            End With
//          End If
//        End If
//      End If
//      If .rightEntityIndex > 0 Then
//        If g_classes.descriptors(.rightEntityIndex).hasPriceAssignmentSubClass Then
//          If .maxLeftCardinality < 0 And .maxRightCardinality < 0 Then
//            .isSubjectToPreisDurchschuss = True
//          ElseIf .maxLeftCardinality = 1 And g_classes.descriptors(.leftEntityIndex).isPsTagged And Not g_classes.descriptors(.leftEntityIndex).isPsForming Then
//            With g_classes.descriptors(.leftEntityIndex)
//              If .aggHeadClassIndexExact <= 0 Then
//                .isSubjectToPreisDurchschuss = True
//              ElseIf g_classes.descriptors(.aggHeadClassIndexExact).isSubjectToPreisDurchschuss Then
//                .isSubjectToPreisDurchschuss = True
//              End If
//            End With
//          End If
//        End If
//      End If
//    End With
//  Next relIndex

// determine whether this relationship defines validity per organization
for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
if (M00_Helper.inStr(1, M23_Relationship.g_relationships.descriptors[relIndex].relName.toUpperCase(), "VALID") != 0) {
if ((M23_Relationship.g_relationships.descriptors[relIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex == M01_Globals.g_classIndexOrganization)) {
M23_Relationship.g_relationships.descriptors[relIndex].isValidForOrganization = true;
M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].isValidForOrganization = true;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].subclassIndexesRecursive); i++) {
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].subclassIndexesRecursive[i]].isValidForOrganization = true;
}
} else if ((M23_Relationship.g_relationships.descriptors[relIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex == M01_Globals.g_classIndexOrganization)) {
M23_Relationship.g_relationships.descriptors[relIndex].isValidForOrganization = true;
M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].isValidForOrganization = true;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].subclassIndexesRecursive); i++) {
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].subclassIndexesRecursive[i]].isValidForOrganization = true;
}
}
}
}

// determine whether this relationship corresponds to an organization-specific reference in some class
boolean someClassUpdated;
someClassUpdated = true;
int thisClassIndex;
while (someClassUpdated) {
someClassUpdated = false;
for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex > 0 & M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex > 0) {
if ((M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].isValidForOrganization |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].hasOrganizationSpecificReference) |  (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].isValidForOrganization |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].hasOrganizationSpecificReference)) {
if (M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality < 0 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality < 0) {
if (!((M23_Relationship.g_relationships.descriptors[relIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex == M01_Globals.g_classIndexOrganization) & ! (M23_Relationship.g_relationships.descriptors[relIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex == M01_Globals.g_classIndexOrganization))) {
// direct references to 'organization' are not included here
someClassUpdated = someClassUpdated | ! M23_Relationship.g_relationships.descriptors[relIndex].hasOrganizationSpecificReference;
M23_Relationship.g_relationships.descriptors[relIndex].hasOrganizationSpecificReference = true;
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].isValidForOrganization |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].hasOrganizationSpecificReference) {
someClassUpdated = someClassUpdated | ! M23_Relationship.g_relationships.descriptors[relIndex].rightClassIsOrganizationSpecific;
M23_Relationship.g_relationships.descriptors[relIndex].rightClassIsOrganizationSpecific = true;
}
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].isValidForOrganization |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].hasOrganizationSpecificReference) {
someClassUpdated = someClassUpdated | ! M23_Relationship.g_relationships.descriptors[relIndex].leftClassIsOrganizationSpecific;
M23_Relationship.g_relationships.descriptors[relIndex].leftClassIsOrganizationSpecific = true;
}
}
} else {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].isValidForOrganization &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality < 0) {
thisClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex;
while (thisClassIndex > 0) {
someClassUpdated = someClassUpdated | ! M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference;
M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference = true;
M22_Class.addRelRef(M22_Class.g_classes.descriptors[thisClassIndex].relRefsToOrganizationSpecificClasses, relIndex, M01_Common.RelNavigationDirection.etRight);
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
}
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].isValidForOrganization &  M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality < 0) {
thisClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex;
while (thisClassIndex > 0) {
someClassUpdated = someClassUpdated | ! M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference;
M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference = true;
M22_Class.addRelRef(M22_Class.g_classes.descriptors[thisClassIndex].relRefsToOrganizationSpecificClasses, relIndex, M01_Common.RelNavigationDirection.etLeft);
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
}
}
}
}
}
}

int leftOrParentClassIndex;
int rightOrParentClassIndex;
for (relIndex = 1; relIndex <= 1; relIndex += (1)) {
M23_Relationship.g_relationships.descriptors[relIndex].isSubjectToExpCopy = M23_Relationship.g_relationships.descriptors[relIndex].isUserTransactional &  M23_Relationship.g_relationships.descriptors[relIndex].aggHeadName.toUpperCase() == M01_ACM_IVK.clnExpression.toUpperCase();

if (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex > 0 &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex > 0 & !M23_Relationship.g_relationships.descriptors[relIndex].isMdsExpressionRel) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].condenseData |  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].condenseData) {
leftOrParentClassIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].orMappingSuperClassIndex;
rightOrParentClassIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].orMappingSuperClassIndex;

if (!((M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality < 0 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality < 0) &  (leftOrParentClassIndex != rightOrParentClassIndex))) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex].condenseData &  ((M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality < 0) |  (M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == 1))) {
thisClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex;
while (thisClassIndex > 0) {
M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference = true;
M22_Class.addRelRef(M22_Class.g_classes.descriptors[thisClassIndex].relRefsToOrganizationSpecificClasses, relIndex, M01_Common.RelNavigationDirection.etLeft);
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
} else if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex].condenseData &  ((M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality < 0) |  (M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == 1))) {
thisClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex;
while (thisClassIndex > 0) {
M22_Class.g_classes.descriptors[thisClassIndex].hasOrganizationSpecificReference = true;
M22_Class.addRelRef(M22_Class.g_classes.descriptors[thisClassIndex].relRefsToOrganizationSpecificClasses, relIndex, M01_Common.RelNavigationDirection.etRight);
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
}
}
}
}

// determine foreign key column names
Integer thisDdlType;
for (int thisDdlType = M01_Common.DdlTypeId.edtPdm; thisDdlType <= M01_Common.DdlTypeId.edtLdm; thisDdlType++) {
if (M23_Relationship.g_relationships.descriptors[relIndex].implementsInOwnTable) {
M23_Relationship.g_relationships.descriptors[relIndex].leftFkColName[(thisDdlType)] = M04_Utilities.genSurrogateKeyName(thisDdlType, M23_Relationship.g_relationships.descriptors[relIndex].leftEntityShortName, null, null, null, null);
M23_Relationship.g_relationships.descriptors[relIndex].rightFkColName[(thisDdlType)] = M04_Utilities.genSurrogateKeyName(thisDdlType, M23_Relationship.g_relationships.descriptors[relIndex].rightEntityShortName, null, null, null, null);
} else {
M23_Relationship.g_relationships.descriptors[relIndex].leftFkColName[(thisDdlType)] = M04_Utilities.genSurrogateKeyName(thisDdlType, M23_Relationship.g_relationships.descriptors[relIndex].effectiveShortName + M23_Relationship.g_relationships.descriptors[relIndex].rlShortRelName, null, null, null, null);
M23_Relationship.g_relationships.descriptors[relIndex].rightFkColName[(thisDdlType)] = M04_Utilities.genSurrogateKeyName(thisDdlType, M23_Relationship.g_relationships.descriptors[relIndex].effectiveShortName + M23_Relationship.g_relationships.descriptors[relIndex].lrShortRelName, null, null, null, null);
}
}
}
// ### ENDIF IVK ###
}


}