 Attribute VB_Name = "M94_DBAdmin_Partitioning"
 ' ### IF IVK ###
 Option Explicit
 
 Enum PartitionType
   ptNone = 0
   ptPsOid = 1
   ptPsOidCid = 2
   ptDivOid = 4
 End Enum
 
 Private Const processingStepAdmin = 4
 
 
 Sub genDbAdminPartitioningDdl( _
   ddlType As DdlTypeId _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStepAdmin, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   genDbAdminPartitioningByPsDdlByDdlType fileNo, ddlType
   genDbAdminPartitioningByDivDdlByDdlType fileNo, ddlType
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genDbAdminPartitioningByPsDdlByDdlType( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   ' ####################################################################################################################
   ' #    SP for configuring table partitioning (by PS_OID)
   ' ####################################################################################################################

   Dim andOrFlag As Boolean
   andOrFlag = False
 
   Dim qualProcedureNameTablePartCfg As String
   qualProcedureNameTablePartCfg = genQualProcName(g_sectionIndexDbAdmin, spnSetTablePartCfgPs, ddlType)

   printSectionHeader "SP for configuring table partitioning (by " & g_anPsOid & ")", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTablePartCfg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "toggle_in", g_dbtBoolean, True, "if set to '1' switch on table partitioning, otherwise switch off"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL"
   genVarDecl fileNo, "v_colDeclTxt", "VARCHAR(20000)", "NULL"
   genVarDecl fileNo, "v_partitionClauseTxt", "VARCHAR(100)", "''"
   genVarDecl fileNo, "v_diagnostics", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_foundPartitionCrit", g_dbtBoolean, gc_dbFalse
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "altObjError", "38553"

   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR altObjError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(1); "END;"

   Dim tempTabNameStatementTabCfg As String
   tempTabNameStatementTabCfg = tempTabNameStatement & "TabPartCfg"

   genDdlForTempStatement fileNo, 1, True, 20000, True, True, True, , "TabPartCfg", , , True, , "msg", "VARCHAR(2048)", "refId", "INTEGER"

   genSpLogProcEnter fileNo, qualProcedureNameTablePartCfg, ddlType, , "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) AND (toggle_in = 1) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameTablePartCfg, ddlType, -2, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine partitiong-clause of statement"
   Print #fileNo, addTab(1); "IF toggle_in = 1 THEN"
   Print #fileNo, addTab(2); "FOR psLoop AS psCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "PS."; g_anIsUnderConstruction; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "PS."; g_anOid
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_partitionClauseTxt = v_partitionClauseTxt ||"
   Print #fileNo, addTab(4); "'PARTITION P' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(c_psOid), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") || ' STARTING ' || RTRIM(CHAR(c_psOid)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(c_psOid)) || ' INCLUSIVE'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "SET v_foundPartitionCrit = "; gc_dbTrue; ";"
 
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo, addTab(2); "SET v_partitionClauseTxt = ' PARTITION BY ("; g_anPsOid; ") (' || v_partitionClauseTxt || ')';"

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME AS c_tabName,"
   Print #fileNo, addTab(3); "T.COMPRESSION AS c_tabCompression,"
   Print #fileNo, addTab(3); "T.TBSPACE AS c_tbSpace,"
   Print #fileNo, addTab(3); "T.INDEX_TBSPACE AS c_indTbSpace,"
   Print #fileNo, addTab(3); "T.LONG_TBSPACE AS c_longTbSpace"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.COLNAME = '"; g_anPsOid; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "assemble CREATE TABLE statement", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CREATE TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName;"
   Print #fileNo, addTab(2); "SET v_colDeclTxt = '';"

   genProcSectionHeader fileNo, "loop over columns to assemble column-declarations", 2
   Print #fileNo, addTab(2); "FOR colLoop AS colCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CO.COLNAME   AS c_colName,"
   Print #fileNo, addTab(4); "CO.TYPENAME  AS c_colType,"
   Print #fileNo, addTab(4); "CO.LENGTH    AS c_length,"
   Print #fileNo, addTab(4); "CO.SCALE     AS c_scale,"
   Print #fileNo, addTab(4); "CO.DEFAULT   AS c_default,"
   Print #fileNo, addTab(4); "CO.NULLS     AS c_nulls,"
   Print #fileNo, addTab(4); "CO.COMPRESS  AS c_compress,"
   Print #fileNo, addTab(4); "CC.CONSTNAME AS c_constName,"
   Print #fileNo, addTab(4); "CH.TYPE      AS c_constType,"
   Print #fileNo, addTab(4); "CH.TEXT      AS c_constText"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS CO"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLCHECKS CC"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "CC.TABSCHEMA = CO.TABSCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CC.TABNAME = CO.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CC.COLNAME = CO.COLNAME"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.CHECKS CH"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "CH.TABSCHEMA = CC.TABSCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.TABNAME = CC.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.CONSTNAME = CC.CONSTNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.TYPE IN ('A', 'C')"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "CO.TABSCHEMA = c_tabSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CO.TABNAME = c_tabName"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_colDeclTxt = v_colDeclTxt || (CASE v_colDeclTxt WHEN '' THEN '' ELSE ', ' END) ||"
   Print #fileNo, addTab(4); "c_colName || ' ' ||"
   Print #fileNo, addTab(4); "c_colType ||"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN c_colType IN ('CHARACTER', 'VARCHAR', 'LONG VARCHAR', 'CLOB', 'BLOB', 'REAL')"
   Print #fileNo, addTab(6); "THEN '(' || RTRIM(CHAR(c_length)) || ')'"
   Print #fileNo, addTab(6); "WHEN c_colType IN ('DECIMAL')"
   Print #fileNo, addTab(6); "THEN '(' || RTRIM(CHAR(c_length)) || ',' || RTRIM(CHAR(c_scale)) || ')'"
   Print #fileNo, addTab(6); "ELSE ''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_nulls = 'N' THEN ' NOT NULL' ELSE '' END) ||"
   Print #fileNo, addTab(4); "COALESCE(' DEFAULT ' || c_default, '') ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_compress = 'S' THEN ' COMPRESS SYSTEM DEFAULT' ELSE '' END) ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_constType = 'C' THEN COALESCE(' CONSTRAINT ' || c_constName || ' CHECK (' || c_constText || ')', '') ELSE '' END)"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader fileNo, "finalize CREATE TABLE statement", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || ' (' || v_colDeclTxt || ')' ||"
   Print #fileNo, addTab(3); "COALESCE(' IN ' || c_tbSpace, '') ||"
   Print #fileNo, addTab(3); "COALESCE(' INDEX IN ' || c_indTbSpace, '') ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_tabCompression IN ('V', 'B') THEN ' VALUE COMPRESSION' ELSE '' END) ||"
   Print #fileNo, addTab(4); "' COMPRESS YES ' ||"
   Print #fileNo, addTab(4); "(CASE WHEN v_foundPartitionCrit = 1 THEN v_partitionClauseTxt ELSE '' END)"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementTabCfg
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', '' || REPLACE(v_stmntTxt, '''', '''''') || '', -1, ?)'"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", 4
   genVarDecl fileNo, "v_altObjMsg", "VARCHAR(2048)", "''", 4
   genVarDecl fileNo, "v_altObjId", "INTEGER", "-1", 4
   genVarDecl fileNo, "v_altObjStmntTxt", "VARCHAR(100)", "NULL", 4

   genProcSectionHeader fileNo, "declare statement", 4
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 4

   Print #fileNo, addTab(4); "SET v_altObjId = -1;"

   Print #fileNo, addTab(4); "SET v_altObjStmntTxt = 'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ?, ?, ?)';"

   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_altObjStmntTxt;"

   Print #fileNo, addTab(4); "EXECUTE"
   Print #fileNo, addTab(5); "v_stmnt"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_altObjId,"
   Print #fileNo, addTab(5); "v_altObjMsg"
   Print #fileNo, addTab(4); "USING"
   Print #fileNo, addTab(5); "v_stmntTxt,"
   Print #fileNo, addTab(5); "v_altObjId"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "IF mode_in = 1 THEN"
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatementTabCfg
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "flag,"
   Print #fileNo, addTab(6); "refId,"
   Print #fileNo, addTab(6); "msg,"
   Print #fileNo, addTab(6); "statement"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN NULL ELSE v_altObjId END),"
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN v_diagnostics ELSE v_altObjMsg END),"
   Print #fileNo, addTab(6); "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ''' || REPLACE(v_stmntTxt, '''', '''''') || ''', -1, ?)'"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo, addTab(3); "END;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementTabCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "ELSEIF mode_in = 1 THEN"

   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag,"
   Print #fileNo, addTab(5); "refId,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementTabCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameTablePartCfg, ddlType, , "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for adding a table partition for PS-tagged tables
   ' ####################################################################################################################

   Dim qualProcedureNameAddTablePartitionByPs As String
   qualProcedureNameAddTablePartitionByPs = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByPs, ddlType)

   printSectionHeader "SP for adding a table partition for PS-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByPs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "if set to '1' commit after each statement"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmntChkTxt", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_chkVal", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_doAddPartition", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_errMsg", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_partitionClauseTxt", "VARCHAR(200)", "''"
   genVarDecl fileNo, "v_tablespaceClauseTxt", "VARCHAR(200)", "''"
   genVarDecl fileNo, "v_returnResult", g_dbtBoolean, gc_dbTrue
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   If supportRangePartitioningByClassId Then
     genVarDecl fileNo, "v_stmntPartClauseTxt", "VARCHAR(2000)", "NULL"
     genVarDecl fileNo, "v_psSupportsPartByCId", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_tabSupportsPartByCId", g_dbtBoolean, gc_dbFalse
   End If
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement", 1
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 1
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR WITH HOLD FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Dim tempTabNameStatementAddTabPart As String
   tempTabNameStatementAddTabPart = tempTabNameStatement & "AddTabPartByPs"

   genDdlForTempStatement fileNo, 1, , 200, True, True, True, , "AddTabPartByPs", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "hook: if mode_in = '-1' or '-2', suppress return of results, but fill temporary table"
   Print #fileNo, addTab(1); "IF mode_in < 0 THEN"
   Print #fileNo, addTab(2); "SET v_returnResult = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "SET mode_in = mode_in + 2;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "DELETE FROM "; tempTabNameStatementAddTabPart; ";"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, -2, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   If supportRangePartitioningByClassId Then
     genProcSectionHeader fileNo, "determine whether PS supports partitioning by CLASSID"
     Print #fileNo, addTab(1); "IF EXISTS(SELECT 1 FROM "; g_qualTabNameClassIdPartitionBoundaries; " WHERE "; g_anPsOid; " = psOid_in AND (LBOUND IS NOT NULL OR UBOUND IS NOT NULL)) THEN"
     Print #fileNo, addTab(2); "SET v_psSupportsPartByCId = "; gc_dbTrue; ";"
     Print #fileNo, addTab(1); "END IF;"
   End If
 
   If Not supportRangePartitioningByClassId Then
     genProcSectionHeader fileNo, "determine partitiong-clause of statement"
     Print #fileNo, addTab(1); "SET v_partitionClauseTxt = ' ADD PARTITION P' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(psOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") ||"; _
                               "' STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE';"
   End If

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME AS c_tabName"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " = T.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " = T.TABNAME"

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L_MQT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityName; " = L_MQT."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityType; " = L_MQT."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anAcmEntitySection; " = L_MQT."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = L_MQT."; g_anLdmIsNl
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = L_MQT."; g_anLdmIsGen
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L_MQT."; g_anLdmIsMqt; " = "; gc_dbTrue

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P_MQT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P_MQT."; g_anPdmLdmFkSchemaName; " = L_MQT."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P_MQT."; g_anPdmLdmFkTableName; " = L_MQT."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = P_MQT."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = P_MQT."; g_anPoolTypeId

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " AS aet"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "aet."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "aet."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "aet."; g_anAcmEntityType; " = L."; g_anAcmEntityType

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("

   If noPartitioningInDataPools <> "" Then
       Dim i As Integer
       For i = 1 To g_pools.numDescriptors
           If includedInList(noPartitioningInDataPools, g_pools.descriptors(i).id) Then
             If andOrFlag Then
               Print #fileNo, addTab(5); "AND"
             End If
             genProcSectionHeader fileNo, "no data partitioning in datapool " & CStr(g_pools.descriptors(i).id), 5, True
             Print #fileNo, addTab(4); "COALESCE(P."; g_anPoolTypeId; ",-1) <> "; CStr(g_pools.descriptors(i).id)
             andOrFlag = True
           End If
       Next i
   End If

   If Not partitionLrtPrivateWhenMqt Then
     If andOrFlag Then
       Print #fileNo, addTab(5); "AND"
     End If
     Print #fileNo, addTab(4); "("
     genProcSectionHeader fileNo, "if MQT-table exists, partitioning is not supported on private tables", 5, True
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsLrt; ", "; gc_dbFalse; ") = "; gc_dbFalse
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsMqt; ", "; gc_dbTrue; ") = "; gc_dbTrue
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "P_MQT."; g_anPoolTypeId; " IS NULL"
     Print #fileNo, addTab(4); ")"
     andOrFlag = True
   End If
 
   If Not partitionLrtPublicWhenMqt Then
     If andOrFlag Then
       Print #fileNo, addTab(5); "AND"
     End If
     Print #fileNo, addTab(4); "("
     genProcSectionHeader fileNo, "if MQT-table exists, partitioning is not supported on public tables", 5, True
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsLrt; ", "; gc_dbTrue; ") = "; gc_dbTrue
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsMqt; ", "; gc_dbTrue; ") = "; gc_dbTrue
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "P_MQT."; g_anPoolTypeId; " IS NULL"
     Print #fileNo, addTab(4); ")"
     andOrFlag = True
   End If
 
   If Not partitionLrtPrivateWhenNoMqt Then
     Print #fileNo, addTab(4); "("
     genProcSectionHeader fileNo, "if MQT-table does not exist, partitioning is not supported on private tables", 5, True
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsLrt; ", "; gc_dbFalse; ") = "; gc_dbFalse
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "P_MQT."; g_anPoolTypeId; " IS NOT NULL"
     Print #fileNo, addTab(4); ")"
     andOrFlag = True
   End If
 
   If Not partitionLrtPublicWhenNoMqt Then
     If andOrFlag Then
       Print #fileNo, addTab(5); "AND"
     End If
     Print #fileNo, addTab(4); "("
     genProcSectionHeader fileNo, "if MQT-table does not exist, partitioning is not supported on public tables", 5, True
     Print #fileNo, addTab(5); "COALESCE(L."; g_anLdmIsLrt; ", "; gc_dbTrue; ") = "; gc_dbTrue
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "P_MQT."; g_anPoolTypeId; " IS NOT NULL"
     Print #fileNo, addTab(4); ")"
     andOrFlag = True
   End If
 
   If andOrFlag Then
     Print #fileNo, addTab(5); "OR"
   End If
   Print #fileNo, addTab(4); "aet.isRangePartAll = 1"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
 
   Print #fileNo, addTab(3); "C.COLNAME = '"; g_anPsOid; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_doAddPartition = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_errMsg = NULL;"

   genProcSectionHeader fileNo, "check whether table supports partitioning by PS", 2, True
   Print #fileNo, addTab(2); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(3); "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(3); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(3); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(3); "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''"; g_anPsOid; "'')' ||"
   Print #fileNo, addTab(3); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(3); "' FETCH FIRST 1 ROW ONLY';"

   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_doAddPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_errMsg = 'not enabled for partitioning';"
   Print #fileNo, addTab(2); "ELSE"
 
   genProcSectionHeader fileNo, "check whether psOid_in is already covered by some partition", 3, True
   Print #fileNo, addTab(3); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(3); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(4); "'WITH ' ||"
   Print #fileNo, addTab(5); "'V_P (TABSCHEMA, TABNAME, LOWVALUE, LOWINCLUSIVE, HIGHVALUE, HIGHINCLUSIVE) AS ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'TABSCHEMA,' ||"
   Print #fileNo, addTab(6); "'TABNAME,' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN POSSTR(LOWVALUE, '','') > 0 THEN LEFT(LOWVALUE, COALESCE(POSSTR(LOWVALUE, '','')-1, LENGTH(LOWVALUE))) ELSE LOWVALUE END),' ||"
   Print #fileNo, addTab(6); "'LOWINCLUSIVE,' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN POSSTR(HIGHVALUE, '','') > 0 THEN LEFT(HIGHVALUE, COALESCE(POSSTR(HIGHVALUE, '','')-1, LENGTH(HIGHVALUE))) ELSE HIGHVALUE END),' ||"
   Print #fileNo, addTab(6); "'HIGHINCLUSIVE ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'SYSCAT.DATAPARTITIONS' ||"
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'SELECT 1 FROM V_P P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(4); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(4); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(4); "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''"; g_anPsOid; "'')' ||"
   Print #fileNo, addTab(4); "' AND ((P.LOWINCLUSIVE  = ''Y'' AND P.LOWVALUE  <= RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))) OR (P.LOWINCLUSIVE  <> ''Y'' AND P.LOWVALUE  < RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))))' ||"
   Print #fileNo, addTab(4); "' AND ((P.HIGHINCLUSIVE = ''Y'' AND P.HIGHVALUE >= RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))) OR (P.HIGHINCLUSIVE <> ''Y'' AND P.HIGHVALUE > RTRIM(CHAR(' || RTRIM(CHAR(psOid_in)) || '))))' ||"
   Print #fileNo, addTab(4); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(4); "' FETCH FIRST 1 ROW ONLY';"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(3); "OPEN c;"
   Print #fileNo, addTab(3); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(3); "CLOSE c WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbTrue; " THEN"
   Print #fileNo, addTab(4); "SET v_doAddPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "SET v_errMsg = 'already covered by partition';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "determine Tablespace-names", 2, True
   Print #fileNo, addTab(2); "SET v_tablespaceClauseTxt ="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "' IN ' || tbs_d.tbspace || ' INDEX IN ' || tbs_i.tbspace || ' LONG IN ' || tbs_l.tbspace"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.DATAPARTITIONS AS dp"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "syscat.tablespaces AS tbs_d"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "dp.tbspaceid = tbs_d.tbspaceid"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "syscat.tablespaces AS tbs_i"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "dp.index_tbspaceid = tbs_i.tbspaceid"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "syscat.tablespaces AS tbs_l"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "dp.long_tbspaceid = tbs_l.tbspaceid"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "dp.TABSCHEMA = c_tabSchema"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "dp.TABNAME = c_tabName"
   Print #fileNo, addTab(3); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); ";"
 
   If supportRangePartitioningByClassId Then
     genProcSectionHeader fileNo, "determine whether table supports partitioning by CLASSID", 2
 
     Print #fileNo, addTab(2); "SET v_chkVal = NULL;"
     Print #fileNo, addTab(2); "SET v_stmntChkTxt ="
     Print #fileNo, addTab(3); "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
     Print #fileNo, addTab(3); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
     Print #fileNo, addTab(3); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
     Print #fileNo, addTab(3); "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''"; g_anCid; "'')' ||"
     Print #fileNo, addTab(3); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
     Print #fileNo, addTab(3); "' FETCH FIRST 1 ROW ONLY';"

     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntChkTxt;"
     Print #fileNo, addTab(2); "OPEN c;"
     Print #fileNo, addTab(2); "FETCH c INTO v_chkVal;"
     Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"
 
     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_tabSupportsPartByCId = COALESCE(v_chkVal, "; gc_dbFalse; ");"

     genProcSectionHeader fileNo, "determine partitiong-clause of statement", 2
     Print #fileNo, addTab(2); "IF v_tabSupportsPartByCId = 1 THEN"
     Print #fileNo, addTab(3); "IF v_psSupportsPartByCId = 1 THEN"
     Print #fileNo, addTab(4); "SET v_stmntPartClauseTxt ="
     Print #fileNo, addTab(5); "'SELECT ' ||"
     Print #fileNo, addTab(5); "'''P'' || COALESCE(LBOUND, ''"; getClassId(0, 0); "'') || ' || '''' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(psOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") || ' ' ||"
     Print #fileNo, addTab(6); "'STARTING (' || RTRIM(CHAR(psOid_in)) || ','' || COALESCE('''''''' || LBOUND || '''''''', ''MINVALUE'') || '') INCLUSIVE ' ||"
     Print #fileNo, addTab(6); "'ENDING (' || RTRIM(CHAR(psOid_in)) || ','' || COALESCE('''''''' || UBOUND || '''''''', ''MAXVALUE'') || '') INCLUSIVE'' ' ||"
     Print #fileNo, addTab(5); "'FROM ' ||"
     Print #fileNo, addTab(6); "'"; g_qualTabNameClassIdPartitionBoundaries; " ' ||"
     Print #fileNo, addTab(5); "'WHERE ' ||"
     Print #fileNo, addTab(6); "'"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(5); "'ORDER BY ' ||"
     Print #fileNo, addTab(6); "'COALESCE(LBOUND, ''"; getClassId(0, 0); "'')'"
     Print #fileNo, addTab(4); ";"
     Print #fileNo, addTab(3); "ELSE"
     Print #fileNo, addTab(4); "SET v_stmntPartClauseTxt ="
     Print #fileNo, addTab(5); "'SELECT ' ||"
     Print #fileNo, addTab(6); "'''P' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(psOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") || ' ' ||"
     Print #fileNo, addTab(6); "'STARTING (' || RTRIM(CHAR(psOid_in)) || ', MINVALUE) INCLUSIVE ' ||"
     Print #fileNo, addTab(6); "'ENDING (' || RTRIM(CHAR(psOid_in)) || ', MAXVALUE) INCLUSIVE'' ' ||"
     Print #fileNo, addTab(5); "'FROM ' ||"
     Print #fileNo, addTab(6); "'SYSIBM.SYSDUMMY1'"
     Print #fileNo, addTab(4); ";"
     Print #fileNo, addTab(3); "END IF;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "SET v_stmntPartClauseTxt ="
     Print #fileNo, addTab(4); "'SELECT ' ||"
     Print #fileNo, addTab(5); "'''P' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(psOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") || ' ' ||"
     Print #fileNo, addTab(5); "'STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE'' ' ||"
     Print #fileNo, addTab(4); "'FROM ' ||"
     Print #fileNo, addTab(5); "'SYSIBM.SYSDUMMY1'"
     Print #fileNo, addTab(3); ";"
     Print #fileNo, addTab(2); "END IF;"
   Else
     Print #fileNo, addTab(2); "SET v_stmntPartClauseTxt ="
     Print #fileNo, addTab(3); "'SELECT ' ||"
     Print #fileNo, addTab(4); "'''P' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(psOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") ||"
     Print #fileNo, addTab(4); "' STARTING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(psOid_in)) || ' INCLUSIVE'' ' ||"
     Print #fileNo, addTab(3); "'FROM ' ||"
     Print #fileNo, addTab(4); "'SYSIBM.SYSDUMMY1'"
     Print #fileNo, addTab(2); ";"
   End If

   genProcSectionHeader fileNo, "loop over partitions and assemble ALTER TABLE statement", 2
 
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntPartClauseTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH c INTO v_partitionClauseTxt;"
   Print #fileNo, addTab(2); "WHILE (SQLCODE = 0) DO"

   genProcSectionHeader fileNo, "assemble ALTER TABLE statement", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' ADD PARTITION ' || v_partitionClauseTxt || v_tablespaceClauseTxt;"

   genProcSectionHeader fileNo, "store statement in temporary table", 3
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "flag,"
   Print #fileNo, addTab(5); "msg,"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "(CASE v_doAddPartition WHEN 0 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(5); "v_errMsg,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 3
   Print #fileNo, addTab(3); "IF mode_in >= 1 AND v_doAddPartition = 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "COMMIT if requested", 4
   Print #fileNo, addTab(4); "IF autoCommit_in = 1 THEN"
   Print #fileNo, addTab(5); "COMMIT;"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "count statement", 3
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH c INTO v_partitionClauseTxt;"

   Print #fileNo, addTab(2); "END WHILE;"

   Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 AND v_returnResult = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for PS-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByPs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameAddTablePartitionByPs; "(mode_in, psOid_in, tabSchema_in, tabName_in, 1, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for PS-tagged tables - for ALL PS in table ProductStructure", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByPs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "AddTabPartByPs", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "loop over ProductStructures"
   Print #fileNo, addTab(1); "FOR psLoop AS psCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "add table partitions for this specific ProductStructure", 2, True
   Print #fileNo, addTab(2); "CALL "; qualProcedureNameAddTablePartitionByPs; "((CASE WHEN mode_in IN (0,1) THEN (mode_in-2) ELSE mode_in END), c_psOid, tabSchema_in, tabName_in, 1, v_rowCount);"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"
   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for PS-tagged tables - for ALL PS in table ProductStructure and all tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByPs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameAddTablePartitionByPs; "(mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByPs, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for deleting a table partition for PS-tagged tables
   ' ####################################################################################################################

   Dim qualProcedureNameDeleteTablePartitionByPs As String
   qualProcedureNameDeleteTablePartitionByPs = genQualProcName(g_sectionIndexDbAdmin, spnDeleteTablePartitionByPs, ddlType)

   printSectionHeader "SP for deleting a table partition for PS-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDeleteTablePartitionByPs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "v_chkVal", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_dbPartitionName", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_doDelPartition", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_errMsg", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement", 1
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 1
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR WITH HOLD FOR v_stmnt;"
 
   Dim tempTabNameStatementDelTabPartitionByDiv As String
   tempTabNameStatementDelTabPartitionByDiv = tempTabNameStatement & "DelTabPartByDiv"

   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "DelTabPartByDiv", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning by PS"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, -2, "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA     AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME       AS c_tabName,"
   Print #fileNo, addTab(3); "T.COMPRESSION   AS c_tabCompression,"
   Print #fileNo, addTab(3); "T.TBSPACE       AS c_tbSpace,"
   Print #fileNo, addTab(3); "T.INDEX_TBSPACE AS c_indTbSpace,"
   Print #fileNo, addTab(3); "T.LONG_TBSPACE  AS c_longTbSpace"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.COLNAME = '"; g_anPsOid; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_doDelPartition = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_errMsg = NULL;"

   genProcSectionHeader fileNo, "check whether table supports partitioning by PS", 2, True
   Print #fileNo, addTab(2); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(3); "'SELECT 1 FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(3); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(3); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(3); "' AND (VARCHAR(E.DATAPARTITIONEXPRESSION) = ''"; g_anPsOid; "'')' ||"
   Print #fileNo, addTab(3); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(3); "' FETCH FIRST 1 ROW ONLY';"

   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_doDelPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_errMsg = 'not enabled for partitioning';"
   Print #fileNo, addTab(2); "ELSE"
 
   genProcSectionHeader fileNo, "check whether psOid_in defines some partition", 3, True
   Print #fileNo, addTab(3); "SET v_dbPartitionName = NULL;"
   Print #fileNo, addTab(3); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(4); "'WITH ' ||"
   Print #fileNo, addTab(5); "'V_DP ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'DATAPARTITIONNAME,' ||"
   Print #fileNo, addTab(5); "'LOWINCLUSIVE,' ||"
   Print #fileNo, addTab(5); "'LOWVALUE,' ||"
   Print #fileNo, addTab(5); "'HIGHINCLUSIVE,' ||"
   Print #fileNo, addTab(5); "'HIGHVALUE' ||"
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'AS ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(5); "'DATAPARTITIONNAME,' ||"
   Print #fileNo, addTab(6); "'LOWINCLUSIVE, ' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN "; g_qualFuncNameIsNumeric; "(LOWVALUE) = 1 THEN "; g_dbtOid; "(LOWVALUE) ELSE ' || RTRIM(CHAR(psOid_in)) || '-1 END), ' ||"
   Print #fileNo, addTab(6); "'HIGHINCLUSIVE, ' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN "; g_qualFuncNameIsNumeric; "(HIGHVALUE) = 1 THEN "; g_dbtOid; "(HIGHVALUE) ELSE ' || RTRIM(CHAR(psOid_in)) || '+1 END) ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'SYSCAT.DATAPARTITIONS ' ||"
   Print #fileNo, addTab(5); "'WHERE ' ||"
   Print #fileNo, addTab(6); "'(' ||"
   Print #fileNo, addTab(7); "'COALESCE(LOWVALUE, '''') <> '''' ' ||"
   Print #fileNo, addTab(8); "'OR ' ||"
   Print #fileNo, addTab(7); "'COALESCE(HIGHVALUE, '''') <> '''' ' ||"
   Print #fileNo, addTab(6); "') ' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'TABSCHEMA = ''' || c_tabSchema || ''' ' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'TABNAME = ''' || c_tabName || ''' ' ||"
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'SELECT ' ||"
   Print #fileNo, addTab(5); "'DATAPARTITIONNAME ' ||"
   Print #fileNo, addTab(4); "'FROM ' ||"
   Print #fileNo, addTab(5); "'V_DP ' ||"
   Print #fileNo, addTab(4); "'WHERE ' ||"
   Print #fileNo, addTab(5); "'((LOWINCLUSIVE  = ''Y'' AND LOWVALUE  <= ' || RTRIM(CHAR(psOid_in)) || ') OR (LOWINCLUSIVE  <> ''Y'' AND LOWVALUE  < ' || RTRIM(CHAR(psOid_in)) || ')) ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'((HIGHINCLUSIVE = ''Y'' AND HIGHVALUE >= ' || RTRIM(CHAR(psOid_in)) || ') OR (HIGHINCLUSIVE <> ''Y'' AND HIGHVALUE > ' || RTRIM(CHAR(psOid_in)) || ')) ' ||"
   Print #fileNo, addTab(4); "'FETCH FIRST 1 ROW ONLY'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(3); "OPEN c;"
   Print #fileNo, addTab(3); "FETCH c INTO v_dbPartitionName;"
   Print #fileNo, addTab(3); "CLOSE c WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_dbPartitionName IS NULL THEN"
   Print #fileNo, addTab(4); "SET v_dbPartitionName = '???';"
   Print #fileNo, addTab(4); "SET v_doDelPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "SET v_errMsg = 'not covered by partition';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "assemble ALTER TABLE statement", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' DETACH PARTITION ' || v_dbPartitionName || ' INTO ' || RTRIM(c_tabSchema) || '.' || c_tabName || '_DET' || RTRIM(CHAR(psOid_in));"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementDelTabPartitionByDiv
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "flag,"
   Print #fileNo, addTab(4); "msg,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(CASE v_doDelPartition WHEN 0 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(4); "v_errMsg,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 AND v_doDelPartition = 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementDelTabPartitionByDiv
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameDeleteTablePartitionByPs, ddlType, , "mode_in", "psOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Sub genDbAdminPartitioningByDivDdlByDdlType( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   ' ####################################################################################################################
   ' #    SP for configuring table partitioning (by DIV_OID)
   ' ####################################################################################################################

   Dim qualProcedureNameTablePartCfg As String
   qualProcedureNameTablePartCfg = genQualProcName(g_sectionIndexDbAdmin, spnSetTablePartCfgDiv, ddlType)

   printSectionHeader "SP for configuring table partitioning (by DIV_OID)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTablePartCfg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "toggle_in", g_dbtBoolean, True, "if set to '1' switch on table partitioning, otherwise switch off"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(20000)", "NULL"
   genVarDecl fileNo, "v_colDeclTxt", "VARCHAR(20000)", "NULL"
   genVarDecl fileNo, "v_partitionClauseTxt", "VARCHAR(100)", "''"
   genVarDecl fileNo, "v_diagnostics", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_foundPartitionCrit", g_dbtBoolean, gc_dbFalse
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "altObjError", "38553"

   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR altObjError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(1); "END;"

   Dim tempTabNameStatementTabCfg As String
   tempTabNameStatementTabCfg = tempTabNameStatement & "TabPartCfg"

   genDdlForTempStatement fileNo, 1, True, 20000, True, True, True, , "TabPartCfg", , , True, , "msg", "VARCHAR(2048)", "refId", "INTEGER"

   genSpLogProcEnter fileNo, qualProcedureNameTablePartCfg, ddlType, , "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) AND (toggle_in = 1) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameTablePartCfg, ddlType, -2, "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA     AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME       AS c_tabName,"
   Print #fileNo, addTab(3); "C.COLNAME       AS c_colName,"
   Print #fileNo, addTab(3); "T.COMPRESSION   AS c_tabCompression,"
   Print #fileNo, addTab(3); "T.TBSPACE       AS c_tbSpace,"
   Print #fileNo, addTab(3); "T.INDEX_TBSPACE AS c_indTbSpace,"
   Print #fileNo, addTab(3); "T.LONG_TBSPACE  AS c_longTbSpace"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.COLNAME LIKE '%DIV_OID'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "determine partitiong-clause of statement for this table", 2
   Print #fileNo, addTab(2); "SET v_foundPartitionCrit = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "SET v_partitionClauseTxt = '';"
   Print #fileNo, addTab(2); "IF toggle_in = 1 THEN"
   Print #fileNo, addTab(3); "FOR divLoop AS divCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "DIV."; g_anOid; " AS c_divOid"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameDivision; " DIV"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "DIV."; g_anOid
   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "SET v_partitionClauseTxt = v_partitionClauseTxt ||"
   Print #fileNo, addTab(5); "'PARTITION D' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(c_divOid), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") || ' STARTING ' || RTRIM(CHAR(c_divOid)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(c_divOid)) || ' INCLUSIVE'"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "SET v_foundPartitionCrit = "; gc_dbTrue; ";"
 
   Print #fileNo, addTab(3); "END FOR;"

   Print #fileNo, addTab(3); "SET v_partitionClauseTxt = ' PARTITION BY RANGE (' || c_colName || ') (' || v_partitionClauseTxt || ')';"

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "assemble CREATE TABLE statement", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CREATE TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName;"
   Print #fileNo, addTab(2); "SET v_colDeclTxt = '';"

   genProcSectionHeader fileNo, "loop over columns to assemble column-declarations", 2
   Print #fileNo, addTab(2); "FOR colLoop AS colCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CO.COLNAME   AS c_colName,"
   Print #fileNo, addTab(4); "CO.TYPENAME  AS c_colType,"
   Print #fileNo, addTab(4); "CO.LENGTH    AS c_length,"
   Print #fileNo, addTab(4); "CO.SCALE     AS c_scale,"
   Print #fileNo, addTab(4); "CO.DEFAULT   AS c_default,"
   Print #fileNo, addTab(4); "CO.NULLS     AS c_nulls,"
   Print #fileNo, addTab(4); "CO.COMPRESS  AS c_compress,"
   Print #fileNo, addTab(4); "CC.CONSTNAME AS c_constName,"
   Print #fileNo, addTab(4); "CH.TYPE      AS c_constType,"
   Print #fileNo, addTab(4); "CH.TEXT      AS c_constText"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS CO"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLCHECKS CC"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "CC.TABSCHEMA = CO.TABSCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CC.TABNAME = CO.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CC.COLNAME = CO.COLNAME"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.CHECKS CH"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "CH.TABSCHEMA = CC.TABSCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.TABNAME = CC.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.CONSTNAME = CC.CONSTNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CH.TYPE IN ('A', 'C')"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "CO.TABSCHEMA = c_tabSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CO.TABNAME = c_tabName"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_colDeclTxt = v_colDeclTxt || (CASE v_colDeclTxt WHEN '' THEN '' ELSE ', ' END) ||"
   Print #fileNo, addTab(4); "c_colName || ' ' ||"
   Print #fileNo, addTab(4); "c_colType ||"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN c_colType IN ('CHARACTER', 'VARCHAR', 'LONG VARCHAR', 'CLOB', 'BLOB', 'REAL')"
   Print #fileNo, addTab(6); "THEN '(' || RTRIM(CHAR(c_length)) || ')'"
   Print #fileNo, addTab(6); "WHEN c_colType IN ('DECIMAL')"
   Print #fileNo, addTab(6); "THEN '(' || RTRIM(CHAR(c_length)) || ',' || RTRIM(CHAR(c_scale)) || ')'"
   Print #fileNo, addTab(6); "ELSE ''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_nulls = 'N' THEN ' NOT NULL' ELSE '' END) ||"
   Print #fileNo, addTab(4); "COALESCE(' DEFAULT ' || c_default, '') ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_compress = 'S' THEN ' COMPRESS SYSTEM DEFAULT' ELSE '' END) ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_constType = 'C' THEN COALESCE(' CONSTRAINT ' || c_constName || ' CHECK (' || c_constText || ')', '') ELSE '' END)"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader fileNo, "finalize CREATE TABLE statement", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || ' (' || v_colDeclTxt || ')' ||"
   Print #fileNo, addTab(3); "COALESCE(' IN ' || c_tbSpace, '') ||"
   Print #fileNo, addTab(3); "COALESCE(' INDEX IN ' || c_indTbSpace, '') ||"
   Print #fileNo, addTab(4); "(CASE WHEN c_tabCompression IN ('V', 'B') THEN ' VALUE COMPRESSION' ELSE '' END) ||"
   Print #fileNo, addTab(4); "' COMPRESS YES ' ||"
   Print #fileNo, addTab(4); "(CASE WHEN v_foundPartitionCrit = 1 THEN v_partitionClauseTxt ELSE '' END)"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementTabCfg
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', '' || REPLACE(v_stmntTxt, '''', '''''') || '', -1, ?)'"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", 4
   genVarDecl fileNo, "v_altObjMsg", "VARCHAR(2048)", "''", 4
   genVarDecl fileNo, "v_altObjId", "INTEGER", "-1", 4
   genVarDecl fileNo, "v_altObjStmntTxt", "VARCHAR(100)", "NULL", 4

   genProcSectionHeader fileNo, "declare statement", 4
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 4

   Print #fileNo, addTab(4); "SET v_altObjId = -1;"

   Print #fileNo, addTab(4); "SET v_altObjStmntTxt = 'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ?, ?, ?)';"

   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_altObjStmntTxt;"

   Print #fileNo, addTab(4); "EXECUTE"
   Print #fileNo, addTab(5); "v_stmnt"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_altObjId,"
   Print #fileNo, addTab(5); "v_altObjMsg"
   Print #fileNo, addTab(4); "USING"
   Print #fileNo, addTab(5); "v_stmntTxt,"
   Print #fileNo, addTab(5); "v_altObjId"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "IF mode_in = 1 THEN"
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatementTabCfg
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "flag,"
   Print #fileNo, addTab(6); "refId,"
   Print #fileNo, addTab(6); "msg,"
   Print #fileNo, addTab(6); "statement"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN NULL ELSE v_altObjId END),"
   Print #fileNo, addTab(6); "(CASE WHEN v_altObjId = -1 THEN v_diagnostics ELSE v_altObjMsg END),"
   Print #fileNo, addTab(6); "'CALL SYSPROC.ALTOBJ(''APPLY_STOP_ON_ERROR'', ''' || REPLACE(v_stmntTxt, '''', '''''') || ''', -1, ?)'"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo, addTab(3); "END;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in = 0 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementTabCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "ELSEIF mode_in = 1 THEN"

   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag,"
   Print #fileNo, addTab(5); "refId,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementTabCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameTablePartCfg, ddlType, , "mode_in", "toggle_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for adding a table partition for DIV-tagged tables
   ' ####################################################################################################################

   Dim qualProcedureNameAddTablePartitionByDiv As String
   qualProcedureNameAddTablePartitionByDiv = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByDiv, ddlType)

   printSectionHeader "SP for adding a table partition for DIV-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByDiv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "divOid_in", g_dbtOid, True, "OID of the Division to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "IN", "autoCommit_in", g_dbtBoolean, True, "if set to '1' commit after each statement"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "v_chkVal", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_doAddPartition", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_errMsg", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_partitionClauseTxt", "VARCHAR(200)", "''"
   genVarDecl fileNo, "v_returnResult", g_dbtBoolean, gc_dbTrue
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement", 1
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 1
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR WITH HOLD FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Dim tempTabNameStatementAddTabPart As String
   tempTabNameStatementAddTabPart = tempTabNameStatement & "AddTabPartByDiv"

   genDdlForTempStatement fileNo, 1, , 200, True, True, True, , "AddTabPartByDiv", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "hook: if mode_in = '-1' or '-2', suppress return of results, but fill temporary table"
   Print #fileNo, addTab(1); "IF mode_in < 0 THEN"
   Print #fileNo, addTab(2); "SET v_returnResult = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "SET mode_in = mode_in + 2;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine partitiong-clause of statement"
   Print #fileNo, addTab(1); "SET v_partitionClauseTxt = ' ADD PARTITION D' || RIGHT(DIGITS("; g_dbtOid; "(0)) || DIGITS(divOid_in), "; CStr(gc_maxDb2PartitionNameSuffixLen); ") ||"; _
                             "' STARTING ' || RTRIM(CHAR(divOid_in)) || ' INCLUSIVE ENDING ' || RTRIM(CHAR(divOid_in)) || ' INCLUSIVE';"

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA     AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME       AS c_tabName,"
   Print #fileNo, addTab(3); "T.COMPRESSION   AS c_tabCompression,"
   Print #fileNo, addTab(3); "T.TBSPACE       AS c_tbSpace,"
   Print #fileNo, addTab(3); "T.INDEX_TBSPACE AS c_indTbSpace,"
   Print #fileNo, addTab(3); "T.LONG_TBSPACE  AS c_longTbSpace"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.COLNAME LIKE '%DIV_OID'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME NOT LIKE 'PRODUCTSTRUCTURE%'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME NOT LIKE 'DIVISION%'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NOT EXISTS ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.COLUMNS CP"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "CP.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CP.TABNAME = C.TABNAME"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CP.COLNAME = '"; g_anPsOid; "'"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_doAddPartition = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_errMsg = NULL;"

   genProcSectionHeader fileNo, "check whether table supports partitioning by DIV_OID", 2, True
   Print #fileNo, addTab(2); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(3); "'SELECT 1' ||"
   Print #fileNo, addTab(3); "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(3); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(3); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(3); "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||"
   Print #fileNo, addTab(3); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(3); "' FETCH FIRST 1 ROW ONLY';"

   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_doAddPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_errMsg = 'not enabled for partitioning';"
   Print #fileNo, addTab(2); "ELSE"
 
   genProcSectionHeader fileNo, "check whether divOid_in is already covered by some partition", 3, True
   Print #fileNo, addTab(3); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(3); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(4); "'SELECT 1' ||"
   Print #fileNo, addTab(4); "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(4); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(4); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(4); "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||"
   Print #fileNo, addTab(4); "' AND ((P.LOWINCLUSIVE  = ''Y'' AND P.LOWVALUE  <= RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))) OR (P.LOWINCLUSIVE  <> ''Y'' AND P.LOWVALUE  < RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))))' ||"
   Print #fileNo, addTab(4); "' AND ((P.HIGHINCLUSIVE = ''Y'' AND P.HIGHVALUE >= RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))) OR (P.HIGHINCLUSIVE <> ''Y'' AND P.HIGHVALUE > RTRIM(CHAR(' || RTRIM(CHAR(divOid_in)) || '))))' ||"
   Print #fileNo, addTab(4); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(4); "' FETCH FIRST 1 ROW ONLY';"

   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(3); "OPEN c;"
   Print #fileNo, addTab(3); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(3); "CLOSE c WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbTrue; " THEN"
   Print #fileNo, addTab(4); "SET v_doAddPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "SET v_errMsg = 'already covered by partition';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "assemble ALTER TABLE statement", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || v_partitionClauseTxt;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "flag,"
   Print #fileNo, addTab(4); "msg,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(CASE v_doAddPartition WHEN 0 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(4); "v_errMsg,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 AND v_doAddPartition = 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "COMMIT if requested", 3
   Print #fileNo, addTab(3); "IF autoCommit_in = 1 THEN"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "count statement", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 AND v_returnResult = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "autoCommit_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for DIV-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByDiv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "divOid_in", g_dbtOid, True, "OID of the Product Structure to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameAddTablePartitionByDiv; "(mode_in, divOid_in, tabSchema_in, tabName_in, 1, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for DIV-tagged tables - for ALL Divisions", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByDiv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "AddTabPartByDiv", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, -2, "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "loop over Divisions"
   Print #fileNo, addTab(1); "FOR divLoop AS divCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "DIV."; g_anOid; " AS c_divOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDivision; " DIV"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "add table partitions for this specific Division", 2, True
   Print #fileNo, addTab(2); "CALL "; qualProcedureNameAddTablePartitionByDiv; "((CASE WHEN mode_in IN (0,1) THEN (mode_in-2) ELSE mode_in END), c_divOid, tabSchema_in, tabName_in, 1, v_rowCount);"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementAddTabPart
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"
   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for adding a table partition for DIV-tagged tables - for ALL Divisions", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAddTablePartitionByDiv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(0); "CALL "; qualProcedureNameAddTablePartitionByDiv; "(mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameAddTablePartitionByDiv, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for deleting a table partition for DIV-tagged tables
   ' ####################################################################################################################

   Dim qualProcedureNameDeleteTablePartitionByDiv As String
   qualProcedureNameDeleteTablePartitionByDiv = genQualProcName(g_sectionIndexDbAdmin, spnDeleteTablePartitionByDiv, ddlType)

   printSectionHeader "SP for deleting a table partition for DIV-tagged tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDeleteTablePartitionByDiv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "divOid_in", g_dbtOid, True, "OID of the Product Structure to add partitions for"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_stmntChkTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "v_chkVal", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_dbPartitionName", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_doDelPartition", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_errMsg", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement", 1
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 1
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR WITH HOLD FOR v_stmnt;"
 
   Dim tempTabNameStatementDelTabPart As String
   tempTabNameStatementDelTabPart = tempTabNameStatement & "DelTabPartByDiv"

   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "DelTabPartByDiv", , , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "determine whether DB supports table partitioning"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05) THEN"
   genSpLogProcEscape fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, -2, "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "TABLE PARTITIONING", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over matching tables to configure"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TABSCHEMA     AS c_tabSchema,"
   Print #fileNo, addTab(3); "T.TABNAME       AS c_tabName,"
   Print #fileNo, addTab(3); "T.COMPRESSION   AS c_tabCompression,"
   Print #fileNo, addTab(3); "T.TBSPACE       AS c_tbSpace,"
   Print #fileNo, addTab(3); "T.INDEX_TBSPACE AS c_indTbSpace,"
   Print #fileNo, addTab(3); "T.LONG_TBSPACE  AS c_longTbSpace"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = C.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.COLNAME LIKE '%DIV_OID'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabName_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME NOT LIKE 'PRODUCTSTRUCTURE%'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME NOT LIKE 'DIVISION%'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NOT EXISTS ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.COLUMNS CP"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "CP.TABSCHEMA = C.TABSCHEMA"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CP.TABNAME = C.TABNAME"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CP.COLNAME = '"; g_anPsOid; "'"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_doDelPartition = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_errMsg = NULL;"

   genProcSectionHeader fileNo, "check whether table supports partitioning by DIV_OID", 2, True
   Print #fileNo, addTab(2); "SET v_chkVal = NULL;"
   Print #fileNo, addTab(2); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(3); "'SELECT 1' ||"
   Print #fileNo, addTab(3); "' FROM SYSCAT.DATAPARTITIONS P INNER JOIN SYSCAT.DATAPARTITIONEXPRESSION E' ||"
   Print #fileNo, addTab(3); "' ON P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME' ||"
   Print #fileNo, addTab(3); "' WHERE P.TABSCHEMA = ''' || c_tabSchema || ''' AND P.TABNAME = ''' || c_tabName || '''' ||"
   Print #fileNo, addTab(3); "' AND ((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||"
   Print #fileNo, addTab(3); "' AND (COALESCE(P.LOWVALUE, '''') <> '''' OR COALESCE(P.HIGHVALUE, '''') <> '''')' ||"
   Print #fileNo, addTab(3); "' FETCH FIRST 1 ROW ONLY';"

   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH c INTO v_chkVal;"
   Print #fileNo, addTab(2); "CLOSE c WITH RELEASE;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF COALESCE(v_chkVal, "; gc_dbFalse; ") = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_doDelPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_errMsg = 'not enabled for partitioning';"
   Print #fileNo, addTab(2); "ELSE"
 
   genProcSectionHeader fileNo, "check whether divOid_in defines some partition", 3, True
   Print #fileNo, addTab(3); "SET v_dbPartitionName = NULL;"
   Print #fileNo, addTab(3); "SET v_stmntChkTxt ="
   Print #fileNo, addTab(4); "'WITH ' ||"
   Print #fileNo, addTab(5); "'V_DP ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'DATAPARTITIONNAME,' ||"
   Print #fileNo, addTab(5); "'LOWINCLUSIVE,' ||"
   Print #fileNo, addTab(5); "'LOWVALUE,' ||"
   Print #fileNo, addTab(5); "'HIGHINCLUSIVE,' ||"
   Print #fileNo, addTab(5); "'HIGHVALUE' ||"
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'AS ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(5); "'P.DATAPARTITIONNAME,' ||"
   Print #fileNo, addTab(6); "'P.LOWINCLUSIVE, ' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN "; g_qualFuncNameIsNumeric; "(P.LOWVALUE) = 1 THEN "; g_dbtOid; "(P.LOWVALUE) ELSE ' || RTRIM(CHAR(divOid_in)) || '-1 END), ' ||"
   Print #fileNo, addTab(6); "'P.HIGHINCLUSIVE, ' ||"
   Print #fileNo, addTab(6); "'(CASE WHEN "; g_qualFuncNameIsNumeric; "(P.HIGHVALUE) = 1 THEN "; g_dbtOid; "(P.HIGHVALUE) ELSE ' || RTRIM(CHAR(divOid_in)) || '+1 END) ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'SYSCAT.DATAPARTITIONS P ' ||"
   Print #fileNo, addTab(5); "'INNER JOIN ' ||"
   Print #fileNo, addTab(6); "'SYSCAT.DATAPARTITIONEXPRESSION E ' ||"
   Print #fileNo, addTab(5); "'ON ' ||"
   Print #fileNo, addTab(6); "'P.TABSCHEMA = E.TABSCHEMA AND P.TABNAME = E.TABNAME ' ||"
   Print #fileNo, addTab(5); "'WHERE ' ||"
   Print #fileNo, addTab(6); "'((VARCHAR(E.DATAPARTITIONEXPRESSION) || '','') LIKE ''%DIV_OID,'')' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'(' ||"
   Print #fileNo, addTab(7); "'COALESCE(LOWVALUE, '''') <> '''' ' ||"
   Print #fileNo, addTab(8); "'OR ' ||"
   Print #fileNo, addTab(7); "'COALESCE(HIGHVALUE, '''') <> '''' ' ||"
   Print #fileNo, addTab(6); "') ' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'TABSCHEMA = ''' || c_tabSchema || ''' ' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'TABNAME = ''' || c_tabName || ''' ' ||"
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'SELECT ' ||"
   Print #fileNo, addTab(5); "'DATAPARTITIONNAME ' ||"
   Print #fileNo, addTab(4); "'FROM ' ||"
   Print #fileNo, addTab(5); "'V_DP ' ||"
   Print #fileNo, addTab(4); "'WHERE ' ||"
   Print #fileNo, addTab(5); "'((LOWINCLUSIVE  = ''Y'' AND LOWVALUE  <= ' || RTRIM(CHAR(divOid_in)) || ') OR (LOWINCLUSIVE  <> ''Y'' AND LOWVALUE  < ' || RTRIM(CHAR(divOid_in)) || ')) ' ||"
   Print #fileNo, addTab(6); "'AND ' ||"
   Print #fileNo, addTab(5); "'((HIGHINCLUSIVE = ''Y'' AND HIGHVALUE >= ' || RTRIM(CHAR(divOid_in)) || ') OR (HIGHINCLUSIVE <> ''Y'' AND HIGHVALUE > ' || RTRIM(CHAR(divOid_in)) || ')) ' ||"
   Print #fileNo, addTab(4); "'FETCH FIRST 1 ROW ONLY'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntChkTxt;"
   Print #fileNo, addTab(3); "OPEN c;"
   Print #fileNo, addTab(3); "FETCH c INTO v_dbPartitionName;"
   Print #fileNo, addTab(3); "CLOSE c WITH RELEASE;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_dbPartitionName IS NULL THEN"
   Print #fileNo, addTab(4); "SET v_dbPartitionName = '???';"
   Print #fileNo, addTab(4); "SET v_doDelPartition = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "SET v_errMsg = 'not covered by partition';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "assemble ALTER TABLE statement", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(c_tabSchema) || '.' || c_tabName || ' DETACH PARTITION ' || v_dbPartitionName || ' INTO ' || RTRIM(c_tabSchema) || '.' || c_tabName || '_DET' || RTRIM(CHAR(divOid_in));"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementDelTabPart
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "flag,"
   Print #fileNo, addTab(4); "msg,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(CASE v_doDelPartition WHEN 0 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(4); "v_errMsg,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 AND v_doDelPartition = 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementDelTabPart
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameDeleteTablePartitionByDiv, ddlType, , "mode_in", "divOid_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 ' ### ENDIF IVK ###
 
