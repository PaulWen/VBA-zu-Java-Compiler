package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M14_XMLExport {


// ### IF IVK ###



private static void genXmlExportViewSupportDdlForNlTable(M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, String xmlElementName, String qualNlTabName, int fileNo, Integer ddlTypeW, Boolean inclTvAttrsW, Boolean inclNonTvAttrsW, Integer onlyThisAttributeW, String tabVariableW, String idVariableW, String idRefVariableW, Integer indentW, String extraAttributeNameW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean inclTvAttrs; 
if (inclTvAttrsW == null) {
inclTvAttrs = false;
} else {
inclTvAttrs = inclTvAttrsW;
}

boolean inclNonTvAttrs; 
if (inclNonTvAttrsW == null) {
inclNonTvAttrs = false;
} else {
inclNonTvAttrs = inclNonTvAttrsW;
}

int onlyThisAttribute; 
if (onlyThisAttributeW == null) {
onlyThisAttribute = -1;
} else {
onlyThisAttribute = onlyThisAttributeW;
}

String tabVariable; 
if (tabVariableW == null) {
tabVariable = "T";
} else {
tabVariable = tabVariableW;
}

String idVariable; 
if (idVariableW == null) {
idVariable = M01_ACM.conOid;
} else {
idVariable = idVariableW;
}

String idRefVariable; 
if (idRefVariableW == null) {
idRefVariable = M01_ACM.conOid;
} else {
idRefVariable = idRefVariableW;
}

int indent; 
if (indentW == null) {
indent = 9;
} else {
indent = indentW;
}

String extraAttributeName; 
if (extraAttributeNameW == null) {
extraAttributeName = "";
} else {
extraAttributeName = extraAttributeNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + ",XMLELEMENT (NAME \"" + xmlElementName.toUpperCase() + "S\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "NAME \"" + xmlElementName.toUpperCase() + "\",");

int i;
for (int i = 1; i <= attrRefs.numDescriptors; i++) {
if (onlyThisAttribute == -1 |  (onlyThisAttribute == attrRefs.descriptors[i].refIndex)) {
if ((inclTvAttrs ? M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying : false) |  (inclNonTvAttrs ? !(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying) : false)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "XMLELEMENT (NAME \"" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName.toUpperCase() + "\", L." + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName.toUpperCase() + "),");
}
}
}

if (extraAttributeName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "XMLELEMENT (NAME \"" + extraAttributeName.toUpperCase() + "\", L." + extraAttributeName.toUpperCase() + "),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "XMLELEMENT (NAME " + "\"" + M01_Globals.g_anLanguageId + "\", " + "L." + M01_Globals.g_anLanguageId + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ") FROM " + qualNlTabName + " AS L WHERE L." + idRefVariable.toUpperCase() + " = " + tabVariable + "." + idVariable.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
}


private static void genXmlExportViewDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String sectionName;
String sectionShortName;
int sectionIndex;
int orMappingEntityIndex;
String entityTypeDescr;
String entityName;
String entityShortName;
String entityNameDb;
String orEntityShortName;
String classIdStr;
boolean hasOwnTable;
String qualTabName;
String qualTabNameGen;
String qualViewName;
String qualViewNameLdm;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean hasGenTab;
boolean isCtoAliasCreated;
boolean isPsTagged;
boolean useSurrogateKey;
int navToDivRelRefIndex;// follow this relationship when navigating to Division
Integer navToDivDirection;// indicates wheter we need to follow left or right hand side to navigate to Division
Integer navToFirstClassToDivDirection;// if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
int navRefClassIndex;
String navRefClassShortName;
String fkAttrToClass;

//On Error GoTo ErrorExit 

orMappingEntityIndex = -1;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
orMappingEntityIndex = M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex;
orEntityShortName = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex].shortName;
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Class";
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityNameDb = entityName;
classIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
hasGenTab = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;

navToFirstClassToDivDirection = -1;
navToDivRelRefIndex = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv.navDirection;
navRefClassIndex = -1;

qualTabName = M04_Utilities.genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, null, null);
if (M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity) {
qualTabNameGen = M04_Utilities.genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, null, null, null, null);
} else {
qualTabNameGen = "";
}

qualViewName = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
qualViewNameLdm = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, false, null, null, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
orMappingEntityIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex;
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Relationship";
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityNameDb = entityName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
orEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
classIdStr = "";
hasOwnTable = true;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
hasGenTab = false;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
useSurrogateKey = M03_Config.useSurrogateKeysForNMRelationships;

navToFirstClassToDivDirection = M23_Relationship.g_relationships.descriptors[acmEntityIndex].navPathToDiv.navDirectionToClass;
navToDivRelRefIndex = -1;
navToDivDirection = -1;
if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etLeft) {
// we need to follow relationship to left -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, navRefClassShortName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].navPathToDiv.navDirection;
} else if (navToFirstClassToDivDirection == M01_Common.RelNavigationDirection.etRight) {
// we need to follow relationship to right -> figure out what the complete path to Division is
navRefClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex;
navRefClassShortName = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName;
fkAttrToClass = M04_Utilities.genSurrogateKeyName(ddlType, M23_Relationship.g_relationships.descriptors[acmEntityIndex].lrShortRelName, null, null, null, null);
navToDivRelRefIndex = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].navPathToDiv.relRefIndex;
navToDivDirection = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].navPathToDiv.navDirection;
}

qualTabName = M04_Utilities.genQualTabNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null);
qualViewName = M04_Utilities.genQualViewNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
qualViewNameLdm = M04_Utilities.genQualViewNameByRelIndex(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, false, null, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
orMappingEntityIndex = M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex;
sectionName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName;
sectionShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Enumeration";
entityName = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;
entityShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].shortName;
orEntityShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].shortName;
entityNameDb = M21_Enum.g_enums.descriptors[acmEntityIndex].enumNameDb;
classIdStr = "";
hasOwnTable = true;
isCommonToOrgs = M21_Enum.g_enums.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M21_Enum.g_enums.descriptors[acmEntityIndex].isCommonToPools;
hasGenTab = false;
isCtoAliasCreated = M21_Enum.g_enums.descriptors[acmEntityIndex].isCtoAliasCreated;
isPsTagged = false;
useSurrogateKey = M03_Config.useSurrogateKeysForNMRelationships;

qualTabName = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);
qualViewName = M04_Utilities.genQualViewNameByEnumIndex(M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
qualViewNameLdm = M04_Utilities.genQualViewNameByEnumIndex(M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, false, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null, null);
}

String parentOidFk;
parentOidFk = M04_Utilities.genSurrogateKeyName(ddlType, orEntityShortName, null, null, null, null);

if (M03_Config.generateXmlExportViews) {
// ####################################################################################################################
// #    View to generate XML-Export for entity
// ####################################################################################################################

if (entityName.compareTo("TaxParameter") == 0) {
M00_FileWriter.printToFile(fileNo, "");
}


M22_Class_Utilities.printSectionHeader("View generating XML-Export of " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
if (M03_Config.generateXmlPsOidColForPsTaggedEntities &  isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.conPsOid, M01_ACM_IVK.cosnPsOid, M01_ACM.dxnOid, M01_ACM.dnOid, acmEntityType, acmEntityIndex, null, true, ddlType, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomXml, M01_Common.AttrCategory.eacPsOid |  M01_Common.AttrCategory.eacFkOid, null, 1, null, null));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.conXmlRecord, M01_ACM_IVK.cosnXmlRecord, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexXmlRecord, acmEntityType, acmEntityIndex, null, false, ddlType, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomXml, M01_Common.AttrCategory.eacRegular, null, 1, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
if (M03_Config.generateXmlPsOidColForPsTaggedEntities &  isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "XMLSERIALIZE (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CONTENT XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NAME \"ROWS\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "NAME \"ROW\",");
M24_Attribute_Utilities.AttributeListTransformation transformation;

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, true, null, "XMLELEMENT (NAME \"", null, null, null, null, null, null, ")", "\", T.", null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", null, null);

transformation.domainRefs.numRefs = 0;
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, "XMLELEMENT (NAME \"" + M01_Globals_IVK.g_anValue + "\", CAST(NULL AS VARCHAR(1)))", M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].domainName, null);

String nlObjName;
String nlObjShortName;
String qualNlTabName;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, false, false, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

// generations
if (M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity) {
M24_Attribute_Utilities.AttributeListTransformation transformationGen;
transformationGen = transformation;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + ",XMLELEMENT (NAME \"GENERATIONS\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "NAME \"GENERATION\",");

transformationGen.attributeRepeatDelimiter = "\", G.";
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 13, false, true, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

transformation.domainRefs = transformationGen.domainRefs;
// NL-attributes for GEN table
if (transformationGen.numNlAttrRefsTv > 0) {
qualNlTabName = M04_Utilities.genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, true, false, null, "G", M01_Globals.g_anOid, parentOidFk, 14, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + ") FROM " + qualTabNameGen + " AS G WHERE T." + M01_Globals.g_anOid + " = G." + parentOidFk);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + ")");

}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, null, null, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomXml);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "XMLELEMENT (NAME " + "\"" + M01_Globals.g_anEnumId + "\", " + "T." + M01_Globals.g_anEnumId + ")" + (M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M03_Config.xmlExportColumnVersionId ? "," : ""));

M22_Class.genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, M01_Common.DdlOutputMode.edomList |  M01_Common.DdlOutputMode.edomXml, M03_Config.xmlExportColumnVersionId);
}

// NL-attributes for non-GEN table
if ((acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) |  ((!(hasGenTab) ? transformation.numNlAttrRefsTv : 0) + transformation.numNlAttrRefsNonTv > 0)) {
qualNlTabName = M04_Utilities.genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anEnumId, M01_Globals.g_anEnumRefId, 8, M01_Globals.g_anEnumLabelText);
} else {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anOid, parentOidFk, 8, null);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS CLOB(" + String.valueOf(M03_Config.maxXmlExportStringLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " AS T");

if (!(hasOwnTable &  !(classIdStr.compareTo("") == 0))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anCid + " = '" + classIdStr + "'");
}

if (M03_Config.generateXmlPsOidColForPsTaggedEntities &  isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_ACM_IVK.conPsOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M22_Class.genAliasDdl(sectionIndex, entityNameDb, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, false, false, false, false, false, "XML-Export-View \"" + sectionName + "." + entityName + "\"", M01_LDM_IVK.gc_xmlObjNameSuffix, null, null, null, null, null, null, null);
}
}

if (M03_Config.generateXmlExportFuncs) {
String qualFuncName;

// ####################################################################################################################
// #    Function generating XML-Export for entity (all-in-one)
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null);
M22_Class_Utilities.printSectionHeader("Function generating " + (isPsTagged ? "PS-specific " : "") + "XML-Export of " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\" (all-in-one)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (isPsTagged) {
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, "OID of the ProductStructure to export records for");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "record CLOB(" + String.valueOf(M03_Config.maxXmlExportStringLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "XMLSERIALIZE (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CONTENT XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "NAME \"ROWS\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "NAME \"ROW\",");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, true, null, "XMLELEMENT (NAME \"", null, null, null, null, null, null, ")", "\", T.", null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, "XMLELEMENT (NAME \"" + M01_Globals_IVK.g_anValue + "\", CAST(NULL AS VARCHAR(1)))", M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].domainName, null);

transformation.domainRefs.numRefs = 0;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, false, false, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

// generations
if (M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity) {
transformationGen = transformation;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + ",XMLELEMENT (NAME \"GENERATIONS\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(12) + "NAME \"GENERATION\",");

transformationGen.attributeRepeatDelimiter = "\", G.";
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 13, false, true, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

transformation.domainRefs = transformationGen.domainRefs;
// NL-attributes for GEN table
if (transformationGen.numNlAttrRefsTv > 0) {
qualNlTabName = M04_Utilities.genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, true, false, null, "G", M01_Globals.g_anOid, parentOidFk, 13, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(11) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + ") FROM " + qualTabNameGen + " AS G WHERE T." + M01_Globals.g_anOid + " = G." + parentOidFk);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + ")");

}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, null, null, false, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "XMLELEMENT (NAME " + "\"" + M01_Globals.g_anEnumId + "\", " + "T." + M01_Globals.g_anEnumId + ")" + (M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M03_Config.xmlExportColumnVersionId ? "," : ""));

M22_Class.genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, M01_Common.DdlOutputMode.edomValue |  M01_Common.DdlOutputMode.edomXml, M03_Config.xmlExportColumnVersionId);
}

// NL-attributes for non-GEN table
if ((acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) |  ((!(hasGenTab) ? transformation.numNlAttrRefsTv : 0) + transformation.numNlAttrRefsNonTv > 0)) {
qualNlTabName = M04_Utilities.genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anEnumId, M01_Globals.g_anEnumRefId, 8, M01_Globals.g_anEnumLabelText);
} else {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anOid, parentOidFk, 8, null);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS CLOB(" + M03_Config.maxXmlExportStringLength + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " AS T");

if (!(hasOwnTable &  !(classIdStr.compareTo("") == 0))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals.g_anCid + " = '" + classIdStr + "'");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_ACM_IVK.conPsOid + " = psOid_in");
}
} else if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_ACM_IVK.conPsOid + " = psOid_in");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function generating XML-Export for entity (segmented)
// ####################################################################################################################

qualFuncName = M04_Utilities.genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, null, null, M01_LDM_IVK.gc_xmlObjNameSuffix, null);
M22_Class_Utilities.printSectionHeader("Function generating " + (isPsTagged ? "PS-specific " : "") + "XML-Export of " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\" (segmented)", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "", "startRecord_in", "BIGINT", true, "'first' record number to retrieve (starting with 1 and based on ascending OID-ordering)");
M11_LRT.genProcParm(fileNo, "", "maxRecords_in", "INTEGER", isPsTagged, "maximum number of records to retrieve in one segment");

if (isPsTagged) {
M11_LRT.genProcParm(fileNo, "", "psOid_in", M01_Globals.g_dbtOid, false, "OID of the ProductStructure to export records for");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "totalRecords BIGINT, -- total number of records in a 'full export'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "record       CLOB(" + String.valueOf(M03_Config.maxXmlExportStringLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_T_Ordered");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, null, null, "T.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, "T." + M01_Globals_IVK.g_anValue + "", M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].domainName, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY T.ID ASC) AS ROWNUM,");
// todo: shouldn't we include this in 'genAttrList...'-Subroutine?
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.ID" + (M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M03_Config.xmlExportColumnVersionId ? "," : ""));
} else {
if (useSurrogateKey) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY T." + M01_Globals.g_anOid + " ASC) AS ROWNUM,");
} else {
String pkAttrList;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
pkAttrList = M24_Attribute.getPkAttrListByClass(acmEntityIndex, ddlType, "T.", null, null, null);
}
if (pkAttrList.compareTo("") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, null, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS ROWNUM,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY " + pkAttrList + ") AS ROWNUM,");
}
}
}

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, M03_Config.xmlExportColumnVersionId, null, null, (M01_Common.DdlOutputMode.edomListNonLrt & ! M01_Common.DdlOutputMode.edomExpression) |  M01_Common.DdlOutputMode.edomXml | M01_Common.DdlOutputMode.edomExpressionRef, M01_Common.RecursionDirection.erdUp);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabName + " AS T");
if (!(hasOwnTable &  !(classIdStr.compareTo("") == 0))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anCid + " = '" + classIdStr + "'");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_ACM_IVK.conPsOid + " = psOid_in");
}
} else if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_ACM_IVK.conPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT COUNT(*) FROM V_T_Ordered),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "XMLSERIALIZE (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CONTENT XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "NAME \"ROW\",");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, null, true, null, "XMLELEMENT (NAME \"", null, null, null, null, null, null, ")", "\", T.", null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, "XMLELEMENT (NAME \"" + M01_Globals_IVK.g_anValue + "\", CAST(NULL AS VARCHAR(1)))", M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].domainName, null);

transformation.domainRefs.numRefs = 0;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, false, false, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

// generations
if (M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity) {
transformationGen = transformation;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ",XMLELEMENT (NAME \"GENERATIONS\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "XMLAGG (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "XMLELEMENT (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "NAME \"GENERATION\",");

transformationGen.attributeRepeatDelimiter = "\", G.";
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 11, false, true, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml, M01_Common.RecursionDirection.erdUp);

transformation.domainRefs = transformationGen.domainRefs;
// NL-attributes for GEN table
if (transformationGen.numNlAttrRefsTv > 0) {
qualNlTabName = M04_Utilities.genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, null, null, null);

genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, true, false, null, "G", M01_Globals.g_anOid, parentOidFk, 11, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + ") FROM " + qualTabNameGen + " AS G WHERE T." + M01_Globals.g_anOid + " = G." + M22_Class.g_classes.descriptors[orMappingEntityIndex].shortName + "_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ")");

}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, null, null, false, M01_Common.DdlOutputMode.edomValueNonLrt |  M01_Common.DdlOutputMode.edomValueExpression | M01_Common.DdlOutputMode.edomXml);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "XMLELEMENT (NAME " + "\"" + M01_Globals.g_anEnumId + "\", " + "T." + M01_Globals.g_anEnumId + ")" + (M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M03_Config.xmlExportColumnVersionId ? "," : ""));

M22_Class.genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, M01_Common.DdlOutputMode.edomValue |  M01_Common.DdlOutputMode.edomXml, M03_Config.xmlExportColumnVersionId);
}

// NL-attributes for non-GEN table
if ((!(hasGenTab) ? transformation.numNlAttrRefsTv : 0) + transformation.numNlAttrRefsNonTv > 0) {
qualNlTabName = M04_Utilities.genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anEnumId, M01_Globals.g_anEnumRefId, 7, M01_Globals.g_anEnumLabelText);
} else {
genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, !(hasGenTab), true, null, null, M01_Globals.g_anOid, parentOidFk, 7, null);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") AS CLOB(" + M03_Config.maxXmlExportStringLength + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_T_Ordered T");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ROWNUM BETWEEN COALESCE(startRecord_in, 1) AND COALESCE(startRecord_in + maxRecords_in - 1, 9999999999999999999999999999999)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genXmlExportXsdFuncSupportForNlTable(M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, String xmlElementName, int fileNo, Integer ddlTypeW, Integer onlyThisAttributeW, Boolean inclTvAttrsW, Boolean inclNonTvAttrsW, Integer indentW, String extraAttributeNameW, String extraSectionNameW, String extraDomainNameW, Boolean extraIsNullableW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int onlyThisAttribute; 
if (onlyThisAttributeW == null) {
onlyThisAttribute = -1;
} else {
onlyThisAttribute = onlyThisAttributeW;
}

boolean inclTvAttrs; 
if (inclTvAttrsW == null) {
inclTvAttrs = false;
} else {
inclTvAttrs = inclTvAttrsW;
}

boolean inclNonTvAttrs; 
if (inclNonTvAttrsW == null) {
inclNonTvAttrs = false;
} else {
inclNonTvAttrs = inclNonTvAttrsW;
}

int indent; 
if (indentW == null) {
indent = 9;
} else {
indent = indentW;
}

String extraAttributeName; 
if (extraAttributeNameW == null) {
extraAttributeName = "";
} else {
extraAttributeName = extraAttributeNameW;
}

String extraSectionName; 
if (extraSectionNameW == null) {
extraSectionName = "";
} else {
extraSectionName = extraSectionNameW;
}

String extraDomainName; 
if (extraDomainNameW == null) {
extraDomainName = "";
} else {
extraDomainName = extraDomainNameW;
}

boolean extraIsNullable; 
if (extraIsNullableW == null) {
extraIsNullable = false;
} else {
extraIsNullable = extraIsNullableW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "<element name=\"" + xmlElementName.toUpperCase() + "S\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "<sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "<element name=\"" + xmlElementName.toUpperCase() + "\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 4) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 5) + "<sequence>'" + " || cr ||");

int i;
for (int i = 1; i <= attrRefs.numDescriptors; i++) {
if (onlyThisAttribute == -1 |  (onlyThisAttribute == attrRefs.descriptors[i].refIndex)) {
if ((inclTvAttrs ? M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying : false) |  (inclNonTvAttrs ? !(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying) : false)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 6) + "<element name=\"" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName.toUpperCase() + "\" type=\"standardxml:" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].sectionName + "_" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainName + (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNullable ? "_N" : "") + "\"/>' || cr ||");
}
}
}

if (extraAttributeName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 6) + "<element name=\"" + extraAttributeName.toUpperCase() + "\" type=\"standardxml:" + extraSectionName + "_" + extraDomainName + (extraIsNullable ? "_N" : "") + "\"/>' || cr ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 6) + "<element name=\"" + M01_Globals.g_anLanguageId + "\" type=\"standardxml:" + M01_Globals.g_anLanguageId + "\"/>' || cr ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 5) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 4) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "</element>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "</element>'" + " || cr ||");
}


private static String xmlNormalizedEntityName(String entityName) {
String returnValue;
returnValue = entityName.substring(0, 1).toUpperCase() + entityName.substring(entityName.length() - 1 - entityName.length() - 1).toLowerCase();
return returnValue;
}


private static void genXmlExportXsdFuncForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.generateXmlXsdFuncs)) {
return;
}

String sectionName;
String sectionShortName;
int sectionIndex;
String entityTypeDescr;
String entityName;
String entityShortName;
String qualFuncName;
boolean isXsdExported;
boolean hasGenTab;
int maxStrLength;

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Class";
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasGenTab = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isXsdExported = M22_Class.g_classes.descriptors[acmEntityIndex].isXsdExported;
maxStrLength = 0;
M22_Class.g_classes.descriptors[acmEntityIndex].isXsdExported = true;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Relationship";
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
hasGenTab = false;
isXsdExported = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isXsdExported;
maxStrLength = 0;

M23_Relationship.g_relationships.descriptors[acmEntityIndex].isXsdExported = true;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
sectionName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName;
sectionShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionIndex;
entityTypeDescr = "ACM-Enumeration";
entityName = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;
entityShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].shortName;
hasGenTab = false;
isXsdExported = M21_Enum.g_enums.descriptors[acmEntityIndex].isXsdExported;
maxStrLength = M21_Enum.g_enums.descriptors[acmEntityIndex].maxLength;

M21_Enum.g_enums.descriptors[acmEntityIndex].isXsdExported = true;
}

if (isXsdExported) {
return;
}

if (M03_Config.generateXsdInCtoSchema) {
qualFuncName = M04_Utilities.genQualFuncName(sectionIndex, entityShortName, ddlType, null, null, null, null, M01_LDM_IVK.gc_xsdObjNameSuffix, null);
} else {
qualFuncName = M04_Utilities.genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, null, null, M01_LDM_IVK.gc_xsdObjNameSuffix, null);
}

M22_Class_Utilities.printSectionHeader("Function generating XSD for " + entityTypeDescr + " \"" + sectionName + "." + entityName + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncName + " ()");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(32000)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE cr CHAR(1);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET cr = CHR(10);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "<schema xmlns:standardxml=\"http://ivkmds.dcx.com/ASBO/StandardXML\"' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "targetNamespace=\"http://ivkmds.dcx.com/ASBO/StandardXML\"' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "xmlns=\"http://www.w3.org/2001/XMLSchema\">' || cr || cr ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "<element name=\"ACM-" + xmlNormalizedEntityName(entityName) + (char)34 + ">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<sequence>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(3) + "<element name=\"ROWS\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(4) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(5) + "<sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(6) + "<element name=\"ROW\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(7) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(8) + "<sequence>'" + " || cr ||");

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 6, true, true, null, null, null, null, null, null, null, null, null, null, null, M03_Config.xmlExportVirtualColumns, M03_Config.xmlExportVirtualColumns);
transformation.distinguishNullabilityForDomainRefs = true;
M24_Attribute_Utilities.AttributeListTransformation transformationGen;
transformationGen.distinguishNullabilityForDomainRefs = true;
M24_Attribute_Utilities.initAttributeTransformation(transformationGen, 0, true, true, null, null, null, null, null, null, null, null, null, null, null, M03_Config.xmlExportVirtualColumns, M03_Config.xmlExportVirtualColumns);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexTemplateFileData].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conValue, M01_Globals_IVK.g_anValue, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBinaryPropertyValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexBIBRegistryValue].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexLongText].domainName, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, null, null, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].sectionName, M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexReportFileData].domainName, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, false, false, M01_Common.DdlOutputMode.edomXsd |  (M03_Config.xmlExportVirtualColumns ? M01_Common.DdlOutputMode.edomXmlVirtual : M01_Common.DdlOutputMode.edomNone), M01_Common.RecursionDirection.erdUp);

//generations
if (M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming & ! M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity) {
transformationGen = transformation;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(9) + "<element name=\"GENERATIONS\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(10) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(11) + "<sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(12) + "<element name=\"GENERATION\" minOccurs=\"0\" maxOccurs=\"unbounded\">'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(13) + "<complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(14) + "<sequence>'" + " || cr ||");

//including generation elements
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 12, false, true, M01_Common.DdlOutputMode.edomXsd |  (M03_Config.xmlExportVirtualColumns ? M01_Common.DdlOutputMode.edomXmlVirtual : M01_Common.DdlOutputMode.edomNone), M01_Common.RecursionDirection.erdUp);
transformation.domainRefs = transformationGen.domainRefs;

// NL-attributes for GEN table
if (transformationGen.numNlAttrRefsTv > 0) {
genXmlExportXsdFuncSupportForNlTable(transformationGen.nlAttrRefs, "NlText", fileNo, ddlType, null, true, false, 15, null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(14) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(13) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(12) + "</element>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(11) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(10) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(9) + "</element>'" + " || cr ||");
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, null, null, false, M01_Common.DdlOutputMode.edomXsd |  (M03_Config.xmlExportVirtualColumns ? M01_Common.DdlOutputMode.edomXmlVirtual : M01_Common.DdlOutputMode.edomNone));
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(9) + "<element name=\"" + M01_Globals.g_anEnumId + "\" type=\"standardxml:" + sectionName + M01_Globals.gc_enumAttrNameSuffix + "\"/>' || cr ||");

M22_Class.genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, M01_Common.DdlOutputMode.edomXsd |  (M03_Config.xmlExportVirtualColumns ? M01_Common.DdlOutputMode.edomXmlVirtual : M01_Common.DdlOutputMode.edomNone), null);
}

// NL-attributes for non-GEN table
if ((acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) |  ((!(hasGenTab) ? transformation.numNlAttrRefsTv : 0) + transformation.numNlAttrRefsNonTv > 0)) {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
genXmlExportXsdFuncSupportForNlTable(transformation.nlAttrRefs, "NlText", fileNo, ddlType, null, false, true, null, M01_ACM.conEnumLabelText, M01_ACM.snCommon, "EnumText", false);
} else {
genXmlExportXsdFuncSupportForNlTable(transformation.nlAttrRefs, "NlText", fileNo, ddlType, null, !(hasGenTab), true, null, null, null, null, null);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(8) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(7) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(6) + "</element>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(5) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(4) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(3) + "</element>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "</sequence>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</complexType>'" + " || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "</element>'" + " || cr || cr ||");
M00_FileWriter.printToFile(fileNo, "");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
genSimpleTypesForXML(fileNo, transformation, M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName, null, M21_Enum.g_enums.descriptors[acmEntityIndex].idDataType, null, val(M21_Enum.g_enums.descriptors[acmEntityIndex].maxLength), null, null, null, true, M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName + M01_Globals.gc_enumAttrNameSuffix);

genSimpleTypesForXML(fileNo, transformation, "Common", "EnumText", M01_Common.typeId.etVarchar, null, String.valueOf(maxStrLength), null, null, null, true, null);
genSimpleTypesForXML(fileNo, transformation, "LANGUAGE", "ID", M01_Common.typeId.etInteger, null, null, null, null, null, true, null);
}

if (transformation.nlAttrRefs.numDescriptors > 0) {
genSimpleTypesForXML(fileNo, transformation, sectionName, null, M01_Common.typeId.etSmallint, null, null, null, null, null, true, M01_Globals.g_anLanguageId);
} else if (transformationGen.nlAttrRefs.numDescriptors > 0) {
genSimpleTypesForXML(fileNo, transformationGen, sectionName, null, M01_Common.typeId.etSmallint, null, null, null, null, null, true, M01_Globals.g_anLanguageId);
}

genSimpleTypesForXML(fileNo, transformation, null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "</schema>';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genXmlExportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNoF, int fileNoV, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.generateXmlExportSupport |  classIndex < 1)) {
return;
}

if (M22_Class.g_classes.descriptors[classIndex].notAcmRelated |  M22_Class.g_classes.descriptors[classIndex].isAbstract | M22_Class.g_classes.descriptors[classIndex].noXmlExport) {
return;
}

genXmlExportXsdFuncForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoF, ddlType);
genXmlExportViewDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNoV, ddlType);
}


public static void genXmlExportDdlForEnum(int thisEnumIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNoF, int fileNoV, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.generateXmlExportSupport |  thisEnumIndex < 1)) {
return;
}
if (M21_Enum.g_enums.descriptors[thisEnumIndex].notAcmRelated) {
return;
}

genXmlExportXsdFuncForEntity(thisEnumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, thisOrgIndex, thisPoolIndex, fileNoF, ddlType);
genXmlExportViewDdlForEntity(thisEnumIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactEnum, thisOrgIndex, thisPoolIndex, fileNoV, ddlType);
}


public static void genXmlExportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNoF, int fileNoV, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.generateXmlExportSupport |  thisRelIndex < 1)) {
return;
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].notAcmRelated |  M23_Relationship.g_relationships.descriptors[thisRelIndex].noXmlExport) {
return;
}

genXmlExportXsdFuncForEntity(thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoF, ddlType);
genXmlExportViewDdlForEntity(thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNoV, ddlType);
}


private static void genSimpleTypesForXML(int fileNo, M24_Attribute_Utilities.AttributeListTransformation transformation, String sectionNameW, String domainNameW, Integer dataTypeW, String minLengthW, String maxLengthW, Integer xscaleW, String minValueW, String maxValueW, Boolean singleTypeW, String attrNameW) {
String sectionName; 
if (sectionNameW == null) {
sectionName = "";
} else {
sectionName = sectionNameW;
}

String domainName; 
if (domainNameW == null) {
domainName = "";
} else {
domainName = domainNameW;
}

Integer dataType; 
if (dataTypeW == null) {
dataType = null;
} else {
dataType = dataTypeW;
}

String minLength; 
if (minLengthW == null) {
minLength = "";
} else {
minLength = minLengthW;
}

String maxLength; 
if (maxLengthW == null) {
maxLength = "";
} else {
maxLength = maxLengthW;
}

int xscale; 
if (xscaleW == null) {
xscale = 0;
} else {
xscale = xscaleW;
}

String minValue; 
if (minValueW == null) {
minValue = "";
} else {
minValue = minValueW;
}

String maxValue; 
if (maxValueW == null) {
maxValue = "";
} else {
maxValue = maxValueW;
}

boolean singleType; 
if (singleTypeW == null) {
singleType = false;
} else {
singleType = singleTypeW;
}

String attrName; 
if (attrNameW == null) {
attrName = "";
} else {
attrName = attrNameW;
}

if (singleType == true) {
printSimpleTypeForXML(dataType, fileNo, sectionName, domainName, minLength, maxLength, xscale, minValue, maxValue, attrName, null);
} else {
int i;
for (i = 1; i <= 1; i += (1)) {
if (M03_Config.supportUnicode &  M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].supportUnicode & !(M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxLength.compareTo("") == 0)) {
printSimpleTypeForXML(M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].dataType, fileNo, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].sectionName, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].domainName, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].minLength, (M03_Config.supportUnicode &  M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].supportUnicode ? new Double(M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].unicodeExpansionFactor * new Double(M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxLength).intValue()).intValue() + "" : M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxLength), M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].scale, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].minValue, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxValue, null, transformation.domainRefs.refs[i].isNullable);
} else {
printSimpleTypeForXML(M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].dataType, fileNo, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].sectionName, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].domainName, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].minLength, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxLength, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].scale, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].minValue, M25_Domain.g_domains.descriptors[transformation.domainRefs.refs[i].ref].maxValue, null, transformation.domainRefs.refs[i].isNullable);
}
}
}
}


private static void printSimpleTypeForXMLCore(int fileNo, String typeStr, boolean isOptional, Integer indentW, String minValueW, String maxValueW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String minValue; 
if (minValueW == null) {
minValue = "";
} else {
minValue = minValueW;
}

String maxValue; 
if (maxValueW == null) {
maxValue = "";
} else {
maxValue = maxValueW;
}

if (isOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "<union>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "<simpleType>' || cr ||");

if (minValue.compareTo("") == 0 |  maxValue.compareTo("") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "<restriction base=\"" + typeStr + "\"/>' || cr ||");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "<restriction base=\"" + typeStr + "\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "<minInclusive value=\"" + minValue + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "<maxInclusive value=\"" + maxValue + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "</restriction>' || cr ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "</simpleType>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "<simpleType>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "<restriction base=\"string\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "<maxLength value=\"0\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 2) + "</restriction>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "</simpleType>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "</union>' || cr ||");
} else {
if (minValue.compareTo("") == 0 |  maxValue.compareTo("") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "<restriction base=\"" + typeStr + "\"/>' || cr ||");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "<restriction base=\"" + typeStr + "\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "<minInclusive value=\"" + minValue + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 1) + "<maxInclusive value=\"" + maxValue + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 0) + "</restriction>' || cr ||");
}
}
}

private static void printSimpleTypeForXML(Integer dataType, int fileNo, String sectionNameW, String domainNameW, String minLengthW, String maxLengthW, Integer xscaleW, String minValueW, String maxValueW, String attrNameW, Boolean isOptionalW) {
String sectionName; 
if (sectionNameW == null) {
sectionName = null;
} else {
sectionName = sectionNameW;
}

String domainName; 
if (domainNameW == null) {
domainName = null;
} else {
domainName = domainNameW;
}

String minLength; 
if (minLengthW == null) {
minLength = null;
} else {
minLength = minLengthW;
}

String maxLength; 
if (maxLengthW == null) {
maxLength = null;
} else {
maxLength = maxLengthW;
}

int xscale; 
if (xscaleW == null) {
xscale = 0;
} else {
xscale = xscaleW;
}

String minValue; 
if (minValueW == null) {
minValue = "";
} else {
minValue = minValueW;
}

String maxValue; 
if (maxValueW == null) {
maxValue = "";
} else {
maxValue = maxValueW;
}

String attrName; 
if (attrNameW == null) {
attrName = "";
} else {
attrName = attrNameW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

if (!(attrName.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "<simpleType name=\"" + attrName + (isOptional ? "_N" : "") + "\">' || cr ||");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "<simpleType name=\"" + sectionName + "_" + domainName + (isOptional ? "_N" : "") + "\">' || cr ||");
}

if (dataType == M01_Common.typeId.etBigInt) {
printSimpleTypeForXMLCore(fileNo, "long", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etBinVarchar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"0\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etBlob) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"0\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etChar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
if (isOptional |  !(minLength.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"" + String.valueOf((isOptional ? 0 : minLength)) + "\"/>' || cr ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<maxLength value=\"" + String.valueOf(maxLength) + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etBinChar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
if (isOptional |  !(minLength.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"" + String.valueOf((isOptional ? 0 : minLength)) + "\"/>' || cr ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<maxLength value=\"" + String.valueOf(maxLength) + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etClob) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"0\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etDate) {
printSimpleTypeForXMLCore(fileNo, "date", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etDecimal) {
printSimpleTypeForXMLCore(fileNo, "decimal", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etDouble) {
printSimpleTypeForXMLCore(fileNo, "double", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etFloat) {
printSimpleTypeForXMLCore(fileNo, "float", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etInteger) {
printSimpleTypeForXMLCore(fileNo, "integer", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etLongVarchar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
if (isOptional |  !(minLength.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"" + String.valueOf((isOptional ? 0 : minLength)) + "\"/>' || cr ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<maxLength value=\"" + String.valueOf(maxLength) + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (sectionName.compareTo(M01_ACM.dxnBoolean) == 0 &  domainName.compareTo(M01_ACM.dnBoolean) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"boolean\">' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<pattern value=\"0\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<pattern value=\"1\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

} else if (dataType == M01_Common.typeId.etSmallint) {
printSimpleTypeForXMLCore(fileNo, "int", isOptional, null, minValue, maxValue);

} else if (dataType == M01_Common.typeId.etTime) {
printSimpleTypeForXMLCore(fileNo, "time", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etTimestamp) {
printSimpleTypeForXMLCore(fileNo, "dateTime", isOptional, null, null, null);

} else if (dataType == M01_Common.typeId.etVarchar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "<restriction base=\"string\">' || cr ||");
if (isOptional |  !(minLength.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<minLength value=\"" + String.valueOf((isOptional ? 0 : minLength)) + "\"/>' || cr ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(2) + "<maxLength value=\"" + String.valueOf(maxLength) + "\"/>' || cr ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(1) + "</restriction>' || cr ||");

}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(0) + "</simpleType>' || cr ||");
}

// ### ENDIF IVK ###

}