package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M05_Main {




private static void copyMigFilesInDir(String sourceDirPath, String targetDirPath, String skipFileSuffix1W, String skipFileSuffix2W, Boolean alsoForFwkTestW) {
String skipFileSuffix1; 
if (skipFileSuffix1W == null) {
skipFileSuffix1 = "";
} else {
skipFileSuffix1 = skipFileSuffix1W;
}

String skipFileSuffix2; 
if (skipFileSuffix2W == null) {
skipFileSuffix2 = "";
} else {
skipFileSuffix2 = skipFileSuffix2W;
}

boolean alsoForFwkTest; 
if (alsoForFwkTestW == null) {
alsoForFwkTest = true;
} else {
alsoForFwkTest = alsoForFwkTestW;
}

if (!(alsoForFwkTest &  M03_Config.generateFwkTest)) {
return;
}

String match;

//On Error Resume Next 
Err.Number = 0;
match = dir(sourceDirPath + "\\*", vbNormal);
if (Err.Number != 0 &  Err.Number != 52) {
goto ErrorExit;
}
if (match.compareTo("") == 0) {
return;
}

//On Error GoTo ErrorExit 
M04_Utilities.assertDir(targetDirPath + "\\X");

do {if (((skipFileSuffix1 == "") |  ((skipFileSuffix1 != "") &  (match.substring(match.length() - 1 - skipFileSuffix1.length()).toUpperCase() != skipFileSuffix1.toUpperCase()))) &  ((skipFileSuffix2 == "") |  ((skipFileSuffix2 != "") &  (match.substring(match.length() - 1 - skipFileSuffix2.length()).toUpperCase() != skipFileSuffix2.toUpperCase())))) {
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


private static void copyMigFiles() {
if ((M03_Config.workSheetSuffix.compareTo("") == 0) |  (!(M03_Config.generatePdm))) {
return;
}

String srcDir;
String srcRootDir;
String dstDir;
String dstRootDir;

srcRootDir = M01_Globals.g_targetDir + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");
dstRootDir = M01_Globals.g_targetDir + "\\" + M03_Config.workSheetSuffix + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");

copyMigFilesInDir(srcRootDir + "\\Deploy", dstRootDir + "\\Deploy", ".dml", (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? "" : "SPLOGGER.DDL"), true);
copyMigFilesInDir(srcRootDir + "\\Deploy\\jar", dstRootDir + "\\Deploy\\jar", null, null, true);

copyMigFilesInDir(srcRootDir + "\\Deploy\\obj\\AIX", dstRootDir + "\\Deploy\\obj\\AIX", (M03_Config.supportSpLogging &  M03_Config.targetPlatform.compareTo("AIX") == 0 & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? "" : "splogger"), null, true);
copyMigFilesInDir(srcRootDir + "\\Deploy\\obj\\Windows", dstRootDir + "\\Deploy\\obj\\Windows", (M03_Config.supportSpLogging &  !(M03_Config.targetPlatform.compareTo("AIX") == 0) & M03_Config.spLogMode == M01_Common.DbSpLogMode.esplFile ? "" : "splogger"), null, true);

int thisOrgIndex;
for (int thisOrgIndex = 1; thisOrgIndex <= M71_Org.g_orgs.numDescriptors; thisOrgIndex++) {
if (!(M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate)) {

int j;
for (int j = 1; j <= 2; j++) {
String infix;
infix = (j == 1 ? "" : "\\drop");
srcDir = srcRootDir + "\\" + M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgId(thisOrgIndex, M01_Common.DdlTypeId.edtPdm, null) + "-" + M71_Org.g_orgs.descriptors[thisOrgIndex].name + "\\Migration" + infix;
dstDir = dstRootDir + "\\" + M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgId(thisOrgIndex, M01_Common.DdlTypeId.edtPdm, null) + "-" + M71_Org.g_orgs.descriptors[thisOrgIndex].name + "\\Migration" + infix;
copyMigFilesInDir(srcDir, dstDir, null, null, false);
}
}
}
}


private static void copyVdokfFiles() {
if ((M03_Config.workSheetSuffix.compareTo("") == 0) |  (!(M03_Config.generatePdm)) | M03_Config.generateFwkTest) {
return;
}

String srcDir;
String srcRootDir;
String dstDir;
String dstRootDir;
int thisOrgId;
int thisPoolId;
String orgName;
String poolName;

srcRootDir = M01_Globals.g_targetDir + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");
dstRootDir = M01_Globals.g_targetDir + "\\" + M03_Config.workSheetSuffix + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");

int thisOrgIndex;
for (int thisOrgIndex = 1; thisOrgIndex <= M71_Org.g_orgs.numDescriptors; thisOrgIndex++) {
if (!(M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate)) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
orgName = M00_Helper.replace(M71_Org.getOrgNameById(thisOrgId), " ", "_", null, null, vbTextCompare);

int thisPoolIndex;
for (int thisPoolIndex = 1; thisPoolIndex <= M72_DataPool.g_pools.numDescriptors; thisPoolIndex++) {
thisPoolId = M72_DataPool.g_pools.descriptors[thisPoolIndex].id;

if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
poolName = M00_Helper.replace(M72_DataPool.getDataPoolNameByIndex(thisPoolIndex), " ", "_", null, null, vbTextCompare);

int k;
for (int k = 1; k <= 2; k++) {
String infix;
infix = (k == 1 ? "" : "\\drop");
srcDir = srcRootDir + "\\" + M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgId(thisOrgIndex, M01_Common.DdlTypeId.edtPdm, null) + "-" + orgName + "\\DPool-" + M04_Utilities.genPoolId(thisPoolIndex, M01_Common.DdlTypeId.edtPdm) + "-" + poolName + "\\VDOKF" + infix;
dstDir = dstRootDir + "\\" + M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgId(thisOrgIndex, M01_Common.DdlTypeId.edtPdm, null) + "-" + orgName + "\\DPool-" + M04_Utilities.genPoolId(thisPoolIndex, M01_Common.DdlTypeId.edtPdm) + "-" + poolName + "\\VDOKF" + infix;
copyMigFilesInDir(srcDir, dstDir, null, null, false);
}
}
}
}
}
}


private static void copyExplainFiles() {
if ((M03_Config.workSheetSuffix.compareTo("") == 0) |  (!(M03_Config.generatePdm)) | M03_Config.generateFwkTest) {
return;
}

String srcDir;
String srcRootDir;
String dstDir;
String dstRootDir;

srcRootDir = M01_Globals.g_targetDir + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");
dstRootDir = M01_Globals.g_targetDir + "\\" + M03_Config.workSheetSuffix + "\\PDM" + (M01_Globals.g_genLrtSupport ? "-LRT" : "");

int k;
for (int k = 1; k <= 2; k++) {
String infix;
infix = (k == 1 ? "" : "\\drop");
srcDir = srcRootDir + "\\Explain" + infix;
dstDir = dstRootDir + "\\Explain" + infix;
copyMigFilesInDir(srcDir, dstDir, null, null, false);
}
}


private static void loadSheets() {
M71_Org.getOrgs();
M72_DataPool.getDataPools();
M75_BufferPool.getBufferPools();
M79_KwMap.getKwMaps();
M79_Err.getErrs();
M78_DbCfg.getDbCfgParams();
M78_TabCfg.getTabCfgParams();
M78_DbProfile.getDbCfgProfiles();
M74_Container.getContainers();
M73_TableSpace.getTableSpaces();
M24_Attribute.getAttributes();
M24_Attribute_NL.getAttributesNl();
M21_Enum.getEnums();
M21_Enum_NL.getEnumsNl();
// ### IF IVK ###
M26_Type.getTypes();
// ### ENDIF IVK ###
M25_Domain.getDomains();
M22_Class.getClasses();
M22_Class_NL.getClassesNl();
M23_Relationship.getRelationships();
M23_Relationship_NL.getRelationshipsNl();
M76_Index.getIndexes();
M77_IndexAttr.getIndexAttrs();
M99_IndexException.getIndexExcp();
M20_Section.getSections();
M79_Privileges.getPrivileges();
M79_SnapshotType.getSnapshotTypes();
M79_SnapshotCol.getSnapshotCols();
M79_SnapshotFilter.getSnapshotFilter();
M79_CleanJobs.getCleanJobs();
// ### IF IVK ###
M79_DataCompare.getDComps();
// ### ENDIF IVK ###

M01_Globals.initGlobals();

M04_Utilities.evalObjects();
}


public static void dropSheet(String sheetName) {
Sheet sheet;
//On Error Resume Next 

sheet = M00_Excel.activeWorkbook.;
//On Error GoTo ErrorExit 
M00_Excel.activeWorkbook.getSheet(sheetName).;
M00_Excel.deleteSheet(.);
sheet.;

ErrorExit:
}


public static void doRunTest() {

M05_Main.doRun(M03_Config.ConfigMode.ecfgTest);
}


public static void doRunProductiveEw() {

M05_Main.doRun(M03_Config.ConfigMode.ecfgProductionEw);
}


public static void doRunForDelivery() {

M05_Main.doRun(M03_Config.ConfigMode.ecfgDelivery);
}


public static void doRun(Integer cfgModeW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

if (M03_Config.irregularSetting(cfgMode)) {
if (System.out.println(MsgBox: ( "Generator Config includes irregular setting. Do you want to continue?" , vbYesNo Or vbCritical ) );
 != vbYes) {
return;
}
}

M04_Utilities.killFile(M04_Utilities.genLogFileName(), null);
M01_Globals.setLogLevesl(!(new Double(0).intValue()), null);

M04_Utilities.closeAllDdlFiles(null, null, null, null, null, null);
M04_Utilities.closeAllCsvFiles(null);

M03_Config.readConfig(cfgMode, null);

Date tsBegin;
tsBegin = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());

M04_Utilities.logMsg("Start DDL Generator", M01_Common.LogLevel.ellInfo, null, null, null);

M01_Globals.setLogLevesl(null, null);

M04_Utilities.resetOid();
loadSheets();

//genPackages
//Exit Sub
int loopStart;
int loopStop;
loopStart = (M03_Config.generateNonLrt ? 1 : 2);
loopStop = (M03_Config.generateLrt ? 2 : 1);

int i;
for (i = loopStart; i <= 1; i += (1)) {
M01_Globals.g_genLrtSupport = (i == 2);
M01_Globals.setEnv(M01_Globals.g_genLrtSupport);

// ### IF IVK ###
M22_Class.dropClassesHibernateSupport(M01_Common.DdlTypeId.edtLdm);
// ### ENDIF IVK ###
M22_Class.dropClassIdList(null);
// ### IF IVK ###
M22_Class.dropClassesXmlExport();
// ### ENDIF IVK ###
M05_Main.dropSheet(M01_Globals.g_sheetNameDdlSummary);

M04_Utilities.dropDdl(null);
M04_Utilities.dropCsv(null);
M93_DBDeploy.dropCsvInventoryLists();
}

M78_DbMeta.initGLdmFks();
M72_DataPool.cleanupPools();

// ### IF IVK ###
if (M03_Config.genSupportForHibernate) {
for (i = loopStart; i <= 1; i += (1)) {
M01_Globals.g_genLrtSupport = (i == 2);
M01_Globals.setEnv(M01_Globals.g_genLrtSupport);
M22_Class.genClassesHibernateSupport(M01_Common.DdlTypeId.edtLdm);
}
}

// ### ENDIF IVK ###
if (M03_Config.generateEntityIdList) {
M22_Class.genClassIdList();
M23_Relationship.genRelIdList();
}

for (i = loopStart; i <= 1; i += (1)) {
M01_Globals.g_genLrtSupport = (i == 2);
M01_Globals.setEnv(M01_Globals.g_genLrtSupport);

// ### IF IVK ###
copyMigFiles();
copyVdokfFiles();
copyExplainFiles();

//    profLogOpen

// ### ENDIF IVK ###

if (M03_Config.generateLdm) {
M04_Utilities.verifyWorksheet(M01_Globals.g_sheetNameDdlSummary + "-tmp", null);
if (M04_Utilities.setSheetName(M00_Excel.activeWorkbook, M00_Excel.activeWorkbook., M01_Globals.g_sheetNameDdlSummary, false, null) == vbNo) {
return;
}
}

Integer ddlType;
for (int ddlType = (M03_Config.generatePdm ? M01_Common.DdlTypeId.edtPdm : M01_Common.DdlTypeId.edtLdm); ddlType <= (M03_Config.generateLdm ? M01_Common.DdlTypeId.edtLdm : M01_Common.DdlTypeId.edtPdm); ddlType++) {
M01_Globals.initGlobalsByDdl(ddlType);

M04_Utilities.genAcmMetaCsv(ddlType);
M04_Utilities.genLdmMetaCsv(ddlType);
M04_Utilities.genPdmMetaCsv(ddlType);
M79_Privileges.genPrivilegesCsv(ddlType);
M79_CleanJobs.genCleanJobsCsv(ddlType);
M78_DbProfile.genDbCfgProfileCsv(ddlType);
M78_TabCfg.genTabCfgCsv(ddlType);
M79_SnapshotType.genSnapshotTypesCsv(ddlType);
M79_SnapshotCol.genSnapshotColsCsv(ddlType);
M79_SnapshotFilter.genSnapshotFilterCsv(ddlType);

// ### IF IVK ###
M79_DataCompare.genDCompCsv(ddlType);
// ### ENDIF IVK ###

M94_DBAdmin.genDbAdminDdl(ddlType);
// ### IF IVK ###
M94_DBAdmin_Partitioning.genDbAdminPartitioningDdl(ddlType);
// ### ENDIF IVK ###
M94_SnapShot.genDbSnapshotDdl(ddlType);
M94_IndexMetrics.genDbIndexMetricsDdl(ddlType);
M07_SpLogging.genSpLogWrapperDdl(ddlType);
M91_DBMeta.genDbMetaDdl(ddlType);
M92_DBUtilities.genDbUtilitiesDdl(ddlType);
// ### IF IVK ###
M98_Trace.genTraceDdl(ddlType);
M97_DataCheck.genDataCheckUtilitiesDdl(ddlType);
M22_Class.genClassesHibernateSupport(ddlType);
// ### ENDIF IVK ###
M11_LRT.genLrtSupportDdl(ddlType);
M11_LRT_MQT.genLrtMqtSupportDdl(ddlType);
// ### IF IVK ###
M11_VirtualAttrs.genVirtAttrSupportDdl(ddlType);
M11_GroupIdAttrs.genGroupIdSupportDdl(ddlType);
M86_SetProductive.genSetProdSupportDdl(ddlType);
M16_Archive.genArchiveSupportDdl(ddlType);
M82_PSCopy.genPsCopySupportDdl(ddlType);
M85_DataFix.genDataFixSupportDdl(ddlType);
M27_Meta.genAcmMetaSupportDdl(ddlType);
M81_PSCreate.genPsCreateSupportDdl(ddlType);
M83_PSdelete.genPsDeleteSupportDdl(ddlType);
M87_FactoryTakeOver.genFactoryTakeOverDdl(ddlType);
M88_CodesWithoutDep.genCodesWithoutDepDdl(ddlType);
M89_TechData.genTechDataSupDdl(ddlType);
M89_RunningServiceServer.genRssSupDdl(ddlType);
M17_FwkTest.genFwkTestDdl(ddlType);
// ### ENDIF IVK ###

M71_Org.genOrgsDdl(ddlType);
M72_DataPool.genDataPoolsDdl(ddlType);
M75_BufferPool.genBufferPoolsDdl(ddlType);
M73_TableSpace.genTableSpacesDdl(ddlType);

// ### IF IVK ###
M79_DataCompare.genDCompSupportDdl(ddlType);
// ### ENDIF IVK ###

M21_Enum.genEnumsDdl(ddlType);
M22_Class.genClassesDdl(ddlType);
M23_Relationship.genRelationshipsDdl(ddlType);

M93_DBDeploy.genDbDeployPostprocess(ddlType);

M04_Utilities.closeAllDdlFiles(null, null, null, null, null, ddlType);
M04_Utilities.closeAllCsvFiles(ddlType);
}

M78_DbMeta.genLdmFksCsvs();
M93_DBDeploy.genCsvInventoryLists();

M04_Utilities.dropDdl(true);

M96_DdlSummary.resetDdl();

M78_DbMeta.initGLdmFks();
}

if (M03_Config.generateDeployPackage) {
M06_Packaging.genPackages();
} else if (M03_Config.generateUpdatePackage) {
M06_Packaging.genPackages();
}


if (M03_Config.exportVBCode) {
exportCode;
}

if (M03_Config.exportXlsSheets) {
M04_Utilities.exportSheets();
}

reset();

Date tsEnd;
tsEnd = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());

long runTimeSec;
runTimeSec = DateDiff("s", tsBegin, tsEnd);
M01_Globals.setLogLevesl(!(new Double(0).intValue()), null);
M04_Utilities.logMsg("End DDL Generator (" + new Double(runTimeSec / 60).longValue() + ":" + new String ("0" + (runTimeSec % 60)).substring(new String ("0" + (runTimeSec % 60)).length() - 1 - 2) + ")", M01_Common.LogLevel.ellInfo, null, null, null);
// ### IF IVK ###
M08_VBA_Profiling.profLogClose();
// ### ENDIF IVK ###
}


public static void doPack(Integer cfgModeW) {
Integer cfgMode; 
if (cfgModeW == null) {
cfgMode = M03_Config.ConfigMode.ecfgTest;
} else {
cfgMode = cfgModeW;
}

if (M03_Config.irregularSetting(cfgMode)) {
if (System.out.println(MsgBox: ( "Generator Config includes irregular setting. Do you want to continue?" , vbYesNo Or vbCritical ) );
 != vbYes) {
return;
}
}

M03_Config.readConfig(cfgMode, null);

M04_Utilities.resetOid();
loadSheets();

if (M03_Config.generateDeployPackage) {
M06_Packaging.genPackages();
} else if (M03_Config.generateUpdatePackage) {
M06_Packaging.genPackages();
}

reset();
}


public static void doPackTest() {

M05_Main.doPack(M03_Config.ConfigMode.ecfgTest);
}


public static void doPackProductive() {

M05_Main.doPack(M03_Config.ConfigMode.ecfgProductionEw);
}

private static void reset() {
M72_DataPool.resetDataPools();
M71_Org.resetOrgs();
M75_BufferPool.resetBufferPools();
M74_Container.resetContainers();
M79_KwMap.resetKwMaps();
M79_Err.resetErrs();
M78_DbCfg.resetDbCfgParams();
M78_TabCfg.resetTabCfgParams();
M78_DbProfile.resetDbCfgProfiles();
M73_TableSpace.resetTableSpaces();
M20_Section.resetSections();
M25_Domain.resetDomains();
M24_Attribute.resetAttributes();
M24_Attribute_NL.resetAttributesNl();
M22_Class.resetClasses();
M22_Class_NL.resetClassesNl();
M76_Index.resetIndexes();
M77_IndexAttr.resetIndexAttrs();
M25_Domain.resetDomains();
M21_Enum.resetEnums();
M21_Enum_NL.resetEnumsNl();
// ### IF IVK ###
M26_Type.resetTypes();
// ### ENDIF IVK ###
M23_Relationship.resetRelationships();
M23_Relationship_NL.resetRelationshipsNl();
M79_Privileges.resetPrivileges();
M79_CleanJobs.resetCleanJobs();
M79_SnapshotType.resetSnapshotTypes();
M79_SnapshotCol.resetSnapshotCols();
M79_SnapshotFilter.resetSnapshotFilter();
// ### IF IVK ###
M79_DataCompare.resetDComps();
// ### ENDIF IVK ###
}

}