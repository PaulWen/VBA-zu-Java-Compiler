package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M72_DataPool {




private static final int colDataPool = 2;
private static final int colName = colDataPool + 1;
private static final int colShortName = colName + 1;
private static final int colSpecificToOrg = colShortName + 1;
// ### IF IVK ###
private static final int colSupportLRT = colSpecificToOrg + 1;
// ### ELSE IVK ###
//Private Const colSupportLRT = colSpecificToOrg + 1
// ### ENDIF IVK ###
// ### IF IVK ###
private static final int colSupportViewsForPsTag = colSupportLRT + 1;
private static final int colSupportTriggerForPsTag = colSupportViewsForPsTag + 1;
private static final int colSupportXmlExport = colSupportTriggerForPsTag + 1;
private static final int colSupportUpdates = colSupportXmlExport + 1;
// ### ELSE IVK ###
//Private Const colSupportUpdates = colSupportLRT + 1
// ### ENDIF IVK ###
private static final int colSuppressRefIntegrity = colSupportUpdates + 1;
private static final int colSuppressUniqueConstraints = colSuppressRefIntegrity + 1;
// ### IF IVK ###
private static final int colInstantiateExpressions = colSuppressUniqueConstraints + 1;
private static final int colCommonItemsLocal = colInstantiateExpressions + 1;
// ### ELSE IVK ###
//Private Const colCommonItemsLocal = colSuppressUniqueConstraints + 1
// ### ENDIF IVK ###
private static final int colSupportAcm = colCommonItemsLocal + 1;
private static final int colIsActive = colSupportAcm + 1;
// ### IF IVK ###
private static final int colIsProductive = colIsActive + 1;
private static final int colIsArchive = colIsProductive + 1;
private static final int colSupportNationalization = colIsArchive + 1;
private static final int colSequenceCacheSize = colSupportNationalization + 1;
// ### ELSE IVK ###
//Private Const colSequenceCacheSize = colIsActive + 1
// ### ENDIF IVK ###

private static final int firstRow = 3;

private static final String sheetName = "DP";

private static final int processingStepOidSeq = 3;
private static final int processingStepUdf = 5;
private static final int processingStepSp = 5;

public static M72_DataPool_Utilities.DataPoolDescriptors g_pools;

private static void readSheet() {
int thisPoolId;

M72_DataPool_Utilities.initDataPoolDescriptors(M72_DataPool.g_pools);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colDataPool).getStringCellValue() + "" != "") {
thisPoolId = new Double(M00_Excel.getCell(thisSheet, thisRow, colDataPool).getStringCellValue()).intValue();

M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].id = thisPoolId;
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].name = M00_Excel.getCell(thisSheet, thisRow, colName).getStringCellValue().trim();
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].specificToOrgId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSpecificToOrg).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportLrt = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportLRT).getStringCellValue(), null);

// ### IF IVK ###
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportViewsForPsTag = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportViewsForPsTag).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportTriggerForPsTag = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportTriggerForPsTag).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportXmlExport).getStringCellValue(), null);
// ### ENDIF IVK ###
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportUpdates = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportUpdates).getStringCellValue(), null);

M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].suppressRefIntegrity = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSuppressRefIntegrity).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].suppressUniqueConstraints = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSuppressUniqueConstraints).getStringCellValue(), null);
// ### IF IVK ###
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].instantiateExpressions = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colInstantiateExpressions).getStringCellValue(), null);
// ### ENDIF IVK ###
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].commonItemsLocal = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colCommonItemsLocal).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportAcm = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportAcm).getStringCellValue(), null);

M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].isActive = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsActive).getStringCellValue(), null);
// ### IF IVK ###
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].isArchive = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsArchive).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].isProductive = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsProductive).getStringCellValue(), null);
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].supportNationalization = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSupportNationalization).getStringCellValue(), null);
// ### ENDIF IVK ###

M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].sequenceCacheSize = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceCacheSize).getStringCellValue(), -1);
// ### IF IVK ###

if (M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].isArchive & ! M03_Config.supportArchivePool) {
M72_DataPool.g_pools.descriptors[M72_DataPool_Utilities.allocDataPoolIndex(M72_DataPool.g_pools)].isActive = false;
}
// ### ENDIF IVK ###
thisRow = thisRow + 1;
}
}


public static void getDataPools() {
if (M72_DataPool.g_pools.numDescriptors == 0) {
readSheet();
}
}


public static void resetDataPools() {
M72_DataPool.g_pools.numDescriptors = 0;
}


public static void cleanupPools() {
int srcIndex;
int dstIndex;
dstIndex = 1;
for (srcIndex = 1; srcIndex <= 1; srcIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[srcIndex].isActive) {
if (!(srcIndex.compareTo(dstIndex) == 0)) {
M72_DataPool.g_pools.descriptors[(dstIndex)] = M72_DataPool.g_pools.descriptors[srcIndex];
}
dstIndex = dstIndex + 1;
}
}
M72_DataPool.g_pools.numDescriptors = dstIndex - 1;
}


public static Integer getDataPoolIndexById(int poolId) {
Integer returnValue;
int i;

returnValue = -1;
M72_DataPool.getDataPools();

for (i = 1; i <= 1; i += (1)) {
if (M72_DataPool.g_pools.descriptors[i].id == poolId) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static String getDataPoolNameByIndex(int poolIndex) {
String returnValue;
returnValue = "";
if ((poolIndex > 0)) {
returnValue = M72_DataPool.g_pools.descriptors[poolIndex].name;
}

return returnValue;
}


public static Boolean poolIsValidForOrg( int thisPoolIndex,  int thisOrgIndex) {
Boolean returnValue;
if (thisPoolIndex < 1 |  thisOrgIndex < 1) {
returnValue = true;
} else {
returnValue = (M72_DataPool.g_pools.descriptors[thisPoolIndex].specificToOrgId == -1 |  M72_DataPool.g_pools.descriptors[thisPoolIndex].specificToOrgId == M71_Org.g_orgs.descriptors[thisOrgIndex].id);
}
return returnValue;
}


// ### IF IVK ###
public static Boolean poolSupportsArchiving(int poolId) {
Boolean returnValue;
returnValue = false;

if (poolId != -1) {
int i;

for (i = 1; i <= 1; i += (1)) {
if (M72_DataPool.g_pools.descriptors[i].id == poolId) {
returnValue = M72_DataPool.g_pools.descriptors[i].isArchive;
return returnValue;
}
}
}
return returnValue;
}


// ### ENDIF IVK ###
public static Boolean poolSupportLrt(int poolId) {
Boolean returnValue;
returnValue = false;

if (poolId != -1) {
int i;

for (i = 1; i <= 1; i += (1)) {
if (M72_DataPool.g_pools.descriptors[i].id == poolId) {
returnValue = M72_DataPool.g_pools.descriptors[i].supportLrt;
return returnValue;
}
}
}
return returnValue;
}


public static void genDataPoolDdl( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer forOrgIndexW, Integer ddlTypeW) {
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

int forOrgIndex; 
if (forOrgIndexW == null) {
forOrgIndex = -1;
} else {
forOrgIndex = forOrgIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (thisPoolIndex > 0) {
if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm)) {
return;
}
}
} else {
return;
}

if (thisOrgIndex > 0 &  thisPoolIndex > 0) {
if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal)) {
return;
}
}

int fileNo;

fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStepOidSeq, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);

M71_Org.genOidSequenceForOrg(thisOrgIndex, fileNo, ddlType, forOrgIndex);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genDataPoolDdl2( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if ((!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm)) |  M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive) {
return;
}
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexAspect, processingStepUdf, ddlType, thisOrgIndex, thisPoolIndex, null, null, M01_Common.ldmIterationPoolSpecific);

M72_DataPool.genSrxUDFsByPool(M01_Common.SrxTypeId.estSr0, thisOrgIndex, thisPoolIndex, fileNo, ddlType);
M72_DataPool.genSrxUDFsByPool(M01_Common.SrxTypeId.estSr1, thisOrgIndex, thisPoolIndex, fileNo, ddlType);
M72_DataPool.genSrxUDFsByPool(M01_Common.SrxTypeId.estNsr1, thisOrgIndex, thisPoolIndex, fileNo, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### ENDIF IVK ###
public static void genDataPoolDdl3( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
return;
}

boolean M72_DataPool.poolSupportLrt;
int thisPoolId;

if ((!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm)) |  M72_DataPool.g_pools.descriptors[thisPoolIndex].specificToOrgId > 0) {
return;
}

thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;

int thisOrgId;
thisOrgId = M72_DataPool.g_pools.descriptors[thisOrgIndex].id;

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStepSp, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseLrt, null);

String qualProcName;
// ####################################################################################################################
// #    SP for checking consistency of DB2 register
// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnCheckDb2Register, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for checking consistency of DB2 register", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "regVarLrtOid_in", "VARCHAR(128)", true, "value of registry variable holding LRT-OID");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "regVarPsOid_in", "VARCHAR(128)", true, "value of registry variable holding PS-OID");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "regVarSchema_in", "VARCHAR(128)", true, "value of registry variable holding current schema");
M11_LRT.genProcParm(fileNo, "IN", "forLrt_in", "INTEGER", false, "'1' iff LRT-context is required, '0' if LRT-context is required to be empty, NULL if no restrictions on LRT-context apply");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
// ### IF IVK ###
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtPsOid", M01_Globals.g_dbtOid, "NULL", null, null);
// ### ENDIF IVK ###
M11_LRT.genVarDecl(fileNo, "v_lrtOrgId", M01_Globals.g_dbtEnumId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_schemaOrgIdStr", "VARCHAR(2)", null, null, null);

M00_FileWriter.printToFile(fileNo, "");
int indent;
indent = 1;
if (M72_DataPool.poolSupportLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF forLrt_in = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "verify that DB2 register for LRTOID is set", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(regVarLrtOid_in, '') = '' THEN");
M79_Err.genSignalDdl("lrtContextNotSet", fileNo, 3, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF forLrt_in = " + M01_LDM.gc_dbFalse + " THEN");
indent = 2;
}
M11_LRT.genProcSectionHeader(fileNo, "verify that DB2 register for LRTOID is NOT set", indent, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "IF COALESCE(regVarLrtOid_in, '') <> '' THEN");
M79_Err.genSignalDdl("lrtContextSet", fileNo, indent + 1, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "END IF;");

if (M72_DataPool.poolSupportLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "this check is temporarily disabled", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN 0;");

M11_LRT.genProcSectionHeader(fileNo, "use default values for input parameter if no values are provided", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(regVarLrtOid_in, '') = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET regVarLrtOid_in = '0';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(regVarPsOid_in, '') = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET regVarPsOid_in = '0';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET regVarSchema_in  = COALESCE(regVarSchema_in, CURRENT SCHEMA);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_dbtOid + "(regVarLrtOid_in);");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid  = " + M01_Globals.g_dbtOid + "(regVarPsOid_in);");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOrgId  = v_lrtOid / 1" + "000000000000000000000000000000000000".substring(0, M01_LDM.gc_sequenceEndValue.length()) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_schemaOrgIdStr  = LEFT(RIGHT(regVarSchema_in, 3),2);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtOid <> 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "verify that DB2 register for LRT-OID is consistent with organization", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lrtOrgId <> " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " THEN");

M79_Err.genSignalDdlWithParms("lrtContextInconsistentWithOrg", fileNo, 3, null, null, null, null, null, null, null, null, null, "COALESCE(RTRIM(CHAR(v_lrtOid)), '')", "RTRIM(CHAR(" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "))", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

// ### IF IVK ###
if (M72_DataPool.poolSupportLrt) {
String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT " + M01_Globals_IVK.g_anPsOid + " INTO v_lrtPsOid FROM " + qualTabNameLrt + " WHERE OID = v_lrtOid WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that LRT OID is valid", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_lrtPsOid IS NULL THEN");
M79_Err.genSignalDdlWithParms("lrtContextInvalid", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_lrtOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "verify that DB2 register for PS-OID is consistent with LRT", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF COALESCE(v_psOid, -1) <> COALESCE(v_lrtPsOid, -1) THEN");
M79_Err.genSignalDdlWithParms("psInconsistentWithLrt", fileNo, 3, null, null, null, null, null, null, null, null, null, "COALESCE(RTRIM(CHAR(v_psOid)),'')", "COALESCE(RTRIM(CHAR(v_lrtPsOid)),'')", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that CURRENT SCHEMA is consistent with organization", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF regVarSchema_in <> '' THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_schemaOrgIdStr <> RIGHT(DIGITS(" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "),2) THEN");
M79_Err.genSignalDdlWithParms("schemaInconsistentWithOrg", fileNo, 3, null, null, null, null, null, null, null, null, null, "regVarSchema_in", "RTRIM(CHAR(" + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + "))", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M22_Class_Utilities.printSectionHeader("SP for checking consistency of DB2 register", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "forLrt_in", "INTEGER", false, "'1' iff LRT-context is required, '0' otherwise");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, "");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcName + "(" + M01_LDM.gc_db2RegVarLrtOid + ", " + M01_Globals_IVK.gc_db2RegVarPsOid + ", " + M01_LDM.gc_db2RegVarSchema + ", forLrt_in);");
// ### ELSE IVK ###
// Print #fileNo, addTab(1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarSchema; ", forLrt_in);"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
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


public static void genDataPoolsDdl(Integer ddlType) {
int thisPoolIndex;
int thisOrgIndex;
int forOrgIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
// ### IF IVK ###
M72_DataPool.genDataPoolDdl2(null, null, M01_Common.DdlTypeId.edtLdm);
// ### ENDIF IVK ###
M72_DataPool.genDataPoolDdl3(null, null, M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M72_DataPool.genDataPoolDdl(null, null, null, M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
for (forOrgIndex = 1; forOrgIndex <= 1; forOrgIndex += (1)) {
M72_DataPool.genDataPoolDdl(thisOrgIndex, thisPoolIndex, forOrgIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}

for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
// ### IF IVK ###
M72_DataPool.genDataPoolDdl2(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
// ### ENDIF IVK ###
M72_DataPool.genDataPoolDdl3(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
// ### IF IVK ###


public static void genSrxUDFsByPool(Integer srxType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String srxTypeStr;
srxTypeStr = M04_Utilities.genSrxType2Str(srxType);

String qualFuncName;
String qualTabNameGenericAspect;
String qualTabNameGenericCode;
qualFuncName = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, srxTypeStr + "Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String colPrefix;
if (srxType == M01_Common.SrxTypeId.estSr0) {
colPrefix = "S0CS";
} else if (srxType == M01_Common.SrxTypeId.estSr1) {
colPrefix = "S1CT";
} else if (srxType == M01_Common.SrxTypeId.estNsr1) {
colPrefix = "N1CN";
} else {
colPrefix = "XXXX";// should not happen
}

M22_Class_Utilities.printSectionHeader("Function for concatenating " + srxTypeStr + "-Context-OIDs for \"Aspect\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, false, "OID of an 'Aspect'-object");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(220)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_oidIdList", "VARCHAR(220)", "''", null, null);

M11_LRT.genProcSectionHeader(fileNo, "add each OID contributing to the SR0Context", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "01_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "02_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "03_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "04_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "05_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "06_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "07_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "08_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "09_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "10_OID FROM " + qualTabNameGenericAspect + " WHERE OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_oidIdList = v_oidIdList || (CASE V_oidIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(OID AS CHAR(20)));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
//  Print #fileNo, addTab(1); "RETURN (CASE WHEN v_oidIdList = '' THEN NULL ELSE v_oidIdList END);"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_oidIdList;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

qualFuncName = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, srxTypeStr + "Ctxt_CDE", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function for concatenating " + srxTypeStr + "-Context-CodeNumbers for \"Aspect\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "oid_in", M01_Globals.g_dbtOid, false, "OID of an 'Aspect'-object");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(159)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumList", "VARCHAR(159)", "''", null, null);

M11_LRT.genProcSectionHeader(fileNo, "add each " + M01_Globals_IVK.g_anCodeNumber + " contributing to the SR0Context", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_ACM_IVK.conCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "01_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "02_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "03_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "04_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "05_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "06_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "07_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "08_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "09_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT C." + M01_Globals_IVK.g_anCodeNumber + " FROM " + qualTabNameGenericCode + " C," + qualTabNameGenericAspect + " A WHERE A." + colPrefix + "10_OID = C." + M01_Globals.g_anOid + " AND A." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_ACM_IVK.conCodeNumber + " AS c_codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_ACM_IVK.conCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_codeNumList = v_codeNumList || (CASE v_codeNumList WHEN '' THEN '' ELSE ',' END) || c_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_codeNumList;");
//  Print #fileNo, addTab(1); "RETURN (CASE WHEN v_codeNumList = '' THEN NULL ELSE v_codeNumList END);"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (!((thisOrgIndex == M01_Globals.g_primaryOrgIndex) &  (thisPoolIndex == M01_Globals.g_workDataPoolIndex))) {
final int numSrxCodes = 10;

qualTabNameGenericAspectFactory;
qualTabNameGenericAspectFactory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null, null, null, null);
qualTabNameGenericAspectOrg;
qualTabNameGenericAspectOrg = qualTabNameGenericAspect;

qualFuncName = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, srxTypeStr + "IsAvail", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function determining whether a factory " + srxTypeStr + "-Context is subsumed by an MPC" + srxTypeStr + "-Context", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "factoryOid_in", M01_Globals.g_dbtOid, true, "OID of a factory 'Aspect'-object");
M11_LRT.genProcParm(fileNo, "", "mpcOid_in", M01_Globals.g_dbtOid, false, "OID of an MPC 'Aspect'-object");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_isAvailable", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);
int i;
for (int i = 1; i <= numSrxCodes; i++) {
M11_LRT.genVarDecl(fileNo, "v_facOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2), M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_mpcOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2), M01_Globals.g_dbtOid, "NULL", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "determine " + srxTypeStr + "Context-OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_facOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + (i < numSrxCodes ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + colPrefix + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + "_OID" + (i < numSrxCodes ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectFactory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = factoryOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_mpcOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + (i < numSrxCodes ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + colPrefix + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + "_OID" + (i < numSrxCodes ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = mpcOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "check each CODE contributing to the " + srxTypeStr + "Context", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR codeLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Fac");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(v_facOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + ")");
if (i < numSrxCodes) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Org");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
for (int i = 1; i <= numSrxCodes; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(v_mpcOid" + new String ("0" + i).substring(new String ("0" + i).length() - 1 - 2) + ")");
if (i < numSrxCodes) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Fac");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_Org");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_isAvailable = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_isAvailable;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (srxType == M01_Common.SrxTypeId.estSr0) {
qualFuncName = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, "IsValidForSr0", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Function determining whether a given Aspect is valid for a given SR0-Context", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "aspectOid_in", M01_Globals.g_dbtOid, true, "OID of the 'Aspect'-object to be checked");
M11_LRT.genProcParm(fileNo, "", "sr0Oid_in", M01_Globals.g_dbtOid, false, "OID of an 'SR0-Validity'-object");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_isValid", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);

M11_LRT.genProcSectionHeader(fileNo, "add each " + M01_Globals_IVK.g_anCodeNumber + " contributing to the SR0Context", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Asp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "01_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "02_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "03_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "04_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "05_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "06_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "07_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "08_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "09_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "10_OID FROM " + qualTabNameGenericAspect + " WHERE OID = aspectOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_SR0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "01_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "02_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "03_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "04_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "05_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "06_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "07_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "08_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "09_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT " + colPrefix + "10_OID FROM " + qualTabNameGenericAspect + " WHERE OID = sr0Oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Asp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_SR0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_isValid = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_isValid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}
// ### ENDIF IVK ###


}