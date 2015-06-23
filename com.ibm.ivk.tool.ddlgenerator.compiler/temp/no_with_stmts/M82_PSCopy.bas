 Attribute VB_Name = "M82_PSCopy"
 ' ### IF IVK ###
 Option Explicit
 
 Global Const tempOidMapTabName = "SESSION.OidMap"
 Global Const tempOidNewTabName = "SESSION.OidNew"
 
 Private Const processingStep = 1
 
 
 Sub genDdlForTempOidMap( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional includeTableForNewRecords As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
 
   genProcSectionHeader fileNo, "temporary table" & IIf(includeTableForNewRecords, "s", "") & " for OID-mapping"
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempOidMapTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "map2Oid    "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve

   If includeTableForNewRecords Then
     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); tempOidNewTabName
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "oid        "; g_dbtOid
     Print #fileNo, addTab(indent + 0); ")"

     genDdlForTempTableDeclTrailer fileNo, indent, True, onCommitPreserve, onRollbackPreserve
   End If
 End Sub
 
 
 Sub genPsCopySupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtLdm Then
     genPsCopySupportDdlByType edtLdm
   ElseIf ddlType = edtPdm Then
     genPsCopySupportDdlByType edtPdm

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportLrt Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_orgs.descriptors(thisOrgIndex).isPrimary Then
             genPsCopySupportDdlByPool thisOrgIndex, thisPoolIndex, edtPdm
           End If
          Next thisOrgIndex
        End If
      Next thisPoolIndex
   End If
 End Sub
 
 
 Private Sub genPsCopySupportDdlByType( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not g_genLrtSupport Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)
 
   ' ####################################################################################################################
   ' #    create view to determine LDM tables 'forming the Product Structure'
   ' ####################################################################################################################

   Dim qualViewName As String
   qualViewName = _
     genQualViewName( _
       g_sectionIndexDbMeta, vnPsFormingLdmTab, vsnPsFormingLdmTab, ddlType _
     )
 
   printSectionHeader "View for all LDM-tables 'forming the Product Structure'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); "TABSCHEMA,"
   Print #fileNo, addTab(1); "TABNAME,"
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "AE."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "AE."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmSchemaName; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmTableName; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmIsLrt; ""
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " AE,"
   Print #fileNo, addTab(2); g_qualTabNameLdmTable; " LT"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "AE."; conIsPsForming; " = "; gc_dbTrue
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genPsCopySupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' PS-Copy is only supported at 'pool-level'
     Exit Sub
   End If
 
   If ddlType = edtPdm And (thisOrgIndex <> g_primaryOrgId) Then
     ' PS-Copy is only supported at for 'primary organization'
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   ' ####################################################################################################################
   ' #    create view to determine PDM tables 'forming the Product Structure'
   ' ####################################################################################################################

   If ddlType = edtPdm Then
     Dim qualViewNamePsFormingLdmTable As String
     Dim qualViewNamePsFormingPdmTable As String
 
     qualViewNamePsFormingLdmTable = _
       genQualViewName(g_sectionIndexDbMeta, vnPsFormingLdmTab, vsnPsFormingLdmTab, ddlType)
     qualViewNamePsFormingPdmTable = _
       genQualViewName(g_sectionIndexLrt, vnPsFormingPdmTab, vsnPsFormingPdmTab, ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader "View for all PDM-tables 'forming the Product Structure'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE VIEW"
     Print #fileNo, addTab(1); qualViewNamePsFormingPdmTable
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "TABSCHEMA,"
     Print #fileNo, addTab(1); "TABNAME,"
     Print #fileNo, addTab(1); g_anLdmIsNl; ","
     Print #fileNo, addTab(1); g_anLdmIsGen; ","
     Print #fileNo, addTab(1); g_anLdmIsLrt
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "PT."; g_anPdmFkSchemaName; ","
     Print #fileNo, addTab(2); "PT."; g_anPdmTableName; ","
     Print #fileNo, addTab(2); "AL."; g_anLdmIsNl; ","
     Print #fileNo, addTab(2); "AL."; g_anLdmIsGen; ","
     Print #fileNo, addTab(2); "AL."; g_anLdmIsLrt; ""
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualViewNamePsFormingLdmTable; " AL,"
     Print #fileNo, addTab(2); g_qualTabNamePdmTable; " PT"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkSchemaName; " = AL.TABSCHEMA"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkTableName; " = AL.TABNAME"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "PT."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "PT."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)

     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
   ' ####################################################################################################################
   ' #    SP for Copy-PS-Data-To-LRT-Tables
   ' ####################################################################################################################

   qualViewNamePsFormingPdmTable = _
     genQualViewName(g_sectionIndexLrt, vnPsFormingPdmTab, vsnPsFormingPdmTab, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim psShortName As String
   psShortName = g_classes.descriptors(g_classIndexProductStructure).shortName

   Dim qualLrtBeginProcName As String
   qualLrtBeginProcName = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim transformation As AttributeListTransformation

   Dim qualProcNamePsCp2Lrt As String
   Dim extended As Boolean
   Dim i As Integer
   For i = 1 To IIf(generatePsCopyExtendedSupport, 2, 1)
     extended = (i = 2)
     qualProcNamePsCp2Lrt = genQualProcName(g_sectionIndexAliasLrt, "PSCP2LRT" & IIf(extended, "_EXT", ""), ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader "SP for 'Copying PS-Data to LRT-Tables'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNamePsCp2Lrt
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
     genProcParm fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number"
     genProcParm fileNo, "IN", "psOidOld_in", g_dbtOid, True, "OID of the Product Structure to copy"
     If extended Then
       genProcParm fileNo, "IN", "useLoggingforLrtTabs_in", g_dbtBoolean, True, "if set to '0', logging is disabled for LRT-related tables, otherwise enabled"
       genProcParm fileNo, "IN", "commitEachTable_in", g_dbtBoolean, True, "if set to '1' commit after each table"
     End If
     genProcParm fileNo, "OUT", "lrtOid_out", g_dbtLrtId, True, "ID of the LRT related to the copied Product Structure data"
     genProcParm fileNo, "OUT", "psOidNew_out", g_dbtOid, True, "OID of the new Product Structure"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied (sum over all tables)"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "alreadyExist", "42710"
     If Not extended Then
       genCondDecl fileNo, "notFound", "02000"
     End If

     genProcSectionHeader fileNo, "declare variables"
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
     If Not extended Then
       genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
       genVarDecl fileNo, "v_tabSchema", g_dbtDbSchemaName, "NULL"
       genVarDecl fileNo, "v_tabName", g_dbtDbTableName, "NULL"
     End If
     genVarDecl fileNo, "v_psOidNew", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_lrtOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_isCentralDataTransfer", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_divisionOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_currentTs", "TIMESTAMP", "NULL"
     genVarDecl fileNo, "v_tempCategoryOid", g_dbtOid, "NULL"
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare statement"
     genVarDecl fileNo, "v_stmnt", "STATEMENT"

     If Not extended Then
       genProcSectionHeader fileNo, "declare cursors"
       Print #fileNo, addTab(1); "DECLARE tabCursor CURSOR FOR"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "TABSCHEMA,"
       Print #fileNo, addTab(3); "TABNAME"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualViewNamePsFormingPdmTable
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); g_anLdmIsLrt; " = "; gc_dbFalse
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); g_anLdmIsGen; " ASC,"
       Print #fileNo, addTab(3); g_anLdmIsNl; " ASC"
       Print #fileNo, addTab(2); "FOR READ ONLY"
       Print #fileNo, addTab(1); ";"
     End If

     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
     Print #fileNo, addTab(1); "END;"
     If Not extended Then
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "SET v_atEnd = "; gc_dbTrue; ";"
       Print #fileNo, addTab(1); "END;"
     End If

     genDdlForTempOidMap fileNo

     If extended Then
       genSpLogProcEnter fileNo, qualProcNamePsCp2Lrt, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
     Else
       genSpLogProcEnter fileNo, qualProcNamePsCp2Lrt, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
     End If

     genProcSectionHeader fileNo, "initialize variables"
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     Print #fileNo, addTab(1); "SET v_currentTs  = CURRENT TIMESTAMP;"
     If extended Then
       Print #fileNo, addTab(1); "SET useLoggingforLrtTabs_in = COALESCE(useLoggingforLrtTabs_in, 1);"
       Print #fileNo, addTab(1); "SET commitEachTable_in      = COALESCE(commitEachTable_in     , 0);"
     End If
     genProcSectionHeader fileNo, "determine division OID"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "PS.PDIDIV_OID"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_divisionOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "PS."; g_anOid; " = psOidOld_in"
     Print #fileNo, addTab(1); "WITH UR;"

     genProcSectionHeader fileNo, "create OID of new Product Structure"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "MAX("; g_anOid; ") + "; CStr(gc_sequenceIncrementValue)
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_psOidNew"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " < 1"; gc_sequenceMinValue
     Print #fileNo, addTab(1); "WITH UR;"
     Print #fileNo, addTab(1); "SET v_psOidNew = COALESCE(v_psOidNew, NEXTVAL FOR "; qualSeqNameOid; ");"

     genProcSectionHeader fileNo, "copy base Product Structure element"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "("

       genAttrListForEntity g_classes.descriptors(g_classIndexProductStructure).classIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 6

     setAttributeMapping transformation, 1, cosnOid, "v_psOidNew"
     setAttributeMapping transformation, 2, conIsUnderConstruction, gc_dbTrue
     setAttributeMapping transformation, 3, conCreateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 5, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 6, conUpdateUser, "cdUserId_in"

       genTransformedAttrListForEntity g_classes.descriptors(g_classIndexProductStructure).classIndex, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = psOidOld_in"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "copy NL-TEXT attributes for Product Structure"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameProductStructureNl
     Print #fileNo, addTab(1); "("

       genNlsAttrDeclsForEntity g_classes.descriptors(g_classIndexProductStructure).classIndex, eactClass, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

       initAttributeTransformation transformation, 3, , False, False, , _
                                  cosnOid, "NEXTVAL FOR " & qualSeqNameOid, _
                                  genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexProductStructure).shortName), "v_psOidNew", _
                                  conVersionId, "1"
       genNlsTransformedAttrListForEntity g_classes.descriptors(g_classIndexProductStructure).classIndex, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructureNl
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); genSurrogateKeyName(ddlType, psShortName); " = psOidOld_in"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "initialize OID-mapping"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); tempOidMapTabName
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid,"
     Print #fileNo, addTab(2); "map2Oid"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "psOidOld_in,"
     Print #fileNo, addTab(2); "v_psOidNew"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "begin a new LRT"
     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualLrtBeginProcName; "(?,?,?,?,?)' ;"
     Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE"
     Print #fileNo, addTab(2); "v_stmnt"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_lrtOid"
     Print #fileNo, addTab(1); "USING"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "trNumber_in,"
     Print #fileNo, addTab(2); "v_psOidNew,"
     Print #fileNo, addTab(2); "v_isCentralDataTransfer"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "loop over all PS-forming tables and copy data into LRT-tables"
     If extended Then
       Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "PT."; g_anPdmFkSchemaName; " AS c_tabSchema,"
       Print #fileNo, addTab(3); "PT."; g_anPdmTableName; " AS c_tabName,"
       Print #fileNo, addTab(3); "PT_MQT."; g_anPdmTableName; " AS c_tabNameMqt,"
       Print #fileNo, addTab(3); "PT_PRIV."; g_anPdmTableName; " AS c_tabNamePriv"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " AE"
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LT"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "LT."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PT"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PT."; g_anPdmLdmFkSchemaName; " = LT."; g_anLdmSchemaName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT."; g_anPdmLdmFkTableName; " = LT."; g_anLdmTableName

       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LT_PRIV"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anLdmIsGen; " = LT."; g_anLdmIsGen
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anLdmIsNl; " = LT."; g_anLdmIsNl
       Print #fileNo, addTab(2); "INNER JOIN"
       Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PT_PRIV"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PT_PRIV."; g_anPdmLdmFkSchemaName; " = LT_PRIV."; g_anLdmSchemaName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT_PRIV."; g_anPdmLdmFkTableName; " = LT_PRIV."; g_anLdmTableName

       Print #fileNo, addTab(2); "LEFT OUTER JOIN"
       Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LT_MQT"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "LT_MQT."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anLdmIsLrt; " = "; gc_dbTrue
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anLdmIsGen; " = LT."; g_anLdmIsGen
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anLdmIsNl; " = LT."; g_anLdmIsNl
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_MQT."; g_anLdmIsMqt; " = "; gc_dbTrue
       Print #fileNo, addTab(2); "LEFT OUTER JOIN"
       Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PT_MQT"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PT_MQT."; g_anPdmLdmFkSchemaName; " = LT_MQT."; g_anLdmSchemaName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT_MQT."; g_anPdmLdmFkTableName; " = LT_MQT."; g_anLdmTableName
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT_MQT."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT_MQT."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)

       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "PT."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT."; g_anLdmIsLrt; " = "; gc_dbFalse
       Print #fileNo, addTab(4); "AND"

       Print #fileNo, addTab(3); "PT_PRIV."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PT_PRIV."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anLdmIsLrt; " = "; gc_dbTrue
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "LT_PRIV."; g_anLdmIsMqt; " = "; gc_dbFalse
       Print #fileNo, addTab(4); "AND"

       Print #fileNo, addTab(3); "("

       Print #fileNo, addTab(4); "(AE."; g_anIsPsForming; " = 1)"
       Dim c As Integer
       For c = 1 To g_classes.numDescriptors
           If g_classes.descriptors(c).supportExtendedPsCopy And (g_classes.descriptors(c).superClassIndex <= 0) Then
             Print #fileNo, addTab(5); "OR"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "AE."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(c).sectionName); "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "AE."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(c).className); "'"
             Print #fileNo, addTab(4); ")"
           End If
       Next c
       Dim r As Integer
       For r = 1 To g_relationships.numDescriptors
           If g_relationships.descriptors(r).supportExtendedPsCopy And g_relationships.descriptors(r).implementsInOwnTable Then
             Print #fileNo, addTab(5); "OR"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "AE."; g_anAcmEntitySection; " = '"; UCase(g_relationships.descriptors(r).sectionName); "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "AE."; g_anAcmEntityName; " = '"; UCase(g_relationships.descriptors(r).relName); "'"
             Print #fileNo, addTab(4); ")"
           End If
       Next r

       Print #fileNo, addTab(3); ")"

       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "LT."; g_anLdmIsGen; " ASC,"
       Print #fileNo, addTab(3); "LT."; g_anLdmIsNl; " ASC"
       Print #fileNo, addTab(2); "FOR READ ONLY"
       Print #fileNo, addTab(1); "DO"
     Else
       Print #fileNo, addTab(1); "OPEN tabCursor;"
       Print #fileNo, addTab(1); "SET v_atEnd = "; gc_dbFalse; ";"
       Print #fileNo,
       Print #fileNo, addTab(1); "WHILE (v_atEnd = 0) DO"
       Print #fileNo, addTab(2); "FETCH"
       Print #fileNo, addTab(3); "tabCursor"
       Print #fileNo, addTab(2); "INTO"
       Print #fileNo, addTab(3); "v_tabSchema,"
       Print #fileNo, addTab(3); "v_tabName"
       Print #fileNo, addTab(2); ";"
       Print #fileNo,
       Print #fileNo, addTab(2); "IF ((v_atEnd <> 0) OR (v_tabSchema IS NULL)) THEN"
       Print #fileNo, addTab(3); "GOTO ExitLoop;"
       Print #fileNo, addTab(2); "END IF;"
       Print #fileNo,
     End If

     If extended Then
       genProcSectionHeader fileNo, "disable logging on table (if required)", 2, True
       Print #fileNo, addTab(2); "IF useLoggingforLrtTabs_in = "; gc_dbFalse; " THEN"
       Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabName || ' ACTIVATE NOT LOGGED INITIALLY';"
       Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
       Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabNamePriv || ' ACTIVATE NOT LOGGED INITIALLY';"
       Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(3); "IF c_tabNameMqt IS NOT NULL THEN"
       Print #fileNo, addTab(4); "SET v_stmntTxt = 'ALTER TABLE ' || c_tabSchema || '.' || c_tabNameMqt || ' ACTIVATE NOT LOGGED INITIALLY';"
       Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
       Print #fileNo, addTab(3); "END IF;"

       Print #fileNo, addTab(2); "END IF;"
       Print #fileNo,
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_tabSchema || '.PSCP2LRT_' || c_tabName || '(?,?,?,?,?,?)';"
     Else
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || v_tabSchema || '.PSCP2LRT_' || v_tabName || '(?,?,?,?,?,?)';"
     End If

     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     Print #fileNo, addTab(3); "v_lrtOid,"
     Print #fileNo, addTab(3); "psOidOld_in,"
     Print #fileNo, addTab(3); "v_psOidNew,"
     Print #fileNo, addTab(3); "cdUserId_in,"
     Print #fileNo, addTab(3); "v_currentTs"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);"

     If extended Then
       genProcSectionHeader fileNo, "commit (if required)", 2, True
       Print #fileNo, addTab(2); "IF commitEachTable_in = 1 THEN"
       Print #fileNo, addTab(3); "COMMIT;"
       Print #fileNo, addTab(2); "END IF;"

       Print #fileNo, addTab(1); "END FOR;"
     Else
       Print #fileNo, addTab(1); "END WHILE;"
       Print #fileNo, addTab(1); "ExitLoop:"
       Print #fileNo,
       Print #fileNo, addTab(1); "CLOSE tabCursor WITH RELEASE;"
     End If

     Dim qualTabNameCodeCategoryLrt As String
     qualTabNameCodeCategoryLrt = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, True)
     Dim qualTabNameCategoryLrt As String
     qualTabNameCategoryLrt = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, False, True)
     Dim qualTabNameCode As String
     qualTabNameCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, False)

     genProcSectionHeader fileNo, "determine OID of Temporary Category"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "CAT."; g_anOid
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_tempCategoryOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameCategoryLrt; " CAT"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "CAT."; g_anPsOid; " = v_psOidNew"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "CAT."; g_anIsDefault; " = "; gc_dbTrue
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "CAT."; g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY -- there should be only one record"
     Print #fileNo, addTab(1); "WITH UR;"

     genProcSectionHeader fileNo, "associate all Code related to 'v_divisionOid' with temporay Category"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCodeCategoryLrt
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_relIndexCodeCategory, eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 16

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 5, conVersionId, "1"

     setAttributeMapping transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 10, "GCO_OID", "C." & g_anOid
     setAttributeMapping transformation, 11, "CAT_OID", "v_tempCategoryOid"
     setAttributeMapping transformation, 12, conPsOid, "v_psOidNew"

     setAttributeMapping transformation, 13, conAhClassId, "'" & getClassIdStrByIndex(g_classIndexStandardCode) & "'"
     setAttributeMapping transformation, 14, conAhOId, "C." & g_anOid
     setAttributeMapping transformation, 15, conDpClassNumber, "CAST(NULL AS SMALLINT)"

     setAttributeMapping transformation, 16, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_relIndexCodeCategory, eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameCode; " C"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "C.CDIDIV_OID = v_divisionOid"
     Print #fileNo, addTab(1); "AND"
     Print #fileNo, addTab(2); "NOT EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameCodeCategoryLrt; " CC"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "CC."; g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "CC.GCO_OID = C."; g_anOid
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "associate all other Codes with temporary Category"
     Print #fileNo, addTab(1); "UPDATE vl6ccde011.codecategory_lrt SET cat_oid = v_tempCategoryOid WHERE inlrt = v_lrtOid;"
 
     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
       genProcSectionHeader fileNo, "register all PS-related entities as being affected by the LRT"

       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity
       Print #fileNo, addTab(1); "("

       genAttrListForEntity g_classIndexLrtAffectedEntity, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

       Print #fileNo, addTab(1); ")"

       If extended Then
         Print #fileNo,
         Print #fileNo, addTab(1); "WITH"
         Print #fileNo, addTab(2); "V_ExtraEntities"
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); "entityId,"
         Print #fileNo, addTab(2); "entityType"
         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "AS"
         Print #fileNo, addTab(1); "("

         Dim firstRow As Boolean
         firstRow = True
         Dim j As Integer
         For j = 1 To g_classes.numDescriptors
             If g_classes.descriptors(j).supportExtendedPsCopy And (g_classes.descriptors(j).superClassIndex <= 0) Then
               If firstRow Then
                 firstRow = False
               Else
                 Print #fileNo, addTab(2); "UNION ALL"
               End If
               Print #fileNo, addTab(2); "VALUES ('"; g_classes.descriptors(j).classIdStr; "', '"; gc_acmEntityTypeKeyClass; "')"
             End If
         Next j

         For j = 1 To g_relationships.numDescriptors
             If g_relationships.descriptors(j).supportExtendedPsCopy And g_relationships.descriptors(j).implementsInOwnTable Then
               If firstRow Then
                 firstRow = False
               Else
                 Print #fileNo, addTab(2); "UNION ALL"
               End If

               Print #fileNo, addTab(2); "VALUES ('"; g_relationships.descriptors(j).relIdStr; "', '"; gc_acmEntityTypeKeyRel; "')"
             End If
         Next j
         Print #fileNo, addTab(1); ")"
       End If

       Print #fileNo, addTab(1); "SELECT DISTINCT"

       initAttributeTransformation transformation, 3, , , , "PSE."

       setAttributeMapping transformation, 1, conLrtOid, "v_lrtOid"
       setAttributeMapping transformation, 2, conAcmOrParEntityId, "PSE." & g_anAcmEntityId
       setAttributeMapping transformation, 3, conLrtOpId, CStr(lrtStatusCreated)

       genTransformedAttrListForEntity g_classIndexLrtAffectedEntity, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "A."; g_anAcmEntityId; ","
       Print #fileNo, addTab(4); "A."; g_anAcmEntityType; ","
       Print #fileNo, addTab(4); "L."; g_anLdmIsGen; ","
       Print #fileNo, addTab(4); "L."; g_anLdmIsNl
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"

       If extended Then
         Print #fileNo, addTab(3); "LEFT OUTER JOIN"
         Print #fileNo, addTab(4); "V_ExtraEntities V"
         Print #fileNo, addTab(3); "ON"
         Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " = V.entityId"
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = V.entityType"
       End If

       Print #fileNo, addTab(3); "INNER JOIN"
       Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "("
       If extended Then
         Print #fileNo, addTab(5); "V.entityId IS NOT NULL"
         Print #fileNo, addTab(6); "OR"
       End If
       Print #fileNo, addTab(5); "A."; g_anAcmIsPsForming; " = "; gc_dbTrue
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
       Print #fileNo, addTab(2); ") PSE"
       Print #fileNo, addTab(1); ";"
     End If

     genProcSectionHeader fileNo, "update reference to root AggregationSlot"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "PS.MASASL_OID = (SELECT MAP.map2Oid FROM "; tempOidMapTabName; " MAP WHERE MAP.oid = PS.MASASL_OID)"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = v_psOidNew"
     Print #fileNo, addTab(1); ";"

     Print #fileNo,
     Print #fileNo, addTab(1); "SET psOidNew_out = v_psOidNew;"
     Print #fileNo, addTab(1); "SET lrtOid_out   = v_lrtOid;"

     If extended Then
       genProcSectionHeader fileNo, "commit (if required)", 1, True
       Print #fileNo, addTab(1); "IF commitEachTable_in = 1 THEN"
       Print #fileNo, addTab(2); "COMMIT;"
       Print #fileNo, addTab(1); "END IF;"
     End If

     If extended Then
       genSpLogProcExit fileNo, qualProcNamePsCp2Lrt, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
     Else
       genSpLogProcExit fileNo, qualProcNamePsCp2Lrt, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i

   Dim qualPsCpProcedureName As String
   Dim useGenWorkspaceParams As Boolean
   For i = 1 To IIf(generatePsCopyExtendedSupport, 3, 2)
     useGenWorkspaceParams = (i = 2 Or i = 3)
     extended = (i = 3)
     qualPsCpProcedureName = genQualProcName(g_sectionIndexAliasLrt, "PSCOPY" & IIf(extended, "_EXT", ""), ddlType, thisOrgIndex, thisPoolIndex)
     qualProcNamePsCp2Lrt = genQualProcName(g_sectionIndexAliasLrt, "PSCP2LRT" & IIf(extended, "_EXT", ""), ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader "SP for 'Copying ProductStructure'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualPsCpProcedureName
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
     genProcParm fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number"
     genProcParm fileNo, "IN", "psOidOld_in", g_dbtOid, True, "OID of the Product Structure to copy"
     If extended Then
       genProcParm fileNo, "IN", "useLoggingforLrtTabs_in", g_dbtBoolean, True, "if set to '0', logging is disabled for LRT-tables, otherwise enabled"
       genProcParm fileNo, "IN", "commitEachTable_in", g_dbtBoolean, True, "if set to '1' commit after each table"
     End If
     genProcParm fileNo, "OUT", "lrtOid_out", g_dbtLrtId, True, "ID of the LRT related to the copied Product Structure data"
     genProcParm fileNo, "OUT", "psOidNew_out", g_dbtOid, True, "OID of the new Product Structure"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being copied (sum over all tables)"

     If useGenWorkspaceParams Then
       genProcParm fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context"
       genProcParm fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace"
       genProcParm fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE"
     End If
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare variables", , True
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
     genVarDecl fileNo, "v_rowCount", "INTEGER", "NULL"
     genVarDecl fileNo, "v_autoPriceSetProductive", g_dbtBoolean, gc_dbFalse
     If Not useGenWorkspaceParams Then
       genVarDecl fileNo, "v_gwspError", "VARCHAR(256)", "NULL"
       genVarDecl fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL"
       genVarDecl fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL"
     End If
     genVarDecl fileNo, "v_currentTs", "TIMESTAMP", "NULL"
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare statement"
     genVarDecl fileNo, "v_stmnt", "STATEMENT"

     genDdlForTempOidMap fileNo, , True

     If extended Then
       If useGenWorkspaceParams Then
         genSpLogProcEnter fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out"
       Else
         genSpLogProcEnter fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
       End If
     Else
       If useGenWorkspaceParams Then
         genSpLogProcEnter fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out"
       Else
         genSpLogProcEnter fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
       End If
     End If

     genProcSectionHeader fileNo, "initialize output parameters"
     Print #fileNo, addTab(1); "SET rowCount_out    = 0;"
     If useGenWorkspaceParams Then
       Print #fileNo, addTab(1); "SET gwspError_out   = NULL;"
       Print #fileNo, addTab(1); "SET gwspInfo_out    = NULL;"
       Print #fileNo, addTab(1); "SET gwspWarning_out = NULL;"
     End If

     genProcSectionHeader fileNo, "initialize variables"
     Print #fileNo, addTab(1); "SET v_currentTs = CURRENT TIMESTAMP;"
     If extended Then
       Print #fileNo, addTab(1); "SET useLoggingforLrtTabs_in = COALESCE(useLoggingforLrtTabs_in, 1);"
       Print #fileNo, addTab(1); "SET commitEachTable_in      = COALESCE(commitEachTable_in     , 0);"
     End If

     genProcSectionHeader fileNo, "copy Product Structure data into LRT-tables"
     If extended Then
       Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNamePsCp2Lrt; "(?,?,?,?,?,?,?,?)' ;"
     Else
       Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNamePsCp2Lrt; "(?,?,?,?,?,?)' ;"
     End If
     Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE"
     Print #fileNo, addTab(2); "v_stmnt"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "lrtOid_out,"
     Print #fileNo, addTab(2); "psOidNew_out,"
     Print #fileNo, addTab(2); "rowCount_out"
     Print #fileNo, addTab(1); "USING"
     Print #fileNo, addTab(2); "cdUserId_in,"
     Print #fileNo, addTab(2); "trNumber_in,"
     If extended Then
       Print #fileNo, addTab(2); "psOidOld_in,"
       Print #fileNo, addTab(2); "useLoggingforLrtTabs_in,"
       Print #fileNo, addTab(2); "commitEachTable_in"
     Else
       Print #fileNo, addTab(2); "psOidOld_in"
     End If
     Print #fileNo, addTab(1); ";"

     Dim qualCommitProcedureName As String
     qualCommitProcedureName = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)

     genProcSectionHeader fileNo, "commit LRT"
     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualCommitProcedureName; "(?,?,0,0,?,?,?,?)';"
     Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE"
     Print #fileNo, addTab(2); "v_stmnt"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "rowCount_out,"

     If useGenWorkspaceParams Then
       Print #fileNo, addTab(2); "gwspError_out,"
       Print #fileNo, addTab(2); "gwspInfo_out,"
       Print #fileNo, addTab(2); "gwspWarning_out"
     Else
       Print #fileNo, addTab(2); "v_gwspError,"
       Print #fileNo, addTab(2); "v_gwspInfo,"
       Print #fileNo, addTab(2); "v_gwspWarning"
     End If

     Print #fileNo, addTab(1); "USING"
     Print #fileNo, addTab(2); "lrtOid_out,"
     Print #fileNo, addTab(2); "v_autoPriceSetProductive"
     Print #fileNo, addTab(1); ";"

     Dim qualTabNamePricePreferences As String
     qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, thisOrgIndex)

     Dim qualTabNameGeneralSettings As String
     qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

     genProcSectionHeader fileNo, "create new Price Preferences"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNamePricePreferences
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexPricePreferences, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 14

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conRebateValueCode, "25"
     setAttributeMapping transformation, 3, conRebateValueType, "0"
     setAttributeMapping transformation, 4, conCurrency, "'EUR'"
     setAttributeMapping transformation, 5, conCurrencyFactor, "1"
     setAttributeMapping transformation, 6, conVehicleTotalPriceCalculationId, "1"
     setAttributeMapping transformation, 7, conPsOid, "psOidNew_out"
     setAttributeMapping transformation, 8, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 9, conCreateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 10, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 11, conLastUpdateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 12, conVersionId, "1"
     setAttributeMapping transformation, 13, conPrimaryPriceTypeForTestId, CStr(gc_dfltPrimaryPriceTypeFactory)
     setAttributeMapping transformation, 14, conPriceSelectionForOverlapId, CStr(gc_dfltPriceSelectionForOverlapFactory)

     genTransformedAttrListForEntity g_classIndexPricePreferences, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomValueNonLrt Or edomDefaultValue

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "create new General Settings"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameGeneralSettings
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexGeneralSettings, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 7
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conPsOid, "psOidNew_out"
     setAttributeMapping transformation, 3, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conCreateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 5, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 6, conLastUpdateTimestamp, "v_currentTs"
     setAttributeMapping transformation, 7, conVersionId, "1"

     genTransformedAttrListForEntity g_classIndexGeneralSettings, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomValueNonLrt Or edomDefaultValue

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     If useGenWorkspaceParams Then
       genGenWorkspacesInWorkDataPoolsDdl fileNo, 1, ddlType, "psOidNew_out", "v_stmntTxt", "v_stmnt", "gwspError_out", "gwspInfo_out", "gwspWarning_out"
     Else
       genGenWorkspacesInWorkDataPoolsDdl fileNo, 1, ddlType, "psOidNew_out", "v_stmntTxt", "v_stmnt", "v_gwspError", "v_gwspInfo", "v_gwspWarning"
     End If

     ' # create VIEWs & DISPLAYSLOTs
     Dim qualTabNameView As String
     qualTabNameView = genQualTabNameByClassIndex(g_classIndexView, ddlType, thisOrgIndex, thisPoolIndex)
 
     Dim relIndexDisplaySlot As Integer
     relIndexDisplaySlot = getRelIndexByName(rxnDisplaySlot, rnDisplaySlot)
     Dim qualTabNameDisplaySlot As String
     qualTabNameDisplaySlot = genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, thisOrgIndex, thisPoolIndex)

     Dim qualProcNameRegStaticInit As String
     qualProcNameRegStaticInit = genQualProcName(g_sectionIndexMeta, spnRegStaticInit, ddlType)

     Dim cpAcmEntityType As AcmAttrContainerType
     Dim cpAcmEntityIndex As Integer
     Dim qualTabName As String
     For j = 1 To 2
       If j = 1 Then
         cpAcmEntityType = eactClass
         cpAcmEntityIndex = g_classIndexView
         genProcSectionHeader fileNo, "copy VIEWs"
       Else
         cpAcmEntityType = eactRelationship
         cpAcmEntityIndex = relIndexDisplaySlot
         genProcSectionHeader fileNo, "copy DISPLAYSLOTs"
       End If

       qualTabName = genQualTabNameByEntityIndex(cpAcmEntityIndex, cpAcmEntityType, ddlType, thisOrgIndex, thisPoolIndex)

       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualTabName
       Print #fileNo, addTab(1); "("

       genAttrListForEntity cpAcmEntityIndex, cpAcmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"

       initAttributeTransformation transformation, 9, , True, True, "S.", , , , , , , , , eacPsFormingOid Or eacOid

       setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
       setAttributeMapping transformation, 2, conPsOid, "psOidNew_out"
       setAttributeMapping transformation, 3, conCreateTimestamp, "v_currentTs"
       setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_currentTs"
       setAttributeMapping transformation, 5, conCreateUser, "cdUserId_in"
       setAttributeMapping transformation, 6, conUpdateUser, "cdUserId_in"
       setAttributeMapping transformation, 7, conVersionId, "1"
       setAttributeMapping transformation, 8, "VIW_OID", "(SELECT map2Oid FROM " & tempOidMapTabName & " WHERE oid = S.VIW_OID FETCH FIRST 1 ROW ONLY)"
       setAttributeMapping transformation, 9, "ESL_OID", "(SELECT map2Oid FROM " & tempOidMapTabName & " WHERE oid = S.ESL_OID FETCH FIRST 1 ROW ONLY)"
 
       genTransformedAttrListForEntity cpAcmEntityIndex, cpAcmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, False, edomListLrt
 
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabName; " S"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "S."; conPsOid; " = psOidOld_in"
       If (j = 1) Then
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "S."; g_anIsStandard; " = "; gc_dbTrue
       ElseIf (j = 2) Then
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "EXISTS (SELECT 1 FROM "; tempOidMapTabName; " M WHERE M.oid = S.VIW_OID)"
       End If
       Print #fileNo, addTab(1); ";"

       genProcSectionHeader fileNo, "count the number of affected rows"
       Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

       If j = 1 Then
         genProcSectionHeader fileNo, "update OID-mapping for table """ & tempOidMapTabName & """"
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); tempOidMapTabName
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); "oid,"
         Print #fileNo, addTab(2); "map2Oid"
         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "O."; g_anOid; ","
         Print #fileNo, addTab(2); "N."; g_anOid
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabName; " O"
         Print #fileNo, addTab(1); "INNER JOIN"
         Print #fileNo, addTab(2); qualTabName; " N"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "O."; g_anName; " = N."; g_anName; ""
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "O."; conPsOid; " = psOidOld_in"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "N."; conPsOid; " = psOidNew_out"
         Print #fileNo, addTab(1); ";"
       End If
     Next j

     ' ################################################################

     genProcSectionHeader fileNo, "create related DataPools"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameDataPool
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexDataPool, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 10

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 7, conAccessModeId, "pool.ID"
     setAttributeMapping transformation, 8, "DPOORG_OID", "org." & g_anOid
     setAttributeMapping transformation, 9, "DPSPST_OID", "psOidNew_out"
     setAttributeMapping transformation, 10, conPaiEntitlementGroupId, "CAST(NULL AS VARCHAR(1))"

     genTransformedAttrListForEntity g_classIndexDataPool, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameOrganization; " org,"
     Print #fileNo, addTab(2); g_qualTabNameDataPoolAccessMode; " pool,"
     Print #fileNo, addTab(2); g_qualTabNamePdmOrganization; " pOrg,"
     Print #fileNo, addTab(2); g_qualTabNamePdmDataPoolType; " pPool"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "org.oid = pOrg.ORGOID"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "pool.id = pPool.id"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "initialize PS-related data in table """ & g_qualTabNameRegistryStatic & """"
     Print #fileNo, addTab(1); "CALL "; qualProcNameRegStaticInit; "(NULL, psOidNew_out, NULL, v_rowCount);"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
     If extended Then
       Print #fileNo, addTab(1); "IF commitEachTable_in = 1 THEN"
       Print #fileNo, addTab(2); "COMMIT;"
       Print #fileNo, addTab(1); "END IF;"
     End If

     If extended Then
       If useGenWorkspaceParams Then
         genSpLogProcExit fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out"
       Else
         genSpLogProcExit fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "useLoggingforLrtTabs_in", "commitEachTable_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
       End If
     Else
       If useGenWorkspaceParams Then
         genSpLogProcExit fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out"
       Else
         genSpLogProcExit fileNo, qualPsCpProcedureName, ddlType, , "'cdUserId_in", "trNumber_in", "psOidOld_in", "lrtOid_out", "psOidNew_out", "rowCount_out"
       End If
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genOidMapSql( _
   ddlType As DdlTypeId, _
   ByRef colName As String, _
   ByRef qualTabName As String, _
   ByRef qualSeqNameOid As String, _
   ByRef lrtOidFilterStr As String, _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional ByRef psOidFilter As String = "" _
 )
   genProcSectionHeader fileNo, gc_sqlDelimLine2, indent + 1
   genProcSectionHeader fileNo, "determine new OIDs to be mapped related to column '" & colName & "'", indent + 1, True

   Print #fileNo, addTab(indent + 1); "DELETE FROM "; tempOidNewTabName; ";"
 
   Print #fileNo,
   Print #fileNo, addTab(indent + 1); "OPEN mapCursor;"
   Print #fileNo, addTab(indent + 1); "SET v_oid   = 0;"
   Print #fileNo, addTab(indent + 1); "SET v_atEnd = "; gc_dbFalse; ";"
   Print #fileNo, addTab(indent + 1); "FETCH mapCursor INTO v_oid, v_map2Oid;"
   Print #fileNo, addTab(indent + 1); "FOR recordLoop AS csr CURSOR FOR"
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); colName; " AS v_record_"; colName
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); qualTabName
   Print #fileNo, addTab(indent + 2); "WHERE"
   If lrtOidFilterStr <> "" Then
     Print #fileNo, addTab(indent + 3); g_anInLrt; " = "; lrtOidFilterStr
     Print #fileNo, addTab(indent + 4); "AND"
   End If
   If psOidFilter <> "" Then
     Print #fileNo, addTab(indent + 3); g_anPsOid; " = "; psOidFilter
     Print #fileNo, addTab(indent + 4); "AND"
   End If
   Print #fileNo, addTab(indent + 3); colName; " IS NOT NULL"
   Print #fileNo, addTab(indent + 2); "ORDER BY"
   Print #fileNo, addTab(indent + 3); colName
   Print #fileNo, addTab(indent + 1); "DO"

   Print #fileNo, addTab(indent + 2); "WHILE (v_atEnd = 0) AND (v_record_"; colName; " >= v_oid) DO"
   Print #fileNo, addTab(indent + 3); "FETCH mapCursor INTO v_oid, v_map2Oid;"
   Print #fileNo, addTab(indent + 2); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(indent + 2); "IF (v_atEnd = 1) OR (v_record_"; colName; " < v_oid) THEN"
   Print #fileNo, addTab(indent + 3); "INSERT INTO "; tempOidNewTabName; "(oid) VALUES(v_record_"; colName; ");"
   Print #fileNo, addTab(indent + 2); "END IF;"

   Print #fileNo, addTab(indent + 1); "END FOR;"
   Print #fileNo, addTab(indent + 1); "CLOSE mapCursor WITH RELEASE;"

   genProcSectionHeader fileNo, "add new OIDs to set of OIDs to be mapped", indent + 1
   Print #fileNo, addTab(indent + 1); "INSERT INTO"
   Print #fileNo, addTab(indent + 2); tempOidMapTabName
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "oid,"
   Print #fileNo, addTab(indent + 2); "map2Oid"
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 1); "WITH"
   Print #fileNo, addTab(indent + 2); "V"
   Print #fileNo, addTab(indent + 1); "AS"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT DISTINCT"
   Print #fileNo, addTab(indent + 3); "oid"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); tempOidNewTabName
   Print #fileNo, addTab(indent + 1); "),"

   Print #fileNo, addTab(indent + 2); "v_newOid"
   Print #fileNo, addTab(indent + 1); "AS"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); "V.oid"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); "V"
   Print #fileNo, addTab(indent + 2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(indent + 3); tempOidMapTabName; " M"
   Print #fileNo, addTab(indent + 2); "ON"
   Print #fileNo, addTab(indent + 3); "M.oid = V.oid"
   Print #fileNo, addTab(indent + 2); "WHERE"
   Print #fileNo, addTab(indent + 3); "M.oid IS NULL"
   Print #fileNo, addTab(indent + 1); ")"

   Print #fileNo, addTab(indent + 1); "SELECT"
   Print #fileNo, addTab(indent + 2); "oid,"
   Print #fileNo, addTab(indent + 2); "NEXTVAL FOR "; qualSeqNameOid
   Print #fileNo, addTab(indent + 1); "FROM"
   Print #fileNo, addTab(indent + 2); "v_newOid"
   Print #fileNo, addTab(indent + 1); ";"

   If lrtOidFilterStr <> "" Then
     genProcSectionHeader fileNo, "map OIDs in column '" & colName & "'", indent + 1
     Print #fileNo, addTab(indent + 1); "OPEN mapCursor;"
     Print #fileNo, addTab(indent + 1); "SET v_oid = 0;"
     Print #fileNo, addTab(indent + 1); "FOR recordLoop AS csr CURSOR FOR"
     Print #fileNo, addTab(indent + 2); "SELECT"
     Print #fileNo, addTab(indent + 3); colName; " AS v_record_"; colName
     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); qualTabName
     Print #fileNo, addTab(indent + 2); "WHERE"
     Print #fileNo, addTab(indent + 3); g_anInLrt; " = "; lrtOidFilterStr
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); colName; " IS NOT NULL"
     Print #fileNo, addTab(indent + 2); "ORDER BY"
     Print #fileNo, addTab(indent + 3); colName
     Print #fileNo, addTab(indent + 2); "FOR UPDATE OF"
     Print #fileNo, addTab(indent + 3); colName
     Print #fileNo, addTab(indent + 1); "DO"

     Print #fileNo, addTab(indent + 2); "WHILE v_oid < v_record_"; colName; " DO"
     Print #fileNo, addTab(indent + 3); "FETCH mapCursor INTO v_oid, v_map2Oid;"
     Print #fileNo, addTab(indent + 2); "END WHILE;"
     Print #fileNo,
     Print #fileNo, addTab(indent + 2); "UPDATE "; qualTabName; " SET "; colName; " = v_map2Oid WHERE CURRENT OF csr;"
     Print #fileNo, addTab(indent + 1); "END FOR;"
 
     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "CLOSE mapCursor WITH RELEASE;"
   End If
 End Sub
 
 
 Private Sub genPsCopySupportDdlForNlTable( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef entityName As String, _
   ByVal sectionIndex As Integer, _
   ByRef qualTabName As String, _
   ByRef qualNlTabName As String, _
   ByRef qualNlTabNameLrt As String, _
   ByRef qualProcName As String, _
   ByRef qualSeqNameOid As String, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forThisAttributeOnly As Integer = -1 _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer
   thisOrgIndex = g_primaryOrgIndex
   thisPoolIndex = g_workDataPoolIndex

   printSectionHeader "SP for copying records of table """ & qualNlTabName & """ (ACM-Class """ & g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """) into private table / includes OID-mapping", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "ID of the LRT corresponding to this transaction"
   genProcParm fileNo, "IN", "psOidOld_in", g_dbtOid, True, "OID of the Product Structure to copy"
   genProcParm fileNo, "IN", "psOidNew_in", g_dbtOid, True, "OID of the new Product Structure"
   genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
   genProcParm fileNo, "IN", "currentTs_in", "TIMESTAMP", True, "timestamp of this transaction"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "notFound", "02000"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_oid", g_dbtOid, "0"
   genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_map2Oid", g_dbtOid, "0"
   genVarDecl fileNo, "v_stmntText", "VARCHAR(200)", "'SELECT oid, map2Oid FROM " & tempOidMapTabName & " ORDER BY oid FOR READ ONLY'"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE mapCursor CURSOR FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_atEnd = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempOidMap fileNo, , , True

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out"

   genProcSectionHeader fileNo, "copy the 'public records' into 'private table'"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualNlTabNameLrt
   Print #fileNo, addTab(1); "("

   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, _
     fileNo, qualTabName, forThisAttributeOnly, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 8, , True, True

   setAttributeMapping transformation, 1, conLrtState, CStr(lrtStatusCreated)
   setAttributeMapping transformation, 2, conInLrt, "lrtOid_in"
   setAttributeMapping transformation, 3, conChangeComment, "CAST(NULL AS VARCHAR(1))"
   setAttributeMapping transformation, 4, conLrtComment, "CAST(NULL AS VARCHAR(1))"
   setAttributeMapping transformation, 5, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 6, conStatusId, CStr(statusWorkInProgress)
   setAttributeMapping transformation, 7, conPsOid, "psOidNew_in"
   setAttributeMapping transformation, 8, conVersionId, "1"

   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, forThisAttributeOnly, False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt
 
   Dim fkColToParent As String
     Dim k As Integer
     For k = 1 To transformation.oidDescriptors.numDescriptors
       If transformation.oidDescriptors.descriptors(k).colCat And eacFkOid Then
         fkColToParent = transformation.oidDescriptors.descriptors(k).colName
       End If
     Next k

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualNlTabName; " NL"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabName; " PAR"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "PAR."; conPsOid; " = psOidOld_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PAR."; g_anOid; " = NL."; fkColToParent
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"
 
   genProcSectionHeader fileNo, "prepare cursor for OID-mapping"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntText;"

     Dim i As Integer
     For i = 1 To transformation.oidDescriptors.numDescriptors
         If ((transformation.oidDescriptors.descriptors(i).colCat And (eacPsFormingOid Or eacOid Or eacAhOid Or eacFkExtPsCopyOid)) <> 0) Then
           genOidMapSql ddlType, transformation.oidDescriptors.descriptors(i).colName, qualNlTabNameLrt, qualSeqNameOid, "lrtOid_in", fileNo, 0
         End If
     Next i
     If i < transformation.oidDescriptors.numDescriptors Then
       Print #fileNo,
       Print #fileNo, addTab(0); "-- "; gc_sqlDelimLine2
     End If
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genPsCopySupportDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoNl As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional includeExtendedEntitySet As Boolean = False _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' PS-Copy is only supported at 'pool-level'
     Exit Sub
   End If
 
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim classIndex As Integer
   Dim useSurrogateKey As Boolean
   Dim dbAcmEntityType As String

   If acmEntityType = eactClass Then
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityTypeDescr = "ACM-Class"
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       classIndex = g_classes.descriptors(acmEntityIndex).classIndex
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
       dbAcmEntityType = gc_acmEntityTypeKeyClass
   ElseIf acmEntityType = eactRelationship Then
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityTypeDescr = "ACM-Relationship"
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyRel
       classIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
       useSurrogateKey = useSurrogateKeysForNMRelationships And (g_relationships.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or g_relationships.descriptors(acmEntityIndex).logLastChange)
   Else
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNamePub As String
   qualTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen)

   Dim qualTabNamePriv As String
   qualTabNamePriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcName As String

   ' ####################################################################################################################
   ' #    SP for copying records related to a given Product Structure to LRT-table(s) / includes mapping of OIDS
   ' ####################################################################################################################

   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , "PSCP2LRT")

   printSectionHeader "SP for copying records of table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """) into private tables / includes OID-mapping", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "ID of the LRT corresponding to this transaction"
   genProcParm fileNo, "IN", "psOidOld_in", g_dbtOid, True, "OID of the Product Structure to copy"
   genProcParm fileNo, "IN", "psOidNew_in", g_dbtOid, True, "OID of the new Product Structure"
   genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
   genProcParm fileNo, "IN", "currentTs_in", "TIMESTAMP", True, "timestamp of this transaction"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "notFound", "02000"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_oid", g_dbtOid, "0"
   genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_map2Oid", g_dbtOid, "0"
   genVarDecl fileNo, "v_stmntText", "VARCHAR(200)", "'SELECT oid, map2Oid FROM " & tempOidMapTabName & " ORDER BY oid FOR READ ONLY'"

   If Not forGen And Not lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     genVarDecl fileNo, "acRecordCount", "INTEGER", "0"
   End If
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE mapCursor CURSOR FOR v_stmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore ("; tempOidMapTabName; " already exists)"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_atEnd = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempOidMap fileNo, , , True

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out"

   genProcSectionHeader fileNo, "copy the 'public records' into 'private table'"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNamePriv
   Print #fileNo, addTab(1); "("

   genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   initAttributeTransformation transformation, 11, , True, True, , , , , , , , , , eacAnyOid

   setAttributeMapping transformation, 1, conLrtState, CStr(lrtStatusCreated)
   setAttributeMapping transformation, 2, conInLrt, "lrtOid_in"
   setAttributeMapping transformation, 3, conPsOid, "psOidNew_in"
   setAttributeMapping transformation, 4, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 5, conStatusId, CStr(statusWorkInProgress)
   setAttributeMapping transformation, 6, conLrtComment, "CAST(NULL AS VARCHAR(1))"
   setAttributeMapping transformation, 7, conCreateTimestamp, "currentTs_in"
   setAttributeMapping transformation, 8, conLastUpdateTimestamp, "currentTs_in"
   setAttributeMapping transformation, 9, conCreateUser, "cdUserId_in"
   setAttributeMapping transformation, 10, conUpdateUser, "cdUserId_in"
   setAttributeMapping transformation, 11, conVersionId, "1"
 
   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNamePub
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); conPsOid; " = psOidOld_in"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

   genProcSectionHeader fileNo, "prepare cursor for OID-mapping"
   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntText;"

   If forGen And useSurrogateKey Then
     genOidMapSql ddlType, genAttrName(entityShortName & "_" & g_surrogateKeyNameShort, ddlType), _
                  qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0
   End If
     Dim i As Integer
     For i = 1 To transformation.oidDescriptors.numDescriptors
 
         ' FIXME: Hard Coded Hack!!! remove this
         ' for certain FK-columns we are not allowed to map OIDs because we did not copy the target classes!
         ' FK : Property -> PropertyTemplate
         If transformation.oidDescriptors.descriptors(i).colName = "PTMHTP_OID" Then
           GoTo NextI
         End If

         If (transformation.oidDescriptors.descriptors(i).colCat And eacAnyOid) = 0 Then
           GoTo NextI
         End If
         If ((transformation.oidDescriptors.descriptors(i).colCat And (eacPsFormingOid Or eacOid Or eacAhOid Or eacFkExtPsCopyOid)) <> 0) Then
           genOidMapSql ddlType, transformation.oidDescriptors.descriptors(i).colName, qualTabNamePriv, qualSeqNameOid, "lrtOid_in", fileNo, 0
         End If
 NextI:
     Next i
     If i < transformation.oidDescriptors.numDescriptors Then
       Print #fileNo,
       Print #fileNo, addTab(0); "-- "; gc_sqlDelimLine2
     End If

     If transformation.nlAttrRefs.numDescriptors > 0 Then
       Dim tabColumns As EntityColumnDescriptors
       tabColumns = nullEntityColumnDescriptors

       qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True, "PSCP2LRT")

       Dim qualTabNameNlPub As String
       qualTabNameNlPub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True)

       Dim qualTabNameNlPriv As String
       qualTabNameNlPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , True)

       genPsCopySupportDdlForNlTable acmEntityIndex, acmEntityType, entityName, sectionIndex, qualTabNamePub, qualTabNameNlPub, qualTabNameNlPriv, _
         qualProcName, qualSeqNameOid, fileNoNl, ddlType, forGen
     End If

   If Not forGen And Not lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     ' we need to do this only once for the 'non-Gen-class'
     Print #fileNo,
     Print #fileNo, addTab(1); "SET acRecordCount ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "COUNT(*)"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anLrtOid; " = lrtOid_in"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anAcmOrParEntityId; " = '"; entityIdStr; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anAcmEntityType; " = '"; dbAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "OPID = "; CStr(lrtStatusCreated)
     Print #fileNo, addTab(1); ");"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF (acRecordCount = 0) THEN"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); g_anLrtOid; ","
     Print #fileNo, addTab(3); g_anAcmOrParEntityId; ","
     Print #fileNo, addTab(3); g_anAcmEntityType; ","
     Print #fileNo, addTab(3); g_anLrtOpId
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(2); "lrtOid_in,"
     Print #fileNo, addTab(2); "'"; entityIdStr; "',"
     Print #fileNo, addTab(2); "'"; dbAcmEntityType; "',"
     Print #fileNo, addTab(2); CStr(lrtStatusCreated)
     Print #fileNo, addTab(2); ");"
     Print #fileNo, addTab(1); "END IF;"
   End If

   genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "psOidOld_in", "psOidNew_in", "'cdUserId_in", "#currentTs_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 End Sub
 
 
 Sub genPsCopySupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoStep2 As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   If ddlType = edtPdm And (thisOrgIndex <> g_primaryOrgIndex) Then
     ' PS-Copy is only supported at for 'primary organization'
     Exit Sub
   End If

     If generatePsCopySupport And (g_classes.descriptors(classIndex).isPsForming Or g_classes.descriptors(classIndex).supportExtendedPsCopy) And g_classes.descriptors(classIndex).isUserTransactional Then
       genPsCopySupportDdlForEntity classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, fileNoStep2, ddlType, forGen
     End If
 End Sub
 
 Sub genPsCopySupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoStep2 As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   If ddlType = edtPdm And (thisOrgIndex <> g_primaryOrgIndex) Then
     ' PS-Copy is only supported at for 'primary organization'
     Exit Sub
   End If
 
     If generatePsCopySupport And (g_relationships.descriptors(thisRelIndex).isPsForming Or g_relationships.descriptors(thisRelIndex).supportExtendedPsCopy) And g_relationships.descriptors(thisRelIndex).isUserTransactional Then
       genPsCopySupportDdlForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, fileNoStep2, ddlType, forGen
     End If
 End Sub
 
 ' ### ENDIF IVK ###
