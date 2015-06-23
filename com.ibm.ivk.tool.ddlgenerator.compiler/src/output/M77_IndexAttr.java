package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M77_IndexAttr {




private static final int colEntryFilter = 1;
private static final int colSection = 2;
private static final int colClassName = colSection + 1;
private static final int colEntityType = colClassName + 1;
private static final int colIndexName = colEntityType + 1;
private static final int colAttrName = colIndexName + 1;
private static final int colAttrIsIncluded = colAttrName + 1;
private static final int colRelSectionName = colAttrIsIncluded + 1;
private static final int colRelName = colRelSectionName + 1;
private static final int colIsAsc = colAttrName + 1;

private static final int firstRow = 3;

private static final String sheetName = "IdxAttr";

public static M77_IndexAttr_Utilities.IndexAttrDescriptors g_indexAttrs;


private static void readSheet() {
M77_IndexAttr_Utilities.initIndexAttrDescriptors(M77_IndexAttr.g_indexAttrs);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M77_IndexAttr_Utilities.allocIndexAttrDescriptorIndex(M77_IndexAttr.g_indexAttrs);
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].sectionName = M00_Excel.getCell(thisSheet, thisRow, colSection).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].className = M00_Excel.getCell(thisSheet, thisRow, colClassName).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].cType = M24_Attribute_Utilities.getAttrContainerType(M00_Excel.getCell(thisSheet, thisRow, colEntityType).getStringCellValue());
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].indexName = M00_Excel.getCell(thisSheet, thisRow, colIndexName).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].attrName = M00_Excel.getCell(thisSheet, thisRow, colAttrName).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].attrIsIncluded = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colAttrIsIncluded).getStringCellValue(), null);
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].relSectionName = M00_Excel.getCell(thisSheet, thisRow, colRelSectionName).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].relName = M00_Excel.getCell(thisSheet, thisRow, colRelName).getStringCellValue().trim();
M77_IndexAttr.g_indexAttrs.descriptors[M77_IndexAttr.g_indexAttrs.numDescriptors].isAsc = !((M00_Excel.getCell(thisSheet, thisRow, colIsAsc).getStringCellValue().toUpperCase() == "DESC"));

NextRow:
thisRow = thisRow + 1;
}
}


public static void getIndexAttrs() {
if ((M77_IndexAttr.g_indexAttrs.numDescriptors == 0)) {
readSheet();
}
}


public static void resetIndexAttrs() {
M77_IndexAttr.g_indexAttrs.numDescriptors = 0;
}


public static void evalIndexAttrs() {
int i;
int j;
M21_Enum_Utilities.EnumDescriptor enumDescr;

for (i = 1; i <= 1; i += (1)) {
// determine references to attributes
for (j = 1; j <= 1; j += (1)) {
if (M77_IndexAttr.g_indexAttrs.descriptors[i].sectionName.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].sectionName.toUpperCase() &  M77_IndexAttr.g_indexAttrs.descriptors[i].className.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].className.toUpperCase() & (M77_IndexAttr.g_indexAttrs.descriptors[i].attrName.toUpperCase() == M24_Attribute.g_attributes.descriptors[j].attributeName.toUpperCase() |  M77_IndexAttr.g_indexAttrs.descriptors[i].attrName.toUpperCase() == (M24_Attribute.g_attributes.descriptors[j].attributeName.toUpperCase() + M01_Globals.gc_enumAttrNameSuffix)) & M77_IndexAttr.g_indexAttrs.descriptors[i].cType.compareTo(M24_Attribute.g_attributes.descriptors[j].cType) == 0) {
M77_IndexAttr.g_indexAttrs.descriptors[i].attrRef = j;
}
}

if (M77_IndexAttr.g_indexAttrs.descriptors[i].attrRef <= 0 &  !(M77_IndexAttr.g_indexAttrs.descriptors[i].relSectionName.compareTo("") == 0) & !(M77_IndexAttr.g_indexAttrs.descriptors[i].relName.compareTo("") == 0)) {
if (M77_IndexAttr.g_indexAttrs.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// check if this index-attribute corresponds to a relationship
for (j = 1; j <= 1; j += (1)) {
if (M77_IndexAttr.g_indexAttrs.descriptors[i].relSectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].sectionName.toUpperCase() &  M77_IndexAttr.g_indexAttrs.descriptors[i].relName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase()) {

if (M23_Relationship.g_relationships.descriptors[j].leftClassName.toUpperCase() == M77_IndexAttr.g_indexAttrs.descriptors[i].className.toUpperCase()) {
M77_IndexAttr.g_indexAttrs.descriptors[i].relRefDirection = M01_Common.RelNavigationDirection.etLeft;
} else {
M77_IndexAttr.g_indexAttrs.descriptors[i].relRefDirection = M01_Common.RelNavigationDirection.etRight;
}

M77_IndexAttr.g_indexAttrs.descriptors[i].relRef = j;
}
}
} else if (M77_IndexAttr.g_indexAttrs.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
for (j = 1; j <= 1; j += (1)) {
if (M77_IndexAttr.g_indexAttrs.descriptors[i].sectionName.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].sectionName.toUpperCase() &  M77_IndexAttr.g_indexAttrs.descriptors[i].className.toUpperCase() == M23_Relationship.g_relationships.descriptors[j].relName.toUpperCase()) {

if (M23_Relationship.g_relationships.descriptors[j].lrRelName.toUpperCase() == M77_IndexAttr.g_indexAttrs.descriptors[i].relName.toUpperCase()) {
M77_IndexAttr.g_indexAttrs.descriptors[i].relRefDirection = M01_Common.RelNavigationDirection.etLeft;
} else {
M77_IndexAttr.g_indexAttrs.descriptors[i].relRefDirection = M01_Common.RelNavigationDirection.etRight;
}

M77_IndexAttr.g_indexAttrs.descriptors[i].relRef = j;
}
}
}
}

// ### IF IVK ###
if (M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM.conOid.toUpperCase() |  M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM.conClassId.toUpperCase() | M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM.conVersionId.toUpperCase() | M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM.conValidFrom.toUpperCase() | M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM.conValidTo.toUpperCase() | M77_IndexAttr.g_indexAttrs.descriptors[i].attrName == M01_ACM_IVK.conIsDeleted.toUpperCase() | M77_IndexAttr.g_indexAttrs.descriptors[i].attrName.substring(M77_IndexAttr.g_indexAttrs.descriptors[i].attrName.length() - 1 - 4) == "_OID") {
// ### ELSE IVK ###
//       If .attrName = ucase(conOid) Or .attrName = ucase(conClassId) Or .attrName = UCase(conVersionId) Or .attrName = UCase(conValidFrom) Or .attrName = UCase(conValidTo) Or Right(.attrName, 4) = "_OID" Then
// ### ENDIF IVK ###
M77_IndexAttr.g_indexAttrs.descriptors[i].attrRef = -1;
// meta attribute
} else if (!((M77_IndexAttr.g_indexAttrs.descriptors[i].attrRef > 0 |  M77_IndexAttr.g_indexAttrs.descriptors[i].relRef > 0))) {
if (!(M77_IndexAttr.g_indexAttrs.descriptors[i].attrName.compareTo("") == 0)) {
M04_Utilities.logMsg("unknown attribute \"" + M77_IndexAttr.g_indexAttrs.descriptors[i].className + "." + M77_IndexAttr.g_indexAttrs.descriptors[i].attrName + "\" used in index \"" + M77_IndexAttr.g_indexAttrs.descriptors[i].sectionName + "." + M77_IndexAttr.g_indexAttrs.descriptors[i].indexName, M01_Common.LogLevel.ellError, null, null, null);
} else if (!(M77_IndexAttr.g_indexAttrs.descriptors[i].relName.compareTo("") == 0)) {
M04_Utilities.logMsg("unknown relationship \"" + M77_IndexAttr.g_indexAttrs.descriptors[i].relSectionName + "." + M77_IndexAttr.g_indexAttrs.descriptors[i].relName + "\" used in index \"" + M77_IndexAttr.g_indexAttrs.descriptors[i].sectionName + "." + M77_IndexAttr.g_indexAttrs.descriptors[i].indexName, M01_Common.LogLevel.ellError, null, null, null);
}
}
}
}


}