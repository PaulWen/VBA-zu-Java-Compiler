package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M11_GroupIdAttrs {


// ### IF IVK ###



private static final int processingStep = 2;

public static final String tempTabNameGroupIdVals = "SESSION.GroupIdVals";
public static final String tempTabNameGroupIdOidMap = "SESSION.GroupIdOidMap";


public static void genGroupIdSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (!(M03_Config.supportGroupIdColumns)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genGroupIdSupportDdlByType(M01_Common.DdlTypeId.edtLdm);

genGroupIdSupportDdlByPool(null, null, null);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genGroupIdSupportDdlByType(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportUpdates) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genGroupIdSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genGroupIdSupportDdlByType(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int thisOrgId;
int thisPoolId;

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStep, ddlType, null, null, null, M01_Common.phaseGroupId, null);

// ####################################################################################################################
// #    SP for Synchronizing Group-ID Attributes
// ####################################################################################################################

String qualProcNameGaSync;
qualProcNameGaSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnGroupIdSync, ddlType, null, null, null, null, null, null);
String unqualProcNameGaSync;
unqualProcNameGaSync = M04_Utilities.getUnqualObjName(qualProcNameGaSync);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing Group-ID Attributes", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGaSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to synchronize groupID-attributes for");
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

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGaSync, ddlType, null, "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out", null, null, null, null, null, null, null);

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

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + M01_Globals.g_schemaNameCtoDbAdmin + "' || " + "RIGHT(DIGITS(orgId),2) || RIGHT(DIGITS(poolId),1) || '." + unqualProcNameGaSync + "(?,?)';");
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

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGaSync, ddlType, null, "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out", null, null, null, null, null, null, null);

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


private static void genGroupIdSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseGroupId, null);

// ####################################################################################################################
// #    SP for Synchronizing Group-ID Attributes
// ####################################################################################################################

String qualProcNameGaSync;
qualProcNameGaSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnGroupIdSync, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing Group-ID Attributes", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGaSync);
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
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all 'groupId attributes' (organization " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " / data pool " + M72_DataPool.g_pools.descriptors[thisPoolIndex].id + ")", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_EntityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

// FIXME: This is correct, but map this to ACM-Meta Model
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES('ASPECT', 'GENERICASPECT', '" + M01_Globals.gc_acmEntityTypeKeyClass + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmSchemaName);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_EntityName A");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.entityType = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.entityName = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.entitySection = L." + M01_Globals.g_anAcmEntitySection);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + ") OR (P." + M01_Globals.g_anPoolTypeId + " IS NULL))");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnGroupIdSync + "_".toUpperCase() + "' || c_TableName || '(" + (M03_Config.disableLoggingDuringSync ? "1," : "") + " ?, ?)';");
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

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

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


public static void genGroupIdSupportForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forNlW) {
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

if (!(M03_Config.supportGroupIdColumns |  forLrt)) {
return;
}

String sectionName;
String sectionShortName;
String entityName;
String entityShortName;
String entityTypeDescr;
boolean hasGroupIdAttrs;
int[] groupIdAttrIndexes;
boolean isPsTagged;
boolean supportMqt;

groupIdAttrIndexes =  new int[0];

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
supportMqt = M03_Config.generateLrt &  M03_Config.useMqtToImplementLrt & M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;

hasGroupIdAttrs = !(forNl & ! forGen & M22_Class.g_classes.descriptors[acmEntityIndex].hasGroupIdAttrInNonGenInclSubClasses);
if (hasGroupIdAttrs) {
groupIdAttrIndexes = M22_Class.g_classes.descriptors[acmEntityIndex].groupIdAttrIndexesInclSubclasses;
}

} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
return;
} else {
return;
}

if (!(hasGroupIdAttrs)) {
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

String targetTabVar;
String sourceTabVar;
String gidTabVar;
String crTabVar;
String gidColName;
String gidColShortName;
String[] subClassIdStrList = new String[5];//5 should be enough
String qualSeqNameGroupId;
int expGroupIdColNo;

// ####################################################################################################################
// #    SP for syncing Group-ID Attributes
// ####################################################################################################################

String qualProcNameGaSync;
qualProcNameGaSync = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnGroupIdSync, null, null, null, null);

boolean usePsOidFilter;
int i;
for (int i = 1; i <= 2; i++) {
usePsOidFilter = (i == 2);

M22_Class_Utilities.printSectionHeader("SP for synchronizing Group-ID Attributes " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"" + (forGen ? " (GEN)" : ""), fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGaSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (usePsOidFilter) {
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure");
}

if (M03_Config.disableLoggingDuringSync) {
M11_LRT.genProcParm(fileNo, "IN", "useCommitCount_in", M01_Globals.g_dbtBoolean, true, "iff '1': commit 'in between'");
}

M11_LRT.genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", true, "number of table columns synchronized");
M11_LRT.genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", false, "number of values updated");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_atEnd", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_valCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL", null, null);

M11_LRT.genVarDecl(fileNo, "v_commitCount", "INTEGER", "1000", null, null);
M11_LRT.genVarDecl(fileNo, "v_loopCount", "INTEGER", "0", null, null);
M00_FileWriter.printToFile(fileNo, "");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", (forLrt ? "T." + M01_Globals.g_anInLrt.toUpperCase() : ""), null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

String effectiveMaxLength;
boolean foundDomain;
String varNamePrefix1;
String varNamePrefix2;
int k;
for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
gidColName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName, ddlType, null, null, null, null, null, null);
gidColShortName = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName;
varNamePrefix1 = "v_" + entityShortName.toUpperCase() + "_" + gidColShortName.toUpperCase() + "_";
varNamePrefix2 = "v_" + gidColShortName.toUpperCase() + "_";

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genVarDecl(fileNo, "v_" + gidColShortName.toUpperCase() + String.valueOf(k), "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_" + entityShortName.toUpperCase() + "_OID" + String.valueOf(k), M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_" + entityShortName.toUpperCase() + "_" + gidColName.toUpperCase() + String.valueOf(k), "BIGINT", "NULL", null, null);

M00_FileWriter.printToFile(fileNo, "");

if (isPsTagged) {
M11_LRT.genVarDecl(fileNo, varNamePrefix1 + M01_ACM_IVK.conPsOid + String.valueOf(k), M01_Globals.g_dbtOid, "NULL", null, null);
}

expGroupIdColNo = 0;
int l;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
int m;
for (int m = 1; m <= tabColumns.numDescriptors; m++) {
if (tabColumns.descriptors[m].columnName == M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase()) {
if (tabColumns.descriptors[m].acmAttributeIndex > 0) {
if (M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength.compareTo("") == 0) {
effectiveMaxLength = "";
} else {
if (M03_Config.supportUnicode &  M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].supportUnicode) {
effectiveMaxLength = new Double(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].unicodeExpansionFactor * new Double(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength).intValue()).intValue() + "";
} else {
effectiveMaxLength = M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength;
}
}

M11_LRT.genVarDecl(fileNo, varNamePrefix1 + tabColumns.descriptors[m].columnName + String.valueOf(k), M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].scale, null, null), "NULL", null, null);
goto exitM;
}
}
}
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
// we currently only support exressions of type BIGINT
expGroupIdColNo = expGroupIdColNo + 1;
M11_LRT.genVarDecl(fileNo, varNamePrefix1 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo), "BIGINT", "NULL", null, null);
} else if (M00_Helper.inStr(1, M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase(), "VALID") > 0) {
M11_LRT.genVarDecl(fileNo, varNamePrefix1 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), "DATE", "NULL", null, null);
} else {
M11_LRT.genVarDecl(fileNo, varNamePrefix1 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), "BIGINT", "NULL", null, null);
}
exitM;

}

M00_FileWriter.printToFile(fileNo, "");
if (isPsTagged) {
M11_LRT.genVarDecl(fileNo, varNamePrefix2 + M01_ACM_IVK.conPsOid + String.valueOf(k), M01_Globals.g_dbtOid, "NULL", null, null);
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
for (int m = 1; m <= tabColumns.numDescriptors; m++) {
if (tabColumns.descriptors[m].columnName == M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase()) {
if (tabColumns.descriptors[m].acmAttributeIndex > 0) {
if (M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength.compareTo("") == 0) {
effectiveMaxLength = "";
} else {
if (M03_Config.supportUnicode &  M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].supportUnicode) {
effectiveMaxLength = new Double(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].unicodeExpansionFactor * new Double(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength).intValue()).intValue() + "";
} else {
effectiveMaxLength = M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].maxLength;
}
}

M11_LRT.genVarDecl(fileNo, varNamePrefix2 + tabColumns.descriptors[m].columnName + String.valueOf(k), M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[m].acmAttributeIndex].domainIndex].scale, null, null), "NULL", null, null);
goto exitMM;
}
}
}
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
// we currently only support exressions of type BIGINT
expGroupIdColNo = expGroupIdColNo + 1;
M11_LRT.genVarDecl(fileNo, varNamePrefix2 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo), "BIGINT", "NULL", null, null);
} else if (M00_Helper.inStr(1, M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase(), "VALID") > 0) {
M11_LRT.genVarDecl(fileNo, varNamePrefix2 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), "DATE", "NULL", null, null);
} else {
M11_LRT.genVarDecl(fileNo, varNamePrefix2 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), "BIGINT", "NULL", null, null);
}
exitMM;

}

}

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursors", null, null);
for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
gidColName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName, ddlType, null, null, null, null, null, null);
gidColShortName = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName;
subClassIdStrList[(k)] = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].acmEntityIndex].subclassIdStrListNonAbstract;
qualSeqNameGroupId = M04_Utilities.genQualObjName(M20_Section.getSectionIndexByName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].sectionName, null), "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName, "SEQ_" + entityShortName + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE " + gidColShortName.toLowerCase() + "Cursor" + gidColShortName.toUpperCase() + String.valueOf(k) + " CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l], thisOrgIndex, thisPoolIndex, ddlType, null, null, null) + " AS EXP_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCid + " IN (" + subClassIdStrList[k] + ")");

if (isPsTagged &  usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ISDELETED = 0");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXP_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
if (usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE tabCursor" + gidColShortName.toUpperCase() + String.valueOf(k) + " CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + gidColName + ",");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + ",");
}
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l], thisOrgIndex, thisPoolIndex, ddlType, null, null, null) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anCid + " IN (" + subClassIdStrList[k] + ")");

if (isPsTagged &  usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ISDELETED = 0");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + ",");
}
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l], thisOrgIndex, thisPoolIndex, ddlType, null, null, null) + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN " + gidColName.toUpperCase() + " IS NULL THEN 1 ELSE 0 END)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
if (usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

}

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_atEnd = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
gidColName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName, ddlType, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for GroupId-OID mapping (attribute \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName.toUpperCase() + "\")", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SESSION." + gidColName + "OidMap" + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + gidColName.toUpperCase() + " " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameGaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = 0;");

expGroupIdColNo = 0;
for (int k = M00_Helper.lBound(groupIdAttrIndexes); k <= M00_Helper.uBound(groupIdAttrIndexes); k++) {
targetTabVar = entityShortName.toUpperCase();
sourceTabVar = entityShortName.toUpperCase() + "1";
gidColName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName, ddlType, null, null, null, null, null, null);
gidColShortName = M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].shortName;
varNamePrefix1 = "v_" + entityShortName.toUpperCase() + "_" + gidColShortName.toUpperCase() + "_";
varNamePrefix2 = "v_" + gidColShortName.toUpperCase() + "_";

String qualTabNameSourceDataPool;
qualTabNameSourceDataPool = "";

if (thisPoolIndex != M01_Globals.g_workDataPoolIndex) {
qualTabNameSourceDataPool = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, forLrt, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "import group-ID column \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName.toUpperCase() + "\" in table \"" + qualTabName + "\" from work data pool", null, null);
} else if (thisOrgIndex != M01_Globals.g_primaryOrgIndex) {
qualTabNameSourceDataPool = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, forLrt, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "import group-ID column \"" + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].attributeName.toUpperCase() + "\" in table \"" + qualTabName + "\" from factory productive data pool", null, null);
}

if ((thisPoolIndex != M01_Globals.g_workDataPoolIndex) |  (thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " " + targetTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + targetTabVar + "." + gidColName + " = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + sourceTabVar + "." + gidColName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameSourceDataPool + " " + sourceTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + targetTabVar + "." + M01_Globals.g_anOid + " = " + sourceTabVar + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + targetTabVar + "." + M01_Globals.g_anCid + " IN (" + subClassIdStrList[k] + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + targetTabVar + "." + gidColName + " IS NULL");
if (isPsTagged &  usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
if (isPsTagged &  usePsOidFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_valCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET valCount_out = valCount_out + v_valCount;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF useCommitCount_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "process GroupId column \"" + gidColName + "\"" + " for classid " + subClassIdStrList[k], null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_atEnd = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN " + gidColShortName.toLowerCase() + "Cursor" + gidColShortName.toUpperCase() + String.valueOf(k) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN tabCursor" + gidColShortName.toUpperCase() + String.valueOf(k) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + gidColShortName.toLowerCase() + "Cursor" + gidColShortName.toUpperCase() + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix2 + M01_Globals_IVK.g_anPsOid + String.valueOf(k) + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix2 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix2 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "tabCursor" + gidColShortName.toUpperCase() + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_" + entityShortName.toUpperCase() + "_" + M01_Globals.g_anOid + String.valueOf(k) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_" + entityShortName.toUpperCase() + "_" + gidColName + String.valueOf(k) + ",");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix1 + M01_Globals_IVK.g_anPsOid + String.valueOf(k) + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix1 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + varNamePrefix1 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE (v_atEnd = 0) DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(v_atEnd = 0) AND");

int maxVarNameLength;
// Fixme: get rid of this hard-coding
maxVarNameLength = 29;
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(((" + M04_Utilities.paddRight(varNamePrefix1 + M01_Globals_IVK.g_anPsOid + String.valueOf(k), maxVarNameLength, null) + " IS NULL) AND (" + M04_Utilities.paddRight(varNamePrefix2 + M01_Globals_IVK.g_anPsOid + String.valueOf(k), maxVarNameLength, null) + " IS NULL)) OR (" + M04_Utilities.paddRight(varNamePrefix1 + M01_Globals_IVK.g_anPsOid + String.valueOf(k), maxVarNameLength, null) + " =  " + M04_Utilities.paddRight(varNamePrefix2 + M01_Globals_IVK.g_anPsOid + String.valueOf(k), maxVarNameLength, null) + ")) AND");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
String v1;
String v2;

if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
v1 = M04_Utilities.paddRight(varNamePrefix1 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(varNamePrefix2 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo), maxVarNameLength, null);
} else {
v1 = M04_Utilities.paddRight(varNamePrefix1 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), maxVarNameLength, null);
v2 = M04_Utilities.paddRight(varNamePrefix2 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k), maxVarNameLength, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(((" + v1 + " IS NULL) AND (" + v2 + " IS NULL)) OR (" + v1 + " =  " + v2 + "))" + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? " AND" : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF (v_" + gidColShortName.toUpperCase() + String.valueOf(k) + " IS NULL) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF (v_" + entityShortName.toUpperCase() + "_" + gidColName.toUpperCase() + String.valueOf(k) + " IS NULL) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_" + gidColShortName.toUpperCase() + String.valueOf(k) + " = NEXTVAL FOR " + qualSeqNameGroupId + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_" + gidColShortName.toUpperCase() + String.valueOf(k) + " = v_" + entityShortName.toUpperCase() + "_" + gidColName.toUpperCase() + String.valueOf(k) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_" + entityShortName.toUpperCase() + "_" + gidColName.toUpperCase() + String.valueOf(k) + " IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SESSION." + gidColName + "OidMap" + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + gidColName.toUpperCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_" + gidColShortName.toUpperCase() + String.valueOf(k) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_" + entityShortName.toUpperCase() + "_OID" + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tabCursor" + gidColShortName.toUpperCase() + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_" + entityShortName.toUpperCase() + "_" + M01_Globals.g_anOid + String.valueOf(k) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_" + entityShortName.toUpperCase() + "_" + gidColName + String.valueOf(k) + ",");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + varNamePrefix1 + M01_Globals_IVK.g_anPsOid + String.valueOf(k) + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + varNamePrefix1 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + varNamePrefix1 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + gidColShortName.toLowerCase() + "Cursor" + gidColShortName.toUpperCase() + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + varNamePrefix2 + M01_Globals_IVK.g_anPsOid + String.valueOf(k) + ",");
}

expGroupIdColNo = 0;
for (int l = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes); l++) {
if (M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].substring(0, 1) == "#") {
expGroupIdColNo = expGroupIdColNo + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + varNamePrefix2 + "EXP" + "_" + String.valueOf(k) + "_" + String.valueOf(expGroupIdColNo) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + varNamePrefix2 + M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes[l].toUpperCase() + String.valueOf(k) + (l < M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[groupIdAttrIndexes[k]].groupIdAttributes) ? "," : ""));
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_" + gidColShortName.toUpperCase() + String.valueOf(k) + " = NULL;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE " + gidColShortName.toLowerCase() + "Cursor" + gidColShortName.toUpperCase() + String.valueOf(k) + " WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE tabCursor" + gidColShortName.toUpperCase() + String.valueOf(k) + " WITH RELEASE;");

M11_LRT.genProcSectionHeader(fileNo, "update column in target table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_loopCount = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR oidLoop AS oidCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + gidColName.toUpperCase() + " AS mapped" + gidColName.toUpperCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " AS " + M01_Globals.g_anOid.toLowerCase() + "ToMap");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION." + gidColName + "OidMap" + String.valueOf(k));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabName + " " + targetTabVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + targetTabVar + "." + gidColName + " = mapped" + gidColName.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + targetTabVar + "." + M01_Globals.g_anOid + " = " + M01_Globals.g_anOid.toLowerCase() + "ToMap");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + targetTabVar + "." + gidColName + " IS NULL) OR (" + targetTabVar + "." + gidColName + " <> mapped" + gidColName.toUpperCase() + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_valCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET valCount_out = valCount_out + v_valCount;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_loopCount = v_loopCount + 1;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (v_loopCount = v_commitCount) AND (useCommitCount_in = 1) THEN");
M11_LRT.genProcSectionHeader(fileNo, "commit UOW", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_loopCount = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET colCount_out = colCount_out + 1;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF useCommitCount_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameGaSync, ddlType, null, "colCount_out", "valCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
}

// ### ENDIF IVK ###


}