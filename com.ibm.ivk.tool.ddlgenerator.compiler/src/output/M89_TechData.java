package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M89_TechData {


// ### IF IVK ###


private static final int processingStep = 5;


public static void genTechDataSupDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
gengenTechDataSupByPool(M01_Common.DdlTypeId.edtLdm, null, null);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M72_DataPool.g_pools.descriptors[thisPoolIndex].supportAcm & !M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive) {
gengenTechDataSupByPool(M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}


private static void gengenTechDataSupByPool(Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// only supported at 'pool-level'
return;
}

if (thisPoolIndex > 0 &  thisPoolIndex != M01_Globals.g_workDataPoolIndex) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexStaging, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualFuncName;
String qualProcName;

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTechDataDeltaImport;
qualTabNameTechDataDeltaImport = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTechDataDeltaImport, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameEndSlotGen;
qualTabNameEndSlotGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNameEndSlotGenNl;
qualTabNameEndSlotGenNl = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNameAggregationSlotGen;
qualTabNameAggregationSlotGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNameAggregationSlotGenNl;
qualTabNameAggregationSlotGenNl = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNamePropertyGen;
qualTabNamePropertyGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabNamePropertyGenNl;
qualTabNamePropertyGenNl = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String sr0ValidityClassIdStr;
sr0ValidityClassIdStr = M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexSr0Validity);

String qualFuncNameSparte2DivOid;
qualFuncNameSparte2DivOid = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.udfnSparte2DivOid, ddlType, null, null, null, null, null, true);

// ####################################################################################################################
// #    Procedure retrieving the BM attribute with undefined baumuster for TECHDATADELTAIMPORT
// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "UDBM4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("Procedure retrieving the BM attribute with undefined baumuster for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.DeletableTDDI");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sparte CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "baumuster VARCHAR(8)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tdLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TD.SPARTE AS v_sparte,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TD.BAUMUSTER AS v_baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TD.FILETIMESTAMP = v_timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.DeletableTDDI");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sparte,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_sparte,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspect + " GA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GA." + M01_Globals.g_anCid + " = '09003'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + " M WHERE M.DPSPARTE = v_sparte AND M.PSOID = GA." + M01_Globals_IVK.g_anPsOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GA.BAUMUSTER LIKE v_baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "HAVING COUNT(*) = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- return result to application");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DECLARE logCursor CURSOR WITH RETURN FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sparte,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.DeletableTDDI");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sparte,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "baumuster");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- leave cursor open for application");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN logCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF retrieving the BEI attribute with undefined code for TECHDATADELTAIMPORT
// ####################################################################################################################
qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAliasLrt, "UDBEI4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);
M22_Class_Utilities.printSectionHeader("Function retrieving the BEI attribute with undefined code for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE  CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI     VARCHAR(752),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CODE    VARCHAR(22)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI_ELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEVEL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "REST,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI_TEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(1 AS INTEGER),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'#',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(RTRIM(TD.BEI), '+-', '|'), '+', '|'), '/', '|'), ',', '|'), '!', '|'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.BEI");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.BEI IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.FILETIMESTAMP = v_timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LEVEL + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE LOCATE('|', REST)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN 0 THEN REST");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "         CASE LOCATE('|', REST, 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "           WHEN 0 THEN LTRIM(RTRIM(SUBSTR(REST, 2)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                  ELSE LTRIM(RTRIM(SUBSTR(REST, 2, LOCATE('|', REST, 2)-2)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "         END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "       ELSE LTRIM(RTRIM(SUBSTR(REST, 1, LOCATE('|', REST)-1)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE LOCATE('|', REST)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN 0 THEN ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHEN 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "         CASE LOCATE('|', REST, 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "           WHEN 0 THEN LTRIM(RTRIM(SUBSTR(REST, 2)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "                  ELSE LTRIM(RTRIM(SUBSTR(REST, LOCATE('|', REST, 2)+1, LENGTH(REST))))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "         END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "       ELSE LTRIM(RTRIM(SUBSTR(REST, LOCATE('|', REST)+1, LENGTH(REST))))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "BEI_TEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "BEI_ELEMENTS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LENGTH(REST) > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LEVEL < 100000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI_ELEMENTS_DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEI_TEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE WHEN LOCATE('-', CODE) > 0 THEN SUBSTR(CODE, LOCATE('-', CODE)+1) ELSE CODE END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEI_ELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CODE <> '#'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEI_ELEMENTS_DISTINCT B");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CDIDIV_OID = " + qualFuncNameSparte2DivOid + "(B.SPARTE)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anCodeNumber + " = B.CODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function retrieving the PROPERTYNAME attribute with undefined property for TECHDATADELTAIMPORT
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAliasLrt, "UDP4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);
M22_Class_Utilities.printSectionHeader("Function retrieving the PROPERTYNAME attribute with undefined property for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE       CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROPERTYNAME VARCHAR(256)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROPERTY_ELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROPERTY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT M.DPSPARTE FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + " M WHERE M.PSOID = PR." + M01_Globals_IVK.g_anPsOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePropertyGenNl + " PRNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePropertyGen + " PR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PR." + M01_Globals.g_anOid + " = PRNL.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRNL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.PROPERTYNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROPERTY_ELEMENTS PE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.SPARTE = PE.SPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TD.PROPERTYNAME = PE.PROPERTY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF retrieving the CODE attribute with undefined code for TECHDATADELTAIMPORT
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAliasLrt, "UDC4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);
M22_Class_Utilities.printSectionHeader("Function retrieving the CODE attribute with undefined code for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CODE   " + M01_Globals_IVK.g_dbtCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.CODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTechDataDeltaImport + " TD");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CDIDIV_OID = " + qualFuncNameSparte2DivOid + "(TD.SPARTE)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anCodeNumber + " = TD.CODE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.CODE IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = v_timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF retrieving the SLOTNAME attribute with undefined endslots for TECHDATADELTAIMPORT
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAliasLrt, "UDES4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);
M22_Class_Utilities.printSectionHeader("Function retrieving the SLOTNAME attribute with undefined endslots for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE   CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOTNAME VARCHAR(256)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT_ELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT M.DPSPARTE FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + " M WHERE M.PSOID = ESL." + M01_Globals_IVK.g_anPsOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESLNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameEndSlotGenNl + " ESLNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameEndSlotGen + " ESL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESL." + M01_Globals.g_anOid + " = ESLNL.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESLNL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SLOTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (SELECT 1 FROM SLOT_ELEMENTS SE WHERE TD.SPARTE = SE.SPARTE AND TD.SLOTNAME = SE.SLOT)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.CODE IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = v_timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UDF retrieving the SLOTNAME attribute with undefined aggregationslots for TECHDATADELTAIMPORT
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAliasLrt, "UDAS4TDDI", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true);
M22_Class_Utilities.printSectionHeader("Function retrieving the SLOTNAME attribute with undefined aggregationslots for TECHDATADELTAIMPORT", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE   CHAR(1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOTNAME VARCHAR(256)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT_ELEMENTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT M.DPSPARTE FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + " M WHERE M.PSOID = ASL." + M01_Globals_IVK.g_anPsOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ASLNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameAggregationSlotGenNl + " ASLNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameAggregationSlotGen + " ASL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ASL." + M01_Globals.g_anOid + " = ASLNL.ASL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ASLNL." + M01_Globals.g_anLanguageId + " = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SLOTNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (SELECT 1 FROM SLOT_ELEMENTS SE WHERE TD.SPARTE = SE.SPARTE AND TD.SLOTNAME = SE.SLOT)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.CODE IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = v_timestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Procedure for 'rolling back' a TechDataDelta-Import
// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "TDDIROLLBACK", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for 'rolling back' a TechDataDelta-Import", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "timestamp_in", "TIMESTAMP", true, null);
M11_LRT.genProcParm(fileNo, "IN", "sparte_in", "VARCHAR(1)", false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "delete records related to the specified timestamp and sparte", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = timestamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE = sparte_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.PROPERTYVALUEOLD IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.VALUEGATHERINGOLD IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "rollback changes of PROPERTYVALUE and VALUEGATHERIN related to the specified timestamp and sparte", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.PROPERTYVALUE = TD.PROPERTYVALUEOLD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.VALUEGATHERING = TD.VALUEGATHERINGOLD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD." + M01_Globals.g_anLastUpdateTimestamp + " = CURRENT TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = timestamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE = sparte_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Procedure for 'rolling back' individual rows for TechDataDelta-Import
// ####################################################################################################################
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "TDDIDELETEROW", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

boolean genCountryParams;
int i;
for (int i = 1; i <= 2; i++) {
genCountryParams = (i == 2);
M22_Class_Utilities.printSectionHeader("SP for 'rolling back' individual rows for TechDataDelta-Import", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "timestamp_in", "TIMESTAMP", true, null);
M11_LRT.genProcParm(fileNo, "IN", "sparte_in", "VARCHAR(1)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "baumuster_in", "VARCHAR(8)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "bei_in", "VARCHAR(752)", true, null);
if (genCountryParams) {
M11_LRT.genProcParm(fileNo, "IN", "land_in", "VARCHAR(3)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "ausserland_in", "VARCHAR(600)", true, null);
}
M11_LRT.genProcParm(fileNo, "IN", "gueltig_ab_in", "DATE", true, null);
M11_LRT.genProcParm(fileNo, "IN", "gueltig_bis_in", "DATE", true, null);
M11_LRT.genProcParm(fileNo, "IN", "property_in", "VARCHAR(256)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "slot_in", "VARCHAR(256)", true, null);
M11_LRT.genProcParm(fileNo, "IN", "code_in", M01_Globals_IVK.g_dbtCodeNumber, false, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "delete records related to the specified parameters", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTechDataDeltaImport + " TD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.FILETIMESTAMP = timestamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TD.SPARTE = sparte_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.BAUMUSTER IS NULL AND baumuster_in IS NULL) OR (TD.BAUMUSTER = baumuster_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.BEI IS NULL AND bei_in IS NULL) OR (TD.BEI = bei_in))");
if (genCountryParams) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.LAND IS NULL AND land_in IS NULL) OR (TD.LAND = land_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.AUSSERLAND IS NULL AND ausserland_in IS NULL) OR (TD.AUSSERLAND = ausserland_in))");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.GUELTIGAB IS NULL AND gueltig_ab_in IS NULL) OR (TD.GUELTIGAB = gueltig_ab_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.GUELTIGBIS IS NULL AND gueltig_bis_in IS NULL) OR (TD.GUELTIGBIS = gueltig_bis_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.PROPERTYNAME IS NULL AND property_in IS NULL) OR (TD.PROPERTYNAME = property_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((TD.SLOTNAME IS NULL AND slot_in IS NULL) OR (TD.SLOTNAME = slot_in))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(TD.CODE,'') = COALESCE(code_in, '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

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