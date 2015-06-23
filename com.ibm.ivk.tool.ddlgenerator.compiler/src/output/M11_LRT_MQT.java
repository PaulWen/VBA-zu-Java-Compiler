package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M11_LRT_MQT {




private static final String pc_tempTabNamePrivOid = "SESSION.PrivOid";
private static final String pc_tempTabNamePubOid = "SESSION.PubOid";

private static final int processingStep = 2;


public static void genLrtMqtSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genLrtMqtSupportDdlByType(M01_Common.DdlTypeId.edtLdm);

genLrtMqtSupportDdlByPool(null, null, null);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genLrtMqtSupportDdlByType(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genLrtMqtSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genLrtMqtSupportDdlByType(Integer ddlTypeW) {
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
// ### IF IVK ###
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, null, null, null, M01_Common.phaseLrtMqt, M01_Common.ldmIterationPoolSpecific);
// ### ELSE IVK ###
// fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, , , , phaseLrtMqt, ldmIterationPoolSpecific)
// ### ENDIF IVK ###

// ####################################################################################################################
// #    SP for Synchronizing LRT-MQTs
// ####################################################################################################################

String qualProcNameMqtSync;
qualProcNameMqtSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnLrtMqtSync, ddlType, null, null, null, null, null, null);
String unqualProcNameMqtSync;
unqualProcNameMqtSync = M04_Utilities.getUnqualObjName(qualProcNameMqtSync);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing LRT-MQTs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameMqtSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to synchronize MQTs for");
M11_LRT.genProcParm(fileNo, "OUT", "orgCount_out", "INTEGER", true, "number of organizations processed");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of tables synchronized");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "BIGINT", false, "number of rows affected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET orgCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all 'matching' organizations", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID");
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

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].supportLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + M01_Globals.g_schemaNameCtoDbAdmin + "' || " + "CAST(RIGHT('00' || RTRIM(CAST(ID AS CHAR(2))),2) || '" + String.valueOf(M72_DataPool.g_pools.descriptors[i].id) + "' AS CHAR(3)) || '." + unqualProcNameMqtSync + "(?,?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_tabCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "accumulate counter values", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET orgCount_out = orgCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + v_tabCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

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


private static void genLrtMqtSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrtMqt, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    SP for Synchronizing LRT-MQTs
// ####################################################################################################################

String qualProcNameMqtSync;
qualProcNameMqtSync = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnLrtMqtSync, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Synchronizing LRT-MQTs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameMqtSync);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of tables synchronized");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "BIGINT", false, "number of rows affected");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
M11_LRT.genProcSectionHeader(fileNo, "loop over all 'LRT-MQT-tables'", null, null);
} else {
M11_LRT.genProcSectionHeader(fileNo, "loop over all 'LRT-MQT-tables' (organization " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " / data pool " + M72_DataPool.g_pools.descriptors[thisPoolIndex].id + ")", null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT ");
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmTableName + " AS c_tableName");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LM." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LM." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = LM." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LM." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LM." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LM." + M01_Globals.g_anLdmIsNl + " = L." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LM." + M01_Globals.g_anLdmIsGen + " = L." + M01_Globals.g_anLdmIsGen);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmUseLrtMqt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
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

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_schemaName || '." + "MqtSync_".toUpperCase() + "' || c_tableName || '(?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "accumulate counter values", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameMqtSync, ddlType, null, "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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

public static void genLrtMqtSupportForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNoTab, int fileNoView, int fileNoFk, int fileNoMqt, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Boolean isPurelyPrivateW) {
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

if (!(M03_Config.useMqtToImplementLrt)) {
return;
}

boolean poolSuppressUniqueConstraints;
boolean M72_DataPool.poolSupportLrt;
if (thisPoolIndex > 0) {
poolSuppressUniqueConstraints = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressUniqueConstraints;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

int orMappingRootEntityIndex;
int sectionIndex;
String sectionName;
String entityName;
String entityShortName;
String entityTypeDescr;
String tabSpaceData;
int tabSpaceIndexData;
String tabSpaceLong;
int tabSpaceIndexLong;
String tabSpaceIndex;
int tabSpaceIndexIndex;
boolean useValueCompression;
boolean isCommonToOrgs;
boolean isCommonToPools;
// ### IF IVK ###
boolean tableIsPsTagged;
boolean isPsTagged;
boolean psTagOptional;
// ### ENDIF IVK ###
boolean isVolatile;
boolean isAggHead;
// ### IF IVK ###
boolean objSupportsPsDpFilter;
// ### ENDIF IVK ###
boolean useMqtToImplementLrtForEntity;
boolean hasVirtualAttrs;
// ### IF IVK ###
Integer isAllowedCountriesRel;
Integer isDisallowedCountriesRel;
// ### ENDIF IVK ###
String acFkColName;
int acClassIndex;
int acOoParClassIndex;
// ### IF IVK ###
String qualCountryListFuncName;
// ### ENDIF IVK ###
String acColName;
int acColLength;
// ### IF IVK ###
boolean condenseData;
boolean expandExpressionsInFtoView;
String fkAttrToDiv;
boolean isDivTagged;
boolean supportPartitionByClassId;

boolean useDivOidWhereClause;
boolean useDivRelKey;

isAllowedCountriesRel = M01_Common.RelNavigationMode.ernmNone;
isDisallowedCountriesRel = M01_Common.RelNavigationMode.ernmNone;
// ### ENDIF IVK ###
acClassIndex = -1;
acOoParClassIndex = -1;
// ### IF IVK ###
qualCountryListFuncName = "";
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {

useDivOidWhereClause = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! isPsTagged;
useDivRelKey = (acmEntityIndex == M01_Globals_IVK.g_classIndexGenericCode) & ! forNl;

sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
orMappingRootEntityIndex = M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex;
useValueCompression = M22_Class.g_classes.descriptors[acmEntityIndex].useValueCompression;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
// ### ENDIF IVK ###
isVolatile = M22_Class.g_classes.descriptors[acmEntityIndex].isVolatile;
isAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].isAggHead & ! forGen & !forNl;
// ### IF IVK ###
tableIsPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
objSupportsPsDpFilter = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
expandExpressionsInFtoView = M22_Class.g_classes.descriptors[acmEntityIndex].expandExpressionsInFtoView;

supportPartitionByClassId = M03_Config.supportRangePartitioningByClassId & ! forNl & M22_Class.g_classes.descriptors[acmEntityIndex].subClassIdStrSeparatePartition.numMaps > 0;

hasVirtualAttrs = !(forNl &  ((forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInGenInclSubClasses) |  (!(forGen &  M22_Class.g_classes.descriptors[acmEntityIndex].hasExpBasedVirtualAttrInNonGenInclSubClasses))));

isDivTagged = (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex > 0) & ! (M22_Class.g_classes.descriptors[acmEntityIndex].classIndex == M01_Globals_IVK.g_classIndexProductStructure);
if (isDivTagged) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].navPathToDiv.navDirectionToClass == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else if (forNl) {
fkAttrToDiv = M01_ACM_IVK.conDivOid;
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
}
// ### ENDIF IVK ###

tabSpaceData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceData;
tabSpaceIndexData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexData;
tabSpaceLong = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceLong;
tabSpaceIndexLong = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexLong;
tabSpaceIndex = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndex;
tabSpaceIndexIndex = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexIndex;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
orMappingRootEntityIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex;
useValueCompression = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useValueCompression;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = false;
// ### ENDIF IVK ###
isVolatile = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isVolatile;
isAggHead = false;
// ### IF IVK ###
tableIsPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged &  (M03_Config.usePsTagInNlTextTables | ! forNl);
objSupportsPsDpFilter = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
condenseData = false;
expandExpressionsInFtoView = false;
supportPartitionByClassId = false;
hasVirtualAttrs = false;

isAllowedCountriesRel = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isAllowedCountries;
isDisallowedCountriesRel = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isDisallowedCountries;

if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].isDisallowedCountries != M01_Common.RelNavigationMode.ernmNone |  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isAllowedCountries != M01_Common.RelNavigationMode.ernmNone) {
acClassIndex = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].isDisallowedCountries == M01_Common.RelNavigationMode.ernmLeft |  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isAllowedCountries == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex : M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex);
acOoParClassIndex = M22_Class.g_classes.descriptors[acClassIndex].orMappingSuperClassIndex;
acFkColName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[acClassIndex].shortName, null, null, null, null);
}

if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].isAllowedCountries != M01_Common.RelNavigationMode.ernmNone) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex, M01_ACM_IVK.udfnAllowedCountry2Str0, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
acColName = M01_Globals_IVK.g_anAllowedCountries;
acColLength = M01_Globals_IVK.gc_allowedCountriesMaxLength;
} else if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].isDisallowedCountries != M01_Common.RelNavigationMode.ernmNone) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex, M01_ACM_IVK.udfnDisallowedCountry2Str0, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
acColName = M01_Globals_IVK.g_anDisAllowedCountries;
acColLength = M01_Globals_IVK.gc_disallowedCountriesMaxLength;
}

isDivTagged = false;
if (!(forNl)) {
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftIsDivision) {
isDivTagged = true;
fkAttrToDiv = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[acmEntityIndex].rlShortRelName, null, null, null, null);
} else if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightIsDivision) {
isDivTagged = true;
fkAttrToDiv = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[acmEntityIndex].lrShortRelName, null, null, null, null);
}
}

// ### ENDIF IVK ###
tabSpaceData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceData;
tabSpaceIndexData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexData;
tabSpaceLong = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceLong;
tabSpaceIndexLong = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexLong;
tabSpaceIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndex;
tabSpaceIndexIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexIndex;
} else {
return;
}

if (!(useMqtToImplementLrtForEntity)) {
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameMqt;
String qualTabNamePriv;
String qualTabNamePub;
String qualTabNameMqtLdm;
qualTabNameMqt = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, forNl, null, null, null);
qualTabNamePriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, false, forNl, null, null, null);
qualTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, false, forNl, null, null, null);
qualTabNameMqtLdm = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, true, true, forNl, null, null, null);

int i;
// ### IF IVK ###
boolean showDeletedObjectsInView;
boolean filterForPsDpMapping;
boolean filterForPsDpMappingExtended;
// ### ENDIF IVK ###
String qualViewName;
String qualViewNameLdm;
String qualAcTableName;
boolean propToPriv;
// ### IF IVK ###
Integer tabPartitionType;
// ### ENDIF IVK ###

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! M72_DataPool.poolSupportLrt) {
return;
// this is handled with non-MQT-LRT
}

// ####################################################################################################################
// #    MQT for LRT-Views
// ####################################################################################################################

M96_DdlSummary.addTabToDdlSummary(qualTabNameMqt, ddlType, false);

M78_DbMeta.registerQualTable(qualTabNameMqtLdm, qualTabNameMqt, orMappingRootEntityIndex, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, ddlType, false, forGen, true, forNl, true);

if (M03_Config.generateDdlCreateTable) {
M22_Class_Utilities.printChapterHeader("LRT-MQT-Table for " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"" + (forGen ? " (GEN)" : "") + (forNl ? " (NL)" : ""), fileNoTab);

M00_FileWriter.printToFile(fileNoTab, "");
M00_FileWriter.printToFile(fileNoTab, M04_Utilities.addTab(0) + "CREATE TABLE");
M00_FileWriter.printToFile(fileNoTab, M04_Utilities.addTab(1) + qualTabNameMqt);
M00_FileWriter.printToFile(fileNoTab, M04_Utilities.addTab(0) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTab, null, null, ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, true, M01_Common.DdlOutputMode.edomDecl |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomDeclVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTab, ddlType, thisOrgIndex, thisPoolIndex, 1, true, forGen, M01_Common.DdlOutputMode.edomDecl |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomDeclVirtual, null);
}
// ### ELSE IVK ###
//   If forNl Then
//     genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTab, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, True, edomDecl Or edomMqtLrt
//   Else
//     genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTab, ddlType, thisOrgIndex, thisPoolIndex, 1, True, forGen, edomDecl Or edomMqtLrt
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTab, ")");

// ### IF IVK ###
M22_Class.genTabDeclTrailer(fileNoTab, ddlType, isDivTagged, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, forNl, true, true, supportPartitionByClassId, fkAttrToDiv, tabPartitionType);
// ### ELSE IVK ###
//   genTabDeclTrailer fileNoTab, ddlType, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, forNl, True, True
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoTab, M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
if ((forNl & ! isPsTagged) |  isVolatile) {
// ### ELSE IVK ###
// If forNl Or isVolatile Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoTab, "");
M00_FileWriter.printToFile(fileNoTab, M04_Utilities.addTab(0) + "ALTER TABLE " + qualTabNameMqt + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
}

// ### IF IVK ###
//Defect 19643 wf
//Einmaliger Aufruf: Indexe fuer VL6CPST011.PROPERTY_GEN_LRT_MQT
M76_Index.genIndexesForEntity(qualTabNameMqt, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoTab, ddlType, forGen, true, true, forNl, poolSuppressUniqueConstraints, tabPartitionType);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & ! forNl) {
M24_Attribute.genFKsForRelationshipsByClassRecursive(qualTabNameMqt, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forGen, true, true, tabPartitionType);
}
// ### ELSE IVK ###
// genIndexesForEntity qualTabNameMqt, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoTab, ddlType, forGen, True, True, forNl, poolSuppressUniqueConstraints
//
// If acmEntityType = eactClass And Not forNl Then
//  genFKsForRelationshipsByClassRecursive qualTabNameMqt, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forGen, True, True
// End If
// ### ENDIF IVK ###

String qualTriggerName;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "_INS", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, , "INS")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Insert-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);
M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

boolean printedHeader;
// ### IF IVK ###
int numVirtualAttrs;
int numVirtualAttrsInstantiated;
printedHeader = false;
numVirtualAttrs = 0;
numVirtualAttrsInstantiated = 0;

if (hasVirtualAttrs) {
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomXref, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomXref, null);
}

int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
numVirtualAttrs = numVirtualAttrs + 1;
if (tabColumns.descriptors[k].isInstantiated) {
numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1;
}

}
}

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if (!(printedHeader)) {
M11_LRT.genProcSectionHeader(fileNoMqt, "declare variables", null, null);
printedHeader = true;
}
M11_LRT.genVarDecl(fileNoMqt, "v_" + tabColumns.descriptors[k].acmAttributeName, M25_Domain.getDbDatatypeByDomainIndex(tabColumns.descriptors[k].dbDomainIndex), "NULL", null, null);
}
}

printedHeader = false;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
if (!(printedHeader)) {
M11_LRT.genProcSectionHeader(fileNoMqt, "initialize variables", null, null);
printedHeader = true;
}
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET " + "v_" + tabColumns.descriptors[k].acmAttributeName + " = " + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueVirtual, null, null, true, null) + ";");
}
}

M11_LRT.genProcSectionHeader(fileNoMqt, "update virtual columns in public table", null, null);

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");

boolean firstAttr;
firstAttr = true;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if ((tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) &  tabColumns.descriptors[k].isInstantiated) {
if (!(firstAttr)) {
M00_FileWriter.printToFile(fileNoMqt, ",");
}
firstAttr = false;
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "PUB." + tabColumns.descriptors[k].columnName + " = v_" + tabColumns.descriptors[k].acmAttributeName);
}
}

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoMqt, "propagate INSERT to MQT-table", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt
// Else
//   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2 + numVirtualAttrs, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
// initAttributeTransformation transformation, 2, , , , gc_newRecordName & "."
// ### ENDIF IVK ###
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInUseBy, "(SELECT LRT.UTROWN_OID FROM " + qualTabNameLrt + " LRT WHERE LRT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt + ")", null, null, null);

// ### IF IVK ###
if (hasVirtualAttrs) {
numVirtualAttrs = 0;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
numVirtualAttrs = numVirtualAttrs + 1;

M24_Attribute_Utilities.setAttributeMapping(transformation, 2 + numVirtualAttrs, tabColumns.descriptors[k].columnName, "v_" + tabColumns.descriptors[k].acmAttributeName, null, null, null);
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueExpression, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueExpression, null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomMqtLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ");");

// ### IF IVK ###
if ((isAllowedCountriesRel |  isDisallowedCountriesRel) &  M03_Config.maintainVirtAttrInTriggerPubOnRelTabs) {
// update in public -> propagate to private and public table
for (int i = 1; i <= 2; i++) {
propToPriv = (i == 2);
qualAcTableName = M04_Utilities.genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate INSERT to table \"" + qualAcTableName + "\"", 1, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualAcTableName + " E");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + acColName + " = " + qualCountryListFuncName + "(" + M01_Globals.gc_newRecordName + "." + acFkColName + (propToPriv ? ", E." + M01_Globals.g_anInLrt + "" : "") + ", " + String.valueOf(acColLength) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + acFkColName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
}
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "L_INS", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , True, , "INS")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Insert-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePriv + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);
M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

// ### IF IVK ###
numVirtualAttrs = 0;
numVirtualAttrsInstantiated = 0;

if (hasVirtualAttrs) {
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
transformation.doCollectVirtualAttrDescriptors = true;
transformation.doCollectAttrDescriptors = true;
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomXref, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomXref, null);
}

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) {
numVirtualAttrs = numVirtualAttrs + 1;
if (tabColumns.descriptors[k].isInstantiated) {
numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1;
}

}
}
}

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNoMqt, "propagate INSERT to MQT-table", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt Or edomMqtLrt
// Else
//   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");

// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2 + numVirtualAttrsInstantiated, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
// initAttributeTransformation transformation, 2, , , , gc_newRecordName & "."
// ### ENDIF IVK ###
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInUseBy, "(SELECT LRT.UTROWN_OID FROM " + qualTabNameLrt + " LRT WHERE LRT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt + ")", null, null, null);

// ### IF IVK ###
if (hasVirtualAttrs) {
numVirtualAttrsInstantiated = 0;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if ((tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacVirtual) != 0 &  tabColumns.descriptors[k].isInstantiated) {
numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1;
M24_Attribute_Utilities.setAttributeMapping(transformation, 2 + numVirtualAttrsInstantiated, tabColumns.descriptors[k].columnName, M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[k].columnName.toUpperCase(), null, null, null);
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueExpression, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueExpression, null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt Or edomMqtLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "_UPD", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, , "UPD")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Update-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER UPDATE ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate UPDATE to MQT-table", null, true);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conOid, "", null, null, null);

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
// Else
//   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conOid, "", null, null, null);

transformation.attributePrefix = M01_Globals.gc_newRecordName + ".";
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt
// End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbFalse);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "MQT", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
// ### IF IVK ###

if ((isAllowedCountriesRel |  isDisallowedCountriesRel) &  M03_Config.maintainVirtAttrInTriggerPubOnRelTabs) {
// update in public -> propagate to private and public table
for (int i = 1; i <= 2; i++) {
propToPriv = (i == 2);
qualAcTableName = M04_Utilities.genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate UPDATE to table \"" + qualAcTableName + "\"", 1, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualAcTableName + " E");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + acColName + " = " + qualCountryListFuncName + "(" + M01_Globals.gc_newRecordName + "." + acFkColName + (propToPriv ? ", E." + M01_Globals.g_anInLrt + "" : "") + ", " + String.valueOf(acColLength) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + acFkColName);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "E", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "IF " + M01_Globals.gc_newRecordName + "." + acFkColName + " <> " + M01_Globals.gc_oldRecordName + "." + acFkColName + " THEN");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualAcTableName + " E");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "E." + acColName + " = " + qualCountryListFuncName + "(" + M01_Globals.gc_oldRecordName + "." + acFkColName + (propToPriv ? ", E." + M01_Globals.g_anInLrt + "" : "") + ", " + String.valueOf(acColLength) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + acFkColName);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "E", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "END IF;");
}
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "L_UPD", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , "UPD")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Update-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePriv + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER UPDATE ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate UPDATE to MQT-table", null, true);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbTrue, null, null, null);

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
// Else
//   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

transformation.attributePrefix = M01_Globals.gc_newRecordName + ".";
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, M01_Globals.gc_newRecordName, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anInLrt, true);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt
// End If
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anInLrt);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "MQT", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "_DEL", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "DEL")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Delete-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);
M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER DELETE ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePub);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate DELETE to MQT-table", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbFalse);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "MQT", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

// ### IF IVK ###
if ((isAllowedCountriesRel |  isDisallowedCountriesRel) &  M03_Config.maintainVirtAttrInTriggerPubOnRelTabs) {
// delete in public -> propagate to private and public table
for (int i = 1; i <= 2; i++) {
propToPriv = (i == 2);
qualAcTableName = M04_Utilities.genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate DELETE to table \"" + qualAcTableName + "\"", 1, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualAcTableName + " E");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + acColName + " = " + qualCountryListFuncName + "(" + M01_Globals.gc_oldRecordName + "." + acFkColName + (propToPriv ? ", E." + M01_Globals.g_anInLrt : "") + ", " + String.valueOf(acColLength) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + acFkColName);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "E", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
}
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! forNl, null, null, null, null, (forNl ? "NLTXT" : "") + "L_DEL", null, null);
// ### ENDIF IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , "DEL")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Delete-Trigger for maintaining LRT-MQT-table for table \"" + qualTabNamePriv + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoMqt, null, null);
M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "AFTER DELETE ON");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNoMqt, "propagate DELETE to MQT-table", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anInLrt + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbTrue);
M11_LRT.genDdlPsDivClause(fileNoMqt, 2, "MQT", M01_Globals.gc_oldRecordName, M01_Globals.gc_oldRecordName, isPsTagged, M03_Config.usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNoMqt, "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    View for providing LRT-specific set of public + private records
// ####################################################################################################################

// ### IF IVK ###
// we need to generate four views
//   - one not filtering out deleted objects (first loop)
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / second loop)
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / third loop)
//   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (fourth loop)
// filtering deleted objects / not filtering by PSDPMAPPING is done in fourth loop since this view is the one used in subsequent trigger definitions
for (int i = 1; i <= 4; i++) {
showDeletedObjectsInView = (i == 1);
filterForPsDpMapping = (i == 2);
filterForPsDpMappingExtended = (i == 3);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

// ### IF IVK ###
if (filterForPsDpMapping &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextII;
}
if (filterForPsDpMappingExtended &  (!(M03_Config.supportFilteringByPsDpMapping | ! objSupportsPsDpFilter))) {
goto NextII;
}

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, forNl, null, (showDeletedObjectsInView ? "D" : "") + (filterForPsDpMapping ? "I" : (filterForPsDpMappingExtended ? "J" : "")), null, null);

M22_Class_Utilities.printSectionHeader("View to 'filter' private and public LRT rows of MQT-table \"" + qualTabNameMqt + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNoView, null, "(" + (showDeletedObjectsInView ? "" : "do not ") + "retrieve deleted objects" + (M03_Config.supportFilteringByPsDpMapping ? " / " + (filterForPsDpMapping |  filterForPsDpMappingExtended ? "" : "do not ") + "filter by PSDPMAPPING" : "") + ")");
// ### ELSE IVK ###
//   qualViewName = _
//     genQualViewNameByEntityIndex( _
//       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, forNl _
//     )
//
//   printSectionHeader "View to 'filter' private and public LRT rows of MQT-table """ & qualTabNameMqt & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoView
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, "");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + "(");

if (!(forGen & ! forNl)) {
M04_Utilities.printConditional(fileNoView, M04_Utilities.genAttrDeclByDomain(M01_ACM.conWorkingState, M01_ACM.conWorkingState, M24_Attribute_Utilities.AttrValueType.eavtEnum, M21_Enum.getEnumIndexByName(M01_ACM.dxnWorkingState, M01_ACM.dnWorkingState, null), acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT', 'STATUS_ID' AND 'INUSEBY'
M04_Utilities.printConditional(fileNoView, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta, null, 1, true, null), null, null);
M04_Utilities.printConditional(fileNoView, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, 1, true, null), null, null);
M04_Utilities.printConditional(fileNoView, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, 1, true, null), null, null);
}

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoView, "", null, ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, false, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListNonLrt | M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 1, false, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListNonLrt | M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//   If forNl Then
//     genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoView, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, _
//       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
//   Else
//     genAttrListForEntity acmEntityIndex, acmEntityType, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, _
//       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + "AS");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "SELECT");

if (!(forGen & ! forNl)) {
// ### IF IVK ###
if (condenseData) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M11_LRT.workingStateUnlocked) + "),");
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "CASE MQT." + M01_Globals.g_anIsLrtPrivate);
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "WHEN 0 THEN");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "WHEN MQT." + M01_Globals.g_anInLrt + " IS NULL THEN " + String.valueOf(M11_LRT.workingStateUnlocked));
// ### IF IVK ###
if (!((filterForPsDpMapping |  filterForPsDpMappingExtended))) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "WHEN MQT." + M01_Globals.g_anInLrt + " = " + M01_Globals.g_activeLrtOidDdl + " THEN " + String.valueOf(M11_LRT.workingLockedInActiveTransaction));
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "WHEN LRT.UTROWN_OID = (SELECT UTROWN_OID FROM " + qualTabNameLrt + " WHERE OID = " + M01_Globals.g_activeLrtOidDdl + ") THEN " + String.valueOf(M11_LRT.workingLockedInInactiveTransaction));
if (!(showDeletedObjectsInView &  isAggHead)) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "WHEN (" + M02_ToolMeta.getActiveLrtOidStrDdl(ddlType, thisOrgIndex) + " = '') AND (RTRIM(CURRENT CLIENT_USERID) = (SELECT USR." + M01_Globals.g_anUserId + " FROM " + M01_Globals.g_qualTabNameUser + " USR WHERE USR." + M01_Globals.g_anOid + " = LRT.UTROWN_OID)) THEN " + String.valueOf(M11_LRT.workingLockedInInactiveTransaction));
}
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "ELSE " + String.valueOf(M11_LRT.workingLockedByOtherUser));
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + String.valueOf(M11_LRT.workingLockedInActiveTransaction));
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "),");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (condenseData) {
// virtually merge-in columns 'INLRT', 'STATUS_ID' and 'INUSEBY'
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(CASE MQT." + M01_Globals.g_anIsLrtPrivate + " WHEN 1 THEN " + String.valueOf(M86_SetProductive.statusWorkInProgress) + " ELSE " + String.valueOf(M86_SetProductive.statusProductive) + " END),");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

// ### ENDIF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, "MQT.", null, null, null, null, null, null, null, null, null, null, null);

// ### IF IVK ###
if (filterForPsDpMapping |  filterForPsDpMappingExtended) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInUseBy, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInUseBy, "LRT.UTROWN_OID", null, null, null);
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoView, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListNonLrt | M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListNonLrt | M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}
// ### ELSE IVK ###
//   setAttributeMapping transformation, 1, conInUseBy, "LRT.UTROWN_OID"
//
//   If forNl Then
//     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoView, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , _
//       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
//   Else
//     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, _
//       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");

// ### IF IVK ###
if (tableIsPsTagged &  (filterForPsDpMapping |  filterForPsDpMappingExtended)) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "MQT." + M01_Globals_IVK.g_anPsOid + " = PSDPM.PSOID");

if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM_SP");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(PSDPM_SP.PSOID = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + ")");
}
}

// ### ENDIF IVK ###
// ### IF IVK ###
if (!((filterForPsDpMapping |  filterForPsDpMappingExtended))) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + qualTabNameLrt + " LRT");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "MQT." + M01_Globals.g_anInLrt + " = LRT." + M01_Globals.g_anOid);
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(1) + "WHERE");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbFalse);
if ((!(condenseData &  (filterForPsDpMapping |  filterForPsDpMappingExtended)))) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}

// ### IF IVK ###
if (!((filterForPsDpMapping |  filterForPsDpMappingExtended))) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "MQT." + M01_Globals.g_anInLrt + " <> " + M01_Globals.g_activeLrtOidDdl);
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(6) + "OR");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "MQT." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + ")");

// ### IF IVK ###
if (!(showDeletedObjectsInView & ! condenseData)) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "OR");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anIsLrtPrivate + " = " + M01_LDM.gc_dbTrue);
if (!(showDeletedObjectsInView)) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anLrtState + " <> " + String.valueOf(M11_LRT.lrtStatusDeleted));
}
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anInLrt + " = " + M01_Globals.g_activeLrtOidDdl);
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + ")");

// ### IF IVK ###
if (tableIsPsTagged & ! (filterForPsDpMapping |  filterForPsDpMappingExtended)) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
if (M03_Config.usePsFltrByDpMappingForRegularViews) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "(MQT." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(PUB." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(3) + "(MQT." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(2) + ")");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNoView, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, null, null, null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(sectionIndex, (forNl ? M04_Utilities.genNlObjName(entityName, null, null, null) : entityName), isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, false, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, true, showDeletedObjectsInView, filterForPsDpMapping, filterForPsDpMappingExtended, "LRT-View" + (showDeletedObjectsInView ? " (include deleted objects)" : "") + (M03_Config.supportFilteringByPsDpMapping ? " (" + (filterForPsDpMapping ? "" : "do not ") + "filter by PSDPMAPPING)" : "") + " \"" + sectionName + "." + entityName + "\"", null, true, tableIsPsTagged, objSupportsPsDpFilter, null, forNl, null, null);
// ### ELSE IVK ###
//     genAliasDdl sectionIndex, IIf(forNl, genNlObjName(entityName), entityName), _
//                 isCommonToOrgs, isCommonToPools, True, _
//                 qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
//                 "LRT-View" & " """ & sectionName & "." & entityName & """", , True , forNl
// ### ENDIF IVK ###
}
// ### IF IVK ###
NextII:
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

// ####################################################################################################################
// #    SP for syncing MQT with base tables
// ####################################################################################################################

String unqualTabName;
unqualTabName = M04_Utilities.getUnqualObjName(qualTabNamePriv);

String qualProcNameMqtSync;
qualProcNameMqtSync = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, forNl, "MqtSync", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for synchronizing LRT-MQT for " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"" + (forGen ? " (GEN)" : "") + (forNl ? " (NL)" : "") + " with underlying base tables", fileNoMqt, null, null);

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + qualProcNameMqtSync);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNoMqt, "OUT", "rowCount_out", "BIGINT", false, "number of rows affected");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNoMqt, "declare variables", null, true);
M11_LRT.genVarDecl(fileNoMqt, "v_rowCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNoMqt, "v_OidCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNoMqt, "v_lBound", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNoMqt, "v_uBound", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNoMqt, "v_numRowsPerUow", "BIGINT", "1000000", null, null);
M07_SpLogging.genSpLogDecl(fileNoMqt, null, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "temporary table for private OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + pc_tempTabNamePrivOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "seqNo INTEGER,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "oid   " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNoMqt, 1, true, true, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "temporary table for public OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + pc_tempTabNamePubOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "seqNo INTEGER,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "oid   " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNoMqt, 1, true, true, null);

M07_SpLogging.genSpLogProcEnter(fileNoMqt, qualProcNameMqtSync, ddlType, null, "v_useLogging_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNoMqt, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNoMqt, "delete records from MQT not found in base tables", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "MQT." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "MQT." + M01_Globals.g_anIsLrtPrivate);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "NOT IN");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "PRIV." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "MQT." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "MQT." + M01_Globals.g_anIsLrtPrivate);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "NOT IN");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "PUB." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "0");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "update public records in MQT differing in base table", null, null);

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conIsLrtPrivate, "", null, null, null);

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
// Else
//   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");

transformation.attributePrefix = "PUB.";
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB", null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, False, , edomListNonLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , False, forGen, edomListNonLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "WHERE");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "( MQT." + M01_ACM.conIsLrtPrivate + " = 0 )");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "( MQT." + M01_Globals.g_anOid + " = PUB." + M01_Globals.g_anOid + " )");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "EXISTS");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "WHERE");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "( MQT." + M01_ACM.conIsLrtPrivate + " = 0 )");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "( MQT." + M01_Globals.g_anOid + " = PUB." + M01_Globals.g_anOid + " )");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "(");
boolean firstCol;
firstCol = true;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
// ### IF IVK ###
if (((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacOid) != M01_Common.AttrCategory.eacOid) &  ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != M01_Common.AttrCategory.eacExpression)) {
// ### ELSE IVK ###
//     If (.columnCategory And eacOid) <> eacOid Then
// ### ENDIF IVK ###
if (!(firstCol)) {
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(7) + "OR");
}
if (false) {
// todo: consider null-values here
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "(COALESCE(MQT." + tabColumns.descriptors[i].columnName + ", PUB." + tabColumns.descriptors[i].columnName + ") IS NOT NULL AND (MQT." + tabColumns.descriptors[i].columnName + " IS NULL OR PUB." + tabColumns.descriptors[i].columnName + " IS NULL OR MQT." + tabColumns.descriptors[i].columnName + " <> PUB." + tabColumns.descriptors[i].columnName + "))");
} else {
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "MQT." + tabColumns.descriptors[i].columnName + " <> PUB." + tabColumns.descriptors[i].columnName);
}
firstCol = false;
}
}
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "update private records in MQT differing in base table", null, null);

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conIsLrtPrivate, "", null, null, null);

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
// Else
//   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");

transformation.attributePrefix = "PRIV.";
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV", "PRIV." + M01_Globals.g_anInLrt, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "WHERE");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "( MQT." + M01_ACM.conIsLrtPrivate + " = 1 )");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "( MQT." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid + " )");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "EXISTS");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "WHERE");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "( MQT." + M01_ACM.conIsLrtPrivate + " = 1 )");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "( MQT." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid + " )");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + "(");
firstCol = true;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
// ### IF IVK ###
if (((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacOid) != M01_Common.AttrCategory.eacOid) &  ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != M01_Common.AttrCategory.eacExpression)) {
// ### ELSE IVK ###
//     If (.columnCategory And eacOid) <> eacOid Then
// ### ENDIF IVK ###
if (!(firstCol)) {
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(7) + "OR");
}
if (false) {
// todo: consider null-values here
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "(COALESCE(MQT." + tabColumns.descriptors[i].columnName + ", PRIV." + tabColumns.descriptors[i].columnName + ") IS NOT NULL AND (MQT." + tabColumns.descriptors[i].columnName + " IS NULL OR PRIV." + tabColumns.descriptors[i].columnName + " IS NULL OR MQT." + tabColumns.descriptors[i].columnName + " <> PRIV." + tabColumns.descriptors[i].columnName + "))");
} else {
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(6) + "MQT." + tabColumns.descriptors[i].columnName + " <> PRIV." + tabColumns.descriptors[i].columnName);
}
firstCol = false;
}
}
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine private OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + pc_tempTabNamePrivOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "seqNo,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY PRIV." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + qualTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "PRIV." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "NOT IN");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anIsLrtPrivate);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine number of OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_OidCount = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "loop over 'sliding window on OIDs' to restrict to a maximum number of records processed in a single UOW", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET v_lBound = 1;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHILE v_lBound <= v_OidCount DO");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine upper bound of 'sliding OID-window'", 2, true);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET v_uBound = v_lBound + v_numRowsPerUow - 1;");

M11_LRT.genProcSectionHeader(fileNoMqt, "insert records in MQT found in 'sliding OID-window' of LRT-private table but not in MQT", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualTabNameMqt);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, true, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, edomListLrt Or edomMqtLrt
// Else
//   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, "PRIV.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV", "PRIV." + M01_Globals.g_anInLrt, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInUseBy, "(SELECT LRT.UTROWN_OID FROM " + qualTabNameLrt + " LRT WHERE PRIV." + M01_Globals.g_anInLrt + " = LRT." + M01_Globals.g_anOid + ")", null, null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomValueVirtualNonPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomMqtLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualTabNamePriv + " PRIV,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + pc_tempTabNamePrivOid + " W");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "W." + M01_Globals.g_anOid + " = PRIV." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "W.SEQNO BETWEEN v_lBound AND v_uBound");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNoMqt, "commit UOW", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine next upper bound of sliding window", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET v_lBound = v_uBound + 1;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "END WHILE;");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine public OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + pc_tempTabNamePubOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "seqNo,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY PUB." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "PUB." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "NOT IN");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "MQT." + M01_Globals.g_anIsLrtPrivate);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + qualTabNameMqt + " MQT");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine number of OIDs to INSERT", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_OidCount = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "loop over 'sliding window on OIDs' to restrict to a maximum number of records processed in a single UOW", null, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "SET v_lBound = 1;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "WHILE v_lBound <= v_OidCount DO");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine upper bound of 'sliding OID-window'", 2, true);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET v_uBound = v_lBound + v_numRowsPerUow - 1;");

M11_LRT.genProcSectionHeader(fileNoMqt, "insert records in MQT found in 'sliding OID-window' of LRT-public table but not in MQT", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualTabNameMqt);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "(");

// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, null, null, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt Or edomMqtLrt
// Else
//   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, "PUB.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB", null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conIsLrtPrivate, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInUseBy, "(SELECT LRT.UTROWN_OID FROM " + qualTabNameLrt + " LRT WHERE PUB." + M01_Globals.g_anInLrt + " = LRT." + M01_Globals.g_anOid + ")", null, null, null);
// ### IF IVK ###
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, null, false, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomValueVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone), null);
}
// ### ELSE IVK ###
// If forNl Then
//   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomMqtLrt
// Else
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomMqtLrt
// End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + qualTabNamePub + " PUB,");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + pc_tempTabNamePubOid + " W");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "W." + M01_Globals.g_anOid + " = PUB." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(3) + "W.SEQNO BETWEEN v_lBound AND v_uBound");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNoMqt, "");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNoMqt, "commit UOW", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "COMMIT;");

M11_LRT.genProcSectionHeader(fileNoMqt, "determine next upper bound of sliding window", 2, null);
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(2) + "SET v_lBound = v_uBound + 1;");
M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(1) + "END WHILE;");

M07_SpLogging.genSpLogProcExit(fileNoMqt, qualProcNameMqtSync, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNoMqt, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNoMqt, M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}




}