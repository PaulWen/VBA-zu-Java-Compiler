package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M22_Class_Utilities {




public class ClassMapping {
public static final int cmSuper = 1;
public static final int cmSub = 2;
public static final int cmOwn = 3;
}

class NavPathFromClassToClass {
public int relRefIndex;// references the relationship which leads to the 'target class'
public Integer navDirection;// indicates which direction to follow to the 'target class'

public NavPathFromClassToClass(int relRefIndex, Integer navDirection) {
this.relRefIndex = relRefIndex;
this.navDirection = navDirection;
}
}

class StrListMap {
public String name;
public String list;

public StrListMap(String name, String list) {
this.name = name;
this.list = list;
}
}

class StrListMaps {
public int numMaps;
public M22_Class_Utilities.StrListMap[] maps;

public StrListMaps(int numMaps, M22_Class_Utilities.StrListMap[] maps) {
this.numMaps = numMaps;
this.maps = maps;
}
}

class ClassDescriptor {
public String sectionName;
public String className;
public String i18nId;
public String aggHeadSection;
public String aggHeadName;
public String classNameLdm;
public String shortName;
// ### IF IVK ###
public String lrtClassification;
public String lrtActivationStatusMode;
// ### ENDIF IVK ###
public boolean ignoreForChangelog;
// ### IF IVK ###
public String mapOidToClAttribute;
public String navPathStrToDivision;
public String navPathStrToOrg;
public String navPathStrToCodeType;
public boolean condenseData;
public boolean isDeletable;
public boolean enforceLrtChangeComment;
public int entityFilterEnumCriteria;
// ### ENDIF IVK ###
public boolean isCommonToOrgs;
public int specificToOrgId;
public boolean isCommonToPools;
public int specificToPool;
public int noIndexesInPool;
public boolean useValueCompression;
public String superClassSection;
public String superClass;
public boolean useSurrogateKey;
public boolean useVersiontag;
// ### IF IVK ###
public Integer mapping;
// ### ENDIF IVK ###
public int classId;
// ### IF IVK ###
public boolean noRangePartitioning;
public boolean rangePartitioningAll;
public String rangePartitionGroup;
public boolean isNationalizable;
// ### ENDIF IVK ###
public boolean isGenForming;
// ### IF IVK ###
public boolean hasNoIdentity;
public boolean isCore;
// ### ENDIF IVK ###
public boolean isAbstract;
// ### IF IVK ###
public boolean supportAhStatusPropagation;
public Integer updateMode;
public boolean isPsTagged;
public boolean psTagNotIdentifying;
public boolean psTagOptional;
public boolean ignPsRegVarOnInsDel;
public boolean isPsForming;
public boolean supportExtendedPsCopy;
// ### ENDIF IVK ###
public boolean logLastChange;
public boolean logLastChangeAutoMaint;
public boolean logLastChangeInView;
// ### IF IVK ###
public boolean expandExpressionsInFtoView;
// ### ENDIF IVK ###
public boolean isUserTransactional;
public boolean isLrtMeta;
public boolean useMqtToImplementLrt;
public boolean notAcmRelated;
public boolean noAlias;
public boolean noFks;
// ### IF IVK ###
public boolean noXmlExport;
public boolean useXmlExport;
// ### ENDIF IVK ###
public boolean isLrtSpecific;
public boolean isPdmSpecific;
// ### IF IVK ###
public int includeInPdmExportSeqNo;
// ### ENDIF IVK ###
public boolean isVolatile;
// ### IF IVK ###
public boolean notPersisted;
public boolean isSubjectToArchiving;
public String nonStandardRefTimeStampForArchiving;
public boolean noTransferToProduction;
public boolean noFto;
public boolean ftoSingleObjProcessing;
// ### ENDIF IVK ###

public String tabSpaceData;
public String tabSpaceLong;
public String tabSpaceNl;
public String tabSpaceIndex;
public int defaultStatus;

// derived attributes
public boolean useLrtCommitPreprocess;
public boolean hasBusinessKey;
public String classIdStr;
public int classNlIndex;
public int aggHeadClassIndex;
public int aggHeadClassIndexExact;
public String aggHeadClassIdStr;
public boolean isAggHead;
public boolean hasSubClass;
public int classIndex;
public int superClassIndex;
public int[] subclassIndexes;
public int[] subclassIndexesRecursive;
public String subclassIdStrListNonAbstract;
//  subClassIdStrSeparatePartition As String
public M22_Class_Utilities.StrListMaps subClassIdStrSeparatePartition;
//Compiler: 2D-Array
//subClassPartitionBoundaries(1 To 2, 1 To 10) As String
public int[] aggChildClassIndexes;
public int[] aggChildRelIndexes;
public int sectionIndex;
public String sectionShortName;
public int orMappingSuperClassIndex;
public boolean hasOwnTable;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClassesWithRepeat;
public M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrs;
public M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrsInclSubclasses;
public M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs;
public M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefsInclSubclasses;
public int numAttrsInNonGen;
public int numAttrsInGen;
public int numNlAttrsInNonGen;
public int numNlAttrsInGen;
public int numRelBasedFkAttrs;
public boolean hasNlAttrsInNonGenInclSubClasses;
public boolean hasNlAttrsInGenInclSubClasses;
public boolean hasLabel;
public boolean hasLabelInGen;
public M76_Index_Utilities.IndexDescriptorRefs indexRefs;
public M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
public M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsRecursive;
public boolean implicitelyGenChangeComment;

public int tabSpaceIndexData;
public int tabSpaceIndexIndex;
public int tabSpaceIndexLong;
public int tabSpaceIndexNl;
// ### IF IVK ###

public boolean containsIsNotPublished;
public boolean containsIsNotPublishedInclSubClasses;
public boolean isPriceAssignment;
public boolean hasPriceAssignmentSubClass;
public boolean hasPriceAssignmentAggHead;
public boolean isSubjectToPreisDurchschuss;
public String subclassIdStrListNonAbstractPriceAssignment;
public boolean isSubjectToExpCopy;
public boolean supportXmlExport;
public boolean hasExpressionInNonGen;
public boolean hasExpressionInGen;
public int allowedCountriesRelIndex;
public int disAllowedCountriesRelIndex;
public int allowedCountriesListRelIndex;
public int disAllowedCountriesListRelIndex;
public boolean isValidForOrganization;
public boolean hasOrganizationSpecificReference;
public M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsToOrganizationSpecificClasses;
public boolean hasGroupIdAttrInNonGen;
public boolean hasGroupIdAttrInNonGenInclSubClasses;
public boolean hasExpBasedVirtualAttrInNonGen;
public boolean hasExpBasedVirtualAttrInGen;
public boolean hasExpBasedVirtualAttrInNonGenInclSubClasses;
public boolean hasExpBasedVirtualAttrInGenInclSubClasses;
public boolean hasRelBasedVirtualAttrInNonGen;
public boolean hasRelBasedVirtualAttrInGen;
public boolean hasRelBasedVirtualAttrInNonGenInclSubClasses;
public boolean hasRelBasedVirtualAttrInGenInclSubClasses;
public boolean hasAttrHasConflict;
public boolean hasIsNationalInclSubClasses;
// ### ENDIF IVK ###

// temporary variables supporting processing
public boolean isLdmCsvExported;
public boolean isLdmLrtCsvExported;
public boolean isCtoAliasCreated;
// ### IF IVK ###
public boolean isXsdExported;
public M22_Class_Utilities.NavPathFromClassToClass navPathToDiv;
public M22_Class_Utilities.NavPathFromClassToClass navPathToOrg;
public M22_Class_Utilities.NavPathFromClassToClass navPathToCodeType;

public int[] groupIdAttrIndexes;
public int[] groupIdAttrIndexesInclSubclasses;
// ### ENDIF IVK ###

public ClassDescriptor(String sectionName, String className, String i18nId, String aggHeadSection, String aggHeadName, String classNameLdm, String shortName, String lrtClassification, String lrtActivationStatusMode, boolean ignoreForChangelog, String mapOidToClAttribute, String navPathStrToDivision, String navPathStrToOrg, String navPathStrToCodeType, boolean condenseData, boolean isDeletable, boolean enforceLrtChangeComment, int entityFilterEnumCriteria, boolean isCommonToOrgs, int specificToOrgId, boolean isCommonToPools, int specificToPool, int noIndexesInPool, boolean useValueCompression, String superClassSection, String superClass, boolean useSurrogateKey, boolean useVersiontag, Integer mapping, int classId, boolean noRangePartitioning, boolean rangePartitioningAll, String rangePartitionGroup, boolean isNationalizable, boolean isGenForming, boolean hasNoIdentity, boolean isCore, boolean isAbstract, boolean supportAhStatusPropagation, Integer updateMode, boolean isPsTagged, boolean psTagNotIdentifying, boolean psTagOptional, boolean ignPsRegVarOnInsDel, boolean isPsForming, boolean supportExtendedPsCopy, boolean logLastChange, boolean logLastChangeAutoMaint, boolean logLastChangeInView, boolean expandExpressionsInFtoView, boolean isUserTransactional, boolean isLrtMeta, boolean useMqtToImplementLrt, boolean notAcmRelated, boolean noAlias, boolean noFks, boolean noXmlExport, boolean useXmlExport, boolean isLrtSpecific, boolean isPdmSpecific, int includeInPdmExportSeqNo, boolean isVolatile, boolean notPersisted, boolean isSubjectToArchiving, String nonStandardRefTimeStampForArchiving, boolean noTransferToProduction, boolean noFto, boolean ftoSingleObjProcessing, String tabSpaceData, String tabSpaceLong, String tabSpaceNl, String tabSpaceIndex, int defaultStatus, boolean useLrtCommitPreprocess, boolean hasBusinessKey, String classIdStr, int classNlIndex, int aggHeadClassIndex, int aggHeadClassIndexExact, String aggHeadClassIdStr, boolean isAggHead, boolean hasSubClass, int classIndex, int superClassIndex, String subclassIdStrListNonAbstract, M22_Class_Utilities.StrListMaps subClassIdStrSeparatePartition, int sectionIndex, String sectionShortName, int orMappingSuperClassIndex, boolean hasOwnTable, M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClasses, M24_Attribute_Utilities.AttrDescriptorRefs attrRefsInclSubClassesWithRepeat, M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs, M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefsInclSubclasses, int numAttrsInNonGen, int numAttrsInGen, int numNlAttrsInNonGen, int numNlAttrsInGen, int numRelBasedFkAttrs, boolean hasNlAttrsInNonGenInclSubClasses, boolean hasNlAttrsInGenInclSubClasses, boolean hasLabel, boolean hasLabelInGen, M76_Index_Utilities.IndexDescriptorRefs indexRefs, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsRecursive, boolean implicitelyGenChangeComment, int tabSpaceIndexData, int tabSpaceIndexIndex, int tabSpaceIndexLong, int tabSpaceIndexNl, boolean containsIsNotPublished, boolean containsIsNotPublishedInclSubClasses, boolean isPriceAssignment, boolean hasPriceAssignmentSubClass, boolean hasPriceAssignmentAggHead, boolean isSubjectToPreisDurchschuss, String subclassIdStrListNonAbstractPriceAssignment, boolean isSubjectToExpCopy, boolean supportXmlExport, boolean hasExpressionInNonGen, boolean hasExpressionInGen, int allowedCountriesRelIndex, int disAllowedCountriesRelIndex, int allowedCountriesListRelIndex, int disAllowedCountriesListRelIndex, boolean isValidForOrganization, boolean hasOrganizationSpecificReference, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsToOrganizationSpecificClasses, boolean hasGroupIdAttrInNonGen, boolean hasGroupIdAttrInNonGenInclSubClasses, boolean hasExpBasedVirtualAttrInNonGen, boolean hasExpBasedVirtualAttrInGen, boolean hasExpBasedVirtualAttrInNonGenInclSubClasses, boolean hasExpBasedVirtualAttrInGenInclSubClasses, boolean hasRelBasedVirtualAttrInNonGen, boolean hasRelBasedVirtualAttrInGen, boolean hasRelBasedVirtualAttrInNonGenInclSubClasses, boolean hasRelBasedVirtualAttrInGenInclSubClasses, boolean hasAttrHasConflict, boolean hasIsNationalInclSubClasses, boolean isLdmCsvExported, boolean isLdmLrtCsvExported, boolean isCtoAliasCreated, boolean isXsdExported, M22_Class_Utilities.NavPathFromClassToClass navPathToDiv, M22_Class_Utilities.NavPathFromClassToClass navPathToOrg, M22_Class_Utilities.NavPathFromClassToClass navPathToCodeType, int[] subclassIndexes, int[] subclassIndexesRecursive, int[] aggChildClassIndexes, int[] aggChildRelIndexes, M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrs, M24_Attribute_Utilities.AttributeMappingForCl[] clMapAttrsInclSubclasses, int[] groupIdAttrIndexes, int[] groupIdAttrIndexesInclSubclasses) {
this.sectionName = sectionName;
this.className = className;
this.i18nId = i18nId;
this.aggHeadSection = aggHeadSection;
this.aggHeadName = aggHeadName;
this.classNameLdm = classNameLdm;
this.shortName = shortName;
this.lrtClassification = lrtClassification;
this.lrtActivationStatusMode = lrtActivationStatusMode;
this.ignoreForChangelog = ignoreForChangelog;
this.mapOidToClAttribute = mapOidToClAttribute;
this.navPathStrToDivision = navPathStrToDivision;
this.navPathStrToOrg = navPathStrToOrg;
this.navPathStrToCodeType = navPathStrToCodeType;
this.condenseData = condenseData;
this.isDeletable = isDeletable;
this.enforceLrtChangeComment = enforceLrtChangeComment;
this.entityFilterEnumCriteria = entityFilterEnumCriteria;
this.isCommonToOrgs = isCommonToOrgs;
this.specificToOrgId = specificToOrgId;
this.isCommonToPools = isCommonToPools;
this.specificToPool = specificToPool;
this.noIndexesInPool = noIndexesInPool;
this.useValueCompression = useValueCompression;
this.superClassSection = superClassSection;
this.superClass = superClass;
this.useSurrogateKey = useSurrogateKey;
this.useVersiontag = useVersiontag;
this.mapping = mapping;
this.classId = classId;
this.noRangePartitioning = noRangePartitioning;
this.rangePartitioningAll = rangePartitioningAll;
this.rangePartitionGroup = rangePartitionGroup;
this.isNationalizable = isNationalizable;
this.isGenForming = isGenForming;
this.hasNoIdentity = hasNoIdentity;
this.isCore = isCore;
this.isAbstract = isAbstract;
this.supportAhStatusPropagation = supportAhStatusPropagation;
this.updateMode = updateMode;
this.isPsTagged = isPsTagged;
this.psTagNotIdentifying = psTagNotIdentifying;
this.psTagOptional = psTagOptional;
this.ignPsRegVarOnInsDel = ignPsRegVarOnInsDel;
this.isPsForming = isPsForming;
this.supportExtendedPsCopy = supportExtendedPsCopy;
this.logLastChange = logLastChange;
this.logLastChangeAutoMaint = logLastChangeAutoMaint;
this.logLastChangeInView = logLastChangeInView;
this.expandExpressionsInFtoView = expandExpressionsInFtoView;
this.isUserTransactional = isUserTransactional;
this.isLrtMeta = isLrtMeta;
this.useMqtToImplementLrt = useMqtToImplementLrt;
this.notAcmRelated = notAcmRelated;
this.noAlias = noAlias;
this.noFks = noFks;
this.noXmlExport = noXmlExport;
this.useXmlExport = useXmlExport;
this.isLrtSpecific = isLrtSpecific;
this.isPdmSpecific = isPdmSpecific;
this.includeInPdmExportSeqNo = includeInPdmExportSeqNo;
this.isVolatile = isVolatile;
this.notPersisted = notPersisted;
this.isSubjectToArchiving = isSubjectToArchiving;
this.nonStandardRefTimeStampForArchiving = nonStandardRefTimeStampForArchiving;
this.noTransferToProduction = noTransferToProduction;
this.noFto = noFto;
this.ftoSingleObjProcessing = ftoSingleObjProcessing;
this.tabSpaceData = tabSpaceData;
this.tabSpaceLong = tabSpaceLong;
this.tabSpaceNl = tabSpaceNl;
this.tabSpaceIndex = tabSpaceIndex;
this.defaultStatus = defaultStatus;
this.useLrtCommitPreprocess = useLrtCommitPreprocess;
this.hasBusinessKey = hasBusinessKey;
this.classIdStr = classIdStr;
this.classNlIndex = classNlIndex;
this.aggHeadClassIndex = aggHeadClassIndex;
this.aggHeadClassIndexExact = aggHeadClassIndexExact;
this.aggHeadClassIdStr = aggHeadClassIdStr;
this.isAggHead = isAggHead;
this.hasSubClass = hasSubClass;
this.classIndex = classIndex;
this.superClassIndex = superClassIndex;
this.subclassIdStrListNonAbstract = subclassIdStrListNonAbstract;
this.subClassIdStrSeparatePartition = subClassIdStrSeparatePartition;
this.sectionIndex = sectionIndex;
this.sectionShortName = sectionShortName;
this.orMappingSuperClassIndex = orMappingSuperClassIndex;
this.hasOwnTable = hasOwnTable;
this.attrRefs = attrRefs;
this.attrRefsInclSubClasses = attrRefsInclSubClasses;
this.attrRefsInclSubClassesWithRepeat = attrRefsInclSubClassesWithRepeat;
this.nlAttrRefs = nlAttrRefs;
this.nlAttrRefsInclSubclasses = nlAttrRefsInclSubclasses;
this.numAttrsInNonGen = numAttrsInNonGen;
this.numAttrsInGen = numAttrsInGen;
this.numNlAttrsInNonGen = numNlAttrsInNonGen;
this.numNlAttrsInGen = numNlAttrsInGen;
this.numRelBasedFkAttrs = numRelBasedFkAttrs;
this.hasNlAttrsInNonGenInclSubClasses = hasNlAttrsInNonGenInclSubClasses;
this.hasNlAttrsInGenInclSubClasses = hasNlAttrsInGenInclSubClasses;
this.hasLabel = hasLabel;
this.hasLabelInGen = hasLabelInGen;
this.indexRefs = indexRefs;
this.relRefs = relRefs;
this.relRefsRecursive = relRefsRecursive;
this.implicitelyGenChangeComment = implicitelyGenChangeComment;
this.tabSpaceIndexData = tabSpaceIndexData;
this.tabSpaceIndexIndex = tabSpaceIndexIndex;
this.tabSpaceIndexLong = tabSpaceIndexLong;
this.tabSpaceIndexNl = tabSpaceIndexNl;
this.containsIsNotPublished = containsIsNotPublished;
this.containsIsNotPublishedInclSubClasses = containsIsNotPublishedInclSubClasses;
this.isPriceAssignment = isPriceAssignment;
this.hasPriceAssignmentSubClass = hasPriceAssignmentSubClass;
this.hasPriceAssignmentAggHead = hasPriceAssignmentAggHead;
this.isSubjectToPreisDurchschuss = isSubjectToPreisDurchschuss;
this.subclassIdStrListNonAbstractPriceAssignment = subclassIdStrListNonAbstractPriceAssignment;
this.isSubjectToExpCopy = isSubjectToExpCopy;
this.supportXmlExport = supportXmlExport;
this.hasExpressionInNonGen = hasExpressionInNonGen;
this.hasExpressionInGen = hasExpressionInGen;
this.allowedCountriesRelIndex = allowedCountriesRelIndex;
this.disAllowedCountriesRelIndex = disAllowedCountriesRelIndex;
this.allowedCountriesListRelIndex = allowedCountriesListRelIndex;
this.disAllowedCountriesListRelIndex = disAllowedCountriesListRelIndex;
this.isValidForOrganization = isValidForOrganization;
this.hasOrganizationSpecificReference = hasOrganizationSpecificReference;
this.relRefsToOrganizationSpecificClasses = relRefsToOrganizationSpecificClasses;
this.hasGroupIdAttrInNonGen = hasGroupIdAttrInNonGen;
this.hasGroupIdAttrInNonGenInclSubClasses = hasGroupIdAttrInNonGenInclSubClasses;
this.hasExpBasedVirtualAttrInNonGen = hasExpBasedVirtualAttrInNonGen;
this.hasExpBasedVirtualAttrInGen = hasExpBasedVirtualAttrInGen;
this.hasExpBasedVirtualAttrInNonGenInclSubClasses = hasExpBasedVirtualAttrInNonGenInclSubClasses;
this.hasExpBasedVirtualAttrInGenInclSubClasses = hasExpBasedVirtualAttrInGenInclSubClasses;
this.hasRelBasedVirtualAttrInNonGen = hasRelBasedVirtualAttrInNonGen;
this.hasRelBasedVirtualAttrInGen = hasRelBasedVirtualAttrInGen;
this.hasRelBasedVirtualAttrInNonGenInclSubClasses = hasRelBasedVirtualAttrInNonGenInclSubClasses;
this.hasRelBasedVirtualAttrInGenInclSubClasses = hasRelBasedVirtualAttrInGenInclSubClasses;
this.hasAttrHasConflict = hasAttrHasConflict;
this.hasIsNationalInclSubClasses = hasIsNationalInclSubClasses;
this.isLdmCsvExported = isLdmCsvExported;
this.isLdmLrtCsvExported = isLdmLrtCsvExported;
this.isCtoAliasCreated = isCtoAliasCreated;
this.isXsdExported = isXsdExported;
this.navPathToDiv = navPathToDiv;
this.navPathToOrg = navPathToOrg;
this.navPathToCodeType = navPathToCodeType;
this.subclassIndexes = subclassIndexes;
this.subclassIndexesRecursive = subclassIndexesRecursive;
this.aggChildClassIndexes = aggChildClassIndexes;
this.aggChildRelIndexes = aggChildRelIndexes;
this.clMapAttrs = clMapAttrs;
this.clMapAttrsInclSubclasses = clMapAttrsInclSubclasses;
this.groupIdAttrIndexes = groupIdAttrIndexes;
this.groupIdAttrIndexesInclSubclasses = groupIdAttrIndexesInclSubclasses;
}
}

class ClassDescriptors {
public M22_Class_Utilities.ClassDescriptor[] descriptors;
public int numDescriptors;

public ClassDescriptors(int numDescriptors, M22_Class_Utilities.ClassDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initStrListMaps(M22_Class_Utilities.StrListMaps mapping) {
mapping.numMaps = 0;
}


public static void addStrListMapEntry(M22_Class_Utilities.StrListMaps mapping,  String name, String list) {
name = name.toUpperCase();

int i;
for (int i = 1; i <= mapping.numMaps; i++) {
if (mapping.maps[i].name.compareTo(name) == 0) {
String[] elems;
elems = list.split(",");
int j;
for (int j = M00_Helper.lBound(elems); j <= M00_Helper.uBound(elems); j++) {
if (!(M00_Helper.inStr(1, mapping.maps[i].list, elems[j]) != 0)) {
mapping.maps[i].list = mapping.maps[i].list + (mapping.maps[i].list.compareTo("") == 0 ? "" : ",") + elems[j];
}
}
return;
}
}

if (mapping.numMaps == 0) {
mapping.maps =  new M22_Class_Utilities.StrListMap[M01_Common.gc_allocBlockSize];
} else if (mapping.numMaps >= M00_Helper.uBound(mapping.maps)) {
M22_Class_Utilities.StrListMap[] mapsBackup = mapping.maps;
mapping.maps =  new M22_Class_Utilities.StrListMap[mapping.numMaps + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M22_Class_Utilities.StrListMap value : mapsBackup) {
mapping.maps[indexCounter] = value;
indexCounter++;
}
}

mapping.numMaps = mapping.numMaps + 1;

mapping.maps[mapping.numMaps].name = name;
mapping.maps[mapping.numMaps].list = list;

}


public static void initClassDescriptors(M22_Class_Utilities.ClassDescriptors classes) {
classes.numDescriptors = 0;
}


public static Integer allocClassDescriptorIndex(M22_Class_Utilities.ClassDescriptors classes) {
Integer returnValue;
returnValue = -1;

if (classes.numDescriptors == 0) {
classes.descriptors =  new M22_Class_Utilities.ClassDescriptor[M01_Common.gc_allocBlockSize];
} else if (classes.numDescriptors >= M00_Helper.uBound(classes.descriptors)) {
M22_Class_Utilities.ClassDescriptor[] descriptorsBackup = classes.descriptors;
classes.descriptors =  new M22_Class_Utilities.ClassDescriptor[classes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M22_Class_Utilities.ClassDescriptor value : descriptorsBackup) {
classes.descriptors[indexCounter] = value;
indexCounter++;
}
}
classes.numDescriptors = classes.numDescriptors + 1;
M24_Attribute_Utilities.initAttrDescriptorRefs(classes.descriptors[classes.numDescriptors].attrRefs);
M24_Attribute_Utilities.initAttrDescriptorRefs(classes.descriptors[classes.numDescriptors].nlAttrRefs);
M24_Attribute_Utilities.initAttrDescriptorRefs(classes.descriptors[classes.numDescriptors].nlAttrRefsInclSubclasses);
M22_Class_Utilities.initStrListMaps(classes.descriptors[classes.numDescriptors].subClassIdStrSeparatePartition);

classes.descriptors[classes.numDescriptors].relRefs.numRefs = 0;
returnValue = classes.numDescriptors;
return returnValue;
}


public static Integer getClassMapping(String str) {
Integer returnValue;
str = str + "".trim().toUpperCase();
if ((str.compareTo("SUPER") == 0)) {
returnValue = M22_Class_Utilities.ClassMapping.cmSuper;
} else if ((str.compareTo("SUB") == 0)) {
returnValue = M22_Class_Utilities.ClassMapping.cmSub;
} else {
returnValue = M22_Class_Utilities.ClassMapping.cmOwn;
}
return returnValue;
}


public static void printChapterHeader(String header, int fileNo) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine1);
M00_FileWriter.printToFile(fileNo, "-- #");
M00_FileWriter.printToFile(fileNo, "-- #    " + header);
M00_FileWriter.printToFile(fileNo, "-- #");
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine1);
M00_FileWriter.printToFile(fileNo, "");
}


public static String printComment(String comment, int fileNo, Integer outputModeW, Integer indentW) {
Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 0;
} else {
indent = indentW;
}

String returnValue;
returnValue = "";

// ### IF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) == 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomDecl) != 0) & ((outputMode &  M01_Common.DdlOutputMode.edomComment) == 0) & ((outputMode &  M01_Common.DdlOutputMode.edomNoDdlComment) == 0) & !((outputMode &  M01_Common.DdlOutputMode.edomNoSpecifics) == M01_Common.DdlOutputMode.edomNoSpecifics)) {
// ### ELSE IVK ###
// If ((outputMode And edomDecl) <> 0) And _
//    ((outputMode And edomComment) = 0) And _
//    ((outputMode And edomNoDdlComment) = 0) And _
//    Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
// ### ENDIF IVK ###
if (fileNo > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "-- " + comment);
} else {
returnValue = M04_Utilities.addTab(indent) + "-- " + comment;
}
}
return returnValue;
}


public static void printSectionHeader(String header, int fileNo, Integer outputModeW, String header2W) {
Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

String header2; 
if (header2W == null) {
header2 = "";
} else {
header2 = header2W;
}

// ### IF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) == 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomDecl) != 0) & ((outputMode &  M01_Common.DdlOutputMode.edomComment) == 0) & ((outputMode &  M01_Common.DdlOutputMode.edomNoDdlComment) == 0) & !((outputMode &  M01_Common.DdlOutputMode.edomNoSpecifics) == M01_Common.DdlOutputMode.edomNoSpecifics)) {
// ### ELSE IVK ###
// If ((outputMode And edomDecl) <> 0) And _
//    ((outputMode And edomComment) = 0) And _
//    ((outputMode And edomNoDdlComment) = 0) And _
//    Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine2);
M00_FileWriter.printToFile(fileNo, "--      " + header);
if (header2 != "") {
M00_FileWriter.printToFile(fileNo, "--      " + header2);
}
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine2);
}
}


public static String getClassId(int sectionNo, int classId) {
String returnValue;
returnValue = new String ("00" + sectionNo).substring(new String ("00" + sectionNo).length() - 1 - 2) + new String ("000" + classId).substring(new String ("000" + classId).length() - 1 - 3);
return returnValue;
}


public static String getClassIdByClassIndex(int thisClassIndex) {
String returnValue;
returnValue = "";

if (thisClassIndex > 0) {
if (M22_Class.g_classes.descriptors[thisClassIndex].classId > 0) {
returnValue = M22_Class_Utilities.getClassId(M20_Section.getSectionSeqNoByIndex(M22_Class.g_classes.descriptors[thisClassIndex].sectionIndex), M22_Class.g_classes.descriptors[thisClassIndex].classId);
}
}
return returnValue;
}


// ### IF IVK ###
public static void genNavPathForClass(M22_Class_Utilities.NavPathFromClassToClass navPath, String str,  int targetClassIndex) {
String[] list;
list = "".split(".");
list = str.split(".");

if (M00_Helper.uBound(list) == 1) {
String relSectionName;
String relName;

relSectionName = list[M00_Helper.lBound(list)];
relName = list[M00_Helper.lBound(list) + 1];
//determine the relationship which leads us to Division
int relIndex;
relIndex = M23_Relationship.getRelIndexByName(relSectionName, relName, null);

if (M23_Relationship.g_relationships.descriptors[relIndex].relName.compareTo("") == 0) {
M04_Utilities.logMsg("unable to determine relationship '" + str + "' supposed to lead to '" + M22_Class.g_classes.descriptors[targetClassIndex].className + "'", M01_Common.LogLevel.ellError, null, null, null);
} else {
navPath.relRefIndex = relIndex;
if (M23_Relationship.g_relationships.descriptors[relIndex].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex == targetClassIndex) {
navPath.navDirection = M01_Common.RelNavigationDirection.etLeft;
} else if (M23_Relationship.g_relationships.descriptors[relIndex].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass &  M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex == targetClassIndex) {
navPath.navDirection = M01_Common.RelNavigationDirection.etRight;
} else {
M04_Utilities.logMsg("relationship '" + str + "' does not to lead to '" + M22_Class.g_classes.descriptors[targetClassIndex].className + "'", M01_Common.LogLevel.ellError, null, null, null);
navPath.relRefIndex = -1;
}
}
}
}


// ### ENDIF IVK ###
public static void addClassIdToList(String classIdList, int thisClassIndex, Boolean includeSubClassesW) {
boolean includeSubClasses; 
if (includeSubClassesW == null) {
includeSubClasses = true;
} else {
includeSubClasses = includeSubClassesW;
}

if ((M00_Helper.inStr(1, classIdList, M22_Class.g_classes.descriptors[thisClassIndex].classIdStr) == 0) & ! M22_Class.g_classes.descriptors[thisClassIndex].isAbstract) {
classIdList = classIdList + (classIdList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[thisClassIndex].classIdStr + "'";
}
if (includeSubClasses) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive); i++) {
if ((M00_Helper.inStr(1, classIdList, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive[i]].classIdStr) == 0) & ! M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive[i]].isAbstract) {
classIdList = classIdList + (classIdList.compareTo("") == 0 ? "" : ",") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].subclassIndexesRecursive[i]].classIdStr + "'";
}
}
}
}


}