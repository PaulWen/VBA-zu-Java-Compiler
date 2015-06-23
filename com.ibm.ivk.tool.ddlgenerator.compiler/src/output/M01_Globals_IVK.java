package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_Globals_IVK {


// ### IF IVK ###


public static final String gc_tempTabNameConflict = "SESSION.Conflict";
public static final String gc_tempTabNameConflictPrice = "SESSION.ConflictPrice";
public static final String gc_tempTabNameConflictSlotNames = "SESSION.ConflictSlotNames";
public static final String gc_tempTabNameConflictMultiGa = "SESSION.ConflictMultiGa";
public static final String gc_tempTabNameConflictMultiSr = "SESSION.ConflictMultiSr";
public static final String gc_tempTabNameConflictMultiGaNl = "SESSION.ConflictMultiGaNl";
public static final String gc_tempTabNameConflictMultiCdNl = "SESSION.ConflictMultiCdNl";
public static final String gc_tempTabNameChangeLogSummary = "SESSION.ChangeLogSummary";
public static final String gc_tempTabNameChangeLogStatus = "SESSION.ChangeLogStatus";
public static final String gc_tempTabNameChangeLogOrgSummary = "SESSION.MpcChangeLogSummary";
public static final String gc_tempTabNameChangeLogImplicitChanges = "SESSION.MpcImplicitChangesSummary";
public static final String gc_tempTabNameRelevantCountryIdList = "SESSION.RelevantCountryIdList";
public static final String gc_tempTabNameRelevantCountryIdXRef = "SESSION.RelevantCountryIdXRef";
public static final String gc_tempTabNameManagedCountry = "SESSION.ManagedCountry";
public static final String gc_tempTabNameRelevantCountry = "SESSION.RelevantCountry";
public static final String gc_tempTabNameFtoClSr0ContextFac = "SESSION.Sr0ContextFac";
public static final String gc_tempTabNameFtoClSr0ContextOrg = "SESSION.Sr0ContextMpc";

public static final String gc_tempTabNameSpFilteredEntities = "SESSION.SpFilteredEntities";
public static final String gc_tempTabNameSpAffectedEntities = "SESSION.SpAffectedEntities";

public static final String gc_db2RegVarPsOid = "CURRENT CLIENT_APPLNAME";
public static final String gc_db2RegVarPsOidSafeSyntax = "'0' || " + M01_Globals_IVK.gc_db2RegVarPsOid;

public static final String gc_dirPrefixOrg = "MPC-";

public static final int gc_ftoConflictStateOpen = 1;
public static final int gc_ftoConflictStateResolved = 2;

public static final int gc_ftoConflictTypeNSr1 = 1;
public static final int gc_ftoConflictTypeGeneralPrice = 7;
public static final int gc_ftoConflictTypeCodeLabel = 3;
public static final int gc_ftoConflictTypeTypeLabel = 8;
public static final int gc_ftoConflictTypePlausibilityRule = 6;
public static final int gc_ftoConflictTypeCodePropertyAssignment = 4;
public static final int gc_ftoConflictTypeSlotPropertyAssignment = 5;

public static final int gc_langIdGerman = 1;
public static final int gc_langIdEnglish = 2;

public static final int gc_allowedCountriesMaxLength = 40;
public static final int gc_disallowedCountriesMaxLength = 200;

public static final String gc_anSuffixNat = "_national";
public static final String gc_asnSuffixNat = "N";
public static final String gc_anSuffixNatActivated = "_isNatActive";
public static final String gc_asnSuffixNatActivated = "ina";

public static int g_phaseIndexGaSyncSupport;
public static int g_phaseIndexArchive;
public static int g_phaseIndexPsTagging;
public static int g_phaseIndexXmlExport;
public static int g_phaseIndexUseCases;
public static int g_phaseIndexVirtAttr;
public static int g_phaseIndexGroupId;
public static int g_phaseIndexDataCompare;

public static int g_domainIndexChangeComment;
public static int g_domainIndexCodeNumber;
public static int g_domainIndexCountryIdList;
public static int g_domainIndexLrtLabel;
public static int g_domainIndexTmpPrio;
public static int g_domainIndexUserName;
public static int g_domainIndexXmlRecord;
public static int g_domainIndexBinaryPropertyValue;
public static int g_domainIndexTemplateFileData;
public static int g_domainIndexBIBRegistryValue;
public static int g_domainIndexLongText;
public static int g_domainIndexReportFileData;

public static int g_enumIndexPdmDataPoolType;
public static int g_enumIndexPdmOrganization;
public static int g_enumIndexStatus;
public static int g_enumIndexLanguage;

public static int g_classIndexFtoChangelogSummary;
public static int g_classIndexFtoOrgChangelogSummary;
public static int g_classIndexFtoOrgImplicitChangesSummary;
public static int g_classIndexSpAffectedEntity;
public static int g_classIndexSpFilteredEntity;
public static int g_classIndexSpStatement;
public static int g_classIndexActionHeading;
public static int g_classIndexActionElement;
public static int g_classIndexAggregationSlot;
public static int g_classIndexApplHistory;
public static int g_classIndexApplVersion;
public static int g_classIndexArchLog;
public static int g_classIndexCategory;
public static int g_classIndexCalculationRun;
public static int g_classIndexChangeLogStatus;
public static int g_classIndexClassIdPartitionBoundaries;
public static int g_classIndexCleanJobs;
public static int g_classIndexCodeBinaryPropertyAssignment;
public static int g_classIndexCodeBooleanPropertyAssignment;
public static int g_classIndexCodeNumericPropertyAssignment;
public static int g_classIndexCodePlausibilityRule;
public static int g_classIndexCodePriceAssignment;
public static int g_classIndexCodePropertyGroup;
public static int g_classIndexCodeTextPropertyAssignment;
public static int g_classIndexCodeType;
public static int g_classIndexConditionHeading;
public static int g_classIndexConflict;
public static int g_classIndexTypeConflict;
public static int g_classIndexGeneralPriceConflict;
public static int g_classIndexCodeLabelConflict;
public static int g_classIndexTypeLabelConflict;
public static int g_classIndexPlausibilityRuleConflict;
public static int g_classIndexCodePropertyAssignmentConflict;
public static int g_classIndexSlotPropertyAssignmentConflict;
public static int g_classIndexCountry;
public static int g_classIndexCountryContextAspect;
public static int g_classIndexCountryIdList;
public static int g_classIndexCountrySpec;
public static int g_classIndexCtsConfig;
public static int g_classIndexCtsConfigHistory;
public static int g_classIndexCtsConfigTemplate;
public static int g_classIndexDataComparison;
public static int g_classIndexDataComparisonAttribute;
public static int g_classIndexDataFix;
public static int g_classIndexDataFixHistory;
public static int g_classIndexDataFixIgnored;
public static int g_classIndexDataFixPrecondition;
public static int g_classIndexDataHistory;
public static int g_classIndexDdlFix;
public static int g_classIndexDdlFixIgnored;
public static int g_classIndexDivision;
public static int g_classIndexMessage;
public static int g_classIndexDocuNews;
public static int g_classIndexDocuNewsType;
public static int g_classIndexEndSlot;
public static int g_classIndexExpression;
public static int g_classIndexGeneralSettings;
public static int g_classIndexGenericAspect;
public static int g_classIndexGenericCode;
public static int g_classIndexJob;
public static int g_classIndexLanguageSequence;
public static int g_classIndexLanguageSequenceElement;
public static int g_classIndexMasterAggregationSlot;
public static int g_classIndexMasterEndSlot;
public static int g_classIndexNotice;
public static int g_classIndexNSr1Validity;
public static int g_classIndexNumericProperty;
public static int g_classIndexPaiMessageLog;
public static int g_classIndexPricePreferences;
public static int g_classIndexProductStructure;
public static int g_classIndexProperty;
public static int g_classIndexPropertyAssignment;
public static int g_classIndexPropertyTemplate;
public static int g_classIndexProtocolLineEntry;
public static int g_classIndexProtocolParameter;
public static int g_classIndexPsDbMapping;
public static int g_classIndexRebateDefault;
public static int g_classIndexRegistryDynamic;
public static int g_classIndexRegistryStatic;
public static int g_classIndexRel2ProdLock;
public static int g_classIndexRel2ProdLockHistory;
public static int g_classIndexRssHistory;
public static int g_classIndexRssStatus;
public static int g_classIndexSlotBinaryPropertyAssignment;
public static int g_classIndexSlotBooleanPropertyAssignment;
public static int g_classIndexSlotNumericPropertyAssignment;
public static int g_classIndexSlotPlausibilityRule;
public static int g_classIndexSlotTextPropertyAssignment;
public static int g_classIndexSolverData;
public static int g_classIndexSr0Validity;
public static int g_classIndexSr1Validity;
public static int g_classIndexStandardCode;
public static int g_classIndexStandardEquipment;
public static int g_classIndexTaxParameter;
public static int g_classIndexTaxType;
public static int g_classIndexTechDataDeltaImport;
public static int g_classIndexTerm;
public static int g_classIndexTypePriceAssignment;
public static int g_classIndexTypeSpec;
public static int g_classIndexTypeStandardEquipment;
public static int g_classIndexView;

public static int g_relIndexAggregationSlotHasNumericProperty;
public static int g_relIndexCategoryHasNumericProperty;
public static int g_relIndexCodeCategory;
public static int g_relIndexCountryGroupElement;
public static int g_relIndexCountryIdXRef;
public static int g_relIndexCpGroupHasProperty;
public static int g_relIndexSpGroupHasProperty;
public static int g_relIndexDisplaySlot;
public static int g_relIndexNsr1ValidForOrganization;
public static int g_relIndexCodeValidForOrganization;
public static int g_relIndexPropertyValidForOrganization;
public static int g_relIndexOrgManagesCountry;

public static int g_migDataPoolIndex;
public static int g_migDataPoolId;
public static int g_productiveDataPoolIndex;
public static int g_productiveDataPoolId;
public static int g_archiveDataPoolIndex;
public static int g_archiveDataPoolId;
public static int g_sim1DataPoolId;
public static int g_sim2DataPoolId;

public static String g_qualTabNameApplHistory;
public static String g_qualTabNameApplVersion;
public static String g_qualTabNameClassIdPartitionBoundaries;
public static String g_qualTabNameCleanJobs;
public static String g_qualTabNameCodeType;
public static String g_qualTabNameCountryIdList;
public static String g_qualTabNameCountrySpec;
public static String g_qualTabNameCtsConfig;
public static String g_qualTabNameCtsConfigTemplate;
public static String g_qualTabNameDataComparison;
public static String g_qualTabNameDataComparisonAttribute;
public static String g_qualTabNameDataFix;
public static String g_qualTabNameDataFixHistory;
public static String g_qualTabNameDataFixIgnored;
public static String g_qualTabNameDataFixPrecondition;
public static String g_qualTabNameDataHistory;
public static String g_qualTabNameDivision;
public static String g_qualTabNameMessage;
public static String g_qualTabNameLanguageSequence;
public static String g_qualTabNameLanguageSequenceElement;
public static String g_qualTabNamePaiMessageLog;
public static String g_qualTabNamePricePreferencesCto;
public static String g_qualTabNameProductStructure;
public static String g_qualTabNameProductStructureNl;
public static String g_qualTabNamePropertyTemplate;
public static String g_qualTabNamePropertyTemplateNl;
public static String g_qualTabNamePsDpMapping;
public static String g_qualTabNameRebateDefault;
public static String g_qualTabNameRegistryDynamic;
public static String g_qualTabNameRegistryStatic;
public static String g_qualTabNameRel2ProdLock;
public static String g_qualTabNameRel2ProdLockHistory;
public static String g_qualTabNameRssHistory;
public static String g_qualTabNameRssStatus;

public static String g_qualTabNameCountryIdXRef;
public static String g_qualTabNameOrgManagesCountry;

public static String g_dbtLockRequestorId;
public static String g_dbtR2pLockContext;
public static String g_dbtCodeNumber;
public static String g_dbtChangeComment;
public static String g_dbtLrtLabel;

public static String g_activePsOidDdl;

public static String g_anIsInUseByFto;
public static String g_anIsActive;
public static String g_anAllowedCountries;
public static String g_anDisAllowedCountries;
public static String g_anCodeNumber;
public static String g_anSlotType;
public static String g_anSr0Context;
public static String g_anIsCentralDataTransfer;
public static String g_anIsUnderConstruction;
public static String g_anChangeComment;
public static String g_anHasBeenSetProductive;
public static String g_anIsPsForming;
public static String g_anIsDeleted;
public static String g_anIsDeletable;
public static String g_anIsNotPublished;
public static String g_anIsStandard;
public static String g_anIsDefault;
public static String g_anIsDuplicating;
public static String g_anIsBlockedFactory;
public static String g_anIsBlockedPrice;
public static String g_anIsNational;
public static String g_anLrtComment;
public static String g_anPsOid;
public static String g_anDivOid;
public static String g_anValidFrom;
public static String g_anValidTo;
public static String g_anHasConflict;
public static String g_anSequenceNumber;
public static String g_anLockValueOld;
public static String g_anLockValueNew;
public static String g_anLockMode;
public static String g_anLockContext;
public static String g_anLockOperation;
public static String g_anLockTimestamp;
public static String g_anRuleScope;
public static String g_anRebateValueType;
public static String g_anSection;
public static String g_anKey;
public static String g_anSubKey;
public static String g_anValue;
public static String g_anMessageId;
public static String g_anName;
public static String g_anEventType;

public static String g_anAcmIsPs;
public static String g_anAcmIsPsForming;
public static String g_anAcmSupportXmlExport;
public static String g_anAcmUseXmlExport;
public static String g_anLdmIsMqt;

public static String g_anAcmLrtActivationType;
public static String g_anAcmDisplayCategory;
public static String g_anAcmIsArch;
public static String g_anAcmIsPriceRelated;
public static String g_anAcmUseFtoPostProcess;
public static String g_anAcmCondenseData;
public static String g_anAcmEntityFilterEnumCriteria;

public static String g_anConflictTypeId;
public static String g_anConflictStateId;

public static String g_qualFuncNameGetLrtTargetStatus;
public static String g_qualFuncNameIsNumeric;


public static void initGlobals_IVK() {
M01_Globals.g_fileNameIncrements =  new int[15];
M01_Globals.g_phaseIndexRegularTables = 1;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexRegularTables)] = M01_Common.phaseRegularTables;
M01_Globals.g_phaseIndexCoreSupport = 2;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexCoreSupport)] = M01_Common.phaseCoreSupport;
M01_Globals.g_phaseIndexModuleMeta = 3;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexModuleMeta)] = M01_Common.phaseModuleMeta;

M01_Globals_IVK.g_phaseIndexGaSyncSupport = 4;
M01_Globals.g_fileNameIncrements[(M01_Globals_IVK.g_phaseIndexGaSyncSupport)] = M01_Common.phaseGaSyncSupport;
M01_Globals.g_phaseIndexFksRelTabs = 5;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexFksRelTabs)] = M01_Common.phaseFksRelTabs;
M01_Globals.g_phaseIndexLrt = 6;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexLrt)] = M01_Common.phaseLrt;
M01_Globals.g_phaseIndexLrtViews = 7;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexLrtViews)] = M01_Common.phaseLrtViews;
M01_Globals.g_phaseIndexChangeLogViews = 8;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexChangeLogViews)] = M01_Common.phaseChangeLogViews;
M01_Globals.g_phaseIndexLrtSupport = 9;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexLrtSupport)] = M01_Common.phaseLrtSupport;
M01_Globals_IVK.g_phaseIndexArchive = 10;
M01_Globals.g_fileNameIncrements[(M01_Globals_IVK.g_phaseIndexArchive)] = M01_Common.phaseArchive;
M01_Globals_IVK.g_phaseIndexPsTagging = 11;
M01_Globals.g_fileNameIncrements[(M01_Globals_IVK.g_phaseIndexPsTagging)] = M01_Common.phasePsTagging;
M01_Globals_IVK.g_phaseIndexXmlExport = 12;
M01_Globals.g_fileNameIncrements[(M01_Globals_IVK.g_phaseIndexXmlExport)] = M01_Common.phaseXmlExport;
M01_Globals.g_phaseIndexDbSupport = 13;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexDbSupport)] = M01_Common.phaseDbSupport;
M01_Globals_IVK.g_phaseIndexUseCases = 14;
M01_Globals.g_fileNameIncrements[(M01_Globals_IVK.g_phaseIndexUseCases)] = M01_Common.phaseUseCases;
M01_Globals.g_phaseIndexAliases = 15;
M01_Globals.g_fileNameIncrements[(M01_Globals.g_phaseIndexAliases)] = M01_Common.phaseAliases;

M01_Globals_IVK.g_phaseIndexVirtAttr = M01_Globals.g_phaseIndexLrtSupport;
M01_Globals_IVK.g_phaseIndexGroupId = M01_Globals.g_phaseIndexLrtSupport;
M01_Globals_IVK.g_phaseIndexDataCompare = M01_Globals.g_phaseIndexDbSupport;
M01_Globals.g_phaseIndexLogChange = M01_Globals_IVK.g_phaseIndexPsTagging;

M01_Globals_IVK.g_domainIndexChangeComment = M25_Domain.getDomainIndexByName(M01_ACM.dxnChangeComment, M01_ACM.dnChangeComment, null);
M01_Globals_IVK.g_domainIndexCodeNumber = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnCodeNumber, M01_ACM_IVK.dnCodeNumber, null);
M01_Globals_IVK.g_domainIndexCountryIdList = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnCountryIdList, M01_ACM_IVK.dnCountryIdList, null);
M01_Globals_IVK.g_domainIndexLrtLabel = M25_Domain.getDomainIndexByName(M01_ACM.dxnLrtLabel, M01_ACM.dnLrtLabel, null);
M01_Globals_IVK.g_domainIndexTmpPrio = M25_Domain.getDomainIndexByName(M01_ACM.dxnSmallNumber, M01_ACM.dnSmallNumber, null);
M01_Globals_IVK.g_domainIndexUserName = M25_Domain.getDomainIndexByName(M01_ACM.dxnUserId, M01_ACM.dnUserName, null);
M01_Globals_IVK.g_domainIndexXmlRecord = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnXmlRecord, M01_ACM_IVK.dnXmlRecord, null);
M01_Globals_IVK.g_domainIndexBinaryPropertyValue = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnBinaryPropertyValue, M01_ACM_IVK.dnBinaryPropertyValue, null);
M01_Globals_IVK.g_domainIndexTemplateFileData = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnTemplateFileData, M01_ACM_IVK.dnTemplateFileData, null);
M01_Globals_IVK.g_domainIndexBIBRegistryValue = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnBIBRegistryValue, M01_ACM_IVK.dnBIBRegistryValue, null);
M01_Globals_IVK.g_domainIndexLongText = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnLongText, M01_ACM_IVK.dnLongText, null);
M01_Globals_IVK.g_domainIndexReportFileData = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnReportFileData, M01_ACM_IVK.dnReportFileData, null);

M01_Globals_IVK.g_enumIndexPdmDataPoolType = M21_Enum.getEnumIndexByName(M01_ACM_IVK.exnPdmDataPoolType, M01_ACM_IVK.enPdmDataPoolType, null);
M01_Globals_IVK.g_enumIndexPdmOrganization = M21_Enum.getEnumIndexByName(M01_ACM_IVK.exnPdmOrganization, M01_ACM_IVK.enPdmOrganization, null);
M01_Globals_IVK.g_enumIndexStatus = M21_Enum.getEnumIndexByName(M01_ACM_IVK.exnStatus, M01_ACM_IVK.enStatus, null);
M01_Globals_IVK.g_enumIndexLanguage = M21_Enum.getEnumIndexByName(M01_ACM.snCommon, M01_ACM.enLanguage, null);

M01_Globals_IVK.g_classIndexFtoChangelogSummary = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnFtoChangelogSummary, M01_ACM_IVK.clnFtoChangelogSummary, null);
M01_Globals_IVK.g_classIndexFtoOrgChangelogSummary = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnFtoOrgChangelogSummary, M01_ACM_IVK.clnFtoOrgChangelogSummary, null);
M01_Globals_IVK.g_classIndexFtoOrgImplicitChangesSummary = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnFtoOrgImplicitChangesSummary, M01_ACM_IVK.clnFtoOrgImplicitChangesSummary, null);
M01_Globals_IVK.g_classIndexSpAffectedEntity = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSpAffectedEntity, M01_ACM_IVK.clnSpAffectedEntity, null);
M01_Globals_IVK.g_classIndexSpFilteredEntity = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSpFilteredEntity, M01_ACM_IVK.clnSpFilteredEntity, null);
M01_Globals_IVK.g_classIndexSpStatement = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSpStatement, M01_ACM_IVK.clnSpStatement, null);
M01_Globals_IVK.g_classIndexActionHeading = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnActionHeading, M01_ACM_IVK.clnActionHeading, null);
M01_Globals_IVK.g_classIndexActionElement = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnActionElement, M01_ACM_IVK.clnActionElement, null);
M01_Globals_IVK.g_classIndexAggregationSlot = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnAggSlot, M01_ACM_IVK.clnAggSlot, null);
M01_Globals_IVK.g_classIndexApplHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnApplHistory, M01_ACM_IVK.clnApplHistory, null);
M01_Globals_IVK.g_classIndexApplVersion = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnApplVersion, M01_ACM_IVK.clnApplVersion, null);
M01_Globals_IVK.g_classIndexArchLog = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnArchLog, M01_ACM_IVK.clnArchLog, null);
M01_Globals_IVK.g_classIndexCalculationRun = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCalculationRun, M01_ACM_IVK.clnCalculationRun, null);
M01_Globals_IVK.g_classIndexCategory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCategory, M01_ACM_IVK.clnCategory, null);
M01_Globals_IVK.g_classIndexChangeLogStatus = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnChangeLogStatus, M01_ACM_IVK.clnChangeLogStatus, null);
M01_Globals_IVK.g_classIndexClassIdPartitionBoundaries = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnClassIdPartitionBoundaries, M01_ACM_IVK.clnClassIdPartitionBoundaries, null);
M01_Globals_IVK.g_classIndexCleanJobs = M22_Class.getClassIndexByName(M01_ACM.clxnCleanJobs, M01_ACM.clnCleanJobs, null);
M01_Globals_IVK.g_classIndexCodeBinaryPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeBinaryPropertyAssignment, M01_ACM_IVK.clnCodeBinaryPropertyAssignment, null);
M01_Globals_IVK.g_classIndexCodeBooleanPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeBooleanPropertyAssignment, M01_ACM_IVK.clnCodeBooleanPropertyAssignment, null);
M01_Globals_IVK.g_classIndexCodeNumericPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeNumericPropertyAssignment, M01_ACM_IVK.clnCodeNumericPropertyAssignment, null);
M01_Globals_IVK.g_classIndexCodePlausibilityRule = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodePlausibilityRule, M01_ACM_IVK.clnCodePlausibilityRule, null);
M01_Globals_IVK.g_classIndexCodePriceAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodePriceAssignment, M01_ACM_IVK.clnCodePriceAssignment, null);
M01_Globals_IVK.g_classIndexCodePropertyGroup = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodePropertyGroup, M01_ACM_IVK.clnCodePropertyGroup, null);
M01_Globals_IVK.g_classIndexCodeTextPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeTextPropertyAssignment, M01_ACM_IVK.clnCodeTextPropertyAssignment, null);
M01_Globals_IVK.g_classIndexCodeType = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeType, M01_ACM_IVK.clnCodeType, null);
M01_Globals_IVK.g_classIndexConditionHeading = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnConditionHeading, M01_ACM_IVK.clnConditionHeading, null);
M01_Globals_IVK.g_classIndexConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnConflict, M01_ACM_IVK.clnConflict, null);
M01_Globals_IVK.g_classIndexTypeConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTypeConflict, M01_ACM_IVK.clnTypeConflict, null);
M01_Globals_IVK.g_classIndexGeneralPriceConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGeneralPriceConflict, M01_ACM_IVK.clnGeneralPriceConflict, null);
M01_Globals_IVK.g_classIndexCodeLabelConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodeLabelConflict, M01_ACM_IVK.clnCodeLabelConflict, null);
M01_Globals_IVK.g_classIndexTypeLabelConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTypeLabelConflict, M01_ACM_IVK.clnTypeLabelConflict, null);
M01_Globals_IVK.g_classIndexPlausibilityRuleConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPlausibilityRuleConflict, M01_ACM_IVK.clnPlausibilityRuleConflict, null);
M01_Globals_IVK.g_classIndexCodePropertyAssignmentConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCodePropertyAssignmentConflict, M01_ACM_IVK.clnCodePropertyAssignmentConflict, null);
M01_Globals_IVK.g_classIndexSlotPropertyAssignmentConflict = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotPropertyAssignmentConflict, M01_ACM_IVK.clnSlotPropertyAssignmentConflict, null);
M01_Globals_IVK.g_classIndexCountry = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCountry, M01_ACM_IVK.clnCountry, null);
M01_Globals_IVK.g_classIndexCountryContextAspect = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCountryContextAspect, M01_ACM_IVK.clnCountryContextAspect, null);
M01_Globals_IVK.g_classIndexCountryIdList = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCountryIdList, M01_ACM_IVK.clnCountryIdList, null);
M01_Globals_IVK.g_classIndexCountrySpec = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCountrySpec, M01_ACM_IVK.clnCountrySpec, null);
M01_Globals_IVK.g_classIndexCtsConfig = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCtsConfig, M01_ACM_IVK.clnCtsConfig, null);
M01_Globals_IVK.g_classIndexCtsConfigHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCtsConfigHistory, M01_ACM_IVK.clnCtsConfigHistory, null);
M01_Globals_IVK.g_classIndexCtsConfigTemplate = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnCtsConfigTemplate, M01_ACM_IVK.clnCtsConfigTemplate, null);
if (M03_Config.supportSstCheck) {
M01_Globals_IVK.g_classIndexDataComparison = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataComparison, M01_ACM_IVK.clnDataComparison, null);
M01_Globals_IVK.g_classIndexDataComparisonAttribute = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataComparisonAttribute, M01_ACM_IVK.clnDataComparisonAttribute, null);
}
M01_Globals_IVK.g_classIndexDataFix = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataFix, M01_ACM_IVK.clnDataFix, null);
M01_Globals_IVK.g_classIndexDataFixHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataFixHistory, M01_ACM_IVK.clnDataFixHistory, null);
M01_Globals_IVK.g_classIndexDataFixIgnored = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataFixIgnored, M01_ACM_IVK.clnDataFixIgnored, null);
M01_Globals_IVK.g_classIndexDataFixPrecondition = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataFixPrecondition, M01_ACM_IVK.clnDataFixPrecondition, null);
M01_Globals_IVK.g_classIndexDataHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDataHistory, M01_ACM_IVK.clnDataHistory, null);
M01_Globals_IVK.g_classIndexDdlFix = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDdlFix, M01_ACM_IVK.clnDdlFix, null);
M01_Globals_IVK.g_classIndexDdlFixIgnored = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDdlFixIgnored, M01_ACM_IVK.clnDdlFixIgnored, null);
M01_Globals_IVK.g_classIndexDivision = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDivision, M01_ACM_IVK.clnDivision, null);
M01_Globals_IVK.g_classIndexMessage = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnMessage, M01_ACM_IVK.clnMessage, null);
M01_Globals_IVK.g_classIndexDocuNews = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDocuNews, M01_ACM_IVK.clnDocuNews, null);
M01_Globals_IVK.g_classIndexDocuNewsType = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnDocuNewsType, M01_ACM_IVK.clnDocuNewsType, null);
M01_Globals_IVK.g_classIndexEndSlot = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnEndSlot, M01_ACM_IVK.clnEndSlot, null);
M01_Globals_IVK.g_classIndexExpression = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnExpression, M01_ACM_IVK.clnExpression, null);
M01_Globals_IVK.g_classIndexGeneralSettings = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGeneralSettings, M01_ACM_IVK.clnGeneralSettings, null);
M01_Globals_IVK.g_classIndexGenericAspect = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGenericAspect, M01_ACM_IVK.clnGenericAspect, null);
M01_Globals_IVK.g_classIndexGenericCode = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGenericCode, M01_ACM_IVK.clnGenericCode, null);
M01_Globals_IVK.g_classIndexJob = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnJob, M01_ACM_IVK.clnJob, null);
M01_Globals_IVK.g_classIndexLanguageSequence = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnLanguageSequence, M01_ACM_IVK.clnLanguageSequence, null);
M01_Globals_IVK.g_classIndexLanguageSequenceElement = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnLanguageSequenceElement, M01_ACM_IVK.clnLanguageSequenceElement, null);
M01_Globals_IVK.g_classIndexMasterAggregationSlot = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnMasterAggSlot, M01_ACM_IVK.clnMasterAggSlot, null);
M01_Globals_IVK.g_classIndexMasterEndSlot = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnMasterEndSlot, M01_ACM_IVK.clnMasterEndSlot, null);
M01_Globals_IVK.g_classIndexNotice = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnNotice, M01_ACM_IVK.clnNotice, null);
M01_Globals_IVK.g_classIndexNSr1Validity = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnNSr1Validity, M01_ACM_IVK.clnNSr1Validity, null);
M01_Globals_IVK.g_classIndexNumericProperty = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnNumericProperty, M01_ACM_IVK.clnNumericProperty, null);
M01_Globals_IVK.g_classIndexPaiMessageLog = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPaiMessageLog, M01_ACM_IVK.clnPaiMessageLog, null);
M01_Globals_IVK.g_classIndexPricePreferences = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPricePreferences, M01_ACM_IVK.clnPricePreferences, null);
M01_Globals_IVK.g_classIndexProductStructure = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnProductStructure, M01_ACM_IVK.clnProductStructure, null);
M01_Globals_IVK.g_classIndexProperty = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnProperty, M01_ACM_IVK.clnProperty, null);
M01_Globals_IVK.g_classIndexPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPropertyAssignment, M01_ACM_IVK.clnPropertyAssignment, null);
M01_Globals_IVK.g_classIndexPropertyTemplate = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPropertyTemplate, M01_ACM_IVK.clnPropertyTemplate, null);
M01_Globals_IVK.g_classIndexProtocolLineEntry = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnProtocolLineEntry, M01_ACM_IVK.clnProtocolLineEntry, null);
M01_Globals_IVK.g_classIndexProtocolParameter = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnProtocolParameter, M01_ACM_IVK.clnProtocolParameter, null);
M01_Globals_IVK.g_classIndexPsDbMapping = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnPsDbMapping, M01_ACM_IVK.clnPsDbMapping, null);
M01_Globals_IVK.g_classIndexRebateDefault = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRebateDefault, M01_ACM_IVK.clnRebateDefault, null);
M01_Globals_IVK.g_classIndexRegistryDynamic = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRegistryDynamic, M01_ACM_IVK.clnRegistryDynamic, null);
M01_Globals_IVK.g_classIndexRegistryStatic = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRegistryStatic, M01_ACM_IVK.clnRegistryStatic, null);
M01_Globals_IVK.g_classIndexRel2ProdLock = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRel2ProdLock, M01_ACM_IVK.clnRel2ProdLock, null);
M01_Globals_IVK.g_classIndexRel2ProdLockHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRel2ProdLockHistory, M01_ACM_IVK.clnRel2ProdLockHistory, null);
M01_Globals_IVK.g_classIndexRssHistory = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRssHistory, M01_ACM_IVK.clnRssHistory, null);
M01_Globals_IVK.g_classIndexRssStatus = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnRssStatus, M01_ACM_IVK.clnRssStatus, null);
M01_Globals_IVK.g_classIndexSlotBinaryPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotBinaryPropertyAssignment, M01_ACM_IVK.clnSlotBinaryPropertyAssignment, null);
M01_Globals_IVK.g_classIndexSlotBooleanPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotBooleanPropertyAssignment, M01_ACM_IVK.clnSlotBooleanPropertyAssignment, null);
M01_Globals_IVK.g_classIndexSlotNumericPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotNumericPropertyAssignment, M01_ACM_IVK.clnSlotNumericPropertyAssignment, null);
M01_Globals_IVK.g_classIndexSlotPlausibilityRule = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotPlausibilityRule, M01_ACM_IVK.clnSlotPlausibilityRule, null);
M01_Globals_IVK.g_classIndexSlotTextPropertyAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSlotTextPropertyAssignment, M01_ACM_IVK.clnSlotTextPropertyAssignment, null);
M01_Globals_IVK.g_classIndexSolverData = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSolverData, M01_ACM_IVK.clnSolverData, null);
M01_Globals_IVK.g_classIndexSr0Validity = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSr0Validity, M01_ACM_IVK.clnSr0Validity, null);
M01_Globals_IVK.g_classIndexSr1Validity = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSr1Validity, M01_ACM_IVK.clnSr1Validity, null);
M01_Globals_IVK.g_classIndexStandardCode = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnStandardCode, M01_ACM_IVK.clnStandardCode, null);
M01_Globals_IVK.g_classIndexStandardEquipment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnStandardEquipment, M01_ACM_IVK.clnStandardEquipment, null);
M01_Globals_IVK.g_classIndexTaxParameter = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTaxParameter, M01_ACM_IVK.clnTaxParameter, null);
M01_Globals_IVK.g_classIndexTaxType = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTaxType, M01_ACM_IVK.clnTaxType, null);
M01_Globals_IVK.g_classIndexTechDataDeltaImport = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTechDataDeltaImport, M01_ACM_IVK.clnTechDataDeltaImport, null);
M01_Globals_IVK.g_classIndexTerm = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTerm, M01_ACM_IVK.clnTerm, null);
M01_Globals_IVK.g_classIndexTypePriceAssignment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTypePriceAssignment, M01_ACM_IVK.clnTypePriceAssignment, null);
M01_Globals_IVK.g_classIndexTypeSpec = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTypeSpec, M01_ACM_IVK.clnTypeSpec, null);
M01_Globals_IVK.g_classIndexTypeStandardEquipment = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnTypeSpec, M01_ACM_IVK.clnTypeStandardEquipment, null);
M01_Globals_IVK.g_classIndexView = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnView, M01_ACM_IVK.clnView, null);

if (M03_Config.snapshotApiVersion.substring(0, 1) == "8") {
M01_Globals.g_classIndexSnapshotAppl = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSnapshotV8Appl, M01_ACM_IVK.clnSnapshotV8Appl, null);
M01_Globals.g_classIndexSnapshotApplInfo = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSnapshotV8ApplInfo, M01_ACM_IVK.clnSnapshotV8ApplInfo, null);
M01_Globals.g_classIndexSnapshotLock = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSnapshotV8Lock, M01_ACM_IVK.clnSnapshotV8Lock, null);
M01_Globals.g_classIndexSnapshotLockWait = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSnapshotV8LockWait, M01_ACM_IVK.clnSnapshotV8LockWait, null);
M01_Globals.g_classIndexSnapshotStatement = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnSnapshotV8Statement, M01_ACM_IVK.clnSnapshotV8Statement, null);
} else if (M03_Config.snapshotApiVersion.compareTo("9.7") == 0) {
M01_Globals.g_classIndexSnapshotAppl = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotV9Appl, M01_ACM.clnSnapshotV9Appl, null);
M01_Globals.g_classIndexSnapshotApplInfo = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotV9ApplInfo, M01_ACM.clnSnapshotV9ApplInfo, null);
M01_Globals.g_classIndexSnapshotLock = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotV9Lock, M01_ACM.clnSnapshotV9Lock, null);
M01_Globals.g_classIndexSnapshotLockWait = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotV9LockWait, M01_ACM.clnSnapshotV9LockWait, null);
M01_Globals.g_classIndexSnapshotStatement = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotV9Statement, M01_ACM.clnSnapshotV9Statement, null);
}

M01_Globals_IVK.g_relIndexAggregationSlotHasNumericProperty = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnAggregationSlotHasNumericProperty, M01_ACM_IVK.rnAggregationSlotHasNumericProperty, null);
M01_Globals_IVK.g_relIndexCategoryHasNumericProperty = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCategoryHasNumericProperty, M01_ACM_IVK.rnCategoryHasNumericProperty, null);
M01_Globals_IVK.g_relIndexCodeCategory = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCodeCategory, M01_ACM_IVK.rnCodeCategory, null);
M01_Globals_IVK.g_relIndexCpGroupHasProperty = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCpGroupHasProperty, M01_ACM_IVK.rnCpGroupHasProperty, null);
M01_Globals_IVK.g_relIndexSpGroupHasProperty = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnSpGroupHasProperty, M01_ACM_IVK.rnSpGroupHasProperty, null);
M01_Globals_IVK.g_relIndexDisplaySlot = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnDisplaySlot, M01_ACM_IVK.rnDisplaySlot, null);
M01_Globals_IVK.g_relIndexCountryGroupElement = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCountryGroupElement, M01_ACM_IVK.rnCountryGroupElement, null);
M01_Globals_IVK.g_relIndexCountryIdXRef = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCountryIdXRef, M01_ACM_IVK.rnCountryIdXRef, null);
M01_Globals_IVK.g_relIndexCodeValidForOrganization = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnCodeValidForOrganization, M01_ACM_IVK.rnCodeValidForOrganization, null);
M01_Globals_IVK.g_relIndexNsr1ValidForOrganization = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnNsr1ValidForOrganization, M01_ACM_IVK.rnNsr1ValidForOrganization, null);
M01_Globals_IVK.g_relIndexPropertyValidForOrganization = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnPropertyValidForOrganization, M01_ACM_IVK.rnPropertyValidForOrganization, null);
M01_Globals_IVK.g_relIndexOrgManagesCountry = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnOrgManagesCountry, M01_ACM_IVK.rnOrgManagesCountry, null);

M01_Globals_IVK.g_dbtLockRequestorId = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexLockRequestorId, null);
M01_Globals_IVK.g_dbtR2pLockContext = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexR2pLockContext, null);
M01_Globals_IVK.g_dbtCodeNumber = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals_IVK.g_domainIndexCodeNumber, null);
M01_Globals_IVK.g_dbtChangeComment = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals_IVK.g_domainIndexChangeComment, null);
M01_Globals_IVK.g_dbtLrtLabel = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals_IVK.g_domainIndexLrtLabel, null);

M01_Globals_IVK.g_activePsOidDdl = M01_Globals.g_dbtOid + "(" + M01_Globals_IVK.gc_db2RegVarPsOidSafeSyntax + ")";

M01_Globals_IVK.g_migDataPoolIndex = M72_DataPool_Utilities.getMigDataPoolIndex();
M01_Globals_IVK.g_migDataPoolId = M72_DataPool_Utilities.getMigDataPoolId();
M01_Globals_IVK.g_productiveDataPoolIndex = M72_DataPool_Utilities.getProductiveDataPoolIndex();
M01_Globals_IVK.g_productiveDataPoolId = M72_DataPool_Utilities.getProductiveDataPoolId();
M01_Globals_IVK.g_archiveDataPoolIndex = M72_DataPool_Utilities.getArchiveDataPoolIndex();
M01_Globals_IVK.g_archiveDataPoolId = M72_DataPool_Utilities.getArchiveDataPoolId();
//FIXME: get rid of hard-coding
M01_Globals_IVK.g_sim1DataPoolId = 5;
M01_Globals_IVK.g_sim2DataPoolId = 6;
}


public static void initGlobalsByDdl_IVK(Integer ddlType) {
M01_Globals.g_qualTabNameDataPoolAccessMode = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals.g_enumIndexDataPoolAccessMode, ddlType, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmDataPoolType = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexPdmDataPoolType, ddlType, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmOrganization = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexPdmOrganization, ddlType, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmOrganizationNl = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexPdmOrganization, ddlType, null, null, true, null, null);
M01_Globals.g_qualTabNameStatus = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexStatus, ddlType, null, null, null, null, null);
M01_Globals.g_qualTabNameLanguage = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexLanguage, ddlType, null, null, null, null, null);

M01_Globals_IVK.g_qualTabNameApplHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexApplHistory, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameApplVersion = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexApplVersion, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameClassIdPartitionBoundaries = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexClassIdPartitionBoundaries, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCleanJobs = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCleanJobs, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCodeType = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCodeType, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCountryIdList = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountryIdList, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCountrySpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountrySpec, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCtsConfig = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCtsConfig, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameCtsConfigTemplate = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCtsConfigTemplate, ddlType, null, null, null, null, null, null, null, null, null);
if (M03_Config.supportSstCheck) {
M01_Globals_IVK.g_qualTabNameDataComparison = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataComparison, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDataComparisonAttribute = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataComparisonAttribute, ddlType, null, null, null, null, null, null, null, null, null);
}
M01_Globals_IVK.g_qualTabNameDataFix = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataFix, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDataFixHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataFixHistory, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDataFixIgnored = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataFixIgnored, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDataFixPrecondition = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataFixPrecondition, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDataHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDataHistory, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameDivision = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDivision, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameMessage = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexMessage, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameLanguageSequence = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexLanguageSequence, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameLanguageSequenceElement = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexLanguageSequenceElement, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNamePaiMessageLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPaiMessageLog, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNamePricePreferencesCto = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameProductStructure = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProductStructure, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameProductStructureNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProductStructure, ddlType, null, null, null, null, null, true, null, null, null);
M01_Globals_IVK.g_qualTabNamePropertyTemplate = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNamePropertyTemplateNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, null, null, null, null, null, true, null, null, null);
M01_Globals_IVK.g_qualTabNamePsDpMapping = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPsDbMapping, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRebateDefault = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRebateDefault, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRegistryDynamic = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRegistryDynamic, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRegistryStatic = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRegistryStatic, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRel2ProdLock = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRel2ProdLock, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRel2ProdLockHistory, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRssHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRssHistory, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameRssStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexRssStatus, ddlType, null, null, null, null, null, null, null, null, null);

M01_Globals_IVK.g_qualTabNameCountryIdXRef = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryIdXRef, ddlType, null, null, null, null, null, null, null, null);
M01_Globals_IVK.g_qualTabNameOrgManagesCountry = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexOrgManagesCountry, ddlType, null, null, null, null, null, null, null, null);

M01_Globals_IVK.g_anIsInUseByFto = M04_Utilities.genAttrName(M01_ACM_IVK.conIsInUseByFto, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsActive = M04_Utilities.genAttrName(M01_ACM_IVK.conIsActive, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAllowedCountries = M04_Utilities.genAttrName(M01_ACM_IVK.conAllowedCountries, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anDisAllowedCountries = M04_Utilities.genAttrName(M01_ACM_IVK.conDisAllowedCountries, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anCodeNumber = M04_Utilities.genAttrName(M01_ACM_IVK.conCodeNumber, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anSlotType = M04_Utilities.genAttrName(M01_ACM_IVK.conSlotTypeId, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anSr0Context = M04_Utilities.genAttrName(M01_ACM_IVK.conSr0Context, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsCentralDataTransfer = M04_Utilities.genAttrName(M01_ACM_IVK.conIsCentralDataTransfer, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsUnderConstruction = M04_Utilities.genAttrName(M01_ACM_IVK.conIsUnderConstruction, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anChangeComment = M04_Utilities.genAttrName(M01_ACM.conChangeComment, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anHasBeenSetProductive = M04_Utilities.genAttrName(M01_ACM_IVK.conHasBeenSetProductive, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsPsForming = M04_Utilities.genAttrName(M01_ACM_IVK.conIsPsForming, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsDeleted = M04_Utilities.genAttrName(M01_ACM_IVK.conIsDeleted, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsDeletable = M04_Utilities.genAttrName(M01_ACM_IVK.conIsDeletable, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsNotPublished = M04_Utilities.genAttrName(M01_ACM_IVK.conIsNotPublished, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsStandard = M04_Utilities.genAttrName(M01_ACM_IVK.conIsStandard, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsDefault = M04_Utilities.genAttrName(M01_ACM_IVK.conIsDefault, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsDuplicating = M04_Utilities.genAttrName(M01_ACM_IVK.conIsDuplicating, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsBlockedFactory = M04_Utilities.genAttrName(M01_ACM_IVK.conIsBlockedFactory, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsBlockedPrice = M04_Utilities.genAttrName(M01_ACM_IVK.conIsBlockedPrice, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anIsNational = M04_Utilities.genAttrName(M01_ACM_IVK.conIsNational, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLrtComment = M04_Utilities.genAttrName(M01_ACM.conLrtComment, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anPsOid = M04_Utilities.genAttrName(M01_ACM_IVK.conPsOid, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anDivOid = M04_Utilities.genAttrName(M01_ACM_IVK.conDivOid, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anValidFrom = M04_Utilities.genAttrName(M01_ACM.conValidFrom, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anValidTo = M04_Utilities.genAttrName(M01_ACM.conValidTo, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anHasConflict = M04_Utilities.genAttrName(M01_ACM_IVK.conHasConflict, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anSequenceNumber = M04_Utilities.genAttrName(M01_ACM_IVK.conSequenceNumber, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockValueOld = M04_Utilities.genAttrName(M01_ACM_IVK.conLockValueOld, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockValueNew = M04_Utilities.genAttrName(M01_ACM_IVK.conLockValueNew, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockMode = M04_Utilities.genAttrName(M01_ACM_IVK.conLockMode, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockContext = M04_Utilities.genAttrName(M01_ACM_IVK.conLockContext, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockOperation = M04_Utilities.genAttrName(M01_ACM_IVK.conLockOperation, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLockTimestamp = M04_Utilities.genAttrName(M01_ACM_IVK.conLockTimestamp, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anRuleScope = M04_Utilities.genAttrName(M01_ACM_IVK.conRuleScopeId, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anRebateValueType = M04_Utilities.genAttrName(M01_ACM_IVK.conRebateValueType, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anSection = M04_Utilities.genAttrName(M01_ACM_IVK.conSection, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anKey = M04_Utilities.genAttrName(M01_ACM_IVK.conKey, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anSubKey = M04_Utilities.genAttrName(M01_ACM_IVK.conSubKey, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anValue = M04_Utilities.genAttrName(M01_ACM_IVK.conValue, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anMessageId = M04_Utilities.genAttrName(M01_ACM_IVK.conMessageId, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anName = M04_Utilities.genAttrName(M01_ACM_IVK.conName, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anEventType = M04_Utilities.genAttrName(M01_ACM_IVK.conEventType, ddlType, null, null, null, null, null, null);

M01_Globals_IVK.g_anAcmIsPs = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmIsPs, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmIsPsForming = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmIsPsForming, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmSupportXmlExport = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmSupportXmlExport, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmUseXmlExport = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmUseXmlExport, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsNt2m = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmIsNt2m, ddlType, null, null, null, null, null, null);

M01_Globals_IVK.g_anAcmLrtActivationType = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmLrtActivationType, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmDisplayCategory = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmDisplayCategory, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmIsArch = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmIsArch, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmIsPriceRelated = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmIsPriceRelated, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmUseFtoPostProcess = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmUseFtoPostProcess, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmCondenseData = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmCondenseData, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anAcmEntityFilterEnumCriteria = M04_Utilities.genAttrName(M01_ACM_IVK.conAcmEntityFilterEnumCriteria, ddlType, null, null, null, null, null, null);

M01_Globals_IVK.g_anConflictTypeId = M04_Utilities.genAttrName(M01_ACM_IVK.conConflictTypeId, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anConflictStateId = M04_Utilities.genAttrName(M01_ACM_IVK.conConflictStateId, ddlType, null, null, null, null, null, null);

M01_Globals.g_qualFuncNameStrElems = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnStrElems, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexLrt, M01_ACM.udfnGetLrtTargetStatus, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_qualFuncNameIsNumeric = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnIsNumeric, ddlType, null, null, null, null, null, true);
}
// ### ENDIF IVK ###








}