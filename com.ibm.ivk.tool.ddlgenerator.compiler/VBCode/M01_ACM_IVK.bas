Attribute VB_Name = "M01_ACM_IVK"
' ### IF IVK ###

' ############################################
' # section meta information
' ############################################

' sn   - section name
' ssn  - section short name

Global Const snAspect = "Aspect"
Global Const ssnAspect = "ASP"
Global Const snDecision = "Decision"
Global Const ssnDecision = "DEC"
Global Const snExpression = "Expression"
Global Const ssnExpression = "EXP"
Global Const snFactoryTakeover = "FactoryTakeover"
Global Const ssnFactoryTakeover = "FTO"
Global Const snMessage = "Message"
Global Const ssnMessage = "ERR"
Global Const snProtocol = "Protocol"
Global Const ssnProtocol = "PRO"
Global Const snCode = "Code"
Global Const ssnCode = "CDE"
Global Const snDataCheck = "DataCheck"
Global Const ssnDataCheck = "CHK"
Global Const snAliasPsDpFiltered = "ALIAS_LRT_PDF"
Global Const ssnAliasPsDpFiltered = "ALI"
Global Const snAliasPsDpFilteredExtended = "ALIAS_LRT_PDFX"
Global Const ssnAliasPsDpFilteredExtended = "ALJ"
Global Const snAliasDelObj = "ALIAS_LRT_DEL"
Global Const ssnAliasDelObj = "ALD"
Global Const snProductStructure = "PS"
Global Const ssnProductStructure = "PST"
Global Const snStaging = "Staging"
Global Const ssnStaging = "STA"
Global Const snOrder = "Order"
Global Const ssnOrder = "ORD"
Global Const snPaiLog = "PaiLog"
Global Const ssnPaiLog = "PLG"
Global Const snPricing = "Pricing"
Global Const ssnPricing = "PRI"
Global Const snReport = "Report"
Global Const ssnReport = "REP"
Global Const snDataFix = "DataFix"
Global Const ssnDataFix = "DFX"
Global Const snSetProductive = "SetProductive"
Global Const ssnSetProductive = "SPR"

Global Const snFwkTest = "FwkTest"

' ############################################
' # domain meta information
' ############################################

' dn   - domain name
' dsn  - domain short name

Global Const dnBIBRegistryValue = "BIBRegistryValue":       Global Const dxnBIBRegistryValue = snStaging
Global Const dnBinaryPropertyValue = "BinaryPropertyValue": Global Const dxnBinaryPropertyValue = snProductStructure
Global Const dnCodeNumber = "CodeNumber":                   Global Const dxnCodeNumber = snCode
Global Const dnCountryIdList = "CountryIdList":             Global Const dxnCountryIdList = "Country"
Global Const dnEndSlotLabel = "EndSlotLabel":               Global Const dxnEndSlotLabel = snProductStructure
Global Const dnLockRequestorId = "LockRequestorId":         Global Const dxnLockRequestorId = snMeta
Global Const dnLongText = "LongText":                       Global Const dxnLongText = snStaging
Global Const dnR2pLockContext = "Rel2ProdLockContext":      Global Const dxnR2pLockContext = snMeta
Global Const dnReportFileData = "ReportFileData":           Global Const dxnReportFileData = snReport
Global Const dnTemplateFileData = "TemplateFileData":       Global Const dxnTemplateFileData = snReport
Global Const dnValTimestamp = "ValidityTimestamp":          Global Const dxnValTimestamp = snCommon
Global Const dnXmlRecord = "XmlCLob":                       Global Const dxnXmlRecord = snCommon

' ############################################
' # column meta information
' ############################################

' con   - column name
' cosn  - column short name

Global Const conAllowedCountries = "allowedCountries"
Global Const conDisAllowedCountries = "disallowedCountries"
Global Const conIsActive = "isActive"
Global Const conIsInUseByFto = "isInUseByFto"
Global Const conIsInvalid = "isInvalid"
Global Const conPsOid = "PS_OID":                                         Global Const cosnPsOid = "poi"
Global Const conDivOid = "DIV_OID":                                       Global Const cosnDivOid = "doi"
Global Const conIsCentralDataTransfer = "isCentralDataTransfer"
Global Const conIsUnderConstruction = "isUnderConstruction"
Global Const conHasConflict = "hasConflict"
Global Const conIsDeletable = "isDeletable"
Global Const conIsStandard = "isStandard"
Global Const conIsDefault = "isDefault"
Global Const conIsDuplicating = "isDuplicating"
Global Const conIsBlockedFactory = "isBlockedFactory"
Global Const conIsBlockedPrice = "isBlockedPrice"
Global Const conPdmOrgIsPrimary = "isFactory"
Global Const conXmlRecord = "record":                                     Global Const cosnXmlRecord = "rec"
Global Const conIsNational = "isNational":                                Global Const cosnIsNational = "ina"
Global Const conIsNationalActive = "isNatActive":                         Global Const cosnIsNationalActive = "ina"
Global Const conIsRebateEnabled = "isRebateEnabled"
Global Const conIsBlockedNational = "isBlockedNational"
Global Const conNotVisibleFactory = "notVisibleFactory"
Global Const conNotVisibleNational = "notVisibleNational"
Global Const conIsCommissionDeductible = "isCommissionDeductible"
Global Const conDisplayOrder = "displayOrder"
Global Const conIsPsForming = "isPsForming"
Global Const conIsLinked = "isLinked"
Global Const conIsBaseSlot = "isBaseSlot"
Global Const conSr0Context = "sr0Context"
Global Const conHasBeenSetProductive = "hasBeenSetProductive":            Global Const cosnHasBeenSetProductive = "spr"
Global Const conIsDeleted = "isDeleted":                                  Global Const cosnIsDeleted = "ide"
Global Const conNationalDisabled = "nationalDisabled"
Global Const conIsNotPublished = "isNotPublished"
Global Const conIsCabin = "isCabin"
Global Const conIsOrderField1 = "isOrderField1"
Global Const conIsOrderField2 = "isOrderField2"
Global Const conIsOrderField3 = "isOrderField3"
Global Const conIsOrderField4 = "isOrderField4"
Global Const conIsOrderField5 = "isOrderField5"
Global Const conIsAEF = "isAEF"
Global Const conCodeNumber = "codeNumber"
Global Const conIsMassUpdate = "isMassUpdate"
Global Const conPaiEntitlementGroupId = "paiEntitlementGroupId"
Global Const conIsReportingSchema = "isReportingSchema"
Global Const conIsMpc = "isMpc"
Global Const conIsDcVd = "isDcVd"
Global Const conSequenceNumber = "sequenceNumber"
Global Const conLockValueOld = "lockValueOld"
Global Const conLockValueNew = "lockValueNew"
Global Const conLockMode = "lockMode"
Global Const conLockContext = "lockContext"
Global Const conLockTimestamp = "lockTimestamp"
Global Const conLockOperation = "lockOperation"
Global Const conCurrencyFactor = "currencyFactor"
Global Const conCurrency = "currency"
Global Const conRebateValueType = "rebateValueType"
Global Const conRebateValueCode = "rebateValueCode"
Global Const conServiceType = "serviceType"
Global Const conSection = "section"
Global Const conSubKey = "subKey"
Global Const conKey = "key"
Global Const conValue = "value"
Global Const conContent = "content"
Global Const conFileName = "fileName"
Global Const conComment = "comment"
Global Const conDpClassNumber = "dpClassNumber"
Global Const conDocumentationFlagFactory = "documentationFlagFactory"
Global Const conBaumusterGroup = "baumusterGroup"
Global Const conOrderAcceptanceFrom = "orderAcceptanceFrom"
Global Const conOrderAcceptanceUntil = "orderAcceptanceUntil"
Global Const conMessageId = "messageId"
Global Const conName = "name"
Global Const conLabel = "label"
Global Const conLabelNational = "label_national"
Global Const conLabelIsNatActive = "label_isNatActive"
Global Const conEventType = "eventType"

Global Const conAbhCode = "abhCode"
Global Const conAssignedPaintZoneKey = "assignedPaintZoneKey"
Global Const conCardinality = "cardinality"
Global Const conCity = "city"
Global Const conClassification = "classification"
Global Const conClientAcctng = "client_acctng"
Global Const conClientApplName = "client_applname"
Global Const conClientUserId = "client_userid"
Global Const conClientWrkstnName = "client_wrkstnname"

Global Const conCodePriority = "codePriority"
Global Const conContactPerson = "contactPerson"
Global Const conPdmDeletedObjectSchemaName = "deletedObjectSchemaName"
Global Const conDigitsAfterDecimalPoint = "digitsAfterDecimalPoint"
Global Const conEMail = "eMail"
Global Const conFax = "fax"
Global Const conFon = "fon"
Global Const conId = "id"
Global Const conIsEstimationRelevant = "isEstimationRelevant"
Global Const conIsMotorVehicleCertificationRelevant = "isMotorVehicleCertificationRel"
Global Const conIsNsr1Slot = "isNsr1Slot"
Global Const conIsolation = "isolation"
Global Const conIsProductionRelevant = "isProductionRelevant"
Global Const conIsRequired = "isRequired"
Global Const conIsSideCosts = "isSideCosts"
Global Const conIsSr0Slot = "isSr0Slot"
Global Const conIsSr1Slot = "isSr1Slot"
Global Const conIsTaxRelevant = "isTaxRelevant"
Global Const conIsViewForming = "isViewForming"
Global Const conMessage = "message"
Global Const conNsr1Order = "nsr1Order"
Global Const conPath = "path"
Global Const conPsDpFilteredSchemaName = "psDpFilteredSchemaName"
Global Const conPsDpFilteredSchemaNameSparte = "psDpFilteredSchemaNameSparte"
Global Const conReturnUnit = "returnUnit"
Global Const conSchema = "schema"
Global Const conSlotIndex = "slotIndex"
Global Const conSr0Order = "sr0Order"
Global Const conSr1Order = "sr1Order"
Global Const conState = "state"
Global Const conStreet = "street"

Global Const conType = "type"
Global Const conUnit = "unit"
Global Const conUser = "user"
Global Const conZipCode = "zipCode"
Global Const conLastCentralDataTransferBegin = "lastCentralDataTransferBegin"
Global Const conMaxLength = "maxLength"
Global Const conCategoryKindId = "categoryKind_id"
Global Const conCodeCharacterId = "codeCharacter_id"
Global Const conPackageTypeId = "packageType_id"
Global Const conPaintHandlingModeId = "paintHandlingMode_id"
Global Const conVehicleTotalPriceCalculationId = "vehicleTotalPriceCalculation_i"
Global Const conReturnPropertyFormatId = "returnPropertyFormat_id"
Global Const conPriceLogicId = "priceLogic_id"
Global Const conTypeId = "type_id"

Global Const conConflictTypeId = "conflictType_id"
Global Const conConflictStateId = "conflictState_id"

Global Const conMaturityLevelId = "maturityLevel_id"
Global Const conStatusId = "status_id"
Global Const conRuleScopeId = "ruleScope_Id"
Global Const conSlotTypeId = "slotType_Id"
Global Const conPrimaryPriceTypeForTestId = "primaryPriceForTest_Id"
Global Const conPriceSelectionForOverlapId = "priceSelectionForOverlap_Id"

Global Const conAcmIsPs = "isPs"
Global Const conAcmIsPsForming = "isPsForming"
Global Const conAcmSupportXmlExport = "supportXmlExport"
Global Const conAcmUseXmlExport = "useXmlExport"

Global Const conAcmLrtActivationType = "lrtActivationType"
Global Const conAcmDisplayCategory = "displayCategory"
Global Const conAcmIsArch = "isArch"
Global Const conAcmIsPriceRelated = "isPriceRelated"
Global Const conAcmUseFtoPostProcess = "useFtoPostProcess"
Global Const conAcmCondenseData = "condenseData"
Global Const conAcmEntityFilterEnumCriteria = "entityFilterEnumCriteria"
Global Const conAcmIsNt2m = "isNt2m"

' ############################################
' # enum meta information
' ############################################

' en   - enum name
' esn  - enum short name
' exn  - enum section name

Global Const enPdmOrganization = "PdmOrganization"
Global Const esnPdmOrganization = "POR"
Global Const exnPdmOrganization = snDbMeta

Global Const enPdmDataPoolType = "PdmDataPoolType"
Global Const esnPdmDataPoolType = "PDT"
Global Const exnPdmDataPoolType = snDbMeta

Global Const enStatus = "Status"
Global Const esnStatus = "sta"
Global Const exnStatus = snCommon

' ############################################
' # class meta information
' ############################################

' cln   - class name
' clxn  - class section name

Global Const clnActionHeading = "ActionHeading":                                   Global Const clxnActionHeading = snDecision
Global Const clnActionElement = "ActionElement":                                   Global Const clxnActionElement = snDecision
Global Const clnAggSlot = "AggregationSlot":                                       Global Const clxnAggSlot = snProductStructure
Global Const clnApplHistory = "ApplHistory":                                       Global Const clxnApplHistory = "DbAdmin"
Global Const clnApplVersion = "ApplVersion":                                       Global Const clxnApplVersion = "DbAdmin"
Global Const clnArchLog = "ArchiveLog":                                            Global Const clxnArchLog = "Meta"
Global Const clnCalculationRun = "CalculationRun":                                 Global Const clxnCalculationRun = snAspect
Global Const clnCategory = "Category":                                             Global Const clxnCategory = snProductStructure
Global Const clnChangeLogStatus = "ChangelogStatus":                               Global Const clxnChangeLogStatus = snChangeLog
Global Const clnChangeLogVdokf = "ChangelogVDokF":                                 Global Const clxnChangeLogVdokf = snChangeLog
Global Const clnClassIdPartitionBoundaries = "ClassIdPartitionBoundaries":         Global Const clxnClassIdPartitionBoundaries = snDbAdmin
Global Const clnCodeBinaryPropertyAssignment = "CodeBinaryPropertyAssignment":     Global Const clxnCodeBinaryPropertyAssignment = snAspect
Global Const clnCodeBooleanPropertyAssignment = "CodeBooleanPropertyAssignment":   Global Const clxnCodeBooleanPropertyAssignment = snAspect
Global Const clnCodeLabelConflict = "CodeLabelConflict":                           Global Const clxnCodeLabelConflict = snFactoryTakeover
Global Const clnCodeNumericPropertyAssignment = "CodeNumericPropertyAssignment":   Global Const clxnCodeNumericPropertyAssignment = snAspect
Global Const clnCodePlausibilityRule = "CodePlausibilityRule":                     Global Const clxnCodePlausibilityRule = snAspect
Global Const clnCodePriceAssignment = "CodePriceAssignment":                       Global Const clxnCodePriceAssignment = snAspect
Global Const clnCodePropertyAssignmentConflict = "CodePropertyAssignmentConflict": Global Const clxnCodePropertyAssignmentConflict = snFactoryTakeover
Global Const clnCodePropertyGroup = "CodePropertyGroup":                           Global Const clxnCodePropertyGroup = snProductStructure
Global Const clnCodeTextPropertyAssignment = "CodeTextPropertyAssignment":         Global Const clxnCodeTextPropertyAssignment = snAspect
Global Const clnCodeType = "CodeType":                                             Global Const clxnCodeType = snCode
Global Const clnConditionHeading = "ConditionHeading":                             Global Const clxnConditionHeading = snDecision
Global Const clnConflict = "Conflict":                                             Global Const clxnConflict = snFactoryTakeover
Global Const clnCountry = "Country":                                               Global Const clxnCountry = snCountry
Global Const clnCountryContextAspect = "CountryContextAspect":                     Global Const clxnCountryContextAspect = snAspect
Global Const clnCountryIdList = "CountryIdList":                                   Global Const clxnCountryIdList = snCountry
Global Const clnCountrySpec = "CountrySpec":                                       Global Const clxnCountrySpec = snCountry
Global Const clnCtsConfig = "CtsConfig":                                           Global Const clxnCtsConfig = snMeta
Global Const clnCtsConfigHistory = "CtsConfigHistory":                             Global Const clxnCtsConfigHistory = snMeta
Global Const clnCtsConfigTemplate = "CtsConfigTemplate":                           Global Const clxnCtsConfigTemplate = snMeta
Global Const clnDataComparison = "DataComparison":                                 Global Const clxnDataComparison = snDataCheck
Global Const clnDataComparisonAttribute = "DataComparisonAttribute":               Global Const clxnDataComparisonAttribute = snDataCheck
Global Const clnDataFix = "DataFix":                                               Global Const clxnDataFix = "DbAdmin"
Global Const clnDataFixHistory = "DataFixHistory":                                 Global Const clxnDataFixHistory = "DbAdmin"
Global Const clnDataFixIgnored = "DataFixIgnored":                                 Global Const clxnDataFixIgnored = "DbAdmin"
Global Const clnDataFixPrecondition = "DataFixPrecondition":                       Global Const clxnDataFixPrecondition = "DbAdmin"
Global Const clnDataHistory = "DataHistory":                                       Global Const clxnDataHistory = snMeta
Global Const clnDdlFix = "DdlFix":                                                 Global Const clxnDdlFix = "DbAdmin"
Global Const clnDdlFixIgnored = "DdlFixIgnored":                                   Global Const clxnDdlFixIgnored = "DbAdmin"
Global Const clnDecisionTable = "DecisionTable":                                   Global Const clxnDecisionTable = snDecision
Global Const clnDivision = "Division":                                             Global Const clxnDivision = "Org"
Global Const clnMessage = "Message":                                               Global Const clxnMessage = snMessage
Global Const clnDocuNews = "DocumentationNews":                                    Global Const clxnDocuNews = "DocuNews"
Global Const clnDocuNewsType = "DocumentationNewsType":                            Global Const clxnDocuNewsType = "DocuNews"
Global Const clnEndSlot = "EndSlot":                                               Global Const clxnEndSlot = snProductStructure
Global Const clnExpression = "Expression":                                         Global Const clxnExpression = snExpression
Global Const clnFtoChangelogSummary = "FtoChangelogSummary":                       Global Const clxnFtoChangelogSummary = snTrace
Global Const clnFtoOrgChangelogSummary = "FtoMpcChangelogSummary":                 Global Const clxnFtoOrgChangelogSummary = snTrace
Global Const clnFtoOrgImplicitChangesSummary = "FtoMpcImplicitChangesSummary":     Global Const clxnFtoOrgImplicitChangesSummary = snTrace
Global Const clnGeneralPriceConflict = "GeneralPriceConflict":                     Global Const clxnGeneralPriceConflict = snFactoryTakeover
Global Const clnGeneralSettings = "GeneralSettings":                               Global Const clxnGeneralSettings = "Meta"
Global Const clnGenericAspect = "GenericAspect":                                   Global Const clxnGenericAspect = snAspect
Global Const clnGenericCode = "GenericCode":                                       Global Const clxnGenericCode = snCode

Global Const clnJob = "Job":                                                       Global Const clxnJob = "Meta"
Global Const clnLanguageSequence = "LanguageSequence":                             Global Const clxnLanguageSequence = snCountry
Global Const clnLanguageSequenceElement = "LanguageSequenceElement":               Global Const clxnLanguageSequenceElement = snCountry
Global Const clnMasterAggSlot = "MasterAggregationSlot":                           Global Const clxnMasterAggSlot = snProductStructure
Global Const clnMasterEndSlot = "MasterEndSlot":                                   Global Const clxnMasterEndSlot = snProductStructure
Global Const clnMdsInbox = "MDSInbox":                                             Global Const clxnMdsInbox = "MDSInbox"
Global Const clnNSr1Validity = "NSR1Validity":                                     Global Const clxnNSr1Validity = snAspect
Global Const clnNotice = "Notice":                                                 Global Const clxnNotice = "Notice"
Global Const clnNumericProperty = "NumericProperty":                               Global Const clxnNumericProperty = snProductStructure
Global Const clnOrganization = "Organization":                                     Global Const clxnOrganization = snCountry
Global Const clnPaiMessageLog = "MessageLog":                                      Global Const clxnPaiMessageLog = snPaiLog
Global Const clnPlausibilityRuleConflict = "PlausibilityRuleConflict":             Global Const clxnPlausibilityRuleConflict = snFactoryTakeover
Global Const clnPricePreferences = "PricePreferences":                             Global Const clxnPricePreferences = snMeta
Global Const clnProductStructure = "ProductStructure":                             Global Const clxnProductStructure = snProductStructure
Global Const clnProperty = "Property":                                             Global Const clxnProperty = snProductStructure
Global Const clnPropertyAssignment = "PropertyAssignment":                         Global Const clxnPropertyAssignment = snAspect
Global Const clnPropertyTemplate = "PropertyTemplate":                             Global Const clxnPropertyTemplate = snProductStructure
Global Const clnProtocolLineEntry = "ProtocolLineEntry":                           Global Const clxnProtocolLineEntry = snProtocol
Global Const clnProtocolParameter = "ProtocolParameter":                           Global Const clxnProtocolParameter = snProtocol
Global Const clnPsDbMapping = "PsDpMapping":                                       Global Const clxnPsDbMapping = snDbMeta
Global Const clnRebateDefault = "RebateDefault":                                   Global Const clxnRebateDefault = "Meta"
Global Const clnRegistryDynamic = "RegistryDynamic":                               Global Const clxnRegistryDynamic = "Meta"
Global Const clnRegistryStatic = "RegistryStatic":                                 Global Const clxnRegistryStatic = "Meta"
Global Const clnRel2ProdLock = "Rel2ProdLock":                                     Global Const clxnRel2ProdLock = snMeta
Global Const clnRel2ProdLockHistory = "Rel2ProdLockHistory":                       Global Const clxnRel2ProdLockHistory = snMeta
Global Const clnRssHistory = "RssHistory":                                         Global Const clxnRssHistory = snPaiLog
Global Const clnRssStatus = "RssStatus":                                           Global Const clxnRssStatus = snPaiLog
Global Const clnSetProdAffectedEntity = "SpAffectedEntity":                        Global Const clxnSetProdAffectedEntity = snDbMeta
Global Const clnSlotBinaryPropertyAssignment = "SlotBinaryPropertyAssignment":     Global Const clxnSlotBinaryPropertyAssignment = snAspect
Global Const clnSlotBooleanPropertyAssignment = "SlotBooleanPropertyAssignment":   Global Const clxnSlotBooleanPropertyAssignment = snAspect
Global Const clnSlotNumericPropertyAssignment = "SlotNumericPropertyAssignment":   Global Const clxnSlotNumericPropertyAssignment = snAspect
Global Const clnSlotPlausibilityRule = "SlotPlausibilityRule":                     Global Const clxnSlotPlausibilityRule = snAspect
Global Const clnSlotPropertyAssignmentConflict = "SlotPropertyAssignmentConflict": Global Const clxnSlotPropertyAssignmentConflict = snFactoryTakeover
Global Const clnSlotTextPropertyAssignment = "SlotTextPropertyAssignment":         Global Const clxnSlotTextPropertyAssignment = snAspect
Global Const clnSnapshotV8Agent = "Snapshot_Agent":                                Global Const clxnSnapshotV8Agent = snDbMonitor
Global Const clnSnapshotV8Appl = "Snapshot_Appl":                                  Global Const clxnSnapshotV8Appl = snDbMonitor
Global Const clnSnapshotV8ApplInfo = "Snapshot_ApplI":                             Global Const clxnSnapshotV8ApplInfo = snDbMonitor
Global Const clnSnapshotV8BufferPool = "Snapshot_Bp":                              Global Const clxnSnapshotV8BufferPool = snDbMonitor
Global Const clnSnapshotV8Container = "Snapshot_Cnt":                              Global Const clxnSnapshotV8Container = snDbMonitor
Global Const clnSnapshotV8Db = "Snapshot_Db":                                      Global Const clxnSnapshotV8Db = snDbMonitor
Global Const clnSnapshotV8Dbm = "Snapshot_Dbm":                                    Global Const clxnSnapshotV8Dbm = snDbMonitor
Global Const clnSnapshotV8Lock = "Snapshot_Lock":                                  Global Const clxnSnapshotV8Lock = snDbMonitor
Global Const clnSnapshotV8LockWait = "Snapshot_LockWt":                            Global Const clxnSnapshotV8LockWait = snDbMonitor
Global Const clnSnapshotV8Sql = "Snapshot_Sql":                                    Global Const clxnSnapshotV8Sql = snDbMonitor
Global Const clnSnapshotV8Statement = "Snapshot_Stmnt":                            Global Const clxnSnapshotV8Statement = snDbMonitor
Global Const clnSnapshotV8Table = "Snapshot_Table":                                Global Const clxnSnapshotV8Table = snDbMonitor
Global Const clnSnapshotV8Tbs = "Snapshot_Tbs":                                    Global Const clxnSnapshotV8Tbs = snDbMonitor
Global Const clnSnapshotV8TbsCfg = "Snapshot_TbsCfg":                              Global Const clxnSnapshotV8TbsCfg = snDbMonitor
Global Const clnSolverData = "SolverData":                                         Global Const clxnSolverData = "Meta"
Global Const clnSpAffectedEntity = "SpAffectedEntity":                             Global Const clxnSpAffectedEntity = snTrace
Global Const clnSpFilteredEntity = "SpFilteredEntity":                             Global Const clxnSpFilteredEntity = snTrace
Global Const clnSpStatement = "SpStatement":                                       Global Const clxnSpStatement = snTrace
Global Const clnSr0Validity = "SR0Validity":                                       Global Const clxnSr0Validity = snAspect
Global Const clnSr1Validity = "SR1Validity":                                       Global Const clxnSr1Validity = snAspect
Global Const clnStandardCode = "StandardCode":                                     Global Const clxnStandardCode = "Code"
Global Const clnStandardEquipment = "StandardEquipment":                           Global Const clxnStandardEquipment = snAspect
Global Const clnTaxParameter = "TaxParameter":                                     Global Const clxnTaxParameter = snPricing
Global Const clnTaxType = "TaxType":                                               Global Const clxnTaxType = snPricing
Global Const clnTechDataDeltaImport = "TechDataDeltaImport":                       Global Const clxnTechDataDeltaImport = snStaging
Global Const clnTerm = "Term":                                                     Global Const clxnTerm = snExpression
Global Const clnTypeConflict = "TypeConflict":                                     Global Const clxnTypeConflict = snFactoryTakeover
Global Const clnTypeLabelConflict = "TypeLabelConflict":                           Global Const clxnTypeLabelConflict = snFactoryTakeover
Global Const clnTypePriceAssignment = "TypePriceAssignment":                       Global Const clxnTypePriceAssignment = snAspect
Global Const clnTypeSpec = "TypeSpec":                                             Global Const clxnTypeSpec = snAspect
Global Const clnTypeStandardEquipment = "TypeStandardEquipment":                   Global Const clxnTypeStandardEquipment = snAspect
Global Const clnUser = "MDSUser":                                                  Global Const clxnUser = snUser
Global Const clnView = "View":                                                     Global Const clxnView = snUser

' ############################################
' # relationship meta information
' ############################################

' rn   - class name
' rxn  - class section name

Global Const rnAggregationSlotHasNumericProperty = "AggregationSlotHasNumericProperty": Global Const rxnAggregationSlotHasNumericProperty = snProductStructure
Global Const rnAllowedCountriesAspect = "AllowedCountriesAspect":                       Global Const rxnAllowedCountriesAspect = snAspect
Global Const rnCategoryHasNumericProperty = "CategoryHasNumericProperty":               Global Const rxnCategoryHasNumericProperty = snProductStructure
Global Const rnCodeCategory = "CodeCategory":                                           Global Const rxnCodeCategory = snCode
Global Const rnCountryGroupElement = "CountryGroupElement":                             Global Const rxnCountryGroupElement = "Country"
Global Const rnCountryIdXRef = "CountryIdXRef":                                         Global Const rxnCountryIdXRef = snCountry
Global Const rnCpGroupHasProperty = "CpGroupHasProperty":                               Global Const rxnCpGroupHasProperty = snProductStructure
Global Const rnSpGroupHasProperty = "SpGroupHasProperty":                               Global Const rxnSpGroupHasProperty = snProductStructure
Global Const rnDisallowedCountriesAspect = "DisallowedCountriesAspect":                 Global Const rxnDisallowedCountriesAspect = snAspect
Global Const rnDisplaySlot = "DisplaySlot":                                             Global Const rxnDisplaySlot = snUser
Global Const rnMessageSeverity = "MessageSeverity":                                     Global Const rxnMessageSeverity = snMessage
Global Const rnNsr1ValidForOrganization = "Nsr1ValidForOrganization":                   Global Const rxnNsr1ValidForOrganization = snProductStructure
Global Const rnCodeValidForOrganization = "CodeValidForOrganization":                   Global Const rxnCodeValidForOrganization = snCode
Global Const rnPropertyValidForOrganization = "PropertyValidForOrganization":           Global Const rxnPropertyValidForOrganization = snProductStructure
Global Const rnOrgManagesCountry = "OrgManagesCountry":                                 Global Const rxnOrgManagesCountry = "Country"

' ################################################
'            View Names
' ################################################

' vn   - view name
' vsn  - view short name

Global Const vnEntityFilterEnum = "EntityFilter_Enum":               Global Const vsnEntityFilterEnum = "EFE"
Global Const vnEntityFilterNlTextEnum = "EntityFilter_Enum_Nl_Text": Global Const vsnEntityFilterNlTextEnum = "EFN"
Global Const vnPsFormingLdmTab = "PsFormingLdmTab":                  Global Const vsnPsFormingLdmTab = "PFL"
Global Const vnPsFormingPdmTab = "PsFormingPdmTab":                  Global Const vsnPsFormingPdmTab = "PFP"
Global Const vnRel2ProdLockHistory = "Rel2ProdLockHistory":          Global Const vsnRel2ProdLockHistory = "RPH"
Global Const vnRel2ProdLock = "Rel2ProdLock":                        Global Const vsnRel2ProdLock = "RPL"
Global Const vnSnapshotV8Agent = "SnapshotAgent":                    Global Const vsnSnapshotV8Agent = "VSA"
Global Const vnSnapshotV8ApplInfo = "SnapshotApplI":                 Global Const vsnSnapshotV8ApplInfo = "VAI"
Global Const vnSnapshotV8Bufferpool = "SnapshotBp":                  Global Const vsnSnapshotV8Bufferpool = "VSB"
Global Const vnSnapshotV8Container = "SnapshotCnt":                  Global Const vsnSnapshotV8Container = "VSC"
Global Const vnSnapshotV8Db = "SnapshotDb":                          Global Const vsnSnapshotV8Db = "VSD"
Global Const vnSnapshotV8Dbm = "SnapshotDbm":                        Global Const vsnSnapshotV8Dbm = "VSM"
Global Const vnSnapshotV8Lock = "SnapshotLock":                      Global Const vsnSnapshotV8Lock = "VSL"
Global Const vnSnapshotV8LockWait = "SnapshotLockWait":              Global Const vsnSnapshotV8LockWait = "VLW"
Global Const vnSnapshotV8SnapshotAppl = "SnapshotAppl":              Global Const vsnSnapshotV8SnapshotAppl = "VAP"
Global Const vnSnapshotV8Sql = "SnapshotSql":                        Global Const vsnSnapshotV8Sql = "VSS"
Global Const vnSnapshotV8Statement = "SnapshotStmnt":                Global Const vsnSnapshotV8Statement = "VST"
Global Const vnSnapshotV8Table = "SnapshotTable":                    Global Const vsnSnapshotV8Table = "VST"
Global Const vnSnapshotV8Tbs = "SnapshotTbs":                        Global Const vsnSnapshotV8Tbs = "VTS"
Global Const vnSnapshotV8TbsCfg = "SnapshotTbsCfg":                  Global Const vsnSnapshotV8TbsCfg = "VTC"
Global Const vnXmlFuncMap = "XmlFuncMap":                            Global Const vsnXmlFuncMap = "XFM"
Global Const vnXmlViewMap = "XmlViewMap":                            Global Const vsnXmlViewMap = "XVM"
Global Const vnXsdFuncMap = "XsdFuncMap":                            Global Const vsnXsdFuncMap = "XSM"

' ################################################
'            Stored Procedure Names
' ################################################

' spn   - stored procedure name
' spsn  - stored procedure short name

Global Const spnAHPropagateStatus = "AHPropagateStatus"
Global Const spnActivateAllPrices = "ActivateAllPrices"
Global Const spnActivateAllCodePrices = "ActivateAllCodePrices"
Global Const spnActivateAllTypePrices = "ActivateAllTypePrices"
Global Const spnActivateNationalCodeTexts = "ActivateNationalCodeTexts"
Global Const spnAddTablePartitionByDiv = "AddTablePartitionByDiv"
Global Const spnAddTablePartitionByPs = "AddTablePartitionByPs"
Global Const spnAddTestUser = "AddTestUser"
Global Const spnArchiveOrg = "ArchiveOrg"
Global Const spnArchiveOrgPurge = "ArchiveOrgPurge"
Global Const spnArchiveOrgEstimate = "ArchiveOrgEstimate"
Global Const spnAssertRebateDefault = "AssertRebateDefault"
Global Const spnAssignCodeCat = "AssignCodeCat"
Global Const spnCheckChangeLog = "CheckChangeLog"
Global Const spnClBroadcast = "ChangelogBroadcast"
Global Const spnGetGroupElements = "GetGroupElements"
Global Const spnDataChkCleanup = "DataCleanup"
Global Const spnDataChkCompare = "DataCompare"
Global Const spnDataChkCp2RefTab = "DataCp2RefTab"
Global Const spnDataChkExport = "DataExport"
Global Const spnDataChkImport = "DataImport"
Global Const spnDataChkLoad = "DataLoad"
Global Const spnDataInconsCleanup = "DataCleanup"
Global Const spnDeleteNSR1 = "DeleteNSR1"
Global Const spnLrtIncludesDivisionData = "lrt_Includes_Division_Data"
Global Const spnLrtLock_Genericcode = "lrtLock_Genericcode"
Global Const spnDeleteCBMV = "DeleteCBMV"
Global Const spnDeleteProductiveCode = "DeleteProductiveCode"
Global Const spnDeleteTablePartitionByDiv = "DeleteTablePartitionByDiv"
Global Const spnDeleteTablePartitionByPs = "DeleteTablePartitionByPs"
Global Const spnDeleteTechAspect = "DeleteTechAspect"
Global Const spnDeleteTechProperty = "DelTechProperty"
Global Const spnDeleteUnusedExpressions = "DeleteUnusedExpressions"
Global Const spnDfxExecute = "DfxExecute"
Global Const spnFactoryTakeOver = "FactoryTakeOver"
Global Const spnFtoGetChangeLog = "FtoGetChangeLog"
Global Const spnFtoGetChangeLogCard = "FtoGetChangeLogCard"
Global Const spnFtoGetConflicts = "FtoGetConflicts"
Global Const spnFtoGetPriceConflicts = "FtoGetPriceConflicts"
Global Const spnFtoGetEnpEbpMap = "FtoGetEnpEbpMapping"
Global Const spnFtoGetImplicitChanges = "FtoGetImplicitChanges"
Global Const spnFtoInitial = "FactoryTakeOver_Initial"
Global Const spnFtoLock = "Ftolock"
Global Const spnFtoPostProc = "FtoPostProc"
Global Const spnFtoSetEnp = "FtoSetEnp"
Global Const spnGenWorkspace = "Gen_Workspace"
Global Const spnGenWorkspaceWrapper = "GenWorkspace"
Global Const spnGetCodesWithoutDep = "GetCodesWithoutDep"
Global Const spnGetCodesWithoutDepAddOids = "GetCodesWithoutDepAddOids"
Global Const spnGetValue = "GetValue"
Global Const spnGroupIdSync = "GASync"
Global Const spnModifyCodeType = "ModifyCodeType"
Global Const spnCheckAffectedObjects = "CheckAffectedObjects"
Global Const spnOrgInit = "OrgInit"
Global Const spnOrgInitBus = "OrgInitBus"
Global Const spnOrgInitEnp = "OrgInitEnp"
Global Const spnOrgInitDupCode = "OrgInitDupCode"
Global Const spnOrgInitMeta = "OrgInitMeta"
Global Const spnPropExpr = "PropExpr"
Global Const spnPropInvExpr = "PropInvExpr"
Global Const spnRebateInitDefault = "RebateInitDefault"
Global Const spnRegStaticInit = "RegStaticInit"
Global Const spnRel2ProdIsSet = "is_Rel2ProdLock_set"
Global Const spnResetRel2ProdLock = "reset_Rel2ProdLock"
Global Const spnResetRel2ProdLockExclusive = "reset_Rel2ProdLockExclusive"
Global Const spnResetRel2ProdLockGenWs = "reset_Rel2ProdLockGenWs"
Global Const spnResetRel2ProdLocks = "reset_Rel2ProdLocks"
Global Const spnResetRel2ProdLocksOrphan = "reset_Rel2ProdLocksOrphan"
Global Const spnResetRel2ProdLocksWrapper = "resetRel2ProdLocks"
Global Const spnRssGetStatus = "RssGetStatus"
Global Const spnSPGetAffectedEntities = "SpGetAffectedEntities"
Global Const spnSetApplVersion = "SetApplVersion"
Global Const spnSetCtsConfig = "SetCtsConfig"
Global Const spnSetMessageSeverity = "SetMessageSeverity"
Global Const spnSetProductivePreProcess = "SetProductivePreProc"
Global Const spnSetProductive = "SetProductive"
Global Const spnSetProductiveIncludesDivisionData = "setProd_Includes_Division_Data"
Global Const spnSetProductivePostProcess = "SetProductivePostProc"
Global Const spnSetRel2ProdLock = "set_Rel2ProdLock"
Global Const spnSetRel2ProdLockExclusive = "set_Rel2ProdLockExclusive"
Global Const spnSetRel2ProdLockGenWs = "set_Rel2ProdLockGenWs"
Global Const spnSetRel2ProdLocksWrapper = "setRel2ProdLocks"
Global Const spnSetTablePartCfgDiv = "SetTablePartCfgByDiv"
Global Const spnSetTablePartCfgPs = "SetTablePartCfgByPs"
Global Const spnSpGenChangelog = "SpGenChangelog"
Global Const spnSuffixRel2ProdOther = "_other"
Global Const spnSuffixRel2ProdOthers = "_others"
Global Const spnTestData = "TestData"
Global Const spnTracePersist = "TracePersist"
Global Const spnVirtAttrSync = "VASync"

' ################################################
'            User Defined Functions
' ################################################

' udfn   - user defined function name
' udfsn  - user defined function short name

Global Const udfnAggrSlotOid4Label = "AggrSlotOid4Label"
Global Const udfnAggrSlotOid4Prop = "AggrSlotOid4Prop"
Global Const udfnAllowedCountry2Str = "ALC2STR"
Global Const udfnAllowedCountry2Str0 = "ALC2STR0"
Global Const udfnAssertCountryIdList = "AssertCidList"
Global Const udfnCatOid4Code = "CatOid4Code"
Global Const udfnDisallowedCountry2Str = "DALC2STR"
Global Const udfnDisallowedCountry2Str0 = "DALC2STR0"
Global Const udfnEndSlotLabel4Oid = "EndSlotLabel4Oid"
Global Const udfnEndSlotOid4Code = "EndSlotOid4Code"
Global Const udfnEndSlotOid4CodeOL = "EndSlotOid4Code_OL"
Global Const udfnEndSlotOid4CodeST = "EndSlotOid4Code_ST"
Global Const udfnEndSlotOid4CodeTB = "EndSlotOid4Code_TB"
Global Const udfnEndSlotOid4Label = "EndSlotOid4Label"
Global Const udfnEndSlotOid4LzCode = "EndSlotOid4LzCode"
Global Const udfnGcoOid4Code = "GcoOid4Code"
Global Const udfnGenRel2ProdLockKey = "genRel2ProdLockKey"
Global Const udfnGetCpgByPriceAssignment = "GCPGBPA"
Global Const udfnNormalizeCountryIdList = "NormCidList"
Global Const udfnNprOid4Code = "NprOid4Code"
Global Const udfnNprOid4CodeId = "NprOid4Code_ID"
Global Const udfnParseSr0Context = "ParseSr0Context"
Global Const udfnPropOid4PropLabel = "PropOid4PropLabel"
Global Const udfnPsOid2Sparte = "PsOid2Sparte"
Global Const udfnSparte2DivOid = "Sparte2DivOid"
Global Const udfnSparte2PsOid = "Sparte2PsOid"


' ### ENDIF IVK ###




