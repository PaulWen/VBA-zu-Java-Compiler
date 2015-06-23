package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M92_DBUtilities {




private static final int processingStepUtilities = 4;

public static final String tempTabNameOids = "SESSION.Oids";
public static final String tempTabNameInvExpOids = "SESSION.InvExpOids";


public static void genDdlForTempInvExpOids(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean withReplace; 
if (withReplaceW == null) {
withReplace = false;
} else {
withReplace = withReplaceW;
}

boolean onCommitPreserve; 
if (onCommitPreserveW == null) {
onCommitPreserve = false;
} else {
onCommitPreserve = onCommitPreserveW;
}

boolean onRollbackPreserve; 
if (onRollbackPreserveW == null) {
onRollbackPreserve = false;
} else {
onRollbackPreserve = onRollbackPreserveW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for OIDs", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M92_DBUtilities.tempTabNameInvExpOids);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

public static void genDdlForTempOids(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean withReplace; 
if (withReplaceW == null) {
withReplace = false;
} else {
withReplace = withReplaceW;
}

boolean onCommitPreserve; 
if (onCommitPreserveW == null) {
onCommitPreserve = false;
} else {
onCommitPreserve = onCommitPreserveW;
}

boolean onRollbackPreserve; 
if (onRollbackPreserveW == null) {
onRollbackPreserve = false;
} else {
onRollbackPreserve = onRollbackPreserveW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary tables for OIDs", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M92_DBUtilities.tempTabNameOids);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


public static void genDbUtilitiesDdl(Integer ddlType) {
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
M92_DBUtilities.genDbUtilitiesDdlByDdl(M01_Common.DdlTypeId.edtLdm);
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
M92_DBUtilities.genDbUtilitiesDdlByDdl(M01_Common.DdlTypeId.edtPdm);
// ### IF IVK ###

M92_DBUtilities.genDbUtilitiesDdlByPool(M01_Common.DdlTypeId.edtPdm, null, null);

int thisOrgIndex;
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
int thisPoolIndex;
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(thisPoolIndex, thisOrgIndex) &  M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal) {
M92_DBUtilities.genDbUtilitiesDdlByPool(M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex);
}
}
}
// ### ENDIF IVK ###
}
}


public static void genDbUtilitiesDdlByDdl(Integer ddlType) {
int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, processingStepUtilities, ddlType, null, null, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

String qualFuncNameStrTrim;
qualFuncNameStrTrim = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnStrTrim, ddlType, null, null, null, null, null, null);

String qualFuncNameStrElemIndexes;
String qualFuncNameLastStrElem;

String qualFuncNamePosStr;
qualFuncNamePosStr = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnPosStr, ddlType, null, null, null, null, null, true);

String qualFuncNameOccurs;
qualFuncNameOccurs = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnOccurs, ddlType, null, null, null, null, null, true);

String qualFuncNameOccursShort;
qualFuncNameOccursShort = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnOccursShort, ddlType, null, null, null, null, null, true);

int maxTrimParamLength;
maxTrimParamLength = 1024;
// ####################################################################################################################
// #    Function trimming limited length strings
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function trimming limited length strings", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStrTrim);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "VARCHAR(" + String.valueOf(maxTrimParamLength) + ")", true, "string-encode list delimited by 'delimiter_in'");
M11_LRT.genProcParm(fileNo, "", "maxLength_in", "INTEGER", true, "maximum length of string returned");
M11_LRT.genProcParm(fileNo, "", "trailer_in", "VARCHAR(5)", false, "trailer added at string end if string is cut off at the end");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxTrimParamLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_strLength", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_maxLength", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "special handling if input parameters are NULL", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (str_in IS NULL) OR (maxLength_in IS NULL) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN str_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_strLength = LENGTH(str_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_maxLength = maxLength_in - COALESCE(LENGTH(trailer_in), 0);");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_strLength > maxLength_in THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN LEFT(str_in, v_maxLength) || COALESCE(trailer_in, '');");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN str_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

String strParamDbDataType;
long maxStrElemNumber;
String contextParamDbDataType;
int maxElemLength;
boolean useContext;
String fNameSuffix;
int i;
for (int i = 1; i <= 3; i++) {
useContext = (i == 3);
contextParamDbDataType = "";
maxElemLength = 50;
useContext = false;
fNameSuffix = "";

if (i == 1) {
strParamDbDataType = "VARCHAR(4000)";
maxStrElemNumber = 1000;
} else if (i == 2) {
strParamDbDataType = "CLOB(1M)";
maxStrElemNumber = 250000;
} else if (i == 4) {
strParamDbDataType = "CLOB(100M)";
maxStrElemNumber = 2000000;
fNameSuffix = "_X";
} else {
strParamDbDataType = "VARCHAR(32672)";
maxStrElemNumber = 1000;
contextParamDbDataType = "VARCHAR(5)";
useContext = true;
maxElemLength = 200;
}

qualFuncNameStrElemIndexes = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnStrElemIndexes + fNameSuffix, ddlType, null, null, null, null, null, null);
qualFuncNameLastStrElem = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnLastStrElem + fNameSuffix, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    Function for retrieving the delimiter index-positions in string-encoded lists
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStrElemIndexes);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, true, "string-encode list delimited by 'delimiter_in'");
M11_LRT.genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal  INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES ( 0, 0 )");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ordinal+1,");
if (useContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(LOCATE(contextPrefix_in || delimiter_in || contextPostfix_in, list_in, posIndex + 1) + LENGTH(contextPrefix_in), LENGTH(list_in) + 1)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(NULLIF(LOCATE(delimiter_in, list_in, posIndex+1), 0), LENGTH(list_in)+1)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ordinal < " + String.valueOf(maxStrElemNumber));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
if (useContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LOCATE(contextPrefix_in || delimiter_in || contextPostfix_in, list_in, posIndex + LENGTH(delimiter_in)) <> 0");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LOCATE(delimiter_in, list_in, posIndex+1) <> 0");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MAX(ordinal)+1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LENGTH(list_in)+1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M22_Class_Utilities.printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameStrElemIndexes);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by ','");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal  INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
if (useContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + qualFuncNameStrElemIndexes + "(list_in, CAST(',' AS CHAR(1)), contextPrefix_in, contextPostfix_in)) AS X");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + qualFuncNameStrElemIndexes + "(list_in, CAST(',' AS CHAR(1)))) AS X");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function for retrieving the elements of string-encoded lists
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the elements of string-encoded lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualFuncNameStrElems);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, true, "string-encode list delimited by 'delimiter_in'");
M11_LRT.genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem     VARCHAR(" + String.valueOf(maxElemLength) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ordinal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ordinal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (useContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + qualFuncNameStrElemIndexes + "(list_in, delimiter_in, contextPrefix_in, contextPostfix_in)) AS X");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + qualFuncNameStrElemIndexes + "(list_in, delimiter_in)) AS X");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(LTRIM(SUBSTR(list_in, t1.posIndex+1, t2.posIndex - t1.posIndex - 1))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "t1.ordinal");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V AS t1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V AS t2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "t2.ordinal = t1.ordinal+1");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the elements of string-encoded lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualFuncNameStrElems);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by ','");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem     VARCHAR(" + String.valueOf(maxElemLength) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
if (useContext) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(list_in, CAST(',' AS CHAR(1)), contextPrefix_in, contextPostfix_in)) AS X");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(list_in, CAST(',' AS CHAR(1)))) AS X");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function for retrieving the last element of a string-encoded list
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the last element of a string-encoded list", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLastStrElem);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, true, "string-encode list delimited by 'delimiter_in'");
M11_LRT.genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxElemLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY posIndex DESC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(list_in, delimiter_in" + (useContext ? " , contextPrefix_in, contextPostfix_in" : "") + ")) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(elem, '') <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo =1");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the last element of a string-encoded list", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameLastStrElem);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by 'delimiter_in'");
if (useContext) {
M11_LRT.genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, true, "prefix supposed to precede the delimiter");
M11_LRT.genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, false, "postfix supposed to follow the delimiter");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(maxElemLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROWNUMBER() OVER (ORDER BY posIndex DESC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(list_in, CAST(',' AS CHAR(1))" + (useContext ? " , contextPrefix_in, contextPostfix_in" : "") + ")) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(elem, '') <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "seqNo =1");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    Function for retrieving the delimiter index-positions in string-encoded lists
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualFuncNameStrListMap);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list_in", "VARCHAR(4000)", true, "string-encode list delimited by ','");
M11_LRT.genProcParm(fileNo, "", "elemVar1_in", "VARCHAR(20)", true, "optional prefix which is placed in front of each string element (1st nstance)");
M11_LRT.genProcParm(fileNo, "", "elemVar2_in", "VARCHAR(20)", true, "optional prefix which is placed in front of each string element (2nd instance)");
M11_LRT.genProcParm(fileNo, "", "elemOp_in", "VARCHAR(20)", true, "optional infix placed between the two instances of each string element");
M11_LRT.genProcParm(fileNo, "", "conjunction_in", "VARCHAR(10)", true, "optional infix placed between each expression element");
M11_LRT.genProcParm(fileNo, "", "useBrackets_in", "INTEGER", false, "specifies whether brackets need to be placed around expressions per string element");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VARCHAR(8000)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 1, true);
M11_LRT.genVarDecl(fileNo, "v_result", "VARCHAR(8000)", "''", null, null);

M11_LRT.genProcSectionHeader(fileNo, "loop over list elements", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "elem,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "posIndex");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(list_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "posIndex ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_result =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_result ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(Case v_result WHEN '' THEN '' ELSE conjunction_in END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE useBrackets_in WHEN 1 THEN '(' ELSE '' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(elemVar1_in,'') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "elem ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "COALESCE(elemOp_in, '') ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE COALESCE(elemVar2_in,'') WHEN '' THEN '' ELSE elemVar2_in || elem END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(CASE useBrackets_in WHEN 1 THEN ')' ELSE '' END);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_result;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function for decomposing classId - OID Lists
// ####################################################################################################################

String qualFuncNameParseClassIdOidList;
qualFuncNameParseClassIdOidList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnParseClassIdOidList, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function for decomposing classId - OID Lists", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameParseClassIdOidList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "classOidList_in", "CLOB(1M)", false, "'|'-separated List of pairs 'classId,Oid'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_Pair");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "pair");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(LTRIM(REPLACE(REPLACE(elem, '<', ''), '>', '')))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(classOidList_in, CAST('|' AS CHAR(1))) ) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_PairResolved");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LEFT(pair, POSSTR(pair, ',')-1),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RIGHT(pair, LENGTH(pair)-POSSTR(pair, ','))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V_Pair");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(RIGHT('00000' || RTRIM(LTRIM(REPLACE(classId, '''', ''))), 5) AS " + M01_Globals.g_dbtEntityId + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtOid + "(oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "V_PairResolved");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function determining whether a character string represents a (BIG-) INTEGER
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function determining whether a character string represents a (BIG-) INTEGER", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualFuncNameIsNumeric);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "VARCHAR(25)", false, "string to analyze");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_str", "VARCHAR(25)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "if string is NULL return", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF str_in IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "strip off trailing and leading blank", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_str = LTRIM(RTRIM(str_in));");

M11_LRT.genProcSectionHeader(fileNo, "if string is empty it does not represent an INTEGER", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF (v_str = '') OR (POSSTR(v_str, '#') > 0) THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "replace any numeric character", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_str = REPLACE(TRANSLATE(v_str, '##########', '0123456789'), '#', '');");

M11_LRT.genProcSectionHeader(fileNo, "check whether result string is empty", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN (CASE WHEN LENGTH(v_str) =0 THEN 1 ELSE 0 END);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function determining the position of a search-string in a string
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function determining the position of a search-string in a string", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNamePosStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "VARCHAR(1024)", true, "string to search in");
M11_LRT.genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", false, "string to search for");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_i", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_length", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_uBound", "INTEGER", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_length = LENGTH(searchStr_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_uBound = COALESCE(LENGTH(str_in) - v_length + 1, 0);");

M11_LRT.genProcSectionHeader(fileNo, "search substring", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE v_i < v_uBound DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF SUBSTR(str_in, v_i, v_length) = searchStr_in THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RETURN v_i;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_i = v_i + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");

M11_LRT.genProcSectionHeader(fileNo, "string not found - return NULL", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN NULL;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function determining the number of occurrences of a search-string in a string
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function determining the number of occurrences of a search-string in a string", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameOccursShort);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "VARCHAR(32000)", true, "string to search in");
M11_LRT.genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", false, "string to search for");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_occurs", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "count matches", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF LENGTH(searchStr_in) > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_occurs = (LENGTH(str_in) - LENGTH(REPLACE(str_in, searchStr_in, '')) ) / LENGTH(searchStr_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return result", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_occurs;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function determining the number of occurrences of a search-string in a string", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameOccurs);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "CLOB(1M)", true, "string to search in");
M11_LRT.genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", false, "string to search for");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTEGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_occurs", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_posStart", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_posSearched", "INTEGER", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "count matches", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF LENGTH(searchStr_in) > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_posSearched = LENGTH(str_in);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_posStart    = LOCATE(searchStr_in, str_in);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE v_posStart > 0 DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_occurs   = v_occurs  + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_posStart = LOCATE(searchStr_in, str_in, v_posStart + 1);");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_posStart >= v_posSearched THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_occurs   = v_occurs  + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_posStart = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "return result", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_occurs;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    Function retrieving a substring from a string based on delimiter strings
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Function retrieving a substring from a string based on delimiter strings", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_qualFuncNameGetStrElem);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "str_in", "VARCHAR(1024)", true, "string to search in");
M11_LRT.genProcParm(fileNo, "", "beginDelim_in", "VARCHAR(20)", true, "delimiter string indicating the beginning of the string to retrieve");
M11_LRT.genProcParm(fileNo, "", "endDelim_in", "VARCHAR(20)", false, "delimiter string indicating the ending of the string to retrieve");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(1024)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_str", "VARCHAR(1024)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_pos", "INTEGER", "0", null, null);

M11_LRT.genProcSectionHeader(fileNo, "search begin delimiter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_pos = COALESCE(" + qualFuncNamePosStr + "(str_in, beginDelim_in), 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_pos = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "ignore anything before begin delimiter including delimiter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_str = SUBSTR(str_in, v_pos + LENGTH(beginDelim_in));");

M11_LRT.genProcSectionHeader(fileNo, "search end delimiter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_pos = COALESCE(" + qualFuncNamePosStr + "(v_str, endDelim_in), 0);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_pos = 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RETURN v_str;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "ignore anything after end delimiter including delimiter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN SUBSTR(v_str, 1,  v_pos-1);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

String qualFuncNameIsSubset;
qualFuncNameIsSubset = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexMeta, M01_ACM.udfnIsSubset, ddlType, null, null, null, null, null, true);

M22_Class_Utilities.printSectionHeader("Function determining whether set represented as delimiter-separated list is a subset of a second set", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameIsSubset);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "list1_in", "VARCHAR(500)", true, "delimiter-separated string-list-representation of set 1");
M11_LRT.genProcParm(fileNo, "", "list2_in", "VARCHAR(500)", true, "delimiter-separated string-list-representation of set 2");
M11_LRT.genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", false, "delimiter separating elements of lists");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "READS SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", 1, true);
M11_LRT.genVarDecl(fileNo, "v_isSubSet", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbTrue, null, null);

M11_LRT.genProcSectionHeader(fileNo, "for each element of set 1: check if it contained in set 2", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET1.elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(list1_in, delimiter_in) ) AS SET1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "TABLE ( " + M01_Globals.g_qualFuncNameStrElems + "(list2_in, delimiter_in) ) AS SET2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET1. elem = SET2.elem");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET1.elem <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET2.elem IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_isSubSet = " + M01_LDM.gc_dbFalse + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_isSubSet;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (M03_Config.generateFwkTest) {
goto NormalExit;
}

// ### IF IVK ###
// ####################################################################################################################
// #    SP for Setting up data pool specific data in REGISTRYSTATIC
// ####################################################################################################################

String qualProcNameRegStaticInit;
qualProcNameRegStaticInit = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnRegStaticInit, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Setting up Data Pool specific data in " + M01_Globals_IVK.g_qualTabNameRegistryStatic, fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameRegStaticInit);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) organization ID");
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the product structure");
M11_LRT.genProcParm(fileNo, "IN", "poolId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the data pool");

M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records inserted");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameRegStaticInit, ddlType, null, "orgId_in", "psOid_in", "poolId_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M92_DBUtilities.genDdlForRegStaticSstUpdate(fileNo, ddlType, 1, true, "orgId_in", null, "psOid_in", "poolId_in", null, null, null, null, null, null, null);

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameRegStaticInit, ddlType, null, "orgId_in", "psOid_in", "poolId_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for initializing Default Rebate
// ####################################################################################################################

String qualProcNameRebateInitDefault;
qualProcNameRebateInitDefault = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnRebateInitDefault, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Initializing Default Rebate value", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameRebateInitDefault);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "(optional) OID of the product structure to initialize rebate value for");

M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of records inserted");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameRebateInitDefault, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "insert default values", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRebateDefault);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUETYPE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN PS.PDIDIV_OID = 16 THEN 20 ELSE 25 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameProductStructure + " PS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals_IVK.g_qualTabNameRebateDefault + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PS." + M01_Globals.g_anOid + " = R." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "R." + M01_Globals_IVK.g_anPsOid + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(psOid_in, PS." + M01_Globals.g_anOid + ") = PS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameRebateInitDefault, ddlType, null, "psOid_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP verifying that a default rebate value is configured for a given ProductStructure
// ####################################################################################################################

String qualProcNameAssertRebateDefault;
qualProcNameAssertRebateDefault = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnAssertRebateDefault, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP verifying that a default rebate value is configured for a given ProductStructure", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameAssertRebateDefault);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the product structure verify");
M11_LRT.genProcParm(fileNo, "IN", "busErrMsg_in", M01_Globals.g_dbtBoolean, false, "(optional) if '1': use business error message");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M79_Err.genSigMsgVarDecl(fileNo, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameAssertRebateDefault, ddlType, null, "psOid_in", "busErrMsg_in", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify default value", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NOT EXISTS (SELECT 1 FROM " + M01_Globals_IVK.g_qualTabNameRebateDefault + " WHERE " + M01_Globals_IVK.g_anPsOid + " = psOid_in) THEN");
M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameAssertRebateDefault, ddlType, 2, "psOid_in", "busErrMsg_in", null, null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF busErrMsg_in = 1 THEN");
M79_Err.genSignalDdlWithParms("rebateDefNotDefBus", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M79_Err.genSignalDdlWithParms("rebateDefNotDef", fileNo, 3, null, null, null, null, null, null, null, null, null, "RTRIM(CHAR(psOid_in))", null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameAssertRebateDefault, ddlType, null, "psOid_in", "busErrMsg_in", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### ENDIF IVK ###
NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}
// ### IF IVK ###

public static void genDelDistNlTextProc(int fileNo, String procNameW, String schemaPrefixW, String tableNameW, String fkNameW) {
String procName; 
if (procNameW == null) {
procName = null;
} else {
procName = procNameW;
}

String schemaPrefix; 
if (schemaPrefixW == null) {
schemaPrefix = null;
} else {
schemaPrefix = schemaPrefixW;
}

String tableName; 
if (tableNameW == null) {
tableName = null;
} else {
tableName = tableNameW;
}

String fkName; 
if (fkNameW == null) {
fkName = null;
} else {
fkName = fkNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
//toDo remove hardcoded reference
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VL6CMET." + procName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN oid_in      BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_rowCount        INTEGER          DEFAULT 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M11_LRT.genProcSectionHeader(fileNo, "loop over all organizations", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + schemaPrefix + "' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) as c_schema");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
//toDo remove hardcoded reference
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VL6CDBM.PDMORGANIZATION_ENUM O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "O.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt = 'DELETE FROM ' || c_schema || '.V_" + tableName + "_LC NL WHERE NL." + fkName + " = ' || oid_in ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


}

public static void genDbUtilitiesDdlByPool(Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
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

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbMeta, processingStepUtilities, ddlType, thisOrgIndex, thisPoolIndex, null, M01_Common.phaseDbSupport, null);

//On Error GoTo ErrorExit 

boolean forPool;
forPool = (thisOrgIndex > 0) &  (thisPoolIndex > 0);

int countryIdListLength;
countryIdListLength = M25_Domain.g_domains.descriptors[M01_Globals_IVK.g_domainIndexCountryIdList].maxLength;

String qualTabNameCountryIdXRef;
qualTabNameCountryIdXRef = M04_Utilities.genQualTabNameByRelIndex(M01_Globals_IVK.g_relIndexCountryIdXRef, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, true, null);
String qualTabNameCountrySpec;
qualTabNameCountrySpec = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, true);

String qualViewNamePdmTable;
qualViewNamePdmTable = M04_Utilities.genQualViewName(M01_Globals.g_sectionIndexDbMeta, M01_ACM.vnPdmTable, M01_ACM.vnsPdmTable, ddlType, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;

// ####################################################################################################################
// #    user defined function normalizing lists of CountryIDs
// ####################################################################################################################

String qualFuncNameNormCidList;
qualFuncNameNormCidList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexCountry, M01_ACM_IVK.udfnNormalizeCountryIdList, ddlType, null, null, null, null, null, null);

if (!(forPool)) {
M22_Class_Utilities.printSectionHeader("UDF normalizing lists of CountryIDs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameNormCidList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "countryIdList_in", "VARCHAR(" + String.valueOf(countryIdListLength) + ")", false, "list of CountryIDs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VARCHAR(" + String.valueOf(countryIdListLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CONTAINS SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_result", "VARCHAR(" + String.valueOf(countryIdListLength) + ")", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "loop over list elements", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR countryLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_dbtEnumId + "(elem) AS countryId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(countryIdList_in)) AS X");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(elem, '') <> ''");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_dbtEnumId + "(elem) ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_result = COALESCE(v_result || ',', '') || RIGHT(DIGITS(countryId), 3);");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN v_result;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

// ####################################################################################################################
// #    user defined function maintaining table of lists of CountryIDs
// ####################################################################################################################

String qualTabNameCountryIdList;
qualTabNameCountryIdList = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCountryIdList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, true);
String qualFuncNameAssertCidList;
qualFuncNameAssertCidList = M04_Utilities.genQualFuncName(M01_Globals.g_sectionIndexCountry, M01_ACM_IVK.udfnAssertCountryIdList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(-1, ddlType, null, null, null, null);

M22_Class_Utilities.printSectionHeader("UDF maintaining table of lists of CountryIDs", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE FUNCTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualFuncNameAssertCidList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "", "countryIdList_in", "VARCHAR(" + String.valueOf(countryIdListLength) + ")", false, "list of CountryIDs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RETURNS TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "oid " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IDLIST VARCHAR(" + String.valueOf(countryIdListLength) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "DETERMINISTIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO EXTERNAL ACTION");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "MODIFIES SQL DATA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_countryIdListOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_countryIdList", "VARCHAR(" + String.valueOf(countryIdListLength) + ")", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "verify that input Country-ID list is not empty", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF countryIdList_in IS NOT NULL AND RTRIM(countryIdList_in) <> '' THEN");
M11_LRT.genProcSectionHeader(fileNo, "determine normalized Country-ID list", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_countryIdList = " + qualFuncNameNormCidList + "(countryIdList_in);");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of Country-ID list", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_countryIdListOid = (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "IDLIST = v_countryIdList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "if Country-ID list is not found create a new one", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_countryIdListOid IS NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_countryIdListOid = (NEXTVAL FOR " + qualSeqNameOid + ");");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IDLIST,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_countryIdListOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_countryIdList,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "RETURN SELECT OID, IDLIST FROM " + qualTabNameCountryIdList + " WHERE OID = v_countryIdListOid;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

String qualTriggerName;

if (!(forPool)) {
// ####################################################################################################################
// #    SP for Propagating EXPRESSIONs and TERMs between Data Pools
// ####################################################################################################################

String qualProcNamePropExpr;
qualProcNamePropExpr = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnPropExpr, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Propagating EXPRESSIONs and TERMs between Data Pools", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePropExpr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the product structure to propagate expressions and terms for");
M11_LRT.genProcParm(fileNo, "IN", "srcOrgId_in", M01_Globals.g_dbtEnumId, true, "organization ID of the 'source data pool'");
M11_LRT.genProcParm(fileNo, "IN", "srcPoolId_in", M01_Globals.g_dbtEnumId, true, "ID of the 'source data pool'");
M11_LRT.genProcParm(fileNo, "IN", "targetOrgId_in", M01_Globals.g_dbtEnumId, true, "organization ID of the 'target data pool'");
M11_LRT.genProcParm(fileNo, "IN", "targetPoolId_in", M01_Globals.g_dbtEnumId, true, "ID of the 'target data pool'");

M11_LRT.genProcParm(fileNo, "OUT", "numExprSuccess_out", "INTEGER", true, "number of Expressions propagated");
M11_LRT.genProcParm(fileNo, "OUT", "numExprFailed_out", "INTEGER", false, "number of Expressions failed to propagate (referred objects not available in target data pool)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_srcQualTabNameExpr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_targetQualTabNameExpr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_srcQualTabNameTerm", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_targetQualTabNameTerm", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_propFailed", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntExpr", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntProp", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_exprOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "foreignKeyNotFound", "23503", null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntExpr", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE exprCursor CURSOR FOR v_stmntExpr;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR foreignKeyNotFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_propFailed = " + M01_LDM.gc_dbTrue + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M92_DBUtilities.genDdlForTempOids(fileNo, null, true, null, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePropExpr, ddlType, null, "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", "numExprSuccess_out", "targetPoolId_in", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numExprSuccess_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numExprFailed_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine qualified table names", null, null);
String prefix;
int classIndex;
String suffix;
int i;
for (int i = 1; i <= 2; i++) {
if (i == 1) {
suffix = "Expr";
classIndex = M01_Globals_IVK.g_classIndexExpression;
} else {
suffix = "Term";
classIndex = M01_Globals_IVK.g_classIndexTerm;
}

int j;
for (int j = 1; j <= 2; j++) {
prefix = (j == 1 ? "src" : "target");
if ((i != 1) |  (j != 1)) {
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anPdmFkSchemaName + " || '.' || " + M01_Globals.g_anPdmTypedTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_" + prefix + "QualTabName" + suffix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePdmTable);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENTITY_ID = '" + M22_Class.g_classes.descriptors[classIndex].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDM_" + M01_Globals.g_anOrganizationId + " = " + prefix + "OrgId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDM_POOLTYPE_ID = " + prefix + "PoolId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LDM_ISLRT = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
}
}

M11_LRT.genProcSectionHeader(fileNo, "statement selecting Expressions in source data pool which do not exist in target data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntExpr =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'S." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_srcQualTabNameExpr || ' S ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'ISINVALID = 0 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'NOT EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_targetQualTabNameExpr || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'S." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anOid + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmntExpr FROM v_stmntExpr;");

M11_LRT.genProcSectionHeader(fileNo, "loop over Expressions in source data pool not in target data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN exprCursor;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FETCH exprCursor INTO v_exprOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHILE (SQLCODE = 0) DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_propFailed = " + M01_LDM.gc_dbFalse + ";");

M11_LRT.genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SAVEPOINT exprPropFail ON ROLLBACK RETAIN CURSORS;");

M11_LRT.genProcSectionHeader(fileNo, "propagate Terms corresponding to this Expression", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_targetQualTabNameTerm || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'* ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_srcQualTabNameTerm || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'" + M01_Globals.g_anAhOid + " = ' || CHAR(v_exprOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntProp;");

M11_LRT.genProcSectionHeader(fileNo, "propagate Expression", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_targetQualTabNameExpr || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'* ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_srcQualTabNameExpr || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'OID = ' || CHAR(v_exprOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntProp;");

M11_LRT.genProcSectionHeader(fileNo, "in case of failure: rollback to savepoint", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF v_propFailed = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ROLLBACK TO SAVEPOINT exprPropFail;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET numExprFailed_out = numExprFailed_out + 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET numExprSuccess_out = numExprSuccess_out + 1;");

M11_LRT.genProcSectionHeader(fileNo, "keep track of this OID - need to create ChangeLog record for it", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT INTO " + M92_DBUtilities.tempTabNameOids + " (OID) VALUES( v_exprOid );");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RELEASE SAVEPOINT exprPropFail;");

M11_LRT.genProcSectionHeader(fileNo, "retrieve next Expression", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH exprCursor INTO v_exprOid;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CLOSE exprCursor WITH RELEASE;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePropExpr, ddlType, null, "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", "numExprSuccess_out", "targetPoolId_in", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Propagating Invalid EXPRESSIONs and TERMs between Data Pools
// ####################################################################################################################

String qualProcNamePropInvExpr;
qualProcNamePropInvExpr = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnPropInvExpr, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Propagating Invalid EXPRESSIONs and TERMs between Data Pools", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNamePropInvExpr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the product structure to propagate expressions and terms for");
M11_LRT.genProcParm(fileNo, "IN", "srcOrgId_in", M01_Globals.g_dbtEnumId, true, "organization ID of the 'source data pool'");
M11_LRT.genProcParm(fileNo, "IN", "srcPoolId_in", M01_Globals.g_dbtEnumId, true, "ID of the 'source data pool'");
M11_LRT.genProcParm(fileNo, "IN", "targetOrgId_in", M01_Globals.g_dbtEnumId, true, "organization ID of the 'target data pool'");
M11_LRT.genProcParm(fileNo, "IN", "targetPoolId_in", M01_Globals.g_dbtEnumId, true, "ID of the 'target data pool'");
M11_LRT.genProcParm(fileNo, "IN", "setProductiveTs_in", "TIMESTAMP", true, "marks the timestamp of setting data productive");
M11_LRT.genProcParm(fileNo, "OUT", "numExprSuccess_out", "INTEGER", true, "number of Expressions propagated");
M11_LRT.genProcParm(fileNo, "OUT", "numExprFailed_out", "INTEGER", false, "number of Expressions failed to propagate (referred objects not available in target data pool)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_srcQualTabNameExpr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_targetQualTabNameExpr", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_srcQualTabNameTerm", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_targetQualTabNameTerm", "VARCHAR(100)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_recordCount", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_propFailed", M01_Globals.g_dbtBoolean, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntExpr", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntProp", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_exprOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, null);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statements", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntExpr", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE exprCursor CURSOR FOR v_stmntExpr;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M92_DBUtilities.genDdlForTempInvExpOids(fileNo, null, true, null, true);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNamePropExpr, ddlType, null, "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", "numExprSuccess_out", "targetPoolId_in", null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numExprSuccess_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numExprFailed_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "determine qualified table names", null, null);
for (int i = 1; i <= 2; i++) {
if (i == 1) {
suffix = "Expr";
classIndex = M01_Globals_IVK.g_classIndexExpression;
} else {
suffix = "Term";
classIndex = M01_Globals_IVK.g_classIndexTerm;
}

for (int j = 1; j <= 2; j++) {
prefix = (j == 1 ? "src" : "target");
if ((i != 1) |  (j != 1)) {
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anPdmFkSchemaName + " || '.' || " + M01_Globals.g_anPdmTypedTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_" + prefix + "QualTabName" + suffix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNamePdmTable);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENTITY_TYPE = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENTITY_ID = '" + M22_Class.g_classes.descriptors[classIndex].classIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDM_" + M01_Globals.g_anOrganizationId + " = " + prefix + "OrgId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PDM_POOLTYPE_ID = " + prefix + "PoolId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LDM_ISLRT = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH UR;");
}
}

//rs6
M11_LRT.genProcSectionHeader(fileNo, "statement inserting Expressions in target data pool which are invalid in source data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntExpr =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SESSION.InvExpOids ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_targetQualTabNameExpr || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T.ISINVALID = 0 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'T." + M01_Globals.g_anOid + " IN (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'S." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "v_srcQualTabNameExpr || ' S ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'S." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'S.ISINVALID = 1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntExpr;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET numExprSuccess_out = (SELECT count(oid) FROM SESSION.InvExpOids);");

M11_LRT.genProcSectionHeader(fileNo, "update Expressions in target data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'UPDATE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_targetQualTabNameExpr || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'SET ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'(ISINVALID, EXTTRM_OID, UPDATEUSER, LASTUPDATETIMESTAMP, VERSIONID) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'= ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'( SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.ISINVALID, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NULL AS EXTTRM_OID, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.UPDATEUSER, ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || setProductiveTs_in || ''', ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S.VERSIONID ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "v_srcQualTabNameExpr || ' S ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'S." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'T." + M01_Globals.g_anOid + " IN (SELECT oid FROM SESSION.InvExpOids) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'T." + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntProp;");

M11_LRT.genProcSectionHeader(fileNo, "delete Terms corresponding to this Expression", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'DELETE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_targetQualTabNameTerm || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals.g_anAhOid + " IN (SELECT oid FROM SESSION.InvExpOids) '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntProp;");

M11_LRT.genProcSectionHeader(fileNo, "update Expressions in source data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'UPDATE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_srcQualTabNameExpr || ' T ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'SET ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'EXTTRM_OID = NULL ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals.g_anOid + " IN (SELECT oid FROM SESSION.InvExpOids) '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntProp;");

M11_LRT.genProcSectionHeader(fileNo, "delete Terms in source data pool corresponding to this Expression", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntProp =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'DELETE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_srcQualTabNameTerm || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + M01_Globals.g_anAhOid + " IN (SELECT oid FROM SESSION.InvExpOids) '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE IMMEDIATE v_stmntProp;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNamePropExpr, ddlType, null, "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", "numExprSuccess_out", "targetPoolId_in", null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Broadcasting Changelog-Records
// ####################################################################################################################

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Broadcasting Changelog-Records (only for public 'insert' of non-NL & non-GEN records)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to broadcast to");
M11_LRT.genProcParm(fileNo, "IN", "poolId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the pool to broadcast to");

M11_LRT.genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", null, true);

M11_LRT.genProcParm(fileNo, "IN", "entityId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "entityType_in", M01_Globals.g_dbtEntityType, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahClassId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahObjectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "nl_in", M01_Globals.g_dbtBoolean, true, null);
M11_LRT.genProcParm(fileNo, "IN", "dbTableName_in", M01_Globals.g_dbtDbTableName, true, null);
M11_LRT.genProcParm(fileNo, "IN", "dbColumnName_in", M01_Globals.g_dbtDbColumnName, true, null);
M11_LRT.genProcParm(fileNo, "IN", "objectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "valueTypeId_in", M01_Globals.g_dbtInteger, true, null);
M11_LRT.genProcParm(fileNo, "IN", "oldValueBigInt_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "newValueBigInt_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "oldValueString_in", M01_Globals.g_dbtChangeLogString, true, null);
M11_LRT.genProcParm(fileNo, "IN", "newValueString_in", M01_Globals.g_dbtChangeLogString, true, null);
M11_LRT.genProcParm(fileNo, "IN", "oldValueInteger_in", M01_Globals.g_dbtInteger, true, null);
M11_LRT.genProcParm(fileNo, "IN", "newValueInteger_in", M01_Globals.g_dbtInteger, true, null);
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "operationId_in", M01_Globals.g_dbtEnumId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", true, null);

M11_LRT.genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", false, "number of changelog tables the message was broadcasted to");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_clRecordOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(10000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_opTimestamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameClBroadCast, ddlType, null, "orgId_in", "poolId_in", "'entityId_in", "'entityType_in", "'ahClassId_in", "ahObjectId_in", "'dbTableName_in", "objectId_in", "'cdUserId_in", "divisionOid_in", "psOid_in", null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET changeLogCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_opTimestamp = COALESCE(opTimestamp_in, CURRENT TIMESTAMP);");

M11_LRT.genProcSectionHeader(fileNo, "determine OID of Changelog Record", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_clRecordOid = NEXTVAL FOR " + qualSeqNameOid + ";");

M11_LRT.genProcSectionHeader(fileNo, "loop over ChangeLog-Tables in data pools to create Changelog-Record", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_clTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P2." + M01_Globals.g_anPdmTableName + " AS c_clsTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A2." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A2." + M01_Globals.g_anAcmEntityId + " IN ('" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexChangeLogStatus) + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A2." + M01_Globals.g_anAcmEntityType + " = L2." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A2." + M01_Globals.g_anAcmEntityName + " = L2." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A2." + M01_Globals.g_anAcmEntitySection + " = L2." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L2." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P2." + M01_Globals.g_anPdmLdmFkSchemaName + " = L2." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P2." + M01_Globals.g_anPdmLdmFkTableName + " = L2." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P2." + M01_Globals.g_anPoolTypeId + " = P." + M01_Globals.g_anPoolTypeId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P2." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M01_Globals.gc_acmEntityTypeKeyClass + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityId + " IN ('" + M22_Class.getClassIdStrByIndex(M01_Globals.g_classIndexChangeLog) + "')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " NOT IN (" + String.valueOf(M01_Globals_IVK.g_migDataPoolId) + ", " + String.valueOf(M01_Globals_IVK.g_archiveDataPoolId) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((poolId_in IS NULL) OR (P." + M01_Globals.g_anPoolTypeId + " = poolId_in) OR ((COALESCE(poolId_in,0) < 0) AND (P." + M01_Globals.g_anPoolTypeId + " <> -poolId_in)))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((orgId_in IS NULL) OR (P." + M01_Globals.g_anOrganizationId + " = orgId_in) OR ((COALESCE(orgId_in,0) < 0) AND (P." + M01_Globals.g_anOrganizationId + " <> -orgId_in)))");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_schemaName || '.' || c_clTableName ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anAcmEntityId + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anAcmEntityType + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anAhCid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'AHOBJECTID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NL,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DBTABLENAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DBCOLUMNNAME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'DIVISIONOID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OBJECTID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'VALUETYPE_ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OLDVALUEBIGINT,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NEWVALUEBIGINT,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OLDVALUESTRING,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NEWVALUESTRING,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OLDVALUEINTEGER,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'NEWVALUEINTEGER,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OPERATION_ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'OPTIMESTAMP,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals.g_anUserId + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'" + M01_Globals_IVK.g_anPsOid + "' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'VALUES' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'(' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(v_clRecordOid)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || entityId_in || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || entityType_in || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN ahClassId_in IS NULL THEN 'NULL' ELSE '''' || ahClassId_in || '''' END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN ahObjectId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(ahObjectId_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN nl_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(nl_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || dbTableName_in || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN dbColumnName_in IS NULL THEN 'NULL' ELSE '''' || dbColumnName_in || '''' END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN divisionOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(divisionOid_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN objectId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(objectId_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN valueTypeId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(valueTypeId_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN oldValueBigInt_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(oldValueBigInt_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN newValueBigInt_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(newValueBigInt_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN oldValueString_in IS NULL THEN 'NULL' ELSE '''' || oldValueString_in || '''' END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN newValueString_in IS NULL THEN 'NULL' ELSE '''' || newValueString_in || '''' END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN oldValueInteger_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(oldValueInteger_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN newValueInteger_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(newValueInteger_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "RTRIM(CHAR(operationId_in)) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || '''),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'''' || COALESCE(cdUserId_in, 'NN') || ''',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(CASE WHEN psOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(psOid_in)) END) ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "')';");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET changeLogCount_out = changeLogCount_out + 1;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_clsTableName IS NOT NULL THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'UPDATE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "c_schemaName || '.' || c_clsTableName || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'SET ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'LASTCOMMITTIME = TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || ''') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN psOid_in IS NULL THEN '" + M01_Globals_IVK.g_anPsOid + " IS NULL' ELSE '" + M01_Globals_IVK.g_anPsOid + " = ' || RTRIM(CHAR(psOid_in)) END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "(CASE WHEN divisionOid_in IS NULL THEN 'DIVISIONOID IS NULL' ELSE 'DIVISIONOID = ' || RTRIM(CHAR(divisionOid_in)) END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "' AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'WITHLRTCONTEXT = 0'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "IF v_rowCount = 0 THEN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'INSERT INTO ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "c_schemaName || '.' || c_clsTableName ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'" + M01_Globals_IVK.g_anPsOid + ",' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'DIVISIONOID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'LASTCOMMITTIME,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'WITHLRTCONTEXT' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "') VALUES (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(CASE WHEN psOid_in       IS NULL THEN 'NULL' ELSE RTRIM(CHAR(psOid_in      )) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(CASE WHEN divisionOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(divisionOid_in)) END) || ',' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || '''),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'0' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameClBroadCast, ddlType, null, "orgId_in", "poolId_in", "'entityId_in", "'entityType_in", "'ahClassId_in", "ahObjectId_in", "'dbTableName_in", "objectId_in", "'cdUserId_in", "divisionOid_in", "psOid_in", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to broadcast to");
M11_LRT.genProcParm(fileNo, "IN", "poolId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the pool to broadcast to");

M11_LRT.genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", null, true);

M11_LRT.genProcParm(fileNo, "IN", "entityId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "entityType_in", M01_Globals.g_dbtEntityType, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahClassId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahObjectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "nl_in", M01_Globals.g_dbtBoolean, true, null);
M11_LRT.genProcParm(fileNo, "IN", "dbTableName_in", M01_Globals.g_dbtDbTableName, true, null);
M11_LRT.genProcParm(fileNo, "IN", "dbColumnName_in", M01_Globals.g_dbtDbColumnName, true, null);
M11_LRT.genProcParm(fileNo, "IN", "objectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "valueTypeId_in", M01_Globals.g_dbtInteger, true, null);
M11_LRT.genProcParm(fileNo, "IN", "oldValueBigInt_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "newValueBigInt_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "operationId_in", M01_Globals.g_dbtEnumId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", true, null);

M11_LRT.genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", false, "number of changelog tables the message was broadcasted to");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcNameClBroadCast + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "poolId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahClassId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahObjectId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nl_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dbTableName_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dbColumnName_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "objectId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "valueTypeId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "oldValueBigInt_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "newValueBigInt_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "divisionOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operationId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "opTimestamp_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "changeLogCount_out);");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M11_LRT.genProcParm(fileNo, "IN", "orgId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the organization to broadcast to");
M11_LRT.genProcParm(fileNo, "IN", "poolId_in", M01_Globals.g_dbtEnumId, true, "(optional) ID of the pool to broadcast to");

M11_LRT.genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", null, true);

M11_LRT.genProcParm(fileNo, "IN", "entityId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "entityType_in", M01_Globals.g_dbtEntityType, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahClassId_in", M01_Globals.g_dbtEntityId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "ahObjectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "dbTableName_in", M01_Globals.g_dbtDbTableName, true, null);
M11_LRT.genProcParm(fileNo, "IN", "objectId_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "divisionOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, null);
M11_LRT.genProcParm(fileNo, "IN", "operationId_in", M01_Globals.g_dbtEnumId, true, null);
M11_LRT.genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", true, null);

M11_LRT.genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", false, "number of changelog tables the message was broadcasted to");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CALL " + qualProcNameClBroadCast + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "orgId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "poolId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahClassId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahObjectId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dbTableName_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "objectId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "null,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "cdUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "divisionOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "psOid_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operationId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "opTimestamp_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "changeLogCount_out);");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

//toDo remove hardcoded reference
M92_DBUtilities.genDelDistNlTextProc(fileNo, "DELAGGNODEDISTNLTEXT", "VL6CMET", "AGGREGATIONNODE_DIST_NL_TEXT", "ANLANO_OID");
M92_DBUtilities.genDelDistNlTextProc(fileNo, "DELENDNODEDISTNLTEXT", "VL6CMET", "ENDNODE_DIST_NL_TEXT", "ENLENO_OID");
M92_DBUtilities.genDelDistNlTextProc(fileNo, "DELGROUPDISTNLTEXT", "VL6CMET", "GROUP_DIST_NL_TEXT", "GNLGRP_OID");

String qualProcNameGetGroupElementsGlobal;
qualProcNameGetGroupElementsGlobal = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexMeta, M01_ACM_IVK.spnGetGroupElements, ddlType, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameGetGroupElementsGlobal);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN languageId_in           INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN fallbackLanguageId_in   INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN classId_in              VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IN groupElementOid_in      BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmntMerge      VARCHAR(2000)    DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_restmntTxt      VARCHAR(200)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_rowCount        INTEGER          DEFAULT 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_level           INTEGER          DEFAULT 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_groupId          VARCHAR(5)     DEFAULT '11022';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_aggNodeId        VARCHAR(5)     DEFAULT '11023';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_endNodeId        VARCHAR(5)     DEFAULT '11024';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_enHasGcId        VARCHAR(5)     DEFAULT '05011';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_enHasSrId        VARCHAR(5)     DEFAULT '09147';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_enHasPtId        VARCHAR(5)     DEFAULT '04035';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- declare statement");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_stmnt                   STATEMENT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_restmnt                 STATEMENT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- declare cursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- temporary table for GroupElements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.GroupElements");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "oid         BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "classid     VARCHAR(5),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "divOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "psOid       BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "orgOid      BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "accModeId   INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "entity      VARCHAR(250)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NOT LOGGED");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH REPLACE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = c_groupId THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " FROM VL6CMET.AGGREGATIONNODE AN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " WHERE ANGGRP_OID = groupElementOid_in ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE IGNORE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT MAXNUMBEROFLEVELS - 2 INTO v_level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM VL6CMET.GROUP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE OID = groupElementOid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = c_aggNodeId THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT MAXNUMBEROFLEVELS - 2 INTO v_level");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM VL6CMET.GROUP G");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN VL6CMET.AGGREGATIONNODE AN ON G.OID = AN.ANGGRP_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE AN.OID = groupElementOid_in;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in <= c_aggNodeId THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " FROM VL6CMET.AGGREGATIONNODE AN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " WHERE ANPANO_OID = groupElementOid_in ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE IGNORE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHILE v_level > 0 DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + " FROM SESSION.GroupElements GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + " JOIN VL6CMET.AGGREGATIONNODE AN ON AN.ANPANO_OID = GE.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + " WHERE GE.CLASSID = c_aggNodeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ELSE IGNORE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_level = v_level - 1;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END WHILE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING (SELECT EN.OID, c_endNodeId AS CLASSID, EVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " FROM VL6CMET.ENDNODE EN ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " WHERE EN.ENPANO_OID = groupElementOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE IGNORE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING (SELECT EN.OID, c_endNodeId AS CLASSID, EVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " FROM SESSION.GroupElements GE JOIN VL6CMET.ENDNODE EN ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + " ON EN.ENPANO_OID = GE.OID AND GE.CLASSID = c_aggNodeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.OID = S.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE IGNORE;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- loop over all organizations");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'VL6CAL' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) || D.ID as c_schema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "O.ORGOID AS c_org_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "D.ID AS c_acc_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VL6CDBM.PDMORGANIZATION_ENUM O JOIN VL6CDBM.PDMDATAPOOLTYPE_ENUM D ON D.ID > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "O.ID, D.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF c_acc_id <= 3 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasGcId || ''' AS CLASSID, GC.CDIDIV_OID AS DIV_OID, NULL AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.ENDNODEHASGENERICCODE EN ON EN.ENO_OID = GE.OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.GENERICCODE GC ON GC.OID = EN.GCO_OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasSrId || ''' AS CLASSID, NULL AS DIV_OID, EN.PS_OID AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.ENDNODEHASNSR1VALIDITY EN ON EN.ENO_OID = GE.OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasPtId || ''' AS CLASSID, NULL AS DIV_OID, EN.EPSPST_OID AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'JOIN VL6CPST.ENDNODEHASPROPERTYTEMPLATE EN ON EN.ENO_OID = GE.OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF classId_in = c_endNodeId THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- loop over all organizations");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'VL6CAL' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) || D.ID as c_schema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "O.ORGOID AS c_org_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "D.ID AS c_acc_id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "VL6CDBM.PDMORGANIZATION_ENUM O JOIN VL6CDBM.PDMDATAPOOLTYPE_ENUM D ON D.ID > 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "O.ID, D.ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "IF c_acc_id <= 3 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasGcId || ''' AS CLASSID, GC.CDIDIV_OID AS DIV_OID, NULL AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'FROM ' || c_schema || '.ENDNODEHASGENERICCODE EN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.GENERICCODE GC ON GC.OID = EN.GCO_OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasSrId || ''' AS CLASSID, NULL AS DIV_OID, EN.PS_OID AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'FROM ' || c_schema || '.ENDNODEHASNSR1VALIDITY EN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "END FOR;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasPtId || ''' AS CLASSID, NULL AS DIV_OID, EN.EPSPST_OID AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'FROM VL6CPST.ENDNODEHASPROPERTYTEMPLATE EN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "SET v_stmntMerge = v_stmntMerge || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "EXECUTE IMMEDIATE v_stmntMerge;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SESSION.GroupElements T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING (SELECT DISTINCT GE.classid, COALESCE(NL1.ENTITYLABEL, NL2.ENTITYLABEL, NL3.ENTITYLABEL, '') AS ENTITY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " FROM SESSION.GroupElements GE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " JOIN VL6CDBM.ACMENTITY E ON E.ENTITYID = GE.classid AND ((E.ENTITYTYPE = 'C' AND E.ENTITYID IN ('11022', '11023', '11024')) OR (E.ENTITYTYPE = 'R' AND E.ENTITYID IN ('05011', '09147', '04035')))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL1 ON E.ENTITYSECTION = NL1.ENTITYSECTION AND E.ENTITYNAME = NL1.ENTITYNAME AND E.ENTITYTYPE = NL1.ENTITYTYPE AND NL1.LANGUAGE_ID = languageId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL2 ON E.ENTITYSECTION = NL2.ENTITYSECTION AND E.ENTITYNAME = NL2.ENTITYNAME AND E.ENTITYTYPE = NL2.ENTITYTYPE AND NL2.LANGUAGE_ID = fallbackLanguageId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL3 ON E.ENTITYSECTION = NL3.ENTITYSECTION AND E.ENTITYNAME = NL3.ENTITYNAME AND E.ENTITYTYPE = NL3.ENTITYTYPE AND NL3.LANGUAGE_ID = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T.classid = S.CLASSID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHEN MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE SET T.entity = S.ENTITY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE IGNORE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_restmntTxt = 'SELECT DISTINCT oid, classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElements ORDER BY divOid, psOid, orgOid, accModeId';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_restmnt FROM v_restmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OPEN c_return;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


}


// ####################################################################################################################
// #    INSERT Trigger handling new CountryID list
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M01_Globals_IVK.g_classIndexCountryIdList, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "_INS", null, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger handling new CountryID list in table \"" + qualTabNameCountryIdList + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabNameCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_idList", "VARCHAR(" + String.valueOf(countryIdListLength) + ")", "NULL", null, null);

if (!(forPool)) {
M11_LRT.genVarDecl(fileNo, "v_cdUserId", M01_Globals.g_dbtUserId, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_psOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_numClRecords", "INTEGER", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_clRecordOid", M01_Globals.g_dbtOid, "NULL", null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "normalize CountryId list", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_idList = " + qualFuncNameNormCidList + "(" + M01_Globals.gc_newRecordName + ".IDLIST);");

M11_LRT.genProcSectionHeader(fileNo, "persist normalized CountryId list", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IDLIST = v_idList");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "maintain table \"" + qualTabNameCountryIdXRef + "\"", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCountryIdXRef);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_relIndexCountryIdXRef, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, fileNo, ddlType, null, null, 2, false, false, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.setAttributeMapping(transformation, 1, "CIL_OID", M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, "CSP_OID", "CSP." + M01_Globals.g_anOid, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_relIndexCountryIdXRef, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, transformation, fileNo, ddlType, null, null, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TABLE (" + M01_Globals.g_qualFuncNameStrElems + "(v_idList)) CID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCountrySpec + " CSP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CSP.ID = " + M01_Globals.g_dbtEnumId + "(CID.elem)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (!(forPool)) {
M11_LRT.genProcSectionHeader(fileNo, "CountryID lists are 'common to Productstructure and Organization'", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_psOid       = NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_divisionOid = NULL;");

M11_LRT.genProcSectionHeader(fileNo, "determine User id", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");

M11_LRT.genProcSectionHeader(fileNo, "create Changelog Records", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexCountryIdList].classIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M01_Globals.gc_acmEntityTypeKeyClass + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.getUnqualObjName(M01_Globals_IVK.g_qualTabNameCountryIdList) + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "" + String.valueOf(M11_LRT.lrtStatusNonLrtCreated) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (M03_Config.generateUpdatableCheckInUpdateTrigger & ! forPool) {
// ####################################################################################################################
// #    UPDATE Trigger prohibiting updates on CountryID list
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M01_Globals_IVK.g_classIndexCountryIdList, ddlType, null, null, null, null, null, null, null, "_UPD", null, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger prohibiting updates on CountryID list in table \"" + M01_Globals_IVK.g_qualTabNameCountryIdList + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "verify that update maintains equivalence of ID lists", 1, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF " + qualFuncNameNormCidList + "(" + M01_Globals.gc_newRecordName + ".IDLIST) <> " + qualFuncNameNormCidList + "(" + M01_Globals.gc_oldRecordName + ".IDLIST) THEN");
M79_Err.genSignalDdl("updateNotAllowed", fileNo, 2, M01_ACM_IVK.clnCountryIdList, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

if (M03_Config.generateUpdatableCheckInUpdateTrigger & ! forPool & !M03_Config.generateFwkTest) {
String qualTabNameCtsConfigHistory;

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M01_Globals_IVK.g_classIndexCtsConfig, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "_INS", null, null);
qualTabNameCtsConfigHistory = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexCtsConfigHistory, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

// ####################################################################################################################
// #    INSERT Trigger propagating record from CTSCONFIG to CTSCONFIGHISTORY
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("Insert-Trigger propagating records from \"" + M01_Globals_IVK.g_qualTabNameCtsConfig + "\" to \"" + qualTabNameCtsConfigHistory + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameCtsConfig);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "propagate record", null, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameCtsConfigHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexCtsConfig, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 2, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCtsConfig, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, null, null, 2, null, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UPDATE Trigger propagating record from CTSCONFIG to CTSCONFIGHISTORY
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByClassIndex(M01_Globals_IVK.g_classIndexCtsConfig, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, "_UPD", null, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger propagating records from \"" + M01_Globals_IVK.g_qualTabNameCtsConfig + "\" to \"" + qualTabNameCtsConfigHistory + "\"", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AFTER UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals_IVK.g_qualTabNameCtsConfig);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_ignorePropagate", M01_Globals.g_dbtBoolean, M01_LDM.gc_dbFalse, null, null);

M11_LRT.genProcSectionHeader(fileNo, "determine whether this update needs to be reported in history", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR recordLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM_IVK.conRuleScopeId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM_IVK.conServiceType, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, "CORORG_OID", "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM_IVK.conPsOid, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM.conVersionId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM.conOid, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexCtsConfig, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, 3, null, null, M01_Common.DdlOutputMode.edomNone, null);
int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tabColumns.descriptors[k].columnName + " AS c_" + tabColumns.descriptors[k].acmAttributeName + (k == tabColumns.numDescriptors ? "" : ","));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCtsConfigHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anRuleScope + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anRuleScope);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SERVICETYPE = " + M01_Globals.gc_newRecordName + ".SERVICETYPE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CORORG_OID = " + M01_Globals.gc_newRecordName + ".CORORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals.gc_newRecordName + "." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "TIMESTAMP DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH FIRST 1 ROW ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_ignorePropagate =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CASE WHEN");

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
if (k > 1) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
}
if (tabColumns.descriptors[k].columnName.compareTo("SIZEFACTOR") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[k].columnName + " > (0.9 * c_" + tabColumns.descriptors[k].acmAttributeName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[k].columnName + " < (1.1 * c_" + tabColumns.descriptors[k].acmAttributeName + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + M01_Globals.gc_newRecordName + "." + tabColumns.descriptors[k].columnName + " = c_" + tabColumns.descriptors[k].acmAttributeName);
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "THEN 1 ELSE 0 END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "propagate record", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF v_ignorePropagate = " + M01_LDM.gc_dbFalse + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameCtsConfigHistory);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute.genAttrListForEntity(M01_Globals_IVK.g_classIndexCtsConfig, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, ddlType, null, null, 3, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VALUES");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(M01_Globals_IVK.g_classIndexCtsConfig, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, fileNo, ddlType, null, null, 3, null, null, null, M01_Common.DdlOutputMode.edomList, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genDdlForRegStaticSstUpdate(int fileNo, Integer ddlTypeW, Integer indentW, Boolean useStaticSqlW, String orgIdFilterStrW, String orgOidFilterStrW, String psOidFilterStrW, String accessModeIdFilterStrW, String stmntTxtVarNameW, String tempTableNameW, String stmntColNameW, String modeVarNameW, String rowCountVarNameW, String rowCountSumVarNameW, Boolean genTimestampDdlW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean useStaticSql; 
if (useStaticSqlW == null) {
useStaticSql = true;
} else {
useStaticSql = useStaticSqlW;
}

String orgIdFilterStr; 
if (orgIdFilterStrW == null) {
orgIdFilterStr = "";
} else {
orgIdFilterStr = orgIdFilterStrW;
}

String orgOidFilterStr; 
if (orgOidFilterStrW == null) {
orgOidFilterStr = "";
} else {
orgOidFilterStr = orgOidFilterStrW;
}

String psOidFilterStr; 
if (psOidFilterStrW == null) {
psOidFilterStr = "";
} else {
psOidFilterStr = psOidFilterStrW;
}

String accessModeIdFilterStr; 
if (accessModeIdFilterStrW == null) {
accessModeIdFilterStr = "";
} else {
accessModeIdFilterStr = accessModeIdFilterStrW;
}

String stmntTxtVarName; 
if (stmntTxtVarNameW == null) {
stmntTxtVarName = "v_stmntTxt";
} else {
stmntTxtVarName = stmntTxtVarNameW;
}

String tempTableName; 
if (tempTableNameW == null) {
tempTableName = M94_DBAdmin.tempTabNameStatement;
} else {
tempTableName = tempTableNameW;
}

String stmntColName; 
if (stmntColNameW == null) {
stmntColName = "statement";
} else {
stmntColName = stmntColNameW;
}

String modeVarName; 
if (modeVarNameW == null) {
modeVarName = "mode_in";
} else {
modeVarName = modeVarNameW;
}

String rowCountVarName; 
if (rowCountVarNameW == null) {
rowCountVarName = "v_rowCount";
} else {
rowCountVarName = rowCountVarNameW;
}

String rowCountSumVarName; 
if (rowCountSumVarNameW == null) {
rowCountSumVarName = "rowCount_out";
} else {
rowCountSumVarName = rowCountSumVarNameW;
}

boolean genTimestampDdl; 
if (genTimestampDdlW == null) {
genTimestampDdl = false;
} else {
genTimestampDdl = genTimestampDdlW;
}

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(-1, ddlType, null, null, null, null);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;

M11_LRT.genProcSectionHeader(fileNo, "setup organization-specific data in \"" + M01_Globals_IVK.g_qualTabNameRegistryStatic + "\"", indent, null);
if (useStaticSql) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_qualTabNameRegistryStatic);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conVersionId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRegistryStatic, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, indent + 1, null, null, M01_Common.DdlOutputMode.edomNone, null);
int k;
for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabColumns.descriptors[k].columnName + (k == tabColumns.numDescriptors ? "" : ","));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_Section");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "section");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "VALUES('STANDARDXML') UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "VALUES('VDFXML')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_RefPs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT MIN(OID) FROM " + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_Src");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AM.ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V.section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "CAST(R." + M01_Globals_IVK.g_anValue + " AS VARCHAR(30))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_qualTabNameDataPoolAccessMode + " AM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_RefPs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AM.ID IN (2,3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "O.ID = 2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals_IVK.g_qualTabNameRegistryStatic + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "R." + M01_Globals_IVK.g_anSubKey + " = RTRIM(CHAR(O.ORGOID)) || ',' || RTRIM(CHAR(V_RefPs.oid)) || ',' || RTRIM(CHAR(AM.ID))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals_IVK.g_anKey + " = 'DESTINATION'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_Section V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "R.SECTION = V.section");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_Default");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AM.ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V.section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'temp'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_Section V,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.g_qualTabNameDataPoolAccessMode + " AM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AM.ID IN (2,3)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_Values");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "prio,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT 1, accessModeId, section, value FROM V_Src");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT 2, accessModeId, section, value FROM V_Default");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_ValuesBestMatch");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "prio,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "prio,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "accessModeId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "section,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "value");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V_Values V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "V_Values V2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "V.accessModeId = V2.accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "V.section = V2.section");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "V.prio > V2.prio");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conCreateUser, "RTRIM(CURRENT USER)", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conSection, "V.section", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conKey, "'DESTINATION'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conSubKey, "RTRIM(CHAR(DP.DPOORG_OID)) || ',' || RTRIM(CHAR(DP.DPSPST_OID)) || ',' || RTRIM(CHAR(DP." + M01_Globals.g_anAccessModeId + "))", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conValue, "V.value", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRegistryStatic, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, indent + 1, null, null, M01_Common.DdlOutputMode.edomNone, null);

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomList, null, null, null, null) + (k < tabColumns.numDescriptors ? "," : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameDataPool + " DP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_ValuesBestMatch V");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DP." + M01_Globals.g_anAccessModeId + " = V.accessModeId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNamePdmOrganization + " O");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DP.DPOORG_OID = O.ORGOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "O.ID = COALESCE(orgId_in, O.ID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DP.DPSPST_OID = COALESCE(psOid_in, DP.DPSPST_OID)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DP." + M01_Globals.g_anAccessModeId + " = COALESCE(poolId_in, DP." + M01_Globals.g_anAccessModeId + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals_IVK.g_qualTabNameRegistryStatic + " R");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "R.SECTION = V.section");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "R." + M01_Globals_IVK.g_anKey + " = 'DESTINATION'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "R." + M01_Globals_IVK.g_anSubKey + " = RTRIM(CHAR(DP.DPOORG_OID)) || ',' || RTRIM(CHAR(DP.DPSPST_OID)) || ',' || RTRIM(CHAR(DP." + M01_Globals.g_anAccessModeId + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

if (rowCountVarName != "") {
M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "GET DIAGNOSTICS " + rowCountVarName + " = ROW_COUNT;");
if (rowCountSumVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + "SET " + rowCountSumVarName + " = " + rowCountSumVarName + " + " + rowCountVarName + ";");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET " + stmntTxtVarName + " = 'INSERT INTO " + M01_Globals_IVK.g_qualTabNameRegistryStatic + " (' ||");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 4, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conVersionId, "", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRegistryStatic, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, indent + 1, null, null, M01_Common.DdlOutputMode.edomNone, null);

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + tabColumns.descriptors[k].columnName + (k == tabColumns.numDescriptors ? "" : ",") + "' ||");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'WITH ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_Section ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'section' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'VALUES(''STANDARDXML'') UNION ALL ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'VALUES(''VDFXML'')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'),' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_RefPs' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'oid' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT MIN(OID) FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + "' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_Src' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'accessModeId,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'value' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AM.ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V.section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'CAST(R." + M01_Globals_IVK.g_anValue + " AS VARCHAR(30)) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals.g_qualTabNameDataPoolAccessMode + " AM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V_RefPs ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AM.ID IN (2,3) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals.g_qualTabNamePdmOrganization + " O ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'O.ID = 2 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals_IVK.g_qualTabNameRegistryStatic + " R ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'R." + M01_Globals_IVK.g_anSubKey + " = RTRIM(CHAR(O.ORGOID)) || '','' || RTRIM(CHAR(V_RefPs.oid)) || '','' || RTRIM(CHAR(AM.ID)) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals_IVK.g_anKey + " = ''DESTINATION'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V_Section V ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'R.SECTION = V.section ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_Default ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'( ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'accessModeId,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'value' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AM.ID,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V.section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'''temp'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V_Section V,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'" + M01_Globals.g_qualTabNameDataPoolAccessMode + " AM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AM.ID IN (2,3)' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_Values ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'prio,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'accessModeId,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'value ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT 1, accessModeId, section, value FROM V_Src ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'UNION ALL ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT 2, accessModeId, section, value FROM V_Default ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'), ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_ValuesBestMatch ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'(' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'prio,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'accessModeId,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'value' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'AS ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'( ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'prio,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'accessModeId,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'section,' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'value ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'V_Values V ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'NOT EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'V_Values V2 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'V.accessModeId = V2.accessModeId ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'V.section = V2.section ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'V.prio > V2.prio' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "') ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'SELECT ' ||");

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 10, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conCreateUser, "'''' || RTRIM(CURRENT USER) || ''''", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 5, M01_ACM.conVersionId, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 6, M01_ACM.conOid, "'NEXTVAL FOR " + qualSeqNameOid + "'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 7, M01_ACM_IVK.conSection, "'V.section'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 8, M01_ACM_IVK.conKey, "'''DESTINATION'''", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 9, M01_ACM_IVK.conSubKey, "'RTRIM(CHAR(DP.DPOORG_OID)) || '','' || RTRIM(CHAR(DP.DPSPST_OID)) || '','' || RTRIM(CHAR(DP." + M01_Globals.g_anAccessModeId + "))'", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 10, M01_ACM_IVK.conValue, "'V.value'", null, null, null);

M24_Attribute.genTransformedAttrListForEntityWithColReuse(M01_Globals_IVK.g_classIndexRegistryStatic, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, ddlType, null, null, indent + 1, null, null, M01_Common.DdlOutputMode.edomNone, null);

for (int k = 1; k <= tabColumns.numDescriptors; k++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.transformAttrName(tabColumns.descriptors[k].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[k].dbDomainIndex, transformation, ddlType, null, null, null, true, tabColumns.descriptors[k].acmAttributeIndex, M01_Common.DdlOutputMode.edomList, null, null, null, null) + " ||" + (k < tabColumns.numDescriptors ? " ',' ||" : ""));
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + M01_Globals.g_qualTabNameDataPool + " DP ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'V_ValuesBestMatch V ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'DP." + M01_Globals.g_anAccessModeId + " = V.accessModeId ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'INNER JOIN ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + M01_Globals.g_qualTabNamePdmOrganization + " O ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'ON ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'DP.DPOORG_OID = O.ORGOID ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'O.ID = COALESCE(orgId_in, O.ID) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'DP.DPSPST_OID = COALESCE(psOid_in, DP.DPSPST_OID) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'DP." + M01_Globals.g_anAccessModeId + " = COALESCE(poolId_in, DP." + M01_Globals.g_anAccessModeId + ") ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'NOT EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'" + M01_Globals_IVK.g_qualTabNameRegistryStatic + " R ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'R.SECTION = V.section ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'R." + M01_Globals_IVK.g_anKey + " = ''DESTINATION'' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "'R." + M01_Globals_IVK.g_anSubKey + " = RTRIM(CHAR(DP.DPOORG_OID)) || '','' || RTRIM(CHAR(DP.DPSPST_OID)) || '','' || RTRIM(CHAR(DP." + M01_Globals.g_anAccessModeId + "))' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "')'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

int offset;
offset = (modeVarName == "" ? 0 : 1);
if (tempTableName != "") {
M00_FileWriter.printToFile(fileNo, "");
if (modeVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF " + modeVarName + " <= 1 THEN");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + offset) + "INSERT INTO " + tempTableName + "(" + stmntColName + ") VALUES (" + stmntTxtVarName + ");");

if (genTimestampDdl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + offset) + "INSERT INTO " + tempTableName + "(statement) VALUES ('VALUES CURRENT TIMESTAMP');");
}

if (modeVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}
}

M00_FileWriter.printToFile(fileNo, "");
if (modeVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "IF " + modeVarName + " >= 1 THEN");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + offset) + "EXECUTE IMMEDIATE " + stmntTxtVarName + ";");
if (rowCountVarName != "") {
M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", indent + offset, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + offset) + "GET DIAGNOSTICS " + rowCountVarName + " = ROW_COUNT;");
if (rowCountSumVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + offset) + "SET " + rowCountSumVarName + " = " + rowCountSumVarName + " + " + rowCountVarName + ";");
}
}
if (modeVarName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END IF;");
}

}
}
// ### ENDIF IVK ###






}