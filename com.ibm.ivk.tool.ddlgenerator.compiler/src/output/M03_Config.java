package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M03_Config {




public class ConfigMode {
public static final int ecfgTest = 1;
public static final int ecfgProductionEw = 2;
public static final int ecfgDelivery = 3;
}

//''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
// Configurable settings
public static long g_cfgSqlStateStart;
public static String g_cfgSqlMsgPrefix;

public static boolean g_cfgGenLogChangeForLrtTabs;
public static boolean g_cfgGenLogChangeForNlTabs;
public static boolean g_cfgGenLogChangeForGenTabs;

public static boolean g_cfgLrtGenDB2View;
public static boolean g_cfgLrtGenDB2Trigger;

public static String productKey;
public static String versionString;
public static String targetPlatform;
public static String[] environmentIds;
public static String entityFilterKeys;
public static String hiddenWorksheetSuffixes;
public static String snapshotApiVersion;
public static boolean supportSpLogging;
public static boolean generateSpLogMessages;
public static Integer spLogMode;
public static boolean spLogAutonomousTransaction;
public static boolean supportIndexMetrics;
public static boolean supportCompresionEstimation;
public static String workSheetSuffix;
public static boolean generateFwkTest;
public static boolean supportVirtualColumns;
public static long virtualColumnSyncCommitCount;
public static boolean supportGroupIdColumns;
public static boolean maintainGroupIdColumnsInLrtTrigger;
public static boolean maintainGroupIdColumnsInLrtCommit;
public static boolean maintainGroupIdColumnsInSetProductive;
public static boolean generateDdlHeader;
// ### IF IVK ###
public static boolean generatePsTaggingView;
public static boolean supportFilteringByPsDpMapping;
public static boolean usePsFltrByDpMappingForRegularViews;
public static boolean generatePsTaggingTrigger;
public static boolean generatePsCopySupport;
public static boolean generatePsCopyExtendedSupport;
public static boolean generatePsCreateSupport;
public static boolean generateExpCopySupport;
// ### ENDIF IVK ###
public static boolean generateLogChangeView;
public static boolean reuseRelationships;
public static boolean supportArchivePool;
public static boolean generateArchiveView;
public static boolean generateLdm;
public static boolean formatLdmForWord;
public static boolean generatePdm;
public static String pdmSchemaNamePattern;
public static boolean generateLrt;
public static boolean generateNonLrt;
public static boolean generateDeployPackage;
public static boolean generateUpdatePackage;
public static boolean includeExplainDdlInDeliveryPackage;
public static boolean bindJdbcPackagesWithReoptAlways;
public static boolean setDefaultCfgDuringDeployment;

public static boolean generateDdlCreateTable;
public static boolean generateDdlCreateIndex;
public static boolean generateDdlCreatePK;
public static boolean generateDdlCreateFK;
public static boolean generateDdlCreateSeq;
public static boolean exportVBCode;
public static boolean exportXlsSheets;
public static boolean includeUtilityScrptsinPackage;

public static boolean generateUpdatableCheckInUpdateTrigger;
public static boolean generateDb2RegistryCheckInSps;
public static boolean generateCommentOnTables;
public static boolean generateCommentOnColumns;
public static boolean generateCommentOnAliases;
public static boolean generateLrtSps;
// ### IF IVK ###
public static boolean lrtLogRetrieveSr0CodesFromSr0Context;
public static boolean genSupportForHibernate;
// ### ENDIF IVK ###
public static boolean generateAhIdsNotNull;
public static boolean disableLoggingDuringSync;
public static boolean supportUnicode;
public static double unicodeExpansionFactor;
public static boolean generateEntityIdList;
// ### IF IVK ###
public static boolean generateXmlExportSupport;
public static boolean generateXmlXsdFuncs;
public static boolean generateXmlExportFuncs;
public static boolean generateXmlExportViews;
public static boolean generateXsdInCtoSchema;
public static boolean xmlExportVirtualColumns;
public static boolean xmlExportColumnInLrt;
public static boolean xmlExportColumnClassId;
public static boolean xmlExportColumnVersionId;
public static boolean generateXmlPsOidColForPsTaggedEntities;
// ### ENDIF IVK ###
public static boolean dbCompressSystemDefaults;
public static boolean dbCompressValues;
public static boolean dbCompressValuesInNlsTabs;
public static boolean dbCompressValuesInEnumTabs;
// ### IF IVK ###
public static String maxXmlExportStringLength;
// ### ENDIF IVK ###
public static boolean generateIndexOnFk;
public static boolean generateIndexOnLrtTabs;
public static boolean generateIndexOnFkForNLang;
// ### IF IVK ###
public static boolean generateIndexOnFkForPsTag;
// ### ENDIF IVK ###
public static boolean generateIndexOnFkForEnums;
public static boolean generateIndexOnFkForLrtId;
public static boolean generateIndexOnClassId;
public static boolean generateIndexOnValidFromUntil;
public static boolean generateIndexOnValidFrom;
public static boolean generateIndexOnValidUntil;
public static boolean generateIndexOnAhClassIdOid;
public static boolean generateIndexOnAhClassIdOidStatus;
public static boolean generateIndexOnAhOid;
// ### IF IVK ###
public static boolean generateIndexOnExpressionFks;
public static boolean generateIndexForSetProductive;
public static boolean generateStatusCheckDdl;
// ### ENDIF IVK ###
public static boolean useSurrogateKeysForNMRelationships;
public static boolean reuseColumnsInTabsForOrMapping;
// ### IF IVK ###
public static boolean generateSupportForUc304;
public static boolean hasBeenSetProductiveInPrivLrt;
// ### ENDIF IVK ###
public static boolean useMqtToImplementLrt;
public static boolean activateLrtMqtViews;
public static boolean implementLrtNonMqtViewsForEntitiesSupportingMqts;
public static boolean includeTermStringsInMqt;
public static int numRetriesRunstatsRebindOnLockTimeout;
// fixme: this is disabled because of a bug - this feature is not supported yet
public static boolean lrtDistinguishGenAndNlTextTabsInAffectedEntities;
public static boolean maintainVirtAttrInTriggerOnRelTabs;
public static boolean maintainVirtAttrInTriggerPubOnRelTabs;
public static boolean maintainVirtAttrInTriggerOnEntityTabs;
public static boolean maintainVirtAttrInTriggerPubOnEntityTabs;
public static boolean maintainVirtAttrInTriggerPrivOnEntityTabs;

public static boolean lrtTablesVolatile;

public static boolean navToAggHeadForClAttrs;
public static boolean cr132;

public static boolean genDataCheckCl;
public static boolean supportSimulationSps;
public static boolean genTemplateDdl;
// ### IF IVK ###
public static boolean supportSstCheck;
public static boolean supportSectionDataFix;
public static boolean resolveCountryIdListInChangeLog;
public static boolean lrtCommitDeleteDeletedNonProductiveRecords;
public static boolean ftoLockSingleObjectProcessing;
// ### ENDIF IVK ###
public static boolean genFksForLrtOnRelationships;
// ### IF IVK ###
public static boolean genTimeStampsDuringOrgInit;
public static String listRangePartitionTablesByPsOid;
public static boolean supportRangePartitioningByPsOid;
public static String listRangePartitionTablesByDivOid;
public static boolean supportRangePartitioningByDivOid;
public static boolean supportRangePartitioningByClassId;
public static boolean supportRangePartitioningByClassIdFirstPsOid;
public static boolean usePsTagInNlTextTables;
public static boolean partitionLrtPrivateWhenMqt;
public static boolean partitionLrtPublicWhenMqt;
public static boolean partitionLrtPrivateWhenNoMqt;
public static boolean partitionLrtPublicWhenNoMqt;
public static String noPartitioningInDataPools;
public static boolean supportCtsConfigByTemplate;
public static boolean supportAddTestUser;
// ### ENDIF IVK ###
public static boolean supportDbCompact;
public static boolean supportColumnIsInstantiatedInAcmAttribute;

// Global settings
public static final boolean ignoreUnknownSections = true;
public static final boolean genIndexesForAcmClasses = true;
public static final boolean includeFksInPks = true;
// ### IF IVK ###
public static final boolean reusePsTagForRelationships = false;
public static final boolean nationalFlagPartOfPK = false;
// ### ENDIF IVK ###
public static final boolean supportNlForRelationships = true;
// ### IF IVK ###
public static final boolean supportAliasDelForNonLrtPools = false;
// ### ENDIF IVK ###
public static final boolean referToAggHeadInChangeLog = true;


private static final int colCategory = 2;
private static final int colSubCategory = colCategory + 1;
private static final int colKey = colSubCategory + 1;
private static final int colParameter = colKey + 1;
private static final int colSetting = colParameter + 1;
private static final int colEffectiveSetting = colSetting + 1;

private static final int colIrregularSettingTest = 10;
private static final int colIrregularSettingProductionEw = colIrregularSettingTest + 5;
private static final int colIrregularSettingDelivery = colIrregularSettingProductionEw + 5;

private static final int colEffectiveSettingFwkTest = colEffectiveSetting;
private static final int colEffectiveSettingProductionEw = colEffectiveSettingFwkTest + 5;
private static final int colEffectiveSettingDelivery = colEffectiveSettingProductionEw + 5;

private static final int firstRow = 4;

private static final String sheetName = "Config";

public static final String configSheetName = sheetName;

private static Integer getColSettingIrreg(Integer cfgModeW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

Integer returnValue;
switch (cfgMode) {
case M03_Config.ConfigMode.ecfgTest: {returnValue = colIrregularSettingTest;
}case M03_Config.ConfigMode.ecfgProductionEw: {returnValue = colIrregularSettingProductionEw;
}case M03_Config.ConfigMode.ecfgDelivery: {returnValue = colIrregularSettingDelivery;
}default: {returnValue = null;
}}
return returnValue;
}


private static Integer getColEffectiveSetting(Integer cfgModeW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

Integer returnValue;
int offset;

switch (cfgMode) {
case M03_Config.ConfigMode.ecfgTest: {returnValue = colEffectiveSetting;
}case M03_Config.ConfigMode.ecfgProductionEw: {returnValue = colEffectiveSettingProductionEw;
}case M03_Config.ConfigMode.ecfgDelivery: {returnValue = colEffectiveSettingDelivery;
}default: {returnValue = null;
}}
return returnValue;
}


public static Boolean irregularSetting(Integer cfgModeW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

Boolean returnValue;
int rowOffset;
rowOffset = (M00_Excel.getCell(M00_Excel.activeWorkbook.getSheet(sheetName), 1, 1).getStringCellValue() == "" ? 0 : 1);
returnValue = String.valueOf(M00_Excel.getCell(M00_Excel.activeWorkbook.getSheet(sheetName), firstRow - 1 + rowOffset, getColSettingIrreg(cfgMode)).getStringCellValue()) != "0";
return returnValue;
}

public static void readConfig(Integer cfgModeW, Boolean silentW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(sheetName);
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

int colSetting;
colSetting = getColEffectiveSetting(cfgMode);

String environmentIdsStr;

String key;
String setting;
String parameter;
while (M00_Excel.getCell(thisSheet, thisRow, colKey).getStringCellValue() + "" != "" |  M00_Excel.getCell(thisSheet, thisRow + 1, colKey).getStringCellValue() + "" != "") {
key = M00_Excel.getCell(thisSheet, thisRow, colKey).getStringCellValue().trim();
setting = M00_Excel.getCell(thisSheet, thisRow, colSetting).getStringCellValue();
parameter = (silent ? "" : M00_Excel.getCell(thisSheet, thisRow, colParameter).getStringCellValue());

if (key.compareTo("GPKY") == 0) {
M03_Config.productKey = setting;
} else if (key.compareTo("GVER") == 0) {
M03_Config.versionString = setting;
} else if (key.compareTo("GWSS") == 0) {
M03_Config.workSheetSuffix = setting;
} else if (key.compareTo("GSQS") == 0) {
M03_Config.g_cfgSqlStateStart = M04_Utilities.getLong(setting, 79000);
} else if (key.compareTo("GTGP") == 0) {
M03_Config.targetPlatform = setting;
} else if (key.compareTo("GENV") == 0) {
environmentIdsStr = M00_Helper.replace(setting, " ", "");
M24_Attribute.genAttrList(M03_Config.environmentIds, environmentIdsStr);
} else if (key.compareTo("GEFK") == 0) {
M03_Config.entityFilterKeys = M00_Helper.replace(setting, " ", "");
} else if (key.compareTo("GEPR") == 0) {
M03_Config.g_cfgSqlMsgPrefix = setting.trim() + " ";
} else if (key.compareTo("GDBV") == 0) {
M03_Config.snapshotApiVersion = M00_Helper.replace(setting, " ", "");
} else if (key.compareTo("GHWS") == 0) {
M03_Config.hiddenWorksheetSuffixes = setting.trim();
} else if (key.compareTo("GSPL") == 0) {
M03_Config.supportSpLogging = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GGSL") == 0) {
M03_Config.generateSpLogMessages = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("SPLA") == 0) {
M03_Config.spLogAutonomousTransaction = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("SPLM") == 0) {
M03_Config.spLogMode = M04_Utilities.getDbSpLogMode(setting);
} else if (key.compareTo("GSIM") == 0) {
M03_Config.supportIndexMetrics = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GSCE") == 0) {
M03_Config.supportCompresionEstimation = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GFWK") == 0) {
M03_Config.generateFwkTest = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GSVC") == 0) {
M03_Config.supportVirtualColumns = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCCC") == 0) {
M03_Config.virtualColumnSyncCommitCount = M04_Utilities.getLong(setting, -1);
} else if (key.compareTo("GGID") == 0) {
M03_Config.supportGroupIdColumns = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MGID") == 0) {
M03_Config.maintainGroupIdColumnsInLrtTrigger = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MGIC") == 0) {
M03_Config.maintainGroupIdColumnsInLrtCommit = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MGIS") == 0) {
M03_Config.maintainGroupIdColumnsInSetProductive = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GSKR") == 0) {
M03_Config.useSurrogateKeysForNMRelationships = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GRCO") == 0) {
M03_Config.reuseColumnsInTabsForOrMapping = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("GHPL") == 0) {
M03_Config.hasBeenSetProductiveInPrivLrt = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("GDHD") == 0) {
M03_Config.generateDdlHeader = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCTD") == 0) {
M03_Config.generateCommentOnTables = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCCD") == 0) {
M03_Config.generateCommentOnColumns = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCAD") == 0) {
M03_Config.generateCommentOnAliases = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCIL") == 0) {
M03_Config.generateEntityIdList = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GRER") == 0) {
M03_Config.reuseRelationships = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GRCK") == 0) {
M03_Config.generateDb2RegistryCheckInSps = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GUCH") == 0) {
M03_Config.generateUpdatableCheckInUpdateTrigger = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCLN") == 0) {
M03_Config.g_cfgGenLogChangeForNlTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GCLG") == 0) {
M03_Config.g_cfgGenLogChangeForGenTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DCSD") == 0) {
M03_Config.dbCompressSystemDefaults = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DCVA") == 0) {
M03_Config.dbCompressValues = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DCVN") == 0) {
M03_Config.dbCompressValuesInNlsTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DCVE") == 0) {
M03_Config.dbCompressValuesInEnumTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("AHNN") == 0) {
M03_Config.generateAhIdsNotNull = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GDSM") == 0) {
M03_Config.disableLoggingDuringSync = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GDLP") == 0) {
M03_Config.generateDeployPackage = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GUPP") == 0) {
M03_Config.generateUpdatePackage = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DEXP") == 0) {
M03_Config.includeExplainDdlInDeliveryPackage = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DBJO") == 0) {
M03_Config.bindJdbcPackagesWithReoptAlways = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DCFD") == 0) {
M03_Config.setDefaultCfgDuringDeployment = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DDLT") == 0) {
M03_Config.generateDdlCreateTable = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DDLI") == 0) {
M03_Config.generateDdlCreateIndex = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DDLP") == 0) {
M03_Config.generateDdlCreatePK = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DDLF") == 0) {
M03_Config.generateDdlCreateFK = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("DDLS") == 0) {
M03_Config.generateDdlCreateSeq = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("IUSC") == 0) {
M03_Config.includeUtilityScrptsinPackage = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("EVBC") == 0) {
M03_Config.exportVBCode = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("ESHE") == 0) {
M03_Config.exportXlsSheets = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LINL") == 0) {
M03_Config.generateNonLrt = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LILR") == 0) {
M03_Config.generateLrt = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LMQT") == 0) {
M03_Config.useMqtToImplementLrt = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LMQV") == 0) {
M03_Config.activateLrtMqtViews = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LMNM") == 0) {
M03_Config.implementLrtNonMqtViewsForEntitiesSupportingMqts = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LMTS") == 0) {
M03_Config.includeTermStringsInMqt = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LVOL") == 0) {
M03_Config.lrtTablesVolatile = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("LRSC") == 0) {
M03_Config.lrtLogRetrieveSr0CodesFromSr0Context = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("LILA") == 0) {
M03_Config.g_cfgGenLogChangeForLrtTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LIDV") == 0) {
M03_Config.g_cfgLrtGenDB2View = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LIDT") == 0) {
M03_Config.g_cfgLrtGenDB2Trigger = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LISP") == 0) {
M03_Config.generateLrtSps = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("LGSC") == 0) {
M03_Config.generateStatusCheckDdl = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("LDTT") == 0) {
M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MVTR") == 0) {
M03_Config.maintainVirtAttrInTriggerOnRelTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MVPU") == 0) {
M03_Config.maintainVirtAttrInTriggerPubOnRelTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MVEN") == 0) {
M03_Config.maintainVirtAttrInTriggerOnEntityTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MVEU") == 0) {
M03_Config.maintainVirtAttrInTriggerPubOnEntityTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("MVER") == 0) {
M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("HGCF") == 0) {
M03_Config.genSupportForHibernate = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("LILD") == 0) {
M03_Config.generateLdm = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("LFWR") == 0) {
M03_Config.formatLdmForWord = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIPD") == 0) {
M03_Config.generatePdm = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSNP") == 0) {
M03_Config.pdmSchemaNamePattern = setting.trim();
} else if (key.compareTo("PILT") == 0) {
M03_Config.generateIndexOnLrtTabs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIFK") == 0) {
M03_Config.generateIndexOnFk = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIFN") == 0) {
M03_Config.generateIndexOnFkForNLang = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("PIFP") == 0) {
M03_Config.generateIndexOnFkForPsTag = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("PIFE") == 0) {
M03_Config.generateIndexOnFkForEnums = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIFL") == 0) {
M03_Config.generateIndexOnFkForLrtId = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PICI") == 0) {
M03_Config.generateIndexOnClassId = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIVA") == 0) {
M03_Config.generateIndexOnValidFromUntil = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIVF") == 0) {
M03_Config.generateIndexOnValidFrom = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIVU") == 0) {
M03_Config.generateIndexOnValidUntil = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PNRR") == 0) {
M03_Config.numRetriesRunstatsRebindOnLockTimeout = M04_Utilities.getInteger(setting, null);
} else if (key.compareTo("PIAH") == 0) {
M03_Config.generateIndexOnAhClassIdOid = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIAS") == 0) {
M03_Config.generateIndexOnAhClassIdOidStatus = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PIAO") == 0) {
M03_Config.generateIndexOnAhOid = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("PIEX") == 0) {
M03_Config.generateIndexOnExpressionFks = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PISP") == 0) {
M03_Config.generateIndexForSetProductive = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSGV") == 0) {
M03_Config.generatePsTaggingView = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSFD") == 0) {
M03_Config.supportFilteringByPsDpMapping = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSFU") == 0) {
M03_Config.usePsFltrByDpMappingForRegularViews = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("LCGV") == 0) {
M03_Config.generateLogChangeView = M04_Utilities.getBoolean(setting, null);
// ### IF IVK ###
} else if (key.compareTo("U304") == 0) {
M03_Config.generateSupportForUc304 = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("ARCP") == 0) {
M03_Config.supportArchivePool = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("ARCV") == 0) {
M03_Config.generateArchiveView = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSGT") == 0) {
M03_Config.generatePsTaggingTrigger = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSCP") == 0) {
M03_Config.generatePsCopySupport = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSCX") == 0) {
M03_Config.generatePsCopyExtendedSupport = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("EXCP") == 0) {
M03_Config.generateExpCopySupport = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("PSCR") == 0) {
M03_Config.generatePsCreateSupport = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("UNCD") == 0) {
M03_Config.supportUnicode = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("UCEF") == 0) {
M03_Config.unicodeExpansionFactor = M04_Utilities.getSingle(setting, null);
// ### IF IVK ###
} else if (key.compareTo("XEXP") == 0) {
M03_Config.generateXmlExportSupport = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XEXX") == 0) {
M03_Config.generateXmlXsdFuncs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XEXV") == 0) {
M03_Config.generateXmlExportViews = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XEXF") == 0) {
M03_Config.generateXmlExportFuncs = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XEVC") == 0) {
M03_Config.xmlExportVirtualColumns = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XCIL") == 0) {
M03_Config.xmlExportColumnInLrt = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XCCI") == 0) {
M03_Config.xmlExportColumnClassId = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XCVI") == 0) {
M03_Config.xmlExportColumnVersionId = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XVCS") == 0) {
M03_Config.generateXsdInCtoSchema = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XEXV") == 0) {
M03_Config.generateXmlExportViews = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XVPT") == 0) {
M03_Config.generateXmlPsOidColForPsTaggedEntities = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("XMSL") == 0) {
M03_Config.maxXmlExportStringLength = setting;
} else if (key.compareTo("CR132") == 0) {
M03_Config.cr132 = M04_Utilities.getBoolean(setting, null);
// ### ENDIF IVK ###
} else if (key.compareTo("GDCC") == 0) {
M03_Config.genDataCheckCl = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("SSSP") == 0) {
M03_Config.supportSimulationSps = M04_Utilities.getBoolean(setting, null);
} else if (key.compareTo("GTDD") == 0) {
M03_Config.genTemplateDdl = M04_Utilities.getBoolean(setting, parameter);
// ### IF IVK ###
} else if (key.compareTo("SSCH") == 0) {
M03_Config.supportSstCheck = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("SSDF") == 0) {
M03_Config.supportSectionDataFix = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("RCLC") == 0) {
M03_Config.resolveCountryIdListInChangeLog = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("CDDD") == 0) {
M03_Config.lrtCommitDeleteDeletedNonProductiveRecords = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("FSOP") == 0) {
M03_Config.ftoLockSingleObjectProcessing = M04_Utilities.getBoolean(setting, parameter);
// ### ENDIF IVK ###
} else if (key.compareTo("GLFR") == 0) {
M03_Config.genFksForLrtOnRelationships = M04_Utilities.getBoolean(setting, parameter);
// ### IF IVK ###
} else if (key.compareTo("SRPP") == 0) {
M03_Config.listRangePartitionTablesByPsOid = M00_Helper.replace(M00_Helper.replace(setting, ".", ","), " ", "");
M03_Config.supportRangePartitioningByPsOid = !(M03_Config.listRangePartitionTablesByPsOid.compareTo("") == 0);
} else if (key.compareTo("OIGT") == 0) {
M03_Config.genTimeStampsDuringOrgInit = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("SRPD") == 0) {
M03_Config.listRangePartitionTablesByDivOid = M00_Helper.replace(M00_Helper.replace(setting, ".", ","), " ", "");
M03_Config.supportRangePartitioningByDivOid = !(M03_Config.listRangePartitionTablesByDivOid.compareTo("") == 0);
} else if (key.compareTo("SRPC") == 0) {
M03_Config.supportRangePartitioningByClassId = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("SR1P") == 0) {
M03_Config.supportRangePartitioningByClassIdFirstPsOid = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("PTNL") == 0) {
M03_Config.usePsTagInNlTextTables = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("PPUM") == 0) {
M03_Config.partitionLrtPublicWhenMqt = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("PPRM") == 0) {
M03_Config.partitionLrtPrivateWhenMqt = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("PPUV") == 0) {
M03_Config.partitionLrtPublicWhenNoMqt = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("PPRV") == 0) {
M03_Config.partitionLrtPrivateWhenNoMqt = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("NPDP") == 0) {
M03_Config.noPartitioningInDataPools = M00_Helper.replace(setting, " ", "");
} else if (key.compareTo("SCCT") == 0) {
M03_Config.supportCtsConfigByTemplate = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("SATU") == 0) {
M03_Config.supportAddTestUser = M04_Utilities.getBoolean(setting, parameter);
// ### ENDIF IVK ###
} else if (key.compareTo("SDBC") == 0) {
M03_Config.supportDbCompact = M04_Utilities.getBoolean(setting, parameter);
} else if (key.compareTo("AARE") == 0) {
M03_Config.supportColumnIsInstantiatedInAcmAttribute = M04_Utilities.getBoolean(setting, parameter);
}
thisRow = thisRow + 1;
}

if (M03_Config.spLogMode != M01_Common.DbSpLogMode.esplTable) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",L";
}
if (!(M03_Config.supportIndexMetrics)) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",IM";
}
// ### IF IVK ###
if (M03_Config.supportSectionDataFix) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",d";
} else {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",D";
}
// ### ENDIF IVK ###
if (M03_Config.snapshotApiVersion.compareTo("8") == 0) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",S9";
} else if (M00_Helper.replace(M03_Config.snapshotApiVersion.substring(0, 3), ",", ".").compareTo("9.7") == 0) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",S8";
}
// ### IF IVK ###
if (M03_Config.supportSstCheck) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",x";
} else {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",X";
}
// ### ENDIF IVK ###
if (M03_Config.supportColumnIsInstantiatedInAcmAttribute) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",r";
} else {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",R";
}
if (!(M00_Helper.inStr(1, "," + environmentIdsStr + ",", ",T,") != 0)) {
M03_Config.entityFilterKeys = M03_Config.entityFilterKeys + ",TE";
}
}


}