package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M83_PSdelete {


// ### IF IVK ###


private static final int processingStep = 3;


public static void genPsDeleteSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M71_Org.g_orgs.descriptors[thisOrgIndex].isPrimary) {
genPsDeleteSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genPsDeleteSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// PS-delete is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameDocuNews;
String qualTabNameDocuNewsType;
String qualTabNameMdsInbox;
String qualTabNameJob;

qualTabNameDocuNews = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDocuNews, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameDocuNewsType = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDocuNewsType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameJob = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "PsDelete", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for 'Deleting ProductStructure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProuctStructure to delete");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being deleted (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_isUnderConstruction", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "verify that ProductStructure exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT EXISTS (SELECT " + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " WHERE " + M01_Globals.g_anOid + " = psOid_in) THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that ProductStructure is 'under construction'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_isUnderConstruction =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anIsUnderConstruction);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT (v_isUnderConstruction = 1) THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("psDelNotUndConstr", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "loop over all PS-tagged tables", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M83_PSdelete.genTabListView(fileNo, thisOrgIndex, thisPoolIndex, thisPoolIndex, ddlType, 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.schemaName AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.tabName AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.filter AS c_filter");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_TabList T,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " = T.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " = T.schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmIsPs + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(P." + M01_Globals.g_anOrganizationId + "," + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + ") = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
// todo: foreign keys on LRT-tables do not appear to be reflected in FKSEQUENCENO; thus we need extra ordering by ISLRT here
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " DESC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " DESC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "delete records in table tagged with this PS-OID", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'DELETE FROM ' || c_schemaName || '.' || c_tableName || ' WHERE ' || c_filter;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = REPLACE(v_stmntTxt,'<PS>',RTRIM(CHAR(psOid_in)));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = REPLACE(v_stmntTxt,'<REFSCHEMA>',c_schemaName);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "delete related DocuNews", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDocuNews + " N");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameDocuNewsType + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "N.DNATPE_OID = T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.DNPPST_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "delete related DocuNewsType", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDocuNewsType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DNPPST_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "delete related DataPools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U.LDPLDP_OID = NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameDataPool + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "U.LDPLDP_OID = P." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P.DPSPST_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameWriteLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "wdpdpo_oid IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dpspst_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameReleaseLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rdpdpo_oid IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "dpspst_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DPSPST_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "delete PS-related data in table \"" + M01_Globals_IVK.g_qualTabNameRegistryStatic + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anSection + " IN ('STANDARDXML', 'VDFXML')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anKey + " = 'DESTINATION'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anSubKey + " LIKE '%,' || RTRIM(CHAR(psOid_in)) || ',%'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "delete ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructureNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].shortName, null, null, null, null) + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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
// ### ENDIF IVK ###




private static void genTabListViewElement(int acmEntityIndex, Integer acmEntityType, boolean forGen, boolean forNl, int fileNo, String qualSrcTabName, String qualDstTabName, boolean firstCall, Integer indentW, Boolean useFilterW, String filterW, Boolean forPsDeleteW) {
int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

boolean useFilter; 
if (useFilterW == null) {
useFilter = true;
} else {
useFilter = useFilterW;
}

String filter; 
if (filterW == null) {
filter = "";
} else {
filter = filterW;
}

boolean forPsDelete; 
if (forPsDeleteW == null) {
forPsDelete = false;
} else {
forPsDelete = forPsDeleteW;
}


String[] listSrc;
String[] listDst;
listSrc = qualSrcTabName.split(".");
listDst = qualDstTabName.split(".");

String colList;
boolean columnsComparable;
colList = "";
columnsComparable = true;

if (!(forPsDelete)) {
M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean isGenericAspect;
isGenericAspect = false;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

if (!(forNl)) {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
isGenericAspect = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == M01_ACM_IVK.clnGenericAspect.toUpperCase());
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, null, null, M01_Common.DdlTypeId.edtPdm, null, null, 0, forGen, false, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, M01_Common.DdlTypeId.edtPdm, null, null, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
}

int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacOid) |  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacCid) | (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacPsOid) | (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacLrtMeta) | (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacFkOidParent)) &  ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular) == 0)) {
// ignore this column
} else {
if ((M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType == M01_Common.typeId.etBlob) |  (M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType == M01_Common.typeId.etClob)) {
columnsComparable = false;
}
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression)) {
// we do not instantiate these columns
} else if (isGenericAspect &  (tabColumns.descriptors[i].columnCategory == M01_Common.AttrCategory.eacFkOid) & (tabColumns.descriptors[i].columnName.length() == 10) & ((tabColumns.descriptors[i].columnName.substring(0, 4) == "S0CS") |  (tabColumns.descriptors[i].columnName.substring(0, 4) == "N1CN") | (tabColumns.descriptors[i].columnName.substring(0, 4) == "S1CT")) & (tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 4) == "_OID")) {
// special treatment of GenericAspect: ignore all FK-columns corresponding to SRX-Context
// this is covered by the string attribute already
// otherwise column list is tooooo long
// -> ignore this column
} else {
colList = colList + (colList.compareTo("") == 0 ? "" : ",") + tabColumns.descriptors[i].columnName;
}
}
}
}

if (!(firstCall)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "UNION");
}
if (listSrc[0] == listDst[0]) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "VALUES('" + listSrc[0] + "','" + listSrc[1] + "'," + (useFilter &  (!(filter.compareTo("") == 0)) ? "'" + filter + "'" : "CAST(NULL AS VARCHAR(200))") + (forPsDelete ? "" : (columnsComparable ? ",1" : ",0")) + (forPsDelete ? "" : ",'" + colList + "'") + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "VALUES('" + listSrc[0] + "','" + listDst[0] + "','" + listSrc[1] + "'," + (useFilter &  (!(filter.compareTo("") == 0)) ? "'" + filter + "'" : "CAST(NULL AS VARCHAR(200))") + (forPsDelete ? "" : (columnsComparable ? ",1" : ",0")) + (forPsDelete ? "" : ",'" + colList + "'") + ")");
}
firstCall = false;
}


public static void genTabListView(int fileNo,  int thisOrgIndex, int srcPoolIndex, int dstPoolIndex, Integer ddlTypeW, Integer indentW, Boolean forPsDeleteW) {
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

boolean forPsDelete; 
if (forPsDeleteW == null) {
forPsDelete = false;
} else {
forPsDelete = forPsDeleteW;
}

boolean firstCall;
firstCall = true;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH V_TabList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");

if (srcPoolIndex == dstPoolIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "schemaName,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "srcSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dstSchemaName,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tabName,");
if (forPsDelete) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "filter");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "filter,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "colListComparable,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "colList");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");

int thisEntityIndex;
String qualSrcTabName;
String qualDstTabName;
String qualSrcTabNameGen;
String qualDstTabNameGen;
String qualSrcTabNameNl;
String qualDstTabNameNl;
String parentFkAttr;

String psOidFilterStr;
psOidFilterStr = M01_Globals_IVK.g_anPsOid + " = <PS>";
String filter;

boolean processEntity;
boolean forLrt;
String fkAttrToDiv;
M22_Class_Utilities.NavPathFromClassToClass navPathFromClassToDiv;
M23_Relationship_Utilities.NavPathFromRelationshipToClass navPathFromRelToDiv;

for (int thisEntityIndex = 1; thisEntityIndex <= M22_Class.g_classes.numDescriptors; thisEntityIndex++) {
fkAttrToDiv = "";
if (forPsDelete) {
processEntity = !((M22_Class.g_classes.descriptors[thisEntityIndex].superClassIndex > 0) &  ((M22_Class.g_classes.descriptors[thisEntityIndex].specificToPool <= 0) |  (M22_Class.g_classes.descriptors[thisEntityIndex].specificToPool == M72_DataPool.g_pools.descriptors[srcPoolIndex].id)) & M22_Class.g_classes.descriptors[thisEntityIndex].isPsTagged);
} else {
processEntity = !(M22_Class.g_classes.descriptors[thisEntityIndex].isCommonToOrgs & ! M22_Class.g_classes.descriptors[thisEntityIndex].isCommonToPools & !M22_Class.g_classes.descriptors[thisEntityIndex].isLrtSpecific & !M22_Class.g_classes.descriptors[thisEntityIndex].notAcmRelated & !(M22_Class.g_classes.descriptors[thisEntityIndex].superClassIndex > 0) & (M22_Class.g_classes.descriptors[thisEntityIndex].specificToPool <= 0));
}
if (processEntity) {
if (M22_Class.g_classes.descriptors[thisEntityIndex].isPsTagged) {
filter = psOidFilterStr;
} else {
navPathFromClassToDiv = M22_Class.g_classes.descriptors[thisEntityIndex].navPathToDiv;
if (navPathFromClassToDiv.relRefIndex > 0) {
fkAttrToDiv = (navPathFromClassToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[navPathFromClassToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[navPathFromClassToDiv.relRefIndex].rightFkColName[ddlType]);
}

if (!(fkAttrToDiv.compareTo("") == 0)) {
filter = fkAttrToDiv + " = <DIV>";
}
}

int i;
for (int i = 1; i <= (M22_Class.g_classes.descriptors[thisEntityIndex].isUserTransactional &  forPsDelete ? 2 : 1); i++) {
forLrt = (i == 2);
qualSrcTabName = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, null, forLrt, null, null, null, null, null);
qualDstTabName = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, null, forLrt, null, null, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, fileNo, qualSrcTabName, qualDstTabName, firstCall, indent + 1, !(filter.compareTo("") == 0), filter, forPsDelete);

if (M22_Class.g_classes.descriptors[thisEntityIndex].hasNlAttrsInNonGenInclSubClasses) {
parentFkAttr = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[thisEntityIndex].shortName, null, null, null, null, null);
qualSrcTabNameNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[thisEntityIndex].classIndex, ddlType, thisOrgIndex, srcPoolIndex, null, forLrt, null, true, null, null, null);
qualDstTabNameNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[thisEntityIndex].classIndex, ddlType, thisOrgIndex, dstPoolIndex, null, forLrt, null, true, null, null, null);

if (forLrt) {
String qualSrcTabNamePub;
qualSrcTabNamePub = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, null, false, null, null, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, true, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, !(filter.compareTo("") == 0), parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabName : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabName)) + " WHERE " + filter + ") OR " + parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabNamePub : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabNamePub)) + " WHERE " + filter + ")", forPsDelete);
} else {
genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, true, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, !(filter.compareTo("") == 0), parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabName : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabName)) + " WHERE " + filter + ")", forPsDelete);
}
}

if (M22_Class.g_classes.descriptors[thisEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[thisEntityIndex].hasNoIdentity) {
qualSrcTabNameGen = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, true, forLrt, null, null, null, null, null);
qualDstTabNameGen = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, true, forLrt, null, null, null, null, null);

parentFkAttr = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[thisEntityIndex].shortName, null, null, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, true, false, fileNo, qualSrcTabNameGen, qualDstTabNameGen, firstCall, indent + 1, !(filter.compareTo("") == 0), filter, forPsDelete);

if (M22_Class.g_classes.descriptors[thisEntityIndex].hasNlAttrsInGenInclSubClasses) {
qualSrcTabNameNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[thisEntityIndex].classIndex, ddlType, thisOrgIndex, srcPoolIndex, true, forLrt, null, true, null, null, null);
qualDstTabNameNl = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[thisEntityIndex].classIndex, ddlType, thisOrgIndex, dstPoolIndex, true, forLrt, null, true, null, null, null);

if (forLrt) {
String qualSrcTabNameGenPub;
qualSrcTabNameGenPub = M04_Utilities.genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, true, false, null, null, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, true, true, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, !(filter.compareTo("") == 0), parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabNameGen : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabNameGen)) + " WHERE " + filter + ") OR " + parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabNameGenPub : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabNameGenPub)) + " WHERE " + filter + ")", forPsDelete);
} else {
genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, true, true, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, !(filter.compareTo("") == 0), parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + (forPsDelete ? qualSrcTabNameGen : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualSrcTabNameGen)) + " WHERE " + filter + ")", forPsDelete);
}
}
}
}
}
}

for (int thisEntityIndex = 1; thisEntityIndex <= M23_Relationship.g_relationships.numDescriptors; thisEntityIndex++) {
fkAttrToDiv = "";
if (forPsDelete) {
processEntity = !(M23_Relationship.g_relationships.descriptors[thisEntityIndex].notAcmRelated &  (M23_Relationship.g_relationships.descriptors[thisEntityIndex].specificToPool <= 0) & M23_Relationship.g_relationships.descriptors[thisEntityIndex].isPsTagged & ((M23_Relationship.g_relationships.descriptors[thisEntityIndex].maxLeftCardinality < 0 &  M23_Relationship.g_relationships.descriptors[thisEntityIndex].maxRightCardinality < 0) |  M23_Relationship.g_relationships.descriptors[thisEntityIndex].isNl));
} else {
processEntity = !(M23_Relationship.g_relationships.descriptors[thisEntityIndex].isCommonToOrgs & ! M23_Relationship.g_relationships.descriptors[thisEntityIndex].isCommonToPools & !M23_Relationship.g_relationships.descriptors[thisEntityIndex].isLrtSpecific & !M23_Relationship.g_relationships.descriptors[thisEntityIndex].notAcmRelated & (M23_Relationship.g_relationships.descriptors[thisEntityIndex].specificToPool <= 0) & ((M23_Relationship.g_relationships.descriptors[thisEntityIndex].maxLeftCardinality < 0 &  M23_Relationship.g_relationships.descriptors[thisEntityIndex].maxRightCardinality < 0) |  M23_Relationship.g_relationships.descriptors[thisEntityIndex].isNl));
}

if (processEntity) {
if (M23_Relationship.g_relationships.descriptors[thisEntityIndex].isPsTagged) {
filter = psOidFilterStr;
} else {
navPathFromRelToDiv = M23_Relationship.g_relationships.descriptors[thisEntityIndex].navPathToDiv;

int navToDivRelRefIndex;// follow this relationship when navigating to Division
Integer navToDivDirection;// indicates wheter we need to follow left or right hand side to navigate to Division
Integer navToFirstClassToDivDirection;// if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
String navRefClassShortName;
String fkAttrToClass;
int navRefClassIndex;

navToFirstClassToDivDirection = M23_Relationship.g_relationships.descriptors[thisEntityIndex].navPathToDiv.navDirectionToClass;
navToDivRelRefIndex = -1;
navToDivDirection = -1;
if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etLeft) {
// we need to follow relationship to left -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[thisEntityIndex].leftEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].leftEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].leftEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].leftEntityIndex].navPathToDiv.navDirection;
} else if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etRight) {
// we need to follow relationship to right -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[thisEntityIndex].rightEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].rightEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].rightEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisEntityIndex].rightEntityIndex].navPathToDiv.navDirection;
}
if (navToDivRelRefIndex > 0) {
if (navToDivDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navToDivRelRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navToDivRelRefIndex].rightFkColName[ddlType];
}
}

if (!(fkAttrToDiv.compareTo("") == 0)) {
String qualRefTabName;
qualRefTabName = M04_Utilities.genQualTabNameByClassIndex(navRefClassIndex, ddlType, thisOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);
filter = fkAttrToClass + " IN (SELECT OID FROM " + (forPsDelete ? qualRefTabName : "<REFSCHEMA>." + M04_Utilities.getUnqualObjName(qualRefTabName)) + " WHERE " + fkAttrToDiv + " = <DIV>)";
}
}

for (int i = 1; i <= (M23_Relationship.g_relationships.descriptors[thisEntityIndex].isUserTransactional &  forPsDelete ? 2 : 1); i++) {
forLrt = (i == 2);
qualSrcTabName = M04_Utilities.genQualTabNameByRelIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, forLrt, null, null, null, null, null);
qualDstTabName = M04_Utilities.genQualTabNameByRelIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, forLrt, null, null, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, fileNo, qualSrcTabName, qualDstTabName, firstCall, indent + 1, !(filter.compareTo("") == 0), filter, forPsDelete);

if (M23_Relationship.g_relationships.descriptors[thisEntityIndex].nlAttrRefs.numDescriptors > 0) {
parentFkAttr = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M23_Relationship.g_relationships.descriptors[thisEntityIndex].shortName, null, null, null, null, null);
qualSrcTabNameNl = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisEntityIndex].relIndex, ddlType, thisOrgIndex, srcPoolIndex, forLrt, null, true, null, null, null);
qualDstTabNameNl = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[thisEntityIndex].relIndex, ddlType, thisOrgIndex, dstPoolIndex, forLrt, null, true, null, null, null);

genTabListViewElement(thisEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, true, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, !(filter.compareTo("") == 0), parentFkAttr + " IN (SELECT " + M01_Globals.g_anOid + " FROM " + qualSrcTabName + " WHERE " + filter + ")", forPsDelete);
}
}
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
}

}