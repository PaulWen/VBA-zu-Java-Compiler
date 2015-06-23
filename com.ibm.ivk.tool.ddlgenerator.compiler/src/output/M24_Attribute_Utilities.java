package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M24_Attribute_Utilities {




public class AttrDescriptorRefType {
public static final int eadrtAttribute = 1;
public static final int eadrtEnum = 2;
public static final int eadrtType = 3;
}

class AttrDescriptorRef {
public int refIndex;
public Integer refType;

public AttrDescriptorRef(int refIndex, Integer refType) {
this.refIndex = refIndex;
this.refType = refType;
}
}

class AttrDescriptorRefs {
public M24_Attribute_Utilities.AttrDescriptorRef[] descriptors;
public int numDescriptors;

public AttrDescriptorRefs(int numDescriptors, M24_Attribute_Utilities.AttrDescriptorRef[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

class OidColDescriptor {
public String colName;
public Integer colCat;

public OidColDescriptor(String colName, Integer colCat) {
this.colName = colName;
this.colCat = colCat;
}
}

class OidColDescriptors {
public M24_Attribute_Utilities.OidColDescriptor[] descriptors;
public int numDescriptors;

public OidColDescriptors(int numDescriptors, M24_Attribute_Utilities.OidColDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

public class AcmAttrContainerType {
public static final int eactClass = 1;
public static final int eactRelationship = 2;
public static final int eactEnum = 3;
// ### IF IVK ###
public static final int eactType = 4;
public static final int eactView = 5;
// ### ELSE IVK ###
// eactView = 4
// ### ENDIF IVK ###
}

public class AttrValueType {
public static final int eavtDomain = 1;
public static final int eavtEnum = 2;
public static final int eavtDomainEnumId = 3;
public static final int eavtDomainEnumValue = 4;
}

// ### IF IVK ###
// two flavors of Attribute Mapping for ACM:
// 1) based on navigation along relationships
// 2) based on scalar SQL-expression
class AttributeMappingForACM {
public String description;
public boolean isRelBasedMapping;
public boolean isInstantiated;
public String mapTo;
public Integer navDirection;// indicates which direction to follow to the 'target class'
public int relIndex;// references the relationship which leads to the 'target class'
public int targetClassIndex;// target class

public AttributeMappingForACM(String description, boolean isRelBasedMapping, boolean isInstantiated, String mapTo, Integer navDirection, int relIndex, int targetClassIndex) {
this.description = description;
this.isRelBasedMapping = isRelBasedMapping;
this.isInstantiated = isInstantiated;
this.mapTo = mapTo;
this.navDirection = navDirection;
this.relIndex = relIndex;
this.targetClassIndex = targetClassIndex;
}
}

// ### ENDIF IVK ###
class AttributeDescriptor {
public String sectionName;
public String className;
public Integer cType;
public String attributeName;
public String shortName;
public String i18nId;
public String[] mapsToChangeLogAttributes;
// ### IF IVK ###

public M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsTo;
public M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsToForRead;
public String ftoConflictWith;
public int ftoConflictType;
public long ftoConflictMessageIdBase;
public String groupIdBasedOn;
public String[] groupIdAttributes;
public int[] groupIdAttributeIndexes;
public boolean isNationalizable;
public boolean isExpression;
public boolean noXmlExport;
public boolean isPersistent;
// ### ENDIF IVK ###

public String domainSection;
public String domainName;
public String defaultValue;
public boolean isNl;
public boolean isNullable;
public String isNullableInOrgs;
public boolean isIdentifying;
public boolean includeInPkIndex;
public boolean isTimeVarying;
public String comment;

// derived attributes
// ### IF IVK ###
public boolean isVirtual;
public int ftoConflictWithAttrIndex;
public int ftoConflictWithSrcAttrIndex;
public boolean isGroupId;
public int[] virtuallyReferredToBy;
// ### ENDIF IVK ###
public boolean isPdmSpecific;
public boolean isNotAcmRelated;
public boolean isPrimaryKey;
public Integer valueType;
public int valueTypeIndex;
public int domainIndex;
public int reusedAttrIndex;
public int[] reusingAttrIndexes;
public boolean compressDefault;
public int acmEntityIndex;
public int attrIndex;
public int attrNlIndex;
public String[] dbColName = new String[2];

public AttributeDescriptor(String sectionName, String className, Integer cType, String attributeName, String shortName, String i18nId, M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsTo, M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsToForRead, String ftoConflictWith, int ftoConflictType, long ftoConflictMessageIdBase, String groupIdBasedOn, boolean isNationalizable, boolean isExpression, boolean noXmlExport, boolean isPersistent, String domainSection, String domainName, String default, boolean isNl, boolean isNullable, String isNullableInOrgs, boolean isIdentifying, boolean includeInPkIndex, boolean isTimeVarying, String comment, boolean isVirtual, int ftoConflictWithAttrIndex, int ftoConflictWithSrcAttrIndex, boolean isGroupId, boolean isPdmSpecific, boolean isNotAcmRelated, boolean isPrimaryKey, Integer valueType, int valueTypeIndex, int domainIndex, int reusedAttrIndex, boolean compressDefault, int acmEntityIndex, int attrIndex, int attrNlIndex, String[] mapsToChangeLogAttributes, String[] groupIdAttributes, int[] groupIdAttributeIndexes, int[] virtuallyReferredToBy, int[] reusingAttrIndexes, String[] dbColName) {
this.sectionName = sectionName;
this.className = className;
this.cType = cType;
this.attributeName = attributeName;
this.shortName = shortName;
this.i18nId = i18nId;
this.virtuallyMapsTo = virtuallyMapsTo;
this.virtuallyMapsToForRead = virtuallyMapsToForRead;
this.ftoConflictWith = ftoConflictWith;
this.ftoConflictType = ftoConflictType;
this.ftoConflictMessageIdBase = ftoConflictMessageIdBase;
this.groupIdBasedOn = groupIdBasedOn;
this.isNationalizable = isNationalizable;
this.isExpression = isExpression;
this.noXmlExport = noXmlExport;
this.isPersistent = isPersistent;
this.domainSection = domainSection;
this.domainName = domainName;
this.default = default;
this.isNl = isNl;
this.isNullable = isNullable;
this.isNullableInOrgs = isNullableInOrgs;
this.isIdentifying = isIdentifying;
this.includeInPkIndex = includeInPkIndex;
this.isTimeVarying = isTimeVarying;
this.comment = comment;
this.isVirtual = isVirtual;
this.ftoConflictWithAttrIndex = ftoConflictWithAttrIndex;
this.ftoConflictWithSrcAttrIndex = ftoConflictWithSrcAttrIndex;
this.isGroupId = isGroupId;
this.isPdmSpecific = isPdmSpecific;
this.isNotAcmRelated = isNotAcmRelated;
this.isPrimaryKey = isPrimaryKey;
this.valueType = valueType;
this.valueTypeIndex = valueTypeIndex;
this.domainIndex = domainIndex;
this.reusedAttrIndex = reusedAttrIndex;
this.compressDefault = compressDefault;
this.acmEntityIndex = acmEntityIndex;
this.attrIndex = attrIndex;
this.attrNlIndex = attrNlIndex;
this.mapsToChangeLogAttributes = mapsToChangeLogAttributes;
this.groupIdAttributes = groupIdAttributes;
this.groupIdAttributeIndexes = groupIdAttributeIndexes;
this.virtuallyReferredToBy = virtuallyReferredToBy;
this.reusingAttrIndexes = reusingAttrIndexes;
this.dbColName = dbColName;
}
}

class AttributeMappingForCl {
public int prio;
public String mapFrom;
public String mapTo;
public boolean isTv;
public int attrIndex;

public AttributeMappingForCl(int prio, String mapFrom, String mapTo, boolean isTv, int attrIndex) {
this.prio = prio;
this.mapFrom = mapFrom;
this.mapTo = mapTo;
this.isTv = isTv;
this.attrIndex = attrIndex;
}
}

class AttributeDescriptors {
public M24_Attribute_Utilities.AttributeDescriptor[] descriptors;
public int numDescriptors;

public AttributeDescriptors(int numDescriptors, M24_Attribute_Utilities.AttributeDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

class AttributeTransformationContext {
public int orgIndex;
public int poolIndex;
public String tabQualifier;
public boolean forLrt;
public String lrtOidRef;

public AttributeTransformationContext(int orgIndex, int poolIndex, String tabQualifier, boolean forLrt, String lrtOidRef) {
this.orgIndex = orgIndex;
this.poolIndex = poolIndex;
this.tabQualifier = tabQualifier;
this.forLrt = forLrt;
this.lrtOidRef = lrtOidRef;
}
}

class AttributeTransformation {
public String attributeName;
public String domainSection;
public String domainName;
public String value;
public boolean isConstant;

public AttributeTransformation(String attributeName, String domainSection, String domainName, String value, boolean isConstant) {
this.attributeName = attributeName;
this.domainSection = domainSection;
this.domainName = domainName;
this.value = value;
this.isConstant = isConstant;
}
}

class AttributeListTransformation {
public String attributePrefix;
public String attributePostfix;
public String attributeRepeatDelimiter;// if this is set, the attribute name is transformed twice with this delimiter in between (support for XML-Export)
public boolean postProcessAfterMapping;
public int numMappings;
public M24_Attribute_Utilities.AttributeTransformation[] mappings;

public boolean containsNlAttribute;
public M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs;// optionally may be used to collect references to AttrDescriptors found during attribute transformation
public int numNlAttrRefsTv;// optionally may be used to count the number of NL attribute references in GEN table
public int numNlAttrRefsNonTv;// optionally may be used to count the number of NL attribute references in non-GEN table
public M25_Domain_Utilities.DomainDescriptorRefs domainRefs;// optionally may be used to collect references to DomainDescriptors found during attribute transformation
// ### IF IVK ###
public M24_Attribute_Utilities.AttrDescriptorRefs virtualAttrRefs;// optionally may be used to collect references to AttrDescriptors found during attribute transformation
// ### ENDIF IVK ###
public M24_Attribute_Utilities.OidColDescriptors oidDescriptors;// optionally may be used to collect infos about OID attribtes found during attribute transformation
public boolean distinguishNullabilityForDomainRefs;
public boolean ignoreConstraint;
public boolean trimRight;
public boolean suppressAllComma;

public boolean doCollectDomainDescriptors;
public boolean doCollectAttrDescriptors;
// ### IF IVK ###
public boolean doCollectVirtualDomainDescriptors;
public boolean doCollectVirtualAttrDescriptors;
// ### ENDIF IVK ###
public boolean doCollectOidColDescriptors;
public Integer oidColFilter;
public M24_Attribute_Utilities.AttributeTransformationContext conEnumLabelText;

public AttributeListTransformation(String attributePrefix, String attributePostfix, String attributeRepeatDelimiter, boolean postProcessAfterMapping, int numMappings, boolean containsNlAttribute, M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs, int numNlAttrRefsTv, int numNlAttrRefsNonTv, M25_Domain_Utilities.DomainDescriptorRefs domainRefs, M24_Attribute_Utilities.AttrDescriptorRefs virtualAttrRefs, M24_Attribute_Utilities.OidColDescriptors oidDescriptors, boolean distinguishNullabilityForDomainRefs, boolean ignoreConstraint, boolean trimRight, boolean suppressAllComma, boolean doCollectDomainDescriptors, boolean doCollectAttrDescriptors, boolean doCollectVirtualDomainDescriptors, boolean doCollectVirtualAttrDescriptors, boolean doCollectOidColDescriptors, Integer oidColFilter, M24_Attribute_Utilities.AttributeTransformationContext conEnumLabelText, M24_Attribute_Utilities.AttributeTransformation[] mappings) {
this.attributePrefix = attributePrefix;
this.attributePostfix = attributePostfix;
this.attributeRepeatDelimiter = attributeRepeatDelimiter;
this.postProcessAfterMapping = postProcessAfterMapping;
this.numMappings = numMappings;
this.containsNlAttribute = containsNlAttribute;
this.nlAttrRefs = nlAttrRefs;
this.numNlAttrRefsTv = numNlAttrRefsTv;
this.numNlAttrRefsNonTv = numNlAttrRefsNonTv;
this.domainRefs = domainRefs;
this.virtualAttrRefs = virtualAttrRefs;
this.oidDescriptors = oidDescriptors;
this.distinguishNullabilityForDomainRefs = distinguishNullabilityForDomainRefs;
this.ignoreConstraint = ignoreConstraint;
this.trimRight = trimRight;
this.suppressAllComma = suppressAllComma;
this.doCollectDomainDescriptors = doCollectDomainDescriptors;
this.doCollectAttrDescriptors = doCollectAttrDescriptors;
this.doCollectVirtualDomainDescriptors = doCollectVirtualDomainDescriptors;
this.doCollectVirtualAttrDescriptors = doCollectVirtualAttrDescriptors;
this.doCollectOidColDescriptors = doCollectOidColDescriptors;
this.oidColFilter = oidColFilter;
this.conEnumLabelText = conEnumLabelText;
this.mappings = mappings;
}
}

class EntityColumnDescriptor {
public String columnName;
public boolean isNullable;
public Integer acmEntityType;
public String acmEntityName;
public String acmAttributeName;
public int acmAttributeIndex;
public int acmFkRelIndex;
public int dbDomainIndex;
public Integer columnCategory;
public String fkTargetAcmEntityName;
public boolean isInstantiated;

public EntityColumnDescriptor(String columnName, boolean isNullable, Integer acmEntityType, String acmEntityName, String acmAttributeName, int acmAttributeIndex, int acmFkRelIndex, int dbDomainIndex, Integer columnCategory, String fkTargetAcmEntityName, boolean isInstantiated) {
this.columnName = columnName;
this.isNullable = isNullable;
this.acmEntityType = acmEntityType;
this.acmEntityName = acmEntityName;
this.acmAttributeName = acmAttributeName;
this.acmAttributeIndex = acmAttributeIndex;
this.acmFkRelIndex = acmFkRelIndex;
this.dbDomainIndex = dbDomainIndex;
this.columnCategory = columnCategory;
this.fkTargetAcmEntityName = fkTargetAcmEntityName;
this.isInstantiated = isInstantiated;
}
}

class EntityColumnDescriptors {
public M24_Attribute_Utilities.EntityColumnDescriptor[] descriptors;
public int numDescriptors;

public EntityColumnDescriptors(int numDescriptors, M24_Attribute_Utilities.EntityColumnDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

public static M24_Attribute_Utilities.AttributeListTransformation nullAttributeTransformation;
public static M24_Attribute_Utilities.EntityColumnDescriptors nullEntityColumnDescriptors;


public static void initAttributeDescriptors(M24_Attribute_Utilities.AttributeDescriptors des) {
des.numDescriptors = 0;
M24_Attribute_Utilities.nullAttributeTransformation.numMappings = 0;
M24_Attribute_Utilities.nullAttributeTransformation.attributePrefix = "";
M24_Attribute_Utilities.nullAttributeTransformation.attributePostfix = "";
M24_Attribute_Utilities.nullAttributeTransformation.doCollectDomainDescriptors = false;
M24_Attribute_Utilities.nullAttributeTransformation.doCollectAttrDescriptors = false;
M24_Attribute_Utilities.nullEntityColumnDescriptors.numDescriptors = 0;
// ### IF IVK ###
M24_Attribute_Utilities.nullAttributeTransformation.doCollectVirtualDomainDescriptors = false;
M24_Attribute_Utilities.nullAttributeTransformation.doCollectVirtualAttrDescriptors = false;
// ### ENDIF IVK ###

M25_Domain_Utilities.initDomainDescriptorRefs(M24_Attribute_Utilities.nullAttributeTransformation.domainRefs);
}


public static void initAttributeTransformation(M24_Attribute_Utilities.AttributeListTransformation transformation, int numMappings, Boolean doCollectDomainDescriptorsW, Boolean doCollectAttrDescriptorsW, Boolean doCollectOidColDescriptorsW, String prefixW, String attr1W, String val1W, String attr2W, String val2W, String attr3W, String val3W, String postfixW, String delimiterW, Integer oidColFilterW, Boolean doCollectVirtualDomainDescriptorsW, Boolean doCollectVirtualAttrDescriptorsW) {
boolean doCollectDomainDescriptors; 
if (doCollectDomainDescriptorsW == null) {
doCollectDomainDescriptors = false;
} else {
doCollectDomainDescriptors = doCollectDomainDescriptorsW;
}

boolean doCollectAttrDescriptors; 
if (doCollectAttrDescriptorsW == null) {
doCollectAttrDescriptors = false;
} else {
doCollectAttrDescriptors = doCollectAttrDescriptorsW;
}

boolean doCollectOidColDescriptors; 
if (doCollectOidColDescriptorsW == null) {
doCollectOidColDescriptors = false;
} else {
doCollectOidColDescriptors = doCollectOidColDescriptorsW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String attr1; 
if (attr1W == null) {
attr1 = "";
} else {
attr1 = attr1W;
}

String val1; 
if (val1W == null) {
val1 = "";
} else {
val1 = val1W;
}

String attr2; 
if (attr2W == null) {
attr2 = "";
} else {
attr2 = attr2W;
}

String val2; 
if (val2W == null) {
val2 = "";
} else {
val2 = val2W;
}

String attr3; 
if (attr3W == null) {
attr3 = "";
} else {
attr3 = attr3W;
}

String val3; 
if (val3W == null) {
val3 = "";
} else {
val3 = val3W;
}

String postfix; 
if (postfixW == null) {
postfix = "";
} else {
postfix = postfixW;
}

String delimiter; 
if (delimiterW == null) {
delimiter = "";
} else {
delimiter = delimiterW;
}

Integer oidColFilter; 
if (oidColFilterW == null) {
oidColFilter = M01_Common.AttrCategory.eacAnyOid;
} else {
oidColFilter = oidColFilterW;
}

boolean doCollectVirtualDomainDescriptors; 
if (doCollectVirtualDomainDescriptorsW == null) {
doCollectVirtualDomainDescriptors = false;
} else {
doCollectVirtualDomainDescriptors = doCollectVirtualDomainDescriptorsW;
}

boolean doCollectVirtualAttrDescriptors; 
if (doCollectVirtualAttrDescriptorsW == null) {
doCollectVirtualAttrDescriptors = false;
} else {
doCollectVirtualAttrDescriptors = doCollectVirtualAttrDescriptorsW;
}

transformation.attributePrefix = prefix;
transformation.attributePostfix = postfix;
transformation.attributeRepeatDelimiter = delimiter;
transformation.postProcessAfterMapping = false;
transformation.numMappings = numMappings;
transformation.distinguishNullabilityForDomainRefs = false;
transformation.doCollectDomainDescriptors = doCollectDomainDescriptors;
transformation.doCollectAttrDescriptors = doCollectAttrDescriptors;
// ### IF IVK ###
transformation.doCollectVirtualDomainDescriptors = doCollectVirtualDomainDescriptors;
transformation.doCollectVirtualAttrDescriptors = doCollectVirtualAttrDescriptors;
// ### ENDIF IVK ###
transformation.doCollectOidColDescriptors = doCollectOidColDescriptors;
transformation.oidColFilter = oidColFilter;
transformation.ignoreConstraint = false;
transformation.trimRight = true;
transformation.containsNlAttribute = false;

if (numMappings > 0) {
transformation.mappings =  new M24_Attribute_Utilities.AttributeTransformation[numMappings];
int i;
for (int i = 1; i <= numMappings; i++) {
transformation.mappings[i].domainSection = "";
transformation.mappings[i].domainName = "";
}

transformation.mappings[1].attributeName = attr1;
transformation.mappings[1].value = val1;
if (numMappings > 1) {
transformation.mappings[2].attributeName = attr2;
transformation.mappings[2].value = val2;
if (numMappings > 2) {
transformation.mappings[3].attributeName = attr3;
transformation.mappings[3].value = val3;
}
}
}
transformation.domainRefs.numRefs = 0;
transformation.nlAttrRefs.numDescriptors = 0;
transformation.oidDescriptors.numDescriptors = 0;
transformation.M01_ACM.conEnumLabelText.orgIndex = -1;
transformation.M01_ACM.conEnumLabelText.poolIndex = -1;
transformation.M01_ACM.conEnumLabelText.tabQualifier = "";
transformation.M01_ACM.conEnumLabelText.forLrt = false;
transformation.M01_ACM.conEnumLabelText.lrtOidRef = "";
}


public static void setAttributeTransformationContext(M24_Attribute_Utilities.AttributeListTransformation transformation,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String tabQualifierW, String lrtOidRefW, boolean forLrt = false) {
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

String tabQualifier; 
if (tabQualifierW == null) {
tabQualifier = "";
} else {
tabQualifier = tabQualifierW;
}

String lrtOidRef; 
if (lrtOidRefW == null) {
lrtOidRef = "";
} else {
lrtOidRef = lrtOidRefW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

transformation.M01_ACM.conEnumLabelText.orgIndex = thisOrgIndex;
transformation.M01_ACM.conEnumLabelText.poolIndex = thisPoolIndex;
transformation.M01_ACM.conEnumLabelText.tabQualifier = tabQualifier;
transformation.M01_ACM.conEnumLabelText.lrtOidRef = lrtOidRef;
transformation.M01_ACM.conEnumLabelText.forLrt = forLrt |  !(lrtOidRef.compareTo("") == 0);
}


public static void setAttributeMapping(M24_Attribute_Utilities.AttributeListTransformation transformation, int mappingIndex, String attrW, String valW, String domainSectionW, String domainNameW, Boolean isConstantW) {
String attr; 
if (attrW == null) {
attr = "";
} else {
attr = attrW;
}

String val; 
if (valW == null) {
val = "";
} else {
val = valW;
}

String domainSection; 
if (domainSectionW == null) {
domainSection = "";
} else {
domainSection = domainSectionW;
}

String domainName; 
if (domainNameW == null) {
domainName = "";
} else {
domainName = domainNameW;
}

boolean isConstant; 
if (isConstantW == null) {
isConstant = false;
} else {
isConstant = isConstantW;
}

transformation.mappings[mappingIndex].attributeName = attr;
transformation.mappings[mappingIndex].domainSection = domainSection;
transformation.mappings[mappingIndex].domainName = domainName;
transformation.mappings[mappingIndex].value = val;
transformation.mappings[mappingIndex].isConstant = isConstant;
}


public static Integer allocAttributeDescriptorIndex(M24_Attribute_Utilities.AttributeDescriptors attributes) {
Integer returnValue;
returnValue = -1;

if (attributes.numDescriptors == 0) {
attributes.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[M01_Common.gc_allocBlockSize];
} else if (attributes.numDescriptors >= M00_Helper.uBound(attributes.descriptors)) {
M24_Attribute_Utilities.EntityColumnDescriptor[] descriptorsBackup = attributes.descriptors;
attributes.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[attributes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.EntityColumnDescriptor value : descriptorsBackup) {
attributes.descriptors[indexCounter] = value;
indexCounter++;
}
}
attributes.numDescriptors = attributes.numDescriptors + 1;
returnValue = attributes.numDescriptors;
attributes.descriptors[attributes.numDescriptors].valueTypeIndex = -1;
attributes.descriptors[attributes.numDescriptors].domainIndex = -1;
attributes.descriptors[attributes.numDescriptors].reusedAttrIndex = -1;
return returnValue;
}

public static Integer allocEntityColumnDescriptorIndex(M24_Attribute_Utilities.EntityColumnDescriptors des) {
Integer returnValue;
returnValue = -1;

if (des.numDescriptors == 0) {
des.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[M01_Common.gc_allocBlockSize];
} else if (des.numDescriptors >= M00_Helper.uBound(des.descriptors)) {
M24_Attribute_Utilities.EntityColumnDescriptor[] descriptorsBackup = des.descriptors;
des.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[des.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.EntityColumnDescriptor value : descriptorsBackup) {
des.descriptors[indexCounter] = value;
indexCounter++;
}
}
des.numDescriptors = des.numDescriptors + 1;
returnValue = des.numDescriptors;
return returnValue;
}
// ### IF IVK ###


public static void addVirtuallyReferingAttr(int attrIndex, int referringAttr) {
if (attrIndex <= 0) {
return;
}

int i;
for (int i = 1; i <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy); i++) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i] == attrIndex) {
return;
}
}

int[] virtuallyReferredToByBackup = M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy;
M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy =  new int[M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy) + 1];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : virtuallyReferredToByBackup) {
M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[indexCounter] = value;
indexCounter++;
}
M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[(M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy))] = referringAttr;
}


// ### ENDIF IVK ###
// ### IF IVK ###
public static Integer findColumnToUse(M24_Attribute_Utilities.EntityColumnDescriptors des, String columnName, String entityName, Integer acmEntityType, String acmAttributeName, Integer valueType, int valueTypeIndex, boolean isReused, Integer columnCategory, Integer fkRelIndexW, Boolean findOnlyW, Integer acmAttributeIndexW, Boolean isNullableW, Boolean isInstantiatedW) {
int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = 0;
} else {
fkRelIndex = fkRelIndexW;
}

boolean findOnly; 
if (findOnlyW == null) {
findOnly = false;
} else {
findOnly = findOnlyW;
}

int acmAttributeIndex; 
if (acmAttributeIndexW == null) {
acmAttributeIndex = -1;
} else {
acmAttributeIndex = acmAttributeIndexW;
}

boolean isNullable; 
if (isNullableW == null) {
isNullable = false;
} else {
isNullable = isNullableW;
}

boolean isInstantiated; 
if (isInstantiatedW == null) {
isInstantiated = true;
} else {
isInstantiated = isInstantiatedW;
}

Integer returnValue;
// ### ELSE IVK ###
//Function findColumnToUse( _
// ByRef des As EntityColumnDescriptors, _
// ByRef columnName As String, _
// ByRef entityName As String, _
// ByRef acmEntityType As AcmAttrContainerType, _
// ByRef acmAttributeName As String, _
// valueType As AttrValueType, _
// valueTypeIndex As Integer, _
// ByRef isReused As Boolean, _
// ByRef columnCategory As AttrCategory, _
// Optional ByRef fkRelIndex As Integer, _
// Optional ByRef findOnly As Boolean = False, _
// Optional ByRef acmAttributeIndex As Integer = -1, _
// Optional isNullable As Boolean = False _
//) As Integer
// ### ENDIF IVK ###
int i;

returnValue = -1;
for (i = 1; i <= 1; i += (1)) {
// FIXME: Use more precise criteria / include domain
if (des.descriptors[i].columnName.toUpperCase() == columnName.toUpperCase()) {
returnValue = i;
isReused = true;
des.descriptors[i].isNullable = des.descriptors[i].isNullable |  isNullable;
// ### IF IVK ###
des.descriptors[i].isInstantiated = des.descriptors[i].isInstantiated |  isInstantiated;
// ### ENDIF IVK ###
des.descriptors[i].columnCategory = des.descriptors[i].columnCategory |  columnCategory;
return returnValue;
}
}

if (!(findOnly)) {
// did not find a column to reuse - record this as a new column
i = M24_Attribute_Utilities.allocEntityColumnDescriptorIndex(des);
des.descriptors[i].acmEntityName = entityName;
des.descriptors[i].acmEntityType = acmEntityType;
des.descriptors[i].acmAttributeName = acmAttributeName;
des.descriptors[i].acmAttributeIndex = acmAttributeIndex;
des.descriptors[i].acmFkRelIndex = fkRelIndex;
des.descriptors[i].columnName = columnName;
if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomain) {
des.descriptors[i].dbDomainIndex = valueTypeIndex;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
des.descriptors[i].dbDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
des.descriptors[i].acmAttributeName = des.descriptors[i].acmAttributeName + M01_Globals.gc_enumAttrNameSuffix;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId) {
des.descriptors[i].dbDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue) {
des.descriptors[i].dbDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexValue;
}
des.descriptors[i].columnCategory = columnCategory;
des.descriptors[i].isNullable = isNullable;
// ### IF IVK ###
des.descriptors[i].isInstantiated = isInstantiated;
// ### ENDIF IVK ###
returnValue = i;
}

isReused = false;
return returnValue;
}


public static Integer getAttrContainerType(String str) {
Integer returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
switch (str) {
case M01_Globals.gc_acmEntityTypeKeyEnum: {returnValue = M24_Attribute_Utilities.AcmAttrContainerType.eactEnum;
}case M01_Globals.gc_acmEntityTypeKeyRel: {returnValue = M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship;
}case M01_Globals.gc_acmEntityTypeKeyClass: {returnValue = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
// ### IF IVK ###
}case M01_Globals.gc_acmEntityTypeKeyType: {returnValue = M24_Attribute_Utilities.AcmAttrContainerType.eactType;
// ### ENDIF IVK ###
}case M01_Globals.gc_acmEntityTypeKeyView: {returnValue = M24_Attribute_Utilities.AcmAttrContainerType.eactView;
}}
return returnValue;
}


public static void initAttrDescriptorRefs(M24_Attribute_Utilities.AttrDescriptorRefs attrRefs) {
attrRefs.numDescriptors = 0;
}


public static Integer allocAttrDescriptorRefIndex(M24_Attribute_Utilities.AttrDescriptorRefs attrRefs) {
Integer returnValue;
returnValue = -1;

if (attrRefs.numDescriptors == 0) {
attrRefs.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[M01_Common.gc_allocBlockSize];
} else if (attrRefs.numDescriptors >= M00_Helper.uBound(attrRefs.descriptors)) {
M24_Attribute_Utilities.EntityColumnDescriptor[] descriptorsBackup = attrRefs.descriptors;
attrRefs.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[attrRefs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.EntityColumnDescriptor value : descriptorsBackup) {
attrRefs.descriptors[indexCounter] = value;
indexCounter++;
}
}
attrRefs.numDescriptors = attrRefs.numDescriptors + 1;
returnValue = attrRefs.numDescriptors;
return returnValue;
}


public static void addAttrDescriptorRef(M24_Attribute_Utilities.AttrDescriptorRefs refs, int ref, Boolean withRepeatW) {
boolean withRepeat; 
if (withRepeatW == null) {
withRepeat = false;
} else {
withRepeat = withRepeatW;
}

int i;

// check if this attribute is already listed
for (i = 1; i <= 1; i += (1)) {
if (M03_Config.reuseColumnsInTabsForOrMapping & ! withRepeat) {
if (M24_Attribute.g_attributes.descriptors[refs.descriptors[i].refIndex].attributeName.compareTo(M24_Attribute.g_attributes.descriptors[ref].attributeName) == 0) {
return;
}
} else {
if (M03_Config.reuseColumnsInTabsForOrMapping) {
if (M24_Attribute.g_attributes.descriptors[refs.descriptors[i].refIndex].attributeName.compareTo(M24_Attribute.g_attributes.descriptors[ref].attributeName) == 0) {
if (M24_Attribute.g_attributes.descriptors[ref].reusedAttrIndex <= 0) {
M24_Attribute.g_attributes.descriptors[ref].reusedAttrIndex = refs.descriptors[i].refIndex;
}
}
}
if (refs.descriptors[i].refIndex == ref) {
return;
}
}
}

// attribute is not listed -> add it
if (refs.numDescriptors == 0) {
refs.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[M01_Common.gc_allocBlockSize];
} else if (refs.numDescriptors >= M00_Helper.uBound(refs.descriptors)) {
M24_Attribute_Utilities.EntityColumnDescriptor[] descriptorsBackup = refs.descriptors;
refs.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[refs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.EntityColumnDescriptor value : descriptorsBackup) {
refs.descriptors[indexCounter] = value;
indexCounter++;
}
}
refs.numDescriptors = refs.numDescriptors + 1;
refs.descriptors[refs.numDescriptors].refIndex = ref;
refs.descriptors[refs.numDescriptors].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute;
}





public static void addOidColDescriptor(M24_Attribute_Utilities.OidColDescriptors des, String colName, Integer colCat) {
int i;
// check if this attribute is already listed
for (i = 1; i <= 1; i += (1)) {
if (des.descriptors[i].colName.compareTo(colName) == 0) {
return;
}
}

if (des.numDescriptors == 0) {
des.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[M01_Common.gc_allocBlockSize];
} else if (des.numDescriptors >= M00_Helper.uBound(des.descriptors)) {
M24_Attribute_Utilities.EntityColumnDescriptor[] descriptorsBackup = des.descriptors;
des.descriptors =  new M24_Attribute_Utilities.EntityColumnDescriptor[des.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities.EntityColumnDescriptor value : descriptorsBackup) {
des.descriptors[indexCounter] = value;
indexCounter++;
}
}
des.numDescriptors = des.numDescriptors + 1;
des.descriptors[des.numDescriptors].colName = colName;
des.descriptors[des.numDescriptors].colCat = colCat;
}


}