package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M13_PSTag {


// ### IF IVK ###


private static final int processingStep = 1;


private static void genPsTagSupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

String entityName;
String entityTypeDescr;
String entityShortName;
boolean isUserTransactional;
boolean isPsTagged;
boolean psTagOptional;
String subclassIdListMandatoryPsTag;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean entityInsertable;
boolean entityUpdatable;
boolean entityDeletable;
boolean isCtoAliasCreated;
String sectionName;
int sectionIndex;
boolean noAlias;
boolean ignorePsRegVarOnInsertDelete;
boolean useSurrogateKey;
boolean isChangeLog;
boolean expandExpressionsInFtoView;
boolean isNotAcmRelated;

subclassIdListMandatoryPsTag = "";
isChangeLog = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
entityInsertable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmInsert);
entityUpdatable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmUpdate);
entityDeletable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmDelete);
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
noAlias = M22_Class.g_classes.descriptors[acmEntityIndex].noAlias;
ignorePsRegVarOnInsertDelete = M22_Class.g_classes.descriptors[acmEntityIndex].ignPsRegVarOnInsDel;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;
isChangeLog = (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == M01_ACM.clnChangeLog.toUpperCase());
expandExpressionsInFtoView = M22_Class.g_classes.descriptors[acmEntityIndex].expandExpressionsInFtoView;
isNotAcmRelated = M22_Class.g_classes.descriptors[acmEntityIndex].notAcmRelated;

if (psTagOptional &  M22_Class.g_classes.descriptors[acmEntityIndex].hasSubClass) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].isPsTagged & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].psTagOptional) {
subclassIdListMandatoryPsTag = subclassIdListMandatoryPsTag + (!(subclassIdListMandatoryPsTag.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = false;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
entityInsertable = true;
entityUpdatable = true;
entityDeletable = true;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
noAlias = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noAlias;
ignorePsRegVarOnInsertDelete = false;
useSurrogateKey = true;// ???? FIXME
expandExpressionsInFtoView = false;
isNotAcmRelated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].notAcmRelated;
}

boolean poolSupportPsTaggingView;
boolean poolSupportPsTaggingTrigger;
boolean M72_DataPool.poolSupportLrt;
poolSupportPsTaggingView = true;
poolSupportPsTaggingTrigger = true;
returnValue = false;

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
poolSupportPsTaggingView = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportViewsForPsTag;
poolSupportPsTaggingTrigger = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportTriggerForPsTag;
}

if (!(isPsTagged |  isNotAcmRelated)) {
return;
}

if (isUserTransactional &  M01_Globals.g_genLrtSupport) {
// filtering by PS is included in LRT-views
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null);

String qualViewName;
String qualViewNameLdm;
boolean filterForPsDpMapping;
boolean filterForPsDpMappingExtended;

String tabQualifier;
tabQualifier = entityShortName.toUpperCase();

if (M03_Config.generatePsTaggingView &  poolSupportPsTaggingView) {
// we need to generate three views
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / first loop)
//   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / second loop)
//   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (third loop)

for (int i = 1; i <= 3; i++) {
filterForPsDpMapping = (i == 1);
filterForPsDpMappingExtended = (i == 2);

if (filterForPsDpMapping & ! M03_Config.supportFilteringByPsDpMapping) {
goto NextI;
}
if (filterForPsDpMappingExtended & ! M03_Config.supportFilteringByPsDpMapping) {
goto NextI;
}

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, null, null, "PS" + (filterForPsDpMapping ? "_I" : (filterForPsDpMappingExtended ? "_J" : "")), null, null);

M22_Class_Utilities.printSectionHeader("View for filtering by Product Structure (PS-tag) on table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, (M03_Config.supportFilteringByPsDpMapping ? (filterForPsDpMapping |  filterForPsDpMappingExtended ? "" : "do not ") + "filter by PSDPMAPPING" : ""));
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null);

M00_FileWriter.printToFile(fileNo, ")");
M00_FileWriter.printToFile(fileNo, "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, tabQualifier + ".", null, null, null, null, null, null, null, null, null, null, null);
// for MPC's work data pool we resolve Expressions to Factory Work Datra Pool - they may not (yet) exist in MPC
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, tabQualifier, null, null);
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone), null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " " + tabQualifier);

if (filterForPsDpMapping |  filterForPsDpMappingExtended) {
if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = PSDPM.PSOID");

if (filterForPsDpMappingExtended) {
if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM_SP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PSDPM_SP.PSOID = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}

if (filterForPsDpMapping |  filterForPsDpMappingExtended) {
if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if (isChangeLog) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS,");
if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM_SP");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals_IVK.g_qualTabNamePsDpMapping + " PSDPM");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PS." + M01_Globals.g_anOid + " = PSDPM.PSOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PS.PDIDIV_OID = " + tabQualifier + ".DIVISIONOID");
if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(PSDPM_SP.PSOID = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
if (filterForPsDpMappingExtended) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PSDPM_SP.PSOID IS NOT NULL)");
}
if (filterForPsDpMapping) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PSDPM.PSOID IS NOT NULL)");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");

if (M03_Config.usePsFltrByDpMappingForRegularViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
if (isChangeLog) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "((" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL) AND EXISTS (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " PS WHERE PS." + M01_Globals.g_anOid + " = " + M01_Globals_IVK.g_activePsOidDdl + " AND PS.PDIDIV_OID = " + tabQualifier + ".DIVISIONOID))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "( " + tabQualifier + ".DIVISIONOID IS NULL )");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! noAlias) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityIndex, M01_Common.DdlTypeId.edtLdm, null, null, forGen, null, null, null, null, "PS", null, null);
M22_Class.genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, false, false, filterForPsDpMapping, filterForPsDpMappingExtended, "PS-Tag-View \"" + sectionName + "." + entityName + "\"" + (M03_Config.supportFilteringByPsDpMapping ? " (" + (filterForPsDpMapping |  filterForPsDpMappingExtended ? "" : "do not ") + "filter by PSDPMAPPING)" : ""), null, isUserTransactional, true, true, null, null, null, null);
}
NextI:
}
}

if (M03_Config.generatePsTaggingTrigger &  poolSupportPsTaggingTrigger & useSurrogateKey) {
String qualTriggerName;

String objSuffix;
for (int i = 1; i <= 3; i++) {
filterForPsDpMapping = (i == 1);
filterForPsDpMappingExtended = (i == 2);

if (filterForPsDpMapping & ! M03_Config.supportFilteringByPsDpMapping) {
goto NextII;
}
if (filterForPsDpMappingExtended & ! M03_Config.supportFilteringByPsDpMapping) {
goto NextII;
}

objSuffix = (filterForPsDpMapping ? "_I" : (filterForPsDpMappingExtended ? "_J" : ""));

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, null, null, "PS" + objSuffix, null, null);

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "PS_INS" + objSuffix, null, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger supporting tagging by Product Structure (PS-tag) on table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityInsertable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("insertNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
if (isChangeLog) {
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_now", "TIMESTAMP", "NULL", null, null);
}
M79_Err.genSigMsgVarDecl(fileNo, null);

// note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
M11_LRT.genPsCheckDdlForInsertDelete(fileNo, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, psTagOptional, null, false, "v_psOidRecord", "v_psOidRegVar", "v_psOid", true, qualViewName, M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

if (isChangeLog) {
M11_LRT.genProcSectionHeader(fileNo, "determine current timestamp", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_now = CURRENT TIMESTAMP;");
}

if (!(subclassIdListMandatoryPsTag.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- for all subclasses with optional PS-tag do not set '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCid + " NOT IN (" + subclassIdListMandatoryPsTag + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psOid = v_psOidRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
if (isChangeLog) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "v_now", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "v_now", null, null, null);
} else {
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conCreateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + ", CURRENT TIMESTAMP)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", CURRENT TIMESTAMP)", null, null, null);
}

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

if (isChangeLog) {
if (!(((M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexChangeLogStatus].specificToPool > 0 &  M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexChangeLogStatus].specificToPool != M72_DataPool.g_pools.descriptors[thisPoolIndex].id) |  (M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexChangeLogStatus].specificToOrgId > 0 &  M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexChangeLogStatus].specificToOrgId != M71_Org.g_orgs.descriptors[thisOrgIndex].id)))) {
String qualTabNameChangelogStatus;
qualTabNameChangelogStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexChangeLogStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "keep track of last update timestamp of changelog", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameChangelogStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LASTCOMMITTIME = v_now");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + M01_Globals_IVK.g_anPsOid + ", -1) = COALESCE(v_psOid, -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(DIVISIONOID, -1) = COALESCE(" + M01_Globals.gc_newRecordName + ".DIVISIONOID, -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITHLRTCONTEXT = ( CASE WHEN " + M01_Globals.gc_newRecordName + ".LRTOID IS NULL THEN " + M01_LDM.gc_dbFalse + " ELSE " + M01_LDM.gc_dbTrue + " END )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_rowCount = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameChangelogStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + ", DIVISIONOID, LASTCOMMITTIME, WITHLRTCONTEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_psOid, " + M01_Globals.gc_newRecordName + ".DIVISIONOID, v_now, ( CASE WHEN " + M01_Globals.gc_newRecordName + ".LRTOID IS NULL THEN " + M01_LDM.gc_dbFalse + " ELSE " + M01_LDM.gc_dbTrue + " END )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "PS_UPD" + objSuffix, null, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger supporting tagging by Product Structure (PS-tag) on table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityUpdatable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("updateNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);

// note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar', 'v_psOid'
M11_LRT.genPsCheckDdlForUpdate(fileNo, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, psTagOptional, null, false, null, null, null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

if (!(subclassIdListMandatoryPsTag.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- for all subclasses with optional PS-tag do not set '" + M01_Globals_IVK.g_anPsOid + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCid + " NOT IN (" + subclassIdListMandatoryPsTag + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_psOid = v_psOidRecord;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conPsOid, "v_psOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conLastUpdateTimestamp, "COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", CURRENT TIMESTAMP)", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "PS_DEL" + objSuffix, null, null);

M22_Class_Utilities.printSectionHeader("Delete-Trigger supporting tagging by Product Structure (PS-tag) on table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityDeletable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {
M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);

// note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
M11_LRT.genPsCheckDdlForInsertDelete(fileNo, M01_Globals.gc_oldRecordName + "." + M01_Globals_IVK.g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, psTagOptional, null, false, null, null, "", null, qualViewName, M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);

M11_LRT.genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.TvBoolean.tvNull, 1);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_ACM_IVK.conPsOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_ACM_IVK.conPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
NextII:
}
}
}


public static void genPsTagSupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

genPsTagSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen);
}


public static void genPsTagSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

genPsTagSupportDdlForEntity(thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen);
}

// ### ENDIF IVK ###



}