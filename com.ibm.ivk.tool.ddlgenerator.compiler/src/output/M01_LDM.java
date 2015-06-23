package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_LDM {




public static final int gc_dbMaxAttributeNameLength = 30;
public static final int gc_dbMaxBufferPoolNameLength = 16;
public static final int gc_dbMaxTablespaceNameLength = 16;
public static final int gc_dbMaxSignalMessageLength = 1000;
public static final String gc_dbMaxBigInt = "9223372036854775807";


public static final int gc_sqlMaxParmNameLength = 5;
public static final int gc_sqlMaxVarNameLength = 25;
public static final int gc_sqlMaxVarTypeLength = 15;

public static final String gc_sequenceMinValue = "00000000000000000";
public static final String gc_sequenceStartValue = "00000002000000000";
public static final String gc_sequenceEndValue = "99999999999999999";
public static final int gc_sequenceIncrementValue = 4;

public static final String gc_dbObjSuffixLrt = "LRT";
public static final String gc_dbObjSuffixShortLrt = "L";
public static final String gc_dbObjSuffixMqt = "MQT";
public static final String gc_dbObjSuffixShortMqt = "M";
public static final String gc_dbObjSuffixGen = "GEN";
public static final String gc_dbObjSuffixShortGen = "G";
public static final String gc_dbObjSuffixNl = "NL";
public static final String gc_dbObjSuffixShortNl = "N";

public static final String gc_dbTrue = "1";
public static final String gc_dbFalse = "0";

public static final String tabPrefixNl = "NL";

public static final String gc_db2RegVarLrtOid = "CURRENT CLIENT_WRKSTNNAME";
public static final String gc_db2RegVarCtrl = "CURRENT CLIENT_ACCTNG";
public static final String gc_db2RegVarSchema = "CURRENT SCHEMA";
public static final String gc_db2RegVarLrtOidSafeSyntax = "'0' || " + M01_LDM.gc_db2RegVarLrtOid;

public static final String gc_sqlCmdDelim = "@";
public static final String gc_sqlDelimLine1 = "########################################################################################";
public static final String gc_sqlDelimLine2 = "----------------------------------------------------------------------------------------";

public static final String gc_seqNameIndexMetricsId = "IndexMetricsId";
public static final String gc_seqNameSnapshotId = "snapshotId";
public static final String gc_seqNameOid = "OidSequence";


}