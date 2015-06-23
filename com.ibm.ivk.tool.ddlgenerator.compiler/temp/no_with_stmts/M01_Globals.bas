 Attribute VB_Name = "M01_Globals"
 Option Explicit
 
 Global Const gc_sheetNameConfig = "Config"
 
 Global Const gc_workBookSuffixes = ".xls,.xlsm"
 Global Const gc_fileNameSuffixDdl = "ddl"
 Global Const gc_fileNameSuffixDml = "dml"
 Global Const gc_fileNameSuffixCsv = "csv"
 
 Global Const gc_tempTabNameChangeLog = "SESSION.ChangeLog"
 Global Const gc_tempTabNameChangeLogNl = "SESSION.ChangeLog_NL_TEXT"
 
 Global Const gc_enumAttrNameSuffix = "_ID"
 
 Global Const gc_acmEntityTypeKeyEnum = "E"
 Global Const gc_acmEntityTypeKeyClass = "C"
 Global Const gc_acmEntityTypeKeyRel = "R"
 Global Const gc_acmEntityTypeKeyView = "V"
 Global Const gc_acmEntityTypeKeyType = "T"
 
 Global Const gc_newRecordName = "NEWRECORD"
 Global Const gc_oldRecordName = "OLDRECORD"
 
 Global Const gc_maxProcessingStep = 6
 
 Global Const gc_maxDb2PartitionNameSuffixLen = 20
 
 ' ### IFNOT IVK ###
 'Global Const gc_dirPrefixOrg = "ORG-"
 ' ### ENDIF IVK ###
 
 Global g_genLrtSupport As Boolean
 Global g_sheetNameDdlSummary As String
 
 Global g_targetDir As String
 Global g_fileNameIncrements() As Integer
 Global g_logLevelsMsgBox As Integer
 Global g_logLevelsReport As Integer
 
 Global g_phaseIndexRegularTables  As Integer
 Global g_phaseIndexCoreSupport    As Integer
 Global g_phaseIndexModuleMeta     As Integer
 Global g_phaseIndexFksRelTabs     As Integer
 Global g_phaseIndexLrt            As Integer
 Global g_phaseIndexLrtViews       As Integer
 Global g_phaseIndexChangeLogViews As Integer
 Global g_phaseIndexLrtSupport     As Integer
 Global g_phaseIndexDbSupport      As Integer
 Global g_phaseIndexAliases        As Integer
 
 Global g_phaseIndexLrtMqt         As Integer
 Global g_phaseIndexLogChange      As Integer
 Global g_phaseIndexDbSupport2     As Integer
 
 Global g_sectionIndexAlias As Integer
 Global g_sectionindexAliasDelObj As Integer
 Global g_sectionIndexAliasLrt As Integer
 Global g_sectionindexAliasPrivateOnly As Integer
 Global g_sectionIndexAliasPsDpFiltered As Integer
 Global g_sectionIndexAliasPsDpFilteredExtended As Integer
 Global g_sectionIndexDb As Integer
 Global g_sectionIndexDbAdmin As Integer
 Global g_sectionIndexSpLog As Integer
 Global g_sectionIndexLrt As Integer
 Global g_sectionIndexDbMeta As Integer
 Global g_sectionIndexDbMonitor As Integer
 Global g_sectionIndexFactoryTakeover As Integer
 Global g_sectionIndexMeta As Integer
 Global g_sectionIndexDataCheck As Integer
 Global g_sectionIndexCountry As Integer
 Global g_sectionIndexChangeLog As Integer
 Global g_sectionIndexDataFix As Integer
 Global g_sectionIndexAspect As Integer
 Global g_sectionIndexPaiLog As Integer
 Global g_sectionIndexTrace As Integer
 Global g_sectionIndexProductStructure As Integer
 Global g_sectionIndexSetProductive As Integer
 Global g_sectionIndexCode As Integer
 Global g_sectionIndexHelp As Integer
 Global g_sectionIndexFwkTest As Integer
 Global g_sectionIndexStaging As Integer
 Global g_sectionIndexCommon As Integer
 
 Global g_domainIndexEntityType As Integer
 Global g_domainIndexCid As Integer
 Global g_domainIndexOid As Integer
 Global g_domainIndexInteger As Integer
 Global g_domainIndexBoolean As Integer
 Global g_domainIndexDbRelease As Integer
 Global g_domainIndexEnumId As Integer
 Global g_domainIndexInUseBy As Integer
 Global g_domainIndexIsLrtPrivate As Integer
 Global g_domainIndexLrtId As Integer
 Global g_domainIndexLrtStatus As Integer
 Global g_domainIndexModTimestamp As Integer
 Global g_domainIndexUserId As Integer
 Global g_domainIndexUserIdAlt As Integer
 Global g_domainIndexLockRequestorId As Integer
 Global g_domainIndexR2pLockContext As Integer
 Global g_domainChangeLogString As Integer
 
 Global g_domainIndexValTimestamp As Integer
 Global g_domainIndexVersion As Integer
 Global g_domainIndexDbSchemaName As Integer
 Global g_domainIndexDbTableName As Integer
 Global g_domainIndexDbColumnName As Integer
 Global g_domainIndexDbViewName As Integer
 Global g_domainIndexDbProcName As Integer
 Global g_domainIndexDbFuncName As Integer
 Global g_domainIndexChangeLogString As Integer
 
 Global g_enumIndexDataPoolAccessMode As Integer
 
 Global g_classIndexAcmAttribute As Integer
 Global g_classIndexAcmDomain As Integer
 Global g_classIndexAcmEntity As Integer
 Global g_classIndexAcmSection As Integer
 Global g_classIndexChangeLog As Integer
 Global g_classIndexDataPool As Integer
 Global g_classIndexWriteLock As Integer
 Global g_classIndexReleaseLock As Integer
 Global g_classIndexLdmSchema As Integer
 Global g_classIndexLdmTable As Integer
 Global g_classIndexLrt As Integer
 Global g_classIndexLrtAffectedEntity As Integer
 Global g_classIndexLrtExecStatus As Integer
 Global g_classIndexOrganization As Integer
 Global g_classIndexPdmPrimarySchema As Integer
 Global g_classIndexPdmSchema As Integer
 Global g_classIndexPdmTable As Integer
 Global g_classIndexSqlLog As Integer
 Global g_classIndexSqlLogCfg As Integer
 Global g_classIndexUser As Integer
 Global g_classIndexDbCfgProfile As Integer
 Global g_classIndexDbPrivileges As Integer
 Global g_classIndexDisabledFks As Integer
 Global g_classIndexDisabledIndexes As Integer
 Global g_classIndexDisabledRtDep As Integer
 Global g_classIndexDisabledRts As Integer
 Global g_classIndexDisabledTriggers As Integer
 Global g_classIndexErrorMessage As Integer
 Global g_classIndexFkDependency As Integer
 Global g_classIndexIndexMetrics As Integer
 Global g_classIndexTableCfg As Integer
 Global g_classIndexSnapshotCol As Integer
 Global g_classIndexSnapshotFilter As Integer
 Global g_classIndexSnapshotHandle As Integer
 Global g_classIndexSnapshotType As Integer
 Global g_classIndexSnapshotAppl As Integer
 Global g_classIndexSnapshotApplInfo As Integer
 Global g_classIndexSnapshotLock As Integer
 Global g_classIndexSnapshotLockWait As Integer
 Global g_classIndexSnapshotStatement  As Integer
 
 Global g_workDataPoolIndex As Integer
 Global g_workDataPoolId As Integer
 
 Global g_primaryOrgIndex As Integer
 Global g_primaryOrgId As Integer
 
 Global g_activeLrtOidDdl As String
 
 Global g_allSchemaNamePattern As String
 Global g_schemaNameCtoMeta As String
 Global g_schemaNameCtoDbMonitor As String
 Global g_schemaNameCtoDbAdmin As String
 
 
 Global g_qualTabNameDataPoolAccessMode As String
 Global g_qualTabNamePdmDataPoolType As String
 Global g_qualTabNamePdmOrganization As String
 Global g_qualTabNamePdmOrganizationNl As String
 Global g_qualTabNameStatus As String
 Global g_qualTabNameLanguage As String
 
 Global g_qualTabNameAcmAttribute As String
 Global g_qualTabNameAcmAttributeNl As String
 Global g_qualTabNameAcmDomain As String
 Global g_qualTabNameAcmEntity As String
 Global g_qualTabNameAcmEntityNl As String
 Global g_qualTabNameAcmSection As String
 Global g_qualTabNameDataPool As String
 Global g_qualTabNameWriteLock As String
 Global g_qualTabNameReleaseLock As String
 Global g_qualTabNameLdmTable As String
 Global g_qualTabNameOrganization As String
 Global g_qualTabNamePdmPrimarySchema As String
 Global g_qualTabNamePdmSchema As String
 Global g_qualTabNamePdmTable As String
 Global g_qualTabNameSqlLog As String
 Global g_qualTabNameSqlLogCfg As String
 Global g_qualTabNameTableCfg As String
 Global g_qualTabNameUser As String
 Global g_qualTabNameDbCfgProfile As String
 Global g_qualTabNameDbPrivileges As String
 Global g_qualTabNameDisabledFks As String
 Global g_qualTabNameDisabledIndexes As String
 Global g_qualTabNameDisabledRtDep As String
 Global g_qualTabNameDisabledRts As String
 Global g_qualTabNameDisabledTriggers As String
 Global g_qualTabNameErrorMessage As String
 Global g_qualTabNameFkDependency As String
 Global g_qualTabNameIndexMetrics As String
 
 Global g_qualTabNameSnapshotCol As String
 Global g_qualTabNameSnapshotFilter As String
 Global g_qualTabNameSnapshotHandle As String
 Global g_qualTabNameSnapshotType As String
 Global g_qualTabNameSnapshotAppl As String
 Global g_qualTabNameSnapshotApplInfo As String
 Global g_qualTabNameSnapshotLock As String
 Global g_qualTabNameSnapshotLockWait As String
 Global g_qualTabNameSnapshotStatement  As String
 
 Global g_anOid As String
 Global g_surrogateKeyNameShort As String
 
 Global g_dbtOid As String
 Global g_dbtInteger As String
 Global g_dbtEntityId As String
 Global g_dbtEntityType As String
 Global g_dbtSequence As String
 Global g_dbtBoolean As String
 Global g_dbtEnumId As String
 Global g_dbtDbRelease As String
 Global g_dbtDbSchemaName As String
 Global g_dbtDbTableName As String
 Global g_dbtDbColumnName As String
 Global g_dbtDbViewName As String
 Global g_dbtDbProcName As String
 Global g_dbtDbFuncName As String
 Global g_dbtUserId As String
 Global g_dbtLrtId As String
 Global g_dbtChangeLogString As String
 
 Global g_anAhOid As String
 Global g_anAhCid As String
 Global g_anCid As String
 Global g_anCreateUser As String
 Global g_anCreateTimestamp As String
 Global g_anEndTime As String
 Global g_anInLrt As String
 Global g_anUpdateUser As String
 Global g_anLastUpdateTimestamp As String
 Global g_anLrtOid As String
 Global g_anLrtOpId As String
 Global g_anIsLrtPrivate As String
 Global g_anLrtState As String
 Global g_anStatus As String
 Global g_anVersionId As String
 Global g_anUserId As String
 Global g_anUserName As String
 Global g_anLastOpTime As String
 Global g_anIgnoreForChangelog As String
 
 Global g_anEnumLabelText As String
 
 Global g_anAcmEntitySection      As String
 Global g_anAcmEntityName         As String
 Global g_anAcmEntityType         As String
 Global g_anAcmEntityId           As String
 Global g_anAcmOrParEntitySection As String
 Global g_anAcmOrParEntityName    As String
 Global g_anAcmOrParEntityType    As String
 Global g_anAcmOrParEntityId      As String
 Global g_anAcmSupEntitySection   As String
 Global g_anAcmSupEntityName      As String
 Global g_anAcmSupEntityType      As String
 Global g_anAcmLeftEntitySection  As String
 Global g_anAcmLeftEntityName     As String
 Global g_anAcmLeftEntityType     As String
 Global g_anAcmRightEntitySection As String
 Global g_anAcmRightEntityName    As String
 Global g_anAcmRightEntityType    As String
 
 Global g_anAcmEntityLabel     As String
 
 Global g_anAcmAttributeName   As String
 Global g_anLdmDbColumnName    As String
 Global g_anAcmIsTv            As String
 Global g_anAcmIsVirtual       As String
 Global g_anLdmSequenceNo      As String
 Global g_anAcmAttributeLabel  As String
 
 Global g_anAcmDomainSection   As String
 Global g_anAcmDomainName      As String
 Global g_anAcmDbDataType      As String
 
 Global g_anLdmSchemaName   As String
 Global g_anLdmTableName   As String
 Global g_anPdmFkSchemaName   As String
 Global g_anPdmTableName   As String
 Global g_anPdmLdmFkSchemaName   As String
 Global g_anPdmTypedTableName   As String
 Global g_anPdmLdmFkTableName   As String
 Global g_anLdmFkSequenceNo As String
 
 Global g_anPdmNativeSchemaName As String
 Global g_anSpLogContextSchema As String
 Global g_anSpLogContextName As String
 Global g_anSpLogContextType As String
 
 Global g_anAcmIsLrtMeta As String
 Global g_anAcmIsLrt As String
 Global g_anLdmIsLrt As String
 Global g_anAcmIsGen As String
 Global g_anLdmIsGen As String
 Global g_anLdmIsNl As String
 
 Global g_anAcmIsCto As String
 Global g_anAcmIsCtp As String
 Global g_anAcmIsRangePartAll As String
 Global g_anAcmIsNt2m As String
 
 Global g_anAcmEntityShortName           As String
 Global g_anAcmUseLrtMqt                 As String
 Global g_anAcmUseLrtCommitPreprocess    As String
 Global g_anAcmIsLogChange               As String
 Global g_anAcmIsAbstract                As String
 Global g_anAcmIgnoreForChangelog        As String
 Global g_anAcmAliasShortName            As String
 Global g_anAcmIsEnforced                As String
 Global g_anAcmRlShortName               As String
 Global g_anAcmMinLeftCardinality        As String
 Global g_anAcmMaxLeftCardinality        As String
 Global g_anAcmLrShortName               As String
 Global g_anAcmMinRightCardinality       As String
 Global g_anAcmMaxRightCardinality       As String
 Global g_anEnumId As String
 Global g_anEnumRefId As String
 Global g_anLanguageId As String
 Global g_anOrganizationId As String
 Global g_anPoolTypeId As String
 Global g_anAccessModeId As String
 
 Global g_qualProcNameGetSnapshot As String
 Global g_qualProcNameGetSnapshotAnalysisLockWait As String
 Global g_qualProcNameGetSnapshotAnalysisAppl As String
 Global g_qualProcNameGetSnapshotAnalysisStatement As String
 Global g_qualProcNameGetSnapshotAnalysis As String
 
 Global g_qualFuncNameDb2Release As String
 Global g_qualFuncNameGetStrElem As String
 Global g_qualFuncNameStrElems As String
 Global g_qualFuncNameGetSubClassIdsByList As String
 Global g_qualFuncNameStrListMap As String
 
 
 Sub initGlobals()
   g_targetDir = dirName(ActiveWorkbook.FullName)

 ' ### IFNOT IVK ###
 ' ReDim g_fileNameIncrements(1 To 12)
 ' g_phaseIndexRegularTables =  1 : g_fileNameIncrements(g_phaseIndexRegularTables) = phaseRegularTables
 ' g_phaseIndexCoreSupport =  2 : g_fileNameIncrements(g_phaseIndexCoreSupport) = phaseCoreSupport
 ' g_phaseIndexModuleMeta =  3 : g_fileNameIncrements(g_phaseIndexModuleMeta) = phaseModuleMeta:
 ' g_phaseIndexFksRelTabs =  4 : g_fileNameIncrements(g_phaseIndexFksRelTabs) = phaseFksRelTabs
 ' g_phaseIndexLrt =  5 : g_fileNameIncrements(g_phaseIndexLrt) = phaseLrt
 ' g_phaseIndexLrtViews =  6 : g_fileNameIncrements(g_phaseIndexLrtViews) = phaseLrtViews
 ' g_phaseIndexChangeLogViews =  7 : g_fileNameIncrements(g_phaseIndexChangeLogViews) = phaseChangeLogViews
 ' g_phaseIndexLrtSupport =  8 : g_fileNameIncrements(g_phaseIndexLrtSupport) = phaseLrtSupport
 ' g_phaseIndexDbSupport =  9 : g_fileNameIncrements(g_phaseIndexDbSupport) = phaseDbSupport
 ' g_phaseIndexAliases = 10 : g_fileNameIncrements(g_phaseIndexAliases) = phaseAliases
 ' g_phaseIndexLogChange = 11 : g_fileNameIncrements(g_phaseIndexLogChange) = phaseLogChange
 ' g_phaseIndexDbSupport2 = 12 : g_fileNameIncrements(g_phaseIndexDbSupport2) = phaseDbSupport2
 ' ### ENDIF IVK ###

   g_phaseIndexLrtMqt = g_phaseIndexLrt

   g_sectionIndexAlias = getSectionIndexByName(snAlias)
   g_sectionindexAliasDelObj = getSectionIndexByName(snAliasDelObj)
   g_sectionIndexAliasLrt = getSectionIndexByName(snAliasLrt)
   g_sectionIndexAliasPsDpFiltered = getSectionIndexByName(snAliasPsDpFiltered)
   g_sectionIndexAliasPsDpFilteredExtended = getSectionIndexByName(snAliasPsDpFilteredExtended)
   g_sectionindexAliasPrivateOnly = getSectionIndexByName(snAliasPrivateOnly)
   g_sectionIndexDbAdmin = getSectionIndexByName(snDbAdmin)
   g_sectionIndexSpLog = getSectionIndexByName(snSpLog)
   g_sectionIndexLrt = getSectionIndexByName(snLrt)
   g_sectionIndexDb = getSectionIndexByName(snDb)
   g_sectionIndexDbMeta = getSectionIndexByName(snDbMeta)
   g_sectionIndexDbMonitor = getSectionIndexByName(snDbMonitor)
   g_sectionIndexFactoryTakeover = getSectionIndexByName(snFactoryTakeover)
   g_sectionIndexMeta = getSectionIndexByName(snMeta)
   g_sectionIndexDataCheck = getSectionIndexByName(snDataCheck)
   g_sectionIndexCountry = getSectionIndexByName(snCountry)
   g_sectionIndexChangeLog = getSectionIndexByName(snChangeLog)
   g_sectionIndexDataFix = getSectionIndexByName(snDataFix)
   g_sectionIndexAspect = getSectionIndexByName(snAspect)
   g_sectionIndexPaiLog = getSectionIndexByName(snPaiLog)
   g_sectionIndexTrace = getSectionIndexByName(snTrace)
   g_sectionIndexProductStructure = getSectionIndexByName(snProductStructure)
   g_sectionIndexSetProductive = getSectionIndexByName(snSetProductive)
   g_sectionIndexCode = getSectionIndexByName(snCode)
   g_sectionIndexHelp = getSectionIndexByName(snHelp)
   If generateFwkTest Then
     g_sectionIndexFwkTest = getSectionIndexByName(snFwkTest)
   End If
   g_sectionIndexStaging = getSectionIndexByName(snStaging)
   g_sectionIndexCommon = getSectionIndexByName(snCommon)

   g_domainIndexEntityType = getDomainIndexByName(dxnEntityType, dnEntityType)
   g_domainIndexCid = getDomainIndexByName(dxnClassId, dnClassId)
   g_domainIndexBoolean = getDomainIndexByName(dxnBoolean, dnBoolean)
   g_domainIndexDbRelease = getDomainIndexByName(dxnDbRelease, dnDbRelease)
   g_domainIndexEnumId = getDomainIndexByName(dxnEnumId, dnEnumId)
   g_domainIndexInUseBy = getDomainIndexByName(dxnInUseBy, dnInUseBy)
   g_domainIndexIsLrtPrivate = getDomainIndexByName(dxnBoolean, dnBoolean)
   g_domainIndexLrtId = getDomainIndexByName(dsnLrt, dnLrt)
   g_domainIndexLrtStatus = getDomainIndexByName(dxnLrtStatus, dnLrtStatus)
   g_domainIndexModTimestamp = getDomainIndexByName(dxnModTimestamp, dnModTimestamp)
   g_domainIndexOid = getDomainIndexByName(dxnOid, dnOid)
   g_domainIndexInteger = getDomainIndexByName(dxnInteger, dnInteger)
   g_domainIndexUserId = getDomainIndexByName(dxnUserId, dnUserId)
   g_domainIndexUserIdAlt = getDomainIndexByName(dxnUserId, dnUserIdAlt)
   g_domainIndexLockRequestorId = getDomainIndexByName(dxnLockRequestorId, dnLockRequestorId)
   g_domainIndexR2pLockContext = getDomainIndexByName(dxnR2pLockContext, dnR2pLockContext)
   g_domainIndexValTimestamp = getDomainIndexByName(dxnValTimestamp, dnValTimestamp)
   g_domainIndexVersion = getDomainIndexByName(dxnVersion, dnVersion)
   g_domainIndexDbSchemaName = getDomainIndexByName(dxnDbSchemaName, dnDbSchemaName)
   g_domainIndexDbTableName = getDomainIndexByName(dxnDbTableName, dnDbTableName)
   g_domainIndexDbColumnName = getDomainIndexByName(dxnDbColumnName, dnDbColumnName)
   g_domainIndexDbViewName = getDomainIndexByName(dxnDbViewName, dnDbViewName)
   g_domainIndexDbProcName = getDomainIndexByName(dxnDbProcName, dnDbProcName)
   g_domainIndexDbFuncName = getDomainIndexByName(dxnDbFuncName, dnDbFuncName)
   g_domainIndexChangeLogString = getDomainIndexByName(dxnChangeLogString, dnChangeLogString)
 
   g_enumIndexDataPoolAccessMode = getEnumIndexByName(snMeta, enDataPoolAccessMode)

   g_classIndexAcmAttribute = getClassIndexByName(clxnAcmAttribute, clnAcmAttribute)
   g_classIndexAcmDomain = getClassIndexByName(clxnAcmDomain, clnAcmDomain)
   g_classIndexAcmEntity = getClassIndexByName(clxnAcmEntity, clnAcmEntity)
   g_classIndexAcmSection = getClassIndexByName(clxnAcmSection, clnAcmSection)
   g_classIndexChangeLog = getClassIndexByName(clxnChangeLog, clnChangeLog)
   g_classIndexDataPool = getClassIndexByName(clxnDataPool, clnDataPool)
   g_classIndexWriteLock = getClassIndexByName(clxnWriteLock, clnWriteLock)
   g_classIndexReleaseLock = getClassIndexByName(clxnReleaseLock, clnReleaseLock)
   g_classIndexLdmSchema = getClassIndexByName(clxnLdmSchema, clnLdmSchema)
   g_classIndexLdmTable = getClassIndexByName(clxnLdmTable, clnLdmTable)
   g_classIndexLrt = getClassIndexByName(clxnLrt, clnLrt)
   g_classIndexLrtAffectedEntity = getClassIndexByName(clxnLrtAffectedEntity, clnLrtAffectedEntity)
   g_classIndexLrtExecStatus = getClassIndexByName(clxnLrtExecStatus, clnLrtExecStatus)
   g_classIndexOrganization = getClassIndexByName(clxnOrganization, clnOrganization)
   g_classIndexPdmPrimarySchema = getClassIndexByName(clxnPdmPrimarySchema, clnPdmPrimarySchema)
   g_classIndexPdmSchema = getClassIndexByName(clxnPdmSchema, clnPdmSchema)
   g_classIndexPdmTable = getClassIndexByName(clxnPdmTable, clnPdmTable)
   If supportSpLogging Then
     g_classIndexSqlLog = getClassIndexByName(clxnSqlLog, clnSqlLog)
     g_classIndexSqlLogCfg = getClassIndexByName(clxnSqlLogCfg, clnSqlLogCfg)
   End If
   g_classIndexUser = getClassIndexByName(clxnUser, clnUser)
   g_classIndexDbCfgProfile = getClassIndexByName(clxnDbCfgProfile, clnDbCfgProfile)
   g_classIndexDbPrivileges = getClassIndexByName(clxnDbPrivileges, clnDbPrivileges)
   g_classIndexDisabledFks = getClassIndexByName(clxnDisabledFks, clnDisabledFks)
   g_classIndexDisabledIndexes = getClassIndexByName(clxnDisabledIndexes, clnDisabledIndexes)
   g_classIndexDisabledRtDep = getClassIndexByName(clxnDisabledRtDep, clnDisabledRtDep)
   g_classIndexDisabledRts = getClassIndexByName(clxnDisabledRts, clnDisabledRts)
   g_classIndexDisabledTriggers = getClassIndexByName(clxnDisabledTriggers, clnDisabledTriggers)
   g_classIndexErrorMessage = getClassIndexByName(clxnErrorMessage, clnErrorMessage)
   g_classIndexFkDependency = getClassIndexByName(clxnFkDependency, clnFkDependency)
   If supportIndexMetrics Then
     g_classIndexIndexMetrics = getClassIndexByName(clxnIndexMetrics, clnIndexMetrics)
   End If
   g_classIndexTableCfg = getClassIndexByName(clxnTableCfg, clnTableCfg)
 
   g_classIndexSnapshotCol = getClassIndexByName(clxnSnapshotCol, clnSnapshotCol)
   g_classIndexSnapshotFilter = getClassIndexByName(clxnSnapshotFilter, clnSnapshotFilter)
   g_classIndexSnapshotHandle = getClassIndexByName(clxnSnapshotHandle, clnSnapshotHandle)
   g_classIndexSnapshotType = getClassIndexByName(clxnSnapshotType, clnSnapshotType)
 ' ### IFNOT IVK ###
 ' g_classIndexSnapshotAppl = getClassIndexByName(clxnSnapshotV9Appl, clnSnapshotV9Appl)
 ' g_classIndexSnapshotApplInfo = getClassIndexByName(clxnSnapshotV9ApplInfo, clnSnapshotV9ApplInfo)
 ' g_classIndexSnapshotLock = getClassIndexByName(clxnSnapshotV9Lock, clnSnapshotV9Lock)
 ' g_classIndexSnapshotLockWait = getClassIndexByName(clxnSnapshotV9LockWait, clnSnapshotV9LockWait)
 ' g_classIndexSnapshotStatement = getClassIndexByName(clxnSnapshotV9Statement, clnSnapshotV9Statement)
 ' ### ENDIF IVK ###
 
   g_dbtOid = getDataTypeByDomainIndex(g_domainIndexOid)
   g_dbtInteger = getDataTypeByDomainIndex(g_domainIndexInteger)
   g_dbtSequence = g_dbtOid
   g_dbtEntityId = getDataTypeByDomainIndex(g_domainIndexCid)
   g_dbtEntityType = getDataTypeByDomainIndex(g_domainIndexEntityType)
   g_dbtBoolean = getDataTypeByDomainIndex(g_domainIndexBoolean)
   g_dbtEnumId = getDataTypeByDomainIndex(g_domainIndexEnumId)
   g_dbtDbRelease = getDataTypeByDomainIndex(g_domainIndexDbRelease)
   g_dbtDbSchemaName = getDataTypeByDomainIndex(g_domainIndexDbSchemaName)
   g_dbtDbTableName = getDataTypeByDomainIndex(g_domainIndexDbTableName)
   g_dbtDbColumnName = getDataTypeByDomainIndex(g_domainIndexDbColumnName)
   g_dbtDbViewName = getDataTypeByDomainIndex(g_domainIndexDbViewName)
   g_dbtDbProcName = getDataTypeByDomainIndex(g_domainIndexDbProcName)
   g_dbtDbFuncName = getDataTypeByDomainIndex(g_domainIndexDbFuncName)
   g_dbtUserId = getDataTypeByDomainIndex(g_domainIndexUserId)
   g_dbtLrtId = getDataTypeByDomainIndex(g_domainIndexLrtId)
   g_dbtChangeLogString = getDataTypeByDomainIndex(g_domainIndexChangeLogString)

   g_activeLrtOidDdl = g_dbtOid & "(" & gc_db2RegVarLrtOidSafeSyntax & ")"
 
   g_workDataPoolIndex = getWorkDataPoolIndex()
   g_workDataPoolId = getWorkDataPoolId()
   g_primaryOrgIndex = getPrimaryOrgIndex()
   g_primaryOrgId = getPrimaryOrgId()

   initGlobals_IVK
 End Sub
 
 
 Sub initGlobalsByDdl( _
  ddlType As DdlTypeId _
 )
   g_allSchemaNamePattern = genSchemaName("%", "%", ddlType)

   g_schemaNameCtoMeta = genSchemaName(snMeta, ssnMeta, ddlType)
   g_schemaNameCtoDbMonitor = genSchemaName(snDbMonitor, ssnDbMonitor, ddlType)
   g_schemaNameCtoDbAdmin = genSchemaName(snDbAdmin, ssnDbAdmin, ddlType)

   g_qualTabNameDataPoolAccessMode = genQualTabNameByEnumIndex(g_enumIndexDataPoolAccessMode, ddlType)

   g_qualTabNameAcmAttribute = genQualTabNameByClassIndex(g_classIndexAcmAttribute, ddlType)
   g_qualTabNameAcmAttributeNl = genQualTabNameByClassIndex(g_classIndexAcmAttribute, ddlType, , , , , , True)
   g_qualTabNameAcmDomain = genQualTabNameByClassIndex(g_classIndexAcmDomain, ddlType)
   g_qualTabNameAcmEntity = genQualTabNameByClassIndex(g_classIndexAcmEntity, ddlType)
   g_qualTabNameAcmEntityNl = genQualTabNameByClassIndex(g_classIndexAcmEntity, ddlType, , , , , , True)
   g_qualTabNameAcmSection = genQualTabNameByClassIndex(g_classIndexAcmSection, ddlType)
   g_qualTabNameDataPool = genQualTabNameByClassIndex(g_classIndexDataPool, ddlType)
   g_qualTabNameWriteLock = genQualTabNameByClassIndex(g_classIndexWriteLock, ddlType)
   g_qualTabNameReleaseLock = genQualTabNameByClassIndex(g_classIndexReleaseLock, ddlType)
   g_qualTabNameLdmTable = genQualTabNameByClassIndex(g_classIndexLdmTable, ddlType)
   g_qualTabNameOrganization = genQualTabNameByClassIndex(g_classIndexOrganization, ddlType)
   g_qualTabNamePdmPrimarySchema = genQualTabNameByClassIndex(g_classIndexPdmPrimarySchema, ddlType)
   g_qualTabNamePdmSchema = genQualTabNameByClassIndex(g_classIndexPdmSchema, ddlType)
   g_qualTabNamePdmTable = genQualTabNameByClassIndex(g_classIndexPdmTable, ddlType)
   If supportSpLogging Then
     g_qualTabNameSqlLog = genQualTabNameByClassIndex(g_classIndexSqlLog, ddlType)
     g_qualTabNameSqlLogCfg = genQualTabNameByClassIndex(g_classIndexSqlLogCfg, ddlType)
   End If
   g_qualTabNameTableCfg = genQualTabNameByClassIndex(g_classIndexTableCfg, ddlType)
   g_qualTabNameUser = genQualTabNameByClassIndex(g_classIndexUser, ddlType)
   g_qualTabNameDbCfgProfile = genQualTabNameByClassIndex(g_classIndexDbCfgProfile, ddlType)
   g_qualTabNameDbPrivileges = genQualTabNameByClassIndex(g_classIndexDbPrivileges, ddlType)
   g_qualTabNameDisabledFks = genQualTabNameByClassIndex(g_classIndexDisabledFks, ddlType)
   g_qualTabNameDisabledIndexes = genQualTabNameByClassIndex(g_classIndexDisabledIndexes, ddlType)
   g_qualTabNameDisabledRtDep = genQualTabNameByClassIndex(g_classIndexDisabledRtDep, ddlType)
   g_qualTabNameDisabledRts = genQualTabNameByClassIndex(g_classIndexDisabledRts, ddlType)
   g_qualTabNameDisabledTriggers = genQualTabNameByClassIndex(g_classIndexDisabledTriggers, ddlType)
   g_qualTabNameErrorMessage = genQualTabNameByClassIndex(g_classIndexErrorMessage, ddlType)
   g_qualTabNameFkDependency = genQualTabNameByClassIndex(g_classIndexFkDependency, ddlType)
   If supportIndexMetrics Then
     g_qualTabNameIndexMetrics = genQualTabNameByClassIndex(g_classIndexIndexMetrics, ddlType)
   End If
 
   g_qualTabNameSnapshotCol = genQualTabNameByClassIndex(g_classIndexSnapshotCol, ddlType)
   g_qualTabNameSnapshotFilter = genQualTabNameByClassIndex(g_classIndexSnapshotFilter, ddlType)
   g_qualTabNameSnapshotHandle = genQualTabNameByClassIndex(g_classIndexSnapshotHandle, ddlType)
   g_qualTabNameSnapshotType = genQualTabNameByClassIndex(g_classIndexSnapshotType, ddlType)
   g_qualTabNameSnapshotAppl = genQualTabNameByClassIndex(g_classIndexSnapshotAppl, ddlType)
   g_qualTabNameSnapshotApplInfo = genQualTabNameByClassIndex(g_classIndexSnapshotApplInfo, ddlType)
   g_qualTabNameSnapshotLock = genQualTabNameByClassIndex(g_classIndexSnapshotLock, ddlType)
   g_qualTabNameSnapshotLockWait = genQualTabNameByClassIndex(g_classIndexSnapshotLockWait, ddlType)
   g_qualTabNameSnapshotStatement = genQualTabNameByClassIndex(g_classIndexSnapshotStatement, ddlType)
 
   g_anOid = genSurrogateKeyName(ddlType)
   g_surrogateKeyNameShort = genSurrogateKeyShortName(ddlType)

   g_anAhOid = genAttrName(conAhOId, ddlType)
   g_anAhCid = genAttrName(conAhClassId, ddlType)
   g_anCid = genAttrName(conClassId, ddlType)
   g_anCreateTimestamp = genAttrName(conCreateTimestamp, ddlType)
   g_anCreateUser = genAttrName(conCreateUser, ddlType)
   g_anEndTime = genAttrName(conEndTime, ddlType)
   g_anInLrt = genAttrName(conInLrt, ddlType)
   g_anIsLrtPrivate = genAttrName(conIsLrtPrivate, ddlType)
   g_anLrtOid = genAttrName(conLrtOid, ddlType)
   g_anLrtOpId = genAttrName(conLrtOpId, ddlType)
   g_anLastUpdateTimestamp = genAttrName(conLastUpdateTimestamp, ddlType)
   g_anUpdateUser = genAttrName(conUpdateUser, ddlType)
   g_anLrtState = genAttrName(conLrtState, ddlType)
   g_anStatus = genAttrName(conStatusId, ddlType)
   g_anVersionId = genAttrName(conVersionId, ddlType)
   g_anAcmOrParEntityId = genAttrName(conAcmOrParEntityId, ddlType)
   g_anUserId = genAttrName(conUserId, ddlType)
   g_anUserName = genAttrName(conUserName, ddlType)
   g_anLastOpTime = genAttrName(conLastOpTime, ddlType)
   g_anIgnoreForChangelog = genAttrName(conIgnoreForChangelog, ddlType)
   g_anEnumLabelText = genAttrName(conEnumLabelText, ddlType)

   g_anAcmEntitySection = genAttrName(conAcmEntitySection, ddlType)
   g_anAcmEntityName = genAttrName(conAcmEntityName, ddlType)
   g_anAcmEntityType = genAttrName(conAcmEntityType, ddlType)
   g_anAcmEntityId = genAttrName(conAcmEntityId, ddlType)
   g_anAcmOrParEntitySection = genAttrName(conAcmOrParEntitySection, ddlType)
   g_anAcmOrParEntityName = genAttrName(conAcmOrParEntityName, ddlType)
   g_anAcmOrParEntityType = genAttrName(conAcmOrParEntityType, ddlType)
   g_anAcmOrParEntityId = genAttrName(conAcmOrParEntityId, ddlType)
   g_anAcmSupEntitySection = genAttrName(conAcmSupEntitySection, ddlType)
   g_anAcmSupEntityName = genAttrName(conAcmSupEntityName, ddlType)
   g_anAcmSupEntityType = genAttrName(conAcmSupEntityType, ddlType)
   g_anAcmLeftEntitySection = genAttrName(conAcmLeftEntitySection, ddlType)
   g_anAcmLeftEntityName = genAttrName(conAcmLeftEntityName, ddlType)
   g_anAcmLeftEntityType = genAttrName(conAcmLeftEntityType, ddlType)
   g_anAcmRightEntitySection = genAttrName(conAcmRightEntitySection, ddlType)
   g_anAcmRightEntityName = genAttrName(conAcmRightEntityName, ddlType)
   g_anAcmRightEntityType = genAttrName(conAcmRightEntityType, ddlType)

   g_anAcmEntityLabel = genAttrName(conAcmEntityLabel, ddlType)
 
   g_anAcmAttributeName = genAttrName(conAcmAttributeName, ddlType)
   g_anLdmDbColumnName = genAttrName(conLdmDbColumnName, ddlType)
   g_anAcmIsTv = genAttrName(conAcmIsTv, ddlType)
   g_anAcmIsVirtual = genAttrName(conAcmIsVirtual, ddlType)
   g_anLdmSequenceNo = genAttrName(conLdmSequenceNo, ddlType)
   g_anAcmAttributeLabel = genAttrName(conAcmAttributeLabel, ddlType)
 
   g_anAcmDomainSection = genAttrName(conAcmDomainSection, ddlType)
   g_anAcmDomainName = genAttrName(conAcmDomainName, ddlType)
   g_anAcmDbDataType = genAttrName(conAcmDbDataType, ddlType)

   g_anLdmSchemaName = genAttrName(conLdmSchemaName, ddlType)
   g_anLdmTableName = genAttrName(conLdmTableName, ddlType)
   g_anPdmFkSchemaName = genAttrName(conPdmFkSchemaName, ddlType)
   g_anPdmTableName = genAttrName(conPdmTableName, ddlType)
   g_anPdmLdmFkSchemaName = genAttrName(conPdmLdmFkSchemaName, ddlType)
   g_anPdmLdmFkTableName = genAttrName(conPdmLdmFkTableName, ddlType)
   g_anLdmFkSequenceNo = genAttrName(conLdmFkSequenceNo, ddlType)

   g_anPdmTypedTableName = genAttrName(conPdmTypedTableName, ddlType)

   g_anPdmNativeSchemaName = genAttrName(conPdmNativeSchemaName, ddlType)
   g_anSpLogContextSchema = genAttrName(conSpLogContextSchema, ddlType)
   g_anSpLogContextName = genAttrName(conSpLogContextName, ddlType)
   g_anSpLogContextType = genAttrName(conSpLogContextType, ddlType)

   g_anAcmIsLrtMeta = genAttrName(conAcmIsLrtMeta, ddlType)
   g_anAcmIsLrt = genAttrName(conAcmIsLrt, ddlType)
   g_anLdmIsLrt = genAttrName(conLdmIsLrt, ddlType)
   g_anAcmIsGen = genAttrName(conAcmIsGen, ddlType)
   g_anLdmIsGen = genAttrName(conLdmIsGen, ddlType)
   g_anLdmIsNl = genAttrName(conLdmIsNl, ddlType)
   g_anLdmIsMqt = genAttrName(conLdmIsMqt, ddlType)

   g_anAcmIsCto = genAttrName(conAcmIsCto, ddlType)
   g_anAcmIsCtp = genAttrName(conAcmIsCtp, ddlType)
   g_anAcmIsRangePartAll = genAttrName(conAcmIsRangePartAll, ddlType)
 
   g_anAcmEntityShortName = genAttrName(conAcmEntityShortName, ddlType)
   g_anAcmUseLrtMqt = genAttrName(conAcmUseLrtMqt, ddlType)
   g_anAcmUseLrtCommitPreprocess = genAttrName(conAcmUseLrtCommitPreprocess, ddlType)
   g_anAcmIsLogChange = genAttrName(conAcmIsLogChange, ddlType)
   g_anAcmIsAbstract = genAttrName(conAcmIsAbstract, ddlType)
   g_anAcmIgnoreForChangelog = genAttrName(conAcmIgnoreForChangelog, ddlType)
   g_anAcmAliasShortName = genAttrName(conAcmAliasShortName, ddlType)
   g_anAcmIsEnforced = genAttrName(conAcmIsEnforced, ddlType)
   g_anAcmRlShortName = genAttrName(conAcmRlShortName, ddlType)
   g_anAcmMinLeftCardinality = genAttrName(conAcmMinLeftCardinality, ddlType)
   g_anAcmMaxLeftCardinality = genAttrName(conAcmMaxLeftCardinality, ddlType)
   g_anAcmLrShortName = genAttrName(conAcmLrShortName, ddlType)
   g_anAcmMinRightCardinality = genAttrName(conAcmMinRightCardinality, ddlType)
   g_anAcmMaxRightCardinality = genAttrName(conAcmMaxRightCardinality, ddlType)
 
   g_anEnumId = genAttrName(conEnumId, ddlType)
   g_anEnumRefId = genAttrName(conEnumRefId, ddlType)
   g_anLanguageId = genAttrName(conLanguageId, ddlType)
   g_anOrganizationId = genAttrName(conOrganizationId, ddlType)
   g_anPoolTypeId = genAttrName(conPoolTypeId, ddlType)
   g_anAccessModeId = genAttrName(conAccessModeId, ddlType)

   g_qualProcNameGetSnapshot = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshot, ddlType)
   g_qualProcNameGetSnapshotAnalysisLockWait = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshotAnalysisLockWait, ddlType)
   g_qualProcNameGetSnapshotAnalysisAppl = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshotAnalysisAppl, ddlType)
   g_qualProcNameGetSnapshotAnalysisStatement = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshotAnalysisStatement, ddlType)
   g_qualProcNameGetSnapshotAnalysis = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshotAnalysis, ddlType)

   g_qualFuncNameDb2Release = genQualFuncName(g_sectionIndexDbAdmin, udfnDb2Release, ddlType, , , , , , True)
   g_qualFuncNameGetStrElem = genQualFuncName(g_sectionIndexMeta, udfnGetStrElem, ddlType, , , , , , True)
   g_qualFuncNameGetSubClassIdsByList = genQualFuncName(g_sectionIndexDbMeta, udfnGetSubClassIdsByList, ddlType, , , , , , True)
   g_qualFuncNameStrListMap = genQualFuncName(g_sectionIndexMeta, udfnStrListMap, ddlType)
 ' ### IFNOT IVK ###
 ' g_qualFuncNameStrElems = genQualFuncName(g_sectionIndexMeta, udfnStrElems, ddlType)
 ' ### ENDIF IVK ###
 
   initGlobalsByDdl_IVK ddlType
 End Sub
 
 
 Sub setLogLevesl( _
   Optional logLevelsReport As Integer = (ellFixableWarning Or ellWarning Or ellError Or ellFatal), _
   Optional logLevelsMsgBox As Integer = (ellFatal Or ellError) _
 )
   g_logLevelsMsgBox = logLevelsMsgBox
   g_logLevelsReport = logLevelsReport
 End Sub
 
 
 Sub setEnv( _
   forLrt As Boolean _
 )
   g_sheetNameDdlSummary = "LDM" & IIf(forLrt, "-LRT", "")
 End Sub
 
 
