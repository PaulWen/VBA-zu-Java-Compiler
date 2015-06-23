package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_ACM_IVK {


// ### IF IVK ###

// ############################################
// # section meta information
// ############################################

// sn   - section name
// ssn  - section short name

public static final String snAspect = "Aspect";
public static final String ssnAspect = "ASP";
public static final String snDecision = "Decision";
public static final String ssnDecision = "DEC";
public static final String snExpression = "Expression";
public static final String ssnExpression = "EXP";
public static final String snFactoryTakeover = "FactoryTakeover";
public static final String ssnFactoryTakeover = "FTO";
public static final String snMessage = "Message";
public static final String ssnMessage = "ERR";
public static final String snProtocol = "Protocol";
public static final String ssnProtocol = "PRO";
public static final String snCode = "Code";
public static final String ssnCode = "CDE";
public static final String snDataCheck = "DataCheck";
public static final String ssnDataCheck = "CHK";
public static final String snAliasPsDpFiltered = "ALIAS_LRT_PDF";
public static final String ssnAliasPsDpFiltered = "ALI";
public static final String snAliasPsDpFilteredExtended = "ALIAS_LRT_PDFX";
public static final String ssnAliasPsDpFilteredExtended = "ALJ";
public static final String snAliasDelObj = "ALIAS_LRT_DEL";
public static final String ssnAliasDelObj = "ALD";
public static final String snProductStructure = "PS";
public static final String ssnProductStructure = "PST";
public static final String snStaging = "Staging";
public static final String ssnStaging = "STA";
public static final String snOrder = "Order";
public static final String ssnOrder = "ORD";
public static final String snPaiLog = "PaiLog";
public static final String ssnPaiLog = "PLG";
public static final String snPricing = "Pricing";
public static final String ssnPricing = "PRI";
public static final String snReport = "Report";
public static final String ssnReport = "REP";
public static final String snDataFix = "DataFix";
public static final String ssnDataFix = "DFX";
public static final String snSetProductive = "SetProductive";
public static final String ssnSetProductive = "SPR";

public static final String snFwkTest = "FwkTest";

// ############################################
// # domain meta information
// ############################################

// dn   - domain name
// dsn  - domain short name

public static final String dnBIBRegistryValue = "BIBRegistryValue";
public static final String dxnBIBRegistryValue = M01_ACM_IVK.snStaging;
public static final String dnBinaryPropertyValue = "BinaryPropertyValue";
public static final String dxnBinaryPropertyValue = M01_ACM_IVK.snProductStructure;
public static final String dnCodeNumber = "CodeNumber";
public static final String dxnCodeNumber = M01_ACM_IVK.snCode;
public static final String dnCountryIdList = "CountryIdList";
public static final String dxnCountryIdList = "Country";
public static final String dnEndSlotLabel = "EndSlotLabel";
public static final String dxnEndSlotLabel = M01_ACM_IVK.snProductStructure;
public static final String dnLockRequestorId = "LockRequestorId";
public static final String dxnLockRequestorId = M01_ACM.snMeta;
public static final String dnLongText = "LongText";
public static final String dxnLongText = M01_ACM_IVK.snStaging;
public static final String dnR2pLockContext = "Rel2ProdLockContext";
public static final String dxnR2pLockContext = M01_ACM.snMeta;
public static final String dnReportFileData = "ReportFileData";
public static final String dxnReportFileData = M01_ACM_IVK.snReport;
public static final String dnTemplateFileData = "TemplateFileData";
public static final String dxnTemplateFileData = M01_ACM_IVK.snReport;
public static final String dnValTimestamp = "ValidityTimestamp";
public static final String dxnValTimestamp = M01_ACM.snCommon;
public static final String dnXmlRecord = "XmlCLob";
public static final String dxnXmlRecord = M01_ACM.snCommon;

// ############################################
// # column meta information
// ############################################

// con   - column name
// cosn  - column short name

public static final String conAllowedCountries = "allowedCountries";
public static final String conDisAllowedCountries = "disallowedCountries";
public static final String conIsActive = "isActive";
public static final String conIsInUseByFto = "isInUseByFto";
public static final String conIsInvalid = "isInvalid";
public static final String conPsOid = "PS_OID";
public static final String cosnPsOid = "poi";
public static final String conDivOid = "DIV_OID";
public static final String cosnDivOid = "doi";
public static final String conIsCentralDataTransfer = "isCentralDataTransfer";
public static final String conIsUnderConstruction = "isUnderConstruction";
public static final String conHasConflict = "hasConflict";
public static final String conIsDeletable = "isDeletable";
public static final String conIsStandard = "isStandard";
public static final String conIsDefault = "isDefault";
public static final String conIsDuplicating = "isDuplicating";
public static final String conIsBlockedFactory = "isBlockedFactory";
public static final String conIsBlockedPrice = "isBlockedPrice";
public static final String conPdmOrgIsPrimary = "isFactory";
public static final String conXmlRecord = "record";
public static final String cosnXmlRecord = "rec";
public static final String conIsNational = "isNational";
public static final String cosnIsNational = "ina";
public static final String conIsNationalActive = "isNatActive";
public static final String cosnIsNationalActive = "ina";
public static final String conIsRebateEnabled = "isRebateEnabled";
public static final String conIsBlockedNational = "isBlockedNational";
public static final String conNotVisibleFactory = "notVisibleFactory";
public static final String conNotVisibleNational = "notVisibleNational";
public static final String conIsCommissionDeductible = "isCommissionDeductible";
public static final String conDisplayOrder = "displayOrder";
public static final String conIsPsForming = "isPsForming";
public static final String conIsLinked = "isLinked";
public static final String conIsBaseSlot = "isBaseSlot";
public static final String conSr0Context = "sr0Context";
public static final String conHasBeenSetProductive = "hasBeenSetProductive";
public static final String cosnHasBeenSetProductive = "spr";
public static final String conIsDeleted = "isDeleted";
public static final String cosnIsDeleted = "ide";
public static final String conNationalDisabled = "nationalDisabled";
public static final String conIsNotPublished = "isNotPublished";
public static final String conIsCabin = "isCabin";
public static final String conIsOrderField1 = "isOrderField1";
public static final String conIsOrderField2 = "isOrderField2";
public static final String conIsOrderField3 = "isOrderField3";
public static final String conIsOrderField4 = "isOrderField4";
public static final String conIsOrderField5 = "isOrderField5";
public static final String conIsAEF = "isAEF";
public static final String conCodeNumber = "codeNumber";
public static final String conIsMassUpdate = "isMassUpdate";
public static final String conPaiEntitlementGroupId = "paiEntitlementGroupId";
public static final String conIsReportingSchema = "isReportingSchema";
public static final String conIsMpc = "isMpc";
public static final String conIsDcVd = "isDcVd";
public static final String conSequenceNumber = "sequenceNumber";
public static final String conLockValueOld = "lockValueOld";
public static final String conLockValueNew = "lockValueNew";
public static final String conLockMode = "lockMode";
public static final String conLockContext = "lockContext";
public static final String conLockTimestamp = "lockTimestamp";
public static final String conLockOperation = "lockOperation";
public static final String conCurrencyFactor = "currencyFactor";
public static final String conCurrency = "currency";
public static final String conRebateValueType = "rebateValueType";
public static final String conRebateValueCode = "rebateValueCode";
public static final String conServiceType = "serviceType";
public static final String conSection = "section";
public static final String conSubKey = "subKey";
public static final String conKey = "key";
public static final String conValue = "value";
public static final String conContent = "content";
public static final String conFileName = "fileName";
public static final String conComment = "comment";
public static final String conDpClassNumber = "dpClassNumber";
public static final String conDocumentationFlagFactory = "documentationFlagFactory";
public static final String conBaumusterGroup = "baumusterGroup";
public static final String conOrderAcceptanceFrom = "orderAcceptanceFrom";
public static final String conOrderAcceptanceUntil = "orderAcceptanceUntil";
public static final String conMessageId = "messageId";
public static final String conName = "name";
public static final String conLabel = "label";
public static final String conLabelNational = "label_national";
public static final String conLabelIsNatActive = "label_isNatActive";
public static final String conEventType = "eventType";

public static final String conAbhCode = "abhCode";
public static final String conAssignedPaintZoneKey = "assignedPaintZoneKey";
public static final String conCardinality = "cardinality";
public static final String conCity = "city";
public static final String conClassification = "classification";
public static final String conClientAcctng = "client_acctng";
public static final String conClientApplName = "client_applname";
public static final String conClientUserId = "client_userid";
public static final String conClientWrkstnName = "client_wrkstnname";

public static final String conCodePriority = "codePriority";
public static final String conContactPerson = "contactPerson";
public static final String conPdmDeletedObjectSchemaName = "deletedObjectSchemaName";
public static final String conDigitsAfterDecimalPoint = "digitsAfterDecimalPoint";
public static final String conEMail = "eMail";
public static final String conFax = "fax";
public static final String conFon = "fon";
public static final String conId = "id";
public static final String conIsEstimationRelevant = "isEstimationRelevant";
public static final String conIsMotorVehicleCertificationRelevant = "isMotorVehicleCertificationRel";
public static final String conIsNsr1Slot = "isNsr1Slot";
public static final String conIsolation = "isolation";
public static final String conIsProductionRelevant = "isProductionRelevant";
public static final String conIsRequired = "isRequired";
public static final String conIsSideCosts = "isSideCosts";
public static final String conIsSr0Slot = "isSr0Slot";
public static final String conIsSr1Slot = "isSr1Slot";
public static final String conIsTaxRelevant = "isTaxRelevant";
public static final String conIsViewForming = "isViewForming";
public static final String conMessage = "message";
public static final String conNsr1Order = "nsr1Order";
public static final String conPath = "path";
public static final String conPsDpFilteredSchemaName = "psDpFilteredSchemaName";
public static final String conPsDpFilteredSchemaNameSparte = "psDpFilteredSchemaNameSparte";
public static final String conReturnUnit = "returnUnit";
public static final String conSchema = "schema";
public static final String conSlotIndex = "slotIndex";
public static final String conSr0Order = "sr0Order";
public static final String conSr1Order = "sr1Order";
public static final String conState = "state";
public static final String conStreet = "street";

public static final String conType = "type";
public static final String conUnit = "unit";
public static final String conUser = "user";
public static final String conZipCode = "zipCode";
public static final String conLastCentralDataTransferBegin = "lastCentralDataTransferBegin";
public static final String conMaxLength = "maxLength";
public static final String conCategoryKindId = "categoryKind_id";
public static final String conCodeCharacterId = "codeCharacter_id";
public static final String conPackageTypeId = "packageType_id";
public static final String conPaintHandlingModeId = "paintHandlingMode_id";
public static final String conVehicleTotalPriceCalculationId = "vehicleTotalPriceCalculation_i";
public static final String conReturnPropertyFormatId = "returnPropertyFormat_id";
public static final String conPriceLogicId = "priceLogic_id";
public static final String conTypeId = "type_id";

public static final String conConflictTypeId = "conflictType_id";
public static final String conConflictStateId = "conflictState_id";

public static final String conMaturityLevelId = "maturityLevel_id";
public static final String conStatusId = "status_id";
public static final String conRuleScopeId = "ruleScope_Id";
public static final String conSlotTypeId = "slotType_Id";
public static final String conPrimaryPriceTypeForTestId = "primaryPriceForTest_Id";
public static final String conPriceSelectionForOverlapId = "priceSelectionForOverlap_Id";

public static final String conAcmIsPs = "isPs";
public static final String conAcmIsPsForming = "isPsForming";
public static final String conAcmSupportXmlExport = "supportXmlExport";
public static final String conAcmUseXmlExport = "useXmlExport";

public static final String conAcmLrtActivationType = "lrtActivationType";
public static final String conAcmDisplayCategory = "displayCategory";
public static final String conAcmIsArch = "isArch";
public static final String conAcmIsPriceRelated = "isPriceRelated";
public static final String conAcmUseFtoPostProcess = "useFtoPostProcess";
public static final String conAcmCondenseData = "condenseData";
public static final String conAcmEntityFilterEnumCriteria = "entityFilterEnumCriteria";
public static final String conAcmIsNt2m = "isNt2m";

// ############################################
// # enum meta information
// ############################################

// en   - enum name
// esn  - enum short name
// exn  - enum section name

public static final String enPdmOrganization = "PdmOrganization";
public static final String esnPdmOrganization = "POR";
public static final String exnPdmOrganization = M01_ACM.snDbMeta;

public static final String enPdmDataPoolType = "PdmDataPoolType";
public static final String esnPdmDataPoolType = "PDT";
public static final String exnPdmDataPoolType = M01_ACM.snDbMeta;

public static final String enStatus = "Status";
public static final String esnStatus = "sta";
public static final String exnStatus = M01_ACM.snCommon;

// ############################################
// # class meta information
// ############################################

// cln   - class name
// clxn  - class section name

public static final String clnActionHeading = "ActionHeading";
public static final String clxnActionHeading = M01_ACM_IVK.snDecision;
public static final String clnActionElement = "ActionElement";
public static final String clxnActionElement = M01_ACM_IVK.snDecision;
public static final String clnAggSlot = "AggregationSlot";
public static final String clxnAggSlot = M01_ACM_IVK.snProductStructure;
public static final String clnApplHistory = "ApplHistory";
public static final String clxnApplHistory = "DbAdmin";
public static final String clnApplVersion = "ApplVersion";
public static final String clxnApplVersion = "DbAdmin";
public static final String clnArchLog = "ArchiveLog";
public static final String clxnArchLog = "Meta";
public static final String clnCalculationRun = "CalculationRun";
public static final String clxnCalculationRun = M01_ACM_IVK.snAspect;
public static final String clnCategory = "Category";
public static final String clxnCategory = M01_ACM_IVK.snProductStructure;
public static final String clnChangeLogStatus = "ChangelogStatus";
public static final String clxnChangeLogStatus = M01_ACM.snChangeLog;
public static final String clnChangeLogVdokf = "ChangelogVDokF";
public static final String clxnChangeLogVdokf = M01_ACM.snChangeLog;
public static final String clnClassIdPartitionBoundaries = "ClassIdPartitionBoundaries";
public static final String clxnClassIdPartitionBoundaries = M01_ACM.snDbAdmin;
public static final String clnCodeBinaryPropertyAssignment = "CodeBinaryPropertyAssignment";
public static final String clxnCodeBinaryPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnCodeBooleanPropertyAssignment = "CodeBooleanPropertyAssignment";
public static final String clxnCodeBooleanPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnCodeLabelConflict = "CodeLabelConflict";
public static final String clxnCodeLabelConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnCodeNumericPropertyAssignment = "CodeNumericPropertyAssignment";
public static final String clxnCodeNumericPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnCodePlausibilityRule = "CodePlausibilityRule";
public static final String clxnCodePlausibilityRule = M01_ACM_IVK.snAspect;
public static final String clnCodePriceAssignment = "CodePriceAssignment";
public static final String clxnCodePriceAssignment = M01_ACM_IVK.snAspect;
public static final String clnCodePropertyAssignmentConflict = "CodePropertyAssignmentConflict";
public static final String clxnCodePropertyAssignmentConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnCodePropertyGroup = "CodePropertyGroup";
public static final String clxnCodePropertyGroup = M01_ACM_IVK.snProductStructure;
public static final String clnCodeTextPropertyAssignment = "CodeTextPropertyAssignment";
public static final String clxnCodeTextPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnCodeType = "CodeType";
public static final String clxnCodeType = M01_ACM_IVK.snCode;
public static final String clnConditionHeading = "ConditionHeading";
public static final String clxnConditionHeading = M01_ACM_IVK.snDecision;
public static final String clnConflict = "Conflict";
public static final String clxnConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnCountry = "Country";
public static final String clxnCountry = M01_ACM.snCountry;
public static final String clnCountryContextAspect = "CountryContextAspect";
public static final String clxnCountryContextAspect = M01_ACM_IVK.snAspect;
public static final String clnCountryIdList = "CountryIdList";
public static final String clxnCountryIdList = M01_ACM.snCountry;
public static final String clnCountrySpec = "CountrySpec";
public static final String clxnCountrySpec = M01_ACM.snCountry;
public static final String clnCtsConfig = "CtsConfig";
public static final String clxnCtsConfig = M01_ACM.snMeta;
public static final String clnCtsConfigHistory = "CtsConfigHistory";
public static final String clxnCtsConfigHistory = M01_ACM.snMeta;
public static final String clnCtsConfigTemplate = "CtsConfigTemplate";
public static final String clxnCtsConfigTemplate = M01_ACM.snMeta;
public static final String clnDataComparison = "DataComparison";
public static final String clxnDataComparison = M01_ACM_IVK.snDataCheck;
public static final String clnDataComparisonAttribute = "DataComparisonAttribute";
public static final String clxnDataComparisonAttribute = M01_ACM_IVK.snDataCheck;
public static final String clnDataFix = "DataFix";
public static final String clxnDataFix = "DbAdmin";
public static final String clnDataFixHistory = "DataFixHistory";
public static final String clxnDataFixHistory = "DbAdmin";
public static final String clnDataFixIgnored = "DataFixIgnored";
public static final String clxnDataFixIgnored = "DbAdmin";
public static final String clnDataFixPrecondition = "DataFixPrecondition";
public static final String clxnDataFixPrecondition = "DbAdmin";
public static final String clnDataHistory = "DataHistory";
public static final String clxnDataHistory = M01_ACM.snMeta;
public static final String clnDdlFix = "DdlFix";
public static final String clxnDdlFix = "DbAdmin";
public static final String clnDdlFixIgnored = "DdlFixIgnored";
public static final String clxnDdlFixIgnored = "DbAdmin";
public static final String clnDecisionTable = "DecisionTable";
public static final String clxnDecisionTable = M01_ACM_IVK.snDecision;
public static final String clnDivision = "Division";
public static final String clxnDivision = "Org";
public static final String clnMessage = "Message";
public static final String clxnMessage = M01_ACM_IVK.snMessage;
public static final String clnDocuNews = "DocumentationNews";
public static final String clxnDocuNews = "DocuNews";
public static final String clnDocuNewsType = "DocumentationNewsType";
public static final String clxnDocuNewsType = "DocuNews";
public static final String clnEndSlot = "EndSlot";
public static final String clxnEndSlot = M01_ACM_IVK.snProductStructure;
public static final String clnExpression = "Expression";
public static final String clxnExpression = M01_ACM_IVK.snExpression;
public static final String clnFtoChangelogSummary = "FtoChangelogSummary";
public static final String clxnFtoChangelogSummary = M01_ACM.snTrace;
public static final String clnFtoOrgChangelogSummary = "FtoMpcChangelogSummary";
public static final String clxnFtoOrgChangelogSummary = M01_ACM.snTrace;
public static final String clnFtoOrgImplicitChangesSummary = "FtoMpcImplicitChangesSummary";
public static final String clxnFtoOrgImplicitChangesSummary = M01_ACM.snTrace;
public static final String clnGeneralPriceConflict = "GeneralPriceConflict";
public static final String clxnGeneralPriceConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnGeneralSettings = "GeneralSettings";
public static final String clxnGeneralSettings = "Meta";
public static final String clnGenericAspect = "GenericAspect";
public static final String clxnGenericAspect = M01_ACM_IVK.snAspect;
public static final String clnGenericCode = "GenericCode";
public static final String clxnGenericCode = M01_ACM_IVK.snCode;

public static final String clnJob = "Job";
public static final String clxnJob = "Meta";
public static final String clnLanguageSequence = "LanguageSequence";
public static final String clxnLanguageSequence = M01_ACM.snCountry;
public static final String clnLanguageSequenceElement = "LanguageSequenceElement";
public static final String clxnLanguageSequenceElement = M01_ACM.snCountry;
public static final String clnMasterAggSlot = "MasterAggregationSlot";
public static final String clxnMasterAggSlot = M01_ACM_IVK.snProductStructure;
public static final String clnMasterEndSlot = "MasterEndSlot";
public static final String clxnMasterEndSlot = M01_ACM_IVK.snProductStructure;
public static final String clnMdsInbox = "MDSInbox";
public static final String clxnMdsInbox = "MDSInbox";
public static final String clnNSr1Validity = "NSR1Validity";
public static final String clxnNSr1Validity = M01_ACM_IVK.snAspect;
public static final String clnNotice = "Notice";
public static final String clxnNotice = "Notice";
public static final String clnNumericProperty = "NumericProperty";
public static final String clxnNumericProperty = M01_ACM_IVK.snProductStructure;
public static final String clnOrganization = "Organization";
public static final String clxnOrganization = M01_ACM.snCountry;
public static final String clnPaiMessageLog = "MessageLog";
public static final String clxnPaiMessageLog = M01_ACM_IVK.snPaiLog;
public static final String clnPlausibilityRuleConflict = "PlausibilityRuleConflict";
public static final String clxnPlausibilityRuleConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnPricePreferences = "PricePreferences";
public static final String clxnPricePreferences = M01_ACM.snMeta;
public static final String clnProductStructure = "ProductStructure";
public static final String clxnProductStructure = M01_ACM_IVK.snProductStructure;
public static final String clnProperty = "Property";
public static final String clxnProperty = M01_ACM_IVK.snProductStructure;
public static final String clnPropertyAssignment = "PropertyAssignment";
public static final String clxnPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnPropertyTemplate = "PropertyTemplate";
public static final String clxnPropertyTemplate = M01_ACM_IVK.snProductStructure;
public static final String clnProtocolLineEntry = "ProtocolLineEntry";
public static final String clxnProtocolLineEntry = M01_ACM_IVK.snProtocol;
public static final String clnProtocolParameter = "ProtocolParameter";
public static final String clxnProtocolParameter = M01_ACM_IVK.snProtocol;
public static final String clnPsDbMapping = "PsDpMapping";
public static final String clxnPsDbMapping = M01_ACM.snDbMeta;
public static final String clnRebateDefault = "RebateDefault";
public static final String clxnRebateDefault = "Meta";
public static final String clnRegistryDynamic = "RegistryDynamic";
public static final String clxnRegistryDynamic = "Meta";
public static final String clnRegistryStatic = "RegistryStatic";
public static final String clxnRegistryStatic = "Meta";
public static final String clnRel2ProdLock = "Rel2ProdLock";
public static final String clxnRel2ProdLock = M01_ACM.snMeta;
public static final String clnRel2ProdLockHistory = "Rel2ProdLockHistory";
public static final String clxnRel2ProdLockHistory = M01_ACM.snMeta;
public static final String clnRssHistory = "RssHistory";
public static final String clxnRssHistory = M01_ACM_IVK.snPaiLog;
public static final String clnRssStatus = "RssStatus";
public static final String clxnRssStatus = M01_ACM_IVK.snPaiLog;
public static final String clnSetProdAffectedEntity = "SpAffectedEntity";
public static final String clxnSetProdAffectedEntity = M01_ACM.snDbMeta;
public static final String clnSlotBinaryPropertyAssignment = "SlotBinaryPropertyAssignment";
public static final String clxnSlotBinaryPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnSlotBooleanPropertyAssignment = "SlotBooleanPropertyAssignment";
public static final String clxnSlotBooleanPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnSlotNumericPropertyAssignment = "SlotNumericPropertyAssignment";
public static final String clxnSlotNumericPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnSlotPlausibilityRule = "SlotPlausibilityRule";
public static final String clxnSlotPlausibilityRule = M01_ACM_IVK.snAspect;
public static final String clnSlotPropertyAssignmentConflict = "SlotPropertyAssignmentConflict";
public static final String clxnSlotPropertyAssignmentConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnSlotTextPropertyAssignment = "SlotTextPropertyAssignment";
public static final String clxnSlotTextPropertyAssignment = M01_ACM_IVK.snAspect;
public static final String clnSnapshotV8Agent = "Snapshot_Agent";
public static final String clxnSnapshotV8Agent = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Appl = "Snapshot_Appl";
public static final String clxnSnapshotV8Appl = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8ApplInfo = "Snapshot_ApplI";
public static final String clxnSnapshotV8ApplInfo = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8BufferPool = "Snapshot_Bp";
public static final String clxnSnapshotV8BufferPool = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Container = "Snapshot_Cnt";
public static final String clxnSnapshotV8Container = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Db = "Snapshot_Db";
public static final String clxnSnapshotV8Db = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Dbm = "Snapshot_Dbm";
public static final String clxnSnapshotV8Dbm = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Lock = "Snapshot_Lock";
public static final String clxnSnapshotV8Lock = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8LockWait = "Snapshot_LockWt";
public static final String clxnSnapshotV8LockWait = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Sql = "Snapshot_Sql";
public static final String clxnSnapshotV8Sql = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Statement = "Snapshot_Stmnt";
public static final String clxnSnapshotV8Statement = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Table = "Snapshot_Table";
public static final String clxnSnapshotV8Table = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8Tbs = "Snapshot_Tbs";
public static final String clxnSnapshotV8Tbs = M01_ACM.snDbMonitor;
public static final String clnSnapshotV8TbsCfg = "Snapshot_TbsCfg";
public static final String clxnSnapshotV8TbsCfg = M01_ACM.snDbMonitor;
public static final String clnSolverData = "SolverData";
public static final String clxnSolverData = "Meta";
public static final String clnSpAffectedEntity = "SpAffectedEntity";
public static final String clxnSpAffectedEntity = M01_ACM.snTrace;
public static final String clnSpFilteredEntity = "SpFilteredEntity";
public static final String clxnSpFilteredEntity = M01_ACM.snTrace;
public static final String clnSpStatement = "SpStatement";
public static final String clxnSpStatement = M01_ACM.snTrace;
public static final String clnSr0Validity = "SR0Validity";
public static final String clxnSr0Validity = M01_ACM_IVK.snAspect;
public static final String clnSr1Validity = "SR1Validity";
public static final String clxnSr1Validity = M01_ACM_IVK.snAspect;
public static final String clnStandardCode = "StandardCode";
public static final String clxnStandardCode = "Code";
public static final String clnStandardEquipment = "StandardEquipment";
public static final String clxnStandardEquipment = M01_ACM_IVK.snAspect;
public static final String clnTaxParameter = "TaxParameter";
public static final String clxnTaxParameter = M01_ACM_IVK.snPricing;
public static final String clnTaxType = "TaxType";
public static final String clxnTaxType = M01_ACM_IVK.snPricing;
public static final String clnTechDataDeltaImport = "TechDataDeltaImport";
public static final String clxnTechDataDeltaImport = M01_ACM_IVK.snStaging;
public static final String clnTerm = "Term";
public static final String clxnTerm = M01_ACM_IVK.snExpression;
public static final String clnTypeConflict = "TypeConflict";
public static final String clxnTypeConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnTypeLabelConflict = "TypeLabelConflict";
public static final String clxnTypeLabelConflict = M01_ACM_IVK.snFactoryTakeover;
public static final String clnTypePriceAssignment = "TypePriceAssignment";
public static final String clxnTypePriceAssignment = M01_ACM_IVK.snAspect;
public static final String clnTypeSpec = "TypeSpec";
public static final String clxnTypeSpec = M01_ACM_IVK.snAspect;
public static final String clnTypeStandardEquipment = "TypeStandardEquipment";
public static final String clxnTypeStandardEquipment = M01_ACM_IVK.snAspect;
public static final String clnUser = "MDSUser";
public static final String clxnUser = M01_ACM.snUser;
public static final String clnView = "View";
public static final String clxnView = M01_ACM.snUser;

// ############################################
// # relationship meta information
// ############################################

// rn   - class name
// rxn  - class section name

public static final String rnAggregationSlotHasNumericProperty = "AggregationSlotHasNumericProperty";
public static final String rxnAggregationSlotHasNumericProperty = M01_ACM_IVK.snProductStructure;
public static final String rnAllowedCountriesAspect = "AllowedCountriesAspect";
public static final String rxnAllowedCountriesAspect = M01_ACM_IVK.snAspect;
public static final String rnCategoryHasNumericProperty = "CategoryHasNumericProperty";
public static final String rxnCategoryHasNumericProperty = M01_ACM_IVK.snProductStructure;
public static final String rnCodeCategory = "CodeCategory";
public static final String rxnCodeCategory = M01_ACM_IVK.snCode;
public static final String rnCountryGroupElement = "CountryGroupElement";
public static final String rxnCountryGroupElement = "Country";
public static final String rnCountryIdXRef = "CountryIdXRef";
public static final String rxnCountryIdXRef = M01_ACM.snCountry;
public static final String rnCpGroupHasProperty = "CpGroupHasProperty";
public static final String rxnCpGroupHasProperty = M01_ACM_IVK.snProductStructure;
public static final String rnSpGroupHasProperty = "SpGroupHasProperty";
public static final String rxnSpGroupHasProperty = M01_ACM_IVK.snProductStructure;
public static final String rnDisallowedCountriesAspect = "DisallowedCountriesAspect";
public static final String rxnDisallowedCountriesAspect = M01_ACM_IVK.snAspect;
public static final String rnDisplaySlot = "DisplaySlot";
public static final String rxnDisplaySlot = M01_ACM.snUser;
public static final String rnMessageSeverity = "MessageSeverity";
public static final String rxnMessageSeverity = M01_ACM_IVK.snMessage;
public static final String rnNsr1ValidForOrganization = "Nsr1ValidForOrganization";
public static final String rxnNsr1ValidForOrganization = M01_ACM_IVK.snProductStructure;
public static final String rnCodeValidForOrganization = "CodeValidForOrganization";
public static final String rxnCodeValidForOrganization = M01_ACM_IVK.snCode;
public static final String rnPropertyValidForOrganization = "PropertyValidForOrganization";
public static final String rxnPropertyValidForOrganization = M01_ACM_IVK.snProductStructure;
public static final String rnOrgManagesCountry = "OrgManagesCountry";
public static final String rxnOrgManagesCountry = "Country";

// ################################################
//            View Names
// ################################################

// vn   - view name
// vsn  - view short name

public static final String vnEntityFilterEnum = "EntityFilter_Enum";
public static final String vsnEntityFilterEnum = "EFE";
public static final String vnEntityFilterNlTextEnum = "EntityFilter_Enum_Nl_Text";
public static final String vsnEntityFilterNlTextEnum = "EFN";
public static final String vnPsFormingLdmTab = "PsFormingLdmTab";
public static final String vsnPsFormingLdmTab = "PFL";
public static final String vnPsFormingPdmTab = "PsFormingPdmTab";
public static final String vsnPsFormingPdmTab = "PFP";
public static final String vnRel2ProdLockHistory = "Rel2ProdLockHistory";
public static final String vsnRel2ProdLockHistory = "RPH";
public static final String vnRel2ProdLock = "Rel2ProdLock";
public static final String vsnRel2ProdLock = "RPL";
public static final String vnSnapshotV8Agent = "SnapshotAgent";
public static final String vsnSnapshotV8Agent = "VSA";
public static final String vnSnapshotV8ApplInfo = "SnapshotApplI";
public static final String vsnSnapshotV8ApplInfo = "VAI";
public static final String vnSnapshotV8Bufferpool = "SnapshotBp";
public static final String vsnSnapshotV8Bufferpool = "VSB";
public static final String vnSnapshotV8Container = "SnapshotCnt";
public static final String vsnSnapshotV8Container = "VSC";
public static final String vnSnapshotV8Db = "SnapshotDb";
public static final String vsnSnapshotV8Db = "VSD";
public static final String vnSnapshotV8Dbm = "SnapshotDbm";
public static final String vsnSnapshotV8Dbm = "VSM";
public static final String vnSnapshotV8Lock = "SnapshotLock";
public static final String vsnSnapshotV8Lock = "VSL";
public static final String vnSnapshotV8LockWait = "SnapshotLockWait";
public static final String vsnSnapshotV8LockWait = "VLW";
public static final String vnSnapshotV8SnapshotAppl = "SnapshotAppl";
public static final String vsnSnapshotV8SnapshotAppl = "VAP";
public static final String vnSnapshotV8Sql = "SnapshotSql";
public static final String vsnSnapshotV8Sql = "VSS";
public static final String vnSnapshotV8Statement = "SnapshotStmnt";
public static final String vsnSnapshotV8Statement = "VST";
public static final String vnSnapshotV8Table = "SnapshotTable";
public static final String vsnSnapshotV8Table = "VST";
public static final String vnSnapshotV8Tbs = "SnapshotTbs";
public static final String vsnSnapshotV8Tbs = "VTS";
public static final String vnSnapshotV8TbsCfg = "SnapshotTbsCfg";
public static final String vsnSnapshotV8TbsCfg = "VTC";
public static final String vnXmlFuncMap = "XmlFuncMap";
public static final String vsnXmlFuncMap = "XFM";
public static final String vnXmlViewMap = "XmlViewMap";
public static final String vsnXmlViewMap = "XVM";
public static final String vnXsdFuncMap = "XsdFuncMap";
public static final String vsnXsdFuncMap = "XSM";

// ################################################
//            Stored Procedure Names
// ################################################

// spn   - stored procedure name
// spsn  - stored procedure short name

public static final String spnAHPropagateStatus = "AHPropagateStatus";
public static final String spnActivateAllPrices = "ActivateAllPrices";
public static final String spnActivateAllCodePrices = "ActivateAllCodePrices";
public static final String spnActivateAllTypePrices = "ActivateAllTypePrices";
public static final String spnActivateNationalCodeTexts = "ActivateNationalCodeTexts";
public static final String spnAddTablePartitionByDiv = "AddTablePartitionByDiv";
public static final String spnAddTablePartitionByPs = "AddTablePartitionByPs";
public static final String spnAddTestUser = "AddTestUser";
public static final String spnArchiveOrg = "ArchiveOrg";
public static final String spnArchiveOrgPurge = "ArchiveOrgPurge";
public static final String spnArchiveOrgEstimate = "ArchiveOrgEstimate";
public static final String spnAssertRebateDefault = "AssertRebateDefault";
public static final String spnAssignCodeCat = "AssignCodeCat";
public static final String spnCheckChangeLog = "CheckChangeLog";
public static final String spnClBroadcast = "ChangelogBroadcast";
public static final String spnGetGroupElements = "GetGroupElements";
public static final String spnDataChkCleanup = "DataCleanup";
public static final String spnDataChkCompare = "DataCompare";
public static final String spnDataChkCp2RefTab = "DataCp2RefTab";
public static final String spnDataChkExport = "DataExport";
public static final String spnDataChkImport = "DataImport";
public static final String spnDataChkLoad = "DataLoad";
public static final String spnDataInconsCleanup = "DataCleanup";
public static final String spnDeleteNSR1 = "DeleteNSR1";
public static final String spnLrtIncludesDivisionData = "lrt_Includes_Division_Data";
public static final String spnLrtLock_Genericcode = "lrtLock_Genericcode";
public static final String spnDeleteCBMV = "DeleteCBMV";
public static final String spnDeleteProductiveCode = "DeleteProductiveCode";
public static final String spnDeleteTablePartitionByDiv = "DeleteTablePartitionByDiv";
public static final String spnDeleteTablePartitionByPs = "DeleteTablePartitionByPs";
public static final String spnDeleteTechAspect = "DeleteTechAspect";
public static final String spnDeleteTechProperty = "DelTechProperty";
public static final String spnDeleteUnusedExpressions = "DeleteUnusedExpressions";
public static final String spnDfxExecute = "DfxExecute";
public static final String spnFactoryTakeOver = "FactoryTakeOver";
public static final String spnFtoGetChangeLog = "FtoGetChangeLog";
public static final String spnFtoGetChangeLogCard = "FtoGetChangeLogCard";
public static final String spnFtoGetConflicts = "FtoGetConflicts";
public static final String spnFtoGetPriceConflicts = "FtoGetPriceConflicts";
public static final String spnFtoGetEnpEbpMap = "FtoGetEnpEbpMapping";
public static final String spnFtoGetImplicitChanges = "FtoGetImplicitChanges";
public static final String spnFtoInitial = "FactoryTakeOver_Initial";
public static final String spnFtoLock = "Ftolock";
public static final String spnFtoPostProc = "FtoPostProc";
public static final String spnFtoSetEnp = "FtoSetEnp";
public static final String spnGenWorkspace = "Gen_Workspace";
public static final String spnGenWorkspaceWrapper = "GenWorkspace";
public static final String spnGetCodesWithoutDep = "GetCodesWithoutDep";
public static final String spnGetCodesWithoutDepAddOids = "GetCodesWithoutDepAddOids";
public static final String spnGetValue = "GetValue";
public static final String spnGroupIdSync = "GASync";
public static final String spnModifyCodeType = "ModifyCodeType";
public static final String spnCheckAffectedObjects = "CheckAffectedObjects";
public static final String spnOrgInit = "OrgInit";
public static final String spnOrgInitBus = "OrgInitBus";
public static final String spnOrgInitEnp = "OrgInitEnp";
public static final String spnOrgInitDupCode = "OrgInitDupCode";
public static final String spnOrgInitMeta = "OrgInitMeta";
public static final String spnPropExpr = "PropExpr";
public static final String spnPropInvExpr = "PropInvExpr";
public static final String spnRebateInitDefault = "RebateInitDefault";
public static final String spnRegStaticInit = "RegStaticInit";
public static final String spnRel2ProdIsSet = "is_Rel2ProdLock_set";
public static final String spnResetRel2ProdLock = "reset_Rel2ProdLock";
public static final String spnResetRel2ProdLockExclusive = "reset_Rel2ProdLockExclusive";
public static final String spnResetRel2ProdLockGenWs = "reset_Rel2ProdLockGenWs";
public static final String spnResetRel2ProdLocks = "reset_Rel2ProdLocks";
public static final String spnResetRel2ProdLocksOrphan = "reset_Rel2ProdLocksOrphan";
public static final String spnResetRel2ProdLocksWrapper = "resetRel2ProdLocks";
public static final String spnRssGetStatus = "RssGetStatus";
public static final String spnSPGetAffectedEntities = "SpGetAffectedEntities";
public static final String spnSetApplVersion = "SetApplVersion";
public static final String spnSetCtsConfig = "SetCtsConfig";
public static final String spnSetMessageSeverity = "SetMessageSeverity";
public static final String spnSetProductivePreProcess = "SetProductivePreProc";
public static final String spnSetProductive = "SetProductive";
public static final String spnSetProductiveIncludesDivisionData = "setProd_Includes_Division_Data";
public static final String spnSetProductivePostProcess = "SetProductivePostProc";
public static final String spnSetRel2ProdLock = "set_Rel2ProdLock";
public static final String spnSetRel2ProdLockExclusive = "set_Rel2ProdLockExclusive";
public static final String spnSetRel2ProdLockGenWs = "set_Rel2ProdLockGenWs";
public static final String spnSetRel2ProdLocksWrapper = "setRel2ProdLocks";
public static final String spnSetTablePartCfgDiv = "SetTablePartCfgByDiv";
public static final String spnSetTablePartCfgPs = "SetTablePartCfgByPs";
public static final String spnSpGenChangelog = "SpGenChangelog";
public static final String spnSuffixRel2ProdOther = "_other";
public static final String spnSuffixRel2ProdOthers = "_others";
public static final String spnTestData = "TestData";
public static final String spnTracePersist = "TracePersist";
public static final String spnVirtAttrSync = "VASync";

// ################################################
//            User Defined Functions
// ################################################

// udfn   - user defined function name
// udfsn  - user defined function short name

public static final String udfnAggrSlotOid4Label = "AggrSlotOid4Label";
public static final String udfnAggrSlotOid4Prop = "AggrSlotOid4Prop";
public static final String udfnAllowedCountry2Str = "ALC2STR";
public static final String udfnAllowedCountry2Str0 = "ALC2STR0";
public static final String udfnAssertCountryIdList = "AssertCidList";
public static final String udfnCatOid4Code = "CatOid4Code";
public static final String udfnDisallowedCountry2Str = "DALC2STR";
public static final String udfnDisallowedCountry2Str0 = "DALC2STR0";
public static final String udfnEndSlotLabel4Oid = "EndSlotLabel4Oid";
public static final String udfnEndSlotOid4Code = "EndSlotOid4Code";
public static final String udfnEndSlotOid4CodeOL = "EndSlotOid4Code_OL";
public static final String udfnEndSlotOid4CodeST = "EndSlotOid4Code_ST";
public static final String udfnEndSlotOid4CodeTB = "EndSlotOid4Code_TB";
public static final String udfnEndSlotOid4Label = "EndSlotOid4Label";
public static final String udfnEndSlotOid4LzCode = "EndSlotOid4LzCode";
public static final String udfnGcoOid4Code = "GcoOid4Code";
public static final String udfnGenRel2ProdLockKey = "genRel2ProdLockKey";
public static final String udfnGetCpgByPriceAssignment = "GCPGBPA";
public static final String udfnNormalizeCountryIdList = "NormCidList";
public static final String udfnNprOid4Code = "NprOid4Code";
public static final String udfnNprOid4CodeId = "NprOid4Code_ID";
public static final String udfnParseSr0Context = "ParseSr0Context";
public static final String udfnPropOid4PropLabel = "PropOid4PropLabel";
public static final String udfnPsOid2Sparte = "PsOid2Sparte";
public static final String udfnSparte2DivOid = "Sparte2DivOid";
public static final String udfnSparte2PsOid = "Sparte2PsOid";


// ### ENDIF IVK ###





}