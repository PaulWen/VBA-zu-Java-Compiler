package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M96_DdlSummary {


private static final int colRowNum = 2;
private static final int colSchemaName = 3;

private static final int colTabName = 4;
private static final int colTabNotAcmRelated = colTabName + 1;
private static final int colAttrName = colTabNotAcmRelated + 1;
private static final int colAttrNameReserved = colAttrName + 1;
private static final int colDBType = colAttrNameReserved + 1;
private static final int colLength = colDBType + 1;
private static final int colSpecifics = colLength + 1;

private static final int colFirst = colTabName;
private static final int colLast = colSpecifics;
private static final int numCols = colSpecifics;

private static final int firstRow = 1;

private static int activeRow;
private static boolean didPrintTabName;

private static String thisTabName;
private static String thisSchemaName;
private static boolean thisNotAcmRelated;


private static void initVars() {
if (activeRow > 0) {
return;
}

activeRow = firstRow;
didPrintTabName = false;
thisTabName = "";
thisSchemaName = "";
thisNotAcmRelated = true;
}


public static void resetDdl() {
activeRow = 0;
}


public static void addAttrToDdlSummary(String attrName, String dataType, String length, String specifics, Integer ddlType) {
if (ddlType != M01_Common.DdlTypeId.edtLdm) {
return;
}

initVars();

int legalErroNo;
legalErroNo = 9;
//On Error GoTo ErrorExit 
Sheet thisSheet;
thisSheet = M00_Excel.activeWorkbook.getSheet(M01_Globals.g_sheetNameDdlSummary);
legalErroNo = -999;
if (!(didPrintTabName)) {
M00_Excel.getCell(thisSheet, activeRow, colTabName).getStringCellValue().setCellValue(thisTabName);
M00_Excel.getCell(thisSheet, activeRow, colTabNotAcmRelated).getStringCellValue().setCellValue((thisNotAcmRelated ? "1" : ""));
didPrintTabName = true;
}

M00_Excel.getCell(thisSheet, activeRow, colAttrName).getStringCellValue().setCellValue(attrName);
M00_Excel.getCell(thisSheet, activeRow, colDBType).getStringCellValue().setCellValue(dataType);
if (!(length.compareTo("") == 0)) {
M00_Excel.getCell(thisSheet, activeRow, colLength).getStringCellValue().setCellValue(length);
}
M00_Excel.getCell(thisSheet, activeRow, colSpecifics).getStringCellValue().setCellValue(specifics);
M00_Excel.getCell(thisSheet, activeRow, colRowNum).getStringCellValue().setCellValue(activeRow);
M00_Excel.getCell(thisSheet, activeRow, 1).getStringCellValue().setCellValue(M20_Section.getSectionSeqNoByName(thisSchemaName));
M00_Excel.getCell(thisSheet, activeRow, colSchemaName).getStringCellValue().setCellValue(thisSchemaName);
M00_Excel.getCell(thisSheet, activeRow, colTabName).getStringCellValue().setAsActiveCell();
activeRow = activeRow + 1;

NormalExit:
return;

ErrorExit:
if (Err.Number != legalErroNo) {
Err.Raise(Err.Number, Err.Source, Err.description, Err.HelpFile, Err.HelpContext);
}
}


public static void addTabToDdlSummary(String tabName, Integer ddlType, boolean notAcmRelated) {
if (ddlType != M01_Common.DdlTypeId.edtLdm) {
return;
}

initVars();

didPrintTabName = false;
thisTabName = M04_Utilities.getUnqualObjName(tabName);
thisSchemaName = M04_Utilities.getSchemaName(tabName);
thisNotAcmRelated = notAcmRelated;
}

}