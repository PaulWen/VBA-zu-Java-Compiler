package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M85_DataFix {


// ### IF IVK ###


public static final String tempExpOidTabName = "SESSION.ExpOid";
public static final String tempFtoExpOidTabName = "SESSION.FtoExpOid";
public static final String tempCodeOidTabName = "SESSION.CodeOid";
public static final String tempDataPoolTabName = "SESSION.DataPool";
public static final String tempAffectedObjectsTabName = "SESSION.AffectedObjects";

private static final boolean generateExpCopySupport = true;

private static final int processingStep = 1;
private static void genDeleteCBMVDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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


if (!(M03_Config.supportSectionDataFix)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// Delete of CBMV is only supported at 'pool-level'
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Delete of CBMV only supported in data pools supporting LRT
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

//On Error GoTo ErrorExit 

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewNameGenericAspectMqt;
qualViewNameGenericAspectMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, null, null, null, null, null);

String qualViewNameGenericAspectNlTextMqt;
qualViewNameGenericAspectNlTextMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, true, null, null, null, null);

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualLrtCommitProcName;
qualLrtCommitProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

// ####################################################################################################################
// #    SP for Deleting 'CBMVs '
// ####################################################################################################################

String qualProcNameDeleteTechProperty;
qualProcNameDeleteTechProperty = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDeleteCBMV, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
M22_Class_Utilities.printSectionHeader("SP for 'Deleting CBMV for a code'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDeleteTechProperty);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "code_in", "VARCHAR(15)", true, "Code");
M11_LRT.genProcParm(fileNo, "IN", "codeOid_in", M01_Globals.g_dbtOid, true, "Code-OID");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", "VARCHAR(15)", true, "User id");
M11_LRT.genProcParm(fileNo, "IN", "ps_oid_in", M01_Globals.g_dbtOid, true, "PS-OID");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records (sum over all involved tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SPECIFIC DELETECBMV");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare constants", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_trNumber           INTEGER          CONSTANT     3;                    -- logical transaction number");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_msg", "VARCHAR(70)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_genChangelog", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspError", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspInfo", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspWarning", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that DB2-registers are empty", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );");

M11_LRT.genProcSectionHeader(fileNo, "open LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualLrtBeginProcName + "(cdUserId_in, c_trNumber, ps_oid_in, 0, v_lrtOid);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( cdUserId_in, v_lrtOid, ps_oid_in, NULL, NULL );");

M11_LRT.genProcSectionHeader(fileNo, "delete delete CBMV from GenericAspects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspectMqt + " GA ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.BCDBCD_OID = codeOid_in ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.CLASSID = '09006' ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PS_OID = ps_oid_in ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete GenericAspect_Nl-Text", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspectNlTextMqt + " NL ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (SELECT 1 FROM " + qualViewNameGenericAspectMqt + " GA WHERE GA.INLRT = v_lrtOid AND NL.AHOID = GA.AHOID AND GA.LRTSTATE = 3) AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.AHCLASSID = '09006' AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.PS_OID = ps_oid_in ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "set LRT comment", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + "_NL_TEXT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEXTVAL FOR " + M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null) + ", v_lrtOid, 1,  'MDS Service Skript: Löschen der Code-BM-Gültigkeiten für Code ''' || RTRIM( code_in ) || '''. PsOid: '  || RTRIM( ps_oid_in ), ps_oid_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "commit LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualLrtCommitProcName + "(v_lrtOid, 0, v_genChangelog, v_rowCount, v_gwspError, v_gwspInfo, v_gwspWarning );");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, "");


NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);

}

public static void genDdlForExpEntityLockAndUpdate(int fileNo, Integer ddlType, int thisOrgIndex, int thisPoolIndex, int classIndex, String objOidVariable, boolean isPrimaryOrg, String qualProcNameAssignCodeCat) {
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);
String unqualTabName;
unqualTabName = M04_Utilities.getUnqualObjName(qualTabName);

String qualViewName;
String qualViewTaxParameter;
if (classIndex != M01_Globals_IVK.g_classIndexTaxParameter) {
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);
} else {
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, false, null, null, null, null);
qualViewTaxParameter = M04_Utilities.genQualViewNameByEntityIndex(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);
}

String qualViewNameGenericAspect;
qualViewNameGenericAspect = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, null, null, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "update re-mapped Expression-references in " + unqualTabName + "s", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewName + " EN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
boolean includeOr;
int numExpressions;
int i;
numExpressions = 0;
if (classIndex != M01_Globals_IVK.g_classIndexTaxParameter) {
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) != 0 &  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) {
numExpressions = numExpressions + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + tabColumns.descriptors[i].columnName + " = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = " + tabColumns.descriptors[i].columnName + ")," + tabColumns.descriptors[i].columnName + "),");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN.FOREXP_OID = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = FOREXP_OID),FOREXP_OID),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

if (classIndex != M01_Globals_IVK.g_classIndexTaxParameter) {
includeOr = false;
numExpressions = 0;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) != 0 &  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) {
numExpressions = numExpressions + 1;
if (includeOr) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
includeOr = true;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EN." + tabColumns.descriptors[i].columnName + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EN.FOREXP_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
if (!(isPrimaryOrg &  classIndex != M01_Globals_IVK.g_classIndexTaxParameter)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (SELECT 1 FROM VL6CASP021.GENERICASPECT GA WHERE GA.OID = EN.AHOID AND GA.ISNATIONAL = 1)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

if (classIndex == M01_Globals_IVK.g_classIndexTaxParameter) {
M11_LRT.genProcSectionHeader(fileNo, "lock GenericAspects which belongs to locked " + unqualTabName, 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewTaxParameter);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
} else {
M11_LRT.genProcSectionHeader(fileNo, "lock GenericAspects which belongs to locked " + unqualTabName, 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbTrue);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
}

}


public static void genDdlForTempFtoExpOid(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for FTO-Expression-OIDs", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempFtoExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}
public static void genDdlForTempExpOid(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Expression-OIDs", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

public static void genDdlForTempAffectedObjects(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for AffectedObjects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempAffectedObjectsTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid                  " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "classid              VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "inLrt                " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "owner                VARCHAR(16),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tr                   " + M01_Globals.g_dbtInteger);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

public static void genDdlForTempCodeOid(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW, Boolean includeHasBeenSetProductiveW) {
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

boolean includeHasBeenSetProductive; 
if (includeHasBeenSetProductiveW == null) {
includeHasBeenSetProductive = false;
} else {
includeHasBeenSetProductive = includeHasBeenSetProductiveW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Code-OIDs", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
if (includeHasBeenSetProductive) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid                  " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "hasBeenSetProductive " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "codeNumber           " + M01_Globals_IVK.g_dbtCodeNumber);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "codeNumber " + M01_Globals_IVK.g_dbtCodeNumber);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


public static void genDdlForTempDataPool(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Data Pools", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orgId        " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orgOid       " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "psOid        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "accessModeId " + M01_Globals.g_dbtEnumId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


public static void genDataFixSupportDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
genDataFixSupportUtils(M01_Common.DdlTypeId.edtPdm);

for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M72_DataPool.g_pools.descriptors[thisPoolIndex].supportUpdates) {
genAssignCodeCatSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genDeleteNSR1SupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genModifyCodeTypeSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genCheckAffectedObjectsByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genDeleteProdCodeSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genDeleteTechAspectSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genDeleteTechPropertySupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genDeleteCBMVDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
genActivateNationalCodeTextSupportDdlByPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
private static void genActivateNationalCodeTextSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// only supported in data pools supporting LRT
return;
}

if (thisOrgIndex == M01_Globals.g_primaryOrgId) {
// only supported in non-primary data pools
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String targetSchemaName;
targetSchemaName = M04_Utilities.genSchemaName(M01_ACM.snAlias, M01_ACM.ssnAlias, ddlType, thisOrgIndex, thisPoolIndex);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String unqualTabNameGenericCode;
unqualTabNameGenericCode = M04_Utilities.getUnqualObjName(qualTabNameGenericCode);
String qualViewNameGenericCode;
qualViewNameGenericCode = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, null, null, null, null, null);

String qualTabNameGenericCodeNlText;
qualTabNameGenericCodeNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String unqualTabNameGenericCodeNlText;
unqualTabNameGenericCodeNlText = M04_Utilities.getUnqualObjName(qualTabNameGenericCodeNlText);
String qualViewNameGenericCodeNlText;
qualViewNameGenericCodeNlText = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, true, null, null, null, null);

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);


// ####################################################################################################################
// #    SP to activate national code texts
// ####################################################################################################################

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcNameActivateNationalCodeTexts;
qualProcNameActivateNationalCodeTexts = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnActivateNationalCodeTexts, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP to activate national code texts", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameActivateNationalCodeTexts);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", M01_Globals.g_dbtOid, true, " -- '0' - only list affected records, '1' execute changes");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of 'current' Product Structure  - used for LRT and to determine division");
M11_LRT.genProcParm(fileNo, "IN", "languageId_in", M01_Globals.g_dbtOid, true, "1 German, 2 English, ...., see Language_Enum");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being updated (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "0 ", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_trNumber", "INTEGER", "1", null, null);

M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_previewStmnt", "STATEMENT", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE clientcur CURSOR WITH RETURN FOR v_previewStmnt;");
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");


M11_LRT.genProcSectionHeader(fileNo, "determine division OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_divisionOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_divisionOid IS NULL) THEN");
M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (mode_in = 0) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'GC.CODENUMBER, NL.LABEL, NL.LABEL_NATIONAL ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'FROM ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || '" + qualTabNameGenericCodeNlText + " NL, " + qualTabNameGenericCode + " GC ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'WHERE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'NL.LANGUAGE_ID = ? ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'GC." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'NL." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'NL.LABEL_ISNATACTIVE = " + M01_LDM.gc_dbFalse + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'NL.LABEL_NATIONAL IS NOT NULL AND LENGTH(NL.LABEL_NATIONAL) > 0 ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'NL.GCO_OID = GC." + M01_Globals.g_anOid + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = v_stmntTxt || 'GC.CDIDIV_OID = ? ';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_previewStmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN clientcur USING languageId_in, v_divisionOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");

M11_LRT.genProcSectionHeader(fileNo, "determine number of affected GenericCodeNlText", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_recordCount = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCodeNlText + " NL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NL.LANGUAGE_ID = languageId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NL." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NL.LABEL_ISNATACTIVE = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NL.LABEL_NATIONAL IS NOT NULL AND LENGTH(NL.LABEL_NATIONAL) > 0 ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXISTS (SELECT 1 FROM " + qualTabNameGenericCode + " GC WHERE GC." + M01_Globals.g_anOid + " = NL.GCO_OID AND GC." + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse + " AND GC.CDIDIV_OID = v_divisionOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if no records are affected, there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_recordCount > 0 THEN");

M11_LRT.genProcSectionHeader(fileNo, "open LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL " + qualLrtBeginProcName + " (?, ?, ?, 0, ? )';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_trNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL SYSPROC.WLM_SET_CLIENT_INFO(cdUserId_in, v_lrtOid, psOid_in, NULL, NULL);");

M11_LRT.genProcSectionHeader(fileNo, "set LRT comment", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + "_NL_TEXT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NEXTVAL FOR " + M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null) + ", v_lrtOid, 1,  'Nationale Codebezeichnung für die Sprache '  || RTRIM( languageId_in ) || ' wurde aktiviert', psOid_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "update GenericCode_NlText", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =                ' UPDATE " + qualViewNameGenericCodeNlText + " NL';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' SET ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   LABEL_ISNATACTIVE = 1,';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   VERSIONID = VERSIONID + 1';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' WHERE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   NL.LANGUAGE_ID = ?';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   NL.LABEL_ISNATACTIVE = 0';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' AND  ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   NL.LABEL_NATIONAL IS NOT NULL';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   EXISTS (SELECT 1 FROM " + qualTabNameGenericCode + " GC WHERE GC.OID = NL.GCO_OID AND GC.ISDELETED = 0 AND GC.CDIDIV_OID = ?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "  v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "  languageId_in, v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "update GenericCode", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =                ' UPDATE " + qualViewNameGenericCode + " GC';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' SET ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   UPDATEUSER = ?,';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   LASTUPDATETIMESTAMP = CURRENT TIMESTAMP,';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   VERSIONID = VERSIONID + 1';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ' WHERE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   GC.CDIDIV_OID = ?';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '  AND ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '   EXISTS (SELECT 1 FROM " + qualTabNameGenericCodeNlText + "  NL WHERE NL.AHOID = GC.OID AND NL.INLRT = ?)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "  v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "  cdUserId_in, v_divisionOid, v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
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


private static void genDataFixSupportUtils(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, null, null, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    SP for 'transactional securely' executing Data Fix scripts
// ####################################################################################################################

String qualProcedureNameSetApplVersion;
qualProcedureNameSetApplVersion = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnSetApplVersion, ddlType, null, null, null, null, null, null);

String qualProcedureNameDropObjects;
qualProcedureNameDropObjects = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnDropObjects, ddlType, null, null, null, null, null, null);

String qualProcedureNameDfxExecute;
qualProcedureNameDfxExecute = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDfxExecute, ddlType, null, null, null, null, null, null);

String schemaNameDataFix;
schemaNameDataFix = M04_Utilities.genSchemaName(M01_ACM_IVK.snDataFix, M01_ACM_IVK.ssnDataFix, ddlType, null, null);
M22_Class_Utilities.printSectionHeader("SP for 'transactional securely' executing Data Fix scripts", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameDfxExecute);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "dfxProcName_in", "VARCHAR(100)", true, "unqualified name of the DFX-routine to execute");
M11_LRT.genProcParm(fileNo, "IN", "version_in", "VARCHAR(20)", true, "version-info");
M11_LRT.genProcParm(fileNo, "IN", "revision_in", "VARCHAR(20)", true, "revision-info");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of LRT if fix is implemented via LRT");
M11_LRT.genProcParm(fileNo, "IN", "onlyOnce_in", M01_Globals.g_dbtBoolean, true, "(optional) if set to '1' register the fix as 'once-only-fix'");
M11_LRT.genProcParm(fileNo, "IN", "description_in", "VARCHAR(100)", true, "description text to store");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records affected by the data fix");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, !(M03_Config.supportSpLogging | ! M03_Config.generateSpLogMessages));
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_error", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_objCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_objFailCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_catchError", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "v_rc", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_msg", "VARCHAR(300)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS EXCEPTION 1 v_msg = DB2_TOKEN_STRING;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_msg = LEFT(TRANSLATE( v_msg, ' ', x'FF' ), " + String.valueOf(M01_LDM.gc_dbMaxSignalMessageLength) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_msg = LEFT(v_msg, " + String.valueOf(M01_LDM.gc_dbMaxSignalMessageLength) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_catchError = " + M01_LDM.gc_dbFalse + " THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameDfxExecute, ddlType, 3, "'dfxProcName_in", "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_error = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcedureNameDfxExecute, ddlType, null, "'dfxProcName_in", "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out", null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameters and variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_catchError = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + schemaNameDataFix + ".' || dfxProcName_in || '(?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "recordCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rc = DB2_RETURN_STATUS;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_catchError = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_error = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualProcedureNameSetApplVersion + "(version_in, revision_in, lrtOid_in, onlyOnce_in, description_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcedureNameDropObjects + "(2, 'PROCEDURE', '" + schemaNameDataFix + "%', dfxProcName_in, NULL, NULL, v_objCount, v_objFailCount);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_error = 1 THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcedureNameDfxExecute, ddlType, 2, "'dfxProcName_in", "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcedureNameDfxExecute, ddlType, 1, "'dfxProcName_in", "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", "'description_in", "recordCount_out", null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_rc;");

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


private static void genDeleteNSR1SupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// only supported in data pools supporting LRT
return;
}

if (thisOrgIndex == M01_Globals.g_primaryOrgId) {
// only supported in non-primary data pools
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String targetSchemaName;
targetSchemaName = M04_Utilities.genSchemaName(M01_ACM.snAlias, M01_ACM.ssnAlias, ddlType, thisOrgIndex, thisPoolIndex);

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String unqualTabNameGenericAspect;
unqualTabNameGenericAspect = M04_Utilities.getUnqualObjName(qualTabNameGenericAspect);
String qualViewNameGenericAspect;
qualViewNameGenericAspect = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, null, null, null, null, null);

String qualTabNameGenericAspectNlText;
qualTabNameGenericAspectNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String unqualTabNameGenericAspectNlText;
unqualTabNameGenericAspectNlText = M04_Utilities.getUnqualObjName(qualTabNameGenericAspectNlText);
String qualViewNameGenericAspectNlText;
qualViewNameGenericAspectNlText = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, true, null, null, null, null);

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualFuncNameSr0;
qualFuncNameSr0 = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAspect, "Sr0Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
String qualFuncNameSr1;
qualFuncNameSr1 = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAspect, "Sr1Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
String qualFuncNameNsr1;
qualFuncNameNsr1 = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexAspect, "Nsr1Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

// ####################################################################################################################
// #    SP to Delete a NSR1
// ####################################################################################################################

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcNameDeleteNSR1;
qualProcNameDeleteNSR1 = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDeleteNSR1, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP to Delete a NSR1", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDeleteNSR1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the Aspect to delete");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "(optional) CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "(optional) logical transaction number - only used if 'lrtOid_inout IS NULL'");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of 'current' Product Structure  - only used if 'lrtOid_inout IS NULL'");
M11_LRT.genProcParm(fileNo, "INOUT", "lrtOid_inout", M01_Globals.g_dbtLrtId, true, "(optional) OID of the LRT used for any data manipulation (may already exist)");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being deleted (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_classIdNsr1", "CHARACTER(5)", "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexNSr1Validity].classIdStr + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_classIdTypePrice", "CHARACTER(5)", "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexTypePriceAssignment].classIdStr + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_parentClassId", "CHARACTER(5)", "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].classIdStr + "'", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_generationCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_typePriceCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "0 ", null, null);
M11_LRT.genVarDecl(fileNo, "v_nlTextCount", "INTEGER", "0 ", null, null);
M11_LRT.genVarDecl(fileNo, "v_countAffectedEntity", "INTEGER", "0 ", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID if LRT is given, if not begin a new LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF lrtOid_inout IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "U." + M01_Globals.g_anUserId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anOid + " = lrtOid_inout");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");
M11_LRT.genProcSectionHeader(fileNo, "make sure we found PS", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_psOid IS NULL THEN");
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_inout))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psoid = psOid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_cdUserId = cdUserId_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualLrtBeginProcName + "(?,?,?,0,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid_inout");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "trNumber_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine number of affected NSR1", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_recordCount = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLASSID = v_classIdNsr1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anIsDeleted + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if no GenericAspects are affected, there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "If v_recordCount = 0 Then");
M79_Err.genSignalDdlWithParms("objNotFound", fileNo, 2, "NSR1", unqualTabNameGenericAspect, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "check if other NSR1 Generations exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_generationCount = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COUNT(*)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspect + " NSR1_1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGenericAspect + " NSR1_2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1_1.NSR1CONTEXT = NSR1_2.NSR1CONTEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1_1.E1VEX1_OID = NSR1_2.E1VEX1_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1_1." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NSR1_1." + M01_Globals.g_anCid + " = v_classIdNsr1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if no other NSR1 Generations exists, check if TypePrices for this NSR1 exist", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "If v_generationCount = 1 Then");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_typePriceCount = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COUNT(DISTINCT TYPEPRICE." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericAspect + " NSR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericAspect + " SR1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NSR1.E1VEX1_OID = SR1." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericAspect + " SR0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SR1.E0VEX0_OID = SR0." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "," + qualTabNameGenericAspect + " TYPEPRICE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NSR1." + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NSR1." + M01_Globals.g_anCid + " = v_classIdNsr1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualFuncNameNsr1 + "(TYPEPRICE." + M01_Globals.g_anOid + ") = " + qualFuncNameNsr1 + "(NSR1." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualFuncNameSr1 + "(TYPEPRICE." + M01_Globals.g_anOid + ") = " + qualFuncNameSr1 + "(SR1." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualFuncNameSr0 + "(TYPEPRICE." + M01_Globals.g_anOid + ") = " + qualFuncNameSr0 + "(SR0." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPEPRICE." + M01_Globals.g_anCid + " = v_classIdTypePrice");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

M11_LRT.genProcSectionHeader(fileNo, "if TypePrices for this NSR1 exist, throw exception", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "If v_typePriceCount > 0 Then");
M79_Err.genSignalDdlWithParms("deleteNotAllowedForReason", fileNo, 3, "NSR1", "TypePrice(s) exist", null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "set environment variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL SYSPROC.WLM_SET_CLIENT_INFO(''' || RTRIM(v_cdUserId) || ''', ''' || RTRIM(CHAR(lrtOid_inout)) || ''', ''' || RTRIM(CHAR(v_psOid)) || ''', NULL, NULL)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "delete GenericAspect via LRT view (marks it with LRTSTATE deleted)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete GENERICASPECT_NL_TEXTs via LRT view (implicitely brings them into private tables and marks it with LRTSTATE deleted)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspectNlText);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GAS_OID = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_nlTextCount = ROW_COUNT;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = v_recordCount + v_nlTextCount;");

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


private static void genAssignCodeCatSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
// only supported at 'pool-level'
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// only supported in data pools supporting LRT
return;
}

//On Error GoTo ErrorExit 

boolean isPrimaryOrg;
isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewNameAcmEntityFkCol;
qualViewNameAcmEntityFkCol = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAcmEntityFkCol, M01_ACM.vnsAcmEntityFkCol, ddlType, null, null, null, null, null, null, null, null, null, null);

String qualTabNameTerm;
qualTabNameTerm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameTermLrt;
qualTabNameTermLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualTabNameExpressionLrt;
qualTabNameExpressionLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualViewNameExpressionLrt;
qualViewNameExpressionLrt = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexExpression, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameGenericAspectLrt;
qualTabNameGenericAspectLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualViewNameGenericAspect;
qualViewNameGenericAspect = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualViewNameEndSlot;
qualViewNameEndSlot = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexEndSlot, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewNameGenericCode;
qualViewNameGenericCode = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualTabNameCategory;
qualTabNameCategory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameCategoryGen;
qualTabNameCategoryGen = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);
String qualTabNameCategoryGenNlText;
qualTabNameCategoryGenNlText = M04_Utilities.genQualNlTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualViewNameCodeCategory;
qualViewNameCodeCategory = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, true, true, null, null, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

// ####################################################################################################################
// #    SP for Re-Assignment of Codes to a Category
// ####################################################################################################################

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcNameAssignCodeCat;
qualProcNameAssignCodeCat = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnAssignCodeCat, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for '(Re)Assignment of Codes to a Category'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameAssignCodeCat);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "codeOid_in", M01_Globals.g_dbtOid, true, "OID of the Code to assign to a category");
M11_LRT.genProcParm(fileNo, "IN", "categoryNewOid_in", M01_Globals.g_dbtOid, true, "OID of the Category to assign the Codes to");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being copied (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

if (!(isPrimaryOrg)) {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, isPrimaryOrg);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
if (!(isPrimaryOrg)) {
M11_LRT.genVarDecl(fileNo, "v_mpcExpCount", "INTEGER", "0", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtForeignOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_unknownCodeOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lockedCodeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumberInUse", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_endSlotOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_endSlotCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_categoryOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_aspectOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_actHeadOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_actElemOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_conHeadOid", M01_Globals.g_dbtOid, "NULL", null, null);

M11_LRT.genVarDecl(fileNo, "v_expLockedOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE codeCursor CURSOR FOR v_stmnt;");

if (!(isPrimaryOrg)) {
M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M82_PSCopy.tempOidMapTabName + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
}

M82_PSCopy.genDdlForTempOidMap(fileNo, null, true, null, null, null);
if (!(isPrimaryOrg)) {
M85_DataFix.genDdlForTempFtoExpOid(fileNo, null, true, null, null);
}
M85_DataFix.genDdlForTempExpOid(fileNo, null, true, null, null);
M85_DataFix.genDdlForTempCodeOid(fileNo, null, isPrimaryOrg, null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameAssignCodeCat, ddlType, null, "codeOid_in", "categoryNewOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTs  = CURRENT TIMESTAMP;");

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, true, 1);

M11_LRT.genProcSectionHeader(fileNo, "verify that we have an active transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_LDM.gc_db2RegVarLrtOid + " = '' THEN");
M79_Err.genSignalDdl("noLrt", fileNo, 2, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_lrtOid  = BIGINT('0' || CURRENT CLIENT_WRKSTNNAME);");

if (!(isPrimaryOrg)) {
M11_LRT.genProcSectionHeader(fileNo, "cleanup Code-OIDs if CodeNumberList or CodeOidList is given", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (codeOid_in IS NOT NULL) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM " + M85_DataFix.tempCodeOidTabName + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID", null, null);
int indent;
indent = 0;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L." + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "U." + M01_Globals.g_anUserId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "v_cdUserId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_qualTabNameUser + " U");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L.UTROWN_OID = U." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L." + M01_Globals.g_anOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure we found PS", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "IF v_psOid IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, -3, "codeOid_in", "categoryNewOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, indent + 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_lrtOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine Division-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_psoid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "insert Code-OID to temp table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCode);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid + " = codeOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_unknownCodeOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_unknownCodeOid IS NOT NULL THEN");
M79_Err.genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_unknownCodeOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that no LRT of the given PS refers to any of the Codes", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " <> AFK.REFENTITYID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colConditions = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.FKCOL AS c_fkCol");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " = c_tabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FKCOL ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' AND ' END) || '(C.' || RTRIM(c_fkCol) || ' = T.oid)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_codeNumberInUse = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT C." + M01_Globals.g_anInLrt + ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C INNER JOIN " + M85_DataFix.tempCodeOidTabName + " T ON ' || v_colConditions || ' WHERE C." + M01_Globals.g_anInLrt + " <> ' || RTRIM(CHAR(v_lrtOid)) || ' AND " + M01_Globals_IVK.g_anPsOid + " = ' || v_psOid;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN codeCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH codeCursor INTO v_lrtForeignOid, v_codeNumberInUse;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE codeCursor WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_codeNumberInUse IS NOT NULL THEN");
M79_Err.genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, null, null, null, null, null, null, null, null, null, "v_codeNumberInUse", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtForeignOid))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure a Category-OID is given", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_categoryOid = categoryNewOid_in;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_categoryOid IS NULL THEN");
M79_Err.genSignalDdlWithParms("catNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that Category uniquely defines an EndSlot", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "eslOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY eslOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_endSlotOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_endSlotCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "E." + M01_Globals.g_anOid + " AS eslOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT E." + M01_Globals.g_anOid + " FROM " + qualViewNameEndSlot + " E WHERE E." + M01_Globals_IVK.g_anPsOid + " = v_psOid AND E.ESCESC_OID = v_categoryOid AND E." + M01_Globals_IVK.g_anIsDeleted + " = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") V_Esl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUMBER() OVER (ORDER BY eslOid) DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_endSlotOid IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, -2, "codeOid_in", "categoryNewOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("eslNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_categoryOid))", null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

if (!(isPrimaryOrg)) {
M11_LRT.genProcSectionHeader(fileNo, "retrieve Expression-OIDs from FTO", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempFtoExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameExpressionLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "retrieve Expression-OIDs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTerm + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.oid = T.CCRCDE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (SELECT 1 FROM " + M85_DataFix.tempFtoExpOidTabName + " E WHERE T." + M01_Globals.g_anAhOid + " = E.oid)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that none of the Expressions is involved in some LRT in this ProductStructure (PS included for performance)", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_expLockedOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameExpressionLrt + " EXP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempExpOidTabName + " EXPOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXPOID.oid = EXP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXP." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXP." + M01_Globals.g_anInLrt + " <> v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_expLockedOid IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameAssignCodeCat, ddlType, null, "codeOid_in", "categoryNewOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M79_Err.genSignalDdlWithParms("expLocked", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_expLockedOid))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_endSlotCount = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "Category has unique EndSlot", 2, null);
M11_LRT.genProcSectionHeader(fileNo, "loop over all Expression-forming tables and copy data into LRT-tables", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabCursor AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anPdmFkSchemaName + " AS v_tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anPdmTableName + " AS v_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " LT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anAcmEntitySection + " = AE." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anAcmEntityName + " = AE." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anAcmEntityType + " = AE." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anPdmLdmFkSchemaName + " = LT." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anPdmLdmFkTableName + " = LT." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(thisPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");

boolean firstEntity;
firstEntity = true;

int c;
for (int c = 1; c <= M22_Class.g_classes.numDescriptors; c++) {
if ((M22_Class.g_classes.descriptors[c].superClassIndex <= 0) &  M22_Class.g_classes.descriptors[c].isSubjectToExpCopy) {
if (!(firstEntity)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntitySection + " = '" + M22_Class.g_classes.descriptors[c].sectionName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntityName + " = '" + M22_Class.g_classes.descriptors[c].className.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
firstEntity = false;
}
}
int r;
for (int r = 1; r <= M23_Relationship.g_relationships.numDescriptors; r++) {
if (M23_Relationship.g_relationships.descriptors[r].implementsInOwnTable &  M23_Relationship.g_relationships.descriptors[r].isSubjectToExpCopy) {
if (!(firstEntity)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyRel + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntitySection + " = '" + M23_Relationship.g_relationships.descriptors[r].sectionName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AE." + M01_Globals.g_anAcmEntityName + " = '" + M23_Relationship.g_relationships.descriptors[r].relName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
firstEntity = false;
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anLdmIsGen + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LT." + M01_Globals.g_anLdmIsNl + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'CALL ' || v_tabSchema || '.EXPCP2LRT_' || v_tabName || '(?,?,?,?)' ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_lrtOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_currentTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

if (!(isPrimaryOrg)) {
M11_LRT.genProcSectionHeader(fileNo, "retrieve Expression-OIDs from FTO", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameExpressionLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ISINVALID = " + String.valueOf(M01_LDM.gc_dbTrue));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "update EndSlot-references in Terms involving re-assigned Codes", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTermLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ESLESL_OID = v_endSlotOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anInLrt + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CCRCDE_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "update EndSlot-references in GenericAspects having re-assigned Codes as 'BaseCode'", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BESESL_OID = v_endSlotOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "BCDBCD_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbTrue);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, null, null, M01_Common.DdlOutputMode.edomNone, null);

M11_LRT.genProcSectionHeader(fileNo, "update re-mapped Expression-references in GenericAspects", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameGenericAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
boolean includeOr;
int numExpressions;
int i;
numExpressions = 0;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) != 0 &  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) {
numExpressions = numExpressions + 1;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tabColumns.descriptors[i].columnName + " = COALESCE((SELECT map2Oid FROM SESSION.OidMap WHERE oid = " + tabColumns.descriptors[i].columnName + ")," + tabColumns.descriptors[i].columnName + "),");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusWorkInProgress) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anUpdateUser + " = v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anLastUpdateTimestamp + " = v_currentTs,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anVersionId + " = " + M01_Globals.g_anVersionId + " + 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");

includeOr = false;
numExpressions = 0;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) != 0 &  (tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) {
numExpressions = numExpressions + 1;
if (includeOr) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
}
includeOr = true;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + tabColumns.descriptors[i].columnName + " IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
if (!(isPrimaryOrg)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anIsNational + " = " + M01_LDM.gc_dbTrue);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M85_DataFix.genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Globals_IVK.g_classIndexActionHeading, "v_actHeadOid", isPrimaryOrg, qualProcNameAssignCodeCat);
M85_DataFix.genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Globals_IVK.g_classIndexActionElement, "v_actElemOid", isPrimaryOrg, qualProcNameAssignCodeCat);
M85_DataFix.genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Globals_IVK.g_classIndexConditionHeading, "v_condHeadOid", isPrimaryOrg, qualProcNameAssignCodeCat);
M85_DataFix.genDdlForExpEntityLockAndUpdate(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Globals_IVK.g_classIndexTaxParameter, "v_taxOid", isPrimaryOrg, qualProcNameAssignCodeCat);

if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M11_LRT.genProcSectionHeader(fileNo, "register all relevant entities as being affected by the LRT", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ExtraEntities");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "opId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES ('" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexExpression].classIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', " + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
if (isPrimaryOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES ('" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericCode].classIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', " + String.valueOf(M11_LRT.lrtStatusLocked) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
//End If
//Print #fileNo, addTab(3); "VALUES ('"; g_classes.descriptors(g_classIndexGenericAspect).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "', "; CStr(lrtStatusUpdated); ")"
//If isPrimaryOrg Then
//Print #fileNo, addTab(3); "UNION ALL"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES ('" + M23_Relationship.g_relationships.descriptors[M01_Globals_IVK.g_relIndexCodeCategory].relIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "', " + String.valueOf(M11_LRT.lrtStatusUpdated) + ")");
}

int j;
for (int j = 1; j <= M22_Class.g_classes.numDescriptors; j++) {
if ((M22_Class.g_classes.descriptors[j].superClassIndex <= 0) &  M22_Class.g_classes.descriptors[j].isSubjectToExpCopy) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES ('" + M22_Class.g_classes.descriptors[j].classIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyClass + "', " + String.valueOf(M11_LRT.lrtStatusCreated) + ")");
}
}

for (int j = 1; j <= M23_Relationship.g_relationships.numDescriptors; j++) {
if (M23_Relationship.g_relationships.descriptors[j].implementsInOwnTable &  M23_Relationship.g_relationships.descriptors[j].isSubjectToExpCopy) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES ('" + M23_Relationship.g_relationships.descriptors[j].relIdStr + "', '" + M01_Globals.gc_acmEntityTypeKeyRel + "', " + String.valueOf(M11_LRT.lrtStatusCreated) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, "PSE.", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtOid, "v_lrtOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conAcmOrParEntityId, "PSE." + M01_Globals.g_anAcmEntityId, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L." + M01_Globals.g_anLdmIsGen + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "L." + M01_Globals.g_anLdmIsNl + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V.opId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameAcmEntity + " A");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "V_ExtraEntities V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityId + " = V.entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityType + " = V.entityType");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") PSE");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameLrtAffectedEntity + " AE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anLrtOid + " = v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + M01_Globals.g_anAcmOrParEntityId + " = PSE." + M01_Globals.g_anAcmEntityId);

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conAcmOrParEntityId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 5, null, null, M01_Common.DdlOutputMode.edomNone, null);

int col;
for (int col = 1; col <= tabColumns.numDescriptors; col++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AE." + tabColumns.descriptors[col].columnName + " = PSE." + tabColumns.descriptors[col].columnName);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}

M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "If Category has multiple EndSlots then only make Expressions invalid - User needs to respond to 2nd Lvl Text", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameExpressionLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ISINVALID = " + String.valueOf(M01_LDM.gc_dbTrue));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OID IN (SELECT oid FROM " + M85_DataFix.tempExpOidTabName + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameAssignCodeCat, ddlType, null, "codeOid_in", "categoryNewOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

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


private static void genCheckAffectedObjectsByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
// Check Affected Objects for AssignCodeCategory is only supported at 'pool-level'
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Check Affected Objects for AssignCodeCategory is only supported in data pools supporting LRT
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameChangeLogFactoryProd;
qualTabNameChangeLogFactoryProd = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null, null, null, null);

String qualTabNameCategoryFactoryProd;
qualTabNameCategoryFactoryProd = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCategory, ddlType, M01_Globals.g_primaryOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGeneralSettings;
qualTabNameGeneralSettings = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameUser;
qualTabNameUser = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexUser, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTerm;
qualTabNameTerm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualViewNameAcmEntityFkCol;
qualViewNameAcmEntityFkCol = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAcmEntityFkCol, M01_ACM.vnsAcmEntityFkCol, ddlType, null, null, null, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    SP for Checking Affected Objects for AssignCodeCategory
// ####################################################################################################################

String qualProcNameCheckAffectedObjects;
String qualProcNameCheckAffectedObjectsIntern;
qualProcNameCheckAffectedObjects = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnCheckAffectedObjects, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameCheckAffectedObjectsIntern = qualProcNameCheckAffectedObjects;
M22_Class_Utilities.printSectionHeader("SP for Checking if Affected Objects for AssignCodeCategory are locked/exist in other LRTs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameCheckAffectedObjectsIntern);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", true, "(optional) list of Code-Numbers to assign to a category");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of 'current' Product Structure  - only used if 'lrtOid_in IS NULL'");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", "BIGINT", false, "(optional) OID of the LRT used for any data manipulation (may already exist)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DYNAMIC RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(3000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_restmntTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_unknownCodeNumber", "VARCHAR(15)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);
M11_LRT.genVarDecl(fileNo, "v_restmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;");

M85_DataFix.genDdlForTempExpOid(fileNo, null, true, null, null);
M85_DataFix.genDdlForTempCodeOid(fileNo, null, true, null, null, null);
M85_DataFix.genDdlForTempAffectedObjects(fileNo, null, true, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine PS-OID if LRT is given", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF lrtOid_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure we found PS", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_psOid IS NULL THEN");
M79_Err.genSignalDdlWithParms("lrtNotExist", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(lrtOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psOid = psOid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine Division-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve Code-numbers from list - if given", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF codeNumberList_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(LTRIM(E.elem))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.elem IS NOT NULL AND E.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "map OIDs to Code-numbers (using REPEATABLE READ)", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempCodeOidTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.oid = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "C." + M01_Globals_IVK.g_anCodeNumber + " = T.codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH RR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_unknownCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_unknownCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameCheckAffectedObjectsIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberNotKnown", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_unknownCodeNumber))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");

if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameCheckAffectedObjectsIntern, ddlType, 2, "'codeNumberList_in", "psOid_in", "lrtOid_in", null, null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("illegParam", fileNo, 2, "codeNumberList_in", null, null, null, null, null, null, null, null, "RTRIM(CHAR(codeNumberList_in))", null, null, null);
} else {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "baseCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameChangeLogFactoryProd + " CL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameGeneralSettings + " GS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.PS_OID = GS.PS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OPTIMESTAMP > GS.LASTCENTRALDATATRANSFERCOMMIT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.PS_OID = v_psoid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.DBTABLENAME = 'CODECATEGORY'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.DBCOLUMNNAME = 'CAT_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CL.OLDVALUEBIGINT <> (SELECT OID FROM " + qualTabNameCategoryFactoryProd + " WHERE ISDEFAULT = 1 AND PS_OID = v_psoid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that no LRT of current PS refers to any of the Codes", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " <> AFK.REFENTITYID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colConditions = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.FKCOL AS c_fkCol");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " = c_tabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FKCOL ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' OR ' END) || 'C.' || RTRIM(c_fkCol) || ' IN (SELECT T.oid FROM " + M85_DataFix.tempCodeOidTabName + " T)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M11_LRT.genProcSectionHeader(fileNo, "determine locked affected objects", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'INSERT INTO " + M85_DataFix.tempAffectedObjectsTabName + " (oid, classid, inLrt) SELECT DISTINCT C.AHOID, C.AHCLASSID, C.INLRT FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE C.INLRT IS NOT NULL AND (' || v_colConditions || ')';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_colConditions");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve Expression-OIDs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempExpOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTerm + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.oid = T.CCRCDE_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M11_LRT.genProcSectionHeader(fileNo, "check GenericAspects referring to mapped Expressions", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexExpression) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " <> AFK.REFENTITYID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(A.ENTITYID <> '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexTaxParameter) + "' OR (L.ISGEN = 1 AND A.ENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexTaxParameter) + "'))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_tabName = '" + qualTabNameGenericAspect.substring(M00_Helper.inStr(1, qualTabNameGenericAspect, ".") + 1 - 1, M00_Helper.inStr(1, qualTabNameGenericAspect, ".") + 1 + qualTabNameGenericAspect.length() - 1) + "' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = 'C.VALEXP_OID_NATIONAL IN (SELECT OID FROM " + M85_DataFix.tempExpOidTabName + ")';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.FKCOL AS c_fkCol");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexExpression) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " = c_tabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FKCOL ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' OR ' END) || '(C.' || RTRIM(c_fkCol) || ' IN (SELECT oid FROM " + M85_DataFix.tempExpOidTabName + "))';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M11_LRT.genProcSectionHeader(fileNo, "determine locked affected objects", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_tabSchemaName = '" + M04_Utilities.genSchemaName(M01_ACM_IVK.snDecision, M01_ACM_IVK.ssnDecision, ddlType, thisOrgIndex, thisPoolIndex) + "' THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'INSERT INTO " + M85_DataFix.tempAffectedObjectsTabName + " (oid, classid, inLrt) SELECT DISTINCT GA.AHOID, GA.AHCLASSID, GA.INLRT FROM VL6CASP011.GENERICASPECT GA WHERE GA.AHOID IN (SELECT DISTINCT C.AHOID FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE PS_OID = ' || v_psOid || ' AND (' || v_colConditions || ')) AND INLRT IS NOT NULL';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'INSERT INTO " + M85_DataFix.tempAffectedObjectsTabName + " (oid, classid, inLrt) SELECT DISTINCT C.AHOID, C.AHCLASSID, C.INLRT FROM ' || RTRIM(c_tabSchemaName) || '.' || RTRIM(c_tabName) || ' C WHERE PS_OID = ' || v_psOid || ' AND INLRT IS NOT NULL AND (' || v_colConditions || ')';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempAffectedObjectsTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "tr = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L.TRNUMBER ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.inLrt = L.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "tr IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempAffectedObjectsTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "owner = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "U.CDUSERID ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameLrt + " L ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameUser + " U ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "U.OID = L.UTROWN_OID ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S.inLrt = L.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "owner IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_restmntTxt = 'SELECT DISTINCT oid, classid, owner, tr FROM " + M85_DataFix.tempAffectedObjectsTabName + "';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_restmnt FROM v_restmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN c_return;");

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

private static void genModifyCodeTypeSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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
// Modification of CodeType is only supported at 'pool-level'
return;
}

if ((thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// Modification of CodeType is only supported in factory
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Modification of CodeType only supported in data pools supporting LRT
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexCode, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
String qualTabNameLrtNlText;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameLrtNlText = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameGenericCodeLrt;
qualTabNameGenericCodeLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualProcNameSetLock;
qualProcNameSetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "OTHERS", null, null);
String qualProcNameResetLock;
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "OTHERS", null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

// ####################################################################################################################
// #    SP for Modifying the Type of Codes
// ####################################################################################################################

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcNameModifyCodeType;
String qualProcNameModifyCodeTypeIntern;
qualProcNameModifyCodeType = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnModifyCodeType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
qualProcNameModifyCodeTypeIntern = qualProcNameModifyCodeType;
M22_Class_Utilities.printSectionHeader("SP for 'Modifying the Type of Codes'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameModifyCodeTypeIntern);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", true, "list of Code-Numbers to modify");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", true, "logical transaction number");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of 'current' Product Structure");
M11_LRT.genProcParm(fileNo, "OUT", "lrtOid_out", M01_Globals.g_dbtOid, true, "OID of the LRT used for any data manipulation (implicitly opened)");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being affected (sum over all tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_currentTs", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_codeNumberIllegalType", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lockedCodeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_unknownCodeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dataPoolDescrStringWdp", "VARCHAR(4000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_dataPoolDescrStringPdp", "VARCHAR(4000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisAccessMode", M01_Globals.g_dbtEnumId, "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_numPs", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspError", "VARCHAR(256)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M85_DataFix.genDdlForTempCodeOid(fileNo, null, true, true, true, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, null, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET lrtOid_out   = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_currentTs  = CURRENT TIMESTAMP;");

M11_LRT.genProcSectionHeader(fileNo, "determine Division-OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that we found Division", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_divisionOid IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("psNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve Code-numbers from list", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(LTRIM(E.elem))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.elem IS NOT NULL AND E.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if no code number is given there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (SELECT COUNT(*) FROM " + M85_DataFix.tempCodeOidTabName + ") = 0 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("procParamEmpty", fileNo, 2, "codeNumberList_in", null, null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "map OIDs to Code-numbers (using REPEATABLE READ)", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.oid = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anCodeNumber + " = T.codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH RR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_unknownCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_unknownCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_unknownCodeNumber))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Codes are \"HilfsCode\"", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S.codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_codeNumberIllegalType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " GC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC." + M01_Globals.g_anOid + " = S." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GC.CTYTYP_OID <> 128");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_codeNumberIllegalType IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNotHilfsCode", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_codeNumberIllegalType))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of Organization", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT ORGOID INTO v_orgOid FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE ID = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true) + " WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "determine data pool descriptor string for all ProductStructures in division", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dataPoolDescrStringWdp = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_dataPoolDescrStringPdp = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_numPs = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR psLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " AS c_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_numPs = v_numPs + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_dataPoolDescrStringWdp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_dataPoolDescrStringWdp ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE v_dataPoolDescrStringWdp WHEN '' THEN '' ELSE '|' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(CHAR(v_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(" + String.valueOf(M01_Globals.g_workDataPoolId) + "));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_dataPoolDescrStringPdp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_dataPoolDescrStringPdp ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(CASE v_dataPoolDescrStringPdp WHEN '' THEN '' ELSE '|' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(CHAR(v_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(" + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + "));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "loop over work and productive data pools and lock", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameSetLock + "(?,' || '''<admin>'', ? ,' || '''update code type'', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_thisAccessMode = " + String.valueOf(M01_Globals.g_workDataPoolId) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_thisAccessMode IS NOT NULL DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_thisAccessMode = " + String.valueOf(M01_Globals.g_workDataPoolId) + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringWdp, cdUserId_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringPdp, cdUserId_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "insist on data pools being locked", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_numDataPools <> v_numPs THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("setRel2ProdLocksFail", fileNo, 3, "OTHER", null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_orgOid))", "RTRIM(CHAR(v_thisAccessMode))", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisAccessMode = (CASE v_thisAccessMode WHEN " + String.valueOf(M01_Globals.g_workDataPoolId) + " THEN " + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + " ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

M11_LRT.genProcSectionHeader(fileNo, "begin a new LRT", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualLrtBeginProcName + "(?,?,?,0,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "trNumber_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "lock Codes", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anInLrt + " = lrtOid_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid + " IN (SELECT oid FROM " + M85_DataFix.tempCodeOidTabName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Codes are locked by this LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals_IVK.g_anCodeNumber);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lockedCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anInLrt + " <> lrtOid_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "C." + M01_Globals.g_anOid + " IN (SELECT oid FROM " + M85_DataFix.tempCodeOidTabName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lockedCodeNumber IS NOT NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("lrtLockAlreadyLockedDetail", fileNo, 2, qualTabNameGenericCode, null, null, null, null, null, null, null, null, "v_lockedCodeNumber", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "copy public Codes to private / update CodeType", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCodeLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 7, null, null, null, "PUB.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusUpdated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, "CTYTYP_OID", "100", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conLastUpdateTimestamp, "v_currentTs", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conVersionId, "PUB." + M01_Globals.g_anVersionId + " + 1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexGenericCode, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericCode + " PUB");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB.CDIDIV_OID = v_divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PUB." + M01_Globals.g_anInLrt.toUpperCase() + " = lrtOid_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (M03_Config.lrtDistinguishGenAndNlTextTabsInAffectedEntities) {
M11_LRT.genProcSectionHeader(fileNo, "register all relevant entities as being affected by the LRT", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtAffectedEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, null, null, "PUB.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtOid, "lrtOid_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conAcmOrParEntityId, "'" + String.valueOf(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericCode].classIdStr) + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conAcmEntityType, "'" + M01_Globals.gc_acmEntityTypeKeyClass + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLdmIsGen, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conLdmIsNl, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLrtOpId, String.valueOf(M11_LRT.lrtStatusUpdated), null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals.g_classIndexLrtAffectedEntity, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, null, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
} else {
// ???
}

M11_LRT.genProcSectionHeader(fileNo, "create LRT-comment", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrtNlText);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals.g_classIndexLrt, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, qualTabNameLrtNlText, null, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].shortName, null, null, null, null), "lrtOid_out", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLanguageId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conTransactionComment, "'Map CODETYPE: Hilfscode -> Ausstattungscode'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "1", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM_IVK.conPsOid, "psOid_in", null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(M01_Globals.g_classIndexLrt, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");

String qualProcNameLrtCommit;
qualProcNameLrtCommit = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "commit LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameLrtCommit + "(?,0,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspError,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspInfo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_gwspWarning");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "lrtOid_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "loop over work and productive data pools and unlock", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt = 'CALL " + qualProcNameResetLock + "(?,' || '''<admin>'', ? ,' || '''update code type'', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_thisAccessMode = " + String.valueOf(M01_Globals.g_workDataPoolId) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_thisAccessMode IS NOT NULL DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_thisAccessMode = " + String.valueOf(M01_Globals.g_workDataPoolId) + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringWdp, cdUserId_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE v_stmnt INTO v_numDataPools USING v_dataPoolDescrStringPdp, cdUserId_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "insist on data pools being unlocked", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_numDataPools <> v_numPs THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("resetRel2ProdLocksFail", fileNo, 3, "OTHER", null, null, null, null, null, null, null, null, "RTRIM(CHAR(v_orgOid))", "RTRIM(CHAR(v_thisAccessMode))", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisAccessMode = (CASE v_thisAccessMode WHEN " + String.valueOf(M01_Globals.g_workDataPoolId) + " THEN " + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + " ELSE NULL END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameModifyCodeTypeIntern, ddlType, 1, "'codeNumberList_in", "'cdUserId_in", "trNumber_in", "psOid_in", "lrtOid_out", "rowCount_out", null, null, null, null, null, null);

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


private static void genDeleteProdCodeSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M03_Config.supportSectionDataFix)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// Modification of CodeType is only supported at 'pool-level'
return;
}

if ((thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// Modification of CodeType is only supported in factory
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Modification of CodeType only supported in data pools supporting LRT
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

String qualTabNameLrt;
String qualTabNameLrtNlText;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
qualTabNameLrtNlText = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericCode;
qualTabNameGenericCode = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String qualTabNameGenericCodeLrt;
qualTabNameGenericCodeLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualViewNameAcmEntityFkCol;
qualViewNameAcmEntityFkCol = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnAcmEntityFkCol, M01_ACM.vnsAcmEntityFkCol, ddlType, null, null, null, null, null, null, null, null, null, null);

String qualProcNameSetLock;
qualProcNameSetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnSetRel2ProdLock, ddlType, null, null, null, "EXCLUSIVEWRITE", null, null);
String qualProcNameResetLock;
qualProcNameResetLock = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMeta, M01_ACM_IVK.spnResetRel2ProdLock, ddlType, null, null, null, "EXCLUSIVEWRITE", null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

String qualProcNameGenWs;
qualProcNameGenWs = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGenWorkspaceWrapper, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    SP for Deleteting a Set of Productive Codes
// ####################################################################################################################

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcNameDeleteProdCode;
qualProcNameDeleteProdCode = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDeleteProductiveCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
M22_Class_Utilities.printSectionHeader("SP for 'Deleteting a Set of Productive Codes'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDeleteProdCode);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "codeNumberList_in", "CLOB(1M)", true, "list of Code-Numbers to delete");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "divOid_in", M01_Globals.g_dbtOid, true, "OID of Division");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of Codes being delete");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "fkViolationOnDelete", "23504", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_processedCodeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_thisCodeNumber", M01_Globals_IVK.g_dbtCodeNumber, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_colConditions", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_numDataPools", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabNameChangeLog", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabNameGenericCode", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_callCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE codeCursor CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR fkViolationOnDelete");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M11_LRT.genProcSectionHeader(fileNo, "declare variables", 2, true);

M11_LRT.genVarDecl(fileNo, "v_tabSchema", M01_Globals.g_dbtDbSchemaName, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_tabName", M01_Globals.g_dbtDbTableName, "NULL", 2, null);
M11_LRT.genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL", 2, null);

M11_LRT.genProcSectionHeader(fileNo, "retrieve diagnostics string", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;");

M11_LRT.genProcSectionHeader(fileNo, "if we are not currently processing a Code we do not process exception message", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_processedCodeNumber IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "parse diagnostics string", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR elemLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELEM AS c_elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "POSINDEX AS c_pos");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(v_diagnostics, CAST('.' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELEM IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF     c_pos = 0 THEN SET v_tabSchema = COALESCE(CAST(c_elem AS " + M01_Globals.g_dbtDbSchemaName + "),'??');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF c_pos = 1 THEN SET v_tabName   = COALESCE(CAST(c_elem AS VARCHAR(50)),'??');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "signal MDS-message", 2, null);
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, -2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("fkViolationOnDelete", fileNo, 2, "Code", null, null, null, null, null, null, null, null, "v_processedCodeNumber", "v_tabSchema || '.' || v_tabName", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M85_DataFix.genDdlForTempCodeOid(fileNo, null, true, true, true, true);
M85_DataFix.genDdlForTempDataPool(fileNo, null, true, true, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDeleteProdCode, ddlType, null, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that Division is valid", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameDivision + " WHERE " + M01_Globals.g_anOid + " = divOid_in) IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("divNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(divOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "make sure that CD-User is valid", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (SELECT 1 FROM " + M01_Globals.g_qualTabNameUser + " WHERE " + M01_Globals.g_anUserId + " = cdUserId_in) IS NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("userUnknown", fileNo, 2, null, null, null, null, null, null, null, null, null, "cdUserId_in", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve Code-numbers from list", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(LTRIM(E.elem))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(codeNumberList_in, CAST(',' AS CHAR(1)))) AS E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.elem IS NOT NULL AND E.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if no code number is given there is nothing to do", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (SELECT COUNT(*) FROM " + M85_DataFix.tempCodeOidTabName + ") = 0 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("procParamEmpty", fileNo, 2, "codeNumberList_in", null, null, null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "map OIDs to Code-numbers", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.hasBeenSetProductive");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anHasBeenSetProductive);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericCode + " C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C.CDIDIV_OID = divOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "C." + M01_Globals_IVK.g_anCodeNumber + " = T.codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Code-numbers map to OIDs", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_thisCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_thisCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberNotKnown", fileNo, 2, null, null, null, null, null, null, null, null, null, "v_thisCodeNumber", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "verify that all Code-numbers are productive", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_thisCodeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(hasBeenSetProductive, " + M01_LDM.gc_dbFalse + ") = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH FIRST 1 ROW ONLY;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_thisCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberNotProductive", fileNo, 2, null, null, null, null, null, null, null, null, null, "v_thisCodeNumber", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine work data pools holding at least one of the referred Codes", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " AS c_poolTypeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + String.valueOf(M01_Globals.g_workDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "check if this data pool holds at least one of the referred Codes", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisCodeNumber = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT T.codeNumber FROM ' || c_tabSchemaName || '.' || c_tabName || ' C INNER JOIN " + M85_DataFix.tempCodeOidTabName + " T ON C." + M01_Globals.g_anOid + " = T.oid FETCH FIRST 1 ROW ONLY';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN codeCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH codeCursor INTO v_thisCodeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE codeCursor WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_thisCodeNumber IS NOT NULL THEN");
M11_LRT.genProcSectionHeader(fileNo, "keep track of associated data pools (assume that (pre)productive pool holds Code if work data pool does)", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S." + M01_Globals.g_anPoolTypeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmPrimarySchema + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S." + M01_Globals.g_anOrganizationId + " = c_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "S." + M01_Globals.g_anPoolTypeId + " IN (" + String.valueOf(M01_Globals.g_workDataPoolId) + "," + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PS.PDIDIV_OID = divOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that none of the Codes itself is involved in some LRT", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID IN (SELECT orgId FROM " + M85_DataFix.tempDataPoolTabName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisCodeNumber = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT C." + M01_Globals.g_anInLrt + ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || c_tabName || ' C INNER JOIN " + M85_DataFix.tempCodeOidTabName + " T ON C." + M01_Globals.g_anOid + " = T.oid';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN codeCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH codeCursor INTO v_lrtOid, v_thisCodeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE codeCursor WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_thisCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, null, null, null, null, null, null, null, null, null, "v_thisCodeNumber", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtOid))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that no LRT refers to any of the Codes", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityId + " = A." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = A." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAhCid + " <> AFK.REFENTITYID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID IN (SELECT orgId FROM " + M85_DataFix.tempDataPoolTabName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "check each foreign key column referring to Code", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_colConditions = '';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR colLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.FKCOL AS c_fkCol");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewNameAcmEntityFkCol + " AFK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYTYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AFK.REFENTITYID = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = c_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " = c_tabSchemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " = c_tabName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FKCOL ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_colConditions = v_colConditions || (CASE v_colConditions WHEN '' THEN '' ELSE ' AND ' END) || '(C.' || RTRIM(c_fkCol) || ' = T.oid)';");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "verify that none of the Codes is found in this table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_thisCodeNumber = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'SELECT C." + M01_Globals.g_anInLrt + ",T.codeNumber  FROM ' || RTRIM(c_tabSchemaName) || '.' || c_tabName || ' C INNER JOIN " + M85_DataFix.tempCodeOidTabName + " T ON ' || v_colConditions;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN codeCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH codeCursor INTO v_lrtOid, v_thisCodeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE codeCursor WITH RELEASE;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_thisCodeNumber IS NOT NULL THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("codeNumberInLrt", fileNo, 3, null, null, null, null, null, null, null, null, null, "v_thisCodeNumber", "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(v_lrtOid))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "exclusively lock each involved data pool", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR poolLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid AS c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId AS c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "lock this data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameSetLock + "(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',''<admin>'', ? ,''delete productive Codes'', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE v_stmnt INTO v_numDataPools USING cdUserId_in;");

M11_LRT.genProcSectionHeader(fileNo, "insist on data pool being locked", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_numDataPools <> 1 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("setRel2ProdLockFail", fileNo, 3, "EXCLUSIVEWRITE", null, null, null, null, null, null, null, null, "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "loop over organizations and accessmodes to delete Codes", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR poolLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId AS c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine ChangeLog- and GenericCode-table for this data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RTRIM(P." + M01_Globals.g_anPdmFkSchemaName + ") || '.' || P." + M01_Globals.g_anPdmTableName + " AS c_qualTabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE A." + M01_Globals.g_anAcmEntityId + " WHEN '" + M22_Class.getClassIdStrByIndex(M01_Globals.g_classIndexChangeLog) + "' THEN 1 " + "WHEN '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "' THEN 2 " + "ELSE 4 END) AS c_seqNo");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityId + " IN (" + "'" + M22_Class.getClassIdStrByIndex(M01_Globals.g_classIndexChangeLog) + "', " + "'" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = c_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPoolTypeId + " = c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF c_seqNo = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_tabNameChangeLog = c_qualTabName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSEIF c_seqNo = 2 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_tabNameGenericCode = c_qualTabName;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "create changelog entries for Codes to delete", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_tabNameChangeLog ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'OID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals.g_anAcmEntityId + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals.g_anAcmEntityType + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals.g_anAhCid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AHOBJECTID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'GEN,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'NL,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'DBTABLENAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'OBJECTID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals_IVK.g_anValidFrom + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals_IVK.g_anValidTo + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'BASECODENUMBER,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'BASECODETYPE,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CODEKIND_ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'DIVISIONOID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'OPERATION_ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'OPTIMESTAMP,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals.g_anUserId + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'NEXTVAL FOR " + M01_Globals.g_schemaNameCtoMeta + "' || RIGHT(DIGITS(c_orgId), 2) || '." + M04_Utilities.getUnqualObjName(qualSeqNameOid) + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals.g_anCid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''" + M01_Globals.gc_acmEntityTypeKeyClass + "'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals.g_anAhCid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals.g_anAhOid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'0,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'0,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''" + M04_Utilities.getUnqualObjName(qualTabNameGenericCode) + "'',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals.g_anOid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals_IVK.g_anValidFrom + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals_IVK.g_anValidTo + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals_IVK.g_anCodeNumber + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(SELECT T.CODETYPENUMBER FROM " + M01_Globals_IVK.g_qualTabNameCodeType + " T WHERE T." + M01_Globals.g_anOid + " = CDE.CTYTYP_OID),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(CASE CDE." + M01_Globals_IVK.g_anIsNational + " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE.CDIDIV_OID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + String.valueOf(M11_LRT.lrtStatusDeleted) + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CURRENT TIMESTAMP,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'''' || cdUserId_in || ''' ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_tabNameGenericCode || ' CDE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M85_DataFix.tempCodeOidTabName + " O ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'CDE." + M01_Globals.g_anOid + " = O.oid ' ");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "loop over tables to delete Codes", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_tabSchemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " AS c_tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CASE WHEN A." + M01_Globals.g_anAcmEntityId + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexNotice) + "' AND A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN 'CNOBCO_OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE '" + M01_Globals.g_anAhOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") AS c_colName");
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAhCid + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexGenericCode) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "OR");
M11_LRT.genProcSectionHeader(fileNo, "special treatment of 'Notes': they do not prohibit delete of Codes", 5, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "A." + M01_Globals.g_anAcmEntityId + " = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexNotice) + "' AND A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = c_orgId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPoolTypeId + " = c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "delete Codes individually in order to be able to name Codes in error messages", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR codeLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "oid AS c_codeOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "codeNumber AS c_codeNumber");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M85_DataFix.tempCodeOidTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "delete Code", 4, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_processedCodeNumber = c_codeNumber;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt = 'DELETE FROM ' || c_tabSchemaName || '.' || c_tabName || ' WHERE ' || c_colName || ' = ' || RTRIM(CHAR(c_codeOid));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 4, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "'deactivate' continue handler for FK-violation", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_processedCodeNumber = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "call GENWORKSPACE in each involved data pool", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR poolLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid AS c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId AS c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "call GENWORKSPACE for this data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameGenWs + "(2, ?, ?, ?, 0, 0, ?)';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_callCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "unlock each involved data pool", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR poolLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgOid AS c_orgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid AS c_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId AS c_accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempDataPoolTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "orgId ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "psOid ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "accessModeId ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "unlock this data pool", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'CALL " + qualProcNameResetLock + "(''' || RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',''<admin>'', ? ,''delete productive Codes'', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE v_stmnt INTO v_numDataPools USING cdUserId_in;");

M11_LRT.genProcSectionHeader(fileNo, "insist on data pool being unlocked", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_numDataPools <> 1 THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteProdCode, ddlType, 3, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("resetRel2ProdLockFail", fileNo, 3, "EXCLUSIVEWRITE", null, null, null, null, null, null, null, null, "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDeleteProdCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "rowCount_out", null, null, null, null, null, null, null, null);

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


private static void genDeleteTechAspectSupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M03_Config.supportSectionDataFix)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// Delete of Technical Aspect is only supported at 'pool-level'
return;
}

if ((thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// Delete of Technical Aspect is only supported in factory
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Delete of Technical Aspect only supported in data pools supporting LRT
return;
}

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameGenericAspectPub;
qualTabNameGenericAspectPub = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
String unqualTabNameGenericAspectPub;
unqualTabNameGenericAspectPub = M04_Utilities.getUnqualObjName(qualTabNameGenericAspectPub);
String qualTabNameGenericAspectPriv;
qualTabNameGenericAspectPriv = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, true, null, null, null, null, null);

String qualTabNameProperty;
qualTabNameProperty = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    SP for Deleteting 'Technical Aspects'
// ####################################################################################################################

String qualProcNameDeleteTechAspect;
qualProcNameDeleteTechAspect = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDeleteTechAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
M22_Class_Utilities.printSectionHeader("SP for 'Deleteting a 'technical' Aspect'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDeleteTechAspect);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "OID of the Aspect to delete");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure the Aspect corresponds to");
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "tr_in", "BIGINT", false, "logical number of the user's transaction to use for record deletion");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M79_Err.genSigMsgVarDecl(fileNo, null);
M11_LRT.genVarDecl(fileNo, "v_oid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtEntityIdCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameDeleteTechAspect, ddlType, 1, "oid_in", "psOid_in", "'cdUserId_in", "tr_in", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify that oid_in refers to TechData in the given ProductStructure", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameGenericAspectPub);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PRPAPR_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProperty + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_qualTabNamePropertyTemplate + " PT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT." + M01_Globals.g_anOid + " = P.PTMHTP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PT.ID IN (1, 2, 4, 5, 9, 43, 157, 186, 187, 188, 189, 190, 191, 192)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "check whether record was found", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_oid IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteTechAspect, ddlType, -2, "oid_in", "psOid_in", "'cdUserId_in", "tr_in", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("objNotFound", fileNo, 2, "Tech.Aspect", unqualTabNameGenericAspectPub, null, null, null, null, null, null, null, "RTRIM(CHAR(oid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "determine user transaction", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LRT." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_qualTabNameUser + " USR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + " LRT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LRT.UTROWN_OID = USR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LRT." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LRT.TRNUMBER = tr_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LRT." + M01_Globals.g_anEndTime + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USR." + M01_Globals.g_anUserId + " = cdUserId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "verify that user transaction exists", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_lrtOid IS NULL) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameDeleteTechAspect, ddlType, -2, "oid_in", "psOid_in", "'cdUserId_in", "tr_in", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdlWithParms("logLrtNotExist", fileNo, 2, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(tr_in))", "cdUserId_in", "RTRIM(CHAR(psOid_in))", null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

qualProcNameLrtLockGenericAspect;
qualProcNameLrtLockGenericAspect = M04_Utilities.genQualProcNameByEntityIndex(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, "LRTLOCK", null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "LRT-lock record", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualProcNameLrtLockGenericAspect + "(v_lrtOid, psOid_in, v_oid, v_rowCount);");

M11_LRT.genProcSectionHeader(fileNo, "convert LRT-lock to LRT-delete", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE " + qualTabNameGenericAspectPriv + " SET LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " WHERE " + M01_Globals.g_anOid + " = v_oid AND " + M01_Globals.g_anInLrt + " = v_lrtOid;");

M11_LRT.genDdlForUpdateAffectedEntities(fileNo, "ACM-Class", M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.gc_acmEntityTypeKeyClass, false, false, qualTabNameLrtAffectedEntity, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].classIdStr, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].classIdStr, "v_lrtOid", 1, String.valueOf(M11_LRT.lrtStatusDeleted), false);

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameDeleteTechAspect, ddlType, 1, "oid_in", "psOid_in", "'cdUserId_in", "tr_in", null, null, null, null, null, null, null, null);

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


private static void genOidMapSql(Integer ddlType, String colName, String qualTabName, String qualSeqNameOid, String lrtOidFilterStr, int fileNo, Integer indentW, String psOidFilterStrW, Boolean joinExpOidW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String psOidFilterStr; 
if (psOidFilterStrW == null) {
psOidFilterStr = "";
} else {
psOidFilterStr = psOidFilterStrW;
}

boolean joinExpOid; 
if (joinExpOidW == null) {
joinExpOid = false;
} else {
joinExpOid = joinExpOidW;
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
if (joinExpOid) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName + " AS v_record_" + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabName + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M85_DataFix.tempExpOidTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + M01_Globals.g_anAhOid + " = E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
if (!(lrtOidFilterStr.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + M01_Globals.g_anInLrt + " = " + lrtOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
if (!(psOidFilterStr.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " AS v_record_" + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
if (!(lrtOidFilterStr.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anInLrt + " = " + lrtOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
if (!(psOidFilterStr.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + colName);
}
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "NOT EXISTS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "( SELECT 1 FROM " + M82_PSCopy.tempOidMapTabName + " M WHERE M.oid = V.oid )");

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
if (joinExpOid) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName + " AS v_record_" + colName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabName + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + M01_Globals.g_anInLrt + " = " + lrtOidFilterStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "EXISTS(SELECT 1 FROM " + M85_DataFix.tempExpOidTabName + " E WHERE L." + M01_Globals.g_anAhOid + " = E.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "L." + colName);
} else {
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
}
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


private static void genExpCopySupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean includeExtendedEntitySetW) {
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

String sectionName;
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

boolean joinExpOid;
boolean hasAhoidCol;
String ahoidCol;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
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
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
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
M24_Attribute_Utilities.AttributeListTransformation transformationDebug;

String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNameLrtAffectedEntity;
qualTabNameLrtAffectedEntity = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabNamePub;
qualTabNamePub = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null);
String qualTabNamePriv;
qualTabNamePriv = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, null, null, null, null, null);

String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualExpViewName;
qualExpViewName = M04_Utilities.genQualViewNameByEntityIndex(M01_Globals_IVK.g_classIndexExpression, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

String qualProcName;

// ####################################################################################################################
// #    SP for copying Expression-records related to a given Product Structure to LRT-table(s) / includes mapping of OIDS
// ####################################################################################################################

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, "EXPCP2LRT", null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for copying Expression-records of table \"" + qualTabNamePub + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\") into private tables / includes OID-mapping", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "ID of the LRT corresponding to this transaction");
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
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);

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
if ((thisOrgIndex > M01_Globals.g_primaryOrgIndex) &  acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M85_DataFix.genDdlForTempFtoExpOid(fileNo, null, true, null, null);
}
M85_DataFix.genDdlForTempExpOid(fileNo, null, null, null, null);

joinExpOid = true;
if ((thisOrgIndex > M01_Globals.g_primaryOrgIndex)) {
if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "map2Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MIN(E." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " I");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anOid + " <> E." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.TERMSTRING = E.TERMSTRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anInLrt + " = E." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.ISINVALID = " + String.valueOf(M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.ISINVALID = " + String.valueOf(M01_LDM.gc_dbTrue));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M82_PSCopy.tempOidMapTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "map2Oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.EXTTRM_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MIN(E.EXTTRM_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " I");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.EXTTRM_OID <> E.EXTTRM_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.TERMSTRING = E.TERMSTRING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anInLrt + " = E." + M01_Globals.g_anInLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E.ISINVALID = " + String.valueOf(M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.ISINVALID = " + String.valueOf(M01_LDM.gc_dbTrue));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "I.EXTTRM_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' relate to the given Expressions into 'private table'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M24_Attribute_Utilities.initAttributeTransformation(transformation, 8, null, true, true, "EN.", null, null, null, null, null, null, null, null, M01_Common.AttrCategory.eacAnyOid, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conInLrt, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conHasBeenSetProductive, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conStatusId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLrtState, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, true, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewName + " EN");
if (acmEntityIndex != M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualExpViewName + " EX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anAhOid + " = EX." + M01_Globals.g_anOid);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M85_DataFix.tempExpOidTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anAhOid + " = E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(EN." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EN." + M01_Globals.g_anInLrt + " IS NULL)");
//rs41
if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ISINVALID = " + String.valueOf(M01_LDM.gc_dbFalse));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anOid + " = S." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + " = S." + M01_Globals_IVK.g_anPsOid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN NOT MATCHED THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT (");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES (");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, true, true, "S.", null, null, null, null, null, null, null, null, M01_Common.AttrCategory.eacAnyOid, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusCreated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conHasBeenSetProductive, M01_LDM.gc_dbFalse, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conLrtComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conCreateTimestamp, "currentTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conLastUpdateTimestamp, "currentTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conVersionId, "1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN MATCHED AND T.ISINVALID = " + String.valueOf(M01_LDM.gc_dbFalse) + " THEN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN MATCHED THEN");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE SET (UPDATEUSER, LASTUPDATETIMESTAMP, VERSIONID) = (cdUserId_in,  currentTs_in, T.VERSIONID + 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE IGNORE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");

M11_LRT.genProcSectionHeader(fileNo, "prepare cursor for OID-mapping", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntText;");

if (forGen &  useSurrogateKey) {
genOidMapSql(ddlType, M04_Utilities.genAttrName(entityShortName + "_" + M01_Globals.g_surrogateKeyNameShort, ddlType, null, null, null, null, null, null), qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, null, joinExpOid);
}

hasAhoidCol = false;
int i;
for (int i = 1; i <= transformation.oidDescriptors.numDescriptors; i++) {
if ((transformation.oidDescriptors.descriptors[i].colCat &  (M01_Common.AttrCategory.eacFkOidExpElement |  M01_Common.AttrCategory.eacOid)) != 0) {
genOidMapSql(ddlType, transformation.oidDescriptors.descriptors[i].colName, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, null, joinExpOid);
}
if ((transformation.oidDescriptors.descriptors[i].colCat &  M01_Common.AttrCategory.eacAhOid) != 0) {
hasAhoidCol = true;
ahoidCol = transformation.oidDescriptors.descriptors[i].colName;
}
}

if (hasAhoidCol) {
genOidMapSql(ddlType, ahoidCol, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0, null, joinExpOid);
}



if (transformation.nlAttrRefs.numDescriptors > 0) {
M04_Utilities.logMsg("NL-attributes for Expression-tables currently not supported for copy", M01_Common.LogLevel.ellError, ddlType, thisOrgIndex, thisPoolIndex);
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmOrParEntityId + " = '" + entityIdStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anAcmEntityType + " = '" + dbAcmEntityType + "'");
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

if (acmEntityIndex == M01_Globals_IVK.g_classIndexExpression) {
M11_LRT.genProcSectionHeader(fileNo, "copy the 'public records' relate to the given Expressions into 'private table' to set invalid", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePriv);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 8, null, true, true, "S.", null, null, null, null, null, null, null, null, M01_Common.AttrCategory.eacAnyOid, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conLrtState, String.valueOf(M11_LRT.lrtStatusUpdated), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conInLrt, "lrtOid_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM_IVK.conIsInvalid, String.valueOf(M01_LDM.gc_dbTrue), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conStatusId, String.valueOf(M86_SetProductive.statusWorkInProgress), null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conLrtComment, "CAST(NULL AS VARCHAR(1))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conLastUpdateTimestamp, "currentTs_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conUpdateUser, "cdUserId_in", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conVersionId, "S.VERSIONID + 1", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, true, forGen, M01_Common.DdlOutputMode.edomListLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M85_DataFix.tempExpOidTabName + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "S." + M01_Globals.g_anAhOid + " = E.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNamePub + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anInLrt + " = lrtOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (SELECT 1 FROM " + qualTabNamePriv + " L WHERE E." + M01_Globals.g_anOid + " = L." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "E." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");

}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "'cdUserId_in", "#currentTs_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}


public static void genExpCopySupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

if (M03_Config.generateExpCopySupport &  M22_Class.g_classes.descriptors[classIndex].isSubjectToExpCopy) {
genExpCopySupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, null);
}
}


public static void genExpCopySupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

if (M03_Config.generateExpCopySupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isSubjectToExpCopy) {
genExpCopySupportDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, null);
}
}

private static void genDeleteTechPropertySupportDdlByPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

if (!(M03_Config.supportSectionDataFix)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm &  (thisOrgIndex < 1 |  thisPoolIndex < 1)) {
// Delete of Technical Property is only supported at 'pool-level'
return;
}

if (!(M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt)) {
// Delete of Technical Property only supported in data pools supporting LRT
return;
}


String qualTabNameLrt;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualViewNameGenericAspectMqt;
qualViewNameGenericAspectMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, null, null, null, null, null);

String qualViewNameGenericAspectNlTextMqt;
qualViewNameGenericAspectNlTextMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, true, null, null, null, null);

String qualViewNamePropertyMqt;
qualViewNamePropertyMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, null, null, null, null, null);

String qualViewNamePropertyGenMqt;
qualViewNamePropertyGenMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, null, null, null, null, null);

String qualTabNamePropertyGenNlText;
qualTabNamePropertyGenNlText = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, false, false, true, null, null, null);

String qualViewNamePropertyGenNlTextMqt;
qualViewNamePropertyGenNlTextMqt = M04_Utilities.genQualViewNameByClassIndex(M01_Globals_IVK.g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, true, null, null, null, null);

String qualViewNameCpGroupHasPropertyLrt;
qualViewNameCpGroupHasPropertyLrt = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualViewNameSpGroupHasPropertyLrt;
qualViewNameSpGroupHasPropertyLrt = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexSpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualViewNameAggregationSlotHasNumericPropertyLrt;
qualViewNameAggregationSlotHasNumericPropertyLrt = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexAggregationSlotHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualViewNameCategoryHasNumericPropertyLrt;
qualViewNameCategoryHasNumericPropertyLrt = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexCategoryHasNumericProperty, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualViewNamePropertyValidForOrganizationLrt;
qualViewNamePropertyValidForOrganizationLrt = M04_Utilities.genQualViewNameByRelIndex(M01_Globals_IVK.g_relIndexPropertyValidForOrganization, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);

String qualTabExpression;
qualTabExpression = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualTabTerm;
qualTabTerm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexTerm, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String qualLrtBeginProcName;
qualLrtBeginProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexLrt, M01_ACM.spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualLrtCommitProcName;
qualLrtCommitProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM.spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDataFix, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseUseCases, M01_Common.ldmIterationPoolSpecific);

// ####################################################################################################################
// #    SP for Deleteting 'Technical properties (generic aspects that point to technical properties'
// ####################################################################################################################

String qualProcNameDeleteTechProperty;
qualProcNameDeleteTechProperty = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDataFix, M01_ACM_IVK.spnDeleteTechProperty, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);
M22_Class_Utilities.printSectionHeader("SP for 'Deleteting a 'technical' property'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameDeleteTechProperty);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "oid_in", M01_Globals.g_dbtOid, true, "Property-OID");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M11_LRT.genProcParm(fileNo, "IN", "id_in", "INTEGER", true, "PropertyTemplate-ID");
}
M11_LRT.genProcParm(fileNo, "IN", "ps_oid_in", M01_Globals.g_dbtOid, true, "PS-OID");
M11_LRT.genProcParm(fileNo, "OUT", "recordCount_out", "INTEGER", false, "number of records (sum over all involved tables)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SPECIFIC DELTECHPROPERTY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare constants", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_cdUserId           VARCHAR(15)      CONSTANT     'IVKMDS_tec_10';           -- CD User Id of the mdsUser");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_trNumber           INTEGER          CONSTANT     2;                    -- logical transaction number");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_lrtOid", "BIGINT", "NULL", null, null);
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M11_LRT.genVarDecl(fileNo, "v_prtOid", "BIGINT", "NULL", null, null);
}
M11_LRT.genVarDecl(fileNo, "v_prtName", "VARCHAR(255)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_msg", "VARCHAR(70)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_genChangelog", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntText", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspError", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspInfo", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_gwspWarning", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "declare continue handler for SQL-Exceptions", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROLLBACK;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = 0;");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "make sure that DB2-registers are empty", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );");
M00_FileWriter.printToFile(fileNo, "");


if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF id_in IS NULL AND oid_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_msg = RTRIM(LEFT('[MDS]: PropertyOID or TemplateID is necessary.',70));");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF id_in IS NOT NULL AND oid_in IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = ' || id_in || ' AND PT.OID = P.PTMHTP_OID WHERE P.AHOID = ' || oid_in || ' AND P.PS_OID = ' || ps_oid_in || ') > 0', 'OID and ID belongs to different Properties.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ') > 0', 'OID doesn''t exists in PS.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY_GEN WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_prtOid = oid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF id_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ') > 0', 'OID doesn''t exists in PS.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY_GEN WHERE AHOID = ' || oid_in || ' AND PS_OID = ' || ps_oid_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_prtOid = oid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = ' || id_in || ' AND PT.OID = P.PTMHTP_OID WHERE P.PS_OID = ' || ps_oid_in || ') > 0', 'Property doesn''t exists in PS.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL VL6CDBM.ASSERT('(SELECT COUNT(*) FROM VL6CPST.PROPERTYTEMPLATE WHERE ID = ' || id_in || ' AND TYPE_ID = 3) > 0', 'It''s not a tech. property.');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_prtOid = (SELECT P.OID FROM VL6CPST011.PROPERTY P JOIN VL6CPST.PROPERTYTEMPLATE PT ON ID = id_in AND PT.OID = P.PTMHTP_OID WHERE P.PS_OID = ps_oid_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
}


if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_prtName = (SELECT LABEL FROM " + qualTabNamePropertyGenNlText + " WHERE AHOID = v_prtOid AND PS_OID = ps_oid_in AND LANGUAGE_ID = 1 FETCH FIRST ROW ONLY);");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_prtName = (SELECT LABEL FROM " + qualTabNamePropertyGenNlText + " WHERE AHOID = oid_in AND PS_OID = ps_oid_in AND LANGUAGE_ID = 1 FETCH FIRST ROW ONLY);");
}


if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( '', '', '', NULL, NULL );");
M11_LRT.genProcSectionHeader(fileNo, "delete in MPCs", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID AS c_orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ORGOID AS c_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VL6CDBM.PDMORGANIZATION_ENUM O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntText = 'CALL VL6CDFX' || CAST(RIGHT('00' || RTRIM(CAST(c_orgId AS CHAR(2))),2) AS CHAR(2)) || '1.DELTECHPROPERTY(' || v_prtOid || ', ' || ps_oid_in || ', ?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntText;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
}



M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "open LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL " + qualLrtBeginProcName + "(c_cdUserId, c_trNumber, ps_oid_in, 0, v_lrtOid);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL SYSPROC.WLM_SET_CLIENT_INFO( c_cdUserId, v_lrtOid, ps_oid_in, NULL, NULL );");

M11_LRT.genProcSectionHeader(fileNo, "delete GenericAspects", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspectMqt + " GA ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, "");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PRPAPR_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PRPAPR_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.PS_OID = ps_oid_in ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GA.ISDELETED = 0 ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "delete GenericAspect_Nl-Text", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameGenericAspectNlTextMqt + " NL ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (SELECT 1 FROM " + qualViewNameGenericAspectMqt + " GA WHERE GA.AHOID = NL.AHOID AND GA.INLRT = v_lrtOid) ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.PS_OID = ps_oid_in ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NL.ISDELETED = 0 ");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "delete Property", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePropertyMqt + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.AHOID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.AHOID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "P.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePropertyGenMqt + "	" + " PG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG.AHOID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG.AHOID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PG.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePropertyGenNlTextMqt + " PNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PNL.AHOID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PNL.AHOID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PNL.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PNL.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameCpGroupHasPropertyLrt + " CPG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPG.PRP_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CPG.PRP_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CPG.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CPG.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameSpGroupHasPropertyLrt + " SPG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPG.PRP_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SPG.PRP_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SPG.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SPG.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameAggregationSlotHasNumericPropertyLrt + " AHP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHP.NPR_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHP.NPR_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AHP.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AHP.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNameCategoryHasNumericPropertyLrt + " CHP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CHP.NPR_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CHP.NPR_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CHP.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CHP.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewNamePropertyValidForOrganizationLrt + " PVO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PVO.PRP_OID = v_prtOid ");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PVO.PRP_OID = oid_in ");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PVO.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PVO.ISDELETED = 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "set LRT comment", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameLrt + "_NL_TEXT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(OID, LRT_OID, LANGUAGE_ID, TRANSACTIONCOMMENT, PS_OID) ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEXTVAL FOR " + M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null) + ", v_lrtOid, 1,  'MDS Service Skript: Löschen der technischen Eigenschaft ' || COALESCE(v_prtName, '-') || '. PsOid: '  || RTRIM( ps_oid_in ), ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, "");
M11_LRT.genProcSectionHeader(fileNo, "commit LRT", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL " + qualLrtCommitProcName + "(v_lrtOid, 0, v_genChangelog, v_rowCount, v_gwspError, v_gwspInfo, v_gwspWarning );");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET recordCount_out = v_rowCount;");
M00_FileWriter.printToFile(fileNo, "");

M11_LRT.genProcSectionHeader(fileNo, "mark invalid Expressions", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabExpression + " EX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET EX.ISINVALID = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EX.PS_OID = ps_oid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
if ((thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXISTS(SELECT 1 FROM " + qualTabTerm + " T WHERE T.PCRPRP_OID = v_prtOid AND EX.OID = T.AHOID AND EX.PS_OID = T.PS_OID)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXISTS(SELECT 1 FROM " + qualTabTerm + " T WHERE T.PCRPRP_OID = oid_in AND EX.OID = T.AHOID AND EX.PS_OID = T.PS_OID)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, "");


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