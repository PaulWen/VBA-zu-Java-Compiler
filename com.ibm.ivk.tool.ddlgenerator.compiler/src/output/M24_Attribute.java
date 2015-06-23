package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M24_Attribute {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colClass = colSection + 1;
private static final int colEntityType = colClass + 1;
private static final int colAttribute = colEntityType + 1;
private static final int colShortName = colAttribute + 1;
private static final int colMapsToClAttributes = colShortName + 1;
// ### IF IVK ###
private static final int colMapsToACMAttribute = colMapsToClAttributes + 1;
private static final int colMapsToACMAttributeForRead = colMapsToACMAttribute + 1;
private static final int colAcmMappingIsInstantiated = colMapsToACMAttributeForRead + 1;
private static final int colFtoConflictWith = colAcmMappingIsInstantiated + 1;
private static final int colGroupIdBasedOn = colFtoConflictWith + 1;
private static final int colDomainSection = colGroupIdBasedOn + 1;
// ### ELSE IVK ###
//Private Const colDomainSection = colMapsToClAttributes + 1
// ### ENDIF IVK ###
private static final int colDomain = colDomainSection + 1;
private static final int colDefault = colDomain + 1;
private static final int colIsNl = colDefault + 1;
// ### IF IVK ###
private static final int colIsNationalizable = colIsNl + 1;
private static final int colIsNullable = colIsNationalizable + 1;
// ### ELSE IVK ###
//Private Const colIsNullable = colIsNl + 1
// ### ENDIF IVK ###
private static final int colIsNullableInOrgs = colIsNullable + 1;
private static final int colIsIdentifying = colIsNullableInOrgs + 1;
private static final int colIncludeInPkIndex = colIsIdentifying + 1;
// ### IF IVK ###
private static final int colIsExpression = colIncludeInPkIndex + 1;
private static final int colNoXmlExport = colIsExpression + 1;
private static final int colIsPersistent = colNoXmlExport + 1;
private static final int colIsTimeVarying = colIsPersistent + 1;
// ### ELSE IVK ###
//Private Const colIsTimeVarying = colIncludeInPkIndex + 1
// ### ENDIF IVK ###
private static final int colComment = colIsTimeVarying + 1;
private static final int colI18nId = colComment + 1;

public static final int colAttrI18nId = colI18nId;

private static final int firstRow = 4;

private static final String sheetName = "Attr";

private static final int acmCsvProcessingStep = 5;

public static M24_Attribute_Utilities.AttributeDescriptors g_attributes;



public static void genAttrList(String[] list, String str) {
int i;
list = str.split(",");
for (int i = M00_Helper.lBound(list); i <= M00_Helper.uBound(list); i++) {
list[(i)] = list[i].trim();
}
}


private static void readSheet() {
M24_Attribute_Utilities.initAttributeDescriptors(M24_Attribute.g_attributes);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

String lastSection;
String lastClassName;
String clAttributes;
while (M00_Excel.getCell(thisSheet, thisRow, colAttribute).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
if ((M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].sectionName + "" == "")) {
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].sectionName = lastSection;
}

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].className = M00_Excel.getCell(thisSheet, thisRow, colClass).getStringCellValue().trim();
if ((M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].className + "" == "")) {
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].className = lastClassName;
}

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].attributeName = M00_Excel.getCell(thisSheet, thisRow, colAttribute).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].cType = M24_Attribute_Utilities.getAttrContainerType(M00_Excel.getCell(thisSheet, thisRow, colEntityType).getStringCellValue().trim());
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue().trim();
clAttributes = M00_Excel.getCell(thisSheet, thisRow, colMapsToClAttributes).getStringCellValue().trim();
if (!(clAttributes.compareTo("") == 0)) {
M24_Attribute.genAttrList(M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].mapsToChangeLogAttributes, clAttributes);
}
// ### IF IVK ###
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].ftoConflictWith = M00_Excel.getCell(thisSheet, thisRow, colFtoConflictWith).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].groupIdBasedOn = M00_Excel.getCell(thisSheet, thisRow, colGroupIdBasedOn).getStringCellValue().trim();
if (!(M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].groupIdBasedOn.compareTo("") == 0)) {
M24_Attribute.genAttrList(M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].groupIdAttributes, M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].groupIdBasedOn);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isGroupId = true;
}

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyMapsTo.description = M00_Excel.getCell(thisSheet, thisRow, colMapsToACMAttribute).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isVirtual = (!(M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyMapsTo.description.compareTo("") == 0));
if (M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isVirtual) {
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyMapsToForRead.description = M00_Excel.getCell(thisSheet, thisRow, colMapsToACMAttributeForRead).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyMapsTo.isInstantiated = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colAcmMappingIsInstantiated).getStringCellValue(), null);
}

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNationalizable = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNationalizable).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isExpression = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsExpression).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].noXmlExport = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colNoXmlExport).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isPersistent = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPersistent).getStringCellValue(), null);
// ### ENDIF IVK ###

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].domainSection = M00_Excel.getCell(thisSheet, thisRow, colDomainSection).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].domainName = M00_Excel.getCell(thisSheet, thisRow, colDomain).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].defaultValue = M00_Excel.getCell(thisSheet, thisRow, colDefault).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNl = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNl).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNullable = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsNullable).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNullableInOrgs = M00_Excel.getCell(thisSheet, thisRow, colIsNullableInOrgs).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isIdentifying = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsIdentifying).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].includeInPkIndex = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIncludeInPkIndex).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isTimeVarying = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsTimeVarying).getStringCellValue(), null);
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].comment = M00_Excel.getCell(thisSheet, thisRow, colComment).getStringCellValue().trim();
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNotAcmRelated = true;
// ### IF IVK ###

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyReferredToBy =  new int[0];
// ### ENDIF IVK ###

lastSection = M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].sectionName;
lastClassName = M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].className;

NextRow:
thisRow = thisRow + 1;
}
}

// ### IF IVK ###
public static void addAttribute(String sectionName, String entityName, Integer Integer, String attributeName, String shortName, String domainSection, String domainName, String defaultValueW, Boolean isNlW, Boolean isNationalizableW, Boolean isNullableW, Boolean isIdentifyingW, Boolean isExpressionW, Boolean isTimeVaryingW, String commentW, Boolean isVirtualW) {
String defaultValue; 
if (defaultValueW == null) {
defaultValue = "";
} else {
defaultValue = defaultValueW;
}

boolean isNl; 
if (isNlW == null) {
isNl = false;
} else {
isNl = isNlW;
}

boolean isNationalizable; 
if (isNationalizableW == null) {
isNationalizable = false;
} else {
isNationalizable = isNationalizableW;
}

boolean isNullable; 
if (isNullableW == null) {
isNullable = false;
} else {
isNullable = isNullableW;
}

boolean isIdentifying; 
if (isIdentifyingW == null) {
isIdentifying = false;
} else {
isIdentifying = isIdentifyingW;
}

boolean isExpression; 
if (isExpressionW == null) {
isExpression = false;
} else {
isExpression = isExpressionW;
}

boolean isTimeVarying; 
if (isTimeVaryingW == null) {
isTimeVarying = false;
} else {
isTimeVarying = isTimeVaryingW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

// ### ELSE IVK ###
//Sub addAttribute( _
// ByRef sectionName As String, _
// ByRef entityName As String, _
// ByRef containerType As AcmAttrContainerType, _
// ByRef attributeName As String, _
// ByRef shortName As String, _
// ByRef domainSection As String, _
// ByRef domainName As String, _
// Optional ByRef default As String = "", _
// Optional isNl As Boolean = False, _
// Optional isNullable As Boolean = False, _
// Optional isIdentifying As Boolean = False, _
// Optional isTimeVarying As Boolean, _
// Optional ByRef comment As String = "" _
//)
// ### ENDIF IVK ###
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].sectionName = sectionName;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].className = entityName;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].cType = Integer;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].attributeName = attributeName;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].shortName = shortName;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].domainSection = domainSection;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].domainName = domainName;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].defaultValue = defaultValue;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNl = isNl;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNullable = isNullable;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isIdentifying = isIdentifying;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isTimeVarying = isTimeVarying;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].comment = comment;
// ### IF IVK ###
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isNationalizable = isNationalizable;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isExpression = isExpression;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isPersistent = true;

M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].isVirtual = isVirtual;
M24_Attribute.g_attributes.descriptors[M24_Attribute_Utilities.allocAttributeDescriptorIndex(M24_Attribute.g_attributes)].virtuallyReferredToBy =  new int[0];
// ### ENDIF IVK ###
}


public static void getAttributes() {
if (M24_Attribute.g_attributes.numDescriptors == 0) {
readSheet();
}
}


public static void resetAttributes() {
M24_Attribute.g_attributes.numDescriptors = 0;
}


public static Integer getAttributeIndexByName(String sectionName, String attributeName) {
Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() == attributeName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


// ### IF IVK ###
public static Integer getAttributeIndexByNameAndEntityIndex(String attributeName, Integer acmEntityType, int acmEntityIndex,  Boolean includeVirtualAttrW) {
boolean includeVirtualAttr; 
if (includeVirtualAttrW == null) {
includeVirtualAttr = false;
} else {
includeVirtualAttr = includeVirtualAttrW;
}

Integer returnValue;
// ### ELSE IVK ###
//Function getAttributeIndexByNameAndEntityIndex( _
// ByRef attributeName As String, _
// ByRef acmEntityType As AcmAttrContainerType, _
// ByRef acmEntityIndex As Integer _
//) As Integer
// ### ENDIF IVK ###
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if ((M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() == attributeName.toUpperCase() |  M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() + M01_Globals.gc_enumAttrNameSuffix == attributeName.toUpperCase()) &  M24_Attribute.g_attributes.descriptors[i].cType.compareTo(acmEntityType) == 0 & M24_Attribute.g_attributes.descriptors[i].acmEntityIndex == acmEntityIndex) {
// ### IF IVK ###
if ((includeVirtualAttr | ! M24_Attribute.g_attributes.descriptors[i].isVirtual)) {
// ### ENDIF IVK ###
returnValue = i;
return returnValue;
// ### IF IVK ###
}
// ### ENDIF IVK ###
}
}
return returnValue;
}


// ### IF IVK ###
public static Integer getAttributeIndexByNameAndEntityIndexRaw(String attributeName, Integer acmEntityType, int acmEntityIndex,  Boolean includeVirtualAttrW) {
boolean includeVirtualAttr; 
if (includeVirtualAttrW == null) {
includeVirtualAttr = false;
} else {
includeVirtualAttr = includeVirtualAttrW;
}

Integer returnValue;
// ### ELSE IVK ###
//Function getAttributeIndexByNameAndEntityIndexRaw( _
//  ByRef attributeName As String, _
//  ByRef acmEntityType As AcmAttrContainerType, _
//  ByRef acmEntityIndex As Integer _
//) As Integer
// ### ENDIF IVK ###
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if ((M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() == attributeName.toUpperCase() |  M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() + M01_Globals.gc_enumAttrNameSuffix == attributeName.toUpperCase()) &  M24_Attribute.g_attributes.descriptors[i].cType.compareTo(acmEntityType) == 0 & M24_Attribute.g_attributes.descriptors[i].acmEntityIndex == acmEntityIndex) {
// ### IF IVK ###
if ((includeVirtualAttr | ! M24_Attribute.g_attributes.descriptors[i].isVirtual)) {
// ### ENDIF IVK ###
returnValue = i;
return returnValue;
// ### IF IVK ###
}
// ### ENDIF IVK ###
}
}
return returnValue;
}


// ### IF IVK ###
public static Integer getAttributeIndexByNameAndEntityIndexRecursive(String attributeName, Integer acmEntityType, int acmEntityIndex,  Boolean includeVirtualAttrW) {
boolean includeVirtualAttr; 
if (includeVirtualAttrW == null) {
includeVirtualAttr = false;
} else {
includeVirtualAttr = includeVirtualAttrW;
}

Integer returnValue;
// ### ELSE IVK ###
//Function getAttributeIndexByNameAndEntityIndexRecursive( _
//  ByRef attributeName As String, _
//  ByRef acmEntityType As AcmAttrContainerType, _
//  ByRef acmEntityIndex As Integer _
//) As Integer
// ### ENDIF IVK ###
int thisAttrIndex;
int i;

returnValue = -1;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// ### IF IVK ###
thisAttrIndex = M24_Attribute.getAttributeIndexByNameAndEntityIndex(attributeName, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, acmEntityIndex, includeVirtualAttr);
// ### ELSE IVK ###
//     thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, acmEntityIndex)
// ### ENDIF IVK ###
if (thisAttrIndex > 0) {
returnValue = thisAttrIndex;
return returnValue;
}
for (int i = M00_Helper.lBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive); i++) {
// ### IF IVK ###
thisAttrIndex = M24_Attribute.getAttributeIndexByNameAndEntityIndex(attributeName, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexesRecursive[i], includeVirtualAttr);
// ### ELSE IVK ###
//       thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, .subclassIndexesRecursive(i))
// ### ENDIF IVK ###
if (thisAttrIndex > 0) {
returnValue = thisAttrIndex;
return returnValue;
}
}
} else {
// ### IF IVK ###
returnValue = M24_Attribute.getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex, includeVirtualAttr);
// ### ELSE IVK ###
//   getAttributeIndexByNameAndEntityIndexRecursive = getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex)
// ### ENDIF IVK ###
}
return returnValue;
}

public static Integer getAttributeIndexByI18nId(String i18nId) {
Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[i].i18nId.toUpperCase() == i18nId.toUpperCase()) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}

public static Integer getMaxDbAttributeLengthByNameAndEntityIndex(String attributeName, Integer acmEntityType, int acmEntityIndex,  Boolean includeVirtualAttrW) {
boolean includeVirtualAttr; 
if (includeVirtualAttrW == null) {
includeVirtualAttr = false;
} else {
includeVirtualAttr = includeVirtualAttrW;
}

Integer returnValue;
returnValue = -1;

int attrIndex;
attrIndex = M24_Attribute.getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex, null);

if (attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].domainIndex > 0) {
returnValue = M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].domainIndex].maxLength * (M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].domainIndex].supportUnicode ? M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].domainIndex].unicodeExpansionFactor : 1);
}
}

return returnValue;
}


public static String getPkAttrListByClass(int classIndex, Integer ddlTypeW, String prefixW, Boolean forLrtW, Boolean includedExtraAttrsW, Boolean excludeFkAttrsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean includedExtraAttrs; 
if (includedExtraAttrsW == null) {
includedExtraAttrs = false;
} else {
includedExtraAttrs = includedExtraAttrsW;
}

boolean excludeFkAttrs; 
if (excludeFkAttrsW == null) {
excludeFkAttrs = false;
} else {
excludeFkAttrs = excludeFkAttrsW;
}

String returnValue;

//On Error GoTo ErrorExit 

String pkAttrList;
returnValue = "";
pkAttrList = "";

String relNameInfix;

int i;
for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if ((M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].isIdentifying & ! includedExtraAttrs) |  (includedExtraAttrs & ! M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].isIdentifying & M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].includeInPkIndex)) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M04_Utilities.genAttrNameByIndex(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attrIndex, ddlType);
}
}
}

if (M03_Config.includeFksInPks & ! excludeFkAttrs) {
String relShortName;
String relDirShortName;
int srcClassIndex;
int dstClassIndex;
int j;
for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etLeft) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].maxRightCardinality == 1) {
if (!(includedExtraAttrs &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].isIdentifyingRight)) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rightEntityIndex].useSurrogateKey) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, null, false, ddlType, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].lrShortRelName, M01_Common.DdlOutputMode.edomList, null, null, 0, null, null);
} else {
relShortName = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName;
relDirShortName = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].lrShortRelName;
relNameInfix = (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].useLrLdmRelName ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].lrLdmRelName : relShortName + relDirShortName);
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rightEntityIndex, ddlType, prefix + relNameInfix + (new String ("_" + prefix + relNameInfix).substring(new String ("_" + prefix + relNameInfix).length() - 1 - 1).compareTo("_") == 0 ? "" : "_"), forLrt, null, null);
}
} else if (includedExtraAttrs &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].includeInPkIndex) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, null, false, ddlType, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].lrShortRelName, M01_Common.DdlOutputMode.edomList, null, null, 0, null, null);
}
}
} else if (M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].maxLeftCardinality == 1) {
if (!(includedExtraAttrs &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].isIdentifyingLeft)) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].leftEntityIndex].useSurrogateKey) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, null, false, ddlType, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rlShortRelName, M01_Common.DdlOutputMode.edomList, null, null, 0, null, null);
} else {
relShortName = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName;
relDirShortName = M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rlShortRelName;
relNameInfix = (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].useRlLdmRelName ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rlLdmRelName : relShortName + relDirShortName);
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + M24_Attribute.getPkAttrListByClass(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].leftEntityIndex, ddlType, prefix + relNameInfix + (new String ("_" + prefix + relNameInfix).substring(new String ("_" + prefix + relNameInfix).length() - 1 - 1).compareTo("_") == 0 ? "" : "_"), forLrt, null, null);
}
} else if (includedExtraAttrs &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].includeInPkIndex) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M04_Utilities.genAttrDeclByDomain(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, classIndex, null, false, ddlType, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex].rlShortRelName, M01_Common.DdlOutputMode.edomList, null, null, 0, null, null);
}
}
}
}
}
// ### IF IVK ###

if (!(pkAttrList.compareTo("") == 0)) {
if (M22_Class.g_classes.descriptors[classIndex].isNationalizable &  M03_Config.nationalFlagPartOfPK) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M01_Globals_IVK.g_anIsNational;
}
}
if (!(pkAttrList.compareTo("") == 0) | ! M22_Class.g_classes.descriptors[classIndex].useSurrogateKey) {
if (M22_Class.g_classes.descriptors[classIndex].isPsTagged &  (!(M22_Class.g_classes.descriptors[classIndex].psTagNotIdentifying |  includedExtraAttrs))) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + prefix.toUpperCase() + M01_Globals_IVK.g_anPsOid;
}
}
// ### ENDIF IVK ###

returnValue = pkAttrList;

NormalExit:
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


public static String getPkAttrListByClassIndex(int classIndex, Integer ddlTypeW, String prefixW, Boolean forLrtW, Boolean includeExtraAttrsW, Boolean excludeFkAttrsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean includeExtraAttrs; 
if (includeExtraAttrsW == null) {
includeExtraAttrs = false;
} else {
includeExtraAttrs = includeExtraAttrsW;
}

boolean excludeFkAttrs; 
if (excludeFkAttrsW == null) {
excludeFkAttrs = false;
} else {
excludeFkAttrs = excludeFkAttrsW;
}

String returnValue;
returnValue = M24_Attribute.getPkAttrListByClass(classIndex, ddlType, prefix, forLrt, includeExtraAttrs, excludeFkAttrs);
return returnValue;
}

public static String getPkAttrListByRel(int thisRelIndex, Integer ddlTypeW, String prefixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String returnValue;
returnValue = M24_Attribute.getPkAttrListByRelIndex(thisRelIndex, ddlType, prefix);
return returnValue;
}


public static String getPkAttrListByRelIndex(int relIndex, Integer ddlTypeW, String prefixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String returnValue;
returnValue = "";

String pkAttrList;
pkAttrList = "";

int i;
for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].attrRefs.descriptors[i].refIndex].isIdentifying) {
if (M24_Attribute.g_attributes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].attrRefs.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ", ") + prefix.toUpperCase() + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].attrRefs.descriptors[i].refIndex].attributeName + M01_Globals.gc_enumAttrNameSuffix, ddlType, null, null, null, null, null, null);
} else {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ", ") + prefix.toUpperCase() + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M23_Relationship.g_relationships.descriptors[relIndex].attrRefs.descriptors[i].refIndex].attributeName, ddlType, null, null, null, null, null, null);
}
}
}

returnValue = pkAttrList;
return returnValue;
}


public static void genAttrListForClassRecursive(int classIndex, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

//On Error GoTo ErrorExit 

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

if (M22_Class.g_classes.descriptors[classIndex].isGenForming) {
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M22_Class.genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, true, true, forLrt, outputMode, direction, null, null, null);
M22_Class.genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, false, false, forLrt, outputMode, direction, null, null, null);
} else {
M22_Class.genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, false, forLrt, outputMode, direction, null, null, null);
}
// ### ELSE IVK ###
//     genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
// ### ENDIF IVK ###
} else {
M22_Class.genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, null, forLrt, outputMode, direction, null, null, null);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrListForClassRecursive(int classIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M24_Attribute.genTransformedAttrListForClassRecursiveWithColReuse(classIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, direction);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrListForClassRecursiveWithColReuse(int classIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

//On Error GoTo ErrorExit 

if (M22_Class.g_classes.descriptors[classIndex].isGenForming) {
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].hasNoIdentity) {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, true, true, forLrt, outputMode, direction, null, null, null, null, null);
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, false, false, forLrt, outputMode, direction, null, null, null, null, null);
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, false, forLrt, outputMode, direction, null, null, null, null, null);
}
// ### ELSE IVK ###
//     genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
// ### ENDIF IVK ###
} else {
M22_Class.genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, null, forLrt, outputMode, direction, null, null, null, null, null);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genAttrListForEntity(int acmEntityIndex, Integer acmEntityType, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genAttrListForClassRecursive(acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, M01_Common.RecursionDirection.erdDown);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M22_Class.genAttrDeclsForRelationship(acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, false, forLrt, outputMode);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M22_Class.genAttrDeclsForEnum(acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, null);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrListForEntity(int acmEntityIndex, Integer acmEntityType, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean useVersiontagW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean useVersiontag; 
if (useVersiontagW == null) {
useVersiontag = true;
} else {
useVersiontag = useVersiontagW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, direction);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M22_Class.genTransformedAttrDeclsForRelationship(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, false, forLrt, outputMode);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M22_Class.genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, outputMode, useVersiontag);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrListForEntityWithColReuse(int acmEntityIndex, Integer acmEntityType, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forLrtW, Boolean forGenW, Integer outputModeW, Integer directionW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomListLrt;
} else {
outputMode = outputModeW;
}

Integer direction; 
if (directionW == null) {
direction = M01_Common.RecursionDirection.erdDown;
} else {
direction = directionW;
}

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genTransformedAttrListForClassRecursiveWithColReuse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, direction);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M23_Relationship.genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, null, false, forLrt, outputMode);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M22_Class.genTransformedAttrDeclsForEnumWithColReuse(acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, outputMode, null);
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForEntityWithColReUse(Integer acmEntityType, int acmEntityIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Boolean forSubClassW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean useSurrogateKeyW, Boolean classIsGenFormingW, Boolean forGenW, Boolean suppressOidW, Boolean enforceClassIdW, Boolean isUserTransactionalW, Boolean suppressTrailingCommaW, Boolean forLrtW, Integer outputModeW, Integer indentW, Boolean suppressLrtStatusW, String genParentTabNameW, Boolean suppressColConstraintsW, Boolean useAlternativeDefaultsW, Boolean suppressMetaAttrsW) {
boolean forSubClass; 
if (forSubClassW == null) {
forSubClass = false;
} else {
forSubClass = forSubClassW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean useSurrogateKey; 
if (useSurrogateKeyW == null) {
useSurrogateKey = true;
} else {
useSurrogateKey = useSurrogateKeyW;
}

boolean classIsGenForming; 
if (classIsGenFormingW == null) {
classIsGenForming = false;
} else {
classIsGenForming = classIsGenFormingW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean suppressOid; 
if (suppressOidW == null) {
suppressOid = false;
} else {
suppressOid = suppressOidW;
}

boolean enforceClassId; 
if (enforceClassIdW == null) {
enforceClassId = false;
} else {
enforceClassId = enforceClassIdW;
}

boolean isUserTransactional; 
if (isUserTransactionalW == null) {
isUserTransactional = false;
} else {
isUserTransactional = isUserTransactionalW;
}

boolean suppressTrailingComma; 
if (suppressTrailingCommaW == null) {
suppressTrailingComma = false;
} else {
suppressTrailingComma = suppressTrailingCommaW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean suppressLrtStatus; 
if (suppressLrtStatusW == null) {
suppressLrtStatus = false;
} else {
suppressLrtStatus = suppressLrtStatusW;
}

String genParentTabName; 
if (genParentTabNameW == null) {
genParentTabName = "";
} else {
genParentTabName = genParentTabNameW;
}

boolean suppressColConstraints; 
if (suppressColConstraintsW == null) {
suppressColConstraints = false;
} else {
suppressColConstraints = suppressColConstraintsW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

boolean suppressMetaAttrs; 
if (suppressMetaAttrsW == null) {
suppressMetaAttrs = false;
} else {
suppressMetaAttrs = suppressMetaAttrsW;
}

int i;
String attrSpecifics;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
boolean isAggregate;
boolean forLrtMqt;
String entitySectionName;
String entityName;
String entityIdStr;
boolean M72_DataPool.poolSupportLrt;
// ### IF IVK ###
int defaultStatus;
boolean isPsForming;
boolean supportPsCopy;
boolean ahSupportPsCopy;
boolean condenseData;
boolean instantiateExpressions;

condenseData = false;
// ### ENDIF IVK ###

if (thisPoolIndex > 0) {
// ### IF IVK ###
instantiateExpressions = M72_DataPool.g_pools.descriptors[thisPoolIndex].instantiateExpressions;
// ### ENDIF IVK ###
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
// ### IF IVK ###
} else {
instantiateExpressions = false;
// ### ENDIF IVK ###
}

//On Error GoTo ErrorExit 

forLrtMqt = forLrt &  ((outputMode &  M01_Common.DdlOutputMode.edomMqtLrt) == M01_Common.DdlOutputMode.edomMqtLrt);

// FIXME: in general we need to set this depending on the class resp. relationship

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entitySectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
// ### IF IVK ###
defaultStatus = M22_Class.g_classes.descriptors[acmEntityIndex].defaultStatus;
isPsForming = M22_Class.g_classes.descriptors[acmEntityIndex].isPsForming;
supportPsCopy = M22_Class.g_classes.descriptors[acmEntityIndex].supportExtendedPsCopy;
if (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) {
ahSupportPsCopy = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].supportExtendedPsCopy;
}
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###
isAggregate = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entitySectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;

attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
// ### IF IVK ###
defaultStatus = M23_Relationship.g_relationships.descriptors[acmEntityIndex].defaultStatus;
isPsForming = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsForming;
supportPsCopy = M23_Relationship.g_relationships.descriptors[acmEntityIndex].supportExtendedPsCopy;
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0) {
ahSupportPsCopy = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].supportExtendedPsCopy;
}
// ### ENDIF IVK ###
isAggregate = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
entitySectionName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName;
entityName = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;

attrRefs = M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs;
// ### IF IVK ###
defaultStatus = M86_SetProductive.statusReadyForActivation;
isPsForming = false;
// ### ENDIF IVK ###
isAggregate = false;
entityIdStr = M21_Enum.g_enums.descriptors[acmEntityIndex].enumIdStr;
}

if (!(forSubClass & ! suppressMetaAttrs)) {
if (!(suppressOid)) {
if (useSurrogateKey) {
if (forGen &  acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (genParentTabName != "") {
M22_Class_Utilities.printSectionHeader("Foreign Key to 'Parent Table' (" + genParentTabName + ")", fileNo, outputMode, null);
}

M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M22_Class.g_classes.descriptors[acmEntityIndex].shortName + "_" + M01_ACM.cosnOid, M22_Class.g_classes.descriptors[acmEntityIndex].shortName + "_" + M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  M01_Common.AttrCategory.eacFkOidParent, null, indent, null, "[LDM] Foreign Key / Object Identifier", null, null, null, null, null), null, null);
}
M22_Class_Utilities.printSectionHeader("Surrogate Key", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacOid, null, indent, null, "[LDM] Object Identifier", null, null, null, null, null), null, null);
}

if (isUserTransactional &  M01_Globals.g_genLrtSupport & (outputMode &  M01_Common.DdlOutputMode.edomMqtLrt)) {
M22_Class_Utilities.printSectionHeader("Flag '" + M01_ACM.conIsLrtPrivate + "'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conIsLrtPrivate, M01_ACM.cosnIsLrtPrivate, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexIsLrtPrivate, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacMqtLrtMeta, null, indent, null, "[LRT-MQT] identifies 'LRT-private' records", "0", null, null, null, null), null, null);
// ### IF IVK ###
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Column '" + M01_ACM.conInUseBy + "'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, M01_Common.AttrCategory.eacMqtLrtMeta, null, indent, null, "[LRT-MQT] identifies the user holding the lock on the record", null, null, true, null, null), null, null);
}
// ### ELSE IVK ###
//       printSectionHeader "Column '" & conInUseBy & "'", fileNo, outputMode
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
//           "[LRT-MQT] identifies the user holding the lock on the record", , True _
//         )
// ### ENDIF IVK ###
}

if (isUserTransactional &  M01_Globals.g_genLrtSupport & ((outputMode &  M01_Common.DdlOutputMode.edomListNoLrt) != M01_Common.DdlOutputMode.edomListNoLrt)) {
// ### IF IVK ###
if ((forLrt | ! condenseData)) {
M22_Class_Utilities.printSectionHeader("LRT - Id", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt & ! forLrtMqt ? "NOT NULL" : ""), null, ddlType, null, outputMode &  (((outputMode &  M01_Common.DdlOutputMode.edomValue) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomLrtPriv) != 0) ? !(M01_Common.DdlOutputMode.edomList) : !()), M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Identifier of LRT the record is involved in", null, null, !(forLrt |  forLrtMqt), null, null), null, null);
}
// ### ELSE IVK ###
//       If forLrt Then
//         printSectionHeader "LRT - Id", fileNo, outputMode
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, transformation, _
//             tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
//             outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
//             eacLrtMeta, , indent, , "[LRT] Identifier of LRT the record is involved in", , Not forLrt Or forLrtMqt _
//           )
//       End If
// ### ENDIF IVK ###

// ### IF IVK ###
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Flag 'status'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt |  forLrtMqt ? "" : "NOT NULL DEFAULT " + (useAlternativeDefaults ? M86_SetProductive.statusProductive : M86_SetProductive.statusWorkInProgress)), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, indent, null, "[ACM] Specifies the state of the record with respect to 'release to production'", String.valueOf(defaultStatus), null, null, null, null), null, null);
}
// ### ENDIF IVK ###
}

if (isAggregate) {
// LRT-specific columns wich exist in public and in private tables
M22_Class_Utilities.printSectionHeader("ClassId of 'aggregate head'", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conAhClassId, M01_ACM.cosnAggHeadClassId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexCid, transformation, tabColumns, acmEntityType, acmEntityIndex, (M03_Config.generateAhIdsNotNull & ! useAlternativeDefaults ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacCid |  M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[MET] ID of the ACM-class of the 'Aggregate Head'", null, null, !(M03_Config.generateAhIdsNotNull |  useAlternativeDefaults), null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , _
//           ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
//           "[MET] ID of the ACM-class of the 'Aggregate Head'", , Not generateAhIdsNotNull Or useAlternativeDefaults _
//         )
// ### ENDIF IVK ###

// ### IF IVK ###
M22_Class_Utilities.printSectionHeader("ObjectId of 'aggregate head'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conAhOId, M01_ACM.cosnAggHeadOId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, (M03_Config.generateAhIdsNotNull & ! useAlternativeDefaults ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  M01_Common.AttrCategory.eacLrtMeta | M01_Common.AttrCategory.eacAhOid | (isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (ahSupportPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0), null, indent, null, "[MET] Object ID of the 'Aggregate Head'", null, null, !(M03_Config.generateAhIdsNotNull |  useAlternativeDefaults), null, null), null, null);
}

if (isUserTransactional &  M01_Globals.g_genLrtSupport & ((outputMode &  M01_Common.DdlOutputMode.edomListNoLrt) != M01_Common.DdlOutputMode.edomListNoLrt)) {
if (M03_Config.hasBeenSetProductiveInPrivLrt) {
if (isUserTransactional &  M01_Globals.g_genLrtSupport) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, (!(M03_Config.hasBeenSetProductiveInPrivLrt &  (forLrt |  forLrtMqt)) ? "" : "NOT NULL DEFAULT " + (useAlternativeDefaults ? 1 : 0)), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record has been set productive", "0", null, null, null, null), null, null);
}
}
// ### ELSE IVK ###
//       printSectionHeader "ObjectId of 'aggregate head'", fileNo, outputMode
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), _
//           , ddlType, , outputMode, eacFkOid Or eacLrtMeta Or eacAhOid , , indent, , _
//           "[MET] Object ID of the 'Aggregate Head'", , Not generateAhIdsNotNull Or useAlternativeDefaults _
//         )
// ### ENDIF IVK ###
// ### IF IVK ###
if (forLrt &  (outputMode &  M01_Common.DdlOutputMode.edomValueNonLrt)) {
if (!(M03_Config.hasBeenSetProductiveInPrivLrt)) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt |  (outputMode &  M01_Common.DdlOutputMode.edomDefaultValue), M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, M01_LDM.gc_dbFalse, null, true, null, null), null, null);
}
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Flag 'isDeleted'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsDeleted, M01_ACM_IVK.conIsDeleted, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt |  (outputMode &  M01_Common.DdlOutputMode.edomDefaultValue), M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, M01_LDM.gc_dbFalse, null, true, null, null), null, null);
}
} else if ((!(forLrt |  (outputMode &  (M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDeclNonLrt))))) {
if (!(M03_Config.hasBeenSetProductiveInPrivLrt)) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt & ! forLrtMqt ? "" : "NOT NULL DEFAULT " + (useAlternativeDefaults ? 1 : 0)), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record has been set productive", "0", null, null, null, null), null, null);
}
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Flag 'IsDeleted'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsDeleted, M01_ACM_IVK.cosnIsDeleted, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt & ! forLrtMqt ? "" : "NOT NULL DEFAULT 0"), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record logically has been deleted", "0", null, null, null, null), null, null);
}
}

// ### ENDIF IVK ###
// columns which exist in private and not in public tables
if (!(forLrt &  (outputMode &  M01_Common.DdlOutputMode.edomValueLrt))) {
M22_Class_Utilities.printSectionHeader("LRT - Status (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])", fileNo, M01_Common.DdlOutputMode.edomValueLrt, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLrtState, M01_ACM.cosnLrtState, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (!(forLrtMqt) ? "NOT NULL" : ""), null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, forLrtMqt, null, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
//             transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
//             edomValueLrt, eacLrtMeta, , indent, , , , forLrtMqt _
//           )
// ### ENDIF IVK ###
} else if (forLrt |  (outputMode &  (M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDeclLrt))) {
M22_Class_Utilities.printSectionHeader("LRT - Status (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLrtState, M01_ACM.cosnLrtState, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (!(forLrtMqt) ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Record status with respect to its involvement in an LRT (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])", null, null, forLrtMqt, null, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
//             transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , outputMode, eacLrtMeta, , indent, , _
//             "[LRT] Record status with respect to its involvement in an LRT (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", , forLrtMqt _
//           )
// ### ENDIF IVK ###
}
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].notAcmRelated &  ((enforceClassId &  !(entityIdStr.compareTo("") == 0) & !M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable) |  M22_Class.g_classes.descriptors[acmEntityIndex].hasSubClass))) {
M22_Class_Utilities.printSectionHeader("Class ID", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conClassId, M01_ACM.cosnClassId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexCid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacCid, null, indent, null, "[LDM] Class Identifier", null, null, null, null, null), null, null);
}
}
}
}

boolean printedHeader;
printedHeader = false;

boolean attrIsReUsed;
for (int i = 1; i <= attrRefs.numDescriptors; i++) {
attrIsReUsed = false;
// ### IF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomXsd) |  (outputMode &  M01_Common.DdlOutputMode.edomXml)) &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].noXmlExport) {
goto NextI;
}
if (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isExpression) {
if (outputMode &  M01_Common.DdlOutputMode.edomXsd) {
} else if (outputMode &  M01_Common.DdlOutputMode.edomXml) {
} else if (!(instantiateExpressions &  outputMode != M01_Common.DdlOutputMode.edomNone)) {
if ((outputMode &  (M01_Common.DdlOutputMode.edomMqtLrt |  M01_Common.DdlOutputMode.edomExpression | M01_Common.DdlOutputMode.edomExpressionDummy)) == 0) {
goto NextI;
} else if ((outputMode &  M01_Common.DdlOutputMode.edomMqtLrt) & ! M03_Config.includeTermStringsInMqt) {
goto NextI;
}
}
}

// ### ENDIF IVK ###
if (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].sectionName.toUpperCase() == entitySectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].className.toUpperCase() == entityName.toUpperCase() & (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].cType.compareTo(acmEntityType) == 0) & (classIsGenForming ? forGen == M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying : true)) {
boolean isNullable;
String defaultValue;
isNullable = M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNullable;
defaultValue = M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].defaultValue;

if (!(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNullableInOrgs.compareTo("") == 0) &  thisOrgIndex > 0) {
if (M04_Utilities.includedInList(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNullableInOrgs, M71_Org.g_orgs.descriptors[thisOrgIndex].id)) {
isNullable = true;
defaultValue = "";
}
}

attrSpecifics = ((M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNl |  isNullable | forSubClass) & ! (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainName.compareTo(M01_ACM.dnBoolean) == 0 &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainSection.compareTo(M01_ACM.dxnBoolean) == 0) ? "" : (suppressColConstraints & ! M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isIdentifying ? "" : "NOT NULL"));
attrSpecifics = attrSpecifics + (defaultValue.trim() == "" ? "" : (attrSpecifics.compareTo("") == 0 ? "" : " ") + "DEFAULT " + defaultValue + (ddlType == M01_Common.DdlTypeId.edtPdm &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].compressDefault ? " COMPRESS SYSTEM DEFAULT" : ""));
String attrNameSuffix;
attrNameSuffix = "";
if (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNl) {
transformation.containsNlAttribute = true;
attrNameSuffix = M01_Common.langDfltSuffix;
if (transformation.doCollectAttrDescriptors) {
M24_Attribute_Utilities.addAttrDescriptorRef(transformation.nlAttrRefs, attrRefs.descriptors[i].refIndex, null);
if (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying) {
transformation.numNlAttrRefsTv = transformation.numNlAttrRefsTv + 1;
} else {
transformation.numNlAttrRefsNonTv = transformation.numNlAttrRefsNonTv + 1;
}
}
if (transformation.doCollectDomainDescriptors) {
M25_Domain_Utilities.addDomainDescriptorRef(transformation.domainRefs, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainIndex, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNullable, transformation.distinguishNullabilityForDomainRefs);
}
}
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass & ! printedHeader & !forSubClass) {
M22_Class_Utilities.printSectionHeader("attributes for \"" + entitySectionName + "." + entityName.toUpperCase() + "\"" + (!(entityIdStr.compareTo("") == 0) ? " (ClassId='" + entityIdStr + "')" : ""), fileNo, outputMode, null);
printedHeader = true;
}

if (!(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNl)) {
String attrComment;
if ((outputMode &  M01_Common.DdlOutputMode.edomComment != 0) &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attrNlIndex > 0) {
if (M24_Attribute_NL.g_attributesNl.descriptors[M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attrNlIndex].nl[M01_Globals_IVK.gc_langIdEnglish] != "") {
attrComment = " (" + M24_Attribute_NL.g_attributesNl.descriptors[M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attrNlIndex].nl[M01_Globals_IVK.gc_langIdEnglish] + ")";
}
}

M22_Class_Utilities.printComment("\"" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + "\" (" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainSection + "." + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainName + ")", fileNo, outputMode, null);
// pass default value to 'genTransformedAttrDeclByDomainWithColReUse' to support outputmode 'edomDefaultValue'
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + attrNameSuffix, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].shortName + attrNameSuffix, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, (!(suppressTrailingComma)) |  (i != attrRefs.numDescriptors), ddlType, null, outputMode, (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isExpression ? M01_Common.AttrCategory.eacExpression : M01_Common.AttrCategory.eacRegular), null, indent, attrIsReUsed, "[ACM] Attribute '" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + "'" + attrComment, defaultValue, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isVirtual, isNullable |  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNationalizable, attrRefs.descriptors[i].refIndex, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, '
//           genTransformedAttrDeclByDomainWithColReUse( _
//             .attributeName & attrNameSuffix, .shortName & attrNameSuffix, .valueType, .valueTypeIndex, _
//             transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, (Not suppressTrailingComma) Or (i <> attrRefs.numDescriptors), _
//             ddlType, , outputMode, eacRegular, , indent, attrIsReUsed, _
//             "[ACM] Attribute '" & .attributeName & "'" & attrComment, default, isNullable, attrRefs.descriptors(i).refIndex _
//           )
// ### ENDIF IVK ###
}
// ### IF IVK ###

if (!(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNl &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNationalizable & !attrIsReUsed)) {
M22_Class_Utilities.printComment("nationalized attribute \"" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + "\" (" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainSection + "." + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].domainName + ")", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + attrNameSuffix + M01_Globals_IVK.gc_anSuffixNat, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].shortName + attrNameSuffix + M01_Globals_IVK.gc_asnSuffixNat, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, null, ddlType, null, outputMode, (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isExpression ? M01_Common.AttrCategory.eacExpression : M01_Common.AttrCategory.eacRegular) |  M01_Common.AttrCategory.eacNational, null, indent, attrIsReUsed, "[ACM] Attribute '" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + "' (nationalized)", M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].defaultValue, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isVirtual, true, attrRefs.descriptors[i].refIndex, null), null, null);
M22_Class_Utilities.printSectionHeader("Is the nationalized attribute active?", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + attrNameSuffix + M01_Globals_IVK.gc_anSuffixNatActivated, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].shortName + attrNameSuffix + M01_Globals_IVK.gc_asnSuffixNatActivated, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), null, ddlType, null, outputMode, (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isExpression ? M01_Common.AttrCategory.eacExpression : M01_Common.AttrCategory.eacRegular) |  M01_Common.AttrCategory.eacNationalBool, null, indent, attrIsReUsed, "[ACM] Indicates whether nationalized attribute '" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName + "' is active", M01_LDM.gc_dbFalse, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isVirtual, false, attrRefs.descriptors[i].refIndex, null), null, null);
}
// ### ENDIF IVK ###
}
NextI:
}

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedAttrDeclsForEntity(Integer acmEntityType, int acmEntityIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Boolean forSubClassW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean useSurrogateKeyW, Boolean classIsGenFormingW, Boolean forGenW, Boolean suppressOidW, Boolean isUserTransactionalW, Boolean suppressTrailingCommaW, Boolean forLrtW, Integer outputModeW, Integer indentW, Boolean suppressLrtStatusW, String genParentTabNameW, Boolean suppressColConstraintsW) {
boolean forSubClass; 
if (forSubClassW == null) {
forSubClass = false;
} else {
forSubClass = forSubClassW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean useSurrogateKey; 
if (useSurrogateKeyW == null) {
useSurrogateKey = true;
} else {
useSurrogateKey = useSurrogateKeyW;
}

boolean classIsGenForming; 
if (classIsGenFormingW == null) {
classIsGenForming = false;
} else {
classIsGenForming = classIsGenFormingW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean suppressOid; 
if (suppressOidW == null) {
suppressOid = false;
} else {
suppressOid = suppressOidW;
}

boolean isUserTransactional; 
if (isUserTransactionalW == null) {
isUserTransactional = false;
} else {
isUserTransactional = isUserTransactionalW;
}

boolean suppressTrailingComma; 
if (suppressTrailingCommaW == null) {
suppressTrailingComma = false;
} else {
suppressTrailingComma = suppressTrailingCommaW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean suppressLrtStatus; 
if (suppressLrtStatusW == null) {
suppressLrtStatus = false;
} else {
suppressLrtStatus = suppressLrtStatusW;
}

String genParentTabName; 
if (genParentTabNameW == null) {
genParentTabName = "";
} else {
genParentTabName = genParentTabNameW;
}

boolean suppressColConstraints; 
if (suppressColConstraintsW == null) {
suppressColConstraints = false;
} else {
suppressColConstraints = suppressColConstraintsW;
}

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M24_Attribute.genTransformedAttrDeclsForEntityWithColReUse(acmEntityType, acmEntityIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, useSurrogateKey, classIsGenForming, forGen, suppressOid, null, isUserTransactional, suppressTrailingComma, forLrt, outputMode, indent, suppressLrtStatus, genParentTabName, suppressColConstraints, null, null);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genAttrDeclsForEntity(Integer acmEntityType, int acmEntityIndex, Boolean forSubClassW, Integer fileNoW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean useSurrogateKeyW, Boolean classIsGenFormingW, Boolean forGenW, Boolean suppressOidW, Boolean classIsTransactionalW, Boolean suppressTrailingCommaW, Boolean forLrtW, Integer outputModeW, Integer indentW, Boolean suppressLrtStatusW, String genParentTabNameW, Boolean suppressColConstraintsW) {
boolean forSubClass; 
if (forSubClassW == null) {
forSubClass = false;
} else {
forSubClass = forSubClassW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean useSurrogateKey; 
if (useSurrogateKeyW == null) {
useSurrogateKey = true;
} else {
useSurrogateKey = useSurrogateKeyW;
}

boolean classIsGenForming; 
if (classIsGenFormingW == null) {
classIsGenForming = false;
} else {
classIsGenForming = classIsGenFormingW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean suppressOid; 
if (suppressOidW == null) {
suppressOid = false;
} else {
suppressOid = suppressOidW;
}

boolean classIsTransactional; 
if (classIsTransactionalW == null) {
classIsTransactional = false;
} else {
classIsTransactional = classIsTransactionalW;
}

boolean suppressTrailingComma; 
if (suppressTrailingCommaW == null) {
suppressTrailingComma = false;
} else {
suppressTrailingComma = suppressTrailingCommaW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean suppressLrtStatus; 
if (suppressLrtStatusW == null) {
suppressLrtStatus = false;
} else {
suppressLrtStatus = suppressLrtStatusW;
}

String genParentTabName; 
if (genParentTabNameW == null) {
genParentTabName = "";
} else {
genParentTabName = genParentTabNameW;
}

boolean suppressColConstraints; 
if (suppressColConstraintsW == null) {
suppressColConstraints = false;
} else {
suppressColConstraints = suppressColConstraintsW;
}

//On Error GoTo ErrorExit 

M24_Attribute.genTransformedAttrDeclsForEntity(acmEntityType, acmEntityIndex, M24_Attribute_Utilities.nullAttributeTransformation, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, useSurrogateKey, classIsGenForming, forGen, suppressOid, classIsTransactional, suppressTrailingComma, forLrt, outputMode, indent, suppressLrtStatus, genParentTabName, suppressColConstraints);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static String genFkTransformedAttrDeclsWithColReuse( int acmClassIndex, String attrSpecifics, boolean isPsForming, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, String refClassNameW, String refClassShortNameW, Integer fileNoW, Integer ddlTypeW, Boolean addCommaW, Boolean nationalizedW, Boolean returnDeclsW, String attrDeclsW, Integer outputModeW, Integer indentW, Boolean isOptionalW) {
String refClassName; 
if (refClassNameW == null) {
refClassName = "";
} else {
refClassName = refClassNameW;
}

String refClassShortName; 
if (refClassShortNameW == null) {
refClassShortName = "";
} else {
refClassShortName = refClassShortNameW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

boolean nationalized; 
if (nationalizedW == null) {
nationalized = false;
} else {
nationalized = nationalizedW;
}

boolean returnDecls; 
if (returnDeclsW == null) {
returnDecls = false;
} else {
returnDecls = returnDeclsW;
}

String attrDecls; 
if (attrDeclsW == null) {
attrDecls = "";
} else {
attrDecls = attrDeclsW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

String returnValue;
returnValue = "";

//On Error GoTo ErrorExit 

String attrList;
String decl;
attrList = "";
String sectionName;
String clasName;
if (M22_Class.g_classes.descriptors[acmClassIndex].useSurrogateKey) {
// ### IF IVK ###
attrList = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? refClassShortName : ""), null, null, null, nationalized);

M22_Class_Utilities.printComment("\"" + M22_Class.g_classes.descriptors[acmClassIndex].className + (M22_Class.g_classes.descriptors[acmClassIndex].className.compareTo("") == 0 ? "" : ":") + M01_ACM.conOid + "\" (" + M01_ACM.dxnOid + "." + M01_ACM.dnOid + ")", fileNo, outputMode, null);
decl = M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? refClassShortName : ""), null, null, null, nationalized), M04_Utilities.genSurrogateKeyShortName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? refClassShortName : "") + (nationalized ? "_" + M01_Globals_IVK.gc_asnSuffixNat : ""), null), M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, acmClassIndex, attrSpecifics, addComma, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  (isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (M22_Class.g_classes.descriptors[acmClassIndex].supportExtendedPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0) | (nationalized ? M01_Common.AttrCategory.eacNational : 0), null, indent, null, "[LDM] Foreign Key to ACM-class '" + M22_Class.g_classes.descriptors[acmClassIndex].className + "'", null, null, isOptional, null, null);
// ### ELSE IVK ###
//      attrList = genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, ""))
//
//     printComment """" & .className & IIf(.className = "", "", ":") & conOid & """ (" & dxnOID & "." & dnOID & ")", fileNo, outputMode
//      decl = genTransformedAttrDeclByDomainWithColReUse(genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, "")), _
//             genSurrogateKeyShortName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, "")), _
//             eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, acmClassIndex, attrSpecifics, addComma, ddlType, , outputMode, _
//             eacFkOid, , indent, , "[LDM] Foreign Key to ACM-class '" & .className & "'", , isOptional)
// ### ENDIF IVK ###
if (returnDecls) {
attrDecls = attrDecls + (attrDecls == "" ? "" : vbCrLf) + decl;
}
M04_Utilities.printConditional(fileNo, decl, null, null);
} else {
int i;
int numAttrs;
numAttrs = 0;
for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[i].sectionName.toUpperCase() == M22_Class.g_classes.descriptors[acmClassIndex].sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[i].className.toUpperCase() == M22_Class.g_classes.descriptors[acmClassIndex].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[i].isIdentifying) {
numAttrs = numAttrs + 1;
}
}

for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[i].sectionName.toUpperCase() == M22_Class.g_classes.descriptors[acmClassIndex].sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[i].className.toUpperCase() == M22_Class.g_classes.descriptors[acmClassIndex].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[i].isIdentifying) {
attrList = (attrList.compareTo("") == 0 ? "" : ",") + M24_Attribute.g_attributes.descriptors[i].attributeName;

M22_Class_Utilities.printComment("\"" + M24_Attribute.g_attributes.descriptors[i].className + (!(M24_Attribute.g_attributes.descriptors[i].className.compareTo("") == 0) ? ":" : "") + M24_Attribute.g_attributes.descriptors[i].attributeName + "\" (" + M24_Attribute.g_attributes.descriptors[i].domainSection + "." + M24_Attribute.g_attributes.descriptors[i].domainName + ")", fileNo, outputMode, null);
// ### IF IVK ###
decl = M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[i].attributeName, M24_Attribute.g_attributes.descriptors[i].shortName, M24_Attribute.g_attributes.descriptors[i].valueType, M24_Attribute.g_attributes.descriptors[i].valueTypeIndex, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, acmClassIndex, attrSpecifics, addComma |  (i.compareTo(numAttrs) < 0), ddlType, null, outputMode, null, null, indent, null, null, null, null, isOptional, null, null);
// ### ELSE IVK ###
//           decl = genTransformedAttrDeclByDomainWithColReUse(.attributeName, .shortName, .valueType, .valueTypeIndex, transformation, tabColumns, _
//                  eactClass, acmClassIndex, attrSpecifics, addComma Or (i < numAttrs), ddlType, , outputMode, , , indent, , , , isOptional)
// ### ENDIF IVK ###
M04_Utilities.printConditional(fileNo, decl, null, null);
if (returnDecls) {
attrDecls = attrDecls + (attrDecls == "" ? "" : vbCrLf) + decl;
}
}
}
}

returnValue = attrList;

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


public static String genFkTransformedAttrDecls( int acmClassIndex, String attrSpecifics, boolean isPsForming, M24_Attribute_Utilities.AttributeListTransformation transformation, String refClassNameW, String refClassShortNameW, Integer fileNoW, Integer ddlTypeW, Boolean addCommaW, Boolean nationalizedW, Boolean returnDeclsW, String attrDeclsW, Integer outputModeW, Integer indentW, Boolean isOptionalW) {
String refClassName; 
if (refClassNameW == null) {
refClassName = "";
} else {
refClassName = refClassNameW;
}

String refClassShortName; 
if (refClassShortNameW == null) {
refClassShortName = "";
} else {
refClassShortName = refClassShortNameW;
}

int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

boolean nationalized; 
if (nationalizedW == null) {
nationalized = false;
} else {
nationalized = nationalizedW;
}

boolean returnDecls; 
if (returnDeclsW == null) {
returnDecls = false;
} else {
returnDecls = returnDeclsW;
}

String attrDecls; 
if (attrDeclsW == null) {
attrDecls = "";
} else {
attrDecls = attrDeclsW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

String returnValue;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

// ### IF IVK ###
returnValue = M24_Attribute.genFkTransformedAttrDeclsWithColReuse(acmClassIndex, attrSpecifics, isPsForming, transformation, tabColumns, refClassName, refClassShortName, fileNo, ddlType, addComma, nationalized, returnDecls, attrDecls, outputMode, indent, isOptional);
// ### ELSE IVK ###
//  genFkTransformedAttrDecls = genFkTransformedAttrDeclsWithColReuse(acmClassIndex, attrSpecifics, False, transformation, tabColumns, refClassName, refClassShortName, _
//                                  fileNo, ddlType, addComma, nationalized, returnDecls, attrDecls, outputMode, indent, isOptional)
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


// ### IF IVK ###
public static void genFkTransformedAttrDeclsForRelationshipWithColReUse(int targetClassIndex, int acmRelIndex, String relationshipNameShort, boolean concatRelNameShort, boolean relationshipIsNationalizable, String attrSpecifics, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer fileNoW, Integer ddlTypeW, Integer outputModeW, Integer indentW, Boolean addCommaW) {
int fileNo; 
if (fileNoW == null) {
fileNo = 1;
} else {
fileNo = fileNoW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

// ### ELSE IVK ###
//Sub genFkTransformedAttrDeclsForRelationshipWithColReUse( _
// targetClassIndex As Integer, _
// ByRef acmRelIndex As Integer, _
// ByRef relationshipNameShort As String, _
// ByRef concatRelNameShort As Boolean, _
// ByRef attrSpecifics As String, _
// ByRef transformation As AttributeListTransformation, _
// ByRef tabColumns As EntityColumnDescriptors, _
//  Optional fileNo As Integer = 1, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
// Optional indent As Integer = 1, _
// Optional addComma As Boolean = True _
//)
// ### ENDIF IVK ###
//On Error GoTo ErrorExit 

boolean isOptional;
isOptional = !(M00_Helper.inStr(1, attrSpecifics.toUpperCase(), "NOT NULL"));

Integer attrCat;
attrCat = M01_Common.AttrCategory.eacFkOid;

String relName;
if (acmRelIndex > 0) {
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[acmRelIndex].isMdsExpressionRel) {
attrCat = attrCat |  M01_Common.AttrCategory.eacFkOidExpression;
}

// ### ENDIF IVK ###
relName = M23_Relationship.g_relationships.descriptors[acmRelIndex].relName;
}

// ### IF IVK ###
if (M22_Class.g_classes.descriptors[targetClassIndex].classIndex == M01_Globals_IVK.g_classIndexCountryIdList) {
attrCat = attrCat |  M01_Common.AttrCategory.eacFkCountryIdList;
}

// ### ENDIF IVK ###
if (M22_Class.g_classes.descriptors[targetClassIndex].useSurrogateKey) {
// ### IF IVK ###
attrCat = attrCat |  (M22_Class.g_classes.descriptors[targetClassIndex].supportExtendedPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0) | (M22_Class.g_classes.descriptors[targetClassIndex].isSubjectToExpCopy ? M01_Common.AttrCategory.eacFkOidExpElement : 0);

// ### ENDIF IVK ###
M22_Class_Utilities.printComment("\"" + relationshipNameShort + (relationshipNameShort.compareTo("") == 0 ? "" : ":") + M01_ACM.conOid + "\" (" + M01_ACM.dxnOid + "." + M01_ACM.dnOid + ")", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[targetClassIndex].classIndex, attrSpecifics, addComma |  relationshipIsNationalizable, ddlType, relationshipNameShort, outputMode, attrCat |  (M22_Class.g_classes.descriptors[targetClassIndex].isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0), acmRelIndex, indent, null, "[LDM] Foreign Key corresponding to ACM-relationship '" + relName + "' :-> '" + M22_Class.g_classes.descriptors[targetClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[targetClassIndex].className + "'", null, null, isOptional, null, null), null, null);
if (relationshipIsNationalizable) {
M22_Class_Utilities.printSectionHeader("nationalized Relationship", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid + M01_Globals_IVK.gc_anSuffixNat, M01_ACM.cosnOid + M01_Globals_IVK.gc_anSuffixNat, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[targetClassIndex].classIndex, attrSpecifics, null, ddlType, relationshipNameShort, outputMode, attrCat |  (M22_Class.g_classes.descriptors[targetClassIndex].isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | M01_Common.AttrCategory.eacNational, acmRelIndex, indent, null, "[LDM] Foreign Key corresponding to ACM-relationship (national) '" + relName + "' :-> '" + M22_Class.g_classes.descriptors[targetClassIndex].sectionName + "." + M22_Class.g_classes.descriptors[targetClassIndex].className + "'", null, null, isOptional, null, null), null, null);
M22_Class_Utilities.printSectionHeader("Is nationalized Relationship active?", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid + M01_Globals_IVK.gc_anSuffixNatActivated, M01_ACM.cosnOid + "_" + M01_Globals_IVK.gc_asnSuffixNatActivated, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[targetClassIndex].classIndex, "NOT NULL DEFAULT 0" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), addComma, ddlType, relationshipNameShort, outputMode, (attrCat |  M01_Common.AttrCategory.eacRegular | M01_Common.AttrCategory.eacNationalBool) & ! (M01_Common.AttrCategory.eacFkOid |  M01_Common.AttrCategory.eacFkExtPsCopyOid), acmRelIndex, indent, null, "[LDM] Is nationalized Relationship active?", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
// ### ELSE IVK ###
//     printConditional fileNo, _
//       genTransformedAttrDeclByDomainWithColReUse( _
//         conOid, cosnOid, _
//         eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, .classIndex, attrSpecifics, addComma , ddlType, _
//         relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, , _
//         "[LDM] Foreign Key corresponding to ACM-relationship '" & relName & "' :-> '" & .sectionName & "." & .className & "'", _
//         , isOptional _
//       )
// ### ENDIF IVK ###
} else {
int i;
int thisClassIndex;
thisClassIndex = M22_Class.g_classes.descriptors[targetClassIndex].classIndex;
while (thisClassIndex > 0) {
for (i = 1; i <= 1; i += (1)) {
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].isIdentifying) {
M22_Class_Utilities.printComment("\"" + relationshipNameShort + (relationshipNameShort.compareTo("") == 0 ? "" : ":") + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName + "\" (" + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].domainSection + "." + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].domainName + ")", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].shortName, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisClassIndex, attrSpecifics, null, ddlType, relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, null, null, null, null, isOptional, null, null), null, null);
if (relationshipIsNationalizable) {
M22_Class_Utilities.printSectionHeader("nationalized Relationship", fileNo, null, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName + M01_Globals_IVK.gc_anSuffixNat, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].shortName + M01_Globals_IVK.gc_anSuffixNat, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisClassIndex, M00_Helper.replace(attrSpecifics, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, acmRelIndex, "NOT NULL", ""), null, ddlType, relationshipNameShort, outputMode, M01_Common.AttrCategory.eacFkOid |  M01_Common.AttrCategory.eacNational, acmRelIndex, indent, null, null, null, null, true, null, null), null, null);
}
// ### ELSE IVK ###
//               printConditional fileNo, _
//                 genTransformedAttrDeclByDomainWithColReUse( _
//                   .attributeName, .shortName, .valueType, .valueTypeIndex, _
//                   transformation, tabColumns, eactClass, thisClassIndex, attrSpecifics, , ddlType, _
//                   relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, , , , isOptional _
//                 )
// ### ENDIF IVK ###
}
}

if (M03_Config.includeFksInPks) {
int j;
for (int j = 1; j <= M22_Class.g_classes.descriptors[thisClassIndex].relRefs.numRefs; j++) {
if (M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refType == M01_Common.RelNavigationDirection.etRight) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].isIdentifyingLeft &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].maxLeftCardinality == 1) {
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].leftEntityIndex, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].relIndex, (concatRelNameShort ? relationshipNameShort + "_" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].rlShortRelName : relationshipNameShort), concatRelNameShort, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, null);
// ### ELSE IVK ###
//                   genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
//                       .relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & .shortName & .rlShortRelName, relationshipNameShort), _
//                       concatRelNameShort, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
// ### ENDIF IVK ###
}
} else if (M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refType == M01_Common.RelNavigationDirection.etLeft) {
if (M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].isIdentifyingRight &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].maxRightCardinality == 1) {
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].rightEntityIndex, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].relIndex, (concatRelNameShort ? relationshipNameShort + "_" + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].shortName + M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].lrShortRelName : relationshipNameShort), concatRelNameShort, M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].relRefs.refs[j].refIndex].isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, null);
// ### ELSE IVK ###
//                   genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
//                       .relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & .shortName & .lrShortRelName, relationshipNameShort), _
//                       concatRelNameShort, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
// ### ENDIF IVK ###
}
}
}
}
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
// ### IF IVK ###
if (relationshipIsNationalizable) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid + M01_Globals_IVK.gc_anSuffixNatActivated, M01_ACM.cosnOid + "_" + M01_Globals_IVK.gc_asnSuffixNatActivated, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M22_Class.g_classes.descriptors[targetClassIndex].classIndex, "NOT NULL", null, ddlType, relationshipNameShort, outputMode, M01_Common.AttrCategory.eacRegular |  M01_Common.AttrCategory.eacNationalBool, acmRelIndex, indent, null, null, M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genTransformedLogChangeAttrDeclsWithColReUse(int fileNo, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer acmEntityTypeW, Integer acmEntityIndexW, Integer ddlTypeW, String classNameW, Integer outputModeW, Integer indentW, Boolean addCommaW, Boolean useAlternativeDefaultsW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String className; 
if (classNameW == null) {
className = "";
} else {
className = classNameW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

//On Error GoTo ErrorExit 

M22_Class_Utilities.printSectionHeader("Last Change Log", fileNo, outputMode, null);
// ### IF IVK ###
if (outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "<component name=\"changeLog\" class=\"com.dcx.ivkmds.common.bo.persistent.ChangeLog\">");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "<component name=\"creator\" class=\"com.dcx.ivkmds.common.bo.persistent.Creator\">");
M00_FileWriter.printToFile(fileNo, "");
indent = indent + 2;
}

if (useAlternativeDefaults) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conCreateUser, M01_ACM.cosnCreateUser, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexUserIdAlt, transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", null, ddlType, null, outputMode, null, null, indent, null, "[ACM] CD Id of user who created the record", null, null, true, null, null), null, null);
} else {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conCreateUser, M01_ACM.cosnCreateUser, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexUserId, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, null, null, indent, null, "[ACM] CD Id of user who created the record", null, null, true, null, null), null, null);
}
// ### ELSE IVK ###
// If useAlternativeDefaults Then
//   printConditional fileNo, _
//     genTransformedAttrDeclByDomainWithColReUse( _
//       conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserIdAlt, _
//       transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
//       "[ACM] CD Id of user who created the record", , True _
//     )
// Else
//   printConditional fileNo, _
//     genTransformedAttrDeclByDomainWithColReUse( _
//       conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserId, _
//       transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
//       "[ACM] CD Id of user who created the record", , True _
//     )
// End If
// ### ENDIF IVK ###
// ### IF IVK ###

if (((outputMode &  M01_Common.DdlOutputMode.edomListVirtual) != 0 &  (outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) == 0 & (outputMode &  M01_Common.DdlOutputMode.edomValueVirtualNonPersisted) == 0) |  (outputMode &  M01_Common.DdlOutputMode.edomDeclVirtual)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conCreateUserName, M01_ACM.cosnCreateUserName, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexUserName, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, null, null, indent, null, "[ACM] User Name of user who created the record", null, true, true, null, false), null, null);
} else if ((outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) != 0 |  (outputMode &  M01_Common.DdlOutputMode.edomValueVirtualNonPersisted) != 0) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conCreateUserName, M01_ACM.cosnCreateUserName, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexUserName, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValue |  M01_Common.DdlOutputMode.edomDefaultValue, null, null, indent, null, "[ACM] User Name of user who created the record", M04_Utilities.genGetUserNameByIdDdl(transformation.attributePrefix + M01_Globals.g_anCreateUser, ddlType, null, null), true, true, null, false), null, null);
}
// ### ENDIF IVK ###

M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_Globals.g_anCreateTimestamp, M01_ACM.cosnCreateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexModTimestamp, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT CURRENT TIMESTAMP", null, ddlType, null, outputMode, null, null, indent, null, "[ACM] Timestamp when the record was created", "CURRENT TIMESTAMP", null, null, null, null), null, null);

// ### IF IVK ###
if (outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent - 1) + "</component>");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent - 1) + "<component name=\"lastModifier\" class=\"com.dcx.ivkmds.common.bo.persistent.LastModifier\">");
}

if (useAlternativeDefaults) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conUpdateUser, M01_ACM.cosnUpdateUser, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexUserIdAlt, transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", null, ddlType, null, outputMode, null, null, indent, null, "[ACM] CD Id of user who last modified the record", null, null, true, null, null), null, null);
} else {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conUpdateUser, M01_ACM.cosnUpdateUser, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexUserId, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, null, null, indent, null, "[ACM] CD Id of user who last modified the record", null, null, true, null, null), null, null);
}
// ### ELSE IVK ###
// If useAlternativeDefaults Then
//   printConditional fileNo, _
//     genTransformedAttrDeclByDomainWithColReUse( _
//       conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserIdAlt, _
//       transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
//       "[ACM] CD Id of user who last modified the record", , True _
//     )
// Else
//   printConditional fileNo, _
//     genTransformedAttrDeclByDomainWithColReUse( _
//       conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserId, _
//       transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
//       "[ACM] CD Id of user who last modified the record", , True _
//     )
// End If
// ### ENDIF IVK ###
// ### IF IVK ###

if (((outputMode &  M01_Common.DdlOutputMode.edomListVirtual) != 0 &  (outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) == 0 & (outputMode &  M01_Common.DdlOutputMode.edomValueVirtualNonPersisted) == 0) |  (outputMode &  M01_Common.DdlOutputMode.edomDeclVirtual)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conUpdateUserName, M01_ACM.cosnUpdateUserName, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexUserName, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, null, null, indent, null, "[ACM] User Name of user who last modified the record", null, true, true, null, false), null, null);
} else if ((outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) != 0 |  (outputMode &  M01_Common.DdlOutputMode.edomValueVirtualNonPersisted) != 0) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conUpdateUserName, M01_ACM.cosnUpdateUserName, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexUserName, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValue |  M01_Common.DdlOutputMode.edomDefaultValue, null, null, indent, null, "[ACM] User Name of user who last modified the record", M04_Utilities.genGetUserNameByIdDdl(transformation.attributePrefix + M01_Globals.g_anUpdateUser, ddlType, null, null), true, true, null, false), null, null);
}
// ### ENDIF IVK ###

M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLastUpdateTimestamp, M01_ACM.cosnLastUpdateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexModTimestamp, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT CURRENT TIMESTAMP", addComma, ddlType, null, outputMode, null, null, indent, null, "[ACM] Timestamp when the record was last modified", "CURRENT TIMESTAMP", null, null, null, null), null, null);
// ### IF IVK ###

if (outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent - 1) + "</component>");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent - 2) + "</component>");
M00_FileWriter.printToFile(fileNo, "");
indent = indent + 2;
}
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}



public static String getFkSrcAttrSeq(int thisClassIndex, String relShortName, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = "";

//On Error GoTo ErrorExit 

if (M22_Class.g_classes.descriptors[thisClassIndex].useSurrogateKey) {
returnValue = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[thisClassIndex].shortName, (M03_Config.reuseColumnsInTabsForOrMapping ? relShortName : ""), null, null, null);
} else {
String attrSeq;
attrSeq = "";
int i;
for (i = 1; i <= 1; i += (1)) {
attrSeq = attrSeq + (attrSeq + "" == "" ? "" : ",") + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName, ddlType, M22_Class.g_classes.descriptors[thisClassIndex].shortName, (M03_Config.reuseColumnsInTabsForOrMapping ? relShortName : ""), null, null, null, null);
}

returnValue = attrSeq;
}

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}

public static String getFkSrcAttrSeqExt(int thisClassIndex, String relShortName,  Integer thisPoolIndexW, Integer ddlTypeW, String strKeyW, Boolean refIsPsTaggedW, Boolean relUseNumMapsW, Boolean relUseMqtToImplementLrtW, Boolean dstRefToNlW) {
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

String strKey; 
if (strKeyW == null) {
strKey = "";
} else {
strKey = strKeyW;
}

boolean refIsPsTagged; 
if (refIsPsTaggedW == null) {
refIsPsTagged = true;
} else {
refIsPsTagged = refIsPsTaggedW;
}

boolean relUseNumMaps; 
if (relUseNumMapsW == null) {
relUseNumMaps = true;
} else {
relUseNumMaps = relUseNumMapsW;
}

boolean relUseMqtToImplementLrt; 
if (relUseMqtToImplementLrtW == null) {
relUseMqtToImplementLrt = true;
} else {
relUseMqtToImplementLrt = relUseMqtToImplementLrtW;
}

boolean dstRefToNl; 
if (dstRefToNlW == null) {
dstRefToNl = false;
} else {
dstRefToNl = dstRefToNlW;
}

String returnValue;
returnValue = "";

//On Error GoTo ErrorExit 

boolean M72_DataPool.poolSupportLrt;
boolean noRangePartitioning;

if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

if (M22_Class.g_classes.descriptors[thisClassIndex].useSurrogateKey) {
returnValue = strKey;
noRangePartitioning = M22_Class.g_classes.descriptors[thisClassIndex].noRangePartitioning;
if (M22_Class.g_classes.descriptors[thisClassIndex].isPsTagged &  refIsPsTagged) {
if (!(noRangePartitioning &  M22_Class.g_classes.descriptors[thisClassIndex].isUserTransactional & M72_DataPool.poolSupportLrt & !M22_Class.g_classes.descriptors[thisClassIndex].rangePartitioningAll)) {
if (M22_Class.g_classes.descriptors[thisClassIndex].useMqtToImplementLrt &  relUseNumMaps & relUseMqtToImplementLrt) {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenMqt);
} else {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenNoMqt);
}
}
if (!(noRangePartitioning & ! M22_Class.g_classes.descriptors[thisClassIndex].psTagOptional)) {
returnValue = M24_Attribute.getFkSrcAttrSeqExt + ", PS_OID";
if (M22_Class.g_classes.descriptors[thisClassIndex].subClassIdStrSeparatePartition.numMaps > 0 & ! dstRefToNl) {
returnValue = M24_Attribute.getFkSrcAttrSeqExt + ", " + M01_Globals.g_anAhCid.toUpperCase();
}
}
}
} else {
String attrSeq;
attrSeq = "";
int i;
for (i = 1; i <= 1; i += (1)) {
attrSeq = attrSeq + (attrSeq + "" == "" ? "" : ",") + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName, ddlType, M22_Class.g_classes.descriptors[thisClassIndex].shortName, (M03_Config.reuseColumnsInTabsForOrMapping ? relShortName : ""), null, null, null, null);
}

returnValue = attrSeq;
}

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}

public static String getFkTargetAttrSeq(int thisClassIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = "";

if (M22_Class.g_classes.descriptors[thisClassIndex].useSurrogateKey) {
returnValue = M01_Globals.g_anOid;
} else {
String attrSeq;
attrSeq = "";
int i;
for (i = 1; i <= 1; i += (1)) {
attrSeq = attrSeq + (attrSeq + "" == "" ? "" : ",") + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName, ddlType, null, null, null, null, null, null);
}

returnValue = attrSeq;
}
return returnValue;
}

public static String getFkTargetAttrSeqExt(int thisClassIndex,  Integer thisPoolIndexW, Integer ddlTypeW,  String strKeyW,  String dstAggHeadClassIdStrW, Boolean relUseMqtToImplementLrtW, Boolean dstRefToNlW) {
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

String strKey; 
if (strKeyW == null) {
strKey = "OID";
} else {
strKey = strKeyW;
}

String dstAggHeadClassIdStr; 
if (dstAggHeadClassIdStrW == null) {
dstAggHeadClassIdStr = "";
} else {
dstAggHeadClassIdStr = dstAggHeadClassIdStrW;
}

boolean relUseMqtToImplementLrt; 
if (relUseMqtToImplementLrtW == null) {
relUseMqtToImplementLrt = true;
} else {
relUseMqtToImplementLrt = relUseMqtToImplementLrtW;
}

boolean dstRefToNl; 
if (dstRefToNlW == null) {
dstRefToNl = false;
} else {
dstRefToNl = dstRefToNlW;
}

String returnValue;
returnValue = "";

boolean M72_DataPool.poolSupportLrt;
boolean noRangePartitioning;

if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

if (M22_Class.g_classes.descriptors[thisClassIndex].useSurrogateKey) {
returnValue = strKey;
noRangePartitioning = M22_Class.g_classes.descriptors[thisClassIndex].noRangePartitioning;
if (M22_Class.g_classes.descriptors[thisClassIndex].isPsTagged) {
if (!(noRangePartitioning &  M22_Class.g_classes.descriptors[thisClassIndex].isUserTransactional & M72_DataPool.poolSupportLrt & !M22_Class.g_classes.descriptors[thisClassIndex].rangePartitioningAll)) {
if (M22_Class.g_classes.descriptors[thisClassIndex].useMqtToImplementLrt &  relUseMqtToImplementLrt) {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenMqt);
} else {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenNoMqt);
}
}

if (!(noRangePartitioning & ! M22_Class.g_classes.descriptors[thisClassIndex].psTagOptional & M22_Class.g_classes.descriptors[thisClassIndex].subClassIdStrSeparatePartition.numMaps <= 0)) {
returnValue = M24_Attribute.getFkTargetAttrSeqExt + ", PS_OID";
}
if (!(noRangePartitioning & ! M22_Class.g_classes.descriptors[thisClassIndex].psTagOptional & M22_Class.g_classes.descriptors[thisClassIndex].subClassIdStrSeparatePartition.numMaps > 0 & dstAggHeadClassIdStr != "" & dstRefToNl)) {
returnValue = M24_Attribute.getFkTargetAttrSeqExt + ", PS_OID";
}
if (!(noRangePartitioning & ! M22_Class.g_classes.descriptors[thisClassIndex].psTagOptional & M22_Class.g_classes.descriptors[thisClassIndex].subClassIdStrSeparatePartition.numMaps > 0 & dstAggHeadClassIdStr != "" & !dstRefToNl)) {
returnValue = M24_Attribute.getFkTargetAttrSeqExt + ", PS_OID" + ", " + M01_Globals.g_anCid.toUpperCase();
}
}
} else {
String attrSeq;
attrSeq = "";
int i;
for (i = 1; i <= 1; i += (1)) {
attrSeq = attrSeq + (attrSeq + "" == "" ? "" : ",") + M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[thisClassIndex].attrRefs.descriptors[i].refIndex].attributeName, ddlType, null, null, null, null, null, null);
}

returnValue = attrSeq;
}
return returnValue;
}

// ### IF IVK ###
private static void genFKForRelationshipByClassAndName(String qualTabName, int classIndex, int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Integer tabPartitionTypeW) {
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

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Private Sub genFKForRelationshipByClassAndName( _
// ByRef qualTabName As String, _
// ByRef classIndex As Integer, _
// thisRelIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False _
//)
// ### ENDIF IVK ###
M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;
M22_Class_Utilities.ClassDescriptor dstClass;
M22_Class_Utilities.ClassDescriptor srcClass;
String srcQualTabName;
String dstQualTabName;
String srcQualTabNameLdm;
String dstQualTabNameLdm;
String srcAttrSeq;
String dstAttrSeq;
// ### IF IVK ###
String srcAttrSeqNat;
// ### ENDIF IVK ###
String relSrc2DstShortName;
String relSrc2DstLdmName;
boolean relSrc2DstUseLdmName;
boolean dstUseSurrogateKey;
boolean srcIsIdentifying;
boolean dstIsIdentifying;
boolean dstRefToGen;
boolean dstRefToNl;
boolean switchedDirection;
boolean useIndexOnFk;
String qualIndexName;
int i;
Integer relFkMaintenanceMode;
boolean extendFK;

//On Error GoTo ErrorExit 

M22_Class_Utilities.ClassDescriptor class;
class = M22_Class.g_classes.descriptors[classIndex];

boolean suppressRefIntegrity;
suppressRefIntegrity = false;
if (thisPoolIndex > 0) {
suppressRefIntegrity = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressRefIntegrity;
}

// check if relationship is implemented as FK in table 'qualTabName'
if (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[thisRelIndex].reusedRelIndex > 0) {
// we re-use an existing foreign key to implement this relationship
return;
}

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl &  M03_Config.supportNlForRelationships) {
// any relationship marked as 'NL' definitely requires a relationship table and cannot solely be mapped to a foreign key
return;
}

if (forGen) {
// we do not support 'timevarying relationships'
return;
}

// two cases: FK from 'left to right' or vice versa
// switch classses, if relationship is from 'right to left' (normalize direction of relationship)
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName.toUpperCase() == class.sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName.toUpperCase() == class.className.toUpperCase() & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1) {
// 'dstClass' is linked via FK
switchedDirection = false;
srcClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName);
dstClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName);
relSrc2DstShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName;
relSrc2DstUseLdmName = M23_Relationship.g_relationships.descriptors[thisRelIndex].useLrLdmRelName;
relSrc2DstLdmName = M23_Relationship.g_relationships.descriptors[thisRelIndex].lrLdmRelName;
srcIsIdentifying = M23_Relationship.g_relationships.descriptors[thisRelIndex].isIdentifyingLeft;
dstIsIdentifying = M23_Relationship.g_relationships.descriptors[thisRelIndex].isIdentifyingRight;
useIndexOnFk = M23_Relationship.g_relationships.descriptors[thisRelIndex].useIndexOnRightFk;
relFkMaintenanceMode = M23_Relationship.g_relationships.descriptors[thisRelIndex].lrFkMaintenanceMode;

dstRefToGen = (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttGen) != 0 &  dstClass.isGenForming & !dstClass.hasNoIdentity;
dstRefToNl = (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttNL) != 0 &  ((dstRefToGen &  dstClass.hasNlAttrsInGenInclSubClasses) |  (!(dstRefToGen &  dstClass.hasNlAttrsInNonGenInclSubClasses)));
} else if (M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName.toUpperCase() == class.sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName.toUpperCase() == class.className.toUpperCase() & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1) {
// 'srcClass' is linked via FK
switchedDirection = true;
dstClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName);
srcClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName);
relSrc2DstShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName;
relSrc2DstUseLdmName = M23_Relationship.g_relationships.descriptors[thisRelIndex].useRlLdmRelName;
relSrc2DstLdmName = M23_Relationship.g_relationships.descriptors[thisRelIndex].rlLdmRelName;
srcIsIdentifying = M23_Relationship.g_relationships.descriptors[thisRelIndex].isIdentifyingRight;
dstIsIdentifying = M23_Relationship.g_relationships.descriptors[thisRelIndex].isIdentifyingLeft;
useIndexOnFk = M23_Relationship.g_relationships.descriptors[thisRelIndex].useIndexOnRightFk;
relFkMaintenanceMode = M23_Relationship.g_relationships.descriptors[thisRelIndex].rlFkMaintenanceMode;

dstRefToGen = (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttGen) != 0 &  dstClass.isGenForming & !dstClass.hasNoIdentity;
dstRefToNl = (M23_Relationship.g_relationships.descriptors[thisRelIndex].leftTargetType &  M23_Relationship_Utilities.RelRefTargetType.erttNL) != 0 &  ((dstRefToGen &  dstClass.hasNlAttrsInGenInclSubClasses) |  (!(dstRefToGen &  dstClass.hasNlAttrsInNonGenInclSubClasses)));
} else {
return;
}
leftClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName);
rightclass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName);

srcQualTabName = M04_Utilities.genQualTabNameByClassIndex(srcClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
srcQualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(srcClass.classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
dstQualTabName = M04_Utilities.genQualTabNameByClassIndex(dstClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, dstRefToGen, null, null, dstRefToNl, null, null, null);
dstQualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(dstClass.classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, dstRefToGen, null, null, dstRefToNl, null, null, null);
dstUseSurrogateKey = dstClass.useSurrogateKey;

if (dstUseSurrogateKey) {
dstAttrSeq = M01_Globals.g_anOid;
String relShortName;
relShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].effectiveShortName;

if (switchedDirection) {
srcAttrSeq = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? "" : M22_Class.getClassShortNameByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)), relShortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName, null, null, null);
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable) {
srcAttrSeqNat = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? "" : M22_Class.getClassShortNameByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftEntityIndex)), relShortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName, null, null, true);
}
// ### ENDIF IVK ###
} else {
srcAttrSeq = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? "" : M22_Class.getClassShortNameByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex)), relShortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName, null, null, null);
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable) {
srcAttrSeqNat = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? "" : M22_Class.getClassShortNameByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex)), relShortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName, null, null, true);
}
// ### ENDIF IVK ###
}
} else {
srcAttrSeq = M24_Attribute.getPkAttrListByClass(dstClass.classIndex, ddlType, (relSrc2DstUseLdmName ? relSrc2DstLdmName + (relSrc2DstLdmName.compareTo("") == 0 ? "" : "_") : M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + relSrc2DstShortName + "_"), forLrt, null, null);
dstAttrSeq = M24_Attribute.getPkAttrListByClass(dstClass.classIndex, ddlType, null, null, null, null);
}

boolean fkEnforced;
fkEnforced = !((M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced |  forLrt));
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName.toUpperCase() == class.sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName.toUpperCase() == class.className.toUpperCase() & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1)) {
// need to deal with a relationship where 'class' is located at the lhs and the FK points to the right

// ### IF IVK ###
if (M03_Config.reusePsTagForRelationships &  switchedDirection & dstClass.isPsTagged & srcClass.classIndex == M01_Globals_IVK.g_classIndexProductStructure) {
// we merge this foreign key with the PS-tag
M22_Class_Utilities.printComment("reusing PS-tag for relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName + "\"", fileNo, null, null);
} else if (M03_Config.reusePsTagForRelationships & ! switchedDirection & srcClass.isPsTagged & dstClass.classIndex == M01_Globals_IVK.g_classIndexProductStructure) {
M22_Class_Utilities.printComment("reusing PS-tag for relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName + "\"", fileNo, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

if (srcClass.isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !dstClass.isCommonToOrgs & !suppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since (source) class \"" + srcClass.sectionName + "." + srcClass.className + "\" is common to MPCs and \"" + dstClass.sectionName + "." + dstClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + srcQualTabName + "\" is common to MPCs and \"" + dstQualTabName + "\" is not");
}
} else if (srcClass.isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & (!((dstClass.isCommonToPools |  dstClass.isCommonToOrgs))) & !suppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since class \"" + srcClass.sectionName + "." + srcClass.className + "\" is common to Pools and \"" + dstClass.sectionName + "." + dstClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + srcQualTabName + "\" is common to pools and \"" + dstQualTabName + "\" is not");
}
} else {
if (!(suppressRefIntegrity &  M03_Config.generateDdlCreateFK)) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! srcClass.isUserTransactional & dstClass.isUserTransactional & !suppressRefIntegrity & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to enforce foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since class \"" + srcClass.sectionName + "." + srcClass.className + "\" is not transactional and \"" + dstClass.sectionName + "." + dstClass.className + "\" is transactional", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
}
fkEnforced = false;
}

// ### IF IVK ###
for (int i = 1; i <= (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable ? 2 : 1); i++) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
if (!(suppressRefIntegrity &  M03_Config.generateDdlCreateFK)) {

extendFK = false;
if (!((M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName.compareTo(M01_ACM.snDbMeta) == 0 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName.compareTo(M01_ACM.snDbAdmin) == 0))) {
if (!((srcClass.classIndex == dstClass.classIndex &  srcClass.subClassIdStrSeparatePartition.numMaps > 0 & dstClass.subClassIdStrSeparatePartition.numMaps > 0))) {
if (!((dstClass.aggHeadClassIdStr.compareTo("09001") == 0 &  !(srcClass.aggHeadClassIdStr.compareTo("09001") == 0) & dstRefToNl == false))) {
//                    If fkEnforced Then
extendFK = true;
//                    End If
}
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isMdsExpressionRel &  M23_Relationship.g_relationships.descriptors[thisRelIndex].isTimeVarying & !class.hasNoIdentity) {
//Special handling for Expression Relations in Gen Class
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "_" + M01_LDM.gc_dbObjSuffixGen);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
// ### IF IVK ###
String foreignKeyName;

foreignKeyName = M04_Utilities.genFkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + foreignKeyName);
// ### ELSE IVK ###
//             Print #fileNo, addTab(1); genFkName(.relName, .shortName, IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
// ### IF IVK ###
if (!(extendFK)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + (i == 1 ? srcAttrSeq : srcAttrSeqNat) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(dstClass.classIndex, "", thisPoolIndex, ddlType, (i == 1 ? srcAttrSeq : srcAttrSeqNat), null, null, null, dstRefToNl) + ")");
}
// ### ELSE IVK ###
//             Print #fileNo, addTab(1); "("; srcAttrSeq; ")"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
if (!(extendFK)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + dstQualTabName + " (" + dstAttrSeq + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + dstQualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(dstClass.classIndex, thisPoolIndex, ddlType, dstAttrSeq, dstClass.aggHeadClassIdStr, null, dstRefToNl) + ")");
}
if (relFkMaintenanceMode) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON DELETE CASCADE");
}
if (!(fkEnforced |  M00_Helper.inStr(foreignKeyName, "3TSTTPA") > 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT ENFORCED");
}
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk & useIndexOnFk & M03_Config.generateDdlCreateIndex) {
// ### IF IVK ###
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, class.shortName + "_" + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), class.shortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, forMqt, null);
// ### ELSE IVK ###
//             qualIndexName = _
//               genQualIndexName( _
//                 .sectionIndex, class.shortName & "_" & .relName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), _
//                 class.shortName & .shortName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, _
//                 thisPoolIndex, forGen, forLrt, , forMqt _
//               )
// ### ENDIF IVK ###
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (i == 1 ? srcAttrSeq : srcAttrSeqNat));
// ### ELSE IVK ###
//               Print #fileNo, addTab(1); srcAttrSeq
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M78_DbMeta.registerQualLdmFk(srcQualTabNameLdm, dstQualTabNameLdm, srcClass.classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, null, null, fkEnforced);
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}

fkEnforced = !((M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced |  forLrt));
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName.toUpperCase() == class.sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName.toUpperCase() == class.className.toUpperCase() & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality != 1)) {
// need to deal with a relationship where 'class' is located at the rhs and the FK points to the left
// ### IF IVK ###
if (M03_Config.reusePsTagForRelationships & ! switchedDirection & dstClass.isPsTagged & srcClass.classIndex == M01_Globals_IVK.g_classIndexProductStructure & !suppressRefIntegrity) {
// we merge this foreign key with the PS-tag
M22_Class_Utilities.printComment("reusing PS-tag for relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName + "\"", fileNo, null, null);
} else if (M03_Config.reusePsTagForRelationships &  switchedDirection & srcClass.isPsTagged & dstClass.classIndex == M01_Globals_IVK.g_classIndexProductStructure & !suppressRefIntegrity) {
M22_Class_Utilities.printComment("reusing PS-tag for relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"(\"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrRelName + "\") : \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName + "\" -> \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName + "\"", fileNo, null, null);
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
if (srcClass.isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !dstClass.isCommonToOrgs & !suppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferenceOrgId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since (source) class \"" + srcClass.sectionName + "." + srcClass.className + "\" is common to MPCs and \"" + dstClass.sectionName + "." + dstClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since table \"" + srcQualTabName + "\" is common to MPCs and \"" + dstQualTabName + "\" is not");
}
} else if (srcClass.isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & (!((dstClass.isCommonToPools |  dstClass.isCommonToOrgs))) & !suppressRefIntegrity & M23_Relationship.g_relationships.descriptors[thisRelIndex].fkReferencePoolId <= 0) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
M04_Utilities.logMsg("unable to implement foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since (source) class \"" + srcClass.sectionName + "." + srcClass.className + "\" is common to Pools and \"" + dstClass.sectionName + "." + dstClass.className + "\" is not", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + srcQualTabName + "\" is common to pools and \"" + dstQualTabName + "\" is not");
}
} else {
if (!(suppressRefIntegrity &  M03_Config.generateDdlCreateFK)) {
M22_Class_Utilities.printSectionHeader("Foreign Key corresponding to Relationship \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\"", fileNo, null, null);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! srcClass.isUserTransactional & dstClass.isUserTransactional & !suppressRefIntegrity & !M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to enforce foreign key for \"" + M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName + "." + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + "\" since class \"" + srcClass.sectionName + "." + srcClass.className + "\" is not transactional and \"" + dstClass.sectionName + "." + dstClass.className + "\" is transactional", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
}
fkEnforced = false;
}

// ### IF IVK ###
for (int i = 1; i <= (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNationalizable ? 2 : 1); i++) {
// ### ELSE IVK ###
// ### INDENT IVK ### -4
// ### ENDIF IVK ###
if (!(suppressRefIntegrity &  M03_Config.generateDdlCreateFK)) {

extendFK = false;
if (!((M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName.compareTo(M01_ACM.snDbMeta) == 0 |  M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionName.compareTo(M01_ACM.snDbAdmin) == 0))) {
if (!((srcClass.classIndex == dstClass.classIndex &  srcClass.subClassIdStrSeparatePartition.numMaps > 0 & dstClass.subClassIdStrSeparatePartition.numMaps > 0))) {
//                  If fkEnforced Then
extendFK = true;
//                  End If
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genFkName(M23_Relationship.g_relationships.descriptors[thisRelIndex].relName, M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName, (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt));
// ### ELSE IVK ###
//             Print #fileNo, addTab(1); genFkName(.relName, .shortName, IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
// ### IF IVK ###
if (!(extendFK)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + (i == 1 ? srcAttrSeq : srcAttrSeqNat) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(dstClass.classIndex, "", thisPoolIndex, ddlType, (i == 1 ? srcAttrSeq : srcAttrSeqNat), null, null, null, null) + ")");
}
// ### ELSE IVK ###
//             Print #fileNo, addTab(1); "(" ; srcAttrSeq ; ")"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
if (!(extendFK)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + dstQualTabName + " (" + dstAttrSeq + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + dstQualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(dstClass.classIndex, thisPoolIndex, ddlType, dstAttrSeq, dstClass.aggHeadClassIdStr, null, null) + ")");
}
if (relFkMaintenanceMode == M23_Relationship_Utilities.FkMaintenanceMode.efkmCascade) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON DELETE CASCADE");
}
if (!(fkEnforced)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NOT ENFORCED");
}
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk & useIndexOnFk & M03_Config.generateDdlCreateIndex) {
// ### IF IVK ###
qualIndexName = M04_Utilities.genQualIndexName(M23_Relationship.g_relationships.descriptors[thisRelIndex].sectionIndex, class.shortName + "_" + M23_Relationship.g_relationships.descriptors[thisRelIndex].relName + (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), class.shortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].shortName + (switchedDirection ? M23_Relationship.g_relationships.descriptors[thisRelIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName) + (i == 1 ? "" : M01_Globals_IVK.gc_asnSuffixNat), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, forMqt, null);
// ### ELSE IVK ###
//             qualIndexName = _
//               genQualIndexName( _
//                 .sectionIndex, class.shortName & "_" & .relName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), _
//                 class.shortName & .shortName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, _
//                 thisPoolIndex, forGen, forLrt, , forMqt _
//               )
// ### ENDIF IVK ###

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + (i == 1 ? srcAttrSeq : srcAttrSeqNat));
// ### ELSE IVK ###
//             Print #fileNo, addTab(1); srcAttrSeq
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###

M78_DbMeta.registerQualLdmFk(srcQualTabNameLdm, dstQualTabNameLdm, srcClass.classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, null, null, fkEnforced);
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

private static void genFKCheckInfoForRelationshipByClassAndName(String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW) {
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


M22_Class_Utilities.ClassDescriptor leftClass;
M22_Class_Utilities.ClassDescriptor rightclass;
M22_Class_Utilities.ClassDescriptor dstClass;
M22_Class_Utilities.ClassDescriptor srcClass;
String srcQualTabName;
String dstQualTabName;
String srcAttrSeq;
String dstAttrSeq;

String qualIndexName;
int i;

//On Error GoTo ErrorExit 

M22_Class_Utilities.ClassDescriptor class;
class = M22_Class.g_classes.descriptors[classIndex];

boolean suppressRefIntegrity;
suppressRefIntegrity = false;

if (thisPoolIndex > 0) {
suppressRefIntegrity = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressRefIntegrity;
}

if (suppressRefIntegrity) {
return;
}

if (forGen |  forLrt | forMqt) {
return;
}

int thisRelIndex;

for (int i = 1; i <= class.relRefs.numRefs; i++) {
if (class.orMappingSuperClassIndex == M01_Globals_IVK.g_classIndexGenericAspect) {

thisRelIndex = class.relRefs.refs[i].refIndex;


if (M23_Relationship.g_relationships.descriptors[thisRelIndex].isNl &  M03_Config.supportNlForRelationships) {
// any relationship marked as 'NL' definitely requires a relationship table and cannot solely be mapped to a foreign key
goto NextI;
}

if ((!(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName.compareTo("Aspect") == 0)) |  (!(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName.compareTo("Code") == 0))) {
goto NextI;
}

// 'dstClass' is linked via FK
srcClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName);
dstClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName);

leftClass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName);
rightclass = M22_Class.getOrMappingSuperClass(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassSectionName, M23_Relationship.g_relationships.descriptors[thisRelIndex].rightClassName);

srcQualTabName = M04_Utilities.genQualTabNameByClassIndex(srcClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
dstQualTabName = M04_Utilities.genQualTabNameByClassIndex(dstClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, false, null, null, null);

dstAttrSeq = M01_Globals.g_anOid;
String relShortName;
relShortName = M23_Relationship.g_relationships.descriptors[thisRelIndex].effectiveShortName;

srcAttrSeq = M04_Utilities.genSurrogateKeyName(ddlType, (M03_Config.reuseColumnsInTabsForOrMapping ? "" : M22_Class.getClassShortNameByIndex(M23_Relationship.g_relationships.descriptors[thisRelIndex].rightEntityIndex)), relShortName + M23_Relationship.g_relationships.descriptors[thisRelIndex].lrShortRelName, null, null, null);

boolean fkEnforced;
fkEnforced = !((M23_Relationship.g_relationships.descriptors[thisRelIndex].isNotEnforced |  forLrt));
if ((M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassSectionName.toUpperCase() == class.sectionName.toUpperCase() &  M23_Relationship.g_relationships.descriptors[thisRelIndex].leftClassName.toUpperCase() == class.className.toUpperCase() & M23_Relationship.g_relationships.descriptors[thisRelIndex].maxRightCardinality == 1)) {

M78_DbMeta.registerCheckFk(srcQualTabName, dstQualTabName, srcAttrSeq, fkEnforced);
}


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


public static void genFKCheckSPForRelationshipByClassAndName(String qualTabName,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}


//On Error GoTo ErrorExit 

boolean suppressRefIntegrity;
suppressRefIntegrity = false;


if (thisPoolIndex > 0) {
suppressRefIntegrity = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressRefIntegrity;
}

if (suppressRefIntegrity) {
return;
}

if (M78_DbMeta.g_checkFks.numFks < 1) {
return;
}


M22_Class_Utilities.printSectionHeader("SP for checking foreign keys not enforced", fileNo, null, null);
String qualProcedureNameCheckFk;

qualProcedureNameCheckFk = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAspect, M01_ACM.spnFkCheckAspectCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameCheckFk);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure");
M11_LRT.genProcParm(fileNo, "IN", "timestamp_in", "TIMESTAMP", true, "marks the execution timestamp of the LRT ");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of invalid code references");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF timestamp_in IS NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT COUNT(OID) FROM (");

String srcQualTabName;
String dstQualTabName;
srcQualTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
dstQualTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

String stmtForFk;
stmtForFk = "";

int i;
for (int i = 1; i <= M78_DbMeta.g_checkFks.numFks; i++) {
if (M78_DbMeta.g_checkFks.fks[i].srcQualTableName.compareTo(srcQualTabName) == 0 &  M78_DbMeta.g_checkFks.fks[i].dstQualTableName.compareTo(dstQualTabName) == 0 & !M78_DbMeta.g_checkFks.fks[i].isEnforced) {
if (!(stmtForFk.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL ");
}

stmtForFk = "SELECT DISTINCT " + M78_DbMeta.g_checkFks.fks[i].srcAttrSeq + " AS OID FROM " + qualTabName + " WHERE PS_OID = psOid_in ";
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + stmtForFk);
}
}


M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT OID FROM " + dstQualTabName + " GC WHERE GC.OID = REF.OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT COUNT(OID) FROM (");

stmtForFk = "";
for (int i = 1; i <= M78_DbMeta.g_checkFks.numFks; i++) {
if (M78_DbMeta.g_checkFks.fks[i].srcQualTableName.compareTo(srcQualTabName) == 0 &  M78_DbMeta.g_checkFks.fks[i].dstQualTableName.compareTo(dstQualTabName) == 0 & !M78_DbMeta.g_checkFks.fks[i].isEnforced) {
if (!(stmtForFk.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL ");
}

stmtForFk = "SELECT DISTINCT " + M78_DbMeta.g_checkFks.fks[i].srcAttrSeq + " AS OID FROM " + qualTabName + " WHERE PS_OID = psOid_in AND LASTUPDATETIMESTAMP = timestamp_in";
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + stmtForFk);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") REF");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT OID FROM " + dstQualTabName + " GC WHERE GC.OID = REF.OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

//create index for this SP on lastupdatetimestamp of GenericAspect: only in Work and Productive, not necessary in LRT-Tables
String schemaName;
String tabName;
M78_DbMeta.splitQualifiedName(srcQualTabName, schemaName, tabName);

String qualIndexName;
qualIndexName = schemaName + ".IDX_GAS_LASTUPDTS";

M22_Class_Utilities.printSectionHeader("Index on LASTUPDATETIMESTAMP for SP " + qualProcedureNameCheckFk, fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (LASTUPDATETIMESTAMP ASC)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

// ### IF IVK ###
public static void genFKsForRelationshipsByClass(String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Integer tabPartitionTypeW) {
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

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genFKsForRelationshipsByClass( _
// ByRef qualTabName As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False _
//)
// ### ENDIF IVK ###
// qualTabName: fully qualified name of table to generate FKs for
// class: Class to analyse for relationships implemented as FK
//        if this class appears on the left hand side of a relationship with a 'maxRightCardinality' of '1'

//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M22_Class.g_classes.descriptors[classIndex].relRefs.numRefs; i++) {
// ### IF IVK ###
genFKForRelationshipByClassAndName(qualTabName, M22_Class.g_classes.descriptors[classIndex].classIndex, M22_Class.g_classes.descriptors[classIndex].relRefs.refs[i].refIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType);
// ### ELSE IVK ###
//     genFKForRelationshipByClassAndName qualTabName, .classIndex, .relRefs.refs(i).refIndex, _
//       thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genFKsForRelationshipsByClassRecursive(String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Integer tabPartitionTypeW) {
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

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genFKsForRelationshipsByClassRecursive( _
// ByRef qualTabName As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False _
//)
// ### ENDIF IVK ###
// qualTabName: fully qualified name of table to generate FKs for
// class: Class to analyse for relationships implemented as FK; this is done recursively over inheritance relationship

//On Error GoTo ErrorExit 

if (M22_Class.g_classes.descriptors[classIndex].noFks) {
return;
}

// ### IF IVK ###
M24_Attribute.genFKsForRelationshipsByClass(qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType);
// ### ELSE IVK ###
//   genFKsForRelationshipsByClass qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
// ### ENDIF IVK ###

if (M22_Class.g_classes.descriptors[classIndex].orMappingSuperClassIndex == M01_Globals_IVK.g_classIndexGenericAspect) {
genFKCheckInfoForRelationshipByClassAndName(qualTabName, M22_Class.g_classes.descriptors[classIndex].classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt);
}


int i;
for (i = 1; i <= 1; i += (1)) {
// ### IF IVK ###
M24_Attribute.genFKsForRelationshipsByClassRecursive(qualTabName, M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i], thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType);
// ### ELSE IVK ###
//     genFKsForRelationshipsByClassRecursive qualTabName, .subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genFKsForGenParent(String qualTabNameGen, String qualTabNameGenLdm, String qualTabName, String qualTabNameLdm, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Integer tabPartitionTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genFKsForGenParent( _
// ByRef qualTabNameGen As String, _
// ByRef qualTabNameGenLdm As String, _
// ByRef qualTabName As String, _
// ByRef qualTabNameLdm As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm _
//)
// ### ENDIF IVK ###
String pkAttrList;
String refPkAttrList;

if (M22_Class.g_classes.descriptors[classIndex].noFks) {
return;
}

if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey) {
pkAttrList = M22_Class.g_classes.descriptors[classIndex].shortName + "_" + M01_Globals.g_anOid;
refPkAttrList = M01_Globals.g_anOid;
} else {
pkAttrList = M24_Attribute.getPkAttrListByClass(M22_Class.g_classes.descriptors[classIndex].classIndex, null, null, null, null, null);
refPkAttrList = pkAttrList;
}

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Parent\" of \"GEN-Table\" \"" + qualTabNameGen + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genFkName(M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, "PAR", ddlType, thisOrgIndex, thisPoolIndex, null, null));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(M22_Class.g_classes.descriptors[classIndex].classIndex, "", thisPoolIndex, ddlType, pkAttrList, null, null, null, null) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(M22_Class.g_classes.descriptors[classIndex].classIndex, thisPoolIndex, ddlType, refPkAttrList, null, null, null) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameGenLdm, qualTabNameLdm, M22_Class.g_classes.descriptors[classIndex].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, null, true, null);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk & M03_Config.generateDdlCreateIndex) {
String qualIndexName;
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className + "GPA", M22_Class.g_classes.descriptors[classIndex].shortName + "GPA", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkAttrList.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}


// ### IF IVK ###
public static void genPKForClass(String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean includeValidFromW, Boolean forLrtW, Boolean forMqtW, Boolean noConstraintsW, Integer tabPartitionTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean includeValidFrom; 
if (includeValidFromW == null) {
includeValidFrom = false;
} else {
includeValidFrom = includeValidFromW;
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
//Sub genPKForClass( _
// ByRef qualTabName As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional includeValidFrom As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional noConstraints As Boolean = False _
//)
// ### ENDIF IVK ###
String pkName;
String ukName;
String qualIndexName;
String ukAttrList;
String pkAttrList;
String attrListIncludedTech;
String attrListIncluded;

//On Error GoTo ErrorExit 

boolean poolCommonItemsLocal;
boolean poolIsArchive;
boolean M72_DataPool.poolSupportLrt;
boolean noRangePartitioning;

if (thisPoolIndex > 0) {
poolCommonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
poolIsArchive = M72_DataPool.g_pools.descriptors[thisPoolIndex].isArchive;
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

attrListIncluded = "";
attrListIncludedTech = "";

pkName = M04_Utilities.genPkName(M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt);
ukName = "UK_" + pkName.substring(4 - 1, 4 + pkName.length() - 1);
qualIndexName = M04_Utilities.genUkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null);

ukAttrList = M24_Attribute.getPkAttrListByClass(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, null, forLrt, null, null);
attrListIncluded = M24_Attribute.getPkAttrListByClass(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, null, forLrt, true, null);

attrListIncludedTech = "";

if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey) {
noRangePartitioning = M22_Class.g_classes.descriptors[classIndex].noRangePartitioning;
if (M22_Class.g_classes.descriptors[classIndex].isPsTagged) {
if (!(noRangePartitioning &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & M72_DataPool.poolSupportLrt & !M22_Class.g_classes.descriptors[classIndex].rangePartitioningAll)) {
if (M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt) {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenMqt);
} else {
noRangePartitioning = !(M03_Config.partitionLrtPublicWhenNoMqt);
}
}
}
}

// ### IF IVK ###
// todo: we currently do not support this - could not prove to help (check this again)
if ((!((thisPoolIndex > 0 &  (poolCommonItemsLocal |  poolIsArchive)))) & ! M22_Class.g_classes.descriptors[classIndex].condenseData & (M22_Class.g_classes.descriptors[classIndex].isAggHead |  M22_Class.g_classes.descriptors[classIndex].isCommonToPools) & (forMqt | ! forLrt)) {
if (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged &  M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex > 0 & !M22_Class.g_classes.descriptors[classIndex].isPsTagged)) {
if (M72_DataPool.poolSupportLrt &  M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].isUserTransactional) {
attrListIncludedTech = (M22_Class.g_classes.descriptors[classIndex].navPathToDiv.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].leftFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[M22_Class.g_classes.descriptors[classIndex].navPathToDiv.relRefIndex].rightFkColName[ddlType]) + "," + M01_Globals.g_anInLrt;
}
}
}

// ### ENDIF IVK ###
if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey) {
pkAttrList = M01_Globals.g_anOid;
} else {
pkAttrList = ukAttrList;
}

if (includeValidFrom) {
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + M01_Globals_IVK.g_anValidFrom;
ukAttrList = ukAttrList + (ukAttrList.compareTo("") == 0 ? "" : ",") + M01_Globals_IVK.g_anValidFrom;
}

// ### IF IVK ###
if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & (!(forLrt |  forMqt)) & !M22_Class.g_classes.descriptors[classIndex].condenseData) {
if (!(ukAttrList.compareTo("") == 0)) {
ukAttrList = ukAttrList + "," + M01_Globals_IVK.g_anIsDeleted;
}
}

// ### ENDIF IVK ###
if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & forLrt) {
String extraAttrs;
extraAttrs = "";
if (forMqt) {
extraAttrs = extraAttrs + (extraAttrs.compareTo("") == 0 ? "" : ",") + M01_Globals.g_anIsLrtPrivate;
}
extraAttrs = extraAttrs + (extraAttrs.compareTo("") == 0 ? "" : ",") + M01_Globals.g_anInLrt + "," + M01_Globals.g_anLrtState;

if (!(ukAttrList.compareTo("") == 0)) {
ukAttrList = ukAttrList + "," + extraAttrs;
}
if (!(pkAttrList.compareTo("") == 0)) {
pkAttrList = pkAttrList + "," + extraAttrs;
}
}
if (!(pkAttrList.compareTo("") == 0)) {
if (noConstraints) {
if (thisPoolIndex != M01_Globals_IVK.g_archiveDataPoolIndex) {
if (M03_Config.generateDdlCreateIndex) {
M22_Class_Utilities.printSectionHeader("Primary Key", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null, forMqt, "PKA"), thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null, forMqt, "PKA"));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "(" + pkAttrList.toUpperCase() + (!(attrListIncluded.compareTo("") == 0) ? "," + attrListIncluded : "") + (!(attrListIncludedTech.compareTo("") == 0) ? "," + attrListIncludedTech : "") + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
} else {
if (M03_Config.generateDdlCreatePK) {
// ArchivePool does not have constraints in general but must have a primary key anyway
M22_Class_Utilities.printSectionHeader("Primary Key", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null, forMqt, "PKA"), thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY(" + pkAttrList.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

}// indexExcp
}
}//archiveDataPool
} else {
if (M03_Config.generateDdlCreatePK) {
M22_Class_Utilities.printSectionHeader("Primary Key", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY(" + pkAttrList.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged & ! noRangePartitioning & !M22_Class.g_classes.descriptors[classIndex].psTagOptional & !forLrt & thisPoolIndex != 1) {

boolean additionalUK;
int i;
for (i = 1; i <= 1; i += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == -1 & (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == false |  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == true &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isCommonToPools == true)) & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == -1 & (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == false |  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == true &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isCommonToPools == true)) & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 & (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == false |  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == true &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isCommonToPools == true)) & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 & (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == false |  (M23_Relationship.g_relationships.descriptors[i].isCommonToPools == true &  M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isCommonToPools == true)) & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M22_Class.g_classes.descriptors[classIndex].sectionName.compareTo("Lrt") == 0 &  M22_Class.g_classes.descriptors[classIndex].className.compareTo("LRT") == 0) {
additionalUK = true;
break;
}
}

if (additionalUK) {
M22_Class_Utilities.printSectionHeader("Unique Constraint for \"" + qualTabName + "\"", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualUkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, ukName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "(" + pkAttrList.toUpperCase() + ", PS_OID" + (M22_Class.g_classes.descriptors[classIndex].subClassIdStrSeparatePartition.numMaps > 0 ? ", " + M01_Globals.g_anCid.toUpperCase() : "") + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ukName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNIQUE (" + pkAttrList.toUpperCase() + ", PS_OID" + (M22_Class.g_classes.descriptors[classIndex].subClassIdStrSeparatePartition.numMaps > 0 ? ", " + M01_Globals.g_anCid.toUpperCase() : "") + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

}

if ((!(attrListIncludedTech.compareTo("") == 0) &  M00_Helper.inStr(1, pkAttrList, attrListIncludedTech) == 0) |  (!(attrListIncluded.compareTo("") == 0) &  M00_Helper.inStr(1, pkAttrList, attrListIncluded) == 0)) {
if (M99_IndexException_Utilities.indexExcp(M04_Utilities.genQualPkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className + "I", M22_Class.g_classes.descriptors[classIndex].shortName + "I", ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null), thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualPkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className + "I", M22_Class.g_classes.descriptors[classIndex].shortName + "I", ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "(" + pkAttrList.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCLUDE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + attrListIncluded + (attrListIncluded.compareTo("") == 0 |  attrListIncludedTech.compareTo("") == 0 | M00_Helper.inStr(1, pkAttrList, attrListIncludedTech) > 0 ? "" : ",") + (attrListIncludedTech.compareTo("") == 0 |  M00_Helper.inStr(1, pkAttrList, attrListIncludedTech) > 0 ? "" : attrListIncludedTech.toUpperCase()) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}
}
}

if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey &  !(ukAttrList.compareTo("") == 0) & !includeValidFrom & M03_Config.generateDdlCreateIndex) {
M22_Class_Utilities.printSectionHeader((forLrt |  forMqt | noConstraints ? "" : "Unique ") + "Index on Business Key Attributes", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (forLrt |  forMqt | noConstraints ? "" : "UNIQUE ") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + ukAttrList.toUpperCase() + (forLrt |  forMqt | noConstraints ? (attrListIncluded.compareTo("") == 0 ? "" : "," + attrListIncluded) : "") + ")");
if (!(attrListIncluded.compareTo("") == 0) & ! (forLrt |  forMqt | noConstraints)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCLUDE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + attrListIncluded.toUpperCase() + ")");
}
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genPKForGenClass( String qualTabName, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forLrtW, Boolean forMqtW, Boolean noConstraintsW) {
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

String pkName;
String ukName;
String qualIndexName;

String ukAttrList;
String ukAttrListBus;
String pkAttrList;
String pkAttrListBus;

//On Error GoTo ErrorExit 

pkName = M04_Utilities.genPkName(M22_Class.g_classes.descriptors[classIndex].shortName, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, true, forLrt);
ukName = "UK_" + pkName.substring(4 - 1, 4 + pkName.length() - 1);
qualIndexName = M04_Utilities.genUkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, true, forLrt, null);

ukAttrListBus = M24_Attribute.getPkAttrListByClass(M22_Class.g_classes.descriptors[classIndex].classIndex, ddlType, null, forLrt, null, null);

if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey) {
pkAttrListBus = M22_Class.g_classes.descriptors[classIndex].shortName + "_" + M01_Globals.g_anOid;
pkAttrList = M01_Globals.g_anOid;
} else {
pkAttrListBus = ukAttrListBus;
pkAttrList = pkAttrListBus;
}

pkAttrListBus = pkAttrListBus + (pkAttrListBus.compareTo("") == 0 ? "" : ",") + M01_Globals_IVK.g_anValidFrom;
ukAttrListBus = ukAttrListBus + (ukAttrListBus.compareTo("") == 0 ? "" : ",") + M01_Globals_IVK.g_anValidFrom;

if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & forLrt) {
String extraAttrs;
extraAttrs = "";
if (forMqt) {
extraAttrs = extraAttrs + (extraAttrs.compareTo("") == 0 ? "" : ",") + M01_Globals.g_anIsLrtPrivate;
}
extraAttrs = extraAttrs + (extraAttrs.compareTo("") == 0 ? "" : ",") + M01_Globals.g_anInLrt + "," + M01_Globals.g_anLrtState;

if (!(ukAttrList.compareTo("") == 0)) {
ukAttrList = ukAttrList + "," + extraAttrs;
}
if (!(pkAttrList.compareTo("") == 0)) {
pkAttrList = pkAttrList + "," + extraAttrs;
}
}

// ### IF IVK ###
if (M01_Globals.g_genLrtSupport &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional & (!(forLrt |  forMqt))) {
if (!(pkAttrListBus.compareTo("") == 0)) {
pkAttrListBus = pkAttrListBus + "," + M01_Globals_IVK.g_anIsDeleted;
}
if (!(ukAttrListBus.compareTo("") == 0)) {
ukAttrListBus = ukAttrListBus + "," + M01_Globals_IVK.g_anIsDeleted;
}
}

// ### ENDIF IVK ###
if (!(pkAttrList.compareTo("") == 0)) {
if (M03_Config.generateDdlCreatePK) {
M22_Class_Utilities.printSectionHeader("Primary Key", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY(" + pkAttrList.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (thisPoolIndex == 3 & ! forLrt) {
//        If g_pools.descriptors(thisPoolIndex).id = 3 And Not forLrt Then

boolean additionalUK;
int i;
for (i = 1; i <= 1; i += (1)) {
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == -1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == -1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(M22_Class.g_classes.descriptors[classIndex].sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(M22_Class.g_classes.descriptors[classIndex].className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1 & M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged == true) {
additionalUK = true;
break;
}
}

if (additionalUK) {
if (M99_IndexException_Utilities.indexExcp(M04_Utilities.genQualUkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, ukName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null), thisOrgIndex, null) == false) {
M22_Class_Utilities.printSectionHeader("Unique Constraint for \"" + qualTabName + "\"", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualUkName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, ukName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + "(" + pkAttrList.toUpperCase() + ", PS_OID" + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ukName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNIQUE (" + pkAttrList.toUpperCase() + ", PS_OID)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
}

}

}

if (M03_Config.generateDdlCreateIndex) {
M22_Class_Utilities.printSectionHeader((forLrt |  noConstraints ? "" : "Unique ") + "Index on \"" + pkAttrListBus + "\"", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (forLrt |  noConstraints ? "" : "UNIQUE ") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + pkAttrListBus.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

if (M22_Class.g_classes.descriptors[classIndex].useSurrogateKey &  !(pkAttrListBus.compareTo("") == 0) & !M22_Class.g_classes.descriptors[classIndex].useSurrogateKey & M03_Config.generateDdlCreateIndex) {
M22_Class_Utilities.printSectionHeader((forMqt |  noConstraints ? "" : "Unique ") + "Index on Business Key Attributes", fileNo, null, null);
if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE " + (forMqt |  noConstraints ? "" : "UNIQUE ") + "INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName + "B");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName + " (" + ukAttrListBus.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static String genOidSequenceNameForClass(int thisClassIndex,  int thisOrgIndex,  int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = M24_Attribute.genOidSequenceNameForClassIndex(thisClassIndex, thisOrgIndex, thisPoolIndex, ddlType);
return returnValue;
}


public static String genOidSequenceNameForClassIndex(int classIndex,  int thisOrgIndex,  int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
String sectionName;
String name;

returnValue = "";

if (!(M22_Class.g_classes.descriptors[classIndex].useSurrogateKey)) {
return returnValue;
}
returnValue = M04_Utilities.genQualObjName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
return returnValue;
}


public static void genNlsAttrDeclsForEntity(int acmEntityIndex, Integer acmEntityType, int fileNo, String qualTabNameW, Integer onlyThisAttributeW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean forLrtW, Integer outputModeW, Boolean includeMetaAttrsW, String parentTabPkAttrListW, String parentTabPkAttrDeclW, String pkAttrListW, String tabAttrListW, Boolean useAlternativeDefaultsW) {
String qualTabName; 
if (qualTabNameW == null) {
qualTabName = "";
} else {
qualTabName = qualTabNameW;
}

int onlyThisAttribute; 
if (onlyThisAttributeW == null) {
onlyThisAttribute = -1;
} else {
onlyThisAttribute = onlyThisAttributeW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
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

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

boolean includeMetaAttrs; 
if (includeMetaAttrsW == null) {
includeMetaAttrs = true;
} else {
includeMetaAttrs = includeMetaAttrsW;
}

String parentTabPkAttrList; 
if (parentTabPkAttrListW == null) {
parentTabPkAttrList = "";
} else {
parentTabPkAttrList = parentTabPkAttrListW;
}

String parentTabPkAttrDecl; 
if (parentTabPkAttrDeclW == null) {
parentTabPkAttrDecl = "";
} else {
parentTabPkAttrDecl = parentTabPkAttrDeclW;
}

String pkAttrList; 
if (pkAttrListW == null) {
pkAttrList = "";
} else {
pkAttrList = pkAttrListW;
}

String tabAttrList; 
if (tabAttrListW == null) {
tabAttrList = "";
} else {
tabAttrList = tabAttrListW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
transformation = M24_Attribute_Utilities.nullAttributeTransformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, onlyThisAttribute, false, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, includeMetaAttrs, outputMode, qualTabName, parentTabPkAttrList, parentTabPkAttrDecl, pkAttrList, tabAttrList, useAlternativeDefaults);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genNlsTransformedAttrListForEntity(int acmEntityIndex, Integer acmEntityType, M24_Attribute_Utilities.AttributeListTransformation transformation, int fileNo, Integer onlyThisAttributeW, Boolean forceNotNullW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean forLrtW, Boolean includeMetaAttrsW, Integer outputModeW, String qualTabNameW, String parentTabPkAttrListW, String parentTabPkAttrDeclW, String pkAttrListW, String tabAttrListW, Boolean useAlternativeDefaultsW) {
int onlyThisAttribute; 
if (onlyThisAttributeW == null) {
onlyThisAttribute = -1;
} else {
onlyThisAttribute = onlyThisAttributeW;
}

boolean forceNotNull; 
if (forceNotNullW == null) {
forceNotNull = false;
} else {
forceNotNull = forceNotNullW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
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

boolean includeMetaAttrs; 
if (includeMetaAttrsW == null) {
includeMetaAttrs = true;
} else {
includeMetaAttrs = includeMetaAttrsW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

String qualTabName; 
if (qualTabNameW == null) {
qualTabName = "";
} else {
qualTabName = qualTabNameW;
}

String parentTabPkAttrList; 
if (parentTabPkAttrListW == null) {
parentTabPkAttrList = "";
} else {
parentTabPkAttrList = parentTabPkAttrListW;
}

String parentTabPkAttrDecl; 
if (parentTabPkAttrDeclW == null) {
parentTabPkAttrDecl = "";
} else {
parentTabPkAttrDecl = parentTabPkAttrDeclW;
}

String pkAttrList; 
if (pkAttrListW == null) {
pkAttrList = "";
} else {
pkAttrList = pkAttrListW;
}

String tabAttrList; 
if (tabAttrListW == null) {
tabAttrList = "";
} else {
tabAttrList = tabAttrListW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, onlyThisAttribute, false, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, includeMetaAttrs, outputMode, qualTabName, parentTabPkAttrList, parentTabPkAttrDecl, pkAttrList, tabAttrList, useAlternativeDefaults);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genNlsTransformedAttrListForEntityWithColReUse(int acmEntityIndex, Integer acmEntityType, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, int fileNo, Integer onlyThisAttributeW, Boolean forceNotNullW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Boolean forGenW, Boolean forLrtW, Boolean includeMetaAttrsW, Integer outputModeW, String qualTabNameW, String parentTabPkAttrListW, String parentTabPkAttrDeclW, String pkAttrListW, String tabAttrListW, Boolean useAlternativeDefaultsW) {
int onlyThisAttribute; 
if (onlyThisAttributeW == null) {
onlyThisAttribute = -1;
} else {
onlyThisAttribute = onlyThisAttributeW;
}

boolean forceNotNull; 
if (forceNotNullW == null) {
forceNotNull = false;
} else {
forceNotNull = forceNotNullW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
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

boolean includeMetaAttrs; 
if (includeMetaAttrsW == null) {
includeMetaAttrs = true;
} else {
includeMetaAttrs = includeMetaAttrsW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

String qualTabName; 
if (qualTabNameW == null) {
qualTabName = "";
} else {
qualTabName = qualTabNameW;
}

String parentTabPkAttrList; 
if (parentTabPkAttrListW == null) {
parentTabPkAttrList = "";
} else {
parentTabPkAttrList = parentTabPkAttrListW;
}

String parentTabPkAttrDecl; 
if (parentTabPkAttrDeclW == null) {
parentTabPkAttrDecl = "";
} else {
parentTabPkAttrDecl = parentTabPkAttrDeclW;
}

String pkAttrList; 
if (pkAttrListW == null) {
pkAttrList = "";
} else {
pkAttrList = pkAttrListW;
}

String tabAttrList; 
if (tabAttrListW == null) {
tabAttrList = "";
} else {
tabAttrList = tabAttrListW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsLeft;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefsRight;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
String className;
String classShortName;
boolean useVersiontag;
boolean useSurrogateKey;
boolean isUserTransactional;
boolean isAggregateMember;
boolean isAggregateHead;
int numNlAttrs;
int defaultStatus;
// ### IF IVK ###
boolean hasNoIdentity;
boolean enforceChangeComment;
boolean noRangePartitioning;
boolean isPsForming;
boolean isPsTagged;
boolean psTagOptional;
boolean supportPsCopy;
boolean ahSupportPsCopy;
boolean condenseData;
condenseData = false;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

// todo: we should use a parameter to pass this info
boolean forLrtMqt;
forLrtMqt = forLrt &  ((outputMode &  M01_Common.DdlOutputMode.edomMqtLrt) == M01_Common.DdlOutputMode.edomMqtLrt);

M24_Attribute_Utilities.initAttrDescriptorRefs(attrRefsLeft);
M24_Attribute_Utilities.initAttrDescriptorRefs(attrRefsRight);

boolean M72_DataPool.poolSupportLrt;

if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
nlAttrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefs;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefs;
className = M22_Class.g_classes.descriptors[acmEntityIndex].className;
classShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
useVersiontag = M22_Class.g_classes.descriptors[acmEntityIndex].useVersiontag;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isAggregateMember = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
isAggregateHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
defaultStatus = M22_Class.g_classes.descriptors[acmEntityIndex].defaultStatus;
if (onlyThisAttribute > 0) {
numNlAttrs = 1;
} else {
numNlAttrs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors;

int i;
for (i = 1; i <= 1; i += (1)) {
numNlAttrs = numNlAttrs + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexes[i]].nlAttrRefs.numDescriptors;
}
}
// ### IF IVK ###
isPsForming = M22_Class.g_classes.descriptors[acmEntityIndex].isPsForming;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
supportPsCopy = M22_Class.g_classes.descriptors[acmEntityIndex].supportExtendedPsCopy;
if (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) {
ahSupportPsCopy = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex].supportExtendedPsCopy;
}
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
noRangePartitioning = M22_Class.g_classes.descriptors[acmEntityIndex].noRangePartitioning;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming &  M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
enforceChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].enforceLrtChangeComment;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
nlAttrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex > -1) {
attrRefsLeft = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].attrRefs;
}
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex > -1) {
attrRefsRight = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].attrRefs;
}
M23_Relationship_Utilities.initRelDescriptorRefs(relRefs);
className = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
classShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
useVersiontag = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useVersiontag;
useSurrogateKey = M03_Config.useSurrogateKeysForNMRelationships &  (M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange);
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
isAggregateMember = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
isAggregateHead = false;
defaultStatus = M23_Relationship.g_relationships.descriptors[acmEntityIndex].defaultStatus;
// ### IF IVK ###
isPsForming = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsForming;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = false;
supportPsCopy = M23_Relationship.g_relationships.descriptors[acmEntityIndex].supportExtendedPsCopy;
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0) {
ahSupportPsCopy = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex].supportExtendedPsCopy;
}
noRangePartitioning = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noRangePartitioning;
hasNoIdentity = true;
enforceChangeComment = false;
// ### ENDIF IVK ###
if (onlyThisAttribute > 0) {
numNlAttrs = 1;
} else {
numNlAttrs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors;

for (i = 1; i <= 1; i += (1)) {
numNlAttrs = numNlAttrs + M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusingRelIndexes.indexes[i]].nlAttrRefs.numDescriptors;
}
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
attrRefs = M21_Enum.g_enums.descriptors[acmEntityIndex].attrRefs;
className = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;
classShortName = M21_Enum.g_enums.descriptors[acmEntityIndex].shortName;
useVersiontag = true;
useSurrogateKey = true;
isUserTransactional = false;
isAggregateMember = false;
isAggregateHead = false;
numNlAttrs = 1;
// ### IF IVK ###
isPsForming = false;
isPsTagged = false;
psTagOptional = false;
noRangePartitioning = true;
hasNoIdentity = false;
// ### ENDIF IVK ###
}

if (includeMetaAttrs) {
if (useSurrogateKey) {
M22_Class_Utilities.printSectionHeader("Surrogate Key", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacOid, null, indent, null, null, null, null, null, null, null), null, null);
}

if (M01_Globals.g_genLrtSupport &  isUserTransactional) {
if (outputMode &  M01_Common.DdlOutputMode.edomMqtLrt) {
M22_Class_Utilities.printSectionHeader("Flag '" + M01_ACM.conIsLrtPrivate + "'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conIsLrtPrivate, M01_ACM.cosnIsLrtPrivate, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexIsLrtPrivate, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacMqtLrtMeta, null, indent, null, "[LRT-MQT] identifies 'LRT-private' records", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
M22_Class_Utilities.printSectionHeader("Column '" + M01_ACM.conInUseBy + "'", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, M01_Common.AttrCategory.eacMqtLrtMeta, null, indent, null, "[LRT-MQT] identifies the user holding the lock on the record", null, null, true, null, null), null, null);
// ### ELSE IVK ###
//        printConditional fileNo, _
//          genTransformedAttrDeclByDomainWithColReUse( _
//            conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
//            tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
//            "[LRT-MQT] identifies the user holding the lock on the record", , True _
//          )
// ### ENDIF IVK ###
}

// ### IF IVK ###
if ((forLrt | ! condenseData)) {
// ### ELSE IVK ###
//     If forLrt Then
// ### ENDIF IVK ###
M22_Class_Utilities.printSectionHeader("LRT - Id", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt & ! forLrtMqt ? "NOT NULL" : ""), null, ddlType, null, outputMode &  (((outputMode &  M01_Common.DdlOutputMode.edomValue) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomLrtPriv) != 0) ? !(M01_Common.DdlOutputMode.edomList) : !()), M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, !(forLrt |  forLrtMqt), null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
//           transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , _
//           ddlType, , outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
//           eacLrtMeta, , indent, , , , Not forLrt Or forLrtMqt _
//         )
// ### ENDIF IVK ###
}

// ### IF IVK ###
if ((isAggregateHead |  enforceChangeComment) & ! forGen) {
// ### ELSE IVK ###
//     If isAggregateHead And Not forGen Then
// ### ENDIF IVK ###
if (!(forLrt &  (outputMode &  M01_Common.DdlOutputMode.edomValueLrt))) {
M22_Class_Utilities.printSectionHeader("Change Comment", fileNo, M01_Common.DdlOutputMode.edomValueLrt, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conChangeComment, M01_ACM.cosnChangeComment, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexChangeComment, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, true, null, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
//             transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt, eacLrtMeta, , indent, , , , True _
//           )
// ### ENDIF IVK ###
} else if (forLrt |  (outputMode &  (M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDeclLrt))) {
M22_Class_Utilities.printSectionHeader("Change Comment", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conChangeComment, M01_ACM.cosnChangeComment, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexChangeComment, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, true, null, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
//             transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacLrtMeta, , indent, , , , True _
//           )
// ### ENDIF IVK ###
}
}

// ### IF IVK ###
M22_Class_Utilities.printSectionHeader("Flag 'status'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt ? "" : "NOT NULL DEFAULT " + (useAlternativeDefaults ? M86_SetProductive.statusProductive : M86_SetProductive.statusWorkInProgress)), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, indent, null, "Specifies the state of the record with respect to 'release to production", String.valueOf(defaultStatus), null, !(forLrt), null, null), null, null);
// ### ENDIF IVK ###
}

if ((isAggregateMember |  (M01_Globals.g_genLrtSupport &  isUserTransactional))) {
M22_Class_Utilities.printSectionHeader("ClassId of 'aggregate head'", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conAhClassId, M01_ACM.cosnAggHeadClassId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexCid, transformation, tabColumns, acmEntityType, acmEntityIndex, (M03_Config.generateAhIdsNotNull & ! useAlternativeDefaults ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacCid |  M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "ID of the ACM-class of the 'Aggregate Head'", null, null, true, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
//           "ID of the ACM-class of the 'Aggregate Head'", , True _
//         )
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("ObjectId of 'aggregate head'", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conAhOId, M01_ACM.cosnAggHeadOId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, (M03_Config.generateAhIdsNotNull & ! useAlternativeDefaults ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  M01_Common.AttrCategory.eacLrtMeta | M01_Common.AttrCategory.eacAhOid | (isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (ahSupportPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0), null, indent, null, "Object ID of the 'Aggregate Head'", null, null, true, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, _
//           eacFkOid Or eacLrtMeta Or eacAhOid, , indent, , _
//           "Object ID of the 'Aggregate Head'", , True _
//         )
// ### ENDIF IVK ###
// ### IF IVK ###
if (M03_Config.hasBeenSetProductiveInPrivLrt) {
if (isUserTransactional &  M01_Globals.g_genLrtSupport) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT " + (useAlternativeDefaults ? 1 : 0), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record has been set productive", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
}
// ### ENDIF IVK ###
}

if (M01_Globals.g_genLrtSupport &  isUserTransactional) {
// columns which exist in public and not in private tables
if (forLrt &  (outputMode &  M01_Common.DdlOutputMode.edomValueNonLrt)) {
// ### IF IVK ###
if (!(M03_Config.hasBeenSetProductiveInPrivLrt)) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, M01_LDM.gc_dbFalse, null, true, null, null), null, null);
}
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Flag 'isDeleted'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsDeleted, M01_ACM_IVK.conIsDeleted, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, M01_LDM.gc_dbFalse, null, true, null, null), null, null);
}
// ### ENDIF IVK ###
} else if (!(forLrt |  (outputMode &  (M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomDeclNonLrt)))) {
// ### IF IVK ###
if (!(M03_Config.hasBeenSetProductiveInPrivLrt)) {
M22_Class_Utilities.printSectionHeader("Flag 'hasBeenSetProductive'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conHasBeenSetProductive, M01_ACM_IVK.cosnHasBeenSetProductive, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt ? "" : "NOT NULL DEFAULT " + (useAlternativeDefaults ? 1 : 0)), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record has been set productive", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
if (!(condenseData)) {
M22_Class_Utilities.printSectionHeader("Flag 'isDeleted'", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conIsDeleted, M01_ACM_IVK.cosnIsDeleted, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, (forLrt ? "" : "NOT NULL DEFAULT 0"), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, "[LRT] Specifies whether record logically has been deleted", M01_LDM.gc_dbFalse, null, null, null, null), null, null);
}
// ### ENDIF IVK ###
}

// columns which exist in private and not in public tables
if (!(forLrt &  (outputMode &  M01_Common.DdlOutputMode.edomValueLrt))) {
M22_Class_Utilities.printSectionHeader("LRT - Status (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])", fileNo, M01_Common.DdlOutputMode.edomValueLrt, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLrtState, M01_ACM.cosnLrtState, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (!(forLrtMqt) ? "NOT NULL" : ""), null, ddlType, null, M01_Common.DdlOutputMode.edomValueLrt, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, forLrtMqt, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
//           edomValueLrt, eacLrtMeta, , indent, , , , forLrtMqt _
//         )
// ### ENDIF IVK ###
} else if (forLrt |  (outputMode &  (M01_Common.DdlOutputMode.edomListLrt |  M01_Common.DdlOutputMode.edomDeclLrt))) {
M22_Class_Utilities.printSectionHeader("LRT - Status (locked[" + M11_LRT.lrtStatusLocked + "], created[" + M11_LRT.lrtStatusCreated + "], updated[" + M11_LRT.lrtStatusUpdated + "], deleted[" + M11_LRT.lrtStatusDeleted + "])", fileNo, outputMode, null);
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLrtState, M01_ACM.cosnLrtState, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtStatus, transformation, tabColumns, acmEntityType, acmEntityIndex, (!(forLrtMqt) ? "NOT NULL" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLrtMeta, null, indent, null, null, null, null, forLrtMqt, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( '
//           conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
//           tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
//           outputMode, eacLrtMeta, , indent, , , , forLrtMqt _
//         )
// ### ENDIF IVK ###
}
}

// determine primary key attribute(s) of this table
if (!(qualTabName.compareTo("") == 0)) {
M22_Class_Utilities.printSectionHeader("Foreign Key to 'Parent Table' (" + qualTabName + ")", fileNo, outputMode, null);
}

if (!(useSurrogateKey &  parentTabPkAttrDecl != "")) {
M00_FileWriter.printToFile(fileNo, parentTabPkAttrDecl);
}

tabAttrList = parentTabPkAttrList;
pkAttrList = parentTabPkAttrList;
if (useSurrogateKey &  acmEntityType != M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
tabAttrList = M04_Utilities.genSurrogateKeyName(ddlType, classShortName, null, null, null, null);
pkAttrList = M01_Globals.g_anOid;
if (M03_Config.reuseColumnsInTabsForOrMapping) {
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M04_Utilities.genSurrogateKeyName(ddlType, classShortName, null, null, null, null), M04_Utilities.genSurrogateKeyShortName(ddlType, classShortName, null), M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  (isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (supportPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0) | M01_Common.AttrCategory.eacFkOidParent, null, indent, null, null, null, null, null, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           genSurrogateKeyName(ddlType, classShortName), genSurrogateKeyShortName(ddlType, classShortName), _
//           eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
//           eacFkOid Or eacFkOidParent, , indent _
//         )
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conOid, M01_ACM.cosnOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  (isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (supportPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0) | M01_Common.AttrCategory.eacFkOidParent, null, indent, null, null, null, null, null, null, null), null, null);
// ### ELSE IVK ###
//       printConditional fileNo, _
//         genTransformedAttrDeclByDomainWithColReUse( _
//           conOid, cosnOid, eavtDomain, g_domainIndexOid, _
//           transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
//           eacFkOid Or eacFkOidParent, , indent _
//         )
// ### ENDIF IVK ###
}
} else {
int j;
for (j = 1; j <= 1; j += (1)) {
if (M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[j].refIndex].isIdentifying) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[j].refIndex].attributeName, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[j].refIndex].shortName, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[j].refIndex].valueType, M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[j].refIndex].valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, null, null, indent, null, null, null, null, null, null, null), null, null);
}
}
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].useSurrogateKey) {
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName, null, null, null, null), M04_Utilities.genSurrogateKeyShortName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].shortName, null), M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex].supportExtendedPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0), null, indent, null, null, null, null, null, null, null), null, null);
// ### ELSE IVK ###
//           printConditional fileNo, _
//             genTransformedAttrDeclByDomainWithColReUse( _
//               genSurrogateKeyName(ddlType, .shortName), genSurrogateKeyShortName(ddlType, .shortName), _
//               eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
//               eacFkOid, , indent _
//             )
// ### ENDIF IVK ###
} else {
;
}
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].useSurrogateKey) {
// ### IF IVK ###
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null, null, null, null), M04_Utilities.genSurrogateKeyShortName(ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].shortName, null), M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacFkOid |  (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].isPsForming ? M01_Common.AttrCategory.eacPsFormingOid : 0) | (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[acmEntityIndex].rightEntityIndex].supportExtendedPsCopy ? M01_Common.AttrCategory.eacFkExtPsCopyOid : 0), null, indent, null, null, null, null, null, null, null), null, null);
// ### ELSE IVK ###
//           printConditional fileNo, _
//             genTransformedAttrDeclByDomainWithColReUse( _
//               genSurrogateKeyName(ddlType, .shortName), genSurrogateKeyShortName(ddlType, .shortName), _
//               eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
//               eacFkOid, , indent _
//             )
// ### ENDIF IVK ###
} else {
;
}
}

if (M03_Config.includeFksInPks) {
for (int j = 1; j <= relRefs.numRefs; j++) {
if (relRefs.refs[j].refType == M01_Common.RelNavigationDirection.etRight) {
if (M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].isIdentifyingLeft &  M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].maxLeftCardinality == 1) {
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].leftEntityIndex, M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].useRlLdmRelName ? M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].rlLdmRelName : M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].shortName + M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].rlShortRelName), !(M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].useRlLdmRelName), M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].isNationalizable, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent, null);
// ### ELSE IVK ###
//               genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
//                 .relIndex, IIf(.useRlLdmRelName, .rlLdmRelName, .shortName & .rlShortRelName), _
//                 Not .useRlLdmRelName, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
// ### ENDIF IVK ###
}
} else if (relRefs.refs[j].refType == M01_Common.RelNavigationDirection.etLeft) {
if (M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].isIdentifyingRight &  M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].maxRightCardinality == 1) {
// ### IF IVK ###
M24_Attribute.genFkTransformedAttrDeclsForRelationshipWithColReUse(M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].rightEntityIndex, M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].relIndex, (M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].useLrLdmRelName ? M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].lrLdmRelName : M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].shortName + M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].lrShortRelName), !(M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].useLrLdmRelName), M23_Relationship.g_relationships.descriptors[relRefs.refs[j].refIndex].isNationalizable, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent, null);
// ### ELSE IVK ###
//               genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
//                 .relIndex, IIf(.useLrLdmRelName, .lrLdmRelName, .shortName & .lrShortRelName), _
//                 Not .useLrLdmRelName, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
// ### ENDIF IVK ###
}
}
}
}

tabAttrList = tabAttrList + (tabAttrList.compareTo("") == 0 ? "" : ",") + M24_Attribute.getPkAttrListByClass(acmEntityIndex, ddlType, null, forLrt, null, null);
pkAttrList = pkAttrList + (pkAttrList.compareTo("") == 0 ? "" : ",") + M24_Attribute.getPkAttrListByClass(acmEntityIndex, ddlType, null, forLrt, null, null);
}

M22_Class_Utilities.printSectionHeader("Language Id", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conLanguageId, M01_ACM.cosnLanguageId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M01_Globals_IVK.g_enumIndexLanguage, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, M01_Common.AttrCategory.eacLangId, null, indent, null, null, null, null, null, null, null), null, null);
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
M22_Class_Utilities.printSectionHeader("REF Id", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conEnumRefId, M01_ACM.cosnEnumRefId, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId, M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, null, null, indent, null, null, null, null, null, null, null), null, null);
M22_Class_Utilities.printSectionHeader("LABEL", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conEnumLabelText, M01_ACM.cosnEnumLabelText, M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue, M21_Enum.g_enums.descriptors[acmEntityIndex].enumIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", null, ddlType, null, outputMode, null, null, indent, null, null, null, null, null, null, null), null, null);
}

for (i = 1; i <= 1; i += (1)) {
if (onlyThisAttribute == -1 |  (onlyThisAttribute == nlAttrRefs.descriptors[i].refIndex)) {
// ### IF IVK ###
if (hasNoIdentity |  (M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].isTimeVarying == forGen)) {
// ### ELSE IVK ###
//       If .isTimeVarying = forGen Then
// ### ENDIF IVK ###
// ### IF IVK ###
M22_Class_Utilities.printSectionHeader("NL-Text Attribute (" + M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].attributeName + "@" + M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].className + ")", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].attributeName, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].shortName, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, (forceNotNull |  (numNlAttrs > 1) ? "" : "NOT NULL"), null, ddlType, null, outputMode, null, null, indent, null, null, null, null, null, nlAttrRefs.descriptors[i].refIndex, null), null, null);
// ### ELSE IVK ###
//         printConditional fileNo, _
//           genTransformedAttrDeclByDomainWithColReUse( _
//             .attributeName, .shortName, .valueType, .valueTypeIndex, transformation, tabColumns, _
//             acmEntityType, acmEntityIndex, IIf(forceNotNull Or (numNlAttrs > 1), "", "NOT NULL"), , _
//             ddlType, , outputMode, , , indent, , , , , nlAttrRefs.descriptors(i).refIndex)
// ### ENDIF IVK ###

// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].isNationalizable) {
M22_Class_Utilities.printSectionHeader("nationalized NL-Text Attribute (" + M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].attributeName + ")", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].attributeName + M01_Globals_IVK.gc_anSuffixNat, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].shortName + M01_Globals_IVK.gc_asnSuffixNat, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, null, null, ddlType, null, outputMode, null, null, indent, null, null, null, null, null, nlAttrRefs.descriptors[i].refIndex, null), null, null);

M22_Class_Utilities.printSectionHeader("Is nationalized Text active?", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].attributeName + M01_Globals_IVK.gc_anSuffixNatActivated, M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[i].refIndex].shortName + M01_Globals_IVK.gc_asnSuffixNatActivated, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), null, ddlType, null, outputMode, M01_Common.AttrCategory.eacNationalBool, null, indent, null, null, M01_LDM.gc_dbFalse, null, null, nlAttrRefs.descriptors[i].refIndex, null), null, null);
}
// ### ENDIF IVK ###
}
}
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
for (i = 1; i <= 1; i += (1)) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(M22_Class.g_classes.descriptors[acmEntityIndex].subclassIndexes[i], M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, onlyThisAttribute, forceNotNull |  numNlAttrs > 1, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, false, outputMode, qualTabName, null, null, null, null, useAlternativeDefaults);
}
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
for (i = 1; i <= 1; i += (1)) {
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusingRelIndexes.indexes[i], M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, tabColumns, fileNo, onlyThisAttribute, forceNotNull |  numNlAttrs > 1, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, false, outputMode, qualTabName, null, null, null, null, useAlternativeDefaults);
}
}

if (includeMetaAttrs) {
// ### IF IVK ###
if (isPsTagged &  M03_Config.usePsTagInNlTextTables & !noRangePartitioning) {
M22_Class_Utilities.printSectionHeader("Product Structure Tag", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conPsOid, M01_ACM_IVK.cosnPsOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, (psTagOptional ? "" : "NOT NULL"), true, ddlType, null, outputMode, M01_Common.AttrCategory.eacPsOid, null, indent, null, "[LDM] Product Structure Tag", null, null, null, null, null), null, null);
} else {
if (className.compareTo("GenericCode") == 0) {
M22_Class_Utilities.printSectionHeader("Division column", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM_IVK.conDivOid, M01_ACM_IVK.cosnDivOid, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0", useVersiontag, ddlType, null, outputMode, M01_Common.AttrCategory.eacDivOid, null, indent, null, "[LDM] Division Tag", null, null, null, null, null), null, null);
}
}

// ### ENDIF IVK ###
if (M03_Config.g_cfgGenLogChangeForNlTabs) {
M24_Attribute.genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, acmEntityType, acmEntityIndex, ddlType, className, outputMode, null, null, useAlternativeDefaults);
}

if (useVersiontag) {
M22_Class_Utilities.printSectionHeader("Object Version ID", fileNo, outputMode, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(M01_ACM.conVersionId, M01_ACM.cosnVersionId, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexVersion, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 1" + (ddlType == M01_Common.DdlTypeId.edtPdm &  M03_Config.dbCompressSystemDefaults ? " COMPRESS SYSTEM DEFAULT" : ""), false, ddlType, null, outputMode, M01_Common.AttrCategory.eacVid, null, indent, null, null, "1", null, null, null, null), null, null);
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genNlsSingleTabForEntity(int rootAcmEntityIndex, int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoFk, int fileNoLrtFk, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, String parentTabPkAttrListW, String parentTabPkAttrDeclW, Boolean useAlternativeDefaultsW) {
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

String parentTabPkAttrList; 
if (parentTabPkAttrListW == null) {
parentTabPkAttrList = "";
} else {
parentTabPkAttrList = parentTabPkAttrListW;
}

String parentTabPkAttrDecl; 
if (parentTabPkAttrDeclW == null) {
parentTabPkAttrDecl = "";
} else {
parentTabPkAttrDecl = parentTabPkAttrDeclW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

String sectionName;
String sectionShortName;
int sectionIndex;
String className;
String classShortName;
boolean isUserTransactional;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean M03_Config.useMqtToImplementLrt;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
boolean notAcmRelated;
boolean noAlias;
boolean useSurrogateKey;
boolean useVersiontag;
int tabSpaceIndexData;
int tabSpaceIndexIndex;
boolean isCtoAliasCreated;
String nlObjName;
String nlObjShortName;
// ### IF IVK ###
boolean isSubjectToArchiving;
boolean isPsTagged;
boolean psTagOptional;
Integer tabPartitionType;
boolean noRangePartitioning;
boolean rangePartitioningAll;
// ### ENDIF IVK ###
boolean M72_DataPool.poolSupportLrt;
boolean poolCommonItemsLocal;
boolean poolSuppressRefIntegrity;

//On Error GoTo ErrorExit 

if (thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
poolCommonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
poolSuppressRefIntegrity = M72_DataPool.g_pools.descriptors[thisPoolIndex].suppressRefIntegrity;
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
className = M22_Class.g_classes.descriptors[acmEntityIndex].className;
classShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;
notAcmRelated = M22_Class.g_classes.descriptors[acmEntityIndex].notAcmRelated;
noAlias = M22_Class.g_classes.descriptors[acmEntityIndex].noAlias;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;
useVersiontag = M22_Class.g_classes.descriptors[acmEntityIndex].useVersiontag;
tabSpaceIndexData = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexNl;
tabSpaceIndexIndex = M22_Class.g_classes.descriptors[acmEntityIndex].tabSpaceIndexIndex;
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
// ### IF IVK ###
isSubjectToArchiving = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToArchiving;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
noRangePartitioning = M22_Class.g_classes.descriptors[acmEntityIndex].noRangePartitioning;
rangePartitioningAll = M22_Class.g_classes.descriptors[acmEntityIndex].rangePartitioningAll;
// ### ENDIF IVK ###

nlObjName = M04_Utilities.genNlObjName(className, null, forGen, null);
nlObjShortName = M04_Utilities.genNlObjShortName(classShortName, null, forGen, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
className = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
classShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
notAcmRelated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].notAcmRelated;
noAlias = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noAlias;
useSurrogateKey = M03_Config.useSurrogateKeysForNMRelationships &  (M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs.numDescriptors > 0 |  M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange);
useVersiontag = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useVersiontag;
tabSpaceIndexData = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexNl;
tabSpaceIndexIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].tabSpaceIndexIndex;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
// ### IF IVK ###
isSubjectToArchiving = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToArchiving;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = false;
noRangePartitioning = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noRangePartitioning;
rangePartitioningAll = false;
// ### ENDIF IVK ###

nlObjName = M04_Utilities.genNlObjName(className, null, forGen, null);
nlObjShortName = M04_Utilities.genNlObjShortName(classShortName, null, forGen, null);
}

boolean genSupportForLrt;
genSupportForLrt = false;
if (M01_Globals.g_genLrtSupport &  isUserTransactional) {
if (thisPoolIndex > 0) {
genSupportForLrt = M72_DataPool.poolSupportLrt;
} else {
genSupportForLrt = ddlType == M01_Common.DdlTypeId.edtLdm;
}
}

String qualNlTabName;
String qualNlTabNameLdm;
String qualTabName;
String qualTabNameLdm;
String qualLangTabName;
String qualIndexName;
String pkAttrList;
String tabAttrList;

qualNlTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, true, null, null, null);
qualNlTabNameLdm = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, true, null, null, null);

M96_DdlSummary.addTabToDdlSummary(qualNlTabName, ddlType, notAcmRelated);
M78_DbMeta.registerQualTable(qualNlTabNameLdm, qualNlTabName, rootAcmEntityIndex, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, ddlType, notAcmRelated, forGen, forLrt, true, null);

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
qualTabName = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null, null);
qualTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
qualTabName = M04_Utilities.genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, null, null);
qualTabNameLdm = M04_Utilities.genQualTabNameByRelIndex(acmEntityIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forLrt, null, null, null, null, null);
}

if (M03_Config.generateDdlCreateTable) {
M22_Class_Utilities.printChapterHeader("NL-Table for ACM-" + (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass ? "Class" : "Relationship") + " \"" + sectionName + "." + className + "\"" + (forLrt ? " (LRT)" : ""), fileNo);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + qualNlTabName);
M00_FileWriter.printToFile(fileNo, "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, qualTabName, null, ddlType, thisOrgIndex, thisPoolIndex, null, forGen, forLrt, (forLrt ? M01_Common.DdlOutputMode.edomDeclLrt : M01_Common.DdlOutputMode.edomDeclNonLrt), null, parentTabPkAttrList, parentTabPkAttrDecl, pkAttrList, tabAttrList, useAlternativeDefaults);

M00_FileWriter.printToFile(fileNo, ")");

boolean isDivTagged;
isDivTagged = (acmEntityIndex == M01_Globals_IVK.g_classIndexGenericCode);

// ### IF IVK ###
M22_Class.genTabDeclTrailer(fileNo, ddlType, isDivTagged, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, true, forLrt, false, false, (isDivTagged ? M01_ACM_IVK.conDivOid : ""), tabPartitionType);
// ### ELSE IVK ###
//   genTabDeclTrailer fileNo, ddlType, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, True, forLrt, False
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if (forLrt &  M03_Config.lrtTablesVolatile) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE " + qualNlTabName + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
// ### IF IVK ###
} else if (!(isCommonToPools & ! poolCommonItemsLocal & !notAcmRelated & (!(M72_DataPool.poolSupportLrt | ! M03_Config.useMqtToImplementLrt)) & !isPsTagged)) {
// ### ELSE IVK ###
// ElseIf Not isCommonToPools And Not poolCommonItemsLocal And Not notAcmRelated And (Not poolSupportLrt Or Not useMqtToImplementLrt) Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE " + qualNlTabName + " VOLATILE CARDINALITY" + M01_LDM.gc_sqlCmdDelim);
}

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! noAlias) {
// ### IF IVK ###
M22_Class.genAliasDdl(sectionIndex, nlObjName, isCommonToOrgs, isCommonToPools, !(notAcmRelated), qualNlTabNameLdm, qualNlTabName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatTable, false, forLrt, false, false, false, "NL-Table \"" + sectionName + "." + nlObjName + "\"", null, isUserTransactional, false, null, isSubjectToArchiving, null, null, null);
// ### ELSE IVK ###
//   genAliasDdl sectionIndex, nlObjName, isCommonToOrgs, isCommonToPools, Not notAcmRelated, _
//               qualNlTabNameLdm, qualNlTabName, isCtoAliasCreated, ddlType, thisOthisOrgIndexrgId, thisPoolIndex, edatTable, False, forLrt, _
//               "NL-Table """ & sectionName & "." & nlObjName & """", , isUserTransactional
// ### ENDIF IVK ###
}

// DDL for Primary Key
String pkName;
String uiName;
String ukName;
pkName = M04_Utilities.genPkName(M01_LDM.tabPrefixNl + classShortName + "NLT".toUpperCase(), M01_LDM.tabPrefixNl + classShortName + "NLT".toUpperCase(), ddlType, thisOrgIndex, thisPoolIndex, false, forLrt);
ukName = "UK_" + pkName.substring(4 - 1, 4 + pkName.length() - 1);

uiName = M04_Utilities.genUkName(sectionIndex, M01_LDM.tabPrefixNl + classShortName + (forGen ? "G" : "") + "NLT", M01_LDM.tabPrefixNl + classShortName + (forGen ? "G" : "") + "NLT", ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, null);

boolean useSurrogateKeysForNlTabs;
useSurrogateKeysForNlTabs = true;

if (useSurrogateKeysForNlTabs &  useSurrogateKey) {
if (M03_Config.generateDdlCreatePK) {
M22_Class_Utilities.printSectionHeader("Primary Key for \"" + qualNlTabName + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anOid + (M01_Globals.g_genLrtSupport &  forLrt ? "," + M01_Globals.g_anInLrt + "," + M01_Globals.g_anLrtState : "") + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (thisPoolIndex == 2 & ! isCommonToPools & !isCommonToOrgs & !noRangePartitioning & rangePartitioningAll & !forLrt) {
//If thisPoolIndex = 2 And isPsTagged And Not noRangePartitioning And rangePartitioningAll And Not forLrt Then
//If g_pools.descriptors(thisPoolIndex).id = 1 And isPsTagged And Not noRangePartitioning And rangePartitioningAll And Not forLrt Then

boolean isLeftPs;
boolean isRightPs;
boolean additionalUK;
int i;
for (i = 1; i <= 1; i += (1)) {
isLeftPs = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].isPsTagged;
isRightPs = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].isPsTagged;
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == -1) {
//g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(className) == 0 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == -1) {
//g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].leftClassSectionName.compareTo(sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].leftClassName.compareTo(className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1) {
//g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
additionalUK = true;
break;
}
if (M23_Relationship.g_relationships.descriptors[i].rightClassSectionName.compareTo(sectionName) == 0 &  M23_Relationship.g_relationships.descriptors[i].rightClassName.compareTo(className) == 0 & M23_Relationship.g_relationships.descriptors[i].minLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].minRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[i].maxRightCardinality == 1) {
//g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
additionalUK = true;
break;
}
}

if (additionalUK) {
M22_Class_Utilities.printSectionHeader("Unique Constraint for \"" + qualNlTabName + "\"", fileNo, null, null);

String columnName;
columnName = (isLeftPs |  isRightPs ? M01_ACM_IVK.conPsOid : M01_ACM_IVK.conDivOid);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE UNIQUE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genQualUkName(sectionIndex, className, ukName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName + "(" + M01_Globals.g_anOid + (M01_Globals.g_genLrtSupport &  forLrt ? "," + M01_Globals.g_anInLrt + "," + M01_Globals.g_anLrtState : "") + ", " + columnName + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

//Print #fileNo, addTab(0); "ALTER TABLE"
//Print #fileNo, addTab(1); qualNlTabName
//Print #fileNo, addTab(0); "ADD CONSTRAINT"
//Print #fileNo, addTab(1); ukName
//Print #fileNo, addTab(1); "UNIQUE (" & g_anOid & IIf(g_genLrtSupport And forLrt, "," & g_anInLrt & "," & g_anLrtState, "") & ", "; columnName; ")"
//Print #fileNo, gc_sqlCmdDelim
}
}

}

if (M03_Config.generateDdlCreateIndex) {
if (M99_IndexException_Utilities.indexExcp(uiName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + uiName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
// ### IF IVK ###

String additionalColumnName;
if (isPsTagged & ! noRangePartitioning & M03_Config.usePsTagInNlTextTables) {
additionalColumnName = M01_Globals_IVK.g_anPsOid;
} else if (className.compareTo(M01_ACM_IVK.clnGenericCode) == 0) {
additionalColumnName = M01_Globals_IVK.g_anDivOid;
} else {
additionalColumnName = "";
}


M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName + "(" + (!(additionalColumnName.compareTo("") == 0) ? additionalColumnName + "," : "") + tabAttrList.toUpperCase() + (tabAttrList.compareTo("") == 0 ? "" : ",") + M01_Globals.g_anLanguageId + (M01_Globals.g_genLrtSupport &  forLrt ? "," + M01_Globals.g_anInLrt : "") + ")");
// ### ELSE IVK ###
//     Print #fileNo, addTab(1); qualNlTabName; "("; _
//                               UCase(tabAttrList); IIf(tabAttrList = "", "", ","); _
//                               g_anLanguageId; _
//                               IIf(g_genLrtSupport And forLrt, "," & g_anInLrt, ""); _
//                               ")"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
} else {
if (M03_Config.generateDdlCreatePK) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + pkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "PRIMARY KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + tabAttrList.toUpperCase() + (tabAttrList.compareTo("") == 0 ? "" : ", ") + M01_Globals.g_anLanguageId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}
}

if (!(forLrt)) {
// DDL for Foreign Key to 'Parent Table'
if (!(poolSuppressRefIntegrity)) {
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"NL-Parent Table\" (" + qualNlTabName + " -> " + qualTabName + ")", fileNoFk, null, null);

M00_FileWriter.printToFile(fileNoFk, "");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + M04_Utilities.genFkName(classShortName + "NLPAR", classShortName + "NLPAR", "", ddlType, thisOrgIndex, thisPoolIndex, false, forLrt));
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "FOREIGN KEY");
if (sectionName.compareTo(M01_ACM.snDbMeta) == 0) {
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + "(" + tabAttrList.toUpperCase() + ")");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + qualTabName + " (" + pkAttrList.toUpperCase() + ")");
} else {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex, "", thisPoolIndex, ddlType, tabAttrList, null, null, M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt, null) + ")");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + qualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex, thisPoolIndex, ddlType, pkAttrList, M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr, M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt, null) + ")");
} else {
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(acmEntityIndex, "", thisPoolIndex, ddlType, tabAttrList, null, null, null, null) + ")");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNoFk, M04_Utilities.addTab(1) + qualTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(acmEntityIndex, thisPoolIndex, ddlType, pkAttrList, M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr, null, null) + ")");
}
}
M00_FileWriter.printToFile(fileNoFk, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualNlTabNameLdm, qualTabNameLdm, acmEntityIndex, acmEntityType, null, null, null);
}

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk & M03_Config.generateDdlCreateIndex) {
qualIndexName = M04_Utilities.genQualIndexName(sectionIndex, className + (forGen ? "G" : "") + "PAR", classShortName + (forGen ? "G" : "") + "PAR", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabAttrList.toUpperCase());
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}
// ### IF IVK ###

if (!(forLrt & ! poolSuppressRefIntegrity)) {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.genFKsForPsTagOnClass(qualNlTabName, qualNlTabNameLdm, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, null, forGen, true, tabPartitionType);
M24_Attribute.genFKsForDivTagOnClass(qualNlTabName, qualNlTabNameLdm, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, null, forGen, true, tabPartitionType);
}
}
// ### ENDIF IVK ###

if (!(poolSuppressRefIntegrity)) {
// DDL for Foreign Key to 'Language Table'
if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Language Table\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M04_Utilities.genFkName(classShortName + "NLLAN", classShortName + "NLLAN", "", ddlType, thisOrgIndex, thisPoolIndex, false, forLrt));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M01_Globals.g_anLanguageId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameLanguage + "(" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualNlTabNameLdm, M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexLanguage, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null), acmEntityIndex, acmEntityType, null, null, null);
}

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForNLang & M03_Config.generateDdlCreateIndex) {
qualIndexName = M04_Utilities.genQualIndexName(sectionIndex, className + "LAN", classShortName + "LAN", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

if (genSupportForLrt & ! poolSuppressRefIntegrity) {
// ### IF IVK ###
M24_Attribute.genFksForLrtByEntity(qualNlTabName, qualNlTabNameLdm, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoLrtFk, ddlType, forGen, forLrt, "NLT", tabPartitionType);
// ### ELSE IVK ###
//   genFksForLrtByEntity qualNlTabName, qualNlTabNameLdm, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoLrtFk, ddlType, forGen, forLrt, "NLT"
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genNlsTabsForClassRecursive(int rootClassIndex, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoFk, int fileNoLrtFk, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, Boolean useAlternativeDefaultsW) {
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

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

//On Error GoTo ErrorExit 

genNlsSingleTabForEntity(rootClassIndex, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoLrtFk, ddlType, forGen, forLrt, null, null, useAlternativeDefaults);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genNlsTabsForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, int fileNoFk, int fileNoLrtFk, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, String parentTabPkAttrListW, String parentTabPkAttrDeclW, Boolean useAlternativeDefaultsW) {
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

String parentTabPkAttrList; 
if (parentTabPkAttrListW == null) {
parentTabPkAttrList = "";
} else {
parentTabPkAttrList = parentTabPkAttrListW;
}

String parentTabPkAttrDecl; 
if (parentTabPkAttrDeclW == null) {
parentTabPkAttrDecl = "";
} else {
parentTabPkAttrDecl = parentTabPkAttrDeclW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

//On Error GoTo ErrorExit 

genNlsSingleTabForEntity(thisRelIndex, thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoLrtFk, ddlType, forGen, forLrt, null, null, useAlternativeDefaults);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genEnumFKsForClassRecursiveWithColReUse(String qualTabName, String qualTabNameLdm, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlType, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, boolean forGen, boolean parentHasNoIdentity, int level, Integer tabPartitionTypeW) {
Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genEnumFKsForClassRecursiveWithColReUse( _
// ByRef qualTabName As String, _
// ByRef qualTabNameLdm As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// ddlType As DdlTypeId, _
// ByRef tabColumns As EntityColumnDescriptors, _
// forGen As Boolean, _
// level As Integer _
//)
// ### ENDIF IVK ###
String sectionName;
int sectionIndex;
String className;
String classShortName;
boolean classIsUserTransactional;
// ### IF IVK ###
boolean classHasNoIdentity;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (M22_Class.g_classes.descriptors[classIndex].noFks) {
return;
}

sectionName = M22_Class.g_classes.descriptors[classIndex].sectionName;
sectionIndex = M22_Class.g_classes.descriptors[classIndex].sectionIndex;
className = M22_Class.g_classes.descriptors[classIndex].className;
classShortName = M22_Class.g_classes.descriptors[classIndex].shortName;
// ### IF IVK ###
classHasNoIdentity = parentHasNoIdentity |  M22_Class.g_classes.descriptors[classIndex].hasNoIdentity;
// ### ENDIF IVK ###
classIsUserTransactional = M22_Class.g_classes.descriptors[classIndex].isUserTransactional;

String qualEnumTabName;
String qualEnumTabNameLdm;
String db2AttrName;
int colIndex;
boolean isReused;

int i;
for (i = 1; i <= 1; i += (1)) {
if (M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtEnum) {
if (!(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].reusedAttrIndex > 0)) {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].sectionName.toUpperCase() == sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].className.toUpperCase() == className.toUpperCase() & (classHasNoIdentity ? !(forGen) : M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].isTimeVarying == forGen) & (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum)) {
// ### ELSE IVK ###
//          If UCase(.sectionName) = UCase(sectionName) And UCase(.className) = UCase(className) And _
//              (.isTimeVarying = forGen) And (.valueType = eavtEnum) Then
// ### ENDIF IVK ###
int effectiveDomainIndex;
if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtDomain) {
effectiveDomainIndex = M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].domainIndex;
} else if (M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].domainIndexId;
}

qualEnumTabName = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].enumIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);
qualEnumTabNameLdm = M04_Utilities.genQualTabNameByEnumIndex(M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].enumIndex, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null);

// DDL for Foreign Key to 'Enum Table'
if (level <= 1 |  M03_Config.reuseColumnsInTabsForOrMapping) {
db2AttrName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName + M01_Globals.gc_enumAttrNameSuffix, ddlType, null, null, null, null, null, null);
} else {
db2AttrName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName + M01_Globals.gc_enumAttrNameSuffix, ddlType, className, classShortName, null, null, null, null);
}

colIndex = M24_Attribute_Utilities.findColumnToUse(tabColumns, db2AttrName, className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueType, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex, isReused, M01_Common.AttrCategory.eacFkOid, null, null, null, null, null);

// ### IF IVK ###
if (!(isReused &  (ddlType == M01_Common.DdlTypeId.edtLdm |  (!((M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName.toUpperCase() == M01_ACM_IVK.enStatus.toUpperCase() &  classIsUserTransactional)))))) {
String fkName;
fkName = M04_Utilities.genFkName(className, classShortName, M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null);

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Enum Table\" on \"" + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName + "@" + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].className + "\" (" + M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].sectionName + "." + M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].enumName + ")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + db2AttrName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualEnumTabName + " (" + M01_Globals.g_anEnumId + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualEnumTabNameLdm, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].valueTypeIndex].notAcmRelated, null, null);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFk & !M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].isIdentifying & M03_Config.generateDdlCreateIndex) {
String qualIndexName;
qualIndexName = M04_Utilities.genQualIndexName(sectionIndex, className + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].attributeName, classShortName + M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[i].refIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + db2AttrName + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}
// ### ENDIF IVK ###
}
}
}
}

for (i = 1; i <= 1; i += (1)) {
// ### IF IVK ###
M24_Attribute.genEnumFKsForClassRecursiveWithColReUse(qualTabName, qualTabNameLdm, M22_Class.g_classes.descriptors[classIndex].subclassIndexes[i], thisOrgIndex, thisPoolIndex, fileNo, ddlType, tabColumns, forGen, classHasNoIdentity, level + 1, tabPartitionType);
// ### ELSE IVK ###
//    genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, .subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, _
//        ddlType, tabColumns, forGen, level + 1
// ### ENDIF IVK ###
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ### IF IVK ###
public static void genEnumFKsForClassRecursive(String qualTabName, String qualTabNameLdm, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlType, boolean forGen, boolean parentHasNoIdentity, int level, Integer tabPartitionTypeW) {
Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

//On Error GoTo ErrorExit 

M24_Attribute.genEnumFKsForClassRecursiveWithColReUse(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, tabColumns, forGen, parentHasNoIdentity, level, tabPartitionType);

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### ELSE IVK ###
//Sub genEnumFKsForClassRecursive( _
// ByRef qualTabName As String, _
// ByRef qualTabNameLdm As String, _
// ByRef classIndex As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// ddlType As DdlTypeId, _
// forGen As Boolean, _
// level As Integer _
//)
// Dim tabColumns As EntityColumnDescriptors
// tabColumns = nullEntityColumnDescriptors
//
// On Error GoTo ErrorExit
//
// genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, _
//     ddlType, tabColumns, forGen, level
//
//NormalExit:
// On Error Resume Next
// Exit Sub
//
//ErrorExit:
// errMsgBox Err.description
// Resume NormalExit
//End Sub
// ### ENDIF IVK ###
// ### IF IVK ###


public static void genFKsForPsTagOnClass(String qualTabName, String qualTabNameLdm, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forLrtW, Boolean forMqtW, Boolean forGenW, Boolean forNlW, Integer tabPartitionTypeW) {
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

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
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

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// DDL for Foreign Key to 'ProductStructure Table'
String fkName;
String qualTabNameProductStructureLdm;

//On Error GoTo ErrorExit 

if (!(M22_Class.g_classes.descriptors[classIndex].isPsTagged |  M22_Class.g_classes.descriptors[classIndex].noFks | (forNl &  M22_Class.g_classes.descriptors[classIndex].noRangePartitioning))) {
return;
}

fkName = M04_Utilities.genFkName(M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, "PS", ddlType, thisOrgIndex, thisPoolIndex, null, null);

qualTabNameProductStructureLdm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexProductStructure, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Product Structure\" Table", fileNo, null, null);
}
if (M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].isCommonToOrgs) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key corresponding to PS-tag for class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" since this class is common to MPCs", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + qualTabName + "\" is common to MPCs");
}
} else if (M22_Class.g_classes.descriptors[classIndex].isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & !M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexProductStructure].isCommonToPools) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key corresponding to PS-tag for class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" since this class is common to Pools", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + qualTabName + "\" is common to pools");
}
} else {
if (M03_Config.generateDdlCreateFK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + qualTabName);
M00_FileWriter.printToFile(fileNo, "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + fkName);
M00_FileWriter.printToFile(fileNo, "FOREIGN KEY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + "(" + M01_Globals_IVK.g_anPsOid + ")");
M00_FileWriter.printToFile(fileNo, "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + M01_Globals_IVK.g_qualTabNameProductStructure + " (" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualTabNameProductStructureLdm, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, null, null, null);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForPsTag & M03_Config.generateDdlCreateIndex) {
String qualIndexName;
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className + "PSO", M22_Class.g_classes.descriptors[classIndex].shortName + "PSO", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anPsOid + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### ENDIF IVK ###

public static void genFKsForDivTagOnClass(String qualTabName, String qualTabNameLdm, int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forLrtW, Boolean forMqtW, Boolean forGenW, Boolean forNlW, Integer tabPartitionTypeW) {
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

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
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

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// DDL for Foreign Key to 'Division Table'
String fkName;
String qualTabNameDivisionLdm;
int aggHeadClassIndex;

//On Error GoTo ErrorExit 

aggHeadClassIndex = M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex;

if (M22_Class.g_classes.descriptors[classIndex].isPsTagged |  M22_Class.g_classes.descriptors[classIndex].noFks | aggHeadClassIndex != M01_Globals_IVK.g_classIndexGenericCode | !forNl) {
return;
}

fkName = M04_Utilities.genFkName(M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, "DIV", ddlType, thisOrgIndex, thisPoolIndex, null, null);
qualTabNameDivisionLdm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexDivision, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

if (M03_Config.generateDdlCreateFK) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Division\" Table", fileNo, null, null);
}
if (M22_Class.g_classes.descriptors[classIndex].isCommonToOrgs &  ddlType == M01_Common.DdlTypeId.edtPdm & !M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexDivision].isCommonToOrgs) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key corresponding to DIV-tag for class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" since this class is common to MPCs", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + qualTabName + "\" is common to MPCs");
}
} else if (M22_Class.g_classes.descriptors[classIndex].isCommonToPools &  ddlType == M01_Common.DdlTypeId.edtPdm & !M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexDivision].isCommonToPools) {
if (M03_Config.generateDdlCreateFK) {
M04_Utilities.logMsg("unable to implement foreign key corresponding to DIV-tag for class \"" + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className + "\" since this class is common to Pools", M01_Common.LogLevel.ellWarning, ddlType, thisOrgIndex, thisPoolIndex);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- unable to implement foreign key since \"" + qualTabName + "\" is common to pools");
}
} else {
if (M03_Config.generateDdlCreateFK) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + qualTabName);
M00_FileWriter.printToFile(fileNo, "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + fkName);
M00_FileWriter.printToFile(fileNo, "FOREIGN KEY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + "(" + M01_Globals_IVK.g_anDivOid + ")");
M00_FileWriter.printToFile(fileNo, "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab + M01_Globals_IVK.g_qualTabNameDivision + " (" + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualTabNameDivisionLdm, classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, null, null, null);

if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateDdlCreateIndex) {
String qualIndexName;
qualIndexName = M04_Utilities.genQualIndexName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className + "DVO", M22_Class.g_classes.descriptors[classIndex].shortName + "DVO", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_anDivOid + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### ENDIF IVK ###


// ### IF IVK ###
public static void genFksForLrtByEntity(String qualTabName, String qualTabNameLdm, int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW, String suffixW, Integer tabPartitionTypeW) {
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

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer tabPartitionType; 
if (tabPartitionTypeW == null) {
tabPartitionType = M94_DBAdmin_Partitioning.PartitionType.ptNone;
} else {
tabPartitionType = tabPartitionTypeW;
}

// ### ELSE IVK ###
//Sub genFksForLrtByEntity( _
// ByRef qualTabName As String, _
// ByRef qualTabNameLdm As String, _
// ByRef acmEntityIndex As Integer, _
// ByRef acmEntityType As AcmAttrContainerType, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ByRef fileNo As Integer, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional ByRef suffix As String = "" _
//)
// ### ENDIF IVK ###
String sectionName;
int sectionIndex;
String className;
String classShortName;
boolean isUserTransactional;
boolean isCommonToOrgs;
boolean isCommonToPools;
int specificToOrgId;
int specificToPool;
boolean isPsTagged;
// ### IF IVK ###
boolean condenseData;
condenseData = false;
// ### ENDIF IVK ###

//On Error GoTo ErrorExit 

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[acmEntityIndex].noFks) {
return;
}

sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
className = M22_Class.g_classes.descriptors[acmEntityIndex].className;
classShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
specificToOrgId = M22_Class.g_classes.descriptors[acmEntityIndex].specificToOrgId;
specificToPool = M22_Class.g_classes.descriptors[acmEntityIndex].specificToPool;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
// ### IF IVK ###
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
className = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
classShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
specificToOrgId = M23_Relationship.g_relationships.descriptors[acmEntityIndex].specificToOrgId;
specificToPool = M23_Relationship.g_relationships.descriptors[acmEntityIndex].specificToPool;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
}

int thisOrgId;
int thisPoolId;
if (thisOrgIndex > 0) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
} else {
thisOrgId = -1;
}

if (thisPoolIndex > 0) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;
} else {
thisPoolId = -1;
}


boolean lrtUseSurogateKey;

if (!((M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToOrgs | ! isCommonToOrgs | thisOrgId == specificToOrgId) | ! (M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].isCommonToPools | ! isCommonToPools | thisPoolId == specificToPool))) {
// we cannot have a foreign key pointing from common to specific pool
return;
}
lrtUseSurogateKey = M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLrt].useSurrogateKey;

String fkName;
String lrtTabName;
String lrtTabNameLdm;

lrtTabName = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
lrtTabNameLdm = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null, null, null, null, null);

// ### IF IVK ###
String qualTabNameLdmStatus;
qualTabNameLdmStatus = M04_Utilities.genQualTabNameByEnumIndex(M01_Globals_IVK.g_enumIndexStatus, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, null, null, null);

// ### ENDIF IVK ###
// Foreign Key on 'InLrt'
fkName = M04_Utilities.genFkName(className, classShortName, "LRT", ddlType, thisOrgIndex, thisPoolIndex, forGen, null);
// ### IF IVK ###
if (M03_Config.generateDdlCreateFK &  (forLrt | ! condenseData)) {
// ### ELSE IVK ###
// If generateDdlCreateFK And forLrt Then
// ### ENDIF IVK ###
M22_Class_Utilities.printSectionHeader("Foreign Key to \"LRT\" Table", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M24_Attribute.getFkSrcAttrSeqExt(M01_Globals.g_classIndexLrt, "", thisPoolIndex, ddlType, M01_Globals.g_anInLrt, isPsTagged, null, null, null) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");

if (lrtUseSurogateKey) {
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + lrtTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(M01_Globals.g_classIndexLrt, thisPoolIndex, ddlType, M01_Globals.g_anOid, null, null, null) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + lrtTabName + " (" + M01_Globals.g_anOid + ")");
}
} else {
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + lrtTabName + " (" + M24_Attribute.getFkTargetAttrSeqExt(M01_Globals.g_classIndexLrt, thisPoolIndex, ddlType, M01_Globals.g_anLrtOid, null, null, null) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + lrtTabName + " (" + M01_Globals.g_anLrtOid + ")");
}
}

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, lrtTabNameLdm, acmEntityIndex, acmEntityType, null, forGen, null);

// Foreign Key on 'Status'
fkName = M04_Utilities.genFkName(className, classShortName, "STA", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt);

// ### IF IVK ###
if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateDdlCreateFK & !condenseData) {
M22_Class_Utilities.printSectionHeader("Foreign Key to \"Status\"-Enumeration Table", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ALTER TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ADD CONSTRAINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + fkName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOREIGN KEY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(" + M04_Utilities.genAttrName(M01_ACM_IVK.enStatus, ddlType, null, null, null, null, null, null) + "_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameStatus + " (ID)");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

M78_DbMeta.registerQualLdmFk(qualTabNameLdm, qualTabNameLdmStatus, acmEntityIndex, acmEntityType, null, forGen, null);
// ### ENDIF IVK ###

// ### IF IVK ###
if ((ddlType == M01_Common.DdlTypeId.edtPdm) &  M03_Config.generateIndexOnFkForLrtId & M03_Config.generateDdlCreateIndex & !condenseData) {
// ### ELSE IVK ###
// If (ddlType = edtPdm) And generateIndexOnFkForLrtId And generateDdlCreateIndex Then
// ### ENDIF IVK ###
String qualIndexName;
qualIndexName = M04_Utilities.genQualIndexName(sectionIndex, className + suffix, classShortName + suffix, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null);

if (M99_IndexException_Utilities.indexExcp(qualIndexName, thisOrgIndex, null) == false) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE INDEX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualIndexName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anInLrt + " ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}// indexExcp
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void evalAttributes() {
int i;
int j;

int relId;
relId = 100;

for (i = 1; i <= 1; i += (1)) {
// determine Domains
M24_Attribute.g_attributes.descriptors[i].domainIndex = -1;
M24_Attribute.g_attributes.descriptors[i].valueTypeIndex = -1;

// ### IF IVK ###
if (!(M24_Attribute.g_attributes.descriptors[i].ftoConflictWith.compareTo("") == 0)) {
String[] elems;
elems = "".split("/");
elems = M24_Attribute.g_attributes.descriptors[i].ftoConflictWith.split("/");

if (M00_Helper.uBound(elems) == 0) {
M24_Attribute.g_attributes.descriptors[i].ftoConflictWithAttrIndex = M24_Attribute.getAttributeIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].ftoConflictWith);
M24_Attribute.g_attributes.descriptors[i].ftoConflictType = -1;
M24_Attribute.g_attributes.descriptors[i].ftoConflictMessageIdBase = -1;
} else {
M24_Attribute.g_attributes.descriptors[i].ftoConflictWithAttrIndex = M24_Attribute.getAttributeIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, elems[0]);
M24_Attribute.g_attributes.descriptors[i].ftoConflictType = M04_Utilities.getInteger(elems[1], null);
if (M00_Helper.uBound(elems) < 2) {
M24_Attribute.g_attributes.descriptors[i].ftoConflictMessageIdBase = -1;
} else {
M24_Attribute.g_attributes.descriptors[i].ftoConflictMessageIdBase = M04_Utilities.getLong(elems[2], null);
}
}
M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[i].ftoConflictWithAttrIndex].ftoConflictWithSrcAttrIndex = i;
}
// ### ENDIF IVK ###
for (int j = 1; j <= M21_Enum.g_enums.numDescriptors; j++) {
if (M24_Attribute.g_attributes.descriptors[i].domainSection.toUpperCase() == M21_Enum.g_enums.descriptors[j].sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[i].domainName.toUpperCase() == M21_Enum.g_enums.descriptors[j].enumName.toUpperCase()) {
if (!(M24_Attribute.g_attributes.descriptors[i].domainSection.compareTo(M21_Enum.g_enums.descriptors[j].sectionName) == 0)) {
M04_Utilities.logMsg("Inconsistent 'casing' for section name \"" + M24_Attribute.g_attributes.descriptors[i].domainSection + "\" used to define attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\"", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
if (!(M24_Attribute.g_attributes.descriptors[i].domainName.compareTo(M21_Enum.g_enums.descriptors[j].enumName) == 0)) {
M04_Utilities.logMsg("Inconsistent 'casing' for enum name \"" + M24_Attribute.g_attributes.descriptors[i].domainName + "\" used to define attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\"", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
M24_Attribute.g_attributes.descriptors[i].valueType = M24_Attribute_Utilities.AttrValueType.eavtEnum;
M24_Attribute.g_attributes.descriptors[i].valueTypeIndex = j;
break;
}
}
if (M24_Attribute.g_attributes.descriptors[i].valueTypeIndex == -1) {
for (int j = 1; j <= M25_Domain.g_domains.numDescriptors; j++) {
if (M24_Attribute.g_attributes.descriptors[i].domainSection.toUpperCase() == M25_Domain.g_domains.descriptors[j].sectionName.toUpperCase() &  M24_Attribute.g_attributes.descriptors[i].domainName.toUpperCase() == M25_Domain.g_domains.descriptors[j].domainName.toUpperCase()) {
if (!(M24_Attribute.g_attributes.descriptors[i].domainSection.compareTo(M25_Domain.g_domains.descriptors[j].sectionName) == 0)) {
M04_Utilities.logMsg("Inconsistent 'casing' for section name \"" + M24_Attribute.g_attributes.descriptors[i].domainSection + "\" used to define attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\"", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
if (!(M24_Attribute.g_attributes.descriptors[i].domainName.compareTo(M25_Domain.g_domains.descriptors[j].domainName) == 0)) {
M04_Utilities.logMsg("Inconsistent 'casing' for domain name \"" + M24_Attribute.g_attributes.descriptors[i].domainName + "\" used to define attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\"", M01_Common.LogLevel.ellFixableWarning, null, null, null);
}
M24_Attribute.g_attributes.descriptors[i].domainIndex = j;
M24_Attribute.g_attributes.descriptors[i].valueType = M24_Attribute_Utilities.AttrValueType.eavtDomain;
M24_Attribute.g_attributes.descriptors[i].valueTypeIndex = j;

// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].isExpression) {
M24_Attribute.g_attributes.descriptors[i].domainIndex = M01_Globals.g_domainIndexOid;
}
// ### ENDIF IVK ###

break;
}
}
if (M24_Attribute.g_attributes.descriptors[i].valueTypeIndex == -1) {
M04_Utilities.logMsg("Unknown domain \"" + M24_Attribute.g_attributes.descriptors[i].domainSection + "." + M24_Attribute.g_attributes.descriptors[i].domainName + "\" used to define attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\"", M01_Common.LogLevel.ellError, null, null, null);
}
}

//was not supported in the past - now allowed for certain cases (one nullable column per unique index)
//If .isNullable And .isIdentifying Then
//  logMsg "Attribute """ & .attributeName & "@" & .sectionName & "." & .className & """ is marked as ""identifying"" and ""nullable""", ellError
//End If

// ### IF IVK ###
int classIndex;
classIndex = -1;
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
classIndex = M22_Class.getClassIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, true);

if (M24_Attribute.g_attributes.descriptors[i].attributeName.compareTo(M01_ACM_IVK.conIsNotPublished) == 0) {
M22_Class.g_classes.descriptors[classIndex].containsIsNotPublished = true;
}
}

if (!(M24_Attribute.g_attributes.descriptors[i].groupIdBasedOn.compareTo("") == 0) &  classIndex > 0) {
M22_Class.addGroupIdAttrIndex(classIndex, i);
M22_Class.g_classes.descriptors[classIndex].hasGroupIdAttrInNonGen = true;
}

// analyze virtual attributes
if (M24_Attribute.g_attributes.descriptors[i].isVirtual) {
// we currently do not fully support expression-based mapping
if (M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.substring(0, 1) == "#") {
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M24_Attribute.g_attributes.descriptors[i].isTimeVarying &  (!(M22_Class.g_classes.descriptors[classIndex].hasNoIdentity))) {
M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInGen = true;
} else {
M22_Class.g_classes.descriptors[classIndex].hasExpBasedVirtualAttrInNonGen = true;
}
}
} else {
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M24_Attribute.g_attributes.descriptors[i].isTimeVarying &  (!(M22_Class.g_classes.descriptors[classIndex].hasNoIdentity))) {
M22_Class.g_classes.descriptors[classIndex].hasRelBasedVirtualAttrInGen = true;
} else {
M22_Class.g_classes.descriptors[classIndex].hasRelBasedVirtualAttrInNonGen = true;
}
}
}
}

// handle attributes marked as 'MDS expression'
if (M24_Attribute.g_attributes.descriptors[i].isExpression) {
if (classIndex <= 0) {
classIndex = M22_Class.getClassIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, null);
}

if (classIndex > 0) {
M22_Class_Utilities.ClassDescriptor class;
class = M22_Class.g_classes.descriptors[classIndex];// just to shorten the following code
relId = M23_Relationship.getMaxRelIdBySection(class.sectionName) + 1;

int relIndex;
relIndex = M23_Relationship_Utilities.allocRelationshipDescriptorIndex(M23_Relationship.g_relationships);

M23_Relationship.g_relationships.descriptors[relIndex].i18nId = "R-" + class.sectionName + "-EXP-" + M24_Attribute.g_attributes.descriptors[i].attributeName;

M23_Relationship.g_relationships.descriptors[relIndex].sectionName = class.sectionName;
M23_Relationship.g_relationships.descriptors[relIndex].relName = class.className + M24_Attribute.g_attributes.descriptors[i].attributeName;
M23_Relationship.g_relationships.descriptors[relIndex].relId = relId;
M23_Relationship.g_relationships.descriptors[relIndex].shortName = M24_Attribute.g_attributes.descriptors[i].shortName;
M23_Relationship.g_relationships.descriptors[relIndex].reuseName = M24_Attribute.g_attributes.descriptors[i].attributeName;
M23_Relationship.g_relationships.descriptors[relIndex].reuseShortName = M24_Attribute.g_attributes.descriptors[i].shortName;
M23_Relationship.g_relationships.descriptors[relIndex].isCommonToOrgs = class.isCommonToOrgs;
M23_Relationship.g_relationships.descriptors[relIndex].specificToOrgId = class.specificToOrgId;
M23_Relationship.g_relationships.descriptors[relIndex].isCommonToPools = class.isCommonToPools;
M23_Relationship.g_relationships.descriptors[relIndex].specificToPool = class.specificToPool;
M23_Relationship.g_relationships.descriptors[relIndex].useValueCompression = class.useValueCompression;
M23_Relationship.g_relationships.descriptors[relIndex].useVersiontag = class.useVersiontag;
M23_Relationship.g_relationships.descriptors[relIndex].notAcmRelated = class.notAcmRelated;
M23_Relationship.g_relationships.descriptors[relIndex].isLrtSpecific = class.isLrtSpecific;
M23_Relationship.g_relationships.descriptors[relIndex].isPdmSpecific = class.isPdmSpecific;
M23_Relationship.g_relationships.descriptors[relIndex].isNotEnforced = false;
M23_Relationship.g_relationships.descriptors[relIndex].isNl = false;
M23_Relationship.g_relationships.descriptors[relIndex].leftClassSectionName = class.sectionName;
M23_Relationship.g_relationships.descriptors[relIndex].leftClassName = class.className;
M23_Relationship.g_relationships.descriptors[relIndex].leftTargetType = M23_Relationship_Utilities.RelRefTargetType.erttRegular;
M23_Relationship.g_relationships.descriptors[relIndex].lrRelName = M24_Attribute.g_attributes.descriptors[i].attributeName + "Expression";
M23_Relationship.g_relationships.descriptors[relIndex].lrShortRelName = "EXP";
M23_Relationship.g_relationships.descriptors[relIndex].lrLdmRelName = M23_Relationship.g_relationships.descriptors[relIndex].lrRelName;
M23_Relationship.g_relationships.descriptors[relIndex].minLeftCardinality = 0;
M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality = -1;
M23_Relationship.g_relationships.descriptors[relIndex].isIdentifyingLeft = false;
M23_Relationship.g_relationships.descriptors[relIndex].useIndexOnLeftFk = M03_Config.generateIndexOnExpressionFks;
M23_Relationship.g_relationships.descriptors[relIndex].ignoreForChangelog = true;
M23_Relationship.g_relationships.descriptors[relIndex].rightClassSectionName = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexExpression].sectionName;
M23_Relationship.g_relationships.descriptors[relIndex].rightClassName = M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexExpression].className;
M23_Relationship.g_relationships.descriptors[relIndex].rightTargetType = M23_Relationship_Utilities.RelRefTargetType.erttRegular;
M23_Relationship.g_relationships.descriptors[relIndex].isMdsExpressionRel = true;
M23_Relationship.g_relationships.descriptors[relIndex].rlRelName = class.className;
M23_Relationship.g_relationships.descriptors[relIndex].rlShortRelName = class.shortName;
M23_Relationship.g_relationships.descriptors[relIndex].rlLdmRelName = M23_Relationship.g_relationships.descriptors[relIndex].rlRelName;
M23_Relationship.g_relationships.descriptors[relIndex].minRightCardinality = 0;
M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality = 1;
M23_Relationship.g_relationships.descriptors[relIndex].isIdentifyingRight = false;
M23_Relationship.g_relationships.descriptors[relIndex].useIndexOnRightFk = M03_Config.generateIndexOnExpressionFks;
M23_Relationship.g_relationships.descriptors[relIndex].isNationalizable = M24_Attribute.g_attributes.descriptors[i].isNationalizable;
M23_Relationship.g_relationships.descriptors[relIndex].isPsForming = class.isPsForming;
M23_Relationship.g_relationships.descriptors[relIndex].logLastChange = class.logLastChange;
M23_Relationship.g_relationships.descriptors[relIndex].isUserTransactional = class.isUserTransactional;
M23_Relationship.g_relationships.descriptors[relIndex].logLastChangeInView = class.logLastChangeInView;
M23_Relationship.g_relationships.descriptors[relIndex].isSubjectToArchiving = class.isSubjectToArchiving;
M23_Relationship.g_relationships.descriptors[relIndex].noTransferToProduction = class.noTransferToProduction;
M23_Relationship.g_relationships.descriptors[relIndex].noFto = class.noFto;
M23_Relationship.g_relationships.descriptors[relIndex].tabSpaceData = class.tabSpaceData;
M23_Relationship.g_relationships.descriptors[relIndex].tabSpaceLong = class.tabSpaceLong;
M23_Relationship.g_relationships.descriptors[relIndex].tabSpaceNl = class.tabSpaceNl;
M23_Relationship.g_relationships.descriptors[relIndex].tabSpaceIndex = class.tabSpaceIndex;
M23_Relationship.g_relationships.descriptors[relIndex].isTimeVarying = M24_Attribute.g_attributes.descriptors[i].isTimeVarying;


if (M24_Attribute.g_attributes.descriptors[i].attrNlIndex > 0) {
int relNlIndex;
relNlIndex = M23_Relationship_Utilities_NL.allocRelationshipNlDescriptorIndex(M23_Relationship_NL.g_relationshipsNl);

M23_Relationship_NL.g_relationshipsNl.descriptors[relNlIndex].i18nId = M23_Relationship.g_relationships.descriptors[relIndex].i18nId;
M23_Relationship_NL.g_relationshipsNl.descriptors[relNlIndex].relationshipIndex = relIndex;

nl =  new String[M23_Relationship_NL.numLangsForRelationshipsNl];
for (int j = 1; j <= M23_Relationship_NL.numLangsForRelationshipsNl; j++) {
M23_Relationship_NL.g_relationshipsNl.descriptors[relNlIndex].nl[(j)] = M24_Attribute_NL.g_attributesNl.descriptors[M24_Attribute.g_attributes.descriptors[i].attrNlIndex].nl[j];
}
}
}
}
// ### ENDIF IVK ###

// verify that 'attribute container' is defined
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.getClassIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, true) <= 0) {
M04_Utilities.logMsg("Class \"" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\" holding attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "\" not known", M01_Common.LogLevel.ellError, null, null, null);
}
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (M23_Relationship.getRelIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, true) <= 0) {
M04_Utilities.logMsg("Relationship \"" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\" holding attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "\" not known", M01_Common.LogLevel.ellError, null, null, null);
}
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
if (M21_Enum.getEnumIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, true) <= 0) {
M04_Utilities.logMsg("Enumeration \"" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\" holding attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "\" not known", M01_Common.LogLevel.ellError, null, null, null);
}
// ### IF IVK ###
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactType) {
if (M26_Type.getTypeIndexByName(M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, true) <= 0) {
M04_Utilities.logMsg("type \"" + M24_Attribute.g_attributes.descriptors[i].sectionName + "." + M24_Attribute.g_attributes.descriptors[i].className + "\" holding attribute \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "\" not known", M01_Common.LogLevel.ellError, null, null, null);
}
// ### ENDIF IVK ###
}

// ### IF IVK ###
// analyze group-ID columns
if (!(M24_Attribute.g_attributes.descriptors[i].groupIdBasedOn.compareTo("") == 0)) {
for (int j = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[i].groupIdAttributes); j <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[i].groupIdAttributes); j++) {
if (M24_Attribute.getAttributeIndexByNameAndEntityIndexRaw(M24_Attribute.g_attributes.descriptors[i].groupIdAttributes[j], M24_Attribute.g_attributes.descriptors[i].cType, M24_Attribute.g_attributes.descriptors[i].acmEntityIndex, true)) {
// todo
}
}
}
// ### ENDIF IVK ###

// determine DB-column names
Integer thisDdlType;
for (int thisDdlType = M01_Common.DdlTypeId.edtPdm; thisDdlType <= M01_Common.DdlTypeId.edtLdm; thisDdlType++) {
M24_Attribute.g_attributes.descriptors[i].dbColName[(thisDdlType)] = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[i].attributeName + (M24_Attribute.g_attributes.descriptors[i].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum ? M01_Globals.gc_enumAttrNameSuffix : ""), thisDdlType, null, null, null, null, null, null);
}
}
}


public static void evalAttributes2() {
int i;
int j;
int relIndex;
Integer relNavDirection;
int classIndex;
String[] elems;
String mapRelName;
String mapAttrName;
int referToAttrIndex;
int referToClassIndex;
int thisClassIndex;

for (int i = 1; i <= M24_Attribute.g_attributes.numDescriptors; i++) {
if (M24_Attribute.g_attributes.descriptors[i].acmEntityIndex <= 0) {
goto NextI;
}

if (M24_Attribute.g_attributes.descriptors[i].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
M24_Attribute.g_attributes.descriptors[i].domainIndex = M21_Enum.g_enums.descriptors[M24_Attribute.g_attributes.descriptors[i].valueTypeIndex].domainIndexId;
}

M24_Attribute.g_attributes.descriptors[i].compressDefault = false;
if (M03_Config.dbCompressSystemDefaults &  M24_Attribute.g_attributes.descriptors[i].domainIndex > 0) {
M24_Attribute.g_attributes.descriptors[i].compressDefault = M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[i].domainIndex].dataType != M01_Common.typeId.etTimestamp &  M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[i].domainIndex].dataType != M01_Common.typeId.etTime & M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[i].domainIndex].dataType != M01_Common.typeId.etDate;
}

if (M24_Attribute.g_attributes.descriptors[i].isIdentifying) {
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M24_Attribute.g_attributes.descriptors[i].isPrimaryKey = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].useSurrogateKey;
M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasBusinessKey = true;
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M24_Attribute.g_attributes.descriptors[i].isPrimaryKey = !(M03_Config.useSurrogateKeysForNMRelationships);
M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasBusinessKey = true;
}
}

if (M24_Attribute.g_attributes.descriptors[i].cType != M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
goto NextI;
}
// ### IF IVK ###

if (M24_Attribute.g_attributes.descriptors[i].isExpression) {
thisClassIndex = M24_Attribute.g_attributes.descriptors[i].acmEntityIndex;
if (M24_Attribute.g_attributes.descriptors[i].isTimeVarying & ! M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasNoIdentity) {
while (thisClassIndex > 0) {
M22_Class.g_classes.descriptors[thisClassIndex].hasExpressionInGen = true;
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
} else {
while (thisClassIndex > 0) {
M22_Class.g_classes.descriptors[thisClassIndex].hasExpressionInNonGen = true;
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
}
}

if (!(M24_Attribute.g_attributes.descriptors[i].isVirtual)) {
goto NextI;
}

if (M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.substring(0, 1) == "#") {
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.isRelBasedMapping = false;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.mapTo = M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.substring(M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.length() - 1 - M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.length() - 1);
if (M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.description.substring(0, 1) == "#") {
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.isRelBasedMapping = false;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.mapTo = M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.description.substring(M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.description.length() - 1 - M24_Attribute.g_attributes.descriptors[i].virtuallyMapsToForRead.description.length() - 1);
}
goto NextI;
}

classIndex = M24_Attribute.g_attributes.descriptors[i].acmEntityIndex;

elems = "".split("/");
elems = M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.description.split("/");

if (M00_Helper.uBound(elems) != 1) {
;
goto NextI;
}
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.isRelBasedMapping = true;

mapRelName = elems[0].trim();
mapAttrName = elems[1].trim();

referToClassIndex = 0;
for (int j = 1; j <= M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].relRefs.numRefs; j++) {
relIndex = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].relRefs.refs[j].refIndex;
relNavDirection = M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].relRefs.refs[j].refType;

if (relNavDirection == M01_Common.RelNavigationDirection.etLeft &  M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality < 0 & M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality == 1 & M23_Relationship.g_relationships.descriptors[relIndex].lrRelName.compareTo(mapRelName) == 0) {
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.mapTo = mapAttrName;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.navDirection = M01_Common.RelNavigationDirection.etLeft;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.relIndex = relIndex;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.targetClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex;
referToClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].rightEntityIndex;
break;
} else if (relNavDirection == M01_Common.RelNavigationDirection.etRight &  M23_Relationship.g_relationships.descriptors[relIndex].maxRightCardinality < 0 & M23_Relationship.g_relationships.descriptors[relIndex].maxLeftCardinality == 1 & M23_Relationship.g_relationships.descriptors[relIndex].rlRelName.compareTo(mapRelName) == 0) {
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.mapTo = mapAttrName;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.navDirection = M01_Common.RelNavigationDirection.etRight;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.relIndex = relIndex;
M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.targetClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex;
referToClassIndex = M23_Relationship.g_relationships.descriptors[relIndex].leftEntityIndex;
break;
}
}
// ### ENDIF IVK ###
// ### IF IVK ###

if (referToClassIndex > 0) {
referToAttrIndex = M24_Attribute.getAttributeIndexByNameAndEntityIndexRaw(mapAttrName, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, referToClassIndex, null);

M24_Attribute_Utilities.addVirtuallyReferingAttr(referToAttrIndex, i);
}
// ### ENDIF IVK ###
NextI:
}

for (int i = 1; i <= M24_Attribute.g_attributes.numDescriptors; i++) {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() == M01_ACM_IVK.conIsNational.toUpperCase()) {
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
thisClassIndex = M24_Attribute.g_attributes.descriptors[i].acmEntityIndex;
while (thisClassIndex > 0) {
M22_Class.g_classes.descriptors[thisClassIndex].hasIsNationalInclSubClasses = true;
thisClassIndex = M22_Class.g_classes.descriptors[thisClassIndex].superClassIndex;
}
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasIsNationalInclSubClasses = true;
}
}

// ### ENDIF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].attributeName.toUpperCase() == "LABEL" &  M24_Attribute.g_attributes.descriptors[i].isNl) {
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].isTimeVarying & ! M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasNoIdentity) {
// ### ELSE IVK ###
//         If .isTimeVarying Then
// ### ENDIF IVK ###
M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasLabelInGen = true;
} else {
M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasLabel = true;
}
} else if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].hasLabel = true;
}
}
}
}


public static void dropAttributeCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmAttribute, M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


private static void printAttrCsvLine(int fileNo, String attributeName, String dbColName, String i18nId, String domainSection, String domain, int attrSeqNo, String sectionName, String className, Integer cType, Boolean isNlW, Boolean isTimeVaryingW, Boolean isBusinessKeyW, Boolean isPrimaryKeyW, Boolean isTechnicalW, Boolean isNullableW, Boolean isVirtualW, Boolean isVInstantiatedW, Boolean isGroupIdW, Boolean isExpressionW, Boolean isInstantiatedW) {
boolean isNl; 
if (isNlW == null) {
isNl = false;
} else {
isNl = isNlW;
}

boolean isTimeVarying; 
if (isTimeVaryingW == null) {
isTimeVarying = false;
} else {
isTimeVarying = isTimeVaryingW;
}

boolean isBusinessKey; 
if (isBusinessKeyW == null) {
isBusinessKey = false;
} else {
isBusinessKey = isBusinessKeyW;
}

boolean isPrimaryKey; 
if (isPrimaryKeyW == null) {
isPrimaryKey = false;
} else {
isPrimaryKey = isPrimaryKeyW;
}

boolean isTechnical; 
if (isTechnicalW == null) {
isTechnical = false;
} else {
isTechnical = isTechnicalW;
}

boolean isNullable; 
if (isNullableW == null) {
isNullable = true;
} else {
isNullable = isNullableW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isVInstantiated; 
if (isVInstantiatedW == null) {
isVInstantiated = false;
} else {
isVInstantiated = isVInstantiatedW;
}

boolean isGroupId; 
if (isGroupIdW == null) {
isGroupId = false;
} else {
isGroupId = isGroupIdW;
}

boolean isExpression; 
if (isExpressionW == null) {
isExpression = false;
} else {
isExpression = isExpressionW;
}

boolean isInstantiated; 
if (isInstantiatedW == null) {
isInstantiated = true;
} else {
isInstantiated = isInstantiatedW;
}

M00_FileWriter.printToFile(fileNo, "\"" + attributeName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + dbColName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, (i18nId.compareTo("") == 0 ? "" : "\"" + i18nId.toUpperCase() + "\"") + ",");
M00_FileWriter.printToFile(fileNo, (isNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isTimeVarying ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isBusinessKey ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isPrimaryKey ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isTechnical ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isNullable ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, (isVirtual ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isVInstantiated ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isGroupId ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, (isExpression ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
if (M03_Config.supportColumnIsInstantiatedInAcmAttribute) {
M00_FileWriter.printToFile(fileNo, (isInstantiated ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
}
// ### ELSE IVK ###
// Print #fileNo, IIf(isInstantiated, gc_dbTrue, gc_dbFalse); ",";
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "\"" + domainSection.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + domain.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(attrSeqNo) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(cType) + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}


public static void genAttributeAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnAcmAttribute, acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

String lastEntityName;
String lastEntitySection;
Integer lastEntityType;
int attrSeqNo;
int i;
int j;
boolean skip;
boolean isReused;

for (int i = 1; i <= M24_Attribute.g_attributes.numDescriptors; i++) {
M24_Attribute.g_attributes.descriptors[i].attrIndex = i;

if (!(M24_Attribute.g_attributes.descriptors[i].isNotAcmRelated)) {
if (!(lastEntityName.compareTo(M24_Attribute.g_attributes.descriptors[i].className) == 0) |  !(lastEntitySection.compareTo(M24_Attribute.g_attributes.descriptors[i].sectionName) == 0) | !(lastEntityType.compareTo(M24_Attribute.g_attributes.descriptors[i].cType) == 0)) {
lastEntityName = M24_Attribute.g_attributes.descriptors[i].className;
lastEntitySection = M24_Attribute.g_attributes.descriptors[i].sectionName;
lastEntityType = M24_Attribute.g_attributes.descriptors[i].cType;
attrSeqNo = 1;
}

isReused = M03_Config.reuseColumnsInTabsForOrMapping &  M24_Attribute.g_attributes.descriptors[i].reusedAttrIndex > 0;
// ### IF IVK ###
skip = (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactType) |  M24_Attribute.g_attributes.descriptors[i].isNotAcmRelated;
// ### ELSE IVK ###
//       skip = .isNotAcmRelated
// ### ENDIF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].isPdmSpecific &  (ddlType != M01_Common.DdlTypeId.edtPdm)) {
skip = true;
}

if (!(skip)) {

// ### IF IVK ###
printAttrCsvLine(fileNo, M24_Attribute.g_attributes.descriptors[i].attributeName, M24_Attribute.g_attributes.descriptors[i].dbColName[ddlType], M24_Attribute.g_attributes.descriptors[i].i18nId, M24_Attribute.g_attributes.descriptors[i].domainSection, M24_Attribute.g_attributes.descriptors[i].domainName, attrSeqNo, M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, M24_Attribute.g_attributes.descriptors[i].cType, M24_Attribute.g_attributes.descriptors[i].isNl, M24_Attribute.g_attributes.descriptors[i].isTimeVarying, M24_Attribute.g_attributes.descriptors[i].isIdentifying, M24_Attribute.g_attributes.descriptors[i].isPrimaryKey, null, M24_Attribute.g_attributes.descriptors[i].isNullable, M24_Attribute.g_attributes.descriptors[i].isVirtual, M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.isInstantiated, M24_Attribute.g_attributes.descriptors[i].isGroupId, null, !(isReused));
// ### ELSE IVK ###
//         printAttrCsvLine fileNo, .attributeName, .dbColName(ddlType), .i18nId, .domainSection, .domain, attrSeqNo, .sectionName, _
//                                  .className, .cType, .isNl, .isTimeVarying, .isIdentifying, .isPrimaryKey, , .isNullable, Not isReused
// ### ENDIF IVK ###
attrSeqNo = attrSeqNo + 1;

// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[i].isNationalizable) {
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[i].attributeName, ddlType, null, null, null, null, true, false), M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[i].attributeName, ddlType, null, null, null, M24_Attribute.g_attributes.descriptors[i].valueType, true, null), M24_Attribute.g_attributes.descriptors[i].i18nId + "-" + M01_Globals_IVK.gc_asnSuffixNat, M24_Attribute.g_attributes.descriptors[i].domainSection, M24_Attribute.g_attributes.descriptors[i].domainName, attrSeqNo, M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, M24_Attribute.g_attributes.descriptors[i].cType, M24_Attribute.g_attributes.descriptors[i].isNl, M24_Attribute.g_attributes.descriptors[i].isTimeVarying, false, false, null, true, M24_Attribute.g_attributes.descriptors[i].isVirtual, M24_Attribute.g_attributes.descriptors[i].virtuallyMapsTo.isInstantiated, M24_Attribute.g_attributes.descriptors[i].isGroupId, null, !(isReused));
attrSeqNo = attrSeqNo + 1;
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[i].attributeName + M01_Globals_IVK.gc_anSuffixNatActivated, ddlType, null, null, null, null, null, false), M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[i].attributeName + M01_Globals_IVK.gc_anSuffixNatActivated, ddlType, null, null, null, null, null, null), M24_Attribute.g_attributes.descriptors[i].i18nId + "-" + M01_Globals_IVK.gc_asnSuffixNatActivated, M01_ACM.dxnBoolean, M01_ACM.dnBoolean, attrSeqNo, M24_Attribute.g_attributes.descriptors[i].sectionName, M24_Attribute.g_attributes.descriptors[i].className, M24_Attribute.g_attributes.descriptors[i].cType, null, null, null, null, null, null, null, null, null, null, !(isReused));
attrSeqNo = attrSeqNo + 1;
}
// ### ENDIF IVK ###
}
}
}

for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (!(M22_Class.g_classes.descriptors[i].notAcmRelated &  M22_Class.g_classes.descriptors[i].superClassIndex <= 0)) {
// surrogate key
if (M22_Class.g_classes.descriptors[i].useSurrogateKey) {
printAttrCsvLine(fileNo, M01_ACM.conOid, M01_Globals.g_anOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, true, true, false, null, null, null, null, null);
}
// classId
if (!(M22_Class.g_classes.descriptors[i].hasOwnTable)) {
printAttrCsvLine(fileNo, M01_ACM.conClassId, M01_Globals.g_anCid, "", M01_ACM.dxnClassId, M01_ACM.dnClassId, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// aggregate head: classId and objectId
if (M22_Class.g_classes.descriptors[i].aggHeadClassIndex > 0) {
printAttrCsvLine(fileNo, M01_ACM.conAhClassId, M01_Globals.g_anAhCid, "", M01_ACM.dxnClassId, M01_ACM.dnClassId, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conAhOId, M01_Globals.g_anAhOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// validFrom / validTo
if (M22_Class.g_classes.descriptors[i].isGenForming) {
printAttrCsvLine(fileNo, M01_ACM.conValidFrom, M01_Globals_IVK.g_anValidFrom, "", M01_ACM_IVK.dxnValTimestamp, M01_ACM_IVK.dnValTimestamp, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conValidTo, M01_Globals_IVK.g_anValidTo, "", M01_ACM_IVK.dxnValTimestamp, M01_ACM_IVK.dnValTimestamp, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
if (M22_Class.g_classes.descriptors[i].logLastChange) {
printAttrCsvLine(fileNo, M01_ACM.conCreateTimestamp, M01_Globals.g_anCreateTimestamp, "", M01_ACM.dxnModTimestamp, M01_ACM.dnModTimestamp, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conCreateUser, M01_Globals.g_anCreateUser, "", M01_ACM.dxnUserId, M01_ACM.dnUserId, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conLastUpdateTimestamp, M01_Globals.g_anLastUpdateTimestamp, "", M01_ACM.dxnModTimestamp, M01_ACM.dnModTimestamp, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conUpdateUser, M01_Globals.g_anUpdateUser, "", M01_ACM.dxnUserId, M01_ACM.dnUserId, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// ### IF IVK ###
// isNational
if (M22_Class.g_classes.descriptors[i].isNationalizable) {
printAttrCsvLine(fileNo, M01_ACM_IVK.conIsNational, M01_Globals_IVK.g_anIsNational, "", M01_ACM.dxnBoolean, M01_ACM.dnBoolean, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// hasBeenSetProductive-tag
if (M22_Class.g_classes.descriptors[i].isUserTransactional) {
printAttrCsvLine(fileNo, M01_ACM_IVK.conHasBeenSetProductive, M01_Globals_IVK.g_anHasBeenSetProductive, "", M01_ACM.dxnBoolean, M01_ACM.dnBoolean, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// PS-tag
if (M22_Class.g_classes.descriptors[i].isPsTagged) {
printAttrCsvLine(fileNo, M01_ACM_IVK.conPsOid, M01_Globals_IVK.g_anPsOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[i].sectionName, M22_Class.g_classes.descriptors[i].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, false, false, false, true, false, null, null, null, null, null);
}
// ### ENDIF IVK ###
}
}

boolean isExpressionRel;
boolean isTimeVaryingRel;
for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
isExpressionRel = M23_Relationship.g_relationships.descriptors[i].isMdsExpressionRel;
isTimeVaryingRel = M23_Relationship.g_relationships.descriptors[i].isTimeVarying;

skip = M23_Relationship.g_relationships.descriptors[i].notAcmRelated |  (M23_Relationship.g_relationships.descriptors[i].isPdmSpecific &  (ddlType != M01_Common.DdlTypeId.edtPdm));

if (!(skip)) {
if (M23_Relationship.g_relationships.descriptors[i].implementsInOwnTable) {
if (M03_Config.useSurrogateKeysForNMRelationships) {
printAttrCsvLine(fileNo, M01_ACM.conOid, M01_Globals.g_anOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, true, true, false, null, null, null, null, null);
}

// aggregate head: classId and objectId
if (M23_Relationship.g_relationships.descriptors[i].aggHeadClassIndex > 0) {
printAttrCsvLine(fileNo, M01_ACM.conAhClassId, M01_Globals.g_anAhCid, "", M01_ACM.dxnClassId, M01_ACM.dnClassId, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conAhOId, M01_Globals.g_anAhOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
}
// createTimestamp, LastUpdateTimestamp, etc
if (M23_Relationship.g_relationships.descriptors[i].logLastChange) {
printAttrCsvLine(fileNo, M01_ACM.conCreateTimestamp, M01_Globals.g_anCreateTimestamp, "", M01_ACM.dxnModTimestamp, M01_ACM.dnModTimestamp, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conCreateUser, M01_Globals.g_anCreateUser, "", M01_ACM.dxnUserId, M01_ACM.dnUserId, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conLastUpdateTimestamp, M01_Globals.g_anLastUpdateTimestamp, "", M01_ACM.dxnModTimestamp, M01_ACM.dnModTimestamp, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
printAttrCsvLine(fileNo, M01_ACM.conUpdateUser, M01_Globals.g_anUpdateUser, "", M01_ACM.dxnUserId, M01_ACM.dnUserId, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
}
// ### IF IVK ###
// hasBeenSetProductive-tag
if (M23_Relationship.g_relationships.descriptors[i].isUserTransactional) {
printAttrCsvLine(fileNo, M01_ACM_IVK.conHasBeenSetProductive, M01_Globals_IVK.g_anHasBeenSetProductive, "", M01_ACM.dxnBoolean, M01_ACM.dnBoolean, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
}
// PS-tag
if (M23_Relationship.g_relationships.descriptors[i].isPsTagged) {
printAttrCsvLine(fileNo, M01_ACM_IVK.conPsOid, M01_Globals_IVK.g_anPsOid, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
}
// ### ENDIF IVK ###

printAttrCsvLine(fileNo, M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType], M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType], "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);

printAttrCsvLine(fileNo, M23_Relationship.g_relationships.descriptors[i].rightFkColName[ddlType], M23_Relationship.g_relationships.descriptors[i].rightFkColName[ddlType], "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[i].sectionName, M23_Relationship.g_relationships.descriptors[i].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, null, null);
} else {// not g_relationships.descriptors(i).implementsInOwnTable
int entityIdImplementingFk;
Integer entityTypeImplementingFk;

if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft) {
entityIdImplementingFk = M23_Relationship.g_relationships.descriptors[i].leftEntityIndex;
entityTypeImplementingFk = M23_Relationship.g_relationships.descriptors[i].leftEntityType;
} else {
entityIdImplementingFk = M23_Relationship.g_relationships.descriptors[i].rightEntityIndex;
entityTypeImplementingFk = M23_Relationship.g_relationships.descriptors[i].rightEntityType;
}
isReused = false;

if (entityIdImplementingFk > 0) {
if (M23_Relationship.g_relationships.descriptors[i].reusedRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft) {
if (M23_Relationship.g_relationships.descriptors[i].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].leftEntityIndex].orMappingSuperClassIndex) {
isReused = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex == M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].leftEntityIndex) {
isReused = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmRight) {
if (M23_Relationship.g_relationships.descriptors[i].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].rightEntityIndex].orMappingSuperClassIndex) {
isReused = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex == M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].rightEntityIndex) {
isReused = true;
}
}
}

if ((M03_Config.supportColumnIsInstantiatedInAcmAttribute | ! isReused) & ! M23_Relationship.g_relationships.descriptors[i].isReusedInSameEntity) {
String fkColName;
fkColName = (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[i].rightFkColName[ddlType] : M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType]);

if (entityTypeImplementingFk == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// ### IF IVK ###
printAttrCsvLine(fileNo, fkColName, fkColName, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[entityIdImplementingFk].sectionName, M22_Class.g_classes.descriptors[entityIdImplementingFk].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, isTimeVaryingRel, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
// ### ELSE IVK ###
//                 printAttrCsvLine fileNo, _
//                   fkColName, fkColName, "", dxnOid, dnOid, 1000, .sectionName, .className, eactClass, _
//                   False, False, False, False, True, False, , Not isReused
// ### ENDIF IVK ###
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].isNationalizable) {
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(fkColName + M01_Globals_IVK.gc_anSuffixNat, ddlType, null, null, null, null, null, false), M04_Utilities.genAttrName(fkColName, ddlType, null, null, null, null, true, null), "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[entityIdImplementingFk].sectionName, M22_Class.g_classes.descriptors[entityIdImplementingFk].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, isTimeVaryingRel, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(fkColName + "_ISNATACTIVE", ddlType, null, null, null, null, null, false), M04_Utilities.genAttrName(fkColName + "_ISNATACTIVE", ddlType, null, null, null, null, null, null), "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M22_Class.g_classes.descriptors[entityIdImplementingFk].sectionName, M22_Class.g_classes.descriptors[entityIdImplementingFk].className, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, false, isTimeVaryingRel, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
}
// ### ENDIF IVK ###
} else if (entityTypeImplementingFk == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
// ### IF IVK ###
printAttrCsvLine(fileNo, fkColName, fkColName, "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].sectionName, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
// ### ELSE IVK ###
//                 printAttrCsvLine fileNo, _
//                   fkColName, fkColName, "", dxnOid, dnOid, 1000, .sectionName, .relName, eactRelationship, _
//                   False, False, False, False, True, False, , Not isReused
// ### ENDIF IVK ###
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[i].isNationalizable) {
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(fkColName + M01_Globals_IVK.gc_anSuffixNat, ddlType, null, null, null, null, null, false), M04_Utilities.genAttrName(fkColName, ddlType, null, null, null, null, true, null), "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].sectionName, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
printAttrCsvLine(fileNo, M04_Utilities.genAttrName(fkColName + "_ISNATACTIVE", ddlType, null, null, null, null, null, false), M04_Utilities.genAttrName(fkColName + "_ISNATACTIVE", ddlType, null, null, null, null, null, null), "", M01_ACM.dxnOid, M01_ACM.dnOid, 1000, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].sectionName, M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].relName, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, false, false, false, false, true, false, null, null, null, isExpressionRel, !(isReused));
}
// ### ENDIF IVK ###
}
}
}
}
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






}