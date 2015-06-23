package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M97_DataCheck {


// ### IF IVK ###


private static final int processingStepDataCheck = 4;

class xrefAttributeMappingForCl {
public String mapFrom;
public String mapTo;
public boolean isTv;
public boolean isNullable;
public String classIdStrList;
public String relIdStrList;

public xrefAttributeMappingForCl(String mapFrom, String mapTo, boolean isTv, boolean isNullable, String classIdStrList, String relIdStrList) {
this.mapFrom = mapFrom;
this.mapTo = mapTo;
this.isTv = isTv;
this.isNullable = isNullable;
this.classIdStrList = classIdStrList;
this.relIdStrList = relIdStrList;
}
}

class xrefAttributeMappingsForCl {
public int numMappings;
public M97_DataCheck.xrefAttributeMappingForCl[] mappings;

public xrefAttributeMappingsForCl(int numMappings, M97_DataCheck.xrefAttributeMappingForCl[] mappings) {
this.numMappings = numMappings;
this.mappings = mappings;
}
}


private static void initXrefAttributeMappingsForCl(M97_DataCheck.xrefAttributeMappingsForCl mapping) {
mapping.numMappings = 0;
}


private static void addXrefAttributeMappingForCl(M97_DataCheck.xrefAttributeMappingsForCl mapping, String mapFrom, String mapTo,  Integer acmEntityType, String acmEntityIdStrList,  Boolean isNullableW,  Boolean isTvW) {
boolean isNullable; 
if (isNullableW == null) {
isNullable = false;
} else {
isNullable = isNullableW;
}

boolean isTv; 
if (isTvW == null) {
isTv = false;
} else {
isTv = isTvW;
}

if (mapping.numMappings == 0) {
mapping.mappings =  new M97_DataCheck.xrefAttributeMappingForCl[M01_Common.gc_allocBlockSize];
}

int i;
for (int i = 1; i <= mapping.numMappings; i++) {
if (mapping.mappings[i].mapFrom.toUpperCase() == mapFrom.toUpperCase() &  mapping.mappings[i].mapTo.toUpperCase() == mapTo.toUpperCase() & mapping.mappings[i].isTv == isTv & mapping.mappings[i].isNullable == isNullable) {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
mapping.mappings[i].classIdStrList = mapping.mappings[i].classIdStrList + (mapping.mappings[i].classIdStrList.compareTo("") == 0 ? "" : ",") + acmEntityIdStrList;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
mapping.mappings[i].relIdStrList = mapping.mappings[i].relIdStrList + (mapping.mappings[i].relIdStrList.compareTo("") == 0 ? "" : ",") + acmEntityIdStrList;
}
return;
}
}

// mapping not found - add new one
if (mapping.numMappings >= M00_Helper.uBound(mapping.mappings)) {
M97_DataCheck.xrefAttributeMappingForCl[] mappingsBackup = mapping.mappings;
mapping.mappings =  new M97_DataCheck.xrefAttributeMappingForCl[mapping.numMappings + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M97_DataCheck.xrefAttributeMappingForCl value : mappingsBackup) {
mapping.mappings[indexCounter] = value;
indexCounter++;
}
}
mapping.numMappings = mapping.numMappings + 1;
mapping.mappings[mapping.numMappings].mapFrom = mapFrom;
mapping.mappings[mapping.numMappings].mapTo = mapTo;
mapping.mappings[mapping.numMappings].isTv = isTv;
mapping.mappings[mapping.numMappings].isNullable = isNullable;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
mapping.mappings[mapping.numMappings].classIdStrList = acmEntityIdStrList;
mapping.mappings[mapping.numMappings].relIdStrList = "";
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
mapping.mappings[mapping.numMappings].relIdStrList = acmEntityIdStrList;
mapping.mappings[mapping.numMappings].classIdStrList = "";
}
}


public static void genDataCheckUtilitiesDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
M97_DataCheck.genDataCheckUtilitiesDdlByDdl(M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M97_DataCheck.genDataCheckUtilitiesDdlByDdl(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M20_Section_Utilities.sectionValidForPoolAndOrg(M01_Globals.g_sectionIndexDataCheck, thisOrgIndex, thisPoolIndex)) {
genDataCheckUtilitiesDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}


public static void genDataCheckUtilitiesDdlByDdl(Integer ddlType) {
if (M03_Config.generateFwkTest) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataCheck, processingStepDataCheck, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

String qualProcName;

// ####################################################################################################################
// #    Procedure TESTDATA
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnTestData, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Generic SP for Testing Consistency of Data", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "message_in", "VARCHAR(1000)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "tbl_in", "VARCHAR(5000)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "stmt_in", "VARCHAR(5000)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "minCount_in", "INTEGER", true, null);
M11_LRT.genProcParm(fileNo, "IN", "maxCount_in", "INTEGER", true, null);
M11_LRT.genProcParm(fileNo, "IN", "countRecords_in", M01_Globals.g_dbtBoolean, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_cnt", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntCnt", "STATEMENT", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntRet", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE cntCursor   CURSOR FOR v_stmntCnt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR v_stmntRet;");

M11_LRT.genProcSectionHeader(fileNo, "wrap 'stmt_in' with COUNT-clause if required", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF countRecords_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET stmt_in = COALESCE(tbl_in, '') || 'SELECT COUNT(*) FROM (' || stmt_in ||') AS Q';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine number of records returned by 'stmt_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmntCnt FROM stmt_in;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN cntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH cntCursor INTO v_cnt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE cntCursor WITH RELEASE;");

M11_LRT.genProcSectionHeader(fileNo, "create return-message", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET stmt_in = 'SELECT ''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN v_cnt < minCount_in OR v_cnt > maxCount_in THEN 'ERROR' ELSE 'OK' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "' with ' || message_in ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "' - MINCOUNT: ' || RTRIM(CHAR(minCount_in)) || ' MAXCOUNT: ' || RTRIM(CHAR(maxCount_in)) || ' actual COUNT: ' || RTRIM(CHAR(v_cnt)) || ''' FROM SYSIBM.SYSDUMMY1';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmntRet FROM stmt_in;");

M11_LRT.genProcSectionHeader(fileNo, "return message to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN stmntCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Procedure GETVALUE
// ####################################################################################################################

String procNameSuffix;
String procParamDbType;
int i;
for (int i = 1; i <= 2; i++) {
if (i == 1) {
procNameSuffix = "";
procParamDbType = "VARCHAR(100)";
} else if (i == 2) {
procNameSuffix = "_BIGINT";
procParamDbType = "BIGINT";
}

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnGetValue + procNameSuffix, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP evaluating SQL-query and retrieving single result value", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "stmt_in", "VARCHAR(5000)", true, null);
M11_LRT.genProcParm(fileNo, "OUT", "value_out", procParamDbType, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare statements", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c1 CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "determine result value returned by 'stmt_in'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM stmt_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN c1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH c1 INTO value_out;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE c1 WITH RELEASE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    Function GETSCHEMA
// ####################################################################################################################

String qualFuncName;
qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM.udfnGetSchema, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function supporting data check", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtDbSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(RTRIM(LEFT(" + M01_Globals_IVK.g_anValue + ", 30)) AS VARCHAR(30))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anSection + " = 'DATACHECK' AND " + M01_Globals_IVK.g_anKey + " = 'SCHEMA' AND " + M01_Globals_IVK.g_anSubKey + " = 'CURRENT'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genDataCheckUtilitiesDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (M03_Config.generateFwkTest) {
return;
}

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

int thisPoolId;
if (thisPoolIndex > 0) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;
} else {
thisPoolId = -1;
}


//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataCheck, processingStepDataCheck, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseDbSupport, null);

String qualTabNameAggregationSlotGen;
qualTabNameAggregationSlotGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNameAggregationSlotGenNl;
qualTabNameAggregationSlotGenNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameCodeCategory;
qualTabNameCodeCategory = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String classIdStrMasterEndSlot;
classIdStrMasterEndSlot = M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexMasterEndSlot);

String qualTabNameEndSlot;
qualTabNameEndSlot = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameEndSlotGen;
qualTabNameEndSlotGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNameEndSlotGenNl;
qualTabNameEndSlotGenNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

String qualTabNameProperty;
qualTabNameProperty = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePropertyGen;
qualTabNamePropertyGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNamePropertyGenNl;
qualTabNamePropertyGenNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

String qualTabNameAggregationSlotHasNumericProperty;
qualTabNameAggregationSlotHasNumericProperty = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexAggregationSlotHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualTabNameCategoryHasNumericProperty;
qualTabNameCategoryHasNumericProperty = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCategoryHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualFuncName;

// ####################################################################################################################
// #    Function AGGRSLOTOID4LABEL
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnAggrSlotOid4Label, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of German label of AggregationSlot", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "aggregationSlotLabel_in", "VARCHAR(256)", true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AG.ASL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotGenNl + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotGen + " AG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AG." + M01_Globals.g_anOid + " = NL.ASL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.LABEL = aggregationSlotLabel_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AG." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function AGGRSLOTOID4PROP
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnAggrSlotOid4Prop, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of AggregationSlot assigned to given property", fileNo, null, null);
M22_Class_Utilities.printSectionHeader("assumes that a maximum of only one AggregationSlot is assigned to the property", fileNo, true, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "propertyOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ANP.ASL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameAggregationSlotHasNumericProperty + " ANP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ANP.NPR_OID = propertyOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function CATOID4CODE
// ####################################################################################################################

String qualFuncNameCatOid4Code;
qualFuncNameCatOid4Code = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnCatOid4Code, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of the Category for a given Code", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameCatOid4Code);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.GCO_OID = GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID = GC.CDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTLABEL4OID
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotLabel4Oid, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of German label of EndSlot", fileNo, null, null);
M22_Class_Utilities.printSectionHeader("assumption: no history in ENDSLOT_GEN (1:1 with ENDSLOT)", fileNo, true, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "endSlotOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(240)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN LOCATE(' ', NL.LABEL) > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "THEN '''' || NL.LABEL || ''''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE NL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END AS ES_LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGenNl + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.ESL_OID = EG." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID = endSlotOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4CODE
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4Code, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of EndSlot corresponding to the given Code", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.GCO_OID = GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlot + " ES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES.ESCESC_OID = CC.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID = GC.CDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anCid + " = '" + classIdStrMasterEndSlot + " '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4CODE_OL
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4CodeOL, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of EndSlot corresponding to the given Code (ignoring 'Lack')", fileNo, null, null);
M22_Class_Utilities.printSectionHeader("assumption: no history in ENDSLOT_GEN (1:1 with ENDSLOT)", fileNo, true, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.GCO_OID = GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlot + " ES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES.ESCESC_OID = CC.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID = ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID = GC.CDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anCid + " = '" + classIdStrMasterEndSlot + " '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals_IVK.g_anSlotType + " < 5");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4CODE_ST
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4CodeST, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning OID of EndSlot corresponding to the given Code and slot type", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "", "slotType_in", M01_Globals.g_dbtEnumId, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.GCO_OID = GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlot + " ES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES.ESCESC_OID = CC.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID = ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID = GC.CDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anCid + " = '" + classIdStrMasterEndSlot + " '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals_IVK.g_anSlotType + " = slotType_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4CODE_TB
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4CodeTB, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning list of EndSlot-OIDs corresponding to the given Code", fileNo, null, null);
M22_Class_Utilities.printSectionHeader("only applicable to paint slots", fileNo, true, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABLE (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC.GCO_OID = GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlot + " ES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES.ESCESC_OID = CC.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID = ES." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID = GC.CDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ES." + M01_Globals.g_anCid + " = '" + classIdStrMasterEndSlot + " '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ASSIGNEDPAINTZONEKEY =''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4LABEL
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4Label, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning EndSlot-OID for German label", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "endSlotLabel_in", "VARCHAR(256)", true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGenNl + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals.g_anOid + " = NL.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.LABEL = endSlotLabel_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function ENDSLOTOID4LZCODE
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnEndSlotOid4LzCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning PaintZone-EndSlot-OID for given PaintZone-Code", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGen + " EG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals_IVK.g_anSlotType + " = 5");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG.ASSIGNEDPAINTZONEKEY = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EG." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function GCOOID4CODE
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnGcoOid4Code, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning Code-OID for given CodeNumber in Division", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "divOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals_IVK.g_anCodeNumber + " = codeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC.CDIDIV_OID = divOid_in");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function NPROID4CODE
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnNprOid4Code, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning NumericProperty-OID for given Code and PropertyLabel", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "", "label_in", "VARCHAR(255)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN.NPR_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryHasNumericProperty + " CN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGen + " PG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN.NPR_OID = PG.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGenNl + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG." + M01_Globals.g_anOid + " = NL.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualFuncNameCatOid4Code + "(codeNumber_in, psOid_in) = CN.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPPER(NL.LABEL) = UPPER(label_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function NPROID4CODE_ID
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnNprOid4CodeId, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning NumericProperty-OID for given Code and PropertyTemplate-ID", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "codeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "", "templateId_in", M01_Globals.g_dbtEnumId, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN.NPR_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryHasNumericProperty + " CN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProperty + " PR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN.NPR_OID = PR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, true) + " PT");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " PT");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PR.PTMHTP_OID = PT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CN." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT.ID = templateId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualFuncNameCatOid4Code + "(codeNumber_in, psOid_in) = CN.CAT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function PROPOID4PROPLABEL
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.udfnPropOid4PropLabel, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function returning Property-OID for given label", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "propertyLabel_in", "VARCHAR(50)", true, null);
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGen + " PG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGenNl + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG." + M01_Globals.g_anOid + " = NL.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.LABEL = propertyLabel_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt &  (M72_DataPool.g_pools.descriptors[thisPoolIndex].id != M71_Org.g_orgs.descriptors[thisOrgIndex].setProductiveTargetPoolId))) {
goto NormalExit;
}

if (M03_Config.genDataCheckCl) {
String qualTabNameChangeLog;
qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    Procedure verifying content of change log
// ####################################################################################################################

String qualProcNameCheckChangeLog;
qualProcNameCheckChangeLog = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnCheckChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP verifying content of ChangeLog", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameCheckChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "SMALLINT", false, "number of consistency violations found");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_cnt", "INTEGER", "0", null, null);

M97_DataCheck.xrefAttributeMappingsForCl xrefMapping;
initXrefAttributeMappingsForCl(xrefMapping);

int i;
for (int i = 1; i <= M24_Attribute.g_attributes.numDescriptors; i++) {
if (!(M04_Utilities.strArrayIsNull(M24_Attribute.g_attributes.descriptors[i].mapsToChangeLogAttributes))) {
int k;
for (int k = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[i].mapsToChangeLogAttributes); k <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[i].mapsToChangeLogAttributes); k++) {
addXrefAttributeMappingForCl(xrefMapping, M24_Attribute.g_attributes.descriptors[i].attributeName, M24_Attribute.g_attributes.descriptors[i].mapsToChangeLogAttributes[k], M24_Attribute.g_attributes.descriptors[i].cType, (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass ? M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].subclassIdStrListNonAbstract : M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].relIdStr), null, null);
}
}
}

for (int i = 1; i <= xrefMapping.numMappings; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CL-attribute (xref)" + xrefMapping.mappings[i].mapFrom + " maps to " + xrefMapping.mappings[i].mapTo + "[" + xrefMapping.mappings[i].isTv + " / " + xrefMapping.mappings[i].isNullable + " / " + xrefMapping.mappings[i].classIdStrList + " / " + xrefMapping.mappings[i].relIdStrList + "]");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.GEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.NL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.OPERATION_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id) + " AS ORGID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(thisPoolId) + " AS ACCESSMODEID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + xrefMapping.mappings[i].mapTo.toUpperCase() + " AS COLUMN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (!(xrefMapping.mappings[i].classIdStrList.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityId + " IN (" + xrefMapping.mappings[i].classIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}
if (!(xrefMapping.mappings[i].relIdStrList.compareTo("") == 0)) {
if (!(xrefMapping.mappings[i].classIdStrList.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityId + " IN (" + xrefMapping.mappings[i].relIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + xrefMapping.mappings[i].mapTo.toUpperCase() + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.GEN,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.NL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL.OPERATION_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + xrefMapping.mappings[i].mapTo.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (!(xrefMapping.mappings[i].classIdStrList.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- analogously check via aggregate head");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + M01_Globals.g_anAhCid + " IN (" + xrefMapping.mappings[i].classIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CL." + xrefMapping.mappings[i].mapTo.toUpperCase() + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
}

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