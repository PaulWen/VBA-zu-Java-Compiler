package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M93_DBDeploy {




private static final int processingStepDeploy = 4;
private static final int maxSubDirs = 30;


public static void genDbDeployPostprocess(Integer ddlType) {
if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDmlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, processingStepDeploy, M01_Common.DdlTypeId.edtPdm, null, null, "Deploy", M01_Common.phaseAliases);

//On Error GoTo ErrorExit 

M93_DBDeploy.genDbDeployPostprocessMeta(fileNo, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genDbDeployPostprocessMeta(int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String qualViewName;

qualViewName = M04_Utilities.genQualViewName(M22_Class.g_classes.descriptors[M01_Globals.g_classIndexLdmTable].sectionIndex, M01_ACM.vnLdmTabDepOrder, M01_ACM.vnsLdmTabDepOrder, ddlType, null, null, null, null, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("order LDM-tables according to their involvement in foreign key chains", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualTabNameLdmTable + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLdmFkSequenceNo + " =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualViewName + " V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + M01_Globals.g_anLdmTableName + " = V.SrcTable");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T." + M01_Globals.g_anLdmSchemaName + " = V.SrcSchema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

String qualProcName;

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnCreateLrtAliases, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("create Aliases for 'private-only' and 'public-only' tables", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcName + "(2, NULL, NULL, ?, ?)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbMonitor, M01_ACM.spnGenViewSnapshot, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("create snapshot-views", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcName + "(0)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (M03_Config.setDefaultCfgDuringDeployment) {
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnSetCfg, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("apply default DB configuration", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcName + "(1, ?, ?)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if (!(M03_Config.generateFwkTest)) {
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnSetTableCfg, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("configure table parameters", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcName + "(2, NULL, NULL, ?)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
// ### IF IVK ###

qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexPaiLog, M01_ACM_IVK.spnRssGetStatus, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("initialize RSS-Status-Tables", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcName + "('DEPLOY', ?)");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
// ### ENDIF IVK ###
}
// ### IF IVK ###

M22_Class_Utilities.printSectionHeader("update deployment history", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameApplVersion);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VERSION,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DEPLOYDATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DESCRIPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "'" + M03_Config.versionString + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CURRENT DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "'DDL-Deployment'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
// ### ENDIF IVK ###
}


public static void genCsvInventoryList(String dirPath, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

String match;
String[] matchElems;
String schemaName;
String tabName;
String fileNameList;
int fileNo;

fileNo = -1;
fileNameList = dirPath + "\\db2csv.lst";

match = dir(dirPath + "\\*CSV", attributes);
if (match.compareTo("") == 0) {
return;
}

//On Error GoTo ErrorExit 
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileNameList, false);

do {matchElems = match.split(".", );
tabName = matchElems[1];
matchElems = matchElems[0].split("-", );
schemaName = matchElems[2];

if (!(schemaName.compareTo("") == 0) &  !(tabName.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, "!\"" + schemaName + "\".\"" + tabName + "\"!" + match + "!");
}

match = dir;// next entry.
} while (!(match.compareTo("") == 0));

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genCsvInventoryListsRecursive(String dirPath, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

String match;
String[] matchElems;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;

match = dir(dirPath + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0)) {
if ((GetAttr(dirPath + "\\" + match) &  vbDirectory) == vbDirectory) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

int i;
for (int i = 1; i <= numSubDirs; i++) {
M93_DBDeploy.genCsvInventoryList(dirPath + "\\" + subDirs[i], attributes);
M93_DBDeploy.genCsvInventoryListsRecursive(dirPath + "\\" + subDirs[i], attributes);
}
}


public static void genCsvInventoryLists() {
if (M03_Config.generateLdm) {
M93_DBDeploy.genCsvInventoryListsRecursive(M01_Globals.g_targetDir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\LDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : ""), null);
}

if (M03_Config.generatePdm) {
M93_DBDeploy.genCsvInventoryListsRecursive(M01_Globals.g_targetDir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : ""), null);
}
}


public static void dropCsvInventoryList(String dirPath) {
String match;

match = dir(dirPath + "\\db2csv.lst", vbNormal);
if (!(match.compareTo("") == 0)) {
M04_Utilities.killFile(dirPath + "\\" + match, null);
}

}


public static void dropCsvInventoryListsRecursive(String dirPath, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

String match;
String[] matchElems;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;

match = dir(dirPath + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0)) {
if ((GetAttr(dirPath + "\\" + match) &  vbDirectory) == vbDirectory) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

int i;
for (int i = 1; i <= numSubDirs; i++) {
M93_DBDeploy.dropCsvInventoryList(dirPath + "\\" + subDirs[i]);
M93_DBDeploy.dropCsvInventoryListsRecursive(dirPath + "\\" + subDirs[i], null);
}
}

public static void dropCsvInventoryLists() {
if (M03_Config.generateLdm) {
M93_DBDeploy.dropCsvInventoryListsRecursive(M01_Globals.g_targetDir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\LDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : ""), null);
}

if (M03_Config.generatePdm) {
M93_DBDeploy.dropCsvInventoryListsRecursive(M01_Globals.g_targetDir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : ""), null);
}
}


}