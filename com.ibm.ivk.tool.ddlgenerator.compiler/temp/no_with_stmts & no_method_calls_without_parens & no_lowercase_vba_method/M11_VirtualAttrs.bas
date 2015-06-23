 Attribute VB_Name = "M11_VirtualAttrs"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 2
 
 
 Sub genVirtAttrSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If Not supportVirtualColumns Then
     Exit Sub
   End If

   If ddlType = edtPdm Then
     genVirtAttrSupportDdlByType(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportUpdates Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             genVirtAttrSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
           End If
         Next thisOrgIndex
       End If
     Next thisPoolIndex
   End If
 End Sub
 
 
 Private Sub genVirtAttrSupportDdlByType( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStep, ddlType, , , , phaseVirtAttr)
 
   ' ####################################################################################################################
   ' #    SP for Synchronizing Virtual Attributes
   ' ####################################################################################################################

   Dim qualProcNameVaSync As String
   qualProcNameVaSync = genQualProcName(g_sectionIndexDbAdmin, spnVirtAttrSync, ddlType)
   Dim unqualProcNameVaSync As String
   unqualProcNameVaSync = getUnqualObjName(qualProcNameVaSync)

   printSectionHeader("SP for Synchronizing Virtual Attributes", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameVaSync
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to synchronize virtual attributes for")
   genProcParm(fileNo, "OUT", "orgCount_out", "INTEGER", True, "number of organizations processed")
   genProcParm(fileNo, "OUT", "poolCount_out", "INTEGER", True, "number of data pools processed")
   genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", True, "number of table columns processed")
   genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_colCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_valCount", "BIGINT", "NULL")
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, , "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out")

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET orgCount_out  = 0;"
   Print #fileNo, addTab(1); "SET poolCount_out = 0;"
   Print #fileNo, addTab(1); "SET colCount_out  = 0;"
   Print #fileNo, addTab(1); "SET valCount_out  = 0;"

   genProcSectionHeader(fileNo, "loop over all 'matching' organizations")
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

   genProcSectionHeader(fileNo, "loop over all data pools of organization", 2, True)
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
                             "RIGHT(DIGITS(orgId),2) || RIGHT(DIGITS(poolId),1) || '."; unqualProcNameVaSync; "(?,?)';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_colCount,"
   Print #fileNo, addTab(4); "v_valCount"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader(fileNo, "accumulate counter values", 3)
   Print #fileNo, addTab(3); "SET poolCount_out = poolCount_out + 1;"
   Print #fileNo, addTab(3); "SET colCount_out  = colCount_out + v_colCount;"
   Print #fileNo, addTab(3); "SET valCount_out  = valCount_out + v_valCount;"

   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader(fileNo, "accumulate counter values", 2)
   Print #fileNo, addTab(2); "SET orgCount_out = orgCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, , "orgId_in", "orgCount_out", "poolCount_out", "colCount_out", "valCount_out")

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
 
 
 Private Sub genVirtAttrSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseVirtAttr)
 
   ' ####################################################################################################################
   ' #    SP for Synchronizing Virtual Attributes
   ' ####################################################################################################################

   Dim qualProcNameVaSync As String
   qualProcNameVaSync = genQualProcName(g_sectionIndexDbAdmin, spnVirtAttrSync, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for Synchronizing Virtual Attributes", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameVaSync
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", True, "number of columns synchronized")
   genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_colCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_valCount", "BIGINT", "NULL")
   genSpLogDecl(fileNo)
   genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, , "colCount_out", "valCount_out")

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET colCount_out = 0;"
   Print #fileNo, addTab(1); "SET valCount_out = 0;"

   genProcSectionHeader(fileNo, "loop over all 'virtual attributes' (organization " & genOrgId(thisOrgIndex, ddlType, True) & " / data pool " & g_pools.descriptors(thisPoolIndex).id & ")")
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_EntityName"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "entitySection,"
   Print #fileNo, addTab(3); "entityName,"
   Print #fileNo, addTab(3); "entityType,"
   Print #fileNo, addTab(3); "isTv"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("

   Print #fileNo, addTab(3); "SELECT DISTINCT"
   Print #fileNo, addTab(4); "E."; g_anAcmOrParEntitySection; ","
   Print #fileNo, addTab(4); "E."; g_anAcmOrParEntityName; ","
   Print #fileNo, addTab(4); "E."; g_anAcmOrParEntityType; ","
   Print #fileNo, addTab(4); "A."; g_anAcmIsTv
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmAttribute; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = E."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = E."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = E."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A."; g_anAcmIsVirtual; " = "; gc_dbTrue
 
   Print #fileNo, addTab(2); ")"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(2); "FROM"

   Print #fileNo, addTab(3); "V_EntityName A"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A.isTv = L."; g_anLdmIsGen

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((P."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType); ") OR (P."; g_anPoolTypeId; " IS NULL))"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnVirtAttrSync & "_"); "' || c_tableName || '(?, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_colCount,"
   Print #fileNo, addTab(3); "v_valCount"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader(fileNo, "accumulate counter values", 2)
   Print #fileNo, addTab(2); "SET colCount_out = colCount_out + v_colCount;"
   Print #fileNo, addTab(2); "SET valCount_out = valCount_out + v_valCount;"

   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, , "colCount_out", "valCount_out")

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
 
 Sub genVirtAttrSupportForEntity( _
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
   If Not supportVirtualColumns Then
     Exit Sub
   End If

   Dim sectionName As String
   Dim entityName As String
   Dim entityShortName As String
   Dim entityTypeDescr As String
   Dim hasVirtualAttrs As Boolean
   Dim hasExpBasedVirtualAttrs As Boolean
   Dim hasRelBasedVirtualAttrs As Boolean
   Dim supportMqt As Boolean
   Dim attrRefsInclSubClasses As AttrDescriptorRefs
 
   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       supportMqt = generateLrt And useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt

       hasExpBasedVirtualAttrs = Not forNl And ((forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInGenInclSubClasses) Or (Not forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses))
       hasRelBasedVirtualAttrs = Not forNl And ((forGen And g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInGenInclSubClasses) Or (Not forGen And g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInNonGenInclSubClasses))
       hasVirtualAttrs = hasExpBasedVirtualAttrs Or hasRelBasedVirtualAttrs
       attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).attrRefsInclSubClassesWithRepeat
   ElseIf acmEntityType = eactRelationship Then
     Exit Sub
   Else
     Exit Sub
   End If

   If Not hasVirtualAttrs Then
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

   ' ####################################################################################################################
   ' #    SP for syncing Virtual Attributes
   ' ####################################################################################################################

   Dim unqualTabName As String
   unqualTabName = getUnqualObjName(qualTabName)

   Dim qualProcNameVaSync As String
   qualProcNameVaSync = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , forNl, spnVirtAttrSync)

   printSectionHeader("SP for synchronizing Virtual Attributes " & entityTypeDescr & " """ & sectionName & "." & entityName & """" & IIf(forGen, " (GEN)", ""), fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameVaSync
   Print #fileNo, addTab(0); "("

   If virtualColumnSyncCommitCount > 0 Then
     genProcParm(fileNo, "IN", "commitCount_in", "INTEGER", True, "commit after this number of updates")
   End If

   genProcParm(fileNo, "OUT", "colCount_out", "INTEGER", True, "number of table columns synchronized")
   genProcParm(fileNo, "OUT", "valCount_out", "BIGINT", False, "number of values updated")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_valCount", "INTEGER", "NULL")
   If virtualColumnSyncCommitCount > 0 Then
     genVarDecl(fileNo, "v_commitCount", "INTEGER", "100000")
     genVarDecl(fileNo, "v_rowStart", "BIGINT", "1")
     genVarDecl(fileNo, "v_rowEnd", "BIGINT", "100000")
     genVarDecl(fileNo, "v_maxRow", "BIGINT", "NULL")
   End If
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameVaSync, ddlType, , "colCount_out", "valCount_out")

   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET colCount_out = 0;"
   Print #fileNo, addTab(1); "SET valCount_out = 0;"

   If virtualColumnSyncCommitCount > 0 Then
     genProcSectionHeader(fileNo, "determine number of rows")
     Print #fileNo, addTab(1); "SET v_maxRow = (SELECT COUNT(1) FROM "; qualTabName; ");"
   End If

   If hasRelBasedVirtualAttrs Then
     Dim colonMissing As Boolean
     Dim relIndex As Integer
     Dim relNavDirection As RelNavigationDirection
     Dim sourceClassIndex As Integer
     Dim sourceOrParClassIndex As Integer
     Dim targetOrParClassIndex As Integer
     Dim virtAttrlist As String
     Dim numVirtAttrs As Integer
     Dim updateFromPriv As Boolean
     Dim offset As Integer

     virtAttrlist = ""
     numVirtAttrs = 0
     Dim i As Integer
     For i = 1 To attrRefsInclSubClasses.numDescriptors
       If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
           If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forGen) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
             virtAttrlist = virtAttrlist & ", " & g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).dbColName(ddlType)
             numVirtAttrs = numVirtAttrs + 1

             ' todo: this only works as long as all virtual columns in a table refer to the same reference-table
             relIndex = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.relIndex
             relNavDirection = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.navDirection
             sourceClassIndex = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).acmEntityIndex
             sourceOrParClassIndex = g_classes.descriptors(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).acmEntityIndex).orMappingSuperClassIndex
             targetOrParClassIndex = g_classes.descriptors(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.targetClassIndex).orMappingSuperClassIndex
           End If
       End If
     Next i

     Dim fkAttrName As String
       fkAttrName = IIf(relNavDirection = etLeft, g_relationships.descriptors(relIndex).rightFkColName(ddlType), g_relationships.descriptors(relIndex).leftFkColName(ddlType))

     Print #fileNo,
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt); " T"
 
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(1); "("

     colonMissing = False
     For i = 1 To attrRefsInclSubClasses.numDescriptors
       If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
           If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forGen) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
             If colonMissing Then
               Print #fileNo, ","
             End If
             Print #fileNo, addTab(2); g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).dbColName(ddlType);
             colonMissing = True
           End If
       End If
     Next i
     Print #fileNo, ""

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"

     colonMissing = False
     For i = 1 To attrRefsInclSubClasses.numDescriptors
       If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
           If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forGen) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
             If colonMissing Then
               Print #fileNo, ","
             End If
             Print #fileNo, addTab(3); "COALESCE(S."; genAttrName(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.mapTo & IIf(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).valueType = eavtEnum, gc_enumAttrNameSuffix, ""), ddlType); ", T."; genAttrName(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).attributeName & IIf(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).valueType = eavtEnum, gc_enumAttrNameSuffix, ""), ddlType); ")";
             colonMissing = True
           End If
       End If
     Next i
     Print #fileNo, ""

     Print #fileNo, addTab(2); "FROM"
     If forGen Then
       Print #fileNo, addTab(3); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, updateFromPriv); " S"
       Print #fileNo, addTab(2); "INNER JOIN"

       If forLrt Then
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT "; g_anOid; ", "; fkAttrName; " FROM "; _
                                   genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " "; _
                                   "WHERE "; g_anIsDeleted; " = "; gc_dbFalse
         Print #fileNo, addTab(5); "UNION ALL"
         Print #fileNo, addTab(4); "SELECT "; g_anOid; ", "; fkAttrName; " FROM "; _
                                   genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " "; _
                                   "WHERE "; g_anLrtState; " <> "; CStr(lrtStatusDeleted)
         Print #fileNo, addTab(3); ") TPar"
       Else
         Print #fileNo, addTab(3); genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " TPar"
       End If

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "TPar."; fkAttrName; " = S."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName)

       If Not forLrt Then
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "TPar."; g_anIsDeleted; " = "; gc_dbFalse
       End If
     Else
       Print #fileNo, addTab(3); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " S"
     End If

     Print #fileNo, addTab(2); "WHERE"

     If forGen Then
       Print #fileNo, addTab(3); "T."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); " = TPar."; g_anOid
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "S."; g_anValidFrom; " <= T."; g_anValidFrom
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "S."; g_anValidTo; " >= T."; g_anValidFrom
     Else
         Print #fileNo, addTab(3); "T."; fkAttrName; " = S."; g_anOid
     End If

     Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY"
     Print #fileNo, addTab(1); ")"

     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"

     If forGen Then
       Print #fileNo, addTab(4); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, updateFromPriv); " S"
       Print #fileNo, addTab(3); "INNER JOIN"

       If forLrt Then
         Print #fileNo, addTab(4); "("
         Print #fileNo, addTab(5); "SELECT "; g_anOid; ", "; fkAttrName; " FROM "; _
                                   genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " "; _
                                   "WHERE "; g_anIsDeleted; " = "; gc_dbFalse
         Print #fileNo, addTab(6); "UNION ALL"
         Print #fileNo, addTab(5); "SELECT "; g_anOid; ", "; fkAttrName; " FROM "; _
                                   genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " "; _
                                   "WHERE "; g_anLrtState; " <> "; CStr(lrtStatusDeleted)
         Print #fileNo, addTab(4); ") TPar"
       Else
         Print #fileNo, addTab(4); genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " TPar"
       End If

       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "TPar."; fkAttrName; " = S."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName)
       If Not forLrt Then
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "TPar."; g_anIsDeleted; " = "; gc_dbFalse
       End If
     Else
       Print #fileNo, addTab(4); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " S"
     End If

     Print #fileNo, addTab(3); "WHERE"

     If forGen Then
       Print #fileNo, addTab(4); "T."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); " = TPar."; g_anOid
     Else
         Print #fileNo, addTab(4); "T."; fkAttrName; " = S."; g_anOid
     End If

     Print #fileNo, addTab(2); ")"

       If Not g_classes.descriptors(sourceClassIndex).hasOwnTable Then
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "T."; g_anCid; " IN ("; g_classes.descriptors(sourceClassIndex).subclassIdStrListNonAbstract; ")"
       End If

     Print #fileNo, addTab(1); ";"

     Print #fileNo,
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_valCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET valCount_out = valCount_out + v_valCount;"
     Print #fileNo, addTab(1); "SET colCount_out = colCount_out + "; CStr(numVirtAttrs); ";"
   End If

   If hasExpBasedVirtualAttrs Then
     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 0)
     transformation.doCollectVirtualAttrDescriptors = True
     transformation.doCollectAttrDescriptors = True
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & UCase(g_anInLrt), ""))

     genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomNone)

     Dim invalidColValue As String
     Dim k As Integer
     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
             If (g_domains.descriptors(tabColumns.descriptors(k).dbDomainIndex).dataType = etChar Or g_domains.descriptors(tabColumns.descriptors(k).dbDomainIndex).dataType = etClob Or g_domains.descriptors(tabColumns.descriptors(k).dbDomainIndex).dataType = etLongVarchar Or g_domains.descriptors(tabColumns.descriptors(k).dbDomainIndex).dataType = etVarchar) Then
               ' this is a hack which works for string columns / need to add logic if we have virtual columns with other data types
               invalidColValue = "''"
             End If

           If virtualColumnSyncCommitCount > 0 Then
             genProcSectionHeader(fileNo, "loop over table and update column")
             Print #fileNo, addTab(1); "WHILE v_rowStart <= v_maxRow DO"

             genProcSectionHeader(fileNo, "update virtual column """ & UCase(tabColumns.descriptors(k).columnName) & """ in ""commit window"" of table""" & qualTabName & """", 2)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "T."; g_anOid; ","
             Print #fileNo, addTab(4); "T."; tabColumns.descriptors(k).columnName; ","
             Print #fileNo, addTab(4); "ROWNUMBER() OVER (ORDER BY T."; g_anOid; " ASC)"
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabName; " T"
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "AS"
             Print #fileNo, addTab(3); "V"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); g_anOid; ","
             Print #fileNo, addTab(3); tabColumns.descriptors(k).columnName; ","
             Print #fileNo, addTab(3); "ROWNUM"
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "V."; tabColumns.descriptors(k).columnName; " = "; transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual)
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "COALESCE(V."; tabColumns.descriptors(k).columnName; ","; invalidColValue; ") <> COALESCE("; _
                                       transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual); ","; invalidColValue; ")"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "V.ROWNUM BETWEEN v_rowStart AND v_rowEnd"
             Print #fileNo, addTab(2); ";"

             genProcSectionHeader(fileNo, "determine next ""commit window""", 2)
             Print #fileNo, addTab(2); "SET v_rowStart = v_rowEnd + 1;"
             Print #fileNo, addTab(2); "SET v_rowEnd = v_rowStart + v_commitCount - 1;"

             Print #fileNo, addTab(1); "END WHILE;"
           Else
             genProcSectionHeader(fileNo, "update virtual column """ & UCase(tabColumns.descriptors(k).columnName) & """ in table""" & qualTabName & """")
             Print #fileNo, addTab(1); "UPDATE"
             Print #fileNo, addTab(2); qualTabName; " T"
             Print #fileNo, addTab(1); "SET"
             Print #fileNo, addTab(2); "T."; tabColumns.descriptors(k).columnName; " = "; transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual)
             Print #fileNo, addTab(1); "WHERE"
             Print #fileNo, addTab(2); "COALESCE(T."; tabColumns.descriptors(k).columnName; ","; invalidColValue; ") <> COALESCE("; _
                                       transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual); ","; invalidColValue; ")"
             Print #fileNo, addTab(1); ";"

             Print #fileNo,
             Print #fileNo, addTab(1); "GET DIAGNOSTICS v_valCount = ROW_COUNT;"
             Print #fileNo, addTab(1); "SET valCount_out = valCount_out + v_valCount;"
             Print #fileNo, addTab(1); "SET colCount_out = colCount_out + 1;"

           End If
         End If
     Next k
   End If

   genSpLogProcExit(fileNo, qualProcNameVaSync, ddlType, , "colCount_out", "valCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 End Sub
 
 ' ### ENDIF IVK ###
 
