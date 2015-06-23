package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M12_ChangeLog {




private static final String pc_tempTabNameChangeLogCte = "SESSION.cte_bas";

private static final String pc_tempTabNameChangeLogAc = "SESSION.ChangeLog_AC";

public static final int clNlPrioAggHead = 0;
public static final int clNlPrioNonAggHead = 100;
public static final int clNlPrioOther = 200;

public class ChangeLogColumnType {
public static final int clValueTypeInteger = 1;
public static final int clValueTypeBoolean = 2;
public static final int clValueTypeTimeStamp = 3;
public static final int clValueTypeString = 4;
public static final int clValueTypeDecimal = 5;
public static final int clValueTypeBigInteger = 6;
public static final int clValueTypeDate = 7;
}

public class ChangeLogMode {
public static final int eclLrt = 0;
// ### IF IVK ###
public static final int eclSetProd = 1;
// ### ENDIF IVK ###
public static final int eclPubUpdate = 2;
// ### IF IVK ###
public static final int eclPubMassUpdate = 3;
// ### ENDIF IVK ###
}

public static Boolean isClAttrCat(Integer columnCategory, boolean includeSetProdMeta) {
Boolean returnValue;
if (columnCategory &  M01_Common.AttrCategory.eacCid) {
returnValue = false;
} else if (columnCategory &  M01_Common.AttrCategory.eacOid) {
returnValue = false;
// ### IF IVK ###
} else if (columnCategory &  M01_Common.AttrCategory.eacPsOid) {
returnValue = false;
} else if (columnCategory &  M01_Common.AttrCategory.eacGroupId) {
returnValue = false;
// ### ENDIF IVK ###
} else if (columnCategory &  M01_Common.AttrCategory.eacFkOidParent) {
returnValue = false;
// ### IF IVK ###
} else if (includeSetProdMeta &  (columnCategory &  M01_Common.AttrCategory.eacSetProdMeta)) {
returnValue = true;
} else if (columnCategory &  M01_Common.AttrCategory.eacNationalBool) {
returnValue = true;
// ### ENDIF IVK ###
} else if (columnCategory &  M01_Common.AttrCategory.eacLrtMeta) {
returnValue = false;
} else if (columnCategory &  M01_Common.AttrCategory.eacRegular) {
returnValue = true;
} else if (columnCategory &  M01_Common.AttrCategory.eacFkOid) {
returnValue = true;
// ### IF IVK ###
} else if (columnCategory &  M01_Common.AttrCategory.eacExpression) {
returnValue = true;
} else if (columnCategory &  M01_Common.AttrCategory.eacFkOidExpression) {
returnValue = true;
// ### ENDIF IVK ###
} else {
returnValue = false;
}
return returnValue;
}


public static String genClModeDescription(Integer clMode) {
String returnValue;
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
returnValue = "LRT";
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
returnValue = "SetProductive";
// ### ENDIF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
returnValue = "public update";
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
returnValue = "public mass update";
// ### ENDIF IVK ###
} else {
returnValue = "-unsupported-";
}
return returnValue;
}


public static Boolean attrTypeMapsToClColType(Integer attrTypeId, Integer clColTypeId) {
Boolean returnValue;
if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger) {
returnValue = (attrTypeId == M01_Common.typeId.etInteger |  attrTypeId == M01_Common.typeId.etSmallint);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean) {
returnValue = (attrTypeId == M01_Common.typeId.etBoolean);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp) {
returnValue = (attrTypeId == M01_Common.typeId.etTimestamp);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeDate) {
returnValue = (attrTypeId == M01_Common.typeId.etDate);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger) {
returnValue = (attrTypeId == M01_Common.typeId.etBigInt);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal) {
returnValue = (attrTypeId == M01_Common.typeId.etDecimal);
} else if (clColTypeId == M12_ChangeLog.ChangeLogColumnType.clValueTypeString) {
returnValue = true;
}
return returnValue;
}

public static Integer getClColTypeByAttrType(Integer attrTypeId) {
Integer returnValue;
if (attrTypeId == M01_Common.typeId.etBoolean) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean;
} else if (attrTypeId == M01_Common.typeId.etTimestamp) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp;
} else if (attrTypeId == M01_Common.typeId.etDate) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeDate;
} else if (attrTypeId == M01_Common.typeId.etBigInt) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger;
} else if (attrTypeId == M01_Common.typeId.etInteger |  attrTypeId == M01_Common.typeId.etSmallint) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger;
} else if (attrTypeId == M01_Common.typeId.etDecimal |  attrTypeId == M01_Common.typeId.etDouble | attrTypeId == M01_Common.typeId.etFloat) {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal;
} else {
returnValue = M12_ChangeLog.ChangeLogColumnType.clValueTypeString;
}
return returnValue;
}


// ### IF IVK ###
public static void genDdlForTempTablesChangeLog(int fileNo,  int thisOrgIndex,  int thisPoolIndex, Integer ddlType, Integer indentW, Boolean includeNlTabW, Boolean includeAeTabW, Boolean includeStTabW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW, Boolean prioColumnInNlTabW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean includeNlTab; 
if (includeNlTabW == null) {
includeNlTab = true;
} else {
includeNlTab = includeNlTabW;
}

boolean includeAeTab; 
if (includeAeTabW == null) {
includeAeTab = false;
} else {
includeAeTab = includeAeTabW;
}

boolean includeStTab; 
if (includeStTabW == null) {
includeStTab = false;
} else {
includeStTab = includeStTabW;
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

boolean prioColumnInNlTab; 
if (prioColumnInNlTabW == null) {
prioColumnInNlTab = true;
} else {
prioColumnInNlTab = prioColumnInNlTabW;
}

// ### ELSE IVK ###
//Sub genDdlForTempTablesChangeLog( _
// fileNo As Integer, _
// thisOrgIndex As Integer, _
// thisPoolIndex As Integer, _
// ddlType As DdlTypeId, _
// Optional indent As Integer = 1, _
// Optional includeNlTab As Boolean = True, _
// Optional includeAeTab As Boolean = False, _
// Optional withReplace As Boolean = False, _
// Optional onCommitPreserve As Boolean = False, _
// Optional onRollbackPreserve As Boolean = False, _
// Optional prioColumnInNlTab As Boolean = True _
//)
// ### ENDIF IVK ###
String qualTabNameChangeLog;
qualTabNameChangeLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for ChangeLog", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LIKE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameChangeLog);
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

if (includeNlTab) {
String qualTabNameChangeLogNl;
qualTabNameChangeLogNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, true, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "temporary NL-Text table for ChangeLog", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
if (prioColumnInNlTab) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M22_Class_Utilities.printComment("the same NL-text column may be filled from different sources - column \"" + M01_ACM.conTmpPrio + "\" defines priorities", fileNo, M01_Common.DdlOutputMode.edomDeclNonLrt, indent + 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genAttrDeclByDomain(M01_ACM.conTmpPrio, M01_ACM.cosnTmpPrio, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals_IVK.g_domainIndexTmpPrio, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.g_classIndexChangeLog, "DEFAULT " + String.valueOf(M12_ChangeLog.clNlPrioOther), null, ddlType, null, M01_Common.DdlOutputMode.edomDeclNonLrt |  M01_Common.DdlOutputMode.edomNoDdlComment, M01_Common.AttrCategory.eacRegular, null, 0, false, null));

M24_Attribute.genNlsAttrDeclsForEntity(M01_Globals.g_classIndexChangeLog, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, indent + 1, null, null, M01_Common.DdlOutputMode.edomDeclNonLrt |  M01_Common.DdlOutputMode.edomNoDdlComment | M01_Common.DdlOutputMode.edomNoSpecifics, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LIKE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameChangeLogNl);
}

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}
// ### IF IVK ###

if (includeStTab) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for ChangeLog - status", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameChangeLogStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "psOid            " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "divisionOid      " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

if (includeAeTab) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for optimized ChangeLog", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + pc_tempTabNameChangeLogCte);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "objectid          " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dbcolumnname     " + M01_Globals.g_dbtDbColumnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "switch          " + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, true, onCommitPreserve, onRollbackPreserve);
}
// ### ENDIF IVK ###
}


// ### IF IVK ###
public static void genDdlForTempChangeLogSummary(int fileNo, Integer indentW, Boolean forOrgW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forOrg; 
if (forOrgW == null) {
forOrg = false;
} else {
forOrg = forOrgW;
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for ChangeLog-Summary", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (forOrg ? M01_Globals_IVK.gc_tempTabNameChangeLogOrgSummary : M01_Globals_IVK.gc_tempTabNameChangeLogSummary));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "objectId        " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityType      " + M01_Globals.g_dbtEntityType + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityId        " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahClassId       " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahObjectId      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "aggregateType   " + M01_Globals.g_dbtEntityId + ",");

if (forOrg) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahIsCreated     " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahIsUpdated     " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahIsDeleted     " + M01_Globals.g_dbtBoolean + ",");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isCreated       " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isUpdated       " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isDeleted       " + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}


public static void genDdlForTempFtoClgGenericAspect(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW, Boolean includeSr0ContextTabOrgW, Boolean includeSr0ContextTabCmpW) {
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

boolean includeSr0ContextTabOrg; 
if (includeSr0ContextTabOrgW == null) {
includeSr0ContextTabOrg = true;
} else {
includeSr0ContextTabOrg = includeSr0ContextTabOrgW;
}

boolean includeSr0ContextTabCmp; 
if (includeSr0ContextTabCmpW == null) {
includeSr0ContextTabCmp = true;
} else {
includeSr0ContextTabCmp = includeSr0ContextTabCmpW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Countries managed by 'this Organization'", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameManagedCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "countryOid     " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for Countries relevant for 'this Organization'", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameRelevantCountry);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "countryOid     " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for CountryId-Lists involving Countries relevant for 'this Organization'", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdList);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "idListOid      " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for mapping of CountryId-Lists to Countries managed by 'this Organization'", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameRelevantCountryIdXRef);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "idListOid      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "countryOid     " + M01_Globals.g_dbtOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

if (includeSr0ContextTabCmp) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for SR0-Contexts (factory) revelant for 'this Organization'", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameFtoClSr0ContextFac);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Context     VARCHAR(50)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

if (includeSr0ContextTabOrg) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for SR0-Contexts (MPC) revelant for 'this Organization", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameFtoClSr0ContextOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Context     VARCHAR(50)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}
}


public static void genDdlForTempImplicitChangeLogSummary(int fileNo, Integer indentW, Boolean forOrgW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean forOrg; 
if (forOrgW == null) {
forOrg = false;
} else {
forOrg = forOrgW;
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

if (forOrg) {
M11_LRT.genProcSectionHeader(fileNo, "temporary table for implicit changes", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameChangeLogImplicitChanges);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "aggregateType   " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahClassId       " + M01_Globals.g_dbtEntityId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahObjectId      " + M01_Globals.g_dbtOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isToBeCreated   " + M01_Globals.g_dbtBoolean + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isToBeDeleted   " + M01_Globals.g_dbtBoolean);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}
}


public static Integer genAddNlTextChangeLogDdlForIndividualAttrs(int fileNo, int acmEntityIndex, Integer acmEntityType, String dbAcmEntityType, String entityIdStrList, String M01_Globals.gc_tempTabNameChangeLog, String M01_Globals.gc_tempTabNameChangeLogNl, String qualRefNlTabName, String oidRefAttrName, String qualAggHeadRefNlTabName, String aggHeadOidRefAttrName, M24_Attribute_Utilities.AttrDescriptorRefs attrRefs, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, boolean forGen, String lrtOidFilterVar, String psOidFilterVar,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Boolean includeChangeCommentW, Boolean includeRegularAttrsW, Integer ddlTypeW, Integer indentW, Boolean skipNlW) {
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

boolean includeChangeComment; 
if (includeChangeCommentW == null) {
includeChangeComment = false;
} else {
includeChangeComment = includeChangeCommentW;
}

boolean includeRegularAttrs; 
if (includeRegularAttrsW == null) {
includeRegularAttrs = true;
} else {
includeRegularAttrs = includeRegularAttrsW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

Integer returnValue;
// ### ELSE IVK ###
//Function genAddNlTextChangeLogDdlForIndividualAttrs( _
// fileNo As Integer, _
// ByRef acmEntityIndex As Integer, _
// ByRef acmEntityType As AcmAttrContainerType, _
// ByRef dbAcmEntityType As String, _
// ByRef entityIdStrList As String, _
// ByRef gc_tempTabNameChangeLog As String, _
// ByRef gc_tempTabNameChangeLogNl As String, _
// ByRef qualRefNlTabName As String, _
// ByRef oidRefAttrName As String, _
// ByRef qualAggHeadRefNlTabName As String, _
// ByRef aggHeadOidRefAttrName As String, _
// ByRef attrRefs As AttrDescriptorRefs, _
// ByRef relRefs As RelationshipDescriptorRefs, _
// ByRef forGen As Boolean, _
// ByRef lrtOidFilterVar As String, _
// Optional ByVal thisOrgIndex As Integer = -1, _
// Optional ByVal thisPoolIndex As Integer = -1, _
// Optional ByRef includeChangeComment As Boolean = False, _
// Optional ByRef includeRegularAttrs As Boolean = True, _
// Optional ByRef ddlType As DdlTypeId = edtLdm, _
// Optional ByRef indent As Integer = 1, _
// Optional skipNl As Boolean = False _
//) As Integer
// ### ENDIF IVK ###
returnValue = 0;

M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsAh;
int aggHeadClassIndex;
boolean M03_Config.useMqtToImplementLrt;
Integer clMode;
boolean isAggHead;
boolean implicitelyGenChangeComment;
boolean hasNl;
boolean useClassIdFilter;
// ### IF IVK ###
boolean hasNoIdentity;
boolean enforceLrtChangeComment;
boolean isPsTagged;
// ### ENDIF IVK ###

useClassIdFilter = true;
// ### IF IVK ###
clMode = (lrtOidFilterVar.compareTo("") == 0 ? M12_ChangeLog.ChangeLogMode.eclSetProd : M12_ChangeLog.ChangeLogMode.eclLrt);
// ### ELSE IVK ###
//  clMode = eclLrt
// ### ENDIF IVK ###

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
isAggHead = M22_Class.g_classes.descriptors[acmEntityIndex].isAggHead;
implicitelyGenChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].implicitelyGenChangeComment;
hasNl = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses;

// ### IF IVK ###
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
enforceLrtChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].enforceLrtChangeComment;
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
useClassIdFilter = useClassIdFilter &  (M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() != M01_ACM_IVK.clnGenericAspect.toUpperCase());// list of class-IDs too long
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
isAggHead = false;
// ### IF IVK ###
hasNoIdentity = false;
enforceLrtChangeComment = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
// ### ENDIF IVK ###
} else {
return returnValue;
}

int i;
int j;
String columnName;
int columnMaxLengthSrc;
int columnMaxLengthDst;
boolean addNewLine;
addNewLine = false;

String isolationLevelSuffix;
isolationLevelSuffix = "WITH UR";// with DB2 V8 it is advised to use uncommitted read for NL-Texts to avoid lock-conflicts

int numAttrsFound;
numAttrsFound = 0;

String tabVarNl;
String tabVarGen;

// ### IF IVK ###
if (includeChangeComment & ! forGen & ((isAggHead &  (implicitelyGenChangeComment |  hasNl)) |  (enforceLrtChangeComment &  clMode == M12_ChangeLog.ChangeLogMode.eclLrt))) {
// ### ELSE IVK ###
// If includeChangeComment And Not forGen And isAggHead Then
// ### ENDIF IVK ###
columnName = M01_Globals_IVK.g_anChangeComment;

columnMaxLengthSrc = M25_Domain.getDbMaxDataTypeLengthByDomainName(M01_ACM.dxnChangeComment, M01_ACM.dnChangeComment);
columnMaxLengthDst = M24_Attribute.getMaxDbAttributeLengthByNameAndEntityIndex(columnName, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.g_classIndexChangeLog, null);

M11_LRT.genProcSectionHeader(fileNo, "add NL-Text-Column \"" + columnName + "\" to changelog entries", indent, skipNl);

tabVarNl = (isAggHead ? "AHNL" : "NL");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "language_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIO,");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_anPsOid + ",");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + ",");
if (columnMaxLengthSrc <= columnMaxLengthDst) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(LEFT(" + tabVarNl + "." + columnName + "," + String.valueOf(columnMaxLengthDst) + ")),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + String.valueOf((isAggHead ? M12_ChangeLog.clNlPrioAggHead : M12_ChangeLog.clNlPrioNonAggHead)) + ",");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals_IVK.g_anPsOid + ",");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualAggHeadRefNlTabName + " " + tabVarNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + aggHeadOidRefAttrName + " = L.ahObjectId");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualRefNlTabName + " " + tabVarNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + oidRefAttrName + " = L.objectId");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + " IS NOT NULL");
if (useClassIdFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahClassid IN (" + entityIdStrList + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityId = " + entityIdStrList);
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + isolationLevelSuffix + ";");

addNewLine = true;
numAttrsFound = numAttrsFound + 1;
}

tabVarNl = "ONL";
tabVarGen = "OGEN";

if (includeRegularAttrs) {
for (int i = 1; i <= attrRefs.numDescriptors; i++) {
if (attrRefs.descriptors[i].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute) {
// ### IF IVK ###
if ((!(M04_Utilities.strArrayIsNull(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes))) &  M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isNl & (forGen |  hasNoIdentity) == M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].isTimeVarying) {
// ### ELSE IVK ###
//         If (Not strArrayIsNull(.mapsToChangeLogAttributes)) And .isNl And forGen = .isTimeVarying Then
// ### ENDIF IVK ###
for (int j = M00_Helper.lBound(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes); j <= M00_Helper.uBound(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes); j++) {
columnName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].attributeName, ddlType, null, null, null, null, null, null);

columnMaxLengthSrc = M24_Attribute.getMaxDbAttributeLengthByNameAndEntityIndex(columnName, acmEntityType, acmEntityIndex, null);
columnMaxLengthDst = M24_Attribute.getMaxDbAttributeLengthByNameAndEntityIndex(M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes[j], M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.g_classIndexChangeLog, null);

M11_LRT.genProcSectionHeader(fileNo, "add NL-Text-Column \"" + columnName + "\" / \"" + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes[j].toUpperCase() + "\" to changelog entries", indent, !(addNewLine));

// propagate NL-text values to all aggregate elements
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M24_Attribute.g_attributes.descriptors[attrRefs.descriptors[i].refIndex].mapsToChangeLogAttributes[j].toUpperCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIO,");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_anPsOid + ",");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + ",");
if (columnMaxLengthSrc <= columnMaxLengthDst) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(LEFT(" + tabVarNl + "." + columnName + "," + String.valueOf(columnMaxLengthDst) + ")),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (isAggHead ? "(CASE WHEN " + tabVarNl + "." + oidRefAttrName + " = " + tabVarNl + "." + M01_Globals.g_anAhOid + " THEN " + M12_ChangeLog.clNlPrioNonAggHead + " ELSE " + M12_ChangeLog.clNlPrioAggHead + " END)" : String.valueOf(M12_ChangeLog.clNlPrioNonAggHead)) + ",");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals_IVK.g_anPsOid + ",");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L,");

if (!(isAggHead &  forGen)) {
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M03_Config.useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, false, null, null, null) + " " + tabVarNl);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + oidRefAttrName + " FROM " + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, false, false, null, null, null) + " " + "WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)");
// ### ELSE IVK ###
//                   Print #fileNo, addTab(indent + 2); "SELECT OID,"; oidRefAttrName; " FROM "; _
//                                                      genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, False); " "; _
//                                                      "WHERE ("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + oidRefAttrName + " FROM " + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, false, false, null, null, null) + " " + "WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarGen + ",");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, false, null, null, null) + " " + tabVarGen + ",");
}
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M03_Config.useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, true, true, null, null, null) + " " + tabVarNl);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + oidRefAttrName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, false, true, null, null, null) + " " + "WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)");
// ### ELSE IVK ###
//                 Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; oidRefAttrName; ","; columnName; ","; g_anLanguageId; " FROM "; _
//                                                    genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True); " "; _
//                                                    "WHERE ("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + oidRefAttrName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, true, false, true, null, null, null) + " " + "WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarNl);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, null, null) + " " + tabVarNl);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");

if (!(isAggHead &  forGen)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + oidRefAttrName + " = " + tabVarGen + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
}

if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(L.ahObjectId = " + tabVarNl + "." + M01_Globals.g_anAhOid + ")");
if (useClassIdFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahClassId IN (" + entityIdStrList + ")");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(L.objectId = " + tabVarNl + "." + oidRefAttrName + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(L.objectId = " + tabVarNl + "." + M01_Globals.g_anOid + ")");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(L.objectId = " + tabVarGen + "." + oidRefAttrName + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
if (useClassIdFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityId = " + entityIdStrList);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M03_Config.useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarNl + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarNl + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
// ### ELSE IVK ###
//                 Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "." ; g_anIsLrtPrivate; " = 0) AND ("; tabVarNl; "."; g_anInLrt; " IS NULL OR "; tabVarNl; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + isolationLevelSuffix + ";");

addNewLine = true;
numAttrsFound = numAttrsFound + 1;
}
}
}
}

// ### IF IVK ###
int referredClassIndex;
int referringClassIndex;
String entityIdStrListForRel;
int attrIndex;
entityIdStrListForRel = "";
String relFkAttrName;
int attrBasePrio;

if (!(forGen)) {
for (int i = 1; i <= relRefs.numRefs; i++) {
if (!(M04_Utilities.arrayIsNull(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute) &  M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reusedRelIndex <= 0)) {
if (relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight) {
referredClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].leftEntityIndex;
referringClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].rightEntityIndex;
relFkAttrName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].leftFkColName[ddlType];
} else {
referredClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].rightEntityIndex;
referringClassIndex = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].leftEntityIndex;
relFkAttrName = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].rightFkColName[ddlType];
}

String refColName;
refColName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[referredClassIndex].shortName, null, null, null, null);

if (referringClassIndex > 0) {
// determine list of subClassIDs for which this relationship applies
entityIdStrListForRel = M22_Class.g_classes.descriptors[referringClassIndex].subclassIdStrListNonAbstract;
// examine reusing relationships
int l;
for (int l = 1; l <= M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reusingRelIndexes.numIndexes; l++) {
M22_Class_Utilities.addClassIdToList(entityIdStrListForRel, (relRefs.refs[i].refType == M01_Common.RelNavigationDirection.etRight ? M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reusingRelIndexes.indexes[l]].rightEntityIndex : M23_Relationship.g_relationships.descriptors[M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].reusingRelIndexes.indexes[l]].leftEntityIndex), true);
}
}

for (int j = M00_Helper.lBound(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute); j <= M00_Helper.uBound(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute); j++) {
attrBasePrio = M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].prio;
attrIndex = M24_Attribute.getAttributeIndexByName(M22_Class.g_classes.descriptors[referredClassIndex].sectionName, M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapTo);

if (attrIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[attrIndex].isNl) {
columnName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[attrIndex].attributeName, ddlType, null, null, null, null, null, null);
columnMaxLengthSrc = M24_Attribute.getMaxDbAttributeLengthByNameAndEntityIndex(columnName, M24_Attribute.g_attributes.descriptors[attrIndex].cType, M24_Attribute.g_attributes.descriptors[attrIndex].acmEntityIndex, null);
columnMaxLengthDst = M24_Attribute.getMaxDbAttributeLengthByNameAndEntityIndex(M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, M01_Globals.g_classIndexChangeLog, null);

String tabVarRef;
refColName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[referredClassIndex].orMappingSuperClassIndex].shortName, null, null, null, null);
tabVarRef = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[referringClassIndex].orMappingSuperClassIndex].shortName.toUpperCase();

if (M24_Attribute.g_attributes.descriptors[attrIndex].isTimeVarying) {
tabVarNl = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[referredClassIndex].orMappingSuperClassIndex].shortName.toUpperCase() + "GNL";
tabVarGen = M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[referredClassIndex].orMappingSuperClassIndex].shortName.toUpperCase() + "G";

M11_LRT.genProcSectionHeader(fileNo, "add NL-Text-Column \"" + M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom.toUpperCase() + "\" (" + M24_Attribute.g_attributes.descriptors[attrIndex].attributeName.toUpperCase() + " @ " + M22_Class.g_classes.descriptors[referredClassIndex].className + " (TV)) to changelog entries", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom.toUpperCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIO,");
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_anPsOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + ",");

if (columnMaxLengthSrc <= columnMaxLengthDst) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(LEFT(" + tabVarNl + "." + columnName + "," + String.valueOf(columnMaxLengthDst) + ")),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (isAggHead ? "(CASE WHEN " + tabVarNl + "." + refColName + " = " + tabVarNl + "." + M01_Globals.g_anAhOid + " THEN " + (attrBasePrio + M12_ChangeLog.clNlPrioNonAggHead) + " ELSE " + (attrBasePrio + M12_ChangeLog.clNlPrioAggHead) + " END)" : String.valueOf(attrBasePrio + M12_ChangeLog.clNlPrioNonAggHead)) + ",");
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals_IVK.g_anPsOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");// Join with 'referring table'

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referringClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, true, null, null) + " " + tabVarRef);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, false, true, null, null) + " WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)" + (M22_Class.g_classes.descriptors[referringClassIndex].isPsTagged ? " AND (" + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterVar + ")" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, false, false, true, null, null) + " WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarRef);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, false, true, null, null) + " " + tabVarRef);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahObjectId = " + tabVarRef + "." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.objectId = " + tabVarRef + "." + M01_Globals.g_anOid);
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referringClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarRef + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarRef + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarRef + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarRef + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarRef + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarRef + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");// Join with GEN of 'referred table'

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + refColName + ",");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anIsLrtPrivate + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals_IVK.g_anIsDeleted + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anInLrt + ",");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "ROWNUMBER() OVER (PARTITION BY " + refColName + " ORDER BY (CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)) AS ROWNUM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SELECT OID," + M01_Globals_IVK.g_anValidTo + "," + refColName + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt ? "," + M01_Globals.g_anIsLrtPrivate + "," + M01_Globals_IVK.g_anIsDeleted + "," + M01_Globals.g_anInLrt + "" : "") + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, false, true, null, null) + " " + "WHERE ((" + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + M01_Globals.g_anInLrt + " IS NULL OR " + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) " + "OR " + "((" + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))" + (M22_Class.g_classes.descriptors[referredClassIndex].isPsTagged ? " AND (" + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterVar + ")" : ""));
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SELECT OID," + M01_Globals_IVK.g_anValidTo + "," + refColName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, false, false, false, true, null, null) + " WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)" + (M22_Class.g_classes.descriptors[referredClassIndex].isPsTagged ? " AND (" + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterVar + ")" : ""));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 5) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SELECT OID," + M01_Globals_IVK.g_anValidTo + "," + refColName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, false, false, true, null, null) + " WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SELECT OID," + M01_Globals_IVK.g_anValidTo + "," + refColName + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, false, true, null, null));
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + ") " + tabVarGen + "_ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarGen);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarRef + "." + relFkAttrName + " = " + tabVarGen + "." + refColName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarGen + ".ROWNUM = 1");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarGen + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarGen + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarGen + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarGen + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarGen + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarGen + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");// Join with NL-TEXT of GEN of 'referred table'
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, true, true, true, null, null) + " " + tabVarNl);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, false, false, true, true, null, null) + " WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, true, false, true, true, null, null) + " WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarNl);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, true, null, null, true, true, null, null) + " " + tabVarNl);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + refColName + " = " + tabVarGen + "." + M01_Globals.g_anOid);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarNl + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarNl + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
//include only reference object labels in german and english
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + " IN (" + M01_Globals_IVK.gc_langIdGerman + "," + M01_Globals_IVK.gc_langIdEnglish + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + " IS NOT NULL");

if (useClassIdFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahClassid IN (" + entityIdStrList + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityId = " + entityIdStrList);
}
}

if (isPsTagged) {
if (!((clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  M03_Config.useMqtToImplementLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarRef + "." + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterVar);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + isolationLevelSuffix + ";");
} else {
tabVarNl = M22_Class.g_classes.descriptors[referredClassIndex].shortName.toUpperCase() + "NL";

M11_LRT.genProcSectionHeader(fileNo, "add NL-Text-Column \"" + M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom.toUpperCase() + "\" (" + M24_Attribute.g_attributes.descriptors[attrIndex].attributeName.toUpperCase() + " @ " + M22_Class.g_classes.descriptors[referredClassIndex].className + ") to changelog entries", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M23_Relationship.g_relationships.descriptors[relRefs.refs[i].refIndex].refersToClAttribute[j].mapFrom.toUpperCase() + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "PRIO,");
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_anPsOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + ",");

if (columnMaxLengthSrc <= columnMaxLengthDst) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(LEFT(" + tabVarNl + "." + columnName + "," + String.valueOf(columnMaxLengthDst) + ")),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (isAggHead ? "(CASE WHEN " + tabVarNl + "." + refColName + " = " + tabVarNl + "." + M01_Globals.g_anAhOid + " THEN " + (attrBasePrio + M12_ChangeLog.clNlPrioNonAggHead) + " ELSE " + (attrBasePrio + M12_ChangeLog.clNlPrioAggHead) + " END)" : String.valueOf(attrBasePrio + M12_ChangeLog.clNlPrioNonAggHead)) + ",");
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals_IVK.g_anPsOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");// Join with 'referring table'

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referringClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, false, true, null, null) + " " + tabVarRef);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, false, true, null, null) + " WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, false, false, true, null, null) + " WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarRef);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, false, true, null, null) + " " + tabVarRef);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahObjectId = " + tabVarRef + "." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.objectId = " + tabVarRef + "." + M01_Globals.g_anOid);
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referringClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarRef + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarRef + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarRef + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarRef + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarRef + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarRef + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");// Join with NL-TEXT of 'referred table'
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, true, true, true, null, null) + " " + tabVarNl);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, false, false, true, true, null, null) + " WHERE ((" + M01_Globals.g_anInLrt + " IS NULL) OR (" + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + ")) AND (" + M01_Globals_IVK.g_anIsDeleted + " = 0)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "UNION ALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT OID," + M01_Globals.g_anAhOid + "," + refColName + "," + columnName + "," + M01_Globals.g_anLanguageId + " FROM " + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, true, false, true, true, null, null) + " WHERE (" + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ") " + tabVarNl);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, false, null, null, true, true, null, null) + " " + tabVarNl);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarRef + "." + relFkAttrName + " = " + tabVarNl + "." + refColName);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (M22_Class.g_classes.descriptors[referredClassIndex].useMqtToImplementLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tabVarNl + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tabVarNl + "." + M01_Globals.g_anInLrt + " <> " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tabVarNl + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tabVarNl + "." + M01_Globals.g_anInLrt + " = " + lrtOidFilterVar + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
//include only reference object labels in german and english
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + M01_Globals.g_anLanguageId + " IN (" + M01_Globals_IVK.gc_langIdGerman + "," + M01_Globals_IVK.gc_langIdEnglish + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarNl + "." + columnName + " IS NOT NULL");

if (useClassIdFilter) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.ahClassid IN (" + entityIdStrList + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityType = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L.entityId = " + entityIdStrList);
}
}

if (isPsTagged) {
if (!((clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  M03_Config.useMqtToImplementLrt)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + tabVarRef + "." + M01_Globals_IVK.g_anPsOid + " = " + psOidFilterVar);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + isolationLevelSuffix + ";");
}
}
numAttrsFound = numAttrsFound + 1;
}
}
}
}
}
// ### ENDIF IVK ###
}

returnValue = numAttrsFound;
return returnValue;
}


public static void genAddNlTextChangeLogDdl(int fileNo, String M01_Globals.gc_tempTabNameChangeLog, String M01_Globals.gc_tempTabNameChangeLogNl, Integer ddlTypeW, Integer indentW, String lrtOidStrW, Integer clModeW, String qualTabNameLrtNlW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String lrtOidStr; 
if (lrtOidStrW == null) {
lrtOidStr = "lrtOid_in";
} else {
lrtOidStr = lrtOidStrW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

String qualTabNameLrtNl; 
if (qualTabNameLrtNlW == null) {
qualTabNameLrtNl = "";
} else {
qualTabNameLrtNl = qualTabNameLrtNlW;
}


if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
String qualTabNameLrt;

M11_LRT.genProcSectionHeader(fileNo, "add NL-texts for \"" + M01_Globals_IVK.g_anLrtComment + "\"", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.g_anLrtComment + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "LRTNL." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "LRTNL.TRANSACTIONCOMMENT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameLrtNl + " LRTNL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "LRTNL.LRT_OID = " + lrtOidStr);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "LRTNL.TRANSACTIONCOMMENT IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
}

M11_LRT.genProcSectionHeader(fileNo, "add NL-texts for entity-names", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anAcmEntityName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ENL." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ENL." + M01_Globals.g_anAcmEntityLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anAcmEntityId + " = E." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anAcmEntityType + " = E." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameAcmEntityNl + " ENL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntitySection + " = ENL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntityName + " = ENL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "And");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntityType + " = ENL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

// FIXME: assuming that within a single class hierarchy a given attribute name is not mapped
// differently for different classes we use 'DISTINCT' here. We should navigate up in the
// class hierarchy and pick exactly the attribute that is referred to!
// E.g. 'SR0CONTEXT' exists multiple times in the GENERICASPECT-tree. Each changelog-entry refers to a unique
// occurence.

M11_LRT.genProcSectionHeader(fileNo, "add NL-texts for attribute-names", indent, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anAcmAttributeLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ANL." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ANL." + M01_Globals.g_anAcmAttributeLabel + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameAcmEntity + " EC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anAcmEntityId + " = EC." + M01_Globals.g_anAcmEntityId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anAcmEntityType + " = EC." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameAcmEntity + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmOrParEntitySection + " = EC." + M01_Globals.g_anAcmOrParEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmOrParEntityName + " = EC." + M01_Globals.g_anAcmOrParEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmOrParEntityType + " = EC." + M01_Globals.g_anAcmOrParEntityType);
// ### ELSE IVK ###
// Print #fileNo, addTab(indent + 1); "COALESCE(E."; g_anAcmOrParEntitySection; ",E."; g_anAcmEntitySection; ") = COALESCE(EC."; g_anAcmOrParEntitySection; "EC."; g_anAcmEntitySection; ")"
// Print #fileNo, addTab(indent + 2); "AND"
// Print #fileNo, addTab(indent + 1); "COALESCE(E."; g_anAcmOrParEntityName; ",E."; g_anAcmEntityName; ") = COALESCE(EC."; g_anAcmOrParEntityName; "EC."; g_anAcmEntityName; ")"
// Print #fileNo, addTab(indent + 2); "AND"
// Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityType; " = EC."; g_anAcmEntityType
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_qualTabNameAcmAttributeNl + " ANL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntitySection + " = ANL." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntityName + " = ANL." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E." + M01_Globals.g_anAcmEntityType + " = ANL." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anLdmDbColumnName + " = ANL." + M01_Globals.g_anAcmAttributeName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "L." + M01_Globals.g_anLdmDbColumnName + " = ANL." + M01_Globals.g_anAcmAttributeName + " || '_ID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
}


public static void genCondenseChangeLogNlDdl(int fileNo, int changeLogClassIndex, String qualTabNameChangeLogNl, String M01_Globals.gc_tempTabNameChangeLogNl, String qualSeqNameOid, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW) {
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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;

int tIndex;
int i;

M11_LRT.genProcSectionHeader(fileNo, "condense and move all ChangeLog NL-records into persistent table", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "BEGIN ATOMIC");
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", indent + 1, true);
M11_LRT.genVarDecl(fileNo, "v_last_clgOid", M01_Globals.g_dbtOid, "-1", indent + 1, null);
M11_LRT.genVarDecl(fileNo, "v_last_languageId", M01_Globals.g_dbtEnumId, "-1", indent + 1, null);
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M11_LRT.genVarDecl(fileNo, "v_last_psOid", M01_Globals.g_dbtOid, "-1", indent + 1, null);
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M11_LRT.genVarDecl(fileNo, "v_last_" + tabColumns.descriptors[i].acmAttributeName, M25_Domain.getDbDatatypeByDomainIndex(tabColumns.descriptors[i].dbDomainIndex), "NULL", indent + 1, null);
}
}

M11_LRT.genProcSectionHeader(fileNo, "loop over individual records in temporary table", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FOR logLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + tabColumns.descriptors[i].columnName + " AS c_" + tabColumns.descriptors[i].acmAttributeName + ",");
}
}

// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals_IVK.g_anPsOid + " AS c_psOid,");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CLG_OID AS c_clgOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLanguageId + " AS c_languageId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "COALESCE(PRIO, " + String.valueOf(M12_ChangeLog.clNlPrioOther) + ") ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "DO");

M11_LRT.genProcSectionHeader(fileNo, "determine whether this record needs to be merged with the previous record", indent + 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "IF (v_last_clgOid > 0) AND ((c_clgOid <> v_last_clgOid) OR (c_languageId <> v_last_languageId)) THEN");
M11_LRT.genProcSectionHeader(fileNo, "this maps to a new record - persist previous record", indent + 3, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + qualTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, indent + 4, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(");

tIndex = 1;
// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, tabColumns.numDescriptors + 4 + (M03_Config.usePsTagInNlTextTables ? 1 : 0), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
// initAttributeTransformation transformation, tabColumns.numDescriptors + 4
// ### ENDIF IVK ###
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex, tabColumns.descriptors[i].columnName, "v_last_" + tabColumns.descriptors[i].acmAttributeName, null, null, null);
tIndex = tIndex + 1;
}
}
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 0, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 1, "CLG_OID", "v_last_clgOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 2, M01_ACM.conLanguageId, "v_last_languageId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 3, M01_ACM.conVersionId, "1", null, null, null);
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 4, M01_ACM_IVK.conPsOid, "v_last_psOid", null, null, null);
}
// ### ENDIF IVK ###

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, indent + 4, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + ");");

M11_LRT.genProcSectionHeader(fileNo, "keep track of values read in this record", indent + 3, null);
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SET v_last_" + tabColumns.descriptors[i].acmAttributeName + " = c_" + tabColumns.descriptors[i].acmAttributeName + ";");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SET v_last_clgOid = c_clgOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SET v_last_languageId = c_languageId;");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SET v_last_psOid = c_psOid;");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ELSE");
M11_LRT.genProcSectionHeader(fileNo, "this record merges with previous record", indent + 3, true);

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "IF c_" + tabColumns.descriptors[i].acmAttributeName + " IS NOT NULL THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SET v_last_" + tabColumns.descriptors[i].acmAttributeName + " = c_" + tabColumns.descriptors[i].acmAttributeName + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "END IF;");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "IF v_last_clgOid < 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SET v_last_clgOid = c_clgOid;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SET v_last_languageId = c_languageId;");
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "SET v_last_psOid = c_psOid;");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END FOR;");

M11_LRT.genProcSectionHeader(fileNo, "persist final record", indent + 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "IF v_last_clgOid > 0 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + qualTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");

tIndex = 1;
// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, tabColumns.numDescriptors + 4 + (M03_Config.usePsTagInNlTextTables ? 1 : 0), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
// initAttributeTransformation transformation, tabColumns.numDescriptors + 4
// ### ENDIF IVK ###
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacRegular)) {
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex, tabColumns.descriptors[i].columnName, "v_last_" + tabColumns.descriptors[i].acmAttributeName, null, null, null);
tIndex = tIndex + 1;
}
}
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 0, M01_ACM.conOid, "NEXTVAL FOR " + qualSeqNameOid, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 1, "CLG_OID", "v_last_clgOid", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 2, M01_ACM.conLanguageId, "v_last_languageId", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 3, M01_ACM.conVersionId, "1", null, null, null);
// ### IF IVK ###
if (M03_Config.usePsTagInNlTextTables) {
M24_Attribute_Utilities.setAttributeMapping(transformation, tIndex + 4, M01_ACM_IVK.conPsOid, "v_last_psOid", null, null, null);
}
// ### ENDIF IVK ###

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(changeLogClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ");");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "END IF;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "END;");
}


public static void genPersistChangeLogDdl(int fileNo, int changeLogClassIndex, String qualTabNameChangeLog, String M01_Globals.gc_tempTabNameChangeLog, String qualTabNameChangeLogNl, String M01_Globals.gc_tempTabNameChangeLogNl, String qualSeqNameOid, Integer ddlTypeW,  Integer thisOrgIndexW,  Integer thisPoolIndexW, Integer indentW, Integer clModeW, String qualTabNameLrtNlW, String lrtOidStrW, Boolean skipNlW, String varNameRefTsW) {
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

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

String qualTabNameLrtNl; 
if (qualTabNameLrtNlW == null) {
qualTabNameLrtNl = "";
} else {
qualTabNameLrtNl = qualTabNameLrtNlW;
}

String lrtOidStr; 
if (lrtOidStrW == null) {
lrtOidStr = "lrtOid_in";
} else {
lrtOidStr = lrtOidStrW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

String varNameRefTs; 
if (varNameRefTsW == null) {
varNameRefTs = "";
} else {
varNameRefTs = varNameRefTsW;
}

String qualTabNameChangeLogWork;
qualTabNameChangeLogWork = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, null, null, null, null, null, null, null);
String qualTabNameChangeLogNlWork;
qualTabNameChangeLogNlWork = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexChangeLog, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, null, null, null, true, null, null, null);

// ### IF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "determine OID of division for log records - if not already known", indent, skipNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " TCL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.divisionOid = (SELECT PS.PDIDIV_OID FROM " + M01_Globals_IVK.g_qualTabNameProductStructure + " PS WHERE PS." + M01_Globals.g_anOid + " = TCL." + M01_Globals_IVK.g_anPsOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(TCL.divisionOid IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(TCL." + M01_Globals_IVK.g_anPsOid + " IS NOT NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

// ### ENDIF IVK ###
M11_LRT.genProcSectionHeader(fileNo, "move all ChangeLog records into persistent table", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");

M22_Class.genAttrDeclsForClassRecursive(changeLogClassIndex, null, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 1, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");

M22_Class.genAttrDeclsForClassRecursive(changeLogClassIndex, null, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 1, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");

M12_ChangeLog.genAddNlTextChangeLogDdl(fileNo, M01_Globals.gc_tempTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLogNl, ddlType, indent, lrtOidStr, clMode, qualTabNameLrtNl);

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M11_LRT.genProcSectionHeader(fileNo, "retrieve LRTCOMMENT from ChangeLog in Work Data Pool", indent, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLogNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CLG_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "LRTCOMMENT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.g_anVersionId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_NlCl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "language_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "lrtcomment,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "rownum");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L." + M01_Globals.g_anOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NLWork." + M01_Globals.g_anLanguageId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NLWork.LRTCOMMENT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ROWNUMBER() OVER (PARTITION BY NLWork.CLG_OID, NLWork." + M01_Globals.g_anLanguageId + ", Work." + M01_Globals.g_anLdmDbColumnName + ", Work.OPERATION_ID ORDER BY Work.OPTIMESTAMP DESC)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + M01_Globals.gc_tempTabNameChangeLog + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameChangeLogWork + " Work");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L.objectId = Work.OBJECTID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "L.operation_id = Work.OPERATION_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(L.dbColumnName, '') = COALESCE(Work." + M01_Globals.g_anLdmDbColumnName + ", '')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + qualTabNameChangeLogNlWork + " NLWork");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "Work." + M01_Globals.g_anOid + " = NLWork.CLG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "Work.OPTIMESTAMP < v_setProductiveTs");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "-1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "clg_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "language_id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "lrtcomment,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V_NlCl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "rownum = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WITH UR;");
}
// ### ENDIF IVK ###

M12_ChangeLog.genCondenseChangeLogNlDdl(fileNo, changeLogClassIndex, qualTabNameChangeLogNl, M01_Globals.gc_tempTabNameChangeLogNl, qualSeqNameOid, ddlType, thisOrgIndex, thisPoolIndex, indent);
}

//Parameter withTempTable added due to change on View V_CL_GENERICASPECT (defect 19001 wf)
private static void genCondOuterJoin(int fileNo, int classIndex, int classIndexAh, Integer clMode,  int thisOrgIndex,  int thisPoolIndex, String tupVar1, String tupVar1Ah, String tupVar2, String fkAttrName, Integer ddlTypeW, Integer indentW, String referredColumnsW,  Boolean forGenW, String lrtOidRefW, Boolean withTempTableW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String referredColumns; 
if (referredColumnsW == null) {
referredColumns = "";
} else {
referredColumns = referredColumnsW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

String lrtOidRef; 
if (lrtOidRefW == null) {
lrtOidRef = "PRIV.INLRT";
} else {
lrtOidRef = lrtOidRefW;
}

boolean withTempTable; 
if (withTempTableW == null) {
withTempTable = true;
} else {
withTempTable = withTempTableW;
}

String thisClassShortName;
int thisClassIndex;
String thisTupVar;

if (classIndex > 0) {
thisClassIndex = classIndex;
thisTupVar = tupVar1;
} else if (classIndexAh > 0) {
thisClassIndex = classIndexAh;
thisTupVar = tupVar1Ah;
} else {
return;
}

thisClassShortName = M22_Class.g_classes.descriptors[thisClassIndex].shortName;

String thisReferredColumns;
String parFkAttrName;
if (forGen) {
parFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, thisClassShortName, null, null, null, null);
thisReferredColumns = referredColumns + (referredColumns != "" ? "," : "") + parFkAttrName;
} else {
parFkAttrName = M01_Globals.g_anOid;
thisReferredColumns = referredColumns;
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");

//added parameter withTempTable (defect 19001 wf)
M22_Class.genTabSubQueryByEntityIndex(thisClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt), forGen, tupVar2, thisReferredColumns, indent + 1, null, "", withTempTable);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + thisTupVar + "." + fkAttrName + " = " + tupVar2 + "." + parFkAttrName);

if (M22_Class.g_classes.descriptors[thisClassIndex].isUserTransactional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tupVar2 + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVar2 + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVar2 + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVar2 + "." + M01_Globals.g_anInLrt + " <> " + lrtOidRef + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(indent + 2); "(("; tupVar2; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVar2; "."; g_anInLrt; " IS NULL OR "; tupVar2; "."; g_anInLrt; " <> "; lrtOidRef; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "((" + tupVar2 + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVar2 + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVar2 + "." + M01_Globals.g_anInLrt + " = " + lrtOidRef + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + tupVar2 + ".ROWNUM = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "LEFT OUTER JOIN");
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(SELECT T.*,ROWNUMBER() OVER (PARTITION BY " + parFkAttrName + " ORDER BY " + "(CASE WHEN " + M01_Globals_IVK.g_anValidTo + " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR(" + M01_Globals_IVK.g_anValidTo + " - CURRENT DATE)) " + "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - " + M01_Globals_IVK.g_anValidTo + ")) + 10000000 END)" + ") AS ROWNUM FROM " + M04_Utilities.genQualTabNameByClassIndex(thisClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null) + " T) " + tupVar2 + "");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M04_Utilities.genQualTabNameByClassIndex(thisClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null) + " " + tupVar2);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + thisTupVar + "." + fkAttrName + " = " + tupVar2 + "." + parFkAttrName);
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + tupVar2 + ".ROWNUM = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}
}


// ### IF IVK ###
public static void genRetrieveSr0ContextForSr1Validity(int fileNo,  int thisOrgIndex,  int thisPoolIndex, Integer ddlType, boolean lrtAware, Integer indentW, String lrtOidVarW, Boolean skipNlW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String lrtOidVar; 
if (lrtOidVarW == null) {
lrtOidVar = "lrtOid_in";
} else {
lrtOidVar = lrtOidVarW;
}

boolean skipNl; 
if (skipNlW == null) {
skipNl = false;
} else {
skipNl = skipNlW;
}

// Mapping of SR0Validity-columns to Sr1Validity-Records could also be done via 'CL-Attribute Mapping', but this is far more efficient

boolean forNsr1Validity;
int i;
for (int i = 1; i <= 2; i++) {
forNsr1Validity = (i == 2);

if (forNsr1Validity) {
M11_LRT.genProcSectionHeader(fileNo, "special treatment of NSR1Validity: retrieve " + M01_Globals_IVK.g_anSr0Context + " / SR1CONTEXT", indent, null);
} else {
M11_LRT.genProcSectionHeader(fileNo, "special treatment of SR1Validity: retrieve " + M01_Globals_IVK.g_anSr0Context + "", indent, skipNl);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals.gc_tempTabNameChangeLog + " TCL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL." + M01_Globals_IVK.g_anSr0Context + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.CSBAUMUSTER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODE10,");

if (forNsr1Validity) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR1CODE10,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "TCL.SR0CODEOID10");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CONTEXT,   SR0.SR0CONTEXT),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.CSBAUMUSTER,  SR0.CSBAUMUSTER),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE1,     S01." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE2,     S02." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE3,     S03." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE4,     S04." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE5,     S05." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE6,     S06." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE7,     S07." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE8,     S08." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE9,     S09." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODE10,    S10." + M01_Globals_IVK.g_anCodeNumber + "),");

if (forNsr1Validity) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE1,     S101." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE2,     S102." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE3,     S103." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE4,     S104." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE5,     S105." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE6,     S106." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE7,     S107." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE8,     S108." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE9,     S109." + M01_Globals_IVK.g_anCodeNumber + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR1CODE10,    S110." + M01_Globals_IVK.g_anCodeNumber + "),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID1,  S01." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID2,  S02." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID3,  S03." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID4,  S04." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID5,  S05." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID6,  S06." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID7,  S07." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID8,  S08." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID9,  S09." + M01_Globals.g_anOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "COALESCE(TCL.SR0CODEOID10, S10." + M01_Globals.g_anOid + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");

M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "SR0", "SR0CONTEXT,CSBAUMUSTER,S0CS01_OID,S0CS02_OID,S0CS03_OID,S0CS04_OID,S0CS05_OID,S0CS06_OID,S0CS07_OID,S0CS08_OID,S0CS09_OID,S0CS10_OID", indent + 2, null, lrtOidVar, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
if (forNsr1Validity) {
M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "SR1", "E0VEX0_OID,S1CT01_OID,S1CT02_OID,S1CT03_OID,S1CT04_OID,S1CT05_OID,S1CT06_OID,S1CT07_OID,S1CT08_OID,S1CT09_OID,S1CT10_OID", indent + 2, null, lrtOidVar, null);
} else {
M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "SR1", "E0VEX0_OID", indent + 2, null, lrtOidVar, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SR0." + M01_Globals.g_anOid + " = SR1.E0VEX0_OID");

if (forNsr1Validity) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "INNER JOIN");
M22_Class.genTabSubQueryByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, false, "NSR1", "E1VEX1_OID", indent + 2, null, lrtOidVar, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SR1." + M01_Globals.g_anOid + " = NSR1.E1VEX1_OID");
}

genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S01", "S0CS01_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS01_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S02", "S0CS02_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS02_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S03", "S0CS03_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS03_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S04", "S0CS04_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS04_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S05", "S0CS05_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS05_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S06", "S0CS06_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS06_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S07", "S0CS07_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS07_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S08", "S0CS08_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS08_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S09", "S0CS09_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS09_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S10", "S0CS10_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S0CS10_OID", null, lrtOidVar, null);

if (forNsr1Validity) {
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S101", "S1CT01_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT01_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S102", "S1CT02_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT02_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S103", "S1CT03_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT03_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S104", "S1CT04_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT04_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S105", "S1CT05_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT05_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S106", "S1CT06_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT06_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S107", "S1CT07_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT07_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S108", "S1CT08_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT08_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S109", "S1CT09_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT09_OID", null, lrtOidVar, null);
genCondOuterJoin(fileNo, M01_Globals_IVK.g_classIndexGenericCode, -1, (lrtAware ? M12_ChangeLog.ChangeLogMode.eclLrt : M12_ChangeLog.ChangeLogMode.eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S110", "S1CT10_OID", ddlType, indent + 1, M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber + ",S1CT10_OID", null, lrtOidVar, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
if (forNsr1Validity) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(TCL.ahObjectId = NSR1." + M01_Globals.g_anOid + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(TCL.ahObjectId = SR1." + M01_Globals.g_anOid + ")");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");
if (forNsr1Validity) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(TCL.ahClassId = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexNSr1Validity) + "')");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(TCL.ahClassId = '" + M22_Class.getClassIdStrByIndex(M01_Globals_IVK.g_classIndexSr1Validity) + "')");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ";");
}
}
// ### ENDIF IVK ###


public static void genChangeLogSupportForEntity(int acmEntityIndex, Integer acmEntityType, M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs, String qualSourceTabName, String qualSourceNlTabName, String qualTargetTabName, String qualTargetNlTabName, String qualRefGenTabName, String qualAggHeadRefNlTabName, String qualAggHeadTabName,  int thisOrgIndex, int srcPoolIndex, int dstPoolIndex, int fileNo, int fileNoClView, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Integer clModeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

String entityName;
String entityTypeDescr;
String entityShortName;
String sectionIndex;
String dbAcmEntityType;
boolean isLogChange;
boolean isGenForming;
boolean hasNlAttrs;
boolean entityIsTransactional;
String entityIdStrList;
String aggHeadShortClassName;
int aggHeadClassIndex;
boolean hasSubClass;
String entityIdStr;
M24_Attribute_Utilities.AttrDescriptorRefs nlAttrRefs;
M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;
boolean hasOwnTable;
boolean isAggHead;
// ### IF IVK ###
boolean hasNoIdentity;
boolean isPsTagged;
boolean isGenericAspect;
boolean hasPriceAssignmentSubClass;
String priceAssignmentSubClassIdList;
boolean priceAssignmentHasNlAttrs;
boolean isSubjectToPreisDurchschuss;
boolean condenseData;
boolean enforceLrtChangeComment;
boolean isNationalizable;
boolean hasIsNationalInclSubClasses;

hasPriceAssignmentSubClass = false;
priceAssignmentSubClassIdList = "";
priceAssignmentHasNlAttrs = false;
isGenericAspect = false;
enforceLrtChangeComment = false;
// ### ELSE IVK ###
//
// ### ENDIF IVK ###
isAggHead = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
// ### IF IVK ###
isGenericAspect = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() == M01_ACM_IVK.clnGenericAspect.toUpperCase() & ! forGen & !forNl;
// ### ENDIF IVK ###

if (forNl) {
entityName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
// ### IF IVK ###
hasNoIdentity = false;
// ### ENDIF IVK ###
isLogChange = false;
isGenForming = false;
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
hasOwnTable = true;
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
// ### IF IVK ###
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
// ### ENDIF IVK ###
isLogChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
if (forGen) {
hasNlAttrs = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses;
} else {
hasNlAttrs = M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses;
}
attrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].attrRefs;

// ### IF IVK ###
hasPriceAssignmentSubClass = M22_Class.g_classes.descriptors[acmEntityIndex].hasPriceAssignmentSubClass;
isSubjectToPreisDurchschuss = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
enforceLrtChangeComment = M22_Class.g_classes.descriptors[acmEntityIndex].enforceLrtChangeComment;

if (!(hasPriceAssignmentSubClass)) {
priceAssignmentHasNlAttrs = hasNlAttrs;
}
// ### ENDIF IVK ###
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
}
entityIsTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
entityIdStrList = M22_Class.getSubClassIdStrListByClassIndex(M22_Class.g_classes.descriptors[acmEntityIndex].classIndex);
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
hasSubClass = M22_Class.g_classes.descriptors[acmEntityIndex].hasSubClass;
nlAttrRefs = M22_Class.g_classes.descriptors[acmEntityIndex].nlAttrRefsInclSubclasses;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
isNationalizable = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable & ! forNl;
hasIsNationalInclSubClasses = M22_Class.g_classes.descriptors[acmEntityIndex].hasIsNationalInclSubClasses & ! forNl;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
if (forNl) {
entityName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
entityShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
isLogChange = false;
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
} else {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isLogChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;
hasNlAttrs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;
attrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].attrRefs;
// ### IF IVK ###
priceAssignmentHasNlAttrs = hasNlAttrs;
// ### ENDIF IVK ###
}
isGenForming = false;
entityIsTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
entityIdStrList = "'" + M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr + "'";
dbAcmEntityType = "R";
aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
hasSubClass = false;
entityIdStr = "";
nlAttrRefs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs;
hasOwnTable = true;
// ### IF IVK ###
hasNoIdentity = false;
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
condenseData = false;
isNationalizable = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isNationalizable;
hasIsNationalInclSubClasses = M23_Relationship.g_relationships.descriptors[acmEntityIndex].hasIsNationalInclSubClasses;
isSubjectToPreisDurchschuss = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToPreisDurchschuss;
// ### ENDIF IVK ###
} else {
return;
}

boolean isPrimaryOrg;
isPrimaryOrg = (thisOrgIndex == M01_Globals.g_primaryOrgIndex);

if (aggHeadClassIndex > 0) {
aggHeadShortClassName = M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName;
}

// ### IF IVK ###
if (isSubjectToPreisDurchschuss) {
int i;
for (int i = 1; i <= M00_Helper.uBound(M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIndexesRecursive); i++) {
if (!(M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIndexesRecursive[i]].isAbstract &  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIndexesRecursive[i]].isPriceAssignment)) {
priceAssignmentSubClassIdList = priceAssignmentSubClassIdList + (!(priceAssignmentSubClassIdList.compareTo("") == 0) ? "," : "") + "'" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIndexesRecursive[i]].classIdStr + "'";
priceAssignmentHasNlAttrs = priceAssignmentHasNlAttrs |  M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[M01_Globals_IVK.g_classIndexGenericAspect].subclassIndexesRecursive[i]].hasNlAttrsInNonGenInclSubClasses;
}
}
}

// ### ENDIF IVK ###
String qualTabNameLrt;
String qualTabNameLrtNl;
qualTabNameLrt = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null, null, null, null);
qualTabNameLrtNl = M04_Utilities.genQualTabNameByClassIndex(M01_Globals.g_classIndexLrt, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, true, null, null, null);

String qualSeqNameOid;
qualSeqNameOid = M71_Org.genQualOidSeqNameForOrg(thisOrgIndex, ddlType, null, null, null, null);

// ####################################################################################################################
// #    View for ChangeLog
// ####################################################################################################################

M12_ChangeLog.genChangeLogViewDdl(acmEntityIndex, acmEntityType, qualSourceTabName, qualRefGenTabName, qualSourceNlTabName, qualTargetTabName, qualTargetNlTabName, qualAggHeadTabName, thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNoClView, ddlType, forGen, clMode);

M12_ChangeLog.genChangeLogViewDdl2(acmEntityIndex, acmEntityType, qualSourceTabName, qualRefGenTabName, qualSourceNlTabName, qualTargetTabName, qualTargetNlTabName, qualAggHeadTabName, thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNoClView, ddlType, forGen, clMode);

// ####################################################################################################################
// #    SP for creating ChangeLog
// ####################################################################################################################
String qualProcName;
int seqNo;
boolean allRegAttrsProcessed;
boolean allNlAttrsProcessed;
int lastRegAttrsProcessed;
int lastNlAttrsProcessed;
int numAttrsProcessedThisLoop;
M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean changeLogAcHasBeenFilled;

String procNamePrefix;
// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
procNamePrefix = M01_ACM.spnLrtGenChangelog;
} else {
procNamePrefix = M01_ACM_IVK.spnSpGenChangelog;
}
// ### ELSE IVK ###
// procNamePrefix = spnLrtGenChangelog
// ### ENDIF IVK ###

String qualViewName;

allRegAttrsProcessed = false;
allNlAttrsProcessed = false;
lastRegAttrsProcessed = 0;
lastNlAttrsProcessed = 0;
seqNo = 0;
changeLogAcHasBeenFilled = false;
while (!(allRegAttrsProcessed | ! allNlAttrsProcessed)) {
numAttrsProcessedThisLoop = 0;

qualProcName = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, forNl, procNamePrefix, null, null, null, (seqNo == 0 ? "" : String.valueOf(seqNo)));

M22_Class_Utilities.printSectionHeader("SP for creating ChangeLog (" + M12_ChangeLog.genClModeDescription(clMode) + ") on \"" + qualTargetTabName + "\" (" + entityTypeDescr + " \"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M11_LRT.genProcParm(fileNo, "IN", "lrtOid_in", M01_Globals.g_dbtLrtId, true, "OID of the LRT to create the Log for");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to create the Log for");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "cdUserId_in", M01_Globals.g_dbtUserId, true, "CD User Id of the mdsUser");
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, true, "identifies the operation (insert, update, delete) to create the Log for");
M11_LRT.genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", true, "marks the execution timestamp of the LRT");
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "autoPriceSetProductive_in", M01_Globals.g_dbtBoolean, true, "specifies whether prices are set productive");
M11_LRT.genProcParm(fileNo, "IN", "settingManActCP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateCodePrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingManActTP_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateTypePrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingManActSE_in", M01_Globals.g_dbtBoolean, true, "setting 'manuallyActivateStandardEquipmentPrice'");
M11_LRT.genProcParm(fileNo, "IN", "settingSelRelease_in", M01_Globals.g_dbtBoolean, true, "setting 'useSelectiveReleaseProcess'");

if (!(isPrimaryOrg)) {
M11_LRT.genProcParm(fileNo, "IN", "isFtoLrt_in", M01_Globals.g_dbtBoolean, true, "'1' if and only if this LRT 'is central data transfer'");
}
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "psOid_in", M01_Globals.g_dbtOid, true, "OID of the Product Structure to create the Log for");
// ### ENDIF IVK ###
M11_LRT.genProcParm(fileNo, "IN", "opId_in", M01_Globals.g_dbtEnumId, true, "identifies the operation (insert, update, delete) to create the Log for");
M11_LRT.genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", true, "marks the timestamp of 'Setting Productive'");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows returned in the log");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genCondDecl(fileNo, "notFound", "02000", null);

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd & ! isPsTagged) {
M11_LRT.genVarDecl(fileNo, "v_divisionOid", M01_Globals.g_dbtOid, "NULL", null, null);
}
// ### ENDIF IVK ###
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare continue handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR notFound");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

boolean hasColumnToFilter;
// ### IF IVK ###
boolean ignoreLastUpdateTimestamp;
ignoreLastUpdateTimestamp = !(isAggHead |  (forGen & ! hasNoIdentity) | forNl | (clMode != M12_ChangeLog.ChangeLogMode.eclSetProd));

// ### ENDIF IVK ###
// ### IF IVK ###
M24_Attribute_Utilities.initAttributeTransformation(transformation, (ignoreLastUpdateTimestamp ? 4 : 3), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
// ### ELSE IVK ###
//   initAttributeTransformation transformation, 4
// ### ENDIF IVK ###
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);
// ### IF IVK ###
if (ignoreLastUpdateTimestamp) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
}
// ### ELSE IVK ###
//   setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
// ### ENDIF IVK ###

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

int attrIndex;
int numAttrs;
// determine whether this table has some column eligible for selective filtering
attrIndex = lastRegAttrsProcessed + 1;
// ### IF IVK ###
numAttrs = (isGenForming &  (forGen |  hasNoIdentity) & (seqNo == 0) ? 2 : (seqNo == 0 ? 1 : 0));
// ### ELSE IVK ###
//   numAttrs = IIf(isGenForming And forGen And (seqNo = 0), 2, IIf(seqNo = 0, 1, 0))
// ### ENDIF IVK ###
hasColumnToFilter = false;
// ### IF IVK ###
if (((clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd)) &  hasSubClass) {
// ### ELSE IVK ###
//   If clMode = eclLrt And hasSubClass Then
// ### ENDIF IVK ###
while (!(hasColumnToFilter &  (attrIndex <= tabColumns.numDescriptors))) {
// ### IF IVK ###
if ((tabColumns.descriptors[attrIndex].columnCategory &  M01_Common.AttrCategory.eacRegular) |  (tabColumns.descriptors[attrIndex].columnCategory &  M01_Common.AttrCategory.eacFkOid) | ((clMode == M12_ChangeLog.ChangeLogMode.eclLrt) &  (tabColumns.descriptors[attrIndex].columnCategory &  M01_Common.AttrCategory.eacSetProdMeta))) {
// ### ELSE IVK ###
//         If (.columnCategory And eacRegular) Or (.columnCategory And eacFkOid) Then
// ### ENDIF IVK ###
if (tabColumns.descriptors[attrIndex].acmAttributeIndex > 0) {
hasColumnToFilter = hasColumnToFilter |  (M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[attrIndex].acmAttributeIndex].acmEntityIndex].superClassIndex > 0);
}
numAttrs = numAttrs + 1;
}
attrIndex = attrIndex + 1;
}

attrIndex = lastNlAttrsProcessed + 1;
while (!(hasColumnToFilter &  (attrIndex <= nlAttrRefs.numDescriptors))) {
if (nlAttrRefs.descriptors[attrIndex].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute) {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[attrIndex].refIndex].isNl &  (forGen |  hasNoIdentity) == M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[attrIndex].refIndex].isTimeVarying) {
// ### ELSE IVK ###
//           If .isNl And forGen = .isTimeVarying Then
// ### ENDIF IVK ###
hasColumnToFilter = hasColumnToFilter |  (M22_Class.g_classes.descriptors[M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[attrIndex].refIndex].acmEntityIndex].superClassIndex > 0);
numAttrs = numAttrs + 1;
}
}
attrIndex = attrIndex + 1;
}
}

// ### IF IVK ###
M12_ChangeLog.genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, (isAggHead |  (enforceLrtChangeComment &  clMode == M12_ChangeLog.ChangeLogMode.eclLrt) | hasNlAttrs) &  (seqNo == 0), true, null, null, null, null, null);


//  genDdlForTempTablesChangeLog fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, _
//   (isAggHead Or (enforceLrtChangeComment And clMode = eclLrt) Or hasNlAttrs) And (seqNo = 0), hasColumnToFilter

// ### ELSE IVK ###
//   genDdlForTempTablesChangeLog fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, (isAggHead Or hasNlAttrs) And (seqNo = 0), hasColumnToFilter
// ### ENDIF IVK ###
// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
if (condenseData) {
M92_DBUtilities.genDdlForTempOids(fileNo, 1, null, null, null);
} else {
M86_SetProductive.genDdlForTempTablesSp(fileNo, 1, null, null, null, null);
}
}
// ### ENDIF IVK ###

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (isPrimaryOrg) {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
} else {
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcName, ddlType, null, "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
}

// ### ELSE IVK ###
//   If clMode = eclLrt Then
//     genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "rowCount_out"
//   Else
//     genSpLogProcEnter fileNo, qualProcName, ddlType, , "opId_in", "#commitTs_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd & ! isPsTagged) {
M11_LRT.genProcSectionHeader(fileNo, "determine division OID", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_divisionOid =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PDIDIV_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.g_qualTabNameProductStructure);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_anOid + " = psOid_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
if (seqNo == 0) {
// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt & ! condenseData) {
// ### ELSE IVK ###
//     If clMode = eclLrt Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + " THEN");
// ### IF IVK ###
if (isGenericAspect) {
M12_ChangeLog.genRetrieveSr0ContextForSr1Validity(fileNo, thisOrgIndex, srcPoolIndex, ddlType, true, 2, "lrtOid_in", true);
}

// if we found some (>2) NL-Text Column to process we need to increase the number of processed attributes
// each NL-Text Column eats up about 50% of the source code volume compared to a 'regular attribute'
numAttrsProcessedThisLoop = M12_ChangeLog.genAddNlTextChangeLogDdlForIndividualAttrs(fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, M01_Globals.gc_tempTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLogNl, qualSourceNlTabName, M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null), qualAggHeadRefNlTabName, M04_Utilities.genSurrogateKeyName(ddlType, aggHeadShortClassName, null, null, null, null), nlAttrRefs, relRefs, forGen, "lrtOid_in", "psOid_in", thisOrgIndex, srcPoolIndex, true, true, ddlType, 2, !(isGenericAspect));

numAttrsProcessedThisLoop = (numAttrsProcessedThisLoop > 2 ? 1 : 0) + (isGenericAspect ? 5 : 0);
// ### ELSE IVK ###
//       ' if we found some (>2) NL-Text Column to process we need to increase the number of processed attributes
//       ' each NL-Text Column eats up about 50% of the source code volume compared to a 'regular attribute'
//       numAttrsProcessedThisLoop = _
//         ( _
//           genAddNlTextChangeLogDdlForIndividualAttrs( _
//             fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, gc_tempTabNameChangeLog, _
//             gc_tempTabNameChangeLogNl, qualSourceNlTabName, genSurrogateKeyName(ddlType, entityShortName), _
//             qualAggHeadRefNlTabName, genSurrogateKeyName(ddlType, aggHeadShortClassName), nlAttrRefs, _
//             relRefs, forGen, "lrtOid_in", thisOrgIndex, srcPoolIndex, True, True, ddlType, 2, True _
//         ) _
//       )
//       numAttrsProcessedThisLoop = IIf(numAttrsProcessedThisLoop > 2, 1, 0)
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " OR opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd & ! condenseData & hasNlAttrs) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + " THEN");

if (isGenericAspect) {
M12_ChangeLog.genRetrieveSr0ContextForSr1Validity(fileNo, thisOrgIndex, srcPoolIndex, ddlType, false, 2, "", true);
}

numAttrsProcessedThisLoop = M12_ChangeLog.genAddNlTextChangeLogDdlForIndividualAttrs(fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, M01_Globals.gc_tempTabNameChangeLog, M01_Globals.gc_tempTabNameChangeLogNl, qualSourceNlTabName, M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null), qualAggHeadRefNlTabName, M04_Utilities.genSurrogateKeyName(ddlType, aggHeadShortClassName, null, null, null, null), nlAttrRefs, relRefs, forGen, "", "psOid_in", thisOrgIndex, srcPoolIndex, false, true, ddlType, 2, true);

numAttrsProcessedThisLoop = (numAttrsProcessedThisLoop > 2 ? 1 : 0) + (isGenericAspect ? 5 : 0);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " OR opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
} else {
if (isGenericAspect) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusLocked) + " THEN");
M12_ChangeLog.genRetrieveSr0ContextForSr1Validity(fileNo, thisOrgIndex, srcPoolIndex, ddlType, false, 2, "lrtOid_in", true);
numAttrsProcessedThisLoop = 5;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " OR opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
} else {
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " THEN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusCreated) + " OR opId_in = " + String.valueOf(M11_LRT.lrtStatusDeleted) + " THEN");
}
// ### ELSE IVK ###
//         Print #fileNo, addTab(1); "IF opId_in = " ; CStr(lrtStatusCreated) ; " OR opId_in = " ; CStr(lrtStatusDeleted) ; " THEN"
// ### ENDIF IVK ###
// ### IF IVK ###
}
// ### ENDIF IVK ###
}

// ### IF IVK ###
M12_ChangeLog.genGenChangeLogRecordDdl(acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, M01_Globals.gc_tempTabNameChangeLog, (condenseData ? "inserts" : "inserts and deletes"), "opId_in", thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, null, null, null, null, clMode, null, null, null, null, null, null, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt ? "cdUserId_in" : ""), (condenseData &  clMode == M12_ChangeLog.ChangeLogMode.eclSetProd ? String.valueOf(M11_LRT.lrtStatusCreated) : ""), false, null, null, null);
// ### ELSE IVK ###
//     genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
//                              gc_tempTabNameChangeLog, "inserts and deletes", _
//                              "opId_in", thisOrgIndex, _
//                              dstPoolIndex, fileNo, ddlType, forGen, , , , , clMode, , , , , , , _
//                              IIf(clMode = eclLrt, "cdUserId_in", ""), , "", False
// ### ENDIF IVK ###
if (hasNlAttrs) {
M00_FileWriter.printToFile(fileNo, "");
// ### IF IVK ###
M12_ChangeLog.genGenChangeLogRecordDdl(acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, M01_Globals.gc_tempTabNameChangeLog, (condenseData ? "inserts" : "inserts and deletes"), "opId_in", thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, true, null, null, null, clMode, null, null, null, null, null, null, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt ? "cdUserId_in" : ""), null, null, null, null, null);
// ### ELSE IVK ###
//       genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
//                                gc_tempTabNameChangeLog, "inserts and deletes", _
//                                "opId_in", thisOrgIndex, _
//                                dstPoolIndex, fileNo, ddlType, forGen, True, , , , clMode, , , , , , , IIf(clMode = eclLrt, "cdUserId_in", "")
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSEIF opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN");
}
// ### ELSE IVK ###
//     Print #fileNo, addTab(1); "ELSEIF opId_in = " ; CStr(lrtStatusUpdated) ; " THEN"
// ### ENDIF IVK ###
} else {
// ### IF IVK ###
if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF opId_in = " + String.valueOf(M11_LRT.lrtStatusUpdated) + " THEN");
}
// ### ELSE IVK ###
//     Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusUpdated); " THEN"
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (condenseData) {
allRegAttrsProcessed = true;
allNlAttrsProcessed = true;
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###


if (seqNo == 0) {
// first procedure takes care of INSERT & DELETE - we thus handle one attribute less
numAttrsProcessedThisLoop = numAttrsProcessedThisLoop + 1;
}

boolean ignoreForChangelog;
int thisAttributeIndex;

String valuesStringForCTE;
String caseUpdateStringForCTE;
valuesStringForCTE = "";
caseUpdateStringForCTE = "";

String qualTabNameExpr;
qualTabNameExpr = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexExpression, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, srcPoolIndex, false, null, null, null, null, null, null);

String qualTabName;
qualTabName = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, ddlType, thisOrgIndex, srcPoolIndex, false, null, null, null, null, null, null);

boolean splitVar;
splitVar = isGenericAspect &  (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd);

String cdUserId_in;
cdUserId_in = (clMode == M12_ChangeLog.ChangeLogMode.eclLrt ? "cdUserId_in" : "V.cdUserId");


String[] stringsPerType = new String[15];
//stringsPerType(1) :  _t
//stringsPerType(2) :  _o
//stringsPerType(3) :  _n
//stringsPerType(4) :  _Dto
//stringsPerType(5) :  _Dtn
//stringsPerType(6) :  _Io
//stringsPerType(7) :  _In
//stringsPerType(8) :  _BIo
//stringsPerType(9) :  _BIn
//stringsPerType(10) :  _Do
//stringsPerType(11) :  _Dn
//stringsPerType(12) :  _Bo
//stringsPerType(13) :  _Bn
//stringsPerType(14) :  _To
//stringsPerType(15) :  _Tn
for (int i = 1; i <= 15; i++) {
stringsPerType[(i)] = "CASE bas.dbColumnName" + vbCrLf + "        WHEN 'DUMMYDUMMY' THEN NULL" + vbCrLf + "    ";
}

// generate change log records for changed regular attributes
thisAttributeIndex = lastRegAttrsProcessed + 1;

while (thisAttributeIndex <= tabColumns.numDescriptors) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[thisAttributeIndex].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
ignoreForChangelog = false;

if (tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex].ignoreForChangelog) {
ignoreForChangelog = true;
}
}

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  tabColumns.descriptors[thisAttributeIndex].columnName.compareTo(M01_Globals_IVK.g_anIsBlockedPrice) == 0) {
ignoreForChangelog = true;
}

// ### ENDIF IVK ###
if (!(ignoreForChangelog)) {
// ### IF IVK ###
String columnTargetValue;
columnTargetValue = "";

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  tabColumns.descriptors[thisAttributeIndex].columnName.compareTo(M01_Globals.g_anStatus) == 0) {
boolean setManActConditional;
//                setManActConditional = Not isPrimaryOrg And hasIsNationalInclSubClasses And isSubjectToPreisDurchschuss
setManActConditional = !(isPrimaryOrg &  hasIsNationalInclSubClasses);

columnTargetValue = (isSubjectToPreisDurchschuss ? "CASE WHEN (autoPriceSetProductive_in = 1) AND (" + M01_Globals.g_anAhCid + " IN (" + priceAssignmentSubClassIdList + ")) THEN " + M86_SetProductive.statusReadyToBeSetProductive + " ELSE " : "") + M01_Globals_IVK.g_qualFuncNameGetLrtTargetStatus + "(" + M01_Globals.g_anAhCid + "," + "CAST('" + M01_Globals.gc_acmEntityTypeKeyClass + "' AS CHAR(1))," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (V." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActCP_in END)" : "settingManActCP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (V." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActTP_in END)" : "settingManActTP_in") + "," + (setManActConditional ? "(CASE WHEN (isFtoLrt_in = 1) AND (V." + M01_Globals_IVK.g_anIsNational + " = 0) THEN " + M01_Globals.g_dbtBoolean + "(0) ELSE settingManActSE_in END)" : "settingManActSE_in") + "," + "settingSelRelease_in" + ")" + (isSubjectToPreisDurchschuss ? " END" : "");
}
String alternativeColumnName;
alternativeColumnName = "";
if (tabColumns.descriptors[thisAttributeIndex].acmAttributeIndex > 0) {
if (M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[thisAttributeIndex].acmAttributeIndex].isExpression) {
alternativeColumnName = M04_Utilities.genSurrogateKeyName(ddlType, M24_Attribute.g_attributes.descriptors[tabColumns.descriptors[thisAttributeIndex].acmAttributeIndex].shortName + "EXP", null, null, null, null);
}
}

M12_ChangeLog.genGenChangeLogRecordForCTEDdl("updates on '" + tabColumns.descriptors[thisAttributeIndex].columnName + "'" + " (" + thisAttributeIndex + ")", fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, splitVar, qualTabNameExpr, ddlType, tabColumns.descriptors[thisAttributeIndex].columnName, alternativeColumnName, M25_Domain.g_domains.descriptors[tabColumns.descriptors[thisAttributeIndex].dbDomainIndex].dataType, clMode, tabColumns.descriptors[thisAttributeIndex].columnCategory, columnTargetValue, tabColumns.descriptors[thisAttributeIndex].isNullable);

}
}

lastRegAttrsProcessed = thisAttributeIndex;
thisAttributeIndex = thisAttributeIndex + 1;
}
allRegAttrsProcessed = allRegAttrsProcessed |  (thisAttributeIndex > tabColumns.numDescriptors);

String viewNameSuffix;
viewNameSuffix = (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate ? "CORE" : "");


qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, forNl, "CL", viewNameSuffix, null, null);

if (valuesStringForCTE.length() > 3) {
valuesStringForCTE = valuesStringForCTE.substring(0, valuesStringForCTE.length() - 3);

printCteChangeLogStatements(fileNo, acmEntityType, qualViewName, qualSourceTabName, qualSeqNameOid, isGenForming, forGen, false, hasNoIdentity, clMode, cdUserId_in, isPsTagged, splitVar, stringsPerType[], valuesStringForCTE, caseUpdateStringForCTE);

}


if ((nlAttrRefs.numDescriptors > 0)) {
//reset for nl attributes
valuesStringForCTE = "";
caseUpdateStringForCTE = "";
for (int i = 1; i <= 15; i++) {
stringsPerType[(i)] = "CASE bas.dbColumnName" + vbCrLf + "        WHEN 'DUMMYDUMMY' THEN NULL" + vbCrLf;
}
}

// generate change log records for changed NL-Text attributes
thisAttributeIndex = lastNlAttrsProcessed + 1;
while (thisAttributeIndex <= nlAttrRefs.numDescriptors) {
if (nlAttrRefs.descriptors[thisAttributeIndex].refType == M24_Attribute_Utilities.AttrDescriptorRefType.eadrtAttribute) {
// ### IF IVK ###
if (M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].isNl &  (forGen |  hasNoIdentity) == M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].isTimeVarying) {

// ### ELSE IVK ###
//           If .isNl And forGen = .isTimeVarying Then
// ### ENDIF IVK ###
String columnName;
columnName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].attributeName, ddlType, null, null, null, null, null, null);

M12_ChangeLog.genGenChangeLogRecordForCTEDdl("updates on NL-column '" + columnName + "'" + " (" + thisAttributeIndex + ")", fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, false, qualTabNameExpr, ddlType, columnName, null, M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].domainIndex].dataType, clMode, null, null, true);

// ### IF IVK ###

if (M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].isNationalizable) {
String natColumnName;
natColumnName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].attributeName, ddlType, null, null, null, null, true, null);

M12_ChangeLog.genGenChangeLogRecordForCTEDdl("updates on NL-column '" + natColumnName + "'" + " (" + thisAttributeIndex + ")", fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, false, qualTabNameExpr, ddlType, natColumnName, null, M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].domainIndex].dataType, clMode, null, null, true);


natColumnName = M04_Utilities.genAttrName(M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].attributeName + M01_Globals_IVK.gc_anSuffixNatActivated, ddlType, null, null, null, null, null, null);

M12_ChangeLog.genGenChangeLogRecordForCTEDdl("updates on NL-column '" + natColumnName + "'" + " (" + thisAttributeIndex + ")", fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, false, qualTabNameExpr, ddlType, natColumnName, null, M25_Domain.g_domains.descriptors[M24_Attribute.g_attributes.descriptors[nlAttrRefs.descriptors[thisAttributeIndex].refIndex].domainIndex].dataType, clMode, null, null, false);

}
// ### ENDIF IVK ###
}
}

lastNlAttrsProcessed = thisAttributeIndex;
thisAttributeIndex = thisAttributeIndex + 1;
}
ExitLoop:

allNlAttrsProcessed = allNlAttrsProcessed |  (thisAttributeIndex > nlAttrRefs.numDescriptors);


if ((nlAttrRefs.numDescriptors > 0)) {
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, true, "CL", viewNameSuffix, null, null);

if (valuesStringForCTE.length() > 3) {
valuesStringForCTE = valuesStringForCTE.substring(0, valuesStringForCTE.length() - 3);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM " + pc_tempTabNameChangeLogCte + ";");

printCteChangeLogStatements(fileNo, acmEntityType, qualViewName, qualSourceTabName, qualSeqNameOid, isGenForming, forGen, true, hasNoIdentity, clMode, cdUserId_in, isPsTagged, false, stringsPerType[], valuesStringForCTE, caseUpdateStringForCTE);
}
}


// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
if (isPrimaryOrg) {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out");
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out", null);
}
} else {
M07_SpLogging.genSpLogProcExit(fileNo, qualProcName, ddlType, null, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt ? "lrtOid_in" : ""), "opId_in", "#commitTs_in", "rowCount_out", null, null, null, null, null, null, null, null);
}
// ### ELSE IVK ###
//   If clMode = eclLrt Then
//     genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "rowCount_out"
//   Else
//     genSpLogProcExit fileNo, qualProcName, ddlType, , "opId_in", "#commitTs_in", "rowCount_out"
//   End If
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

seqNo = seqNo + 1;
}
}



public static void genGenChangeLogRecordDdl(int acmEntityIndex, Integer acmEntityType, String qualTabName, String qualLrtTabName, String qualSeqNameOid, String qualTabNameChangeLog, String opDescription, String lrtStateFilter,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, String dbColumnNameW, String dbColumnNameAlternativeW, Integer dbColumnTypeW, Integer clModeW, Integer columnCategoryW, Integer indentW, String valueOldW, String valueNewW, String refOidW, String logRecordOidW, String cdUserIdW, String opStatusW, Boolean addNewLineW, Boolean columnIsNullableW, String divisionOidW, Integer priceClassIndexW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

boolean forNl; 
if (forNlW == null) {
forNl = false;
} else {
forNl = forNlW;
}

String dbColumnName; 
if (dbColumnNameW == null) {
dbColumnName = "";
} else {
dbColumnName = dbColumnNameW;
}

String dbColumnNameAlternative; 
if (dbColumnNameAlternativeW == null) {
dbColumnNameAlternative = "";
} else {
dbColumnNameAlternative = dbColumnNameAlternativeW;
}

Integer dbColumnType; 
if (dbColumnTypeW == null) {
dbColumnType = M01_Common.typeId.etNone;
} else {
dbColumnType = dbColumnTypeW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

Integer columnCategory; 
if (columnCategoryW == null) {
columnCategory = M01_Common.AttrCategory.eacRegular;
} else {
columnCategory = columnCategoryW;
}

int indent; 
if (indentW == null) {
indent = 2;
} else {
indent = indentW;
}

String valueOld; 
if (valueOldW == null) {
valueOld = "";
} else {
valueOld = valueOldW;
}

String valueNew; 
if (valueNewW == null) {
valueNew = "";
} else {
valueNew = valueNewW;
}

String refOid; 
if (refOidW == null) {
refOid = "";
} else {
refOid = refOidW;
}

String logRecordOid; 
if (logRecordOidW == null) {
logRecordOid = "";
} else {
logRecordOid = logRecordOidW;
}

String cdUserId; 
if (cdUserIdW == null) {
cdUserId = "";
} else {
cdUserId = cdUserIdW;
}

String opStatus; 
if (opStatusW == null) {
opStatus = "";
} else {
opStatus = opStatusW;
}

boolean addNewLine; 
if (addNewLineW == null) {
addNewLine = true;
} else {
addNewLine = addNewLineW;
}

boolean columnIsNullable; 
if (columnIsNullableW == null) {
columnIsNullable = false;
} else {
columnIsNullable = columnIsNullableW;
}

String divisionOid; 
if (divisionOidW == null) {
divisionOid = "";
} else {
divisionOid = divisionOidW;
}

int priceClassIndex; 
if (priceClassIndexW == null) {
priceClassIndex = 0;
} else {
priceClassIndex = priceClassIndexW;
}

String entityName;
String entityTypeDescr;
String entityShortName;
// ### IF IVK ###
boolean isPsTagged;
// ### ENDIF IVK ###
boolean hasOwnTable;
String entityIdStr;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
// ### IF IVK ###
boolean hasNoIdentity;
// ### ENDIF IVK ###
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
String dbAcmEntityType;
// ### IF IVK ###
M22_Class_Utilities.NavPathFromClassToClass navPathToDiv;
boolean condenseData;
// ### ENDIF IVK ###
boolean isAggHead;

// ### IF IVK ###
navPathToDiv.relRefIndex = -1;
// ### ENDIF IVK ###
isAggHead = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityTypeDescr = "ACM-Class";
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyClass;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) & !forGen & !forNl;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
navPathToDiv = M22_Class.g_classes.descriptors[acmEntityIndex].navPathToDiv;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityTypeDescr = "ACM-Relationship";
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
hasOwnTable = true;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
relRefs.numRefs = 0;
isGenForming = false;
dbAcmEntityType = M01_Globals.gc_acmEntityTypeKeyRel;
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = false;
condenseData = false;
// ### ENDIF IVK ###

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);
} else {
return;
}

String viewNameSuffix;
// ### IF IVK ###
viewNameSuffix = (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate ? "CORE" : "");
// ### ELSE IVK ###
// viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
// ### ENDIF IVK ###
String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, forNl, "CL", viewNameSuffix, null, null);

if (addNewLine) {
M00_FileWriter.printToFile(fileNo, "");
}

M11_LRT.genProcSectionHeader(fileNo, opDescription + (forGen |  forNl ? " (" : "") + (forGen ? "GEN" : "") + (forNl ? (forGen ? "/" : "") + "NL" : "") + (forGen |  forNl ? ")" : ""), indent + 0, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dbTableName,");
if (dbColumnName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dbColumnName,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "objectId,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "refObjectId2,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "propertyType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isNational,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "csBaumuster,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr0CodeOid10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "nsr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "expexp_oid,");
}
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "validTo,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "codeKind_Id,");
if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "baseEndSlotOid,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "assignedPaintZoneKey,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "dpClassNumber,");
// ### ENDIF IVK ###

if (dbColumnName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "valueType_Id,");
// ### IF IVK ###
if (M12_ChangeLog.isClAttrCat(columnCategory, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) | (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate))) {
// ### ELSE IVK ###
//   If isClAttrCat(columnCategory, (clMode = eclLrt) Or (clMode = eclPubUpdate)) Then
// ### ENDIF IVK ###
// ### IF IVK ###
if (M03_Config.resolveCountryIdListInChangeLog &  ((columnCategory &  M01_Common.AttrCategory.eacFkCountryIdList) != 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueString,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueString,");
} else if ((columnCategory &  M01_Common.AttrCategory.eacFkOid) == 0 &  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeString)) {
// ### ELSE IVK ###
//     If (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueString,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueString,");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueTimestamp,");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueDate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueDate,");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueInteger,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueInteger,");
}
// ### IF IVK ###
if (((columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  (columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) |  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueBigInt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueBigInt,");
}
// ### ENDIF IVK ###
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueDecimal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueDecimal,");
}
if ((columnCategory &  M01_Common.AttrCategory.eacRegular)) {
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oldValueBoolean,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "newValueBoolean,");
}
}
}
}
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "lrtOid,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "isPerformedInMassupdate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "operation_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "opTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ps_Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "versionId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SELECT");
// OID
if (logRecordOid != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + logRecordOid + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NEXTVAL FOR " + qualSeqNameOid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.dbTableName,");

if (dbColumnName != "") {
//dbColumnName
if (dbColumnNameAlternative == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + dbColumnName.toUpperCase() + "',");
} else {
// ### IF IVK ###
if (columnCategory &  M01_Common.AttrCategory.eacNational) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + M04_Utilities.genAttrName(dbColumnNameAlternative, ddlType, null, null, null, null, true, null) + "',");
} else if (columnCategory &  M01_Common.AttrCategory.eacNationalBool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + dbColumnNameAlternative + M01_Globals_IVK.gc_anSuffixNatActivated.toUpperCase() + "',");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "'" + M04_Utilities.genAttrName(dbColumnNameAlternative, ddlType, null, null, null, null, null, null) + "',");
}
// ### ELSE IVK ###
//     Print #fileNo, addTab(indent + 1); "'"; genAttrName(dbColumnNameAlternative, ddlType); "',"
// ### ENDIF IVK ###
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.objectId,");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.refObjectId2,");
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.propertyType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(V.isNational, " + M01_LDM.gc_dbFalse + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.csBaumuster,");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr0CodeOid10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.sr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.nsr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.expexp_oid,");
}

if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + M01_Globals_IVK.g_anValidTo + ",");
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.codeKind_id,");
if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.baseEndSlotOid,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.assignedPaintZoneKey,");
if (divisionOid != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "COALESCE(V.divisionOid," + divisionOid + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.divisionOid,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.dpClassNumber,");
// ### ENDIF IVK ###

if (dbColumnName != "") {
// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
// ### ELSE IVK ###
//   If clMode = eclPubUpdate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + String.valueOf(M12_ChangeLog.getClColTypeByAttrType(dbColumnType)) + ",");

if (M12_ChangeLog.isClAttrCat(columnCategory, true)) {
if ((columnCategory &  M01_Common.AttrCategory.eacFkOid) == 0 &  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeString)) {
if (dbColumnType == M01_Common.typeId.etBigInt |  dbColumnType == M01_Common.typeId.etDecimal | dbColumnType == M01_Common.typeId.etDouble | dbColumnType == M01_Common.typeId.etFloat | dbColumnType == M01_Common.typeId.etInteger | dbColumnType == M01_Common.typeId.etSmallint | dbColumnType == M01_Common.typeId.etTime | dbColumnType == M01_Common.typeId.etTimestamp | dbColumnType == M01_Common.typeId.etDate | dbColumnType == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(CAST(" + valueOld + " AS CHAR(30))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "RTRIM(CAST(" + valueNew + " AS CHAR(30))),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS VARCHAR(4000)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS VARCHAR(4000)),");
}
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
if (dbColumnType == M01_Common.typeId.etTimestamp) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueNew + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS TIMESTAMP),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS TIMESTAMP),");
}
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
if (dbColumnType == M01_Common.typeId.etDate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueNew + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS DATE),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS DATE),");
}
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
if (dbColumnType == M01_Common.typeId.etInteger |  dbColumnType == M01_Common.typeId.etSmallint) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueNew + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS INTEGER),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS INTEGER),");
}
}
// ### IF IVK ###
if (((columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  (columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) |  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
if (dbColumnType == M01_Common.typeId.etBigInt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueNew + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS " + M01_Globals.g_dbtOid + "),");
}
}
// ### ENDIF IVK ###
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS DECIMAL(31,10)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS DECIMAL(31,10)),");
}
if ((columnCategory &  M01_Common.AttrCategory.eacRegular)) {
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
if (dbColumnType == M01_Common.typeId.etBoolean |  dbColumnType == M01_Common.typeId.etSmallint) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueOld + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + valueNew + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueOld + " AS " + M01_Globals.g_dbtBoolean + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CAST(" + valueNew + " AS " + M01_Globals.g_dbtBoolean + "),");
}
}
}
}
} else {
if (dbColumnNameAlternative == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t,");
}

if (M12_ChangeLog.isClAttrCat(columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
// ### IF IVK ###
if (M03_Config.resolveCountryIdListInChangeLog &  ((columnCategory &  M01_Common.AttrCategory.eacFkCountryIdList) != 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(SELECT IDLIST FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE OID = V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(SELECT IDLIST FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE OID = " + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "CAST(RTRIM(CAST(" + valueNew + " AS CHAR(254))) AS VARCHAR(4000))" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n") + "),");
} else if ((columnCategory &  M01_Common.AttrCategory.eacFkOid) == 0 &  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeString)) {
// ### ELSE IVK ###
//       If (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "CAST(RTRIM(CAST(" + valueNew + " AS CHAR(254))) AS VARCHAR(4000))" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n") + ",");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_To,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "TIMESTAMP(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Tn") + ",");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dto,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "DATE(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dtn") + ",");
}
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Io,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "INTEGER(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_In") + ",");
}
// ### IF IVK ###
if (((columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  (columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0) |  M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
if (dbColumnNameAlternative == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? M01_Globals.g_dbtOid + "(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIn") + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? M01_Globals.g_dbtOid + "(" + valueNew + ")" : "V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIn") + ",");
}
}
// ### ENDIF IVK ###
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Do,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? "DECIMAL(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dn") + ",");
}
if ((columnCategory &  M01_Common.AttrCategory.eacRegular)) {
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + (clMode == M12_ChangeLog.ChangeLogMode.eclLrt &  valueNew != "" ? M01_Globals.g_dbtBoolean + "(" + valueNew + ")" : "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bn") + ",");
}
}
}
}
}

// lrtOid
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "lrtOid_in,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.isPerformedInMassupdate,");

if (opStatus == "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.operation_Id,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + opStatus + ",");
}

// opTimestamp
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "CURRENT TIMESTAMP,");
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "v_currentTimestamp,");
// ### ENDIF IVK ###
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "commitTs_in,");
}
// cdUserId
if (!(cdUserId.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + cdUserId + ",");
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "v_cdUserId,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.cdUserId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.ps_Oid,");

// versionId"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualViewName + " V");

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M92_DBUtilities.tempTabNameOids + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.orParEntityType = '" + dbAcmEntityType + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.orParEntityId = '" + entityIdStr + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.opId = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.isNl = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.isGen = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "E.oid = V.objectId");
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + qualTabName + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.objectId = T." + M01_Globals.g_anOid);
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "WHERE");

if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.objectId = " + refOid);
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "T." + M01_Globals.g_anStatus + " < v_targetState");

if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anIsNational + " = forNational_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + (isAggHead ? M01_Globals.g_anCid : M01_Globals.g_anAhCid) + " = classId_in");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "T." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
}

if (!(isAggHead)) {
String qualTabNameGenericAspect;
qualTabNameGenericAspect = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameGenericAspect + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anOid + " = T." + M01_Globals.g_anAhOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anPsOid + " = v_psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anInLrt + " IS NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anIsNational + " = forNational_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
} else {
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd &  condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(1=1) -- nothing to filter");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.operation_Id IN (" + lrtStateFilter + ")");
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + M01_Globals.g_anLrtOid + " = lrtOid_in");
} else if (!(condenseData)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.status_Id = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd & ! condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.ps_Oid = psOid_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V.divisionOid = v_divisionOid");
}
}

if (dbColumnName != "") {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
String colNewSuffix;
String colOldSuffix;
String colOldSuffixAlternative;
String colNewSuffixAlternative;
if ((columnCategory &  M01_Common.AttrCategory.eacFkOid)) {
colOldSuffix = "_BIo";
colNewSuffix = "_BIn";
} else {
colOldSuffix = "_o";
colNewSuffix = "_n";
colOldSuffixAlternative = "_BIo";
colNewSuffixAlternative = "_BIn";
}

if (columnIsNullable) {
if ((columnCategory &  M01_Common.AttrCategory.eacExpression) == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "NOT (V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " IS NULL AND V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + " IS NULL)");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " <> V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
} else {
//Special case: column is expression
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NOT (V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " IS NULL AND V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " <> V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "NOT (V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffixAlternative + " IS NULL AND V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffixAlternative + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffixAlternative + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffixAlternative + " IS NULL)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffixAlternative + " <> V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffixAlternative + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
} else {
if ((columnCategory &  M01_Common.AttrCategory.eacExpression) == 0) {
if (dbColumnName == M01_Globals.g_anStatus) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_Io" + " <> " + valueNew);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " <> V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix);
}
} else {
//Special case: column is expression
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffix + " <> V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffix);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colOldSuffixAlternative + " <> V." + dbColumnNameAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + colNewSuffixAlternative);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + ")");
}
}
}
// ### ENDIF IVK ###
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ((clMode == M12_ChangeLog.ChangeLogMode.eclLrt |  clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) ? "WITH UR" : "") + ";");

if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
// ### ELSE IVK ###
// Print #fileNo, addTab(indent + 0); IIf(clMode = eclLrt, "WITH UR", ""); ";"
//
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "-- count affected rows");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "GET DIAGNOSTICS v_rowCount = ROW_COUNT;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "SET rowCount_out = rowCount_out + v_rowCount;");
}
}




public static void genChangeLogViewDdlHeader(int acmEntityIndex, Integer acmEntityType, String qualTargetTabName,  int thisOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Integer clModeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
String entityIdStr;
boolean isGenForming;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
boolean isLogChange;
boolean M03_Config.useMqtToImplementLrt;
// ### IF IVK ###
boolean hasNoIdentity;
boolean condenseData;
// ### ENDIF IVK ###

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
isLogChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isLogChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
isGenForming = false;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
hasNoIdentity = false;
condenseData = false;
// ### ENDIF IVK ###
} else {
return;
}

// ####################################################################################################################
// #    ChangeLog-View for entity
// ####################################################################################################################

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

String viewNameSuffix;
// ### IF IVK ###
viewNameSuffix = (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate ? "CORE" : "");
// ### ELSE IVK ###
// viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
// ### ENDIF IVK ###

String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, null, "CL", viewNameSuffix, null, null);
M22_Class_Utilities.printSectionHeader("ChangeLog-View (" + M12_ChangeLog.genClModeDescription(clMode) + ") for table \"" + qualTargetTabName + "\" (ACM-" + (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass ? "Class" : "Relationship") + "\"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dbTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "objectId,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refObjectId2,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "propertyType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isNational,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "csBaumuster,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "expexp_oid,");
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "validTo,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "codeKind_id,");
//Print #fileNo, addTab(1); "codeGroup???Key3,"
//Print #fileNo, addTab(1); "codeGroup???Key2,"
//Print #fileNo, addTab(1); "codeGroup???Key,"
if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseEndSlotOid,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "assignedPaintZoneKey,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dpClassNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPerformedInMassupdate,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "lrtOid,");
// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !(clMode == M12_ChangeLog.ChangeLogMode.eclSetProd &  condenseData)) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operation_Id,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "status_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operationTimestamp,");

// make sure that 'LastUpdateTimeStamp' is handled as attribute
// guess we do not need this any more
int domainIndexModTs;
if (isLogChange) {
domainIndexModTs = M01_Globals.g_domainIndexModTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals.g_anLastUpdateTimestamp, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conLastUpdateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexModTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// make sure that 'validFrom' and 'validTo' are handled as attribute
int domainIndexValidTs;
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
domainIndexValidTs = M01_Globals.g_domainIndexValTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidFrom, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidFrom, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidTo, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidTo, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
Integer attrTypeId;
attrTypeId = M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType;
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n,");

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_To,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Tn,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dto,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dtn,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Io,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_In,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIn,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Do,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dn,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bo,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bn,");
}
}
}
}

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "cdUserId,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ps_Oid");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
}


public static void genChangeLogViewDdl(int acmEntityIndex, Integer acmEntityType, String qualSourceTabName, String qualSourceGenTabName, String qualSourceNlTabName, String qualTargetTabName, String qualTargetNlTabName, String qualAggHeadTabName,  int thisOrgIndex, int srcPoolIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Integer clModeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
boolean hasOwnTable;
String entityIdStr;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
boolean hasNlAttributes;
boolean isLogChange;
boolean checkAggHeadForAttrs;
int aggHeadClassIndex;
boolean isAggHead;
boolean isAbstract;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMappingAh;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsAh;
boolean includeAggHeadInJoinPath;
boolean includeGenInJoinPath;
boolean includeAggHeadGenInJoinPath;
String aggHeadReferredColumns;
String aggHeadGenReferredColumns;
String genReferredColumns;
boolean aggHeadSupportMqt;
boolean useMqtToImplementLrtForEntity;
boolean ignoreForChangelog;
// ### IF IVK ###
boolean isPsTagged;
boolean hasNoIdentity;
int allowedCountriesRelIndex;
int disAllowedCountriesRelIndex;
int allowedCountriesListRelIndex;
int disAllowedCountriesListRelIndex;
boolean condenseData;
boolean isNationalizable;
// ### ENDIF IVK ###

includeAggHeadInJoinPath = false;
includeGenInJoinPath = false;
includeAggHeadGenInJoinPath = false;
isAggHead = false;
isAbstract = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
isLogChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isNationalizable = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
allowedCountriesRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].allowedCountriesRelIndex;
disAllowedCountriesRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].disAllowedCountriesRelIndex;
allowedCountriesListRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].allowedCountriesListRelIndex;
disAllowedCountriesListRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].disAllowedCountriesListRelIndex;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;

checkAggHeadForAttrs = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  ((M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex != M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) |  forGen);
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isLogChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;

sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
hasOwnTable = true;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
relRefs.numRefs = 0;
isGenForming = false;
hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);

aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
checkAggHeadForAttrs = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = false;
isNationalizable = false;
allowedCountriesRelIndex = -1;
disAllowedCountriesRelIndex = -1;
allowedCountriesListRelIndex = -1;
disAllowedCountriesListRelIndex = -1;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = false;
// ### ENDIF IVK ###
} else {
return;
}

aggHeadSupportMqt = false;
if (checkAggHeadForAttrs) {
attrMappingAh = M22_Class.g_classes.descriptors[aggHeadClassIndex].clMapAttrsInclSubclasses;
relRefsAh = M22_Class.g_classes.descriptors[aggHeadClassIndex].relRefsRecursive;
aggHeadSupportMqt = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[aggHeadClassIndex].useMqtToImplementLrt;
}

if (ignoreForChangelog) {
return;
}

String tupVarSrc;
String tupVarSrcGen;
String tupVarSrcPar;
String tupVarSrcParGen;
String tupVarTgt;
String tupVarAh;

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
tupVarSrc = "PRIV";
tupVarSrcGen = "GEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = "PUB";
tupVarAh = "AH";
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
tupVarSrc = "OBJ";
tupVarSrcGen = "OBJGEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = " - no used -";
tupVarAh = "AH";
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
tupVarSrc = "OBJ";
tupVarSrcGen = "OBJGEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = " - no used -";
tupVarAh = "AH";
// ### ENDIF IVK ###
} else {
tupVarSrc = "SRC";
tupVarSrcGen = "SRCGEN";
tupVarSrcPar = "SRCPAR";
tupVarSrcParGen = "SRCPARGEN";
tupVarTgt = "TGT";
tupVarAh = "AH";
}

boolean parTabIsAhTab;
parTabIsAhTab = (aggHeadClassIndex == acmEntityIndex) &  (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass);

// ####################################################################################################################
// #    ChangeLog-View for entity
// ####################################################################################################################

//separate some code to avoid 'Procedure too large' - errors
M12_ChangeLog.genChangeLogViewDdlHeader(acmEntityIndex, acmEntityType, qualTargetTabName, thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, clMode);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

String viewNameSuffix;
// ### IF IVK ###
viewNameSuffix = (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate ? "CORE" : "");
// ### ELSE IVK ###
// viewNameSuffix = IIf(clMode = eclPubUpdate , "CORE", "")
// ### ENDIF IVK ###
String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, null, "CL", viewNameSuffix, null, null);

// make sure that 'LastUpdateTimeStamp' is handled as attribute
// guess we do not need this any more
int domainIndexModTs;
if (isLogChange) {
domainIndexModTs = M01_Globals.g_domainIndexModTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals.g_anLastUpdateTimestamp, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conLastUpdateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexModTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// make sure that 'validFrom' and 'validTo' are handled as attribute
int domainIndexValidTs;
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
domainIndexValidTs = M01_Globals.g_domainIndexValTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidFrom, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidFrom, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidTo, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidTo, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// entityId / entityType
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityId");
if (hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
} else {
if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcPar + "." + M01_Globals.g_anCid + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anCid + ",");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M01_Globals.gc_acmEntityTypeKeyClass + "',");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M01_Globals.gc_acmEntityTypeKeyRel + "',");
}
// ahClassId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ahClassId");
if (aggHeadClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhCid + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
}
// ahObjectId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ahObjectId");
if (aggHeadClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + ",");
}
// gen
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- gen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? "1," : "0,"));
// nl
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
//dbTableName
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- dbTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.getObjBaseName(qualTargetTabName, null) + "',");
// objectId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + ",");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
// refClassId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refClassId1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + relLeftClassIdStr + "',");
// refObjectId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refObjectId1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + relLeftFk + ",");
// refClassId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refClassId2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + relRightClassIdStr + "',");
// refObjectId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refObjectId2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + relRightFk + ",");
}

// ### IF IVK ###
int priceTargetClassIndex;
int priceTargetClassIndexAh;
String priceFkAttrName;
String priceQualObjName;
boolean includeAggHeadInJoinPathForPrice;
// price

boolean foundPrice;
boolean foundPriceInAggHead;

foundPrice = M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, (isAggHead &  forGen ? tupVarSrcPar : tupVarAh), tupVarSrcGen, null, foundPriceInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);

if (includeAggHeadInJoinPathForPrice & ! isAggHead) {
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  includeAggHeadInJoinPathForPrice;
}

// ### ENDIF IVK ###
// ### IF IVK ###
int propertyTargetClassIndex;
int propertyTargetClassIndexAh;
String propertyFkAttrName;
String propertyQualObjName;
boolean includeAggHeadInJoinPathForPropertyOid;
// propertyOid

boolean foundPropertyOid;
boolean foundPropertyOidInAggHead;

foundPropertyOid = M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, propertyTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", propertyQualObjName, propertyFkAttrName, includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, (isAggHead &  forGen ? tupVarSrcPar : tupVarAh), tupVarSrcGen, null, foundPropertyOidInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);

if (includeAggHeadInJoinPathForPropertyOid & ! isAggHead) {
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  includeAggHeadInJoinPathForPropertyOid;
}

int propertyTypeTargetClassIndex;
int propertyTypeTargetClassIndexAh;
String propertyTypeFkAttrName;
String propertyTypeQualObjName;
// propertyType_Id
if (foundPropertyOid) {
String qualObjNamePropertyGen;

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
qualObjNamePropertyGen = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].classIndex, ddlType, thisOrgIndex, srcPoolIndex, true, true, M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].useMqtToImplementLrt, null, null, null, null, null);
} else {
qualObjNamePropertyGen = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].classIndex, ddlType, thisOrgIndex, srcPoolIndex, true, null, null, null, null, null, null);
}

// Fixme: get rid of hard-coding here!!
if (propertyTargetClassIndex > 0 |  propertyTargetClassIndexAh > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = PRP." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
} else if (foundPropertyOidInAggHead) {
if (isAggHead) {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", propertyTypeQualObjName, propertyTypeFkAttrName, true, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, (forGen ? tupVarSrcPar : tupVarAh), tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = " + tupVarAh + "." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = " + tupVarSrc + "." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", propertyTypeQualObjName, propertyTypeFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

// isNational
if (isNationalizable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- isNational");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals_IVK.g_anIsNational + ",");
} else {
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "isNational", M01_LDM.gc_dbFalse, forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtBoolean, 2, true, tupVarSrc, tupVarAh, null, null, aggHeadReferredColumns, null, null);
}

// csBaumuster
int csBaumusterTargetClassIndex;
int csBaumusterTargetClassIndexAh;
String csBaumusterFkAttrName;
String csBaumusterQualObjName;
boolean csBaumusterFoundInAggHead;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- csBaumuster");

if (M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, csBaumusterFoundInAggHead, null, null, null, aggHeadReferredColumns, null, null, true, true)) {

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(");

// FixMe: for csBaumuster we only navigate along 'direct' relationships (i.e. not related to aggregate head)
// A more generic criterion would be to check with which relationship this class effectively can be related to an Aggregate Head having a 'csBaumuster'-relationship
// E.g.: A DecisionTable may never have an NSR1Validity as Aggregate Head which then is related to some SR0Validity carying 'baumuster'
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "csBaumuster", "CSB", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "CAST(NULL AS VARCHAR(1))", 2, true, tupVarSrc, tupVarAh, null, null, aggHeadReferredColumns, null, true);
if (csBaumusterTargetClassIndex > 0 &  csBaumusterTargetClassIndexAh <= 0) {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, false, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, true);
}
csBaumusterTargetClassIndexAh = 0;
M00_FileWriter.printToFile(fileNo, "CAST(NULL AS VARCHAR(8))),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS VARCHAR(8)),");
}

// sr0Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "sr0Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrc, tupVarAh, null, null, aggHeadReferredColumns, null, null);

int s0_01TargetClassIndex;
int s0_01TargetClassIndexAh;
String s0_01FkAttrName;
String s0_01QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code01", "S01", s0_01TargetClassIndex, s0_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_01QualObjName, s0_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_02TargetClassIndex;
int s0_02TargetClassIndexAh;
String s0_02FkAttrName;
String s0_02QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code02", "S02", s0_02TargetClassIndex, s0_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_02QualObjName, s0_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_03TargetClassIndex;
int s0_03TargetClassIndexAh;
String s0_03FkAttrName;
String s0_03QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code03", "S03", s0_03TargetClassIndex, s0_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_03QualObjName, s0_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_04TargetClassIndex;
int s0_04TargetClassIndexAh;
String s0_04FkAttrName;
String s0_04QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code04", "S04", s0_04TargetClassIndex, s0_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_04QualObjName, s0_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_05TargetClassIndex;
int s0_05TargetClassIndexAh;
String s0_05FkAttrName;
String s0_05QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code05", "S05", s0_05TargetClassIndex, s0_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_05QualObjName, s0_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_06TargetClassIndex;
int s0_06TargetClassIndexAh;
String s0_06FkAttrName;
String s0_06QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code06", "S06", s0_06TargetClassIndex, s0_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_06QualObjName, s0_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_07TargetClassIndex;
int s0_07TargetClassIndexAh;
String s0_07FkAttrName;
String s0_07QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code07", "S07", s0_07TargetClassIndex, s0_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_07QualObjName, s0_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_08TargetClassIndex;
int s0_08TargetClassIndexAh;
String s0_08FkAttrName;
String s0_08QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code08", "S08", s0_08TargetClassIndex, s0_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_08QualObjName, s0_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_09TargetClassIndex;
int s0_09TargetClassIndexAh;
String s0_09FkAttrName;
String s0_09QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code09", "S09", s0_09TargetClassIndex, s0_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_09QualObjName, s0_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s0_10TargetClassIndex;
int s0_10TargetClassIndexAh;
String s0_10FkAttrName;
String s0_10QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code10", "S10", s0_10TargetClassIndex, s0_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_10QualObjName, s0_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// sr0CodeOids
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_01TargetClassIndex > 0 |  s0_01TargetClassIndexAh > 0 ? "S01." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_02TargetClassIndex > 0 |  s0_02TargetClassIndexAh > 0 ? "S02." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_03TargetClassIndex > 0 |  s0_03TargetClassIndexAh > 0 ? "S03." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_04TargetClassIndex > 0 |  s0_04TargetClassIndexAh > 0 ? "S04." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_05TargetClassIndex > 0 |  s0_05TargetClassIndexAh > 0 ? "S05." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_06TargetClassIndex > 0 |  s0_06TargetClassIndexAh > 0 ? "S06." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_07TargetClassIndex > 0 |  s0_07TargetClassIndexAh > 0 ? "S07." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_08TargetClassIndex > 0 |  s0_08TargetClassIndexAh > 0 ? "S08." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_09TargetClassIndex > 0 |  s0_09TargetClassIndexAh > 0 ? "S09." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_10TargetClassIndex > 0 |  s0_10TargetClassIndexAh > 0 ? "S10." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");

// sr1Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "sr1Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);

int s1_01TargetClassIndex;
int s1_01TargetClassIndexAh;
String s1_01FkAttrName;
String s1_01QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code01", "S101", s1_01TargetClassIndex, s1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_01QualObjName, s1_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_02TargetClassIndex;
int s1_02TargetClassIndexAh;
String s1_02FkAttrName;
String s1_02QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code02", "S102", s1_02TargetClassIndex, s1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_02QualObjName, s1_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_03TargetClassIndex;
int s1_03TargetClassIndexAh;
String s1_03FkAttrName;
String s1_03QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code03", "S103", s1_03TargetClassIndex, s1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_03QualObjName, s1_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_04TargetClassIndex;
int s1_04TargetClassIndexAh;
String s1_04FkAttrName;
String s1_04QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code04", "S104", s1_04TargetClassIndex, s1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_04QualObjName, s1_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_05TargetClassIndex;
int s1_05TargetClassIndexAh;
String s1_05FkAttrName;
String s1_05QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code05", "S105", s1_05TargetClassIndex, s1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_05QualObjName, s1_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_06TargetClassIndex;
int s1_06TargetClassIndexAh;
String s1_06FkAttrName;
String s1_06QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code06", "S106", s1_06TargetClassIndex, s1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_06QualObjName, s1_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_07TargetClassIndex;
int s1_07TargetClassIndexAh;
String s1_07FkAttrName;
String s1_07QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code07", "S107", s1_07TargetClassIndex, s1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_07QualObjName, s1_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_08TargetClassIndex;
int s1_08TargetClassIndexAh;
String s1_08FkAttrName;
String s1_08QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code08", "S108", s1_08TargetClassIndex, s1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_08QualObjName, s1_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_09TargetClassIndex;
int s1_09TargetClassIndexAh;
String s1_09FkAttrName;
String s1_09QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code09", "S109", s1_09TargetClassIndex, s1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_09QualObjName, s1_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int s1_10TargetClassIndex;
int s1_10TargetClassIndexAh;
String s1_10FkAttrName;
String s1_10QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code10", "S110", s1_10TargetClassIndex, s1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_10QualObjName, s1_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// nsr1Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "nsr1Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);

int ns1_01TargetClassIndex;
int ns1_01TargetClassIndexAh;
String ns1_01FkAttrName;
String ns1_01QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code01", "N101", ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_01QualObjName, ns1_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_02TargetClassIndex;
int ns1_02TargetClassIndexAh;
String ns1_02FkAttrName;
String ns1_02QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code02", "N102", ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_02QualObjName, ns1_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_03TargetClassIndex;
int ns1_03TargetClassIndexAh;
String ns1_03FkAttrName;
String ns1_03QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code03", "N103", ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_03QualObjName, ns1_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_04TargetClassIndex;
int ns1_04TargetClassIndexAh;
String ns1_04FkAttrName;
String ns1_04QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code04", "N104", ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_04QualObjName, ns1_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_05TargetClassIndex;
int ns1_05TargetClassIndexAh;
String ns1_05FkAttrName;
String ns1_05QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code05", "N105", ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_05QualObjName, ns1_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_06TargetClassIndex;
int ns1_06TargetClassIndexAh;
String ns1_06FkAttrName;
String ns1_06QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code06", "N106", ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_06QualObjName, ns1_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_07TargetClassIndex;
int ns1_07TargetClassIndexAh;
String ns1_07FkAttrName;
String ns1_07QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code07", "N107", ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_07QualObjName, ns1_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_08TargetClassIndex;
int ns1_08TargetClassIndexAh;
String ns1_08FkAttrName;
String ns1_08QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code08", "N108", ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_08QualObjName, ns1_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_09TargetClassIndex;
int ns1_09TargetClassIndexAh;
String ns1_09FkAttrName;
String ns1_09QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code09", "N109", ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_09QualObjName, ns1_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

int ns1_10TargetClassIndex;
int ns1_10TargetClassIndexAh;
String ns1_10FkAttrName;
String ns1_10QualObjName;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code10", "N110", ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_10QualObjName, ns1_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// slotPlausibilityRuleType_ID
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "slotPlausibilityRuleType_ID", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtEnumId, 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);

// with
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "with", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);
// withNot
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "withNot", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);
// expression
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "expression", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, tupVarSrc, tupVarAh, null, null, null, aggHeadReferredColumns, null);

// ### ENDIF IVK ###
// validFrom / validTo
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- validFrom");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- validTo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals_IVK.g_anValidTo + ",");
}

// ### IF IVK ###
int bcTargetClassIndex;
int bcTargetClassIndexAh;
String bcFkAttrName;
String bcQualObjName;
String bcReferredColumnList;

// baseCodeNumber, baseCodeType and codeKind
boolean baseCodeNumberFoundInAh;
if (M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseCodeNumber", "BC", bcTargetClassIndex, bcTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", bcQualObjName, bcFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, baseCodeNumberFoundInAh, null, null, bcReferredColumnList, aggHeadReferredColumns, null, null, null, null)) {

String tupVarRef;
if (bcTargetClassIndex > 0 |  bcTargetClassIndexAh > 0) {
tupVarRef = "BC";
M04_Utilities.addStrListElem(bcReferredColumnList, M01_Globals_IVK.g_anCodeNumber);
M04_Utilities.addStrListElem(bcReferredColumnList, "CTLTLV_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, "CTYTYP_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, "CDIDIV_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, M01_Globals_IVK.g_anIsNational);
} else {
if (baseCodeNumberFoundInAh) {
M04_Utilities.addStrListElem(aggHeadReferredColumns, M01_Globals_IVK.g_anCodeNumber);
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CTLTLV_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CTYTYP_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CDIDIV_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, M01_Globals_IVK.g_anIsNational);
tupVarRef = tupVarAh;
} else {
tupVarRef = tupVarSrc;
}
}

// FIXME: get rid of this hard-coded column name
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- baseCodeType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT T.CODETYPENUMBER FROM " + M01_Globals_IVK.g_qualTabNameCodeType + " T WHERE T." + M01_Globals.g_anOid + " = " + tupVarRef + ".CTYTYP_OID),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- codeKind");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE " + tupVarRef + "." + M01_Globals_IVK.g_anIsNational + " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- baseCodeType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS CHAR(1)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- codeKind");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtEnumId + "),");
}

int ctTargetClassIndex;
int ctTargetClassIndexAh;
String ctFkAttrName;
String ctQualObjName;

int endSlotTargetClassIndex;
int endSlotTargetClassIndexAh;
String endSlotFkAttrName;
String endSlotQualObjName;

if (M03_Config.cr132) {
// baseEndSlotOid
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseEndSlotOid", "BES", endSlotTargetClassIndex, endSlotTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", endSlotQualObjName, endSlotFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

int slotTypeTargetClassIndex;
int slotTypeTargetClassIndexAh;
String slotTypeFkAttrName;
String slotTypeQualObjName;
boolean slotTypeIdIsGen;
boolean slotTypeIdIsAggHead;
boolean slotTypeIdIsAggHeadGen;
// slotType_Id
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "slotType_Id", "BEG", slotTypeTargetClassIndex, slotTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", slotTypeQualObjName, slotTypeFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, null, slotTypeIdIsAggHead, slotTypeIdIsGen, slotTypeIdIsAggHeadGen, null, aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns, null, null);
includeGenInJoinPath = includeGenInJoinPath |  (slotTypeIdIsGen & ! slotTypeIdIsAggHeadGen & slotTypeTargetClassIndex <= 0 & slotTypeTargetClassIndexAh <= 0);
includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath |  (slotTypeIdIsAggHeadGen & ! slotTypeIdIsGen & slotTypeTargetClassIndex <= 0 & slotTypeTargetClassIndexAh <= 0);
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  slotTypeIdIsAggHead;

// allowedCountries
String qualCountryListFuncName;
String qualCountryListFuncNameSuffix;
// for LRT- and SETPRODUCTIVE changelog use 'country-list-function' aware of 'deleted records'
qualCountryListFuncNameSuffix = ((clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) ? "_D" : "");

int acTargetClassIndex;
int acTargetClassIndexAh;
String acFkAttrName;
String acQualObjName;
String acReferredColumnList;

if (allowedCountriesRelIndex > 0) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[allowedCountriesRelIndex].sectionIndex, M01_ACM_IVK.udfnAllowedCountry2Str0 + qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- aclacl_oid");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + "," + tupVarSrc + "." + M01_Globals.g_anInLrt + ",1024)),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + ",1024)),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "aclacl_oid", "AC", acTargetClassIndex, acTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", acQualObjName, acFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, acReferredColumnList, aggHeadReferredColumns, null, null, null, null);
}

// disallowedCountries
int dcTargetClassIndex;
int dcTargetClassIndexAh;
String dcFkAttrName;
String dcQualObjName;
String dcReferredColumnList;

if (disAllowedCountriesRelIndex > 0) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[disAllowedCountriesRelIndex].sectionIndex, M01_ACM_IVK.udfnDisallowedCountry2Str0 + qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- dcldcl_oid");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + "," + tupVarSrc + "." + M01_Globals.g_anInLrt + ",1024)),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + ",1024)),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "dcldcl_oid", "DC", dcTargetClassIndex, dcTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", dcQualObjName, dcFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, dcReferredColumnList, aggHeadReferredColumns, null, null, null, null);
}

// assignedPaintZoneKey
int assignedPzkTargetClassIndex;
int assignedPzkTargetClassIndexAh;
String assignedPzkFkAttrName;
String assignedPzkQualObjName;
boolean assignedPzkIsGen;
boolean assignedPzkIsAggHead;
boolean assignedPzkIsAggHeadGen;

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, M01_ACM_IVK.conAssignedPaintZoneKey, "BEG", assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, "CAST(NULL AS VARCHAR(15))", assignedPzkQualObjName, assignedPzkFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, null, assignedPzkIsAggHead, assignedPzkIsGen, assignedPzkIsAggHeadGen, null, aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns, null, null);
includeGenInJoinPath = includeGenInJoinPath |  (assignedPzkIsGen & ! assignedPzkIsAggHeadGen & assignedPzkTargetClassIndex <= 0 & assignedPzkIsAggHeadGen <= 0);
includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath |  (assignedPzkIsAggHeadGen &  assignedPzkIsGen & assignedPzkTargetClassIndex <= 0 & assignedPzkIsAggHeadGen <= 0);
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  assignedPzkIsAggHead;

//includeAggHeadInJoinPath = includeAggHeadInJoinPath Or (clMode = eclSetProd)


int divisionTargetClassIndex;
int divisionTargetClassIndexAh;
String divisionFkAttrName;
String divisionQualObjName;
String divisionReferredColumnList;
boolean noJoinForDivisionOid;
noJoinForDivisionOid = (aggHeadClassIndex == M01_Globals_IVK.g_classIndexGenericCode) &  (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) & !isPsTagged;

// divisionOid
if (bcTargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BC.CDIDIV_OID,");
} else if (noJoinForDivisionOid) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + "DIV_OID,");
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "divisionOid", "DIV", divisionTargetClassIndex, divisionTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", divisionQualObjName, divisionFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, false, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, divisionReferredColumnList, aggHeadReferredColumns, null, null, null, null);
}

// dpClassNumber
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, M01_ACM_IVK.conDpClassNumber, "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "SMALLINT", 2, true, tupVarSrc, tupVarSrcGen, (parTabIsAhTab ? tupVarSrcPar : tupVarAh), tupVarSrcPar, null, aggHeadReferredColumns, null);

// isPerformedInMassupdate
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- isPerformedInMassupdate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
// ### ENDIF IVK ###
// lrtOid
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- lrtOid");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anInLrt + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !(clMode == M12_ChangeLog.ChangeLogMode.eclSetProd &  condenseData)) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
// operation_Id
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- operation_Id");

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".LRTSTATE,");
// ### IF IVK ###
} else {
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST((CASE WHEN " + tupVarTgt + "." + M01_Globals.g_anOid + " IS NULL THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " ELSE " + String.valueOf(M11_LRT.lrtStatusUpdated) + " END) AS " + M01_Globals.g_dbtEnumId + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST((CASE WHEN " + tupVarSrc + "." + M01_ACM_IVK.conIsDeleted + " = 1 THEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " WHEN " + tupVarTgt + "." + M01_Globals.g_anOid + " IS NULL THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " ELSE " + String.valueOf(M11_LRT.lrtStatusUpdated) + " END) AS " + M01_Globals.g_dbtEnumId + "),");
}
// ### ENDIF IVK ###
}
}

// ### IF IVK ###
// status_Id
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- status_Id");
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtEnumId + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anStatus + ",");
}

// ### ENDIF IVK ###
// operationTimestamp
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- operationTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anLastUpdateTimestamp + ",");

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
Integer attrTypeId;
attrTypeId = M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType;

// valueType
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M12_ChangeLog.getClColTypeByAttrType(attrTypeId)) + ",");

// ### IF IVK ###
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression)) {
String oldVal;
String newVal;
M24_Attribute_Utilities.AttributeListTransformation transformationExpr;
M24_Attribute_Utilities.initAttributeTransformation(transformationExpr, 0, null, null, null, tupVarTgt + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformationExpr, thisOrgIndex, dstPoolIndex, tupVarTgt, null, null);
newVal = M04_Utilities.transformAttrName(tabColumns.descriptors[i].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformationExpr, ddlType, null, null, null, null, tabColumns.descriptors[i].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueExpression, null, null, null, tabColumns.descriptors[i].columnCategory);
transformationExpr.attributePrefix = tupVarSrc + ".";
M24_Attribute_Utilities.setAttributeTransformationContext(transformationExpr, thisOrgIndex, srcPoolIndex, tupVarSrc, null, clMode == M12_ChangeLog.ChangeLogMode.eclLrt);
oldVal = M04_Utilities.transformAttrName(tabColumns.descriptors[i].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformationExpr, ddlType, null, null, null, null, tabColumns.descriptors[i].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueExpression, null, null, null, tabColumns.descriptors[i].columnCategory);

if (attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + newVal + " AS CHAR(30))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + oldVal + " AS CHAR(30))),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + newVal + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + oldVal + ",");
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
// oldValueString / newValueString
if (attrTypeId == M01_Common.typeId.etBigInt |  attrTypeId == M01_Common.typeId.etDecimal | attrTypeId == M01_Common.typeId.etDouble | attrTypeId == M01_Common.typeId.etFloat | attrTypeId == M01_Common.typeId.etInteger | attrTypeId == M01_Common.typeId.etSmallint | attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS CHAR(30))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS CHAR(30))),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(4000)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(4000)),");
}

// oldValueTimestamp / newValueTimestamp
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}

// oldValueDate / newValueDate
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}

// oldValueInteger / newValueInteger
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}

// oldValueBigInt / newValueBigInt
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}

// oldValueDecimal / newValueDecimal
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS DECIMAL(31,10)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS DECIMAL(31,10)),");
}

// oldValueBoolean / newValueBoolean
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}
}

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
// cdUserId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + tupVarSrc + "." + M01_Globals.g_anUpdateUser + "," + tupVarSrc + "." + M01_Globals.g_anCreateUser + ",'-unk-'),");
}

// ps_Oid
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ps_Oid");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals_IVK.g_anPsOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
}
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualSourceTabName + " " + tupVarSrc);

if (M03_Config.referToAggHeadInChangeLog &  checkAggHeadForAttrs & includeAggHeadInJoinPath) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");

M22_Class.genTabSubQueryByEntityIndex(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt), forGen, "AH", aggHeadReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + " = " + tupVarAh + "." + M01_Globals.g_anOid);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarAh + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarAh + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarAh + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarAh + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarAh; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarAh + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarAh + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarAh + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarAh + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarAh + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}

if (includeAggHeadGenInJoinPath) {
String aggHeadFkAttrName;
aggHeadFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName, null, null, null, null);
aggHeadGenReferredColumns = aggHeadGenReferredColumns + (aggHeadGenReferredColumns.compareTo("") == 0 ? "" : ",") + aggHeadFkAttrName;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");

M22_Class.genTabSubQueryByEntityIndex(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt), true, tupVarSrcGen, aggHeadGenReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + "" + " = " + tupVarSrcGen + "." + aggHeadFkAttrName);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarSrcGen + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcGen + ".ROWNUM = 1");
} else if (includeGenInJoinPath) {
genReferredColumns = genReferredColumns + (genReferredColumns.compareTo("") == 0 ? "" : ",") + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M22_Class.genTabSubQueryByEntityIndex(acmEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, clMode == M12_ChangeLog.ChangeLogMode.eclLrt, true, tupVarSrcGen, genReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + " = " + tupVarSrcGen + "." + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null));

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarSrcGen + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcGen + ".ROWNUM = 1");
}

if (forGen) {
String qualViewNameNonGen;
qualViewNameNonGen = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, false, true, useMqtToImplementLrtForEntity, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameNonGen + " " + tupVarSrcPar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null) + " = " + tupVarSrcPar + "." + M01_Globals.g_anOid);
}

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetTabName + " " + tupVarTgt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + " = " + tupVarTgt + "." + M01_Globals.g_anOid);
}

// ### IF IVK ###
genCondOuterJoin(fileNo, propertyTargetClassIndex, propertyTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "PRP", propertyFkAttrName, ddlType, null, null, null, null, null);
if (!(noJoinForDivisionOid)) {
genCondOuterJoin(fileNo, divisionTargetClassIndex, divisionTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "DIV", divisionFkAttrName, ddlType, null, null, null, null, null);
}
genCondOuterJoin(fileNo, propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "PT", propertyTypeFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, bcTargetClassIndex, bcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BC", bcFkAttrName, ddlType, null, bcReferredColumnList, null, null, null);
if (M03_Config.cr132) {
genCondOuterJoin(fileNo, endSlotTargetClassIndex, endSlotTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BES", endSlotFkAttrName, ddlType, null, "ASSIGNEDPAINTZONEKEY," + M01_Globals_IVK.g_anSlotType, null, null, null);
}

//Change only on View V_CL_GENERICASPECT (Defect 19001 wf)
if ((acmEntityIndex == M01_Globals_IVK.g_classIndexGenericAspect)) {
genCondOuterJoin(fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, null, "ASSIGNEDPAINTZONEKEY," + M01_Globals_IVK.g_anSlotType, assignedPzkIsGen |  assignedPzkIsAggHeadGen, null, false);
} else {
// we know that ASSIGNEDPAINTZONEKEY and SLOTTYPE_ID always go hand-in-hand. we thus use some hard-coding here
genCondOuterJoin(fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, null, "ASSIGNEDPAINTZONEKEY," + M01_Globals_IVK.g_anSlotType, assignedPzkIsGen |  assignedPzkIsAggHeadGen, null, null);
}

String refColumnsNSrX;
refColumnsNSrX = M01_Globals.g_anOid + "," + M01_Globals_IVK.g_anCodeNumber;

genCondOuterJoin(fileNo, s0_01TargetClassIndex, s0_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S01", s0_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_02TargetClassIndex, s0_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S02", s0_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_03TargetClassIndex, s0_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S03", s0_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_04TargetClassIndex, s0_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S04", s0_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_05TargetClassIndex, s0_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S05", s0_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_06TargetClassIndex, s0_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S06", s0_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_07TargetClassIndex, s0_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S07", s0_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_08TargetClassIndex, s0_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S08", s0_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_09TargetClassIndex, s0_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S09", s0_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_10TargetClassIndex, s0_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S10", s0_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);

genCondOuterJoin(fileNo, s1_01TargetClassIndex, s1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S101", s1_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_02TargetClassIndex, s1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S102", s1_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_03TargetClassIndex, s1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S103", s1_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_04TargetClassIndex, s1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S104", s1_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_05TargetClassIndex, s1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S105", s1_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_06TargetClassIndex, s1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S106", s1_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_07TargetClassIndex, s1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S107", s1_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_08TargetClassIndex, s1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S108", s1_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_09TargetClassIndex, s1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S109", s1_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_10TargetClassIndex, s1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S110", s1_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);

genCondOuterJoin(fileNo, ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N101", ns1_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N102", ns1_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N103", ns1_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N104", ns1_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N105", ns1_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N106", ns1_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N107", ns1_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N108", ns1_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N109", ns1_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N110", ns1_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, acTargetClassIndex, acTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "AC", acFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, dcTargetClassIndex, dcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "DC", dcFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "CSB", csBaumusterFkAttrName, ddlType, null, null, null, null, null);

// ### ENDIF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
// ### IF IVK ###
if (condenseData) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".LRTSTATE = " + String.valueOf(M11_LRT.lrtStatusCreated));
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusLocked));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tupVarSrc + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tupVarTgt + "." + M01_Globals.g_anOid + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
// ### ELSE IVK ###
// If clMode = eclPubUpdate Then
// ### ENDIF IVK ###
return;
}

if (hasNlAttributes) {
// ####################################################################################################################
// #    ChangeLog-View for NL-Tab
// ####################################################################################################################

includeAggHeadInJoinPath = (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd);

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, null, false, ddlType, thisOrgIndex, dstPoolIndex, 0, forGen, false, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);

qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, true, "CL", null, null, null);

M22_Class_Utilities.printSectionHeader("ChangeLog-View (" + M12_ChangeLog.genClModeDescription(clMode) + ") for table \"" + qualTargetNlTabName + "\" (ACM-" + (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass ? "Class" : "Relationship") + "\"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dbTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "objectId,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "refObjectId2,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "propertyType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isNational,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "csBaumuster,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr0CodeOid10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "sr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "nsr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "expexp_oid,");
}
// ### ENDIF IVK ###
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
//   If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "validTo,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "codeKind_id,");

if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "baseEndSlotOid,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "assignedPaintZoneKey,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "dpClassNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "isPerformedInMassupdate,");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "lrtOid,");
// ### IF IVK ###
if (!((clMode == M12_ChangeLog.ChangeLogMode.eclSetProd &  condenseData))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operation_Id,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "status_Id,");
// ### ELSE IVK ###
//   Print #fileNo, addTab(1); "operation_Id,"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "operationTimestamp,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + M01_Globals.g_anLanguageId + ",");

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
// ### IF IVK ###
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt) &  ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacNationalEntityMeta) == 0)) {
// ### ELSE IVK ###
//       If isClAttrCat(.columnCategory, clMode = eclLrt) Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n,");

if (M12_ChangeLog.attrTypeMapsToClColType(M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Io,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_In,");
}
}
}

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "cdUserId,");
}
// ### ENDIF IVK ###

boolean useParTab;
useParTab = !(forGen);
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ps_Oid");
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
// entityId / entityType

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityId");
if (hasOwnTable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? tupVarSrcParGen : tupVarSrcPar) + "." + M01_Globals.g_anCid + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M01_Globals.gc_acmEntityTypeKeyClass + "',");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityIdStr + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- entityType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M01_Globals.gc_acmEntityTypeKeyRel + "',");
}
// ahClassId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ahClassId");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhCid + ",");
// ahObjectId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ahObjectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + ",");
// gen
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- gen");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? "1," : "0,"));
// nl
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- nl");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
//dbTableName
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- dbTableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + M04_Utilities.getObjBaseName(qualTargetNlTabName, null) + "',");
// objectId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + ",");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
// refClassId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refClassId1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + relLeftClassIdStr + "',");
// refObjectId1
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refObjectId1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcPar + "." + relLeftFk + ",");
// refClassId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refClassId2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + relRightClassIdStr + "',");
// refObjectId2
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- refObjectId2");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcPar + "." + relRightFk + ",");
useParTab = true;
}
// ### IF IVK ###

// price
foundPrice = M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, null, null, null, aggHeadReferredColumns, null, null, true, null);

if (foundPriceInAggHead &  includeAggHeadInJoinPathForPrice) {
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- price");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".," + priceFkAttrName + ",");
} else {
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  includeAggHeadInJoinPathForPrice;
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15, 3))", priceQualObjName, priceFkAttrName, includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);
}
// ### ENDIF IVK ###
// ### IF IVK ###

// propertyOId
foundPropertyOid = M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, propertyTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", propertyQualObjName, propertyFkAttrName, includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, foundPropertyOidInAggHead, null, null, null, aggHeadReferredColumns, null, null, true, null);

if (foundPropertyOidInAggHead &  includeAggHeadInJoinPathForPropertyOid) {
if (isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + ",");
} else {
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  includeAggHeadInJoinPathForPropertyOid;
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, propertyTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", propertyQualObjName, propertyFkAttrName, includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, foundPropertyOidInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, propertyTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", propertyQualObjName, propertyFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, foundPropertyOidInAggHead, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

// Fixme: get rid of hard-coding here!!
if (foundPropertyOid) {
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
qualObjNamePropertyGen = M04_Utilities.genQualViewNameByClassIndex(M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].classIndex, ddlType, thisOrgIndex, srcPoolIndex, true, true, M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].useMqtToImplementLrt, null, null, null, null, null);
} else {
qualObjNamePropertyGen = M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[(propertyTargetClassIndex > 0 ? propertyTargetClassIndex : (propertyTargetClassIndexAh > 0 ? propertyTargetClassIndexAh : (foundPropertyOidInAggHead ? aggHeadClassIndex : acmEntityIndex)))].classIndex, ddlType, thisOrgIndex, srcPoolIndex, true, null, null, null, null, null, null);
}

if (propertyTargetClassIndex > 0 |  propertyTargetClassIndexAh > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = PRP." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
} else if (foundPropertyOidInAggHead) {
if (!(M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", propertyTypeQualObjName, propertyTypeFkAttrName, includeAggHeadInJoinPath, false, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = " + tupVarAh + "." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- propertyType_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT PRPG.TYPE_ID FROM " + qualObjNamePropertyGen + " PRPG WHERE PRPG.PRP_OID = " + tupVarSrc + "." + M01_Globals.g_anOid + " ORDER BY " + M01_Globals_IVK.g_anValidFrom + " DESC FETCH FIRST 1 ROW ONLY),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", propertyTypeQualObjName, propertyTypeFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

// isNational
if (isNationalizable) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- isNational");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcPar + "." + M01_Globals_IVK.g_anIsNational + ",");
useParTab = true;
} else {
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "isNational", M01_LDM.gc_dbFalse, forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtBoolean, 2, true, tupVarSrcPar, tupVarAh, null, null, aggHeadReferredColumns, null, null);
}

// csBaumuster
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- csBaumuster");
if (M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrcPar, tupVarAh, tupVarSrcParGen, null, csBaumusterFoundInAggHead, null, null, null, aggHeadReferredColumns, null, null, true, true)) {

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(");

// FixMe: for csBaumuster we only navigate along 'direct' relationships (i.e. not related to aggregate head)
// A more generic criterion would be to check with which relationship this class effectively can be related to an Aggregate Head having a 'csBaumuster'-relationship
// E.g.: A DecisionTable may never have an NSR1Validity as Aggregate Head which then is related to some SR0Validity carying 'baumuster'
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "csBaumuster", "CSB", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "CAST(NULL AS VARCHAR(1))", 2, true, tupVarSrcPar, tupVarAh, null, null, aggHeadReferredColumns, null, true);
if (csBaumusterTargetClassIndex > 0 &  csBaumusterTargetClassIndexAh <= 0) {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, false, true, true, tupVarSrcPar, tupVarAh, tupVarSrcParGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, true);
}
csBaumusterTargetClassIndexAh = 0;
M00_FileWriter.printToFile(fileNo, "CAST(NULL AS VARCHAR(8))),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS VARCHAR(8)),");
}

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
// sr0Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "sr0Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrcPar, tupVarAh, null, null, aggHeadReferredColumns, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code01", "S01", s0_01TargetClassIndex, s0_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_01QualObjName, s0_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code02", "S02", s0_02TargetClassIndex, s0_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_02QualObjName, s0_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code03", "S03", s0_03TargetClassIndex, s0_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_03QualObjName, s0_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code04", "S04", s0_04TargetClassIndex, s0_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_04QualObjName, s0_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code05", "S05", s0_05TargetClassIndex, s0_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_05QualObjName, s0_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code06", "S06", s0_06TargetClassIndex, s0_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_06QualObjName, s0_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code07", "S07", s0_07TargetClassIndex, s0_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_07QualObjName, s0_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code08", "S08", s0_08TargetClassIndex, s0_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_08QualObjName, s0_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code09", "S09", s0_09TargetClassIndex, s0_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_09QualObjName, s0_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code10", "S10", s0_10TargetClassIndex, s0_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_10QualObjName, s0_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, null, null, null, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// sr1CodeOids
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_01TargetClassIndex > 0 |  s0_01TargetClassIndexAh > 0 ? "S01." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_02TargetClassIndex > 0 |  s0_02TargetClassIndexAh > 0 ? "S02." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_03TargetClassIndex > 0 |  s0_03TargetClassIndexAh > 0 ? "S03." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_04TargetClassIndex > 0 |  s0_04TargetClassIndexAh > 0 ? "S04." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_05TargetClassIndex > 0 |  s0_05TargetClassIndexAh > 0 ? "S05." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_06TargetClassIndex > 0 |  s0_06TargetClassIndexAh > 0 ? "S06." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_07TargetClassIndex > 0 |  s0_07TargetClassIndexAh > 0 ? "S07." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_08TargetClassIndex > 0 |  s0_08TargetClassIndexAh > 0 ? "S08." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_09TargetClassIndex > 0 |  s0_09TargetClassIndexAh > 0 ? "S09." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (s0_10TargetClassIndex > 0 |  s0_10TargetClassIndexAh > 0 ? "S10." + M01_Globals.g_anOid : "CAST(NULL AS " + M01_Globals.g_dbtOid + ")") + "	" + ",");

// sr1Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "sr1Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrcPar, tupVarAh, null, null, aggHeadReferredColumns, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code01", "S101", s1_01TargetClassIndex, s1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_01QualObjName, s1_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code02", "S102", s1_02TargetClassIndex, s1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_02QualObjName, s1_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code03", "S103", s1_03TargetClassIndex, s1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_03QualObjName, s1_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code04", "S104", s1_04TargetClassIndex, s1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_04QualObjName, s1_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code05", "S105", s1_05TargetClassIndex, s1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_05QualObjName, s1_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code06", "S106", s1_06TargetClassIndex, s1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_06QualObjName, s1_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code07", "S107", s1_07TargetClassIndex, s1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_07QualObjName, s1_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code08", "S108", s1_08TargetClassIndex, s1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_08QualObjName, s1_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code09", "S109", s1_09TargetClassIndex, s1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_09QualObjName, s1_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code10", "S110", s1_10TargetClassIndex, s1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_10QualObjName, s1_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// nsr1Context
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "nsr1Context", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "VARCHAR(159)", 2, true, tupVarSrcPar, tupVarAh, null, null, aggHeadReferredColumns, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code01", "N101", ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_01QualObjName, ns1_01FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code02", "N102", ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_02QualObjName, ns1_02FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code03", "N103", ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_03QualObjName, ns1_03FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code04", "N104", ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_04QualObjName, ns1_04FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code05", "N105", ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_05QualObjName, ns1_05FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code06", "N106", ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_06QualObjName, ns1_06FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code07", "N107", ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_07QualObjName, ns1_07FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code08", "N108", ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_08QualObjName, ns1_08FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code09", "N109", ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_09QualObjName, ns1_09FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code10", "N110", ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_10QualObjName, ns1_10FkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, null, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);

// slotPlausibilityRuleType_ID
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "slotPlausibilityRuleType_ID", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtEnumId, 2, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, aggHeadReferredColumns, null);
// with
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "with", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, aggHeadReferredColumns, null);
// withNot
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "withNot", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, aggHeadReferredColumns, null);
// expression
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, "expression", "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, M01_Globals.g_dbtOid, 2, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, aggHeadReferredColumns, null);
}
// ### ENDIF IVK ###

// validFrom / validTo
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
//   If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- validFrom");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? tupVarSrcParGen : tupVarSrcPar) + "." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- validTo");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? tupVarSrcParGen : tupVarSrcPar) + "." + M01_Globals_IVK.g_anValidTo + ",");
}
// ### IF IVK ###

// baseCodeNumber, baseCodeType and codeKind
if (M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseCodeNumber", "BC", bcTargetClassIndex, bcTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", bcQualObjName, bcFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, true, tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, baseCodeNumberFoundInAh, null, null, bcReferredColumnList, aggHeadReferredColumns, null, null, null, null)) {

if (bcTargetClassIndex > 0 |  bcTargetClassIndexAh > 0) {
tupVarRef = "BC";
M04_Utilities.addStrListElem(bcReferredColumnList, M01_Globals_IVK.g_anCodeNumber);
M04_Utilities.addStrListElem(bcReferredColumnList, "CTLTLV_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, "CTYTYP_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, "CDIDIV_OID");
M04_Utilities.addStrListElem(bcReferredColumnList, M01_Globals_IVK.g_anIsNational);
} else {
if (baseCodeNumberFoundInAh) {
M04_Utilities.addStrListElem(aggHeadReferredColumns, M01_Globals_IVK.g_anCodeNumber);
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CTLTLV_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CTYTYP_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, "CDIDIV_OID");
M04_Utilities.addStrListElem(aggHeadReferredColumns, M01_Globals_IVK.g_anIsNational);
tupVarRef = tupVarAh;
} else {
tupVarRef = tupVarSrcPar;
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- baseCodeType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT T.CODETYPENUMBER FROM " + M01_Globals_IVK.g_qualTabNameCodeType + " T WHERE T." + M01_Globals.g_anOid + " = " + tupVarRef + ".CTYTYP_OID),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- codeKind");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE " + tupVarRef + "." + M01_Globals_IVK.g_anIsNational + " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- baseCodeType");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS CHAR(1)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- codeKind");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtEnumId + "),");
}

if (M03_Config.cr132) {
// baseEndSlotOid
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseEndSlotOid", "BES", endSlotTargetClassIndex, endSlotTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", endSlotQualObjName, endSlotFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

// slotType_Id
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "slotType_Id", "BEG", slotTypeTargetClassIndex, slotTypeTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtEnumId + ")", slotTypeQualObjName, slotTypeFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, slotTypeIdIsAggHead, slotTypeIdIsGen, slotTypeIdIsAggHeadGen, null, aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns, null, null);
includeGenInJoinPath = includeGenInJoinPath |  (slotTypeIdIsGen & ! slotTypeIdIsAggHeadGen & slotTypeTargetClassIndex <= 0 & slotTypeTargetClassIndexAh <= 0);
includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath |  (slotTypeIdIsAggHeadGen & ! slotTypeIdIsGen & slotTypeTargetClassIndex <= 0 & slotTypeTargetClassIndexAh <= 0);
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  slotTypeIdIsAggHead;

// allowedCountries
if (allowedCountriesRelIndex > 0) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[allowedCountriesRelIndex].sectionIndex, M01_ACM_IVK.udfnAllowedCountry2Str0 + qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- aclacl_oid");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + "," + tupVarSrc + "." + M01_Globals.g_anInLrt + ",1024)),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + ",1024)),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "aclacl_oid", "AC", acTargetClassIndex, acTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", acQualObjName, acFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, null, null, acReferredColumnList, aggHeadReferredColumns, null, null, null, null);
}

// disallowedCountries
if (disAllowedCountriesRelIndex > 0) {
qualCountryListFuncName = M04_Utilities.genQualFuncName(M23_Relationship.g_relationships.descriptors[disAllowedCountriesRelIndex].sectionIndex, M01_ACM_IVK.udfnDisallowedCountry2Str0 + qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- dcldcl_oid");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + "," + tupVarSrc + "." + M01_Globals.g_anInLrt + ",1024)),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT OID FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE IDLIST = " + qualCountryListFuncName + "(" + tupVarSrc + "." + M01_Globals.g_anOid + ",1024)),");
}
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "dcldcl_oid", "DC", dcTargetClassIndex, dcTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", dcQualObjName, dcFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, null, null, dcReferredColumnList, aggHeadReferredColumns, null, null, null, null);
}

// assignedPaintZoneKey
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, M01_ACM_IVK.conAssignedPaintZoneKey, "BEG", assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, "CAST(NULL AS VARCHAR(15))", assignedPzkQualObjName, assignedPzkFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, true, true, false, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, assignedPzkIsAggHead, assignedPzkIsGen, assignedPzkIsAggHeadGen, null, aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns, null, null);
includeGenInJoinPath = includeGenInJoinPath |  (assignedPzkIsGen & ! assignedPzkIsAggHeadGen & assignedPzkTargetClassIndex <= 0 & assignedPzkIsAggHeadGen <= 0);
includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath |  (assignedPzkIsAggHeadGen &  assignedPzkIsGen & assignedPzkTargetClassIndex <= 0 & assignedPzkIsAggHeadGen <= 0);
includeAggHeadInJoinPath = includeAggHeadInJoinPath |  assignedPzkIsAggHead;

// divisionOid
if (bcTargetClassIndex > 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "BC.CDIDIV_OID,");
} else {
M11_LRT.genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "divisionOid", "DIV", divisionTargetClassIndex, divisionTargetClassIndexAh, "CAST(NULL AS " + M01_Globals.g_dbtOid + ")", divisionQualObjName, divisionFkAttrName, includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, false, true, false, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, tupVarSrcGen, null, null, null, null, null, aggHeadReferredColumns, null, null, null, null);
}

// dpClassNumber
M11_LRT.genLrtLogColDdlAh(fileNo, attrMapping, attrMappingAh, M01_ACM_IVK.conDpClassNumber, "NULL", forGen, includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, null, "SMALLINT", 2, true, (forGen ? tupVarSrcParGen : tupVarSrcPar), tupVarAh, null, null, null, aggHeadReferredColumns, null);

// isPerformedInMassupdate
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- isPerformedInMassupdate");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
// ### ENDIF IVK ###

// lrtOid
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- lrtOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anInLrt + ",");
// operation_Id
// ### IF IVK ###
if (!((clMode == M12_ChangeLog.ChangeLogMode.eclSetProd &  condenseData))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- operation_Id");
}
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".LRTSTATE,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST((CASE WHEN " + tupVarSrc + "." + M01_ACM_IVK.conIsDeleted + " = 1 THEN " + String.valueOf(M11_LRT.lrtStatusDeleted) + " WHEN " + tupVarTgt + "." + M01_Globals.g_anOid + " IS NULL THEN " + String.valueOf(M11_LRT.lrtStatusCreated) + " ELSE " + String.valueOf(M11_LRT.lrtStatusUpdated) + " END) AS " + M01_Globals.g_dbtEnumId + "),");
}
// status_Id
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- status_Id");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anStatus + ",");
// ### ELSE IVK ###
//   Print #fileNo, addTab(2); "-- operation_Id"
//   If clMode = eclLrt Then
//     Print #fileNo, addTab(2); tupVarSrc ; ".LRTSTATE,"
//   Else
//     Print #fileNo, addTab(2); "CAST((CASE WHEN " ; tupVarTgt ; "." ; g_anOid ; _
//                               " IS NULL THEN " ; CStr(lrtStatusCreated) ; " ELSE " ; CStr(lrtStatusUpdated) ; " END) AS "; g_dbtEnumId; "),"
//   End If
// ### ENDIF IVK ###

// operationTimestamp
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- operationTimestamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? tupVarSrcParGen : tupVarSrcPar) + "." + M01_Globals.g_anLastUpdateTimestamp + ",");

// language_Id
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- " + M01_Globals.g_anLanguageId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anLanguageId + ",");

for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
attrTypeId = M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType;

// valueType
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M12_ChangeLog.getClColTypeByAttrType(attrTypeId)) + ",");

if (attrTypeId == M01_Common.typeId.etBigInt |  attrTypeId == M01_Common.typeId.etDecimal | attrTypeId == M01_Common.typeId.etDouble | attrTypeId == M01_Common.typeId.etFloat | attrTypeId == M01_Common.typeId.etInteger | attrTypeId == M01_Common.typeId.etSmallint | attrTypeId == M01_Common.typeId.etTime | attrTypeId == M01_Common.typeId.etTimestamp | attrTypeId == M01_Common.typeId.etDate | attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS CHAR(30))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS CHAR(30))),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(4000)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(4000)),");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + tabColumns.descriptors[i].columnName + ",");
}
}
}
// ### IF IVK ###

if (clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
// cdUserId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "COALESCE(" + (forGen ? tupVarAh : tupVarSrcPar) + "." + M01_Globals.g_anUpdateUser + "," + (forGen ? tupVarAh : tupVarSrcPar) + "." + M01_Globals.g_anCreateUser + ",'-unk-'),");
}

// ps_Oid
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- ps_Oid");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + (forGen ? tupVarSrcParGen : tupVarSrcPar) + "." + M01_Globals_IVK.g_anPsOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + ")");
}
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");

String qualViewOrTabNamePar;
String qualViewOrTabNameParGen;
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
qualViewOrTabNamePar = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, false, true, useMqtToImplementLrtForEntity, null, null, null, null, null);
if (forGen) {
qualViewOrTabNameParGen = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, true, true, useMqtToImplementLrtForEntity, null, null, null, null, null);
}
} else {
qualViewOrTabNamePar = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, false, null, null, null, null, null, null);
if (forGen) {
qualViewOrTabNameParGen = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, true, null, null, null, null, null, null);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualSourceNlTabName + " " + tupVarSrc);

if (forGen) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewOrTabNameParGen + " " + tupVarSrcParGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null) + " = " + tupVarSrcParGen + "." + M01_Globals.g_anOid);

if (useParTab) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewOrTabNamePar + " " + tupVarSrcPar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcParGen + "." + M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null) + " = " + tupVarSrcPar + "." + M01_Globals.g_anOid);
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewOrTabNamePar + " " + tupVarSrcPar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null) + " = " + tupVarSrcPar + "." + M01_Globals.g_anOid);
}

if (M03_Config.referToAggHeadInChangeLog &  checkAggHeadForAttrs & includeAggHeadInJoinPath) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualAggHeadTabName + " " + tupVarAh);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + " = " + tupVarAh + "." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetNlTabName + " " + tupVarTgt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + " = " + tupVarTgt + "." + M01_Globals.g_anOid);

// ### IF IVK ###
genCondOuterJoin(fileNo, priceTargetClassIndex, priceTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, (useParTab ? tupVarSrcPar : tupVarSrc), tupVarAh, "PRI", priceFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, propertyTargetClassIndex, propertyTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, (useParTab ? tupVarSrcPar : tupVarSrc), tupVarAh, "PRP", propertyFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "PT", propertyTypeFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, divisionTargetClassIndex, divisionTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "DIV", divisionFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, bcTargetClassIndex, bcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BC", bcFkAttrName, ddlType, null, bcReferredColumnList, null, null, null);
if (M03_Config.cr132) {
genCondOuterJoin(fileNo, endSlotTargetClassIndex, endSlotTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BES", endSlotFkAttrName, ddlType, null, "ASSIGNEDPAINTZONEKEY," + M01_Globals_IVK.g_anSlotType, null, null, null);
}

genCondOuterJoin(fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, null, "ASSIGNEDPAINTZONEKEY," + M01_Globals_IVK.g_anSlotType, assignedPzkIsGen, null, null);

genCondOuterJoin(fileNo, s0_01TargetClassIndex, s0_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S01", s0_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_02TargetClassIndex, s0_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S02", s0_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_03TargetClassIndex, s0_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S03", s0_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_04TargetClassIndex, s0_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S04", s0_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_05TargetClassIndex, s0_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S05", s0_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_06TargetClassIndex, s0_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S06", s0_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_07TargetClassIndex, s0_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S07", s0_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_08TargetClassIndex, s0_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S08", s0_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_09TargetClassIndex, s0_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S09", s0_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s0_10TargetClassIndex, s0_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S10", s0_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);

genCondOuterJoin(fileNo, s1_01TargetClassIndex, s1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S101", s1_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_02TargetClassIndex, s1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S102", s1_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_03TargetClassIndex, s1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S103", s1_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_04TargetClassIndex, s1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S104", s1_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_05TargetClassIndex, s1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S105", s1_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_06TargetClassIndex, s1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S106", s1_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_07TargetClassIndex, s1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S107", s1_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_08TargetClassIndex, s1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S108", s1_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_09TargetClassIndex, s1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S109", s1_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, s1_10TargetClassIndex, s1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S110", s1_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);

genCondOuterJoin(fileNo, ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N101", ns1_01FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N102", ns1_02FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N103", ns1_03FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N104", ns1_04FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N105", ns1_05FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N106", ns1_06FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N107", ns1_07FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N108", ns1_08FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N109", ns1_09FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N110", ns1_10FkAttrName, ddlType, null, refColumnsNSrX, null, null, null);
genCondOuterJoin(fileNo, acTargetClassIndex, acTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "AC", acFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, dcTargetClassIndex, dcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "DC", dcFkAttrName, ddlType, null, null, null, null, null);
genCondOuterJoin(fileNo, csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "CSB", csBaumusterFkAttrName, ddlType, null, null, null, null, null);
// ### ENDIF IVK ###

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusLocked));
// ### IF IVK ###
if (!(condenseData)) {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tupVarSrc + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tupVarTgt + "." + M01_Globals.g_anOid + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}
}


// ### IF IVK ###
public static void genMaintainChangeLogStatusDdl( int thisOrgIndex,  int thisPoolIndex, int fileNo, String timeStamp, Integer offsetW, Integer ddlTypeW, Boolean withLrtContextW) {
int offset; 
if (offsetW == null) {
offset = 1;
} else {
offset = offsetW;
}

Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean withLrtContext; 
if (withLrtContextW == null) {
withLrtContext = true;
} else {
withLrtContext = withLrtContextW;
}

if (M03_Config.generateFwkTest) {
return;
}

String qualTabNameChangelogStatus;
qualTabNameChangelogStatus = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexChangeLogStatus, ddlType, thisOrgIndex, thisPoolIndex, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "keep track of last update timestamp of changelog", offset, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + qualTabNameChangelogStatus + " CLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "CLS.LASTCOMMITTIME = (CASE WHEN CLS.LASTCOMMITTIME > " + timeStamp + " THEN CLS.LASTCOMMITTIME ELSE " + timeStamp + " END)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "CLS.WITHLRTCONTEXT = " + (withLrtContext ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + M01_Globals_IVK.gc_tempTabNameChangeLogStatus + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "COALESCE(CLS." + M01_Globals_IVK.g_anPsOid + ", -1) = COALESCE(S.psOid, -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "CLS.DIVISIONOID = S.divisionOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + ";");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + qualTabNameChangelogStatus);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "DIVISIONOID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "LASTCOMMITTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "WITHLRTCONTEXT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "SELECT DISTINCT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "psOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + timeStamp + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + (withLrtContext ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + M01_Globals_IVK.gc_tempTabNameChangeLogStatus + " S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + qualTabNameChangelogStatus + " CLS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "COALESCE(S.psOid, -1) = COALESCE(CLS." + M01_Globals_IVK.g_anPsOid + ", -1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "S.divisionOid = CLS.DIVISIONOID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 3) + "CLS.WITHLRTCONTEXT = " + (withLrtContext ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(offset + 0) + ";");
}
// ### ENDIF IVK ###

public static void genChangeLogViewDdlHeader2(int acmEntityIndex, Integer acmEntityType, String qualTargetTabName,  int thisOrgIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Integer clModeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
String entityIdStr;
boolean isGenForming;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
boolean isLogChange;
boolean M03_Config.useMqtToImplementLrt;
// ### IF IVK ###
boolean hasNoIdentity;
boolean condenseData;
// ### ENDIF IVK ###

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
isLogChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isLogChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
isGenForming = false;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
// ### IF IVK ###
hasNoIdentity = false;
condenseData = false;
// ### ENDIF IVK ###
} else {
return;
}

// ####################################################################################################################
// #    ChangeLog-View for entity
// ####################################################################################################################

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

String viewNameSuffix;
// ### IF IVK ###
viewNameSuffix = "REDUCED";
// ### ELSE IVK ###
// viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
// ### ENDIF IVK ###

String qualViewName;
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, null, null, null, "CL", viewNameSuffix, null, null);
M22_Class_Utilities.printSectionHeader("Reduced ChangeLog-View - non string columns - (" + M12_ChangeLog.genClModeDescription(clMode) + ") for table \"" + qualTargetTabName + "\" (ACM-" + (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass ? "Class" : "Relationship") + "\"" + M20_Section.g_sections.descriptors[sectionIndex].sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "objectId,");



// make sure that 'LastUpdateTimeStamp' is handled as attribute
// guess we do not need this any more
int domainIndexModTs;
if (isLogChange) {
domainIndexModTs = M01_Globals.g_domainIndexModTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals.g_anLastUpdateTimestamp, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conLastUpdateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexModTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// make sure that 'validFrom' and 'validTo' are handled as attribute
int domainIndexValidTs;
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
domainIndexValidTs = M01_Globals.g_domainIndexValTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidFrom, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidFrom, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidTo, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidTo, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
Integer attrTypeId;
String newValueString;
newValueString = "";
attrTypeId = M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType;
if (attrTypeId != M01_Common.typeId.etVarchar) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n,");
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_To,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Tn";
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dto,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dtn";
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Io,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_In";
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIo,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIn";
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Do,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Dn";
}

if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bo,");
newValueString = tabColumns.descriptors[i].columnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_Bn";
}
if (!(tabColumns.descriptors[i].columnName.compareTo("LASTUPDATETIMESTAMP") == 0) &  !(newValueString.compareTo("") == 0)) {
newValueString = newValueString + ",";
}
if (!(newValueString.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + newValueString);
}
}
}
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
}


public static void genChangeLogViewDdl2(int acmEntityIndex, Integer acmEntityType, String qualSourceTabName, String qualSourceGenTabName, String qualSourceNlTabName, String qualTargetTabName, String qualTargetNlTabName, String qualAggHeadTabName,  int thisOrgIndex, int srcPoolIndex, int dstPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Integer clModeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}


if (clMode != M12_ChangeLog.ChangeLogMode.eclSetProd) {
return;
}

if (acmEntityIndex != M01_Globals_IVK.g_classIndexGenericAspect) {
return;
}



//0x3.V_CL_GENERICASPECT_REDUCED erzeugen: ohne jegliche String-Attribute

int sectionIndex;
String entityName;
String entityTypeDescr;
String entityShortName;
boolean hasOwnTable;
String entityIdStr;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefs;
boolean isGenForming;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMapping;
String relLeftClassIdStr;
String relLeftFk;
String relRightClassIdStr;
String relRightFk;
boolean hasNlAttributes;
boolean isLogChange;
boolean checkAggHeadForAttrs;
int aggHeadClassIndex;
boolean isAggHead;
boolean isAbstract;
M24_Attribute_Utilities.AttributeMappingForCl[] attrMappingAh;
M23_Relationship_Utilities.RelationshipDescriptorRefs relRefsAh;
boolean includeAggHeadInJoinPath;
boolean includeGenInJoinPath;
boolean includeAggHeadGenInJoinPath;
String aggHeadReferredColumns;
String aggHeadGenReferredColumns;
String genReferredColumns;
boolean aggHeadSupportMqt;
boolean useMqtToImplementLrtForEntity;
boolean ignoreForChangelog;
// ### IF IVK ###
boolean isPsTagged;
boolean hasNoIdentity;
int allowedCountriesRelIndex;
int disAllowedCountriesRelIndex;
int allowedCountriesListRelIndex;
int disAllowedCountriesListRelIndex;
boolean condenseData;
boolean isNationalizable;
// ### ENDIF IVK ###

includeAggHeadInJoinPath = false;
includeGenInJoinPath = false;
includeAggHeadGenInJoinPath = false;
isAggHead = false;
isAbstract = false;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isAbstract = M22_Class.g_classes.descriptors[acmEntityIndex].isAbstract;
relRefs = M22_Class.g_classes.descriptors[acmEntityIndex].relRefsRecursive;
isGenForming = M22_Class.g_classes.descriptors[acmEntityIndex].isGenForming;
isLogChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
entityIdStr = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
hasNlAttributes = (forGen ? M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInGenInclSubClasses : M22_Class.g_classes.descriptors[acmEntityIndex].hasNlAttrsInNonGenInclSubClasses);
attrMapping = M22_Class.g_classes.descriptors[acmEntityIndex].clMapAttrsInclSubclasses;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = M22_Class.g_classes.descriptors[acmEntityIndex].hasNoIdentity;
isNationalizable = M22_Class.g_classes.descriptors[acmEntityIndex].isNationalizable;
aggHeadClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
ignoreForChangelog = M22_Class.g_classes.descriptors[acmEntityIndex].ignoreForChangelog;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
allowedCountriesRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].allowedCountriesRelIndex;
disAllowedCountriesRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].disAllowedCountriesRelIndex;
allowedCountriesListRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].allowedCountriesListRelIndex;
disAllowedCountriesListRelIndex = M22_Class.g_classes.descriptors[acmEntityIndex].disAllowedCountriesListRelIndex;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;

checkAggHeadForAttrs = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  ((M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex != M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex) |  forGen);
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isLogChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;

sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
hasOwnTable = true;
entityIdStr = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
relRefs.numRefs = 0;
isGenForming = false;
hasNlAttributes = M23_Relationship.g_relationships.descriptors[acmEntityIndex].nlAttrRefs.numDescriptors > 0;

int reuseRelIndex;
reuseRelIndex = (M03_Config.reuseRelationships &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex > 0 ? M23_Relationship.g_relationships.descriptors[acmEntityIndex].reusedRelIndex : acmEntityIndex);
relLeftClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].classIdStr;
relLeftFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].leftEntityIndex].shortName, null, null, null, null, null);
relRightClassIdStr = M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].classIdStr;
relRightFk = M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, M22_Class.g_classes.descriptors[M23_Relationship.g_relationships.descriptors[reuseRelIndex].rightEntityIndex].shortName, null, null, null, null, null);

aggHeadClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
checkAggHeadForAttrs = (M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex > 0);
ignoreForChangelog = M23_Relationship.g_relationships.descriptors[acmEntityIndex].ignoreForChangelog;
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
hasNoIdentity = false;
isNationalizable = false;
allowedCountriesRelIndex = -1;
disAllowedCountriesRelIndex = -1;
allowedCountriesListRelIndex = -1;
disAllowedCountriesListRelIndex = -1;
useMqtToImplementLrtForEntity = M03_Config.useMqtToImplementLrt &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = false;
// ### ENDIF IVK ###
} else {
return;
}

aggHeadSupportMqt = false;
if (checkAggHeadForAttrs) {
attrMappingAh = M22_Class.g_classes.descriptors[aggHeadClassIndex].clMapAttrsInclSubclasses;
relRefsAh = M22_Class.g_classes.descriptors[aggHeadClassIndex].relRefsRecursive;
aggHeadSupportMqt = M03_Config.useMqtToImplementLrt &  M22_Class.g_classes.descriptors[aggHeadClassIndex].useMqtToImplementLrt;
}

if (ignoreForChangelog) {
return;
}

String tupVarSrc;
String tupVarSrcGen;
String tupVarSrcPar;
String tupVarSrcParGen;
String tupVarTgt;
String tupVarAh;

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
tupVarSrc = "PRIV";
tupVarSrcGen = "GEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = "PUB";
tupVarAh = "AH";
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
tupVarSrc = "OBJ";
tupVarSrcGen = "OBJGEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = " - no used -";
tupVarAh = "AH";
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
tupVarSrc = "OBJ";
tupVarSrcGen = "OBJGEN";
tupVarSrcPar = "PAR";
tupVarSrcParGen = "PARGEN";
tupVarTgt = " - no used -";
tupVarAh = "AH";
// ### ENDIF IVK ###
} else {
tupVarSrc = "SRC";
tupVarSrcGen = "SRCGEN";
tupVarSrcPar = "SRCPAR";
tupVarSrcParGen = "SRCPARGEN";
tupVarTgt = "TGT";
tupVarAh = "AH";
}

boolean parTabIsAhTab;
parTabIsAhTab = (aggHeadClassIndex == acmEntityIndex) &  (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass);

// ####################################################################################################################
// #    Reduced ChangeLog-View only for GenericAspect
// ####################################################################################################################

//separate some code to avoid 'Procedure too large' - errors
M12_ChangeLog.genChangeLogViewDdlHeader2(acmEntityIndex, acmEntityType, qualTargetTabName, thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, clMode);

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);

M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);

// make sure that 'LastUpdateTimeStamp' is handled as attribute
// guess we do not need this any more
int domainIndexModTs;
if (isLogChange) {
domainIndexModTs = M01_Globals.g_domainIndexModTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals.g_anLastUpdateTimestamp, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conLastUpdateTimestamp, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexModTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}

// make sure that 'validFrom' and 'validTo' are handled as attribute
int domainIndexValidTs;
// ### IF IVK ###
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
domainIndexValidTs = M01_Globals.g_domainIndexValTimestamp;
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidFrom, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidFrom, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
M24_Attribute_Utilities.findColumnToUse(tabColumns, M01_Globals_IVK.g_anValidTo, M01_ACM.clnAcmEntity, acmEntityType, M01_ACM.conValidTo, M24_Attribute_Utilities.AttrValueType.eavtDomain, domainIndexValidTs, false, M01_Common.AttrCategory.eacRegular, null, null, null, null, null);
}


// objectId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + ",");

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
int i;
for (int i = 1; i <= tabColumns.numDescriptors; i++) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[i].columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt)) {
Integer attrTypeId;
attrTypeId = M25_Domain.g_domains.descriptors[tabColumns.descriptors[i].dbDomainIndex].dataType;

// valueType
if (attrTypeId == M01_Common.typeId.etBigInt |  attrTypeId == M01_Common.typeId.etDecimal | attrTypeId == M01_Common.typeId.etDouble | attrTypeId == M01_Common.typeId.etFloat | attrTypeId == M01_Common.typeId.etInteger | attrTypeId == M01_Common.typeId.etSmallint | attrTypeId == M01_Common.typeId.etDate | attrTypeId == M01_Common.typeId.etTimestamp | attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + String.valueOf(M12_ChangeLog.getClColTypeByAttrType(attrTypeId)) + ",");
}

// ### IF IVK ###
if ((tabColumns.descriptors[i].columnCategory &  M01_Common.AttrCategory.eacExpression)) {
String oldVal;
String newVal;
M24_Attribute_Utilities.AttributeListTransformation transformationExpr;
M24_Attribute_Utilities.initAttributeTransformation(transformationExpr, 0, null, null, null, tupVarTgt + ".", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformationExpr, thisOrgIndex, dstPoolIndex, tupVarTgt, null, null);
newVal = M04_Utilities.transformAttrName(tabColumns.descriptors[i].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformationExpr, ddlType, null, null, null, null, tabColumns.descriptors[i].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueExpression, null, null, null, tabColumns.descriptors[i].columnCategory);
transformationExpr.attributePrefix = tupVarSrc + ".";
M24_Attribute_Utilities.setAttributeTransformationContext(transformationExpr, thisOrgIndex, srcPoolIndex, tupVarSrc, null, clMode == M12_ChangeLog.ChangeLogMode.eclLrt);
oldVal = M04_Utilities.transformAttrName(tabColumns.descriptors[i].columnName, M24_Attribute_Utilities.AttrValueType.eavtDomain, tabColumns.descriptors[i].dbDomainIndex, transformationExpr, ddlType, null, null, null, null, tabColumns.descriptors[i].acmAttributeIndex, M01_Common.DdlOutputMode.edomValueExpression, null, null, null, tabColumns.descriptors[i].columnCategory);

if (attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + newVal + " AS VARCHAR(50))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + oldVal + " AS VARCHAR(50))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + newVal + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + oldVal + ",");
}
} else {
// ### ELSE IVK ###
// ### INDENT IVK ### -2
// ### ENDIF IVK ###
// oldValueString / newValueString
if (attrTypeId == M01_Common.typeId.etBigInt |  attrTypeId == M01_Common.typeId.etDecimal | attrTypeId == M01_Common.typeId.etDouble | attrTypeId == M01_Common.typeId.etFloat | attrTypeId == M01_Common.typeId.etInteger | attrTypeId == M01_Common.typeId.etSmallint | attrTypeId == M01_Common.typeId.etDate | attrTypeId == M01_Common.typeId.etTimestamp | attrTypeId == M01_Common.typeId.etBoolean) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(50))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "RTRIM(CAST(" + tupVarSrc + "." + tabColumns.descriptors[i].columnName + " AS VARCHAR(50))),");
}
String newValueColumn;
newValueColumn = "";

// oldValueTimestamp / newValueTimestamp
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
}

// oldValueDate / newValueDate
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
}

// oldValueInteger / newValueInteger
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
}

// oldValueBigInt / newValueBigInt
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
}

// oldValueDecimal / newValueDecimal
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + tupVarTgt + "." + tabColumns.descriptors[i].columnName + " AS DECIMAL(31,10)),");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
newValueColumn = "CAST(" + newValueColumn + " AS DECIMAL(31,10))";
}

// oldValueBoolean / newValueBoolean
if (M12_ChangeLog.attrTypeMapsToClColType(attrTypeId, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarTgt + "." + tabColumns.descriptors[i].columnName + ",");
newValueColumn = tupVarSrc + "." + tabColumns.descriptors[i].columnName;
}

if (!(tabColumns.descriptors[i].columnName.compareTo("LASTUPDATETIMESTAMP") == 0) &  !(newValueColumn.compareTo("") == 0)) {
newValueColumn = newValueColumn + ",";
}
if (!(newValueColumn.compareTo("") == 0)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + newValueColumn);
}

// ### IF IVK ###
}
// ### ELSE IVK ###
// ### INDENT IVK ### 0
// ### ENDIF IVK ###
}
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualSourceTabName + " " + tupVarSrc);

if (M03_Config.referToAggHeadInChangeLog &  checkAggHeadForAttrs & includeAggHeadInJoinPath) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");

M22_Class.genTabSubQueryByEntityIndex(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt), forGen, "AH", aggHeadReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + " = " + tupVarAh + "." + M01_Globals.g_anOid);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarAh + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarAh + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarAh + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarAh + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarAh; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarAh + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarAh + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarAh + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}

if (includeAggHeadGenInJoinPath) {
String aggHeadFkAttrName;
aggHeadFkAttrName = M04_Utilities.genSurrogateKeyName(ddlType, M22_Class.g_classes.descriptors[aggHeadClassIndex].shortName, null, null, null, null);
aggHeadGenReferredColumns = aggHeadGenReferredColumns + (aggHeadGenReferredColumns.compareTo("") == 0 ? "" : ",") + aggHeadFkAttrName;

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");

M22_Class.genTabSubQueryByEntityIndex(aggHeadClassIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt), true, tupVarSrcGen, aggHeadGenReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anAhOid + "" + " = " + tupVarSrcGen + "." + aggHeadFkAttrName);

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarSrcGen + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcGen + ".ROWNUM = 1");
} else if (includeGenInJoinPath) {
genReferredColumns = genReferredColumns + (genReferredColumns.compareTo("") == 0 ? "" : ",") + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "LEFT OUTER JOIN");
M22_Class.genTabSubQueryByEntityIndex(acmEntityIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, clMode == M12_ChangeLog.ChangeLogMode.eclLrt, true, tupVarSrcGen, genReferredColumns, 2, null, "", null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + " = " + tupVarSrcGen + "." + M04_Utilities.genSurrogateKeyName(ddlType, entityShortName, null, null, null, null));

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals_IVK.g_anIsDeleted + " = 0) AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " IS NULL OR " + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " <> PRIV." + M01_Globals.g_anInLrt + "))");
// ### ELSE IVK ###
//     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "((" + tupVarSrcGen + "." + M01_Globals.g_anIsLrtPrivate + " = 1) AND (" + tupVarSrcGen + ".LRTSTATE <> " + String.valueOf(M11_LRT.lrtStatusDeleted) + ") AND (" + tupVarSrcGen + "." + M01_Globals.g_anInLrt + " = PRIV." + M01_Globals.g_anInLrt + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrcGen + ".ROWNUM = 1");
}

if (forGen) {
String qualViewNameNonGen;
qualViewNameNonGen = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, false, true, useMqtToImplementLrtForEntity, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualViewNameNonGen + " " + tupVarSrcPar);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M04_Utilities.genAttrName(M01_ACM.conOid, ddlType, entityShortName, null, null, null, null, null) + " = " + tupVarSrcPar + "." + M01_Globals.g_anOid);
}

// ### IF IVK ###
if (clMode != M12_ChangeLog.ChangeLogMode.eclPubUpdate &  clMode != M12_ChangeLog.ChangeLogMode.eclPubMassUpdate & !condenseData) {
// ### ELSE IVK ###
// If clMode <> eclPubUpdate Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTargetTabName + " " + tupVarTgt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + tupVarSrc + "." + M01_Globals.g_anOid + " = " + tupVarTgt + "." + M01_Globals.g_anOid);
}

// ### IF IVK ###


// ### ENDIF IVK ##

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ### IF IVK ###
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
// ### ELSE IVK ###
// If clMode = eclPubUpdate Then
// ### ENDIF IVK ###
return;
}


}

private static void printCteChangeLogStatements(int fileNo, Integer acmEntityType, String qualViewName, String tabName, String qualSeqNameOid, boolean isGenForming, boolean forGen, boolean forNl, boolean hasNoIdentity, Integer clMode, String cdUserId_in, boolean isPsTagged, boolean splitCaseColumns, String[] stringsPerType, String valuesStringForCTE, String caseUpdateStringForCTE) {

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO " + pc_tempTabNameChangeLogCte);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WITH cte_bas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "AS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + caseUpdateStringForCTE);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewName + " V");
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals_IVK.gc_tempTabNameSpAffectedEntities + " E");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.oid = V.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "E.opId = opId_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.operation_Id = opId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.LRTOID = lrtOid_in" + vbCrLf);
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate |  clMode == M12_ChangeLog.ChangeLogMode.eclSetProd) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals.g_anStatus + " = " + String.valueOf(M86_SetProductive.statusReadyToBeSetProductive));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.divisionOid = v_divisionOid");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.objectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lat.dbColumnName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lat.switch");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "cte_bas AS bas,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "LATERAL(VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + valuesStringForCTE);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AS lat (dbColumnName, switch)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lat.switch = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "now use cte for inserting into change log", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_tempTabNameChangeLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "dbTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "dbColumnName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "objectId,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refObjectId2,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "propertyType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isNational,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "csBaumuster,");
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr0CodeOid10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "sr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "nsr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "expexp_oid,");
}
if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validFrom,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "validTo,");
}
// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "codeKind_Id,");
if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "baseEndSlotOid,");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "assignedPaintZoneKey,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "dpClassNumber,");
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "valueType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueString,");
if (!(splitCaseColumns)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueString,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueDate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueDate,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueInteger,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueInteger,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueBigInt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueBigInt,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueDecimal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueDecimal,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueBoolean,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueBoolean,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueTimestamp,");
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid,");
}
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "languageId,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "isPerformedInMassupdate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "operation_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "opTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ps_Oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "versionId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");


M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
// OID
//If logRecordOid <> "" Then
// Print #fileNo, addTab(1); logRecordOid; ","
//Else
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEXTVAL FOR " + qualSeqNameOid + ",");
//End If
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.entityId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.entityType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.ahClassId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.ahObjectId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.gen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nl,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.dbTableName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.dbColumnName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.objectId,");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.refClassId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.refObjectId1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.refClassId2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.refObjectId2,");
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.price,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.propertyOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.propertyType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(V.isNational, " + M01_LDM.gc_dbFalse + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.csBaumuster,");

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr0CodeOid10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.sr1Code10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Context,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code5,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code7,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code8,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.nsr1Code10,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.slotPlausibilityRuleType_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.witexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.winexp_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.expexp_oid,");
}

if (isGenForming &  (forGen |  hasNoIdentity)) {
// ### ELSE IVK ###
// If isGenForming And forGen Then
// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals_IVK.g_anValidFrom + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals_IVK.g_anValidTo + ",");
}

// ### IF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.baseCodeNumber,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.baseCodeType,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.codeKind_id,");
if (M03_Config.cr132) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.baseEndSlotOid,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.slotType_Id,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.aclacl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.dcldcl_oid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.assignedPaintZoneKey,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.divisionOid,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.dpClassNumber,");
// ### ENDIF IVK ###

// finalize the built strings
String endSuffix;
endSuffix = "      END AS wert";
stringsPerType[(1)] = stringsPerType[1] + "    ELSE NULL" + vbCrLf + endSuffix + "_t,";
stringsPerType[(2)] = stringsPerType[2] + "    ELSE CAST(NULL AS VARCHAR(4000))" + vbCrLf + endSuffix + "_o,";
stringsPerType[(3)] = stringsPerType[3] + "    ELSE CAST(NULL AS VARCHAR(4000))" + vbCrLf + endSuffix + "_n";
stringsPerType[(4)] = stringsPerType[4] + "    ELSE CAST(NULL AS DATE)" + vbCrLf + endSuffix + "_Dto,";
stringsPerType[(5)] = stringsPerType[5] + "    ELSE CAST(NULL AS DATE)" + vbCrLf + endSuffix + "_Dtn,";
stringsPerType[(6)] = stringsPerType[6] + "    ELSE CAST(NULL AS INTEGER)" + vbCrLf + endSuffix + "_Io,";
stringsPerType[(7)] = stringsPerType[7] + "    ELSE CAST(NULL AS INTEGER)" + vbCrLf + endSuffix + "_In,";
stringsPerType[(8)] = stringsPerType[8] + "    ELSE CAST(NULL AS BIGINT)" + vbCrLf + endSuffix + "_BIo,";
stringsPerType[(9)] = stringsPerType[9] + "    ELSE CAST(NULL AS BIGINT)" + vbCrLf + endSuffix + "_BIn,";
stringsPerType[(10)] = stringsPerType[10] + "    ELSE CAST(NULL AS DECIMAL)" + vbCrLf + endSuffix + "_Do,";
stringsPerType[(11)] = stringsPerType[11] + "    ELSE CAST(NULL AS DECIMAL)" + vbCrLf + endSuffix + "_Dn,";
stringsPerType[(12)] = stringsPerType[12] + "    ELSE CAST(NULL AS SMALLINT)" + vbCrLf + endSuffix + "_Bo,";
stringsPerType[(13)] = stringsPerType[13] + "    ELSE CAST(NULL AS SMALLINT)" + vbCrLf + endSuffix + "_Bn,";
stringsPerType[(14)] = stringsPerType[14] + "    ELSE CAST(NULL AS TIMESTAMP)" + vbCrLf + endSuffix + "_To,";
stringsPerType[(15)] = stringsPerType[15] + "    ELSE CAST(NULL AS TIMESTAMP)" + vbCrLf + endSuffix + "_Tn";
if (!(splitCaseColumns)) {
stringsPerType[(3)] = stringsPerType[3] + ",";
stringsPerType[(15)] = stringsPerType[15] + ",";
}
int i;
int limit;
if (splitCaseColumns) {
limit = 2;
} else {
limit = 15;
}

for (int i = 1; i <= limit; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + stringsPerType[i]);
}

if (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "lrtOid_in,");
}
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.LANGUAGE_ID,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.isPerformedInMassupdate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.operation_Id,");

// opTimestamp
if (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CURRENT TIMESTAMP,");
// ### IF IVK ###
} else if (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_currentTimestamp,");
// ### ENDIF IVK ###
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "commitTs_in,");
}
// cdUserId
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + cdUserId_in + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V.ps_Oid,");

// versionId"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewName + " AS V, ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + pc_tempTabNameChangeLogCte + " AS bas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.switch = 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.objectId = V.objectId");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");


if (splitCaseColumns) {
//additional update statements

// update for newValueString
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.gc_tempTabNameChangeLog + " bas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueString");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + stringsPerType[3]);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + tabName + " AS V ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.objectId = V.oid");
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "V." + M01_Globals_IVK.g_anPsOid + " = psOid_in");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");

//update the rest
qualViewName = qualViewName + "_REDUCED";

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.gc_tempTabNameChangeLog + " bas");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueDate,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueDate,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueInteger,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueInteger,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueBigInt,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueBigInt,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueDecimal,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueDecimal,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueBoolean,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueBoolean,");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oldValueTimestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "newValueTimestamp");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT");

for (int i = 4; i <= 15; i++) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + stringsPerType[i]);
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualViewName + " AS V ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "bas.objectId = V.objectId");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
}


}


public static void genGenChangeLogRecordForCTEDdl(String opDescription, int fileNo, String[] stringsPerType, String valuesStringForCTE, String caseStringForCTE, boolean splitVar, String exprTabName, Integer ddlTypeW, String dbColumnNameW, String dbColumnNameAlternativeW, Integer dbColumnTypeW, Integer clModeW, Integer columnCategoryW, String valueNewW, Boolean columnIsNullableW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String dbColumnName; 
if (dbColumnNameW == null) {
dbColumnName = "";
} else {
dbColumnName = dbColumnNameW;
}

String dbColumnNameAlternative; 
if (dbColumnNameAlternativeW == null) {
dbColumnNameAlternative = "";
} else {
dbColumnNameAlternative = dbColumnNameAlternativeW;
}

Integer dbColumnType; 
if (dbColumnTypeW == null) {
dbColumnType = M01_Common.typeId.etNone;
} else {
dbColumnType = dbColumnTypeW;
}

Integer clMode; 
if (clModeW == null) {
clMode = M12_ChangeLog.ChangeLogMode.eclLrt;
} else {
clMode = clModeW;
}

Integer columnCategory; 
if (columnCategoryW == null) {
columnCategory = M01_Common.AttrCategory.eacRegular;
} else {
columnCategory = columnCategoryW;
}

String valueNew; 
if (valueNewW == null) {
valueNew = "";
} else {
valueNew = valueNewW;
}

boolean columnIsNullable; 
if (columnIsNullableW == null) {
columnIsNullable = false;
} else {
columnIsNullable = columnIsNullableW;
}


String oldValue;
String newValue;
String usedColumnName;
String countryString;
String originalDbColumnAlternative;
String exprString;

countryString = "";
exprString = "";

originalDbColumnAlternative = dbColumnNameAlternative;
//special case for STATUS_ID
if (dbColumnName == M01_Globals.g_anStatus) {
oldValue = "V." + dbColumnName + "_Io";
} else {
oldValue = "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_o";
}

if (valueNew != "") {
newValue = valueNew;
} else {
newValue = "V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_n";
}
usedColumnName = dbColumnName;
if (dbColumnName != "") {

if (dbColumnNameAlternative != "") {
if (columnCategory &  M01_Common.AttrCategory.eacNational) {
dbColumnNameAlternative = M04_Utilities.genAttrName(dbColumnNameAlternative, ddlType, null, null, null, null, true, null);
} else if (columnCategory &  M01_Common.AttrCategory.eacNationalBool) {
dbColumnNameAlternative = dbColumnNameAlternative + M01_Globals_IVK.gc_anSuffixNatActivated.toUpperCase();
usedColumnName = dbColumnNameAlternative;
}
}

if (M12_ChangeLog.isClAttrCat(columnCategory, (clMode == M12_ChangeLog.ChangeLogMode.eclLrt) |  (clMode == M12_ChangeLog.ChangeLogMode.eclPubUpdate) | (clMode == M12_ChangeLog.ChangeLogMode.eclPubMassUpdate))) {

caseStringForCTE = caseStringForCTE + vbTab + "-- " + opDescription + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + "CASE" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "WHEN" + vbCrLf + vbTab + vbTab + vbTab + vbTab;
if (((columnCategory &  M01_Common.AttrCategory.eacExpression) != 0 &  ((columnCategory &  M01_Common.AttrCategory.eacNationalBool) == 0))) {
caseStringForCTE = caseStringForCTE + " NOT (" + oldValue + " IS NULL AND " + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "AND" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " <> " + newValue + ")" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + ")" + vbCrLf;

oldValue = "V." + originalDbColumnAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIo";
String newValueExp;
newValueExp = "V." + originalDbColumnAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3) + "_BIn";
usedColumnName = dbColumnNameAlternative;

if (splitVar) {
exprString = "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING,750)) AS VARCHAR(750)) FROM " + exprTabName + " X WHERE X.OID = V." + usedColumnName + ")";
}

caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "NOT (" + oldValue + " IS NULL AND " + newValueExp + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "AND" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + newValueExp + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " <> " + newValueExp + ")" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + ")" + vbCrLf;
} else if ((columnCategory &  M01_Common.AttrCategory.eacFkOid)) {
oldValue = "V." + dbColumnName + "_BIo";
newValue = "V." + dbColumnName + "_BIn";
caseStringForCTE = caseStringForCTE + " NOT (" + oldValue + " IS NULL AND " + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "AND" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " <> " + newValue + ")" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + ")" + vbCrLf;
if ((M12_ChangeLog.isClAttrCat(columnCategory, clMode == M12_ChangeLog.ChangeLogMode.eclLrt) &  M03_Config.resolveCountryIdListInChangeLog & ((columnCategory &  M01_Common.AttrCategory.eacFkCountryIdList) != 0))) {
countryString = "(SELECT IDLIST FROM " + M01_Globals_IVK.g_qualTabNameCountryIdList + " WHERE OID = V." + dbColumnName;
}
} else {
if (columnIsNullable) {
caseStringForCTE = caseStringForCTE + " NOT (" + oldValue + " IS NULL AND " + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + "AND" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + newValue + " IS NULL)" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "OR" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "(" + oldValue + " <> " + newValue + ")" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + ")" + vbCrLf;
} else {
caseStringForCTE = caseStringForCTE + oldValue + " <> " + newValue + vbCrLf;
}
}

valuesStringForCTE = valuesStringForCTE + vbTab + vbTab + "('" + usedColumnName + "', bas." + usedColumnName + ")," + vbCrLf;

String prefix;
String suffix;

prefix = "    WHEN '" + usedColumnName + "' THEN V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2);
suffix = vbCrLf + "    ";
//special case for VAL_EXP_OID_t
if (columnCategory &  M01_Common.AttrCategory.eacNational & !(originalDbColumnAlternative.compareTo("") == 0)) {
stringsPerType[(1)] = stringsPerType[1] + "    WHEN '" + usedColumnName + "' THEN V." + originalDbColumnAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t" + suffix;
} else {
stringsPerType[(1)] = stringsPerType[1] + "    WHEN '" + usedColumnName + "' THEN V." + usedColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 2) + "_t" + suffix;
}

boolean isFk;
boolean isFkCountry;

isFk = (columnCategory &  M01_Common.AttrCategory.eacFkOid) != 0;
isFkCountry = (columnCategory &  M01_Common.AttrCategory.eacFkCountryIdList) != 0;
if ((isFk & ! isFkCountry)) {
// omit these entries
} else {
//special case for ACLACL_OID, DCLDCL_OID
if (!(countryString.compareTo("") == 0)) {
stringsPerType[(2)] = stringsPerType[2] + "    WHEN '" + usedColumnName + "' THEN " + countryString + "_o)" + suffix;
} else {
stringsPerType[(2)] = stringsPerType[2] + prefix + "_o" + suffix;
}
//special case for STATUS_ID
if ((usedColumnName.compareTo(M01_Globals.g_anStatus) == 0)) {
stringsPerType[(3)] = stringsPerType[3] + "    WHEN '" + usedColumnName + "' THEN CAST(RTRIM(CAST(" + newValue + " AS CHAR(254))) AS VARCHAR(4000))" + suffix;
} else {
//special case for ACLACL_OID, DCLDCL_OID
if (!(countryString.compareTo("") == 0)) {
stringsPerType[(3)] = stringsPerType[3] + "    WHEN '" + usedColumnName + "' THEN " + countryString;
//no _n
if (splitVar) {
stringsPerType[(3)] = stringsPerType[3] + ")" + suffix;
} else {
stringsPerType[(3)] = stringsPerType[3] + "_n)" + suffix;
}
} else {
if (splitVar) {
// special case for expressions
if (!(exprString.compareTo("") == 0)) {
stringsPerType[(3)] = stringsPerType[3] + "    WHEN '" + usedColumnName + "' THEN " + exprString + suffix;
} else {
stringsPerType[(3)] = stringsPerType[3] + "    WHEN '" + usedColumnName + "' THEN CAST(V." + usedColumnName + " AS VARCHAR(4000))" + suffix;
}
} else {
stringsPerType[(3)] = stringsPerType[3] + "    WHEN '" + usedColumnName + "' THEN " + newValue + suffix;
}
}
}
}
prefix = "    WHEN '" + usedColumnName + "' THEN V." + dbColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 3);
if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDate)) {
//Views have column length 31
//prefix = "    WHEN '" & usedColumnName & "' THEN V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 4)
stringsPerType[(4)] = stringsPerType[4] + prefix + "_Dto" + suffix;
stringsPerType[(5)] = stringsPerType[5] + prefix + "_Dtn" + suffix;
} else if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeInteger)) {
stringsPerType[(6)] = stringsPerType[6] + prefix + "_Io" + suffix;
//special case for STATUS_ID
if ((usedColumnName.compareTo(M01_Globals.g_anStatus) == 0)) {
stringsPerType[(7)] = stringsPerType[7] + "    WHEN '" + usedColumnName + "' THEN INTEGER(" + newValue + ")" + suffix;
} else {
stringsPerType[(7)] = stringsPerType[7] + prefix + "_In" + suffix;
}
} else if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBigInteger) |  (dbColumnNameAlternative == usedColumnName & ! (usedColumnName.length() > 12 &  usedColumnName.substring(usedColumnName.length() - 1 - 12) == "_ISNATACTIVE"))) {
String prefixForBI;
// special case special case for VAL_EXP_OID
if (columnCategory &  M01_Common.AttrCategory.eacNational & !(originalDbColumnAlternative.compareTo("") == 0)) {
prefixForBI = "    WHEN '" + usedColumnName + "' THEN V." + originalDbColumnAlternative.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 4);
//special case expressions
} else if (dbColumnNameAlternative == usedColumnName) {
prefixForBI = "    WHEN '" + usedColumnName + "' THEN V." + usedColumnName.substring(0, M01_LDM.gc_dbMaxAttributeNameLength - 4);
} else {
prefixForBI = prefix;
}
stringsPerType[(8)] = stringsPerType[8] + prefixForBI + "_BIo" + suffix;
stringsPerType[(9)] = stringsPerType[9] + prefixForBI + "_BIn" + suffix;
} else if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeDecimal)) {
stringsPerType[(10)] = stringsPerType[10] + prefix + "_Do" + suffix;
stringsPerType[(11)] = stringsPerType[11] + prefix + "_Dn" + suffix;
} else if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeBoolean)) {
stringsPerType[(12)] = stringsPerType[12] + prefix + "_Bo" + suffix;
stringsPerType[(13)] = stringsPerType[13] + prefix + "_Bn" + suffix;
} else if (M12_ChangeLog.attrTypeMapsToClColType(dbColumnType, M12_ChangeLog.ChangeLogColumnType.clValueTypeTimeStamp)) {
stringsPerType[(14)] = stringsPerType[14] + prefix + "_To" + suffix;
stringsPerType[(15)] = stringsPerType[15] + prefix + "_Tn" + suffix;
}
}

caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "THEN" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + vbTab + "1" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + "ELSE" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + vbTab + vbTab + "0" + vbCrLf;
caseStringForCTE = caseStringForCTE + vbTab + vbTab + "END AS " + usedColumnName + "," + vbCrLf;
}

}



}