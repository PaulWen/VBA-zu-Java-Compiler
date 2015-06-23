package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M24_Attribute_NL {




private static final int colEntryFilter = 1;
private static final int colI18nId = M24_Attribute.colAttrI18nId;
private static final int colFirstLang = colI18nId + 1;

private static int[] langIds;

private static final int firstRow = 4;

private static final String sheetName = "Attr";

public static int numLangsForAttributesNl;
private static boolean isIntialized;

private static final int acmCsvProcessingStep = 5;

public static M24_Attribute_Utilities_NL.AttributeNlDescriptors g_attributesNl;


private static void readSheet() {
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));

int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

if (!(M01_Common.isInitialized)) {
M24_Attribute_NL.numLangsForAttributesNl = 0;

while (M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + M24_Attribute_NL.numLangsForAttributesNl).getStringCellValue() + "" != "") {
M24_Attribute_NL.numLangsForAttributesNl = M24_Attribute_NL.numLangsForAttributesNl + 1;
}
if (M24_Attribute_NL.numLangsForAttributesNl > 0) {
langIds =  new int[M24_Attribute_NL.numLangsForAttributesNl];
int i;
for (int i = 1; i <= M24_Attribute_NL.numLangsForAttributesNl; i++) {
langIds[(i)] = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow - 1, colFirstLang + i - 1).getStringCellValue(), null);

if (langIds[i] < 0) {
M04_Utilities.logMsg("invalid language ID '" + M00_Excel.getCell(thisSheet, thisRow - 1, i).getStringCellValue() + "' found in sheet '" + thisSheet + "' (column" + colFirstLang + i - 1 + ")", M01_Common.LogLevel.ellError, null, null, null);
}
}
}
}

if (M24_Attribute_NL.numLangsForAttributesNl > 0) {
while (M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue() + "" != "") {
if (M04_Utilities.getIsEntityFiltered(M00_Excel.getCell(thisSheet, thisRow, colEntryFilter).getStringCellValue())) {
goto NextRow;
}

M24_Attribute_NL.g_attributesNl.descriptors[M24_Attribute_Utilities_NL.allocAttributeNlDescriptorIndex(M24_Attribute_NL.g_attributesNl)].i18nId = M00_Excel.getCell(thisSheet, thisRow, colI18nId).getStringCellValue().trim();
for (int i = 1; i <= M24_Attribute_NL.numLangsForAttributesNl; i++) {
M24_Attribute_NL.g_attributesNl.descriptors[M24_Attribute_Utilities_NL.allocAttributeNlDescriptorIndex(M24_Attribute_NL.g_attributesNl)].nl[(i)] = M00_Excel.getCell(thisSheet, thisRow, colFirstLang + i - 1).getStringCellValue().trim();
}

NextRow:
thisRow = thisRow + 1;
}
}
}


public static void getAttributesNl() {
if (M24_Attribute_NL.g_attributesNl.numDescriptors == 0) {
readSheet();
}
}


public static void resetAttributesNl() {
M24_Attribute_NL.g_attributesNl.numDescriptors = 0;
M01_Common.isInitialized = false;
}


public static void evalAttributesNl() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex = M24_Attribute.getAttributeIndexByI18nId(M24_Attribute_NL.g_attributesNl.descriptors[i].i18nId);
if (M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex > 0) {
M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].attrNlIndex = i;
}
}
}


public static void dropAttributeNlCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmAttribute, null, null, null), M01_Globals.g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM");
}


public static void genAttributeNlAcmMetaCsv(Integer ddlType) {
String fileName;
int fileNo;

fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M04_Utilities.genNlObjName(M01_ACM.clnAcmAttribute, null, null, null), acmCsvProcessingStep, "ACM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

// ### IF IVK ###
// FIXME: some hard-coding for NL-Text-Suffixes
String[] natNlSuffixes;
natNlSuffixes =  new String[M24_Attribute_NL.numLangsForAttributesNl];
String[] isNatActiveNlSuffixes;
isNatActiveNlSuffixes =  new String[M24_Attribute_NL.numLangsForAttributesNl];

natNlSuffixes[(1)] = " (national)";
isNatActiveNlSuffixes[(1)] = " (national aktiv)";
if ((M24_Attribute_NL.numLangsForAttributesNl > 1)) {
natNlSuffixes[(2)] = " (national)";
isNatActiveNlSuffixes[(2)] = " (national active)";
}

// ### ENDIF IVK ###
int i;
int j;
for (int i = 1; i <= M24_Attribute_NL.g_attributesNl.numDescriptors; i++) {
for (int j = 1; j <= M24_Attribute_NL.numLangsForAttributesNl; j++) {
if (M24_Attribute_NL.g_attributesNl.descriptors[i].nl[j] != "" &  M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex > 0) {
// ### IF IVK ###
if (!(M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].isNotAcmRelated &  (M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].cType != M24_Attribute_Utilities.AcmAttrContainerType.eactType))) {
String effectiveAttrName;
int k;
for (int k = 1; k <= (M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].isExpression ? 2 : 1); k++) {
if (k == 1) {
effectiveAttrName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].attributeName, ddlType, null, null, null, null, null, false);
} else {
effectiveAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].shortName + "EXP", null, null, null, null);
}

M00_FileWriter.printToFile(fileNo, "\"" + effectiveAttrName.toUpperCase() + "\",");
// ### ELSE IVK ###
//           If Not .isNotAcmRelated Then
// ### INDENT IVK ### -2
//               Print #fileNo, """"; UCase(.attributeName); """,";
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].cType) + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(j) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute_NL.g_attributesNl.descriptors[i].nl[j] + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
// ### IF IVK ###

if (M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].isNationalizable) {
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.genAttrName(effectiveAttrName, ddlType, null, null, null, null, true, false).toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].cType) + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(j) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute_NL.g_attributesNl.descriptors[i].nl[j] + natNlSuffixes[j] + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.genAttrName(effectiveAttrName + M01_Globals_IVK.gc_anSuffixNatActivated, ddlType, null, null, null, null, null, false).toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M04_Utilities.getAcmEntityTypeKey(M24_Attribute.g_attributes.descriptors[M24_Attribute_NL.g_attributesNl.descriptors[i].attributeIndex].cType) + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(j) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M24_Attribute_NL.g_attributesNl.descriptors[i].nl[j] + isNatActiveNlSuffixes[j] + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}
}
}

for (int i = 1; i <= M22_Class.g_classes.numDescriptors; i++) {
if (!(M22_Class.g_classes.descriptors[i].notAcmRelated &  M22_Class.g_classes.descriptors[i].superClassIndex <= 0)) {
// surrogate key
if (M22_Class.g_classes.descriptors[i].useSurrogateKey) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conOid.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Objekt ID\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conOid.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Object ID\"," + M04_Utilities.getCsvTrailer(0));
}
// validFrom / validTo
if (M22_Class.g_classes.descriptors[i].isGenForming) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conValidFrom.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Gültig von\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conValidFrom.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Valid from\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conValidTo.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Gültig bis\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conValidTo.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Valid to\"," + M04_Utilities.getCsvTrailer(0));
}
if (M22_Class.g_classes.descriptors[i].logLastChange) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateTimestamp.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Erstellungszeitpunkt\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateTimestamp.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Create Timestamp\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateUser.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Ersteller\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateUser.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Create user\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conLastUpdateTimestamp.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Zeitpunkt\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conLastUpdateTimestamp.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Timestamp\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conUpdateUser.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Benutzer\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conUpdateUser.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"User\"," + M04_Utilities.getCsvTrailer(0));
}
// ### IF IVK ###
// isNational
if (M22_Class.g_classes.descriptors[i].isNationalizable) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conIsNational.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Nationalisiert\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conIsNational.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Nationalized\"," + M04_Utilities.getCsvTrailer(0));
}
// hasBeenSetProductive-tag
if (M22_Class.g_classes.descriptors[i].isUserTransactional) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conHasBeenSetProductive.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Produktivgestellt\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conHasBeenSetProductive.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Set productive\"," + M04_Utilities.getCsvTrailer(0));
}
// PS-tag
if (M22_Class.g_classes.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conPsOid.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Produktstruktur OID\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conPsOid.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M22_Class.g_classes.descriptors[i].className.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Productstructure OID\"," + M04_Utilities.getCsvTrailer(0));
}
// ### ENDIF IVK ###
}
}

for (int i = 1; i <= M23_Relationship.g_relationships.numDescriptors; i++) {
if (M23_Relationship.g_relationships.descriptors[i].relName.compareTo("BinaryPropertyValue") == 0) {
M23_Relationship_Utilities.RelationshipDescriptor rel;
rel = M23_Relationship.g_relationships.descriptors[i];
}
if (!(M23_Relationship.g_relationships.descriptors[i].notAcmRelated &  M23_Relationship.g_relationships.descriptors[i].reusedRelIndex <= 0)) {
if (M23_Relationship.g_relationships.descriptors[i].implementsInOwnTable) {
if (M03_Config.useSurrogateKeysForNMRelationships) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conOid.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Beziehungs-ID\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conOid.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Relationship ID\"," + M04_Utilities.getCsvTrailer(0));
}

// createTimestamp, LastUpdateTimestamp, etc
if (M23_Relationship.g_relationships.descriptors[i].logLastChange) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateTimestamp.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Erstellungszeitpunkt\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateTimestamp.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Create Timestamp\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateUser.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Ersteller\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conCreateUser.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Create user\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conLastUpdateTimestamp.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Zeitpunkt\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conLastUpdateTimestamp.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Timestamp\"," + M04_Utilities.getCsvTrailer(0));

M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conUpdateUser.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Benutzer\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM.conUpdateUser.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"User\"," + M04_Utilities.getCsvTrailer(0));
}
// ### IF IVK ###

// PS-tag
if (M23_Relationship.g_relationships.descriptors[i].isPsTagged) {
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conPsOid.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + ",\"Produktstruktur OID\"," + M04_Utilities.getCsvTrailer(0));
M00_FileWriter.printToFile(fileNo, "\"" + M01_ACM_IVK.conPsOid.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + ",\"Productstructure OID\"," + M04_Utilities.getCsvTrailer(0));
}

if (M23_Relationship.g_relationships.descriptors[i].relNlIndex > 0) {
if (M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[M01_Globals_IVK.gc_langIdGerman] != "") {
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType] + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdGerman) + "," + "\"" + M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[M01_Globals_IVK.gc_langIdGerman] + "\"," + M04_Utilities.getCsvTrailer(0));
}
if (M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[M01_Globals_IVK.gc_langIdEnglish] != "") {
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType] + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].sectionName.toUpperCase() + "\"," + "\"" + M23_Relationship.g_relationships.descriptors[i].relName.toUpperCase() + "\"," + "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\"," + String.valueOf(M01_Globals_IVK.gc_langIdEnglish) + "," + "\"" + M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[M01_Globals_IVK.gc_langIdEnglish] + "\"," + M04_Utilities.getCsvTrailer(0));
}
}
// ### ENDIF IVK ###
// not .implementsInOwnTable
} else if ((M23_Relationship.g_relationships.descriptors[i].relNlIndex > 0)) {
entityIdImplementingFk;
entityIdImplementingFk = (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft ? M23_Relationship.g_relationships.descriptors[i].leftEntityIndex : (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmRight ? M23_Relationship.g_relationships.descriptors[i].rightEntityIndex : -1));

if (entityIdImplementingFk > 0) {
String fkColName;
int relNlIndex;
boolean skip;
skip = false;

if (M23_Relationship.g_relationships.descriptors[i].reusedRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft) {
if (M23_Relationship.g_relationships.descriptors[i].leftEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].leftEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].leftEntityIndex].orMappingSuperClassIndex) {
skip = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].leftEntityIndex == M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].leftEntityIndex) {
skip = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmRight) {
if (M23_Relationship.g_relationships.descriptors[i].rightEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[i].rightEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].rightEntityIndex].orMappingSuperClassIndex) {
skip = true;
}
} else if (M23_Relationship.g_relationships.descriptors[i].rightEntityIndex == M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[i].reusedRelIndex].rightEntityIndex) {
skip = true;
}
}
}

Integer entityTypeImplementingFk;

if ((M03_Config.supportColumnIsInstantiatedInAcmAttribute | ! skip) & ! M23_Relationship.g_relationships.descriptors[i].isReusedInSameEntity) {
if (M23_Relationship.g_relationships.descriptors[i].implementsInEntity == M01_Common.RelNavigationMode.ernmLeft) {
fkColName = M23_Relationship.g_relationships.descriptors[i].rightFkColName[ddlType];
entityTypeImplementingFk = M23_Relationship.g_relationships.descriptors[i].leftEntityType;
} else {
fkColName = M23_Relationship.g_relationships.descriptors[i].leftFkColName[ddlType];
entityTypeImplementingFk = M23_Relationship.g_relationships.descriptors[i].rightEntityType;
}

relNlIndex = M23_Relationship.g_relationships.descriptors[i].relNlIndex;

if (entityTypeImplementingFk == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
for (int j = 1; j <= M24_Attribute_NL.numLangsForAttributesNl; j++) {
if (M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[j] != "") {
M00_FileWriter.printToFile(fileNo, "\"" + fkColName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[entityIdImplementingFk].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M22_Class.g_classes.descriptors[entityIdImplementingFk].className.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M01_Globals.gc_acmEntityTypeKeyClass + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(j) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[j] + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}
}
} else if (entityTypeImplementingFk == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
for (int j = 1; j <= M24_Attribute_NL.numLangsForAttributesNl; j++) {
if (M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[j] != "") {
M00_FileWriter.printToFile(fileNo, "\"" + fkColName + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].sectionName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship.g_relationships.descriptors[entityIdImplementingFk].relName.toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M01_Globals.gc_acmEntityTypeKeyRel + "\",");
M00_FileWriter.printToFile(fileNo, String.valueOf(j) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[i].relNlIndex].nl[j] + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}
}
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