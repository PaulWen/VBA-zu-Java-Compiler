 Attribute VB_Name = "M81_PSCreate"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 2
 
 
 Sub genPsCreateSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim i As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtPdm And g_genLrtSupport Then
     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportLrt Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_orgs.descriptors(thisOrgIndex).isPrimary Then
             genPsCreateSupportDdlByPool thisOrgIndex, thisPoolIndex, edtPdm
           End If
          Next thisOrgIndex
        End If
      Next thisPoolIndex
   End If
 End Sub
 
 
 Private Sub genPsCreateSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' PS-Create is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)
 
   Dim attrNameFkAggSlot As String
     attrNameFkAggSlot = genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexAggregationSlot).shortName)

   Dim masterAggSlotClassIdStr As String
   masterAggSlotClassIdStr = getClassIdStrByIndex(g_classIndexMasterAggregationSlot)

   Dim qualTabNameAggregationSlotPriv As String
   qualTabNameAggregationSlotPriv = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualTabNameAggregationSlotGenPriv As String
   qualTabNameAggregationSlotGenPriv = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True, True)

   Dim qualTabNameAggregationSlotGenNlPriv As String
   qualTabNameAggregationSlotGenNlPriv = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True, True, , True)

   Dim qualTabNameAggregationSlotNlPriv As String
   qualTabNameAggregationSlotNlPriv = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)

   Dim masterEndSlotClassIdStr As String
   masterEndSlotClassIdStr = getClassIdStrByIndex(g_classIndexMasterEndSlot)

   Dim qualTabNameEndSlotPriv As String
   qualTabNameEndSlotPriv = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, , True)

   Dim qualTabNameEndSlotGenPriv As String
   qualTabNameEndSlotGenPriv = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True, True)

   Dim qualTabNameEndSlotGenNlPriv As String
   qualTabNameEndSlotGenNlPriv = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True, True, , True)

   Dim qualTabNameEndSlotNlPriv As String
   qualTabNameEndSlotNlPriv = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)

   Dim categoryShortName As String
   Dim categoryClassIdStr As String
     categoryShortName = g_classes.descriptors(g_classIndexCategory).shortName
     categoryClassIdStr = g_classes.descriptors(g_classIndexCategory).classIdStr

   Dim qualTabNameCategoryPriv As String
   qualTabNameCategoryPriv = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, False, True)

   Dim qualTabNameCategoryGenPriv As String
   qualTabNameCategoryGenPriv = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, True, True)

   Dim qualTabNameCategoryGenNlPriv As String
   qualTabNameCategoryGenNlPriv = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, True, True, , True)

   Dim qualTabNameCategoryNlPriv As String
   qualTabNameCategoryNlPriv = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)

   Dim qualTabNameGenericCodePub As String
   qualTabNameGenericCodePub = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, False)

   Dim qualTabNameGenericCodePriv As String
   qualTabNameGenericCodePriv = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, True)

   Dim standardCodeClassIdStr As String
   standardCodeClassIdStr = g_classes.descriptors(g_classIndexStandardCode).classIdStr

   Dim qualTabNameCodeCategoryPriv As String
   qualTabNameCodeCategoryPriv = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualProcNamePsCreate As String
   qualProcNamePsCreate = genQualProcName(g_sectionIndexAliasLrt, "PsCreate", ddlType, thisOrgIndex, thisPoolIndex)

   Dim useGenWorkspaceParams As Boolean
   Dim i As Integer
   For i = 1 To 2
     useGenWorkspaceParams = (i = 2)

     printSectionHeader "SP for 'Creating ProductStructure'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNamePsCreate
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
     genProcParm fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number"
     genProcParm fileNo, "IN", "languageId1_in", g_dbtEnumId, True, "ID of the language for first set of labels"
     genProcParm fileNo, "IN", "languageId2_in", g_dbtEnumId, True, "ID of the language for second set of labels"
     genProcParm fileNo, "IN", "psLabel1_in", "VARCHAR(225)", True, "(NL-) label of new ProductStructure (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "psLabel2_in", "VARCHAR(225)", True, "(NL-) label of new ProductStructure (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "mainAggSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of main AggregationSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "mainAggSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of main AggregationSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "tempCatLabel1_in", "VARCHAR(225)", True, "(NL-) label of temporary Category (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "tempCatLabel2_in", "VARCHAR(225)", True, "(NL-) label of temporary Category (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "dupCatLabel1_in", "VARCHAR(225)", True, "(NL-) label of duplicating Category (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "dupCatLabel2_in", "VARCHAR(225)", True, "(NL-) label of duplicating Category (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "tempEndSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of temporary EndSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "tempEndSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of temporary EndSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "dupEndSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of duplicating EndSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "dupEndSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of duplicating EndSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "lrtComment_in", g_dbtChangeComment, True, "LRT comment related to this transaction"
     genProcParm fileNo, "IN", "psStartTime_in", "DATE", True, "date when this Product Structure first is valid"
     genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "identifies the division that the Product Structure corresponds to"
     genProcParm fileNo, "IN", "paintHandlingModeId_in", g_dbtEnumId, True, "paint handling mode used for the new Product Structure"
     genProcParm fileNo, "IN", "dupCodeNumber_in", g_dbtCodeNumber, True, "'dup' code number ('DUP0')"
     genProcParm fileNo, "IN", "dupCodeType_in", "CHAR(1)", True, "'dup' code type ('0')"
     genProcParm fileNo, "IN", "defaultCodeGroupKey_in", "VARCHAR(2)", True, "DEPRECATED - formerly: default code group key - if it is required to create it"
     genProcParm fileNo, "IN", "priceLogic_in", g_dbtEnumId, True, "'price logic' of the Product Structure"
     genProcParm fileNo, "IN", "type_in", g_dbtEnumId, True, "'type' of the Product Structure"

     genProcParm fileNo, "OUT", "lrtOid_out", g_dbtLrtId, True, "ID of the LRT related to the created Product Structure data"
     genProcParm fileNo, "OUT", "psOidNew_out", g_dbtOid, True, "OID of the new Product Structure"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being created (sum over all tables)"

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
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
     genVarDecl fileNo, "v_createProdTs", "TIMESTAMP", "NULL"
     genVarDecl fileNo, "v_lrtOid", g_dbtOid, "0"
     genVarDecl fileNo, "v_isCentralDataTransfer", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_psOidNew", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_rootAggSlotOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_rootAggSlotGenOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_tempEndSlotOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_tempEndSlotGenOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_dupEndSlotOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_dupEndSlotGenOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_tempCategoryOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_tempCategoryGenOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_dupCategoryOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_dupCategoryGenOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_codeOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_codeTypeOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_codeCategoryOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_validityBegin", "DATE", "NULL"
     genVarDecl fileNo, "v_validityEnd", "DATE", "NULL"
     genVarDecl fileNo, "v_autoPriceSetProductive", g_dbtBoolean, gc_dbFalse
     If Not useGenWorkspaceParams Then
       genVarDecl fileNo, "v_gwspError", "VARCHAR(256)", "NULL"
       genVarDecl fileNo, "v_gwspInfo", "VARCHAR(1024)", "NULL"
       genVarDecl fileNo, "v_gwspWarning", "VARCHAR(512)", "NULL"
     End If
     genSpLogDecl fileNo

     genSpLogProcEnter fileNo, qualProcNamePsCreate, ddlType, , "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out"

     genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1

     genProcSectionHeader fileNo, "set defaults if no values provided"
     Print #fileNo, addTab(1); "IF languageId1_in         IS NULL THEN SET languageId1_in         = 1                                ; END IF;"
     Print #fileNo, addTab(1); "IF languageId2_in         IS NULL THEN SET languageId2_in         = 2                                ; END IF;"
     Print #fileNo, addTab(1); "IF psLabel1_in            IS NULL THEN SET psLabel1_in            = 'Bezeichnung der Productstruktur'; END IF;"
     Print #fileNo, addTab(1); "IF psLabel2_in            IS NULL THEN SET psLabel2_in            = 'label of productstructure'      ; END IF;"
     Print #fileNo, addTab(1); "IF mainAggSlotLabel1_in   IS NULL THEN SET mainAggSlotLabel1_in   = 'Wurzel-Aggregationsslot'        ; END IF;"
     Print #fileNo, addTab(1); "IF mainAggSlotLabel2_in   IS NULL THEN SET mainAggSlotLabel2_in   = 'root aggregatuionslot'          ; END IF;"
     Print #fileNo, addTab(1); "IF tempCatLabel1_in       IS NULL THEN SET tempCatLabel1_in       = 'temporaräre Kategorie'          ; END IF;"
     Print #fileNo, addTab(1); "IF tempCatLabel2_in       IS NULL THEN SET tempCatLabel2_in       = 'temporary category'             ; END IF;"
     Print #fileNo, addTab(1); "IF dupCatLabel1_in        IS NULL THEN SET dupCatLabel1_in        = 'Duplikatskategorie'             ; END IF;"
     Print #fileNo, addTab(1); "IF dupCatLabel2_in        IS NULL THEN SET dupCatLabel2_in        = 'duplicating category'           ; END IF;"
     Print #fileNo, addTab(1); "IF tempEndSlotLabel1_in   IS NULL THEN SET tempEndSlotLabel1_in   = 'tempoärer Endslot'              ; END IF;"
     Print #fileNo, addTab(1); "IF tempEndSlotLabel2_in   IS NULL THEN SET tempEndSlotLabel2_in   = 'temporary endslot'              ; END IF;"
     Print #fileNo, addTab(1); "IF dupEndSlotLabel1_in    IS NULL THEN SET dupEndSlotLabel1_in    = 'Duplikats-Endslot'              ; END IF;"
     Print #fileNo, addTab(1); "IF dupEndSlotLabel2_in    IS NULL THEN SET dupEndSlotLabel2_in    = 'duplicating endslot'            ; END IF;"
     Print #fileNo, addTab(1); "IF lrtComment_in          IS NULL THEN SET lrtComment_in          = 'no LRT comment'                 ; END IF;"
     Print #fileNo, addTab(1); "IF psStartTime_in         IS NULL THEN SET psStartTime_in         = CURRENT DATE                     ; END IF;"
     Print #fileNo, addTab(1); "IF paintHandlingModeId_in IS NULL THEN SET paintHandlingModeId_in = 1                                ; END IF;"
     Print #fileNo, addTab(1); "IF dupCodeNumber_in       IS NULL THEN SET dupCodeNumber_in       = 'DUP0'                           ; END IF;"
     Print #fileNo, addTab(1); "IF dupCodeType_in         IS NULL THEN SET dupCodeType_in         = 'D'                              ; END IF;"

     genProcSectionHeader fileNo, "generate OIDs"
     Print #fileNo, addTab(1); "SET v_createProdTs       = CURRENT TIMESTAMP;"
     Print #fileNo, addTab(1); "SET v_rootAggSlotOid     = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_rootAggSlotGenOid  = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_tempEndSlotOid     = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_tempEndSlotGenOid  = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_dupEndSlotOid      = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_dupEndSlotGenOid   = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_tempCategoryOid    = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_tempCategoryGenOid = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_dupCategoryOid     = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_dupCategoryGenOid  = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "SET v_codeCategoryOid    = NEXTVAL FOR "; qualSeqNameOid; ";"

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

     genProcSectionHeader fileNo, "initialize output parameters"
     Print #fileNo, addTab(1); "SET rowCount_out    = 0;"
     Print #fileNo, addTab(1); "SET lrtOid_out      = CAST(NULL AS "; g_dbtOid; ");"
     Print #fileNo, addTab(1); "SET psOidNew_out    = CAST(NULL AS "; g_dbtOid; ");"

     If useGenWorkspaceParams Then
       Print #fileNo, addTab(1); "SET gwspError_out   = NULL;"
       Print #fileNo, addTab(1); "SET gwspInfo_out    = NULL;"
       Print #fileNo, addTab(1); "SET gwspWarning_out = NULL;"
     End If

     Print #fileNo,
     Print #fileNo, addTab(1); "SET v_rowCount = 0;"

     genProcSectionHeader fileNo, "validity of created objects starts with psStartTime_in - if provided - otherwise with beginning of current month"
     Print #fileNo, addTab(1); "SET v_validityBegin = COALESCE(psStartTime_in, CURRENT DATE - (DAY(CURRENT DATE) - 1) DAYS);"
     genProcSectionHeader fileNo, "validity of created objects lasts 'for ever'"
     Print #fileNo, addTab(1); "SET v_validityEnd = DATE("; gc_valDateInfinite; ");"

     genProcSectionHeader fileNo, "create new ProductStructure"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "("

     Dim transformation As AttributeListTransformation
     initAttributeTransformation transformation, 0

     genTransformedAttrListForEntity g_classIndexProductStructure, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 14

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "v_psOidNew"
     setAttributeMapping transformation, 7, conIsUnderConstruction, gc_dbTrue
     setAttributeMapping transformation, 8, conPaintHandlingModeId, "paintHandlingModeId_in"
     setAttributeMapping transformation, 9, "PDIDIV_OID", "divisionOid_in"
     setAttributeMapping transformation, 10, conComment, "psLabel1_in"
     setAttributeMapping transformation, 11, "MASASL_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 12, conPriceLogicId, "priceLogic_in"
     setAttributeMapping transformation, 13, conTypeId, "type_in"
     setAttributeMapping transformation, 14, "ISTIREVALIDITY", gc_dbFalse

     genTransformedAttrListForEntity g_classIndexProductStructure, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "label of ProductStructure"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameProductStructureNl
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexProductStructure, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- label / language 1"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 5

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
       setAttributeMapping transformation, 2, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexProductStructure).shortName), "v_psOidNew"
     setAttributeMapping transformation, 3, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 4, conLabel, "psLabel1_in"
     setAttributeMapping transformation, 5, conVersionId, "1"

     genNlsTransformedAttrListForEntity g_classIndexProductStructure, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 3, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 4, conLabel, "psLabel2_in"

     genNlsTransformedAttrListForEntity g_classIndexProductStructure, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     Dim qualProcNameLrtBegin As String
     qualProcNameLrtBegin = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)

     genProcSectionHeader fileNo, "begin a new LRT"

     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameLrtBegin; "(?,?,?,?,?)';"
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

     genProcSectionHeader fileNo, "one row created in LRT-table"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + 1;"

     genProcSectionHeader fileNo, "create related DataPools"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); g_qualTabNameDataPool
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexDataPool, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 10

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 7, conAccessModeId, "pool.ID"
     setAttributeMapping transformation, 8, "DPOORG_OID", "org." & g_anOid
     setAttributeMapping transformation, 9, "DPSPST_OID", "v_psOidNew"
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

     ' for LRT-tables we count the number of affected rows via LRTCOMMIT

     genProcSectionHeader fileNo, "create root AggregationSlot"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameAggregationSlotPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexAggregationSlot, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 20

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "v_rootAggSlotOid"
     setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 10, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 11, conClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 12, "ASPPAR_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 13, conCardinality, "1"
     setAttributeMapping transformation, 14, conSlotIndex, "CAST(NULL AS SMALLINT)"
     setAttributeMapping transformation, 15, "SARASL_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 16, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 17, conAhClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 18, conAhOId, "v_rootAggSlotOid"
     setAttributeMapping transformation, 19, conDisplayOrder, "1"
     setAttributeMapping transformation, 20, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_classIndexAggregationSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "lrtComment of root AggregationSlot"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameAggregationSlotNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexAggregationSlot, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, , True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- changeComment / language 1"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 12

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 5, attrNameFkAggSlot, "v_rootAggSlotOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 8, conVersionId, "1"
     setAttributeMapping transformation, 9, conAhClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 10, conAhOId, "v_rootAggSlotOid"
     setAttributeMapping transformation, 11, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 12, conHasBeenSetProductive, gc_dbFalse
 
     genNlsTransformedAttrListForEntity g_classIndexAggregationSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "set main AggregationSlot at new ProductStructure"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "MASASL_OID = v_rootAggSlotOid"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = v_psOidNew;"

     genProcSectionHeader fileNo, "create GEN-part of root AggregationSlot"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameAggregationSlotGenPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexAggregationSlot, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 18

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, attrNameFkAggSlot, "v_rootAggSlotOid"
     setAttributeMapping transformation, 7, conOid, "v_rootAggSlotGenOid"
     setAttributeMapping transformation, 8, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 9, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 10, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 11, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 12, conClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 13, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 14, conValidFrom, "v_validityBegin"
     setAttributeMapping transformation, 15, conValidTo, "v_validityEnd"
     setAttributeMapping transformation, 16, conAhClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 17, conAhOId, "v_rootAggSlotOid"
     setAttributeMapping transformation, 18, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_classIndexAggregationSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "label of root AggregationSlot"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameAggregationSlotGenNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexAggregationSlot, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- label / language 1"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 15

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 5, attrNameFkAggSlot, "v_rootAggSlotGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conLabel, "mainAggSlotLabel1_in"
     setAttributeMapping transformation, 8, conLabelNational, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 9, conLabelIsNatActive, gc_dbFalse
     setAttributeMapping transformation, 10, conVersionId, "1"
     setAttributeMapping transformation, 11, conAhClassId, "'" & masterAggSlotClassIdStr & "'"
     setAttributeMapping transformation, 12, conAhOId, "v_rootAggSlotOid"
     setAttributeMapping transformation, 13, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 14, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 15, conPsOid, "v_psOidNew"

     genNlsTransformedAttrListForEntity g_classIndexAggregationSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 7, conLabel, "mainAggSlotLabel2_in"
     setAttributeMapping transformation, 13, conChangeComment, "CAST(NULL AS VARCHAR(1))"

     genNlsTransformedAttrListForEntity g_classIndexAggregationSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "create temporary and duplicating Category"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCategoryPriv
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 1
     setAttributeMapping transformation, 1, conCategoryKindId, ""
 
     genTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- temporary Category"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 19

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "v_tempCategoryOid"
     setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 10, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 11, conIsDuplicating, gc_dbFalse
     setAttributeMapping transformation, 12, conIsDefault, gc_dbTrue
     setAttributeMapping transformation, 13, conDpClassNumber, "-1"
     setAttributeMapping transformation, 14, conClassId, "'" & categoryClassIdStr & "'"
     setAttributeMapping transformation, 15, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 16, conAhClassId, "'" & categoryClassIdStr & "'"
     setAttributeMapping transformation, 17, conAhOId, "v_tempCategoryOid"
     setAttributeMapping transformation, 18, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 19, conCategoryKindId, ""
 
     genTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- duplicating Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conOid, "v_dupCategoryOid"
     setAttributeMapping transformation, 11, conIsDuplicating, gc_dbTrue
     setAttributeMapping transformation, 12, conIsDefault, gc_dbFalse
     setAttributeMapping transformation, 13, conDpClassNumber, "9999"
     setAttributeMapping transformation, 17, conAhOId, "v_dupCategoryOid"

     genTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "lrtComment of temporary and duplicating Category"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCategoryNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexCategory, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, , True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- changeComment / language 1 for temporary Category"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 12

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, categoryShortName), "v_tempCategoryGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 8, conVersionId, "1"
     setAttributeMapping transformation, 9, conAhClassId, "'" & categoryClassIdStr & "'"
     setAttributeMapping transformation, 10, conAhOId, "v_tempCategoryOid"
     setAttributeMapping transformation, 11, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 12, conPsOid, "v_psOidNew"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- changeComment / language 1 for duplicating Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, categoryShortName), "v_dupCategoryGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 10, conAhOId, "v_dupCategoryOid"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "create GEN-parts of temporary and duplicating Category"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCategoryGenPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexCategory, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- temporary Category"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 17

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, genSurrogateKeyName(ddlType, categoryShortName), "v_tempCategoryOid"
     setAttributeMapping transformation, 7, conOid, "v_tempCategoryGenOid"
     setAttributeMapping transformation, 8, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 9, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 10, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 11, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 12, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 13, conValidFrom, "v_validityBegin"
     setAttributeMapping transformation, 14, conValidTo, "v_validityEnd"
     setAttributeMapping transformation, 15, conAhClassId, "'" & categoryClassIdStr & "'"
     setAttributeMapping transformation, 16, conAhOId, "v_tempCategoryOid"
     setAttributeMapping transformation, 17, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- duplicating Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, genSurrogateKeyName(ddlType, categoryShortName), "v_dupCategoryOid"
     setAttributeMapping transformation, 7, conOid, "v_dupCategoryGenOid"
     setAttributeMapping transformation, 16, conAhOId, "v_dupCategoryOid"

     genTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "labels of temporary and duplicating Category"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCategoryGenNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexCategory, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- label / language 1 for temporary Category"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 15

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, categoryShortName), "v_tempCategoryGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conLabel, "tempCatLabel1_in"
     setAttributeMapping transformation, 8, conLabelNational, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 9, conLabelIsNatActive, gc_dbFalse
     setAttributeMapping transformation, 10, conVersionId, "1"
     setAttributeMapping transformation, 11, conAhClassId, "'" & categoryClassIdStr & "'"
     setAttributeMapping transformation, 12, conAhOId, "v_tempCategoryOid"
     setAttributeMapping transformation, 13, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 14, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 15, conPsOid, "v_psOidNew"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2 for temporary Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 7, conLabel, "tempCatLabel2_in"
     setAttributeMapping transformation, 13, conChangeComment, "CAST(NULL AS VARCHAR(1))"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 1 for duplicating Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, categoryShortName), "v_dupCategoryGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conLabel, "dupCatLabel1_in"
     setAttributeMapping transformation, 12, conAhOId, "v_dupCategoryOid"
     setAttributeMapping transformation, 13, conChangeComment, "lrtComment_in"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2 for duplicating Category"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 7, conLabel, "dupCatLabel2_in"
     setAttributeMapping transformation, 13, conChangeComment, "CAST(NULL AS VARCHAR(1))"

     genNlsTransformedAttrListForEntity g_classIndexCategory, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "create temporary and duplicating EndSlot"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameEndSlotPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexEndSlot, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- temporary EndSlot"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 25

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conOid, "v_tempEndSlotOid"
     setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 10, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 11, conClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 12, "ESCESC_OID", "v_tempCategoryOid"
     setAttributeMapping transformation, 13, "LNKRBC_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 14, conSr0Order, "CAST(NULL AS SMALLINT)"
     setAttributeMapping transformation, 15, conSr1Order, "CAST(NULL AS SMALLINT)"
     setAttributeMapping transformation, 16, conNsr1Order, "CAST(NULL AS SMALLINT)"
     setAttributeMapping transformation, 17, conIsDuplicating, gc_dbFalse
     setAttributeMapping transformation, 18, "ESRASL_OID", "v_rootAggSlotOid"
     setAttributeMapping transformation, 19, conSlotIndex, "CAST(NULL AS SMALLINT)"
     setAttributeMapping transformation, 20, "SERESL_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 21, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 22, conAhClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 23, conAhOId, "v_tempEndSlotOid"
     setAttributeMapping transformation, 24, conDisplayOrder, "1"
     setAttributeMapping transformation, 25, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- duplicating EndSlot"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conOid, "v_dupEndSlotOid"
     setAttributeMapping transformation, 12, "ESCESC_OID", "v_dupCategoryOid"
     setAttributeMapping transformation, 17, conIsDuplicating, gc_dbTrue
     setAttributeMapping transformation, 23, conAhOId, "v_dupEndSlotOid"

     genTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "changeComment of temporary and duplicating EndSlot"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameEndSlotNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexEndSlot, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, , True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- changeComment / language 1 for temporary EndSlot"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 12

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
       setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_tempEndSlotGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 8, conVersionId, "1"
     setAttributeMapping transformation, 9, conAhClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 10, conAhOId, "v_tempEndSlotOid"
     setAttributeMapping transformation, 11, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 12, conPsOid, "v_psOidNew"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- changeComment / language 1 of duplicating EndSlot"
     Print #fileNo, addTab(1); "("

       setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_dupEndSlotGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conChangeComment, "lrtComment_in"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "create GEN-part of temporary and duplicating EndSlot"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameEndSlotGenPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_classIndexEndSlot, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- temporary EndSlot"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 33

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
       setAttributeMapping transformation, 6, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_tempEndSlotOid"
     setAttributeMapping transformation, 7, conOid, "v_tempEndSlotGenOid"
     setAttributeMapping transformation, 8, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 9, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 10, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 11, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 12, conClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 13, conSlotTypeId, "2"
     setAttributeMapping transformation, 14, conIsLinked, gc_dbFalse
     setAttributeMapping transformation, 15, conIsBaseSlot, gc_dbFalse
     setAttributeMapping transformation, 16, conAssignedPaintZoneKey, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 17, conIsSr0Slot, gc_dbFalse
     setAttributeMapping transformation, 18, conIsSr1Slot, gc_dbFalse
     setAttributeMapping transformation, 19, conIsNsr1Slot, gc_dbFalse
     setAttributeMapping transformation, 20, conIsRequired, gc_dbFalse
     setAttributeMapping transformation, 21, conIsViewForming, gc_dbFalse
     setAttributeMapping transformation, 22, conIsCabin, gc_dbFalse
     setAttributeMapping transformation, 23, conIsOrderField1, gc_dbFalse
     setAttributeMapping transformation, 24, conIsOrderField2, gc_dbFalse
     setAttributeMapping transformation, 25, conIsOrderField3, gc_dbFalse
     setAttributeMapping transformation, 26, conIsOrderField4, gc_dbFalse
     setAttributeMapping transformation, 27, conIsOrderField5, gc_dbFalse
     setAttributeMapping transformation, 28, conPsOid, "v_psOidNew"
     setAttributeMapping transformation, 29, conValidFrom, "v_validityBegin"
     setAttributeMapping transformation, 30, conValidTo, "v_validityEnd"
     setAttributeMapping transformation, 31, conAhClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 32, conAhOId, "v_tempEndSlotOid"
     setAttributeMapping transformation, 33, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- duplicating EndSlot"
     Print #fileNo, addTab(1); "("

       setAttributeMapping transformation, 6, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_dupEndSlotOid"
     setAttributeMapping transformation, 7, conOid, "v_dupEndSlotGenOid"
     setAttributeMapping transformation, 32, conAhOId, "v_dupEndSlotOid"

     genTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "labels of temporary and duplicating EndSlot"

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameEndSlotGenNlPriv
     Print #fileNo, addTab(1); "("

     genNlsAttrDeclsForEntity g_classIndexEndSlot, eactClass, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "-- label / language 1 for temporary EndSlot"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 15

     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
       setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_tempEndSlotGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conLabel, "tempEndSlotLabel1_in"
     setAttributeMapping transformation, 8, conLabelNational, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 9, conLabelIsNatActive, gc_dbFalse
     setAttributeMapping transformation, 10, conVersionId, "1"
     setAttributeMapping transformation, 11, conAhClassId, "'" & masterEndSlotClassIdStr & "'"
     setAttributeMapping transformation, 12, conAhOId, "v_tempEndSlotOid"
     setAttributeMapping transformation, 13, conChangeComment, "lrtComment_in"
     setAttributeMapping transformation, 14, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 15, conPsOid, "v_psOidNew"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2 for temporary EndSlot"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 7, conLabel, "tempEndSlotLabel2_in"
     setAttributeMapping transformation, 13, conChangeComment, "CAST(NULL AS VARCHAR(1))"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 1 of duplicating EndSlot"
     Print #fileNo, addTab(1); "("

       setAttributeMapping transformation, 5, genSurrogateKeyName(ddlType, g_classes.descriptors(g_classIndexEndSlot).shortName), "v_dupEndSlotGenOid"
     setAttributeMapping transformation, 6, conLanguageId, "languageId1_in"
     setAttributeMapping transformation, 7, conLabel, "dupEndSlotLabel1_in"
     setAttributeMapping transformation, 12, conAhOId, "v_dupEndSlotOid"
     setAttributeMapping transformation, 13, conChangeComment, "lrtComment_in"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); "),"
     Print #fileNo, addTab(1); "-- label / language 2 of duplicating EndSlot"
     Print #fileNo, addTab(1); "("

     setAttributeMapping transformation, 6, conLanguageId, "languageId2_in"
     setAttributeMapping transformation, 7, conLabel, "dupEndSlotLabel2_in"
     setAttributeMapping transformation, 13, conChangeComment, "CAST(NULL AS VARCHAR(1))"

     genNlsTransformedAttrListForEntity g_classIndexEndSlot, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, , edomListLrt

     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "check if Codenumber dupCodeNumber_in already exists"
     Print #fileNo, addTab(1); "SET v_codeOid ="
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); g_anOid
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameGenericCodePub
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); g_anCodeNumber; " = dupCodeNumber_in"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "CDIDIV_OID = divisionOid_in"
     Print #fileNo, addTab(2); ");"

     genProcSectionHeader fileNo, "if Codenumber dupCodeNumber_in does not exist, create it"
     Print #fileNo, addTab(1); "IF v_codeOid IS NULL THEN"
     Print #fileNo, addTab(2); "SET v_codeOid          = NEXTVAL FOR "; qualSeqNameOid; ";"
 
     genProcSectionHeader fileNo, "determine OID of CodeType used for dup StandardCode", 2
     Print #fileNo, addTab(2); "SET v_codeTypeOid = (SELECT "; g_anOid; " FROM "; g_qualTabNameCodeType; " WHERE CODETYPENUMBER = dupCodeType_in);"

     genProcSectionHeader fileNo, "create new dup StandardCode", 2
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameGenericCodePriv
     Print #fileNo, addTab(2); "("

     initAttributeTransformation transformation, 1
     setAttributeMapping transformation, 1, conCodeCharacterId, ""

     genTransformedAttrListForEntity g_classIndexGenericCode, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, False, edomListLrt

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(2); "("

     initAttributeTransformation transformation, 52

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"
     setAttributeMapping transformation, 6, conCodeNumber, "dupCodeNumber_in"
     setAttributeMapping transformation, 7, conComment, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 8, conIsAEF, gc_dbFalse
     setAttributeMapping transformation, 9, conCodePriority, "0"
     setAttributeMapping transformation, 10, conNotVisibleFactory, "''"
     setAttributeMapping transformation, 11, conNotVisibleNational, "''"
     setAttributeMapping transformation, 12, conHasConflict, gc_dbFalse
     setAttributeMapping transformation, 13, conIsNotPublished, gc_dbFalse
     setAttributeMapping transformation, 14, conIsBlockedFactory, gc_dbFalse
     setAttributeMapping transformation, 15, conIsBlockedNational, gc_dbFalse
     setAttributeMapping transformation, 16, conIsRebateEnabled, gc_dbFalse
     setAttributeMapping transformation, 17, conIsRebateEnabled & "_NATIONAL", gc_dbFalse
     setAttributeMapping transformation, 18, conIsRebateEnabled & "_ISNATACTIVE", gc_dbFalse
     setAttributeMapping transformation, 19, conIsCommissionDeductible, gc_dbFalse
     setAttributeMapping transformation, 20, conIsProductionRelevant, gc_dbFalse
     setAttributeMapping transformation, 21, conIsTaxRelevant, gc_dbFalse
     setAttributeMapping transformation, 22, conIsSideCosts, gc_dbFalse
     setAttributeMapping transformation, 23, conIsMotorVehicleCertificationRelevant, gc_dbFalse
     setAttributeMapping transformation, 24, conIsEstimationRelevant, gc_dbFalse
     setAttributeMapping transformation, 25, conPackageTypeId, "CAST(NULL AS " & g_dbtEnumId & ")"
     setAttributeMapping transformation, 26, conName, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 27, conContactPerson, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 28, conStreet, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 29, conZipCode, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 30, conCity, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 31, conState, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 32, conFax, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 33, conFon, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 34, conEMail, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 35, conAbhCode, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 36, conOid, "v_codeOid"
     setAttributeMapping transformation, 37, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 38, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 39, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 40, conLrtComment, "lrtComment_in"
     setAttributeMapping transformation, 41, conClassId, "'" & standardCodeClassIdStr & "'"
     setAttributeMapping transformation, 42, "CTLTLV_OID", "v_codeGrpLevel3Oid"
     setAttributeMapping transformation, 43, "CDIDIV_OID", "divisionOid_in"
     setAttributeMapping transformation, 44, "CTYTYP_OID", "v_codeTypeOid"
     setAttributeMapping transformation, 45, "ECDCDE_OID", "CAST(NULL AS " & g_dbtOid & ")"
     setAttributeMapping transformation, 46, conIsNational, gc_dbTrue
     setAttributeMapping transformation, 47, conValidFrom, gc_valDateEarliest
     setAttributeMapping transformation, 48, conValidTo, "v_validityEnd"
     setAttributeMapping transformation, 49, conAhClassId, "'" & standardCodeClassIdStr & "'"
     setAttributeMapping transformation, 50, conAhOId, "v_codeOid"
     setAttributeMapping transformation, 51, conHasBeenSetProductive, gc_dbFalse
     setAttributeMapping transformation, 52, conCodeCharacterId, ""

     genTransformedAttrListForEntity g_classIndexGenericCode, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, False, edomListLrt

     Print #fileNo, addTab(2); ");"

     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "associate duplicating Code with duplicating Category"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCodeCategoryPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_relIndexCodeCategory, eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("

     initAttributeTransformation transformation, 17

     setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
     setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
     setAttributeMapping transformation, 5, conVersionId, "1"

     setAttributeMapping transformation, 6, conOid, "v_codeCategoryOid"
     setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
     setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
     setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
     setAttributeMapping transformation, 10, conLrtComment, "lrtComment_in"

     setAttributeMapping transformation, 11, "GCO_OID", "v_codeOid"
     setAttributeMapping transformation, 12, "CAT_OID", "v_dupCategoryOid"
     setAttributeMapping transformation, 13, conPsOid, "v_psOidNew"

     setAttributeMapping transformation, 14, conAhClassId, "'" & standardCodeClassIdStr & "'"
     setAttributeMapping transformation, 15, conAhOId, "v_codeOid"
     setAttributeMapping transformation, 16, conDpClassNumber, "CAST(NULL AS SMALLINT)"

     setAttributeMapping transformation, 17, conHasBeenSetProductive, gc_dbFalse

     genTransformedAttrListForEntity g_relIndexCodeCategory, eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

     Print #fileNo, addTab(1); ");"

     ' #########################################################

     genProcSectionHeader fileNo, "associate all Code related to 'divisionOid_in' with temporay Category"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCodeCategoryPriv
     Print #fileNo, addTab(1); "("

     genAttrListForEntity g_relIndexCodeCategory, eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     setAttributeMapping transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 11, "GCO_OID", "C." & g_anOid
     setAttributeMapping transformation, 12, "CAT_OID", "v_tempCategoryOid"
     setAttributeMapping transformation, 15, conAhOId, "C." & g_anOid
     setAttributeMapping transformation, 16, conDpClassNumber, "CAST(NULL AS SMALLINT)"

     genTransformedAttrListForEntity g_relIndexCodeCategory, eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameGenericCodePub; " C"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "C.CDIDIV_OID = divisionOid_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "C."; g_anOid; " <> v_codeOid"
     Print #fileNo, addTab(1); ";"
 
     ' procedure too large :-(
     genPsCreateSupportDdlByPool2 fileNo, thisOrgIndex, thisPoolIndex, useGenWorkspaceParams, ddlType

     genSpLogProcExit fileNo, qualProcNamePsCreate, ddlType, , "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     printSectionHeader "SP for 'Creating ProductStructure'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNamePsCreate
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
     genProcParm fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number"
     genProcParm fileNo, "IN", "languageId1_in", g_dbtEnumId, True, "ID of the language for first set of labels"
     genProcParm fileNo, "IN", "languageId2_in", g_dbtEnumId, True, "ID of the language for second set of labels"
     genProcParm fileNo, "IN", "psLabel1_in", "VARCHAR(225)", True, "(NL-) label of new ProductStructure (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "psLabel2_in", "VARCHAR(225)", True, "(NL-) label of new ProductStructure (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "mainAggSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of main AggregationSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "mainAggSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of main AggregationSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "tempCatLabel1_in", "VARCHAR(225)", True, "(NL-) label of temporary Category (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "tempCatLabel2_in", "VARCHAR(225)", True, "(NL-) label of temporary Category (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "dupCatLabel1_in", "VARCHAR(225)", True, "(NL-) label of duplicating Category (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "dupCatLabel2_in", "VARCHAR(225)", True, "(NL-) label of duplicating Category (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "tempEndSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of temporary EndSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "tempEndSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of temporary EndSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "dupEndSlotLabel1_in", "VARCHAR(225)", True, "(NL-) label of duplicating EndSlot (corresponding to languageId1_in)"
     genProcParm fileNo, "IN", "dupEndSlotLabel2_in", "VARCHAR(225)", True, "(NL-) label of duplicating EndSlot (corresponding to languageId2_in)"
     genProcParm fileNo, "IN", "lrtComment_in", g_dbtChangeComment, True, "LRT comment related to this transaction"
     genProcParm fileNo, "IN", "psStartTime_in", "DATE", True, "date when this Product Structure first is valid"
     genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "identifies the division that the Product Structure corresponds to"
     genProcParm fileNo, "IN", "paintHandlingModeId_in", g_dbtEnumId, True, "paint handling mode used for the new Product Structure"
     genProcParm fileNo, "IN", "dupCodeNumber_in", g_dbtCodeNumber, True, "'dup' code number ('DUP0')"
     genProcParm fileNo, "IN", "dupCodeType_in", "CHAR(1)", True, "'dup' code type ('0')"
     genProcParm fileNo, "IN", "defaultCodeGroupKey_in", "VARCHAR(2)", True, "DEPRECATED - formerly: default code group key - if it is required to create it"

     genProcParm fileNo, "OUT", "lrtOid_out", g_dbtLrtId, True, "ID of the LRT related to the created Product Structure data"
     genProcParm fileNo, "OUT", "psOidNew_out", g_dbtOid, True, "OID of the new Product Structure"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", useGenWorkspaceParams, "number of rows being created (sum over all tables)"

     If useGenWorkspaceParams Then
       genProcParm fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context"
       genProcParm fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace"
       genProcParm fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE"
     End If

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genSpLogDecl fileNo, -1, True

     genSpLogProcEnter fileNo, qualProcNamePsCreate, ddlType, , "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out"
     Print #fileNo, addTab(1); "CALL "; qualProcNamePsCreate; "(cdUserId_in, trNumber_in, languageId1_in, languageId2_in, psLabel1_in, psLabel2_in,"
     Print #fileNo, addTab(4); "mainAggSlotLabel1_in, mainAggSlotLabel2_in, tempCatLabel1_in, tempCatLabel2_in, dupCatLabel1_in, dupCatLabel2_in,"
     Print #fileNo, addTab(4); "tempEndSlotLabel1_in, tempEndSlotLabel2_in, dupEndSlotLabel1_in, dupEndSlotLabel2_in, lrtComment_in, psStartTime_in,"
     Print #fileNo, addTab(4); "divisionOid_in, paintHandlingModeId_in, dupCodeNumber_in, dupCodeType_in, defaultCodeGroupKey_in, 1, 1, lrtOid_out,"
     Print #fileNo, addTab(4); "psOidNew_out, rowCount_out"; IIf(useGenWorkspaceParams, ", gwspError_out, gwspInfo_out, gwspWarning_out", ""); ");"

     genSpLogProcExit fileNo, qualProcNamePsCreate, ddlType, , "'cdUserId_in", "trNumber_in", "'...'", "lrtOid_out", "psOidNew_out", "rowCount_out"

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
 
 
 Private Sub genPsCreateSupportDdlByPool2( _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   useGenWorkspaceParams As Boolean, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameChangeLogNlText As String
   qualTabNameChangeLogNlText = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

   Dim qualTabNamePropertyLrt As String
   qualTabNamePropertyLrt = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, False, True)
   Dim qualTabNamePropertyGenLrt As String
   qualTabNamePropertyGenLrt = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, True)
   Dim qualTabNamePropertyGenNlTextLrt As String
   qualTabNamePropertyGenNlTextLrt = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True, True, , True)

   Dim transformation As AttributeListTransformation
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)
 
   genProcSectionHeader fileNo, "derive Properties from PropertyTemplates"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNamePropertyLrt
   Print #fileNo, addTab(1); "("
 
   genAttrListForEntity g_classIndexProperty, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, False, edomListLrt

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
 
   initAttributeTransformation transformation, 15, , , , "T."
 
   setAttributeMapping transformation, 1, conCreateUser, "cdUserId_in"
   setAttributeMapping transformation, 2, conCreateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
   setAttributeMapping transformation, 4, conLastUpdateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 5, conVersionId, "1"
 
   setAttributeMapping transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformation, 7, conInLrt, "v_lrtOid"
   setAttributeMapping transformation, 8, conStatusId, CStr(statusWorkInProgress), , , True
   setAttributeMapping transformation, 9, conLrtState, CStr(lrtStatusCreated)
 
   setAttributeMapping transformation, 10, "PSPPST_OID", "v_psOidNew"
   setAttributeMapping transformation, 11, "PTMHTP_OID", "T." & g_anOid
   setAttributeMapping transformation, 12, conPsOid, "v_psOidNew"
   setAttributeMapping transformation, 13, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 14, conAhOId, "-1"
   setAttributeMapping transformation, 15, conAhClassId, "T." & g_anCid

   genTransformedAttrListForEntity g_classIndexProperty, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, False, edomListLrt

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePropertyTemplate; " T"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
 
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabNamePropertyLrt
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); g_anAhOid; " = "; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anInLrt; " = v_lrtOid"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNamePropertyGenLrt
   Print #fileNo, addTab(1); "("
 
   initAttributeTransformation transformation, 22
   setAttributeMapping transformation, 1, conIsDeleted, ""
   setAttributeMapping transformation, 2, conMaxLength, ""
   setAttributeMapping transformation, 3, conDigitsAfterDecimalPoint, ""
   setAttributeMapping transformation, 4, conUnit, ""
   setAttributeMapping transformation, 5, conReturnPropertyFormatId, ""
   setAttributeMapping transformation, 6, conReturnUnit, ""

   transformation.numMappings = 6
   genTransformedAttrListForEntity g_classIndexProperty, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt
   transformation.numMappings = 22
 
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
 
   transformation.attributePrefix = "T."

   setAttributeMapping transformation, 7, conCreateUser, "cdUserId_in"
   setAttributeMapping transformation, 8, conCreateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 9, conUpdateUser, "cdUserId_in"
   setAttributeMapping transformation, 10, conLastUpdateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 11, conVersionId, "1"
 
   setAttributeMapping transformation, 12, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformation, 13, conInLrt, "v_lrtOid"
   setAttributeMapping transformation, 14, conStatusId, CStr(statusWorkInProgress), , , True
   setAttributeMapping transformation, 15, conLrtState, CStr(lrtStatusCreated)
 
   setAttributeMapping transformation, 16, conPsOid, "v_psOidNew"
   setAttributeMapping transformation, 17, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 18, conAhOId, "P." & g_anOid
   setAttributeMapping transformation, 19, conAhClassId, "T." & g_anCid
   setAttributeMapping transformation, 20, conValidFrom, "v_validityBegin"
   setAttributeMapping transformation, 21, conValidTo, "v_validityEnd"

   setAttributeMapping transformation, 22, "PRP_OID", "P." & g_anOid
 
   genTransformedAttrListForEntity g_classIndexProperty, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, True, edomListLrt
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePropertyTemplate; " T"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyLrt; " P"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "P.PTMHTP_OID = T."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "P."; g_anInLrt; " = v_lrtOid"

   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNamePropertyGenNlTextLrt
   Print #fileNo, addTab(1); "("
 
   genNlsAttrDeclsForEntity g_classIndexProperty, eactClass, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, edomListLrt

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   initAttributeTransformation transformation, 10, , , , "TNL."

   setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformation, 2, conInLrt, "v_lrtOid"
   setAttributeMapping transformation, 3, conStatusId, CStr(statusWorkInProgress), , , True
   setAttributeMapping transformation, 4, conLrtState, CStr(lrtStatusCreated)
 
   setAttributeMapping transformation, 5, conPsOid, "v_psOidNew"
   setAttributeMapping transformation, 6, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 7, conAhOId, "PGEN." & g_anAhOid
   setAttributeMapping transformation, 8, conAhClassId, "PGEN." & g_anAhCid
   setAttributeMapping transformation, 9, conVersionId, "1"

   setAttributeMapping transformation, 10, "PRP_OID", "PGEN." & g_anOid

   genNlsTransformedAttrListForEntity g_classIndexProperty, eactClass, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, True, True, True, edomListLrt

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePropertyTemplateNl; " TNL"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyLrt; " P"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "P.PTMHTP_OID = TNL.PRT_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNamePropertyGenLrt; " PGEN"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "P."; g_anOid; " = PGEN.PRP_OID"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "register entities as being affected by the LRT"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity
   Print #fileNo, addTab(1); "("

   genAttrListForEntity g_classIndexLrtAffectedEntity, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, False, edomListNonLrt
 
   Print #fileNo, addTab(1); ")"
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
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "A."; g_anAcmEntityId; " IN ("; _
                             "'"; g_classes.descriptors(g_classIndexAggregationSlot).classIdStr; "',"; _
                             "'"; g_classes.descriptors(g_classIndexEndSlot).classIdStr; "',"; _
                             "'"; g_classes.descriptors(g_classIndexCategory).classIdStr; "',"; _
                             "'"; g_classes.descriptors(g_classIndexProperty).classIdStr; "',"; _
                             "'"; g_classes.descriptors(g_classIndexGenericCode).classIdStr; "')"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(6); "OR"

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "A."; g_anAcmEntityId; " IN ("; "'"; g_relationships.descriptors(g_relIndexCodeCategory).relIdStr; "')"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); ")"

   Print #fileNo, addTab(5); "AND"

   Print #fileNo, addTab(4); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(2); ") PSE"
   Print #fileNo, addTab(1); ";"

   Dim qualCommitProcedureName As String
   qualCommitProcedureName = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)
 
   genProcSectionHeader fileNo, "commit LRT"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualCommitProcedureName; "(?,?,1,0,?,?,?,?)';"

   Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "EXECUTE"
   Print #fileNo, addTab(2); "v_stmnt"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_rowCount,"
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
   Print #fileNo, addTab(2); "v_lrtOid,"
   Print #fileNo, addTab(2); "v_autoPriceSetProductive"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader fileNo, "consider ChangeLog rows as affected rows"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + (SELECT COUNT(*) FROM "; qualTabNameChangeLog; " WHERE "; g_anPsOid; " = v_psOidNew);"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + (SELECT COUNT(*) FROM "; qualTabNameChangeLogNlText; " NL WHERE (SELECT "; g_anPsOid; " FROM "; qualTabNameChangeLog; " L WHERE L."; g_anOid; " = NL.CLG_OID) = v_psOidNew);"

   genPsRelatedCtoObjsDdl fileNo, thisOrgIndex, thisPoolIndex, ddlType, useGenWorkspaceParams

   genProcSectionHeader fileNo, "set output parameters"
   Print #fileNo, addTab(1); "SET psOidNew_out = v_psOidNew;"
   Print #fileNo, addTab(1); "SET lrtOid_out   = v_lrtOid;"
 End Sub
 
 
 Sub genGenWorkspacesInWorkDataPoolsDdl( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef varNamePsOid As String = "v_psOidNew", _
   Optional ByRef varNameStmntTxt As String = "v_stmntTxt", _
   Optional ByRef stmntName As String = "v_stmnt", _
   Optional ByRef varNameGwspError As String = "v_gwspError", _
   Optional ByRef varNameGwspErrorInfo As String = "v_gwspInfo", _
   Optional ByRef varNameGwspWarning As String = "v_gwspWarning" _
 )
   genProcSectionHeader fileNo, "create Solver-Files for new ProductStructure in all Work- and Productive data pools", indent
   Print #fileNo, addTab(indent + 0); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", indent + 1, True
   genVarDecl fileNo, "v_callCount", "INTEGER", "NULL", indent + 1
   genVarDecl fileNo, "v_accessModeId", g_dbtEnumId, "NULL", indent + 1

   Dim qualProcNameGenWorkspaceWrapper As String
 
   qualProcNameGenWorkspaceWrapper = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType)
 
   Print #fileNo,
   If varNameGwspWarning = "" Then
     Print #fileNo, addTab(indent + 1); "SET "; varNameStmntTxt; " = 'CALL "; qualProcNameGenWorkspaceWrapper; "(2,NULL,?,?,0,?)';"
   Else
     Print #fileNo, addTab(indent + 1); "SET "; varNameStmntTxt; " = 'CALL "; qualProcNameGenWorkspaceWrapper; "(2,NULL,?,?,0,?,?,?,?)';"
   End If
   Print #fileNo,
   Print #fileNo, addTab(indent + 1); "PREPARE "; stmntName; " FROM "; varNameStmntTxt; ";"

   Dim i As Integer
   For i = 1 To 2
     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "SET v_accessModeId = "; CStr(IIf(i = 1, g_workDataPoolId, g_productiveDataPoolId)); ";"
     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "EXECUTE"
     Print #fileNo, addTab(indent + 2); stmntName
     Print #fileNo, addTab(indent + 1); "INTO"

     If varNameGwspWarning = "" Then
       Print #fileNo, addTab(indent + 2); "v_callCount"
     Else
       Print #fileNo, addTab(indent + 2); "v_callCount,"
       Print #fileNo, addTab(indent + 2); varNameGwspError; ","
       Print #fileNo, addTab(indent + 2); varNameGwspErrorInfo; ","
       Print #fileNo, addTab(indent + 2); varNameGwspWarning
     End If

     Print #fileNo, addTab(indent + 1); "USING"
     Print #fileNo, addTab(indent + 2); "v_accessModeId,"
     Print #fileNo, addTab(indent + 2); varNamePsOid
     Print #fileNo, addTab(indent + 1); ";"
   Next i

   Print #fileNo, addTab(indent + 0); "END;"
 End Sub
 
 
 Sub genPsRelatedCtoObjsDdl( _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional useGenWorkspaceParams As Boolean = True _
 )
   Dim qualTabNamePricePreferences As String
   qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, thisOrgIndex)

   Dim qualTabNameGeneralSettings As String
   qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualProcNameRegStaticInit As String
   qualProcNameRegStaticInit = genQualProcName(g_sectionIndexMeta, spnRegStaticInit, ddlType)

   Dim transformation As AttributeListTransformation

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

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
   setAttributeMapping transformation, 7, conPsOid, "v_psOidNew"
   setAttributeMapping transformation, 8, conCreateUser, "cdUserId_in"
   setAttributeMapping transformation, 9, conCreateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 10, conUpdateUser, "cdUserId_in"
   setAttributeMapping transformation, 11, conLastUpdateTimestamp, "v_createProdTs"
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
   setAttributeMapping transformation, 2, conPsOid, "v_psOidNew"
   setAttributeMapping transformation, 3, conCreateUser, "cdUserId_in"
   setAttributeMapping transformation, 4, conCreateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 5, conUpdateUser, "cdUserId_in"
   setAttributeMapping transformation, 6, conLastUpdateTimestamp, "v_createProdTs"
   setAttributeMapping transformation, 7, conVersionId, "1"

   genTransformedAttrListForEntity g_classIndexGeneralSettings, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomValueNonLrt Or edomDefaultValue
 
   Print #fileNo, addTab(1); ");"
 
   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   If useGenWorkspaceParams Then
     genGenWorkspacesInWorkDataPoolsDdl fileNo, 1, ddlType, "v_psOidNew", "v_stmntTxt", "v_stmnt", "gwspError_out", "gwspInfo_out", "gwspWarning_out"
   Else
     genGenWorkspacesInWorkDataPoolsDdl fileNo, 1, ddlType, "v_psOidNew", "v_stmntTxt", "v_stmnt", "v_gwspError", "v_gwspInfo", "v_gwspWarning"
   End If

   genProcSectionHeader fileNo, "initialize PS-related data in table """ & g_qualTabNameRegistryStatic & """"
   Print #fileNo, addTab(1); "CALL "; qualProcNameRegStaticInit; "(NULL, v_psOidNew, NULL, v_rowCount);"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 End Sub
 
 ' ### ENDIF IVK ###
