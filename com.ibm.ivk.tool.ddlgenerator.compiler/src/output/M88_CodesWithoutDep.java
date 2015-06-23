package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M88_CodesWithoutDep {


// ### IF IVK ###


private static final String tempCodeOidTabName = "SESSION.CodeOid";
private static final String tempAspectOidTabName = "SESSION.AspectOid";
private static final String tempCodeOidTabNameReferred = "SESSION.CodeOidsReferred";

private static final int processingStep = 5;



private static void genDdlForTempCodeAspOids(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for ASPECT-OIDs", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tempAspectOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


private static void genDdlForTempCodeOids(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for CODE-OIDs", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tempCodeOidTabNameReferred);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "codeNumber " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


public static void genCodesWithoutDepDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genCodesWithoutDepDdlByPool(M01_Common.DdlTypeId.edtLdm, null, null);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) & ! M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal & M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm & !M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive) {
genCodesWithoutDepDdlByPool(M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}


private static void genCodesWithoutDepVAspectViewDdl(int fileNo, boolean referToAllAspectsInPs, String qualTabNameAspectOid, String qualTabNameGenericAspect, String psOidVarNameW, Boolean addCommaW, Integer indentW) {
String psOidVarName; 
if (psOidVarNameW == null) {
psOidVarName = "v_psOid";
} else {
psOidVarName = psOidVarNameW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = false;
} else {
addComma = addCommaW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

if (!(referToAllAspectsInPs)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_AspectOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "asp_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameAspectOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_Aspect");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "A.*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameGenericAspect + " A");

if (!(referToAllAspectsInPs)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_AspectOid O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "A." + M01_Globals.g_anOid + " = O.asp_oid");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "A." + M01_ACM_IVK.conPsOid + " = " + psOidVarName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")" + (addComma ? "," : ""));
}


private static void genCodesWithoutDepDdlByPool(Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// only supported at 'pool-level'
return;
}

if (!(M03_Config.generateSupportForUc304)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameExpression;
qualTabNameExpression = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTerm;
qualTabNameTerm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    Determine Codes without dependencies
// ####################################################################################################################

int i;
boolean referToAllAspectsInPs;
boolean useRegDynamicForOidList;
boolean useOidListParameter;

final boolean implementCodesWithoutDepViaRegDynamic = false;
final boolean implementCodesWithoutDepViaOidList = true;

if (implementCodesWithoutDepViaOidList) {
String qualProcedureNameCodesWithoutDepAddOids;

qualProcedureNameCodesWithoutDepAddOids = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnGetCodesWithoutDepAddOids, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for adding Aspect-OIDS as filter for Codes without dependencies", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameCodesWithoutDepAddOids);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", true, "string holding the OIDs of Aspects");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of OIDs added");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, !(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages));
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genCondDecl(fileNo, "illegalCharacter", "22018", null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR illegalCharacter");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (extra blanks, ',' etc.)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

genDdlForTempCodeAspOids(fileNo, null, false, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDepAddOids, ddlType, null, "aspOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve OIDs of referred Aspects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempAspectOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtOid + "(E.elem)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.elem IS NOT NULL AND E.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine number of OIDs to retrieved", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDepAddOids, ddlType, null, "aspOidList_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

String qualProcedureNameCodesWithoutDep;
for (int i = (implementCodesWithoutDepViaRegDynamic ? 1 : 2); i <= (implementCodesWithoutDepViaOidList ? 3 : 2); i++) {
useRegDynamicForOidList = (i == 1);
referToAllAspectsInPs = (i == 2);
useOidListParameter = (i == 3);

// we provide multiple APIs for this, two based on an explicit list of ASPECT-OIDs and one which refers to all ASPECTs of the current ProductStructure
qualProcedureNameCodesWithoutDep = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnGetCodesWithoutDep, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for determining Codes without dependencies", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameCodesWithoutDep);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure we are working with");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division we are working with");
if (useRegDynamicForOidList) {
M11_LRT.genProcParm(fileNo, "IN", "regSubKey_in", "VARCHAR(64)", true, "'subKey' identifying the records in table 'REGISTRYDYNAMIC' holding the OIDs of Aspects");
} else if (useOidListParameter) {
M11_LRT.genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", true, "string holding the OIDs of Aspects");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of CODEs found without dependencies");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

genDdlForTempCodeOids(fileNo, null, true, null, null);

if (useRegDynamicForOidList |  useOidListParameter) {
genDdlForTempCodeAspOids(fileNo, null, !(useOidListParameter), null, null);
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDep, ddlType, null, "psOid_in", "divisionOid_in", (useRegDynamicForOidList ? "'regSubKey_in" : (useOidListParameter ? "aspOidList_in" : "")), "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

if (useOidListParameter) {
M11_LRT.genProcSectionHeader(fileNo, "determine OIDs of referred Aspects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempAspectOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtOid + "(E.elem)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.elem IS NOT NULL AND E.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine number of OIDs to retrieved", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");
}

if (useRegDynamicForOidList) {
M11_LRT.genProcSectionHeader(fileNo, "determine OIDs of referred Aspects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempAspectOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (useOidListParameter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtOid + "(E.elem)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(aspOidList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.elem IS NOT NULL AND E.elem <> ''");
} else if (useRegDynamicForOidList) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(LEFT(" + M01_Globals_IVK.g_anValue + ",19) AS " + M01_Globals.g_dbtOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRegistryDynamic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anSection + " = '" + M01_PDM_IVK.gc_regDynamicSectionCodeWithoutDependencies + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anKey + " = '" + M01_PDM_IVK.gc_regDynamicKeyCodeWithoutDependencies + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anSubKey + " = regSubKey_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "determine codes referred to by " + M01_Globals_IVK.g_anSr0Context, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR aspectLoop AS aspectCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", null, 2);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSr0Context);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Aspect");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSr0Context + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tempCodeOidTabNameReferred);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.ELEM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(" + M01_Globals_IVK.g_anSr0Context + ", CAST('+' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(E.ELEM IS NOT NULL AND E.ELEM <> '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.codenumber = E.ELEM)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine codes referred to by SR1CONTEXT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR aspectLoop AS aspectCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", null, 2);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SR1CONTEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Aspect");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SR1CONTEXT IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tempCodeOidTabNameReferred);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.ELEM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(SR1CONTEXT, CAST('+' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(E.ELEM IS NOT NULL AND E.ELEM <> '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.codeNumber = E.ELEM)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine codes referred to by NSR1CONTEXT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR aspectLoop AS aspectCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", null, 2);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1CONTEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Aspect");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1CONTEXT IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tempCodeOidTabNameReferred);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.ELEM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(NSR1CONTEXT, CAST('+' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(E.ELEM IS NOT NULL AND E.ELEM <> '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.codeNumber = E.ELEM)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine OIDs for referred codes identified so far", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempCodeOidTabNameReferred + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R." + M01_Globals.g_anOid + " = (SELECT OID FROM " + qualTabNameGenericCode + " C WHERE R.CodeNumber = C." + M01_Globals_IVK.g_anCodeNumber + " AND C.CDIDIV_OID = divisionOid_in AND C." + M01_Globals_IVK.g_anIsDeleted + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine codes referred to by BCDBCD_OID, BPCBPC_OID, BCCBCD_OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR aspectLoop AS aspectCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
genCodesWithoutDepVAspectViewDdl(fileNo, referToAllAspectsInPs, tempAspectOidTabName, qualTabNameGenericAspect, "psOid_in", null, 2);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BCDBCD_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BPCBPC_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BCCBCD_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Aspect");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF BCDBCD_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.oid = BCDBCD_OID) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + tempCodeOidTabNameReferred + " ( oid ) VALUES (BCDBCD_OID);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF BPCBPC_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.oid = BPCBPC_OID) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + tempCodeOidTabNameReferred + " ( oid ) VALUES (BPCBPC_OID);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF BCCBCD_OID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM " + tempCodeOidTabNameReferred + " C WHERE C.oid = BCCBCD_OID) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + tempCodeOidTabNameReferred + " ( oid ) VALUES (BCCBCD_OID);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine codes referred by TERMs", null, null);
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomNone, null);
boolean isFirstLoop;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempCodeOidTabNameReferred);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Exp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

isFirstLoop = true;
int j;
for (int j = 1; j <= tabColumns.numDescriptors; j++) {
if (tabColumns.descriptors[j].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[j].acmAttributeIndex].isExpression &  (tabColumns.descriptors[j].columnCategory &  (M01_Common.AttrCategory.eacNational |  M01_Common.AttrCategory.eacNationalBool)) == 0) {
if (!(isFirstLoop)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT A." + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[j].acmAttributeIndex].shortName + "EXP", null, null, null, null) + " FROM " + qualTabNameGenericAspect + " A WHERE A." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[j].acmAttributeIndex].isNationalizable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT A." + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[j].acmAttributeIndex].shortName + "EXP", null, null, null, true) + " FROM " + qualTabNameGenericAspect + " A WHERE A." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
isFirstLoop = false;
}
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "v_ExpDistinct");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Exp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_Code");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.CCRCDE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTerm + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_ExpDistinct E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anAhOid + " = E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.CCRCDE_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Code C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempCodeOidTabNameReferred + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.oid = R.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R.oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine result set of CODE OIDs 'not referred'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tempCodeOidTabNameReferred + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid + " = R.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.CDIDIV_OID = divisionOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R.oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine number of CODE-OIDs in result set", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = (SELECT COUNT(*) FROM " + M85_DataFix.tempCodeOidTabName + ");");

M11_LRT.genProcSectionHeader(fileNo, "return Code-OIDs to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE codeCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN codeCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDep, ddlType, null, "psOid_in", "divisionOid_in", (useRegDynamicForOidList ? "'regSubKey_in" : (useOidListParameter ? "aspOidList_in" : "")), "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ##############################################################

M22_Class_Utilities.printSectionHeader("SP for Determining Codes without dependencies", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameCodesWithoutDep);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (useRegDynamicForOidList) {
M11_LRT.genProcParm(fileNo, "IN", "regSubKey_in", "VARCHAR(64)", true, "'subKey' identifying the records in table 'REGISTRYDYNAMIC' holding the OIDs of Aspects");
} else if (useOidListParameter) {
M11_LRT.genProcParm(fileNo, "IN", "aspOidList_in", "CLOB(1M)", true, "string holding the OIDs of Aspects");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of CODEs found without dependencies");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameCodesWithoutDep, ddlType, null, (useRegDynamicForOidList ? "'regSubKey_in" : (useOidListParameter ? "aspOidList_in" : "")), "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "determine ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid = " + M01_Globals_IVK.g_activePsOidDdl + ";");

M11_LRT.genProcSectionHeader(fileNo, "make sure that ProductStructure exists and Division can be determined", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_divisionOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_divisionOid IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameCodesWithoutDep, ddlType, 2, (useRegDynamicForOidList ? "'regSubKey_in" : (useOidListParameter ? "aspOidList_in" : "")), "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_psOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameCodesWithoutDep + "(v_psOid, v_divisionOid, " + (useRegDynamicForOidList ? "regSubKey_in, " : "") + (useOidListParameter ? "aspOidList_in, " : "") + "rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameCodesWithoutDep, ddlType, null, (useRegDynamicForOidList ? "'regSubKey_in" : (useOidListParameter ? "aspOidList_in" : "")), "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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
// ### ENDIF IVK ###


}