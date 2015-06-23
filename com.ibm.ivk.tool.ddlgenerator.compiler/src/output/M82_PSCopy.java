package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M82_PSCopy {


// ### IF IVK ###


public static final String tempOidMapTabName = "SESSION.OidMap";
public static final String tempOidNewTabName = "SESSION.OidNew";

private static final int processingStep = 1;


public static void genDdlForTempOidMap(int fileNo, Integer indentW, Boolean withReplaceW, Boolean includeTableForNewRecordsW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

boolean includeTableForNewRecords; 
if (includeTableForNewRecordsW == null) {
includeTableForNewRecords = false;
} else {
includeTableForNewRecords = includeTableForNewRecordsW;
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


M11_LRT.genProcSectionHeader(fileNo, "temporary table" + (includeTableForNewRecords ? "s" : "") + " for OID-mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "map2Oid    " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

if (includeTableForNewRecords) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M82_PSCopy.tempOidNewTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, true, onCommitPreserve, onRollbackPreserve);
}
}


public static void genPsCopySupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
genPsCopySupportDdlByType(M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genPsCopySupportDdlByType(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M71_Org.g_orgs.descriptors[thisOrgIndex].isPrimary) {
genPsCopySupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genPsCopySupportDdlByType(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M01_Globals.g_genLrtSupport)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    create view to determine LDM tables 'forming the Product Structure'
// ####################################################################################################################

String qualViewName;
qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnPsFormingLdmTab, M01_ACM_IVK.vsnPsFormingLdmTab, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for all LDM-tables 'forming the Product Structure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anLdmIsLrt + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " AE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameLdmTable + " LT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AE." + M01_ACM_IVK.conIsPsForming + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genPsCopySupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
// PS-Copy is only supported at 'pool-level'
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex != M01_Globals.g_primaryOrgId)) {
// PS-Copy is only supported at for 'primary organization'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    create view to determine PDM tables 'forming the Product Structure'
// ####################################################################################################################

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
String qualViewNamePsFormingLdmTable;
String qualViewNamePsFormingPdmTable;

qualViewNamePsFormingLdmTable = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.vnPsFormingLdmTab, M01_ACM_IVK.vsnPsFormingLdmTab, ddlType, null, null, null, null, null, null, null, null, null, null);
qualViewNamePsFormingPdmTable = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM_IVK.vnPsFormingPdmTab, M01_ACM_IVK.vsnPsFormingPdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View for all PDM-tables 'forming the Product Structure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewNamePsFormingPdmTable);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmIsLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AL." + M01_Globals.g_anLdmIsLrt + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePsFormingLdmTable + " AL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmTable + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkSchemaName + " = AL.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPdmLdmFkTableName + " = AL.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PT." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    SP for Copy-PS-Data-To-LRT-Tables
// ####################################################################################################################

qualViewNamePsFormingPdmTable = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexLrt, M01_ACM_IVK.vnPsFormingPdmTab, M01_ACM_IVK.vsnPsFormingPdmTab, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);

String psShortName;
psShortName = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].shortName;

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualProcNamePsCp2Lrt;
boolean extended;
int i;
for (int i = 1; i <= (M03_Config.generatePsCopyExtendedSupport ? 2 : 1); i++) {
extended = (i == 2);
qualProcNamePsCp2Lrt = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "PSCP2LRT" + (extended ? "_EXT" : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for 'Copying PS-Data to LRT-Tables'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePsCp2Lrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
M11_LRT.genProcParm(fileNo, "IN", "psOidOld_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to copy");
if (extended) {
M11_LRT.genProcParm(fileNo, "IN", "useLoggingforLrtTabs_in", M01_Globals.g_dbtBoolean, true, "if set to '0', logging is disabled for LRT-related tables, otherwise enabled");
M11_LRT.genProcParm(fileNo, "IN", "commitEachTable_in", M01_Globals.g_dbtBoolean, true, "if set to '1' commit after each table");
}
M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtLrtId, true, "ID of the LRT related to the copied Product Structure data");
M11_LRT.genProcParm(fileNo, "OUT", "psOidNew_out", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being copied (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
if (!(extended)) {
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (!(extended)) {
M11_LRT.genVarDecl(fileNo, "v_atEnd", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_tabSchema", M01_Globals.g_dbtDbSchemaName, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabName", M01_Globals.g_dbtDbTableName, "NULL", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_psOidNew", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_isCentralDataTransfer", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tempCategoryOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

if (!(extended)) {
M11_LRT.genProcSectionHeader(fileNo, "declare cursors", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE tabCursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNamePsFormingPdmTable);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLdmIsGen + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLdmIsNl + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M82_PSCopy.tempOidMapTabName + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
if (!(extended)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_atEnd = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
}

M82_PSCopy.genDdlForTempOidMap(fileNo, null, null, null, null, null);

if (extended) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePsCp2Lrt, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePsCp2Lrt, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTs  = CURRENT TIMESTAMP;");
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET useLoggingforLrtTabs_in = COALESCE(useLoggingforLrtTabs_in, 1);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET commitEachTable_in      = COALESCE(commitEachTable_in     , 0);");
}
M11_LRT.genProcSectionHeader(fileNo, "determine division OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "create OID of new Product Structure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MAX(" + M01_Globals.g_anOid + ") + " + String.valueOf(M01_LDM.gc_sequenceIncrementValue));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOidNew");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " < 1" + M01_LDM.gc_sequenceMinValue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOidNew = COALESCE(v_psOidNew, NEXTVAL FOR " + qualSeqNameOid + ");");

M11_LRT.genProcSectionHeader(fileNo, "copy base Product Structure element", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.cosnOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conIsUnderConstruction, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "copy NL-TEXT attributes for Product Structure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructureNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, false, false, null, M01_ACM.cosnOid, "NEXTVAL FOR " + qualSeqNameOid, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].shortName, null, null, null, null), "v_psOidNew", M01_ACM.conVersionId, "1", null, null, null, null, null);
M24_Attribute.genNlsTransformedAttrListForEntity(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructureNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genSurrogateKeyName(ddlType, psShortName, null, null, null, null) + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "initialize OID-mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "map2Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOidOld_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOidNew");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "begin a new LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualLrtBeginProcName + "(?,?,?,?,?)' ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "trNumber_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOidNew,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_isCentralDataTransfer");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "loop over all PS-forming tables and copy data into LRT-tables", null, null);
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_MQT." + M01_Globals.g_anPdmTableName + " AS c_tabNameMqt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_PRIV." + M01_Globals.g_anPdmTableName + " AS c_tabNamePriv");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anPdmLdmFkSchemaName + " = LT." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anPdmLdmFkTableName + " = LT." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LT_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anLdmIsGen + " = LT." + M01_Globals.g_anLdmIsGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anLdmIsNl + " = LT." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PT_PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_PRIV." + M01_Globals.g_anPdmLdmFkSchemaName + " = LT_PRIV." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_PRIV." + M01_Globals.g_anPdmLdmFkTableName + " = LT_PRIV." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LT_MQT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anLdmIsGen + " = LT." + M01_Globals.g_anLdmIsGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals.g_anLdmIsNl + " = LT." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_MQT." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PT_MQT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_MQT." + M01_Globals.g_anPdmLdmFkSchemaName + " = LT_MQT." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_MQT." + M01_Globals.g_anPdmLdmFkTableName + " = LT_MQT." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_MQT." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_MQT." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_PRIV." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PT_PRIV." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT_PRIV." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(AE." + M01_Globals_IVK.g_anIsPsForming + " = 1)");
int c;
for (int c = 1; c <= M22_Class.g_classes.numDescriptors; c++) {
if (M22_Class.g_classes.descriptors[c].supportExtendedPsCopy &  (M22_Class.g_classes.descriptors[c].superClassIndex <= 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntitySection + " = '" + M22_Class.g_classes.descriptors[c].sectionName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntityName + " = '" + M22_Class.g_classes.descriptors[c].className.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}
int r;
for (int r = 1; r <= M23_Relationship.g_relationships.numDescriptors; r++) {
if (M23_Relationship.g_relationships.descriptors[r].supportExtendedPsCopy &  M23_Relationship.g_relationships.descriptors[r].implementsInOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntitySection + " = '" + M23_Relationship.g_relationships.descriptors[r].sectionName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmEntityName + " = '" + M23_Relationship.g_relationships.descriptors[r].relName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anLdmIsGen + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LT." + M01_Globals.g_anLdmIsNl + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN tabCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_atEnd = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE (v_atEnd = 0) DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "tabCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF ((v_atEnd <> 0) OR (v_tabSchema IS NULL)) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GOTO ExitLoop;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}

if (extended) {
M11_LRT.genProcSectionHeader(fileNo, "disable logging on table (if required)", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF useLoggingforLrtTabs_in = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabName || ' ACTIVATE NOT LOGGED INITIALLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabNamePriv || ' ACTIVATE NOT LOGGED INITIALLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_tabNameMqt IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabNameMqt || ' ACTIVATE NOT LOGGED INITIALLY';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || c_tabSchema || '.PSCP2LRT_' || c_tabName || '(?,?,?,?,?,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL ' || v_tabSchema || '.PSCP2LRT_' || v_tabName || '(?,?,?,?,?,?)';");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOidOld_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOidNew,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_currentTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);");

if (extended) {
M11_LRT.genProcSectionHeader(fileNo, "commit (if required)", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF commitEachTable_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ExitLoop:");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE tabCursor WITH RELEASE;");
}

String qualTabNameCodeCategoryLrt;
qualTabNameCodeCategoryLrt = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null);
String qualTabNameCategoryLrt;
qualTabNameCategoryLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);
String qualTabNameCode;
qualTabNameCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine OID of Temporary Category", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_tempCategoryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryLrt + " CAT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAT." + M01_Globals_IVK.g_anPsOid + " = v_psOidNew");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAT." + M01_Globals_IVK.g_anIsDefault + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAT." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY -- there should be only one record");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "associate all Code related to 'v_divisionOid' with temporay Category", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategoryLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 16, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, "GCO_OID", "C." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, "CAT_OID", "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conAhClassId, "'" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexStandardCode) + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conAhOId, "C." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conDpClassNumber, "CAST(NULL AS SMALLINT)", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameCodeCategoryLrt + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC." + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CC.GCO_OID = C." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "associate all other Codes with temporary Category", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE vl6ccde011.codecategory_lrt SET cat_oid = v_tempCategoryOid WHERE inlrt = v_lrtOid;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M11_LRT.genProcSectionHeader(fileNo, "register all PS-related entities as being affected by the LRT", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

if (extended) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_ExtraEntities");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

boolean firstRow;
firstRow = true;
int j;
for (int j = 1; j <= M22_Class.g_classes.numDescriptors; j++) {
if (M22_Class.g_classes.descriptors[j].supportExtendedPsCopy &  (M22_Class.g_classes.descriptors[j].superClassIndex <= 0)) {
if (firstRow) {
firstRow = false;
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES ('" + M22_Class.g_classes.descriptors[j].classIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "')");
}
}

for (int j = 1; j <= M23_Relationship.g_relationships.numDescriptors; j++) {
if (M23_Relationship.g_relationships.descriptors[j].supportExtendedPsCopy &  M23_Relationship.g_relationships.descriptors[j].implementsInOwnTable) {
if (firstRow) {
firstRow = false;
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES ('" + M23_Relationship.g_relationships.descriptors[j].relIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, "PSE.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtOid, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conAcmOrParEntityId, "PSE." + M01_Globals.g_anAcmEntityId, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLrtOpId, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " A");

if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ExtraEntities V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityId + " = V.entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = V.entityType");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V.entityId IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals_IVK.g_anAcmIsPsForming + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") PSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "update reference to root AggregationSlot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS.MASASL_OID = (SELECT MAP.map2Oid FROM " + M82_PSCopy.tempOidMapTabName + " MAP WHERE MAP.oid = PS.MASASL_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_psOidNew");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET psOidNew_out = v_psOidNew;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out   = v_lrtOid;");

if (extended) {
M11_LRT.genProcSectionHeader(fileNo, "commit (if required)", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF commitEachTable_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (extended) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePsCp2Lrt, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePsCp2Lrt, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

String qualPsCpProcedureName;
boolean useGenWorkspaceParams;
for (int i = 1; i <= (M03_Config.generatePsCopyExtendedSupport ? 3 : 2); i++) {
useGenWorkspaceParams = (i == 2 |  i == 3);
extended = (i == 3);
qualPsCpProcedureName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "PSCOPY" + (extended ? "_EXT" : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNamePsCp2Lrt = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "PSCP2LRT" + (extended ? "_EXT" : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for 'Copying ProductStructure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualPsCpProcedureName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
M11_LRT.genProcParm(fileNo, "IN", "psOidOld_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to copy");
if (extended) {
M11_LRT.genProcParm(fileNo, "IN", "useLoggingforLrtTabs_in", M01_Globals.g_dbtBoolean, true, "if set to '0', logging is disabled for LRT-tables, otherwise enabled");
M11_LRT.genProcParm(fileNo, "IN", "commitEachTable_in", M01_Globals.g_dbtBoolean, true, "if set to '1' commit after each table");
}
M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtLrtId, true, "ID of the LRT related to the copied Product Structure data");
M11_LRT.genProcParm(fileNo, "OUT", "psOidNew_out", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being copied (sum over all tables)");

if (useGenWorkspaceParams) {
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_autoPriceSetProductive", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
if (!(useGenWorkspaceParams)) {
M11_LRT.genVarDecl(fileNo, "v_gwspError", "VARCHAR(256)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M82_PSCopy.genDdlForTempOidMap(fileNo, null, true, null, null, null);

if (extended) {
if (useGenWorkspaceParams) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null);
}
} else {
if (useGenWorkspaceParams) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);
}
}

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameters", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out    = 0;");
if (useGenWorkspaceParams) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspError_out   = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspInfo_out    = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspWarning_out = NULL;");
}

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTs = CURRENT TIMESTAMP;");
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET useLoggingforLrtTabs_in = COALESCE(useLoggingforLrtTabs_in, 1);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET commitEachTable_in      = COALESCE(commitEachTable_in     , 0);");
}

M11_LRT.genProcSectionHeader(fileNo, "copy Product Structure data into LRT-tables", null, null);
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNamePsCp2Lrt + "(?,?,?,?,?,?,?,?)' ;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNamePsCp2Lrt + "(?,?,?,?,?,?)' ;");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOidNew_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "trNumber_in,");
if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOidOld_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "useLoggingforLrtTabs_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "commitEachTable_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOidOld_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

String qualCommitProcedureName;
qualCommitProcedureName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "commit LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualCommitProcedureName + "(?,?,0,0,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");

if (useGenWorkspaceParams) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspError_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspInfo_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gwspWarning_out");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspError,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspInfo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspWarning");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_autoPriceSetProductive");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "create new Price Preferences", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexPricePreferences, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 14, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conRebateValueCode, "25", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conRebateValueType, "0", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conCurrency, "'EUR'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conCurrencyFactor, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conVehicleTotalPriceCalculationId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "psOidNew_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conCreateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conPrimaryPriceTypeForTestId, String.valueOf(M01_LDM_IVK.gc_dfltPrimaryPriceTypeFactory), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conPriceSelectionForOverlapId, String.valueOf(M01_LDM_IVK.gc_dfltPriceSelectionForOverlapFactory), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexPricePreferences, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "create new General Settings", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexGeneralSettings, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "psOidNew_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conCreateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGeneralSettings, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (useGenWorkspaceParams) {
M81_PSCreate.genGenWorkspacesInWorkDataPoolsDdl(fileNo, 1, ddlType, "psOidNew_out", "v_stmntTxt", "v_stmnt", "gwspError_out", "gwspInfo_out", "gwspWarning_out");
} else {
M81_PSCreate.genGenWorkspacesInWorkDataPoolsDdl(fileNo, 1, ddlType, "psOidNew_out", "v_stmntTxt", "v_stmnt", "v_gwspError", "v_gwspInfo", "v_gwspWarning");
}

// # create VIEWs & DISPLAYSLOTs
String qualTabNameView;
qualTabNameView = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexView, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

int relIndexDisplaySlot;
relIndexDisplaySlot = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnDisplaySlot, M01_ACM_IVK.rnDisplaySlot, null);
String qualTabNameDisplaySlot;
qualTabNameDisplaySlot = M04_Utilities.genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);

String qualProcNameRegStaticInit;
qualProcNameRegStaticInit = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnRegStaticInit, ddlType, null, null, null, null, null, null);

Integer cpAcmEntityType;
int cpAcmEntityIndex;
String qualTabName;
for (int j = 1; j <= 2; j++) {
if (j == 1) {
cpAcmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
cpAcmEntityIndex = M01_Globals_IVK.g_classIndexView;
M11_LRT.genProcSectionHeader(fileNo, "copy VIEWs", null, null);
} else {
cpAcmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship;
cpAcmEntityIndex = relIndexDisplaySlot;
M11_LRT.genProcSectionHeader(fileNo, "copy DISPLAYSLOTs", null, null);
}

qualTabName = M04_Utilities.genQualTabNameByEntityIndex(cpAcmEntityIndex, cpAcmEntityType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(cpAcmEntityIndex, cpAcmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 9, null, true, true, "S.", null, null, null, null, null, null, null, null, M01_Common.AttrCategory.eacPsFormingOid |  M01_Common.AttrCategory.eacOid, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "psOidNew_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, "VIW_OID", "(SELECT map2Oid FROM " + M82_PSCopy.tempOidMapTabName + " WHERE oid = S.VIW_OID FETCH FIRST 1 ROW ONLY)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "ESL_OID", "(SELECT map2Oid FROM " + M82_PSCopy.tempOidMapTabName + " WHERE oid = S.ESL_OID FETCH FIRST 1 ROW ONLY)", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(cpAcmEntityIndex, cpAcmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S." + M01_ACM_IVK.conPsOid + " = psOidOld_in");
if ((j == 1)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S." + M01_Globals_IVK.g_anIsStandard + " = " + M01_LDM.gc_dbTrue);
} else if ((j == 2)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (SELECT 1 FROM " + M82_PSCopy.tempOidMapTabName + " M WHERE M.oid = S.VIW_OID)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (j == 1) {
M11_LRT.genProcSectionHeader(fileNo, "update OID-mapping for table \"" + M82_PSCopy.tempOidMapTabName + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "map2Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "N." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " N");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_Globals_IVK.g_anName + " = N." + M01_Globals_IVK.g_anName + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "O." + M01_ACM_IVK.conPsOid + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "N." + M01_ACM_IVK.conPsOid + " = psOidNew_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
}

// ################################################################

M11_LRT.genProcSectionHeader(fileNo, "create related DataPools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexDataPool, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conAccessModeId, "pool.ID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, "DPOORG_OID", "org." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "DPSPST_OID", "psOidNew_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conPaiEntitlementGroupId, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexDataPool, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameOrganization + " org,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameDataPoolAccessMode + " pool,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmOrganization + " pOrg,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmDataPoolType + " pPool");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "org.oid = pOrg.ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "pool.id = pPool.id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "initialize PS-related data in table \"" + M01_Globals_IVK.g_qualTabNameRegistryStatic + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameRegStaticInit + "(NULL, psOidNew_out, NULL, v_rowCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (extended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF commitEachTable_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COMMIT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (extended) {
if (useGenWorkspaceParams) {
M07_SpLogging.genSpLogProcExit(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null);
}
} else {
if (useGenWorkspaceParams) {
M07_SpLogging.genSpLogProcExit(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out", null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualPsCpProcedureName, ddlType, null, "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);
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


private static void genOidMapSql(Integer ddlType, String colName, String qualTabName, String qualSeqNameOid, String lrtOidFilterStr, int fileNo, Integer indentW, String psOidFilterW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String psOidFilter; 
if (psOidFilterW == null) {
psOidFilter = "";
} else {
psOidFilter = psOidFilterW;
}

M11_LRT.genProcSectionHeader(fileNo, M01_LDM.gc_sqlDelimLine2, indent + 1, null);
M11_LRT.genProcSectionHeader(fileNo, "determine new OIDs to be mapped related to column '" + colName + "'", indent + 1, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DELETE FROM " + M82_PSCopy.tempOidNewTabName + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "OPEN mapCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_oid   = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_atEnd = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FETCH mapCursor INTO v_oid, v_map2Oid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FOR recordLoop AS csr CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " AS v_record_" + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
if (!(lrtOidFilterStr.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anInLrt + " = " + lrtOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
if (psOidFilter != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals_IVK.g_anPsOid + " = " + psOidFilter);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHILE (v_atEnd = 0) AND (v_record_" + colName + " >= v_oid) DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "FETCH mapCursor INTO v_oid, v_map2Oid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "IF (v_atEnd = 1) OR (v_record_" + colName + " < v_oid) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "INSERT INTO " + M82_PSCopy.tempOidNewTabName + "(oid) VALUES(v_record_" + colName + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLOSE mapCursor WITH RELEASE;");

M11_LRT.genProcSectionHeader(fileNo, "add new OIDs to set of OIDs to be mapped", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "map2Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M82_PSCopy.tempOidNewTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "),");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_newOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "V.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M82_PSCopy.tempOidMapTabName + " M");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "M.oid = V.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "M.oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NEXTVAL FOR " + qualSeqNameOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_newOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ";");

if (!(lrtOidFilterStr.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, "map OIDs in column '" + colName + "'", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "OPEN mapCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_oid = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FOR recordLoop AS csr CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " AS v_record_" + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anInLrt + " = " + lrtOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FOR UPDATE OF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHILE v_oid < v_record_" + colName + " DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "FETCH mapCursor INTO v_oid, v_map2Oid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "UPDATE " + qualTabName + " SET " + colName + " = v_map2Oid WHERE CURRENT OF csr;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLOSE mapCursor WITH RELEASE;");
}
}


private static void genPsCopySupportDdlForNlTable(int acmEntityIndex, Integer acmEntityType, String entityName,  int sectionIndex, String qualTabName, String qualNlTabName, String qualNlTabNameLrt, String qualProcName, String qualSeqNameOid, int fileNo, Integer ddlTypeW, Boolean forGenW, Integer forThisAttributeOnlyW) {
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

int forThisAttributeOnly; 
if (forThisAttributeOnlyW == null) {
forThisAttributeOnly = -1;
} else {
forThisAttributeOnly = forThisAttributeOnlyW;
}

int thisOrgIndex;
int thisPoolIndex;
thisOrgIndex = M01_Globals.g_primaryOrgIndex;
thisPoolIndex = M01_Globals.g_workDataPoolIndex;

M22_Class_Utilities.printSectionHeader("SP for copying records of table \"" + qualNlTabName + "\" (ACM-Class \"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\") into private table / includes OID-mapping", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "ID of the LRT corresponding to this transaction");
M11_LRT.genProcParm(fileNo, "IN", "psOidOld_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to copy");
M11_LRT.genProcParm(fileNo, "IN", "psOidNew_in", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "currentTs_in", "TIMESTAMP", true, "timestamp of this transaction");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being copied");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_atEnd", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_map2Oid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(200)", "'SELECT oid, map2Oid FROM " + M82_PSCopy.tempOidMapTabName + " ORDER BY oid FOR READ ONLY'", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE mapCursor CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M82_PSCopy.tempOidMapTabName + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_atEnd = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M82_PSCopy.genDdlForTempOidMap(fileNo, null, null, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, qualTabName, forThisAttributeOnly, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 8, null, true, true, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "psOidNew_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, forThisAttributeOnly, false, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

String fkColToParent;
int k;
for (int k = 1; k <= transformation.oidDescriptors.numDescriptors; k++) {
if (transformation.oidDescriptors.descriptors[k].colCat &  M01_Common.AttrCategory.eacFkOid) {
fkColToParent = transformation.oidDescriptors.descriptors[k].colName;
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabName + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabName + " PAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PAR." + M01_ACM_IVK.conPsOid + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PAR." + M01_Globals.g_anOid + " = NL." + fkColToParent);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNo, "prepare cursor for OID-mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntText;");

int i;
for (int i = 1; i <= transformation.oidDescriptors.numDescriptors; i++) {
if (((transformation.oidDescriptors.descriptors[i].colCat &  (M01_Common.AttrCategory.eacPsFormingOid |  M01_Common.AttrCategory.eacOid | M01_Common.AttrCategory.eacAhOid | M01_Common.AttrCategory.eacFkExtPsCopyOid)) != 0)) {
genOidMapSql(ddlType, transformation.oidDescriptors.descriptors[i].colName, qualNlTabNameLrt, qualSeqNameOid, "lrtOid_in", fileNo, 0, null);
}
}
if (i < transformation.oidDescriptors.numDescriptors) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "-- " + M01_LDM.gc_sqlDelimLine2);
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}


private static void genPsCopySupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNoNl, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean includeExtendedEntitySetW) {
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

boolean includeExtendedEntitySet; 
if (includeExtendedEntitySetW == null) {
includeExtendedEntitySet = false;
} else {
includeExtendedEntitySet = includeExtendedEntitySetW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// PS-Copy is only supported at 'pool-level'
return;
}

int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
boolean isUserTransactional;
boolean isPsTagged;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
int classIndex;
boolean useSurrogateKey;
String dbAcmEntityType;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityTypeDescr = "ACM-Class";
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
classIndex = M22_Class.g_classes.descriptors[acmEntityIndex].classIndex;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityTypeDescr = "ACM-Relationship";
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyRel;
classIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex;
useSurrogateKey = M03_Config.useSurrogateKeysForNMRelationships &  (M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange);
} else {
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePub;
qualTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null);

String qualTabNamePriv;
qualTabNamePriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, null, null, null, null);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcName;

// ####################################################################################################################
// #    SP for copying records related to a given Product Structure to LRT-table(s) / includes mapping of OIDS
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, "PSCP2LRT", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for copying records of table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\") into private tables / includes OID-mapping", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "ID of the LRT corresponding to this transaction");
M11_LRT.genProcParm(fileNo, "IN", "psOidOld_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to copy");
M11_LRT.genProcParm(fileNo, "IN", "psOidNew_in", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "currentTs_in", "TIMESTAMP", true, "timestamp of this transaction");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being copied");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_atEnd", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_map2Oid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(200)", "'SELECT oid, map2Oid FROM " + M82_PSCopy.tempOidMapTabName + " ORDER BY oid FOR READ ONLY'", null, null);

if (!(forGen & ! M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities)) {
M11_LRT.genVarDecl(fileNo, "acRecordCount", "INTEGER", "0", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE mapCursor CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M82_PSCopy.tempOidMapTabName + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_atEnd = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M82_PSCopy.genDdlForTempOidMap(fileNo, null, null, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 11, null, true, true, null, null, null, null, null, null, null, null, null, M01_Common.AttrCategory.eacAnyOid, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conPsOid, "psOidNew_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLrtComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conCreateTimestamp, "currentTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conLastUpdateTimestamp, "currentTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = psOidOld_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNo, "prepare cursor for OID-mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntText;");

if (forGen &  useSurrogateKey) {
genOidMapSql(ddlType, M04_Utilities.genAttrName(entityShortName + "_" + M01_Globals.g_surrogateKeyNameShort, ddlType, null, null, null, null, null, null), qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, null);
}
int i;
for (int i = 1; i <= transformation.oidDescriptors.numDescriptors; i++) {

// FIXME: Hard Coded Hack!!! remove this
// for certain FK-columns we are not allowed to map OIDs because we did not copy the target classes!
// FK : Property -> PropertyTemplate
if (transformation.oidDescriptors.descriptors[i].colName.compareTo("PTMHTP_OID") == 0) {
goto NextI;
}

if ((transformation.oidDescriptors.descriptors[i].colCat &  M01_Common.AttrCategory.eacAnyOid) == 0) {
goto NextI;
}
if (((transformation.oidDescriptors.descriptors[i].colCat &  (M01_Common.AttrCategory.eacPsFormingOid |  M01_Common.AttrCategory.eacOid | M01_Common.AttrCategory.eacAhOid | M01_Common.AttrCategory.eacFkExtPsCopyOid)) != 0)) {
genOidMapSql(ddlType, transformation.oidDescriptors.descriptors[i].colName, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, null);
}
NextI:
}
if (i < transformation.oidDescriptors.numDescriptors) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "-- " + M01_LDM.gc_sqlDelimLine2);
}

if (transformation.nlAttrRefs.numDescriptors > 0) {
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, "PSCP2LRT", null, null, null, null);

String qualTabNameNlPub;
qualTabNameNlPub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, null, null);

String qualTabNameNlPriv;
qualTabNameNlPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, true, null, null, null);

genPsCopySupportDdlForNlTable(acmEntityIndex, acmEntityType, entityName, sectionIndex, qualTabNamePub, qualTabNameNlPub, qualTabNameNlPriv, qualProcName, qualSeqNameOid, fileNoNl, ddlType, forGen, null);
}

if (!(forGen & ! M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities)) {
// we need to do this only once for the 'non-Gen-class'
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET acRecordCount =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLrtOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmOrParEntityId + " = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityType + " = '" + dbAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPID = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (acRecordCount = 0) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLrtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmOrParEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLrtOpId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + dbAcmEntityType + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}


public static void genPsCopySupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoStep2, Integer ddlTypeW, Boolean forGenW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// PS-Copy is only supported at for 'primary organization'
return;
}

if (M03_Config.generatePsCopySupport &  (M22_Class.g_classes.descriptors[classIndex].isPsForming |  M22_Class.g_classes.descriptors[classIndex].supportExtendedPsCopy) & M22_Class.g_classes.descriptors[classIndex].isUserTransactional) {
genPsCopySupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, fileNoStep2, ddlType, forGen, null);
}
}

public static void genPsCopySupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoStep2, Integer ddlTypeW, Boolean forGenW) {
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

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// PS-Copy is only supported at for 'primary organization'
return;
}

if (M03_Config.generatePsCopySupport &  (M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsForming |  M23_Relationship.g_relationships.descriptors[thisRelIndex].supportExtendedPsCopy) & M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional) {
genPsCopySupportDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, fileNoStep2, ddlType, forGen, null);
}
}

// ### ENDIF IVK ###

}