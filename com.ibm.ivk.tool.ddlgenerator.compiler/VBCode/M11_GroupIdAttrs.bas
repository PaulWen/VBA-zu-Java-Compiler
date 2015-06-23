Attribute VB_Name = "M11_GroupIdAttrs"
' ### IF IVK ###

Option Explicit

Private Const processingStep = 2

Global Const tempTabNameGroupIdVals = "SESSION.GroupIdVals"
Global Const tempTabNameGroupIdOidMap = "SESSION.GroupIdOidMap"


Sub genGroupIdSupportDdl( _
  ddlType As DdlTypeId _
)
  Dim thisOrgIndex As Integer
  Dim thisPoolIndex As Integer
  
  If Not supportGroupIdColumns Then
    Exit Sub
  End If
  
  If ddlType = edtLdm Then
    genGroupIdSupportDdlByType edtLdm
    
    genGroupIdSupportDdlByPool
  ElseIf ddlType = edtPdm Then
    genGroupIdSupportDdlByType edtPdm
    
    For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
      If g_pools.descriptors(thisPoolIndex).supportUpdates Then
        For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
          If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
            genGroupIdSupportDdlByPool thisOrgIndex, thisPoolIndex, edtPdm
          End If
        Next thisOrgIndex
      End If
    Next thisPoolIndex
  End If
End Sub


Private Sub genGroupIdSupportDdlByType( _
  Optional ddlType As DdlTypeId = edtLdm _
)
  On Error GoTo ErrorExit
  
  Dim thisOrgId As Integer
  Dim thisPoolId As Integer
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStep, ddlType, , , , phaseGroupId)

  ' ####################################################################################################################
  ' #    SP for Synchronizing Group-ID Attributes
  ' ####################################################################################################################
    
  Dim qualProcNameGaSync As String
  qualProcNameGaSync = genQualProcName(g_sectionIndexDbAdmin, spnGroupIdSync, ddlType)
  Dim unqualProcNameGaSync As String
  unqualProcNameGaSync = getUnqualObjName(qualProcNameGaSync)
  
  printSectionHeader "SP for Synchronizing Group-ID Attributes", fileNo
  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcNameGaSync
  Print #fileNo, addTab(0); "("
    
  genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to synchronize groupID-attributes for"
  genProcParm fileNo, "OUT", "orgCount_out", "INTEGER", True, "number of organizations processed"
  genProcParm fileNo, "OUT", "poolCount_out", "INTEGER", True, "number of data pools processed"
  genProcParm fileNo, "OUT", "colCount_out", "INTEGER", True, "number of table columns processed"
  genProcParm fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated"
  
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "BEGIN"
  
  genProcSectionHeader fileNo, "declare variables", , True
  genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
  genVarDecl fileNo, "v_colCount", "INTEGER", "NULL"
  genVarDecl fileNo, "v_valCount", "BIGINT", "NULL"
  genSpLogDecl fileNo
  
  genSpLogProcEnter fileNo, qualProcNameGaSync, ddlType, , "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out"
  
  genProcSectionHeader fileNo, "initialize output variables"
  Print #fileNo, addTab(1); "SET orgCount_out  = 0;"
  Print #fileNo, addTab(1); "SET poolCount_out = 0;"
  Print #fileNo, addTab(1); "SET colCount_out  = 0;"
  Print #fileNo, addTab(1); "SET valCount_out  = 0;"
  
  genProcSectionHeader fileNo, "loop over all 'matching' organizations"
  Print #fileNo, addTab(1); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "O.ID AS orgId,"
  Print #fileNo, addTab(3); "O.ORGOID AS orgOid"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " O"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "orgId_in IS NULL"
  Print #fileNo, addTab(4); "OR"
  Print #fileNo, addTab(3); "O.ID = orgId_in"
  Print #fileNo, addTab(2); "ORDER BY"
  Print #fileNo, addTab(3); "O.ID"
  Print #fileNo, addTab(2); "FOR READ ONLY"
  Print #fileNo, addTab(1); "DO"
 
  genProcSectionHeader fileNo, "loop over all data pools of organization", 2, True
  Print #fileNo, addTab(2); "FOR poolLoop AS poolCursor CURSOR WITH HOLD FOR"
  Print #fileNo, addTab(3); "SELECT DISTINCT"
  Print #fileNo, addTab(4); "D."; g_anAccessModeId; " AS poolId"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); g_qualTabNameDataPool; " D"
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "D.DPOORG_OID = orgOid"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "D."; g_anAccessModeId; " IN ("; CStr(g_workDataPoolId); ","; CStr(g_productiveDataPoolId); ")"
  Print #fileNo, addTab(3); "ORDER BY"
  Print #fileNo, addTab(4); "D."; g_anAccessModeId; ""
  Print #fileNo, addTab(3); "FOR READ ONLY"
  Print #fileNo, addTab(2); "DO"
  
  Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; g_schemaNameCtoDbAdmin; "' || "; _
                            "RIGHT(DIGITS(orgId),2) || RIGHT(DIGITS(poolId),1) || '."; unqualProcNameGaSync; "(?,?)';"
  Print #fileNo,
  Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
  Print #fileNo,
  Print #fileNo, addTab(3); "EXECUTE"
  Print #fileNo, addTab(4); "v_stmnt"
  Print #fileNo, addTab(3); "INTO"
  Print #fileNo, addTab(4); "v_colCount,"
  Print #fileNo, addTab(4); "v_valCount"
  Print #fileNo, addTab(3); ";"
  
  genProcSectionHeader fileNo, "accumulate counter values", 3
  Print #fileNo, addTab(3); "SET poolCount_out = poolCount_out + 1;"
  Print #fileNo, addTab(3); "SET colCount_out  = colCount_out + v_colCount;"
  Print #fileNo, addTab(3); "SET valCount_out  = valCount_out + v_valCount;"
  
  Print #fileNo, addTab(2); "END FOR;"
  
  genProcSectionHeader fileNo, "accumulate counter values", 2
  Print #fileNo, addTab(2); "SET orgCount_out = orgCount_out + 1;"
  Print #fileNo, addTab(1); "END FOR;"
  
  genSpLogProcExit fileNo, qualProcNameGaSync, ddlType, , "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out"
  
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


Private Sub genGroupIdSupportDdlByPool( _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1, _
  Optional ddlType As DdlTypeId = edtLdm _
)
  If generateFwkTest Then
    Exit Sub
  End If
  
  On Error GoTo ErrorExit
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseGroupId)

  ' ####################################################################################################################
  ' #    SP for Synchronizing Group-ID Attributes
  ' ####################################################################################################################
    
  Dim qualProcNameGaSync As String
  qualProcNameGaSync = genQualProcName(g_sectionIndexDbAdmin, spnGroupIdSync, ddlType, thisOrgIndex, thisPoolIndex)
  
  printSectionHeader "SP for Synchronizing Group-ID Attributes", fileNo
  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcNameGaSync
  Print #fileNo, addTab(0); "("
    
  genProcParm fileNo, "OUT", "colCount_out", "INTEGER", True, "number of columns synchronized"
  genProcParm fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated"
  
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "BEGIN"

  genProcSectionHeader fileNo, "declare variables", , True
  genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
  genVarDecl fileNo, "v_colCount", "INTEGER", "NULL"
  genVarDecl fileNo, "v_valCount", "BIGINT", "NULL"
  genSpLogDecl fileNo
  genSpLogProcEnter fileNo, qualProcNameGaSync, ddlType, , "colCount_out", "valCount_out"
  
  genProcSectionHeader fileNo, "initialize output variables"
  Print #fileNo, addTab(1); "SET colCount_out = 0;"
  Print #fileNo, addTab(1); "SET valCount_out = 0;"
  
  genProcSectionHeader fileNo, "loop over all 'groupId attributes' (organization " & genOrgId(thisOrgIndex, ddlType, True) & " / data pool " & g_pools.descriptors(thisPoolIndex).id & ")"
  Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
  
  Print #fileNo, addTab(2); "WITH"
  Print #fileNo, addTab(3); "V_EntityName"
  Print #fileNo, addTab(2); "("
  Print #fileNo, addTab(3); "entitySection,"
  Print #fileNo, addTab(3); "entityName,"
  Print #fileNo, addTab(3); "entityType"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(2); "AS"
  Print #fileNo, addTab(2); "("
  
  ' FIXME: This is correct, but map this to ACM-Meta Model
  Print #fileNo, addTab(3); "VALUES('ASPECT', 'GENERICASPECT', '"; gc_acmEntityTypeKeyClass; "')"
  Print #fileNo, addTab(2); ")"
  
  Print #fileNo, addTab(2); "SELECT"
  If ddlType = edtPdm Then
    Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
    Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
  Else
    Print #fileNo, addTab(3); "L."; g_anLdmSchemaName; ","
    Print #fileNo, addTab(3); "L."; g_anLdmSchemaName
  End If
  Print #fileNo, addTab(2); "FROM"
  
  Print #fileNo, addTab(3); "V_EntityName A"
  
  Print #fileNo, addTab(2); "INNER JOIN"
  Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
  Print #fileNo, addTab(2); "ON"
  Print #fileNo, addTab(3); "A.entityType = L."; g_anAcmEntityType
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A.entityName = L."; g_anAcmEntityName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A.entitySection = L."; g_anAcmEntitySection
  
  If ddlType = edtPdm Then
    Print #fileNo, addTab(2); "INNER JOIN"
    Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
    Print #fileNo, addTab(2); "ON"
    Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
    Print #fileNo, addTab(4); "AND"
    Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
  End If
  
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
  If ddlType = edtPdm Then
    Print #fileNo, addTab(4); "AND"
    Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
    Print #fileNo, addTab(4); "AND"
    Print #fileNo, addTab(3); "((P."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType); ") OR (P."; g_anPoolTypeId; " IS NULL))"
  End If
  Print #fileNo, addTab(2); "FOR READ ONLY"
  
  Print #fileNo, addTab(1); "DO"
  
  Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnGroupIdSync & "_"); "' || c_TableName || '("; IIf(disableLoggingDuringSync, "1,", ""); " ?, ?)';"
  Print #fileNo,
  Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
  Print #fileNo,
  Print #fileNo, addTab(2); "EXECUTE"
  Print #fileNo, addTab(3); "v_stmnt"
  Print #fileNo, addTab(2); "INTO"
  Print #fileNo, addTab(3); "v_colCount,"
  Print #fileNo, addTab(3); "v_valCount"
  Print #fileNo, addTab(2); ";"
  
  genProcSectionHeader fileNo, "accumulate counter values", 2
  Print #fileNo, addTab(2); "SET colCount_out = colCount_out + v_colCount;"
  Print #fileNo, addTab(2); "SET valCount_out = valCount_out + v_valCount;"
  
  Print #fileNo, addTab(1); "END FOR;"
  
  genSpLogProcExit fileNo, qualProcNameGaSync, ddlType, , "colCount_out", "valCount_out"
  
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


Sub genGroupIdSupportForEntity( _
  ByRef acmEntityIndex As Integer, _
  ByRef acmEntityType As AcmAttrContainerType, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forGen As Boolean = False, _
  Optional forLrt As Boolean = False, _
  Optional forNl As Boolean = False _
)
  If Not supportGroupIdColumns Or forLrt Then
    Exit Sub
  End If
  
  Dim sectionName As String
  Dim sectionShortName As String
  Dim entityName As String
  Dim entityShortName As String
  Dim entityTypeDescr As String
  Dim hasGroupIdAttrs As Boolean
  Dim groupIdAttrIndexes() As Integer
  Dim isPsTagged As Boolean
  Dim supportMqt As Boolean
  
  ReDim groupIdAttrIndexes(0 To 0)
  
  If acmEntityType = eactClass Then
    With g_classes.descriptors(acmEntityIndex)
      sectionName = .sectionName
      sectionShortName = .sectionShortName
      entityName = .className
      entityShortName = .shortName
      entityTypeDescr = "ACM-Class"
      isPsTagged = .isPsTagged
      supportMqt = generateLrt And useMqtToImplementLrt And .useMqtToImplementLrt
      
      hasGroupIdAttrs = Not forNl And Not forGen And .hasGroupIdAttrInNonGenInclSubClasses
      If hasGroupIdAttrs Then
        groupIdAttrIndexes = .groupIdAttrIndexesInclSubclasses
      End If
      
    End With
  ElseIf acmEntityType = eactRelationship Then
    Exit Sub
  Else
    Exit Sub
  End If
  
  If Not hasGroupIdAttrs Then
    Exit Sub
  End If
  
  Dim transformation As AttributeListTransformation
  Dim tabColumns As EntityColumnDescriptors
  
  Dim qualTabName As String
  qualTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
  Dim qualTabNameMqt As String
  If supportMqt Then
    qualTabNameMqt = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True)
  End If
  
  Dim targetTabVar As String
  Dim sourceTabVar As String
  Dim gidTabVar As String
  Dim crTabVar As String
  Dim gidColName As String
  Dim gidColShortName As String
     Dim subClassIdStrList(5) As String  '5 should be enough
  Dim qualSeqNameGroupId As String
  Dim expGroupIdColNo As Integer
  
  ' ####################################################################################################################
  ' #    SP for syncing Group-ID Attributes
  ' ####################################################################################################################
  
  Dim qualProcNameGaSync As String
  qualProcNameGaSync = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, spnGroupIdSync)

  Dim usePsOidFilter As Boolean
  Dim i As Integer
  For i = 1 To 2
    usePsOidFilter = (i = 2)

    printSectionHeader "SP for synchronizing Group-ID Attributes " & entityTypeDescr & " """ & sectionName & "." & entityName & """" & IIf(forGen, " (GEN)", ""), fileNo
    
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcNameGaSync
    Print #fileNo, addTab(0); "("

    If usePsOidFilter Then
      genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure"
    End If

    If disableLoggingDuringSync Then
      genProcParm fileNo, "IN", "useCommitCount_in", g_dbtBoolean, True, "iff '1': commit 'in between'"
    End If
    
    genProcParm fileNo, "OUT", "colCount_out", "INTEGER", True, "number of table columns synchronized"
    genProcParm fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated"
    
    Print #fileNo, addTab(0); ")"
    Print #fileNo, addTab(0); "RESULT SETS 0"
    Print #fileNo, addTab(0); "LANGUAGE SQL"
    Print #fileNo, addTab(0); "BEGIN"
    
    genProcSectionHeader fileNo, "declare conditions", , True
    genCondDecl fileNo, "notFound", "02000"
  
    genProcSectionHeader fileNo, "declare variables"
    genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
    genVarDecl fileNo, "v_valCount", "INTEGER", "0"
    genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
    
    genVarDecl fileNo, "v_commitCount", "INTEGER", "1000"
    genVarDecl fileNo, "v_loopCount", "INTEGER", "0"
    Print #fileNo,
    
    tabColumns = nullEntityColumnDescriptors
    initAttributeTransformation transformation, 0
    transformation.doCollectVirtualAttrDescriptors = True
    transformation.doCollectAttrDescriptors = True
    setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & UCase(g_anInLrt), "")
    
    genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomNone
  
    Dim effectiveMaxLength As String
    Dim foundDomain As Boolean
    Dim varNamePrefix1 As String
    Dim varNamePrefix2 As String
    Dim k As Integer
    For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
      With g_attributes.descriptors(groupIdAttrIndexes(k))
        gidColName = genAttrName(.attributeName, ddlType)
        gidColShortName = .shortName
        varNamePrefix1 = "v_" & UCase(entityShortName) & "_" & UCase(gidColShortName) & "_"
        varNamePrefix2 = "v_" & UCase(gidColShortName) & "_"
        
        Print #fileNo,
        genVarDecl fileNo, "v_" & UCase(gidColShortName) & CStr(k), "BIGINT", "NULL"
        genVarDecl fileNo, "v_" & UCase(entityShortName) & "_OID" & CStr(k), g_dbtOid, "NULL"
        genVarDecl fileNo, "v_" & UCase(entityShortName) & "_" & UCase(gidColName) & CStr(k), "BIGINT", "NULL"
        
        Print #fileNo,
        
        If isPsTagged Then
          genVarDecl fileNo, varNamePrefix1 & conPsOid & CStr(k), g_dbtOid, "NULL"
        End If
        
        expGroupIdColNo = 0
        Dim l As Integer
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          Dim m As Integer
          For m = 1 To tabColumns.numDescriptors
            If tabColumns.descriptors(m).columnName = UCase(.groupIdAttributes(l)) Then
              If tabColumns.descriptors(m).acmAttributeIndex > 0 Then
                With g_domains.descriptors(g_attributes.descriptors(tabColumns.descriptors(m).acmAttributeIndex).domainIndex)
                  If .maxLength = "" Then
                    effectiveMaxLength = ""
                  Else
                    If supportUnicode And .supportUnicode Then
                      effectiveMaxLength = CInt(.unicodeExpansionFactor * CInt(.maxLength)) & ""
                    Else
                      effectiveMaxLength = .maxLength
                    End If
                  End If
                  
                  genVarDecl fileNo, varNamePrefix1 & tabColumns.descriptors(m).columnName & CStr(k), getDataType(.dataType, effectiveMaxLength, .scale), "NULL"
                End With
                GoTo exitM
              End If
            End If
          Next m
          If Left(.groupIdAttributes(l), 1) = "#" Then
            ' we currently only support exressions of type BIGINT
            expGroupIdColNo = expGroupIdColNo + 1
            genVarDecl fileNo, varNamePrefix1 & "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo), "BIGINT", "NULL"
          ElseIf InStr(1, UCase(.groupIdAttributes(l)), "VALID") > 0 Then
            genVarDecl fileNo, varNamePrefix1 & UCase(.groupIdAttributes(l)) & CStr(k), "DATE", "NULL"
          Else
            genVarDecl fileNo, varNamePrefix1 & UCase(.groupIdAttributes(l)) & CStr(k), "BIGINT", "NULL"
          End If
exitM:
        Next l
      
        Print #fileNo,
        If isPsTagged Then
          genVarDecl fileNo, varNamePrefix2 & conPsOid & CStr(k), g_dbtOid, "NULL"
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          For m = 1 To tabColumns.numDescriptors
            If tabColumns.descriptors(m).columnName = UCase(.groupIdAttributes(l)) Then
              If tabColumns.descriptors(m).acmAttributeIndex > 0 Then
                With g_domains.descriptors(g_attributes.descriptors(tabColumns.descriptors(m).acmAttributeIndex).domainIndex)
                  If .maxLength = "" Then
                    effectiveMaxLength = ""
                  Else
                    If supportUnicode And .supportUnicode Then
                      effectiveMaxLength = CInt(.unicodeExpansionFactor * CInt(.maxLength)) & ""
                    Else
                      effectiveMaxLength = .maxLength
                    End If
                  End If
                  
                  genVarDecl fileNo, varNamePrefix2 & tabColumns.descriptors(m).columnName & CStr(k), getDataType(.dataType, effectiveMaxLength, .scale), "NULL"
                End With
                GoTo exitMM
              End If
            End If
          Next m
          If Left(.groupIdAttributes(l), 1) = "#" Then
            ' we currently only support exressions of type BIGINT
            expGroupIdColNo = expGroupIdColNo + 1
            genVarDecl fileNo, varNamePrefix2 & "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo), "BIGINT", "NULL"
          ElseIf InStr(1, UCase(.groupIdAttributes(l)), "VALID") > 0 Then
            genVarDecl fileNo, varNamePrefix2 & UCase(.groupIdAttributes(l)) & CStr(k), "DATE", "NULL"
          Else
            genVarDecl fileNo, varNamePrefix2 & UCase(.groupIdAttributes(l)) & CStr(k), "BIGINT", "NULL"
          End If
exitMM:
        Next l
      
      End With
    Next k
    
    genSpLogDecl fileNo
    
    genProcSectionHeader fileNo, "declare cursors"
    For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
      With g_attributes.descriptors(groupIdAttrIndexes(k))
        gidColName = genAttrName(.attributeName, ddlType)
        gidColShortName = .shortName
        subClassIdStrList(k) = g_classes.descriptors(.acmEntityIndex).subclassIdStrListNonAbstract
        qualSeqNameGroupId = _
          genQualObjName( _
            getSectionIndexByName(.sectionName), _
            "SEQ_" & entityShortName & .shortName, _
            "SEQ_" & entityShortName & .shortName, _
            ddlType, thisOrgIndex)
    
        Print #fileNo,
        Print #fileNo, addTab(1); "DECLARE "; LCase(gidColShortName); "Cursor"; UCase(gidColShortName) & CStr(k); " CURSOR WITH HOLD FOR"
        Print #fileNo, addTab(2); "WITH"
        Print #fileNo, addTab(3); "V"
        Print #fileNo, addTab(2); "AS"
        Print #fileNo, addTab(2); "("
        Print #fileNo, addTab(3); "SELECT DISTINCT"
        If isPsTagged Then
          Print #fileNo, addTab(4); g_anPsOid; ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(4); mapExpression(.groupIdAttributes(l), thisOrgIndex, thisPoolIndex, ddlType); " AS EXP_"; CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(4); UCase(.groupIdAttributes(l)); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        
        Print #fileNo, addTab(3); "FROM"
        Print #fileNo, addTab(4); qualTabName
        Print #fileNo, addTab(3); "WHERE"
        Print #fileNo, addTab(4); g_anCid; " IN ("; subClassIdStrList(k); ")"

        If isPsTagged And usePsOidFilter Then
          Print #fileNo, addTab(5); "AND"
          Print #fileNo, addTab(4); g_anPsOid; " = psOid_in"
        End If
        
        Print #fileNo, addTab(5); "AND"
        Print #fileNo, addTab(4); "ISDELETED = 0"

        Print #fileNo, addTab(2); ")"
        Print #fileNo, addTab(2); "SELECT"
        Print #fileNo, addTab(3); "*"
        Print #fileNo, addTab(2); "FROM"
        Print #fileNo, addTab(3); "V"
        Print #fileNo, addTab(2); "ORDER BY"
        
        If isPsTagged Then
          Print #fileNo, addTab(3); g_anPsOid; ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(3); "EXP_"; CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(3); UCase(.groupIdAttributes(l)); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        
        Print #fileNo, addTab(2); "FOR READ ONLY"
        If usePsOidFilter Then
          Print #fileNo, addTab(2); "WITH UR"
        End If
        Print #fileNo, addTab(1); ";"
      
        Print #fileNo,
        Print #fileNo, addTab(1); "DECLARE tabCursor"; UCase(gidColShortName) & CStr(k); " CURSOR WITH HOLD FOR"
        Print #fileNo, addTab(2); "SELECT"
        Print #fileNo, addTab(3); g_anOid; ","
        Print #fileNo, addTab(3); gidColName; ","
        If isPsTagged Then
          Print #fileNo, addTab(3); g_anPsOid; ","
        End If
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          Print #fileNo, addTab(3); mapExpression(.groupIdAttributes(l), thisOrgIndex, thisPoolIndex, ddlType); IIf(l < UBound(.groupIdAttributes), ",", "")
        Next l
        Print #fileNo, addTab(2); "FROM"
        Print #fileNo, addTab(3); qualTabName
        Print #fileNo, addTab(2); "WHERE"
        Print #fileNo, addTab(3); g_anCid; " IN ("; subClassIdStrList(k); ")"

        If isPsTagged And usePsOidFilter Then
          Print #fileNo, addTab(4); "AND"
          Print #fileNo, addTab(3); g_anPsOid; " = psOid_in"
        End If

        Print #fileNo, addTab(4); "AND"
        Print #fileNo, addTab(3); "ISDELETED = 0"

        Print #fileNo, addTab(2); "ORDER BY"
        If isPsTagged Then
          Print #fileNo, addTab(3); g_anPsOid; ","
        End If
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          Print #fileNo, addTab(3); mapExpression(.groupIdAttributes(l), thisOrgIndex, thisPoolIndex, ddlType); ","
        Next l
        Print #fileNo, addTab(3); "(CASE WHEN "; UCase(gidColName); " IS NULL THEN 1 ELSE 0 END)"
        
        Print #fileNo, addTab(2); "FOR READ ONLY"
        If usePsOidFilter Then
          Print #fileNo, addTab(2); "WITH UR"
        End If
        Print #fileNo, addTab(1); ";"
      
      End With
    Next k
    
    genProcSectionHeader fileNo, "declare condition handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
    Print #fileNo, addTab(1); "BEGIN"
    Print #fileNo, addTab(2); "SET v_atEnd = "; gc_dbTrue; ";"
    Print #fileNo, addTab(1); "END;"
      
    For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
      With g_attributes.descriptors(groupIdAttrIndexes(k))
        gidColName = genAttrName(.attributeName, ddlType)
        
        genProcSectionHeader fileNo, "temporary table for GroupId-OID mapping (attribute """ & UCase(.attributeName) & """)"
        Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
        Print #fileNo, addTab(1); "SESSION."; gidColName; "OidMap" & CStr(k)
        Print #fileNo, addTab(1); "("
        Print #fileNo, addTab(2); UCase(gidColName); " "; g_dbtOid; ","
        Print #fileNo, addTab(2); g_anOid; " "; g_dbtOid
        Print #fileNo, addTab(1); ")"
        genDdlForTempTableDeclTrailer fileNo, 1, True
      End With
    Next k
      
    genSpLogProcEnter fileNo, qualProcNameGaSync, ddlType, , "colCount_out", "valCount_out"
    
    genProcSectionHeader fileNo, "initialize output parameter"
    Print #fileNo, addTab(1); "SET colCount_out = 0;"
    Print #fileNo, addTab(1); "SET valCount_out = 0;"
    
    expGroupIdColNo = 0
    For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
      With g_attributes.descriptors(groupIdAttrIndexes(k))
        targetTabVar = UCase(entityShortName)
        sourceTabVar = UCase(entityShortName) & "1"
        gidColName = genAttrName(.attributeName, ddlType)
        gidColShortName = .shortName
        varNamePrefix1 = "v_" & UCase(entityShortName) & "_" & UCase(gidColShortName) & "_"
        varNamePrefix2 = "v_" & UCase(gidColShortName) & "_"
        
        Dim qualTabNameSourceDataPool As String
        qualTabNameSourceDataPool = ""
        
        If thisPoolIndex <> g_workDataPoolIndex Then
          qualTabNameSourceDataPool = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, g_workDataPoolIndex, forGen, forLrt)
  
          genProcSectionHeader fileNo, "import group-ID column """ & UCase(.attributeName) & """ in table """ & qualTabName & """ from work data pool"
        ElseIf thisOrgIndex <> g_primaryOrgIndex Then
            qualTabNameSourceDataPool = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex, forGen, forLrt)
          
            genProcSectionHeader fileNo, "import group-ID column """ & UCase(.attributeName) & """ in table """ & qualTabName & """ from factory productive data pool"
        End If
              
        If (thisPoolIndex <> g_workDataPoolIndex) Or (thisOrgIndex <> g_primaryOrgIndex) Then
          Print #fileNo, addTab(1); "UPDATE"
          Print #fileNo, addTab(2); qualTabName; " "; targetTabVar
          Print #fileNo, addTab(1); "SET"
          Print #fileNo, addTab(2); targetTabVar; "."; gidColName; " = ("
          Print #fileNo, addTab(3); "SELECT"
          Print #fileNo, addTab(4); sourceTabVar; "."; gidColName
          Print #fileNo, addTab(3); "FROM"
          Print #fileNo, addTab(4); qualTabNameSourceDataPool; " "; sourceTabVar
          Print #fileNo, addTab(3); "WHERE"
          Print #fileNo, addTab(4); targetTabVar; "."; g_anOid; " = "; sourceTabVar; "."; g_anOid
          Print #fileNo, addTab(2); ")"
          Print #fileNo, addTab(1); "WHERE"
          Print #fileNo, addTab(2); targetTabVar; "."; g_anCid; " IN ("; subClassIdStrList(k); ")"
          Print #fileNo, addTab(3); "AND"
          Print #fileNo, addTab(2); targetTabVar; "."; gidColName; " IS NULL"
          If isPsTagged And usePsOidFilter Then
            Print #fileNo, addTab(3); "AND"
            Print #fileNo, addTab(2); g_anPsOid; " = psOid_in"
          End If
          If isPsTagged And usePsOidFilter Then
            Print #fileNo, addTab(1); "WITH UR;"
          Else
            Print #fileNo, addTab(1); ";"
          End If
          
          Print #fileNo,
          Print #fileNo, addTab(1); "GET DIAGNOSTICS v_valCount = ROW_COUNT;"
          Print #fileNo, addTab(1); "SET valCount_out = valCount_out + v_valCount;"
        
          Print #fileNo,
          Print #fileNo, addTab(1); "IF useCommitCount_in = 1 THEN"
          Print #fileNo, addTab(2); "COMMIT;"
          Print #fileNo, addTab(1); "END IF;"
        End If
        
        genProcSectionHeader fileNo, "process GroupId column """ & gidColName & """" & " for classid " & subClassIdStrList(k)
        Print #fileNo, addTab(1); "SET v_atEnd = "; gc_dbFalse; ";"
        Print #fileNo, addTab(1); "OPEN "; LCase(gidColShortName); "Cursor"; UCase(gidColShortName) & CStr(k); ";"
        Print #fileNo, addTab(1); "OPEN tabCursor"; UCase(gidColShortName) & CStr(k); ";"
        
        Print #fileNo,
        Print #fileNo, addTab(1); "FETCH"
        Print #fileNo, addTab(2); LCase(gidColShortName); "Cursor"; UCase(gidColShortName) & CStr(k)
        Print #fileNo, addTab(1); "INTO"
        
        If isPsTagged Then
          Print #fileNo, addTab(2); varNamePrefix2; g_anPsOid & CStr(k) & ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(2); varNamePrefix2; "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(2); varNamePrefix2; UCase(.groupIdAttributes(l)) & CStr(k); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        Print #fileNo, addTab(1); ";"
        
        Print #fileNo,
        Print #fileNo, addTab(1); "FETCH"
        Print #fileNo, addTab(2); "tabCursor"; UCase(gidColShortName) & CStr(k)
        Print #fileNo, addTab(1); "INTO"
        
        Print #fileNo, addTab(2); "v_"; UCase(entityShortName); "_"; g_anOid & CStr(k); ","
        Print #fileNo, addTab(2); "v_"; UCase(entityShortName); "_"; gidColName & CStr(k); ","
        If isPsTagged Then
          Print #fileNo, addTab(2); varNamePrefix1; g_anPsOid & CStr(k); ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(2); varNamePrefix1; "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(2); varNamePrefix1; UCase(.groupIdAttributes(l)) & CStr(k); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        Print #fileNo, addTab(1); ";"
        
        Print #fileNo,
        Print #fileNo, addTab(1); "WHILE (v_atEnd = 0) DO"
        Print #fileNo, addTab(2); "WHILE ("
        Print #fileNo, addTab(3); "(v_atEnd = 0) AND"
        
        Dim maxVarNameLength As Integer
        ' Fixme: get rid of this hard-coding
        maxVarNameLength = 29
        If isPsTagged Then
          Print #fileNo, addTab(3); "((("; _
                                    paddRight(varNamePrefix1 & g_anPsOid & CStr(k), maxVarNameLength); " IS NULL) AND ("; _
                                    paddRight(varNamePrefix2 & g_anPsOid & CStr(k), maxVarNameLength); " IS NULL)) OR ("; _
                                    paddRight(varNamePrefix1 & g_anPsOid & CStr(k), maxVarNameLength); " =  "; _
                                    paddRight(varNamePrefix2 & g_anPsOid & CStr(k), maxVarNameLength); ")) AND"
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          Dim v1 As String
          Dim v2 As String
          
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            v1 = paddRight(varNamePrefix1 & "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo), maxVarNameLength)
            v2 = paddRight(varNamePrefix2 & "EXP" & "_" & CStr(k) & "_" & CStr(expGroupIdColNo), maxVarNameLength)
          Else
            v1 = paddRight(varNamePrefix1 & UCase(.groupIdAttributes(l)) & CStr(k), maxVarNameLength)
            v2 = paddRight(varNamePrefix2 & UCase(.groupIdAttributes(l)) & CStr(k), maxVarNameLength)
          End If
          
          Print #fileNo, addTab(3); "((("; v1; " IS NULL) AND ("; v2; " IS NULL)) OR ("; v1; " =  "; v2; "))"; IIf(l < UBound(.groupIdAttributes), " AND", "")
        Next l
        
        Print #fileNo, addTab(2); ") DO"
        
        Print #fileNo, addTab(3); "IF (v_"; UCase(gidColShortName) & CStr(k); " IS NULL) THEN"
        Print #fileNo, addTab(4); "IF (v_"; UCase(entityShortName); "_"; UCase(gidColName) & CStr(k); " IS NULL) THEN"
        Print #fileNo, addTab(5); "SET v_"; UCase(gidColShortName) & CStr(k); " = NEXTVAL FOR "; qualSeqNameGroupId; ";"
        Print #fileNo, addTab(4); "ELSE"
        Print #fileNo, addTab(5); "SET v_"; UCase(gidColShortName) & CStr(k); " = v_"; UCase(entityShortName); "_"; UCase(gidColName) & CStr(k); ";"
        Print #fileNo, addTab(4); "END IF;"
        Print #fileNo, addTab(3); "END IF;"
        Print #fileNo,
        Print #fileNo, addTab(3); "IF v_"; UCase(entityShortName); "_"; UCase(gidColName) & CStr(k); " IS NULL THEN"
        Print #fileNo, addTab(4); "INSERT INTO"
        Print #fileNo, addTab(5); "SESSION."; gidColName; "OidMap" & CStr(k)
        Print #fileNo, addTab(4); "("
        Print #fileNo, addTab(5); UCase(gidColName); ","
        Print #fileNo, addTab(5); g_anOid
        Print #fileNo, addTab(4); ")"
        Print #fileNo, addTab(4); "VALUES"
        Print #fileNo, addTab(4); "("
        Print #fileNo, addTab(5); "v_"; UCase(gidColShortName) & CStr(k); ","
        Print #fileNo, addTab(5); "v_"; UCase(entityShortName); "_OID" & CStr(k)
        Print #fileNo, addTab(4); ");"
        Print #fileNo, addTab(3); "END IF;"
        
        Print #fileNo,
        Print #fileNo, addTab(3); "FETCH"
        Print #fileNo, addTab(4); "tabCursor"; UCase(gidColShortName) & CStr(k)
        Print #fileNo, addTab(3); "INTO"
        
        Print #fileNo, addTab(4); "v_"; UCase(entityShortName); "_"; g_anOid & CStr(k); ","
        Print #fileNo, addTab(4); "v_"; UCase(entityShortName); "_"; gidColName & CStr(k); ","
        If isPsTagged Then
          Print #fileNo, addTab(4); varNamePrefix1; g_anPsOid & CStr(k); ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(4); varNamePrefix1; "EXP"; "_"; CStr(k); "_"; CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(4); varNamePrefix1; UCase(.groupIdAttributes(l)) & CStr(k); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        Print #fileNo, addTab(3); ";"
        
        Print #fileNo, addTab(2); "END WHILE;"
        
        Print #fileNo,
        Print #fileNo, addTab(2); "FETCH"
        Print #fileNo, addTab(3); LCase(gidColShortName); "Cursor"; UCase(gidColShortName) & CStr(k)
        Print #fileNo, addTab(2); "INTO"
        
        If isPsTagged Then
          Print #fileNo, addTab(3); varNamePrefix2; g_anPsOid & CStr(k) & ","
        End If
        
        expGroupIdColNo = 0
        For l = LBound(.groupIdAttributes) To UBound(.groupIdAttributes)
          If Left(.groupIdAttributes(l), 1) = "#" Then
            expGroupIdColNo = expGroupIdColNo + 1
            Print #fileNo, addTab(3); varNamePrefix2; "EXP"; "_"; CStr(k); "_"; CStr(expGroupIdColNo); IIf(l < UBound(.groupIdAttributes), ",", "")
          Else
            Print #fileNo, addTab(3); varNamePrefix2; UCase(.groupIdAttributes(l)) & CStr(k); IIf(l < UBound(.groupIdAttributes), ",", "")
          End If
        Next l
        Print #fileNo, addTab(2); ";"
        
        Print #fileNo, addTab(2); "SET v_"; UCase(gidColShortName) & CStr(k); " = NULL;"
  
        Print #fileNo, addTab(1); "END WHILE;"
        
        Print #fileNo,
        Print #fileNo, addTab(1); "CLOSE "; LCase(gidColShortName); "Cursor"; UCase(gidColShortName) & CStr(k); " WITH RELEASE;"
        Print #fileNo, addTab(1); "CLOSE tabCursor"; UCase(gidColShortName) & CStr(k); " WITH RELEASE;"
      
        genProcSectionHeader fileNo, "update column in target table"
        Print #fileNo, addTab(1); "SET v_loopCount = 0;"
        Print #fileNo, addTab(1); "FOR oidLoop AS oidCursor CURSOR WITH HOLD FOR"
        Print #fileNo, addTab(2); "SELECT"
        Print #fileNo, addTab(3); UCase(gidColName); " AS mapped"; UCase(gidColName); ","
        Print #fileNo, addTab(3); g_anOid; " AS "; LCase(g_anOid); "ToMap"
        Print #fileNo, addTab(2); "FROM"
        Print #fileNo, addTab(3); "SESSION."; gidColName; "OidMap"; CStr(k)
        Print #fileNo, addTab(2); "FOR READ ONLY"
        Print #fileNo, addTab(1); "DO"
        
        Print #fileNo, addTab(2); "UPDATE"
        Print #fileNo, addTab(3); qualTabName; " "; targetTabVar
        Print #fileNo, addTab(2); "SET"
        Print #fileNo, addTab(3); targetTabVar; "."; gidColName; " = mapped"; UCase(gidColName)
        Print #fileNo, addTab(2); "WHERE"
        Print #fileNo, addTab(3); targetTabVar; "."; g_anOid; " = "; LCase(g_anOid); "ToMap"
        Print #fileNo, addTab(4); "AND"
        Print #fileNo, addTab(3); "(("; targetTabVar; "."; gidColName; " IS NULL) OR ("; targetTabVar; "."; gidColName; " <> mapped"; UCase(gidColName); "))"
        Print #fileNo, addTab(2); ";"
    
        Print #fileNo,
        Print #fileNo, addTab(2); "GET DIAGNOSTICS v_valCount = ROW_COUNT;"
        Print #fileNo, addTab(2); "SET valCount_out = valCount_out + v_valCount;"
        Print #fileNo,
        
        Print #fileNo, addTab(2); "SET v_loopCount = v_loopCount + 1;"
  
        Print #fileNo, addTab(2); "IF (v_loopCount = v_commitCount) AND (useCommitCount_in = 1) THEN"
          genProcSectionHeader fileNo, "commit UOW", 3, True
          Print #fileNo, addTab(3); "COMMIT;"
          Print #fileNo, addTab(3); "SET v_loopCount = 0;"
        Print #fileNo, addTab(2); "END IF;"
        
        Print #fileNo, addTab(1); "END FOR;"
  
        Print #fileNo,
        Print #fileNo, addTab(1); "SET colCount_out = colCount_out + 1;"
        
        Print #fileNo,
        Print #fileNo, addTab(1); "IF useCommitCount_in = 1 THEN"
        Print #fileNo, addTab(2); "COMMIT;"
        Print #fileNo, addTab(1); "END IF;"
      End With
    Next k
    
    genSpLogProcExit fileNo, qualProcNameGaSync, ddlType, , "colCount_out", "valCount_out"
    
    Print #fileNo, addTab(0); "END"
    Print #fileNo, gc_sqlCmdDelim
  Next i
End Sub

' ### ENDIF IVK ###

