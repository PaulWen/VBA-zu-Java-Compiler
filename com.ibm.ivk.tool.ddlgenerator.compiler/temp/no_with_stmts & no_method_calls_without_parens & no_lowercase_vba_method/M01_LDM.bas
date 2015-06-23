 Attribute VB_Name = "M01_LDM"
 Option Explicit
 
 Global Const gc_dbMaxAttributeNameLength = 30
 Global Const gc_dbMaxBufferPoolNameLength = 16
 Global Const gc_dbMaxTablespaceNameLength = 16
 Global Const gc_dbMaxSignalMessageLength = 1000
 Global Const gc_dbMaxBigInt = "9223372036854775807"
 
 
 Global Const gc_sqlMaxParmNameLength = 5
 Global Const gc_sqlMaxVarNameLength = 25
 Global Const gc_sqlMaxVarTypeLength = 15
 
 Global Const gc_sequenceMinValue = "00000000000000000"
 Global Const gc_sequenceStartValue = "00000002000000000"
 Global Const gc_sequenceEndValue = "99999999999999999"
 Global Const gc_sequenceIncrementValue = 4
 
 Global Const gc_dbObjSuffixLrt = "LRT"
 Global Const gc_dbObjSuffixShortLrt = "L"
 Global Const gc_dbObjSuffixMqt = "MQT"
 Global Const gc_dbObjSuffixShortMqt = "M"
 Global Const gc_dbObjSuffixGen = "GEN"
 Global Const gc_dbObjSuffixShortGen = "G"
 Global Const gc_dbObjSuffixNl = "NL"
 Global Const gc_dbObjSuffixShortNl = "N"
 
 Global Const gc_dbTrue = "1"
 Global Const gc_dbFalse = "0"
 
 Global Const tabPrefixNl = "NL"
 
 Global Const gc_db2RegVarLrtOid = "CURRENT CLIENT_WRKSTNNAME"
 Global Const gc_db2RegVarCtrl = "CURRENT CLIENT_ACCTNG"
 Global Const gc_db2RegVarSchema = "CURRENT SCHEMA"
 Global Const gc_db2RegVarLrtOidSafeSyntax = "'0' || " & gc_db2RegVarLrtOid
 
 Global Const gc_sqlCmdDelim = "@"
 Global Const gc_sqlDelimLine1 = "########################################################################################"
 Global Const gc_sqlDelimLine2 = "----------------------------------------------------------------------------------------"
 
 Global Const gc_seqNameIndexMetricsId = "IndexMetricsId"
 Global Const gc_seqNameSnapshotId = "snapshotId"
 Global Const gc_seqNameOid = "OidSequence"
 
