package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M87_FactoryTakeOver {


// ### IF IVK ###


public static final String tempEnpOidTabName = "SESSION.EnpOids";
public static final String tempEbpTypePriceTabName = "SESSION.EbpTypePrice";
public static final String tempEnpTypePriceTabName = "SESSION.EnpTypePrice";
public static final String tempTabNameSr0ContextFac = "SESSION.Sr0ContextFac";
public static final String tempTabNameSr0ContextOrg = "SESSION.Sr0ContextMpc";

public static final int propertyTemplateIdEbp = 105;
public static final int propertyTemplateIdEnp = 107;
public static final String propertyTemplateIdListCalcPrice = "60,61,101,102";

private static final int processingStep = 5;
public static void genInsertSessionConflictMultiGa(int fileNo) {

M11_LRT.genProcSectionHeader(fileNo, "Split conflicts to seperate rows and insert to common session table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Conflict");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'exclusionFormulaFactory',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExclusionFormulaFactory= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'inclusionFormulaFactory',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrInclusionFormulaFactory= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'conclusionFactory',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrConclusionFactory= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'numValue',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrNumValue= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'valueGathering',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrValueGathering= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'boolValue',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrBoolValue= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'expression',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExpression= 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

}

public static void genFactoryTakeOverDdl(Integer ddlType) {
if (M03_Config.generateFwkTest | ! M01_Globals.g_genLrtSupport) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
int srcOrgIndex;
int srcPoolIndex;
int dstPoolIndex;

srcOrgIndex = M01_Globals.g_primaryOrgIndex;
srcPoolIndex = M01_Globals_IVK.g_productiveDataPoolIndex;
dstPoolIndex = M01_Globals.g_workDataPoolIndex;

genFactoryTakeOverDdlCommon(M01_Common.DdlTypeId.edtPdm);

int dstOrgIndex;
for (dstOrgIndex = 1; dstOrgIndex <= 1; dstOrgIndex += (1)) {
if (!(M71_Org.g_orgs.descriptors[dstOrgIndex].isPrimary)) {
genFactoryTakeOverDdlByOrg(srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
genFactoryTakeOverDdlByOrg2(srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
genFactoryTakeOverDdlByOrg3(srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
genFactoryTakeOverDdlByOrg4(srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
genFactoryTakeOverPriceConflictHandling(dstOrgIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
genFactoryTakeOverExtendedConflictHandling(dstOrgIndex, dstPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}


public static void genDdlForTempEnpOids(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for ENP-OIDs", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M87_FactoryTakeOver.tempEnpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "EBP_OID         " + M01_Globals.g_dbtOid + " NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ENP_OID         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ENPNEW_OID      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isDeleted       " + M01_Globals.g_dbtBoolean + " DEFAULT 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


private static void genDdlForTempTypePriceAspects(int fileNo, Integer indentW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for EBP Type Prices", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M87_FactoryTakeOver.tempEbpTypePriceTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid                        " + M01_Globals.g_dbtOid + " NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validFrom                  DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validTo                    DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "allowedCountryIdListOid    " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "disallowedCountryIdListOid " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CtxtOidList             VARCHAR(220),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1CtxtOidList             VARCHAR(220)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for ENP Type Prices", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M87_FactoryTakeOver.tempEnpTypePriceTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid                        " + M01_Globals.g_dbtOid + " NOT NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validFrom                  DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validTo                    DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "allowedCountryIdListOid    " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "disallowedCountryIdListOid " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CtxtOidList             VARCHAR(220),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1CtxtOidList             VARCHAR(220)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, true, null, null);
}


private static void genFactoryTakeOverDdlCommon(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    View determining the tables corresponding to aggregate heads
// ####################################################################################################################

String qualViewNameAggHead;
qualViewNameAggHead = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAggHeadTab, M01_ACM.vsnAggHeadTab, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View determining tables corresponding to aggregate heads", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewNameAggHead);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anAcmCondenseData + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAcmIsNt2m + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmFkSequenceNo);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A." + M01_Globals_IVK.g_anAcmCondenseData + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "A." + M01_Globals.g_anAcmIsNt2m + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anOrganizationId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmFkSequenceNo);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static String getNonAbstractClassIdSeq(int classIndex) {
String returnValue;
String classIdSeq;
classIdSeq = "";

classIdSeq = (M22_Class.g_classes.descriptors[classIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[classIndex].classIdStr + "'");

int j;
for (int j = 1; j <= M00_Helper.uBound(M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive); j++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[j]].isAbstract)) {
classIdSeq = classIdSeq + (classIdSeq.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].subclassIndexesRecursive[j]].classIdStr + "'";
}
}

returnValue = classIdSeq;
return returnValue;
}


private static void genFactoryTakeOverDdlByOrg(int srcOrgIndex, int dstOrgIndex, int srcPoolIndex, int dstPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  dstOrgIndex < 1 | srcPoolIndex < 0 | dstPoolIndex < 1)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(dstOrgIndex, ddlType, null, null, null, null);

String qualViewTabName;
qualViewTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualViewNameAggHead;
qualViewNameAggHead = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAggHeadTab, M01_ACM.vsnAggHeadTab, ddlType, null, null, null, null, null, null, null, null, null, null);

String qualProcedureNameGetEnpEbpMapping;
String qualProcedureNameSetEnp;

// ####################################################################################################################
// #    Retrieve OID-magging of EBP-/ENP-objects
// ####################################################################################################################

qualProcedureNameGetEnpEbpMapping = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexFactoryTakeover, M01_ACM_IVK.spnFtoGetEnpEbpMap, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Retrieving OID-magging of EBP-/ENP-objects", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameGetEnpEbpMapping);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", "INTEGER", true, "OID of the ProductStructure");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of EBPs found for 'Factory Takeover'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, !(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages));
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M87_FactoryTakeOver.genDdlForTempEnpOids(fileNo, null, null, null, null);
genDdlForTempTypePriceAspects(fileNo, null);
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);

M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, null, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameGetEnpEbpMapping, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M11_LRT.genProcSectionHeader(fileNo, "determine EBP-Typeprices", null, null);

String qualTabNameSrcGenericAspect;
String qualTabNameSrcProperty;
String qualTabNameSrcPropertyTemplate;
qualTabNameSrcGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
qualTabNameSrcProperty = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
qualTabNameSrcPropertyTemplate = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);

String qualTabNameDstGenericAspect;
String qualTabNameDstProperty;
qualTabNameDstGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);
qualTabNameDstProperty = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualViewNameDstGenericAspect;
String qualViewNameDstProperty;
String qualTabNameDstPropertyTemplate;
qualViewNameDstGenericAspect = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex, null, true, M03_Config.useMqtToImplementLrt, null, null, null, null, null);
qualViewNameDstProperty = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, dstOrgIndex, dstPoolIndex, null, true, M03_Config.useMqtToImplementLrt, null, null, null, null, null);
qualTabNameDstPropertyTemplate = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M87_FactoryTakeOver.tempEnpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M87_FactoryTakeOver.tempEnpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EBP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahIsDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameSrcGenericAspect + " GA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahObjectId = GA." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahClassId = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MICS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MICS.ahObjectId = GA." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MICS.isToBeDeleted = 0");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameSrcProperty + " PRP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PRPAPR_OID = PRP." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameSrcPropertyTemplate + " PRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahIsDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRT.ID = " + String.valueOf(M87_FactoryTakeOver.propertyTemplateIdEbp));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M87_FactoryTakeOver.tempEnpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EBP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahIsDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDstGenericAspect + " GA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahObjectId = GA." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahClassId = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDstProperty + " PRP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PRPAPR_OID = PRP." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDstPropertyTemplate + " PRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahIsDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRT.ID = " + String.valueOf(M87_FactoryTakeOver.propertyTemplateIdEbp));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

String qualFuncNameSr0Src;
String qualFuncNameSr1Src;
qualFuncNameSr0Src = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, "SR0Ctxt_OID", ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);
qualFuncNameSr1Src = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, "SR1Ctxt_OID", ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "identify ENP-Typeprices", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF rowCount_out > 0 THEN");

M11_LRT.genProcSectionHeader(fileNo, "retrieve details of related EBP-Typeprices in MPC's data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEbpTypePriceTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validTo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "allowedCountryIdListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "disallowedCountryIdListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CtxtOidList,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1CtxtOidList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anValidTo + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS.ACLACL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS.DCLDCL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameSr0Src + "(GAS." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameSr1Src + "(GAS." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEnpOidTabName + " B");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDstGenericAspect + " GAS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "B.EBP_OID = GAS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve details of all ENP-Typeprices in MPC's data pool", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEnpTypePriceTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validTo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "allowedCountryIdListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "disallowedCountryIdListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CtxtOidList,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1CtxtOidList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anValidTo + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS.ACLACL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS.DCLDCL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameSr0Src + "(GAS." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameSr1Src + "(GAS." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDstGenericAspect + " GAS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDstProperty + " PRP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS.PRPAPR_OID = PRP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDstPropertyTemplate + " PRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GAS." + M01_Globals.g_anCid + " = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRT.ID = " + String.valueOf(M87_FactoryTakeOver.propertyTemplateIdEnp));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "map ENPs to EBPs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEnpOidTabName + " EBP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EBP.ENP_OID =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAM.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M87_FactoryTakeOver.tempEbpTypePriceTabName + " GAF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M87_FactoryTakeOver.tempEnpTypePriceTabName + " GAM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.validFrom = GAM.validFrom");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.validTo = GAM.validTo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.allowedCountryIdListOid = GAM.allowedCountryIdListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(GAF.disallowedCountryIdListOid,0) = COALESCE(GAM.disallowedCountryIdListOid,0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.sr0CtxtOidList = GAM.sr0CtxtOidList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.sr1CtxtOidList = GAM.sr1CtxtOidList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GAF.oid = EBP.EBP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameGetEnpEbpMapping, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Set ENP- Prices
// ####################################################################################################################

boolean mapEnpInLrt;
String qualTargetNameGenericAspect;
String qualTargetNameProperty;

int i;
for (int i = 1; i <= 2; i++) {
mapEnpInLrt = (i == 1);
qualTargetNameGenericAspect = (mapEnpInLrt ? qualViewNameDstGenericAspect : qualTabNameDstGenericAspect);
qualTargetNameProperty = (mapEnpInLrt ? qualViewNameDstProperty : qualTabNameDstProperty);

qualProcedureNameSetEnp = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexFactoryTakeover, M01_ACM_IVK.spnFtoSetEnp, ddlType, dstOrgIndex, dstPoolIndex, null, (mapEnpInLrt ? "" : "NoLrt"), false, null);

M22_Class_Utilities.printSectionHeader("SP for Setting ENP-prices", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetEnp);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "rebateValue_in", "INTEGER", true, "rebate to apply (%)");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of ENPs being set");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_someEnpDeleted", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_someEnpCreated", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_propertyOidEnp", M01_Globals.g_dbtOid, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M87_FactoryTakeOver.genDdlForTempEnpOids(fileNo, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameSetEnp, ddlType, null, "rebateValue_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify that the rebate value entered is within the limits", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (COALESCE(rebateValue_in, -1) < 0) OR (rebateValue_in >= 100) THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameSetEnp, ddlType, -2, "rebateValue_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M79_Err.genSignalDdlWithParms("illegalRebateValue", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(rebateValue_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether some ENP is created", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (SELECT 1 FROM " + M87_FactoryTakeOver.tempEnpOidTabName + " WHERE isDeleted = 0 AND ENP_OID IS NULL ) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_someEnpCreated = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine whether some ENP is deleted", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (SELECT 1 FROM " + M87_FactoryTakeOver.tempEnpOidTabName + " WHERE isDeleted = 1 AND ENP_OID IS NOT NULL ) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_someEnpDeleted = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "create new ENPs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_someEnpCreated > 0 THEN");

M11_LRT.genProcSectionHeader(fileNo, "determine PROPERTY OID for ENP", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_propertyOidEnp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetNameProperty + " PRP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDstPropertyTemplate + " PRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRP.PTMHTP_OID = PRT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRT.ID = " + String.valueOf(M87_FactoryTakeOver.propertyTemplateIdEnp));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "create OIDs for new ENPs as needed", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEnpOidTabName + " MAP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.ENPNEW_OID = NEXTVAL FOR " + qualSeqNameOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.ENP_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

M11_LRT.genProcSectionHeader(fileNo, "handle INSERT in GENERICASPECT", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, (mapEnpInLrt ? 5 : 4), null, null, null, "EBP.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "MAP.ENPNEW_OID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, "EBP." + M01_Globals_IVK.g_anValue + " * (DECIMAL(100-rebateValue_in)/100)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, "PRPAPR_OID", "v_propertyOidEnp", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conAhOId, "MAP.ENPNEW_OID", null, null, null);
if (mapEnpInLrt) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
}

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameSrcGenericAspect + " EBP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempEnpOidTabName + " MAP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.EBP_OID = EBP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.ENP_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAP.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "update existing ENPs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetNameGenericAspect + " ENP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals_IVK.g_anValidTo + ",");
if (mapEnpInLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals.g_anStatus + ",");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP.ACLACL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP.DCLDCL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals_IVK.g_anValue + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP.NSR1CONTEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP." + M01_Globals_IVK.g_anValidTo + ",");
if (mapEnpInLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP.ACLACL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP.DCLDCL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP." + M01_Globals_IVK.g_anValue + " * (DECIMAL(100-rebateValue_in)/100),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EBP.NSR1CONTEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ENP." + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameSrcGenericAspect + " EBP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M87_FactoryTakeOver.tempEnpOidTabName + " MAP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAP.EBP_OID = EBP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAP.ENP_OID = ENP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAP.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENP." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ENP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M87_FactoryTakeOver.tempEnpOidTabName + " MAP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAP.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAP.ENP_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "remove deleted ENPs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_someEnpDeleted > 0 THEN");

M11_LRT.genProcSectionHeader(fileNo, "handle DELETE of EBPs", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetNameGenericAspect + " ENP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ENP." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MAP.ENP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M87_FactoryTakeOver.tempEnpOidTabName + " MAP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MAP.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MAP.ENP_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameSetEnp, ddlType, null, "rebateValue_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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


private static void genFactoryTakeOverDdlByOrg2(int srcOrgIndex, int dstOrgIndex, int srcPoolIndex, int dstPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  dstOrgIndex < 1 | srcPoolIndex < 1 | dstPoolIndex < 1)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

int clClassIndex;
clClassIndex = M01_Globals.g_classIndexChangeLog;
String qualSrcClTabName;
qualSrcClTabName = M04_Utilities.genQualTabNameByClassIndex(clClassIndex, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(dstOrgIndex, ddlType, null, null, null, null);

String qualViewTabName;
qualViewTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualViewNameAggHead;
qualViewNameAggHead = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAggHeadTab, M01_ACM.vsnAggHeadTab, ddlType, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

String qualProcedureNameFtoGetChangelog;
String qualProcedureNameFtoGetImplicitChanges;
String qualProcName;

// ####################################################################################################################
// #    SP for Retrieving ChangeLog for Factory Data Take-Over
// ####################################################################################################################

String qualTabNameChangeLog;
qualTabNameChangeLog = M01_Globals.gc_tempTabNameChangeLog;

qualProcedureNameFtoGetChangelog = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetChangeLog, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Retrieving ChangeLog for 'Factory Data Take-Over' (limit numer of records)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameFtoGetChangelog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "classIdList_in", "VARCHAR(200)", true, "(optional) ','-separated list of classIDs used as filter-critereon");
M11_LRT.genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", true, "(optional) maximum number of rows to retrieve (= -1 when called from FACTORYTAKEOVER)");
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtEnumId, true, "(optional) retrieve NL-strings only for this language");
M11_LRT.genProcParm(fileNo, "IN", "filterBySr0Context_in", M01_Globals.g_dbtBoolean, true, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)");
M11_LRT.genProcParm(fileNo, "INOUT", "endTimestamp_inout", "TIMESTAMP", true, "marks the 'current' timestamp: only records before this timestamp are retrieved");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows in the ChangeLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_maxClOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_startTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_endTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, false, null, null, null, null, null, null);
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, false, null, null, null);
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoGetChangelog, ddlType, null, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, dstOrgIndex, dstPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid = " + M01_Globals_IVK.g_activePsOidDdl + ";");

M11_LRT.genProcSectionHeader(fileNo, "make sure that ProductStructure exists and Division can be determined", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_divisionOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_divisionOid IS NULL) THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameFtoGetChangelog, ddlType, 2, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null);

M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_psOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of 'my Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_orgOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ID = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameFtoGetChangelog, ddlType, -2, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null);

M79_Err.genSignalDdl("noOrg", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine TimeStamp of last Factory Data Take-Over", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_startTimestamp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(LASTCENTRALDATATRANSFERCOMMIT)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_startTimestamp = COALESCE(v_startTimestamp, TIMESTAMP(" + M01_LDM_IVK.gc_valTimestampOrigin + "));");

M11_LRT.genProcSectionHeader(fileNo, "retrieve ChangeLog-Summary data", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET endTimestamp_inout = COALESCE(endTimestamp_inout, CURRENT TIMESTAMP);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_endTimestamp     = endTimestamp_inout;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogSummary);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.AHOBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AE." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN 1 ELSE 0 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN 1 ELSE 0 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN 1 ELSE 0 END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSrcClTabName + " CL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((CL." + M01_Globals_IVK.g_anPsOid + " IS NULL AND CL.DIVISIONOID = v_divisionOid) OR CL." + M01_Globals_IVK.g_anPsOid + " = v_psOid )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OPTIMESTAMP > v_startTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OPTIMESTAMP <= v_endTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AE." + M01_Globals.g_anAcmEntityId + " = CL." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AE." + M01_Globals.g_anAcmIsNt2m + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.AHOBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AE." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT (ISCREATED = 1 AND " + M01_Globals_IVK.g_anIsDeleted + " = 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "loop over aggregate heads", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_FltrClassIds");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(classIdList_in, CAST(',' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "elem IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_FltrAhClassIds");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahClassId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_FltrClassIds F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.classId = AH." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "classIdList_in IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAhCid + " IN (SELECT ahClassId FROM V_FltrAhClassIds)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIgnoreForChangelog + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsNt2m + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(dstPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each aggregate head individually", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnFtoGetChangeLog.toUpperCase() + "_' || c_tableName || '(?,?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "filterBySr0Context_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_startTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_endTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF maxRowCount_in < 0 THEN");
M11_LRT.genProcSectionHeader(fileNo, "retrieve MPC-related ChangeLog-Summary", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahIsCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahIsUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahIsDeleted,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL.OBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL.AHOBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AE." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN 1 ELSE 0 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN 1 ELSE 0 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(CASE CL.OPERATION_ID WHEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN 1 ELSE 0 END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.gc_tempTabNameChangeLog + " CL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AE." + M01_Globals.g_anAcmEntityId + " = CL." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL.OBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CL.AHOBJECTID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AE." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(AHD.isCreated," + M01_LDM.gc_dbFalse + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(AHD.isUpdated," + M01_LDM.gc_dbFalse + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(AHD.isDeleted," + M01_LDM.gc_dbFalse + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.isCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.isUpdated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.isDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V OBJ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V AHD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OBJ.ahObjectId = AHD.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine summary of implicit changes", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TABSCHEMA AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TABNAME AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameAggHead);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(dstPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anAcmCondenseData + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anAcmIsNt2m + " = " + M01_LDM.gc_dbFalse);
// do not call an empty generated SP
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TABNAME" + " <> " + "'EXPRESSION'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anLdmFkSequenceNo + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each aggregate head individually", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnFtoGetImplicitChanges.toUpperCase() + "_' || c_tableName || '(?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "if classId-filter is given ignore records not matching filter critereon", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classIdList_in IS NOT NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAhCid + " NOT IN (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameGetSubClassIdsByList + "(classIdList_in) ) X");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine max OID of ChangeLog - if number of output records is limited", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF maxRowCount_in > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_maxClOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ROWNUMBER() OVER (ORDER BY OID ASC) AS seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.gc_tempTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V_OidSeq");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_OidSeq.seqNo = maxRowCount_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

int changeLogClassIndex;
changeLogClassIndex = M01_Globals.g_classIndexChangeLog;
String qualTabNameChangeLogNl;
qualTabNameChangeLogNl = M04_Utilities.genQualTabNameByClassIndex(changeLogClassIndex, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, true, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(maxRowCount_in,1) > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF languageId_in > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, M01_Common.DdlOutputMode.edomNone |  M01_Common.DdlOutputMode.edomXref, null, null, null, null, null, null);

int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (tabColumns.descriptors[i].columnCategory == M01_Common.AttrCategory.eacLangId |  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CLNL." + tabColumns.descriptors[i].columnName + ",");
}
}

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, "CL.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, srcOrgIndex, srcPoolIndex, "CL", null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute.genTransformedAttrListForEntityWithColReuse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 6, null, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomExpression | M01_Common.DdlOutputMode.edomNoDdlComment | M01_Common.DdlOutputMode.edomColumnName, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals.gc_tempTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT OUTER JOIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTabNameChangeLogNl + " CLNL");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CLNL.CLG_OID = CL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CLNL." + M01_Globals.g_anLanguageId + " = languageId_in");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_maxClOid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CL." + M01_Globals.g_anOid + " <= v_maxClOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN logCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CL.*");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals.gc_tempTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(v_maxClOid IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CL." + M01_Globals.g_anOid + " <= v_maxClOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OPEN logCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoGetChangelog, ddlType, null, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Retrieving ChangeLog for 'Factory Data Take-Over' (limit numer of records)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameFtoGetChangelog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", true, "(optional) maximum number of rows to retrieve (= -1 when called from FACTORYTAKEOVER)");
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtEnumId, true, "(optional) retrieve NL-strings only for this language");
M11_LRT.genProcParm(fileNo, "IN", "filterBySr0Context_in", M01_Globals.g_dbtBoolean, true, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)");
M11_LRT.genProcParm(fileNo, "INOUT", "endTimestamp_inout", "TIMESTAMP", true, "marks the 'current' timestamp: only records before this timestamp are retrieved");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows in the ChangeLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoGetChangelog, ddlType, null, "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameFtoGetChangelog + "(CAST(NULL AS VARCHAR(1)), maxRowCount_in, languageId_in, filterBySr0Context_in, endTimestamp_inout, rowCount_out);");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoGetChangelog, ddlType, null, "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out", null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for retrieving cardinality of Changelog for Factory Data Take-Over
// ####################################################################################################################

String qualProcedureNameFtoGetChangelogCard;
qualProcedureNameFtoGetChangelogCard = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetChangeLogCard, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Retrieving cardinality of ChangeLog for 'Factory Data Take-Over'", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameFtoGetChangelogCard);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows in the log");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;");

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, false, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoGetChangelogCard, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "count rows in LRT-Log", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = (SELECT COUNT(*) FROM " + M01_Globals.gc_tempTabNameChangeLog + ");");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoGetChangelogCard, ddlType, null, "rowCount_out", null, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

boolean isGenericAspect;
String qualFuncNameHasAlCountry;

String qualSrcTabName;
String qualDstTabName;
String qualDstTabNameLrt;

for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[i].isAggHead &  M22_Class.g_classes.descriptors[i].isUserTransactional & !M22_Class.g_classes.descriptors[i].noFto) {
qualSrcTabName = M04_Utilities.genQualTabNameByClassIndex(i, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
qualDstTabName = M04_Utilities.genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

// GenericAspects always need special treatment ;-)
isGenericAspect = (M22_Class.g_classes.descriptors[i].className.toUpperCase() == "GENERICASPECT");
if (M22_Class.g_classes.descriptors[i].className.toUpperCase() != "EXPRESSION") {

// ####################################################################################################################
// #    Get Summary of Implicit Changes for Factory Data Take-Over
// ####################################################################################################################

qualProcedureNameFtoGetImplicitChanges = M04_Utilities.genQualProcNameByEntityIndex(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, M01_ACM_IVK.spnFtoGetImplicitChanges, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Retrieving Summary of Implicit Changes for 'Factory Data Take-Over' on Aggregate Head '" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameFtoGetImplicitChanges);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure to retrieve the ChangeLog for");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division to retrieve the ChangeLog for");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of 'my Organization'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows in the ChangeLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

boolean aggHeadContainsIsNotPublished;
String aggHeadSubClassIdStrList;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
aggHeadContainsIsNotPublished = false;
aggHeadSubClassIdStrList = "";

aggHeadSubClassIdStrList = (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].classIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].classIndex].classIdStr + "'");

int j;
for (int j = 1; j <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].classIndex].subclassIndexesRecursive); j++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].classIndex].subclassIndexesRecursive[j]].isAbstract)) {
aggHeadSubClassIdStrList = aggHeadSubClassIdStrList + (aggHeadSubClassIdStrList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[i].classIndex].subclassIndexesRecursive[j]].classIdStr + "'";
}
}

// todo: should this be derived during initialization and stored in the class itself?
M24_Attribute.genTransformedAttrListForEntityWithColReuse(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, true, null, M01_Common.DdlOutputMode.edomNone, null);
for (int j = 1; j <= tabColumns.numDescriptors; j++) {
if (tabColumns.descriptors[j].columnName.compareTo(M01_Globals_IVK.g_anIsNotPublished) == 0) {
aggHeadContainsIsNotPublished = true;
break;
}
}

if (M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex > 0 |  aggHeadContainsIsNotPublished | isGenericAspect) {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

if (aggHeadContainsIsNotPublished) {
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, false, null, null, null, null, null, null);
}
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null, null);

if (isGenericAspect) {
M12_ChangeLog.genDdlForTempFtoClgGenericAspect(fileNo, 1, true, null, null, false, false);
}

if (isGenericAspect) {
String qualTabNameCountryGroupElem;
qualTabNameCountryGroupElem = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryGroupElement, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null);
//            Dim qualTabNameGenericAspectOrg As String
//            qualTabNameGenericAspectOrg = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex)

M11_LRT.genProcSectionHeader(fileNo, "determine Countries managed by 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameManagedCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesManaged");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.CNT_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameOrgManagesCountry + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ORG_OID = orgOid_in");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountriesManaged M,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryGroupElem + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.countryOid = E.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.level < 1000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "M.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesManaged M");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine Countries relevant for 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesRelevant");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameManagedCountry);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.CNG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountriesRelevant R,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryGroupElem + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.countryOid = E.CSP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.level < 1000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesRelevant R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine CountryId Lists involving Countries relevant for 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountry + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameCountryIdXRef + " X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.CSP_OID = R.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

}

M11_LRT.genProcSectionHeader(fileNo, "determine implicit 'create' related to this aggregate", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

boolean useUnion;
useUnion = false;
if (M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex > 0) {
String qualRelTabOrg;
String relOrgEntityIdStr;

qualRelTabOrg = M04_Utilities.genQualTabNameByRelIndex(M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null);
relOrgEntityIdStr = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex].relIdStr;

M11_LRT.genProcSectionHeader(fileNo, "insert records related to a newly created organization-relationship", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualRelTabOrg + " VFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.entityId = '" + relOrgEntityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.entityType = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahIsCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.objectId = VFO." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VFO.ORG_OID = orgOid_in");
useUnion = true;
}

if (aggHeadContainsIsNotPublished) {
if (useUnion) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
}
M11_LRT.genProcSectionHeader(fileNo, "insert records related to a change of '" + M01_Globals_IVK.g_anIsNotPublished + "'", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog + " CL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityType = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityId IN (" + aggHeadSubClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.operation_ID = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.dbColumnName = '" + M01_Globals_IVK.g_anIsNotPublished + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.objectId = CL.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);
useUnion = true;
}

if (isGenericAspect) {
if (useUnion) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
}

M11_LRT.genProcSectionHeader(fileNo, "insert records related to a change of '" + M01_Globals_IVK.g_anIsNotPublished + "' of a code", 3, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericAspect) + "', -- aggregateType 'GenericAspect'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF." + M01_Globals.g_anAhCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF." + M01_Globals.g_anAhOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSrcTabName + " AF");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList + " ACL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF.ACLACL_OID = ACL.idListOid");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF." + M01_Globals_IVK.g_anIsNotPublished + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF.BCDBCD_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CL.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.gc_tempTabNameChangeLog + " CL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CL.entityType = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CL.entityId IN (" + getNonAbstractClassIdSeq(M01_Globals_IVK.g_classIndexGenericCode) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CL.operation_ID = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CL.dbColumnName = '" + M01_Globals_IVK.g_anIsNotPublished + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId = CL.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AF.DCLDCL_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AF.DCLDCL_OID NOT IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "DCL.idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList + " DCL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M11_LRT.genProcSectionHeader(fileNo, "insert records related to a change of 'ACLACL_OID' or 'DCLDCL_OID'", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.objectId = CL.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSrcTabName + " AF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId = AF." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AF." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityType = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityId IN (" + aggHeadSubClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.operation_ID = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.dbColumnName IN ('ACLACL_OID', 'DCLDCL_OID')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

qualFuncNameHasAlCountry = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[i].sectionIndex, "HASALCNTRY", ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameHasAlCountry + "(AF." + M01_Globals.g_anOid + ", AF." + M01_Globals.g_anCid + ", orgOid_in) = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M11_LRT.genProcSectionHeader(fileNo, "only consider this record if it does not yet exist for given MPC", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualDstTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "REF." + M01_Globals.g_anOid + " = MCLS.ahobjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (isGenericAspect) {
M11_LRT.genProcSectionHeader(fileNo, "determine implicit 'delete' related to this aggregate", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M11_LRT.genProcSectionHeader(fileNo, "delete records related to a change of 'ACLACL_OID' or 'DCLDCL_OID'", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.objectId = CL.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSrcTabName + " AF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId = AF." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityType = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.entityId IN (" + aggHeadSubClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.operation_ID = " + String.valueOf(M11_LRT.lrtStatusUpdated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.dbColumnName IN ('ACLACL_OID', 'DCLDCL_OID')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

qualFuncNameHasAlCountry = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[i].sectionIndex, "HASALCNTRY", ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualFuncNameHasAlCountry + "(AF." + M01_Globals.g_anOid + ", AF." + M01_Globals.g_anCid + ", orgOid_in) = " + M01_LDM.gc_dbFalse);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M11_LRT.genProcSectionHeader(fileNo, "only consider this record if it exists for given MPC", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualDstTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "REF." + M01_Globals.g_anOid + " = MCLS.ahobjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "aggregateType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeCreated,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isToBeDeleted");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogDecl(fileNo, null, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out  = 0;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}//<> EXPRESSION
// ####################################################################################################################
// #    ChangeLog for Factory Data Take-Over per aggregate
// ####################################################################################################################

String qualFuncNameSr0;
qualFuncNameSr0 = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].sectionIndex, "Sr0IsAvail", ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, M01_ACM_IVK.spnFtoGetChangeLog, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Retrieving ChangeLog for 'Factory Data Take-Over' on Aggregate Head '" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure to retrieve the ChangeLog for");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division to retrieve the ChangeLog for");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of 'my Organization'");
M11_LRT.genProcParm(fileNo, "IN", "filterBySr0Context_in", M01_Globals.g_dbtBoolean, true, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)");
M11_LRT.genProcParm(fileNo, "IN", "startTimestamp_in", "TIMESTAMP", true, "only ChangeLog records past this timestamp are retrieved");
M11_LRT.genProcParm(fileNo, "IN", "endTimestamp_in", "TIMESTAMP", true, "only ChangeLog records up to this timestamp are retrieved");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows returned in the ChangeLog");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

boolean containsSr0Context;
containsSr0Context = false;

int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (tabColumns.descriptors[k].columnName.compareTo(M01_Globals_IVK.g_anSr0Context) == 0) {
containsSr0Context = true;
break;
}
}

if (containsSr0Context) {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_sr0Context", "VARCHAR(50)", "NULL", null, null);
}
if (isGenericAspect) {
M11_LRT.genVarDecl(fileNo, "v_takeoverCBVFlag", "SMALLINT", "0", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, !(containsSr0Context));
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M07_SpLogging.genSpLogDecl(fileNo, null, true);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, null, null, null, null, null, null, null);
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, null, null, null, null);

if (containsSr0Context) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for Factory SR0CONTEXTs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M87_FactoryTakeOver.tempTabNameSr0ContextFac);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr0Context      VARCHAR(50),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isMpcRelevant   " + M01_Globals.g_dbtBoolean + " DEFAULT 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for MPC SR0CONTEXTs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M87_FactoryTakeOver.tempTabNameSr0ContextOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr0Context      VARCHAR(50)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, null, null, null);
}

if (isGenericAspect) {
M12_ChangeLog.genDdlForTempFtoClgGenericAspect(fileNo, 1, true, null, null, false, false);
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "filterBySr0Context_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null);

if (isGenericAspect) {
//          Dim qualTabNameCountryGroupElem As String
//          qualTabNameCountryGroupElem = genQualTabNameByRelIndex(g_relIndexCountryGroupElement, ddlType, dstOrgIndex, dstPoolIndex)
String qualTabNameGenericAspectOrg;
qualTabNameGenericAspectOrg = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine Countries managed by 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameManagedCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesManaged");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.CNT_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameOrgManagesCountry + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ORG_OID = orgOid_in");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountriesManaged M,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryGroupElem + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.countryOid = E.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "M.level < 1000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "M.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesManaged M");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine Countries relevant for 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesRelevant");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameManagedCountry);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.CNG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountriesRelevant R,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryGroupElem + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.countryOid = E.CSP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "R.level < 1000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountriesRelevant R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine CountryId Lists involving Countries relevant for 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.CIL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountry + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameCountryIdXRef + " X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.CSP_OID = R.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine cross-references for CountryId Lists involving Countries managed by 'this Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdXRef);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "idListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountryIdList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "V_CountryIdXRef");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "idListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "countryOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "X.CIL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "X.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountryIdList V,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameCountryIdXRef + " X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.idListOid = X.CIL_OID");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.idListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.CSP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.level + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_CountryIdXRef V,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCountryGroupElem + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.countryOid = E.CNG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.level < 1000");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.idListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_CountryIdXRef X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameManagedCountry + " MC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "X.countryOid = MC.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameCountrySpec + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid + " = MC.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anCid + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexCountry) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

// special handling for CodeBaumusterValidities depends on PricePreferences
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.ISDPB * P.TAKEOVERBLOCKEDPRICEFLAG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_takeoverCBVFlag");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePricePreferences + " P ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PS_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

}

M11_LRT.genProcSectionHeader(fileNo, "retrieve ChangeLog records related to this aggregate", null, null);

int offset;
offset = 0;

if (containsSr0Context) {
String qualFuncNameIsSubset;
qualFuncNameIsSubset = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnIsSubset, ddlType, null, null, null, null, null, true);

offset = 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF filterBySr0Context_in = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine SR0Contexts in Factory data", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempTabNameSr0ContextFac);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Context");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals_IVK.g_anSr0Context);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogSummary + " CLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSrcTabName + " AHD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals.g_anOid + " = CLS.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");

// Fixme: the following handling of navigation paths make implicit assumtions about cardinality of relationships!
//      : remove this!
if (M22_Class.g_classes.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
} else if (M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex > 0) {
String fkAttrToDiv;
M22_Class_Utilities.NavPathFromClassToClass navPathToDiv;
navPathToDiv = M22_Class.g_classes.descriptors[i].navPathToDiv;
if (navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navPathToDiv.relRefIndex].rightFkColName[ddlType];
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + fkAttrToDiv + " = divisionOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals_IVK.g_anSr0Context + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(AHD." + M01_Globals_IVK.g_anSr0Context + ") <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine SR0Contexts supported by 'this Organization'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M87_FactoryTakeOver.tempTabNameSr0ContextOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Context");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SR0." + M01_Globals_IVK.g_anSr0Context);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectOrg + " NSR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectOrg + " SR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1.E1VEX1_OID = SR1." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectOrg + " SR0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SR1.E0VEX0_OID = SR0." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1." + M01_Globals.g_anCid + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexNSr1Validity) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "first check: compare by 'syntactic containment'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR sr0Loop AS sr0Cursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Context AS fSr0Context");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M87_FactoryTakeOver.tempTabNameSr0ContextFac + " F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR UPDATE OF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isMpcRelevant");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_sr0Context = '%' || REPLACE(fSr0Context, '+', '%') || '%';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF EXISTS(SELECT 1 FROM " + M87_FactoryTakeOver.tempTabNameSr0ContextOrg + " M WHERE M.sr0Context LIKE v_sr0Context) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M87_FactoryTakeOver.tempTabNameSr0ContextFac);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isMpcRelevant = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CURRENT OF sr0Cursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "second check (for remaining sr0Contexts): compare by 'set containment'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR sr0Loop AS sr0Cursor CURSOR FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "sr0Context AS fSr0Context");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M87_FactoryTakeOver.tempTabNameSr0ContextFac + " F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "F.isMpcRelevant = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR UPDATE OF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isMpcRelevant");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF EXISTS(SELECT 1 FROM " + M87_FactoryTakeOver.tempTabNameSr0ContextOrg + " M WHERE " + qualFuncNameIsSubset + "(fSr0Context, M.sr0Context, CHAR('+')) = 1) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M87_FactoryTakeOver.tempTabNameSr0ContextFac);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "isMpcRelevant = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CURRENT OF sr0Cursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
}

boolean filterBySr0Context;
for (int k = (containsSr0Context ? 1 : 2); k <= 2; k++) {
filterBySr0Context = (k == 1);
if ((containsSr0Context & ! filterBySr0Context)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
}

for (int j = 1; j <= (M22_Class.g_classes.descriptors[i].isDeletable ? 2 : 1); j++) {
if ((j == 1)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "(");
M24_Attribute.genAttrListForEntity(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, srcOrgIndex, srcPoolIndex, offset + 2, null, null, M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ")");
} else {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "UNION");
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, "CL.", null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, offset + 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomNoDdlComment | M01_Common.DdlOutputMode.edomColumnName, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + qualSrcClTabName + " CL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CLS.objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CLS.entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CLS.entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals_IVK.gc_tempTabNameChangeLogSummary + " CLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + (j == 2 ? qualDstTabName : qualSrcTabName) + " AHD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + M01_Globals.g_anOid + " = CLS.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");

// Fixme: the following handling of navigation paths make implicit assumtions about cardinality of relationships!
//      : remove this!
if (M22_Class.g_classes.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
} else if (M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex > 0) {
navPathToDiv = M22_Class.g_classes.descriptors[i].navPathToDiv;
if (navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[navPathToDiv.relRefIndex].rightFkColName[ddlType];
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + fkAttrToDiv + " = divisionOid_in");
}

if (filterBySr0Context) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M87_FactoryTakeOver.tempTabNameSr0ContextFac + " S0F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "S0F.sr0Context = AHD." + M01_Globals_IVK.g_anSr0Context);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "S0F.isMpcRelevant = " + M01_LDM.gc_dbTrue);
}

// FIXME: get rid of this hard-coding!
if (isGenericAspect &  (j == 1)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList + " ACL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD.ACLACL_OID = ACL.idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList + " DCL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD.DCLDCL_OID = DCL.idListOid");

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + qualTabNameGenericCode + " CD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + M01_Globals_IVK.g_qualTabNameCodeType + " CT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "CD.CTYTYP_OID = CT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD.BCDBCD_OID = CD." + M01_Globals.g_anOid);
}

String fkAttrToAh;
String qualRelTabName;
String qualCodeTypeTabName;
if (M22_Class.g_classes.descriptors[i].navPathToCodeType.relRefIndex > 0) {
M22_Class_Utilities.NavPathFromClassToClass navPathToCodeType;
String fkAttrToCodeType;
qualCodeTypeTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCodeType, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
navPathToCodeType = M22_Class.g_classes.descriptors[i].navPathToCodeType;

if (navPathToCodeType.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToCodeType = M23_Relationship.g_relationships.descriptors[navPathToCodeType.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToCodeType = M23_Relationship.g_relationships.descriptors[navPathToCodeType.relRefIndex].rightFkColName[ddlType];
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + qualCodeTypeTabName + " CTY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + fkAttrToCodeType + " = CTY." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CTY.CODETYPENUMBER <> 'H'");
}

// check which columns we find in this table
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntityWithColReuse(i, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null);

String fkAttrToOrg;
fkAttrToOrg = "";
if (M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex > 0) {
M22_Class_Utilities.NavPathFromClassToClass navPathToOrg;
qualRelTabName = M04_Utilities.genQualTabNameByRelIndex(M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null);
navPathToOrg = M22_Class.g_classes.descriptors[i].navPathToOrg;
if (navPathToOrg.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToOrg = M23_Relationship.g_relationships.descriptors[navPathToOrg.relRefIndex].leftFkColName[ddlType];
fkAttrToAh = M23_Relationship.g_relationships.descriptors[navPathToOrg.relRefIndex].rightFkColName[ddlType];
} else {
fkAttrToOrg = M23_Relationship.g_relationships.descriptors[navPathToOrg.relRefIndex].rightFkColName[ddlType];
fkAttrToAh = M23_Relationship.g_relationships.descriptors[navPathToOrg.relRefIndex].leftFkColName[ddlType];
}
if ((j == 1)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + qualRelTabName + " VFO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + M01_Globals.g_anOid + " = VFO." + fkAttrToAh);
}
}

boolean firstCondition;
boolean printedWhere;
firstCondition = true;
printedWhere = false;

if (!(fkAttrToOrg.compareTo("") == 0) &  (j == 1)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
printedWhere = true;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "COALESCE(VFO." + fkAttrToOrg + ", orgOid_in) = orgOid_in");
firstCondition = false;
}

int m;
for (int m = 1; m <= tabColumns.numDescriptors; m++) {
if (tabColumns.descriptors[m].columnName.compareTo(M01_Globals_IVK.g_anIsNotPublished) == 0) {
if (!(printedWhere)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
printedWhere = true;
}
if (!(firstCondition)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
}
firstCondition = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AHD." + M01_Globals_IVK.g_anIsNotPublished + " = " + M01_LDM.gc_dbFalse);
} else if (tabColumns.descriptors[m].columnName.compareTo(M01_Globals_IVK.g_anSr0Context) == 0) {
containsSr0Context = true;
}
}

if (isGenericAspect &  (j == 1)) {
if (!(printedWhere)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
printedWhere = true;
}
if (!(firstCondition)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
}
firstCondition = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(CD." + M01_Globals.g_anOid + " IS NULL OR (CD." + M01_Globals_IVK.g_anIsNotPublished + " = 0 AND CT.CODETYPENUMBER <> 'H'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AHD." + M01_Globals.g_anCid + " = '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexSr0Validity].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "-- at least one country exists in the 'allowed countries list' which is managed by 'this Organization' and not disallowed in the 'disallowed countries list'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 7) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 8) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 7) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 8) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdXRef + " AX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 7) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 8) + "AX.idListOid = ACL.idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 9) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 8) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 9) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 10) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 9) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 10) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdXRef + " DX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 9) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 10) + "DX.countryOid = AX.countryOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 11) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 10) + "DX.idListOid = DCL.idListOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 8) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 6) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + ")");
}

if (M22_Class.g_classes.descriptors[i].isDeletable) {
if (!(isGenericAspect)) {
if (!(printedWhere)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "WHERE");
printedWhere = true;
}
if (!(firstCondition)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 5) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "CLS.isDeleted = " + (j == 1 ? M01_LDM.gc_dbFalse : M01_LDM.gc_dbTrue));
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + ") FLTR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CL.objectId = FLTR.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CL." + M01_Globals.g_anAcmEntityId + " = FLTR." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CL." + M01_Globals.g_anAcmEntityType + " = FLTR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CL.OPTIMESTAMP > startTimestamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "CL.OPTIMESTAMP <= endTimestamp_in");
if (isGenericAspect) {
//special handling CodeBaumusterValidity: no changes in MPC in this case
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "AND NOT ( CL.OPERATION_ID = 2 AND CL.AHCLASSID = '09006' AND CL.DBCOLUMNNAME = 'ISBLOCKEDPRICE' AND v_takeoverCBVFlag = 0)");
}

if ((j == (M22_Class.g_classes.descriptors[i].isDeletable ? 2 : 1))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ";");
}
}
}

if (containsSr0Context) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "divisionOid_in", "orgOid_in", "filterBySr0Context_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}



private static void genFactoryTakeOverDdlByOrg4(int srcOrgIndex, int dstOrgIndex, int srcPoolIndex, int dstPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (M03_Config.generateFwkTest) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  dstOrgIndex < 1 | srcPoolIndex < 1 | dstPoolIndex < 1)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(-1, ddlType, null, null, null, null);

// ####################################################################################################################
// #    SP for initial factory takeover
// ####################################################################################################################

String qualProcedureNameInitialFTO;
qualProcedureNameInitialFTO = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoInitial, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for initial factory takeover", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameInitialFTO);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, " the organization oid");
M11_LRT.genProcParm(fileNo, "IN", "psOid", M01_Globals.g_dbtOid, true, "the productstructure oid");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid", M01_Globals.g_dbtOid, true, "the oid of the productstructure's division");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid", M01_Globals.g_dbtOid, true, "the lrt oid");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId", "VARCHAR(16)", true, "the lrt's user id");
M11_LRT.genProcParm(fileNo, "OUT", "endTimestamp_out", "TIMESTAMP", true, "marks the 'end timestamp' for data being taken over");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being taken over");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(32000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxtTerm", "VARCHAR(32000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colList", "VARCHAR(8000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colListForSelect", "VARCHAR(8000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_IsBlockedPriceExpression", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_fltrTxt", "VARCHAR(1600)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_deleteFltrTxt", "VARCHAR(800)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount2", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_aliasSchemaName", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgId", "SMALLINT", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_idx", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_len", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmtSinceFrom", "VARCHAR(32000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_insert", "VARCHAR(32000)", "NULL", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmt_cursor CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Termoids", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Termoids");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "termOid " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "termAhOid " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameInitialFTO, ddlType, 1, "orgOid_in", "psOid", "divisionOid", "lrtOid", "cdUserId", "endTimestamp_out", "rowCount_out", null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, dstOrgIndex, dstPoolIndex, M01_Common.TvBoolean.tvNull, 1);

String aliasSchemaName;
// contains <<mpcId>>
if (qualProcedureNameInitialFTO.length() > 35) {
aliasSchemaName = qualProcedureNameInitialFTO.substring(1 - 1, 1 + 17 - 1);
} else {
aliasSchemaName = qualProcedureNameInitialFTO.substring(1 - 1, 1 + 10 - 1);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_aliasSchemaName = '" + aliasSchemaName + "';");

M11_LRT.genProcSectionHeader(fileNo, "determine Organization's ID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNamePdmOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORGOID = orgOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET endTimestamp_out = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "copy data from factory productive data pool to organization's work data pool (into LRT tables)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " AS c_entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " AS c_entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityShortName + " AS c_entityShortName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " AS c_entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmIsPs + " AS c_isPs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ISLRT AS c_isLrt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.USELRTMQT AS c_useLrtMqt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A1." + M01_ACM.conAcmEntityShortName + " AS c_divPrefix,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntityName + " AS c_ahClassName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PF." + M01_Globals.g_anPdmFkSchemaName + " AS c_srcTabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anPdmFkSchemaName + " AS c_tgtTabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anPoolTypeId + " AS c_poolTypeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PH." + M01_Globals.g_anPdmFkSchemaName + " AS c_ahTabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PH." + M01_Globals.g_anPdmTableName + " AS c_ahTabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " AS c_isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " AS c_isNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " AS c_fkSequenceNo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN D.SRC_SCHEMANAME IS NULL THEN 0 ELSE 1 END) AS c_hasSelfReference");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PF." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PF." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntitySection + " = LH." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntityName + " = LH." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals.g_anAcmEntityType + " = LH." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LH." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LH." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LH." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PH." + M01_Globals.g_anPdmLdmFkSchemaName + " = LH." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PH." + M01_Globals.g_anPdmLdmFkTableName + " = LH." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PH." + M01_Globals.g_anOrganizationId + " = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(PH." + M01_Globals.g_anPoolTypeId + "," + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameFkDependency + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "D.SRC_SCHEMANAME = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "D.SRC_TABLENAME = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "D.DST_SCHEMANAME = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "D.DST_TABLENAME = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = A1." + M01_ACM.conAcmLeftEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A1." + M01_ACM.conAcmRightEntityName + " = 'DIVISION'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

boolean firstException;
String exceptionComment;
String thisExceptionComment;
firstException = true;
exceptionComment = "";
int i;
for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
thisExceptionComment = "";
if (!(M23_Relationship.g_relationships.descriptors[i].isUserTransactional &  ((M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == -1 &  M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == -1) |  M23_Relationship.g_relationships.descriptors[i].isNl) & (M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snOrder.toUpperCase() |  M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snReport.toUpperCase() | M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snPricing.toUpperCase()))) {
thisExceptionComment = "exclude \"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "." + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\" (section \"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\")";
}

if (!(thisExceptionComment.compareTo("") == 0)) {
if (firstException) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityId + " IN (");
} else {
M00_FileWriter.printToFile(fileNo, "," + (!(exceptionComment.compareTo("") == 0) ? " -- " + exceptionComment : ""));
}
exceptionComment = thisExceptionComment;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M23_Relationship.g_relationships.descriptors[i].relIdStr.toUpperCase() + "'");
firstException = false;
}
}

if (!(exceptionComment.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, exceptionComment, 1, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}

firstException = true;
exceptionComment = "";
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
thisExceptionComment = "";
if (M22_Class.g_classes.descriptors[i].classIndex == M01_Globals_IVK.g_classIndexPricePreferences) {
thisExceptionComment = "exclude \"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "." + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\" (already initialized)";
}

if ((M22_Class.g_classes.descriptors[i].superClassIndex <= 0 & ! M22_Class.g_classes.descriptors[i].isUserTransactional) |  M22_Class.g_classes.descriptors[i].classIndex == M01_Globals_IVK.g_classIndexTaxParameter | M22_Class.g_classes.descriptors[i].classIndex == M01_Globals_IVK.g_classIndexTaxType) {
if (M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snOrder.toUpperCase() |  M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snReport.toUpperCase() | M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() == M01_ACM_IVK.snPricing.toUpperCase()) {
thisExceptionComment = "exclude \"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "." + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\" (section \"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\")";
}
}

if (!(thisExceptionComment.compareTo("") == 0)) {
if (firstException) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityId + " IN (");
} else {
M00_FileWriter.printToFile(fileNo, "," + (!(exceptionComment.compareTo("") == 0) ? " -- " + exceptionComment : ""));
}
exceptionComment = thisExceptionComment;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M22_Class.g_classes.descriptors[i].classIdStr.toUpperCase() + "'");
firstException = false;
}
}

if (!(exceptionComment.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, exceptionComment, 1, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A." + M01_Globals.g_anAcmIsLrt + " = 1 OR PO." + M01_Globals.g_anPoolTypeId + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ISCTO = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A.ISCTP = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PO." + M01_Globals.g_anOrganizationId + " = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(PO." + M01_Globals.g_anPoolTypeId + "," + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PF." + M01_Globals.g_anOrganizationId + " = " + String.valueOf(M01_Globals.g_primaryOrgId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(PF." + M01_Globals.g_anPoolTypeId + "," + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + ") = " + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE WHEN A." + M01_Globals.g_anAhCid + " IS NULL THEN 0 ELSE 1 END) DESC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "determine common columns in source and target table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colList = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colListForSelect = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS colCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.COLNAME AS V_COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.TABNAME = T.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.COLNAME = T.COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.TABSCHEMA = c_tgtTabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.TABSCHEMA = c_srcTabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.TABNAME = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.COLNO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colList = v_colList || (CASE v_colList WHEN '' THEN '' ELSE ',' END) || V_COLNAME;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_tabName = 'GENERICASPECT' AND V_COLNAME = 'ISBLOCKEDPRICE' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_IsBlockedPriceExpression = 'CASE WHEN S.CLASSID = ''09006'' AND ((SELECT P.ISDPB FROM VL6CMET' || RIGHT(DIGITS(v_orgId),2) || '.PRICEPREFERENCES P WHERE P.PS_OID = S.PS_OID) = 0) THEN 1 ELSE S.ISBLOCKEDPRICE END';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || v_IsBlockedPriceExpression;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || V_COLNAME;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'INSERT INTO ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || '(' || v_colList || ')' || ' SELECT ' || v_colListForSelect || ' FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S WHERE (1=1)';");

M11_LRT.genProcSectionHeader(fileNo, "for PS-tagged tables: exclude records corresponding to PRODUCTSTRUCTURE under construction or to not relevant product structures/divisions", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (c_isPs = 1) AND (c_isNl = 0) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " PS WHERE S." + M01_Globals_IVK.g_anPsOid + " = PS." + M01_Globals.g_anOid + " AND PS." + M01_Globals.g_anOid + " = ' || psOid || ' AND PS." + M01_Globals_IVK.g_anIsUnderConstruction + " = 0)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF (c_isNl = 0) AND (c_isGen = 0) AND (c_divPrefix IS NOT NULL) THEN");
M11_LRT.genProcSectionHeader(fileNo, "filter out already existing GenericCodes", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND S.' || c_divPrefix ||  'DIV_OID = ' || divisionOid || ')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND S.' || c_divPrefix ||  'DIV_OID = ' || divisionOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF (c_isNl = 0) AND (c_isGen = 0) AND (c_divPrefix IS NULL) THEN");
M11_LRT.genProcSectionHeader(fileNo, "filter out already existing entries like EndNodeHasGenericCode, CODEVALIDFORORGANIZATION", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF (c_isNl = 1) AND (c_isPs = 0) THEN");
M11_LRT.genProcSectionHeader(fileNo, "filter out already existing GenericCode-NlText", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

String viewName;
M11_LRT.genProcSectionHeader(fileNo, "exclude records referring to aggregate heads not relevant for this organization", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (c_ahTabSchemaName IS NOT NULL AND c_ahTabName IS NOT NULL) AND (c_ahTabSchemaName <> c_tgtTabSchemaName OR c_ahTabName <> c_tabName) AND (c_ahClassName <> '" + M01_ACM_IVK.clnExpression.toUpperCase() + "') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_ahTabName) || ' AH WHERE S." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "for GEN- and NL_TEXT-tables: exclude records referring to 'parent records' not relevant for this organization'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (c_isNl = 1) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(REPLACE(c_tabName, '_NL_TEXT', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR." + M01_Globals.g_anOid + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSEIF (c_isGen = 1) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(REPLACE(c_tabName, '_GEN', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR." + M01_Globals.g_anOid + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "for relationship-tables: filter by foreign keys referring to records not relevant for this organization", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_entityType = '" + M01_Globals.gc_acmEntityTypeKeyRel + "' AND c_isNl = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_fltrTxt = NULL;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN PL." + M01_Globals.g_anOrganizationId + " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM " + aliasSchemaName + ".' || RTRIM(PL." + M01_Globals.g_anPdmTableName + ") || ' L WHERE S.' || AL." + M01_Globals.g_anAcmEntityShortName + " || '_OID = L." + M01_Globals.g_anOid + ")' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE WHEN PR." + M01_Globals.g_anOrganizationId + " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM " + aliasSchemaName + ".' || RTRIM(PR." + M01_Globals.g_anPdmTableName + ") || ' R WHERE S.' || AR." + M01_Globals.g_anAcmEntityShortName + " || '_OID = R." + M01_Globals.g_anOid + ")' END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_fltrTxt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " AL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmLeftEntitySection + " = AL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmLeftEntityName + " = AL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.LEFT_" + M01_Globals.g_anAcmEntityType + " = AL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " ALPar");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALPar." + M01_Globals.g_anAcmEntitySection + " = COALESCE(AL." + M01_Globals.g_anAcmOrParEntitySection + ", AL." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALPar." + M01_Globals.g_anAcmEntityName + " = COALESCE(AL." + M01_Globals.g_anAcmOrParEntityName + ", AL." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALPar." + M01_Globals.g_anAcmEntityType + " = AL." + M01_Globals.g_anAcmOrParEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " AR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmRightEntitySection + " = AR." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmRightEntityName + " = AR." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmRightEntityType + " = AR." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " ARPar");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARPar." + M01_Globals.g_anAcmEntitySection + " = COALESCE(AR." + M01_Globals.g_anAcmOrParEntitySection + ", AR." + M01_Globals.g_anAcmEntitySection + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARPar." + M01_Globals.g_anAcmEntityName + " = COALESCE(AR." + M01_Globals.g_anAcmOrParEntityName + ", AR." + M01_Globals.g_anAcmEntityName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARPar." + M01_Globals.g_anAcmEntityType + " = AR." + M01_Globals.g_anAcmOrParEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " LL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anAcmEntitySection + " = ALPar." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anAcmEntityName + " = ALPar." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anAcmEntityType + " = ALPar." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LL." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " LR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anAcmEntitySection + " = ARPar." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anAcmEntityName + " = ARPar." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anAcmEntityType + " = ARPar." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LR." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " PL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PL." + M01_Globals.g_anPdmLdmFkSchemaName + " = LL." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PL." + M01_Globals.g_anPdmLdmFkTableName + " = LL." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(PL." + M01_Globals.g_anOrganizationId + ",v_orgId) = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(PL." + M01_Globals.g_anPoolTypeId + "," + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " PR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PR." + M01_Globals.g_anPdmLdmFkSchemaName + " = LR." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PR." + M01_Globals.g_anPdmLdmFkTableName + " = LR." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(PR." + M01_Globals.g_anOrganizationId + ",v_orgId) = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(PR." + M01_Globals.g_anPoolTypeId + "," + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = c_entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityName + " = c_entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntitySection + " = c_entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(A." + M01_Globals.g_anAcmMaxLeftCardinality + " IS NULL AND A." + M01_Globals.g_anAcmMaxRightCardinality + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(PL." + M01_Globals.g_anOrganizationId + " IS NOT NULL OR PR." + M01_Globals.g_anOrganizationId + " IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_fltrTxt IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = v_stmntTxt || v_fltrTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "apply some table-specific filter", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR filterLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_EntityFilter");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "forGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "forNl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "filter");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

M11_LRT.genProcSectionHeader(fileNo, "dummy-entry - first record", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('-none-', '-none-', 'X', 0, 0, '0=1')");

String qualFuncNameHasAlCountry;
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnGenericCode.toUpperCase()) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M11_LRT.genProcSectionHeader(fileNo, "exclude CODEs by type", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, 'S.CTYTYP_OID <> 128')");
} else if (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnGenericAspect.toUpperCase()) {
qualFuncNameHasAlCountry = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[i].sectionIndex, "HASALCNTRY", ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M11_LRT.genProcSectionHeader(fileNo, "exclude " + M22_Class.g_classes.descriptors[i].className + "s not valid for this organization", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, 'S.ACLACL_OID IS NULL OR (" + qualFuncNameHasAlCountry + "(S." + M01_Globals.g_anOid + ",S." + M01_Globals.g_anCid + ",' || RTRIM(CHAR(orgOid_in)) || ')=1)')");
} else if (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnDecisionTable.toUpperCase()) {
qualFuncNameHasAlCountry = M04_Utilities.genQualFuncName(M22_Class.g_classes.descriptors[i].sectionIndex, "HASALCNTRY", ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M11_LRT.genProcSectionHeader(fileNo, "exclude " + M22_Class.g_classes.descriptors[i].className + "s not valid for this organization", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, '" + qualFuncNameHasAlCountry + "(S." + M01_Globals.g_anOid + ",' || RTRIM(CHAR(orgOid_in)) || ')=1')");
}
if (M22_Class.g_classes.descriptors[i].className.toUpperCase() == M01_ACM_IVK.clnView.toUpperCase()) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M11_LRT.genProcSectionHeader(fileNo, "exclude non SR1/SR0-VIEWS and deletable VIEWS", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, 'UPPER(S." + M01_Globals_IVK.g_anName + ") IN (''SR0'',''SR1'') AND S." + M01_Globals_IVK.g_anIsDeletable + " = 0')");
}
if (M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");

String qualRelTabOrg;
String relOrgEntityIdStr;
qualRelTabOrg = M04_Utilities.genQualTabNameByRelIndex(M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex, ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null, null, null);

String fkAttrToOrg;
String fkAttrToAh;
if (M22_Class.g_classes.descriptors[i].navPathToOrg.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToOrg = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex].leftFkColName[ddlType];
fkAttrToAh = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex].rightFkColName[ddlType];
} else {
fkAttrToOrg = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex].rightFkColName[ddlType];
fkAttrToAh = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToOrg.relRefIndex].leftFkColName[ddlType];
}

M11_LRT.genProcSectionHeader(fileNo, "exclude '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "s not relevant for this organization'", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, 'NOT EXISTS (SELECT 1 FROM " + qualRelTabOrg + " V WHERE V." + fkAttrToAh + " = S." + M01_Globals.g_anOid + ") OR EXISTS (SELECT 1 FROM " + qualRelTabOrg + " V WHERE V." + fkAttrToAh + " = S." + M01_Globals.g_anOid + " AND V." + fkAttrToOrg + " = ' || RTRIM(CHAR(orgOid_in)) || ')')");
}
if (M22_Class.g_classes.descriptors[i].containsIsNotPublishedInclSubClasses &  M22_Class.g_classes.descriptors[i].superClassIndex <= 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION ALL");
M11_LRT.genProcSectionHeader(fileNo, "exclude 'not published " + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "s'", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES('" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "', '" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', 0, 0, 'S." + M01_Globals_IVK.g_anIsNotPublished + " = 0')");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.filter AS c_filter");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_EntityFilter V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.entitySection = c_entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.entityName = c_entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.entityType = c_entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.forGen = c_isGen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.forNl = c_isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_filter = '0=1' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = v_stmntTxt  || ' AND (' || c_filter || ')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "apply Foreign-Key-based filter", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_deleteFltrTxt = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR fkLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_FkCandidates");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "fkColName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isEnforced");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(L." + M01_Globals.g_anAcmOrParEntitySection + ", L." + M01_Globals.g_anAcmEntitySection + ") ELSE COALESCE(R." + M01_Globals.g_anAcmOrParEntitySection + ", R." + M01_Globals.g_anAcmEntitySection + ") END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(L." + M01_Globals.g_anAcmOrParEntityName + ",    L." + M01_Globals.g_anAcmEntityName + ")    ELSE COALESCE(R." + M01_Globals.g_anAcmOrParEntityName + ",    R." + M01_Globals.g_anAcmEntityName + ")    END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN L." + M01_Globals.g_anAcmEntityType + " ELSE R." + M01_Globals.g_anAcmEntityType + " END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(R." + M01_Globals.g_anAcmOrParEntitySection + ", R." + M01_Globals.g_anAcmEntitySection + ") ELSE COALESCE(L." + M01_Globals.g_anAcmOrParEntitySection + ", L." + M01_Globals.g_anAcmEntitySection + ") END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(R." + M01_Globals.g_anAcmOrParEntityName + ",    R." + M01_Globals.g_anAcmEntityName + ")    ELSE COALESCE(L." + M01_Globals.g_anAcmOrParEntityName + ",    L." + M01_Globals.g_anAcmEntityName + ")    END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN R." + M01_Globals.g_anAcmEntityType + " ELSE L." + M01_Globals.g_anAcmEntityType + " END ),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(E." + M01_Globals.g_anAcmAliasShortName + ", E." + M01_Globals.g_anAcmEntityShortName + ") ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN E." + M01_Globals.g_anAcmLrShortName + " ELSE E." + M01_Globals.g_anAcmRlShortName + " END ) || '_OID',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "E." + M01_Globals.g_anAcmIsEnforced);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmLeftEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmLeftEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmLeftEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmRightEntitySection + " = R." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmRightEntityName + " = R." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmRightEntityType + " = R." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(L." + M01_Globals.g_anAcmOrParEntitySection + ", L." + M01_Globals.g_anAcmEntitySection + ") ELSE COALESCE(R." + M01_Globals.g_anAcmOrParEntitySection + ", R." + M01_Globals.g_anAcmEntitySection + ") END) = c_entitySection");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN COALESCE(L." + M01_Globals.g_anAcmOrParEntityName + ", L." + M01_Globals.g_anAcmEntityName + ") ELSE COALESCE(R." + M01_Globals.g_anAcmOrParEntityName + ", R." + M01_Globals.g_anAcmEntityName + ") END) = c_entityName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN E." + M01_Globals.g_anAcmMaxRightCardinality + " = 1 THEN L." + M01_Globals.g_anAcmEntityType + " ELSE R." + M01_Globals.g_anAcmEntityType + " END) = c_entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(E." + M01_Globals.g_anAcmMaxLeftCardinality + ",0) = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE(E." + M01_Globals.g_anAcmMaxRightCardinality + ",0) = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_FksByTab");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "fkColName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isEnforced,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcTabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "srcTabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtTabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "tgtTabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "isSelfReference");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtOrParentEntitySection,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtOrParentEntityName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtEntityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.fkColName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.isEnforced,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_SRC." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_SRC." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_TGT." + M01_Globals.g_anPdmTableName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_TGT." + M01_Globals.g_anPdmFkSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CAST((CASE WHEN P_TGT." + M01_Globals.g_anPdmFkSchemaName + " = P_SRC." + M01_Globals.g_anPdmFkSchemaName + " AND P_TGT." + M01_Globals.g_anPdmTableName + " = P_SRC." + M01_Globals.g_anPdmTableName + " THEN 1 ELSE 0 END) AS " + M01_Globals.g_dbtBoolean + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_FkCandidates E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameLdmTable + " L_SRC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcEntityType = L_SRC." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcOrParentEntityName = L_SRC." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.srcOrParentEntitySection = L_SRC." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmTable + " P_SRC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_SRC." + M01_Globals.g_anLdmTableName + " = P_SRC." + M01_Globals.g_anPdmLdmFkTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_SRC." + M01_Globals.g_anLdmSchemaName + " = P_SRC." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameLdmTable + " L_TGT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtEntityType = L_TGT." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtOrParentEntityName = L_TGT." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E.tgtOrParentEntitySection = L_TGT." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmTable + " P_TGT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_TGT." + M01_Globals.g_anLdmTableName + " = P_TGT." + M01_Globals.g_anPdmLdmFkTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_TGT." + M01_Globals.g_anLdmSchemaName + " = P_TGT." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_SRC." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_TGT." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_TGT." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L_TGT." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(P_SRC." + M01_Globals.g_anPoolTypeId + ", " + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(P_TGT." + M01_Globals.g_anPoolTypeId + ", " + String.valueOf(M01_Globals.g_workDataPoolId) + ") = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_SRC." + M01_Globals.g_anOrganizationId + " = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P_TGT." + M01_Globals.g_anOrganizationId + " = v_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(C.COLNAME AS VARCHAR(10)) AS c_fkColName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.tgtTabSchema                 AS c_tgtTabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.tgtTabName                   AS c_tgtTabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V.isSelfReference              AS c_hasSelfReference");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_FksByTab V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.COLNAME = V.fkColName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.TABNAME = V.srcTabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.TABSCHEMA = V.srcTabSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_isNl = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_isGen = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_hasSelfReference = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_deleteFltrTxt = v_deleteFltrTxt || (CASE WHEN v_deleteFltrTxt = '' THEN '' ELSE ' AND ' END) ||' (T.' || c_fkColName || ' IS NOT NULL AND NOT EXISTS(SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tgtTabName) || ' T2 WHERE T.' || c_fkColName || ' = T2.oid))';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = v_stmntTxt || ' AND (S.' || c_fkColName || ' IS NULL OR EXISTS(SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tgtTabName) || ' T WHERE S.' || c_fkColName || ' = T.oid))';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_stmntTxt IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF (c_entityName <> 'TERM') THEN");
//Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");

//Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
//special term handling
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxtTerm = v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_idx = locate_in_string(v_stmntTxt, 'FROM');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_len = length(v_stmntTxt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmtSinceFrom = substr(v_stmntTxt, v_idx, v_len - v_idx +1);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_insert = 'INSERT INTO SESSION.Termoids(TERMOID, TERMAHOID) SELECT OID, AHOID ' || v_stmtSinceFrom;");
//Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_insert );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_insert;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;");
//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "if table has some 'self-reference' we need to do some specific cleanup", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_hasSelfReference = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "ignore records if they correspond to the same aggregate as other ignored records", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF (c_entityType <> '" + M01_Globals.gc_acmEntityTypeKeyClass + "' OR c_entityName <> c_ahClassName OR c_isGen = 1 OR c_isNl = 1) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "IF (c_entityName = 'TERM') THEN");
//special term handling
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntTxt = 'DELETE FROM Session.Termoids WHERE termAhOid IN (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SELECT DISTINCT S." + M01_Globals.g_anAhOid + " FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S LEFT OUTER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'Session.Termoids T ON S." + M01_Globals.g_anOid + " = T.termOid  WHERE T.termOid IS NULL)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntTxt = 'DELETE FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' WHERE " + M01_Globals.g_anAhOid + " IN (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SELECT DISTINCT S." + M01_Globals.g_anAhOid + " FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S LEFT OUTER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' T ON S." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anOid + " WHERE T." + M01_Globals.g_anOid + " IS NULL)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END IF;");

//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_rowCount = v_rowCount - v_rowCount2;");
//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
// special term handling: now the insert
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "IF (c_entityName = 'TERM') THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntTxt = v_stmntTxtTerm || ' AND OID IN (SELECT TERMOID FROM Session.Termoids)';");
//Print #fileNo, addTab(6); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_rowCount = v_rowCount + v_rowCount2;");
//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "ignore records referring to other ignored records", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF c_entityType = '" + M01_Globals.gc_acmEntityTypeKeyClass + "' AND c_entityName = c_ahClassName AND c_isGen = 0 AND c_isNl = 0 AND v_deleteFltrTxt <> '' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntTxt = 'DELETE FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' T WHERE ' || v_deleteFltrTxt;");

M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;");
//Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_rowCount = v_rowCount - v_rowCount2;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
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


private static void genFactoryTakeOverDdlByOrg3(int srcOrgIndex, int dstOrgIndex, int srcPoolIndex, int dstPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  dstOrgIndex < 1 | srcPoolIndex < 1 | dstPoolIndex < 1)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(dstOrgIndex, ddlType, null, null, null, null);

String qualTabNameViewDst;
qualTabNameViewDst = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

int relIndexDisplaySlot;
relIndexDisplaySlot = M23_Relationship.getRelIndexByName(M01_ACM_IVK.rxnDisplaySlot, M01_ACM_IVK.rnDisplaySlot, null);
String qualTabNameDisplaySlotSrc;
qualTabNameDisplaySlotSrc = M04_Utilities.genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null);
String qualTabNameDisplaySlotDst;
qualTabNameDisplaySlotDst = M04_Utilities.genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null);

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameNsr1ValidForOrganizationDst;
qualTabNameNsr1ValidForOrganizationDst = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexNsr1ValidForOrganization, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null);

String qualTabNameEndSlotDst;
qualTabNameEndSlotDst = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexEndSlot, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameCategory;
qualTabNameCategory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

String qualTabNameCodeCategory;
qualTabNameCodeCategory = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null);
String qualTabNameCodeCategoryLrt;
qualTabNameCodeCategoryLrt = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, dstOrgIndex, dstPoolIndex, true, null, null, null, null, null);

String qualProcNameAssignCodeCat;
qualProcNameAssignCodeCat = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnAssignCodeCat, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcedureNameFtoGetChangelog;
qualProcedureNameFtoGetChangelog = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetChangeLog, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcedureNameFtoInitial;
qualProcedureNameFtoInitial = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoInitial, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcedureNameSetEnp;
qualProcedureNameSetEnp = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexFactoryTakeover, M01_ACM_IVK.spnFtoSetEnp, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcedureNameGetEnpEbpMapping;
qualProcedureNameGetEnpEbpMapping = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexFactoryTakeover, M01_ACM_IVK.spnFtoGetEnpEbpMap, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcedureNameFtoLock;
qualProcedureNameFtoLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM_IVK.spnFtoLock, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

String qualProcNameAssertRebateDefault;
qualProcNameAssertRebateDefault = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnAssertRebateDefault, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    Lock Aggregate Heads for Factory Data Take-Over
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Lock Aggregate Heads for 'Factory Data Take-Over'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameFtoLock);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "OID of the LRT to use for locking");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being locked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameFtoLock, ddlType, null, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "loop over affected aggregate heads", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AffectedAggregateType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aggregateType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT aggregateType FROM " + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT aggregateType FROM " + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_AffectedAggregateType V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.aggregateType = A." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmCondenseData + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(dstPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each aggregate head individually", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnFtoLock.toUpperCase() + "_' || c_tableName || '(?,?,?,?)' ;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "divisionOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameFtoLock, ddlType, null, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Lock Aggregate Heads for Factory Data Take-Over
// ####################################################################################################################

String busKeyAttrListNoFks;
String[] busKeyAttrArrayNoFks;

String qualProcName;
String qualDstTabName;
String qualDstTabNameLrt;

int i;
for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (M22_Class.g_classes.descriptors[i].isAggHead &  M22_Class.g_classes.descriptors[i].isUserTransactional & !M22_Class.g_classes.descriptors[i].condenseData) {
String fkAttrToDiv;
fkAttrToDiv = "";

if (M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex > 0) {
if (M22_Class.g_classes.descriptors[i].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDiv = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[i].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
}

busKeyAttrListNoFks = "";

if (M22_Class.g_classes.descriptors[i].hasBusinessKey) {
busKeyAttrListNoFks = M24_Attribute.getPkAttrListByClassIndex(M22_Class.g_classes.descriptors[i].classIndex, ddlType, null, null, null, true);

M24_Attribute.genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks);
}

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, M01_ACM_IVK.spnFtoLock, null, null, null, null);
qualDstTabName = M04_Utilities.genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);
qualDstTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Locking Records corresponding to Aggregate Head '" + M22_Class.g_classes.descriptors[i].sectionName + "." + M22_Class.g_classes.descriptors[i].className + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "OID of the LRT to use for locking");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being locked");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtEntityIdCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_oidStr", "VARCHAR(30)", "NULL", null, null);
if (!(M22_Class.g_classes.descriptors[i].hasOwnTable)) {
M11_LRT.genVarDecl(fileNo, "v_entityId", M01_Globals.g_dbtEntityId, "'" + M22_Class.g_classes.descriptors[i].classIdStr + "'", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_entityLabel", "VARCHAR(90)", "'" + M04_Utilities.getPrimaryEntityLabelByIndex(M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[i].classIndex) + "'", null, null);
if (!(busKeyAttrListNoFks.compareTo("") == 0)) {
M11_LRT.genVarDecl(fileNo, "v_busKeyValues", "VARCHAR(200)", "NULL", null, null);
int j;
for (int j = M00_Helper.lBound(busKeyAttrArrayNoFks); j <= M00_Helper.uBound(busKeyAttrArrayNoFks); j++) {
M11_LRT.genVarDecl(fileNo, "v_" + busKeyAttrArrayNoFks[j], "VARCHAR(40)", "NULL", null, null);
}
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLWARNING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "lock records in 'public table' of MPC work data pool", null, null);
if (M03_Config.ftoLockSingleObjectProcessing) {

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR oidLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId AS c_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualDstTabName + " AHD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals.g_anOid + " = c_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AHD." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

} else {

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualDstTabName + " AHD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anInLrt + " IS NULL");
if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + fkAttrToDiv.toUpperCase() + " = divisionOid_in");
} else if (M22_Class.g_classes.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.ahObjectId = AHD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNo, "verify that now all records are locked by this LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
if (M22_Class.g_classes.descriptors[i].hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.ahClassId");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
if (M22_Class.g_classes.descriptors[i].hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_entityId");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualDstTabName + " AHD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anOid + " = MCLS.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals.g_anInLrt + " <> lrtOid_in");
if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + fkAttrToDiv.toUpperCase() + " = divisionOid_in");
} else if (M22_Class.g_classes.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHD." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");

M11_LRT.genProcSectionHeader(fileNo, "if there is any row that is locked in some other transaction we need to quit", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_oid IS NOT NULL) THEN");

M11_LRT.genProcSectionHeader(fileNo, "determine entityLabel", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityLabel);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_entityLabel");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityId + " = " + (M22_Class.g_classes.descriptors[i].hasOwnTable ? "'" + M22_Class.g_classes.descriptors[i].classIdStr + "'" : "v_entityId"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE ENL." + M01_Globals.g_anLanguageId + " WHEN " + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + " THEN 0 ELSE ENL." + M01_Globals.g_anLanguageId + " END) ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_entityLabel = RTRIM(LEFT(COALESCE(v_entityLabel, " + "'" + M04_Utilities.getPrimaryEntityLabelByIndex(M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[i].classIndex) + "'), " + String.valueOf(33 - (busKeyAttrListNoFks.compareTo("") == 0 ? 3 : busKeyAttrListNoFks.length())) + "));");

if (!(busKeyAttrListNoFks.compareTo("") == 0)) {
M11_LRT.genProcSectionHeader(fileNo, "determine non-FK values violating business key", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
for (int j = M00_Helper.lBound(busKeyAttrArrayNoFks); j <= M00_Helper.uBound(busKeyAttrArrayNoFks); j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CAST(RTRIM(CAST(" + busKeyAttrArrayNoFks[j].toUpperCase() + " AS CHAR(40))) AS VARCHAR(40))" + (j < M00_Helper.uBound(busKeyAttrArrayNoFks) ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
for (int j = M00_Helper.lBound(busKeyAttrArrayNoFks); j <= M00_Helper.uBound(busKeyAttrArrayNoFks); j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_" + busKeyAttrArrayNoFks[j] + (j < M00_Helper.uBound(busKeyAttrArrayNoFks) ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualDstTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = v_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "concatenate business key values for error message", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_busKeyValues =");
for (int j = M00_Helper.lBound(busKeyAttrArrayNoFks); j <= M00_Helper.uBound(busKeyAttrArrayNoFks); j++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(v_" + busKeyAttrArrayNoFks[j] + ", '" + busKeyAttrArrayNoFks[j] + "=?')" + (j < M00_Helper.uBound(busKeyAttrArrayNoFks) ? " || ',' ||" : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "signal eror message", 2, null);
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNo, 2, busKeyAttrListNoFks, null, null, null, null, null, null, null, null, "v_entityLabel", "v_busKeyValues", null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("ftoLockDetail", fileNo, 2, M01_Globals.g_anOid, null, null, null, null, null, null, null, null, "v_entityLabel", "RTRIM(CHAR(v_oid))", null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualDstTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, "" + M11_LRT.lrtStatusLocked, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M22_Class.g_classes.descriptors[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualDstTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.aggregateType = '" + M22_Class.g_classes.descriptors[i].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, "ACM-class", M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.gc_acmEntityTypeKeyClass, false, false, qualTabNameLrtAffectedEntity, M22_Class.g_classes.descriptors[i].classIdStr, M22_Class.g_classes.descriptors[i].classIdStr, "lrtOid_in", 1, String.valueOf(M11_LRT.lrtStatusLocked), false);

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}

// ####################################################################################################################
// #    Factory Data Take-Over
// ####################################################################################################################

String qualViewName;
qualViewName = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnSetProdAffectedPdmTab, M01_ACM.vsnSetProdAffectedPdmTab, ddlType, null, null, null, null, null, null, null, null, null, null);

boolean simulate;
String procNameSuffix;
for (int j = 1; j <= (M03_Config.supportSimulationSps ? 2 : 1); j++) {
simulate = (j == 2);
procNameSuffix = (simulate ? "sim" : "");

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFactoryTakeOver, ddlType, dstOrgIndex, dstPoolIndex, null, procNameSuffix, null, null);

M22_Class_Utilities.printSectionHeader("SP for 'Factory Data Take-Over'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "endTimestamp_out", "TIMESTAMP", true, "marks the 'end timestamp' for data being taken over");
if (simulate) {
M11_LRT.genProcParm(fileNo, "OUT", "refId_out", "INTEGER", true, "ID used to identify persisted records related to this procedure call");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being taken over");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_filterBySr0Context", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_rebateValueType", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_nsr1ViewOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_MaxSequenceNumber", "SMALLINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_opType", M01_Globals.g_dbtEnumId, "NULL", null, null);
if (!(simulate)) {
M11_LRT.genVarDecl(fileNo, "v_initialFTO", "TIMESTAMP", "NULL", null, null);
}

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, null, null, null, true, null, null, null);
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, true, null, null);

if (simulate) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, dstOrgIndex, dstPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
if (simulate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET refId_out = 0;");
}

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "determine OID of 'my Organization'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_orgOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ID = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_orgOid IS NULL) THEN");

if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdl("noOrg", fileNo, 2, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid = " + M01_Globals_IVK.g_activePsOidDdl + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify lrtOid", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid = " + M01_Globals.g_activeLrtOidDdl + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtCount =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if this transaction does not exist, we need to quit", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lrtCount = 0) THEN");
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_lrtOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that no other factory takeover is running for current ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anOid + " <> v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + " = " + M01_LDM.gc_dbTrue);
// FIXME: USE UNCOMMITTED READ here
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "THEN");
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdl("ftoAlreadyOnPs", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine division OID", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that no other factory takeover is running for current Division", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameProductStructure + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P.PDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOid + " = L." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anOid + " <> v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "THEN");
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdl("ftoAlreadyInDiv", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that active transaction is empty", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anLrtOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.OPID IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + "," + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtCount > 0 THEN");
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdl("ftoLrtNotEmpty", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that there are no uncommitted changes related to active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anLrtOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.OPID IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + "," + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtCount > 0 THEN");
if (simulate) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
}
M79_Err.genSignalDdl("ftoLrtInUse", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine Id of User executing this Take-Over", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U." + M01_Globals.g_anUserId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U." + M01_Globals.g_anOid + " = L.UTROWN_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET endTimestamp_out  = CURRENT TIMESTAMP;");

// ########################################################################

M11_LRT.genProcSectionHeader(fileNo, "Step 1: Verify GeneralSettings and PricePreferences", null, null);
M11_LRT.genProcSectionHeader(fileNo, "make sure that this ProductStructure has a default rebate (for type)", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameAssertRebateDefault + "(v_psOid, 1);");

M11_LRT.genProcSectionHeader(fileNo, "GeneralSettings", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT EXISTS (SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameGeneralSettings + " WHERE " + M01_Globals_IVK.g_anPsOid + " = v_psOid) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexGeneralSettings, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateUser, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conUpdateUser, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conLastCentralDataTransferBegin, "endTimestamp_out", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGeneralSettings, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "PricePreferences", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT EXISTS (SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNamePricePreferences + " WHERE " + M01_Globals_IVK.g_anPsOid + " = v_psOid) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexPricePreferences, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 11, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conVehicleTotalPriceCalculationId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conRebateValueCode, "25", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conRebateValueType, "COALESCE((SELECT VALUETYPE FROM " + M01_Globals_IVK.g_qualTabNameRebateDefault + " WHERE " + M01_Globals_IVK.g_anPsOid + " = v_psOid), 25)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conCurrency, "'EUR'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conCurrencyFactor, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateUser, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conUpdateUser, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conPrimaryPriceTypeForTestId, String.valueOf(M01_LDM_IVK.gc_dfltPrimaryPriceTypeOrg), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 11, M01_ACM_IVK.conPriceSelectionForOverlapId, String.valueOf(M01_LDM_IVK.gc_dfltPriceSelectionForOverlapOrg), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexPricePreferences, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine RebateValueType", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rebateValueType =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anRebateValueType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROWS ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_rebateValueType = COALESCE(v_rebateValueType, 25);");

// ########################################################################
if (!(simulate)) {
M11_LRT.genProcSectionHeader(fileNo, "check if initial factory takeover", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_initialFTO =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MAX(LASTCENTRALDATATRANSFERCOMMIT) ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "Special handling for initial factory takeovers", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_initialFTO IS NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcedureNameFtoInitial + "(?,?,?,?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "endTimestamp_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out  = rowCount_out + v_rowCount;");

//   If Not simulate Then
//Print #fileNo, addTab(1); "CALL DBMS_OUTPUT.PUT_LINE( 'TF' || current timestamp || ' ' || v_stmntTxt || ' called with result: ' ||  v_rowCount);"
//End If
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
}

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Retrieve MPC-related ChangeLog Entries", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameFtoGetChangelog + "(-1, -1, v_filterBySr0Context, endTimestamp_out, rowCount_out);");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Lock Aggregate Heads", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameFtoLock + "(v_lrtOid, v_psOid, v_divisionOid, v_rowCount);");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Calculate ENP-OID Mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameGetEnpEbpMapping + "(v_psOid, v_rowCount);");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "verify again that there are no uncommitted changes related to active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anLrtOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.OPID IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + "," + String.valueOf(M11_LRT.lrtStatusDeleted) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_lrtCount > 0 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("ftoLrtInUse", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Data Take-Over - process each affected table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntitySection + " = A." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityName + " = A." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmSchemaName + " = P." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmTableName + " = P." + M01_Globals.g_anPdmLdmFkTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsNt2m + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCto + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmIsCtp + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals.g_workDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnFactoryTakeOver.toUpperCase() + "_' || c_tableName || '(?,?,?,?)' ;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "delete records implicitly deleted for target organization", null, null);
M11_LRT.genProcSectionHeader(fileNo, "and insert records implicitly created for target organization", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_opType = 3;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_opType IS NOT NULL DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " A,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anAcmEntitySection + " = A." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anAcmEntityName + " = A." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmSchemaName + " = P." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmTableName + " = P." + M01_Globals.g_anPdmLdmFkTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsNt2m + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsCto + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmIsCtp + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anAcmUseFtoPostProcess + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " IN ('" + M01_Globals.gc_acmEntityTypeKeyClass + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(v_opType = 3 OR A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals.g_workDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmFkSequenceNo + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnFtoPostProc.toUpperCase() + "_' || c_tableName || '(?,?,?,?,?)' ;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_opType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_opType = (CASE v_opType WHEN 3 THEN 1 ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Calculate ENPs", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameSetEnp + "(v_rebateValueType, v_rowCount);");

// ########################################################################

M11_LRT.genProcSectionHeader(fileNo, "AssignCodeCat", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.GCO_OID AS v_code,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.CAT_OID AS v_category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategoryLrt + " CCL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCodeCategory + " CC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.OID = CC.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCategory + "	" + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.ISDEFAULT <> 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.PS_OID = CC.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CC.CAT_OID = C.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.INLRT = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCL.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL " + qualProcNameAssignCodeCat + " (?,?,?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_code,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_category");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

if (!(simulate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF; -- v_initialFTO");
}

M11_LRT.genProcSectionHeader(fileNo, "Verify existence of 'StandardViews' for SR0, SR1 and NSR1", null, null);
String viewName;
for (int i = 1; i <= 3; i++) {
viewName = (i == 1 ? "SR0" : (i == 2 ? "SR1" : "NSR1"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT EXISTS (SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNameViewDst + " WHERE " + "RTRIM(UPPER(" + M01_Globals_IVK.g_anName + ")) = '" + viewName + "' AND " + M01_Globals_IVK.g_anPsOid + " = v_psOid) THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameViewDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexView, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
if (i < 3) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexView, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexView, ddlType, srcOrgIndex, srcPoolIndex, null, null, null, null, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(" + M01_Globals_IVK.g_anName + ") = '" + viewName + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conName, "'" + viewName + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conIsStandard, M01_LDM.gc_dbTrue, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexView, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomDefaultValue, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (i < 3) {
M00_FileWriter.printToFile(fileNo, "");
}
}

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Maintain DisplaySlots for Standard Views", null, null);
M11_LRT.genProcSectionHeader(fileNo, "Delete existing Standard Slots", null, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameViewDst + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V." + M01_Globals.g_anOid + " = D.VIW_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(V." + M01_Globals_IVK.g_anName + ") IN ('SR0', 'SR1', 'NSR1')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "Copy DisplaySlots for SR0- and SR1-View from factory", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute.genAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotSrc + " D");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameViewDst + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V." + M01_Globals.g_anOid + " = D.VIW_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "UPPER(V." + M01_Globals_IVK.g_anName + ") IN ('SR0', 'SR1')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "Create DisplaySlots for NSR1-View", null, null);
M11_LRT.genProcSectionHeader(fileNo, "Determine OID of NSR1-View", null, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_nsr1ViewOid = (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameViewDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPPER(" + M01_Globals_IVK.g_anName + ") = 'NSR1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "Copy DisplaySlots of SR1-View for NSR1-View (create new OIDs, point FK to NSR1-View)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, "D.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, "VIW_OID", "v_nsr1ViewOid", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst + " D,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameViewDst + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "D.VIW_OID = V." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPPER(V." + M01_Globals_IVK.g_anName + ") = 'SR1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "Determine MaxSequenceNumber of NSR1-DisplaySlots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_MaxSequenceNumber = COALESCE((");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(" + M01_Globals_IVK.g_anSequenceNumber + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VIW_OID = v_nsr1ViewOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "), 0);");

M11_LRT.genProcSectionHeader(fileNo, "Create DisplaySlots for NSR1-Slots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, null, null, "N.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, "VIW_OID", "v_nsr1ViewOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conSequenceNumber, "v_MaxSequenceNumber + E.NSR1ORDER", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "ESL_OID", "N.ESL_OID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameNsr1ValidForOrganizationDst + " N");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotDst + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "N.ESL_OID = E." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "N." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.NSR1ORDER IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "Determine MaxSequenceNumber of NSR1-DisplaySlots", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_MaxSequenceNumber = COALESCE((");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(" + M01_Globals_IVK.g_anSequenceNumber + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VIW_OID = v_nsr1ViewOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "), 0);");

M11_LRT.genProcSectionHeader(fileNo, "Create DisplaySlot for DUP-Slot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameDisplaySlotDst);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, null, null, "D.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUserName, "v_cdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, "VIW_OID", "v_nsr1ViewOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conSequenceNumber, "v_MaxSequenceNumber + 1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, "ESL_OID", "E." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(relIndexDisplaySlot, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomValueNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameEndSlotDst + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals_IVK.g_anIsDuplicating + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

// ########################################################################
M11_LRT.genProcSectionHeader(fileNo, "Update GeneralSettings", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGeneralSettings);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LASTCENTRALDATATRANSFERBEGIN = endTimestamp_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anLastUpdateTimestamp + " = CURRENT TIMESTAMP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "mark active transaction as 'factory takeover' (should already be done by application)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anIsCentralDataTransfer + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_lrtoid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "determine current set of FTO-CONFLICTs", 1, null);
String qualProcNameGetConflicts;
qualProcNameGetConflicts = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetConflicts, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameGetConflicts + "(?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtoid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (simulate) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, 1, "endTimestamp_out", "refId_out", "rowCount_out", null, null, null, null, null, null, null, null, null);
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, 1, "endTimestamp_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);
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


private static void genFtoConflictSpecLine(int fileNo,  int objClassIndex, String attrName,  long messageId,  int conflictTypeId,  boolean includeColon,  Integer indentW) {
int indent; 
if (indentW == null) {
indent = 6;
} else {
indent = indentW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "('" + M22_Class.g_classes.descriptors[objClassIndex].classIdStr + "', '" + attrName + "', " + String.valueOf(messageId) + ", " + String.valueOf(conflictTypeId) + ")" + (includeColon ? "," : ""));
}


private static void genFactoryTakeOverPriceConflictHandling(int thisOrgIndex, int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 0 |  thisPoolIndex < 0)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameConflict;
qualTabNameConflict = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

// ####################################################################################################################
// #    Factory Data Take-Over: determine prices 'in conflict'
// ####################################################################################################################
String qualPriceConflictProcName;
qualPriceConflictProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetPriceConflicts, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualViewNamePropertyLrtMqt;
qualViewNamePropertyLrtMqt = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexProperty, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, "", null, null);

String qualViewNameGenericCodeLrtMqt;
qualViewNameGenericCodeLrtMqt = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, "", null, null);

String qualViewNameEndSlotGen;
qualViewNameEndSlotGen = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, false, null, "", null, null);

String qualViewNameEndSlotGenNl;
qualViewNameEndSlotGenNl = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, true, null, "", null, null);

String qualTabNamePropertyTemplate;
qualTabNamePropertyTemplate = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPropertyTemplate, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

String qualTabNameGenericAspectLrt;
qualTabNameGenericAspectLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String qualViewNameGenericAspectLrtMqt;
qualViewNameGenericAspectLrtMqt = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, "", null, null);

String qualTabNameUser;
qualTabNameUser = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexUser, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

String qualTabNameOrg;
qualTabNameOrg = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexOrganization, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for determining 'records in price conflict during Factory Data Take-Over'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualPriceConflictProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M25_Domain.getDbDatatypeByDomainIndex(M01_Globals.g_domainIndexOid), true, "OID of the LRT holding the FTO-data");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records identified as 'being in conflict'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_isCentralDatatransfer", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_endtime", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_elpId", "INTEGER", "106", null, null);
M11_LRT.genVarDecl(fileNo, "v_enpId", "INTEGER", "107", null, null);
M11_LRT.genVarDecl(fileNo, "v_tireOmissionElpId", "INTEGER", "144", null, null);
M11_LRT.genVarDecl(fileNo, "v_tireOmissionEnpId", "INTEGER", "145", null, null);
M11_LRT.genVarDecl(fileNo, "v_priceTemplateId", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tireOmissionPriceTemplate", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_langIdUser", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_primLangIdOrg", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_secLangIdOrg", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_oidOfinvalidPrice", "BIGINT", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for PriceConflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictPrice);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRICE_OID                 " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRICE_COUNT               " + M01_Globals.g_dbtInteger + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRICE_LRTSTATE            " + M01_Globals.g_dbtInteger + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OMISSIONPRICE_OID         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OMISSIONPRICE_COUNT       " + M01_Globals.g_dbtInteger + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OMISSIONPRICE_LRTSTATE    " + M01_Globals.g_dbtInteger + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NATIONALPRICE_OID         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NATIONALOMISSIONPRICE_OID " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEADINGCODE_OID           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEADINGCODE_NUMBER        VARCHAR(320),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEADINGSLOT_OID           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEADINGSLOT_STRING        VARCHAR(320)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Slot OID and label mapping", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT_OID    " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SLOT_STRING VARCHAR(320)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CREATE INDEX " + M01_Globals_IVK.gc_tempTabNameConflictPrice + "_OID_INDEX ON " + M01_Globals_IVK.gc_tempTabNameConflictPrice + " (LEADINGSLOT_OID) COLLECT STATISTICS;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualPriceConflictProcName, ddlType, 1, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "verify that LRT corresponds to FTO and is consistent to 'current ProductStructure'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PDIDIV_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U." + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anEndTime);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_isCentralDatatransfer,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_endtime");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anPsOid + " = P." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_psOid IS NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not exist", 2, true);
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_psOid <> " + M01_Globals_IVK.g_activePsOidDdl + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not match current PS", 2, true);
M79_Err.genSignalDdl("incorrPsTag", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_endTime IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT is already closed", 2, true);
M79_Err.genSignalDdl("lrtClosed", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_isCentralDatatransfer <> 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not refer to FTO", 2, true);
M79_Err.genSignalDdlWithParms("lrtNotFto", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo,  + qualTabNameGenericAspectLrt + , null, null);
M11_LRT.genProcSectionHeader(fileNo, "If price conflict determination ist to be done according to price preferences of the current organization then ...", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF 1 = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ISCONFLICTDETERMFORPRICES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "Group codePriceAssignments and paintZonePriceAssignments, that are inserted, updated or deleted in the current LRT, and which are of ELP or TireOmissionELP, resp. ENP or TireOmissionENP, depending", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "on flag isEnpBasedForNP in price preferences of the current organization", 2, true);
M11_LRT.genProcSectionHeader(fileNo, "Read flag isEnpBasedForNP in price preferences", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF 0 = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ISENPBASEDFORNP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNamePricePreferences);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_priceTemplateId  = v_elpId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_tireOmissionPriceTemplate = v_tireOmissionElpId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_priceTemplateId  = v_enpId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_tireOmissionPriceTemplate = v_tireOmissionEnpId;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "Write into Conflict SessionTable", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "Group by SR0-Kontext (String-Representation), leading code, leading slot, with, with not,", 2, true);
M11_LRT.genProcSectionHeader(fileNo, " allowed countries, disallowed countries, validTo, validFrom", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRICE_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRICE_COUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRICE_LRTSTATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OMISSIONPRICE_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OMISSIONPRICE_COUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OMISSIONPRICE_LRTSTATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGCODE_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT_OID     ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_priceTemplateId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS PRICE_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SUM(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_priceTemplateId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS PRICE_COUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_priceTemplateId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "S.LRTSTATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS PRICE_LRTSTATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_tireOmissionPriceTemplate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS OMISSIONPRICE_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SUM(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_tireOmissionPriceTemplate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS OMISSIONPRICE_COUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MAX(CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PT.ID = v_tireOmissionPriceTemplate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "S.LRTSTATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END) AS OMISSIONPRIC_LRTSTATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BCDBCD_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BESESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNamePropertyLrtMqt + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.PRPAPR_OID = P.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNamePropertyTemplate + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P.PTMHTP_OID = PT.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.CLASSID IN ('09031', '09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "( PT.ID = v_priceTemplateId OR PT.ID = v_tireOmissionPriceTemplate )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.SR0CONTEXT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.BCDBCD_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.BESESL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.WITEXP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.WINEXP_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.ACLACL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.DCLDCL_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.VALIDFROM,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.VALIDTO,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "S.LRTSTATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");


M11_LRT.genProcSectionHeader(fileNo, "take over slot oids to slot mapping table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SLOT_OID ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DISTINCT LEADINGSLOT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT_OID IS NOT NULL;");


M11_LRT.genProcSectionHeader(fileNo, "Check Consistency: Only max 1 ELN(ENP) and only max 1 OmitELN(ENP)", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN PRICE_COUNT > 1 THEN PRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE OMISSIONPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_oidOfinvalidPrice");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRICE_COUNT > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OMISSIONPRICE_COUNT > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_oidOfinvalidPrice IS NOT NULL THEN");
M79_Err.genSignalDdlWithParms("priceGrpNotValid", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_oidOfinvalidPrice))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "For each central price and central tire omission price check for a national price reference and national tire omission price reference", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NATIONALPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NP.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameGenericAspectLrtMqt + " NP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NP.CCPCCP_OID = C.PRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.PRICE_LRTSTATE IN (2, 3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NATIONALOMISSIONPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NO.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameGenericAspectLrtMqt + " NO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NO.CCPCCP_OID = C.OMISSIONPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "C.OMISSIONPRICE_LRTSTATE IN (2, 3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");


M11_LRT.genProcSectionHeader(fileNo, "Pricechanges / Deletions without national Prices are irrelevant", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRICE_LRTSTATE IN (2, 3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NATIONALPRICE_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NATIONALOMISSIONPRICE_OID IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "Enrich session table with CodeNumbers", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGCODE_NUMBER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GC.CODENUMBER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameGenericCodeLrtMqt + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GC.OID = C.LEADINGCODE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGCODE_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGCODE_NUMBER IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "Enrich slot oid to string mapping table with NlStrings", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "Step 1 - For dataLanguage@User", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_langIdUser = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATALANGUAGE_ID ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameUser);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CDUSERID = v_lrtCdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_langIdUser IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE ESGNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGen + " ESG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGenNl + " ESGNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.OID = ESGNL.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.ESL_OID = C.SLOT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESGNL.LANGUAGE_ID = v_langIdUser");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "Enrich slot oid to string mapping table with NlStrings", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "Step 2 - For primaryLanguage@Org", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_primLangIdOrg = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PRIMARYLANGUAGE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OID = ( SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + M01_Globals.g_qualTabNamePdmOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "ID=" + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_primLangIdOrg IS NOT NULL THEN ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE ESGNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGen + " ESG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGenNl + " ESGNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.OID = ESGNL.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.ESL_OID = C.SLOT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESGNL.LANGUAGE_ID = v_primLangIdOrg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "Enrich slot oid to string mapping table with NlStrings", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "Step 3 - For secondaryLanguage@Org", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_secLangIdOrg = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FALLBACKLANGUAGE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OID = ( SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + M01_Globals.g_qualTabNamePdmOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "ID=" + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_secLangIdOrg IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE ESGNL.LABEL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGen + " ESG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewNameEndSlotGenNl + " ESGNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.OID = ESGNL.ESL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESG.ESL_OID = C.SLOT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ESGNL.LANGUAGE_ID = v_secLangIdOrg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT_STRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SLOT_STRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameConflictSlotNames + " SN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SN.SLOT_OID = C.LEADINGSLOT_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + " )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT_OID IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT_STRING IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "Insert sessionTable entries into Conflict table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLASSID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CONFLICTTYPE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CONFLICTSTATE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MESSAGEID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MESSAGEARGUMENT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGCODE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEADINGSLOT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FAPPRA_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FTPPRA_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NAPPRA_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NTPPRA_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLRLRT_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CREATEUSER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CREATETIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATEUSER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LASTUPDATETIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VERSIONID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEXTVAL FOR " + qualSeqNameOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'27010',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.PRICE_LRTSTATE=1 OR SC.OMISSIONPRICE_LRTSTATE=1 THEN 9");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.PRICE_LRTSTATE=2 OR SC.OMISSIONPRICE_LRTSTATE=2 THEN 10");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.PRICE_LRTSTATE=3 OR SC.OMISSIONPRICE_LRTSTATE=3 THEN 11");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1300029,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TRIM(SUBSTR(SC.LEADINGCODE_NUMBER,1,15)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TRIM(SUBSTR(SC.LEADINGSLOT_STRING,1,300)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.PRICE_OID = 0 THEN NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE SC.PRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.OMISSIONPRICE_OID = 0 THEN NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE SC.OMISSIONPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.NATIONALPRICE_OID = 0 THEN NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE SC.NATIONALPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN SC.NATIONALOMISSIONPRICE_OID = 0 THEN NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE SC.NATIONALOMISSIONPRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtCdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_lrtCdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameConflictPrice + " SC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + " not exists (SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "CLRLRT_OID = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "(FAPPRA_OID = SC.PRICE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + "FTPPRA_OID = SC.OMISSIONPRICE_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;  ");

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

private static void genFactoryTakeOverExtendedConflictHandling(int thisOrgIndex, int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 0 |  thisPoolIndex < 0)) {
// Factory-Take-Over is only supported at 'pool-level'
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameConflict;
qualTabNameConflict = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

// ####################################################################################################################
// #    Factory Data Take-Over: determine records 'in conflict'
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetConflicts, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualPriceConflictProcName;
qualPriceConflictProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnFtoGetPriceConflicts, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for determining 'records in conflict during Factory Data Take-Over'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M25_Domain.getDbDatatypeByDomainIndex(M01_Globals.g_domainIndexOid), true, "OID of the LRT holding the FTO-data");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records identified as 'being in conflict'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtCdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_isCentralDatatransfer", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_endtime", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_generalPriceConflict", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(500)", "NULL", null, null);

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Conflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "conflictClassId  " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId          " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName         CHAR(60),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl             " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "conflictType_Id  " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "messageId        BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "messageArgument  VARCHAR(1000),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid          " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "canOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcgOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cgnOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid           " + M01_Globals.g_dbtOid);
//  SM Änderung PriceConflict: hier müssen noch die neuen Felder eingetragen werden - wird von RS realisiert
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for potentially multiple GenericAspect Conflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictMultiGa);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId          " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExclusionFormulaFactory " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrInclusionFormulaFactory " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrConclusionFactory       " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrNumValue                " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrValueGathering          " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrBoolValue               " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExpression              " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl             " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid          " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid           " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for potentially multiple SRValidity Conflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictMultiSr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId          " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType1   " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType2   " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExtTypeDesc  " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelDrive   " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelWheelBase " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSr1Context   " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl             " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid          " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid           " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for potentially multiple GenericAspectNlText Conflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictMultiGaNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId          " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrDescription  " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrTextValue    " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl             " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid           " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for potentially multiple GenericCodeNlText Conflicts", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflictMultiCdNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "conflictClassId  " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId          " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId         " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrLabel        " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSortingCriterion      " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrICodeShortDescription " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen            " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl             " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid           " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid           " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, true, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, 1, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M11_LRT.genProcSectionHeader(fileNo, "verify that LRT corresponds to FTO and is consistent to 'current ProductStructure'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PDIDIV_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "U." + M01_Globals.g_anUserId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anIsCentralDataTransfer + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anEndTime);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtCdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_isCentralDatatransfer,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_endtime");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals_IVK.g_anPsOid + " = P." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "L." + M01_Globals.g_anOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_psOid IS NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not exist", 2, true);
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_psOid <> " + M01_Globals_IVK.g_activePsOidDdl + " THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not match current PS", 2, true);
M79_Err.genSignalDdl("incorrPsTag", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_endTime IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT is already closed", 2, true);
M79_Err.genSignalDdl("lrtClosed", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_isCentralDatatransfer <> 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "LRT does not refer to FTO", 2, true);
M79_Err.genSignalDdlWithParms("lrtNotFto", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspectLrt;
qualTabNameGenericAspectLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String qualTabNameGenericAspectNlText;
qualTabNameGenericAspectNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspectNlTextLrt;
qualTabNameGenericAspectNlTextLrt = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String qualTabNameExpression;
qualTabNameExpression = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCodeLrt;
qualTabNameGenericCodeLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

String qualTabNameGenericCodeNlText;
qualTabNameGenericCodeNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCodeNlTextLrt;
qualTabNameGenericCodeNlTextLrt = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, true, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine update-records in '" + qualTabNameGenericAspectLrt + "' causing conflict", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGa");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExclusionFormulaFactory,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrInclusionFormulaFactory,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrConclusionFactory,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrNumValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrValueGathering,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrBoolValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExpression,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "plrOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.CLASSID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.EFNEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.EFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.EFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.IFNEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.IFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.IFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.CONEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.COFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.COFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.NUMVALUE_NATIONAL IS NOT NULL) AND (COALESCE(CHAR(S.NUMVALUE), '#') <> COALESCE(CHAR(T.NUMVALUE), '#'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.VALUEGATHERING_NATIONAL_ID IS NOT NULL) AND (COALESCE(CHAR(S.VALUEGATHERING_ID), '#') <> COALESCE(CHAR(T.VALUEGATHERING_ID), '#'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.BOOLVALUE_ISNATACTIVE = 1) AND (S.BOOLVALUE <> T.BOOLVALUE)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.VALEXP_OID_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.VALEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.VALEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN S.CLASSID = '09005' THEN S.E1VEX1_OID ELSE CAST(NULL AS BIGINT) END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN S.CLASSID = '09004' THEN S.OID ELSE CAST(NULL AS BIGINT) END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN S.CLASSID = '09025' THEN S.OID ELSE CAST(NULL AS BIGINT) END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN S.CLASSID IN ('09016','09017','09018','09019','09021','09022','09023','09024') THEN S.OID ELSE CAST(NULL AS BIGINT) END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN S.CLASSID IN ( '09013' , '09014' ) THEN S.OID ELSE CAST(NULL AS BIGINT) END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspect + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'exclusionFormulaFactory@CodePlausibilityRule'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.EFNEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.EFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.EFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'inclusionFormulaFactory@CodePlausibilityRule'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.IFNEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.IFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.IFFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'conclusionFactory@SlotPlausibilityRule'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.CONEXP_OID IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.COFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.COFEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'numValue@SlotNumericPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.NUMVALUE_NATIONAL IS NOT NULL) AND (COALESCE(CHAR(S.NUMVALUE), '#') <> COALESCE(CHAR(T.NUMVALUE), '#'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'valueGathering@SlotNumericPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.VALUEGATHERING_NATIONAL_ID IS NOT NULL) AND (COALESCE(CHAR(S.VALUEGATHERING_ID), '#') <> COALESCE(CHAR(T.VALUEGATHERING_ID), '#'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'boolValue@SlotBooleanPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.BOOLVALUE_ISNATACTIVE = 1) AND (S.BOOLVALUE <> T.BOOLVALUE)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'expression@SlotTextPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.VALEXP_OID_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = T.VALEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "<>");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM " + qualTabNameExpression + " X WHERE X.OID = S.VALEXP_OID), 'not available')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M87_FactoryTakeOver.genInsertSessionConflictMultiGa(fileNo);

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "determin whether 'general price conflict record' applies", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLASSID IN ('09031','09032','09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_generalPriceConflict = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "determine whether update of some nationalized SR1Validity-attribute causes conflict for NSR1", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExtTypeDesc,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelDrive,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelWheelBase,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'09004',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.MODELTYPE1, '') <> COALESCE(SR1_M.MODELTYPE1, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.MODELTYPE2, '') <> COALESCE(SR1_M.MODELTYPE2, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.EXTTYPEDESC, '') <> COALESCE(SR1_M.EXTTYPEDESC, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.MODELDRIVE, '') <> COALESCE(SR1_M.MODELDRIVE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.MODELWHEELBASE, '') <> COALESCE(SR1_M.MODELWHEELBASE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_F.SR1CONTEXT, '') <> COALESCE(SR1_M.SR1CONTEXT, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectLrt + " SR1_F");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspect + " SR1_M");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.OID = SR1_M.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspect + " NSR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.E1VEX1_OID = SR1_M.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_M.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.CLASSID = '09004'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_F.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.MODELTYPE1, '') <> COALESCE(SR1_M.MODELTYPE1, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.MODELTYPE2, '') <> COALESCE(SR1_M.MODELTYPE2, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.EXTTYPEDESC, '') <> COALESCE(SR1_M.EXTTYPEDESC, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.MODELDRIVE, '') <> COALESCE(SR1_M.MODELDRIVE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.MODELWHEELBASE, '') <> COALESCE(SR1_M.MODELWHEELBASE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_F.SR1CONTEXT, '') <> COALESCE(SR1_M.SR1CONTEXT, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "Split conflicts to seperate rows and insert to common session table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Conflict");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'modelType1',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType1 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'modelType2',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelType2 = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'extTypeDesc',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrExtTypeDesc = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'modelDrive',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelDrive = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'modelWheelBase',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrModelWheelBase = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'sr1Context',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiSr");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSr1Context = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "determine update-records in '" + qualTabNameGenericAspectNlTextLrt + "' causing conflict", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGaNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrDescription,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrTextValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.CLASSID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.DESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.DESCRIPTION, '') <> COALESCE(T.DESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.TEXTVALUE_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.TEXTVALUE, '') <> COALESCE(T.TEXTVALUE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlTextLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlText + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectLrt + " SAH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.GAS_OID = SAH.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'description@PropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.DESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.DESCRIPTION, '') <> COALESCE(T.DESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'textValue@SlotTextPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.TEXTVALUE_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.TEXTVALUE, '') <> COALESCE(T.TEXTVALUE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
//TF: CQDAT00027123: the record with lrtstate = 1 contains the merged entries
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGaNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrDescription,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrTextValue,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.CLASSID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(S.DESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.DESCRIPTION_NATIONAL, '') <> COALESCE(S.DESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(S.TEXTVALUE_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.TEXTVALUE_NATIONAL, '') <> COALESCE(S.TEXTVALUE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlTextLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectLrt + " SAH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.GAS_OID = SAH.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.LRTSTATE = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'description@PropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.DESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.DESCRIPTION_NATIONAL, '') <> COALESCE(S.DESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'textValue@SlotTextPropertyAssignment'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.TEXTVALUE_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.TEXTVALUE_NATIONAL, '') <> COALESCE(S.TEXTVALUE, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "Split conflicts to seperate rows and insert to common session table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Conflict");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'description',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGaNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrDescription = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'textValue',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "prpOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiGaNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrTextValue = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "determin whether 'general price conflict record' applies", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspectLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLASSID IN ('09031','09032','09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_generalPriceConflict = 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "determine whether update of some nationalized SR1Validity-attribute causes conflict for NSR1", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Conflict");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nsr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "sr1Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "nanOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "canOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'09004',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.GAS_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(SR1_FNL.MODELNAME, '') <> COALESCE(SR1_MNL.MODELNAME, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN 'modelName'");
M11_LRT.genProcSectionHeader(fileNo, "this is never reached", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 'unknown'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.GAS_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1_MNL.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlTextLrt + " SR1_FNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlText + " SR1_MNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.OID = SR1_MNL.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspect + " NSR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.E1VEX1_OID = SR1_MNL.GAS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectNlText + " NSR1_MNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.OID = NSR1_MNL.GAS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1_MNL.LANGUAGE_ID = SR1_MNL.LANGUAGE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NSR1.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_MNL.PS_OID = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SR1_FNL.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(COALESCE(SR1_FNL.MODELNAME, '') <> COALESCE(SR1_MNL.MODELNAME, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "determine update-records in '" + qualTabNameGenericCodeNlTextLrt + "' causing conflict", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiCdNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSortingCriterion,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrICodeShortDescription,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'05006',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.LABEL_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.LABEL, '') <> COALESCE(T.LABEL, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.SORTINGCRITERION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.SORTINGCRITERION, '') <> COALESCE(T.SORTINGCRITERION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(T.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.ICODESHORTDESCRIPTION, '') <> COALESCE(T.ICODESHORTDESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeNlTextLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeNlText + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeLrt + " SAH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.GCO_OID = SAH.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.LRTSTATE = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'label@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.LABEL_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.LABEL, '') <> COALESCE(T.LABEL, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'sortingCriterion@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.SORTINGCRITERION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.SORTINGCRITERION, '') <> COALESCE(T.SORTINGCRITERION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'iCodeShortDescription@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(T.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.ICODESHORTDESCRIPTION, '') <> COALESCE(T.ICODESHORTDESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
//TF: CQDAT00027123: the record with lrtstate = 1 contains the merged entries
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiCdNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrLabel,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSortingCriterion,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrICodeShortDescription,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'05006',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.AHOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(S.LABEL_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.LABEL_NATIONAL, '') <> COALESCE(S.LABEL, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(S.SORTINGCRITERION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.SORTINGCRITERION_NATIONAL, '') <> COALESCE(S.SORTINGCRITERION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(S.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(COALESCE(S.ICODESHORTDESCRIPTION_NATIONAL, '') <> COALESCE(S.ICODESHORTDESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "THEN 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAH.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeNlTextLrt + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeLrt + " SAH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.GCO_OID = SAH.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.INLRT = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.LRTSTATE = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'label@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.LABEL_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.LABEL_NATIONAL, '') <> COALESCE(S.LABEL, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'sortingCriterion@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.SORTINGCRITERION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.SORTINGCRITERION_NATIONAL, '') <> COALESCE(S.SORTINGCRITERION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M11_LRT.genProcSectionHeader(fileNo, "check attribute 'iCodeShortDescription@GenericCode'", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(S.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(COALESCE(S.ICODESHORTDESCRIPTION_NATIONAL, '') <> COALESCE(S.ICODESHORTDESCRIPTION, ''))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "Split conflicts to seperate rows and insert to common session table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.Conflict");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'label',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiCdNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrLabel = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'sortingCriterion',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiCdNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrSortingCriterion = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'iCodeShortDescription',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "gcoOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cnlOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isGen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "isNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.ConflictMultiCdNl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "attrICodeShortDescription = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

// newRS end

M11_LRT.genProcSectionHeader(fileNo, "determine message IDs and types of conflicts", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflict + " TC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "messageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "conflictClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "conflictType_Id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictDetails.messageId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictType.classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictType.typeId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeNSr1) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypeConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeGeneralPrice) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGeneralPriceConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeCodeLabel) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodeLabelConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeTypeLabel) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypeLabelConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypePlausibilityRule) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexPlausibilityRuleConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCodePropertyAssignmentConflict].classIdStr + "'),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + "	" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment) + ", '" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexSlotPropertyAssignmentConflict].classIdStr + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") V_ConflictType ( typeId, classId )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VALUES");
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodePlausibilityRule, "inclusionFormulaFactory", 1300041, M01_Globals_IVK.gc_ftoConflictTypePlausibilityRule, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodePlausibilityRule, "exclusionFormulaFactory", 1300042, M01_Globals_IVK.gc_ftoConflictTypePlausibilityRule, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotPlausibilityRule, "conclusionFactory", 1300043, M01_Globals_IVK.gc_ftoConflictTypePlausibilityRule, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexGenericCode, "label ", 1300030, M01_Globals_IVK.gc_ftoConflictTypeCodeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexGenericCode, "sortingCriterion", 1300031, M01_Globals_IVK.gc_ftoConflictTypeCodeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexGenericCode, "iCodeShortDescription", 1300032, M01_Globals_IVK.gc_ftoConflictTypeCodeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "modelName", 1300035, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "sr1Context", 1300036, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "modelType1", 1300037, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "modelType2", 1300037, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "extTypeDesc", 1300038, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "modeDrive", 1300039, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSr1Validity, "modelWheelBase", 1300040, M01_Globals_IVK.gc_ftoConflictTypeTypeLabel, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeBinaryPropertyAssignment, "description", 1300011, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeBooleanPropertyAssignment, "description", 1300012, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeNumericPropertyAssignment, "description", 1300013, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeTextPropertyAssignment, "description", 1300014, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeBooleanPropertyAssignment, "boolValue", 1300015, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeNumericPropertyAssignment, "numValue", 1300016, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeNumericPropertyAssignment, "valueGathering", 1300017, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeTextPropertyAssignment, "textValue", 1300018, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexCodeTextPropertyAssignment, "expression", 1300019, M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotBinaryPropertyAssignment, "description", 1300020, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotBooleanPropertyAssignment, "description", 1300021, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotNumericPropertyAssignment, "description", 1300022, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotNumericPropertyAssignment, "description", 1300022, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotTextPropertyAssignment, "description", 1300023, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotBooleanPropertyAssignment, "boolValue", 1300024, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotNumericPropertyAssignment, "numValue", 1300025, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotNumericPropertyAssignment, "valueGathering", 1300026, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotTextPropertyAssignment, "textValue", 1300027, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, true, null);
genFtoConflictSpecLine(fileNo, M01_Globals_IVK.g_classIndexSlotTextPropertyAssignment, "expression", 1300028, M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment, false, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") V_ConflictDetails ( classId, attributeName, messageId, conflictTypeId )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictType.typeId = V_ConflictDetails.conflictTypeId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictDetails.classId = TC.classId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ConflictDetails.attributeName = TC.attrName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete 'previous set of open conflict records'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLRLRT_OID = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conConflictStateId + " = " + String.valueOf(M01_Globals_IVK.gc_ftoConflictStateOpen));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anConflictTypeId + " IN (" + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeNSr1) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeCodeLabel) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeGeneralPrice) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeCodePropertyAssignment) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeSlotPropertyAssignment) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypePlausibilityRule) + ", " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeTypeLabel) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "ignore current conflict records which are marked as 'resolved'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflict + " TC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameConflict + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_ACM_IVK.conConflictStateId + " = " + String.valueOf(M01_Globals_IVK.gc_ftoConflictStateResolved));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anConflictTypeId + " = TC." + M01_Globals_IVK.g_anConflictTypeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anMessageId + " = TC.messageId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TC.objectId = COALESCE(C.SR1SR1_OID, C.SEQSEQ_OID, C.PRPPRP_OID, C.PLRPLR_OID, C.NANCNL_OID, C.CNLCNL_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CLRLRT_OID = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "set '" + M01_Globals_IVK.g_anHasConflict + "-flag' for remaining records (loop over involved tables)", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR ahTabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ClassIds (classId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "( SELECT DISTINCT classId FROM " + M01_Globals_IVK.gc_tempTabNameConflict + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E_AH." + M01_Globals.g_anAcmEntityId + " AS c_ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_pdmSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_pdmTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E_AH." + M01_Globals_IVK.g_anAcmIsPs + " AS c_IsPs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ClassIds C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAcmEntityId + " = C.classId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " E_AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E_AH." + M01_Globals.g_anAcmEntityId + " = E." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E_AH." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityType + " = E_AH." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntitySection + " = E_AH." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anAcmEntityName + " = E_AH." + M01_Globals.g_anAcmEntityName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmSchemaName + " = P." + M01_Globals.g_anPdmLdmFkSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmTableName + " = P." + M01_Globals.g_anPdmLdmFkTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "reset '" + M01_Globals_IVK.g_anHasConflict + "' for all records having no DB-related conflict (i.e. only SOLVER-related conflics)", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'UPDATE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_pdmSchemaName || '.' || c_pdmTableName || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SET ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals_IVK.g_anHasConflict + " = 0 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals_IVK.g_anHasConflict + " = 1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals.g_anInLrt + " = ' || lrtOid_in || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_IsPs = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals_IVK.g_anPsOid + " = ' || v_psOid || ' '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T.CDIDIV_OID = ' || v_divOid || ' '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'NOT EXISTS(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'" + qualTabNameConflict + " C ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE  ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'T." + M01_Globals.g_anOid + " = COALESCE(C.SR1SR1_OID, C.SEQSEQ_OID, C.PRPPRP_OID, C.PLRPLR_OID, C.NANCNL_OID, C.CNLCNL_OID) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'LEFT(CHAR(C." + M01_Globals_IVK.g_anMessageId + "), 2) = ''13''' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'C.CLRLRT_OID = ' || lrtOid_in ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE(v_stmntTxt);");

M11_LRT.genProcSectionHeader(fileNo, "prepare update-statement for this table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_IsPs = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'UPDATE ' || c_pdmSchemaName || '.' || c_pdmTableName || ' SET " + M01_Globals_IVK.g_anHasConflict + " = 1 WHERE " + M01_Globals.g_anOid + " = ? AND " + M01_Globals.g_anInLrt + " = ' || lrtOid_in || ' AND " + M01_Globals_IVK.g_anPsOid + " = ' || v_psOid ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'UPDATE ' || c_pdmSchemaName || '.' || c_pdmTableName || ' SET " + M01_Globals_IVK.g_anHasConflict + " = 1 WHERE " + M01_Globals.g_anOid + " = ? AND " + M01_Globals.g_anInLrt + " = ' || lrtOid_in || ' AND CDIDIV_OID = ' || v_divOid ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "loop over involved objects in this table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR oidLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TC.objectId AS c_objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameConflict + " TC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TC.classId = E." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " E_AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E_AH." + M01_Globals.g_anAcmEntityId + " = E." + M01_Globals.g_anAhCid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E_AH." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E_AH." + M01_Globals.g_anAcmEntityId + " = c_ahClassId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "update this record", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE v_stmnt USING c_objectId;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "persist current set of conflict records", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.AttributeListTransformation transformationConflict;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformationConflict, 9, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 1, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 2, M01_ACM.conLastUpdateTimestamp, null, "", null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 3, "CBVCBM_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 4, "LEADINGCODE", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 5, "LEADINGSLOT", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 6, "FAPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 7, "FTPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 8, "NAPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 9, "NTPPRA_OID", "", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexConflict, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformationConflict, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformationConflict, 30, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 1, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 2, M01_ACM.conLastUpdateTimestamp, null, "", null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 3, "CBVCBM_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 4, "LEADINGCODE", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 5, "LEADINGSLOT", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 6, "FAPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 7, "FTPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 8, "NAPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 9, "NTPPRA_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 10, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 11, M01_ACM.conClassId, "conflictClassId  ", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 12, M01_ACM_IVK.conConflictTypeId, "conflictType_ID", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 13, M01_ACM_IVK.conConflictStateId, String.valueOf(M01_Globals_IVK.gc_ftoConflictStateOpen), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 14, M01_ACM_IVK.conMessageId, "messageId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 15, "CLRLRT_OID", "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 16, "NR1NS1_OID", "nsr1Oid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 17, "SR1SR1_OID", "sr1Oid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 18, "SEQSEQ_OID", "seqOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 19, "CANCNL_OID", "canOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 20, "NANCNL_OID", "nanOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 21, "CCOCOD_OID", "gcoOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 22, "CNLCNL_OID", "cnlOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 23, "PRPPRP_OID", "prpOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 24, "PLRPLR_OID", "plrOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 25, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 26, M01_ACM.conCreateUser, "v_lrtCdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 27, M01_ACM.conCreateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 28, M01_ACM.conUpdateUser, "v_lrtCdUserId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 29, M01_ACM.conLastUpdateTimestamp, "CURRENT TIMESTAMP", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformationConflict, 30, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexConflict, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformationConflict, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "conflictClassId IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "create 'general price conflict' if required", null, null);
M11_LRT.genProcSectionHeader(fileNo, "and call subroutine for detailed price conflicts", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_generalPriceConflict = 1 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF NOT EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLRLRT_OID = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anConflictTypeId + " = " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeGeneralPrice));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anConflictTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_ACM_IVK.conConflictStateId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anMessageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLRLRT_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCreateUser + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anCreateTimestamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NEXTVAL FOR " + qualSeqNameOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGeneralPriceConflict].classIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeGeneralPrice) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + String.valueOf(M01_Globals_IVK.gc_ftoConflictStateOpen) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1300029,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "lrtOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtCdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + 1;");
M11_LRT.genProcSectionHeader(fileNo, "call subroutine for detailed price conflicts", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + qualPriceConflictProcName + "(?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameConflict);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anConflictTypeId + " = " + String.valueOf(M01_Globals_IVK.gc_ftoConflictTypeGeneralPrice));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLRLRT_OID = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, 1, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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
public static void genFtoSupportSpsForEntity(int acmEntityIndex, Integer acmEntityType, int srcOrgIndex, int srcPoolIndex, int dstOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

String sectionName;
String acmEntityName;
String acmEntityShortName;
String entityTypeDescr;
boolean isUserTransactional;
boolean isPsTagged;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
String aggHeadIdStr;
int aggHeadClassIndex;
String dbAcmEntityType;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
boolean isDeletable;
boolean hasNoIdentity;
boolean ignoreForChangelog;
boolean hasNlAttributes;
boolean hasNlAttributesInGen;
boolean useMqtToImplementLrtForEntity;
M22_Class_Utilities.NavPathFromClassToClass aggHeadNavPathToOrg;
String subClassIdStrList;
String aggHeadSubClassIdStrList;
boolean hasOrganizationSpecificReference;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsToOrganizationSpecificClasses;
boolean condenseData;
boolean isAggHead;
boolean isDisAllowedCountriesAspect;
boolean isTerm;
String fkAttrToDiv;
boolean hasNationalColumn;

//On Error GoTo ErrorExit 

fkAttrToDiv = "";
subClassIdStrList = "";
hasNationalColumn = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
aggHeadNavPathToOrg = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToOrg;

sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
hasNlAttributesInGen = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
if (forNl) {
entityTypeDescr = "ACM-Class (NL-Text)";
} else {
entityTypeDescr = "ACM-Class";
if (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged & !forNl) {
fkAttrToDiv = (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType]);
}
}
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
aggHeadIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefsInclSubClasses;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isDeletable = M22_Class.g_classes.descriptors[acmEntityIndex].isDeletable;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
isDisAllowedCountriesAspect = false;
isTerm = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == "TERM");
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M22_Class.g_classes.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
relRefsToOrganizationSpecificClasses = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsToOrganizationSpecificClasses;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) & !forGen & !forNl;

subClassIdStrList = (M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'");
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract)) {
subClassIdStrList = subClassIdStrList + (subClassIdStrList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
if (forNl) {
entityTypeDescr = "ACM-Relationship (NL-Text)";
} else {
entityTypeDescr = "ACM-Relationship";
}

hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
hasNlAttributesInGen = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
aggHeadIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = "R";
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
relRefs.numRefs = 0;
isGenForming = false;
hasNoIdentity = false;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
subClassIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
isDisAllowedCountriesAspect = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName.toUpperCase() == "DISALLOWEDCOUNTRIESASPECT");
isTerm = false;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
condenseData = false;
isAggHead = false;
} else {
return;
}

String qualSourceTabName;
String qualSourceParTabName;
String qualTargetRefTabName;
String qualTargetViewName;

qualSourceTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, forNl, null, null, null);
qualSourceParTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, null, null, null, null);
qualTargetRefTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, null, null, null);
qualTargetViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, true, useMqtToImplementLrtForEntity, forNl, null, null, null, null);

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  srcPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
String qualRelTabOrg;
String relOrgEntityIdStr;

// ####################################################################################################################
// #    SP for Factory Data Takeover
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnFactoryTakeOver, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Factory Data Takeover for \"" + qualSourceTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + acmEntityName + "\"" + (forGen ? "(GEN)" : "") + ")", fileNo, null, null);

boolean readUnCommitedInWorkDataPool;
readUnCommitedInWorkDataPool = isPsTagged;
readUnCommitedInWorkDataPool = true;// all records wich are subject to FTO are locked by FTOLOCK

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean aggHeadContainsIsNotPublished;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
aggHeadContainsIsNotPublished = false;

if (aggHeadClassIndex > 0) {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (tabColumns.descriptors[i].columnName.compareTo(M01_Globals_IVK.g_anIsNotPublished) == 0) {
aggHeadContainsIsNotPublished = true;
}
}
}

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, null, null, ddlType, dstOrgIndex, dstPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
}

int numAttrsToSkip;
numAttrsToSkip = 0;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].acmAttributeName.substring(tabColumns.descriptors[i].acmAttributeName.length() - 1 - 8).toUpperCase() == "NATIONAL") |  (tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 8) == "NATIONAL") | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_NATIONAL_ID" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_ISNATACTIVE" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 9) == "_ISNATACT") {
numAttrsToSkip = numAttrsToSkip + 1;
hasNationalColumn = true;
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  tabColumns.descriptors[i].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].isNationalizable |  M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].ftoConflictWithSrcAttrIndex > 0) {
numAttrsToSkip = numAttrsToSkip + 1;
}
}
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division owning the Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to process");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of the Organization");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this call");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (forNl &  hasNationalColumn) {
M11_LRT.genVarDecl(fileNo, "v_lrtId", M01_Globals.g_dbtOid, "0", null, null);
}
if (acmEntityName.toUpperCase() == "GENERICASPECT" & ! forNl) {
M11_LRT.genVarDecl(fileNo, "v_isDpb", "SMALLINT", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_isTakeoverBlockedPriceFlag", "SMALLINT", "0", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

if (aggHeadContainsIsNotPublished) {
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, null, null, null, null, null, null, null);
}
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);
if (!(condenseData)) {
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);
}

if (forNl &  hasNationalColumn) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for specially handled nl records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SESSION.NL_TEXT_OIDS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FACTORY_NL_OID      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MPC_NL_OID   " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT LOGGED;");
}

if (acmEntityName.toUpperCase() == "GENERICASPECT" & ! forNl) {
// special handling for CodeBaumusterValidities depends on PricePreferences
String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, dstOrgIndex, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.ISDPB, P.TAKEOVERBLOCKEDPRICEFLAG ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_isDpb, v_isTakeoverBlockedPriceFlag ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePricePreferences + " P ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PS_OID = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "orgOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, dstOrgIndex, dstPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

// handle INSERTs
M11_LRT.genProcSectionHeader(fileNo, "handle INSERTs (ignore INSERTs for already existing records)", null, null);

//rs40
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.isCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.entityId IN (" + subClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTargetRefTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "REF." + M01_Globals.g_anOid + " = MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conInLrt, "", null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (acmEntityName.toUpperCase() == "GENERICASPECT" & ! forNl) {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, "E.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conIsBlockedPrice, "CASE" + vbCrLf + vbTab + vbTab + "WHEN E.CLASSID = '09006' AND v_isDpb = 0 THEN 1" + vbCrLf + vbTab + vbTab + "ELSE E.ISBLOCKEDPRICE" + vbCrLf + vbTab + "END", null, null, null);
} else {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, null, null, "E.", null, null, null, null, null, null, null, null, null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualSourceTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "propagate all records not found in target data pool", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MPC_E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetRefTabName + " MPC_E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MPC_E." + M01_Globals.g_anOid + " = E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
} else {
M11_LRT.genProcSectionHeader(fileNo, "propagate inserts of records to this entity", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.ahObjectId = MIC.ahObjectId");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityId IN (" + subClassIdStrList + ")");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(MIC.isToBeDeleted," + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anOid + " IN (");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAhOid + " IN (");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.isToBeCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.aggregateType = '" + aggHeadIdStr + "'");
//rs40
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anOid + " = REF." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + fkAttrToDiv + " = divisionOid_in");
}

if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.ISINVALID = 0");
}

if (isPsTagged) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (readUnCommitedInWorkDataPool ? "WITH UR" : "") + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "no need to deal with UPDATEs since a \"" + acmEntityName + "\" is only inserted", null, null);
} else if (isTerm) {
M11_LRT.genProcSectionHeader(fileNo, "no need to deal with UPDATEs since a \"" + acmEntityName + "\" is only inserted or deleted", null, null);
} else {
// handle UPDATEs
M11_LRT.genProcSectionHeader(fileNo, "handle UPDATEs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

if (acmEntityName.toUpperCase() == "GENERICASPECT" & ! forNl) {
M24_Attribute_Utilities.initAttributeTransformation(transformation, numAttrsToSkip + 12 + 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
} else if (acmEntityName.toUpperCase() == "EXPRESSION") {
M24_Attribute_Utilities.initAttributeTransformation(transformation, numAttrsToSkip + 12 + 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
} else {
M24_Attribute_Utilities.initAttributeTransformation(transformation, numAttrsToSkip + 12 + 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 1, M01_ACM.conOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 2, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 3, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 5, M01_ACM.conVersionId, M01_Globals.g_anVersionId, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 6, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 7, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 8, M01_ACM_IVK.conIsNational, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 9, M01_ACM_IVK.conHasBeenSetProductive, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 10, M01_ACM_IVK.conHasConflict, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 11, M01_ACM_IVK.conIsDeleted, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 12, M01_ACM_IVK.conNationalDisabled, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 13, M01_ACM_IVK.conStatusId, M01_Globals.g_anStatus, null, null, null);


int thisColNo;
thisColNo = 1;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].acmAttributeName.substring(tabColumns.descriptors[i].acmAttributeName.length() - 1 - 8).toUpperCase() == "NATIONAL") |  (tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 8) == "NATIONAL") | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_NATIONAL_ID" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_ISNATACTIVE" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 9) == "_ISNATACT") {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, tabColumns.descriptors[i].columnName, "", null, null, null);
thisColNo = thisColNo + 1;
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  tabColumns.descriptors[i].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].isNationalizable) {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].shortName + "EXP", null, null, null, true), "", null, null, null);
thisColNo = thisColNo + 1;
} else if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].ftoConflictWithSrcAttrIndex > 0) {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].shortName + "EXP", null, null, null, null), "", null, null, null);
thisColNo = thisColNo + 1;
}
}
}
//special handling of Expression to Term reference due to codeCategoryAssignment processing - update to null value are not transferred (will be handled in setProd)
//If .acmEntityName = "Term" And .columnName = "EXTTRM_OID" Then
//  setAttributeMapping transformation, thisColNo, "EXTTRM_OID", "COALESCE(S.EXTTRM_OID,T.EXTTRM_OID)"
//End If
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

transformation.attributePrefix = "S.";
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 5, M01_ACM.conVersionId, "T." + M01_Globals.g_anVersionId + " + 1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 13, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

//special handling of Expression to Term reference due to codeCategoryAssignment processing - update to null value are not transferred (will be handled in setProd)
if (acmEntityName.toUpperCase() == "EXPRESSION") {
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 14, "EXTTRM_OID", "COALESCE(S.EXTTRM_OID,T.EXTTRM_OID)", null, null, null);
}

if (acmEntityName.toUpperCase() == "GENERICASPECT" & ! forNl) {
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 14, M01_ACM_IVK.conIsBlockedPrice, "CASE" + vbCrLf + vbTab + vbTab + "WHEN S.CLASSID = '09006' AND v_isDpb = 1 AND v_isTakeoverBlockedPriceFlag = 1 THEN S.ISBLOCKEDPRICE" + vbCrLf + vbTab + vbTab + "ELSE T.ISBLOCKEDPRICE" + vbCrLf + vbTab + vbTab + "END", null, null, null);
}


if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSourceTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = S." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.aggregateType = '" + aggHeadIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.entityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MCLS.entityId IN (" + subClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + fkAttrToDiv + " = divisionOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (forNl &  hasNationalColumn) {
M11_LRT.genProcSectionHeader(fileNo, "merge an entry created in MPC with the entry created in factory", null, null);
String targetLrtTableName;
targetLrtTableName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, true, null, forNl, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtId = (SELECT CURRENT CLIENT_WRKSTNNAME FROM sysibm.sysdummy1);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.NL_TEXT_OIDS(FACTORY_NL_OID, MPC_NL_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid , S.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + targetLrtTableName + " T, " + qualTargetRefTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.AHOID = S.AHOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.LANGUAGE_ID = S.LANGUAGE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.INLRT = v_lrtId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.OID <> T.oid");

String nlColumn;
String nlColumnWithoutNational;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M11_LRT.genProcSectionHeader(fileNo, "update central records with national values", null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT ST.FACTORY_NL_OID, NL.* FROM SESSION.NL_TEXT_OIDS ST, " + qualTargetRefTabName + " NL WHERE NL.OID = ST.MPC_NL_OID ) AS S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid = S.FACTORY_NL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN MATCHED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET ");

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
nlColumn = tabColumns.descriptors[i].columnName;
if ((nlColumn.substring(nlColumn.length() - 1 - 8) == "NATIONAL")) {
nlColumnWithoutNational = nlColumn.substring(0, nlColumn.length() - 9);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + nlColumn + " = S." + nlColumn + ", ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + nlColumnWithoutNational + "_ISNATACTIVE = 1, ");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.VERSIONID = T.VERSIONID + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE IGNORE;");

M11_LRT.genProcSectionHeader(fileNo, "delete national records", null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT MPC_NL_OID FROM SESSION.NL_TEXT_OIDS);");
}


}

// handle DELETEs
if (!(condenseData)) {
M11_LRT.genProcSectionHeader(fileNo, "handle DELETEs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M11_LRT.genProcSectionHeader(fileNo, "propagate deletes of records to this entity", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (isTerm) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "MCLS.aggregateType = '" + aggHeadIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "MCLS.ahObjectId = E." + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "F_E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualSourceTabName + " F_E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "F_E." + M01_Globals.g_anOid + " = E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "MCLS.ahObjectId = MIC.ahObjectId");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityId IN (" + subClassIdStrList + ")");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(MIC.isToBeCreated," + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anOid + " IN (");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAhOid + " IN (");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.isToBeDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.aggregateType = '" + aggHeadIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

if (isDeletable &  forGen & !forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anAhOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityId IN (" + subClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + fkAttrToDiv + " = divisionOid_in");
}

if (readUnCommitedInWorkDataPool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "orgOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if ((!(forGen & ! forNl & hasOrganizationSpecificReference))) {
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
}


public static void genFtoSupportSpsForEntitySingleObject(int acmEntityIndex, Integer acmEntityType, int srcOrgIndex, int srcPoolIndex, int dstOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

String sectionName;
String acmEntityName;
String acmEntityShortName;
String entityTypeDescr;
boolean isUserTransactional;
boolean isPsTagged;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
String aggHeadIdStr;
int aggHeadClassIndex;
String dbAcmEntityType;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
boolean hasNoIdentity;
boolean ignoreForChangelog;
boolean hasNlAttributes;
boolean hasNlAttributesInGen;
boolean useMqtToImplementLrtForEntity;
M22_Class_Utilities.NavPathFromClassToClass aggHeadNavPathToOrg;
String subClassIdStrList;
String aggHeadSubClassIdStrList;
boolean hasOrganizationSpecificReference;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsToOrganizationSpecificClasses;
boolean condenseData;
boolean isAggHead;
boolean isDisAllowedCountriesAspect;
boolean isTerm;
String fkAttrToDiv;

//On Error GoTo ErrorExit 

fkAttrToDiv = "";
subClassIdStrList = "";

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
aggHeadNavPathToOrg = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToOrg;

sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
hasNlAttributesInGen = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
if (forNl) {
entityTypeDescr = "ACM-Class (NL-Text)";
} else {
entityTypeDescr = "ACM-Class";
if (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged & !forNl) {
fkAttrToDiv = (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType]);
}
}
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
aggHeadIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefsInclSubClasses;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
isDisAllowedCountriesAspect = false;
isTerm = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == "TERM");
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M22_Class.g_classes.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
relRefsToOrganizationSpecificClasses = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsToOrganizationSpecificClasses;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) & !forGen & !forNl;

subClassIdStrList = (M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'");
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract)) {
subClassIdStrList = subClassIdStrList + (subClassIdStrList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
if (forNl) {
entityTypeDescr = "ACM-Relationship (NL-Text)";
} else {
entityTypeDescr = "ACM-Relationship";
}

hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
hasNlAttributesInGen = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
aggHeadIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = "R";
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
attrRefsInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
relRefs.numRefs = 0;
isGenForming = false;
hasNoIdentity = false;
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
subClassIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
isDisAllowedCountriesAspect = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName.toUpperCase() == "DISALLOWEDCOUNTRIESASPECT");
isTerm = false;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
condenseData = false;
isAggHead = false;
} else {
return;
}

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  srcPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

String qualSourceTabName;
String qualSourceParTabName;
String qualTargetViewName;
String qualTargetRefTabName;

qualSourceTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, forNl, null, null, null);
qualSourceParTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, null, null, null, null);
qualTargetRefTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, null, null, null);
qualTargetViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, true, useMqtToImplementLrtForEntity, forNl, null, null, null, null);

String qualTargetTabNamePub;
qualTargetTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, false, null, forNl, null, null, null);

String qualRelTabOrg;
String relOrgEntityIdStr;
M24_Attribute_Utilities.AttributeListTransformation transformation;

// ####################################################################################################################
// #    SP for Factory Data Takeover
// ####################################################################################################################

String qualProcName;
qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnFactoryTakeOver, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Factory Data Takeover for \"" + qualSourceTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + acmEntityName + "\"" + (forGen ? "(GEN)" : "") + ")", fileNo, null, null);

boolean readUnCommitedInFactory;
readUnCommitedInFactory = isPsTagged;

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean aggHeadContainsIsNotPublished;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
aggHeadContainsIsNotPublished = false;

if (aggHeadClassIndex > 0) {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (tabColumns.descriptors[i].columnName.compareTo(M01_Globals_IVK.g_anIsNotPublished) == 0) {
aggHeadContainsIsNotPublished = true;
}
}
}

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, null, null, ddlType, dstOrgIndex, dstPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
}

int numAttrsToSkip;
numAttrsToSkip = 0;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].acmAttributeName.substring(tabColumns.descriptors[i].acmAttributeName.length() - 1 - 8).toUpperCase() == "NATIONAL") |  (tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 8) == "NATIONAL") | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_NATIONAL_ID" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_ISNATACTIVE" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 9) == "_ISNATACT") {
numAttrsToSkip = numAttrsToSkip + 1;
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  tabColumns.descriptors[i].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].isNationalizable |  M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].ftoConflictWithSrcAttrIndex > 0) {
numAttrsToSkip = numAttrsToSkip + 1;
}
}
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division owning the Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to process");
M11_LRT.genProcParm(fileNo, "IN", "orgOid_in", M01_Globals.g_dbtOid, true, "OID of the Organization");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this call");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (forNl) {
M11_LRT.genVarDecl(fileNo, "v_lrtId", M01_Globals.g_dbtOid, "0", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

if (aggHeadContainsIsNotPublished) {
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, null, null, null, null, null, null, null);
}
M12_ChangeLog.genDdlForTempChangeLogSummary(fileNo, 1, true, null, null, null);
if (!(condenseData)) {
M12_ChangeLog.genDdlForTempImplicitChangeLogSummary(fileNo, 1, true, null, null, null);
}

if (forNl) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for specially handled nl records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SESSION.NL_TEXT_OIDS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FACTORY_NL_OID      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MPC_NL_OID   " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT LOGGED;");
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "psOid_in", "orgOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, dstOrgIndex, dstPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

// handle INSERTs
M11_LRT.genProcSectionHeader(fileNo, "handle INSERTs (ignore INSERTs for already existing records)", null, null);

//rs40
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.isCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MCLS.entityId IN (" + subClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTargetRefTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "REF." + M01_Globals.g_anOid + " = MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conInLrt, "", null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (acmEntityName.toUpperCase() == "GENERICASPECT") {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, null, null, "E.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
} else {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, null, null, "E.", null, null, null, null, null, null, null, null, null, null, null);
}
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualSourceTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "propagate all records not found in target data pool", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MPC_E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetRefTabName + " MPC_E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MPC_E." + M01_Globals.g_anOid + " = E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
} else {
M11_LRT.genProcSectionHeader(fileNo, "propagate inserts of records to this entity", 3, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.ahObjectId = MIC.ahObjectId");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityId IN (" + subClassIdStrList + ")");
if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "COALESCE(MIC.isToBeDeleted," + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anOid + " IN (");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "E." + M01_Globals.g_anAhOid + " IN (");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.isToBeCreated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MIC.aggregateType = '" + aggHeadIdStr + "'");
//rs40
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabName + " REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "E." + M01_Globals.g_anOid + " = REF." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + fkAttrToDiv + " = divisionOid_in");
}

if (isPsTagged) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (readUnCommitedInFactory ? "WITH UR" : "") + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (condenseData) {
M11_LRT.genProcSectionHeader(fileNo, "no need to deal with UPDATEs since a \"" + acmEntityName + "\" is only inserted", null, null);
} else if (isTerm) {
M11_LRT.genProcSectionHeader(fileNo, "no need to deal with UPDATEs since a \"" + acmEntityName + "\" is only inserted or deleted", null, null);
} else {
// handle UPDATEs
M11_LRT.genProcSectionHeader(fileNo, "handle UPDATEs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR oidLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " AS oidToUpdate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSourceTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isUpdated = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isCreated = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.isDeleted = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.aggregateType = '" + aggHeadIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "MCLS.entityId IN (" + subClassIdStrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E." + fkAttrToDiv + " = divisionOid_in");
}
if (readUnCommitedInFactory) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, numAttrsToSkip + 12 + 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 1, M01_ACM.conOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 2, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 3, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 5, M01_ACM.conVersionId, M01_Globals.g_anVersionId, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 6, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 7, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 8, M01_ACM_IVK.conIsNational, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 9, M01_ACM_IVK.conHasBeenSetProductive, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 10, M01_ACM_IVK.conHasConflict, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 11, M01_ACM_IVK.conIsDeleted, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 12, M01_ACM_IVK.conNationalDisabled, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 13, M01_ACM_IVK.conStatusId, M01_Globals.g_anStatus, null, null, null);

int thisColNo;
thisColNo = 1;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].acmAttributeName.substring(tabColumns.descriptors[i].acmAttributeName.length() - 1 - 8).toUpperCase() == "NATIONAL") |  (tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 8) == "NATIONAL") | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_NATIONAL_ID" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 12) == "_ISNATACTIVE" | tabColumns.descriptors[i].columnName.substring(tabColumns.descriptors[i].columnName.length() - 1 - 9) == "_ISNATACT") {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, tabColumns.descriptors[i].columnName, "", null, null, null);
thisColNo = thisColNo + 1;
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  tabColumns.descriptors[i].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].isNationalizable) {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].shortName + "EXP", null, null, null, true), "", null, null, null);
thisColNo = thisColNo + 1;
} else if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].ftoConflictWithSrcAttrIndex > 0) {
M24_Attribute_Utilities.setAttributeMapping(transformation, thisColNo, M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[i].acmAttributeIndex].shortName + "EXP", null, null, null, null), "", null, null, null);
thisColNo = thisColNo + 1;
}
}
}
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 3, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");

transformation.attributePrefix = "S.";
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 5, M01_ACM.conVersionId, "T." + M01_Globals.g_anVersionId + " + 1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, numAttrsToSkip + 13, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, srcOrgIndex, srcPoolIndex, 4, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 4, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualSourceTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + M01_Globals.g_anOid + " = S." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = oidToUpdate");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");


if (forNl) {
M11_LRT.genProcSectionHeader(fileNo, "merge an entry created in MPC with the entry created in factory", null, null);
M00_FileWriter.printToFile(fileNo, "");
String targetLrtTableName;
targetLrtTableName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, true, null, forNl, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtId = (SELECT CURRENT CLIENT_WRKSTNNAME FROM sysibm.sysdummy1);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.NL_TEXT_OIDS(FACTORY_NL_OID, MPC_NL_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid , S.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + targetLrtTableName + " T, " + qualTargetRefTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.AHOID = S.AHOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.LANGUAGE_ID = S.LANGUAGE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.INLRT = v_lrtId");

String nlColumn;
String nlColumnWithoutNational;

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
nlColumn = tabColumns.descriptors[i].columnName;
if ((nlColumn.substring(nlColumn.length() - 1 - 8) == "NATIONAL")) {
nlColumnWithoutNational = nlColumn.substring(0, nlColumn.length() - 9);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S." + nlColumnWithoutNational + " IS NULL ");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M11_LRT.genProcSectionHeader(fileNo, "update central records with national values", null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT ST.FACTORY_NL_OID, NL.* FROM SESSION.NL_TEXT_OIDS ST, " + qualTargetRefTabName + " NL WHERE NL.OID = ST.MPC_NL_OID ) AS S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid = S.FACTORY_NL_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN MATCHED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "THEN UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET ");

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
nlColumn = tabColumns.descriptors[i].columnName;
if ((nlColumn.substring(nlColumn.length() - 1 - 8) == "NATIONAL")) {
nlColumnWithoutNational = nlColumn.substring(0, nlColumn.length() - 9);
String suffix;
if ((nlColumnWithoutNational.compareTo("ICODESHORTDESCRIPTION") == 0)) {
suffix = "_ISNATACT";
} else {
suffix = "_ISNATACTIVE";
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + nlColumn + " = S." + nlColumn + ", ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + nlColumnWithoutNational + suffix + " = 1, ");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.VERSIONID = T.VERSIONID + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE IGNORE;");

M11_LRT.genProcSectionHeader(fileNo, "delete national records", null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid IN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT MPC_NL_OID FROM SESSION.NL_TEXT_OIDS);");
}


}

// handle DELETEs
if (!(condenseData)) {
M11_LRT.genProcSectionHeader(fileNo, "handle DELETEs based on explicitly deleted objects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR oidLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.objectId AS oidToDelete");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary + " MCLS");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.ahObjectId = MIC.ahObjectId");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.isDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.entityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MCLS.entityId IN (" + subClassIdStrList + ")");

if (isDisAllowedCountriesAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(MIC.isToBeCreated," + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = oidToDelete");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "handle DELETEs based on implicitly deleted aggregate heads", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR oidLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MIC.ahObjectId AS oidToDelete");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MIC.ahObjectId AS ahOidToDelete");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges + " MIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MIC.isToBeDeleted = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MIC.aggregateType = '" + aggHeadIdStr + "'");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetViewName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anOid + " = oidToDelete");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T." + M01_Globals.g_anAhOid + " = ahOidToDelete");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "psOid_in", "orgOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
}


public static void genFtoPostProcSupportSpsForEntity(int acmEntityIndex, Integer acmEntityType, int srcOrgIndex, int srcPoolIndex, int dstOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW) {
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

String sectionName;
String acmEntityName;
String acmEntityShortName;
String dbObjShortName;
String entityTypeDescr;
boolean isUserTransactional;
boolean isPsTagged;
boolean hasOwnTable;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isAbstract;
String entityIdStr;
String aggHeadIdStr;
int aggHeadClassIndex;
String dbAcmEntityType;
boolean isGenForming;
boolean hasNoIdentity;
boolean hasNlAttributes;
boolean hasNlAttributesInGen;
boolean useMqtToImplementLrtForEntity;
M22_Class_Utilities.NavPathFromClassToClass aggHeadNavPathToOrg;
String subClassIdStrList;
String aggHeadSubClassIdStrList;
boolean hasOrganizationSpecificReference;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsToOrganizationSpecificClasses;
boolean condenseData;
boolean isAggHead;
boolean isTerm;
String fkAttrToDiv;

//On Error GoTo ErrorExit 

fkAttrToDiv = "";
subClassIdStrList = "";

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
aggHeadNavPathToOrg = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToOrg;
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
hasNlAttributesInGen = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
if (forNl) {
dbObjShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
} else {
dbObjShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
if (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged & !forNl) {
fkAttrToDiv = (M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType]);
}
}
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
aggHeadIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isTerm = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == "TERM");
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M22_Class.g_classes.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
relRefsToOrganizationSpecificClasses = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsToOrganizationSpecificClasses;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) & !forGen & !forNl;

subClassIdStrList = (M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract ? "" : "'" + M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr + "'");
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isAbstract)) {
subClassIdStrList = subClassIdStrList + (subClassIdStrList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
if (forNl) {
dbObjShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
} else {
dbObjShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
}

hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
hasNlAttributesInGen = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isAbstract = false;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
aggHeadIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIdStr;
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
dbAcmEntityType = "R";
isGenForming = false;
hasNoIdentity = false;
subClassIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
isTerm = false;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
hasOrganizationSpecificReference = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasOrganizationSpecificReference;
condenseData = false;
isAggHead = false;
} else {
return;
}

String qualSourceTabName;
String qualSourceParTabName;
String qualTargetRefTabName;
String qualTargetViewName;

qualSourceTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, forNl, null, null, null);
qualSourceParTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, null, null, null, null, null, null);
qualTargetRefTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, null, null, null);
qualTargetViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, true, useMqtToImplementLrtForEntity, forNl, null, null, null, null);

String qualTabNamePricePreferences;
qualTabNamePricePreferences = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex, null, null, null, null, null, null, null);

if (!(M03_Config.generateLrt | ! isUserTransactional)) {
return;
}
if (ddlType == M01_Common.DdlTypeId.edtPdm &  (srcOrgIndex < 1 |  srcPoolIndex < 1)) {
// LRT is only supported at 'pool-level'
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
String qualRelTabOrg;
String relOrgEntityIdStr;

if ((!(forGen & ! forNl & hasOrganizationSpecificReference))) {
// ####################################################################################################################
// #    SP for Factory Data Takeover Post-Processing
// ####################################################################################################################
String qualProcNameFtoPostProc;
qualProcNameFtoPostProc = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnFtoPostProc, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Factory Data Takeover Post-Processing for \"" + qualSourceTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + acmEntityName + "\"" + (forGen ? "(GEN)" : "") + ")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameFtoPostProc);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "OID of Factory-Takeover LRT");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the ProductStructure corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, "OID of the Division corresponding to the LRT");
M11_LRT.genProcParm(fileNo, "IN", "opType_in", M01_Globals.g_dbtEnumId, true, "if '1' post-process INSERT, if set to '3' post-process DELETE");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows affected by this call");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (acmEntityName.toUpperCase() == M01_ACM_IVK.rnCodeCategory.toUpperCase()) {
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_catOid", "BIGINT", "NULL", null, null);
}
if (isAggHead |  isTerm) {
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(200)", "NULL", null, null);
}
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameFtoPostProc, ddlType, null, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

String qualTargetTabNamePriv;
qualTargetTabNamePriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);

String qualTargetTabNamePub;
qualTargetTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);

if (isAggHead |  isTerm) {
M92_DBUtilities.genDdlForTempOids(fileNo, 1, true, null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out  = 0;");

String qualTargetRefTabNamePriv;
String qualTargetRefTabNamePub;
String fkAttrName;
int tabClassIndex;
boolean isFirstRel;
boolean isFirstLoop;
int start;
int ende;

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opType_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " ) THEN");

if (isAggHead |  isTerm) {
M11_LRT.genProcSectionHeader(fileNo, "determine records with references to organization-specific records in other tables", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M92_DBUtilities.tempTabNameOids);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anAhOid);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNamePriv + " " + dbObjShortName.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anLrtState + " IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

if (!(fkAttrToDiv.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + fkAttrToDiv + " = divisionOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
isFirstRel = true;
for (int i = 1; i <= relRefsToOrganizationSpecificClasses.numRefs; i++) {
if (M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].reusedRelIndex > 0) {
goto nextRelRef;
}

if (relRefsToOrganizationSpecificClasses.refs[i].refType == M01_Common.RelNavigationDirection.etLeft) {
tabClassIndex = (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].hasOwnTable ? M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].classIndex : M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].orMappingSuperClassIndex);
fkAttrName = M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightFkColName[ddlType];
} else {
tabClassIndex = (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].hasOwnTable ? M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].classIndex : M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].orMappingSuperClassIndex);
fkAttrName = M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftFkColName[ddlType];
}
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);

if (!(isFirstRel)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
isFirstRel = false;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + dbObjShortName.toUpperCase() + "." + fkAttrName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
//rs32
if (!(M22_Class.g_classes.descriptors[tabClassIndex].condenseData & ! condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
nextRelRef;

}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex <= 0) {

isFirstLoop = true;

start = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftClassIsOrganizationSpecific &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].isUserTransactional ? 1 : 2);
ende = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightClassIsOrganizationSpecific &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].isUserTransactional ? 2 : 1);
for (int i = start; i <= ende; i++) {
// left class is organization specific
if (i == 1) {
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null, null, null, null);
} else {
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName, null, null, null, null);
}

if (!(isFirstLoop)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
isFirstLoop = false;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

if (isAggHead |  isTerm) {
M11_LRT.genProcSectionHeader(fileNo, "if some record is to be deleted, delete it and all records related to this aggregate", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_rowCount > 0 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anPdmTableName + " AS TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(dstOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(dstPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAhCid + " = '" + aggHeadIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WITH UR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "delete dependent aggregate elements", 4, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntText = 'DELETE FROM ' || TABSCHEMA || '.' || TABNAME || ' AE WHERE EXISTS (SELECT 1 FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(13) + "'" + M92_DBUtilities.tempTabNameOids + " O WHERE AE." + M01_Globals.g_anAhOid + " = O." + M01_Globals.g_anOid + ") AND AE." + M01_Globals.g_anInLrt + " = ' || COALESCE(RTRIM(CHAR(lrtOid_in)), '-1') || ' WITH UR';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntText;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

if (acmEntityName.toUpperCase() == M01_ACM_IVK.clnGenericAspect.toUpperCase()) {
M11_LRT.genProcSectionHeader(fileNo, "CCPCCP_OID reference set NULL, if the central record has been deleted", 4, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF EXISTS ( SELECT " + M01_Globals.g_anOid + " FROM " + qualTabNamePricePreferences + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in AND isconflictdetermforprices = 0 ) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTargetViewName + " AS gas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "gas.ccpccp_oid = NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.ccpccp_oid IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas." + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.classid IN ( '09031', '09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gas.ps_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'1'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + qualTargetTabNamePriv + " AS gas_l");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "gas.ccpccp_oid = gas_l.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "gas_l." + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "gas_l.classid IN ( '09031', '09033')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "gas_l.ps_oid = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "gas_l.lrtstate = " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

}

} else {

if (acmEntityName.toUpperCase() == M01_ACM_IVK.rnCodeCategory.toUpperCase()) {
M11_LRT.genProcSectionHeader(fileNo, "don't update records with references to organization-specific records in other tables", 2, !((isAggHead |  isTerm)));
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT " + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO v_CatOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNamePriv + " " + dbObjShortName.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anLrtState + " IN (" + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_CatOid IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_msg = RTRIM(LEFT('[MDS]:  CodeCategory with invalid category ''' || RTRIM(CHAR(v_CatOid)) || ''' for this MPC',70));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SIGNAL SQLSTATE '79030' SET MESSAGE_TEXT = v_msg;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}


M11_LRT.genProcSectionHeader(fileNo, "ignore records with references to organization-specific records in other tables", 2, !((isAggHead |  isTerm)));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNamePriv + " " + dbObjShortName.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + dbObjShortName.toUpperCase() + "." + M01_Globals.g_anLrtState + " IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
isFirstRel = true;
for (int i = 1; i <= relRefsToOrganizationSpecificClasses.numRefs; i++) {
if (M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].reusedRelIndex > 0) {
goto nextRelRef2;
}

if (relRefsToOrganizationSpecificClasses.refs[i].refType == M01_Common.RelNavigationDirection.etLeft) {
tabClassIndex = (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].hasOwnTable ? M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].classIndex : M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightEntityIndex].orMappingSuperClassIndex);
fkAttrName = M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].rightFkColName[ddlType];
} else {
tabClassIndex = (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].hasOwnTable ? M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].classIndex : M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftEntityIndex].orMappingSuperClassIndex);
fkAttrName = M23_Relationship.g_relationships.descriptors[relRefsToOrganizationSpecificClasses.refs[i].refIndex].leftFkColName[ddlType];
}
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);

if (!(isFirstRel)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
isFirstRel = false;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + dbObjShortName.toUpperCase() + "." + fkAttrName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
nextRelRef2;

}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex <= 0) {

isFirstLoop = true;

start = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftClassIsOrganizationSpecific &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].isUserTransactional ? 1 : 2);
ende = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightClassIsOrganizationSpecific &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].isUserTransactional ? 2 : 1);
for (int i = start; i <= ende; i++) {
// left class is organization specific
if (i == 1) {
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null, null, null, null);
} else {
qualTargetRefTabNamePriv = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, true, null, null, null, null, null);
qualTargetRefTabNamePub = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, null, false, null, null, null, null, null);
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName, null, null, null, null);
}

if (!(isFirstLoop)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
isFirstLoop = false;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePriv + " PRIV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PRIV." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + qualTargetRefTabNamePub + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals.g_anOid + " = " + dbObjShortName.toUpperCase() + "." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "PUB." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

if (isGenForming & ! hasNoIdentity) {
//        Dim qualTargetTabNamePub As String
qualTargetTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, false, false, null, null, null, null, null);
String qualTargetTabNameGenPriv;
qualTargetTabNameGenPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, true, true, null, null, null, null, null);

fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, acmEntityShortName, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "ignore records in GEN-table with references to organization-specific records in parent table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNameGenPriv + " PRIVGEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIVGEN." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIVGEN." + M01_Globals.g_anLrtState + " IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetTabNamePriv + " PRIVPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anOid + " = PRIVGEN." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetTabNamePub + " PUBPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBPAR." + M01_Globals.g_anOid + " = PRIVGEN." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBPAR." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}

if ((isGenForming & ! hasNoIdentity & hasNlAttributesInGen) |  (!(isGenForming &  hasNlAttributes))) {
String qualTargetTabNameNlPriv;
String qualTargetTabNameParPub;
String qualTargetTabNameParPriv;

if (isGenForming & ! hasNoIdentity & hasNlAttributesInGen) {
qualTargetTabNameNlPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, true, true, null, true, null, null, null);
qualTargetTabNameParPub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, true, false, null, null, null, null, null);
qualTargetTabNameParPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, true, true, null, null, null, null, null);
} else {
qualTargetTabNameNlPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, false, true, null, true, null, null, null);
qualTargetTabNameParPub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, false, false, null, null, null, null, null);
qualTargetTabNameParPriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, false, true, null, null, null, null, null);
}

fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, acmEntityShortName, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "ignore records in NL-table with references to organization-specific records in parent table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNameNlPriv + " PRIVNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIVNL." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PRIVNL." + M01_Globals.g_anLrtState + " IN (" + String.valueOf(M11_LRT.lrtStatusCreated) + "," + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetTabNameParPriv + " PRIVPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anOid + " = PRIVNL." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anLrtState + " = " + String.valueOf(M11_LRT.lrtStatusCreated));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PRIVPAR." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTargetTabNameParPub + " PUBPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBPAR." + M01_Globals.g_anOid + " = PRIVNL." + fkAttrName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PUBPAR." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex <= 0 &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
String qualTargetTabNameLeft;
String qualTargetTabNameRight;
String fkAttrNameLeft;
String fkAttrNameRight;
String fkAttrToDivLeft;
String fkAttrToDivRight;
int leftEntityIndexPar;
boolean leftEntityIsCommonToOrgs;

leftEntityIsCommonToOrgs = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].isCommonToOrgs;
if (leftEntityIsCommonToOrgs) {
qualTargetTabNameLeft = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].classIndex, ddlType, dstOrgIndex, dstPoolIndex, forGen, false, null, null, null, null, null);
} else {
qualTargetTabNameLeft = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].classIndex, ddlType, dstOrgIndex, dstPoolIndex, false, true, true, null, null, null, null, null);
}

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].isPsTagged) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDivLeft = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDivLeft = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
}

fkAttrNameLeft = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName, null, null, null, null);


//           qualTargetTabNameRight = genQualTabNameByClassIndex(.classIndex, ddlType, dstOrgIndex, dstPoolIndex, forGen, False, , , , True)
qualTargetTabNameRight = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].classIndex, ddlType, dstOrgIndex, dstPoolIndex, false, true, true, null, null, null, null, null);

if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].isPsTagged) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft) {
fkAttrToDivRight = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType];
} else {
fkAttrToDivRight = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].orMappingSuperClassIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType];
}
}

fkAttrNameRight = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF ( opType_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " ) THEN");
M11_LRT.genProcSectionHeader(fileNo, "include records with references to organization-specific records in other tables", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conInLrt, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, null, false, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 5, null, null, null, "SRC.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conUpdateUserName, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, null, false, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualSourceTabName + " SRC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNameLeft + " LFT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LFT." + M01_Globals.g_anOid + " = SRC." + fkAttrNameLeft);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNameRight + " RGHT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RGHT." + M01_Globals.g_anOid + " = SRC." + fkAttrNameRight);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNamePriv + " TGTPRI");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC." + M01_Globals.g_anOid + " = TGTPRI." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTargetTabNamePub + " TGTPUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC." + M01_Globals.g_anOid + " = TGTPUB." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TGTPRI." + M01_Globals.g_anOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TGTPUB." + M01_Globals.g_anOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SRC." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);

if (!(fkAttrToDivLeft.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LFT." + fkAttrToDivLeft + " = divisionOid_in");
}

if (!(fkAttrToDivRight.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RGHT." + fkAttrToDivRight + " = divisionOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}
}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameFtoPostProc, ddlType, null, "lrtOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
}


public static void genFtoSupportDdlForClass(int classIndex, int srcOrgIndex, int srcPoolIndex, int dstOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

if (M22_Class.g_classes.descriptors[classIndex].ftoSingleObjProcessing) {
M87_FactoryTakeOver.genFtoSupportSpsForEntitySingleObject(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);
} else {
M87_FactoryTakeOver.genFtoSupportSpsForEntity(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);
}

M87_FactoryTakeOver.genFtoPostProcSupportSpsForEntity(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);

if ((forGen ? M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInNonGenInclSubClasses)) {
if (M22_Class.g_classes.descriptors[classIndex].ftoSingleObjProcessing) {
M87_FactoryTakeOver.genFtoSupportSpsForEntitySingleObject(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, true);
} else {
M87_FactoryTakeOver.genFtoSupportSpsForEntity(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, true);
}
}
}


public static void genFtoSupportDdlForRelationship(int thisRelIndex, int srcOrgIndex, int srcPoolIndex, int dstOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].ftoSingleObjProcessing) {
M87_FactoryTakeOver.genFtoSupportSpsForEntitySingleObject(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);
} else {
M87_FactoryTakeOver.genFtoSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);
}
M87_FactoryTakeOver.genFtoPostProcSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].ftoSingleObjProcessing) {
M87_FactoryTakeOver.genFtoSupportSpsForEntitySingleObject(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, true);
} else {
M87_FactoryTakeOver.genFtoSupportSpsForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, true);
}
}
}
// ### ENDIF IVK ###













}