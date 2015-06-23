package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_DbMeta {




class LdmFk {
public String srcSchema;
public String srcTable;
public String dstSchema;
public String dstTable;
public boolean isEnforced;

public LdmFk(String srcSchema, String srcTable, String dstSchema, String dstTable, boolean isEnforced) {
this.srcSchema = srcSchema;
this.srcTable = srcTable;
this.dstSchema = dstSchema;
this.dstTable = dstTable;
this.isEnforced = isEnforced;
}
}

class CheckFk {
public String srcQualTableName;
public String dstQualTableName;
public String srcAttrSeq;
public boolean isEnforced;

public CheckFk(String srcQualTableName, String dstQualTableName, String srcAttrSeq, boolean isEnforced) {
this.srcQualTableName = srcQualTableName;
this.dstQualTableName = dstQualTableName;
this.srcAttrSeq = srcAttrSeq;
this.isEnforced = isEnforced;
}
}

class LdmFks {
public int numFks;
public M78_DbMeta.LdmFk[] fks;

public LdmFks(int numFks, M78_DbMeta.LdmFk[] fks) {
this.numFks = numFks;
this.fks = fks;
}
}

class CheckFks {
public int numFks;
public M78_DbMeta.CheckFk[] fks;

public CheckFks(int numFks, M78_DbMeta.CheckFk[] fks) {
this.numFks = numFks;
this.fks = fks;
}
}

public static M78_DbMeta.LdmFks g_ldmFks;

public static M78_DbMeta.CheckFks g_checkFks;

private static final int pdmCsvProcessingStep = 3;

private static int fileNoCsvLdmTable;
private static int fileNoCsvPdmTable;


public static void closeCsvFilesLPdmTable() {
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoCsvLdmTable);
M00_FileWriter.closeFile(fileNoCsvPdmTable);

fileNoCsvLdmTable = -1;
fileNoCsvPdmTable = -1;
}


public static void initGLdmFks() {
initLdmFks(M78_DbMeta.g_ldmFks);
initCheckFks(M78_DbMeta.g_checkFks);
}


private static void initLdmFks(M78_DbMeta.LdmFks fks) {
fks.numFks = 0;
}

private static void initCheckFks(M78_DbMeta.CheckFks fks) {
fks.numFks = 0;
}


public static Integer allocLdmFkIndex(M78_DbMeta.LdmFks fks) {
Integer returnValue;
returnValue = -1;

if (fks.numFks == 0) {
fks.fks =  new M78_DbMeta.LdmFks[M01_Common.gc_allocBlockSize];
} else if (fks.numFks >= M00_Helper.uBound(fks.fks)) {
M78_DbMeta.LdmFks[] fksBackup = fks.fks;
fks.fks =  new M78_DbMeta.LdmFks[fks.numFks + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M78_DbMeta.LdmFks value : fksBackup) {
fks.fks[indexCounter] = value;
indexCounter++;
}
}
fks.numFks = fks.numFks + 1;
returnValue = fks.numFks;
return returnValue;
}

public static Integer allocCheckFkIndex(M78_DbMeta.CheckFks fks) {
Integer returnValue;
returnValue = -1;

if (fks.numFks == 0) {
fks.fks =  new M78_DbMeta.CheckFks[M01_Common.gc_allocBlockSize];
} else if (fks.numFks >= M00_Helper.uBound(fks.fks)) {
M78_DbMeta.CheckFks[] fksBackup = fks.fks;
fks.fks =  new M78_DbMeta.CheckFks[fks.numFks + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M78_DbMeta.CheckFks value : fksBackup) {
fks.fks[indexCounter] = value;
indexCounter++;
}
}
fks.numFks = fks.numFks + 1;
returnValue = fks.numFks;
return returnValue;
}


private static void addLdmFk(String srcSchema, String srcTable, String dstSchema, String dstTable, Boolean isEnforcedW) {
boolean isEnforced; 
if (isEnforcedW == null) {
isEnforced = true;
} else {
isEnforced = isEnforcedW;
}

int i;
for (int i = 1; i <= M78_DbMeta.g_ldmFks.numFks; i++) {
if (M78_DbMeta.g_ldmFks.fks[i].srcSchema.compareTo(srcSchema) == 0 &  M78_DbMeta.g_ldmFks.fks[i].srcTable.compareTo(srcTable) == 0 & M78_DbMeta.g_ldmFks.fks[i].dstSchema.compareTo(dstSchema) == 0 & M78_DbMeta.g_ldmFks.fks[i].dstTable.compareTo(dstTable) == 0) {
return;
}
}
M78_DbMeta.g_ldmFks.fks[M78_DbMeta.allocLdmFkIndex(M78_DbMeta.g_ldmFks)].srcSchema = srcSchema;
M78_DbMeta.g_ldmFks.fks[M78_DbMeta.allocLdmFkIndex(M78_DbMeta.g_ldmFks)].srcTable = srcTable;
M78_DbMeta.g_ldmFks.fks[M78_DbMeta.allocLdmFkIndex(M78_DbMeta.g_ldmFks)].dstSchema = dstSchema;
M78_DbMeta.g_ldmFks.fks[M78_DbMeta.allocLdmFkIndex(M78_DbMeta.g_ldmFks)].dstTable = dstTable;
M78_DbMeta.g_ldmFks.fks[M78_DbMeta.allocLdmFkIndex(M78_DbMeta.g_ldmFks)].isEnforced = isEnforced;
}

private static void addCheckFk(String srcQualTableName, String dstQualTableName, String srcAttrSeq, Boolean isEnforcedW) {
boolean isEnforced; 
if (isEnforcedW == null) {
isEnforced = true;
} else {
isEnforced = isEnforcedW;
}

int i;
for (int i = 1; i <= M78_DbMeta.g_checkFks.numFks; i++) {
if (M78_DbMeta.g_checkFks.fks[i].srcQualTableName.compareTo(srcQualTableName) == 0 &  M78_DbMeta.g_checkFks.fks[i].dstQualTableName.compareTo(dstQualTableName) == 0 & M78_DbMeta.g_checkFks.fks[i].srcAttrSeq.compareTo(srcAttrSeq) == 0) {
return;
}
}
M78_DbMeta.g_checkFks.fks[M78_DbMeta.allocCheckFkIndex(M78_DbMeta.g_checkFks)].srcQualTableName = srcQualTableName;
M78_DbMeta.g_checkFks.fks[M78_DbMeta.allocCheckFkIndex(M78_DbMeta.g_checkFks)].dstQualTableName = dstQualTableName;
M78_DbMeta.g_checkFks.fks[M78_DbMeta.allocCheckFkIndex(M78_DbMeta.g_checkFks)].srcAttrSeq = srcAttrSeq;
M78_DbMeta.g_checkFks.fks[M78_DbMeta.allocCheckFkIndex(M78_DbMeta.g_checkFks)].isEnforced = isEnforced;
}
public static void registerCheckFk(String srcQualTableName, String dstQualTableName, String srcAttrSeq, Boolean isEnforcedW) {
boolean isEnforced; 
if (isEnforcedW == null) {
isEnforced = true;
} else {
isEnforced = isEnforcedW;
}

addCheckFk(srcQualTableName, dstQualTableName, srcAttrSeq, isEnforced);
}

private static void registerLdmFk(String srcSchemaName, String srcTabName, String dstSchemaName, String dstTabName, int srcAcmEntityIndex, Integer srcAcmEntityType, Boolean dstNotAcmRelatedW, Boolean forGenW, Boolean isEnforcedW) {
boolean dstNotAcmRelated; 
if (dstNotAcmRelatedW == null) {
dstNotAcmRelated = false;
} else {
dstNotAcmRelated = dstNotAcmRelatedW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean isEnforced; 
if (isEnforcedW == null) {
isEnforced = true;
} else {
isEnforced = isEnforcedW;
}

addLdmFk(srcSchemaName, srcTabName, dstSchemaName, dstTabName, isEnforced);
}


private static void genLdmFksCsv(Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int fileNo;
String fileName;
fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnFkDependency, M22_Class.ldmCsvFkProcessingStep, "LDM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

//On Error GoTo ErrorExit 
M00_FileWriter.openFileForOutput(fileNo, fileName, true);

int i;
for (int i = 1; i <= M78_DbMeta.g_ldmFks.numFks; i++) {
M00_FileWriter.printToFile(fileNo, (M78_DbMeta.g_ldmFks.fks[i].isEnforced ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbMeta.g_ldmFks.fks[i].srcTable.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbMeta.g_ldmFks.fks[i].srcSchema.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbMeta.g_ldmFks.fks[i].dstTable.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, "\"" + M78_DbMeta.g_ldmFks.fks[i].dstSchema.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.getCsvTrailer(0));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genLdmFksCsvs() {
if (M03_Config.generateLdm) {
genLdmFksCsv(M01_Common.DdlTypeId.edtLdm);
}

if (M03_Config.generatePdm) {
genLdmFksCsv(M01_Common.DdlTypeId.edtPdm);
}
}


public static void registerQualLdmFk(String qualLdmSrcTableName, String qualLdmDstTableName, int srcAcmEntityIndex, Integer srcAcmEntityType, Boolean dstNotAcmRelatedW, Boolean forGenW, Boolean isEnforcedW) {
boolean dstNotAcmRelated; 
if (dstNotAcmRelatedW == null) {
dstNotAcmRelated = false;
} else {
dstNotAcmRelated = dstNotAcmRelatedW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean isEnforced; 
if (isEnforcedW == null) {
isEnforced = true;
} else {
isEnforced = isEnforcedW;
}

if (dstNotAcmRelated) {
return;
}

if (srcAcmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if (M22_Class.g_classes.descriptors[srcAcmEntityIndex].notAcmRelated) {
return;
}
} else if (srcAcmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if (M23_Relationship.g_relationships.descriptors[srcAcmEntityIndex].notAcmRelated |  M23_Relationship.g_relationships.descriptors[srcAcmEntityIndex].isNotEnforced) {
return;
}
} else if (srcAcmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
if (M21_Enum.g_enums.descriptors[srcAcmEntityIndex].notAcmRelated) {
return;
}
}

String srcSchemaName;
String srcTabName;
String dstSchemaName;
String dstTabName;

M78_DbMeta.splitQualifiedName(qualLdmSrcTableName, srcSchemaName, srcTabName);
M78_DbMeta.splitQualifiedName(qualLdmDstTableName, dstSchemaName, dstTabName);
registerLdmFk(srcSchemaName, srcTabName, dstSchemaName, dstTabName, srcAcmEntityIndex, srcAcmEntityType, dstNotAcmRelated, forGen, isEnforced);
}


private static void registerLdmTable(String M01_ACM.clnLdmSchema, String ldmTabName, int rootAcmEntityIndex, int acmEntityIndex, Integer acmEntityType, Integer ddlTypeW, Boolean isGenW, Boolean isLrtW, Boolean isNlW, Boolean isMqtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean isGen; 
if (isGenW == null) {
isGen = false;
} else {
isGen = isGenW;
}

boolean isLrt; 
if (isLrtW == null) {
isLrt = false;
} else {
isLrt = isLrtW;
}

boolean isNl; 
if (isNlW == null) {
isNl = false;
} else {
isNl = isNlW;
}

boolean isMqt; 
if (isMqtW == null) {
isMqt = false;
} else {
isMqt = isMqtW;
}

String acmSectionName;
String acmEntityName;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
if ((!(isLrt &  M22_Class.g_classes.descriptors[rootAcmEntityIndex].isLdmCsvExported)) |  (isLrt &  M22_Class.g_classes.descriptors[rootAcmEntityIndex].isLdmLrtCsvExported) | M22_Class.g_classes.descriptors[rootAcmEntityIndex].notAcmRelated) {
return;
}
acmSectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
if (M21_Enum.g_enums.descriptors[rootAcmEntityIndex].isLdmCsvExported |  M21_Enum.g_enums.descriptors[rootAcmEntityIndex].notAcmRelated) {
return;
}
acmSectionName = M21_Enum.g_enums.descriptors[acmEntityIndex].sectionName;
acmEntityName = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
if ((!(isLrt &  M23_Relationship.g_relationships.descriptors[rootAcmEntityIndex].isLdmCsvExported)) |  (isLrt &  M23_Relationship.g_relationships.descriptors[rootAcmEntityIndex].isLdmLrtCsvExported) | M23_Relationship.g_relationships.descriptors[rootAcmEntityIndex].notAcmRelated) {
return;
}
acmSectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
}

//On Error GoTo ErrorExit 

if (fileNoCsvLdmTable < 1) {
String fileName;
fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnLdmTable, M22_Class.ldmCsvTableProcessingStep, "LDM", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNoCsvLdmTable = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNoCsvLdmTable, fileName, true);
}

M00_FileWriter.printToFile(fileNoCsvLdmTable, "\"" + ldmTabName.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, ",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, (isNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, (isGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, (isLrt ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, (isMqt ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse) + ",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, "\"" + acmSectionName.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, "\"" + acmEntityName.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, "\"" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "\",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, "\"" + M01_ACM.clnLdmSchema.trim().toUpperCase() + "\",");
M00_FileWriter.printToFile(fileNoCsvLdmTable, M04_Utilities.getCsvTrailer(0));

NormalExit:
//On Error Resume Next 
// leave file open
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void splitQualifiedName(String qualifiedName, String qualifier, String unqualifiedName) {
String[] elems;
elems = qualifiedName.split(".", );

qualifier = "";
unqualifiedName = "";
if (M00_Helper.uBound(elems) == 1) {
qualifier = elems[0];
unqualifiedName = elems[1];
}
}


private static void registerPdmTable(String qualRefObjNamePdm, String qualRefObjNameLdm,  int thisOrgIndex,  int thisPoolIndex) {
String qualifierLdm;
String nameLdm;
String qualifierPdm;
String namePdm;

M78_DbMeta.splitQualifiedName(qualRefObjNameLdm, qualifierLdm, nameLdm);
M78_DbMeta.splitQualifiedName(qualRefObjNamePdm, qualifierPdm, namePdm);

if (M71_Org.getOrgIsTemplate(thisOrgIndex)) {
// we do not create CSV for template Orgs
return;
}

//On Error GoTo ErrorExit 

if (fileNoCsvPdmTable < 1) {
String fileNameCsv;
fileNameCsv = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, M01_ACM.clnPdmTable, pdmCsvProcessingStep, "PDM", M01_Common.DdlTypeId.edtPdm, null, null, null, null, thisOrgIndex);
M04_Utilities.assertDir(fileNameCsv);

fileNoCsvPdmTable = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoCsvPdmTable, fileNameCsv, true);
}

String orgIdStr;
if (thisOrgIndex <= 0) {
orgIdStr = "";
} else {
if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
orgIdStr = M04_Utilities.genTemplateParamWrapper(String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id), null);
} else {
orgIdStr = String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id);
}
}

M00_FileWriter.printToFile(fileNoCsvPdmTable, "\"" + namePdm + "\",");
M00_FileWriter.printToFile(fileNoCsvPdmTable, orgIdStr + ",");
if (thisPoolIndex > 0) {
M00_FileWriter.printToFile(fileNoCsvPdmTable, String.valueOf(M72_DataPool.g_pools.descriptors[thisPoolIndex].id) + ",");
} else {
M00_FileWriter.printToFile(fileNoCsvPdmTable, ",");
}
M00_FileWriter.printToFile(fileNoCsvPdmTable, "\"" + nameLdm + "\",");
M00_FileWriter.printToFile(fileNoCsvPdmTable, "\"" + qualifierLdm + "\",");
M00_FileWriter.printToFile(fileNoCsvPdmTable, "\"" + qualifierPdm + "\",");
M00_FileWriter.printToFile(fileNoCsvPdmTable, M04_Utilities.getCsvTrailer(0));

NormalExit:
//On Error Resume Next 
// leave file open
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void registerQualTable(String qualLdmTableName, String qualPdmTableName, int rootAcmEntityIndex, int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, Integer ddlTypeW, Boolean notAcmRelatedW, Boolean isGenW, Boolean isLrtW, Boolean isNlW, Boolean isMqtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean notAcmRelated; 
if (notAcmRelatedW == null) {
notAcmRelated = false;
} else {
notAcmRelated = notAcmRelatedW;
}

boolean isGen; 
if (isGenW == null) {
isGen = false;
} else {
isGen = isGenW;
}

boolean isLrt; 
if (isLrtW == null) {
isLrt = false;
} else {
isLrt = isLrtW;
}

boolean isNl; 
if (isNlW == null) {
isNl = false;
} else {
isNl = isNlW;
}

boolean isMqt; 
if (isMqtW == null) {
isMqt = false;
} else {
isMqt = isMqtW;
}

String ldmSchemaName;
String ldmTableName;

M78_DbMeta.splitQualifiedName(qualLdmTableName, ldmSchemaName, ldmTableName);
registerLdmTable(ldmSchemaName, ldmTableName, rootAcmEntityIndex, acmEntityIndex, acmEntityType, ddlType, isGen, isLrt, isNl, isMqt);

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! notAcmRelated) {
registerPdmTable(qualPdmTableName, qualLdmTableName, thisOrgIndex, thisPoolIndex);
}
}




}