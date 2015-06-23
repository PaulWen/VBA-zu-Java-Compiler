package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M98_Trace {


// ### IF IVK ###


private static final int processingStepTrace = 4;

private static class tempTabMapping {
public int classIndex;
public String qualTabName;
public String tempTabName;
public String idAttrName;

public tempTabMapping(int classIndex, String qualTabName, String tempTabName, String idAttrName) {
this.classIndex = classIndex;
this.qualTabName = qualTabName;
this.tempTabName = tempTabName;
this.idAttrName = idAttrName;
}
}


private static void setTabMapping(tempTabMapping mapping,  int classIndex, String tempTabName, Integer ddlType,  int thisOrgIndex,  int thisPoolIndex) {
mapping.classIndex = classIndex;
if (M22_Class.g_classes.descriptors[classIndex].attrRefs.numDescriptors > 0) {
// implicit assumption: ID-column is the first column in table
mapping.idAttrName = M24_Attribute.g_attributes.descriptors[M22_Class.g_classes.descriptors[classIndex].attrRefs.descriptors[1].refIndex].attributeName;
} else {
mapping.idAttrName = "";
}
mapping.qualTabName = M04_Utilities.genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);
mapping.tempTabName = tempTabName;
}


public static void genTraceDdl(Integer ddlType) {
if (ddlType == M01_Common.DdlTypeId.edtPdm) {
int thisOrgIndex;
int thisPoolIndex;

for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
M98_Trace.genTraceDdlByPool(M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
}
}


public static void genTraceDdlByPool(Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

if (M03_Config.generateFwkTest) {
return;
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


//On Error GoTo ErrorExit 

boolean nothingToDo;
nothingToDo = true;
final int numClasses = 5;
tempTabMapping[] tabMapping = new tempTabMapping[numClasses];
setTabMapping(tabMapping[1], M01_Globals_IVK.g_classIndexFtoChangelogSummary, M01_Globals_IVK.gc_tempTabNameChangeLogSummary, ddlType, thisOrgIndex, thisPoolIndex);
setTabMapping(tabMapping[2], M01_Globals_IVK.g_classIndexFtoOrgChangelogSummary, M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary, ddlType, thisOrgIndex, thisPoolIndex);
setTabMapping(tabMapping[3], M01_Globals_IVK.g_classIndexFtoOrgImplicitChangesSummary, M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges, ddlType, thisOrgIndex, thisPoolIndex);
setTabMapping(tabMapping[4], M01_Globals_IVK.g_classIndexSpAffectedEntity, M01_Globals_IVK.gc_tempTabNameSpAffectedEntities, ddlType, thisOrgIndex, thisPoolIndex);
setTabMapping(tabMapping[5], M01_Globals_IVK.g_classIndexSpFilteredEntity, M01_Globals_IVK.gc_tempTabNameSpFilteredEntities, ddlType, thisOrgIndex, thisPoolIndex);

int i;
for (int i = 1; i <= numClasses; i++) {
if (M22_Class.g_classes.descriptors[tabMapping[i].classIndex].specificToOrgId <= 0 |  M22_Class.g_classes.descriptors[tabMapping[i].classIndex].specificToOrgId == thisOrgId) {
if (M22_Class.g_classes.descriptors[tabMapping[i].classIndex].specificToPool < 0 |  M22_Class.g_classes.descriptors[tabMapping[i].classIndex].specificToPool == thisPoolId) {
nothingToDo = false;
}
}
}

if (nothingToDo) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexTrace, processingStepTrace, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseDbSupport, null);

// ####################################################################################################################
// #    SP for Persisting Trace Tables
// ####################################################################################################################

String qualProcNameTracePersist;
qualProcNameTracePersist = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexTrace, M01_ACM_IVK.spnTracePersist, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Persisting Trace Tables", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameTracePersist);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "OUT", "traceId_out", "BIGINT", true, "ID used to identify persisted records related to this procedure call");
M11_LRT.genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", true, "number of non-empty temporary tables persisted");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows persisted");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_traceId", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;");

M11_LRT.genProcSectionHeader(fileNo, "declare temporary temporary tables", null, null);
for (int i = 1; i <= numClasses; i++) {
if (i > 1) {
M00_FileWriter.printToFile(fileNo, "");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabMapping[i].tempTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LIKE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabMapping[i].qualTabName);

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, 1, false, null, null);
}

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameTracePersist, ddlType, null, "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET tabCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET traceId_out = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "determine trace ID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_traceId = NEXTVAL FOR " + M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null) + ";");

M11_LRT.genProcSectionHeader(fileNo, "persist records in temporary tables", null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;
for (int i = 1; i <= numClasses; i++) {
if (i > 1) {
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabMapping[i].qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(tabMapping[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, tabMapping[i].idAttrName, "v_traceId", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(tabMapping[i].classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tabMapping[i].tempTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows and tables", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF rowCount_out > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET tabCount_out = tabCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

}

M11_LRT.genProcSectionHeader(fileNo, "set output trace ID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET traceId_out = v_traceId;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameTracePersist, ddlType, null, "tabCount_out", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### ENDIF IVK ###


}