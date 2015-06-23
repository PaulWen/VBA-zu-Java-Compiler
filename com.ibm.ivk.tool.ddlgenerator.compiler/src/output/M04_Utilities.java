package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M04_Utilities {




int nextOid;

private static int ddlEmptyFileSize;

private static String targetDir;

public static class ObjNameDelimMode {
public static final int eondmNone = 0;
public static final int eondmPrefix = 1;
public static final int eondmSuffix = 2;
public static final int eondmInfix = 4;

public static final int eondmAll = eondmPrefix |  eondmSuffix | eondmInfix;
public static final int eondmFrame = eondmPrefix |  eondmSuffix;
}

public class CodeScope {
public static final int ecsBase = 0;
public static final int ecsDecl = 1;
public static final int ecsBody = 2;
}


// ### IF IVK ###
private static class ModuleDescriptor {
public String moduleName;
public vbext_ComponentType moduleType;

public ModuleDescriptor(String moduleName, vbext_ComponentType moduleType) {
this.moduleName = moduleName;
this.moduleType = moduleType;
}
}

private static class ModuleDescriptors {
public ModuleDescriptor[] descriptors;
public int numDescriptors;

public ModuleDescriptors(int numDescriptors, ModuleDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

private static class ProcDescriptor {
public String procName;
public long startsAtLine;
public boolean hasErrorHandler;
public boolean hasErrorExit;

public ProcDescriptor(String procName, long startsAtLine, boolean hasErrorHandler, boolean hasErrorExit) {
this.procName = procName;
this.startsAtLine = startsAtLine;
this.hasErrorHandler = hasErrorHandler;
this.hasErrorExit = hasErrorExit;
}
}

private static class ProcDescriptors {
public ProcDescriptor[] descriptors;
public int numDescriptors;

public ProcDescriptors(int numDescriptors, ProcDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static String dirName(String fileName) {
String returnValue;
long intI;

if (fileName.compareTo("") == 0) {
returnValue = "";
return returnValue;
}

for (intI = fileName.length(); intI <= -1; intI += (-1)) {
if (fileName.substring(intI - 1, intI + 1 - 1) == "/" |  fileName.substring(intI - 1, intI + 1 - 1) == "\\") {
;
}

}

returnValue = fileName.substring(0, (intI > 0 ? intI - 1 : intI));
return returnValue;
}


public static String baseName( String fileName, String suffixListW, String delimiterW, String suffixDelimiterW, String suffixListDelimiterW) {
String suffixList; 
if (suffixListW == null) {
suffixList = "";
} else {
suffixList = suffixListW;
}

String delimiter; 
if (delimiterW == null) {
delimiter = "";
} else {
delimiter = delimiterW;
}

String suffixDelimiter; 
if (suffixDelimiterW == null) {
suffixDelimiter = "";
} else {
suffixDelimiter = suffixDelimiterW;
}

String suffixListDelimiter; 
if (suffixListDelimiterW == null) {
suffixListDelimiter = ",";
} else {
suffixListDelimiter = suffixListDelimiterW;
}

String returnValue;
long intI;
long lastI;
String base;
int fileNameLen;
String[] suffixElems;

if (fileName.compareTo("") == 0) {
returnValue = "";
return returnValue;
}

suffixElems = suffixList.split(suffixListDelimiter);

lastI = 0;
if (delimiter == "") {
if (fileName.substring(fileName.length() - 1 - 1) == "\\" |  fileName.substring(fileName.length() - 1 - 1) == "/") {
fileName = fileName.substring(0, fileName.length() - 1);
}

fileNameLen = fileName.length();
for (intI = 1; intI <= 1; intI += (1)) {
if (fileName.substring(intI - 1, intI + 1 - 1) == "/" |  fileName.substring(intI - 1, intI + 1 - 1) == "\\") {
lastI = intI;
}
}
} else {
fileNameLen = fileName.length();
for (intI = 1; intI <= 1; intI += (1)) {
if (fileName.substring(intI - 1, intI + 1 - 1) == delimiter) {
lastI = intI;
}
}
}

base = fileName.substring(fileName.length() - 1 - fileName.length() - lastI);
for (int intI = M00_Helper.lBound(suffixElems); intI <= M00_Helper.uBound(suffixElems); intI++) {
if ((suffixElems[intI] != "") &  (base.substring(base.length() - 1 - suffixElems[intI].length()) == (suffixElems[intI]))) {
base = base.substring(0, base.length() - suffixElems[intI].length());
intI = M00_Helper.uBound(suffixElems);
}
}

if (suffixDelimiter != "") {
intI = M00_Helper.inStr(base, suffixDelimiter);
if (intI > 0) {
base = base.substring(0, intI - 1);
}
}

returnValue = base;
return returnValue;
}


public static void addStrListElem(String strList, String elem) {
String[] list;

if (strList.trim() == "") {
strList = elem;
return;
}
if (elem.trim() == "") {
return;
}

list = strList.split(",");
int i;
for (int i = M00_Helper.lBound(list); i <= M00_Helper.uBound(list); i++) {
if (list[i].toUpperCase() == elem.toUpperCase()) {
return;
}
}

strList = strList + "," + elem;
}

public static Integer setSheetName(Workbook book, Sheet sheet, String name, boolean allowMerge, Boolean forceW) {
boolean force; 
if (forceW == null) {
force = true;
} else {
force = forceW;
}

Integer returnValue;
Sheet oldSheet;
// vbNo - add data to existing worksheet
// vbYes - delete old worksheet and replace by new sheet
// vbCancel - add data to new 'anonymous' worksheet

returnValue = vbNo;

//On Error GoTo setName 
oldSheet = book.getSheet(name);
//On Error Resume Next 

int rsp;
int options;
if (allowMerge) {
options = vbYesNoCancel;
} else {
options = vbOKCancel;
}

if (force) {
rsp = vbOK;
} else {
rsp = System.out.println(MsgBox: ( "Do you want to replace sheet '" & name & "'?" , vbCritical Or options , "Confirm delete of Worksheet" ) );
;
}

if (rsp == vbOK) {
rsp = vbYes;
}

if (rsp == vbNo) {
M00_Excel.deleteSheet(sheet);
M00_Excel.activateSheet(oldSheet);
return returnValue;
} else if (rsp == vbCancel) {
returnValue = vbCancel;
return returnValue;
}

M00_Excel.deleteSheet(oldSheet);
setName;

M00_Excel.renameSheet(sheet, name);
M00_Excel.activateSheet(sheet);
returnValue = vbYes;
return returnValue;
}


public static Boolean verifyWorksheet(String name, String tmplNameW) {
String tmplName; 
if (tmplNameW == null) {
tmplName = "";
} else {
tmplName = tmplNameW;
}

Boolean returnValue;
Sheet xlWs;
returnValue = true;

//On Error GoTo ErrorExit 
xlWs = M00_Excel.activeWorkbook.getSheet(name);

NormalExit:
return returnValue;

ErrorExit:
if (!(tmplName + "".trim().compareTo("") == 0)) {
M00_Excel.copySheet(M00_Excel.activeWorkbook.getSheet(tmplName));
M00_Excel.renameSheet(M00_Excel.activeWorkbook.getSheetAt(2), name);
} else {
xlWs = M00_Excel.activeWorkbook.createSheet();;
M00_Excel.renameSheet(xlWs, name);
;
;
}

returnValue = false;
return returnValue;
}


public static String paddRight(String str, Integer widthW, Boolean cutOffW) {
int width; 
if (widthW == null) {
width = 25;
} else {
width = widthW;
}

boolean cutOff; 
if (cutOffW == null) {
cutOff = false;
} else {
cutOff = cutOffW;
}

String returnValue;
if (!(cutOff &  str.length() >= width)) {
returnValue = str;
} else {
returnValue = str + "                                                                                                    ".substring(0, width);
}
return returnValue;
}


public static String addTab(Integer tabCountW) {
int tabCount; 
if (tabCountW == null) {
tabCount = 1;
} else {
tabCount = tabCountW;
}

String returnValue;
String result;
result = "";

int i;
for (i = 1; i <= 1; i += (1)) {
result = result + "  ";
}

returnValue = result;
return returnValue;
}


public static Boolean getBoolean( String str, String queryW) {
String query; 
if (queryW == null) {
query = "";
} else {
query = queryW;
}

Boolean returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
if (str.compareTo("?") == 0 &  query != "") {
returnValue = (System.out.println(MsgBox: ( query & "?" , vbYesNo Or vbQuestion Or vbDefaultButton2 ) );
 == vbYes);
return returnValue;
}

returnValue = (str.compareTo("X") == 0) |  (str.compareTo("1") == 0) | (str.compareTo("J") == 0) | (str.compareTo("Y") == 0);
return returnValue;
}


public static Integer getTvBoolean( String str, String queryW) {
String query; 
if (queryW == null) {
query = "";
} else {
query = queryW;
}

Integer returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
int rsp;
if (str.compareTo("?") == 0 &  query != "") {
rsp = (System.out.println(MsgBox: ( query & "?" , vbYesNoCancel Or vbQuestion Or vbDefaultButton2 ) );
 == vbYes);
if (rsp == vbYes) {
returnValue = M01_Common.TvBoolean.tvTrue;
} else if (rsp == vbNo) {
returnValue = M01_Common.TvBoolean.tvFalse;
} else {
returnValue = M01_Common.TvBoolean.tvNull;
}
return returnValue;
}

if ((str.compareTo("x") == 0) |  (str.compareTo("+") == 0) | (str.compareTo("J") == 0) | (str.compareTo("Y") == 0)) {
returnValue = M01_Common.TvBoolean.tvTrue;
} else if ((str.compareTo("-") == 0) |  (str.compareTo("N") == 0)) {
returnValue = M01_Common.TvBoolean.tvFalse;
} else {
returnValue = M01_Common.TvBoolean.tvNull;
}
return returnValue;
}


public static Integer getInteger(String str, Integer defaultValueW) {
int defaultValue; 
if (defaultValueW == null) {
defaultValue = -1;
} else {
defaultValue = defaultValueW;
}

Integer returnValue;
//On Error GoTo ErrorExit 
returnValue = new Double(str).intValue();

NormalExit:
return returnValue;

ErrorExit:
returnValue = defaultValue;
return returnValue;
}


public static Long getLong(String str, Long defaultValueW) {
long defaultValue; 
if (defaultValueW == null) {
defaultValue = -1;
} else {
defaultValue = defaultValueW;
}

Long returnValue;
//On Error GoTo ErrorExit 
returnValue = new Double(str).longValue();

NormalExit:
return returnValue;

ErrorExit:
returnValue = defaultValue;
return returnValue;
}

public static Double getSingle(String str, Double defaultValueW) {
double defaultValue; 
if (defaultValueW == null) {
defaultValue = -1;
} else {
defaultValue = defaultValueW;
}

Double returnValue;
//On Error GoTo ErrorExit 
returnValue = new Double(str);

NormalExit:
return returnValue;

ErrorExit:
returnValue = defaultValue;
return returnValue;
}


public static Integer getDbSpLogMode( String str) {
Integer returnValue;
str = str.toUpperCase();
returnValue = (str.compareTo("FILE") == 0 ? M01_Common.DbSpLogMode.esplFile : (str.compareTo("TABLE") == 0 ? M01_Common.DbSpLogMode.esplTable : M01_Common.DbSpLogMode.esplNone));
return returnValue;
}


public static Boolean getIsEntityFiltered( String str) {
Boolean returnValue;
if (str.compareTo("") == 0) {
returnValue = false;
} else {
returnValue = M00_Helper.inStr(1, "," + M03_Config.entityFilterKeys + ",", "," + str.trim() + ",") != 0;
}
return returnValue;
}


// ### IF IVK ###
public static Integer getDbUpdateMode(String str) {
Integer returnValue;
str = str.toUpperCase();

if (str.compareTo("") == 0) {
returnValue = M01_Common.DbUpdateMode.eupmAll;
return returnValue;
}

String[] list;
list = "".split(".");
list = str.split(".");
Integer result;
result = M01_Common.DbUpdateMode.eupmNone;

int i;
for (int i = M00_Helper.lBound(list); i <= M00_Helper.uBound(list); i++) {
if (list[i] == "X") {
result = M01_Common.DbUpdateMode.eupmNone;
} else if (list[i] == "I") {
result = result |  M01_Common.DbUpdateMode.eupmInsert;
} else if (list[i] == "U") {
result = result |  M01_Common.DbUpdateMode.eupmUpdate;
} else if (list[i] == "D") {
result = result |  M01_Common.DbUpdateMode.eupmDelete;
}
}
returnValue = result;
return returnValue;
}


// ### ENDIF IVK ###
public static Integer getFkMaintenanceMode( String str) {
Integer returnValue;
str = str.substring(0, 1).toUpperCase();

if (str.compareTo("C") == 0) {
returnValue = M23_Relationship_Utilities.FkMaintenanceMode.efkmCascade;
} else if (str.compareTo("") == 0) {
returnValue = M23_Relationship_Utilities.FkMaintenanceMode.efkmRestrict;
}
return returnValue;
}


public static String genTemplateParamWrapper(String str, Boolean forOidW) {
boolean forOid; 
if (forOidW == null) {
forOid = false;
} else {
forOid = forOidW;
}

String returnValue;
if (forOid) {
returnValue = "<<$" + (forOid ? "OID$" : "") + str + "$>>";
} else {
// ### IF IVK ###
returnValue = "<<mpcId>>";
// ### ELSE IVK ###
//   genTemplateParamWrapper = "<<orgId>>"
// ### ENDIF IVK ###
}
return returnValue;
}


public static String genOrgId( int thisOrgIndex, Integer ddlType, Boolean strippedW) {
boolean stripped; 
if (strippedW == null) {
stripped = false;
} else {
stripped = strippedW;
}

String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "0";
} else if (thisOrgIndex < 0) {
returnValue = "";
} else {
if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
if (stripped) {
returnValue = M04_Utilities.genTemplateParamWrapper(String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id), null);
} else {
returnValue = M04_Utilities.genTemplateParamWrapper(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).substring(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).length() - 1 - 2), null);
}
} else {
if (stripped) {
returnValue = String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id);
} else {
returnValue = new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).substring(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).length() - 1 - 2);
}
}
}
return returnValue;
}


public static String genOrgIdByIndex( int thisOrgIndex, Integer ddlType, Boolean strippedW) {
boolean stripped; 
if (strippedW == null) {
stripped = false;
} else {
stripped = strippedW;
}

String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "0";
} else if (thisOrgIndex < 0) {
returnValue = "";
} else {
if (M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate) {
if (stripped) {
returnValue = M04_Utilities.genTemplateParamWrapper(String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id), null);
} else {
returnValue = M04_Utilities.genTemplateParamWrapper(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).substring(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).length() - 1 - 2), null);
}
} else {
if (stripped) {
returnValue = String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id);
} else {
returnValue = new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).substring(new String ("00" + M71_Org.g_orgs.descriptors[thisOrgIndex].id).length() - 1 - 2);
}
}
}
return returnValue;
}


public static String genPoolId( int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "0";
} else {
if (thisPoolIndex < 1) {
returnValue = "";
} else {
returnValue = new String ("0" + M72_DataPool.g_pools.descriptors[thisPoolIndex].id).substring(new String ("0" + M72_DataPool.g_pools.descriptors[thisPoolIndex].id).length() - 1 - 1);
}
}
return returnValue;
}


public static String genPoolIdByIndex( int thisPoolIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "0";
} else {
if (thisPoolIndex < 0) {
returnValue = "";
} else {
returnValue = new String ("0" + M72_DataPool.g_pools.descriptors[thisPoolIndex].id).substring(new String ("0" + M72_DataPool.g_pools.descriptors[thisPoolIndex].id).length() - 1 - 1);
}
}
return returnValue;
}


public static String genSchemaName(String sectName, String sectNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

String returnValue;
returnValue = "X";

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = sectName.toUpperCase();
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
String thisOrgIdStr;
String thisPoolIdStr;
if (thisOrgIndex > 0) {
thisOrgIdStr = M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, null);
} else {
thisOrgIdStr = "";
}

if (thisPoolIndex > 0) {
thisPoolIdStr = M04_Utilities.genPoolIdByIndex(thisPoolIndex, ddlType);
} else {
thisPoolIdStr = "";
}


returnValue = M00_Helper.replace(M00_Helper.replace(M00_Helper.replace(M00_Helper.replace(M03_Config.pdmSchemaNamePattern, "<pk>", M03_Config.productKey), "<s>", sectNameShort.toUpperCase()), "<o>", thisOrgIdStr), "<p>", thisPoolIdStr);
}
return returnValue;
}


public static String getObjBaseName(String qualObjName, String delimiterW) {
String delimiter; 
if (delimiterW == null) {
delimiter = ".";
} else {
delimiter = delimiterW;
}

String returnValue;
long intI;
long pos;

if (qualObjName.compareTo("") == 0) {
returnValue = "";
return returnValue;
}

pos = 0;
for (intI = 1; intI <= 1; intI += (1)) {
if (qualObjName.substring(intI - 1, intI + 1 - 1) == delimiter) {
pos = intI;
break;
}
}

returnValue = qualObjName.substring(qualObjName.length() - 1 - qualObjName.length() - pos);
return returnValue;
}


public static String genEnumObjName(String entityName, Boolean forNlW) {
boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String returnValue;
String objName;

objName = entityName + "_ENUM";

if (forNl) {
objName = M04_Utilities.genNlObjName(objName, null, null, null);
}

returnValue = objName;
return returnValue;
}


public static String genNlObjName(String objName, String attributeNameW, Boolean forGenW, Boolean abbreviateW) {
String attributeName; 
if (attributeNameW == null) {
attributeName = "";
} else {
attributeName = attributeNameW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
if (abbreviate) {
returnValue = objName + (forGen ? "G" : "") + M01_LDM.tabPrefixNl + "T";
} else {
returnValue = objName + "_" + (forGen ? "GEN_" : "") + M01_LDM.tabPrefixNl + "_TEXT";
}
return returnValue;
}


public static String genNlObjShortName(String objName, String attributeNameW, Boolean forGenW, Boolean abbreviateW) {
String attributeName; 
if (attributeNameW == null) {
attributeName = "";
} else {
attributeName = attributeNameW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = objName + (forGen ? (abbreviate ? "G" : "GEN") : "") + (abbreviate ? "NL" : M01_LDM.tabPrefixNl + "TXT");
return returnValue;
}


public static String genQualObjName(int sectionIndex, String objName, String objNameShortW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
String objNameShort; 
if (objNameShortW == null) {
objNameShort = "";
} else {
objNameShort = objNameShortW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
final String delim = "_";
returnValue = "X.X";

if (forNl) {
returnValue = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[sectionIndex].sectionName, M20_Section.g_sections.descriptors[sectionIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex) + "." + prefix.toUpperCase() + ((!(prefix.compareTo("") == 0)) &  (delimMode &  M04_Utilities.ObjNameDelimMode.eondmPrefix) != M04_Utilities.ObjNameDelimMode.eondmNone ? delim : "") + (forNl ? M04_Utilities.genNlObjName(objName, null, forGen, abbreviate) : objName).toUpperCase() + (forGen & ! forNl ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortGen : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixGen) : "") + (forLrt ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortLrt : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixLrt) : "") + (forMqt ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortMqt : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixMqt) : "") + (!(suffix.compareTo("") == 0) &  (delimMode &  M04_Utilities.ObjNameDelimMode.eondmSuffix) != M04_Utilities.ObjNameDelimMode.eondmNone ? delim : "") + suffix.toUpperCase();
} else {
returnValue = M04_Utilities.genSchemaName(M20_Section.g_sections.descriptors[sectionIndex].sectionName, M20_Section.g_sections.descriptors[sectionIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex) + "." + prefix.toUpperCase() + ((!(prefix.compareTo("") == 0)) &  (delimMode &  M04_Utilities.ObjNameDelimMode.eondmPrefix) != M04_Utilities.ObjNameDelimMode.eondmNone ? delim : "") + objName.toUpperCase() + (forGen & ! forNl ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortGen : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixGen) : "") + (forLrt ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortLrt : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixLrt) : "") + (forMqt ? (abbreviate ? M01_LDM.gc_dbObjSuffixShortMqt : (delimMode &  M04_Utilities.ObjNameDelimMode.eondmInfix ? delim : "") + M01_LDM.gc_dbObjSuffixMqt) : "") + (!(suffix.compareTo("") == 0) &  (delimMode &  M04_Utilities.ObjNameDelimMode.eondmSuffix) != M04_Utilities.ObjNameDelimMode.eondmNone ? delim : "") + suffix.toUpperCase();
}
return returnValue;
}


public static String genQualIndexName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forNlW, Boolean forMqtW, String suffixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

String returnValue;
String infix;
infix = (forMqt ? "M" : "") + (forLrt ? "L" : "") + (forGen ? "G" : "") + (forNl ? "N" : "");

if (objNameShort.toUpperCase().substring(0, 4) == "IDX_") {
returnValue = M04_Utilities.genQualObjName(M20_Section.g_sections.descriptors[sectionIndex].sectionIndex, objNameShort + infix, objNameShort + infix, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, suffix, null, null);
} else {
returnValue = M04_Utilities.genQualObjName(M20_Section.g_sections.descriptors[sectionIndex].sectionIndex, "IDX_" + objNameShort + infix, "IDX_" + objNameShort + infix, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, suffix, null, null);
}
return returnValue;
}


public static String genQualTabName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(sectionIndex, objName, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, null, null);
return returnValue;
}


public static String genQualObjNameByClassIndex(int classIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Boolean useOrParentW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, Integer delimModeW, Boolean abbreviateW, String extraInfixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean useOrParent; 
if (useOrParentW == null) {
useOrParent = false;
} else {
useOrParent = useOrParentW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String extraInfix; 
if (extraInfixW == null) {
extraInfix = "";
} else {
extraInfix = extraInfixW;
}

String returnValue;
//On Error GoTo ErrorExit 

int effectiveClassIndex;
effectiveClassIndex = classIndex;
if (useOrParent) {
effectiveClassIndex = M22_Class.g_classes.descriptors[classIndex].orMappingSuperClassIndex;
}

int effectiveSectionIndex;
effectiveSectionIndex = (inLrtAliasSchema ? M01_Globals.g_sectionIndexAliasLrt : M22_Class.g_classes.descriptors[effectiveClassIndex].sectionIndex);

int thisPoolId;
int thisOrgId;

boolean commonItemsLocal;
if (thisPoolIndex <= 0) {
thisPoolId = -1;
commonItemsLocal = false;
} else {
commonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
}

if (thisOrgId <= 0) {
thisOrgId = -1;
thisPoolId = -1;
}

int effectiveOrgIndex;
int effectivePoolIndex;
effectiveOrgIndex = M71_Org_Utilities.getEffectiveOrgId(thisOrgIndex, M22_Class.g_classes.descriptors[effectiveClassIndex].isCommonToOrgs & ! forcePoolParams & !commonItemsLocal);
effectivePoolIndex = M72_DataPool_Utilities.getEffectivePoolId(thisPoolIndex, (effectiveOrgIndex == -1 |  M22_Class.g_classes.descriptors[effectiveClassIndex].isCommonToPools) & ! forcePoolParams & !commonItemsLocal);

returnValue = M04_Utilities.genQualObjName(effectiveSectionIndex, M22_Class.g_classes.descriptors[effectiveClassIndex].className + extraInfix, M22_Class.g_classes.descriptors[effectiveClassIndex].shortName + extraInfix, ddlType, effectiveOrgIndex, effectivePoolIndex, forGen, forLrt |  (forMqt &  M22_Class.g_classes.descriptors[effectiveClassIndex].useMqtToImplementLrt), forMqt &  M22_Class.g_classes.descriptors[effectiveClassIndex].useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate);

NormalExit:
return returnValue;

ErrorExit:
errMsgBox(Err.description);
return returnValue;
}


public static String genQualTabNameByClassIndex(int classIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, Boolean useOrParentW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean useOrParent; 
if (useOrParentW == null) {
useOrParent = false;
} else {
useOrParent = useOrParentW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, null, null, useOrParent, inLrtAliasSchema, forcePoolParams, null, null, null);
return returnValue;
}


public static String genQualNlTabNameByClassIndex(int classIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, Boolean useOrParentW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean useOrParent; 
if (useOrParentW == null) {
useOrParent = false;
} else {
useOrParent = useOrParentW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, true, null, null, useOrParent, inLrtAliasSchema, forcePoolParams, null, null, null);
return returnValue;
}


public static String genQualObjNameByRelIndex(int relIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, Integer delimModeW, Boolean abbreviateW, String extraInfixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String extraInfix; 
if (extraInfixW == null) {
extraInfix = "";
} else {
extraInfix = extraInfixW;
}

String returnValue;
//On Error GoTo ErrorExit 

int effectiveSectionIndex;
effectiveSectionIndex = (inLrtAliasSchema ? M01_Globals.g_sectionIndexAliasLrt : M23_Relationship.g_relationships.descriptors[relIndex].sectionIndex);

boolean commonItemsLocal;
if (thisPoolIndex > 0) {
commonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
} else {
commonItemsLocal = false;
}

if (thisOrgIndex <= 0) {
thisPoolIndex = -1;
}

int effectiveOrgIndex;
int effectivePoolIndex;
effectiveOrgIndex = M71_Org_Utilities.getEffectiveOrgIndex(thisOrgIndex, M23_Relationship.g_relationships.descriptors[relIndex].isCommonToOrgs & ! forcePoolParams & !commonItemsLocal);
effectivePoolIndex = M72_DataPool_Utilities.getEffectivePoolIndex(thisPoolIndex, (effectiveOrgIndex == -1 |  M23_Relationship.g_relationships.descriptors[relIndex].isCommonToPools) & ! forcePoolParams & !commonItemsLocal);

returnValue = M04_Utilities.genQualObjName(effectiveSectionIndex, M23_Relationship.g_relationships.descriptors[relIndex].relName + extraInfix, M23_Relationship.g_relationships.descriptors[relIndex].shortName + extraInfix, ddlType, effectiveOrgIndex, effectivePoolIndex, false, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate);

NormalExit:
return returnValue;

ErrorExit:
errMsgBox(Err.description);
return returnValue;
}


public static String genQualTabNameByRelIndex(int relIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, String prefixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjNameByRelIndex(relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, null, inLrtAliasSchema, forcePoolParams, M04_Utilities.ObjNameDelimMode.eondmInfix |  M04_Utilities.ObjNameDelimMode.eondmSuffix, null, null);
return returnValue;
}

public static String genQualObjNameByEnumIndex(int enumIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forNlW, String prefixW, String suffixW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, Integer delimModeW, Boolean abbreviateW, String extraInfixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String extraInfix; 
if (extraInfixW == null) {
extraInfix = "";
} else {
extraInfix = extraInfixW;
}

String returnValue;
//On Error GoTo ErrorExit 

int effectiveSectionIndex;
effectiveSectionIndex = (inLrtAliasSchema ? M01_Globals.g_sectionIndexAliasLrt : M21_Enum.g_enums.descriptors[enumIndex].sectionIndex);

boolean commonItemsLocal;
if (thisPoolIndex > 0) {
commonItemsLocal = M72_DataPool.g_pools.descriptors[thisPoolIndex].commonItemsLocal;
} else {
commonItemsLocal = false;
}

if (thisOrgIndex <= 0) {
thisPoolIndex = -1;
}

int effectiveOrgIndex;
int effectivePoolIndex;
effectiveOrgIndex = M71_Org_Utilities.getEffectiveOrgId(thisOrgIndex, M21_Enum.g_enums.descriptors[enumIndex].isCommonToOrgs & ! forcePoolParams & !commonItemsLocal);
effectivePoolIndex = M72_DataPool_Utilities.getEffectivePoolId(thisPoolIndex, (effectiveOrgIndex == -1 |  M21_Enum.g_enums.descriptors[enumIndex].isCommonToPools) & ! forcePoolParams & !commonItemsLocal);

returnValue = M04_Utilities.genQualObjName(effectiveSectionIndex, M04_Utilities.genEnumObjName(M21_Enum.g_enums.descriptors[enumIndex].enumName, null) + extraInfix, M04_Utilities.genEnumObjName(M21_Enum.g_enums.descriptors[enumIndex].shortName, null) + extraInfix, ddlType, effectiveOrgIndex, effectivePoolIndex, false, false, false, forNl, prefix, suffix, delimMode, abbreviate);

NormalExit:
return returnValue;

ErrorExit:
errMsgBox(Err.description);
return returnValue;
}


public static String genQualTabNameByEnumIndex(int enumIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forNlW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjNameByEnumIndex(enumIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, null, null, inLrtAliasSchema, forcePoolParams, null, null, null);
return returnValue;
}


public static String genQualObjNameByEntityIndex( int acmEntityIndex,  Integer acmEntityType, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, Boolean useOrParentW, Integer delimModeW, Boolean abbreviateW, String extraInfixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

boolean useOrParent; 
if (useOrParentW == null) {
useOrParent = false;
} else {
useOrParent = useOrParentW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String extraInfix; 
if (extraInfixW == null) {
extraInfix = "";
} else {
extraInfix = extraInfixW;
}

String returnValue;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M04_Utilities.genQualObjNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, useOrParent, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M04_Utilities.genQualObjNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
returnValue = M04_Utilities.genQualObjNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix);
}
return returnValue;
}


public static String genQualTabNameByEntityIndex( int acmEntityIndex,  Integer acmEntityType, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, Boolean inLrtAliasSchemaW, Boolean forcePoolParamsW, Boolean useOrParentW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

boolean inLrtAliasSchema; 
if (inLrtAliasSchemaW == null) {
inLrtAliasSchema = false;
} else {
inLrtAliasSchema = inLrtAliasSchemaW;
}

boolean forcePoolParams; 
if (forcePoolParamsW == null) {
forcePoolParams = false;
} else {
forcePoolParams = forcePoolParamsW;
}

boolean useOrParent; 
if (useOrParentW == null) {
useOrParent = false;
} else {
useOrParent = useOrParentW;
}

String returnValue;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, useOrParent, inLrtAliasSchema, forcePoolParams);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M04_Utilities.genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, inLrtAliasSchema, forcePoolParams, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
returnValue = M04_Utilities.genQualTabNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, inLrtAliasSchema, forcePoolParams);
}
return returnValue;
}


public static String genQualViewName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(sectionIndex, objName, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, (prefix.compareTo("") == 0 ? "V" : "V_") + prefix, suffix, delimMode, abbreviate);
return returnValue;
}


public static String genQualViewNameByClassIndex(int classIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualViewName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional, forMqt &  M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


public static String genQualViewNameByRelIndex(int relIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualViewName(M23_Relationship.g_relationships.descriptors[relIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[relIndex].relName, M23_Relationship.g_relationships.descriptors[relIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt, forMqt &  M23_Relationship.g_relationships.descriptors[relIndex].useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


public static String genQualViewNameByEnumIndex(int enumIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualViewName(M21_Enum.g_enums.descriptors[enumIndex].sectionIndex, M04_Utilities.genEnumObjName(M21_Enum.g_enums.descriptors[enumIndex].enumName, null), M04_Utilities.genEnumObjName(M21_Enum.g_enums.descriptors[enumIndex].shortName, null), ddlType, thisOrgIndex, thisPoolIndex, false, false, false, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


public static String genQualViewNameByEntityIndex( int acmEntityIndex,  Integer acmEntityType, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M04_Utilities.genQualViewNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M04_Utilities.genQualViewNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
returnValue = M04_Utilities.genQualViewNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, delimMode, abbreviate);
}
return returnValue;
}


public static String genQualProcName(int sectionIndex, String procName, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(sectionIndex, procName, null, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, prefix, suffix, delimMode, null);
return returnValue;
}


public static String genQualProcNameByEntityIndex(int acmEntityIndex, Integer acmEntityType, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW, String extraInfixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String extraInfix; 
if (extraInfixW == null) {
extraInfix = "";
} else {
extraInfix = extraInfixW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, null, null, null, delimMode, abbreviate, extraInfix);
return returnValue;
}


public static String genQualFuncName(int sectionIndex, String objName, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, String suffixW, Boolean noPrefixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean noPrefix; 
if (noPrefixW == null) {
noPrefix = false;
} else {
noPrefix = noPrefixW;
}

String returnValue;
if (noPrefix) {
returnValue = M04_Utilities.genQualObjName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, suffix, null, null);
} else {
returnValue = M04_Utilities.genQualObjName(sectionIndex, "F_" + objName, "F_" + objName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, suffix, null, null);
}
return returnValue;
}


public static String genQualTriggerName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmSuffix;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = true;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(sectionIndex, objNameShort, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, "TR_" + prefix, suffix, delimMode, abbreviate);
return returnValue;
}


// ### IF IVK ###
public static String genQualTriggerNameByClassIndex(int classIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW,  Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmNone;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = true;
} else {
abbreviate = abbreviateW;
}

String returnValue;
// ### ELSE IVK ###
//Function genQualTriggerNameByClassIndex( _
// ByRef classIndex As Integer, _
// Optional ByRef ddlType As DdlTypeId = edtLdm, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional forNl As Boolean = False, _
// Optional ByRef prefix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional delimMode As ObjNameDelimMode = eondmSuffix, _
// Optional abbreviate As Boolean = True _
//) As String
// ### ENDIF IVK ###
returnValue = M04_Utilities.genQualTriggerName(M22_Class.g_classes.descriptors[classIndex].sectionIndex, M22_Class.g_classes.descriptors[classIndex].className, M22_Class.g_classes.descriptors[classIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt &  M22_Class.g_classes.descriptors[classIndex].isUserTransactional, forMqt &  M22_Class.g_classes.descriptors[classIndex].useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


// ### IF IVK ###
public static String genQualTriggerNameByRelIndex(int relIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW,  Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmNone;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = true;
} else {
abbreviate = abbreviateW;
}

String returnValue;
// ### ELSE IVK ###
//Function genQualTriggerNameByRelIndex( _
// ByRef relIndex As Integer, _
// Optional ByRef ddlType As DdlTypeId = edtLdm, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional forNl As Boolean = False, _
// Optional ByRef prefix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional delimMode As ObjNameDelimMode = eondmSuffix, _
// Optional abbreviate As Boolean = True _
//) As String
// ### ENDIF IVK ###
returnValue = M04_Utilities.genQualTriggerName(M23_Relationship.g_relationships.descriptors[relIndex].sectionIndex, M23_Relationship.g_relationships.descriptors[relIndex].relName, M23_Relationship.g_relationships.descriptors[relIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, forLrt &  M23_Relationship.g_relationships.descriptors[relIndex].isUserTransactional, forMqt &  M23_Relationship.g_relationships.descriptors[relIndex].useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


// ### IF IVK ###
public static String genQualTriggerNameByEnumIndex(int enumIndex, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forNlW, String prefixW, String suffixW,  Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmNone;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = true;
} else {
abbreviate = abbreviateW;
}

String returnValue;
// ### ELSE IVK ###
//Function genQualTriggerNameByEnumIndex( _
// ByRef enumIndex As Integer, _
// Optional ByRef ddlType As DdlTypeId = edtLdm, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional forNl As Boolean = False, _
// Optional ByRef prefix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional delimMode As ObjNameDelimMode = eondmSuffix, _
// Optional abbreviate As Boolean = True _
//) As String
// ### ENDIF IVK ###
returnValue = M04_Utilities.genQualTriggerName(M21_Enum.g_enums.descriptors[enumIndex].sectionIndex, M21_Enum.g_enums.descriptors[enumIndex].enumName, M21_Enum.g_enums.descriptors[enumIndex].shortName, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, forNl, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


// ### IF IVK ###
public static String genQualTriggerNameByEntityIndex( int acmEntityIndex,  Integer acmEntityType, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW, Boolean forNlW, String prefixW, String suffixW,  Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmNone;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = true;
} else {
abbreviate = abbreviateW;
}

String returnValue;
// ### ELSE IVK ###
//Function genQualTriggerNameByEntityIndex( _
// ByVal acmEntityIndex As Integer, _
// ByVal acmEntityType As AcmAttrContainerType, _
// Optional ByRef ddlType As DdlTypeId = edtLdm, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional forMqt As Boolean = False, _
// Optional forNl As Boolean = False, _
// Optional ByRef prefix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional ByVal delimMode As ObjNameDelimMode = eondmSuffix, _
// Optional abbreviate As Boolean = True _
//) As String
// ### ENDIF IVK ###
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M04_Utilities.genQualTriggerNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M04_Utilities.genQualTriggerNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
returnValue = M04_Utilities.genQualTriggerNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, delimMode, abbreviate);
}
return returnValue;
}


public static String genQualSeqName(int sectionIndex, String seqName, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String prefixW, String suffixW, Integer delimModeW, Boolean abbreviateW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

String prefix; 
if (prefixW == null) {
prefix = "";
} else {
prefix = prefixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer delimMode; 
if (delimModeW == null) {
delimMode = M04_Utilities.ObjNameDelimMode.eondmAll;
} else {
delimMode = delimModeW;
}

boolean abbreviate; 
if (abbreviateW == null) {
abbreviate = false;
} else {
abbreviate = abbreviateW;
}

String returnValue;
returnValue = M04_Utilities.genQualObjName(sectionIndex, seqName, seqName, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, prefix, suffix, delimMode, abbreviate);
return returnValue;
}


// ### IF IVK ###
public static String genQualAliasName(String objName, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer aliasTypeW, Boolean forGenW, Boolean forLrtW, String suffixW, Boolean forLrtSchemaW, Boolean forDeletedObjectsW, Boolean forPsDpFilterW, Boolean forPsDpFilterExtendedW, Boolean suppressGenSuffixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

Integer aliasType; 
if (aliasTypeW == null) {
aliasType = null;
} else {
aliasType = aliasTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

boolean forLrtSchema; 
if (forLrtSchemaW == null) {
forLrtSchema = false;
} else {
forLrtSchema = forLrtSchemaW;
}

boolean forDeletedObjects; 
if (forDeletedObjectsW == null) {
forDeletedObjects = false;
} else {
forDeletedObjects = forDeletedObjectsW;
}

boolean forPsDpFilter; 
if (forPsDpFilterW == null) {
forPsDpFilter = false;
} else {
forPsDpFilter = forPsDpFilterW;
}

boolean forPsDpFilterExtended; 
if (forPsDpFilterExtendedW == null) {
forPsDpFilterExtended = false;
} else {
forPsDpFilterExtended = forPsDpFilterExtendedW;
}

boolean suppressGenSuffix; 
if (suppressGenSuffixW == null) {
suppressGenSuffix = false;
} else {
suppressGenSuffix = suppressGenSuffixW;
}

String returnValue;
// ### ELSE IVK ###
//Function genQualAliasName( _
// ByRef objName As String, _
// ByRef objNameShort As String, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional aliasType As DbAliasEntityType, _
// Optional forGen As Boolean = False, _
// Optional forLrt As Boolean = False, _
// Optional byref suffix As String = "", _
// Optional forLrtSchema As Boolean = False, _
// Optional suppressGenSuffix As Boolean = False _
//) As String
// ### ENDIF IVK ###
returnValue = "";

String result;
int sectionIndex;
// ### IF IVK ###
if (forPsDpFilter) {
sectionIndex = M01_Globals.g_sectionIndexAliasPsDpFiltered;
} else if (forPsDpFilterExtended) {
sectionIndex = M01_Globals.g_sectionIndexAliasPsDpFilteredExtended;
} else if (forDeletedObjects) {
sectionIndex = M01_Globals.g_sectionindexAliasDelObj;
} else if (forLrtSchema) {
// ### ELSE IVK ###
// If forLrtSchema Then
// ### ENDIF IVK ###
sectionIndex = M01_Globals.g_sectionIndexAliasLrt;
} else {
sectionIndex = M01_Globals.g_sectionIndexAlias;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
if (aliasType == M01_Common.DbAliasEntityType.edatTable) {
result = M04_Utilities.genQualTabName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! suppressGenSuffix, forLrt & ! forLrtSchema, null, null, null, suffix);
} else if (aliasType == M01_Common.DbAliasEntityType.edatView) {
result = M04_Utilities.genQualViewName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, forGen & ! suppressGenSuffix, forLrt & ! forLrtSchema, null, null, null, suffix, null, null);
}
}

if (M00_Helper.inStr(1, result, "NL_TEXT_GEN") != 0) {
// FixMe: Hack!! cleanup concepts for generating table names!
returnValue = M00_Helper.replace(result, "_NL_TEXT_GEN", "_GEN_NL_TEXT", 1);
} else {
returnValue = result;
}
return returnValue;
}


public static String genBufferPoolNameByIndex(int thisBufPoolIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

String returnValue;
returnValue = M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolName.substring(0, M01_LDM.gc_dbMaxBufferPoolNameLength - 1).toUpperCase();

if (M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].isCommonToOrgs) {
return returnValue;
}

String thisOrgIdString;
thisOrgIdString = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);
if ((M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].isCommonToPools |  thisPoolIndex <= 0) &  thisOrgIndex > 0) {
returnValue = M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolName.substring(0, M01_LDM.gc_dbMaxBufferPoolNameLength - 1 - thisOrgIdString.length()) + thisOrgIdString.toUpperCase();
return returnValue;
}

String thisPoolIdString;
thisPoolIdString = M04_Utilities.genPoolId(thisPoolIndex, ddlType);
if (thisOrgIndex > 0 &  thisPoolIndex > 0) {
returnValue = M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolName.substring(0, M01_LDM.gc_dbMaxBufferPoolNameLength - 1 - thisOrgIdString.length() - thisPoolIdString.length()) + thisOrgIdString + thisPoolIdString.toUpperCase();
}
return returnValue;
}


public static String genTablespaceNameByIndex(int thisTabSpaceIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

String returnValue;
returnValue = M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.substring(0, M01_LDM.gc_dbMaxTablespaceNameLength - 1).toUpperCase();

if (M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].isCommonToOrgs) {
return returnValue;
}

String thisOrgIdString;
thisOrgIdString = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);
if ((M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].isCommonToPools |  thisPoolIndex <= 0) &  thisOrgIndex > 0) {
returnValue = M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.substring(0, M01_LDM.gc_dbMaxTablespaceNameLength - 1 - thisOrgIdString.length()) + thisOrgIdString.toUpperCase();
return returnValue;
}

String thisPoolIdString;
thisPoolIdString = M04_Utilities.genPoolId(thisPoolIndex, ddlType);
if (thisOrgIndex > 0 &  thisPoolIndex > 0) {
returnValue = M73_TableSpace.g_tableSpaces.descriptors[thisTabSpaceIndex].tableSpaceName.substring(0, M01_LDM.gc_dbMaxTablespaceNameLength - 1 - thisOrgIdString.length() - thisPoolIdString.length()) + thisOrgIdString + thisPoolIdString.toUpperCase();
}
return returnValue;
}


public static String genContainerNameByIndex(int thisContainerIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer ddlTypeW) {
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

String returnValue;
returnValue = M74_Container.g_containers.descriptors[thisContainerIndex].containerName;

if (M74_Container.g_containers.descriptors[thisContainerIndex].isCommonToOrgs) {
return returnValue;
}

String thisOrgIdString;
thisOrgIdString = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null);
if ((M74_Container.g_containers.descriptors[thisContainerIndex].isCommonToPools |  thisPoolIndex <= 0) &  thisOrgIndex > 0) {
returnValue = M74_Container.g_containers.descriptors[thisContainerIndex].containerName + thisOrgIdString;
return returnValue;
}

String thisPoolIdString;
thisPoolIdString = M04_Utilities.genPoolId(thisPoolIndex, ddlType);
if (thisOrgIndex > 0 &  thisPoolIndex > 0) {
returnValue = M74_Container.g_containers.descriptors[thisContainerIndex].containerName + thisOrgIdString + thisPoolIdString;
}
return returnValue;
}


public static String genGetUserNameByIdDdl(String cdUserId, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

String returnValue;
if (M00_Helper.inStr(cdUserId.toUpperCase(), "CREATE") != 0) {
returnValue = "COALESCE((SELECT U." + M01_Globals.g_anUserName + " FROM " + M01_Globals.g_qualTabNameUser + " U WHERE U." + M01_Globals.g_anUserId + " = " + cdUserId + "), '[' || " + cdUserId + " || ']')";
} else {
returnValue = "COALESCE((SELECT U." + M01_Globals.g_anUserName + " FROM " + M01_Globals.g_qualTabNameUser + " U WHERE U." + M01_Globals.g_anUserId + " = " + cdUserId + "), (CASE WHEN " + cdUserId + " = '' THEN '' ELSE '[' || " + cdUserId + " || ']' END))";
}
return returnValue;
}


// ### IF IVK ###
public static String mapExpression( String expression,  int thisOrgIndex,  int thisPoolIndex, Integer ddlType, String tabQualifier1W, String tabQualifier2W, String lrtOidRefW) {
String tabQualifier1; 
if (tabQualifier1W == null) {
tabQualifier1 = "";
} else {
tabQualifier1 = tabQualifier1W;
}

String tabQualifier2; 
if (tabQualifier2W == null) {
tabQualifier2 = "";
} else {
tabQualifier2 = tabQualifier2W;
}

String lrtOidRef; 
if (lrtOidRefW == null) {
lrtOidRef = "";
} else {
lrtOidRef = lrtOidRefW;
}

String returnValue;
//On Error GoTo ErrorExit 

if (expression.substring(0, 1) == "#") {
expression = expression.substring(expression.length() - 1 - expression.length() - 1);
}

expression = M00_Helper.replace(expression, ";", ",");
returnValue = expression;

// map section parameters
int sPosStart;
sPosStart = M00_Helper.inStr(1, expression, "<%S");
while (sPosStart > 0) {
int sPosEnd;
String sectionExpression;
String schemaName;
sPosEnd = M00_Helper.inStr(sPosStart + 2, expression, ">");

sectionExpression = expression.substring(sPosStart + 1 - 1, sPosStart + 1 + new Double(sPosEnd - sPosStart - 1).longValue() - 1);

if (sectionExpression.substring(0, 3) != "%S(" |  sectionExpression.substring(sectionExpression.length() - 1 - 1) != ")") {
goto SyntaxError;
}

boolean isCtoSchema;
boolean isCtpSchema;

isCtoSchema = false;
isCtpSchema = false;
schemaName = sectionExpression.substring(4 - 1, 4 + sectionExpression.length() - 4 - 1);
if (schemaName.substring(0, 5) == "[cto]") {
isCtoSchema = true;
isCtpSchema = true;
schemaName = schemaName.substring(schemaName.length() - 1 - schemaName.length() - 5);
}
if (schemaName.substring(0, 5) == "[ctp]") {
isCtpSchema = true;
schemaName = schemaName.substring(schemaName.length() - 1 - schemaName.length() - 5);
}
schemaName = M04_Utilities.genSchemaName(schemaName, M20_Section.getSectionShortNameByName(schemaName), ddlType, (isCtoSchema ? -1 : thisOrgIndex), (isCtpSchema ? -1 : thisPoolIndex));
expression = expression.substring(0, sPosStart - 1) + schemaName + "." + expression.substring(sPosEnd + 1 - 1, sPosEnd + 1 + expression.length() - 1);

sPosStart = M00_Helper.inStr(1, expression, "<%S");
}

// map tab qualifiers
int tPos;
tPos = M00_Helper.inStr(1, expression, "<%T>");
while (tPos > 0) {
expression = expression.substring(0, tPos - 1) + tabQualifier1 + (tabQualifier1 == "" |  tabQualifier1.substring(tabQualifier1.length() - 1 - 1) == "." ? "" : ".") + expression.substring(tPos + 4 - 1, tPos + 4 + expression.length() - 1);
tPos = M00_Helper.inStr(1, expression, "<%T>");
}
if (tabQualifier2 != "") {
tPos = M00_Helper.inStr(1, expression, "<%T2>");
while (tPos > 0) {
expression = expression.substring(0, tPos - 1) + tabQualifier1 + (tabQualifier2.substring(tabQualifier2.length() - 1 - 1) == "." ? "" : ".") + expression.substring(tPos + 5 - 1, tPos + 5 + expression.length() - 1);
tPos = M00_Helper.inStr(1, expression, "<%T2>");
}
}

// map lrtOid reference
int lPos;
lPos = M00_Helper.inStr(1, expression, "<,%L>");
while (lPos > 0) {
expression = expression.substring(0, lPos - 1) + (lrtOidRef.compareTo("") == 0 ? "" : "," + lrtOidRef) + expression.substring(lPos + 5 - 1, lPos + 5 + expression.length() - 1);
lPos = M00_Helper.inStr(1, expression, "<,%L>");
}
lPos = M00_Helper.inStr(1, expression, "<, %L>");
while (lPos > 0) {
expression = expression.substring(0, lPos - 1) + (lrtOidRef.compareTo("") == 0 ? "" : ", " + lrtOidRef) + expression.substring(lPos + 6 - 1, lPos + 6 + expression.length() - 1);
lPos = M00_Helper.inStr(1, expression, "<, %L>");
}

returnValue = expression;

NormalExit:
return returnValue;

SyntaxError:
M04_Utilities.logMsg("Syntax Error in attribute mapping expression \"" + expression + "\"", M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
goto NormalExit;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


// ### ENDIF IVK ###
// ### IF IVK ###
public static String transformAttrName(String db2AttrName,  Integer valueType, int valueTypeIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer ddlTypeW, Boolean useDomainW, String infixW, Boolean transformToConstantW, Boolean isVirtualW, Integer attrIndexW,  Integer outputModeW, Boolean isNullableW,  Boolean persistedW, Boolean forReadW, Integer attrCatW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean useDomain; 
if (useDomainW == null) {
useDomain = true;
} else {
useDomain = useDomainW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

boolean transformToConstant; 
if (transformToConstantW == null) {
transformToConstant = false;
} else {
transformToConstant = transformToConstantW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomList;
} else {
outputMode = outputModeW;
}

boolean isNullable; 
if (isNullableW == null) {
isNullable = true;
} else {
isNullable = isNullableW;
}

boolean persisted; 
if (persistedW == null) {
persisted = true;
} else {
persisted = persistedW;
}

boolean forRead; 
if (forReadW == null) {
forRead = false;
} else {
forRead = forReadW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

String returnValue;
// ### ELSE IVK ###
//Function transformAttrName( _
// ByRef db2AttrName As String, _
// ByVal valueType as AttrValueType, _
// ByRef valueTypeIndex As Integer, _
// ByRef transformation As AttributeListTransformation, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional useDomain As Boolean = True, _
// Optional byref infix As String = "", _
// Optional transformToConstant As Boolean = False, _
// Optional attrIndex As Integer = -1, _
// Optional ByVal outputMode As DdlOutputMode = edomList, _
// Optional isNullable As Boolean = True, _
// Optional forRead As Boolean = False, _
// Optional attrCat As AttrCategory = eacRegular _
//) As String
// ### ENDIF IVK ###

int i;
String name;
transformToConstant = false;
returnValue = "";

int effectiveDomainIndex;
if (valueTypeIndex > 0) {
if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomain) {
effectiveDomainIndex = valueTypeIndex;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
db2AttrName = db2AttrName + M01_Globals.gc_enumAttrNameSuffix.substring(0, M01_LDM.gc_dbMaxAttributeNameLength);
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexValue;
}
}

// ### IF IVK ###
if (isVirtual & ! persisted & (outputMode &  M01_Common.DdlOutputMode.edomVirtualPersisted)) {
return returnValue;
}

if (((outputMode &  M01_Common.DdlOutputMode.edomListVirtual != 0) &  (outputMode &  M01_Common.DdlOutputMode.edomValueVirtualNonPersisted != 0))) {
// two alternative options specified - check if this attribute is persisted
if (!(persisted)) {
outputMode = outputMode &  (!(M01_Common.DdlOutputMode.edomListVirtual));
outputMode = outputMode |  M01_Common.DdlOutputMode.edomValueVirtual;
}
}

if ((isVirtual &  transformation.doCollectVirtualDomainDescriptors) |  (!(isVirtual &  transformation.doCollectDomainDescriptors))) {
M25_Domain_Utilities.addDomainDescriptorRef(transformation.domainRefs, effectiveDomainIndex, isNullable, transformation.distinguishNullabilityForDomainRefs);
}
if (isVirtual & ! persisted & ((outputMode &  M01_Common.DdlOutputMode.edomListVirtual) != 0) & ((outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) != 0)) {
outputMode = outputMode &  (!(M01_Common.DdlOutputMode.edomListVirtual));
}

// ### ENDIF IVK ###
for (i = 1; i <= 1; i += (1)) {
if ((!(transformation.mappings[i].attributeName.compareTo("") == 0) ? transformation.mappings[i].attributeName.toUpperCase() == db2AttrName.toUpperCase() : !(transformation.mappings[i].domainSection.compareTo("") == 0)) &  (useDomain ? (transformation.mappings[i].domainSection.compareTo("") == 0 |  transformation.mappings[i].domainSection.toUpperCase() == M25_Domain.g_domains.descriptors[effectiveDomainIndex].sectionName.toUpperCase()) &  (transformation.mappings[i].domainName.compareTo("") == 0 |  transformation.mappings[i].domainName.toUpperCase() == M25_Domain.g_domains.descriptors[effectiveDomainIndex].domainName.toUpperCase()) : true)) {
name = transformation.mappings[i].value;
transformToConstant = transformation.mappings[i].isConstant;
if (!(transformation.postProcessAfterMapping)) {
returnValue = name;
return returnValue;
}
}
}

// ### IF IVK ###
if ((outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) &  name.compareTo("") == 0 & isVirtual & attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.isRelBasedMapping) {
// FixMe: implement this ...
} else {
if ((outputMode &  M01_Common.DdlOutputMode.edomVirtualPersisted) &  M24_Attribute.g_attributes.descriptors[attrIndex].isPersistent) {
// handle as regular attribute
} else {
if (forRead &  !(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsToForRead.description.compareTo("") == 0)) {
returnValue = M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsToForRead.mapTo, transformation.M01_ACM.conEnumLabelText.orgIndex, transformation.M01_ACM.conEnumLabelText.poolIndex, ddlType, transformation.M01_ACM.conEnumLabelText.tabQualifier, null, transformation.M01_ACM.conEnumLabelText.lrtOidRef);
} else {
returnValue = M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.mapTo, transformation.M01_ACM.conEnumLabelText.orgIndex, transformation.M01_ACM.conEnumLabelText.poolIndex, ddlType, transformation.M01_ACM.conEnumLabelText.tabQualifier, null, transformation.M01_ACM.conEnumLabelText.lrtOidRef);
}
transformToConstant = true;
return returnValue;
}
}
}

if (((outputMode &  M01_Common.DdlOutputMode.edomXml) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomExpressionRef) == 0) & (attrCat &  M01_Common.AttrCategory.eacFkOidExpression)) {
return returnValue;
}

if ((outputMode &  (M01_Common.DdlOutputMode.edomValueExpression |  M01_Common.DdlOutputMode.edomXml)) &  attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].isExpression) {
if ((outputMode &  M01_Common.DdlOutputMode.edomExpression) == 0) {
if ((outputMode &  (!(M01_Common.DdlOutputMode.edomDecl)) & M01_Common.DdlOutputMode.edomExpressionDummy) == 0) {
// we accept edomExpressionDummy only in 'List' or 'Value'-mode
return returnValue;
}
}

if ((outputMode &  M01_Common.DdlOutputMode.edomList) &  ((outputMode &  M01_Common.DdlOutputMode.edomValue) == 0)) {
returnValue = transformation.attributePrefix + db2AttrName + transformation.attributePostfix;
return returnValue;
}
String qualTabNameExpression;
String qualTabNameExpressionLrt;
if (attrCat &  M01_Common.AttrCategory.eacNationalBool) {
String attrNameBase;
attrNameBase = M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, null) + "_ISNATACTIVE";
returnValue = transformation.attributePrefix + (outputMode &  M01_Common.DdlOutputMode.edomXml ? db2AttrName : attrNameBase) + (!(transformation.attributeRepeatDelimiter.compareTo("") == 0) ? transformation.attributeRepeatDelimiter + attrNameBase : "") + transformation.attributePostfix;
} else {
if (outputMode &  M01_Common.DdlOutputMode.edomExpressionDummy) {
name = "CAST(NULL AS VARCHAR(1))";
} else if (outputMode &  M01_Common.DdlOutputMode.edomExpressionRef) {
name = M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, null);
} else {
int maxStrLength;
String castToDataType;
maxStrLength = 0;
if (effectiveDomainIndex > 0) {
if (M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType == M01_Common.typeId.etChar |  M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType == M01_Common.typeId.etVarchar) {
maxStrLength = M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength;
castToDataType = M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength, null, M25_Domain.g_domains.descriptors[effectiveDomainIndex].supportUnicode, null);
}
}

qualTabNameExpression = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, transformation.M01_ACM.conEnumLabelText.orgIndex, transformation.M01_ACM.conEnumLabelText.poolIndex, null, null, null, null, null, null, null);
if (transformation.M01_ACM.conEnumLabelText.forLrt) {
qualTabNameExpressionLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexExpression, ddlType, transformation.M01_ACM.conEnumLabelText.orgIndex, transformation.M01_ACM.conEnumLabelText.poolIndex, null, true, true, null, null, null, null);
}

if (transformation.M01_ACM.conEnumLabelText.forLrt) {
if (M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexExpression].useMqtToImplementLrt) {
if (maxStrLength > 0) {
name = "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING," + maxStrLength + ")) AS " + castToDataType + ") FROM " + qualTabNameExpressionLrt + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + " FETCH FIRST 1 ROW ONLY)" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
} else {
name = "(SELECT X.TERMSTRING FROM " + qualTabNameExpressionLrt + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + " FETCH FIRST 1 ROW ONLY)" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
}
} else {
if (maxStrLength > 0) {
name = "CAST(RTRIM(LEFT(COALESCE(" + "(SELECT X.TERMSTRING FROM " + qualTabNameExpressionLrt + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + "," + "(SELECT X.TERMSTRING FROM " + qualTabNameExpression + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + ")," + maxStrLength + ")) AS VARCHAR(" + maxStrLength + "))" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
} else {
name = "COALESCE(" + "(SELECT X.TERMSTRING FROM " + qualTabNameExpressionLrt + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + "," + "(SELECT X.TERMSTRING FROM " + qualTabNameExpression + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + ")" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
}
}
} else {
if (maxStrLength > 0) {
name = "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING," + maxStrLength + ")) AS " + castToDataType + ") FROM " + qualTabNameExpression + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
} else {
name = "(SELECT X.TERMSTRING FROM " + qualTabNameExpression + " X WHERE X." + M01_Globals.g_anOid + " = " + (transformation.M01_ACM.conEnumLabelText.tabQualifier.compareTo("") == 0 ? "" : transformation.M01_ACM.conEnumLabelText.tabQualifier + ".") + M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[attrIndex].shortName + "EXP", null, null, null, (attrCat &  M01_Common.AttrCategory.eacNational) != 0) + ")" + (outputMode &  M01_Common.DdlOutputMode.edomColumnName ? " AS " + db2AttrName : "");
}
}

if ((outputMode &  M01_Common.DdlOutputMode.edomValue) != 0 &  (outputMode &  M01_Common.DdlOutputMode.edomXml) == 0) {
returnValue = name;
return returnValue;
}
}

if (outputMode &  M01_Common.DdlOutputMode.edomList) {
returnValue = transformation.attributePrefix + name + transformation.attributePostfix;
} else if (outputMode &  M01_Common.DdlOutputMode.edomXsd) {
returnValue = name;
} else if (outputMode &  M01_Common.DdlOutputMode.edomXml) {
returnValue = "XMLELEMENT (NAME \"" + db2AttrName + "\", " + name + ")";
} else {
returnValue = name;
}
}
return returnValue;
}
} else if (((outputMode &  M01_Common.DdlOutputMode.edomValueVirtual) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomExpressionDummy) != 0) & attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].isExpression) {
if (attrCat &  M01_Common.AttrCategory.eacNationalBool) {
returnValue = "CAST(NULL AS " + M01_Globals.g_dbtBoolean + ")";
return returnValue;
} else {
returnValue = "CAST(NULL AS VARCHAR(1))";
return returnValue;
}
}
}

// ### ENDIF IVK ###
if (name.compareTo("") == 0) {
name = db2AttrName;
}

returnValue = transformation.attributePrefix + name + (!(transformation.attributeRepeatDelimiter.compareTo("") == 0) ? transformation.attributeRepeatDelimiter + name : "") + transformation.attributePostfix;
return returnValue;
}


// ### IF IVK ###
private static String genTransformedAttrDeclWithColReUse(String attrName, String attrNameShort,  Integer valueType,  int valueTypeIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW,  Boolean addCommaW,  Integer ddlTypeW, String infixW,  Integer outputModeW,  Integer attrCatW, Integer fkRelIndexW,  Integer indentW, Boolean attrIsReUsedW, String commentW, String defaultValueW,  Boolean useAlternativeDefaultsW,  Boolean isVirtualW,  Boolean isOptionalW, Integer attrIndexW,  Boolean persistedW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean attrIsReUsed; 
if (attrIsReUsedW == null) {
attrIsReUsed = false;
} else {
attrIsReUsed = attrIsReUsedW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String defaultValue; 
if (defaultValueW == null) {
defaultValue = "";
} else {
defaultValue = defaultValueW;
}

boolean useAlternativeDefaults; 
if (useAlternativeDefaultsW == null) {
useAlternativeDefaults = false;
} else {
useAlternativeDefaults = useAlternativeDefaultsW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

boolean persisted; 
if (persistedW == null) {
persisted = true;
} else {
persisted = persistedW;
}

String returnValue;
// ### ELSE IVK ###
//Private Function genTransformedAttrDeclWithColReUse( _
// ByRef attrName As String, ByRef attrNameShort As String, _
// valueType As AttrValueType, _
// valueTypeIndex As Integer, _
// ByRef transformation As AttributeListTransformation, _
// ByRef tabColumns As EntityColumnDescriptors, _
// Optional acmEntityType As AcmAttrContainerType = eactClass, _
// Optional acmEntityIndex As Integer = -1, _
// Optional ByRef specifics As String = "", _
// Optional ByVal addComma As Boolean = True, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional byref infix As String = "", _
// Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
// Optional attrCat As AttrCategory = eacRegular, _
// Optional ByRef fkRelIndex As Integer = -1, _
// Optional indent As Integer = 1, _
// Optional ByRef attrIsReUsed As Boolean = False, _
// Optional ByRef comment As String = "", _
// Optional byref default As String = "", _
// Optional useAlternativeDefaults As Boolean = False, _
// Optional ByVal isOptional As Boolean = False, _
// Optional attrIndex As Integer = -1 _
//) As String
// ### ENDIF IVK ###
//On Error GoTo ErrorExit 

String entityName;
String entityNameShort;
int effectiveDomainIndex;
// ### IF IVK ###
boolean attrSupportXmlExport;
attrSupportXmlExport = true;

boolean isExpression;
isExpression = false;

// ### ENDIF IVK ###
int i;

if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomain) {
effectiveDomainIndex = valueTypeIndex;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumId) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexId;
} else if (valueType == M24_Attribute_Utilities.AttrValueType.eavtDomainEnumValue) {
effectiveDomainIndex = M21_Enum.g_enums.descriptors[valueTypeIndex].domainIndexValue;
}

if (acmEntityIndex > 0) {
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// ### IF IVK ###
if (M22_Class.g_classes.descriptors[acmEntityIndex].noXmlExport) {
attrSupportXmlExport = false;
}
// ### ENDIF IVK ###
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityNameShort = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
// ### IF IVK ###
if (M23_Relationship.g_relationships.descriptors[acmEntityIndex].noXmlExport) {
attrSupportXmlExport = false;
}
// ### ENDIF IVK ###
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityNameShort = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
// ### IF IVK ###
if (M21_Enum.g_enums.descriptors[acmEntityIndex].noXmlExport) {
attrSupportXmlExport = false;
}
// ### ENDIF IVK ###
entityName = M21_Enum.g_enums.descriptors[acmEntityIndex].enumName;
entityNameShort = M21_Enum.g_enums.descriptors[acmEntityIndex].shortName;
}
}

returnValue = "";

// ### IF IVK ###
// FIXME: work-around as long as we do not fully support virtual attributes
// any virtual attribute implicitly is nullable
isOptional = isOptional |  (isVirtual &  M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType != M01_Common.typeId.etBoolean);

if (!(M03_Config.supportVirtualColumns)) {
isVirtual = false;
}

if (isVirtual & ! M03_Config.xmlExportVirtualColumns) {
return returnValue;
}

if ((outputMode &  (M01_Common.DdlOutputMode.edomXsd |  M01_Common.DdlOutputMode.edomXml))) {
if (!(attrSupportXmlExport)) {
return returnValue;
}

if (attrName.toUpperCase() == M01_ACM.conInLrt.toUpperCase()) {
if (!(M03_Config.xmlExportColumnInLrt)) {
return returnValue;
}
}
if (attrName.toUpperCase() == M01_ACM.conClassId.toUpperCase()) {
if (!(M03_Config.xmlExportColumnClassId)) {
return returnValue;
}
}
if (attrName.toUpperCase() == M01_ACM.conVersionId.toUpperCase()) {
if (!(M03_Config.xmlExportColumnVersionId)) {
return returnValue;
}
}
}

// ### ENDIF IVK ###
addComma = addComma & ! transformation.suppressAllComma;

String effectiveMaxLength;
if (M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength.compareTo("") == 0) {
effectiveMaxLength = "";
} else {
if (M03_Config.supportUnicode &  M25_Domain.g_domains.descriptors[effectiveDomainIndex].supportUnicode) {
effectiveMaxLength = new Double(M25_Domain.g_domains.descriptors[effectiveDomainIndex].unicodeExpansionFactor * new Double(M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength).intValue()).intValue() + "";
} else {
effectiveMaxLength = M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength;
}
}

String db2AttrName;
if (M03_Config.reuseColumnsInTabsForOrMapping) {
db2AttrName = M04_Utilities.genAttrName(attrName, ddlType, null, infix, null, null, null, null);
} else {
db2AttrName = M04_Utilities.genAttrName(attrName, ddlType, entityNameShort, infix, null, null, null, null);
}

// ### IF IVK ###
boolean isVirtuallyReferredTo;
isVirtuallyReferredTo = false;
boolean isVirtualInstantiated;
isVirtualInstantiated = true;
if (attrIndex > 0) {
isVirtual = M24_Attribute.g_attributes.descriptors[attrIndex].isVirtual & ! M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.isRelBasedMapping;
isExpression = M24_Attribute.g_attributes.descriptors[attrIndex].isExpression;

if (isVirtual & ! M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.isInstantiated & ((outputMode &  (M01_Common.DdlOutputMode.edomMqtLrt |  M01_Common.DdlOutputMode.edomXref)) == 0)) {
returnValue = M22_Class_Utilities.printComment("virtual column / not instantiated", -1, outputMode, null);
return returnValue;
}

if (M24_Attribute.g_attributes.descriptors[attrIndex].noXmlExport &  (outputMode &  (M01_Common.DdlOutputMode.edomXml |  M01_Common.DdlOutputMode.edomXsd)) != 0) {
return returnValue;
}
isVirtualInstantiated = M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.isInstantiated;
isVirtuallyReferredTo = M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy) > 0;
}

// ### ENDIF IVK ###
String transformedAttrName;
boolean transformToConstant;
// ### IF IVK ###
transformedAttrName = M04_Utilities.transformAttrName(db2AttrName, valueType, valueTypeIndex, transformation, ddlType, null, infix, transformToConstant, isVirtual, attrIndex, outputMode, isOptional, persisted, (outputMode &  M01_Common.DdlOutputMode.edomValue) != 0, attrCat);
// ### ELSE IVK ###
//   transformedAttrName = _
//     transformAttrName(db2AttrName, valueType, valueTypeIndex, transformation, ddlType, , infix, transformToConstant, _
//       attrIndex, outputMode, isOptional, (outputMode And edomValue) <> 0, attrCat)
// ### ENDIF IVK ###

if (transformedAttrName.compareTo("") == 0) {
return returnValue;
}

if (transformation.doCollectOidColDescriptors &  (attrCat &  transformation.oidColFilter)) {
M24_Attribute_Utilities.addOidColDescriptor(transformation.oidDescriptors, db2AttrName, attrCat);
}

int colIndex;
// ### IF IVK ###
if (isVirtual & ! (outputMode &  M01_Common.DdlOutputMode.edomList) & !transformation.doCollectVirtualAttrDescriptors) {
colIndex = M24_Attribute_Utilities.findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, valueType, valueTypeIndex, attrIsReUsed, attrCat, fkRelIndex, true, attrIndex, isOptional, isVirtualInstantiated);
} else {
colIndex = M24_Attribute_Utilities.findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, valueType, valueTypeIndex, attrIsReUsed, attrCat, fkRelIndex, null, attrIndex, isOptional, isVirtualInstantiated);
}
// ### ELSE IVK ###
//   colIndex = findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, effectiveDomainIndex, attrIsReUsed, attrCat, fkRelIndex, , attrIndex, isOptional)
// ### ENDIF IVK ###

if (attrIsReUsed &  colIndex > 0) {
tabColumns.descriptors[colIndex].columnCategory = tabColumns.descriptors[colIndex].columnCategory |  attrCat;
}

// ### IF IVK ###
if (colIndex > 0) {
if (isVirtual) {
tabColumns.descriptors[colIndex].columnCategory = tabColumns.descriptors[colIndex].columnCategory |  M01_Common.AttrCategory.eacVirtual;
}
if (isExpression) {
tabColumns.descriptors[colIndex].columnCategory = tabColumns.descriptors[colIndex].columnCategory |  M01_Common.AttrCategory.eacExpression;
}

if (attrIndex > 0) {
if (!(M24_Attribute.g_attributes.descriptors[attrIndex].groupIdBasedOn.compareTo("") == 0)) {
tabColumns.descriptors[colIndex].columnCategory = tabColumns.descriptors[colIndex].columnCategory |  M01_Common.AttrCategory.eacGroupId;
}
}
}

// ### ENDIF IVK ###
// ### IF IVK ###
if ((outputMode &  (!(M01_Common.DdlOutputMode.edomXref))) == M01_Common.DdlOutputMode.edomNone) {
// ### ELSE IVK ###
//   If outputMode = edomNone Then
// ### ENDIF IVK ###
return returnValue;
}

// ### IF IVK ###
if (outputMode &  M01_Common.DdlOutputMode.edomMapHibernate) {
String javaMaxTypeLength;
String javaDataType;
javaMaxTypeLength = M02_ToolMeta.getJavaMaxTypeLength(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxLength);
javaDataType = M02_ToolMeta.getJavaDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType);

String attrSpecs;
attrSpecs = "";

if (attrCat &  M01_Common.AttrCategory.eacAnyOid) {
returnValue = M04_Utilities.addTab(indent) + "<id name=\"" + attrName + "\" type=\"" + javaDataType + "\">" + vbCrLf + M04_Utilities.addTab(indent + 1) + "<column name=\"" + transformedAttrName + "\"/>" + vbCrLf + M04_Utilities.addTab(indent + 1) + "<generator class=\"sequence\" >" + vbCrLf + M04_Utilities.addTab(indent + 2) + "<param name=\"sequence\">" + M01_ACM.snMeta.toUpperCase() + "." + M01_LDM.gc_seqNameOid.toUpperCase() + "</param>" + vbCrLf + M04_Utilities.addTab(indent + 1) + "</generator>" + vbCrLf + M04_Utilities.addTab(indent) + "</id>" + vbCrLf;
} else if (attrCat &  M01_Common.AttrCategory.eacCid) {
returnValue = M04_Utilities.addTab(indent) + "<discriminator  column=\"" + transformedAttrName + "\" type=\"" + javaDataType + "\"" + (!(javaMaxTypeLength.compareTo("") == 0) ? "\" length=\"" + javaMaxTypeLength + "\"" : "") + "/>" + vbCrLf;
} else if (attrCat &  M01_Common.AttrCategory.eacVid) {
returnValue = M04_Utilities.addTab(indent) + "<version name=\"" + M01_ACM.conVersionId + "\" type=\"" + javaDataType + "\">" + vbCrLf + M04_Utilities.addTab(indent + 1) + "<column name=\"" + transformedAttrName + "\"/>" + vbCrLf + M04_Utilities.addTab(indent) + "</version>" + vbCrLf;
} else {
returnValue = M04_Utilities.addTab(indent) + "<property name=\"" + attrName + "\" type=\"" + javaDataType + "\">" + vbCrLf + M04_Utilities.addTab(indent + 1) + "<column name=\"" + transformedAttrName + "\"" + (!(javaMaxTypeLength.compareTo("") == 0) ? "\" length=\"" + javaMaxTypeLength + "\"" : "") + "/>" + vbCrLf + M04_Utilities.addTab(indent) + "</property>" + vbCrLf;
}
return returnValue;
} else if (outputMode &  M01_Common.DdlOutputMode.edomXsd) {
if ((attrCat &  M01_Common.AttrCategory.eacFkOidExpression)) {
return returnValue;
}
if (((outputMode &  M01_Common.DdlOutputMode.edomVirtual) | ! isVirtual) &  (!(M03_Config.reuseColumnsInTabsForOrMapping | ! attrIsReUsed))) {
if (!(transformedAttrName.compareTo("") == 0)) {
returnValue = M04_Utilities.addTab(2) + "'" + M04_Utilities.addTab(indent + 3) + "<element name=\"" + transformedAttrName + "\" type=\"standardxml:" + M25_Domain.g_domains.descriptors[effectiveDomainIndex].sectionName + "_" + M25_Domain.g_domains.descriptors[effectiveDomainIndex].domainName + (isOptional ? "_N" : "") + "\"/>' || cr ||";
}
}
return returnValue;
}

// ### ENDIF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomList) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomComment) == 0)) {
// ### IF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomVirtual) | ! isVirtual) &  (!(M03_Config.reuseColumnsInTabsForOrMapping | ! attrIsReUsed))) {
// ### ENDIF IVK ###
if ((outputMode &  M01_Common.DdlOutputMode.edomDefaultValue) &  !(defaultValue.compareTo("") == 0)) {
if (!(defaultValue.compareTo(transformedAttrName) == 0) & ! transformToConstant) {
transformedAttrName = "COALESCE(" + transformedAttrName + ", " + defaultValue + ")";
}
}
returnValue = (transformedAttrName.compareTo("") == 0 ? "" : M04_Utilities.addTab(indent) + transformedAttrName + (addComma ? "," : ""));
// ### IF IVK ###
}
// ### ENDIF IVK ###
} else if (((outputMode &  M01_Common.DdlOutputMode.edomValue) != 0) &  ((outputMode &  M01_Common.DdlOutputMode.edomComment) == 0)) {
// ### IF IVK ###
if (!(((outputMode &  M01_Common.DdlOutputMode.edomVirtual) | ! isVirtual) | ! attrIsReUsed)) {
// ### ELSE IVK ###
//     If Not attrIsReUsed Then
// ### ENDIF IVK ###
String transformedAttrValue;

transformedAttrValue = transformedAttrName;
// ### IF IVK ###
if (isVirtuallyReferredTo) {
int thisClassIndex;
int referringClassIndex;
String fkAttrName;
thisClassIndex = M24_Attribute.g_attributes.descriptors[attrIndex].acmEntityIndex;
for (int i = 1; i <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy); i++) {
referringClassIndex = M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i]].acmEntityIndex;
fkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, null, M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i]].virtuallyMapsTo.relIndex].shortName + (M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i]].virtuallyMapsTo.navDirection == M01_Common.RelNavigationDirection.etLeft ? M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i]].virtuallyMapsTo.relIndex].rlShortRelName : M23_Relationship.g_relationships.descriptors[M24_Attribute.g_attributes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyReferredToBy[i]].virtuallyMapsTo.relIndex].lrShortRelName), null, null, null);
//                .virtuallyMapsTo.relIndex
//                classIndex = g_attributes.descriptors(.virtuallyReferredToBy(i)).acmEntityIndex
}
} else if (transformedAttrValue == (transformation.attributePrefix + db2AttrName + transformation.attributePostfix)) {
// ### ELSE IVK ###
//       If transformedAttrValue = (transformation.attributePrefix & db2AttrName & transformation.attributePostfix) Then
// ### ENDIF IVK ###
// todo: use a more transparent way to figure out if attribute value effectively was transformed
if ((outputMode &  M01_Common.DdlOutputMode.edomDefaultValue) &  !(defaultValue.compareTo("") == 0)) {
transformedAttrValue = defaultValue;
} else {
transformedAttrValue = "CAST(NULL AS " + M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[effectiveDomainIndex].scale, null, null) + ")";
}
}
returnValue = M04_Utilities.addTab(indent) + transformedAttrValue + (addComma ? "," : "");
}
} else if (outputMode &  M01_Common.DdlOutputMode.edomComment) {
if (!(comment.compareTo("") == 0)) {
returnValue = M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " IS '" + M00_Helper.replace(comment, "'", "''", vbTextCompare) + "'" + (addComma ? "," : "");
} else {
System.out.println("empty comment / " + db2AttrName);
;
}
} else {
// ### IF IVK ###
if (((outputMode &  M01_Common.DdlOutputMode.edomVirtual) | ! isVirtual) &  (outputMode &  M01_Common.DdlOutputMode.edomNoSpecifics) == M01_Common.DdlOutputMode.edomNoSpecifics) {
// ### ELSE IVK ###
//     If (outputMode And edomNoSpecifics) = edomNoSpecifics Then
// ### ENDIF IVK ###
if (!(M03_Config.reuseColumnsInTabsForOrMapping | ! attrIsReUsed)) {
if (addComma) {
returnValue = M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " " + M04_Utilities.paddRight(M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[effectiveDomainIndex].scale, null, null), null, null) + " " + (addComma ? "," : "");
} else {
returnValue = M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " " + M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[effectiveDomainIndex].scale, null, null);
}
}
} else {
// ### IF IVK ###
if (isVirtual &  attrIndex > 0 & !((outputMode &  M01_Common.DdlOutputMode.edomDeclVirtual) == M01_Common.DdlOutputMode.edomDeclVirtual)) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.isRelBasedMapping) {
returnValue = M22_Class_Utilities.printComment("virtually map to \"" + M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.mapTo + M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.targetClassIndex].className + "\"", -1, outputMode, null);
} else {
returnValue = M22_Class_Utilities.printComment("virtually map to \"" + M04_Utilities.mapExpression(M24_Attribute.g_attributes.descriptors[attrIndex].virtuallyMapsTo.mapTo, transformation.M01_ACM.conEnumLabelText.orgIndex, transformation.M01_ACM.conEnumLabelText.poolIndex, ddlType, transformation.M01_ACM.conEnumLabelText.tabQualifier, null, transformation.M01_ACM.conEnumLabelText.lrtOidRef) + "\"", -1, outputMode, null) + ((M03_Config.reuseColumnsInTabsForOrMapping &  attrIsReUsed) | ! M24_Attribute.g_attributes.descriptors[attrIndex].isPersistent ? "" : vbCrLf + M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " " + M04_Utilities.paddRight(M02_ToolMeta.getDataTypeByDomainIndex(effectiveDomainIndex, null), null, null) + " " + (addComma ? "," : ""));
}
} else if (M03_Config.reuseColumnsInTabsForOrMapping &  attrIsReUsed) {
// ### ELSE IVK ###
//       If reuseColumnsInTabsForOrMapping And attrIsReUsed Then
// ### ENDIF IVK ###
returnValue = M22_Class_Utilities.printComment("reuse attribute \"" + tabColumns.descriptors[colIndex].acmAttributeName + (!(tabColumns.descriptors[colIndex].acmEntityName.compareTo("") == 0) ? "@" + tabColumns.descriptors[colIndex].acmEntityName : "") + "\"", -1, outputMode, null);
} else {
String constraint;
String constraintName;
int numConditions;
numConditions = 0;
if (acmEntityIndex > 0) {
constraintName = "CHK_" + (attrNameShort.compareTo("") == 0 ? attrName.substring(0, 14).toUpperCase() : attrNameShort.toUpperCase());
}
constraint = "";

if (!(M25_Domain.g_domains.descriptors[effectiveDomainIndex].minLength.compareTo("") == 0)) {
constraint = constraint + "(LENGTH(" + db2AttrName + ") >= " + M25_Domain.g_domains.descriptors[effectiveDomainIndex].minLength + ")";
numConditions = numConditions + 1;
}

if (!(M25_Domain.g_domains.descriptors[effectiveDomainIndex].minValue.compareTo("") == 0)) {
constraint = constraint + (constraint.compareTo("") == 0 ? "" : " AND ") + "(" + db2AttrName + " >= " + M25_Domain.g_domains.descriptors[effectiveDomainIndex].minValue + ")";
numConditions = numConditions + 1;
}

if (!(M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxValue.compareTo("") == 0)) {
constraint = constraint + (constraint.compareTo("") == 0 ? "" : " AND ") + "(" + db2AttrName + " <= " + M25_Domain.g_domains.descriptors[effectiveDomainIndex].maxValue + ")";
numConditions = numConditions + 1;
}

if (!(M25_Domain.g_domains.descriptors[effectiveDomainIndex].constraint.compareTo("") == 0)) {
constraint = constraint + (constraint.compareTo("") == 0 ? "" : " AND ") + "(" + M00_Helper.replace(M25_Domain.g_domains.descriptors[effectiveDomainIndex].constraint, "<value>", db2AttrName) + ")";
numConditions = numConditions + 1;
}

if (!(M25_Domain.g_domains.descriptors[effectiveDomainIndex].valueList.compareTo("") == 0)) {
constraint = constraint + (constraint.compareTo("") == 0 ? "" : " AND ") + "(" + db2AttrName + " IN (" + M25_Domain.g_domains.descriptors[effectiveDomainIndex].valueList + "))";
numConditions = numConditions + 1;
}

if (!(constraint.compareTo("") == 0)) {
constraint = (specifics.compareTo("") == 0 ? "" : " ") + (!(constraintName.compareTo("") == 0) ? "CONSTRAINT " + constraintName + " " : "") + "CHECK" + (numConditions > 1 ? "(" : "") + constraint + (numConditions > 1 ? ")" : "");
}

if (M25_Domain.g_domains.descriptors[effectiveDomainIndex].notLogged) {
constraint = constraint + (new String (" " + constraint).substring(new String (" " + constraint).length() - 1 - 1).compareTo(" ") == 0 ? "" : " ") + "NOT LOGGED";
}

if (M25_Domain.g_domains.descriptors[effectiveDomainIndex].notCompact) {
constraint = constraint + (new String (" " + constraint).substring(new String (" " + constraint).length() - 1 - 1).compareTo(" ") == 0 ? "" : " ") + "NOT COMPACT";
}

M96_DdlSummary.addAttrToDdlSummary(db2AttrName, M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, null, null, null, null), effectiveMaxLength, specifics, ddlType);

if (transformation.trimRight) {
returnValue = M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " " + M04_Utilities.paddRight(M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[effectiveDomainIndex].scale, null, null), null, null) + " " + specifics + (transformation.ignoreConstraint ? "" : constraint) + (addComma ? "," : "").replaceAll(" +$","");
} else {
returnValue = M04_Utilities.addTab(indent) + M04_Utilities.paddRight(db2AttrName, M01_LDM.gc_dbMaxAttributeNameLength, null) + " " + M04_Utilities.paddRight(M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[effectiveDomainIndex].dataType, effectiveMaxLength, M25_Domain.g_domains.descriptors[effectiveDomainIndex].scale, null, null), null, null) + " " + specifics + (transformation.ignoreConstraint ? "" : constraint) + (addComma ? "," : "");
}
}
}
}

NormalExit:
//On Error Resume Next 
return returnValue;
ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


// ### IF IVK ###
private static String genTransformedAttrDecl(String attrName, String attrNameShort, Integer valueType, int valueTypeIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW, Boolean addCommaW, Integer ddlTypeW, String infixW, Integer outputModeW, Integer attrCatW, Integer fkRelIndexW, Integer indentW,  Boolean isVirtualW,  Boolean isOptionalW, Integer attrIndexW, String commentW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String returnValue;
//On Error GoTo ErrorExit 

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

returnValue = genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, null, comment, null, null, isVirtual, isOptional, attrIndex, null);

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}
// ### ELSE IVK ###
//Private Function genTransformedAttrDecl( _
// ByRef attrName As String, _
// ByRef attrNameShort As String, _
// valueType As AttrValueType, _
// valueTypeIndex As Integer, _
// ByRef transformation As AttributeListTransformation, _
// Optional acmEntityType As AcmAttrContainerType = eactClass, _
// Optional acmEntityIndex As Integer = -1, _
// Optional ByRef specifics As String = "", _
// Optional addComma As Boolean = True, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional byref infix As String = "", _
// Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
// Optional attrCat As AttrCategory = eacRegular, _
// Optional ByRef fkRelIndex As Integer = -1, _
// Optional indent As Integer = 1, _
// Optional ByVal isOptional As Boolean = False, _
// Optional attrIndex As Integer = -1, _
// Optional ByRef comment As String = "" _
//) As String
// On Error Goto ErrorExit
//
// Dim tabColumns As EntityColumnDescriptors
// tabColumns = nullEntityColumnDescriptors
//
// genTransformedAttrDecl = _
//   genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, _
//      specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, , comment, , , isOptional, attrIndex)
//
//NormalExit:
// On Error Resume Next
// Exit Function
//
//ErrorExit:
// errMsgBox Err.description
// Resume NormalExit
//End Function
// ### ENDIF IVK ###


// ### IF IVK ###
public static String genAttrDecl(String attrName, String attrNameShort, Integer valueType, int valueTypeIndex, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW, Boolean addCommaW, Integer ddlTypeW, String infixW, Integer outputModeW, Integer attrCatW, Integer fkRelIndexW, Integer indentW,  Boolean isVirtualW,  Boolean isOptionalW, Integer attrIndexW, String commentW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String returnValue;
//On Error GoTo ErrorExit 

returnValue = genTransformedAttrDecl(attrName, attrNameShort, valueType, valueTypeIndex, M24_Attribute_Utilities.nullAttributeTransformation, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, isVirtual, isOptional, attrIndex, comment);

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}
// ### ELSE IVK ###
//Function genAttrDecl( _
// ByRef attrName As String, _
// ByRef attrNameShort As String, _
// valueType As AttrValueType, _
// valueTypeIndex As Integer, _
// Optional acmEntityType As AcmAttrContainerType = eactClass, _
// Optional acmEntityIndex As Integer = -1, _
// Optional ByRef specifics As String = "", _
// Optional addComma As Boolean = True, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional byref infix As String = "", _
// Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
// Optional attrCat As AttrCategory = eacRegular, _
// Optional ByRef fkRelIndex As Integer = -1, _
// Optional indent As Integer = 1, _
// Optional ByVal isOptional As Boolean = False, _
// Optional attrIndex As Integer = -1, _
// Optional ByRef comment As String = "" _
//) As String
// On Error Goto ErrorExit
//
// genAttrDecl = _
//   genTransformedAttrDecl(attrName, attrNameShort, valueType, valueTypeIndex, nullAttributeTransformation, acmEntityType, acmEntityIndex, _
//     specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, isOptional, attrIndex, comment)
//
//NormalExit:
// On Error Resume Next
// Exit Function
//
//ErrorExit:
// errMsgBox Err.description
// Resume NormalExit
//End Function
// ### ENDIF IVK ###


// ### IF IVK ###
public static String genSurrogateKeyName(Integer ddlTypeW, String classNameShortW, String infixW, String suffixW,  Integer valueTypeW,  Boolean isNationalW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String classNameShort; 
if (classNameShortW == null) {
classNameShort = "";
} else {
classNameShort = classNameShortW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer valueType; 
if (valueTypeW == null) {
valueType = null;
} else {
valueType = valueTypeW;
}

boolean isNational; 
if (isNationalW == null) {
isNational = false;
} else {
isNational = isNationalW;
}

String returnValue;
returnValue = M04_Utilities.genAttrName(M01_ACM.cosnOid, ddlType, classNameShort, infix, suffix, valueType, isNational, null);
return returnValue;
}
// ### ELSE IVK ###
//Function genSurrogateKeyName( _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional ByRef classNameShort As String = "", _
// Optional ByRef infix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional ByVal valueType As AttrValueType _
//) As String
// genSurrogateKeyName = genAttrName(cosnOid, ddlType, classNameShort, infix, suffix, valueType)
//End Function
// ### ENDIF IVK ###


public static String genSurrogateKeyShortName(Integer ddlTypeW, String classNameShortW, String infixW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String classNameShort; 
if (classNameShortW == null) {
classNameShort = "";
} else {
classNameShort = classNameShortW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

String returnValue;
returnValue = M04_Utilities.genAttrName(M01_ACM.cosnOid, ddlType, classNameShort, infix, null, null, null, null);
return returnValue;
}


// ### IF IVK ###
public static String genTransformedAttrDeclByDomainWithColReUse(String attrName, String attrNameShort, Integer valueType, int valueTypeIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, M24_Attribute_Utilities.EntityColumnDescriptors tabColumns, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW, Boolean addCommaW, Integer ddlTypeW, String infixW, Integer outputModeW, Integer attrCatW, Integer fkRelIndexW, Integer indentW, Boolean attrIsReUsedW, String commentW, String defaultValueW,  Boolean isVirtualW,  Boolean isOptionalW, Integer attrIndexW,  Boolean persistedW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean attrIsReUsed; 
if (attrIsReUsedW == null) {
attrIsReUsed = false;
} else {
attrIsReUsed = attrIsReUsedW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String defaultValue; 
if (defaultValueW == null) {
defaultValue = "";
} else {
defaultValue = defaultValueW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

boolean persisted; 
if (persistedW == null) {
persisted = true;
} else {
persisted = persistedW;
}

String returnValue;
// ### ELSE IVK ###
//Function genTransformedAttrDeclByDomainWithColReUse( _
// ByRef attrName As String, _
// ByRef attrNameShort As String, _
// valueType As AttrValueType, _
// valueTypeIndex As Integer, _
// ByRef transformation As AttributeListTransformation, _
// ByRef tabColumns As EntityColumnDescriptors, _
// Optional acmEntityType As AcmAttrContainerType = eactClass, _
// Optional acmEntityIndex As Integer = -1, _
// Optional ByRef specifics As String = "", _
// Optional addComma As Boolean = True, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional ByRef infix As String = "", _
// Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
// Optional attrCat As AttrCategory = eacRegular, _
// Optional ByRef fkRelIndex As Integer = -1, _
// Optional indent As Integer = 1, _
// Optional ByRef attrIsReUsed As Boolean = False, _
// Optional ByRef comment As String = "", _
// Optional ByRef default As String = "", _
// Optional ByVal isOptional As Boolean = False, _
// Optional attrIndex As Integer = -1 _
//) As String
// ### ENDIF IVK ###
//On Error GoTo ErrorExit 

returnValue = "";

// FIXME: This Proc-Level is redundant ?

// ### IF IVK ###
returnValue = genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, attrIsReUsed, comment, defaultValue, null, isVirtual, isOptional, attrIndex, persisted);
// ### ELSE IVK ###
//   genTransformedAttrDeclByDomainWithColReUse = _
//     genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, _
//       acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, _
//       indent, attrIsReUsed, comment, default, , isOptional, attrIndex)
// ### ENDIF IVK ###

NormalExit:
//On Error Resume Next 
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


public static String genTransformedAttrDeclByDomain(String attrName, String attrNameShort, Integer valueType, int valueTypeIndex, M24_Attribute_Utilities.AttributeListTransformation transformation, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW, Boolean addCommaW, Integer ddlTypeW, String infixW, Integer outputModeW, Integer attrCatW, Integer fkRelIndexW, Integer indentW, String commentW, String defaultValueW,  Boolean isVirtualW,  Boolean isOptionalW, Integer attrIndexW,  Boolean persistedW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String defaultValue; 
if (defaultValueW == null) {
defaultValue = "";
} else {
defaultValue = defaultValueW;
}

boolean isVirtual; 
if (isVirtualW == null) {
isVirtual = false;
} else {
isVirtual = isVirtualW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

int attrIndex; 
if (attrIndexW == null) {
attrIndex = -1;
} else {
attrIndex = attrIndexW;
}

boolean persisted; 
if (persistedW == null) {
persisted = true;
} else {
persisted = persistedW;
}

String returnValue;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

// ### IF IVK ###
returnValue = M04_Utilities.genTransformedAttrDeclByDomainWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, null, comment, defaultValue, isVirtual, isOptional, attrIndex, persisted);
// ### ELSE IVK ###
// genTransformedAttrDeclByDomain = _
//   genTransformedAttrDeclByDomainWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, _
//     transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, _
//     outputMode, attrCat, fkRelIndex, indent, , comment, default, isOptional, attrIndex)
// ### ENDIF IVK ###
return returnValue;
}


public static String genAttrDeclByDomain(String attrName, String attrNameShort, Integer valueType, int valueTypeIndex, Integer acmEntityTypeW, Integer acmEntityIndexW, String specificsW, Boolean addCommaW, Integer ddlTypeW, String infixW, Integer outputModeW, Integer attrCatW, Integer fkRelIndexW, Integer indentW, Boolean isOptionalW, String commentW) {
Integer acmEntityType; 
if (acmEntityTypeW == null) {
acmEntityType = M24_Attribute_Utilities.AcmAttrContainerType.eactClass;
} else {
acmEntityType = acmEntityTypeW;
}

int acmEntityIndex; 
if (acmEntityIndexW == null) {
acmEntityIndex = -1;
} else {
acmEntityIndex = acmEntityIndexW;
}

String specifics; 
if (specificsW == null) {
specifics = "";
} else {
specifics = specificsW;
}

boolean addComma; 
if (addCommaW == null) {
addComma = true;
} else {
addComma = addCommaW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

Integer outputMode; 
if (outputModeW == null) {
outputMode = M01_Common.DdlOutputMode.edomDeclNonLrt;
} else {
outputMode = outputModeW;
}

Integer attrCat; 
if (attrCatW == null) {
attrCat = M01_Common.AttrCategory.eacRegular;
} else {
attrCat = attrCatW;
}

int fkRelIndex; 
if (fkRelIndexW == null) {
fkRelIndex = -1;
} else {
fkRelIndex = fkRelIndexW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean isOptional; 
if (isOptionalW == null) {
isOptional = false;
} else {
isOptional = isOptionalW;
}

String comment; 
if (commentW == null) {
comment = "";
} else {
comment = commentW;
}

String returnValue;
returnValue = M04_Utilities.genTransformedAttrDeclByDomain(attrName, attrNameShort, valueType, valueTypeIndex, M24_Attribute_Utilities.nullAttributeTransformation, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, comment, null, null, isOptional, null, null);
return returnValue;
}


// ### IF IVK ###
public static String genAttrName(String attrName, Integer ddlTypeW, String entityNameShortW, String infixW, String suffixW,  Integer valueTypeW,  Boolean isNationalW,  Boolean forDbW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityNameShort; 
if (entityNameShortW == null) {
entityNameShort = "";
} else {
entityNameShort = entityNameShortW;
}

String infix; 
if (infixW == null) {
infix = "";
} else {
infix = infixW;
}

String suffix; 
if (suffixW == null) {
suffix = "";
} else {
suffix = suffixW;
}

Integer valueType; 
if (valueTypeW == null) {
valueType = M24_Attribute_Utilities.AttrValueType.eavtDomain;
} else {
valueType = valueTypeW;
}

boolean isNational; 
if (isNationalW == null) {
isNational = false;
} else {
isNational = isNationalW;
}

boolean forDb; 
if (forDbW == null) {
forDb = true;
} else {
forDb = forDbW;
}

String returnValue;
// ### ELSE IVK ###
//Function genAttrName( _
// ByRef attrName As String, _
// Optional ddlType As DdlTypeId = edtLdm, _
// Optional ByRef entityNameShort As String = "", _
// Optional ByRef infix As String = "", _
// Optional ByRef suffix As String = "", _
// Optional ByVal valueType As AttrValueType = eavtDomain, _
//  Optional ByVal forDb As Boolean = True _
//) As String
// ### ENDIF IVK ###
String result;
returnValue = "X?";

// ### IF IVK ###
if (isNational) {
if (!(entityNameShort.compareTo("") == 0) &  entityNameShort.length() == 3) {
infix = infix + M00_Helper.replace(M01_Globals_IVK.gc_anSuffixNat, "_", "", 1, 1);
} else {
suffix = suffix + M01_Globals_IVK.gc_anSuffixNat;
}
}
// ### ENDIF IVK ###

if (valueType == M24_Attribute_Utilities.AttrValueType.eavtEnum) {
suffix = suffix + M01_Globals.gc_enumAttrNameSuffix;
}

if (forDb) {
returnValue = entityNameShort + (entityNameShort.compareTo("") == 0 ? "" : "_") + infix + (infix.compareTo("") == 0 ? "" : "_") + attrName + suffix.toUpperCase().substring(0, M01_LDM.gc_dbMaxAttributeNameLength);
} else {
returnValue = entityNameShort + (entityNameShort.compareTo("") == 0 ? "" : "_") + infix + (infix.compareTo("") == 0 ? "" : "_") + attrName + suffix;
}
return returnValue;
}


public static String genAttrNameByIndex(int attrIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String returnValue;
returnValue = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrIndex].attributeName, ddlType, null, null, null, M24_Attribute.g_attributes.descriptors[attrIndex].valueType, null, null);
return returnValue;
}


public static String genPkName(String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

String returnValue;
returnValue = "";

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "PK_" + objNameShort.toUpperCase() + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : "");
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
returnValue = "PK_" + objNameShort.toUpperCase() + M04_Utilities.genOrgId(thisOrgIndex, ddlType, null) + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : "");
}
return returnValue;
}


public static String genUkName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

String returnValue;
String prefix;
prefix = (forLrt ? "IDX" : "UK") + (forMqt ? M01_LDM.gc_dbObjSuffixShortMqt : "") + "_";

returnValue = M04_Utilities.genQualObjName(sectionIndex, prefix + objNameShort.toUpperCase() + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), prefix + objNameShort.toUpperCase() + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
return returnValue;
}


public static String genQualPkName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

String returnValue;
String prefix;
prefix = "PK_";

returnValue = M04_Utilities.genQualObjName(sectionIndex, prefix + objNameShort.toUpperCase() + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), prefix + objNameShort.toUpperCase() + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null);
return returnValue;
}

public static String genQualUkName(int sectionIndex, String objName, String objNameShort, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW, Boolean forMqtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

boolean forMqt; 
if (forMqtW == null) {
forMqt = false;
} else {
forMqt = forMqtW;
}

String returnValue;
String prefix;
prefix = "";

returnValue = M00_Helper.replace(M04_Utilities.genQualObjName(sectionIndex, prefix + objNameShort + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), prefix + objNameShort + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : ""), ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null), "<<MPCID>>", "<<mpcId>>");
return returnValue;
}

public static String genFkName(String objName, String objNameShort, String refObjDescr, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean forGenW, Boolean forLrtW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "FK_" + objNameShort + (refObjDescr.compareTo("") == 0 ? "" : refObjDescr) + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : "").toUpperCase();
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
String numInfix;
numInfix = M04_Utilities.genOrgId(thisOrgIndex, ddlType, null) + M04_Utilities.genPoolId(thisPoolIndex, ddlType);
returnValue = "FK_" + numInfix + objNameShort + (refObjDescr.compareTo("") == 0 ? "" : refObjDescr) + (forLrt ? "_" + M01_LDM.gc_dbObjSuffixLrt : "") + (forGen ? "_" + M01_LDM.gc_dbObjSuffixGen : "").toUpperCase();
}
return returnValue;
}


// ### IF IVK ###
public static String genPartitionName(long oid, Boolean byPsOidW, String cidW) {
boolean byPsOid; 
if (byPsOidW == null) {
byPsOid = true;
} else {
byPsOid = byPsOidW;
}

String cid; 
if (cidW == null) {
cid = "";
} else {
cid = cidW;
}

String returnValue;
returnValue = (byPsOid ? "P" : "D") + cid + new String ("000000000000000000000000000000000000000000000000" + String.valueOf(oid)).substring(new String ("000000000000000000000000000000000000000000000000" + String.valueOf(oid)).length() - 1 - M01_Globals.gc_maxDb2PartitionNameSuffixLen);
return returnValue;
}


// ### ENDIF IVK ###
public static String genMetaFileName(String dir, String fileBase, String suffixW) {
String suffix; 
if (suffixW == null) {
suffix = ".lst";
} else {
suffix = suffixW;
}

String returnValue;
returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\meta\\" + fileBase + suffix;
return returnValue;
}


public static String genCsvFileName(String dir, int sectionIndex, String objName, int step, String subDirW, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean isCommonToOrgW, Boolean isCommonToPoolsW, Integer forOrgIndexW) {
String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

boolean isCommonToOrg; 
if (isCommonToOrgW == null) {
isCommonToOrg = true;
} else {
isCommonToOrg = isCommonToOrgW;
}

boolean isCommonToPools; 
if (isCommonToPoolsW == null) {
isCommonToPools = true;
} else {
isCommonToPools = isCommonToPoolsW;
}

int forOrgIndex; 
if (forOrgIndexW == null) {
forOrgIndex = -1;
} else {
forOrgIndex = forOrgIndexW;
}

String returnValue;
int seqNo;
seqNo = M20_Section.getSectionSeqNoByIndex(sectionIndex);
String fileBase;

thisOrgIndex = M71_Org_Utilities.getEffectiveOrgIndex(thisOrgIndex, isCommonToOrg);
thisPoolIndex = M72_DataPool_Utilities.getEffectivePoolIndex(thisPoolIndex, isCommonToPools);

if (forOrgIndex == -1) {
forOrgIndex = thisOrgIndex;
}

fileBase = (subDir == "" ? "" : subDir + "\\") + new String ("00000" + seqNo).substring(new String ("00000" + seqNo).length() - 1 - M01_Common.seqNoDigits) + "-" + new String ("00000" + step).substring(new String ("00000" + step).length() - 1 - M01_Common.stepDigits) + "-" + M04_Utilities.genQualObjName(sectionIndex, objName, "", ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null, null) + "." + M01_Globals.gc_fileNameSuffixCsv;

returnValue = "";

String dirInfix;
dirInfix = (M01_Globals.g_genLrtSupport ? "-LRT" : "");

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
dirInfix = "\\LDM" + dirInfix;

returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + dirInfix + "\\CSV\\" + fileBase;
} else {
boolean orgIsTemplate;
orgIsTemplate = false;
if (forOrgIndex > 0) {
orgIsTemplate = M71_Org.g_orgs.descriptors[forOrgIndex].isTemplate;
}

dirInfix = "\\PDM" + dirInfix;

String orgNameInfix;
String poolName;
if (orgIsTemplate) {
dirInfix = dirInfix + "\\template\\";
orgNameInfix = M00_Helper.replace(M71_Org.getOrgNameByIndex(forOrgIndex), " ", "_", null, null, vbTextCompare);
} else {
orgNameInfix = M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, null) + "-" + M00_Helper.replace(M71_Org.getOrgNameByIndex(forOrgIndex), " ", "_", null, null, vbTextCompare);
}

if (thisPoolIndex > 0) {
poolName = M00_Helper.replace(M72_DataPool.g_pools.descriptors[thisPoolIndex].name, " ", "_", null, null, vbTextCompare);
}

returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + dirInfix + ((thisOrgIndex < 0) & ! orgIsTemplate ? "" : orgNameInfix) + (thisPoolIndex < 0 ? "" : "\\DPool-" + M04_Utilities.genPoolIdByIndex(thisPoolIndex, ddlType) + "-" + poolName) + "\\CSV\\" + fileBase;
}
return returnValue;
}


private static String genXFileName(String dir,  int sectionIndex, int step, Integer ddlType, String suffix,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementW, Integer ldmIterationW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int increment; 
if (incrementW == null) {
increment = 0;
} else {
increment = incrementW;
}

int ldmIteration; 
if (ldmIterationW == null) {
ldmIteration = -1;
} else {
ldmIteration = ldmIterationW;
}

String returnValue;
int seqNo;
seqNo = M20_Section.getSectionSeqNoByIndex(sectionIndex);
String fileBase;

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
fileBase = (subDir == "" ? "" : subDir + "\\") + (ldmIteration >= 0 ? ldmIteration + "-" : "") + new String ("00000" + seqNo + increment).substring(new String ("00000" + seqNo + increment).length() - 1 - M01_Common.seqNoDigits) + "-" + new String ("00000" + step).substring(new String ("00000" + step).length() - 1 - M01_Common.stepDigits) + "-" + M20_Section.g_sections.descriptors[sectionIndex].sectionName.toUpperCase() + "." + suffix;
} else {
fileBase = (subDir == "" ? "" : subDir + "\\") + new String ("00000" + seqNo + increment).substring(new String ("00000" + seqNo + increment).length() - 1 - M01_Common.seqNoDigits) + "-" + new String ("00000" + step).substring(new String ("00000" + step).length() - 1 - M01_Common.stepDigits) + "-" + M20_Section.g_sections.descriptors[sectionIndex].sectionName.toUpperCase() + "." + suffix;
}
String dirInfix;
dirInfix = (M01_Globals.g_genLrtSupport ? "-LRT" : "");

returnValue = "";

if (ddlType == M01_Common.DdlTypeId.edtLdm) {
dirInfix = "\\LDM" + dirInfix;

returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + dirInfix + "\\" + fileBase;
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
boolean orgIsTemplate;
if (thisOrgIndex > 0) {
orgIsTemplate = M71_Org.g_orgs.descriptors[thisOrgIndex].isTemplate;
} else {
orgIsTemplate = false;
}

dirInfix = "\\PDM" + dirInfix + (orgIsTemplate ? "\\template" : "");

String orgNameInfix;
String poolName;
orgNameInfix = (orgIsTemplate ? "" : M01_Globals_IVK.gc_dirPrefixOrg + M04_Utilities.genOrgId(thisOrgIndex, ddlType, null) + "-") + M00_Helper.replace(M71_Org.getOrgNameByIndex(thisOrgIndex), " ", "_", null, null, vbTextCompare);

poolName = M00_Helper.replace(M72_DataPool.getDataPoolNameByIndex(thisPoolIndex), " ", "_", null, null, vbTextCompare);

returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + dirInfix + (thisOrgIndex <= 0 ? "" : "\\" + orgNameInfix) + (thisPoolIndex <= 0 ? "" : "\\DPool-" + M04_Utilities.genPoolId(thisPoolIndex, ddlType) + "-" + poolName) + "\\" + fileBase;
}
return returnValue;
}


public static String genDdlFileName(String dir,  int sectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementW, Integer ldmIterationW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int increment; 
if (incrementW == null) {
increment = 0;
} else {
increment = incrementW;
}

int ldmIteration; 
if (ldmIterationW == null) {
ldmIteration = M01_Common.ldmIterationGlobal;
} else {
ldmIteration = ldmIterationW;
}

String returnValue;
returnValue = genXFileName(dir, sectionIndex, step, ddlType, M01_Globals.gc_fileNameSuffixDdl, thisOrgIndex, thisPoolIndex, subDir, increment, ldmIteration);
return returnValue;
}


private static void addHeaderLine(int fileNo, String lineW) {
String line; 
if (lineW == null) {
line = "";
} else {
line = lineW;
}

final int lineLength = 88;
final String blanks = "                                                                                                         ";

M00_FileWriter.printToFile(fileNo, "-- # " + line + blanks.substring(0, lineLength - 4) + " #");
}


private static void addDxlFileHeader(int fileNo, String fileName) {
final int maxFileNameLen = 80;

if (M03_Config.generateDdlHeader) {
if (M00_FileWriter.openFileForOutput(fileNo) == 0) {
fileName = M00_Helper.replace(fileName, "\\", "/");

if (M03_Config.workSheetSuffix + "" != "") {
fileName = M00_Helper.replace(fileName, M03_Config.workSheetSuffix + "/", "");
}

final String extraBlanks = "                                                                                                              ";
int fileNameLength;
fileNameLength = fileName.length();
if (fileNameLength > maxFileNameLen) {
fileNameLength = maxFileNameLen;
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine1);
addHeaderLine(fileNo, null);
addHeaderLine(fileNo, "File: " + fileName);
addHeaderLine(fileNo, null);
M00_FileWriter.printToFile(fileNo, "-- " + M01_LDM.gc_sqlDelimLine1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "ECHO processing file '" + fileName.substring(0, maxFileNameLen) + "'" + M01_LDM.gc_sqlCmdDelim);

// add extra blanks to make sure that we can exactly recognize 'empty' files by their size

M00_FileWriter.printToFile(fileNo, (fileNameLength >= maxFileNameLen ? "" : extraBlanks.substring(0, maxFileNameLen - fileNameLength)));

if (ddlEmptyFileSize == 0) {
ddlEmptyFileSize = M00_FileWriter.openFileForOutput(fileNo);
}

}
} else {
ddlEmptyFileSize = 0;
}
}


public static Integer openDdlFile(String dir, int sectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementW, Integer ldmIterationW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int increment; 
if (incrementW == null) {
increment = 0;
} else {
increment = incrementW;
}

int ldmIteration; 
if (ldmIterationW == null) {
ldmIteration = M01_Common.ldmIterationGlobal;
} else {
ldmIteration = ldmIterationW;
}

Integer returnValue;
String fileName;
int fileNo;

returnValue = -1;

fileName = M04_Utilities.genDdlFileName(dir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, increment, ldmIteration);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, true);
addDxlFileHeader(fileNo, fileName.substring(fileName.length() - 1 - fileName.length() - dir.length() - 1));
returnValue = fileNo;

return returnValue;
return returnValue;
}


public static Integer openDdlFileBySectionIndex(String dir, int thisSectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementIndexW, Integer ldmIterationW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int incrementIndex; 
if (incrementIndexW == null) {
incrementIndex = 1;
} else {
incrementIndex = incrementIndexW;
}

int ldmIteration; 
if (ldmIterationW == null) {
ldmIteration = M01_Common.ldmIterationGlobal;
} else {
ldmIteration = ldmIterationW;
}

Integer returnValue;
returnValue = -1;

//On Error GoTo ErrorExit 

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
thisOrgIndex = -1;
thisPoolIndex = -1;
}

if (M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[thisOrgIndex, thisPoolIndex, step, incrementIndex] <= 0) {
String fileName;
int fileNo;
fileName = M04_Utilities.genDdlFileName(dir, thisSectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, M01_Globals.g_fileNameIncrements[incrementIndex], ldmIteration);
M04_Utilities.assertDir(fileName);

fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);
addDxlFileHeader(fileNo, fileName.substring(fileName.length() - 1 - fileName.length() - dir.length() - 1));

M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[(thisOrgIndex, thisPoolIndex, step, incrementIndex)] = fileNo;
}

returnValue = M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[thisOrgIndex, thisPoolIndex, step, incrementIndex];

NormalExit:
return returnValue;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
return returnValue;
}


public static void closeAllCsvFiles(int ddlType = M01_Common.DdlTypeId.edtLdm) {
int ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

M78_DbMeta.closeCsvFilesLPdmTable();
}


public static void closeAllDdlFiles( Integer orgIndexW,  Integer poolIndexW,  Integer sectionIndexW,  Integer processingStepW,  Integer incrementIndexW,  Integer ddlTypeW) {
int orgIndex; 
if (orgIndexW == null) {
orgIndex = -1;
} else {
orgIndex = orgIndexW;
}

int poolIndex; 
if (poolIndexW == null) {
poolIndex = -1;
} else {
poolIndex = poolIndexW;
}

int sectionIndex; 
if (sectionIndexW == null) {
sectionIndex = -1;
} else {
sectionIndex = sectionIndexW;
}

int processingStep; 
if (processingStepW == null) {
processingStep = -1;
} else {
processingStep = processingStepW;
}

int incrementIndex; 
if (incrementIndexW == null) {
incrementIndex = -1;
} else {
incrementIndex = incrementIndexW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtPdm;
} else {
ddlType = ddlTypeW;
}

if (ddlType != M01_Common.DdlTypeId.edtPdm) {
orgIndex = -1;
poolIndex = -1;
}

int thisOrgIndex;
int thisPoolIndex;
int thisSectionIndex;
int thisProcessingStep;
int thisIncrementIndex;

for (int thisOrgIndex = orgIndex; thisOrgIndex <= (ddlType == M01_Common.DdlTypeId.edtPdm ? M71_Org.g_orgs.numDescriptors : orgIndex); thisOrgIndex++) {
for (int thisPoolIndex = poolIndex; thisPoolIndex <= (ddlType == M01_Common.DdlTypeId.edtPdm ? M72_DataPool.g_pools.numDescriptors : poolIndex); thisPoolIndex++) {
for (int thisSectionIndex = (sectionIndex > 0 ? sectionIndex : 1); thisSectionIndex <= (sectionIndex > 0 ? sectionIndex : M20_Section.g_sections.numDescriptors); thisSectionIndex++) {
for (int thisProcessingStep = (processingStep > 0 ? processingStep : 1); thisProcessingStep <= (processingStep > 0 ? processingStep : M01_Globals.gc_maxProcessingStep); thisProcessingStep++) {
for (int thisIncrementIndex = (incrementIndex > 0 ? incrementIndex : M00_Helper.lBound(M01_Globals.g_fileNameIncrements)); thisIncrementIndex <= (incrementIndex > 0 ? incrementIndex : M00_Helper.uBound(M01_Globals.g_fileNameIncrements)); thisIncrementIndex++) {
if (M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex] > 0) {
M00_FileWriter.closeFile(M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex]);
M20_Section.g_sections.descriptors[thisSectionIndex].fileNoDdl[(thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex)] = -1;
}
}
}
}
}
}
}

public static String genDmlFileName(String dir, int sectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int increment; 
if (incrementW == null) {
increment = 0;
} else {
increment = incrementW;
}

String returnValue;
returnValue = genXFileName(dir, sectionIndex, step, ddlType, M01_Globals.gc_fileNameSuffixDml, thisOrgIndex, thisPoolIndex, subDir, increment, null);
return returnValue;
}


public static Integer openDmlFile(String dir, int sectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, String subDirW, Integer incrementW) {
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

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

int increment; 
if (incrementW == null) {
increment = 0;
} else {
increment = incrementW;
}

Integer returnValue;
String fileName;
int fileNo;

fileName = M04_Utilities.genDmlFileName(dir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, increment);
M04_Utilities.assertDir(fileName);
fileNo = M00_FileWriter.freeFileNumber();

M00_FileWriter.openFileForOutput(fileNo, fileName, true);
//  addDxlFileHeader fileNo, baseName(fileName)
addDxlFileHeader(fileNo, fileName.substring(fileName.length() - 1 - fileName.length() - dir.length() - 1));
returnValue = fileNo;
return returnValue;
}


// ### IF IVK ###
public static String genHCfgFileName(String dir, int classIndex, Integer ddlType) {
String returnValue;
returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\hcfg" + (M01_Globals.g_genLrtSupport ? "-LRT" : "") + "\\" + M22_Class.g_classes.descriptors[classIndex].sectionName.toLowerCase() + "\\" + M22_Class.g_classes.descriptors[classIndex].className + ".hbm.xml";
return returnValue;
}


public static String genXmlExportFileName(String dir, int classIndex, Integer ddlType, Boolean forGenW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

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

String returnValue;
boolean usePsSubdirs;
usePsSubdirs = false;

String unQualTabNameNoGen;
unQualTabNameNoGen = M04_Utilities.getUnqualObjName(M04_Utilities.genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null));

returnValue = dir + (!(M03_Config.workSheetSuffix.compareTo("") == 0) ? "\\" + M03_Config.workSheetSuffix : "") + "\\" + (ddlType == M01_Common.DdlTypeId.edtLdm ? "LDM-" : "PDM-") + "xmlExport" + "\\" + M22_Class.g_classes.descriptors[classIndex].sectionName.toLowerCase() + "\\" + (usePsSubdirs &  M22_Class.g_classes.descriptors[classIndex].isPsTagged ? "[PS]\\" : "") + unQualTabNameNoGen + "\\" + M22_Class.g_classes.descriptors[classIndex].className + (forGen ? "-GEN" : "") + ".xmlExp.sql";
return returnValue;
}


// ### ENDIF IVK ###
public static String genLogFileName() {
String returnValue;
if (targetDir.compareTo("") == 0) {
targetDir = (M01_Globals.g_targetDir.compareTo("") == 0 ? M04_Utilities.dirName(M00_Excel.fileName) : M01_Globals.g_targetDir);
}

returnValue = targetDir + "\\" + M04_Utilities.baseName(M00_Excel.activeWorkbook, M01_Globals.gc_workBookSuffixes, null, null, null) + ".log";
return returnValue;
}


public static void killFile(String filePath, Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

long fileSize;
//On Error Resume Next 
fileSize = -1;
fileSize = FileLen(filePath);

if (fileSize < 0) {
return;
}

if (!(onlyIfEmpty |  fileSize == 0 | (M03_Config.generateDdlHeader &  (fileSize <= ddlEmptyFileSize)))) {
Files.delete(filePath);
}
}


public static void killCsvFileWhereEver(int sectionIndex, String entityName, String directory, Integer stepW, Boolean onlyIfEmptyW, String subDirW) {
int step; 
if (stepW == null) {
step = 0;
} else {
step = stepW;
}

boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

String subDir; 
if (subDirW == null) {
subDir = "";
} else {
subDir = subDirW;
}

//On Error Resume Next 

//On Error Resume Next 
if (M03_Config.generateLdm) {
M04_Utilities.killFile(M04_Utilities.genCsvFileName(directory, sectionIndex, entityName, step, subDir, M01_Common.DdlTypeId.edtLdm, null, null, null, null, null), onlyIfEmpty);
}

if (M03_Config.generatePdm) {
M04_Utilities.killFile(M04_Utilities.genCsvFileName(directory, sectionIndex, entityName, step, subDir, M01_Common.DdlTypeId.edtPdm, null, null, null, null, null), onlyIfEmpty);
int thisOrgIndex;
for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genCsvFileName(directory, sectionIndex, entityName, step, subDir, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, null, null, null, thisOrgIndex), onlyIfEmpty);

int thisPoolIndex;
for (thisPoolIndex = 1; thisPoolIndex <= 1; thisPoolIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genCsvFileName(directory, sectionIndex, entityName, step, subDir, M01_Common.DdlTypeId.edtPdm, thisOrgIndex, thisPoolIndex, null, null, thisOrgIndex), onlyIfEmpty);
}
}
}
NormalExit:
}


public static void dropDdlByProcessingStepSectionAndDllType(int sectionIndex, int step, Integer ddlType,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean onlyIfEmptyW) {
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

boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

if (sectionIndex < 1) {
goto NormalExit;
}

String[] suffixes = new String[2];
int numSuffixes;

suffixes[(1)] = "";
suffixes[(2)] = "Deploy";

numSuffixes = (thisOrgIndex <= 0 ? 2 : 1);

int sIndex;
int iIndex;
int i;
for (iIndex = M00_Helper.lBound(M01_Globals.g_fileNameIncrements); iIndex <= 1; iIndex += (1)) {
for (sIndex = M00_Helper.lBound(suffixes); sIndex <= 1; sIndex += (1)) {
if (suffixes[sIndex] != "-") {
for (int i = M01_Common.ldmIterationGlobal; i <= (ddlType == M01_Common.DdlTypeId.edtLdm ? M01_Common.ldmIterationPostProc : M01_Common.ldmIterationGlobal); i++) {
M04_Utilities.killFile(M04_Utilities.genDdlFileName(M01_Globals.g_targetDir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, suffixes[sIndex], M01_Globals.g_fileNameIncrements[iIndex], i), onlyIfEmpty);
}
}
}
}

String[] mSuffixes = new String[1];
mSuffixes[(1)] = "Deploy";
int[] mIncrements = new int[1];
mIncrements[(1)] = M01_Common.phaseAliases;
for (iIndex = M00_Helper.lBound(mIncrements); iIndex <= 1; iIndex += (1)) {
for (sIndex = M00_Helper.lBound(mSuffixes); sIndex <= 1; sIndex += (1)) {
M04_Utilities.killFile(M04_Utilities.genDmlFileName(M01_Globals.g_targetDir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, mSuffixes[sIndex], mIncrements[iIndex]), onlyIfEmpty);
}
}

NormalExit:
}


public static void dropDdlByProcessingStep(int step, Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

int sectIndex;
for (sectIndex = 1; sectIndex <= 1; sectIndex += (1)) {
if (!(M20_Section.g_sections.descriptors[sectIndex].isTechnical)) {
M04_Utilities.dropDdlByProcessingStepAndSection(step, M20_Section.g_sections.descriptors[sectIndex].sectionIndex, onlyIfEmpty, null);
}
}
}


public static void dropDdlByProcessingStepAndSection(int step, int sectionIndex, Boolean onlyIfEmptyW, Boolean allLevelsW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

boolean allLevels; 
if (allLevelsW == null) {
allLevels = true;
} else {
allLevels = allLevelsW;
}

if (M03_Config.generateLdm) {
M04_Utilities.dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, M01_Common.DdlTypeId.edtLdm, null, null, onlyIfEmpty);
}

if (M03_Config.generatePdm) {
M04_Utilities.dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, M01_Common.DdlTypeId.edtPdm, null, null, onlyIfEmpty);
if (allLevels) {
int orgIndex;
for (orgIndex = 1; orgIndex <= 1; orgIndex += (1)) {
M04_Utilities.dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, M01_Common.DdlTypeId.edtPdm, orgIndex, null, onlyIfEmpty);
int poolIndex;
for (poolIndex = 1; poolIndex <= 1; poolIndex += (1)) {
M04_Utilities.dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, M01_Common.DdlTypeId.edtPdm, orgIndex, poolIndex, onlyIfEmpty);
}
}
}
}
}


public static void dropDdl(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

int i;
for (i = 0; i <= 1; i += (1)) {
M04_Utilities.dropDdlByProcessingStep(i, onlyIfEmpty);
}

M71_Org.dropOrgsDdl(onlyIfEmpty);
ddlEmptyFileSize = 0;
}


public static void dropCsv(Boolean onlyIfEmptyW) {
boolean onlyIfEmpty; 
if (onlyIfEmptyW == null) {
onlyIfEmpty = false;
} else {
onlyIfEmpty = onlyIfEmptyW;
}

M23_Relationship.dropRelationshipsCsv(onlyIfEmpty);
M23_Relationship_NL.dropRelationshipsNlCsv(onlyIfEmpty);
M22_Class.dropClassesCsv(onlyIfEmpty);
M22_Class_NL.dropClassesNlCsv(onlyIfEmpty);
M21_Enum.dropEnumsCsv(onlyIfEmpty);
M21_Enum_NL.dropEnumsNlCsv(onlyIfEmpty);
M24_Attribute.dropAttributeCsv(onlyIfEmpty);
M24_Attribute_NL.dropAttributeNlCsv(onlyIfEmpty);
M25_Domain.dropDomainCsv(onlyIfEmpty);
M20_Section.dropSectionsCsv(onlyIfEmpty);
M79_Privileges.dropPrivilegesCsv(onlyIfEmpty);
M79_CleanJobs.dropCleanJobsCsv(onlyIfEmpty);
// ### IF IVK ###
M79_DataCompare.dropDCompCsv(onlyIfEmpty);
// ### ENDIF IVK ###
M78_DbProfile.dropDbCfgProfilesCsv(onlyIfEmpty);
M78_TabCfg.dropTabCfgsCsv(onlyIfEmpty);
M79_SnapshotType.dropSnapshotTypesCsv(onlyIfEmpty);
M79_SnapshotCol.dropSnapshotColsCsv(onlyIfEmpty);
M79_SnapshotFilter.dropSnapshotFilterCsv(onlyIfEmpty);
M79_Err.dropErrorCsv(onlyIfEmpty);
}


public static void evalObjects() {
M74_Container.evalContainers();
// ### IF IVK ###
M71_Org.evalOrgs();
// ### ENDIF IVK ###
M20_Section.evalSections();
M25_Domain.evalDomains();
// we need to do this before relationships since new relationships may be added here
M24_Attribute.evalAttributes();
M24_Attribute_NL.evalAttributesNl();
M22_Class.evalClasses();
M22_Class_NL.evalClassesNl();
M23_Relationship.evalRelationships();
M23_Relationship_NL.evalRelationshipsNl();
M21_Enum.evalEnums();
M21_Enum_NL.evalEnumsNl();
M76_Index.evalIndexes();
// ### IF IVK ###
M22_Class.evalClasses2();
M26_Type.evalTypes();
// ### ENDIF IVK ###
M73_TableSpace.evalTablespaces();
M75_BufferPool_Utilities.evalBufferPools();
M77_IndexAttr.evalIndexAttrs();
M79_Privileges_Utilities.evalPrivileges();
// ### IF IVK ###
M79_DataCompare.evalDComps();
// ### ENDIF IVK ###
M79_SnapshotType.evalSnapshotTypes();

// link attributes to relationships and classes
M24_Attribute.evalAttributes2();
}


public static void genAcmMetaCsv( Integer ddlType) {
M20_Section.genSectionAcmMetaCsv(ddlType);
M21_Enum.genEnumAcmMetaCsv(ddlType);
M21_Enum_NL.genEnumNlAcmMetaCsv(ddlType);
M22_Class.genClassAcmMetaCsv(ddlType);
M22_Class_NL.genClassNlAcmMetaCsv(ddlType);
M23_Relationship.genRelationshipAcmMetaCsv(ddlType);
M23_Relationship_NL.genRelationshipNlAcmMetaCsv(ddlType);
M24_Attribute.genAttributeAcmMetaCsv(ddlType);
M24_Attribute_NL.genAttributeNlAcmMetaCsv(ddlType);
M25_Domain.genDomainAcmMetaCsv(ddlType);
M79_Err.genErrorCsv(ddlType);
}


public static void genLdmMetaCsv( Integer ddlType) {
M20_Section.genSectionLdmMetaCsv(ddlType);
}


public static void genPdmMetaCsv( Integer ddlType) {
if (ddlType != M01_Common.DdlTypeId.edtPdm) {
return;
}

M20_Section.genSectionPdmMetaCsv();
}


public static void assertDir(String path) {
String dirPath;
dirPath = M04_Utilities.dirName(path);
if (dirPath.compareTo("") == 0) {
return;
}

//On Error Resume Next 
Err.Number = 0;
MkDir(dirPath);
if (Err.Number == 76) {
M04_Utilities.assertDir(dirPath);
MkDir(dirPath);
}
if (Err.Number != 75 &  Err.Number != 0) {
M04_Utilities.logMsg(Err.Number + "/" + Err.description, M01_Common.LogLevel.ellError, M01_Common.DdlTypeId.edtNone, null, null);
}
}


public static String getUnqualObjName(String qualObjName) {
String returnValue;
returnValue = qualObjName;
if (qualObjName.compareTo("") == 0) {
return returnValue;
}

int dotPos;
for (int dotPos = 1; dotPos <= qualObjName.length(); dotPos++) {
if (qualObjName.substring(dotPos - 1, dotPos + 1 - 1) == ".") {
returnValue = qualObjName.substring(qualObjName.length() - 1 - qualObjName.length() - dotPos);
return returnValue;
}
}
return returnValue;
}


public static String getSchemaName(String qualTabName) {
String returnValue;
returnValue = qualTabName;
if (qualTabName.compareTo("") == 0) {
return returnValue;
}

int dotPos;
for (int dotPos = 1; dotPos <= qualTabName.length(); dotPos++) {
if (qualTabName.substring(dotPos - 1, dotPos + 1 - 1) == ".") {
returnValue = qualTabName.substring(0, dotPos - 1);
return returnValue;
}
}
return returnValue;
}


public static String getAcmEntityTypeKey(Integer acmEntityType) {
String returnValue;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M01_Globals.gc_acmEntityTypeKeyClass;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactEnum) {
returnValue = M01_Globals.gc_acmEntityTypeKeyEnum;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M01_Globals.gc_acmEntityTypeKeyRel;
// ### IF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactType) {
returnValue = M01_Globals.gc_acmEntityTypeKeyType;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactView) {
returnValue = M01_Globals.gc_acmEntityTypeKeyView;
}
return returnValue;
}



public static String logLevelId(Integer logLvl) {
String returnValue;
returnValue = "";

if (logLvl == M01_Common.LogLevel.ellFatal) {
returnValue = "F";
} else if (logLvl == M01_Common.LogLevel.ellError) {
returnValue = "E";
} else if (logLvl == M01_Common.LogLevel.ellWarning) {
returnValue = "W";
} else if (logLvl == M01_Common.LogLevel.ellFixableWarning) {
returnValue = "w";
} else if (logLvl == M01_Common.LogLevel.ellInfo) {
returnValue = "I";
}
return returnValue;
}


public static void logMsg(String msg, Integer logLvl, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

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

if (logLvl &  M01_Globals.g_logLevelsReport) {
int fileNo;
String fileName;
fileName = M04_Utilities.genLogFileName();
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, true);

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


String msgPrefix;
msgPrefix = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()) + " [" + M04_Utilities.logLevelId(logLvl) + "]: " + M02_ToolMeta.getDdlTypeDescr(ddlType) + (M01_Globals.g_genLrtSupport ? "[LRT]" : "");

//On Error Resume Next 
if (ddlType == M01_Common.DdlTypeId.edtLdm |  ddlType == M01_Common.DdlTypeId.edtNone) {
M00_FileWriter.printToFile(fileNo, msgPrefix + ": " + msg);
} else {
M00_FileWriter.printToFile(fileNo, msgPrefix + "[" + String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id) + "|" + String.valueOf(thisPoolId) + "] : " + msg);
}
M00_FileWriter.closeFile(fileNo);
}

if (logLvl &  M01_Globals.g_logLevelsMsgBox) {
VbMsgBoxStyle button;
if (logLvl == M01_Common.LogLevel.ellFatal) {
button = vbCritical |  vbExclamation;
} else if (logLvl == M01_Common.LogLevel.ellError) {
button = vbCritical;
} else if (logLvl == M01_Common.LogLevel.ellWarning) {
button = vbExclamation;
}

;
//wait 2
}
}


public static String genSheetFileName(String dir, String sheetName, String subDirNameW) {
String subDirName; 
if (subDirNameW == null) {
subDirName = "Sheets";
} else {
subDirName = subDirNameW;
}

String returnValue;
returnValue = dir + "\\" + (subDirName != "" ? subDirName + "\\" : "") + sheetName + ".csv";
return returnValue;
}


public static void exportSheetsByWorkbook(Workbook thisWorkbook) {
String targetDir;
String fileName;
int fileNo;

targetDir = M04_Utilities.dirName(M00_Excel.fileName) + "\\" + M04_Utilities.baseName(thisWorkbook, M01_Globals.gc_workBookSuffixes, null, null, null);
fileName = M04_Utilities.genSheetFileName(targetDir, "XXX", null);
M04_Utilities.assertDir(fileName);

M04_Utilities.logMsg("exporting Excel-Sheets to \"" + M04_Utilities.dirName(fileName) + "\"", M01_Common.LogLevel.ellInfo, M01_Common.DdlTypeId.edtNone, null, null);

//On Error GoTo ErrorExit 
int sheetNum;
int rowNum;
int colNum;
int numEmpty;
int i;
for (int sheetNum = 1; sheetNum <= thisWorkbook..; sheetNum++) {
fileName = M04_Utilities.genSheetFileName(targetDir, thisWorkbook.getSheet(sheetNum)., null);
fileNo = M00_FileWriter.freeFileNumber();
M00_FileWriter.openFileForOutput(fileNo, fileName, false);
rowNum = 1;
while (M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, 1).getStringCellValue() != "" |  M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, 1).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, 3).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, 4).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, 5).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 1, 1).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 1, 1).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 1, 3).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 1, 4).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 1, 5).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 2, 1).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 2, 1).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 2, 3).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 2, 4).getStringCellValue() != "" | M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum + 2, 5).getStringCellValue() != "") {
numEmpty = 0;
for (int colNum = 1; colNum <= 100; colNum++) {
if (String.valueOf(M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, colNum).getStringCellValue()) == "") {
numEmpty = numEmpty + 1;
} else {
for (int i = 1; i <= numEmpty; i++) {
M00_FileWriter.printToFile(fileNo, ";");
}
numEmpty = 0;
M00_FileWriter.printToFile(fileNo, "\"" + M00_Helper.replace(String.valueOf(M00_Excel.getCell(thisWorkbook.getSheet(sheetNum), rowNum, colNum).getStringCellValue()), "\"", "\"\"") + "\";");
}
}
M00_FileWriter.printToFile(fileNo, "");

rowNum = rowNum + 1;
}
M00_FileWriter.closeFile(fileNo);
}

NormalExit:
//On Error Resume Next 
M00_FileWriter.closeFile(fileNo);

return;
ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void exportSheets() {

M04_Utilities.exportSheetsByWorkbook(M00_Excel.activeWorkbook);
}

public static String getCsvTrailer(int numCommas) {
String returnValue;
String str;
str = "";
int i;
for (int i = 1; i <= numCommas; i++) {
str = str + ",";
}
returnValue = str + "1";
return returnValue;
}


public static Integer pullOid() {
Integer returnValue;
final int firstOid = 8000;
final int lastOid = 14999;

if (M04_Utilities.nextOid == 0) {
M04_Utilities.nextOid = firstOid;
}

returnValue = M04_Utilities.nextOid;
M04_Utilities.nextOid = M04_Utilities.nextOid + 1;
return returnValue;
}


public static void resetOid() {
M04_Utilities.nextOid = 0;
}


public static Boolean arrayIsNull(M24_Attribute_Utilities.AttributeMappingForCl[] arr) {
Boolean returnValue;
returnValue = false;
//On Error GoTo ErrorExit 
int i;
i = M00_Helper.lBound(arr);

NormalExit:
return returnValue;

ErrorExit:
returnValue = true;
return returnValue;
}


public static Boolean strArrayIsNull(String[] arr) {
Boolean returnValue;
returnValue = false;
//On Error GoTo ErrorExit 
int i;
i = M00_Helper.lBound(arr);

NormalExit:
return returnValue;

ErrorExit:
returnValue = true;
return returnValue;
}


public static String getWorkSheetName(String workSheetBaseName, String suffix) {
String returnValue;
returnValue = workSheetBaseName;
if (suffix + "".trim().compareTo("") == 0) {
return returnValue;
}

Sheet ws;

//On Error GoTo ErrorExit 
ws = M00_Excel.activeWorkbook.getSheet(workSheetBaseName + "." + suffix);

returnValue = workSheetBaseName + "." + suffix;

ErrorExit:
return returnValue;
return returnValue;
}


public static String genSrxType2Str(Integer srxType) {
String returnValue;
if (srxType == M01_Common.SrxTypeId.estSr0) {
returnValue = "SR0";
} else if (srxType == M01_Common.SrxTypeId.estSr1) {
returnValue = "SR1";
} else if (srxType == M01_Common.SrxTypeId.estNsr1) {
returnValue = "NSR1";
} else {
returnValue = "- unknown -";
}
return returnValue;
}


public static String getPrimaryEntityLabelByIndex(Integer acmEntityType, int acmEntityIndex) {
String returnValue;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
returnValue = M22_Class_Utilities_NL.getPrimaryClassLabelByIndex(acmEntityIndex);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
returnValue = M23_Relationship_Utilities_NL.getPrimaryRelationshipLabelByIndex(acmEntityIndex);
} else {
returnValue = "<unknown entity type>";
}
return returnValue;
}


public static Boolean listHasPostiveElement( String list) {
Boolean returnValue;
returnValue = false;
String[] elems;
elems = list.split(",");

int i;
for (int i = M00_Helper.lBound(elems); i <= M00_Helper.uBound(elems); i++) {
if (elems[i] > 0) {
returnValue = true;
return returnValue;
}
}
return returnValue;
}


public static Boolean includedInList( String list, int element) {
Boolean returnValue;
list = M00_Helper.replace(list, " ", "");
list = M00_Helper.replace(list, ".", ",");

if ((element < 0) |  (list.compareTo("") == 0)) {
returnValue = true;
} else {
String[] elems;
elems = list.split(",");

int i;
for (int i = M00_Helper.lBound(elems); i <= M00_Helper.uBound(elems); i++) {
if (elems[i] == ("-" + String.valueOf(element))) {
returnValue = false;
return returnValue;
} else if (elems[i] == String.valueOf(element)) {
returnValue = true;
return returnValue;
}
}
returnValue = list.substring(0, 1) == "-";
}
return returnValue;
}


public static void printConditional(int fileNo, String line, Boolean conditionW, Integer indentW) {
boolean condition; 
if (conditionW == null) {
condition = true;
} else {
condition = conditionW;
}

int indent; 
if (indentW == null) {
indent = 0;
} else {
indent = indentW;
}

if (condition &  (!(line.compareTo("") == 0))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent) + line);
}
}



}