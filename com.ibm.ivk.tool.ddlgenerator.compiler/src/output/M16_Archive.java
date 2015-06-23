package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M16_Archive {


// ### IF IVK ###


public static final String tempArchiveTabStatsTabName = "SESSION.ArchiveTabStats";
public static final String tempArchiveIndStatsTabName = "SESSION.ArchiveIndStats";
public static final String tempPsDates = "SESSION.PsDates";
public static final String tempToBeArchived = "SESSION.ToBeArchived";

private static final int processingStep = 2;

private static final boolean usePsDpMappingForArchiveViews = false;

public static void genDdlForCalculationRunCheckTypeSpec(int fileNo, Integer indentW, String typeSpecW, String typeSpecWorkW, String aliasW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String typeSpec; 
if (typeSpecW == null) {
typeSpec = null;
} else {
typeSpec = typeSpecW;
}

String typeSpecWork; 
if (typeSpecWorkW == null) {
typeSpecWork = null;
} else {
typeSpecWork = typeSpecWorkW;
}

String alias; 
if (aliasW == null) {
alias = null;
} else {
alias = aliasW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NOT EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + typeSpec + " TYPSPROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "TYPSPROD.CRTCAR_OID = " + alias + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "TYPSPROD." + M01_Globals_IVK.g_anPsOid + " = " + alias + "." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + typeSpecWork + " TYPSWORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "TYPSWORK.CRTCAR_OID = " + alias + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "TYPSWORK." + M01_Globals_IVK.g_anPsOid + " = " + alias + "." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");
}

public static void genDdlForWorkProdJoinWithPs(int fileNo, Integer indentW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "PROD." + M01_Globals.g_anOid + " = WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "PROD." + M01_Globals_IVK.g_anPsOid + " = WORK." + M01_Globals_IVK.g_anPsOid);
}

public static void genDdlForTypeSpecCheckNsr(int fileNo, Integer indentW, String tsAliasW, String refTsColumnNameW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String tsAlias; 
if (tsAliasW == null) {
tsAlias = null;
} else {
tsAlias = tsAliasW;
}

String refTsColumnName; 
if (refTsColumnNameW == null) {
refTsColumnName = null;
} else {
refTsColumnName = refTsColumnNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DATE(NSR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals.g_anOid + " = " + tsAlias + ".TSNN1V_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals_IVK.g_anPsOid + " = " + tsAlias + "." + M01_Globals_IVK.g_anPsOid);
}
public static void genDdlForTypeStandardEquipmentCheckTypeSpecNsr(int fileNo, Integer indentW, String tseAliasW, String refTsColumnNameW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String tseAlias; 
if (tseAliasW == null) {
tseAlias = null;
} else {
tseAlias = tseAliasW;
}

String refTsColumnName; 
if (refTsColumnNameW == null) {
refTsColumnName = null;
} else {
refTsColumnName = refTsColumnNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DATE(NSR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals.g_anOid + " = TYPS.TSNN1V_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals_IVK.g_anPsOid + " = TYPS." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals.g_anOid + " = " + tseAlias + ".TSETYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = " + tseAlias + "." + M01_Globals_IVK.g_anPsOid);
}
public static void genDdlForProtocolLineEntryCheckTypeSpecNsr(int fileNo, Integer indentW, String refTsColumnNameW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String refTsColumnName; 
if (refTsColumnNameW == null) {
refTsColumnName = null;
} else {
refTsColumnName = refTsColumnNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DATE(NSR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals.g_anOid + " = TYPS.TSNN1V_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals_IVK.g_anPsOid + " = TYPS." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals.g_anOid + " = WORK.TSPTYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = WORK." + M01_Globals_IVK.g_anPsOid);
}
public static void genDdlForProtocolParameterCheckPleTypeSpecNsr(int fileNo, Integer indentW, String refTsColumnNameW) {
int indent; 
if (indentW == null) {
indent = 1;
} else {
indent = indentW;
}

String refTsColumnName; 
if (refTsColumnNameW == null) {
refTsColumnName = null;
} else {
refTsColumnName = refTsColumnNameW;
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR.CLASSID = '09005'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DATE(NSR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals.g_anOid + " = TYPS.TSNN1V_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "NSR." + M01_Globals_IVK.g_anPsOid + " = TYPS." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals.g_anOid + " = PLE.TSPTYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = PLE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "PLE." + M01_Globals.g_anOid + " = WORK.PLPLEN_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "PLE." + M01_Globals_IVK.g_anPsOid + " = WORK." + M01_Globals_IVK.g_anPsOid);
}

public static void genDdlForTempArchiveStats(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for (Table-) Statistics / Estimates on Archive Data", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M16_Archive.tempArchiveTabStatsTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orgId           " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "poolId          " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tabSchema       " + M01_Globals.g_dbtDbSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tabName         VARCHAR(50),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "card            BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "avgRowLen       INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "size            BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "cardArch        BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sizeArch        BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

M11_LRT.genProcSectionHeader(fileNo, "temporary table for (Index-) Statistics / Estimates on Archive Data", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M16_Archive.tempArchiveIndStatsTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "orgId           " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "poolId          " + M01_Globals.g_dbtEnumId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tabSchema       " + M01_Globals.g_dbtDbSchemaName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "tabName         VARCHAR(50),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "indName         VARCHAR(20),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "card            BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "avgKeyLen       INTEGER,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "size            BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "cardArch        BIGINT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "sizeArch        BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);
}

public static void genDdlForTempPsDates(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW, Boolean inclGenWsProdTsW) {
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

boolean inclGenWsProdTs; 
if (inclGenWsProdTsW == null) {
inclGenWsProdTs = false;
} else {
inclGenWsProdTs = inclGenWsProdTsW;
}

M11_LRT.genProcSectionHeader(fileNo, "temporary table for min FTO-Date", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M16_Archive.tempPsDates);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "psOid           BIGINT,");
if (inclGenWsProdTs) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "genWsProd       TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "fto             TIMESTAMP");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "ftoCommit       TIMESTAMP");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

}
public static void genDdlForTempToBeArchived(int fileNo, Integer indentW, Boolean withReplaceW, Boolean onCommitPreserveW, Boolean onRollbackPreserveW) {
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

M11_LRT.genProcSectionHeader(fileNo, "temporary table for changelog recordes to be archived", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "DECLARE GLOBAL TEMPORARY TABLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 1) + "oid           BIGINT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 0) + ")");

M11_LRT.genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve);

}


public static void genArchiveSupportDdl(Integer ddlType) {
int thisOrgIndex;

if (!(M03_Config.supportArchivePool)) {
return;
}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {

for (thisOrgIndex = 1; thisOrgIndex <= 1; thisOrgIndex += (1)) {
if (M72_DataPool.poolIsValidForOrg(M01_Globals_IVK.g_productiveDataPoolIndex, thisOrgIndex)) {
genArchiveSupportDdlByPool(thisOrgIndex, M01_Common.DdlTypeId.edtPdm);
genArchiveOrgPurgeDdlByPool(thisOrgIndex, M01_Common.DdlTypeId.edtPdm);
}
}
}
}

private static void genArchiveOrgPurgeDdlByPool( int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.supportArchivePool |  ddlType != M01_Common.DdlTypeId.edtPdm)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexDbAdmin, processingStep, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolId, null, M01_Common.phaseArchive, null);

String qualProcNameArchiveOrgPurge;
qualProcNameArchiveOrgPurge = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnArchiveOrgPurge, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolId, null, null, null, null);

String qualProcNameArchiveOrgPurgeChg;
qualProcNameArchiveOrgPurgeChg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnArchiveOrgPurge, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolId, null, null, null, null);

String thisMetSchema;
thisMetSchema = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, ddlType, thisOrgIndex, null);
//schemaNameDataFix = genSchemaName(snDataFix, ssnDataFix, ddlType)

String qualProcedureNameReorg;
qualProcedureNameReorg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnReorg, ddlType, null, null, null, null, null, null);

// ####################################################################################################################
// #    SP for Purging Data per Organization (2 parameters)
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Purging Archive Data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrgPurge);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "purgeUserId_in", M01_Globals.g_dbtUserId, true, "user for ArchiveHistory entry");
M11_LRT.genProcParm(fileNo, "IN", "clOnly_in", "INTEGER", true, "purge only ChangeLog-records if this parameter is '1'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_useCase", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_failCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_purgeTimeStamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrgPurge, ddlType, null, "#refDate_in", "'purgeUserId_in", "clOnly_in", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_purgeTimeStamp = CURRENT TIMESTAMP;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_orgOid = (SELECT ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE SEQUENCESCHEMANAME = '" + thisMetSchema + "');");

M11_LRT.genProcSectionHeader(fileNo, "process each table separately which is 'subject to archiving'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF clOnly_in <> 1 THEN");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anAcmIsArch + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_archiveDataPoolId, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.ENTITYID <> '23001'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each table individually", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnArchiveOrgPurge.toUpperCase() + "_' || c_tableName || '(?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
//Print #fileNo, addTab(2); "COMMIT;"

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgPurgeChg + "_CHANGELOG_NL_TEXT(?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
//Print #fileNo, addTab(1); "COMMIT;"
//reorg!
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgPurgeChg); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );"
//Print #fileNo, addTab(1); "COMMIT;"
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgPurgeChg + "_CHANGELOG(?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 1, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = rowCount_out + v_rowCount;");
//Print #fileNo, addTab(1); "COMMIT;"
//reorg!
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgPurgeChg); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a reorg
//Print #fileNo, addTab(1); "COMMIT;"
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF clOnly_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_useCase = 'UC1368CL'; ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_useCase = 'UC1368';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VL6CMET.ARCHIVEHISTORY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ARCHIVEDATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USECASE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CDUSERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "STARTTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENDTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OBJECTCOUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEXT VALUE FOR VL6CMET.OIDSEQUENCE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_useCase,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "purgeUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_purgeTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "current timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrgPurge, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Purging Data per Organization (2 parameters)
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Purging Archive Data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrgPurge);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "purgeUserId_in", M01_Globals.g_dbtUserId, true, "user for ArchiveHistory entry");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrgPurge, ddlType, null, "#refDate_in", "'purgeUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgPurge + "(?,?,0,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "purgeUserId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrgPurge, ddlType, null, "#refDate_in", "'purgeUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);
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


private static void genArchiveSupportDdlByPool( int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

if (!(M03_Config.supportArchivePool |  ddlType != M01_Common.DdlTypeId.edtPdm)) {
return;
}

//On Error GoTo ErrorExit 

int fileNo;
fileNo = M04_Utilities.openDdlFile(M01_Globals.g_targetDir, M01_Globals.g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, M01_Common.phaseArchive, null);

// ####################################################################################################################
// #    SP for Archiving Data per Organization
// ####################################################################################################################

String qualProcNameArchiveOrg;
qualProcNameArchiveOrg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexAliasLrt, M01_ACM_IVK.spnArchiveOrg, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null);

String qualProcNameArchiveOrgChg;
qualProcNameArchiveOrgChg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnArchiveOrg, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null);

String qualProcNameArchiveOrgChgWork;
qualProcNameArchiveOrgChgWork = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnArchiveOrg, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, null, null, null, null);

String thisMetSchema;
thisMetSchema = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, ddlType, thisOrgIndex, null);

String qualProcedureNameReorg;
qualProcedureNameReorg = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM.spnReorg, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Archiving data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "archUserId_in", M01_Globals.g_dbtUserId, true, "archived records are tagged with this user as '" + M01_ACM.conUpdateUser + "'");
M11_LRT.genProcParm(fileNo, "IN", "clOnly_in", "INTEGER", true, "archive only ChangeLog-records if and only if this parameter is '1'");
M11_LRT.genProcParm(fileNo, "IN", "onDataPoolOnly_in", "INTEGER", true, "archive only ChangeLog-records in one data pool if and only if this parameter is '1' for working or '3' for productiv");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_useCase", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_tabCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_failCount", "INTEGER", "0", null, null);
M11_LRT.genVarDecl(fileNo, "v_archTimeStamp", "TIMESTAMP", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", M01_Globals.g_dbtOid, "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_objCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M16_Archive.genDdlForTempPsDates(fileNo, 1, true, null, null, null);
}

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_archTimeStamp = CURRENT TIMESTAMP;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_orgOid = (SELECT ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE SEQUENCESCHEMANAME = '" + thisMetSchema + "');");

M11_LRT.genProcSectionHeader(fileNo, "runstats / rebind", null, null);
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
// Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CASP"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', 'VL6CASP010', NULL, v_objCount, v_failCount );"
} else {
// Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CASP"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', NULL, NULL, v_objCount, v_failCount );"
}
//Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CDEC"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', NULL, NULL, v_objCount, v_failCount );"
//Print #fileNo, addTab(1); "CALL VL6CDBA.REBIND(2, 'VL6%"; genOrgId(thisOrgIndex, ddlType, False); "3', 'ARCHIVEORG_%', 1, v_objCount);"

M11_LRT.genProcSectionHeader(fileNo, "process each table separately which is 'subject to archiving'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF clOnly_in <> 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET onDataPoolOnly_in = 0;");

if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {

M11_LRT.genProcSectionHeader(fileNo, "get last FTO creation date", 2, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR orgLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SCHEMANAME AS c_schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmPrimarySchema);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "POOLTYPE_ID = 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORGANIZATION_ID > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORGANIZATION_ID ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = 'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '" + M16_Archive.tempPsDates + " T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'USING (SELECT ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || '" + M01_Globals_IVK.g_anPsOid + ", ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'LASTCENTRALDATATRANSFERCOMMIT AS FTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'FROM ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || c_schemaName || '.GENERALSETTINGS ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'T.psOid = S." + M01_Globals_IVK.g_anPsOid + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'WHEN MATCHED AND COALESCE(T.ftoCommit, '" + M01_LDM_IVK.gc_valDateInfinite + "') > COALESCE(S.FTO, '" + M01_LDM_IVK.gc_valDateInfinite + "') THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'UPDATE SET T.ftoCommit = S.FTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'INSERT (psOid, ftoCommit) VALUES (S." + M01_Globals_IVK.g_anPsOid + ", S.FTO) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A." + M01_Globals_IVK.g_anAcmIsArch + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "A.ENTITYID <> '23001'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each table individually", 3, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnArchiveOrg.toUpperCase() + "_' || c_tableName || '(?,?,?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "v_archTimeStamp");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 3, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameOrganization);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(LASTARCHIVEDATE, UPDATEUSER, LASTUPDATETIMESTAMP) ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "= ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(DATE(refDate_in), archUserId_in, current timestamp)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "COALESCE(LASTARCHIVEDATE, " + M01_LDM_IVK.gc_valDateEarliest + ") < DATE(refDate_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF onDataPoolOnly_in <> 3 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgChg + "_CHANGELOG_NL_TEXT_WP(?,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_archTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "clOnly_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChgWork); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );"
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgChg + "_CHANGELOG_WP(?,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_archTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "clOnly_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChgWork); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a reorg
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF onDataPoolOnly_in <> 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgChg + "_CHANGELOG_NL_TEXT_PP(?,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_archTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "clOnly_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
//Print #fileNo, addTab(2); "COMMIT;"
//Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChg); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrgChg + "_CHANGELOG_PP(?,?,?,?,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_archTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "clOnly_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");
//Print #fileNo, addTab(2); "COMMIT;"
//Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChg); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a
//Print #fileNo, addTab(2); "COMMIT;"
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF clOnly_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_useCase = 'UC841CL'; ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF onDataPoolOnly_in = 1 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_useCase = v_useCase || '_WP';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF onDataPoolOnly_in = 3 THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_useCase = v_useCase || '_PP';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_useCase = 'UC841';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VL6CMET.ARCHIVEHISTORY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ARCHIVEDATE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USECASE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CDUSERID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "STARTTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ENDTIME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OBJECTCOUNT,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AHOORG_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEXT VALUE FOR VL6CMET.OIDSEQUENCE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_useCase,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_archTimeStamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "current timestamp,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);


// ####################################################################################################################
// #    SP for Archiving Data per Organization - Wrapper (2 input parms)
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Archiving data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "archUserId_in", M01_Globals.g_dbtUserId, true, "archived records are tagged with this user as '" + M01_ACM.conUpdateUser + "'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrg + "(?,?,0,0,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "archUserId_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Archiving Data per Organization - Wrapper (3 input parms)
// ####################################################################################################################

M22_Class_Utilities.printSectionHeader("SP for Archiving data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrg);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "archUserId_in", M01_Globals.g_dbtUserId, true, "archived records are tagged with this user as '" + M01_ACM.conUpdateUser + "'");
M11_LRT.genProcParm(fileNo, "IN", "clOnly_in", "INTEGER", true, "archive only ChangeLog-records if and only if this parameter is '1'");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_stmntTxt  = 'CALL " + qualProcNameArchiveOrg + "(?,?,?,0,?)';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "PREPARE v_stmnt FROM v_stmntTxt;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "rowCount_out");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "refDate_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "clOnly_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrg, ddlType, null, "#refDate_in", "'archUserId_in", "rowCount_out", null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Estimating Volume of Archive Data per Organization
// ####################################################################################################################

String qualProcNameArchiveOrgEstimate;
qualProcNameArchiveOrgEstimate = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexDbAdmin, M01_ACM_IVK.spnArchiveOrgEstimate, ddlType, thisOrgIndex, null, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Estimating Volume of Archive Data (per Organization)", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrgEstimate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "count only data with validity ending before this date");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows to be archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, true);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "INTEGER", "0", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrgEstimate, ddlType, null, "#refDate_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "process each table separately which is 'subject to archiving'", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmFkSchemaName + " AS c_schemaName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmTableName + " AS c_tableName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = L." + M01_Globals.g_anAcmEntitySection);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals_IVK.g_anAcmIsArch + " = " + M01_LDM.gc_dbTrue);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = " + M01_LDM.gc_dbFalse);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmFkSequenceNo + " DESC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "process each table individually", 2, true);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt  = 'CALL ' || c_schemaName || '." + M01_ACM_IVK.spnArchiveOrgEstimate.toUpperCase() + "_' || c_tableName || '(?,?)';");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXECUTE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_stmnt");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");

M11_LRT.genProcSectionHeader(fileNo, "add to number of affected rows", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET rowCount_out = rowCount_out + v_rowCount;");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrgEstimate, ddlType, null, "#refDate_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

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


public static void genArchiveOrgPurgeDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex, int archPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Boolean isPurelyPrivateW) {
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

boolean isPurelyPrivate; 
if (isPurelyPrivateW == null) {
isPurelyPrivate = false;
} else {
isPurelyPrivate = isPurelyPrivateW;
}

if (!(M03_Config.supportArchivePool |  ddlType != M01_Common.DdlTypeId.edtPdm)) {
return;
}

String acmEntityName;
String acmEntityShortName;
String dbObjName;
String dbObjShortName;
String entityTypeDescr;
String sectionName;
String sectionShortName;
int sectionIndex;
boolean isSubjectToArchiving;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isCtoAliasCreated;
boolean notAcmRelated;
int ahClassIndex;
boolean isAggHead;
String refTsColumnName;
boolean hasOwnTable;
boolean isUserTransactional;
boolean M03_Config.useMqtToImplementLrt;
boolean isPsTagged;
boolean psTagOptional;
boolean condenseData;
boolean expandExpressionsInFtoView;

//On Error GoTo ErrorExit 

isAggHead = false;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;

if (forNl) {
dbObjName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
dbObjShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged & !M22_Class.g_classes.descriptors[acmEntityIndex].noRangePartitioning;
} else {
dbObjName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
dbObjShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
}
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isSubjectToArchiving = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToArchiving;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
notAcmRelated = M22_Class.g_classes.descriptors[acmEntityIndex].notAcmRelated;
ahClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
refTsColumnName = M04_Utilities.genAttrName((!(M22_Class.g_classes.descriptors[acmEntityIndex].nonStandardRefTimeStampForArchiving.compareTo("") == 0) ? M22_Class.g_classes.descriptors[acmEntityIndex].nonStandardRefTimeStampForArchiving : M01_ACM.conValidTo), ddlType, null, null, null, null, null, null);
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
expandExpressionsInFtoView = M22_Class.g_classes.descriptors[acmEntityIndex].expandExpressionsInFtoView;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;

if (forNl) {
dbObjName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
dbObjShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged & !M23_Relationship.g_relationships.descriptors[acmEntityIndex].noRangePartitioning;
} else {
dbObjName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
dbObjShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
}

sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isSubjectToArchiving = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToArchiving;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
notAcmRelated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].notAcmRelated;
ahClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
refTsColumnName = M01_Globals_IVK.g_anValidTo;
hasOwnTable = true;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = false;
expandExpressionsInFtoView = false;
} else {
return;
}

String qualAggHeadTabNameArch;
if (ahClassIndex > 0) {
qualAggHeadTabNameArch = M04_Utilities.genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolId, null, null, null, null, null, null, null);
refTsColumnName = M04_Utilities.genAttrName((!(M22_Class.g_classes.descriptors[ahClassIndex].nonStandardRefTimeStampForArchiving.compareTo("") == 0) ? M22_Class.g_classes.descriptors[ahClassIndex].nonStandardRefTimeStampForArchiving : M01_ACM.conValidTo), ddlType, null, null, null, null, null, null);
}

if (!(isSubjectToArchiving)) {
return;
}

final int ctoOrgId = 1;
String qualTabNameArch;
qualTabNameArch = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabParentNameArch;
qualTabParentNameArch = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, false, null, null, null);

String qualTabNameProd;
qualTabNameProd = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameWork;
qualTabNameWork = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameProdPar;
if (forNl) {
qualTabNameProdPar = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, null, null, null, null);
}

String qualTabNameArchiveLog;
qualTabNameArchiveLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexArchLog, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, null, null, null, null, null, null, null);

String qualTabNameTypeSpecNameArch;
qualTabNameTypeSpecNameArch = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameAspectArch;
qualTabNameAspectArch = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameProtocolLineEntryArch;
qualTabNameProtocolLineEntryArch = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexProtocolLineEntry, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String baseArchTabName;
baseArchTabName = M04_Utilities.baseName(qualTabNameArch, null, ".", null, null);

// ####################################################################################################################
// #    SP for Purging Arche Data for individual Entity
// ####################################################################################################################

String qualProcNameArchiveOrPugeEntity;
qualProcNameArchiveOrPugeEntity = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolId, forGen, null, null, forNl, M01_ACM_IVK.spnArchiveOrgPurge, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Purging Arche Data for " + entityTypeDescr + " '" + sectionName + "." + dbObjName + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveOrPugeEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveOrPugeEntity, ddlType, null, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "purge archive log records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArchiveLog + " ALOG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DBTABLENAME = '" + baseArchTabName + "'");
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " CRUN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS.CRTCAR_OID = CRUN." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = CRUN." + M01_Globals_IVK.g_anPsOid + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = CRUN." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = TYPS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " TYSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals.g_anOid + " = TYSE.TSETYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = TYSE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = TYSE." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " PLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals.g_anOid + " = PLE.TSPTYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = PLE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = PLE." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " PROP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProtocolLineEntryArch + " PLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PLE." + M01_Globals.g_anOid + " = PROP.PLPLEN_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PLE." + M01_Globals_IVK.g_anPsOid + " = PROP." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals.g_anOid + " = PLE.TSPTYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = PLE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = PROP." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}default: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualAggHeadTabNameArch + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " ARCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCH." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCH." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + refTsColumnName + " < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = ARCH." + M01_Globals.g_anOid);
} else {
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " ARCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(ARCH." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = ARCH." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameArch + " ARCHNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabParentNameArch + " ARCHPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
//To changed to dynamic key name oder switch case if other non aggregate nl text tables follow
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCHNL.CLG_OID = ARCHPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ARCHNL." + M01_Globals_IVK.g_anPsOid + " = ARCHPAR." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ALOG.OBJECTID = ARCHNL." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(ARCHPAR." + refTsColumnName + ") < refDate_in");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete records in archive data pool", null, null);
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " CRUN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NOT EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = CRUN." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS.CRTCAR_OID = CRUN." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " TYSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals.g_anOid + " = TYSE.TSETYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " PLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PLE." + M01_Globals_IVK.g_anPsOid + " = TYPS." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PLE.TSPTYS_OID = TYPS." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " PROP");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProtocolLineEntryArch + " PLE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameArch + " TYPS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals.g_anOid + " = PLE.TSPTYS_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "TYPS." + M01_Globals_IVK.g_anPsOid + " = PLE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectArch + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "TYPS", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROP." + M01_Globals_IVK.g_anPsOid + " = PLE." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROP.PLPLEN_OID = PLE." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}default: {if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " ARCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualAggHeadTabNameArch + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCH." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCH." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + refTsColumnName + " < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(" + refTsColumnName + ") < refDate_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " ARCHNL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabParentNameArch + " ARCHPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
//To changed to dynamic key name oder switch case if other non aggregate nl text tables follow
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ARCHNL.CLG_OID = ARCHPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ARCHNL." + M01_Globals_IVK.g_anPsOid + " = ARCHPAR." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(ARCHPAR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}
}}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveOrPugeEntity, ddlType, null, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, "");

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genArchiveSupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex, int archPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forNlW, Boolean isPurelyPrivateW, Boolean isChangeLogWorkingPoolSpecialHandlingW) {
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

boolean isPurelyPrivate; 
if (isPurelyPrivateW == null) {
isPurelyPrivate = false;
} else {
isPurelyPrivate = isPurelyPrivateW;
}

boolean isChangeLogWorkingPoolSpecialHandling; 
if (isChangeLogWorkingPoolSpecialHandlingW == null) {
isChangeLogWorkingPoolSpecialHandling = false;
} else {
isChangeLogWorkingPoolSpecialHandling = isChangeLogWorkingPoolSpecialHandlingW;
}

if (!(M03_Config.supportArchivePool |  ddlType != M01_Common.DdlTypeId.edtPdm)) {
return;
}

String acmEntityName;
String acmEntityShortName;
String dbObjName;
String dbObjShortName;
String entityTypeDescr;
String sectionName;
String sectionShortName;
int sectionIndex;
boolean isSubjectToArchiving;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean isCtoAliasCreated;
boolean notAcmRelated;
int ahClassIndex;
boolean isAggHead;
String refTsColumnName;
boolean hasOwnTable;
boolean isUserTransactional;
boolean M03_Config.useMqtToImplementLrt;
boolean isPsTagged;
boolean psTagOptional;
boolean condenseData;
boolean expandExpressionsInFtoView;

//On Error GoTo ErrorExit 

isAggHead = false;
if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
acmEntityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
acmEntityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;

if (forNl) {
dbObjName = M04_Utilities.genNlObjName(M22_Class.g_classes.descriptors[acmEntityIndex].className, null, forGen, null);
dbObjShortName = M04_Utilities.genNlObjShortName(M22_Class.g_classes.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Class (NL-Text)";
isPsTagged = M03_Config.usePsTagInNlTextTables &  M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged & !M22_Class.g_classes.descriptors[acmEntityIndex].noRangePartitioning;
} else {
dbObjName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
dbObjShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
isAggHead = (M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex > 0) &  (M22_Class.g_classes.descriptors[acmEntityIndex].orMappingSuperClassIndex == M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex);
}
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
sectionShortName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
isSubjectToArchiving = M22_Class.g_classes.descriptors[acmEntityIndex].isSubjectToArchiving;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
notAcmRelated = M22_Class.g_classes.descriptors[acmEntityIndex].notAcmRelated;
ahClassIndex = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIndex;
refTsColumnName = M04_Utilities.genAttrName((!(M22_Class.g_classes.descriptors[acmEntityIndex].nonStandardRefTimeStampForArchiving.compareTo("") == 0) ? M22_Class.g_classes.descriptors[acmEntityIndex].nonStandardRefTimeStampForArchiving : M01_ACM.conValidTo), ddlType, null, null, null, null, null, null);
hasOwnTable = M22_Class.g_classes.descriptors[acmEntityIndex].hasOwnTable;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
M03_Config.useMqtToImplementLrt = M22_Class.g_classes.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = M22_Class.g_classes.descriptors[acmEntityIndex].condenseData;
expandExpressionsInFtoView = M22_Class.g_classes.descriptors[acmEntityIndex].expandExpressionsInFtoView;
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
acmEntityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
acmEntityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;

if (forNl) {
dbObjName = M04_Utilities.genNlObjName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName, null, forGen, null);
dbObjShortName = M04_Utilities.genNlObjShortName(M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName, null, forGen, true);
entityTypeDescr = "ACM-Relationship (NL-Text)";
isPsTagged = M03_Config.usePsTagInNlTextTables &  M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged & !M23_Relationship.g_relationships.descriptors[acmEntityIndex].noRangePartitioning;
} else {
dbObjName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
dbObjShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
}

sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
sectionShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionShortName;
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
isSubjectToArchiving = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isSubjectToArchiving;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
notAcmRelated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].notAcmRelated;
ahClassIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].aggHeadClassIndex;
refTsColumnName = M01_Globals_IVK.g_anValidTo;
hasOwnTable = true;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
M03_Config.useMqtToImplementLrt = M23_Relationship.g_relationships.descriptors[acmEntityIndex].useMqtToImplementLrt;
condenseData = false;
expandExpressionsInFtoView = false;
} else {
return;
}

String qualAggHeadTabNameProd;
if (ahClassIndex > 0) {
qualAggHeadTabNameProd = M04_Utilities.genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, null, null, null, null, null, null, null);
refTsColumnName = M04_Utilities.genAttrName((!(M22_Class.g_classes.descriptors[ahClassIndex].nonStandardRefTimeStampForArchiving.compareTo("") == 0) ? M22_Class.g_classes.descriptors[ahClassIndex].nonStandardRefTimeStampForArchiving : M01_ACM.conValidTo), ddlType, null, null, null, null, null, null);
}

final int ctoOrgId = 1;
String qualTabNameArch;
qualTabNameArch = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameProd;
qualTabNameProd = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameWork;
qualTabNameWork = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameAspectProd;
qualTabNameAspectProd = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameAspectArch;
qualTabNameAspectArch = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, null, null, 0, null, null, null);

String qualTabNameTypeSpecNameProd;
qualTabNameTypeSpecNameProd = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameProtocolLineEntryWork;
qualTabNameProtocolLineEntryWork = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexProtocolLineEntry, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameProtocolParameterWork;
qualTabNameProtocolParameterWork = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexProtocolParameter, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameTypeSpecNameWork;
qualTabNameTypeSpecNameWork = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, forNl, null, null, null);

String qualTabNameTypeSpecNameWorkLrt;
qualTabNameTypeSpecNameWorkLrt = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, true, null, forNl, null, null, null);

String qualTabNameSolverData;
qualTabNameSolverData = M04_Utilities.genQualTabNameByEntityIndex(M01_Globals_IVK.g_classIndexSolverData, acmEntityType, ddlType, null, null, null, null, null, false, null, null, null);

String qualTabNameProdPar;
if (forNl) {
qualTabNameProdPar = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, null, null, null, null);
}

String qualTabNameProdParWork;
if (forNl) {
qualTabNameProdParWork = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals.g_workDataPoolIndex, forGen, null, null, null, null, null, null);
}

String tabQualifier;
tabQualifier = acmEntityShortName.toUpperCase();

String baseArchTabName;
baseArchTabName = M04_Utilities.baseName(qualTabNameArch, null, ".", null, null);

String qualTabNameArchiveLog;
qualTabNameArchiveLog = M04_Utilities.genQualTabNameByClassIndex(M01_Globals_IVK.g_classIndexArchLog, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, null, null, null, null, null, null, null);

String attrNameFkEntity;
attrNameFkEntity = M04_Utilities.genSurrogateKeyName(ddlType, acmEntityShortName, null, null, null, null);

String thisMetSchema;
thisMetSchema = M04_Utilities.genSchemaName(M01_ACM.snMeta, M01_ACM.ssnMeta, ddlType, thisOrgIndex, null);

String qualViewName;
String qualViewNameLdm;

// ####################################################################################################################
// #    View 'linking' archive data to productive data
// ####################################################################################################################

if (M03_Config.generateArchiveView & ! )) {
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, forGen, false, null, forNl, null, "ARC", null, null);

if (!(isSubjectToArchiving)) {
M22_Class_Utilities.printSectionHeader("View 'linking' archive data to productive data / table \"" + qualTabNameProd + "\" (" + entityTypeDescr + " \"" + sectionName + "." + dbObjName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conWorkingState, M01_ACM.conWorkingState, M24_Attribute_Utilities.AttrValueType.eavtEnum, M21_Enum.getEnumIndexByName(M01_ACM.dxnWorkingState, M01_ACM.dnWorkingState, null), acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

if (condenseData) {
// virtually merge-in columns 'INLRT', and 'STATUS_ID'
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInLrt, M01_ACM.cosnInLrt, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexLrtId, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta, null, 1, true, null), null, null);
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM_IVK.enStatus, M01_ACM_IVK.esnStatus, M24_Attribute_Utilities.AttrValueType.eavtEnum, M01_Globals_IVK.g_enumIndexStatus, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacLrtMeta |  M01_Common.AttrCategory.eacSetProdMeta, null, 1, true, null), null, null);
}

M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 1, forGen, null, (isUserTransactional ? M01_Common.DdlOutputMode.edomListLrt : M01_Common.DdlOutputMode.edomListNonLrt) |  M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 1, false, forGen, (isUserTransactional ? M01_Common.DdlOutputMode.edomListLrt : M01_Common.DdlOutputMode.edomListNonLrt) |  M01_Common.DdlOutputMode.edomListVirtual | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (isPurelyPrivate) {
if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}

if (condenseData) {
// virtually merge-in columns 'INLRT' 'STATUS_ID' and 'INUSEBY'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, null, M01_Common.DdlOutputMode.edomValue |  (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomValue |  (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SYSIBM.SYSDUMMY1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0 = 1");
} else {
if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}
if (condenseData) {
// virtually merge-in columns 'INLRT' and 'STATUS_ID'
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_dbtEnumId + "(" + String.valueOf(M86_SetProductive.statusProductive) + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, tabQualifier + ".", null, null, null, null, null, null, null, null, null, null, null);
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " " + tabQualifier);

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");

if (M03_Config.usePsFltrByDpMappingForRegularViews &  usePsDpMappingForArchiveViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "(ARC." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
} else {
M22_Class_Utilities.printSectionHeader("View 'merging' productive and archive data / table \"" + qualTabNameArch + "\" (" + entityTypeDescr + " \"" + sectionName + "." + dbObjName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");

// FIXME: include this in the gen...ForEntity-routines!!!
if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conWorkingState, M01_ACM.conWorkingState, M24_Attribute_Utilities.AttrValueType.eavtEnum, M21_Enum.getEnumIndexByName(M01_ACM.dxnWorkingState, M01_ACM.dnWorkingState, null), acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

M04_Utilities.printConditional(fileNo, M04_Utilities.genAttrDeclByDomain(M01_ACM.conInUseBy, M01_ACM.cosnInUseBy, M24_Attribute_Utilities.AttrValueType.eavtDomain, M01_Globals.g_domainIndexInUseBy, acmEntityType, acmEntityIndex, null, null, ddlType, null, M01_Common.DdlOutputMode.edomListLrt, M01_Common.AttrCategory.eacRegular, null, 1, true, null), null, null);
}

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 1, forGen, null, (isUserTransactional ? M01_Common.DdlOutputMode.edomListLrt : M01_Common.DdlOutputMode.edomListNonLrt) |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 1, false, forGen, (isUserTransactional ? M01_Common.DdlOutputMode.edomListLrt : M01_Common.DdlOutputMode.edomListNonLrt) |  M01_Common.DdlOutputMode.edomListVirtual | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomListExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "ARC.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, "ARC", null, null);
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch + " ARC");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");

if (M03_Config.usePsFltrByDpMappingForRegularViews &  usePsDpMappingForArchiveViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(ARC." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(ARC." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(ARC." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "UNION ALL");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (isUserTransactional) {
if (!(forGen & ! forNl)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(" + String.valueOf(M11_LRT.workingStateUnlocked) + " AS " + M01_Globals.g_dbtEnumId + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CAST(NULL AS " + M01_Globals.g_dbtOid + "),");
}

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "PROD.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeTransformationContext(transformation, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, "PROD", null, null);
if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, false, null, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  (isUserTransactional ? M01_Common.DdlOutputMode.edomValueLrt : 0) | M01_Common.DdlOutputMode.edomValueVirtual | M01_Common.DdlOutputMode.edomVirtualPersisted | (M03_Config.includeTermStringsInMqt ? M01_Common.DdlOutputMode.edomValueExpression : M01_Common.DdlOutputMode.edomNone) | (expandExpressionsInFtoView ? M01_Common.DdlOutputMode.edomExpressionDummy : M01_Common.DdlOutputMode.edomNone) | M01_Common.DdlOutputMode.edomLrtPriv, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");

if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");

if (M03_Config.usePsFltrByDpMappingForRegularViews &  usePsDpMappingForArchiveViews) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '0')");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(PROD." + M01_Globals_IVK.g_anPsOid + " IN (SELECT PSOID FROM " + M01_Globals_IVK.g_qualTabNamePsDpMapping + "))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PROD." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(PROD." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

}

if (ddlType == M01_Common.DdlTypeId.edtPdm) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, null, null, forGen, null, null, forNl, null, null, null, null);
M22_Class.genAliasDdl(sectionIndex, dbObjName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen & ! forNl, false, false, false, false, "Archive-View \"" + sectionName + "." + dbObjName + "\"", null, null, null, null, true, null, null, null);
}
}

if (!(isSubjectToArchiving)) {
return;
}

// ####################################################################################################################
// #    SP for Archiving data for individual Entity
// ####################################################################################################################

String qualProcNameArchiveEntity;
qualProcNameArchiveEntity = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnArchiveOrg, null, null, null, null);

if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
if (isChangeLogWorkingPoolSpecialHandling) {
qualProcNameArchiveEntity = qualProcNameArchiveEntity + ;
} else {
qualProcNameArchiveEntity = qualProcNameArchiveEntity + ;
}
}

M22_Class_Utilities.printSectionHeader("SP for Archiving data for " + entityTypeDescr + " '" + sectionName + "." + dbObjName + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveEntity);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "only data with validity ending before this date is archived");
M11_LRT.genProcParm(fileNo, "IN", "archUserId_in", M01_Globals.g_dbtUserId, true, "archived records are tagged with this user as '" + M01_ACM.conUpdateUser + "'");
M11_LRT.genProcParm(fileNo, "IN", "archTimeStamp_in", "TIMESTAMP", true, "timestamp used for logging archived records");
if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
M11_LRT.genProcParm(fileNo, "IN", "reduceCl_in", "INTEGER", true, "reduce ChangeLog-records if and only if this parameter is '1'");
}
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", false, "number of rows being archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 0");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M07_SpLogging.genSpLogDecl(fileNo, -1, true);
M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveEntity, ddlType, null, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);

if (thisOrgIndex == M01_Globals.g_primaryOrgIndex |  acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
if (!())) {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
}

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_orgOid", "BIGINT", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL", null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

if (!())) {
M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M16_Archive.genDdlForTempToBeArchived(fileNo, 1, null, 1, null);
} else {
M16_Archive.genDdlForTempToBeArchived(fileNo, 1, 1, 1, null);
M16_Archive.genDdlForTempPsDates(fileNo, 1, 1, 1, null, 1);
}
} else {
M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);
M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M16_Archive.tempPsDates + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");
M16_Archive.genDdlForTempPsDates(fileNo, 1, null, null, null, null);
}

}


M11_LRT.genProcSectionHeader(fileNo, "initialize output variables", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

if (acmEntityIndex == M01_Globals.g_classIndexChangeLog &  forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_orgOid = (SELECT ORGOID FROM " + M01_Globals.g_qualTabNamePdmOrganization + " WHERE SEQUENCESCHEMANAME = '" + thisMetSchema + "');");
M11_LRT.genProcSectionHeader(fileNo, "generate ToBeArchive records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF reduceCl_in = 1 THEN");
M11_LRT.genProcSectionHeader(fileNo, "get last GenWS creation date", 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "MERGE INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempPsDates + " T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "USING (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals_IVK.g_anPsOid + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "MAX(" + M01_Globals.g_anCreateTimestamp + ") AS genWsProd");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameSolverData);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SDOORG_OID = v_orgOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ACCESSMODE_ID = " + M01_Globals_IVK.g_productiveDataPoolIndex);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FILENAME = 'root.inf'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GROUP BY " + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ") S");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "T.psOid = S." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "UPDATE SET T.genWsProd = S.genWsProd");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHEN NOT MATCHED THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "INSERT (psOid, genWsProd) VALUES (S." + M01_Globals_IVK.g_anPsOid + ", S.genWsProd)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ELSE IGNORE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
if (!() &  thisOrgIndex == M01_Globals.g_primaryOrgIndex)) {
M11_LRT.genProcSectionHeader(fileNo, "get last WDÜ creation date", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR orgLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SCHEMANAME AS c_schemaName");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M01_Globals.g_qualTabNamePdmPrimarySchema);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "POOLTYPE_ID = " + M01_Globals_IVK.g_productiveDataPoolIndex);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORGANIZATION_ID > 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "ORGANIZATION_ID ASC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt =               'MERGE INTO ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   '" + M16_Archive.tempPsDates + " T ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'USING (SELECT ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   '" + M01_Globals_IVK.g_anPsOid + ", ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   'LASTCENTRALDATATRANSFERCOMMIT as fto ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'FROM ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   c_schemaName || '.GENERALSETTINGS ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || ') S ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'ON ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   'T.psOid = S." + M01_Globals_IVK.g_anPsOid + " ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'WHEN MATCHED AND COALESCE(T.fto, '" + M01_LDM_IVK.gc_valDateInfinite + "') > S.fto THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   'UPDATE SET T.fto = S.fto ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'WHEN NOT MATCHED THEN ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt ||   'INSERT (psOid, fto) VALUES (S." + M01_Globals_IVK.g_anPsOid + ", S.fto) ';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET v_stmntTxt = v_stmntTxt || 'ELSE IGNORE';");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXECUTE IMMEDIATE v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END FOR;");
M00_FileWriter.printToFile(fileNo, "");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " CLOG");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " CLOG");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " DEL");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " DEL");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "DEL.OPERATION_ID = 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "CLOG.OPERATION_ID < 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "DEL.OBJECTID = CLOG.OBJECTID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + M16_Archive.tempToBeArchived + " TBA WHERE CLOG." + M01_Globals.g_anOid + " = TBA.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
if (isChangeLogWorkingPoolSpecialHandling) {
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(2); "COMMIT;"
//Print #fileNo,
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " CLOG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPERATION_ID = 3");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 YEAR)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + M16_Archive.tempToBeArchived + " TBA WHERE CLOG." + M01_Globals.g_anOid + " = TBA.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(2); "COMMIT;"
//Print #fileNo,
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " CLOG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.DBCOLUMNNAME = 'STATUS_ID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 YEAR)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + M16_Archive.tempToBeArchived + " TBA WHERE CLOG." + M01_Globals.g_anOid + " = TBA.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, "");
//Print #fileNo, addTab(2); "COMMIT;"
//Print #fileNo,
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " CLOG");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " CLOG");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOG.DBCOLUMNNAME LIKE 'S1CT%OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOG.DBCOLUMNNAME LIKE 'S0CS%OID'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND ");
if (!())) {
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (SELECT COALESCE(CASE WHEN genWsProd < COALESCE(fto," + M01_LDM_IVK.gc_valDateInfinite + ") THEN genWsProd ELSE fto END, " + M01_LDM_IVK.gc_valDateEarliest + ") FROM " + M16_Archive.tempPsDates + " D WHERE CLOG." + M01_Globals_IVK.g_anPsOid + " = D.psOid)");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (SELECT COALESCE(genWsProd, " + M01_LDM_IVK.gc_valDateEarliest + ") FROM " + M16_Archive.tempPsDates + " D WHERE CLOG." + M01_Globals_IVK.g_anPsOid + " = D.psOid)");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (SELECT COALESCE(genWsProd, " + M01_LDM_IVK.gc_valDateEarliest + ") FROM " + M16_Archive.tempPsDates + " D WHERE CLOG." + M01_Globals_IVK.g_anPsOid + " = D.psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 MONTH)");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + M16_Archive.tempToBeArchived + " TBA WHERE CLOG.OID = TBA.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
if (!())) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " CLOG");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG.DBCOLUMNNAME = 'LASTUPDATETIMESTAMP'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOG.OPTIMESTAMP < (SELECT COALESCE(fto, " + M01_LDM_IVK.gc_valDateEarliest + ") FROM " + M16_Archive.tempPsDates + " D WHERE CLOG." + M01_Globals_IVK.g_anPsOid + " = D.psOid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "CLOG.OPTIMESTAMP < (archTimeStamp_in - 90 DAY)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NOT EXISTS (SELECT 1 FROM " + M16_Archive.tempToBeArchived + " TBA WHERE CLOG.OID = TBA.oid)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
}
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempToBeArchived);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CLOG." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " CLOG");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdParWork + " CLOG");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameAspectArch + " GA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GA." + M01_Globals.g_anOid + " = CLOG.AHOBJECTID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "GA." + M01_Globals_IVK.g_anPsOid + " = CLOG." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (!(refTsColumnName.compareTo("") == 0)) {
if (isUserTransactional) {
M11_LRT.genProcSectionHeader(fileNo, "verify that no records are locked", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");

switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexTypeSpec: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameAspectProd + " NSR");
break;
}default: {if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualAggHeadTabNameProd + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProdPar + " PRODPAR,");
}
}
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
//onlyfactory
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameProd + " PROD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameWork + " WORK");
}
}}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AH." + refTsColumnName + " < refDate_in");
} else {
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {//Special Case CalculationRun - Archive only if all TypeSpecs are archived
M16_Archive.genDdlForCalculationRunCheckTypeSpec(fileNo, 3, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD");
M16_Archive.genDdlForWorkProdJoinWithPs(fileNo, 3);
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {//Special Case TypeSpec - Archive only if referenced NSR1 is archived
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 3, "WORK", refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {//Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 3, "WORK", refTsColumnName);
break;
}default: {if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PROD." + attrNameFkEntity + " = " + " PRODPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DATE(PRODPAR." + refTsColumnName + ") < refDate_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
}
M16_Archive.genDdlForWorkProdJoinWithPs(fileNo, 3);
}}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anInLrt + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameArchiveEntity, ddlType, 2, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("archRecordLocked", fileNo, 2, qualTabNameProd, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");
}

if (!())) {
M11_LRT.genProcSectionHeader(fileNo, "generate archive log records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArchiveLog);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ARCHIVETIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DBTABLENAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OBJECTID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "archTimeStamp_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + baseArchTabName + "',");
if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBA.oid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempToBeArchived + " TBA");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempToBeArchived + " TBA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TBA.oid = PROD.CLG_OID");
}
} else {
if (acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolLineEntry |  acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolParameter | acmEntityIndex == M01_Globals_IVK.g_classIndexTypeSpec | acmEntityIndex == M01_Globals_IVK.g_classIndexTypeStandardEquipment) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WORK." + M01_Globals.g_anOid);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals.g_anOid);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");

if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualAggHeadTabNameProd + " AH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
} else {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProdPar + " PRODPAR,");
}
}

switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexTypeSpec: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolLineEntryWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolParameterWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolLineEntryWork + " PLE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexGenericAspect: {if (!((ahClassIndex > 0 & ! isAggHead))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
}
}
break;
}default: {if (ahClassIndex > 0 & ! isAggHead) {
// handled above
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
}
}}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "AH." + refTsColumnName + " < refDate_in");
} else {
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {//Special Case CalculationRun - Archive only if all TypeSpecs are archived
M16_Archive.genDdlForCalculationRunCheckTypeSpec(fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD");
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {//Special Case TypeSpec - Archive only if referenced NSR1 is archived
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 2, "WORK", refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {//Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 2, "WORK", refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {//Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForProtocolLineEntryCheckTypeSpecNsr(fileNo, 2, refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {//Special Case ProtocolParameter - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForProtocolParameterCheckPleTypeSpecNsr(fileNo, 2, refTsColumnName);
break;
}default: {if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + attrNameFkEntity + " = " + " PRODPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(PRODPAR." + refTsColumnName + ") < refDate_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
}
}}
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


if ((acmEntityIndex == M01_Globals_IVK.g_classIndexGenericAspect & ! forNl & thisOrgIndex != M01_Globals.g_primaryOrgIndex)) {
M11_LRT.genProcSectionHeader(fileNo, "verify that no records to be updated are locked", null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "DATE(WORK." + refTsColumnName + ") >= refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK." + M01_Globals.g_anInLrt + " IS NOT NULL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WORK.CCPCCP_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "DATE(PROD." + refTsColumnName + ") < refDate_in)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ") THEN");

M07_SpLogging.genSpLogProcEscape(fileNo, qualProcNameArchiveEntity, ddlType, 2, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);
M79_Err.genSignalDdl("archRecordLocked", fileNo, 2, qualTabNameWork, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

M11_LRT.genProcSectionHeader(fileNo, "update references to aspects to be archived in work", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CCPCCP_OID = NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VERSIONID = VERSIONID + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATEUSER = archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LASTUPDATETIMESTAMP = archTimeStamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(" + refTsColumnName + ") >= refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CCPCCP_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");


M11_LRT.genProcSectionHeader(fileNo, "update references to aspects to be archived in prod", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET ");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CCPCCP_OID = NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "VERSIONID = VERSIONID + 1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "UPDATEUSER = archUserId_in,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LASTUPDATETIMESTAMP = archTimeStamp_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(" + refTsColumnName + ") >= refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CCPCCP_OID IN (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}


M11_LRT.genProcSectionHeader(fileNo, "copy records to archive data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

if (forNl) {
M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

if (acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolLineEntry |  acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolParameter) {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "WORK.", null, null, null, null, null, null, null, null, null, null, null);
} else {
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "PROD.", null, null, null, null, null, null, null, null, null, null, null);
}

if (forNl) {
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, null, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, forGen, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);
} else {
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, null, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");

switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexTypeSpec: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameProd + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolLineEntryWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolParameterWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProtocolLineEntryWork + " PLE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
break;
}case M01_Globals_IVK.g_classIndexGenericAspect: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
if (!((ahClassIndex > 0 & ! isAggHead))) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
}
break;
}default: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
}}

if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M16_Archive.tempToBeArchived + " TBA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "ON");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + M01_Globals.g_anOid + " = TBA.oid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PROD." + attrNameFkEntity + " = TBA.oid");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualAggHeadTabNameProd + " AH");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + refTsColumnName + " < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {//Special Case CalculationRun - Archive only if all TypeSpecs are archived
M16_Archive.genDdlForCalculationRunCheckTypeSpec(fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD");
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {//Special Case TypeSpec - Archive only if referenced NSR1 is archived
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 2, "PROD", refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {//Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 2, "PROD", refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {//Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForProtocolLineEntryCheckTypeSpecNsr(fileNo, 2, refTsColumnName);
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {//Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
M16_Archive.genDdlForProtocolParameterCheckPleTypeSpecNsr(fileNo, 2, refTsColumnName);
break;
}default: {if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(PRODPAR." + refTsColumnName + ") < refDate_in");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
}
}}
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");


M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

if (!((acmEntityIndex == M01_Globals.g_classIndexChangeLog))) {
M11_LRT.genProcSectionHeader(fileNo, "delete records in work data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");

if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualAggHeadTabNameProd + " AH");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + refTsColumnName + " < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {M16_Archive.genDdlForCalculationRunCheckTypeSpec(fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "WORK");
//Print #fileNo, addTab(2); "NOT EXISTS ("
// Print #fileNo, addTab(3); "SELECT"
// Print #fileNo, addTab(4); "1"
// Print #fileNo, addTab(3); "FROM"
// Print #fileNo, addTab(4); qualTabNameTypeSpecNameWork; " TYPS"
// Print #fileNo, addTab(3); "WHERE"
// Print #fileNo, addTab(4); "WORK."; g_anOid; " = TYPS.CRTCAR_OID"
// Print #fileNo, addTab(5); "AND"
// Print #fileNo, addTab(4); "WORK."; g_anPsOid; " = TYPS."; g_anPsOid
// Print #fileNo, addTab(2); ")"
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {//Special Case TypeSpec - Archive only if referenced NSR1 is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "PROD", refTsColumnName);
M16_Archive.genDdlForWorkProdJoinWithPs(fileNo, 4);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {//Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameProd + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 4, "PROD", refTsColumnName);
M16_Archive.genDdlForWorkProdJoinWithPs(fileNo, 4);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolLineEntry: {//Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForProtocolLineEntryCheckTypeSpecNsr(fileNo, 4, refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexProtocolParameter: {//Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProtocolLineEntryWork + " PLE,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForProtocolParameterCheckPleTypeSpecNsr(fileNo, 4, refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}case M01_Globals_IVK.g_classIndexGenericAspect: {M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = WORK." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
break;
}default: {if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProdPar + " PRODPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + attrNameFkEntity + " = " + " PRODPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PRODPAR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = WORK." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

if (!((acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolLineEntry |  acmEntityIndex == M01_Globals_IVK.g_classIndexProtocolParameter))) {
M11_LRT.genProcSectionHeader(fileNo, "delete records in productive data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");

if (acmEntityIndex == M01_Globals.g_classIndexChangeLog) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempToBeArchived + " TBA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = TBA.oid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + attrNameFkEntity + " = TBA.oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualAggHeadTabNameProd + " AH");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = AH." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AH." + refTsColumnName + " < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
switch (acmEntityIndex) {
case M01_Globals_IVK.g_classIndexCalculationRun: {//Special Case CalculationRun - Archive only if all TypeSpecs in Work and Prod are archived
M16_Archive.genDdlForCalculationRunCheckTypeSpec(fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD");
break;
}case M01_Globals_IVK.g_classIndexTypeSpec: {//Special Case TypeSpec - Archive only if referenced NSR1 is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "PROD", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "copy records to archive data pool for work data pool only records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M24_Attribute_Utilities.initAttributeTransformation(transformation, 1, null, null, null, "WORK.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, "TSTTPA_OID", "NULL", null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, null, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 2, "WORK", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNo, "delete records in work data pool for work data pool only records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeSpecCheckNsr(fileNo, 4, "WORK", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete TypeSpec to TypePriceAssignment in work data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " TS_WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TS_WORK.TSTTPA_OID = null");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = TS_WORK." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = TS_WORK.TSTTPA_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD.CLASSID = '09032'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete TypeSpec_Lrt to TypePriceAssignment in work data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + "_LRT TS_WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TS_WORK.TSTTPA_OID = null");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = TS_WORK." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = TS_WORK.TSTTPA_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD.CLASSID = '09032'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "delete TypeSpec to TypePriceAssignment in prod data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameProd + " TS_PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "TS_PROD.TSTTPA_OID = null");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " PROD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = TS_PROD." + M01_Globals_IVK.g_anPsOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals.g_anOid + " = TS_PROD.TSTTPA_OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD.CLASSID = '09032'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

break;
}case M01_Globals_IVK.g_classIndexTypeStandardEquipment: {//Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameProd + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 4, "PROD", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "copy records to archive data pool for work data pool only records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameArch);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");
M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, "WORK.", null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, 2, null, null, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 2, "WORK", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M11_LRT.genProcSectionHeader(fileNo, "count the number of affected rows", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "GET DIAGNOSTICS rowCount_out = ROW_COUNT;");

M11_LRT.genProcSectionHeader(fileNo, "delete records in work data pool for work data pool only records", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameTypeSpecNameWork + " TYPS,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameAspectProd + " NSR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M16_Archive.genDdlForTypeStandardEquipmentCheckTypeSpecNsr(fileNo, 4, "WORK", refTsColumnName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");

break;
}default: {if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + qualTabNameProdPar + " PRODPAR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + attrNameFkEntity + " = " + " PRODPAR." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "DATE(PRODPAR." + refTsColumnName + ") < refDate_in");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "DATE(PROD." + refTsColumnName + ") < refDate_in");
if (thisOrgIndex == M01_Globals.g_primaryOrgIndex) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempPsDates + " FTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD." + M01_Globals_IVK.g_anPsOid + " = FTO.psOid");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}
}
}}
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}
} else {
//Special Case ChangeLog Work
M11_LRT.genProcSectionHeader(fileNo, "delete records in work data pool", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabNameWork + " WORK");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "EXISTS (");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + M16_Archive.tempToBeArchived + " TBA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "WHERE");
if (!())) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + M01_Globals.g_anOid + " = TBA.oid");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "WORK." + attrNameFkEntity + " = TBA.oid");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
}

}

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveEntity, ddlType, null, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out", null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    SP for Estimating Volume of Archive Data for individual Entity
// ####################################################################################################################

if (!())) {
String qualProcNameArchiveEntityEstimate;
qualProcNameArchiveEntityEstimate = M04_Utilities.genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, M01_Globals_IVK.g_productiveDataPoolIndex, forGen, null, null, forNl, M01_ACM_IVK.spnArchiveOrgEstimate, null, null, null, null);

M22_Class_Utilities.printSectionHeader("SP for Estimating Volume of Archive Data for " + entityTypeDescr + " '" + sectionName + "." + dbObjName + "'", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE PROCEDURE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualProcNameArchiveEntityEstimate);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M11_LRT.genProcParm(fileNo, "IN", "refDate_in", "DATE", true, "count only data with validity ending before this date");
M11_LRT.genProcParm(fileNo, "OUT", "rowCount_out", "BIGINT", false, "number of rows in Productive Data Pool ready to be archived");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "RESULT SETS 1");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "LANGUAGE SQL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN");

M11_LRT.genProcSectionHeader(fileNo, "declare variables", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_tgtVarName", "VARCHAR(10)", "NULL", null, null);
M11_LRT.genVarDecl(fileNo, "v_sizeFactor", "INTEGER", "1", null, null);
M11_LRT.genVarDecl(fileNo, "v_rowCount", "BIGINT", "NULL", null, null);
M07_SpLogging.genSpLogDecl(fileNo, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare conditions", null, true);
M11_LRT.genCondDecl(fileNo, "alreadyExist", "42710", null);

M11_LRT.genProcSectionHeader(fileNo, "declare statement", null, null);
M11_LRT.genVarDecl(fileNo, "v_stmnt", "STATEMENT", null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "declare cursor", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE cntCursor CURSOR FOR v_stmnt;");

M11_LRT.genProcSectionHeader(fileNo, "declare condition handler", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE CONTINUE HANDLER FOR alreadyExist");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "BEGIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "-- just ignore (" + M82_PSCopy.tempOidMapTabName + " already exists)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END;");

M16_Archive.genDdlForTempArchiveStats(fileNo, null, false, true, null);

M07_SpLogging.genSpLogProcEnter(fileNo, qualProcNameArchiveEntityEstimate, ddlType, null, "#refDate_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M11_LRT.genProcSectionHeader(fileNo, "initialize output parameter", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET rowCount_out = 0;");

M11_LRT.genProcSectionHeader(fileNo, "examine each involved table", null, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FOR tabLoop AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(PARCH." + M01_Globals.g_anPdmFkSchemaName + ") AS c_ARCHTABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(P." + M01_Globals.g_anPdmFkSchemaName + ") AS c_TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "RTRIM(P." + M01_Globals.g_anPdmTableName + ") AS c_TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " AS c_POOLTYPE_ID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameAcmEntity + " A");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " L");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = L." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = L." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " P");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkSchemaName + " = L." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPdmLdmFkTableName + " = L." + M01_Globals.g_anLdmTableName);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNameLdmTable + " LARCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = LARCH." + M01_Globals.g_anAcmEntityName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LARCH." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = LARCH." + M01_Globals.g_anAcmEntityType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = LARCH." + M01_Globals.g_anLdmIsGen);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = LARCH." + M01_Globals.g_anLdmIsNl);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsLrt + " = LARCH." + M01_Globals.g_anLdmIsLrt + "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt + " = LARCH." + M01_Globals_IVK.g_anLdmIsMqt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "LEFT OUTER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.g_qualTabNamePdmTable + " PARCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARCH." + M01_Globals.g_anPdmLdmFkSchemaName + " = LARCH." + M01_Globals.g_anLdmSchemaName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARCH." + M01_Globals.g_anPdmLdmFkTableName + " = LARCH." + M01_Globals.g_anLdmTableName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARCH." + M01_Globals.g_anOrganizationId + " = P." + M01_Globals.g_anOrganizationId);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "PARCH." + M01_Globals.g_anPoolTypeId + " = " + String.valueOf(M01_Globals_IVK.g_archiveDataPoolId));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " = " + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId));

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntitySection + " = '" + sectionName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityName + " = '" + acmEntityName.toUpperCase() + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "A." + M01_Globals.g_anAcmEntityType + " = '" + M04_Utilities.getAcmEntityTypeKey(acmEntityType) + "'");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsGen + " = " + (forGen ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals.g_anLdmIsNl + " = " + (forNl ? M01_LDM.gc_dbTrue : M01_LDM.gc_dbFalse));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(L." + M01_Globals.g_anLdmIsLrt + " = 0 OR L." + M01_Globals_IVK.g_anLdmIsMqt + " = 1)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anOrganizationId + " = " + M04_Utilities.genOrgId(thisOrgIndex, ddlType, true));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + " IN (" + String.valueOf(M01_Globals.g_workDataPoolId) + ", " + String.valueOf(M01_Globals_IVK.g_productiveDataPoolId) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "ORDER BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "P." + M01_Globals.g_anPoolTypeId + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "L." + M01_Globals_IVK.g_anLdmIsMqt);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FOR READ ONLY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DO");
M11_LRT.genProcSectionHeader(fileNo, "determine number of records in this table to be archived", 2, true);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_tgtVarName = (CASE WHEN c_" + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType) + " THEN 'PROD' ELSE 'TGT' END);");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "SET v_stmntTxt =");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'COUNT(*) ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "c_TABSCHEMA || '.' || c_TABNAME || ' ' || v_tgtVarName || ' ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WHERE ' ||");

if (ahClassIndex > 0 & ! isAggHead) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(CASE WHEN c_" + M01_Globals.g_anPoolTypeId + " = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType) + " THEN '' ELSE '" + qualTabNameProd + " PROD,' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'" + qualAggHeadTabNameProd + " AH ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "(CASE WHEN c_POOLTYPE_ID = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType) + " THEN '' ELSE 'PROD." + M01_Globals.g_anOid + " = TGT." + M01_Globals.g_anOid + " AND ' END) ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'PROD." + M01_Globals.g_anAhOid + " = AH." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'AND ' ||");
if (!(hasOwnTable)) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "v_tgtVarName || '." + M01_Globals.g_anAhCid + " = AH." + M01_Globals.g_anCid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'AND ' ||");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'AH." + refTsColumnName + " < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "') ' ||");
} else {
if (forNl) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'" + qualTabNameProdPar + " PRODPAR ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "v_tgtVarName || '." + attrNameFkEntity + " = PRODPAR." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'DATE(PRODPAR." + refTsColumnName + ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "') ' ||");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "CASE WHEN c_POOLTYPE_ID = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType));
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "THEN 'DATE(PROD." + refTsColumnName + ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''') '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "ELSE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "'EXISTS (' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'SELECT ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "'1 ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'FROM ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "'" + qualTabNameProd + " PROD ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(8) + "'WHERE ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "'PROD." + M01_Globals.g_anOid + " = ' || v_tgtVarName || '." + M01_Globals.g_anOid + " ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(10) + "'AND ' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(9) + "'DATE(PROD." + refTsColumnName + ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(7) + "') '");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(6) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(5) + ") ||");
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "'WITH UR';");
M00_FileWriter.printToFile(fileNo, "");

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "PREPARE v_stmnt FROM v_stmntTxt;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OPEN cntCursor;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "FETCH");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "cntCursor");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_rowCount");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CLOSE cntCursor;");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_POOLTYPE_ID = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType) + " THEN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "SET rowCount_out = v_rowCount;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");

int indent;
boolean forArchivePool;
indent = 0;
int j;
for (int j = 1; j <= 2; j++) {
forArchivePool = (j == 2);
if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "IF c_POOLTYPE_ID = " + M04_Utilities.genPoolId(M01_Globals_IVK.g_productiveDataPoolIndex, ddlType) + " THEN");
indent = 1;
}
M11_LRT.genProcSectionHeader(fileNo, "create statistics / estimate record for this table in " + (forArchivePool ? "archive" : "this") + " pool", indent + 2, forArchivePool);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M16_Archive.tempArchiveTabStatsTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "poolId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "card,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "size,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "avgRowLen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "cardArch,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "sizeArch");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id) + ",");
if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + String.valueOf(M01_Globals_IVK.g_archiveDataPoolId) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_ARCHTABSCHEMA,30)) AS " + M01_Globals.g_dbtDbSchemaName + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "c_POOLTYPE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_TABSCHEMA,30))     AS " + M01_Globals.g_dbtDbSchemaName + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_TABNAME,  50))     AS VARCHAR(50)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.CARD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.CARD * (SUM(C.AVGCOLLEN) + 10),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SUM(C.AVGCOLLEN) + 10,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "v_rowCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + (forArchivePool ? "" : "-1 * ") + "(v_rowCount * (SUM(C.AVGCOLLEN) + 10))");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "C.TABSCHEMA = c_ARCHTABSCHEMA");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "C.TABSCHEMA = c_TABSCHEMA");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "C.TABNAME = T.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABSCHEMA = c_TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABNAME = c_TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.CARD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WITH UR;");

M11_LRT.genProcSectionHeader(fileNo, "create statistics / estimate record for indexes of this table in " + (forArchivePool ? "archive" : "this") + " pool", indent + 2, null);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + M16_Archive.tempArchiveIndStatsTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "orgId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "poolId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "tabSchema,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "tabName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "indName,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "card,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "size,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "avgKeyLen,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "cardArch,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "sizeArch");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "SELECT");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + String.valueOf(M71_Org.g_orgs.descriptors[thisOrgIndex].id) + ",");
if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + String.valueOf(M01_Globals_IVK.g_archiveDataPoolId) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_ARCHTABSCHEMA,30)) AS " + M01_Globals.g_dbtDbSchemaName + "),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "c_POOLTYPE_ID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_TABSCHEMA,30))     AS " + M01_Globals.g_dbtDbSchemaName + "),");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(c_TABNAME,  50))     AS VARCHAR(50)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "CAST(RTRIM(LEFT(I.INDNAME,  20))     AS VARCHAR(20)),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.CARD,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "(T.CARD * (SUM(C.AVGCOLLEN) + 9)) * 2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SUM(C.AVGCOLLEN) + 9,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "v_rowCount,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + (forArchivePool ? "" : "-") + "((v_rowCount * (SUM(C.AVGCOLLEN) + 9)) * 2)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.TABLES T");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.INDEXES I");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABSCHEMA = I.TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABNAME = I.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.INDEXCOLUSE IC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.INDSCHEMA = IC.INDSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.INDNAME = IC.INDNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "INNER JOIN");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "SYSCAT.COLUMNS C");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "ON");
if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.TABSCHEMA = c_ARCHTABSCHEMA");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.TABSCHEMA = c_TABSCHEMA");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.TABNAME = C.TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "IC.COLNAME = C.COLNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "C.TABSCHEMA = c_TABSCHEMA");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 4) + "AND");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABNAME = c_TABNAME");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "GROUP BY");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABSCHEMA,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.TABNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "I.INDNAME,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 3) + "T.CARD");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(indent + 2) + "WITH UR;");

if (forArchivePool) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "END IF;");
}
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END FOR;");

M07_SpLogging.genSpLogProcExit(fileNo, qualProcNameArchiveEntityEstimate, ddlType, null, "#refDate_in", "rowCount_out", null, null, null, null, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}

public static void genArchiveSupportDdlForClass(int classIndex,  int thisOrgIndex, int archPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

boolean hasNlTab;
boolean nlTabIsPurelyPrivate;

//On Error GoTo ErrorExit 

hasNlTab = (forGen &  M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses) |  (!(forGen &  (M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInNonGenInclSubClasses |  (M22_Class.g_classes.descriptors[classIndex].aggHeadClassIndex == M22_Class.g_classes.descriptors[classIndex].classIndex &  M22_Class.g_classes.descriptors[classIndex].implicitelyGenChangeComment & !M22_Class.g_classes.descriptors[classIndex].condenseData))));
nlTabIsPurelyPrivate = hasNlTab & ! (forGen &  M22_Class.g_classes.descriptors[classIndex].hasNlAttrsInGenInclSubClasses) & !(!(forGen &  )));

M16_Archive.genArchiveSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, null, null, null);
if (classIndex == M01_Globals.g_classIndexChangeLog) {

M16_Archive.genArchiveSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, null, null, true);
}
M16_Archive.genArchiveOrgPurgeDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, null, null);
if (hasNlTab) {
M16_Archive.genArchiveSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, true, nlTabIsPurelyPrivate, null);
if (classIndex == M01_Globals.g_classIndexChangeLog) {

M16_Archive.genArchiveSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, true, nlTabIsPurelyPrivate, true);
}
M16_Archive.genArchiveOrgPurgeDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, forGen, true, nlTabIsPurelyPrivate);
}

NormalExit:
//On Error Resume Next 
return;

ErrorExit:
errMsgBox(Err.description);
Resume(NormalExit);
}


public static void genArchiveSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex, int archPoolIndex, int fileNo, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

M16_Archive.genArchiveSupportDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, null, null, null, null);
M16_Archive.genArchiveOrgPurgeDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, null, null, null);

if (M23_Relationship.g_relationships.descriptors[thisRelIndex].nlAttrRefs.numDescriptors > 0) {
M16_Archive.genArchiveSupportDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, null, true, null, null);
M16_Archive.genArchiveOrgPurgeDdlForEntity(M23_Relationship.g_relationships.descriptors[thisRelIndex].relIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, M01_Globals_IVK.g_archiveDataPoolIndex, fileNo, ddlType, null, true, null);
}
}
// ### ENDIF IVK ###









}