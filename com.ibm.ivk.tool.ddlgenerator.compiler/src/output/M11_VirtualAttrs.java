package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M11_VirtualAttrs {


// ### IF IVK ###


private static final int processingStep = 2;


public static void genVirtAttrSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (!(M03_Config.supportVirtualColumns)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genVirtAttrSupportDdlByType(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportUpdates) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genVirtAttrSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genVirtAttrSupportDdlByType(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStep, ddlType, null, null, null, M01_Common.phaseVirtAttr, null);

// ####################################################################################################################
// #    SP for Synchronizing Virtual Attributes
// ####################################################################################################################

String qualProcNameVaSync;
qualProcNameVaSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnVirtAttrSync, ddlType, null, null, null, null, null, null);
String unqualProcNameVaSync;
unqualProcNameVaSync = M04_Utilities.getUnqualObjName(qualProcNameVaSync);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing Virtual Attributes", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameVaSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to synchronize virtual attributes for");
M11_LRT.genProcParm(fileNo, "OUT", "orgCount_out", "INTEGER", true, "number of organizations processed");
M11_LRT.genProcParm(fileNo, "OUT", "poolCount_out", "INTEGER", true, "number of data pools processed");
M11_LRT.genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", true, "number of table columns processed");
M11_LRT.genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", false, "number of values updated");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_valCount", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, null, "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET orgCount_out  = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET poolCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out  = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out  = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all 'matching' organizations", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID AS orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = orgId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "loop over all data pools of organization", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR poolLoop AS poolCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D." + M01_Globals.g_anAccessModeId + " AS poolId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameDataPool + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D.DPOORG_OID = orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D." + M01_Globals.g_anAccessModeId + " IN (" + String.valueOf(M01_Globals.g_workDataPoolId) + "," + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "D." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M01_Globals.g_schemaNameCtoDbAdmin + "' || " + "RIGHT(DIGITS(orgId),2) || RIGHT(DIGITS(poolId),1) || '." + unqualProcNameVaSync + "(?,?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_colCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_valCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "accumulate counter values", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET poolCount_out = poolCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET colCount_out  = colCount_out + v_colCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET valCount_out  = valCount_out + v_valCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "accumulate counter values", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET orgCount_out = orgCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, null, "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out", null, null, null, null, null, null, null);

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


private static void genVirtAttrSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseVirtAttr, null);

// ####################################################################################################################
// #    SP for Synchronizing Virtual Attributes
// ####################################################################################################################

String qualProcNameVaSync;
qualProcNameVaSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnVirtAttrSync, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing Virtual Attributes", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameVaSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", true, "number of columns synchronized");
M11_LRT.genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", false, "number of values updated");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_valCount", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all 'virtual attributes' (organization " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " / data pool " + M72_DataPool.g_pools.descriptors[thisPoolIndex].id + ")", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_EntityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isTv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmOrParEntitySection + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmOrParEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmOrParEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsTv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmAttribute + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = E." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityName + " = E." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntitySection + " = E." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsVirtual + " = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_EntityName A");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.isTv = L." + M01_Globals.g_anLdmIsGen);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + ") OR (P." + M01_Globals.g_anPoolTypeId + " IS NULL))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnVirtAttrSync + "_".toUpperCase() + "' || c_tableName || '(?, ?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_colCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_valCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "accumulate counter values", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET colCount_out = colCount_out + v_colCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET valCount_out = valCount_out + v_valCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

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

public static void genVirtAttrSupportForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forNlW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

if (!(M03_Config.supportVirtualColumns)) {
return;
}

String sectionName;
String entityName;
String entityShortName;
String entityTypeDescr;
boolean hasVirtualAttrs;
boolean hasExpBasedVirtualAttrs;
boolean hasRelBasedVirtualAttrs;
boolean supportMqt;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
supportMqt = M03_Config.generateLrt &  M03_Config.useMqtToImplementLrt & M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;

hasExpBasedVirtualAttrs = !(forNl &  ((forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInGenInclSubClasses) |  (!(forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses))));
hasRelBasedVirtualAttrs = !(forNl &  ((forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInGenInclSubClasses) |  (!(forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasRelBasedVirtualAttrInNonGenInclSubClasses))));
hasVirtualAttrs = hasExpBasedVirtualAttrs |  hasRelBasedVirtualAttrs;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefsInclSubClassesWithRepeat;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
return;
} else {
return;
}

if (!(hasVirtualAttrs)) {
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null, null);
String qualTabNameMqt;
if (supportMqt) {
qualTabNameMqt = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, null, null, null, null);
}

// ####################################################################################################################
// #    SP for syncing Virtual Attributes
// ####################################################################################################################

String unqualTabName;
unqualTabName = M04_Utilities.getUnqualObjName(qualTabName);

String qualProcNameVaSync;
qualProcNameVaSync = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, forNl, M01_ACM_IVK.spnVirtAttrSync, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for synchronizing Virtual Attributes " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"" + (forGen ? " (GEN)" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameVaSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (M03_Config.virtualColumnSyncCommitCount > 0) {
M11_LRT.genProcParm(fileNo, "IN", "commitCount_in", "INTEGER", true, "commit after this number of updates");
}

M11_LRT.genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", true, "number of table columns synchronized");
M11_LRT.genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", false, "number of values updated");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_valCount", "INTEGER", "NULL", null, null);
if (M03_Config.virtualColumnSyncCommitCount > 0) {
M11_LRT.genVarDecl(fileNo, "v_commitCount", "INTEGER", "100000", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowStart", "BIGINT", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowEnd", "BIGINT", "100000", null, null);
M11_LRT.genVarDecl(fileNo, "v_maxRow", "BIGINT", "NULL", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = 0;");

if (M03_Config.virtualColumnSyncCommitCount > 0) {
M11_LRT.genProcSectionHeader(fileNo, "determine number of rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_maxRow = (SELECT COUNT(1) FROM " + qualTabName + ");");
}

if (hasRelBasedVirtualAttrs) {
boolean colonMissing;
int relIndex;
Integer relNavDirection;
int sourceClassIndex;
int sourceOrParClassIndex;
int targetOrParClassIndex;
String virtAttrlist;
int numVirtAttrs;
boolean updateFromPriv;
int offset;

virtAttrlist = "";
numVirtAttrs = 0;
int i;
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forGen) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
virtAttrlist = virtAttrlist + ", " + M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].dbColName[ddlType];
numVirtAttrs = numVirtAttrs + 1;

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

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null, null) + " T");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

colonMissing = false;
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forGen) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
if (colonMissing) {
M00_FileWriter.printToFile(fileNo, ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].dbColName[ddlType]);
colonMissing = true;
}
}
}
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

colonMissing = false;
for (int i = 1; i <= attrRefsInclSubClasses.numDescriptors; i++) {
if (attrRefsInclSubClasses.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute &  attrRefsInclSubClasses.descriptors[i].refIndex > 0) {
if ((M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isTimeVarying == forGen) &  M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].isVirtual) {
if (colonMissing) {
M00_FileWriter.printToFile(fileNo, ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(S." + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].virtuallyMapsTo.mapTo + (M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum ? M01_Globals.gc_enumAttrNameSuffix : ""), ddlType, null, null, null, null, null, null) + ", T." + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].attributeName + (M24_Attribute.g_attributes.descriptors[attrRefsInclSubClasses.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum ? M01_Globals.gc_enumAttrNameSuffix : ""), ddlType, null, null, null, null, null, null) + ")");
colonMissing = true;
}
}
}
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, updateFromPriv, null, null, null, null, null) + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") TPar");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " TPar");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TPar." + fkAttrName + " = S." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null));

if (!(forLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TPar." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " S");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + " = TPar." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anValidFrom + " <= T." + M01_Globals_IVK.g_anValidFrom);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anValidTo + " >= T." + M01_Globals_IVK.g_anValidFrom);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + fkAttrName + " = S." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, updateFromPriv, null, null, null, null, null) + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " " + "WHERE " + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT " + M01_Globals.g_anOid + ", " + fkAttrName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null) + " " + "WHERE " + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") TPar");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " TPar");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TPar." + fkAttrName + " = S." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null));
if (!(forLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TPar." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null) + " S");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");

if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[targetOrParClassIndex].shortName, null, null, null, null) + " = TPar." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + fkAttrName + " = S." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

if (!(M22_Class.g_classes.descriptors[sourceClassIndex].hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anCid + " IN (" + M22_Class.g_classes.descriptors[sourceClassIndex].subclassIdStrListNonAbstract + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_valCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = valCount_out + v_valCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = colCount_out + " + String.valueOf(numVirtAttrs) + ";");
}

if (hasExpBasedVirtualAttrs) {
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt.toUpperCase() : ""), null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

String invalidColValue;
int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if ((M25_Domain.g_domains.descriptors[tabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etChar |  M25_Domain.g_domains.descriptors[tabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etClob | M25_Domain.g_domains.descriptors[tabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etLongVarchar | M25_Domain.g_domains.descriptors[tabColumns.descriptors[k].dbDomainIndex].dataType == M01_Common.typeId.etVarchar)) {
// this is a hack which works for string columns / need to add logic if we have virtual columns with other data types
invalidColValue = "''";
}

if (M03_Config.virtualColumnSyncCommitCount > 0) {
M11_LRT.genProcSectionHeader(fileNo, "loop over table and update column", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_rowStart <= v_maxRow DO");

M11_LRT.genProcSectionHeader(fileNo, "update virtual column \"" + tabColumns.descriptors[k].columnName.toUpperCase() + "\" in \"commit window\" of table\"" + qualTabName + "\"", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + tabColumns.descriptors[k].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (ORDER BY T." + M01_Globals.g_anOid + " ASC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tabColumns.descriptors[k].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + tabColumns.descriptors[k].columnName + " = " + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(V." + tabColumns.descriptors[k].columnName + "," + invalidColValue + ") <> COALESCE(" + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null) + "," + invalidColValue + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.ROWNUM BETWEEN v_rowStart AND v_rowEnd");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine next \"commit window\"", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_rowStart = v_rowEnd + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_rowEnd = v_rowStart + v_commitCount - 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");
} else {
M11_LRT.genProcSectionHeader(fileNo, "update virtual column \"" + tabColumns.descriptors[k].columnName.toUpperCase() + "\" in table\"" + qualTabName + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + tabColumns.descriptors[k].columnName + " = " + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(T." + tabColumns.descriptors[k].columnName + "," + invalidColValue + ") <> COALESCE(" + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null) + "," + invalidColValue + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_valCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = valCount_out + v_valCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = colCount_out + 1;");

}
}
}
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

// ### ENDIF IVK ###


}