package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_Globals {




public static final String gc_sheetNameConfig = "Config";

public static final String gc_workBookSuffixes = ".xls,.xlsm";
public static final String gc_fileNameSuffixDdl = "ddl";
public static final String gc_fileNameSuffixDml = "dml";
public static final String gc_fileNameSuffixCsv = "csv";

public static final String gc_tempTabNameChangeLog = "SESSION.ChangeLog";
public static final String gc_tempTabNameChangeLogNl = "SESSION.ChangeLog_NL_TEXT";

public static final String gc_enumAttrNameSuffix = "_ID";

public static final String gc_acmEntityTypeKeyEnum = "E";
public static final String gc_acmEntityTypeKeyClass = "C";
public static final String gc_acmEntityTypeKeyRel = "R";
public static final String gc_acmEntityTypeKeyView = "V";
public static final String gc_acmEntityTypeKeyType = "T";

public static final String gc_newRecordName = "NEWRECORD";
public static final String gc_oldRecordName = "OLDRECORD";

public static final int gc_maxProcessingStep = 6;

public static final int gc_maxDb2PartitionNameSuffixLen = 20;

// ### IFNOT IVK ###
//Global Const gc_dirPrefixOrg = "ORG-"
// ### ENDIF IVK ###

public static boolean g_genLrtSupport;
public static String g_sheetNameDdlSummary;

public static String g_targetDir;
public static int[] g_fileNameIncrements;
public static int g_logLevelsMsgBox;
public static int g_logLevelsReport;

public static int g_phaseIndexRegularTables;
public static int g_phaseIndexCoreSupport;
public static int g_phaseIndexModuleMeta;
public static int g_phaseIndexFksRelTabs;
public static int g_phaseIndexLrt;
public static int g_phaseIndexLrtViews;
public static int g_phaseIndexChangeLogViews;
public static int g_phaseIndexLrtSupport;
public static int g_phaseIndexDbSupport;
public static int g_phaseIndexAliases;

public static int g_phaseIndexLrtMqt;
public static int g_phaseIndexLogChange;
public static int g_phaseIndexDbSupport2;

public static int g_sectionIndexAlias;
public static int g_sectionindexAliasDelObj;
public static int g_sectionIndexAliasLrt;
public static int g_sectionindexAliasPrivateOnly;
public static int g_sectionIndexAliasPsDpFiltered;
public static int g_sectionIndexAliasPsDpFilteredExtended;
public static int g_sectionIndexDb;
public static int g_sectionIndexDbAdmin;
public static int g_sectionIndexSpLog;
public static int g_sectionIndexLrt;
public static int g_sectionIndexDbMeta;
public static int g_sectionIndexDbMonitor;
public static int g_sectionIndexFactoryTakeover;
public static int g_sectionIndexMeta;
public static int g_sectionIndexDataCheck;
public static int g_sectionIndexCountry;
public static int g_sectionIndexChangeLog;
public static int g_sectionIndexDataFix;
public static int g_sectionIndexAspect;
public static int g_sectionIndexPaiLog;
public static int g_sectionIndexTrace;
public static int g_sectionIndexProductStructure;
public static int g_sectionIndexSetProductive;
public static int g_sectionIndexCode;
public static int g_sectionIndexHelp;
public static int g_sectionIndexFwkTest;
public static int g_sectionIndexStaging;
public static int g_sectionIndexCommon;

public static int g_domainIndexEntityType;
public static int g_domainIndexCid;
public static int g_domainIndexOid;
public static int g_domainIndexInteger;
public static int g_domainIndexBoolean;
public static int g_domainIndexDbRelease;
public static int g_domainIndexEnumId;
public static int g_domainIndexInUseBy;
public static int g_domainIndexIsLrtPrivate;
public static int g_domainIndexLrtId;
public static int g_domainIndexLrtStatus;
public static int g_domainIndexModTimestamp;
public static int g_domainIndexUserId;
public static int g_domainIndexUserIdAlt;
public static int g_domainIndexLockRequestorId;
public static int g_domainIndexR2pLockContext;
public static int g_domainChangeLogString;

public static int g_domainIndexValTimestamp;
public static int g_domainIndexVersion;
public static int g_domainIndexDbSchemaName;
public static int g_domainIndexDbTableName;
public static int g_domainIndexDbColumnName;
public static int g_domainIndexDbViewName;
public static int g_domainIndexDbProcName;
public static int g_domainIndexDbFuncName;
public static int g_domainIndexChangeLogString;

public static int g_enumIndexDataPoolAccessMode;

public static int g_classIndexAcmAttribute;
public static int g_classIndexAcmDomain;
public static int g_classIndexAcmEntity;
public static int g_classIndexAcmSection;
public static int g_classIndexChangeLog;
public static int g_classIndexDataPool;
public static int g_classIndexWriteLock;
public static int g_classIndexReleaseLock;
public static int g_classIndexLdmSchema;
public static int g_classIndexLdmTable;
public static int g_classIndexLrt;
public static int g_classIndexLrtAffectedEntity;
public static int g_classIndexLrtExecStatus;
public static int g_classIndexOrganization;
public static int g_classIndexPdmPrimarySchema;
public static int g_classIndexPdmSchema;
public static int g_classIndexPdmTable;
public static int g_classIndexSqlLog;
public static int g_classIndexSqlLogCfg;
public static int g_classIndexUser;
public static int g_classIndexDbCfgProfile;
public static int g_classIndexDbPrivileges;
public static int g_classIndexDisabledFks;
public static int g_classIndexDisabledIndexes;
public static int g_classIndexDisabledRtDep;
public static int g_classIndexDisabledRts;
public static int g_classIndexDisabledTriggers;
public static int g_classIndexErrorMessage;
public static int g_classIndexFkDependency;
public static int g_classIndexIndexMetrics;
public static int g_classIndexTableCfg;
public static int g_classIndexSnapshotCol;
public static int g_classIndexSnapshotFilter;
public static int g_classIndexSnapshotHandle;
public static int g_classIndexSnapshotType;
public static int g_classIndexSnapshotAppl;
public static int g_classIndexSnapshotApplInfo;
public static int g_classIndexSnapshotLock;
public static int g_classIndexSnapshotLockWait;
public static int g_classIndexSnapshotStatement;

public static int g_workDataPoolIndex;
public static int g_workDataPoolId;

public static int g_primaryOrgIndex;
public static int g_primaryOrgId;

public static String g_activeLrtOidDdl;

public static String g_allSchemaNamePattern;
public static String g_schemaNameCtoMeta;
public static String g_schemaNameCtoDbMonitor;
public static String g_schemaNameCtoDbAdmin;


public static String g_qualTabNameDataPoolAccessMode;
public static String g_qualTabNamePdmDataPoolType;
public static String g_qualTabNamePdmOrganization;
public static String g_qualTabNamePdmOrganizationNl;
public static String g_qualTabNameStatus;
public static String g_qualTabNameLanguage;

public static String g_qualTabNameAcmAttribute;
public static String g_qualTabNameAcmAttributeNl;
public static String g_qualTabNameAcmDomain;
public static String g_qualTabNameAcmEntity;
public static String g_qualTabNameAcmEntityNl;
public static String g_qualTabNameAcmSection;
public static String g_qualTabNameDataPool;
public static String g_qualTabNameWriteLock;
public static String g_qualTabNameReleaseLock;
public static String g_qualTabNameLdmTable;
public static String g_qualTabNameOrganization;
public static String g_qualTabNamePdmPrimarySchema;
public static String g_qualTabNamePdmSchema;
public static String g_qualTabNamePdmTable;
public static String g_qualTabNameSqlLog;
public static String g_qualTabNameSqlLogCfg;
public static String g_qualTabNameTableCfg;
public static String g_qualTabNameUser;
public static String g_qualTabNameDbCfgProfile;
public static String g_qualTabNameDbPrivileges;
public static String g_qualTabNameDisabledFks;
public static String g_qualTabNameDisabledIndexes;
public static String g_qualTabNameDisabledRtDep;
public static String g_qualTabNameDisabledRts;
public static String g_qualTabNameDisabledTriggers;
public static String g_qualTabNameErrorMessage;
public static String g_qualTabNameFkDependency;
public static String g_qualTabNameIndexMetrics;

public static String g_qualTabNameSnapshotCol;
public static String g_qualTabNameSnapshotFilter;
public static String g_qualTabNameSnapshotHandle;
public static String g_qualTabNameSnapshotType;
public static String g_qualTabNameSnapshotAppl;
public static String g_qualTabNameSnapshotApplInfo;
public static String g_qualTabNameSnapshotLock;
public static String g_qualTabNameSnapshotLockWait;
public static String g_qualTabNameSnapshotStatement;

public static String g_anOid;
public static String g_surrogateKeyNameShort;

public static String g_dbtOid;
public static String g_dbtInteger;
public static String g_dbtEntityId;
public static String g_dbtEntityType;
public static String g_dbtSequence;
public static String g_dbtBoolean;
public static String g_dbtEnumId;
public static String g_dbtDbRelease;
public static String g_dbtDbSchemaName;
public static String g_dbtDbTableName;
public static String g_dbtDbColumnName;
public static String g_dbtDbViewName;
public static String g_dbtDbProcName;
public static String g_dbtDbFuncName;
public static String g_dbtUserId;
public static String g_dbtLrtId;
public static String g_dbtChangeLogString;

public static String g_anAhOid;
public static String g_anAhCid;
public static String g_anCid;
public static String g_anCreateUser;
public static String g_anCreateTimestamp;
public static String g_anEndTime;
public static String g_anInLrt;
public static String g_anUpdateUser;
public static String g_anLastUpdateTimestamp;
public static String g_anLrtOid;
public static String g_anLrtOpId;
public static String g_anIsLrtPrivate;
public static String g_anLrtState;
public static String g_anStatus;
public static String g_anVersionId;
public static String g_anUserId;
public static String g_anUserName;
public static String g_anLastOpTime;
public static String g_anIgnoreForChangelog;

public static String g_anEnumLabelText;

public static String g_anAcmEntitySection;
public static String g_anAcmEntityName;
public static String g_anAcmEntityType;
public static String g_anAcmEntityId;
public static String g_anAcmOrParEntitySection;
public static String g_anAcmOrParEntityName;
public static String g_anAcmOrParEntityType;
public static String g_anAcmOrParEntityId;
public static String g_anAcmSupEntitySection;
public static String g_anAcmSupEntityName;
public static String g_anAcmSupEntityType;
public static String g_anAcmLeftEntitySection;
public static String g_anAcmLeftEntityName;
public static String g_anAcmLeftEntityType;
public static String g_anAcmRightEntitySection;
public static String g_anAcmRightEntityName;
public static String g_anAcmRightEntityType;

public static String g_anAcmEntityLabel;

public static String g_anAcmAttributeName;
public static String g_anLdmDbColumnName;
public static String g_anAcmIsTv;
public static String g_anAcmIsVirtual;
public static String g_anLdmSequenceNo;
public static String g_anAcmAttributeLabel;

public static String g_anAcmDomainSection;
public static String g_anAcmDomainName;
public static String g_anAcmDbDataType;

public static String g_anLdmSchemaName;
public static String g_anLdmTableName;
public static String g_anPdmFkSchemaName;
public static String g_anPdmTableName;
public static String g_anPdmLdmFkSchemaName;
public static String g_anPdmTypedTableName;
public static String g_anPdmLdmFkTableName;
public static String g_anLdmFkSequenceNo;

public static String g_anPdmNativeSchemaName;
public static String g_anSpLogContextSchema;
public static String g_anSpLogContextName;
public static String g_anSpLogContextType;

public static String g_anAcmIsLrtMeta;
public static String g_anAcmIsLrt;
public static String g_anLdmIsLrt;
public static String g_anAcmIsGen;
public static String g_anLdmIsGen;
public static String g_anLdmIsNl;

public static String g_anAcmIsCto;
public static String g_anAcmIsCtp;
public static String g_anAcmIsRangePartAll;
public static String g_anAcmIsNt2m;

public static String g_anAcmEntityShortName;
public static String g_anAcmUseLrtMqt;
public static String g_anAcmUseLrtCommitPreprocess;
public static String g_anAcmIsLogChange;
public static String g_anAcmIsAbstract;
public static String g_anAcmIgnoreForChangelog;
public static String g_anAcmAliasShortName;
public static String g_anAcmIsEnforced;
public static String g_anAcmRlShortName;
public static String g_anAcmMinLeftCardinality;
public static String g_anAcmMaxLeftCardinality;
public static String g_anAcmLrShortName;
public static String g_anAcmMinRightCardinality;
public static String g_anAcmMaxRightCardinality;
public static String g_anEnumId;
public static String g_anEnumRefId;
public static String g_anLanguageId;
public static String g_anOrganizationId;
public static String g_anPoolTypeId;
public static String g_anAccessModeId;

public static String g_qualProcNameGetSnapshot;
public static String g_qualProcNameGetSnapshotAnalysisLockWait;
public static String g_qualProcNameGetSnapshotAnalysisAppl;
public static String g_qualProcNameGetSnapshotAnalysisStatement;
public static String g_qualProcNameGetSnapshotAnalysis;

public static String g_qualFuncNameDb2Release;
public static String g_qualFuncNameGetStrElem;
public static String g_qualFuncNameStrElems;
public static String g_qualFuncNameGetSubClassIdsByList;
public static String g_qualFuncNameStrListMap;


public static void initGlobals() {
M01_Globals.g_targetDir = M04_Utilities.dirName(M00_Excel.fileName);

// ### IFNOT IVK ###
// ReDim g_fileNameIncrements(1 To 12)
// g_phaseIndexRegularTables =  1 : g_fileNameIncrements(g_phaseIndexRegularTables) = phaseRegularTables
// g_phaseIndexCoreSupport =  2 : g_fileNameIncrements(g_phaseIndexCoreSupport) = phaseCoreSupport
// g_phaseIndexModuleMeta =  3 : g_fileNameIncrements(g_phaseIndexModuleMeta) = phaseModuleMeta:
// g_phaseIndexFksRelTabs =  4 : g_fileNameIncrements(g_phaseIndexFksRelTabs) = phaseFksRelTabs
// g_phaseIndexLrt =  5 : g_fileNameIncrements(g_phaseIndexLrt) = phaseLrt
// g_phaseIndexLrtViews =  6 : g_fileNameIncrements(g_phaseIndexLrtViews) = phaseLrtViews
// g_phaseIndexChangeLogViews =  7 : g_fileNameIncrements(g_phaseIndexChangeLogViews) = phaseChangeLogViews
// g_phaseIndexLrtSupport =  8 : g_fileNameIncrements(g_phaseIndexLrtSupport) = phaseLrtSupport
// g_phaseIndexDbSupport =  9 : g_fileNameIncrements(g_phaseIndexDbSupport) = phaseDbSupport
// g_phaseIndexAliases = 10 : g_fileNameIncrements(g_phaseIndexAliases) = phaseAliases
// g_phaseIndexLogChange = 11 : g_fileNameIncrements(g_phaseIndexLogChange) = phaseLogChange
// g_phaseIndexDbSupport2 = 12 : g_fileNameIncrements(g_phaseIndexDbSupport2) = phaseDbSupport2
// ### ENDIF IVK ###

M01_Globals.g_phaseIndexLrtMqt = M01_Globals.g_phaseIndexLrt;

M01_Globals.g_sectionIndexAlias = M20_Section.getSectionIndexByName(M01_ACM.snAlias, null);
M01_Globals.g_sectionindexAliasDelObj = M20_Section.getSectionIndexByName(M01_ACM_IVK.snAliasDelObj, null);
M01_Globals.g_sectionIndexAliasLrt = M20_Section.getSectionIndexByName(M01_ACM.snAliasLrt, null);
M01_Globals.g_sectionIndexAliasPsDpFiltered = M20_Section.getSectionIndexByName(M01_ACM_IVK.snAliasPsDpFiltered, null);
M01_Globals.g_sectionIndexAliasPsDpFilteredExtended = M20_Section.getSectionIndexByName(M01_ACM_IVK.snAliasPsDpFilteredExtended, null);
M01_Globals.g_sectionindexAliasPrivateOnly = M20_Section.getSectionIndexByName(M01_ACM.snAliasPrivateOnly, null);
M01_Globals.g_sectionIndexDbAdmin = M20_Section.getSectionIndexByName(M01_ACM.snDbAdmin, null);
M01_Globals.g_sectionIndexSpLog = M20_Section.getSectionIndexByName(M01_ACM.snSpLog, null);
M01_Globals.g_sectionIndexLrt = M20_Section.getSectionIndexByName(M01_ACM.snLrt, null);
M01_Globals.g_sectionIndexDb = M20_Section.getSectionIndexByName(M01_ACM.snDb, null);
M01_Globals.g_sectionIndexDbMeta = M20_Section.getSectionIndexByName(M01_ACM.snDbMeta, null);
M01_Globals.g_sectionIndexDbMonitor = M20_Section.getSectionIndexByName(M01_ACM.snDbMonitor, null);
M01_Globals.g_sectionIndexFactoryTakeover = M20_Section.getSectionIndexByName(M01_ACM_IVK.snFactoryTakeover, null);
M01_Globals.g_sectionIndexMeta = M20_Section.getSectionIndexByName(M01_ACM.snMeta, null);
M01_Globals.g_sectionIndexDataCheck = M20_Section.getSectionIndexByName(M01_ACM_IVK.snDataCheck, null);
M01_Globals.g_sectionIndexCountry = M20_Section.getSectionIndexByName(M01_ACM.snCountry, null);
M01_Globals.g_sectionIndexChangeLog = M20_Section.getSectionIndexByName(M01_ACM.snChangeLog, null);
M01_Globals.g_sectionIndexDataFix = M20_Section.getSectionIndexByName(M01_ACM_IVK.snDataFix, null);
M01_Globals.g_sectionIndexAspect = M20_Section.getSectionIndexByName(M01_ACM_IVK.snAspect, null);
M01_Globals.g_sectionIndexPaiLog = M20_Section.getSectionIndexByName(M01_ACM_IVK.snPaiLog, null);
M01_Globals.g_sectionIndexTrace = M20_Section.getSectionIndexByName(M01_ACM.snTrace, null);
M01_Globals.g_sectionIndexProductStructure = M20_Section.getSectionIndexByName(M01_ACM_IVK.snProductStructure, null);
M01_Globals.g_sectionIndexSetProductive = M20_Section.getSectionIndexByName(M01_ACM_IVK.snSetProductive, null);
M01_Globals.g_sectionIndexCode = M20_Section.getSectionIndexByName(M01_ACM_IVK.snCode, null);
M01_Globals.g_sectionIndexHelp = M20_Section.getSectionIndexByName(M01_ACM.snHelp, null);
if (M03_Config.generateFwkTest) {
M01_Globals.g_sectionIndexFwkTest = M20_Section.getSectionIndexByName(M01_ACM_IVK.snFwkTest, null);
}
M01_Globals.g_sectionIndexStaging = M20_Section.getSectionIndexByName(M01_ACM_IVK.snStaging, null);
M01_Globals.g_sectionIndexCommon = M20_Section.getSectionIndexByName(M01_ACM.snCommon, null);

M01_Globals.g_domainIndexEntityType = M25_Domain.getDomainIndexByName(M01_ACM.dxnEntityType, M01_ACM.dnEntityType, null);
M01_Globals.g_domainIndexCid = M25_Domain.getDomainIndexByName(M01_ACM.dxnClassId, M01_ACM.dnClassId, null);
M01_Globals.g_domainIndexBoolean = M25_Domain.getDomainIndexByName(M01_ACM.dxnBoolean, M01_ACM.dnBoolean, null);
M01_Globals.g_domainIndexDbRelease = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbRelease, M01_ACM.dnDbRelease, null);
M01_Globals.g_domainIndexEnumId = M25_Domain.getDomainIndexByName(M01_ACM.dxnEnumId, M01_ACM.dnEnumId, null);
M01_Globals.g_domainIndexInUseBy = M25_Domain.getDomainIndexByName(M01_ACM.dxnInUseBy, M01_ACM.dnInUseBy, null);
M01_Globals.g_domainIndexIsLrtPrivate = M25_Domain.getDomainIndexByName(M01_ACM.dxnBoolean, M01_ACM.dnBoolean, null);
M01_Globals.g_domainIndexLrtId = M25_Domain.getDomainIndexByName(M01_ACM.dsnLrt, M01_ACM.dnLrt, null);
M01_Globals.g_domainIndexLrtStatus = M25_Domain.getDomainIndexByName(M01_ACM.dxnLrtStatus, M01_ACM.dnLrtStatus, null);
M01_Globals.g_domainIndexModTimestamp = M25_Domain.getDomainIndexByName(M01_ACM.dxnModTimestamp, M01_ACM.dnModTimestamp, null);
M01_Globals.g_domainIndexOid = M25_Domain.getDomainIndexByName(M01_ACM.dxnOid, M01_ACM.dnOid, null);
M01_Globals.g_domainIndexInteger = M25_Domain.getDomainIndexByName(M01_ACM.dxnInteger, M01_ACM.dnInteger, null);
M01_Globals.g_domainIndexUserId = M25_Domain.getDomainIndexByName(M01_ACM.dxnUserId, M01_ACM.dnUserId, null);
M01_Globals.g_domainIndexUserIdAlt = M25_Domain.getDomainIndexByName(M01_ACM.dxnUserId, M01_ACM.dnUserIdAlt, null);
M01_Globals.g_domainIndexLockRequestorId = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnLockRequestorId, M01_ACM_IVK.dnLockRequestorId, null);
M01_Globals.g_domainIndexR2pLockContext = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnR2pLockContext, M01_ACM_IVK.dnR2pLockContext, null);
M01_Globals.g_domainIndexValTimestamp = M25_Domain.getDomainIndexByName(M01_ACM_IVK.dxnValTimestamp, M01_ACM_IVK.dnValTimestamp, null);
M01_Globals.g_domainIndexVersion = M25_Domain.getDomainIndexByName(M01_ACM.dxnVersion, M01_ACM.dnVersion, null);
M01_Globals.g_domainIndexDbSchemaName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbSchemaName, M01_ACM.dnDbSchemaName, null);
M01_Globals.g_domainIndexDbTableName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbTableName, M01_ACM.dnDbTableName, null);
M01_Globals.g_domainIndexDbColumnName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbColumnName, M01_ACM.dnDbColumnName, null);
M01_Globals.g_domainIndexDbViewName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbViewName, M01_ACM.dnDbViewName, null);
M01_Globals.g_domainIndexDbProcName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbProcName, M01_ACM.dnDbProcName, null);
M01_Globals.g_domainIndexDbFuncName = M25_Domain.getDomainIndexByName(M01_ACM.dxnDbFuncName, M01_ACM.dnDbFuncName, null);
M01_Globals.g_domainIndexChangeLogString = M25_Domain.getDomainIndexByName(M01_ACM.dxnChangeLogString, M01_ACM.dnChangeLogString, null);

M01_Globals.g_enumIndexDataPoolAccessMode = M21_Enum.getEnumIndexByName(M01_ACM.snMeta, M01_ACM.enDataPoolAccessMode, null);

M01_Globals.g_classIndexAcmAttribute = M22_Class.getClassIndexByName(M01_ACM.clxnAcmAttribute, M01_ACM.clnAcmAttribute, null);
M01_Globals.g_classIndexAcmDomain = M22_Class.getClassIndexByName(M01_ACM.clxnAcmDomain, M01_ACM.clnAcmDomain, null);
M01_Globals.g_classIndexAcmEntity = M22_Class.getClassIndexByName(M01_ACM.clxnAcmEntity, M01_ACM.clnAcmEntity, null);
M01_Globals.g_classIndexAcmSection = M22_Class.getClassIndexByName(M01_ACM.clxnAcmSection, M01_ACM.clnAcmSection, null);
M01_Globals.g_classIndexChangeLog = M22_Class.getClassIndexByName(M01_ACM.clxnChangeLog, M01_ACM.clnChangeLog, null);
M01_Globals.g_classIndexDataPool = M22_Class.getClassIndexByName(M01_ACM.clxnDataPool, M01_ACM.clnDataPool, null);
M01_Globals.g_classIndexWriteLock = M22_Class.getClassIndexByName(M01_ACM.clxnWriteLock, M01_ACM.clnWriteLock, null);
M01_Globals.g_classIndexReleaseLock = M22_Class.getClassIndexByName(M01_ACM.clxnReleaseLock, M01_ACM.clnReleaseLock, null);
M01_Globals.g_classIndexLdmSchema = M22_Class.getClassIndexByName(M01_ACM.clxnLdmSchema, M01_ACM.clnLdmSchema, null);
M01_Globals.g_classIndexLdmTable = M22_Class.getClassIndexByName(M01_ACM.clxnLdmTable, M01_ACM.clnLdmTable, null);
M01_Globals.g_classIndexLrt = M22_Class.getClassIndexByName(M01_ACM.clxnLrt, M01_ACM.clnLrt, null);
M01_Globals.g_classIndexLrtAffectedEntity = M22_Class.getClassIndexByName(M01_ACM.clxnLrtAffectedEntity, M01_ACM.clnLrtAffectedEntity, null);
M01_Globals.g_classIndexLrtExecStatus = M22_Class.getClassIndexByName(M01_ACM.clxnLrtExecStatus, M01_ACM.clnLrtExecStatus, null);
M01_Globals.g_classIndexOrganization = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnOrganization, M01_ACM_IVK.clnOrganization, null);
M01_Globals.g_classIndexPdmPrimarySchema = M22_Class.getClassIndexByName(M01_ACM.clxnPdmPrimarySchema, M01_ACM.clnPdmPrimarySchema, null);
M01_Globals.g_classIndexPdmSchema = M22_Class.getClassIndexByName(M01_ACM.clxnPdmSchema, M01_ACM.clnPdmSchema, null);
M01_Globals.g_classIndexPdmTable = M22_Class.getClassIndexByName(M01_ACM.clxnPdmTable, M01_ACM.clnPdmTable, null);
if (M03_Config.supportSpLogging) {
M01_Globals.g_classIndexSqlLog = M22_Class.getClassIndexByName(M01_ACM.clxnSqlLog, M01_ACM.clnSqlLog, null);
M01_Globals.g_classIndexSqlLogCfg = M22_Class.getClassIndexByName(M01_ACM.clxnSqlLogCfg, M01_ACM.clnSqlLogCfg, null);
}
M01_Globals.g_classIndexUser = M22_Class.getClassIndexByName(M01_ACM_IVK.clxnUser, M01_ACM_IVK.clnUser, null);
M01_Globals.g_classIndexDbCfgProfile = M22_Class.getClassIndexByName(M01_ACM.clxnDbCfgProfile, M01_ACM.clnDbCfgProfile, null);
M01_Globals.g_classIndexDbPrivileges = M22_Class.getClassIndexByName(M01_ACM.clxnDbPrivileges, M01_ACM.clnDbPrivileges, null);
M01_Globals.g_classIndexDisabledFks = M22_Class.getClassIndexByName(M01_ACM.clxnDisabledFks, M01_ACM.clnDisabledFks, null);
M01_Globals.g_classIndexDisabledIndexes = M22_Class.getClassIndexByName(M01_ACM.clxnDisabledIndexes, M01_ACM.clnDisabledIndexes, null);
M01_Globals.g_classIndexDisabledRtDep = M22_Class.getClassIndexByName(M01_ACM.clxnDisabledRtDep, M01_ACM.clnDisabledRtDep, null);
M01_Globals.g_classIndexDisabledRts = M22_Class.getClassIndexByName(M01_ACM.clxnDisabledRts, M01_ACM.clnDisabledRts, null);
M01_Globals.g_classIndexDisabledTriggers = M22_Class.getClassIndexByName(M01_ACM.clxnDisabledTriggers, M01_ACM.clnDisabledTriggers, null);
M01_Globals.g_classIndexErrorMessage = M22_Class.getClassIndexByName(M01_ACM.clxnErrorMessage, M01_ACM.clnErrorMessage, null);
M01_Globals.g_classIndexFkDependency = M22_Class.getClassIndexByName(M01_ACM.clxnFkDependency, M01_ACM.clnFkDependency, null);
if (M03_Config.supportIndexMetrics) {
M01_Globals.g_classIndexIndexMetrics = M22_Class.getClassIndexByName(M01_ACM.clxnIndexMetrics, M01_ACM.clnIndexMetrics, null);
}
M01_Globals.g_classIndexTableCfg = M22_Class.getClassIndexByName(M01_ACM.clxnTableCfg, M01_ACM.clnTableCfg, null);

M01_Globals.g_classIndexSnapshotCol = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotCol, M01_ACM.clnSnapshotCol, null);
M01_Globals.g_classIndexSnapshotFilter = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotFilter, M01_ACM.clnSnapshotFilter, null);
M01_Globals.g_classIndexSnapshotHandle = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotHandle, M01_ACM.clnSnapshotHandle, null);
M01_Globals.g_classIndexSnapshotType = M22_Class.getClassIndexByName(M01_ACM.clxnSnapshotType, M01_ACM.clnSnapshotType, null);
// ### IFNOT IVK ###
// g_classIndexSnapshotAppl = getClassIndexByName(clxnSnapshotV9Appl, clnSnapshotV9Appl)
// g_classIndexSnapshotApplInfo = getClassIndexByName(clxnSnapshotV9ApplInfo, clnSnapshotV9ApplInfo)
// g_classIndexSnapshotLock = getClassIndexByName(clxnSnapshotV9Lock, clnSnapshotV9Lock)
// g_classIndexSnapshotLockWait = getClassIndexByName(clxnSnapshotV9LockWait, clnSnapshotV9LockWait)
// g_classIndexSnapshotStatement = getClassIndexByName(clxnSnapshotV9Statement, clnSnapshotV9Statement)
// ### ENDIF IVK ###

M01_Globals.g_dbtOid = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexOid, null);
M01_Globals.g_dbtInteger = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexInteger, null);
M01_Globals.g_dbtSequence = M01_Globals.g_dbtOid;
M01_Globals.g_dbtEntityId = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexCid, null);
M01_Globals.g_dbtEntityType = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexEntityType, null);
M01_Globals.g_dbtBoolean = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexBoolean, null);
M01_Globals.g_dbtEnumId = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexEnumId, null);
M01_Globals.g_dbtDbRelease = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbRelease, null);
M01_Globals.g_dbtDbSchemaName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbSchemaName, null);
M01_Globals.g_dbtDbTableName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbTableName, null);
M01_Globals.g_dbtDbColumnName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbColumnName, null);
M01_Globals.g_dbtDbViewName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbViewName, null);
M01_Globals.g_dbtDbProcName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbProcName, null);
M01_Globals.g_dbtDbFuncName = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexDbFuncName, null);
M01_Globals.g_dbtUserId = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexUserId, null);
M01_Globals.g_dbtLrtId = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexLrtId, null);
M01_Globals.g_dbtChangeLogString = M02_ToolMeta.getDataTypeByDomainIndex(M01_Globals.g_domainIndexChangeLogString, null);

M01_Globals.g_activeLrtOidDdl = M01_Globals.g_dbtOid + "(" + M01_LDM.gc_db2RegVarLrtOidSafeSyntax + ")";

M01_Globals.g_workDataPoolIndex = M72_DataPool_Utilities.getWorkDataPoolIndex();
M01_Globals.g_workDataPoolId = M72_DataPool_Utilities.getWorkDataPoolId();
M01_Globals.g_primaryOrgIndex = M71_Org_Utilities.getPrimaryOrgIndex();
M01_Globals.g_primaryOrgId = M71_Org_Utilities.getPrimaryOrgId();

M01_Globals_IVK.initGlobals_IVK();
}


public static void initGlobalsByDdl(Integer ddlType) {
M01_Globals.g_allSchemaNamePattern = M04_Utilities.genSchemaName("%", "%", ddlType, null, null);

M01_Globals.g_schemaNameCtoMeta = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, ddlType, null, null);
M01_Globals.g_schemaNameCtoDbMonitor = M04_Utilities.genSchemaName(M01_ACM.snDbMonitor, M01_ACM.ssnDbMonitor, ddlType, null, null);
M01_Globals.g_schemaNameCtoDbAdmin = M04_Utilities.genSchemaName(M01_ACM.snDbAdmin, M01_ACM.ssnDbAdmin, ddlType, null, null);

M01_Globals.g_qualTabNameDataPoolAccessMode = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals.g_enumIndexDataPoolAccessMode, ddlType, null, null, null, null, null);

M01_Globals.g_qualTabNameAcmAttribute = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmAttribute, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameAcmAttributeNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmAttribute, ddlType, null, null, null, null, null, true, null, null, null);
M01_Globals.g_qualTabNameAcmDomain = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmDomain, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameAcmEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmEntity, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameAcmEntityNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmEntity, ddlType, null, null, null, null, null, true, null, null, null);
M01_Globals.g_qualTabNameAcmSection = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexAcmSection, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDataPool = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDataPool, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameWriteLock = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexWriteLock, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameReleaseLock = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexReleaseLock, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameLdmTable = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLdmTable, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameOrganization = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexOrganization, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmPrimarySchema = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexPdmPrimarySchema, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmSchema = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexPdmSchema, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNamePdmTable = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexPdmTable, ddlType, null, null, null, null, null, null, null, null, null);
if (M03_Config.supportSpLogging) {
M01_Globals.g_qualTabNameSqlLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSqlLog, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSqlLogCfg = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSqlLogCfg, ddlType, null, null, null, null, null, null, null, null, null);
}
M01_Globals.g_qualTabNameTableCfg = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexTableCfg, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameUser = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexUser, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDbCfgProfile = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDbCfgProfile, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDbPrivileges = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDbPrivileges, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDisabledFks = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDisabledFks, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDisabledIndexes = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDisabledIndexes, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDisabledRtDep = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDisabledRtDep, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDisabledRts = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDisabledRts, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameDisabledTriggers = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexDisabledTriggers, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameErrorMessage = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexErrorMessage, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameFkDependency = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexFkDependency, ddlType, null, null, null, null, null, null, null, null, null);
if (M03_Config.supportIndexMetrics) {
M01_Globals.g_qualTabNameIndexMetrics = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexIndexMetrics, ddlType, null, null, null, null, null, null, null, null, null);
}

M01_Globals.g_qualTabNameSnapshotCol = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotCol, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotFilter = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotFilter, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotHandle = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotHandle, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotType = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotType, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotAppl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotAppl, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotApplInfo = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotApplInfo, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotLock = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotLock, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotLockWait = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotLockWait, ddlType, null, null, null, null, null, null, null, null, null);
M01_Globals.g_qualTabNameSnapshotStatement = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexSnapshotStatement, ddlType, null, null, null, null, null, null, null, null, null);

M01_Globals.g_anOid = M04_Utilities.genSurrogateKeyName(ddlType, null, null, null, null, null);
M01_Globals.g_surrogateKeyNameShort = M04_Utilities.genSurrogateKeyShortName(ddlType, null, null);

M01_Globals.g_anAhOid = M04_Utilities.genAttrName(M01_ACM.conAhOId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAhCid = M04_Utilities.genAttrName(M01_ACM.conAhClassId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anCid = M04_Utilities.genAttrName(M01_ACM.conClassId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anCreateTimestamp = M04_Utilities.genAttrName(M01_ACM.conCreateTimestamp, ddlType, null, null, null, null, null, null);
M01_Globals.g_anCreateUser = M04_Utilities.genAttrName(M01_ACM.conCreateUser, ddlType, null, null, null, null, null, null);
M01_Globals.g_anEndTime = M04_Utilities.genAttrName(M01_ACM.conEndTime, ddlType, null, null, null, null, null, null);
M01_Globals.g_anInLrt = M04_Utilities.genAttrName(M01_ACM.conInLrt, ddlType, null, null, null, null, null, null);
M01_Globals.g_anIsLrtPrivate = M04_Utilities.genAttrName(M01_ACM.conIsLrtPrivate, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLrtOid = M04_Utilities.genAttrName(M01_ACM.conLrtOid, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLrtOpId = M04_Utilities.genAttrName(M01_ACM.conLrtOpId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLastUpdateTimestamp = M04_Utilities.genAttrName(M01_ACM.conLastUpdateTimestamp, ddlType, null, null, null, null, null, null);
M01_Globals.g_anUpdateUser = M04_Utilities.genAttrName(M01_ACM.conUpdateUser, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLrtState = M04_Utilities.genAttrName(M01_ACM.conLrtState, ddlType, null, null, null, null, null, null);
M01_Globals.g_anStatus = M04_Utilities.genAttrName(M01_ACM_IVK.conStatusId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anVersionId = M04_Utilities.genAttrName(M01_ACM.conVersionId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmOrParEntityId = M04_Utilities.genAttrName(M01_ACM.conAcmOrParEntityId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anUserId = M04_Utilities.genAttrName(M01_ACM.conUserId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anUserName = M04_Utilities.genAttrName(M01_ACM.conUserName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLastOpTime = M04_Utilities.genAttrName(M01_ACM.conLastOpTime, ddlType, null, null, null, null, null, null);
M01_Globals.g_anIgnoreForChangelog = M04_Utilities.genAttrName(M01_ACM.conIgnoreForChangelog, ddlType, null, null, null, null, null, null);
M01_Globals.g_anEnumLabelText = M04_Utilities.genAttrName(M01_ACM.conEnumLabelText, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmEntitySection = M04_Utilities.genAttrName(M01_ACM.conAcmEntitySection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmEntityName = M04_Utilities.genAttrName(M01_ACM.conAcmEntityName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmEntityType = M04_Utilities.genAttrName(M01_ACM.conAcmEntityType, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmEntityId = M04_Utilities.genAttrName(M01_ACM.conAcmEntityId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmOrParEntitySection = M04_Utilities.genAttrName(M01_ACM.conAcmOrParEntitySection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmOrParEntityName = M04_Utilities.genAttrName(M01_ACM.conAcmOrParEntityName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmOrParEntityType = M04_Utilities.genAttrName(M01_ACM.conAcmOrParEntityType, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmOrParEntityId = M04_Utilities.genAttrName(M01_ACM.conAcmOrParEntityId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmSupEntitySection = M04_Utilities.genAttrName(M01_ACM.conAcmSupEntitySection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmSupEntityName = M04_Utilities.genAttrName(M01_ACM.conAcmSupEntityName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmSupEntityType = M04_Utilities.genAttrName(M01_ACM.conAcmSupEntityType, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmLeftEntitySection = M04_Utilities.genAttrName(M01_ACM.conAcmLeftEntitySection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmLeftEntityName = M04_Utilities.genAttrName(M01_ACM.conAcmLeftEntityName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmLeftEntityType = M04_Utilities.genAttrName(M01_ACM.conAcmLeftEntityType, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmRightEntitySection = M04_Utilities.genAttrName(M01_ACM.conAcmRightEntitySection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmRightEntityName = M04_Utilities.genAttrName(M01_ACM.conAcmRightEntityName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmRightEntityType = M04_Utilities.genAttrName(M01_ACM.conAcmRightEntityType, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmEntityLabel = M04_Utilities.genAttrName(M01_ACM.conAcmEntityLabel, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmAttributeName = M04_Utilities.genAttrName(M01_ACM.conAcmAttributeName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmDbColumnName = M04_Utilities.genAttrName(M01_ACM.conLdmDbColumnName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsTv = M04_Utilities.genAttrName(M01_ACM.conAcmIsTv, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsVirtual = M04_Utilities.genAttrName(M01_ACM.conAcmIsVirtual, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmSequenceNo = M04_Utilities.genAttrName(M01_ACM.conLdmSequenceNo, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmAttributeLabel = M04_Utilities.genAttrName(M01_ACM.conAcmAttributeLabel, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmDomainSection = M04_Utilities.genAttrName(M01_ACM.conAcmDomainSection, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmDomainName = M04_Utilities.genAttrName(M01_ACM.conAcmDomainName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmDbDataType = M04_Utilities.genAttrName(M01_ACM.conAcmDbDataType, ddlType, null, null, null, null, null, null);

M01_Globals.g_anLdmSchemaName = M04_Utilities.genAttrName(M01_ACM.conLdmSchemaName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmTableName = M04_Utilities.genAttrName(M01_ACM.conLdmTableName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anPdmFkSchemaName = M04_Utilities.genAttrName(M01_ACM.conPdmFkSchemaName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anPdmTableName = M04_Utilities.genAttrName(M01_ACM.conPdmTableName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anPdmLdmFkSchemaName = M04_Utilities.genAttrName(M01_ACM.conPdmLdmFkSchemaName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anPdmLdmFkTableName = M04_Utilities.genAttrName(M01_ACM.conPdmLdmFkTableName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmFkSequenceNo = M04_Utilities.genAttrName(M01_ACM.conLdmFkSequenceNo, ddlType, null, null, null, null, null, null);

M01_Globals.g_anPdmTypedTableName = M04_Utilities.genAttrName(M01_ACM.conPdmTypedTableName, ddlType, null, null, null, null, null, null);

M01_Globals.g_anPdmNativeSchemaName = M04_Utilities.genAttrName(M01_ACM.conPdmNativeSchemaName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anSpLogContextSchema = M04_Utilities.genAttrName(M01_ACM.conSpLogContextSchema, ddlType, null, null, null, null, null, null);
M01_Globals.g_anSpLogContextName = M04_Utilities.genAttrName(M01_ACM.conSpLogContextName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anSpLogContextType = M04_Utilities.genAttrName(M01_ACM.conSpLogContextType, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmIsLrtMeta = M04_Utilities.genAttrName(M01_ACM.conAcmIsLrtMeta, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsLrt = M04_Utilities.genAttrName(M01_ACM.conAcmIsLrt, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmIsLrt = M04_Utilities.genAttrName(M01_ACM.conLdmIsLrt, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsGen = M04_Utilities.genAttrName(M01_ACM.conAcmIsGen, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmIsGen = M04_Utilities.genAttrName(M01_ACM.conLdmIsGen, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLdmIsNl = M04_Utilities.genAttrName(M01_ACM.conLdmIsNl, ddlType, null, null, null, null, null, null);
M01_Globals_IVK.g_anLdmIsMqt = M04_Utilities.genAttrName(M01_ACM.conLdmIsMqt, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmIsCto = M04_Utilities.genAttrName(M01_ACM.conAcmIsCto, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsCtp = M04_Utilities.genAttrName(M01_ACM.conAcmIsCtp, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsRangePartAll = M04_Utilities.genAttrName(M01_ACM.conAcmIsRangePartAll, ddlType, null, null, null, null, null, null);

M01_Globals.g_anAcmEntityShortName = M04_Utilities.genAttrName(M01_ACM.conAcmEntityShortName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmUseLrtMqt = M04_Utilities.genAttrName(M01_ACM.conAcmUseLrtMqt, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmUseLrtCommitPreprocess = M04_Utilities.genAttrName(M01_ACM.conAcmUseLrtCommitPreprocess, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsLogChange = M04_Utilities.genAttrName(M01_ACM.conAcmIsLogChange, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsAbstract = M04_Utilities.genAttrName(M01_ACM.conAcmIsAbstract, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIgnoreForChangelog = M04_Utilities.genAttrName(M01_ACM.conAcmIgnoreForChangelog, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmAliasShortName = M04_Utilities.genAttrName(M01_ACM.conAcmAliasShortName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmIsEnforced = M04_Utilities.genAttrName(M01_ACM.conAcmIsEnforced, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmRlShortName = M04_Utilities.genAttrName(M01_ACM.conAcmRlShortName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmMinLeftCardinality = M04_Utilities.genAttrName(M01_ACM.conAcmMinLeftCardinality, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmMaxLeftCardinality = M04_Utilities.genAttrName(M01_ACM.conAcmMaxLeftCardinality, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmLrShortName = M04_Utilities.genAttrName(M01_ACM.conAcmLrShortName, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmMinRightCardinality = M04_Utilities.genAttrName(M01_ACM.conAcmMinRightCardinality, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAcmMaxRightCardinality = M04_Utilities.genAttrName(M01_ACM.conAcmMaxRightCardinality, ddlType, null, null, null, null, null, null);

M01_Globals.g_anEnumId = M04_Utilities.genAttrName(M01_ACM.conEnumId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anEnumRefId = M04_Utilities.genAttrName(M01_ACM.conEnumRefId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anLanguageId = M04_Utilities.genAttrName(M01_ACM.conLanguageId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anOrganizationId = M04_Utilities.genAttrName(M01_ACM.conOrganizationId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anPoolTypeId = M04_Utilities.genAttrName(M01_ACM.conPoolTypeId, ddlType, null, null, null, null, null, null);
M01_Globals.g_anAccessModeId = M04_Utilities.genAttrName(M01_ACM.conAccessModeId, ddlType, null, null, null, null, null, null);

M01_Globals.g_qualProcNameGetSnapshot = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshot, ddlType, null, null, null, null, null, null);
M01_Globals.g_qualProcNameGetSnapshotAnalysisLockWait = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshotAnalysisLockWait, ddlType, null, null, null, null, null, null);
M01_Globals.g_qualProcNameGetSnapshotAnalysisAppl = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshotAnalysisAppl, ddlType, null, null, null, null, null, null);
M01_Globals.g_qualProcNameGetSnapshotAnalysisStatement = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshotAnalysisStatement, ddlType, null, null, null, null, null, null);
M01_Globals.g_qualProcNameGetSnapshotAnalysis = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGetSnapshotAnalysis, ddlType, null, null, null, null, null, null);

M01_Globals.g_qualFuncNameDb2Release = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.udfnDb2Release, ddlType, null, null, null, null, null, true);
M01_Globals.g_qualFuncNameGetStrElem = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnGetStrElem, ddlType, null, null, null, null, null, true);
M01_Globals.g_qualFuncNameGetSubClassIdsByList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.udfnGetSubClassIdsByList, ddlType, null, null, null, null, null, true);
M01_Globals.g_qualFuncNameStrListMap = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnStrListMap, ddlType, null, null, null, null, null, null);
// ### IFNOT IVK ###
// g_qualFuncNameStrElems = genQualFuncName(g_sectionIndexMeta, udfnStrElems, ddlType)
// ### ENDIF IVK ###

M01_Globals_IVK.initGlobalsByDdl_IVK(ddlType);
}


public static void setLogLevesl(Integer logLevelsReportW, Integer logLevelsMsgBoxW) {
int logLevelsReport; 
if (logLevelsReportW == null) {
logLevelsReport = (M01_Common.LogLevel.ellFixableWarning |  M01_Common.LogLevel.ellWarning | M01_Common.LogLevel.ellError | M01_Common.LogLevel.ellFatal);
} else {
logLevelsReport = logLevelsReportW;
}

int logLevelsMsgBox; 
if (logLevelsMsgBoxW == null) {
logLevelsMsgBox = (M01_Common.LogLevel.ellFatal |  M01_Common.LogLevel.ellError);
} else {
logLevelsMsgBox = logLevelsMsgBoxW;
}

M01_Globals.g_logLevelsMsgBox = logLevelsMsgBox;
M01_Globals.g_logLevelsReport = logLevelsReport;
}


public static void setEnv(boolean forLrt) {
M01_Globals.g_sheetNameDdlSummary = "LDM" + (forLrt ? "-LRT" : "");
}



}