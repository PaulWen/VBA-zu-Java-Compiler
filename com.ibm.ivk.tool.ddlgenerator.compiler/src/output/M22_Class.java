package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M22_Class {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colClass = colSection + 1;
private static final int colAggHeadSection = colClass + 1;
private static final int colAggHeadName = colAggHeadSection + 1;
private static final int colClassLdm = colAggHeadName + 1;
private static final int colShortName = colClassLdm + 1;
// ### IF IVK ###
private static final int colLrtClassification = colShortName + 1;
private static final int colLrtActivationStatusMode = colLrtClassification + 1;
private static final int colEntityFilterEnumCriteria = colLrtActivationStatusMode + 1;
private static final int colIgnoreForChangeLog = colEntityFilterEnumCriteria + 1;
// ### ELSE IVK ###
//Private Const colIgnoreForChangeLog = colShortName + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colMapToClAttribute = colIgnoreForChangeLog + 1;
private static final int colNavPathToDivision = colMapToClAttribute + 1;
private static final int colNavPathToOrg = colNavPathToDivision + 1;
private static final int colNavPathToCodeType = colNavPathToOrg + 1;
private static final int colCondenseData = colNavPathToCodeType + 1;
private static final int colIsDeletable = colCondenseData + 1;
private static final int colEnforceChangeComment = colIsDeletable + 1;
private static final int colIsCommonToOrgs = colEnforceChangeComment + 1;
// ### ELSE IVK ###
//Private Const colIsCommonToOrgs = colIgnoreForChangeLog + 1
// ### ENDIF IVK ###
private static final int colSpecificToOrg = colIsCommonToOrgs + 1;
private static final int colIsCommonToPools = colSpecificToOrg + 1;
private static final int colSpecificToPool = colIsCommonToPools + 1;
private static final int colNoIndexesInPool = colSpecificToPool + 1;
private static final int colUseValueCompression = colNoIndexesInPool + 1;
// ### IF IVK ###
private static final int colIsCore = colUseValueCompression + 1;
private static final int colIsAbstract = colIsCore + 1;
// ### ELSE IVK ###
//Private Const colIsAbstract = colUseValueCompression + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colSupportAhStatusPropagation = colIsAbstract + 1;
private static final int colUpdateMode = colSupportAhStatusPropagation + 1;
private static final int colSuperClassSection = colUpdateMode + 1;
// ### ELSE IVK ###
//Private Const colSuperClassSection = colIsAbstract + 1
// ### ENDIF IVK ###
private static final int colSuperClass = colSuperClassSection + 1;
private static final int colUseSurrogateKey = colSuperClass + 1;
private static final int colUseVersionTag = colUseSurrogateKey + 1;
// ### IF IVK ###
private static final int colClassMapping = colUseVersionTag + 1;
private static final int colClassId = colClassMapping + 1;
// ### ELSE IVK ###
//Private Const colClassId = colUseVersionTag + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colNoRangePartitioning = colClassId + 1;
private static final int colRangePartitioningAll = colNoRangePartitioning + 1;
private static final int colRangePartitionGroup = colRangePartitioningAll + 1;
private static final int colIsNationalizable = colRangePartitionGroup + 1;
private static final int colIsGenForming = colIsNationalizable + 1;
// ### ELSE IVK ###
//Private Const colIsGenForming = colClassId + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colHasNoIdentity = colIsGenForming + 1;
private static final int colIsPsTagged = colHasNoIdentity + 1;
private static final int colPsTagNotIdentifying = colIsPsTagged + 1;
private static final int colPsTagOptional = colPsTagNotIdentifying + 1;
private static final int colIgnPsRegVarOnInsDel = colPsTagOptional + 1;
private static final int colIsPsForming = colIgnPsRegVarOnInsDel + 1;
private static final int colSupportExtendedPsCopy = colIsPsForming + 1;
private static final int colLogLastChange = colSupportExtendedPsCopy + 1;
// ### ELSE IVK ###
//Private Const colLogLastChange = colIsGenForming + 1
// ### ENDIF IVK ###
private static final int colLogLastChangeInView = colLogLastChange + 1;
private static final int colLogLastChangeAutoMaint = colLogLastChangeInView + 1;
// ### IF IVK ###
private static final int colExpandExpressionsInFtoView = colLogLastChangeAutoMaint + 1;
private static final int colIsUserTransactional = colExpandExpressionsInFtoView + 1;
// ### ELSE IVK ###
//Private Const colIsUserTransactional = colLogLastChangeAutoMaint + 1
// ### ENDIF IVK ###
private static final int colUseMqtToImplementLrt = colIsUserTransactional + 1;
private static final int colNotAcmRelated = colUseMqtToImplementLrt + 1;
private static final int colNoAlias = colNotAcmRelated + 1;
private static final int colNoFks = colNoAlias + 1;
// ### IF IVK ###
private static final int colNoXmlExport = colNoFks + 1;
private static final int colUseXmlExport = colNoXmlExport + 1;
private static final int colIsLrtSpecific = colUseXmlExport + 1;
// ### ELSE IVK ###
//Private Const colIsLrtSpecific = colNoFks + 1
// ### ENDIF IVK ###
private static final int colIsPdmSpecific = colIsLrtSpecific + 1;
// ### IF IVK ###
private static final int colIncludeInPdmExportSeqNo = colIsPdmSpecific + 1;
private static final int colIsVolatile = colIncludeInPdmExportSeqNo + 1;
// ### ELSE IVK ###
//Private Const colIsVolatile = colIsPdmSpecific + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colNotPersisted = colIsVolatile + 1;
private static final int colIsSubjectToArchiving = colNotPersisted + 1;
private static final int colNonStandardRefTimeStampForArchiving = colIsSubjectToArchiving + 1;
private static final int colNoTransferToProduction = colNonStandardRefTimeStampForArchiving + 1;
private static final int colNoFto = colNoTransferToProduction + 1;
private static final int colFtoSingleObjProcessing = colNoFto + 1;
private static final int colTabSpaceData = colFtoSingleObjProcessing + 1;
// ### ELSE IVK ###
//Private Const colTabSpaceData = colIsVolatile + 1
// ### ENDIF IVK ###
private static final int colTabSpaceLong = colTabSpaceData + 1;
private static final int colTabSpaceNl = colTabSpaceLong + 1;
private static final int colTabSpaceIndex = colTabSpaceNl + 1;
private static final int colComment = colTabSpaceIndex + 1;
private static final int colI18nId = colComment + 1;

public static final int colClassI18nId = colI18nId;

private static final int firstRow = 4;

private static final String sheetName = "Class";

private static final int processingStep = 2;
private static final int processingStepLrt = 2;
private static final int processingStepPsCopy = 1;
private static final int processingStepPsCopy2 = 2;
private static final int processingStepExpCopy = 6;
private static final int processingStepSetProd = 5;
private static final int processingStepFto = 3;
private static final int processingStepAlias = 3;
private static final int processingStepComment = 4;
private static final int processingStepMiscMeta = 1;

private static final int acmCsvProcessingStep = 1;
public static final int ldmCsvTableProcessingStep = 2;
public static final int ldmCsvFkProcessingStep = 3;
private static final int pdmCsvProcessingStep = 3;

public static M22_Class_Utilities.ClassDescriptors g_classes;


private static void readSheet() {
M22_Class_Utilities.initClassDescriptors(M22_Class.g_classes);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;

thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

String lastSection;
while (M00_Excel.getCell(thisSheet, thisRow, colClass).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
if ((M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].sectionName + "" == "")) {
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].sectionName = lastSection;
}

M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].className = M00_Excel.getCell(thisSheet, thisRow, colClass).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].aggHeadSection = M00_Excel.getCell(thisSheet, thisRow, colAggHeadSection).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].aggHeadName = M00_Excel.getCell(thisSheet, thisRow, colAggHeadName).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].classNameLdm = M00_Excel.getCell(thisSheet, thisRow, colClassLdm).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].lrtClassification = M00_Excel.getCell(thisSheet, thisRow, colLrtClassification).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].lrtActivationStatusMode = M00_Excel.getCell(thisSheet, thisRow, colLrtActivationStatusMode).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].entityFilterEnumCriteria = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colEntityFilterEnumCriteria).getStringCellValue(), 0);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].navPathStrToDivision = M00_Excel.getCell(thisSheet, thisRow, colNavPathToDivision).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].navPathStrToOrg = M00_Excel.getCell(thisSheet, thisRow, colNavPathToOrg).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].navPathStrToCodeType = M00_Excel.getCell(thisSheet, thisRow, colNavPathToCodeType).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].condenseData = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colCondenseData).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isDeletable = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsDeletable).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].enforceLrtChangeComment = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colEnforceChangeComment).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].ignoreForChangelog = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIgnoreForChangeLog).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].mapOidToClAttribute = M00_Excel.getCell(thisSheet, thisRow, colMapToClAttribute).getStringCellValue().trim();
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isCommonToOrgs = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToOrgs).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isCommonToPools = M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isCommonToOrgs |  M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCommonToPools).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].specificToPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noIndexesInPool = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colNoIndexesInPool).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].useValueCompression = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseValueCompression).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isCore = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsCore).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isAbstract = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsAbstract).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].supportAhStatusPropagation = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportAhStatusPropagation).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].updateMode = M04_Utilities.getDbUpdateMode(M00_Excel.getCell(thisSheet, thisRow, colUpdateMode).getStringCellValue());
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].superClassSection = M00_Excel.getCell(thisSheet, thisRow, colSuperClassSection).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].superClass = M00_Excel.getCell(thisSheet, thisRow, colSuperClass).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].useSurrogateKey = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseSurrogateKey).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].useVersiontag = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseVersionTag).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].mapping = M22_Class_Utilities.getClassMapping(M00_Excel.getCell(thisSheet, thisRow, colClassMapping).getStringCellValue());
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].classId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colClassId).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noRangePartitioning = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoRangePartitioning).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].rangePartitioningAll = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colRangePartitioningAll).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].rangePartitionGroup = M00_Excel.getCell(thisSheet, thisRow, colRangePartitionGroup).getStringCellValue().trim();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isNationalizable = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNationalizable).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isGenForming = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsGenForming).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].hasNoIdentity = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colHasNoIdentity).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isPsTagged = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPsTagged).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].psTagNotIdentifying = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colPsTagNotIdentifying).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].psTagOptional = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colPsTagOptional).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].ignPsRegVarOnInsDel = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIgnPsRegVarOnInsDel).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isPsForming = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPsForming).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].supportExtendedPsCopy = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportExtendedPsCopy).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].logLastChange = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChange).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].logLastChangeInView = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChangeInView).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].logLastChangeAutoMaint = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colLogLastChangeAutoMaint).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].expandExpressionsInFtoView = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colExpandExpressionsInFtoView).getStringCellValue(), null);
// ### ENDIF IVK ###
if (M00_Excel.getCell(thisSheet, thisRow, colIsUserTransactional).getStringCellValue().trim().toUpperCase() == "M") {
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isLrtMeta = true;
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isUserTransactional = false;
} else {
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isUserTransactional = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsUserTransactional).getStringCellValue(), null);
}
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].useMqtToImplementLrt = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseMqtToImplementLrt).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].notAcmRelated = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotAcmRelated).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noAlias = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoAlias).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noFks = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoFks).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoXmlExport).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].useXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colUseXmlExport).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isLrtSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsLrtSpecific).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isPdmSpecific = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPdmSpecific).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].includeInPdmExportSeqNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colIncludeInPdmExportSeqNo).getStringCellValue(), -1);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isVolatile = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsVolatile).getStringCellValue(), null);
// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].notPersisted = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNotPersisted).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].isSubjectToArchiving = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsSubjectToArchiving).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].nonStandardRefTimeStampForArchiving = M00_Excel.getCell(thisSheet, thisRow, colNonStandardRefTimeStampForArchiving).getStringCellValue();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noTransferToProduction = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoTransferToProduction).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].noFto = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoFto).getStringCellValue(), null);
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].ftoSingleObjProcessing = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colFtoSingleObjProcessing).getStringCellValue(), null);
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].tabSpaceData = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceData).getStringCellValue();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].tabSpaceLong = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceLong).getStringCellValue();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].tabSpaceNl = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceNl).getStringCellValue();
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].tabSpaceIndex = M00_Excel.getCell(thisSheet, thisRow, colTabSpaceIndex).getStringCellValue();

// ### IF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].defaultStatus = M86_SetProductive.statusReadyForActivation;

M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].groupIdAttrIndexes =  new int[0];
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].aggChildClassIndexes =  new int[0];
M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].aggChildRelIndexes =  new int[0];

lastSection = M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].sectionName;
// ### IF IVK ###

M22_Class.g_classes.descriptors[M22_Class_Utilities.allocClassDescriptorIndex(M22_Class.g_classes)].hasGroupIdAttrInNonGen = false;
// ### ENDIF IVK ###

NextRow:
thisRow = thisRow + 1;
}
}


public static void resetClassesCsvExported() {
int i;

for (i = 1; i <= 1; i += (1)) {
M22_Class.g_classes.descriptors[i].isLdmCsvExported = false;
M22_Class.g_classes.descriptors[i].isLdmLrtCsvExported = false;
// ### IF IVK ###
M22_Class.g_classes.descriptors[i].isXsdExported = false;
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[i].isCtoAliasCreated = false;
}
}


public static void getClasses() {
if (M22_Class.g_classes.numDescriptors == 0) {
readSheet();
}
}


public static void resetClasses() {
M22_Class.g_classes.numDescriptors = 0;
M22_Class.g_classes.descriptors =  new M21_Enum_Utilities_NL.EnumNlDescriptor[1];
}


public static Integer getClassIndexByName(String sectionName, String className, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;
if (sectionName.compareTo("") == 0 &  className.compareTo("") == 0) {
return returnValue;
}

for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == className.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
M04_Utilities.logMsg("unable to identify class '" + sectionName + "." + className + "'", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtLdm, null, null);
}
return returnValue;
}

public static String getClassIdStrByIndex(int classIndex) {
String returnValue;
int i;

returnValue = -1;

if (classIndex > 0 &  classIndex < M22_Class.g_classes.numDescriptors) {
returnValue = M22_Class.g_classes.descriptors[classIndex].classIdStr;
}
return returnValue;
}


public static String getSubClassIdStrListByClassIndex(int classIndex) {
String returnValue;

String subClassIdStrList;
subClassIdStrList = "";

subClassIdStrList = (M22_Class.g_classes.descriptors[classIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[classIndex].classIdStr + "'");
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[i]].isAbstract)) {
subClassIdStrList = subClassIdStrList + (subClassIdStrList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}

returnValue = subClassIdStrList;
return returnValue;
}


public static void getSubClassIdStrListPartitionGroupMap(int classIndex) {

int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive); i++) {
int j;
for (int j = 1; j <= M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[i]].subClassIdStrSeparatePartition.numMaps; j++) {
M22_Class_Utilities.addStrListMapEntry(M22_Class.g_classes.descriptors[classIndex].subClassIdStrSeparatePartition, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[i]].subClassIdStrSeparatePartition.maps[j].name, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[i]].subClassIdStrSeparatePartition.maps[j].list);
}
}
}


private static String getNonAbstractSubClassIdStrListHavingAttrByClassIndex(int classIndex, String attrName) {
String returnValue;

returnValue = "";

int i;
for (int i = 1; i <= M22_Class.g_classes.descriptors[classIndex].attrRefs.numDescriptors; i++) {
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName.toUpperCase() == attrName.toUpperCase()) {
returnValue = M22_Class.g_classes.descriptors[classIndex].subclassIdStrListNonAbstract;
return returnValue;
}
}
return returnValue;
}


public static String getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex(int classIndex, String attrName) {
String returnValue;

String resClassIdStrList;
String subClassIdStrList;
resClassIdStrList = "";

resClassIdStrList = getNonAbstractSubClassIdStrListHavingAttrByClassIndex(classIndex, attrName);

if (!(resClassIdStrList.compareTo("") == 0)) {
returnValue = resClassIdStrList;
return returnValue;
}

int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[classIndex].subclassIndexes); i++) {
subClassIdStrList = M22_Class.getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].classIndex, attrName);
if (!(subClassIdStrList.compareTo("") == 0)) {
resClassIdStrList = resClassIdStrList + (resClassIdStrList.compareTo("") == 0 ? "" : ",") + subClassIdStrList;
}
}

returnValue = resClassIdStrList;
return returnValue;
}


public static M22_Class_Utilities.ClassDescriptor getClassByIndex(int classIndex) {
M22_Class_Utilities.ClassDescriptor returnValue;
if ((classIndex > 0)) {
returnValue = M22_Class.g_classes.descriptors[classIndex];
}

return returnValue;
}


public static Integer getClassIndexByI18nId(String i18nId) {
Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[i].i18nId.toUpperCase() == i18nId.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static String getClassShortNameByIndex(int classIndex) {
String returnValue;
returnValue = "";
if ((classIndex > 0)) {
returnValue = M22_Class.g_classes.descriptors[classIndex].shortName;
}

return returnValue;
}

public static String getUseSurrogateKeyByClassName(String sectionName, String className) {
String returnValue;
int classIndex;
classIndex = M22_Class.getClassIndexByName(sectionName, className, null);

returnValue = true;
if ((classIndex > 0)) {
returnValue = M22_Class.g_classes.descriptors[classIndex].useSurrogateKey;
}

return returnValue;
}


public static Integer getOrMappingSuperClassIndexByClassIndex( int classIndex) {
Integer returnValue;
returnValue = classIndex;

while ((classIndex > 0)) {
if (M22_Class.g_classes.descriptors[classIndex].superClass.compareTo("") == 0) {
returnValue = classIndex;
classIndex = -1;
} else {
classIndex = M22_Class.g_classes.descriptors[classIndex].superClassIndex;
}
}
return returnValue;
}


public static M22_Class_Utilities.ClassDescriptor getOrMappingSuperClass(String sectionName, String className) {
M22_Class_Utilities.ClassDescriptor returnValue;
int classIndex;
classIndex = M22_Class.getClassIndexByName(sectionName, className, null);

returnValue = M22_Class.g_classes.descriptors[classIndex];

while ((classIndex > 0)) {
if (M22_Class.g_classes.descriptors[classIndex].superClass.compareTo("") == 0) {
returnValue = M22_Class.g_classes.descriptors[classIndex];
classIndex = -1;
} else {
classIndex = M22_Class.g_classes.descriptors[classIndex].superClassIndex;
}
}
return returnValue;
}


public static Integer getAttributeIndexByClassIndexAndName(int classIndex, String attrName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;
if (classIndex < 0 |  classIndex > M22_Class.g_classes.numDescriptors) {
return returnValue;
}

for (int i = 1; i <= M22_Class.g_classes.descriptors[classIndex].attrRefsInclSubClasses.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[classIndex].attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefsInclSubClasses.descriptors[i].refIndex].attributeName.toUpperCase() == attrName.toUpperCase()) {
returnValue = M22_Class.g_classes.descriptors[classIndex].attrRefsInclSubClasses.descriptors[i].refIndex;
return returnValue;
}
}
}

if (!(silent)) {
errMsgBox("unable to identify attribute '" + attrName + " in class '" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "'", vbCritical);
}
return returnValue;
}


public static Integer getAttributeIndexByClassNameAndName(String sectionName, String className, String attrName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int classIndex;
classIndex = M22_Class.getClassIndexByName(sectionName, className, null);

returnValue = M22_Class.getAttributeIndexByClassIndexAndName(classIndex, attrName, null);
return returnValue;
}


public static void addAggChildClassIndex(int thisClassIndex, int aggChildClassIndex) {
int i;

if ((thisClassIndex <= 0) |  (aggChildClassIndex <= 0)) {
return;
}

if ((M22_Class.g_classes.descriptors[thisClassIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[aggChildClassIndex].orMappingSuperClassIndex) &  (M22_Class.g_classes.descriptors[thisClassIndex].classIndex != M22_Class.g_classes.descriptors[aggChildClassIndex].classIndex)) {
return;
}

int ub;
ub = M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes);
for (int i = 1; i <= ub; i++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes[i] == aggChildClassIndex) {
return;
}
}

if (ub == 0) {
M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes =  new int[1];
} else {
int[] aggChildClassIndexesBackup = M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes;
M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes =  new int[(ub + 1)];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : aggChildClassIndexesBackup) {
M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes[indexCounter] = value;
indexCounter++;
}
}
M22_Class.g_classes.descriptors[thisClassIndex].aggChildClassIndexes[(ub + 1)] = aggChildClassIndex;
}


public static void addAggChildRelIndex(int thisClassIndex, int aggChildRelIndex) {
int i;

if (thisClassIndex <= 0 |  aggChildRelIndex <= 0) {
return;
}

if ((M23_Relationship.g_relationships.descriptors[aggChildRelIndex].maxLeftCardinality == 1 |  M23_Relationship.g_relationships.descriptors[aggChildRelIndex].maxRightCardinality == 1) &  (!(M23_Relationship.g_relationships.descriptors[aggChildRelIndex].isNl))) {
return;
}

int ub;
ub = M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes);

for (int i = 1; i <= ub; i++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes[i] == aggChildRelIndex) {
return;
}
}

if (ub == 0) {
M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes =  new int[1];
} else {
int[] aggChildRelIndexesBackup = M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes;
M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes =  new int[(ub + 1)];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : aggChildRelIndexesBackup) {
M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes[indexCounter] = value;
indexCounter++;
}
}

M22_Class.g_classes.descriptors[thisClassIndex].aggChildRelIndexes[(ub + 1)] = aggChildRelIndex;
}
// ### IF IVK ###


public static void addGroupIdAttrIndex(int thisClassIndex, int groupIdAttrIndex) {
int i;

if ((thisClassIndex <= 0) |  (groupIdAttrIndex <= 0)) {
return;
}

int ub;
ub = M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes);
for (int i = 1; i <= ub; i++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes[i] == groupIdAttrIndex) {
return;
}
}

if (ub == 0) {
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes =  new int[1];
} else {
int[] groupIdAttrIndexesBackup = M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes;
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes =  new int[(ub + 1)];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : groupIdAttrIndexesBackup) {
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes[indexCounter] = value;
indexCounter++;
}
}
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes[(ub + 1)] = groupIdAttrIndex;
}


public static void addGroupIdAttrIndexInclSubClasses(int thisClassIndex, int groupIdAttrIndex) {
int i;

if ((thisClassIndex <= 0) |  (groupIdAttrIndex <= 0)) {
return;
}

int ub;
ub = M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses);
for (int i = 1; i <= ub; i++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes[i] == groupIdAttrIndex) {
return;
}
}

//TF: groupIdAttrIndexes added
if (ub == 0) {
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses =  new int[1];
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes =  new int[1];
} else {
int[] groupIdAttrIndexesInclSubclassesBackup = M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses;
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses =  new int[(ub + 1)];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : groupIdAttrIndexesInclSubclassesBackup) {
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses[indexCounter] = value;
indexCounter++;
}
int[] groupIdAttrIndexesBackup = M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes;
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes =  new int[(ub + 1)];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : groupIdAttrIndexesBackup) {
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexes[indexCounter] = value;
indexCounter++;
}
}
M22_Class.g_classes.descriptors[thisClassIndex].groupIdAttrIndexesInclSubclasses[(ub + 1)] = groupIdAttrIndex;
}
// ### ENDIF IVK ###


public static int[] getDirectSubclassIndexes(int thisClassIndex) {
int[] returnValue;
String thisSection;
String thisClassName;
int[] result;
int resultPos;

result =  new int[M22_Class.g_classes.numDescriptors];
resultPos = 0;
thisSection = M22_Class.g_classes.descriptors[thisClassIndex].sectionName.toUpperCase();
thisClassName = M22_Class.g_classes.descriptors[thisClassIndex].className.toUpperCase();

int i;
for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[i].superClassSection.toUpperCase() == thisSection &  M22_Class.g_classes.descriptors[i].superClass.toUpperCase() == thisClassName) {
resultPos = resultPos + 1;
result[(resultPos)] = i;
}
}

if (resultPos > 0) {
int[] resultBackup = result;
result =  new int[resultPos];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : resultBackup) {
result[indexCounter] = value;
indexCounter++;
}
} else {
result =  new int[0];
}

returnValue = result;
return returnValue;
}


private static void addDirectSubclassIndexes(int[] indexes, int pos, int thisClassIndex) {

int thisI;
for (int thisI = 1; thisI <= M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes); thisI++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[thisI] != thisClassIndex) {
pos = pos + 1;
indexes[(pos)] = M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[thisI];
addDirectSubclassIndexes(indexes, pos, M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[thisI]);
}
}
}


public static int[] getSubclassIndexesRecursive(int thisClassIndex) {
int[] returnValue;
String thisSection;
String thisClassName;
int[] result;
int resultPos;

result =  new int[M22_Class.g_classes.numDescriptors];
resultPos = 0;
addDirectSubclassIndexes(result, resultPos, thisClassIndex);

if (resultPos > 0) {
int[] resultBackup = result;
result =  new int[resultPos];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : resultBackup) {
result[indexCounter] = value;
indexCounter++;
}
} else {
result =  new int[0];
}

returnValue = result;
return returnValue;
}


public static void addRelRef(M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, int thisRelIndex, Integer refType) {
int i;
for (int i = 1; i <= relRefs.numRefs; i++) {
if (relRefs.refs[i].refIndex == thisRelIndex &  relRefs.refs[i].refType.compareTo(refType) == 0) {
return;
}
}

relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(relRefs)].refIndex = thisRelIndex;
relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(relRefs)].refType = refType;
}


private static void addRelRefsRecursive(M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, int thisClassIndex) {

int thisI;
int thisR;
for (int thisR = 1; thisR <= M22_Class.g_classes.descriptors[thisClassIndex].relRefs.numRefs; thisR++) {
relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(relRefs)].refIndex = M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[thisR].refIndex;
relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(relRefs)].refType = M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[thisR].refType;
}

for (int thisI = 1; thisI <= M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes); thisI++) {
addRelRefsRecursive(relRefs, M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[thisI]);
}
}

public static M23_Relationship_Utilities.RelationshipDescriptorRefs getRelRefsRecursive(int thisClassIndex) {
M23_Relationship_Utilities.RelationshipDescriptorRefs returnValue;
String thisSection;
String thisClassName;
M23_Relationship_Utilities.RelationshipDescriptorRefs result;

addRelRefsRecursive(result, thisClassIndex);

returnValue = result;
return returnValue;
}


public static void genTransformedAttrDeclsForClassRecursiveWithColReUse(int classIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer levelW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW, Integer directionW, Boolean attrIsReUsedW, String genParentTabNameW, Boolean suppressColConstraintsW, Boolean useAlternativeDefaultsW, Boolean forceCommaW) {
int level; 
if (levelW == null) {
level = 1;
} else {
level = levelW;
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

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

boolean attrIsReUsed; 
if (attrIsReUsedW == null) {
attrIsReUsed = false;
} else {
attrIsReUsed = attrIsReUsedW;
}

String genParentTabName; 
if (genParentTabNameW == null) {
genParentTabName = "";
} else {
genParentTabName = genParentTabNameW;
}

boolean suppressColConstraints; 
if (suppressColConstraintsW == null) {
suppressColConstraints = false;
} else {
suppressColConstraints = suppressColConstraintsW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

boolean forceComma; 
if (forceCommaW == null) {
forceComma = false;
} else {
forceComma = forceCommaW;
}

boolean addComma;
boolean hasMetaAttrs;
boolean forSubClass;
boolean useVersiontag;

//On Error GoTo ErrorExit 

useVersiontag = (level == 1) &  (!(suppressMetaAttrs)) & M22_Class.g_classes.descriptors[classIndex].useVersiontag;
// ### IF IVK ###
hasMetaAttrs = useVersiontag |  M22_Class.g_classes.descriptors[classIndex].isPsTagged | (!(forGen &  M22_Class.g_classes.descriptors[classIndex].isNationalizable)) | ((forGen |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) &  M22_Class.g_classes.descriptors[classIndex].isGenForming) | (M22_Class.g_classes.descriptors[classIndex].logLastChange &  (!(forGen |  M03_Config.g_cfgGenLogChangeForGenTabs)));
// ### ELSE IVK ###
//   hasMetaAttrs = useVersiontag Or _
//                  (forGen And .isGenForming) Or _
//                  (.logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))
// ### ENDIF IVK ###
forSubClass = (direction == M01_Common.RecursionDirection.erdDown ? level > 1 : M22_Class.g_classes.descriptors[classIndex].superClassIndex > 0);

if (direction == M01_Common.RecursionDirection.erdUp &  !(M22_Class.g_classes.descriptors[classIndex].superClass.compareTo("") == 0)) {
// recurse to parent class
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(M22_Class.g_classes.descriptors[classIndex].superClassIndex, transformation, tabColumns, level + 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode, direction, null, null, null, null, forceComma |  useVersiontag | ((M22_Class.g_classes.descriptors[classIndex].numAttrsInNonGen - M22_Class.g_classes.descriptors[classIndex].numNlAttrsInNonGen) > 0));
}

if ((level > 1)) {
M22_Class_Utilities.printSectionHeader("private attributes for subclass \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className.toUpperCase() + (!(M22_Class.g_classes.descriptors[classIndex].classIdStr.compareTo("") == 0) ? "\" (ClassId='" + M22_Class.g_classes.descriptors[classIndex].classIdStr + "')" : ""), fileNo, outputMode, null);
}

// Fixme: add 'derived columns in ClassDescriptor'
// ######################################################
int i;
int numAttrsInSubclasses;
int numRelBasedFkAttrsInclSubclasses;
numAttrsInSubclasses = 0;
numRelBasedFkAttrsInclSubclasses = M22_Class.g_classes.descriptors[classIndex].numRelBasedFkAttrs;
if (direction == M01_Common.RecursionDirection.erdDown) {
for (i = 1; i <= 1; i += (1)) {
numAttrsInSubclasses = numAttrsInSubclasses + (forGen ? M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].numAttrsInGen : M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].numAttrsInNonGen);
if (!(forGen)) {
numRelBasedFkAttrsInclSubclasses = numRelBasedFkAttrsInclSubclasses + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].numRelBasedFkAttrs;
}

}
}

addComma = forceComma |  hasMetaAttrs | (numAttrsInSubclasses > 0) | (numRelBasedFkAttrsInclSubclasses > 0);

M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[classIndex].classIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, M22_Class.g_classes.descriptors[classIndex].useSurrogateKey, M22_Class.g_classes.descriptors[classIndex].isGenForming, forGen, suppressMetaAttrs |  (direction == M01_Common.RecursionDirection.erdUp &  !(M22_Class.g_classes.descriptors[classIndex].superClass.compareTo("") == 0)), direction == M01_Common.RecursionDirection.erdUp, M22_Class.g_classes.descriptors[classIndex].isUserTransactional, !(addComma), forLrt, outputMode, indent, null, genParentTabName, suppressColConstraints, useAlternativeDefaults, null);

addComma = forceComma |  hasMetaAttrs | (numAttrsInSubclasses > 0);

M23_Relationship.genTransformedAttrDeclForRelationshipsByClassWithColReuse(classIndex, transformation, tabColumns, (direction == M01_Common.RecursionDirection.erdDown) &  (level > 1), fileNo, ddlType, thisOrgIndex, thisPoolIndex, forGen, outputMode, indent, addComma, direction == M01_Common.RecursionDirection.erdUp);

if (direction == M01_Common.RecursionDirection.erdDown) {
for (i = 1; i <= 1; i += (1)) {
numAttrsInSubclasses = numAttrsInSubclasses - (forGen ? M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].numAttrsInGen : M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i]].numAttrsInNonGen);

addComma = forceComma |  hasMetaAttrs | (numAttrsInSubclasses > 0);

M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i], transformation, tabColumns, level + 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode, M01_Common.RecursionDirection.erdDown, null, null, null, null, addComma);
}
}

if (level == 1) {
if (!(suppressMetaAttrs)) {
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
addComma = forceComma |  useVersiontag | (!(forGen &  M22_Class.g_classes.descriptors[classIndex].isNationalizable)) | ((forGen |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) &  M22_Class.g_classes.descriptors[classIndex].isGenForming) | (M22_Class.g_classes.descriptors[classIndex].logLastChange &  (!(forGen |  M03_Config.g_cfgGenLogChangeForGenTabs)));

M22_Class_Utilities.printSectionHeader("Product Structure Tag", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conPsOid, M01_ACM_IVK.cosnPsOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, (M22_Class.g_classes.descriptors[classIndex].psTagOptional ? "" : "NOT NULL"), addComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacPsOid, null, indent, null, "[LDM] Product Structure Tag", null, null, null, null, null), null, null);
}
if (!(forGen &  M22_Class.g_classes.descriptors[classIndex].isNationalizable)) {
addComma = forceComma |  useVersiontag | ((forGen |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) &  M22_Class.g_classes.descriptors[classIndex].isGenForming) | (M22_Class.g_classes.descriptors[classIndex].logLastChange &  (!(forGen |  M03_Config.g_cfgGenLogChangeForGenTabs)));

M22_Class_Utilities.printSectionHeader("Is this a 'nationalized' entity?", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsNational, M01_ACM_IVK.cosnIsNational, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, "NOT NULL", addComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacNationalEntityMeta |  M01_Common.AttrCategory.eacRegular, null, indent, null, "[LDM] Is this a 'nationalized' entity?", "0", null, null, null, null), null, null);
}
if ((forGen |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) &  M22_Class.g_classes.descriptors[classIndex].isGenForming) {
// ### ELSE IVK ###
//       If forGen  And .isGenForming Then
// ### ENDIF IVK ###
addComma = forceComma |  useVersiontag | (M22_Class.g_classes.descriptors[classIndex].logLastChange &  (!(forGen |  M03_Config.g_cfgGenLogChangeForGenTabs)));

M22_Class_Utilities.printSectionHeader("Validity Range", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conValidFrom, M01_ACM.cosnValidFrom, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexValTimestamp, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, "NOT NULL", null, ddlType, null, outputMode, null, null, indent, null, "[ACM] Begin timestamp of record's validity range", null, null, null, null, null), null, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conValidTo, M01_ACM.cosnValidTo, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexValTimestamp, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, "NOT NULL", addComma, ddlType, null, outputMode, null, null, indent, null, "[ACM] End timestamp of record's validity range", null, null, null, null, null), null, null);
}

if (M22_Class.g_classes.descriptors[classIndex].logLastChange &  (!(forGen |  M03_Config.g_cfgGenLogChangeForGenTabs))) {
addComma = forceComma |  useVersiontag;

if (!(forLrt |  M03_Config.g_cfgGenLogChangeForLrtTabs)) {
M24_Attribute.genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, ddlType, M22_Class.g_classes.descriptors[classIndex].className, outputMode, indent, addComma, useAlternativeDefaults);
} else if (forLrt & ! M03_Config.g_cfgGenLogChangeForLrtTabs & (outputMode &  M01_Common.DdlOutputMode.edomValueNonLrt)) {
M24_Attribute.genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, ddlType, M22_Class.g_classes.descriptors[classIndex].className, M01_Common.DdlOutputMode.edomValueNonLrt, indent, addComma, useAlternativeDefaults);
}
}

if (M22_Class.g_classes.descriptors[classIndex].useVersiontag) {
M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), forceComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacVid, null, indent, null, "[LDM] Record version tag", "1", null, null, null, null), null, null);
}
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genAttrDeclsForClassRecursiveWithColReUse(int classIndex, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer levelW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW, Integer directionW, String genParentTabNameW, Boolean suppressColConstraintsW, Boolean useAlternativeDefaultsW) {
int level; 
if (levelW == null) {
level = 1;
} else {
level = levelW;
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

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

String genParentTabName; 
if (genParentTabNameW == null) {
genParentTabName = "";
} else {
genParentTabName = genParentTabNameW;
}

boolean suppressColConstraints; 
if (suppressColConstraintsW == null) {
suppressColConstraints = false;
} else {
suppressColConstraints = suppressColConstraintsW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

//On Error GoTo ErrorExit 

M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, M24_Attribute_Utilities.nullAttributeTransformation, tabColumns, level, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode, direction, null, genParentTabName, suppressColConstraints, useAlternativeDefaults, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genAttrDeclsForClassRecursive(int classIndex, Integer levelW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW) {
int level; 
if (levelW == null) {
level = 1;
} else {
level = levelW;
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

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, M24_Attribute_Utilities.nullAttributeTransformation, tabColumns, level, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode, null, null, null, null, null, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForRelationship(int thisRelIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW) {
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

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(thisRelIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genAttrDeclsForRelationship(int thisRelIndex, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean suppressMetaAttrsW, Boolean forLrtW, Integer outputModeW) {
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

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(thisRelIndex, M24_Attribute_Utilities.nullAttributeTransformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genAttrDeclsForEnum(int thisEnumIndex, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer outputModeW) {
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
outputMode = M01_Common.DdlOutputMode.edomDecl;
} else {
outputMode = outputModeW;
}

//On Error GoTo ErrorExit 

M24_Attribute.genAttrDeclsForEntity(M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, thisEnumIndex, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, null, null, outputMode, null, null, null, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForEnum(int thisEnumIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Integer outputModeW, Boolean useVersiontagW) {
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

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

boolean useVersiontag; 
if (useVersiontagW == null) {
useVersiontag = true;
} else {
useVersiontag = useVersiontagW;
}

//On Error GoTo ErrorExit 

M24_Attribute.genTransformedAttrDeclsForEntity(M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, transformation, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, false, null, !(useVersiontag), false, outputMode, indent, null, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, null);

M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, transformation, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, outputMode, M01_Common.AttrCategory.eacVid, null, indent, null, null, null, null, null, null), null, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForEnumWithColReuse(int thisEnumIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Integer outputModeW, Boolean useVersiontagW) {
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

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

boolean useVersiontag; 
if (useVersiontagW == null) {
useVersiontag = true;
} else {
useVersiontag = useVersiontagW;
}

M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, transformation, tabColumns, false, fileNo, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, false, null, null, !(useVersiontag), false, outputMode, indent, null, null, null, null, null);

if (useVersiontag) {
M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomain(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, transformation, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, M21_Enum.g_enums.descriptors[thisEnumIndex].enumIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, outputMode, M01_Common.AttrCategory.eacVid, null, indent, null, null, null, null, null, null), null, null);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genDbObjComment(String objType, String objName, String objComment, int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (!(objComment.compareTo("") == 0)) {
String commentMeta;

commentMeta = "";
if (thisOrgIndex >= 0) {
commentMeta = "[MPC" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);
}
if (thisPoolIndex >= 0) {
commentMeta = commentMeta + ",DP" + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + "] ";
}

M00_FileWriter.printToFile(fileNo, "COMMENT ON " + objType.toUpperCase() + " " + objName + " IS " + "'" + commentMeta + M00_Helper.replace(objComment, "'", "''") + "'" + M01_LDM.gc_sqlCmdDelim);
}
}


private static void genDbAlias(String qualAliasName, String qualRefObj, String qualRefObjLdm, String objName, String tabDescr, int sectionIndex, Boolean forLrtW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String extraCommentW) {
boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
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

String extraComment; 
if (extraCommentW == null) {
extraComment = "";
} else {
extraComment = extraCommentW;
}

int fileNoAl;
fileNoAl = M04_Utilities.openDdlFileBySectionIndex(M01_Globals.g_targetDir, sectionIndex, processingStepAlias, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex, null, M01_Globals.g_phaseIndexAliases, M01_Common.ldmIterationPostProc);

//On Error GoTo ErrorExit 
M22_Class_Utilities.printSectionHeader("Alias for " + tabDescr + (!(forLrt) ? "" : " (LRT)") + (extraComment == "" ? "" : " (" + extraComment + ")"), fileNoAl, null, null);

M00_FileWriter.printToFile(fileNoAl, "");
M00_FileWriter.printToFile(fileNoAl, "CREATE ALIAS " + qualAliasName + " FOR " + qualRefObj + M01_LDM.gc_sqlCmdDelim);

if (M03_Config.generateCommentOnAliases) {
M00_FileWriter.printToFile(fileNoAl, "");
M22_Class.genDbObjComment("ALIAS", qualAliasName, tabDescr + (forLrt ? " (LRT)" : ""), fileNoAl, thisOrgIndex, thisPoolIndex, null);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

//added paramter withTempTable (Defect 19001 wf)
public static void genTabSubQueryByEntityIndex(int acmEntityIndex, Integer acmEntityType, int fileNo,  int thisOrgIndex,  int thisPoolIndex, Integer ddlType, boolean lrtAware, boolean forGen, String tabVar,  String columnList, Integer indentW, String oidVarW, String lrtOidVarW, Boolean withTempTableW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String oidVar; 
if (oidVarW == null) {
oidVar = "";
} else {
oidVar = oidVarW;
}

String lrtOidVar; 
if (lrtOidVarW == null) {
lrtOidVar = "lrtOid_in";
} else {
lrtOidVar = lrtOidVarW;
}

boolean withTempTable; 
if (withTempTableW == null) {
withTempTable = true;
} else {
withTempTable = withTempTableW;
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


if (columnList.substring(0, M01_Globals.g_anOid.length()) != M01_Globals.g_anOid) {
columnList = M01_Globals.g_anOid + (columnList.compareTo("") == 0 ? "" : ",") + columnList;
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
String parFkAttrName;
parFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, null, null, null);

if (lrtAware &  M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional) {
if (M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt) {
if (oidVar == "" &  lrtOidVar == "" & !forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, null, null, null, null) + " " + tabVar);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
if (withTempTable) {
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + columnList + "," + M01_Globals.g_anInLrt + ",LRTSTATE," + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo + "," + "ROWNUMBER() OVER (PARTITION BY " + parFkAttrName + "," + M01_Globals.g_anInLrt + " ORDER BY (CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) " + "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)) AS ROWNUM");
// ### ELSE IVK ###
//             Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",LRTSTATE," ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
//                                                "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
//                                                "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
indent = indent + 2;
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + "," + M01_Globals.g_anInLrt + ",LRTSTATE," + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + (forGen ? "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo : "") + " FROM " + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, null, null, null, null) + (oidVar != "" |  lrtOidVar != "" ? " WHERE " : "") + (lrtOidVar == "" ? "" : "((" + M01_Globals.g_anIsLrtPrivate + " = 0 AND " + M01_Globals_IVK.g_anIsDeleted + " = 0 AND ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidVar + "))) OR " + "(" + M01_Globals.g_anIsLrtPrivate + " = 1 AND LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + " AND (" + M01_Globals.g_anInLrt + " = " + lrtOidVar + ")))") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
// ### ELSE IVK ###
//           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE," ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom;  & "," & g_anValidTo, ""); " FROM "; _
//                                              genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True); _
//                                              IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
//                                              IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
//                                              "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
//                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
// ### ENDIF IVK ###
if (forGen) {
indent = indent - 2;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ") G_" + tabVar);
}
// Branch for modification of View V_CL_GENERICASPECT (Defect 19001 wf)
// withTempTable = False
} else {
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + columnList + "," + M01_Globals.g_anInLrt + ",LRTSTATE," + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo + "," + "ROWNUMBER() OVER (PARTITION BY " + parFkAttrName + "," + M01_Globals.g_anInLrt + " ORDER BY (CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) " + "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)) AS ROWNUM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
indent = indent + 2;
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, null, null, null, null));
if (forGen) {
indent = indent - 2;
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ") " + tabVar);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + columnList + "," + M01_Globals.g_anInLrt + ",CAST(0 AS " + M01_Globals.g_dbtEnumId + ") AS LRTSTATE,CAST(0 AS " + M01_Globals.g_dbtBoolean + ") AS " + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo + "," + "ROWNUMBER() OVER (PARTITION BY " + parFkAttrName + "," + M01_Globals.g_anInLrt + " ORDER BY (CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) " + "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)) AS ROWNUM");
// ### ELSE IVK ###
//           Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
//                                              "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
//                                              "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
indent = indent + 2;
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + "," + M01_Globals.g_anInLrt + ",CAST(0 AS " + M01_Globals.g_dbtEnumId + ") AS LRTSTATE,CAST(0 AS " + M01_Globals.g_dbtBoolean + ") AS " + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "" + (forGen ? "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo : "") + " FROM " + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, null, null, null, null) + " WHERE (" + M01_Globals_IVK.g_anIsDeleted + " = 0)" + (lrtOidVar == "" ? "" : " AND ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidVar + "))") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
// ### ELSE IVK ###
//        Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
//                                           genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False); _
//                                           IIf(lrtOidVar = "", "", " WHERE ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
//                                           IIf(oidVar <> "", IIf(lrtOidVar = "", " WHERE", " AND") & " (OID = " & oidVar & ")", "")
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "UNION ALL");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + "," + M01_Globals.g_anInLrt + ",LRTSTATE,CAST(1 AS " + M01_Globals.g_dbtBoolean + ") AS " + M01_Globals.g_anIsLrtPrivate + ",CAST(0 AS " + M01_Globals.g_dbtBoolean + ") AS " + M01_Globals_IVK.g_anIsDeleted + "" + (forGen ? "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo : "") + " FROM " + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, null, null, null, null) + " WHERE (LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")" + (lrtOidVar == "" ? "" : " AND (" + M01_Globals.g_anInLrt + " = " + lrtOidVar + ")") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
// ### ELSE IVK ###
//         Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE,CAST(1 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
//                                            genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True); _
//                                            " WHERE (LRTSTATE <> " & CStr(lrtStatusDeleted) & ")"; _
//                                            IIf(lrtOidVar = "", "", " AND (" & g_anInLrt & " = " & lrtOidVar & ")"); _
//                                            IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
// ### ENDIF IVK ###
if (forGen) {
indent = indent - 2;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ") G_" + tabVar);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ") " + tabVar);
}
} else {
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + columnList + "," + M01_Globals.g_anInLrt + ",CAST(0 AS " + M01_Globals.g_dbtEnumId + ") AS LRTSTATE,CAST(0 AS " + M01_Globals.g_dbtBoolean + ") AS " + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "," + M01_Globals_IVK.g_anValidFrom + "," + M01_Globals_IVK.g_anValidTo + "," + "ROWNUMBER() OVER (PARTITION BY " + parFkAttrName + " ORDER BY (CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) " + "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)) AS ROWNUM");
// ### ELSE IVK ###
//         Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
//                                            "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
//                                            "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ") " + tabVar);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null) + " " + tabVar);
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (lrtAware &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional) {
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt) {
if (oidVar == "" &  lrtOidVar == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null) + " " + tabVar);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + " FROM " + M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null) + (oidVar != "" |  lrtOidVar != "" ? " WHERE " : "") + (lrtOidVar == "" ? "" : "((" + M01_Globals.g_anIsLrtPrivate + " = 0 AND " + M01_Globals_IVK.g_anIsDeleted + " = 0 AND ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidVar + "))) OR " + "(" + M01_Globals.g_anIsLrtPrivate + " = 1 AND LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + " AND (" + M01_Globals.g_anInLrt + " = " + lrtOidVar + ")))") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
// ### ELSE IVK ###
//           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
//                                              genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True); _
//                                              IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
//                                              IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
//                                              "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
//                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ") " + tabVar);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + " FROM " + M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, null) + " WHERE (" + M01_Globals_IVK.g_anIsDeleted + " = 0)" + (lrtOidVar == "" ? "" : " AND ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidVar + "))") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
// ### ELSE IVK ###
//        Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
//                                           genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, thisPoolIndex, False); _
//                                           IIf(lrtOidVar = "", "", " WHERE ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
//                                           IIf(oidVar <> "", IIf(lrtOidVar = "", " WHERE", " AND") & " (OID = " & oidVar & ")", "")
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT " + columnList + " FROM " + M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null) + " WHERE (LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")" + (lrtOidVar == "" ? "" : " AND (" + M01_Globals.g_anInLrt + " = " + lrtOidVar + ")") + (oidVar != "" ? " AND (OID = " + oidVar + ")" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ") " + tabVar);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null) + " " + tabVar);
}
}
}


// ### IF IVK ###
private static void genGetCodePropertyGroupByPriceAssignmentFunction(int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (M03_Config.generateFwkTest) {
return;
}

String qualObjNameCpGroupHasProperty;

boolean isWorkDataPool;
boolean isProductiveDataPool;
boolean isArchiveDataPool;
boolean M72_DataPool.poolSupportLrt;

if (thisPoolIndex > 0) {
isWorkDataPool = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
isProductiveDataPool = M72_DataPool.g_pools.descriptors[thisPoolIndex].isProductive;
isArchiveDataPool = M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;

if (isArchiveDataPool) {
return;
}
}

String qualFuncName;

boolean lrtAware;
int k;
for (int k = 1; k <= (M72_DataPool.poolSupportLrt ? 2 : 1); k++) {
lrtAware = (k == 2);
qualFuncName = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, M01_ACM_IVK.udfnGetCpgByPriceAssignment, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function retrieving the OID of the CODEPROPERTYGROUP corresponding to CODEPRICEASSIGNMENT" + (lrtAware ? " (LRT-aware)" : ""), fileNo, null, null);

int relIndexCpGroupHasProperty;
relIndexCpGroupHasProperty = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCpGroupHasProperty, M01_ACM_IVK.rnCpGroupHasProperty, null);

if (lrtAware) {
qualObjNameCpGroupHasProperty = M04_Utilities.genQualViewNameByRelIndex(relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, lrtAware, M03_Config.useMqtToImplementLrt, null, null, null, null, null);
} else {
qualObjNameCpGroupHasProperty = M04_Utilities.genQualViewNameByRelIndex(relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, lrtAware, "OID of '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionShortName + "'-object");
if (lrtAware) {
M11_LRT.genProcParm(fileNo, "", "lrtOid_in", M01_Globals.g_dbtOid, false, "OID of the LRT used for reference");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_cpgOid", M01_Globals.g_dbtOid, "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "retrieve OID of CODEPROPERTYGROUP", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cpgOid = (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CPG." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "GAS", M01_Globals.g_anOid + ", PRPAPR_OID, BESESL_OID", 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M11_LRT.genProcSectionHeader(fileNo, "1st Navigation: PriceAssignment -> (Numeric)Property -> CodePropertyGroup", 3, true);

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "PRP", M01_Globals.g_anOid + ", CLASSID", 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRP." + M01_Globals.g_anOid + " = GAS.PRPAPR_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRP." + M01_Globals.g_anCid + " = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexNumericProperty].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_relIndexCpGroupHasProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "CHP", "CPG_OID, PRP_OID", 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CHP.PRP_OID = PRP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexCodePropertyGroup, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "CPG", M01_Globals.g_anOid + ", CGCHCA_OID", 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CPG." + M01_Globals.g_anOid + " = CHP.CPG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M11_LRT.genProcSectionHeader(fileNo, "2nd Navigation: CodePriceAssignment -> EndSlot -> Category -> CodePropertyGroup", 3, true);

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "ESL", M01_Globals.g_anOid + ", CLASSID, ESCESC_OID", 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESL." + M01_Globals.g_anOid + " = GAS.BESESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M11_LRT.genProcSectionHeader(fileNo, "filter criterion on ENDSLOT", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESL." + M01_Globals.g_anCid + " = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexMasterEndSlot].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "CAT", M01_Globals.g_anOid, 3, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESL.ESCESC_OID = CAT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M11_LRT.genProcSectionHeader(fileNo, "filter criterion on GENERICASPECT", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M11_LRT.genProcSectionHeader(fileNo, "intersect both navigation paths", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CPG.CGCHCA_OID = CAT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CPG." + M01_Globals.g_anOid + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_cpgOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}


// ### ENDIF IVK ###
// ### IF IVK ###
public static void genAliasDdl(int sectionIndex, String objName, boolean isCommonToOrgs, boolean isCommonToPools, boolean isAcmRelated, String qualRefObjNameLdm, String qualRefObjNamePdm,  Boolean isCtoAliasCreatedW, Integer ddlTypeW,  Integer objOrgIndexW,  Integer objPoolIndexW, Integer aliasTypeW, Boolean forGenW, Boolean forLrtW, boolean forLrtDeletedObjects = false, Boolean forPsDpFilterW, Boolean forPsDpFilterExtendedW, String commentW, String suffixW,  Boolean objSupportsLrtW,  Boolean objIsPsTaggedW,  Boolean objSupportsPsDpFilterW,  Boolean objIsArchiveW,  Boolean objSupportsLogChangeW,  Boolean suppressGenSuffixW,  Boolean forRegularSchemaOnlyW) {
boolean isCtoAliasCreated; 
if (isCtoAliasCreatedW == null) {
isCtoAliasCreated = false;
} else {
isCtoAliasCreated = isCtoAliasCreatedW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int objOrgIndex; 
if (objOrgIndexW == null) {
objOrgIndex = -1;
} else {
objOrgIndex = objOrgIndexW;
}

int objPoolIndex; 
if (objPoolIndexW == null) {
objPoolIndex = -1;
} else {
objPoolIndex = objPoolIndexW;
}

Integer aliasType; 
if (aliasTypeW == null) {
aliasType = null;
} else {
aliasType = aliasTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forLrtDeletedObjects; 
if (forLrtDeletedObjectsW == null) {
forLrtDeletedObjects = false;
} else {
forLrtDeletedObjects = forLrtDeletedObjectsW;
}

boolean forPsDpFilter; 
if (forPsDpFilterW == null) {
forPsDpFilter = false;
} else {
forPsDpFilter = forPsDpFilterW;
}

boolean forPsDpFilterExtended; 
if (forPsDpFilterExtendedW == null) {
forPsDpFilterExtended = false;
} else {
forPsDpFilterExtended = forPsDpFilterExtendedW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean objSupportsLrt; 
if (objSupportsLrtW == null) {
objSupportsLrt = false;
} else {
objSupportsLrt = objSupportsLrtW;
}

boolean objIsPsTagged; 
if (objIsPsTaggedW == null) {
objIsPsTagged = false;
} else {
objIsPsTagged = objIsPsTaggedW;
}

boolean objSupportsPsDpFilter; 
if (objSupportsPsDpFilterW == null) {
objSupportsPsDpFilter = false;
} else {
objSupportsPsDpFilter = objSupportsPsDpFilterW;
}

boolean objIsArchive; 
if (objIsArchiveW == null) {
objIsArchive = false;
} else {
objIsArchive = objIsArchiveW;
}

boolean objSupportsLogChange; 
if (objSupportsLogChangeW == null) {
objSupportsLogChange = false;
} else {
objSupportsLogChange = objSupportsLogChangeW;
}

boolean suppressGenSuffix; 
if (suppressGenSuffixW == null) {
suppressGenSuffix = false;
} else {
suppressGenSuffix = suppressGenSuffixW;
}

boolean forRegularSchemaOnly; 
if (forRegularSchemaOnlyW == null) {
forRegularSchemaOnly = false;
} else {
forRegularSchemaOnly = forRegularSchemaOnlyW;
}

// ### ELSE IVK ###
//Sub genAliasDdl( _
//  ByRef sectionIndex As Integer, _
//  ByRef objName As String, _
//  isCommonToOrgs As Boolean, _
//  isCommonToPools As Boolean, _
//  isAcmRelated As Boolean, _
//  ByRef qualRefObjNameLdm As String, _
//  ByRef qualRefObjNamePdm As String, _
//  Optional ByVal isCtoAliasCreated As Boolean = False, _
//  Optional ddlType As DdlTypeId = edtLdm, _
//  Optional ByVal objOrgIndex As Integer = -1, _
//  Optional ByVal objPoolIndex As Integer = -1, _
//  Optional aliasType As DbAliasEntityType, _
//  Optional forGen As Boolean = False, _
//  Optional forLrt As Boolean = False, _
//  Optional ByRef comment As String = "", _
//  Optional byref suffix As String = "", _
//  Optional ByVal objSupportsLrt As Boolean = False, _
//  Optional ByVal objSupportsLogChange As Boolean = False, _
//  Optional ByVal suppressGenSuffix As Boolean = False, _
//  Optional ByVal forRegularSchemaOnly As Boolean = False _
//)
// ### ENDIF IVK ###
if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

boolean mapViewToTab;
boolean objMapsViewToTab;
boolean skipAliasInNonRegularSchemas;

int thisOrgIndex;
int thisPoolIndex;
// ### IF IVK ###
if (objPoolIndex > 0) {
objIsArchive = (objIsArchive &  M72_DataPool.g_pools.descriptors[objPoolIndex].isArchive);
}

objMapsViewToTab = objIsPsTagged |  objSupportsLrt | objSupportsLogChange | objIsArchive;
// ### ELSE IVK ###

// objMapsViewToTab = objSupportsLogChange
// ### ENDIF IVK ###
mapViewToTab = objMapsViewToTab &  aliasType == M01_Common.DbAliasEntityType.edatView;
skipAliasInNonRegularSchemas = (objMapsViewToTab &  aliasType == M01_Common.DbAliasEntityType.edatTable) |  forRegularSchemaOnly;

String qualAliasNamePdm;
final String lrtAliasComment = "LRT-Alias-Schema";

if (isCommonToOrgs) {
if (!(qualRefObjNamePdm.compareTo("VL6CMET.V_GROUP_NL_TEXT") == 0) &  !(qualRefObjNamePdm.compareTo("VL6CMET.V_AGGREGATIONNODE_NL_TEXT") == 0) & !(qualRefObjNamePdm.compareTo("VL6CMET.V_ENDNODE_NL_TEXT") == 0)) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  (objPoolIndex > 0 ? M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal : !(M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal))) {
// if we explicitly specified a pool then this pool implements common items locally
// ### IF IVK ###
if ((objIsPsTagged |  objIsArchive) &  aliasType == M01_Common.DbAliasEntityType.edatTable) {
// generate Alias in 'regular schema'
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "Data Pool Alias Schema");

// we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
// instead we generate aliases for PS-Tagging-Views which 'look like' the corresponding tables
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
// generate Alias in LRT-Alias-Schema
// we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-org-branch
// ### IF IVK ###
if (!(forPsDpFilter & ! forPsDpFilterExtended & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, false, false, suppressGenSuffix);
// ### ELSE IVK ###
//               If Not skipAliasInNonRegularSchemas Then
//                 qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
//                   IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "LRT-Alias-Schema");
}

// ### IF IVK ###
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt |  M03_Config.supportAliasDelForNonLrtPools) {
// generate Alias in LRT-Alias-Schema for deleted objects
// we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-org-branch
if (!(objIsArchive & ! forPsDpFilter & !forPsDpFilterExtended & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, true, false, false, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "LRT-Alias-Schema for deleted objects");
}
}

if (M03_Config.supportFilteringByPsDpMapping) {
// generate Alias in Alias-Schema for PS-DP-Filtering if this is not 'for deleted objects'
if ((forPsDpFilter | ! objSupportsPsDpFilter) & ! skipAliasInNonRegularSchemas) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, true, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}

if ((forPsDpFilterExtended | ! objSupportsPsDpFilter) & ! skipAliasInNonRegularSchemas) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, null, true, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}
}

// ### ENDIF IVK ###
// ### IF IVK ###
if (!((objIsPsTagged |  objIsArchive))) {
// generate Alias in 'regular schema'
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
// ### ELSE IVK ###
// ### INDENT IVK ### -4
//                 ' generate Alias in 'regular schema'
//                 qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
//                   aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, thisOrgIndex, thisPoolIndex, "Data Pool Alias Schema");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

// ### IF IVK ###
if (!(isCtoAliasCreated &  objPoolIndex <= 0 & !forPsDpFilter & !forPsDpFilterExtended)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, null, null, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
// ### ELSE IVK ###
//               If Not isCtoAliasCreated And objPoolIndex <= 0 Then
//                 qualAliasNamePdm = genQualAliasName(objName, ddlType, , , aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, null, null, null);
isCtoAliasCreated = true;
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}
}
}
} else {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, null, null, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, null, null, null);
}
} else if (isCommonToPools) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, objOrgIndex) &  (objPoolIndex > 0 ? M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal : !(M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal))) {
// if we explicitly specified a pool then this pool implements items locally
// ### IF IVK ###
if ((objIsPsTagged |  objSupportsLogChange | objIsArchive) &  (aliasType == M01_Common.DbAliasEntityType.edatTable)) {
// generate Alias in 'regular schema'
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
// ### ELSE IVK ###
//           If objSupportsLogChange And (aliasType = edatTable) Then
//             ' generate Alias in 'regular schema'
//             qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "Data Pool Alias Schema");

// we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
// instead we generate aliases for PS-Tagging-Views which 'look like' the corresponding tables
} else {

// generate Alias in LRT-Alias-Schema
// we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-pools-branch
// ### IF IVK ###
if (!(forPsDpFilter & ! forPsDpFilterExtended & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, false, false, suppressGenSuffix);
// ### ELSE IVK ###
//             If Not skipAliasInNonRegularSchemas Then
//               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
//                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "LRT-Alias-Schema");
}

// ### IF IVK ###
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt |  M03_Config.supportAliasDelForNonLrtPools) {
// generate Alias in LRT-Alias-Schema for deleted objects
// we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-pools-branch

if ((!(objIsArchive |  forLrtDeletedObjects)) & ! skipAliasInNonRegularSchemas & !forPsDpFilter & !forPsDpFilterExtended) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, true, false, false, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "LRT-Alias-Schema for deleted objects");
}
}

if (M03_Config.supportFilteringByPsDpMapping) {
// generate Alias in Alias-Schema for PS-DP-Filtering
if ((forPsDpFilter | ! objSupportsPsDpFilter) & ! M03_Config.supportAliasDelForNonLrtPools) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, true, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}

if ((forPsDpFilterExtended | ! objSupportsPsDpFilter) & ! M03_Config.supportAliasDelForNonLrtPools) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, null, true, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}
}

// ### ENDIF IVK ###
// ### IF IVK ###
if (!((objIsPsTagged |  objIsArchive))) {
// generate Alias in 'regular schema'
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, suppressGenSuffix, null);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
//               ' generate Alias in 'regular schema'
//               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, "Data Pool Alias Schema");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}
}
}
} else {
if (M72_DataPool.g_pools.descriptors[objPoolIndex].supportAcm) {
if (M72_DataPool.poolIsValidForOrg(objPoolIndex, objOrgIndex)) {
// generate Alias in LRT-Alias-Schema
// ### IF IVK ###
if ((objIsPsTagged |  objSupportsLrt | objIsArchive) &  aliasType == M01_Common.DbAliasEntityType.edatTable) {
// generate Alias in 'regular schema'
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
// ### ELSE IVK ###
//       If objSupportsLrt And aliasType = edatTable Then
//         ' generate Alias in 'regular schema'
//         qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "Data Pool Alias Schema");

// we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
// instead we generate aliases for LRT-Views which 'look like' the corresponding tables
} else {
// ### IF IVK ###
// generate Alias in LRT-Alias-Schema if this is not 'for deleted objects' and not 'for Ps-Dp Filter'
if (!(forLrtDeletedObjects & ! forPsDpFilter & !forPsDpFilterExtended & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, null, null, suppressGenSuffix);
// ### ELSE IVK ###
//         ' generate Alias in LRT-Alias-Schema
//         If Not skipAliasInNonRegularSchemas Then
//           qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "LRT-Alias-Schema");
}

// ### IF IVK ###
if (M03_Config.supportFilteringByPsDpMapping) {
// generate Alias in Alias-Schema for PS-DP-Filtering if this is not 'for deleted objects'
if (!(forLrtDeletedObjects &  (forPsDpFilter | ! objSupportsPsDpFilter) & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, true, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}

if (!(forLrtDeletedObjects &  (forPsDpFilterExtended | ! objSupportsPsDpFilter) & !skipAliasInNonRegularSchemas)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, false, null, true, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "Alias-Schema for filtering by PSDPMAPPING");
}
}

if (M72_DataPool.g_pools.descriptors[objPoolIndex].supportLrt |  M03_Config.supportAliasDelForNonLrtPools) {
// generate Alias in LRT-Alias-Schema for deleted objects
if ((!((objIsPsTagged |  objSupportsLrt | objIsArchive) |  forLrtDeletedObjects)) & ! skipAliasInNonRegularSchemas & !forPsDpFilter & !forPsDpFilterExtended) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, true, null, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "LRT-Alias-Schema for deleted objects");
}
}

// add aliases for tables not CTO, not CTP, ACM-related and not user transaction, in work data pools, not for NL_TEXT
if ((objPoolIndex == M01_Globals.g_workDataPoolIndex & ! objSupportsLrt & isAcmRelated & !forPsDpFilter & !forPsDpFilterExtended & !skipAliasInNonRegularSchemas & !M00_Helper.inStr(1, objName.toUpperCase(), "NL_TEXT") > 0)) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, (mapViewToTab ? M01_Common.DbAliasEntityType.edatTable : aliasType), forGen, forLrt, suffix, true, true, null, null, suppressGenSuffix);
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "Alias-Schema for deleted objects");
}

// ### ENDIF IVK ###
}

// generate Alias in 'regular schema'
// ### IF IVK ###
if (!((objIsPsTagged |  objSupportsLrt | objIsArchive))) {
qualAliasNamePdm = M04_Utilities.genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, null, null, null, null, suppressGenSuffix);
// ### ELSE IVK ###
//       If Not objSupportsLrt Then
//         qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
// ### ENDIF IVK ###
genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, "Data Pool Alias Schema");
}
}
}
}

NormalExit:
M04_Utilities.closeAllDdlFiles(null, null, sectionIndex, processingStepAlias, M01_Globals.g_phaseIndexAliases, ddlType);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropClassIdList(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

if (!(M03_Config.generateEntityIdList)) {
return;
}

M04_Utilities.killFile(M04_Utilities.genMetaFileName(M01_Globals.g_targetDir, "ClassId", null), onlyIfEmpty);
}


public static void genClassIdList() {
if (!(M03_Config.generateEntityIdList)) {
return;
}

String fileName;
fileName = M04_Utilities.genMetaFileName(M01_Globals.g_targetDir, "ClassId", null);
M04_Utilities.assertDir(fileName);
int fileNo;
fileNo = M00_FileWriter.freeFileNumber();

//On Error GoTo ErrorExit 
M00_FileWriter.openFileForOutput(fileNo, fileName, false);

int thisClassIndex;
int maxQualClassNameLen;
maxQualClassNameLen = 0;

for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
if (!(M22_Class.g_classes.descriptors[thisClassIndex].notAcmRelated &  M22_Class.g_classes.descriptors[thisClassIndex].classId > 0)) {
if (M22_Class.g_classes.descriptors[thisClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[thisClassIndex].className.length() > maxQualClassNameLen) {
maxQualClassNameLen = M22_Class.g_classes.descriptors[thisClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[thisClassIndex].className.length();
}
}
}

for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
if (!(M22_Class.g_classes.descriptors[thisClassIndex].notAcmRelated &  M22_Class.g_classes.descriptors[thisClassIndex].classId > 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.paddRight(M22_Class.g_classes.descriptors[thisClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[thisClassIndex].className, maxQualClassNameLen, null) + " : " + M22_Class.g_classes.descriptors[thisClassIndex].classIdStr);
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


private static void genClassDdl(int classIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
int noIndexesInPool;
int fileNo;
int fileNoCl;
int fileNoLrt;
int fileNoLrtView;
int fileNoLrtSup;
int fileNoLc;
int fileNoFk;
// ### IF IVK ###
int fileNoXmlF;
int fileNoXmlV;
int fileNoSetProd;
int fileNoSetProdCl;
int fileNoFto;
int fileNoGaSup;
int fileNoPs;
int fileNoPsCopy;
int fileNoPsCopy2;
int fileNoExpCopy;
int fileNoArc;
boolean isGenericAspect;
boolean isDivTagged;
int thisPartitionIndex;
String lbClassIdStr;
String ubClassIdStr;
boolean supportPartitionByClassId;
Integer tabPartitionType;
// ### ENDIF IVK ###

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

// ### IF IVK ###
int orgSetProductiveTargetPoolIndex;
boolean orgIsPrimary;

if (thisOrgIndex < 1) {
orgSetProductiveTargetPoolIndex = -1;
orgIsPrimary = false;
} else {
orgSetProductiveTargetPoolIndex = M71_Org.g_orgs.descriptors[thisOrgIndex].setProductiveTargetPoolIndex;
orgIsPrimary = M71_Org.g_orgs.descriptors[thisOrgIndex].isPrimary;
}

// ### ENDIF IVK ###
thisOrgDescriptorStr = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);

int ldmIteration;
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].notPersisted) {
return;
}

// ### ENDIF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].sectionName + "" == "") {
goto NormalExit;
}

if (M03_Config.ignoreUnknownSections &  (M22_Class.g_classes.descriptors[classIndex].sectionIndex < 0)) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (!(M20_Section_Utilities.sectionValidForPoolAndOrg(M22_Class.g_classes.descriptors[classIndex].sectionIndex, thisOrgIndex, thisPoolIndex))) {
goto NormalExit;
}
}

if (M22_Class.g_classes.descriptors[classIndex].isLrtSpecific & ! M01_Globals.g_genLrtSupport) {
goto NormalExit;
}

if (M22_Class.g_classes.descriptors[classIndex].isPdmSpecific &  ddlType != M01_Common.DdlTypeId.edtPdm) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M22_Class.g_classes.descriptors[classIndex].specificToOrgId >= 0 & M22_Class.g_classes.descriptors[classIndex].specificToOrgId != thisOrgId) {
goto NormalExit;
}

// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  M22_Class.g_classes.descriptors[classIndex].specificToPool >= 0 & M22_Class.g_classes.descriptors[classIndex].specificToPool != thisPoolId & thisPoolId != M01_Globals_IVK.g_migDataPoolId) {
// ### ELSE IVK ###
//   If ddlType = edtPdm And .specificToPool >= 0 And .specificToPool <> thisPoolId Then
// ### ENDIF IVK ###
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolId != -1) {
if (!(M22_Class.g_classes.descriptors[classIndex].notAcmRelated & ! poolSupportAcm)) {
goto NormalExit;
}
}

// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex == M01_Globals_IVK.g_archiveDataPoolIndex & !M03_Config.supportArchivePool) {
goto NormalExit;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex == M01_Globals_IVK.g_archiveDataPoolIndex & !M22_Class.g_classes.descriptors[classIndex].isSubjectToArchiving & !M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
goto NormalExit;
}

isDivTagged = (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex > 0) & ! (M22_Class.g_classes.descriptors[classIndex].classIndex == M01_Globals_IVK.g_classIndexProductStructure);

// ### ENDIF IVK ###
ldmIteration = (M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs ? M01_Common.ldmIterationGlobal : M01_Common.ldmIterationPoolSpecific);
// ### IF IVK ###
isGenericAspect = (M22_Class.g_classes.descriptors[classIndex].className.toUpperCase() == "GENERICASPECT");
supportPartitionByClassId = M03_Config.supportRangePartitioningByClassId &  M22_Class.g_classes.descriptors[classIndex].subClassIdStrSeparatePartition.numMaps > 0;
// ### ENDIF IVK ###

fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseRegularTables, ldmIteration);
fileNoFk = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseFksRelTabs, M01_Common.ldmIterationPoolSpecific);

// ### IF IVK ###
if (isGenericAspect) {
fileNoGaSup = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepMiscMeta, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseGaSyncSupport, ldmIteration);
}
// ### ENDIF IVK ###

if (M03_Config.generateLrt) {
fileNoLrt = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrt, ldmIteration);

fileNoLrtView = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrtViews, ldmIteration);

fileNoCl = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseChangeLogViews, ldmIteration);

fileNoLrtSup = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrtSupport, ldmIteration);
// ### IF IVK ###

if (orgSetProductiveTargetPoolIndex > 0) {
// we need to place this DDL into the file corresponding to the 'higher pool id'! otherwise this results in errors during deployment
fileNoSetProd = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);

fileNoSetProdCl = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, null, M01_Common.phaseChangeLogViews, ldmIteration);
}

if (!(orgIsPrimary & ! M22_Class.g_classes.descriptors[classIndex].noFto)) {
fileNoFto = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepFto, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (M03_Config.generateXmlExportSupport) {
fileNoXmlV = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseXmlExport, ldmIteration);

if (M03_Config.generateXsdInCtoSchema &  ddlType == M01_Common.DdlTypeId.edtPdm & thisOrgIndex > 0) {
fileNoXmlF = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, null, null, null, M01_Common.phaseXmlExport, ldmIteration);
} else {
fileNoXmlF = fileNoXmlV;
}
}

if (M03_Config.generatePsTaggingView &  M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
fileNoPs = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phasePsTagging, ldmIteration);
}

if (M22_Class.g_classes.descriptors[classIndex].logLastChange) {
if () |  (M03_Config.generateLogChangeView & ! M22_Class.g_classes.descriptors[classIndex].isUserTransactional & !M22_Class.g_classes.descriptors[classIndex].isPsTagged & M22_Class.g_classes.descriptors[classIndex].logLastChangeInView)) {
if (fileNoPs > 0) {
fileNoLc = fileNoPs;
} else {
fileNoLc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLogChange, ldmIteration);
}
}
}

if (M01_Globals.g_genLrtSupport &  M03_Config.generatePsCopySupport & (M22_Class.g_classes.descriptors[classIndex].isPsForming |  M22_Class.g_classes.descriptors[classIndex].supportExtendedPsCopy) & M22_Class.g_classes.descriptors[classIndex].isUserTransactional) {
fileNoPsCopy = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepPsCopy, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
fileNoPsCopy2 = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepPsCopy2, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}

if (M01_Globals.g_genLrtSupport &  M03_Config.generateExpCopySupport & M22_Class.g_classes.descriptors[classIndex].isSubjectToExpCopy) {
fileNoExpCopy = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStepExpCopy, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, ldmIteration);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.supportArchivePool) {
if (thisPoolIndex == M01_Globals_IVK.g_productiveDataPoolIndex) {
fileNoArc = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M22_Class.g_classes.descriptors[classIndex].sectionIndex, processingStep, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, null, M01_Common.phaseArchive, ldmIteration);
}
}

// ### ENDIF IVK ###
//On Error GoTo ErrorExit

if (M22_Class.g_classes.descriptors[classIndex].superClass + "" != "") {
// ### IF IVK ###
goto GenXmlExport;
// ### ELSE IVK ###
//     GoTo NormalExit
// ### ENDIF IVK ###
}

noIndexesInPool = M22_Class.g_classes.descriptors[classIndex].noIndexesInPool;

boolean genSupportForLrt;
genSupportForLrt = false;
if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional) {
if (thisPoolId > 0) {
genSupportForLrt = M72_DataPool.poolSupportLrt;
} else {
genSupportForLrt = (ddlType == M01_Common.DdlTypeId.edtLdm) & ! M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs & !M22_Class.g_classes.descriptors[classIndex].isCommonToPools;
}
}

// (optionally) loop twice over the table structure: first run: 'Main' table + GEN-table; second run: corresponding LRT-tables
int loopCount;
int iteration;
boolean forLrt;
loopCount = (genSupportForLrt ? 2 : 1);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
String qualTabName;
String qualTabNameLdm;
boolean isAggregateHead;
M24_Attribute_Utilities.AttributeListTransformation transformation;

isAggregateHead = (M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[classIndex].classIndex);
for (iteration = 1; iteration <= 1; iteration += (1)) {
forLrt = (iteration == 2);

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

qualTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null, null, null, null, null);
qualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, false, forLrt, null, null, null, null, null);

M96_DdlSummary.addTabToDdlSummary(qualTabName, ddlType, M22_Class.g_classes.descriptors[classIndex].notAcmRelated);
M78_DbMeta.registerQualTable(qualTabNameLdm, qualTabName, M22_Class.g_classes.descriptors[classIndex].classIndex, M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, ddlType, M22_Class.g_classes.descriptors[classIndex].notAcmRelated, false, forLrt, false, null);

if (M03_Config.generateDdlCreateTable) {
if (M22_Class.g_classes.descriptors[classIndex].classId >= 0 & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
M22_Class_Utilities.printChapterHeader("ACM-Class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\"" + (!(forLrt) ? "" : " (LRT)"), fileNo);
} else {
M22_Class_Utilities.printChapterHeader("LDM-Table \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\"" + (!(forLrt) ? "" : " (LRT)"), fileNo);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming &  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, true, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, false, false, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
}
// ### ELSE IVK ###
//       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , , poolcommonItemsLocal, poolcommonItemsLocal
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, ")");

// ### IF IVK ###
String fkAttrToDiv;
fkAttrToDiv = "";
if (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex > 0) {
if (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
}

M22_Class.genTabDeclTrailer(fileNo, ddlType, isDivTagged, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, false, forLrt, false, supportPartitionByClassId, fkAttrToDiv, tabPartitionType);
// ### ELSE IVK ###
//       genTabDeclTrailer fileNo, ddlType, eactClass, .classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
} else {
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming &  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, true, forLrt, M01_Common.DdlOutputMode.edomNone, null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, false, false, forLrt, M01_Common.DdlOutputMode.edomNone, null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, forLrt, M01_Common.DdlOutputMode.edomNone, null, null, null, poolCommonItemsLocal, poolCommonItemsLocal, null);
}
// ### ELSE IVK ###
//       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomNone, , , , poolcommonItemsLocal, poolcommonItemsLocal
// ### ENDIF IVK ###
}

if ((forLrt &  M03_Config.lrtTablesVolatile) |  M22_Class.g_classes.descriptors[classIndex].isVolatile) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE " + qualTabName + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
M24_Attribute.genPKForClass(qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, null, forLrt, null, poolSuppressUniqueConstraints, tabPartitionType);
// ### ELSE IVK ###
//     genPKForClass qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, , forLrt, , poolsuppressUniqueConstraints
// ### ENDIF IVK ###

if (!(((ddlType == M01_Common.DdlTypeId.edtPdm) &  (noIndexesInPool >= 0) & (noIndexesInPool == thisPoolId)))) {
// ### IF IVK ###
M76_Index.genIndexesForEntity(qualTabName, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, false, forLrt, false, false, poolSuppressUniqueConstraints, tabPartitionType);
// ### ELSE IVK ###
//       genIndexesForEntity qualTabName, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, False, poolsuppressUniqueConstraints
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (!(forLrt & ! poolSuppressRefIntegrity)) {
M24_Attribute.genEnumFKsForClassRecursive(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, false, false, 1, tabPartitionType);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming &  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M24_Attribute.genEnumFKsForClassRecursive(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, true, false, 1, tabPartitionType);
}
M24_Attribute.genFKsForPsTagOnClass(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forLrt, null, null, null, tabPartitionType);
}

if (!(forLrt)) {
M24_Attribute.genFKsForRelationshipsByClassRecursive(qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, null, null, null, tabPartitionType);
if ((classIndex == M01_Globals_IVK.g_classIndexGenericAspect)) {
M24_Attribute.genFKCheckSPForRelationshipByClassAndName(qualTabName, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType);
}
}
if (genSupportForLrt & ! poolSuppressRefIntegrity) {
M24_Attribute.genFksForLrtByEntity(qualTabName, qualTabNameLdm, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, false, forLrt, null, tabPartitionType);
}

if (transformation.containsNlAttribute |  (forLrt &  ((isAggregateHead &  M22_Class.g_classes.descriptors[classIndex].implicitelyGenChangeComment) |  M22_Class.g_classes.descriptors[classIndex].enforceLrtChangeComment))) {
M24_Attribute.genNlsTabsForClassRecursive(classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, false, forLrt, poolCommonItemsLocal);
}
// ### ELSE IVK ###
//     If Not forLrt And Not pool.suppressRefIntegrity Then
//       genEnumFKsForClassRecursive qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, 1
//     End If
//
//     If Not forLrt Then
//       genFKsForRelationshipsByClassRecursive qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType
//     End If
//     If genSupportForLrt And Not pool.suppressRefIntegrity Then
//       genFksForLrtByEntity qualTabName, qualTabNameLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt
//     End If
//
//     If transformation.containsNlAttribute Or (forLrt And isAggregateHead) Then
//       genNlsTabsForClassRecursive classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, False, forLrt, poolcommonItemsLocal
//     End If
// ### ENDIF IVK ###

if (M03_Config.generateCommentOnTables & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M22_Class.genDbObjComment("TABLE", qualTabName, "ACM-Class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\"" + (forLrt ? " (LRT)" : ""), fileNo, thisOrgIndex, thisPoolIndex, null);
}

if (M03_Config.generateCommentOnColumns & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "COMMENT ON " + qualTabName + " (");
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming &  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, true, forLrt, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null);
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, false, false, forLrt, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null);
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, forLrt, M01_Common.DdlOutputMode.edomComment, null, null, null, null, null, null);
}
// ### ELSE IVK ###
//       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomComment
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
// ### IF IVK ###

if (!(forLrt)) {
if (M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses &  (!(M72_DataPool.poolSupportLrt | ! M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt)) & poolSupportUpdates) {
// create INSERT-trigger to maintain derived attributes (for LRT-MQT-supported classes this is done in MQT-triggers)
genVirtualAttrTrigger(fileNoLrtSup, classIndex, qualTabName, thisOrgIndex, thisPoolIndex, ddlType, false, null);
}
}

if ((M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses |  M22_Class.g_classes.descriptors[classIndex].hasRelBasedVirtualAttrInNonGenInclSubClasses) &  poolSupportUpdates) {
M11_VirtualAttrs.genVirtAttrSupportForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, null, forLrt, null);
}
if (M22_Class.g_classes.descriptors[classIndex].hasGroupIdAttrInNonGenInclSubClasses &  poolSupportUpdates) {
M11_GroupIdAttrs.genGroupIdSupportForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, null, forLrt, null);
}
// ### ENDIF IVK ###

// GEN-Tabs if class is Generation-Forming
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
// ### ELSE IVK ###
//     If .isGenForming Then
// ### ENDIF IVK ###
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

String qualTabNameGen;
String qualTabNameGenLdm;
qualTabNameGen = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, forLrt, null, null, null, null, null);
//Defect 19643 wf
//Folgender Aufruf wird erreicht für Tabelle VL6CPST011.PROPERTY_GEN_LRT, aber nicht für VL6CPST011.PROPERTY_GEN_LRT_MQT
qualTabNameGenLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, true, forLrt, null, null, null, null, null);

M96_DdlSummary.addTabToDdlSummary(qualTabNameGen, ddlType, M22_Class.g_classes.descriptors[classIndex].notAcmRelated);
M78_DbMeta.registerQualTable(qualTabNameGenLdm, qualTabNameGen, M22_Class.g_classes.descriptors[classIndex].classIndex, M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, ddlType, M22_Class.g_classes.descriptors[classIndex].notAcmRelated, true, forLrt, false, null);

if (M03_Config.generateDdlCreateTable) {
M22_Class_Utilities.printChapterHeader("\"GEN\"-Table for ACM-Class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\"" + (!(forLrt) ? "" : " (LRT)"), fileNo);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameGen);
M00_FileWriter.printToFile(fileNo, "(");

M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), null, null, qualTabName, null, poolCommonItemsLocal, null);

M00_FileWriter.printToFile(fileNo, ")");

// ### IF IVK ###
M22_Class.genTabDeclTrailer(fileNo, ddlType, false, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, false, forLrt, false, supportPartitionByClassId, null, tabPartitionType);
// ### ELSE IVK ###
//         genTabDeclTrailer fileNo, ddlType, eactClass, .classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, forLrt, M01_Common.DdlOutputMode.edomNone, null, null, qualTabName, null, poolCommonItemsLocal, null);
}

if (forLrt &  M03_Config.lrtTablesVolatile) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE " + qualTabNameGen + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
}

if (!(poolSuppressRefIntegrity)) {
M24_Attribute.genPKForGenClass(qualTabNameGen, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, null, null);
}

// ### IF IVK ###
if (!(((ddlType == M01_Common.DdlTypeId.edtPdm) &  (M22_Class.g_classes.descriptors[classIndex].noIndexesInPool >= 0) & (M22_Class.g_classes.descriptors[classIndex].noIndexesInPool == thisPoolId)))) {
M76_Index.genIndexesForEntity(qualTabNameGen, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, true, forLrt, null, null, poolSuppressUniqueConstraints, tabPartitionType);
}

if (!(forLrt & ! poolSuppressRefIntegrity)) {
M24_Attribute.genEnumFKsForClassRecursive(qualTabNameGen, qualTabNameGenLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, true, false, 1, tabPartitionType);
M24_Attribute.genFKsForGenParent(qualTabNameGen, qualTabNameGenLdm, qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, tabPartitionType);
}
if (genSupportForLrt & ! poolSuppressRefIntegrity) {
M24_Attribute.genFksForLrtByEntity(qualTabNameGen, qualTabNameGenLdm, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, true, forLrt, null, tabPartitionType);
}
// ### ELSE IVK ###
//       If Not ((ddlType = edtPdm) And (.noIndexesInPool >= 0) And (.noIndexesInPool = thisPoolId)) Then
//         genIndexesForEntity qualTabNameGen, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, True, forLrt, , , poolSuppressUniqueConstraints
//       End If
//
//       If Not forLrt And Not pool.suppressRefIntegrity Then
//         genEnumFKsForClassRecursive qualTabNameGen, qualTabNameGenLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, 1
//         genFKsForGenParent qualTabNameGen, qualTabNameGenLdm, qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType
//       End If
//       If genSupportForLrt And Not pool.suppressRefIntegrity Then
//         genFksForLrtByEntity qualTabNameGen, qualTabNameGenLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, forLrt
//       End If
// ### ENDIF IVK ###

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInGenInclSubClasses & ! forLrt & (!(M72_DataPool.poolSupportLrt | ! M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt)) & poolSupportUpdates) {
// create INSERT-trigger to maintain derived attributes (for LRT-MQT-supported classes this is done in MQT-triggers)
genVirtualAttrTrigger(fileNoLrtSup, classIndex, qualTabNameGen, thisOrgIndex, thisPoolIndex, ddlType, true, null);
}
if ((M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInGenInclSubClasses |  M22_Class.g_classes.descriptors[classIndex].hasRelBasedVirtualAttrInGenInclSubClasses) &  poolSupportUpdates) {
M11_VirtualAttrs.genVirtAttrSupportForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, true, forLrt, null);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M22_Class.g_classes.descriptors[classIndex].noAlias) {
M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs, M22_Class.g_classes.descriptors[classIndex].isCommonToPools, !(M22_Class.g_classes.descriptors[classIndex].notAcmRelated), qualTabNameGenLdm, qualTabNameGen, M22_Class.g_classes.descriptors[classIndex].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, true, forLrt, false, false, false, (M22_Class.g_classes.descriptors[classIndex].classId >= 0 & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated ? "ACM-Class" : "LDM-Table") + " \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" (GEN)", null, M22_Class.g_classes.descriptors[classIndex].isUserTransactional, M22_Class.g_classes.descriptors[classIndex].isPsTagged, null, null, M22_Class.g_classes.descriptors[classIndex].logLastChangeInView, null, null);
}

if (transformation.containsNlAttribute |  (forLrt &  M22_Class.g_classes.descriptors[classIndex].implicitelyGenChangeComment)) {
M24_Attribute.genNlsTabsForClassRecursive(classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, true, forLrt, poolCommonItemsLocal);
}
// ### ELSE IVK ###
//       If ddlType = edtPdm And Not .noAlias Then
//         genAliasDdl.sectionName, .sectionShortName, .className, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
//           qualTabNameGenLdm, qualTabNameGen, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, True, forLrt, _
//           IIf(.classId >= 0 And Not .notAcmRelated, "ACM-Class", "LDM-Table") & " """ & .sectionName & "." & .className & """ (GEN)", , _
//           .isUserTransactional, .logLastChangeInView
//       End If
//
//       If transformation.containsNlAttribute Or forLrt Then
//         genNlsTabsForClassRecursive classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, True, forLrt, poolcommonItemsLocal
//       End If
// ### ENDIF IVK ###

if (M03_Config.generateCommentOnTables & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M22_Class.genDbObjComment("TABLE", qualTabNameGen, "ACM-Class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" (GEN)" + (forLrt ? " (LRT)" : ""), fileNo, thisOrgIndex, thisPoolIndex, null);
}

if (M03_Config.generateCommentOnColumns & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "COMMENT ON " + qualTabNameGen + " (");

M22_Class.genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt) |  M01_Common.DdlOutputMode.edomComment, null, qualTabName, null, poolCommonItemsLocal);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M22_Class.g_classes.descriptors[classIndex].noAlias) {
// ### IF IVK ###
M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs, M22_Class.g_classes.descriptors[classIndex].isCommonToPools, !(M22_Class.g_classes.descriptors[classIndex].notAcmRelated), qualTabNameLdm, qualTabName, M22_Class.g_classes.descriptors[classIndex].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, forLrt, false, false, false, (M22_Class.g_classes.descriptors[classIndex].classId >= 0 & ! M22_Class.g_classes.descriptors[classIndex].notAcmRelated ? "ACM-Class" : "LDM-Table") + " \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\"", null, M22_Class.g_classes.descriptors[classIndex].isUserTransactional, M22_Class.g_classes.descriptors[classIndex].isPsTagged, null, null, M22_Class.g_classes.descriptors[classIndex].logLastChangeInView, null, null);
// ### ELSE IVK ###
//       genAliasDdl .sectionName, .sectionShortName, .className, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
//         qualTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, _
//         IIf(.classId >= 0 And Not .notAcmRelated, "ACM-Class", "LDM-Table") & " """ & .sectionName & "." & .className & """", , _
//         .isUserTransactional, .logLastChangeInView
// ### ENDIF IVK ###
}
}

if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & !poolCommonItemsLocal) {
M11_LRT.genLrtSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoCl, fileNoFk, fileNoLrtSup, ddlType, null);
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
// ### ELSE IVK ###
//     If .isGenForming Then
// ### ENDIF IVK ###
M11_LRT.genLrtSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoCl, fileNoFk, fileNoLrtSup, ddlType, true);
}
}

// ### IF IVK ###
if (genSupportForLrt) {
if (M03_Config.generatePsCopySupport) {
M82_PSCopy.genPsCopySupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M82_PSCopy.genPsCopySupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType, true);
}
}

if (M03_Config.generateExpCopySupport) {
M85_DataFix.genExpCopySupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M85_DataFix.genExpCopySupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType, true);
}
}

if (orgSetProductiveTargetPoolIndex > 0) {
M86_SetProductive.genSetProdSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M86_SetProductive.genSetProdSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType, true);
}
}

if (!(orgIsPrimary & ! M22_Class.g_classes.descriptors[classIndex].noFto)) {
M87_FactoryTakeOver.genFtoSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M87_FactoryTakeOver.genFtoSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType, true);
}
}
}

if (M03_Config.generatePsTaggingView &  M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
M13_PSTag.genPsTagSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M13_PSTag.genPsTagSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType, true);
}
}

if (M03_Config.generateLogChangeView & ! M22_Class.g_classes.descriptors[classIndex].isUserTransactional & !M22_Class.g_classes.descriptors[classIndex].isPsTagged & M22_Class.g_classes.descriptors[classIndex].logLastChange & M22_Class.g_classes.descriptors[classIndex].logLastChangeInView) {
M18_LogChange.genLogChangeSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M18_LogChange.genLogChangeSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, true);
}
}

// ### ENDIF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].logLastChange &  M22_Class.g_classes.descriptors[classIndex].logLastChangeAutoMaint) {
M18_LogChange.genLogChangeAutoMaintSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, null, forLrt);
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
// ### ELSE IVK ###
//     If .isGenForming Then
// ### ENDIF IVK ###
M18_LogChange.genLogChangeAutoMaintSupportDdlForClass(M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, true, forLrt);
}
}
// ### IF IVK ###

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.supportArchivePool) {
if (thisPoolIndex == M01_Globals_IVK.g_productiveDataPoolIndex) {
M16_Archive.genArchiveSupportDdlForClass(classIndex, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNoArc, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M16_Archive.genArchiveSupportDdlForClass(classIndex, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNoArc, ddlType, true);
}
}
}
// ### ENDIF IVK ###

// class may be a copy taken from g_glasses! make sure we update the original source!
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].classIndex].isLdmCsvExported = true;
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].classIndex].isCtoAliasCreated = true;
M22_Class.g_classes.descriptors[classIndex].isLdmCsvExported = true;// safe is safe ;-)
M22_Class.g_classes.descriptors[classIndex].isCtoAliasCreated = true;// safe is safe ;-)
if (genSupportForLrt) {
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].classIndex].isLdmLrtCsvExported = true;
M22_Class.g_classes.descriptors[classIndex].isLdmLrtCsvExported = true;// safe is safe ;-)
}
// ### IF IVK ###

GenXmlExport:
if (M03_Config.generateXmlExportSupport &  M22_Class.g_classes.descriptors[classIndex].supportXmlExport & (ddlType == M01_Common.DdlTypeId.edtLdm |  thisPoolId == -1 | poolSupportXmlExport)) {
M14_XMLExport.genXmlExportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType);
}

if (isGenericAspect) {
genGetCodePropertyGroupByPriceAssignmentFunction(fileNoGaSup, thisOrgIndex, thisPoolIndex, ddlType);
}
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
M00_FileWriter.closeFile(fileNoLrt);
M00_FileWriter.closeFile(fileNoLrtView);
M00_FileWriter.closeFile(fileNoCl);
M00_FileWriter.closeFile(fileNoLrtSup);
M00_FileWriter.closeFile(fileNoLc);
M00_FileWriter.closeFile(fileNoFk);
// ### IF IVK ###
M00_FileWriter.closeFile(fileNoSetProd);
M00_FileWriter.closeFile(fileNoSetProdCl);
M00_FileWriter.closeFile(fileNoFto);
M00_FileWriter.closeFile(fileNoXmlV);
M00_FileWriter.closeFile(fileNoPs);
M00_FileWriter.closeFile(fileNoGaSup);
M00_FileWriter.closeFile(fileNoPsCopy);
M00_FileWriter.closeFile(fileNoPsCopy2);
M00_FileWriter.closeFile(fileNoExpCopy);
M00_FileWriter.closeFile(fileNoArc);
M00_FileWriter.closeFile(fileNoXmlF);
// ### ENDIF IVK ###
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genTabDeclTrailer(int fileNo, Integer ddlType, boolean isDivTagged, Integer acmEntityType, int acmEntityIndex,  int thisOrgIndex,  int thisPoolIndex, Boolean forNlW, Boolean forLrtW, Boolean forMqtW, Boolean supportPartitionByClassIdW, String fkAttrToDivW, Integer tabPartitionTypeW) {
boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean supportPartitionByClassId; 
if (supportPartitionByClassIdW == null) {
supportPartitionByClassId = false;
} else {
supportPartitionByClassId = supportPartitionByClassIdW;
}

String fkAttrToDiv; 
if (fkAttrToDivW == null) {
fkAttrToDiv = null;
} else {
fkAttrToDiv = fkAttrToDivW;
}

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

boolean partitionByClassId;
partitionByClassId = supportPartitionByClassId &  M03_Config.supportRangePartitioningByClassIdFirstPsOid;

// ### ELSE IVK ###
//Sub genTabDeclTrailer( _
// fileNo As Integer, _
// ddlType As DdlTypeId, _
// acmEntityType As AcmAttrContainerType, _
// acmEntityIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// Optional forNl As Boolean = False, _
// Optional forLrt As Boolean, _
// Optional forMqt As Boolean _
//)
// ### ENDIF IVK ###
if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

int thisPartitionIndex;
String lbClassIdVirtStr;
String lbClassIdStr;
String ubClassIdStr;
String tabSpaceData;
String tabSpaceLong;
String tabSpaceIndex;
int tabSpaceIndexData;
int tabSpaceIndexLong;
int tabSpaceIndexIndex;
boolean useValueCompression;
// ### IF IVK ###
boolean isPsTagged;
boolean psTagOptional;
boolean noRangePartitioning;
// ### ENDIF IVK ###

boolean M72_DataPool.poolSupportLrt;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
tabSpaceIndex = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndex;
tabSpaceIndexIndex = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexIndex;
tabSpaceLong = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceLong;
tabSpaceIndexLong = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexLong;
if (forNl) {
tabSpaceData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceNl;
tabSpaceIndexData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexNl;
} else {
tabSpaceData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceData;
tabSpaceIndexData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexData;
}
useValueCompression = M22_Class.g_classes.descriptors[acmEntityIndex].useValueCompression;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
noRangePartitioning = M22_Class.g_classes.descriptors[acmEntityIndex].noRangePartitioning;
if (!(noRangePartitioning &  M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional & M72_DataPool.poolSupportLrt & !M22_Class.g_classes.descriptors[acmEntityIndex].rangePartitioningAll)) {
if (M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt) {
if (forLrt) {
noRangePartitioning = !((forMqt |  M03_Config.partitionLrtPrivateWhenMqt));
} else {
noRangePartitioning = !((forMqt |  M03_Config.partitionLrtPublicWhenMqt));
}
} else {
if (forLrt) {
noRangePartitioning = !(M03_Config.partitionLrtPrivateWhenNoMqt);
} else {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenNoMqt);
}
}
}
if (!(noRangePartitioning &  !(M03_Config.noPartitioningInDataPools.compareTo("") == 0) & thisPoolIndex > 0 & !M22_Class.g_classes.descriptors[acmEntityIndex].rangePartitioningAll)) {
noRangePartitioning = M04_Utilities.includedInList(M03_Config.noPartitioningInDataPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id);
}
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
tabSpaceIndexIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexIndex;
tabSpaceIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndex;
tabSpaceLong = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceLong;
tabSpaceIndexLong = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexLong;
if (forNl) {
tabSpaceData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceNl;
tabSpaceIndexData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexNl;
} else {
tabSpaceData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceData;
tabSpaceIndexData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexData;
}
useValueCompression = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useValueCompression;
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
psTagOptional = false;
noRangePartitioning = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noRangePartitioning;
if (!(noRangePartitioning &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional & M72_DataPool.poolSupportLrt)) {
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt) {
if (forLrt) {
noRangePartitioning = !((forMqt |  M03_Config.partitionLrtPrivateWhenMqt));
} else {
noRangePartitioning = !((forMqt |  M03_Config.partitionLrtPublicWhenMqt));
}
} else {
if (forLrt) {
noRangePartitioning = !(M03_Config.partitionLrtPrivateWhenNoMqt);
} else {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenNoMqt);
}
}
}
if (!(noRangePartitioning &  !(M03_Config.noPartitioningInDataPools.compareTo("") == 0) & thisPoolIndex > 0)) {
noRangePartitioning = M04_Utilities.includedInList(M03_Config.noPartitioningInDataPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id);
}
// ### ENDIF IVK ###
} else {
return;
}

if (tabSpaceIndexData > 0) {
M00_FileWriter.printToFile(fileNo, "IN " + M04_Utilities.genTablespaceNameByIndex(tabSpaceIndexData, thisOrgIndex, thisPoolIndex, null));
}
// ### IF IVK ###
// wf If-Bedingung deaktivuert --> Alle Tabellen mit Definition LONG-TS (wird aber nicht implemetiert für Bestand)
// wf WI19388
//  If tabSpaceIndexLong > 0 And _
//    (noRangePartitioning Or _
//      (Not isPsTagged Or Not supportRangePartitioningByPsOid) And _
//      (Not isDivTagged Or Not supportRangePartitioningByDivOid) _
//    ) Then
// ### ELSE IVK ###
// If tabSpaceIndexLong > 0 Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "LONG IN " + M04_Utilities.genTablespaceNameByIndex(tabSpaceIndexLong, thisOrgIndex, thisPoolIndex, null));
//  End If
if (tabSpaceIndexIndex > 0) {
M00_FileWriter.printToFile(fileNo, "INDEX IN " + M04_Utilities.genTablespaceNameByIndex(tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex, null));
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  useValueCompression) {
M00_FileWriter.printToFile(fileNo, "VALUE COMPRESSION");
}
M00_FileWriter.printToFile(fileNo, "COMPRESS YES");

// ### IF IVK ###
if (forNl & ! M03_Config.usePsTagInNlTextTables) {
return;
}

if (noRangePartitioning) {
return;
}

if (isPsTagged &  M03_Config.supportRangePartitioningByPsOid) {
long thisPsOidForPartitioning;
String[] elemsRangePartitionTablesByPsOid;
elemsRangePartitionTablesByPsOid = M03_Config.listRangePartitionTablesByPsOid.split(",");

if (supportPartitionByClassId &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARTITION BY RANGE (" + M01_Globals_IVK.g_anPsOid + " NULLS FIRST, " + M01_Globals.g_anCid.toUpperCase() + " NULLS FIRST) (");
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptPsOidCid;

if (partitionByClassId) {
if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(0, null, null) + " STARTING (MINVALUE,MINVALUE) INCLUSIVE ENDING (0,MAXVALUE) INCLUSIVE,");
}
int i;
for (int i = M00_Helper.lBound(elemsRangePartitionTablesByPsOid); i <= M00_Helper.uBound(elemsRangePartitionTablesByPsOid); i++) {
thisPartitionIndex = 1;
while (M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(1, thisPartitionIndex) != "" |  M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(2, thisPartitionIndex) != "") {
lbClassIdVirtStr = (M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(1, thisPartitionIndex) == "" ? M22_Class_Utilities.getClassId(0, 0) : M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(1, thisPartitionIndex));
lbClassIdStr = (M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(1, thisPartitionIndex) == "" ? "MINVALUE" : "'" + M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(1, thisPartitionIndex) + "'");
ubClassIdStr = (M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(2, thisPartitionIndex) == "" ? "MAXVALUE" : "'" + M22_Class.g_classes.descriptors[acmEntityIndex].subClassPartitionBoundaries(2, thisPartitionIndex) + "'");

if (thisPartitionIndex > 1 |  i > M00_Helper.lBound(elemsRangePartitionTablesByPsOid)) {
M00_FileWriter.printToFile(fileNo, ",");
}
thisPsOidForPartitioning = M04_Utilities.getLong(elemsRangePartitionTablesByPsOid[i], -1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(thisPsOidForPartitioning, null, lbClassIdVirtStr) + " " + "STARTING (" + String.valueOf(thisPsOidForPartitioning) + ", " + lbClassIdStr + ") INCLUSIVE " + "ENDING (" + String.valueOf(thisPsOidForPartitioning) + ", " + ubClassIdStr + ") INCLUSIVE");
thisPartitionIndex = thisPartitionIndex + 1;
}
}
M00_FileWriter.printToFile(fileNo, "");
} else {
for (int i = M00_Helper.lBound(elemsRangePartitionTablesByPsOid); i <= M00_Helper.uBound(elemsRangePartitionTablesByPsOid); i++) {
thisPsOidForPartitioning = M04_Utilities.getLong(elemsRangePartitionTablesByPsOid[i], -1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(thisPsOidForPartitioning, null, null) + " " + "STARTING (" + String.valueOf(thisPsOidForPartitioning) + ", MINVALUE) INCLUSIVE " + "ENDING (" + String.valueOf(thisPsOidForPartitioning) + ", MAXVALUE) INCLUSIVE" + (i < M00_Helper.uBound(elemsRangePartitionTablesByPsOid) ? "," : ""));
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARTITION BY RANGE (" + M01_Globals_IVK.g_anPsOid + " NULLS FIRST) (");
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptPsOid;

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(0, null, null) + " STARTING MINVALUE INCLUSIVE ENDING 0 INCLUSIVE,");
}
for (int i = M00_Helper.lBound(elemsRangePartitionTablesByPsOid); i <= M00_Helper.uBound(elemsRangePartitionTablesByPsOid); i++) {
thisPsOidForPartitioning = M04_Utilities.getLong(elemsRangePartitionTablesByPsOid[i], -1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(thisPsOidForPartitioning, null, null) + " STARTING " + String.valueOf(thisPsOidForPartitioning) + " INCLUSIVE ENDING " + String.valueOf(thisPsOidForPartitioning) + " INCLUSIVE" + (i < M00_Helper.uBound(elemsRangePartitionTablesByPsOid) ? "," : ""));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
} else if (isDivTagged &  M03_Config.supportRangePartitioningByDivOid) {
long thisDivOidForPartitioning;
String[] elemsRangePartitionTablesByDivOid;
elemsRangePartitionTablesByDivOid = M03_Config.listRangePartitionTablesByDivOid.split(",");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PARTITION BY RANGE (" + fkAttrToDiv + " NULLS FIRST) (");
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptDivOid;

for (int i = M00_Helper.lBound(elemsRangePartitionTablesByDivOid); i <= M00_Helper.uBound(elemsRangePartitionTablesByDivOid); i++) {
thisDivOidForPartitioning = M04_Utilities.getLong(elemsRangePartitionTablesByDivOid[i], -1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PARTITION " + M04_Utilities.genPartitionName(thisDivOidForPartitioning, false, null) + " STARTING " + String.valueOf(thisDivOidForPartitioning) + " INCLUSIVE ENDING " + String.valueOf(thisDivOidForPartitioning) + " INCLUSIVE" + (i < M00_Helper.uBound(elemsRangePartitionTablesByDivOid) ? "," : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
}
// ### ENDIF IVK ###
}
// ### IF IVK ###


private static void genVirtualAttrTrigger(int fileNo, int classIndex, String qualTabName,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String entityTypeDescr;
entityTypeDescr = "ACM-Class" + (forNl ? " (NL-Text)" : "");

// we currently only support insert trigger
boolean hasVirtualAttrs;
hasVirtualAttrs = !(forNl &  ((forGen &  M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInGenInclSubClasses) |  (!(forGen &  M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses))));

if (!(hasVirtualAttrs)) {
return;
}

String qualTriggerName;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
M24_Attribute_Utilities.AttributeListTransformation transformation;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, !(forNl &  forGen), null, null, null, null, (forNl ? "NLTXT" : "") + "_INS", null, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger for maintaining virtual columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "update virtual columns in table", null, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");

boolean firstAttr;
firstAttr = true;
int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if (!(firstAttr)) {
M00_FileWriter.printToFile(fileNo, ",");
}
firstAttr = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + tabColumns.descriptors[k].columnName + " = " + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
// ### ENDIF IVK ###


public static void genClassesDdl(Integer ddlType) {
int thisClassIndex;
int thisOrgIndex;
int thisPoolIndex;

M22_Class.resetClassesCsvExported();

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
genClassDdl(thisClassIndex, null, null, M01_Common.DdlTypeId.edtLdm);
}

M22_Class.resetClassesCsvExported();
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
thisOrgIndex = -1;
thisPoolIndex = -1;
if (M22_Class.g_classes.descriptors[thisClassIndex].isCommonToOrgs) {
genClassDdl(thisClassIndex, null, null, M01_Common.DdlTypeId.edtPdm);

// if there is some data pool which locally implements this class, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}

} else {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M22_Class.g_classes.descriptors[thisClassIndex].isCommonToPools) {
genClassDdl(thisClassIndex, thisOrgIndex, null, M01_Common.DdlTypeId.edtPdm);
// if there is some data pool which locally implements this class, take care of that
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}

} else {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}

M22_Class.resetClassesCsvExported();
}
}


// ### IF IVK ###
private static void genClassHibernateSupport(int classIndex, Integer ddlType) {
String fileNameHCfg;
int fileNoHCfg;

if (!(M03_Config.genSupportForHibernate)) {
return;
}

fileNameHCfg = M04_Utilities.genHCfgFileName(M01_Globals.g_targetDir, classIndex, ddlType);
M04_Utilities.assertDir(fileNameHCfg);
fileNoHCfg = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoHCfg, fileNameHCfg, true);

//On Error GoTo ErrorExit 

// (optionally) loop twice over the table structure: first run: 'Main' table + GEN-table; second run: corresponding LRT-tables
int loopCount;
int iteration;
boolean forLrt;
loopCount = (M03_Config.generateLrt ? 2 : 1);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
for (iteration = 1; iteration <= 1; iteration += (1)) {
forLrt = (iteration == 2);
}

M15_Hibernate.genHCfgForClass(classIndex, fileNoHCfg, ddlType, null);
if (M22_Class.g_classes.descriptors[classIndex].isGenForming & ! M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M15_Hibernate.genHCfgForClass(classIndex, fileNoHCfg, ddlType, true);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoHCfg);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genClassesHibernateSupport(Integer ddlType) {
if (!(M03_Config.genSupportForHibernate |  ddlType != M01_Common.DdlTypeId.edtLdm)) {
return;
}

int thisClassIndex;

for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
genClassHibernateSupport(thisClassIndex, ddlType);
}
}


public static void dropClassesHibernateSupport(Integer ddlType) {
if (!(M03_Config.genSupportForHibernate)) {
return;
}

int thisClassIndex;

for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genHCfgFileName(M01_Globals.g_targetDir, thisClassIndex, ddlType), null);
}
}


// ### ENDIF IVK ###
public static void dropClassesCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmSection, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnLdmTable, M01_Globals.g_targetDir, M22_Class.ldmCsvTableProcessingStep, onlyIfEmpty, "LDM");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnFkDependency, M01_Globals.g_targetDir, M22_Class.ldmCsvFkProcessingStep, onlyIfEmpty, "LDM");

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmTable, M01_Globals.g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM");
}


public static void genClassAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmEntity, acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

String typeKey;
typeKey = M01_Globals.gc_acmEntityTypeKeyClass;

int i;
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (!(M22_Class.g_classes.descriptors[i].notAcmRelated)) {
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[i].shortName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + typeKey + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[i].classIdStr + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[i].i18nId + "\",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isCommonToOrgs ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isCommonToPools ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].supportXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].useXmlExport ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].aggHeadClassIdStr.compareTo("") == 0) ? "\"" + M22_Class.g_classes.descriptors[i].aggHeadClassIdStr + "\"" : "") + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].noFto ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isUserTransactional ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isLrtMeta ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isUserTransactional &  M22_Class.g_classes.descriptors[i].useMqtToImplementLrt ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].useLrtCommitPreprocess ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].lrtActivationStatusMode.compareTo("") == 0) ? "\"" + M22_Class.g_classes.descriptors[i].lrtActivationStatusMode + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].lrtClassification.compareTo("") == 0) ? "\"" + M22_Class.g_classes.descriptors[i].lrtClassification + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isSubjectToArchiving ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isGenForming & ! M22_Class.g_classes.descriptors[i].hasNoIdentity ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ELSE IVK ###
//       Print #fileNo, IIf(.isGenForming, gc_dbTrue, gc_dbFalse); ",";
// ### ENDIF IVK ###
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isPsTagged ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isPsForming ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].logLastChange ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isAbstract ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isSubjectToPreisDurchschuss ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].isUserTransactional &  M22_Class.g_classes.descriptors[i].hasOrganizationSpecificReference ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].ignoreForChangelog ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].condenseData ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, M22_Class.g_classes.descriptors[i].entityFilterEnumCriteria + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].supportAhStatusPropagation ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (M22_Class.g_classes.descriptors[i].rangePartitioningAll ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].orMappingSuperClassIndex].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].orMappingSuperClassIndex].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + typeKey + "\",");
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].superClassSection.compareTo("") == 0) ? "\"" + M22_Class.g_classes.descriptors[i].superClassSection.toUpperCase() + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].superClass.compareTo("") == 0) ? "\"" + M22_Class.g_classes.descriptors[i].superClass.toUpperCase() + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M22_Class.g_classes.descriptors[i].superClass.compareTo("") == 0) ? "\"" + typeKey + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, ",,0,");
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
// ### IF IVK ###


public static void dropClassesXmlExport() {
if (!(M03_Config.generateXmlExportSupport)) {
return;
}

int thisClassIndex;

if (M03_Config.generateLdm) {
for (thisClassIndex = 1; thisClassIndex <= 1; thisClassIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genXmlExportFileName(M01_Globals.g_targetDir, thisClassIndex, M01_Common.DdlTypeId.edtLdm, null, null, null), null);
M04_Utilities.killFile(M04_Utilities.genXmlExportFileName(M01_Globals.g_targetDir, thisClassIndex, M01_Common.DdlTypeId.edtLdm, true, null, null), null);
}
}
}
// ### ENDIF IVK ###


public static void evalClasses() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
// determine TableSpaces
M22_Class.g_classes.descriptors[i].tabSpaceIndexData = (!(M22_Class.g_classes.descriptors[i].tabSpaceData.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M22_Class.g_classes.descriptors[i].tabSpaceData) : -1);
M22_Class.g_classes.descriptors[i].tabSpaceIndexIndex = (!(M22_Class.g_classes.descriptors[i].tabSpaceIndex.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M22_Class.g_classes.descriptors[i].tabSpaceIndex) : -1);
M22_Class.g_classes.descriptors[i].tabSpaceIndexLong = (!(M22_Class.g_classes.descriptors[i].tabSpaceLong.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M22_Class.g_classes.descriptors[i].tabSpaceLong) : -1);
M22_Class.g_classes.descriptors[i].tabSpaceIndexNl = (!(M22_Class.g_classes.descriptors[i].tabSpaceNl.compareTo("") == 0) ? M73_TableSpace.getTableSpaceIndexByName(M22_Class.g_classes.descriptors[i].tabSpaceNl) : -1);

if (M22_Class.g_classes.descriptors[i].tabSpaceIndexData > 0) {
if (M73_TableSpace.g_tableSpaces.descriptors[M22_Class.g_classes.descriptors[i].tabSpaceIndexData].category == M73_TableSpace_Utilities.TabSpaceCategory.tscSms) {
if (M22_Class.g_classes.descriptors[i].tabSpaceIndexIndex > 0 &  M22_Class.g_classes.descriptors[i].tabSpaceIndexIndex != M22_Class.g_classes.descriptors[i].tabSpaceIndexData) {
M22_Class.g_classes.descriptors[i].tabSpaceIndexIndex = M22_Class.g_classes.descriptors[i].tabSpaceIndexData;
M04_Utilities.logMsg("index table space \"" + M22_Class.g_classes.descriptors[i].tabSpaceIndex + "\" for class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
if (M22_Class.g_classes.descriptors[i].tabSpaceIndexLong > 0 &  M22_Class.g_classes.descriptors[i].tabSpaceIndexLong != M22_Class.g_classes.descriptors[i].tabSpaceIndexData) {
M22_Class.g_classes.descriptors[i].tabSpaceIndexLong = M22_Class.g_classes.descriptors[i].tabSpaceIndexData;
M04_Utilities.logMsg("long table space \"" + M22_Class.g_classes.descriptors[i].tabSpaceLong + "\" for class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\"" + " must be identical to data table space since data table space is \"SMS\" - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
}
}

// confirm that class name is unique
for (j = 1; j <= 1; j += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M22_Class.g_classes.descriptors[j].sectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == M22_Class.g_classes.descriptors[j].className.toUpperCase()) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\" is not unque", M01_Common.LogLevel.ellFatal, null, null, null);
}
}

// initialize variables
M22_Class.g_classes.descriptors[i].useValueCompression = M22_Class.g_classes.descriptors[i].useValueCompression &  M03_Config.dbCompressValues;
M22_Class.g_classes.descriptors[i].numRelBasedFkAttrs = 0;
M22_Class.g_classes.descriptors[i].isAggHead = false;
M22_Class.g_classes.descriptors[i].hasLabel = false;
M22_Class.g_classes.descriptors[i].hasLabelInGen = false;
// ### IF IVK ###
M22_Class.g_classes.descriptors[i].hasAttrHasConflict = false;
M22_Class.g_classes.descriptors[i].hasIsNationalInclSubClasses = M22_Class.g_classes.descriptors[i].isNationalizable;
// determine whether class supports XML-export
if (M22_Class.g_classes.descriptors[i].noXmlExport |  M22_Class.g_classes.descriptors[i].isAbstract) {
M22_Class.g_classes.descriptors[i].supportXmlExport = false;
} else if (M22_Class.g_classes.descriptors[i].isCommonToPools |  M22_Class.g_classes.descriptors[i].isCommonToOrgs) {
M22_Class.g_classes.descriptors[i].supportXmlExport = true;
} else {
if (M22_Class.g_classes.descriptors[i].specificToPool >= 0) {
int dataPoolIndex;
dataPoolIndex = M72_DataPool.getDataPoolIndexById(M22_Class.g_classes.descriptors[i].specificToPool);
if (dataPoolIndex > 0) {
if (M72_DataPool.g_pools.descriptors[dataPoolIndex].supportXmlExport) {
M22_Class.g_classes.descriptors[i].supportXmlExport = true;
}
}
} else {
M22_Class.g_classes.descriptors[i].supportXmlExport = true;
}
}
// ### ENDIF IVK ###

// determine reference to section
M22_Class.g_classes.descriptors[i].sectionIndex = M20_Section.getSectionIndexByName(M22_Class.g_classes.descriptors[i].sectionName, null);
M22_Class.g_classes.descriptors[i].sectionShortName = "";
if (M22_Class.g_classes.descriptors[i].sectionIndex > 0) {
M22_Class.g_classes.descriptors[i].sectionShortName = M20_Section.g_sections.descriptors[M22_Class.g_classes.descriptors[i].sectionIndex].shortName;
}

// determine 'hasSubClasses'
for (j = 1; j <= 1; j += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.compareTo(M22_Class.g_classes.descriptors[j].superClassSection) == 0 &  M22_Class.g_classes.descriptors[i].className.compareTo(M22_Class.g_classes.descriptors[j].superClass) == 0) {
M22_Class.g_classes.descriptors[i].hasSubClass = true;
j = M22_Class.g_classes.numDescriptors;// just to exit this loop
}
}
NextI:
}

for (i = 1; i <= 1; i += (1)) {
// ### IF IVK ###
if (!(M22_Class.g_classes.descriptors[i].mapOidToClAttribute.compareTo("") == 0)) {
M23_Relationship.addAttrMapping(M22_Class.g_classes.descriptors[i].clMapAttrs, M04_Utilities.genSurrogateKeyName(M01_Common.DdlTypeId.edtPdm, null, null, null, null, null), M22_Class.g_classes.descriptors[i].mapOidToClAttribute, null, null);
}

// determine whether this is a PriceAssignment
M22_Class.g_classes.descriptors[i].isPriceAssignment = M00_Helper.inStr(1, M22_Class.g_classes.descriptors[i].className.toUpperCase(), "PRICEASSIGNMENT") != 0;
M22_Class.g_classes.descriptors[i].hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[i].isPriceAssignment;
M22_Class.g_classes.descriptors[i].isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[i].isPriceAssignment;
// ### ENDIF IVK ###

// determine class ID as string
M22_Class.g_classes.descriptors[i].classIdStr = M22_Class_Utilities.getClassIdByClassIndex(i);
// determine class index
M22_Class.g_classes.descriptors[i].classIndex = M22_Class.getClassIndexByName(M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, null);
// determine class index of aggregate head
M22_Class.g_classes.descriptors[i].aggHeadClassIndex = -1;
M22_Class.g_classes.descriptors[i].aggHeadClassIndexExact = -1;
M22_Class.g_classes.descriptors[i].aggHeadClassIdStr = "";
if (!(M22_Class.g_classes.descriptors[i].notAcmRelated)) {
if (!(M22_Class.g_classes.descriptors[i].aggHeadSection.compareTo("") == 0) &  !(M22_Class.g_classes.descriptors[i].aggHeadName.compareTo("") == 0)) {
M22_Class.g_classes.descriptors[i].aggHeadClassIndex = M22_Class.getClassIndexByName(M22_Class.g_classes.descriptors[i].aggHeadSection, M22_Class.g_classes.descriptors[i].aggHeadName, null);
M22_Class.g_classes.descriptors[i].aggHeadClassIndexExact = M22_Class.g_classes.descriptors[i].aggHeadClassIndex;
if (M22_Class.g_classes.descriptors[i].aggHeadClassIndex <= 0) {
M04_Utilities.logMsg("unable to identify aggregate head class '" + M22_Class.g_classes.descriptors[i].aggHeadSection + "." + M22_Class.g_classes.descriptors[i].aggHeadName + "'", M01_Common.LogLevel.ellError, null, null, null);
} else {
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].isAggHead = (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].superClassSection.compareTo("") == 0);
M22_Class.g_classes.descriptors[i].aggHeadClassIdStr = M22_Class_Utilities.getClassIdByClassIndex(M22_Class.g_classes.descriptors[i].aggHeadClassIndex);
}
}
}
// determine superclass index
//  !! we need to do this separately because 'getOrMappingSuperClassIndex' relies on all super class indexes being set!
M22_Class.g_classes.descriptors[i].superClassIndex = M22_Class.getClassIndexByName(M22_Class.g_classes.descriptors[i].superClassSection, M22_Class.g_classes.descriptors[i].superClass, null);
// ### IF IVK ###

// verify that enforceLrtChangeComment is only set for userTransActional classes
if (M22_Class.g_classes.descriptors[i].enforceLrtChangeComment & ! M22_Class.g_classes.descriptors[i].isUserTransactional) {
M04_Utilities.logMsg("class '" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "' enforces LRT-ChangeComment but is not user-transactional - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].enforceLrtChangeComment = false;
}
// ### ENDIF IVK ###
}

int k;
// ### IF IVK ###
// propagate 'isSubjectToPreisDurchschuss' to parent classes
for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[i].isSubjectToPreisDurchschuss) {
k = M22_Class.g_classes.descriptors[i].superClassIndex;
while (k > 0) {
M22_Class.g_classes.descriptors[k].isSubjectToPreisDurchschuss = true;
k = M22_Class.g_classes.descriptors[k].superClassIndex;
}
}
}

// ### ENDIF IVK ###
for (i = 1; i <= 1; i += (1)) {
// determine index of class 'owning' the table implementing this class
M22_Class.g_classes.descriptors[i].orMappingSuperClassIndex = M22_Class.getOrMappingSuperClassIndexByClassIndex(i);
//
// determine all 'direct' subclasses
M22_Class.g_classes.descriptors[i].subclassIndexes = M22_Class.getDirectSubclassIndexes(i);
// is this class implemented with an 'own table'?

M22_Class.g_classes.descriptors[i].hasOwnTable = !(M22_Class.g_classes.descriptors[i].hasSubClass &  (M00_Helper.uBound(M22_Class.g_classes.descriptors[i].subclassIndexes) == 0) & M22_Class.g_classes.descriptors[i].superClass.compareTo("") == 0);
// determine attribute references

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[i].supportExtendedPsCopy & ! M22_Class.g_classes.descriptors[i].isPsTagged) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is tagged to 'support PSCOPY' but is not PS-tagged - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].supportExtendedPsCopy = false;
}

if (M22_Class.g_classes.descriptors[i].supportExtendedPsCopy &  (M22_Class.g_classes.descriptors[i].isCommonToPools |  M22_Class.g_classes.descriptors[i].isCommonToOrgs)) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is tagged to 'support PSCOPY' is but common " + (M22_Class.g_classes.descriptors[i].isCommonToOrgs ? "organizations (cto)" : "pools (ctp)"), M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].supportExtendedPsCopy = false;
}
// ### ENDIF IVK ###

if (M22_Class.g_classes.descriptors[i].isUserTransactional &  (M22_Class.g_classes.descriptors[i].isCommonToPools |  M22_Class.g_classes.descriptors[i].isCommonToOrgs)) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "has stereotype <lrt> but is common to " + (M22_Class.g_classes.descriptors[i].isCommonToOrgs ? "organizations (cto)" : "pools (ctp)") + " - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].isUserTransactional = false;
}

if (M22_Class.g_classes.descriptors[i].isUserTransactional & ! M22_Class.g_classes.descriptors[i].logLastChange) {
M04_Utilities.logMsg("potential inconsistency with class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "class has stereotype <lrt> but does not have stereotype <logChange>", M01_Common.LogLevel.ellWarning, null, null, null);
}

if (M22_Class.g_classes.descriptors[i].isUserTransactional &  M22_Class.g_classes.descriptors[i].logLastChange & !M22_Class.g_classes.descriptors[i].logLastChangeInView) {
M04_Utilities.logMsg("inconsistency with class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "class has stereotypes <logChange> and <lrt> but does not support 'logChangeInView' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].logLastChangeInView = true;
}
// ### IF IVK ###

if (M22_Class.g_classes.descriptors[i].isPsTagged &  M22_Class.g_classes.descriptors[i].logLastChange & !M22_Class.g_classes.descriptors[i].logLastChangeInView) {
M04_Utilities.logMsg("inconsistency with class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "class has stereotypes <logChange> and <ps> but does not support 'logChangeInView' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].logLastChangeInView = true;
}

if (M22_Class.g_classes.descriptors[i].isPsForming & ! M22_Class.g_classes.descriptors[i].isUserTransactional) {
M04_Utilities.logMsg("potential inconsistency with class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "class is 'PS-forming' but does not have stereotype <lrt>", M01_Common.LogLevel.ellInfo, null, null, null);
}

if (M22_Class.g_classes.descriptors[i].isSubjectToArchiving & ! M22_Class.g_classes.descriptors[i].logLastChange) {
M04_Utilities.logMsg("potential inconsistency with class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "class is marked as \"subject to archiving\" but does not have stereotype <logChange>", M01_Common.LogLevel.ellWarning, null, null, null);
}

if (M22_Class.g_classes.descriptors[i].specificToOrgId >= 0 & ! M22_Class.g_classes.descriptors[i].noFto) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is specific to MPC " + M22_Class.g_classes.descriptors[i].specificToOrgId + " but does not have stereotype <nt2m> (no transfer to MPC) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].noFto = true;
} else if (M22_Class.g_classes.descriptors[i].specificToPool >= 0 & ! M22_Class.g_classes.descriptors[i].noTransferToProduction) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is specific to pool " + M22_Class.g_classes.descriptors[i].specificToPool + " but does not have stereotype <nt2p> (no transfer to production) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].noTransferToProduction = true;
}

if (M22_Class.g_classes.descriptors[i].isCommonToOrgs & ! M22_Class.g_classes.descriptors[i].noFto) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is common to organizations (cto) but does not have stereotype <nt2m> (no transfer to MPC) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].noFto = true;
} else if (M22_Class.g_classes.descriptors[i].isCommonToPools & ! M22_Class.g_classes.descriptors[i].noTransferToProduction) {
M04_Utilities.logMsg("class \"" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "\": " + "is common to pools (ctp) but does not have stereotype <nt2p> (no transfer to production) - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M22_Class.g_classes.descriptors[i].noTransferToProduction = true;
}
// ### ENDIF IVK ###

// determine references to attributes
M22_Class.g_classes.descriptors[i].attrRefs.numDescriptors = 0;
M22_Class.g_classes.descriptors[i].attrRefsInclSubClasses.numDescriptors = 0;
M22_Class.g_classes.descriptors[i].attrRefsInclSubClassesWithRepeat.numDescriptors = 0;
M22_Class.g_classes.descriptors[i].numAttrsInGen = 0;
M22_Class.g_classes.descriptors[i].numAttrsInNonGen = 0;
M22_Class.g_classes.descriptors[i].numNlAttrsInGen = 0;
M22_Class.g_classes.descriptors[i].numNlAttrsInNonGen = 0;
M22_Class.g_classes.descriptors[i].hasNlAttrsInGenInclSubClasses = false;
M22_Class.g_classes.descriptors[i].hasNlAttrsInNonGenInclSubClasses = false;
// ### IF IVK ###
M22_Class.g_classes.descriptors[i].hasGroupIdAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasGroupIdAttrInNonGen;
M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInGen;
M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInNonGen;
M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInGen;
M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInNonGen;
M22_Class.g_classes.descriptors[i].containsIsNotPublishedInclSubClasses = M22_Class.g_classes.descriptors[i].containsIsNotPublished;
// ### ENDIF IVK ###
}

for (i = 1; i <= 1; i += (1)) {
for (j = 1; j <= 1; j += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].sectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[j].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.g_attributes.descriptors[j].acmEntityIndex = i;
M24_Attribute.g_attributes.descriptors[j].isPdmSpecific = M24_Attribute.g_attributes.descriptors[j].isPdmSpecific |  M22_Class.g_classes.descriptors[i].isPdmSpecific;
if (!(M22_Class.g_classes.descriptors[i].notAcmRelated)) {
M24_Attribute.g_attributes.descriptors[j].isNotAcmRelated = false;
}

// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[j].attributeName.toUpperCase() == M01_ACM_IVK.conHasConflict.toUpperCase()) {
M22_Class.g_classes.descriptors[i].hasAttrHasConflict = true;
}

// ### ENDIF IVK ###
if (!(M22_Class.g_classes.descriptors[i].isGenForming &  M24_Attribute.g_attributes.descriptors[j].isTimeVarying)) {
M04_Utilities.logMsg("Attribute \"" + M24_Attribute.g_attributes.descriptors[j].className + "." + M24_Attribute.g_attributes.descriptors[j].attributeName + " is marked as 'timeVarying' but class is not 'genForming' - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M24_Attribute.g_attributes.descriptors[j].isTimeVarying = false;
}
if (M24_Attribute.g_attributes.descriptors[j].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
M22_Class.g_classes.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M22_Class.g_classes.descriptors[i].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtEnum;
// ### IF IVK ###
} else if (M26_Type.isType(M24_Attribute.g_attributes.descriptors[j].domainSection, M24_Attribute.g_attributes.descriptors[j].domainName, null)) {
M22_Class.g_classes.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M22_Class.g_classes.descriptors[i].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtType;
// ### ENDIF IVK ###
} else {
M22_Class.g_classes.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M22_Class.g_classes.descriptors[i].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute;
}
M22_Class.g_classes.descriptors[i].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M22_Class.g_classes.descriptors[i].attrRefs)].refIndex = j;

if (M24_Attribute.g_attributes.descriptors[j].isNl) {
M22_Class.g_classes.descriptors[i].nlAttrRefs.descriptors[(M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M22_Class.g_classes.descriptors[i].nlAttrRefs))] = M22_Class.g_classes.descriptors[i].attrRefs.descriptors[M22_Class.g_classes.descriptors[i].attrRefs.numDescriptors];
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[j].isTimeVarying & ! M22_Class.g_classes.descriptors[i].hasNoIdentity) {
// ### ELSE IVK ###
//                 If g_attributes.descriptors(j).isTimeVarying Then
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[i].numNlAttrsInGen = M22_Class.g_classes.descriptors[i].numNlAttrsInGen + 1;
} else {
M22_Class.g_classes.descriptors[i].numNlAttrsInNonGen = M22_Class.g_classes.descriptors[i].numNlAttrsInNonGen + 1;
}
} else {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[j].isTimeVarying & ! M22_Class.g_classes.descriptors[i].hasNoIdentity) {
// ### ELSE IVK ###
//                 If g_attributes.descriptors(j).isTimeVarying Then
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[i].numAttrsInGen = M22_Class.g_classes.descriptors[i].numAttrsInGen + 1;
} else {
M22_Class.g_classes.descriptors[i].numAttrsInNonGen = M22_Class.g_classes.descriptors[i].numAttrsInNonGen + 1;
}
}

if (!(M04_Utilities.strArrayIsNull(M24_Attribute.g_attributes.descriptors[j].mapsToChangeLogAttributes))) {
for (int k = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[j].mapsToChangeLogAttributes); k <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[j].mapsToChangeLogAttributes); k++) {
// ### IF IVK ###
M23_Relationship.addAttrMapping(M22_Class.g_classes.descriptors[i].clMapAttrs, M24_Attribute.g_attributes.descriptors[j].attributeName + (M24_Attribute.g_attributes.descriptors[j].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum ? M01_Globals.gc_enumAttrNameSuffix : ""), M24_Attribute.g_attributes.descriptors[j].mapsToChangeLogAttributes[k], M24_Attribute.g_attributes.descriptors[j].isTimeVarying & ! M22_Class.g_classes.descriptors[i].hasNoIdentity, j);
// ### ELSE IVK ###
//                 addAttrMapping g_classes.descriptors(i).clMapAttrs, .attributeName & IIf(.valueType = eavtEnum, gc_enumAttrNameSuffix, ""), .mapsToChangeLogAttributes(k), _
//                   .isTimeVarying, j
// ### ENDIF IVK ###
}
}
}
}

// determine references to indexes
M22_Class.g_classes.descriptors[i].indexRefs.numRefs = 0;
for (j = 1; j <= 1; j += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M76_Index.g_indexes.descriptors[j].sectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == M76_Index.g_indexes.descriptors[j].className.toUpperCase()) {
M22_Class.g_classes.descriptors[i].indexRefs.refs[(M76_Index_Utilities.allocIndexDescriptorRefIndex(M22_Class.g_classes.descriptors[i].indexRefs))] = j;
}
}

// ### IF IVK ###
// determine references to relationships
M22_Class.g_classes.descriptors[i].allowedCountriesRelIndex = -1;
M22_Class.g_classes.descriptors[i].disAllowedCountriesRelIndex = -1;
M22_Class.g_classes.descriptors[i].allowedCountriesListRelIndex = -1;
M22_Class.g_classes.descriptors[i].disAllowedCountriesListRelIndex = -1;

// ### ENDIF IVK ###
boolean invertDirection;
M22_Class.g_classes.descriptors[i].relRefs.numRefs = 0;
for (j = 1; j <= 1; j += (1)) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].leftClassSectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].leftClassName.toUpperCase()) {
// ### IF IVK ###
// prefer directed relationship if we have the choice between both directions
if (M22_Class.g_classes.descriptors[M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[j].leftClassSectionName, M23_Relationship.g_relationships.descriptors[j].leftClassName, null)].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M22_Class.getClassIndexByName(M23_Relationship.g_relationships.descriptors[j].rightClassSectionName, M23_Relationship.g_relationships.descriptors[j].rightClassName, null)].orMappingSuperClassIndex &  M23_Relationship.g_relationships.descriptors[j].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[j].maxRightCardinality != 1) {
// restrict this to individual relationship: hack to avoid re-ordering of columns in tables (MIG-team would complain)
invertDirection = M23_Relationship.g_relationships.descriptors[j].relName.compareTo("ExtendsSr0Validity") == 0;
} else {
invertDirection = false;
}
// Fixme: get rid of hard coded relationship names
if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase(), "DISALLOWEDCOUNTRIESLIST") != 0) {
M22_Class.g_classes.descriptors[i].disAllowedCountriesListRelIndex = j;
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase(), "DISALLOWEDCOUNTRIES")) {
M22_Class.g_classes.descriptors[i].disAllowedCountriesRelIndex = j;
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase(), "ALLOWEDCOUNTRIESLIST")) {
M22_Class.g_classes.descriptors[i].allowedCountriesListRelIndex = j;
} else if (M00_Helper.inStr(M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase(), "ALLOWEDCOUNTRIES")) {
M22_Class.g_classes.descriptors[i].allowedCountriesRelIndex = j;
}

// ### ELSE IVK ###
//             invertDirection = False
// ### ENDIF IVK ###

M22_Class.g_classes.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M22_Class.g_classes.descriptors[i].relRefs)].refIndex = j;
M22_Class.g_classes.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M22_Class.g_classes.descriptors[i].relRefs)].refType = (invertDirection ? M01_Common.RelNavigationDirection.etRight : M01_Common.RelNavigationDirection.etLeft);
} else if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].rightClassSectionName.toUpperCase() &  M22_Class.g_classes.descriptors[i].className.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].rightClassName.toUpperCase()) {
invertDirection = false;

M22_Class.g_classes.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M22_Class.g_classes.descriptors[i].relRefs)].refIndex = j;
M22_Class.g_classes.descriptors[i].relRefs.refs[M23_Relationship_Utilities.allocRelDescriptorRefIndex(M22_Class.g_classes.descriptors[i].relRefs)].refType = (invertDirection ? M01_Common.RelNavigationDirection.etLeft : M01_Common.RelNavigationDirection.etRight);
}
}
}

for (i = 1; i <= 1; i += (1)) {
// verify consistency of aggregate heads with object relational mapping
if (M22_Class.g_classes.descriptors[i].aggHeadClassIndex > 0) {
if (M22_Class.g_classes.descriptors[i].aggHeadClassIndex != M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex) {
M04_Utilities.logMsg("potential inconsistency: aggregate head of class '" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "' is not identical to its 'OR-mapping parent class' " + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].className + "'", M01_Common.LogLevel.ellInfo, null, null, null);
M22_Class.g_classes.descriptors[i].aggHeadClassIndex = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex;
M22_Class.g_classes.descriptors[i].aggHeadSection = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].sectionName;
M22_Class.g_classes.descriptors[i].aggHeadName = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].className;
M22_Class.g_classes.descriptors[i].aggHeadClassIdStr = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].classIdStr;
M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].orMappingSuperClassIndex].isAggHead = true;
}
}
}

for (i = 1; i <= 1; i += (1)) {
// determine all subclasses (recurse down)
// Important: can only be done after direct subclasses of all classes are determined
M22_Class.g_classes.descriptors[i].subclassIndexesRecursive = M22_Class.getSubclassIndexesRecursive(i);
M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstract = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[i].classIndex);
M22_Class.g_classes.descriptors[i].attrRefsInclSubClasses = M22_Class.g_classes.descriptors[i].attrRefs;
M22_Class.g_classes.descriptors[i].attrRefsInclSubClassesWithRepeat = M22_Class.g_classes.descriptors[i].attrRefs;
M22_Class.g_classes.descriptors[i].nlAttrRefsInclSubclasses = M22_Class.g_classes.descriptors[i].nlAttrRefs;
M22_Class.g_classes.descriptors[i].hasNlAttrsInGenInclSubClasses = (M22_Class.g_classes.descriptors[i].numNlAttrsInGen > 0);
M22_Class.g_classes.descriptors[i].hasNlAttrsInNonGenInclSubClasses = (M22_Class.g_classes.descriptors[i].numNlAttrsInNonGen > 0);
M22_Class.g_classes.descriptors[i].implicitelyGenChangeComment = M22_Class.g_classes.descriptors[i].sectionShortName.compareTo("PST") == 0 & ! M22_Class.g_classes.descriptors[i].condenseData;
M22_Class.g_classes.descriptors[i].clMapAttrsInclSubclasses = M22_Class.g_classes.descriptors[i].clMapAttrs;
// ### IF IVK ###
M22_Class.g_classes.descriptors[i].groupIdAttrIndexesInclSubclasses = M22_Class.g_classes.descriptors[i].groupIdAttrIndexes;
// ### ENDIF IVK ###

M22_Class.addAggChildClassIndex(M22_Class.g_classes.descriptors[i].aggHeadClassIndex, M22_Class.g_classes.descriptors[i].classIndex);

for (j = 1; j <= 1; j += (1)) {
M22_Class.g_classes.descriptors[i].hasNlAttrsInGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasNlAttrsInGenInclSubClasses |  (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].numNlAttrsInGen > 0);
M22_Class.g_classes.descriptors[i].hasNlAttrsInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasNlAttrsInNonGenInclSubClasses |  (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].numNlAttrsInNonGen > 0);
// ### IF IVK ###
M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInGenInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].hasExpBasedVirtualAttrInGen;
M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasExpBasedVirtualAttrInNonGenInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].hasExpBasedVirtualAttrInNonGen;
M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInGenInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].hasRelBasedVirtualAttrInGen;
M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInNonGenInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].hasRelBasedVirtualAttrInNonGen;
M22_Class.g_classes.descriptors[i].hasGroupIdAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[i].hasGroupIdAttrInNonGenInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].hasGroupIdAttrInNonGen;
M22_Class.g_classes.descriptors[i].containsIsNotPublishedInclSubClasses = M22_Class.g_classes.descriptors[i].containsIsNotPublishedInclSubClasses |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].containsIsNotPublished;

M22_Class.g_classes.descriptors[i].hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[i].hasPriceAssignmentSubClass |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].isPriceAssignment;
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].isPriceAssignment & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].isAbstract) {
M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstractPriceAssignment = (M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstractPriceAssignment.compareTo("") == 0 ? "" : M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstractPriceAssignment + ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].classIdStr + "'";
}

// check if some subclass is PS-tagged while this class is not
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].isPsTagged) {
if (!(M22_Class.g_classes.descriptors[i].isPsTagged)) {
M22_Class.g_classes.descriptors[i].isPsTagged = true;
M22_Class.g_classes.descriptors[i].psTagOptional = true;
}
}
// ### ENDIF IVK ###

for (int k = 1; k <= M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].attrRefs.numDescriptors; k++) {
M24_Attribute_Utilities.addAttrDescriptorRef(M22_Class.g_classes.descriptors[i].attrRefsInclSubClasses, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].attrRefs.descriptors[k].refIndex, null);
M24_Attribute_Utilities.addAttrDescriptorRef(M22_Class.g_classes.descriptors[i].attrRefsInclSubClassesWithRepeat, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].attrRefs.descriptors[k].refIndex, true);
}
for (int k = 1; k <= M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].nlAttrRefs.numDescriptors; k++) {
M24_Attribute_Utilities.addAttrDescriptorRef(M22_Class.g_classes.descriptors[i].nlAttrRefsInclSubclasses, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].nlAttrRefs.descriptors[k].refIndex, null);
}

// ### IF IVK ###
for (int k = 1; k <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].groupIdAttrIndexes); k++) {
M22_Class.addGroupIdAttrIndexInclSubClasses(i, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].groupIdAttrIndexes[k]);
}

// ### ENDIF IVK ###
if (!(M04_Utilities.arrayIsNull(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs))) {
for (int k = M00_Helper.lBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs); k <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs); k++) {
M23_Relationship.addAttrMapping(M22_Class.g_classes.descriptors[i].clMapAttrsInclSubclasses, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs[k].mapFrom, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs[k].mapTo, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs[k].isTv, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].clMapAttrs[k].attrIndex);
}
}
// ### IF IVK ###

// propagate '(dis)allowedCountries-relationships'
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].allowedCountriesRelIndex > 0) {
M22_Class.g_classes.descriptors[i].allowedCountriesRelIndex = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].allowedCountriesRelIndex;
}
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].disAllowedCountriesRelIndex > 0) {
M22_Class.g_classes.descriptors[i].disAllowedCountriesRelIndex = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].disAllowedCountriesRelIndex;
}
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].allowedCountriesListRelIndex > 0) {
M22_Class.g_classes.descriptors[i].allowedCountriesListRelIndex = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].allowedCountriesListRelIndex;
}
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].disAllowedCountriesListRelIndex > 0) {
M22_Class.g_classes.descriptors[i].disAllowedCountriesListRelIndex = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].subclassIndexesRecursive[j]].disAllowedCountriesListRelIndex;
}
// ### ENDIF IVK ###
}
// ### IF IVK ###

// determine whether aggregate head is price assignment
if (M22_Class.g_classes.descriptors[i].aggHeadClassIndexExact > 0) {
M22_Class.g_classes.descriptors[i].hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndexExact].hasPriceAssignmentSubClass & ! M22_Class.g_classes.descriptors[i].isAggHead;
} else if (M22_Class.g_classes.descriptors[i].aggHeadClassIndex > 0) {
M22_Class.g_classes.descriptors[i].hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].aggHeadClassIndex].hasPriceAssignmentSubClass;
}
// ### ENDIF IVK ###
//
// determine all references to relationships (recurse down)
// Important: can only be done after relrefs of all classes are determined
M22_Class.g_classes.descriptors[i].relRefsRecursive = M22_Class.getRelRefsRecursive(i);
}

// ### IF IVK ###
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (!(M22_Class.g_classes.descriptors[i].rangePartitionGroup.compareTo("") == 0)) {
M22_Class_Utilities.addStrListMapEntry(M22_Class.g_classes.descriptors[i].subClassIdStrSeparatePartition, M22_Class.g_classes.descriptors[i].rangePartitionGroup, M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstract);
}
}

for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
M22_Class.getSubClassIdStrListPartitionGroupMap(M22_Class.g_classes.descriptors[i].classIndex);
M22_Class.g_classes.descriptors[i].useLrtCommitPreprocess = (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnGenericCode.toUpperCase()) |  (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnTypeSpec.toUpperCase()) | M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInGenInclSubClasses | M22_Class.g_classes.descriptors[i].hasRelBasedVirtualAttrInNonGenInclSubClasses;
M22_Class.g_classes.descriptors[i].isSubjectToExpCopy = M22_Class.g_classes.descriptors[i].isUserTransactional &  M22_Class.g_classes.descriptors[i].aggHeadName.toUpperCase() == M01_ACM_IVK.clnExpression.toUpperCase();
}

// determine boundaries of partition-ranges
if (M03_Config.supportRangePartitioningByClassId) {
String[] subClassIdStrings;
String minClassIdStr;
String lastMinClassIdStr;
String lowerBoundClassIdStr;
boolean foundNewMinClassId;
String matchingRangeIndexes;
String lastMatchingRangeIndexes;
int thisBoundaryIndex;
String thisClassIdStr;

lastMatchingRangeIndexes = "";
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[i].subClassIdStrSeparatePartition.numMaps > 0 &  (M22_Class.g_classes.descriptors[i].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[i].classIndex)) {
// loop over all sub-classes (ascending order)
// - if set of 'matching range definitions' differs to 'previous classid' this classid defines the lower bound of a new effective range

matchingRangeIndexes = "";
subClassIdStrings = M22_Class.g_classes.descriptors[i].subclassIdStrListNonAbstract.split(",");
lastMinClassIdStr = M22_Class_Utilities.getClassId(0, 0);
lowerBoundClassIdStr = M22_Class_Utilities.getClassId(0, 0);
minClassIdStr = M22_Class_Utilities.getClassId(99, 999);
foundNewMinClassId = true;
thisBoundaryIndex = 1;
while (foundNewMinClassId) {
foundNewMinClassId = false;
matchingRangeIndexes = "";

// lookup 'next smallest' classid
for (int j = 0; j <= M00_Helper.uBound(subClassIdStrings); j++) {
thisClassIdStr = M00_Helper.replace(subClassIdStrings[j], "'", "");
if (StrComp(thisClassIdStr, minClassIdStr, vbTextCompare) == -1 &  StrComp(thisClassIdStr, lowerBoundClassIdStr, vbTextCompare) == 1) {
minClassIdStr = M00_Helper.replace(thisClassIdStr, "'", "");
foundNewMinClassId = true;
}
}

if (foundNewMinClassId) {
// determine set of range-definitions holding this classid
for (int k = 1; k <= M22_Class.g_classes.descriptors[i].subClassIdStrSeparatePartition.numMaps; k++) {
if (M00_Helper.inStr(1, M22_Class.g_classes.descriptors[i].subClassIdStrSeparatePartition.maps[k].list, "'" + minClassIdStr + "'") != 0) {
matchingRangeIndexes = matchingRangeIndexes + "-" + k + "-";
}
}

// if set of matching range indexes differs from previous one, this classid defines the lower bound of a new range
if (!(matchingRangeIndexes.compareTo(lastMatchingRangeIndexes) == 0) &  lastMinClassIdStr != M22_Class_Utilities.getClassId(0, 0)) {
if (thisBoundaryIndex == 1) {
M22_Class.g_classes.descriptors[i].subClassPartitionBoundaries[(1, thisBoundaryIndex)] = "";
}
M22_Class.g_classes.descriptors[i].subClassPartitionBoundaries[(2, thisBoundaryIndex)] = lastMinClassIdStr;
thisBoundaryIndex = thisBoundaryIndex + 1;
M22_Class.g_classes.descriptors[i].subClassPartitionBoundaries[(1, thisBoundaryIndex)] = minClassIdStr;
M22_Class.g_classes.descriptors[i].subClassPartitionBoundaries[(2, thisBoundaryIndex)] = "";
}
lastMinClassIdStr = minClassIdStr;
lowerBoundClassIdStr = minClassIdStr;
minClassIdStr = M22_Class_Utilities.getClassId(99, 999);
lastMatchingRangeIndexes = matchingRangeIndexes;
}
}
}
}
}
// ### ENDIF IVK ###
}


// ### IF IVK ###
public static void evalClasses2() {
int i;
int j;
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
// determine navigation path to division
M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex = -1;
if (!(M22_Class.g_classes.descriptors[i].navPathStrToDivision.compareTo("") == 0)) {
M22_Class_Utilities.genNavPathForClass(M22_Class.g_classes.descriptors[i].navPathToDiv, M22_Class.g_classes.descriptors[i].navPathStrToDivision, M01_Globals_IVK.g_classIndexDivision);
}

// determine navigation path to Organization
M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex = -1;
if (!(M22_Class.g_classes.descriptors[i].navPathStrToOrg.compareTo("") == 0)) {
M22_Class_Utilities.genNavPathForClass(M22_Class.g_classes.descriptors[i].navPathToOrg, M22_Class.g_classes.descriptors[i].navPathStrToOrg, M01_Globals.g_classIndexOrganization);
}

// determine navigation path to Code
M22_Class.g_classes.descriptors[i].navPathToCodeType.relRefIndex = -1;
if (!(M22_Class.g_classes.descriptors[i].navPathStrToCodeType.compareTo("") == 0)) {
M22_Class_Utilities.genNavPathForClass(M22_Class.g_classes.descriptors[i].navPathToCodeType, M22_Class.g_classes.descriptors[i].navPathStrToCodeType, M01_Globals_IVK.g_classIndexCodeType);
}
}
}


// ### ENDIF IVK ###
private static void printsubClassHier(int thisClassIndex, int level) {
int i;
for (int i = M00_Helper.lBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes); i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes); i++) {
if (i > 0) {
System.out.println(M04_Utilities.addTab(level) + M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[i] + " - " + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[i]].className);
;
printsubClassHier(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexes[i], level + 1);
}
}
}


private static void printRefs() {
int i;
int j;
M24_Attribute_Utilities.AttributeDescriptor attr;
M24_Attribute_Utilities.AttributeDescriptor refAttr;

for (i = 1; i <= 1; i += (1)) {
//        Debug.Print .className & " : " & .attrRefs.numDescriptors
for (int j = 1; j <= M22_Class.g_classes.descriptors[i].attrRefs.numDescriptors; j++) {
//          Debug.Print .className & " / " & .attrRefs.descriptors(j).refType & " / " & .attrRefs.descriptors(j).refIndex
}
}

for (i = 1; i <= 1; i += (1)) {
for (int j = 1; j <= M22_Class.g_classes.descriptors[i].attrRefs.numDescriptors; j++) {
attr = M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[i].attrRefs.descriptors[j].refIndex];
if (attr.reusedAttrIndex > 0) {
refAttr = M24_Attribute.g_attributes.descriptors[attr.reusedAttrIndex];
System.out.println(attr.attributeName + "@" + attr.className + " [" + M22_Class.g_classes.descriptors[i].attrRefs.descriptors[j].refIndex + "] -> " + refAttr.attributeName + "@" + refAttr.className + " [" + attr.reusedAttrIndex + "]");
;
}
}
}
}





}