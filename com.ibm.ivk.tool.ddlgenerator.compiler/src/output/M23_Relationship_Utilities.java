package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M23_Relationship_Utilities {




public class RelRefTargetType {
public static final int erttRegular = 0;
public static final int erttGen = 1;
public static final int erttNL = 2;
public static final int erttGenNl = erttGen |  erttNL;
}

class NavPathFromRelationshipToClass {
public Integer navDirectionToClass;// indicates whether the 'first class' on the path to 'target class' is reached following this relationship in left or right direction

public NavPathFromRelationshipToClass(Integer navDirectionToClass) {
this.navDirectionToClass = navDirectionToClass;
}
}

class RelationshipIndexes {
public int[] indexes;
public int numIndexes;

public RelationshipIndexes(int numIndexes, int[] indexes) {
this.numIndexes = numIndexes;
this.indexes = indexes;
}
}

public class FkMaintenanceMode {
public static final int efkmRestrict = 0;
public static final int efkmCascade = 1;
}

class RelationshipDescriptorRef {
public int refIndex;
public Integer refType;

public RelationshipDescriptorRef(int refIndex, Integer refType) {
this.refIndex = refIndex;
this.refType = refType;
}
}

class RelationshipDescriptorRefs {
public M23_Relationship_Utilities.RelationshipDescriptorRef[] refs;
public int numRefs;

public RelationshipDescriptorRefs(int numRefs, M23_Relationship_Utilities.RelationshipDescriptorRef[] refs) {
this.numRefs = numRefs;
this.refs = refs;
}
}

class RelationshipDescriptor {
public String sectionName;
public String relName;
public String i18nId;
public String aggHeadSection;
public String aggHeadName;
public String shortName;
public boolean ignoreForChangelog;
public String reuseName;
public String reuseShortName;
// ### IF IVK ###
public String lrtClassification;
public String lrtActivationStatusMode;
public M24_Attribute_Utilities.AttributeMappingForCl[] refersToClAttribute;
public M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsTo;
public String navPathStrToDivision;
public boolean noRangePartitioning;
public boolean noXmlExport;
public boolean useXmlExport;
public boolean isNationalizable;
public boolean isPsForming;
public boolean supportExtendedPsCopy;
public boolean noTransferToProduction;
public boolean noFto;
public boolean ftoSingleObjProcessing;
// ### ENDIF IVK ###
public boolean isCommonToOrgs;
public int specificToOrgId;
public int fkReferenceOrgId;
public boolean isCommonToPools;
public int specificToPool;
public int fkReferencePoolId;
public int noIndexesInPool;
public boolean useValueCompression;
public boolean useSurrogateKey;
public boolean useVersiontag;
public int relId;
public boolean notAcmRelated;
public boolean noAlias;
public boolean isLrtSpecific;
public boolean isPdmSpecific;
public int includeInPdmExportSeqNo;
public boolean isVolatile;
public boolean isNotEnforced;
public boolean isNl;
public boolean includeInPkIndex;
public String leftClassSectionName;
public String leftClassName;
public Integer leftTargetType;
public String lrRelName;
public String lrShortRelName;
public String lrLdmRelName;
public Integer lrFkMaintenanceMode;
public int minLeftCardinality;
public int maxLeftCardinality;
public boolean isIdentifyingLeft;
public boolean useIndexOnLeftFk;
// ### IF IVK ###
public String leftDependentAttribute;
// ### ENDIF IVK ###
public String rightClassSectionName;
public String rightClassName;
public Integer rightTargetType;
public String rlRelName;
public String rlShortRelName;
public String rlLdmRelName;
public Integer rlFkMaintenanceMode;
public int minRightCardinality;
public int maxRightCardinality;
public boolean isIdentifyingRight;
public boolean useIndexOnRightFk;
// ### IF IVK ###
public boolean isRightRefToTimeVarying;
public String rightDependentAttribute;
// ### ENDIF IVK ###
public boolean logLastChange;
public boolean logLastChangeAutoMaint;
public boolean logLastChangeInView;
public boolean isUserTransactional;
public boolean isLrtMeta;
public boolean useMqtToImplementLrt;
public String tabSpaceData;
public String tabSpaceLong;
public String tabSpaceNl;
public String tabSpaceIndex;
public int defaultStatus;
public boolean isTimeVarying;

public boolean isMdsExpressionRel;

// derived attributes
public int fkReferenceOrgIndex;
public int fkReferencePoolIndex;
public String effectiveShortName;
public boolean hasBusinessKey;
public boolean implementsInOwnTable;
public Integer implementsInEntity;
public String relIdStr;
public int relNlIndex;
public boolean isVirtual;
public int aggHeadClassIndex;
public int aggHeadClassIndexExact;
public String aggHeadClassIdStr;
public boolean hasPriceAssignmentAggHead;
public boolean isSubjectToPreisDurchschuss;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
public M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs;
public M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
public M76_Index_Utilities.IndexDescriptorRefs indexRefs;
public int numAttrs;
public int leftEntityIndex;
public Integer leftEntityType;
public String leftEntityShortName;
public String[] leftFkColName = new String[2];
public int rightEntityIndex;
public Integer rightEntityType;
public String rightEntityShortName;
public String[] rightFkColName = new String[2];
public boolean useLrLdmRelName;
public boolean useRlLdmRelName;
public boolean isSubjectToArchiving;
public boolean leftIsSubjectToArchiving;
public boolean rightIsSubjectToArchiving;
public boolean rightIsDivision;
public boolean leftIsDivision;
public boolean isPsTagged;
public int relIndex;
public int sectionIndex;
public String sectionShortName;
public int tabSpaceIndexData;
public int tabSpaceIndexIndex;
public int tabSpaceIndexLong;
public int tabSpaceIndexNl;

public boolean hasLabel;
// ### IF IVK ###
public boolean hasIsNationalInclSubClasses;
public Integer isAllowedCountries;
public Integer isDisallowedCountries;
public Integer isAllowedCountriesList;
public Integer isDisallowedCountriesList;
public boolean isValidForOrganization;
public boolean hasOrganizationSpecificReference;
public boolean leftClassIsOrganizationSpecific;
public boolean rightClassIsOrganizationSpecific;
public boolean supportXmlExport;
public boolean isSubjectToExpCopy;
// ### ENDIF IVK ###

public boolean isReusedInSameEntity;
public int reusedRelIndex;
public M23_Relationship_Utilities.RelationshipIndexes reusingRelIndexes;

// temporary variables supporting processing
public boolean isLdmCsvExported;
public boolean isLdmLrtCsvExported;
public boolean isCtoAliasCreated;
// ### IF IVK ###
public boolean isXsdExported;
public M23_Relationship_Utilities.NavPathFromRelationshipToClass navPathToDiv;
// ### ENDIF IVK ###

public RelationshipDescriptor(String sectionName, String relName, String i18nId, String aggHeadSection, String aggHeadName, String shortName, boolean ignoreForChangelog, String reuseName, String reuseShortName, String lrtClassification, String lrtActivationStatusMode, M24_Attribute_Utilities.AttributeMappingForACM virtuallyMapsTo, String navPathStrToDivision, boolean noRangePartitioning, boolean noXmlExport, boolean useXmlExport, boolean isNationalizable, boolean isPsForming, boolean supportExtendedPsCopy, boolean noTransferToProduction, boolean noFto, boolean ftoSingleObjProcessing, boolean isCommonToOrgs, int specificToOrgId, int fkReferenceOrgId, boolean isCommonToPools, int specificToPool, int fkReferencePoolId, int noIndexesInPool, boolean useValueCompression, boolean useSurrogateKey, boolean useVersiontag, int relId, boolean notAcmRelated, boolean noAlias, boolean isLrtSpecific, boolean isPdmSpecific, int includeInPdmExportSeqNo, boolean isVolatile, boolean isNotEnforced, boolean isNl, boolean includeInPkIndex, String leftClassSectionName, String leftClassName, Integer leftTargetType, String lrRelName, String lrShortRelName, String lrLdmRelName, Integer lrFkMaintenanceMode, int minLeftCardinality, int maxLeftCardinality, boolean isIdentifyingLeft, boolean useIndexOnLeftFk, String leftDependentAttribute, String rightClassSectionName, String rightClassName, Integer rightTargetType, String rlRelName, String rlShortRelName, String rlLdmRelName, Integer rlFkMaintenanceMode, int minRightCardinality, int maxRightCardinality, boolean isIdentifyingRight, boolean useIndexOnRightFk, boolean isRightRefToTimeVarying, String rightDependentAttribute, boolean logLastChange, boolean logLastChangeAutoMaint, boolean logLastChangeInView, boolean isUserTransactional, boolean isLrtMeta, boolean useMqtToImplementLrt, String tabSpaceData, String tabSpaceLong, String tabSpaceNl, String tabSpaceIndex, int defaultStatus, boolean isTimeVarying, boolean isMdsExpressionRel, int fkReferenceOrgIndex, int fkReferencePoolIndex, String effectiveShortName, boolean hasBusinessKey, boolean implementsInOwnTable, Integer implementsInEntity, String relIdStr, int relNlIndex, boolean isVirtual, int aggHeadClassIndex, int aggHeadClassIndexExact, String aggHeadClassIdStr, boolean hasPriceAssignmentAggHead, boolean isSubjectToPreisDurchschuss, M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, M76_Index_Utilities.IndexDescriptorRefs indexRefs, int numAttrs, int leftEntityIndex, Integer leftEntityType, String leftEntityShortName, int rightEntityIndex, Integer rightEntityType, String rightEntityShortName, boolean useLrLdmRelName, boolean useRlLdmRelName, boolean isSubjectToArchiving, boolean leftIsSubjectToArchiving, boolean rightIsSubjectToArchiving, boolean rightIsDivision, boolean leftIsDivision, boolean isPsTagged, int relIndex, int sectionIndex, String sectionShortName, int tabSpaceIndexData, int tabSpaceIndexIndex, int tabSpaceIndexLong, int tabSpaceIndexNl, boolean hasLabel, boolean hasIsNationalInclSubClasses, Integer isAllowedCountries, Integer isDisallowedCountries, Integer isAllowedCountriesList, Integer isDisallowedCountriesList, boolean isValidForOrganization, boolean hasOrganizationSpecificReference, boolean leftClassIsOrganizationSpecific, boolean rightClassIsOrganizationSpecific, boolean supportXmlExport, boolean isSubjectToExpCopy, boolean isReusedInSameEntity, int reusedRelIndex, M23_Relationship_Utilities.RelationshipIndexes reusingRelIndexes, boolean isLdmCsvExported, boolean isLdmLrtCsvExported, boolean isCtoAliasCreated, boolean isXsdExported, M23_Relationship_Utilities.NavPathFromRelationshipToClass navPathToDiv, M24_Attribute_Utilities.AttributeMappingForCl[] refersToClAttribute, String[] leftFkColName, String[] rightFkColName) {
this.sectionName = sectionName;
this.relName = relName;
this.i18nId = i18nId;
this.aggHeadSection = aggHeadSection;
this.aggHeadName = aggHeadName;
this.shortName = shortName;
this.ignoreForChangelog = ignoreForChangelog;
this.reuseName = reuseName;
this.reuseShortName = reuseShortName;
this.lrtClassification = lrtClassification;
this.lrtActivationStatusMode = lrtActivationStatusMode;
this.virtuallyMapsTo = virtuallyMapsTo;
this.navPathStrToDivision = navPathStrToDivision;
this.noRangePartitioning = noRangePartitioning;
this.noXmlExport = noXmlExport;
this.useXmlExport = useXmlExport;
this.isNationalizable = isNationalizable;
this.isPsForming = isPsForming;
this.supportExtendedPsCopy = supportExtendedPsCopy;
this.noTransferToProduction = noTransferToProduction;
this.noFto = noFto;
this.ftoSingleObjProcessing = ftoSingleObjProcessing;
this.isCommonToOrgs = isCommonToOrgs;
this.specificToOrgId = specificToOrgId;
this.fkReferenceOrgId = fkReferenceOrgId;
this.isCommonToPools = isCommonToPools;
this.specificToPool = specificToPool;
this.fkReferencePoolId = fkReferencePoolId;
this.noIndexesInPool = noIndexesInPool;
this.useValueCompression = useValueCompression;
this.useSurrogateKey = useSurrogateKey;
this.useVersiontag = useVersiontag;
this.relId = relId;
this.notAcmRelated = notAcmRelated;
this.noAlias = noAlias;
this.isLrtSpecific = isLrtSpecific;
this.isPdmSpecific = isPdmSpecific;
this.includeInPdmExportSeqNo = includeInPdmExportSeqNo;
this.isVolatile = isVolatile;
this.isNotEnforced = isNotEnforced;
this.isNl = isNl;
this.includeInPkIndex = includeInPkIndex;
this.leftClassSectionName = leftClassSectionName;
this.leftClassName = leftClassName;
this.leftTargetType = leftTargetType;
this.lrRelName = lrRelName;
this.lrShortRelName = lrShortRelName;
this.lrLdmRelName = lrLdmRelName;
this.lrFkMaintenanceMode = lrFkMaintenanceMode;
this.minLeftCardinality = minLeftCardinality;
this.maxLeftCardinality = maxLeftCardinality;
this.isIdentifyingLeft = isIdentifyingLeft;
this.useIndexOnLeftFk = useIndexOnLeftFk;
this.leftDependentAttribute = leftDependentAttribute;
this.rightClassSectionName = rightClassSectionName;
this.rightClassName = rightClassName;
this.rightTargetType = rightTargetType;
this.rlRelName = rlRelName;
this.rlShortRelName = rlShortRelName;
this.rlLdmRelName = rlLdmRelName;
this.rlFkMaintenanceMode = rlFkMaintenanceMode;
this.minRightCardinality = minRightCardinality;
this.maxRightCardinality = maxRightCardinality;
this.isIdentifyingRight = isIdentifyingRight;
this.useIndexOnRightFk = useIndexOnRightFk;
this.isRightRefToTimeVarying = isRightRefToTimeVarying;
this.rightDependentAttribute = rightDependentAttribute;
this.logLastChange = logLastChange;
this.logLastChangeAutoMaint = logLastChangeAutoMaint;
this.logLastChangeInView = logLastChangeInView;
this.isUserTransactional = isUserTransactional;
this.isLrtMeta = isLrtMeta;
this.useMqtToImplementLrt = useMqtToImplementLrt;
this.tabSpaceData = tabSpaceData;
this.tabSpaceLong = tabSpaceLong;
this.tabSpaceNl = tabSpaceNl;
this.tabSpaceIndex = tabSpaceIndex;
this.defaultStatus = defaultStatus;
this.isTimeVarying = isTimeVarying;
this.isMdsExpressionRel = isMdsExpressionRel;
this.fkReferenceOrgIndex = fkReferenceOrgIndex;
this.fkReferencePoolIndex = fkReferencePoolIndex;
this.effectiveShortName = effectiveShortName;
this.hasBusinessKey = hasBusinessKey;
this.implementsInOwnTable = implementsInOwnTable;
this.implementsInEntity = implementsInEntity;
this.relIdStr = relIdStr;
this.relNlIndex = relNlIndex;
this.isVirtual = isVirtual;
this.aggHeadClassIndex = aggHeadClassIndex;
this.aggHeadClassIndexExact = aggHeadClassIndexExact;
this.aggHeadClassIdStr = aggHeadClassIdStr;
this.hasPriceAssignmentAggHead = hasPriceAssignmentAggHead;
this.isSubjectToPreisDurchschuss = isSubjectToPreisDurchschuss;
this.attrRefs = attrRefs;
this.nlAttrRefs = nlAttrRefs;
this.relRefs = relRefs;
this.indexRefs = indexRefs;
this.numAttrs = numAttrs;
this.leftEntityIndex = leftEntityIndex;
this.leftEntityType = leftEntityType;
this.leftEntityShortName = leftEntityShortName;
this.rightEntityIndex = rightEntityIndex;
this.rightEntityType = rightEntityType;
this.rightEntityShortName = rightEntityShortName;
this.useLrLdmRelName = useLrLdmRelName;
this.useRlLdmRelName = useRlLdmRelName;
this.isSubjectToArchiving = isSubjectToArchiving;
this.leftIsSubjectToArchiving = leftIsSubjectToArchiving;
this.rightIsSubjectToArchiving = rightIsSubjectToArchiving;
this.rightIsDivision = rightIsDivision;
this.leftIsDivision = leftIsDivision;
this.isPsTagged = isPsTagged;
this.relIndex = relIndex;
this.sectionIndex = sectionIndex;
this.sectionShortName = sectionShortName;
this.tabSpaceIndexData = tabSpaceIndexData;
this.tabSpaceIndexIndex = tabSpaceIndexIndex;
this.tabSpaceIndexLong = tabSpaceIndexLong;
this.tabSpaceIndexNl = tabSpaceIndexNl;
this.hasLabel = hasLabel;
this.hasIsNationalInclSubClasses = hasIsNationalInclSubClasses;
this.isAllowedCountries = isAllowedCountries;
this.isDisallowedCountries = isDisallowedCountries;
this.isAllowedCountriesList = isAllowedCountriesList;
this.isDisallowedCountriesList = isDisallowedCountriesList;
this.isValidForOrganization = isValidForOrganization;
this.hasOrganizationSpecificReference = hasOrganizationSpecificReference;
this.leftClassIsOrganizationSpecific = leftClassIsOrganizationSpecific;
this.rightClassIsOrganizationSpecific = rightClassIsOrganizationSpecific;
this.supportXmlExport = supportXmlExport;
this.isSubjectToExpCopy = isSubjectToExpCopy;
this.isReusedInSameEntity = isReusedInSameEntity;
this.reusedRelIndex = reusedRelIndex;
this.reusingRelIndexes = reusingRelIndexes;
this.isLdmCsvExported = isLdmCsvExported;
this.isLdmLrtCsvExported = isLdmLrtCsvExported;
this.isCtoAliasCreated = isCtoAliasCreated;
this.isXsdExported = isXsdExported;
this.navPathToDiv = navPathToDiv;
this.refersToClAttribute = refersToClAttribute;
this.leftFkColName = leftFkColName;
this.rightFkColName = rightFkColName;
}
}

class RelationshipDescriptors {
public M23_Relationship_Utilities.RelationshipDescriptor[] descriptors;
public int numDescriptors;

public RelationshipDescriptors(int numDescriptors, M23_Relationship_Utilities.RelationshipDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static Integer allocRelationshipDescriptorIndex(M23_Relationship_Utilities.RelationshipDescriptors relationships) {
Integer returnValue;
returnValue = -1;

if (relationships.numDescriptors == 0) {
relationships.descriptors =  new M23_Relationship_Utilities.RelationshipDescriptor[M01_Common.gc_allocBlockSize];
} else if (relationships.numDescriptors >= M00_Helper.uBound(relationships.descriptors)) {
M23_Relationship_Utilities.RelationshipDescriptor[] descriptorsBackup = relationships.descriptors;
relationships.descriptors =  new M23_Relationship_Utilities.RelationshipDescriptor[relationships.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M23_Relationship_Utilities.RelationshipDescriptor value : descriptorsBackup) {
relationships.descriptors[indexCounter] = value;
indexCounter++;
}
}
relationships.numDescriptors = relationships.numDescriptors + 1;
M24_Attribute_Utilities.initAttrDescriptorRefs(relationships.descriptors[relationships.numDescriptors].attrRefs);
M24_Attribute_Utilities.initAttrDescriptorRefs(relationships.descriptors[relationships.numDescriptors].nlAttrRefs);
returnValue = relationships.numDescriptors;
return returnValue;
}

public static Integer allocRelationshipIndex(M23_Relationship_Utilities.RelationshipIndexes relIndexes) {
Integer returnValue;
returnValue = -1;

if (relIndexes.numIndexes == 0) {
relIndexes.indexes =  new int[M01_Common.gc_allocBlockSize];
} else if (relIndexes.numIndexes >= M00_Helper.uBound(relIndexes.indexes)) {
int[] indexesBackup = relIndexes.indexes;
relIndexes.indexes =  new int[relIndexes.numIndexes + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : indexesBackup) {
relIndexes.indexes[indexCounter] = value;
indexCounter++;
}
}
relIndexes.numIndexes = relIndexes.numIndexes + 1;
returnValue = relIndexes.numIndexes;
return returnValue;
}


public static void addRelIndex(M23_Relationship_Utilities.RelationshipIndexes relIndexes, int relIndex) {
relIndexes.indexes[(M23_Relationship_Utilities.allocRelationshipIndex(relIndexes))] = relIndex;
}


public static void initRelDescriptorRefs(M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs) {
relRefs.numRefs = 0;
}


public static Integer allocRelDescriptorRefIndex(M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs) {
Integer returnValue;
returnValue = -1;

if (relRefs.numRefs == 0) {
relRefs.refs =  new M23_Relationship_Utilities.RelationshipDescriptorRef[M01_Common.gc_allocBlockSize];
} else if (relRefs.numRefs >= M00_Helper.uBound(relRefs.refs)) {
M23_Relationship_Utilities.RelationshipDescriptorRef[] refsBackup = relRefs.refs;
relRefs.refs =  new M23_Relationship_Utilities.RelationshipDescriptorRef[relRefs.numRefs + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M23_Relationship_Utilities.RelationshipDescriptorRef value : refsBackup) {
relRefs.refs[indexCounter] = value;
indexCounter++;
}
}
relRefs.numRefs = relRefs.numRefs + 1;
returnValue = relRefs.numRefs;
return returnValue;
}


public static String getRelIdByIndex(int thisRelIndex) {
String returnValue;
if (thisRelIndex < 1) {
returnValue = "";
} else {
returnValue = new String ("00" + M20_Section.getSectionSeqNoByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex)).substring(new String ("00" + M20_Section.getSectionSeqNoByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex)).length() - 1 - 2) + new String ("000" + M23_Relationship.g_relationships.descriptors[thisRelIndex].relId).substring(new String ("000" + M23_Relationship.g_relationships.descriptors[thisRelIndex].relId).length() - 1 - 3);
}
return returnValue;
}


public static Integer getRelRefTargetType( String str) {
Integer returnValue;
str = str + "".trim().toUpperCase();
if (str.compareTo("GEN") == 0) {
returnValue = M23_Relationship_Utilities.RelRefTargetType.erttGen;
} else if (str.compareTo("NL") == 0) {
returnValue = M23_Relationship_Utilities.RelRefTargetType.erttNL;
} else if (str.compareTo("GEN-NL") == 0) {
returnValue = M23_Relationship_Utilities.RelRefTargetType.erttGenNl;
} else {
returnValue = M23_Relationship_Utilities.RelRefTargetType.erttRegular;
}
return returnValue;
}


public static void genNavPathForRelationship(int relIndex, M23_Relationship_Utilities.NavPathFromRelationshipToClass navPath, String str) {
String[] list;
list = "".split(".");
list = str.split(".");

if (M00_Helper.uBound(list) == 1) {
String classSectionName;
String className;

classSectionName = list[M00_Helper.lBound(list)];
className = list[M00_Helper.lBound(list) + 1];
//determine the class which leads us to Division
int classIndex;
int leftClassIndex;
int rightClassIndex;
classIndex = M22_Class.getClassIndexByName(classSectionName, className, null);
if (classIndex < 0) {
M04_Utilities.logMsg("unable to determine class '" + str + "' supposed to lead to 'Division'", M01_Common.LogLevel.ellError, null, null, null);
return;
}

leftClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex;
rightClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex;

if (classIndex == leftClassIndex) {
navPath.navDirectionToClass = M01_Common.RelNavigationDirection.etLeft;
} else if (classIndex == rightClassIndex) {
navPath.navDirectionToClass = M01_Common.RelNavigationDirection.etRight;
} else {
M04_Utilities.logMsg("incosistent specification of path '" + str + "' supposed to lead to 'Division'", M01_Common.LogLevel.ellError, null, null, null);
return;
}
}
}


}