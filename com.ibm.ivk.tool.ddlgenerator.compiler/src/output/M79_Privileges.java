package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_Privileges {




private static final int colSequenceNumber = 2;
private static final int colEnvironment = colSequenceNumber + 1;
private static final int colOperation = colEnvironment + 1;
private static final int colObjectType = colOperation + 1;
private static final int colSchemaName = colObjectType + 1;
private static final int colObjectName = colSchemaName + 1;
private static final int colFilter = colObjectName + 1;
private static final int colGranteeType = colFilter + 1;
private static final int colGrantee = colGranteeType + 1;
private static final int colPrivilege = colGrantee + 1;
private static final int colWithGrantOption = colPrivilege + 1;

private static final int firstRow = 3;
private static final String sheetName = "Privileges";
private static final int processingStep = 2;
private static final String keyWordProductKey = "<prodKey>";

public static M79_Privileges_Utilities.PrivilegeDescriptors g_privileges;


public static String getGranteeType( String str) {
String returnValue;
returnValue = "";

str = str + "".trim().substring(0, 1).toUpperCase();
switch (str) {
case "U": {returnValue = "USER";
}case "G": {returnValue = "GROUP";
}case "P": {returnValue = "PUBLIC";
}default: {M04_Utilities.logMsg("Unknown Grantee-Type '" + str + "'", M01_Common.LogLevel.ellWarning, null, null, null);
}}
return returnValue;
}


private static void readSheet() {
M79_Privileges_Utilities.initPrivilegeDescriptors(M79_Privileges.g_privileges);

Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M04_Utilities.getWorkSheetName(sheetName, M03_Config.workSheetSuffix));
int thisRow;
thisRow = firstRow + (M00_Excel.getCell(thisSheet, 1, 1).getStringCellValue() == "" ? 0 : 1);

while (M00_Excel.getCell(thisSheet, thisRow, colOperation).getStringCellValue() + "" != "") {
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].sequenceNumber = M04_Utilities.getInteger(M00_Excel.getCell(thisSheet, thisRow, colSequenceNumber).getStringCellValue(), null);
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].environment = M00_Excel.getCell(thisSheet, thisRow, colEnvironment).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].operation = M00_Excel.getCell(thisSheet, thisRow, colOperation).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].objectType = M00_Excel.getCell(thisSheet, thisRow, colObjectType).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].schemaName = M00_Helper.replace(M00_Excel.getCell(thisSheet, thisRow, colSchemaName).getStringCellValue().trim(), keyWordProductKey, M03_Config.productKey);
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].objectName = M00_Excel.getCell(thisSheet, thisRow, colObjectName).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].filter = M00_Helper.replace(M00_Excel.getCell(thisSheet, thisRow, colFilter).getStringCellValue().trim(), keyWordProductKey, M03_Config.productKey);
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].granteeType = M79_Privileges.getGranteeType(M00_Excel.getCell(thisSheet, thisRow, colGranteeType).getStringCellValue());
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].grantee = M00_Excel.getCell(thisSheet, thisRow, colGrantee).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].privilege = M00_Excel.getCell(thisSheet, thisRow, colPrivilege).getStringCellValue().trim();
M79_Privileges.g_privileges.descriptors[M79_Privileges_Utilities.allocPrivilegeDescriptorIndex(M79_Privileges.g_privileges)].withGrantOption = M04_Utilities.getBoolean(M00_Excel.getCell(thisSheet, thisRow, colWithGrantOption).getStringCellValue(), null);

thisRow = thisRow + 1;
}
}


public static void getPrivileges() {
if ((M79_Privileges.g_privileges.numDescriptors == 0)) {
readSheet();
}
}


public static void resetPrivileges() {
M79_Privileges.g_privileges.numDescriptors = 0;
}


public static void genPrivilegesCsv(Integer ddlType) {
String fileName;
int fileNo;
fileName = M04_Utilities.genCsvFileName(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnDbPrivileges, processingStep, "DbAdmin", ddlType, null, null, null, null, null);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//On Error GoTo ErrorExit 

int i;
for (int i = 1; i <= M79_Privileges.g_privileges.numDescriptors; i++) {
M00_FileWriter.printToFile(fileNo, (M79_Privileges.g_privileges.descriptors[i].sequenceNumber > 0 ? "" + M79_Privileges.g_privileges.descriptors[i].sequenceNumber : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].environment.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].environment + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].operation.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].operation + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].objectType.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].objectType + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].schemaName.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].schemaName + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].objectName.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].objectName + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].filter.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].filter + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].granteeType.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].granteeType + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].grantee.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].grantee + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (!(M79_Privileges.g_privileges.descriptors[i].privilege.compareTo("") == 0) ? "\"" + M79_Privileges.g_privileges.descriptors[i].privilege + "\"" : "") + ",");
M00_FileWriter.printToFile(fileNo, (M79_Privileges.g_privileges.descriptors[i].withGrantOption ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void dropPrivilegesCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M04_Utilities.killCsvFileWhereEver(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.clnDbPrivileges, M01_Globals.g_targetDir, processingStep, onlyIfEmpty, "DbAdmin");
}


}