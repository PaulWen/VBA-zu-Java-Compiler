package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M06_Packaging {




private static final int maxSubDirs = 30;

private static final String dirInstall = "install";
private static final String dirScripts = "scripts";
private static final String dirTemplate = "template";
private static final String dirData = "data";
private static final String dirEtc = "etc";

private static final String fileReadMe = "readme.txt";
private static final String fileFilesystemSetup = "00_please_edit_filesystem-setup.sh";
private static final String fileCreateDb = "01_please_edit_create_db.sql";
private static final String fileCreateTs = "02_please_edit_create_tablespaces.sql";
private static final String fileUpdDbCfg = "03_please_edit_update_db_cfg.sql";
private static final String fileUpdDbProfile = "03_update_db_profile.sh";
private static final String fileCreateExtSps = "04_please_edit_create_ext_storedprocs.sql";
private static final String fileCreateExtSpsSh = "04_please_edit_create_ext_storedprocs.sh";
private static final String fileCreateObjects = "05_create_db_objects.sql";
private static final String fileCreateObjectsTemplate = "05_create_db_objects-template.sql";
private static final String fileImportData = "06_import_data.sql";
private static final String fileInitMeta = "07_init_meta_data.sql";
private static final String fileGrant = "08_grant.sql";
private static final String fileRebindCli = "09_rebind_cli.sql";

private static final String fnSuffixTmp = ".tmp";
private static final String fnSuffixJar = ".jar";

public class DeployPackageType {
public static final int edptFullDeployment = 1.0;
public static final int edptUpdate = 2.0;
}


public static void genPackages() {
if (M03_Config.generateLdm) {
if (M03_Config.generateNonLrt) {
M06_Packaging.genPackageByDdlType(M01_Common.DdlTypeId.edtLdm, false, null);
}
if (M03_Config.generateLrt) {
M06_Packaging.genPackageByDdlType(M01_Common.DdlTypeId.edtLdm, true, null);
}
}

if (M03_Config.generatePdm) {
if (M03_Config.generateNonLrt) {
M06_Packaging.genPackageByDdlType(M01_Common.DdlTypeId.edtPdm, false, null);
}
if (M03_Config.generateLrt) {
M06_Packaging.genPackageByDdlType(M01_Common.DdlTypeId.edtPdm, true, null);
}
}
}


// ####################################################################################################################
// #    utilities
// ####################################################################################################################

private static void genScriptHeader(int fileNo, String fileName, String description, Boolean forShellW, Boolean ignoreTsW) {
boolean forShell; 
if (forShellW == null) {
forShell = false;
} else {
forShell = forShellW;
}

boolean ignoreTs; 
if (ignoreTsW == null) {
ignoreTs = false;
} else {
ignoreTs = ignoreTsW;
}

String linePrefix;
if (forShell) {
linePrefix = "";
M00_FileWriter.printToFile(fileNo, "#!/bin/ksh" + vbLf);
M00_FileWriter.printToFile(fileNo,  + vbLf);
} else {
linePrefix = "-- ";
}

M00_FileWriter.printToFile(fileNo, linePrefix + M01_LDM.gc_sqlDelimLine1 + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#" + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Script      : " + M04_Utilities.baseName(fileName, null, null, null, null) + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Version     : " + M03_Config.versionString + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Contact     : " + M79_KwMap.kwTranslate("<contactCompany>") + ", " + M79_KwMap.kwTranslate("<contactPerson>") + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Description : " + description + vbLf);

if (forShell) {
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Usage       : " + M04_Utilities.baseName(fileName, null, null, null, null) + vbLf);
} else {
M00_FileWriter.printToFile(fileNo, linePrefix + "#  Usage       : db2 -td@ -f " + M04_Utilities.baseName(fileName, null, null, null, null) + vbLf);
}

M00_FileWriter.printToFile(fileNo, linePrefix + "#  History     :" + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + "#" + vbLf);
M00_FileWriter.printToFile(fileNo, linePrefix + M01_LDM.gc_sqlDelimLine1 + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);

if (!(forShell & ! ignoreTs)) {
M00_FileWriter.printToFile(fileNo, "UPDATE COMMAND OPTIONS USING V OFF" + M01_LDM.gc_sqlCmdDelim + vbLf);
//    Print #fileNo, "UPDATE COMMAND OPTIONS USING S ON"; gc_sqlCmdDelim; vbLf;
M00_FileWriter.printToFile(fileNo, "UPDATE COMMAND OPTIONS USING N ON" + M01_LDM.gc_sqlCmdDelim + vbLf);
M00_FileWriter.printToFile(fileNo, "UPDATE COMMAND OPTIONS USING X ON" + M01_LDM.gc_sqlCmdDelim + vbLf);

M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, "VALUES" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*******************************************************************************************' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'* Begin of script execution (" + M04_Utilities.baseName(fileName, null, null, null, null) + "): ' || CURRENT TIMESTAMP || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*******************************************************************************************' || CHR(10)" + vbLf);
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
}
}


private static void genScriptTrailer(int fileNo, String fileName, Boolean forShellW, Boolean forTemplateW) {
boolean forShell; 
if (forShellW == null) {
forShell = false;
} else {
forShell = forShellW;
}

boolean forTemplate; 
if (forTemplateW == null) {
forTemplate = false;
} else {
forTemplate = forTemplateW;
}

String linePrefix;
if (forShell) {
linePrefix = "";
} else {
linePrefix = "-- ";
}

if (forTemplate) {
M22_Class_Utilities.printSectionHeader("update deployment history of template", fileNo, null, null);
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
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "'" + M03_Config.versionString + " [" + M04_Utilities.genTemplateParamWrapper("1", null) + "]',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CURRENT DATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "'MPC " + M04_Utilities.genTemplateParamWrapper("1", null) + " created'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}

if (!(forShell)) {
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, "VALUES" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*******************************************************************************************' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'* End of script execution (" + M04_Utilities.baseName(fileName, null, null, null, null) + "): ' || CURRENT TIMESTAMP || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*' || CHR(10) ||" + vbLf);
M00_FileWriter.printToFile(fileNo, "'*******************************************************************************************' || CHR(10)" + vbLf);
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
}
}


private static void mapConditionalDdlSections(String text, boolean condition, String keyword) {
String startStr;
String endStr;
int startPos;
int endPos;

// first remove sections which do not apply
if (condition) {
startStr = "-- $$IF NOT " + keyword.toUpperCase() + "$$";
endStr = "-- $$END IF NOT " + keyword.toUpperCase() + "$$";
} else {
startStr = "-- $$IF " + keyword.toUpperCase() + "$$";
endStr = "-- $$END IF " + keyword.toUpperCase() + "$$";
}

startPos = M00_Helper.inStr(1, text, startStr, vbTextCompare);
endPos = M00_Helper.inStr(1, text, endStr, vbTextCompare);

while (startPos > 0 &  endPos > 0) {
text = text.substring(0, startPos - 1) + text.substring(text.length() - 1 - text.length() - endPos - endStr.length() - 1);

startPos = M00_Helper.inStr(1, text, startStr, vbTextCompare);
endPos = M00_Helper.inStr(1, text, endStr, vbTextCompare);
}

// second remove escape sequences for sections which do apply
if (condition) {
startStr = "-- $$IF " + keyword.toUpperCase() + "$$";
endStr = "-- $$END IF " + keyword.toUpperCase() + "$$";
} else {
startStr = "-- $$IF NOT " + keyword.toUpperCase() + "$$";
endStr = "-- $$END IF NOT " + keyword.toUpperCase() + "$$";
}

startPos = M00_Helper.inStr(1, text, startStr, vbTextCompare);
while (startPos > 0) {
text = text.substring(0, startPos - 1) + text.substring(text.length() - 1 - text.length() - startPos - startStr.length() - 1);
startPos = M00_Helper.inStr(1, text, startStr, vbTextCompare);
}

endPos = M00_Helper.inStr(1, text, endStr, vbTextCompare);
while (endPos > 0) {
text = text.substring(0, endPos - 1) + text.substring(text.length() - 1 - text.length() - endPos - endStr.length() - 1);
endPos = M00_Helper.inStr(1, text, endStr, vbTextCompare);
}
}

private static void catFile(String fileNameIn, int fileNoOut, Boolean mapKeyWordsW, Boolean mapConditionalSectionsW) {
boolean mapKeyWords; 
if (mapKeyWordsW == null) {
mapKeyWords = false;
} else {
mapKeyWords = mapKeyWordsW;
}

boolean mapConditionalSections; 
if (mapConditionalSectionsW == null) {
mapConditionalSections = false;
} else {
mapConditionalSections = mapConditionalSectionsW;
}

int fileNo;

//On Error GoTo ErrorExit 

fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForInput(fileNo, fileNameIn));

String text;
text = M00_FileWriter.textOfFile(fileNo);

if (mapConditionalSections) {
mapConditionalDdlSections(text, M03_Config.supportSpLogging, "SPLOGGING");
}

if (mapKeyWords) {
M00_FileWriter.printToFile(fileNoOut, M00_Helper.replace(M79_KwMap.kwTranslate(text), vbCr, ""));
} else {
M00_FileWriter.printToFile(fileNoOut, M00_Helper.replace(text, vbCr, ""));
}

if ((text.substring(text.length() - 1 - 1) == vbLf) |  (text.substring(text.length() - 1 - 1) == " " &  text.substring(text.length() - 1 - 2).substring(0, 1) == vbLf)) {
//
} else {
M00_FileWriter.printToFile(fileNoOut, vbLf);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void catDdlsInDir(int fileNoOutCrTs, int fileNoOutCrObj, String dirPath, Integer ddlType, Boolean mapKeyWordsW, Integer attributesW, Boolean mapConditionalSectionsW) {
boolean mapKeyWords; 
if (mapKeyWordsW == null) {
mapKeyWords = false;
} else {
mapKeyWords = mapKeyWordsW;
}

int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

boolean mapConditionalSections; 
if (mapConditionalSectionsW == null) {
mapConditionalSections = false;
} else {
mapConditionalSections = mapConditionalSectionsW;
}

//On Error GoTo ErrorExit 

String match;
String tsDdlFileName;
tsDdlFileName = M73_TableSpace.getTableSpaceDdlBaseFileName(ddlType);
String bpDdlFileName;
bpDdlFileName = M75_BufferPool.getBufferPoolDdlBaseFileName(ddlType);

match = dir(dirPath + "\\*DDL", attributes);

do {if (match.substring(0, 3).toLowerCase() == "ivk") {
// ignore
} else if (M00_Helper.inStr(1, match.toLowerCase(), "splog") & ! (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile)) {
// ignore
} else {
if ((fileNoOutCrTs > 0) &  (match.compareTo(tsDdlFileName) == 0 |  match.compareTo(bpDdlFileName) == 0)) {
catFile(dirPath + "\\" + match, fileNoOutCrTs, mapKeyWords, null);
} else {
catFile(dirPath + "\\" + match, fileNoOutCrObj, mapKeyWords, mapConditionalSections);
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

NormalExit:
return;

ErrorExit:
//On Error Resume Next 
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void catDdlsInDirRecursive(int fileNoOutCrTs, int fileNoOutCrObj, String dirPath, Integer ddlType, Boolean mapKeyWordsW, Integer attributesW,  Boolean mapConditionalSectionsW) {
boolean mapKeyWords; 
if (mapKeyWordsW == null) {
mapKeyWords = false;
} else {
mapKeyWords = mapKeyWordsW;
}

int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

boolean mapConditionalSections; 
if (mapConditionalSectionsW == null) {
mapConditionalSections = false;
} else {
mapConditionalSections = mapConditionalSectionsW;
}


String match;
String baseDirName;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;
baseDirName = M04_Utilities.baseName(dirPath, null, null, null, null);

if ((baseDirName.toUpperCase() == "EXPLAIN") & ! M03_Config.includeExplainDdlInDeliveryPackage) {
return;
}

// ### IF IVK ###
if ((baseDirName.toUpperCase() == "SST-TEST") & ! M03_Config.supportSstCheck) {
return;
}

if ((baseDirName.toUpperCase() == "MIGRATION") |  (baseDirName.toUpperCase() == "VDOKF") | (baseDirName.toUpperCase() == "EXPLAIN")) {
mapConditionalSections = true;
}

// ### ENDIF IVK ###
match = dir(dirPath + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0) & match.toUpperCase() != ".SVN" & match.toUpperCase() != "DROP" & match.toUpperCase() != "TEMPLATE" & match.toUpperCase() != "DEPLOY") {
if ((GetAttr(dirPath + "\\" + match) &  vbDirectory) == vbDirectory) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

catDdlsInDir(fileNoOutCrTs, fileNoOutCrObj, dirPath, ddlType, mapKeyWords, attributes, mapConditionalSections);

int i;
for (int i = 1; i <= numSubDirs; i++) {
catDdlsInDirRecursive(fileNoOutCrTs, fileNoOutCrObj, dirPath + "\\" + subDirs[i], ddlType, mapKeyWords, attributes, mapConditionalSections);
}
}


private static void catDmlsInDir(int fileNoOut, String dirPath, Integer ddlType, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}


String match;
String fileName;

match = dir(dirPath + "\\*DML", attributes);
do {catFile(dirPath + "\\" + match, fileNoOut, null, null);
match = dir;// next entry.
} while (!(match.compareTo("") == 0));
}


private static void stripCrInFile(String fileNameIn) {
String fileNameOut;
int fileNoIn;
int fileNoOut;

fileNameOut = fileNameIn + fnSuffixTmp;

//On Error GoTo ErrorExit 

fileNoIn = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForInput(fileNoIn, fileNameIn));

fileNoOut = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoOut, fileNameOut, false);

M00_FileWriter.printToFile(fileNoOut, M00_Helper.replace(M00_FileWriter.textOfFile(fileNoIn), vbCr, ""));

M00_FileWriter.closeFile(fileNoIn);
M00_FileWriter.closeFile(fileNoOut);

Files.delete(fileNameIn);
;

NormalExit:
//On Error Resume Next 
Files.delete(fileNameOut);
M00_FileWriter.closeFile(fileNoIn);
M00_FileWriter.closeFile(fileNoOut);
return;

ErrorExit:
if (M04_Utilities.baseName(fileNameIn, null, null, null, null).substring(0, 6).toUpperCase() != "EDIT.") {
errMsgBox(Err.description + "/" + Err.Number + "/" + fileNameIn);
}
Resume(NormalExit);
}


private static void stripCrInDir(String dirPath) {
String match;
String fileName;

match = dir(dirPath + "\\*");
do {fileName = dirPath + "\\" + match;
if ((fileName.substring(fileName.length() - 1 - fnSuffixTmp.length()) != fnSuffixTmp) &  (fileName.substring(fileName.length() - 1 - fnSuffixTmp.length()) != fnSuffixJar)) {
if ((GetAttr(fileName) &  vbDirectory) == 0) {
stripCrInFile(fileName);
}
}

match = dir;// next entry.
} while (!(match.compareTo("") == 0));
}


private static void stripCrInDirRecursive(String dirPath) {
String match;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;

match = dir(dirPath + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0) & match.toUpperCase() != ".SVN") {
if ((GetAttr(dirPath + "\\" + match) &  vbDirectory) == vbDirectory &  !(match.compareTo("obj") == 0)) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

stripCrInDir(dirPath);

int i;
for (int i = 1; i <= numSubDirs; i++) {
stripCrInDirRecursive(dirPath + "\\" + subDirs[i]);
}
}


private static void cpEtcFilesFromDir(String sourceDirPath, String targetDirPath, Integer ddlType, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

if (!(M03_Config.includeUtilityScrptsinPackage)) {
return;
}

String match;
//On Error GoTo ErrorExit 
String qualTabName;

match = dir(sourceDirPath + "\\*", attributes);
if (match.compareTo("") == 0) {
return;
}

M04_Utilities.assertDir(targetDirPath + "\\dummy");

String[] list;
do {FileCopy(sourceDirPath + "\\" + match, targetDirPath + "\\" + match);
stripCrInFile(targetDirPath + "\\" + match);
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void cpCsvsFromDir(int fileNoOutImportData, String sourceDirPath, String targetDirPath, Integer ddlType, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

String match;
//On Error GoTo ErrorExit 
String qualTabName;

match = dir(sourceDirPath + "\\*CSV", attributes);
if (match.compareTo("") == 0) {
return;
}

String[] list;
do {qualTabName = M04_Utilities.baseName(match, ".csv", null, null, null);
list = qualTabName.split("-");
if (M00_Helper.uBound(list) >= 2) {
FileCopy(sourceDirPath + "\\" + match, targetDirPath + "\\" + match);
stripCrInFile(targetDirPath + "\\" + match);
qualTabName = list[2];
M00_FileWriter.printToFile(fileNoOutImportData, "IMPORT FROM ../data/" + match + " OF DEL MODIFIED BY COLDEL, COMMITCOUNT 10000 INSERT INTO " + qualTabName + " " + M01_LDM.gc_sqlCmdDelim + vbLf);
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void cpCsvFromDirRecursive(int fileNoOutImportData, String sourceDirPath, String targetDirPath, Integer ddlType, Integer attributesW) {
int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}


String match;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;

match = dir(sourceDirPath + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0) & match.toUpperCase() != ".SVN") {
if ((GetAttr(sourceDirPath + "\\" + match) &  vbDirectory) == vbDirectory) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

M04_Utilities.assertDir(targetDirPath + "\\dummy");
cpCsvsFromDir(fileNoOutImportData, sourceDirPath, targetDirPath, ddlType, null);

int i;
for (int i = 1; i <= numSubDirs; i++) {
cpCsvFromDirRecursive(fileNoOutImportData, sourceDirPath + "\\" + subDirs[i], targetDirPath, ddlType, attributes);
}
}


private static void cpImplModulesFromDir(String sourceDirPath, String targetDirPath, String fileNameSuffixW, Integer attributesW, String exceptionW) {
String fileNameSuffix; 
if (fileNameSuffixW == null) {
fileNameSuffix = "";
} else {
fileNameSuffix = fileNameSuffixW;
}

int attributes; 
if (attributesW == null) {
attributes = vbNormal;
} else {
attributes = attributesW;
}

String exception; 
if (exceptionW == null) {
exception = "";
} else {
exception = exceptionW;
}

String match;
//On Error GoTo ErrorExit 

match = dir(sourceDirPath + "\\*" + fileNameSuffix, attributes);

do {if ((exception.length() > 0) &  (match.substring(0, exception.length()) == exception)) {
//On Error Resume Next 
Files.delete(targetDirPath + "\\" + match);
//On Error GoTo ErrorExit 
} else {
M04_Utilities.assertDir(targetDirPath + "\\" + match);
FileCopy(sourceDirPath + "\\" + match, targetDirPath + "\\" + match);
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    README
// ####################################################################################################################

private static void genReadMe(String targetDir) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + fileReadMe;
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, false);

M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("Please edit each script as follows:") + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- to provide correct connection information replace '<myDb>'") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  with the actual db name according to environment; ") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- in script '" + fileCreateDb + "' replace '<myDbDir>' with the path where '<myDb>' is supposed to reside") + vbLf);

if (!(M79_KwMap.kwTranslate("<tsRootDir>").compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- in scripts '" + fileCreateDb + "' and '" + fileCreateTs + "' replace '<tsRootDir>'") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  with the path where tablespace containers are supposed to reside;") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  (verify that the replacemant results in container paths which conform to the environment)") + vbLf);
}

if (!(M79_KwMap.kwTranslate("<dbInstance>").compareTo("") == 0)) {
if (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- in script '" + fileCreateExtSpsSh + "' replace '<dbInstance>'") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  with the name of the database instance;") + vbLf);
}
}

if (!(M79_KwMap.kwTranslate("<logPath>").compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- in script '" + fileUpdDbCfg + "' replace '<logPath>'") + vbLf);
}

if (!(M79_KwMap.kwTranslate("<jarPath>").compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("- in script '" + fileCreateExtSps + "' replace '<jarPath>'") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  with the absolute directory path where 'jar'-files reside which implement the") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  JAVA-Stored Procedures;") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("  (the relative path in the deployment package is '../install/jar')") + vbLf);
}

M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("Please execute scripts using \"db2 -td@ -f <scriptname>\"") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("in the following order (make sure to check output for successful execution):") + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileCreateDb) + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 connect to <myDb>") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileCreateTs) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileUpdDbCfg) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("./" + fileUpdDbProfile) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 terminate") + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("< take backup in order to take database out of 'backup pending'-state >") + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 connect to <myDb>") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileCreateExtSps) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileCreateObjects) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileImportData) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileInitMeta) + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileGrant) + vbLf);
if (M03_Config.bindJdbcPackagesWithReoptAlways) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 -td@ -f " + fileRebindCli) + vbLf);
}

M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2 terminate") + vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("Note: To simplify script adaptation the 'CONNECT'-command is no longer") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("      part of the individual SQL-scripts. Thus, 'CONNECT'- and 'TERMINATE'-commands") + vbLf);
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("      must be executed separately as indicated above.") + vbLf);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

// ####################################################################################################################
// #    create the directory structures
// ####################################################################################################################

private static void genScriptFilesystemSetup(String targetDir) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileFilesystemSetup;
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "used to create the directory structures", true, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

// ####################################################################################################################
// #    create DB
// ####################################################################################################################

private static void genScriptCreateDb(String targetDir) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileCreateDb;
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "Creates the MDS database", null, true);

M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("CREATE DB <dbName> ON <dbPath> USING CODESET UTF-8 TERRITORY DE") + vbLf);
if (!(M79_KwMap.kwTranslate("<tsRootDir>").compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M79_KwMap.kwTranslate("CATALOG   TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/system1/SYSCATSPACE')") + vbLf);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M79_KwMap.kwTranslate("USER      TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/data1/USERSPACE1')") + vbLf);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M79_KwMap.kwTranslate("TEMPORARY TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/temp1sms/TEMPSPACE1')") + vbLf);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim + vbLf);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    update DB cfg
// ####################################################################################################################



private static void genScriptUpdDbCfg(String targetDir) {
String fileName;
int fileNo;
Integer ddlType;

ddlType = M01_Common.DdlTypeId.edtPdm;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileUpdDbCfg;
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "Sets database manager and database parameters for MDS", null, null);

M00_FileWriter.printToFile(fileNo, "UPDATE COMMAND OPTIONS USING V ON" + M01_LDM.gc_sqlCmdDelim + vbLf);

int i;
for (int i = 1; i <= M78_DbCfg.g_dbCfgParams.numDescriptors; i++) {
if (M78_DbCfg.g_dbCfgParams.descriptors[i].isDbmCfgParam) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("UPDATE DBM CFG USING " + M78_DbCfg.g_dbCfgParams.descriptors[i].parameter + "                     ".substring(0, 20) + M78_DbCfg.g_dbCfgParams.descriptors[i].value) + " " + M01_LDM.gc_sqlCmdDelim + vbLf);
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "UPDATE COMMAND OPTIONS USING V OFF" + M01_LDM.gc_sqlCmdDelim + vbLf);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "");

// ####################################################################################################################
// #    (temporary) SP for configuring database parameter
// ####################################################################################################################

String qualProcedureNameSetDbCfg;
qualProcedureNameSetDbCfg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnSetDbCfg, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("(temporary) SP for configuring database parameter", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcedureNameSetDbCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "mode_in", "SMALLINT", true, "'0' - only list statements, '1' list and execute, '2' execute only");
M11_LRT.genProcParm(fileNo, "OUT", "osPlatform_out", "VARCHAR(5)", true, "OS-platform of the database server");
M11_LRT.genProcParm(fileNo, "OUT", "dbRelease_out", M01_Globals.g_dbtDbRelease, true, "DB-release of the database server");
M11_LRT.genProcParm(fileNo, "OUT", "parmCount_out", "INTEGER", true, "number of parameters sucessfully set");
M11_LRT.genProcParm(fileNo, "OUT", "failCount_out", "INTEGER", false, "number of parameter failed to set");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(80)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_flag", "CHAR(1)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_messageText", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_catchException", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);
M11_LRT.genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLWARNING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_flag = '?';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING, v_messageText = MESSAGE_TEXT;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF SQLCODE > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_flag = '?';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_catchException = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "RESIGNAL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_flag = '-';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET failCount_out = failCount_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

String tempTabNameStatementDbCfg;
tempTabNameStatementDbCfg = M94_DBAdmin.tempTabNameStatement + "DbCfg";

M94_DBAdmin.genDdlForTempStatement(fileNo, 1, true, 80, true, true, true, null, "DbCfg", null, null, true, null, "msg", "VARCHAR(100)", null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET parmCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET failCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine OS-Platform", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CASE SERVER_PLATFORM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  0 THEN 'UNK'   -- Unknown platform");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  1 THEN 'OS2'   -- OS/2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  2 THEN 'DOS'   -- DOS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  3 THEN 'WIN'   -- Windows");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  4 THEN 'AIX'   -- AIX");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  5 THEN 'NT'    -- NT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  6 THEN 'HP'    -- HP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  7 THEN 'SUN'   -- Sun");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  8 THEN 'MVS'   -- MVS (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN  9 THEN '400'   -- AS400 (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 10 THEN 'VM'    -- VM (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 11 THEN 'VSE'   -- VSE (client via DRDA)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 12 THEN 'UDRD'  -- Unknown DRDA Client");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 13 THEN 'SNI'   -- Siemens Nixdorf");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 14 THEN 'MacC'  -- Macintosh Client");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 15 THEN 'W95'   -- Windows 95");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 16 THEN 'SCO'   -- SCO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 17 THEN 'SIGR'  -- Silicon Graphic");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 18 THEN 'LINUX' -- Linux");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 19 THEN 'DYNIX' -- DYNIX/ptx");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 20 THEN 'AIX64' -- AIX 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 21 THEN 'SUN64' -- Sun 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 22 THEN 'HP64'  -- HP 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 23 THEN 'NT64'  -- NT 64 bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 24 THEN 'L390'  -- Linux for S/390");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 25 THEN 'L900'  -- Linux for z900");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 26 THEN 'LIA64' -- Linux for IA64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 27 THEN 'LPPC'  -- Linux for PPC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 28 THEN 'LPP64' -- Linux for PPC64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 29 THEN 'OS390' -- OS/390 Tools (CC, DW)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 30 THEN 'L8664' -- Linux for x86-64");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 31 THEN 'HPI32' -- HP-UX Itanium 32bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 32 THEN 'HPI64' -- HP-UX Itanium 64bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 33 THEN 'S8632' -- Sun x86 32bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHEN 34 THEN 'S8664' -- Sun x86-64 64bit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ELSE RTRIM(CAST(SERVER_PLATFORM AS CHAR(5)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "osPlatform_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE(SYSPROC.SNAPSHOT_DATABASE(CURRENT SERVER, -1)) X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET osPlatform_out = COALESCE(osPlatform_out, 'AIX64');");

M11_LRT.genProcSectionHeader(fileNo, "determine DB-Release", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST((v_int + ((v_dec - v_int) / 10)) AS " + M01_Globals.g_dbtDbRelease + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "dbRelease_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CAST(str AS " + M01_Globals.g_dbtDbRelease + ") AS v_dec,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INTEGER(CAST(str AS " + M01_Globals.g_dbtDbRelease + ")) AS v_int,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "str AS v_str");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SUBSTR(str, 1, POSSTR(str, '.') + POSSTR(RIGHT(str, LENGTH(str) - POSSTR(str, '.')), '.') -1) AS str");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "REPLACE(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "RIGHT(SERVICE_LEVEL, LENGTH(SERVICE_LEVEL) - POSSTR(SERVICE_LEVEL, ' ')),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'v', ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + ") AS str");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "TABLE(SYSPROC.ENV_GET_INST_INFO()) AS INSTANCEINFO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") V_VERS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ") V_VERS_T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") V_VERS_S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET dbRelease_out = COALESCE(dbRelease_out, 8);");

M11_LRT.genProcSectionHeader(fileNo, "loop over configuration parameters and find 'best match' based on DB2 Release", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR parmLoop AS parmCsr CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ParmList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "name,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "value,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNoDeploy,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "serverPlatform,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "minDbRelease");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

boolean firstParam;
firstParam = true;
for (int i = 1; i <= M78_DbCfg.g_dbCfgParams.numDescriptors; i++) {
if (!(M78_DbCfg.g_dbCfgParams.descriptors[i].isDbmCfgParam & ! M78_DbCfg.g_dbCfgParams.descriptors[i].isDbProfileParam)) {
if (!(firstParam)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UNION ALL");
}
firstParam = false;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES('" + M78_DbCfg.g_dbCfgParams.descriptors[i].parameter + "',                     ".substring(0, 20) + " '" + M78_DbCfg.g_dbCfgParams.descriptors[i].value.trim() + "',                     ".substring(0, 50) + " " + "SMALLINT(" + (M78_DbCfg.g_dbCfgParams.descriptors[i].sequenceNo <= 0 ? "9999" : String.valueOf(M78_DbCfg.g_dbCfgParams.descriptors[i].sequenceNo)) + "),    ".substring(0, 16) + " " + "CAST(" + (M78_DbCfg.g_dbCfgParams.descriptors[i].serverPlatform.compareTo("") == 0 ? "NULL" : "'" + M78_DbCfg.g_dbCfgParams.descriptors[i].serverPlatform + "'") + " AS VARCHAR(5)),           ".substring(0, 31) + " CAST(" + (M78_DbCfg.g_dbCfgParams.descriptors[i].minDbRelease.compareTo("") == 0 ? "NULL" : M00_Helper.replace(M78_DbCfg.g_dbCfgParams.descriptors[i].minDbRelease.toUpperCase(), ",", ".")) + " AS " + M01_Globals.g_dbtDbRelease + ")" + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ParmListOrdered");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "name,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "value,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNoDeploy,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNoReleaseMatch");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "name,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "value,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "seqNoDeploy,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ROWNUMBER() OVER (PARTITION BY name ORDER BY COALESCE(minDbRelease,0) DESC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "V_ParmList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(minDbRelease, dbRelease_out) <= dbRelease_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(serverPlatform, osPlatform_out) = osPlatform_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "name AS c_name,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "value AS c_value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_ParmListOrdered");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNoReleaseMatch = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "seqNoDeploy,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "name");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "assemble UPDATE statement", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'UPDATE DB CFG FOR ' || RTRIM(CURRENT SERVER) || ' USING ' || c_name || ' ' || c_value;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_flag = '+';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_messageText = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET parmCount_out = parmCount_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "execute config-update-statement", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in >= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_catchException = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_catchException = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "store statement in temporary table", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + tempTabNameStatementDbCfg + " (flag, statement, msg) VALUES (v_flag, v_stmntTxt, v_messageText);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET parmCount_out = parmCount_out - failCount_out;");

M11_LRT.genProcSectionHeader(fileNo, "return result to application", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF mode_in <= 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "flag AS F,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "statement,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "msg");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + tempTabNameStatementDbCfg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "seqno ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "leave cursor open for application", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OPEN stmntCursor;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF failCount_out = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcedureNameSetDbCfg + "(1, ?, ?, ?, ?)" + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, vbLf);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DROP PROCEDURE " + qualProcedureNameSetDbCfg + "(SMALLINT, VARCHAR(5), " + M01_Globals.g_dbtDbRelease + ", INTEGER, INTEGER)" + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, vbLf);

genScriptTrailer(fileNo, fileName, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);

stripCrInFile(fileName);

return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


private static void genScriptUpdDbProfile(String targetDir) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileUpdDbProfile;
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "Sets database profile parameters for MDS", true, null);

int i;
for (int i = 1; i <= M78_DbCfg.g_dbCfgParams.numDescriptors; i++) {
if (M78_DbCfg.g_dbCfgParams.descriptors[i].isDbProfileParam) {
M00_FileWriter.printToFile(fileNo, M79_KwMap.kwTranslate("db2set " + M78_DbCfg.g_dbCfgParams.descriptors[i].parameter.trim() + "=" + M78_DbCfg.g_dbCfgParams.descriptors[i].value.trim()) + vbLf);
}
}

genScriptTrailer(fileNo, fileName, true, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    create bufferpools, tablespaces and DB objects
// ####################################################################################################################

private static void genScriptCreateTsAndObjects(String sourceDir, String targetDir, Integer ddlType) {
String fileNameCrTs;
int fileNoCrTs;
String fileNameCrObj;
int fileNoCrObj;

//On Error GoTo ErrorExit 

fileNameCrTs = targetDir + "\\" + dirScripts + "\\" + fileCreateTs;
fileNameCrObj = targetDir + "\\" + dirScripts + "\\" + fileCreateObjects;

M04_Utilities.assertDir(fileNameCrTs);
M04_Utilities.assertDir(fileNameCrObj);

fileNoCrTs = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoCrTs, fileNameCrTs, false);
fileNoCrObj = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoCrObj, fileNameCrObj, false);

genScriptHeader(fileNoCrTs, fileNameCrTs, "Creates Bufferpools and Tablespaces for the MDS database", null, null);
genScriptHeader(fileNoCrObj, fileNameCrObj, "Creates all objects (tables, views, procedures etc) of the MDS database", null, null);

catDdlsInDirRecursive(fileNoCrTs, fileNoCrObj, sourceDir, ddlType, null, null, null);

genScriptTrailer(fileNoCrTs, fileNameCrTs, null, null);
genScriptTrailer(fileNoCrObj, fileNameCrObj, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoCrTs);
M00_FileWriter.closeFile(fileNoCrObj);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    create DB objects for template DDL
// ####################################################################################################################

private static void genScriptCreateObjectsTemplate(String sourceDir, String targetDir, Integer ddlType) {
String fileNameCrObj;
int fileNoCrObj;

//On Error GoTo ErrorExit 

String match;
String[] subDirs = new String[maxSubDirs];
int numSubDirs;
numSubDirs = 0;

match = dir(sourceDir + "\\*", vbDirectory);
do {if (numSubDirs < maxSubDirs &  !(match.compareTo(".") == 0) & !(match.compareTo("..") == 0) & match.toUpperCase() != ".SVN" & match.toUpperCase() != "DROP") {
if ((GetAttr(sourceDir + "\\" + match) &  vbDirectory) == vbDirectory) {
numSubDirs = numSubDirs + 1;
subDirs[(numSubDirs)] = match;
}
}
match = dir;// next entry.
} while (!(match.compareTo("") == 0));

int i;
for (int i = 1; i <= numSubDirs; i++) {
fileNameCrObj = targetDir + "\\" + dirTemplate + "\\" + subDirs[i] + "\\" + fileCreateObjectsTemplate;
M04_Utilities.assertDir(fileNameCrObj);

fileNoCrObj = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoCrObj, fileNameCrObj, false);

genScriptHeader(fileNoCrObj, fileNameCrObj, "Creates all objects (tables, views, procedures etc) of the MDS database", null, null);

catDdlsInDirRecursive(-1, fileNoCrObj, sourceDir + "\\" + subDirs[i], ddlType, null, null, null);

genScriptTrailer(fileNoCrObj, fileNameCrObj, false, true);

M00_FileWriter.closeFile(fileNoCrObj);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoCrObj);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    create external Stored Procedures
// ####################################################################################################################

private static void genScriptCreateExternalProcedures(String sourceDir, String sourceJavaImplDir, String sourceNativeImplDir, String targetDir, String targetJavaImplDir, String targetNativeImplDir, Integer ddlType) {
String fileName;
int fileNo;
int fileNoSh;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileCreateExtSps;

M04_Utilities.assertDir(fileName);

fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "Creates external Stored Procedures for the MDS database", null, null);

catDdlsInDirRecursive(-1, fileNo, sourceDir, ddlType, true, null, true);

genScriptTrailer(fileNo, fileName, null, null);

cpImplModulesFromDir(sourceJavaImplDir, targetJavaImplDir, fnSuffixJar, null, null);

cpImplModulesFromDir(sourceNativeImplDir, targetNativeImplDir, null, null, (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? "" : "splogger"));

if (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile) {
fileName = targetDir + "\\" + dirScripts + "\\" + fileCreateExtSpsSh;

M04_Utilities.assertDir(fileName);

fileNoSh = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoSh, fileName, false);

genScriptHeader(fileNoSh, fileName, "Deploy Implementation Modules for external Stored Procedures for the MDS database", true, null);

M00_FileWriter.printToFile(fileNoSh, M79_KwMap.kwTranslate("spRootDir=`echo \"echo <dbInstance>/sqllib/function\" | /bin/ksh`") + vbLf);
M00_FileWriter.printToFile(fileNoSh, M79_KwMap.kwTranslate("spSubDir=\"<spPathPrefix>/\"") + vbLf);

M00_FileWriter.printToFile(fileNoSh, "dirMode=a+r+x " + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "spSubDir=`echo $spSubDir | sed 's#/[\\/]*#/#g'`" + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "[ \"$spSubDir\" = '/' ] && spSubDir=''" + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "spDir=\"$spRootDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "if [ \"$spSubDir\" ]; then" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  spDir=\"$spRootDir/$spSubDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "  echo \"Creating directory $spDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  mkdir -p \"$spDir\" " + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "  # fenced user needs to have access to deployment directory" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  thisDir=\"$spSubDir\" " + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  while [ \"$thisDir\" != '' ] ; do " + vbLf);
M00_FileWriter.printToFile(fileNoSh, "    echo \"Setting permissions '$dirMode' on directory $spRootDir/$thisDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "    chmod $dirMode \"$spRootDir/$thisDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "    thisDir=\"`dirname \"$thisDir\"`\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "    [ \"$thisDir\" = '.' -o \"$thisDir\" = '/' ] && thisDir=''" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  done" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "fi" + vbLf);
M00_FileWriter.printToFile(fileNoSh, vbLf);
M00_FileWriter.printToFile(fileNoSh, "echo \"Copying Stored Procedure Modules to directory $spDir\" " + vbLf);
M00_FileWriter.printToFile(fileNoSh, "for module in ../install/obj/*; do" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  rm -f \"$spDir\"/`basename $module`" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  cp $module \"$spDir\"" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "  chmod 644 \"$spDir\"/`basename $module`" + vbLf);
M00_FileWriter.printToFile(fileNoSh, "done" + vbLf);

genScriptTrailer(fileNoSh, fileName, null, null);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
M00_FileWriter.closeFile(fileNoSh);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    import data
// ####################################################################################################################

private static void genScriptImportData(String sourceDir, String targetDir, Integer ddlType) {
String fileNameImpData;
int fileNoImpData;

//On Error GoTo ErrorExit 

fileNameImpData = targetDir + "\\" + dirScripts + "\\" + fileImportData;

M04_Utilities.assertDir(fileNameImpData);

fileNoImpData = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoImpData, fileNameImpData, false);

genScriptHeader(fileNoImpData, fileNameImpData, "Imports base data into the MDS database", null, null);

cpCsvFromDirRecursive(fileNoImpData, sourceDir, targetDir + "\\" + dirData, ddlType, null);

genScriptTrailer(fileNoImpData, fileNameImpData, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoImpData);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    initialize meta data
// ####################################################################################################################

private static void genScriptMetaData(String sourceDir, String targetDir, Integer ddlType) {
String fileNameInMet;
int fileNoInMet;

//On Error GoTo ErrorExit 

fileNameInMet = targetDir + "\\" + dirScripts + "\\" + fileInitMeta;

M04_Utilities.assertDir(fileNameInMet);

fileNoInMet = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoInMet, fileNameInMet, false);

genScriptHeader(fileNoInMet, fileNameInMet, "initializes meta data in the MDS database", null, null);

catDmlsInDir(fileNoInMet, sourceDir, ddlType, null);

genScriptTrailer(fileNoInMet, fileNameInMet, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoInMet);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    Bind CLI
// ####################################################################################################################

private static void genScriptBindCli(String sourceDir, String targetDir, Integer ddlType) {
String fileNameBindCli;
int fileNoBindCli;

//On Error GoTo ErrorExit 

if (!(M03_Config.bindJdbcPackagesWithReoptAlways)) {
return;
}

fileNameBindCli = targetDir + "\\" + dirScripts + "\\" + fileRebindCli;

M04_Utilities.assertDir(fileNameBindCli);

fileNoBindCli = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNoBindCli, fileNameBindCli, false);

String schemaNamePackageReopt;
schemaNamePackageReopt = M04_Utilities.genSchemaName(M01_ACM.snPackageReopt, M01_ACM.ssnPackageReopt, M01_Common.DdlTypeId.edtPdm, null, null);

genScriptHeader(fileNoBindCli, fileNameBindCli, "bind CLI in dedicated schema '" + schemaNamePackageReopt + "'", null, null);

M00_FileWriter.printToFile(fileNoBindCli, M79_KwMap.kwTranslate("BIND \"<dbBindDir>@db2cli.lst\" BLOCKING ALL CLIPKG 3 COLLECTION " + schemaNamePackageReopt + " GRANT PUBLIC REOPT ALWAYS") + vbLf);
M00_FileWriter.printToFile(fileNoBindCli, M01_LDM.gc_sqlCmdDelim + vbLf);

genScriptTrailer(fileNoBindCli, fileNameBindCli, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNoBindCli);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    grant permssions
// ####################################################################################################################

private static void genScriptGrant(String targetDir, Integer ddlType) {
String fileName;
int fileNo;

//On Error GoTo ErrorExit 

fileName = targetDir + "\\" + dirScripts + "\\" + fileGrant;

M04_Utilities.assertDir(fileName);

fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, false);

genScriptHeader(fileNo, fileName, "grants privileges for the MDS database and objects in the database", null, null);

if (M04_Utilities.strArrayIsNull(M03_Config.environmentIds)) {
M00_FileWriter.printToFile(fileNo, "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnGrant, ddlType, null, null, null, null, null, null) + "(1,?)" + M01_LDM.gc_sqlCmdDelim + vbLf);
} else {
int i;
for (int i = M00_Helper.lBound(M03_Config.environmentIds); i <= M00_Helper.uBound(M03_Config.environmentIds); i++) {
M00_FileWriter.printToFile(fileNo, "CALL " + M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnGrant, ddlType, null, null, null, "ByEnv", M04_Utilities.ObjNameDelimMode.eondmNone, null) + "('" + M03_Config.environmentIds[i] + "',1,?)" + M01_LDM.gc_sqlCmdDelim + vbLf);
}
}

genScriptTrailer(fileNo, fileName, null, null);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


// ####################################################################################################################
// #    generate package
// ####################################################################################################################

public static void genPackageByDdlType(Integer ddlType, boolean forLrt, Integer packageTypeW) {
Integer packageType; 
if (packageTypeW == null) {
packageType = int;
} else {
packageType = packageTypeW;
}

String sourceDir;

sourceDir = M01_Globals.g_targetDir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + (ddlType == M01_Common.DdlTypeId.edtLdm ? "\\LDM" : "\\PDM") + (forLrt ? "-LRT" : "") + "\\";

String targetDir;
targetDir = M01_Globals.g_targetDir + "\\deploy" + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + (ddlType == M01_Common.DdlTypeId.edtLdm ? "\\LDM" : "\\PDM") + (forLrt ? "-LRT" : "") + "\\";

M04_Utilities.logMsg("packaging deployment package to \"" + targetDir + "\"", M01_Common.LogLevel.ellInfo, ddlType, null, null);

//On Error Resume Next 
Files.delete(targetDir + "\\" + dirScripts + "\\" + "*.sql");
Files.delete(targetDir + "\\" + dirData + "\\" + "*.csv");

//On Error GoTo ErrorExit 

genReadMe(targetDir);

genScriptFilesystemSetup(targetDir);
genScriptCreateDb(targetDir);
genScriptUpdDbCfg(targetDir);
genScriptUpdDbProfile(targetDir);
genScriptCreateTsAndObjects(sourceDir, targetDir, ddlType);
genScriptCreateObjectsTemplate(sourceDir + "\\template", targetDir, ddlType);
genScriptCreateExternalProcedures(sourceDir + "\\Deploy", sourceDir + "\\Deploy\\jar", sourceDir + "\\Deploy\\obj\\" + M03_Config.targetPlatform, targetDir, targetDir + "\\" + dirInstall + "\\jar", targetDir + "\\" + dirInstall + "\\obj", ddlType);

genScriptGrant(targetDir, ddlType);
genScriptImportData(sourceDir + "\\CSV", targetDir, ddlType);
genScriptMetaData(sourceDir + "\\Deploy", targetDir, ddlType);
genScriptBindCli(sourceDir + "\\Deploy", targetDir, ddlType);
cpEtcFilesFromDir(sourceDir + "\\Deploy\\" + dirEtc, targetDir + "\\" + dirEtc, ddlType, null);

NormalExit:
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


}