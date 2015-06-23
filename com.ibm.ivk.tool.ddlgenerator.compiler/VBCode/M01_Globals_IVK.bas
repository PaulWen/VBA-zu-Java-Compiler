Attribute VB_Name = "M01_Globals_IVK"
' ### IF IVK ###
Option Explicit

Global Const gc_tempTabNameConflict = "SESSION.Conflict"
Global Const gc_tempTabNameConflictPrice = "SESSION.ConflictPrice"
Global Const gc_tempTabNameConflictSlotNames = "SESSION.ConflictSlotNames"
Global Const gc_tempTabNameConflictMultiGa = "SESSION.ConflictMultiGa"
Global Const gc_tempTabNameConflictMultiSr = "SESSION.ConflictMultiSr"
Global Const gc_tempTabNameConflictMultiGaNl = "SESSION.ConflictMultiGaNl"
Global Const gc_tempTabNameConflictMultiCdNl = "SESSION.ConflictMultiCdNl"
Global Const gc_tempTabNameChangeLogSummary = "SESSION.ChangeLogSummary"
Global Const gc_tempTabNameChangeLogStatus = "SESSION.ChangeLogStatus"
Global Const gc_tempTabNameChangeLogOrgSummary = "SESSION.MpcChangeLogSummary"
Global Const gc_tempTabNameChangeLogImplicitChanges = "SESSION.MpcImplicitChangesSummary"
Global Const gc_tempTabNameRelevantCountryIdList = "SESSION.RelevantCountryIdList"
Global Const gc_tempTabNameRelevantCountryIdXRef = "SESSION.RelevantCountryIdXRef"
Global Const gc_tempTabNameManagedCountry = "SESSION.ManagedCountry"
Global Const gc_tempTabNameRelevantCountry = "SESSION.RelevantCountry"
Global Const gc_tempTabNameFtoClSr0ContextFac = "SESSION.Sr0ContextFac"
Global Const gc_tempTabNameFtoClSr0ContextOrg = "SESSION.Sr0ContextMpc"

Global Const gc_tempTabNameSpFilteredEntities = "SESSION.SpFilteredEntities"
Global Const gc_tempTabNameSpAffectedEntities = "SESSION.SpAffectedEntities"

Global Const gc_db2RegVarPsOid = "CURRENT CLIENT_APPLNAME"
Global Const gc_db2RegVarPsOidSafeSyntax = "'0' || " & gc_db2RegVarPsOid

Global Const gc_dirPrefixOrg = "MPC-"

Global Const gc_ftoConflictStateOpen = 1
Global Const gc_ftoConflictStateResolved = 2

Global Const gc_ftoConflictTypeNSr1 = 1
Global Const gc_ftoConflictTypeGeneralPrice = 7
Global Const gc_ftoConflictTypeCodeLabel = 3
Global Const gc_ftoConflictTypeTypeLabel = 8
Global Const gc_ftoConflictTypePlausibilityRule = 6
Global Const gc_ftoConflictTypeCodePropertyAssignment = 4
Global Const gc_ftoConflictTypeSlotPropertyAssignment = 5

Global Const gc_langIdGerman = 1
Global Const gc_langIdEnglish = 2

Global Const gc_allowedCountriesMaxLength = 40
Global Const gc_disallowedCountriesMaxLength = 200

Global Const gc_anSuffixNat = "_national"
Global Const gc_asnSuffixNat = "N"
Global Const gc_anSuffixNatActivated = "_isNatActive"
Global Const gc_asnSuffixNatActivated = "ina"

Global g_phaseIndexGaSyncSupport  As Integer
Global g_phaseIndexArchive        As Integer
Global g_phaseIndexPsTagging      As Integer
Global g_phaseIndexXmlExport      As Integer
Global g_phaseIndexUseCases       As Integer
Global g_phaseIndexVirtAttr       As Integer
Global g_phaseIndexGroupId        As Integer
Global g_phaseIndexDataCompare    As Integer

Global g_domainIndexChangeComment As Integer
Global g_domainIndexCodeNumber As Integer
Global g_domainIndexCountryIdList As Integer
Global g_domainIndexLrtLabel As Integer
Global g_domainIndexTmpPrio As Integer
Global g_domainIndexUserName As Integer
Global g_domainIndexXmlRecord As Integer
Global g_domainIndexBinaryPropertyValue As Integer
Global g_domainIndexTemplateFileData As Integer
Global g_domainIndexBIBRegistryValue As Integer
Global g_domainIndexLongText As Integer
Global g_domainIndexReportFileData As Integer

Global g_enumIndexPdmDataPoolType As Integer
Global g_enumIndexPdmOrganization As Integer
Global g_enumIndexStatus As Integer
Global g_enumIndexLanguage As Integer

Global g_classIndexFtoChangelogSummary As Integer
Global g_classIndexFtoOrgChangelogSummary As Integer
Global g_classIndexFtoOrgImplicitChangesSummary As Integer
Global g_classIndexSpAffectedEntity As Integer
Global g_classIndexSpFilteredEntity As Integer
Global g_classIndexSpStatement As Integer
Global g_classIndexActionHeading As Integer
Global g_classIndexActionElement As Integer
Global g_classIndexAggregationSlot As Integer
Global g_classIndexApplHistory As Integer
Global g_classIndexApplVersion As Integer
Global g_classIndexArchLog As Integer
Global g_classIndexCategory As Integer
Global g_classIndexCalculationRun As Integer
Global g_classIndexChangeLogStatus As Integer
Global g_classIndexClassIdPartitionBoundaries As Integer
Global g_classIndexCleanJobs As Integer
Global g_classIndexCodeBinaryPropertyAssignment As Integer
Global g_classIndexCodeBooleanPropertyAssignment As Integer
Global g_classIndexCodeNumericPropertyAssignment As Integer
Global g_classIndexCodePlausibilityRule As Integer
Global g_classIndexCodePriceAssignment As Integer
Global g_classIndexCodePropertyGroup As Integer
Global g_classIndexCodeTextPropertyAssignment As Integer
Global g_classIndexCodeType As Integer
Global g_classIndexConditionHeading As Integer
Global g_classIndexConflict As Integer
Global g_classIndexTypeConflict As Integer
Global g_classIndexGeneralPriceConflict As Integer
Global g_classIndexCodeLabelConflict As Integer
Global g_classIndexTypeLabelConflict As Integer
Global g_classIndexPlausibilityRuleConflict As Integer
Global g_classIndexCodePropertyAssignmentConflict As Integer
Global g_classIndexSlotPropertyAssignmentConflict As Integer
Global g_classIndexCountry As Integer
Global g_classIndexCountryContextAspect As Integer
Global g_classIndexCountryIdList As Integer
Global g_classIndexCountrySpec As Integer
Global g_classIndexCtsConfig As Integer
Global g_classIndexCtsConfigHistory As Integer
Global g_classIndexCtsConfigTemplate As Integer
Global g_classIndexDataComparison As Integer
Global g_classIndexDataComparisonAttribute As Integer
Global g_classIndexDataFix As Integer
Global g_classIndexDataFixHistory As Integer
Global g_classIndexDataFixIgnored As Integer
Global g_classIndexDataFixPrecondition As Integer
Global g_classIndexDataHistory As Integer
Global g_classIndexDdlFix As Integer
Global g_classIndexDdlFixIgnored As Integer
Global g_classIndexDivision As Integer
Global g_classIndexMessage As Integer
Global g_classIndexDocuNews As Integer
Global g_classIndexDocuNewsType As Integer
Global g_classIndexEndSlot As Integer
Global g_classIndexExpression As Integer
Global g_classIndexGeneralSettings As Integer
Global g_classIndexGenericAspect As Integer
Global g_classIndexGenericCode As Integer
Global g_classIndexJob As Integer
Global g_classIndexLanguageSequence As Integer
Global g_classIndexLanguageSequenceElement As Integer
Global g_classIndexMasterAggregationSlot As Integer
Global g_classIndexMasterEndSlot As Integer
Global g_classIndexNotice As Integer
Global g_classIndexNSr1Validity As Integer
Global g_classIndexNumericProperty As Integer
Global g_classIndexPaiMessageLog As Integer
Global g_classIndexPricePreferences As Integer
Global g_classIndexProductStructure As Integer
Global g_classIndexProperty As Integer
Global g_classIndexPropertyAssignment As Integer
Global g_classIndexPropertyTemplate As Integer
Global g_classIndexProtocolLineEntry As Integer
Global g_classIndexProtocolParameter As Integer
Global g_classIndexPsDbMapping As Integer
Global g_classIndexRebateDefault As Integer
Global g_classIndexRegistryDynamic As Integer
Global g_classIndexRegistryStatic As Integer
Global g_classIndexRel2ProdLock As Integer
Global g_classIndexRel2ProdLockHistory As Integer
Global g_classIndexRssHistory As Integer
Global g_classIndexRssStatus As Integer
Global g_classIndexSlotBinaryPropertyAssignment As Integer
Global g_classIndexSlotBooleanPropertyAssignment As Integer
Global g_classIndexSlotNumericPropertyAssignment As Integer
Global g_classIndexSlotPlausibilityRule As Integer
Global g_classIndexSlotTextPropertyAssignment As Integer
Global g_classIndexSolverData As Integer
Global g_classIndexSr0Validity As Integer
Global g_classIndexSr1Validity As Integer
Global g_classIndexStandardCode As Integer
Global g_classIndexStandardEquipment As Integer
Global g_classIndexTaxParameter As Integer
Global g_classIndexTaxType As Integer
Global g_classIndexTechDataDeltaImport As Integer
Global g_classIndexTerm As Integer
Global g_classIndexTypePriceAssignment As Integer
Global g_classIndexTypeSpec As Integer
Global g_classIndexTypeStandardEquipment As Integer
Global g_classIndexView As Integer

Global g_relIndexAggregationSlotHasNumericProperty As Integer
Global g_relIndexCategoryHasNumericProperty As Integer
Global g_relIndexCodeCategory As Integer
Global g_relIndexCountryGroupElement As Integer
Global g_relIndexCountryIdXRef As Integer
Global g_relIndexCpGroupHasProperty As Integer
Global g_relIndexSpGroupHasProperty As Integer
Global g_relIndexDisplaySlot As Integer
Global g_relIndexNsr1ValidForOrganization As Integer
Global g_relIndexCodeValidForOrganization As Integer
Global g_relIndexPropertyValidForOrganization As Integer
Global g_relIndexOrgManagesCountry As Integer

Global g_migDataPoolIndex As Integer
Global g_migDataPoolId As Integer
Global g_productiveDataPoolIndex As Integer
Global g_productiveDataPoolId As Integer
Global g_archiveDataPoolIndex As Integer
Global g_archiveDataPoolId As Integer
Global g_sim1DataPoolId As Integer
Global g_sim2DataPoolId As Integer

Global g_qualTabNameApplHistory As String
Global g_qualTabNameApplVersion As String
Global g_qualTabNameClassIdPartitionBoundaries As String
Global g_qualTabNameCleanJobs As String
Global g_qualTabNameCodeType As String
Global g_qualTabNameCountryIdList As String
Global g_qualTabNameCountrySpec As String
Global g_qualTabNameCtsConfig As String
Global g_qualTabNameCtsConfigTemplate As String
Global g_qualTabNameDataComparison As String
Global g_qualTabNameDataComparisonAttribute As String
Global g_qualTabNameDataFix As String
Global g_qualTabNameDataFixHistory As String
Global g_qualTabNameDataFixIgnored As String
Global g_qualTabNameDataFixPrecondition As String
Global g_qualTabNameDataHistory As String
Global g_qualTabNameDivision As String
Global g_qualTabNameMessage As String
Global g_qualTabNameLanguageSequence As String
Global g_qualTabNameLanguageSequenceElement As String
Global g_qualTabNamePaiMessageLog As String
Global g_qualTabNamePricePreferencesCto As String
Global g_qualTabNameProductStructure As String
Global g_qualTabNameProductStructureNl As String
Global g_qualTabNamePropertyTemplate As String
Global g_qualTabNamePropertyTemplateNl As String
Global g_qualTabNamePsDpMapping As String
Global g_qualTabNameRebateDefault As String
Global g_qualTabNameRegistryDynamic As String
Global g_qualTabNameRegistryStatic As String
Global g_qualTabNameRel2ProdLock As String
Global g_qualTabNameRel2ProdLockHistory As String
Global g_qualTabNameRssHistory As String
Global g_qualTabNameRssStatus As String

Global g_qualTabNameCountryIdXRef As String
Global g_qualTabNameOrgManagesCountry As String

Global g_dbtLockRequestorId As String
Global g_dbtR2pLockContext As String
Global g_dbtCodeNumber As String
Global g_dbtChangeComment As String
Global g_dbtLrtLabel As String

Global g_activePsOidDdl As String

Global g_anIsInUseByFto As String
Global g_anIsActive As String
Global g_anAllowedCountries As String
Global g_anDisAllowedCountries As String
Global g_anCodeNumber As String
Global g_anSlotType As String
Global g_anSr0Context As String
Global g_anIsCentralDataTransfer As String
Global g_anIsUnderConstruction As String
Global g_anChangeComment As String
Global g_anHasBeenSetProductive As String
Global g_anIsPsForming As String
Global g_anIsDeleted As String
Global g_anIsDeletable As String
Global g_anIsNotPublished As String
Global g_anIsStandard As String
Global g_anIsDefault As String
Global g_anIsDuplicating As String
Global g_anIsBlockedFactory As String
Global g_anIsBlockedPrice As String
Global g_anIsNational As String
Global g_anLrtComment As String
Global g_anPsOid As String
Global g_anDivOid As String
Global g_anValidFrom As String
Global g_anValidTo As String
Global g_anHasConflict As String
Global g_anSequenceNumber As String
Global g_anLockValueOld As String
Global g_anLockValueNew As String
Global g_anLockMode As String
Global g_anLockContext As String
Global g_anLockOperation As String
Global g_anLockTimestamp As String
Global g_anRuleScope As String
Global g_anRebateValueType As String
Global g_anSection As String
Global g_anKey As String
Global g_anSubKey As String
Global g_anValue As String
Global g_anMessageId As String
Global g_anName As String
Global g_anEventType As String

Global g_anAcmIsPs As String
Global g_anAcmIsPsForming As String
Global g_anAcmSupportXmlExport As String
Global g_anAcmUseXmlExport As String
Global g_anLdmIsMqt As String

Global g_anAcmLrtActivationType         As String
Global g_anAcmDisplayCategory           As String
Global g_anAcmIsArch                    As String
Global g_anAcmIsPriceRelated            As String
Global g_anAcmUseFtoPostProcess         As String
Global g_anAcmCondenseData              As String
Global g_anAcmEntityFilterEnumCriteria  As String

Global g_anConflictTypeId As String
Global g_anConflictStateId As String

Global g_qualFuncNameGetLrtTargetStatus As String
Global g_qualFuncNameIsNumeric As String


Sub initGlobals_IVK()
  ReDim g_fileNameIncrements(1 To 15)
  g_phaseIndexRegularTables = 1: g_fileNameIncrements(g_phaseIndexRegularTables) = phaseRegularTables
  g_phaseIndexCoreSupport = 2: g_fileNameIncrements(g_phaseIndexCoreSupport) = phaseCoreSupport
  g_phaseIndexModuleMeta = 3: g_fileNameIncrements(g_phaseIndexModuleMeta) = phaseModuleMeta:
  g_phaseIndexGaSyncSupport = 4: g_fileNameIncrements(g_phaseIndexGaSyncSupport) = phaseGaSyncSupport
  g_phaseIndexFksRelTabs = 5: g_fileNameIncrements(g_phaseIndexFksRelTabs) = phaseFksRelTabs
  g_phaseIndexLrt = 6: g_fileNameIncrements(g_phaseIndexLrt) = phaseLrt
  g_phaseIndexLrtViews = 7: g_fileNameIncrements(g_phaseIndexLrtViews) = phaseLrtViews
  g_phaseIndexChangeLogViews = 8: g_fileNameIncrements(g_phaseIndexChangeLogViews) = phaseChangeLogViews
  g_phaseIndexLrtSupport = 9: g_fileNameIncrements(g_phaseIndexLrtSupport) = phaseLrtSupport
  g_phaseIndexArchive = 10: g_fileNameIncrements(g_phaseIndexArchive) = phaseArchive
  g_phaseIndexPsTagging = 11: g_fileNameIncrements(g_phaseIndexPsTagging) = phasePsTagging
  g_phaseIndexXmlExport = 12: g_fileNameIncrements(g_phaseIndexXmlExport) = phaseXmlExport
  g_phaseIndexDbSupport = 13: g_fileNameIncrements(g_phaseIndexDbSupport) = phaseDbSupport
  g_phaseIndexUseCases = 14: g_fileNameIncrements(g_phaseIndexUseCases) = phaseUseCases
  g_phaseIndexAliases = 15: g_fileNameIncrements(g_phaseIndexAliases) = phaseAliases

  g_phaseIndexVirtAttr = g_phaseIndexLrtSupport
  g_phaseIndexGroupId = g_phaseIndexLrtSupport
  g_phaseIndexDataCompare = g_phaseIndexDbSupport
  g_phaseIndexLogChange = g_phaseIndexPsTagging
  
  g_domainIndexChangeComment = getDomainIndexByName(dxnChangeComment, dnChangeComment)
  g_domainIndexCodeNumber = getDomainIndexByName(dxnCodeNumber, dnCodeNumber)
  g_domainIndexCountryIdList = getDomainIndexByName(dxnCountryIdList, dnCountryIdList)
  g_domainIndexLrtLabel = getDomainIndexByName(dxnLrtLabel, dnLrtLabel)
  g_domainIndexTmpPrio = getDomainIndexByName(dxnSmallNumber, dnSmallNumber)
  g_domainIndexUserName = getDomainIndexByName(dxnUserId, dnUserName)
  g_domainIndexXmlRecord = getDomainIndexByName(dxnXmlRecord, dnXmlRecord)
  g_domainIndexBinaryPropertyValue = getDomainIndexByName(dxnBinaryPropertyValue, dnBinaryPropertyValue)
  g_domainIndexTemplateFileData = getDomainIndexByName(dxnTemplateFileData, dnTemplateFileData)
  g_domainIndexBIBRegistryValue = getDomainIndexByName(dxnBIBRegistryValue, dnBIBRegistryValue)
  g_domainIndexLongText = getDomainIndexByName(dxnLongText, dnLongText)
  g_domainIndexReportFileData = getDomainIndexByName(dxnReportFileData, dnReportFileData)

  g_enumIndexPdmDataPoolType = getEnumIndexByName(exnPdmDataPoolType, enPdmDataPoolType)
  g_enumIndexPdmOrganization = getEnumIndexByName(exnPdmOrganization, enPdmOrganization)
  g_enumIndexStatus = getEnumIndexByName(exnStatus, enStatus)
  g_enumIndexLanguage = getEnumIndexByName(snCommon, enLanguage)
  
  g_classIndexFtoChangelogSummary = getClassIndexByName(clxnFtoChangelogSummary, clnFtoChangelogSummary)
  g_classIndexFtoOrgChangelogSummary = getClassIndexByName(clxnFtoOrgChangelogSummary, clnFtoOrgChangelogSummary)
  g_classIndexFtoOrgImplicitChangesSummary = getClassIndexByName(clxnFtoOrgImplicitChangesSummary, clnFtoOrgImplicitChangesSummary)
  g_classIndexSpAffectedEntity = getClassIndexByName(clxnSpAffectedEntity, clnSpAffectedEntity)
  g_classIndexSpFilteredEntity = getClassIndexByName(clxnSpFilteredEntity, clnSpFilteredEntity)
  g_classIndexSpStatement = getClassIndexByName(clxnSpStatement, clnSpStatement)
  g_classIndexActionHeading = getClassIndexByName(clxnActionHeading, clnActionHeading)
  g_classIndexActionElement = getClassIndexByName(clxnActionElement, clnActionElement)
  g_classIndexAggregationSlot = getClassIndexByName(clxnAggSlot, clnAggSlot)
  g_classIndexApplHistory = getClassIndexByName(clxnApplHistory, clnApplHistory)
  g_classIndexApplVersion = getClassIndexByName(clxnApplVersion, clnApplVersion)
  g_classIndexArchLog = getClassIndexByName(clxnArchLog, clnArchLog)
  g_classIndexCalculationRun = getClassIndexByName(clxnCalculationRun, clnCalculationRun)
  g_classIndexCategory = getClassIndexByName(clxnCategory, clnCategory)
  g_classIndexChangeLogStatus = getClassIndexByName(clxnChangeLogStatus, clnChangeLogStatus)
  g_classIndexClassIdPartitionBoundaries = getClassIndexByName(clxnClassIdPartitionBoundaries, clnClassIdPartitionBoundaries)
  g_classIndexCleanJobs = getClassIndexByName(clxnCleanJobs, clnCleanJobs)
  g_classIndexCodeBinaryPropertyAssignment = getClassIndexByName(clxnCodeBinaryPropertyAssignment, clnCodeBinaryPropertyAssignment)
  g_classIndexCodeBooleanPropertyAssignment = getClassIndexByName(clxnCodeBooleanPropertyAssignment, clnCodeBooleanPropertyAssignment)
  g_classIndexCodeNumericPropertyAssignment = getClassIndexByName(clxnCodeNumericPropertyAssignment, clnCodeNumericPropertyAssignment)
  g_classIndexCodePlausibilityRule = getClassIndexByName(clxnCodePlausibilityRule, clnCodePlausibilityRule)
  g_classIndexCodePriceAssignment = getClassIndexByName(clxnCodePriceAssignment, clnCodePriceAssignment)
  g_classIndexCodePropertyGroup = getClassIndexByName(clxnCodePropertyGroup, clnCodePropertyGroup)
  g_classIndexCodeTextPropertyAssignment = getClassIndexByName(clxnCodeTextPropertyAssignment, clnCodeTextPropertyAssignment)
  g_classIndexCodeType = getClassIndexByName(clxnCodeType, clnCodeType)
  g_classIndexConditionHeading = getClassIndexByName(clxnConditionHeading, clnConditionHeading)
  g_classIndexConflict = getClassIndexByName(clxnConflict, clnConflict)
  g_classIndexTypeConflict = getClassIndexByName(clxnTypeConflict, clnTypeConflict)
  g_classIndexGeneralPriceConflict = getClassIndexByName(clxnGeneralPriceConflict, clnGeneralPriceConflict)
  g_classIndexCodeLabelConflict = getClassIndexByName(clxnCodeLabelConflict, clnCodeLabelConflict)
  g_classIndexTypeLabelConflict = getClassIndexByName(clxnTypeLabelConflict, clnTypeLabelConflict)
  g_classIndexPlausibilityRuleConflict = getClassIndexByName(clxnPlausibilityRuleConflict, clnPlausibilityRuleConflict)
  g_classIndexCodePropertyAssignmentConflict = getClassIndexByName(clxnCodePropertyAssignmentConflict, clnCodePropertyAssignmentConflict)
  g_classIndexSlotPropertyAssignmentConflict = getClassIndexByName(clxnSlotPropertyAssignmentConflict, clnSlotPropertyAssignmentConflict)
  g_classIndexCountry = getClassIndexByName(clxnCountry, clnCountry)
  g_classIndexCountryContextAspect = getClassIndexByName(clxnCountryContextAspect, clnCountryContextAspect)
  g_classIndexCountryIdList = getClassIndexByName(clxnCountryIdList, clnCountryIdList)
  g_classIndexCountrySpec = getClassIndexByName(clxnCountrySpec, clnCountrySpec)
  g_classIndexCtsConfig = getClassIndexByName(clxnCtsConfig, clnCtsConfig)
  g_classIndexCtsConfigHistory = getClassIndexByName(clxnCtsConfigHistory, clnCtsConfigHistory)
  g_classIndexCtsConfigTemplate = getClassIndexByName(clxnCtsConfigTemplate, clnCtsConfigTemplate)
  If supportSstCheck Then
    g_classIndexDataComparison = getClassIndexByName(clxnDataComparison, clnDataComparison)
    g_classIndexDataComparisonAttribute = getClassIndexByName(clxnDataComparisonAttribute, clnDataComparisonAttribute)
  End If
  g_classIndexDataFix = getClassIndexByName(clxnDataFix, clnDataFix)
  g_classIndexDataFixHistory = getClassIndexByName(clxnDataFixHistory, clnDataFixHistory)
  g_classIndexDataFixIgnored = getClassIndexByName(clxnDataFixIgnored, clnDataFixIgnored)
  g_classIndexDataFixPrecondition = getClassIndexByName(clxnDataFixPrecondition, clnDataFixPrecondition)
  g_classIndexDataHistory = getClassIndexByName(clxnDataHistory, clnDataHistory)
  g_classIndexDdlFix = getClassIndexByName(clxnDdlFix, clnDdlFix)
  g_classIndexDdlFixIgnored = getClassIndexByName(clxnDdlFixIgnored, clnDdlFixIgnored)
  g_classIndexDivision = getClassIndexByName(clxnDivision, clnDivision)
  g_classIndexMessage = getClassIndexByName(clxnMessage, clnMessage)
  g_classIndexDocuNews = getClassIndexByName(clxnDocuNews, clnDocuNews)
  g_classIndexDocuNewsType = getClassIndexByName(clxnDocuNewsType, clnDocuNewsType)
  g_classIndexEndSlot = getClassIndexByName(clxnEndSlot, clnEndSlot)
  g_classIndexExpression = getClassIndexByName(clxnExpression, clnExpression)
  g_classIndexGeneralSettings = getClassIndexByName(clxnGeneralSettings, clnGeneralSettings)
  g_classIndexGenericAspect = getClassIndexByName(clxnGenericAspect, clnGenericAspect)
  g_classIndexGenericCode = getClassIndexByName(clxnGenericCode, clnGenericCode)
  g_classIndexJob = getClassIndexByName(clxnJob, clnJob)
  g_classIndexLanguageSequence = getClassIndexByName(clxnLanguageSequence, clnLanguageSequence)
  g_classIndexLanguageSequenceElement = getClassIndexByName(clxnLanguageSequenceElement, clnLanguageSequenceElement)
  g_classIndexMasterAggregationSlot = getClassIndexByName(clxnMasterAggSlot, clnMasterAggSlot)
  g_classIndexMasterEndSlot = getClassIndexByName(clxnMasterEndSlot, clnMasterEndSlot)
  g_classIndexNotice = getClassIndexByName(clxnNotice, clnNotice)
  g_classIndexNSr1Validity = getClassIndexByName(clxnNSr1Validity, clnNSr1Validity)
  g_classIndexNumericProperty = getClassIndexByName(clxnNumericProperty, clnNumericProperty)
  g_classIndexPaiMessageLog = getClassIndexByName(clxnPaiMessageLog, clnPaiMessageLog)
  g_classIndexPricePreferences = getClassIndexByName(clxnPricePreferences, clnPricePreferences)
  g_classIndexProductStructure = getClassIndexByName(clxnProductStructure, clnProductStructure)
  g_classIndexProperty = getClassIndexByName(clxnProperty, clnProperty)
  g_classIndexPropertyAssignment = getClassIndexByName(clxnPropertyAssignment, clnPropertyAssignment)
  g_classIndexPropertyTemplate = getClassIndexByName(clxnPropertyTemplate, clnPropertyTemplate)
  g_classIndexProtocolLineEntry = getClassIndexByName(clxnProtocolLineEntry, clnProtocolLineEntry)
  g_classIndexProtocolParameter = getClassIndexByName(clxnProtocolParameter, clnProtocolParameter)
  g_classIndexPsDbMapping = getClassIndexByName(clxnPsDbMapping, clnPsDbMapping)
  g_classIndexRebateDefault = getClassIndexByName(clxnRebateDefault, clnRebateDefault)
  g_classIndexRegistryDynamic = getClassIndexByName(clxnRegistryDynamic, clnRegistryDynamic)
  g_classIndexRegistryStatic = getClassIndexByName(clxnRegistryStatic, clnRegistryStatic)
  g_classIndexRel2ProdLock = getClassIndexByName(clxnRel2ProdLock, clnRel2ProdLock)
  g_classIndexRel2ProdLockHistory = getClassIndexByName(clxnRel2ProdLockHistory, clnRel2ProdLockHistory)
  g_classIndexRssHistory = getClassIndexByName(clxnRssHistory, clnRssHistory)
  g_classIndexRssStatus = getClassIndexByName(clxnRssStatus, clnRssStatus)
  g_classIndexSlotBinaryPropertyAssignment = getClassIndexByName(clxnSlotBinaryPropertyAssignment, clnSlotBinaryPropertyAssignment)
  g_classIndexSlotBooleanPropertyAssignment = getClassIndexByName(clxnSlotBooleanPropertyAssignment, clnSlotBooleanPropertyAssignment)
  g_classIndexSlotNumericPropertyAssignment = getClassIndexByName(clxnSlotNumericPropertyAssignment, clnSlotNumericPropertyAssignment)
  g_classIndexSlotPlausibilityRule = getClassIndexByName(clxnSlotPlausibilityRule, clnSlotPlausibilityRule)
  g_classIndexSlotTextPropertyAssignment = getClassIndexByName(clxnSlotTextPropertyAssignment, clnSlotTextPropertyAssignment)
  g_classIndexSolverData = getClassIndexByName(clxnSolverData, clnSolverData)
  g_classIndexSr0Validity = getClassIndexByName(clxnSr0Validity, clnSr0Validity)
  g_classIndexSr1Validity = getClassIndexByName(clxnSr1Validity, clnSr1Validity)
  g_classIndexStandardCode = getClassIndexByName(clxnStandardCode, clnStandardCode)
  g_classIndexStandardEquipment = getClassIndexByName(clxnStandardEquipment, clnStandardEquipment)
  g_classIndexTaxParameter = getClassIndexByName(clxnTaxParameter, clnTaxParameter)
  g_classIndexTaxType = getClassIndexByName(clxnTaxType, clnTaxType)
  g_classIndexTechDataDeltaImport = getClassIndexByName(clxnTechDataDeltaImport, clnTechDataDeltaImport)
  g_classIndexTerm = getClassIndexByName(clxnTerm, clnTerm)
  g_classIndexTypePriceAssignment = getClassIndexByName(clxnTypePriceAssignment, clnTypePriceAssignment)
  g_classIndexTypeSpec = getClassIndexByName(clxnTypeSpec, clnTypeSpec)
  g_classIndexTypeStandardEquipment = getClassIndexByName(clxnTypeSpec, clnTypeStandardEquipment)
  g_classIndexView = getClassIndexByName(clxnView, clnView)
  
  If Left(snapshotApiVersion, 1) = "8" Then
    g_classIndexSnapshotAppl = getClassIndexByName(clxnSnapshotV8Appl, clnSnapshotV8Appl)
    g_classIndexSnapshotApplInfo = getClassIndexByName(clxnSnapshotV8ApplInfo, clnSnapshotV8ApplInfo)
    g_classIndexSnapshotLock = getClassIndexByName(clxnSnapshotV8Lock, clnSnapshotV8Lock)
    g_classIndexSnapshotLockWait = getClassIndexByName(clxnSnapshotV8LockWait, clnSnapshotV8LockWait)
    g_classIndexSnapshotStatement = getClassIndexByName(clxnSnapshotV8Statement, clnSnapshotV8Statement)
  ElseIf snapshotApiVersion = "9.7" Then
    g_classIndexSnapshotAppl = getClassIndexByName(clxnSnapshotV9Appl, clnSnapshotV9Appl)
    g_classIndexSnapshotApplInfo = getClassIndexByName(clxnSnapshotV9ApplInfo, clnSnapshotV9ApplInfo)
    g_classIndexSnapshotLock = getClassIndexByName(clxnSnapshotV9Lock, clnSnapshotV9Lock)
    g_classIndexSnapshotLockWait = getClassIndexByName(clxnSnapshotV9LockWait, clnSnapshotV9LockWait)
    g_classIndexSnapshotStatement = getClassIndexByName(clxnSnapshotV9Statement, clnSnapshotV9Statement)
  End If
  
  g_relIndexAggregationSlotHasNumericProperty = getRelIndexByName(rxnAggregationSlotHasNumericProperty, rnAggregationSlotHasNumericProperty)
  g_relIndexCategoryHasNumericProperty = getRelIndexByName(rxnCategoryHasNumericProperty, rnCategoryHasNumericProperty)
  g_relIndexCodeCategory = getRelIndexByName(rxnCodeCategory, rnCodeCategory)
  g_relIndexCpGroupHasProperty = getRelIndexByName(rxnCpGroupHasProperty, rnCpGroupHasProperty)
  g_relIndexSpGroupHasProperty = getRelIndexByName(rxnSpGroupHasProperty, rnSpGroupHasProperty)
  g_relIndexDisplaySlot = getRelIndexByName(rxnDisplaySlot, rnDisplaySlot)
  g_relIndexCountryGroupElement = getRelIndexByName(rxnCountryGroupElement, rnCountryGroupElement)
  g_relIndexCountryIdXRef = getRelIndexByName(rxnCountryIdXRef, rnCountryIdXRef)
  g_relIndexCodeValidForOrganization = getRelIndexByName(rxnCodeValidForOrganization, rnCodeValidForOrganization)
  g_relIndexNsr1ValidForOrganization = getRelIndexByName(rxnNsr1ValidForOrganization, rnNsr1ValidForOrganization)
  g_relIndexPropertyValidForOrganization = getRelIndexByName(rxnPropertyValidForOrganization, rnPropertyValidForOrganization)
  g_relIndexOrgManagesCountry = getRelIndexByName(rxnOrgManagesCountry, rnOrgManagesCountry)
  
  g_dbtLockRequestorId = getDataTypeByDomainIndex(g_domainIndexLockRequestorId)
  g_dbtR2pLockContext = getDataTypeByDomainIndex(g_domainIndexR2pLockContext)
  g_dbtCodeNumber = getDataTypeByDomainIndex(g_domainIndexCodeNumber)
  g_dbtChangeComment = getDataTypeByDomainIndex(g_domainIndexChangeComment)
  g_dbtLrtLabel = getDataTypeByDomainIndex(g_domainIndexLrtLabel)
  
  g_activePsOidDdl = g_dbtOid & "(" & gc_db2RegVarPsOidSafeSyntax & ")"
  
  g_migDataPoolIndex = getMigDataPoolIndex()
  g_migDataPoolId = getMigDataPoolId()
  g_productiveDataPoolIndex = getProductiveDataPoolIndex()
  g_productiveDataPoolId = getProductiveDataPoolId()
  g_archiveDataPoolIndex = getArchiveDataPoolIndex()
  g_archiveDataPoolId = getArchiveDataPoolId()
  'FIXME: get rid of hard-coding
  g_sim1DataPoolId = 5
  g_sim2DataPoolId = 6
End Sub


Sub initGlobalsByDdl_IVK( _
 ddlType As DdlTypeId _
)
  g_qualTabNameDataPoolAccessMode = genQualTabNameByEnumIndex(g_enumIndexDataPoolAccessMode, ddlType)
  g_qualTabNamePdmDataPoolType = genQualTabNameByEnumIndex(g_enumIndexPdmDataPoolType, ddlType)
  g_qualTabNamePdmOrganization = genQualTabNameByEnumIndex(g_enumIndexPdmOrganization, ddlType)
  g_qualTabNamePdmOrganizationNl = genQualTabNameByEnumIndex(g_enumIndexPdmOrganization, ddlType, , , True)
  g_qualTabNameStatus = genQualTabNameByEnumIndex(g_enumIndexStatus, ddlType)
  g_qualTabNameLanguage = genQualTabNameByEnumIndex(g_enumIndexLanguage, ddlType)

  g_qualTabNameApplHistory = genQualTabNameByClassIndex(g_classIndexApplHistory, ddlType)
  g_qualTabNameApplVersion = genQualTabNameByClassIndex(g_classIndexApplVersion, ddlType)
  g_qualTabNameClassIdPartitionBoundaries = genQualTabNameByClassIndex(g_classIndexClassIdPartitionBoundaries, ddlType)
  g_qualTabNameCleanJobs = genQualTabNameByClassIndex(g_classIndexCleanJobs, ddlType)
  g_qualTabNameCodeType = genQualTabNameByClassIndex(g_classIndexCodeType, ddlType)
  g_qualTabNameCountryIdList = genQualTabNameByClassIndex(g_classIndexCountryIdList, ddlType)
  g_qualTabNameCountrySpec = genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType)
  g_qualTabNameCtsConfig = genQualTabNameByClassIndex(g_classIndexCtsConfig, ddlType)
  g_qualTabNameCtsConfigTemplate = genQualTabNameByClassIndex(g_classIndexCtsConfigTemplate, ddlType)
  If supportSstCheck Then
    g_qualTabNameDataComparison = genQualTabNameByClassIndex(g_classIndexDataComparison, ddlType)
    g_qualTabNameDataComparisonAttribute = genQualTabNameByClassIndex(g_classIndexDataComparisonAttribute, ddlType)
  End If
  g_qualTabNameDataFix = genQualTabNameByClassIndex(g_classIndexDataFix, ddlType)
  g_qualTabNameDataFixHistory = genQualTabNameByClassIndex(g_classIndexDataFixHistory, ddlType)
  g_qualTabNameDataFixIgnored = genQualTabNameByClassIndex(g_classIndexDataFixIgnored, ddlType)
  g_qualTabNameDataFixPrecondition = genQualTabNameByClassIndex(g_classIndexDataFixPrecondition, ddlType)
  g_qualTabNameDataHistory = genQualTabNameByClassIndex(g_classIndexDataHistory, ddlType)
  g_qualTabNameDivision = genQualTabNameByClassIndex(g_classIndexDivision, ddlType)
  g_qualTabNameMessage = genQualTabNameByClassIndex(g_classIndexMessage, ddlType)
  g_qualTabNameLanguageSequence = genQualTabNameByClassIndex(g_classIndexLanguageSequence, ddlType)
  g_qualTabNameLanguageSequenceElement = genQualTabNameByClassIndex(g_classIndexLanguageSequenceElement, ddlType)
  g_qualTabNamePaiMessageLog = genQualTabNameByClassIndex(g_classIndexPaiMessageLog, ddlType)
  g_qualTabNamePricePreferencesCto = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType)
  g_qualTabNameProductStructure = genQualTabNameByClassIndex(g_classIndexProductStructure, ddlType)
  g_qualTabNameProductStructureNl = genQualTabNameByClassIndex(g_classIndexProductStructure, ddlType, , , , , , True)
  g_qualTabNamePropertyTemplate = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType)
  g_qualTabNamePropertyTemplateNl = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, , , , , , True)
  g_qualTabNamePsDpMapping = genQualTabNameByClassIndex(g_classIndexPsDbMapping, ddlType)
  g_qualTabNameRebateDefault = genQualTabNameByClassIndex(g_classIndexRebateDefault, ddlType)
  g_qualTabNameRegistryDynamic = genQualTabNameByClassIndex(g_classIndexRegistryDynamic, ddlType)
  g_qualTabNameRegistryStatic = genQualTabNameByClassIndex(g_classIndexRegistryStatic, ddlType)
  g_qualTabNameRel2ProdLock = genQualTabNameByClassIndex(g_classIndexRel2ProdLock, ddlType)
  g_qualTabNameRel2ProdLockHistory = genQualTabNameByClassIndex(g_classIndexRel2ProdLockHistory, ddlType)
  g_qualTabNameRssHistory = genQualTabNameByClassIndex(g_classIndexRssHistory, ddlType)
  g_qualTabNameRssStatus = genQualTabNameByClassIndex(g_classIndexRssStatus, ddlType)
  
  g_qualTabNameCountryIdXRef = genQualTabNameByRelIndex(g_relIndexCountryIdXRef, ddlType)
  g_qualTabNameOrgManagesCountry = genQualTabNameByRelIndex(g_relIndexOrgManagesCountry, ddlType)

  g_anIsInUseByFto = genAttrName(conIsInUseByFto, ddlType)
  g_anIsActive = genAttrName(conIsActive, ddlType)
  g_anAllowedCountries = genAttrName(conAllowedCountries, ddlType)
  g_anDisAllowedCountries = genAttrName(conDisAllowedCountries, ddlType)
  g_anCodeNumber = genAttrName(conCodeNumber, ddlType)
  g_anSlotType = genAttrName(conSlotTypeId, ddlType)
  g_anSr0Context = genAttrName(conSr0Context, ddlType)
  g_anIsCentralDataTransfer = genAttrName(conIsCentralDataTransfer, ddlType)
  g_anIsUnderConstruction = genAttrName(conIsUnderConstruction, ddlType)
  g_anChangeComment = genAttrName(conChangeComment, ddlType)
  g_anHasBeenSetProductive = genAttrName(conHasBeenSetProductive, ddlType)
  g_anIsPsForming = genAttrName(conIsPsForming, ddlType)
  g_anIsDeleted = genAttrName(conIsDeleted, ddlType)
  g_anIsDeletable = genAttrName(conIsDeletable, ddlType)
  g_anIsNotPublished = genAttrName(conIsNotPublished, ddlType)
  g_anIsStandard = genAttrName(conIsStandard, ddlType)
  g_anIsDefault = genAttrName(conIsDefault, ddlType)
  g_anIsDuplicating = genAttrName(conIsDuplicating, ddlType)
  g_anIsBlockedFactory = genAttrName(conIsBlockedFactory, ddlType)
  g_anIsBlockedPrice = genAttrName(conIsBlockedPrice, ddlType)
  g_anIsNational = genAttrName(conIsNational, ddlType)
  g_anLrtComment = genAttrName(conLrtComment, ddlType)
  g_anPsOid = genAttrName(conPsOid, ddlType)
  g_anDivOid = genAttrName(conDivOid, ddlType)
  g_anValidFrom = genAttrName(conValidFrom, ddlType)
  g_anValidTo = genAttrName(conValidTo, ddlType)
  g_anHasConflict = genAttrName(conHasConflict, ddlType)
  g_anSequenceNumber = genAttrName(conSequenceNumber, ddlType)
  g_anLockValueOld = genAttrName(conLockValueOld, ddlType)
  g_anLockValueNew = genAttrName(conLockValueNew, ddlType)
  g_anLockMode = genAttrName(conLockMode, ddlType)
  g_anLockContext = genAttrName(conLockContext, ddlType)
  g_anLockOperation = genAttrName(conLockOperation, ddlType)
  g_anLockTimestamp = genAttrName(conLockTimestamp, ddlType)
  g_anRuleScope = genAttrName(conRuleScopeId, ddlType)
  g_anRebateValueType = genAttrName(conRebateValueType, ddlType)
  g_anSection = genAttrName(conSection, ddlType)
  g_anKey = genAttrName(conKey, ddlType)
  g_anSubKey = genAttrName(conSubKey, ddlType)
  g_anValue = genAttrName(conValue, ddlType)
  g_anMessageId = genAttrName(conMessageId, ddlType)
  g_anName = genAttrName(conName, ddlType)
  g_anEventType = genAttrName(conEventType, ddlType)
  
  g_anAcmIsPs = genAttrName(conAcmIsPs, ddlType)
  g_anAcmIsPsForming = genAttrName(conAcmIsPsForming, ddlType)
  g_anAcmSupportXmlExport = genAttrName(conAcmSupportXmlExport, ddlType)
  g_anAcmUseXmlExport = genAttrName(conAcmUseXmlExport, ddlType)
  g_anAcmIsNt2m = genAttrName(conAcmIsNt2m, ddlType)
  
  g_anAcmLrtActivationType = genAttrName(conAcmLrtActivationType, ddlType)
  g_anAcmDisplayCategory = genAttrName(conAcmDisplayCategory, ddlType)
  g_anAcmIsArch = genAttrName(conAcmIsArch, ddlType)
  g_anAcmIsPriceRelated = genAttrName(conAcmIsPriceRelated, ddlType)
  g_anAcmUseFtoPostProcess = genAttrName(conAcmUseFtoPostProcess, ddlType)
  g_anAcmCondenseData = genAttrName(conAcmCondenseData, ddlType)
  g_anAcmEntityFilterEnumCriteria = genAttrName(conAcmEntityFilterEnumCriteria, ddlType)
  
  g_anConflictTypeId = genAttrName(conConflictTypeId, ddlType)
  g_anConflictStateId = genAttrName(conConflictStateId, ddlType)
  
  g_qualFuncNameStrElems = genQualFuncName(g_sectionIndexMeta, udfnStrElems, ddlType)
  g_qualFuncNameGetLrtTargetStatus = genQualFuncName(g_sectionIndexLrt, udfnGetLrtTargetStatus, ddlType)
  g_qualFuncNameIsNumeric = genQualFuncName(g_sectionIndexMeta, udfnIsNumeric, ddlType, , , , , , True)
End Sub
' ### ENDIF IVK ###







