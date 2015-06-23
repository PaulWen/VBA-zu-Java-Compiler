 Attribute VB_Name = "M16_Archive"
 ' ### IF IVK ###
 Option Explicit
 
 Global Const tempArchiveTabStatsTabName = "SESSION.ArchiveTabStats"
 Global Const tempArchiveIndStatsTabName = "SESSION.ArchiveIndStats"
 Global Const tempPsDates = "SESSION.PsDates"
 Global Const tempToBeArchived = "SESSION.ToBeArchived"
 
 Private Const processingStep = 2
 
 Private Const usePsDpMappingForArchiveViews = False
 
 Sub genDdlForCalculationRunCheckTypeSpec( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional typeSpec As String, _
   Optional typeSpecWork As String, _
   Optional alias As String _
 )
   Print #fileNo, addTab(indent + 0); "NOT EXISTS("
   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "1"
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); typeSpec; " TYPSPROD"
   Print #fileNo, addTab(indent + 1); "WHERE"
   Print #fileNo, addTab(indent + 2); "TYPSPROD.CRTCAR_OID = "; alias; "."; g_anOid
   Print #fileNo, addTab(indent + 3); "AND"
   Print #fileNo, addTab(indent + 2); "TYPSPROD."; g_anPsOid; " = "; alias; "."; g_anPsOid
   Print #fileNo, addTab(indent + 0); ")"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NOT EXISTS ("
   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "1"
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); typeSpecWork; " TYPSWORK"
   Print #fileNo, addTab(indent + 1); "WHERE"
   Print #fileNo, addTab(indent + 2); "TYPSWORK.CRTCAR_OID = "; alias; "."; g_anOid
   Print #fileNo, addTab(indent + 3); "AND"
   Print #fileNo, addTab(indent + 2); "TYPSWORK."; g_anPsOid; " = "; alias; "."; g_anPsOid
   Print #fileNo, addTab(indent + 0); ")"
 End Sub
 
 Sub genDdlForWorkProdJoinWithPs( _
   fileNo As Integer, _
   Optional indent As Integer = 1 _
 )
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "PROD."; g_anOid; " = WORK."; g_anOid
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "PROD."; g_anPsOid; " = WORK."; g_anPsOid
 End Sub
 
 Sub genDdlForTypeSpecCheckNsr( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional tsAlias As String, _
   Optional refTsColumnName As String _
 )
   Print #fileNo, addTab(indent + 0); "NSR.CLASSID = '09005'"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "DATE(NSR."; refTsColumnName; ") < refDate_in"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anOid; " = "; tsAlias; ".TSNN1V_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anPsOid; " = "; tsAlias; "."; g_anPsOid
 End Sub
 Sub genDdlForTypeStandardEquipmentCheckTypeSpecNsr( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional tseAlias As String, _
   Optional refTsColumnName As String _
 )
   Print #fileNo, addTab(indent + 0); "NSR.CLASSID = '09005'"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "DATE(NSR."; refTsColumnName; ") < refDate_in"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anOid; " = TYPS.TSNN1V_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anPsOid; " = TYPS."; g_anPsOid
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anOid; " = "; tseAlias; ".TSETYS_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anPsOid; " = "; tseAlias; "."; g_anPsOid
 End Sub
 Sub genDdlForProtocolLineEntryCheckTypeSpecNsr( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional refTsColumnName As String _
 )
   Print #fileNo, addTab(indent + 0); "NSR.CLASSID = '09005'"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "DATE(NSR."; refTsColumnName; ") < refDate_in"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anOid; " = TYPS.TSNN1V_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anPsOid; " = TYPS."; g_anPsOid
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anOid; " = WORK.TSPTYS_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anPsOid; " = WORK."; g_anPsOid
 End Sub
 Sub genDdlForProtocolParameterCheckPleTypeSpecNsr( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional refTsColumnName As String _
 )
   Print #fileNo, addTab(indent + 0); "NSR.CLASSID = '09005'"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "DATE(NSR."; refTsColumnName; ") < refDate_in"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anOid; " = TYPS.TSNN1V_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "NSR."; g_anPsOid; " = TYPS."; g_anPsOid
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anOid; " = PLE.TSPTYS_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "TYPS."; g_anPsOid; " = PLE."; g_anPsOid
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "PLE."; g_anOid; " = WORK.PLPLEN_OID"
   Print #fileNo, addTab(indent + 1); "AND"
   Print #fileNo, addTab(indent + 0); "PLE."; g_anPsOid; " = WORK."; g_anPsOid
 End Sub
 
 Sub genDdlForTempArchiveStats( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for (Table-) Statistics / Estimates on Archive Data"

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempArchiveTabStatsTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "orgId           "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "poolId          "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "tabSchema       "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(indent + 1); "tabName         VARCHAR(50),"
   Print #fileNo, addTab(indent + 1); "card            BIGINT,"
   Print #fileNo, addTab(indent + 1); "avgRowLen       INTEGER,"
   Print #fileNo, addTab(indent + 1); "size            BIGINT,"
   Print #fileNo, addTab(indent + 1); "cardArch        BIGINT,"
   Print #fileNo, addTab(indent + 1); "sizeArch        BIGINT"
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 
   genProcSectionHeader fileNo, "temporary table for (Index-) Statistics / Estimates on Archive Data"

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempArchiveIndStatsTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "orgId           "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "poolId          "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "tabSchema       "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(indent + 1); "tabName         VARCHAR(50),"
   Print #fileNo, addTab(indent + 1); "indName         VARCHAR(20),"
   Print #fileNo, addTab(indent + 1); "card            BIGINT,"
   Print #fileNo, addTab(indent + 1); "avgKeyLen       INTEGER,"
   Print #fileNo, addTab(indent + 1); "size            BIGINT,"
   Print #fileNo, addTab(indent + 1); "cardArch        BIGINT,"
   Print #fileNo, addTab(indent + 1); "sizeArch        BIGINT"
   Print #fileNo, addTab(indent + 0); ")"
 
   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 Sub genDdlForTempPsDates( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False, _
   Optional inclGenWsProdTs As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for min FTO-Date"

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempPsDates
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "psOid           BIGINT,"
   If inclGenWsProdTs Then
     Print #fileNo, addTab(indent + 1); "genWsProd       TIMESTAMP,"
     Print #fileNo, addTab(indent + 1); "fto             TIMESTAMP"
   Else
     Print #fileNo, addTab(indent + 1); "ftoCommit       TIMESTAMP"
   End If
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 
 End Sub
 Sub genDdlForTempToBeArchived( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for changelog recordes to be archived"

   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempToBeArchived
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid           BIGINT"
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 
 End Sub
 
 
 Sub genArchiveSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer

   If Not supportArchivePool Then
     Exit Sub
   End If

   If ddlType = edtPdm Then

     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       If poolIsValidForOrg(g_productiveDataPoolIndex, thisOrgIndex) Then
         genArchiveSupportDdlByPool thisOrgIndex, edtPdm
         genArchiveOrgPurgeDdlByPool thisOrgIndex, edtPdm
       End If
     Next thisOrgIndex
   End If
 End Sub
 
 Private Sub genArchiveOrgPurgeDdlByPool( _
   ByVal thisOrgIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportArchivePool Or ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStep, ddlType, thisOrgIndex, g_archiveDataPoolId, , phaseArchive)

   Dim qualProcNameArchiveOrgPurge As String
   qualProcNameArchiveOrgPurge = genQualProcName(g_sectionIndexAliasLrt, spnArchiveOrgPurge, ddlType, thisOrgIndex, g_productiveDataPoolId)
 
   Dim qualProcNameArchiveOrgPurgeChg As String
   qualProcNameArchiveOrgPurgeChg = genQualProcName(g_sectionIndexChangeLog, spnArchiveOrgPurge, ddlType, thisOrgIndex, g_archiveDataPoolId)
 
   Dim thisMetSchema As String
   thisMetSchema = genSchemaName(snMeta, ssnMeta, ddlType, thisOrgIndex)
   'schemaNameDataFix = genSchemaName(snDataFix, ssnDataFix, ddlType)
 
   Dim qualProcedureNameReorg As String
   qualProcedureNameReorg = genQualProcName(g_sectionIndexDbAdmin, spnReorg, ddlType)
 
   ' ####################################################################################################################
   ' #    SP for Purging Data per Organization (2 parameters)
   ' ####################################################################################################################
 
   printSectionHeader "SP for Purging Archive Data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrgPurge
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "purgeUserId_in", g_dbtUserId, True, "user for ArchiveHistory entry"
   genProcParm fileNo, "IN", "clOnly_in", "INTEGER", True, "purge only ChangeLog-records if this parameter is '1'"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_useCase", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genVarDecl fileNo, "v_failCount", "INTEGER", "0"
   genVarDecl fileNo, "v_purgeTimeStamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrgPurge, ddlType, , "#refDate_in", "'purgeUserId_in", "clOnly_in"

   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET v_purgeTimeStamp = CURRENT TIMESTAMP;"

   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET v_orgOid = (SELECT ORGOID FROM "; g_qualTabNamePdmOrganization; " WHERE SEQUENCESCHEMANAME = '"; thisMetSchema; "');"

   genProcSectionHeader fileNo, "process each table separately which is 'subject to archiving'"
   Print #fileNo, addTab(1); "IF clOnly_in <> 1 THEN"
   Print #fileNo,
   Print #fileNo,
   Print #fileNo, addTab(2); "FOR tabLoop AS"
 
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(3); "FROM"
 
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmIsArch; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " = "; genPoolId(g_archiveDataPoolId, ddlType)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.ENTITYID <> '23001'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "L."; g_anLdmFkSequenceNo; " DESC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
 
   Print #fileNo, addTab(2); "DO"
   genProcSectionHeader fileNo, "process each table individually", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnArchiveOrgPurge); "_' || c_tableName || '(?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "refDate_in"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 3
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(2); "END FOR;"
   'Print #fileNo, addTab(2); "COMMIT;"

   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgPurgeChg; "_CHANGELOG_NL_TEXT(?,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_rowCount"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "refDate_in"
   Print #fileNo, addTab(1); ";"
   genProcSectionHeader fileNo, "add to number of affected rows", 1
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
   'Print #fileNo, addTab(1); "COMMIT;"
   'reorg!
   Print #fileNo,
   'Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgPurgeChg); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );"
   'Print #fileNo, addTab(1); "COMMIT;"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgPurgeChg; "_CHANGELOG(?,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_rowCount"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "refDate_in"
   Print #fileNo, addTab(1); ";"
   genProcSectionHeader fileNo, "add to number of affected rows", 1
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
   'Print #fileNo, addTab(1); "COMMIT;"
   'reorg!
   Print #fileNo,
   'Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgPurgeChg); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a reorg
   'Print #fileNo, addTab(1); "COMMIT;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF clOnly_in = 1 THEN"
   Print #fileNo, addTab(2); "SET v_useCase = 'UC1368CL'; "
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_useCase = 'UC1368';"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "VL6CMET.ARCHIVEHISTORY"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "OID,"
   Print #fileNo, addTab(2); "ARCHIVEDATE,"
   Print #fileNo, addTab(2); "USECASE,"
   Print #fileNo, addTab(2); "CDUSERID,"
   Print #fileNo, addTab(2); "STARTTIME,"
   Print #fileNo, addTab(2); "ENDTIME,"
   Print #fileNo, addTab(2); "OBJECTCOUNT,"
   Print #fileNo, addTab(2); "AHOORG_OID"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "VALUES"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NEXT VALUE FOR VL6CMET.OIDSEQUENCE,"
   Print #fileNo, addTab(2); "refDate_in,"
   Print #fileNo, addTab(2); "v_useCase,"
   Print #fileNo, addTab(2); "purgeUserId_in,"
   Print #fileNo, addTab(2); "v_purgeTimeStamp,"
   Print #fileNo, addTab(2); "current timestamp,"
   Print #fileNo, addTab(2); "rowCount_out,"
   Print #fileNo, addTab(2); "v_orgOid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); ""

   genSpLogProcExit fileNo, qualProcNameArchiveOrgPurge, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"
     Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for Purging Data per Organization (2 parameters)
   ' ####################################################################################################################

   printSectionHeader "SP for Purging Archive Data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrgPurge
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "purgeUserId_in", g_dbtUserId, True, "user for ArchiveHistory entry"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrgPurge, ddlType, , "#refDate_in", "'purgeUserId_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   Print #fileNo,
 
   Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgPurge; "(?,?,0,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "rowCount_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "refDate_in,"
   Print #fileNo, addTab(2); "purgeUserId_in"
   Print #fileNo, addTab(1); ";"
 
   genSpLogProcExit fileNo, qualProcNameArchiveOrgPurge, ddlType, , "#refDate_in", "'purgeUserId_in", "rowCount_out"
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
 
 
 Private Sub genArchiveSupportDdlByPool( _
   ByVal thisOrgIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not supportArchivePool Or ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, g_productiveDataPoolIndex, , phaseArchive)

   ' ####################################################################################################################
   ' #    SP for Archiving Data per Organization
   ' ####################################################################################################################

   Dim qualProcNameArchiveOrg As String
   qualProcNameArchiveOrg = genQualProcName(g_sectionIndexAliasLrt, spnArchiveOrg, ddlType, thisOrgIndex, g_productiveDataPoolIndex)
 
   Dim qualProcNameArchiveOrgChg As String
   qualProcNameArchiveOrgChg = genQualProcName(g_sectionIndexChangeLog, spnArchiveOrg, ddlType, thisOrgIndex, g_productiveDataPoolIndex)
 
   Dim qualProcNameArchiveOrgChgWork As String
   qualProcNameArchiveOrgChgWork = genQualProcName(g_sectionIndexChangeLog, spnArchiveOrg, ddlType, thisOrgIndex, g_workDataPoolIndex)

   Dim thisMetSchema As String
   thisMetSchema = genSchemaName(snMeta, ssnMeta, ddlType, thisOrgIndex)
 
   Dim qualProcedureNameReorg As String
   qualProcedureNameReorg = genQualProcName(g_sectionIndexDbAdmin, spnReorg, ddlType)
 
   printSectionHeader "SP for Archiving data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "archUserId_in", g_dbtUserId, True, "archived records are tagged with this user as '" & conUpdateUser & "'"
   genProcParm fileNo, "IN", "clOnly_in", "INTEGER", True, "archive only ChangeLog-records if and only if this parameter is '1'"
   genProcParm fileNo, "IN", "onDataPoolOnly_in", "INTEGER", True, "archive only ChangeLog-records in one data pool if and only if this parameter is '1' for working or '3' for productiv"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_useCase", "VARCHAR(10)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_tabCount", "INTEGER", "0"
   genVarDecl fileNo, "v_failCount", "INTEGER", "0"
   genVarDecl fileNo, "v_archTimeStamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_objCount", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"

   If thisOrgIndex = g_primaryOrgIndex Then
     genDdlForTempPsDates fileNo, 1, True
   End If
 
   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET v_archTimeStamp = CURRENT TIMESTAMP;"

   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET v_orgOid = (SELECT ORGOID FROM "; g_qualTabNamePdmOrganization; " WHERE SEQUENCESCHEMANAME = '"; thisMetSchema; "');"

   genProcSectionHeader fileNo, "runstats / rebind"
   If thisOrgIndex = g_primaryOrgIndex Then
    ' Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CASP"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', 'VL6CASP010', NULL, v_objCount, v_failCount );"
   Else
    ' Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CASP"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', NULL, NULL, v_objCount, v_failCount );"
   End If
   'Print #fileNo, addTab(1); "CALL VL6CDBA.RUNSTATS( 2, 'VL6CDEC"; genOrgId(thisOrgIndex, ddlType, False); "%', '%', NULL, NULL, v_objCount, v_failCount );"
   'Print #fileNo, addTab(1); "CALL VL6CDBA.REBIND(2, 'VL6%"; genOrgId(thisOrgIndex, ddlType, False); "3', 'ARCHIVEORG_%', 1, v_objCount);"

   genProcSectionHeader fileNo, "process each table separately which is 'subject to archiving'"
   Print #fileNo, addTab(1); "IF clOnly_in <> 1 THEN"
   Print #fileNo, addTab(2); "SET onDataPoolOnly_in = 0;"
 
   If thisOrgIndex = g_primaryOrgIndex Then
 
     genProcSectionHeader fileNo, "get last FTO creation date", 2

     Print #fileNo, addTab(2); "FOR orgLoop AS"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "SCHEMANAME AS c_schemaName"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); g_qualTabNamePdmPrimarySchema
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "POOLTYPE_ID = 3"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "ORGANIZATION_ID > 1"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "ORGANIZATION_ID ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"
     Print #fileNo, addTab(2); "DO"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'MERGE INTO ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '"; tempPsDates; " T ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'USING (SELECT ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || '"; g_anPsOid; ", ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'LASTCENTRALDATATRANSFERCOMMIT AS FTO ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'FROM ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || c_schemaName || '.GENERALSETTINGS ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ') S ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'ON ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'T.psOid = S."; g_anPsOid; " ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'WHEN MATCHED AND COALESCE(T.ftoCommit, '"; gc_valDateInfinite; "') > COALESCE(S.FTO, '"; gc_valDateInfinite; "') THEN ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'UPDATE SET T.ftoCommit = S.FTO ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'INSERT (psOid, ftoCommit) VALUES (S."; g_anPsOid; ", S.FTO) ';"
     Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'ELSE IGNORE';"
     Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(2); "END FOR;"
   End If

   Print #fileNo,
   Print #fileNo, addTab(2); "FOR tabLoop AS"
 
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(3); "FROM"
 
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmIsArch; " = "; gc_dbTrue
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.ENTITYID <> '23001'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "L."; g_anLdmFkSequenceNo; " DESC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
 
   Print #fileNo, addTab(2); "DO"
   genProcSectionHeader fileNo, "process each table individually", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnArchiveOrg); "_' || c_tableName || '(?,?,?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "refDate_in,"
   Print #fileNo, addTab(4); "archUserId_in,"
   Print #fileNo, addTab(4); "v_archTimeStamp"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 3
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo,
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); g_qualTabNameOrganization
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "(LASTARCHIVEDATE, UPDATEUSER, LASTUPDATETIMESTAMP) "
   Print #fileNo, addTab(4); "= "
   Print #fileNo, addTab(3); "(DATE(refDate_in), archUserId_in, current timestamp)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "OID = v_orgOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(LASTARCHIVEDATE, "; gc_valDateEarliest; ") < DATE(refDate_in)"
   Print #fileNo, addTab(2); ";"
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF onDataPoolOnly_in <> 3 THEN"
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgChg; "_CHANGELOG_NL_TEXT_WP(?,?,?,?,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "refDate_in,"
   Print #fileNo, addTab(3); "archUserId_in,"
   Print #fileNo, addTab(3); "v_archTimeStamp,"
   Print #fileNo, addTab(3); "clOnly_in"
   Print #fileNo, addTab(2); ";"
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo,
   'Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChgWork); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );"
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgChg; "_CHANGELOG_WP(?,?,?,?,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "refDate_in,"
   Print #fileNo, addTab(3); "archUserId_in,"
   Print #fileNo, addTab(3); "v_archTimeStamp,"
   Print #fileNo, addTab(3); "clOnly_in"
   Print #fileNo, addTab(2); ";"
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo,
   'Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChgWork); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a reorg
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF onDataPoolOnly_in <> 1 THEN"
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgChg; "_CHANGELOG_NL_TEXT_PP(?,?,?,?,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "refDate_in,"
   Print #fileNo, addTab(3); "archUserId_in,"
   Print #fileNo, addTab(3); "v_archTimeStamp,"
   Print #fileNo, addTab(3); "clOnly_in"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "add to number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   'Print #fileNo, addTab(2); "COMMIT;"
   'Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChg); "', 'CHANGELOG_NL_TEXT', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrgChg; "_CHANGELOG_PP(?,?,?,?,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "refDate_in,"
   Print #fileNo, addTab(3); "archUserId_in,"
   Print #fileNo, addTab(3); "v_archTimeStamp,"
   Print #fileNo, addTab(3); "clOnly_in"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   'Print #fileNo, addTab(2); "COMMIT;"
   'Print #fileNo, addTab(2); "CALL "; qualProcedureNameReorg; "( 1, 'T', '"; getSchemaName(qualProcNameArchiveOrgChg); "', 'CHANGELOG', NULL, NULL, 0, 1, 0, v_tabCount, v_failCount );" 'Refs_rs1a
   'Print #fileNo, addTab(2); "COMMIT;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF clOnly_in = 1 THEN"
   Print #fileNo, addTab(2); "SET v_useCase = 'UC841CL'; "
   Print #fileNo, addTab(2); "IF onDataPoolOnly_in = 1 THEN"
   Print #fileNo, addTab(3); "SET v_useCase = v_useCase || '_WP';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF onDataPoolOnly_in = 3 THEN"
   Print #fileNo, addTab(3); "SET v_useCase = v_useCase || '_PP';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_useCase = 'UC841';"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "VL6CMET.ARCHIVEHISTORY"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "OID,"
   Print #fileNo, addTab(2); "ARCHIVEDATE,"
   Print #fileNo, addTab(2); "USECASE,"
   Print #fileNo, addTab(2); "CDUSERID,"
   Print #fileNo, addTab(2); "STARTTIME,"
   Print #fileNo, addTab(2); "ENDTIME,"
   Print #fileNo, addTab(2); "OBJECTCOUNT,"
   Print #fileNo, addTab(2); "AHOORG_OID"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "VALUES"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NEXT VALUE FOR VL6CMET.OIDSEQUENCE,"
   Print #fileNo, addTab(2); "refDate_in,"
   Print #fileNo, addTab(2); "v_useCase,"
   Print #fileNo, addTab(2); "archUserId_in,"
   Print #fileNo, addTab(2); "v_archTimeStamp,"
   Print #fileNo, addTab(2); "current timestamp,"
   Print #fileNo, addTab(2); "rowCount_out,"
   Print #fileNo, addTab(2); "v_orgOid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); ""

   genSpLogProcExit fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   ' #    SP for Archiving Data per Organization - Wrapper (2 input parms)
   ' ####################################################################################################################

   printSectionHeader "SP for Archiving data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "archUserId_in", g_dbtUserId, True, "archived records are tagged with this user as '" & conUpdateUser & "'"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   Print #fileNo,
 
   Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrg; "(?,?,0,0,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "rowCount_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "refDate_in,"
   Print #fileNo, addTab(2); "archUserId_in"
   Print #fileNo, addTab(1); ";"
 
   genSpLogProcExit fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for Archiving Data per Organization - Wrapper (3 input parms)
   ' ####################################################################################################################

   printSectionHeader "SP for Archiving data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "archUserId_in", g_dbtUserId, True, "archived records are tagged with this user as '" & conUpdateUser & "'"
   genProcParm fileNo, "IN", "clOnly_in", "INTEGER", True, "archive only ChangeLog-records if and only if this parameter is '1'"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   Print #fileNo,
 
   Print #fileNo, addTab(1); "SET v_stmntTxt  = 'CALL "; qualProcNameArchiveOrg; "(?,?,?,0,?)';"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "rowCount_out"
   Print #fileNo, addTab(1); "USING"
   Print #fileNo, addTab(2); "refDate_in,"
   Print #fileNo, addTab(2); "archUserId_in,"
   Print #fileNo, addTab(2); "clOnly_in"
   Print #fileNo, addTab(1); ";"
 
   genSpLogProcExit fileNo, qualProcNameArchiveOrg, ddlType, , "#refDate_in", "'archUserId_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for Estimating Volume of Archive Data per Organization
   ' ####################################################################################################################

   Dim qualProcNameArchiveOrgEstimate As String
   qualProcNameArchiveOrgEstimate = genQualProcName(g_sectionIndexDbAdmin, spnArchiveOrgEstimate, ddlType, thisOrgIndex)
 
   printSectionHeader "SP for Estimating Volume of Archive Data (per Organization)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrgEstimate
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "count only data with validity ending before this date"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows to be archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genSpLogProcEnter fileNo, qualProcNameArchiveOrgEstimate, ddlType, , "#refDate_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "process each table separately which is 'subject to archiving'"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
 
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(2); "FROM"
 
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsArch; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " DESC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "process each table individually", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnArchiveOrgEstimate); "_' || c_tableName || '(?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "refDate_in"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcNameArchiveOrgEstimate, ddlType, , "#refDate_in", "rowCount_out"

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
 
 
 Sub genArchiveOrgPurgeDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   archPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional isPurelyPrivate As Boolean = False _
 )
   If Not supportArchivePool Or ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim dbObjName As String
   Dim dbObjShortName As String
   Dim entityTypeDescr As String
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim isSubjectToArchiving As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isCtoAliasCreated As Boolean
   Dim notAcmRelated As Boolean
   Dim ahClassIndex As Integer
   Dim isAggHead As Boolean
   Dim refTsColumnName As String
   Dim hasOwnTable As Boolean
   Dim isUserTransactional As Boolean
   Dim useMqtToImplementLrt As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim condenseData As Boolean
   Dim expandExpressionsInFtoView As Boolean

   On Error GoTo ErrorExit

   isAggHead = False
   If acmEntityType = eactClass Then
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName

       If forNl Then
         dbObjName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         dbObjShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged And Not g_classes.descriptors(acmEntityIndex).noRangePartitioning
       Else
         dbObjName = g_classes.descriptors(acmEntityIndex).className
         dbObjShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
       End If
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isSubjectToArchiving = g_classes.descriptors(acmEntityIndex).isSubjectToArchiving
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
       notAcmRelated = g_classes.descriptors(acmEntityIndex).notAcmRelated
       ahClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       refTsColumnName = genAttrName(IIf(g_classes.descriptors(acmEntityIndex).nonStandardRefTimeStampForArchiving <> "", g_classes.descriptors(acmEntityIndex).nonStandardRefTimeStampForArchiving, conValidTo), ddlType)
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       expandExpressionsInFtoView = g_classes.descriptors(acmEntityIndex).expandExpressionsInFtoView
   ElseIf acmEntityType = eactRelationship Then
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName

       If forNl Then
         dbObjName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         dbObjShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged And Not g_relationships.descriptors(acmEntityIndex).noRangePartitioning
       Else
         dbObjName = g_relationships.descriptors(acmEntityIndex).relName
         dbObjShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       End If

       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isSubjectToArchiving = g_relationships.descriptors(acmEntityIndex).isSubjectToArchiving
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
       notAcmRelated = g_relationships.descriptors(acmEntityIndex).notAcmRelated
       ahClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       refTsColumnName = g_anValidTo
       hasOwnTable = True
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = False
       expandExpressionsInFtoView = False
   Else
     Exit Sub
   End If

   Dim qualAggHeadTabNameArch As String
   If ahClassIndex > 0 Then
     qualAggHeadTabNameArch = genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, g_archiveDataPoolId)
       refTsColumnName = genAttrName(IIf(g_classes.descriptors(ahClassIndex).nonStandardRefTimeStampForArchiving <> "", g_classes.descriptors(ahClassIndex).nonStandardRefTimeStampForArchiving, conValidTo), ddlType)
   End If

   If Not isSubjectToArchiving Then
     Exit Sub
   End If

   Const ctoOrgId = 1
   Dim qualTabNameArch As String
   qualTabNameArch = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabParentNameArch As String
   qualTabParentNameArch = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , False)

   Dim qualTabNameProd As String
   qualTabNameProd = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameWork As String
   qualTabNameWork = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameProdPar As String
   If forNl Then
     qualTabNameProdPar = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen)
   End If

   Dim qualTabNameArchiveLog As String
   qualTabNameArchiveLog = genQualTabNameByClassIndex(g_classIndexArchLog, ddlType, thisOrgIndex, g_archiveDataPoolIndex)

   Dim qualTabNameTypeSpecNameArch As String
   qualTabNameTypeSpecNameArch = genQualTabNameByEntityIndex(g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameAspectArch As String
   qualTabNameAspectArch = genQualTabNameByEntityIndex(g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameProtocolLineEntryArch As String
   qualTabNameProtocolLineEntryArch = genQualTabNameByEntityIndex(g_classIndexProtocolLineEntry, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , forNl)

   Dim baseArchTabName As String
   baseArchTabName = baseName(qualTabNameArch, , ".")

   ' ####################################################################################################################
   ' #    SP for Purging Arche Data for individual Entity
   ' ####################################################################################################################

   Dim qualProcNameArchiveOrPugeEntity As String
   qualProcNameArchiveOrPugeEntity = _
     genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolId, forGen, , , forNl, spnArchiveOrgPurge)
 
   printSectionHeader "SP for Purging Arche Data for " & entityTypeDescr & " '" & sectionName & "." & dbObjName & "'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveOrPugeEntity
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcNameArchiveOrPugeEntity, ddlType, , "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "purge archive log records"
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualTabNameArchiveLog; " ALOG"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "DBTABLENAME = '"; baseArchTabName; "'"
   Select Case acmEntityIndex
   Case g_classIndexCalculationRun
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "NOT EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameArch; " CRUN"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "TYPS.CRTCAR_OID = CRUN."; g_anOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = CRUN."; g_anPsOid; ""
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "ALOG.OBJECTID = CRUN."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case g_classIndexTypeSpec
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "ALOG.OBJECTID = TYPS."; g_anOid
     Print #fileNo, addTab(3); ")"
   Case g_classIndexTypeStandardEquipment
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameArch; " TYSE"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "TYPS."; g_anOid; " = TYSE.TSETYS_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = TYSE."; g_anPsOid
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "ALOG.OBJECTID = TYSE."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case g_classIndexProtocolLineEntry
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameArch; " PLE"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "TYPS."; g_anOid; " = PLE.TSPTYS_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = PLE."; g_anPsOid
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "ALOG.OBJECTID = PLE."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case g_classIndexProtocolParameter
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameArch; " PROP"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameProtocolLineEntryArch; " PLE"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "PLE."; g_anOid; " = PROP.PLPLEN_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "PLE."; g_anPsOid; " = PROP."; g_anPsOid
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "TYPS."; g_anOid; " = PLE.TSPTYS_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = PLE."; g_anPsOid
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "ALOG.OBJECTID = PROP."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case Else
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     If ahClassIndex > 0 And Not isAggHead Then
       Print #fileNo, addTab(4); qualAggHeadTabNameArch; " AH"
       Print #fileNo, addTab(3); "JOIN"
       Print #fileNo, addTab(4); qualTabNameArch; " ARCH"
       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "ARCH."; g_anPsOid; " = AH."; g_anPsOid
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "ARCH."; g_anAhOid; " = AH."; g_anOid
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "AH."; refTsColumnName; " < refDate_in"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "ALOG.OBJECTID = ARCH."; g_anOid
     Else
       If Not (forNl) Then
         Print #fileNo, addTab(4); qualTabNameArch; " ARCH"
         Print #fileNo, addTab(3); "WHERE"
         Print #fileNo, addTab(4); "DATE(ARCH."; refTsColumnName; ") < refDate_in"
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "ALOG.OBJECTID = ARCH."; g_anOid
       Else
         Print #fileNo, addTab(4); qualTabNameArch; " ARCHNL"
         Print #fileNo, addTab(3); "JOIN"
         Print #fileNo, addTab(4); qualTabParentNameArch; " ARCHPAR"
         Print #fileNo, addTab(3); "ON"
         'To changed to dynamic key name oder switch case if other non aggregate nl text tables follow
         Print #fileNo, addTab(4); "ARCHNL.CLG_OID = ARCHPAR."; g_anOid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(3); "ARCHNL."; g_anPsOid; " = ARCHPAR."; g_anPsOid
         Print #fileNo, addTab(3); "WHERE"
         Print #fileNo, addTab(4); "ALOG.OBJECTID = ARCHNL."; g_anOid
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "DATE(ARCHPAR."; refTsColumnName; ") < refDate_in"
       End If
     End If
     Print #fileNo, addTab(2); ")"
   End Select
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "delete records in archive data pool"
   Select Case acmEntityIndex
   Case g_classIndexCalculationRun
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameArch; " CRUN"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "NOT EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = CRUN."; g_anPsOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS.CRTCAR_OID = CRUN."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case g_classIndexTypeSpec
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameArch; " TYPS"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "WHERE"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(2); ")"
   Case g_classIndexTypeStandardEquipment
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameArch; " TYSE"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TYPS."; g_anOid; " = TYSE.TSETYS_OID"
     Print #fileNo, addTab(2); ")"
   Case g_classIndexProtocolLineEntry
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameArch; " PLE"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "PLE."; g_anPsOid; " = TYPS."; g_anPsOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "PLE.TSPTYS_OID = TYPS."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case g_classIndexProtocolParameter
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameArch; " PROP"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameProtocolLineEntryArch; " PLE"
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameTypeSpecNameArch; " TYPS"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "TYPS."; g_anOid; " = PLE.TSPTYS_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPS."; g_anPsOid; " = PLE."; g_anPsOid
     Print #fileNo, addTab(3); "JOIN"
     Print #fileNo, addTab(4); qualTabNameAspectArch; " NSR"
     Print #fileNo, addTab(3); "ON"
     genDdlForTypeSpecCheckNsr fileNo, 4, "TYPS", refTsColumnName
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "PROP."; g_anPsOid; " = PLE."; g_anPsOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "PROP.PLPLEN_OID = PLE."; g_anOid
     Print #fileNo, addTab(2); ")"
   Case Else
     If ahClassIndex > 0 And Not isAggHead Then
       Print #fileNo, addTab(1); "DELETE FROM"
       Print #fileNo, addTab(2); qualTabNameArch; " ARCH"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "EXISTS ("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "1"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); qualAggHeadTabNameArch; " AH"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ARCH."; g_anPsOid; " = AH."; g_anPsOid
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "ARCH."; g_anAhOid; " = AH."; g_anOid
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "AH."; refTsColumnName; " < refDate_in"
       Print #fileNo, addTab(2); ")"
     Else
       If Not (forNl) Then
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNameArch
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "DATE("; refTsColumnName; ") < refDate_in"
       Else
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNameArch; " ARCHNL"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "EXISTS ("
         Print #fileNo, addTab(3); "SELECT"
         Print #fileNo, addTab(4); "1"
         Print #fileNo, addTab(3); "FROM"
         Print #fileNo, addTab(4); qualTabParentNameArch; " ARCHPAR"
         Print #fileNo, addTab(3); "WHERE"
         'To changed to dynamic key name oder switch case if other non aggregate nl text tables follow
         Print #fileNo, addTab(4); "ARCHNL.CLG_OID = ARCHPAR."; g_anOid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(3); "ARCHNL."; g_anPsOid; " = ARCHPAR."; g_anPsOid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(4); "DATE(ARCHPAR."; refTsColumnName; ") < refDate_in"
         Print #fileNo, addTab(2); ")"
       End If
     End If
   End Select
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

   genSpLogProcExit fileNo, qualProcNameArchiveOrPugeEntity, ddlType, , "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
   Print #fileNo,

 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genArchiveSupportDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   archPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional isPurelyPrivate As Boolean = False, _
   Optional isChangeLogWorkingPoolSpecialHandling As Boolean = False _
 )
   If Not supportArchivePool Or ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim dbObjName As String
   Dim dbObjShortName As String
   Dim entityTypeDescr As String
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim isSubjectToArchiving As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isCtoAliasCreated As Boolean
   Dim notAcmRelated As Boolean
   Dim ahClassIndex As Integer
   Dim isAggHead As Boolean
   Dim refTsColumnName As String
   Dim hasOwnTable As Boolean
   Dim isUserTransactional As Boolean
   Dim useMqtToImplementLrt As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim condenseData As Boolean
   Dim expandExpressionsInFtoView As Boolean

   On Error GoTo ErrorExit

   isAggHead = False
   If acmEntityType = eactClass Then
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName

       If forNl Then
         dbObjName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         dbObjShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged And Not g_classes.descriptors(acmEntityIndex).noRangePartitioning
       Else
         dbObjName = g_classes.descriptors(acmEntityIndex).className
         dbObjShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
       End If
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isSubjectToArchiving = g_classes.descriptors(acmEntityIndex).isSubjectToArchiving
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
       notAcmRelated = g_classes.descriptors(acmEntityIndex).notAcmRelated
       ahClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       refTsColumnName = genAttrName(IIf(g_classes.descriptors(acmEntityIndex).nonStandardRefTimeStampForArchiving <> "", g_classes.descriptors(acmEntityIndex).nonStandardRefTimeStampForArchiving, conValidTo), ddlType)
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       expandExpressionsInFtoView = g_classes.descriptors(acmEntityIndex).expandExpressionsInFtoView
   ElseIf acmEntityType = eactRelationship Then
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName

       If forNl Then
         dbObjName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         dbObjShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged And Not g_relationships.descriptors(acmEntityIndex).noRangePartitioning
       Else
         dbObjName = g_relationships.descriptors(acmEntityIndex).relName
         dbObjShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       End If

       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isSubjectToArchiving = g_relationships.descriptors(acmEntityIndex).isSubjectToArchiving
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
       notAcmRelated = g_relationships.descriptors(acmEntityIndex).notAcmRelated
       ahClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       refTsColumnName = g_anValidTo
       hasOwnTable = True
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = False
       expandExpressionsInFtoView = False
   Else
     Exit Sub
   End If

   Dim qualAggHeadTabNameProd As String
   If ahClassIndex > 0 Then
     qualAggHeadTabNameProd = genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, g_productiveDataPoolIndex)
       refTsColumnName = genAttrName(IIf(g_classes.descriptors(ahClassIndex).nonStandardRefTimeStampForArchiving <> "", g_classes.descriptors(ahClassIndex).nonStandardRefTimeStampForArchiving, conValidTo), ddlType)
   End If

   Const ctoOrgId = 1
   Dim qualTabNameArch As String
   qualTabNameArch = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameProd As String
   qualTabNameProd = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameWork As String
   qualTabNameWork = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameAspectProd As String
   qualTabNameAspectProd = genQualTabNameByEntityIndex(g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameAspectArch As String
   qualTabNameAspectArch = genQualTabNameByEntityIndex(g_classIndexGenericAspect, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, , , 0)

   Dim qualTabNameTypeSpecNameProd As String
   qualTabNameTypeSpecNameProd = genQualTabNameByEntityIndex(g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameProtocolLineEntryWork As String
   qualTabNameProtocolLineEntryWork = genQualTabNameByEntityIndex(g_classIndexProtocolLineEntry, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameProtocolParameterWork As String
   qualTabNameProtocolParameterWork = genQualTabNameByEntityIndex(g_classIndexProtocolParameter, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameTypeSpecNameWork As String
   qualTabNameTypeSpecNameWork = genQualTabNameByEntityIndex(g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, , , forNl)

   Dim qualTabNameTypeSpecNameWorkLrt As String
   qualTabNameTypeSpecNameWorkLrt = genQualTabNameByEntityIndex(g_classIndexTypeSpec, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, True, , forNl)

   Dim qualTabNameSolverData As String
   qualTabNameSolverData = genQualTabNameByEntityIndex(g_classIndexSolverData, acmEntityType, ddlType, , , , , , False)

   Dim qualTabNameProdPar As String
   If forNl Then
     qualTabNameProdPar = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen)
   End If

   Dim qualTabNameProdParWork As String
   If forNl Then
     qualTabNameProdParWork = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen)
   End If

   Dim tabQualifier As String
   tabQualifier = UCase(acmEntityShortName)
 
   Dim baseArchTabName As String
   baseArchTabName = baseName(qualTabNameArch, , ".")

   Dim qualTabNameArchiveLog As String
   qualTabNameArchiveLog = genQualTabNameByClassIndex(g_classIndexArchLog, ddlType, thisOrgIndex, g_archiveDataPoolIndex)

   Dim attrNameFkEntity As String
   attrNameFkEntity = genSurrogateKeyName(ddlType, acmEntityShortName)

   Dim thisMetSchema As String
   thisMetSchema = genSchemaName(snMeta, ssnMeta, ddlType, thisOrgIndex)

   Dim qualViewName As String
   Dim qualViewNameLdm  As String

   ' ####################################################################################################################
   ' #    View 'linking' archive data to productive data
   ' ####################################################################################################################

   If generateArchiveView And Not (isChangeLogWorkingPoolSpecialHandling) Then
     qualViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_archiveDataPoolIndex, forGen, False, , forNl, , "ARC")

     If Not isSubjectToArchiving Then
       printSectionHeader "View 'linking' archive data to productive data / table """ & qualTabNameProd & """ (" & entityTypeDescr & " """ & sectionName & "." & dbObjName & """)", fileNo
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE VIEW"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, addTab(0); "("
 
       If isUserTransactional Then
         If Not forGen And Not forNl Then
           printConditional fileNo, _
             genAttrDeclByDomain( _
               conWorkingState, conWorkingState, eavtEnum, getEnumIndexByName(dxnWorkingState, dnWorkingState), _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True _
             )
         End If

         If condenseData Then
           ' virtually merge-in columns 'INLRT', and 'STATUS_ID'
           printConditional fileNo, _
             genAttrDeclByDomain( _
               conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta, , 1, True _
             )
           printConditional fileNo, _
             genAttrDeclByDomain( _
               enStatus, esnStatus, eavtEnum, g_enumIndexStatus, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta Or eacSetProdMeta, , 1, True _
             )
         End If
 
         printConditional fileNo, _
           genAttrDeclByDomain( _
             conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, _
             acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True _
           )
       End If

       If forNl Then
         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 1, forGen, , _
                                  IIf(isUserTransactional, edomListLrt, edomListNonLrt) Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       Else
         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 1, False, forGen, _
                              IIf(isUserTransactional, edomListLrt, edomListNonLrt) Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       End If

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "AS"
       Print #fileNo, addTab(0); "("

       Print #fileNo, addTab(1); "SELECT"

       If isPurelyPrivate Then
         If isUserTransactional Then
           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS " & g_dbtEnumId & "),"
           End If

           If condenseData Then
             ' virtually merge-in columns 'INLRT' 'STATUS_ID' and 'INUSEBY'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If

           Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
         End If
 
         If forNl Then
           genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, , edomValue Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
         Else
           genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, False, forGen, edomValue Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
         End If
 
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); "SYSIBM.SYSDUMMY1"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "0 = 1"
       Else
         If isUserTransactional Then
           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
           End If
           If condenseData Then
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If
           Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
         End If

         Dim transformation As AttributeListTransformation
         initAttributeTransformation transformation, 0, , , , tabQualifier & "."
         If forNl Then
           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, False, , _
           edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or edomValueVirtual Or edomVirtualPersisted Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
         Else
           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, _
             fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , False, forGen, _
             edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or edomValueVirtual Or edomVirtualPersisted Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
         End If

         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNameProd; " "; tabQualifier

         If isPsTagged Then
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"

           If usePsFltrByDpMappingForRegularViews And usePsDpMappingForArchiveViews Then
             Print #fileNo, addTab(4); "OR"
             Print #fileNo, addTab(3); "("
             Print #fileNo, addTab(4); "("; gc_db2RegVarPsOid; " = '0')"
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "(ARC."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
             Print #fileNo, addTab(3); ")"
           End If
 
           If psTagOptional Then
             Print #fileNo, addTab(4); "OR"
             Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
           End If

           Print #fileNo, addTab(4); "OR"
           Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " = "; g_activePsOidDdl; ")"
           Print #fileNo, addTab(2); ")"
         End If
       End If

       Print #fileNo, addTab(0); ")"

       Print #fileNo, gc_sqlCmdDelim
     Else
       printSectionHeader "View 'merging' productive and archive data / table """ & qualTabNameArch & """ (" & entityTypeDescr & " """ & sectionName & "." & dbObjName & """)", fileNo
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE VIEW"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, addTab(0); "("

 ' FIXME: include this in the gen...ForEntity-routines!!!
       If isUserTransactional Then
         If Not forGen And Not forNl Then
           printConditional fileNo, _
             genAttrDeclByDomain( _
               conWorkingState, conWorkingState, eavtEnum, getEnumIndexByName(dxnWorkingState, dnWorkingState), _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True _
             )
         End If

         printConditional fileNo, _
           genAttrDeclByDomain( _
             conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, _
             acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True _
           )
       End If

       If forNl Then
         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 1, forGen, , _
                                  IIf(isUserTransactional, edomListLrt, edomListNonLrt) Or edomListVirtual Or _
                                  IIf(includeTermStringsInMqt, edomListExpression, edomNone) Or _
                                  IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       Else
         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 1, False, forGen, _
                              IIf(isUserTransactional, edomListLrt, edomListNonLrt) Or edomListVirtual Or _
                              IIf(includeTermStringsInMqt, edomListExpression, edomNone) Or _
                              IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       End If

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "AS"
       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); "SELECT"
 
       If isUserTransactional Then
         If Not forGen And Not forNl Then
           Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
         End If
         Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
       End If

       initAttributeTransformation transformation, 0, , , , "ARC."
       setAttributeTransformationContext transformation, thisOrgIndex, g_productiveDataPoolIndex, "ARC"
       If forNl Then
         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, False, , _
         edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or edomValueVirtual Or edomVirtualPersisted Or _
         IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
         IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       Else
         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, _
           fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , False, forGen, _
           edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or edomValueVirtual Or edomVirtualPersisted Or _
           IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
           IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       End If
 
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNameArch; " ARC"

       If isPsTagged Then
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "("; gc_db2RegVarPsOid; " = '')"

         If usePsFltrByDpMappingForRegularViews And usePsDpMappingForArchiveViews Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '0')"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "(ARC."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
           Print #fileNo, addTab(2); ")"
         End If

         If psTagOptional Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); "(ARC."; g_anPsOid; " IS NULL)"
         End If

         Print #fileNo, addTab(3); "OR"
         Print #fileNo, addTab(2); "(ARC."; g_anPsOid; " = "; g_activePsOidDdl; ")"
       End If

       Print #fileNo, addTab(0); ")"

       Print #fileNo, addTab(0); "UNION ALL"

       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); "SELECT"
 
       If isUserTransactional Then
         If Not forGen And Not forNl Then
           Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
         End If
         Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
       End If

       initAttributeTransformation transformation, 0, , , , "PROD."
       setAttributeTransformationContext transformation, thisOrgIndex, g_productiveDataPoolIndex, "PROD"
       If forNl Then
         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, False, , _
         edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or edomValueVirtual Or edomVirtualPersisted Or _
         IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
         IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       Else
         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, _
           fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , False, forGen, edomListNonLrt Or IIf(isUserTransactional, edomValueLrt, 0) Or _
           edomValueVirtual Or edomVirtualPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
           IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv
       End If

       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNameProd; " PROD"

       If isPsTagged Then
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "("; gc_db2RegVarPsOid; " = '')"

         If usePsFltrByDpMappingForRegularViews And usePsDpMappingForArchiveViews Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '0')"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "(PROD."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
           Print #fileNo, addTab(2); ")"
         End If

         If psTagOptional Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); "(PROD."; g_anPsOid; " IS NULL)"
         End If

         Print #fileNo, addTab(3); "OR"
         Print #fileNo, addTab(2); "(PROD."; g_anPsOid; " = "; g_activePsOidDdl; ")"
       End If

       Print #fileNo, addTab(0); ")"

       Print #fileNo, gc_sqlCmdDelim

     End If

     If ddlType = edtPdm Then
       qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, , , forGen, , , forNl)
       genAliasDdl sectionIndex, dbObjName, isCommonToOrgs, isCommonToPools, True, _
                   qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, g_archiveDataPoolIndex, edatView, forGen And Not forNl, False, False, False, False, _
                   "Archive-View """ & sectionName & "." & dbObjName & """", , , , , True
     End If
   End If

   If Not isSubjectToArchiving Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP for Archiving data for individual Entity
   ' ####################################################################################################################

   Dim qualProcNameArchiveEntity As String
   qualProcNameArchiveEntity = _
   genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl, spnArchiveOrg)
 
   If acmEntityIndex = g_classIndexChangeLog Then
     If isChangeLogWorkingPoolSpecialHandling Then
       qualProcNameArchiveEntity = qualProcNameArchiveEntity + "_WP"
     Else
       qualProcNameArchiveEntity = qualProcNameArchiveEntity + "_PP"
     End If
   End If

   printSectionHeader "SP for Archiving data for " & entityTypeDescr & " '" & sectionName & "." & dbObjName & "'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameArchiveEntity
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "refDate_in", "DATE", True, "only data with validity ending before this date is archived"
   genProcParm fileNo, "IN", "archUserId_in", g_dbtUserId, True, "archived records are tagged with this user as '" & conUpdateUser & "'"
   genProcParm fileNo, "IN", "archTimeStamp_in", "TIMESTAMP", True, "timestamp used for logging archived records"
   If acmEntityIndex = g_classIndexChangeLog Then
     genProcParm fileNo, "IN", "reduceCl_in", "INTEGER", True, "reduce ChangeLog-records if and only if this parameter is '1'"
   End If
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcNameArchiveEntity, ddlType, , "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"

   If thisOrgIndex = g_primaryOrgIndex Or acmEntityIndex = g_classIndexChangeLog Then
     If acmEntityIndex = g_classIndexChangeLog Then
       If Not (forNl) Then
         genProcSectionHeader fileNo, "declare conditions", , True
         genCondDecl fileNo, "alreadyExist", "42710"
       End If

       genProcSectionHeader fileNo, "declare variables"
       genVarDecl fileNo, "v_orgOid", "BIGINT", "NULL"
       genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"

       genProcSectionHeader fileNo, "declare statement"
       genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
       If Not (forNl) Then
         genProcSectionHeader fileNo, "declare condition handler"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"
         genDdlForTempToBeArchived fileNo, 1, , 1
       Else
         genDdlForTempToBeArchived fileNo, 1, 1, 1
         genDdlForTempPsDates fileNo, 1, 1, 1, , 1
       End If
     Else
       genProcSectionHeader fileNo, "declare conditions", , True
       genCondDecl fileNo, "alreadyExist", "42710"
       genProcSectionHeader fileNo, "declare condition handler"
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore ("; tempPsDates; " already exists)"
       Print #fileNo, addTab(1); "END;"
       genDdlForTempPsDates fileNo, 1
     End If

   End If

 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   If acmEntityIndex = g_classIndexChangeLog And forNl Then
     Print #fileNo, addTab(1); "SET v_orgOid = (SELECT ORGOID FROM "; g_qualTabNamePdmOrganization; " WHERE SEQUENCESCHEMANAME = '"; thisMetSchema; "');"
     genProcSectionHeader fileNo, "generate ToBeArchive records"
     Print #fileNo, addTab(1); "IF reduceCl_in = 1 THEN"
     genProcSectionHeader fileNo, "get last GenWS creation date", 2
     Print #fileNo, addTab(2); "MERGE INTO"
     Print #fileNo, addTab(3); tempPsDates; " T"
     Print #fileNo, addTab(2); "USING ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); g_anPsOid; ","
     Print #fileNo, addTab(4); "MAX(" & g_anCreateTimestamp & ") AS genWsProd"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameSolverData
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "SDOORG_OID = v_orgOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "ACCESSMODE_ID = "; g_productiveDataPoolIndex
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "FILENAME = 'root.inf'"
     Print #fileNo, addTab(3); "GROUP BY "; g_anPsOid
     Print #fileNo, addTab(2); ") S"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.psOid = S."; g_anPsOid
     Print #fileNo, addTab(2); "WHEN MATCHED THEN"
     Print #fileNo, addTab(3); "UPDATE SET T.genWsProd = S.genWsProd"
     Print #fileNo, addTab(2); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(3); "INSERT (psOid, genWsProd) VALUES (S."; g_anPsOid; ", S.genWsProd)"
     Print #fileNo, addTab(2); "ELSE IGNORE"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     If Not (isChangeLogWorkingPoolSpecialHandling) And thisOrgIndex = g_primaryOrgIndex Then
       genProcSectionHeader fileNo, "get last WDÜ creation date"
       Print #fileNo, addTab(2); "FOR orgLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "SCHEMANAME AS c_schemaName"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); g_qualTabNamePdmPrimarySchema
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "POOLTYPE_ID = "; g_productiveDataPoolIndex
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "ORGANIZATION_ID > 1"
       Print #fileNo, addTab(3); "ORDER BY"
       Print #fileNo, addTab(4); "ORGANIZATION_ID ASC"
       Print #fileNo, addTab(3); "FOR READ ONLY"
       Print #fileNo, addTab(2); "DO"
       Print #fileNo, addTab(3); "SET v_stmntTxt =               'MERGE INTO ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   '"; tempPsDates; " T ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'USING (SELECT ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   '"; g_anPsOid; ", ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   'LASTCENTRALDATATRANSFERCOMMIT as fto ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'FROM ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   c_schemaName || '.GENERALSETTINGS ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ') S ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'ON ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   'T.psOid = S."; g_anPsOid; " ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'WHEN MATCHED AND COALESCE(T.fto, '"; gc_valDateInfinite; "') > S.fto THEN ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   'UPDATE SET T.fto = S.fto ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'WHEN NOT MATCHED THEN ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||   'INSERT (psOid, fto) VALUES (S."; g_anPsOid; ", S.fto) ';"
       Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || 'ELSE IGNORE';"
       Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,
     End If
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempToBeArchived
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "CLOG.OID"
     Print #fileNo, addTab(2); "FROM"
     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       Print #fileNo, addTab(3); qualTabNameProdPar; " CLOG"
     Else
       Print #fileNo, addTab(3); qualTabNameProdParWork; " CLOG"
     End If
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "EXISTS ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "1"
     Print #fileNo, addTab(4); "FROM"
     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       Print #fileNo, addTab(3); qualTabNameProdPar; " DEL"
     Else
       Print #fileNo, addTab(3); qualTabNameProdParWork; " DEL"
     End If
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "DEL.OPERATION_ID = 3"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "CLOG.OPERATION_ID < 3"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "DEL.OBJECTID = CLOG.OBJECTID"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempToBeArchived; " TBA WHERE CLOG."; g_anOid; " = TBA.oid)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     If isChangeLogWorkingPoolSpecialHandling Then
       Print #fileNo,
       'Print #fileNo, addTab(2); "COMMIT;"
       'Print #fileNo,
       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); tempToBeArchived
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "oid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CLOG.OID"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualTabNameProdParWork; " CLOG"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "CLOG.OPERATION_ID = 3"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 YEAR)"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempToBeArchived; " TBA WHERE CLOG."; g_anOid; " = TBA.oid)"
       Print #fileNo, addTab(2); ";"
       Print #fileNo,
       'Print #fileNo, addTab(2); "COMMIT;"
       'Print #fileNo,
       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); tempToBeArchived
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "oid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CLOG.OID"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualTabNameProdParWork; " CLOG"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "CLOG.DBCOLUMNNAME = 'STATUS_ID'"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 YEAR)"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempToBeArchived; " TBA WHERE CLOG."; g_anOid; " = TBA.oid)"
       Print #fileNo, addTab(2); ";"
       Print #fileNo,
       'Print #fileNo, addTab(2); "COMMIT;"
       'Print #fileNo,
     End If
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempToBeArchived
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "CLOG."; g_anOid
     Print #fileNo, addTab(2); "FROM"
     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       Print #fileNo, addTab(3); qualTabNameProdPar; " CLOG"
     Else
       Print #fileNo, addTab(3); qualTabNameProdParWork; " CLOG"
     End If
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "CLOG.DBCOLUMNNAME LIKE 'S1CT%OID'"
     Print #fileNo, addTab(5); "OR"
     Print #fileNo, addTab(4); "CLOG.DBCOLUMNNAME LIKE 'S0CS%OID'"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(4); "AND "
     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       If thisOrgIndex = g_primaryOrgIndex Then
         Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (SELECT COALESCE(CASE WHEN genWsProd < COALESCE(fto,"; gc_valDateInfinite; ") THEN genWsProd ELSE fto END, "; gc_valDateEarliest; ") FROM "; tempPsDates; " D WHERE CLOG."; g_anPsOid; " = D.psOid)"
       Else
         Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (SELECT COALESCE(genWsProd, "; gc_valDateEarliest; ") FROM "; tempPsDates; " D WHERE CLOG."; g_anPsOid; " = D.psOid)"
       End If
     Else
       Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (SELECT COALESCE(genWsProd, "; gc_valDateEarliest; ") FROM "; tempPsDates; " D WHERE CLOG."; g_anPsOid; " = D.psOid)"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "CLOG.OPTIMESTAMP < (archTimeStamp_in - 1 MONTH)"
     End If
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempToBeArchived; " TBA WHERE CLOG.OID = TBA.oid)"
     Print #fileNo, addTab(2); ";"
    If Not (isChangeLogWorkingPoolSpecialHandling) Then
       Print #fileNo,
       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); tempToBeArchived
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "oid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CLOG.OID"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualTabNameProdPar; " CLOG"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "CLOG.DBCOLUMNNAME = 'LASTUPDATETIMESTAMP'"
       Print #fileNo, addTab(4); "AND "
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "CLOG.OPTIMESTAMP < (SELECT COALESCE(fto, "; gc_valDateEarliest; ") FROM "; tempPsDates; " D WHERE CLOG."; g_anPsOid; " = D.psOid)"
       Print #fileNo, addTab(5); "OR"
       Print #fileNo, addTab(4); "CLOG.OPTIMESTAMP < (archTimeStamp_in - 90 DAY)"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "NOT EXISTS (SELECT 1 FROM "; tempToBeArchived; " TBA WHERE CLOG.OID = TBA.oid)"
       Print #fileNo, addTab(2); ";"
     End If
     Print #fileNo,

     Print #fileNo, addTab(1); "ELSE"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); tempToBeArchived
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "CLOG."; g_anOid
     Print #fileNo, addTab(2); "FROM"
     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       Print #fileNo, addTab(3); qualTabNameProdPar; " CLOG"
     Else
       Print #fileNo, addTab(3); qualTabNameProdParWork; " CLOG"
     End If
     Print #fileNo, addTab(2); "JOIN"
     Print #fileNo, addTab(3); qualTabNameAspectArch; " GA"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "GA."; g_anOid; " = CLOG.AHOBJECTID"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "GA."; g_anPsOid; " = CLOG."; g_anPsOid
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END IF;"
   End If

   If refTsColumnName <> "" Then
     If isUserTransactional Then
       genProcSectionHeader fileNo, "verify that no records are locked"
       Print #fileNo, addTab(1); "IF EXISTS ("
       Print #fileNo, addTab(2); "SELECT"

       Select Case acmEntityIndex
       Case g_classIndexTypeSpec
         Print #fileNo, addTab(3); "WORK."; g_anOid
         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); qualTabNameWork; " WORK,"
         Print #fileNo, addTab(3); qualTabNameAspectProd; " NSR"
       Case g_classIndexTypeStandardEquipment
         Print #fileNo, addTab(3); "WORK."; g_anOid
         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); qualTabNameWork; " WORK,"
         Print #fileNo, addTab(3); qualTabNameTypeSpecNameWork; " TYPS,"
         Print #fileNo, addTab(3); qualTabNameAspectProd; " NSR"
       Case Else
         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(3); "WORK."; g_anOid
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); qualAggHeadTabNameProd; " AH"
           Print #fileNo, addTab(2); "JOIN"
         Else
           Print #fileNo, addTab(3); "PROD."; g_anOid
           Print #fileNo, addTab(2); "FROM"
           If forNl Then
             Print #fileNo, addTab(3); qualTabNameProdPar; " PRODPAR,"
           End If
         End If
         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(3); qualTabNameWork; " WORK"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "WORK."; g_anAhOid; " = AH."; g_anOid
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "WORK."; g_anPsOid; " = AH."; g_anPsOid
           'onlyfactory
           If thisOrgIndex = g_primaryOrgIndex Then
             Print #fileNo, addTab(2); "JOIN"
             Print #fileNo, addTab(3); tempPsDates; " FTO"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "AH."; g_anPsOid; " = FTO.psOid"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
           End If
         Else
           Print #fileNo, addTab(3); qualTabNameProd; " PROD,"
           Print #fileNo, addTab(3); qualTabNameWork; " WORK"
         End If
       End Select

       Print #fileNo, addTab(2); "WHERE"
       If ahClassIndex > 0 And Not isAggHead Then
         Print #fileNo, addTab(3); "AH."; refTsColumnName; " < refDate_in"
       Else
         Select Case acmEntityIndex
         Case g_classIndexCalculationRun
           'Special Case CalculationRun - Archive only if all TypeSpecs are archived
           genDdlForCalculationRunCheckTypeSpec fileNo, 3, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD"
           genDdlForWorkProdJoinWithPs fileNo, 3
         Case g_classIndexTypeSpec
           'Special Case TypeSpec - Archive only if referenced NSR1 is archived
           genDdlForTypeSpecCheckNsr fileNo, 3, "WORK", refTsColumnName
         Case g_classIndexTypeStandardEquipment
           'Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
           genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 3, "WORK", refTsColumnName
         Case Else
           If forNl Then
             Print #fileNo, addTab(3); "PROD."; attrNameFkEntity; " = "; " PRODPAR."; g_anOid
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "DATE(PRODPAR."; refTsColumnName; ") < refDate_in"
           Else
             Print #fileNo, addTab(3); "DATE(PROD."; refTsColumnName; ") < refDate_in"
           End If
           genDdlForWorkProdJoinWithPs fileNo, 3
         End Select
       End If
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "WORK."; g_anInLrt; " IS NOT NULL"
       Print #fileNo, addTab(1); ") THEN"

       genSpLogProcEscape fileNo, qualProcNameArchiveEntity, ddlType, 2, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"
       genSignalDdl "archRecordLocked", fileNo, 2, qualTabNameProd

       Print #fileNo, addTab(1); "END IF;"
     End If

     If Not (isChangeLogWorkingPoolSpecialHandling) Then
       genProcSectionHeader fileNo, "generate archive log records"
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualTabNameArchiveLog
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "ARCHIVETIMESTAMP,"
       Print #fileNo, addTab(2); "DBTABLENAME,"
       Print #fileNo, addTab(2); "OBJECTID"
       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "archTimeStamp_in,"
       Print #fileNo, addTab(2); "'"; baseArchTabName; "',"
       If acmEntityIndex = g_classIndexChangeLog Then
         If Not (forNl) Then
           Print #fileNo, addTab(2); "TBA.oid"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); tempToBeArchived; " TBA"
         Else
           Print #fileNo, addTab(2); "PROD."; g_anOid
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); tempToBeArchived; " TBA"
           Print #fileNo, addTab(1); "JOIN"
           Print #fileNo, addTab(2); qualTabNameProd; " PROD"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "TBA.oid = PROD.CLG_OID"
         End If
       Else
         If acmEntityIndex = g_classIndexProtocolLineEntry Or _
         acmEntityIndex = g_classIndexProtocolParameter Or _
         acmEntityIndex = g_classIndexTypeSpec Or _
         acmEntityIndex = g_classIndexTypeStandardEquipment _
         Then
           Print #fileNo, addTab(2); "WORK."; g_anOid
         Else
           Print #fileNo, addTab(2); "PROD."; g_anOid
         End If

         Print #fileNo, addTab(1); "FROM"

         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(2); qualAggHeadTabNameProd; " AH"
           Print #fileNo, addTab(1); "JOIN"
           Print #fileNo, addTab(2); qualTabNameProd; " PROD"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "PROD."; g_anAhOid; " = AH."; g_anOid
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PROD."; g_anPsOid; " = AH."; g_anPsOid
           If thisOrgIndex = g_primaryOrgIndex Then
             Print #fileNo, addTab(1); "JOIN"
             Print #fileNo, addTab(2); tempPsDates; " FTO"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "AH."; g_anPsOid; " = FTO.psOid"
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
           End If
         Else
           If forNl Then
             Print #fileNo, addTab(2); qualTabNameProdPar; " PRODPAR,"
           End If
         End If

         Select Case acmEntityIndex
         Case g_classIndexTypeSpec
           Print #fileNo, addTab(2); qualTabNameWork; " WORK,"
           Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
         Case g_classIndexTypeStandardEquipment
           Print #fileNo, addTab(2); qualTabNameWork; " WORK,"
           Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
           Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
         Case g_classIndexProtocolLineEntry
           Print #fileNo, addTab(2); qualTabNameProtocolLineEntryWork; " WORK,"
           Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
           Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
         Case g_classIndexProtocolParameter
           Print #fileNo, addTab(2); qualTabNameProtocolParameterWork; " WORK,"
           Print #fileNo, addTab(2); qualTabNameProtocolLineEntryWork; " PLE,"
           Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
           Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
         Case g_classIndexGenericAspect
           If Not (ahClassIndex > 0 And Not isAggHead) Then
             Print #fileNo, addTab(2); qualTabNameProd; " PROD"
             If thisOrgIndex = g_primaryOrgIndex Then
               Print #fileNo, addTab(1); "JOIN"
               Print #fileNo, addTab(2); tempPsDates; " FTO"
               Print #fileNo, addTab(1); "ON"
               If ahClassIndex > 0 And Not isAggHead Then
                 Print #fileNo, addTab(2); "AH."; g_anPsOid; " = FTO.psOid"
                 Print #fileNo, addTab(3); "AND"
                 Print #fileNo, addTab(2); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
               Else
                 Print #fileNo, addTab(2); "PROD."; g_anPsOid; " = FTO.psOid"
                 Print #fileNo, addTab(3); "AND"
                 Print #fileNo, addTab(2); "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit"
               End If
             End If
           End If
         Case Else
           If ahClassIndex > 0 And Not isAggHead Then
             ' handled above
           Else
             Print #fileNo, addTab(2); qualTabNameProd; " PROD"
           End If
         End Select

         Print #fileNo, addTab(1); "WHERE"
         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(2); "AH."; refTsColumnName; " < refDate_in"
         Else
           Select Case acmEntityIndex
           Case g_classIndexCalculationRun
             'Special Case CalculationRun - Archive only if all TypeSpecs are archived
             genDdlForCalculationRunCheckTypeSpec fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD"
          Case g_classIndexTypeSpec
             'Special Case TypeSpec - Archive only if referenced NSR1 is archived
             genDdlForTypeSpecCheckNsr fileNo, 2, "WORK", refTsColumnName
           Case g_classIndexTypeStandardEquipment
             'Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
             genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 2, "WORK", refTsColumnName
           Case g_classIndexProtocolLineEntry
             'Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
             genDdlForProtocolLineEntryCheckTypeSpecNsr fileNo, 2, refTsColumnName
           Case g_classIndexProtocolParameter
             'Special Case ProtocolParameter - Archive only if referenced TypeSpec is archived
             genDdlForProtocolParameterCheckPleTypeSpecNsr fileNo, 2, refTsColumnName
           Case Else
             If forNl Then
               Print #fileNo, addTab(2); "PROD."; attrNameFkEntity; " = "; " PRODPAR."; g_anOid
               Print #fileNo, addTab(3); "AND"
               Print #fileNo, addTab(2); "DATE(PRODPAR."; refTsColumnName; ") < refDate_in"
             Else
               Print #fileNo, addTab(2); "DATE(PROD."; refTsColumnName; ") < refDate_in"
             End If
           End Select
         End If
       End If

       Print #fileNo, addTab(1); ";"


       If (acmEntityIndex = g_classIndexGenericAspect And Not forNl And thisOrgIndex <> g_primaryOrgIndex) Then
     genProcSectionHeader fileNo, "verify that no records to be updated are locked"

     Print #fileNo, addTab(1); "IF EXISTS ("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "WORK."; g_anOid
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameWork; " WORK"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "DATE(WORK."; refTsColumnName; ") >= refDate_in"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "WORK."; g_anInLrt; " IS NOT NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "WORK.CCPCCP_OID IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "PROD."; g_anOid
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualTabNameProd; " PROD"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "DATE(PROD."; refTsColumnName; ") < refDate_in)"
     Print #fileNo, addTab(1); ") THEN"

     genSpLogProcEscape fileNo, qualProcNameArchiveEntity, ddlType, 2, "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"
     genSignalDdl "archRecordLocked", fileNo, 2, qualTabNameWork

     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "update references to aspects to be archived in work"
     Print #fileNo, addTab(1); "UPDATE "
     Print #fileNo, addTab(2); qualTabNameWork
     Print #fileNo, addTab(1); "SET "
     Print #fileNo, addTab(2); "CCPCCP_OID = NULL,"
     Print #fileNo, addTab(2); "VERSIONID = VERSIONID + 1,"
     Print #fileNo, addTab(2); "UPDATEUSER = archUserId_in,"
     Print #fileNo, addTab(2); "LASTUPDATETIMESTAMP = archTimeStamp_in"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "DATE("; refTsColumnName; ") >= refDate_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "CCPCCP_OID IN ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "PROD."; g_anOid
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameProd; " PROD"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
     Print #fileNo, addTab(1); ");"


     genProcSectionHeader fileNo, "update references to aspects to be archived in prod"
     Print #fileNo, addTab(1); "UPDATE "
     Print #fileNo, addTab(2); qualTabNameProd
     Print #fileNo, addTab(1); "SET "
     Print #fileNo, addTab(2); "CCPCCP_OID = NULL,"
     Print #fileNo, addTab(2); "VERSIONID = VERSIONID + 1,"
     Print #fileNo, addTab(2); "UPDATEUSER = archUserId_in,"
     Print #fileNo, addTab(2); "LASTUPDATETIMESTAMP = archTimeStamp_in"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "DATE("; refTsColumnName; ") >= refDate_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "CCPCCP_OID IN ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "PROD."; g_anOid
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameProd; " PROD"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
     Print #fileNo, addTab(1); ");"

   End If


       genProcSectionHeader fileNo, "copy records to archive data pool"
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualTabNameArch
       Print #fileNo, addTab(1); "("

       If forNl Then
         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, , edomListNonLrt
       Else
         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, False, forGen, edomListNonLrt
       End If

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"

       If acmEntityIndex = g_classIndexProtocolLineEntry Or acmEntityIndex = g_classIndexProtocolParameter Then
         initAttributeTransformation transformation, 0, , , , "WORK."
       Else
         initAttributeTransformation transformation, 0, , , , "PROD."
       End If

       If forNl Then
         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, forGen, , , edomListNonLrt
       Else
         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , , forGen, edomListNonLrt
       End If

       Print #fileNo, addTab(1); "FROM"

       Select Case acmEntityIndex
       Case g_classIndexTypeSpec
         Print #fileNo, addTab(2); qualTabNameProd; " PROD,"
         Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
       Case g_classIndexTypeStandardEquipment
         Print #fileNo, addTab(2); qualTabNameProd; " PROD,"
         Print #fileNo, addTab(2); qualTabNameTypeSpecNameProd; " TYPS,"
         Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
       Case g_classIndexProtocolLineEntry
         Print #fileNo, addTab(2); qualTabNameProtocolLineEntryWork; " WORK,"
         Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
         Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
       Case g_classIndexProtocolParameter
         Print #fileNo, addTab(2); qualTabNameProtocolParameterWork; " WORK,"
         Print #fileNo, addTab(2); qualTabNameProtocolLineEntryWork; " PLE,"
         Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
         Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
       Case g_classIndexGenericAspect
         Print #fileNo, addTab(2); qualTabNameProd; " PROD"
         If thisOrgIndex = g_primaryOrgIndex Then
           If Not (ahClassIndex > 0 And Not isAggHead) Then
             Print #fileNo, addTab(1); "JOIN"
             Print #fileNo, addTab(2); tempPsDates; " FTO"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "PROD."; g_anPsOid; " = FTO.psOid"
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit"
           End If
         End If
       Case Else
         Print #fileNo, addTab(2); qualTabNameProd; " PROD"
       End Select

       If acmEntityIndex = g_classIndexChangeLog Then
         Print #fileNo, addTab(1); "JOIN"
         Print #fileNo, addTab(2); tempToBeArchived; " TBA"
         Print #fileNo, addTab(1); "ON"
         If Not (forNl) Then
           Print #fileNo, addTab(2); "PROD."; g_anOid; " = TBA.oid"
         Else
           Print #fileNo, addTab(2); "PROD."; attrNameFkEntity; " = TBA.oid"
         End If
       Else
         Print #fileNo, addTab(1); "WHERE"
         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(2); "EXISTS ("
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "1"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); qualAggHeadTabNameProd; " AH"
           If thisOrgIndex = g_primaryOrgIndex Then
             Print #fileNo, addTab(3); "JOIN"
             Print #fileNo, addTab(4); tempPsDates; " FTO"
             Print #fileNo, addTab(3); "ON"
             Print #fileNo, addTab(4); "AH."; g_anPsOid; " = FTO.psOid"
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
           End If
           Print #fileNo, addTab(3); "WHERE"
           Print #fileNo, addTab(4); "PROD."; g_anAhOid; " = AH."; g_anOid
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = AH."; g_anPsOid
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "AH."; refTsColumnName; " < refDate_in"
           Print #fileNo, addTab(2); ")"
         Else
           Select Case acmEntityIndex
           Case g_classIndexCalculationRun
             'Special Case CalculationRun - Archive only if all TypeSpecs are archived
             genDdlForCalculationRunCheckTypeSpec fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD"
           Case g_classIndexTypeSpec
             'Special Case TypeSpec - Archive only if referenced NSR1 is archived
             genDdlForTypeSpecCheckNsr fileNo, 2, "PROD", refTsColumnName
           Case g_classIndexTypeStandardEquipment
             'Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
             genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 2, "PROD", refTsColumnName
           Case g_classIndexProtocolLineEntry
             'Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
             genDdlForProtocolLineEntryCheckTypeSpecNsr fileNo, 2, refTsColumnName
           Case g_classIndexProtocolParameter
             'Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
             genDdlForProtocolParameterCheckPleTypeSpecNsr fileNo, 2, refTsColumnName
           Case Else
             If forNl Then
               Print #fileNo, addTab(2); "DATE(PRODPAR."; refTsColumnName; ") < refDate_in"
             Else
               Print #fileNo, addTab(2); "DATE(PROD."; refTsColumnName; ") < refDate_in"
             End If
           End Select
         End If
       End If

       Print #fileNo, addTab(1); ";"


       genProcSectionHeader fileNo, "count the number of affected rows"
       Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

       If Not (acmEntityIndex = g_classIndexChangeLog) Then
         genProcSectionHeader fileNo, "delete records in work data pool"
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNameWork; " WORK"
         Print #fileNo, addTab(1); "WHERE"

         If ahClassIndex > 0 And Not isAggHead Then
           Print #fileNo, addTab(2); "EXISTS ("
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "1"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); qualAggHeadTabNameProd; " AH"
           If thisOrgIndex = g_primaryOrgIndex Then
             Print #fileNo, addTab(3); "JOIN"
             Print #fileNo, addTab(4); tempPsDates; " FTO"
             Print #fileNo, addTab(3); "ON"
             Print #fileNo, addTab(4); "AH."; g_anPsOid; " = FTO.psOid"
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
           End If
           Print #fileNo, addTab(3); "WHERE"
           Print #fileNo, addTab(4); "WORK."; g_anAhOid; " = AH."; g_anOid
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "WORK."; g_anPsOid; " = AH."; g_anPsOid
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "AH."; refTsColumnName; " < refDate_in"
           Print #fileNo, addTab(2); ")"
         Else
           Select Case acmEntityIndex
           Case g_classIndexCalculationRun
            genDdlForCalculationRunCheckTypeSpec fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "WORK"
            'Print #fileNo, addTab(2); "NOT EXISTS ("
            ' Print #fileNo, addTab(3); "SELECT"
            ' Print #fileNo, addTab(4); "1"
            ' Print #fileNo, addTab(3); "FROM"
            ' Print #fileNo, addTab(4); qualTabNameTypeSpecNameWork; " TYPS"
            ' Print #fileNo, addTab(3); "WHERE"
            ' Print #fileNo, addTab(4); "WORK."; g_anOid; " = TYPS.CRTCAR_OID"
            ' Print #fileNo, addTab(5); "AND"
            ' Print #fileNo, addTab(4); "WORK."; g_anPsOid; " = TYPS."; g_anPsOid
            ' Print #fileNo, addTab(2); ")"
           Case g_classIndexTypeSpec
             'Special Case TypeSpec - Archive only if referenced NSR1 is archived
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabNameProd; " PROD,"
             Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
             Print #fileNo, addTab(3); "WHERE"
             genDdlForTypeSpecCheckNsr fileNo, 4, "PROD", refTsColumnName
             genDdlForWorkProdJoinWithPs fileNo, 4
             Print #fileNo, addTab(2); ")"
           Case g_classIndexTypeStandardEquipment
             'Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabNameProd; " PROD,"
             Print #fileNo, addTab(4); qualTabNameTypeSpecNameProd; " TYPS,"
             Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
             Print #fileNo, addTab(3); "WHERE"
             genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 4, "PROD", refTsColumnName
             genDdlForWorkProdJoinWithPs fileNo, 4
             Print #fileNo, addTab(2); ")"
           Case g_classIndexProtocolLineEntry
             'Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabNameTypeSpecNameWork; " TYPS,"
             Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
             Print #fileNo, addTab(3); "WHERE"
             genDdlForProtocolLineEntryCheckTypeSpecNsr fileNo, 4, refTsColumnName
             Print #fileNo, addTab(2); ")"
           Case g_classIndexProtocolParameter
             'Special Case ProtocolLineEntries - Archive only if referenced TypeSpec is archived
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabNameProtocolLineEntryWork; " PLE,"
             Print #fileNo, addTab(4); qualTabNameTypeSpecNameWork; " TYPS,"
             Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
             Print #fileNo, addTab(3); "WHERE"
             genDdlForProtocolParameterCheckPleTypeSpecNsr fileNo, 4, refTsColumnName
             Print #fileNo, addTab(2); ")"
           Case g_classIndexGenericAspect
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabNameProd; " PROD"
             If thisOrgIndex = g_primaryOrgIndex Then
               Print #fileNo, addTab(3); "JOIN"
               Print #fileNo, addTab(4); tempPsDates; " FTO"
               Print #fileNo, addTab(3); "ON"
               Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = FTO.psOid"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit"
             End If
             Print #fileNo, addTab(3); "WHERE"
             Print #fileNo, addTab(4); "PROD."; g_anOid; " = WORK."; g_anOid
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = WORK."; g_anPsOid
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
             Print #fileNo, addTab(2); ")"
           Case Else
             If forNl Then
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameProdPar; " PRODPAR"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "WORK."; attrNameFkEntity; " = "; " PRODPAR."; g_anOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "DATE(PRODPAR."; refTsColumnName; ") < refDate_in"
               Print #fileNo, addTab(2); ")"
             Else
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameProd; " PROD"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "PROD."; g_anOid; " = WORK."; g_anOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
               Print #fileNo, addTab(2); ")"
             End If
           End Select
         End If

         Print #fileNo, addTab(1); ";"
       End If

       If Not (acmEntityIndex = g_classIndexProtocolLineEntry Or acmEntityIndex = g_classIndexProtocolParameter) Then
         genProcSectionHeader fileNo, "delete records in productive data pool"
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNameProd; " PROD"
         Print #fileNo, addTab(1); "WHERE"

         If acmEntityIndex = g_classIndexChangeLog Then
           Print #fileNo, addTab(2); "EXISTS ("
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "1"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); tempToBeArchived; " TBA"
           Print #fileNo, addTab(3); "WHERE"
           If Not (forNl) Then
            Print #fileNo, addTab(4); "PROD."; g_anOid; " = TBA.oid"
           Else
            Print #fileNo, addTab(4); "PROD."; attrNameFkEntity; " = TBA.oid"
           End If
           Print #fileNo, addTab(2); ")"
         Else
           If ahClassIndex > 0 And Not isAggHead Then
             Print #fileNo, addTab(2); "EXISTS ("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualAggHeadTabNameProd; " AH"
             If thisOrgIndex = g_primaryOrgIndex Then
               Print #fileNo, addTab(3); "JOIN"
               Print #fileNo, addTab(4); tempPsDates; " FTO"
               Print #fileNo, addTab(3); "ON"
               Print #fileNo, addTab(4); "AH."; g_anPsOid; " = FTO.psOid"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "AH.LASTUPDATETIMESTAMP < FTO.ftoCommit"
             End If
             Print #fileNo, addTab(3); "WHERE"
             Print #fileNo, addTab(4); "PROD."; g_anAhOid; " = AH."; g_anOid
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = AH."; g_anPsOid
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "AH."; refTsColumnName; " < refDate_in"
             Print #fileNo, addTab(2); ")"
           Else
             Select Case acmEntityIndex
             Case g_classIndexCalculationRun
               'Special Case CalculationRun - Archive only if all TypeSpecs in Work and Prod are archived
               genDdlForCalculationRunCheckTypeSpec fileNo, 2, qualTabNameTypeSpecNameProd, qualTabNameTypeSpecNameWork, "PROD"
             Case g_classIndexTypeSpec
               'Special Case TypeSpec - Archive only if referenced NSR1 is archived
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(3); "WHERE"
               genDdlForTypeSpecCheckNsr fileNo, 4, "PROD", refTsColumnName
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "copy records to archive data pool for work data pool only records"
               Print #fileNo, addTab(1); "INSERT INTO"
               Print #fileNo, addTab(2); qualTabNameArch
               Print #fileNo, addTab(1); "("
               genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, False, forGen, edomListNonLrt
               Print #fileNo, addTab(1); ")"
               Print #fileNo, addTab(1); "SELECT"
               initAttributeTransformation transformation, 1, , , , "WORK."
               setAttributeMapping transformation, 1, "TSTTPA_OID", "NULL"

               genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , , forGen, edomListNonLrt
               Print #fileNo, addTab(1); "FROM"
               Print #fileNo, addTab(2); qualTabNameWork; " WORK,"
               Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(1); "WHERE"
               genDdlForTypeSpecCheckNsr fileNo, 2, "WORK", refTsColumnName
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "count the number of affected rows"
               Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

               genProcSectionHeader fileNo, "delete records in work data pool for work data pool only records"
               Print #fileNo, addTab(1); "DELETE FROM"
               Print #fileNo, addTab(2); qualTabNameWork; " WORK"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(3); "WHERE"
               genDdlForTypeSpecCheckNsr fileNo, 4, "WORK", refTsColumnName
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "delete TypeSpec to TypePriceAssignment in work data pool"
               Print #fileNo, addTab(1); "UPDATE"
               Print #fileNo, addTab(2); qualTabNameWork; " TS_WORK"
               Print #fileNo, addTab(1); "SET"
               Print #fileNo, addTab(2); "TS_WORK.TSTTPA_OID = null"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " PROD"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = TS_WORK."; g_anPsOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD."; g_anOid; " = TS_WORK.TSTTPA_OID"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD.CLASSID = '09032'"
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "delete TypeSpec_Lrt to TypePriceAssignment in work data pool"
               Print #fileNo, addTab(1); "UPDATE"
               Print #fileNo, addTab(2); qualTabNameWork; "_LRT TS_WORK"
               Print #fileNo, addTab(1); "SET"
               Print #fileNo, addTab(2); "TS_WORK.TSTTPA_OID = null"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " PROD"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = TS_WORK."; g_anPsOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD."; g_anOid; " = TS_WORK.TSTTPA_OID"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD.CLASSID = '09032'"
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "delete TypeSpec to TypePriceAssignment in prod data pool"
               Print #fileNo, addTab(1); "UPDATE"
               Print #fileNo, addTab(2); qualTabNameProd; " TS_PROD"
               Print #fileNo, addTab(1); "SET"
               Print #fileNo, addTab(2); "TS_PROD.TSTTPA_OID = null"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " PROD"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = TS_PROD."; g_anPsOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD."; g_anOid; " = TS_PROD.TSTTPA_OID"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "DATE(PROD."; refTsColumnName; ") < refDate_in"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PROD.CLASSID = '09032'"
               Print #fileNo, addTab(2); ")"

             Case g_classIndexTypeStandardEquipment
               'Special Case TypeStandardEquipment - Archive only if referenced TypeSpec is archived
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameTypeSpecNameProd; " TYPS,"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(3); "WHERE"
               genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 4, "PROD", refTsColumnName
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "copy records to archive data pool for work data pool only records"
               Print #fileNo, addTab(1); "INSERT INTO"
               Print #fileNo, addTab(2); qualTabNameArch
               Print #fileNo, addTab(1); "("
               genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, False, forGen, edomListNonLrt

               Print #fileNo, addTab(1); ")"
               Print #fileNo, addTab(1); "SELECT"
               initAttributeTransformation transformation, 0, , , , "WORK."
               genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, g_archiveDataPoolIndex, 2, , , forGen, edomListNonLrt

               Print #fileNo, addTab(1); "FROM"
               Print #fileNo, addTab(2); qualTabNameWork; " WORK,"
               Print #fileNo, addTab(2); qualTabNameTypeSpecNameWork; " TYPS,"
               Print #fileNo, addTab(2); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(1); "WHERE"
               genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 2, "WORK", refTsColumnName
               Print #fileNo, addTab(1); ";"

               genProcSectionHeader fileNo, "count the number of affected rows"
               Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

               genProcSectionHeader fileNo, "delete records in work data pool for work data pool only records"
               Print #fileNo, addTab(1); "DELETE FROM"
               Print #fileNo, addTab(2); qualTabNameWork; " WORK"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "EXISTS ("
               Print #fileNo, addTab(3); "SELECT"
               Print #fileNo, addTab(4); "1"
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameTypeSpecNameWork; " TYPS,"
               Print #fileNo, addTab(4); qualTabNameAspectProd; " NSR"
               Print #fileNo, addTab(3); "WHERE"
               genDdlForTypeStandardEquipmentCheckTypeSpecNsr fileNo, 4, "WORK", refTsColumnName
               Print #fileNo, addTab(2); ")"

             Case Else
               If forNl Then
                 Print #fileNo, addTab(2); "EXISTS ("
                 Print #fileNo, addTab(3); "SELECT"
                 Print #fileNo, addTab(4); "1"
                 Print #fileNo, addTab(3); "FROM"
                 Print #fileNo, addTab(4); qualTabNameProdPar; " PRODPAR"
                 Print #fileNo, addTab(3); "WHERE"
                 Print #fileNo, addTab(4); "PROD."; attrNameFkEntity; " = "; " PRODPAR."; g_anOid
                 Print #fileNo, addTab(5); "AND"
                 Print #fileNo, addTab(4); "DATE(PRODPAR."; refTsColumnName; ") < refDate_in"
                 Print #fileNo, addTab(2); ")"
               Else
                 Print #fileNo, addTab(2); "DATE(PROD."; refTsColumnName; ") < refDate_in"
                 If thisOrgIndex = g_primaryOrgIndex Then
                   Print #fileNo, addTab(3); "AND"
                   Print #fileNo, addTab(2); "EXISTS("
                   Print #fileNo, addTab(3); "SELECT"
                   Print #fileNo, addTab(4); "1"
                   Print #fileNo, addTab(3); "FROM"
                   Print #fileNo, addTab(4); tempPsDates; " FTO"
                   Print #fileNo, addTab(3); "WHERE"
                   Print #fileNo, addTab(4); "PROD."; g_anPsOid; " = FTO.psOid"
                   Print #fileNo, addTab(5); "AND"
                   Print #fileNo, addTab(4); "PROD.LASTUPDATETIMESTAMP < FTO.ftoCommit"
                   Print #fileNo, addTab(2); ")"
                 End If
               End If
             End Select
           End If
         End If
         Print #fileNo, addTab(1); ";"
       End If
     Else
       'Special Case ChangeLog Work
       genProcSectionHeader fileNo, "delete records in work data pool"
       Print #fileNo, addTab(1); "DELETE FROM"
       Print #fileNo, addTab(2); qualTabNameWork; " WORK"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "EXISTS ("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "1"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); tempToBeArchived; " TBA"
       Print #fileNo, addTab(3); "WHERE"
       If Not (forNl) Then
         Print #fileNo, addTab(4); "WORK."; g_anOid; " = TBA.oid"
       Else
         Print #fileNo, addTab(4); "WORK."; attrNameFkEntity; " = TBA.oid"
       End If
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(1); ";"
     End If

   End If

   genSpLogProcExit fileNo, qualProcNameArchiveEntity, ddlType, , "#refDate_in", "'archUserId_in", "#archTimeStamp_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for Estimating Volume of Archive Data for individual Entity
   ' ####################################################################################################################

   If Not (isChangeLogWorkingPoolSpecialHandling) Then
     Dim qualProcNameArchiveEntityEstimate As String
     qualProcNameArchiveEntityEstimate = _
       genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_productiveDataPoolIndex, forGen, , , forNl, spnArchiveOrgEstimate)

     printSectionHeader "SP for Estimating Volume of Archive Data for " & entityTypeDescr & " '" & sectionName & "." & dbObjName & "'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameArchiveEntityEstimate
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "refDate_in", "DATE", True, "count only data with validity ending before this date"
     genProcParm fileNo, "OUT", "rowCount_out", "BIGINT", False, "number of rows in Productive Data Pool ready to be archived"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare variables"
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL"
     genVarDecl fileNo, "v_tgtVarName", "VARCHAR(10)", "NULL"
     genVarDecl fileNo, "v_sizeFactor", "INTEGER", "1"
     genVarDecl fileNo, "v_rowCount", "BIGINT", "NULL"
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "alreadyExist", "42710"

     genProcSectionHeader fileNo, "declare statement"
     genVarDecl fileNo, "v_stmnt", "STATEMENT"

     genProcSectionHeader fileNo, "declare cursor"
     Print #fileNo, addTab(1); "DECLARE cntCursor CURSOR FOR v_stmnt;"

     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempArchiveStats fileNo, , False, True

     genSpLogProcEnter fileNo, qualProcNameArchiveEntityEstimate, ddlType, , "#refDate_in", "rowCount_out"

     genProcSectionHeader fileNo, "initialize output parameter"
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"

     genProcSectionHeader fileNo, "examine each involved table"
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "RTRIM(PARCH."; g_anPdmFkSchemaName; ") AS c_ARCHTABSCHEMA,"
     Print #fileNo, addTab(3); "RTRIM(P."; g_anPdmFkSchemaName; ") AS c_TABSCHEMA,"
     Print #fileNo, addTab(3); "RTRIM(P."; g_anPdmTableName; ") AS c_TABNAME,"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " AS c_POOLTYPE_ID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LARCH"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LARCH."; g_anAcmEntityName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LARCH."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LARCH."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = LARCH."; g_anLdmIsGen
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = LARCH."; g_anLdmIsNl
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = LARCH."; g_anLdmIsLrt; ""
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = LARCH."; g_anLdmIsMqt
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PARCH"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "PARCH."; g_anPdmLdmFkSchemaName; " = LARCH."; g_anLdmSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "PARCH."; g_anPdmLdmFkTableName; " = LARCH."; g_anLdmTableName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "PARCH."; g_anOrganizationId; " = P."; g_anOrganizationId
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "PARCH."; g_anPoolTypeId; " = "; CStr(g_archiveDataPoolId)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; CStr(g_productiveDataPoolId)

     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = '"; UCase(sectionName); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = '"; UCase(acmEntityName); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; getAcmEntityTypeKey(acmEntityType); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "(L."; g_anLdmIsLrt; " = 0 OR L."; g_anLdmIsMqt; " = 1)"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " IN ("; CStr(g_workDataPoolId); ", "; CStr(g_productiveDataPoolId); ")"
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; ","
     Print #fileNo, addTab(3); "L."; g_anLdmIsMqt
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
     genProcSectionHeader fileNo, "determine number of records in this table to be archived", 2, True

     Print #fileNo, addTab(2); "SET v_tgtVarName = (CASE WHEN c_"; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType); " THEN 'PROD' ELSE 'TGT' END);"

     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_stmntTxt ="
     Print #fileNo, addTab(4); "'SELECT ' ||"
     Print #fileNo, addTab(5); "'COUNT(*) ' ||"
     Print #fileNo, addTab(4); "'FROM ' ||"
     Print #fileNo, addTab(5); "c_TABSCHEMA || '.' || c_TABNAME || ' ' || v_tgtVarName || ' ' ||"
     Print #fileNo, addTab(4); "'WHERE ' ||"

     If ahClassIndex > 0 And Not isAggHead Then
       Print #fileNo, addTab(5); "'EXISTS (' ||"
       Print #fileNo, addTab(6); "'SELECT ' ||"
       Print #fileNo, addTab(7); "'1 ' ||"
       Print #fileNo, addTab(6); "'FROM ' ||"
       Print #fileNo, addTab(7); "(CASE WHEN c_"; g_anPoolTypeId; " = "; genPoolId(g_productiveDataPoolIndex, ddlType); " THEN '' ELSE '"; qualTabNameProd; " PROD,' END) ||"
       Print #fileNo, addTab(7); "'"; qualAggHeadTabNameProd; " AH ' ||"
       Print #fileNo, addTab(6); "'WHERE ' ||"
       Print #fileNo, addTab(7); "(CASE WHEN c_POOLTYPE_ID = "; genPoolId(g_productiveDataPoolIndex, ddlType); " THEN '' ELSE 'PROD."; g_anOid; " = TGT."; g_anOid; " AND ' END) ||"
       Print #fileNo, addTab(7); "'PROD."; g_anAhOid; " = AH."; g_anOid; " ' ||"
       Print #fileNo, addTab(8); "'AND ' ||"
       If Not hasOwnTable Then
         Print #fileNo, addTab(7); "v_tgtVarName || '."; g_anAhCid; " = AH."; g_anCid; " ' ||"
         Print #fileNo, addTab(8); "'AND ' ||"
       End If
       Print #fileNo, addTab(7); "'AH."; refTsColumnName; " < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||"
       Print #fileNo, addTab(5); "') ' ||"
     Else
       If forNl Then
         Print #fileNo, addTab(5); "'EXISTS (' ||"
         Print #fileNo, addTab(6); "'SELECT ' ||"
         Print #fileNo, addTab(5); "'1 ' ||"
         Print #fileNo, addTab(6); "'FROM ' ||"
         Print #fileNo, addTab(7); "'"; qualTabNameProdPar; " PRODPAR ' ||"
         Print #fileNo, addTab(6); "'WHERE ' ||"
         Print #fileNo, addTab(7); "v_tgtVarName || '."; attrNameFkEntity; " = PRODPAR."; g_anOid; " ' ||"
         Print #fileNo, addTab(8); "'AND ' ||"
         Print #fileNo, addTab(7); "'DATE(PRODPAR."; refTsColumnName; ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||"
         Print #fileNo, addTab(5); "') ' ||"
       Else
         Print #fileNo, addTab(5); "("
         Print #fileNo, addTab(6); "CASE WHEN c_POOLTYPE_ID = "; genPoolId(g_productiveDataPoolIndex, ddlType)
         Print #fileNo, addTab(6); "THEN 'DATE(PROD."; refTsColumnName; ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''') '"
         Print #fileNo, addTab(6); "ELSE"
         Print #fileNo, addTab(7); "'EXISTS (' ||"
         Print #fileNo, addTab(8); "'SELECT ' ||"
         Print #fileNo, addTab(9); "'1 ' ||"
         Print #fileNo, addTab(8); "'FROM ' ||"
         Print #fileNo, addTab(9); "'"; qualTabNameProd; " PROD ' ||"
         Print #fileNo, addTab(8); "'WHERE ' ||"
         Print #fileNo, addTab(9); "'PROD."; g_anOid; " = ' || v_tgtVarName || '."; g_anOid; " ' ||"
         Print #fileNo, addTab(10); "'AND ' ||"
         Print #fileNo, addTab(9); "'DATE(PROD."; refTsColumnName; ") < DATE(''' || RTRIM(CHAR(refDate_in)) || ''')' ||"
         Print #fileNo, addTab(7); "') '"
         Print #fileNo, addTab(6); "END"
         Print #fileNo, addTab(5); ") ||"
       End If
     End If

     Print #fileNo, addTab(4); "'WITH UR';"
     Print #fileNo,

     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo, addTab(2); "OPEN cntCursor;"
     Print #fileNo,
     Print #fileNo, addTab(2); "FETCH"
     Print #fileNo, addTab(3); "cntCursor"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(2); "CLOSE cntCursor;"
     Print #fileNo,
     Print #fileNo, addTab(2); "IF c_POOLTYPE_ID = "; genPoolId(g_productiveDataPoolIndex, ddlType); " THEN"
     Print #fileNo, addTab(3); "SET rowCount_out = v_rowCount;"
     Print #fileNo, addTab(2); "END IF;"

     Dim indent As Integer
     Dim forArchivePool As Boolean
     indent = 0
     Dim j As Integer
     For j = 1 To 2
       forArchivePool = (j = 2)
       If forArchivePool Then
         Print #fileNo,
         Print #fileNo, addTab(2); "IF c_POOLTYPE_ID = "; genPoolId(g_productiveDataPoolIndex, ddlType); " THEN"
         indent = 1
       End If
       genProcSectionHeader fileNo, "create statistics / estimate record for this table in " & IIf(forArchivePool, "archive", "this") & " pool", indent + 2, forArchivePool
       Print #fileNo, addTab(indent + 2); "INSERT INTO"
       Print #fileNo, addTab(indent + 3); tempArchiveTabStatsTabName
       Print #fileNo, addTab(indent + 2); "("
       Print #fileNo, addTab(indent + 3); "orgId,"
       Print #fileNo, addTab(indent + 3); "poolId,"
       Print #fileNo, addTab(indent + 3); "tabSchema,"
       Print #fileNo, addTab(indent + 3); "tabName,"
       Print #fileNo, addTab(indent + 3); "card,"
       Print #fileNo, addTab(indent + 3); "size,"
       Print #fileNo, addTab(indent + 3); "avgRowLen,"
       Print #fileNo, addTab(indent + 3); "cardArch,"
       Print #fileNo, addTab(indent + 3); "sizeArch"
       Print #fileNo, addTab(indent + 2); ")"
       Print #fileNo, addTab(indent + 2); "SELECT"
       Print #fileNo, addTab(indent + 3); CStr(g_orgs.descriptors(thisOrgIndex).id); ","
       If forArchivePool Then
         Print #fileNo, addTab(indent + 3); CStr(g_archiveDataPoolId); ","
         Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_ARCHTABSCHEMA,30)) AS "; g_dbtDbSchemaName; "),"
       Else
         Print #fileNo, addTab(indent + 3); "c_POOLTYPE_ID,"
         Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_TABSCHEMA,30))     AS "; g_dbtDbSchemaName; "),"
       End If
       Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_TABNAME,  50))     AS VARCHAR(50)),"
       Print #fileNo, addTab(indent + 3); "T.CARD,"
       Print #fileNo, addTab(indent + 3); "T.CARD * (SUM(C.AVGCOLLEN) + 10),"
       Print #fileNo, addTab(indent + 3); "SUM(C.AVGCOLLEN) + 10,"
       Print #fileNo, addTab(indent + 3); "v_rowCount,"
       Print #fileNo, addTab(indent + 3); IIf(forArchivePool, "", "-1 * "); "(v_rowCount * (SUM(C.AVGCOLLEN) + 10))"
       Print #fileNo, addTab(indent + 2); "FROM"
       Print #fileNo, addTab(indent + 3); "SYSCAT.TABLES T"
       Print #fileNo, addTab(indent + 2); "INNER JOIN"
       Print #fileNo, addTab(indent + 3); "SYSCAT.COLUMNS C"
       Print #fileNo, addTab(indent + 2); "ON"
       If forArchivePool Then
         Print #fileNo, addTab(indent + 3); "C.TABSCHEMA = c_ARCHTABSCHEMA"
       Else
         Print #fileNo, addTab(indent + 3); "C.TABSCHEMA = c_TABSCHEMA"
       End If
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "C.TABNAME = T.TABNAME"
       Print #fileNo, addTab(indent + 2); "WHERE"
       Print #fileNo, addTab(indent + 3); "T.TABSCHEMA = c_TABSCHEMA"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "T.TABNAME = c_TABNAME"
       Print #fileNo, addTab(indent + 2); "GROUP BY"
       Print #fileNo, addTab(indent + 3); "T.TABSCHEMA,"
       Print #fileNo, addTab(indent + 3); "T.TABNAME,"
       Print #fileNo, addTab(indent + 3); "T.CARD"
       Print #fileNo, addTab(indent + 2); "WITH UR;"

       genProcSectionHeader fileNo, "create statistics / estimate record for indexes of this table in " & IIf(forArchivePool, "archive", "this") & " pool", indent + 2
       Print #fileNo, addTab(indent + 2); "INSERT INTO"
       Print #fileNo, addTab(indent + 3); tempArchiveIndStatsTabName
       Print #fileNo, addTab(indent + 2); "("
       Print #fileNo, addTab(indent + 3); "orgId,"
       Print #fileNo, addTab(indent + 3); "poolId,"
       Print #fileNo, addTab(indent + 3); "tabSchema,"
       Print #fileNo, addTab(indent + 3); "tabName,"
       Print #fileNo, addTab(indent + 3); "indName,"
       Print #fileNo, addTab(indent + 3); "card,"
       Print #fileNo, addTab(indent + 3); "size,"
       Print #fileNo, addTab(indent + 3); "avgKeyLen,"
       Print #fileNo, addTab(indent + 3); "cardArch,"
       Print #fileNo, addTab(indent + 3); "sizeArch"
       Print #fileNo, addTab(indent + 2); ")"
       Print #fileNo, addTab(indent + 2); "SELECT"
       Print #fileNo, addTab(indent + 3); CStr(g_orgs.descriptors(thisOrgIndex).id); ","
       If forArchivePool Then
         Print #fileNo, addTab(indent + 3); CStr(g_archiveDataPoolId); ","
         Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_ARCHTABSCHEMA,30)) AS "; g_dbtDbSchemaName; "),"
       Else
         Print #fileNo, addTab(indent + 3); "c_POOLTYPE_ID,"
         Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_TABSCHEMA,30))     AS "; g_dbtDbSchemaName; "),"
       End If
       Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(c_TABNAME,  50))     AS VARCHAR(50)),"
       Print #fileNo, addTab(indent + 3); "CAST(RTRIM(LEFT(I.INDNAME,  20))     AS VARCHAR(20)),"
       Print #fileNo, addTab(indent + 3); "T.CARD,"
       Print #fileNo, addTab(indent + 3); "(T.CARD * (SUM(C.AVGCOLLEN) + 9)) * 2,"
       Print #fileNo, addTab(indent + 3); "SUM(C.AVGCOLLEN) + 9,"
       Print #fileNo, addTab(indent + 3); "v_rowCount,"
       Print #fileNo, addTab(indent + 3); IIf(forArchivePool, "", "-"); "((v_rowCount * (SUM(C.AVGCOLLEN) + 9)) * 2)"
       Print #fileNo, addTab(indent + 2); "FROM"
       Print #fileNo, addTab(indent + 3); "SYSCAT.TABLES T"
       Print #fileNo, addTab(indent + 2); "INNER JOIN"
       Print #fileNo, addTab(indent + 3); "SYSCAT.INDEXES I"
       Print #fileNo, addTab(indent + 2); "ON"
       Print #fileNo, addTab(indent + 3); "T.TABSCHEMA = I.TABSCHEMA"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "T.TABNAME = I.TABNAME"
       Print #fileNo, addTab(indent + 2); "INNER JOIN"
       Print #fileNo, addTab(indent + 3); "SYSCAT.INDEXCOLUSE IC"
       Print #fileNo, addTab(indent + 2); "ON"
       Print #fileNo, addTab(indent + 3); "I.INDSCHEMA = IC.INDSCHEMA"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "I.INDNAME = IC.INDNAME"
       Print #fileNo, addTab(indent + 2); "INNER JOIN"
       Print #fileNo, addTab(indent + 3); "SYSCAT.COLUMNS C"
       Print #fileNo, addTab(indent + 2); "ON"
       If forArchivePool Then
         Print #fileNo, addTab(indent + 3); "I.TABSCHEMA = c_ARCHTABSCHEMA"
       Else
         Print #fileNo, addTab(indent + 3); "I.TABSCHEMA = c_TABSCHEMA"
       End If
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "I.TABNAME = C.TABNAME"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "IC.COLNAME = C.COLNAME"
       Print #fileNo, addTab(indent + 2); "WHERE"
       Print #fileNo, addTab(indent + 3); "C.TABSCHEMA = c_TABSCHEMA"
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); "T.TABNAME = c_TABNAME"
       Print #fileNo, addTab(indent + 2); "GROUP BY"
       Print #fileNo, addTab(indent + 3); "T.TABSCHEMA,"
       Print #fileNo, addTab(indent + 3); "T.TABNAME,"
       Print #fileNo, addTab(indent + 3); "I.INDNAME,"
       Print #fileNo, addTab(indent + 3); "T.CARD"
       Print #fileNo, addTab(indent + 2); "WITH UR;"

       If forArchivePool Then
         Print #fileNo, addTab(2); "END IF;"
       End If
     Next j
     Print #fileNo, addTab(1); "END FOR;"

     genSpLogProcExit fileNo, qualProcNameArchiveEntityEstimate, ddlType, , "#refDate_in", "rowCount_out"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genArchiveSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   archPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   Dim hasNlTab As Boolean
   Dim nlTabIsPurelyPrivate As Boolean

   On Error GoTo ErrorExit

     hasNlTab = _
       (forGen And g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses) Or _
       (Not forGen And (g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses Or (g_classes.descriptors(classIndex).aggHeadClassIndex = g_classes.descriptors(classIndex).classIndex And g_classes.descriptors(classIndex).implicitelyGenChangeComment And Not g_classes.descriptors(classIndex).condenseData)))
     nlTabIsPurelyPrivate = _
       hasNlTab And _
       Not (forGen And g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses) And _
       Not (Not forGen And (g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses))

   genArchiveSupportDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen
   If classIndex = g_classIndexChangeLog Then
   	genArchiveSupportDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen, , , True
   End If
   genArchiveOrgPurgeDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen
   If hasNlTab Then
     genArchiveSupportDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen, True, nlTabIsPurelyPrivate
     If classIndex = g_classIndexChangeLog Then
     	genArchiveSupportDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen, True, nlTabIsPurelyPrivate, True
     End If
     genArchiveOrgPurgeDdlForEntity classIndex, eactClass, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, forGen, True, nlTabIsPurelyPrivate
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genArchiveSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   archPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
     genArchiveSupportDdlForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType
     genArchiveOrgPurgeDdlForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType

     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       genArchiveSupportDdlForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, , True
       genArchiveOrgPurgeDdlForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, g_archiveDataPoolIndex, fileNo, ddlType, , True
     End If
 End Sub
 ' ### ENDIF IVK ###
 
 
 
 
 
 
 
 
