package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M27_Meta {


// ### IF IVK ###


private static final int processingStep = 3;

public static final String tempTabNameGenWorkSpaceResult = "SESSION.GenWorkSpaceResult";

public static void genAcmMetaSupportDdl(Integer ddlType) {
int i;
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genAcmMetaSupportDdlForCode(M01_Common.DdlTypeId.edtLdm);
genAcmMetaSupportDdlForCodeByPool(null, null, M01_Common.DdlTypeId.edtLdm);
genAcmMetaSupportDdlForMetaByPool(null, null, M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genAcmMetaSupportDdlForCode(M01_Common.DdlTypeId.edtPdm);
genAcmMetaSupportDdlForGenWorkspace(M01_Common.DdlTypeId.edtPdm);
genAcmMetaSupportDdlForCtsConfig(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].isActive &  M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm & !M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive) {
genAcmMetaSupportDdlForCodeByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genAcmMetaSupportDdlForMetaByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].isActive &  M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm) {
genAcmMetaSupportDdlForGenWorkspaceByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}

for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
genAcmMetaSupportDdlForGenWorkspaceByOrg(thisOrgIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}

private static void genAcmMetaSupportDdlForCodeByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (M03_Config.generateFwkTest) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseCoreSupport, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    currently no objects to create
// ####################################################################################################################


NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

private static void genAcmMetaSupportDdlForMetaByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (M03_Config.generateFwkTest) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseCoreSupport, M01_Common.ldmIterationPoolSpecific);

String qualProcNameGetGroupElementsGlobal;
String qualProcNameGetGroupElementsLocal;
qualProcNameGetGroupElementsGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGetGroupElements, ddlType, null, null, null, null, null, null);
qualProcNameGetGroupElementsLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnGetGroupElements, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetGroupElementsLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "( ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN languageId_in           INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN fallbackLanguageId_in   INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN classId_in              VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN groupElementOid_in      BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_dstmntTxt       VARCHAR(500)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_restmntTxt      VARCHAR(500)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- declare statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmnt                   STATEMENT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_restmnt                 STATEMENT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- declare cursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary table for GroupElements from VL6CMET.GETGROUPELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.GroupElements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid         BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classid     VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "divOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid       BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accModeId   INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entity      VARCHAR(250)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.GroupElementsDistinct");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classid     VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "divOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid       BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accModeId   INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entity      VARCHAR(250)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- call 'global procedure'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameGetGroupElementsGlobal + "(?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "languageId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "fallbackLanguageId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "groupElementOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dstmntTxt = 'INSERT INTO SESSION.GroupElementsDistinct (classid, divOid, psOid, orgOid, accModeId, entity) SELECT DISTINCT classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElements';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_dstmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_restmntTxt = 'SELECT classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElementsDistinct';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_restmnt FROM v_restmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN c_return;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
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

private static void genAcmMetaSupportDdlForCode(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (M03_Config.generateFwkTest) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexCode, processingStep, ddlType, null, null, null, M01_Common.phaseCoreSupport, null);

// ####################################################################################################################
// #    Function for decomposing Sr0Context into CodeNumbers
// ####################################################################################################################

String qualFuncNameParseSr0Context;
qualFuncNameParseSr0Context = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnParseSr0Context, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function for decomposing Sr0Context into CodeNumbers", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameParseSr0Context);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "sr0Context_in", "VARCHAR(50)", false, "string-encode list of CodeNumbers delimited by '+'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber01 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber02 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber03 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber04 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber05 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber06 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber07 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber08 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber09 " + M01_Globals_IVK.g_dbtCodeNumber + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber10 " + M01_Globals_IVK.g_dbtCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_sr0Context", "VARCHAR(50)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_pos", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_start", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeIndex", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber01", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber02", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber03", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber04", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber05", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber06", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber07", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber08", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber09", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumber10", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_sr0Context = REPLACE(REPLACE(RTRIM(LTRIM(sr0Context_in)), '-', '+'), ' ', '');");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE (v_codeIndex <= 10) AND (v_pos > 0) DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_pos = LOCATE('+', v_sr0Context, v_start);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_pos = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_codeNumber = SUBSTR(v_sr0Context, v_start);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_pos > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_codeNumber = SUBSTR(v_sr0Context, v_start, v_pos - v_start);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF     v_codeIndex =  1 THEN SET v_codeNumber01 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  2 THEN SET v_codeNumber02 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  3 THEN SET v_codeNumber03 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  4 THEN SET v_codeNumber04 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  5 THEN SET v_codeNumber05 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  6 THEN SET v_codeNumber06 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  7 THEN SET v_codeNumber07 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  8 THEN SET v_codeNumber08 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex =  9 THEN SET v_codeNumber09 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF v_codeIndex = 10 THEN SET v_codeNumber10 = v_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_start = v_pos + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_codeIndex = v_codeIndex + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber01,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber02,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber03,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber04,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber05,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber06,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber07,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber08,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber09,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_codeNumber10");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

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





private static void genAcmMetaSupportDdlForGenWorkspace(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexMeta, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, null);

// ####################################################################################################################
// #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
// ####################################################################################################################

String qualProcedureNameGlobal;
String qualProcedureNameLocal;

qualProcedureNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspace, ddlType, null, null, null, null, null, null);

String qualTabNameTempOrgOids;
qualTabNameTempOrgOids = "SESSION.OrgOids";
String qualTabNameTempPsOids;
qualTabNameTempPsOids = "SESSION.PsOids";
String qualTabNameTempAccessModeIds;
qualTabNameTempAccessModeIds = "SESSION.AccessModeIds";

boolean useListParams;
boolean withSqlError;
int i;
for (int i = 1; i <= 3; i++) {
useListParams = (i == 1);
withSqlError = (i == 2);

qualProcedureNameLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, null, null, null, (useListParams ? "S" : (withSqlError ? "_WITHERROR" : "")), false, null);

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
if (useListParams) {
M11_LRT.genProcParm(fileNo, "IN", "orgIdList_in", "VARCHAR(200)", true, "(optional) ','-delimited list of IDs of Organizations");
M11_LRT.genProcParm(fileNo, "IN", "accessModeIdList_in", "VARCHAR(50)", true, "(optional) ','-delimited list of AccessModes");
M11_LRT.genProcParm(fileNo, "IN", "psOidList_in", "VARCHAR(400)", true, "(optional) ','-delimited list of OIDs of ProductStructures");
} else {
M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) identifies the 'rule scope' of the work space");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");
}
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "IN", "useRel2ProdLock_in", M01_Globals.g_dbtBoolean, true, "lock data pools first if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", false, "number of calls to GEN_WORKSPACE submitted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_error", "VARCHAR(256)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_errorInfo", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_warning", "VARCHAR(512)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for procedure results", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo        INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgId        " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "stateMent    VARCHAR(100),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "error        VARCHAR(256),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "info         VARCHAR(1024),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "warning      VARCHAR(512)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, true, true);

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempOrgOids + "( id " + M01_Globals.g_dbtEnumId + ", oid " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempPsOids + "( oid " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempAccessModeIds + "( id " + M01_Globals.g_dbtEnumId + " ) NOT LOGGED WITH REPLACE;");

if (useListParams) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET callCount_out = 0;");

if (useListParams) {
M11_LRT.genProcSectionHeader(fileNo, "determine referred ORG-OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF orgIdList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid ) SELECT O.ID, O.ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " O;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT O.ID, O.ORGOID FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(orgIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN " + M01_Globals.g_qualTabNamePdmOrganization + " O ON O.ID = " + M01_Globals.g_dbtEnumId + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine referred PS-OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF psOidList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempPsOids + "( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempPsOids + "( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN " + M01_Globals_IVK.g_qualTabNameProductStructure + " P ON P." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine referred AccessMode-IDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF accessModeIdList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + M01_Globals.g_dbtEnumId + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S ON S.ID = " + M01_Globals.g_dbtEnumId + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
} else {
M11_LRT.genProcSectionHeader(fileNo, "initialize referred IDs / OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid ) SELECT O.ID, O.ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " O WHERE COALESCE(orgId_in, O.ID) = O.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF accessModeId_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID AND S.ID < 4;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO " + qualTabNameTempPsOids + "( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P WHERE COALESCE(psOid_in, P." + M01_Globals.g_anOid + ") = P." + M01_Globals.g_anOid + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "ignore AccessMode \"" + String.valueOf(M01_Globals_IVK.g_migDataPoolId) + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM " + qualTabNameTempAccessModeIds + " WHERE id = " + String.valueOf(M01_Globals_IVK.g_migDataPoolId) + ";");

String qualProcNameSetLock;
qualProcNameSetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "GENWS", null, null);
String qualProcNameResetLock;
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "GENWS", null, null);

M11_LRT.genProcSectionHeader(fileNo, "if required lock all matching data pools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF useRel2ProdLock_in = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SAVEPOINT rel2ProdLock UNIQUE ON ROLLBACK RETAIN CURSORS;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching data pools", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR dpLoop AS csr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id           AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.oid          AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + "  AS c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.oid          AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempOrgOids + " OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id = SC." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempPsOids + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempAccessModeIds + " AM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + " = AM.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "lock data pool", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + qualProcNameSetLock + "(''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''<admin>'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS " + M01_Globals.g_dbtUserId + ") || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''<cmd>'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_numDataPools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if lock on data pool could not be aquired rollback and exit", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_numDataPools = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ROLLBACK TO SAVEPOINT rel2ProdLock;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RELEASE SAVEPOINT rel2ProdLock;");

if (useListParams) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameLocal, ddlType, 5, "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameLocal, ddlType, 5, "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
}

M79_Err.genSignalDdlWithParms("setRel2ProdLockFail", fileNo, 5, "GENWS", null, null, null, null, null, null, null, null, "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF autoCommit_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COMMIT;");
if (useListParams) {
M11_LRT.genProcSectionHeader(fileNo, "re-determine referred ORG-OIDs", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF orgIdList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid ) SELECT O.ID, O.ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " O;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT O.ID, O.ORGOID FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(orgIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "INNER JOIN " + M01_Globals.g_qualTabNamePdmOrganization + " O ON O.ID = " + M01_Globals.g_dbtEnumId + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "re-determine referred PS-OIDs", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF psOidList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempPsOids + "( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempPsOids + "( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(psOidList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "INNER JOIN " + M01_Globals_IVK.g_qualTabNameProductStructure + " P ON P." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "re-determine referred AccessMode-IDs", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF accessModeIdList_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT " + M01_Globals.g_dbtEnumId + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(accessModeIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "INNER JOIN " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S ON S.ID = " + M01_Globals.g_dbtEnumId + "(X.elem);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
} else {
M11_LRT.genProcSectionHeader(fileNo, "re-initialize referred IDs / OIDs", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO " + qualTabNameTempOrgOids + "( id, oid ) SELECT O.ID, O.ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " O WHERE COALESCE(orgId_in, O.ID) = O.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO " + qualTabNameTempAccessModeIds + "( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S WHERE COALESCE(accessModeId_in, S.ID) = S.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO " + qualTabNameTempPsOids + "( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P WHERE COALESCE(psOid_in, P." + M01_Globals.g_anOid + ") = P." + M01_Globals.g_anOid + ";");
}
M11_LRT.genProcSectionHeader(fileNo, "ignore AccessMode \"" + String.valueOf(M01_Globals_IVK.g_migDataPoolId) + "\"", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DELETE FROM " + qualTabNameTempAccessModeIds + " WHERE id = " + String.valueOf(M01_Globals_IVK.g_migDataPoolId) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RELEASE SAVEPOINT rel2ProdLock;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching data pools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR dpLoop AS csr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SC." + M01_Globals.g_anOrganizationId + " AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR.oid AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SC." + M01_Globals.g_anPoolTypeId + " AS c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SC.NATIVESCHEMANAME1 AS c_schemaName1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SC.NATIVESCHEMANAME2 AS c_schemaName2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS.oid AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + String.valueOf(M01_Globals_IVK.g_sim1DataPoolId) + " AS " + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC2." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anOrganizationId + " = SC2." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals.g_workDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC2." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + String.valueOf(M01_Globals_IVK.g_sim2DataPoolId) + " AS " + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC2." + M01_Globals.g_anPdmNativeSchemaName + " AS NATIVESCHEMANAME2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anOrganizationId + " = SC2." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC1." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals.g_workDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SC2." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") SC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTempOrgOids + " OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR.id = SC." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTempPsOids + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTempAccessModeIds + " AM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SC." + M01_Globals.g_anPoolTypeId + " = AM.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "initialize procedure parameters", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_error     = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_errorInfo = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_warning   = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcedureNameGlobal + "(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "'''' || c_schemaName1 || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "'''' || c_schemaName2 || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "RTRIM(CHAR(c_orgOid)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "RTRIM(CHAR(c_psOid)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "RTRIM(CHAR(c_accessModeId)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "'?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_error,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_errorInfo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_warning");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF autoCommit_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "keep track of error messages", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "error,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "info,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "warning");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_error,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_errorInfo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_warning");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET callCount_out = callCount_out + 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "if required unlock all matching data pools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF useRel2ProdLock_in = 1 THEN");

M11_LRT.genProcSectionHeader(fileNo, "loop over all matching data pools", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR dpLoop AS csr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id           AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.oid          AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + "  AS c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.oid          AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmPrimarySchema + " SC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempOrgOids + " OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id = SC." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempPsOids + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTempAccessModeIds + " AM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + " = AM.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR.id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SC." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "unlock data pool", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + qualProcNameResetLock + "(''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''<admin>'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS " + M01_Globals.g_dbtUserId + ") || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'''<cmd>'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_numDataPools");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF autoCommit_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return results to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "error,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "info,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "warning");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF mode_in = 0 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M27_Meta.tempTabNameGenWorkSpaceResult);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (withSqlError) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF EXISTS ( SELECT 1 FROM " + M27_Meta.tempTabNameGenWorkSpaceResult + " WHERE error IS NOT NULL ) THEN");
M79_Err.genSignalDdlWithParms("GenWsWithError", fileNo, 3, "GENWS", null, null, null, null, null, null, null, null, "COALESCE( ( SELECT COALESCE( MIN( info ), MIN( error ) ) FROM SESSION.GenWorkSpaceResult WHERE error IS NOT NULL ), 'GenWs Error' )", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (useListParams) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "orgIdList_in", "accessModeIdList_in", "psOidList_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "useRel2ProdLock_in", "callCount_out", null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) identifies the 'rule scope' of the work space");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", false, "number of calls to GEN_WORKSPACE submitted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameLocal + "(mode_in, orgId_in, accessModeId_in, psOid_in, autoCommit_in, 0, callCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) identifies the 'rule scope' of the work space");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "IN", "useRel2ProdLock_in", M01_Globals.g_dbtBoolean, true, "lock data pools first if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", true, "number of calls to GEN_WORKSPACE submitted");
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE resCursor CURSOR WITH HOLD FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET callCount_out   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspError_out   = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspInfo_out    = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspWarning_out = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcedureNameLocal + "(?,?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "callCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "autoCommit_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "useRel2ProdLock_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "check for errors", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'SELECT error, info, warning FROM " + M27_Meta.tempTabNameGenWorkSpaceResult + " WHERE error IS NOT NULL ORDER BY orgId, psOid, accessModeId FETCH FIRST 1 ROWS ONLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "resCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspError_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspInfo_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspWarning_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE resCursor;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF gwspWarning_out IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT warning FROM " + M27_Meta.tempTabNameGenWorkSpaceResult + " WHERE warning IS NOT NULL ORDER BY orgId, psOid, accessModeId FETCH FIRST 1 ROWS ONLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "resCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gwspWarning_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to call GEN_WORKSPACE for (1, 2, ...)");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) identifies the 'rule scope' of the work space");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");
M11_LRT.genProcParm(fileNo, "IN", "autoCommit_in", M01_Globals.g_dbtBoolean, true, "commit after each call to GEN_WORKSPACE if (and only if) set to '1'");
M11_LRT.genProcParm(fileNo, "OUT", "callCount_out", "INTEGER", true, "number of calls to GEN_WORKSPACE submitted");
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameLocal + "(mode_in, orgId_in, accessModeId_in, psOid_in, autoCommit_in, 0, callCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "orgId_in", "accessModeId_in", "psOid_in", "autoCommit_in", "callCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);

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


private static void genAcmMetaSupportDdlForCtsConfig(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.supportCtsConfigByTemplate)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexMeta, processingStep, ddlType, null, null, null, M01_Common.phaseDbSupport2, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(-1, ddlType, null, null, null, null);

String qualProcedureNameSetCtsConfig;

qualProcedureNameSetCtsConfig = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnSetCtsConfig, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    Stored Procedure initializing table CTSCONFIG
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Stored Procedure initializing table CTSCONFIG", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetCtsConfig);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "cts_in", "INTEGER", true, "(optional) CTS-ID to configure");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of ProductStructureto configure");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of Organization to configure");
M11_LRT.genProcParm(fileNo, "IN", "ruleScopeId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of RuleScope (Access Mode) to configure");
M11_LRT.genProcParm(fileNo, "IN", "serviceTypeId_in", M01_Globals.g_dbtEnumId, true, "(optional) id of ServiceType to configure");
M11_LRT.genProcParm(fileNo, "IN", "overWrite_in", M01_Globals.g_dbtBoolean, true, "existing records will be overwritten if and only if set to '1'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCountDel_out", "INTEGER", true, "number of rows deleted in CTSCONFIG");
M11_LRT.genProcParm(fileNo, "OUT", "rowCountIns_out", "INTEGER", false, "number of rows inserted in CTSCONFIG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

String qualTabNameTempStatement;
qualTabNameTempStatement = M94_DBAdmin.tempTabNameStatement + "CtsConfig";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 400, true, null, null, null, "CtsConfig", true, null, null, null, "flag", "INTEGER", null, null);

String qualTabNameTempOrgOids;
qualTabNameTempOrgOids = "SESSION.OrgOids";
String qualTabNameTempPsOids;
qualTabNameTempPsOids = "SESSION.PsOids";
String qualTabNameTempRuleScopeIds;
qualTabNameTempRuleScopeIds = "SESSION.RuleScopeIds";
String qualTabNameTempSeverviceTypes;
qualTabNameTempSeverviceTypes = "SESSION.SeverviceTypes";

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for OIDs / IDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempOrgOids + "( oid " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempPsOids + "( oid " + M01_Globals.g_dbtOid + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempRuleScopeIds + "( id " + M01_Globals.g_dbtEnumId + " ) NOT LOGGED WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE " + qualTabNameTempSeverviceTypes + "( type " + M01_Globals.g_dbtEnumId + " ) NOT LOGGED WITH REPLACE;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetCtsConfig, ddlType, null, "mode_in", "cts_in", "psOid_in", "orgOid_in", "ruleScopeId_in", "serviceTypeId_in", "overWrite_in", "rowCountDel_out", "rowCountIns_out", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCountDel_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCountIns_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over config templates and derive config records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR templateLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CTS           AS c_cts,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORGOIDS       AS c_orgOIdsTmpl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PSOIDS        AS c_psOidsTpl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RULESCOPES    AS c_ruleScopesTpl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SERVICETYPES  AS c_serviceTypesTpl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "STICKY        AS c_sticky,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SIZEFACTOR    AS c_sizeFactor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameCtsConfigTemplate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(cts_in, CTS) = CTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "empty temporary tables", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM SESSION.OrgOids;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM SESSION.PsOids;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM SESSION.RuleScopeIds;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM SESSION.SeverviceTypes;");

M11_LRT.genProcSectionHeader(fileNo, "determine ORG-OIDs related to this entry", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_orgOIdsTmpl IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.OrgOids ( oid ) SELECT O." + M01_Globals.g_anOid + " FROM " + M01_Globals.g_qualTabNameOrganization + " O WHERE COALESCE(orgOid_in, O." + M01_Globals.g_anOid + ") = O." + M01_Globals.g_anOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.OrgOids ( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(c_orgOIdsTmpl, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals.g_qualTabNameOrganization + " O ON O." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem) WHERE COALESCE(orgOid_in, O." + M01_Globals.g_anOid + ") = O." + M01_Globals.g_anOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine PS-OIDs related to this entry", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_psOidsTpl IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.PsOids ( oid ) SELECT P." + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " P WHERE COALESCE(psOid_in, P." + M01_Globals.g_anOid + ") = P." + M01_Globals.g_anOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.PsOids ( oid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtOid + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(c_psOidsTpl, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals_IVK.g_qualTabNameProductStructure + " P ON P." + M01_Globals.g_anOid + " = " + M01_Globals.g_dbtOid + "(X.elem) WHERE COALESCE(psOid_in, P." + M01_Globals.g_anOid + ") = P." + M01_Globals.g_anOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine rulescope-IDs related to this entry", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_ruleScopesTpl IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.RuleScopeIds ( id ) SELECT S.ID FROM " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S WHERE COALESCE(ruleScopeId_in, S.ID) = S.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.RuleScopeIds ( id )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtEnumId + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(c_ruleScopesTpl, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN " + M01_Globals.g_qualTabNameDataPoolAccessMode + " S ON S.ID = " + M01_Globals.g_dbtEnumId + "(X.elem) WHERE COALESCE(ruleScopeId_in, S.ID) = S.ID;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine service-types related to this entry", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_serviceTypesTpl IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.SeverviceTypes ( type ) SELECT 1 FROM SYSIBM.SYSDUMMY1 WHERE COALESCE(serviceTypeId_in, 1) = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.SeverviceTypes ( type ) SELECT 2 FROM SYSIBM.SYSDUMMY1 WHERE COALESCE(serviceTypeId_in, 2) = 2;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO SESSION.SeverviceTypes ( type )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_dbtEnumId + "(X.elem) FROM TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(c_serviceTypesTpl, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE " + M01_Globals.g_dbtEnumId + "(X.elem) IN (1,2);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine DELETE- and INSERT-statements", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in < 2 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF overWrite_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.StatementsCtsConfig");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DELETE FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M01_Globals_IVK.g_qualTabNameCtsConfig + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M01_Globals_IVK.g_anRuleScope + " = ' || RTRIM(CHAR(R.id))  || ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SERVICETYPE = ' || RTRIM(CHAR(S.type))|| ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'CORORG_OID = ' || RTRIM(CHAR(O." + M01_Globals.g_anOid + ")) || ' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(P." + M01_Globals.g_anOid + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.OrgOids O,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.PsOids P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.RuleScopeIds R,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION.SeverviceTypes S,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameCtsConfig + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C." + M01_Globals_IVK.g_anRuleScope + " = R.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C.SERVICETYPE = S.type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C.CORORG_OID = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C." + M01_Globals_IVK.g_anPsOid + " = P.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of rows to delete", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCountDel_out = rowCountDel_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.StatementsCtsConfig");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "flag,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals_IVK.g_qualTabNameCtsConfig + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OID, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CTS, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals_IVK.g_anRuleScope + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SERVICETYPE, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'STICKY, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SIZEFACTOR, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CORORG_OID, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals_IVK.g_anPsOid + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anCreateUser + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anCreateTimestamp + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anUpdateUser + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anLastUpdateTimestamp + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anVersionId + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "' VALUES ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NEXTVAL FOR " + qualSeqNameOid + ", ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(c_cts)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(R.id)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(S.type)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(c_sticky)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(c_sizeFactor)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(O.oid)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(P.oid)) ||', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CURRENT USER, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CURRENT TIMESTAMP, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CURRENT USER, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'CURRENT TIMESTAMP, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.OrgOids O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.PsOids P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.RuleScopeIds R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.SeverviceTypes S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCtsConfig + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anRuleScope + " = R.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.SERVICETYPE = S.type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CORORG_OID = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anPsOid + " = P.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "overWrite_in = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals.g_anOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of rows inserted", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCountIns_out = rowCountIns_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in > 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute DELETE-statement to remove eventually existing records", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF overWrite_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCtsConfig + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SESSION.OrgOids O,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SESSION.PsOids P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SESSION.RuleScopeIds R,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SESSION.SeverviceTypes S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "C." + M01_Globals_IVK.g_anRuleScope + " = R.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "C.SERVICETYPE = S.type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "C.CORORG_OID = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "C." + M01_Globals_IVK.g_anPsOid + " = P.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of rows deleted", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCountDel_out = rowCountDel_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "execute INSERT-statement", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCtsConfig);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CTS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anRuleScope + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SERVICETYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "STICKY,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SIZEFACTOR,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CORORG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCreateUser + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCreateTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anUpdateUser + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anLastUpdateTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NEXTVAL FOR " + qualSeqNameOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_cts,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "R.id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.type,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_sticky,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_sizeFactor,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "O.oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P.oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT USER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT USER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.OrgOids O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.PsOids P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.RuleScopeIds R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.SeverviceTypes S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(1=1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameCtsConfig + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anRuleScope + " = R.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.SERVICETYPE = S.type");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CORORG_OID = O.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anPsOid + " = P.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals.g_anOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of rows inserted", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCountIns_out = rowCountIns_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTempStatement);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetCtsConfig, ddlType, null, "mode_in", "cts_in", "psOid_in", "orgOid_in", "ruleScopeId_in", "serviceTypeId_in", "overWrite_in", "rowCountDel_out", "rowCountIns_out", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Stored Procedure initializing table CTSCONFIG", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetCtsConfig);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "rowCountDel_out", "INTEGER", true, "number of rows deleted in CTSCONFIG");
M11_LRT.genProcParm(fileNo, "OUT", "rowCountIns_out", "INTEGER", false, "number of rows inserted in CTSCONFIG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetCtsConfig, ddlType, null, "mode_in", "rowCountDel_out", "rowCountIns_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameSetCtsConfig + "(mode_in, NULL, NULL, NULL, NULL, NULL, 1, rowCountDel_out, rowCountIns_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetCtsConfig, ddlType, null, "mode_in", "rowCountDel_out", "rowCountIns_out", null, null, null, null, null, null, null, null, null);

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


private static void genAcmMetaSupportDdlForGenWorkspaceByOrg( Integer thisOrgIndexW, Integer ddlTypeW) {
int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, null, null, M01_Common.phaseDbSupport, null);

String qualProcedureNameGlobal;
String qualProcedureNameLocal;

// ####################################################################################################################
// #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
// ####################################################################################################################

qualProcedureNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, null, null, null, null, null, null);
qualProcedureNameLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, thisOrgIndex, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, false, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_callCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "psOid_in", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcedureNameGlobal + "(?," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + ",?,?,0,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_callCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "psOid_in", null, null, null, null, null, null, null, null, null, null);

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


private static void genAcmMetaSupportDdlForGenWorkspaceByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexMeta, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseDbSupport, null);

String qualProcedureNameGlobal;
String qualProcedureNameLocal;

// ####################################################################################################################
// #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
// ####################################################################################################################

qualProcedureNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, null, null, null, null, null, null);
qualProcedureNameLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, false, "(optional) OID of the Product Structure to call GEN_WORKSPACE for");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_callCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "psOid_in", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcedureNameGlobal + "(?," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "," + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + ",?,0,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_callCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "mode_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "mode_in", "psOid_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (thisPoolIndex > 0) {
if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
goto NormalExit;
}
}

// ####################################################################################################################
// #    'Wrapper'-Stored Procedure for GEN_WORKSPACE
// ####################################################################################################################

qualProcedureNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspace, ddlType, null, null, null, null, null, null);
qualProcedureNameLocal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnGenWorkspace, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("'Wrapper-SP' for GEN_WORKSPACE", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameLocal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "workingSchema_in", "VARCHAR(12)", true, "primary schema of the work data pool");
M11_LRT.genProcParm(fileNo, "IN", "productiveSchema_in", "VARCHAR(12)", true, "primary schema of the productive data pool");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of the Organization");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "identifies the 'rule scope' of the work space");
M11_LRT.genProcParm(fileNo, "OUT", "errorAt_out", "VARCHAR(256)", true, "in case of error: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "errorInfo_out", "VARCHAR(1024)", true, "in case of error: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "warnings_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the procedure's result");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameLocal, ddlType, null, "'workingSchema_in", "'productiveSchema_in", "orgOid_in", "psOid_in", "accessModeId_in", "'errorAt_out", "'errorInfo_out", "'warnings_out", null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "call 'global procedure'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcedureNameGlobal + "(?,?,?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "errorAt_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "errorInfo_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "warnings_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "workingSchema_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "productiveSchema_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accessModeId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameLocal, ddlType, null, "'workingSchema_in", "'productiveSchema_in", "orgOid_in", "psOid_in", "accessModeId_in", "'errorAt_out", "'errorInfo_out", "'warnings_out", null, null, null, null);

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


public static void genCallGenWorkspaceDdl(int fileNo,  int thisOrgIndex,  int thisPoolIndex, String orgOidVar, String psOidVar, int accessMode, String errorVar, String infoVar, String warningsVar, Integer indentW, Integer ddlTypeW, Boolean skipNlW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

String targetSchemaName;
targetSchemaName = M04_Utilities.genSchemaName(M01_ACM.snAlias, M01_ACM.ssnAlias, ddlType, thisOrgIndex, thisPoolIndex);

String qualProcedureNameGlobal;
qualProcedureNameGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspace, ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "generate new solver-workspace (organization " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + ", accessMode " + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + ")", indent, skipNl);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET v_stmntTxt = 'CALL " + qualProcedureNameGlobal + "(''" + targetSchemaName + "'', ''" + targetSchemaName + "'', ?, ?, " + String.valueOf(accessMode) + ", ?, ?, ?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + errorVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + infoVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + warningsVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + orgOidVar + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + psOidVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
}
// ### ENDIF IVK ###



}