package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_DataCompare {


// ### IF IVK ###


private static final int colEntryFilter = 1;
private static final int colCheckName = 2;
private static final int colSection = colCheckName + 1;
private static final int colEntityName = colSection + 1;
private static final int colEntityType = colEntityName + 1;
private static final int colDataPoolId = colEntityType + 1;
private static final int colRefDataPoolId = colDataPoolId + 1;
private static final int colAttrName = colRefDataPoolId + 1;
private static final int colCompareMode = colAttrName + 1;
private static final int colSequenceNumber = colCompareMode + 1;

private static final int processingStep = 2;

private static final int firstRow = 3;

private static final String sheetName = "DComp";

private static final String cmpModeLeftNotRight = "<--";
private static final String cmpModeRightNotLeft = "-->";
private static final String cmpModeDiffer = "<->";
private static final String cmpModeDupLeft = "<##";
private static final String cmpModeDupRight = "##>";

public static M79_DataCompare_Utilities.DCompDescriptors g_dComps;


private static void readSheet() {
M79_DataCompare_Utilities.initDCompDescriptors(M79_DataCompare.g_dComps);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].checkName = M00_Excel.getCell(thisSheet, thisRow, colCheckName).getStringCellValue().trim();
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].entityName = M00_Excel.getCell(thisSheet, thisRow, colEntityName).getStringCellValue().trim();
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].cType = M24_Attribute_Utilities.getAttrContainerType(M00_Excel.getCell(thisSheet, thisRow, colEntityType).getStringCellValue().trim());
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].dataPoolId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colDataPoolId).getStringCellValue(), -1);
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].refDataPoolId = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colRefDataPoolId).getStringCellValue(), -1);
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].attrName = M00_Excel.getCell(thisSheet, thisRow, colAttrName).getStringCellValue().trim();
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].compareMode = M79_DataCompare_Utilities.getDataCompareMode(M00_Excel.getCell(thisSheet, thisRow, colCompareMode).getStringCellValue());
M79_DataCompare.g_dComps.descriptors[M79_DataCompare_Utilities.allocDCompDescriptorIndex(M79_DataCompare.g_dComps)].sequenceNo = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNumber).getStringCellValue(), null);

NextRow:
thisRow = thisRow + 1;
}
}


public static void getDComps() {
if ((M79_DataCompare.g_dComps.numDescriptors == 0)) {
readSheet();
}
}


public static void resetDComps() {
M79_DataCompare.g_dComps.numDescriptors = 0;
}


public static void genDCompSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genDCompSupportDdlByType(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
genDCompSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genDCompSupportDdlByType(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.supportSstCheck)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataCheck, processingStep, ddlType, null, null, null, M01_Common.phaseDataCompare, M01_Common.ldmIterationGlobal);

// ####################################################################################################################
// #    SP comparing data in tables / views
// ####################################################################################################################

int numKeyColumns;
numKeyColumns = 25;
int maxKeyValLength;
maxKeyValLength = 50;
String keyColPrefix;
keyColPrefix = "keyCol";
String keyValPrefix;
keyValPrefix = "keyVal";

String qualProcNameDataCompare;
qualProcNameDataCompare = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkCompare, ddlType, null, null, null, null, null, null);

String tempTabNameStmntCompare;
tempTabNameStmntCompare = M94_DBAdmin.tempTabNameStatement + "Compare";

M22_Class_Utilities.printSectionHeader("SP comparing data in tables / views", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataCompare);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the (set of) compare-checks to execute");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names that apply");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables / views that apply");
M11_LRT.genProcParm(fileNo, "OUT", "compareCount_out", "INTEGER", true, "number of comparisons executed");
M11_LRT.genProcParm(fileNo, "OUT", "diffCount_out", "INTEGER", false, "number of 'differences' identified");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_firstCol", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_compareMode", "CHAR(3)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colNo", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_selColList1", "VARCHAR(2500)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_selColList2", "VARCHAR(2500)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_grpColList1", "VARCHAR(2500)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_grpColList2", "VARCHAR(2500)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tgtColList", "VARCHAR(2500)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_joinCond", "VARCHAR(8000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_compareCond", "VARCHAR(8000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 16000, true, null, null, null, "Compare", null, null, null, null, null, null, null, null);

String dbTypeColname;
dbTypeColname = M25_Domain.getDbDataTypeByDomainName(M01_ACM.dxnPdmColumnName, M01_ACM.dnPdmColumnName);
String qualTabNameTempCompareResults;
qualTabNameTempCompareResults = "SESSION.CompareResults";
M11_LRT.genProcSectionHeader(fileNo, "temporary table for comparison result", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTempCompareResults);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "schemaName    " + M25_Domain.getDbDataTypeByDomainName(M01_ACM.dxnDbSchemaName, M01_ACM.dnDbSchemaName) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refSchemaName " + M25_Domain.getDbDataTypeByDomainName(M01_ACM.dxnDbSchemaName, M01_ACM.dnDbSchemaName) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objName       " + M25_Domain.getDbDataTypeByDomainName(M01_ACM.dxnPdmTableName, M01_ACM.dnPdmTableName) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "mode          CHAR(3),");
int k;
for (int k = 1; k <= numKeyColumns; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + keyColPrefix + M04_Utilities.paddRight(String.valueOf(k), 8, null) + dbTypeColname + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + keyValPrefix + M04_Utilities.paddRight(String.valueOf(k), 8, null) + "VARCHAR(" + String.valueOf(maxKeyValLength) + ")" + (k < numKeyColumns ? "," : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, true, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataCompare, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "compareCount_out", "diffCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET compareCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET diffCount_out    = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables / views to compare", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100)) AS c_cmpSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100)) AS c_refSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") AS c_entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " AS c_entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAccessModeId + " AS c_dataPoolid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.REFACCESSMODE_ID AS c_refDataPoolid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine list of key-columns", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colNo       = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_firstCol    = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_selColList1 = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_selColList2 = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_grpColList1 = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_grpColList2 = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tgtColList  = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_joinCond    = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmAttributeName + " AS c_attributeName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN COL.TYPENAME IN ('VARCHAR', 'CHARACTER') THEN 1 ELSE 0 END) AS c_isChar,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN COL.CODEPAGE = 0                         THEN 1 ELSE 0 END) AS c_isBinary");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameDataComparisonAttribute + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS COL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(COL.COLNAME) = UPPER(A." + M01_Globals.g_anAcmAttributeName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(COL.TABNAME) = UPPER(A." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(COL.TABSCHEMA) = c_cmpSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(A." + M01_Globals.g_anAcmEntitySection + ") = c_entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(A." + M01_Globals.g_anAcmEntityName + ") = c_entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = c_entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.COMPAREMODE = 'K'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(COL.TYPENAME, '') NOT IN ('BLOB', 'CLOB')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_firstCol = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_firstCol = UPPER(c_attributeName);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colNo = v_colNo + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_isChar = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF c_isBinary = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(HEX(T1.' || UPPER(c_attributeName) || ')," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(HEX(T2.' || UPPER(c_attributeName) || ')," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(T1.' || UPPER(c_attributeName) || '," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(T2.' || UPPER(c_attributeName) || '," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_selColList1 = v_selColList1 || ','''  || UPPER(c_attributeName) || ''',LEFT(RTRIM(CHAR(T1.' || UPPER(c_attributeName) || '))," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_selColList2 = v_selColList2 || ','''  || UPPER(c_attributeName) || ''',LEFT(RTRIM(CHAR(T2.' || UPPER(c_attributeName) || '))," + String.valueOf(maxKeyValLength) + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_grpColList1 = v_grpColList1 || (CASE v_grpColList1 WHEN '' THEN '' ELSE ',' END) || 'T1.' || UPPER(c_attributeName);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_grpColList2 = v_grpColList2 || (CASE v_grpColList2 WHEN '' THEN '' ELSE ',' END) || 'T2.' || UPPER(c_attributeName);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_joinCond    = v_joinCond    || (CASE v_joinCond    WHEN '' THEN '' ELSE ' AND ' END) || '(T1.' || UPPER(c_attributeName) || '=T2.'   || UPPER(c_attributeName) || ')';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_colNo <= " + String.valueOf(numKeyColumns) + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_tgtColList = v_tgtColList || '," + keyColPrefix + "' || RTRIM(CHAR(v_colNo)) || '," + keyValPrefix + "' || RTRIM(CHAR(v_colNo));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "determine list of compare-columns", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_compareCond = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmAttributeName + " AS c_attributeName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN COL.TYPENAME IN ('BLOB', 'CLOB') THEN 1 ELSE 0 END) AS c_isLob");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameDataComparisonAttribute + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS COL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COL.COLNAME = UPPER(A." + M01_Globals.g_anAcmAttributeName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COL.TABNAME = UPPER(A." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COL.TABSCHEMA = c_cmpSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(A." + M01_Globals.g_anAcmEntitySection + ") = c_entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(A." + M01_Globals.g_anAcmEntityName + ") = c_entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = c_entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.COMPAREMODE = 'C'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.SEQUENCENO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_isLob = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_compareCond = v_compareCond || (CASE v_compareCond WHEN '' THEN '' ELSE ' OR ' END) || '(T1.' || UPPER(c_attributeName) || '<>T2.'   || UPPER(c_attributeName) || ')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "loop over compare-modes and determine compare-statements", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_compareMode = '" + cmpModeLeftNotRight + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_compareMode IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "identify records in tables depending on the resp. compare mode", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_compareMode IN ('" + cmpModeLeftNotRight + "') THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "c_cmpSchemaName || '.' || c_entityName || ' T1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' ' || (CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'LEFT OUTER' WHEN '" + cmpModeRightNotLeft + "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "c_refSchemaName || '.' || c_entityName || ' T2' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_joinCond ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'T2.' || v_firstCol || ' IS NULL' ELSE 'T1.' || v_firstCol || ' IS NULL' END);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF v_compareMode IN ('" + cmpModeRightNotLeft + "') THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList2 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "c_cmpSchemaName || '.' || c_entityName || ' T1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' ' || (CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'LEFT OUTER' WHEN '" + cmpModeRightNotLeft + "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "c_refSchemaName || '.' || c_entityName || ' T2' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_joinCond ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'T2.' || v_firstCol || ' IS NULL' ELSE 'T1.' || v_firstCol || ' IS NULL' END);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF v_compareMode IN ('" + cmpModeDiffer + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF v_compareCond = '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntTxt = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "' FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "c_cmpSchemaName || '.' || c_entityName || ' T1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "' ' || (CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'LEFT OUTER' WHEN '" + cmpModeRightNotLeft + "' THEN 'RIGHT OUTER' ELSE 'INNER' END) || ' JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "c_refSchemaName || '.' || c_entityName || ' T2' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "' ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "v_joinCond ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "' WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN 'T2.' || v_firstCol || ' IS NULL' WHEN '" + cmpModeRightNotLeft + "' THEN 'T1.' || v_firstCol || ' IS NULL' ELSE v_compareCond END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF v_compareMode IN ('" + cmpModeDupLeft + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList1 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_compareMode = '" + cmpModeDupLeft + "' THEN c_cmpSchemaName ELSE c_refSchemaName END) || '.' || c_entityName || ' T1' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' GROUP BY ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_grpColList1 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' HAVING ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'COUNT(*) > 1';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF v_compareMode IN ('" + cmpModeDupRight + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'''' || c_cmpSchemaName || ''',''' || c_refSchemaName || ''',''' || c_entityName || ''',''' || v_compareMode || '''' || v_selColList2 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN v_compareMode = '" + cmpModeDupLeft + "' THEN c_cmpSchemaName ELSE c_refSchemaName END) || '.' || c_entityName || ' T2' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' GROUP BY ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_grpColList2 ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "' HAVING ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'COUNT(*) > 1';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_stmntTxt IS NOT NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + tempTabNameStmntCompare);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "retrieve comparison result", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'" + qualTabNameTempCompareResults + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'schemaName,refSchemaName,objName,mode' || v_tgtColList ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "') ' || v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "count number of differences found", 5, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET diffCount_out = diffCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "keep track of number of comparisons executed", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET compareCount_out = compareCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "select next compare-mode", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_compareMode = (CASE v_compareMode WHEN '" + cmpModeLeftNotRight + "' THEN '" + cmpModeRightNotLeft + "' " + "WHEN '" + cmpModeRightNotLeft + "' THEN '" + cmpModeDiffer + "' " + "WHEN '" + cmpModeDiffer + "' THEN '" + cmpModeDupLeft + "' " + "WHEN '" + cmpModeDupLeft + "' THEN '" + cmpModeDupRight + "' " + "ELSE NULL END);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntCompare);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameTempCompareResults);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "schemaName ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "refSchemaName ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "objName ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN resCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataCompare, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "compareCount_out", "diffCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP comparing data in tables / views", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataCompare);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "compareCount_out", "INTEGER", true, "number of comparisons executed");
M11_LRT.genProcParm(fileNo, "OUT", "diffCount_out", "INTEGER", false, "number of 'differences' identified");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataCompare, ddlType, null, "mode_in", "compareCount_out", "diffCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameDataCompare + "(mode_in, NULL, NULL, NULL, compareCount_out, diffCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataCompare, ddlType, null, "mode_in", "compareCount_out", "diffCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP cleaning up data in tables
// ####################################################################################################################

String qualProcNameDataCleanup;
qualProcNameDataCleanup = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkCleanup, ddlType, null, null, null, null, null, null);

String tempTabNameStmntCleanup;
tempTabNameStmntCleanup = M94_DBAdmin.tempTabNameStatement + "Cleanup";

M22_Class_Utilities.printSectionHeader("SP cleaning up data in tables", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataCleanup);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the set of tables to cleanup");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names to cleanup");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables to cleanup");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "determines the data pool holding the tables to cleanup");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of tables cleaned up");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows deleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(120)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 120, true, true, true, null, "Cleanup", null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataCleanup, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "accessModeId_in", "tabCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to cleanup", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C.REFACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine DELETE-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'DELETE FROM ' || c_tabSchema || '.' || c_tabName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntCleanup);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute DELETE-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntCleanup);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataCleanup, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "accessModeId_in", "tabCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP copying data to reference tables
// ####################################################################################################################

String qualProcNameDataCp2RefTab;
qualProcNameDataCp2RefTab = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkCp2RefTab, ddlType, null, null, null, null, null, null);

String tempTabNameStmntCp2RefTab;
tempTabNameStmntCp2RefTab = M94_DBAdmin.tempTabNameStatement + "Cp2Ref";

M22_Class_Utilities.printSectionHeader("SP copying data to reference tables", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataCp2RefTab);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the set of tables to copy");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names to copy");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables to copy");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of tables copied");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows copied");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "Cp2Ref", null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataCp2RefTab, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to copy", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100)) AS c_cmpSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100)) AS c_refSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine COPY-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'INSERT INTO ' || c_refSchemaName || '.' || c_tabName || ' SELECT * FROM ' || c_cmpSchemaName || '.' || c_tabName;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntCp2RefTab);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute DELETE-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntCp2RefTab);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataCp2RefTab, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP exporting data
// ####################################################################################################################

String qualProcNameDataExport;
qualProcNameDataExport = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkExport, ddlType, null, null, null, null, null, null);

String tempTabNameStmntExport;
tempTabNameStmntExport = M94_DBAdmin.tempTabNameStatement + "Export";

M22_Class_Utilities.printSectionHeader("SP exporting data", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataExport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the set of tables to export");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) determines the data pool holding the tables to export");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names to export");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables to export");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", false, "number of tables exported");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "Export", null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataExport, ddlType, null, "mode_in", "'checkNamePattern_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "Verify that this DB-Version supports Export", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_db2Release < 9 and mode_in >= 1 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDataExport, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_db2Release))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to export", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C.REFACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine EXPORT-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'EXPORT TO ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF LOBFILE ' ||" + "c_tabSchema || '.' || c_tabName || ' MODIFIED BY LOBSINFILE SELECT * FROM ' || c_tabSchema || '.' || c_tabName;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntExport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute EXPORT-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntExport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataExport, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP importing data
// ####################################################################################################################

String qualProcNameDataImport;
qualProcNameDataImport = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkImport, ddlType, null, null, null, null, null, null);

String tempTabNameStmntImport;
tempTabNameStmntImport = M94_DBAdmin.tempTabNameStatement + "Import";

M22_Class_Utilities.printSectionHeader("SP importing data", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataImport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the set of tables to import");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) determines the data pool holding the tables to import");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names to import");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables to import");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", false, "number of tables imported");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "Import", null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataImport, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "Verify that this DB-Version supports IMPORT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_db2Release < 9 and mode_in >= 1 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDataImport, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_db2Release))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to import", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C.REFACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine IMPORT-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'IMPORT FROM ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF COMMITCOUNT 10000 INSERT INTO ' ||" + "c_tabSchema || '.' || c_tabName;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntImport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute IMPORT-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntImport);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataImport, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP loading data
// ####################################################################################################################

String qualProcNameDataLoad;
qualProcNameDataLoad = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.spnDataChkLoad, ddlType, null, null, null, null, null, null);

String tempTabNameStmntLoad;
tempTabNameStmntLoad = M94_DBAdmin.tempTabNameStatement + "Load";

M22_Class_Utilities.printSectionHeader("SP loading data", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDataLoad);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "INTEGER", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "IN", "checkNamePattern_in", "VARCHAR(40)", true, "(optional) identifies the set of tables to load");
M11_LRT.genProcParm(fileNo, "IN", "accessModeId_in", M01_Globals.g_dbtEnumId, true, "(optional) determines the data pool holding the tables to import");
M11_LRT.genProcParm(fileNo, "IN", "schemaNamePattern_in", M01_Globals.g_dbtDbSchemaName, true, "(optional) determines the logical schema names to load");
M11_LRT.genProcParm(fileNo, "IN", "entityNamePattern_in", "VARCHAR(80)", true, "(optional) determines the tables to load");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", false, "number of tables loaded");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_db2Release", M01_Globals.g_dbtDbRelease, "NULL", null, null);

// FIXME: use as parameter ?
M11_LRT.genVarDecl(fileNo, "v_cmpOrgId", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_refOrgId", "INTEGER", "1", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 200, true, true, true, null, "Load", null, null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDataLoad, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "Verify that this DB-Version supports LOAD", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_db2Release = " + M01_Globals.g_qualFuncNameDb2Release + "();");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_db2Release < 9 and mode_in >= 1 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDataLoad, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("dbVersNotSupported", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_db2Release))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to load", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR objLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_cmpOrgId),2)|| RIGHT(DIGITS(C." + M01_Globals.g_anAccessModeId + "   ),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE CAST('" + M04_Utilities.genSchemaName("", "", ddlType, null, null) + "' || S.SECTIONSHORTNAME || RIGHT(DIGITS(v_refOrgId),2)|| RIGHT(DIGITS(C.REFACCESSMODE_ID),1) AS VARCHAR(100))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntityName + ") AS c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameDataComparison + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmSection + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(C." + M01_Globals.g_anAcmEntitySection + ") = UPPER(S.SECTIONNAME)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C.CHECKNAME)) LIKE COALESCE(UPPER(checkNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(S.SECTIONNAME)) LIKE COALESCE(UPPER(schemaNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(UPPER(C." + M01_Globals.g_anAcmEntityName + ")) LIKE COALESCE(UPPER(entityNamePattern_in), '%')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C." + M01_Globals.g_anAccessModeId + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId_in = C.REFACCESSMODE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine LOAD-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'LOAD FROM ' || c_tabSchema || '.' || c_tabName || '.ixf OF IXF INSERT INTO ' ||" + "c_tabSchema || '.' || c_tabName;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntLoad);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute LOAD-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine SET-INTEGRITY-statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt   = 'SET INTEGRITY FOR ' || c_tabSchema || '.' || c_tabName || ' IMMEDIATE CHECKED';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tempTabNameStmntLoad);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmntTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "execute EXPORT-statement", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "STATEMENT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStmntLoad);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SEQNO ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDataLoad, ddlType, null, "mode_in", "'checkNamePattern_in", "accessModeId_in", "'schemaNamePattern_in", "'entityNamePattern_in", "tabCount_out", null, null, null, null, null, null);

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


private static void genDCompSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// only supported at 'pool-level'
return;
}
}

public static void evalDComps() {
int i;
int j;

M21_Enum_Utilities.EnumDescriptor enumDescr;
M26_Type_Utilities.TypeDescriptor typeDescr;

for (i = 1; i <= 1; i += (1)) {
// determine references to attributes
M79_DataCompare.g_dComps.descriptors[i].attrRef = -1;
if (M79_DataCompare.g_dComps.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M79_DataCompare.g_dComps.descriptors[i].attrRef = M22_Class.getAttributeIndexByClassNameAndName(M79_DataCompare.g_dComps.descriptors[i].sectionName, M79_DataCompare.g_dComps.descriptors[i].entityName, M79_DataCompare.g_dComps.descriptors[i].attrName, true);
if (M79_DataCompare.g_dComps.descriptors[i].attrRef < 0) {
M04_Utilities.logMsg("unknown attribute \"" + M79_DataCompare.g_dComps.descriptors[i].attrName + "\" used in specification of DataComparison for \"" + M79_DataCompare.g_dComps.descriptors[i].sectionName + "." + M79_DataCompare.g_dComps.descriptors[i].entityName + "\"", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}
} else if (M79_DataCompare.g_dComps.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
// fixme - implement this (if we need this)
} else if (M79_DataCompare.g_dComps.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
// fixme - implement this (if we need this)
}
}
}


public static void genDCompCsv(Integer ddlType) {
String fileName;
int fileNo;

if (!(M03_Config.supportSstCheck |  ddlType != M01_Common.DdlTypeId.edtPdm)) {
return;
}

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.clnDataComparison, processingStep, "DataCheck", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

String lastCheckName;
String lastSectionName;
String lastEntityName;
Integer lastCType;

lastCheckName = "";
lastSectionName = "";
lastEntityName = "";
lastCType = M24_Attribute_Utilities.AcmAttrContainerType.eactType;
int i;
for (int i = 1; i <= M79_DataCompare.g_dComps.numDescriptors; i++) {
if ((!(lastCheckName.compareTo(M79_DataCompare.g_dComps.descriptors[i].checkName) == 0)) |  (!(lastSectionName.compareTo(M79_DataCompare.g_dComps.descriptors[i].sectionName) == 0)) | (!(lastEntityName.compareTo(M79_DataCompare.g_dComps.descriptors[i].entityName) == 0)) | (!(lastCType.compareTo(M79_DataCompare.g_dComps.descriptors[i].cType) == 0))) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_DataCompare.g_dComps.descriptors[i].checkName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_DataCompare.g_dComps.descriptors[i].sectionName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_DataCompare.g_dComps.descriptors[i].entityName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M79_DataCompare.g_dComps.descriptors[i].cType) + "\",");
M00_FileWriter.printToFile(fileNo, (M79_DataCompare.g_dComps.descriptors[i].dataPoolId >= 0 ? String.valueOf(M79_DataCompare.g_dComps.descriptors[i].dataPoolId) : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_DataCompare.g_dComps.descriptors[i].refDataPoolId >= 0 ? String.valueOf(M79_DataCompare.g_dComps.descriptors[i].refDataPoolId) : ""));
lastCheckName = M79_DataCompare.g_dComps.descriptors[i].checkName;
lastSectionName = M79_DataCompare.g_dComps.descriptors[i].sectionName;
lastEntityName = M79_DataCompare.g_dComps.descriptors[i].entityName;
lastCType = M79_DataCompare.g_dComps.descriptors[i].cType;
}
}

M00_FileWriter.closeFile(fileNo);

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.clnDataComparisonAttribute, processingStep, "DataCheck", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
for (int i = 1; i <= M79_DataCompare.g_dComps.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, "\"" + M79_DataCompare.g_dComps.descriptors[i].sectionName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M79_DataCompare.g_dComps.descriptors[i].entityName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M79_DataCompare.g_dComps.descriptors[i].cType) + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.genAttrName(M79_DataCompare.g_dComps.descriptors[i].attrName, ddlType, null, null, null, null, null, null) + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + (M79_DataCompare.g_dComps.descriptors[i].compareMode == M79_DataCompare_Utilities.DataCompareMode.dcmKey ? "K" : (M79_DataCompare.g_dComps.descriptors[i].compareMode == M79_DataCompare_Utilities.DataCompareMode.dcmCompare ? "C" : "N")) + "\",");
M00_FileWriter.printToFile(fileNo, (M79_DataCompare.g_dComps.descriptors[i].sequenceNo >= 0 ? String.valueOf(M79_DataCompare.g_dComps.descriptors[i].sequenceNo) : "0"));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropDCompCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.clnDataComparison, M01_Globals.g_targetDir, processingStep, null, "DataCheck");
M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDataCheck, M01_ACM_IVK.clnDataComparisonAttribute, M01_Globals.g_targetDir, processingStep, null, "DataCheck");
}
// ### ENDIF IVK ###

}