package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M86_SetProductive {


// ### IF IVK ###


private static final String pc_tempTabNameDataPool = "SESSION.DataPool";

private static final String pc_tempTabNameOrgOids = "SESSION.OrgOids";
private static final String pc_tempTabNamePsOids = "SESSION.PsOids";
private static final String pc_tempTabNameAccessModeIds = "SESSION.AccessModeIds";

//Fixme: Implement this as enumeration

public static final int statusWorkInProgress = 1;
public static final int statusReadyForActivation = 2;
public static final int statusReadyForRelease = 3;
public static final int statusReadyToBeSetProductive = 4;
public static final int statusProductive = 5;

private static final int processingStep = 2;

private static final String lockModeSharedWrite = "S";
private static final String lockModeSharedRead = "R";
private static final String lockModeExclusiveWrite = "E";

private static final String lockLogOpSet = "S";
private static final String lockLogOpReSet = "R";



public static void genSetProdSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genSetProdSupportForDb(null);
genSetProdSupportForDb2(null);
genSetProdSupportForDb3(null);
genRel2ProdLockWrapperDdlForDb(null);
genRel2ProdLockCompatibilityWrapperDdlForDb(null);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
genSetProdSupportDdlByPool(thisOrgIndex, thisPoolIndex, M71_Org.g_orgs.descriptors[thisOrgIndex].setProductiveTargetPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
genSetProdSupportDdlByPoolForAllPools(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}


public static void genDdlForTempTablesSp(int fileNo, Integer indentW, Boolean includeFilterTableW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean includeFilterTable; 
if (includeFilterTableW == null) {
includeFilterTable = false;
} else {
includeFilterTable = includeFilterTableW;
}

boolean withReplace; 
if (withReplaceW == null) {
withReplace = false;
} else {
withReplace = withReplaceW;
}

boolean onCommitPreserve; 
if (onCommitPreserveW == null) {
onCommitPreserve = false;
} else {
onCommitPreserve = onCommitPreserveW;
}

boolean onRollbackPreserve; 
if (onRollbackPreserveW == null) {
onRollbackPreserve = false;
} else {
onRollbackPreserve = onRollbackPreserveW;
}

if (includeFilterTable) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for 'Set Productive'-filtered (by LRT) records", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameSpFilteredEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "priceOid         " + M01_Globals.g_dbtOid + "   NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for 'Set Productive'-affected records", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orParEntityType  CHAR(1) NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orParEntityId    " + M01_Globals.g_dbtEntityId + " NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isNl             " + M01_Globals.g_dbtBoolean + " NOT NULL DEFAULT 0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isGen            " + M01_Globals.g_dbtBoolean + " NOT NULL DEFAULT 0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid              " + M01_Globals.g_dbtOid + " NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "opId             " + M01_Globals.g_dbtEnumId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


private static void genSetProdSupportForDb(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSetProductive, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualViewName;
String qualViewNameLdm;

// ####################################################################################################################
// #    create view to determine PDM tables involved in 'setting data productive'
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnSetProdAffectedPdmTab, M01_ACM.vsnSetProdAffectedPdmTab, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for all PDM-tables involved in 'setting data productive'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "" + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SOURCE_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anAcmCondenseData + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDMW." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDMW." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals_IVK.g_anAcmCondenseData + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDMW." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDMW." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmFkSequenceNo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsCto + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsCtp + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anAcmEntitySection + " = A." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anAcmEntityName + " = A." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmTable + " PDMW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmSchemaName + " = PDMW." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmTableName + " = PDMW." + M01_Globals.g_anPdmLdmFkTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnSetProdAffectedPdmTab, M01_ACM.vsnSetProdAffectedPdmTab, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnSetProdAffectedPdmTab, true, true, true, qualViewNameLdm, qualViewName, false, ddlType, null, null, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "\"Set Productive\"-related PDM-TABLES View", null, null, null, null, null, null, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genSetProdSupportForDb2(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSetProductive, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualFuncNameGenRel2ProdLockKey;
qualFuncNameGenRel2ProdLockKey = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnGenRel2ProdLockKey, ddlType, null, null, null, null, null, true);

String qualFuncNameParseDataPools;
qualFuncNameParseDataPools = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnParseDataPools, ddlType, null, null, null, null, null, true);

boolean targetHistoryTab;
M24_Attribute_Utilities.AttributeListTransformation transformation;



// ####################################################################################################################
// #    Release all 'Set Productive'-locks held by a given application (server)
// ####################################################################################################################

String qualProcNameReSetLocks;
qualProcNameReSetLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLocks, ddlType, null, null, null, null, null, null);

String qualProcNameResetLock;
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP to release all 'Set Productive'-locks held by a given application (-server)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameReSetLocks);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User initiating the lock release");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools unlocked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLWARNING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameReSetLocks, ddlType, null, "requestorId_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over locks related to given 'requestorId_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lockLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anLockContext);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(requestorId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(REQUESTORID = requestorId_in)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameResetLock + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE " + M01_Globals_IVK.g_anLockMode + " WHEN '" + lockModeSharedWrite + "' THEN '_SHAREDWRITE' WHEN '" + lockModeSharedRead + "' THEN '_SHAREDREAD' ELSE '_EXCLUSIVEWRITE' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'(''' || RTRIM(CHAR(RPOORG_OID)) || ',' || RTRIM(CHAR(" + M01_Globals_IVK.g_anPsOid + ")) || ',' || RTRIM(CHAR(" + M01_Globals.g_anAccessModeId + ")) || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'''' || requestorId_in || ''',''' || COALESCE(cdUserId_in, '<system>') || ''',''' || " + M01_Globals_IVK.g_anLockContext + " || ''', ?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_numDataPools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = numDataPools_out + v_numDataPools;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");


M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameReSetLocks, ddlType, null, "requestorId_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Release all orphan 'Set Productive'-locks
// ####################################################################################################################

String qualProcNameReSetLocksOrphan;
qualProcNameReSetLocksOrphan = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLocksOrphan, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP to release all orphan 'Set Productive'-locks", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameReSetLocksOrphan);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lockCtxtOuterPattern_in", "VARCHAR(100)", true, "(optional) (pattern of) 'outer' lockContexts considered as 'orphan' - default 'DBMaster%'");
M11_LRT.genProcParm(fileNo, "IN", "lockCtxtInnerPattern_in", "VARCHAR(100)", true, "(optional) (pattern of) 'outer' lockContexts considered as 'orphan' - default 'UC1022%'");
M11_LRT.genProcParm(fileNo, "IN", "minAgeMinutes_in", "INTEGER", true, "(optional) minimum age of lock to be considered 'orphan' (# minutes) - default 20");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "(optional) identifies the User initiating the lock release - default '<system>'");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools unlocked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_refTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstOuterLockTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lastInnerLockTs", "TIMESTAMP", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameReSetLocksOrphan, ddlType, null, "'lockCtxtOuterPattern_in", "'lockCtxtInnerPattern_in", "minAgeMinutes_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lockCtxtOuterPattern_in = COALESCE(lockCtxtOuterPattern_in, 'DBMaster%');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lockCtxtInnerPattern_in = COALESCE(lockCtxtInnerPattern_in, 'UC1022%');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET minAgeMinutes_in        = COALESCE(minAgeMinutes_in,        20);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET cdUserId_in             = COALESCE(cdUserId_in,             '<system>');");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine timestamp of oldest outer lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MIN(" + M01_Globals_IVK.g_anLockTimestamp + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_firstOuterLockTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtOuterPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O.REQUESTORID = 'anonymous'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if there is no outer lock there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_firstOuterLockTs IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine timestamp of youngest inner lock (from history)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MAX(" + M01_Globals_IVK.g_anLockTimestamp + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lastInnerLockTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory + " I");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtInnerPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if there is some inner lock during 'most recent history' we need to examine 'gap in lock-history'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lastInnerLockTs >= (CURRENT TIMESTAMP - minAgeMinutes_in MINUTE) THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine reference time stamp such that all 'older locks' are known to be 'orphan'", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(" + M01_Globals_IVK.g_anLockTimestamp + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_refTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory + " H");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "H." + M01_Globals_IVK.g_anLockTimestamp + " > v_firstOuterLockTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "H." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtInnerPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "H." + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "R." + M01_Globals_IVK.g_anLockTimestamp + " > (H." + M01_Globals_IVK.g_anLockTimestamp + " - minAgeMinutes_in MINUTE)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "R." + M01_Globals_IVK.g_anLockTimestamp + " < H." + M01_Globals_IVK.g_anLockTimestamp + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "R." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtInnerPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "R." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtOuterPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "R." + M01_Globals_IVK.g_anLockMode + " ='" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "no inner lock found 'in recent history' -> all outer locks with sufficient age are orphan", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_refTimestamp   = CURRENT TIMESTAMP - minAgeMinutes_in MINUTE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "if no reference time stamp was found there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_refTimestamp IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over orphan locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lockLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.RPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anLockMode + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtOuterPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.REQUESTORID = 'anonymous'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals_IVK.g_anLockTimestamp + " < v_refTimestamp");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameResetLock + "_OTHER' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'(''' || RTRIM(CHAR(RPOORG_OID)) || ',' || RTRIM(CHAR(" + M01_Globals_IVK.g_anPsOid + ")) || ',' || RTRIM(CHAR(" + M01_Globals.g_anAccessModeId + ")) || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'''' || REQUESTORID || ''',''' || COALESCE(cdUserId_in, '<system>') || ''',' || COALESCE('''' || " + M01_Globals_IVK.g_anLockContext + " || '''', 'NULL') || ',?)'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_numDataPools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = numDataPools_out + v_numDataPools;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "delete outdated outer locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtOuterPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O.REQUESTORID = 'anonymous'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anLockTimestamp + " < v_refTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete outdated inner locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "" + M01_Globals_IVK.g_anLockContext + " LIKE lockCtxtInnerPattern_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + " = '" + lockModeSharedRead + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "" + M01_Globals_IVK.g_anLockTimestamp + " < v_refTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameReSetLocksOrphan, ddlType, null, "'lockCtxtOuterPattern_in", "'lockCtxtInnerPattern_in", "minAgeMinutes_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP to release all orphan 'Set Productive'-locks", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameReSetLocksOrphan);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User initiating the lock release");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools unlocked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameReSetLocksOrphan, ddlType, null, "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameReSetLocksOrphan + "('DBMaster%', 'UC1022%', 20, cdUserId_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameReSetLocksOrphan, ddlType, null, "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function for generating KEY to use for locking a data pool for 'Set Productive'
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for generating KEY to use for locking a data pool for 'Set Productive'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameGenRel2ProdLockKey);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of the data pool's organization");
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the data pool's Product Structure");
M11_LRT.genProcParm(fileNo, "", "accessMode_in", M01_Globals.g_dbtEnumId, false, "access mode of the data pool");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "VARCHAR(50)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RTRIM(CAST(orgOid_in AS CHAR(20))) || ',' || RTRIM(CAST(psOid_in AS CHAR(20))) || ',' || RTRIM(CAST(accessMode_in AS CHAR(2)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function for parsing '|'-separated List of data pool descriptors <ORG_OID,PS_OID,ACCESSMODE_ID>
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for parsing '|'-separated List of data pool descriptors <ORG_OID," + M01_Globals_IVK.g_anPsOid + "," + M01_Globals.g_anAccessModeId + ">", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameParseDataPools);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "dataPoolDescr_in", "VARCHAR(4000)", false, "'|'-separated List of expressions <ORG_OID," + M01_Globals_IVK.g_anPsOid + "," + M01_Globals.g_anAccessModeId + ">");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId " + M01_Globals.g_dbtEnumId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_list");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(LTRIM(REPLACE(REPLACE(elem, '<', ''), '>', '')))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(dataPoolDescr_in, CAST('|' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_list1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "col1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowRest");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(row, POSSTR(row, ',')-1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RIGHT(row, LENGTH(row)-POSSTR(row, ','))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_list");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_list2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "col1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "col2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "col3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "col1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(rowRest, POSSTR(rowRest, ',')-1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RIGHT(rowRest, LENGTH(rowRest)-POSSTR(rowRest, ','))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_list1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(CASE col1 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col1 END AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(CASE col2 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col2 END AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(CASE col3 WHEN '' THEN CAST(NULL AS VARCHAR(1)) ELSE col3 END AS " + M01_Globals.g_dbtEnumId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_list2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP determining whether a LOCK is set on 'Set Productive'
// ####################################################################################################################

String qualProcNameLockIsSet;
String mode;
String modeShort;
String procNameSuffix;

int j;
for (int j = 1; j <= 3; j++) {
if ((j == 1)) {
mode = "SHAREDWRITE";
modeShort = lockModeSharedWrite;
} else if ((j == 2)) {
mode = "SHAREDREAD";
modeShort = lockModeSharedRead;
} else {
mode = "EXCLUSIVEWRITE";
modeShort = lockModeExclusiveWrite;
}

procNameSuffix = "_IN_" + mode + "_MODE";

qualProcNameLockIsSet = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnRel2ProdIsSet, ddlType, null, null, null, procNameSuffix, M04_Utilities.ObjNameDelimMode.eondmNone, null);

M22_Class_Utilities.printSectionHeader("SP determinig whether a LOCK is set on 'Set Productive' (" + mode + ") for a given data pool", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLockIsSet);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool to query the LOCK-status for");
M11_LRT.genProcParm(fileNo, "OUT", "isLocked_out", M01_Globals.g_dbtBoolean, false, "specifies whether a LOCK is set (0=false, 1=true)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLockIsSet, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "parse dataPoolDescr_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescr_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify syntax of input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLockIsSet, ddlType, 2, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "check if data pool is locked", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isLocked_out =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals_IVK.g_anLockMode + " = '" + modeShort + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLockIsSet, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

String qualProcNameSetLocks;

for (int j = 1; j <= 2; j++) {
mode = (j == 1 ? "SHAREDWRITE" : "SHAREDREAD");
modeShort = (j == 1 ? lockModeSharedWrite : lockModeSharedRead);
procNameSuffix = (j == 1 ? "_SHAREDWRITE" : "_SHAREDREAD");

// ####################################################################################################################
// #    SP to acquire LOCKs for 'Set Productive'
// ####################################################################################################################

qualProcNameSetLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, procNameSuffix, M04_Utilities.ObjNameDelimMode.eondmNone, null);

M22_Class_Utilities.printSectionHeader("SP to acquire LOCK for 'Set Productive'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameSetLocks);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "data pool to acquire LOCK for");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of datapools locked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "parse dataPoolDescr_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescr_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify syntax of input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN");
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;");

M11_LRT.genProcSectionHeader(fileNo, "Step 1: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anLockMode + " IN ('" + (j == 1 ? lockModeSharedRead : lockModeSharedWrite) + "', '" + lockModeExclusiveWrite + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");


M11_LRT.genProcSectionHeader(fileNo, "Step 2: insert new lock", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M11_LRT.genProcSectionHeader(fileNo, "Step 3: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anLockMode + " IN ('" + (j == 1 ? lockModeSharedRead : lockModeSharedWrite) + "', '" + lockModeExclusiveWrite + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK TO SAVEPOINT rel2ProdLockSp;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RELEASE SAVEPOINT rel2ProdLockSp;");
M00_FileWriter.printToFile(fileNo, "");
//determine old lock value
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anLockMode + " = '" + modeShort + "');");


M11_LRT.genProcSectionHeader(fileNo, "Step 4: add history entry", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue -1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");


M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 1;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################

mode = "EXCLUSIVEWRITE";
qualProcNameSetLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, mode, null, null);

M22_Class_Utilities.printSectionHeader("SP to acquire LOCK for 'Set Productive' (" + mode + ")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameSetLocks);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "data pool to acquire LOCK for");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools locked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "parse dataPoolDescr_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescr_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify syntax of input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;");

M11_LRT.genProcSectionHeader(fileNo, "Step 1: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");


M11_LRT.genProcSectionHeader(fileNo, "Step 2: insert new lock", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockModeExclusiveWrite + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M11_LRT.genProcSectionHeader(fileNo, "Step 3: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK TO SAVEPOINT rel2ProdLockSp;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RELEASE SAVEPOINT rel2ProdLockSp;");


M11_LRT.genProcSectionHeader(fileNo, "Step 4: add history entry", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockModeExclusiveWrite + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 1;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################


for (int j = 1; j <= 2; j++) {
mode = (j == 1 ? "SHAREDWRITES" : "SHAREDREADS");
modeShort = (j == 1 ? lockModeSharedWrite : lockModeSharedRead);
procNameSuffix = (j == 1 ? "_SHAREDWRITES" : "_SHAREDREADS");

//mode = "SHAREDREADS"
qualProcNameSetLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, mode, null, null);

M22_Class_Utilities.printSectionHeader("SP to acquire LOCKs for 'Set Productive' (" + mode + ")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameSetLocks);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", true, "datapools to acquire LOCKs for");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User acquiring the lock");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of datapools locked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescrs_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescrs_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "temporary table for data pool infos", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId " + M01_Globals.g_dbtEnumId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SAVEPOINT rel2ProdLockSp ON ROLLBACK RETAIN CURSORS;");

M11_LRT.genProcSectionHeader(fileNo, "loop over data pool descriptors", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR dataPoolDescrLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid        AS orgOidFltr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid         AS psOidFltr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId  AS accessModeIdFltr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescrs_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "collect all matching data pools in temporary table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.DPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.DPSPST_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anAccessModeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool + " TP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P.DPOORG_OID = TP.orgOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P.DPSPST_OID = TP.psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P." + M01_Globals.g_anAccessModeId + " = TP.accessModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(TP.accessModeId IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(orgOidFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P.DPOORG_OID = orgOidFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(psOidFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P.DPSPST_OID = psOidFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(accessModeIdFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P." + M01_Globals.g_anAccessModeId + " = accessModeIdFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");


M11_LRT.genProcSectionHeader(fileNo, "Step 1: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.orgOid = R.RPOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.psOid = R." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.accessModeId = R." + M01_Globals.g_anAccessModeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R." + M01_Globals_IVK.g_anLockMode + " IN ('" + (j == 1 ? lockModeSharedRead : lockModeSharedWrite) + "', '" + lockModeExclusiveWrite + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");


M11_LRT.genProcSectionHeader(fileNo, "Step 2: insert new lock", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M11_LRT.genProcSectionHeader(fileNo, "Step 3: check for concurrent lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "numDataPools_out = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.orgOid = R.RPOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.psOid = R." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.accessModeId = R." + M01_Globals.g_anAccessModeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R." + M01_Globals_IVK.g_anLockMode + " IN ('" + (j == 1 ? lockModeSharedRead : lockModeSharedWrite) + "', '" + lockModeExclusiveWrite + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET numDataPools_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK TO SAVEPOINT rel2ProdLockSp;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RELEASE SAVEPOINT rel2ProdLockSp;");


M11_LRT.genProcSectionHeader(fileNo, "Step 4: add history entries", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID, ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNTERS_PER_DATAPOOL (ORG_OID, PS_OID, ACCESSMODE_ID, COUNTER) AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPOORG_OID, DP.DPSPST_OID, DP.ACCESSMODE_ID, COALESCE(R.COUNTER, 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VL6CMET.DataPool DP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID, COUNT(1) AS COUNTER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " RPL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " RPL.LOCKMODE = '" + modeShort + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.RPOORG_OID = DP.DPOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.PS_OID = DP.DPSPST_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.ACCESSMODE_ID = DP.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.ACCESSMODE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.COUNTER -1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.COUNTER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.ORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNTERS_PER_DATAPOOL CPD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.DataPool p");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.orgOid = CPD.ORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.psOid = CPD.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.accessModeId = CPD.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected data pools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;");


M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genSetProdSupportForDb3(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSetProductive, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualFuncNameGenRel2ProdLockKey;
qualFuncNameGenRel2ProdLockKey = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnGenRel2ProdLockKey, ddlType, null, null, null, null, null, true);

String qualFuncNameParseDataPools;
qualFuncNameParseDataPools = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnParseDataPools, ddlType, null, null, null, null, null, true);

String qualProcNameSetLocks;
String procNameSuffix;
String mode;
String modeShort;

String qualProcNameResetLock;
int j;
for (int j = 1; j <= 2; j++) {
mode = (j == 1 ? "SHAREDWRITE" : "SHAREDREAD");
modeShort = (j == 1 ? lockModeSharedWrite : lockModeSharedRead);
procNameSuffix = (j == 1 ? "_SHAREDWRITE" : "_SHAREDREAD");

// ####################################################################################################################
// #    Release LOCKs for 'Set Productive'
// ####################################################################################################################

qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, procNameSuffix, M04_Utilities.ObjNameDelimMode.eondmNone, null);

M22_Class_Utilities.printSectionHeader("SP to release LOCK for 'Set Productive'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameResetLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "data pool for which to release the lock");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) releasing the lock");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User releasing the lock");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools unlocked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lockValue", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameSetLocks, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "parse dataPoolDescr_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescr_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify syntax of input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameSetLocks, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

//determine old lock value
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue = ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anLockMode + " = '" + modeShort + "');");

M11_LRT.genProcSectionHeader(fileNo, "remove log-record for lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID = requestorId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + M01_Globals_IVK.g_anLockContext + ", '') = COALESCE(lockContext_in, '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + " = '" + modeShort + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected log records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out = 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "if no log record was found, do not write history entry", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "add history-records to keep track of released locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockValue - 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpReSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN 0;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################

mode = "EXCLUSIVEWRITE";
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, mode, null, null);

M22_Class_Utilities.printSectionHeader("SP to release LOCK for 'Set Productive' (" + mode + ")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameResetLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "data pool for which to release the lock");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) releasing the lock");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User releasing the lock");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools unlocked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameResetLock, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "parse dataPoolDescr_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescr_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one row");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify syntax of input parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) OR (v_psOid IS NULL) OR (v_accessModeId IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescr_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "remove log-record for lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID = requestorId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + M01_Globals_IVK.g_anLockContext + ", '') = COALESCE(lockContext_in, '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + " = v_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RPOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + " = '" + lockModeExclusiveWrite + "';");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF numDataPools_out = 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "if no log record was found, do not write history entry", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "add history record to keep track of released locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockModeExclusiveWrite + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpReSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN 0;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

//mode = "SHAREDREADS"
for (int j = 1; j <= 2; j++) {
mode = (j == 1 ? "SHAREDWRITES" : "SHAREDREADS");
modeShort = (j == 1 ? lockModeSharedWrite : lockModeSharedRead);
procNameSuffix = (j == 1 ? "_SHAREDWRITES" : "_SHAREDREADS");
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, mode, null, null);

M22_Class_Utilities.printSectionHeader("SP to release LOCKs for 'Set Productive' (" + mode + ")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameResetLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", true, "datapools for which to release the lock");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server) releasing the locks");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User releasing the locks");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of datapools unlocked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "delimMissing", "38552", null);
M11_LRT.genCondDecl(fileNo, "castError", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR delimMissing");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescrs_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR castError");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameResetLock, ddlType, 2, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dataPoolDescrSynError", fileNo, 2, null, null, null, null, null, null, null, null, null, "dataPoolDescrs_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "temporary table for data pool infos", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId " + M01_Globals.g_dbtEnumId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameResetLock, ddlType, null, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numDataPools_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over data pool descriptors", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR dataPoolDescrLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid        AS orgOidFltr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid         AS psOidFltr,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId  AS accessModeIdFltr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE(" + qualFuncNameParseDataPools + "(dataPoolDescrs_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "collect all matching data pools in temporary table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.DPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.DPSPST_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameDataPool + " TP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P.DPOORG_OID = TP.orgOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P.DPSPST_OID = TP.psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(P." + M01_Globals.g_anAccessModeId + " = TP.accessModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(TP.accessModeId IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(orgOidFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P.DPOORG_OID = orgOidFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(psOidFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P.DPSPST_OID = psOidFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(accessModeIdFltr IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(P." + M01_Globals.g_anAccessModeId + " = accessModeIdFltr)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "add history-records to keep track of locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLockHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REQUESTORID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockContext + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAccessModeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockMode + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockValueNew + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockOperation + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anLockTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RHOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNTERS_PER_DATAPOOL (ORG_OID, PS_OID, ACCESSMODE_ID, COUNTER) AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPOORG_OID, DP.DPSPST_OID, DP.ACCESSMODE_ID, COALESCE(R.COUNTER, 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VL6CMET.DataPool DP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID, COUNT(1) AS COUNTER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " RPL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPL.LOCKMODE = '" + modeShort + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RPL.RPOORG_OID, RPL.PS_OID, RPL.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.RPOORG_OID = DP.DPOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.PS_OID = DP.DPSPST_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.ACCESSMODE_ID = DP.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "requestorId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lockContext_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.ACCESSMODE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + modeShort + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.COUNTER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + lockLogOpReSet + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.ORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPD.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNTERS_PER_DATAPOOL CPD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.DataPool p");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.orgOid = CPD.ORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.psOid = CPD.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "p.accessModeId = CPD.ACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "remove log-records for locks", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRel2ProdLock + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.REQUESTORID = requestorId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(L." + M01_Globals_IVK.g_anLockContext + ", '') = COALESCE(lockContext_in, '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anAccessModeId + " = P.accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L.RPOORG_OID = P.orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anPsOid + " = P.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected data pools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS numDataPools_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, "");
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameResetLock, ddlType, null, "'dataPoolDescrs_in", "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

// ####################################################################################################################



NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

private static void genRel2ProdLockCompatibilityWrapperDdlForDb(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSetProductive, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualProcName;

// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP SET_REL2PRODLOCK", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.SET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "GENWS", null, null);

M22_Class_Utilities.printSectionHeader("SP SET_REL2PRODLOCK_GENWS", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.SET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "OTHER", null, null);

M22_Class_Utilities.printSectionHeader("SP SET_REL2PRODLOCK_OTHER", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.SET_REL2PRODLOCK_SHAREDREAD(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "OTHERS", null, null);

M22_Class_Utilities.printSectionHeader("SP SET_REL2PRODLOCK_OTHERS", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(50)", true, "specifies the data pools");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) acquiring the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.SET_REL2PRODLOCK_SHAREDREADS(dataPoolDescrs_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP RESET_REL2PRODLOCK", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.RESET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "GENWS", null, null);

M22_Class_Utilities.printSectionHeader("SP RESET_REL2PRODLOCK_GENWS", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.RESET_REL2PRODLOCK_EXCLUSIVEWRITE(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "OTHER", null, null);

M22_Class_Utilities.printSectionHeader("SP RESET_REL2PRODLOCK_OTHER", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.RESET_REL2PRODLOCK_SHAREDREAD(dataPoolDescr_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "OTHERS", null, null);

M22_Class_Utilities.printSectionHeader("SP RESET_REL2PRODLOCK_OTHERS", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(50)", true, "specifies the data pools");
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "(optional) identifies the Application (Server) to release the locks for");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.RESET_REL2PRODLOCK_SHAREDREADS(dataPoolDescrs_in, requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescrs_in", "'requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnRel2ProdIsSet, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP IS_REL2PRODLOCK_SET", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool");
M11_LRT.genProcParm(fileNo, "OUT", "isLocked_out", M01_Globals.g_dbtBoolean, false, "specifies whether a LOCK is set (0=false, 1=true)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CDBM.IS_REL2PRODLOCK_SET_IN_EXCLUSIVEWRITE_MODE(dataPoolDescr_in, isLocked_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

private static void genRel2ProdLockWrapperDdlForDb(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexSetProductive, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualProcedureNameRel2ProdLocks;

String qualTabNameTempStatement;
qualTabNameTempStatement = M94_DBAdmin.tempTabNameStatement + "Rel2ProdLocks";

boolean forReset;
String spName;
int i;
for (int i = 1; i <= 2; i++) {
forReset = (i == 2);
if (forReset) {
spName = M01_ACM_IVK.spnResetRel2ProdLock;
qualProcedureNameRel2ProdLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnResetRel2ProdLocksWrapper, ddlType, null, null, null, null, null, null);
} else {
spName = M01_ACM_IVK.spnSetRel2ProdLock;
qualProcedureNameRel2ProdLocks = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnSetRel2ProdLocksWrapper, ddlType, null, null, null, null, null, null);
}

// ####################################################################################################################
// #    Wrapper-Stored Procedure for requesting / releasing REL2PROD-Locks
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Wrapper-Stored Procedure for " + (forReset ? "release of" : "requesting") + " REL2PROD-Locks", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameRel2ProdLocks);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOidList_in", "VARCHAR(400)", true, "(optional) ','-delimited list of OIDs of ProductStructures");
M11_LRT.genProcParm(fileNo, "IN", "orgOidList_in", "VARCHAR(400)", true, "(optional) ','-delimited list of OIDs of Organizations");
M11_LRT.genProcParm(fileNo, "IN", "accessModeIdList_in", "VARCHAR(50)", true, "(optional) ','-delimited list of AccessModes");
M11_LRT.genProcParm(fileNo, "IN", "abortOnFailure_in", M01_Globals.g_dbtBoolean, true, "if set to '1' abort (and rollback all locks requested so far)");
M11_LRT.genProcParm(fileNo, "IN", "lockMode_in", M01_Globals.g_dbtEnumId, true, "'0' = 'SHAREDREAD', '1' = 'EXCLUSIVEWRITE', '2' = 'SHAREDWRITE'");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the (Business-) User calling this procedure");
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", "VARCHAR(100)", true, "(optional) refers to the Use Case context");
M11_LRT.genProcParm(fileNo, "OUT", "psOidFail_out", M01_Globals.g_dbtOid, true, "identifies the PS of the (last) data pool for which lock-operation failed");
M11_LRT.genProcParm(fileNo, "OUT", "orgOidFail_out", M01_Globals.g_dbtOid, true, "identifies the Organization of the (last) data pool for which lock-operation failed");
M11_LRT.genProcParm(fileNo, "OUT", "accessModeIdFail_out", M01_Globals.g_dbtEnumId, true, "identifies the AccessMode of the (last) data pool for which lock-operation failed");
M11_LRT.genProcParm(fileNo, "OUT", "locksRequested_out", "INTEGER", true, "number of locks processed");
M11_LRT.genProcParm(fileNo, "OUT", "locksFailed_out", "INTEGER", false, "number of locks failed to process");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SAVEPOINT lockFail ON ROLLBACK RETAIN CURSORS;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 2, true);
M79_Err.genSigMsgVarDecl(fileNo, 2);
M11_LRT.genVarDecl(fileNo, "v_procName", "VARCHAR(50)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(150)", "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "0", 2, null);
M11_LRT.genVarDecl(fileNo, "v_requestorId", "VARCHAR(100)", "'anonymous'", 2, null);
M07_SpLogging.genSpLogDecl(fileNo, 2, true);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", 2, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, 2, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROLLBACK TO SAVEPOINT lockFail;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M94_DBAdmin.genDdlForTempStatement(fileNo, 2, true, 150, true, true, true, null, "Rel2ProdLocks", true, null, null, null, "status", "CHAR(1)", null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE GLOBAL TEMPORARY TABLE " + pc_tempTabNameOrgOids + "( oid  " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE GLOBAL TEMPORARY TABLE " + pc_tempTabNamePsOids + "( oid  " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE GLOBAL TEMPORARY TABLE " + pc_tempTabNameAccessModeIds + "( id " + M01_Globals.g_dbtEnumId + " ) NOT LOGGED WITH REPLACE;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 2, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out", null);

M11_LRT.genProcSectionHeader(fileNo, "verify input parameter", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET cdUserId_in = COALESCE(cdUserId_in, LEFT(CURRENT USER, 16));");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET psOidFail_out        = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET orgOidFail_out       = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET accessModeIdFail_out = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET locksRequested_out   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET locksFailed_out      = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine procedure to call", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF lockMode_in = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_procName = '" + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, spName, ddlType, null, null, null, "SHAREDREAD", null, null) + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF lockMode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_procName = '" + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, spName, ddlType, null, null, null, "EXCLUSIVEWRITE", null, null) + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF lockMode_in = 2 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_procName = '" + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, spName, ddlType, null, null, null, "SHAREDWRITE", null, null) + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 3, null, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out");
M79_Err.genSignalDdlWithParms("illegParam", fileNo, 3, "lockMode_in", null, null, null, null, null, null, null, null, "RTRIM(CHAR(lockMode_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine referred ORG-OIDs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF orgOidList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNameOrgOids + " ( oid ) SELECT O." + M01_Globals.g_anOid + " FROM " + M01_Globals.g_qualTabNameOrganization + " O;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNameOrgOids + " ( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(orgOidList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals.g_qualTabNameOrganization + " O ON O." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine referred PS-OIDs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF psOidList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNamePsOids + " ( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNamePsOids + " ( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals_IVK.g_qualTabNameProductStructure + " P ON P." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine referred AccessMode-IDs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF accessModeIdList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNameAccessModeIds + " ( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + pc_tempTabNameAccessModeIds + " ( id )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtEnumId + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S ON S.ID = " + M01_Globals.g_dbtEnumId + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over referred data pools and lock", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR poolLoop AS poolCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPOORG_OID AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPSPST_OID AS c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP." + M01_Globals.g_anAccessModeId + " AS c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VL6CMET.DATAPOOL DP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameOrgOids + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPOORG_OID = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNamePsOids + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPSPST_OID = P.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + pc_tempTabNameAccessModeIds + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP." + M01_Globals.g_anAccessModeId + " = A.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPOORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP.DPSPST_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DP." + M01_Globals.g_anAccessModeId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "count data pool", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET locksRequested_out = locksRequested_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "determine statement to execute", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL ' || v_procName || '(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || '''' ||" + "', ''' || v_requestorId || ''', ''' || cdUserId_in || ''', ''' || lockContext_in || ''', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_numDataPools = 0;");

M11_LRT.genProcSectionHeader(fileNo, "execute statement", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE v_stmnt INTO v_numDataPools;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in < 2 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTempStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "status,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN v_numDataPools = 0 THEN '-' ELSE '+' END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that operation succeeded", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_numDataPools = 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "count data pool failed", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET locksFailed_out = locksFailed_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "keep track of failed data pool ", 5, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET psOidFail_out        = c_psOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET orgOidFail_out       = c_orgOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET accessModeIdFail_out = c_accessModeId;");

M11_LRT.genProcSectionHeader(fileNo, "exit - if requested to abort on failure", 5, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "IF abortOnFailure_in = 1 THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameRel2ProdLocks, ddlType, -6, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ROLLBACK TO SAVEPOINT lockFail;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "status,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameTempStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF mode_in = 0 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameTempStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameRel2ProdLocks, ddlType, 2, "mode_in", "'psOidList_in", "'orgOidList_in", "'accessModeIdList_in", "abortOnFailure_in", "lockMode_in", "psOidFail_out", "orgOidFail_out", "accessModeIdFail_out", "locksRequested_out", "locksFailed_out", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genSetProdSupportDdlByPool( Integer thisOrgIndexW, Integer srcPoolIndexW, Integer dstPoolIndexW, Integer ddlTypeW) {
int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

int srcPoolIndex; 
if (srcPoolIndexW == null) {
srcPoolIndex = -1;
} else {
srcPoolIndex = srcPoolIndexW;
}

int dstPoolIndex; 
if (dstPoolIndexW == null) {
dstPoolIndex = -1;
} else {
dstPoolIndex = dstPoolIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, dstPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualViewNameAffectedPdmTabGlob;
qualViewNameAffectedPdmTabGlob = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnSetProdAffectedPdmTab, M01_ACM.vsnSetProdAffectedPdmTab, ddlType, null, null, null, null, null, null, null, null, null, null);

String qualTabNameChangeLog;
String qualTabNameChangeLogNl;

qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);
qualTabNameChangeLogNl = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspectSrc;
qualTabNameGenericAspectSrc = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspectDst;
qualTabNameGenericAspectDst = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

// ####################################################################################################################
// #    SP for Determining Entities affected by 'Set Data Productive'
// ####################################################################################################################

String qualProcedureNameSpGetAffectedEntities;

qualProcedureNameSpGetAffectedEntities = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexProductStructure, M01_ACM_IVK.spnSPGetAffectedEntities, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Determining Entities affected by 'Set Data Productive'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSpGetAffectedEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - if specified only consider prices related to this LRT");

M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of tables containing records to be set productive");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records found to be 'set productive'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M86_SetProductive.genDdlForTempTablesSp(fileNo, null, true, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.IDX_SPAFFECTEDENTITIES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(OID ASC);");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSpGetAffectedEntities, ddlType, null, "psOid_in", "lrtOid_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameters", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "cleanup temporary table", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM " + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM " + M01_Globals_IVK.gc_tempTabNameSpFilteredEntities + ";");

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
String qualTabNameProperty;
qualTabNameProperty = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
String priceAssignmentClassIdList;
priceAssignmentClassIdList = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIdStrListNonAbstractPriceAssignment;
String qualTabNameTypeSpec;
qualTabNameTypeSpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeSpec, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
String qualTabNameTypeStandardEquipment;
qualTabNameTypeStandardEquipment = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeStandardEquipment, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "if LRT-OID is specified determine OIDs of records related to this LRT for filtering", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF lrtOid_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameSpFilteredEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "priceOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_FilteredOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(LEFT(" + M01_Globals_IVK.g_anValue + ",19) AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameRegistryDynamic);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anSection + " = '" + M01_PDM_IVK.gc_regDynamicSectionAutoSetProd + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anKey + " = '" + M01_PDM_IVK.gc_regDynamicKeyAutoSetProd + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anSubKey + " = '" + new String ("00" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true)).substring(new String ("00" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true)).length() - 1 - 2) + "-' || RTRIM(CAST(lrtOid_in AS CHAR(40)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualFuncNameIsNumeric + "(" + M01_Globals_IVK.g_anValue + ") = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_FilteredOid R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER Join");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectSrc + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.oid = A." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables related to 'set productive'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " AS c_entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " AS c_classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmIsPriceRelated + " AS c_isPriceRelated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " AS c_entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmCondenseData + " AS c_condenseData,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "' AND A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid + " THEN 1 ELSE 0 END) AS c_isAggHead,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityShortName + " || '_OID' AS c_fkName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(NP2DIV.RELSHORTNAME || NP2DIV.DIRRELSHORTNAME || '_OID', (CASE WHEN A.AHCLASSID = '05006' AND A.ENTITYID <> A.AHCLASSID AND A.ISPS=0 THEN 'DIV_OID' ELSE cast (NULL as varchar(20)) END)) AS c_fkNameDiv,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmIsPs + " AS c_isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " AS c_isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " AS c_isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmTableName + " AS c_parTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmTableName + " AS c_parParTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = LPAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LPAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LPAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPAR." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPAR." + M01_Globals.g_anLdmIsGen + " <= L." + M01_Globals.g_anLdmIsGen + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(LPAR." + M01_Globals.g_anLdmIsNl + " = 0 AND L." + M01_Globals.g_anLdmIsNl + " = 0 AND LPAR." + M01_Globals.g_anLdmIsGen + " <> L." + M01_Globals.g_anLdmIsGen + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(LPAR." + M01_Globals.g_anLdmIsNl + " = 0 AND L." + M01_Globals.g_anLdmIsNl + " = 1 AND LPAR." + M01_Globals.g_anLdmIsGen + " = L." + M01_Globals.g_anLdmIsGen + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmLdmFkSchemaName + " = LPAR." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmLdmFkTableName + " = LPAR." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPoolTypeId + " = P." + M01_Globals.g_anPoolTypeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LPARPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = LPARPAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LPARPAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LPARPAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsGen + " < LPAR." + M01_Globals.g_anLdmIsGen + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PPARPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmLdmFkSchemaName + " = LPARPAR." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmLdmFkTableName + " = LPARPAR." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPoolTypeId + " = P." + M01_Globals.g_anPoolTypeId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityName + " AS RELNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityShortName + " AS RELSHORTNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLeftEntityName + " AS REFENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLrShortName + " AS DIRRELSHORTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRightEntityName + " = '" + M01_ACM_IVK.clnDivision.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityName + " AS RELNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityShortName + " AS RELSHORTNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRightEntityName + " AS REFENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRlShortName + " AS DIRRELSHORTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLeftEntityName + " = '" + M01_ACM_IVK.clnDivision.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") NP2DIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NP2DIV.REFENTITYNAME = A." + M01_Globals.g_anAcmEntityName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCto + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCtp + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(srcPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A." + M01_Globals_IVK.g_anAcmCondenseData + " = 0 OR (A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "' AND A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(lrtOId_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "((A." + M01_Globals_IVK.g_anAcmCondenseData + " = 1 OR A." + M01_Globals_IVK.g_anAcmIsPriceRelated + " = 1) AND L." + M01_Globals.g_anLdmIsNl + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY L.FKSEQUENCENO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_condenseData = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "just insert a 'dummy-OID'", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO " + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + "(orParEntityType,orParEntityId,isNl,isGen,oid,opId) VALUES (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''' || c_entityType || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''' || c_entityId || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(c_isNl AS CHAR(1)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(c_isGen AS CHAR(1)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'0,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + String.valueOf(M11_LRT.lrtStatusCreated) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO " + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + "(orParEntityType,orParEntityId,isNl,isGen,oid,opId) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''' || c_entityType || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''' || c_entityId || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(c_isNl AS CHAR(1)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(c_isGen AS CHAR(1)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals.g_anOid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(CASE WHEN T." + M01_Globals_IVK.g_anHasBeenSetProductive + "=" + M01_LDM.gc_dbFalse + " AND T." + M01_Globals_IVK.g_anIsDeleted + "=" + M01_LDM.gc_dbFalse + " THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " " + "WHEN T." + M01_Globals_IVK.g_anHasBeenSetProductive + "=" + M01_LDM.gc_dbTrue + " AND T." + M01_Globals_IVK.g_anIsDeleted + "=" + M01_LDM.gc_dbFalse + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " " + "WHEN T." + M01_Globals_IVK.g_anHasBeenSetProductive + "=" + M01_LDM.gc_dbTrue + " AND T." + M01_Globals_IVK.g_anIsDeleted + "=" + M01_LDM.gc_dbTrue + " THEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " " + "ELSE CAST(NULL AS " + M01_Globals.g_dbtEnumId + ") END) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' || c_schemaName || '.' || c_tableName || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN lrtOid_in IS NULL THEN 'T." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE 'EXISTS (SELECT 1 FROM " + M01_Globals_IVK.gc_tempTabNameSpFilteredEntities + " F WHERE F.priceOid = T." + M01_Globals.g_anAhOid + ")'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE c_isPs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN c_fkNameDiv IS NULL THEN ''");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 0) AND (c_isGen = 0)) THEN " + "' AND T.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1) AND (c_isGen = 1)) THEN " + "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P.' || c_fkName || ' = PP." + M01_Globals.g_anOid + " AND PP.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1)  OR (c_isGen = 1)) THEN " + "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE  ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_isNl = 0) AND (c_isGen = 0) THEN 'T." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_isNl = 1) AND (c_isGen = 1) THEN 'EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P.' || c_fkName || ' = PP." + M01_Globals.g_anOid + " AND PP." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_isNl = 1)  OR (c_isGen = 1) THEN 'EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");

M11_LRT.genProcSectionHeader(fileNo, "filter out calculated prices", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE WHEN (c_isPriceRelated = 1) AND (c_classId = '" + M22_Class_Utilities.getClassIdByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect) + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN c_isAggHead=1 AND c_isNl=0 THEN ' AND ((T." + M01_Globals.g_anCid + " NOT IN (" + M00_Helper.replace(priceAssignmentClassIdList, "'", "''") + ")) OR (COALESCE((SELECT PRT.ID FROM " + qualTabNameProperty + " PRP INNER JOIN " + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " PRT ON PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid + " WHERE T.PRPAPR_OID = PRP." + M01_Globals.g_anOid + "), -1) NOT IN (" + M87_FactoryTakeOver.propertyTemplateIdListCalcPrice + ")))'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE ' AND ((T." + M01_Globals.g_anAhCid + " NOT IN (" + M00_Helper.replace(priceAssignmentClassIdList, "'", "''") + ")) OR (COALESCE((SELECT PRT.ID FROM " + qualTabNameGenericAspect + " GA INNER JOIN " + qualTabNameProperty + " PRP ON GA.PRPAPR_OID = PRP." + M01_Globals.g_anOid + " INNER JOIN " + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " PRT ON PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid + " WHERE T." + M01_Globals.g_anAhOid + " = GA." + M01_Globals.g_anOid + "), -1) NOT IN (" + M87_FactoryTakeOver.propertyTemplateIdListCalcPrice + ")))'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");

M11_LRT.genProcSectionHeader(fileNo, "filter out typespecs with references to non-productive TypePriceAssignments", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE WHEN (c_entityType = 'C' AND c_entityId = '" + M22_Class_Utilities.getClassIdByClassIndex(M01_Globals_IVK.g_classIndexTypeSpec) + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "' AND (T.TSTTPA_OID IS NULL' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "' OR T.TSTTPA_OID IN (SELECT TPA.OID FROM " + qualTabNameGenericAspect + " TPA WHERE TPA.STATUS_ID = 5)' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "' OR T.TSTTPA_OID IN (SELECT OID FROM " + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + "))'");
//  Print #fileNo, addTab(7); "' AND (T.PTYPTY_OID IS NULL OR T.PTYPTY_OID IN (SELECT TS.OID FROM "; qualTabNameTypeSpec; " TS WHERE TS.STATUS_ID IN (4,5)))'"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");

M11_LRT.genProcSectionHeader(fileNo, "filter out typestandardequipments with references to non-productive TypeSpecs", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE WHEN (c_entityType = 'C' AND c_entityId = '" + M22_Class_Utilities.getClassIdByClassIndex(M01_Globals_IVK.g_classIndexTypeStandardEquipment) + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "' AND (T.TSETYS_OID IN (SELECT TS.OID FROM " + qualTabNameTypeSpec + " TS WHERE TS.STATUS_ID = 5)' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "' OR (T.TSETYS_OID IN (SELECT OID FROM " + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + ")))'");
//Print #fileNo, addTab(7); "' AND (TS.PTYPTY_OID IS NULL OR TS.PTYPTY_OID IN (SELECT PREV.OID FROM "; qualTabNameTypeSpec; " PREV WHERE PREV.STATUS_ID IN (4,5))))'"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' WITH UR';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows and tables", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_rowCount > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSpGetAffectedEntities, ddlType, null, "psOid_in", "lrtOid_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for preprocessing Setting Data Productive GENERICASPECT
// ####################################################################################################################

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex) {

String qualProcedureNameSetProdPre;
qualProcedureNameSetProdPre = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAspect, M01_ACM_IVK.spnSetProductivePreProcess, ddlType, thisOrgIndex, srcPoolIndex, null, M01_ACM_IVK.clnGenericAspect.toUpperCase(), null, null);

M22_Class_Utilities.printSectionHeader("SP for preprocessing Setting Data Productive '" + qualTabNameGenericAspectDst + "'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetProdPre);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - if specified set only prices productive related to this LRT");
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, false, "identifies the operation (insert, update, delete, gen NL-Text for ChangeLog) to set productive");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " ) THEN");
M11_LRT.genProcSectionHeader(fileNo, "CCPCCP_OID reference set NULL, if the central record has been deleted", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectDst + " AS gas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gas.ccpccp_oid = NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.ccpccp_oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas." + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.classid IN ( '09031', '09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.ps_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameGenericAspectSrc + " AS gas_ne");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas.ccpccp_oid = gas_ne.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas_ne." + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas_ne.classid IN ( '09031', '09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas_ne.ps_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas_ne." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

// ####################################################################################################################
// #   SP for determining if division data is set productive
// ####################################################################################################################

String qualProcedureName;

qualProcedureName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnSetProductiveIncludesDivisionData, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for determining if division data is set productive", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "OUT", "result_out", M01_Globals.g_dbtBoolean, false, "0 = false, 1 = true");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE objCursor CURSOR FOR v_stmnt;");
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureName, ddlType, null, "psOid_in", "result_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameters", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET result_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables related to 'set productive'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityShortName + " || '_OID' AS c_fkName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " AS c_ahclassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NP2DIV.RELSHORTNAME || NP2DIV.DIRRELSHORTNAME || '_OID' AS c_fkNameDiv,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " AS c_isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " AS c_isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmTableName + " AS c_parTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmTableName + " AS c_parParTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = LPAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LPAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LPAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPAR." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPAR." + M01_Globals.g_anLdmIsGen + " <= L." + M01_Globals.g_anLdmIsGen + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(LPAR." + M01_Globals.g_anLdmIsNl + " = 0 AND L." + M01_Globals.g_anLdmIsNl + " = 0 AND LPAR." + M01_Globals.g_anLdmIsGen + " <> L." + M01_Globals.g_anLdmIsGen + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(LPAR." + M01_Globals.g_anLdmIsNl + " = 0 AND L." + M01_Globals.g_anLdmIsNl + " = 1 AND LPAR." + M01_Globals.g_anLdmIsGen + " = L." + M01_Globals.g_anLdmIsGen + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmLdmFkSchemaName + " = LPAR." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPdmLdmFkTableName + " = LPAR." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPAR." + M01_Globals.g_anPoolTypeId + " = P." + M01_Globals.g_anPoolTypeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LPARPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = LPARPAR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LPARPAR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LPARPAR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsGen + " < LPAR." + M01_Globals.g_anLdmIsGen + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LPARPAR." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PPARPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmLdmFkSchemaName + " = LPARPAR." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPdmLdmFkTableName + " = LPARPAR." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PPARPAR." + M01_Globals.g_anPoolTypeId + " = P." + M01_Globals.g_anPoolTypeId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityName + " AS RELNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityShortName + " AS RELSHORTNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLeftEntityName + " AS REFENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLrShortName + " AS DIRRELSHORTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRightEntityName + " = '" + M01_ACM_IVK.clnDivision.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityName + " AS RELNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityShortName + " AS RELSHORTNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRightEntityName + " AS REFENTITYNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmRlShortName + " AS DIRRELSHORTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAcmLeftEntityName + " = '" + M01_ACM_IVK.clnDivision.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") NP2DIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NP2DIV.REFENTITYNAME = A." + M01_Globals.g_anAcmEntityName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCto + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCtp + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ISPS = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ISPSFORMING = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(srcPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A." + M01_Globals_IVK.g_anAcmCondenseData + " = 0 OR (A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "' AND A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid + "))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT COUNT(*) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' || c_schemaName || '.' || c_tableName || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'T." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " || (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "CASE");
//special logic for CodeValidForOrganization and EndNodeHasGnericCode, which have same schema as GenericCode
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NULL AND c_ahclassid = '05006') THEN " + "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.GENERICCODE P WHERE T.GCO_OID = P." + M01_Globals.g_anOid + " AND P.CDIDIV_OID = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN c_fkNameDiv IS NULL THEN ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 0) AND (c_isGen = 0)) THEN " + "' AND T.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || ')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1) AND (c_isGen = 1)) THEN " + "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parParTableName || ' PP,' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P.' || c_fkName || ' = PP." + M01_Globals.g_anOid + " AND PP.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN (c_fkNameDiv IS NOT NULL) AND ((c_isNl = 1)  OR (c_isGen = 1)) THEN " + "' AND EXISTS (SELECT 1 FROM ' || c_schemaName || '.' || c_parTableName || ' P WHERE T.' || c_fkName || ' = P." + M01_Globals.g_anOid + " AND P.' || c_fkNameDiv || ' = (SELECT PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = ' || RTRIM(CAST(psOid_in AS CHAR(20))) || '))'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " || ' WITH UR';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN objCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH objCursor INTO v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE objCursor;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_rowCount > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET result_out = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureName, ddlType, null, "psOid_in", "result_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

//------------------------------------------------------------------------------------------------


boolean callGenWorkSpace;
boolean simulate;
String procNameSuffix;
int p;
for (int p = 2; p <= (M03_Config.supportSimulationSps ? 3 : 2); p++) {
// we currently do not support SETPRODUCTIVE without GEN_WORKSPACE
// to support this start loop with 'p = 1'
callGenWorkSpace = (p == 2);
simulate = (p == 3);
procNameSuffix = (simulate ? "sim" : "");
// ####################################################################################################################
// #    SP for Setting Data Productive
// ####################################################################################################################

String qualProcedureNameSetProdInt;
String qualProcedureNameSetProd;

qualProcedureNameSetProd = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, null, procNameSuffix, null, null);
qualProcedureNameSetProdInt = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexProductStructure, M01_ACM_IVK.spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, null, procNameSuffix, null, null);
M22_Class_Utilities.printSectionHeader("SP for Setting Data Productive (internal)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetProdInt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - if specified set only prices productive related to this LRT");
if (callGenWorkSpace) {
M11_LRT.genProcParm(fileNo, "IN", "isAdHoc_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse + " and isGenWsAs_in = " + M01_LDM.gc_dbTrue);
M11_LRT.genProcParm(fileNo, "IN", "isGenWsAs_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse + " and isGenWsAs_in = " + M01_LDM.gc_dbTrue);
}

if (simulate) {
M11_LRT.genProcParm(fileNo, "OUT", "refId_out", "INTEGER", true, "ID used to identify persisted records related to this procedure call");
}

M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected");

if (callGenWorkSpace) {
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_opType", "INTEGER", String.valueOf(M11_LRT.lrtStatusCreated), null, null);
M11_LRT.genVarDecl(fileNo, "v_setProductiveTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_isUnderConstruction", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
if (callGenWorkSpace) {
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
}

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M86_SetProductive.genDdlForTempTablesSp(fileNo, null, null, true, null, null);
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, null, null, null, true, null, null, null);

if (callGenWorkSpace) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
if (simulate) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}
}

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out      = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_setProductiveTs = CURRENT TIMESTAMP;");

if (simulate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET refId_out         = NULL;");
}

if (callGenWorkSpace) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspError_out     = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspInfo_out      = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspWarning_out   = NULL;");
}

if (simulate) {
M11_LRT.genProcSectionHeader(fileNo, "set savepoint to rollback to", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SAVEPOINT simulateReset UNIQUE ON ROLLBACK RETAIN CURSORS;");
}

M11_LRT.genProcSectionHeader(fileNo, "verify that Product Structure is not 'under construction'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsUnderConstruction);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_isUnderConstruction");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT (v_isUnderConstruction = 0) THEN");
if (callGenWorkSpace) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameSetProdInt, ddlType, 2, "psOid_in", "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}
}
M79_Err.genSignalDdlWithParms("setProdUndConstr", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine OIDs affected by 'Set Productive'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameSpGetAffectedEntities + "(psOid_in, lrtOid_in, v_tabCount, v_rowCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rowCount = 0;");

M11_LRT.genProcSectionHeader(fileNo, "handle all 'DELETE', 'INSERT', 'UPDATE' and 'GEN-NL-CHANGELOG'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_opType IS NOT NULL DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_SpAffectedEntity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orParEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orParEntityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(v_opType = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(opId = v_opType)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_SpAffectedTab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V.SOURCE_SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V.SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameAffectedPdmTabGlob + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_SpAffectedEntity E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anAcmEntityType + " = E.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anAcmEntityId + " = E.entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(E.isNl = 1 OR V." + M01_Globals.g_anLdmIsNl + " = E.isNl)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anLdmIsGen + " = E.isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(srcPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "V." + M01_Globals.g_anAcmEntityType + " <> '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "V." + M01_Globals_IVK.g_anAcmCondenseData + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "V." + M01_Globals.g_anAhCid + " = V." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.tableName  AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.schemaName AS c_schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_SpAffectedTab V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M11_LRT.genProcSectionHeader(fileNo, "sequence of tables processed must be inverted for 'DELETE'", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN -1 ELSE 1 END) * V.seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "preprocessing Setting Data Productive GENERICASPECT", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_tableName = '" + M01_ACM_IVK.clnGenericAspect.toUpperCase() + "' AND v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.SETPRODUCTIVEPREPROC_' || c_tableName || '( ?, ?, ? )' ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.SETPRODUCTIVE_' || c_tableName || '(?,?,?,?,?)' ;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_opType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_setProductiveTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_opType = (CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN " + String.valueOf(M11_LRT.lrtStatusLocked) + " ELSE NULL END);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

if (!(simulate)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF rowCount_out > 0 THEN");

M12_ChangeLog.genPersistChangeLogDdl(fileNo, M01_Globals.g_classIndexChangeLog, qualTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLog, qualTabNameChangeLogNl, M01_Globals.gc_tempTabNameChangeLogNl, qualSeqNameOid, ddlType, thisOrgIndex, dstPoolIndex, 2, M12_ChangeLog.ChangeLogMode.eclSetProd, null, null, true, "v_setProductiveTs");
}

if (callGenWorkSpace) {
M11_LRT.genProcSectionHeader(fileNo, "determine OID of Organization", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_orgOid = (SELECT ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF isAdHoc_in = " + M01_LDM.gc_dbFalse + " AND isGenWsAs_in = " + M01_LDM.gc_dbTrue + " THEN");
M27_Meta.genCallGenWorkspaceDdl(fileNo, thisOrgIndex, srcPoolIndex, "v_orgOid", "psOid_in", M72_DataPool.g_pools.descriptors[srcPoolIndex].id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", 3, ddlType, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M27_Meta.genCallGenWorkspaceDdl(fileNo, thisOrgIndex, dstPoolIndex, "v_orgOid", "psOid_in", M72_DataPool.g_pools.descriptors[dstPoolIndex].id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", 2, ddlType, null);
}

if (!(simulate)) {
M11_LRT.genProcSectionHeader(fileNo, "mark records in work data pool as 'being productive' and delete records marked as 'deleted'", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusLocked) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE v_opType <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + " DO");

M11_LRT.genProcSectionHeader(fileNo, "1st loop: INSERT, 2nd loop: UPDATE, 3rd loop: DELETE", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_opType = (CASE v_opType WHEN 0 THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ELSE " + String.valueOf(M11_LRT.lrtStatusDeleted) + " END);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_SpAffectedEntity");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "orParEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "orParEntityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "opId = v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V.SOURCE_SCHEMANAME AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN - V.SEQNO ELSE V.SEQNO END) AS c_seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameAffectedPdmTabGlob + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_SpAffectedEntity E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anAcmEntityType + " = E.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anAcmEntityId + " = E.entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(E.isNl = 1 OR V." + M01_Globals.g_anLdmIsNl + " = E.isNl)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anLdmIsGen + " = E.isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(srcPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(V." + M01_Globals_IVK.g_anAcmCondenseData + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(V." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "' AND V." + M01_Globals.g_anAhCid + " = V." + M01_Globals.g_anAcmEntityId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN - V.SEQNO ELSE V.SEQNO END) ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnSetProductivePostProcess.toUpperCase() + "_' || c_tableName || '(?,?,?)' ;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (callGenWorkSpace) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
if (simulate) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProdInt, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}
}

if (simulate) {
M11_LRT.genProcSectionHeader(fileNo, "rollback to savepoint", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ROLLBACK TO SAVEPOINT simulateReset;");

M11_LRT.genProcSectionHeader(fileNo, "persist content of temporay tables", null, null);

String qualProcNameTracePersist;
qualProcNameTracePersist = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexTrace, M01_ACM_IVK.spnTracePersist, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameTracePersist + "(refId_out, v_rowCount, v_tabCount);");

M11_LRT.genProcSectionHeader(fileNo, "release savepoint", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RELEASE SAVEPOINT simulateReset;");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

boolean genCallSwitchParam;
boolean genCallSwitchParam2;
long j;
for (int j = (simulate ? 55555 : 1); j <= (callGenWorkSpace ? 3 : 1); j++) {
genCallSwitchParam = (j == 2);
genCallSwitchParam2 = (j == 3);

// ####################################################################################################################
// #    SP for Setting Data Productive
// ####################################################################################################################

qualProcedureNameSetProd = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Setting Data Productive", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetProd);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
if (genCallSwitchParam |  genCallSwitchParam2) {
M11_LRT.genProcParm(fileNo, "IN", "isAdHoc_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse);
}
if (genCallSwitchParam2) {
M11_LRT.genProcParm(fileNo, "IN", "isGenWsAs_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse + " and isGenWsAs_in = " + M01_LDM.gc_dbTrue);
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected");

if (callGenWorkSpace) {
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

if (callGenWorkSpace) {
if (genCallSwitchParam) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null, null);
}
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, srcPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNo, "");
if (callGenWorkSpace) {
if (genCallSwitchParam2) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?,?,?,?,?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?," + (genCallSwitchParam ? "?,1," : "1,1,") + "?,?,?,?)';");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?,?)';");
}
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
if (callGenWorkSpace) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspError_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspInfo_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspWarning_out");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in,");
if (genCallSwitchParam2) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isAdHoc_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGenWsAs_in");
} else if (genCallSwitchParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isAdHoc_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (callGenWorkSpace) {
if (genCallSwitchParam) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null, null);
}
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Setting Prices Productive
// ####################################################################################################################

qualProcedureNameSetProd = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnSetProductive, ddlType, thisOrgIndex, srcPoolIndex, null, "Prices", null, null);

M22_Class_Utilities.printSectionHeader("SP for Setting Prices Productive", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetProd);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - set only prices productive related to this LRT");
if (genCallSwitchParam |  genCallSwitchParam2) {
M11_LRT.genProcParm(fileNo, "IN", "isAdHoc_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse);
}
if (genCallSwitchParam2) {
M11_LRT.genProcParm(fileNo, "IN", "isGenWsAs_in", M01_Globals.g_dbtBoolean, true, "call GEN_WORKSPACE for Work Data Pool if and only if isAdHoc_in = " + M01_LDM.gc_dbFalse + " and isGenWsAs_in = " + M01_LDM.gc_dbTrue);
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", callGenWorkSpace, "number of rows in public tables affected");

if (callGenWorkSpace) {
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

if (callGenWorkSpace) {
if (genCallSwitchParam) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
}
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, srcPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "call 'general procedure for SETPRODUCTIVE'", null, null);
if (callGenWorkSpace) {
if (genCallSwitchParam2) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?,?,?,?,?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?," + (genCallSwitchParam ? "?,1," : "1,1,") + "?,?,?,?)';");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcedureNameSetProdInt + "(?,?,?)';");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
if (callGenWorkSpace) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspError_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspInfo_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspWarning_out");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in,");
if (genCallSwitchParam2) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isAdHoc_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGenWsAs_in");
} else if (genCallSwitchParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isAdHoc_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (callGenWorkSpace) {
if (genCallSwitchParam) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "isAdHoc_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
}
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetProd, ddlType, null, "psOid_in", "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
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


private static void genSetProdSupportDdlByPoolForAllPools( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (ddlType == M01_Common.DdlTypeId.edtLdm |  thisPoolIndex < 1) {
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String sectionName;
String sectionNameShort;
int sectionIndex;

sectionName = M01_ACM.snAliasLrt;
sectionNameShort = M01_ACM.ssnAliasLrt;
sectionIndex = M01_Globals.g_sectionIndexAliasLrt;

int i;
for (int i = 1; i <= (M03_Config.supportFilteringByPsDpMapping ? 2 : 1); i++) {
if (i == 2) {
sectionIndex = M01_Globals.g_sectionIndexAliasPsDpFiltered;
sectionName = M01_ACM_IVK.snAliasPsDpFiltered;
sectionNameShort = M01_ACM_IVK.ssnAliasPsDpFiltered;
}

// ####################################################################################################################
// #    SP: Determine wether a LOCK is set on 'Set Productive'
// ####################################################################################################################

String qualProcNameLockIsSetLocal;
String qualProcNameLockIsSetGlobal;

qualProcNameLockIsSetLocal = M04_Utilities.genQualProcName(sectionIndex, M01_ACM_IVK.spnRel2ProdIsSet, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameLockIsSetGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnRel2ProdIsSet, ddlType, null, null, null, null, null, null);

genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, null, ddlType);
genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_EXCLUSIVEWRITE_MODE", ddlType);
genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_SHAREDWRITE_MODE", ddlType);
genIsLockedDdl(fileNo, qualProcNameLockIsSetLocal, qualProcNameLockIsSetGlobal, thisOrgIndex, thisPoolIndex, "IN_SHAREDREAD_MODE", ddlType);

// ####################################################################################################################
// #    'local' SPs to acquire LOCKs for 'Set Productive'
// ####################################################################################################################

String qualProcNameLocal;
String qualProcNameGlobal;
qualProcNameLocal = M04_Utilities.genQualProcName(sectionIndex, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, null, null, null);

//TODO(TF): remove these wrapper SPs as soon as application code is changed
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", null, ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "GENWS", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "OTHER", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "OTHERS", ddlType, null);

genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "EXCLUSIVEWRITE", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDWRITE", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDREAD", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDREADS", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "acquire", "SHAREDWRITES", ddlType, null);

// ####################################################################################################################
// #    'local' SPs to release LOCKs for 'Set Productive'
// ####################################################################################################################

qualProcNameLocal = M04_Utilities.genQualProcName(sectionIndex, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, null, null, null);

//TODO(TF): remove these wrapper SPs as soon as application code is changed
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", null, ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "GENWS", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "OTHER", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "OTHERS", ddlType, null);

genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "EXCLUSIVEWRITE", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "SHAREDWRITE", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 1, thisOrgIndex, thisPoolIndex, "release", "SHAREDREAD", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "SHAREDREADS", ddlType, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 2, thisOrgIndex, thisPoolIndex, "release", "SHAREDWRITES", ddlType, null);

qualProcNameLocal = M04_Utilities.genQualProcName(sectionIndex, M01_ACM_IVK.spnResetRel2ProdLocks, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLocks, ddlType, null, null, null, null, null, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 0, thisOrgIndex, thisPoolIndex, "release", null, ddlType, false);

if (thisOrgIndex == M01_Globals.g_primaryOrgIndex &  (thisPoolIndex == M01_Globals.g_workDataPoolIndex) & i == 1) {
qualProcNameLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAlias, M01_ACM_IVK.spnResetRel2ProdLocks, ddlType, null, null, null, null, null, null);
genSetLockDdl(fileNo, qualProcNameLocal, qualProcNameGlobal, 0, -1, -1, "release", null, ddlType, false);
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
private static void genIsLockedDdl(int fileNo, String qualProcNameLocal, String qualProcNameGlobal,  int thisOrgIndex,  int thisPoolIndex, String procNameSuffixW, Integer ddlTypeW) {
String procNameSuffix; 
if (procNameSuffixW == null) {
procNameSuffix = "";
} else {
procNameSuffix = procNameSuffixW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String suffix;
suffix = (procNameSuffix.compareTo("") == 0 ? "" : "_" + procNameSuffix);


M22_Class_Utilities.printSectionHeader("SP determinig whether a LOCK is set on 'Set Productive' for a given data pool" + procNameSuffix, fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLocal + suffix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "specifies the data pool to query the LOCK-status for");
M11_LRT.genProcParm(fileNo, "OUT", "isLocked_out", M01_Globals.g_dbtBoolean, false, "specifies whether a LOCK is set (0=false, 1=true)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLocal + suffix, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "call 'global' procedure'", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGlobal + suffix + "(dataPoolDescr_in, isLocked_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLocal + suffix, ddlType, null, "'dataPoolDescr_in", "isLocked_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

private static void genSetLockDdl(int fileNo, String qualProcNameLocal, String qualProcNameGlobal, int numPoolDescrs,  int thisOrgIndex,  int thisPoolIndex, String descrW, String procNameSuffixW, Integer ddlTypeW, Boolean includeLockContextW) {
String descr; 
if (descrW == null) {
descr = "acquire";
} else {
descr = descrW;
}

String procNameSuffix; 
if (procNameSuffixW == null) {
procNameSuffix = "";
} else {
procNameSuffix = procNameSuffixW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean includeLockContext; 
if (includeLockContextW == null) {
includeLockContext = true;
} else {
includeLockContext = includeLockContextW;
}

String suffix;
suffix = (procNameSuffix.compareTo("") == 0 ? "" : "_" + procNameSuffix);

M22_Class_Utilities.printSectionHeader("SP to " + descr + " locks for 'Set Productive'" + (procNameSuffix.compareTo("") == 0 ? "" : " (" + procNameSuffix + ")"), fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLocal + suffix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (numPoolDescrs == 1) {
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescr_in", "VARCHAR(50)", true, "data pool for which to " + descr + " the lock");
} else if (numPoolDescrs > 0) {
M11_LRT.genProcParm(fileNo, "IN", "dataPoolDescrs_in", "VARCHAR(4000)", true, "data pools for which to " + descr + " the locks");
}
M11_LRT.genProcParm(fileNo, "IN", "requestorId_in", M01_Globals_IVK.g_dbtLockRequestorId, true, "identifies the Application (Server)");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "identifies the User");
if (includeLockContext) {
M11_LRT.genProcParm(fileNo, "IN", "lockContext_in", M01_Globals_IVK.g_dbtR2pLockContext, true, "(optional) refers to the Use Case context");
}
M11_LRT.genProcParm(fileNo, "OUT", "numDataPools_out", "INTEGER", false, "number of data pools processed");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

if (includeLockContext) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLocal + suffix, ddlType, null, (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "'dataPoolDescr_in" : "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLocal + suffix, ddlType, null, (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "'dataPoolDescr_in" : "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null);
}

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "call 'global' procedure'", null, true);
if (includeLockContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGlobal + suffix + "(" + (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "dataPoolDescr_in, " : "dataPoolDescrs_in, ")) + "requestorId_in, cdUserId_in, lockContext_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLocal + suffix, ddlType, null, (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "'dataPoolDescr_in" : "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "'lockContext_in", "numDataPools_out", null, null, null, null, null, null, null);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameGlobal + suffix + "(" + (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "dataPoolDescr_in, " : "dataPoolDescrs_in, ")) + "requestorId_in, cdUserId_in, numDataPools_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLocal + suffix, ddlType, null, (numPoolDescrs == 0 ? "" : (numPoolDescrs == 1 ? "'dataPoolDescr_in" : "'dataPoolDescrs_in")), "requestorId_in", "'cdUserId_in", "numDataPools_out", null, null, null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


public static void genSetProdSupportSpsForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex, int srcPoolIndex, int dstPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

int sectionIndex;
String acmEntityName;
String acmEntityShortName;
String entityTypeDescr;
boolean isUserTransactional;
boolean isPsTagged;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
String entityIdStrList;
String dbAcmEntityType;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
boolean hasNoIdentity;
boolean ignoreForChangelog;
boolean hasNlAttributes;
int aggHeadClassIndex;
String aggHeadShortClassName;
boolean isGenericAspect;
int navToDivRelRefIndex;// follow this relationship when navigating to Division
Integer navToDivDirection;// indicates wheter we need to follow left or right hand side to navigate to Division
Integer navToFirstClassToDivDirection;// if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
int navRefClassIndex;
String navRefClassShortName;
String fkAttrToClass;
boolean hasGroupIdAttrs;
boolean isSubjectToPreisDurchschuss;
boolean condenseData;
boolean isAggHead;
int[] aggChildClassIndexes;

//On Error GoTo ErrorExit 

isGenericAspect = false;
aggChildClassIndexes =  new int[0];

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
navToFirstClassToDivDirection = -1;
navToDivRelRefIndex = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.navDirection;
navRefClassIndex = -1;

sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
isGenericAspect = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == "GENERICASPECT");
isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;

hasGroupIdAttrs = !(forNl & ! forGen & M22_Class.g_classes.descriptors[acmEntityIndex].hasGroupIdAttrInNonGenInclSubClasses);

if (forNl) {
entityTypeDescr = "ACM-Class (NL-Text)";
} else {
entityTypeDescr = "ACM-Class";
}
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
entityIdStrList = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
aggChildClassIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
navToFirstClassToDivDirection = M23_Relationship.g_relationships.descriptors[acmEntityIndex].navPathToDiv.navDirectionToClass;
navToDivRelRefIndex = -1;
navToDivDirection = -1;
if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etLeft) {
// we need to follow relationship to left -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].navPathToDiv.navDirection;
} else if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etRight) {
// we need to follow relationship to right -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[acmEntityIndex].lrShortRelName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].navPathToDiv.navDirection;
}

sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
if (forNl) {
entityTypeDescr = "ACM-Relationship (NL-Text)";
} else {
entityTypeDescr = "ACM-Relationship";
}

hasGroupIdAttrs = false;

hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
entityIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
dbAcmEntityType = "R";
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
relRefs.numRefs = 0;
isGenForming = false;
hasNoIdentity = false;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
isSubjectToPreisDurchschuss = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
condenseData = false;
isAggHead = false;
} else {
return;
}

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 0 |  srcPoolIndex < 0)) {
// LRT is only supported at 'pool-level'
return;
}
if (condenseData & ! isAggHead) {
// propagataion of data for aggregate children is done by aggregate head
return;
}

if (aggHeadClassIndex > 0) {
aggHeadShortClassName = M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
String qualTabNameSrc;
String qualTabNameSrcPar;
String qualTabNameTgt;
String qualTabNamenNavRef;
String qualTabNameAggHead;
String qualTabNameAggHeadNl;
String qualTabNameSrcNl;
String qualTabNameTgtNl;
String qualTabNameSrcGen;

if (navRefClassIndex > 0) {
qualTabNamenNavRef = M04_Utilities.genQualTabNameByClassIndex(navRefClassIndex, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, null, null, null, null);
}

qualTabNameSrc = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, forNl, null, null, null);
qualTabNameTgt = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, forNl, null, null, null);

if (forNl |  (navRefClassIndex > 0)) {
qualTabNameSrcPar = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, null, null, null, null);
}

if (isGenForming & ! hasNoIdentity) {
qualTabNameSrcGen = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, true, null, null, null, null, null, null);
} else {
qualTabNameSrcGen = "";
}

if (!(ignoreForChangelog & ! forNl & hasNlAttributes)) {
qualTabNameTgtNl = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, true, null, null, null);
qualTabNameSrcNl = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, true, null, null, null);
}

if (aggHeadClassIndex > 0) {
qualTabNameAggHead = M04_Utilities.genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
qualTabNameAggHeadNl = M04_Utilities.genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, true, null, null, null);
}

String fkAttrToDiv;
String psFkAttrToDiv;
if (navToDivRelRefIndex > 0) {
if (M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
psFkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
psFkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
if (navToDivDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navToDivRelRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navToDivRelRefIndex].rightFkColName[ddlType];
}
}

String attrNameFkEntity;
attrNameFkEntity = M04_Utilities.genSurrogateKeyName(ddlType, acmEntityShortName, null, null, null, null);

// ####################################################################################################################
// #    SP for Setting Data Productive for given class / relationship
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnSetProductive, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Setting Data Productive for \"" + qualTabNameSrc + "\" (" + entityTypeDescr + " \"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + acmEntityName + "\"" + (forGen ? "(GEN)" : "") + ")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to set productive");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - if NOT NULL set only prices productive related to this LRT");
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, true, "identifies the operation (insert, update, delete, gen NL-Text for ChangeLog) to set productive");
M11_LRT.genProcParm(fileNo, "IN", "setProductiveTs_in", "TIMESTAMP", true, "marks the timestamp of setting data productive");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this call");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

if (!())) {
if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
// we currently only have this for Expressions; we thus explicitly refer to Propagate-Routine for Expressions
String qualProcNamePropagate;
qualProcNamePropagate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnPropExpr, ddlType, null, null, null, null, null, null);

String qualProcNameInvPropagate;
qualProcNameInvPropagate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnPropInvExpr, ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_numSuccess", "INTEGER", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_numFail", "INTEGER", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "propagate new Aggregates to productive data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");

String qualProcNameGenChangeLog;
qualProcNameGenChangeLog = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnSpGenChangelog, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "generate Change Log for propagated records", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameGenChangeLog + "(?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCountCLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "opId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "setProductiveTs_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "propagate records", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNamePropagate + "(psOid_in," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "," + M04_Utilities.genPoolId(srcPoolIndex, ddlType) + "," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "," + M04_Utilities.genPoolId(dstPoolIndex, null) + ",v_numSuccess,v_numFail);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = v_numSuccess;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ) THEN");

M11_LRT.genProcSectionHeader(fileNo, "generate Change Log for propagated records", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameGenChangeLog + "(?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCountCLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "opId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "setProductiveTs_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "propagate records", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcNameInvPropagate + "(psOid_in," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "," + M04_Utilities.genPoolId(srcPoolIndex, ddlType) + "," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "," + M04_Utilities.genPoolId(dstPoolIndex, null) + ",setProductiveTs_in,v_numSuccess,v_numFail);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = v_numSuccess;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
} else {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

if ((!(ignoreForChangelog & ! forNl)) |  (M03_Config.maintainGroupIdColumnsInSetProductive &  hasGroupIdAttrs)) {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", null, null, null);
}
if (!(ignoreForChangelog & ! forNl)) {
M11_LRT.genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0", null, null);
}
if (M03_Config.maintainGroupIdColumnsInSetProductive &  hasGroupIdAttrs) {
M11_LRT.genVarDecl(fileNo, "v_gidColCount", "INTEGER", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_gidValCount", "BIGINT", null, null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

if (!(forNl &  hasNlAttributes)) {
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, srcPoolIndex, ddlType, 1, null, null, null, null, null, null, null);
}

M86_SetProductive.genDdlForTempTablesSp(fileNo, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

if (!(ignoreForChangelog & ! forNl)) {
String qualCalledProcedureName;
qualCalledProcedureName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnSpGenChangelog, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "generate Change Log", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualCalledProcedureName + "(?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_rowCountCLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "opId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "setProductiveTs_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in <> " + String.valueOf(M11_LRT.lrtStatusLocked) + " ) THEN");
M11_LRT.genProcSectionHeader(fileNo, "generate Change Log", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualCalledProcedureName + "(?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCountCLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "opId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "setProductiveTs_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}
}
M11_LRT.genProcSectionHeader(fileNo, "execute requested operation", null, null);
if (forNl | ! hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + " ) THEN");

M12_ChangeLog.genAddNlTextChangeLogDdlForIndividualAttrs(fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, M01_Globals.gc_tempTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLogNl, qualTabNameSrcNl, M04_Utilities.genSurrogateKeyName(ddlType, acmEntityShortName, null, null, null, null), qualTabNameAggHeadNl, M04_Utilities.genSurrogateKeyName(ddlType, aggHeadShortClassName, null, null, null, null), attrRefs, relRefs, forGen, "", "psOid_in", thisOrgIndex, srcPoolIndex, false, true, ddlType, 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");
}

if (M03_Config.maintainGroupIdColumnsInSetProductive &  hasGroupIdAttrs) {
M11_LRT.genProcSectionHeader(fileNo, "determine group IDs for new records in Work Data Pool", 2, true);

String unqualSourceTabName;
unqualSourceTabName = M04_Utilities.getUnqualObjName(qualTabNameSrc);

String qualProcNameGaSync;
qualProcNameGaSync = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnGroupIdSync, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameGaSync + "(?," + (M03_Config.disableLoggingDuringSync ? "0," : "") + "?,?)';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_gidColCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_gidValCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "INSERT: propagate all records marked as 'created' in the work data pool to tables of the target data pool", 2, !((M03_Config.maintainGroupIdColumnsInSetProductive &  hasGroupIdAttrs)));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTgt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, "WORK.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusProductive), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conVersionId, "WORK." + M01_Globals.g_anVersionId + " + 1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conIsDeleted, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateTimestamp, "setProductiveTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLastUpdateTimestamp, "setProductiveTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrc + " WORK");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.oid = WORK." + M01_Globals.g_anOid);

if ((isPsTagged |  navToDivRelRefIndex > 0) &  forNl) {
// if we are processing an NL-Text table we need to navigate to parent table
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrcPar + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PAR." + M01_Globals.g_anOid + " = WORK." + attrNameFkEntity);
} else if (!(isPsTagged &  navRefClassIndex > 0)) {
// if we need to navigate to a class first before following a relation, do so
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamenNavRef + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PAR." + M01_Globals.g_anOid + " = WORK." + M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null));
}

// alternative navigation to Division if we cannot navigate to ProductStructure
if (!(isPsTagged &  navToDivRelRefIndex > 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + (forNl |  navRefClassIndex > 0 ? "PAR." : "WORK.") + fkAttrToDiv + " = PS." + psFkAttrToDiv);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + (forNl ? "PAR." : "WORK.") + M01_Globals_IVK.g_anPsOid + " = psOid_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(1=1)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- UPDATE: propagate all records marked as 'changed' in the work data pool to tables of the target data pool");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTgt + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, dstPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, "WORK.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusProductive), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conVersionId, "WORK." + M01_ACM.conVersionId + " + 1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateUser, "PROD." + M01_Globals.g_anCreateUser, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conCreateTimestamp, "PROD." + M01_Globals.g_anCreateTimestamp, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conLastUpdateTimestamp, "setProductiveTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, dstPoolIndex, 4, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 4, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameSrc + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + M01_Globals.g_anOid + " = PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROD." + M01_Globals.g_anOid + " IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameSrc + " WORK");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.oid = WORK." + M01_Globals.g_anOid);

if ((isPsTagged |  navToDivRelRefIndex > 0) &  forNl) {
// if we are processing an NL-Text table we need to navigate to parent table
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameSrcPar + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PAR." + M01_Globals.g_anOid + " = WORK." + attrNameFkEntity);
} else if (!(isPsTagged &  navRefClassIndex > 0)) {
// if we need to navigate to a class first before following a relation, do so
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNamenNavRef + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PAR." + M01_Globals.g_anOid + " = WORK." + M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null));
}

// alternative navigation to Division if we cannot navigate to ProductStructure
if (!(isPsTagged &  navToDivRelRefIndex > 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + (forNl |  navRefClassIndex > 0 ? "PAR." : "WORK.") + fkAttrToDiv + " = PS." + psFkAttrToDiv);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PROD." + M01_Globals.g_anOid + " = WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + M01_Globals_IVK.g_anHasBeenSetProductive + " = " + M01_LDM.gc_dbTrue);
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + (forNl ? "PAR." : "WORK.") + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " ) THEN");
M11_LRT.genProcSectionHeader(fileNo, "DELETE: delete all 'deleted' records in the target data pool", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTgt + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameSrc + " WORK");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E.oid = WORK." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PROD." + M01_Globals.g_anOid + " = WORK." + M01_Globals.g_anOid);
if (isPsTagged) {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + attrNameFkEntity + " IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "( SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameSrcPar + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in )");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WORK." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
} else {
if (navToDivRelRefIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");

if (forNl |  navRefClassIndex > 0) {
// need to navigate to parent to find the reference to Division
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "PAR." + fkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + (forNl ? qualTabNameSrcPar : qualTabNamenNavRef) + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "PAR." + M01_Globals.g_anOid + " = PROD." + M04_Utilities.genSurrogateKeyName(ddlType, (forNl ? acmEntityShortName : navRefClassShortName), null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "=");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + fkAttrToDiv + " =");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + psFkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");

}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "lrtOid_in", "opId_in", "#setProductiveTs_in", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for 'post-processing' data in Work Data Pool after 'setProductive' for given class / relationship
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnSetProductivePostProcess, null, null, null, null);

M22_Class_Utilities.printSectionHeader("post-processing' data in Work Data Pool after 'setProductive' for \"" + qualTabNameSrc + "\" (" + entityTypeDescr + " \"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + acmEntityName + "\"" + (forGen ? "(GEN)" : "") + ")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to process");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "LRT-OID - if NOT NULL only process prices related to this LRT");
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, false, "identifies the operation (insert, update or delete) to process");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M86_SetProductive.genDdlForTempTablesSp(fileNo, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "opId_in", null, null, null, null, null, null, null, null, null, null);


if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");

M11_LRT.genProcSectionHeader(fileNo, "mark all 'new' records of the work data pool as 'being productive'", 2, true);

int i;
for (int i = 1; i <= M00_Helper.uBound(aggChildClassIndexes); i++) {
if (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex == M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].orMappingSuperClassIndex) {
M00_FileWriter.printToFile(fileNo, "");

int j;
for (int j = 1; j <= (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].isGenForming & ! M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].hasNoIdentity ? 2 : 1); j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, srcPoolIndex, (j == 2), null, null, null, null, null, null) + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anHasBeenSetProductive + " = 1,");
if (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals.g_anStatus + " = 5,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals.g_anVersionId + " = S." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anHasBeenSetProductive + " = " + M01_LDM.gc_dbFalse);
if (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, dstPoolIndex, (j == 2), null, null, null, null, null, null) + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "S." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "T." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, dstPoolIndex, (j == 2), null, null, null, null, null, null) + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anHasBeenSetProductive + " = 1,");
if (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].classIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals.g_anStatus + " = 5,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals.g_anVersionId + " = S." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anHasBeenSetProductive + " = " + M01_LDM.gc_dbFalse);
if (M22_Class.g_classes.descriptors[aggChildClassIndexes[i]].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

}

}
}

} else {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");

M11_LRT.genProcSectionHeader(fileNo, "mark all 'new' records of the work data pool as 'being productive'", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrc + " UPD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals_IVK.g_anHasBeenSetProductive + " = 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusProductive) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid = UPD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

if (isPsTagged) {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + attrNameFkEntity + " IN ( SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameSrcPar + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in )");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
} else {
if (navToDivRelRefIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

if (forNl |  navRefClassIndex > 0) {
// need to navigate to parent to find the reference to Division
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + fkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + (forNl ? qualTabNameSrcPar : qualTabNamenNavRef) + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + M01_Globals.g_anOid + " = UPD." + M04_Utilities.genSurrogateKeyName(ddlType, (forNl ? acmEntityShortName : navRefClassShortName), null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + fkAttrToDiv + " =");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + psFkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ) THEN");

M11_LRT.genProcSectionHeader(fileNo, "mark all 'changed' records of the work data pool as 'being productive'", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrc + " UPD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusProductive) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid = UPD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

if (isPsTagged) {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + attrNameFkEntity + " IN ( SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameSrcPar + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in )");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPD." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
} else {
if (navToDivRelRefIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

if (forNl |  navRefClassIndex > 0) {
// need to navigate to parent to find the reference to Division
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + fkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + (forNl ? qualTabNameSrcPar : qualTabNamenNavRef) + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + M01_Globals.g_anOid + " = UPD." + M04_Utilities.genSurrogateKeyName(ddlType, (forNl ? acmEntityShortName : navRefClassShortName), null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + fkAttrToDiv + " =");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + psFkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " ) THEN");

M11_LRT.genProcSectionHeader(fileNo, "delete all 'deleted' records in the work data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrc + " DEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DEL." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.oid = DEL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

if (isPsTagged) {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DEL." + attrNameFkEntity + " IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "( SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameSrcPar + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in )");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DEL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
} else {
if (navToDivRelRefIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

if (forNl |  navRefClassIndex > 0) {
// need to navigate to parent to find the reference to Division
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + fkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + (forNl ? qualTabNameSrcPar : qualTabNamenNavRef) + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PAR." + M01_Globals.g_anOid + " = DEL." + M04_Utilities.genSurrogateKeyName(ddlType, (forNl ? acmEntityShortName : navRefClassShortName), null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + fkAttrToDiv + " =");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + psFkAttrToDiv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "opId_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (!(ignoreForChangelog & ! forNl)) {
// we handle ChangeLog only in the non-NL-case; NL-tables are handled inside
M12_ChangeLog.genChangeLogSupportForEntity(acmEntityIndex, acmEntityType, relRefs, qualTabNameSrc, qualTabNameSrcNl, qualTabNameTgt, qualTabNameTgtNl, qualTabNameSrcGen, qualTabNameAggHeadNl, qualTabNameAggHead, thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl, M12_ChangeLog.ChangeLogMode.eclSetProd);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
}


public static void genSetProdSupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int dstPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW) {
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

M86_SetProductive.genSetProdSupportSpsForEntity(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, null);

if ((forGen ? M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInNonGenInclSubClasses)) {
M86_SetProductive.genSetProdSupportSpsForEntity(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, true);
}
}


public static void genSetProdSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int dstPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW) {
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

M86_SetProductive.genSetProdSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M86_SetProductive.genSetProdSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, dstPoolIndex, fileNo, fileNoClView, ddlType, forGen, true);
}
}

// ### ENDIF IVK ###





}