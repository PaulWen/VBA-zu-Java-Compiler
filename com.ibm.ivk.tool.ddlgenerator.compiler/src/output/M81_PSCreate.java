package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M81_PSCreate {


// ### IF IVK ###


private static final int processingStep = 2;


public static void genPsCreateSupportDdl(Integer ddlType) {
int i;
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtPdm &  M01_Globals.g_genLrtSupport) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M71_Org.g_orgs.descriptors[thisOrgIndex].isPrimary) {
genPsCreateSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
}


private static void genPsCreateSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
// PS-Create is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String attrNameFkAggSlot;
attrNameFkAggSlot = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexAggregationSlot].shortName, null, null, null, null);

String masterAggSlotClassIdStr;
masterAggSlotClassIdStr = M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexMasterAggregationSlot);

String qualTabNameAggregationSlotPriv;
qualTabNameAggregationSlotPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualTabNameAggregationSlotGenPriv;
qualTabNameAggregationSlotGenPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null);

String qualTabNameAggregationSlotGenNlPriv;
qualTabNameAggregationSlotGenNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, true, null, null, null);

String qualTabNameAggregationSlotNlPriv;
qualTabNameAggregationSlotNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);

String masterEndSlotClassIdStr;
masterEndSlotClassIdStr = M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexMasterEndSlot);

String qualTabNameEndSlotPriv;
qualTabNameEndSlotPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualTabNameEndSlotGenPriv;
qualTabNameEndSlotGenPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null);

String qualTabNameEndSlotGenNlPriv;
qualTabNameEndSlotGenNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, true, null, null, null);

String qualTabNameEndSlotNlPriv;
qualTabNameEndSlotNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);

String categoryShortName;
String categoryClassIdStr;
categoryShortName = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCategory].shortName;
categoryClassIdStr = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCategory].classIdStr;

String qualTabNameCategoryPriv;
qualTabNameCategoryPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String qualTabNameCategoryGenPriv;
qualTabNameCategoryGenPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null);

String qualTabNameCategoryGenNlPriv;
qualTabNameCategoryGenNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, true, null, null, null);

String qualTabNameCategoryNlPriv;
qualTabNameCategoryNlPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, true, null, null, null);

String qualTabNameGenericCodePub;
qualTabNameGenericCodePub = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, false, null, null, null, null, null);

String qualTabNameGenericCodePriv;
qualTabNameGenericCodePriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String standardCodeClassIdStr;
standardCodeClassIdStr = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexStandardCode].classIdStr;

String qualTabNameCodeCategoryPriv;
qualTabNameCodeCategoryPriv = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null);

String qualProcNamePsCreate;
qualProcNamePsCreate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "PsCreate", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

boolean useGenWorkspaceParams;
int i;
for (int i = 1; i <= 2; i++) {
useGenWorkspaceParams = (i == 2);

M22_Class_Utilities.printSectionHeader("SP for 'Creating ProductStructure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePsCreate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
M11_LRT.genProcParm(fileNo, "IN", "languageId1_in", M01_Globals.g_dbtEnumId, true, "ID of the language for first set of labels");
M11_LRT.genProcParm(fileNo, "IN", "languageId2_in", M01_Globals.g_dbtEnumId, true, "ID of the language for second set of labels");
M11_LRT.genProcParm(fileNo, "IN", "psLabel1_in", "VARCHAR(225)", true, "(NL-) label of new ProductStructure (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "psLabel2_in", "VARCHAR(225)", true, "(NL-) label of new ProductStructure (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "mainAggSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of main AggregationSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "mainAggSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of main AggregationSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempCatLabel1_in", "VARCHAR(225)", true, "(NL-) label of temporary Category (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempCatLabel2_in", "VARCHAR(225)", true, "(NL-) label of temporary Category (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupCatLabel1_in", "VARCHAR(225)", true, "(NL-) label of duplicating Category (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupCatLabel2_in", "VARCHAR(225)", true, "(NL-) label of duplicating Category (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempEndSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of temporary EndSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempEndSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of temporary EndSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupEndSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of duplicating EndSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupEndSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of duplicating EndSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "lrtComment_in", M01_Globals_IVK.g_dbtChangeComment, true, "LRT comment related to this transaction");
M11_LRT.genProcParm(fileNo, "IN", "psStartTime_in", "DATE", true, "date when this Product Structure first is valid");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "identifies the division that the Product Structure corresponds to");
M11_LRT.genProcParm(fileNo, "IN", "paintHandlingModeId_in", M01_Globals.g_dbtEnumId, true, "paint handling mode used for the new Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "dupCodeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, "'dup' code number ('DUP0')");
M11_LRT.genProcParm(fileNo, "IN", "dupCodeType_in", "CHAR(1)", true, "'dup' code type ('0')");
M11_LRT.genProcParm(fileNo, "IN", "defaultCodeGroupKey_in", "VARCHAR(2)", true, "DEPRECATED - formerly: default code group key - if it is required to create it");
M11_LRT.genProcParm(fileNo, "IN", "priceLogic_in", M01_Globals.g_dbtEnumId, true, "'price logic' of the Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "type_in", M01_Globals.g_dbtEnumId, true, "'type' of the Product Structure");

M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtLrtId, true, "ID of the LRT related to the created Product Structure data");
M11_LRT.genProcParm(fileNo, "OUT", "psOidNew_out", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being created (sum over all tables)");

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
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_createProdTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_isCentralDataTransfer", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_psOidNew", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rootAggSlotOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rootAggSlotGenOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tempEndSlotOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tempEndSlotGenOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dupEndSlotOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dupEndSlotGenOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tempCategoryOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tempCategoryGenOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dupCategoryOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dupCategoryGenOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeTypeOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeCategoryOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_validityBegin", "DATE", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_validityEnd", "DATE", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_autoPriceSetProductive", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
if (!(useGenWorkspaceParams)) {
M11_LRT.genVarDecl(fileNo, "v_gwspError", "VARCHAR(256)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePsCreate, ddlType, null, "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "set defaults if no values provided", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF languageId1_in         IS NULL THEN SET languageId1_in         = 1                                ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF languageId2_in         IS NULL THEN SET languageId2_in         = 2                                ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF psLabel1_in            IS NULL THEN SET psLabel1_in            = 'Bezeichnung der Productstruktur'; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF psLabel2_in            IS NULL THEN SET psLabel2_in            = 'label of productstructure'      ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mainAggSlotLabel1_in   IS NULL THEN SET mainAggSlotLabel1_in   = 'Wurzel-Aggregationsslot'        ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mainAggSlotLabel2_in   IS NULL THEN SET mainAggSlotLabel2_in   = 'root aggregatuionslot'          ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF tempCatLabel1_in       IS NULL THEN SET tempCatLabel1_in       = 'temporaräre Kategorie'          ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF tempCatLabel2_in       IS NULL THEN SET tempCatLabel2_in       = 'temporary category'             ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupCatLabel1_in        IS NULL THEN SET dupCatLabel1_in        = 'Duplikatskategorie'             ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupCatLabel2_in        IS NULL THEN SET dupCatLabel2_in        = 'duplicating category'           ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF tempEndSlotLabel1_in   IS NULL THEN SET tempEndSlotLabel1_in   = 'tempoärer Endslot'              ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF tempEndSlotLabel2_in   IS NULL THEN SET tempEndSlotLabel2_in   = 'temporary endslot'              ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupEndSlotLabel1_in    IS NULL THEN SET dupEndSlotLabel1_in    = 'Duplikats-Endslot'              ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupEndSlotLabel2_in    IS NULL THEN SET dupEndSlotLabel2_in    = 'duplicating endslot'            ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF lrtComment_in          IS NULL THEN SET lrtComment_in          = 'no LRT comment'                 ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF psStartTime_in         IS NULL THEN SET psStartTime_in         = CURRENT DATE                     ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF paintHandlingModeId_in IS NULL THEN SET paintHandlingModeId_in = 1                                ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupCodeNumber_in       IS NULL THEN SET dupCodeNumber_in       = 'DUP0'                           ; END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF dupCodeType_in         IS NULL THEN SET dupCodeType_in         = 'D'                              ; END IF;");

M11_LRT.genProcSectionHeader(fileNo, "generate OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_createProdTs       = CURRENT TIMESTAMP;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rootAggSlotOid     = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rootAggSlotGenOid  = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tempEndSlotOid     = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tempEndSlotGenOid  = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dupEndSlotOid      = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dupEndSlotGenOid   = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tempCategoryOid    = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_tempCategoryGenOid = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dupCategoryOid     = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dupCategoryGenOid  = NEXTVAL FOR " + qualSeqNameOid + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_codeCategoryOid    = NEXTVAL FOR " + qualSeqNameOid + ";");

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

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameters", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out    = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out      = CAST(NULL AS " + M01_Globals.g_dbtOid + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET psOidNew_out    = CAST(NULL AS " + M01_Globals.g_dbtOid + ");");

if (useGenWorkspaceParams) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspError_out   = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspInfo_out    = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET gwspWarning_out = NULL;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rowCount = 0;");

M11_LRT.genProcSectionHeader(fileNo, "validity of created objects starts with psStartTime_in - if provided - otherwise with beginning of current month", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_validityBegin = COALESCE(psStartTime_in, CURRENT DATE - (DAY(CURRENT DATE) - 1) DAYS);");
M11_LRT.genProcSectionHeader(fileNo, "validity of created objects lasts 'for ever'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_validityEnd = DATE(" + M01_LDM_IVK.gc_valDateInfinite + ");");

M11_LRT.genProcSectionHeader(fileNo, "create new ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProductStructure, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 14, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conIsUnderConstruction, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conPaintHandlingModeId, "paintHandlingModeId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "PDIDIV_OID", "divisionOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conComment, "psLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, "MASASL_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPriceLogicId, "priceLogic_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conTypeId, "type_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, "ISTIREVALIDITY", M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProductStructure, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "label of ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructureNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexProductStructure, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].shortName, null, null, null, null), "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conLabel, "psLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProductStructure, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conLabel, "psLabel2_in", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProductStructure, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

String qualProcNameLrtBegin;
qualProcNameLrtBegin = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "begin a new LRT", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameLrtBegin + "(?,?,?,?,?)';");
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

M11_LRT.genProcSectionHeader(fileNo, "one row created in LRT-table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "create related DataPools", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameDataPool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexDataPool, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conAccessModeId, "pool.ID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, "DPOORG_OID", "org." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "DPSPST_OID", "v_psOidNew", null, null, null);
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

// for LRT-tables we count the number of affected rows via LRTCOMMIT

M11_LRT.genProcSectionHeader(fileNo, "create root AggregationSlot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 20, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, "ASPPAR_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conCardinality, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conSlotIndex, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, "SARASL_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM.conAhClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM.conAhOId, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM_IVK.conDisplayOrder, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 20, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "lrtComment of root AggregationSlot", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- changeComment / language 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 12, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, attrNameFkAggSlot, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conAhClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conAhOId, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "set main AggregationSlot at new ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MASASL_OID = v_rootAggSlotOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_psOidNew;");

M11_LRT.genProcSectionHeader(fileNo, "create GEN-part of root AggregationSlot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotGenPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 18, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, attrNameFkAggSlot, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conOid, "v_rootAggSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conValidFrom, "v_validityBegin", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conValidTo, "v_validityEnd", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM.conAhClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM.conAhOId, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "label of root AggregationSlot", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAggregationSlotGenNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 15, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, attrNameFkAggSlot, "v_rootAggSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "mainAggSlotLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conLabelNational, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conLabelIsNatActive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conAhClassId, "'" + masterAggSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conAhOId, "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "mainAggSlotLabel2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexAggregationSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "create temporary and duplicating Category", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conCategoryKindId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 19, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conIsDuplicating, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conIsDefault, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conDpClassNumber, "-1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conClassId, "'" + categoryClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM.conAhClassId, "'" + categoryClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM.conAhOId, "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM_IVK.conCategoryKindId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- duplicating Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_dupCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conIsDuplicating, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conIsDefault, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conDpClassNumber, "9999", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM.conAhOId, "v_dupCategoryOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "lrtComment of temporary and duplicating Category", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- changeComment / language 1 for temporary Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 12, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_tempCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conAhClassId, "'" + categoryClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conAhOId, "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- changeComment / language 1 for duplicating Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_dupCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conAhOId, "v_dupCategoryOid", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "create GEN-parts of temporary and duplicating Category", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryGenPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 17, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conOid, "v_tempCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conValidFrom, "v_validityBegin", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conValidTo, "v_validityEnd", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conAhClassId, "'" + categoryClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM.conAhOId, "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- duplicating Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_dupCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conOid, "v_dupCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM.conAhOId, "v_dupCategoryOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "labels of temporary and duplicating Category", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCategoryGenNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1 for temporary Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 15, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_tempCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "tempCatLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conLabelNational, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conLabelIsNatActive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conAhClassId, "'" + categoryClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conAhOId, "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2 for temporary Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "tempCatLabel2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1 for duplicating Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, categoryShortName, null, null, null, null), "v_dupCategoryGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "dupCatLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conAhOId, "v_dupCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2 for duplicating Category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "dupCatLabel2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "create temporary and duplicating EndSlot", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 25, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, "ESCESC_OID", "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, "LNKRBC_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conSr0Order, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conSr1Order, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conNsr1Order, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conIsDuplicating, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, "ESRASL_OID", "v_rootAggSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM_IVK.conSlotIndex, "CAST(NULL AS SMALLINT)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 20, "SERESL_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 21, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 22, M01_ACM.conAhClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 23, M01_ACM.conAhOId, "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 24, M01_ACM_IVK.conDisplayOrder, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 25, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- duplicating EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_dupEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, "ESCESC_OID", "v_dupCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conIsDuplicating, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 23, M01_ACM.conAhOId, "v_dupEndSlotOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "changeComment of temporary and duplicating EndSlot", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- changeComment / language 1 for temporary EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 12, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_tempEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conAhClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conAhOId, "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- changeComment / language 1 of duplicating EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_dupEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "create GEN-part of temporary and duplicating EndSlot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGenPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 33, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conOid, "v_tempEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conSlotTypeId, "2", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conIsLinked, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conIsBaseSlot, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conAssignedPaintZoneKey, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conIsSr0Slot, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM_IVK.conIsSr1Slot, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM_IVK.conIsNsr1Slot, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 20, M01_ACM_IVK.conIsRequired, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 21, M01_ACM_IVK.conIsViewForming, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 22, M01_ACM_IVK.conIsCabin, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 23, M01_ACM_IVK.conIsOrderField1, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 24, M01_ACM_IVK.conIsOrderField2, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 25, M01_ACM_IVK.conIsOrderField3, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 26, M01_ACM_IVK.conIsOrderField4, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 27, M01_ACM_IVK.conIsOrderField5, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 28, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 29, M01_ACM.conValidFrom, "v_validityBegin", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 30, M01_ACM.conValidTo, "v_validityEnd", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 31, M01_ACM.conAhClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 32, M01_ACM.conAhOId, "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 33, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- duplicating EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_dupEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conOid, "v_dupEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 32, M01_ACM.conAhOId, "v_dupEndSlotOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "labels of temporary and duplicating EndSlot", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotGenNlPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, "", null, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1 for temporary EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 15, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_tempEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "tempEndSlotLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conLabelNational, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conLabelIsNatActive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conAhClassId, "'" + masterEndSlotClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conAhOId, "v_tempEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2 for temporary EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "tempEndSlotLabel2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 1 of duplicating EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].shortName, null, null, null, null), "v_dupEndSlotGenOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "dupEndSlotLabel1_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conAhOId, "v_dupEndSlotOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "lrtComment_in", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- label / language 2 of duplicating EndSlot");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLanguageId, "languageId2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conLabel, "dupEndSlotLabel2_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conChangeComment, "CAST(NULL AS VARCHAR(1))", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, null, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "check if Codenumber dupCodeNumber_in already exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_codeOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCodePub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anCodeNumber + " = dupCodeNumber_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CDIDIV_OID = divisionOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if Codenumber dupCodeNumber_in does not exist, create it", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_codeOid IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_codeOid          = NEXTVAL FOR " + qualSeqNameOid + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of CodeType used for dup StandardCode", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_codeTypeOid = (SELECT " + M01_Globals.g_anOid + " FROM " + M01_Globals_IVK.g_qualTabNameCodeType + " WHERE CODETYPENUMBER = dupCodeType_in);");

M11_LRT.genProcSectionHeader(fileNo, "create new dup StandardCode", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericCodePriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conCodeCharacterId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 52, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conCodeNumber, "dupCodeNumber_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conIsAEF, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conCodePriority, "0", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conNotVisibleFactory, "''", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conNotVisibleNational, "''", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conHasConflict, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conIsNotPublished, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conIsBlockedFactory, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM_IVK.conIsBlockedNational, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conIsRebateEnabled, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conIsRebateEnabled + "_NATIONAL", M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM_IVK.conIsRebateEnabled + "_ISNATACTIVE", M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM_IVK.conIsCommissionDeductible, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 20, M01_ACM_IVK.conIsProductionRelevant, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 21, M01_ACM_IVK.conIsTaxRelevant, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 22, M01_ACM_IVK.conIsSideCosts, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 23, M01_ACM_IVK.conIsMotorVehicleCertificationRelevant, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 24, M01_ACM_IVK.conIsEstimationRelevant, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 25, M01_ACM_IVK.conPackageTypeId, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 26, M01_ACM_IVK.conName, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 27, M01_ACM_IVK.conContactPerson, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 28, M01_ACM_IVK.conStreet, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 29, M01_ACM_IVK.conZipCode, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 30, M01_ACM_IVK.conCity, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 31, M01_ACM_IVK.conState, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 32, M01_ACM_IVK.conFax, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 33, M01_ACM_IVK.conFon, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 34, M01_ACM_IVK.conEMail, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 35, M01_ACM_IVK.conAbhCode, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 36, M01_ACM.conOid, "v_codeOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 37, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 38, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 39, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 40, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 41, M01_ACM.conClassId, "'" + standardCodeClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 42, "CTLTLV_OID", "v_codeGrpLevel3Oid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 43, "CDIDIV_OID", "divisionOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 44, "CTYTYP_OID", "v_codeTypeOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 45, "ECDCDE_OID", "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 46, M01_ACM_IVK.conIsNational, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 47, M01_ACM.conValidFrom, M01_LDM_IVK.gc_valDateEarliest, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 48, M01_ACM.conValidTo, "v_validityEnd", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 49, M01_ACM.conAhClassId, "'" + standardCodeClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 50, M01_ACM.conAhOId, "v_codeOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 51, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 52, M01_ACM_IVK.conCodeCharacterId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "associate duplicating Code with duplicating Category", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategoryPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 17, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "v_codeCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLrtComment, "lrtComment_in", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 11, "GCO_OID", "v_codeOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, "CAT_OID", "v_dupCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conAhClassId, "'" + standardCodeClassIdStr + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conAhOId, "v_codeOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conDpClassNumber, "CAST(NULL AS SMALLINT)", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

// #########################################################

M11_LRT.genProcSectionHeader(fileNo, "associate all Code related to 'divisionOid_in' with temporay Category", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCodeCategoryPriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, "GCO_OID", "C." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, "CAT_OID", "v_tempCategoryOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conAhOId, "C." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conDpClassNumber, "CAST(NULL AS SMALLINT)", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCodeCategory, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodePub + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.CDIDIV_OID = divisionOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid + " <> v_codeOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

// procedure too large :-(
genPsCreateSupportDdlByPool2(fileNo, thisOrgIndex, thisPoolIndex, useGenWorkspaceParams, ddlType);

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePsCreate, ddlType, null, "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M22_Class_Utilities.printSectionHeader("SP for 'Creating ProductStructure'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePsCreate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
M11_LRT.genProcParm(fileNo, "IN", "languageId1_in", M01_Globals.g_dbtEnumId, true, "ID of the language for first set of labels");
M11_LRT.genProcParm(fileNo, "IN", "languageId2_in", M01_Globals.g_dbtEnumId, true, "ID of the language for second set of labels");
M11_LRT.genProcParm(fileNo, "IN", "psLabel1_in", "VARCHAR(225)", true, "(NL-) label of new ProductStructure (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "psLabel2_in", "VARCHAR(225)", true, "(NL-) label of new ProductStructure (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "mainAggSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of main AggregationSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "mainAggSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of main AggregationSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempCatLabel1_in", "VARCHAR(225)", true, "(NL-) label of temporary Category (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempCatLabel2_in", "VARCHAR(225)", true, "(NL-) label of temporary Category (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupCatLabel1_in", "VARCHAR(225)", true, "(NL-) label of duplicating Category (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupCatLabel2_in", "VARCHAR(225)", true, "(NL-) label of duplicating Category (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempEndSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of temporary EndSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "tempEndSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of temporary EndSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupEndSlotLabel1_in", "VARCHAR(225)", true, "(NL-) label of duplicating EndSlot (corresponding to languageId1_in)");
M11_LRT.genProcParm(fileNo, "IN", "dupEndSlotLabel2_in", "VARCHAR(225)", true, "(NL-) label of duplicating EndSlot (corresponding to languageId2_in)");
M11_LRT.genProcParm(fileNo, "IN", "lrtComment_in", M01_Globals_IVK.g_dbtChangeComment, true, "LRT comment related to this transaction");
M11_LRT.genProcParm(fileNo, "IN", "psStartTime_in", "DATE", true, "date when this Product Structure first is valid");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "identifies the division that the Product Structure corresponds to");
M11_LRT.genProcParm(fileNo, "IN", "paintHandlingModeId_in", M01_Globals.g_dbtEnumId, true, "paint handling mode used for the new Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "dupCodeNumber_in", M01_Globals_IVK.g_dbtCodeNumber, true, "'dup' code number ('DUP0')");
M11_LRT.genProcParm(fileNo, "IN", "dupCodeType_in", "CHAR(1)", true, "'dup' code type ('0')");
M11_LRT.genProcParm(fileNo, "IN", "defaultCodeGroupKey_in", "VARCHAR(2)", true, "DEPRECATED - formerly: default code group key - if it is required to create it");

M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtLrtId, true, "ID of the LRT related to the created Product Structure data");
M11_LRT.genProcParm(fileNo, "OUT", "psOidNew_out", M01_Globals.g_dbtOid, true, "OID of the new Product Structure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being created (sum over all tables)");

if (useGenWorkspaceParams) {
M11_LRT.genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", true, "in case of error of GEN_WORKSPACE: provides information about the error context");
M11_LRT.genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", true, "in case of error of GEN_WORKSPACE: JAVA stack trace");
M11_LRT.genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", false, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePsCreate, ddlType, null, "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNamePsCreate + "(cdUserId_in, trNumber_in, languageId1_in, languageId2_in, psLabel1_in, psLabel2_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "mainAggSlotLabel1_in, mainAggSlotLabel2_in, tempCatLabel1_in, tempCatLabel2_in, dupCatLabel1_in, dupCatLabel2_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tempEndSlotLabel1_in, tempEndSlotLabel2_in, dupEndSlotLabel1_in, dupEndSlotLabel2_in, lrtComment_in, psStartTime_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "divisionOid_in, paintHandlingModeId_in, dupCodeNumber_in, dupCodeType_in, defaultCodeGroupKey_in, 1, 1, lrtOid_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOidNew_out, rowCount_out" + (useGenWorkspaceParams ? ", gwspError_out, gwspInfo_out, gwspWarning_out" : "") + ");");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePsCreate, ddlType, null, "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out", null, null, null, null, null, null);

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


private static void genPsCreateSupportDdlByPool2(int fileNo,  int thisOrgIndex,  int thisPoolIndex, boolean useGenWorkspaceParams, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameChangeLog;
qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameChangeLogNlText;
qualTabNameChangeLogNlText = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

String qualTabNamePropertyLrt;
qualTabNamePropertyLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);
String qualTabNamePropertyGenLrt;
qualTabNamePropertyGenLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null);
String qualTabNamePropertyGenNlTextLrt;
qualTabNamePropertyGenNlTextLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, true, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "derive Properties from PropertyTemplates", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 15, null, null, null, "T.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 10, "PSPPST_OID", "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, "PTMHTP_OID", "T." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM.conAhOId, "-1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conAhClassId, "T." + M01_Globals.g_anCid, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, false, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anAhOid + " = " + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGenLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 22, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conIsDeleted, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conMaxLength, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conDigitsAfterDecimalPoint, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conUnit, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conReturnPropertyFormatId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conReturnUnit, "", null, null, null);

transformation.numMappings = 6;
M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);
transformation.numMappings = 22;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

transformation.attributePrefix = "T.";

M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 12, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 13, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 14, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 15, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 16, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 17, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 18, M01_ACM.conAhOId, "P." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 19, M01_ACM.conAhClassId, "T." + M01_Globals.g_anCid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 20, M01_ACM.conValidFrom, "v_validityBegin", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 21, M01_ACM.conValidTo, "v_validityEnd", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 22, "PRP_OID", "P." + M01_Globals.g_anOid, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, true, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyLrt + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PTMHTP_OID = T." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anInLrt + " = v_lrtOid");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGenNlTextLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, null, null, "TNL.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, true);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conAhOId, "PGEN." + M01_Globals.g_anAhOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conAhClassId, "PGEN." + M01_Globals.g_anAhCid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 10, "PRP_OID", "PGEN." + M01_Globals.g_anOid, null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, true, true, true, M01_Common.DdlOutputMode.edomListLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePropertyTemplateNl + " TNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyLrt + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PTMHTP_OID = TNL.PRT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePropertyGenLrt + " PGEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anOid + " = PGEN.PRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "register entities as being affected by the LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "A." + M01_Globals.g_anAcmEntityId + " IN (" + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexAggregationSlot].classIdStr + "'," + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexEndSlot].classIdStr + "'," + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCategory].classIdStr + "'," + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProperty].classIdStr + "'," + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericCode].classIdStr + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "A." + M01_Globals.g_anAcmEntityId + " IN (" + "'" + M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].relIdStr + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") PSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

String qualCommitProcedureName;
qualCommitProcedureName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "commit LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualCommitProcedureName + "(?,?,1,0,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_rowCount,");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_autoPriceSetProductive");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "consider ChangeLog rows as affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + (SELECT COUNT(*) FROM " + qualTabNameChangeLog + " WHERE " + M01_Globals_IVK.g_anPsOid + " = v_psOidNew);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + (SELECT COUNT(*) FROM " + qualTabNameChangeLogNlText + " NL WHERE (SELECT " + M01_Globals_IVK.g_anPsOid + " FROM " + qualTabNameChangeLog + " L WHERE L." + M01_Globals.g_anOid + " = NL.CLG_OID) = v_psOidNew);");

M81_PSCreate.genPsRelatedCtoObjsDdl(fileNo, thisOrgIndex, thisPoolIndex, ddlType, useGenWorkspaceParams);

M11_LRT.genProcSectionHeader(fileNo, "set output parameters", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET psOidNew_out = v_psOidNew;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out   = v_lrtOid;");
}


public static void genGenWorkspacesInWorkDataPoolsDdl(int fileNo, Integer indentW, Integer ddlTypeW, String varNamePsOidW, String varNameStmntTxtW, String stmntNameW, String varNameGwspErrorW, String varNameGwspErrorInfoW, String varNameGwspWarningW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String varNamePsOid; 
if (varNamePsOidW == null) {
varNamePsOid = "v_psOidNew";
} else {
varNamePsOid = varNamePsOidW;
}

String varNameStmntTxt; 
if (varNameStmntTxtW == null) {
varNameStmntTxt = "v_stmntTxt";
} else {
varNameStmntTxt = varNameStmntTxtW;
}

String stmntName; 
if (stmntNameW == null) {
stmntName = "v_stmnt";
} else {
stmntName = stmntNameW;
}

String varNameGwspError; 
if (varNameGwspErrorW == null) {
varNameGwspError = "v_gwspError";
} else {
varNameGwspError = varNameGwspErrorW;
}

String varNameGwspErrorInfo; 
if (varNameGwspErrorInfoW == null) {
varNameGwspErrorInfo = "v_gwspInfo";
} else {
varNameGwspErrorInfo = varNameGwspErrorInfoW;
}

String varNameGwspWarning; 
if (varNameGwspWarningW == null) {
varNameGwspWarning = "v_gwspWarning";
} else {
varNameGwspWarning = varNameGwspWarningW;
}

M11_LRT.genProcSectionHeader(fileNo, "create Solver-Files for new ProductStructure in all Work- and Productive data pools", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent + 1, true);
M11_LRT.genVarDecl(fileNo, "v_callCount", "INTEGER", "NULL", indent + 1, null);
M11_LRT.genVarDecl(fileNo, "v_accessModeId", M01_Globals.g_dbtEnumId, "NULL", indent + 1, null);

String qualProcNameGenWorkspaceWrapper;

qualProcNameGenWorkspaceWrapper = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
if (varNameGwspWarning == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET " + varNameStmntTxt + " = 'CALL " + qualProcNameGenWorkspaceWrapper + "(2,NULL,?,?,0,?)';");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET " + varNameStmntTxt + " = 'CALL " + qualProcNameGenWorkspaceWrapper + "(2,NULL,?,?,0,?,?,?,?)';");
}
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PREPARE " + stmntName + " FROM " + varNameStmntTxt + ";");

int i;
for (int i = 1; i <= 2; i++) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SET v_accessModeId = " + String.valueOf((i == 1 ? M01_Globals.g_workDataPoolId : M01_Globals_IVK.g_productiveDataPoolId)) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + stmntName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INTO");

if (varNameGwspWarning == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_callCount");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_callCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + varNameGwspError + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + varNameGwspErrorInfo + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + varNameGwspWarning);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + varNamePsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ";");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END;");
}


public static void genPsRelatedCtoObjsDdl(int fileNo,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW, Boolean useGenWorkspaceParamsW) {
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

boolean useGenWorkspaceParams; 
if (useGenWorkspaceParamsW == null) {
useGenWorkspaceParams = true;
} else {
useGenWorkspaceParams = useGenWorkspaceParamsW;
}

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualProcNameRegStaticInit;
qualProcNameRegStaticInit = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnRegStaticInit, ddlType, null, null, null, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

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
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
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
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "v_psOidNew", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conCreateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLastUpdateTimestamp, "v_createProdTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGeneralSettings, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (useGenWorkspaceParams) {
M81_PSCreate.genGenWorkspacesInWorkDataPoolsDdl(fileNo, 1, ddlType, "v_psOidNew", "v_stmntTxt", "v_stmnt", "gwspError_out", "gwspInfo_out", "gwspWarning_out");
} else {
M81_PSCreate.genGenWorkspacesInWorkDataPoolsDdl(fileNo, 1, ddlType, "v_psOidNew", "v_stmntTxt", "v_stmnt", "v_gwspError", "v_gwspInfo", "v_gwspWarning");
}

M11_LRT.genProcSectionHeader(fileNo, "initialize PS-related data in table \"" + M01_Globals_IVK.g_qualTabNameRegistryStatic + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameRegStaticInit + "(NULL, v_psOidNew, NULL, v_rowCount);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

// ### ENDIF IVK ###

}