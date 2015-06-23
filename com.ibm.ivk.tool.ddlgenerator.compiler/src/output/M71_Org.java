package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M71_Org {




private static final int colOrg = 2;
private static final int colName = colOrg + 1;
private static final int colIsPrimary = colName + 1;
private static final int colIsTemplate = colIsPrimary + 1;
private static final int colOid = colIsTemplate + 1;
private static final int colSequenceCacheSize = colOid + 1;

private static final int firstRow = 3;

private static final String sheetName = "Org";

private static final int processingStep = 3;

public static M71_Org_Utilities.OrgDescriptors g_orgs;


private static void readSheet() {
int thisOrgId;

M71_Org_Utilities.initOrgDescriptors(M71_Org.g_orgs);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colOrg).getStringCellValue() + "" != "") {
thisOrgId = new Double(M00_Excel.getCell(thisSheet, thisRow, colOrg).getStringCellValue()).intValue();

M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].id = thisOrgId;
M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].name = M00_Excel.getCell(thisSheet, thisRow, colName).getStringCellValue().trim();
M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].isPrimary = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsPrimary).getStringCellValue(), null);
M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].isTemplate = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colIsTemplate).getStringCellValue(), null);
M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].oid = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colOid).getStringCellValue(), null);
M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].sequenceCacheSize = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceCacheSize).getStringCellValue(), -1);

if (!(M03_Config.genTemplateDdl &  M71_Org.g_orgs.descriptors[M71_Org_Utilities.allocOrgIndex(M71_Org.g_orgs)].isTemplate)) {
M71_Org.g_orgs.numDescriptors = M71_Org.g_orgs.numDescriptors - 1;
}
thisRow = thisRow + 1;
}
}


public static void getOrgs() {
if (M71_Org.g_orgs.numDescriptors == 0) {
readSheet();
}
}


public static void resetOrgs() {
M71_Org.g_orgs.numDescriptors = 0;
}

public static Integer getOrgIndexById(int thisOrgId) {
Integer returnValue;
int i;

returnValue = -1;
M71_Org.getOrgs();

for (i = 1; i <= 1; i += (1)) {
if (M71_Org.g_orgs.descriptors[i].id == thisOrgId) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static String getOrgNameById(int thisOrgId) {
String returnValue;
returnValue = "";
int orgIndex;
orgIndex = M71_Org.getOrgIndexById(thisOrgId);
if ((orgIndex > 0)) {
returnValue = M71_Org.g_orgs.descriptors[orgIndex].name;
}

return returnValue;
}


public static String getOrgNameByIndex( int thisOrgIndex) {
String returnValue;
returnValue = "";
if ((thisOrgIndex > 0)) {
returnValue = M71_Org.g_orgs.descriptors[thisOrgIndex].name;
}

return returnValue;
}


public static Boolean getOrgIsTemplate( int thisOrgIndex) {
Boolean returnValue;
returnValue = false;
if ((thisOrgIndex > 0)) {
returnValue = M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate;
}

return returnValue;
}


public static void genOrgDdl( Integer thisOrgIndexW, Integer ddlTypeW) {
int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, null, null, null, null);

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
M71_Org.genSequence("Common Sequence for Object IDs", M01_ACM.snMeta.toUpperCase() + "." + M01_LDM.gc_seqNameOid.toUpperCase(), 1, fileNo, null, null, null, null, null, null, null);
} else {
M71_Org.genOidSequenceForOrg(thisOrgIndex, fileNo, ddlType, null);
}

// ### IF IVK ###
if (M03_Config.supportGroupIdColumns) {
int thisOrgId;
thisOrgId = -1;
boolean orgIsTemplate;
if (thisOrgIndex > 0) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
orgIsTemplate = M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate;
}

String qualSeqNameGroupId;
String lastQualSeqNameGroupId;
lastQualSeqNameGroupId = "";
String attrShortName;
int i;
for (int i = 1; i <= M24_Attribute.g_attributes.numDescriptors; i++) {
if (!(M24_Attribute.g_attributes.descriptors[i].groupIdBasedOn.compareTo("") == 0)) {
qualSeqNameGroupId = "";
attrShortName = M24_Attribute.g_attributes.descriptors[i].shortName;
if (M24_Attribute.g_attributes.descriptors[i].cType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].orMappingSuperClassIndex].specificToOrgId > 0 &  thisOrgId != M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].orMappingSuperClassIndex].specificToOrgId) {
goto NextI;
}
qualSeqNameGroupId = M04_Utilities.genQualObjName(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].orMappingSuperClassIndex].sectionIndex, "SEQ_" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].orMappingSuperClassIndex].shortName + attrShortName, "SEQ_" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[i].acmEntityIndex].orMappingSuperClassIndex].shortName + attrShortName, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);
} else {
// we currently do not need this
}

if (!(qualSeqNameGroupId.compareTo("") == 0) &  !(lastQualSeqNameGroupId.compareTo(qualSeqNameGroupId) == 0)) {
M71_Org.genSequence("Sequence for Group IDs for Column \"" + M24_Attribute.g_attributes.descriptors[i].attributeName + "@" + M24_Attribute.g_attributes.descriptors[i].className + "\"", qualSeqNameGroupId, thisOrgIndex, fileNo, M01_LDM.gc_sequenceMinValue, null, null, null, null, "1", orgIsTemplate);
lastQualSeqNameGroupId = qualSeqNameGroupId;
}
}
goto NextI;
errMsgBox(Err.description);

NextI:
}
}

// ### ENDIF IVK ###
NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genOrgsDdl(Integer ddlType) {
int thisOrgIndex;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
M71_Org.genOrgDdl(null, M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
M71_Org.genOrgDdl(thisOrgIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}


public static String genQualOidSeqNameForOrg( int thisOrgIndex, Integer ddlTypeW, Integer forOrgIndexW,  Integer thisPoolIndexW, Integer sectionIndexW, String nameW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int forOrgIndex; 
if (forOrgIndexW == null) {
forOrgIndex = -1;
} else {
forOrgIndex = forOrgIndexW;
}

int thisPoolIndex; 
if (thisPoolIndexW == null) {
thisPoolIndex = -1;
} else {
thisPoolIndex = thisPoolIndexW;
}

int sectionIndex; 
if (sectionIndexW == null) {
sectionIndex = -1;
} else {
sectionIndex = sectionIndexW;
}

String name; 
if (nameW == null) {
name = M01_LDM.gc_seqNameOid;
} else {
name = nameW;
}

String returnValue;
if (sectionIndex < 0) {
sectionIndex = M01_Globals.g_sectionIndexMeta;
}

returnValue = M04_Utilities.genQualObjName(sectionIndex, name, name, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, (forOrgIndex > 0 ? M04_Utilities.genOrgId(forOrgIndex, ddlType, null) : ""), null, null);
return returnValue;
}


public static void genOidSequenceForOrg( int thisOrgIndex, int fileNo, Integer ddlTypeW, Integer forOrgIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int forOrgIndex; 
if (forOrgIndexW == null) {
forOrgIndex = -1;
} else {
forOrgIndex = forOrgIndexW;
}

int thisPoolIndex;
int orgSeqCacheSize;
int poolSeqCacheSize;
boolean orgIsTemplate;
int thisOrgId;
int forOrgId;

boolean isCtoSequence;
if (thisOrgIndex < 1) {
orgSeqCacheSize = M71_Org.g_orgs.descriptors[M01_Globals.g_primaryOrgIndex].sequenceCacheSize;
isCtoSequence = true;
orgIsTemplate = false;
thisOrgId = -1;
} else {
orgSeqCacheSize = M71_Org.g_orgs.descriptors[thisOrgIndex].sequenceCacheSize;
isCtoSequence = M71_Org.g_orgs.descriptors[thisOrgIndex].isPrimary &  (thisOrgIndex < 1);
orgIsTemplate = M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate;
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
}

if (isCtoSequence) {
M71_Org.genSequence("Sequence for Generating CTO-Object IDs", M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null), 0, fileNo, "9" + M01_LDM.gc_sequenceMinValue.substring(M01_LDM.gc_sequenceMinValue.length() - 1 - M01_LDM.gc_sequenceMinValue.length() - 1), "9" + M01_LDM.gc_sequenceMinValue.substring(M01_LDM.gc_sequenceMinValue.length() - 1 - M01_LDM.gc_sequenceMinValue.length() - 1), null, null, null, null, null);
M71_Org.genSequence("Sequence for Synchronization of VDF/XML-Export Jobs", M04_Utilities.genQualObjName(M01_Globals.g_sectionIndexMeta, "RunningNMB", "RunningNMB", ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null), 0, fileNo, "0", "0", null, null, null, 1, null);
return;
}

boolean forOrgIsTemplate;
forOrgIsTemplate = false;
if (forOrgIndex > 0) {
forOrgIsTemplate = M71_Org.g_orgs.descriptors[forOrgIndex].isTemplate;
forOrgId = M71_Org.g_orgs.descriptors[forOrgIndex].id;
} else {
forOrgId = -1;
}
if (!(forOrgIsTemplate)) {
for (int thisPoolIndex = 1; thisPoolIndex <= M72_DataPool.g_pools.numDescriptors; thisPoolIndex++) {
poolSeqCacheSize = M72_DataPool.g_pools.descriptors[thisPoolIndex].sequenceCacheSize;
if ((((M72_DataPool.g_pools.descriptors[thisPoolIndex].specificToOrgId == -1) |  (M72_DataPool.g_pools.descriptors[thisPoolIndex].specificToOrgId == thisOrgId)) &  M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal)) {
M71_Org.genSequence("Sequence for Generating Object IDs for Org \"" + M71_Org.g_orgs.descriptors[thisOrgIndex].name + "\" (MIG)", M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, forOrgIndex, thisPoolIndex, null, null), (forOrgIndex >= 0 ? forOrgId : 0), fileNo, null, M01_LDM.gc_sequenceStartValue, (forOrgIndex >= 0 ? M01_LDM.gc_sequenceEndValue : "8" + M01_LDM.gc_sequenceEndValue.substring(M01_LDM.gc_sequenceEndValue.length() - 1 - M01_LDM.gc_sequenceEndValue.length() - 1)), (orgSeqCacheSize > 1) |  (poolSeqCacheSize > 1), (orgSeqCacheSize > poolSeqCacheSize ? orgSeqCacheSize : poolSeqCacheSize), null, orgIsTemplate);
}
}

M71_Org.genSequence("Sequence for Generating Object IDs for Org \"" + (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate ? M04_Utilities.genTemplateParamWrapper(M71_Org.g_orgs.descriptors[thisOrgIndex].name, null) : M71_Org.g_orgs.descriptors[thisOrgIndex].name) + "\"", M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, forOrgIndex, null, null, null), (forOrgIndex >= 0 ? forOrgId : thisOrgId), fileNo, null, null, null, (orgSeqCacheSize > 1), orgSeqCacheSize, null, orgIsTemplate);
}
}


public static void genSequence(String comment, String qualSeqName, int seqNo, int fileNo, String startValueW, String minValueW, String maxValueW, Boolean useCachingW, Integer cacheSizeW, String incrementW, Boolean forTemplateW) {
String startValue; 
if (startValueW == null) {
startValue = M01_LDM.gc_sequenceStartValue;
} else {
startValue = startValueW;
}

String minValue; 
if (minValueW == null) {
minValue = M01_LDM.gc_sequenceMinValue;
} else {
minValue = minValueW;
}

String maxValue; 
if (maxValueW == null) {
maxValue = M01_LDM.gc_sequenceEndValue;
} else {
maxValue = maxValueW;
}

boolean useCaching; 
if (useCachingW == null) {
useCaching = true;
} else {
useCaching = useCachingW;
}

int cacheSize; 
if (cacheSizeW == null) {
cacheSize = 500;
} else {
cacheSize = cacheSizeW;
}

String increment; 
if (incrementW == null) {
increment = String.valueOf(M01_LDM.gc_sequenceIncrementValue);
} else {
increment = incrementW;
}

boolean forTemplate; 
if (forTemplateW == null) {
forTemplate = false;
} else {
forTemplate = forTemplateW;
}

if (!(M03_Config.generateDdlCreateSeq)) {
return;
}

String seqNoStr;
if (forTemplate) {
seqNoStr = (seqNo < 0 ? "" : M04_Utilities.genTemplateParamWrapper(String.valueOf(seqNo), null) + "");
} else {
seqNoStr = (seqNo < 0 ? "" : seqNo + "");
}

M22_Class_Utilities.printSectionHeader(comment, fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE SEQUENCE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualSeqName + " AS " + M01_Globals.g_dbtSequence);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "START WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + seqNoStr + startValue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INCREMENT BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + increment);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MINVALUE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + seqNoStr + minValue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MAXVALUE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + seqNoStr + maxValue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CYCLE");
if (useCaching) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CACHE " + String.valueOf(cacheSize));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CACHE");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}


public static void dropOrgsDdl(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

//On Error Resume Next 
if (M03_Config.generateLdm) {
M04_Utilities.killFile(M04_Utilities.genDdlFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDb, processingStep, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null), onlyIfEmpty);
}

if (M03_Config.generatePdm) {
int thisOrgIndex;
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genDdlFileName(M01_Globals.g_targetDir, M01_ACM.snDb, processingStep, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null, null, null, null), onlyIfEmpty);
}
}

NormalExit:
}


// ### IF IVK ###
public static void evalOrgs() {
int i;
for (i = 1; i <= 1; i += (1)) {
M71_Org.g_orgs.descriptors[i].setProductiveTargetPoolId = M01_Globals_IVK.g_productiveDataPoolId;
M71_Org.g_orgs.descriptors[i].setProductiveTargetPoolIndex = M72_DataPool.getDataPoolIndexById(M71_Org.g_orgs.descriptors[i].setProductiveTargetPoolId);
}
}
// ### ENDIF IVK ###


}