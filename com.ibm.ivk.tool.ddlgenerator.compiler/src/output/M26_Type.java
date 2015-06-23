package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M26_Type {


// ### IF IVK ###


private static final int colSection = 2;
private static final int colTypeName = colSection + 1;
private static final int colShortName = colTypeName + 1;
private static final int colComment = colShortName + 1;

private static final int firstRow = 3;

private static final String sheetName = "Type";

public static M26_Type_Utilities.TypeDescriptors g_types;


private static void readSheet() {
M26_Type_Utilities.initTypeDescriptors(M26_Type.g_types);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow;

String lastSection;
String lastTypeName;
while (M00_Excel.getCell(thisSheet, thisRow, colTypeName).getStringCellValue() + "" != "") {
M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue();
if ((M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].sectionName + "" == "")) {
M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].sectionName = lastSection;
}

M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].typeName = M00_Excel.getCell(thisSheet, thisRow, colTypeName).getStringCellValue();
if ((M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].typeName + "" == "")) {
M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].typeName = lastTypeName;
}

M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].shortName = M00_Excel.getCell(thisSheet, thisRow, colShortName).getStringCellValue();
M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].comment = M00_Excel.getCell(thisSheet, thisRow, colComment).getStringCellValue();

lastSection = M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].sectionName;
lastTypeName = M26_Type.g_types.descriptors[M26_Type_Utilities.allocTypeDescriptorIndex(M26_Type.g_types)].typeName;

thisRow = thisRow + 1;
}
}


public static void getTypes() {
if (M26_Type.g_types.numDescriptors == 0) {
readSheet();
}
}


public static void resetTypes() {
M26_Type.g_types.numDescriptors = 0;
}


public static Integer getTypeIndexByName(String sectionName, String typeName, Boolean silentW) {
boolean silent; 
if (silentW == null) {
silent = false;
} else {
silent = silentW;
}

Integer returnValue;
int i;

returnValue = -1;

for (i = 1; i <= 1; i += (1)) {
if (M26_Type.g_types.descriptors[i].sectionName.toUpperCase() == sectionName.toUpperCase() &  M26_Type.g_types.descriptors[i].typeName.toUpperCase() == typeName.toUpperCase()) {
returnValue = i;
return returnValue;
}
}

if (!(silent)) {
errMsgBox("unable to identify type '" + sectionName + "." + typeName + "'", vbCritical);
}
return returnValue;
}


public static Boolean isType(String sectionName, String typeName, Integer typeIndexW) {
int typeIndex; 
if (typeIndexW == null) {
typeIndex = -1;
} else {
typeIndex = typeIndexW;
}

Boolean returnValue;
returnValue = false;

typeIndex = M26_Type.getTypeIndexByName(sectionName, typeName, true);
if ((typeIndex > 0)) {
returnValue = true;
}
return returnValue;
}


public static void evalTypes() {
int thisTypeIndex;
int thisAttrIndex;

for (thisTypeIndex = 1; thisTypeIndex <= 1; thisTypeIndex += (1)) {
// determine class index
M26_Type.g_types.descriptors[thisTypeIndex].typeIndex = M26_Type.getTypeIndexByName(M26_Type.g_types.descriptors[thisTypeIndex].sectionName, M26_Type.g_types.descriptors[thisTypeIndex].typeName, null);
// determine reference to section
M26_Type.g_types.descriptors[thisTypeIndex].sectionIndex = M20_Section.getSectionIndexByName(M26_Type.g_types.descriptors[thisTypeIndex].sectionName, null);
// determine index of class 'owning' the table implementing this class

M26_Type.g_types.descriptors[thisTypeIndex].attrRefs.numDescriptors = 0;
for (thisAttrIndex = 1; thisAttrIndex <= 1; thisAttrIndex += (1)) {
if (M26_Type.g_types.descriptors[thisTypeIndex].sectionName.toUpperCase() == M24_Attribute.g_attributes.descriptors[thisAttrIndex].sectionName.toUpperCase() &  M26_Type.g_types.descriptors[thisTypeIndex].typeName.toUpperCase() == M24_Attribute.g_attributes.descriptors[thisAttrIndex].className.toUpperCase() & M24_Attribute.g_attributes.descriptors[thisAttrIndex].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactType) {
if (M24_Attribute.g_attributes.descriptors[thisAttrIndex].valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
M26_Type.g_types.descriptors[thisTypeIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M26_Type.g_types.descriptors[thisTypeIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtEnum;
} else if (M26_Type.isType(M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainSection, M24_Attribute.g_attributes.descriptors[thisAttrIndex].domainName, null)) {
M26_Type.g_types.descriptors[thisTypeIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M26_Type.g_types.descriptors[thisTypeIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtType;
} else {
M26_Type.g_types.descriptors[thisTypeIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M26_Type.g_types.descriptors[thisTypeIndex].attrRefs)].refType = M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute;
}
M26_Type.g_types.descriptors[thisTypeIndex].attrRefs.descriptors[M24_Attribute_Utilities.allocAttrDescriptorRefIndex(M26_Type.g_types.descriptors[thisTypeIndex].attrRefs)].refIndex = thisAttrIndex;
}
}
}
}


private static void printRefs() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
System.out.println(M26_Type.g_types.descriptors[i].typeName + " : " + M26_Type.g_types.descriptors[i].attrRefs.numDescriptors);
;
for (int j = 1; j <= M26_Type.g_types.descriptors[i].attrRefs.numDescriptors; j++) {
System.out.println(M26_Type.g_types.descriptors[i].typeName + " / " + M26_Type.g_types.descriptors[i].attrRefs.descriptors[j].refType + " / " + M26_Type.g_types.descriptors[i].attrRefs.descriptors[j].refIndex);
;
}
}
}
// ### ENDIF IVK ###


}