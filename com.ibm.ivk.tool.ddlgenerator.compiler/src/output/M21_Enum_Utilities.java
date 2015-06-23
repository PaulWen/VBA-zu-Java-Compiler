package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M21_Enum_Utilities {




public static final int maxAttrsPerEnum = 15;

class EnumVal {
public int id;
public int oid;
public int languageId;
public String valueString;

public boolean isOrgSpecific;

public String[] attrStrings = new String[maxAttrsPerEnum];

public EnumVal(int id, int oid, int languageId, String valueString, boolean isOrgSpecific, String[] attrStrings) {
this.id = id;
this.oid = oid;
this.languageId = languageId;
this.valueString = valueString;
this.isOrgSpecific = isOrgSpecific;
this.attrStrings = attrStrings;
}
}

class EnumVals {
public M21_Enum_Utilities.EnumVal[] vals;
public int numVals;

public EnumVals(int numVals, M21_Enum_Utilities.EnumVal[] vals) {
this.numVals = numVals;
this.vals = vals;
}
}

class EnumDescriptor {
public String sectionName;
public String enumName;
public String i18nId;
public String shortName;
public boolean isEnumLang;
public String idDomainSection;
public String idDomainName;
public int maxLength;
public boolean isCommonToOrgs;
public boolean isCommonToPools;
public int enumId;
public boolean notAcmRelated;
public boolean noAlias;
// ### IF IVK ###
public boolean noXmlExport;
public boolean useXmlExport;
// ### ENDIF IVK ###
public boolean isLrtSpecific;
public boolean isPdmSpecific;
public boolean refersToPdm;

public String tabSpaceData;
public String tabSpaceLong;
public String tabSpaceNl;
public String tabSpaceIndex;

public M21_Enum_Utilities.EnumVals values;

// derived attributes
public String enumIdStr;
public int enumIndex;
public String enumNameDb;
public Integer idDataType;
public int domainIndexId;
public int domainIndexValue;
public int sectionIndex;
public String sectionShortName;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
public int tabSpaceIndexData;
public int tabSpaceIndexIndex;
public int tabSpaceIndexLong;
public int tabSpaceIndexNl;
// ### IF IVK ###
public boolean supportXmlExport;
// ### ENDIF IVK ###

// temporary variables supporting processing
public boolean isLdmCsvExported;
// ### IF IVK ###
public boolean isXsdExported;
// ### ENDIF IVK ###
public boolean isCtoAliasCreated;

public EnumDescriptor(String sectionName, String enumName, String i18nId, String shortName, boolean isEnumLang, String idDomainSection, String idDomainName, int maxLength, boolean isCommonToOrgs, boolean isCommonToPools, int enumId, boolean notAcmRelated, boolean noAlias, boolean noXmlExport, boolean useXmlExport, boolean isLrtSpecific, boolean isPdmSpecific, boolean refersToPdm, String tabSpaceData, String tabSpaceLong, String tabSpaceNl, String tabSpaceIndex, M21_Enum_Utilities.EnumVals values, String enumIdStr, int enumIndex, String enumNameDb, Integer idDataType, int domainIndexId, int domainIndexValue, int sectionIndex, String sectionShortName, M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, int tabSpaceIndexData, int tabSpaceIndexIndex, int tabSpaceIndexLong, int tabSpaceIndexNl, boolean supportXmlExport, boolean isLdmCsvExported, boolean isXsdExported, boolean isCtoAliasCreated) {
this.sectionName = sectionName;
this.enumName = enumName;
this.i18nId = i18nId;
this.shortName = shortName;
this.isEnumLang = isEnumLang;
this.idDomainSection = idDomainSection;
this.idDomainName = idDomainName;
this.maxLength = maxLength;
this.isCommonToOrgs = isCommonToOrgs;
this.isCommonToPools = isCommonToPools;
this.enumId = enumId;
this.notAcmRelated = notAcmRelated;
this.noAlias = noAlias;
this.noXmlExport = noXmlExport;
this.useXmlExport = useXmlExport;
this.isLrtSpecific = isLrtSpecific;
this.isPdmSpecific = isPdmSpecific;
this.refersToPdm = refersToPdm;
this.tabSpaceData = tabSpaceData;
this.tabSpaceLong = tabSpaceLong;
this.tabSpaceNl = tabSpaceNl;
this.tabSpaceIndex = tabSpaceIndex;
this.values = values;
this.enumIdStr = enumIdStr;
this.enumIndex = enumIndex;
this.enumNameDb = enumNameDb;
this.idDataType = idDataType;
this.domainIndexId = domainIndexId;
this.domainIndexValue = domainIndexValue;
this.sectionIndex = sectionIndex;
this.sectionShortName = sectionShortName;
this.attrRefs = attrRefs;
this.tabSpaceIndexData = tabSpaceIndexData;
this.tabSpaceIndexIndex = tabSpaceIndexIndex;
this.tabSpaceIndexLong = tabSpaceIndexLong;
this.tabSpaceIndexNl = tabSpaceIndexNl;
this.supportXmlExport = supportXmlExport;
this.isLdmCsvExported = isLdmCsvExported;
this.isXsdExported = isXsdExported;
this.isCtoAliasCreated = isCtoAliasCreated;
}
}

class EnumDescriptors {
public M21_Enum_Utilities.EnumDescriptor[] descriptors;
public int numDescriptors;

public EnumDescriptors(int numDescriptors, M21_Enum_Utilities.EnumDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static Integer getEnumLangIndex() {
Integer returnValue;
int i;
returnValue = -1;
for (i = 1; i <= 1; i += (1)) {
if (M21_Enum.g_enums.descriptors[i].isEnumLang) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static void initEnumVals(M21_Enum_Utilities.EnumVals vals) {
vals.numVals = 0;
}


public static Integer allocEnumValIndex(M21_Enum_Utilities.EnumVals values) {
Integer returnValue;
returnValue = -1;

if (values.numVals == 0) {
values.vals =  new M21_Enum_Utilities.EnumVals[M01_Common.gc_allocBlockSize];
} else if (values.numVals >= M00_Helper.uBound(values.vals)) {
M21_Enum_Utilities.EnumVals[] valsBackup = values.vals;
values.vals =  new M21_Enum_Utilities.EnumVals[values.numVals + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M21_Enum_Utilities.EnumVals value : valsBackup) {
values.vals[indexCounter] = value;
indexCounter++;
}
}
values.numVals = values.numVals + 1;
returnValue = values.numVals;
return returnValue;
}


public static void initEnumDescriptors(M21_Enum_Utilities.EnumDescriptors enums) {
enums.numDescriptors = 0;
}

public static Integer allocEnumDescriptorIndex(M21_Enum_Utilities.EnumDescriptors enums) {
Integer returnValue;
returnValue = -1;

if (enums.numDescriptors == 0) {
enums.descriptors =  new M21_Enum_Utilities.EnumDescriptor[M01_Common.gc_allocBlockSize];
} else if (enums.numDescriptors >= M00_Helper.uBound(enums.descriptors)) {
M21_Enum_Utilities.EnumDescriptor[] descriptorsBackup = enums.descriptors;
enums.descriptors =  new M21_Enum_Utilities.EnumDescriptor[enums.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M21_Enum_Utilities.EnumDescriptor value : descriptorsBackup) {
enums.descriptors[indexCounter] = value;
indexCounter++;
}
}
enums.numDescriptors = enums.numDescriptors + 1;
returnValue = enums.numDescriptors;
return returnValue;
}

public static String getEnumIdByIndex(int thisEnumIndex) {
String returnValue;
returnValue = "";

if (thisEnumIndex > 0) {
if (M21_Enum.g_enums.descriptors[thisEnumIndex].enumId > 0) {
returnValue = new String ("00" + M20_Section.getSectionSeqNoByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex)).substring(new String ("00" + M20_Section.getSectionSeqNoByIndex(M21_Enum.g_enums.descriptors[thisEnumIndex].sectionIndex)).length() - 1 - 2) + new String ("000" + M21_Enum.g_enums.descriptors[thisEnumIndex].enumId).substring(new String ("000" + M21_Enum.g_enums.descriptors[thisEnumIndex].enumId).length() - 1 - 3);
}
}
return returnValue;
}


}