package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M76_Index {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colClassName = colSection + 1;
private static final int colEntityType = colClassName + 1;
private static final int colIndexName = colEntityType + 1;
private static final int colShortName = colIndexName + 1;
private static final int colIsUnique = colShortName + 1;
private static final int colForGen = colIsUnique + 1;
private static final int colSpecificToQueryTables = colForGen + 1;
private static final int colSpecificToPool = colSpecificToQueryTables + 1;

private static final int firstRow = 3;

private static final String sheetName = "Idx";

public static M76_Index_Utilities.IndexDescriptors g_indexes;


private static void readSheet() {
M76_Index_Utilities.initIndexDescriptors(M76_Index.g_indexes);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].className = M00_Excel.getCell(thisSheet, thisRow, colClassName).getStringCellValue().trim();
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].cType = M24_Attribute_Utilities.getAttrContainerType(M00_Excel.getCell(thisSheet, thisRow, colEntityType).getStringCellValue().trim());
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].indexName = M00_Excel.getCell(thisSheet, thisRow, colIndexName).getStringCellValue().trim();
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].isUnique = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsUnique).getStringCellValue(), null);
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].forGen = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colForGen).getStringCellValue(), null);
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].specificToQueryTables = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colSpecificToQueryTables).getStringCellValue(), null);
M76_Index.g_indexes.descriptors[M76_Index_Utilities.allocIndexDescriptorIndex(M76_Index.g_indexes)].specificToPools = M00_Excel.getCell(thisSheet, thisRow, colSpecificToPool).getStringCellValue().trim();

NextRow:
thisRow = thisRow + 1;
}
}


public static void getIndexes() {
if ((M76_Index.g_indexes.numDescriptors == 0)) {
readSheet();
}
}


public static void resetIndexes() {
M76_Index.g_indexes.numDescriptors = 0;
}


// ### IF IVK ###
public static void genIndexesForEntity(String qualTabName, int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, Boolean noConstraintsW, Integer tabPartitionTypeW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean noConstraints; 
if (noConstraintsW == null) {
noConstraints = false;
} else {
noConstraints = noConstraintsW;
}

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genIndexesForEntity( _
// ByRef qualTabName As String, _
// ByRef acmEntityIndex As Integer, _
// ByRef acmEntityType As AcmAttrContainerType, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional forNl As Boolean = False, _
// Optional noConstraints As Boolean = False _
//)
// ### ENDIF IVK ###
//On Error GoTo ErrorExit 

if (!(M03_Config.genIndexesForAcmClasses |  (forLrt & ! M03_Config.generateIndexOnLrtTabs) | !M03_Config.generateDdlCreateIndex)) {
return;
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
//Defect 19643 wf
//Hier ein Aufruf für Erstelung Indexe VL6CPST011.PROPERTY_GEN_LRT_MQT
M76_Index.genIndexesForClassIndex(qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forNl, forMqt, null, noConstraints);

if (!(forNl)) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
//Defect 19643 wf
//Aufruf erfolgt 5 Mal fuer VL6CPST011.PROPERTY_GEN_LRT_MQT
M76_Index.genIndexesForClassIndex(qualTabName, M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i], thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forNl, forMqt, true, noConstraints);
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (!(forNl)) {
// ### IF IVK ###
M76_Index.genIndexesForRelationshipIndex(qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, forNl, forMqt, noConstraints, tabPartitionType);
// ### ELSE IVK ###
//     genIndexesForRelationshipIndex qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, forNl, forMqt, noConstraints
// ### ENDIF IVK ###
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

// ### IF IVK ###
public static void genIndexesForRelationshipIndex(String qualTabName, int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forLrtW, Boolean forNlW, Boolean forMqtW, Boolean noConstraintsW, Integer tabPartitionTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean noConstraints; 
if (noConstraintsW == null) {
noConstraints = false;
} else {
noConstraints = noConstraintsW;
}

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genIndexesForRelationshipIndex( _
// ByRef qualTabName As String, _
// ByRef thisRelIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forLrt As Boolean = False, _
// Optional forNl As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional noConstraints As Boolean = False _
//)
// ### ENDIF IVK ###
if (!(M03_Config.genIndexesForAcmClasses |  (forLrt & ! M03_Config.generateIndexOnLrtTabs) | !M03_Config.generateDdlCreateIndex)) {
return;
}

//On Error GoTo ErrorExit 

boolean poolSuppressUniqueConstraints;
boolean M72_DataPool.poolSupportLrt;
boolean poolCommonItemsLocal;
if (thisPoolIndex > 0) {
poolSuppressUniqueConstraints = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressUniqueConstraints;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
poolCommonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
}

String qualIndexName;
String colList;
colList = "";
String colListIncluded;
colListIncluded = "";
String ukAttrDecls;
String pkAttrList;
String leftFkAttrs;
String rightFkAttrs;
String relShortName;
String ukName;

M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;
leftClass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex];
rightclass = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex];

M22_Class_Utilities.ClassDescriptor leftOrClass;
M22_Class_Utilities.ClassDescriptor rightOrClass;
leftOrClass = M22_Class.getOrMappingSuperClass(leftClass.sectionName, leftClass.className);
rightOrClass = M22_Class.getOrMappingSuperClass(rightclass.sectionName, rightclass.className);

relShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName;

int numAttrs;
numAttrs = M23_Relationship.g_relationships.descriptors[thisRelIndex].attrRefs.numDescriptors;

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse_Int(thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, null, false, forLrt, M01_Common.DdlOutputMode.edomNone, poolCommonItemsLocal);

if (M03_Config.useSurrogateKeysForNMRelationships &  (numAttrs > 0 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange | M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional) & M23_Relationship.g_relationships.descriptors[thisRelIndex].useSurrogateKey & !forLrt) {
if (M03_Config.generateDdlCreatePK) {
M22_Class_Utilities.printSectionHeader("Primary Key for \"" + qualTabName + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genPkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY (" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
if ((thisPoolIndex == 2 |  thisPoolIndex == 3) &  numAttrs > 1) {
//        If (g_pools.descriptors(thisPoolIndex).id = 1 Or g_pools.descriptors(thisPoolIndex).id = 3) And numAttrs > 1 Then

boolean additionalUK;
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == -1 &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == -1 &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
}
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].minLeftCardinality == 1 &  M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[thisRelIndex].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
}

if (additionalUK) {
ukName = "UK_" + M04_Utilities.genPkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null).substring(4 - 1, 4 + M04_Utilities.genPkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null).length() - 1);

M22_Class_Utilities.printSectionHeader("Unique Constraint for \"" + qualTabName + "\"", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualUkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, "", ukName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "(" + M01_Globals.g_anOid + ", PS_OID" + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ukName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNIQUE (" + M01_Globals.g_anOid + ", PS_OID" + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

}

}

qualIndexName = M04_Utilities.genUkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, false, null);

pkAttrList = M24_Attribute.getPkAttrListByRel(thisRelIndex, ddlType, null);

pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ", ") + leftFkAttrs.toUpperCase() + ", " + rightFkAttrs.toUpperCase();

// ### IF IVK ###
if (M01_Globals.g_genLrtSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & !forLrt & !(pkAttrList.compareTo("") == 0)) {
pkAttrList = pkAttrList + ", " + M01_Globals_IVK.g_anIsDeleted;
}

// ### ENDIF IVK ###
M22_Class_Utilities.printSectionHeader("Unique Index on Foreign Key Attributes", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (poolSuppressUniqueConstraints ? "" : "UNIQUE ") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + pkAttrList + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
} else if (M03_Config.useSurrogateKeysForNMRelationships &  (numAttrs > 0 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange | !M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional | forLrt) & M23_Relationship.g_relationships.descriptors[thisRelIndex].useSurrogateKey) {

pkAttrList = leftFkAttrs.toUpperCase() + ", " + rightFkAttrs.toUpperCase();

if (forLrt) {
pkAttrList = pkAttrList + ", " + M01_Globals.g_anInLrt + ", " + M01_Globals.g_anLrtState;

qualIndexName = M04_Utilities.genUkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forMqt);

// FIXME: Uniqueness of this index is correct from business point of view, but
// Hibernate may propagate INSERTs / DELETEs in a wrong sequence
M22_Class_Utilities.printSectionHeader("Index on Foreign Key Attributes", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + pkAttrList + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
} else {
if (M03_Config.generateDdlCreatePK) {
M22_Class_Utilities.printSectionHeader("Primary Key for \"" + qualTabName + "\"", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genPkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY (" + pkAttrList + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}
}

if (M03_Config.useSurrogateKeysForNMRelationships &  forLrt & !forMqt & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged & (rightOrClass.isCommonToOrgs != leftOrClass.isCommonToOrgs)) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, M01_ACM.cosnInLrt + "CFK");

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anInLrt + "\", " + (!(leftOrClass.isCommonToOrgs) ? "\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftFkColName[ddlType] + "\", " : "") + (!(rightOrClass.isCommonToOrgs) ? "\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightFkColName[ddlType] + "\", " : "") + "\"" + M01_Globals.g_anLrtState + "\" and \"" + M01_Globals.g_anOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");

if (!(leftOrClass.isCommonToOrgs)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftFkColName[ddlType] + " ASC,");
}

if (!(rightOrClass.isCommonToOrgs)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightFkColName[ddlType] + " ASC,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (M03_Config.useSurrogateKeysForNMRelationships) {
if (forMqt) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, M01_ACM.cosnOid + M01_ACM.cosnIsLrtPrivate);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anOid + "\" and \"" + M01_Globals.g_anIsLrtPrivate + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
} else if (forLrt) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, M01_ACM.cosnOid);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp

qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, "IS" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange ? "CU" : ""));

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anInLrt + ", " + M01_Globals.g_anLrtState + ", " + (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange ? M01_Globals.g_anCreateTimestamp + ", " + M01_Globals.g_anLastUpdateTimestamp : "") + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC" + (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange ? "," : ""));
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].logLastChange) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anCreateTimestamp + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLastUpdateTimestamp + " ASC");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

// ### IF IVK ###
if ((rightOrClass.isPsTagged |  leftOrClass.isPsTagged)) {
if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForPsTag) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, "PSO");

colList = M01_Globals_IVK.g_anPsOid + " ASC";

colList = colList + ", " + leftFkAttrs + " ASC, " + rightFkAttrs + " ASC";

if (M01_Globals.g_genLrtSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & (!(forLrt |  forMqt))) {
colList = colList + ", " + M01_Globals_IVK.g_anIsDeleted + " ASC";
}

if (forMqt) {
colList = colList + ", " + M01_Globals.g_anIsLrtPrivate + " ASC";
}

if (forLrt) {
colList = colList + ", " + M01_Globals.g_anInLrt + " ASC" + ", " + M01_Globals.g_anLrtState + " ASC";
}

M22_Class_Utilities.printSectionHeader("Index on Foreign Key to \"Product Structure\" Table", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + colList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
} else if ((rightOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode |  leftOrClass.aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode)) {
//generate index for DIV_OID
if ((ddlType == M01_Common.DdlTypeId.edtPdm)) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, "DVO");

colList = M01_Globals_IVK.g_anDivOid + " ASC";

colList = colList + ", " + leftFkAttrs + " ASC, " + rightFkAttrs + " ASC";

if (M01_Globals.g_genLrtSupport &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & (!(forLrt |  forMqt))) {
colList = colList + ", " + M01_Globals_IVK.g_anIsDeleted + " ASC";
}

if (forMqt) {
colList = colList + ", " + M01_Globals.g_anIsLrtPrivate + " ASC";
}

if (forLrt) {
colList = colList + ", " + M01_Globals.g_anInLrt + " ASC" + ", " + M01_Globals.g_anLrtState + " ASC";
}

M22_Class_Utilities.printSectionHeader("Index on Foreign Key to \"Division\" Table", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + colList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}


}

// ### ENDIF IVK ###
if (M03_Config.supportNlForRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl) {
if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForNLang) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "LAN", M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + "LAN", ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, null);

M22_Class_Utilities.printSectionHeader("Index on Foreign Key to \"Language Table\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, rightclass.className + relShortName, rightclass.shortName + relShortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, null);

M22_Class_Utilities.printSectionHeader("Index on Foreign Key corresponding to Class \"" + rightclass.sectionName + "." + rightclass.className + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M24_Attribute.getFkSrcAttrSeq(rightclass.classIndex, "", ddlType) + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp

qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, leftClass.className + relShortName, leftClass.shortName + relShortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, null);

M22_Class_Utilities.printSectionHeader("Index on Foreign Key corresponding to Class \"" + leftClass.sectionName + "." + leftClass.className + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M24_Attribute.getFkSrcAttrSeq(leftClass.classIndex, "", ddlType) + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional &  M03_Config.generateIndexOnAhClassIdOid & (!(forLrt |  forMqt))) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, M01_ACM.cosnAggHeadClassId + M01_ACM.cosnAggHeadOId);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhCid + "\" and \"" + M01_Globals.g_anAhOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhCid + " ASC,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### IF IVK ###
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].aggHeadClassIndex > 0) &  M01_Globals.g_genLrtSupport & M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & M03_Config.generateIndexOnAhClassIdOidStatus & (!(forLrt |  forMqt))) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, "X" + M01_ACM.cosnAggHeadClassId.substring(0, 1) + M01_ACM.cosnAggHeadOId.substring(0, 1) + M01_ACM_IVK.esnStatus.substring(0, 1));

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhCid + "\", \"" + M01_Globals.g_anAhOid + "\" and \"" + M01_Globals.g_anStatus + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhCid + " ASC,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anStatus + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### ENDIF IVK ###
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].aggHeadClassIndex > 0) &  (!(forLrt |  forMqt)) & M03_Config.generateIndexOnAhOid) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, M01_ACM.cosnAggHeadOId);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isPsTagged &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isUserTransactional & !forLrt & !forMqt & M03_Config.generateIndexForSetProductive) {
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forNl, forMqt, "STP");

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anPsOid + "\",\"" + M01_Globals.g_anStatus + "\",\"" + M01_Globals_IVK.g_anIsDeleted + "\",\"" + M01_Globals_IVK.g_anHasBeenSetProductive + "\",\"" + M01_Globals.g_anOid + "\" in table \"" + qualTabName + "\" (for SETPRODUCTIVE)", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anStatus + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anHasBeenSetProductive + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### ENDIF IVK ###
int i;
for (int i = 1; i <= M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.numRefs; i++) {
colList = "";
colListIncluded = "";
if (M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].specificToQueryTables) {
if (M72_DataPool.poolSupportLrt) {
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].useMqtToImplementLrt) {
if (!(forMqt)) {
goto NextI;
}
}
}
}

if ((M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.numRefs > 0) &  (M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].specificToPools.compareTo("") == 0 |  M04_Utilities.includedInList(M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].specificToPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id))) {
int j;
for (int j = 1; j <= M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.numRefs; j++) {
String thisColName;
String extraColName;
thisColName = "";
extraColName = "";
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef > 0) {
thisColName = M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].dbColName[ddlType];
} else if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef < 0) {
// meta attribute
thisColName = M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrName;
} else if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef > 0) {
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRefDirection == M01_Common.RelNavigationDirection.etLeft) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightEntityIndex].useSurrogateKey) {
thisColName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightEntityIndex].shortName, null, null, null, null);
} else {
thisColName = M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightEntityIndex, ddlType, null, null, null, null);
}
} else {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftEntityIndex].useSurrogateKey) {
thisColName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftEntityIndex].shortName, null, null, null, null);
} else {
thisColName = M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftEntityIndex, ddlType, null, null, null, null);
}
}
}

if (!(thisColName.compareTo("") == 0)) {
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrIsIncluded) {
colListIncluded = colListIncluded + (colListIncluded.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + thisColName + (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].isAsc ? " ASC" : " DESC");
} else {
colList = colList + (colList.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + thisColName + (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].attrRefs.refs[j]].isAsc ? " ASC" : " DESC");
if (!(extraColName.compareTo("") == 0)) {
colList = colList + (colList.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + extraColName + " ASC";
}
}
}
}

M22_Class_Utilities.printSectionHeader("Index \"" + M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].indexName + "\" for " + (M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated ? "table" : "ACM relationship") + " \"" + M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].sectionName + "." + M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].className + "\"", fileNo, null, null);

qualIndexName = M04_Utilities.genQualIndexName(M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].sectionIndex, M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].indexName, M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forNl, forMqt, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (M76_Index.g_indexes.descriptors[M23_Relationship.g_relationships.descriptors[thisRelIndex].indexRefs.refs[i]].isUnique & ! noConstraints & !forMqt ? "UNIQUE " : "") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + colList + (forLrt |  forMqt ? "," : ""));

if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

if (!(colListIncluded.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCLUDE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + colListIncluded);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
NextI:
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genIndexesForClassIndex(String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forNlW, Boolean forMqtW, Boolean forSubClassW, Boolean noConstraintsW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forSubClass; 
if (forSubClassW == null) {
forSubClass = false;
} else {
forSubClass = forSubClassW;
}

boolean noConstraints; 
if (noConstraintsW == null) {
noConstraints = false;
} else {
noConstraints = noConstraintsW;
}

if (!(M03_Config.genIndexesForAcmClasses |  (forLrt & ! M03_Config.generateIndexOnLrtTabs) | !M03_Config.generateDdlCreateIndex)) {
return;
}

boolean M72_DataPool.poolSupportLrt;
boolean poolCommonItemsLocal;
if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
poolCommonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
}

String qualIndexName;
int i;
int j;
String colList;
String colListIncluded;

colList = "";
colListIncluded = "";

// ### IF IVK ###
String fkAttrToDiv;
fkAttrToDiv = "";
boolean useFkToDiv;
Integer tabPartitionType;
// ### ENDIF IVK ###

// ### IF IVK ###
boolean isDivTagged;
isDivTagged = false;

if (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex > 0 & ! M22_Class.g_classes.descriptors[classIndex].isPsTagged & !forNl) {
fkAttrToDiv = (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType]);
isDivTagged = true;
}

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged &  M03_Config.supportRangePartitioningByPsOid) {
tabPartitionType = (M22_Class.g_classes.descriptors[classIndex].noRangePartitioning ? M94_DBAdmin_Partitioning.PartitionType.ptNone : M94_DBAdmin_Partitioning.PartitionType.ptPsOid);
} else if (isDivTagged &  M03_Config.supportRangePartitioningByDivOid) {
tabPartitionType = (M22_Class.g_classes.descriptors[classIndex].noRangePartitioning ? M94_DBAdmin_Partitioning.PartitionType.ptNone : M94_DBAdmin_Partitioning.PartitionType.ptDivOid);
}

if (tabPartitionType != M94_DBAdmin_Partitioning.PartitionType.ptNone) {
if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional) {
if (M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt) {
if (forLrt) {
if (!((forMqt |  M03_Config.partitionLrtPrivateWhenMqt))) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
}

} else {
if (!((forMqt |  M03_Config.partitionLrtPublicWhenMqt))) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
}

}
} else {
if (forLrt) {
if (!(M03_Config.partitionLrtPrivateWhenNoMqt)) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
}

} else {
if (!(M03_Config.partitionLrtPublicWhenNoMqt)) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
}

}
}
}
if ((tabPartitionType != M94_DBAdmin_Partitioning.PartitionType.ptNone) &  !(M03_Config.noPartitioningInDataPools.compareTo("") == 0) & thisPoolIndex > 0) {
if (M04_Utilities.includedInList(M03_Config.noPartitioningInDataPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id)) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
}

}
}

if (M22_Class.g_classes.descriptors[classIndex].hasGroupIdAttrInNonGenInclSubClasses & ! forNl & !forSubClass & (thisPoolIndex != M01_Globals_IVK.g_archiveDataPoolIndex)) {
if ((M22_Class.g_classes.descriptors[classIndex].isUserTransactional &  (M72_DataPool.poolSupportLrt &  (M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt == forMqt))) | ! M72_DataPool.poolSupportLrt) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "GRP");

M22_Class_Utilities.printSectionHeader("Index on \"GroupID-columns\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (!(forNl & ! M22_Class.g_classes.descriptors[classIndex].hasOwnTable & !forSubClass & !M22_Class.g_classes.descriptors[classIndex].notAcmRelated)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anCid + " ASC,");
}

// add groupid attributes only once
int k;
String[] groupIdAttrNames = new String[5];// so far only 2
for (int k = M00_Helper.lBound(M22_Class.g_classes.descriptors[classIndex].groupIdAttrIndexesInclSubclasses); k <= M00_Helper.uBound(M22_Class.g_classes.descriptors[classIndex].groupIdAttrIndexesInclSubclasses); k++) {
boolean found;
found = false;
String attrName;
attrName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].groupIdAttrIndexesInclSubclasses[k]].attributeName, ddlType, null, null, null, null, null, null);
groupIdAttrNames[(k)] = attrName;
for (int i = 1; i <= k - 1; i++) {
if ((groupIdAttrNames[i] == attrName)) {
found = true;
}
}
if (!(found)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + attrName + " ASC,");
}
}

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC" + (forMqt |  M22_Class.g_classes.descriptors[classIndex].isUserTransactional ? "," : ""));
}
if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional &  forLrt & M72_DataPool.poolSupportLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC,");
}
if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional &  M72_DataPool.poolSupportLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
}
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

// ### ENDIF IVK ###
if (forMqt & ! forSubClass) {
// ### IF IVK ###
for (int i = 1; i <= (fkAttrToDiv.compareTo("") == 0 ? 1 : 2); i++) {
useFkToDiv = (i == 2);
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnOid + M01_ACM.cosnIsLrtPrivate + (useFkToDiv ? "D" : ""));
// ### ELSE IVK ###
// ### INDENT IVK ### -2
//       qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnIsLrtPrivate)
// ### ENDIF IVK ###

// ### IF IVK ###
M22_Class_Utilities.printSectionHeader("Index on " + (useFkToDiv ? "\"" + fkAttrToDiv + "\", " : "") + "\"" + M01_Globals.g_anOid + "\", \"" + M01_Globals.g_anIsLrtPrivate + "\", ... in table \"" + qualTabName + "\"", fileNo, null, null);
// ### ELSE IVK ###
//       printSectionHeader "Index on " & """" & g_anOid & """, """ & g_anIsLrtPrivate & """, ... in table """ & qualTabName & """", fileNo
// ### ENDIF IVK ###

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
// ### IF IVK ###

if (useFkToDiv) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkAttrToDiv + " ASC,");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt | ! M22_Class.g_classes.descriptors[classIndex].isPsTagged ? "," : ""));
// ### ELSE IVK ###
//       Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
// ### ENDIF IVK ###

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC" + (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged) ? "," : ""));
// ### ELSE IVK ###
//         Print #fileNo, addTab(1); g_anLrtState; " ASC"
// ### ENDIF IVK ###
}
// ### IF IVK ###

if (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC");
}
// ### ELSE IVK ###
//       Print #fileNo, addTab(1); g_anAhOid; " ASC"
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
// ### IF IVK ###

if (!(fkAttrToDiv.compareTo("") == 0) & ! M22_Class.g_classes.descriptors[classIndex].isCommonToPools & !poolCommonItemsLocal & (forMqt | ! (M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt &  forLrt))) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnOid + M01_ACM.cosnIsLrtPrivate + "DD");

M22_Class_Utilities.printSectionHeader("Index on \"" + fkAttrToDiv + "\", \"" + M01_Globals.g_anOid + "\", ... in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC,");

if (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged & ! forLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkAttrToDiv + " ASC" + ((!(M22_Class.g_classes.descriptors[classIndex].isPsTagged &  forLrt)) |  forLrt | forMqt ? "," : ""));

if (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged &  forLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC" + (forLrt |  forMqt ? "," : ""));
}

if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}

if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### ENDIF IVK ###

if (forLrt & ! forMqt & !forSubClass) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "IS" + (M22_Class.g_classes.descriptors[classIndex].logLastChange ? "CU" : ""));

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anInLrt + ", " + M01_Globals.g_anLrtState + ", " + (M22_Class.g_classes.descriptors[classIndex].logLastChange ? M01_Globals.g_anCreateTimestamp + ", " + M01_Globals.g_anLastUpdateTimestamp : "") + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC" + (M22_Class.g_classes.descriptors[classIndex].logLastChange ? "," : ""));
if (M22_Class.g_classes.descriptors[classIndex].logLastChange) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anCreateTimestamp + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLastUpdateTimestamp + " ASC");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### IF IVK ###

if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional &  !(fkAttrToDiv.compareTo("") == 0) & !forNl & !forGen & !forNl & !forLrt & M72_DataPool.poolSupportLrt) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexDivision].shortName);

M22_Class_Utilities.printSectionHeader("Index on \"" + fkAttrToDiv + "\" and \"" + M01_Globals.g_anInLrt + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkAttrToDiv + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### ENDIF IVK ###

if (!(forSubClass & ! forNl & (forMqt |  (!(M22_Class.g_classes.descriptors[classIndex].notAcmRelated & ! M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt))) & !M22_Class.g_classes.descriptors[classIndex].hasOwnTable & !poolCommonItemsLocal)) {
// ### IF IVK ###
for (int i = 1; i <= (fkAttrToDiv.compareTo("") == 0 ? 1 : 2); i++) {
useFkToDiv = (i == 2);
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnOid + M01_ACM.cosnClassId + (useFkToDiv ? "D" : ""));

M22_Class_Utilities.printSectionHeader("Index on " + (useFkToDiv ? "\"" + fkAttrToDiv + "\", " : "") + "\"" + M01_Globals.g_anOid + "\"" + (forMqt ? "," : " and") + " \"" + M01_Globals.g_anCid + "\"" + (forMqt ? " and \"" + M01_Globals.g_anIsLrtPrivate + "\"" : "") + " in table \"" + qualTabName + "\"", fileNo, null, null);
// ### ELSE IVK ###
// ### INDENT IVK ### -2
//       qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnClassId)
//
//       printSectionHeader "Index on " & """" & g_anOid & """" & IIf(forMqt, ",", " and") & " """ & g_anCid & """" & IIf(forMqt, " and """ & g_anIsLrtPrivate & """", "") & " in table """ & qualTabName & """", fileNo
// ### ENDIF IVK ###

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
// ### IF IVK ###

if (useFkToDiv) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkAttrToDiv + " ASC,");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC,");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anCid + " ASC" + (M22_Class.g_classes.descriptors[classIndex].isPsTagged |  forMqt | M22_Class.g_classes.descriptors[classIndex].isUserTransactional ? "," : ""));
if (M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC" + (forMqt |  M22_Class.g_classes.descriptors[classIndex].isUserTransactional ? "," : ""));
}
// ### ELSE IVK ###
//       Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(forMqt Or .isUserTransactional, ",", "")
// ### ENDIF IVK ###
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (M22_Class.g_classes.descriptors[classIndex].isUserTransactional ? "," : ""));
}
if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC" + (forLrt ? "," : ""));
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
// ### IF IVK ###

if ((!(forLrt |  forMqt)) & ! forNl & !forSubClass & ((forGen &  M22_Class.g_classes.descriptors[classIndex].hasExpressionInGen) |  (!(forGen &  M22_Class.g_classes.descriptors[classIndex].hasExpressionInNonGen)))) {
if (M03_Config.generateIndexOnFk) {
boolean classHasNoIdentity;
classHasNoIdentity = M22_Class.g_classes.descriptors[classIndex].hasNoIdentity;

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntityWithColReuse(M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null, forLrt, forGen, M01_Common.DdlOutputMode.edomNone, null);

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if ((tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) != 0 &  (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName + M23_Relationship.g_relationships.descriptors[tabColumns.descriptors[k].acmFkRelIndex].shortName + (tabColumns.descriptors[k].columnCategory &  M01_Common.AttrCategory.eacNational ? "N" : ""), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "EXP");

M22_Class_Utilities.printSectionHeader("Index on Expression-Foreign-Key \"" + tabColumns.descriptors[k].columnName + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[k].columnName + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}
}
}
// ### ENDIF IVK ###

if (forNl) {
if (M03_Config.generateIndexOnFk) {
String attrNameParFk;
attrNameParFk = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[classIndex].shortName, null, null, null, null);

qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PAR");

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + attrNameParFk + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

if (!(forNl & ! M22_Class.g_classes.descriptors[classIndex].hasOwnTable & !forSubClass & M03_Config.generateIndexOnClassId & !M22_Class.g_classes.descriptors[classIndex].notAcmRelated)) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnClassId);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anCid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anCid + " ASC" + (forLrt |  forMqt | M22_Class.g_classes.descriptors[classIndex].isPsTagged ? "," : ""));
if (M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC" + (forMqt |  forLrt ? "," : ""));
}
// ### ELSE IVK ###
//     Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(forLrt Or forMqt, ",", "")
// ### ENDIF IVK ###
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### IF IVK ###

if (!(forNl &  M22_Class.g_classes.descriptors[classIndex].isPsTagged & forLrt & !forSubClass & M03_Config.generateIndexOnFkForPsTag & !M22_Class.g_classes.descriptors[classIndex].notAcmRelated)) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PLS");

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anPsOid + "\", \"" + (forMqt ? "\"" + M01_Globals.g_anIsLrtPrivate + "\", " : "") + M01_Globals.g_anInLrt + "\" and \"" + M01_Globals.g_anLrtState + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### ENDIF IVK ###

// ### IF IVK ###
if (!(forNl & ! forSubClass & M22_Class.g_classes.descriptors[classIndex].isGenForming & (forGen |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity))) {
// ### ELSE IVK ###
//   If Not forNl And Not forSubClass And .isGenForming And forGen Then
// ### ENDIF IVK ###
if (M03_Config.generateIndexOnValidFrom) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnValidFrom);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anValidFrom + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anValidFrom + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (M03_Config.generateIndexOnValidUntil) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnValidTo);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anValidTo + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anValidTo + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (M03_Config.generateIndexOnValidFromUntil) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnValidFrom + M01_ACM.cosnValidTo);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anValidFrom + "\" and \"" + M01_Globals_IVK.g_anValidTo + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anValidFrom + " ASC,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anValidTo + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].isUserTransactional & ! forSubClass & !M22_Class.g_classes.descriptors[classIndex].condenseData & M03_Config.generateIndexOnAhClassIdOid & (!(forLrt |  forMqt))) {
// ### ELSE IVK ###
//   If .isUserTransactional And Not forSubClass And generateIndexOnAhClassIdOid And (Not forLrt Or forMqt) Then
// ### ENDIF IVK ###
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnAggHeadClassId + M01_ACM.cosnAggHeadOId);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhCid + "\" and \"" + M01_Globals.g_anAhOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhCid + " ASC,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### IF IVK ###
if ((M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex > 0) &  M01_Globals.g_genLrtSupport & M22_Class.g_classes.descriptors[classIndex].isUserTransactional & !forSubClass & !M22_Class.g_classes.descriptors[classIndex].condenseData & M03_Config.generateIndexOnAhClassIdOidStatus & (!(forLrt |  forMqt))) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "X" + M01_ACM.cosnAggHeadClassId.substring(0, 1) + M01_ACM.cosnAggHeadOId.substring(0, 1) + M01_ACM_IVK.esnStatus.substring(0, 1));

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhCid + "\", \"" + M01_Globals.g_anAhOid + "\" and \"" + M01_Globals.g_anStatus + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhCid + " ASC,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anStatus + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

// ### ENDIF IVK ###
if ((M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex > 0) &  (!(forLrt |  forMqt)) & M03_Config.generateIndexOnAhOid & !forSubClass) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, M01_ACM.cosnAggHeadOId);

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals.g_anAhOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anAhOid + " ASC" + (forLrt |  forMqt ? "," : ""));
if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### IF IVK ###

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & M22_Class.g_classes.descriptors[classIndex].isAggHead & !M22_Class.g_classes.descriptors[classIndex].condenseData & !forGen & !forLrt & !forMqt & !forSubClass & M72_DataPool.poolSupportLrt & (thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
// Index for FTOLOCK
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PIO");

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anPsOid + "\",\"" + M01_Globals.g_anInLrt + "\", \"" + M01_Globals.g_anOid + "\" in table \"" + qualTabName + "\"", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & !forLrt & !forMqt & !forSubClass & M03_Config.generateIndexForSetProductive) {
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "STP");

M22_Class_Utilities.printSectionHeader("Index on \"" + M01_Globals_IVK.g_anPsOid + "\",\"" + M01_Globals.g_anStatus + "\",\"" + M01_Globals_IVK.g_anIsDeleted + "\",\"" + M01_Globals_IVK.g_anHasBeenSetProductive + "\",\"" + M01_Globals.g_anOid + "\" in table \"" + qualTabName + "\" (for SETPRODUCTIVE)", fileNo, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC,");
if (!(M22_Class.g_classes.descriptors[classIndex].condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anStatus + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anIsDeleted + " ASC,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anHasBeenSetProductive + " ASC,");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[classIndex].shortName, null, null, null, null) + " ASC");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anOid + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### ENDIF IVK ###

if (!(forNl)) {
for (int i = 1; i <= M22_Class.g_classes.descriptors[classIndex].indexRefs.numRefs; i++) {
colList = "";
colListIncluded = "";
if ((!(M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].specificToPools.compareTo("") == 0))) {
if ((thisPoolIndex < 1)) {
goto NextI;
} else if (!(M04_Utilities.includedInList(M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].specificToPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id))) {
goto NextI;
}
}

if ((M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].forGen != forGen)) {
goto NextI;
}

if (M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].specificToQueryTables) {
if (M72_DataPool.poolSupportLrt) {
if (M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt) {
if (!(forMqt)) {
goto NextI;
}
}
}
}

if (M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.numRefs > 0) {
for (int j = 1; j <= M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.numRefs; j++) {
String thisColName;
String extraColName;
thisColName = "";
extraColName = "";
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef > 0) {
// ### IF IVK ###
boolean isGenAttr;
isGenAttr = M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].isTimeVarying;
if (M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].acmEntityIndex > 0) {
if (M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].acmEntityIndex].hasNoIdentity) {
isGenAttr = false;
}
}
if (forGen == isGenAttr) {
thisColName = M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].dbColName[ddlType];
if (!(M24_Attribute.g_attributes.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef].groupIdBasedOn.compareTo("") == 0) & ! M22_Class.g_classes.descriptors[classIndex].hasOwnTable) {
extraColName = M01_Globals.g_anCid;
}
}
// ### ENDIF IVK ###
} else if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrRef < 0) {
// meta attribute such as 'CLASSID'
thisColName = M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrName;
} else if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef > 0 & ! forGen) {
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRefDirection == M01_Common.RelNavigationDirection.etLeft) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightEntityIndex].useSurrogateKey) {
thisColName = M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightFkColName[ddlType];
} else {
thisColName = M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].rightEntityIndex, ddlType, null, null, null, null);
}
} else {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftEntityIndex].useSurrogateKey) {
thisColName = M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftFkColName[ddlType];
} else {
thisColName = M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].relRef].leftEntityIndex, ddlType, null, null, null, null);
}
}
}

// ### IF IVK ###
if (!(thisColName.compareTo("") == 0) &  (!(thisColName.compareTo(M01_Globals_IVK.g_anIsDeleted) == 0) |  forMqt)) {
// ### ELSE IVK ###
//             If thisColName <> "" And forMqt Then
// ### ENDIF IVK ###
if (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].attrIsIncluded) {
colListIncluded = colListIncluded + (colListIncluded.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + thisColName + (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].isAsc ? " ASC" : " DESC");
} else {
colList = colList + (colList.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + thisColName + (M77_IndexAttr.g_indexAttrs.descriptors[M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].attrRefs.refs[j]].isAsc ? " ASC" : " DESC");
if (!(extraColName.compareTo("") == 0)) {
colList = colList + (colList.compareTo("") == 0 ? "" : "," + vbCrLf) + M04_Utilities.addTab(1) + extraColName + " ASC";
}
}
}
}

M22_Class_Utilities.printSectionHeader("Index \"" + M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].indexName + "\" for " + (M22_Class.g_classes.descriptors[classIndex].notAcmRelated ? "table" : "ACM class") + " \"" + M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].sectionName + "." + M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].className + "\"", fileNo, null, null);

qualIndexName = M04_Utilities.genQualIndexName(M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].sectionIndex, M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].indexName, M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (M76_Index.g_indexes.descriptors[M22_Class.g_classes.descriptors[classIndex].indexRefs.refs[i]].isUnique & ! noConstraints & !forMqt ? "UNIQUE " : "") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + colList + (forLrt |  forMqt ? "," : ""));

if (forMqt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anIsLrtPrivate + " ASC" + (forLrt ? "," : ""));
}
if (forLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLrtState + " ASC");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

if (!(colListIncluded.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCLUDE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + colListIncluded);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
NextI:
}
}
}


public static void evalIndexes() {
int i;
int j;
M21_Enum_Utilities.EnumDescriptor enumDescr;

for (i = 1; i <= 1; i += (1)) {
// determine references to index attributes
M76_Index.g_indexes.descriptors[i].attrRefs.numRefs = 0;
M76_Index.g_indexes.descriptors[i].sectionIndex = M20_Section.getSectionIndexByName(M76_Index.g_indexes.descriptors[i].sectionName, null);
for (j = 1; j <= 1; j += (1)) {
if (M76_Index.g_indexes.descriptors[i].sectionName.toUpperCase() == M77_IndexAttr.g_indexAttrs.descriptors[j].sectionName.toUpperCase() &  M76_Index.g_indexes.descriptors[i].cType.compareTo(M77_IndexAttr.g_indexAttrs.descriptors[j].cType) == 0 & M76_Index.g_indexes.descriptors[i].indexName.toUpperCase() == M77_IndexAttr.g_indexAttrs.descriptors[j].indexName.toUpperCase()) {
// verify that .className corresponds to some sub-class of the indexes .classname
boolean foundMatch;
if (M77_IndexAttr.g_indexAttrs.descriptors[j].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M76_Index.g_indexes.descriptors[i].className.toUpperCase() == M77_IndexAttr.g_indexAttrs.descriptors[j].className.toUpperCase()) {
foundMatch = true;
} else {
foundMatch = false;
int thisClassIndex;
thisClassIndex = M22_Class.getClassIndexByName(M77_IndexAttr.g_indexAttrs.descriptors[j].sectionName, M76_Index.g_indexes.descriptors[i].className, null);
int k;
for (int k = 1; k <= M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive); k++) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive[k]].className.compareTo(M77_IndexAttr.g_indexAttrs.descriptors[j].className) == 0) {
foundMatch = true;
}
}
}
} else {
foundMatch = true;
}
if (foundMatch) {
M77_IndexAttr_Utilities.allocIndexAttrDescriptorRefIndex(M76_Index.g_indexes.descriptors[i].attrRefs);
M76_Index.g_indexes.descriptors[i].attrRefs.refs[(M76_Index.g_indexes.descriptors[i].attrRefs.numRefs)] = j;
}
}
}
}
}




}