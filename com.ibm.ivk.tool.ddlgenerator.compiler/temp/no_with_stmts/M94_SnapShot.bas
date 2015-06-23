 Attribute VB_Name = "M94_SnapShot"
 Option Explicit
 
 Private Const processingStepAdmin = 4
 
 Private Const pc_tempTabNameSnRecords = "SESSION.Records"
 
 
 Sub genDbSnapshotDdl( _
   ddlType As DdlTypeId _
 )
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMonitor, processingStepAdmin, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   genDbSnapshotDdlUtilities fileNo, ddlType
   genDbSnapshotDdlGetSnapshot fileNo, ddlType
   genDbSnapshotDdlAdmin fileNo, ddlType
   genDbSnapshotDdlAnalysis fileNo, ddlType
   genDbEventMonitoringDdl fileNo, ddlType
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbSnapshotDdlUtilities( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim largeTables As Boolean
   largeTables = False
   If Left(snapshotApiVersion, 1) = "9" Then
     largeTables = True
   End If
 
   If generateDdlCreateSeq Then
     ' ####################################################################################################################
     ' #    Sequence for SNAPSHOT IDs
     '  ####################################################################################################################

     Dim qualSeqNameSnapShotId As String
     qualSeqNameSnapShotId = genQualSeqName(g_sectionIndexDbMonitor, gc_seqNameSnapshotId, ddlType)

     genProcSectionHeader fileNo, "create sequence for snapshot IDs"
     Print #fileNo, addTab(0); "CREATE SEQUENCE"
     Print #fileNo, addTab(1); qualSeqNameSnapShotId; " AS "; g_dbtSequence
     Print #fileNo, addTab(0); "START WITH"
     Print #fileNo, addTab(1); "1"
     Print #fileNo, addTab(0); "INCREMENT BY"
     Print #fileNo, addTab(1); "1"
     Print #fileNo, addTab(0); "NO CYCLE"
     Print #fileNo, addTab(0); "CACHE 500"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If

   ' ####################################################################################################################
   ' #    UDF for determining columns to be retrieved in snapshot table
   ' ####################################################################################################################

   Dim qualFuncNameSnCols As String
   qualFuncNameSnCols = genQualFuncName(g_sectionIndexDbMonitor, udfnSnapshotCols, ddlType)
 
   printSectionHeader "Function for determining columns to be retrieved in snapshot table", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameSnCols
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "tabName_in", g_dbtDbTableName, True, "name of the snapshot table to retrieve columns for"
   genProcParm fileNo, "", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "", "tabVariable_in", "VARCHAR(16)", False, "(optional) reference variable to use to qualify column"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(" & IIf(largeTables, "4096", "2048") & ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_colList", "VARCHAR(" & IIf(largeTables, "4096", "2048") & ")", "''"
   genVarDecl fileNo, "v_colPrefix", "VARCHAR(17)", "''"

   genProcSectionHeader fileNo, "loop over columns related to the given table"
   Print #fileNo, addTab(1); "FOR colLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "C.COLNAME,"
   Print #fileNo, addTab(3); "C.COLEXPRESSION,"
   Print #fileNo, addTab(3); "(CASE WHEN RTRIM(C.COLALIAS       ) = '' THEN CAST(NULL AS VARCHAR(1)) ELSE C.COLALIAS END       ) AS COLALIAS,"
   Print #fileNo, addTab(3); "(CASE WHEN RTRIM(C.DISPLAYFUNCNAME) = '' THEN CAST(NULL AS VARCHAR(1)) ELSE C.DISPLAYFUNCNAME END) AS DISPLAYFUNCNAME"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotCol; " C"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(UPPER(C.TABLENAME) = UPPER(tabName_in))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(category_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(C.CATEGORY, category_in) = category_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(level_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(C.LEVEL, level_in) >= level_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "C.SEQUENCENO"
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_colList ="
   Print #fileNo, addTab(3); "v_colList ||"
   Print #fileNo, addTab(3); "(CASE WHEN v_colList = '' THEN '' ELSE ',' END) ||"
   Print #fileNo, addTab(3); "(CASE WHEN DISPLAYFUNCNAME IS NULL THEN '' ELSE '"; getSchemaName(qualFuncNameSnCols); ".' || DISPLAYFUNCNAME || '(' END) ||"
   Print #fileNo, addTab(3); "COALESCE(COLEXPRESSION, COLNAME) ||"
   Print #fileNo, addTab(3); "(CASE WHEN DISPLAYFUNCNAME IS NULL THEN '' ELSE ')' END) ||"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN COLALIAS IS NULL"
   Print #fileNo, addTab(5); "THEN (CASE WHEN DISPLAYFUNCNAME IS NULL AND COLEXPRESSION IS NULL THEN '' ELSE ' AS ""' || COLNAME || '""' END)"
   Print #fileNo, addTab(5); "ELSE ' AS ""' || COLALIAS || '""'"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"
 
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_colList;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric application status to text (short)
   ' ####################################################################################################################

   Dim qualFuncNameApplStatus2StrS As String
   qualFuncNameApplStatus2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str & "_S", ddlType)
 
   printSectionHeader "Function for converting application status to text (short)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameApplStatus2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "applStatusNum_in", "BIGINT", False, "application status"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(3)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE applStatusNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'PIN' -- performing initialization"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'CPE' -- connect pending"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'CCO' -- connect completed"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'UEX' -- UOW executing"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'UWA' -- UOW waiting"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'LWT' -- lock wait"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'CAC' -- commit active"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'RAC' -- rollback active"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'RPL' -- recompiling plan"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'CSS' -- compiling SQL statement"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'RIN' -- request interrupted"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'DPE' -- disconnect pending"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'PTR' -- Prepared transaction"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'HCO' -- heuristically committed"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'HRB' -- heuristically rolled back"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'TEN' -- Transaction ended"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'CRD' -- Creating Database"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'RSD' -- Restarting Database"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'RDB' -- Restoring Database"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'PBK' -- Performing Backup"
   Print #fileNo, addTab(3); "WHEN 20 THEN 'PFL' -- Performing fast load"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'PFU' -- Performing fast unload"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'WDT' -- Wait to disable tablespace"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'QTS' -- Quiescing tablespace"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'WRN' -- Waiting for remote node"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'PRR' -- Pending results from remote request"
   Print #fileNo, addTab(3); "WHEN 26 THEN 'ADC' -- App has been decoupled from coord"
   Print #fileNo, addTab(3); "WHEN 27 THEN 'RSP' -- Rollback to savepoint"
   Print #fileNo, addTab(3); "ELSE         '???'"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric application status to text (long)
   ' ####################################################################################################################

   Dim qualFuncNameApplStatus2Str As String
   qualFuncNameApplStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str, ddlType)
 
   printSectionHeader "Function for converting application status to text (long)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameApplStatus2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "applStatusNum_in", "BIGINT", False, "application status"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(35)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE applStatusNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'performing initialization'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'connect pending'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'connect completed'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'UOW executing'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'UOW waiting'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'lock wait'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'commit active'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'rollback active'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'recompiling plan'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'compiling SQL statement'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'request interrupted'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'disconnect pending'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Prepared transaction'"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'heuristically committed'"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'heuristically rolled back'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'Transaction ended'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'Creating Database'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'Restarting a Database'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Restoring a Database'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'Performing a Backup'"
   Print #fileNo, addTab(3); "WHEN 20 THEN 'Performing a fast load'"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'Performing a fast unload'"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'Wait to disable tablespace'"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'Quiescing a tablespace'"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'Waiting for remote node'"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'Pending results from remote request'"
   Print #fileNo, addTab(3); "WHEN 26 THEN 'App has been decoupled from coord'"
   Print #fileNo, addTab(3); "WHEN 27 THEN 'Rollback to savepoint'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(applStatusNum_in AS CHAR(35)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric platform ID to text (short)
   ' ####################################################################################################################

   Dim qualFuncNamePlatform2Str As String
   qualFuncNamePlatform2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnPlatform2Str & "_S", ddlType)
 
   printSectionHeader "Function for converting numeric platform ID to text (short)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNamePlatform2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "platformId_in", "BIGINT", False, "platform ID"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(5)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE platformId_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'UNK'   -- Unknown platform"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'OS2'   -- OS/2"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'DOS'   -- DOS"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'WIN'   -- Windows"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'AIX'   -- AIX"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'NT'    -- NT"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'HP'    -- HP"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'SUN'   -- Sun"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'MVS'   -- MVS (client via DRDA)"
   Print #fileNo, addTab(3); "WHEN  9 THEN '400'   -- AS400 (client via DRDA)"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'VM'    -- VM (client via DRDA)"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'VSE'   -- VSE (client via DRDA)"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'UDRD'  -- Unknown DRDA Client"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'SNI'   -- Siemens Nixdorf"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'MacC'  -- Macintosh Client"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'W95'   -- Windows 95"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'SCO'   -- SCO"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'SIGR'  -- Silicon Graphic"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'LINUX' -- Linux"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'DYNIX' -- DYNIX/ptx"
   Print #fileNo, addTab(3); "WHEN 20 THEN 'AIX64' -- AIX 64 bit"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'SUN64' -- Sun 64 bit"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'HP64'  -- HP 64 bit"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'NT64'  -- NT 64 bit"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'L390'  -- Linux for S/390"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'L900'  -- Linux for z900"
   Print #fileNo, addTab(3); "WHEN 26 THEN 'LIA64' -- Linux for IA64"
   Print #fileNo, addTab(3); "WHEN 27 THEN 'LPPC'  -- Linux for PPC"
   Print #fileNo, addTab(3); "WHEN 28 THEN 'LPP64' -- Linux for PPC64"
   Print #fileNo, addTab(3); "WHEN 29 THEN 'OS390' -- OS/390 Tools (CC, DW)"
   Print #fileNo, addTab(3); "WHEN 30 THEN 'L8664' -- Linux for x86-64"
   Print #fileNo, addTab(3); "WHEN 31 THEN 'HPI32' -- HP-UX Itanium 32bit"
   Print #fileNo, addTab(3); "WHEN 32 THEN 'HPI64' -- HP-UX Itanium 64bit"
   Print #fileNo, addTab(3); "WHEN 33 THEN 'S8632' -- Sun x86 32bit"
   Print #fileNo, addTab(3); "WHEN 34 THEN 'S8664' -- Sun x86-64 64bit"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(platformId_in AS CHAR(5)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric platform ID to text (long)
   ' ####################################################################################################################

   qualFuncNamePlatform2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnPlatform2Str, ddlType)
 
   printSectionHeader "Function for converting numeric platform ID to text (long)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNamePlatform2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "platformId_in", "BIGINT", False, "platform ID"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(21)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE platformId_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'Unknown platform'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'OS/2'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'DOS'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'Windows'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'AIX'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'NT'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'HP'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'Sun'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'MVS (via DRDA)'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'AS400 (via DRDA)'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'VM (via DRDA)'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'VSE (via DRDA)'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Unknown DRDA Client'"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'Siemens Nixdorf'"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'Macintosh Client'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'Windows 95'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'SCO'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'Silicon Graphic'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Linux'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'DYNIX/ptx'"
   Print #fileNo, addTab(3); "WHEN 20 THEN 'AIX 64 bit'"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'Sun 64 bit'"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'HP 64 bit'"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'NT 64 bit'"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'Linux for S/390'"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'Linux for z900'"
   Print #fileNo, addTab(3); "WHEN 26 THEN 'Linux for IA64'"
   Print #fileNo, addTab(3); "WHEN 27 THEN 'Linux for PPC'"
   Print #fileNo, addTab(3); "WHEN 28 THEN 'Linux for PPC64'"
   Print #fileNo, addTab(3); "WHEN 29 THEN 'OS/390 Tools (CC, DW)'"
   Print #fileNo, addTab(3); "WHEN 30 THEN 'Linux for x86-64'"
   Print #fileNo, addTab(3); "WHEN 31 THEN 'HP-UX Itanium 32bit'"
   Print #fileNo, addTab(3); "WHEN 32 THEN 'HP-UX Itanium 64bit'"
   Print #fileNo, addTab(3); "WHEN 33 THEN 'Sun x86 32bit'"
   Print #fileNo, addTab(3); "WHEN 34 THEN 'Sun x86-64 64bit'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(platformId_in AS CHAR(21)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric protocol ID to text
   ' ####################################################################################################################

   Dim qualFuncNameProtocol2Str As String
   qualFuncNameProtocol2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnProtocol2Str, ddlType)
 
   printSectionHeader "Function for converting numeric protocol ID to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameProtocol2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "protocolId_in", "BIGINT", False, "platform ID"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(17)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE protocolId_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'APPC'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'NETBIOS'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'APPN'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'TCPIP'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'APPC using CPIC'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'IPX/SPX'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'Local IPC'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'Named Pipe'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'TCPIP using SOCKS'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'TCPIP using SSL'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(protocolId_in AS CHAR(17)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric database status to text
   ' ####################################################################################################################

   Dim qualFuncNameDbStatus2Str As String
   qualFuncNameDbStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnDbStatus2Str, ddlType)
 
   printSectionHeader "Function for converting numeric database status to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameDbStatus2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "dbStatus_in", "BIGINT", False, "DB status ID"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(15)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE dbStatus_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'active'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Quiesce pending'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'quiesced'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'rolling forward'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(dbStatus_in AS CHAR(15)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric database manager status to text
   ' ####################################################################################################################

   Dim qualFuncNameDbmStatus2Str As String
   qualFuncNameDbmStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnDbmStatus2Str, ddlType)
 
   printSectionHeader "Function for converting numeric database manager status to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameDbmStatus2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "dbmStatus_in", "BIGINT", False, "DB manager status ID"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(15)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE dbmStatus_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'active'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Quiesce pending'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'quiesced'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(dbmStatus_in AS CHAR(15)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric statement type to text (short)
   ' ####################################################################################################################

   Dim qualFuncNameStmntType2StrS As String
   qualFuncNameStmntType2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str & "_S", ddlType)

   printSectionHeader "Function for converting numeric statement type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameStmntType2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "statementTypeNum_in", "BIGINT", False, "numeric statement type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(7)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE statementTypeNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Static'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'Dynamic'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'non-SQL'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'unknown'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(statementTypeNum_in AS CHAR(7)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric statement type to text (long)
   ' ####################################################################################################################

   Dim qualFuncNameStmntType2Str As String
   qualFuncNameStmntType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str, ddlType)

   printSectionHeader "Function for converting numeric statement type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameStmntType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "statementTypeNum_in", "BIGINT", False, "numeric statement type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(17)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE statementTypeNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Static statement'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'Dynamic statement'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'other than SQL'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'unknown'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(statementTypeNum_in AS CHAR(17)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric statement operation to text (short)
   ' ####################################################################################################################

   Dim qualFuncNameStmntOperation2StrS As String
   qualFuncNameStmntOperation2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str & "_S", ddlType)

   printSectionHeader "Function for converting numeric statement operation to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameStmntOperation2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "statementOpNum_in", "BIGINT", False, "numeric statement operation"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(8)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE statementOpNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'SQL Pre'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'SQL Exe'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'SQL Imm'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'SQL Ope'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'SQL Ftc'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'SQL Clo'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'SQL Des'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'SQL Com'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'SQL Rbk'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'SQL Fre'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'Pre com'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Call SP'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'SELECT'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'Prep op'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'Prep ex'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Compile'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'SET'"
 
   Print #fileNo, addTab(3); "WHEN 20 THEN 'Runstats'"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'Reorg'"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'Rebind'"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'Redist'"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'GetTabAu'"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'GetAdmAu'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(statementOpNum_in AS CHAR(7)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric statement operation to text (long)
   ' ####################################################################################################################

   Dim qualFuncNameStmntOperation2Str As String
   qualFuncNameStmntOperation2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str, ddlType)

   printSectionHeader "Function for converting numeric statement operation to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameStmntOperation2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "statementOpNum_in", "BIGINT", False, "numeric statement operation"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(35)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE statementOpNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'SQL Prepare'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'SQL Execute'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'SQL Execute Immediate'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'SQL Open'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'SQL Fetch'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'SQL Close'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'SQL Describe'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'SQL Static Commit'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'SQL Static Rollback'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'SQL Free Locator'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'Prepare to commit (2-phase commit)'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Call a stored procedure'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'SELECT statement'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'Prep. and open (DB2 Connect only)'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'Prep. and execute (DB2 Connect)'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Compile (DB2 Connect only)'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'SET statement'"
 
   Print #fileNo, addTab(3); "WHEN 20 THEN 'Runstats'"
   Print #fileNo, addTab(3); "WHEN 21 THEN 'Reorg'"
   Print #fileNo, addTab(3); "WHEN 22 THEN 'Rebind package'"
   Print #fileNo, addTab(3); "WHEN 23 THEN 'Redistribute'"
   Print #fileNo, addTab(3); "WHEN 24 THEN 'Get Table Authorization'"
   Print #fileNo, addTab(3); "WHEN 25 THEN 'Get Administrative Authorization'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(statementOpNum_in AS CHAR(35)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric lock mode to text (short)
   ' ####################################################################################################################

   Dim qualFuncNameLockMode2StrS As String
   qualFuncNameLockMode2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockMode2Str & "_S", ddlType)
 
   printSectionHeader "Function for converting numeric lock mode to text (short)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockMode2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockModeNum_in", "BIGINT", False, "numeric lock mode"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(3)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockModeNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN ''    -- No Lock"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'IS'  -- Intention Share Lock"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'IX'  -- Intention Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'S'   -- Share Lock"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'SIX' -- Share with Intention Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'X'   -- Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'IN'  -- Intent None"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'Z'   -- Super Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'U'   -- Update Lock"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'NS'  -- Next Key Share Lock"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'NX'  -- Next Key Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'W'   -- Weak Exclusive Lock"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'NW'  -- Next Key Weak Exclusive Lock"
   Print #fileNo, addTab(3); "ELSE         '???'"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric lock mode to text (long)
   ' ####################################################################################################################

   Dim qualFuncNameLockMode2Str As String
   qualFuncNameLockMode2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockMode2Str, ddlType)
 
   printSectionHeader "Function for converting numeric lock mode to text (long)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockMode2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockModeNum_in", "BIGINT", False, "numeric lock mode"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(36)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockModeNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'No Lock'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Intention Share Lock'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'Intention Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'Share Lock'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'Share with Intention Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'Intent None'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'Super Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'Update Lock'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'Next Key Share Lock'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'Next Key Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'Weak Exclusive Lock'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Next Key Weak Exclusive Lock'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(lockModeNum_in AS CHAR(36)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric lock object type to text
   ' ####################################################################################################################

   Dim qualFuncNameLockObjType2Str As String
   qualFuncNameLockObjType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockObjType2Str, ddlType)
   Dim qualFuncNameLockObjType2StrS As String
   qualFuncNameLockObjType2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockObjType2StrS, ddlType)
 
   printSectionHeader "Function for converting numeric lock object type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockObjType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockObjTypeNum_in", "BIGINT", False, "numeric lock object type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(35)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockObjTypeNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Table Lock'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'Table Row Lock'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'Internal Lock'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'Tablespace Lock'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'End of Table'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'Key Value Lock'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'Internal Lock on the Sysboot Table'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'Internal Plan Lock'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'Internal Variation Lock'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'Internal Sequence Lock'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'Bufferpool Lock'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'Internal LONG/LOB Lock'"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'Internal Catalog Cache Lock'"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'Internal Online Backup Lock'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'Internal Object Table Lock'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'Internal Table Alter Lock'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'Internal DMS Sequence Lock'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Inplace Reorg Lock'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'Block Lock'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(lockObjTypeNum_in AS CHAR(35)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "Function for converting numeric lock object type to text (short)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockObjType2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockObjTypeNum_in", "BIGINT", False, "numeric lock object type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(13)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockObjTypeNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'Table'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'Row'"
   Print #fileNo, addTab(3); "WHEN  3 THEN 'Internal'"
   Print #fileNo, addTab(3); "WHEN  4 THEN 'TableSpace'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'End of Table'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'Key Value'"
   Print #fileNo, addTab(3); "WHEN  7 THEN 'IntSysboot'"
   Print #fileNo, addTab(3); "WHEN  8 THEN 'Plan'"
   Print #fileNo, addTab(3); "WHEN  9 THEN 'Variation'"
   Print #fileNo, addTab(3); "WHEN 10 THEN 'Sequence'"
   Print #fileNo, addTab(3); "WHEN 11 THEN 'Bufferpool'"
   Print #fileNo, addTab(3); "WHEN 12 THEN 'LONG/LOB'"
   Print #fileNo, addTab(3); "WHEN 13 THEN 'Catalog Cache'"
   Print #fileNo, addTab(3); "WHEN 14 THEN 'Online Backup'"
   Print #fileNo, addTab(3); "WHEN 15 THEN 'Object Table'"
   Print #fileNo, addTab(3); "WHEN 16 THEN 'Table Alter'"
   Print #fileNo, addTab(3); "WHEN 17 THEN 'DMS Sequence'"
   Print #fileNo, addTab(3); "WHEN 18 THEN 'Reorg'"
   Print #fileNo, addTab(3); "WHEN 19 THEN 'Block'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(lockObjTypeNum_in AS CHAR(13)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric lock status to text
   ' ####################################################################################################################

   Dim qualFuncNameLockStatus2Str As String
   Dim qualFuncNameLockStatus2StrS As String
   qualFuncNameLockStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockStatus2Str, ddlType)
   qualFuncNameLockStatus2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockStatus2Str & "_S", ddlType)
 
   printSectionHeader "Function for converting numeric lock status to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockStatus2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockStatusNum_in", "BIGINT", False, "numeric lock status"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(10)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockStatusNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'granted'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'converting'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(lockStatusNum_in AS CHAR(10)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "Function for converting numeric lock status to text (short)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameLockStatus2StrS
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "lockStatusNum_in", "BIGINT", False, "numeric lock status"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(3)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE lockStatusNum_in"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'GRA'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'CON'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(lockStatusNum_in AS CHAR(3)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric tablespace container type to text
   ' ####################################################################################################################

   Dim qualFuncNameContType2Str As String
   qualFuncNameContType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnContType2Str, ddlType)
 
   printSectionHeader "Function for converting numeric tablespace container type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameContType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "containerTypeNum_in", "BIGINT", False, "container type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(17)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE containerTypeNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'SMS'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'DMS device (page)'"
   Print #fileNo, addTab(3); "WHEN  2 THEN 'DMS file (page)'"
   Print #fileNo, addTab(3); "WHEN  5 THEN 'DMS device (ext)'"
   Print #fileNo, addTab(3); "WHEN  6 THEN 'DMS file (ext)'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(containerTypeNum_in AS CHAR(17)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric boolean to text
   ' ####################################################################################################################

   Dim qualFuncNameBoolean2Str As String
   qualFuncNameBoolean2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnBoolean2Str, ddlType)
 
   printSectionHeader "Function for converting numeric boolean to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameBoolean2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "booleanNum_in", "BIGINT", False, "numeric boolean value (0 or 1)"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(3)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE booleanNum_in"
   Print #fileNo, addTab(3); "WHEN  0 THEN 'no'"
   Print #fileNo, addTab(3); "WHEN  1 THEN 'yes'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(booleanNum_in AS CHAR(3)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric tablespace state to text
   ' ####################################################################################################################

   Dim qualFuncNameTsState2Str As String
   qualFuncNameTsState2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnTsState2Str, ddlType)
 
   printSectionHeader "Function for converting numeric tablespace state to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameTsState2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "tablespaceStateNum_in", "BIGINT", False, "numeric tablespace state"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(41)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE tableSpaceStateNum_in"
   Print #fileNo, addTab(3); "WHEN           0  THEN 'Normal'"
   Print #fileNo, addTab(3); "WHEN           1  THEN 'Quiesced (SHARE)'"
   Print #fileNo, addTab(3); "WHEN           2  THEN 'Quiesced (UPDATE)'"
   Print #fileNo, addTab(3); "WHEN           4  THEN 'Quiesced (EXCLUSIVE)'"
   Print #fileNo, addTab(3); "WHEN           8  THEN 'Load pending'"
   Print #fileNo, addTab(3); "WHEN          16  THEN 'Delete pending'"
   Print #fileNo, addTab(3); "WHEN          32  THEN 'Backup pending'"
   Print #fileNo, addTab(3); "WHEN          64  THEN 'Roll forward in progress'"
   Print #fileNo, addTab(3); "WHEN         128  THEN 'Roll forward pending'"
   Print #fileNo, addTab(3); "WHEN         256  THEN 'Restore pending'"
   Print #fileNo, addTab(3); "WHEN         256  THEN 'Recovery pending'"
   Print #fileNo, addTab(3); "WHEN         512  THEN 'Disable pending'"
   Print #fileNo, addTab(3); "WHEN        1024  THEN 'Reorg in progress'"
   Print #fileNo, addTab(3); "WHEN        2048  THEN 'Backup in progress'"
   Print #fileNo, addTab(3); "WHEN        4096  THEN 'Storage must be defined'"
   Print #fileNo, addTab(3); "WHEN        8192  THEN 'Restore in progress'"
   Print #fileNo, addTab(3); "WHEN       16384  THEN 'Offline and not accessible'"
   Print #fileNo, addTab(3); "WHEN       32768  THEN 'Drop pending'"
   Print #fileNo, addTab(3); "WHEN    33554432  THEN 'Storage may be defined'"
   Print #fileNo, addTab(3); "WHEN    67108864  THEN 'Storage Definition ''final'''"
   Print #fileNo, addTab(3); "WHEN   134217728  THEN 'Storage Def. changed prior to rollforward'"
   Print #fileNo, addTab(3); "WHEN   268435456  THEN 'DMS rebalancer active'"
   Print #fileNo, addTab(3); "WHEN   536870912  THEN 'TBS deletion in progress'"
   Print #fileNo, addTab(3); "WHEN  1073741824  THEN 'TBS creation in progress'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(tableSpaceStateNum_in AS CHAR(41)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric tablespace content type to text
   ' ####################################################################################################################

   Dim qualFuncNameTsContType2Str As String
   qualFuncNameTsContType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnTsContType2Str, ddlType)
 
   printSectionHeader "Function for converting numeric tablespace content type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameTsContType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "tsContentTypeNum_in", "BIGINT", False, "numeric tablespace content type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(10)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE tsContentTypeNum_in"
   Print #fileNo, addTab(3); "WHEN 0 THEN 'any'"
   Print #fileNo, addTab(3); "WHEN 1 THEN 'long'"
   Print #fileNo, addTab(3); "WHEN 2 THEN 'temp (sys)'"
   Print #fileNo, addTab(3); "WHEN 3 THEN 'temp (usr)'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(tsContentTypeNum_in AS CHAR(10)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric tablespace type to text
   ' ####################################################################################################################

   Dim qualFuncNameTsType2Str As String
   qualFuncNameTsType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnTsType2Str, ddlType)
 
   printSectionHeader "Function for converting numeric tablespace type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameTsType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "tableSpaceTypeNum_in", "BIGINT", False, "numeric tablespace type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(3)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE tableSpaceTypeNum_in"
   Print #fileNo, addTab(3); "WHEN 0 THEN 'DMS'"
   Print #fileNo, addTab(3); "WHEN 1 THEN 'SMS'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(tableSpaceTypeNum_in AS CHAR(3)))"
 
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF for converting numeric table type to text
   ' ####################################################################################################################

   Dim qualFuncNameTabType2Str As String
   qualFuncNameTabType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnTabType2Str, ddlType)
 
   printSectionHeader "Function for converting numeric table type to text", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameTabType2Str
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "tableTypeNum_in", "BIGINT", False, "numeric table type"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(10)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "RETURN"
 
   Print #fileNo, addTab(2); "CASE tableTypeNum_in"
   Print #fileNo, addTab(3); "WHEN 1 THEN 'user'"
   Print #fileNo, addTab(3); "WHEN 2 THEN 'dropped'"
   Print #fileNo, addTab(3); "WHEN 3 THEN 'temporary'"
   Print #fileNo, addTab(3); "WHEN 4 THEN 'system'"
   Print #fileNo, addTab(3); "WHEN 5 THEN 'reorg'"
   Print #fileNo, addTab(3); "ELSE RTRIM(CAST(tableTypeNum_in AS CHAR(10)))"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(1); ";"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genDdlForTempTablesSnapshotAnalysis( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   maxRecordLength As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = True, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for analysis records retrieved", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); pc_tempTabNameSnRecords
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "seqNo  INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),"
   Print #fileNo, addTab(indent + 1); "record VARCHAR("; CStr(maxRecordLength); ")"
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Private Sub genDbSnapshotDdlGetSnapshot( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
 ' ### IF IVK ###
   If Left(snapshotApiVersion, 1) = "8" Then
     genDbSnapshotDdlGetSnapshotV8 fileNo, ddlType
   ElseIf snapshotApiVersion = "9.7" Then
     genDbSnapshotDdlGetSnapshotV9_7 fileNo, ddlType
   End If
 ' ### ELSE IVK ###
 ' genDbSnapshotDdlGetSnapshotV9_7 fileNo, ddlType
 ' ### ENDIF IVK ###
 End Sub
 
 
 Private Sub genDbSnapshotDdlGetSnapshotV9_7( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim qualFuncNameSnCols As String
   qualFuncNameSnCols = genQualFuncName(g_sectionIndexDbMonitor, udfnSnapshotCols, ddlType)
   Dim qualSeqNameSnapShotId As String
   qualSeqNameSnapShotId = genQualSeqName(g_sectionIndexDbMonitor, gc_seqNameSnapshotId, ddlType)
 
   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database manager
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Dbm, vnSnapshotV9Dbm, vsnSnapshotV9Dbm, _
     clxnSnapshotV9Dbm, clnSnapshotV9Dbm, "database manager", "SNAP_GET_DBM_V95", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database manager memory pool
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9DbmMemoryPool, vnSnapshotV9DbmMemoryPool, vsnSnapshotV9DbmMemoryPool, _
     clxnSnapshotV9DbmMemoryPool, clnSnapshotV9DbmMemoryPool, "database manager memory pool", "SNAP_GET_DBM_MEMORY_POOL ", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Db, vnSnapshotV9Db, vsnSnapshotV9Db, _
     clxnSnapshotV9Db, clnSnapshotV9Db, "database", "SNAP_GET_DB_V97", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database memory pool
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9DbMemoryPool, vnSnapshotV9DbMemoryPool, vsnSnapshotV9DbMemoryPool, _
     clxnSnapshotV9DbMemoryPool, clnSnapshotV9DbMemoryPool, "database memory pool", "SNAP_GET_DB_MEMORY_POOL", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespaces
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9TbSp, vnSnapshotV9TbSp, vsnSnapshotV9TbSp, _
     clxnSnapshotV9TbSp, clnSnapshotV9TbSp, "table spaces", "SNAP_GET_TBSP_V91", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespace partitions
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9TbSpPart, vnSnapshotV9TbSpPart, vsnSnapshotV9TbSpPart, _
     clxnSnapshotV9TbSpPart, clnSnapshotV9TbSpPart, "table space partitions", "SNAP_GET_TBSP_PART_V97", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespace quiescer
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9TbSpQuiescer, vnSnapshotV9TbSpQuiescer, vsnSnapshotV9TbSpQuiescer, _
     clxnSnapshotV9TbSpQuiescer, clnSnapshotV9TbSpQuiescer, "table space quiescer", "SNAP_GET_TBSP_QUIESCER", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespace range
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9TbSpRange, vnSnapshotV9TbSpRange, vsnSnapshotV9TbSpRange, _
     clxnSnapshotV9TbSpRange, clnSnapshotV9TbSpRange, "table space range", "SNAP_GET_TBSP_RANGE", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on container
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Container, vnSnapshotV9Container, vsnSnapshotV9Container, _
     clxnSnapshotV9Container, clnSnapshotV9Container, "container", "SNAP_GET_CONTAINER_V91", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on bufferpools
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Bp, vnSnapshotV9Bp, vsnSnapshotV9Bp, _
     clxnSnapshotV9Bp, clnSnapshotV9Bp, "buffer pools", "SNAP_GET_BP_V95", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on bufferpool partitions
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9BpPart, vnSnapshotV9BpPart, vsnSnapshotV9BpPart, _
     clxnSnapshotV9BpPart, clnSnapshotV9BpPart, "buffer pool partitions", "SNAP_GET_BP_PART", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tables
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Tab, vnSnapshotV9Tab, vsnSnapshotV9Tab, _
     clxnSnapshotV9Tab, clnSnapshotV9Tab, "tables", "SNAP_GET_TAB_V91", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on table reorg
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9TabReorg, vnSnapshotV9TabReorg, vsnSnapshotV9TabReorg, _
     clxnSnapshotV9TabReorg, clnSnapshotV9TabReorg, "table reorg", "SNAP_GET_TAB_REORG", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on agents
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Agent, vnSnapshotV9Agent, vsnSnapshotV9Agent, _
     clxnSnapshotV9Agent, clnSnapshotV9Agent, "agents", "SNAP_GET_AGENT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on agent memory pools
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9AgentMemoryPool, vnSnapshotV9AgentMemoryPool, vsnSnapshotV9AgentMemoryPool, _
     clxnSnapshotV9AgentMemoryPool, clnSnapshotV9AgentMemoryPool, "agent memory pools", "SNAP_GET_AGENT_MEMORY_POOL", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on applications
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Appl, vnSnapshotV9Appl, vsnSnapshotV9Appl, _
     clxnSnapshotV9Appl, clnSnapshotV9Appl, "applications", "SNAP_GET_APPL_V95", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on application infos
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9ApplInfo, vnSnapshotV9ApplInfo, vsnSnapshotV9ApplInfo, _
     clxnSnapshotV9ApplInfo, clnSnapshotV9ApplInfo, "application infos", "SNAP_GET_APPL_INFO_V95", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on locks
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Lock, vnSnapshotV9Lock, vsnSnapshotV9Lock, _
     clxnSnapshotV9Lock, clnSnapshotV9Lock, "locks", "SNAP_GET_LOCK", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on lock waits
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9LockWait, vnSnapshotV9LockWait, vsnSnapshotV9LockWait, _
     clxnSnapshotV9LockWait, clnSnapshotV9LockWait, "lock waits", "SNAP_GET_LOCKWAIT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on statements
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Stmt, vnSnapshotV9Statement, vsnSnapshotV9Statement, _
     clxnSnapshotV9Statement, clnSnapshotV9Statement, "statements", "SNAP_GET_STMT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on dynamic SQL statements
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9DynSql, vnSnapshotV9DynSql, vsnSnapshotV9DynSql, _
     clxnSnapshotV9DynSql, clnSnapshotV9DynSql, "dynamic SQL statements", "SNAP_GET_DYN_SQL_V91", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on detail log
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9DetailLog, vnSnapshotV9DetailLog, vsnSnapshotV9DetailLog, _
     clxnSnapshotV9DetailLog, clnSnapshotV9DetailLog, "detail log", "SNAP_GET_DETAILLOG_V91", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on fcm (fast communication manager)
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Fcm, vnSnapshotV9Fcm, vsnSnapshotV9Fcm, _
     clxnSnapshotV9Fcm, clnSnapshotV9Fcm, "fcm (fast communication manager)", "SNAP_GET_FCM", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on fcm part (fast communication manager)
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9FcmPart, vnSnapshotV9FcmPart, vsnSnapshotV9FcmPart, _
     clxnSnapshotV9FcmPart, clnSnapshotV9FcmPart, "fcm part (fast communication manager)", "SNAP_GET_FCM_PART", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on hadr
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Hadr, vnSnapshotV9Hadr, vsnSnapshotV9Hadr, _
     clxnSnapshotV9Hadr, clnSnapshotV9Hadr, "HADR", "SNAP_GET_HADR", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on storage path
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9StoragePaths, vnSnapshotV9StoragePaths, vsnSnapshotV9StoragePaths, _
     clxnSnapshotV9StoragePaths, clnSnapshotV9StoragePaths, "storage paths", "SNAP_GET_STORAGE_PATHS_V97", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on subsection
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Subsection, vnSnapshotV9Subsection, vsnSnapshotV9Subsection, _
     clxnSnapshotV9Subsection, clnSnapshotV9Subsection, "subsection", "SNAP_GET_SUBSECTION", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on switches
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Switches, vnSnapshotV9Switches, vsnSnapshotV9Switches, _
     clxnSnapshotV9Switches, clnSnapshotV9Switches, "switches", "SNAP_GET_SWITCHES", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on util
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9Util, vnSnapshotV9Util, vsnSnapshotV9Util, _
     clxnSnapshotV9Util, clnSnapshotV9Util, "util", "SNAP_GET_UTIL", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on util progress
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotV9UtilProgress, vnSnapshotV9UtilProgress, vsnSnapshotV9UtilProgress, _
     clxnSnapshotV9UtilProgress, clnSnapshotV9UtilProgress, "util progress", "SNAP_GET_UTIL_PROGRESS", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   Dim transformation As AttributeListTransformation

   ' ####################################################################################################################
   ' #    SP for retrieving collective snapshot information
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexDbMonitor, spnGetSnapshot, ddlType)

   printSectionHeader "SP for retrieving collective snapshot information", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optionally) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "useLogging_in", g_dbtBoolean, True, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported"
   genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 30"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "create snapshot ID if none is provided", 1
   Print #fileNo, addTab(1); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(2); "IF mode_in >=1 THEN"
   Print #fileNo, addTab(3); "SET snapshotId_inout = NEXTVAL FOR "; qualSeqNameSnapShotId; ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); g_qualTabNameSnapshotHandle
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "ID,"
   Print #fileNo, addTab(4); "SNAPSHOT_TIMESTAMP"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "CURRENT TIMESTAMP"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET snapshotId_inout = (SELECT MAX(ID) FROM "; g_qualTabNameSnapshotHandle; ");"

   Print #fileNo, addTab(3); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(4); "RETURN;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = UR;"

   genProcSectionHeader fileNo, "loop over all snapshot procedures"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.PROCNAME       AS c_procName,"
   Print #fileNo, addTab(3); "T.ISAPPLSPECIFIC AS c_isApplSpecific"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType; " T"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(agentId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(T.ISAPPLSPECIFIC = 1)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(category_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(T.CATEGORY, category_in) = category_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(level_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(T.LEVEL, level_in) >= level_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "T.SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "call snapshot procedure", 2, True
 
   Print #fileNo, addTab(2); "IF (c_isApplSpecific = 1) THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualTabNameSnapshotType); ".' || c_procName || ' (?,?,?,?,?,?,?)';"
 
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "mode_in,"
   Print #fileNo, addTab(4); "useLogging_in,"
   Print #fileNo, addTab(4); "agentId_in,"
   Print #fileNo, addTab(4); "category_in,"
   Print #fileNo, addTab(4); "level_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualTabNameSnapshotType); ".' || c_procName || ' (?,?,?,?,?,?)';"
 
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "mode_in,"
   Print #fileNo, addTab(4); "useLogging_in,"
   Print #fileNo, addTab(4); "category_in,"
   Print #fileNo, addTab(4); "level_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "count rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "reset ISOLATION LEVEL"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = RESET;"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("

   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optionally) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 30"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, 0, agentId_in, category_in, level_in, tabCount_out, rowCount_out);"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information (short parameter list)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "useLogging_in", g_dbtBoolean, False, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 30"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_agentId", "BIGINT", "NULL"
   genVarDecl fileNo, "v_category", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, useLogging_in, v_agentId, v_category, v_level, v_tabCount, v_rowCount);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information (short parameter list)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", False, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 30"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_agentId", "BIGINT", "NULL"
   genVarDecl fileNo, "v_category", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, 0, v_agentId, v_category, v_level, v_tabCount, v_rowCount);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genDbSnapshotDdlGetSnapshotV8( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim qualFuncNameSnCols As String
   qualFuncNameSnCols = genQualFuncName(g_sectionIndexDbMonitor, udfnSnapshotCols, ddlType)
   Dim qualSeqNameSnapShotId As String
   qualSeqNameSnapShotId = genQualSeqName(g_sectionIndexDbMonitor, gc_seqNameSnapshotId, ddlType)
 
   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database manager
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotDbm, vnSnapshotV8Dbm, vsnSnapshotV8Dbm, _
     clxnSnapshotV8Dbm, clnSnapshotV8Dbm, "database manager", "SNAPSHOT_DBM", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on database
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotDb, vnSnapshotV8Db, vsnSnapshotV8Db, _
     clxnSnapshotV8Db, clnSnapshotV8Db, "database", "SNAPSHOT_DATABASE", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespace configuration
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotTbsCfg, vnSnapshotV8TbsCfg, vsnSnapshotV8TbsCfg, _
     clxnSnapshotV8TbsCfg, clnSnapshotV8TbsCfg, "tablespace configuration", "SNAPSHOT_TBS_CFG", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tablespaces
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotTbs, vnSnapshotV8Tbs, vsnSnapshotV8Tbs, _
     clxnSnapshotV8Tbs, clnSnapshotV8Tbs, "table spaces", "SNAPSHOT_TBS", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on container
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotContainer, vnSnapshotV8Container, vsnSnapshotV8Container, _
     clxnSnapshotV8Container, clnSnapshotV8Container, "container", "SNAPSHOT_CONTAINER", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on bufferpools
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotBufferpool, vnSnapshotV8Bufferpool, vsnSnapshotV8Bufferpool, _
     clxnSnapshotV8BufferPool, clnSnapshotV8BufferPool, "buffer pools", "SNAPSHOT_BP", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on tables
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotTable, vnSnapshotV8Table, vsnSnapshotV8Table, _
     clxnSnapshotV8Table, clnSnapshotV8Table, "tables", "SNAPSHOT_TABLE", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on agents
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotAgent, vnSnapshotV8Agent, vsnSnapshotV8Agent, _
     clxnSnapshotV8Agent, clnSnapshotV8Agent, "agents", "SNAPSHOT_AGENT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on locks
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotLock, vnSnapshotV8Lock, vsnSnapshotV8Lock, _
     clxnSnapshotV8Lock, clnSnapshotV8Lock, "locks", "SNAPSHOT_LOCK", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on lock waits
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotLockWait, vnSnapshotV8LockWait, vsnSnapshotV8LockWait, _
     clxnSnapshotV8LockWait, clnSnapshotV8LockWait, "lock waits", "SNAPSHOT_LOCKWAIT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on applications
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotAppl, vnSnapshotV8SnapshotAppl, vsnSnapshotV8SnapshotAppl, _
     clxnSnapshotV8Appl, clnSnapshotV8Appl, "applications", "SNAPSHOT_APPL", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on application infos
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotApplInfo, vnSnapshotV8ApplInfo, vsnSnapshotV8ApplInfo, _
     clxnSnapshotV8ApplInfo, clnSnapshotV8ApplInfo, "application infos", "SNAPSHOT_APPL_INFO", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on statements
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotStatement, vnSnapshotV8Statement, vsnSnapshotV8Statement, _
     clxnSnapshotV8Statement, clnSnapshotV8Statement, "statements", "SNAPSHOT_STATEMENT", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, True

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot on dynamic SQL statements
   ' ####################################################################################################################

   genGetSnapshotForXyzDdlV _
     fileNo, ddlType, spnGetSnapshotSql, vnSnapshotV8Sql, vsnSnapshotV8Sql, _
     clxnSnapshotV8Sql, clnSnapshotV8Sql, "dynamic SQL statements", "SNAPSHOT_DYN_SQL", qualFuncNameSnCols, _
     qualSeqNameSnapShotId, g_qualTabNameSnapshotType, g_qualTabNameSnapshotFilter, g_qualTabNameSnapshotHandle, False
 
   Dim transformation As AttributeListTransformation

   ' ####################################################################################################################
   ' #    SP for retrieving collective snapshot information
   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optionally) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "useLogging_in", g_dbtBoolean, True, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported"
   genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "create snapshot ID if none is provided", 1
   Print #fileNo, addTab(1); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(2); "IF mode_in >=1 THEN"
   Print #fileNo, addTab(3); "SET snapshotId_inout = NEXTVAL FOR "; qualSeqNameSnapShotId; ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); g_qualTabNameSnapshotHandle
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "ID,"
   Print #fileNo, addTab(4); "SNAPSHOT_TIMESTAMP"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "CURRENT TIMESTAMP"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET snapshotId_inout = (SELECT MAX(ID) FROM "; g_qualTabNameSnapshotHandle; ");"

   Print #fileNo, addTab(3); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(4); "RETURN;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = UR;"

   genProcSectionHeader fileNo, "loop over all snapshot procedures"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.PROCNAME,"
   Print #fileNo, addTab(3); "T.ISAPPLSPECIFIC"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType; " T"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(agentId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(T.ISAPPLSPECIFIC = 1)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(category_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(T.CATEGORY, category_in) = category_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(level_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(T.LEVEL, level_in) >= level_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "T.SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "call snapshot procedure", 2, True
 
   Print #fileNo, addTab(2); "IF ISAPPLSPECIFIC = 1 THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualTabNameSnapshotType); ".' || PROCNAME || ' (?,?,?,?,?,?)';"
 
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "mode_in,"
   Print #fileNo, addTab(4); "agentId_in,"
   Print #fileNo, addTab(4); "category_in,"
   Print #fileNo, addTab(4); "level_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualTabNameSnapshotType); ".' || PROCNAME || ' (?,?,?,?,?,?)';"
 
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "snapshotId_inout,"
   Print #fileNo, addTab(4); "mode_in,"
   Print #fileNo, addTab(4); "useLogging_in,"
   Print #fileNo, addTab(4); "category_in,"
   Print #fileNo, addTab(4); "level_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "count rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "reset ISOLATION LEVEL"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = RESET;"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("

   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optionally) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, 0, agentId_in, category_in, level_in, tabCount_out, rowCount_out);"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information (short parameter list)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "useLogging_in", g_dbtBoolean, False, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_agentId", "BIGINT", "NULL"
   genVarDecl fileNo, "v_category", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, useLogging_in, v_agentId, v_category, v_level, v_tabCount, v_rowCount);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for retrieving collective snapshot information (short parameter list)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshot
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", False, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_agentId", "BIGINT", "NULL"
   genVarDecl fileNo, "v_category", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshot; "(snapshotId_inout, mode_in, 0, v_agentId, v_category, v_level, v_tabCount, v_rowCount);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshot, ddlType, , "snapshotId_inout", "mode_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub genRecreateSnapshotTabDdl( _
   fileNo As Integer, _
   seqNo As Integer, _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   ByRef tempTabNameCrTabStmnt As String, _
   Optional ddlType As DdlTypeId = edtPdm, _
   Optional ByRef viewName As String _
 )
   Dim qualProcNameRevalidate As String
   qualProcNameRevalidate = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType, , , , "VIEWS")

   Dim qualProcNameSetGrants As String
   qualProcNameSetGrants = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "Fltr", eondmNone)

   Dim qualTabName As String
   If acmEntityType = eactClass Then
     qualTabName = genQualTabNameByClassIndex(acmEntityIndex, ddlType)
   ElseIf acmEntityType = eactRelationship Then
     qualTabName = genQualTabNameByRelIndex(acmEntityIndex, ddlType)
   Else
     Exit Sub
   End If

   genProcSectionHeader fileNo, "DROP-Statement for table """ & qualTabName & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DROP TABLE "; qualTabName; "';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempTabNameCrTabStmnt
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "statement"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); CStr(seqNo); ","
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "CREATE-Statement for table """ & qualTabName & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt ="
   Print #fileNo, addTab(2); "'CREATE TABLE ' || CHR(10) ||"
   Print #fileNo, addTab(3); "'"; qualTabName; " ' || CHR(10) ||"
   Print #fileNo, addTab(2); "'(' || CHR(10) ||"

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   Dim columnDefault As String

   initAttributeTransformation transformation, 0
   transformation.trimRight = False
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone

   Dim attributeShortName As String
   Dim i As Integer
   For i = 1 To tabColumns.numDescriptors
       columnDefault = ""
       attributeShortName = "???"
       If tabColumns.descriptors(i).acmAttributeIndex > 0 Then
           columnDefault = g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).default
           attributeShortName = g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).shortName
       End If

       Print #fileNo, addTab(3); "'"; _
                                 Replace( _
                                   genTransformedAttrDeclByDomain( _
                                     tabColumns.descriptors(i).acmAttributeName, attributeShortName, eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformation, _
                                     acmEntityType, acmEntityIndex, IIf(tabColumns.descriptors(i).isNullable, "", "NOT NULL") & _
                                     IIf(columnDefault = "", "", " DEFAULT " & columnDefault), False, ddlType, , , , , 0 _
                                   ), _
                                   "'", "''" _
                                 ); _
                                 IIf(i < tabColumns.numDescriptors, ",", ""); "' || CHR(10) ||"
   Next i

   Print #fileNo, addTab(2); "') ' || CHR(10) ||"
   If ddlType = edtPdm Then
       If g_classes.descriptors(acmEntityIndex).tabSpaceData <> "" Then
         Print #fileNo, addTab(2); "'IN "; genTablespaceNameByIndex(g_classes.descriptors(acmEntityIndex).tabSpaceIndexData); " ' || CHR(10) ||"
       End If
       If g_classes.descriptors(acmEntityIndex).tabSpaceLong <> "" Then
         Print #fileNo, addTab(2); "'LONG IN "; genTablespaceNameByIndex(g_classes.descriptors(acmEntityIndex).tabSpaceIndexLong); " ' || CHR(10) ||"
       End If
       If g_classes.descriptors(acmEntityIndex).tabSpaceIndex <> "" Then
         Print #fileNo, addTab(2); "'INDEX IN "; genTablespaceNameByIndex(g_classes.descriptors(acmEntityIndex).tabSpaceIndexIndex); " ' || CHR(10) ||"
       End If
       If g_classes.descriptors(acmEntityIndex).useValueCompression Then
         Print #fileNo, addTab(2); "'VALUE COMPRESSION' ||"
       End If
       Print #fileNo, addTab(2); "'COMPRESS YES' ||"
   End If

   Print #fileNo, addTab(1); "'';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempTabNameCrTabStmnt
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "statement"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "300000 + "; CStr(seqNo); ","
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "700000 + "; CStr(seqNo); ","
   Print #fileNo, addTab(3); "'CALL "; qualProcNameSetGrants; "(2, ''"; getSchemaName(qualTabName); "%'', ''"; getUnqualObjName(qualTabName); "'', ?)'"
   If viewName <> "" Then
     Print #fileNo, addTab(2); "),"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "400000 + "; CStr(seqNo); ","
     Print #fileNo, addTab(3); "'CALL "; qualProcNameRevalidate; "(''"; g_schemaNameCtoDbMonitor; "%'', ''"; UCase(viewName); "'', 2, ?)'"
     Print #fileNo, addTab(2); "),"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "600000 + "; CStr(seqNo); ","
     Print #fileNo, addTab(3); "'CALL "; qualProcNameSetGrants; "(2, ''"; g_schemaNameCtoDbMonitor; "%'', ''"; UCase(viewName); "'', ?)'"
   End If
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "CALL "; qualProcNameSetGrants; "(2, '"; getSchemaName(qualTabName); "%', '"; getUnqualObjName(qualTabName); "', v_grantCount);"

   If viewName <> "" Then
     Print #fileNo, addTab(2); "CALL "; qualProcNameRevalidate; "('"; g_schemaNameCtoDbMonitor; "%', '"; UCase(viewName); "', 2, v_viewCount);"
 
     Print #fileNo, addTab(2); "CALL "; qualProcNameSetGrants; "(2, '"; g_schemaNameCtoDbMonitor; "%', '"; UCase(viewName); "', v_grantCount);"
   End If

   Print #fileNo, addTab(1); "END IF;"
 End Sub
 
 
 Private Sub genDbSnapshotDdlAdmin( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for creating snapshot-views
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexDbMonitor, spnGenViewSnapshot, ddlType)

   printSectionHeader "SP for creating snapshot-views", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "level_in", "INTEGER", False, "(optional) level to use for column filtering"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(512)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "level_in"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET level_in = COALESCE(level_in, 0);"

   genProcSectionHeader fileNo, "loop over all snapshot views"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.VIEWNAME,"
   Print #fileNo, addTab(3); "T.ISAPPLSPECIFIC"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType; " T"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "T.SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualTabNameSnapshotType); ".' || REPLACE(VIEWNAME, 'V_', 'GENVIEW_') || ' (?)';"
 
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"

   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "level_in"
   Print #fileNo, addTab(2); ";"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "level_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for cleaning up snapshot data
   ' ####################################################################################################################

   qualProcName = genQualProcName(g_sectionIndexDbMonitor, spnSnapshotClear, ddlType)

   printSectionHeader "SP for cleaning up snapshot data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optionally) only snapshot data 'before' this snapshot is cleaned up"
   genProcParm fileNo, "IN", "before_in", "TIMESTAMP", True, "(optionally) only snapshot data before this timestamp is cleaned up"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only"
   genProcParm fileNo, "IN", "commitCount_in", "INTEGER", True, "number of rows to delete before commit (0 = no commit, -1 disable logging + final commit)"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(1024)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genDdlForTempStatement fileNo, 1, True
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_in", "#before_in", "mode_in", "commitCount_in", "tabCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET commitCount_in = COALESCE(commitCount_in, 0);"
   Print #fileNo, addTab(1); "SET tabCount_out   = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out   = 0;"
 
   genProcSectionHeader fileNo, "loop over all snapshot tables"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABLENAME"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType; " T"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "T.SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "cleanup snapshot table", 2
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM "; getSchemaName(g_qualTabNameSnapshotType); ".' || TABLENAME;"
   Print #fileNo, addTab(2); "IF snapshotId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' WHERE (SID <= ' || RTRIM(CHAR(snapshotId_in)) || ')';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF before_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || (CASE WHEN snapshotId_in IS NULL THEN ' WHERE' ELSE ' AND' END) || ' (SNAPSHOT_TIMESTAMP <= ''' || RTRIM(CHAR(before_in)) || ''')';"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "seqNo,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "tabCount_out,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "IF commitCount_in > 0 THEN"

   Print #fileNo, addTab(4); "SET v_stmntTxt = REPLACE(v_stmntTxt, 'DELETE FROM', 'DELETE FROM (SELECT * FROM') || ' FETCH FIRST ' || RTRIM(CHAR(commitCount_in)) || ' ROWS ONLY)';"
   Print #fileNo, addTab(4); "SET v_rowCount = commitCount_in;"
   Print #fileNo,
   Print #fileNo, addTab(4); "WHILE v_rowCount = commitCount_in DO"

   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(5); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo,
   Print #fileNo, addTab(5); "COMMIT;"
   Print #fileNo, addTab(4); "END WHILE;"

   Print #fileNo, addTab(3); "ELSE"

   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "commit if logging is disabled (to minimize risk of unaccessible table)", 3
   Print #fileNo, addTab(3); "IF commitCount_in < 0 THEN"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application", 1
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_in", "#before_in", "mode_in", "commitCount_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for cleaning up snapshot data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optionally) only snapshot data 'before' this snapshot is cleaned up"
   genProcParm fileNo, "IN", "before_in", "TIMESTAMP", True, "(optionally) only snapshot data before this timestamp is cleaned up"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_in", "#before_in", "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(snapshotId_in, before_in, mode_in, -1, tabCount_out, rowCount_out);"
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_in", "#before_in", "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for cleaning up snapshot data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "before_in", "TIMESTAMP", True, "(optionally) only snapshot data before this timestamp is cleaned up"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "before_in", "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(NULL, before_in, mode_in, -1, tabCount_out, rowCount_out);"
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "before_in", "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for cleaning up snapshot data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup statements, '1' cleanup and list statements, '2' cleanup only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of snapshot tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(NULL, NULL, mode_in, -1, tabCount_out, rowCount_out);"
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "mode_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for re-creating Snapshot-Tables
   ' ####################################################################################################################

   Dim qualProcNameReCreateSnapshots As String
   qualProcNameReCreateSnapshots = genQualProcName(g_sectionIndexDbMonitor, spnReCreateSnapshotTables, ddlType)

   printSectionHeader "SP for re-creating Snapshot-Tables", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameReCreateSnapshots
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "tableCount_out", "INTEGER", False, "number of tables re-created"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "doesNotExist", "42704"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_returnResult", g_dbtBoolean, gc_dbTrue
   genVarDecl fileNo, "v_tableCount", "INTEGER", "0"
   genVarDecl fileNo, "v_viewCount", "INTEGER", "0"
   genVarDecl fileNo, "v_grantCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_returnResult = "; gc_dbFalse; "; -- just fill the table"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcNameReCreateSnapshots, ddlType, , "mode_in", "tableCount_out"
 
   Dim tempTabNameCrTabStmnt  As String
   tempTabNameCrTabStmnt = tempTabNameStatement & "CrTab"
   genDdlForTempStatement fileNo, 1, False, 30000, , True, , , "CrTab"
 
   genProcSectionHeader fileNo, "SET output parameter"
   Print #fileNo, addTab(1); "SET tableCount_out = "; CStr(g_snapshotTypes.numDescriptors); ";"

   Dim qualProcNameSetGrants As String
   qualProcNameSetGrants = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "Fltr", eondmNone)

   Dim tabSpaceNameSnapshot As String
   tabSpaceNameSnapshot = ""
   Dim classIndexSnapshot As Integer
   Dim qualTabName As String
   Dim j As Integer
   For j = 1 To g_snapshotTypes.numDescriptors
       classIndexSnapshot = g_snapshotTypes.descriptors(j).classIndex

       If tabSpaceNameSnapshot = "" Then
         tabSpaceNameSnapshot = g_classes.descriptors(classIndexSnapshot).tabSpaceData
       End If

       genRecreateSnapshotTabDdl fileNo, j, eactClass, classIndexSnapshot, tempTabNameCrTabStmnt, ddlType, g_snapshotTypes.descriptors(j).viewName
   Next j

   Dim i As Integer
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).tabSpaceData <> tabSpaceNameSnapshot Or g_classes.descriptors(i).sectionName = snDbMonitor Then
         GoTo NextI
       End If

       genRecreateSnapshotTabDdl fileNo, j, eactClass, g_classes.descriptors(i).classIndex, tempTabNameCrTabStmnt, ddlType
       j = j + 1
 NextI:
   Next i

   For i = 1 To g_relationships.numDescriptors
       If g_relationships.descriptors(i).tabSpaceData <> tabSpaceNameSnapshot Or g_relationships.descriptors(i).sectionName = snDbMonitor Then
         GoTo NextII
       End If

       genRecreateSnapshotTabDdl fileNo, j, eactRelationship, g_relationships.descriptors(i).relIndex, tempTabNameCrTabStmnt, ddlType
       j = j + 1
 NextII:
   Next i

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 AND v_returnResult = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameCrTabStmnt
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcNameReCreateSnapshots, ddlType, , "mode_in", "tableCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for pruning Snapshot Monitor Tablespace
   ' ####################################################################################################################

   Dim qualProcNamePruneSnapshots As String
   qualProcNamePruneSnapshots = genQualProcName(g_sectionIndexDbMonitor, spnSnapshotPrune, ddlType)

   printSectionHeader "SP for pruning Snapshot-Monitor-Tablespace", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNamePruneSnapshots
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "containerSize_in", "INTEGER", True, "size of tablespace container to allocate initially"
   genProcParm fileNo, "OUT", "tableCount_out", "INTEGER", False, "number of tables re-created"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "doesNotExist", "42704"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_tsPageSize", "INTEGER", "NULL"
   genVarDecl fileNo, "v_tsType", "CHAR(1)", "NULL"
   genVarDecl fileNo, "v_tsBufferpoolName", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_tsExtentSize", "INTEGER", "NULL"
   genVarDecl fileNo, "v_tsPrefetchSize", "INTEGER", "NULL"
   genVarDecl fileNo, "v_tsOverhead", "DOUBLE", "NULL"
   genVarDecl fileNo, "v_tsTransferRate", "DOUBLE", "NULL"

   genVarDecl fileNo, "v_containerFound", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_grantCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL"
   genVarDecl fileNo, "v_stmntTxtDropTs", "VARCHAR(300)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   tempTabNameCrTabStmnt = tempTabNameStatement & "CrTab"
   genDdlForTempStatement fileNo, 1, True, 30000, , True, , , "CrTab"
 
   genProcSectionHeader fileNo, "set output parameter"
   Print #fileNo, addTab(1); "SET tableCount_out = "; CStr(g_snapshotTypes.numDescriptors); ";"

   genProcSectionHeader fileNo, "verify input parameter"
   Print #fileNo, addTab(1); "SET containerSize_in = COALESCE(containerSize_in, 100000);"
 
   Dim thisTabSpaceIndex As Integer
   Dim tsNameList As String
   tsNameList = ""
   For thisTabSpaceIndex = 1 To g_tableSpaces.numDescriptors
       If g_tableSpaces.descriptors(thisTabSpaceIndex).isMonitor Then
         tsNameList = tsNameList & IIf(tsNameList = "", "", ", ") & "'" & UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName) & "'"
       End If
   Next thisTabSpaceIndex

   genSpLogProcEnter fileNo, qualProcNamePruneSnapshots, ddlType, , "mode_in", "containerSize_in", "tableCount_out"
 
   genProcSectionHeader fileNo, "Drop all tables in tablespaces """ & tsNameList & """"

   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA,"
   Print #fileNo, addTab(3); "T.TABNAME,"
   Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY T.TABSCHEMA ASC, T.TABNAME ASC) TABNO"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "UPPER(T.TBSPACE) IN ("; tsNameList; ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "UPPER(T.INDEX_TBSPACE) IN ("; tsNameList; ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "UPPER(T.LONG_TBSPACE) IN ("; tsNameList; ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "T.TABSCHEMA,"
   Print #fileNo, addTab(3); "T.TABNAME"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DROP TABLE ' || RTRIM(TABSCHEMA) || '.' || TABNAME;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"

   Dim tsNo As Integer
   tsNo = 1
   For thisTabSpaceIndex = 1 To g_tableSpaces.numDescriptors
       If g_tableSpaces.descriptors(thisTabSpaceIndex).isMonitor Then
         genProcSectionHeader fileNo, "determine statements for drop and create of tablespace """ & g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName & """"
         Print #fileNo, addTab(1); "SET v_stmntTxtDropTs = 'DROP TABLESPACE "; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); "';"
         Print #fileNo,

         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "T.PAGESIZE,"
         Print #fileNo, addTab(2); "T.TBSPACETYPE,"
         Print #fileNo, addTab(2); "B.BPNAME,"
         Print #fileNo, addTab(2); "T.EXTENTSIZE,"
         Print #fileNo, addTab(2); "T.PREFETCHSIZE,"
         Print #fileNo, addTab(2); "T.OVERHEAD,"
         Print #fileNo, addTab(2); "T.TRANSFERRATE"
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_tsPageSize,"
         Print #fileNo, addTab(2); "v_tsType,"
         Print #fileNo, addTab(2); "v_tsBufferpoolName,"
         Print #fileNo, addTab(2); "v_tsExtentSize,"
         Print #fileNo, addTab(2); "v_tsPrefetchSize,"
         Print #fileNo, addTab(2); "v_tsOverhead,"
         Print #fileNo, addTab(2); "v_tsTransferRate"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); "SYSCAT.TABLESPACES T"
         Print #fileNo, addTab(1); "INNER JOIN"
         Print #fileNo, addTab(2); "SYSCAT.BUFFERPOOLS B"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "T.BUFFERPOOLID = B.BUFFERPOOLID"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "UPPER(T.TBSPACE) = '"; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); "'"
         Print #fileNo, addTab(1); ";"
         Print #fileNo,
         Print #fileNo, addTab(1); "SET v_tsPageSize       = COALESCE(v_tsPageSize       , "; IIf(g_tableSpaces.descriptors(thisTabSpaceIndex).pageSize <> "", g_tableSpaces.descriptors(thisTabSpaceIndex).pageSize, "4096"); ");"
         Print #fileNo, addTab(1); "SET v_tsType           = COALESCE(v_tsType           , '"; IIf(g_tableSpaces.descriptors(thisTabSpaceIndex).category = tscDms, "D", "S"); "');"
         Print #fileNo, addTab(1); "SET v_tsBufferpoolName = COALESCE(v_tsBufferpoolName , '"; genBufferPoolNameByIndex(g_tableSpaces.descriptors(thisTabSpaceIndex).bufferPoolIndex); "');"
         Print #fileNo, addTab(1); "SET v_tsExtentSize     = COALESCE(v_tsExtentSize     , "; g_tableSpaces.descriptors(thisTabSpaceIndex).extentSize; ");"
         Print #fileNo, addTab(1); "SET v_tsPrefetchSize   = COALESCE(v_tsPrefetchSize   , "; g_tableSpaces.descriptors(thisTabSpaceIndex).prefetchSize; ");"
         Print #fileNo,
         Print #fileNo, addTab(1); "SET v_stmntTxt ="
         Print #fileNo, addTab(2); "'CREATE "; IIf(g_tableSpaces.descriptors(thisTabSpaceIndex).type <> "", UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).type) & " ", ""); "TABLESPACE ' || CHR(10) ||"

         Print #fileNo, addTab(3); "'"; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); " ' || CHR(10) ||"
         Print #fileNo, addTab(2); "'PAGESIZE ' || CHAR(v_tsPageSize) || CHR(10) ||"
         Print #fileNo, addTab(2); "'MANAGED BY ' || (CASE v_tsType WHEN 'D' THEN 'DATABASE ' ELSE 'SYSTEM ' END) || CHR(10) ||"

         Print #fileNo, addTab(2); "'USING ( ' || CHR(10);"

         genProcSectionHeader fileNo, "determine tablespace container", 1
         Print #fileNo, addTab(1); "SET v_containerFound = "; gc_dbFalse; ";"
         Print #fileNo, addTab(1); "FOR containerLoop AS"
         Print #fileNo, addTab(2); "SELECT"
         Print #fileNo, addTab(3); "C.CONTAINER_NAME,"
         Print #fileNo, addTab(3); "C.CONTAINER_TYPE,"
         Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY C.CONTAINER_NAME ASC) CNO"
         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); "TABLE(SYSPROC.SNAPSHOT_CONTAINER(CURRENT SERVER,-1)) C"
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "C.TABLESPACE_NAME = '"; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); "'"
         Print #fileNo, addTab(2); "ORDER BY"
         Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY C.CONTAINER_NAME DESC)"
         Print #fileNo, addTab(1); "DO"
         Print #fileNo, addTab(2); "SET v_containerFound = "; gc_dbTrue; ";"
         Print #fileNo,
         Print #fileNo, addTab(2); "IF CONTAINER_TYPE = 0 THEN -- SMS"
         Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '  ''' || CONTAINER_NAME || '''' || CHR(10);"
         Print #fileNo, addTab(2); "ELSE -- DMS"
         Print #fileNo, addTab(3); "IF CONTAINER_TYPE IN (2,6) THEN -- DMS / File"
         Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || '  FILE ''' || CONTAINER_NAME || ''' ' || CHAR(containerSize_in) || (CASE CNO WHEN 1 THEN ' ' ELSE ', ' END) || CHR(10);"
         Print #fileNo, addTab(3); "ELSE -- DMS / Disk"
         Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || '  DEVICE ''' || CONTAINER_NAME || ''' ' || CHAR(containerSize_in) || (CASE CNO WHEN 1 THEN ' ' ELSE ', ' END) || CHR(10);"
         Print #fileNo, addTab(3); "END IF;"
         Print #fileNo, addTab(2); "END IF;"
         Print #fileNo, addTab(1); "END FOR;"
         Print #fileNo,
 
         Print #fileNo, addTab(1); "IF v_containerFound = 1 THEN"
         Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || ') ' || CHR(10);"
         Print #fileNo, addTab(2); "IF v_tsType = 'D' THEN"
         If g_tableSpaces.descriptors(thisTabSpaceIndex).autoResize Then
           Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||"
           Print #fileNo, addTab(4); "'AUTORESIZE YES ' || CHR(10) ||"
           If g_tableSpaces.descriptors(thisTabSpaceIndex).increasePercent > 0 Then
             Print #fileNo, addTab(4); "'INCREASESIZE "; CStr(g_tableSpaces.descriptors(thisTabSpaceIndex).increasePercent); " PERCENT ' || CHR(10) ||"
           ElseIf g_tableSpaces.descriptors(thisTabSpaceIndex).increaseAbsolute <> "" Then
             Print #fileNo, addTab(4); "'INCREASESIZE "; g_tableSpaces.descriptors(thisTabSpaceIndex).increaseAbsolute; " ' || CHR(10) ||"
           End If
           If g_tableSpaces.descriptors(thisTabSpaceIndex).maxSize <> "" Then
             Print #fileNo, addTab(4); "'MAXSIZE "; g_tableSpaces.descriptors(thisTabSpaceIndex).maxSize; " ' || CHR(10) ||"
           End If
           Print #fileNo, addTab(4); "''"
           Print #fileNo, addTab(3); ";"
         End If
         Print #fileNo, addTab(2); "END IF;"
         Print #fileNo, addTab(1); "ELSE"
         Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt ||"

         Dim numContainerRefs As Integer
         numContainerRefs = g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.numDescriptors
         If g_tableSpaces.descriptors(thisTabSpaceIndex).category = tscSms Then
             For j = 1 To numContainerRefs
               Print #fileNo, addTab(3); "'"; genContainerNameByIndex(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(j)); "'"; IIf(j = numContainerRefs, "", ","); " ' || CHR(10 ||"
             Next j
             Print #fileNo, addTab(3); "') ' || CHR(10) ||"
         Else
             For j = 1 To numContainerRefs
                 Print #fileNo, addTab(3); "'  "; IIf(g_containers.descriptors(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(j)).type = cntFile, "FILE", "DEVICE"); " "; _
                       "''"; genContainerNameByIndex(g_containers.descriptors(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(j)).containerIndex); "''"; " "; _
                       CStr(g_containers.descriptors(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(j)).size); IIf(j = numContainerRefs, "", ","); " ' || chr(10) ||"
             Next j
             Print #fileNo, addTab(2); "') ' || CHR(10) ||"

           If g_tableSpaces.descriptors(thisTabSpaceIndex).autoResize Then
             Print #fileNo, addTab(3); "'AUTORESIZE YES ' || CHR(10) ||"

             If g_tableSpaces.descriptors(thisTabSpaceIndex).increasePercent > 0 Then
               Print #fileNo, addTab(3); "'INCREASESIZE "; CStr(g_tableSpaces.descriptors(thisTabSpaceIndex).increasePercent); " PERCENT ' || CHR(10) ||"
             ElseIf g_tableSpaces.descriptors(thisTabSpaceIndex).increaseAbsolute <> "" Then
               Print #fileNo, addTab(3); "'INCREASESIZE "; g_tableSpaces.descriptors(thisTabSpaceIndex).increaseAbsolute; " ' || CHR(10) ||"
             End If

             If g_tableSpaces.descriptors(thisTabSpaceIndex).maxSize <> "" Then
               Print #fileNo, addTab(3); "'MAXSIZE "; g_tableSpaces.descriptors(thisTabSpaceIndex).maxSize; " ' || CHR(10) ||"
             End If
           End If
         End If
 
         Print #fileNo, addTab(3); "''"
         Print #fileNo, addTab(2); ";"
         Print #fileNo, addTab(1); "END IF;"
         Print #fileNo,
 
         Print #fileNo, addTab(1); "SET v_stmntTxt = v_stmntTxt ||"

         If ddlType = edtPdm Then
           If g_tableSpaces.descriptors(thisTabSpaceIndex).extentSize <> "" Then
             Print #fileNo, addTab(2); "'EXTENTSIZE ' || CHAR(v_tsExtentSize) || CHR(10) ||"
           End If
           If g_tableSpaces.descriptors(thisTabSpaceIndex).prefetchSize <> "" Then
             Print #fileNo, addTab(2); "'PREFETCHSIZE ' || CHAR(v_tsPrefetchSize) || CHR(10) ||"
           End If
         End If

         Print #fileNo, addTab(2); "'BUFFERPOOL "; genBufferPoolNameByIndex(g_tableSpaces.descriptors(thisTabSpaceIndex).bufferPoolIndex); " ' || CHR(10) ||"

         If ddlType = edtPdm Then
           Print #fileNo, addTab(2); "'"; IIf(Not g_tableSpaces.descriptors(thisTabSpaceIndex).useFileSystemCaching, "NO ", ""); "FILE SYSTEM CACHING ' || CHR(10) ||"
           Print #fileNo, addTab(2); "(CASE WHEN v_tsOverhead IS NULL THEN '' ELSE 'OVERHEAD ' || CHAR(v_tsOverhead) || ' ' END) || CHR(10) ||"
           Print #fileNo, addTab(2); "(CASE WHEN v_tsTransferRate IS NULL THEN '' ELSE 'TRANSFERRATE ' || CHAR(v_tsTransferRate) || ' ' END) || CHR(10) ||"
           Print #fileNo, addTab(2); "'DROPPED TABLE RECOVERY "; IIf(g_tableSpaces.descriptors(thisTabSpaceIndex).supportDroppedTableRecovery, "ON", "OFF"); " ' || CHR(10) ||"
         End If
         Print #fileNo, addTab(2); "''"
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader fileNo, "store statements in temporary table", 1
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); tempTabNameCrTabStmnt
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); "seqNo,"
         Print #fileNo, addTab(2); "statement"
         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "VALUES"
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); CStr(100000 + tsNo); ","
         Print #fileNo, addTab(2); "v_stmntTxtDropTs"
         Print #fileNo, addTab(1); "),"
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); CStr(200000 + tsNo); ","
         Print #fileNo, addTab(2); "v_stmntTxt"
         Print #fileNo, addTab(1); "),"
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); CStr(500000 + tsNo); ","
         Print #fileNo, addTab(2); "'CALL "; qualProcNameSetGrants; "(2, NULL, ''"; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); "'', ?)'"
         Print #fileNo, addTab(1); ");"

         Print #fileNo,
         Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
         genProcSectionHeader fileNo, "commit changes", 2, True
         Print #fileNo, addTab(2); "COMMIT;"

         genProcSectionHeader fileNo, "drop tablespace", 2, True
         Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxtDropTs;"

         genProcSectionHeader fileNo, "commit changes", 2
         Print #fileNo, addTab(2); "COMMIT;"

         genProcSectionHeader fileNo, "create tablespace", 2
         Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

         genProcSectionHeader fileNo, "set GRANTs on tablespace", 2
         Print #fileNo, addTab(2); "CALL "; qualProcNameSetGrants; "(2, NULL, '"; UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName); "', v_grantCount);"
         Print #fileNo, addTab(1); "END IF;"

         tsNo = tsNo + 1
       End If
   Next thisTabSpaceIndex

   genProcSectionHeader fileNo, "recreate Snapshot tables", 1
   Print #fileNo, addTab(1); "CALL "; qualProcNameReCreateSnapshots; "(mode_in, tableCount_out);"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameCrTabStmnt
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcNamePruneSnapshots, ddlType, , "mode_in", "containerSize_in", "tableCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   qualProcNamePruneSnapshots = genQualProcName(g_sectionIndexDbMonitor, spnSnapshotPrune, ddlType)

   printSectionHeader "SP for pruning Snapshot-Monitor-Tablespace", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNamePruneSnapshots
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "tableCount_out", "INTEGER", False, "number of tables re-created"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcNamePruneSnapshots, ddlType, , "mode_in", "tableCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNamePruneSnapshots; "(mode_in, 100000, tableCount_out);"
 
   genSpLogProcExit fileNo, qualProcNamePruneSnapshots, ddlType, , "mode_in", "tableCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbSnapshotDdlAnalysis( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
 ' ### IF IVK ###
   If Left(snapshotApiVersion, 1) = "8" Then
     genDbSnapshotDdlAnalysisV8 fileNo, ddlType
   ElseIf snapshotApiVersion = "9.7" Then
     genDbSnapshotDdlAnalysisV9_7 fileNo, ddlType
   End If
 ' ### ELSE IVK ###
 ' genDbSnapshotDdlAnalysisV9_7 fileNo, ddlType
 ' ### ENDIF IVK ###
 End Sub
 
 
 Private Sub genDbSnapshotDdlAnalysisV9_7( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for analyzing LOCK-WAIT snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing LOCK-WAIT snapshot data", fileNo
 
   Dim qualFuncNameLockMode2Str As String
   qualFuncNameLockMode2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockMode2Str, ddlType)

   Dim qualFuncNameLockObjType2Str As String
   qualFuncNameLockObjType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockObjType2Str, ddlType)

   Dim qualFuncNameStmntType2Str As String
   qualFuncNameStmntType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str, ddlType)

   Dim qualFuncNameStmntType2StrS As String
   qualFuncNameStmntType2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str & "_S", ddlType)

   Dim qualFuncNameApplStatus2Str As String
   qualFuncNameApplStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str, ddlType)

   Dim qualFuncNameApplStatus2StrS As String
   qualFuncNameApplStatus2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str & "_S", ddlType)

   Dim qualFuncNameStmntOperation2Str As String
   qualFuncNameStmntOperation2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str, ddlType)

   Dim qualFuncNameStmntOperation2StrS As String
   qualFuncNameStmntOperation2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str & "_S", ddlType)
 
   Const maxRecordLength = 8000

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisLockWait
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "ignored for this procedure"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_delimLine", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_emptyLine", "VARCHAR(80)", "NULL"
   genVarDecl fileNo, "v_firstLine", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_thisRecord", "VARCHAR(" & CStr(2 * maxRecordLength) & ")", "NULL"
   genVarDecl fileNo, "v_agentLoopCount", "SMALLINT", "NULL"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, maxRecordLength

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_delimLine = REPEAT('-', 100);"
   Print #fileNo, addTab(1); "SET v_emptyLine = REPEAT(' ', 100);"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snWtLoop AS snWtCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "L.SID                  L_SID,"
   Print #fileNo, addTab(3); "L.SNAPSHOT_TIMESTAMP   L_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "L.AGENT_ID             L_AGENT_ID,"
   Print #fileNo, addTab(3); "L.AGENT_ID_HOLDING_LK  L_AGENT_ID_HOLDING_LK,"
   Print #fileNo, addTab(3); "L.LOCK_WAIT_START_TIME L_LOCK_WAIT_START_TIME,"
   Print #fileNo, addTab(3); "L.LOCK_MODE            L_LOCK_MODE,"
   Print #fileNo, addTab(3); "L.LOCK_OBJECT_TYPE     LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(3); "L.LOCK_MODE_REQUESTED  L_LOCK_MODE_REQUESTED,"
   Print #fileNo, addTab(3); "L.TBSP_NAME            L_TBSP_NAME,"
   Print #fileNo, addTab(3); "L.TABSCHEMA            L_TABSCHEMA,"
   Print #fileNo, addTab(3); "L.TABNAME              L_TABNAME,"
   Print #fileNo, addTab(3); "AW.APPL_STATUS         AW_APPL_STATUS,"
   Print #fileNo, addTab(3); "AW.APPL_ID             AW_APPL_ID,"
   Print #fileNo, addTab(3); "AW.PRIMARY_AUTH_ID     AW_PRIMARY_AUTH_ID,"
   Print #fileNo, addTab(3); "AW.SESSION_AUTH_ID     AW_SESSION_AUTH_ID,"
   Print #fileNo, addTab(3); "AW.CLIENT_NNAME        AW_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_USERID AW_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_WKSTN  AW_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_APP    AW_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "AW.TPMON_ACC_STR       AW_TPMON_ACC_STR,"
   Print #fileNo, addTab(3); "AH.APPL_STATUS         AH_APPL_STATUS,"
   Print #fileNo, addTab(3); "AH.APPL_ID             AH_APPL_ID,"
   Print #fileNo, addTab(3); "AH.PRIMARY_AUTH_ID     AH_PRIMARY_AUTH_ID,"
   Print #fileNo, addTab(3); "AH.SESSION_AUTH_ID     AH_SESSION_AUTH_ID,"
   Print #fileNo, addTab(3); "AH.CLIENT_NNAME        AH_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_USERID AH_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_WKSTN  AH_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_APP    AH_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "AH.TPMON_ACC_STR       AH_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotLockWait; " L"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " AW"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L.SID = AW.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L.AGENT_ID = AW.AGENT_ID"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " AH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L.SID = AH.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L.AGENT_ID_HOLDING_LK = AH.AGENT_ID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(snapshotId_in IS NULL)"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(L.SID = snapshotId_in)"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L.SID,"
   Print #fileNo, addTab(3); "L.AGENT_ID"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_thisRecord = '';"
   Print #fileNo, addTab(2); "SET v_thisRecord = v_nl ||"
   Print #fileNo, addTab(2); "                   'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(L_SID)), '') || v_nl || v_nl ||"
   Print #fileNo, addTab(2); "                   'Timestamp               : ' || COALESCE(RTRIM(CHAR(L_SNAPSHOT_TIMESTAMP)), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Wait Start Time    : ' || COALESCE(RTRIM(CHAR(L_LOCK_WAIT_START_TIME)), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Object Type        : ' || COALESCE("; qualFuncNameLockObjType2Str; "(LOCK_OBJECT_TYPE), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Mode               : ' || COALESCE("; qualFuncNameLockMode2Str; "(L_LOCK_MODE), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Mode Requested     : ' || COALESCE("; qualFuncNameLockMode2Str; "(L_LOCK_MODE_REQUESTED), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Space             : ' || COALESCE(L_TBSP_NAME, '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Schema            : ' || COALESCE(L_TABSCHEMA, '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Name              : ' || COALESCE(L_TABNAME, '') || v_nl ||"
   Print #fileNo, addTab(2); "                   v_nl"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_agentLoopCount = 1;"
 
   genProcSectionHeader fileNo, "loop over agents: 1 = agent waiting for lock, 2 = agent holding lock", 2
   Print #fileNo, addTab(2); "REPEAT"
   Print #fileNo, addTab(3); "IF v_agentLoopCount = 1 THEN"
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   'Agent waiting for Lock  : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AW_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(AW_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID (P)  : ' || COALESCE(AW_PRIMARY_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID (S)  : ' || COALESCE(AW_SESSION_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_NNAME          : ' || COALESCE(AW_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_USERID         : ' || COALESCE(AW_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AW_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_APPLNAME       : ' || COALESCE(AW_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_ACCTNG         : ' || COALESCE(AW_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   'Agent holding Lock      : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID_HOLDING_LK)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AH_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(AH_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID (P)  : ' || COALESCE(AH_PRIMARY_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID (S)  : ' || COALESCE(AH_SESSION_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_NNAME          : ' || COALESCE(AH_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_USERID         : ' || COALESCE(AH_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AH_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_APPLNAME       : ' || COALESCE(AH_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_ACCTNG         : ' || COALESCE(AH_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader fileNo, "loop over all statements related to this agent", 3
   Print #fileNo, addTab(3); "SET v_firstLine = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "FOR snStLoop AS snStCursor CURSOR FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "S.SID,"
   Print #fileNo, addTab(5); "S.SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(5); "S.ROWS_READ,"
   Print #fileNo, addTab(5); "S.ROWS_WRITTEN,"
   Print #fileNo, addTab(5); "S.STMT_TYPE,"
   Print #fileNo, addTab(5); "S.STMT_OPERATION,"
   Print #fileNo, addTab(5); "S.STMT_TEXT,"
   Print #fileNo, addTab(5); "S.STMT_START,"
   Print #fileNo, addTab(5); "S.STMT_STOP,"
   Print #fileNo, addTab(5); "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS ELAPSED_TIME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "S.SID = L_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "S.AGENT_ID = (CASE v_agentLoopCount WHEN 1 THEN L_AGENT_ID ELSE L_AGENT_ID_HOLDING_LK END)"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "S.STMT_START"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(4); "WITH UR"
   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "IF v_firstLine = 1 THEN"
   Print #fileNo, addTab(5); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(5); "                   '    ' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Statement Start ', 27) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Statement Stop ' , 28) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Type '           ,  9) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Op '             ,  9) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Rows Read '      , 13) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Rows Written '   , 13) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Time Elapsed '   , 18) || '|' ||"
   Print #fileNo, addTab(5); "                   ' STATEMENT' ||"
   Print #fileNo, addTab(5); "                   v_nl ||"
   Print #fileNo, addTab(5); "                   '    ' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 27) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 28) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine,  9) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine,  9) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 13) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 13) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 18) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 81) ||"
   Print #fileNo, addTab(5); "                   v_nl"
   Print #fileNo, addTab(5); ";"
   Print #fileNo, addTab(5); "SET v_firstLine = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   '    ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CHAR(STMT_START),''),26) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CHAR(STMT_STOP),''),26) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE("; qualFuncNameStmntType2StrS; "(STMT_TYPE),''), 7) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE("; qualFuncNameStmntOperation2StrS; "(STMT_OPERATION),''), 7) || ' | ' ||"
   Print #fileNo, addTab(4); "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_READ)),''),11) || ' | ' ||"
   Print #fileNo, addTab(4); "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_WRITTEN)),''),11) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CAST("
   Print #fileNo, addTab(4); "                     CAST(SECOND(ELAPSED_TIME) + 60 * (MINUTE(ELAPSED_TIME) + 60 * (HOUR(ELAPSED_TIME) + 24 * DAY(ELAPSED_TIME))) + CAST(MICROSECOND(ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6))"
   Print #fileNo, addTab(4); "                   AS CHAR(16)),''),16) || ' | ' ||"
   Print #fileNo, addTab(4); "                   COALESCE(REPLACE(LEFT(STMT_TEXT,80), CHR(10), ' '), '') ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "END FOR;"
   Print #fileNo, addTab(3); "SET v_agentLoopCount = v_agentLoopCount + 1;"
   Print #fileNo, addTab(2); "UNTIL"
   Print #fileNo, addTab(3); "v_agentLoopCount = 3"
   Print #fileNo, addTab(2); "END REPEAT;"
   Print #fileNo,
 
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameSnRecords
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "record"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "LEFT(CLOB("
   Print #fileNo, addTab(4); "v_thisRecord || v_nl)"
   Print #fileNo, addTab(4); ", "; CStr(maxRecordLength)
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   printSectionHeader "SP for analyzing LOCK-WAIT snapshot data", fileNo
 
   ' ####################################################################################################################

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisLockWait
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "recordCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisLockWait; "(snapshotId_in, 0, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for analyzing Application snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing Appplication snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisAppl
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "if '0' retrieve records involving inconsistencies, if '1' retrieve all records"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_delimLine", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_lrtIdStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_previousLrtIdStr", "VARCHAR(25)", "NULL"
 ' ### IF IVK ###
   genVarDecl fileNo, "v_orgIdLrtStr", "VARCHAR(2)", "NULL"
   genVarDecl fileNo, "v_orgIdLockStr", "VARCHAR(2)", "NULL"
 ' ### ENDIF IVK ###
   genVarDecl fileNo, "v_qualTabNameLock", "VARCHAR(100)", "NULL"
 ' ### IF IVK ###
   genVarDecl fileNo, "v_orgIdStmntStr", "VARCHAR(2)", "NULL"
 ' ### ENDIF IVK ###
   genVarDecl fileNo, "v_creatorStmnt", "VARCHAR(20)", "NULL"
   genVarDecl fileNo, "v_stmnt", "VARCHAR(80)", "NULL"
 ' ### IF IVK ###
   genVarDecl fileNo, "v_psOidStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_previousPsOidStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_psOidLrtStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_cdUserId", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_previousCdUserId", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_cdUserIdLrt", g_dbtUserId, "NULL"
 ' ### ENDIF IVK ###
   genVarDecl fileNo, "v_thisRecord", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousRecord", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_thisRecordInfo", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousRecordInfo", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousApplStatus", "SMALLINT", "NULL"
   genVarDecl fileNo, "v_previousAgentId", "INTEGER", "NULL"
   genVarDecl fileNo, "v_firstSid", "BIGINT", "NULL"
   genVarDecl fileNo, "v_previousSid", "BIGINT", "NULL"
   genVarDecl fileNo, "v_firstTimeStamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_outputPreviousRecord", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(300)", "NULL"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, 2048

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_delimLine = REPEAT('-', 100);"
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisAppl, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snAppLoop AS snAppCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "A.SID                 A_SID,"
   Print #fileNo, addTab(3); "A.SNAPSHOT_TIMESTAMP  A_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "A.APPL_STATUS         A_APPL_STATUS,"
   Print #fileNo, addTab(3); "A.AGENT_ID            A_AGENT_ID,"
   Print #fileNo, addTab(3); "A.APPL_ID             A_APPL_ID,"
   Print #fileNo, addTab(3); "A.PRIMARY_AUTH_ID     A_PRIMARY_AUTH_ID,"
   Print #fileNo, addTab(3); "A.SESSION_AUTH_ID     A_SESSION_AUTH_ID,"
   Print #fileNo, addTab(3); "A.CLIENT_NNAME        A_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_USERID A_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_WKSTN  A_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_APP    A_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "A.TPMON_ACC_STR       A_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " A"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(snapshotId_in IS NULL)"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(A.SID = snapshotId_in)"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "A.AGENT_ID ASC,"
   Print #fileNo, addTab(3); "A.SID      ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_thisRecordInfo = '';"
   Print #fileNo, addTab(2); "SET v_firstSid       = COALESCE(v_firstSid,       A_SID);"
   Print #fileNo, addTab(2); "SET v_firstTimeStamp = COALESCE(v_firstTimeStamp, A_SNAPSHOT_TIMESTAMP);"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_lrtIdStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_WKSTN ),  25));"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "SET v_psOidStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_APP   ),  25));"
   Print #fileNo, addTab(2); "SET v_cdUserId = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_USERID), 100));"
 ' ### ENDIF IVK ###
   Print #fileNo,

 ' ### IF IVK ###
   Print #fileNo, addTab(2); "SET v_orgIdLrtStr     = NULL;"
   Print #fileNo, addTab(2); "SET v_orgIdLockStr    = NULL;"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "SET v_qualTabNameLock = NULL;"
   Print #fileNo,

 ' ### IF IVK ###
   Print #fileNo, addTab(2); "IF LENGTH(v_lrtIdStr) > LENGTH('"; gc_sequenceMinValue; "') THEN"
   Print #fileNo, addTab(3); "SET v_orgIdLrtStr = LEFT(v_lrtIdStr, LENGTH(v_lrtIdStr) - LENGTH('"; gc_sequenceMinValue; "'));"
   Print #fileNo, addTab(3); "IF LENGTH(v_orgIdLrtStr) = 1 THEN"
   Print #fileNo, addTab(4); "SET v_orgIdLrtStr = '0' || v_orgIdLrtStr;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF "; g_qualFuncNameIsNumeric; "(v_orgIdLrtStr) = 1 THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt  = 'SELECT RTRIM(CHAR(L."; g_anPsOid; ")), U."; g_anUserId; " FROM "; genSchemaName(snLrt, ssnLrt, ddlType); "' || RTRIM(CHAR(v_orgIdLrtStr)) || '"; CStr(g_workDataPoolId); ".LRT L LEFT OUTER JOIN "; _
                             g_qualTabNameUser; " U ON U."; g_anOid; " = L.UTROWN_OID WHERE L."; g_anOid; " = ' || v_lrtIdStr || ' WITH UR';"
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "OPEN c;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "c"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_psOidLrtStr,"
   Print #fileNo, addTab(5); "v_cdUserIdLrt"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE c WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "V.orgIdStr,"
   Print #fileNo, addTab(3); "RTRIM(V.tabSchema) || '.' || RTRIM(V.tabName)"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_orgIdLockStr,"
   Print #fileNo, addTab(3); "v_qualTabNameLock"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "L.TABSCHEMA AS tabSchema,"
   Print #fileNo, addTab(5); "L.TABNAME AS tabName,"
   Print #fileNo, addTab(5); "LEFT(RIGHT(L.TABSCHEMA, 3), 2) AS orgIdStr,"
   Print #fileNo, addTab(5); "(CASE WHEN LEFT(RIGHT(L.TABSCHEMA, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotLock; " L"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "L.SID = A_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L.AGENT_ID = A_AGENT_ID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_qualFuncNameIsNumeric; "(LEFT(RIGHT(L.TABSCHEMA, 3), 2)) = "; gc_dbTrue
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "LEFT(L.TABSCHEMA, 3) = '"; productKey; "'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "sortCrit ASC"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(3); ") V"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "V.orgIdStr,"
   Print #fileNo, addTab(3); "V.creator,"
   Print #fileNo, addTab(3); "V.stmt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_orgIdStmntStr,"
   Print #fileNo, addTab(3); "v_creatorStmnt,"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "RTRIM(LEFT(S.CREATOR, 20)) AS creator,"
   Print #fileNo, addTab(5); "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS stmt,"
   Print #fileNo, addTab(5); "RTRIM(LEFT(RIGHT(S.CREATOR, 3), 2)) AS orgIdStr,"
   Print #fileNo, addTab(5); "(CASE WHEN LEFT(RIGHT(S.CREATOR, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "S.SID = A_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "S.AGENT_ID = A_AGENT_ID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_qualFuncNameIsNumeric; "(LEFT(RIGHT(S.CREATOR, 3), 2)) = "; gc_dbTrue
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "LEFT(S.CREATOR, 3) = '"; productKey; "'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "sortCrit ASC"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(3); ") V"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdLockStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    ORG-ID according to LRT-OID-Register <-> ''locked Table'' : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdLockStr || COALESCE(' (' || v_qualTabNameLock || ')', '') || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdStmntStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    ORG-ID according to LRT-OID-Register <-> ''static SQL''   : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdStmntStr || COALESCE(' (' || v_creatorStmnt || ' / ' || v_stmnt || ')', '') || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_psOidStr IS NOT NULL AND v_psOidLrtStr IS NOT NULL AND v_psOidStr <> v_psOidLrtStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    PS-OID according to PS-OID-Register  <-> LRT            : ' || v_psOidStr || ' <-> ' || v_psOidLrtStr || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_cdUserId IS NOT NULL AND v_cdUserIdLrt IS NOT NULL AND v_cdUserId <> v_cdUserIdLrt THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    CD-UserId according to UID-Register  <-> LRT            : ''' || v_cdUserId || ''' <-> ''' || v_cdUserIdLrt || '''' || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "SET v_outputPreviousRecord = (CASE WHEN"
   Print #fileNo, addTab(2); "                               ("
   Print #fileNo, addTab(2); "                                 (v_previousRecordInfo IS NOT NULL AND v_previousRecordInfo <> v_thisRecordInfo)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (v_previousApplStatus IS NOT NULL AND v_previousApplStatus <> A_APPL_STATUS)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousAgentId, -1) <> A_AGENT_ID)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousLrtIdStr, '') <> COALESCE(v_lrtIdStr, ''))"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousPsOidStr, '') <> COALESCE(v_psOidStr, ''))"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousCdUserId, '') <> COALESCE(v_cdUserId, ''))"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "                               )"
   Print #fileNo, addTab(2); "                                 AND"
   Print #fileNo, addTab(2); "                               (mode_in = 1 OR v_previousRecordInfo <> '')"
   Print #fileNo, addTab(2); "                             THEN 1 ELSE 0 END);"
   Print #fileNo, addTab(2); "SET v_previousRecord = v_thisRecord;"
   Print #fileNo, addTab(2); "SET v_thisRecord =  LEFT("
   Print #fileNo, addTab(14); "v_nl ||"
   Print #fileNo, addTab(14); "'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(v_firstSid)), '') || (CASE WHEN v_firstSid <> A_SID THEN ' - ' || COALESCE(RTRIM(CHAR(A_SID)), '') ELSE '' END) || v_nl || v_nl ||"
   Print #fileNo, addTab(14); "'Timestamp               : ' || COALESCE(RTRIM(CHAR(v_firstTimeStamp)), '') || (CASE WHEN v_firstTimeStamp <> A_SNAPSHOT_TIMESTAMP THEN ' - ' || COALESCE(RTRIM(CHAR(A_SNAPSHOT_TIMESTAMP)), '') ELSE '' END) || v_nl ||"
   Print #fileNo, addTab(14); "'Agent                   : ' || COALESCE(RTRIM(CHAR(A_AGENT_ID)), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Application Id          : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Application Status      : ' || COALESCE("; qualFuncNameApplStatus2Str; "(A_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Authorization ID (P)    : ' || COALESCE(A_PRIMARY_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(14); "'Authorization ID (S)    : ' || COALESCE(A_SESSION_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_NNAME            : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_USERID           : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_WRKSTNNAME       : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_APPLNAME         : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_ACCTNG           : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(14); "v_nl ||"
   Print #fileNo, addTab(14); "(CASE WHEN v_thisRecordInfo = '' THEN '' ELSE '  Inconsistencies       : ' || v_nl || v_nl || v_thisRecordInfo || v_nl END)"
   Print #fileNo, addTab(14); ", 1024"
   Print #fileNo, addTab(13); ");"
   Print #fileNo,

   Print #fileNo, addTab(2); "IF v_outputPreviousRecord = 1 THEN"

   genProcSectionHeader fileNo, "add statement related infos", 3, True
   Print #fileNo, addTab(3); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(4); "FOR snStmtLoop AS snStmtCursor CURSOR FOR"
   Print #fileNo, addTab(5); "SELECT DISTINCT"
   Print #fileNo, addTab(6); "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS S_STMT"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "(S.SID >= v_firstSid)"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.SID <= COALESCE(v_previousSid, v_firstSid))"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.STMT_TEXT IS NOT NULL)"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.AGENT_ID = A_AGENT_ID)"
   Print #fileNo, addTab(5); "FETCH FIRST 10 ROWS ONLY"

   Print #fileNo, addTab(4); "DO"
   Print #fileNo, addTab(5); "SET v_previousRecord = v_previousRecord ||"
   Print #fileNo, addTab(17); "'      > ' || S_STMT || v_nl;"

   Print #fileNo, addTab(4); "END FOR;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_previousRecord || v_nl || v_delimLine || v_nl"
   Print #fileNo, addTab(3); ");"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(3); "SET v_previousSid   = A_SID;"
   Print #fileNo, addTab(3); "SET v_firstSid      = NULL;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "SET v_previousRecord     = v_thisRecord;"
   Print #fileNo, addTab(2); "SET v_previousRecordInfo = v_thisRecordInfo;"
   Print #fileNo, addTab(2); "SET v_previousApplStatus = A_APPL_STATUS;"
   Print #fileNo, addTab(2); "SET v_previousAgentId    = A_AGENT_ID;"
   Print #fileNo, addTab(2); "SET v_previousLrtIdStr   = v_lrtIdStr;"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "SET v_previousPsOidStr   = v_psOidStr;"
   Print #fileNo, addTab(2); "SET v_previousCdUserId   = v_cdUserId;"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "END FOR;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_outputPreviousRecord = (CASE WHEN (v_firstSid IS NOT NULL) AND (mode_in = 1 OR v_previousRecordInfo <> '') THEN 1 ELSE 0 END);"
   Print #fileNo, addTab(1); "IF v_outputPreviousRecord = 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameSnRecords
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "record"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "v_previousRecord"
   Print #fileNo, addTab(2); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out  = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisAppl, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for analyzing STATEMENT snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Const maxRecordLengthStmnt = 32000

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotIdStart_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "snapshotIdEnd_in", "BIGINT", True, "(optional) identifies the last snapshot to analyze"
   genProcParm fileNo, "IN", "startTime_in", "TIMESTAMP", True, "(optional) identifies the fime of the first snapshot to analyze"
   genProcParm fileNo, "IN", "endTime_in", "TIMESTAMP", True, "(optional) identifies the fime of the last snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "determines the level of details provided (0=low, 1=medium, 2=high)"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_headLine1", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_sidDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_agentDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmtDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_thisRecord", "CLOB(100M)", "NULL"
   genVarDecl fileNo, "v_thisRecordLck", "CLOB(100M)", "NULL"
   genVarDecl fileNo, "v_stmtNo", "INTEGER", "1"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genVarDecl fileNo, "v_recordLength", "INTEGER", CStr(maxRecordLengthStmnt)
   genVarDecl fileNo, "v_lastSid", "BIGINT", "-1"
   genVarDecl fileNo, "v_lastAgentId", "BIGINT", "-1"
   genVarDecl fileNo, "v_numLocks", "BIGINT", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, maxRecordLengthStmnt

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_sidDelimLine = REPEAT('#', 100);"
   Print #fileNo, addTab(1); "SET v_agentDelimLine = REPEAT('=', 100);"
   Print #fileNo, addTab(1); "SET v_stmtDelimLine = REPEAT('-', 100);"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snStmntLoop AS snAppCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "S.SID                  S_SID,"
   Print #fileNo, addTab(3); "S.SNAPSHOT_TIMESTAMP   S_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "S.AGENT_ID             S_AGENT_ID,"
   Print #fileNo, addTab(3); "S.STMT_TEXT            S_STMT_TEXT,"
   Print #fileNo, addTab(3); "S.ROWS_READ            S_ROWS_READ,"
   Print #fileNo, addTab(3); "S.ROWS_WRITTEN         S_ROWS_WRITTEN,"
   Print #fileNo, addTab(3); "S.STMT_TYPE            S_STMT_TYPE,"
   Print #fileNo, addTab(3); "S.STMT_OPERATION       S_STMT_OPERATION,"
   Print #fileNo, addTab(3); "S.QUERY_COST_ESTIMATE  S_QUERY_COST_ESTIMATE,"
   Print #fileNo, addTab(3); "S.QUERY_CARD_ESTIMATE  S_QUERY_CARD_ESTIMATE,"
   Print #fileNo, addTab(3); "S.STMT_SORTS           S_STMT_SORTS,"
   Print #fileNo, addTab(3); "S.TOTAL_SORT_TIME      S_TOTAL_SORT_TIME,"
   Print #fileNo, addTab(3); "S.SORT_OVERFLOWS       S_SORT_OVERFLOWS,"
   Print #fileNo, addTab(3); "S.INT_ROWS_DELETED     S_INT_ROWS_DELETED,"
   Print #fileNo, addTab(3); "S.INT_ROWS_UPDATED     S_INT_ROWS_UPDATED,"
   Print #fileNo, addTab(3); "S.INT_ROWS_INSERTED    S_INT_ROWS_INSERTED,"
   Print #fileNo, addTab(3); "S.STMT_START           S_STMT_START,"
   Print #fileNo, addTab(3); "S.STMT_STOP            S_STMT_STOP,"
   Print #fileNo, addTab(3); "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS S_ELAPSED_TIME,"
   Print #fileNo, addTab(3); "A.APPL_STATUS          A_APPL_STATUS,"
   Print #fileNo, addTab(3); "A.APPL_ID              A_APPL_ID,"
   Print #fileNo, addTab(3); "A.PRIMARY_AUTH_ID      A_PRIMARY_AUTH_ID,"
   Print #fileNo, addTab(3); "A.SESSION_AUTH_ID      A_SESSION_AUTH_ID,"
   Print #fileNo, addTab(3); "A.CLIENT_NNAME         A_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_USERID  A_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_WKSTN   A_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_APP     A_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "A.TPMON_ACC_STR        A_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "S.SID = A.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "S.AGENT_ID = A.AGENT_ID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(snapshotIdStart_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SID >= snapshotIdStart_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(snapshotIdEnd_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SID <= snapshotIdEnd_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(startTime_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SNAPSHOT_TIMESTAMP >= startTime_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(endTime_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SNAPSHOT_TIMESTAMP <= endTime_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(agentId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.AGENT_ID = agentId_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(mode_in > 1)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(STMT_TEXT IS NOT NULL)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "S.SID        ASC,"
   Print #fileNo, addTab(3); "S.AGENT_ID   ASC,"
   Print #fileNo, addTab(3); "S.STMT_START ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "retrieve LOCK-infos for previous Statement", 2, True
   Print #fileNo, addTab(2); "IF (v_lastSid <> S_SID OR v_lastAgentId <> S_AGENT_ID) AND v_lastSid > 0 AND v_lastAgentId > 0 THEN"
   genSaveLockInfoDdl fileNo, 3, g_qualTabNameSnapshotLock, ddlType
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_thisRecord = v_nl;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_lastSid <> S_SID THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_nl ||"
   Print #fileNo, addTab(12); " v_sidDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(S_SID                            )), '') || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " '  Timestamp             : ' || COALESCE(RTRIM(CHAR(S_SNAPSHOT_TIMESTAMP             )), '') || v_nl || v_nl"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastSid = S_SID;"
   Print #fileNo, addTab(3); "SET v_lastAgentId = -1;"
   Print #fileNo, addTab(3); "SET v_stmtNo = 1;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_lastAgentId <> S_AGENT_ID THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl ||"
   Print #fileNo, addTab(12); " v_agentDelimLine || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " 'Agent Id                : ' || COALESCE(RTRIM(CHAR(S_AGENT_ID                       )), '') || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " '  Application Id        : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(12); " '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(A_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(12); " '  Authorization ID (P)  : ' || COALESCE(A_PRIMARY_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  Authorization ID (S)  : ' || COALESCE(A_SESSION_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_NNAME          : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_USERID         : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_WRKSTNNAME     : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_APPLNAME       : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_ACCTNG         : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "SET v_lastAgentId = S_AGENT_ID;"
   Print #fileNo, addTab(3); "SET v_stmtNo = 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_thisRecord = v_thisRecord || v_nl ||"
   Print #fileNo, addTab(3); " v_stmtDelimLine || v_nl ||"
   Print #fileNo, addTab(3); " v_nl ||"
   Print #fileNo, addTab(12); " 'Statement               : ' || COALESCE(RTRIM(CHAR(v_stmtNo)), '') || v_nl ||"
   Print #fileNo, addTab(12); "   v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Stop        : ' || COALESCE(CHAR(S_STMT_STOP ),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Time Elapsed          : ' || COALESCE(CAST(CAST(SECOND(S_ELAPSED_TIME) + 60 * (MINUTE(S_ELAPSED_TIME) + 60 * (HOUR(S_ELAPSED_TIME) + 24 * DAY(S_ELAPSED_TIME))) + CAST(MICROSECOND(S_ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6)) AS CHAR(16)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Type                  : ' || COALESCE("; qualFuncNameStmntType2Str; "(S_STMT_TYPE),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Op                    : ' || COALESCE("; qualFuncNameStmntOperation2Str; "(S_STMT_OPERATION),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Read             : ' || COALESCE(RTRIM(CHAR(S_ROWS_READ)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Written          : ' || COALESCE(RTRIM(CHAR(S_ROWS_WRITTEN)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Query Cost (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_COST_ESTIMATE)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Query Card (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_CARD_ESTIMATE)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Sorts       : ' || COALESCE(RTRIM(CHAR(S_STMT_SORTS)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Total Sort Time       : ' || COALESCE(RTRIM(CHAR(S_TOTAL_SORT_TIME)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Sort Overflows        : ' || COALESCE(RTRIM(CHAR(S_SORT_OVERFLOWS)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Deleted (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_DELETED)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Updated (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_UPDATED)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Inserted (int.)  : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_INSERTED)),'') || v_nl"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Statement Text          : ' || v_nl || v_nl || COALESCE(RTRIM(LEFT(REPLACE(S_STMT_TEXT, CHR(10), ' '), 120)), '')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Statement Text          : ' || v_nl || v_nl || COALESCE(S_STMT_TEXT, '');"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmtNo = v_stmtNo + 1;"
   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO "; pc_tempTabNameSnRecords; "(record) VALUES (LEFT(CLOB(v_thisRecord || v_nl),  v_recordLength));"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   genProcSectionHeader fileNo, "retrieve LOCK-infos for last Statement", 1, True
   Print #fileNo, addTab(1); "IF v_lastSid > 0 AND v_lastAgentId > 0 THEN"
   genSaveLockInfoDdl fileNo, 2, g_qualTabNameSnapshotLock, ddlType
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "recordCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisStatement; "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, 0, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "determines the level of details provided (0=low, 1=medium, 2=high)"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "mode_in", "recordCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisStatement; "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, mode_in, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "mode_in", "recordCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for analyzing snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysis
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_recordCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysis, ddlType, , "snapshotId_in", "recordCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"

   genProcSectionHeader fileNo, "loop over all snapshot types supporting analysis"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "REPLACE(PROCNAME, 'GETSNAPSHOT', 'GETSNAPSHOTANALYSIS') AS PROCNAME"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "SUPPORTANALYSIS = "; gc_dbTrue
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualProcNameGetSnapshotAnalysis); ".' || PROCNAME || '(?,0,?)';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_recordCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "snapshotId_in"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + v_recordCount;"

   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysis, ddlType, , "snapshotId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub

 
 ' ### IF IVK ###
 Private Sub genDbSnapshotDdlAnalysisV8( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for analyzing LOCK-WAIT snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing LOCK-WAIT snapshot data", fileNo
 
   Dim qualFuncNameLockMode2Str As String
   qualFuncNameLockMode2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockMode2Str, ddlType)

   Dim qualFuncNameLockObjType2Str As String
   qualFuncNameLockObjType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnLockObjType2Str, ddlType)

   Dim qualFuncNameStmntType2StrS As String
   qualFuncNameStmntType2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str & "_S", ddlType)

   Dim qualFuncNameStmntType2Str As String
   qualFuncNameStmntType2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntType2Str, ddlType)

   Dim qualFuncNameApplStatus2Str As String
   Dim qualFuncNameApplStatus2StrS As String
   qualFuncNameApplStatus2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str, ddlType)
   qualFuncNameApplStatus2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnApplStatus2Str & "_S", ddlType)

   Dim qualFuncNameStmntOperation2StrS As String
   qualFuncNameStmntOperation2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str & "_S", ddlType)
 
   Dim qualFuncNameStmntOperation2Str As String
   qualFuncNameStmntOperation2Str = genQualFuncName(g_sectionIndexDbMonitor, udfnStmntOp2Str, ddlType)
 
   Const maxRecordLength = 8000

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisLockWait
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "ignored for this procedure"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_delimLine", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_emptyLine", "VARCHAR(80)", "NULL"
   genVarDecl fileNo, "v_firstLine", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_thisRecord", "VARCHAR(" & CStr(2 * maxRecordLength) & ")", "NULL"
   genVarDecl fileNo, "v_agentLoopCount", "SMALLINT", "NULL"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, maxRecordLength

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_delimLine = REPEAT('-', 100);"
   Print #fileNo, addTab(1); "SET v_emptyLine = REPEAT(' ', 100);"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snWtLoop AS snWtCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "L.SID                  L_SID,"
   Print #fileNo, addTab(3); "L.SNAPSHOT_TIMESTAMP   L_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "L.AGENT_ID             L_AGENT_ID,"
   Print #fileNo, addTab(3); "L.AGENT_ID_HOLDING_LK  L_AGENT_ID_HOLDING_LK,"
   Print #fileNo, addTab(3); "L.LOCK_WAIT_START_TIME L_LOCK_WAIT_START_TIME,"
   Print #fileNo, addTab(3); "L.LOCK_MODE            L_LOCK_MODE,"
   Print #fileNo, addTab(3); "L.LOCK_OBJECT_TYPE     LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(3); "L.LOCK_MODE_REQUESTED  L_LOCK_MODE_REQUESTED,"
   Print #fileNo, addTab(3); "L.TABLESPACE_NAME      L_TABLESPACE_NAME,"
   Print #fileNo, addTab(3); "L.TABLE_SCHEMA         L_TABLE_SCHEMA,"
   Print #fileNo, addTab(3); "L.TABLE_NAME           L_TABLE_NAME,"
   Print #fileNo, addTab(3); "AW.APPL_STATUS         AW_APPL_STATUS,"
   Print #fileNo, addTab(3); "AW.APPL_ID             AW_APPL_ID,"
   Print #fileNo, addTab(3); "AW.AUTH_ID             AW_AUTH_ID,"
   Print #fileNo, addTab(3); "AW.CLIENT_NNAME        AW_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_USERID AW_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_WKSTN  AW_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "AW.TPMON_CLIENT_APP    AW_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "AW.TPMON_ACC_STR       AW_TPMON_ACC_STR,"
   Print #fileNo, addTab(3); "AH.APPL_STATUS         AH_APPL_STATUS,"
   Print #fileNo, addTab(3); "AH.APPL_ID             AH_APPL_ID,"
   Print #fileNo, addTab(3); "AH.AUTH_ID             AH_AUTH_ID,"
   Print #fileNo, addTab(3); "AH.CLIENT_NNAME        AH_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_USERID AH_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_WKSTN  AH_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "AH.TPMON_CLIENT_APP    AH_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "AH.TPMON_ACC_STR       AH_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotLockWait; " L"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " AW"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L.SID = AW.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L.AGENT_ID = AW.AGENT_ID"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " AH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L.SID = AH.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L.AGENT_ID_HOLDING_LK = AH.AGENT_ID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(snapshotId_in IS NULL)"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(L.SID = snapshotId_in)"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L.SID,"
   Print #fileNo, addTab(3); "L.AGENT_ID"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_thisRecord = '';"
   Print #fileNo, addTab(2); "SET v_thisRecord = v_nl ||"
   Print #fileNo, addTab(2); "                   'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(L_SID                            )), '') || v_nl || v_nl ||"
   Print #fileNo, addTab(2); "                   'Timestamp               : ' || COALESCE(RTRIM(CHAR(L_SNAPSHOT_TIMESTAMP             )), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Wait Start Time    : ' || COALESCE(RTRIM(CHAR(L_LOCK_WAIT_START_TIME           )), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Object Type        : ' || COALESCE("; qualFuncNameLockObjType2Str; "(LOCK_OBJECT_TYPE), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Mode               : ' || COALESCE("; qualFuncNameLockMode2Str; "(L_LOCK_MODE           ), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Lock Mode Requested     : ' || COALESCE("; qualFuncNameLockMode2Str; "(L_LOCK_MODE_REQUESTED ), '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Space             : ' || COALESCE(L_TABLESPACE_NAME                             , '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Schema            : ' || COALESCE(L_TABLE_SCHEMA                                , '') || v_nl ||"
   Print #fileNo, addTab(2); "                   'Table Name              : ' || COALESCE(L_TABLE_NAME                                  , '') || v_nl ||"
   Print #fileNo, addTab(2); "                   v_nl"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_agentLoopCount = 1;"
 
   genProcSectionHeader fileNo, "loop over agents: 1 = agent waiting for lock, 2 = agent holding lock", 2
   Print #fileNo, addTab(2); "REPEAT"
   Print #fileNo, addTab(3); "IF v_agentLoopCount = 1 THEN"
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   'Agent waiting for Lock  : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AW_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(AW_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID      : ' || COALESCE(AW_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_NNAME          : ' || COALESCE(AW_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_USERID         : ' || COALESCE(AW_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AW_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_APPLNAME       : ' || COALESCE(AW_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_ACCTNG         : ' || COALESCE(AW_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   'Agent holding Lock      : ' || COALESCE(RTRIM(CHAR(L_AGENT_ID_HOLDING_LK)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Id        : ' || COALESCE(RTRIM(CHAR(AH_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(AH_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  Authorization ID      : ' || COALESCE(AH_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_NNAME          : ' || COALESCE(AH_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_USERID         : ' || COALESCE(AH_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_WRKSTNNAME     : ' || COALESCE(AH_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_APPLNAME       : ' || COALESCE(AH_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   '  CLIENT_ACCTNG         : ' || COALESCE(AH_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader fileNo, "loop over all statements related to this agent", 3
   Print #fileNo, addTab(3); "SET v_firstLine = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "FOR snStLoop AS snStCursor CURSOR FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "S.SID,"
   Print #fileNo, addTab(5); "S.SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(5); "S.ROWS_READ,"
   Print #fileNo, addTab(5); "S.ROWS_WRITTEN,"
   Print #fileNo, addTab(5); "S.STMT_TYPE,"
   Print #fileNo, addTab(5); "S.STMT_OPERATION,"
   Print #fileNo, addTab(5); "S.STMT_TEXT,"
   Print #fileNo, addTab(5); "S.STMT_START,"
   Print #fileNo, addTab(5); "S.STMT_STOP,"
   Print #fileNo, addTab(5); "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS ELAPSED_TIME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "S.SID = L_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "S.AGENT_ID = (CASE v_agentLoopCount WHEN 1 THEN L_AGENT_ID ELSE L_AGENT_ID_HOLDING_LK END)"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "S.STMT_START"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(4); "WITH UR"
   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "IF v_firstLine = 1 THEN"
   Print #fileNo, addTab(5); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(5); "                   '    ' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Statement Start ', 27) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Statement Stop ' , 28) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Type '           ,  9) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Op '             ,  9) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Rows Read '      , 13) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Rows Written '   , 13) || '|' ||"
   Print #fileNo, addTab(5); "                   RIGHT(v_emptyLine || 'Time Elapsed '   , 18) || '|' ||"
   Print #fileNo, addTab(5); "                   ' STATEMENT' ||"
   Print #fileNo, addTab(5); "                   v_nl ||"
   Print #fileNo, addTab(5); "                   '    ' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 27) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 28) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine,  9) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine,  9) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 13) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 13) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 18) || '+' ||"
   Print #fileNo, addTab(5); "                   LEFT(v_delimLine, 81) ||"
   Print #fileNo, addTab(5); "                   v_nl"
   Print #fileNo, addTab(5); ";"
   Print #fileNo, addTab(5); "SET v_firstLine = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(4); "SET v_thisRecord = v_thisRecord ||"
   Print #fileNo, addTab(4); "                   '    ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CHAR(STMT_START),''),26) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CHAR(STMT_STOP),''),26) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE("; qualFuncNameStmntType2StrS; "(STMT_TYPE),''), 7) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE("; qualFuncNameStmntOperation2StrS; "(STMT_OPERATION),''), 7) || ' | ' ||"
   Print #fileNo, addTab(4); "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_READ)),''),11) || ' | ' ||"
   Print #fileNo, addTab(4); "                   RIGHT(v_emptyLine || COALESCE(RTRIM(CHAR(ROWS_WRITTEN)),''),11) || ' | ' ||"
   Print #fileNo, addTab(4); "                   CHAR(COALESCE(CAST("
   Print #fileNo, addTab(4); "                     CAST(SECOND(ELAPSED_TIME) + 60 * (MINUTE(ELAPSED_TIME) + 60 * (HOUR(ELAPSED_TIME) + 24 * DAY(ELAPSED_TIME))) + CAST(MICROSECOND(ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6))"
   Print #fileNo, addTab(4); "                   AS CHAR(16)),''),16) || ' | ' ||"
   Print #fileNo, addTab(4); "                   COALESCE(REPLACE(LEFT(STMT_TEXT,80), CHR(10), ' '), '') ||"
   Print #fileNo, addTab(4); "                   v_nl"
   Print #fileNo, addTab(4); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "END FOR;"
   Print #fileNo, addTab(3); "SET v_agentLoopCount = v_agentLoopCount + 1;"
   Print #fileNo, addTab(2); "UNTIL"
   Print #fileNo, addTab(3); "v_agentLoopCount = 3"
   Print #fileNo, addTab(2); "END REPEAT;"
   Print #fileNo,
 
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameSnRecords
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "record"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "LEFT(CLOB("
   Print #fileNo, addTab(4); "v_thisRecord || v_nl)"
   Print #fileNo, addTab(4); ", "; CStr(maxRecordLength)
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   printSectionHeader "SP for analyzing LOCK-WAIT snapshot data", fileNo
 
   ' ####################################################################################################################

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisLockWait
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "recordCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisLockWait; "(snapshotId_in, 0, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisLockWait, ddlType, , "snapshotId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for analyzing Application snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing Appplication snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisAppl
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "if '0' retrieve records involving inconsistencies, if '1' retrieve all records"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_delimLine", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_lrtIdStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_previousLrtIdStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_orgIdLrtStr", "VARCHAR(2)", "NULL"
   genVarDecl fileNo, "v_orgIdLockStr", "VARCHAR(2)", "NULL"
   genVarDecl fileNo, "v_qualTabNameLock", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_orgIdStmntStr", "VARCHAR(2)", "NULL"
   genVarDecl fileNo, "v_creatorStmnt", "VARCHAR(20)", "NULL"
   genVarDecl fileNo, "v_stmnt", "VARCHAR(80)", "NULL"
   genVarDecl fileNo, "v_psOidStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_previousPsOidStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_psOidLrtStr", "VARCHAR(25)", "NULL"
   genVarDecl fileNo, "v_cdUserId", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_previousCdUserId", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_cdUserIdLrt", g_dbtUserId, "NULL"
   genVarDecl fileNo, "v_thisRecord", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousRecord", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_thisRecordInfo", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousRecordInfo", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_previousApplStatus", "SMALLINT", "NULL"
   genVarDecl fileNo, "v_previousAgentId", "INTEGER", "NULL"
   genVarDecl fileNo, "v_firstSid", "BIGINT", "NULL"
   genVarDecl fileNo, "v_previousSid", "BIGINT", "NULL"
   genVarDecl fileNo, "v_firstTimeStamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_outputPreviousRecord", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(300)", "NULL"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, 2048

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_delimLine = REPEAT('-', 100);"
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisAppl, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snAppLoop AS snAppCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "A.SID                 A_SID,"
   Print #fileNo, addTab(3); "A.SNAPSHOT_TIMESTAMP  A_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "A.APPL_STATUS         A_APPL_STATUS,"
   Print #fileNo, addTab(3); "A.AGENT_ID            A_AGENT_ID,"
   Print #fileNo, addTab(3); "A.APPL_ID             A_APPL_ID,"
   Print #fileNo, addTab(3); "A.AUTH_ID             A_AUTH_ID,"
   Print #fileNo, addTab(3); "A.CLIENT_NNAME        A_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_USERID A_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_WKSTN  A_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_APP    A_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "A.TPMON_ACC_STR       A_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " A"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(snapshotId_in IS NULL)"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(A.SID = snapshotId_in)"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "A.AGENT_ID ASC,"
   Print #fileNo, addTab(3); "A.SID      ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_thisRecordInfo = '';"
   Print #fileNo, addTab(2); "SET v_firstSid       = COALESCE(v_firstSid,       A_SID);"
   Print #fileNo, addTab(2); "SET v_firstTimeStamp = COALESCE(v_firstTimeStamp, A_SNAPSHOT_TIMESTAMP);"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_lrtIdStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_WKSTN ),  25));"
   Print #fileNo, addTab(2); "SET v_psOidStr = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_APP   ),  25));"
   Print #fileNo, addTab(2); "SET v_cdUserId = RTRIM(LEFT(LTRIM(A_TPMON_CLIENT_USERID), 100));"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET v_orgIdLrtStr     = NULL;"
   Print #fileNo, addTab(2); "SET v_orgIdLockStr    = NULL;"
   Print #fileNo, addTab(2); "SET v_qualTabNameLock = NULL;"
   Print #fileNo,

   Print #fileNo, addTab(2); "IF LENGTH(v_lrtIdStr) > LENGTH('"; gc_sequenceMinValue; "') THEN"
   Print #fileNo, addTab(3); "SET v_orgIdLrtStr = LEFT(v_lrtIdStr, LENGTH(v_lrtIdStr) - LENGTH('"; gc_sequenceMinValue; "'));"
   Print #fileNo, addTab(3); "IF LENGTH(v_orgIdLrtStr) = 1 THEN"
   Print #fileNo, addTab(4); "SET v_orgIdLrtStr = '0' || v_orgIdLrtStr;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF "; g_qualFuncNameIsNumeric; "(v_orgIdLrtStr) = 1 THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt  = 'SELECT RTRIM(CHAR(L."; g_anPsOid; ")), U."; g_anUserId; " FROM "; genSchemaName(snLrt, ssnLrt, ddlType); "' || RTRIM(CHAR(v_orgIdLrtStr)) || '"; CStr(g_workDataPoolId); ".LRT L LEFT OUTER JOIN "; _
                             g_qualTabNameUser; " U ON U."; g_anOid; " = L.UTROWN_OID WHERE L."; g_anOid; " = ' || v_lrtIdStr || ' WITH UR';"
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "OPEN c;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "c"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_psOidLrtStr,"
   Print #fileNo, addTab(5); "v_cdUserIdLrt"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE c WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "V.orgIdStr,"
   Print #fileNo, addTab(3); "RTRIM(V.tabSchema) || '.' || RTRIM(V.tabName)"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_orgIdLockStr,"
   Print #fileNo, addTab(3); "v_qualTabNameLock"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "L.TABLE_SCHEMA AS tabSchema,"
   Print #fileNo, addTab(5); "L.TABLE_NAME AS tabName,"
   Print #fileNo, addTab(5); "LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2) AS orgIdStr,"
   Print #fileNo, addTab(5); "(CASE WHEN LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotLock; " L"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "L.SID = A_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L.AGENT_ID = A_AGENT_ID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_qualFuncNameIsNumeric; "(LEFT(RIGHT(L.TABLE_SCHEMA, 3), 2)) = "; gc_dbTrue
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "LEFT(L.TABLE_SCHEMA, 3) = '"; productKey; "'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "sortCrit ASC"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(3); ") V"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "V.orgIdStr,"
   Print #fileNo, addTab(3); "V.creator,"
   Print #fileNo, addTab(3); "V.stmt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_orgIdStmntStr,"
   Print #fileNo, addTab(3); "v_creatorStmnt,"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "RTRIM(LEFT(S.CREATOR, 20)) AS creator,"
   Print #fileNo, addTab(5); "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS stmt,"
   Print #fileNo, addTab(5); "RTRIM(LEFT(RIGHT(S.CREATOR, 3), 2)) AS orgIdStr,"
   Print #fileNo, addTab(5); "(CASE WHEN LEFT(RIGHT(S.CREATOR, 3), 2) = v_orgIdLrtStr THEN 1 ELSE 0 END) AS sortCrit"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "S.SID = A_SID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "S.AGENT_ID = A_AGENT_ID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); g_qualFuncNameIsNumeric; "(LEFT(RIGHT(S.CREATOR, 3), 2)) = "; gc_dbTrue
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "LEFT(S.CREATOR, 3) = '"; productKey; "'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "sortCrit ASC"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(3); ") V"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdLockStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    ORG-ID according to LRT-OID-Register <-> ''locked Table'' : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdLockStr || COALESCE(' (' || v_qualTabNameLock || ')', '') || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_orgIdLrtStr IS NOT NULL AND v_orgIdLockStr IS NOT NULL AND v_orgIdLrtStr <> v_orgIdStmntStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    ORG-ID according to LRT-OID-Register <-> ''static SQL''   : ' || v_orgIdLrtStr || ' <-> ' || v_orgIdStmntStr || COALESCE(' (' || v_creatorStmnt || ' / ' || v_stmnt || ')', '') || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_psOidStr IS NOT NULL AND v_psOidLrtStr IS NOT NULL AND v_psOidStr <> v_psOidLrtStr THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    PS-OID according to PS-OID-Register  <-> LRT            : ' || v_psOidStr || ' <-> ' || v_psOidLrtStr || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF v_cdUserId IS NOT NULL AND v_cdUserIdLrt IS NOT NULL AND v_cdUserId <> v_cdUserIdLrt THEN"
   Print #fileNo, addTab(3); "SET v_thisRecordInfo = v_thisRecordInfo ||"
   Print #fileNo, addTab(7); "           '    CD-UserId according to UID-Register  <-> LRT            : ''' || v_cdUserId || ''' <-> ''' || v_cdUserIdLrt || '''' || v_nl;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_outputPreviousRecord = (CASE WHEN"
   Print #fileNo, addTab(2); "                               ("
   Print #fileNo, addTab(2); "                                 (v_previousRecordInfo IS NOT NULL AND v_previousRecordInfo <> v_thisRecordInfo)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (v_previousApplStatus IS NOT NULL AND v_previousApplStatus <> A_APPL_STATUS)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousAgentId, -1) <> A_AGENT_ID)"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousLrtIdStr, '') <> COALESCE(v_lrtIdStr, ''))"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousPsOidStr, '') <> COALESCE(v_psOidStr, ''))"
   Print #fileNo, addTab(2); "                                   OR"
   Print #fileNo, addTab(2); "                                 (COALESCE(v_previousCdUserId, '') <> COALESCE(v_cdUserId, ''))"
   Print #fileNo, addTab(2); "                               )"
   Print #fileNo, addTab(2); "                                 AND"
   Print #fileNo, addTab(2); "                               (mode_in = 1 OR v_previousRecordInfo <> '')"
   Print #fileNo, addTab(2); "                             THEN 1 ELSE 0 END);"
   Print #fileNo, addTab(2); "SET v_previousRecord = v_thisRecord;"
   Print #fileNo, addTab(2); "SET v_thisRecord =  LEFT("
   Print #fileNo, addTab(14); "v_nl ||"
   Print #fileNo, addTab(14); "'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(v_firstSid)), '') || (CASE WHEN v_firstSid <> A_SID THEN ' - ' || COALESCE(RTRIM(CHAR(A_SID)), '') ELSE '' END) || v_nl || v_nl ||"
   Print #fileNo, addTab(14); "'Timestamp               : ' || COALESCE(RTRIM(CHAR(v_firstTimeStamp)), '') || (CASE WHEN v_firstTimeStamp <> A_SNAPSHOT_TIMESTAMP THEN ' - ' || COALESCE(RTRIM(CHAR(A_SNAPSHOT_TIMESTAMP)), '') ELSE '' END) || v_nl ||"
   Print #fileNo, addTab(14); "'Agent                   : ' || COALESCE(RTRIM(CHAR(A_AGENT_ID)), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Application Id          : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Application Status      : ' || COALESCE("; qualFuncNameApplStatus2Str; "(A_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(14); "'Authorization ID        : ' || COALESCE(A_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_NNAME            : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_USERID           : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_WRKSTNNAME       : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_APPLNAME         : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(14); "'CLIENT_ACCTNG           : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl ||"
   Print #fileNo, addTab(14); "v_nl ||"
   Print #fileNo, addTab(14); "(CASE WHEN v_thisRecordInfo = '' THEN '' ELSE '  Inconsistencies       : ' || v_nl || v_nl || v_thisRecordInfo || v_nl END)"
   Print #fileNo, addTab(14); ", 1024"
   Print #fileNo, addTab(13); ");"
   Print #fileNo,

   Print #fileNo, addTab(2); "IF v_outputPreviousRecord = 1 THEN"

   genProcSectionHeader fileNo, "add statement related infos", 3, True
   Print #fileNo, addTab(3); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(4); "FOR snStmtLoop AS snStmtCursor CURSOR FOR"
   Print #fileNo, addTab(5); "SELECT DISTINCT"
   Print #fileNo, addTab(6); "RTRIM(CAST(LEFT(S.STMT_TEXT, 80) AS VARCHAR(80))) AS S_STMT"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "(S.SID >= v_firstSid)"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.SID <= COALESCE(v_previousSid, v_firstSid))"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.STMT_TEXT IS NOT NULL)"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(S.AGENT_ID = A_AGENT_ID)"
   Print #fileNo, addTab(5); "FETCH FIRST 10 ROWS ONLY"

   Print #fileNo, addTab(4); "DO"
   Print #fileNo, addTab(5); "SET v_previousRecord = v_previousRecord ||"
   Print #fileNo, addTab(17); "'      > ' || S_STMT || v_nl;"

   Print #fileNo, addTab(4); "END FOR;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_previousRecord || v_nl || v_delimLine || v_nl"
   Print #fileNo, addTab(3); ");"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(3); "SET v_previousSid   = A_SID;"
   Print #fileNo, addTab(3); "SET v_firstSid      = NULL;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "SET v_previousRecord     = v_thisRecord;"
   Print #fileNo, addTab(2); "SET v_previousRecordInfo = v_thisRecordInfo;"
   Print #fileNo, addTab(2); "SET v_previousApplStatus = A_APPL_STATUS;"
   Print #fileNo, addTab(2); "SET v_previousAgentId    = A_AGENT_ID;"
   Print #fileNo, addTab(2); "SET v_previousLrtIdStr   = v_lrtIdStr;"
   Print #fileNo, addTab(2); "SET v_previousPsOidStr   = v_psOidStr;"
   Print #fileNo, addTab(2); "SET v_previousCdUserId   = v_cdUserId;"
   Print #fileNo, addTab(1); "END FOR;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_outputPreviousRecord = (CASE WHEN (v_firstSid IS NOT NULL) AND (mode_in = 1 OR v_previousRecordInfo <> '') THEN 1 ELSE 0 END);"
   Print #fileNo, addTab(1); "IF v_outputPreviousRecord = 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); pc_tempTabNameSnRecords
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "record"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "v_previousRecord"
   Print #fileNo, addTab(2); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out  = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisAppl, ddlType, , "snapshotId_in", "mode_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for analyzing STATEMENT snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Const maxRecordLengthStmnt = 32000

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotIdStart_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "snapshotIdEnd_in", "BIGINT", True, "(optional) identifies the last snapshot to analyze"
   genProcParm fileNo, "IN", "startTime_in", "TIMESTAMP", True, "(optional) identifies the fime of the first snapshot to analyze"
   genProcParm fileNo, "IN", "endTime_in", "TIMESTAMP", True, "(optional) identifies the fime of the last snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "determines the level of details provided (0=low, 1=medium, 2=high)"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "truncated", "01004"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_headLine1", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_sidDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_agentDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmtDelimLine", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_thisRecord", "CLOB(100M)", "NULL"
   genVarDecl fileNo, "v_thisRecordLck", "CLOB(100M)", "NULL"
   genVarDecl fileNo, "v_stmtNo", "INTEGER", "1"
   genVarDecl fileNo, "v_nl", "CHAR(1)", "NULL"
   genVarDecl fileNo, "v_recordLength", "INTEGER", CStr(maxRecordLengthStmnt)
   genVarDecl fileNo, "v_lastSid", "BIGINT", "-1"
   genVarDecl fileNo, "v_lastAgentId", "BIGINT", "-1"
   genVarDecl fileNo, "v_numLocks", "BIGINT", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR truncated"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempTablesSnapshotAnalysis fileNo, ddlType, maxRecordLengthStmnt

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_nl = CHR(10);"
   Print #fileNo, addTab(1); "SET v_sidDelimLine = REPEAT('#', 100);"
   Print #fileNo, addTab(1); "SET v_agentDelimLine = REPEAT('=', 100);"
   Print #fileNo, addTab(1); "SET v_stmtDelimLine = REPEAT('-', 100);"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over all matching snapshots"
   Print #fileNo, addTab(1); "FOR snStmntLoop AS snAppCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "S.SID                  S_SID,"
   Print #fileNo, addTab(3); "S.SNAPSHOT_TIMESTAMP   S_SNAPSHOT_TIMESTAMP,"
   Print #fileNo, addTab(3); "S.AGENT_ID             S_AGENT_ID,"
   Print #fileNo, addTab(3); "S.STMT_TEXT            S_STMT_TEXT,"
   Print #fileNo, addTab(3); "S.ROWS_READ            S_ROWS_READ,"
   Print #fileNo, addTab(3); "S.ROWS_WRITTEN         S_ROWS_WRITTEN,"
   Print #fileNo, addTab(3); "S.STMT_TYPE            S_STMT_TYPE,"
   Print #fileNo, addTab(3); "S.STMT_OPERATION       S_STMT_OPERATION,"
   Print #fileNo, addTab(3); "S.QUERY_COST_ESTIMATE  S_QUERY_COST_ESTIMATE,"
   Print #fileNo, addTab(3); "S.QUERY_CARD_ESTIMATE  S_QUERY_CARD_ESTIMATE,"
   Print #fileNo, addTab(3); "S.STMT_SORTS           S_STMT_SORTS,"
   Print #fileNo, addTab(3); "S.TOTAL_SORT_TIME      S_TOTAL_SORT_TIME,"
   Print #fileNo, addTab(3); "S.SORT_OVERFLOWS       S_SORT_OVERFLOWS,"
   Print #fileNo, addTab(3); "S.INT_ROWS_DELETED     S_INT_ROWS_DELETED,"
   Print #fileNo, addTab(3); "S.INT_ROWS_UPDATED     S_INT_ROWS_UPDATED,"
   Print #fileNo, addTab(3); "S.INT_ROWS_INSERTED    S_INT_ROWS_INSERTED,"
   Print #fileNo, addTab(3); "S.STMT_START           S_STMT_START,"
   Print #fileNo, addTab(3); "S.STMT_STOP            S_STMT_STOP,"
   Print #fileNo, addTab(3); "COALESCE(S.STMT_STOP, (CASE WHEN S.SNAPSHOT_TIMESTAMP < S.STMT_START THEN S.STMT_START ELSE S.SNAPSHOT_TIMESTAMP END)) - S.STMT_START AS S_ELAPSED_TIME,"
   Print #fileNo, addTab(3); "A.APPL_STATUS          A_APPL_STATUS,"
   Print #fileNo, addTab(3); "A.APPL_ID              A_APPL_ID,"
   Print #fileNo, addTab(3); "A.AUTH_ID              A_AUTH_ID,"
   Print #fileNo, addTab(3); "A.CLIENT_NNAME         A_CLIENT_NNAME,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_USERID  A_TPMON_CLIENT_USERID,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_WKSTN   A_TPMON_CLIENT_WKSTN,"
   Print #fileNo, addTab(3); "A.TPMON_CLIENT_APP     A_TPMON_CLIENT_APP,"
   Print #fileNo, addTab(3); "A.TPMON_ACC_STR        A_TPMON_ACC_STR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotStatement; " S"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotApplInfo; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "S.SID = A.SID"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "S.AGENT_ID = A.AGENT_ID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(snapshotIdStart_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SID >= snapshotIdStart_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(snapshotIdEnd_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SID <= snapshotIdEnd_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(startTime_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SNAPSHOT_TIMESTAMP >= startTime_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(endTime_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.SNAPSHOT_TIMESTAMP <= endTime_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(agentId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S.AGENT_ID = agentId_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(mode_in > 1)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(STMT_TEXT IS NOT NULL)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "S.SID        ASC,"
   Print #fileNo, addTab(3); "S.AGENT_ID   ASC,"
   Print #fileNo, addTab(3); "S.STMT_START ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "retrieve LOCK-infos for previous Statement", 2, True
   Print #fileNo, addTab(2); "IF (v_lastSid <> S_SID OR v_lastAgentId <> S_AGENT_ID) AND v_lastSid > 0 AND v_lastAgentId > 0 THEN"
   genSaveLockInfoDdl fileNo, 3, g_qualTabNameSnapshotLock, ddlType
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_thisRecord = v_nl;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_lastSid <> S_SID THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_nl ||"
   Print #fileNo, addTab(12); " v_sidDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Snapshot ID             : ' || COALESCE(RTRIM(CHAR(S_SID                            )), '') || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " '  Timestamp             : ' || COALESCE(RTRIM(CHAR(S_SNAPSHOT_TIMESTAMP             )), '') || v_nl || v_nl"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastSid = S_SID;"
   Print #fileNo, addTab(3); "SET v_lastAgentId = -1;"
   Print #fileNo, addTab(3); "SET v_stmtNo = 1;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_lastAgentId <> S_AGENT_ID THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl ||"
   Print #fileNo, addTab(12); " v_agentDelimLine || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " 'Agent Id                : ' || COALESCE(RTRIM(CHAR(S_AGENT_ID                       )), '') || v_nl ||"
   Print #fileNo, addTab(12); " v_nl ||"
   Print #fileNo, addTab(12); " '  Application Id        : ' || COALESCE(RTRIM(CHAR(A_APPL_ID)), '') || v_nl ||"
   Print #fileNo, addTab(12); " '  Application Status    : ' || COALESCE("; qualFuncNameApplStatus2Str; "(A_APPL_STATUS), '') || v_nl ||"
   Print #fileNo, addTab(12); " '  Authorization ID      : ' || COALESCE(A_AUTH_ID, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_NNAME          : ' || COALESCE(A_CLIENT_NNAME, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_USERID         : ' || COALESCE(A_TPMON_CLIENT_USERID, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_WRKSTNNAME     : ' || COALESCE(A_TPMON_CLIENT_WKSTN, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_APPLNAME       : ' || COALESCE(A_TPMON_CLIENT_APP, '') || v_nl ||"
   Print #fileNo, addTab(12); " '  CLIENT_ACCTNG         : ' || COALESCE(A_TPMON_ACC_STR, '') || v_nl"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "SET v_lastAgentId = S_AGENT_ID;"
   Print #fileNo, addTab(3); "SET v_stmtNo = 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_thisRecord = v_thisRecord || v_nl ||"
   Print #fileNo, addTab(3); " v_stmtDelimLine || v_nl ||"
   Print #fileNo, addTab(3); " v_nl ||"
   Print #fileNo, addTab(12); " 'Statement               : ' || COALESCE(RTRIM(CHAR(v_stmtNo)), '') || v_nl ||"
   Print #fileNo, addTab(12); "   v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Stop        : ' || COALESCE(CHAR(S_STMT_STOP ),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Time Elapsed          : ' || COALESCE(CAST(CAST(SECOND(S_ELAPSED_TIME) + 60 * (MINUTE(S_ELAPSED_TIME) + 60 * (HOUR(S_ELAPSED_TIME) + 24 * DAY(S_ELAPSED_TIME))) + CAST(MICROSECOND(S_ELAPSED_TIME)AS DECIMAL(20,6))/CAST(1000000 AS DECIMAL(20,6)) AS DECIMAL(15,6)) AS CHAR(16)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Type                  : ' || COALESCE("; qualFuncNameStmntType2Str; "(S_STMT_TYPE),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Op                    : ' || COALESCE("; qualFuncNameStmntOperation2Str; "(S_STMT_OPERATION),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Start       : ' || COALESCE(CHAR(S_STMT_START),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Read             : ' || COALESCE(RTRIM(CHAR(S_ROWS_READ)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Written          : ' || COALESCE(RTRIM(CHAR(S_ROWS_WRITTEN)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Query Cost (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_COST_ESTIMATE)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Query Card (est.)     : ' || COALESCE(RTRIM(CHAR(S_QUERY_CARD_ESTIMATE)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Statement Sorts       : ' || COALESCE(RTRIM(CHAR(S_STMT_SORTS)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Total Sort Time       : ' || COALESCE(RTRIM(CHAR(S_TOTAL_SORT_TIME)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Sort Overflows        : ' || COALESCE(RTRIM(CHAR(S_SORT_OVERFLOWS)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Deleted (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_DELETED)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Updated (int.)   : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_UPDATED)),'') || v_nl ||"
   Print #fileNo, addTab(12); " '  Rows Inserted (int.)  : ' || COALESCE(RTRIM(CHAR(S_INT_ROWS_INSERTED)),'') || v_nl"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Statement Text          : ' || v_nl || v_nl || COALESCE(RTRIM(LEFT(REPLACE(S_STMT_TEXT, CHR(10), ' '), 120)), '')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_thisRecord = v_thisRecord || v_nl || v_stmtDelimLine || v_nl || v_nl ||"
   Print #fileNo, addTab(12); " 'Statement Text          : ' || v_nl || v_nl || COALESCE(S_STMT_TEXT, '');"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmtNo = v_stmtNo + 1;"
   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO "; pc_tempTabNameSnRecords; "(record) VALUES (LEFT(CLOB(v_thisRecord || v_nl),  v_recordLength));"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   genProcSectionHeader fileNo, "retrieve LOCK-infos for last Statement", 1, True
   Print #fileNo, addTab(1); "IF v_lastSid > 0 AND v_lastAgentId > 0 THEN"
   genSaveLockInfoDdl fileNo, 2, g_qualTabNameSnapshotLock, ddlType
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "return records to application"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "declare cursor", 2, True
   Print #fileNo, addTab(2); "DECLARE recordCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "record"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); pc_tempTabNameSnRecords
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN recordCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotIdStart_in", "snapshotIdEnd_in", "#startTime_in", "#endTime_in", "agentId_in", "mode_in", "recordCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "recordCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisStatement; "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, 0, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for analyzing STATEMENT snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysisStatement
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the first snapshot to analyze"
   genProcParm fileNo, "IN", "agentId_in", "INTEGER", True, "(otional) identifies the agent to analyze"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "determines the level of details provided (0=low, 1=medium, 2=high)"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "mode_in", "recordCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; g_qualProcNameGetSnapshotAnalysisStatement; "(snapshotId_in, snapshotId_in, CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP), agentId_in, mode_in, recordCount_out);"
 
   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysisStatement, ddlType, , "snapshotId_in", "agentId_in", "mode_in", "recordCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for analyzing snapshot data
   ' ####################################################################################################################

   printSectionHeader "SP for analyzing snapshot data", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); g_qualProcNameGetSnapshotAnalysis
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "snapshotId_in", g_dbtOid, True, "(optional) identifies the snapshot to analyze"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of records retrieved"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 15"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_recordCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genSpLogProcEnter fileNo, g_qualProcNameGetSnapshotAnalysis, ddlType, , "snapshotId_in", "recordCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"

   genProcSectionHeader fileNo, "loop over all snapshot types supporting analysis"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "REPLACE(PROCNAME, 'GETSNAPSHOT', 'GETSNAPSHOTANALYSIS') AS PROCNAME"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotType
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "SUPPORTANALYSIS = "; gc_dbTrue
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; getSchemaName(g_qualProcNameGetSnapshotAnalysis); ".' || PROCNAME || '(?,0,?)';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_recordCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "snapshotId_in"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + v_recordCount;"

   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, g_qualProcNameGetSnapshotAnalysis, ddlType, , "snapshotId_in", "recordCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub genSaveLockInfoDdl( _
   fileNo As Integer, _
   indent As Integer, _
   ByRef g_qualTabNameSnapshotLock As String, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   Dim qualFuncNameLockObjType2StrS As String
   qualFuncNameLockObjType2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockObjType2StrS, ddlType)
   Dim qualFuncNameLockStatus2StrS As String
   qualFuncNameLockStatus2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockStatus2Str & "_S", ddlType)
   Dim qualFuncNameLockMode2StrS As String
   qualFuncNameLockMode2StrS = genQualFuncName(g_sectionIndexDbMonitor, udfnLockMode2Str & "_S", ddlType)

   Dim useApiV9 As Boolean
   useApiV9 = False
   If snapshotApiVersion = "9.7" Then
     useApiV9 = True
   End If

   Print #fileNo, addTab(indent + 0); "SET v_thisRecordLck = '';"
   Print #fileNo, addTab(indent + 0); "SET v_numLocks      = 0;"
   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "FOR lckLoop AS lckCursor CURSOR FOR"
   Print #fileNo, addTab(indent + 1); "WITH"
   Print #fileNo, addTab(indent + 2); "V_LOCK"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "L_LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_MODE,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_STATUS,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_ESCALATION,"
   Print #fileNo, addTab(indent + 2); "L_TABNAME,"
   Print #fileNo, addTab(indent + 2); "L_TABSCHEMA,"
   Print #fileNo, addTab(indent + 2); "L_TBSP_NAME,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_COUNT"
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 1); "AS"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); "L.LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_MODE,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_STATUS,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_ESCALATION,"
 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TABNAME", "TABLE_NAME"); ","
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TABSCHEMA", "TABLE_SCHEMA"); ","
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TBSP_NAME", "TABLESPACE_NAME"); ","
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(indent + 3); "L.TABNAME,"
 ' Print #fileNo, addTab(indent + 3); "L.TABSCHEMA,"
 ' Print #fileNo, addTab(indent + 3); "L.TBSP_NAME,"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 3); "COUNT(*)"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); g_qualTabNameSnapshotLock; " L"
   Print #fileNo, addTab(indent + 2); "WHERE"
   Print #fileNo, addTab(indent + 3); "L.SID = v_lastSid"
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); "L.AGENT_ID = v_lastAgentId"
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); "("
   Print #fileNo, addTab(indent + 4); "(mode_in > 1)"
   Print #fileNo, addTab(indent + 5); "OR"
   Print #fileNo, addTab(indent + 4); "(L.LOCK_MODE NOT IN(1,2,4,6))"
   Print #fileNo, addTab(indent + 3); ")"
   Print #fileNo, addTab(indent + 2); "GROUP BY"
   Print #fileNo, addTab(indent + 3); "L.LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_MODE,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_STATUS,"
   Print #fileNo, addTab(indent + 3); "L.LOCK_ESCALATION,"
 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TABNAME", "TABLE_NAME"); ","
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TABSCHEMA", "TABLE_SCHEMA"); ","
   Print #fileNo, addTab(indent + 3); "L."; IIf(useApiV9, "TBSP_NAME", "TABLESPACE_NAME")
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(indent + 3); "L.TABNAME,"
 ' Print #fileNo, addTab(indent + 3); "L.TABSCHEMA,"
 ' Print #fileNo, addTab(indent + 3); "L.TBSP_NAME"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "L_LOCK_OBJECT_TYPE,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_MODE,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_STATUS,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_ESCALATION,"
   Print #fileNo, addTab(indent + 2); "L_TABNAME,"
   Print #fileNo, addTab(indent + 2); "L_TABSCHEMA,"
   Print #fileNo, addTab(indent + 2); "L_TBSP_NAME,"
   Print #fileNo, addTab(indent + 2); "L_LOCK_COUNT,"
   Print #fileNo, addTab(indent + 2); "ROWNUMBER() OVER (ORDER BY L_TABSCHEMA ASC, L_TABNAME ASC) L_ROWNUM"
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); "V_LOCK"
   Print #fileNo, addTab(indent + 1); "ORDER BY L_ROWNUM"
   Print #fileNo, addTab(indent + 0); "DO"
   Print #fileNo, addTab(indent + 1); "SET v_thisRecordLck = v_thisRecordLck ||"
   Print #fileNo, addTab(indent + 12); "'  ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR(L_ROWNUM),''),4) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR("; qualFuncNameLockObjType2StrS; "(L_LOCK_OBJECT_TYPE)),''),13) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR("; qualFuncNameLockStatus2StrS; "(L_LOCK_STATUS)),''),3) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR("; qualFuncNameLockMode2StrS; "(L_LOCK_MODE)),''),3) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR(L_LOCK_ESCALATION),''),3) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(CHAR(L_LOCK_COUNT),''),7) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(L_TBSP_NAME,''),15) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "CHAR(COALESCE(RTRIM(L_TABSCHEMA) || '.' || L_TABNAME,''),60) || ' ' ||"
   Print #fileNo, addTab(indent + 12); "v_nl"
   Print #fileNo, addTab(indent + 1); ";"
   Print #fileNo, addTab(indent + 1); "SET v_numLocks = v_numLocks + 1;"
   Print #fileNo, addTab(indent + 0); "END FOR;"
   Print #fileNo,

   Print #fileNo, addTab(indent + 0); "IF v_numLocks > 0 THEN"
   Print #fileNo, addTab(indent + 1); "INSERT INTO "; pc_tempTabNameSnRecords; "(record) VALUES(LEFT(CLOB(v_nl || v_stmtDelimLine || v_nl || v_nl ||"; _
                                      " 'Locks                   : ' || v_nl || v_nl || v_thisRecordLck || v_nl),  v_recordLength));"
   Print #fileNo, addTab(indent + 0); "END IF;"
 End Sub
 
 
 Private Sub genGetSnapshotForXyzDdlV( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   ByRef spName As String, _
   ByRef viewName As String, _
   ByRef viewShortName As String, _
   ByRef classSectionName As String, _
   ByRef className As String, _
   ByRef forWhom As String, _
   ByRef db2UdfName As String, _
   ByRef colFltrUdfName As String, _
   ByRef qualIdSequenceName As String, _
   ByRef g_qualTabNameSnapshotType As String, _
   ByRef g_qualTabNameSnapshotFilter As String, _
   ByRef g_qualTabNameSnapshotHandle As String, _
   Optional isApplSpecific As Boolean = True, _
   Optional useUdfDbParam As Boolean = True _
 )
   Dim transformation As AttributeListTransformation

   Dim largeTables As Boolean
   largeTables = False
   If Left(snapshotApiVersion, 1) = "9" Then
     largeTables = True
   End If

   ' ####################################################################################################################
   ' #    SP for retrieving snapshot of specified type
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexDbMonitor, spName, ddlType)

   printSectionHeader "SP for retrieving snapshot on " & forWhom, fileNo

   Dim classIndexSnapshot As Integer
   classIndexSnapshot = getClassIndexByName(classSectionName, className)
   Dim qualTabNameSnapshot As String
   qualTabNameSnapshot = genQualTabNameByClassIndex(classIndexSnapshot, ddlType)

   Dim unqualTabNameSnapshot As String
   unqualTabNameSnapshot = getUnqualObjName(qualTabNameSnapshot)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   genProcParm fileNo, "IN", "useLogging_in", g_dbtBoolean, True, "'ACTIVATE NOT LOGGED INITIALLY' is no longer supported"
   If isApplSpecific Then
     genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   End If
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "tableNotAccessible", "55019"
   genCondDecl fileNo, "tableDoesNotExist", "42704"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_snapshotTs", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_myLevel", "INTEGER", "0"
   genVarDecl fileNo, "v_filter", "VARCHAR(" & IIf(largeTables, "8000", "4000") & ")", "''"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(" & IIf(largeTables, "8192", "4096") & ")", "NULL"
   genVarDecl fileNo, "v_stmntTxtNoLog", "VARCHAR(512)", "NULL"
   genVarDecl fileNo, "v_stmntTxtCrTab", "VARCHAR(" & IIf(largeTables, "12000", "10000") & ")", "NULL"
   genVarDecl fileNo, "v_recreateTable", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_grantCount", "INTEGER", "0"
 
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tableNotAccessible"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_recreateTable = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tableDoesNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_recreateTable = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", IIf(isApplSpecific, "agentId_in", ""), "'category_in", "level_in", "rowCount_out"

   genProcSectionHeader fileNo, "determine snapshot timestamp"
   Print #fileNo, addTab(1); "SET v_snapshotTs = CURRENT TIMESTAMP;"

   genProcSectionHeader fileNo, "determine this procedure's level"
   Print #fileNo, addTab(1); "SET v_myLevel = COALESCE((SELECT LEVEL FROM "; g_qualTabNameSnapshotType; " WHERE PROCNAME = '"; UCase(spName); "'), 0);"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "set ISOLATION LEVEL to 'UNCOMMITED READ'"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = UR;"

   genProcSectionHeader fileNo, "collect snapshot data"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "create snapshot ID if none is provided", 2, True
   Print #fileNo, addTab(2); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(3); "SET snapshotId_inout = NEXTVAL FOR "; qualIdSequenceName; ";"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "determine collect-filter to apply", 2
   Print #fileNo, addTab(2); "FOR filterLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "F.COLLECTFILTER AS FILTER"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameSnapshotFilter; " F"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "F.TABLENAME = '"; unqualTabNameSnapshot; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "F.LEVEL <= level_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "F.COLLECTFILTER IS NOT NULL"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';"

   Print #fileNo, addTab(2); "END FOR;"

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   initAttributeTransformation transformation, 0, , True
   transformation.trimRight = False
   genTransformedAttrListForEntityWithColReuse classIndexSnapshot, eactClass, transformation, tabColumns, fileNo, ddlType, , , 3, , , edomNone

   genProcSectionHeader fileNo, "retrieve data", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'INSERT INTO "; qualTabNameSnapshot; "(' ||"
   Dim k As Integer
   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(4); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(3); "') ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"

   For k = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(k).columnName = "SID" Then
         Print #fileNo, addTab(4); "CHAR(snapshotId_inout) ||"; IIf(k = tabColumns.numDescriptors, "", " ',' ||")
       ElseIf tabColumns.descriptors(k).columnName = "SNAPSHOT_TIMESTAMP" Then
         Print #fileNo, addTab(4); "'TIMESTAMP(''' || CHAR(v_snapshotTs) ||"; IIf(k = tabColumns.numDescriptors, "", " '''),' ||")
       Else
         Print #fileNo, addTab(4); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, " ", ","); "' ||"
       End If
   Next k
   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(4); "'TABLE(SYSPROC."; UCase(db2UdfName); "("; IIf(useUdfDbParam, "CURRENT SERVER,", ""); "-1)) AS SN ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"

   Print #fileNo, addTab(4); "(CASE WHEN v_filter = '' THEN '(0=0) ' ELSE '(' || v_filter || ') ' END)"

   If isApplSpecific Then
     Print #fileNo, addTab(4); "|| (CASE WHEN agentId_in IS NULL THEN '' ELSE ' AND (AGENT_ID = ' || CHAR(agentId_in) || ')' END)"
   End If
   Print #fileNo, addTab(2); ";"
   Print #fileNo,

   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_recreateTable = 1 THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxtCrTab = 'DROP TABLE "; qualTabNameSnapshot; "';"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxtCrTab;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxtCrTab ="
   Print #fileNo, addTab(5); "'CREATE TABLE ' ||"
   Print #fileNo, addTab(6); "'"; qualTabNameSnapshot; " ' ||"
   Print #fileNo, addTab(5); "'(' ||"

     Dim i As Integer
     For i = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(6); "'"; _
                                   genTransformedAttrDeclByDomain(tabColumns.descriptors(i).acmAttributeName, "???", eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformation, _
                                   eactClass, classIndexSnapshot, IIf(tabColumns.descriptors(i).isNullable, "", "NOT NULL"), False, ddlType, , , , , 0); _
                                   IIf(i < tabColumns.numDescriptors, ",", ""); "' ||"
     Next i

     Print #fileNo, addTab(5); "')' ||"

     If ddlType = edtPdm Then
       If g_classes.descriptors(classIndexSnapshot).tabSpaceData <> "" Then
         Print #fileNo, addTab(5); "' IN "; genTablespaceNameByIndex(g_classes.descriptors(classIndexSnapshot).tabSpaceIndexData); "' ||"
       End If
       If g_classes.descriptors(classIndexSnapshot).tabSpaceLong <> "" Then
         Print #fileNo, addTab(5); "' LONG IN "; genTablespaceNameByIndex(g_classes.descriptors(classIndexSnapshot).tabSpaceIndexLong); "' ||"
       End If
       If g_classes.descriptors(classIndexSnapshot).tabSpaceIndex <> "" Then
         Print #fileNo, addTab(5); "' INDEX IN "; genTablespaceNameByIndex(g_classes.descriptors(classIndexSnapshot).tabSpaceIndexIndex); "' ||"
       End If

       If g_classes.descriptors(classIndexSnapshot).useValueCompression Then
         Print #fileNo, addTab(5); "' VALUE COMPRESSION' ||"
       End If
       Print #fileNo, addTab(5); "' COMPRESS YES'"
     End If
     Print #fileNo, addTab(3); ";"

   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxtCrTab;"
 
   Dim qualProcedureNameSetGrants As String
   qualProcedureNameSetGrants = genQualProcName(g_sectionIndexDbAdmin, spnGrant & "Fltr", ddlType)

   genProcSectionHeader fileNo, "set GRANTs on new table", 3
   Print #fileNo, addTab(3); "CALL "; qualProcedureNameSetGrants; _
                                  "(2, '"; getSchemaName(qualTabNameSnapshot); "', '"; getUnqualObjName(qualTabNameSnapshot); "', v_grantCount);"
 
   genProcSectionHeader fileNo, "try again to retrieve data", 3
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "ELSE"
   genProcSectionHeader fileNo, "use 'last' snapshot ID if none is provided", 2, True
   Print #fileNo, addTab(2); "IF snapshotId_inout IS NULL THEN"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "MAX(ID)"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "snapshotId_inout"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameSnapshotHandle
   Print #fileNo, addTab(3); "WITH UR;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "register snapshot ID"
   Print #fileNo, addTab(1); "IF (snapshotId_inout IS NOT NULL) AND (NOT EXISTS (SELECT 1 FROM "; g_qualTabNameSnapshotHandle; " WHERE ID = snapshotId_inout)) THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotHandle
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "ID,"
   Print #fileNo, addTab(3); "SNAPSHOT_TIMESTAMP"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "snapshotId_inout,"
   Print #fileNo, addTab(3); "CURRENT TIMESTAMP"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF (mode_in <= 1) and (level_in <= v_myLevel) THEN"
   Print #fileNo, addTab(2); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_stmntTxtRowCount", "VARCHAR(2048)", "NULL", 3
 
   genProcSectionHeader fileNo, "declare statements", 3
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 3
   genVarDecl fileNo, "v_stmntRowCount", "STATEMENT", , 3
 
   genProcSectionHeader fileNo, "declare cursor", 3
   Print #fileNo, addTab(3); "DECLARE resultCursor CURSOR WITH RETURN TO CLIENT FOR v_stmnt;"
   Print #fileNo, addTab(3); "DECLARE rowCountCursor CURSOR FOR v_stmntRowCount;"

   genProcSectionHeader fileNo, "determine select-filter to apply", 3
   Print #fileNo, addTab(3); "SET v_filter = '';"
   Print #fileNo, addTab(3); "FOR filterLoop AS"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "F.SELECTFILTER AS FILTER"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameSnapshotFilter; " F"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "F.TABLENAME = '"; unqualTabNameSnapshot; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "F.LEVEL <= level_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "F.SELECTFILTER IS NOT NULL"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); "DO"

   Print #fileNo, addTab(4); "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';"

   Print #fileNo, addTab(3); "END FOR;"

   genProcSectionHeader fileNo, "determine SELECT statement", 3
   Print #fileNo, addTab(3); "IF snapshotId_inout IS NULL THEN"

   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); colFltrUdfName; "('"; UCase(className); "', category_in, level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'"; qualTabNameSnapshot; "' || (CASE v_filter WHEN '' THEN '' ELSE ' WHERE ' || v_filter END)"
   Print #fileNo, addTab(4); ";"

   Print #fileNo, addTab(3); "ELSE"
 
   Print #fileNo, addTab(4); "SET v_stmntTxt ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); colFltrUdfName; "('"; UCase(className); "', category_in, level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'"; qualTabNameSnapshot; "' || ' ' ||"
   Print #fileNo, addTab(5); "'WHERE ' ||"
   Print #fileNo, addTab(6); "'(SID = ' || RTRIM(CHAR(snapshotId_inout)) || ')' || (CASE v_filter WHEN '' THEN '' ELSE ' AND (' || v_filter || ')' END)"
   Print #fileNo, addTab(4); ";"
 
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in = 0 THEN"
   genProcSectionHeader fileNo, "count the number of rows", 4, True

   Print #fileNo, addTab(4); "SET v_stmntTxtRowCount ="
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(7); "'COUNT(*) ' ||"
   Print #fileNo, addTab(6); "'FROM ' ||"
   Print #fileNo, addTab(7); "'"; qualTabNameSnapshot; " ' ||"
   Print #fileNo, addTab(6); "'WHERE ' ||"
   Print #fileNo, addTab(5); "'(SID = ' || RTRIM(CHAR(snapshotId_inout)) || ')' || (CASE v_filter WHEN '' THEN '' ELSE ' AND (' || v_filter || ')' END)"
   Print #fileNo, addTab(4); ";"

   Print #fileNo,
   Print #fileNo, addTab(4); "PREPARE v_stmntRowCount FROM v_stmntTxtRowCount;"
   Print #fileNo, addTab(4); "OPEN rowCountCursor;"
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "rowCountCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "rowCount_out"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE rowCountCursor WITH RELEASE;"

   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resultCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "reset ISOLATION LEVEL"
   Print #fileNo, addTab(1); "SET CURRENT ISOLATION = RESET;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "useLogging_in", IIf(isApplSpecific, "agentId_in", ""), "'category_in", "level_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for retrieving snapshot on " & forWhom, fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   If isApplSpecific Then
     genProcParm fileNo, "IN", "agentId_in", "BIGINT", True, "(optional) id of the agent to filter snapshot data for"
   End If
   genProcParm fileNo, "IN", "category_in", "VARCHAR(10)", True, "(optional) category to use for column filtering"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of snapshot records listed (mode_in = 0) or created (mode_in = 1 resp. 2)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True
 
   If isApplSpecific Then
     genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "rowCount_out"
   Else
     genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "'category_in", "level_in", "rowCount_out"
   End If

   genProcSectionHeader fileNo, "call procedure", , True
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(snapshotId_inout, mode_in, 1, "; IIf(isApplSpecific, "agentId_in,", ""); "category_in, level_in, rowCount_out);"

   If isApplSpecific Then
     genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "agentId_in", "'category_in", "level_in", "rowCount_out"
   Else
     genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in", "'category_in", "level_in", "rowCount_out"
   End If

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for retrieving snapshot on " & forWhom & " (short parameter list)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "INOUT", "snapshotId_inout", g_dbtOid, True, "(optional) identifies the snapshot"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", False, "'0' list existing records, '1' retrieve snapshot and list result, '2' retrieve snapshot only"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   If isApplSpecific Then
     genVarDecl fileNo, "v_agentId", "BIGINT", "NULL"
   End If
   genVarDecl fileNo, "v_category", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(snapshotId_inout, mode_in, "; IIf(isApplSpecific, "v_agentId,", ""); "v_category, v_level, v_rowCount);"
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "snapshotId_inout", "mode_in"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for creating snapshot-view of specified type
   ' ####################################################################################################################

   Dim qualFuncNameSnCols As String
   qualFuncNameSnCols = genQualFuncName(g_sectionIndexDbMonitor, udfnSnapshotCols, ddlType)

   Dim qualViewName As String
   qualViewName = genQualViewName(g_sectionIndexDbMonitor, viewName, viewShortName, ddlType)

   qualProcName = genQualProcName(g_sectionIndexDbMonitor, viewName, ddlType, , , "GenView")

   printSectionHeader "SP for creating snapshot-view on " & forWhom, fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "level_in", "INTEGER", False, "(optional) level to use for column filtering"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "viewAlreadyExists", "42710"
   genCondDecl fileNo, "viewDoesNotExist", "42704"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_filter", "VARCHAR(" & IIf(largeTables, "8000", "4000") & ")", "''"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(" & IIf(largeTables, "8192", "4096") & ")", "NULL"

   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR viewAlreadyExists"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR viewDoesNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, , "level_in"

   genProcSectionHeader fileNo, "determine collect-filter to apply", 1
   Print #fileNo, addTab(1); "FOR filterLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "F.COLLECTFILTER AS FILTER"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameSnapshotFilter; " F"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "F.TABLENAME = '"; db2UdfName; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "F.LEVEL <= level_in"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_filter = v_filter || (CASE v_filter WHEN '' THEN '' ELSE ' AND ' END) || '(' || FILTER || ')';"

   Print #fileNo, addTab(1); "END FOR;"

   tabColumns = nullEntityColumnDescriptors

   initAttributeTransformation transformation, 0, , True
   genTransformedAttrListForEntityWithColReuse classIndexSnapshot, eactClass, transformation, tabColumns, fileNo, ddlType, , , 3, , , edomNone

   genProcSectionHeader fileNo, "drop view - if exists", 1
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DROP VIEW "; qualViewName; "';"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader fileNo, "create view", 1
   Print #fileNo, addTab(1); "SET v_stmntTxt ="
   Print #fileNo, addTab(2); "'CREATE VIEW "; qualViewName; " ' ||"

   Print #fileNo, addTab(2); "'AS ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"

   Print #fileNo, addTab(4); qualFuncNameSnCols; "('"; unqualTabNameSnapshot; "', CAST(NULL AS VARCHAR(1)), level_in, CAST(NULL AS VARCHAR(1))) || ' ' ||"

   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(4); "'"; qualTabNameSnapshot; " ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"

   Print #fileNo, addTab(4); "(CASE WHEN v_filter = '' THEN '(0=0) ' ELSE '(' || v_filter || ') ' END) ||"

   Print #fileNo, addTab(2); "')'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "level_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Sub genDbAdminDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   If thisOrgIndex < 0 Or thisPoolIndex < 0 Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStepAdmin, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)
 
   Dim qualProcedureNameCleanupGlobal As String
   qualProcedureNameCleanupGlobal = genQualProcName(g_sectionIndexDbAdmin, spnCleanData, ddlType)
   Dim qualProcedureNameCleanupLocal As String
   qualProcedureNameCleanupLocal = genQualProcName(g_sectionIndexAliasLrt, spnCleanData, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader "SP for executing data cleanup jobs (wrapper with unique name / no overloadding)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanupLocal
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "IN", "parameter1_in", "VARCHAR(30)", True, "(optional) parameter 1 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter2_in", "VARCHAR(30)", True, "(optional) parameter 2 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter3_in", "VARCHAR(30)", True, "(optional) parameter 3 to use in condition term for job"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, qualProcedureNameCleanupLocal, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanupGlobal; "(mode_in, jobCategory_in, jobName_in, level_in, parameter1_in, parameter2_in, parameter3_in, stmntCount_out, rowCount_out);"
 
   genSpLogProcExit fileNo, qualProcedureNameCleanupLocal, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbEventMonitoringDdl( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

 
   ' ####################################################################################################################
   ' #    SP for creating event monitor for locking
   ' ####################################################################################################################

   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); "VL6CMON.EVENTMONITORCREATE"
   Print #fileNo, addTab(0); "("

   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "eventMonitorCount_out", "INTEGER", False, "number of event monitor created"

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "doesNotExist", "42704"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_returnResult", g_dbtBoolean, gc_dbTrue
   genVarDecl fileNo, "v_tableCount", "INTEGER", "0"
   genVarDecl fileNo, "v_viewCount", "INTEGER", "0"
   genVarDecl fileNo, "v_grantCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(30000)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler", 1, True
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   genProcSectionHeader fileNo, "just ignore", 2, True
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_returnResult = 0;  -- just fill the table"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader fileNo, "temporary table for statements", 1, True
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.StatementsEMC"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo     INTEGER,"
   Print #fileNo, addTab(2); "statement VARCHAR(30000)"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "ON ROLLBACK PRESERVE ROWS;"

   genProcSectionHeader fileNo, "SET output parameter", 1, True
   Print #fileNo, addTab(1); "SET eventMonitorCount_out = 1;"

   genProcSectionHeader fileNo, "DROP-Statement for event monitor ""EVMON_LOCKING """, 1, True
   Print #fileNo, addTab(1); "SET v_stmntTxt ="
   Print #fileNo, addTab(2); "'SET EVENT MONITOR evmon_locking STATE 0';"

   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); "SESSION.StatementsEMC"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "statement"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "100000 + 1,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DROP EVENT MONITOR EVMON_LOCKING ';"

   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); "SESSION.StatementsEMC"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "statement"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "200000 + 1,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "CREATE-Statement for event monitor ""EVMON_LOCKING """, 1, True
   Print #fileNo, addTab(1); "SET v_stmntTxt ="
   Print #fileNo, addTab(2); "'CREATE EVENT MONITOR EVMON_LOCKING FOR LOCKING WRITE TO UNFORMATTED EVENT TABLE ( TABLE VL6CMON.EVMON_LOCKING IN MONITOR ) AUTOSTART ';"

   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); "SESSION.StatementsEMC"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "statement"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "300000 + 1,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "return result to application", 0, True
   Print #fileNo, addTab(1); "IF mode_in <= 1 AND v_returnResult = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CHR(10) || statement || CHR(10) || '@' || CHR(10) AS statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SESSION.StatementsEMC"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3, True
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for cleaning up event monitor data
   ' ####################################################################################################################

   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); "VL6CMON.EVENTMONITORCLEAR"
   Print #fileNo, addTab(0); "("

   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "before_in", "TIMESTAMP", True, "(optionally) only event monitor data before this timestamp is cleaned up"
   genProcParm fileNo, "IN", "commitCount_in", "INTEGER", True, "number of rows to delete before commit (0 = no commit, -1 disable logging + final commit)"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of event monitor tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(1024)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "temporary table for statements", 1, True
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.StatementsEMC"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo     INTEGER,"
   Print #fileNo, addTab(2); "statement VARCHAR(400)"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED"
   Print #fileNo, addTab(1); "ON ROLLBACK PRESERVE ROWS"
   Print #fileNo, addTab(1); "WITH REPLACE;"

   genProcSectionHeader fileNo, "initialize variables", 1, True
   Print #fileNo, addTab(1); "SET commitCount_in = COALESCE(commitCount_in, 0);"
   Print #fileNo, addTab(1); "SET tabCount_out   = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out   = 0;"

   genProcSectionHeader fileNo, "cleanup event monitor table", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM VL6CMON.evmon_locking';"
   Print #fileNo, addTab(2); "IF before_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' WHERE ( EVENT_TIMESTAMP <= ''' || RTRIM( CHAR( before_in ) ) || ''')';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"

   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.StatementsEMC"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "seqNo,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "tabCount_out,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "IF commitCount_in > 0 THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = REPLACE(v_stmntTxt, 'DELETE FROM', 'DELETE FROM (SELECT * FROM') || ' FETCH FIRST ' || RTRIM(CHAR(commitCount_in)) || ' ROWS ONLY)';"
   Print #fileNo, addTab(4); "SET v_rowCount = commitCount_in;"

   Print #fileNo, addTab(4); "WHILE v_rowCount = commitCount_in DO"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(5); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(5); "COMMIT;"
   Print #fileNo, addTab(4); "END WHILE;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "commit if logging is disabled (to minimize risk of unaccessible table)", 3, True
   Print #fileNo, addTab(3); "IF commitCount_in < 0 THEN"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"


   genProcSectionHeader fileNo, "return result to application", 1, True
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SESSION.StatementsEMC"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3, True
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim



 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
