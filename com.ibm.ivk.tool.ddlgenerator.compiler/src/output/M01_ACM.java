package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_ACM {



// ############################################
// # section meta information
// ############################################

// sn   - section name
// ssn  - section short name

public static final String snLrt = "LRT";
public static final String ssnLrt = "LRT";
public static final String snCommon = "Common";
public static final String ssnCommon = "CMN";
public static final String snMeta = "Meta";
public static final String ssnMeta = "MET";
public static final String snCountry = "Country";
public static final String ssnCountry = "CNT";
public static final String snDbAdmin = "DbAdmin";
public static final String ssnDbAdmin = "DBA";
public static final String snDb = "DB";
public static final String ssnDb = "DB";
public static final String snDbMeta = "DbMeta";
public static final String ssnDbMeta = "DBM";
public static final String snDbMonitor = "DbMonitor";
public static final String ssnDbMonitor = "MON";
public static final String snHelp = "Help";
public static final String ssnHelp = "HLP";
public static final String snSpLog = "Log";
public static final String ssnSpLog = "LOG";
public static final String snTrace = "Trace";
public static final String ssnTrace = "TRC";
public static final String snAlias = "ALIAS";
public static final String ssnAlias = "AL";
public static final String snAliasLrt = "ALIAS_LRT";
public static final String ssnAliasLrt = "ALL";
public static final String snAliasPrivateOnly = "ALIAS_LRT_PRI";
public static final String ssnAliasPrivateOnly = "ALP";
public static final String snAliasPublicOnly = "ALIAS_LRT_PUB";
public static final String ssnAliasPublicOnly = "ALO";
public static final String snUser = "User";
public static final String ssnUser = "USR";
public static final String snChangeLog = "Changelog";
public static final String ssnChangeLog = "CLG";
public static final String snPackageReopt = "PackageReopt";
public static final String ssnPackageReopt = "ROP";

// ############################################
// # domain meta information
// ############################################

// dn   - domain name
// dxn  - domain section name

public static final String dnOid = "OID";
public static final String dxnOid = M01_ACM.snCommon;
public static final String dnInteger = "Integer";
public static final String dxnInteger = M01_ACM.snCommon;
public static final String dnModTimestamp = "ModTimestamp";
public static final String dxnModTimestamp = M01_ACM.snCommon;
public static final String dnLrt = "LRT";
public static final String dsnLrt = "Lrt";
public static final String dnInUseBy = M01_ACM.dnLrt;
public static final String dxnInUseBy = M01_ACM.dsnLrt;
public static final String dnLrtLabel = "Label";
public static final String dxnLrtLabel = M01_ACM.snLrt;
public static final String dnBoolean = "Boolean";
public static final String dxnBoolean = M01_ACM.snCommon;
public static final String dnDbRelease = "DbRelease";
public static final String dxnDbRelease = M01_ACM.snDbMeta;
public static final String dnNumber = "Number";
public static final String dxnNumber = M01_ACM.snCommon;
public static final String dnSmallNumber = "SmallNumber";
public static final String dxnSmallNumber = M01_ACM.snCommon;
public static final String dnWorkingState = "WorkingState";
public static final String dxnWorkingState = M01_ACM.snCommon;
public static final String dnUserId = "UserId";
public static final String dxnUserId = M01_ACM.snUser;
public static final String dnUserIdAlt = "UserIdAlt";
public static final String dnUserName = "UserName";
public static final String dxnUserName = M01_ACM.snUser;
public static final String dnVersion = "Version";
public static final String dxnVersion = M01_ACM.snCommon;
public static final String dnPdmTableName = "TableName";
public static final String dxnPdmTableName = M01_ACM.snMeta;
public static final String dnPdmColumnName = "ColumnName";
public static final String dxnPdmColumnName = M01_ACM.snMeta;
public static final String dnEntityType = "AcmEntityType";
public static final String dxnEntityType = M01_ACM.snDbMeta;
public static final String dnClassId = "ClassId";
public static final String dxnClassId = M01_ACM.snCommon;
public static final String dnChangeComment = "ChangeComment";
public static final String dxnChangeComment = "Lrt";
public static final String dnLrtStatus = "LRTStatus";
public static final String dxnLrtStatus = "Lrt";
public static final String dnEnumId = "EnumId";
public static final String dxnEnumId = "Meta";
public static final String dnChangeLogString = "AttrValueString";
public static final String dxnChangeLogString = "ChangeLog";

public static final String dnDbSchemaName = "DbSchemaName";
public static final String dxnDbSchemaName = M01_ACM.snDbMeta;
public static final String dnDbTableName = "DbTableName";
public static final String dxnDbTableName = M01_ACM.snDbMeta;
public static final String dnDbColumnName = "DbColumnName";
public static final String dxnDbColumnName = M01_ACM.snDbMeta;
public static final String dnDbViewName = "DbViewName";
public static final String dxnDbViewName = M01_ACM.snDbMeta;
public static final String dnDbProcName = "DbProcName";
public static final String dxnDbProcName = M01_ACM.snDbMeta;
public static final String dnDbFuncName = "DbFuncName";
public static final String dxnDbFuncName = M01_ACM.snDbMeta;

// ############################################
// # column meta information
// ############################################

// con   - column name
// cosn  - column short name

public static final String conAcmAliasShortName = "aliasShortName";
public static final String conAcmAttributeLabel = "attributeLabel";
public static final String conAcmAttributeName = "attributeName";
public static final String conAcmDbDataType = "dbDataType";
public static final String conAcmDomainName = "domainName";
public static final String conAcmDomainSection = "domainSection";
public static final String conAcmEntityId = "entityId";
public static final String conAcmEntityLabel = "entityLabel";
public static final String conAcmEntityName = "entityName";
public static final String conAcmEntitySection = "entitySection";
public static final String conAcmEntityShortName = "entityShortName";
public static final String conAcmEntityType = "entityType";
public static final String conAcmIgnoreForChangelog = "ignoreForChangelog";
public static final String conAcmIsAbstract = "isAbstract";
public static final String conAcmIsCto = "isCto";
public static final String conAcmIsCtp = "isCtp";
public static final String conAcmIsRangePartAll = "isRangePartAll";
public static final String conAcmIsEnforced = "isEnforced";
public static final String conAcmIsGen = "isGen";
public static final String conAcmIsLogChange = "isLogChange";
public static final String conAcmIsLrt = "isLrt";
public static final String conAcmIsLrtMeta = "isLrtMeta";
public static final String conAcmIsTv = "isTv";
public static final String conAcmIsVirtual = "isVirtual";
public static final String conAcmLeftEntityName = "left_entityName";
public static final String conAcmLeftEntitySection = "left_entitySection";
public static final String conAcmLeftEntityType = "left_entityType";
public static final String conAcmLrShortName = "lrShortName";
public static final String conAcmMaxLeftCardinality = "maxLeftCardinality";
public static final String conAcmMaxRightCardinality = "maxRightCardinality";
public static final String conAcmMinLeftCardinality = "minLeftCardinality";
public static final String conAcmMinRightCardinality = "minRightCardinality";
public static final String conAcmOrParEntityId = "orParentEntityId";
public static final String conAcmOrParEntityName = "orpPar_entityName";
public static final String conAcmOrParEntitySection = "orpPar_entitySection";
public static final String conAcmOrParEntityType = "orpPar_entityType";
public static final String conAcmRightEntityName = "right_entityName";
public static final String conAcmRightEntitySection = "right_entitySection";
public static final String conAcmRightEntityType = "right_entityType";
public static final String conAcmRlShortName = "rlShortName";
public static final String conAcmSupEntityName = "supSup_entityName";
public static final String conAcmSupEntitySection = "supSup_entitySection";
public static final String conAcmSupEntityType = "supSup_entityType";
public static final String conAcmUseLrtCommitPreprocess = "useLrtCommitPreprocess";
public static final String conAcmUseLrtMqt = "useLrtMqt";

public static final String conLdmDbColumnName = "dbColumnName";
public static final String conLdmFkSequenceNo = "fkSequenceNo";
public static final String conLdmIsGen = "isGen";
public static final String conLdmIsLrt = "isLrt";
public static final String conLdmIsMqt = "isMqt";
public static final String conLdmIsNl = "isNl";
public static final String conLdmSchemaName = "schemaName";
public static final String conLdmSequenceNo = "sequenceNo";
public static final String conLdmTableName = "tableName";

public static final String conPdmFkSchemaName = "pdm_schemaName";
public static final String conPdmLdmFkSchemaName = "ldm_schemaName";
public static final String conPdmLdmFkTableName = "ldm_tableName";
public static final String conPdmNativeSchemaName = "nativeSchemaName";
public static final String cosnPdmNativeSchemaName = "nos";
public static final String conPdmPrimSchemaName = "schemaName";
public static final String conPdmPrivateSchemaName = "privateSchemaName";
public static final String cosnPdmPrivateSchemaName = "prs";
public static final String conPdmPublicSchemaName = "publicSchemaName";
public static final String cosnPdmPublicSchemaName = "pus";
public static final String conPdmSequenceSchemaName = "sequenceSchemaName";
public static final String cosnPdmSequenceSchemaName = "ssn";
public static final String conPdmTableName = "tableName";
public static final String conPdmTypedTableName = "pdm_tableName";

public static final String conSpLogContextName = "contextName";
public static final String conSpLogContextSchema = "contextSchema";
public static final String conSpLogContextType = "contextType";
public static final String conSpLogEventTime = "eventTime";
public static final String conSpLogEventTimeRelative = "eventTimeRelative";

public static final String conAhClassId = "ahClassId";
public static final String cosnAggHeadClassId = "aci";
public static final String conAhOId = "ahOId";
public static final String cosnAggHeadOId = "aoi";
public static final String conChangeComment = "changeComment";
public static final String cosnChangeComment = "ccm";
public static final String conClassId = "classId";
public static final String cosnClassId = "cid";
public static final String conCreateTimestamp = "createTimestamp";
public static final String cosnCreateTimestamp = "cts";
public static final String conCreateUser = "createUser";
public static final String cosnCreateUser = "cui";
public static final String conCreateUserName = "createUserName";
public static final String cosnCreateUserName = "cun";
public static final String conEndTime = "endTime";
public static final String conEnumId = "id";
public static final String cosnEnumId = "id";
public static final String conEnumLabelText = "text";
public static final String cosnEnumLabelText = "txt";
public static final String conFallBackLanguage = "fallBackLanguage";
public static final String conIgnoreForChangelog = "ignoreForChangelog";
public static final String conInLrt = "inLrt";
public static final String cosnInLrt = "itr";
public static final String conInUseBy = "inUseBy";
public static final String cosnInUseBy = "iub";
public static final String conIsLrtPrivate = "isLrtPrivate";
public static final String cosnIsLrtPrivate = "ilp";
public static final String conLastOpTime = "lastOpTime";
public static final String conLastUpdateTimestamp = "lastUpdateTimestamp";
public static final String cosnLastUpdateTimestamp = "uts";
public static final String conLrtComment = "lrtComment";
public static final String conLrtOid = "lrtOId";
public static final String conLrtOpId = "opId";
public static final String cosnLrtOpId = "opi";
public static final String conLrtState = "lrtState";
public static final String cosnLrtState = "lst";
public static final String conOid = "oid";
public static final String cosnOid = "oid";
public static final String conOrgOid = "orgOid";
public static final String cosnOrgOid = "ooi";
public static final String conPrimaryLanguage = "primaryLanguage";
public static final String conTmpPrio = "prio";
public static final String cosnTmpPrio = "pri";
public static final String conTransactionComment = "transactionComment";
public static final String conUpdateUser = "updateUser";
public static final String cosnUpdateUser = "uui";
public static final String conUpdateUserName = "updateUserName";
public static final String cosnUpdateUserName = "uun";
public static final String conUserId = "cdUserId";
public static final String conUserName = "userName";
public static final String cosnUserName = "unm";
public static final String conValidFrom = "validFrom";
public static final String cosnValidFrom = "vft";
public static final String conValidTo = "validTo";
public static final String cosnValidTo = "vut";
public static final String conVersionId = "versionId";
public static final String cosnVersionId = "vid";
public static final String conWorkingState = "workingState";

public static final String conAccessModeId = "accessMode_Id";
public static final String conEnumRefId = "ref_Id";
public static final String cosnEnumRefId = "rid";
public static final String conLanguageId = "language_Id";
public static final String cosnLanguageId = "lid";
public static final String conOrganizationId = "organization_Id";
public static final String conPoolTypeId = "poolType_Id";

// ############################################
// # enum meta information
// ############################################

// en   - enum name
// esn  - enum short name

public static final String enLanguage = "Language";
public static final String esnLanguage = "LAN";
public static final String enDataPoolAccessMode = "DataPoolAccessMode";
public static final String esnDataPoolAccessMode = "DAM";

// ############################################
// # class meta information
// ############################################

// cln   - class name
// clxn  - class section name

public static final String clnAcmAttribute = "AcmAttribute";
public static final String clxnAcmAttribute = M01_ACM.snDbMeta;
public static final String clnAcmDomain = "AcmDomain";
public static final String clxnAcmDomain = M01_ACM.snDbMeta;
public static final String clnAcmEntity = "AcmEntity";
public static final String clxnAcmEntity = M01_ACM.snDbMeta;
public static final String clnAcmSection = "AcmSection";
public static final String clxnAcmSection = M01_ACM.snDbMeta;
public static final String clnChangeLog = "Changelog";
public static final String clxnChangeLog = M01_ACM.snChangeLog;
public static final String clnCleanJobs = "CleanJobs";
public static final String clxnCleanJobs = M01_ACM.snDbAdmin;
public static final String clnDataPool = "DataPool";
public static final String clxnDataPool = M01_ACM.snMeta;
public static final String clnWriteLock = "WriteLock";
public static final String clxnWriteLock = M01_ACM.snMeta;
public static final String clnReleaseLock = "ReleaseLock";
public static final String clxnReleaseLock = M01_ACM.snMeta;
public static final String clnDbCfgProfile = "DbCfgProfile";
public static final String clxnDbCfgProfile = M01_ACM.snDbAdmin;
public static final String clnDbPrivileges = "DbPrivileges";
public static final String clxnDbPrivileges = M01_ACM.snDbAdmin;
public static final String clnDisabledFks = "DisabledForeignKeys";
public static final String clxnDisabledFks = M01_ACM.snDbAdmin;
public static final String clnDisabledIndexes = "DisabledIndexes";
public static final String clxnDisabledIndexes = M01_ACM.snDbAdmin;
public static final String clnDisabledRtDep = "DisabledRoutineDep";
public static final String clxnDisabledRtDep = M01_ACM.snDbAdmin;
public static final String clnDisabledRts = "DisabledRoutines";
public static final String clxnDisabledRts = M01_ACM.snDbAdmin;
public static final String clnDisabledTriggers = "DisabledTriggers";
public static final String clxnDisabledTriggers = M01_ACM.snDbAdmin;
public static final String clnErrMsg = "ErrorMessage";
public static final String clxnErrMsg = M01_ACM.snDbMeta;
public static final String clnErrorMessage = "ErrorMessage";
public static final String clxnErrorMessage = M01_ACM.snDbMeta;
public static final String clnFkDependency = "LdmFkDependency";
public static final String clxnFkDependency = M01_ACM.snDbMeta;
public static final String clnIndexMetrics = "IndexMetrics";
public static final String clxnIndexMetrics = M01_ACM.snDbMonitor;
public static final String clnLdmSchema = "LDMSchema";
public static final String clxnLdmSchema = M01_ACM.snDbMeta;
public static final String clnLdmTable = "LDMTable";
public static final String clxnLdmTable = M01_ACM.snDbMeta;
public static final String clnLrt = "LRT";
public static final String clxnLrt = M01_ACM.snLrt;
public static final String clnLrtAffectedEntity = "LrtAffectedEntity";
public static final String clxnLrtAffectedEntity = M01_ACM.snLrt;
public static final String clnLrtExecStatus = "LRTExecStatus";
public static final String clxnLrtExecStatus = M01_ACM.snLrt;
public static final String clnPdmPrimarySchema = "PDMPrimarySchema";
public static final String clxnPdmPrimarySchema = M01_ACM.snDbMeta;
public static final String clnPdmSchema = "PDMSchema";
public static final String clxnPdmSchema = M01_ACM.snDbMeta;
public static final String clnPdmTable = "PDMTable";
public static final String clxnPdmTable = M01_ACM.snDbMeta;
public static final String clnSnapshotCol = "SnapshotCol";
public static final String clxnSnapshotCol = M01_ACM.snDbMonitor;
public static final String clnSnapshotFilter = "SnapshotFltr";
public static final String clxnSnapshotFilter = M01_ACM.snDbMonitor;
public static final String clnSnapshotHandle = "SnapshotHandle";
public static final String clxnSnapshotHandle = M01_ACM.snDbMonitor;
public static final String clnSnapshotType = "SnapshotType";
public static final String clxnSnapshotType = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Agent = "Snapshot_Agent";
public static final String clxnSnapshotV9Agent = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9AgentMemoryPool = "Snapshot_AgentMemoryPool";
public static final String clxnSnapshotV9AgentMemoryPool = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Appl = "Snapshot_Appl";
public static final String clxnSnapshotV9Appl = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9ApplInfo = "Snapshot_ApplInfo";
public static final String clxnSnapshotV9ApplInfo = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Bp = "Snapshot_Bp";
public static final String clxnSnapshotV9Bp = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9BpPart = "Snapshot_BpPart";
public static final String clxnSnapshotV9BpPart = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Container = "Snapshot_Container";
public static final String clxnSnapshotV9Container = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Db = "Snapshot_Db";
public static final String clxnSnapshotV9Db = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9DbMemoryPool = "Snapshot_DbMemoryPool";
public static final String clxnSnapshotV9DbMemoryPool = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Dbm = "Snapshot_Dbm";
public static final String clxnSnapshotV9Dbm = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9DbmMemoryPool = "Snapshot_DbmMemoryPool";
public static final String clxnSnapshotV9DbmMemoryPool = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9DetailLog = "Snapshot_DetailLog";
public static final String clxnSnapshotV9DetailLog = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9DynSql = "Snapshot_DynSql";
public static final String clxnSnapshotV9DynSql = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Fcm = "Snapshot_Fcm";
public static final String clxnSnapshotV9Fcm = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9FcmPart = "Snapshot_FcmPart";
public static final String clxnSnapshotV9FcmPart = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Hadr = "Snapshot_Hadr";
public static final String clxnSnapshotV9Hadr = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Lock = "Snapshot_Lock";
public static final String clxnSnapshotV9Lock = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9LockWait = "Snapshot_LockWait";
public static final String clxnSnapshotV9LockWait = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Statement = "Snapshot_Stmt";
public static final String clxnSnapshotV9Statement = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9StoragePaths = "Snapshot_StoragePaths";
public static final String clxnSnapshotV9StoragePaths = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Subsection = "Snapshot_Subsection";
public static final String clxnSnapshotV9Subsection = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Switches = "Snapshot_Switches";
public static final String clxnSnapshotV9Switches = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Tab = "Snapshot_Tab";
public static final String clxnSnapshotV9Tab = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9TabReorg = "Snapshot_TabReorg";
public static final String clxnSnapshotV9TabReorg = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9TbSp = "Snapshot_TbSp";
public static final String clxnSnapshotV9TbSp = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9TbSpPart = "Snapshot_TbSpPart";
public static final String clxnSnapshotV9TbSpPart = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9TbSpQuiescer = "Snapshot_TbSpQuiescer";
public static final String clxnSnapshotV9TbSpQuiescer = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9TbSpRange = "Snapshot_TbSpRange";
public static final String clxnSnapshotV9TbSpRange = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9Util = "Snapshot_Util";
public static final String clxnSnapshotV9Util = M01_ACM.snDbMonitor;
public static final String clnSnapshotV9UtilProgress = "Snapshot_UtilProgress";
public static final String clxnSnapshotV9UtilProgress = M01_ACM.snDbMonitor;
public static final String clnSqlLog = "SqlLog";
public static final String clxnSqlLog = M01_ACM.snSpLog;
public static final String clnSqlLogCfg = "SqlLogCfg";
public static final String clxnSqlLogCfg = M01_ACM.snSpLog;
public static final String clnTableCfg = "TableCfg";
public static final String clxnTableCfg = M01_ACM.snDbAdmin;
// ### IFNOT IVK ###
//Global Const clnUser = "User":                                          Global Const clxnUser = snUser
// ### ENDIF IVK ###

// ############################################
// # relationship meta information
// ############################################

// rn   - class name
// rxn  - class section name

// ################################################
//            View Names
// ################################################

// vn   - view name
// vsn  - view short name

public static final String vnLdmTabDepOrder = "LDM_TabDepOrder";
public static final String vnsLdmTabDepOrder = "LTO";
public static final String vnLdmTabDepChain = "LDM_TabDepChain";
public static final String vnsLdmTabDepChain = "LTC";
public static final String vnAcmEntityFkCol = "AcmEntityFkCol";
public static final String vnsAcmEntityFkCol = "AFK";
public static final String vnPdmTable = "PdmTable";
public static final String vnsPdmTable = "PTA";
public static final String vnLrtAffectedLdmTab = "LrtAffectedLdmTab";
public static final String vsnLrtAffectedLdmTab = "LAL";
public static final String vnLrtAffectedPdmTab = "LrtAffectedPdmTab";
public static final String vsnLrtAffectedPdmTab = "LAP";
public static final String vnPdmExportTabList = "PDM_ExpImpTabList";
public static final String vnsPdmExportTabList = "ETL";
public static final String vnPdmImportStmnt = "PDM_Import";
public static final String vnsPdmImportStmnt = "PIM";
public static final String vnPdmCoreImportStmnt = "PDM_CoreImport";
public static final String vnsPdmCoreImportStmnt = "PCI";
public static final String vnPdmLoadStmnt = "PDM_Load";
public static final String vnsPdmLoadStmnt = "PLD";
public static final String vnPdmExportStmnt = "PDM_Export";
public static final String vnsPdmExportStmnt = "PEX";
public static final String vnPdmExportStmntMove = "PDM_Move_Export";
public static final String vnsPdmExportStmntMove = "PME";
public static final String vnPdmImportStmntMove = "PDM_Move_Import";
public static final String vnsPdmImportStmntMove = "PMI";
public static final String vnPdmLoadStmntMove = "PDM_Move_Load";
public static final String vnsPdmLoadStmntMove = "PML";
public static final String vnPdmMoveScript = "PDM_Move_Script";
public static final String vnsPdmMoveScript = "PMS";
public static final String vnRedirectedRestoreScript = "RedirectedRestore_Script";
public static final String vnsRedirectedRestoreScript = "RRS";
public static final String vnPdmCoreExportStmnt = "PDM_CoreExport";
public static final String vnsPdmCoreExportStmnt = "PCE";
public static final String vnSetProdAffectedPdmTab = "SetProdAffectedPdmTab";
public static final String vsnSetProdAffectedPdmTab = "SPP";
public static final String vnAggHeadTab = "AggHeadTab";
public static final String vsnAggHeadTab = "AHT";
public static final String vnAcmCoreEntityId = "ACM_CoreEntityIds";
public static final String vsnAcmCoreEntityId = "ACE";
public static final String vnDropTrigger = "TriggerDrop";
public static final String vsnDropTrigger = "TRD";
public static final String vnInvalidDbObjects = "InvalidObjects";
public static final String vsnInvalidDbObjects = "IOB";
public static final String vnSnapshotV9Agent = "SnapshotAgent";
public static final String vsnSnapshotV9Agent = "VAG";
public static final String vnSnapshotV9AgentMemoryPool = "SnapshotAgentMemoryPool";
public static final String vsnSnapshotV9AgentMemoryPool = "SnapshotAgentMemoryPool";
public static final String vnSnapshotV9Appl = "SnapshotAppl";
public static final String vsnSnapshotV9Appl = "VAP";
public static final String vnSnapshotV9ApplInfo = "SnapshotApplInfo";
public static final String vsnSnapshotV9ApplInfo = "VAI";
public static final String vnSnapshotV9Bp = "SnapshotBp";
public static final String vsnSnapshotV9Bp = "VBU";
public static final String vnSnapshotV9BpPart = "SnapshotBpPart";
public static final String vsnSnapshotV9BpPart = "VBP";
public static final String vnSnapshotV9Container = "SnapshotContainer";
public static final String vsnSnapshotV9Container = "VCN";
public static final String vnSnapshotV9Db = "SnapshotDb";
public static final String vsnSnapshotV9Db = "VDB";
public static final String vnSnapshotV9DbMemoryPool = "SnapshotDbMemoryPool";
public static final String vsnSnapshotV9DbMemoryPool = "VBM";
public static final String vnSnapshotV9Dbm = "SnapshotDbm";
public static final String vsnSnapshotV9Dbm = "VDM";
public static final String vnSnapshotV9DbmMemoryPool = "SnapshotDbmMemoryPool";
public static final String vsnSnapshotV9DbmMemoryPool = "DMP";
public static final String vnSnapshotV9DetailLog = "SnapshotDetailLog";
public static final String vsnSnapshotV9DetailLog = "VDL";
public static final String vnSnapshotV9DynSql = "SnapshotDynSql";
public static final String vsnSnapshotV9DynSql = "VDS";
public static final String vnSnapshotV9Fcm = "SnapshotFcm";
public static final String vsnSnapshotV9Fcm = "VFM";
public static final String vnSnapshotV9FcmPart = "SnapshotFcmPart";
public static final String vsnSnapshotV9FcmPart = "VFP";
public static final String vnSnapshotV9Hadr = "SnapshotHadr";
public static final String vsnSnapshotV9Hadr = "VHA";
public static final String vnSnapshotV9Lock = "SnapshotLock";
public static final String vsnSnapshotV9Lock = "VLK";
public static final String vnSnapshotV9LockWait = "SnapshotLockWait";
public static final String vsnSnapshotV9LockWait = "VLW";
public static final String vnSnapshotV9Statement = "SnapshotStmt";
public static final String vsnSnapshotV9Statement = "VST";
public static final String vnSnapshotV9StoragePaths = "SnapshotStoragePaths";
public static final String vsnSnapshotV9StoragePaths = "VSP";
public static final String vnSnapshotV9Subsection = "SnapshotSubsection";
public static final String vsnSnapshotV9Subsection = "VSS";
public static final String vnSnapshotV9Switches = "SnapshotSwitches";
public static final String vsnSnapshotV9Switches = "VSW";
public static final String vnSnapshotV9Tab = "SnapshotTab";
public static final String vsnSnapshotV9Tab = "VTA";
public static final String vnSnapshotV9TabReorg = "SnapshotTabReorg";
public static final String vsnSnapshotV9TabReorg = "VTR";
public static final String vnSnapshotV9TbSp = "SnapshotTbSp";
public static final String vsnSnapshotV9TbSp = "VTS";
public static final String vnSnapshotV9TbSpPart = "SnapshotTbSpPart";
public static final String vsnSnapshotV9TbSpPart = "VTP";
public static final String vnSnapshotV9TbSpQuiescer = "SnapshotTbSpQuiescer";
public static final String vsnSnapshotV9TbSpQuiescer = "VTQ";
public static final String vnSnapshotV9TbSpRange = "SnapshotTbSpRange";
public static final String vsnSnapshotV9TbSpRange = "VTR";
public static final String vnSnapshotV9UtilProgress = "SnapshotUtilProgress";
public static final String vsnSnapshotV9UtilProgress = "VUR";
public static final String vnSnapshotV9Util = "SnapshotUtil";
public static final String vsnSnapshotV9Util = "VUT";

// ################################################
//            Stored Procedure Names
// ################################################

// spn   - stored procedure name

public static final String spnAlterTable = "AlterTable";
public static final String spnAssert = "Assert";
public static final String spnDivCreate = "DivCreate";
public static final String spnCheckDb2Register = "checkDb2Register";
public static final String spnCheckValidity = "CheckValidity";
public static final String spnCleanData = "CleanData";
public static final String spnClearLrt = "ClearLrt";
public static final String spnCompressEstimate = "CompressEstimate";
public static final String spnCreateLrtAliases = "createLrtAliases";
public static final String spnDbCompact = "DbCompact";
public static final String spnDbStrip = "DbStrip";
public static final String spnDeclTempTablesAutoDeploy = "DeclTempTablesAutoDeploy";
public static final String spnDropObjects = "DropObjects";
public static final String spnError = "Error";
public static final String spnFkDisable = "FkDisable";
public static final String spnFkEnable = "FkEnable";
public static final String spnFkCheckAspectCode = "FKCHECK_ASPECT_CODE";
public static final String spnGenViewSnapshot = "GenView_Snapshot";
public static final String spnGetDataFixesToDeploy = "GetDataFixesToDeploy";
public static final String spnGetDb2Level = "getDb2Level";
public static final String spnGetIndexMetrics = "GetIndexMetrics";
public static final String spnGetIndexMetricsAnalysis = "GetIndexMetricsAnalysis";
public static final String spnGetSnapshot = "GetSnapshot";
public static final String spnGetSnapshotAgent = "GetSnapshotAgent";
public static final String spnGetSnapshotAnalysis = "GetSnapshotAnalysis";
public static final String spnGetSnapshotAnalysisAppl = "GetSnapshotAnalysisAppl";
public static final String spnGetSnapshotAnalysisLockWait = "GetSnapshotAnalysisLockWait";
public static final String spnGetSnapshotAnalysisStatement = "GetSnapshotAnalysisStatement";
public static final String spnGetSnapshotAppl = "GetSnapshotAppl";
public static final String spnGetSnapshotApplInfo = "GetSnapshotApplI";
public static final String spnGetSnapshotBufferpool = "GetSnapshotBp";
public static final String spnGetSnapshotContainer = "GetSnapshotCnt";
public static final String spnGetSnapshotDb = "GetSnapshotDb";
public static final String spnGetSnapshotDbm = "GetSnapshotDbm";
public static final String spnGetSnapshotLock = "GetSnapshotLock";
public static final String spnGetSnapshotLockWait = "GetSnapshotLockWait";
public static final String spnGetSnapshotSql = "GetSnapshotSql";
public static final String spnGetSnapshotStatement = "GetSnapshotStmnt";
public static final String spnGetSnapshotTable = "GetSnapshotTable";
public static final String spnGetSnapshotTbs = "GetSnapshotTbs";
public static final String spnGetSnapshotTbsCfg = "GetSnapshotTbsCfg";
public static final String spnGetSnapshotV9Agent = "GetSnapshotAgent";
public static final String spnGetSnapshotV9AgentMemoryPool = "GetSnapshotAgentMemoryPool";
public static final String spnGetSnapshotV9Appl = "GetSnapshotAppl";
public static final String spnGetSnapshotV9ApplInfo = "GetSnapshotApplInfo";
public static final String spnGetSnapshotV9Bp = "GetSnapshotBp";
public static final String spnGetSnapshotV9BpPart = "GetSnapshotBpPart";
public static final String spnGetSnapshotV9Container = "GetSnapshotContainer";
public static final String spnGetSnapshotV9Db = "GetSnapshotDb";
public static final String spnGetSnapshotV9DbMemoryPool = "GetSnapshotDbMemoryPool";
public static final String spnGetSnapshotV9Dbm = "GetSnapshotDbm";
public static final String spnGetSnapshotV9DbmMemoryPool = "GetSnapshotDbmMemoryPool";
public static final String spnGetSnapshotV9DetailLog = "GetSnapshotDetailLog";
public static final String spnGetSnapshotV9DynSql = "GetSnapshotDynSql";
public static final String spnGetSnapshotV9Fcm = "GetSnapshotFcm";
public static final String spnGetSnapshotV9FcmPart = "GetSnapshotFcmPart";
public static final String spnGetSnapshotV9Hadr = "GetSnapshotHadr";
public static final String spnGetSnapshotV9Lock = "GetSnapshotLock";
public static final String spnGetSnapshotV9LockWait = "GetSnapshotLockWait";
public static final String spnGetSnapshotV9Stmt = "GetSnapshotStmt";
public static final String spnGetSnapshotV9StoragePaths = "GetSnapshotStoragePaths";
public static final String spnGetSnapshotV9Subsection = "GetSnapshotSubsection";
public static final String spnGetSnapshotV9Switches = "GetSnapshotSwitches";
public static final String spnGetSnapshotV9Tab = "GetSnapshotTab";
public static final String spnGetSnapshotV9TabReorg = "GetSnapshotTabReorg";
public static final String spnGetSnapshotV9TbSp = "GetSnapshotTbSp";
public static final String spnGetSnapshotV9TbSpPart = "GetSnapshotTbSpPart";
public static final String spnGetSnapshotV9TbSpQuiescer = "GetSnapshotTbSpQuiescer";
public static final String spnGetSnapshotV9TbSpRange = "GetSnapshotTbSpRange";
public static final String spnGetSnapshotV9Util = "GetSnapshotUtil";
public static final String spnGetSnapshotV9UtilProgress = "GetSnapshotUtilProgress";
public static final String spnGetTabStatus = "GetTabStatus";
public static final String spnGetstats = "Getstats";
public static final String spnGrant = "SetGrants";
public static final String spnHelp = "Help";
public static final String spnHelpCatchSqlCode = "HelpCatchSqlCode";
public static final String spnIndexDisable = "IndexDisable";
public static final String spnIndexEnable = "IndexEnable";
public static final String spnIntegrity = "SetIntegrity";
public static final String spnLrtBegin = "LrtBegin";
public static final String spnLrtCommit = "LrtCommit";
public static final String spnLrtCommitList = "LrtCommitList";
public static final String spnLrtCommitPreProc = "LrtCommitPreProc";
public static final String spnLrtGenChangelog = "LrtGenChangelog";
public static final String spnLrtGetLog = "LrtGetLog";
public static final String spnLrtGetLogCard = "LrtGetLogCard";
public static final String spnLrtMqtSync = "MQTSync";
public static final String spnLrtRollback = "LrtRollback";
public static final String spnLrtRollbackList = "LrtRollbackList";
public static final String spnReCreateSnapshotTables = "SnapshotRecreateTables";
public static final String spnRebind = "Rebind";
public static final String spnReorg = "Reorg";
public static final String spnResetOidSeq = "ResetOidSequence";
public static final String spnRevalidate = "Revalidate";
public static final String spnRevoke = "RevokeGrants";
public static final String spnRowCompEstWrapper = "RowCompEstWrapper";
public static final String spnRtDisable = "RoutineDisable";
public static final String spnRtEnable = "RoutineEnable";
public static final String spnRunstats = "Runstats";
public static final String spnSetCfg = "SetCfgProfile";
public static final String spnSetDbCfg = "SetDbCfg";
public static final String spnSetTabAppend = "SetTabAppend";
public static final String spnSetTableCfg = "SetTableCfg";
public static final String spnSnapshotClear = "SnapshotClear";
public static final String spnSnapshotPrune = "SnapshotPrune";
public static final String spnTriggerDisable = "TriggerDisable";
public static final String spnTriggerEnable = "TriggerEnable";
public static final String spnVerifyCfg = "VerifyCfg";


// ################################################
//            User Defined Functions
// ################################################

// udfn   - function name
// udfsn  - function short name

public static final String udfnAcmLobAttrs = "AcmLobAttrs";
public static final String udfnApplStatus2Str = "ApplStatus2Str";
public static final String udfnBoolean2Str = "Boolean2Str";
public static final String udfnContType2Str = "ContType2Str";
public static final String udfnDb2Release = "Db2Release";
public static final String udfnDbStatus2Str = "DbStatus2Str";
public static final String udfnDbTabColList = "GetTabColList";
public static final String udfnDbmStatus2Str = "DbmStatus2Str";
public static final String udfnGetLrtTargetStatus = "GetLrtTgs";
public static final String udfnGetSchema = "GetSchema";
public static final String udfnGetStrElem = "GetStrElem";
public static final String udfnGetSubClassIds = "GetSubClassIds";
public static final String udfnGetSubClassIdsByList = "GetSubClassIdsByList";
public static final String udfnIsNumeric = "IsNumeric";
public static final String udfnIsSubset = "isSubset";
public static final String udfnLastStrElem = "LastStrElem";
public static final String udfnLockMode2Str = "LockMode2Str";
public static final String udfnLockObjType2Str = "LockObjType2Str";
public static final String udfnLockObjType2StrS = "LockOT2Str_S";
public static final String udfnLockStatus2Str = "LockStatus2Str";
public static final String udfnLrtGetOid = "LrtGetOid";
public static final String udfnOccurs = "Occurs";
public static final String udfnOccursShort = "Occurs_S";
public static final String udfnParseClassIdOidList = "ParseClassOidList";
public static final String udfnParseDataPools = "ParseDataPools";
public static final String udfnPdmSchemaName = "PdmSchemaName";
public static final String udfnPlatform2Str = "Platform2Str";
public static final String udfnPosStr = "PosStr";
public static final String udfnProtocol2Str = "Protocol2Str";
public static final String udfnSnapshotCols = "SCL";
public static final String udfnStmntOp2Str = "StmntOp2Str";
public static final String udfnStmntType2Str = "StmntType2Str";
public static final String udfnStrElemIndexes = "StrElemIndexes";
public static final String udfnStrElems = "StrElems";
public static final String udfnStrListMap = "StrListMap";
public static final String udfnStrTrim = "Trim";
public static final String udfnTabType2Str = "TabType2Str";
public static final String udfnTsContType2Str = "TsContType2Str";
public static final String udfnTsState2Str = "TsState2Str";
public static final String udfnTsType2Str = "TsType2Str";



}