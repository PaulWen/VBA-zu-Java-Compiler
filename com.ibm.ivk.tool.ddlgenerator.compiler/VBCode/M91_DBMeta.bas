Attribute VB_Name = "M91_DBMeta"
Option Explicit

Private Const processingStepMeta = 4
Private Const processingStepMeta2 = 5


Sub genDbMetaDdl( _
  ddlType As DdlTypeId _
)
  genDbMetaDdl_1 ddlType
  genDbMetaDdl_2 ddlType
End Sub


Private Sub genDbMetaDdl_1( _
  ddlType As DdlTypeId _
)
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMeta, processingStepMeta, ddlType, , , , phaseDbSupport)
    
  On Error GoTo ErrorExit
  
  Const scrSchemaName As String = "srcSchema"
  Const scrTableName As String = "srcTable"
  Const maxPathLengthName As String = "maxPathLength"
  Const sequenceNoName As String = "seqNo"
  
  Dim qualFuncNamePdmSchemaName As String
  qualFuncNamePdmSchemaName = genQualFuncName(g_sectionIndexDbMeta, udfnPdmSchemaName, ddlType)

  Dim qualViewName As String
  Dim qualViewNameLdm As String
  Dim qualViewNameTabDepChain As String
  
  ' ####################################################################################################################
  ' #    Procedure asserting a condition
  ' ####################################################################################################################
  
  Dim qualProcNameAssert As String
  qualProcNameAssert = genQualProcName(g_sectionIndexDbMeta, spnAssert, ddlType)
  
  printSectionHeader "Procedure asserting a condition", fileNo
  Print #fileNo,

  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcNameAssert
  Print #fileNo, addTab(0); "("

  genProcParm fileNo, "IN", "condition_in", "VARCHAR(2048)", True, "SQL-expression"
  genProcParm fileNo, "IN", "message_in", "VARCHAR(50)", False, "exception-message used if condition does not evaluate to 'true'"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "BEGIN"

  genProcSectionHeader fileNo, "declare variables", 1, True
  genVarDecl fileNo, "v_condValue", g_dbtBoolean, "NULL"
  genVarDecl fileNo, "v_stmntText", "VARCHAR(2150)", "NULL"
  genVarDecl fileNo, "v_msg", "VARCHAR(100)", "NULL"
  
  genProcSectionHeader fileNo, "declare cursor"
  Print #fileNo, addTab(1); "DECLARE c_cond CURSOR FOR v_stmntCond;"

  genProcSectionHeader fileNo, "determine condition value of 'condition_in'"
  Print #fileNo, addTab(1); "SET v_stmntText = 'VALUES(CASE WHEN ' || condition_in || ' THEN 1 ELSE 0 END)';"

  Print #fileNo,
  Print #fileNo, addTab(1); "PREPARE v_stmntCond FROM v_stmntText;"
  Print #fileNo, addTab(1); "OPEN c_cond;"
  Print #fileNo, addTab(1); "FETCH c_cond INTO v_condValue;"
  Print #fileNo, addTab(1); "CLOSE c_cond WITH RELEASE;"
  
  genProcSectionHeader fileNo, "SIGNAL if condition does not evaluate to 'true'"
  Print #fileNo, addTab(1); "IF (v_condValue = 0) THEN"
  genSignalDdlWithParms "assertFailed", fileNo, 2, , , , , , , , , , "message_in"
  Print #fileNo, addTab(1); "END IF;"
  
  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

' ### IF IVK ###
  ' ####################################################################################################################
  ' #    Function mapping a 'Sparte' to the corresponding PS-OID
  ' ####################################################################################################################
  
  Dim qualFuncNameSparte2PsOid As String
  qualFuncNameSparte2PsOid = genQualFuncName(g_sectionIndexMeta, udfnSparte2PsOid, ddlType, , , , , , True)
  
  printSectionHeader "Function mapping a 'Sparte' to the corresponding PS-OID", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNameSparte2PsOid
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "sparte_in", "CHAR(1)", False, "SPARTE"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); g_dbtOid
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "RETURN"
  
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_dbtOid; "("; g_anValue; ")"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameRegistryStatic
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); g_anSection; " = 'MAPPING_DPLUS'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anKey; " = 'DPSPARTE2PS_OID'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anSubKey; " = sparte_in"
  Print #fileNo, addTab(1); ")"

  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
  ' ####################################################################################################################
  ' #    Function mapping a 'Sparte' to the corresponding DIV-OID
  ' ####################################################################################################################
  
  Dim qualFuncNameSparte2DivOid As String
  qualFuncNameSparte2DivOid = genQualFuncName(g_sectionIndexMeta, udfnSparte2DivOid, ddlType, , , , , , True)
  
  printSectionHeader "Function mapping a 'Sparte' to the corresponding DIV-OID", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNameSparte2DivOid
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "sparte_in", "CHAR(1)", False, "SPARTE"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); g_dbtOid
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "RETURN"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_dbtOid; "("; g_anValue; ")"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameRegistryStatic
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); g_anSection; " = 'MAPPING_DPLUS'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anKey; " = 'DPSPARTE2DIV_OID'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anSubKey; " = sparte_in"
  Print #fileNo, addTab(1); ")"

  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
  ' ####################################################################################################################
  ' #    Function mapping a PS-OID to the corresponding 'Sparte'
  ' ####################################################################################################################
  
  Dim qualFuncNamePsOid2Sparte As String
  qualFuncNamePsOid2Sparte = genQualFuncName(g_sectionIndexMeta, udfnPsOid2Sparte, ddlType, , , , , , True)
  
  printSectionHeader "Function mapping a PS-OID to the corresponding 'Sparte'", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNamePsOid2Sparte
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "psOid_in", g_dbtOid, False, "OID of ProductStructure"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); "CHAR(1)"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "RETURN"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_anSubKey
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameRegistryStatic
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); g_anSection; " = 'MAPPING_DPLUS'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anKey; " = 'DPSPARTE2PS_OID'"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); ""; g_anValue; " = CHAR(psOid_in)"
  Print #fileNo, addTab(1); ")"

  Print #fileNo, addTab(0); gc_sqlCmdDelim
                 
' ### ENDIF IVK ###
  ' ####################################################################################################################
  ' #    Function returning the PDM schema name for a given ACM section, organization and data pool
  ' ####################################################################################################################
  
  printSectionHeader "Function returning the PDM schema name for a given ACM section, organization and data pool", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNamePdmSchemaName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "acmSection_in", "VARCHAR(20)", True, "name of the ACM-section"
  genProcParm fileNo, "", "orgId_in", "INTEGER", True, "ID of the organization"
  genProcParm fileNo, "", "poolId_in", "INTEGER", False, "ID of the data pool"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); "VARCHAR(10)"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "RETURN"
  Print #fileNo, addTab(0); "( SELECT CAST(REPLACE('"; genSchemaName("<$$$>", "<$$$>", ddlType); "', '<$$$>', SECTIONSHORTNAME) AS VARCHAR(50)) FROM "; g_qualTabNameAcmSection; " WHERE SECTIONNAME = acmSection_in ) ||"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "CASE WHEN"
  Print #fileNo, addTab(2); "orgId_in IS NULL"
  Print #fileNo, addTab(1); "THEN"
  Print #fileNo, addTab(2); "''"
  Print #fileNo, addTab(1); "ELSE"
  Print #fileNo, addTab(2); "RIGHT(RTRIM('00' || CAST(orgId_in AS CHAR(2))),2) ||"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "CASE WHEN"
  Print #fileNo, addTab(4); "poolId_in IS NULL"
  Print #fileNo, addTab(3); "THEN"
  Print #fileNo, addTab(4); "''"
  Print #fileNo, addTab(3); "ELSE"
  Print #fileNo, addTab(4); "CAST(poolId_in AS CHAR(1))"
  Print #fileNo, addTab(3); "END"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); "END"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
  ' ####################################################################################################################
  ' #    Function returning the ','-separated list of column names of a database table prefixed by an optional column prefix
  ' ####################################################################################################################
  
  Dim qualFuncNameDbTabColList As String
  qualFuncNameDbTabColList = genQualFuncName(g_sectionIndexDbMeta, udfnDbTabColList, ddlType, , , , , , True)

  printSectionHeader "Function returning the ','-separated list of column names of a database table prefixed by an optional column prefix", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNameDbTabColList
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "tabSchema_in", g_dbtDbSchemaName, True, "table schema name"
  genProcParm fileNo, "", "tabName_in", g_dbtDbTableName, True, "table name"
  genProcParm fileNo, "", "prefix_in", "VARCHAR(20)", False, "(optional) column prefix"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); "VARCHAR(8000)"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "BEGIN ATOMIC"

  genProcSectionHeader fileNo, "declare variables"
  genVarDecl fileNo, "v_colList", "VARCHAR(8000)", "''"

  genProcSectionHeader fileNo, "add each column name of the table"
  Print #fileNo, addTab(1); "FOR tabLoop AS"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "COLNAME"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); "SYSCAT.COLUMNS"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "TABSCHEMA = tabSchema_in"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "TABNAME = tabName_in"
  Print #fileNo, addTab(2); "ORDER BY"
  Print #fileNo, addTab(3); "COLNO ASC"
  Print #fileNo, addTab(1); "DO"
  Print #fileNo, addTab(2); "SET v_colList = v_colList || (CASE v_colList WHEN '' THEN '' ELSE ',' END) || COALESCE(prefix_in, '') || COLNAME;"
  Print #fileNo, addTab(1); "END FOR;"
  Print #fileNo,
  Print #fileNo, addTab(1); "RETURN v_colList;"
  Print #fileNo, addTab(0); "END"
  
  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
  ' ####################################################################################################################
  ' #    View for LDM table dependency chains based on foreign keys
  ' ####################################################################################################################
    
  qualViewNameTabDepChain = genQualViewName(g_sectionIndexDbMeta, vnLdmTabDepChain, vnsLdmTabDepChain, ddlType)
      
  printSectionHeader "View for LDM table dependency chains based on foreign keys", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewNameTabDepChain
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(2); scrSchemaName; ","
  Print #fileNo, addTab(2); scrTableName; ","
  Print #fileNo, addTab(2); "DSTSCHEMA,"
  Print #fileNo, addTab(2); "DSTTABLE,"
  Print #fileNo, addTab(2); "PATHLENGTH,"
  Print #fileNo, addTab(2); "PATH"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(1); "WITH"
  Print #fileNo, addTab(2); "V_Tab"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "srcSchema,"
  Print #fileNo, addTab(2); "srcTable,"
  Print #fileNo, addTab(2); "dstSchema,"
  Print #fileNo, addTab(2); "dstTable,"
  Print #fileNo, addTab(2); "path"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_anLdmSchemaName; ","
  Print #fileNo, addTab(3); g_anLdmTableName; ","
  Print #fileNo, addTab(3); "CAST('' AS VARCHAR(1)),"
  Print #fileNo, addTab(3); "CAST('' AS VARCHAR(1)),"
  Print #fileNo, addTab(3); "CAST('' AS VARCHAR(1))"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameLdmTable
  Print #fileNo, addTab(1); "),"
  Print #fileNo, addTab(2); "V_Dep"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "srcSchema,"
  Print #fileNo, addTab(2); "srcTable,"
  Print #fileNo, addTab(2); "dstSchema,"
  Print #fileNo, addTab(2); "dstTable"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "SRC_SCHEMANAME,"
  Print #fileNo, addTab(3); "SRC_TABLENAME,"
  Print #fileNo, addTab(3); "DST_SCHEMANAME,"
  Print #fileNo, addTab(3); "DST_TABLENAME"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameFkDependency
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); g_anAcmIsEnforced; " = "; gc_dbTrue
  Print #fileNo, addTab(4); "AND NOT"
  Print #fileNo, addTab(3); "(SRC_SCHEMANAME=DST_SCHEMANAME AND SRC_TABLENAME=DST_TABLENAME)"
  Print #fileNo, addTab(1); "),"
  Print #fileNo, addTab(2); "V_DepClosure"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "srcSchema,"
  Print #fileNo, addTab(2); "srcTable,"
  Print #fileNo, addTab(2); "dstSchema,"
  Print #fileNo, addTab(2); "dstTable,"
  Print #fileNo, addTab(2); "pathLength,"
  Print #fileNo, addTab(2); "path"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "srcSchema,"
  Print #fileNo, addTab(4); "srcTable,"
  Print #fileNo, addTab(4); "dstSchema,"
  Print #fileNo, addTab(4); "dstTable,"
  Print #fileNo, addTab(4); "1,"
  Print #fileNo, addTab(4); "CAST(SrcSchema || '.' || SrcTable || '->' || DstSchema || '.' || DstTable AS VARCHAR(2000))"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); "V_Dep"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "UNION ALL"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "T.srcSchema,"
  Print #fileNo, addTab(4); "T.srcTable,"
  Print #fileNo, addTab(4); "D.dstSchema,"
  Print #fileNo, addTab(4); "D.dstTable,"
  Print #fileNo, addTab(4); "D.pathlength + 1,"
  Print #fileNo, addTab(4); "T.srcSchema || '.' || T.SrcTable || '->' || D.Path"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); "V_Dep         T,"
  Print #fileNo, addTab(4); "V_DepClosure  D"
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "T.dstSchema = D.srcSchema"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "T.dstTable = D.srcTable"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "D.pathLength < 50"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); "),"
  Print #fileNo, addTab(2); "V_DepClosureAll"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "srcSchema,"
  Print #fileNo, addTab(2); "srcTable,"
  Print #fileNo, addTab(2); "dstSchema,"
  Print #fileNo, addTab(2); "dstTable,"
  Print #fileNo, addTab(2); "pathLength,"
  Print #fileNo, addTab(2); "path"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "srcSchema,"
  Print #fileNo, addTab(4); "srcTable,"
  Print #fileNo, addTab(4); "dstSchema,"
  Print #fileNo, addTab(4); "dstTable,"
  Print #fileNo, addTab(4); "pathLength,"
  Print #fileNo, addTab(4); "path"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); "V_DepClosure"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "UNION ALL"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "srcSchema,"
  Print #fileNo, addTab(4); "srcTable,"
  Print #fileNo, addTab(4); "dstSchema,"
  Print #fileNo, addTab(4); "dstTable,"
  Print #fileNo, addTab(4); "0,"
  Print #fileNo, addTab(4); "path"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); "V_Tab"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "*"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); "V_DepClosureAll"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    View order LDM-tables according to their involvement in foreign key chains
  ' ####################################################################################################################
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnLdmTabDepOrder, vnsLdmTabDepOrder, ddlType)
      
  printSectionHeader "View ordering LDM-tables according to their involvement in foreign key chains", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); scrSchemaName; ","
  Print #fileNo, addTab(1); scrTableName; ","
  Print #fileNo, addTab(1); maxPathLengthName; ","
  Print #fileNo, addTab(1); sequenceNoName
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(1); "WITH"
  Print #fileNo, addTab(2); "V_DepClosureMaxLevelFromTop"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); scrSchemaName; ","
  Print #fileNo, addTab(2); scrTableName; ","
  Print #fileNo, addTab(2); maxPathLengthName
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "SRCSCHEMA,"
  Print #fileNo, addTab(3); "SRCTABLE,"
  Print #fileNo, addTab(3); "MAX(PATHLENGTH)"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); qualViewNameTabDepChain
  Print #fileNo, addTab(2); "GROUP BY"
  Print #fileNo, addTab(3); "SRCSCHEMA,"
  Print #fileNo, addTab(3); "SRCTABLE"
  Print #fileNo, addTab(1); "),"
  Print #fileNo, addTab(2); "V_TabsOrderedByDependencies"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "srcSchema,"
  Print #fileNo, addTab(2); "srcTable,"
  Print #fileNo, addTab(2); "maxLevel,"
  Print #fileNo, addTab(2); "seqNo"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "AS"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "srcSchema,"
  Print #fileNo, addTab(3); "srcTable,"
  Print #fileNo, addTab(3); "maxPathLength,"
  Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY maxPathLength ASC, srcSchema ASC, srcTable ASC)"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); "V_DepClosureMaxLevelFromTop"
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "*"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); "V_TabsOrderedByDependencies"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    View relating ACM-Entities to Foreign-Key-names implenting relationships
  ' ####################################################################################################################
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnAcmEntityFkCol, vnsAcmEntityFkCol, ddlType)
  printSectionHeader "View relating ACM-Entities to Foreign-Key-names implenting relationships", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); g_anAcmEntitySection; ","
  Print #fileNo, addTab(1); g_anAcmEntityName; ","
  Print #fileNo, addTab(1); g_anAcmEntityId; ","
  Print #fileNo, addTab(1); g_anAcmEntityType; ","
  Print #fileNo, addTab(1); "REFENTITYSECTION,"
  Print #fileNo, addTab(1); "REFENTITYNAME,"
  Print #fileNo, addTab(1); "REFENTITYID,"
  Print #fileNo, addTab(1); "REFENTITYTYPE,"
  Print #fileNo, addTab(1); "FKCOL"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(1); "SELECT DISTINCT"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ERightPar."; g_anAcmEntitySection; " WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ELeftPar."; g_anAcmEntitySection; "  ELSE EPar."; g_anAcmEntitySection; "     END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ERightPar."; g_anAcmEntityName; "    WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityName; "     ELSE EPar."; g_anAcmEntityName; "        END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ERightPar."; g_anAcmEntityId; "      WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityId; "       ELSE EPar."; g_anAcmEntityId; "          END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ERightPar."; g_anAcmEntityType; "    WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityType; "     ELSE EPar."; g_anAcmEntityType; "        END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ELeftPar."; g_anAcmEntitySection; "  WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ERightPar."; g_anAcmEntitySection; " ELSE ELeftPar."; g_anAcmEntitySection; " END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityName; "     WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ERightPar."; g_anAcmEntityName; "    ELSE ELeftPar."; g_anAcmEntityName; "    END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityId; "       WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ERightPar."; g_anAcmEntityId; "      ELSE ELeftPar."; g_anAcmEntityId; "      END),"
  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN ELeftPar."; g_anAcmEntityType; "     WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN ERightPar."; g_anAcmEntityType; "    ELSE ELeftPar."; g_anAcmEntityType; "    END),"

  Print #fileNo, addTab(2); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 OR E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(E."; g_anAcmAliasShortName; ", E."; g_anAcmEntityShortName; ") ELSE '' END ) ||"
  Print #fileNo, addTab(3); "(CASE WHEN E."; g_anAcmMaxLeftCardinality; " = 1 THEN E."; g_anAcmRlShortName; " WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN E."; g_anAcmLrShortName; " ELSE ELeft."; g_anAcmEntityShortName; " END) || '_OID'"

  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " E"
  Print #fileNo, addTab(1); "INNER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " EPar"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "EPar."; g_anAcmEntityName; " = COALESCE(E."; g_anAcmOrParEntityName; ", E."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "EPar."; g_anAcmEntitySection; " = COALESCE(E."; g_anAcmOrParEntitySection; ", E."; g_anAcmEntitySection; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "EPar."; g_anAcmEntityType; " = E."; g_anAcmEntityType
  Print #fileNo, addTab(1); "INNER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " ELeft"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "E."; g_anAcmLeftEntityName; " = ELeft."; g_anAcmEntityName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmLeftEntitySection; " = ELeft."; g_anAcmEntitySection
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmLeftEntityType; " = ELeft."; g_anAcmEntityType
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "OR"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "(E."; g_anAcmMaxLeftCardinality; " IS NULL AND E."; g_anAcmMaxRightCardinality; " IS NULL)"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmRightEntityName; " = ELeft."; g_anAcmEntityName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmRightEntitySection; " = ELeft."; g_anAcmEntitySection
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmRightEntityType; " = ELeft."; g_anAcmEntityType
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); "INNER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " ERight"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "E."; g_anAcmRightEntityName; " = ERight. "; g_anAcmEntityName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "E."; g_anAcmRightEntitySection; " = ERight."; g_anAcmEntitySection
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "E."; g_anAcmRightEntityType; " = ERight."; g_anAcmEntityType
  Print #fileNo, addTab(1); "INNER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " ELeftPar"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "ELeftPar."; g_anAcmEntityName; " = COALESCE(ELeft."; g_anAcmOrParEntityName; ", ELeft."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "ELeftPar."; g_anAcmEntitySection; " = COALESCE(ELeft."; g_anAcmOrParEntitySection; ", ELeft."; g_anAcmEntitySection; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "ELeftPar."; g_anAcmEntityType; " = ELeft."; g_anAcmEntityType
  Print #fileNo, addTab(1); "INNER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " ERightPar"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "ERightPar."; g_anAcmEntityName; " = COALESCE(ERight."; g_anAcmOrParEntityName; ", ERight."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "ERightPar."; g_anAcmEntitySection; " = COALESCE(ERight."; g_anAcmOrParEntitySection; ", ERight."; g_anAcmEntitySection; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "ERightPar."; g_anAcmEntityType; " = ERight."; g_anAcmEntityType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "E."; g_anAcmEntityType; " = 'R'"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "E."; g_anAcmIsEnforced; " = "; gc_dbTrue
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "E."; g_anAcmMaxRightCardinality; " = "; gc_dbTrue
  Print #fileNo, addTab(4); "OR"
  Print #fileNo, addTab(3); "("
  Print #fileNo, addTab(4); "E."; g_anAcmMaxLeftCardinality; " = 1"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "NOT (E."; g_anAcmMaxRightCardinality; " = 1)"
  Print #fileNo, addTab(3); ")"
  Print #fileNo, addTab(4); "OR"
  Print #fileNo, addTab(3); "("
  Print #fileNo, addTab(4); "E."; g_anAcmMaxLeftCardinality; " IS NULL"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "E."; g_anAcmMaxRightCardinality; " IS NULL"
  Print #fileNo, addTab(3); ")"
  Print #fileNo, addTab(2); ")"
  
  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
  ' ####################################################################################################################
  ' #    View relating PDM-tables to their LDM-tables and ACM-entities
  ' ####################################################################################################################
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)
  printSectionHeader "View relating PDM-tables to their LDM-tables and ACM-entities", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  
  Print #fileNo, addTab(1); "ENTITY_SECTION,"
  Print #fileNo, addTab(1); "ENTITY_NAME,"
  Print #fileNo, addTab(1); "ENTITY_TYPE,"
  Print #fileNo, addTab(1); "ENTITY_ID,"
  Print #fileNo, addTab(1); "ENTITY_ISCTO,"
  Print #fileNo, addTab(1); "ENTITY_ISCTP,"
  Print #fileNo, addTab(1); "ENTITY_ISLRT,"
  Print #fileNo, addTab(1); "ENTITY_ISRANGEPARTALL,"
  Print #fileNo, addTab(1); "ENTITY_ISGEN,"
' ### IF IVK ###
  Print #fileNo, addTab(1); "ENTITY_ISPS,"
  Print #fileNo, addTab(1); "ENTITY_ISPSFORMING,"
' ### ENDIF IVK ###
  Print #fileNo, addTab(1); "ENTITY_ISLOGCHANGE,"
  Print #fileNo, addTab(1); "ENTITY_ISABSTRACT,"
  Print #fileNo, addTab(1); "ENTITY_PARSECTION,"
  Print #fileNo, addTab(1); "ENTITY_PARNAME,"
  Print #fileNo, addTab(1); "REL_MINLEFTCARDINALITY,"
  Print #fileNo, addTab(1); "REL_MAXLEFTCARDINALITY,"
  Print #fileNo, addTab(1); "REL_LEFT_ENTITYSECTION,"
  Print #fileNo, addTab(1); "REL_LEFT_ENTITYNAME,"
  Print #fileNo, addTab(1); "REL_LEFT_ENTITYTYPE,"
  Print #fileNo, addTab(1); "REL_MINRIGHTCARDINALITY,"
  Print #fileNo, addTab(1); "REL_MAXRIGHTCARDINALITY,"
  Print #fileNo, addTab(1); "REL_RIGHT_ENTITYSECTION,"
  Print #fileNo, addTab(1); "REL_RIGHT_ENTITYNAME,"
  Print #fileNo, addTab(1); "REL_RIGHT_ENTITYTYPE,"
  Print #fileNo, addTab(1); "LDM_SCHEMANAME,"
  Print #fileNo, addTab(1); "LDM_TABLENAME,"
  Print #fileNo, addTab(1); "LDM_FKSEQUENCENO,"
  Print #fileNo, addTab(1); "LDM_ISLRT,"
  Print #fileNo, addTab(1); "LDM_ISNL,"
  Print #fileNo, addTab(1); "LDM_ISGEN,"
  Print #fileNo, addTab(1); "PDM_SCHEMANAME,"
  Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
  Print #fileNo, addTab(1); "PDM_"; g_anOrganizationId; ","
  Print #fileNo, addTab(1); "PDM_POOLTYPE_ID"
  
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(1); "SELECT"
  
  Print #fileNo, addTab(2); "AE."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmEntityName; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmEntityType; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmEntityId; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsCto; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsCtp; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsLrt; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsRangePartAll; ","
  Print #fileNo, addTab(2); "AE."; g_anLdmIsGen; ","
' ### IF IVK ###
  Print #fileNo, addTab(2); "AE."; g_anAcmIsPs; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsPsForming; ","
' ### ENDIF IVK ###
  Print #fileNo, addTab(2); "AE."; g_anAcmIsLogChange; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmIsAbstract; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmOrParEntitySection; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmOrParEntityName; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmMinLeftCardinality; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmMaxLeftCardinality; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmLeftEntitySection; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmLeftEntityName; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmLeftEntityType; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmMinRightCardinality; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmMaxRightCardinality; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmRightEntitySection; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmRightEntityName; ","
  Print #fileNo, addTab(2); "AE."; g_anAcmRightEntityType; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmSchemaName; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmTableName; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmFkSequenceNo; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmIsLrt; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmIsNl; ","
  Print #fileNo, addTab(2); "LT."; g_anLdmIsGen; ","
  Print #fileNo, addTab(2); "PT."; g_anPdmFkSchemaName; ","
  Print #fileNo, addTab(2); "PT."; g_anPdmTableName; ","
  Print #fileNo, addTab(2); "PT."; g_anOrganizationId; ","
  Print #fileNo, addTab(2); "PT."; g_anPoolTypeId
  
  Print #fileNo, addTab(1); "FROM"
  
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " AE,"
  Print #fileNo, addTab(2); g_qualTabNameLdmTable; " LT,"
  Print #fileNo, addTab(2); g_qualTabNamePdmTable; " PT"
  
  Print #fileNo, addTab(1); "WHERE"
  
  Print #fileNo, addTab(2); "LT."; g_anAcmEntitySection; " = COALESCE(AE."; g_anAcmOrParEntitySection; ", AE."; g_anAcmEntitySection; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "LT."; g_anAcmEntityName; " = COALESCE(AE."; g_anAcmOrParEntityName; ", AE."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "LT."; g_anAcmEntityType; " = COALESCE(AE."; g_anAcmOrParEntityType; ", AE."; g_anAcmEntityType; ")"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkSchemaName; " = LT."; g_anLdmSchemaName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkTableName; " = LT."; g_anLdmTableName
  
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    Function retrieving ','-separated list of LOB columns per ACM-entity
  ' ####################################################################################################################

  Dim qualFuncNameLobAttrs As String
  qualFuncNameLobAttrs = genQualFuncName(g_sectionIndexDbMeta, udfnAcmLobAttrs, ddlType)
  
  printSectionHeader "Function retrieving ','-separated list of LOB columns per ACM-entity", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNameLobAttrs
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "acmEntitySection_in", "VARCHAR(20)", True, "section name of the ACM-entity"
  genProcParm fileNo, "", "acmEntityName_in", "VARCHAR(50)", True, "name of the ACM-entity"
  genProcParm fileNo, "", "acmEntityType_in", g_dbtEntityType, False, "type of the ACM-entity"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); "VARCHAR(1024)"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "BEGIN ATOMIC"
    
  genProcSectionHeader fileNo, "declare variables"
  genVarDecl fileNo, "v_attrNameList", "VARCHAR(1024)", "NULL"
  
  genProcSectionHeader fileNo, "loop over ACM attributes related to th given ACM-entity"
  Print #fileNo, addTab(1); "FOR attrLoop AS"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "A."; g_anAcmAttributeName
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameAcmAttribute; " A,"
  Print #fileNo, addTab(3); g_qualTabNameAcmDomain; " D"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = acmEntitySection_in"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = acmEntityName_in"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = acmEntityType_in"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmDomainSection; " = D."; g_anAcmDomainSection
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmDomainName; " = D."; g_anAcmDomainName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "D."; g_anAcmDbDataType; " LIKE '%LOB%'"
  Print #fileNo, addTab(2); "ORDER BY"
  Print #fileNo, addTab(3); "A."; g_anLdmSequenceNo
  Print #fileNo, addTab(1); "DO"
  Print #fileNo, addTab(2); "SET v_attrNameList = COALESCE(v_attrNameList, '');"
  Print #fileNo, addTab(2); "SET v_attrNameList = RTRIM(LEFT(v_attrNameList || (CASE v_attrNameList WHEN '' THEN '' ELSE ',' END) || "; g_anAcmAttributeName; ", 1024));"
  Print #fileNo, addTab(1); "END FOR;"
  Print #fileNo,
  Print #fileNo, addTab(1); "RETURN v_attrNameList;"
  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim
  
' ### IF IVK ###
  ' ####################################################################################################################
  ' #    View mapping ACM-entity-names to XSD-generating UDFs
  ' ####################################################################################################################
  
  Dim poolIdsSupportingXmlExport As String
  poolIdsSupportingXmlExport = ""
  Dim i As Integer
  For i = 1 To g_pools.numDescriptors
    With g_pools.descriptors(i)
      If .supportXmlExport Then
        poolIdsSupportingXmlExport = poolIdsSupportingXmlExport & IIf(poolIdsSupportingXmlExport = "", "", ",") & .id
      End If
    End With
  Next i
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnXsdFuncMap, vsnXsdFuncMap, ddlType)
  
  printSectionHeader "View mapping ACM-entity-names to XSD-generating UDFs", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "acmEntitySection,"
  Print #fileNo, addTab(1); "acmEntityName,"
  Print #fileNo, addTab(1); "acmEntityType,"
  Print #fileNo, addTab(1); "xsdFuncSchema,"
  Print #fileNo, addTab(1); "xsdFuncName,"
  Print #fileNo, addTab(1); "blobAttributes"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "WITH"
  Print #fileNo, addTab(1); "V_AcmEntity"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "entitySection,"
  Print #fileNo, addTab(1); "entityName,"
  Print #fileNo, addTab(1); "entityShortName,"
  Print #fileNo, addTab(1); "entityType,"
  Print #fileNo, addTab(1); "isAbstract,"
  Print #fileNo, addTab(1); "parEntitySection,"
  Print #fileNo, addTab(1); "parEntityName"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityShortName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityType; ","
  Print #fileNo, addTab(2); "A."; g_anAcmIsAbstract; ","
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntitySection; ", A."; g_anAcmEntitySection; "),"
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntityName; ", A."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
  Print #fileNo, addTab(1); "LEFT OUTER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " PAR"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntitySection; " = PAR."; g_anAcmEntitySection
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityName; " = PAR."; g_anAcmEntityName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityType; " = PAR."; g_anAcmEntityType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "A."; g_anAcmSupportXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmUseXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "SELECT"
  Print #fileNo, addTab(1); "A.entitySection,"
  Print #fileNo, addTab(1); "A.entityName,"
  Print #fileNo, addTab(1); "A.entityType,"
  Print #fileNo, addTab(1); qualFuncNamePdmSchemaName; "(S.SECTIONNAME, CAST(NULL AS INTEGER), CAST(NULL AS INTEGER)),"
  Print #fileNo, addTab(1); "CAST('F_' || A.entityShortName || '_"; UCase(gc_xsdObjNameSuffix); "' AS VARCHAR(20)),"
  Print #fileNo, addTab(1); qualFuncNameLobAttrs; "(A.entitySection, A.entityName, A.entityType)"
  Print #fileNo, addTab(0); "FROM"
  Print #fileNo, addTab(1); g_qualTabNameLdmTable; " L,"
  Print #fileNo, addTab(1); g_qualTabNamePdmTable; " P,"
  Print #fileNo, addTab(1); g_qualTabNameAcmSection; " S,"
  Print #fileNo, addTab(1); "V_AcmEntity A"
  Print #fileNo, addTab(0); "WHERE"
  Print #fileNo, addTab(1); "A.entitySection = S.SECTIONNAME"
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.parEntitySection = L."; g_anAcmEntitySection
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.parEntityName = L."; g_anAcmEntityName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.entityType = L."; g_anAcmEntityType
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsNl; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsGen; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "((P."; g_anOrganizationId; " = "; CStr(g_primaryOrgId); ") OR (P."; g_anOrganizationId; " IS NULL))"
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "((P."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType); ") OR (P."; g_anPoolTypeId; " IS NULL))"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnXsdFuncMap, vsnXsdFuncMap, edtLdm)
  genAliasDdl g_sectionIndexDbMeta, vnXsdFuncMap, _
                    True, True, False, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                    "View mapping ACM-entity-names to XSD-generating UDFs"

  ' ####################################################################################################################
  ' #    View mapping ACM-entity-names to XML-generating UDFs
  ' ####################################################################################################################
    
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnXmlFuncMap, vsnXmlFuncMap, ddlType)
      
  printSectionHeader "View mapping ACM-entity-names to XML-generating UDFs", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "acmEntitySection,"
  Print #fileNo, addTab(1); "acmEntityName,"
  Print #fileNo, addTab(1); "acmEntityType,"
  Print #fileNo, addTab(1); "isPs,"
  Print #fileNo, addTab(1); "ldmSchemaName,"
  Print #fileNo, addTab(1); "ldmTableName,"
  Print #fileNo, addTab(1); "orgId,"
  Print #fileNo, addTab(1); "orgOid,"
  Print #fileNo, addTab(1); "poolId,"
  Print #fileNo, addTab(1); "xmlFuncSchema,"
  Print #fileNo, addTab(1); "xmlFuncName,"
  Print #fileNo, addTab(1); "blobAttributes"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "WITH"
  Print #fileNo, addTab(1); "V_AcmEntity"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "entitySection,"
  Print #fileNo, addTab(1); "entityName,"
  Print #fileNo, addTab(1); "entityShortName,"
  Print #fileNo, addTab(1); "entityType,"
  Print #fileNo, addTab(1); "isAbstract,"
  Print #fileNo, addTab(1); "isPs,"
  Print #fileNo, addTab(1); "parEntitySection,"
  Print #fileNo, addTab(1); "parEntityName,"
  Print #fileNo, addTab(1); "parEntityShortName"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityShortName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityType; ","
  Print #fileNo, addTab(2); "A."; g_anAcmIsAbstract; ","
  Print #fileNo, addTab(2); "A."; g_anAcmIsPs; ","
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntitySection; ", A."; g_anAcmEntitySection; "),"
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntityName; ", A."; g_anAcmEntityName; "),"
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntityShortName; ", A."; g_anAcmEntityShortName; ")"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
  Print #fileNo, addTab(1); "LEFT OUTER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " PAR"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntitySection; " = PAR."; g_anAcmEntitySection
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityName; " = PAR."; g_anAcmEntityName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityType; " = PAR."; g_anAcmEntityType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "A."; g_anAcmSupportXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmUseXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "SELECT"
  Print #fileNo, addTab(1); "A.entitySection,"
  Print #fileNo, addTab(1); "A.entityName,"
  Print #fileNo, addTab(1); "A.entityType,"
  Print #fileNo, addTab(1); "A.isPs,"
  Print #fileNo, addTab(1); "L.schemaName,"
  Print #fileNo, addTab(1); "L.tableName,"
  Print #fileNo, addTab(1); "P."; g_anOrganizationId; ","
  Print #fileNo, addTab(1); "O.ORGOID,"
  Print #fileNo, addTab(1); "P."; g_anPoolTypeId; ","
  Print #fileNo, addTab(1); qualFuncNamePdmSchemaName; "(S.SECTIONNAME, P."; g_anOrganizationId; ", P."; g_anPoolTypeId; "),"
  Print #fileNo, addTab(1); "CAST('F_' || A."; g_anAcmEntityShortName; " || '_"; UCase(gc_xmlObjNameSuffix); "' AS VARCHAR(20)),"
  Print #fileNo, addTab(1); qualFuncNameLobAttrs; "(A."; g_anAcmEntitySection; ", A."; g_anAcmEntityName; ", A."; g_anAcmEntityType; ")"
  Print #fileNo, addTab(0); "FROM"
  Print #fileNo, addTab(1); "V_AcmEntity A"
  Print #fileNo, addTab(0); "INNER JOIN"
  Print #fileNo, addTab(1); g_qualTabNameAcmSection; " S"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "A.entitySection = S.SECTIONNAME"
  Print #fileNo, addTab(0); "INNER JOIN"
  Print #fileNo, addTab(1); g_qualTabNameLdmTable; " L"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "A.parEntitySection = L."; g_anAcmEntitySection
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.parEntityName = L."; g_anAcmEntityName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.entityType = L."; g_anAcmEntityType
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsNl; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsGen; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
  Print #fileNo, addTab(0); "LEFT OUTER JOIN"
  Print #fileNo, addTab(1); g_qualTabNamePdmTable; " P"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "((P."; g_anPoolTypeId; " IN ("; poolIdsSupportingXmlExport; ")) OR (P."; g_anPoolTypeId; " IS NULL))"
  Print #fileNo, addTab(0); "LEFT OUTER JOIN"
  Print #fileNo, addTab(1); g_qualTabNamePdmOrganization; " O"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "P."; g_anOrganizationId; " = O.ID"

  Print #fileNo, addTab(0); gc_sqlCmdDelim

  qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnXmlFuncMap, vsnXmlFuncMap, edtLdm)
  genAliasDdl g_sectionIndexDbMeta, vnXmlFuncMap, _
                    True, True, False, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                    "View mapping ACM-entity-names to XML-generating UDFs"
  
  ' ####################################################################################################################
  ' #    View mapping ACM-entity-names to XML-generating Views
  ' ####################################################################################################################
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnXmlViewMap, vsnXmlViewMap, ddlType)
      
  printSectionHeader "View mapping ACM-entity-names to XML-generating Views", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "acmEntitySection,"
  Print #fileNo, addTab(1); "acmEntityName,"
  Print #fileNo, addTab(1); "acmEntityType,"
  Print #fileNo, addTab(1); "isPs,"
  Print #fileNo, addTab(1); "ldmSchemaName,"
  Print #fileNo, addTab(1); "ldmTableName,"
  Print #fileNo, addTab(1); "orgId,"
  Print #fileNo, addTab(1); "orgOId,"
  Print #fileNo, addTab(1); "poolId,"
  Print #fileNo, addTab(1); "xmlViewSchema,"
  Print #fileNo, addTab(1); "xmlViewName,"
  Print #fileNo, addTab(1); "blobAttributes"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "WITH"
  Print #fileNo, addTab(1); "V_AcmEntity"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "entitySection,"
  Print #fileNo, addTab(1); "entityName,"
  Print #fileNo, addTab(1); "entityShortName,"
  Print #fileNo, addTab(1); "entityType,"
  Print #fileNo, addTab(1); "isAbstract,"
  Print #fileNo, addTab(1); "isPs,"
  Print #fileNo, addTab(1); "parEntitySection,"
  Print #fileNo, addTab(1); "parEntityName"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityShortName; ","
  Print #fileNo, addTab(2); "A."; g_anAcmEntityType; ","
  Print #fileNo, addTab(2); "A."; g_anAcmIsAbstract; ","
  Print #fileNo, addTab(2); "A."; g_anAcmIsPs; ","
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntitySection; ", A."; g_anAcmEntitySection; "),"
  Print #fileNo, addTab(2); "COALESCE(PAR."; g_anAcmEntityName; ", A."; g_anAcmEntityName; ")"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
  Print #fileNo, addTab(1); "LEFT OUTER JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " PAR"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntitySection; " = PAR."; g_anAcmEntitySection
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityName; " = PAR."; g_anAcmEntityName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmOrParEntityType; " = PAR."; g_anAcmEntityType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "A."; g_anAcmSupportXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "A."; g_anAcmUseXmlExport; " = "; gc_dbTrue
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "SELECT"
  Print #fileNo, addTab(1); "A.entitySection,"
  Print #fileNo, addTab(1); "A.entityName,"
  Print #fileNo, addTab(1); "A.entityType,"
  Print #fileNo, addTab(1); "A.isPs,"
  Print #fileNo, addTab(1); "L."; g_anLdmSchemaName; ","
  Print #fileNo, addTab(1); "L."; g_anLdmTableName; ","
  Print #fileNo, addTab(1); "P."; g_anOrganizationId; ","
  Print #fileNo, addTab(1); "O.ORGOID,"
  Print #fileNo, addTab(1); "P."; g_anPoolTypeId; ","
  Print #fileNo, addTab(1); qualFuncNamePdmSchemaName; "(S.SECTIONNAME, P."; g_anOrganizationId; ", P."; g_anPoolTypeId; "),"
  Print #fileNo, addTab(1); "CAST('V_' || A."; g_anAcmEntityName; " || '_"; UCase(gc_xmlObjNameSuffix); "' AS VARCHAR(60)),"
  Print #fileNo, addTab(1); qualFuncNameLobAttrs; "(A."; g_anAcmEntitySection; ", A."; g_anAcmEntityName; ", A."; g_anAcmEntityType; ")"
  Print #fileNo, addTab(0); "FROM "
  Print #fileNo, addTab(1); "V_AcmEntity A"
  Print #fileNo, addTab(0); "INNER JOIN"
  Print #fileNo, addTab(1); g_qualTabNameAcmSection; " S"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "A.entitySection = S.SECTIONNAME"
  Print #fileNo, addTab(0); "INNER JOIN"
  Print #fileNo, addTab(1); g_qualTabNameLdmTable; " L"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "A.parEntitySection = L."; g_anAcmEntitySection
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.parEntityName = L."; g_anAcmEntityName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "A.entityType = L."; g_anAcmEntityType
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsNl; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsGen; " = "; gc_dbFalse
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
  Print #fileNo, addTab(0); "INNER JOIN"
  Print #fileNo, addTab(1); g_qualTabNamePdmTable; " P"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
  Print #fileNo, addTab(2); "AND"
  Print #fileNo, addTab(1); "((P."; g_anPoolTypeId; " IN ("; poolIdsSupportingXmlExport; ")) OR (P."; g_anPoolTypeId; " IS NULL))"
  Print #fileNo, addTab(0); "LEFT OUTER JOIN"
  Print #fileNo, addTab(1); g_qualTabNamePdmOrganization; " O"
  Print #fileNo, addTab(0); "ON"
  Print #fileNo, addTab(1); "P."; g_anOrganizationId; " = O.ID"
    
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnXmlViewMap, vsnXmlViewMap, edtLdm)
  genAliasDdl g_sectionIndexDbMeta, vnXmlViewMap, _
                    True, True, False, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                    "View mapping ACM-entity-names to XML-generating Views"

' ### ENDIF IVK ###
NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genDbMetaDdl_2( _
  ddlType As DdlTypeId _
)
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMeta, processingStepMeta2, ddlType, , , , phaseDbSupport)
    
  On Error GoTo ErrorExit

  ' ####################################################################################################################
  ' #    Function to get a list of subclasses' ClassIds for a given ClassId
  ' ####################################################################################################################
  
  Dim qualFuncNameGetSubClassIds As String
  qualFuncNameGetSubClassIds = genQualFuncName(g_sectionIndexDbMeta, udfnGetSubClassIds, ddlType, , , , , , True)
  
  printSectionHeader "Function to get a list of subclasses' ClassIds for a given ClassId", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); qualFuncNameGetSubClassIds
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "classId_in", g_dbtEntityId, False, "CLASSID"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS"
  Print #fileNo, addTab(1); "VARCHAR(1000)"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "BEGIN ATOMIC"
  genProcSectionHeader fileNo, "declare variables", , True
  Print #fileNo, addTab(1); "DECLARE v_subClassIds VARCHAR(1024);"
  
  Print #fileNo,
  Print #fileNo, addTab(1); "SET v_subClassIds = classId_in;"
  Print #fileNo,
  Print #fileNo, addTab(1); "FOR eLoop AS"
  Print #fileNo, addTab(2); "WITH V_Node (entitySection, entityName, entityType, entityid, rootId, depth) AS"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_anAcmEntitySection; ","
  Print #fileNo, addTab(3); g_anAcmEntityName; ","
  Print #fileNo, addTab(3); g_anAcmEntityType; ","
  Print #fileNo, addTab(3); g_anAcmEntityId; ","
  Print #fileNo, addTab(3); g_anAcmEntityId; ","
  Print #fileNo, addTab(3); "0"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " R"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "R."; g_anAcmEntityId; " = classId_in"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
  
  Print #fileNo, addTab(2); "UNION ALL"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "C."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityName; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityType; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityId; ","
  Print #fileNo, addTab(3); "P.rootId,"
  Print #fileNo, addTab(3); "P.depth + 1"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " C,"
  Print #fileNo, addTab(3); "V_Node P"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntitySection; " = P.entitySection"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntityName; " = P.entityName"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntityType; " = P.entityType"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "P.depth < 100"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "entityId AS c_entityId"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); "V_Node"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "entityId <> v_subClassIds"
  Print #fileNo, addTab(2); "ORDER BY entityId ASC"
  Print #fileNo, addTab(2); "FETCH FIRST 166 ROWS ONLY"
  Print #fileNo, addTab(1); "DO"
  Print #fileNo, addTab(2); "SET v_subClassIds = v_subClassIds || ',' || c_entityId;"
  Print #fileNo, addTab(1); "END FOR;"
  Print #fileNo, addTab(1); "RETURN v_subClassIds;"
  Print #fileNo, addTab(0); "END"

  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    Function to get a list of subclasses' ClassIds for a given ClassId
  ' ####################################################################################################################
  
  printSectionHeader "Function returning set of subclass-Ids for a list of given ClassIds", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE FUNCTION"
  Print #fileNo, addTab(1); g_qualFuncNameGetSubClassIdsByList
  
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "", "classIdList_in", "CHAR(200)", False, "','-separated list of CLASSIDs"
  Print #fileNo, addTab(0); ")"

  Print #fileNo, addTab(0); "RETURNS TABLE"
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "classId    "; g_dbtEntityId; ","
  Print #fileNo, addTab(2); "isAbstract "; g_dbtBoolean
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "DETERMINISTIC"
  Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
  Print #fileNo, addTab(0); "READS SQL DATA"
  Print #fileNo, addTab(0); "BEGIN ATOMIC"
  
  Print #fileNo, addTab(1); "RETURN"
  Print #fileNo, addTab(2); "WITH"
  Print #fileNo, addTab(3); "NODE"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "entitySection,"
  Print #fileNo, addTab(3); "entityName,"
  Print #fileNo, addTab(3); "entityType,"
  Print #fileNo, addTab(3); "entityId,"
  Print #fileNo, addTab(3); "rootId,"
  Print #fileNo, addTab(3); "depth,"
  Print #fileNo, addTab(3); "isAbstract"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "AS"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "E."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(4); "E."; g_anAcmEntityName; ","
  Print #fileNo, addTab(4); "E."; g_anAcmEntityType; ","
  Print #fileNo, addTab(4); "E."; g_anAcmEntityId; ","
  Print #fileNo, addTab(4); "E."; g_anAcmEntityId; ","
  Print #fileNo, addTab(4); "0,"
  Print #fileNo, addTab(4); "E."; g_anAcmIsAbstract
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " E"
  Print #fileNo, addTab(2); "INNER JOIN"
  Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(classIdList_in, CAST(',' AS CHAR(1))) ) AS X"
  Print #fileNo, addTab(2); "ON"
  Print #fileNo, addTab(3); "E."; g_anAcmEntityId; " = X.elem"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "E."; g_anAcmEntityType; "='"; gc_acmEntityTypeKeyClass; "'"
  Print #fileNo, addTab(2); "UNION ALL"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "C."; g_anAcmEntitySection; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityName; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityType; ","
  Print #fileNo, addTab(3); "C."; g_anAcmEntityId; ","
  Print #fileNo, addTab(3); "P.rootId,"
  Print #fileNo, addTab(3); "P.depth + 1,"
  Print #fileNo, addTab(3); "C."; g_anAcmIsAbstract
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " C,"
  Print #fileNo, addTab(3); "NODE P"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntitySection; " = P.entitySection"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntityName; " = P.entityName"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "C."; g_anAcmSupEntityType; " = P.entityType"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "P.depth < 100"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "SELECT DISTINCT"
  Print #fileNo, addTab(3); "entityId,"
  Print #fileNo, addTab(3); "isAbstract"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); "NODE"
  Print #fileNo, addTab(1); ";"
  Print #fileNo, addTab(0); "END"

  Print #fileNo, addTab(0); gc_sqlCmdDelim
' ### IF IVK ###

  ' ####################################################################################################################
  ' #    View representing a pseudo Enum for entities with flag EntityFilterEnum Criteria, including list of subclass ids
  ' ####################################################################################################################
  
  Dim qualViewName As String
  Dim qualViewNameLdm As String
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnEntityFilterEnum, vsnEntityFilterEnum, ddlType)
      
  printSectionHeader "representing a pseudo Enum for entities with flag EntityFilterEnum Criteria, including list of subclass ids", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "ID,"
  Print #fileNo, addTab(1); "SUBCLASSIDS,"
  Print #fileNo, addTab(1); g_anVersionId
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY E."; g_anAcmEntityId; "),"
  Print #fileNo, addTab(2); qualFuncNameGetSubClassIds; "(E."; g_anAcmEntityId; "),"
  Print #fileNo, addTab(2); "1"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " E"
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); g_anAcmEntityFilterEnumCriteria; " = 1"
  Print #fileNo, addTab(0); ")"
    
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnEntityFilterEnum, vsnEntityFilterEnum, edtLdm)
  genAliasDdl g_sectionIndexDbMeta, vnEntityFilterEnum, _
                    True, True, True, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                    "View representing a pseudo Enum for entities with flag EntityFilterEnum Criteria", , , True
  
  ' ####################################################################################################################
  ' #    View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria
  ' ####################################################################################################################
  
  qualViewName = genQualViewName(g_sectionIndexDbMeta, vnEntityFilterNlTextEnum, vsnEntityFilterNlTextEnum, ddlType)
      
  printSectionHeader "View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria", fileNo
  Print #fileNo,
  
  Print #fileNo, addTab(0); "CREATE VIEW"
  Print #fileNo, addTab(1); qualViewName
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); g_anOid; ","
  Print #fileNo, addTab(1); g_anEnumRefId; ","
  Print #fileNo, addTab(1); g_anLanguageId; ","
  Print #fileNo, addTab(1); "TEXT,"
  Print #fileNo, addTab(1); g_anVersionId
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "AS"
  Print #fileNo, addTab(0); "("
  Print #fileNo, addTab(1); "SELECT"
  Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY E."; g_anAcmEntityId; ", ET."; g_anLanguageId; "),"
  Print #fileNo, addTab(2); "ROWNUMBER() OVER (PARTITION BY ET."; g_anLanguageId; " ORDER BY E."; g_anAcmEntityId; "),"
  Print #fileNo, addTab(2); "ET."; g_anLanguageId; ","
  Print #fileNo, addTab(2); "ET."; g_anAcmEntityLabel; ","
  Print #fileNo, addTab(2); "1"
  Print #fileNo, addTab(1); "FROM"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " E"
  Print #fileNo, addTab(1); "JOIN"
  Print #fileNo, addTab(2); g_qualTabNameAcmEntityNl; " ET"
  Print #fileNo, addTab(1); "ON"
  Print #fileNo, addTab(2); "E."; g_anAcmEntityName; " = ET."; g_anAcmEntityName
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "E."; g_anAcmEntitySection; " = ET."; g_anAcmEntitySection
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); "E."; g_anAcmEntityType; " = ET."; g_anAcmEntityType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); g_anAcmEntityFilterEnumCriteria; " = 1"
  Print #fileNo, addTab(0); ")"
    
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  qualViewNameLdm = genQualViewName(g_sectionIndexDbMeta, vnEntityFilterEnum, vsnEntityFilterEnum, edtLdm)
  genAliasDdl g_sectionIndexCommon, vnEntityFilterNlTextEnum, _
                    True, True, False, qualViewNameLdm, qualViewName, False, ddlType, , , edatView, False, False, False, False, False, _
                    "View representing NlText for pseudo Enum for entities with flag EntityFilterEnum Criteria", , , True
' ### ENDIF IVK ###

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


