package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M18_LogChange {




private static final int processingStep = 1;

private static final boolean generateLogChangeTrigger = true;

private static void genInsertChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase();
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr.compareTo("") == 0)) {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
} else {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
if (entityName.compareTo("AGGREGATIONNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.AVDDIV_OID,");
} else if (entityName.compareTo("ENDNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.EVDDIV_OID,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genInsertRelChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
int leftEntityIndex;
String leftFkColName;
String ahClassId;

entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName.toUpperCase();
classId = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
leftEntityIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex;
leftFkColName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftFkColName[1];

ahClassId = M22_Class.g_classes.descriptors[leftEntityIndex].classIdStr;

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'R',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD." + leftFkColName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genUpdateChangeLogBroadcastCall(int fileNo, int acmEntityIndex, M24_Attribute_Utilities.EntityColumnDescriptor colDesc,  int thisOrgIndex, boolean nlText, boolean distNlText, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;
String shortName;
String columnName;
int dataType;
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean isNullable;

if (nlText & ! distNlText) {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() + "_NL_TEXT";
} else {
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase();
}
shortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr.compareTo("") == 0)) {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
} else {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
}

columnName = colDesc.columnName;
dataType = M25_Domain.g_domains.descriptors[colDesc.dbDomainIndex].dataType;
isNullable = colDesc.isNullable;

if (distNlText) {
// map classId / ahClassId to parent
// if approach becomes generic concept this hardcoding should be replaced
if (classId.compareTo("11025") == 0) {
classId = "11022";
ahClassId = "11022";
} else if (classId.compareTo("11026") == 0) {
classId = "11023";
ahClassId = "11023";
} else if (classId.compareTo("11027") == 0) {
classId = "11024";
ahClassId = "11024";
}
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, "");
if (isNullable |  (nlText & ! distNlText)) {
if (dataType == 1 |  dataType == 2 | dataType == 3 | dataType == 17) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(NEWRECORD." + columnName + ", -1) <> COALESCE(OLDRECORD." + columnName + ", -1) THEN");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF COALESCE(NEWRECORD." + columnName + ", '') <> COALESCE(OLDRECORD." + columnName + ", '') THEN");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "IF NEWRECORD." + columnName + " <> OLDRECORD." + columnName + " THEN");
}
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 2);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
if (distNlText) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, true) + ",");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + ahClassId + "',");
if (nlText) {
if (distNlText) {
if (entityName.compareTo("GROUP_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.GNLGRP_OID,");
} else if (entityName.compareTo("AGGREGATIONNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.ANLANO_OID,");
} else if (entityName.compareTo("ENDNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.ENLENO_OID,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.OID,");
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD." + shortName + "_OID,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "0,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "'" + columnName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD.OID,");
if (dataType == 6) {
//dataType String
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "4,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD." + columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEWRECORD." + columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
} else if (dataType == 1 |  dataType == 2 | dataType == 17) {
//dataType Enum or Integer or Boolean
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD." + columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEWRECORD." + columnName + ",");
} else if (dataType == 3) {
//dataType BigInteger
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "OLDRECORD." + columnName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEWRECORD." + columnName + ",");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_cdUserId,");
if (entityName.compareTo("AGGREGATIONNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEWRECORD.AVDDIV_OID,");
} else if (entityName.compareTo("ENDNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NEWRECORD.EVDDIV_OID,");
} else if (entityName.compareTo("AGGREGATIONNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = NEWRECORD." + shortName + "_OID),");
} else if (entityName.compareTo("ENDNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = NEWRECORD." + shortName + "_OID),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "2,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ");");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "END IF;");

}

private static void genInsertDistNlChangeLogBroadcastCall(int fileNo, int acmEntityIndex,  int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase();
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;

// map classId / ahClassId to parent
// if approach becomes generic concept this hardcoding should be replaced
if (classId.compareTo("11025") == 0) {
classId = "11022";
ahClassId = "11022";
} else if (classId.compareTo("11026") == 0) {
classId = "11023";
ahClassId = "11023";
} else if (classId.compareTo("11027") == 0) {
classId = "11024";
ahClassId = "11024";
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, true) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
if (entityName.compareTo("GROUP_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.GNLGRP_OID,");
} else if (entityName.compareTo("AGGREGATIONNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.ANLANO_OID,");
} else if (entityName.compareTo("ENDNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.ENLENO_OID,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genInsertNlChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;
String shortName;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() + "_NL_TEXT";
shortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr.compareTo("") == 0)) {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
} else {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD." + shortName + "_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NEWRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
//Special handling: EndNode/AggregationNode includes parent reference in insert changeLog
if (entityName.compareTo("AGGREGATIONNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = NEWRECORD." + shortName + "_OID),");
} else if (entityName.compareTo("ENDNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = NEWRECORD." + shortName + "_OID),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genDeleteChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase();
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr.compareTo("") == 0)) {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
} else {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
//Special handling: EndNode/AggregationNode includes parent reference in delete changeLog
if (entityName.compareTo("AGGREGATIONNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'ANPANO_OID',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(CASE WHEN OLDRECORD.ANPANO_OID IS NULL THEN NULL ELSE 6 END),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.ANPANO_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.AVDDIV_OID,");
} else if (entityName.compareTo("ENDNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'ENPANO_OID',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "6,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.ENPANO_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.EVDDIV_OID,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genDeleteRelChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
int leftEntityIndex;
String leftFkColName;
String ahClassId;

entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName.toUpperCase();
classId = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relIdStr;
leftEntityIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftEntityIndex;
leftFkColName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].leftFkColName[1];

ahClassId = M22_Class.g_classes.descriptors[leftEntityIndex].classIdStr;

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'R',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD." + leftFkColName + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "0,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}
private static void genDeleteDistNlChangeLogBroadcastCall(int fileNo, int acmEntityIndex,  int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase();
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;

// map classId / ahClassId to parent
// if approach becomes generic concept this hardcoding should be replaced
if (classId.compareTo("11025") == 0) {
classId = "11022";
ahClassId = "11022";
} else if (classId.compareTo("11026") == 0) {
classId = "11023";
ahClassId = "11023";
} else if (classId.compareTo("11027") == 0) {
classId = "11024";
ahClassId = "11024";
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, true) + ",");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
if (entityName.compareTo("GROUP_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.GNLGRP_OID,");
} else if (entityName.compareTo("AGGREGATIONNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.ANLANO_OID,");
} else if (entityName.compareTo("ENDNODE_DIST_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.ENLENO_OID,");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}

private static void genDeleteNlChangeLogBroadcastCall(int fileNo, int acmEntityIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

String entityName;
String classId;
String ahClassId;
String shortName;

entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className.toUpperCase() + "_NL_TEXT";
shortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
classId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
if (!(M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr.compareTo("") == 0)) {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].aggHeadClassIdStr;
} else {
ahClassId = M22_Class.g_classes.descriptors[acmEntityIndex].classIdStr;
}

String qualProcNameClBroadCast;
qualProcNameClBroadCast = M04_Utilities.genQualProcName(M01_Globals.g_sectionIndexChangeLog, M01_ACM_IVK.spnClBroadcast, ddlType, null, null, null, null, null, null);

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("create Changelog Records", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualProcNameClBroadCast);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + classId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'C',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + ahClassId + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD." + shortName + "_OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "1,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "'" + entityName + "',");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OLDRECORD.OID,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_cdUserId,");
//Special handling: EndNode/AggregationNode includes parent reference in delete changeLog
if (entityName.compareTo("AGGREGATIONNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = OLDRECORD." + shortName + "_OID),");
} else if (entityName.compareTo("ENDNODE_NL_TEXT") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = OLDRECORD." + shortName + "_OID),");
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
}
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "NULL,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "3,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "CURRENT TIMESTAMP,");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "v_numClRecords");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");

}



private static void genLogChangeSupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

String entityName;
String entityTypeDescr;
String entityShortName;
boolean isUserTransactional;
boolean isCommonToOrgs;
boolean isCommonToPools;
boolean entityInsertable;
boolean entityUpdatable;
boolean entityDeletable;
boolean isCtoAliasCreated;
int sectionIndex;
String sectionName;
boolean noAlias;
boolean useSurrogateKey;
// ### IF IVK ###
boolean isPsTagged;
boolean psTagOptional;
boolean ignorePsRegVarOnInsertDelete;
// ### ELSE ###
//
// entityInsertable = True
// entityUpdatable = True
// entityDeletable = True
// ### ENDIF IVK ###

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionIndex = M22_Class.g_classes.descriptors[acmEntityIndex].sectionIndex;
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M22_Class.g_classes.descriptors[acmEntityIndex].isCtoAliasCreated;
noAlias = M22_Class.g_classes.descriptors[acmEntityIndex].noAlias;
useSurrogateKey = M22_Class.g_classes.descriptors[acmEntityIndex].useSurrogateKey;
// ### IF IVK ###
isPsTagged = M22_Class.g_classes.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = M22_Class.g_classes.descriptors[acmEntityIndex].psTagOptional;
entityInsertable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmInsert);
entityUpdatable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmUpdate);
entityDeletable = (M22_Class.g_classes.descriptors[acmEntityIndex].updateMode &  M01_Common.DbUpdateMode.eupmDelete);
ignorePsRegVarOnInsertDelete = M22_Class.g_classes.descriptors[acmEntityIndex].ignPsRegVarOnInsDel;
// ### ENDIF IVK ###
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionIndex = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionIndex;
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;
isCommonToOrgs = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToOrgs;
isCommonToPools = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isCtoAliasCreated = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCtoAliasCreated;
noAlias = M23_Relationship.g_relationships.descriptors[acmEntityIndex].noAlias;
useSurrogateKey = true;// ???? FIXME
// ### IF IVK ###
isPsTagged = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isPsTagged;
psTagOptional = false;
entityInsertable = true;
entityUpdatable = true;
entityDeletable = true;
ignorePsRegVarOnInsertDelete = false;
// ### ENDIF IVK ###
}

// ### IF IVK ###
boolean supportTriggerForPsTag;
supportTriggerForPsTag = true;

boolean poolSupportPsTaggingTrigger;
poolSupportPsTaggingTrigger = true;
// ### ENDIF IVK ###

boolean M72_DataPool.poolSupportLrt;
returnValue = false;

if (ddlType == M01_Common.DdlTypeId.edtPdm &  thisPoolIndex > 0) {
returnValue = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportLrt;
// ### IF IVK ###
supportTriggerForPsTag = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportViewsForPsTag;
poolSupportPsTaggingTrigger = M72_DataPool.g_pools.descriptors[thisPoolIndex].supportTriggerForPsTag;
// ### ENDIF IVK ###
}

// ### IF IVK ###
if (isPsTagged) {
// included in PS-tagging-views
return;
}

// ### ENDIF IVK ###
if (isUserTransactional &  M01_Globals.g_genLrtSupport) {
// included in LRT-views
return;
}

M24_Attribute_Utilities.AttributeListTransformation transformation;
String qualViewName;
String qualNlViewName;
String qualViewNameLdm;
String qualNlViewNameLdm;

String qualTabName;
String qualNlTabName;
qualTabName = M04_Utilities.genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, null, null);

String tabQualifier;
tabQualifier = entityShortName.toUpperCase();

if (M03_Config.generateLogChangeView) {
qualViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, null, null, "LC", null, null);

M22_Class_Utilities.printSectionHeader("View supporting <logChange>-columns for table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, "(");

// ### IF IVK ###
M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomListVirtual, null);
// ### ELSE IVK ###
//   genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListNonLrt
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, ")");
M00_FileWriter.printToFile(fileNo, "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, tabQualifier + ".", null, null, null, null, null, null, null, null, null, null, null);
// ### IF IVK ###
M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueVirtual, null);
// ### ELSE IVK ###
//   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt
// ### ENDIF IVK ###

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName + " " + tabQualifier);

// ### IF IVK ###
if (isPsTagged) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + M01_Globals_IVK.gc_db2RegVarPsOid + " = '')");
if (psTagOptional) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " IS NULL)");
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(4) + "OR");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + "(" + tabQualifier + "." + M01_Globals_IVK.g_anPsOid + " = " + M01_Globals_IVK.g_activePsOidDdl + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + ")");
}

// ### ENDIF IVK ###
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! noAlias) {
qualViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "LC", null, null);
// ### IF IVK ###
M22_Class.genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, false, false, false, false, "<logChange>-View \"" + sectionName + "." + entityName + "\"", null, null, false, null, null, true, null, null);
// ### ELSE IVK ###
//     genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
//                 qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, False, _
//                 "<logChange>-View """ & sectionName & "." & entityName & """", , , True
// ### ENDIF IVK ###
}

if (qualTabName.compareTo("VL6CMET.GROUP") == 0 |  qualTabName.compareTo("VL6CMET.AGGREGATIONNODE") == 0 | qualTabName.compareTo("VL6CMET.ENDNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- VIEW for NL Entity here - " + qualTabName);
qualNlTabName = qualTabName + "_NL_TEXT";

qualNlViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, true, null, null, null, null);

M22_Class_Utilities.printSectionHeader("View as base for triggers for table \"" + qualNlTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, "CREATE VIEW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlViewName);
M00_FileWriter.printToFile(fileNo, "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, ")");
M00_FileWriter.printToFile(fileNo, "AS");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "(");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SELECT");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, tabQualifier + ".", null, null, null, null, null, null, null, null, null, null, null);
// genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , _
// False, forGen, edomAll
M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomValueVirtual, null, null, null, null, null, null);
// genNlsTransformedAttrListForEntity g_classIndexLrt, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , _
// edomListNonLrt

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabName + " " + tabQualifier);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + ")");

M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

//initAttributeTransformation transformation, 0
//transformation.doCollectVirtualAttrDescriptors = True
//transformation.doCollectAttrDescriptors = True
//setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName

if (ddlType == M01_Common.DdlTypeId.edtPdm & ! noAlias) {
qualNlViewNameLdm = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, M01_Common.DdlTypeId.edtLdm, thisOrgIndex, thisPoolIndex, forGen, null, null, true, "", "NL_TEXT", null, null);
M22_Class.genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, true, qualViewNameLdm, qualNlViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, M01_Common.DbAliasEntityType.edatView, forGen, false, false, false, false, " NL-View \"" + sectionName + "." + entityName + "_NL_TEXT\"", "NL_TEXT", null, false, null, null, true, null, null);
}

}


}

if (generateLogChangeTrigger) {
String qualTriggerName;
String qualNlTriggerName;
boolean broadcastChanges;
boolean broadcastForDist;
boolean broadcastForNl;
boolean broadcastForRel;
boolean hasDistTable;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "LC_INS", null, null);

M22_Class_Utilities.printSectionHeader("Insert-Trigger supporting <logChange>-columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityInsertable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("insertNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {

if (qualTabName.compareTo("VL6CMET.GROUP") == 0 |  qualTabName.compareTo("VL6CMET.GROUPVALIDFORORGANIZATION") == 0 | qualTabName.compareTo("VL6CMET.GROUP_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE") == 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.ENDNODE") == 0 | qualTabName.compareTo("VL6CMET.ENDNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0) {
broadcastChanges = true;
broadcastForDist = false;
broadcastForNl = false;
broadcastForRel = false;
if (M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 |  M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 != 0) {
broadcastForDist = true;
} else if (qualTabName.compareTo("VL6CMET.GROUPVALIDFORORGANIZATION") == 0) {
broadcastForRel = true;
} else {
broadcastForNl = true;
}
} else {
broadcastChanges = false;
broadcastForDist = false;
}

if (broadcastChanges) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
}

if (broadcastChanges) {
if (broadcastForDist) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Distributed Entity Insert");
genInsertDistNlChangeLogBroadcastCall(fileNo, acmEntityIndex, thisOrgIndex, ddlType);
} else if (broadcastForRel) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Rel Entity Insert");
genInsertRelChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Base Entity Insert");
genInsertChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
}
}

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

if (broadcastChanges &  broadcastForNl) {
qualNlTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, "_INS", null, null);
qualNlViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, true, null, "", null, null);
//qualNlViewName = genQualNlTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True)
qualNlTabName = qualTabName + "_NL_TEXT";

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Insert");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "INSERT INTO");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "VALUES");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ");");
M00_FileWriter.printToFile(fileNo, "");
genInsertNlChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "LC_UPD", null, null);

M22_Class_Utilities.printSectionHeader("Update-Trigger supporting <logChange>-columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityUpdatable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("updateNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {
if (qualTabName.compareTo("VL6CMET.GROUP") == 0 |  qualTabName.compareTo("VL6CMET.GROUP_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE") == 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.ENDNODE") == 0 | qualTabName.compareTo("VL6CMET.ENDNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0) {
broadcastChanges = true;
broadcastForDist = false;
broadcastForNl = false;
broadcastForRel = false;
if (M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 |  M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 != 0) {
broadcastForDist = true;
} else if (qualTabName.compareTo("VL6CMET.GROUPVALIDFORORGANIZATION") == 0) {
broadcastForRel = true;
} else {
broadcastForNl = true;
}
} else {
broadcastChanges = false;
broadcastForDist = false;
}

if (broadcastChanges) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, null, false, forGen, M01_Common.DdlOutputMode.edomListNonLrt, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (broadcastChanges) {
M24_Attribute_Utilities.EntityColumnDescriptors tabColumns;
boolean ignoreLastUpdateTimestamp;

boolean filterByClassId;
boolean ignoreForChangelog;
int thisAttributeIndex;
int orSuperClassIndex;
String attrAppearsInClassIdStr;
String lastAttrAppearsInClassIdStr;
boolean closingEndIfOutStanding;
boolean hasColumnToFilter;

if (broadcastForDist) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Distributed Entity Update here");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
hasColumnToFilter = false;
closingEndIfOutStanding = false;

// generate change log records for changed regular attributes
ignoreLastUpdateTimestamp = true;
M24_Attribute_Utilities.initAttributeTransformation(transformation, (ignoreLastUpdateTimestamp ? 4 : 3), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);
if (ignoreLastUpdateTimestamp) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
}

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
thisAttributeIndex = 1;
attrAppearsInClassIdStr = "";
lastAttrAppearsInClassIdStr = "";

while (thisAttributeIndex <= tabColumns.numDescriptors) {
//Print #fileNo, addTab(1); "-- Column:"; .columnName
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[thisAttributeIndex].columnCategory, false)) {
filterByClassId = false;
ignoreForChangelog = false;

if (tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex].ignoreForChangelog) {
ignoreForChangelog = true;
}
}

if (!(ignoreForChangelog)) {
genUpdateChangeLogBroadcastCall(fileNo, acmEntityIndex, tabColumns.descriptors[thisAttributeIndex], thisOrgIndex, true, true, ddlType);
}
}

thisAttributeIndex = thisAttributeIndex + 1;
}
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Base Entity Update");
M00_FileWriter.printToFile(fileNo, "");
M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");

hasColumnToFilter = false;
closingEndIfOutStanding = false;

// generate change log records for changed regular attributes
ignoreLastUpdateTimestamp = true;
M24_Attribute_Utilities.initAttributeTransformation(transformation, (ignoreLastUpdateTimestamp ? 4 : 3), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);
if (ignoreLastUpdateTimestamp) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
}

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 0, false, forGen, M01_Common.DdlOutputMode.edomNone, null);
thisAttributeIndex = 1;
attrAppearsInClassIdStr = "";
lastAttrAppearsInClassIdStr = "";

while (thisAttributeIndex <= tabColumns.numDescriptors) {
//Print #fileNo, addTab(1); "-- Column:"; .columnName
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[thisAttributeIndex].columnCategory, false)) {
filterByClassId = false;
ignoreForChangelog = false;

if (tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex].ignoreForChangelog) {
ignoreForChangelog = true;
}
}

if (!(ignoreForChangelog)) {
genUpdateChangeLogBroadcastCall(fileNo, acmEntityIndex, tabColumns.descriptors[thisAttributeIndex], thisOrgIndex, false, false, ddlType);
}
}

thisAttributeIndex = thisAttributeIndex + 1;
}
}

}
}

M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (broadcastChanges &  broadcastForNl) {
qualNlTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, "_UPD", null, null);
qualNlViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, true, null, "", null, null);
qualNlTabName = qualTabName + "_NL_TEXT";

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Update");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "UPDATE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute.genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, false, M01_Common.DdlOutputMode.edomListNonLrt |  M01_Common.DdlOutputMode.edomMqtLrt | M01_Common.DdlOutputMode.edomListVirtual | M01_Common.DdlOutputMode.edomListExpression, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "=");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "(");

M24_Attribute_Utilities.initAttributeTransformation(transformation, 0, null, null, null, M01_Globals.gc_newRecordName + ".", null, null, null, null, null, null, null, null, null, null, null);

M24_Attribute.genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, null, false, ddlType, thisOrgIndex, thisPoolIndex, 2, null, null, null, M01_Common.DdlOutputMode.edomListNonLrt, null, null, null, null, null, null);

M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ")");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID = OLDRECORD.OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M22_Class_Utilities.printComment("determine User id", fileNo, null, 1);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS " + M01_Globals.g_dbtUserId + ");");
M00_FileWriter.printToFile(fileNo, "");
hasColumnToFilter = false;
closingEndIfOutStanding = false;
// generate change log records for changed regular attributes
ignoreLastUpdateTimestamp = true;
M24_Attribute_Utilities.initAttributeTransformation(transformation, (ignoreLastUpdateTimestamp ? 4 : 3), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 1, M01_ACM.conCreateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 2, M01_ACM.conUpdateUser, "", null, null, null);
M24_Attribute_Utilities.setAttributeMapping(transformation, 3, M01_ACM.conCreateTimestamp, "", null, null, null);
if (ignoreLastUpdateTimestamp) {
M24_Attribute_Utilities.setAttributeMapping(transformation, 4, M01_ACM.conLastUpdateTimestamp, "", null, null, null);
}

tabColumns = M24_Attribute_Utilities.nullEntityColumnDescriptors;
M24_Attribute.genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, null, null, ddlType, thisOrgIndex, thisPoolIndex, 0, false, false, null, M01_Common.DdlOutputMode.edomNone, null, null, null, null, null, null);
thisAttributeIndex = 1;
attrAppearsInClassIdStr = "";
lastAttrAppearsInClassIdStr = "";

while (thisAttributeIndex <= tabColumns.numDescriptors) {
if (M12_ChangeLog.isClAttrCat(tabColumns.descriptors[thisAttributeIndex].columnCategory, false)) {
filterByClassId = false;
ignoreForChangelog = false;

if (tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[tabColumns.descriptors[thisAttributeIndex].acmFkRelIndex].ignoreForChangelog) {
ignoreForChangelog = true;
}
}

if (!(ignoreForChangelog)) {
genUpdateChangeLogBroadcastCall(fileNo, acmEntityIndex, tabColumns.descriptors[thisAttributeIndex], thisOrgIndex, true, false, ddlType);
}
}

thisAttributeIndex = thisAttributeIndex + 1;
}
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);
M00_FileWriter.printToFile(fileNo, "");
}

// ####################################################################################################################
// #    DELETE Trigger
// ####################################################################################################################

qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "LC_DEL", null, null);

M22_Class_Utilities.printSectionHeader("Delete-Trigger supporting <logChange>-columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");

if (!(entityDeletable &  M03_Config.generateUpdatableCheckInUpdateTrigger)) {
M79_Err.genSignalDdl("deleteNotAllowed", fileNo, 1, entityName, null, null, null, null, null, null, null, null);
} else {
//When approach becomes a generic concept, replace hardcoded name checks by flags
if (qualTabName.compareTo("VL6CMET.GROUP") == 0 |  qualTabName.compareTo("VL6CMET.GROUPVALIDFORORGANIZATION") == 0 | qualTabName.compareTo("VL6CMET.GROUP_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE") == 0 | qualTabName.compareTo("VL6CMET.AGGREGATIONNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | qualTabName.compareTo("VL6CMET.ENDNODE") == 0 | qualTabName.compareTo("VL6CMET.ENDNODE_NL_TEXT") == 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0) {
broadcastChanges = true;
broadcastForDist = false;
broadcastForNl = false;
broadcastForRel = false;
if (M00_Helper.inStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 |  M00_Helper.inStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 | M00_Helper.inStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 != 0) {
broadcastForDist = true;
} else if (qualTabName.compareTo("VL6CMET.GROUPVALIDFORORGANIZATION") == 0) {
broadcastForRel = true;
} else {
broadcastForNl = true;
}
} else {
broadcastChanges = false;
broadcastForDist = false;
}

if (broadcastChanges) {
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");
if (qualTabName.compareTo("VL6CMET.GROUP") == 0 |  qualTabName.compareTo("VL6CMET.ENDNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CMET.DEL" + entityName.toUpperCase() + "DISTNLTEXT(OLDRECORD.OID);");
} else if (qualTabName.compareTo("VL6CMET.AGGREGATIONNODE") == 0) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "CALL VL6CMET.DELAGGNODEDISTNLTEXT(OLDRECORD.OID);");
}
}

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + M01_Globals.g_anOid + " = " + M01_Globals.gc_oldRecordName + "." + M01_Globals.g_anOid);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

if (broadcastChanges) {
if (broadcastForDist) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Distributed Entity Delete");
genDeleteDistNlChangeLogBroadcastCall(fileNo, acmEntityIndex, thisOrgIndex, ddlType);
} else if (broadcastForRel) {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Rel Entity Delete");
genDeleteRelChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
} else {
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- CHANGELOGBROADCAST for Base Entity Delete");
genDeleteChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
}
}

}

M00_FileWriter.printToFile(fileNo, "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

if (broadcastChanges &  broadcastForNl) {
qualNlTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, true, null, "_DEL", null, null);
qualNlViewName = M04_Utilities.genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, false, null, true, null, "", null, null);
//qualNlViewName = genQualNlTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True)
qualNlTabName = qualTabName + "_NL_TEXT";

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Delete");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "INSTEAD OF DELETE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualNlViewName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "OLD AS " + M01_Globals.gc_oldRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;");

M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "DELETE FROM");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + qualNlTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "WHERE");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(2) + "OID = " + M01_Globals.gc_oldRecordName + ".OID");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");

M00_FileWriter.printToFile(fileNo, "");
genDeleteNlChangeLogBroadcastCall(fileNo, acmEntityIndex, ddlType);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + M01_LDM.gc_sqlCmdDelim);

}

}
}


public static void genLogChangeSupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

genLogChangeSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen);
}


public static void genLogChangeSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW) {
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

genLogChangeSupportDdlForEntity(thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen);
}


private static void genLogChangeAutoMaintSupportDdlForEntity(int acmEntityIndex, Integer acmEntityType,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

String entityName;
String entityTypeDescr;
String entityShortName;
String sectionName;
boolean logLastChange;
boolean isCtp;
boolean isUserTransactional;
String qualTabName;

if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactClass) {
sectionName = M22_Class.g_classes.descriptors[acmEntityIndex].sectionName;
entityName = M22_Class.g_classes.descriptors[acmEntityIndex].className;
entityShortName = M22_Class.g_classes.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Class";
logLastChange = M22_Class.g_classes.descriptors[acmEntityIndex].logLastChange;
isCtp = M22_Class.g_classes.descriptors[acmEntityIndex].isCommonToPools;
isUserTransactional = M22_Class.g_classes.descriptors[acmEntityIndex].isUserTransactional;

qualTabName = M04_Utilities.genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null, null);
} else if (acmEntityType == M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship) {
sectionName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].sectionName;
entityName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].relName;
entityShortName = M23_Relationship.g_relationships.descriptors[acmEntityIndex].shortName;
entityTypeDescr = "ACM-Relationship";
logLastChange = M23_Relationship.g_relationships.descriptors[acmEntityIndex].logLastChange;
isCtp = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isCommonToPools;
isUserTransactional = M23_Relationship.g_relationships.descriptors[acmEntityIndex].isUserTransactional;

qualTabName = M04_Utilities.genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, null, null, null, null);
}

if (thisPoolIndex > 0 &  isCtp) {
return;
}

if (!(logLastChange)) {
return;
}

if (forLrt & ! isUserTransactional) {
return;
}

String qualTriggerName;

// ####################################################################################################################
// #    INSERT Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "_LCINS", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_INS")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Insert-Trigger for maintaining log-change-columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CASCADE BEFORE INSERT ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateUser + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateUser + ", RTRIM(LEFT(CURRENT USER, 16))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + ", CURRENT TIMESTAMP),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anUpdateUser + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anUpdateUser + ", RTRIM(LEFT(CURRENT USER, 16))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", CURRENT TIMESTAMP)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);

// ####################################################################################################################
// #    UPDATE Trigger
// ####################################################################################################################

// ### IF IVK ###
qualTriggerName = M04_Utilities.genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, null, null, null, null, "_LCUPD", null, null);
// ### ELSE IVK ###
// qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_UPD")
// ### ENDIF IVK ###

M22_Class_Utilities.printSectionHeader("Update-Trigger for maintaining log-change-columns in table \"" + qualTabName + "\" (" + entityTypeDescr + " \"" + sectionName + "." + entityName + "\")", fileNo, null, null);
M00_FileWriter.printToFile(fileNo, "");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "CREATE TRIGGER");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTriggerName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "NO CASCADE BEFORE UPDATE ON");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + qualTabName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "REFERENCING");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "NEW AS " + M01_Globals.gc_newRecordName);
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "FOR EACH ROW");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "BEGIN ATOMIC");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + "SET " + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateUser + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateUser + ", RTRIM(LEFT(CURRENT USER, 16))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anCreateTimestamp + ", CURRENT TIMESTAMP),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anUpdateUser + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anUpdateUser + ", RTRIM(LEFT(CURRENT USER, 16))),");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(3) + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + " = COALESCE(" + M01_Globals.gc_newRecordName + "." + M01_Globals.g_anLastUpdateTimestamp + ", CURRENT TIMESTAMP)");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(1) + ";");
M00_FileWriter.printToFile(fileNo, M04_Utilities.addTab(0) + "END");
M00_FileWriter.printToFile(fileNo, M01_LDM.gc_sqlCmdDelim);
}


public static void genLogChangeAutoMaintSupportDdlForClass(int classIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

genLogChangeAutoMaintSupportDdlForEntity(classIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt);
}


public static void genLogChangeAutoMaintSupportDdlForRelationship(int thisRelIndex,  int thisOrgIndex,  int thisPoolIndex, int fileNo, Integer ddlTypeW, Boolean forGenW, Boolean forLrtW) {
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

boolean forLrt; 
if (forLrtW == null) {
forLrt = false;
} else {
forLrt = forLrtW;
}

genLogChangeAutoMaintSupportDdlForEntity(thisRelIndex, M24_Attribute_Utilities.AcmAttrContainerType.eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt);
}




}