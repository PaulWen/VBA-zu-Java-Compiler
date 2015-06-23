package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M11_LRT {





private static final String pc_tempTabNamePubOidsAffected = "SESSION.PubOidsAffected";
private static final String pc_tempTabNamePubOidsAffectedNl = "SESSION.PubOidsAffectedNl";

public static final int lrtStatusLocked = 0;
public static final int lrtStatusCreated = 1;
public static final int lrtStatusUpdated = 2;
public static final int lrtStatusDeleted = 3;
public static final int lrtStatusMassDeleted = 4;
public static final int lrtStatusNonLrtCreated = 5;

public static final int workingStateUnlocked = 1;
public static final int workingLockedByOtherUser = 2;
public static final int workingLockedInActiveTransaction = 3;
public static final int workingLockedInInactiveTransaction = 4;

private static final int processingStep = 1;
private static final int attrListAlign = 40;

public static final String tempTabNameLrtLog = "SESSION.LrtLog";

public static void genLrtSupportDdl(Integer ddlType) {
int i;
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
// ### IF IVK ###
genLrtSupportDdlByType(M01_Common.DdlTypeId.edtLdm);

// ### ENDIF IVK ###
genLrtSupportDdlByPool(null, null, null);
// ### IF IVK ###
genLrtSpSupportDdlByPool(null, null, null);
// ### ENDIF IVK ###
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
// ### IF IVK ###
genLrtSupportDdlByType(M01_Common.DdlTypeId.edtPdm);
// ### ENDIF IVK ###

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genLrtSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
// ### IF IVK ###
genLrtSpSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
// ### ENDIF IVK ###
}
}
}
}
}
}
// ### IF IVK ###


private static void genLrtSupportDdlByType(Integer ddlTypeW) {
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
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, null, null, null, M01_Common.phaseLrt, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    create user defined function determining target status of records after LRT-Commit
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("UDF for determining target status of records after LRT-Commit", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "entityId_in", M01_Globals.g_dbtEntityId, true, "ACM entity ID");
M11_LRT.genProcParm(fileNo, "", "entityType_in", M01_Globals.g_dbtEntityType, true, "ACM entity type");
M11_LRT.genProcParm(fileNo, "", "settingManActCP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateCodePrice'");
M11_LRT.genProcParm(fileNo, "", "settingManActTP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateTypePrice'");
M11_LRT.genProcParm(fileNo, "", "settingManActSE_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateStandardEquipmentPrice'");
M11_LRT.genProcParm(fileNo, "", "settingSelRelease_in", M01_Globals.g_dbtBoolean, false, "setting 'useSelectiveReleaseProcess'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtEnumId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtActivationType", "VARCHAR(2)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine LRT activation type (CP,TP,SE,GA,DT,NP,NULL)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtActivationType =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anAcmLrtActivationType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anAcmEntityId + " = entityId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anAcmEntityType + " = entityType_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M11_LRT.genProcSectionHeader(fileNo, "return result", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE v_lrtActivationType");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'CP' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'Code Price'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingManActCP_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForActivation));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingSelRelease_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForRelease));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'TP' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'Type Price'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingManActTP_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForActivation));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingSelRelease_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForRelease));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'SE' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'Standard Equipment'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingManActSE_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForActivation));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingSelRelease_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForRelease));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'GA' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'GenericAspect'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingSelRelease_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForRelease));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'DT' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'DecisionTable'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "WHEN settingSelRelease_in = 1 THEN " + String.valueOf(M86_SetProductive.statusReadyForRelease));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 'NP' THEN");
M11_LRT.genProcSectionHeader(fileNo, "target status for 'no transfer to production'", 5, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + String.valueOf(M86_SetProductive.statusWorkInProgress));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

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

public static void genDdlForTempPrivClassIdOid(int fileNo) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLASSID CHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID BIGINT NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON COMMIT PRESERVE ROWS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED;");

}


public static void genDdlPdmEntityCheck(int fileNo, int indent, String prefix) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefix + "ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefix + "ENTITY_ISLRT = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefix + "LDM_ISGEN = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefix + "LDM_ISLRT = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefix + "LDM_ISNL = " + M01_LDM.gc_dbFalse);

}

public static void genDdlPsDivClause(int fileNo, int indent, String prefixLeft,  String prefixRightPs,  String prefixRightDiv, boolean isPsTagged, boolean M03_Config.usePsTagInNlTextTables, boolean forNl, boolean useDivOidWhereClause, boolean useDivRelKey, Boolean useForAggHeadJoinW) {
boolean useForAggHeadJoin; 
if (useForAggHeadJoinW == null) {
useForAggHeadJoin = false;
} else {
useForAggHeadJoin = useForAggHeadJoinW;
}

if (!(prefixLeft.compareTo("") == 0)) {
prefixLeft = prefixLeft + ;
}
if (!(prefixRightPs.compareTo("") == 0)) {
prefixRightPs = prefixRightPs + ;
}
if (!(prefixRightDiv.compareTo("") == 0)) {
prefixRightDiv = prefixRightDiv + ;
}
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
if (prefixRightPs.compareTo("") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefixLeft + M01_ACM_IVK.conPsOid + " = v_psOid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefixLeft + M01_ACM_IVK.conPsOid + " = " + prefixRightPs + M01_ACM_IVK.conPsOid);
}
} else if (useDivOidWhereClause) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
if (useDivRelKey) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefixLeft + "CDIDIV_OID = " + prefixRightDiv + "CDIDIV_OID");
} else if (useForAggHeadJoin) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefixLeft + "CDIDIV_OID = " + prefixRightDiv + M01_ACM_IVK.conDivOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + prefixLeft + M01_ACM_IVK.conDivOid + " = " + prefixRightDiv + M01_ACM_IVK.conDivOid);
}
}
}

public static void genStatusCheckDdl(int fileNo, String recordVar, String statusAttrW, Integer indentW) {
String statusAttr; 
if (statusAttrW == null) {
statusAttr = "STATUS_ID";
} else {
statusAttr = statusAttrW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

if (!(M03_Config.generateStatusCheckDdl)) {
return;
}

M11_LRT.genProcSectionHeader(fileNo, "verify that new status is supported by the MDS status concept", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "IF ((" + recordVar + "." + statusAttr + " IS NOT NULL) AND (" + recordVar + "." + statusAttr + " NOT IN (" + String.valueOf(M86_SetProductive.statusWorkInProgress) + ", " + String.valueOf(M86_SetProductive.statusReadyForActivation) + ", " + String.valueOf(M86_SetProductive.statusReadyForRelease) + ", " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive) + "))) THEN");
M79_Err.genSignalDdl("attrVal4", fileNo, indent + 1, statusAttr, M86_SetProductive.statusWorkInProgress, M86_SetProductive.statusReadyForActivation, M86_SetProductive.statusReadyForRelease, M86_SetProductive.statusReadyToBeSetProductive, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}
// ### ENDIF IVK ###


public static void genDb2RegVarCheckDdl(int fileNo, Integer ddlType,  int thisOrgIndex,  int thisPoolIndex, Integer forLrtW, Integer indentW) {
Integer forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

if (!(M03_Config.generateDb2RegistryCheckInSps)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
return;
}

if (thisOrgIndex < 1 |  thisPoolIndex < 1) {
// we do not check DB2 register 'outside of DataPools'
return;
}

if (thisPoolIndex > 0) {
// ### IF IVK ###
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
return;
}
// ### ENDIF IVK ###
if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
forLrt = M01_Common.TvBoolean.tvFalse;
}
}

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnCheckDb2Register, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify that DB2-Register are used consistently", indent, null);

if (M03_Config.supportSpLogging) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF COALESCE(RIGHT(" + M01_LDM.gc_db2RegVarCtrl + ",1), '') = '1' THEN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF COALESCE(" + M01_LDM.gc_db2RegVarCtrl + ", '') = '1' THEN");
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CALL " + qualProcName + "(" + M01_LDM.gc_db2RegVarLrtOid + ", " + M01_Globals_IVK.gc_db2RegVarPsOid + ", " + M01_LDM.gc_db2RegVarSchema + ", " + (forLrt == M01_Common.TvBoolean.tvTrue ? M01_LDM.gc_dbTrue : (forLrt == M01_Common.TvBoolean.tvFalse ? M01_LDM.gc_dbFalse : "NULL")) + ");");
// ### ELSE IVK ###
// Print #fileNo, addTab(indent + 1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarSchema; ", "; IIf(forLrt = tvTrue, gc_dbTrue, IIf(forLrt = tvFalse, gc_dbFalse, "NULL")); ");"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}


// ### IF IVK ###
public static void genPsCheckDdlForInsertDelete(int fileNo, String qualAttrName, Integer ddlType,  int thisOrgIndex, Boolean ignorePsRegVarW, Boolean psTagOptionalW, Integer indentW, Boolean genHeaderW, String psOidRecordW, String psOidRegVarW, String psOidEffectiveW, Boolean forInsertW, String refTabW, String refOidW) {
boolean ignorePsRegVar; 
if (ignorePsRegVarW == null) {
ignorePsRegVar = false;
} else {
ignorePsRegVar = ignorePsRegVarW;
}

boolean psTagOptional; 
if (psTagOptionalW == null) {
psTagOptional = false;
} else {
psTagOptional = psTagOptionalW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean genHeader; 
if (genHeaderW == null) {
genHeader = true;
} else {
genHeader = genHeaderW;
}

String psOidRecord; 
if (psOidRecordW == null) {
psOidRecord = "v_psOidRecord";
} else {
psOidRecord = psOidRecordW;
}

String psOidRegVar; 
if (psOidRegVarW == null) {
psOidRegVar = "v_psOidRegVar";
} else {
psOidRegVar = psOidRegVarW;
}

String psOidEffective; 
if (psOidEffectiveW == null) {
psOidEffective = "v_psOid";
} else {
psOidEffective = psOidEffectiveW;
}

boolean forInsert; 
if (forInsertW == null) {
forInsert = false;
} else {
forInsert = forInsertW;
}

String refTab; 
if (refTabW == null) {
refTab = "";
} else {
refTab = refTabW;
}

String refOid; 
if (refOidW == null) {
refOid = "";
} else {
refOid = refOidW;
}

if (genHeader) {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent, null);
}
M11_LRT.genVarDecl(fileNo, psOidRecord, M01_Globals.g_dbtOid, "NULL", indent, null);
if (forInsert |  (!(ignorePsRegVar))) {
M11_LRT.genVarDecl(fileNo, psOidRegVar, M01_Globals.g_dbtOid, "NULL", indent, null);
}
if (psOidEffective + "" != "") {
M11_LRT.genVarDecl(fileNo, psOidEffective, M01_Globals.g_dbtOid, "NULL", indent, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidRecord + " = " + qualAttrName + ";");

if (forInsert |  (!(ignorePsRegVar))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidRegVar + " = (CASE WHEN " + M01_Globals_IVK.gc_db2RegVarPsOid + " IN ('','0') THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals_IVK.g_activePsOidDdl + " END);");
}

if (psOidEffective + "" != "") {
if (ignorePsRegVar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidEffective + " = COALESCE(" + psOidRecord + ", " + psOidRegVar + ");");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidEffective + " = COALESCE(" + psOidRegVar + ", " + psOidRecord + ");");
}
}

M11_LRT.genProcSectionHeader(fileNo, "verify that the PS-tag is used consistently", indent, null);
if (ignorePsRegVar) {
if (!(psTagOptional)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF " + (psOidEffective + "" != "" ? psOidEffective : psOidRecord) + " IS NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is not specified in record return with error", indent + 1, true);
M79_Err.genSignalDdl("noPs", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}
} else {
int indent2;
indent2 = 0;
if (psTagOptional) {
indent2 = -1;
}

if (!(psTagOptional)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF (" + psOidRecord + " IS NULL AND " + psOidRegVar + " IS NULL) THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + 1, true);
M79_Err.genSignalDdl("noPs", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ELSE");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + indent2 + 1) + "IF ((" + psOidRecord + " IS NOT NULL) AND (" + psOidRegVar + " IS NOT NULL) AND (" + psOidRecord + " <> " + psOidRegVar + ")) THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + indent2 + 2, true);

M79_Err.genSignalDdlWithParms("incorrPsTagExtended", fileNo, indent + indent2 + 2, (refTab == "" ? "" : refTab), null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + psOidRecord + "))", "RTRIM(CHAR(" + psOidRegVar + "))", (refOid == "" ? "" : "RTRIM(CHAR(" + refOid + "))"), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + indent2 + 1) + "END IF;");
if (!(psTagOptional)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}
}
}


public static void genPsCheckDdlForUpdate(int fileNo, String qualAttrNameOld, String qualAttrNameNew, Integer ddlType,  int thisOrgIndex, Boolean psTagOptionalW, Integer indentW, Boolean genHeaderW, String psOidRecordNewW, String psOidRegVarW, String psOidEffectiveW, String refTabW, String refOidW) {
boolean psTagOptional; 
if (psTagOptionalW == null) {
psTagOptional = false;
} else {
psTagOptional = psTagOptionalW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean genHeader; 
if (genHeaderW == null) {
genHeader = true;
} else {
genHeader = genHeaderW;
}

String psOidRecordNew; 
if (psOidRecordNewW == null) {
psOidRecordNew = "v_psOidRecord";
} else {
psOidRecordNew = psOidRecordNewW;
}

String psOidRegVar; 
if (psOidRegVarW == null) {
psOidRegVar = "v_psOidRegVar";
} else {
psOidRegVar = psOidRegVarW;
}

String psOidEffective; 
if (psOidEffectiveW == null) {
psOidEffective = "v_psOid";
} else {
psOidEffective = psOidEffectiveW;
}

String refTab; 
if (refTabW == null) {
refTab = "";
} else {
refTab = refTabW;
}

String refOid; 
if (refOidW == null) {
refOid = "";
} else {
refOid = refOidW;
}

if (genHeader) {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent, null);
}
M11_LRT.genVarDecl(fileNo, psOidRecordNew, M01_Globals.g_dbtOid, "NULL", indent, null);
M11_LRT.genVarDecl(fileNo, psOidRegVar, M01_Globals.g_dbtOid, "NULL", indent, null);
if (psOidEffective + "" != "") {
M11_LRT.genVarDecl(fileNo, psOidEffective, M01_Globals.g_dbtOid, "NULL", indent, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidRecordNew + " = COALESCE(" + qualAttrNameNew + ", " + qualAttrNameOld + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidRegVar + " = (CASE WHEN " + M01_Globals_IVK.gc_db2RegVarPsOid + " IN ('','0') THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals_IVK.g_activePsOidDdl + " END);");

if (psOidEffective + "" != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidEffective + " = COALESCE(" + psOidRegVar + ", " + psOidRecordNew + ");");
}

M11_LRT.genProcSectionHeader(fileNo, "verify that the PS-tag is used consistently", indent, null);
int indent2;
indent2 = 0;
if (psTagOptional) {
indent2 = -1;
}

if (!(psTagOptional)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF (" + psOidRecordNew + " IS NULL AND " + psOidRegVar + " IS NULL) THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + 1, true);
M79_Err.genSignalDdl("noPs", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ELSE");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + indent2 + 1) + "IF ((" + psOidRecordNew + " IS NOT NULL) AND (" + psOidRegVar + " IS NOT NULL) AND (" + psOidRecordNew + " <> " + psOidRegVar + ")) THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is specified inconsistently return with error", indent + indent2 + 2, true);

M79_Err.genSignalDdlWithParms("incorrPsTagExtended", fileNo, indent + indent2 + 2, (refTab == "" ? "" : refTab), null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + psOidRecordNew + "))", "RTRIM(CHAR(" + psOidRegVar + "))", (refOid == "" ? "" : "RTRIM(CHAR(" + refOid + "))"), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + indent2 + 1) + "END IF;");

if (!(psTagOptional)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}
}


public static void genPsCheckDdlForNonPsTaggedInLrt(int fileNo, Integer ddlType,  int thisOrgIndex, Integer indentW, Boolean genHeaderW, String psOidRegVarW, String psOidEffectiveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean genHeader; 
if (genHeaderW == null) {
genHeader = true;
} else {
genHeader = genHeaderW;
}

String psOidRegVar; 
if (psOidRegVarW == null) {
psOidRegVar = "v_psOidRegVar";
} else {
psOidRegVar = psOidRegVarW;
}

String psOidEffective; 
if (psOidEffectiveW == null) {
psOidEffective = "v_psOid";
} else {
psOidEffective = psOidEffectiveW;
}

if (genHeader) {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent, null);
}
M11_LRT.genVarDecl(fileNo, psOidRegVar, M01_Globals.g_dbtOid, "NULL", indent, null);
if (psOidEffective + "" != "") {
M11_LRT.genVarDecl(fileNo, psOidEffective, M01_Globals.g_dbtOid, "NULL", indent, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidRegVar + " = (CASE WHEN " + M01_Globals_IVK.gc_db2RegVarPsOid + " IN ('','0') THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals_IVK.g_activePsOidDdl + " END);");

if (psOidEffective + "" != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + psOidEffective + " = " + psOidRegVar + ";");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF " + psOidRegVar + " IS NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag is not specified in record return with error", indent + 1, true);
M79_Err.genSignalDdl("noPs", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}


// ### ENDIF IVK ###
public static void genVerifyActiveLrtDdl(int fileNo, Integer ddlType, String qualTabNameLrt, String lrtOidStr, Integer indentW, Boolean skipNlW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

M11_LRT.genProcSectionHeader(fileNo, "verify that current LRT is (still) active", indent + 0, skipNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_lrtClosed = (SELECT (CASE WHEN " + M01_Globals.g_anEndTime + " IS NULL THEN " + M01_LDM.gc_dbFalse + " ELSE " + M01_LDM.gc_dbTrue + " END) FROM " + qualTabNameLrt + " WHERE " + M01_Globals.g_anOid + " = " + lrtOidStr + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF v_lrtClosed = " + M01_LDM.gc_dbTrue + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT is already closed", indent + 1, true);
M79_Err.genSignalDdl("lrtClosed", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ELSEIF v_lrtClosed IS NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not exist", indent + 1, true);
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, indent + 1, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidStr + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}


public static void genAggHeadLockPropDdl(int fileNo, String recordName, int ahClassIndex, String qualAggHeadTabName, String qualAggHeadLrtTabName, String qualTabNameLrtAffectedEntity, String varNameCdUserId, Integer ddlType,  int thisOrgIndex,  int thisPoolIndex, Integer indentW, Boolean usePsOidWhereClauseW, Boolean useDivOidWhereClauseW, Boolean useDivRelKeyW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean usePsOidWhereClause; 
if (usePsOidWhereClauseW == null) {
usePsOidWhereClause = false;
} else {
usePsOidWhereClause = usePsOidWhereClauseW;
}

boolean useDivOidWhereClause; 
if (useDivOidWhereClauseW == null) {
useDivOidWhereClause = false;
} else {
useDivOidWhereClause = useDivOidWhereClauseW;
}

boolean useDivRelKey; 
if (useDivRelKeyW == null) {
useDivRelKey = false;
} else {
useDivRelKey = useDivRelKeyW;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[ahClassIndex].condenseData) {
return;
}

// ### ENDIF IVK ###
String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "lock the 'public aggregate head record' with this LRT-OID", indent + 0, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF " + recordName + "." + M01_Globals.g_anAhOid + " IS NOT NULL THEN");

M11_LRT.genProcSectionHeader(fileNo, "since DB2 applies some restrictions on 'table access contexts'", indent + 1, true);
M11_LRT.genProcSectionHeader(fileNo, "it is NOT possible to call LRTLOCK here", indent + 1, true);

M11_LRT.genProcSectionHeader(fileNo, "determine OID of LRT owning a lock on aggregate head", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_inLrt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "PUB." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + qualAggHeadTabName + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "PUB." + M01_Globals.g_anOid + " = " + recordName + "." + M01_Globals.g_anAhOid);
//add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
if (!(((ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) &  usePsOidWhereClause))) {
M11_LRT.genDdlPsDivClause(fileNo, indent + 4, "PUB", recordName, recordName, usePsOidWhereClause, false, false, useDivOidWhereClause, useDivRelKey, true);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "check if aggregate head is locked by some LRT other than this one", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "IF (v_inLrt IS NOT NULL) AND (v_inLrt <> v_lrtOid) THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine ID of user holding the lock", indent + 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SET " + varNameCdUserId + " = (SELECT USR." + M01_Globals.g_anUserId + " FROM " + M01_Globals.g_qualTabNameUser + " USR INNER JOIN " + qualTabNameLrt + " LRT ON LRT.UTROWN_OID = USR." + M01_Globals.g_anOid + " WHERE LRT." + M01_Globals.g_anOid + " = v_inLrt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SET " + varNameCdUserId + " = COALESCE(" + varNameCdUserId + ", '<unknown>');");
M00_FileWriter.printToFile(fileNo, "");
M79_Err.genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, indent + 2, null, null, null, null, null, null, null, null, null, varNameCdUserId, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "IF v_inLrt IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "aggregate head is already locked by this transaction", indent + 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public aggregate head' into 'private table'", indent + 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualAggHeadLrtTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");

// ### IF IVK ###
M24_Attribute.genAttrListForEntity(ahClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, true, false, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
// ### ELSE IVK ###
//   genAttrListForEntity ahClassIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, True, False, edomListLrt
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, true, true, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//   initAttributeTransformation transformation, 2, , True, True
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "" + M11_LRT.lrtStatusLocked, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(ahClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, null, true, false, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
// ### ELSE IVK ###
//   genTransformedAttrListForEntity ahClassIndex, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, , True, False, edomListLrt
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualAggHeadTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anOid + " = " + recordName + "." + M01_Globals.g_anAhOid);
//add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
if (!(((ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) &  usePsOidWhereClause))) {
M11_LRT.genDdlPsDivClause(fileNo, indent + 3, "", recordName, recordName, usePsOidWhereClause, false, false, useDivOidWhereClause, useDivRelKey, true);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "lock the 'public aggregate head' with this LRT-OID", indent + 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualAggHeadTabName + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PUB." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PUB." + M01_Globals.g_anOid + " = " + recordName + "." + M01_Globals.g_anAhOid);
//add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
if (!(((ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) &  usePsOidWhereClause))) {
M11_LRT.genDdlPsDivClause(fileNo, indent + 3, "PUB", recordName, recordName, usePsOidWhereClause, false, false, useDivOidWhereClause, useDivRelKey, true);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ";");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, "aggregate head", M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.gc_acmEntityTypeKeyClass, false, false, qualTabNameLrtAffectedEntity, M22_Class.getClassIdStrByIndex(ahClassIndex), M22_Class.getClassIdStrByIndex(ahClassIndex), "v_lrtOid", indent + 2, String.valueOf(M11_LRT.lrtStatusLocked), false);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}


public static void genProcParm(int fileNo, String mode, String name, String dbType, Boolean addCommaW, String commentW) {
boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String comma;
comma = (addComma ? "," : "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (!(mode.compareTo("") == 0) ? mode + "   ".substring(0, M01_LDM.gc_sqlMaxParmNameLength) + " " : ""));
M00_FileWriter.printToFile(fileNo, name + "                           ".substring(0, M01_LDM.gc_sqlMaxVarNameLength) + " ");
M00_FileWriter.printToFile(fileNo, (comment.compareTo("") == 0 ? dbType + comma : (dbType.length() >= M01_LDM.gc_sqlMaxVarTypeLength ? dbType + comma : dbType + comma + "                        ".substring(0, M01_LDM.gc_sqlMaxVarTypeLength)) + " -- " + comment));
}


public static void genVarDecl(int fileNo, String varName, String dbType, String defaultValueW, Integer indentW, String commentW) {
String defaultValue; 
if (defaultValueW == null) {
defaultValue = "";
} else {
defaultValue = defaultValueW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "DECLARE " + (varName.length() > M01_LDM.gc_sqlMaxVarNameLength ? varName : varName + "                           ".substring(0, M01_LDM.gc_sqlMaxVarNameLength)) + " " + (defaultValue.compareTo("") == 0 ? dbType + ";" : (dbType.length() >= M01_LDM.gc_sqlMaxVarTypeLength ? dbType : dbType + "                        ".substring(0, M01_LDM.gc_sqlMaxVarTypeLength)) + " " + "DEFAULT " + defaultValue + ";") + (comment.compareTo("") == 0 ? "" : " -- " + comment));
}


public static void genCondDecl(int fileNo, String condName, String sqlState, Integer indentW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "DECLARE " + condName + "                           ".substring(0, M01_LDM.gc_sqlMaxVarNameLength) + " " + "CONDITION FOR SQLSTATE '" + sqlState + "';");
}

public static void genProcSectionHeader(int fileNo, String header, Integer indentW, Boolean skipNlW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

if (!(skipNl)) {
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + header);
}

public static void genDdlForUpdateAffectedEntities(int fileNo, String entityTypeDescr, Integer acmEntityType, String dbAcmEntityType, boolean isGen,  boolean isNl, String qualTabNameLrtAffectedEntity, String entityIdStr, String ahClassIdStr, String lrtOidStr, Integer indentW, String opW, Boolean propagateToAhW) {
int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

String op; 
if (opW == null) {
op = "v_lrtExecutedOperation";
} else {
op = opW;
}

boolean propagateToAh; 
if (propagateToAhW == null) {
propagateToAh = true;
} else {
propagateToAh = propagateToAhW;
}


M11_LRT.genProcSectionHeader(fileNo, "register that this " + entityTypeDescr + " is affected by this LRT", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_lrtEntityIdCount =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLrtOid + " = " + lrtOidStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anAcmOrParEntityId + " = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anAcmEntityType + " = '" + dbAcmEntityType + "'");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLdmIsGen + " = " + String.valueOf((isGen ? 1 : 0)));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLdmIsNl + " = " + String.valueOf((isNl ? 1 : 0)));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLrtOpId + " = " + op);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF v_lrtEntityIdCount = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anAcmEntityType + ",");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLdmIsNl + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLrtOpId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + lrtOidStr + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + entityIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + dbAcmEntityType + "',");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + String.valueOf((isGen ? 1 : 0)) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + String.valueOf((isNl ? 1 : 0)) + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + op);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");

if (propagateToAh &  ((acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) |  (!(entityIdStr.compareTo(ahClassIdStr) == 0)))) {
M11_LRT.genProcSectionHeader(fileNo, "register that aggregate head is affected (locked) by this LRT", indent, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_lrtEntityIdCount =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLrtOid + " = " + lrtOidStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anAcmOrParEntityId + " = '" + ahClassIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anAcmEntityType + " = '" + M04_Utilities.getAcmEntityTypeKey(M24_Attribute_Utilities.AcmAttrContainerType.eactClass) + "'");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLrtOpId + " = " + String.valueOf(M11_LRT.lrtStatusLocked));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF v_lrtEntityIdCount = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anAcmEntityType + ",");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLdmIsNl + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anLrtOpId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + lrtOidStr + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + ahClassIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals.gc_acmEntityTypeKeyClass + "',");
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "0,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + String.valueOf(M11_LRT.lrtStatusLocked));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}
}


public static void genDdlForUpdateLrtLastOpTs(int fileNo,  int thisOrgIndex,  int thisPoolIndex, String lrtOidStr, String timestampStrW, Integer ddlTypeW, Integer indentW) {
String timestampStr; 
if (timestampStrW == null) {
timestampStr = "CURRENT TIMESTAMP";
} else {
timestampStr = timestampStrW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String qualTabNameLrtExecStatus;
qualTabNameLrtExecStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "record LRT's last update timestamp", indent + 0, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameLrtExecStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLastOpTime + " = " + timestampStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLrtOid + " = " + lrtOidStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
}


public static void genDdlForTempLrtLog(int fileNo, Integer indentW, Boolean restrictColSetW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean restrictColSet; 
if (restrictColSetW == null) {
restrictColSet = false;
} else {
restrictColSet = restrictColSetW;
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for LRT-Log", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
if (!(restrictColSet)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "displayMe           " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orParEntityId       " + M01_Globals.g_dbtEntityId + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityId            " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityType          " + M01_Globals.g_dbtEntityType + ",");
if (!(restrictColSet)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityName          VARCHAR(60),");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "displayCategory     CHAR(1),");
// ### ENDIF IVK ###
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "gen                 " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isNl                " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid                 " + M01_Globals.g_dbtOid + ",");
if (!(restrictColSet)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId1         " + M01_Globals.g_dbtEntityId + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId1        " + M01_Globals.g_dbtOid + ",");
if (!(restrictColSet)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId2         " + M01_Globals.g_dbtEntityId + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId2        " + M01_Globals.g_dbtOid + ",");
if (!(restrictColSet)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "label               " + M01_Globals_IVK.g_dbtLrtLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "comment             " + M01_Globals_IVK.g_dbtChangeComment + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "code                " + M01_Globals_IVK.g_dbtCodeNumber + ",");
if (!(restrictColSet)) {
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Context          VARCHAR(159),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code1            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code2            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code3            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code4            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code5            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code6            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code7            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code8            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code9            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code10           " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseCode            " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseEndSlot         " + M25_Domain.getDbDataTypeByDomainName(M01_ACM_IVK.dxnEndSlotLabel, M01_ACM_IVK.dnEndSlotLabel) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "t_baseEndSlotGenOid " + M01_Globals.g_dbtOid + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validFrom           DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validTo             DATE,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "operation           " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ts                  TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, null, null);
}


public static void genDdlForTempTableDeclTrailer(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
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

if (onCommitPreserve) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON COMMIT PRESERVE ROWS");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NOT LOGGED");

if (onRollbackPreserve) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON ROLLBACK PRESERVE ROWS");
}

if (withReplace) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH REPLACE");
}

M00_FileWriter.printToFile(fileNo, ";");
}


private static void genLrtSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrt, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtNlText;
qualTabNameLrtNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLdmLrt;
qualTabNameLdmLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

boolean lrtClassUseSurrogateKey;
lrtClassUseSurrogateKey = M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].useSurrogateKey;

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtExecStatus;
qualTabNameLrtExecStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ### IF IVK ###
if (!(M03_Config.generateFwkTest)) {
String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameConflict;
qualTabNameConflict = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
}

// ### ENDIF IVK ###

String qualViewName;
String qualViewNameLdm;
qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M01_ACM.vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M01_ACM.vsnLrtAffectedLdmTab, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);

genLrtSupportDdlByPool0(fileNo, thisOrgIndex, thisPoolIndex, ddlType);

// just to initialize the variables

String qualNlTabName;
String qualNlTabNameLdm;
String nlObjName;
String nlObjNameShort;
nlObjName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, null, null, null);
nlObjNameShort = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].shortName, null, null, null);
qualNlTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);
qualNlTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);



// ####################################################################################################################
// #    create view to determine PDM tables involved in an LRT
// ####################################################################################################################

String qualViewNamePdmTabs;
String qualViewNamePdmTabsLdm;

qualViewNamePdmTabs = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedPdmTab, M01_ACM.vsnLrtAffectedPdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
qualViewNamePdmTabsLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedPdmTab, M01_ACM.vsnLrtAffectedPdmTab, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for all PDM-tables related to a specific LRT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmIgnoreForChangelog + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmUseLrtCommitPreprocess + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anAcmDisplayCategory + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anInLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL.OPID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anAcmIgnoreForChangelog + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anAcmUseLrtCommitPreprocess + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals_IVK.g_anAcmDisplayCategory + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL.SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewName + " AL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmTable + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkSchemaName + " = AL." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkTableName + " = AL." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### IF IVK ###
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedPdmTab, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCommonToPools, true, qualViewNamePdmTabsLdm, qualViewNamePdmTabs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "LRT-AFFECTED-PDM-TABLES View \"" + M01_ACM.snDbMeta + "." + M01_ACM.vnLrtAffectedPdmTab + "\"", null, true, null, null, null, null, null, null);
// ### ELSE IVK ###
//   genAliasDdlX g_sectionIndexLrt, vnLrtAffectedPdmTab, .isCommonToOrgs, .isCommonToPools, True, _
//     qualViewNamePdmTabsLdm, qualViewNamePdmTabs, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
//     "LRT-AFFECTED-PDM-TABLES View """ & snDbMeta & "." & vnLrtAffectedPdmTab & """", , True
// ### ENDIF IVK ###

// ####################################################################################################################
// #    SP for BEGIN of LRT
// ####################################################################################################################

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

boolean internalProcVersion;
String qualProcName;
// ### IFNOT IVK ###
int i;
// ### ENDIF IVK ###
for (int i = 1; i <= 2; i++) {
internalProcVersion = (i == 2);

qualProcName = M04_Utilities.genQualProcName((internalProcVersion ? M01_Globals.g_sectionIndexLrt : M01_Globals.g_sectionIndexAliasLrt), M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Stored Procedure for BEGIN of LRT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
if (internalProcVersion) {
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure we are working with");
}
M11_LRT.genProcParm(fileNo, "IN", "isCentralDataTransfer_in", M01_Globals.g_dbtBoolean, true, "logically 'boolean' (0 = false, 1 = true)");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtLrtId, false, "return value: OID of the created LRT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_now", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_userOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

// ### IF IVK ###
if (!(internalProcVersion)) {
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'cdUserId_in", "trNumber_in", (internalProcVersion ? "psOid_in" : ""), "isCentralDataTransfer_in", "lrtOid_out", null, null, null, null, null, null, null);
// ### ELSE IVK ###
//   genSpLogProcEnter fileNo, qualProcName, ddlType, , "'cdUserId_in", "lrtOid_out"
// ### ENDIF IVK ###

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "determine timestamp of LRT begin", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_now = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out = NULL;");

// ### IF IVK ###
if (!(internalProcVersion)) {
M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid = " + M01_Globals_IVK.g_activePsOidDdl + ";");
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine user's OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_userOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U." + M01_Globals.g_anUserId + " = cdUserId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that cdUserId_in identifies a valid user", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_userOid IS NULL) THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "cdUserId_in", "trNumber_in", (internalProcVersion ? "psOid_in" : ""), "isCentralDataTransfer_in", "lrtOid_out", null, null, null, null, null, null, null);
// ### ELSE IVK ###
//   genSpLogProcEscape fileNo, qualProcName, ddlType, , "'cdUserId_in", "lrtOid_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("userUnknown", fileNo, 2, null, null, null, null, null, null, null, null, null, "cdUserId_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
// ### IF IVK ###

M11_LRT.genProcSectionHeader(fileNo, "verify that this transaction has not ended", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.UTROWN_OID = v_userOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.TRNUMBER = trNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anPsOid + " = " + (internalProcVersion ? "psOid_in" : "v_psOid"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "if there is already an active transaction for this user with the same logical trNumber, we need to quit", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lrtCount > 0) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "cdUserId_in", "trNumber_in", (internalProcVersion ? "psOid_in" : ""), "isCentralDataTransfer_in", "lrtOid_out", null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("ltrAlreadyActive", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(trNumber_in))", "cdUserId_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "create and register new LRT-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
if (lrtClassUseSurrogateKey) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsCentralDataTransfer + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "STARTTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TRNUMBER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UTROWN_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
if (lrtClassUseSurrogateKey) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isCentralDataTransfer_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_now,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "trNumber_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_userOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (internalProcVersion ? "psOid_in" : "v_psOid") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtExecStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM.conLastOpTime);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_now");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

// ### IF IVK ###
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'cdUserId_in", "trNumber_in", (internalProcVersion ? "psOid_in" : ""), "isCentralDataTransfer_in", "lrtOid_out", null, null, null, null, null, null, null);
// ### ELSE IVK ###
//   genSpLogProcExit fileNo, qualProcName, ddlType, , "'cdUserId_in", "trNumber_in", "lrtOid_out"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    SP for LRT-(UN)LOCK
// ####################################################################################################################

String qualPdmTableViewName;
qualPdmTableViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnPdmTable, M01_ACM.vnsPdmTable, ddlType, null, null, null, null, null, null, null, null, null, null);

String qualFuncNameParseClassIdOidList;
qualFuncNameParseClassIdOidList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnParseClassIdOidList, ddlType, null, null, null, null, null, true);

String unQualProcedureName;
String unQualProcedureShortName;
String un;
unQualProcedureName = "LRTLOCK";
unQualProcedureShortName = "LCK";
un = "";

boolean forLock;
for (int i = 1; i <= 2; i++) {
forLock = (i == 1);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, unQualProcedureName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-" + un.toUpperCase() + "LOCK", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classId_in", M01_Globals.g_dbtEntityId, true, "CLASSID of the row being " + un + "locked");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row being " + un + "locked");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being " + un + "locked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabFound", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "0", null, null);
// ### ENDIF IVK ###
if (forLock) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, (forLock ? M01_Common.TvBoolean.tvTrue : M01_Common.TvBoolean.tvNull), 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_activeLrtOidDdl + ";");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");
// ### ENDIF IVK ###

if (forLock) {
M11_LRT.genProcSectionHeader(fileNo, "verify that we have an active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_LDM.gc_db2RegVarLrtOid + " = '' THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("noLrt", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "process involved table(s) - with current MDS concepts there is exactly one table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmTypedTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENTITY_ID = classId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M11_LRT.genDdlPdmEntityCheck(fileNo, 3, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDM_" + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDM_POOLTYPE_ID = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
// ### IF IVK ###
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '(?,?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '(?,?,?)';");
}
// ### ELSE IVK ###
//   If forLock Then
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
//   Else
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?)';"
//   End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtOid,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tabFound = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that we found a table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_tabFound = " + M01_LDM.gc_dbFalse + " THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
if (un.compareTo("") == 0) {
M79_Err.genSignalDdlWithParms("noTableToLock", fileNo, 2, null, null, null, null, null, null, null, null, null, "classId_in", null, null, null);
} else {
M79_Err.genSignalDdlWithParms("noTableToUnLock", fileNo, 2, null, null, null, null, null, null, null, null, null, "classId_in", null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, unQualProcedureName + "List", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-" + un.toUpperCase() + "LOCK by list of OIDs", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classIdOidList_in", "CLOB(1M)", true, "'|'-separated list of pairs of 'classId,OID' to " + un + "lock");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being " + un + "locked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "0", null, null);
// ### ENDIF IVK ###
if (forLock) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, (forLock ? M01_Common.TvBoolean.tvTrue : M01_Common.TvBoolean.tvNull), 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_activeLrtOidDdl + ";");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");
// ### ENDIF IVK ###

if (forLock) {
M11_LRT.genProcSectionHeader(fileNo, "verify that we have an active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_LDM.gc_db2RegVarLrtOid + " = '' THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("noLrt", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "for each OID process involved table(s) - with current MDS concepts there is exactly one table per OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anPdmTypedTableName + "  AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals.g_anCid + " AS c_classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O." + M01_Globals.g_anOid + " AS c_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + qualFuncNameParseClassIdOidList + "(classIdOidList_in)) AS O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ENTITY_ID = O." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anPdmTypedTableName + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genDdlPdmEntityCheck(fileNo, 4, "T.");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.PDM_" + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.PDM_POOLTYPE_ID = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "make sure that we found a table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_tableName IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 3, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
if (un.compareTo("") == 0) {
M79_Err.genSignalDdlWithParms("noTableToLock", fileNo, 3, null, null, null, null, null, null, null, null, null, "c_classId", null, null, null);
} else {
M79_Err.genSignalDdlWithParms("noTableToUnLock", fileNo, 3, null, null, null, null, null, null, null, null, null, "c_classId", null, null, null);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
// ### IF IVK ###
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '(?,?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '(?,?,?)';");
}
// ### ELSE IVK ###
//   If forLock Then
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
//   Else
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?)';"
//   End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtOid,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "c_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


if (forLock) {
// ####################################################################################################################
// rs15

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "FILLTEMPTABLE", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for to store Oid / ClassId tuples in a temporary table", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classIdOidList_in", "CLOB(1M)", true, "'|'-separated list of pairs of 'classId,OID' to " + un + "lock");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being inserted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "0", null, null);
// ### ENDIF IVK ###
if (forLock) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, (forLock ? M01_Common.TvBoolean.tvTrue : M01_Common.TvBoolean.tvNull), 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_activeLrtOidDdl + ";");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");
// ### ENDIF IVK ###

if (forLock) {
M11_LRT.genProcSectionHeader(fileNo, "verify that we have an active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_LDM.gc_db2RegVarLrtOid + " = '' THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("noLrt", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genDdlForTempPrivClassIdOid(fileNo);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O.CLASSID AS classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O.OID AS oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (VL6CMET.PARSECLASSOIDLIST(classIdOidList_in)) AS O;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_rowCount = ( SELECT COUNT(*) FROM SESSION.PRIVCLASSIDOID );");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);");
M00_FileWriter.printToFile(fileNo, "");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, unQualProcedureName + "ListTempTable", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-" + un.toUpperCase() + "LOCK by list of OIDs in temporary table", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being " + un + "locked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "0", null, null);
// ### ENDIF IVK ###
if (forLock) {
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_classId", "CHAR(5)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, (forLock ? M01_Common.TvBoolean.tvTrue : M01_Common.TvBoolean.tvNull), 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_activeLrtOidDdl + ";");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");
// ### ENDIF IVK ###

if (forLock) {
M11_LRT.genProcSectionHeader(fileNo, "verify that we have an active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_LDM.gc_db2RegVarLrtOid + " = '' THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("noLrt", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genDdlForTempPrivClassIdOid(fileNo);
M11_LRT.genProcSectionHeader(fileNo, "make sure that there is a table for each classId", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID AS O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals.g_anCid + " NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.ENTITY_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualPdmTableViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M11_LRT.genDdlPdmEntityCheck(fileNo, 4, "T.");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.PDM_" + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.PDM_POOLTYPE_ID = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR READ ONLY;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_classId IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 3, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("noTableToLock", fileNo, 3, null, null, null, null, null, null, null, null, null, "v_classId", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "for each classId in the temp table process involved table(s)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anPdmTypedTableName + "  AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.PRIVCLASSIDOID AS O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ENTITY_ID = O." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M11_LRT.genDdlPdmEntityCheck(fileNo, 3, "T.");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.PDM_" + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.PDM_POOLTYPE_ID = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, "");
// ### IF IVK ###
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '_TEMPTABLE(?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + unQualProcedureName + "_' || c_tableName || '_TEMPTABLE(?,?)';");
}
// ### ELSE IVK ###
//   If forLock Then
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?,?)';"
//   Else
//     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?)';"
//   End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
if (forLock) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtOid,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "classIdOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

unQualProcedureName = "LRTUNLOCK";
unQualProcedureShortName = "ULK";
un = "un";

}

String qualTabNameChangeLog;
String qualTabNameChangeLogNl;

qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameChangeLogNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

// ### IF IVK ###
String qualTabNameJob;
qualTabNameJob = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ### ENDIF IVK ###
String qualProcNameLrtCommitIntern;
String qualProcNameLrtCommitExtern;
boolean isPrimaryOrg;
isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

boolean useLrtOidListParam;
String lrtOidRefVar;
for (int i = 1; i <= 2; i++) {
useLrtOidListParam = (i == 2);
if (useLrtOidListParam) {
lrtOidRefVar = "v_lrtOid";
qualProcNameLrtCommitExtern = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommitList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameLrtCommitIntern = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtCommitList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
// todo: move the internal procedure to 'internal schema' / remove the following statement
qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern;
} else {
lrtOidRefVar = "lrtOid_in";
qualProcNameLrtCommitExtern = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameLrtCommitIntern = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
// todo: move the internal procedure to 'internal schema' / remove the following statement
qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern;
}

// ####################################################################################################################
// #    SP for COMMIT on LRT
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for COMMIT an LRT", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtCommitIntern);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (useLrtOidListParam) {
M11_LRT.genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", true, "','-separated list of OIDs of LRTs to commit");
} else {
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to commit");
}
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "autoPriceSetProductive_in", M01_Globals.g_dbtBoolean, true, "specifies whether prices are set productive automatically");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "genChangelog_in", M01_Globals.g_dbtBoolean, true, "generate ChangeLog-records if and only if this parameter is '1'");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "forceGenWorkSpace_in", M01_Globals.g_dbtBoolean, true, "force call to GEN_WORKSPACE if and only if this parameter is '1'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", true, "number of rows affected by the commit");
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
// ### ELSE IVK ###
//   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by the commit"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0", null, null);
// ### IF IVK ###
if (!(isPrimaryOrg)) {
M11_LRT.genVarDecl(fileNo, "v_isCentralDataTransfer", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
}
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_jobCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_opType", "INTEGER", String.valueOf(M11_LRT.lrtStatusCreated), null, null);
M11_LRT.genVarDecl(fileNo, "v_commitTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_commitEndTs", "TIMESTAMP", "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_settingManActCP", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_settingManActTP", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_settingManActSE", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_settingSelRelease", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, true, null, true, true, null, null, null);

// ### IF IVK ###
if (useLrtOidListParam) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitIntern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitIntern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
}
// ### ELSE IVK ###
//   If useLrtOidListParam Then
//     genSpLogProcEnter fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "genChangelog_in", "rowCount_out"
//   Else
//     genSpLogProcEnter fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "genChangelog_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out    = 0;");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspError_out   = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspInfo_out    = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspWarning_out = NULL;");
// ### ENDIF IVK ###

M11_LRT.genProcSectionHeader(fileNo, "determine COMMIT timestamp", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_commitTs = CURRENT TIMESTAMP;");

int offset;
offset = 0;
if (useLrtOidListParam) {
M11_LRT.genProcSectionHeader(fileNo, "loop over all OIDs of LRTs in lrtOids_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lrtLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(ELEM AS " + M01_Globals.g_dbtOid + ") AS " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "POSINDEX ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
offset = 1;
}

M11_LRT.genProcSectionHeader(fileNo, "verify that this is an existing transaction", offset + 1, useLrtOidListParam);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "if this transaction does not exist, we need to quit", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_lrtCount = 0) THEN");

// ### IF IVK ###
if (useLrtOidListParam) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
}
// ### ELSE IVK ###
//   If useLrtOidListParam Then
//     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "genChangelog_in", "rowCount_out"
//   Else
//     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "genChangelog_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID" + (isPrimaryOrg ? "" : " / isCentralDataTransfer"), offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals_IVK.g_anPsOid + ",");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "U." + M01_Globals.g_anUserId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_psOid,");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_isCentralDataTransfer,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "verify that this transaction has not ended", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "NOT (L." + M01_Globals.g_anEndTime + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "if this transaction has already ended, we need to quit", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_lrtCount > 0) THEN");

// ### IF IVK ###
if (useLrtOidListParam) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
}
// ### ELSE IVK ###
//   If useLrtOidListParam Then
//     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "'lrtOids_in", "genChangelog_in", "rowCount_out"
//   Else
//     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "lrtOid_in", "genChangelog_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M79_Err.genSignalDdlWithParms("lrtAlreadyCompleted", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

// ### IF IVK ###
if (!(M03_Config.generateFwkTest)) {
String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine configuration settings", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "ISMANUALLYACTIVATEDCODEPRICE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "ISMANUALLYACTIVATEDTYPEPRICE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "ISMANUALLYACTIVATEDSTANDARDEQU");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_settingManActCP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_settingManActTP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_settingManActSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "USESELECTIVERELEASEPROCESS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_settingSelRelease");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "make sure that no job is running for this LRT", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_jobCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameJob);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF v_jobCount > 0 THEN");

if (useLrtOidListParam) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
}
M79_Err.genSignalDdlWithParms("lrtComHasActiveJobs", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");
}

// ### ENDIF IVK ###
if (useLrtOidListParam) {
M11_LRT.genProcSectionHeader(fileNo, "empty ChangeLog", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM " + M01_Globals.gc_tempTabNameChangeLog + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM " + M01_Globals.gc_tempTabNameChangeLogNl + ";");
}

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "for operation INSERT and DELETE loop over tables supporting pre-processing of LRT-Commit", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHILE v_opType IS NOT NULL DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anInLrt + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "OPID = v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anAcmUseLrtCommitPreprocess + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "schemaName AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "tableName  AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ORDER BY");
M11_LRT.genProcSectionHeader(fileNo, "invert sequence of tables processed for 'DELETE'", offset + 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(CASE WHEN v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN -1 ELSE 1 END) * seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '." + M01_ACM.spnLrtCommitPreProc.toUpperCase() + "_' || c_tableName || '(?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + lrtOidRefVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_opType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_commitTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_opType = (CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END WHILE;");

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "for each operation loop over tables to generate Change Log", offset + 1, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF genChangelog_in = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHILE v_opType IS NOT NULL DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "(CASE WHEN LENGTH(" + M01_Globals.g_anPdmTableName + ") > 8 and SUBSTR(" + M01_Globals.g_anPdmTableName + ", LENGTH(" + M01_Globals.g_anPdmTableName + ")- 8 + 1, 8) = '_NL_TEXT' THEN SUBSTR(" + M01_Globals.g_anPdmTableName + ", 1, LENGTH(" + M01_Globals.g_anPdmTableName + ")- 8) ELSE " + M01_Globals.g_anPdmTableName + " END) AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + M01_Globals.g_anInLrt + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "((OPID = v_opType) OR (v_opType = " + String.valueOf(M11_LRT.lrtStatusLocked) + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + M01_Globals.g_anAcmIgnoreForChangelog + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "ORDER BY");
M11_LRT.genProcSectionHeader(fileNo, "invert sequence of tables processed for 'DELETE'", offset + 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "(CASE WHEN v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN -1 ELSE 1 END) * seqNo ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "schemaName AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "DO");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '." + M01_ACM.spnLrtGenChangelog.toUpperCase() + "_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?," + (isPrimaryOrg ? "" : "?,") + "?)';");
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 4); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnLrtGenChangelog); "_' || c_tableName || '(?,?,?,?)';"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + lrtOidRefVar + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_psOid,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_opType,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_commitTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "autoPriceSetProductive_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_settingManActCP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_settingManActTP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_settingManActSE,");
if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_settingSelRelease");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_settingSelRelease,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "v_isCentralDataTransfer");
}
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 5); "v_commitTs"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SET v_opType = (CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN " + String.valueOf(M11_LRT.lrtStatusLocked) + " ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "END WHILE;");

M12_ChangeLog.genPersistChangeLogDdl(fileNo, M01_Globals.g_classIndexChangeLog, qualTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLog, qualTabNameChangeLogNl, M01_Globals.gc_tempTabNameChangeLogNl, qualSeqNameOid, ddlType, thisOrgIndex, thisPoolIndex, offset + 2, M12_ChangeLog.ChangeLogMode.eclLrt, qualNlTabName, lrtOidRefVar, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "for each operation <> 'locked' loop over tables to LRT-commit (sequence: DELETE -> CREATE -> UPDATE)", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHILE v_opType IS NOT NULL DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SCHEMANAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(CASE WHEN LENGTH(" + M01_Globals.g_anPdmTableName + ") > 8 and SUBSTR(" + M01_Globals.g_anPdmTableName + ", LENGTH(" + M01_Globals.g_anPdmTableName + ")- 8 + 1, 8) = '_NL_TEXT' THEN SUBSTR(" + M01_Globals.g_anPdmTableName + ", 1, LENGTH(" + M01_Globals.g_anPdmTableName + ")- 8) ELSE " + M01_Globals.g_anPdmTableName + " END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals.g_anInLrt + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "OPID = v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "schemaName AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "MIN(SEQNO) AS SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "V_Tab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "schemaName," + M01_Globals.g_anPdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ORDER BY");
M11_LRT.genProcSectionHeader(fileNo, "sequence of tables processed must be inverted for 'DELETE'", offset + 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(CASE WHEN v_opType = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN -1 ELSE 1 END) * SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "DO");

// ### IF IVK ###
if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?)' ;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?,?)' ;");
}
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 3); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?)' ;"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + lrtOidRefVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_cdUserId,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_opType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_commitTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "autoPriceSetProductive_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_settingManActCP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_settingManActTP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_settingManActSE,");

if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_settingSelRelease");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_settingSelRelease,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "v_isCentralDataTransfer");
}
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 4); "v_opType,"
//   Print #fileNo, addTab(offset + 4); "v_commitTs"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count number of committed rows", offset + 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_opType = (CASE v_opType WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END WHILE;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to finish LRT-commit (unlock)", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_opType = " + String.valueOf(M11_LRT.lrtStatusLocked) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SCHEMANAME AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anInLrt + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "ORDER BY");
M11_LRT.genProcSectionHeader(fileNo, "sequence of tables processed must be inverted since we execute 'DELETE' during postprocessing", offset + 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SEQNO DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DO");

// ### IF IVK ###
if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?)' ;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?,?)' ;");
}
// ### ELSE IVK ###
//     Print #fileNo, addTab(offset + 2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?)' ;"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + lrtOidRefVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_cdUserId,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_psOid,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_opType,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_commitTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "autoPriceSetProductive_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_settingManActCP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_settingManActTP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_settingManActSE,");
if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_settingSelRelease");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_settingSelRelease,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_isCentralDataTransfer");
}
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 3); "v_commitTs"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END FOR;");

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "call GEN_WORKSPACE if required", offset + 1, null);

if (!(isPrimaryOrg)) {
if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "MAX(L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_isCentralDataTransfer");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = CAST(X.ELEM AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF " + (isPrimaryOrg ? "" : "(v_isCentralDataTransfer = 1) OR ") + "(forceGenWorkSpace_in = 1) THEN");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of Organization", offset + 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT ORGOID INTO v_orgOid FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " WITH UR;");

M27_Meta.genCallGenWorkspaceDdl(fileNo, thisOrgIndex, thisPoolIndex, "v_orgOid", "v_psOid", M72_DataPool.g_pools.descriptors[thisPoolIndex].id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", offset + 2, ddlType, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "keep track of Product Structures and Divisions involved in this LRT", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals_IVK.gc_tempTabNameChangeLogStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "V_PsDiv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "DIVISIONOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "V_PsDiv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals_IVK.gc_tempTabNameChangeLogStatus + " CS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "COALESCE(V_PsDiv.psOid, -1) = COALESCE(CS.psOid, -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "COALESCE(V_PsDiv.divisionOid, -1) = COALESCE(CS.divisionOid, -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

// ### ENDIF IVK ###
if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
}

if (useLrtOidListParam) {
M11_LRT.genProcSectionHeader(fileNo, "loop again over all OIDs of LRTs in lrtOids_in to update meta information", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lrtLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(ELEM AS " + M01_Globals.g_dbtOid + ") AS " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "POSINDEX ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID" + (isPrimaryOrg ? "" : " / isCentralDataTransfer"), offset + 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals_IVK.g_anPsOid + ",");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "U." + M01_Globals.g_anUserId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_psOid,");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_isCentralDataTransfer,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (!(isPrimaryOrg & ! M03_Config.generateFwkTest)) {
M11_LRT.genProcSectionHeader(fileNo, "set 'commit timestamp' for last 'central data transfer'", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_isCentralDataTransfer = 1) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LASTCENTRALDATATRANSFERCOMMIT = LASTCENTRALDATATRANSFERBEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "cleanup FTO-CONFLICT-table", offset + 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "CLRLRT_OID = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "cleanup table \"" + qualTabNameLrtAffectedEntity + "\"", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_commitEndTs = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "mark this LRT as 'committed'", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anEndTime + " = v_commitTs,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COMMITTIME = v_commitEndTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals_IVK.g_anIsActive + " = " + M01_LDM.gc_dbFalse);
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 2); "COMMITTIME = CURRENT TIMESTAMP"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "cleanup info associated to LRT", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrtExecStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
}

// ### IF IVK ###
M12_ChangeLog.genMaintainChangeLogStatusDdl(thisOrgIndex, thisPoolIndex, fileNo, "v_commitEndTs", 1, ddlType, true);

if (useLrtOidListParam) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitIntern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitIntern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null);
}
// ### ELSE IVK ###
//   If useLrtOidListParam Then
//     genSpLogProcExit fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "genChangelog_in", "rowCount_out"
//   Else
//     genSpLogProcExit fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "genChangelog_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for COMMIT on LRT
// ####################################################################################################################

// ### IF IVK ###
boolean useGenChangeLogParam;
int j;
for (int j = 1; j <= 2; j++) {
useGenChangeLogParam = (j == 2);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M22_Class_Utilities.printSectionHeader("SP for COMMIT an LRT", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtCommitExtern);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (useLrtOidListParam) {
M11_LRT.genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", true, "','-separated list of OIDs of LRTs to commit");
} else {
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to commit");
}

// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "autoPriceSetProductive_in", M01_Globals.g_dbtBoolean, true, "specifies whether prices are set productive automatically");
if (useGenChangeLogParam) {
M11_LRT.genProcParm(fileNo, "IN", "genChangelog_in", M01_Globals.g_dbtBoolean, true, "generate ChangeLog-records if and only if this parameter is '1'");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", true, "number of rows affected by the commit");
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
// ### ELSE IVK ###
//     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by the commit"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

if (useLrtOidListParam) {
// ### IF IVK ###
if (useGenChangeLogParam) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtCommitIntern + "(lrtOids_in, autoPriceSetProductive_in, genChangelog_in, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtCommitIntern + "(lrtOids_in, autoPriceSetProductive_in, 1, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "'lrtOids_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
}
// ### ELSE IVK ###
//       genSpLogProcEnter fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "rowCount_out"
//
//       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOids_in, 1, rowCount_out);"
//
//       genSpLogProcExit fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "rowCount_out"
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
if (useGenChangeLogParam) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtCommitIntern + "(lrtOid_in, autoPriceSetProductive_in, genChangelog_in, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtCommitIntern + "(lrtOid_in, autoPriceSetProductive_in, 1, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, null, "lrtOid_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null, null, null, null);
}
// ### ELSE IVK ###
//       genSpLogProcEnter fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "rowCount_out"
//
//       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOid_in, 1, rowCount_out);"
//
//       genSpLogProcExit fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "rowCount_out"
// ### ENDIF IVK ###
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}

// ####################################################################################################################

genLrtSupportDdlByPool2(fileNo, thisOrgIndex, thisPoolIndex, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
private static void genActivateCodeForEntity(Integer acmEntityType, int acmEntityIndex, int fileNo, Boolean forGenW, Boolean forNlW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

//On Error GoTo ErrorExit 

boolean isSubjectToActivation;
boolean isAggHead;
boolean isPsTagged;
String qualTabName;
boolean ignoreForChangelog;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
isSubjectToActivation = (M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentAggHead |  M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass) &  M22_Class.g_classes.descriptors[acmEntityIndex].superClassIndex <= 0;
isAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].isAggHead & ! forNl;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
qualTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, null, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
isSubjectToActivation = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasPriceAssignmentAggHead &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].maxLeftCardinality < 0 & M23_Relationship.g_relationships.descriptors[acmEntityIndex].maxRightCardinality < 0 & M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex <= 0;
isAggHead = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
qualTabName = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, forNl, null, null, null);
} else {
return;
}

if (!(isSubjectToActivation)) {
return;
}

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

if (!(forNl & ! ignoreForChangelog)) {
String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

M12_ChangeLog.genGenChangeLogRecordDdl(acmEntityIndex, acmEntityType, qualTabName, "", qualSeqNameOid, M01_Globals.gc_tempTabNameChangeLog, "create changelog-records for status-update on table '" + qualTabName + "'", "", thisOrgIndex, thisPoolIndex, fileNo, ddlType, null, null, M01_Globals.g_anStatus, null, M01_Common.typeId.etSmallint, M12_ChangeLog.ChangeLogMode.eclPubMassUpdate, M01_Common.AttrCategory.eacSetProdMeta, 1, "T." + M01_Globals.g_anStatus, "v_targetState", "", "NEXTVAL FOR " + qualSeqNameOid, "v_cdUserId", String.valueOf(M11_LRT.lrtStatusUpdated), null, null, null, M01_Globals_IVK.g_classIndexCodePriceAssignment);
}

M11_LRT.genProcSectionHeader(fileNo, "update status on table '" + qualTabName + "'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anStatus + " = v_targetState,");
if (!(forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anVersionId + " = T." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anStatus + " < v_targetState");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anIsNational + " = forNational_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + (isAggHead ? M01_Globals.g_anCid : M01_Globals.g_anAhCid) + " = classId_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}

if (!(isAggHead)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericAspect + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anIsNational + " = forNational_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (isAggHead & ! forGen & !forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET priceCount_out = priceCount_out + v_rowCount;");
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
}
// ### ENDIF IVK ###


private static void genLrtSupportDdlByPool0(int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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


String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtNlText;
qualTabNameLrtNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLdmLrt;
qualTabNameLdmLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

boolean lrtClassUseSurrogateKey;
lrtClassUseSurrogateKey = M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].useSurrogateKey;

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtExecStatus;
qualTabNameLrtExecStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ### IF IVK ###
if (!(M03_Config.generateFwkTest)) {
String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameConflict;
qualTabNameConflict = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
}

String qualViewName;
String qualViewNameLdm;

// ####################################################################################################################
// #    create view for LRTExecStatus
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for LRT-ExecStatus", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M22_Class.genAttrDeclsForClassRecursive(M01_Globals.g_classIndexLrtExecStatus, null, fileNo, ddlType, null, null, 1, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLastOpTime, "MAX(" + M01_Globals.g_anLastOpTime + ")", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals.g_classIndexLrtExecStatus, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtExecStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLrtOid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
qualViewNameLdm = M04_Utilities.genQualViewNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, "ACTIVE", null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].sectionIndex, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].className, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].isCommonToPools, true, qualViewNameLdm, qualViewName, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "Active LRT View \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtExecStatus].className + "\"", null, false, true, null, null, null, null, null);
// ### ELSE IVK ###
//     genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
//       qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
//       "Active LRT View """ & .sectionName & "." & .className & """", , False
// ### ENDIF IVK ###
}

// ####################################################################################################################
// #    create view to filter active LRTs
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "ACTIVE", null, null);

M22_Class_Utilities.printSectionHeader("View for filtering active LRTs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M22_Class.genAttrDeclsForClassRecursive(M01_Globals.g_classIndexLrt, null, fileNo, ddlType, null, null, 1, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M22_Class.genAttrDeclsForClassRecursive(M01_Globals.g_classIndexLrt, null, fileNo, ddlType, null, null, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anEndTime + " IS NULL");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualViewNameLdm = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, "ACTIVE", null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionIndex, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToPools, true, qualViewNameLdm, qualViewName, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "Active LRT View \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className + "\"", null, false, true, null, null, null, null, null);
// ### ELSE IVK ###
//   genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
//     qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
//     "Active LRT View """ & .sectionName & "." & .className & """", , False
// ### ENDIF IVK ###
// gen Aliases for NL-Text table

String qualNlTabName;
String qualNlTabNameLdm;
String nlObjName;
String nlObjNameShort;
nlObjName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, null, null, null);
nlObjNameShort = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].shortName, null, null, null);
qualNlTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);
qualNlTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

// ### IF IVK ###
M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionIndex, nlObjName, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToPools, true, qualNlTabNameLdm, qualNlTabName, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, false, false, false, false, "NL-Table \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + nlObjName + "\"", null, null, null, null, null, null, null, null);

M22_Class.genAliasDdl(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionIndex, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToPools, true, qualTabNameLdmLrt, qualTabNameLrt, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, false, false, false, false, "LDM-Table \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className + "\"", null, null, null, null, null, null, null, true);
// ### ELSE IVK ###
//   genAliasDdl .sectionIndex, nlObjName, .isCommonToOrgs, .isCommonToPools, True, _
//               qualNlTabNameLdm, qualNlTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
//               "NL-Table """ & .sectionName & "." & nlObjName & """"
//
//   genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
//               qualTabNameLdmLrt, qualTabNameLrt, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
//               "LDM-Table """ & .sectionName & "." & .className & """", , , , , True
// ### ENDIF IVK ###


// ### ENDIF IVK ###

String qualTriggerName;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################
nlObjName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, null, null, null);
nlObjNameShort = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].shortName, null, null, null);
qualNlTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);
qualNlTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);
// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "ACTIVE_INS", null, null);
// ### ELSE IVK ###
//   qualTriggerName = genQualTriggerNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE_INS")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Insert-Trigger on table \"" + qualTabNameLrt + "\" (ACM-class \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M79_Err.genSignalDdl("insertNotAllowed", fileNo, 1, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, "NL_INS", null, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger on table \"" + qualTabNameLrtNlText + "\" (ACM-class \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CASCADE BEFORE INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameLrtNlText);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "retrieve PS-Tag from registry-variable (if not explicitly set)", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid + " IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET " + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
// ### ENDIF IVK ###

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################
qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "ACTIVE_UPD", null, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger on table \"" + qualTabNameLrt + "\" (ACM-class \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].className + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "if PS-tag does not 'fit' return with error", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (" + M01_Globals_IVK.gc_db2RegVarPsOid + " <> '') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (" + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid + " <> " + M01_Globals_IVK.g_activePsOidDdl + ") OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "   (" + M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid + " <> " + M01_Globals_IVK.g_activePsOidDdl + ") THEN");
M79_Err.genSignalDdl("incorrPsTag", fileNo, 3, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, M01_Globals_IVK.g_anIsActive, "", M01_Globals_IVK.g_anIsCentralDataTransfer, "", M01_Globals_IVK.g_anIsInUseByFto, "", null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals.g_classIndexLrt, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "make sure that update does not involve any of the 'non-updatable' columns", null, null);
boolean firstCol;
firstCol = true;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF");
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (tabColumns.descriptors[i].columnCategory != M01_Common.AttrCategory.eacVid) {
if (!(firstCol)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
}

if (M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType == M01_Common.typeId.etTimestamp) {
if (tabColumns.descriptors[i].isNullable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(VARCHAR_FORMAT(" + M01_Globals.gc_oldRecordName + "." + tabColumns.descriptors[i].columnName + ", 'YYYY-MM-DD HH24:MI:SS'),'') <> " + "COALESCE(VARCHAR_FORMAT(" + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[i].columnName + ", 'YYYY-MM-DD HH24:MI:SS'),'')");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VARCHAR_FORMAT(" + M01_Globals.gc_oldRecordName + "." + tabColumns.descriptors[i].columnName + ", 'YYYY-MM-DD HH24:MI:SS') <> " + "VARCHAR_FORMAT(" + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[i].columnName + ", 'YYYY-MM-DD HH24:MI:SS')");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.gc_oldRecordName + "." + tabColumns.descriptors[i].columnName + " <> " + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[i].columnName);
}

firstCol = false;
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "THEN");

M79_Err.genSignalDdl("updateNotAllowed", fileNo, 2, M01_ACM.clnLrt, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "do the update", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsActive + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsActive + ", 0),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsCentralDataTransfer + " = (CASE " + M01_Globals_IVK.g_anIsCentralDataTransfer + " WHEN 1 THEN 1 ELSE COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsCentralDataTransfer + ", " + M01_Globals_IVK.g_anIsCentralDataTransfer + ") END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsInUseByFto + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsInUseByFto + ", " + M01_Globals_IVK.g_anIsInUseByFto + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anVersionId + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anVersionId + ", " + M01_Globals.g_anVersionId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
// ### ELSE IVK ###
// genSignalDdl "updateNotAllowed", fileNo, 1, clnLrt
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "ACTIVE_DEL", null, null);

M22_Class_Utilities.printSectionHeader("Delete-Trigger on table \"" + qualTabNameLrt + "\" (ACM-class \"" + M01_ACM.snLrt + "." + M01_ACM.clnLrt + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M79_Err.genSignalDdl("deleteNotAllowed", fileNo, 1, M01_ACM.clnLrt, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    create view to determine LDM tables involved in an LRT
// ####################################################################################################################

qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M01_ACM.vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for all LDM-tables related to a specific LRT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmIgnoreForChangelog + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmUseLrtCommitPreprocess + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anAcmDisplayCategory + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT." + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT." + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT.OPID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIgnoreForChangelog + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmUseLrtCommitPreprocess + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals_IVK.g_anAcmDisplayCategory + ",");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmFkSequenceNo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity + " AT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT." + M01_Globals.g_anAcmOrParEntityId + " = AE." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameLdmTable + " LT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anLdmIsGen + " = AT." + M01_Globals.g_anAcmIsGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AT.OPID = " + String.valueOf(M11_LRT.lrtStatusLocked));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsNl + " = AT." + M01_Globals.g_anLdmIsNl);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
qualViewNameLdm = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M01_ACM.vsnLrtAffectedLdmTab, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCommonToOrgs, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCommonToPools, true, qualViewNameLdm, qualViewName, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "LRT-AFFECTED-LDM-TABLES View \"" + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].sectionName + "." + M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrtAffectedEntity].className + "\"", null, true, null, null, null, null, null, null);
// ### ELSE IVK ###
//     genAliasDdl g_sectionIndexLrt, vnLrtAffectedLdmTab, .isCommonToOrgs, .isCommonToPools, True, _
//       qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
//       "LRT-AFFECTED-LDM-TABLES View """ & .sectionName & "." & .className & """", , True
// ### ENDIF IVK ###
}

}


private static void genLrtSupportDdlByPool2(int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewNamePdmTabs;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
qualViewNamePdmTabs = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedLdmTab, M01_ACM.vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
} else {
qualViewNamePdmTabs = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM.vnLrtAffectedPdmTab, M01_ACM.vsnLrtAffectedPdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
}

String qualPdmTableViewName;
qualPdmTableViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnPdmTable, M01_ACM.vnsPdmTable, ddlType, null, null, null, null, null, null, null, null, null, null);

boolean isPrimaryOrg;
isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

// ### IF IVK ###
if (!(M03_Config.generateFwkTest)) {
String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameConflict;
qualTabNameConflict = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
}

String qualTabNameJob;
qualTabNameJob = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ### ENDIF IVK ###
String qualProcNameLrtCommitIntern;
String qualProcNameLrtCommitExtern;
qualProcNameLrtCommitExtern = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern;

String qualTabNameLrtExecStatus;
qualTabNameLrtExecStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ####################################################################################################################

String qualProcNameLrtRollback;
boolean useLrtOidListParam;
String lrtOidRefVar;
int i;
for (int i = 1; i <= 2; i++) {
useLrtOidListParam = (i == 2);
if (useLrtOidListParam) {
lrtOidRefVar = "v_lrtOid";
qualProcNameLrtRollback = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtRollbackList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
} else {
lrtOidRefVar = "lrtOid_in";
qualProcNameLrtRollback = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtRollback, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
}

// ####################################################################################################################
// #    SP for ROLLBACK on LRT
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for ROLLBACK on an LRT", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtRollback);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (useLrtOidListParam) {
M11_LRT.genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", true, "','-separated list of OIDs of LRTs to commit");
} else {
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to rollback");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by the rollback");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", 0, null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_jobCount", "INTEGER", "0", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rollBackTs", "TIMESTAMP", "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
if (!(isPrimaryOrg)) {
M11_LRT.genVarDecl(fileNo, "v_isCentralDataTransfer", M01_Globals.g_dbtBoolean, 0, null, null);
}
// ### ENDIF IVK ###
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtRollback, ddlType, null, (useLrtOidListParam ? "'lrtOids_in" : "lrtOid_in"), "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

int offset;
offset = 0;
if (useLrtOidListParam) {
M11_LRT.genProcSectionHeader(fileNo, "loop over all OIDs of LRTs in lrtOids_in", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lrtLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(ELEM AS " + M01_Globals.g_dbtOid + ") AS " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "POSINDEX ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
offset = 1;
}

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID", offset + 1, useLrtOidListParam);
if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT " + M01_Globals_IVK.g_anPsOid + " INTO v_psOid FROM " + qualTabNameLrt + " WHERE OID = " + lrtOidRefVar + " WITH UR;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_psOid = " + M01_Globals_IVK.g_activePsOidDdl + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "make sure that no job is running for this LRT", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "v_jobCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameJob);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF v_jobCount > 0 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, (useLrtOidListParam ? "'lrtOids_in" : "lrtOid_in"), "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("lrtRbHasActiveJobs", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "verify that this transaction has not ended", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "NOT (L." + M01_Globals.g_anEndTime + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "if this transaction has already ended, we need to quit", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_lrtCount > 0) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, (useLrtOidListParam ? "'lrtOids_in" : "lrtOid_in"), "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("lrtAlreadyCompleted", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that this is an existing transaction", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "if this transaction does not exist, we need to quit", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_lrtCount = 0) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, -(offset + 2), (useLrtOidListParam ? "'lrtOids_in" : "lrtOid_in"), "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, offset + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + lrtOidRefVar + "))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine ROLLBACK timestamp", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET v_rollBackTs = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "rollback all tables", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SCHEMANAME AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anInLrt + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.LRTROLLBACK_' || c_tableName || '(?,?)' ;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END FOR;");

if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "loop again over all OIDs of LRTs in lrtOids_in to update meta information", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR lrtLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(ELEM AS " + M01_Globals.g_dbtOid + ") AS " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "POSINDEX ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
}

// ### IF IVK ###
if (!(isPrimaryOrg & ! M03_Config.generateFwkTest)) {
M11_LRT.genProcSectionHeader(fileNo, "cleanup FTO-CONFLICT-table", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CLRLRT_OID = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");
}

// ### ENDIF IVK ###

M11_LRT.genProcSectionHeader(fileNo, "cleanup table \"" + qualTabNameLrtAffectedEntity + "\"", offset + 1, useLrtOidListParam);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "mark this LRT as 'rolled back'", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SET");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anEndTime + " = v_rollBackTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals_IVK.g_anIsActive + " = " + M01_LDM.gc_dbFalse);
// ### ELSE IVK ###
//   Print #fileNo, addTab(offset + 2); g_anEndTime; " = v_rollBackTs"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "cleanup info associated to LRT", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrtExecStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.g_anLrtOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");

if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "MAX(L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "v_isCentralDataTransfer");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "L." + M01_Globals.g_anOid + " = " + lrtOidRefVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "IF (v_isCentralDataTransfer = 1) THEN");
M11_LRT.genProcSectionHeader(fileNo, "cleanup generalsettings info if LRT was used for FTO", offset + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LASTCENTRALDATATRANSFERBEGIN = LASTCENTRALDATATRANSFERCOMMIT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "END IF;");
}

if (useLrtOidListParam) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, (useLrtOidListParam ? "'lrtOids_in" : "lrtOid_in"), "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
// ####################################################################################################################
// #    SP for propagating status update from aggregate head to aggregate children
// ####################################################################################################################

String qualProcNamePropStatus;

qualProcNamePropStatus = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnAHPropagateStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for propagating status update from aggregate head to aggregate children", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePropStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classId_in", M01_Globals.g_dbtEntityId, true, "CLASSID of the row to propagate the status for");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row to propagate the status for");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records updated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabFound", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePropStatus, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");

M11_LRT.genProcSectionHeader(fileNo, "process involved table(s) - with current MDS concepts there is exactly one table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmFkSchemaName + " AS tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmTypedTableName + " AS tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENTITY_ID = classId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENTITY_ISLRT = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LDM_ISGEN = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LDM_ISLRT = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LDM_ISNL = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDM_" + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDM_POOLTYPE_ID = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));

// FIXME: extend meta model to cover this
String tabNameList;
tabNameList = "";
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[i].supportAhStatusPropagation &  M22_Class.g_classes.descriptors[i].isAggHead) {
tabNameList = tabNameList + (tabNameList.compareTo("") == 0 ? "" : ",") + "'" + M04_Utilities.getUnqualObjName(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[i].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null)) + "'";
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmTypedTableName + " IN (" + tabNameList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || tabSchema || '." + M01_ACM_IVK.spnAHPropagateStatus.toUpperCase() + "_' || tabName || '(?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tabFound = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that we found a table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_tabFound = " + M01_LDM.gc_dbFalse + " THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNamePropStatus, ddlType, 2, "classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("noTablePropStatus", fileNo, 2, null, null, null, null, null, null, null, null, null, "classId_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePropStatus, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for propagating status update from aggregate head to aggregate children
// ####################################################################################################################

String qualProcNameDelObjPropStatus;

qualProcNameDelObjPropStatus = M04_Utilities.genQualProcName(M01_Globals.g_sectionindexAliasDelObj, M01_ACM_IVK.spnAHPropagateStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for propagating status update from aggregate head to aggregate children", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDelObjPropStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classId_in", M01_Globals.g_dbtEntityId, true, "CLASSID of the row to propagate the status for");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row to propagate the status for");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records updated");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDelObjPropStatus, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNamePropStatus + "(classId_in, oid_in, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDelObjPropStatus, ddlType, null, "'classId_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### ENDIF IVK ###
// ####################################################################################################################
// #    SP for retrieving LRT-Log
// ####################################################################################################################

// we define multiple versions of this stored procedure with different sets of API-parameters
boolean fillRestrictedColSetOnly;
boolean useLangParameter;
boolean useMaxRecordCount;
// ### IF IVK ###
boolean useDisplayCategory;
// ### ENDIF IVK ###
String spInfix;
String qualProcNameLrtGetLog;
// to enable API with just two parameters start loop with 'i = 1'
for (int i = 2; i <= 4; i++) {
fillRestrictedColSetOnly = (i == 3);
useLangParameter = (i == 2 |  i == 4);
// ### IF IVK ###
useDisplayCategory = (i == 4);
// ### ENDIF IVK ###
useMaxRecordCount = (i == 4);
spInfix = (fillRestrictedColSetOnly ? "_RED" : "");

qualProcNameLrtGetLog = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtGetLog + spInfix, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

if (fillRestrictedColSetOnly) {
M22_Class_Utilities.printSectionHeader("SP for retrieving LRT-Log / retrieve 'restricted set of columns only' (e.g. no ChangeComment)", fileNo, null, null);
} else {
M22_Class_Utilities.printSectionHeader("SP for retrieving LRT-Log", fileNo, null, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtGetLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to retrieve the Log for");
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", useLangParameter, "(optional) retrieve only records for updates past this timestamp");
// ### IF IVK ###
if (useDisplayCategory) {
M11_LRT.genProcParm(fileNo, "IN", "displayCategory_in", "CHAR(1)", useLangParameter |  useMaxRecordCount, "(optional) retrieve only records related to this category");
}
// ### ENDIF IVK ###
if (useLangParameter) {
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtEnumId, useMaxRecordCount, "use this language to retrieve NL-Texts");
}
if (useMaxRecordCount) {
M11_LRT.genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", false, "maximum number of rows to retrieve");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (!(fillRestrictedColSetOnly & ! useLangParameter)) {
M11_LRT.genVarDecl(fileNo, "v_languageId", M01_Globals.g_dbtEnumId, "NULL", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_maxRowCount", "INTEGER", "NULL", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genDdlForTempLrtLog(fileNo, null, fillRestrictedColSetOnly, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtGetLog, ddlType, null, "lrtOid_in", "startTime_in", (useLangParameter ? "languageId_in" : ""), "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

if (useMaxRecordCount) {
M11_LRT.genProcSectionHeader(fileNo, "initialize variable(s)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_maxRowCount = maxRowCount_in;");
}

if (!(fillRestrictedColSetOnly & ! useLangParameter)) {
M11_LRT.genProcSectionHeader(fileNo, "determine user's language", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U.DATALANGUAGE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_languageId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "use 'English' if user does not have data language", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_languageId = COALESCE(v_languageId, " + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ");");
} else if (useLangParameter) {
M11_LRT.genProcSectionHeader(fileNo, "use 'English' if no language is specified", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET languageId_in = COALESCE(languageId_in, " + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ");");
}

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID corresponding to LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "loop over affected PDM tables and collect log records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SCHEMANAME AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SEQNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNamePdmTabs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
// ### IF IVK ###
if (useDisplayCategory) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "displayCategory_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anAcmDisplayCategory + " = displayCategory_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anInLrt + " = lrtOid_in");
if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPID <> " + String.valueOf(M11_LRT.lrtStatusLocked));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anIgnoreForChangelog + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '" + M01_ACM.spnLrtGetLog.toUpperCase() + spInfix + "_' || c_tableName || '(" + (!(fillRestrictedColSetOnly) ? "?,?," : "") + "?,?,?,?)' ;");
// ### ELSE IVK ###
//   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; UCase(spnLrtGetLog); spInfix; "_' || c_tableName || '("; _
//                             IIf(Not fillRestrictedColSetOnly, "?,?,", ""); _
//                             "?,?,?)' ;"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid_in,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
// ### ENDIF IVK ###
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + (useLangParameter ? "languageId_in" : "v_languageId") + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "startTime_in" + (fillRestrictedColSetOnly ? "" : ","));
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_maxRowCount");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_maxRowCount = (CASE WHEN v_maxRowCount > v_rowCount THEN v_maxRowCount - v_rowCount ELSE 0 END);");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

if (!(fillRestrictedColSetOnly)) {
M11_LRT.genProcSectionHeader(fileNo, "set language specific column 'entityName' in log", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M11_LRT.tempTabNameLrtLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityName = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ENL." + M01_Globals.g_anAcmEntityLabel);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityType + " = L.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityId + " = L.entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ENL." + M01_Globals.g_anLanguageId + " = " + (useLangParameter ? "languageId_in" : "v_languageId"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_LrtLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");

if (useMaxRecordCount) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ROWNUMBER() OVER (ORDER BY displayMe DESC, OID ASC) AS seqNo,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "entityType,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "entityName,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "displayCategory,");
// ### ENDIF IVK ###
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refClassId1,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refObjectId1,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refClassId2,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refObjectId2,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "label,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "comment,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "code,");
// ### ENDIF IVK ###
if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "baseCode,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "baseEndSlot,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "validTo,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "operation,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ts");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M11_LRT.tempTabNameLrtLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityType,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityName,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "displayCategory,");
// ### ENDIF IVK ###
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "oid,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refClassId1,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refObjectId1,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refClassId2,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refObjectId2,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "label,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "comment,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "code,");
// ### ENDIF IVK ###
if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "baseCode,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "baseEndSlot,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "validTo,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "operation,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ts");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_LrtLog");
if (useMaxRecordCount) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "maxRowCount_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo <= maxRowCount_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN logCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtGetLog, ddlType, null, "lrtOid_in", "startTime_in", (useLangParameter ? "languageId_in" : ""), "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (fillRestrictedColSetOnly) {
String qualProcNameLrtGetLogWrapper;
qualProcNameLrtGetLogWrapper = M04_Utilities.genQualProcName(M01_Globals.g_sectionindexAliasPrivateOnly, M01_ACM.spnLrtGetLog + spInfix, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Wrapper SP for retrieving LRT-Log in Work Data Pool / retrieve 'restricted set of columns only'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtGetLogWrapper);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to retrieve the Log for");
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", false, "(optional) retrieve only records for updates past this timestamp");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtGetLogWrapper, ddlType, null, "lrtOid_in", "startTime_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtGetLog + "(lrtOid_in, startTime_in);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtGetLogWrapper, ddlType, null, "lrtOid_in", "startTime_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
if (useDisplayCategory) {
if (fillRestrictedColSetOnly) {
M22_Class_Utilities.printSectionHeader("SP for retrieving LRT-Log / retrieve 'restricted set of columns only' (e.g. no ChangeComment)", fileNo, null, null);
} else {
M22_Class_Utilities.printSectionHeader("SP for retrieving LRT-Log", fileNo, null, null);
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtGetLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to retrieve the Log for");
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", useLangParameter |  useMaxRecordCount, "(optional) retrieve only records for updates past this timestamp");
if (useLangParameter) {
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtEnumId, useMaxRecordCount, "use this language to retrieve NL-Texts");
}
if (useMaxRecordCount) {
M11_LRT.genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", false, "maximum number of rows to retrieve");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtGetLog, ddlType, null, "lrtOid_in", "startTime_in", (useLangParameter ? "languageId_in" : ""), (useMaxRecordCount ? "maxRowCount_in" : ""), null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtGetLog + "(lrtOid_in, startTime_in" + ", NULL" + (useLangParameter ? ", languageId_in" : "") + (useMaxRecordCount ? ", maxRowCount_in" : "") + ");");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtGetLog, ddlType, null, "lrtOid_in", "startTime_in", (useLangParameter ? "languageId_in" : ""), (useMaxRecordCount ? "maxRowCount_in" : ""), null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
// ### ENDIF IVK ###
}

// ####################################################################################################################
// #    SP for retrieving cardinality of LRT-Log
// ####################################################################################################################

String qualProcNameLrtGetLogCard;
qualProcNameLrtGetLogCard = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtGetLogCard, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for retrieving cardinality of LRT-Log", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtGetLogCard);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows in the log");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;");

M11_LRT.genDdlForTempLrtLog(fileNo, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtGetLogCard, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "count rows in LRT-Log", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = (SELECT COUNT(DISTINCT oid) FROM " + M11_LRT.tempTabNameLrtLog + ");");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtGetLogCard, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
// #    SP for checking if Lrt contains division data
// ####################################################################################################################

String procName;
procName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnLrtIncludesDivisionData, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for checking if Lrt contains division data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + procName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to check");
M11_LRT.genProcParm(fileNo, "OUT", "result_out", M01_Globals.g_dbtBoolean, false, "0 = false, 1 = true");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogProcEnter(fileNo, procName, ddlType, null, "lrtOid_in", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "result_out =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals.g_qualTabNameAcmEntity + " E, " + qualViewNamePdmTabs + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E.ENTITYID = L.ORPARENTENTITYID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E.ENTITYTYPE = L.ENTITYTYPE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E.ISPS = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E.ISPSFORMING = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E.ISLRT = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "L.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, procName, ddlType, null, "result_out", null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


}


// ### IF IVK ###
private static void genLrtSpSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M01_Globals.g_genLrtSupport |  M03_Config.generateFwkTest)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    SP for activion of all Prices
// ####################################################################################################################

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTypeSpec;
qualTabNameTypeSpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTypeStandardEquipment;
qualTabNameTypeStandardEquipment = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeStandardEquipment, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

int changeLogClassIndex;
changeLogClassIndex = M01_Globals.g_classIndexChangeLog;
String qualTabNameChangeLog;
qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(changeLogClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameChangeLogNl;
qualTabNameChangeLogNl = M04_Utilities.genQualNlTabNameByClassIndex(changeLogClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcName;
String qualProcNameCP;
String qualProcNameDelObCP;
String qualProcNameTP;
String qualProcNameDelObTP;

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnActivateAllPrices, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activation of all Prices", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forNational_in", M01_Globals.g_dbtBoolean, true, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices");
M11_LRT.genProcParm(fileNo, "IN", "classId_in", M01_Globals.g_dbtEntityId, true, "classId of Price to activate (supported: Code Prices and Type Price)");
M11_LRT.genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", true, "number of Code Prices activated");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records activated (including aggregate children)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_targetState", M01_Globals.g_dbtEnumId, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(600)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, false, null, null, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "forNational_in", "classId_in", "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals_IVK.g_activePsOidDdl + ";");

M11_LRT.genProcSectionHeader(fileNo, "make sure that ProductStructure exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_psOid IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "forNational_in", "classId_in", "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("noPs", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF NOT EXISTS (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE OID = v_psOid) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "forNational_in", "classId_in", "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_psOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "make parameter classId_in is set correctly", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + );
M79_Err.genSignalDdlWithParms("illegParam", fileNo, 2, "classId_in", null, null, null, null, null, null, null, null, "RTRIM(CHAR(classId_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");


M11_LRT.genProcSectionHeader(fileNo, "determine target state according to configuration settings for 'selective release process'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "((CASE USESELECTIVERELEASEPROCESS WHEN 0 THEN " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive) + " ELSE " + String.valueOf(M86_SetProductive.statusReadyForRelease) + " END))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_targetState");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_targetState = COALESCE(v_targetState, " + String.valueOf(M86_SetProductive.statusReadyForRelease) + ");");

M11_LRT.genProcSectionHeader(fileNo, "determine current user id (for changelog)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'MIG_NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");

M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTimestamp = CURRENT TIMESTAMP;");

int i;
for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
if (M23_Relationship.g_relationships.descriptors[i].hasPriceAssignmentAggHead &  M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality < 0 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality < 0 & M23_Relationship.g_relationships.descriptors[i].reusedRelIndex <= 0) {
genActivateCodeForEntity(M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, M23_Relationship.g_relationships.descriptors[i].relIndex, fileNo, null, null, thisOrgIndex, thisPoolIndex, ddlType);
}
}
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if ((M22_Class.g_classes.descriptors[i].hasPriceAssignmentAggHead |  M22_Class.g_classes.descriptors[i].hasPriceAssignmentSubClass) &  M22_Class.g_classes.descriptors[i].superClassIndex <= 0) {
genActivateCodeForEntity(M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[i].classIndex, fileNo, null, null, thisOrgIndex, thisPoolIndex, ddlType);

if (M22_Class.g_classes.descriptors[i].hasNlAttrsInNonGenInclSubClasses) {
genActivateCodeForEntity(M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[i].classIndex, fileNo, null, true, thisOrgIndex, thisPoolIndex, ddlType);
}
}
}


M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = '09032' THEN");
M11_LRT.genProcSectionHeader(fileNo, "update status on table table '" + qualTabNameTypeSpec + "'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTypeSpec + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anStatus + " = v_targetState,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anVersionId + " = T." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anStatus + " < v_targetState");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TSTTPA_OID IN (SELECT OID FROM " + qualTabNameGenericAspect + " GA WHERE GA.STATUS_ID = v_targetState)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "update status on table '" + qualTabNameTypeStandardEquipment + "'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTypeStandardEquipment + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anStatus + " = v_targetState,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anVersionId + " = T." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anStatus + " < v_targetState");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.TSETYS_OID IN (SELECT OID FROM " + qualTabNameTypeSpec + " TS WHERE TS.STATUS_ID = v_targetState)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "move all ChangeLog records into persistent table", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute.genAttrListForEntity(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add NL-texts to changelog", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAcmAttributeLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anVersionId);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "languageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attributeLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENL." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ANL." + M01_Globals.g_anAcmAttributeLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENL." + M01_Globals.g_anAcmEntityLabel);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog + " L");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityId + " = E." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityType + " = E." + M01_Globals.g_anAcmEntityType);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);

// FIXME: assuming that within a single class hierarchy a given attribute name is not mapped
// differently for different classes we use 'DISTINCT' here. We should navigate up in the
// class hierarchy and pick exactly the attribute that is referred to!
// E.g. 'SR0CONTEXT' exists multiple times in the GENERICASPECT-tree. Each changelog-entry refers to a unique
// occurence.
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " EA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmOrParEntitySection + " = E." + M01_Globals.g_anAcmOrParEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmOrParEntityName + " = E." + M01_Globals.g_anAcmOrParEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmOrParEntityType + " = E." + M01_Globals.g_anAcmOrParEntityType);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmAttributeNl + " ANL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmEntitySection + " = ANL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmEntityName + " = ANL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EA." + M01_Globals.g_anAcmEntityType + " = ANL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmDbColumnName + " = ANL." + M01_Globals.g_anAcmAttributeName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmDbColumnName + " = ANL." + M01_Globals.g_anAcmAttributeName + " || '_ID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_NlD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "languageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attributeLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "languageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "attributeLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEXTVAL FOR " + qualSeqNameOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "languageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attributeLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_NlD");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "forNational_in", "classId_in", "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for activion of all Code Prices
// ####################################################################################################################

qualProcNameDelObCP = M04_Utilities.genQualProcName(M01_Globals.g_sectionindexAliasDelObj, M01_ACM_IVK.spnActivateAllCodePrices, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activion of all Code Prices", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDelObCP);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forNational_in", M01_Globals.g_dbtBoolean, true, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices");
M11_LRT.genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", true, "number of Code Prices activated");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records activated (including aggregate children)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");


M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDelObCP, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + );

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for activion of all Code Prices
// ####################################################################################################################

qualProcNameCP = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnActivateAllCodePrices, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activion of all Code Prices", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameCP);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forNational_in", M01_Globals.g_dbtBoolean, true, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices");
M11_LRT.genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", true, "number of Code Prices activated");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records activated (including aggregate children)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");


M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameCP, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + );

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for activion of all Type Prices
// ####################################################################################################################

qualProcNameDelObTP = M04_Utilities.genQualProcName(M01_Globals.g_sectionindexAliasDelObj, M01_ACM_IVK.spnActivateAllTypePrices, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activion of all Type Prices", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDelObTP);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forNational_in", M01_Globals.g_dbtBoolean, true, "if 'TRUE' activate all national Type Prices, if 'FALSE' activate non-national Type Prices");
M11_LRT.genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", true, "number of Type Prices activated");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records activated (including aggregate children)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");


M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDelObTP, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + );

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for activion of all Type Prices
// ####################################################################################################################

qualProcNameTP = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnActivateAllTypePrices, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for activion of all Type Prices", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameTP);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forNational_in", M01_Globals.g_dbtBoolean, true, "if 'TRUE' activate all national Type Prices, if 'FALSE' activate non-national Type Prices");
M11_LRT.genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", true, "number of Type Prices activated");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records activated (including aggregate children)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");


M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameTP, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName +  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + );

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "forNational_in",  + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + , "priceCount_out", "rowCount_out", null, null, null, null, null, null, null, null);

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


private static void genDdlForAggStatusProp(String qualChildTabName, int fileNo, Integer indentW, String oidReferenceW, String statusReferenceW, String classIdReferenceW, String recordCountVarW, String recordCountVarSumW) {
int indent; 
if (indentW == null) {
indent = 3;
} else {
indent = indentW;
}

String oidReference; 
if (oidReferenceW == null) {
oidReference = "NEWRECORD.OID";
} else {
oidReference = oidReferenceW;
}

String statusReference; 
if (statusReferenceW == null) {
statusReference = "NEWRECORD.STATUS_ID";
} else {
statusReference = statusReferenceW;
}

String classIdReference; 
if (classIdReferenceW == null) {
classIdReference = "NEWRECORD.CLASSID";
} else {
classIdReference = classIdReferenceW;
}

String recordCountVar; 
if (recordCountVarW == null) {
recordCountVar = "";
} else {
recordCountVar = recordCountVarW;
}

String recordCountVarSum; 
if (recordCountVarSumW == null) {
recordCountVarSum = "";
} else {
recordCountVarSum = recordCountVarSumW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualChildTabName + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals.g_anStatus + " = " + statusReference);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals.g_anAhCid + " = " + classIdReference);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals.g_anAhOid + " = " + oidReference);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals.g_anStatus + " NOT IN (" + statusReference + "," + String.valueOf(M86_SetProductive.statusProductive) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

if (recordCountVar != "" &  recordCountVarSum != "") {
M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "GET DIAGNOSTICS " + recordCountVar + " = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + recordCountVarSum + " = " + recordCountVarSum + " + " + recordCountVar + ";");
}
}


private static void genDdlForAggStatusPropLrtCommit(String qualChildTabName, String qualAhPrivTabName, String priceAssignmentSubClassIdList, int fileNo, Integer indentW, Boolean isPsTaggedW, String psTagRefValW) {
int indent; 
if (indentW == null) {
indent = 3;
} else {
indent = indentW;
}

boolean isPsTagged; 
if (isPsTaggedW == null) {
isPsTagged = false;
} else {
isPsTagged = isPsTaggedW;
}

String psTagRefVal; 
if (psTagRefValW == null) {
psTagRefVal = "";
} else {
psTagRefVal = psTagRefValW;
}

boolean hasPriceAssignmentSubClass;
hasPriceAssignmentSubClass = (!(priceAssignmentSubClassIdList.compareTo("") == 0));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualChildTabName + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals.g_anStatus + " = ");

M00_FileWriter.printToFile(fileNo, (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (D." + M01_Globals.g_anAhCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : ""));
M00_FileWriter.printToFile(fileNo, M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(");
M00_FileWriter.printToFile(fileNo, "D." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + "),");
M00_FileWriter.printToFile(fileNo, "settingManActCP_in,");
M00_FileWriter.printToFile(fileNo, "settingManActTP_in,");
M00_FileWriter.printToFile(fileNo, "settingManActSE_in,");
M00_FileWriter.printToFile(fileNo, "settingSelRelease_in");
M00_FileWriter.printToFile(fileNo, ")");
M00_FileWriter.printToFile(fileNo, (hasPriceAssignmentSubClass ? " END" : ""));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");

if (isPsTagged &  psTagRefVal != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "D." + M01_Globals_IVK.g_anPsOid + " = " + psTagRefVal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + pc_tempTabNamePubOidsAffected + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "D." + M01_Globals.g_anAhOid + " = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "D." + M01_Globals.g_anAhCid + " = O.classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "D." + M01_Globals.g_anStatus + " NOT IN (O.privStatusId, " + String.valueOf(M86_SetProductive.statusProductive) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR" + ";");
}


// ### ENDIF IVK ###
public static void genLrtSupportViewForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Boolean isPurelyPrivateW) {
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

boolean isPurelyPrivate; 
if (isPurelyPrivateW == null) {
isPurelyPrivate = false;
} else {
isPurelyPrivate = isPurelyPrivateW;
}

String sectionName;
int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
boolean isUserTransactional;
// ### IF IVK ###
boolean isPsTagged;
boolean psTagOptional;
// ### ENDIF IVK ###
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
String dbAcmEntityType;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
// ### IF IVK ###
boolean hasNoIdentity;
boolean isNational;
// ### ENDIF IVK ###
boolean isAggHead;
int ahClassIndex;
String ahClassIdStr;
int[] aggChildClassIndexes;
int[] aggChildRelIndexes;
boolean useMqtToImplementLrtForEntity;
// ### IF IVK ###
boolean objSupportsPsDpFilter;
boolean condenseData;
boolean expandExpressionsInFtoView;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
ahClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
ahClassIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggChildClassIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes;
aggChildRelIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
objSupportsPsDpFilter = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
expandExpressionsInFtoView = M22_Class.g_classes.descriptors[acmEntityIndex].expandExpressionsInFtoView;
// ### ENDIF IVK ###
isAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].isAggHead & ! forGen & !forNl;

if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
hasOwnTable = true;
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
// ### ENDIF IVK ###
isAbstract = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
hasNoIdentity = false;
isNational = false;
// ### ENDIF IVK ###
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
// ### ENDIF IVK ###
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
// ### IF IVK ###
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isNational = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable;
// ### ENDIF IVK ###
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
dbAcmEntityType = "R";
ahClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
ahClassIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
hasNoIdentity = false;
isNational = false;
// ### ENDIF IVK ###
isAggHead = false;
// ### IF IVK ###
psTagOptional = false;
// ### ENDIF IVK ###
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
objSupportsPsDpFilter = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
condenseData = false;
expandExpressionsInFtoView = false;
// ### ENDIF IVK ###

aggChildClassIndexes =  new int[0];
aggChildRelIndexes =  new int[0];

if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
}
} else {
return;
}

if (!(M03_Config.generateLrt |  (ddlType == M01_Common.DdlTypeId.edtLdm & ! isUserTransactional))) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

boolean M72_DataPool.poolSupportLrt;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex == M01_Globals_IVK.g_archiveDataPoolIndex) {
// LRT-emulating view is implemented in Archive-module
return;
}

// ### ENDIF IVK ###
//  If poolsupportLrt And useMqtToImplementLrtForEntity And Not isPurelyPrivate And Not implementLrtNonMqtViewsForEntitiesSupportingMqts Then
if (M72_DataPool.poolSupportLrt &  useMqtToImplementLrtForEntity & !isPurelyPrivate) {
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
String qualTabNamePub;
String qualTabNamePriv;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, null, null);
} else {
qualTabNamePub = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, forNl, null, null, null);
}

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualAggHeadTabName;
String qualAggHeadLockProcName;
if (ahClassIndex > 0) {
qualAggHeadTabName = M04_Utilities.genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualAggHeadLockProcName = M04_Utilities.genQualProcName(M22_Class.g_classes.descriptors[ahClassIndex].sectionIndex, "LRTLOCK_" + M22_Class.g_classes.descriptors[ahClassIndex].className, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
}

String qualViewName;
String qualViewNameLdm;
// ### IF IVK ###
boolean showDeletedObjectsInView;
boolean filterForPsDpMapping;
boolean filterForPsDpMappingExtended;
// ### ENDIF IVK ###
String tabQualifier;

if (M03_Config.g_cfgLrtGenDB2View) {
// if pool does not support LRT generate View which provides the same interface as LRT-Views
if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolSupportLrt) {
// ### IF IVK ###
// we need to generate three views
//   - one filtering for Product Structures in PSDPMAPPING (special feature for interfaces / first loop)
//   - one filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / second loop)
//   - one not filtering for Product Structures in PSDPMAPPING (third loop)
// not filtering for Product Structures in PSDPMAPPING is done in second loop since this view is the one used in subsequent trigger definitions
int i;
for (int i = 1; i <= 3; i++) {
filterForPsDpMapping = (i == 1);
filterForPsDpMappingExtended = (i == 2);

if (filterForPsDpMapping &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextI;
}
if (filterForPsDpMappingExtended &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextI;
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

// ### IF IVK ###
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, (filterForPsDpMapping ? "I" : (filterForPsDpMappingExtended ? "J" : "")), null, null);
// ### ELSE IVK ###
//       qualViewName = _
//         genQualViewNameByEntityIndex( _
//           acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl _
//         )
// ### ENDIF IVK ###
M22_Class_Utilities.printSectionHeader("LRT-emulating View for table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, "(");
if (!(forGen & ! forNl)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conWorkingState, M01_ACM.conWorkingState, M24_Attribute_Utilities.AttrValueType.eavtEnum, M21_Enum.getEnumIndexByName(M01_ACM.dxnWorkingState, M01_ACM.dnWorkingState, null), acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT' AND 'STATUS_ID'
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta, null, 1, true, null), null, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, 1, true, null), null, null);
}

// ### ENDIF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, false, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, , edomListLrt Or edomLrtPriv
//       Else
//         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListLrt Or edomLrtPriv
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, ")");
M00_FileWriter.printToFile(fileNo, "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (isPurelyPrivate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomValue |  (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomValue |  (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//         If forNl Then
//           genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomValue Or edomLrtPriv
//         Else
//           genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomValue Or edomLrtPriv
//         End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSIBM.SYSDUMMY1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0 = 1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");

tabQualifier = entityShortName.toUpperCase();
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, tabQualifier + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, tabQualifier, null, null);

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//         If forNl Then
//           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomValueLrt Or edomLrtPriv
//         Else
//           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomValueLrt Or edomLrtPriv
//         End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " " + tabQualifier);

// ### IF IVK ###
if (isPsTagged) {
if (filterForPsDpMapping |  filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = PSDPM.PSOID");

if (thisPoolIndex == M01_Globals.g_workDataPoolIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabQualifier + "." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}

if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM_SP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PSDPM_SP.PSOID = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}

if (!((filterForPsDpMapping |  filterForPsDpMappingExtended))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
if (M03_Config.usePsFltrByDpMappingForRegularViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}
// ### ENDIF IVK ###
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, null, null, forGen, true, null, forNl, null, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, false, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, true, null, filterForPsDpMapping, filterForPsDpMappingExtended, "LRT-emulating View " + "\"" + sectionName + "." + entityName + "\"", null, true, isPsTagged, objSupportsPsDpFilter, null, null, forNl, null);
// ### ELSE IVK ###
//         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
//           qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
//           "LRT-emulating View " & """" & sectionName & "." & entityName & """", , True, , forNl
// ### ENDIF IVK ###
}
// ### IF IVK ###

NextI:
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
// we need to generate four views
//   - one not filtering out deleted objects (first loop)
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / second loop)
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / third loop)
//   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (fourth loop)
// filtering deleted objects / not filtering by PSDPMAPPING is done in third loop since this view is the one used in subsequent trigger definitions
for (int i = 1; i <= 4; i++) {
showDeletedObjectsInView = (i == 1);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
// ### IF IVK ###
filterForPsDpMapping = (i == 2);
filterForPsDpMappingExtended = (i == 3);

if (filterForPsDpMapping &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextII;
}
if (filterForPsDpMappingExtended &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextII;
}

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, (showDeletedObjectsInView ? "D" : "") + (filterForPsDpMapping ? "I" : (filterForPsDpMappingExtended ? "J" : "")), null, null);

M22_Class_Utilities.printSectionHeader("View for 'merging' private and public LRT rows of table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, "(" + (showDeletedObjectsInView ? "" : "do not ") + "retrieve deleted objects" + (M03_Config.supportFilteringByPsDpMapping ? " / " + (filterForPsDpMapping |  filterForPsDpMappingExtended ? "" : "do not ") + "filter by PSDPMAPPING" : "") + ")");
// ### ELSE IVK ###
//       qualViewName = genQualViewNameByEntityIndex(sacmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
//
//       printSectionHeader "View for 'merging' private and public LRT rows of table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (!(forGen & ! forNl)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conWorkingState, M01_ACM.conWorkingState, M24_Attribute_Utilities.AttrValueType.eavtEnum, M21_Enum.getEnumIndexByName(M01_ACM.dxnWorkingState, M01_ACM.dnWorkingState, null), acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta, null, 1, true, null), null, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, 1, true, null), null, null);
}

// ### ENDIF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, false, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, false, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, edomListLrt
//       Else
//         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListLrt
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");

// ### IF IVK ###
if (isPurelyPrivate &  (filterForPsDpMapping |  filterForPsDpMappingExtended)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}

if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, M01_Common.DdlOutputMode.edomValue |  (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomValue |  (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSIBM.SYSDUMMY1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
}

// ### ENDIF IVK ###
if (!(isPurelyPrivate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

// ### IF IVK ###
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M11_LRT.workingStateUnlocked) + "),");
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
} else if (!(forGen & ! forNl)) {
// ### ELSE IVK ###
//         If Not forGen And Not forNl Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN PUB." + M01_Globals.g_anInLrt + " IS NULL THEN " + String.valueOf(M11_LRT.workingStateUnlocked));
// ### IF IVK ###
if (!((filterForPsDpMapping |  filterForPsDpMappingExtended))) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN PUB." + M01_Globals.g_anInLrt + " = " + M01_Globals.g_activeLrtOidDdl + " THEN " + String.valueOf(M11_LRT.workingLockedInActiveTransaction));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN PUBLRT.UTROWN_OID = (SELECT UTROWN_OID FROM " + qualTabNameLrt + " WHERE OID = " + M01_Globals.g_activeLrtOidDdl + ") THEN " + String.valueOf(M11_LRT.workingLockedInInactiveTransaction));
// ### IF IVK ###
if (!(showDeletedObjectsInView &  isAggHead)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN (" + M02_ToolMeta.getActiveLrtOidStrDdl(ddlType, thisOrgIndex) + " = '') AND (RTRIM(CURRENT CLIENT_USERID) = (SELECT USR." + M01_Globals.g_anUserId + " FROM " + M01_Globals.g_qualTabNameUser + " USR WHERE USR." + M01_Globals.g_anOid + " = PUBLRT.UTROWN_OID)) THEN " + String.valueOf(M11_LRT.workingLockedInInactiveTransaction));
}
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE " + String.valueOf(M11_LRT.workingLockedByOtherUser));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
}

// ### IF IVK ###
if (filterForPsDpMapping |  filterForPsDpMappingExtended | condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUBLRT.UTROWN_OID,");
}

// ### ENDIF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "PUB.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB.", M01_Globals.g_activeLrtOidDdl, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueLrt | M01_Common.DdlOutputMode.edomValueVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueLrt | M01_Common.DdlOutputMode.edomValueVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//         If forNl Then
//           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomValueLrt Or edomLrtPriv
//         Else
//           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomValueLrt Or edomLrtPriv
//         End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");

// ### IF IVK ###
if (isPsTagged &  (filterForPsDpMapping |  filterForPsDpMappingExtended)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals_IVK.g_anPsOid + " = PSDPM.PSOID");
if (thisPoolIndex == M01_Globals.g_workDataPoolIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
}

if (!(filterForPsDpMapping & ! filterForPsDpMappingExtended & !condenseData)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " PUBLRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = PUBLRT." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
// ### IF IVK ###
if (!(showDeletedObjectsInView)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PUB." + M01_Globals_IVK.g_anIsDeleted + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}

if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(1=1)");
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -6
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PUB." + M01_Globals.g_anInLrt + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PUB." + M01_Globals.g_anInLrt + " <> " + M01_Globals.g_activeLrtOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###

// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");

if (M03_Config.usePsFltrByDpMappingForRegularViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PUB." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PUB." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PUB." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

// ### IF IVK ###
if (!(filterForPsDpMapping & ! filterForPsDpMappingExtended)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "UNION ALL");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (!(filterForPsDpMapping & ! filterForPsDpMappingExtended)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M11_LRT.workingLockedInActiveTransaction) + ",");
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anInLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIVLRT.UTROWN_OID,");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, (M03_Config.hasBeenSetProductiveInPrivLrt ? 1 : 2) + (condenseData ? 1 : 0), null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV.", M01_Globals.g_activeLrtOidDdl, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conIsDeleted, M01_LDM.gc_dbFalse, null, null, null);
if (condenseData) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "", null, null, null);
}
if (!(M03_Config.hasBeenSetProductiveInPrivLrt)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, (condenseData ? 3 : 2), M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomValueNonLrt | M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomValueNonLrt | M01_Common.DdlOutputMode.edomValueVirtual, null);
}
// ### ELSE IVK ###
//         initAttributeTransformation transformation, 0, , , , "PRIV."
//         setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "PRIV.", g_activeLrtOidDdl
//
//         If forNl Then
//           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, True, edomListLrt Or edomValueNonLrt
//         Else
//           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomValueNonLrt
//         End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " PRIV");

// ### IF IVK ###
if (isPsTagged &  (filterForPsDpMapping |  filterForPsDpMappingExtended)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = PSDPM.PSOID");

if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM_SP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PSDPM_SP.PSOID = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}



// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " PRIVLRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anInLrt + " = PRIVLRT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if (!(showDeletedObjectsInView)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PRIV." + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PRIV." + M01_Globals.g_anInLrt + " = " + M01_Globals.g_activeLrtOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
if (!(filterForPsDpMapping & ! filterForPsDpMappingExtended & M03_Config.usePsFltrByDpMappingForRegularViews)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PRIV." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PRIV." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PRIV." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  (!(useMqtToImplementLrtForEntity | ! M03_Config.activateLrtMqtViews | isPurelyPrivate))) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, null, null, forGen, true, null, forNl, null, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, false, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, true, showDeletedObjectsInView, filterForPsDpMapping, filterForPsDpMappingExtended, "LRT-View" + (showDeletedObjectsInView ? " (include deleted objects)" : "") + (M03_Config.supportFilteringByPsDpMapping ? " (" + (filterForPsDpMapping ? "" : "do not ") + "filter by PSDPMAPPING)" : "") + " \"" + sectionName + "." + entityName + "\"", null, true, isPsTagged, objSupportsPsDpFilter, null, null, forNl, null);
// ### ELSE IVK ###
//         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
//           qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
//           "LRT-View """ & sectionName & "." & entityName & """", , True, , forNl
// ### ENDIF IVK ###
}
// ### IF IVK ###

NextII:
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genLrtSupportTriggerForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNoClView, int fileNoTrigger, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Boolean forMqtW, Boolean isPurelyPrivateW) {
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

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean isPurelyPrivate; 
if (isPurelyPrivateW == null) {
isPurelyPrivate = false;
} else {
isPurelyPrivate = isPurelyPrivateW;
}

String sectionName;
String sectionShortName;
int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
boolean isUserTransactional;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean entityInsertable;
boolean entityUpdatable;
boolean entityDeletable;
boolean isAbstract;
String entityIdStr;
String dbAcmEntityType;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
int ahClassIndex;
String ahClassIdStr;
int[] aggChildClassIndexes;
int[] aggChildRelIndexes;
boolean useMqtToImplementLrtForEntity;
boolean isSubjectToActivation;
String busKeyAttrListNoFks;
String[] busKeyAttrArrayNoFks;
boolean logLastChange;
// ### IF IVK ###
boolean isPsTagged;
boolean psTagOptional;
boolean hasNoIdentity;
boolean isNational;
boolean ignorePsRegVarOnInsertDelete;
int numGroupIdAttrs;
int[] groupIdAttrIndexes;
boolean hasExpBasedVirtualAttrs;
boolean condenseData;
boolean isGenericAspectHead;// GenericAspects always need special treatment ;-)
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

// ### IF IVK ###
isGenericAspectHead = false;
// ### ENDIF IVK ###
busKeyAttrListNoFks = "";

groupIdAttrIndexes =  new int[0];

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
logLastChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
// ### IF IVK ###
entityInsertable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmInsert);
entityUpdatable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmUpdate);
entityDeletable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmDelete);
// ### ELSE IVK ###
//     entityInsertable = True
//     entityUpdatable = True
//     entityDeletable = True
// ### ENDIF IVK ###
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
ahClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
ahClassIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggChildClassIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes;
aggChildRelIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
if (M22_Class.g_classes.descriptors[acmEntityIndex].hasBusinessKey) {
busKeyAttrListNoFks = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, true);
M24_Attribute.genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks);
}

// ### IF IVK ###
isGenericAspectHead = M22_Class.g_classes.descriptors[acmEntityIndex].classIndex == M01_Globals_IVK.g_classIndexGenericAspect & ! forGen & !forNl;

isSubjectToActivation = (M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentAggHead |  M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass) &  M22_Class.g_classes.descriptors[acmEntityIndex].superClassIndex <= 0;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;

if (M22_Class.g_classes.descriptors[acmEntityIndex].hasGroupIdAttrInNonGenInclSubClasses & ! forNl & !forGen) {
groupIdAttrIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].groupIdAttrIndexesInclSubclasses;
numGroupIdAttrs = M00_Helper.uBound(groupIdAttrIndexes) - M00_Helper.lBound(groupIdAttrIndexes) + 1;
}

ignorePsRegVarOnInsertDelete = M22_Class.g_classes.descriptors[acmEntityIndex].ignPsRegVarOnInsDel;

// ### ENDIF IVK ###
if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
hasOwnTable = true;
isAbstract = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
hasNoIdentity = false;
isNational = false;
hasExpBasedVirtualAttrs = false;
// ### ENDIF IVK ###
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isNational = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable;

hasExpBasedVirtualAttrs = !(forNl &  ((forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInGenInclSubClasses) |  (!(forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses))));
// ### ENDIF IVK ###
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
entityInsertable = true;
entityUpdatable = true;
entityDeletable = true;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
dbAcmEntityType = "R";
ahClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
ahClassIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
relRefs.numRefs = 0;
isGenForming = false;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
hasNoIdentity = false;
isNational = false;
psTagOptional = false;
hasExpBasedVirtualAttrs = false;
isSubjectToActivation = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasPriceAssignmentAggHead &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].maxLeftCardinality < 0 & M23_Relationship.g_relationships.descriptors[acmEntityIndex].maxRightCardinality < 0 & M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex <= 0;
condenseData = false;

numGroupIdAttrs = 0;
ignorePsRegVarOnInsertDelete = false;
// ### ENDIF IVK ###

aggChildClassIndexes =  new int[0];
aggChildRelIndexes =  new int[0];

if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
}
} else {
return;
}

if (!(M03_Config.generateLrt |  (ddlType == M01_Common.DdlTypeId.edtLdm & ! isUserTransactional))) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

boolean M72_DataPool.poolSupportLrt;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
} else {
returnValue = (ddlType == M01_Common.DdlTypeId.edtLdm);
}
// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex == M01_Globals_IVK.g_archiveDataPoolIndex) {
// LRT-emulating view is implemented in Archive-module
return;
}

// ### ENDIF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  forMqt & !M72_DataPool.poolSupportLrt) {
// LRT-emulating view is only supported in non-MQT-mode
return;
}

if (M72_DataPool.poolSupportLrt &  useMqtToImplementLrtForEntity & !forMqt & !isPurelyPrivate & !M03_Config.implementLrtNonMqtViewsForEntitiesSupportingMqts) {
return;
}

if (!(M03_Config.g_cfgLrtGenDB2Trigger)) {
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePub;
String qualTabNamePriv;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, null, null);
} else {
qualTabNamePub = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, forNl, null, null, null);
}

String unQualTabNamePub;
String unQualTabNamePriv;
unQualTabNamePub = M04_Utilities.getUnqualObjName(qualTabNamePub);
unQualTabNamePriv = M04_Utilities.getUnqualObjName(qualTabNamePriv);

String qualTabNameAggHeadPub;
String qualViewNameAggHead;
String qualTabNameAggHeadPriv;
String qualProcNameAggHeadLock;
if (ahClassIndex > 0) {
qualTabNameAggHeadPub = M04_Utilities.genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameAggHeadPriv = M04_Utilities.genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

qualViewNameAggHead = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[ahClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, M22_Class.g_classes.descriptors[ahClassIndex].useMqtToImplementLrt, null, null, null, null, null);
qualProcNameAggHeadLock = M04_Utilities.genQualProcNameByEntityIndex(M22_Class.g_classes.descriptors[ahClassIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, "LRTLOCK", null, null, null, null);
}

String qualSeqNameGroupId;

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualTabNameChangeLog;
qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewName;

int nextTrIndex;
M24_Attribute_Utilities.EntityColumnDescriptors thisEntityTabColumns;
// ### IF IVK ###
M24_Attribute_Utilities.EntityColumnDescriptors genericAspectTabColumns;
int numVirtualAttrs;
// ### ENDIF IVK ###

boolean forDeletedObjects;
String nameSuffix;
int l;
// ### IF IVK ###
for (int l = 1; l <= ((isGenericAspectHead |  (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  (acmEntityIndex == M01_Globals_IVK.g_classIndexTypeSpec |  acmEntityIndex == M01_Globals_IVK.g_classIndexTypeStandardEquipment))) &  M72_DataPool.poolSupportLrt ? 2 : 1); l++) {
// ### ELSE IVK ###
// For l = 1 To 2
// ### ENDIF IVK ###
forDeletedObjects = (l == 2);
nameSuffix = (forDeletedObjects ? "D" : "");

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, forMqt, forNl, null, nameSuffix, null, null);

// ### IF IVK ###
if (isSubjectToActivation &  ((ddlType == M01_Common.DdlTypeId.edtLdm & ! forMqt) |  M72_DataPool.poolSupportLrt) & !forNl & (!(forMqt | ! M03_Config.implementLrtNonMqtViewsForEntitiesSupportingMqts)) & !forDeletedObjects) {
// ####################################################################################################################
// #    ChangeLog View for Public Update
// ####################################################################################################################

M12_ChangeLog.genChangeLogViewDdl(acmEntityIndex, acmEntityType, qualTabNamePub, "", "", qualTabNamePub, "", qualTabNameAggHeadPub, thisOrgIndex, thisPoolIndex, thisPoolIndex, fileNoClView, ddlType, forGen, M12_ChangeLog.ChangeLogMode.eclPubUpdate);
}

// ### ENDIF IVK ###
String qualTriggerName;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, (forNl ? "NL" : "") + (forMqt ? "M" : "") + nameSuffix + "LRT_INS", M04_Utilities.ObjNameDelimMode.eondmNone, null);
// ### ELSE IVK ###
//   qualTriggerName = _
//     genQualTriggerNameByEntityIndex( _
//       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "INS" _
//     )
// ### ENDIF IVK ###

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolSupportLrt) {
M22_Class_Utilities.printSectionHeader("Insert-Trigger for LRT-emulating view on table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityInsertable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
} else if (isPurelyPrivate) {
M79_Err.genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
} else {
// ### IF IVK ###
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);

// note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_PsOid'
M11_LRT.genPsCheckDdlForInsertDelete(fileNoTrigger, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, psTagOptional, null, false, "v_psOidRecord", "v_psOidRegVar", "v_psOid", null, qualViewName, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
}

// ### ENDIF IVK ###
M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvFalse, 1);

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, (ignorePsRegVarOnInsertDelete ? 0 : 1), null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
if (!(ignorePsRegVarOnInsertDelete)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
}
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ");");
}
M00_FileWriter.printToFile(fileNoTrigger, "END");
M00_FileWriter.printToFile(fileNoTrigger, M01_LDM.gc_sqlCmdDelim);
} else if (!((isPurelyPrivate &  forMqt))) {
M22_Class_Utilities.printSectionHeader("Insert-Trigger for 'public-private' LRT-view \"" + qualViewName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtClosed", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);

if (!(isPurelyPrivate)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_privOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_pubRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);

if ((!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) &  (ahClassIndex > 0)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_inLrt", M01_Globals.g_dbtOid, "0", null, null);
}
// ### IF IVK ###

if (M03_Config.maintainGroupIdColumnsInLrtTrigger &  (numGroupIdAttrs > 0)) {
int i;
for (int i = M00_Helper.lBound(groupIdAttrIndexes); i <= M00_Helper.uBound(groupIdAttrIndexes); i++) {
M11_LRT.genVarDecl(fileNoTrigger, "v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName, M25_Domain.getDbDatatypeByDomainIndex(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].domainIndex), "NULL", null, null);
}
}
// ### ENDIF IVK ###
}

M11_LRT.genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", String.valueOf(M11_LRT.lrtStatusCreated), null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0", null, null);

// ### IF IVK ###
if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
thisEntityTabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, thisEntityTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomXref, null);

numVirtualAttrs = 0;
int k;
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M11_LRT.genVarDecl(fileNoTrigger, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, M25_Domain.getDbDatatypeByDomainIndex(thisEntityTabColumns.descriptors[k].dbDomainIndex), "NULL", null, null);
numVirtualAttrs = numVirtualAttrs + 1;
}
}
}

boolean useDivOidHandling;
useDivOidHandling = (ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) &  (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) & !isPsTagged;

boolean useDivOidWhereClause;
useDivOidWhereClause = (ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! isPsTagged;

boolean useDivRelKey;
useDivRelKey = (acmEntityIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! forNl;


if (useDivOidHandling) {
M11_LRT.genVarDecl(fileNoTrigger, "v_DivOid", "BIGINT", "NULL", null, null);
}

if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
// note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
M11_LRT.genPsCheckDdlForInsertDelete(fileNoTrigger, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, null, psTagOptional, null, false, "v_psOidRecord", "v_psOidRegVar", null, null, qualViewName, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
} else if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) {
M11_LRT.genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, null, false, null, null);
}

// ### ENDIF IVK ###
M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine LRT OID", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET v_lrtOid = (CASE " + M01_LDM.gc_db2RegVarLrtOid + " WHEN '' THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals.g_activeLrtOidDdl + " END);");



// ### IF IVK ###
if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
boolean printedHeader;
printedHeader = false;
// to minimze number of calls to UDFs always call the LRT-version - use LRTOID = NULL if no LRT-context is set
transformation.M01_ACM.conEnumLabelText.lrtOidRef = (M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs ? "v_lrtOid" : "");

for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if (!(printedHeader)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "initialize variables for virtual attributes", 1, null);
printedHeader = true;
}
String virtAttrStr;
virtAttrStr = M04_Utilities.transformAttrName(thisEntityTabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, thisEntityTabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, thisEntityTabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null);
if (M03_Config.maintainVirtAttrInTriggerPubOnEntityTabs &  M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = " + virtAttrStr + ";");
} else if (M03_Config.maintainVirtAttrInTriggerPubOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = (CASE WHEN v_lrtOid IS NULL THEN " + virtAttrStr + " ELSE " + M01_Globals.gc_newRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " END);");
} else if (M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = (CASE WHEN v_lrtOid IS NOT NULL THEN " + virtAttrStr + " ELSE " + M01_Globals.gc_newRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " END);");
}
}
}
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, insert in public table", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "IF v_lrtOid IS NULL THEN");

if (useDivOidHandling) {
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_DivOid = (SELECT CDIDIV_OID FROM " + qualTabNameAggHeadPub + " WHERE OID = NEWRECORD.AHOID);");
}

if (isPurelyPrivate) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, true);
} else {
// ### IF IVK ###
if (M03_Config.maintainGroupIdColumnsInLrtTrigger &  (numGroupIdAttrs > 0)) {
for (int i = M00_Helper.lBound(groupIdAttrIndexes); i <= M00_Helper.uBound(groupIdAttrIndexes); i++) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCid + " IN ('" + M22_Class.getClassIdStrByIndex(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].acmEntityIndex) + "') THEN");
M11_LRT.genProcSectionHeader(fileNoTrigger, "determine value of group-ID column \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + "\"", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " = (");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName.toUpperCase());
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anCid + " IN ('" + M22_Class.getClassIdStrByIndex(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].acmEntityIndex) + "')");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "AND");
int j;
for (int j = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes); j <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes); j++) {
String v1;
String v2;
int maxVarNameLength;
// Fixme: get rid of this hard-coding
maxVarNameLength = 29;

if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].substring(0, 1) == "#") {
v1 = M04_Utilities.paddRight(M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j], thisOrgIndex, thisPoolIndex, ddlType, "PUB", null, null), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j], thisOrgIndex, thisPoolIndex, ddlType, M01_Globals.gc_newRecordName, null, null), maxVarNameLength, null);
} else {
v1 = M04_Utilities.paddRight("PUB." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].toUpperCase(), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(M01_Globals.gc_newRecordName + "." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].toUpperCase(), maxVarNameLength, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "(((" + v1 + " IS NULL) AND (" + v2 + " IS NULL)) OR (" + v1 + " =  " + v2 + "))" + (j < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes) ? " AND" : ""));
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " IS NULL THEN");
qualSeqNameGroupId = M04_Utilities.genQualObjName(sectionIndex, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].shortName, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].shortName, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " = NEXTVAL FOR " + qualSeqNameGroupId + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, "");
}
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt
//       Else
//         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 6 + numVirtualAttrs + (M03_Config.maintainGroupIdColumnsInLrtTrigger ? numGroupIdAttrs : 0), null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 5, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtLrtId + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + ", v_now)", null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", v_now)", null, null, true);
// ### IF IVK ###
if ((useDivOidHandling)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conDivOid, "v_DivOid", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
nextTrIndex = 7;

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors[k].columnName, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
}

if (M03_Config.maintainGroupIdColumnsInLrtTrigger &  (numGroupIdAttrs > 0)) {
for (int i = M00_Helper.lBound(groupIdAttrIndexes); i <= M00_Helper.uBound(groupIdAttrIndexes); i++) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName, "v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
// ### ENDIF IVK ###

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, _
//           forGen, False, , edomListNonLrt Or edomDefaultValue
//       Else
//         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , _
//           False, forGen, edomListNonLrt Or edomDefaultValue
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ");");

M00_FileWriter.printToFile(fileNoTrigger, "");
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "ELSE");

M11_LRT.genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, true);
// ### IF IVK ###
M11_LRT.genStatusCheckDdl(fileNoTrigger, M01_Globals.gc_newRecordName, null, 2);
// ### ENDIF IVK ###

if (!(isPurelyPrivate)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_newRecordName + " already exists as 'public record' (v_pubRecordExists = 1)", 2, null);

// ### IF IVK ###
if (condenseData) {
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF EXISTS(SELECT 1 FROM " + qualTabNamePub + " PUB WHERE PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + " AND PUB." + M01_ACM_IVK.conPsOid + " = " + M01_Globals.gc_newRecordName + "." + M01_ACM_IVK.conPsOid + " ) THEN");
} else {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF EXISTS(SELECT 1 FROM " + qualTabNamePub + " PUB WHERE PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + " ) THEN");
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_pubRecordExists = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_pubRecordExists = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PUB." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", M01_Globals.gc_newRecordName, M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}

M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_newRecordName + " already exists as 'private record' (v_privRecordExists > 0)", 2, null);
if (isPurelyPrivate) {
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF EXISTS(SELECT 1 FROM " + qualTabNamePriv + " PRIV WHERE PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + " AND PRIV." + M01_ACM_IVK.conPsOid + " = " + M01_Globals.gc_newRecordName + "." + M01_ACM_IVK.conPsOid + " ) THEN");
} else {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF EXISTS(SELECT 1 FROM " + qualTabNamePriv + " PRIV WHERE PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + " ) THEN");
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_privRecordExists = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_privRecordExists = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
} else {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PRIV." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PRIV", M01_Globals.gc_newRecordName, M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);");
}

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordExists = 1 THEN");

M79_Err.genSignalDdlWithParmsForCompoundSql("recordPrivatelyExists", fileNoTrigger, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + ".OID))", null, null, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");

if (isPurelyPrivate) {
M11_LRT.genProcSectionHeader(fileNoTrigger, M01_Globals.gc_newRecordName + " is a new 'private record'", 3, null);
} else {
M11_LRT.genProcSectionHeader(fileNoTrigger, "record does not exist 'in private' - make sure it does not exist 'in public'", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF v_pubRecordExists = 1 THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtInsAlready", fileNoTrigger, 4, unQualTabNamePriv, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0) &  ahClassIndex > 0) {
// lock the 'public aggregate head record' with this LRT-OID
M11_LRT.genAggHeadLockPropDdl(fileNoTrigger, M01_Globals.gc_newRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)), useDivOidWhereClause, useDivRelKey);
}

// ### IF IVK ###
if (M03_Config.maintainGroupIdColumnsInLrtTrigger &  (numGroupIdAttrs > 0)) {
for (int i = M00_Helper.lBound(groupIdAttrIndexes); i <= M00_Helper.uBound(groupIdAttrIndexes); i++) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCid + " IN ('" + M22_Class.getClassIdStrByIndex(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].acmEntityIndex) + "') THEN");
M11_LRT.genProcSectionHeader(fileNoTrigger, "determine value of group-ID column \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + "\"", 4, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " = (");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName.toUpperCase());
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "PRIV." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "PRIV." + M01_Globals.g_anCid + " IN ('" + M22_Class.getClassIdStrByIndex(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].acmEntityIndex) + "')");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "AND");
for (int j = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes); j <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes); j++) {
// Fixme: get rid of this hard-coding
maxVarNameLength = 29;

if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].substring(0, 1) == "#") {
v1 = M04_Utilities.paddRight(M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j], thisOrgIndex, thisPoolIndex, ddlType, "PRIV", null, "v_lrtOid"), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j], thisOrgIndex, thisPoolIndex, ddlType, M01_Globals.gc_newRecordName, null, "v_lrtOid"), maxVarNameLength, null);
} else {
v1 = M04_Utilities.paddRight("PRIV." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].toUpperCase(), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(M01_Globals.gc_newRecordName + "." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes[j].toUpperCase(), maxVarNameLength, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "(((" + v1 + " IS NULL) AND (" + v2 + " IS NULL)) OR (" + v1 + " =  " + v2 + "))" + (j < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].groupIdAttributes) ? " AND" : ""));
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + ");");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "IF v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " IS NULL THEN");
qualSeqNameGroupId = M04_Utilities.genQualObjName(sectionIndex, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].shortName, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].shortName, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SET v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName + " = NEXTVAL FOR " + qualSeqNameGroupId + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");
}
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "record neither exists 'in private' nor 'in public' - consider " + M01_Globals.gc_newRecordName + " as new 'private record'", 3, null);
}

if (useDivOidHandling) {
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_DivOid = (CASE WHEN NEWRECORD.DIV_OID IS NULL THEN (SELECT CDIDIV_OID FROM " + qualTabNameAggHeadPriv + " WHERE OID = NEWRECORD.AHOID) ELSE NEWRECORD.DIV_OID END);");
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//     If forNl Then
//       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
//     Else
//       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 7 + numVirtualAttrs + (M03_Config.maintainGroupIdColumnsInLrtTrigger ? numGroupIdAttrs : 0), null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + ", v_now)", null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", v_now)", null, null, true);
// ### IF IVK ###
if ((useDivOidHandling)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conDivOid, "v_DivOid", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conStatusId, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anStatus + ", " + M86_SetProductive.statusWorkInProgress + ")", null, null, true);
nextTrIndex = 8;

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors[k].columnName, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
}

if (M03_Config.maintainGroupIdColumnsInLrtTrigger &  (numGroupIdAttrs > 0)) {
for (int i = M00_Helper.lBound(groupIdAttrIndexes); i <= M00_Helper.uBound(groupIdAttrIndexes); i++) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName, "v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[i]].attributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//     If forNl Then
//       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , _
//         edomListLrt Or edomDefaultValue
//     Else
//       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , _
//         True, forGen, edomListLrt Or edomDefaultValue
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

// ### IF IVK ###
M11_LRT.genDdlForUpdateAffectedEntities(fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, entityIdStr, ahClassIdStr, "v_lrtOid", 2, null, !(condenseData));
// ### ELSE IVK ###
//     genDdlForUpdateAffectedEntities fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
//       entityIdStr, ahClassIdStr, "v_lrtOid", 2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
if (ddlType == M01_Common.DdlTypeId.edtPdm &  M72_DataPool.poolSupportLrt & !forNl & !forGen & (!(forMqt | ! M03_Config.implementLrtNonMqtViewsForEntitiesSupportingMqts)) & !forDeletedObjects & (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass)) {
if ((M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt ? !(forMqt | ! M03_Config.implementLrtNonMqtViewsForEntitiesSupportingMqts) : true) &  M22_Class.g_classes.descriptors[acmEntityIndex].supportAhStatusPropagation & M22_Class.g_classes.descriptors[acmEntityIndex].isAggHead) {
// ####################################################################################################################
// #    Procedure for propagating status update from aggregate head to aggregate children
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnAHPropagateStatus, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Procedure for propagating status update from aggregate head to aggregate children (ACM-Class \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNoTrigger, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "PS-OID of the row to propagate the status for");
M11_LRT.genProcParm(fileNoTrigger, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row to propagate the status for");
M11_LRT.genProcParm(fileNoTrigger, "OUT", "rowCount_out", "INTEGER", false, "number of records updated");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_statusId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_classId", M01_Globals.g_dbtEntityId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNoTrigger, null, null);

M07_SpLogging.genSpLogProcEnter(fileNoTrigger, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine aggregate's status", 1, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals.g_anStatus + ",");

if (M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'");
} else {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals.g_anCid);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "v_statusId,");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "v_classId");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = oid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNoTrigger, "if record does not exist there is nothing to propagate", 1, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "IF v_statusId IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNoTrigger, qualProcName, ddlType, 2, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("ahStatusPropNotFound", fileNoTrigger, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNoTrigger, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M00_FileWriter.printToFile(fileNoTrigger, "");
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes); i++) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isUserTransactional & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isCommonToOrgs & !M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isCommonToPools & M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].superClassIndex <= 0) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex != acmEntityIndex) {
// set status for base table
M11_LRT.genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "'", 1, true);
genDdlForAggStatusProp(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null), fileNoTrigger, 1, "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out");
}

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNlAttrsInNonGenInclSubClasses) {
// set status for NL-Text table
M11_LRT.genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (NL_TEXT)", 1, true);
genDdlForAggStatusProp(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null), fileNoTrigger, 1, "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out");
}

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isGenForming & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNoIdentity) {
// set status for GENtable
M11_LRT.genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (GEN)", 1, true);
genDdlForAggStatusProp(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null), fileNoTrigger, 1, "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out");

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNlAttrsInGenInclSubClasses) {
// set status for NL-Text GEN-table
M11_LRT.genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (GEN/NL_TEXT)", 1, true);
genDdlForAggStatusProp(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null), fileNoTrigger, 1, "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out");
}
}
}
}

for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes); i++) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isUserTransactional & ! M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isCommonToOrgs & !M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isCommonToPools) {
// set status for relationship table
M11_LRT.genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child relationship '" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].relName + "'", 1, true);
genDdlForAggStatusProp(M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null), fileNoTrigger, 1, "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out");
}
}

M07_SpLogging.genSpLogProcExit(fileNoTrigger, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

// ### ENDIF IVK ###
// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

// ### ENDIF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, (forNl ? "NL" : "") + (forMqt ? "M" : "") + nameSuffix + "LRT_UPD", null, null);
// ### ENDIF IVK ###
//   qualTriggerName = _
//     genQualTriggerNameByEntityIndex( _
//       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "UPD" _
//     )
// ### ENDIF IVK ###

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolSupportLrt) {
M22_Class_Utilities.printSectionHeader("Update-Trigger for LRT-emulating view on table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

useDivOidWhereClause = (ahClassIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! isPsTagged;
useDivRelKey = (acmEntityIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! forNl;

if (!(entityUpdatable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
} else if (isPurelyPrivate) {
M79_Err.genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
// ### IF IVK ###
} else if (condenseData) {
M79_Err.genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);

// note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
M11_LRT.genPsCheckDdlForUpdate(fileNoTrigger, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, psTagOptional, 1, false, null, null, null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
}

M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvFalse, 1);

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 2, "", "", M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ";");
}
M00_FileWriter.printToFile(fileNoTrigger, "END");
M00_FileWriter.printToFile(fileNoTrigger, M01_LDM.gc_sqlCmdDelim);
} else if (!((isPurelyPrivate &  forMqt))) {
M22_Class_Utilities.printSectionHeader("Update-Trigger for 'public-private' LRT-view \"" + qualViewName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);

M11_LRT.genVarDecl(fileNoTrigger, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtClosed", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privRecordCountDeleted", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);

if (!(isPurelyPrivate)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_pubRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
// ### IF IVK ###
}
if (!(isPurelyPrivate & ! condenseData)) {
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);

if ((!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) &  (ahClassIndex > 0)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_inLrt", M01_Globals.g_dbtOid, "0", null, null);
}

M11_LRT.genVarDecl(fileNoTrigger, "v_oidCount", "INTEGER", "0", null, null);

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex &  M72_DataPool.poolSupportLrt) {
M11_LRT.genVarDecl(fileNoTrigger, "v_isFtoLrt", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_entityLabel", "VARCHAR(90)", "'" + M04_Utilities.getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) + "'", null, null);
if (!(busKeyAttrListNoFks.compareTo("") == 0) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & !forNl) {
M11_LRT.genVarDecl(fileNoTrigger, "v_busKeyValues", "VARCHAR(200)", "NULL", null, null);
}
}
}

M11_LRT.genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", String.valueOf(M11_LRT.lrtStatusUpdated), null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0", null, null);

// ### IF IVK ###
if (isGenericAspectHead) {
M11_LRT.genVarDecl(fileNoTrigger, "v_logRecordOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
}

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
thisEntityTabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, thisEntityTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomXref, null);

numVirtualAttrs = 0;
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M11_LRT.genVarDecl(fileNoTrigger, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, M25_Domain.getDbDatatypeByDomainIndex(thisEntityTabColumns.descriptors[k].dbDomainIndex), "NULL", null, null);
numVirtualAttrs = numVirtualAttrs + 1;
}
}
}

if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
// note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
M11_LRT.genPsCheckDdlForUpdate(fileNoTrigger, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, psTagOptional, 1, false, null, null, null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
} else if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) {
M11_LRT.genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, null, false, null, null);
}

// ### ENDIF IVK ###
M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine LRT OID", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET v_lrtOid = (CASE " + M01_LDM.gc_db2RegVarLrtOid + " WHEN '' THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals.g_activeLrtOidDdl + " END);");

// ### IF IVK ###
if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
printedHeader = false;
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if (!(printedHeader)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "initialize variables for virtual attributes", 1, null);
printedHeader = true;
}
if (M03_Config.maintainVirtAttrInTriggerPubOnEntityTabs &  M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = " + M01_Globals.gc_oldRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + ";");
} else if (M03_Config.maintainVirtAttrInTriggerPubOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = (CASE WHEN v_lrtOid IS NULL THEN " + M01_Globals.gc_oldRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " ELSE " + M01_Globals.gc_newRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " END);");
} else if (M03_Config.maintainVirtAttrInTriggerPrivOnEntityTabs) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET " + "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName + " = (CASE WHEN v_lrtOid IS NOT NULL THEN " + M01_Globals.gc_oldRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " ELSE " + M01_Globals.gc_newRecordName + "." + thisEntityTabColumns.descriptors[k].acmAttributeName.toUpperCase() + " END);");
}
}
}
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, update in public table", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "IF (v_lrtOid IS NULL) THEN");

if (isPurelyPrivate) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, true);
// ### IF IVK ###
} else if (condenseData) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "not supported - table does not support 'update in public'", 2, true);
// ### ENDIF IVK ###
} else {
int indentOffset;
indentOffset = 0;

// ### IF IVK ###
// GenericAspects always need special treatment ;-)
if (isGenericAspectHead & ! M03_Config.generateFwkTest) {

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF");

genericAspectTabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conStatusId, "", null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conIsBlockedPrice, "", null, null, false);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, false);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "", null, null, false);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "", null, null, false);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, genericAspectTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, null, null, M01_Common.DdlOutputMode.edomNone, null);

String invalidValue;
String colValueDescr;
int colValueDescrLen;
String spaces;
spaces = M00_Helper.space(41);
for (int k = 1; k <= genericAspectTabColumns.numDescriptors; k++) {
if ((genericAspectTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacExpression) == 0) {
invalidValue = "XX";
if ((M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etChar |  M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etClob | M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etLongVarchar | M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etVarchar)) {
invalidValue = "''";
} else if ((M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etBigInt |  M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etSmallint | M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etInteger | M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etBoolean)) {
invalidValue = "-1";
} else if ((M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etDate)) {
invalidValue = "DATE('0001-01-01')";
} else if ((M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etDecimal)) {
invalidValue = "DECIMAL(-0.000000001)";
} else {
M04_Utilities.logMsg("data type \"" + M25_Domain.g_domains.descriptors[genericAspectTabColumns.descriptors[k].dbDomainIndex].dataType + "\" not (yet) supported in LRT-update-trigger", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}
colValueDescr = genericAspectTabColumns.descriptors[k].columnName + "," + invalidValue;
colValueDescrLen = colValueDescr.length();
if (colValueDescrLen < 40) {
colValueDescrLen = 40;
}


M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(COALESCE(" + M01_Globals.gc_newRecordName + "." + colValueDescr + ")" + spaces.substring(0, colValueDescrLen + 1) + " <>" + " COALESCE(" + M01_Globals.gc_oldRecordName + "." + colValueDescr + ")" + spaces.substring(0, colValueDescrLen + 1) + ")" + (k == genericAspectTabColumns.numDescriptors ? "" : " OR"));
}
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "THEN");
indentOffset = 1;
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "verify that record is not locked", 2 + indentOffset, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2 + indentOffset) + "SET v_oidCount =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3 + indentOffset) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4 + indentOffset) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5 + indentOffset) + "COUNT(*)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4 + indentOffset) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5 + indentOffset) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4 + indentOffset) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5 + indentOffset) + "PUB." + M01_Globals.g_anInLrt + " IS NOT NULL");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6 + indentOffset) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5 + indentOffset) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5 + indentOffset, "PUB", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3 + indentOffset) + ");");
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2 + indentOffset) + "IF v_oidCount > 0 THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 3 + indentOffset, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2 + indentOffset) + "END IF;");

// ### IF IVK ###
if (isGenericAspectHead & ! M03_Config.generateFwkTest) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
}

if (isGenericAspectHead & ! M03_Config.generateFwkTest) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "determine division OID", 2, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_divisionOid =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + M01_Globals.g_anOid + " = v_psOid");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");

M11_LRT.genProcSectionHeader(fileNoTrigger, "create changelog entry if " + M01_Globals.g_anStatus + " or " + M01_Globals_IVK.g_anIsBlockedPrice + " has changed", 2, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF (" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anStatus + "" + " <> " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anStatus + ") OR");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "   (" + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice + " <> " + M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice + ") THEN");

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine current user id (for changelog)", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'MIG_NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anStatus + "" + " <> " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anStatus + " THEN");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_logRecordOid = NEXTVAL FOR " + qualSeqNameOid + ";");

M12_ChangeLog.genGenChangeLogRecordDdl(acmEntityIndex, acmEntityType, qualTabNamePub, qualTabNamePriv, qualSeqNameOid, qualTabNameChangeLog, "update of '" + M01_Globals.g_anStatus + "'", "", thisOrgIndex, thisPoolIndex, fileNoTrigger, ddlType, null, null, M01_Globals.g_anStatus, null, M01_Common.typeId.etSmallint, M12_ChangeLog.ChangeLogMode.eclPubUpdate, M01_Common.AttrCategory.eacSetProdMeta, 3, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anStatus, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anStatus, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid, "v_logRecordOid", "v_cdUserId", String.valueOf(M11_LRT.lrtStatusUpdated), null, null, "v_divisionOid", M01_Globals_IVK.g_classIndexCodePriceAssignment);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice + " <> " + M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice + " THEN");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_logRecordOid = NEXTVAL FOR " + qualSeqNameOid + ";");

M12_ChangeLog.genGenChangeLogRecordDdl(acmEntityIndex, acmEntityType, qualTabNamePub, qualTabNamePriv, qualSeqNameOid, qualTabNameChangeLog, "update of '" + M01_Globals_IVK.g_anIsBlockedPrice + "'", "", thisOrgIndex, thisPoolIndex, fileNoTrigger, ddlType, null, null, M01_Globals_IVK.g_anIsBlockedPrice, null, M01_Common.typeId.etBoolean, M12_ChangeLog.ChangeLogMode.eclPubUpdate, M01_Common.AttrCategory.eacRegular, 3, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anIsBlockedPrice, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid, "v_logRecordOid", "v_cdUserId", String.valueOf(M11_LRT.lrtStatusUpdated), null, null, "v_divisionOid", M01_Globals_IVK.g_classIndexCodePriceAssignment);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "update record in public table", 2, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt
//       Else
//         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 5 + numVirtualAttrs, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 3, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anInLrt, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anCreateTimestamp, null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", v_now)", null, null, true);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conHasBeenSetProductive, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anHasBeenSetProductive, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
nextTrIndex = 6;

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors[k].columnName, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//
//       If forNl Then
//         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
//       Else
//         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomDefaultValue
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 3, "PUB", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ";");
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "ELSE");

M11_LRT.genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, true);
// ### IF IVK ###
M11_LRT.genStatusCheckDdl(fileNoTrigger, M01_Globals.gc_newRecordName, null, 2);
// ### ENDIF IVK ###

if (!(isPurelyPrivate)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_newRecordName + " is an update of a 'public record' (v_pubRecordExists = 1)", 2, null);
// ### IF IVK ###
if (condenseData) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF EXISTS(SELECT 1 FROM " + qualTabNamePub + " PUB WHERE PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + ") THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_pubRecordExists = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET v_pubRecordExists = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PUB." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);");
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_newRecordName + " corresponds to a 'private record' (v_privRecordExists = 1)", 2, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PRIV." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PRIV", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordExists = 1 THEN");
M11_LRT.genProcSectionHeader(fileNoTrigger, "check if the 'private record' is marked 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]'", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET (v_privRecordCountDeleted) =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "COUNT(*)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "(PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "(PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M11_LRT.genDdlPsDivClause(fileNoTrigger, 6, "PRIV", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordCountDeleted > 0 THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 3, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

// ### IF IVK ###
if (condenseData) {
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_pubRecordExists = 1 THEN");
M79_Err.genSignalDdl("pubUpdateNotAllowed", fileNoTrigger, 3, entityName, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
}

if (!(isPurelyPrivate & ! condenseData)) {
// ### ELSE IVK ###
//     If Not isPurelyPrivate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_pubRecordExists = 1 THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- check if this record is locked by some LRT other than this one");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF NOT ((v_pubOwnerId IS NULL) OR (v_pubOwnerId = v_lrtOid)) THEN");

// ### IF IVK ###
if (thisOrgIndex != M01_Globals.g_primaryOrgIndex &  M72_DataPool.poolSupportLrt) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "determine whether this LRT is a FACTORYTAKEOVER-LRT", 4, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET v_isFtoLrt = COALESCE((SELECT " + M01_Globals_IVK.g_anIsCentralDataTransfer + " FROM " + qualTabNameLrt + " WHERE " + M01_Globals.g_anOid + " = v_lrtOid), 0);");

M11_LRT.genProcSectionHeader(fileNoTrigger, "create a 'business error message' if this LRT is FACTORYTAKEOVER", 4, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "IF v_isFtoLrt = 1 THEN");

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine entityLabel", 5, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SET v_entityLabel = RTRIM(LEFT(COALESCE((");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_anAcmEntityLabel);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "INNER JOIN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityId + " = " + (hasOwnTable ? "'" + entityIdStr + "'" : M01_Globals.gc_oldRecordName + "." + M01_ACM.conClassId));
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "ORDER BY");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "(CASE ENL." + M01_Globals.g_anLanguageId + " WHEN " + M01_Globals_IVK.gc_langIdEnglish + " THEN 0 ELSE ENL." + M01_Globals.g_anLanguageId + " END) ASC");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "FETCH FIRST 1 ROW ONLY), '" + M04_Utilities.getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) + "'), " + String.valueOf(33 - (busKeyAttrListNoFks.compareTo("") == 0 ? 3 : busKeyAttrListNoFks.length()) - (forNl |  forGen ? 3 : 0) - (forGen ? 1 : 0) - (forNl ? 1 : 0)) + ")" +  + (forGen |  forNl ? " || ' (" + (forGen ? "G" : "") + (forNl ? "N" : "") + ")'" : "") + ");");

if (!(busKeyAttrListNoFks.compareTo("") == 0) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & !forNl) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "concatenate business key values for error message", 5, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SET v_busKeyValues =");
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + M01_Globals.gc_oldRecordName + "." + busKeyAttrArrayNoFks[i] + (i < M00_Helper.uBound(busKeyAttrArrayNoFks) ? " || ',' ||" : ""));
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + ";");

M11_LRT.genProcSectionHeader(fileNoTrigger, "signal eror message", 5, null);
M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, busKeyAttrListNoFks, null, null, null, null, null, null, null, null, "v_entityLabel", "v_busKeyValues", null, null);
} else {
M11_LRT.genProcSectionHeader(fileNoTrigger, "signal eror message", 5, null);
M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, M01_Globals.g_anOid, null, null, null, null, null, null, null, null, "v_entityLabel", "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "ELSE");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 5, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "END IF;");
} else {
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 4, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNoTrigger, "lock the 'public record' with this LRT-OID", 3, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF (v_pubOwnerId IS NULL) OR (v_pubOwnerId <> v_lrtOid) THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0) &  ahClassIndex > 0) {
// lock the 'public aggregate head record' with this LRT-OID
M11_LRT.genAggHeadLockPropDdl(fileNoTrigger, M01_Globals.gc_newRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)), useDivOidWhereClause, useDivRelKey);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
}

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordExists = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- private record does not exist; thus consider " + M01_Globals.gc_newRecordName + " as new 'private record'");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//     If forNl Then
//       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
//     Else
//       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 7 + numVirtualAttrs, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "" + M11_LRT.lrtStatusUpdated, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anCreateTimestamp, null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", v_now)", null, null, true);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conHasBeenSetProductive, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anHasBeenSetProductive, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
nextTrIndex = 8;

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors[k].columnName, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//
//     If forNl Then
//       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue
//     Else
//       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- private record exists - check if it is locked by some LRT other than this one");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF NOT (v_privOwnerId = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt + ") THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 4, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, "");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- now update private record with values in " + M01_Globals.gc_newRecordName + "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//     If forNl Then
//       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
//     Else
//       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "=");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 7 + numVirtualAttrs, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "(CASE WHEN " + M01_Globals.g_anLrtState + " = " + M11_LRT.lrtStatusLocked + " THEN " + M11_LRT.lrtStatusUpdated + " ELSE " + M01_Globals.g_anLrtState + " END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anCreateTimestamp, null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", v_now)", null, null, true);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conHasBeenSetProductive, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anHasBeenSetProductive, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conStatusId, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anStatus + ", " + M86_SetProductive.statusWorkInProgress + ")", null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
nextTrIndex = 8;

if (hasExpBasedVirtualAttrs &  M03_Config.maintainVirtAttrInTriggerOnEntityTabs) {
for (int k = 1; k <= thisEntityTabColumns.numDescriptors; k++) {
if (thisEntityTabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
M24_Attribute_Utilities.setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors[k].columnName, "v_" + thisEntityTabColumns.descriptors[k].acmAttributeName, null, null, null);
nextTrIndex = nextTrIndex + 1;
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDefaultValue | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//
//     If forNl Then
//       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue
//     Else
//       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue
//     End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 4, "PRIV", "", M01_Globals.gc_newRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

// ### IF IVK ###
M11_LRT.genDdlForUpdateAffectedEntities(fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, entityIdStr, ahClassIdStr, "v_lrtOid", 2, null, !(condenseData));
// ### ELSEIF IVK ###
//     genDdlForUpdateAffectedEntities fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
//       entityIdStr, ahClassIdStr, "v_lrtOid", 2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, (forNl ? "NL" : "") + (forMqt ? "M" : "") + nameSuffix + "LRT_DEL", null, null);
// ### ELSEIF IVK ###
//   qualTriggerName = _
//     genQualTriggerNameByEntityIndex( _
//       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "DEL" _
//     )
// ### ENDIF IVK ###

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolSupportLrt) {
M22_Class_Utilities.printSectionHeader("Delete-Trigger for LRT-emulating view on table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityDeletable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
} else if (isPurelyPrivate) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
// ### IF IVK ###
} else if (condenseData) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);

// note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
M11_LRT.genPsCheckDdlForInsertDelete(fileNoTrigger, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, psTagOptional, null, false, null, null, "", null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
}

// ### ENDIF IVK ###
M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvFalse, 1);

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 2, "", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + ";");
}
M00_FileWriter.printToFile(fileNoTrigger, "END");
M00_FileWriter.printToFile(fileNoTrigger, M01_LDM.gc_sqlCmdDelim);
} else if (!((isPurelyPrivate &  forMqt))) {
M22_Class_Utilities.printSectionHeader("Delete-Trigger for 'public-private' LRT-view \"" + qualViewName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoTrigger, null, null);

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

// ### IF IVK ###
if (condenseData) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName, null, null, null, null, null, null, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoTrigger, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNoTrigger, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtClosed", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privRecordCountDeleted", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_privOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);

if (!(isPurelyPrivate)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_pubRecordExists", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerId", M01_Globals.g_dbtLrtId, "NULL", null, null);

if ((!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) &  (ahClassIndex > 0)) {
M11_LRT.genVarDecl(fileNoTrigger, "v_pubOwnerUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_inLrt", M01_Globals.g_dbtOid, "0", null, null);
}

M11_LRT.genVarDecl(fileNoTrigger, "v_oidCount", "INTEGER", "0", null, null);

if (thisOrgIndex != M01_Globals.g_primaryOrgIndex &  M72_DataPool.poolSupportLrt) {
M11_LRT.genVarDecl(fileNoTrigger, "v_isFtoLrt", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_entityLabel", "VARCHAR(90)", "'" + M04_Utilities.getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) + "'", null, null);
if (!(busKeyAttrListNoFks.compareTo("") == 0) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & !forNl) {
M11_LRT.genVarDecl(fileNoTrigger, "v_busKeyValues", "VARCHAR(200)", "NULL", null, null);
}
}
}

M11_LRT.genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", String.valueOf(M11_LRT.lrtStatusDeleted), null, null);
M11_LRT.genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0", null, null);

// ### IF IVK ###
if (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)) {
// note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
M11_LRT.genPsCheckDdlForInsertDelete(fileNoTrigger, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, psTagOptional, null, false, null, null, (qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0 ? "" : "v_psOid"), null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
} else if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0)) {
M11_LRT.genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, null, false, null, null);
}

// ### ENDIF IVK ###
M11_LRT.genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "-- determine LRT OID");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "SET v_lrtOid = (CASE " + M01_LDM.gc_db2RegVarLrtOid + " WHEN '' THEN CAST(NULL AS " + M01_Globals.g_dbtOid + ") ELSE " + M01_Globals.g_activeLrtOidDdl + " END);");

M11_LRT.genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, delete in public table", null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "IF v_lrtOid IS NULL THEN");

if (isPurelyPrivate) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, true);
} else {
M11_LRT.genProcSectionHeader(fileNoTrigger, "verify that record is not locked", 2, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_oidCount =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COUNT(*)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anInLrt + " IS NOT NULL");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_oidCount > 0 THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtDelLocked", fileNoTrigger, 3, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 3, "", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + ";");
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "ELSE");

M11_LRT.genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, true);

if (!(isPurelyPrivate)) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_oldRecordName + " refers to a 'public record' (v_pubRecordExists = 1)", 2, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PUB." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);");
}

M11_LRT.genProcSectionHeader(fileNoTrigger, "check if " + M01_Globals.gc_oldRecordName + " corresponds to a 'private record' (v_privRecordExists = 1)", 2, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = NULL;");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "COALESCE(PRIV." + M01_Globals.g_anInLrt + ",-1)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PRIV", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);");

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordExists = 1 THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- check if the 'private record' is marked 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]'");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET (v_privRecordCountDeleted) =");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "COUNT(*)");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "(PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "(PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M11_LRT.genDdlPsDivClause(fileNoTrigger, 6, "PRIV", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordCountDeleted > 0 THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- should we allow to delete an already deleted record?");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtDelAlreadyDel", fileNoTrigger, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

if (!(isPurelyPrivate)) {
M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_pubRecordExists = 1 THEN");
M11_LRT.genProcSectionHeader(fileNoTrigger, "check if this record is locked by some LRT other than this one", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF NOT ((v_pubOwnerId IS NULL) OR (v_pubOwnerId = v_lrtOid)) THEN");
// ### IF IVK ###
if (thisOrgIndex != M01_Globals.g_primaryOrgIndex &  M72_DataPool.poolSupportLrt) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "determine whether this LRT is a FACTORYTAKEOVER-LRT", 4, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET v_isFtoLrt = COALESCE((SELECT " + M01_Globals_IVK.g_anIsCentralDataTransfer + " FROM " + qualTabNameLrt + " WHERE " + M01_Globals.g_anOid + " = v_lrtOid), 0);");

M11_LRT.genProcSectionHeader(fileNoTrigger, "create a 'business error message' if this LRT is FACTORYTAKEOVER", 4, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "IF v_isFtoLrt = 1 THEN");

M11_LRT.genProcSectionHeader(fileNoTrigger, "determine entityLabel", 5, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SET v_entityLabel = RTRIM(LEFT(COALESCE((");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_anAcmEntityLabel);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "INNER JOIN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "ON");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anAcmEntityId + " = " + (hasOwnTable ? "'" + entityIdStr + "'" : M01_Globals.gc_oldRecordName + "." + M01_ACM.conClassId));
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "ORDER BY");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(7) + "(CASE ENL." + M01_Globals.g_anLanguageId + " WHEN " + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + " THEN 0 ELSE ENL." + M01_Globals.g_anLanguageId + " END) ASC");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + "FETCH FIRST 1 ROW ONLY), '" + M04_Utilities.getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) + "'), " + String.valueOf(33 - (busKeyAttrListNoFks.compareTo("") == 0 ? 3 : busKeyAttrListNoFks.length()) - (forNl |  forGen ? 3 : 0) - (forGen ? 1 : 0) - (forNl ? 1 : 0)) + ")" +  + (forGen |  forNl ? " || ' (" + (forGen ? "G" : "") + (forNl ? "N" : "") + ")'" : "") + ");");

if (!(busKeyAttrListNoFks.compareTo("") == 0) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & !forNl) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "concatenate business key values for error message", 5, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "SET v_busKeyValues =");
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(6) + M01_Globals.gc_oldRecordName + "." + busKeyAttrArrayNoFks[i] + (i < M00_Helper.uBound(busKeyAttrArrayNoFks) ? " || ',' ||" : ""));
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + ";");

M11_LRT.genProcSectionHeader(fileNoTrigger, "signal eror message", 5, null);
M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, busKeyAttrListNoFks, null, null, null, null, null, null, null, null, "v_entityLabel", "v_busKeyValues", null, null);
} else {
M11_LRT.genProcSectionHeader(fileNoTrigger, "signal eror message", 5, null);
M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, M01_Globals.g_anOid, null, null, null, null, null, null, null, null, "v_entityLabel", "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "ELSE");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 5, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "END IF;");
} else {
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 4, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
}
// ### ELSE IVK ###
//         genSignalDdlWithParmsForCompoundSql "lrtDelNotOwner", fileNoTrigger, 4, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNoTrigger, "lock the 'public record' with this LRT-OID", 3, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF (v_pubOwnerId IS NULL) OR (v_pubOwnerId <> v_lrtOid) THEN");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "SET");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 5, "PUB", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

if (!(qualTabNamePub.compareTo(qualTabNameAggHeadPub) == 0) &  ahClassIndex > 0) {
// lock the 'public aggregate head record' with this LRT-OID
M11_LRT.genAggHeadLockPropDdl(fileNoTrigger, M01_Globals.gc_oldRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl)), useDivOidWhereClause, useDivRelKey);
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");
}

M00_FileWriter.printToFile(fileNoTrigger, "");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "IF v_privRecordExists = " + M01_LDM.gc_dbFalse + " THEN");
M11_LRT.genProcSectionHeader(fileNoTrigger, "private record does not exist; thus copy " + M01_Globals.gc_oldRecordName + " as new 'private record' and mark it as 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]'", 3, true);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, null, null, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//       If forNl Then
//         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
//       Else
//         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, M01_Globals.gc_oldRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 3, , , , gc_oldRecordName & "."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusDeleted), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "v_now", null, null, true);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
}
// ### ELSE IVK ###
//
//       If forNl Then
//         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt
//       Else
//         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "-- private record exists - check if it is locked by some LRT other than this one");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "IF v_privOwnerId <> v_lrtOid THEN");
M79_Err.genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 4, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(" + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid + "))", null, null, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "END IF;");

if (isPurelyPrivate) {
M11_LRT.genProcSectionHeader(fileNoTrigger, "delete the private record", 3, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 4, "PRIV", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ";");
} else {
M11_LRT.genProcSectionHeader(fileNoTrigger, "mark 'private record' as 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]'", 3, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "SET");

if (!(forNl &  (logLastChange |  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship))) {
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + M01_Globals.g_anLastUpdateTimestamp + " = v_now,");
}
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusDeleted));

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M11_LRT.genDdlPsDivClause(fileNoTrigger, 4, "PRIV", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(3) + ";");
}

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genDdlForUpdateAffectedEntities(fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, entityIdStr, ahClassIdStr, "v_lrtOid", 2, null, null);

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType, null);
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNoTrigger, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
private static void genLrtSupportSpsForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

String sectionName;
String entityName;
String entityTypeDescr;
String entityShortName;
String ahClassIdStr;
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
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
boolean hasNoIdentity;
boolean hasNlAttributes;
boolean hasNlTable;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
boolean ignoreForChangelog;
boolean hasPriceAssignmentSubClass;
boolean hasPriceAssignmentAggHead;
boolean isSubjectToPreisDurchschuss;
String priceAssignmentSubClassIdList;
int aggHeadClassIndex;
String aggHeadShortClassName;
boolean isAggregateHead;
String busKeyAttrList;
String busKeyAttrListNoFks;
String[] busKeyAttrArray;
String[] busKeyAttrArrayNoFks;
boolean hasGroupIdAttrs;
int[] groupIdAttrIndexes;
boolean isGenericCode;
boolean isEndSlot;
boolean isTypeSpec;
boolean condenseData;
boolean useLrtCommitPreprocess;
boolean hasRelBasedVirtualAttrInGenInclSubClasses;
boolean hasRelBasedVirtualAttrInNonGenInclSubClasses;

//On Error GoTo ErrorExit 

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (thisPoolIndex < 1) {
return;
} else if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
return;
}
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;

hasPriceAssignmentSubClass = false;
hasPriceAssignmentAggHead = false;
priceAssignmentSubClassIdList = "";

busKeyAttrList = "";
busKeyAttrListNoFks = "";
useLrtCommitPreprocess = false;
hasRelBasedVirtualAttrInGenInclSubClasses = false;
hasRelBasedVirtualAttrInNonGenInclSubClasses = false;

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
String ukAttrDecls;
String pkAttrList;
String leftFkAttrs;
String rightFkAttrs;
boolean isPrimaryOrg;

isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
entityIdStrList = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
ahClassIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
if (M22_Class.g_classes.descriptors[acmEntityIndex].hasBusinessKey) {
busKeyAttrList = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, null);
busKeyAttrListNoFks = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, true);

M24_Attribute.genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks);
}
hasGroupIdAttrs = !(forNl & ! forGen & M22_Class.g_classes.descriptors[acmEntityIndex].hasGroupIdAttrInNonGenInclSubClasses);
if (hasGroupIdAttrs) {
groupIdAttrIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].groupIdAttrIndexesInclSubclasses;
}
hasRelBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInGenInclSubClasses;
hasRelBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInNonGenInclSubClasses;
useLrtCommitPreprocess = M22_Class.g_classes.descriptors[acmEntityIndex].useLrtCommitPreprocess & ! forGen & !forNl;
isGenericCode = M22_Class.g_classes.descriptors[acmEntityIndex].classIndex == M01_Globals_IVK.g_classIndexGenericCode & ! forGen & !forNl;
isEndSlot = M22_Class.g_classes.descriptors[acmEntityIndex].classIndex == M01_Globals_IVK.g_classIndexEndSlot;
isTypeSpec = M22_Class.g_classes.descriptors[acmEntityIndex].classIndex == M01_Globals_IVK.g_classIndexTypeSpec;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;

if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
hasOwnTable = true;
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
isAbstract = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
relRefs.numRefs = 0;
isGenForming = false;
hasNoIdentity = false;
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefsInclSubClassesWithRepeat;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass;
hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;

if (hasPriceAssignmentSubClass) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
}

sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
entityIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
ahClassIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = "R";
relRefs.numRefs = 0;
isGenForming = false;
hasNoIdentity = false;
hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = false;
isGenericCode = false;
hasPriceAssignmentAggHead = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}

hasGroupIdAttrs = false;
condenseData = false;

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, null, false, false, M01_Common.DdlOutputMode.edomNone, null);
busKeyAttrList = leftFkAttrs + "," + rightFkAttrs;

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);
} else {
return;
}

hasNlTable = hasNlAttributes |  (isAggregateHead & ! forGen & !forNl & !condenseData);

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

if (aggHeadClassIndex > 0) {
aggHeadShortClassName = M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName;
}

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePub;
String qualTabNamePriv;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, null, null);
} else {
qualTabNamePub = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, forNl, null, null, null);
}

String qualAggHeadTabName;
String qualAggHeadNlTabNamePriv;
String aggHeadFkAttrName;
qualAggHeadTabName = "";
qualAggHeadNlTabNamePriv = "";
if (aggHeadClassIndex > 0) {
qualAggHeadTabName = M04_Utilities.genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

qualAggHeadNlTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);

aggHeadFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName, null, null, null, null);
}

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

if (M03_Config.generateLrtSps) {
if (!(forNl)) {
if (useLrtCommitPreprocess & ! M03_Config.generateFwkTest) {
// ####################################################################################################################
// #    SP for prepocessing LRTCOMMIT
// ####################################################################################################################

String qualProcNameLrtCommitPreProc;
qualProcNameLrtCommitPreProc = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, M01_ACM.spnLrtCommitPreProc, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for preprocessing LRT for LRT-COMMIT on \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtCommitPreProc);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to preprocess");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the LRT's product structure");
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, true, "identifies the operation (insert, update, delete) to create the Log for");
M11_LRT.genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", true, "marks the commit timestamp of the LRT");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this commit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

if (hasRelBasedVirtualAttrInNonGenInclSubClasses |  hasRelBasedVirtualAttrInGenInclSubClasses) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN");

boolean colonMissing;
int relIndex;
Integer relNavDirection;
int sourceClassIndex;
int sourceOrParClassIndex;
int targetOrParClassIndex;
String virtAttrlist;
boolean forTvColumns;
boolean updateFromPriv;
int offset;
int j;
for (int j = (hasRelBasedVirtualAttrInNonGenInclSubClasses ? 1 : 2); j <= (hasRelBasedVirtualAttrInGenInclSubClasses ? 2 : 1); j++) {
forTvColumns = (j == 2);
M11_LRT.genProcSectionHeader(fileNo, "instantiate virtual attributes" + (forTvColumns ? " (GEN)" : ""), 2, !(forTvColumns));

virtAttrlist = "";
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forTvColumns) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
virtAttrlist = virtAttrlist + ", " + M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].dbColName[ddlType];

// todo: this only works as long as all virtual columns in a table refer to the same reference-table
relIndex = M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].virtuallyMapsTo.relIndex;
relNavDirection = M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].virtuallyMapsTo.navDirection;
sourceClassIndex = M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].acmEntityIndex;
sourceOrParClassIndex = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].acmEntityIndex].orMappingSuperClassIndex;
targetOrParClassIndex = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].virtuallyMapsTo.targetClassIndex].orMappingSuperClassIndex;
}
}
}

String fkAttrName;
fkAttrName = (relNavDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[relIndex].rightFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[relIndex].leftFkColName[ddlType]);

if (M22_Class.g_classes.descriptors[sourceClassIndex].hasOwnTable) {
offset = 0;
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF EXISTS (SELECT 1 FROM ");
if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, true, null, null, null, null, null));
} else {
M00_FileWriter.printToFile(fileNo, qualTabNamePriv);
}
M00_FileWriter.printToFile(fileNo, " WHERE " + M01_Globals.g_anCid + " IN (" + M22_Class.g_classes.descriptors[sourceClassIndex].subclassIdStrListNonAbstract + ")) THEN");
offset = 1;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "UPDATE");
if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, true, null, null, null, null, null) + " T");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualTabNamePriv + " T");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "(");

colonMissing = false;
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forTvColumns) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
if (colonMissing) {
M00_FileWriter.printToFile(fileNo, ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].dbColName[ddlType]);
colonMissing = true;
}
}
}
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");

colonMissing = false;
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forTvColumns) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
if (colonMissing) {
M00_FileWriter.printToFile(fileNo, ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "COALESCE(T." + M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].dbColName[ddlType] + ", S." + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].virtuallyMapsTo.mapTo + (M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum ? M01_Globals.gc_enumAttrNameSuffix : ""), ddlType, null, null, null, null, null, null) + ")");
colonMissing = true;
}
}
}
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + virtAttrlist + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + virtAttrlist + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ") S");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INNER JOIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ") TPar");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "TPar." + fkAttrName + " = S." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + virtAttrlist + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + virtAttrlist + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ") S");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");

if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "T." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + " = TPar." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "T." + fkAttrName + " = S." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "T." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "T." + M01_Globals.g_anLrtState + " = opId_in");

if (!(M22_Class.g_classes.descriptors[sourceClassIndex].hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "T." + M01_Globals.g_anCid + " IN (" + M22_Class.g_classes.descriptors[sourceClassIndex].subclassIdStrListNonAbstract + ")");
}
if (M22_Class.g_classes.descriptors[sourceClassIndex].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "T." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "FROM");

if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + ", " + M01_Globals_IVK.g_anValidFrom + ", " + M01_Globals_IVK.g_anValidTo + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "SELECT " + M01_Globals.g_anOid + ", " + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + ", " + M01_Globals_IVK.g_anValidFrom + ", " + M01_Globals_IVK.g_anValidTo + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ") S");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "INNER JOIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 7) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + ") TPar");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "TPar." + fkAttrName + " = S." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SELECT " + M01_Globals.g_anOid + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " <> lrtOid_in AND " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 7) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "SELECT " + M01_Globals.g_anOid + " FROM " + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anInLrt + " = lrtOid_in AND " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + (M22_Class.g_classes.descriptors[sourceOrParClassIndex].isPsTagged ? " AND " + M01_Globals_IVK.g_anPsOid + " = psOid_in" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + ") S");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "WHERE");

if (forTvColumns) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "T." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + " = TPar." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "S." + M01_Globals_IVK.g_anValidFrom + " <= T." + M01_Globals_IVK.g_anValidFrom);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "S." + M01_Globals_IVK.g_anValidTo + " >= T." + M01_Globals_IVK.g_anValidFrom);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "T." + fkAttrName + " = S." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ";");

if (!(M22_Class.g_classes.descriptors[sourceClassIndex].hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (isEndSlot) {
String qualTabNameAggregationSlotPriv;
qualTabNameAggregationSlotPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "if a EndSlot exists for this LRT with a changed ASL reference", 2, true);
M11_LRT.genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced ASL is deleted", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " AS u_esl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "u_esl.esrasl_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "u_esl." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl_l." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " AS esl_l");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " AS esl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl_l." + M01_Globals.g_anOid + " = esl." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameAggregationSlotPriv + " AS asl_l");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl.esrasl_oid = asl_l." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl_l." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl_l.lrtstate = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl_l." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "esl." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "asl_l.lrtstate = " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

}

if (isTypeSpec) {
String qualTabNameTypeSpecPriv;
qualTabNameTypeSpecPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);
String qualTabNameTypeSpecPub;
qualTabNameTypeSpecPub = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex, null, false, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "if a TypeSpec exists for this LRT with a changed TPA reference", 2, true);
M11_LRT.genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced TPA is deleted", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTypeSpecPub + " TS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TS.TSTTPA_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TS." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTypeSpecPriv + " TSL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTypeSpecPub + " TSP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anOid + " = TSP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL.LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSP." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSP.TSTTPA_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "TSL.TSTTPA_OID <> TSP.TSTTPA_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "TSL.TSTTPA_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if a TypeSpec exists for this LRT with a changed previousTypeSpec reference", 2, true);
M11_LRT.genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced TypeSpec is deleted", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTypeSpecPub + " TS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TS.PTYPTY_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TS." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTypeSpecPriv + " TSL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTypeSpecPub + " TSP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anOid + " = TSP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL.LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSP." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TSP.PTYPTY_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "TSL.PTYPTY_OID <> TSP.PTYPTY_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "TSL.PTYPTY_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

}

if (isGenericCode) {
if (isPrimaryOrg) {
// special treetment for GENERICCODE: for a newly inserted / deleted GENERICCODE its association(s) to CATEGORY must replicated to each PRODUCTSTRUCTURE
String qualTabNameCodeCategoryPriv;
qualTabNameCodeCategoryPriv = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null);
String qualTabNameCodeCategoryPub;
qualTabNameCodeCategoryPub = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, null);
String qualTabNameGenericCodePriv;
qualTabNameGenericCodePriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);
String qualTabNameCategory;
qualTabNameCategory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN");

M11_LRT.genProcSectionHeader(fileNo, "create CODE <-> temporary CATEGORY relationships for newly created GENERICCODE and all ProductStructures", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategoryPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 12, null, null, null, "GC.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conDpClassNumber, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, "GCO_OID", "GC." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, "CAT_OID", "CA." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "PS." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conCreateTimestamp, "commitTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conLastUpdateTimestamp, "commitTs_in", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericCodePriv + " GC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDivision + " DV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC.CDIDIV_OID = DV." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS.PDIDIV_OID = DV." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCategory + " CA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CA." + M01_Globals_IVK.g_anPsOid + " = PS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CA." + M01_Globals_IVK.g_anIsDefault + " = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC.LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameCodeCategoryPriv + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GC." + M01_Globals.g_anOid + " = CC.GCO_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PS." + M01_Globals.g_anOid + " = CC." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GC." + M01_Globals.g_anInLrt + " = CC." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, "ACM-Relationship", M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, "R", false, false, qualTabNameLrtAffectedEntity, M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].relIdStr, M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].aggHeadClassIdStr, "lrtOid_in", 2, String.valueOf(M11_LRT.lrtStatusCreated), false);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "delete CODE <-> temporary CATEGORY relationships for deleted GENERICCODE and all ProductStructures", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategoryPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, "CC.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusDeleted), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericCodePriv + " GC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategoryPub + " CC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC." + M01_Globals.g_anOid + " = CC.GCO_OID");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC.LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GC." + M01_Globals.g_anInLrt + " = lrtOid_in");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameCodeCategoryPriv + " CCPRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CCPRIV." + M01_Globals.g_anOid + " = CC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CCPRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "lock public records", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategoryPub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameCodeCategoryPriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PUB." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, "ACM-Relationship", M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, "R", false, false, qualTabNameLrtAffectedEntity, M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].relIdStr, M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].aggHeadClassIdStr, "lrtOid_in", 2, String.valueOf(M11_LRT.lrtStatusDeleted), false);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

} else {
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "pre-processing is only done at factory side", 1, true);
}
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommitPreProc, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}
}

genLrtSupportSpsForEntity2(acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genLrtSupportSpsForEntity2(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

// ### ELSE IVK ###
//Private Sub genLrtSupportSpsForEntity( _
// ByRef acmEntityIndex As Integer, _
// ByRef acmEntityType As AcmAttrContainerType, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// fileNo As Integer, _
// fileNoClView As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forNl As Boolean = False _
//)
// ### ENDIF IVK ###
String sectionName;
String sectionShortName;
int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
String ahClassIdStr;
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
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
boolean hasNlAttributes;
boolean hasNlTable;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
boolean ignoreForChangelog;
int aggHeadClassIndex;
String aggHeadShortClassName;
boolean isAggregateHead;
boolean implicitelyGenChangeComment;
String busKeyAttrList;
String busKeyAttrListNoFks;
String[] busKeyAttrArray;
String[] busKeyAttrArrayNoFks;
boolean useLrtCommitPreprocess;
String tmpClassId;

// ### IF IVK ###
boolean hasNoIdentity;
boolean hasPriceAssignmentSubClass;
boolean hasPriceAssignmentAggHead;
boolean isSubjectToPreisDurchschuss;
String priceAssignmentSubClassIdList;
boolean hasGroupIdAttrs;
int[] groupIdAttrIndexes;
boolean isGenericAspectHead;// GenericAspects always need special treatment ;-)
boolean isGenericCode;
boolean condenseData;

boolean hasRelBasedVirtualAttrInGenInclSubClasses;
boolean hasRelBasedVirtualAttrInNonGenInclSubClasses;
boolean isNationalizable;
boolean hasIsNationalInclSubClasses;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (thisPoolIndex < 1) {
return;
} else if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
return;
}
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;

busKeyAttrList = "";
busKeyAttrListNoFks = "";
useLrtCommitPreprocess = false;
// ### IF IVK ###
hasPriceAssignmentSubClass = false;
hasPriceAssignmentAggHead = false;
priceAssignmentSubClassIdList = "";
hasRelBasedVirtualAttrInGenInclSubClasses = false;
hasRelBasedVirtualAttrInNonGenInclSubClasses = false;
// ### ENDIF IVK ###

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
String ukAttrDecls;
String pkAttrList;
String leftFkAttrs;
String rightFkAttrs;
boolean isPrimaryOrg;

isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
entityIdStrList = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
ahClassIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
implicitelyGenChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].implicitelyGenChangeComment;
if (M22_Class.g_classes.descriptors[acmEntityIndex].hasBusinessKey) {
busKeyAttrList = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, null);
busKeyAttrListNoFks = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, true);

M24_Attribute.genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks);
}
useLrtCommitPreprocess = M22_Class.g_classes.descriptors[acmEntityIndex].useLrtCommitPreprocess & ! forGen & !forNl;
// ### IF IVK ###
hasGroupIdAttrs = !(forNl & ! forGen & M22_Class.g_classes.descriptors[acmEntityIndex].hasGroupIdAttrInNonGenInclSubClasses);
if (hasGroupIdAttrs) {
groupIdAttrIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].groupIdAttrIndexesInclSubclasses;
}
hasRelBasedVirtualAttrInGenInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInGenInclSubClasses;
hasRelBasedVirtualAttrInNonGenInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInNonGenInclSubClasses;
isGenericAspectHead = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == M01_ACM_IVK.clnGenericAspect.toUpperCase() & ! forGen & !forNl;
isGenericCode = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == M01_ACM_IVK.clnGenericCode.toUpperCase() & ! forGen & !forNl;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isNationalizable = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable;
hasIsNationalInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasIsNationalInclSubClasses & ! forNl;
// ### ENDIF IVK ###

if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
hasOwnTable = true;
isAbstract = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = false;
// ### ENDIF IVK ###
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefsInclSubClassesWithRepeat;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass;
hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;

if (hasPriceAssignmentSubClass) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
// ### ENDIF IVK ###
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
}

sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
entityIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
ahClassIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = "R";
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
hasNoIdentity = false;
// ### ENDIF IVK ###
hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = false;
// ### IF IVK ###
isGenericAspectHead = false;
isGenericCode = false;
hasPriceAssignmentAggHead = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}

hasGroupIdAttrs = false;
condenseData = false;
isNationalizable = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isNationalizable & ! M23_Relationship.g_relationships.descriptors[acmEntityIndex].isNl;
hasIsNationalInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasIsNationalInclSubClasses & ! M23_Relationship.g_relationships.descriptors[acmEntityIndex].isNl;
// ### ENDIF IVK ###

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, null, false, false, M01_Common.DdlOutputMode.edomNone, null);
busKeyAttrList = leftFkAttrs + "," + rightFkAttrs;

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);
} else {
return;
}

// ### IF IVK ###
hasNlTable = hasNlAttributes |  (isAggregateHead &  implicitelyGenChangeComment & !forGen & !forNl & !condenseData);
// ### ELSE IVK ###
// hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl)
// ### ENDIF IVK ###

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

if (aggHeadClassIndex > 0) {
aggHeadShortClassName = M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName;
}

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePub;
String qualTabNamePriv;
String qualTabNamePubNl;
String qualTabNamePrivNl;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, null, null, null, null);

if (hasNlTable) {
qualTabNamePubNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, null, null);
qualTabNamePrivNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, true, null, null, null);
}
} else {
qualTabNamePub = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null);

if (hasNlTable) {
qualTabNamePubNl = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, true, null, null, null);
qualTabNamePrivNl = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, true, null, null, null);
}
}

String qualAggHeadTabName;
String qualAggHeadNlTabNamePriv;
String aggHeadFkAttrName;
qualAggHeadTabName = "";
qualAggHeadNlTabNamePriv = "";
if (aggHeadClassIndex > 0) {
qualAggHeadTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggHeadClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualAggHeadNlTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggHeadClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);
aggHeadFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName, null, null, null, null);
}

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

boolean hasNlLabelAttr;
hasNlLabelAttr = false;
// ### IF IVK ###
boolean labelIsNationalizable;
labelIsNationalizable = false;
// ### ENDIF IVK ###

if (!(forNl)) {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, null, true, forGen, M01_Common.DdlOutputMode.edomNone, null);

int j;
for (int j = 1; j <= transformation.nlAttrRefs.numDescriptors; j++) {
if (transformation.nlAttrRefs.descriptors[j].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute) {
if (M24_Attribute.g_attributes.descriptors[transformation.nlAttrRefs.descriptors[j].refIndex].attributeName.toUpperCase() == "LABEL") {
hasNlLabelAttr = true;
// ### IF IVK ###
labelIsNationalizable = M24_Attribute.g_attributes.descriptors[transformation.nlAttrRefs.descriptors[j].refIndex].isNationalizable;
// ### ENDIF IVK ###
}
}
}
}

// ### IF IVK ###
boolean setManActConditional;
setManActConditional = !(isPrimaryOrg &  hasIsNationalInclSubClasses);

// ### ENDIF IVK ###
if (M03_Config.generateLrtSps) {
if (!(forNl)) {

// ####################################################################################################################
// #    SP for COMMIT on given class
// ####################################################################################################################

String qualProcNameLrtCommit;

qualProcNameLrtCommit = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, M01_ACM.spnLrtCommit, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-COMMIT on \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameLrtCommit);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to commit");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "ID of the user owning the LRT");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure corresponding to the LRT");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "lrtStatus_in", M01_Globals.g_dbtEnumId, true, "commit only records in this status (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])");
M11_LRT.genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", true, "marks the execution timestamp of the LRT");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "autoPriceSetProductive_in", M01_Globals.g_dbtBoolean, true, "specifies whether prices are set productive");
M11_LRT.genProcParm(fileNo, "IN", "settingManActCP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateCodePrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingManActTP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateTypePrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingManActSE_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateStandardEquipmentPrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingSelRelease_in", M01_Globals.g_dbtBoolean, true, "setting 'useSelectiveReleaseProcess'");

if (!(isPrimaryOrg)) {
M11_LRT.genProcParm(fileNo, "IN", "isFtoLrt_in", M01_Globals.g_dbtBoolean, true, "'1' if and only if this LRT 'is central data transfer'");
}
// ### ENDIF IVK ###

M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this commit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

// ### IF IVK ###
if (!(condenseData)) {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);

if (!(busKeyAttrList.compareTo("") == 0)) {
M79_Err.genSigMsgVarDecl(fileNo, null);
}
if (!(busKeyAttrListNoFks.compareTo("") == 0)) {
M11_LRT.genVarDecl(fileNo, "v_busKeyValues", "VARCHAR(200)", "NULL", null, null);
// ### IFNOT IVK ###
//       Dim i As Integer
// ### ENDIF IVK ###
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M11_LRT.genVarDecl(fileNo, "v_" + busKeyAttrArrayNoFks[i], "VARCHAR(40)", "NULL", null, null);
}
}
// ### IF IVK ###
if (!(condenseData)) {
if (M03_Config.maintainGroupIdColumnsInLrtCommit &  hasGroupIdAttrs) {
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);
} else {
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
//       genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0", null, null);
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);

// ### IF IVK ###
if (M03_Config.maintainGroupIdColumnsInLrtCommit &  hasGroupIdAttrs) {
String gidColShortName;
int k;
for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
gidColShortName = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName;
M11_LRT.genVarDecl(fileNo, "v_" + gidColShortName.toUpperCase(), "BIGINT", "NULL", null, null);
}
}

// ### ENDIF IVK ###
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

// ### IF IVK ###
if (!(condenseData)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, true, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for Public OIDs affected", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNamePubOidsAffected);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid                  " + M01_Globals.g_dbtOid + ",");
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId              " + M01_Globals.g_dbtEntityId + ",");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "statusId             " + M01_Globals.g_dbtEnumId + ",");
if (isGenericAspectHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "privStatusId         " + M01_Globals.g_dbtEnumId + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "hasBeenSetProductive " + M01_Globals.g_dbtBoolean);
// ### ELSE IVK ###
//       Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

if (hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + pc_tempTabNamePubOidsAffectedNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid                  " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "parOid               " + M01_Globals.g_dbtOid + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "hasBeenSetProductive " + M01_Globals.g_dbtBoolean);
// ### ELSE IVK ###
//         Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

// ### IF IVK ###
if (!(isPrimaryOrg)) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommit, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameLrtCommit, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
// ### ELSE IVK ###
//     genSpLogProcEnter fileNo, qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
// ### ENDIF IVK ###

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter 'rowCount_out'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

// ### IF IVK ###
if (hasPriceAssignmentSubClass) {
M11_LRT.genProcSectionHeader(fileNo, "take care of prices being set productive automatically (Preis-Durchschuss)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( lrtStatus_in <> " + String.valueOf(M11_LRT.lrtStatusLocked) + " ) AND ( autoPriceSetProductive_in = 1 ) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRegistryDynamic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anKey + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSubKey + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anValue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEXTVAL FOR " + qualSeqNameOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_PDM_IVK.gc_regDynamicSectionAutoSetProd + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_PDM_IVK.gc_regDynamicKeyAutoSetProd + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + new String ("00" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true)).substring(new String ("00" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true)).length() - 1 - 2) + "-' || RTRIM(CAST(lrtOid_in AS CHAR(40))),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(CAST(PRIV." + M01_Globals.g_anOid + " AS CHAR(40)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anLrtState + " = lrtStatus_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (!(condenseData)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine Public OIDs of affected entities", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( lrtStatus_in IN (" + String.valueOf(M11_LRT.lrtStatusLocked) + ", " + String.valueOf(M11_LRT.lrtStatusUpdated) + ", " + String.valueOf(M11_LRT.lrtStatusDeleted) + ")) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNamePubOidsAffected);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "statusId,");
if (isGenericAspectHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "privStatusId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isDeleted,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "hasBeenSetProductive");
// ### ELSE IVK ###
//       Print #fileNo, addTab(3); "oid"; IIf(hasOwnTable, "", ",")
//       If Not hasOwnTable Then
//         Print #fileNo, addTab(3); "classId"
//       End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + ",");
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anCid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anStatus + ",");
if (isGenericAspectHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anStatus + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anIsDeleted + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anHasBeenSetProductive);
// ### ELSE IVK ###
//       Print #fileNo, addTab(3); "PUB."; g_anOid; IIf(hasOwnTable, "", ",")
//       If Not hasOwnTable Then
//         Print #fileNo, addTab(3); "PUB."; g_anCid
//       End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + ") OR (PRIV." + M01_Globals.g_anLrtState + " = lrtStatus_in))");

// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

if (hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNamePubOidsAffectedNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "parOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isDeleted,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "hasBeenSetProductive");
// ### ELSE IVK ###
//         Print #fileNo, addTab(3); "parOid"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anIsDeleted + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anHasBeenSetProductive);
// ### ELSE IVK ###
//         Print #fileNo, addTab(3); "PUB."; genSurrogateKeyName(ddlType, entityShortName)
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePrivNl + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + ") OR (PRIV." + M01_Globals.g_anLrtState + " = lrtStatus_in))");

// ### IF IVK ###
if (isPsTagged &  M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

String multipleTableSuffix;
multipleTableSuffix = (hasNlTable ? "s" : "");

M11_LRT.genProcSectionHeader(fileNo, "propagate changes to public tables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + " ) THEN");

// ### IF IVK ###
if (!(condenseData)) {
M11_LRT.genProcSectionHeader(fileNo, "delete all rows in public table" + multipleTableSuffix + " related to this LRT marked as deleted and not being set productive", 2, true);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
if (hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM " + qualTabNamePubNl + " AS PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM " + qualTabNamePub + " AS PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "unlock ALL rows in public table" + multipleTableSuffix + " related to this LRT", 2, null);
if (hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE " + qualTabNamePubNl + " AS PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET PUBNL.INLRT = CAST(NULL AS BIGINT)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE " + qualTabNamePub + " AS PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET PUB.INLRT = CAST(NULL AS BIGINT)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
// ### IF IVK ###
}

M11_LRT.genProcSectionHeader(fileNo, "cleanup private table" + multipleTableSuffix, 2, condenseData);
// ### ELSE IVK ###
// ### INDENT IVK ### 0
//
//     genProcSectionHeader fileNo, "cleanup private table" & multipleTableSuffix, 2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
//CQDAT00027607: additional CodeCategory entries for foreign product structures must be removed
if (isPsTagged & ! (isPrimaryOrg &  acmEntityIndex == M01_Globals_IVK.g_relIndexCodeCategory & acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

if (hasNlTable) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePrivNl + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");

// ### IF IVK ###
String gidTabVar;
// ### ENDIF IVK ###
String crTabVar;
String sourceTabVar;
String mgidColName;
String subClassIdStrList;
// ### IF IVK ###
String qualSeqNameGroupId;
int expGroupIdColNo;
if (M03_Config.maintainGroupIdColumnsInLrtCommit &  hasGroupIdAttrs) {
int maxVarNameLength;
// Fixme: get rid of this hard-coding
maxVarNameLength = 22;

for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
M11_LRT.genProcSectionHeader(fileNo, "update group-ID column \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName.toUpperCase() + "\" in table \"" + qualTabNamePriv + "\"", 2, true);

gidTabVar = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName.toUpperCase();
crTabVar = "CR";
sourceTabVar = "PRIV";// UCase(entityShortName)
String gidColName;
gidColName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName, ddlType, null, null, null, null, null, null);
gidColShortName = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName;

subClassIdStrList = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].acmEntityIndex].subclassIdStrListNonAbstract;
qualSeqNameGroupId = M04_Utilities.genQualObjName(sectionIndex, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR gidLoop AS gidCursor CURSOR WITH HOLD FOR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.paddRight(crTabVar + "." + M01_Globals_IVK.g_anPsOid, maxVarNameLength, null) + " AS v_" + M01_Globals_IVK.g_anPsOid + ", ");
}

expGroupIdColNo = 0;
int l;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.paddRight(crTabVar + ".EXP_" + String.valueOf(expGroupIdColNo), maxVarNameLength, null) + " AS v_EXP_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.paddRight(crTabVar + "." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase(), maxVarNameLength, null) + " AS v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT DISTINCT");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + sourceTabVar + "." + M01_Globals_IVK.g_anPsOid + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l], thisOrgIndex, thisPoolIndex, ddlType, sourceTabVar, null, sourceTabVar + "." + M01_Globals.g_anInLrt) + " AS EXP_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + sourceTabVar + "." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNamePriv + " " + sourceTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + sourceTabVar + "." + M01_Globals.g_anCid + " IN (" + subClassIdStrList + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + sourceTabVar + "." + M01_Globals.g_anInLrt + " = lrtOid_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + sourceTabVar + "." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") AS " + crTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_" + gidColShortName.toUpperCase() + " = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + sourceTabVar + "." + gidColName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNamePriv + " " + sourceTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(" + sourceTabVar + "." + gidColName + " IS NOT NULL) AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(" + sourceTabVar + "." + M01_Globals.g_anInLrt + " = lrtOid_in) AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(" + sourceTabVar + "." + M01_Globals.g_anCid + " IN (" + subClassIdStrList + ")) AND");

// Fixme: get rid of this hard-coding
maxVarNameLength = 24;

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(" + sourceTabVar + "." + M01_Globals_IVK.g_anPsOid + " = v_" + M01_Globals_IVK.g_anPsOid + ") AND");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
String v1;
String v2;
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
v1 = M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l], thisOrgIndex, thisPoolIndex, ddlType, sourceTabVar, null, sourceTabVar + "." + M01_Globals.g_anInLrt);
v2 = "v_EXP" + "_" + String.valueOf(expGroupIdColNo);
} else {
v1 = M04_Utilities.paddRight(sourceTabVar + "." + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase(), maxVarNameLength, null);
v2 = M04_Utilities.paddRight("v_" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase(), maxVarNameLength, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(((" + v1 + " IS NULL) AND (" + v2 + " IS NULL)) OR (" + v1 + " = " + v2 + "))" + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? " AND" : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_" + gidColShortName.toUpperCase() + " IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_" + gidColShortName.toUpperCase() + " = NEXTVAL FOR " + qualSeqNameGroupId + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'UPDATE " + qualTabNamePriv + " " + entityShortName.toUpperCase() + " SET " + entityShortName.toUpperCase() + "." + gidColName + " = ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "                 RTRIM(CHAR(v_" + gidColShortName.toUpperCase() + ")) || ' WHERE ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "                 '(" + entityShortName.toUpperCase() + "." + gidColName + " IS NULL) AND ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "                 '(" + entityShortName.toUpperCase() + "." + M01_Globals.g_anCid + " IN (" + M00_Helper.replace(subClassIdStrList, "'", "''") + ")) AND ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "                 '(" + entityShortName + "." + M01_Globals.g_anInLrt + " = " + "' || RTRIM(CHAR(lrtOid_in)) || ') AND ' ||");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "                 '(" + entityShortName + "." + M01_Globals_IVK.g_anPsOid + " = " + "' || RTRIM(CHAR(v_" + M01_Globals_IVK.g_anPsOid + ")) || ') AND ' ||");
}

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, true, true);
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
String thisColumn;
thisColumn = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase();
String colCastPrefix;
String colCastPostfix;
colCastPrefix = "";
colCastPostfix = "";
int thisDomainIndex;
Integer thisDbType;

thisDbType = M01_Common.typeId.etNone;
if (thisColumn.compareTo(M01_Globals_IVK.g_anValidFrom) == 0 |  thisColumn.compareTo(M01_Globals_IVK.g_anValidTo) == 0) {
thisDbType = M01_Common.typeId.etDate;
} else {
int m;
for (int m = 1; m <= tabColumns.numDescriptors; m++) {
if (tabColumns.descriptors[m].columnName.compareTo(thisColumn) == 0) {
thisDbType = M25_Domain.g_domains.descriptors[tabColumns.descriptors[m].dbDomainIndex].dataType;
break;
}
}
}

if ((thisDbType == M01_Common.typeId.etChar |  thisDbType == M01_Common.typeId.etClob | thisDbType == M01_Common.typeId.etLongVarchar | thisDbType == M01_Common.typeId.etVarchar)) {
colCastPrefix = "''";
colCastPostfix = "''";
} else if (thisDbType == M01_Common.typeId.etDate) {
colCastPrefix = "DATE(''";
colCastPostfix = "'')";
} else if (thisDbType == M01_Common.typeId.etTime) {
colCastPrefix = "TIME(''";
colCastPostfix = "'')";
} else if (thisDbType == M01_Common.typeId.etTimestamp) {
colCastPrefix = "TIMESTAMP(''";
colCastPostfix = "'')";
}

if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
String thisColumnExpr;
String refColumnExpr;
expGroupIdColNo = expGroupIdColNo + 1;

thisColumnExpr = "v_EXP_" + String.valueOf(expGroupIdColNo);
refColumnExpr = M04_Utilities.mapExpression(thisColumn, thisOrgIndex, thisPoolIndex, ddlType, entityShortName, null, entityShortName + "." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + " (CASE WHEN " + thisColumnExpr + " IS NULL THEN '(" + refColumnExpr + " IS NULL)' " + "ELSE '(" + refColumnExpr + " = " + colCastPrefix + "' || RTRIM(REPLACE(CHAR(" + thisColumnExpr + "),'''',''''''))" + (colCastPostfix.compareTo("") == 0 ? "" : " || '" + colCastPostfix + "'") + " || ')'" + " END)" + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? " || ' AND ' ||" : ";"));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + " (CASE WHEN v_" + thisColumn + " IS NULL THEN '(" + entityShortName + "." + thisColumn + " IS NULL)' " + "ELSE '(" + entityShortName + "." + thisColumn + " = " + colCastPrefix + "' || RTRIM(REPLACE(CHAR(v_" + thisColumn + "),'''',''''''))" + (colCastPostfix.compareTo("") == 0 ? "" : " || '" + colCastPostfix + "'") + " || ')'" + " END)" + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? " || ' AND ' ||" : ";"));
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
}
M00_FileWriter.printToFile(fileNo, "");
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "CREATE: move all LRT-private 'new' records into public tables (INSERT)", 2, true);

if (!(busKeyAttrList.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, "verify that there is no conflict with some public record with respect to business key", 2, null);
M24_Attribute.genAttrList(busKeyAttrArray, busKeyAttrList);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNamePub + " PUB,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
// ### IF IVK ###
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIV." + M01_Globals.g_anLrtState.toUpperCase() + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");

for (int i = M00_Helper.lBound(busKeyAttrArray); i <= M00_Helper.uBound(busKeyAttrArray); i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + busKeyAttrArray[i].toUpperCase() + " = PRIV." + busKeyAttrArray[i].toUpperCase());
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") THEN");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  !(busKeyAttrListNoFks.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, "determine non-FK values violating business key", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CAST(RTRIM(CAST(" + busKeyAttrArrayNoFks[i].toUpperCase() + " AS CHAR(40))) AS VARCHAR(40))" + (i < M00_Helper.uBound(busKeyAttrArrayNoFks) ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_" + busKeyAttrArrayNoFks[i] + (i < M00_Helper.uBound(busKeyAttrArrayNoFks) ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
// ### IF IVK ###
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState.toUpperCase() + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");

for (int i = M00_Helper.lBound(busKeyAttrArray); i <= M00_Helper.uBound(busKeyAttrArray); i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + busKeyAttrArray[i].toUpperCase() + " = PRIV." + busKeyAttrArray[i].toUpperCase());
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FETCH FIRST 1 ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "concatenate business key values for error message", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_busKeyValues =");
for (int i = M00_Helper.lBound(busKeyAttrArrayNoFks); i <= M00_Helper.uBound(busKeyAttrArrayNoFks); i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + busKeyAttrArrayNoFks[i] + "=' || v_" + busKeyAttrArrayNoFks[i] + (i < M00_Helper.uBound(busKeyAttrArrayNoFks) ? " || ',' ||" : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

// ### IF IVK ###
if (!(isPrimaryOrg)) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
// ### ELSE IVK ###
//         genSpLogProcEscape fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "signal eror message", 4, null);
M79_Err.genSignalDdlWithParms("lrtCommitBusKeyViolation", fileNo, 4, M22_Class_Utilities_NL.getPrimaryClassLabelByIndex(acmEntityIndex), null, null, null, null, null, null, null, null, "COALESCE(v_busKeyValues,'<->')", null, null, null);
} else {
// ### IF IVK ###
if (!(isPrimaryOrg)) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommit, ddlType, -4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameLrtCommit, ddlType, -4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
// ### ELSE IVK ###
//         genSpLogProcEscape fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "signal eror message", 4, null);
M79_Err.genSignalDdlWithParms("lrtCommitBusKeyViolation", fileNo, 4, M04_Utilities.getUnqualObjName(qualTabNamePub), busKeyAttrList, null, null, null, null, null, null, null, "'" + busKeyAttrList + "'", null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 8, null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//     initAttributeTransformation transformation, 5, , , , "PRIV."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "commitTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "commitTs_in", null, null, null);

// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conIsDeleted, M01_LDM.gc_dbFalse, null, null, null);
if (isPrimaryOrg) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." + M01_Globals.g_anCreateUser + " ELSE cdUserId_in END)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." + M01_Globals.g_anUpdateUser + " ELSE cdUserId_in END)", null, null, null);
}

M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

if (aggHeadClassIndex > 0) {
tmpClassId = "PRIV." + M01_Globals.g_anAhCid;
} else {
tmpClassId = "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'";
}

M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (PRIV." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + tmpClassId + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (hasPriceAssignmentSubClass ? " END" : ""), null, null, null);
// ### ELSE IVK ###
//     setAttributeMapping transformation, 4, conCreateUser, "cdUserId_in"
//     setAttributeMapping transformation, 5, conUpdateUser, "cdUserId_in"
// ### ENDIF IVK ###

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "insert records into NL-table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 1, , , , "PRIV."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conIsDeleted, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

if (aggHeadClassIndex > 0) {
tmpClassId = "PRIV." + M01_Globals.g_anAhCid;
} else {
tmpClassId = "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'";
}

M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conStatusId, (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + tmpClassId + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (hasPriceAssignmentSubClass ? " END" : ""), null, null, null);
// ### ENDIF IVK ###

M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePrivNl + " PRIV");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePrivNl + " PRIV,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PAR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIV." + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null) + " = PAR." + M01_Globals.g_anOid);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " ) THEN");
// ### IF IVK ###
if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "UPDATE not supported for this table", 2, true);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "UPDATE: propagate all LRT-private modified records into public tables", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 8, null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 4, , , , "PRIV."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conLastUpdateTimestamp, "commitTs_in", null, null, null);

// ### IF IVK ###
if (isPrimaryOrg) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." + M01_Globals.g_anUpdateUser + " ELSE cdUserId_in END)", null, null, null);
}
// ### ELSE IVK ###
//       setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conIsDeleted, "PUB." + M01_Globals_IVK.g_anIsDeleted, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conHasBeenSetProductive, "PUB." + M01_Globals_IVK.g_anHasBeenSetProductive, null, null, null);

if (isPrimaryOrg) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conIsBlockedPrice, "PUB." + M01_Globals_IVK.g_anIsBlockedPrice, null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conIsBlockedPrice, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV.ISNATIONAL = 0) THEN PRIV." + M01_Globals_IVK.g_anIsBlockedPrice + " ELSE PUB." + M01_Globals_IVK.g_anIsBlockedPrice + " END)", null, null, null);
}


if (aggHeadClassIndex > 0) {
tmpClassId = "PRIV." + M01_Globals.g_anAhCid;
} else {
tmpClassId = "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'";
}

M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (PRIV." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + tmpClassId + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (hasPriceAssignmentSubClass ? " END" : ""), null, null, null);
// ### ENDIF IVK ###

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUB." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "update records in NL-table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//         initAttributeTransformation transformation, 1, , , , "PRIV."
// ### ENDIF IVK ###

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conIsDeleted, "PUB." + M01_Globals_IVK.g_anIsDeleted, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conHasBeenSetProductive, "PUB." + M01_Globals_IVK.g_anHasBeenSetProductive, null, null, null);

if (aggHeadClassIndex > 0) {
tmpClassId = "PRIV." + M01_Globals.g_anAhCid;
} else {
tmpClassId = "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'";
}

M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conStatusId, (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + tmpClassId + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (hasPriceAssignmentSubClass ? " END" : ""), null, null, null);
// ### ENDIF IVK ###

M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNamePrivNl + " PRIV");
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNamePub + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV." + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null) + " = PAR." + M01_Globals.g_anOid);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUB." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

}
// ### IF IVK ###

if (entityName.compareTo(M01_ACM_IVK.clnTypeSpec) == 0) {
String genericAspectTabName;
genericAspectTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGenericAspect, M01_ACM_IVK.clnGenericAspect, null), ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "if TPA is deleted, then the typespec must have the same status as the deleted TPA in order to avoid constraint violations during setproductive", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_ACM_IVK.conStatusId + " = (SELECT TPA." + M01_ACM_IVK.conStatusId + " FROM " + genericAspectTabName + " TPA WHERE PUB.TSTTPA_OID = TPA." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + genericAspectTabName + " AS TPA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB.TSTTPA_OID = TPA." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TPA." + M01_ACM_IVK.conIsDeleted + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TPA." + M01_ACM_IVK.conHasBeenSetProductive + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
}


if (isGenericAspectHead) {
// GenericAspect always requires some special treatment...
M11_LRT.genProcSectionHeader(fileNo, "propagate status to all aggregate children", 2, null);

for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes); i++) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isUserTransactional & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isCommonToOrgs & !M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isCommonToPools & M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].superClassIndex <= 0) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex != acmEntityIndex) {
// set status for base table
M11_LRT.genProcSectionHeader(fileNo, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "'", 2, true);
genDdlForAggStatusPropLrtCommit(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null), qualTabNamePriv, priceAssignmentSubClassIdList, fileNo, 2, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isPsTagged, "psOid_in");
}
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNlAttrsInNonGenInclSubClasses) {
// set status for NL-Text table
M11_LRT.genProcSectionHeader(fileNo, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (NL_TEXT)", 2, true);
genDdlForAggStatusPropLrtCommit(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null), qualTabNamePriv, priceAssignmentSubClassIdList, fileNo, 2, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isPsTagged, "psOid_in");
}

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isGenForming & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNoIdentity) {
// set status for GENtable
M11_LRT.genProcSectionHeader(fileNo, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (GEN)", 2, true);
genDdlForAggStatusPropLrtCommit(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null), qualTabNamePriv, priceAssignmentSubClassIdList, fileNo, 2, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isPsTagged, "psOid_in");

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].hasNlAttrsInGenInclSubClasses) {
// set status for NL-Text GEN-table
M11_LRT.genProcSectionHeader(fileNo, "propagate status to aggregate child class '" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].sectionName + "." + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].className + "' (GEN/NL_TEXT)", 2, true);
genDdlForAggStatusPropLrtCommit(M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null), qualTabNamePriv, priceAssignmentSubClassIdList, fileNo, 2, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildClassIndexes[i]].isPsTagged, "psOid_in");
}
}
}
}

for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes); i++) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isUserTransactional & ! M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isCommonToOrgs & !M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isCommonToPools) {
// set status for relationship table
M11_LRT.genProcSectionHeader(fileNo, "propagate status to aggregate child relationship '" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].sectionName + "." + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].relName + "'", 2, true);
genDdlForAggStatusPropLrtCommit(M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null), qualTabNamePriv, priceAssignmentSubClassIdList, fileNo, 2, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggChildRelIndexes[i]].isPsTagged, "psOid_in");
}
}
}
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF ( lrtStatus_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " ) THEN");

// ### IF IVK ###
if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "DELETE not supported for this table", 2, true);
} else {
if (M03_Config.lrtCommitDeleteDeletedNonProductiveRecords) {
M11_LRT.genProcSectionHeader(fileNo, "DELETE: delete records in public tables which are not 'set productive' and marked 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]' in LRT", 2, true);

// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "delete records in NL-table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " AS PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

if (acmEntityIndex == M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGenericCode, M01_ACM_IVK.clnGenericCode, null) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M11_LRT.genProcSectionHeader(fileNo, "delete records in 'codecategory'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null) + " AS PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anAhOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

M11_LRT.genProcSectionHeader(fileNo, "delete records in 'base table'", 2, !(hasNlAttributes));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " AS PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
// ### IF IVK ###
} else {
M11_LRT.genProcSectionHeader(fileNo, "DELETE: mark records in public tables as 'deleted' which are not 'set productive' and marked 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]' in LRT", 2, true);

if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "mark record in NL-table as being deleted", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

M11_LRT.genProcSectionHeader(fileNo, "mark records in 'base table' as being deleted", 2, !(hasNlAttributes));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
//          Print #fileNo, addTab(2); "END FOR;"
}

M11_LRT.genProcSectionHeader(fileNo, "DELETE: mark records in public tables as 'deleted' which are 'set productive' and marked 'deleted[" + String.valueOf(M11_LRT.lrtStatusDeleted) + "]' in LRT", 2, null);

if (hasNlAttributes) {
M11_LRT.genProcSectionHeader(fileNo, "mark records in NL-table", 2, true);

if (hasPriceAssignmentSubClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anIsDeleted + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals.g_anInLrt + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals.g_anStatus + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN" + M86_SetProductive.statusReadyToBeSetProductive + "ELSE " + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + "PUBNL." + M01_Globals.g_anAhCid + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + " END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

if (hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSIBM.SYDUMMY1");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNamePub + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PUBNL." + entityShortName + "_" + M01_Globals.g_anOid + " = PAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PAR." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePubNl + " PUBNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anIsDeleted + " = 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals.g_anStatus + " = " + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + "PUBNL." + M01_Globals.g_anAhCid + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + "settingManActCP_in," + "settingManActTP_in," + "settingManActSE_in," + "settingSelRelease_in" + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals.g_anInLrt + " = CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUBNL." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffectedNl + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBNL." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
}

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

if (acmEntityIndex == M22_Class.getClassIndexByName(M01_ACM_IVK.clxnGenericCode, M01_ACM_IVK.clnGenericCode, null) &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M11_LRT.genProcSectionHeader(fileNo, "mark records in 'codecategory'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null) + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB.ISDELETED = 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB.INLRT = CAST(NULL AS BIGINT),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB.STATUS_ID = VL6CLRT.F_GETLRTTGS(PUB.AHCLASSID,CAST('C' AS CHAR(1)),settingManActCP_in,settingManActTP_in,settingManActSE_in,settingSelRelease_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anAhOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

}

M11_LRT.genProcSectionHeader(fileNo, "mark records in 'base table'", 2, !(hasNlAttributes));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anInLrt + " = CAST(NULL AS " + M01_Globals.g_dbtOid + "),");

if (aggHeadClassIndex > 0) {
tmpClassId =  + M01_Globals.g_anAhCid;
} else {
tmpClassId =  + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + ;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anStatus + " = " + (hasPriceAssignmentSubClass ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (PUB." + M01_Globals.g_anCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + tmpClassId + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS " + M01_Globals.g_dbtEntityType + ")," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (hasPriceAssignmentSubClass ? " END" : ""));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PUB." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + pc_tempTabNamePubOidsAffected + " AS ses");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUB." + M01_Globals.g_anOid + " = ses.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ses.hasBeenSetProductive = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

// ### ENDIF IVK ###

// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

// ### IF IVK ###
if (!(isPrimaryOrg)) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommit, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameLrtCommit, ddlType, null, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
// ### ELSE IVK ###
//     genSpLogProcExit fileNo, qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

genLrtSupportSpsForEntity3(acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genLrtSupportSpsForEntity3(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

String sectionName;
String entityName;
String entityTypeDescr;
String entityShortName;
String ahClassIdStr;
boolean isUserTransactional;
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
boolean hasNlAttributes;
boolean hasNlTable;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
boolean ignoreForChangelog;
int aggHeadClassIndex;
String aggHeadShortClassName;
boolean isAggregateHead;
boolean implicitelyGenChangeComment;
boolean ahHasChangeComment;
String busKeyAttrList;
// ### IF IVK ###
boolean isPsTagged;
boolean hasNoIdentity;
String lrtClassification;
String lrtActivationStatusMode;
boolean hasPriceAssignmentSubClass;
boolean hasPriceAssignmentAggHead;
String priceAssignmentSubClassIdList;
String isSubjectToPreisDurchschuss;

boolean condenseData;
boolean enforceLrtChangeComment;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (thisPoolIndex < 1) {
return;
} else if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
return;
}
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;

// ### IF IVK ###
hasPriceAssignmentSubClass = false;
hasPriceAssignmentAggHead = false;
priceAssignmentSubClassIdList = "";
enforceLrtChangeComment = false;
// ### ENDIF IVK ###
busKeyAttrList = "";

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
entityIdStrList = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
ahClassIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
implicitelyGenChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].implicitelyGenChangeComment;
if (M22_Class.g_classes.descriptors[acmEntityIndex].hasBusinessKey) {
busKeyAttrList = M24_Attribute.getPkAttrListByClassIndex(acmEntityIndex, ddlType, null, null, null, null);
}
// ### IF IVK ###
lrtClassification = M22_Class.g_classes.descriptors[acmEntityIndex].lrtClassification;
lrtActivationStatusMode = M22_Class.g_classes.descriptors[acmEntityIndex].lrtActivationStatusMode;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###

int i;
if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
hasOwnTable = true;
isAbstract = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
relRefs.numRefs = 0;
isGenForming = false;
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = false;
// ### ENDIF IVK ###
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass;
hasPriceAssignmentAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
enforceLrtChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].enforceLrtChangeComment;

if (hasPriceAssignmentSubClass) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
// ### ENDIF IVK ###
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
// ### IF IVK ###
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
}

sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
entityIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
ahClassIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
dbAcmEntityType = "R";
relRefs.numRefs = 0;
isGenForming = false;
hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
isAggregateHead = false;
// ### IF IVK ###
condenseData = false;
hasNoIdentity = false;
lrtClassification = M23_Relationship.g_relationships.descriptors[acmEntityIndex].lrtClassification;
lrtActivationStatusMode = M23_Relationship.g_relationships.descriptors[acmEntityIndex].lrtActivationStatusMode;
hasPriceAssignmentAggHead = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasPriceAssignmentAggHead;
isSubjectToPreisDurchschuss = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;

if (hasPriceAssignmentAggHead) {
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
// ### ENDIF IVK ###

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

String ukAttrDecls;
String pkAttrList;
String leftFkAttrs;
String rightFkAttrs;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, null, null, 1, null, false, false, M01_Common.DdlOutputMode.edomNone, null);
busKeyAttrList = leftFkAttrs + "," + rightFkAttrs;

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);
} else {
return;
}

// ### IF IVK ###
hasNlTable = hasNlAttributes |  (isAggregateHead &  implicitelyGenChangeComment & !forGen & !forNl & !condenseData);
// ### ELSE IVK ###
// hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl)
// ### ENDIF IVK ###

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 0)) {
// LRT is only supported at 'pool-level'
return;
}

if (aggHeadClassIndex > 0) {
aggHeadShortClassName = M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName;
}

String qualTabNamePub;
String qualTabNamePriv;
String unQualTabNamePub;
qualTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, null, null);
unQualTabNamePub = M04_Utilities.getUnqualObjName(qualTabNamePub);

String qualTabNameNlPub;
String qualTabNameNlPriv;
// ### IF IVK ###
if (hasNlAttributes |  ((isAggregateHead |  enforceLrtChangeComment) & ! forGen & !forNl)) {
// ### ELSE IVK ###
// If hasNlAttributes Or (isAggregateHead And Not forGen And Not forNl) Then
// ### ENDIF IVK ###
qualTabNameNlPub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, null, null);
qualTabNameNlPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, true, null, null, null);
}

String qualTabNameAggHeadPub;
String qualViewNameAggHead;
String qualTabNameAggHeadNlPriv;
String aggHeadFkAttrName;
qualTabNameAggHeadPub = "";
qualTabNameAggHeadNlPriv = "";
if (aggHeadClassIndex > 0) {
qualTabNameAggHeadPub = M04_Utilities.genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

qualViewNameAggHead = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[aggHeadClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, M22_Class.g_classes.descriptors[aggHeadClassIndex].useMqtToImplementLrt, null, null, null, null, null);
qualTabNameAggHeadNlPriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[aggHeadClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);
ahHasChangeComment = M22_Class.g_classes.descriptors[aggHeadClassIndex].implicitelyGenChangeComment |  M22_Class.g_classes.descriptors[aggHeadClassIndex].hasNlAttrsInNonGenInclSubClasses;
aggHeadFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName, null, null, null, null);
}

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualPdmTableViewName;
qualPdmTableViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnPdmTable, M01_ACM.vnsPdmTable, ddlType, null, null, null, null, null, null, null, null, null, null);

boolean hasNlLabelAttr;
hasNlLabelAttr = false;
// ### IF IVK ###
boolean labelIsNationalizable;
labelIsNationalizable = false;
// ### ENDIF IVK ###

if (!(forNl)) {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, null, true, forGen, M01_Common.DdlOutputMode.edomNone, null);

int j;
for (int j = 1; j <= transformation.nlAttrRefs.numDescriptors; j++) {
if (transformation.nlAttrRefs.descriptors[j].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute) {
if (M24_Attribute.g_attributes.descriptors[transformation.nlAttrRefs.descriptors[j].refIndex].attributeName.toUpperCase() == "LABEL") {
hasNlLabelAttr = true;
// ### IF IVK ###
labelIsNationalizable = M24_Attribute.g_attributes.descriptors[transformation.nlAttrRefs.descriptors[j].refIndex].isNationalizable;
// ### ENDIF IVK ###
}
}
}
}

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualProcName;

if (M03_Config.generateLrtSps) {
if (!(forNl)) {
// ####################################################################################################################
// #    SP for ROLLBACK on given class
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, "LRTROLLBACK", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-ROLLBACK on \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to rollback");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this rollback");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (isPsTagged) {
M11_LRT.genVarDecl(fileNo, "v_psOid", "BIGINT", "0", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter 'rowCount_out'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid = ( SELECT lrt.ps_oid FROM " + qualTabNameLrt + " AS lrt WHERE lrt.oid = lrtOid_in );");
}

// ### IF IVK ###
if (!(condenseData)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "unlock rows in public table" + (hasNlAttributes ? "s" : ""), 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = lrtOid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (hasNlAttributes) {
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameNlPub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = lrtOid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "cleanup private table" + (hasNlTable ? "s" : ""), 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (hasNlTable) {
M11_LRT.genProcSectionHeader(fileNo, "cleanup private NL-table", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameNlPriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (!(forNl & ! forGen & (acmEntityType != M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship))) {
// ####################################################################################################################
// #    SP for LRT-LOCK on record on a given class
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, "LRTLOCK", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-LOCK on \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "LRT-OID used to lock the record");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure this row is supposed to correspond to");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row being locked");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being locked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

// ### IF IVK ###
if (condenseData) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("lrtLockNotSup", fileNo, 1, unQualTabNamePub, null, null, null, null, null, null, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_pubOwnerUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

// ### IF IVK ###
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine existance and 'current owner' of record to lock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that record exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_oid IS NULL THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("lrtLockNotFound", fileNo, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that record is not already locked by some other transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lrtOid IS NOT NULL) AND (v_lrtOid <> lrtOid_in) THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine ID of user holding the lock", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_pubOwnerUserId = (SELECT USR." + M01_Globals.g_anUserId + " FROM " + M01_Globals.g_qualTabNameUser + " USR INNER JOIN " + qualTabNameLrt + " LRT ON LRT.UTROWN_OID = USR." + M01_Globals.g_anOid + " WHERE LRT." + M01_Globals.g_anOid + " = v_lrtOid);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_pubOwnerUserId = COALESCE(v_pubOwnerUserId, '<unknown>');");
M00_FileWriter.printToFile(fileNo, "");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, 2, null, null, null, null, null, null, null, null, null, "v_pubOwnerUserId", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "if record is already locked by current transaction there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtOid = lrtOid_in THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcExit fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public record' into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, true, true, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 2, , True, True
// ### ENDIF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "" + M11_LRT.lrtStatusLocked, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conPsOid, "psOid_in", null, null, null);
// ### ENDIF IVK ###

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "lock the 'public record' with this LRT-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, entityIdStr, ahClassIdStr, "lrtOid_in", 1, String.valueOf(M11_LRT.lrtStatusLocked), false);

// ### IF IVK ###
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
// #    SP for LRT-LOCK via TempTable on records on a given table
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, "LRTLOCK", "TEMPTABLE", null, null, null);

M22_Class_Utilities.printSectionHeader("SP for TempTable-based LRT-LOCK on records in \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "LRT-OID used to lock the record");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure this row is supposed to correspond to");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being locked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

// ### IF IVK ###
if (condenseData) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("lrtLockNotSup", fileNo, 1, unQualTabNamePub, null, null, null, null, null, null, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_pubOwnerUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

// ### IF IVK ###
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genDdlForTempPrivClassIdOid(fileNo);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine if at least one record does not exist at all", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + "= T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName + " TBL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_ID= T." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmTypedTableName + " = '" + M04_Utilities.getUnqualObjName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmFkSchemaName + " = '" + M04_Utilities.getSchemaName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "verify that records exist and are of given PS", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_oid IS NOT NULL THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("objNotFound", fileNo, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_oid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (isPsTagged) {
M11_LRT.genProcSectionHeader(fileNo, "determine if at least one record does not exist in current PS", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + "= T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName + " TBL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_ID= T." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmTypedTableName + " = '" + M04_Utilities.getUnqualObjName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmFkSchemaName + " = '" + M04_Utilities.getSchemaName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " <> psOid_in");

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "verify that records exist and are of given PS", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_oid IS NOT NULL THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("objNotFoundInPs", fileNo, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_oid))", "RTRIM(CHAR(psOid_in))", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "determine if at least one record is locked", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.PRIVCLASSIDOID T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + "= T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualPdmTableViewName + " TBL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_ID= T." + M01_Globals.g_anCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL.ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmTypedTableName + " = '" + M04_Utilities.getUnqualObjName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBL." + M01_Globals.g_anPdmFkSchemaName + " = '" + M04_Utilities.getSchemaName(qualTabNamePub) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " IS NOT NULL AND PUB." + M01_Globals.g_anInLrt + " <> lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "verify that record is not already locked by some other transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtOid IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine ID of user holding the lock", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_pubOwnerUserId = (SELECT USR." + M01_Globals.g_anUserId + " FROM " + M01_Globals.g_qualTabNameUser + " USR INNER JOIN " + qualTabNameLrt + " LRT ON LRT.UTROWN_OID = USR." + M01_Globals.g_anOid + " WHERE LRT." + M01_Globals.g_anOid + " = v_lrtOid);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_pubOwnerUserId = COALESCE(v_pubOwnerUserId, '<unknown>');");
M00_FileWriter.printToFile(fileNo, "");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, 2, null, null, null, null, null, null, null, null, null, "v_pubOwnerUserId", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, true, true, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       initAttributeTransformation transformation, 2, , True, True
// ### ENDIF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "" + M11_LRT.lrtStatusLocked, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
// ### IF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conPsOid, "psOid_in", null, null, null);
// ### ENDIF IVK ###

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " IN (SELECT OID FROM SESSION.PRIVCLASSIDOID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(INLRT IS NULL OR INLRT <> lrtOid_in)");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "lock the 'public records' with this LRT-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " IN (SELECT OID FROM SESSION.PRIVCLASSIDOID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PUB.INLRT IS NULL OR PUB.INLRT <> lrtOid_in)");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, entityIdStr, ahClassIdStr, "lrtOid_in", 1, String.valueOf(M11_LRT.lrtStatusLocked), false);

// ### IF IVK ###
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
// ### ENDIF IVK ###
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);



// ####################################################################################################################
// #    SP for UNLOCK on record of given class
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, "LRTUNLOCK", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for LRT-UNLOCK on \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure this row is supposed to correspond to");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the row being unlocked");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being unlocked (0 or 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

// ### IF IVK ###
if (condenseData) {
M07_SpLogging.genSpLogDecl(fileNo, -1, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("lrtUnLockNotSup", fileNo, 1, unQualTabNamePub, null, null, null, null, null, null, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtState", M01_Globals.g_dbtEnumId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

// ### IF IVK ###
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEnter fileNo, qualProcName, ddlType, , "oid_in", "rowCount_out"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine existance and 'current owner' of record to unlock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that record exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_oid IS NULL THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("lrtUnlockNotFound", fileNo, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "if record is not locked by any transaction there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtOid IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine LRTSTATE of record to unlock", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anLrtState);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtState");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lrtState IS NOT NULL) AND (v_lrtState  <> " + String.valueOf(M11_LRT.lrtStatusLocked) + ") THEN");
// ### IF IVK ###
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "oid_in", "rowCount_out"
// ### ENDIF IVK ###
M79_Err.genSignalDdlWithParms("lrtUnlockChPending", fileNo, 2, unQualTabNamePub, null, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "unlock the 'public record'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt + " = CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "remove 'private record'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anOid + " = oid_in");
// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRIV." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

// ### IF IVK ###
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "oid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//       genSpLogProcExit fileNo, qualProcName, ddlType, , "oid_in", "rowCount_out"
// ### ENDIF IVK ###
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (!(ignoreForChangelog)) {
String parFkAttrName;
parFkAttrName = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null);

boolean fillRestrictedColSetOnly;
String spInfix;
for (int i = 1; i <= 2; i++) {
fillRestrictedColSetOnly = (i == 2);
spInfix = (fillRestrictedColSetOnly ? "_RED" : "");

// ####################################################################################################################
// #    SP for retrieving LRT-Log
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, M01_ACM.spnLrtGetLog + spInfix, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for retrieving LRT-Log on \"" + qualTabNamePub + (fillRestrictedColSetOnly ? " (restricted column set)" : "") + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to retrieve the Log-Records for");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure the LRT corresponds to");
// ### ENDIF IVK ###
if (!(fillRestrictedColSetOnly)) {
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtEnumId, true, "ID of language to use for language-specific columns");
}
M11_LRT.genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", true, "(optional) retrieve only records for updates past this timestamp");
if (!(fillRestrictedColSetOnly)) {
M11_LRT.genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", true, "maximum number of rows to add to the change log");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows placed in the log");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- the temporary table holding the LRT-Log already exists in this session - ignore this");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genDdlForTempLrtLog(fileNo, null, fillRestrictedColSetOnly, false, null, null);

// ### IF IVK ###
if (fillRestrictedColSetOnly) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "#startTime_in", "rowCount_out", null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out", null, null, null, null, null, null);
}
// ### ELSE IVK ###
//       If fillRestrictedColSetOnly Then
//         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "#startTime_in", "rowCount_out"
//       Else
//         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out"
//       End If
// ### ENDIF IVK ###

M11_LRT.genProcSectionHeader(fileNo, "process inserts, updates deletes related to '" + sectionName + "." + entityName + "'", null, null);

int indent;
indent = 1;

if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(maxRowCount_in,1) > 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "retrieve records to be returned to application", 2, true);
indent = 2;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "displayMe,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orParEntityId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityType,");

if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "displayCategory,");
// ### ENDIF IVK ###
if (hasNlLabelAttr) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "label,");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship & ! forNl) {
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId1,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId1,");
if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId2,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId2,");
}
// ### IF IVK ###
if (!(forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "code,");
}
// ### ENDIF IVK ###

if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
if (!(forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Context,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseCode,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseEndSlot,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validTo,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "operation,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ts");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");

if (!(fillRestrictedColSetOnly)) {
// displayMe
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(CASE WHEN COALESCE(maxRowCount_in,1) > 0 THEN 1 ELSE 0 END),");
// orParEntityId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + entityIdStr + "',");
}

// entityId
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + entityIdStr + "',");
} else {
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(PAR_PRIV." + M01_Globals.g_anCid + ",PAR_PUB." + M01_Globals.g_anCid + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals.g_anCid + ",");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + entityIdStr + "',");
}

// entityType
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + dbAcmEntityType + "',");

if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
// displayCategory
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (lrtClassification.compareTo("") == 0 ? "CAST(NULL AS VARCHAR(1))," : "'" + lrtClassification + "',"));

// ### ENDIF IVK ###
if (hasNlLabelAttr) {
// label
// ### IF IVK ###
if (labelIsNationalizable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE((CASE NL_PRIV.LABEL_ISNATACTIVE WHEN 1 THEN NL_PRIV.LABEL_NATIONAL ELSE NL_PRIV.LABEL END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "         (CASE NL_PUB. LABEL_ISNATACTIVE WHEN 1 THEN NL_PUB. LABEL_NATIONAL ELSE NL_PUB. LABEL END)),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(NL_PRIV.LABEL, NL_PUB.LABEL),");
}
// ### ELSE IVK ###
//           Print #fileNo, addTab(indent + 1); "COALESCE(NL_PRIV.LABEL, NL_PUB.LABEL),"
// ### ENDIF IVK ###
}
}

// gen
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (forGen ? "1," : "0,"));

// isNl
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (forNl ? "1," : "0,"));

// OID
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals.g_anOid + ",");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship & ! forNl) {
if (!(fillRestrictedColSetOnly)) {
// refClassId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + relLeftClassIdStr + "',");
}
// refObjectId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + relLeftFk + ",");
if (!(fillRestrictedColSetOnly)) {
// refClassId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + relRightClassIdStr + "',");
}
// refObjectId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + relRightFk + ",");
}
// ### IF IVK ###

// code
if (!(forNl)) {
M11_LRT.genLrtLogColDdl(fileNo, attrMapping, "code", "NULL", forGen & ! hasNoIdentity, ddlType, null, "VARCHAR(1)", indent + 1, null, "PRIV", null, null, null, null, null, null);
}

boolean foundSr0Context;
int s0_01TargetClassIndex;
String s0_01FkAttrName;
String s0_01QualObjName;
int s0_02TargetClassIndex;
String s0_02FkAttrName;
String s0_02QualObjName;
int s0_03TargetClassIndex;
String s0_03FkAttrName;
String s0_03QualObjName;
int s0_04TargetClassIndex;
String s0_04FkAttrName;
String s0_04QualObjName;
int s0_05TargetClassIndex;
String s0_05FkAttrName;
String s0_05QualObjName;
int s0_06TargetClassIndex;
String s0_06FkAttrName;
String s0_06QualObjName;
int s0_07TargetClassIndex;
String s0_07FkAttrName;
String s0_07QualObjName;
int s0_08TargetClassIndex;
String s0_08FkAttrName;
String s0_08QualObjName;
int s0_09TargetClassIndex;
String s0_09FkAttrName;
String s0_09QualObjName;
int s0_10TargetClassIndex;
String s0_10FkAttrName;
String s0_10QualObjName;
int bcTargetClassIndex;
String bcFkAttrName;
String bcQualObjName;
int beTargetClassIndex;
String beFkAttrName;
String beQualObjName;

s0_01TargetClassIndex = 0;
s0_02TargetClassIndex = 0;
s0_03TargetClassIndex = 0;
s0_04TargetClassIndex = 0;
s0_05TargetClassIndex = 0;
s0_06TargetClassIndex = 0;
s0_07TargetClassIndex = 0;
s0_08TargetClassIndex = 0;
s0_09TargetClassIndex = 0;
s0_10TargetClassIndex = 0;
bcTargetClassIndex = 0;
beTargetClassIndex = 0;
// ### ENDIF IVK ###

if (!(fillRestrictedColSetOnly)) {
// ### IF IVK ###
if (forNl) {
foundSr0Context = false;
} else {
foundSr0Context = M11_LRT.genLrtLogColDdl(fileNo, attrMapping, "sr0Context", "NULL", forGen & ! hasNoIdentity, ddlType, null, "VARCHAR(1)", indent + 1, null, "PRIV", null, null, null, null, null, null);
}

if (M03_Config.lrtLogRetrieveSr0CodesFromSr0Context) {
if (foundSr0Context) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber01,                          -- sr0Code01");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber02,                          -- sr0Code02");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber03,                          -- sr0Code03");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber04,                          -- sr0Code04");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber05,                          -- sr0Code05");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber06,                          -- sr0Code06");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber07,                          -- sr0Code07");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber08,                          -- sr0Code08");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber08,                          -- sr0Code09");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T.codeNumber10,                          -- sr0Code10");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code01");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code02");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code03");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code04");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code05");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code06");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code07");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code08");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code09");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS VARCHAR(1)),                -- sr0Code10");
}
} else {
// sr0Code01
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code01", "S01", s0_01TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_01QualObjName, s0_01FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code02
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code02", "S02", s0_02TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_02QualObjName, s0_02FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code03
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code03", "S03", s0_03TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_03QualObjName, s0_03FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code04
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code04", "S04", s0_04TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_04QualObjName, s0_04FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code05
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code05", "S05", s0_05TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_05QualObjName, s0_05FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code06
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code06", "S06", s0_06TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_06QualObjName, s0_06FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code07
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code07", "S07", s0_07TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_07QualObjName, s0_07FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code08
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code08", "S08", s0_08TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_08QualObjName, s0_08FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code09
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code09", "S09", s0_09TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_09QualObjName, s0_09FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// sr0Code10
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "sr0Code10", "S10", s0_10TargetClassIndex, "CAST(NULL AS VARCHAR(1))", s0_10QualObjName, s0_10FkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);
}

// baseCodeNumber
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "baseCodeNumber", "BC", bcTargetClassIndex, "CAST(NULL AS VARCHAR(1))", bcQualObjName, bcFkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent + 1, null, null, null, null, null, null, null, null, null);

// baseEndSlot
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, "baseEndSlot", "BE", beTargetClassIndex, "CAST(NULL AS VARCHAR(1))", beQualObjName, beFkAttrName, M12_ChangeLog.ChangeLogMode.eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, "CHAR(60)", indent + 1, null, null, null, null, true, null, null, null, null);

if (beTargetClassIndex > 0) {
// actually not needed here since this will be overwritten below
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(PRIV." + beFkAttrName + " AS CHAR(22)),");
}

// ### ENDIF IVK ###
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
//         If isGenForming And forGen Then
// ### ENDIF IVK ###
// validFrom
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals_IVK.g_anValidFrom + ",");
// validTo
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals_IVK.g_anValidTo + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS DATE),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(NULL AS DATE),");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV.LRTSTATE,");
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(PAR_PRIV." + M01_Globals.g_anLastUpdateTimestamp + ", PAR_PRIV." + M01_Globals.g_anCreateTimestamp + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(PRIV." + M01_Globals.g_anLastUpdateTimestamp + ", PRIV." + M01_Globals.g_anCreateTimestamp + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNamePriv + " PRIV");
// ### IF IVK ###

if (M03_Config.lrtLogRetrieveSr0CodesFromSr0Context &  foundSr0Context & !fillRestrictedColSetOnly) {
String qualFuncNameParseSr0Context;
qualFuncNameParseSr0Context = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnParseSr0Context, ddlType, null, null, null, null, null, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TABLE(" + qualFuncNameParseSr0Context + "(PRIV.sr0Context)) T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(1=1)");
}
// ### ENDIF IVK ###

if (hasNlLabelAttr & ! fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameNlPriv + " NL_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NL_PRIV." + parFkAttrName + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NL_PRIV." + M01_Globals.g_anLanguageId + " = languageId_in");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameNlPub + " NL_PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NL_PUB." + parFkAttrName + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NL_PUB." + M01_Globals.g_anLanguageId + " = languageId_in");
}

if (forGen |  forNl) {
String qualTabNameAggHeadPriv;
qualTabNameAggHeadPriv = M04_Utilities.genQualTabNameByEntityIndex(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameAggHeadPriv + " PAR_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + "AHOID" + " = PAR_PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameAggHeadPub + " PAR_PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + "AHOID" + " = PAR_PUB." + M01_Globals.g_anOid);
}
// ### IF IVK ###

if (bcTargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + bcQualObjName + " BC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + bcFkAttrName + " = BC." + M01_Globals.g_anOid);
}

if (!(M03_Config.lrtLogRetrieveSr0CodesFromSr0Context)) {
if (s0_01TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_01QualObjName + " S01");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_01FkAttrName + " = S01." + M01_Globals.g_anOid);
}

if (s0_02TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_02QualObjName + " S02");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_02FkAttrName + " = S02." + M01_Globals.g_anOid);
}

if (s0_03TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_03QualObjName + " S03");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_03FkAttrName + " = S03." + M01_Globals.g_anOid);
}

if (s0_04TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_04QualObjName + " S04");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_04FkAttrName + " = S04." + M01_Globals.g_anOid);
}

if (s0_05TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_05QualObjName + " S05");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_05FkAttrName + " = S05." + M01_Globals.g_anOid);
}

if (s0_06TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_06QualObjName + " S06");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_06FkAttrName + " = S06." + M01_Globals.g_anOid);
}

if (s0_07TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_07QualObjName + " S07");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_07FkAttrName + " = S07." + M01_Globals.g_anOid);
}

if (s0_08TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_08QualObjName + " S08");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_08FkAttrName + " = S08." + M01_Globals.g_anOid);
}

if (s0_09TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_09QualObjName + " S09");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_09FkAttrName + " = S09." + M01_Globals.g_anOid);
}

if (s0_10TargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + s0_10QualObjName + " S10");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + s0_10FkAttrName + " = S10." + M01_Globals.g_anOid);
}
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");

if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV.LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusLocked));
}
// ### IF IVK ###

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(startTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");

String alias;
alias = "PRIV";
if (forNl) {
alias = "PAR_PRIV";
}

if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(COALESCE(" + alias + "." + M01_Globals.g_anLastUpdateTimestamp + ", " + alias + "." + M01_Globals.g_anCreateTimestamp + ") >= (startTime_in - 500000 MICROSECONDS))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "((PRIV.LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") OR (COALESCE(" + alias + "." + M01_Globals.g_anLastUpdateTimestamp + ", " + alias + "." + M01_Globals.g_anCreateTimestamp + ") >= startTime_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(COALESCE(" + alias + "." + M01_Globals.g_anLastUpdateTimestamp + ", " + alias + "." + M01_Globals.g_anCreateTimestamp + ") >= startTime_in)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");

if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");
}

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "retrieve records NOT to be returned to application", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "displayMe,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orParEntityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");

// displayMe
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "0,");
// orParEntityId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + entityIdStr + "',");
// entityType
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + dbAcmEntityType + "',");
// gen
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (forGen ? "1," : "0,"));
// isNl
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (forNl ? "1," : "0,"));
// OID
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNamePriv + " PRIV");

if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameAggHeadPriv + " PAR_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + "AHOID" + " = PAR_PRIV." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
// ### IF IVK ###

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIV." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(startTime_in IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(COALESCE(PAR_PRIV." + M01_Globals.g_anLastUpdateTimestamp + ", PAR_PRIV." + M01_Globals.g_anCreateTimestamp + ") >= startTime_in)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(COALESCE(PRIV." + M01_Globals.g_anLastUpdateTimestamp + ", PRIV." + M01_Globals.g_anCreateTimestamp + ") >= startTime_in)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

// ### IF IVK ###
if (!(condenseData &  (((aggHeadClassIndex > 0) &  ((aggHeadClassIndex != acmEntityIndex) |  forGen | forNl) & ahHasChangeComment) |  (implicitelyGenChangeComment |  hasNlAttributes)))) {
// ### ELSE IVK ###
//       If (((aggHeadClassIndex > 0) And ((aggHeadClassIndex <> acmEntityIndex) Or forGen Or forNl)) Or (implicitelyGenChangeComment Or hasNlAttributes)) Then
// ### ENDIF IVK ###
indent = 1;
if (!(fillRestrictedColSetOnly)) {
M11_LRT.genProcSectionHeader(fileNo, "retrieve details for records to be returned to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(maxRowCount_in,1) > 0 THEN");
indent = 2;
}

if (!(fillRestrictedColSetOnly &  !(qualTabNameAggHeadNlPriv.compareTo("") == 0))) {
if ((aggHeadClassIndex > 0) &  ((aggHeadClassIndex != acmEntityIndex) |  forGen | forNl)) {
M11_LRT.genProcSectionHeader(fileNo, "retrieve CHANGECOMMENT from Aggregate Head", indent, true);
} else {
M11_LRT.genProcSectionHeader(fileNo, "determine CHANGECOMMENT", indent, true);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.comment = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CNL_PRIV." + M01_ACM.conChangeComment);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");

if ((aggHeadClassIndex > 0) &  ((aggHeadClassIndex != acmEntityIndex) |  forGen | forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNameAggHeadNlPriv + " CNL_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + M01_Globals.g_anAhOid + " = CNL_PRIV." + aggHeadFkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + M01_Globals.g_anOid + " = L.oid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNameNlPriv + " CNL_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CNL_PRIV." + aggHeadFkAttrName + " = L.oid");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CNL_PRIV." + M01_Globals.g_anLanguageId + " = languageId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CNL_PRIV.LRTSTATE ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.comment IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + dbAcmEntityType + "'");
if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");
}
}

// ### IF IVK ###
if (beTargetClassIndex > 0) {
String beQualGenObjName;
String beQualGenNlObjName;

beQualGenObjName = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[beTargetClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, M22_Class.g_classes.descriptors[beTargetClassIndex].useMqtToImplementLrt, null, null, null, null, null);
beQualGenNlObjName = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[beTargetClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, M22_Class.g_classes.descriptors[beTargetClassIndex].useMqtToImplementLrt, true, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "retrieve LABEL from BaseEndSlot", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.t_baseEndSlotGenOID = (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "BEG." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + beQualGenObjName + " BEG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + beFkAttrName + " = BEG." + M04_Utilities.genSurrogateKeyName(ddlType, "ESL", null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + M01_Globals_IVK.g_anValidTo + " >= BEG." + M01_Globals_IVK.g_anValidFrom);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + M01_Globals_IVK.g_anValidFrom + " <= BEG." + M01_Globals_IVK.g_anValidTo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "PRIV." + M01_Globals.g_anOid + " = L." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "BEG." + M01_Globals_IVK.g_anValidFrom);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + dbAcmEntityType + "'");
if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M11_LRT.tempTabNameLrtLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.baseEndSlot = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "BEGNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + beQualGenNlObjName + " BEGNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "BEGNL." + M04_Utilities.genSurrogateKeyName(ddlType, "ESL", null, null, null, null) + " = L.t_baseEndSlotGenOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "BEGNL." + M01_Globals.g_anLanguageId + " = languageId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + dbAcmEntityType + "'");
if (fillRestrictedColSetOnly) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");
}
}
// ### ENDIF IVK ###

if (!(fillRestrictedColSetOnly)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}
}

// ### IF IVK ###
if (fillRestrictedColSetOnly) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "#startTime_in", "rowCount_out", null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out", null, null, null, null, null, null);
}
// ### ELSE IVK ###
//       If fillRestrictedColSetOnly Then
//         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "#startTime_in", "rowCount_out"
//       Else
//         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out"
//       End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (!(forNl)) {
String qualGenLrtViewName;
if (!(forGen)) {
qualGenLrtViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexAliasLrt, entityName, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null, null);
} else {
qualGenLrtViewName = "";
}

M12_ChangeLog.genChangeLogSupportForEntity(acmEntityIndex, acmEntityType, relRefs, qualTabNamePriv, qualTabNameNlPriv, qualTabNamePub, qualTabNameNlPub, qualGenLrtViewName, qualTabNameAggHeadNlPriv, qualViewNameAggHead, thisOrgIndex, thisPoolIndex, thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl, null);
}
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static Boolean genLrtLogColDdl(int fileNo, M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrs, String clMapAttrName, String defaultValue, boolean forGen, Integer ddlTypeW, String castToTypeW, String castToTypeDefaultW, Integer indentW, Boolean commentOnSeparateLineW, String tabVariableNameW, String tabVariableNameGenW, String referredColumnsW, String referredColumnsGenW, Boolean silentW, Boolean colFoundInGenW,  Boolean searchAllColumnsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String castToType; 
if (castToTypeW == null) {
castToType = "";
} else {
castToType = castToTypeW;
}

String castToTypeDefault; 
if (castToTypeDefaultW == null) {
castToTypeDefault = "";
} else {
castToTypeDefault = castToTypeDefaultW;
}

int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

boolean commentOnSeparateLine; 
if (commentOnSeparateLineW == null) {
commentOnSeparateLine = false;
} else {
commentOnSeparateLine = commentOnSeparateLineW;
}

String tabVariableName; 
if (tabVariableNameW == null) {
tabVariableName = "PRIV";
} else {
tabVariableName = tabVariableNameW;
}

String tabVariableNameGen; 
if (tabVariableNameGenW == null) {
tabVariableNameGen = "GEN";
} else {
tabVariableNameGen = tabVariableNameGenW;
}

String referredColumns; 
if (referredColumnsW == null) {
referredColumns = "";
} else {
referredColumns = referredColumnsW;
}

String referredColumnsGen; 
if (referredColumnsGenW == null) {
referredColumnsGen = "";
} else {
referredColumnsGen = referredColumnsGenW;
}

boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

boolean colFoundInGen; 
if (colFoundInGenW == null) {
colFoundInGen = false;
} else {
colFoundInGen = colFoundInGenW;
}

boolean searchAllColumns; 
if (searchAllColumnsW == null) {
searchAllColumns = false;
} else {
searchAllColumns = searchAllColumnsW;
}

Boolean returnValue;
//On Error GoTo ErrorExit 

returnValue = false;

String tabVariableNameToUse;
tabVariableNameToUse = tabVariableName;

if (!(M04_Utilities.arrayIsNull(clMapAttrs))) {
int i;
for (int i = M00_Helper.lBound(clMapAttrs); i <= M00_Helper.uBound(clMapAttrs); i++) {
if (clMapAttrs[i].mapTo.toUpperCase() == clMapAttrName.toUpperCase() &  (forGen == clMapAttrs[i].isTv |  (!(forGen)))) {
if (!(forGen &  clMapAttrs[i].isTv)) {
colFoundInGen = true;
referredColumnsGen = referredColumnsGen + (referredColumnsGen == "" ? "" : ",") + clMapAttrs[i].mapFrom.toUpperCase();
tabVariableNameToUse = tabVariableNameGen;
} else {
referredColumns = referredColumns + (referredColumns == "" ? "" : ",") + clMapAttrs[i].mapFrom.toUpperCase();
}

returnValue = true;

//        If silent And Not searchAllColumns Then
//          Exit Function
//        End If

String mapFromToUse;
String castToTypeToUse;
mapFromToUse = clMapAttrs[i].mapFrom.toUpperCase();
castToTypeToUse = castToType;

// ### IF IVK ###
if (clMapAttrs[i].attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[clMapAttrs[i].attrIndex].isExpression) {
mapFromToUse = M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[clMapAttrs[i].attrIndex].shortName + "EXP", null, null, null, null);
castToTypeToUse = "";
castToTypeDefault = M01_Globals.g_dbtOid;
}
}

// ### ENDIF IVK ###
if (!(silent)) {
if (!(castToTypeToUse.compareTo("") == 0)) {
if (commentOnSeparateLine) {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, "CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToTypeToUse + "), ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToTypeToUse + "),");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight("CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToType + "),", attrListAlign, null) + " -- " + clMapAttrName);
}
} else {
if (commentOnSeparateLine) {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, tabVariableNameToUse + "." + mapFromToUse + ", ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + tabVariableNameToUse + "." + mapFromToUse + ",");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(tabVariableNameToUse + "." + mapFromToUse + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
}

if (searchAllColumns) {
goto NextI;
} else {
return returnValue;
}

if (!(castToTypeToUse.compareTo("") == 0)) {
if (commentOnSeparateLine) {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, "CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToType + "), ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToType + "),");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight("CAST(" + tabVariableNameToUse + "." + mapFromToUse + " AS " + castToTypeToUse + "),", attrListAlign, null) + " -- " + clMapAttrName);
}
} else {
if (commentOnSeparateLine) {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, tabVariableNameToUse + "." + mapFromToUse + ", ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + tabVariableNameToUse + "." + mapFromToUse + ",");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(tabVariableNameToUse + "." + mapFromToUse + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
return returnValue;
}
NextI:
}
}

if (!(defaultValue.compareTo("") == 0) & ! silent & !searchAllColumns) {
if (castToTypeDefault != "") {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CAST(" + defaultValue + " AS " + castToTypeDefault + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight("CAST(" + defaultValue + " AS " + castToTypeDefault + "),", attrListAlign, null) + " -- " + clMapAttrName);
}
} else {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + defaultValue + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(defaultValue + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
}

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


public static Boolean genLrtLogColDdlAh(int fileNo, M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrs, M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrsAh, String clMapAttrName, String defaultValue, boolean forGen, boolean includeAggHeadInJoinPath,  boolean checkAggHeadForAttrs, Integer ddlTypeW, String castToTypeW, String castToTypeDefaultW, Integer indentW, Boolean commentOnSeparateLineW, String tabVariableNameW, String tabVariableNameGenW, String tabVariableNameAhW, String tabVariableNameParW, String referredColumnsW, String referredAggHeadColumnsW,  Boolean searchAllColumnsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String castToType; 
if (castToTypeW == null) {
castToType = "";
} else {
castToType = castToTypeW;
}

String castToTypeDefault; 
if (castToTypeDefaultW == null) {
castToTypeDefault = "";
} else {
castToTypeDefault = castToTypeDefaultW;
}

int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

boolean commentOnSeparateLine; 
if (commentOnSeparateLineW == null) {
commentOnSeparateLine = false;
} else {
commentOnSeparateLine = commentOnSeparateLineW;
}

String tabVariableName; 
if (tabVariableNameW == null) {
tabVariableName = "PRIV";
} else {
tabVariableName = tabVariableNameW;
}

String tabVariableNameGen; 
if (tabVariableNameGenW == null) {
tabVariableNameGen = "GEN";
} else {
tabVariableNameGen = tabVariableNameGenW;
}

String tabVariableNameAh; 
if (tabVariableNameAhW == null) {
tabVariableNameAh = "AH";
} else {
tabVariableNameAh = tabVariableNameAhW;
}

String tabVariableNamePar; 
if (tabVariableNameParW == null) {
tabVariableNamePar = "";
} else {
tabVariableNamePar = tabVariableNameParW;
}

String referredColumns; 
if (referredColumnsW == null) {
referredColumns = "";
} else {
referredColumns = referredColumnsW;
}

String referredAggHeadColumns; 
if (referredAggHeadColumnsW == null) {
referredAggHeadColumns = "";
} else {
referredAggHeadColumns = referredAggHeadColumnsW;
}

boolean searchAllColumns; 
if (searchAllColumnsW == null) {
searchAllColumns = false;
} else {
searchAllColumns = searchAllColumnsW;
}

Boolean returnValue;
returnValue = false;

if (M11_LRT.genLrtLogColDdl(fileNo, clMapAttrs, clMapAttrName, (checkAggHeadForAttrs ? "" : defaultValue), forGen, ddlType, castToType, castToTypeDefault, indent, commentOnSeparateLine, tabVariableName, tabVariableNameGen, referredColumns, null, null, null, searchAllColumns)) {
returnValue = true;
} else {
if (checkAggHeadForAttrs) {
if (M11_LRT.genLrtLogColDdl(fileNo, clMapAttrsAh, clMapAttrName, defaultValue, false, ddlType, castToType, castToTypeDefault, indent, commentOnSeparateLine, tabVariableNameAh, tabVariableNameGen, referredAggHeadColumns, null, null, null, searchAllColumns)) {
returnValue = true;
includeAggHeadInJoinPath = (tabVariableNamePar != tabVariableNameAh);
}
}
}
return returnValue;
}


// ### IF IVK ###
public static void genLrtLogRelColDdl(int fileNo, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, String clMapAttrName, String refTabVariableName, int targetClassIndex, String defaultValue, String qualObjName, String fkAttrName, Integer clMode, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, String castToTypeW, Integer indentW, Boolean suppressDefaultW, Boolean commentOnSeparateLineW, Boolean forceFollowOidReferencesW, String srcTabVariableNameW, Boolean silentW, String referredColumnsW, Boolean colFoundW, Boolean colIsGenW,  Boolean searchAllColumnsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

String castToType; 
if (castToTypeW == null) {
castToType = "";
} else {
castToType = castToTypeW;
}

int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

boolean suppressDefault; 
if (suppressDefaultW == null) {
suppressDefault = false;
} else {
suppressDefault = suppressDefaultW;
}

boolean commentOnSeparateLine; 
if (commentOnSeparateLineW == null) {
commentOnSeparateLine = false;
} else {
commentOnSeparateLine = commentOnSeparateLineW;
}

boolean forceFollowOidReferences; 
if (forceFollowOidReferencesW == null) {
forceFollowOidReferences = true;
} else {
forceFollowOidReferences = forceFollowOidReferencesW;
}

String srcTabVariableName; 
if (srcTabVariableNameW == null) {
srcTabVariableName = "";
} else {
srcTabVariableName = srcTabVariableNameW;
}

boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

String referredColumns; 
if (referredColumnsW == null) {
referredColumns = "";
} else {
referredColumns = referredColumnsW;
}

boolean colFound; 
if (colFoundW == null) {
colFound = false;
} else {
colFound = colFoundW;
}

boolean colIsGen; 
if (colIsGenW == null) {
colIsGen = false;
} else {
colIsGen = colIsGenW;
}

boolean searchAllColumns; 
if (searchAllColumnsW == null) {
searchAllColumns = false;
} else {
searchAllColumns = searchAllColumnsW;
}

targetClassIndex = -1;
colFound = false;
colIsGen = false;

String directedRelShortName;
String relName;
String relShortName;
String colName;
int i;

for (int i = 1; i <= relRefs.numRefs; i++) {
if (!(M04_Utilities.arrayIsNull(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute))) {
int j;
for (int j = M00_Helper.lBound(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute); j <= M00_Helper.uBound(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute); j++) {
if (M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom.toUpperCase() == clMapAttrName.toUpperCase()) {
// this relationship points to the class where the attribute 'clMapAttrName' can be found
if (relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft) {
// the targetClass is found at the right hand side
// make sure that we do not navigate along the relationship in the reverse direction
if (M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].maxRightCardinality != 1) {
goto ExitFor;
}
colFound = true;
targetClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].rightEntityIndex;
directedRelShortName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].lrShortRelName;
} else {
// the targetClass is found at the right hand side
// make sure that we do not navigate along the relationship in the reverse direction
if (M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].maxLeftCardinality != 1) {
goto ExitFor;
}
colFound = true;
targetClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].leftEntityIndex;
directedRelShortName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].rlShortRelName;
}
// we need to refer to the 'OR-Mapping class' if target class does not have an own table
targetClassIndex = M22_Class.g_classes.descriptors[targetClassIndex].orMappingSuperClassIndex;
colName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapTo;
relName = (!(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reuseName.compareTo("") == 0) ? M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reuseName : M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].relName);
relShortName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].effectiveShortName;
if (M22_Class.g_classes.descriptors[targetClassIndex].isGenForming & ! M22_Class.g_classes.descriptors[targetClassIndex].hasNoIdentity) {
int thisAttrIndex;
thisAttrIndex = M24_Attribute.getAttributeIndexByNameAndEntityIndexRecursive(colName, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[targetClassIndex].classIndex, null);
if (thisAttrIndex > 0) {
colIsGen = M24_Attribute.g_attributes.descriptors[thisAttrIndex].isTimeVarying;
}
}
goto ExitFor;
}
}
}
}

ExitFor:
if (colFound) {
fkAttrName = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, relShortName + directedRelShortName, null, null, null, null, null);

if ((colName.toUpperCase() == M01_Globals.g_anOid) & ! forceFollowOidReferences) {
targetClassIndex = -1;
if (!(silent)) {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, srcTabVariableName + "." + fkAttrName.toUpperCase() + ", ");
} else {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + srcTabVariableName + "." + fkAttrName.toUpperCase() + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(srcTabVariableName + "." + fkAttrName.toUpperCase() + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
}
} else {
if (!(silent)) {
if (castToType != "") {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, "CAST(" + refTabVariableName + "." + colName.toUpperCase() + " AS " + castToType + "), ");
} else {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "CAST(" + refTabVariableName + "." + colName.toUpperCase() + " AS " + castToType + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight("CAST(" + refTabVariableName + "." + colName.toUpperCase() + " AS " + castToType + "),", attrListAlign, null) + " -- " + clMapAttrName);
}
}
} else {
if (searchAllColumns) {
M00_FileWriter.printToFile(fileNo, refTabVariableName + "." + colName.toUpperCase() + ", ");
} else {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + refTabVariableName + "." + colName.toUpperCase() + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(refTabVariableName + "." + colName.toUpperCase() + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
}
}

M04_Utilities.addStrListElem(referredColumns, fkAttrName);

if (M22_Class.g_classes.descriptors[targetClassIndex].isUserTransactional &  (clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
qualObjName = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[targetClassIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen |  colIsGen, true, M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[targetClassIndex].useMqtToImplementLrt, null, null, null, null, null);
} else {
qualObjName = M04_Utilities.genQualTabNameByClassIndex(targetClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen |  colIsGen, null, null, null, null, null, null);
}
}
} else {
if (!(suppressDefault & ! searchAllColumns)) {
if (commentOnSeparateLine) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + clMapAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + defaultValue + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + M04_Utilities.paddRight(defaultValue + ",", attrListAlign, null) + " -- " + clMapAttrName);
}
}
}
}


public static Boolean genLrtLogRelColDdlAh(int fileNo, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsAh, M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping, M24_Attribute_Utilities.AttributeMappingForCl[] attrMappingAh, String clMapAttrName, String refTupVar, int targetClassIndex, int targetClassIndexAh, String defaultVal, String qualObjName, String fkAttrName, boolean includeAggHeadInJoinPath,  boolean checkAggHeadForAttrs, Integer clMode, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Integer indentW, Boolean checkColumnsAlsoW, Boolean commentOnSeparateLineW, Boolean forceFollowOidReferencesW, String colTupVarSrcW, String colTupVarAhW, String colTupVarSrcGenW, String colTupVarSrcParW, Boolean colFoundInAggHeadW, Boolean colFoundInGenW, Boolean colFoundInAggHeadGenW, String referredColumnsW, String referredAggHeadColumnsW, String referredGenColumnsW, String referredAggHeadGenColumnsW, Boolean silentW,  Boolean searchAllColumnsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

boolean checkColumnsAlso; 
if (checkColumnsAlsoW == null) {
checkColumnsAlso = false;
} else {
checkColumnsAlso = checkColumnsAlsoW;
}

boolean commentOnSeparateLine; 
if (commentOnSeparateLineW == null) {
commentOnSeparateLine = false;
} else {
commentOnSeparateLine = commentOnSeparateLineW;
}

boolean forceFollowOidReferences; 
if (forceFollowOidReferencesW == null) {
forceFollowOidReferences = true;
} else {
forceFollowOidReferences = forceFollowOidReferencesW;
}

String colTupVarSrc; 
if (colTupVarSrcW == null) {
colTupVarSrc = "";
} else {
colTupVarSrc = colTupVarSrcW;
}

String colTupVarAh; 
if (colTupVarAhW == null) {
colTupVarAh = "";
} else {
colTupVarAh = colTupVarAhW;
}

String colTupVarSrcGen; 
if (colTupVarSrcGenW == null) {
colTupVarSrcGen = "";
} else {
colTupVarSrcGen = colTupVarSrcGenW;
}

String colTupVarSrcPar; 
if (colTupVarSrcParW == null) {
colTupVarSrcPar = "";
} else {
colTupVarSrcPar = colTupVarSrcParW;
}

boolean colFoundInAggHead; 
if (colFoundInAggHeadW == null) {
colFoundInAggHead = false;
} else {
colFoundInAggHead = colFoundInAggHeadW;
}

boolean colFoundInGen; 
if (colFoundInGenW == null) {
colFoundInGen = false;
} else {
colFoundInGen = colFoundInGenW;
}

boolean colFoundInAggHeadGen; 
if (colFoundInAggHeadGenW == null) {
colFoundInAggHeadGen = false;
} else {
colFoundInAggHeadGen = colFoundInAggHeadGenW;
}

String referredColumns; 
if (referredColumnsW == null) {
referredColumns = "";
} else {
referredColumns = referredColumnsW;
}

String referredAggHeadColumns; 
if (referredAggHeadColumnsW == null) {
referredAggHeadColumns = "";
} else {
referredAggHeadColumns = referredAggHeadColumnsW;
}

String referredGenColumns; 
if (referredGenColumnsW == null) {
referredGenColumns = "";
} else {
referredGenColumns = referredGenColumnsW;
}

String referredAggHeadGenColumns; 
if (referredAggHeadGenColumnsW == null) {
referredAggHeadGenColumns = "";
} else {
referredAggHeadGenColumns = referredAggHeadGenColumnsW;
}

boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

boolean searchAllColumns; 
if (searchAllColumnsW == null) {
searchAllColumns = false;
} else {
searchAllColumns = searchAllColumnsW;
}

Boolean returnValue;

boolean colFound;

returnValue = false;
colFoundInAggHead = false;

// try to find a column following relationships at 'targetClass'
M11_LRT.genLrtLogRelColDdl(fileNo, relRefs, clMapAttrName, refTupVar, targetClassIndex, defaultVal, qualObjName, fkAttrName, clMode, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent, checkAggHeadForAttrs |  checkColumnsAlso, commentOnSeparateLine, forceFollowOidReferences, (colTupVarSrcPar + "" == "" ? colTupVarSrc : colTupVarSrcPar), silent, null, colFound, colFoundInGen, searchAllColumns);

if (checkAggHeadForAttrs & ! colFound) {
// did not find a column following relationships at 'targetClass' -> try aggregate head
M11_LRT.genLrtLogRelColDdl(fileNo, relRefsAh, clMapAttrName, refTupVar, targetClassIndexAh, defaultVal, qualObjName, fkAttrName, clMode, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, indent, checkColumnsAlso, commentOnSeparateLine, forceFollowOidReferences, colTupVarAh, silent, referredAggHeadColumns, colFound, colFoundInAggHeadGen, searchAllColumns);

if (colFound) {
if (!(colFoundInAggHeadGen)) {
includeAggHeadInJoinPath = true;
}
colFoundInAggHead = !(colFoundInAggHeadGen);
}
}

returnValue = colFound;

if (checkColumnsAlso) {
if (searchAllColumns | ! colFound) {
if (M11_LRT.genLrtLogColDdl(fileNo, attrMapping, clMapAttrName, (checkAggHeadForAttrs ? "" : defaultVal), forGen, ddlType, null, null, indent, commentOnSeparateLine, (colTupVarSrcPar == "" ? colTupVarSrc : colTupVarSrcPar), colTupVarSrcGen, referredColumns, referredGenColumns, silent, colFoundInGen, searchAllColumns)) {
returnValue = true;
} else {
if (checkAggHeadForAttrs) {
if (M11_LRT.genLrtLogColDdl(fileNo, attrMappingAh, clMapAttrName, defaultVal, false, ddlType, null, null, indent, true, colTupVarAh, colTupVarSrcGen, referredAggHeadColumns, referredAggHeadGenColumns, silent, colFoundInAggHeadGen, searchAllColumns)) {
if (!(colFoundInAggHeadGen)) {
includeAggHeadInJoinPath = !(colFoundInAggHeadGen);
}
colFoundInAggHead = !(colFoundInAggHeadGen);
returnValue = true;
}
}
}
}
}
return returnValue;
}


// ### ENDIF IVK ###
public static void genLrtSupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNoTab, int fileNoView, int fileNoClView, int fileNoFk, int fileNoSup, Integer ddlTypeW, Boolean forGenW) {
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

boolean hasNlTab;
boolean nlTabIsPurelyPrivate;

//On Error GoTo ErrorExit 

// ### IF IVK ###
hasNlTab = (forGen &  M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses) |  (!(forGen &  (M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInNonGenInclSubClasses |  M22_Class.g_classes.descriptors[classIndex].enforceLrtChangeComment | (M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[classIndex].classIndex &  M22_Class.g_classes.descriptors[classIndex].implicitelyGenChangeComment & !M22_Class.g_classes.descriptors[classIndex].condenseData))));
// ### ELSE IVK ###
//   hasNlTab = (forGen And .hasNlAttrsInGenInclSubClasses) Or _
//              (Not forGen And (.hasNlAttrsInNonGenInclSubClasses Or (.aggHeadClassIndex = .classIndex)))
// ### ENDIF IVK ###
nlTabIsPurelyPrivate = hasNlTab & ! (forGen &  M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses) & !(!(forGen &  )));
M11_LRT.genLrtSupportViewForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, forGen, false, null);
if (hasNlTab) {
M11_LRT.genLrtSupportViewForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, forGen, true, nlTabIsPurelyPrivate);
}

M11_LRT.genLrtSupportTriggerForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, false, null, null);
if (hasNlTab) {
M11_LRT.genLrtSupportTriggerForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, true, false, nlTabIsPurelyPrivate);
}

genLrtSupportSpsForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, forGen, false);
if (hasNlTab & ! nlTabIsPurelyPrivate) {
genLrtSupportSpsForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, forGen, true);
}

if (M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt) {
M11_LRT_MQT.genLrtMqtSupportForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, forGen, false, null);
if (hasNlTab & ! nlTabIsPurelyPrivate) {
M11_LRT_MQT.genLrtMqtSupportForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, forGen, true, nlTabIsPurelyPrivate);
}

M11_LRT.genLrtSupportTriggerForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, false, true, null);
if (hasNlTab) {
M11_LRT.genLrtSupportTriggerForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, true, true, nlTabIsPurelyPrivate);
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// todo: hoarminize parameter list with genLrtSupportDdlForClass
public static void genLrtSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNoTab, int fileNoView, int fileNoClView, int fileNoFk, int fileNoSup, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

M11_LRT.genLrtSupportViewForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, null, false, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M11_LRT.genLrtSupportViewForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, null, true, null);
}

M11_LRT.genLrtSupportTriggerForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, null, false, null, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M11_LRT.genLrtSupportTriggerForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, null, true, null, null);
}

genLrtSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, null, false);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
genLrtSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, null, true);
}

if (M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[thisRelIndex].useMqtToImplementLrt) {
M11_LRT_MQT.genLrtMqtSupportForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, null, false, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M11_LRT_MQT.genLrtMqtSupportForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, null, true, null);
}

M11_LRT.genLrtSupportTriggerForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, null, false, true, null);
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M11_LRT.genLrtSupportTriggerForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, null, true, true, null);
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}




}