package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M17_FwkTest {


// ### IF IVK ###


private static final int processingStep = 2;


public static void genFwkTestDdlForOrg( Integer thisOrgIndexW, Integer ddlTypeW) {
int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

int thisOrgId;
if (thisOrgIndex > 0) {
thisOrgId = M71_Org.g_orgs.descriptors[thisOrgIndex].id;
} else {
thisOrgId = -1;
}


if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

if (!(M20_Section.g_sections.descriptors[M01_Globals.g_sectionIndexFwkTest].specificToOrgs.compareTo("") == 0) & ! M04_Utilities.includedInList(M20_Section.g_sections.descriptors[M01_Globals.g_sectionIndexFwkTest].specificToOrgs, thisOrgId)) {
return;
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexFwkTest, processingStep, ddlType, thisOrgIndex, null, null, null, null);

M17_FwkTest.genFwkTestOidSequenceForOrg(thisOrgIndex, fileNo, ddlType);

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genFwkTestDdlForPool( Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

//On Error GoTo ErrorExit 

if (!(M20_Section.g_sections.descriptors[M01_Globals.g_sectionIndexFwkTest].specificToOrgs.compareTo("") == 0)) {
if (thisOrgIndex < 1) {
return;
} else if (!(M20_Section.g_sections.descriptors[M01_Globals.g_sectionIndexFwkTest].specificToOrgs.compareTo("") == 0) & ! M04_Utilities.includedInList(M20_Section.g_sections.descriptors[M01_Globals.g_sectionIndexFwkTest].specificToOrgs, M71_Org.g_orgs.descriptors[thisOrgIndex].id)) {
return;
}
}

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexFwkTest, processingStep, ddlType, thisOrgIndex, thisPoolIndex, null, null, null);

String qualProcName;
qualProcName = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, "Exc_Test", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Testing Business Exceptions", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 17;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SIGNAL SQLSTATE '79133' SET MESSAGE_TEXT = '[MDS]: 1300005;" + M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericCode, ddlType, 2, 1, null, null, null, null, null, null, null) + "';");

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


public static String genFwkTestOidSequenceNameForOrg( int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(M01_Globals.g_sectionIndexFwkTest, M01_LDM.gc_seqNameOid, M01_LDM.gc_seqNameOid, ddlType, thisOrgIndex, null, null, null, null, null, null, null, null, null);
return returnValue;
}


public static void genFwkTestOidSequenceForOrg( int thisOrgIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

M71_Org.genSequence("Sequence for Generating Object IDs for FwkTest / MPC \"" + M71_Org.g_orgs.descriptors[thisOrgIndex].name + "\"", M17_FwkTest.genFwkTestOidSequenceNameForOrg(M71_Org.g_orgs.descriptors[thisOrgIndex].id, ddlType), 0, fileNo, "00000000000000000", null, null, null, null, null, null);
}

public static void genFwkTestDdl(Integer ddlType) {
int thisOrgIndex;
int thisPoolIndex;

if (!(M03_Config.generateFwkTest)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
M17_FwkTest.genFwkTestDdlForOrg(thisOrgIndex, M01_Common.DdlTypeId.edtPdm);
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex)) {
M17_FwkTest.genFwkTestDdlForPool(thisOrgIndex, thisPoolIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}
}
// ### ENDIF IVK ###


}