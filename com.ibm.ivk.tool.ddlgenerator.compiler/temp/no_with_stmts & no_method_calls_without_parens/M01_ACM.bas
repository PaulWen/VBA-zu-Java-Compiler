 Attribute VB_Name = "M01_ACM"
 
 ' ############################################
 ' # section meta information
 ' ############################################
 
 ' sn   - section name
 ' ssn  - section short name
 
 Global Const snLrt = "LRT":                        Global Const ssnLrt = "LRT"
 Global Const snCommon = "Common":                  Global Const ssnCommon = "CMN"
 Global Const snMeta = "Meta":                      Global Const ssnMeta = "MET"
 Global Const snCountry = "Country":                Global Const ssnCountry = "CNT"
 Global Const snDbAdmin = "DbAdmin":                Global Const ssnDbAdmin = "DBA"
 Global Const snDb = "DB":                          Global Const ssnDb = "DB"
 Global Const snDbMeta = "DbMeta":                  Global Const ssnDbMeta = "DBM"
 Global Const snDbMonitor = "DbMonitor":            Global Const ssnDbMonitor = "MON"
 Global Const snHelp = "Help":                      Global Const ssnHelp = "HLP"
 Global Const snSpLog = "Log":                      Global Const ssnSpLog = "LOG"
 Global Const snTrace = "Trace":                    Global Const ssnTrace = "TRC"
 Global Const snAlias = "ALIAS":                    Global Const ssnAlias = "AL"
 Global Const snAliasLrt = "ALIAS_LRT":             Global Const ssnAliasLrt = "ALL"
 Global Const snAliasPrivateOnly = "ALIAS_LRT_PRI": Global Const ssnAliasPrivateOnly = "ALP"
 Global Const snAliasPublicOnly = "ALIAS_LRT_PUB":  Global Const ssnAliasPublicOnly = "ALO"
 Global Const snUser = "User":                      Global Const ssnUser = "USR"
 Global Const snChangeLog = "Changelog":            Global Const ssnChangeLog = "CLG"
 Global Const snPackageReopt = "PackageReopt":      Global Const ssnPackageReopt = "ROP"
 
 ' ############################################
 ' # domain meta information
 ' ############################################
 
 ' dn   - domain name
 ' dxn  - domain section name
 
 Global Const dnOid = "OID":                     Global Const dxnOid = snCommon
 Global Const dnInteger = "Integer":             Global Const dxnInteger = snCommon
 Global Const dnModTimestamp = "ModTimestamp":   Global Const dxnModTimestamp = snCommon
 Global Const dnLrt = "LRT":                     Global Const dsnLrt = "Lrt"
 Global Const dnInUseBy = dnLrt
 Global Const dxnInUseBy = dsnLrt
 Global Const dnLrtLabel = "Label":              Global Const dxnLrtLabel = snLrt
 Global Const dnBoolean = "Boolean":             Global Const dxnBoolean = snCommon
 Global Const dnDbRelease = "DbRelease":         Global Const dxnDbRelease = snDbMeta
 Global Const dnNumber = "Number":               Global Const dxnNumber = snCommon
 Global Const dnSmallNumber = "SmallNumber":     Global Const dxnSmallNumber = snCommon
 Global Const dnWorkingState = "WorkingState":   Global Const dxnWorkingState = snCommon
 Global Const dnUserId = "UserId":               Global Const dxnUserId = snUser
 Global Const dnUserIdAlt = "UserIdAlt"
 Global Const dnUserName = "UserName":           Global Const dxnUserName = snUser
 Global Const dnVersion = "Version":             Global Const dxnVersion = snCommon
 Global Const dnPdmTableName = "TableName":      Global Const dxnPdmTableName = snMeta
 Global Const dnPdmColumnName = "ColumnName":    Global Const dxnPdmColumnName = snMeta
 Global Const dnEntityType = "AcmEntityType":    Global Const dxnEntityType = snDbMeta
 Global Const dnClassId = "ClassId":             Global Const dxnClassId = snCommon
 Global Const dnChangeComment = "ChangeComment": Global Const dxnChangeComment = "Lrt"
 Global Const dnLrtStatus = "LRTStatus":         Global Const dxnLrtStatus = "Lrt"
 Global Const dnEnumId = "EnumId":               Global Const dxnEnumId = "Meta"
 Global Const dnChangeLogString = "AttrValueString": Global Const dxnChangeLogString = "ChangeLog"
 
 Global Const dnDbSchemaName = "DbSchemaName":   Global Const dxnDbSchemaName = snDbMeta
 Global Const dnDbTableName = "DbTableName":     Global Const dxnDbTableName = snDbMeta
 Global Const dnDbColumnName = "DbColumnName":   Global Const dxnDbColumnName = snDbMeta
 Global Const dnDbViewName = "DbViewName":       Global Const dxnDbViewName = snDbMeta
 Global Const dnDbProcName = "DbProcName":       Global Const dxnDbProcName = snDbMeta
 Global Const dnDbFuncName = "DbFuncName":       Global Const dxnDbFuncName = snDbMeta
 
 ' ############################################
 ' # column meta information
 ' ############################################
 
 ' con   - column name
 ' cosn  - column short name
 
 Global Const conAcmAliasShortName = "aliasShortName"
 Global Const conAcmAttributeLabel = "attributeLabel"
 Global Const conAcmAttributeName = "attributeName"
 Global Const conAcmDbDataType = "dbDataType"
 Global Const conAcmDomainName = "domainName"
 Global Const conAcmDomainSection = "domainSection"
 Global Const conAcmEntityId = "entityId"
 Global Const conAcmEntityLabel = "entityLabel"
 Global Const conAcmEntityName = "entityName"
 Global Const conAcmEntitySection = "entitySection"
 Global Const conAcmEntityShortName = "entityShortName"
 Global Const conAcmEntityType = "entityType"
 Global Const conAcmIgnoreForChangelog = "ignoreForChangelog"
 Global Const conAcmIsAbstract = "isAbstract"
 Global Const conAcmIsCto = "isCto"
 Global Const conAcmIsCtp = "isCtp"
 Global Const conAcmIsRangePartAll = "isRangePartAll"
 Global Const conAcmIsEnforced = "isEnforced"
 Global Const conAcmIsGen = "isGen"
 Global Const conAcmIsLogChange = "isLogChange"
 Global Const conAcmIsLrt = "isLrt"
 Global Const conAcmIsLrtMeta = "isLrtMeta"
 Global Const conAcmIsTv = "isTv"
 Global Const conAcmIsVirtual = "isVirtual"
 Global Const conAcmLeftEntityName = "left_entityName"
 Global Const conAcmLeftEntitySection = "left_entitySection"
 Global Const conAcmLeftEntityType = "left_entityType"
 Global Const conAcmLrShortName = "lrShortName"
 Global Const conAcmMaxLeftCardinality = "maxLeftCardinality"
 Global Const conAcmMaxRightCardinality = "maxRightCardinality"
 Global Const conAcmMinLeftCardinality = "minLeftCardinality"
 Global Const conAcmMinRightCardinality = "minRightCardinality"
 Global Const conAcmOrParEntityId = "orParentEntityId"
 Global Const conAcmOrParEntityName = "orpPar_entityName"
 Global Const conAcmOrParEntitySection = "orpPar_entitySection"
 Global Const conAcmOrParEntityType = "orpPar_entityType"
 Global Const conAcmRightEntityName = "right_entityName"
 Global Const conAcmRightEntitySection = "right_entitySection"
 Global Const conAcmRightEntityType = "right_entityType"
 Global Const conAcmRlShortName = "rlShortName"
 Global Const conAcmSupEntityName = "supSup_entityName"
 Global Const conAcmSupEntitySection = "supSup_entitySection"
 Global Const conAcmSupEntityType = "supSup_entityType"
 Global Const conAcmUseLrtCommitPreprocess = "useLrtCommitPreprocess"
 Global Const conAcmUseLrtMqt = "useLrtMqt"
 
 Global Const conLdmDbColumnName = "dbColumnName"
 Global Const conLdmFkSequenceNo = "fkSequenceNo"
 Global Const conLdmIsGen = "isGen"
 Global Const conLdmIsLrt = "isLrt"
 Global Const conLdmIsMqt = "isMqt"
 Global Const conLdmIsNl = "isNl"
 Global Const conLdmSchemaName = "schemaName"
 Global Const conLdmSequenceNo = "sequenceNo"
 Global Const conLdmTableName = "tableName"
 
 Global Const conPdmFkSchemaName = "pdm_schemaName"
 Global Const conPdmLdmFkSchemaName = "ldm_schemaName"
 Global Const conPdmLdmFkTableName = "ldm_tableName"
 Global Const conPdmNativeSchemaName = "nativeSchemaName":            Global Const cosnPdmNativeSchemaName = "nos"
 Global Const conPdmPrimSchemaName = "schemaName"
 Global Const conPdmPrivateSchemaName = "privateSchemaName":          Global Const cosnPdmPrivateSchemaName = "prs"
 Global Const conPdmPublicSchemaName = "publicSchemaName":            Global Const cosnPdmPublicSchemaName = "pus"
 Global Const conPdmSequenceSchemaName = "sequenceSchemaName":        Global Const cosnPdmSequenceSchemaName = "ssn"
 Global Const conPdmTableName = "tableName"
 Global Const conPdmTypedTableName = "pdm_tableName"
 
 Global Const conSpLogContextName = "contextName"
 Global Const conSpLogContextSchema = "contextSchema"
 Global Const conSpLogContextType = "contextType"
 Global Const conSpLogEventTime = "eventTime"
 Global Const conSpLogEventTimeRelative = "eventTimeRelative"
 
 Global Const conAhClassId = "ahClassId":                             Global Const cosnAggHeadClassId = "aci"
 Global Const conAhOId = "ahOId":                                     Global Const cosnAggHeadOId = "aoi"
 Global Const conChangeComment = "changeComment":                     Global Const cosnChangeComment = "ccm"
 Global Const conClassId = "classId":                                 Global Const cosnClassId = "cid"
 Global Const conCreateTimestamp = "createTimestamp":                 Global Const cosnCreateTimestamp = "cts"
 Global Const conCreateUser = "createUser":                           Global Const cosnCreateUser = "cui"
 Global Const conCreateUserName = "createUserName":                   Global Const cosnCreateUserName = "cun"
 Global Const conEndTime = "endTime"
 Global Const conEnumId = "id":                                       Global Const cosnEnumId = "id"
 Global Const conEnumLabelText = "text":                              Global Const cosnEnumLabelText = "txt"
 Global Const conFallBackLanguage = "fallBackLanguage"
 Global Const conIgnoreForChangelog = "ignoreForChangelog"
 Global Const conInLrt = "inLrt":                                     Global Const cosnInLrt = "itr"
 Global Const conInUseBy = "inUseBy":                                 Global Const cosnInUseBy = "iub"
 Global Const conIsLrtPrivate = "isLrtPrivate":                       Global Const cosnIsLrtPrivate = "ilp"
 Global Const conLastOpTime = "lastOpTime"
 Global Const conLastUpdateTimestamp = "lastUpdateTimestamp":         Global Const cosnLastUpdateTimestamp = "uts"
 Global Const conLrtComment = "lrtComment"
 Global Const conLrtOid = "lrtOId"
 Global Const conLrtOpId = "opId":                                    Global Const cosnLrtOpId = "opi"
 Global Const conLrtState = "lrtState":                               Global Const cosnLrtState = "lst"
 Global Const conOid = "oid":                                         Global Const cosnOid = "oid"
 Global Const conOrgOid = "orgOid":                                   Global Const cosnOrgOid = "ooi"
 Global Const conPrimaryLanguage = "primaryLanguage"
 Global Const conTmpPrio = "prio":                                    Global Const cosnTmpPrio = "pri"
 Global Const conTransactionComment = "transactionComment"
 Global Const conUpdateUser = "updateUser":                           Global Const cosnUpdateUser = "uui"
 Global Const conUpdateUserName = "updateUserName":                   Global Const cosnUpdateUserName = "uun"
 Global Const conUserId = "cdUserId"
 Global Const conUserName = "userName":                               Global Const cosnUserName = "unm"
 Global Const conValidFrom = "validFrom":                             Global Const cosnValidFrom = "vft"
 Global Const conValidTo = "validTo":                                 Global Const cosnValidTo = "vut"
 Global Const conVersionId = "versionId":                             Global Const cosnVersionId = "vid"
 Global Const conWorkingState = "workingState"
 
 Global Const conAccessModeId = "accessMode_Id"
 Global Const conEnumRefId = "ref_Id":                                Global Const cosnEnumRefId = "rid"
 Global Const conLanguageId = "language_Id":                          Global Const cosnLanguageId = "lid"
 Global Const conOrganizationId = "organization_Id"
 Global Const conPoolTypeId = "poolType_Id"
 
 ' ############################################
 ' # enum meta information
 ' ############################################
 
 ' en   - enum name
 ' esn  - enum short name
 
 Global Const enLanguage = "Language":                     Global Const esnLanguage = "LAN"
 Global Const enDataPoolAccessMode = "DataPoolAccessMode": Global Const esnDataPoolAccessMode = "DAM"
 
 ' ############################################
 ' # class meta information
 ' ############################################
 
 ' cln   - class name
 ' clxn  - class section name
 
 Global Const clnAcmAttribute = "AcmAttribute":                          Global Const clxnAcmAttribute = snDbMeta
 Global Const clnAcmDomain = "AcmDomain":                                Global Const clxnAcmDomain = snDbMeta
 Global Const clnAcmEntity = "AcmEntity":                                Global Const clxnAcmEntity = snDbMeta
 Global Const clnAcmSection = "AcmSection":                              Global Const clxnAcmSection = snDbMeta
 Global Const clnChangeLog = "Changelog":                                Global Const clxnChangeLog = snChangeLog
 Global Const clnCleanJobs = "CleanJobs":                                Global Const clxnCleanJobs = snDbAdmin
 Global Const clnDataPool = "DataPool":                                  Global Const clxnDataPool = snMeta
 Global Const clnWriteLock = "WriteLock":                                Global Const clxnWriteLock = snMeta
 Global Const clnReleaseLock = "ReleaseLock":                            Global Const clxnReleaseLock = snMeta
 Global Const clnDbCfgProfile = "DbCfgProfile":                          Global Const clxnDbCfgProfile = snDbAdmin
 Global Const clnDbPrivileges = "DbPrivileges":                          Global Const clxnDbPrivileges = snDbAdmin
 Global Const clnDisabledFks = "DisabledForeignKeys":                    Global Const clxnDisabledFks = snDbAdmin
 Global Const clnDisabledIndexes = "DisabledIndexes":                    Global Const clxnDisabledIndexes = snDbAdmin
 Global Const clnDisabledRtDep = "DisabledRoutineDep":                   Global Const clxnDisabledRtDep = snDbAdmin
 Global Const clnDisabledRts = "DisabledRoutines":                       Global Const clxnDisabledRts = snDbAdmin
 Global Const clnDisabledTriggers = "DisabledTriggers":                  Global Const clxnDisabledTriggers = snDbAdmin
 Global Const clnErrMsg = "ErrorMessage":                                Global Const clxnErrMsg = snDbMeta
 Global Const clnErrorMessage = "ErrorMessage":                          Global Const clxnErrorMessage = snDbMeta
 Global Const clnFkDependency = "LdmFkDependency":                       Global Const clxnFkDependency = snDbMeta
 Global Const clnIndexMetrics = "IndexMetrics":                          Global Const clxnIndexMetrics = snDbMonitor
 Global Const clnLdmSchema = "LDMSchema":                                Global Const clxnLdmSchema = snDbMeta
 Global Const clnLdmTable = "LDMTable":                                  Global Const clxnLdmTable = snDbMeta
 Global Const clnLrt = "LRT":                                            Global Const clxnLrt = snLrt
 Global Const clnLrtAffectedEntity = "LrtAffectedEntity":                Global Const clxnLrtAffectedEntity = snLrt
 Global Const clnLrtExecStatus = "LRTExecStatus":                        Global Const clxnLrtExecStatus = snLrt
 Global Const clnPdmPrimarySchema = "PDMPrimarySchema":                  Global Const clxnPdmPrimarySchema = snDbMeta
 Global Const clnPdmSchema = "PDMSchema":                                Global Const clxnPdmSchema = snDbMeta
 Global Const clnPdmTable = "PDMTable":                                  Global Const clxnPdmTable = snDbMeta
 Global Const clnSnapshotCol = "SnapshotCol":                            Global Const clxnSnapshotCol = snDbMonitor
 Global Const clnSnapshotFilter = "SnapshotFltr":                        Global Const clxnSnapshotFilter = snDbMonitor
 Global Const clnSnapshotHandle = "SnapshotHandle":                      Global Const clxnSnapshotHandle = snDbMonitor
 Global Const clnSnapshotType = "SnapshotType":                          Global Const clxnSnapshotType = snDbMonitor
 Global Const clnSnapshotV9Agent = "Snapshot_Agent":                     Global Const clxnSnapshotV9Agent = snDbMonitor
 Global Const clnSnapshotV9AgentMemoryPool = "Snapshot_AgentMemoryPool": Global Const clxnSnapshotV9AgentMemoryPool = snDbMonitor
 Global Const clnSnapshotV9Appl = "Snapshot_Appl":                       Global Const clxnSnapshotV9Appl = snDbMonitor
 Global Const clnSnapshotV9ApplInfo = "Snapshot_ApplInfo":               Global Const clxnSnapshotV9ApplInfo = snDbMonitor
 Global Const clnSnapshotV9Bp = "Snapshot_Bp":                           Global Const clxnSnapshotV9Bp = snDbMonitor
 Global Const clnSnapshotV9BpPart = "Snapshot_BpPart":                   Global Const clxnSnapshotV9BpPart = snDbMonitor
 Global Const clnSnapshotV9Container = "Snapshot_Container":             Global Const clxnSnapshotV9Container = snDbMonitor
 Global Const clnSnapshotV9Db = "Snapshot_Db":                           Global Const clxnSnapshotV9Db = snDbMonitor
 Global Const clnSnapshotV9DbMemoryPool = "Snapshot_DbMemoryPool":       Global Const clxnSnapshotV9DbMemoryPool = snDbMonitor
 Global Const clnSnapshotV9Dbm = "Snapshot_Dbm":                         Global Const clxnSnapshotV9Dbm = snDbMonitor
 Global Const clnSnapshotV9DbmMemoryPool = "Snapshot_DbmMemoryPool":     Global Const clxnSnapshotV9DbmMemoryPool = snDbMonitor
 Global Const clnSnapshotV9DetailLog = "Snapshot_DetailLog":             Global Const clxnSnapshotV9DetailLog = snDbMonitor
 Global Const clnSnapshotV9DynSql = "Snapshot_DynSql":                   Global Const clxnSnapshotV9DynSql = snDbMonitor
 Global Const clnSnapshotV9Fcm = "Snapshot_Fcm":                         Global Const clxnSnapshotV9Fcm = snDbMonitor
 Global Const clnSnapshotV9FcmPart = "Snapshot_FcmPart":                 Global Const clxnSnapshotV9FcmPart = snDbMonitor
 Global Const clnSnapshotV9Hadr = "Snapshot_Hadr":                       Global Const clxnSnapshotV9Hadr = snDbMonitor
 Global Const clnSnapshotV9Lock = "Snapshot_Lock":                       Global Const clxnSnapshotV9Lock = snDbMonitor
 Global Const clnSnapshotV9LockWait = "Snapshot_LockWait":               Global Const clxnSnapshotV9LockWait = snDbMonitor
 Global Const clnSnapshotV9Statement = "Snapshot_Stmt":                  Global Const clxnSnapshotV9Statement = snDbMonitor
 Global Const clnSnapshotV9StoragePaths = "Snapshot_StoragePaths":       Global Const clxnSnapshotV9StoragePaths = snDbMonitor
 Global Const clnSnapshotV9Subsection = "Snapshot_Subsection":           Global Const clxnSnapshotV9Subsection = snDbMonitor
 Global Const clnSnapshotV9Switches = "Snapshot_Switches":               Global Const clxnSnapshotV9Switches = snDbMonitor
 Global Const clnSnapshotV9Tab = "Snapshot_Tab":                         Global Const clxnSnapshotV9Tab = snDbMonitor
 Global Const clnSnapshotV9TabReorg = "Snapshot_TabReorg":               Global Const clxnSnapshotV9TabReorg = snDbMonitor
 Global Const clnSnapshotV9TbSp = "Snapshot_TbSp":                       Global Const clxnSnapshotV9TbSp = snDbMonitor
 Global Const clnSnapshotV9TbSpPart = "Snapshot_TbSpPart":               Global Const clxnSnapshotV9TbSpPart = snDbMonitor
 Global Const clnSnapshotV9TbSpQuiescer = "Snapshot_TbSpQuiescer":       Global Const clxnSnapshotV9TbSpQuiescer = snDbMonitor
 Global Const clnSnapshotV9TbSpRange = "Snapshot_TbSpRange":             Global Const clxnSnapshotV9TbSpRange = snDbMonitor
 Global Const clnSnapshotV9Util = "Snapshot_Util":                       Global Const clxnSnapshotV9Util = snDbMonitor
 Global Const clnSnapshotV9UtilProgress = "Snapshot_UtilProgress":       Global Const clxnSnapshotV9UtilProgress = snDbMonitor
 Global Const clnSqlLog = "SqlLog":                                      Global Const clxnSqlLog = snSpLog
 Global Const clnSqlLogCfg = "SqlLogCfg":                                Global Const clxnSqlLogCfg = snSpLog
 Global Const clnTableCfg = "TableCfg":                                  Global Const clxnTableCfg = snDbAdmin
 ' ### IFNOT IVK ###
 'Global Const clnUser = "User":                                          Global Const clxnUser = snUser
 ' ### ENDIF IVK ###
 
 ' ############################################
 ' # relationship meta information
 ' ############################################
 
 ' rn   - class name
 ' rxn  - class section name
 
 ' ################################################
 '            View Names
 ' ################################################
 
 ' vn   - view name
 ' vsn  - view short name
 
 Global Const vnLdmTabDepOrder = "LDM_TabDepOrder":                     Global Const vnsLdmTabDepOrder = "LTO"
 Global Const vnLdmTabDepChain = "LDM_TabDepChain":                     Global Const vnsLdmTabDepChain = "LTC"
 Global Const vnAcmEntityFkCol = "AcmEntityFkCol":                      Global Const vnsAcmEntityFkCol = "AFK"
 Global Const vnPdmTable = "PdmTable":                                  Global Const vnsPdmTable = "PTA"
 Global Const vnLrtAffectedLdmTab = "LrtAffectedLdmTab":                Global Const vsnLrtAffectedLdmTab = "LAL"
 Global Const vnLrtAffectedPdmTab = "LrtAffectedPdmTab":                Global Const vsnLrtAffectedPdmTab = "LAP"
 Global Const vnPdmExportTabList = "PDM_ExpImpTabList":                 Global Const vnsPdmExportTabList = "ETL"
 Global Const vnPdmImportStmnt = "PDM_Import":                          Global Const vnsPdmImportStmnt = "PIM"
 Global Const vnPdmCoreImportStmnt = "PDM_CoreImport":                  Global Const vnsPdmCoreImportStmnt = "PCI"
 Global Const vnPdmLoadStmnt = "PDM_Load":                              Global Const vnsPdmLoadStmnt = "PLD"
 Global Const vnPdmExportStmnt = "PDM_Export":                          Global Const vnsPdmExportStmnt = "PEX"
 Global Const vnPdmExportStmntMove = "PDM_Move_Export":                 Global Const vnsPdmExportStmntMove = "PME"
 Global Const vnPdmImportStmntMove = "PDM_Move_Import":                 Global Const vnsPdmImportStmntMove = "PMI"
 Global Const vnPdmLoadStmntMove = "PDM_Move_Load":                     Global Const vnsPdmLoadStmntMove = "PML"
 Global Const vnPdmMoveScript = "PDM_Move_Script":                      Global Const vnsPdmMoveScript = "PMS"
 Global Const vnRedirectedRestoreScript = "RedirectedRestore_Script": Global Const vnsRedirectedRestoreScript = "RRS"
 Global Const vnPdmCoreExportStmnt = "PDM_CoreExport":                  Global Const vnsPdmCoreExportStmnt = "PCE"
 Global Const vnSetProdAffectedPdmTab = "SetProdAffectedPdmTab":        Global Const vsnSetProdAffectedPdmTab = "SPP"
 Global Const vnAggHeadTab = "AggHeadTab":                              Global Const vsnAggHeadTab = "AHT"
 Global Const vnAcmCoreEntityId = "ACM_CoreEntityIds":                  Global Const vsnAcmCoreEntityId = "ACE"
 Global Const vnDropTrigger = "TriggerDrop":                            Global Const vsnDropTrigger = "TRD"
 Global Const vnInvalidDbObjects = "InvalidObjects":                    Global Const vsnInvalidDbObjects = "IOB"
 Global Const vnSnapshotV9Agent = "SnapshotAgent":                      Global Const vsnSnapshotV9Agent = "VAG"
 Global Const vnSnapshotV9AgentMemoryPool = "SnapshotAgentMemoryPool":  Global Const vsnSnapshotV9AgentMemoryPool = "SnapshotAgentMemoryPool"
 Global Const vnSnapshotV9Appl = "SnapshotAppl":                        Global Const vsnSnapshotV9Appl = "VAP"
 Global Const vnSnapshotV9ApplInfo = "SnapshotApplInfo":                Global Const vsnSnapshotV9ApplInfo = "VAI"
 Global Const vnSnapshotV9Bp = "SnapshotBp":                            Global Const vsnSnapshotV9Bp = "VBU"
 Global Const vnSnapshotV9BpPart = "SnapshotBpPart":                    Global Const vsnSnapshotV9BpPart = "VBP"
 Global Const vnSnapshotV9Container = "SnapshotContainer":              Global Const vsnSnapshotV9Container = "VCN"
 Global Const vnSnapshotV9Db = "SnapshotDb":                            Global Const vsnSnapshotV9Db = "VDB"
 Global Const vnSnapshotV9DbMemoryPool = "SnapshotDbMemoryPool":        Global Const vsnSnapshotV9DbMemoryPool = "VBM"
 Global Const vnSnapshotV9Dbm = "SnapshotDbm":                          Global Const vsnSnapshotV9Dbm = "VDM"
 Global Const vnSnapshotV9DbmMemoryPool = "SnapshotDbmMemoryPool":      Global Const vsnSnapshotV9DbmMemoryPool = "DMP"
 Global Const vnSnapshotV9DetailLog = "SnapshotDetailLog":              Global Const vsnSnapshotV9DetailLog = "VDL"
 Global Const vnSnapshotV9DynSql = "SnapshotDynSql":                    Global Const vsnSnapshotV9DynSql = "VDS"
 Global Const vnSnapshotV9Fcm = "SnapshotFcm":                          Global Const vsnSnapshotV9Fcm = "VFM"
 Global Const vnSnapshotV9FcmPart = "SnapshotFcmPart":                  Global Const vsnSnapshotV9FcmPart = "VFP"
 Global Const vnSnapshotV9Hadr = "SnapshotHadr":                        Global Const vsnSnapshotV9Hadr = "VHA"
 Global Const vnSnapshotV9Lock = "SnapshotLock":                        Global Const vsnSnapshotV9Lock = "VLK"
 Global Const vnSnapshotV9LockWait = "SnapshotLockWait":                Global Const vsnSnapshotV9LockWait = "VLW"
 Global Const vnSnapshotV9Statement = "SnapshotStmt":                   Global Const vsnSnapshotV9Statement = "VST"
 Global Const vnSnapshotV9StoragePaths = "SnapshotStoragePaths":        Global Const vsnSnapshotV9StoragePaths = "VSP"
 Global Const vnSnapshotV9Subsection = "SnapshotSubsection":            Global Const vsnSnapshotV9Subsection = "VSS"
 Global Const vnSnapshotV9Switches = "SnapshotSwitches":                Global Const vsnSnapshotV9Switches = "VSW"
 Global Const vnSnapshotV9Tab = "SnapshotTab":                          Global Const vsnSnapshotV9Tab = "VTA"
 Global Const vnSnapshotV9TabReorg = "SnapshotTabReorg":                Global Const vsnSnapshotV9TabReorg = "VTR"
 Global Const vnSnapshotV9TbSp = "SnapshotTbSp":                        Global Const vsnSnapshotV9TbSp = "VTS"
 Global Const vnSnapshotV9TbSpPart = "SnapshotTbSpPart":                Global Const vsnSnapshotV9TbSpPart = "VTP"
 Global Const vnSnapshotV9TbSpQuiescer = "SnapshotTbSpQuiescer":        Global Const vsnSnapshotV9TbSpQuiescer = "VTQ"
 Global Const vnSnapshotV9TbSpRange = "SnapshotTbSpRange":              Global Const vsnSnapshotV9TbSpRange = "VTR"
 Global Const vnSnapshotV9UtilProgress = "SnapshotUtilProgress":        Global Const vsnSnapshotV9UtilProgress = "VUR"
 Global Const vnSnapshotV9Util = "SnapshotUtil":                        Global Const vsnSnapshotV9Util = "VUT"
 
 ' ################################################
 '            Stored Procedure Names
 ' ################################################
 
 ' spn   - stored procedure name
 
 Global Const spnAlterTable = "AlterTable"
 Global Const spnAssert = "Assert"
 Global Const spnDivCreate = "DivCreate"
 Global Const spnCheckDb2Register = "checkDb2Register"
 Global Const spnCheckValidity = "CheckValidity"
 Global Const spnCleanData = "CleanData"
 Global Const spnClearLrt = "ClearLrt"
 Global Const spnCompressEstimate = "CompressEstimate"
 Global Const spnCreateLrtAliases = "createLrtAliases"
 Global Const spnDbCompact = "DbCompact"
 Global Const spnDbStrip = "DbStrip"
 Global Const spnDeclTempTablesAutoDeploy = "DeclTempTablesAutoDeploy"
 Global Const spnDropObjects = "DropObjects"
 Global Const spnError = "Error"
 Global Const spnFkDisable = "FkDisable"
 Global Const spnFkEnable = "FkEnable"
 Global Const spnFkCheckAspectCode = "FKCHECK_ASPECT_CODE"
 Global Const spnGenViewSnapshot = "GenView_Snapshot"
 Global Const spnGetDataFixesToDeploy = "GetDataFixesToDeploy"
 Global Const spnGetDb2Level = "getDb2Level"
 Global Const spnGetIndexMetrics = "GetIndexMetrics"
 Global Const spnGetIndexMetricsAnalysis = "GetIndexMetricsAnalysis"
 Global Const spnGetSnapshot = "GetSnapshot"
 Global Const spnGetSnapshotAgent = "GetSnapshotAgent"
 Global Const spnGetSnapshotAnalysis = "GetSnapshotAnalysis"
 Global Const spnGetSnapshotAnalysisAppl = "GetSnapshotAnalysisAppl"
 Global Const spnGetSnapshotAnalysisLockWait = "GetSnapshotAnalysisLockWait"
 Global Const spnGetSnapshotAnalysisStatement = "GetSnapshotAnalysisStatement"
 Global Const spnGetSnapshotAppl = "GetSnapshotAppl"
 Global Const spnGetSnapshotApplInfo = "GetSnapshotApplI"
 Global Const spnGetSnapshotBufferpool = "GetSnapshotBp"
 Global Const spnGetSnapshotContainer = "GetSnapshotCnt"
 Global Const spnGetSnapshotDb = "GetSnapshotDb"
 Global Const spnGetSnapshotDbm = "GetSnapshotDbm"
 Global Const spnGetSnapshotLock = "GetSnapshotLock"
 Global Const spnGetSnapshotLockWait = "GetSnapshotLockWait"
 Global Const spnGetSnapshotSql = "GetSnapshotSql"
 Global Const spnGetSnapshotStatement = "GetSnapshotStmnt"
 Global Const spnGetSnapshotTable = "GetSnapshotTable"
 Global Const spnGetSnapshotTbs = "GetSnapshotTbs"
 Global Const spnGetSnapshotTbsCfg = "GetSnapshotTbsCfg"
 Global Const spnGetSnapshotV9Agent = "GetSnapshotAgent"
 Global Const spnGetSnapshotV9AgentMemoryPool = "GetSnapshotAgentMemoryPool"
 Global Const spnGetSnapshotV9Appl = "GetSnapshotAppl"
 Global Const spnGetSnapshotV9ApplInfo = "GetSnapshotApplInfo"
 Global Const spnGetSnapshotV9Bp = "GetSnapshotBp"
 Global Const spnGetSnapshotV9BpPart = "GetSnapshotBpPart"
 Global Const spnGetSnapshotV9Container = "GetSnapshotContainer"
 Global Const spnGetSnapshotV9Db = "GetSnapshotDb"
 Global Const spnGetSnapshotV9DbMemoryPool = "GetSnapshotDbMemoryPool"
 Global Const spnGetSnapshotV9Dbm = "GetSnapshotDbm"
 Global Const spnGetSnapshotV9DbmMemoryPool = "GetSnapshotDbmMemoryPool"
 Global Const spnGetSnapshotV9DetailLog = "GetSnapshotDetailLog"
 Global Const spnGetSnapshotV9DynSql = "GetSnapshotDynSql"
 Global Const spnGetSnapshotV9Fcm = "GetSnapshotFcm"
 Global Const spnGetSnapshotV9FcmPart = "GetSnapshotFcmPart"
 Global Const spnGetSnapshotV9Hadr = "GetSnapshotHadr"
 Global Const spnGetSnapshotV9Lock = "GetSnapshotLock"
 Global Const spnGetSnapshotV9LockWait = "GetSnapshotLockWait"
 Global Const spnGetSnapshotV9Stmt = "GetSnapshotStmt"
 Global Const spnGetSnapshotV9StoragePaths = "GetSnapshotStoragePaths"
 Global Const spnGetSnapshotV9Subsection = "GetSnapshotSubsection"
 Global Const spnGetSnapshotV9Switches = "GetSnapshotSwitches"
 Global Const spnGetSnapshotV9Tab = "GetSnapshotTab"
 Global Const spnGetSnapshotV9TabReorg = "GetSnapshotTabReorg"
 Global Const spnGetSnapshotV9TbSp = "GetSnapshotTbSp"
 Global Const spnGetSnapshotV9TbSpPart = "GetSnapshotTbSpPart"
 Global Const spnGetSnapshotV9TbSpQuiescer = "GetSnapshotTbSpQuiescer"
 Global Const spnGetSnapshotV9TbSpRange = "GetSnapshotTbSpRange"
 Global Const spnGetSnapshotV9Util = "GetSnapshotUtil"
 Global Const spnGetSnapshotV9UtilProgress = "GetSnapshotUtilProgress"
 Global Const spnGetTabStatus = "GetTabStatus"
 Global Const spnGetstats = "Getstats"
 Global Const spnGrant = "SetGrants"
 Global Const spnHelp = "Help"
 Global Const spnHelpCatchSqlCode = "HelpCatchSqlCode"
 Global Const spnIndexDisable = "IndexDisable"
 Global Const spnIndexEnable = "IndexEnable"
 Global Const spnIntegrity = "SetIntegrity"
 Global Const spnLrtBegin = "LrtBegin"
 Global Const spnLrtCommit = "LrtCommit"
 Global Const spnLrtCommitList = "LrtCommitList"
 Global Const spnLrtCommitPreProc = "LrtCommitPreProc"
 Global Const spnLrtGenChangelog = "LrtGenChangelog"
 Global Const spnLrtGetLog = "LrtGetLog"
 Global Const spnLrtGetLogCard = "LrtGetLogCard"
 Global Const spnLrtMqtSync = "MQTSync"
 Global Const spnLrtRollback = "LrtRollback"
 Global Const spnLrtRollbackList = "LrtRollbackList"
 Global Const spnReCreateSnapshotTables = "SnapshotRecreateTables"
 Global Const spnRebind = "Rebind"
 Global Const spnReorg = "Reorg"
 Global Const spnResetOidSeq = "ResetOidSequence"
 Global Const spnRevalidate = "Revalidate"
 Global Const spnRevoke = "RevokeGrants"
 Global Const spnRowCompEstWrapper = "RowCompEstWrapper"
 Global Const spnRtDisable = "RoutineDisable"
 Global Const spnRtEnable = "RoutineEnable"
 Global Const spnRunstats = "Runstats"
 Global Const spnSetCfg = "SetCfgProfile"
 Global Const spnSetDbCfg = "SetDbCfg"
 Global Const spnSetTabAppend = "SetTabAppend"
 Global Const spnSetTableCfg = "SetTableCfg"
 Global Const spnSnapshotClear = "SnapshotClear"
 Global Const spnSnapshotPrune = "SnapshotPrune"
 Global Const spnTriggerDisable = "TriggerDisable"
 Global Const spnTriggerEnable = "TriggerEnable"
 Global Const spnVerifyCfg = "VerifyCfg"
 
 
 ' ################################################
 '            User Defined Functions
 ' ################################################
 
 ' udfn   - function name
 ' udfsn  - function short name
 
 Global Const udfnAcmLobAttrs = "AcmLobAttrs"
 Global Const udfnApplStatus2Str = "ApplStatus2Str"
 Global Const udfnBoolean2Str = "Boolean2Str"
 Global Const udfnContType2Str = "ContType2Str"
 Global Const udfnDb2Release = "Db2Release"
 Global Const udfnDbStatus2Str = "DbStatus2Str"
 Global Const udfnDbTabColList = "GetTabColList"
 Global Const udfnDbmStatus2Str = "DbmStatus2Str"
 Global Const udfnGetLrtTargetStatus = "GetLrtTgs"
 Global Const udfnGetSchema = "GetSchema"
 Global Const udfnGetStrElem = "GetStrElem"
 Global Const udfnGetSubClassIds = "GetSubClassIds"
 Global Const udfnGetSubClassIdsByList = "GetSubClassIdsByList"
 Global Const udfnIsNumeric = "IsNumeric"
 Global Const udfnIsSubset = "isSubset"
 Global Const udfnLastStrElem = "LastStrElem"
 Global Const udfnLockMode2Str = "LockMode2Str"
 Global Const udfnLockObjType2Str = "LockObjType2Str"
 Global Const udfnLockObjType2StrS = "LockOT2Str_S"
 Global Const udfnLockStatus2Str = "LockStatus2Str"
 Global Const udfnLrtGetOid = "LrtGetOid"
 Global Const udfnOccurs = "Occurs"
 Global Const udfnOccursShort = "Occurs_S"
 Global Const udfnParseClassIdOidList = "ParseClassOidList"
 Global Const udfnParseDataPools = "ParseDataPools"
 Global Const udfnPdmSchemaName = "PdmSchemaName"
 Global Const udfnPlatform2Str = "Platform2Str"
 Global Const udfnPosStr = "PosStr"
 Global Const udfnProtocol2Str = "Protocol2Str"
 Global Const udfnSnapshotCols = "SCL"
 Global Const udfnStmntOp2Str = "StmntOp2Str"
 Global Const udfnStmntType2Str = "StmntType2Str"
 Global Const udfnStrElemIndexes = "StrElemIndexes"
 Global Const udfnStrElems = "StrElems"
 Global Const udfnStrListMap = "StrListMap"
 Global Const udfnStrTrim = "Trim"
 Global Const udfnTabType2Str = "TabType2Str"
 Global Const udfnTsContType2Str = "TsContType2Str"
 Global Const udfnTsState2Str = "TsState2Str"
 Global Const udfnTsType2Str = "TsType2Str"
 
 
